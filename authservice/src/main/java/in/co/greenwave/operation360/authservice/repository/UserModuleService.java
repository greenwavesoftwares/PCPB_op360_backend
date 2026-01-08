package in.co.greenwave.operation360.authservice.repository;

import in.co.greenwave.operation360.authservice.entity.AuthRequest;
import in.co.greenwave.operation360.authservice.entity.User;
import in.co.greenwave.operation360.authservice.service.UserModuleRepositoryServiceImpl;

/**
 * Service interface for user-related operations.
 * 
 * This interface defines methods for retrieving user information. It is intended to be implemented by
 * a service class that interacts with the repository layer to perform user-related operations.
 */
public interface UserModuleService {
    
//    /**
//     * Retrieves a user based on their unique identifier.
//     * 
//     * @param userId the unique identifier of the user to retrieve.
//     * @return the {@link User} object corresponding to the provided user ID, or null if not found.
//     */
//    User getUserByUserId(AuthRequest authRequest);

	/**
	 * Retrieves a user based on their unique identifier.
	 * 
	 * This method uses the {@link UserModuleRepositoryServiceImpl} to fetch user data from the repository.
	 * It logs the user ID and the resulting {@link User} object for debugging purposes.
	 * 
	 * @param userId the unique identifier of the user to retrieve.
	 * @return the {@link User} object corresponding to the provided user ID, or an empty user object if an error occurs.
	 */
	User getUserByUserId(AuthRequest authRequest);
}
