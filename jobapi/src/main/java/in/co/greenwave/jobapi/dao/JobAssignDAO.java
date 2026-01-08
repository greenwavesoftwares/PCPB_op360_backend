package in.co.greenwave.jobapi.dao;

import java.util.List;

import in.co.greenwave.jobapi.model.CalendarEventModel;
import in.co.greenwave.jobapi.model.JobDetails;
import in.co.greenwave.jobapi.model.PerformerAvailability;


public interface JobAssignDAO {

    /**
     * Retrieves a list of existing jobs assigned to a specific user within a tenant.
     *
     * @param user the identifier of the user.
     * @param tenantId the identifier of the tenant.
     * @return a list of CalendarEventModel representing existing jobs.
     */
    List<CalendarEventModel> getExistingJobs(String user, String tenantId);

    /**
     * Fetches detailed information about a specific job.
     *
     * @param tenantId the identifier of the tenant.
     * @param jobId the identifier of the job.
     * @return a JobDetails object containing detailed information about the job.
     */
    JobDetails getJobDetails(String tenantId, String jobId);

    /**
     * Retrieves all group IDs associated with a tenant.
     *
     * @param tenantId the identifier of the tenant.
     * @return a list of group IDs.
     */
    List<String> fetchAllGroupId(String tenantId);

    /**
     * Gets performer availability within a specified time range for a user in a tenant.
     *
     * @param activityStart the start time of the activity .
     * @param activityStop the stop time of the activity .
     * @param userId the identifier of the user.
     * @param tenantId the identifier of the tenant.
     * @return a list of PerformerAvailability objects representing performer availability.
     */
    List<List<PerformerAvailability>> getPerformerAvailability(String activityStart, String activityStop,String activityId,String taskId, String tenantId);

    /**
     * Deletes an assigned job from the system for a specific tenant.
     *
     * @param job the JobDetails object representing the job to be deleted.
     * @param tenantId the identifier of the tenant.
     * @return the number of rows affected.
     */
    int deleteAssignedJob(JobDetails job, String tenantId);

    /**
     * Retrieves asset availability within a specified time range for a tenant.
     *
     * @param activityStart the start time of the activity .
     * @param activityStop the stop time of the activity .
     * @param assetID the identifier of the asset.
     * @param tenantId the identifier of the tenant.
     * @return a list of PerformerAvailability objects representing asset availability.
     */
//    List<PerformerAvailability> getAssetAvailability(String activityStart, String activityStop, String assetID, String tenantId);

    /**
     * Gets the count of jobs assigned by a specific assigner that have not yet started.
     *
     * @param assigner the identifier of the assigner.
     * @param tenantId the identifier of the tenant.
     * @return the count of jobs that have not started.
     */
    int getNotStartedJobsCount(String assigner, String tenantId);

    /**
     * Adds or updates job details in the system for a specific tenant.
     *
     * @param job the JobDetails object containing the job information to be added or updated.
     * @param tenantId the identifier of the tenant.
     * @return true if the operation was successful, false otherwise.
     */
    void addOrUpdateJobDetails(JobDetails job, String tenantId);

	void addOrUpdateListOfJobs(List<JobDetails> jobList, String tenantId);

	List<List<PerformerAvailability>> getAssetAvailability(String activityStart, String activityStop,String activityId,String taskId, String tenantId);

	List<JobDetails> fetchAllAdhocJobs(String tenantId);

	

	List<JobDetails> fetchAllAdhocJobsByAssigner(String tenantId, String assigner);

	
}

