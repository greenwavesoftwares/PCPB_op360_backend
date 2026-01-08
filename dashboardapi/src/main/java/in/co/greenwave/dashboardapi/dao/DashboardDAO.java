package in.co.greenwave.dashboardapi.dao; // Package declaration for DashboardDAO interface

import java.util.LinkedList; // Importing LinkedList for list operations
import java.util.List; // Importing List interface for returning lists of objects
import java.util.Map; // Importing Map interface for returning key-value pairs

import in.co.greenwave.dashboardapi.model.ActivityDetails; // Importing ActivityDetails model class
import in.co.greenwave.dashboardapi.model.FormDetails; // Importing FormDetails model class (not used in this snippet)
import in.co.greenwave.dashboardapi.model.JobDetails; // Importing JobDetails model class
import in.co.greenwave.dashboardapi.model.JobwiseCardData; // Importing JobwiseCardData model class
import in.co.greenwave.dashboardapi.model.Transactiondata; // Importing Transactiondata model class
import in.co.greenwave.dashboardapi.model.UserwiseJobDetails; // Importing UserwiseJobDetails model class

// Defining the DashboardDAO interface for data access methods related to the dashboard functionality
public interface DashboardDAO {

    /**
     * Fetches job-wise information for a specific tenant and date range.
     * 
     * @param tenantId The ID of the tenant
     * @param fromDate The start date for the date range
     * @param toDate The end date for the date range
     * @return List of JobwiseCardData objects containing job-wise information
     */
    public List<JobwiseCardData> fetchJobwiseInfo(String tenantId, String fromDate, String toDate); 

    /**
     * Fetches the activity status for a given job ID.
     * (Currently commented out; can be implemented if needed)
     * 
     * @param jobId The ID of the job
     * @return List of ActivityDetails objects related to the specified job ID
     */
    // public List<ActivityDetails> fetchActivityStatus(String jobId);

    /**
     * Fetches all job details for a specific tenant within a given date range.
     * 
     * @param tenantId The ID of the tenant
     * @param fromDate The start date for the date range
     * @param toDate The end date for the date range
     * @return List of JobDetails objects containing job details for the specified tenant and date range
     */
    public List<JobDetails> fetchAllJobDetails(String tenantId, String fromDate, String toDate); 

    /**
     * Fetches transaction details based on tenant ID, job ID, and activity ID.
     * 
     * @param tenantId The ID of the tenant
     * @param jobId The ID of the job
     * @param activityId The ID of the activity
     * @return List of Transactiondata objects related to the specified tenant, job, and activity
     */
    public List<Transactiondata> fetchFromActivityId(String tenantId, String jobId, String activityId); 

    /**
     * Fetches form details associated with a specific job and activity ID.
     * (Currently commented out; can be implemented if needed)
     * 
     * @param jobId The ID of the job
     * @param activityId The ID of the activity
     * @return FormDetails object containing the form details for the specified job and activity
     */
    // public FormDetails fetchFormDetails(String jobId, String activityId);

    /**
     * Fetches activity details for a given job ID and activity ID.
     * (Currently commented out; can be implemented if needed)
     * 
     * @param jobId The ID of the job
     * @param activityId The ID of the activity
     * @return ActivityDetails object containing details for the specified job and activity
     */
    // public ActivityDetails fetchActivitiy(String jobId, String activityId);

    /**
     * Fetches task-wise job details for a tenant within a specific date range.
     * 
     * @param tenantId The ID of the tenant
     * @param fromDate The start date for the date range
     * @param toDate The end date for the date range
     * @return Map with task IDs as keys and linked lists of UserwiseJobDetails as values
     */
    public Map<String, LinkedList<UserwiseJobDetails>> fetchTaskWiseJob(String tenantId, String fromDate, String toDate); 

    /**
     * Fetches tag-wise job details for a tenant within a specific date range.
     * 
     * @param tenantId The ID of the tenant
     * @param fromDate The start date for the date range
     * @param toDate The end date for the date range
     * @return Map with tag IDs as keys and linked lists of UserwiseJobDetails as values
     */
    public Map<String, LinkedList<UserwiseJobDetails>> fetchTagWiseJob(String tenantId, String fromDate, String toDate); 

    /**
     * Fetches user-wise job details for a tenant within a specific date range.
     * 
     * @param tenantId The ID of the tenant
     * @param fromDate The start date for the date range
     * @param toDate The end date for the date range
     * @return Map with user IDs as keys and linked lists of UserwiseJobDetails as values
     */
    public Map<String, LinkedList<UserwiseJobDetails>> fetchUserWiseJob(String tenantId, String fromDate, String toDate); 

    /**
     * Fetches asset-wise job details for a tenant within a specific date range.
     * 
     * @param tenantId The ID of the tenant
     * @param fromDate The start date for the date range
     * @param toDate The end date for the date range
     * @return Map with asset IDs as keys and linked lists of UserwiseJobDetails as values
     */
    public Map<String, LinkedList<UserwiseJobDetails>> fetchAssetWiseJob(String tenantId, String fromDate, String toDate); 

    /**
     * Fetches transaction details from a specific date range logbook based on the logbook name.
     * (Currently commented out; can be implemented if needed)
     * 
     * @param fromDate The start date for the date range
     * @param toDate The end date for the date range
     * @param fromName The name of the logbook
     * @return List of Transactiondata objects related to the specified date range and logbook name
     */
    // public List<Transactiondata> fetchFromDateRangeLogBook(String fromDate, String toDate, String fromName);

    /**
     * Fetches logbooks for a tenant within a specific date range.
     * 
     * @param tenantId The ID of the tenant
     * @param fromDate The start date for the date range
     * @param toDate The end date for the date range
     * @return List of logbook names for the specified tenant and date range
     */
    public List<String> fetchLogbooksByDateRange(String tenantId, String fromDate, String toDate); 

    /**
     * Fetches asset information for a tenant within a specific date range.
     * 
     * @param tenantId The ID of the tenant
     * @param fromDate The start date for the date range
     * @param toDate The end date for the date range
     * @return List of asset names for the specified tenant and date range
     */
    public List<String> fetchAssetsByDateRange(String tenantId, String fromDate, String toDate); 

    /**
     * Fetches card-wise data for a tenant within a given date range and group type.
     * 
     * @param tenantId The ID of the tenant
     * @param fromDate The start date for the date range
     * @param toDate The end date for the date range
     * @param groupType The type of grouping for the data
     * @return Map with group types as keys and lists of JobwiseCardData as values
     */
    public Map<String, List<JobwiseCardData>> fetchAllCardData(String tenantId, String fromDate, String toDate, String groupType); 

    /**
     * Fetches job status based on tenant ID and ticket number.
     * 
     * @param tenantId The ID of the tenant
     * @param ticketNo The ticket number to check status for
     * @return Map containing job status information for the specified tenant and ticket number
     */
    public List<Map<String, Object>> fetchJobStatusByTicketNo(String tenantId, String ticketNo);
    
    void registerNewTenant(String tenantid);
}
