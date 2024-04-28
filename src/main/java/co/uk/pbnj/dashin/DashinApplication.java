package co.uk.pbnj.dashin;

import co.uk.pbnj.dashin.config.CountdownAppConfig;
import co.uk.pbnj.dashin.config.TodoDisplayConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;


@SpringBootApplication
@EnableConfigurationProperties({
		TodoDisplayConfig.class,
		CountdownAppConfig.class
})
public class DashinApplication {

	public static void main(String[] args) {
		SpringApplication.run(DashinApplication.class, args);
	}

}
