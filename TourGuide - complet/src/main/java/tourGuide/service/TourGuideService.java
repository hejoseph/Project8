package tourGuide.service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import tourGuide.helper.InternalTestHelper;
import tourGuide.module.GpsUtilCustom;
import tourGuide.tracker.Tracker;
import tourGuide.user.User;
import tourGuide.user.UserReward;
import tripPricer.Provider;
import tripPricer.TripPricer;

@Service
public class TourGuideService {
	private Logger logger = LoggerFactory.getLogger(TourGuideService.class);
	private final GpsUtilCustom gpsUtilCustom;
	private final RewardsService rewardsService;
	private final TripPricer tripPricer = new TripPricer();
	public final Tracker tracker;
	boolean testMode = true;

	ExecutorService es = Executors.newCachedThreadPool();
	
	public TourGuideService(GpsUtilCustom gpsUtilCustom, RewardsService rewardsService) {
		this.gpsUtilCustom = gpsUtilCustom;
		this.rewardsService = rewardsService;
		
		if(testMode) {
			logger.info("TestMode enabled");
			logger.debug("Initializing users");
			initializeInternalUsers();
			logger.debug("Finished initializing users");
		}
		tracker = new Tracker(this);
		addShutDownHook();
	}
	
	public List<UserReward> getUserRewards(User user) {
		return user.getUserRewards();
	}
	
	public VisitedLocation getUserLocation(User user) {
		VisitedLocation visitedLocation = (user.getVisitedLocations().size() > 0) ?
			user.getLastVisitedLocation() :
			trackUserLocation(user);
		return visitedLocation;
	}
	
	public User getUser(String userName) {
		return internalUserMap.get(userName);
	}
	
	public List<User> getAllUsers() {
		return internalUserMap.values().stream().collect(Collectors.toList());
	}
	
	public void addUser(User user) {
		if(!internalUserMap.containsKey(user.getUserName())) {
			internalUserMap.put(user.getUserName(), user);
		}
	}
	
	public List<Provider> getTripDeals(User user) {
		int cumulatativeRewardPoints = user.getUserRewards().stream().mapToInt(i -> i.getRewardPoints()).sum();
		List<Provider> providers = tripPricer.getPrice(tripPricerApiKey, user.getUserId(), user.getUserPreferences().getNumberOfAdults(), 
				user.getUserPreferences().getNumberOfChildren(), user.getUserPreferences().getTripDuration(), cumulatativeRewardPoints);
		user.setTripDeals(providers);
		return providers;
	}
	
	public VisitedLocation trackUserLocationWithoutThread(User user) {
//		StopWatch stopWatch = new StopWatch();
//		stopWatch.start();
		VisitedLocation visitedLocation = gpsUtilCustom.getUserLocation(user.getUserId());
		user.addToVisitedLocations(visitedLocation);
		rewardsService.calculateRewards(user);
//		stopWatch.stop();
//		logger.debug("Tracker Time Elapsed: " + stopWatch.getTime() + " ms.");
		return visitedLocation;
	}

	public boolean waitThreadToFinish(int minutes) throws InterruptedException {
		es.shutdown();
		return es.awaitTermination(minutes, TimeUnit.MINUTES);
	}


	public VisitedLocation trackUserLocation(User user) {
//		StopWatch stopWatch = new StopWatch();
//		stopWatch.start();

		es.execute(new Runnable(){
			@Override
			public void run() {
//				rewardsService.reNewThreadPool();
				trackUserLocationWithoutThread(user);
//				try {
//					rewardsService.waitThreadToFinish(1);
//				} catch (InterruptedException e) {
//					logger.error("error",e);
//				}
			}
		});

//		CompletableFuture<VisitedLocation> completableFuture = new CompletableFuture<>();
//		Executors.newCachedThreadPool()
//				.submit(() -> {
//					completableFuture.complete(trackUserLocationWithoutThread(user));
//					return null;
//				});
//
		VisitedLocation visitedLocation = null;
//		try {
//			visitedLocation = completableFuture.get();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		} catch (ExecutionException e) {
//			e.printStackTrace();
//		}

//		stopWatch.stop();
//		logger.debug("Tracker Time Elapsed: " + stopWatch.getTime() + " ms.");
		return visitedLocation;
	}

	private void addAndSortToArrayOrderByAsc(List<Attraction> attractions, Attraction newAttraction, VisitedLocation visitedLocation){
		if(attractions.size()==0){
			attractions.add(newAttraction);
			return;
		}

		double newDistance = rewardsService.getDistance(newAttraction, visitedLocation.location);

		for(int i = 0;i<attractions.size();i++){
			Attraction attraction = attractions.get(i);
			double distance = rewardsService.getDistance(attraction, visitedLocation.location);

			if(newDistance<distance){
				attractions.add(i,newAttraction);
				return;
			}
		}

		attractions.add(newAttraction);

	}

	public List<Attraction> getNearByAttractions(VisitedLocation visitedLocation) {
		List<Attraction> nearbyAttractions = new ArrayList<>();
		for(Attraction attraction : gpsUtilCustom.getAttractions()) {
//			if(rewardsService.isWithinAttractionProximity(attraction, visitedLocation.location)) {
//				nearbyAttractions.add(attraction);
//			}
			addAndSortToArrayOrderByAsc(nearbyAttractions, attraction, visitedLocation);
		}
		return nearbyAttractions.stream().limit(5).collect(Collectors.toList());
	}
	
	private void addShutDownHook() {
		Runtime.getRuntime().addShutdownHook(new Thread() { 
		      public void run() {
		        tracker.stopTracking();
		      } 
		    }); 
	}
	
	/**********************************************************************************
	 * 
	 * Methods Below: For Internal Testing
	 * 
	 **********************************************************************************/
	private static final String tripPricerApiKey = "test-server-api-key";
	// Database connection will be used for external users, but for testing purposes internal users are provided and stored in memory
	private final Map<String, User> internalUserMap = new HashMap<>();
	private void initializeInternalUsers() {
		IntStream.range(0, InternalTestHelper.getInternalUserNumber()).forEach(i -> {
			String userName = "internalUser" + i;
			String phone = "000";
			String email = userName + "@tourGuide.com";
			User user = new User(UUID.randomUUID(), userName, phone, email);
			generateUserLocationHistory(user);
			
			internalUserMap.put(userName, user);
		});
		logger.debug("Created " + InternalTestHelper.getInternalUserNumber() + " internal test users.");
	}
	
	private void generateUserLocationHistory(User user) {
		IntStream.range(0, 3).forEach(i-> {
			user.addToVisitedLocations(new VisitedLocation(user.getUserId(), new Location(generateRandomLatitude(), generateRandomLongitude()), getRandomTime()));
		});
	}
	
	private double generateRandomLongitude() {
		double leftLimit = -180;
	    double rightLimit = 180;
	    return leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
	}
	
	private double generateRandomLatitude() {
		double leftLimit = -85.05112878;
	    double rightLimit = 85.05112878;
	    return leftLimit + new Random().nextDouble() * (rightLimit - leftLimit);
	}
	
	private Date getRandomTime() {
		LocalDateTime localDateTime = LocalDateTime.now().minusDays(new Random().nextInt(30));
	    return Date.from(localDateTime.toInstant(ZoneOffset.UTC));
	}
	
}
