package tourGuide.service;

import org.springframework.stereotype.Service;
import rewardCentral.RewardCentral;

import java.util.UUID;

@Service
public class RewardCentralService {

    public RewardCentral rewardCentral;

    public RewardCentralService(){
        this.rewardCentral = new RewardCentral();
    }

    public int getAttractionRewardPoints(UUID attractionId, UUID userId){
        return this.rewardCentral.getAttractionRewardPoints(attractionId, userId);
    }

}
