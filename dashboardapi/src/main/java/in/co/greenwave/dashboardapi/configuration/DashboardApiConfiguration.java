package in.co.greenwave.dashboardapi.configuration;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import in.co.greenwave.dashboardapi.dao.factory.DAOFactory;
import in.co.greenwave.dashboardapi.dao.factory.PostgresDAOFactory;
import in.co.greenwave.dashboardapi.dao.factory.SQLServerDAOFactory;


/**
 * "which.factory" is a property defined in application.properties
 *  Decides which DAOFactory we are going to use based on the value of "whichFactory"
 */
@Configuration
public class DashboardApiConfiguration {

	@Bean
	public static DAOFactory getDAOFactory(@Value("1") int whichFactory) {
		
		System.out.println("whichFactory => "+ whichFactory);
		final int SQLSERVER = 1;
		final int ORACLE = 2;
		final int POSTGRES = 3;
		
		switch (whichFactory) {
		case SQLSERVER: 
			return new SQLServerDAOFactory();
			// case ORACLE    : 
			//return new OracleDAOFactory(); 
		case POSTGRES:
			return new PostgresDAOFactory();

		default           : 
			return null;
		}
	}
}
