package tourGuide;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
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

@ContextConfiguration(classes = { RestTemplateConfig.class, HttpClientConfig.class })
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class TestRewardsService {

//	@Autowired
	public GpsUtilService gpsUtilService;

//	@Autowired
	public RewardsService rewardsService;

//	@Autowired
	public TourGuideService tourGuideService;


	@Before
	public void init(){
		this.gpsUtilService = new GpsUtilService();
		this.rewardsService = new RewardsService(this.gpsUtilService);
		this.tourGuideService = new TourGuideService(this.gpsUtilService, this.rewardsService);
	}

	@Test
	public void userGetRewards() throws InterruptedException, ExecutionException {
		InternalTestHelper.setInternalUserNumber(0);
		tourGuideService.initializeUserAndTracker();

		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		Attraction attraction = gpsUtilService.getAttractions().get(0);
		user.addToVisitedLocations(new VisitedLocation(user.getUserId(), attraction, new Date()));
		tourGuideService.trackUserLocationWithoutRewardExecutorService(user);
		List<UserReward> userRewards = user.getUserRewards();
		tourGuideService.tracker.stopTracking();
		assertTrue(userRewards.size() == 1);
	}

	@Test
	public void isWithinAttractionProximity() {
		Attraction attraction = gpsUtilService.getAttractions().get(0);
		assertTrue(rewardsService.isWithinAttractionProximity(attraction, attraction));
	}

	@Test
	public void nearAllAttractions() throws InterruptedException {
		rewardsService.setProximityBuffer(Integer.MAX_VALUE);
		InternalTestHelper.setInternalUserNumber(1);
		tourGuideService.initializeUserAndTracker();
		User user = tourGuideService.getAllUsers().get(0);
		rewardsService.calculateRewardsWithoutThread(user);
		List<UserReward> userRewards = tourGuideService.getUserRewards(user);
		tourGuideService.tracker.stopTracking();
		assertEquals(gpsUtilService.getAttractions().size(), userRewards.size());
	}
	
}
