package com.random.number.models;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder(alphabetic = true)
public class ActivityDetails implements Serializable{

	/**
	 * 
	 */
	// Serial version UID for serialization compatibility
	
	// Unique identifier for the task
	private String taskId;

	// Name of the task
	private String taskName;

	// Identifier for the job associated with this task
	private String jobId;

	// Identifier for the activity within the task
	private String activityId;

	// Name of the activity
	private String activityName;

	// Sequence number to maintain the order of activities
	private int sequence = 1;

	// Logbook entry related to the task or activity
	private String logbook;

	// Duration of the activity in some time unit (e.g., minutes)
	private int duration;

	// X position for UI representation
	private String xPos;

	// Y position for UI representation
	private String yPos;

	// Name of the performer responsible for the activity
	private String performer;

	// Name of the approver for the activity
	private String approver;

	// Scheduled start time of the activity
	private Date scheduledActivityStartTime;

	// Scheduled end time of the activity
	private Date scheduledActivityEndTime;

	// Actual start time of the activity
	private Date actualActivityStartTime;

	// Actual end time of the activity
	private Date actualActivityEndTime;

	// Flag indicating if the task does not belong to the performer
	private boolean notBelongToPerformer;

	// Flag indicating if the task does not belong to the approver
	private boolean notBelongToApprover;

	// Status of the activity (e.g., pending, completed)
	private String actvityStatus;

	// Flag to disable activity button upon completion
	private boolean isActvityBtnDisableOnCompletion;

	// Flag to disable activity button for activity order
	private boolean isActvityBtnDisbleForActvtOrder;

	// Abbreviation for the activity
	private String actAbrv;

	// Flag indicating if the activity is currently available
	private boolean available = true;

	// List of reasons for performer unavailability
	private List<PerformerAvailability> performerAvlReasons = new LinkedList<PerformerAvailability>();

	// Flag indicating if the asset is available for the activity
	private boolean assetAvailable = true;

	// List of reasons for asset unavailability
	private List<PerformerAvailability> assetAvlReasons = new LinkedList<PerformerAvailability>();

	// String representation of the actual activity start time
	private String actualActvtyStrt;

	// String representation of the actual activity end time
	private String actualActvtEnd;

	// Start time for the reviewer's activity
	private Date reviewerActivityStartTime;

	// End time for the reviewer's activity
	private Date reviewerActivityStopTime;

	// Additional remarks or comments regarding the activity
	private String remarks;

	// Identifier for the asset related to the task
	private String assetId;

	// Name of the asset related to the task
	private String assetName;

	// List of asset IDs associated with the activity
	private List<String> assetIDList;

	// List of asset names associated with the activity
	private List<String> assetNameList;

	// Flag indicating if the activity is for a group or department
	private boolean groupOrDept;

	// Name of the group or department associated with the activity
	private String groupOrDeptName;

	// Type of performer (e.g., individual, group)
	private String performerType;

	// List of performers categorized by group or department
	private List<User> getGroupOrDeptWisePerformer;

	// Count of completed activities
	private int completedActivity;

	// Count of pending activities
	private int pendingActivity;

	// Count of rejected activities
	private int rejectedActivity;

	// Date associated with the activity
	private String date;

	// Count of activities related to this task
	private int actvtCount;

	// File associated with the activity
	private String actFile;

	// Binary data for an activity file, potentially for uploads
	private byte[] activityFileData; // Added by Sreepriya
	//Integer to calculate scheduled start of activities with sequence greater than 1
	private boolean enforceStart;

	// Number of weekdays associated with the activity, if applicable
	private Integer weekdays; // Added by Ashok
	
//	DAOFactory SQLServerFactory = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
//	JobAssignDAO dao = SQLServerFactory.getJobAssignService();
//	AssetConfigurationDAO assetDao = SQLServerFactory.getAssetConfigurationService();
//	public DateFormat dfDtTm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	
	public byte[] getActivityFileData() {
	return activityFileData;
}

public void setActivityFileData(byte[] activityFileData) {
	this.activityFileData = activityFileData;
}

	private int buffer;
	private String delayDueToBuffer;
	private boolean enforce;
	private boolean booleanForActivityStatus = false;
	private boolean disable;
	private boolean disableForAction;
	
	private List<String> selectedAssetList;
	private List<String> selectedAssetIdsList;
	private boolean enableStart;

	public boolean isEnableStart() {
		return enableStart;
	}

	public void setEnableStart(boolean enableStart) {
		this.enableStart = enableStart;
	}

	public String redirectLogbook() {
		System.out.println("Performer.redirectLogbook()"+this.logbook);
		return "LogbookForm.xhtml?selectedLogBook="+this.logbook+"faces-redirect=true&includeViewParams=true";
	}

	public ActivityDetails(String activityName, String logbook, int duration) {
		super();
		this.activityName = activityName;
		this.logbook = logbook;
		this.duration = duration;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((activityId == null) ? 0 : activityId.hashCode());
		result = prime * result + ((taskId == null) ? 0 : taskId.hashCode());
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
		ActivityDetails other = (ActivityDetails) obj;
		if (activityId == null) {
			if (other.activityId != null)
				return false;
		} else if (!activityId.equals(other.activityId))
			return false;
		if (taskId == null) {
			if (other.taskId != null)
				return false;
		} else if (!taskId.equals(other.taskId))
			return false;
		return true;
	}
	
	public ActivityDetails(String taskId, String taskName, String activityId, String activityName, String logbook,
			String performer, String approver, Date scheduledActivityStartTime, Date scheduledActivityEndTime,
			String assetName,int sequence) {
		super();
		this.taskId = taskId;
		this.taskName = taskName;
		this.activityId = activityId;
		this.activityName = activityName;
		this.logbook = logbook;
		this.performer = performer;
		this.approver = approver;
		this.scheduledActivityStartTime = scheduledActivityStartTime;
		this.scheduledActivityEndTime = scheduledActivityEndTime;
		this.assetName = assetName;
		this.sequence = sequence;
	}
	// constructor for duration.
	public ActivityDetails(String taskId, String taskName, String activityId, String activityName, String logbook,
			String performer, String approver, Date scheduledActivityStartTime, Date scheduledActivityEndTime,
			String assetName,int sequence,int duration,boolean groupOrDept, String performerType, String groupOrDeptName,String assetId) {
		super();
		this.taskId = taskId;
		this.taskName = taskName;
		this.activityId = activityId;
		this.activityName = activityName;
		this.logbook = logbook;
		this.performer = performer;
		this.approver = approver;
		this.scheduledActivityStartTime = scheduledActivityStartTime;
		this.scheduledActivityEndTime = scheduledActivityEndTime;
		this.assetId = assetId;
		this.assetName = assetName;
		this.sequence = sequence;
		this.duration = duration;
		this.groupOrDept = groupOrDept;
		this.performerType = performerType;
		this.groupOrDeptName = groupOrDeptName;
	}
	//for duration, assetName and assetID
	public ActivityDetails(String taskId, String taskName, String activityId, String activityName, String logbook,
			String performer, String approver, Date scheduledActivityStartTime, Date scheduledActivityEndTime, String assetId,
			String assetName,int sequence,int duration, boolean groupOrDept, String performerType, String groupOrDeptName) {
		super();
		this.taskId = taskId;
		this.taskName = taskName;
		this.activityId = activityId;
		this.activityName = activityName;
		this.logbook = logbook;
		this.performer = performer;
		this.approver = approver;
		this.scheduledActivityStartTime = scheduledActivityStartTime;
		this.scheduledActivityEndTime = scheduledActivityEndTime;
		this.assetId = assetId;
		this.assetName = assetName;
		this.sequence = sequence;
		this.duration = duration;
		this.groupOrDept = groupOrDept;
		this.performerType = performerType;
		this.groupOrDeptName = groupOrDeptName;
	}
	public ActivityDetails(String taskId, String activityId, String activityName,int sequence, String logbook, int duration,String assetName,String performer,String approver) {
		super();
		this.taskId = taskId;
		this.activityId = activityId;
		this.activityName = activityName;
		this.sequence=sequence;
		this.logbook = logbook;
		this.duration = duration;
		this.assetName = assetName;
		this.performer = performer;
		this.approver = approver;
	}
	public ActivityDetails(String taskId, String activityId, String activityName,int sequence, String logbook, int duration,String assetName) {
		super();
		this.taskId = taskId;
		this.activityId = activityId;
		this.activityName = activityName;
		this.sequence=sequence;
		this.logbook = logbook;
		this.duration = duration;
		this.assetName = assetName;
	}
	
	// for assetNameList and assetIDList 
	public ActivityDetails(String taskId, String activityId, String activityName,int sequence, String logbook, int duration,List<String> assetIDList,List<String> assetNameList) {
		super();
		this.taskId = taskId;
		this.activityId = activityId;
		this.activityName = activityName;
		this.sequence=sequence;
		this.logbook = logbook;
		this.duration = duration;
		this.assetIDList = assetIDList;
		this.assetNameList = assetNameList;
	}
	public ActivityDetails(String taskId, String activityId, String activityName,int sequence, String logbook, int duration,String performer,String approver) {
		super();
		this.taskId = taskId;
		this.activityId = activityId;
		this.activityName = activityName;
		this.sequence=sequence;
		this.logbook = logbook;
		this.duration = duration;
		this.performer = performer;
		this.approver = approver;
	}
	
	public ActivityDetails(String taskId, String taskName, String jobId, String activityId, String activityName,
			int sequence, String logbook, int duration, String xPos, String yPos, String performer, String approver,
			Date scheduledActivityStartTime, Date scheduledActivityEndTime, Date actualActivityStartTime,
			Date actualActivityEndTime, boolean notBelongToPerformer, boolean notBelongToApprover, String actvityStatus,
			boolean isActvityBtnDisableOnCompletion, boolean isActvityBtnDisbleForActvtOrder, String actAbrv,
			boolean available, List<PerformerAvailability> performerAvlReasons, boolean assetAvailable,
			List<PerformerAvailability> assetAvlReasons, String actualActvtyStrt, String actualActvtEnd,
			Date reviewerActivityStartTime, Date reviewerActivityStopTime, String remarks, String assetId,
			String assetName, List<String> assetIDList, List<String> assetNameList, boolean groupOrDept,
			String groupOrDeptName, String performerType, List<User> getGroupOrDeptWisePerformer, int completedActivity,
			int pendingActivity, int rejectedActivity, String date, int actvtCount, String actFile,
			byte[] activityFileData, int buffer, String delayDueToBuffer, boolean enforce,
			boolean booleanForActivityStatus, boolean disable, boolean disableForAction, List<String> selectedAssetList,
			List<String> selectedAssetIdsList,boolean enforceStart,boolean enableStart) {
		super();
		this.taskId = taskId;
		this.taskName = taskName;
		this.jobId = jobId;
		this.activityId = activityId;
		this.activityName = activityName;
		this.sequence = sequence;
		this.logbook = logbook;
		this.duration = duration;
		this.xPos = xPos;
		this.yPos = yPos;
		this.performer = performer;
		this.approver = approver;
		this.scheduledActivityStartTime = scheduledActivityStartTime;
		this.scheduledActivityEndTime = scheduledActivityEndTime;
		this.actualActivityStartTime = actualActivityStartTime;
		this.actualActivityEndTime = actualActivityEndTime;
		this.notBelongToPerformer = notBelongToPerformer;
		this.notBelongToApprover = notBelongToApprover;
		this.actvityStatus = actvityStatus;
		this.isActvityBtnDisableOnCompletion = isActvityBtnDisableOnCompletion;
		this.isActvityBtnDisbleForActvtOrder = isActvityBtnDisbleForActvtOrder;
		this.actAbrv = actAbrv;
		this.available = available;
		this.performerAvlReasons = performerAvlReasons;
		this.assetAvailable = assetAvailable;
		this.assetAvlReasons = assetAvlReasons;
		this.actualActvtyStrt = actualActvtyStrt;
		this.actualActvtEnd = actualActvtEnd;
		this.reviewerActivityStartTime = reviewerActivityStartTime;
		this.reviewerActivityStopTime = reviewerActivityStopTime;
		this.remarks = remarks;
		this.assetId = assetId;
		this.assetName = assetName;
		this.assetIDList = assetIDList;
		this.assetNameList = assetNameList;
		this.groupOrDept = groupOrDept;
		this.groupOrDeptName = groupOrDeptName;
		this.performerType = performerType;
		this.getGroupOrDeptWisePerformer = getGroupOrDeptWisePerformer;
		this.completedActivity = completedActivity;
		this.pendingActivity = pendingActivity;
		this.rejectedActivity = rejectedActivity;
		this.date = date;
		this.actvtCount = actvtCount;
		this.actFile = actFile;
		this.activityFileData = activityFileData;
		this.buffer = buffer;
		this.delayDueToBuffer = delayDueToBuffer;
		this.enforce = enforce;
		this.booleanForActivityStatus = booleanForActivityStatus;
		this.disable = disable;
		this.disableForAction = disableForAction;
		this.selectedAssetList = selectedAssetList;
		this.selectedAssetIdsList = selectedAssetIdsList;
		this.enforceStart=enforceStart;
		this.enableStart=enableStart;
	}

	public ActivityDetails(String taskId, String taskName, String activityId, String activityName, String logbook,
			String performer, String approver, Date scheduledActivityStartTime, Date scheduledActivityEndTime,
			String assetName,int sequence,int duration,boolean groupOrDept, String performerType, String groupOrDeptName,String assetId,String actvityStatus,Date actualActivityStartTime,Date actualActivityEndTime,String jobId,Integer weekdays) {
		super();
		this.taskId = taskId;
		this.taskName = taskName;
		this.activityId = activityId;
		this.activityName = activityName;
		this.logbook = logbook;
		this.performer = performer;
		this.approver = approver;
		this.scheduledActivityStartTime = scheduledActivityStartTime;
		this.scheduledActivityEndTime = scheduledActivityEndTime;
		this.assetId = assetId;
		this.assetName = assetName;
		this.sequence = sequence;
		this.duration = duration;
		this.groupOrDept = groupOrDept;
		this.performerType = performerType;
		this.groupOrDeptName = groupOrDeptName;
		this.actvityStatus=actvityStatus;
		this.actualActivityStartTime=actualActivityStartTime;
		this.actualActivityEndTime=actualActivityEndTime;
		this.jobId = jobId;
		this.weekdays = weekdays;
	}//Added By ashok

	public ActivityDetails() {
	}

	public ActivityDetails( String activityName) {
		this.activityName=activityName;
	}

	public ActivityDetails(String taskId, String activityId, String activityName, String xPos, String yPos) {
		super();
		this.taskId = taskId;
		this.activityId = activityId;
		this.activityName = activityName;
		this.xPos = xPos;
		this.yPos = yPos;
	}
	
	@Override
	public String toString() {
		return "ActivityDetails [taskId=" + taskId + ", taskName=" + taskName + ", jobId=" + jobId + ", activityId="
				+ activityId + ", activityName=" + activityName + ", sequence=" + sequence + ", logbook=" + logbook
				+ ", duration=" + duration + ", xPos=" + xPos + ", yPos=" + yPos + ", performer=" + performer
				+ ", approver=" + approver + ", scheduledActivityStartTime=" + scheduledActivityStartTime
				+ ", scheduledActivityEndTime=" + scheduledActivityEndTime + ", actualActivityStartTime="
				+ actualActivityStartTime + ", actualActivityEndTime=" + actualActivityEndTime
				+ ", notBelongToPerformer=" + notBelongToPerformer + ", notBelongToApprover=" + notBelongToApprover
				+ ", actvityStatus=" + actvityStatus + ", isActvityBtnDisableOnCompletion="
				+ isActvityBtnDisableOnCompletion + ", isActvityBtnDisbleForActvtOrder="
				+ isActvityBtnDisbleForActvtOrder + ", actAbrv=" + actAbrv + ", available=" + available
				+ ", performerAvlReasons=" + performerAvlReasons + ", assetAvailable=" + assetAvailable
				+ ", assetAvlReasons=" + assetAvlReasons + ", actualActvtyStrt=" + actualActvtyStrt
				+ ", actualActvtEnd=" + actualActvtEnd + ", reviewerActivityStartTime=" + reviewerActivityStartTime
				+ ", reviewerActivityStopTime=" + reviewerActivityStopTime + ", remarks=" + remarks + ", assetId="
				+ assetId + ", assetName=" + assetName + ", assetIDList=" + assetIDList + ", assetNameList="
				+ assetNameList + ", groupOrDept=" + groupOrDept + ", groupOrDeptName=" + groupOrDeptName
				+ ", performerType=" + performerType + ", getGroupOrDeptWisePerformer=" + getGroupOrDeptWisePerformer
				+ ", completedActivity=" + completedActivity + ", pendingActivity=" + pendingActivity
				+ ", rejectedActivity=" + rejectedActivity + ", date=" + date + ", actvtCount=" + actvtCount
				+ ", actFile=" + actFile + ", buffer=" + buffer + ", delayDueToBuffer=" + delayDueToBuffer
				+ ", enforce=" + enforce + ", selectedAssetList=" + selectedAssetList + ", selectedAssetIdsList="
				+ selectedAssetIdsList + "]";
	}
	
	//constructor with activity status
		public ActivityDetails(String taskId, String taskName, String activityId, String activityName, String logbook,
				String performer, String approver, Date scheduledActivityStartTime, Date scheduledActivityEndTime,
				String assetName,int sequence,int duration,boolean groupOrDept, String performerType, String groupOrDeptName,String assetId,String actvityStatus,int buffer) {
			super();
			this.taskId = taskId;
			this.taskName = taskName;
			this.activityId = activityId;
			this.activityName = activityName;
			this.logbook = logbook;
			this.performer = performer;
			this.approver = approver;
			this.scheduledActivityStartTime = scheduledActivityStartTime;
			this.scheduledActivityEndTime = scheduledActivityEndTime;
			this.assetId = assetId;
			this.assetName = assetName;
			this.sequence = sequence;
			this.duration = duration;
			this.groupOrDept = groupOrDept;
			this.performerType = performerType;
			this.groupOrDeptName = groupOrDeptName;
			this.actvityStatus=actvityStatus;
			this.buffer=buffer;
		}
		//Constructor with actual Start and Stop 
		public ActivityDetails(String taskId, String taskName, String activityId, String activityName, String logbook,
				String performer, String approver, Date scheduledActivityStartTime, Date scheduledActivityEndTime,
				String assetName,int sequence,int duration,boolean groupOrDept, String performerType, String groupOrDeptName,String assetId,String actvityStatus,Date actualActivityStartTime,Date actualActivityEndTime,String jobId) {
			super();
			this.taskId = taskId;
			this.taskName = taskName;
			this.activityId = activityId;
			this.activityName = activityName;
			this.logbook = logbook;
			this.performer = performer;
			this.approver = approver;
			this.scheduledActivityStartTime = scheduledActivityStartTime;
			this.scheduledActivityEndTime = scheduledActivityEndTime;
			this.assetId = assetId;
			this.assetName = assetName;
			this.sequence = sequence;
			this.duration = duration;
			this.groupOrDept = groupOrDept;
			this.performerType = performerType;
			this.groupOrDeptName = groupOrDeptName;
			this.actvityStatus=actvityStatus;
			this.actualActivityStartTime=actualActivityStartTime;
			this.actualActivityEndTime=actualActivityEndTime;
			this.jobId = jobId;
		}
	
	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public String getLogbook() {
		return logbook;
	}

	public void setLogbook(String logbook) {
		this.logbook = logbook;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}


	public String getTaskName() {
		return taskName;
	}


	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}


	public String getxPos() {
		return xPos;
	}


	public void setxPos(String xPos) {
		this.xPos = xPos;
	}

	public String getyPos() {
		return yPos;
	}

	public void setyPos(String yPos) {
		this.yPos = yPos;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public Date getScheduledActivityStartTime() {
		return scheduledActivityStartTime;
	}

	public void setScheduledActivityStartTime(Date scheduledActivityStartTime) {
		this.scheduledActivityStartTime = scheduledActivityStartTime;
	}

	public String getDate() {
		return date;
	}

	public int getActvtCount() {
		return actvtCount;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void setActvtCount(int actvtCount) {
		this.actvtCount = actvtCount;
	}

	public Date getScheduledActivityEndTime() {
		return scheduledActivityEndTime;
	}


	public void setScheduledActivityEndTime(Date scheduledActivityEndTime) {
		this.scheduledActivityEndTime = scheduledActivityEndTime;
	}


	public Date getActualActivityStartTime() {
		return actualActivityStartTime;
	}


	public void setActualActivityStartTime(Date actualActivityStartTime) {
		this.actualActivityStartTime = actualActivityStartTime;
	}


	public Date getActualActivityEndTime() {
		return actualActivityEndTime;
	}


	public void setActualActivityEndTime(Date actualActivityEndTime) {
		this.actualActivityEndTime = actualActivityEndTime;
	}


	public String getPerformer() {
		return performer;
	}


	public void setPerformer(String performer) {
		this.performer = performer;
	}


	public String getApprover() {
		return approver;
	}


	public void setApprover(String approver) {
		this.approver = approver;
	}


	public boolean isNotBelongToPerformer() {
		return notBelongToPerformer;
	}


	public void setNotBelongToPerformer(boolean notBelongToPerformer) {
		this.notBelongToPerformer = notBelongToPerformer;
	}


	public String getActvityStatus() {
		return actvityStatus;
	}


	public void setActvityStatus(String actvityStatus) {
		this.actvityStatus = actvityStatus;
	}

	public boolean isActvityBtnDisableOnCompletion() {
		return isActvityBtnDisableOnCompletion;
	}


	public void setActvityBtnDisableOnCompletion(boolean isActvityBtnDisableOnCompletion) {
		this.isActvityBtnDisableOnCompletion = isActvityBtnDisableOnCompletion;
	}


	public boolean isActvityBtnDisbleForActvtOrder() {
		return isActvityBtnDisbleForActvtOrder;
	}


	public void setActvityBtnDisbleForActvtOrder(boolean isActvityBtnDisbleForActvtOrder) {
		this.isActvityBtnDisbleForActvtOrder = isActvityBtnDisbleForActvtOrder;
	}


	public String getJobId() {
		return jobId;
	}


	public void setJobId(String jobId) {
		this.jobId = jobId;
	}


	public String getActualActvtyStrt() {
		return actualActvtyStrt;
	}


	public void setActualActvtyStrt(String actualActvtyStrt) {
		this.actualActvtyStrt = actualActvtyStrt;
	}


	public String getActualActvtEnd() {
		return actualActvtEnd;
	}


	public void setActualActvtEnd(String actualActvtEnd) {
		this.actualActvtEnd = actualActvtEnd;
	}

	public boolean isNotBelongToApprover() {
		return notBelongToApprover;
	}


	public void setNotBelongToApprover(boolean notBelongToApprover) {
		this.notBelongToApprover = notBelongToApprover;
	}


	public Date getReviewerActivityStartTime() {
		return reviewerActivityStartTime;
	}


	public void setReviewerActivityStartTime(Date reviewerActivityStartTime) {
		this.reviewerActivityStartTime = reviewerActivityStartTime;
	}


	public Date getReviewerActivityStopTime() {
		return reviewerActivityStopTime;
	}


	public void setReviewerActivityStopTime(Date reviewerActivityStopTime) {
		this.reviewerActivityStopTime = reviewerActivityStopTime;
	}


	public String getRemarks() {
		return remarks;
	}


	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}


	public int getCompletedActivity() {
		return completedActivity;
	}


	public int getPendingActivity() {
		return pendingActivity;
	}


	public int getRejectedActivity() {
		return rejectedActivity;
	}


	public void setCompletedActivity(int completedActivity) {
		this.completedActivity = completedActivity;
	}


	public void setPendingActivity(int pendingActivity) {
		this.pendingActivity = pendingActivity;
	}


	public void setRejectedActivity(int rejectedActivity) {
		this.rejectedActivity = rejectedActivity;
	}
	
	public String getAssetName() {
		return assetName;
	}
	public void setAssetName(String assetName) {
		this.assetName = assetName;
	}
	public String getActFile() {
		return actFile;
	}
	public void setActFile(String actFile) {
		this.actFile = actFile;
	}
	public String getActAbrv() {
		return actAbrv;
	}
	public void setActAbrv(String actAbrv) {
		this.actAbrv = actAbrv;
	}
	public boolean isAvailable() {
		return available;
	}
	public void setAvailable(boolean available) {
		this.available = available;
	}
	public List<PerformerAvailability> getPerformerAvlReasons() {
		return performerAvlReasons;
	}
	public void setPerformerAvlReasons(List<PerformerAvailability> performerAvlReasons) {
		this.performerAvlReasons = performerAvlReasons;
	}
	public int getBuffer() {
		return buffer;
	}
	public void setBuffer(int buffer) {
		this.buffer = buffer;
	}
	public boolean isEnforce() {
		return enforce;
	}
	public void setEnforce(boolean enforce) {
		this.enforce = enforce;
	}
	public List<String> getAssetIDList() {
		return assetIDList;
	}
	public void setAssetIDList(List<String> assetID) {
		this.assetIDList = assetID;
	}
	public List<String> getAssetNameList() {
		return assetNameList;
	}
	public void setAssetNameList(List<String> assetNameList) {
		this.assetNameList = assetNameList;
	}
	public String getAssetId() {
		return assetId;
	}
	public void setAssetId(String assetId) {
		this.assetId = assetId;
	}
	public boolean isGroupOrDept() {
		return groupOrDept;
	}
	public void setGroupOrDept(boolean groupOrDept) {
		this.groupOrDept = groupOrDept;
	}
	public String getGroupOrDeptName() {
		return groupOrDeptName;
	}
	public void setGroupOrDeptName(String groupOrDeptName) {
		this.groupOrDeptName = groupOrDeptName;
	}
	public String getPerformerType() {
		return performerType;
	}
	public void setPerformerType(String performerType) {
		this.performerType = performerType;
	}
	public List<User> getGetGroupOrDeptWisePerformer() {
		return getGroupOrDeptWisePerformer;
	}
	public void setGetGroupOrDeptWisePerformer(List<User> getGroupOrDeptWisePerformer) {
		this.getGroupOrDeptWisePerformer = getGroupOrDeptWisePerformer;
	}
	public List<String> getSelectedAssetList() {
		return selectedAssetList;
	}
	public void setSelectedAssetList(List<String> selectedAssetList) {
		this.selectedAssetList = selectedAssetList;
	}
	public List<String> getSelectedAssetIdsList() {
		return selectedAssetIdsList;
	}
	public void setSelectedAssetIdsList(List<String> selectedAssetIdsList) {
		this.selectedAssetIdsList = selectedAssetIdsList;
	}
	public List<PerformerAvailability> getAssetAvlReasons() {
		return assetAvlReasons;
	}
	public void setAssetAvlReasons(List<PerformerAvailability> assetAvlReasons) {
		this.assetAvlReasons = assetAvlReasons;
	}
	public boolean isAssetAvailable() {
		return assetAvailable;
	}
	public void setAssetAvailable(boolean assetAvailable) {
		this.assetAvailable = assetAvailable;
	}
	public String getDelayDueToBuffer() {
		return delayDueToBuffer;
	}
	public void setDelayDueToBuffer(String delayDueToBuffer) {
		this.delayDueToBuffer = delayDueToBuffer;
	}
	public boolean isBooleanForActivityStatus() {
		return booleanForActivityStatus;
	}
	public void setBooleanForActivityStatus(boolean booleanForActivityStatus) {
		this.booleanForActivityStatus = booleanForActivityStatus;
	}
	public boolean isDisable() {
		return disable;
	}
	public void setDisable(boolean disable) {
		this.disable = disable;
	}
	public boolean isDisableForAction() {
		disableForAction = this.notBelongToPerformer || this.isActvityBtnDisbleForActvtOrder 
				|| this.booleanForActivityStatus || this.disable;
		return disableForAction;
	}
	public void setDisableForAction(boolean disableForAction) {
		this.disableForAction = disableForAction;
	}

	public Integer getWeekdays() {
		return weekdays;
	}

	public void setWeekdays(Integer weekdays) {
		this.weekdays = weekdays;
	}

	public boolean getEnforceStart() {
		return enforceStart;
	}

	public void setEnforceStart(boolean enforceStart) {
		this.enforceStart= enforceStart;
	}

}

