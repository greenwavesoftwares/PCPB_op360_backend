package in.co.greenwave.dashboardapi.model;

import java.util.ArrayList;
import java.util.List;

public class UserwiseJobDetails {
	
	private String jobId;
	private String jobName;
	private String groupId;
	private String jobScheduleStartTime;
	private String jobScheduleStopTime;
	private String jobActualStartTime;
	private String jobActualStopTime;
	private String jobElapsedTime;
	private String progress;
	private String priority;
	private String tag;
	
	private String taskTemplate;
	private String activityId;
	private String activityName;
	private String logBook;
	private String performer; //User
	private String approver;
	private String activityStartTime;
	private String activityStoptime;
	private String scheduleActivityStartTime;
	private String scheduleActivityStopTime;
	private String currentActivity;
	private String currentPerformer;
	private String performers;
	
	private String sequeance;
	private String elapsedtime;
	private String standardDuration;
	private String assets;
	
	private String jobstatus;
	private List<ActivityDetails> actvityList;
	private TaskDetail task;
	
	 
	private List<String> initial=new ArrayList<String>();
	
	//private UserInitial userInitial;
	public UserwiseJobDetails() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	

	public UserwiseJobDetails(List<ActivityDetails> actvityList) {
		super();
		this.actvityList = actvityList;
	}

	
	
	public String getJobId() {
		return jobId;
	}
	public void setJobId(String jobId) {
		this.jobId = jobId;
	}
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	public String getTaskTemplate() {
		return taskTemplate;
	}
	public void setTaskTemplate(String taskTemplate) {
		this.taskTemplate = taskTemplate;
	}
	public String getActivityId() {
		return activityId;
	}
	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}
	public String getActivityName() {
		return activityName;
	}
	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}
	public String getLogBook() {
		return logBook;
	}
	public void setLogBook(String logBook) {
		this.logBook = logBook;
	}
	public String getApprover() {
		return approver;
	}
	public void setApprover(String approver) {
		this.approver = approver;
	}
	public String getActivityStartTime() {
		return activityStartTime;
	}
	public void setActivityStartTime(String activityStartTime) {
		this.activityStartTime = activityStartTime;
	}

	public String getJobScheduleStartTime() {
		return jobScheduleStartTime;
	}


	public String getJobScheduleStopTime() {
		return jobScheduleStopTime;
	}


	public String getJobActualStartTime() {
		return jobActualStartTime;
	}


	public String getJobActualStopTime() {
		return jobActualStopTime;
	}


	public String getScheduleActivityStartTime() {
		return scheduleActivityStartTime;
	}


	public String getScheduleActivityStopTime() {
		return scheduleActivityStopTime;
	}


	public void setJobScheduleStartTime(String jobScheduleStartTime) {
		this.jobScheduleStartTime = jobScheduleStartTime;
	}


	public void setJobScheduleStopTime(String jobScheduleStopTime) {
		this.jobScheduleStopTime = jobScheduleStopTime;
	}


	public void setJobActualStartTime(String jobActualStartTime) {
		this.jobActualStartTime = jobActualStartTime;
	}


	public void setJobActualStopTime(String jobActualStopTime) {
		this.jobActualStopTime = jobActualStopTime;
	}


	public void setScheduleActivityStartTime(String scheduleActivityStartTime) {
		this.scheduleActivityStartTime = scheduleActivityStartTime;
	}


	public void setScheduleActivityStopTime(String scheduleActivityStopTime) {
		this.scheduleActivityStopTime = scheduleActivityStopTime;
	}


	public String getSequeance() {
		return sequeance;
	}
	public void setSequeance(String sequeance) {
		this.sequeance = sequeance;
	}
	public String getElapsedtime() {
		return elapsedtime;
	}
	public void setElapsedtime(String elapsedtime) {
		this.elapsedtime = elapsedtime;
	}
	public String getStandardDuration() {
		return standardDuration;
	}
	public void setStandardDuration(String standardDuration) {
		this.standardDuration = standardDuration;
	}
	public String getAssets() {
		return assets;
	}
	public void setAssets(String assets) {
		this.assets = assets;
	}
	public String getPerformer() {
		return performer;
	}
	public void setPerformer(String performer) {
		this.performer = performer;
	}
	public String getActivityStoptime() {
		return activityStoptime;
	}
	public void setActivityStoptime(String activityStoptime) {
		this.activityStoptime = activityStoptime;
	}
	
	public String getProgress() {
		return progress;
	}
	public String getPriority() {
		return priority;
	}
	public String getTag() {
		return tag;
	}
	public String getCurrentActivity() {
		return currentActivity;
	}
	public String getCurrentPerformer() {
		return currentPerformer;
	}
	public String getPerformers() {
		return performers;
	}
	
	public void setProgress(String progress) {
		this.progress = progress;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public void setCurrentActivity(String currentActivity) {
		this.currentActivity = currentActivity;
	}
	public void setCurrentPerformer(String currentPerformer) {
		this.currentPerformer = currentPerformer;
	}
	public void setPerformers(String performers) {
		this.performers = performers;
	}


	public String getGroupId() {
		return groupId;
	}


	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}


	public String getJobElapsedTime() {
		return jobElapsedTime;
	}


	public void setJobElapsedTime(String jobElapsedTime) {
		this.jobElapsedTime = jobElapsedTime;
	}



	public List<ActivityDetails> getActvityList() {
		return actvityList;
	}



	public void setActvityList(List<ActivityDetails> actvityList) {
		this.actvityList = actvityList;
	}



	public TaskDetail getTask() {
		return task;
	}



	public void setTask(TaskDetail task) {
		this.task = task;
	}








	public List<String> getInitial() {
		return initial;
	}



	public void setInitial(List<String> initial) {
		this.initial = initial;
	}



	


	public String getJobstatus() {
		return jobstatus;
	}



	public void setJobstatus(String jobstatus) {
		this.jobstatus = jobstatus;
	}



	@Override
	public String toString() {
		return "UserwiseJobDetails [jobId=" + jobId + ", jobName=" + jobName + ", groupId=" + groupId
				+ ", jobScheduleStartTime=" + jobScheduleStartTime + ", jobScheduleStopTime=" + jobScheduleStopTime
				+ ", jobActualStartTime=" + jobActualStartTime + ", jobActualStopTime=" + jobActualStopTime
				+ ", jobElapsedTime=" + jobElapsedTime + ", progress=" + progress + ", priority=" + priority + ", tag="
				+ tag + ", taskTemplate=" + taskTemplate + ", activityId=" + activityId + ", activityName="
				+ activityName + ", logBook=" + logBook + ", performer=" + performer + ", approver=" + approver
				+ ", activityStartTime=" + activityStartTime + ", activityStoptime=" + activityStoptime
				+ ", scheduleActivityStartTime=" + scheduleActivityStartTime + ", scheduleActivityStopTime="
				+ scheduleActivityStopTime + ", currentActivity=" + currentActivity + ", currentPerformer="
				+ currentPerformer + ", performers=" + performers + ", sequeance=" + sequeance + ", elapsedtime="
				+ elapsedtime + ", standardDuration=" + standardDuration + ", assets=" + assets + ", jobstatus="
				+ jobstatus + ", actvityList=" + actvityList + ", task=" + task + ", initial=" + initial + "]";
	}


}

