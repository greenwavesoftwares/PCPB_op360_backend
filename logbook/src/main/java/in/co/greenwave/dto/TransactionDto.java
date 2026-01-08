package in.co.greenwave.dto;

import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import in.co.greenwave.entity.Transaction;

/**
 * a transaction DTO containing transaction information @Transaction
 */
public class TransactionDto {
	private String transactionId;
	private String jobId;
	private String activityId;
	private String formName;
	private int formversion;
	private String tenantid;
	private Map<String, Object> logbookData = new LinkedHashMap<String, Object>();
//	private String remarks;
	private String userId;
	private String role;
	private Timestamp transactionTimestamp;
//	private String cellAliasId;
	private String userRemarks;
	
	
	public static class TrData {
		private String value = null;
		private String remarks = "NA";
		private boolean disabled = false;

		public TrData() {
		}

		public TrData(String value, String remarks, boolean disabled) {
			super();
			this.value = value;
			this.remarks = remarks;
			this.disabled = disabled;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		public String getRemarks() {
			return remarks;
		}

		public void setRemarks(String remarks) {
			this.remarks = remarks;
		}

		public boolean isDisabled() {
			return disabled;
		}

		public void setDisabled(boolean disabled) {
			this.disabled = disabled;
		}
	}
	
public TransactionDto(String transactionId, String jobId, String activityId, String formName, int formversion,
			String tenantid, Map<String, Object> logbookData, String userId, String role,
			Timestamp transactionTimestamp, String userRemarks) {
		super();
		this.transactionId = transactionId;
		this.jobId = jobId;
		this.activityId = activityId;
		this.formName = formName;
		this.formversion = formversion;
		this.tenantid = tenantid;
		this.logbookData = logbookData;
		this.userId = userId;
		this.role = role;
		this.transactionTimestamp = transactionTimestamp;
		this.userRemarks = userRemarks;
	}

	public TransactionDto() {
	}

	
	
	public TransactionDto(String transactionId, String jobId, String activityId, String formName, int formversion,
			Map<String, Object> jsonInfo,String userId, String role, Timestamp transactionTimestamp,
			String cellAliasId, String userRemarks,Map<String,Object> logbookData) {
		super();
		this.transactionId = transactionId;
		this.jobId = jobId;
		this.activityId = activityId;
		this.formName = formName;
		this.formversion = formversion;
//		this.jsonInfo = jsonInfo;
//		this.remarks = userRemarks;
		this.userId = userId;
		this.role = role;
		this.transactionTimestamp = transactionTimestamp;
//		this.cellAliasId = cellAliasId;
		this.userRemarks = userRemarks;
		this.logbookData = logbookData;
	}

	
	
	
	
	
	public TransactionDto(Transaction transaction) {
	    this.transactionId = transaction.getTransactionId();
	    this.jobId = transaction.getJobId();
	    this.activityId = transaction.getActivityId();
	    this.formName = transaction.getFormName();
	    this.formversion = transaction.getVersion();
	    this.tenantid = transaction.getTenantId();
	    
	    // Assuming the logbookData is stored as JSON in the entity and needs to be parsed
	    ObjectMapper objectMapper = new ObjectMapper();
	    try {
	        this.logbookData = objectMapper.readValue(transaction.getLogbookData(), LinkedHashMap.class);
	    } catch (JsonProcessingException e) {
	        e.printStackTrace();
	        // handle the exception as per your requirements
	    }

	    this.userId = transaction.getUserId();
	    this.role = transaction.getRole();
	    this.transactionTimestamp = transaction.getTimestamp();
	    this.userRemarks = transaction.getTransactionRemarks();
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

	public Map<String,Object> getLogbookData() {
		return logbookData;
	}

	public void setLogbookData(Map<String,Object> logbookData) {
		
		this.logbookData = logbookData;
	}

//	public String getRemarks() {
//		return remarks;
//	}
//
//	public void setRemarks(String remarks) {
//		this.remarks = remarks;
//	}

	public int getFormversion() {
		return formversion;
	}

	public void setFormversion(int formversion) {
		this.formversion = formversion;
	}

//	public Map<String, Object> getJsonInfo() {
//		return jsonInfo;
//	}
//
//	public void setJsonInfo(Map<String, Object> jsonInfo) {
//		this.jsonInfo = jsonInfo;
//	}

	public Timestamp getTransactionTimestamp() {
		return transactionTimestamp;
	}

	public void setTransactionTimestamp(Timestamp transactionTimestamp) {
		this.transactionTimestamp = transactionTimestamp;
	}

//	public String getCellAliasId() {
//		return cellAliasId;
//	}
//
//	public void setCellAliasId(String cellAliasId) {
//		this.cellAliasId = cellAliasId;
//	}

	public String getUserRemarks() {
		return userRemarks;
	}

	public void setUserRemarks(String userRemarks) {
		this.userRemarks = userRemarks;
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



	public String getTenantid() {
		return tenantid;
	}



	public void setTenantid(String tenantid) {
		this.tenantid = tenantid;
	}

	
	@Override
	public String toString() {
		return "TransactionDto [transactionId=" + transactionId + ", jobId=" + jobId + ", activityId=" + activityId
				+ ", formName=" + formName + ", formversion=" + formversion + ", tenantid=" + tenantid
				+ ", logbookData=" + logbookData + ", userId=" + userId + ", role=" + role + ", transactionTimestamp="
				+ transactionTimestamp + ", userRemarks=" + userRemarks + "]";
	}


}
