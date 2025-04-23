package cz.muni.fi.musiccatalogservice;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("cz.muni.fi.musiccatalogservice.model")
@EnableJpaRepositories("cz.muni.fi.musiccatalogservice.repository")
@OpenAPIDefinition(info = @Info(title = "Music Catalog API", version = "1.0", description = "Music Catalog Information"))
public class MusicCatalogServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MusicCatalogServiceApplication.class, args);
	}
}