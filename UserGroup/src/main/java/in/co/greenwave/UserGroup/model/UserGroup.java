package in.co.greenwave.UserGroup.model;

import java.math.BigInteger;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
//only shows not null parameters in the json
@JsonInclude(JsonInclude.Include.NON_NULL)
//lexicographically arranges the alphabets
@JsonPropertyOrder(alphabetic=true)
public class UserGroup {


	// Group ID: A unique identifier for the group.
	private String groupID;

	// Group Name: The name of the group.
	private String groupName;

	// Phone: The contact phone number associated with the group, stored as a BigInteger to handle large numbers.
	private BigInteger phone;

	// Home Page: The URL of the group's homepage or website.
	private String homePage;

	// Active: A flag indicating whether the group is currently active (true) or inactive (false).
	private boolean active;



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


	
	public UserGroup() {
		super();
		// TODO Auto-generated constructor stub
	}
	public UserGroup(String groupID, String groupName, BigInteger phone, String homePage, boolean active) {
		super();
		this.groupID = groupID;
		this.groupName = groupName;
		this.phone = phone;
		this.homePage = homePage;
		this.active = active;
	}
	@Override
	public String toString() {
		return "UserGroup [groupID=" + groupID + ", groupName=" + groupName + ", phone=" + phone + ", homePage="
				+ homePage + ", active=" + active + "]";
	}




}