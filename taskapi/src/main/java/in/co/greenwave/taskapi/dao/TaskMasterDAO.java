package in.co.greenwave.taskapi.dao; // Package declaration for TaskMasterDAO interface

import java.util.List; // Importing List interface for returning lists of objects
import java.util.Map; // Importing Map interface (not used in this snippet but might be used in the implementation)

import in.co.greenwave.taskapi.model.ActivityConnection; // Importing ActivityConnection model class
import in.co.greenwave.taskapi.model.ActivityDetails; // Importing ActivityDetails model class (not used in this snippet)
import in.co.greenwave.taskapi.model.TaskDetail; // Importing TaskDetail model class

// Defining the TaskMasterDAO interface for data access methods related to task management
public interface TaskMasterDAO {

    // New Task API services added by ASHOK

    /**
     * Retrieves a list of TaskDetail objects based on the creator and tenant ID.
     * 
     * @param creator The creator of the tasks
     * @param tenantId The ID of the tenant
     * @return List of TaskDetail objects associated with the given creator and tenant
     */
    public List<TaskDetail> getAllTasksbyTenant(String creator, String tenantId); 

    /**
     * Retrieves a list of ActivityConnection objects based on the task ID and tenant ID.
     * 
     * @param taskId The ID of the task
     * @param tenantId The ID of the tenant
     * @return List of ActivityConnection objects associated with the given task and tenant
     */
    public List<ActivityConnection> getActivityConnection(String taskId, String tenantId); 

    /**
     * Checks if a job exists by its task ID for a specific tenant.
     * 
     * @param taskId The ID of the task
     * @param tenantId The ID of the tenant
     * @return Boolean indicating whether the job exists (true) or not (false)
     */
    public Boolean checkJobExistsByTaskId(String taskId, String tenantId); 

    /**
     * Checks if a task exists by its asset group ID for a specific tenant.
     * 
     * @param assetGroupName The name of the asset group
     * @param tenantId The ID of the tenant
     * @return Boolean indicating whether the task exists (true) or not (false)
     */
    public Boolean checkTaskExistsByAssetGroupId(String assetGroupName, String tenantId); 

    /**
     * Updates the status of a task identified by its task ID for a specific tenant.
     * 
     * @param taskId The ID of the task
     * @param status The new status to set for the task
     * @param tenantId The ID of the tenant
     */
    public void updateTaskStatus(String taskId, String status, String tenantId); 

    /**
     * Updates the remarks associated with a task identified by its task ID for a specific tenant.
     * 
     * @param taskId The ID of the task
     * @param remarks The new remarks to set for the task
     * @param tenantId The ID of the tenant
     */
    public void updateTaskRemarks(String taskId, String remarks, String tenantId); 

    /**
     * Creates activity connections for the specified tenant using a list of ActivityConnection objects.
     * 
     * @param activityConnections The list of ActivityConnection objects to create
     * @param tenantId The ID of the tenant
     * @return Boolean indicating whether the activity connections were created successfully (true) or not (false)
     */
    public boolean createActivityConnections(List<ActivityConnection> activityConnections, String tenantId); 

    /**
     * Updates task details for a specific tenant using a TaskDetail object.
     * 
     * @param tenantId The ID of the tenant
     * @param taskDetail The TaskDetail object containing updated task information
     */
    public void updateTask(String tenantId, TaskDetail taskDetail); 
    
    
    void registerNewTenant(String tenantid);

}
