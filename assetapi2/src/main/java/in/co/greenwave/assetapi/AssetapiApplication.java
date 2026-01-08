package in.co.greenwave.assetapi; // Declares the package where the Assetapi application resides

//Imports necessary Spring framework classes for application configuration and startup
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication // This tells Spring Boot to start the application and handle setup automatically.
public class AssetapiApplication  extends SpringBootServletInitializer{

    // This is where the program starts running.
    public static void main(String[] args) {
        SpringApplication.run(AssetapiApplication.class, args); // This runs the whole application.
    }

    // This part sets up CORS (Cross-Origin Resource Sharing).
    // CORS is like a permission system for websites so they can share data safely with each other.
    @Bean // Marks this method as a Spring bean, meaning it will be managed by the Spring container.
    public WebMvcConfigurer crosConfigurer() {       
        return new WebMvcConfigurer() {
            // This method allows the website to share data with any other website or app.
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // This means all paths (like all web addresses) are allowed.
                        .allowedMethods("*") // This means all types of actions (like GET, POST, etc.) are allowed.
                        .allowedOrigins("*"); // This means any website is allowed to share data with this application.
            }
        };
    }
  //This method is essential for creating the war file that is generated in target
  	@Override
  	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
  		return application.sources(AssetapiApplication.class);
  	}
}

