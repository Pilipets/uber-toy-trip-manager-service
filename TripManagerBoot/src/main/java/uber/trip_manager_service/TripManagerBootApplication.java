package uber.trip_manager_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
@EnableFeignClients
public class TripManagerBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(TripManagerBootApplication.class, args);
	}
}
