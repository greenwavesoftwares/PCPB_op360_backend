package in.co.greenwave.entity;

/**
 * The CellEditHistory class represents the history of changes made to a specific cell in a transaction.
 * <p>
 * This class is used to store the details of any edits made to a cell, including the cell's ID and name, 
 * the transaction ID associated with the edit, the time when the change was made, the new value, the reason for the change,
 * and the ID of the user who made the edit. It allows tracking of the changes and ensures accountability 
 * by recording the reason and the user responsible for the edit.
 * </p>
 */
public class CellEditHistory {

	// The unique identifier for the cell that was edited.
	private String cellId;

	// The name of the cell that was edited.
	private String cellName;

	// The ID of the transaction associated with the cell edit.
	private String transactionId;

	// The timestamp representing when the edit was made.
	private String timestamp;

	// The new value of the cell after the edit.
	private String value;

	// The reason why the edit was made to the cell.
	private String reason;

	// The user ID of the person who made the edit.
	private String userid;

	// Default constructor: Creates an empty CellEditHistory object.
	public CellEditHistory() {}

	/**
	 * Constructor to create a CellEditHistory object with all details.
	 * 
	 * @param cellId the unique ID of the cell
	 * @param cellName the name of the cell
	 * @param transactionId the ID of the transaction related to the edit
	 * @param timestamp the time when the edit occurred
	 * @param value the new value of the cell
	 * @param reason the reason for the edit
	 * @param userid the ID of the user who made the edit
	 */
	public CellEditHistory(String cellId, String cellName, String transactionId, String timestamp, String value,
			String reason, String userid) {
		super();
		this.cellId = cellId;
		this.cellName = cellName;
		this.transactionId = transactionId;
		this.timestamp = timestamp;
		this.value = value;
		this.reason = reason;
		this.userid = userid;
	}

	// A method to represent the object as a string, useful for logging or debugging.
	@Override
	public String toString() {
		return "CellEditHistory [cellId=" + cellId + ", cellName=" + cellName + ", transactionId=" + transactionId
				+ ", timestamp=" + timestamp + ", value=" + value + ", reason=" + reason + ", userid=" + userid + "]";
	}

	// Getters and Setters to get and change the values of each field of CellEditHistory object.
	public String getCellId() {
		return cellId;
	}
	public void setCellId(String cellId) {
		this.cellId = cellId;
	}
	public String getCellName() {
		return cellName;
	}
	public void setCellName(String cellName) {
		this.cellName = cellName;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}


}
