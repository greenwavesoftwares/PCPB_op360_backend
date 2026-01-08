package in.co.greenwave.jobapi.model;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

//import org.primefaces.model.diagram.DefaultDiagramModel;
//import org.primefaces.model.timeline.TimelineModel;
//@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(alphabetic = true)
public class JobDetails {

	// Details of the associated task
	private TaskDetail task;

	// Name of the job
	private String jobName;

	// Unique identifier for the job
	private String jobID;

	// Name of the person or entity assigning the job
	private String assigner;

	// Name of the person responsible for approving the job
	private String approver;

	// Scheduled start time for the job
	private Date scheduledJobStartTime;

	// Scheduled end time for the job
	private Date scheduledJobEndTime;

	// Actual start time for the job
	private Date actualJobStartTime;

	// Actual end time for the job
	private Date actualJobEndTime;

	// Time when the reviewer was notified about the job
	private Date reviewerIntimationTime;

	// Priority level of the job (e.g., high, medium, low)
	private String priority;

	// Instrument or tool associated with the job
	private String instrument;

	// Identifier for the group associated with the job
	private String groupId;

	// Additional remarks or comments regarding the job
	private String remarks;

	// Current status of the job (e.g., in progress, completed, canceled)
	private String jobStatus;

	// Color representation of the job's priority (for UI purposes)
	private String priorityColor;

	// Time duration the job has been awaiting action
	private String awatingTime;

	// Remaining time for the job to be completed
	private int jobRemainingTime;

	// Total count of jobs associated with this context
	private int jobCount;

	// Progress percentage of the job (0-100)
	private Integer progress;

	// Number of weekdays relevant to the job's schedule
	private Integer weekdays;

	// Flag indicating if the job is an ad-hoc task (true for ad-hoc)
	private boolean adhoc = false;

	// Count of completed jobs
	private int completedJob;

	// Count of jobs that are currently pending
	private int pendingJob;

	// Count of jobs that have been rejected
	private int rejectedjob;

	// Current working information related to the job
	private String currentActivityName; // Name of the current activity being worked on
	private String currentPerformer; // Name of the performer currently assigned to the activity
	private int activityWaitingTime; // Time the current activity has been waiting

	// Timeline data for job tracking
	private LocalDateTime start; // Start time of the job's timeline
	private LocalDateTime end;   // End time of the job's timeline

	// Name of the attached file related to the job
	private String fileName;

	// Lists for performers and activities, potentially for UI display
	private String performerLists = ""; // Comma-separated list of performers
	private String activitylists = "";   // Comma-separated list of activities

	@Override
	public String toString() {
		return "JobDetails [task=" + task + ", jobID=" + jobID + ", instrument=" + instrument + ", assigner=" + assigner + 
				", approver=" + approver + ", scheduledJobStartTime="
				+ scheduledJobStartTime + ", scheduledJobEndTime=" + scheduledJobEndTime + ", actualJobStartTime="
				+ actualJobStartTime + ", actualJobEndTime=" + actualJobEndTime + ", priority=" + priority + "]";
	}

	public void calculateawatingTime() {
		//System.out.println("JobDetails.calculateawatingTime()");
		Calendar now = Calendar.getInstance();
		Calendar intimationTime = Calendar.getInstance();
		intimationTime.setTime(this.reviewerIntimationTime);
		
		long seconds = (now.getTimeInMillis()- intimationTime.getTimeInMillis()) / 1000;
		long minutes = seconds / 60;
		long hours = minutes / 60;
		long days = hours / 24;
		this.awatingTime = days + " Day(s) " + hours % 24 + " Hr " + minutes % 60 + " Minute " + seconds % 60 +" Seconds"; 
	}
	public void calculateJobWatingTime() {
		this.activityWaitingTime = this.activityWaitingTime+1;
	}
	public JobDetails() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public JobDetails(TaskDetail task, String jobID,String jobName ,String approver, Date scheduledJobStartTime,
			Date scheduledJobEndTime, Date actualJobStartTime, Date actualJobEndTime,String priority,String instrument
			,String remarks,String assigner,String status) {
		super();
		this.task = task;
		this.jobID = jobID;
		this.jobName = jobName;
		this.approver = approver;
		this.scheduledJobStartTime = scheduledJobStartTime;
		this.scheduledJobEndTime = scheduledJobEndTime;
		this.actualJobStartTime = actualJobStartTime;
		this.actualJobEndTime = actualJobEndTime;
		this.priority = priority;
		this.instrument = instrument;
		this.remarks = remarks;
		this.assigner = assigner;
		this.jobStatus = status;
	}

	public JobDetails(TaskDetail task, String jobID,String jobName ,String approver, Date scheduledJobStartTime,
			Date scheduledJobEndTime, Date actualJobStartTime, Date actualJobEndTime,String priority,String instrument
			,String remarks) {
		super();
		this.task = task;
		this.jobID = jobID;
		this.jobName = jobName;
		this.approver = approver;
		this.scheduledJobStartTime = scheduledJobStartTime;
		this.scheduledJobEndTime = scheduledJobEndTime;
		this.actualJobStartTime = actualJobStartTime;
		this.actualJobEndTime = actualJobEndTime;
		this.priority = priority;
		this.instrument = instrument;
		this.remarks = remarks;
	}

	public String getJobID() {
		return jobID;
	}
	public void setJobID(String jobID) {
		this.jobID = jobID;
	}
	public String getApprover() {
		return approver;
	}
	public void setApprover(String approver) {
		this.approver = approver;
	}
	public Date getScheduledJobStartTime() {
		return scheduledJobStartTime;
	}
	public void setScheduledJobStartTime(Date scheduledJobStartTime) {
		this.scheduledJobStartTime = scheduledJobStartTime;
	}
	public Date getScheduledJobEndTime() {
		return scheduledJobEndTime;
	}
	public void setScheduledJobEndTime(Date scheduledJobEndTime) {
		this.scheduledJobEndTime = scheduledJobEndTime;
	}
	public Date getActualJobStartTime() {
		return actualJobStartTime;
	}
	public void setActualJobStartTime(Date actualJobStartTime) {
		this.actualJobStartTime = actualJobStartTime;
	}
	public Date getActualJobEndTime() {
		return actualJobEndTime;
	}
	public void setActualJobEndTime(Date actualJobEndTime) {
		this.actualJobEndTime = actualJobEndTime;
	}


	public String getPriority() {
		return priority;
	}


	public void setPriority(String priority) {
		this.priority = priority;
	}


	public TaskDetail getTask() {
		return task;
	}


	public void setTask(TaskDetail task) {
		this.task = task;
	}


	public String getInstrument() {
		return instrument;
	}


	public void setInstrument(String instrument) {
		this.instrument = instrument;
	}


	public String getRemarks() {
		return remarks;
	}


	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}


	public String getJobStatus() {
		return jobStatus;
	}


	public void setJobStatus(String jobStatus) {
		this.jobStatus = jobStatus;
	}


	public int getCompletedJob() {
		return completedJob;
	}

	public int getPendingJob() {
		return pendingJob;
	}

	public int getRejectedjob() {
		return rejectedjob;
	}

	public void setCompletedJob(int completedJob) {
		this.completedJob = completedJob;
	}

	public void setPendingJob(int pendingJob) {
		this.pendingJob = pendingJob;
	}

	public void setRejectedjob(int rejectedjob) {
		this.rejectedjob = rejectedjob;
	}

	public String getPriorityColor() {
		return priorityColor;
	}


	public void setPriorityColor(String priorityColor) {
		this.priorityColor = priorityColor;
	}


//	public TimelineModel<String, ?> getTimeline() {
//		return timeline;
//	}
//
//
//	public void setTimeline(TimelineModel<String, ?> timeline) {
//		this.timeline = timeline;
//	}


	public LocalDateTime getStart() {
		return start;
	}


	public void setStart(LocalDateTime start) {
		this.start = start;
	}


	public LocalDateTime getEnd() {
		return end;
	}


	public void setEnd(LocalDateTime end) {
		this.end = end;
	}


	public Date getReviewerIntimationTime() {
		return reviewerIntimationTime;
	}


	public void setReviewerIntimationTime(Date reviewerIntimationTime) {
		this.reviewerIntimationTime = reviewerIntimationTime;
	}

	public String getAwatingTime() {
		return awatingTime;
	}

	public void setAwatingTime(String awatingTime) {
		this.awatingTime = awatingTime;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getAssigner() {
		return assigner;
	}

	public void setAssigner(String assigner) {
		this.assigner = assigner;
	}

	public int getJobCount() {
		return jobCount;
	}

	public void setJobCount(int jobCount) {
		this.jobCount = jobCount;
	}

	public int getJobRemainingTime() {
		return jobRemainingTime;
	}

	public void setJobRemainingTime(int jobRemainingTime) {
		this.jobRemainingTime = jobRemainingTime;
	}



	public Integer getProgress() {
		return progress;
	}

	public void setProgress(Integer progress) {
		this.progress = progress;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getCurrentActivityName() {
		return currentActivityName;
	}

	public void setCurrentActivityName(String currentActivityName) {
		this.currentActivityName = currentActivityName;
	}

	public String getCurrentPerformer() {
		return currentPerformer;
	}

	public void setCurrentPerformer(String currentPerformer) {
		this.currentPerformer = currentPerformer;
	}

	public int getActivityWaitingTime() {
		return activityWaitingTime;
	}

	public void setActivityWaitingTime(int activityWaitingTime) {
		this.activityWaitingTime = activityWaitingTime;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Integer getWeekdays() {
		return weekdays;
	}

	public void setWeekdays(Integer weekdays) {
		this.weekdays = weekdays;
	}

	public String getPerformerLists() {
		return performerLists;
	}

	public void setPerformerLists(String performerLists) {
		this.performerLists = performerLists;
	}

	public String getActivitylists() {
		return activitylists;
	}

	public void setActivitylists(String activitylists) {
		this.activitylists = activitylists;
	}

	public boolean isAdhoc() {
		return adhoc;
	}

	public void setAdhoc(boolean adhoc) {
		this.adhoc = adhoc;
	}
}

