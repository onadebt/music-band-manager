package cz.muni.fi.tourmanagementservice;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("cz.muni.fi.tourmanagementservice.model")
@EnableJpaRepositories("cz.muni.fi.tourmanagementservice.repository")
@OpenAPIDefinition(info = @Info(title = "Tour API", version = "1.0", description = "Tour Information"))
public class TourServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TourServiceApplication.class, args);
	}

}
