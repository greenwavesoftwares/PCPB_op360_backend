package in.co.greenwave.operation360.authservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import in.co.greenwave.operation360.authservice.entity.AuthRequest;
import in.co.greenwave.operation360.authservice.entity.JsonResponse;
import in.co.greenwave.operation360.authservice.entity.User;
import in.co.greenwave.operation360.authservice.repository.UserModuleRepositoryService;
import in.co.greenwave.operation360.authservice.repository.UserModuleService;
import in.co.greenwave.operation360.authservice.repository.factory.DAOFactory;
import in.co.greenwave.operation360.authservice.service.UserModuleServiceImpl;
import in.co.greenwave.operation360.authservice.utility.JwtService;
import jakarta.servlet.http.HttpServletRequest;

/**
 * AuthController is responsible for handling authentication-related API requests.
 * It includes operations for signing in and extending JWT token expiration.
 */
@RestController // Marks this class as a controller that handles RESTful web requests
@RequestMapping("/auth") // Maps all requests starting with "/auth" to methods in this controller
public class AuthController {

	@Autowired // Automatically injects the DAOFactory object
	private DAOFactory factory;
	
    // Service to handle JWT (token) operations like generating and extending tokens
    @Autowired
    private JwtService jwtService;

    // Service to interact with user-related operations, such as retrieving users from the database
    @Autowired
    private UserModuleService userModuleService;

    // Service to handle database repository actions related to the user module
    @Autowired
    private UserModuleRepositoryService userModuleRepositoryService;

    // Salt value used to strengthen password hashing for added security
    public static final String SALT = "Pr!mef@ce$";
    
    @GetMapping("/userWiseTenant")
    public ResponseEntity<List<String>> userWiseTenant(@RequestParam String userId) {
    	
    	userModuleRepositoryService = factory.getuserModuleRepositoryService();
    	
    	List<String> TenantList = userModuleRepositoryService.userWiseTenant(userId);
        if (TenantList != null) {
            return ResponseEntity.ok().body(TenantList);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * API endpoint to authenticate a user and generate a JWT (token) upon successful authentication.
     * 
     * @param authRequest Contains username, password, and tenantId for the login attempt.
     * @param request Captures the client's IP address for logging purposes.
     * @return A {@link ResponseEntity} containing a {@link JsonResponse} with either the generated token and user information or an error message.
     */
    @PostMapping("/signin")
    public ResponseEntity<JsonResponse> authenticateAndGetToken(@RequestBody AuthRequest authRequest, HttpServletRequest request) {
        System.out.println("signin invoked!");
        System.out.println("authRequest.getUsername(): " + authRequest.getUsername() + " : authRequest.getPassword(): " + authRequest.getPassword() + " : authRequest.getTenantId(): " + authRequest.getTenantId());

        // Hash the incoming password using a predefined salt value
        String userPassword = UserModuleServiceImpl.generateHash(SALT + authRequest.getPassword());
        System.out.println("userPassword: " + userPassword);
        
        userModuleService = factory.getuserModuleService();
        userModuleRepositoryService = factory.getuserModuleRepositoryService();
        
        // Retrieve the user from the database by username and tenantId
        User savedUser = userModuleService.getUserByUserId(authRequest);
        String savedPassword = savedUser.getPassword();
        System.out.println("savedPassword.equals(userPassword): " + savedPassword.equals(userPassword));
        System.out.println("sAVED PASSWORD:"+savedPassword);
        System.out.println("User password:"+userPassword);
        // If the saved password matches the hashed password from the request, generate a JWT
        if (savedPassword.equals(userPassword)) {
            String jwtString = jwtService.generateToken(authRequest.getUsername());
            System.out.println("jwtString: " + jwtString);

            // Create a response object with the generated token and user details
            JsonResponse json = new JsonResponse();
            json.setStatus(true);
            json.setToken(jwtString);
            json.setTenantId(savedUser.getTenantId());
            json.setUser(savedUser);
            return ResponseEntity.ok().body(json); // Return a success response with the token
        }

        // If authentication fails, log the attempt and return an error response
        JsonResponse json = new JsonResponse();
        System.out.println("userModuleRepositoryService.getByUserId(authRequest.getUsername()).getTenantId(): " + userModuleRepositoryService.getByUserId(authRequest).getTenantId());

        // Log invalid login attempt, including the user's IP address
        String clientIpAddress = request.getRemoteAddr();
        userModuleRepositoryService.addWrongPasswordLog(authRequest, clientIpAddress);

        // Create and return an error response indicating invalid login
        json.setStatus(false);
        json.setToken("Invalid user request!");
        json.setTenantId("Invalid user request!");
        json.setUser(null);
        return ResponseEntity.badRequest().body(json); // Return a bad request response
        
    }

    /**
     * API endpoint to extend the expiration of an existing JWT token.
     * 
     * @param token The JWT token whose expiration time needs to be extended.
     * @return A {@link ResponseEntity} containing the extended token or an error message if the operation fails.
     */
    @GetMapping("/extendTokenExpiration")
    public ResponseEntity<String> extendTokenExpiration(@RequestParam String token) {
        System.out.println("token : " + token);
        
        // Try to extend the token's expiration time using the JwtService
        String extendedToken = jwtService.extendTokenExpiration(token);
        
        // If the token extension was successful, return the new token
        if (extendedToken != null) {
            return ResponseEntity.ok().body(extendedToken); // Return the extended token
        } else {
            // If it fails, return an error message
            return ResponseEntity.badRequest().body("Failed to extend token expiration");
        }
    }
    @PostMapping("/register-new-tenant/{tenantid}")
    public ResponseEntity<String> registernewtenant(@PathVariable("tenantid") String tenantid) {
    	try {
    		userModuleRepositoryService = factory.getuserModuleRepositoryService();
        	
        	userModuleRepositoryService.registerNewTenant(tenantid);
        	return ResponseEntity.ok().body("Tenant registered successfully");
    	}catch(Exception e) {
    		e.printStackTrace();
    		return ResponseEntity.badRequest().body("Tenant registration unsuccessful");
    	}
    }
}
