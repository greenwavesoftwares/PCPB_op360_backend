package in.co.greenwave.operation360.authservice.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.co.greenwave.operation360.authservice.entity.AuthRequest;
import in.co.greenwave.operation360.authservice.entity.User;
import in.co.greenwave.operation360.authservice.repository.UserModuleService;

/**
 * Service implementation for user-related operations.
 * 
 * This class implements the {@link UserModuleService} interface and provides the actual logic for
 * retrieving user information. It interacts with the repository layer to fetch user data based on the user ID.
 */
@Service
public class UserModuleServiceImpl implements UserModuleService {

    @Autowired
    private UserModuleRepositoryServiceImpl userModuleServiceRepository;

    /**
     * Retrieves a user based on their unique identifier.
     * 
     * This method uses the {@link UserModuleRepositoryServiceImpl} to fetch user data from the repository.
     * It logs the user ID and the resulting {@link User} object for debugging purposes.
     * 
     * @param userId the unique identifier of the user to retrieve.
     * @return the {@link User} object corresponding to the provided user ID, or an empty user object if an error occurs.
     */
    @Override
    public User getUserByUserId(AuthRequest authRequest) {
    	
    	String userId = authRequest.getUsername();
    	String tenantId = authRequest.getTenantId();
    	
        System.out.println("userId : : : " + userId);
        User responseEntity = new User();
        try {
            responseEntity = userModuleServiceRepository.getByUserId(authRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseEntity;
    }
    
    public static String generateHash(String input) {
		StringBuilder hash = new StringBuilder();

		try {
			MessageDigest sha = MessageDigest.getInstance("SHA-1");
			byte[] hashedBytes = sha.digest(input.getBytes());
			char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
					'a', 'b', 'c', 'd', 'e', 'f' };
			for (int idx = 0; idx < hashedBytes.length; ++idx) {
				byte b = hashedBytes[idx];
				hash.append(digits[(b & 0xf0) >> 4]);
				hash.append(digits[b & 0x0f]);
			}
		} catch (NoSuchAlgorithmException e) {
			// handle error here.
			e.printStackTrace();
		}

		return hash.toString();
	}
}
