package in.co.greenwave.usermodule.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import in.co.greenwave.usermodule.dao.factory.DAOFactory;
import in.co.greenwave.usermodule.dao.factory.PostgresDAOFactory;
import in.co.greenwave.usermodule.dao.factory.SQLServerDAOFactory;


@Configuration
public class UsermoduleConfiguration {
	
	
    @Bean
    static DAOFactory getDAOFactory(@Value("${which.factory}") int whichFactory) {	
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
