package in.co.greenwave.usermodule;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class UsermoduleApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(UsermoduleApplication.class, args);
	}
	//This method is essential for mapping the required properties the war file that is generated in target
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(UsermoduleApplication.class);
	}


}
