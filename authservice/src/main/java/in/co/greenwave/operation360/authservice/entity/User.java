package in.co.greenwave.operation360.authservice.entity;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * The User class represents a user in the system, containing details such as the user's ID, 
 * name, password, assigned groups, contact information, and the pages they have access to.
 * It also includes information about their role, active status, and homepages for 
 * different modules (e.g., G360 and Workflow).
 */
@JsonPropertyOrder(alphabetic = true)  // Ensure properties are serialized in alphabetical order.
public class User {

	// Unique identifier for the user
	private String userID;

	// The display name of the user
	private String userName;

	// The user's password (should be hashed and stored securely)
	private String password;

	// The list of groups that the user belongs to
	private List<UserGroup> group = new ArrayList<>();

	// User's contact phone number
	private String phone;

	// The homepage for the user's workflow module
	private String workFlowHomePage;

	// The homepage for the G360 module
	private String g360HomePage;

	// A flag indicating whether the user is a G360 admin
	private boolean g360Admin;

	// The list of pages that have been assigned to the user
	private List<Page> allotedPages = new ArrayList<>();

	// Indicates if the user is currently active
	private boolean active = true;

	// The date when the user was created
	private String createdOn;

	// The date when the user was last modified
	private String modifiedOn;

	// The ID of the user who created this user record
	private String createdBy;

	// The ID of the user who last modified this user record
	private String modifiedBy;

	// The department that the user belongs to
	private UserDepartment department = new UserDepartment();

	// A flag indicating if the user needs to change their password on first login
	private boolean firstLoginRequired = true;

	// The number of days after which the user's password expires
	private int passwordExpiryDuration = 90;

	// The date when the user's password will expire
	private String passwordExpiryDate;

	// The date when the user last logged in
	private String lastLogIn;

	// List of pages for selecting the homepage in G360
	private List<Page> g360AllottedPageList = new ArrayList<>();

	// List of pages for selecting the homepage in Workflow
	private List<Page> workflowAllottedPageList = new ArrayList<>();

	// List of all available pages in G360
	private List<Page> allG360PageList = new ArrayList<>();

	// List of all available pages in Workflow
	private List<Page> allWorkflowPageList = new ArrayList<>();

	// The tenant ID associated with the user
	private String tenantId;

	/**
	 * Default constructor to create an empty User object.
	 */
	public User() {
		super();
	}

	/**
	 * Constructor to initialize a User with a userID and userName.
	 * 
	 * @param userID the unique ID of the user.
	 * @param userName the display name of the user.
	 */
	public User(String userID, String userName) {
		this.userID = userID;
		this.userName = userName;
	}

	/**
	 * Constructor to initialize a User with a userID and allotted pages.
	 * 
	 * @param userID the unique ID of the user.
	 * @param allotedPages the pages assigned to the user.
	 */
	public User(String userID, List<Page> allotedPages) {
		this.userID = userID;
		this.allotedPages = allotedPages;
	}

	/**
	 * Generates a unique hash code for the User object based on the userID.
	 * This is used to efficiently compare User objects.
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((userID == null) ? 0 : userID.hashCode());
		return result;
	}

	/**
	 * Compares this User object with another object to check for equality based on userID.
	 * 
	 * @param obj the object to compare.
	 * @return true if both User objects have the same userID, false otherwise.
	 */
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
	
	/**
	 * Parameterized constructor for initializing a User object with all its fields.
	 * This constructor allows you to set all properties of a User at the time of creation.
	 * 
	 * @param userID                   The unique identifier for the user.
	 * @param userName                 The display name of the user.
	 * @param password                 The password of the user (should be securely hashed).
	 * @param group                    A list of user groups to which the user belongs.
	 * @param phone                    The user's contact phone number.
	 * @param workFlowHomePage          The homepage URL for the user's workflow module.
	 * @param g360HomePage              The homepage URL for the G360 module.
	 * @param g360Admin                 A boolean flag indicating whether the user is a G360 administrator.
	 * @param allotedPages              A list of pages assigned to the user.
	 * @param active                   A boolean indicating whether the user account is active.
	 * @param createdOn                 The date when the user account was created.
	 * @param modifiedOn                The date when the user account was last modified.
	 * @param createdBy                 The ID of the user who created this user account.
	 * @param modifiedBy                The ID of the user who last modified this user account.
	 * @param department                The department to which the user belongs.
	 * @param firstLoginRequired        A boolean indicating if the user must change their password on first login.
	 * @param passwordExpiryDuration    The number of days after which the user's password expires.
	 * @param passwordExpiryDate        The date when the user's password will expire.
	 * @param lastLogIn                 The date of the user's last login.
	 * @param g360AllottedPageList      A list of pages the user can select for their G360 homepage.
	 * @param workflowAllottedPageList  A list of pages the user can select for their Workflow homepage.
	 * @param allG360PageList           A list of all available pages in the G360 system.
	 * @param allWorkflowPageList       A list of all available pages in the Workflow system.
	 * @param tenantId                  The tenant ID associated with the user.
	 */
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

	// Getters and Setters for all fields 
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
	
	
	/**
     * Returns a string representation of the User object, helpful for logging or debugging.
     * 
     * @return a string that represents the User object.
     */
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

