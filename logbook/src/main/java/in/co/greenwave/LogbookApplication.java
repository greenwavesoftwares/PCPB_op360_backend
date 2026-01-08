package in.co.greenwave; // Declares the package where the Logbook application resides

//Imports necessary Spring framework classes for application configuration and startup
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
//import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication // This tells Spring Boot to start the application and handle setup automatically.
//@EnableDiscoveryClient // This line is turned off (commented). It would let the application tell other services it exists, but we're not doing that now.
public class LogbookApplication  extends SpringBootServletInitializer{
	
	// This is where the program starts running.
	public static void main(String[] args) {
		SpringApplication.run(LogbookApplication.class, args); // This runs the whole application.
	}
	//This method is essential for creating the war file that is generated in target
		@Override
		protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
			return application.sources(LogbookApplication.class);
		}
}
