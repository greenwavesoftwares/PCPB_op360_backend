package in.co.greenwave.taskapi.model;

import java.math.BigInteger;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
@JsonPropertyOrder(alphabetic = true)
@JsonInclude(JsonInclude.Include.NON_NULL)

public class UserGroup {


	private String groupID;
	private String groupName;
	private BigInteger phone;
	private String homePage;
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


	public UserGroup(String groupID, String groupName, BigInteger phone, String homePage, boolean active) {
		super();
		this.groupID = groupID;
		this.groupName = groupName;
		this.phone = phone;
		this.homePage = homePage;
		this.active = active;
	}
	public UserGroup() {
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "UserGroup [groupID=" + groupID + ", groupName=" + groupName + ", phone=" + phone + ", homePage="
				+ homePage + ", active=" + active + "]";
	}

}
