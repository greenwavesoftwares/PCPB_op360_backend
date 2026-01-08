package in.co.greenwave.service; // This package contains the service classes for the application.

import org.springframework.context.annotation.Bean; // Importing the Bean annotation to create a bean.
import org.springframework.context.annotation.Configuration; // Importing the Configuration annotation to define a configuration class.
import org.springframework.web.client.RestTemplate; // Importing the RestTemplate class to make HTTP requests.

@Configuration // This annotation tells Spring that this class contains configuration settings for the application.
public class AppConfig {

    /**
     * This method creates and configures a RestTemplate bean.
     * 
     * @return A new instance of RestTemplate.
     */
    @Bean // This annotation marks the method as a bean factory method.
    public RestTemplate restTemplate() {
        return new RestTemplate(); // Creates a new RestTemplate object that can be used to make HTTP requests.
    }
}
