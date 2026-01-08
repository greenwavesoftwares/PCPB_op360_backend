package in.co.greenwave.UserGroup.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

//only shows not null parameters in the json
@JsonInclude(JsonInclude.Include.NON_NULL)
//lexicographically arranges the alphabets
@JsonPropertyOrder(alphabetic=true)
public class TaskDetail implements Serializable {

	/**
	 * Serial version UID for serialization and deserialization compatibility.
	 * Used to ensure that the serialized version of the class is compatible with its deserialized version.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Time when the task was created, typically in a string format (e.g., "yyyy-MM-dd HH:mm:ss").
	 */
	private String creationTime;

	/**
	 * Name of the task, which can be used for identifying the task in the system.
	 */
	private String taskName;

	/**
	 * Unique identifier for the task, used to reference and manage the task in the system.
	 */
	private String taskId;

	/**
	 * Status of the task (e.g., "Pending", "In Progress", "Completed").
	 */
	private String status;

	/**
	 * Name of the reviewer assigned to review or approve the task.
	 */
	private String reviewer;

	/**
	 * Additional remarks or comments related to the task.
	 */
	private String remarks;

	/**
	 * Detailed description of the task, providing context or specific instructions.
	 */
	private String description;

	/**
	 * Name of the creator or initiator of the task.
	 */
	private String creator;

	/**
	 * Time duration associated with the task, typically in hours and minutes (e.g., "2 hours 30 minutes").
	 */
	private String hourMinutes;

	/**
	 * List of activity details related to the task. Each entry could represent a specific step or part of the task.
	 */
	private List<ActivityDetails> activityList;

	/**
	 * File associated with the activity, could represent a report or document related to the task.
	 */
	// private DefaultDiagramModel model; // Model for diagram representation (commented out for now)

	private String actFile;  // Activity-related file (e.g., report, log)

	// List of user groups associated with the task, representing the groups of users that can be involved or notified
	private List<String> userGroupsList = new ArrayList<String>();

	// List of user departments associated with the task, representing the departments that have responsibilities or interest in the task
	private List<String> userDeptsList = new ArrayList<>();

	/**
	 * Asset group associated with the task, potentially linking it to specific assets or tools needed.
	 */
	private String assetGroup;

	/**
	 * Integer representing the task's status transition. It could indicate the task's progression through various stages (e.g., 1 for Pending, 2 for In Progress).
	 */
	private int statusTransition;

	/**
	 * Number of activities associated with the task, indicating how many individual activities or steps are involved.
	 */
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

