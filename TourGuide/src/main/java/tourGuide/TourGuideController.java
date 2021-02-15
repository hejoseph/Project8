package tourGuide;

import com.jsoniter.output.JsonStream;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tourGuide.helper.InternalTestHelper;
import tourGuide.model.Provider;
import tourGuide.model.VisitedLocation;
import tourGuide.service.GpsUtilService;
import tourGuide.service.RewardsService;
import tourGuide.service.TourGuideService;
import tourGuide.user.User;

import java.util.HashMap;
import java.util.List;

@RestController
public class TourGuideController {

	TourGuideService tourGuideService;

    GpsUtilService gpsUtilService;

    RewardsService rewardsService;

    public TourGuideController(){
        this.gpsUtilService = new GpsUtilService();
        this.rewardsService = new RewardsService(this.gpsUtilService);
        this.tourGuideService = new TourGuideService(this.gpsUtilService, this.rewardsService);
    }

    @RequestMapping("/")
    public String index() {
        return "Greetings from TourGuide!";
    }

        @RequestMapping("/getAllUsername")
    public List<User> getAllUserName(){
        return tourGuideService.getAllUsers();
    }

    @RequestMapping("/getLocation") 
    public String getLocation(@RequestParam String userName) {
        User user = getUserObject(userName);
        if(user!=null){
            VisitedLocation visitedLocation = tourGuideService.getUserLocation(user);
            return JsonStream.serialize(visitedLocation.location);
        }else{
            return "username:"+userName+" does not exist";
        }


    }

    @RequestMapping("/initUser")
    public String initUser(@RequestParam String number) {
        InternalTestHelper.setInternalUserNumber(Integer.parseInt(number));
        tourGuideService.initializeUserAndTracker();
        return "Success : "+number;
    }
    
    //  TODO: Change this method to no longer return a List of Attractions.
 	//  Instead: Get the closest five tourist attractions to the user - no matter how far away they are.
 	//  Return a new JSON object that contains:
    	// Name of Tourist attraction, 
        // Tourist attractions lat/long, 
        // The user's location lat/long, 
        // The distance in miles between the user's location and each of the attractions.
        // The reward points for visiting each Attraction.
        //    Note: Attraction reward points can be gathered from RewardsCentral
    @RequestMapping("/getNearbyAttractions") 
    public String getNearbyAttractions(@RequestParam String userName) {
        User user = getUserObject(userName);
        if(user!=null){
            VisitedLocation visitedLocation = tourGuideService.getUserLocation(user);
            return JsonStream.serialize(tourGuideService.getNearByAttractions(visitedLocation));
        }else{
            return "username:"+userName+" does not exist";
        }

    }
    
    @RequestMapping("/getRewards") 
    public String getRewards(@RequestParam String userName) {
        User user = getUserObject(userName);
        if(user!=null){
    	    return JsonStream.serialize(tourGuideService.getUserRewards(user));
        }else{
            return "username:"+userName+" does not exist";
        }
    }
    
    @RequestMapping("/getAllCurrentLocations")
    public String getAllCurrentLocations() {
    	// TODO: Get a list of every user's most recent location as JSON
    	//- Note: does not use gpsUtil to query for their current location, 
    	//        but rather gathers the user's current location from their stored location history.
    	//
    	// Return object should be the just a JSON mapping of userId to Locations similar to:
    	//     {
    	//        "019b04a9-067a-4c76-8817-ee75088c3822": {"longitude":-48.188821,"latitude":74.84371} 
    	//        ...
    	//     }

        HashMap<String, VisitedLocation> locationHashMap = new HashMap<>();
        List<User> users = tourGuideService.getAllUsers();
        for(User user : users){
            VisitedLocation lastLocation = user.getLastVisitedLocation();
            locationHashMap.put(user.getUserId()+"", lastLocation);
        }
    	return JsonStream.serialize(locationHashMap);
    }

    @RequestMapping("/trackUserLocation")
    public User trackUserLocation(@RequestParam String userName) {
        User user = getUser(userName);
        tourGuideService.trackUserLocationWithoutRewardExecutorService(user);
        return user;
    }
    
    @RequestMapping("/getTripDeals")
    public String getTripDeals(@RequestParam String userName) {
        User user = getUserObject(userName);
        if(user!=null){
            List<Provider> providers = tourGuideService.getTripDeals(getUserObject(userName));
            return JsonStream.serialize(providers);
        }else{
            return "username:"+userName+" does not exist";
        }
    }

    @RequestMapping("/getUser")
    public User getUser(@RequestParam String userName) {
        User user = getUserObject(userName);
        return user;
    }

    private User getUserObject(String userName) {
    	return tourGuideService.getUser(userName);
    }
}