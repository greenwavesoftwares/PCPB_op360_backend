package in.co.greenwave.usermodule.model;

public class UserLog {
	
    /**
     * The unique identifier for the transaction.
     */
    private String transaction_id;

    /**
     * The timestamp of the transaction, indicating when it occurred.
     */
    private String timestamp;

    /**
     * the user whose information is getting modified
     */
    private String id;

    /**
     * The source or origin of the data in the transaction.
     */
    private String data_from;

    /**
     * The identifier of the user who last modified the transaction data.
     */
    private String modified_by;

    /**
     * The main data payload of the transaction.
     */
    private String data;

    /**
     * Any additional remarks or comments associated with the transaction.
     */
    private String remarks;

    /**
     * The details of the user before modifications.
     */
    private User userDetails;

    /**
     * The details of the user after modifications.
     */
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
