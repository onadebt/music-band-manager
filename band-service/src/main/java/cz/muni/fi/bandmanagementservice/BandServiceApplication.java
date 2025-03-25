package cz.muni.fi.bandmanagementservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class BandServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BandServiceApplication.class, args);
	}

}
