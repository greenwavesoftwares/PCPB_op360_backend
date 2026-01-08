package in.co.greenwave.entity;

import java.util.Date; // Importing the Date class to handle date and time

/**
 * This class represents a User entity in the system.
 * 
 * <p>
 * A UserEntity contains information about a user, including their personal details,
 * account status, and timestamps for when the account was created or modified.
 * </p>
 */
public class UserEntity {
	// Unique identifier for the user
	private String userId;

	// The user's name
	private String userName;

	// The user's password for account access
	private String password;

	// The user's phone number
	private long phoneNumber;

	// The URL of the user's home page or profile
	private String homePage;

	// Indicates whether the user's account is active (true) or inactive (false)
	private boolean active;

	// The date and time when the user account was created
	private Date createdOn;

	// The date and time when the user account was last modified
	private Date modifiedOn;

	// The identifier of the user who created this account
	private String createdBy;

	// The identifier of the user who last modified this account
	private String modifiedBy;

	// Identifier for the tenant (user group) associated with this user
	private String tenantId;

	// Default constructor: Creates an empty UserEntity object.
	public UserEntity() {
	}

	/**
	 * Constructor to create a UserEntity object with all its details.
	 *
	 * @param userId     Unique identifier for the user
	 * @param userName   The user's name
	 * @param password   The user's password for account access
	 * @param phoneNumber The user's phone number
	 * @param homePage   The URL of the user's home page
	 * @param active     Indicates whether the account is active
	 * @param createdOn  The date and time when the account was created
	 * @param modifiedOn The date and time when the account was last modified
	 * @param createdBy  The identifier of the user who created this account
	 * @param modifiedBy The identifier of the user who last modified this account
	 * @param tenantId   Identifier for the tenant associated with this user
	 */
	public UserEntity(String userId, String userName, String password, Long phoneNumber, String homePage,
			boolean active, Date createdOn, Date modifiedOn, String createdBy, String modifiedBy, String tenantId) {
		super();
		this.userId = userId; // Set the user ID
		this.userName = userName; // Set the user's name
		this.password = password; // Set the user's password
		this.phoneNumber = phoneNumber; // Set the user's phone number
		this.homePage = homePage; // Set the user's home page URL
		this.active = active; // Set whether the account is active
		this.createdOn = createdOn; // Set the creation date
		this.modifiedOn = modifiedOn; // Set the modification date
		this.createdBy = createdBy; // Set the creator's user ID
		this.modifiedBy = modifiedBy; // Set the modifier's user ID
		this.tenantId = tenantId; // Set the tenant ID
	}

	// Getters and Setters to get and change the values of each field of UserEntity object.
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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

	public long getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(Long long1) {
		this.phoneNumber = long1;
	}

	public String getHomePage() {
		return homePage;
	}

	public void setHomePage(String homePage) {
		this.homePage = homePage;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public Date getModifiedOn() {
		return modifiedOn;
	}

	public void setModifiedOn(Date modifiedOn) {
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

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	/**
	 * Returns a string representation of the UserEntity object.
	 * This is useful for debugging and displaying information.
	 */
	@Override
	public String toString() {
		return "UserEntity [userId=" + userId + ", userName=" + userName + ", password=" + password + ", phoneNumber="
				+ phoneNumber + ", homePage=" + homePage + ", active=" + active + ", createdOn=" + createdOn
				+ ", modifiedOn=" + modifiedOn + ", createdBy=" + createdBy + ", modifiedBy=" + modifiedBy
				+ ", tenantId=" + tenantId + "]";
	}

}
