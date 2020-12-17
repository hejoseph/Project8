package tourGuide.service;

import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import tourGuide.model.Attraction;
import tourGuide.model.VisitedLocation;

import javax.security.auth.login.AccountNotFoundException;
import javax.xml.ws.Response;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;

public class GpsUtilService{

//    @Autowired
    public RestTemplate restTemplate;

    private final WebClient webClient;

    public String serviceUrl;

    public AsyncRestTemplate asyncRestTemplate;

    public GpsUtilService() {
//        this.restTemplate = new RestTemplate();
        this.asyncRestTemplate = new AsyncRestTemplate();
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
//        this.sleep();
//        VisitedLocation location = restTemplate.getForObject(serviceUrl
//                + "/getVisitedLocation?uuid={number}", VisitedLocation.class, uuid);

//        VisitedLocation location =
//                ListenableFuture<ResponseEntity<VisitedLocation>> future = asyncRestTemplate.getForEntity(serviceUrl
//                + "/getVisitedLocation?uuid={number}", VisitedLocation.class, uuid);



        Mono<VisitedLocation> stream = this.webClient
                .get()
                .uri("/getVisitedLocation?uuid={number}", uuid)
                .retrieve().bodyToMono(new ParameterizedTypeReference<VisitedLocation>(){});


        return stream.block();
    }


    ExecutorService es = Executors.newFixedThreadPool(1000);




//    @Override
//    public void run() {
//        for(int i = 0; i<100000 ; i++){
//            es.execute(new Runnable(){
//                @Override
//                public void run() {
//                        List<Attraction> attractions = getAttractions();
//                }
//            });
//            System.out.println(i+" : "+Thread.currentThread().getId());
//        }
//        System.out.println("end thread");
//    }

//    public static void main(String[] args) {
//        GpsUtilService t1 = new GpsUtilService();
//        GpsUtilService t2 = new GpsUtilService();
//        t1.start();
//        t2.start();
//    }

    public int called = 0;


    public List<Attraction> getAttractions(){
//        called += 1 ;
//        if(called%1000==0){
//            this.restTemplate = new RestTemplate();
//        }

//        this.sleep();
//        ResponseEntity<Attraction[]> response = restTemplate.getForEntity(serviceUrl+"/getAttractions", Attraction[].class);

//        List<Attraction> results = new ArrayList<>();
//
//        ListenableFuture<ResponseEntity<Attraction[]>> future = asyncRestTemplate.getForEntity(serviceUrl+"/getAttractions", Attraction[].class);
//
//        try {
//            results = Arrays.asList(future.get().getBody());
//        } catch (InterruptedException exception) {
//            exception.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }

//        return results;
//        return Arrays.asList(response.getBody());


        Mono<List<Attraction>> stream = this.webClient
                .get()
                .uri("/getAttractions")
                .retrieve().bodyToMono(new ParameterizedTypeReference<List<Attraction>>(){});

        return stream.block();
    }

}