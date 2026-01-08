package in.co.greenwave.UserGroup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
//This tells Spring Boot to start the application and handle setup automatically.
@SpringBootApplication
public class UserGroupApplication extends SpringBootServletInitializer{
	//This is the initiation of the services
	public static void main(String[] args) {
		SpringApplication.run(UserGroupApplication.class, args);
	}
	
	//This method is essential for creating the war file that is generated in target
	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(UserGroupApplication.class);
    }

}
