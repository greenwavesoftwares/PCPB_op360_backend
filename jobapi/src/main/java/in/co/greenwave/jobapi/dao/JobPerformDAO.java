package in.co.greenwave.jobapi.dao;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import in.co.greenwave.jobapi.model.ActivityDetails;
import in.co.greenwave.jobapi.model.JobDetails;


public interface JobPerformDAO {

    /**
     * Fetches the last completed order for a specific job within a tenant.
     *
     * @param jobId the identifier of the job.
     * @param tenantId the identifier of the tenant.
     * @return the order of the last completed activity in the job.
     */
    int fetchLastCompletedOrder(String jobId, String tenantId);

    /**
     * Fetches the count of remaining activities for a job based on the last completed order.
     *
     * @param jobId the identifier of the job.
     * @param lastCompletedOrder the order of the last completed activity.
     * @param tenantId the identifier of the tenant.
     * @return the number of remaining activities.
     */
    int fetchRemainingActivties(String jobId, int lastCompletedOrder, String tenantId);

    /**
     * Retrieves job details for a specific job ID within a tenant.
     *
     * @param jobId the identifier of the job.
     * @param tenantId the identifier of the tenant.
     * @return a JobDetails object containing the job's details.
     */
    JobDetails getJobDetailsByJobId(String jobId, String tenantId);

    /**
     * Updates the filename associated with a specific activity within a tenant.
     *
     * @param act the ActivityDetails object representing the activity.
     * @param filename the new filename to be updated.
     * @param tenantId the identifier of the tenant.
     */
    void updateFileName(ActivityDetails act, String filename, String tenantId);

    /**
     * Retrieves job data for jobs based on the current performer within a tenant.
     *
     * @param currentPerformer the identifier of the current performer.
     * @param tenantId the identifier of the tenant.
     * @return a list of JobDetails objects representing the job data.
     */
    List<JobDetails> getJobDataBasedOnPerformer(String currentPerformer, String tenantId);

    /**
     * Fetches the count of jobs completed by a specific reviewer within a tenant.
     *
     * @param reviewer the identifier of the reviewer.
     * @param tenantId the identifier of the tenant.
     * @return the count of completed jobs.
     */
    int fetchCompletedJobCount(String reviewer, String tenantId);

    /**
     * Fetches the details of the last ten jobs assigned to a list of performers within a tenant.
     *
     * @param performerList a comma-separated list of performer identifiers.
     * @param tenantId the identifier of the tenant.
     * @return a list of JobDetails objects for the last ten jobs.
     */
    List<JobDetails> fetchLastTenJobs(String performerList, String tenantId);

    /**
     * Fetches performance statistics for a list of performers within a tenant.
     *
     * @param performers a comma-separated list of performer identifiers.
     * @param tenantId the identifier of the tenant.
     * @return a map where each performer maps to another map containing activity statistics.
     */
    Map<String, Map<String, Integer>> fetchPerformerStats(String performers, String tenantId);

    /**
     * Updates the status of activities based on a map of activity details.
     *
     * @param activity a map containing activity details, such as status updates.
     */
    void updateActivityStatus(ActivityDetails activity,String tenantId);

    /**
     * Updates enforced activity details for a tenant.
     *
     * @param tenantId the identifier of the tenant.
     */
    void updateEnforcedActivitiesDetails(String tenantId);

    /**
     * Retrieves job details for a specific job ID within a tenant.
     *
     * @param jobId the identifier of the job.
     * @param tenantId the identifier of the tenant.
     * @return a JobDetails object containing the job's details.
     */
    JobDetails getJobDetailsBasedOnJobId(String jobId, String tenantId);

    /**
     * Updates the status of a job based on its job ID within a tenant.
     *
     * @param jobId the identifier of the job.
     * @param tenantId the identifier of the tenant.
     */
    void updateJobStatus(JobDetails job, String tenantId);
    /**
     * Updates enforced activity details for every tenant in the database.

     */
	void updateEnforcedActivitiesForEveryTenant();
	
	/**
	 * updates the bit through which sequence is bypassed
	 * @param jobid the identifier of the job.
     * @param tenantid the identifier of the tenant.
     * @param activityid the identifier of the tenant.
	 * */
	void enableActivitiesBypassingSequence(String jobid, String activityid, String tenantid);

	List<ActivityDetails> getHistoricalActivitiesBasedOnPerformer(String performergrouplist,
			String tenantid, Date fromDate, Date toDate);


	Object sendMail(String tenantId, String token, String jobId);

	
}

