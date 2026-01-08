package in.co.greenwave.operation360.authservice.repository.factory; // Package containing the DAO factory classes

// Importing necessary DAO interfaces to be implemented
import in.co.greenwave.operation360.authservice.repository.UserModuleRepositoryService;
import in.co.greenwave.operation360.authservice.repository.UserModuleService;

// Concrete class that extends the abstract DAOFactory class for Postgres database implementation
public class PostgresDAOFactory extends DAOFactory {

	@Override
    public UserModuleService getuserModuleService() {
		return null;
	}

    // Service to handle database repository actions related to the user module
	@Override
    public UserModuleRepositoryService getuserModuleRepositoryService() {
    	return null;
    }
}
