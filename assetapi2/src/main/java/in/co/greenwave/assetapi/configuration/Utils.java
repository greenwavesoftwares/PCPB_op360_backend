package in.co.greenwave.assetapi.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration // This tells Spring that this class is for setting up things the application might need (configuration).

public class Utils {

    // This method creates a RestTemplate, which is a tool that helps us send requests to other websites or services (like APIs).
    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate(); // We return a new RestTemplate object so it can be used anywhere in the application to make web requests.
    }
}
