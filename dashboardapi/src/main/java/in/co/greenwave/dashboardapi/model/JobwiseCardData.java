package in.co.greenwave.dashboardapi.model;

import java.util.List;

public class JobwiseCardData {

	private String jobId;
	private String jobName;
	private String priority;
	private int intPriority;
	private int plannedProgress;
	private int progress;
	private String asset = "NA";
	private String workStatus = "On-Time";
	private String statusMsg = "Early by 3 Minute";
	private String currentPerformer;
	private String currentActivity;
	private List<String> performers;
	private String groupId;
	private String taskName;
	private String instrument;
	private String taskId;
	
	
	public JobwiseCardData(String jobId, String jobName, String priority,String groupId,String taskName,String instrument ,int progress, List<String> performers) {
		super();
		this.jobId = jobId;
		this.jobName = jobName;
		this.priority = priority;
		this.progress = progress;
		this.performers = performers;
		this.groupId = groupId;
		this.taskName = taskName;
		this.instrument = instrument;
		if(this.priority.equalsIgnoreCase("Critical")) 
			this.intPriority = 3;
		else if(this.priority.equalsIgnoreCase("Normal")) 
			this.intPriority = 2;
		else
			this.intPriority = 1;
	}
	
	
	
	public JobwiseCardData() {
		super();
		// TODO Auto-generated constructor stub
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((jobId == null) ? 0 : jobId.hashCode());
		return result;
	}



	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		JobwiseCardData other = (JobwiseCardData) obj;
		if (jobId == null) {
			if (other.jobId != null)
				return false;
		} else if (!jobId.equals(other.jobId))
			return false;
		return true;
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
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	public int getProgress() {
		return progress;
	}
	public void setProgress(int progress) {
		this.progress = progress;
	}
	public List<String> getPerformers() {
		return performers;
	}
	public void setPerformers(List<String> performers) {
		this.performers = performers;
	}
	
	public String getAsset() {
		return asset;
	}
	public void setAsset(String asset) {
		this.asset = asset;
	}
	public String getWorkStatus() {
		return workStatus;
	}
	public void setWorkStatus(String workStatus) {
		this.workStatus = workStatus;
	}
	public String getStatusMsg() {
		return statusMsg;
	}
	public void setStatusMsg(String statusMsg) {
		this.statusMsg = statusMsg;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public String getInstrument() {
		return instrument;
	}
	public void setInstrument(String instrument) {
		this.instrument = instrument;
	}
	public String getCurrentPerformer() {
		return currentPerformer;
	}
	public void setCurrentPerformer(String currentPerformer) {
		this.currentPerformer = currentPerformer;
	}
	public String getCurrentActivity() {
		return currentActivity;
	}
	public void setCurrentActivity(String currentActivity) {
		this.currentActivity = currentActivity;
	}



	public int getIntPriority() {
		return intPriority;
	}



	public void setIntPriority(int intPriority) {
		this.intPriority = intPriority;
	}



	public int getPlannedProgress() {
		return plannedProgress;
	}



	public void setPlannedProgress(int plannedProgress) {
		this.plannedProgress = plannedProgress;
	}



	public String getTaskId() {
		return taskId;
	}



	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	
	
}
