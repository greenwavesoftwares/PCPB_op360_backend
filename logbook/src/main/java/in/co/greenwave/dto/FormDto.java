package in.co.greenwave.dto;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import in.co.greenwave.entity.FormInfo;

/**
 * Data Transfer Object (DTO) for transferring form information between layers of the application.
 * <p>
 * This DTO represents details of a form, such as SQL queries, document info, and other metadata.
 * It helps move form data between various parts of the application, like between the database layer
 * and the service layer or from the service layer to the user interface.
 * </p>
 * <p>
 * This class uses the {@link FormInfo} entity to initialize its fields and can also be directly created with specific data.
 * </p>
 * 
 * <p>
 * Example Usage:
 * <pre>
 * FormDto formDto = new FormDto();
 * formDto.setFormName("Sample Form");
 * formDto.setUserId("user123");
 * // Add more properties as needed
 * </pre>
 * </p>
 * 
 * @see FormInfo
 */
public class FormDto {

	// The unique ID for the form.
	private int formID;

	// The name of the form.
	private String formName;

	// SQL query used to create a table in the database for this form.
	private String tableSQL;

	// SQL query used to insert data into the form.
	private String insertSQL;

	// SQL query used to delete the form's data from the database.
	private String deleteSQL;

	// The username of the person who created this form.
	private String createdUser;

	// The document ID associated with this form.
	private String documentId;

	// The department responsible for the form.
	private String department;

	// The selected group (if any) for this form.
	private String selectedGrp;

	// The format ID used for the form, indicating its structure or layout.
	private String formatID;

	// The version number of the form.
	private int versionNumber;

	// Indicates if the form is active (true) or not (false).
	private boolean isActiveForm = false;

	// The ID of the user who created or is responsible for this form.
	private String userId;

	// The timestamp indicating when the form was created.
	private Timestamp creationDate;

	// The user group associated with the form.
	private String userGroup;

	// SQL query used to save data in the form.
	private String saveSQL;

	// The tenant ID related to multi-tenant environments.
	private String tenantId;

	//added by ashok
	private List<CellDto> CellInfo = new ArrayList<>();

	//Added By Ashok
	private boolean isPublicAccess = false;

	//Added By Ashok
	private String modifiedBy;
	//added by ashok
	private Timestamp modifiedDate;

//	private boolean dashboardType;

	// Default constructor: Creates an empty FormDto instance.
	public FormDto() {
	}

	// Constructor to initialize the FormDto using the FormInfo entity.
	public FormDto(FormInfo formInfo) {
		this.formID = formInfo.getFormId();
		this.userId = formInfo.getUserId();
		this.formName = formInfo.getFormName();
		this.tableSQL = formInfo.getTableSQL();
		this.deleteSQL = formInfo.getDeleteSQL();
		this.createdUser = formInfo.getCreatedUser();
		this.documentId = formInfo.getDocumentId();
		this.department = formInfo.getDepartment();
		this.formatID = formInfo.getFormatId();
		this.versionNumber = formInfo.getVersionNumber();
		this.isActiveForm = formInfo.getIsActiveForm();
		this.userGroup = formInfo.getUserGroup();
		this.isPublicAccess = formInfo.isPublicAccess();
		this.saveSQL=formInfo.getSaveSQL();
		this.tenantId = formInfo.getTenantId();
	}

	// Constructor to create a FormDto directly with all necessary fields.
	public FormDto(int formID, String formName, String tableSQL, String insertSQL, String deleteSQL, String createdUser,
			String documentId, String department, String selectedGrp, String formatID, int versionNumber,
			boolean isActiveForm, String userId, Timestamp creationDate, String userGroup, String saveSQL) {
		super(); // Calls the parent class constructor (Object class by default).
		this.formID = formID;
		this.formName = formName;
		this.tableSQL = tableSQL;
		this.insertSQL = insertSQL;
		this.deleteSQL = deleteSQL;
		this.createdUser = createdUser;
		this.documentId = documentId;
		this.department = department;
		this.selectedGrp = selectedGrp;
		this.formatID = formatID;
		this.versionNumber = versionNumber;
		this.isActiveForm = isActiveForm;
		this.userId = userId;
		this.creationDate = creationDate;
		this.userGroup = userGroup;
		this.saveSQL = saveSQL;
	}

	//added by ashok

	public FormDto(int formID, String formName, String tableSQL, String insertSQL, String deleteSQL, String createdUser,
			String documentId, String department, String selectedGrp, String formatID, int versionNumber,
			boolean isActiveForm, String userId, Timestamp creationDate, String userGroup, String saveSQL,
			String tenantId, List<CellDto> cellInfo) {
		super();
		this.formID = formID;
		this.formName = formName;
		this.tableSQL = tableSQL;
		this.insertSQL = insertSQL;
		this.deleteSQL = deleteSQL;
		this.createdUser = createdUser;
		this.documentId = documentId;
		this.department = department;
		this.selectedGrp = selectedGrp;
		this.formatID = formatID;
		this.versionNumber = versionNumber;
		this.isActiveForm = isActiveForm;
		this.userId = userId;
		this.creationDate = creationDate;
		this.userGroup = userGroup;
		this.saveSQL = saveSQL;
		this.tenantId = tenantId;
		CellInfo = cellInfo;
	}

	public FormDto(int formID, String formName, String tableSQL, String insertSQL, String deleteSQL, String createdUser,
			String documentId, String department, String selectedGrp, String formatID, int versionNumber,
			boolean isActiveForm, String userId, Timestamp creationDate, String userGroup, String saveSQL,
			String tenantId, List<CellDto> cellInfo, boolean isPublicAccess, String modifiedBy, Timestamp modifiedDate
//			, boolean dashboardType
			) {
		super();
		this.formID = formID;
		this.formName = formName;
		this.tableSQL = tableSQL;
		this.insertSQL = insertSQL;
		this.deleteSQL = deleteSQL;
		this.createdUser = createdUser;
		this.documentId = documentId;
		this.department = department;
		this.selectedGrp = selectedGrp;
		this.formatID = formatID;
		this.versionNumber = versionNumber;
		this.isActiveForm = isActiveForm;
		this.userId = userId;
		this.creationDate = creationDate;
		this.userGroup = userGroup;
		this.saveSQL = saveSQL;
		this.tenantId = tenantId;
		CellInfo = cellInfo;
		this.isPublicAccess = isPublicAccess;
		this.modifiedBy = modifiedBy;
		this.modifiedDate = modifiedDate;
//		this.dashboardType = dashboardType;
	}

	public List<CellDto> getCellInfo() {
		return CellInfo;
	}
	public FormDto(List<CellDto> tabCells) {
		this.CellInfo = tabCells;
	}

	public void setCellInfo(List<CellDto> cellInfo) {
		CellInfo = cellInfo;
	}

	public void setActiveForm(boolean isActiveForm) {
		this.isActiveForm = isActiveForm;
	}

	// Getter and Setter methods for each field.
	public int getFormID() {
		return formID;
	}

	public void setFormID(int formID) {
		this.formID = formID;
	}

	public String getFormName() {
		return formName;
	}

	public void setFormName(String formName) {
		this.formName = formName;
	}

	public String getSaveSQL() {
		return saveSQL;
	}

	public void setSaveSQL(String saveSQL) {
		this.saveSQL = saveSQL;
	}

	public String getTableSQL() {
		return tableSQL;
	}

	public void setTableSQL(String tableSQL) {
		this.tableSQL = tableSQL;
	}

	public String getDeleteSQL() {
		return deleteSQL;
	}

	public void setDeleteSQL(String deleteSQL) {
		this.deleteSQL = deleteSQL;
	}

	public String getInsertSQL() {
		return insertSQL;
	}

	public void setInsertSQL(String insertSQL) {
		this.insertSQL = insertSQL;
	}

	public Timestamp getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Timestamp creationDate) {
		this.creationDate = creationDate;
	}

	public String getCreatedUser() {
		return createdUser;
	}

	public void setCreatedUser(String createdUser) {
		this.createdUser = createdUser;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getUserGroup() {
		return userGroup;
	}

	public void setUserGroup(String userGroup) {
		this.userGroup = userGroup;
	}

	public String getDocumentId() {
		return documentId;
	}

	public void setDocumentId(String documentId) {
		this.documentId = documentId;
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

	public String getSelectedGrp() {
		return selectedGrp;
	}

	public void setSelectedGrp(String selectedGrp) {
		this.selectedGrp = selectedGrp;
	}

	public boolean getIsActiveForm() {
		return isActiveForm;
	}

	public void setIsActiveForm(boolean isActiveForm) {
		this.isActiveForm = isActiveForm;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public boolean isPublicAccess() {
		return isPublicAccess;
	}

	public void setPublicAccess(boolean isPublicAccess) {
		this.isPublicAccess = isPublicAccess;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Timestamp getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Timestamp modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

//	public boolean isDashboardType() {
//		return dashboardType;
//	}
//
//	public void setDashboardType(boolean dashboardType) {
//		this.dashboardType = dashboardType;
//	}

	@Override
	public String toString() {
		return "FormDto [formID=" + formID + ", formName=" + formName + ", tableSQL=" + tableSQL + ", insertSQL="
				+ insertSQL + ", deleteSQL=" + deleteSQL + ", createdUser=" + createdUser + ", documentId=" + documentId
				+ ", department=" + department + ", selectedGrp=" + selectedGrp + ", formatID=" + formatID
				+ ", versionNumber=" + versionNumber + ", isActiveForm=" + isActiveForm + ", userId=" + userId
				+ ", creationDate=" + creationDate + ", userGroup=" + userGroup + ", saveSQL=" + saveSQL + ", tenantId="
				+ tenantId + ", CellInfo=" + CellInfo + ", isPublicAccess=" + isPublicAccess + ", modifiedBy="
				+ modifiedBy + ", modifiedDate=" + modifiedDate + 
//				", dashboardType=" + dashboardType + 
				"]";
	}

}