package in.co.greenwave.entity;

import java.sql.Timestamp; // Importing the Timestamp class to handle date and time
import java.util.Map; // Importing the Map class to use key-value pairs

import com.google.gson.Gson; // Importing Gson to convert objects to JSON format

import in.co.greenwave.dto.TransactionDto; // Importing the TransactionDto class for data transfer

/**
 * This class represents a Transaction entity.
 * 
 * <p>
 * A Transaction contains details about a transaction of a @Logbook in the system, 
 * such as job details, user information, and timestamp. It also includes 
 * logbook data and remarks associated with the transaction.
 * </p>
 */
public class Transaction {
	// Unique identifier for the transaction
	private String transactionId;

	// Serial number of the transaction
	private long slNo;

	// Unique identifier for the job associated with this transaction
	private String jobId;

	// Unique identifier for the activity associated with this transaction
	private String activityId;

	// The name of the form related to this transaction
	private String formName;

	// The version number of the form used in this transaction
	private int version;

	// Data related to the logbook, stored as a JSON string
	private String logbookData;

	// Remarks or comments provided during the transaction
	private String transactionRemarks;

	// Timestamp indicating when the transaction occurred
	private Timestamp timestamp;

	// Unique identifier for the user who performed the transaction
	private String userId;

	// The role of the user (e.g., admin, user) in the transaction
	private String role;

	// Identifier for the tenant (user group) associated with this transaction
	private String tenantId;

	// Default constructor: Creates an empty Transaction object.
	public Transaction() {
	}

	/**
	 * Constructor to create a Transaction object with all its details.
	 *
	 * @param slNo               Serial number of the transaction
	 * @param transactionId      Unique identifier for the transaction
	 * @param jobId              Unique identifier for the job
	 * @param activityId         Unique identifier for the activity
	 * @param formName           Name of the form related to this transaction
	 * @param version            Version number of the form
	 * @param logbookData        Data related to the logbook (in JSON format)
	 * @param transactionRemarks  Remarks provided during the transaction
	 * @param timestamp          Timestamp of when the transaction occurred
	 * @param userId             Unique identifier for the user
	 * @param role               Role of the user in the transaction
	 * @param tenantId           Identifier for the tenant (user group)
	 */
	public Transaction(long slNo, String transactionId, String jobId, String activityId, String formName,
			int version, String logbookData, String transactionRemarks, Timestamp timestamp, String userId,
			String role, String tenantId) {
		super();
		this.slNo = slNo; // Set the serial number
		this.transactionId = transactionId; // Set the transaction ID
		this.jobId = jobId; // Set the job ID
		this.activityId = activityId; // Set the activity ID
		this.formName = formName; // Set the form name
		this.version = version; // Set the form version
		this.logbookData = logbookData; // Set the logbook data
		this.transactionRemarks = transactionRemarks; // Set the transaction remarks
		this.timestamp = timestamp; // Set the timestamp
		this.userId = userId; // Set the user ID
		this.role = role; // Set the user's role
		this.tenantId = tenantId; // Set the tenant ID
	}

	/**
	 * Constructor to create a Transaction object from a TransactionDto.
	 *
	 * @param dto The TransactionDto object containing transaction data
	 */
	public Transaction(TransactionDto dto) {
		// Initialize the Transaction fields from the DTO
		this.transactionId = dto.getTransactionId(); // Set the transaction ID from the DTO
		this.jobId = dto.getJobId(); // Set the job ID from the DTO
		this.activityId = dto.getActivityId(); // Set the activity ID from the DTO
		this.formName = dto.getFormName(); // Set the form name from the DTO
		this.version = dto.getFormversion(); // Set the form version from the DTO

		// Get logbook data from the DTO and convert it to a JSON string
		Map<String, Object> logBookMap = dto.getLogbookData(); // Get logbook data as a map
		Gson gson = new Gson(); // Create a Gson object to handle JSON conversion
		String logBookData = gson.toJson(logBookMap); // Convert the map to a JSON string
		this.logbookData = logBookData; // Set the logbook data

		// Set the remaining fields from the DTO
		this.transactionRemarks = dto.getUserRemarks(); // Set the transaction remarks
		this.timestamp = dto.getTransactionTimestamp(); // Set the timestamp of the transaction
		this.userId = dto.getUserId(); // Set the user ID
		this.role = dto.getRole(); // Set the user's role
		this.tenantId = dto.getTenantid(); // Set the tenant ID
	}

	// Getters and Setters to get and change the values of each field of Transaction object.
	public long getSlno() {
		return slNo;
	}

	public void setSlno(long slno) {
		this.slNo = slno;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public String getFormName() {
		return formName;
	}

	public void setFormName(String formName) {
		this.formName = formName;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getLogbookData() {
		return logbookData;
	}

	public void setLogbookData(String logbookData) {
		this.logbookData = logbookData;
	}

	public String getTransactionRemarks() {
		return transactionRemarks;
	}

	public void setTransactionRemarks(String transactionRemarks) {
		this.transactionRemarks = transactionRemarks;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}


	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	/**
	 * Returns a string representation of the Transaction object.
	 * This is useful for debugging and displaying information.
	 */
	@Override
	public String toString() {
		return "Transaction [transactionId=" + transactionId + ", slNo=" + slNo + ", jobId=" + jobId + ", activityId="
				+ activityId + ", formName=" + formName + ", version=" + version + ", logbookData=" + logbookData
				+ ", transactionRemarks=" + transactionRemarks + ", timestamp=" + timestamp + ", userId=" + userId
				+ ", role=" + role + ", tenantId=" + tenantId + "]";
	}



}
