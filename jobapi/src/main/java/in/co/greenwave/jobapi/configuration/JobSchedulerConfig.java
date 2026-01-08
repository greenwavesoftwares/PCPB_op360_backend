package in.co.greenwave.jobapi.configuration;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import in.co.greenwave.jobapi.dao.JobPerformDAO;
import in.co.greenwave.jobapi.dao.factory.DAOFactory;

/**
* Indicates that a class declares one or more @Bean methods and 
* may be processed by the Spring container to generate bean definitions and 
* service requests for those beans at runtime, for example: 
* */
@Configuration
/*allows selective scheduling of beans at different levels*/
@EnableScheduling
/*
 * Class to schedule @Bean methods at regular interval
 * */
public class JobSchedulerConfig {
	/**
	 * The "factory" reference is getting Autowired ,with the Bean  of the JobApiConfiguration class
	 */
	@Autowired
	private DAOFactory factory;

	private JobPerformDAO jobperformdao;

	/* method to update enforced activities of every tenant*/
	@Scheduled(fixedRate = 10000)
	public void updateEnforcedActivitiesForEveryTenant() {

		try {
			jobperformdao=factory.getJobPerformService();
			jobperformdao.updateEnforcedActivitiesForEveryTenant();
			
		}catch(Exception e) {
			System.err.println("Error in updateEnforcedActivitiesForEveryTenant of JobPerformerController:"+e);
		}
	}
}
