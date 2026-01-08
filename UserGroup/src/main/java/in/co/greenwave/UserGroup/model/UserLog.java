package in.co.greenwave.UserGroup.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
//only shows not null parameters in the json
@JsonInclude(JsonInclude.Include.NON_NULL)
//lexicographically arranges the alphabets
@JsonPropertyOrder(alphabetic=true)
public class UserLog {
	
	// Transaction ID: A unique identifier for the transaction.
	private String transaction_id;

	// Timestamp: The date and time when the transaction occurred or was recorded.
	private String timestamp;

	// ID: A general identifier associated with the record or entity (could refer to a specific object or entity).
	private String id;

	// Data From: The source or origin of the data, indicating where the data came from.
	private String data_from;

	// Modified By: The identifier of the user who made changes to the data.
	private String modified_by;

	// Data: The actual content or payload of the transaction, representing the data being processed or transferred.
	private String data;

	// Remarks: Additional comments or notes related to the transaction, often for further explanation or context.
	private String remarks;

	// User Details: The user details before the modification (could contain information like name, role, etc.).
	private User userDetails;

	// New User Details: The user details after the modification, showing the updated information.
	private User newUserDetails;

	
	public UserLog(String transaction_id, String timestamp, String id, String data_from, String modified_by,
			String data, String remarks) {
		super();
		this.transaction_id = transaction_id;
		this.timestamp = timestamp;
		this.id = id;
		this.data_from = data_from;
		this.modified_by = modified_by;
		this.data = data;
		this.remarks = remarks;
	}
	public String getTransaction_id() {
		return transaction_id;
	}
	public void setTransaction_id(String transaction_id) {
		this.transaction_id = transaction_id;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getData_from() {
		return data_from;
	}
	public void setData_from(String data_from) {
		this.data_from = data_from;
	}
	public String getModified_by() {
		return modified_by;
	}
	public void setModified_by(String modified_by) {
		this.modified_by = modified_by;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public User getUserDetails() {
		return userDetails;
	}
	public void setUserDetails(User userDetails) {
		this.userDetails = userDetails;
	}
	public User getNewUserDetails() {
		return newUserDetails;
	}
	public void setNewUserDetails(User newUserDetails) {
		this.newUserDetails = newUserDetails;
	}
	
}
