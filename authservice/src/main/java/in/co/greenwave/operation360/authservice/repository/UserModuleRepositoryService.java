package in.co.greenwave.operation360.authservice.repository;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;

import in.co.greenwave.operation360.authservice.entity.AuthRequest;
import in.co.greenwave.operation360.authservice.entity.User;

/**
 * Interface for user repository services.
 * 
 * This interface defines the methods for interacting with user-related data in the repository.
 * It includes operations for retrieving user information and logging incorrect password attempts.
 */
public interface UserModuleRepositoryService {

    /**
     * Retrieves a user from the repository based on the provided user ID.
     * 
     * @param userId the unique identifier for the user.
     * @return the {@link User} object corresponding to the provided user ID, or null if not found.
     */
    public User getByUserId(AuthRequest authRequest);

    /**
     * Logs an incorrect password attempt for a specific user.
     * 
     * @param userId the unique identifier for the user.
     * @param tenantId the tenant ID associated with the user.
     * @param clientIpAddress the IP address of the client making the request.
     * @return true if the log entry was successfully added, false otherwise.
     */
    public boolean addWrongPasswordLog(AuthRequest authRequest, String clientIpAddress);
    
    public List<Map<String, Object>> getTenantDB(String tenantId);
	
	public List<String> userWiseTenant(String userId);
	
	public void registerNewTenant(String tenantid);
		
}
