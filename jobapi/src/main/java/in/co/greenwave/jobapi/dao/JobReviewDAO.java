package in.co.greenwave.jobapi.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import in.co.greenwave.jobapi.model.ApproverCardData;
import in.co.greenwave.jobapi.model.JobDetails;

public interface JobReviewDAO {

    /**
     * Retrieves a list of jobs pending review for a specific user within a tenant.
     *
     * @param user the identifier of the user.
     * @param tenantId the identifier of the tenant.
     * @return a list of JobDetails objects representing jobs pending for review.
     */
    List<JobDetails> pendingForReviewJobs(String user, String tenantId);

    /**
     * Updates the approval details for a specific job, including remarks and action taken.
     *
     * @param jobId the identifier of the job.
     * @param remarks the remarks added by the approver.
     * @param action the action taken (e.g., approved, rejected).
     * @param tenantId the identifier of the tenant.
     */
    void updateApprovalDetails(String jobId, String remarks, String action, String tenantId);

    /**
     * Retrieves the approval history of a specific approver within a tenant.
     *
     * @param approver the identifier of the approver.
     * @param tenantId the identifier of the tenant.
     * @return an ApproverCardData object containing the approver's approval history.
     */
    ApproverCardData getApprovedHistory(String approver, String tenantId);

    /**
     * Fetches footer details for generating a PDF for a specific job and activity within a tenant.
     *
     * @param jobId the identifier of the job.
     * @param activityId the identifier of the activity.
     * @param tenantId the identifier of the tenant.
     * @return a map where keys and values represent footer details for the PDF.
     */
    Map<String, String> getPdfFooterDetails(String jobId, String activityId, String tenantId);

    /**
     * Retrieves a list of activities pending review for a specific user within a tenant.
     *
     * @param user the identifier of the user.
     * @param tenantId the identifier of the tenant.
     * @return a list of JobDetails objects representing activities pending for review.
     */
    List<JobDetails> pendingForReviewActivities(String user, String tenantId);

    /**
     * Retrieves all jobs for a specific tenant.
     *
     * @param tenantId the identifier of the tenant.
     * @return a list of JobDetails objects representing all jobs in the tenant.
     */
    List<JobDetails> getAllJobs(String tenantId);

    /**
     * Retrieves all jobs for a specific tenant within a specified date range for display in a card view in Dashboard.
     *
     * @param tenantId the identifier of the tenant.
     * @param fromDate the start date of the range. 
     * @param toDate the end date of the range .
     * @return a list of JobDetails objects representing jobs in the specified date range.
     */
    List<JobDetails> getAllJobsForCard(String tenantId, String fromDate, String toDate);
    List<Map<String, Object>> getJobDetailsByTicketId(String tenantId, String ticketId);

	void registerNewTenant(String tenantid);

	void updateSequentialStatus(String tenantid, String jobid, String activityid, int sequence);

	JobDetails fetchJobDetails(String tenantid, String jobid);

		

		List<JobDetails> getAllJobsByUserAndDate(String tenantId, String user, String date);

		List<JobDetails> getAllJobsByUser(String tenantId, String user, int eventMonth, int eventYear);
}

