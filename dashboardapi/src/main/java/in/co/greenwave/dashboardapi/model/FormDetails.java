package in.co.greenwave.dashboardapi.model;


import java.util.List;

public class FormDetails {
	
	private int formID;
	private String formName;
	private String tableSQL;
	private String insertSql;
	private String deleteSQL;
	private String createdUser;
	private String documentId;
	private String department;
	private String selectedGrp;
	private String formatID;
	private int versionNumber;
	private Boolean isActiveForm = false;
	
	public FormDetails() {
		
	}
	
	public FormDetails(int formID, String formName, String tableSQL, String insertSql, String deleteSQL, String documentId,
			String department,String selectedGrp, String formatID, int vrsnNumb) {
		super();
		this.formID = formID;
		this.formName = formName;
		this.tableSQL = tableSQL;
		this.insertSql = insertSql;
		this.deleteSQL = deleteSQL;
		this.documentId = documentId;
		this.department = department;
		this.formatID = formatID;
		this.versionNumber = vrsnNumb;
		this.selectedGrp = selectedGrp;
	}
	
	public FormDetails( int formID, String formName, int versionNumber, String documentId,
			String department, String selectedGrp,String formatID, String createdUser, Boolean isActiveForm,
			String insertSql, String deleteSQL, String tableSQL) {
		super();
		this.formID = formID;
		this.formName = formName;
		this.versionNumber = versionNumber;
		this.documentId = documentId;
		this.department = department;
		this.formatID = formatID;
		this.createdUser = createdUser;
		this.isActiveForm = isActiveForm;
		this.selectedGrp = selectedGrp;
		this.insertSql = insertSql;
		this.deleteSQL = deleteSQL;
		this.tableSQL = tableSQL;
	}
	
	public FormDetails( int formID, String formName, int versionNumber, String documentId,
			String department, String selectedGrp,String formatID, Boolean isActiveForm) {
		super();
		this.formID = formID;
		this.formName = formName;
		this.versionNumber = versionNumber;
		this.documentId = documentId;
		this.department = department;
		this.formatID = formatID;
		this.isActiveForm = isActiveForm;
		this.selectedGrp = selectedGrp;
	}
	
	public FormDetails(String formName, String tableSQL, String insertSql,String deleteSQL) {
		super();
		this.formName = formName;
		this.tableSQL = tableSQL;
		this.insertSql = insertSql;
		this.deleteSQL = deleteSQL;
	}

	public String getFormName() {
		return formName;
	}
	public void setFormName(String formName) {
		this.formName = formName;
	}
	public String getTableSQL() {
		return tableSQL;
	}
	public void setTableSQL(String tableSQL) {
		this.tableSQL = tableSQL;
	}
	public String getInsertSql() {
		return insertSql;
	}
	public void setInsertSql(String insertSql) {
		this.insertSql = insertSql;
	}
	public String getDeleteSQL() {
		return deleteSQL;
	}
	public void setDeleteSQL(String deleteSQL) {
		this.deleteSQL = deleteSQL;
	}
	public String getDocumentId() {
		return documentId;
	}
	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getFormatID() {
		return formatID;
	}
	public void setFormatID(String formatID) {
		this.formatID = formatID;
	}

	public int getVersionNumber() {
		return versionNumber;
	}

	public void setVersionNumber(int versionNumber) {
		this.versionNumber = versionNumber;
	}

	public int getFormID() {
		return formID;
	}

	public void setFormID(int formID) {
		this.formID = formID;
	}
	public Boolean getIsActiveForm() {
		return isActiveForm;
	}
	public void setIsActiveForm(Boolean isActiveForm) {
		this.isActiveForm = isActiveForm;
	}
	public String getSelectedGrp() {
		return selectedGrp;
	}
	public void setSelectedGrp(String selectedGrp) {
		this.selectedGrp = selectedGrp;
	}
	public String getCreatedUser() {
		return createdUser;
	}
	public void setCreatedUser(String createdUser) {
		this.createdUser = createdUser;
	}

	@Override
	public String toString() {
		return "FormDetails [formID=" + formID + ", formName=" + formName + ", tableSQL=" + tableSQL + ", insertSql="
				+ insertSql + ", deleteSQL=" + deleteSQL + ", createdUser=" + createdUser + ", documentId=" + documentId
				+ ", department=" + department + ", selectedGrp=" + selectedGrp + ", formatID=" + formatID
				+ ", versionNumber=" + versionNumber + ", isActiveForm=" + isActiveForm + "]";
	}
	
}
