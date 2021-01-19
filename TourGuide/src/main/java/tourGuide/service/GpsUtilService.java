package tourGuide.service;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import tourGuide.model.Attraction;
import tourGuide.model.VisitedLocation;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class GpsUtilService{

    private final WebClient webClient;

    public String serviceUrl;


    public GpsUtilService() {
        this.serviceUrl = "http://localhost:9092";
        this.serviceUrl = serviceUrl.startsWith("http") ?
                serviceUrl : "http://" + serviceUrl;
        this.webClient = WebClient.create(this.serviceUrl);
    }

    public int nbErrAttraction = 0;
    public int nbErrLocation = 0;

    private void sleep() {
        int random = ThreadLocalRandom.current().nextInt(30, 100);

        try {
            TimeUnit.MILLISECONDS.sleep((long)random);
        } catch (InterruptedException var3) {
        }

    }

    public VisitedLocation getUserLocation(UUID uuid) {
        Mono<VisitedLocation> stream = this.webClient
                .get()
                .uri("/getVisitedLocation?uuid={number}", uuid)
                .retrieve().bodyToMono(new ParameterizedTypeReference<VisitedLocation>(){});


        return stream.block();
    }


    ExecutorService es = Executors.newFixedThreadPool(1000);

    public int called = 0;


    public List<Attraction> getAttractions(){

        Mono<List<Attraction>> stream = this.webClient
                .get()
                .uri("/getAttractions")
                .retrieve().bodyToMono(new ParameterizedTypeReference<List<Attraction>>(){});

        return stream.block();
    }

}