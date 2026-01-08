package in.co.greenwave.dashboardapi.model;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

//import org.primefaces.model.diagram.DefaultDiagramModel;
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(alphabetic = true)
public class TaskDetail implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String creationTime;
	private String taskName;
	private String taskId;
	private String status;
	private String reviewer;
	private String remarks;
	private String description;
	private String creator;
	private String hourMinutes;
	private List<ActivityDetails> activityList;
//	private DefaultDiagramModel model;
	private String actFile;

	private List<String> userGroupsList = new ArrayList<String>();
	private List<String> userDeptsList = new ArrayList<>();
	private String assetGroup;
	
	private int statusTransition;
	private int noOfActivites;

	public TaskDetail(String taskName, String reviewer, String remarks, String description) {
		super();
		this.taskName = taskName;
		this.reviewer = reviewer;
		this.remarks = remarks;
		this.description = description;
	}
	
	public TaskDetail(String taskName, String taskId, List<ActivityDetails> activityList, List<String> userGroupsList, List<String> userDeptsList, String assetGroup) {
		super();
		this.taskName = taskName;
		this.taskId = taskId;
		this.activityList = activityList;
		this.userGroupsList = userGroupsList;
		this.userDeptsList = userDeptsList;
		this.assetGroup = assetGroup;
	}

	public TaskDetail(String taskName, String taskId, List<ActivityDetails> activityList) {
		super();
		this.taskName = taskName;
		this.taskId = taskId;
		this.activityList = activityList;
	}

	public TaskDetail(List<ActivityDetails> activityList) {
		super();
		this.activityList = activityList;
	}

	public TaskDetail() {

	}

	public TaskDetail(TaskDetail td) {
		// TODO Auto-generated constructor stub
	}

	public String getTaskName() {
		return taskName;
	}

	public String getReviewer() {
		return reviewer;
	}

	public String getRemarks() {
		return remarks;
	}

	public String getDescription() {
		return description;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public void setReviewer(String reviewer) {
		this.reviewer = reviewer;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	public String getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(String creationTime) {
		this.creationTime = creationTime;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getHourMinutes() {
		return hourMinutes;
	}

	public void setHourMinutes(String hourMinutes) {
		this.hourMinutes = hourMinutes;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<ActivityDetails> getActivityList() {
		return activityList;
	}

	public void setActivityList(List<ActivityDetails> activityList) {
		this.activityList = activityList;
	}

	@Override
	public String toString() {
		return "TaskDetail [creationTime=" + creationTime + ", taskName=" + taskName + ", taskId=" + taskId
				+ ", status=" + status + ", reviewer=" + reviewer + ", remarks=" + remarks + ", description="
				+ description + ", creator=" + creator + ", hourMinutes=" + hourMinutes + ", activityList="
				+ activityList + ", model=" + /*model +*/ ", actFile=" + actFile + ", userGroupsList=" + userGroupsList
				+ ", userDeptsList=" + userDeptsList + ", assetGroup=" + assetGroup + "]";
	}

//	public DefaultDiagramModel getModel() {
//		return model;
//	}
//
//	public void setModel(DefaultDiagramModel model) {
//		this.model = model;
//	}

	public String getActFile() {
		return actFile;
	}

	public void setActFile(String actFile) {
		this.actFile = actFile;
	}

	public List<String> getUserGroupsList() {
		return userGroupsList;
	}

	public void setUserGroupsList(List<String> userGroupsList) {
		this.userGroupsList = userGroupsList;
	}

	public List<String> getUserDeptsList() {
		return userDeptsList;
	}

	public void setUserDeptsList(List<String> userDeptsList) {
		this.userDeptsList = userDeptsList;
	}

	public String getAssetGroup() {
		return assetGroup;
	}

	public void setAssetGroup(String assetGroup) {
		this.assetGroup = assetGroup;
	}

	public int getStatusTransition() {
		return statusTransition;
	}

	public void setStatusTransition(int statusTransition) {
		this.statusTransition = statusTransition;
	}

	public int getNoOfActivites() {
		return noOfActivites;
	}

	public void setNoOfActivites(int noOfActivites) {
		this.noOfActivites = noOfActivites;
	}
}


