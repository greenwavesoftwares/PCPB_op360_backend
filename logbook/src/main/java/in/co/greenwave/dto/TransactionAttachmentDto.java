package in.co.greenwave.dto;

import java.util.Arrays;

public class TransactionAttachmentDto {
	private String transactionId;
	private String jobId;
	private String activityId;
	private String formName;
	private int formversion;
	private String cellId;
	private String fileName;
	private byte[] fileContent;
	private String tenantid;
	
	public TransactionAttachmentDto(String transactionId, String jobId, String activityId, String formName,
			int formversion, String cellId, String fileName, byte[] fileContent, String tenantid) {
		super();
		this.transactionId = transactionId;
		this.jobId = jobId;
		this.activityId = activityId;
		this.formName = formName;
		this.formversion = formversion;
		this.cellId = cellId;
		this.fileName = fileName;
		this.fileContent = fileContent;
		this.tenantid = tenantid;
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

	public int getFormversion() {
		return formversion;
	}

	public void setFormversion(int formversion) {
		this.formversion = formversion;
	}

	public String getCellId() {
		return cellId;
	}

	public void setCellId(String cellId) {
		this.cellId = cellId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public byte[] getFileContent() {
		return fileContent;
	}

	public void setFileContent(byte[] fileContent) {
		this.fileContent = fileContent;
	}

	public String getTenantid() {
		return tenantid;
	}

	public void setTenantid(String tenantid) {
		this.tenantid = tenantid;
	}

	@Override
	public String toString() {
		return "TransactionAttachmentDto [transactionId=" + transactionId + ", jobId=" + jobId + ", activityId="
				+ activityId + ", formName=" + formName + ", formversion=" + formversion + ", cellId=" + cellId
				+ ", fileName=" + fileName + ", fileContent=" + Arrays.toString(fileContent) + ", tenantid=" + tenantid
				+ "]";
	}
}
