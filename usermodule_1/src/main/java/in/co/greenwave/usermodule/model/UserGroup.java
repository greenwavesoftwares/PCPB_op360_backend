package in.co.greenwave.usermodule.model;

import java.math.BigInteger;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
@JsonPropertyOrder(alphabetic = true)
public class UserGroup {


    /**
     * The unique identifier for the user group.
     */
    private String groupID;

    /**
     * The name of the user group.
     */
    private String groupName;

    /**
     * The phone number associated with the group, stored as a BigInteger 
     * to accommodate large numbers without formatting constraints.
     */
    private BigInteger phone;

    /**
     * The URL of the group's homepage or main page.
     */
    private String homePage;

    /**
     * Indicates whether the group is active or inactive.
     * True if active, false otherwise.
     */
    private boolean active;

    /**
     * The identifier of the tenant to which the group belongs.
     */
    private String tenantId;



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
	public UserGroup(String groupID, String groupName, BigInteger phone, String homePage, boolean active,
			String tenantId) {
		super();
		this.groupID = groupID;
		this.groupName = groupName;
		this.phone = phone;
		this.homePage = homePage;
		this.active = active;
		this.tenantId = tenantId;
	}
	public UserGroup() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "UserGroup [groupID=" + groupID + ", groupName=" + groupName + ", phone=" + phone + ", homePage="
				+ homePage + ", active=" + active + ", tenantId=" + tenantId + "]";
	}
	


	


}
