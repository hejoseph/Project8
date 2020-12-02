package tourGuide.service;

import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tourGuide.model.Attraction;
import tourGuide.model.VisitedLocation;

import javax.security.auth.login.AccountNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class GpsUtilService {
    public RestTemplate restTemplate;
    public String serviceUrl;

    public GpsUtilService() {
        this.restTemplate = new RestTemplate();
        this.serviceUrl = "http://localhost:9092";
        this.serviceUrl = serviceUrl.startsWith("http") ?
                serviceUrl : "http://" + serviceUrl;
    }

    public VisitedLocation getUserLocation(UUID uuid) {
        VisitedLocation location = restTemplate.getForObject(serviceUrl
                + "/getVisitedLocation?uuid={number}", VisitedLocation.class, uuid);

        return location;
    }

    public List<Attraction> getAttractions(){
        ResponseEntity<Attraction[]> response = restTemplate.getForEntity(serviceUrl+"/getAttractions", Attraction[].class);
        return Arrays.asList(response.getBody());
    }

}
