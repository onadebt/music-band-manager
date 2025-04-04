package cz.muni.fi.musiccatalogservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableDiscoveryClient
@EntityScan("cz.muni.fi.musiccatalogservice.model")
@EnableJpaRepositories("cz.muni.fi.musiccatalogservice.repository")
public class MusicCatalogServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MusicCatalogServiceApplication.class, args);
	}

}
