package tourGuide;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tourGuide.service.RewardCentralService;

import java.util.UUID;

@RestController
public class RewardCentralController {

    @Autowired
    public RewardCentralService rewardCentralService;

    public int times = 0;

    @RequestMapping("/")
    public String index() {
        return "Greetings from TourGuide! Reward";
    }

    @RequestMapping("/getAttractionRewardPoints")
    public int getLocation(@RequestParam UUID attractionId, @RequestParam UUID userId) {
//		times++;
//		System.out.println("called getAttractionRewardPoints : "+times);
        return rewardCentralService.getAttractionRewardPoints(attractionId, userId);
    }

}


