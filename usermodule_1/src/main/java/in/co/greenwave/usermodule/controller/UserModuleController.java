package in.co.greenwave.usermodule.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.co.greenwave.usermodule.dao.UserModuleDAO;
import in.co.greenwave.usermodule.dao.factory.DAOFactory;
import in.co.greenwave.usermodule.model.Page;
import in.co.greenwave.usermodule.model.User;
import in.co.greenwave.usermodule.model.UserDepartment;
import in.co.greenwave.usermodule.model.UserGroup;
import in.co.greenwave.usermodule.model.UserLog;


@RestController
@RequestMapping("/users")
public class UserModuleController {
	
	@Autowired
	private DAOFactory factory;

	private UserModuleDAO usermoduledao;
	
	
	   Logger logger
       = LoggerFactory.getLogger(UserModuleController.class);
	
	//**************************************** GET Mappings *********************************************
	
	@GetMapping("/allusers/{tenantId}")
	// This annotation defines an HTTP GET request mapping for the endpoint "/allusers/{tenantId}".
	// The {tenantId} is a path variable that the method will capture as a String.
	public ResponseEntity<Object> getAllUsers(@PathVariable String tenantId) {
	    // The method returns a ResponseEntity<Object>, which allows you to return an object (in this case, a list of users)
	    // along with an HTTP status code. It takes a String parameter tenantId, extracted from the request URL.
	    
	    logger.info("getAllUsers() called "+tenantId);

	    usermoduledao = factory.getUserModuleService();
	    // Here, usermoduledao is being assigned an instance of UserModuleService via a factory.
	    // This is a managed factory pattern to manage the above service instance.
	    
	    List<User> allUserDetails = new ArrayList<>();
	    // Initialize an empty ArrayList to hold the user details.
	    
	    try {
	        allUserDetails = usermoduledao.getAllUsersDetails(tenantId);
	        // Within the try block, the method retrieves all user details for the specified tenantId
	        // by calling the appropriate method on usermoduledao.
	    } catch (Exception e) {
	        e.printStackTrace();
	        // In case of any exception, the stack trace is printed for debugging purposes.
	        
	        return new ResponseEntity<Object>("Error occured : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	        // Return an error message and an HTTP status code 500 (Internal Server Error) in the response.
	    }
	    
	    return new ResponseEntity<Object>(allUserDetails, HttpStatus.OK);
	    // If everything goes well, return the list of users along with HTTP status code 200 (OK).
	}

	
	@GetMapping("/allgroups/{tenantId}")
	// This defines a GET endpoint that maps to the path "/allgroups/{tenantId}".
	// {tenantId} is captured as a path variable and passed to the method.

	public ResponseEntity<Object> getAllGroups(@PathVariable String tenantId) {
	    // The method returns a ResponseEntity<Object> to wrap the response data and HTTP status code.
	    // The tenantId is extracted from the path variable.
	    
	    logger.info("getAllGroups() called "+tenantId);
	    
	    usermoduledao = factory.getUserModuleService();
	    // Fetches the UserModuleService instance through the factory.
	    // This is a managed factory pattern to manage the above service instance.
	    
	    List<UserGroup> allGroupDetails = new ArrayList<>();
	    // Initializes an empty list of UserGroup to hold group details.
	    
	    try {
	        allGroupDetails = usermoduledao.getAllGroups(tenantId);
	        // Calls the DAO method to retrieve all groups for the given tenantId.
	        // Assumes that `getAllGroups` is a well-defined method that returns a list of UserGroup objects.
	    } catch (Exception e) {
	        e.printStackTrace();
	        // Logs the exception stack trace for debugging purposes.
	        
	        return new ResponseEntity<Object>("Error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	        // Returns an error message along with an HTTP 500 Internal Server Error status.
	    }
	    
	    return new ResponseEntity<Object>(allGroupDetails, HttpStatus.OK);
	    // Returns the list of groups and a 200 OK status when no exceptions occur.
	}

	
	
	@GetMapping("/alldepartments/{tenantId}")
	// This maps an HTTP GET request to the "/alldepartments/{tenantId}" endpoint.
	// The {tenantId} in the URL is mapped to the method parameter via the @PathVariable annotation.

	public ResponseEntity<Object> getAllDepartments(@PathVariable String tenantId) {
	    // This method returns a ResponseEntity<Object> containing the department details or an error message.
	    // It receives tenantId as a path variable to identify which tenant's departments should be retrieved.
	    
		logger.info("getAllDepartments() called "+tenantId);

	    
	    usermoduledao = factory.getUserModuleService();
	    // Fetches the UserModuleService via a factory. This approach is acceptable, but using Spring's dependency injection (DI) would be more standard and maintainable.
	    
	    List<UserDepartment> allDepartmentDetails = new ArrayList<>();
	    // Initializes an empty list to hold department details.
	    
	    try {
	        allDepartmentDetails = usermoduledao.getAllDepartments(tenantId);
	        // Fetches all departments for the provided tenantId by invoking the appropriate method from the usermoduledao.
	    } catch (Exception e) {
	        e.printStackTrace();
	        // If an exception occurs, prints the stack trace for debugging purposes.
	        
	        return new ResponseEntity<Object>("Error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	        // Returns an error message and the HTTP status 500 (Internal Server Error) in case of an exception.
	    }
	    
	    return new ResponseEntity<Object>(allDepartmentDetails, HttpStatus.OK);
	    // If no exceptions occur, returns the list of departments along with an HTTP status of 200 (OK).
	}

	
	@GetMapping("/pages/{tenantId}")
	// Maps an HTTP GET request to "/pages/{tenantId}" where tenantId is passed as a path variable.

	public ResponseEntity<Object> getAllPages(@PathVariable String tenantId) {
	    // The method takes tenantId as a path variable and returns a ResponseEntity<Object>.
	    // The response includes the page details or an error message with the appropriate HTTP status.

		logger.info("getAllPages() called "+tenantId);

	    usermoduledao = factory.getUserModuleService();
	    // Retrieves the UserModuleService via a factory method. Using dependency injection (DI) would be more maintainable and cleaner.

	    List<Page> allPages = new ArrayList<>();
	    // Initializes an empty list to store page details.

	    try { 
	    	// Fetches all page details for the provided tenantId by calling the appropriate method from usermoduledao.
	        allPages = usermoduledao.getAllPageDetails(tenantId);
	       
	    } catch (Exception e) {
	        e.printStackTrace();
	        // If an exception occurs, it logs the stack trace for debugging purposes.

	        return new ResponseEntity<Object>("Error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	        // Returns an error message and a 500 Internal Server Error status in case of an exception.
	    }

	    return new ResponseEntity<Object>(allPages, HttpStatus.OK);
	    // Returns the list of pages and an HTTP 200 OK status if no exception occurs.
	}

	
	@GetMapping("/pagesMap/{tenantId}")
	// Maps an HTTP GET request to "/pagesMap/{tenantId}" where tenantId is passed as a path variable.

	public ResponseEntity<Object> getPagesMap(@PathVariable String tenantId) {
	    // Takes tenantId as a path variable and returns a ResponseEntity<Object>.
	    // The response includes a map of pages or an error message with the appropriate HTTP status.

		logger.info("getPagesMap() called "+tenantId);
	    // Logs the tenantId for tracking purpose.

	    usermoduledao = factory.getUserModuleService();
	   

	    Map<String, List<Page>> pagesMap = new HashMap<>();
	    // Initializes an empty HashMap to store the map of pages.

	    try {
	        pagesMap = usermoduledao.getPagesMap(tenantId);
	        // Fetches the map of pages for the provided tenantId using usermoduledao.
	    } catch (Exception e) {
	        e.printStackTrace();
	        // If an exception occurs, prints the stack trace for debugging purposes.

	        return new ResponseEntity<Object>("Error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	        // Returns an error message and a 500 Internal Server Error status in case of an exception.
	    }

	    return new ResponseEntity<Object>(pagesMap, HttpStatus.OK);
	    // Returns the map of pages and an HTTP 200 OK status if no exception occurs.
	}

	
	@GetMapping("/{userId}/{tenantId}")
	// Maps the HTTP GET request to "/{userId}/{tenantId}" where both userId and tenantId are path variables.

	public ResponseEntity<Object> getUserDetails(@PathVariable String tenantId, @PathVariable String userId) {
	    // This method takes tenantId and userId as path variables and returns a ResponseEntity<Object>.
	    // It returns the user details or an error message along with the appropriate HTTP status.

//	    System.out.println("getUserDetails() called " + tenantId);
	   

	    usermoduledao = factory.getUserModuleService();
	    // Retrieves the UserModuleService using the factory

	    User userDetails = new User();
	    // Initializes an empty User object.

	    try {
	        userDetails = usermoduledao.getUserDetails(tenantId, userId);
	        // Fetches the user details for the given tenantId and userId by calling the appropriate method from usermoduledao.
	    } catch (Exception e) {
	        e.printStackTrace();
	        // Prints the stack trace of any exception that occurs. This should be replaced with proper logging.

	        return new ResponseEntity<Object>("Error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	        // Returns a 500 Internal Server Error response with the error message if an exception occurs.
	    }

	    return new ResponseEntity<Object>(userDetails, HttpStatus.OK);
	    // Returns the user details and an HTTP 200 OK status if the request is successful.
	}

	
	
	@GetMapping("/logs/{userId}/{tenantId}")
	// Maps the HTTP GET request to "/logs/{userId}/{tenantId}" where both userId and tenantId are extracted as path variables.

	public ResponseEntity<Object> getUserLogs(@PathVariable String tenantId, @PathVariable String userId) {
	    // The method accepts tenantId and userId as path variables and returns a ResponseEntity<Object>.
	    // The response contains user logs or an error message along with the appropriate HTTP status.

	   logger.info("getUserLogs() called " + tenantId);
	    // Logs the tenantId when the method is invoked. 

	    usermoduledao = factory.getUserModuleService();
	    // The UserModuleService is retrieved from the factory.

	    List<UserLog> userLogs = new ArrayList<>();
	    // Initializes an empty list of user logs.

	    try {
	        userLogs = usermoduledao.getUserLogs(tenantId, userId);
	        // Fetches the user logs for the specified tenantId and userId from usermoduledao.
	    } catch (Exception e) {
	        e.printStackTrace();
	        // Prints the exception stack trace, which is not recommended for production. A logging framework should be used instead.

	        return new ResponseEntity<Object>("Error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	        // Returns a 500 Internal Server Error response along with the error message if an exception occurs.
	    }

	    return new ResponseEntity<Object>(userLogs, HttpStatus.OK);
	    // Returns the list of user logs and an HTTP 200 OK status if the request is successful.
	}

	@GetMapping("/groups/{groupId}/{groupName}/{tenantId}")
	// Maps the HTTP GET request to "/groups/{groupId}/{tenantId}" where groupId and tenantId are path variables.

	public ResponseEntity<Object> checkGroupAssociation(@PathVariable String tenantId, @PathVariable String groupId,@PathVariable String groupName) {
	    // The method accepts tenantId and groupId as path variables and returns a ResponseEntity<Object>.
	    // It returns a boolean indicating whether the group is associated with the tenant or an error message if an exception occurs.

	    logger.info("checkGroupAssociation() called " + tenantId);
	    // Logs the tenantId when the method is invoked

	    usermoduledao = factory.getUserModuleService();
	    // Retrieves the UserModuleService via a factory. Dependency injection is a better approach for obtaining this service.
	    String reason="";
//	    boolean associated = false;
	    // Initializes a boolean flag to track whether the group is associated with the tenant.

	    try {
	        reason = usermoduledao.checkGroupAssociation(groupId,groupName, tenantId);
	        // Calls usermoduledao to check if the group is associated with the tenant.
	    } catch (Exception e) {
	        e.printStackTrace();
	        // Prints the exception stack trace, which is not suitable for production environments. Use a logging framework instead.

	        return new ResponseEntity<Object>("Error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	        // Returns a 500 Internal Server Error response with the exception message if an error occurs.
	    }

	    return new ResponseEntity<Object>(reason, HttpStatus.OK);
	    // Returns the association status (true/false) and an HTTP 200 OK status if the request is successful.
	}

	
	@GetMapping("/departments/{deptId}/{tenantId}")
	// Maps the HTTP GET request to "/departments/{deptId}/{tenantId}" where deptId and tenantId are path variables.

	public ResponseEntity<Object> checkDepartmentAssociation(@PathVariable String tenantId, @PathVariable String deptId) {
	    // The method accepts tenantId and deptId as path variables and returns a ResponseEntity<Object>.
	    // It checks whether the department is associated with the tenant and returns the result or an error message.

	    logger.info("checkDepartmentAssociation() called " + tenantId);
	    // Logs the tenantId when the method is invoked.

	    usermoduledao = factory.getUserModuleService();
	    // The UserModuleService is retrieved from the factory. Dependency injection is a cleaner approach to obtaining this service.

	    String reason = "";
	    // Initializes a boolean flag to track whether the department is associated with the tenant.

	    try {
	    	reason = usermoduledao.checkDepartmentAssociation(deptId, tenantId);
	        // Calls usermoduledao to check if the department is associated with the tenant.
	    } catch (Exception e) {
	        e.printStackTrace();
	        // Prints the exception stack trace, which is not ideal for production. Logging frameworks should be used instead.

	        return new ResponseEntity<Object>("Error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	        // Returns a 500 Internal Server Error response with the exception message if an error occurs.
	        
	    }
	    

	    return new ResponseEntity<Object>(reason, HttpStatus.OK);
	}

	
	    @GetMapping("/password/{userID}/{tenantId}/{oldPassword}")
	 // Maps the HTTP GET request to "/password/{userID}/{tenantId}/{oldPassword}" 
	 // where userID, tenantId, and oldPassword are path variables.

	 public ResponseEntity<Object> checkOldPassword(@PathVariable String oldPassword, @PathVariable String userID, @PathVariable String tenantId) {
	     // The method accepts userID, tenantId, and oldPassword as path variables and returns a ResponseEntity<Object>.
	     // It verifies if the provided old password is correct for the given user and tenant.

	    logger.info("checkOldPassword() called " + userID + " Old password: " + oldPassword + " TenantID: " + tenantId);
	     // Logs the invocation of the method with parameters for debugging purposes. 
	    

	     usermoduledao = factory.getUserModuleService();
	     // Retrieves the UserModuleService from the factory. 

	     boolean isVerified = false;
	     // Initializes a boolean flag to track whether the old password is verified.

	     try {
	         isVerified = usermoduledao.checkOldPassword(userID, oldPassword, tenantId);
	         // Calls usermoduledao to check if the provided old password is correct for the user and tenant.
	     } catch (Exception e) {
	         e.printStackTrace();
	         // Prints the exception stack trace, which is not ideal for production. Logging frameworks should be used instead.

	         return new ResponseEntity<Object>("Error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	         // Returns a 500 Internal Server Error response with the exception message if an error occurs.
	     }

	     return new ResponseEntity<Object>(isVerified, HttpStatus.OK);
	     // Returns the verification status (true/false) and an HTTP 200 OK status if the request is successful.
	 }
	    
	    
	    
	    @GetMapping("/groupsByUser/{tenantId}")
	    public ResponseEntity<Object> getAllUsersGrouped(@PathVariable String tenantId) {

	        logger.info("getAllUsersGrouped() called for tenantId: " + tenantId);

	        usermoduledao = factory.getUserModuleService(); 
	        // Matches your existing pattern

	        Map<String, List<String>> usersGroupedMap = new HashMap<>();

	        try {
	        	usersGroupedMap = usermoduledao.getAllUserIdsByGroup(tenantId);
	        } catch (Exception e) {
	            e.printStackTrace(); // same style as your example
	            return new ResponseEntity<>("Error occurred: " + e.getMessage(),
	                                        HttpStatus.INTERNAL_SERVER_ERROR);
	        }

	        return new ResponseEntity<>(usersGroupedMap, HttpStatus.OK);
	    }


	
	
	//**************************************** POST Mappings *********************************************
	
    /**
     * Saves or updates the user details.
     * This method handles both the creation and updating of user data.
     *
     * @param user The User object containing the details to be saved or updated.
     * @return ResponseEntity containing the status of the operation.
     */
	 @PostMapping("/")
    public ResponseEntity<Object> saveOrUpdate(@RequestBody User user) {
        // Log to console that the method is called
        System.out.println("saveOrUpdate() called ");

        // Get the instance of the UserModuleService from the factory
        usermoduledao = factory.getUserModuleService();

        // Variable to track whether the user creation/update was successful
        boolean userCreated = false;

        try {
            // Try to save or update the user details through the UserModuleDAO
        	userCreated= usermoduledao.saveOrUpdateUser(user);
            if(!userCreated)
            	throw new RuntimeException("Unable to create user");
        }
        catch (Exception e) {
            // Print the exception stack trace for debugging
            e.printStackTrace();
            
            // Return an error response if an exception occurs
            return new ResponseEntity<Object>("Error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // Return a successful response with the result of the save/update operation
        return new ResponseEntity<Object>(userCreated, HttpStatus.OK);
    }

	
	
	    /**
	     * Saves a new user group.
	     * This method handles the creation of a new user group.
	     *
	     * @param group The UserGroup object containing the details of the new group.
	     * @return ResponseEntity containing the status of the operation.
	     */
	    @PostMapping("/groups")
	    public ResponseEntity<Object> saveNewGroup(@RequestBody UserGroup group) {
	        // Log to console that the method is called
	        System.out.println("saveNewGroup() called ");

	        // Get the instance of the UserModuleService from the factory
	        usermoduledao = factory.getUserModuleService();

	        // Variable to track whether the new group was successfully added
	        boolean newGroupAdded = false;

	        try {
	            // Try to save the new group through the UserModuleDAO
	            newGroupAdded = usermoduledao.saveNewGroup(group);
	        }
	        catch (Exception e) {
	            // Print the exception stack trace for debugging
	            e.printStackTrace();
	            
	            // Return an error response if an exception occurs
	            return new ResponseEntity<Object>("Error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	        }

	        // Return a successful response with the result of the group save operation
	        return new ResponseEntity<Object>(newGroupAdded, HttpStatus.OK);
	    }

	
	
	    /**
	     * Saves a new user department.
	     * This method handles the creation of a new department.
	     *
	     * @param department The UserDepartment object containing the details of the new department.
	     * @return ResponseEntity containing the status of the operation.
	     */
	    @PostMapping("/departments")
	    public ResponseEntity<Object> saveNewDepartment(@RequestBody UserDepartment department) {
	        // Log to console that the method is called
	        System.out.println("saveNewDepartment() called ");

	        // Get the instance of the UserModuleService from the factory
	        usermoduledao = factory.getUserModuleService();

	        // Variable to track whether the new department was successfully added
	        boolean newDepartmentAdded = false;

	        try {
	            // Try to save the new department through the UserModuleDAO
	            newDepartmentAdded = usermoduledao.saveNewDepartment(department);
	        }
	        catch (Exception e) {
	            // Print the exception stack trace for debugging
	            e.printStackTrace();
	            
	            // Return an error response if an exception occurs
	            return new ResponseEntity<Object>("Error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	        }

	        // Return a successful response with the result of the department save operation
	        return new ResponseEntity<Object>(newDepartmentAdded, HttpStatus.OK);
	    }

	
	
	    /**
	     * Merges user groups based on the provided group details.
	     * This method saves merged groups by associating users with a specific group.
	     *
	     * @param groupDetails JSON string containing the details for merging groups,
	     *                     including groupID, usersList, and tenantId.
	     * @return ResponseEntity containing the status of the operation.
	     */
	    @PostMapping("/mergeGroups")
	    public ResponseEntity<Object> saveMergedGroups(@RequestBody String groupDetails) {
	        // Log to console that the method is called
	        System.out.println("saveMergedGroups() called ");

	        // Convert the incoming string (groupDetails) into a JSONObject
	        JSONObject jsonObject = new JSONObject(groupDetails);

	        // Extract the groupID from the JSON object
	        String groupId = jsonObject.getString("groupID");

	        // Get the list of users from the JSON array
	        JSONArray usersArray = jsonObject.getJSONArray("usersList");

	        // Initialize a list to store user IDs
	        List<String> users = new ArrayList<>();

	        // Loop through the usersArray and add each user to the users list
	        for (int i = 0; i < usersArray.length(); i++) {
	            users.add(usersArray.getString(i));
	        }

	        // Extract the tenantId from the JSON object
	        String tenantId = jsonObject.getString("tenantId");

	        // Get the instance of the UserModuleService from the factory
	        usermoduledao = factory.getUserModuleService();

	        // Variable to track whether the groups were successfully merged
	        boolean isGroupsMerged = false;

	        try {
	            // Try to merge the groups through the UserModuleDAO
	            isGroupsMerged = usermoduledao.saveMergedGroups(users, groupId, tenantId);
	        }
	        catch (Exception e) {
	            // Print the exception stack trace for debugging
	            e.printStackTrace();
	            
	            // Return an error response if an exception occurs
	            return new ResponseEntity<Object>("Error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	        }

	        // Return a successful response with the result of the groups merge operation
	        return new ResponseEntity<Object>(isGroupsMerged, HttpStatus.OK);
	    }

	
	//**************************************** PUT Mapping *********************************
	
	    /**
	     * Updates an existing user group.
	     * This method is responsible for updating the details of an existing group.
	     *
	     * @param group The UserGroup object containing the updated details of the group.
	     * @return ResponseEntity containing the status of the operation.
	     */
	    @PutMapping("/groups")
	    public ResponseEntity<Object> updateGroups(@RequestBody UserGroup group) {
	        // Log to console that the method is called
	        System.out.println("updateGroups() called ");

	        // Get the instance of the UserModuleService from the factory
	        usermoduledao = factory.getUserModuleService();

	        // Variable to track whether the group was successfully updated
	        boolean isGroupUpdated = false;

	        try {
	            // Try to update the group through the UserModuleDAO
	            isGroupUpdated = usermoduledao.updateGroup(group);
	        }
	        catch (Exception e) {
	            // Print the exception stack trace for debugging
	            e.printStackTrace();
	            
	            // Return an error response if an exception occurs
	            return new ResponseEntity<Object>("Error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	        }

	        // Return a successful response with the result of the group update operation
	        return new ResponseEntity<Object>(isGroupUpdated, HttpStatus.OK);
	    }

	
	    /**
	     * Updates an existing user department.
	     * This method is responsible for updating the details of an existing department.
	     *
	     * @param department The UserDepartment object containing the updated details of the department.
	     * @return ResponseEntity containing the status of the operation.
	     */
	    @PutMapping("/departments")
	    public ResponseEntity<Object> updateDepartments(@RequestBody UserDepartment department) {
	        // Log to console that the method is called
	        System.out.println("updateDepartments() called ");

	        // Get the instance of the UserModuleService from the factory
	        usermoduledao = factory.getUserModuleService();

	        // Variable to track whether the department was successfully updated
	        boolean isDepartmentUpdated = false;

	        try {
	            // Try to update the department through the UserModuleDAO
	            isDepartmentUpdated = usermoduledao.updateDepartment(department);
	        }
	        catch (Exception e) {
	            // Print the exception stack trace for debugging
	            e.printStackTrace();
	            
	            // Return an error response if an exception occurs
	            return new ResponseEntity<Object>("Error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	        }

	        // Return a successful response with the result of the department update operation
	        return new ResponseEntity<Object>(isDepartmentUpdated, HttpStatus.OK);
	    }

	
	    /**
	     * Changes the password of an existing user.
	     * This method is responsible for updating the password of a user.
	     *
	     * @param user The User object containing the details of the user, including the new password.
	     * @return ResponseEntity containing the status of the password change operation.
	     */
	    @PutMapping("/password")
	    public ResponseEntity<Object> changePassword(@RequestBody User user) {
	        // Log to console that the method is called, along with the user ID for debugging
	        System.out.println("changePassword() called " + user.getUserID());

	        // Get the instance of the UserModuleService from the factory
	        usermoduledao = factory.getUserModuleService();

	        // Variable to track whether the password was successfully changed
	        boolean isPasswordChanged = false;

	        try {
	            // Try to change the password through the UserModuleDAO
	            isPasswordChanged = usermoduledao.changePassword(user);
	        }
	        catch (Exception e) {
	            // Print the exception stack trace for debugging
	            e.printStackTrace();
	            
	            // Return an error response if an exception occurs
	            return new ResponseEntity<Object>("Error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	        }

	        // Return a successful response with the result of the password change operation
	        return new ResponseEntity<Object>(isPasswordChanged, HttpStatus.OK);
	    }

	
	    /**
	     * Updates the login time of a user.
	     * This method is responsible for updating the user's last login time when they log in.
	     *
	     * @param user The User object containing the user details including the user ID and tenant ID.
	     * @return ResponseEntity containing the status of the login update operation.
	     */
	    @PutMapping("/login")
	    public ResponseEntity<Object> updateLogin(@RequestBody User user) {
	        // Log to console that the method is called, along with the user ID for debugging
	        System.out.println("updateLogin() called " + user.getUserID());

	        // Get the instance of the UserModuleService from the factory
	        usermoduledao = factory.getUserModuleService();

	        // Variable to track whether the login time was successfully updated
	        boolean lastLoginChanged = false;

	        try {
	            // Try to update the login time through the UserModuleDAO
	            lastLoginChanged = usermoduledao.updateLoginTime(user.getUserID(), user.getTenantId());
	        }
	        catch (Exception e) {
	            // Print the exception stack trace for debugging
	            e.printStackTrace();
	            
	            // Return an error response if an exception occurs
	            return new ResponseEntity<Object>("Error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	        }

	        // Return a successful response with the result of the login update operation
	        return new ResponseEntity<Object>(lastLoginChanged, HttpStatus.OK);
	    }

	
	//************************************** DELETE Mapping ****************************
	    /**
	     * Deletes a user group .
	     * This method is responsible for deleting the specified user group from the database.
	     *
	     * @param group The UserGroup object containing the details of the group to be deleted.
	     * @return ResponseEntity containing the status of the group deletion operation.
	     */
	    @DeleteMapping("/groups")
	    public ResponseEntity<Object> deleteGroup(@RequestBody UserGroup group) {
	        // Log to console that the method is called for deleting a group
	        System.out.println("deleteGroup() called ");

	        // Get the instance of the UserModuleService from the factory
	        usermoduledao = factory.getUserModuleService();

	        // Variable to track whether the group was successfully deleted
	        boolean isGroupDeleted = false;

	        try {
	            // Try to delete the group through the UserModuleDAO
	            isGroupDeleted = usermoduledao.deleteGroup(group);
	        }
	        catch (Exception e) {
	            // Print the exception stack trace for debugging
	            e.printStackTrace();
	            
	            // Return an error response if an exception occurs
	            return new ResponseEntity<Object>("Error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	        }

	        // Return a successful response with the result of the group deletion operation
	        return new ResponseEntity<Object>(isGroupDeleted, HttpStatus.OK);
	    }

	
	    /**
	     * Deletes a user department.
	     * This method is responsible for deleting the specified user department from the database.
	     *
	     * @param dept The UserDepartment object containing the details of the department to be deleted.
	     * @return ResponseEntity containing the status of the department deletion operation.
	     */
	    @DeleteMapping("/departments")
	    public ResponseEntity<Object> deleteDepartment(@RequestBody UserDepartment dept) {
	        // Log to console that the method is called for deleting a department
	        System.out.println("deleteDepartment() called ");
	        
	        // Get the instance of the UserModuleService from the factory
	        usermoduledao = factory.getUserModuleService();
	        
	        // Variable to track whether the department was successfully deleted
	        boolean isDeptDeleted = false;

	        try {
	            // Try to delete the department through the UserModuleDAO
	            isDeptDeleted = usermoduledao.deleteDepartment(dept);
	        }
	        catch (Exception e) {
	            // Print the exception stack trace for debugging
	            e.printStackTrace();
	            
	            // Return an error response if an exception occurs
	            return new ResponseEntity<Object>("Error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	        }

	        // Return a successful response with the result of the department deletion operation
	        return new ResponseEntity<Object>(isDeptDeleted, HttpStatus.OK);
	    }
	    
	    
	    @PostMapping("/register-new-tenant/{tenantid}")
	    public ResponseEntity<String> registernewtenant(@PathVariable("tenantid") String tenantid) {
	    	try {
	    		usermoduledao = factory.getUserModuleService();
	        	
	    		usermoduledao.registerNewTenant(tenantid);
	        	return ResponseEntity.ok().body("Tenant registered successfully");
	    	}catch(Exception e) {
	    		e.printStackTrace();
	    		return ResponseEntity.badRequest().body("Tenant registration unsuccessful");
	    	}
	    }

	
}
