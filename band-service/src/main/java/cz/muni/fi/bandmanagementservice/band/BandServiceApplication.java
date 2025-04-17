package cz.muni.fi.bandmanagementservice.band;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @author Tomáš MAREK
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class BandServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(BandServiceApplication.class, args);
    }

}
