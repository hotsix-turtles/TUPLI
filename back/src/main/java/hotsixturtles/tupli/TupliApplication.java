package hotsixturtles.tupli;

import hotsixturtles.tupli.config.auth.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({
		AppProperties.class
})
public class TupliApplication {

	public static void main(String[] args) {
		SpringApplication.run(TupliApplication.class, args);
	}

}
