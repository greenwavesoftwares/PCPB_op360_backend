package in.co.greenwave.taskapi.controller;

// Import necessary libraries and packages
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired; // For dependency injection
import org.springframework.http.HttpStatus; // For HTTP status codes
import org.springframework.http.ResponseEntity; // For HTTP responses
import org.springframework.web.bind.annotation.CrossOrigin; // For CORS support
import org.springframework.web.bind.annotation.DeleteMapping; // For handling DELETE requests
import org.springframework.web.bind.annotation.GetMapping; // For handling GET requests
import org.springframework.web.bind.annotation.PathVariable; // For extracting values from the URL
import org.springframework.web.bind.annotation.PostMapping; // For handling POST requests
import org.springframework.web.bind.annotation.PutMapping; // For handling PUT requests
import org.springframework.web.bind.annotation.RequestBody; // For reading request body
import org.springframework.web.bind.annotation.RequestMapping; // For mapping HTTP requests to handler methods
import org.springframework.web.bind.annotation.RestController; // For marking a class as a REST controller

import in.co.greenwave.taskapi.dao.RestDAO; // Importing RestDAO interface
import in.co.greenwave.taskapi.dao.TaskMasterDAO; // Importing TaskMasterDAO interface
import in.co.greenwave.taskapi.dao.factory.DAOFactory; // Importing DAOFactory for getting DAO instances
import in.co.greenwave.taskapi.dao.implementation.sqlserver.TaskMasterService; // Importing TaskMasterService for task-related operations
import in.co.greenwave.taskapi.model.ActivityConnection; // Importing ActivityConnection model
import in.co.greenwave.taskapi.model.TaskDetail; // Importing TaskDetail model

// Authored By Ashok
@RestController // Indicates that this class is a REST controller
@RequestMapping("task") // Maps HTTP requests to /task
@CrossOrigin("*") // Allows cross-origin requests from any domain
public class TaskController {

    @Autowired // Automatically injects DAOFactory bean
    DAOFactory factory;

    TaskMasterDAO taskmasterdao; // DAO for task management
    RestDAO restdao; // DAO for RESTful operations

    @Autowired // Automatically injects TaskMasterService bean
    TaskMasterService taskMasterService;

    /*
     * Endpoint to fetch all tasks details
     */
    // Added By Ashok
    
    @GetMapping(path = "/test")
    public String Test() {
    	return "Hello Ashok";
    }
    
    @GetMapping(path="/alltask/{creator}/{tenantId}", produces="application/json") // Maps GET requests for all tasks by creator and tenantId
    public ResponseEntity<Object> getAllTask(@PathVariable String creator, @PathVariable String tenantId) {
        taskmasterdao = factory.getTaskMasterService(); // Get the task master DAO
        List<TaskDetail> allTaskDetail = new ArrayList<TaskDetail>(); // Initialize a list to hold task details
        try {
            allTaskDetail.addAll(taskmasterdao.getAllTasksbyTenant(creator, tenantId)); // Fetch tasks by creator and tenantId
        } catch (Exception e) {
            e.printStackTrace(); // Print the stack trace in case of an error
            return new ResponseEntity<Object>("Error Occurred." + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR); // Return an error response
        }
        if (allTaskDetail.isEmpty()) // Check if no tasks were found
            return new ResponseEntity<Object>("UNAUTHORIZED ACCESS.", HttpStatus.UNAUTHORIZED); // Return unauthorized response
        return new ResponseEntity<Object>(allTaskDetail, HttpStatus.OK); // Return the list of tasks with a 200 OK status
    }

    /*
     * Endpoint to fetch Activity Connections by tenant and taskId
     */
    @GetMapping(path = "/acivityConnections/{taskId}/{tenantId}", produces = "application/json") // Maps GET requests for activity connections by taskId and tenantId
    public ResponseEntity<?> getAllActivityConnections(@PathVariable String taskId, @PathVariable String tenantId) {
        try {
            taskmasterdao = factory.getTaskMasterService(); // Get the task master DAO
            List<ActivityConnection> allActivityConnections = taskmasterdao.getActivityConnection(taskId, tenantId); // Fetch activity connections

            // Check if the result is empty
            if (allActivityConnections == null || allActivityConnections.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND) // Return a 404 NOT FOUND response if no connections are found
                                     .body("No Activity Connections found for taskId: " + taskId + " and tenantId: " + tenantId);
            }

            // Return the list of activity connections with a 200 OK status
            return ResponseEntity.ok(allActivityConnections); // Return activity connections with a 200 OK status
        } catch (Exception e) {
            // Log the error (optional)
            e.printStackTrace(); // Print the stack trace for debugging

            // Return 500 Internal Server Error with the error message
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR) // Return an internal server error
                                 .body("An error occurred while fetching activity connections: " + e.getMessage());
        }
    }

    /*
     * Delete task based on task id
     */
    // Added tenantId
    @DeleteMapping(path="deleteTask/{tenantId}") // Maps DELETE requests to delete a task by tenantId
    public ResponseEntity<Object> deleteTask(@PathVariable String tenantId, @RequestBody TaskDetail taskDetail) {
        restdao = factory.getRestfulService(); // Get the REST DAO
        String taskId = taskDetail.getTaskId(); // Get the task ID from the request body
        String taskStatus = restdao.getTaskStatus(taskId, tenantId); // Get the current status of the task
        try {
            if (!(taskStatus.equalsIgnoreCase("Published") || taskStatus.equalsIgnoreCase("Pending"))) { // Check if the task can be deleted
                restdao.deleteTask(taskId, tenantId); // Delete the task
                System.out.println("Task Deleted"); // Log the deletion
                return new ResponseEntity<Object>("SUCCESSFULLY DELETED", HttpStatus.OK); // Return success response
            } else {
                return new ResponseEntity<Object>("UNAUTHORIZED", HttpStatus.UNAUTHORIZED); // Return unauthorized response
            }
        } catch (Exception e) {
            e.printStackTrace(); // Print the stack trace for debugging
            return new ResponseEntity<Object>("Error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR); // Return error response
        }
    }

    /*
     * Endpoint to check if this Task ID is associated with any Job or Not
     */
    @GetMapping(path = "/checkJobExist/{taskId}/{tenantId}", produces = "application/json") // Maps GET requests to check if a job exists for a task ID
    public ResponseEntity<Boolean> checkJobExistsByTaskId(@PathVariable String taskId, @PathVariable String tenantId) {
        try {
            taskmasterdao = factory.getTaskMasterService(); // Get the task master DAO
            Boolean isJobExist = taskmasterdao.checkJobExistsByTaskId(taskId, tenantId); // Check if the job exists for the task ID

            if (isJobExist != null && isJobExist) { // If a job exists
                return ResponseEntity.ok(true); // Return 200 OK with true
            } else {
                // Return 404 NOT FOUND if the job doesn't exist
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                     .body(false); // Return 404 NOT FOUND with false
            }
        } catch (Exception e) {
            // Log the exception and return 500 INTERNAL SERVER ERROR
            e.printStackTrace(); // Print the stack trace for debugging
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR) // Return an internal server error
                                 .body(false); // Return false
        }
    }

    /*
     * Endpoint to check if this Asset Group Name is associated with any Task or Not
     */
    @GetMapping(path = "/checkTaskExist/{assetGroupName}/{tenantId}", produces = "application/json") // Maps GET requests to check if a task exists for an asset group name
    public ResponseEntity<Boolean> checkTaskExistsByAssetGroupId(@PathVariable String assetGroupName, @PathVariable String tenantId) {
        try {
            taskmasterdao = factory.getTaskMasterService(); // Get the task master DAO
            Boolean isJobExist = taskmasterdao.checkTaskExistsByAssetGroupId(assetGroupName, tenantId); // Check if the task exists for the asset group name

            if (isJobExist != null && isJobExist) { // If a task exists
                return ResponseEntity.ok(true); // Return 200 OK with true
            } else {
                // Return 404 NOT FOUND if the task doesn't exist
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                     .body(false); // Return 404 NOT FOUND with false
            }
        } catch (Exception e) {
            // Log the exception and return 500 INTERNAL SERVER ERROR
            e.printStackTrace(); // Print the stack trace for debugging
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR) // Return an internal server error
                                 .body(false); // Return false
        }
    }

    /*
     * Endpoint to update task details based on task id
     */
    @PutMapping(path="updateStatusOrRemarks/{tenantId}", consumes="application/json") // Maps PUT requests to update task status or remarks
    public ResponseEntity<Object> updateTaskStatus(@PathVariable String tenantId, @RequestBody TaskDetail task) {
        taskmasterdao = factory.getTaskMasterService(); // Get the task master DAO
        try {
            taskmasterdao.updateTaskStatus(task.getTaskId(), task.getStatus(), tenantId); // Update task status
            taskmasterdao.updateTaskRemarks(task.getTaskId(), task.getRemarks(), tenantId); // Update task remarks
            return new ResponseEntity<Object>("SUCCESSFULLY UPDATED", HttpStatus.OK); // Return success response
        } catch (Exception e) {
            e.printStackTrace(); // Print the stack trace for debugging
            return new ResponseEntity<Object>("Error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR); // Return error response
        }
    }

	
    
    
    
    /*
     * Endpoint to Insert ActivityConnections and TenantId to Activity Connections table
     */
    @PostMapping(path = "/saveActivityConnection/{tenantId}", consumes = "application/json", produces = "application/json") // Mapping POST requests to /saveActivityConnection/{tenantId} with JSON input and output
    public ResponseEntity<Map<String, Object>> createActivityConnections(
            @RequestBody List<ActivityConnection> activityConnections, // Request body containing a list of ActivityConnection objects
            @PathVariable String tenantId) { // Path variable for tenant ID

        // Create a map to hold the response data
        Map<String, Object> response = new HashMap<>(); // Initializing a new HashMap for response data

        try {
            // Call the service method
            ActivityConnection actCon = activityConnections.get(0); // Getting the first ActivityConnection from the list

            // Checking if taskId is null
            if (actCon.getTaskId() == null) { // If taskId is null
                response.put("status", HttpStatus.CREATED.value()); // Setting HTTP status code to 201 (Created)
                response.put("message", "Activity Connections created successfully."); // Adding success message to response
                return ResponseEntity.status(HttpStatus.CREATED).body(response); // Returning response with 201 status
            }

            taskmasterdao = factory.getTaskMasterService(); // Getting TaskMasterDAO from factory
            boolean isCreated = taskmasterdao.createActivityConnections(activityConnections, tenantId); // Creating activity connections

            if (isCreated) { // If creation is successful
                response.put("status", HttpStatus.CREATED.value()); // Setting HTTP status code to 201 (Created)
                response.put("message", "Activity Connections created successfully."); // Adding success message to response
                return ResponseEntity.status(HttpStatus.CREATED).body(response); // Returning response with 201 status
            } else { // If creation failed
                response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value()); // Setting HTTP status code to 500 (Internal Server Error)
                response.put("message", "Failed to create Activity Connections."); // Adding failure message to response
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response); // Returning response with 500 status
            }
        } catch (Exception e) { // Catching any exceptions
            // Log the exception
            e.printStackTrace(); // Printing the stack trace for debugging
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value()); // Setting HTTP status code to 500 (Internal Server Error)
            response.put("message", "An error occurred: " + e.getMessage()); // Adding error message to response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response); // Returning response with 500 status
        }
    }

    /*
     * Endpoint to update task details
     */
    @PostMapping("/saveTaskDetails/{tenantId}") // Mapping POST requests to /saveTaskDetails/{tenantId}
    public ResponseEntity<Map<String, Object>> updateTask(@PathVariable String tenantId, @RequestBody TaskDetail taskDetail) { // Method to update task details
        Map<String, Object> response = new HashMap<>(); // Initializing a new HashMap for response data
        try {
            taskmasterdao = factory.getTaskMasterService(); // Getting TaskMasterDAO from factory
            taskmasterdao.updateTask(tenantId, taskDetail); // Calling service method to update task details

            response.put("status", HttpStatus.OK.value()); // Setting HTTP status code to 200 (OK)
            response.put("message", "Task updated successfully"); // Adding success message to response

            return new ResponseEntity<>(response, HttpStatus.OK); // Returning response with 200 status
        } catch (Exception e) { // Catching any exceptions
            e.printStackTrace(); // Printing the stack trace for debugging
            System.out.println("error : " + e.getMessage()); // Logging the error message
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value()); // Setting HTTP status code to 500 (Internal Server Error)
            response.put("message", "Error updating task: " + e.getMessage()); // Adding error message to response

            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR); // Returning response with 500 status
        }
    }
    
    
    /*
     * Endpoint to register-new-tenant and update the jdbc connections
     */
    @PostMapping("/register-new-tenant/{tenantid}")
    public ResponseEntity<String> registernewtenant(@PathVariable("tenantid") String tenantid) {
    	try {
    		taskmasterdao = factory.getTaskMasterService();
        	
    		taskmasterdao.registerNewTenant(tenantid);
        	return ResponseEntity.ok().body("Tenant registered successfully");
    	}catch(Exception e) {
    		e.printStackTrace();
    		return ResponseEntity.badRequest().body("Tenant registration unsuccessful");
    	}
    }

}

