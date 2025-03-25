package cz.muni.fi.musiccatalogservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MusicCatalogServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MusicCatalogServiceApplication.class, args);
	}

}
