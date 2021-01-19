package tourGuide;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tourGuide.service.PricerService;
import tripPricer.Provider;

import java.util.List;
import java.util.UUID;

@RestController
public class PricerController {

    @Autowired
    public PricerService pricerService;

    @RequestMapping("/")
    public String index() {
        return "Greetings from TourGuide! Pricer";
    }

    @RequestMapping("/getPrice")
    public List<Provider> getPrice(@RequestParam String apiKey, @RequestParam UUID attractionId, @RequestParam int adults, @RequestParam int children, @RequestParam int nightsStay, @RequestParam int rewardsPoints) {
        List<Provider> providers = pricerService.getPrice(apiKey, attractionId, adults, children, nightsStay, rewardsPoints);
        return providers;
    }

}
