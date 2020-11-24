//package tourGuide.module;
//
//import com.google.common.util.concurrent.RateLimiter;
//import gpsUtil.GpsUtil;
//import gpsUtil.location.Location;
//import gpsUtil.location.VisitedLocation;
//import org.springframework.stereotype.Service;
//
//import java.util.Date;
//import java.util.UUID;
//import java.util.concurrent.ThreadLocalRandom;
//import java.util.concurrent.TimeUnit;
//
//@Service
//public class GpsUtilCustom extends GpsUtil {
//
//    private static final RateLimiter rateLimiter = RateLimiter.create(1000.0D);
//
//    @Override
//    public VisitedLocation getUserLocation(UUID userId) {
//        rateLimiter.acquire();
//        this.sleep();
//        double longitude = ThreadLocalRandom.current().nextDouble(-180.0D, 180.0D);
//        longitude = Double.parseDouble(String.format("%.6f", longitude).replace(",","."));
//        double latitude = ThreadLocalRandom.current().nextDouble(-85.05112878D, 85.05112878D);
//        latitude = Double.parseDouble(String.format("%.6f", latitude).replace(",","."));
//        VisitedLocation visitedLocation = new VisitedLocation(userId, new Location(latitude, longitude), new Date());
//        return visitedLocation;
//    }
//
//    private void sleep() {
//        int random = ThreadLocalRandom.current().nextInt(30, 100);
//
//        try {
//            TimeUnit.MILLISECONDS.sleep((long)random);
//        } catch (InterruptedException var3) {
//        }
//
//    }
//
//    private void sleepLighter() {
//        try {
//            TimeUnit.MILLISECONDS.sleep(10L);
//        } catch (InterruptedException var2) {
//        }
//
//    }
//
//}
