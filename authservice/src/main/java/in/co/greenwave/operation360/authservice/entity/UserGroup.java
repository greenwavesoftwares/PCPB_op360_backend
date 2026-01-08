package in.co.greenwave.operation360.authservice.entity;

import java.math.BigInteger;
/**
 * The UserGroup class represents a group or role within the system.
 * It includes information such as group ID, group name, contact phone number, 
 * the homepage assigned to the group, its active status, and tenant ID.
 */
public class UserGroup {

	// Unique identifier for the group
	private String groupID;

	// Name of the group or role
	private String groupName;

	// Contact phone number for the group (using BigInteger for large phone numbers)
	private BigInteger phone;

	// Home page URL assigned to the group
	private String homePage;

	// Indicates whether the group is currently active or not
	private boolean active;

	// The tenant ID associated with this group
	private String tenantId;

	/**
	 * Constructor to initialize all fields of the UserGroup class.
	 * 
	 * @param groupID   The unique ID for the group.
	 * @param groupName The name of the group.
	 * @param phone     The contact phone number for the group.
	 * @param homePage  The home page URL assigned to the group.
	 * @param active    Whether the group is currently active.
	 * @param tenantId  The tenant ID.
	 */
	public UserGroup(String groupID, String groupName, BigInteger phone, String homePage, boolean active, String tenantId) {
		super();
		this.groupID = groupID;
		this.groupName = groupName;
		this.phone = phone;
		this.homePage = homePage;
		this.active = active;
		this.tenantId = tenantId;
	}

	/**
	 * Default constructor for creating an empty UserGroup object.
	 */
	public UserGroup() {
		super();
	}

	// Getters and Setters

	public String getGroupID() {
		return groupID;
	}
	public void setGroupID(String groupID) {
		this.groupID = groupID;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public BigInteger getPhone() {
		return phone;
	}
	public void setPhone(BigInteger phone) {
		this.phone = phone;
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
	public String getTenantId() {
		return tenantId;
	}
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	/**
	 * Provides a string representation of the UserGroup object.
	 * This method helps in easily printing the group details.
	 * 
	 * @return A string showing the group ID, name, phone, homepage, active status, and tenant ID.
	 */
	@Override
	public String toString() {
		return "UserGroup [groupID=" + groupID + ", groupName=" + groupName + ", phone=" + phone + ", homePage="
				+ homePage + ", active=" + active + ", tenantId=" + tenantId + "]";
	}

}
