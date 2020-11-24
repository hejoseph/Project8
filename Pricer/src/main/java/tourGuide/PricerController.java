package tourGuide;

import com.jsoniter.output.JsonStream;
import gpsUtil.location.VisitedLocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import tourGuide.service.PricerService;
import tripPricer.Provider;

import java.util.List;
import java.util.UUID;

public class PricerController {

    @Autowired
    public PricerService pricerService;

    @RequestMapping("/getPrice")
    public List<Provider> getPrice(@RequestParam String apiKey, @RequestParam UUID attractionId, @RequestParam int adults, @RequestParam int children, @RequestParam int nightsStay, @RequestParam int rewardsPoints) {
        return pricerService.getPrice(apiKey, attractionId, adults, children, nightsStay, rewardsPoints);
    }

}
