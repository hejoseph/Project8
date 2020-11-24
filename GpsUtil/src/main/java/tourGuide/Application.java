package tourGuide;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
//import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
//@EnableEurekaServer
public class Application {

    public static void main(String[] args) {
        // Tell Boot to look for registration-server.yml
//        System.setProperty("spring.config.name", "registration-server");
        SpringApplication.run(Application.class, args);
    }

}
