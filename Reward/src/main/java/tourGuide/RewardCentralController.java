package tourGuide;

import com.jsoniter.output.JsonStream;
import gpsUtil.location.VisitedLocation;
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

    @RequestMapping("/getAttractionRewardPoints")
    public int getLocation(@RequestParam UUID attractionId, @RequestParam UUID userId) {
		return rewardCentralService.getAttractionRewardPoints(attractionId, userId);
    }

}
