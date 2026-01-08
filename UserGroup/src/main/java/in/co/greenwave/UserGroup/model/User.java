package in.co.greenwave.UserGroup.model;

import java.util.ArrayList;

import java.util.HashSet;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
//only shows not null parameters in the json
@JsonInclude(JsonInclude.Include.NON_NULL)
//lexicographically arranges the alphabets
@JsonPropertyOrder(alphabetic=true)
public class User {
	
	// Unique identifier for the user, typically used for login or user management
	private String userID;

	// Name of the user, typically their full name or username
	private String userName;

	// Password for user authentication (should be securely managed)
	private String password;

	// List of groups the user is assigned to, used for role-based access control or permissions
	private List<String> group;

	// User's phone number, often used for communication or verification
	private String phone;

	// URL or path for the user's workflow homepage
	private String workFlowHomePage;

	// URL or path for the user's G360 homepage (possibly a dashboard or main page)
	private String g360HomePage;

	// Flag indicating if the user has administrative privileges in the G360 system
	private boolean g360Admin;

	// List of pages allotted to the user for the G360 platform
	private List<String> allotedPages;

	// Flag indicating if the user account is active or deactivated (default is active)
	private boolean active = true;

	// Timestamp indicating when the user account was created
	private String createdOn;

	// Timestamp indicating when the user account was last modified
	private String modifiedOn;

	// The user who created the account (typically an admin or system)
	private String createdBy;

	// The user who last modified the account
	private String modifiedBy;

	// Department to which the user belongs, used for organizing and managing users by department
	private String department;

	// Flag indicating if the user is required to change their password on first login (default is true)
	private boolean firstLoginRequired = true;

	// Duration in days after which the user's password will expire (default is 90 days)
	private int passwordExpiryDuration = 90;

	// The date when the user's password will expire
	private String passwordExpiryDate;

	// Timestamp of the user's last login
	private String lastLogIn;

	// Error message string for tracking authentication or user-related errors
	private String errMsg;

	// List of pages allocated to the user for the G360 homepage selection
	private List<String> g360AllottedPageList = new ArrayList<>();

	// List of pages allocated to the user for the workflow homepage selection
	private List<String> workflowAllottedPageList = new ArrayList<>();

	// Set of all available G360 pages for the user to choose from
	private HashSet<String> allG360PageList = new HashSet<>();

	// Set of all available workflow pages for the user to choose from
	private HashSet<String> allWorkflowPageList = new HashSet<>();

	public User(String errMsg) {
		this.errMsg = errMsg;
		// TODO Auto-generated constructor stub
	}
	
	public User() {
		super();
		// TODO Auto-generated constructor stub
	}
	public User(String userID, String userName) {
		this.userID = userID;
		this.userName = userName;
	}
	
	public User(String userID, List<String> allotedPages) {
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
	public User(String userID, String userName, String password, String phone, String workFlowHomePage,
			String g360HomePage, boolean g360Admin, boolean active, String createdOn, String modifiedOn,
			String createdBy, String modifiedBy) {
		super();
		this.userID = userID;
		this.userName = userName;
		this.password = password;
		this.phone = phone;
		this.workFlowHomePage = workFlowHomePage;
		this.g360HomePage = g360HomePage;
		this.g360Admin = g360Admin;
		this.active = active;
		this.createdOn = createdOn;
		this.modifiedOn = modifiedOn;
		this.createdBy = createdBy;
		this.modifiedBy = modifiedBy;
	}

	public User(String userID, String userName, String password, String phone, String workFlowHomePage,
			String g360HomePage, boolean g360Admin, boolean active, String createdOn, String modifiedOn,
			String createdBy, String modifiedBy , String department) {
		super();
		this.userID = userID;
		this.userName = userName;
		this.password = password;
		this.phone = phone;
		this.workFlowHomePage = workFlowHomePage;
		this.g360HomePage = g360HomePage;
		this.g360Admin = g360Admin;
		this.active = active;
		this.createdOn = createdOn;
		this.modifiedOn = modifiedOn;
		this.createdBy = createdBy;
		this.modifiedBy = modifiedBy;
		this.department = department;
	}
	
	


	public User(String userID, String userName, String password, String phone, String workFlowHomePage,
			String g360HomePage, boolean g360Admin, boolean active, String createdOn, String modifiedOn,
			String createdBy, String modifiedBy, String department, boolean firstLoginRequired,
			int passwordExpiryDuration,String passwordExpiryDate , String lastLogIn) {
		super();
		this.userID = userID;
		this.userName = userName;
		this.password = password;
		this.phone = phone;
		this.workFlowHomePage = workFlowHomePage;
		this.g360HomePage = g360HomePage;
		this.g360Admin = g360Admin;
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
	}


	public User(User us) {
		
	}
	
	public  void updateSourceSelectionPages(List<String> allotedPages){


		g360AllottedPageList = new ArrayList<>();
		workflowAllottedPageList = new ArrayList<>();


		for(String onePage : allotedPages) {

			if(allG360PageList.contains(onePage) ) {
				g360AllottedPageList.add(onePage);
			}

			if(allWorkflowPageList.contains(onePage)) {
				workflowAllottedPageList.add(onePage);
			}
		}

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
				+ ", allWorkflowPageList=" + allWorkflowPageList + "]";
	}
//	@XmlElement(name = "usID", namespace = "http://example.com")
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
	public List<String> getAllotedPages() {
		return allotedPages;
	}
	public void setAllotedPages(List<String> allotedPages) {

		updateSourceSelectionPages(allotedPages);

		this.allotedPages = allotedPages;
	}


	public List<String> getGroup() {
		return group;
	}
	public void setGroup(List<String> group) {
		this.group = group;
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


	public List<String> getG360AllottedPageList() {
		return g360AllottedPageList;
	}


	public void setG360AllottedPageList(List<String> g360AllottedPageList) {
		this.g360AllottedPageList = g360AllottedPageList;
	}


	public List<String> getWorkflowAllottedPageList() {
		return workflowAllottedPageList;
	}


	public void setWorkflowAllottedPageList(List<String> workflowAllottedPageList) {
		this.workflowAllottedPageList = workflowAllottedPageList;
	}


	public HashSet<String> getAllG360PageList() {
		return allG360PageList;
	}


	public void setAllG360PageList(HashSet<String> allG360PageList) {
		this.allG360PageList = allG360PageList;
	}


	public HashSet<String> getAllWorkflowPageList() {
		return allWorkflowPageList;
	}


	public void setAllWorkflowPageList(HashSet<String> allWorkflowPageList) {
		this.allWorkflowPageList = allWorkflowPageList;
	}


	public String getDepartment() {
		return department;
	}


	public void setDepartment(String department) {
		this.department = department;
	}


	public boolean isFirstLoginRequired() {
		return firstLoginRequired;
	}


	public void setFirstLoginRequired(boolean firstLoginRequired) {
		this.firstLoginRequired = firstLoginRequired;
	}


	public int getPasswordExpiryDuration() {
		System.out.println(passwordExpiryDuration);
		return passwordExpiryDuration;
	}


	public void setPasswordExpiryDuration(int passwordExpiryDuration) {
//		System.out.println(passwordExpiryDuration);
		this.passwordExpiryDuration = passwordExpiryDuration;
	}


	public String getLastLogIn() {
		return lastLogIn;
	}


	public void setLastLogIn(String lastLogIn) {
		this.lastLogIn = lastLogIn;
	}


	public String getPasswordExpiryDate() {
		return passwordExpiryDate;
	}


	public void setPasswordExpiryDate(String passwordExpiryDate) {
		this.passwordExpiryDate = passwordExpiryDate;
	}



}
