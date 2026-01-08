package in.co.greenwave.taskapi.dao; // Package declaration for RestDAO interface

import in.co.greenwave.taskapi.model.TaskDetail; // Importing TaskDetail model class

// Defining the RestDAO interface for data access methods related to task operations
public interface RestDAO {

    /**
     * Fetches the status of a task based on its ID and tenant ID.
     * 
     * @param taskid The ID of the task
     * @param tenantId The ID of the tenant
     * @return The status of the specified task as a String
     */
    public String getTaskStatus(String taskid, String tenantId);

    /**
     * Fetches detailed information about a task using its ID and tenant ID.
     * 
     * @param taskid The ID of the task
     * @param tenantId The ID of the tenant
     * @return TaskDetail object containing details of the specified task
     */
    public TaskDetail getTaskDetailByID(String taskid, String tenantId);

    /**
     * Deletes a task based on its ID and tenant ID.
     * 
     * @param taskId The ID of the task to be deleted
     * @param tenantId The ID of the tenant associated with the task
     */
    public void deleteTask(String taskId, String tenantId);
}
