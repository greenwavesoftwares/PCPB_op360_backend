package in.co.greenwave.usermodule.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
@JsonPropertyOrder(alphabetic = true)
public class User {
	
	private String userID; // Unique identifier for the user that is used during login operations360
	private String userName; // name of the user
	private String password; // Password for user authentication
	private List<UserGroup> group = new ArrayList<>(); // List of user groups the user belongs to
	private String phone; // User's phone number
	private String workFlowHomePage; // URL or identifier for the user's workflow home page
	private String g360HomePage; // URL or identifier for the user's G360 home page
	private boolean g360Admin; // Flag indicating if the user is a G360 admin
	private List<Page> allotedPages = new ArrayList<>(); // List of pages allotted to the user
	private boolean active = true; // Flag to check if the user account is active
	private String createdOn; // Timestamp of when the user was created
	private String modifiedOn; // Timestamp of the last modification of the user
	private String createdBy; // Identifier of who created the user
	private String modifiedBy; // Identifier of who last modified the user
	private UserDepartment department = new UserDepartment(); // User's department details

	private boolean firstLoginRequired = true; // Flag indicating if the user needs to reset their password on first login
	private int passwordExpiryDuration = 90; // Duration in days for password expiry
	private String passwordExpiryDate; // Date when the password will expire
	private String lastLogIn; // Timestamp of the user's last login

	// List of pages for G360 homepage selection
	private List<Page> g360AllottedPageList = new ArrayList<>();

	// List of pages for workflow homepage selection
	private List<Page> workflowAllottedPageList = new ArrayList<>();

	// List of all available pages for G360 home page selection
	private List<Page> allG360PageList = new ArrayList<>();

	// List of all available pages for workflow home page selection
	private List<Page> allWorkflowPageList = new ArrayList<>();

	private String tenantId; // Identifier for the tenant the user belongs to


	public User() {
		super();
		// TODO Auto-generated constructor stub
	}
	public User(String userID, String userName) {
		this.userID = userID;
		this.userName = userName;
	}
	
	public User(String userID, List<Page> allotedPages) {
		this.userID = userID;
		this.allotedPages = allotedPages;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((userID == null) ? 0 : userID.hashCode());
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
		User other = (User) obj;
		if (userID == null) {
			if (other.userID != null)
				return false;
		} else if (!userID.equals(other.userID))
			return false;
		return true;
	}
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public List<UserGroup> getGroup() {
		return group;
	}
	public void setGroup(List<UserGroup> group) {
		this.group = group;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getWorkFlowHomePage() {
		return workFlowHomePage;
	}
	public void setWorkFlowHomePage(String workFlowHomePage) {
		this.workFlowHomePage = workFlowHomePage;
	}
	public String getG360HomePage() {
		return g360HomePage;
	}
	public void setG360HomePage(String g360HomePage) {
		this.g360HomePage = g360HomePage;
	}
	public boolean isG360Admin() {
		return g360Admin;
	}
	public void setG360Admin(boolean g360Admin) {
		this.g360Admin = g360Admin;
	}

	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public String getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}
	public String getModifiedOn() {
		return modifiedOn;
	}
	public void setModifiedOn(String modifiedOn) {
		this.modifiedOn = modifiedOn;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public UserDepartment getDepartment() {
		return department;
	}
	public void setDepartment(UserDepartment department) {
		this.department = department;
	}
	public boolean isFirstLoginRequired() {
		return firstLoginRequired;
	}
	public void setFirstLoginRequired(boolean firstLoginRequired) {
		this.firstLoginRequired = firstLoginRequired;
	}
	public int getPasswordExpiryDuration() {
		return passwordExpiryDuration;
	}
	public void setPasswordExpiryDuration(int passwordExpiryDuration) {
		this.passwordExpiryDuration = passwordExpiryDuration;
	}
	public String getPasswordExpiryDate() {
		return passwordExpiryDate;
	}
	public void setPasswordExpiryDate(String passwordExpiryDate) {
		this.passwordExpiryDate = passwordExpiryDate;
	}
	public String getLastLogIn() {
		return lastLogIn;
	}
	public void setLastLogIn(String lastLogIn) {
		this.lastLogIn = lastLogIn;
	}
	public List<Page> getG360AllottedPageList() {
		return g360AllottedPageList;
	}
	public void setG360AllottedPageList(List<Page> g360AllottedPageList) {
		this.g360AllottedPageList = g360AllottedPageList;
	}
	public List<Page> getWorkflowAllottedPageList() {
		return workflowAllottedPageList;
	}
	public void setWorkflowAllottedPageList(List<Page> workflowAllottedPageList) {
		this.workflowAllottedPageList = workflowAllottedPageList;
	}
	
	public String getTenantId() {
		return tenantId;
	}
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
	public List<Page> getAllG360PageList() {
		return allG360PageList;
	}
	public void setAllG360PageList(List<Page> allG360PageList) {
		this.allG360PageList = allG360PageList;
	}
	public List<Page> getAllWorkflowPageList() {
		return allWorkflowPageList;
	}
	public void setAllWorkflowPageList(List<Page> allWorkflowPageList) {
		this.allWorkflowPageList = allWorkflowPageList;
	}
	public List<Page> getAllotedPages() {
		return allotedPages;
	}
	public void setAllotedPages(List<Page> allotedPages) {
		this.allotedPages = allotedPages;
	}
	public User(String userID, String userName, String password, List<UserGroup> group, String phone,
			String workFlowHomePage, String g360HomePage, boolean g360Admin, List<Page> allotedPages, boolean active,
			String createdOn, String modifiedOn, String createdBy, String modifiedBy, UserDepartment department,
			boolean firstLoginRequired, int passwordExpiryDuration, String passwordExpiryDate, String lastLogIn,
			List<Page> g360AllottedPageList, List<Page> workflowAllottedPageList, List<Page> allG360PageList,
			List<Page> allWorkflowPageList, String tenantId) {
		super();
		this.userID = userID;
		this.userName = userName;
		this.password = password;
		this.group = group;
		this.phone = phone;
		this.workFlowHomePage = workFlowHomePage;
		this.g360HomePage = g360HomePage;
		this.g360Admin = g360Admin;
		this.allotedPages = allotedPages;
		this.active = active;
		this.createdOn = createdOn;
		this.modifiedOn = modifiedOn;
		this.createdBy = createdBy;
		this.modifiedBy = modifiedBy;
		this.department = department;
		this.firstLoginRequired = firstLoginRequired;
		this.passwordExpiryDuration = passwordExpiryDuration;
		this.passwordExpiryDate = passwordExpiryDate;
		this.lastLogIn = lastLogIn;
		this.g360AllottedPageList = g360AllottedPageList;
		this.workflowAllottedPageList = workflowAllottedPageList;
		this.allG360PageList = allG360PageList;
		this.allWorkflowPageList = allWorkflowPageList;
		this.tenantId = tenantId;
	}
	@Override
	public String toString() {
		return "User [userID=" + userID + ", userName=" + userName + ", password=" + password + ", group=" + group
				+ ", phone=" + phone + ", workFlowHomePage=" + workFlowHomePage + ", g360HomePage=" + g360HomePage
				+ ", g360Admin=" + g360Admin + ", allotedPages=" + allotedPages + ", active=" + active + ", createdOn="
				+ createdOn + ", modifiedOn=" + modifiedOn + ", createdBy=" + createdBy + ", modifiedBy=" + modifiedBy
				+ ", department=" + department + ", firstLoginRequired=" + firstLoginRequired
				+ ", passwordExpiryDuration=" + passwordExpiryDuration + ", passwordExpiryDate=" + passwordExpiryDate
				+ ", lastLogIn=" + lastLogIn + ", g360AllottedPageList=" + g360AllottedPageList
				+ ", workflowAllottedPageList=" + workflowAllottedPageList + ", allG360PageList=" + allG360PageList
				+ ", allWorkflowPageList=" + allWorkflowPageList + ", tenantId=" + tenantId + "]";
	}
	
	
	
	

}

