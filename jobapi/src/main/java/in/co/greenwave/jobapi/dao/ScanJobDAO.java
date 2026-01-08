package in.co.greenwave.jobapi.dao;

import java.util.Date;
import java.util.List;

import in.co.greenwave.jobapi.model.AssetModel;
import in.co.greenwave.jobapi.model.AutoJob;

public interface ScanJobDAO {

    /**
     * Retrieves the logbooks associated with a specific asset within a tenant.
     *
     * @param assetId the identifier of the asset.
     * @param tenantId the identifier of the tenant.
     * @return an AssetModel object containing the logbook details of the asset.
     */
    AssetModel getLogbooksofAsset(String assetId, String tenantId);

    /**
     * Fetches pending job information for a specific asset within a tenant.
     *
     * @param assetId the identifier of the asset.
     * @param tenantId the identifier of the tenant.
     * @return an AutoJob object containing information about the pending job.
     */
    AutoJob getPendingJobInfoforAssetId(String assetId, String tenantId);

    /**
     * Fetches pending job information based on logbook details such as form name, form ID, version, and user.
     *
     * @param formName the name of the form associated with the job.
     * @param formId the identifier of the form.
     * @param version the version of the form.
     * @param user the identifier of the user.
     * @param tenantId the identifier of the tenant.
     * @return an AutoJob object containing information about the pending job.
     */
    AutoJob getPendingJobInfoforLogbook(String formName, int formId, int version, String user, String tenantId);

    /**
     * Initiates a new auto job for a specific tenant.
     *
     * @param job an AutoJob object containing the details of the job to be initiated.
     * @param tenantId the identifier of the tenant.
     * @return true if the job was successfully initiated, false otherwise.
     */
    boolean initialteNewAutoJob(AutoJob job, String tenantId);

    /**
     * Updates the end time of a specific job and activity for an asset within a tenant.
     *
     * @param jobId the identifier of the job.
     * @param activityId the identifier of the activity.
     * @param assetId the identifier of the asset.
     * @param tenantId the identifier of the tenant.
     * @return true if the end time was successfully updated, false otherwise.
     */
    boolean updateJobEndTime(String jobId, String activityId, String assetId, String tenantId,Date time);

    /**
     * Retrieves a list of self-assigned jobs that are pending for approval for a specific user within a tenant.
     *
     * @param userId the identifier of the user.
     * @param tenantId the identifier of the tenant.
     * @return a list of AutoJob objects representing self-assigned jobs pending for approval.
     */
    List<AutoJob> getPendingforApprovalSelfAssignedJobs(String userId, String tenantId);

    /**
     * Updates the status of a self-assigned job for a specific activity and asset within a tenant.
     *
     * @param jobId the identifier of the job.
     * @param activityId the identifier of the activity.
     * @param assetId the identifier of the asset.
     * @param status the new status of the job (e.g., approved, rejected, completed).
     * @param remarks remarks or comments associated with the status update.
     * @param tenantId the identifier of the tenant.
     * @return true if the status was successfully updated, false otherwise.
     */
    boolean updateStatusForSelfAssignedJob(String jobId, String activityId, String assetId, String status, String remarks, String tenantId,Date reviewerStart);
    
    List<AutoJob> getAllSelfJobs(String tenantId);
}

