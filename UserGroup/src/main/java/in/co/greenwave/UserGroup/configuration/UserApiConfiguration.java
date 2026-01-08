package in.co.greenwave.UserGroup.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import in.co.greenwave.UserGroup.dao.factory.DAOFactory;
import in.co.greenwave.UserGroup.dao.factory.PostgresDAOFactory;
import in.co.greenwave.UserGroup.dao.factory.SQLServerDAOFactory;

@Configuration // Marks this class as a configuration class in Spring, used to define beans
public class UserApiConfiguration {

    // This method is annotated with @Bean to declare that it will return a Spring Bean.
    // It uses @Value to inject the value of 'whichFactory' to determine the type of DAOFactory to create.
    @Bean
    public static DAOFactory getDAOFactory(@Value("1") int whichFactory) {
        
        // Log the factory type to the console
        System.out.println("whichFactory => "+ whichFactory);

        // Constants representing different types of database factories
        final int SQLSERVER = 1;
        final int ORACLE = 2;
        final int POSTGRES = 3;
        
        // Switch statement to choose the appropriate DAOFactory implementation based on 'whichFactory'
        switch (whichFactory) {
        case SQLSERVER: 
            // Return the SQLServerDAOFactory when 'whichFactory' is 1
            return new SQLServerDAOFactory();
        
        // Case for Oracle database is commented out (possibly for future implementation)
        // case ORACLE    : 
        //    return new OracleDAOFactory();
        
        case POSTGRES:
            // Return the PostgresDAOFactory when 'whichFactory' is 3
            return new PostgresDAOFactory();
        
        default:
            // Return null if an unsupported factory type is provided
            return null;
        }
    }
}
