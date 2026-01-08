package in.co.greenwave.operation360.authservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Configuration class for setting up application-specific beans.
 * 
 * This class is used to define and configure Spring beans. In this case,
 * it provides a bean for {@link RestTemplate}, which is a synchronous HTTP 
 * client that can be used to make RESTful web service calls.
 */
@Configuration
public class AppConfig {
	
	/**
     * Creates and configures a {@link RestTemplate} bean.
     * 
     * @return a new instance of {@link RestTemplate} configured for making HTTP requests.
     */
	@Bean
	public RestTemplate template() {
		return new RestTemplate();
	}
}
