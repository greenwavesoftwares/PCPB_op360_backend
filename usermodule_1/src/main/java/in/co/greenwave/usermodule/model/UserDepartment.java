package in.co.greenwave.usermodule.model;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
@JsonPropertyOrder(alphabetic = true)
public class UserDepartment {
    /**
     * The unique identifier for the department.
     */
    private String deptID;

    /**
     * The name of the department.
     */
    private String deptName;

    /**
     * The identifier of the tenant to which the department belongs.
     */
    private String tenantId;

	public UserDepartment(String deptID, String deptName, String tenantId) {
		super();
		this.deptID = deptID;
		this.deptName = deptName;
		this.tenantId = tenantId;
	}
	public UserDepartment() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getDeptID() {
		return deptID;
	}
	public void setDeptID(String deptID) {
		this.deptID = deptID;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getTenantId() {
		return tenantId;
	}
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
	@Override
	public String toString() {
		return "UserDepartment [deptID=" + deptID + ", deptName=" + deptName + ", tenantId=" + tenantId + "]";
	}


	
}
