package in.co.greenwave.taskapi.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import in.co.greenwave.taskapi.dao.factory.DAOFactory;
import in.co.greenwave.taskapi.dao.factory.PostgresDAOFactory;
import in.co.greenwave.taskapi.dao.factory.SQLServerDAOFactory;

@Configuration
public class TaskApiConfiguration {
	
	@Bean
	public DAOFactory getDAOFactory(@Value("1") int whichFactory) {
		
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
