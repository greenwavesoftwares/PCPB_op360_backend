package in.co.greenwave.config; // Declares the package where the configuration resides

// Imports necessary Spring framework classes for bean creation and configuration

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import in.co.greenwave.dao.factory.DAOFactory;
import in.co.greenwave.dao.factory.PostgresDAOFactory;
import in.co.greenwave.dao.factory.SQLServerDAOFactory;

// Imports the DAOFactory and its specific implementations

/**
 * AssetApiConfiguration class is a configuration class for defining and configuring beans
 * related to the asset API's data access object (DAO) factories. This allows the system
 * to switch between different database sources (SQL Server, Postgres, etc.).
 */
@Configuration // Marks this class as a configuration that defines bean methods.
public class LogbookApiConfiguration {

    /**
     * Creates and returns a DAOFactory bean based on the provided database type.
     * This allows the system to use the correct DAO factory depending on the database being used.
     * 
     * @param whichFactory The integer value that determines which factory to use. 
     *                     1 for SQL Server, 3 for Postgres.
     * @return An instance of the appropriate DAOFactory (SQL Server or Postgres).
     */
    @Bean // Marks this method as a Spring bean, meaning it will be managed by the Spring container.
    public static DAOFactory getDAOFactory(@Value("1") int whichFactory) {
        
        // Logging whichFactory value for debugging purposes.
        System.out.println("whichFactory => " + whichFactory);
        
        // Constants representing different database choices.
        final int SQLSERVER = 1;
        final int ORACLE = 2;   // ORACLE is defined but not implemented yet.
        final int POSTGRES = 3;

        // Switch statement to select and return the appropriate DAOFactory based on the input.
        switch (whichFactory) {
            case SQLSERVER: 
                // If whichFactory is 1, return an instance of SQLServerDAOFactory.
                return new SQLServerDAOFactory();
                
            // case ORACLE:
                // Future implementation for Oracle database could be placed here.
                // return new OracleDAOFactory(); 

            case POSTGRES:
                // If whichFactory is 3, return an instance of PostgresDAOFactory.
                return new PostgresDAOFactory();

            default:
                // If no valid database is selected, return null (or handle accordingly).
                return null;
        }
    }
}
