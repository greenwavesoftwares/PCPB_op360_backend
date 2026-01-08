package in.co.greenwave.taskapi.dao;

import java.util.Date;
import java.util.List;

import in.co.greenwave.taskapi.model.ActivityDetails;
import in.co.greenwave.taskapi.model.JobDetails;
import in.co.greenwave.taskapi.model.PerformerAvailability;
import in.co.greenwave.taskapi.model.TaskDetail;


public interface JobAssignDAO {

	/**
	 * Retrieves a list of published tasks along with their associated activities.
	 *
	 * @return List of TaskDetail containing information about published tasks and their activities.
	 */
	List<TaskDetail> getPublishedTasks(String tenantId);
	/**
	 * Adds or updates job details in the database.
	 *
	 * @param job JobDetails object containing information about the job.
	 * @return true if the operation is successful, false otherwise.
	 */
	boolean addOrUpdateJobDetails(JobDetails job, String tenantId);
	
	
//	public Object microserviceTesting();

	/**this method is used to fetch the details of the published task*/ 
//	public List<TaskDetail> getPublishedTasks();
//	public ScheduleModel getExistingJobs(String user);
//	public void saveAssignedJobs(List<JobDetails> jobs);
//	public JobDetails getJobByStartTime(Date starttime, Date EndTime, String taskName);
//	public List<String> fetchAllGroupId();
//	public List<PerformerAvailability> getPerformerAvailability(String activityStart,String activityStop,String userid);
//	public void updateAssignedJob(JobDetails jobs);
//	public Boolean traceJobDetailsOnUpdate(JobDetails job);
//	public void deleteAssignedJob(JobDetails job);
//	public List<PerformerAvailability> getAssetAvailability(String activityStart, String activityStop, String assetID);
//	public List<JobDetails> getAllAdhocJobs(String user);
//	public List<ActivityDetails> getAllActivitesOfAdhocJobByTask(String taskId);
}
