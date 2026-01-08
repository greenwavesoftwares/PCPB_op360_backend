package in.co.greenwave.dashboardapi.model;


import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import in.co.greenwave.dashboardapi.dao.factory.DAOFactory;





public class ActivityDetails implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String taskId;
	private String taskName;
	private String jobId;
	private String activityId;
	private String activityName;
	private int sequence = 1;
	private String logbook;
	private int duration;
	private String xPos;
	private String yPos;
	private String performer;
	private String approver;
	private Date scheduledActivityStartTime;
	private Date scheduledActivityEndTime;
	private Date actualActivityStartTime;
	private Date actualActivityEndTime;
	private boolean notBelongToPerformer;
	private boolean notBelongToApprover;
	private String actvityStatus;
	private boolean isActvityBtnDisableOnCompletion;
	private boolean isActvityBtnDisbleForActvtOrder;
	private String actAbrv;
	private boolean available = true;
	private List<PerformerAvailability> performerAvlReasons = new LinkedList<PerformerAvailability>();
	private boolean assetAvailable = true;
	private List<PerformerAvailability> assetAvlReasons = new LinkedList<PerformerAvailability>();

	private String actualActvtyStrt;
	private String actualActvtEnd;
	private Date reviewerActivityStartTime;
	private Date reviewerActivityStopTime;
	private String remarks;
	private String assetId;
	private String assetName;
	private List<String> assetIDList;
	private List<String> assetNameList;
	private boolean groupOrDept;
	private String groupOrDeptName;
	private String performerType;
	private List<User> getGroupOrDeptWisePerformer;

	private int completedActivity;
	private int pendingActivity;
	private int rejectedActivity;

	private String date;
	private int actvtCount;
	private String actFile;
//	DAOFactory SQLServerFactory = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
//	JobAssignDAO dao = SQLServerFactory.getJobAssignService();
//	AssetConfigurationDAO assetDao = SQLServerFactory.getAssetConfigurationService();
////	public DateFormat dfDtTm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private int buffer;
	private String delayDueToBuffer;
	private boolean enforce;
	private boolean booleanForActivityStatus = false;
	private boolean disable;
	private boolean disableForAction;
	
	private List<String> selectedAssetList;
	private List<String> selectedAssetIdsList;

	public String redirectLogbook() {
		System.out.println("Performer.redirectLogbook()"+this.logbook);
		return "LogbookForm.xhtml?selectedLogBook="+this.logbook+"faces-redirect=true&includeViewParams=true";
	}
//	public String redirectLogbook(String jobId) {
//		System.out.println("Performer.redirectLogbook()"+this.logbook);
//		HttpSession session = (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(true);
//		session.setAttribute("ActivityId", this.activityId);
//		session.setAttribute("JobId", jobId);
//		return "LogbookForm.xhtml?faces-redirect=true";
//	}
//	public String redirectLogbook(JobDetails job) {
//		System.out.println("Performer.redirectLogbook()"+this.logbook);
//		HttpSession session = (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(true);
//		session.setAttribute("ActivityId", this.activityId);
//		session.setAttribute("JobId", job.getJobID());
//		session.setAttribute("job", job);
//		return "LogbookForm.xhtml?faces-redirect=true";
//	}

//	public void onPerformerSelection(ActivityDetails row) {
//		System.out.println("ActivityDetails.onPerformerSelection() "+row);
//		performerAvlReasons = new LinkedList<PerformerAvailability>();
//		performerAvlReasons = dao.getPerformerAvailability(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(row.getScheduledActivityStartTime()), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(row.getScheduledActivityEndTime()), row.getPerformer());
//		if(performerAvlReasons.size()>0) {
//			this.setAvailable(false);
//		}else {
//			this.setAvailable(true);
//		}
//		System.out.println("user availability is "+this.available);
//	}
//	
//	public void onAssetSelection(ActivityDetails row, Map<String,String> assetNameIdMapping) {
//		System.out.println("ActivityDetails.onAssetSelection() "+row);
//		System.out.println(assetNameIdMapping.get(row.getAssetName()));
//		assetAvlReasons = new LinkedList<PerformerAvailability>();
//		assetAvlReasons = dao.getAssetAvailability(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(row.getScheduledActivityStartTime()), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(row.getScheduledActivityEndTime()), assetNameIdMapping.get(row.getAssetName()));
//		if(assetAvlReasons.size()>0) {
//			this.setAssetAvailable(false);
//		}else {
//			this.setAssetAvailable(true);
//		}
//		System.out.println("user availability is "+this.assetAvailable);
//	}
//	
//	public void onGroupOrDeptToggle(ActivityDetails active,String group,Map<String,String> userGroupMapping,String selectedOption) {
//		System.out.println("JobAssigner.onGroupOrDeptToggle()");
//		getGroupOrDeptWisePerformer = new LinkedList<User>();
//		
////		System.out.println(selectedOption);
//		
//		if (selectedOption.equalsIgnoreCase("User Group")) {
//			String getGroup = "";
//			for(Map.Entry<String, String> m: userGroupMapping.entrySet()) {
//				if (m.getValue().equalsIgnoreCase(group)) {
//					getGroup = m.getKey();
//					break;
//				}
//			}
////			System.out.println(getGroup);
//			getGroupOrDeptWisePerformer = assetDao.getUserDeptOrGroupwsie(getGroup, "User Group");
//		} 
//		else if (selectedOption.equalsIgnoreCase("Department")) {
////			System.out.println(group);
//			getGroupOrDeptWisePerformer = assetDao.getUserDeptOrGroupwsie(group, "department");
//		} 
//		else
//			System.out.println("Do nothing");
//		
//		active.setPerformer(null);
//		active.setGroupOrDeptName(group);
////		System.out.println(group);
//		List<String> s = new ArrayList<String>();
//		String temp = "";
//		for(User u: getGroupOrDeptWisePerformer) {
//			s.add(u.getUserID());
//		}
//		temp = s.stream().collect(Collectors.joining(","));
//		System.out.println(temp);
//		active.setPerformer(temp);
//	}

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
}

