package tourGuide;

import java.util.List;
import java.util.UUID;

import gpsUtil.location.Attraction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gpsUtil.location.VisitedLocation;
import tourGuide.module.GpsUtilCustom;

@RestController
public class GpsController {

    public int times = 0;
    public int times2 = 0;

	@Autowired
    GpsUtilCustom gpsUtilCustom;

    @RequestMapping("/")
    public String index() {
        return "Greetings from TourGuide!";
    }

//    @RequestMapping("/getVisitedLocation")
//    public VisitedLocation getVisitedLocation(@RequestParam String uuid) {
//        UUID num = UUID.fromString(uuid);
//        return gpsUtilCustom.getUserLocation(num);
//    }

    @RequestMapping("/getVisitedLocation")
    public VisitedLocation getVisitedLocation(@RequestParam UUID uuid) {
//        times2++;
//        System.out.println("called getVisitedLocation : "+times2);
        VisitedLocation location = gpsUtilCustom.getUserLocation(uuid);
        return location;
    }


    @RequestMapping("/getAttractions")
    public List<Attraction> getAttractions() {
//        times++;
//		System.out.println("called getAttractions : "+times);
        List<Attraction> attractions = gpsUtilCustom.getAttractions();
        return attractions;
    }


}