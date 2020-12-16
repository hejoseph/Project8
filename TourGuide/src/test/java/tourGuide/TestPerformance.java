package tourGuide;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;

import org.apache.commons.lang3.time.StopWatch;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import tourGuide.helper.InternalTestHelper;
import tourGuide.model.Attraction;
import tourGuide.model.VisitedLocation;
import tourGuide.service.GpsUtilService;
import tourGuide.service.RewardsService;
import tourGuide.service.TourGuideService;
import tourGuide.user.User;
import tourGuide.user.UserReward;
import tourGuide.util.HttpClientConfig;
import tourGuide.util.RestTemplateConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ContextConfiguration(classes = { RestTemplateConfig.class, HttpClientConfig.class })
public class TestPerformance {

//	@Autowired
	public GpsUtilService gpsUtilService;

//	@Autowired
	public RewardsService rewardsService;

//	@Autowired
	public TourGuideService tourGuideService;

	/*
	 * A note on performance improvements:
	 *     
	 *     The number of users generated for the high volume tests can be easily adjusted via this method:
	 *     
	 *     		InternalTestHelper.setInternalUserNumber(100000);
	 *     
	 *     
	 *     These tests can be modified to suit new solutions, just as long as the performance metrics
	 *     at the end of the tests remains consistent. 
	 * 
	 *     These are performance metrics that we are trying to hit:
	 *     
	 *     highVolumeTrackLocation: 100,000 users within 15 minutes:
	 *     		assertTrue(TimeUnit.MINUTES.toSeconds(15) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
     *
     *     highVolumeGetRewards: 100,000 users within 20 minutes:
	 *          assertTrue(TimeUnit.MINUTES.toSeconds(20) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
	 */


//	@Before
//	public void init(){
//		this.gpsUtilService = new GpsUtilService();
//		this.rewardsService = new RewardsService(new GpsUtilService());
//		this.tourGuideService = new TourGuideService(new GpsUtilService(), new RewardsService(new GpsUtilService()));
//	}

	@Before
	public void init(){
		this.gpsUtilService = new GpsUtilService();
		this.rewardsService = new RewardsService(this.gpsUtilService);
		this.tourGuideService = new TourGuideService(this.gpsUtilService, this.rewardsService);
	}


//	@Ignore
	@Test
	public void highVolumeTrackLocation() throws InterruptedException {
		InternalTestHelper.setInternalUserNumber(100000);
		tourGuideService.initializeUserAndTracker();

		List<User> allUsers = new ArrayList<>();
		allUsers = tourGuideService.getAllUsers();
		
	    StopWatch stopWatch = new StopWatch();
		stopWatch.start();

//		rewardsService.setProximityBuffer(Integer.MAX_VALUE);
		for(User user : allUsers) {
			tourGuideService.trackUserLocation(user);
		}

		for(int i = 0; i < allUsers.size(); i++) {
			User user = allUsers.get(i);
			user.addDebug("main");
			tourGuideService.trackUserLocation(user);
		}

		tourGuideService.waitThreadToFinish(20);
		rewardsService.waitThreadToFinish(20);

		int nb = 0;
		for(User user : allUsers) {
//			System.out.println(user.getUserRewards().size());
//			assertTrue(user.getUserRewards().size() > 0);
			assertTrue(user.isRewardCalled());
			if(user.isRewardCalled()) nb = nb+1;
//			System.out.println(user.isRewardCalled()+" "+user.getDebug());
		}

		stopWatch.stop();
		tourGuideService.tracker.stopTracking();

		System.out.println("highVolumeTrackLocation: Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds."); 
		assertTrue(TimeUnit.MINUTES.toSeconds(15) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
		System.out.println(nb);
	}

	@Test
	public void highVolumeGetRewards() throws ExecutionException, InterruptedException {
		// Users should be incremented up to 100,000, and test finishes within 20 minutes
		InternalTestHelper.setInternalUserNumber(100000);
		tourGuideService.initializeUserAndTracker();

		StopWatch stopWatch = new StopWatch();
		stopWatch.start();

	    Attraction attraction = gpsUtilService.getAttractions().get(0);
		List<User> allUsers = new ArrayList<>();
		allUsers = tourGuideService.getAllUsers();
		allUsers.forEach(u -> u.addToVisitedLocations(new VisitedLocation(u.getUserId(), attraction, new Date())));

//	    allUsers.forEach(u -> rewardsService.calculateRewards(u));
//		for(User user : allUsers){
		for(int i = 0; i < allUsers.size(); i++){
			User user = allUsers.get(i);
			user.addDebug("main");
			rewardsService.calculateRewards(user);
//			if(i%1000==0)Thread.sleep(1000);
		}
		rewardsService.waitThreadToFinish(10);

//		Thread.sleep(10000);

		int nb = 0;
	    for(User user : allUsers) {
//			assertTrue(user.getUserRewards().size() > 0);
			assertTrue(user.isRewardCalled());
			if(user.isRewardCalled()) nb = nb+1;
//			System.out.println(user.isRewardCalled()+" "+user.getDebug());
		}
		stopWatch.stop();
		tourGuideService.tracker.stopTracking();

		System.out.println("highVolumeGetRewards: Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds."); 
		assertTrue(TimeUnit.MINUTES.toSeconds(20) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
		System.out.println(nb);
	}
}
