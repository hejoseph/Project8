package tourGuide.service;

import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;

import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import tourGuide.model.Attraction;
import tourGuide.model.Location;
import tourGuide.model.VisitedLocation;
import tourGuide.user.User;
import tourGuide.user.UserReward;

public class RewardsService {
    private static final double STATUTE_MILES_PER_NAUTICAL_MILE = 1.15077945;

	public GpsUtilService gpsUtilService;

	private final WebClient webClient;

	// proximity in miles
    private int defaultProximityBuffer = 10;
	private int proximityBuffer = defaultProximityBuffer;
	private int attractionProximityRange = 200;
//	private final GpsUtilCustom gpsUtilCustom;
//	private final RewardCentral rewardsCentral;

	public RestTemplate restTemplate;
	public String serviceUrl;


//	ExecutorService es = Executors.newCachedThreadPool();
	ExecutorService es = Executors.newFixedThreadPool(500);
//	ExecutorService es = new ThreadPoolExecutor(1000, 1000, 0L, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(200000));


	public RewardsService(GpsUtilService gpsUtilService) {
		this.gpsUtilService = gpsUtilService;
		this.restTemplate = new RestTemplate();
		this.serviceUrl = "http://localhost:9094";
		this.serviceUrl = serviceUrl.startsWith("http") ?
				serviceUrl : "http://" + serviceUrl;
		this.webClient = WebClient.create(this.serviceUrl);
	}
	
	public void setProximityBuffer(int proximityBuffer) {
		this.proximityBuffer = proximityBuffer;
	}
	
	public void setDefaultProximityBuffer() {
		proximityBuffer = defaultProximityBuffer;
	}

	public void calculateRewardsWithoutThread(User user){
//		StopWatch stopWatch = new StopWatch();
//		stopWatch.start();


		List<VisitedLocation> userLocations = user.getVisitedLocations().stream().collect(Collectors.toList());
		List<Attraction> attractions = gpsUtilService.getAttractions().stream().collect(Collectors.toList());

//		List<VisitedLocation> userLocations = user.getVisitedLocations();
//		List<Attraction> attractions = gpsUtilService.getAttractions();

		for(int i = 0; i<userLocations.size();i++){
			VisitedLocation visitedLocation = userLocations.get(i);
			for(Attraction attraction : attractions) {
				if(!user.hasRewardForAttraction(attraction.attractionName)){
					if(nearAttraction(visitedLocation, attraction)) {
						user.addUserReward(new UserReward(visitedLocation, attraction, getRewardPoints(attraction, user)));
					}
				}
			}
		}

//		stopWatch.stop();
//		System.out.println("Time Elapsed: " + stopWatch.getTime() + " ms.");
		user.setRewardCalled(true);
//		if(attractions.size()==0){
//			user.setRewardCalled(false);
//		}
	}


	public void reNewThreadPool(){
		es = Executors.newCachedThreadPool();
	}

	public int called = 0;

	public synchronized void increment(){
		this.called = this.called+1;
	}

	public void calculateRewardsWithExecutorService(User user) {
		//		StopWatch stopWatch = new StopWatch();
//		stopWatch.start();

//		Thread thread = new Thread(()->{

//		user.addDebug(user.getPhoneNumber());

		es.execute(new Runnable(){
			@Override
			public void run() {
				calculateRewardsWithoutThread(user);
			}
		});

//		thread.start();

//		stopWatch.stop();
//		System.out.println("Time Elapsed: " + stopWatch.getTime() + " ms.");

	}

	public boolean isWithinAttractionProximity(Attraction attraction, Location location) {
		return getDistance(attraction, location) > attractionProximityRange ? false : true;
	}
	
	private boolean nearAttraction(VisitedLocation visitedLocation, Attraction attraction) {
		return getDistance(attraction, visitedLocation.location) > proximityBuffer ? false : true;
	}
	
	public int getRewardPoints(Attraction attraction, User user) {
//		Integer point = restTemplate.getForObject(serviceUrl
//				+ "/getAttractionRewardPoints?attractionId={attractionId}&userId={userId}", Integer.class, attraction.attractionId, user.getUserId());

		Mono<Integer> stream = this.webClient
				.get()
				.uri("/getAttractionRewardPoints?attractionId={attractionId}&userId={userId}", attraction.attractionId, user.getUserId())
				.retrieve().bodyToMono(new ParameterizedTypeReference<Integer>(){});

//		return point;
		return stream.block();
//		return rewardsCentral.getAttractionRewardPoints(attraction.attractionId, user.getUserId());
	}
	
	public double getDistance(Location loc1, Location loc2) {
        double lat1 = Math.toRadians(loc1.latitude);
        double lon1 = Math.toRadians(loc1.longitude);
        double lat2 = Math.toRadians(loc2.latitude);
        double lon2 = Math.toRadians(loc2.longitude);

        double angle = Math.acos(Math.sin(lat1) * Math.sin(lat2)
                               + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2));

        double nauticalMiles = 60 * Math.toDegrees(angle);
        double statuteMiles = STATUTE_MILES_PER_NAUTICAL_MILE * nauticalMiles;
        return statuteMiles;
	}

	public boolean waitThreadToFinish(int minutes) throws InterruptedException {
		es.shutdown();
		return es.awaitTermination(minutes, TimeUnit.MINUTES);
	}
}
