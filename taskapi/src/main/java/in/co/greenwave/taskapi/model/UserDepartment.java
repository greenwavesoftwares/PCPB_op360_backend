package in.co.greenwave.taskapi.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(alphabetic=true)
public class UserDepartment {
	private String deptID;
	private String deptName;
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
	public UserDepartment(String deptID, String deptName) {
		super();
		this.deptID = deptID;
		this.deptName = deptName;
	}
	
	
}
