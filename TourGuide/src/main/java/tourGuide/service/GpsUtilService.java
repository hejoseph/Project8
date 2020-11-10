package tourGuide.service;

import gpsUtil.location.VisitedLocation;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.security.auth.login.AccountNotFoundException;
import java.util.UUID;

public class GpsUtilService {
    protected RestTemplate restTemplate;
    protected String serviceUrl;

    public GpsUtilService(String serviceUrl) {
        this.serviceUrl = serviceUrl.startsWith("http") ?
                serviceUrl : "http://" + serviceUrl;
    }

    public VisitedLocation getUserLocation(UUID uuid) {
        VisitedLocation location = restTemplate.getForObject(serviceUrl
                + "/getVisitedLocation?uuid={number}", VisitedLocation.class, uuid);

        return location;
    }
}
