package in.co.greenwave.operation360.authservice.repository.factory; // Package declaration for the DAO Factory class


// Importing the necessary DAO interfaces for use in the factory
import in.co.greenwave.operation360.authservice.repository.UserModuleRepositoryService;
import in.co.greenwave.operation360.authservice.repository.UserModuleService;

// Abstract class DAOFactory serves as a blueprint for creating instances of various DAO services
public abstract class DAOFactory {

	// Abstract method to obtain an instance of LogbookDAO. 
	// Concrete subclasses must implement this method to provide the actual service.

	// Service to interact with user-related operations, such as retrieving users from the database
	public abstract UserModuleService getuserModuleService();

	// Service to handle database repository actions related to the user module
	public abstract UserModuleRepositoryService getuserModuleRepositoryService();
}
