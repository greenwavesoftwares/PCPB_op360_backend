package in.co.greenwave.entity;

import java.sql.Timestamp;
import in.co.greenwave.dto.FormDto;

/**
 * Represents a digital logbook form in the system.
 * 
 * <p>
 * This class is linked to the "DigitalLogbookFormInfo" table in the database.
 * It holds important details about a logbook form, like its name and SQL commands 
 * for saving, deleting, and managing the form. Additionally, it contains information 
 * about when it was created, who created it, and some other important identifiers 
 * such as document ID and format ID.
 * </p>
 * 
 * <p>
 * The class provides different ways to create a form:
 * - You can create a new form by providing all its details at once.
 * - You can also create a form from a Data Transfer Object (DTO), which is 
 * a simpler way to pass data around in the application.
 * </p>
 * 
 * <p>
 * This class has methods to get and set (change) the values of its fields, 
 * making it easy to work with form data.
 * </p>
 * 
 * <p>
 * Usage Example:
 * <pre>
 * FormInfo formInfo = new FormInfo();
 * formInfo.setFormName("Sample Form");
 * formInfo.setUserId("user123");
 * // Set other properties as needed
 * </pre>
 * </p>
 */

public class FormInfo {
	// Unique identifier for the form
	private int formId;

	// The name of the form
	private String formName;

	// The ID of the user who created the form
	private String userId;

	// SQL statement for saving the form's data
	private String saveSQL;

	// SQL statement for accessing the table associated with the form
	private String tableSQL;

	// SQL statement for deleting the form's data
	private String deleteSQL;

	// The date and time when the form was created
	private Timestamp creationDate;

	// The user who created the form
	private String createdUser;

	// The department associated with the form
	private String department;

	// The user group for the form
	private String userGroup;

	// Unique identifier for the document linked to the form
	private String documentId;

	// The format of the form
	private String formatId;

	// The version number of the form
	private int versionNumber;

	// Indicates whether the form is currently active
	private boolean isActiveForm;

	// Identifier for the tenant (user group) of the form
	private String tenantId;

	// Default constructor: Creates an empty FormInfo object.
	//Added By Ashok
	private boolean isPublicAccess = false;

	//Added By Ashok
	private String modifiedBy;
	//added by ashok
	private Timestamp modifiedDate;

	private boolean dashboardType;

	public FormInfo() {
	}

	/**
	 * Constructor to create a FormInfo object with all its details.
	 *
	 * @param formId       Unique identifier for the form
	 * @param formName     Name of the form
	 * @param userId       ID of the user who created the form
	 * @param saveSQL      SQL statement to save the form's data
	 * @param tableSQL     SQL statement for the form's table
	 * @param deleteSQL    SQL statement to delete the form's data
	 * @param creationDate Date and time of form creation
	 * @param createdUser  User who created the form
	 * @param department   Department linked to the form
	 * @param userGroup    User group associated with the form
	 * @param documentID   Unique identifier for the document
	 * @param formatId     Format of the form
	 * @param versionNumber Version number of the form
	 * @param isActiveForm  Indicates if the form is active
	 * @param tenantId      Identifier for the tenant of the form
	 */
	public FormInfo(int formId, String formName, String userId, String saveSQL, String tableSQL, String deleteSQL,
			Timestamp creationDate, String createdUser, String department, String userGroup, String documentID,
			String formatId, int versionNumber, boolean isActiveForm, String tenantId) {
		super();
		this.formId = formId;
		this.formName = formName;
		this.userId = userId;
		this.saveSQL = saveSQL;
		this.tableSQL = tableSQL;
		this.deleteSQL = deleteSQL;
		this.creationDate = creationDate;
		this.createdUser = createdUser;
		this.department = department;
		this.userGroup = userGroup;
		this.documentId = documentID;
		this.formatId = formatId;
		this.versionNumber = versionNumber;
		this.isActiveForm = isActiveForm;
		this.tenantId = tenantId;
	}

	/**
	 * Another constructor to create a FormInfo object without the tenant ID.
	 *
	 * @param formId       Unique identifier for the form
	 * @param formName     Name of the form
	 * @param userId       ID of the user who created the form
	 * @param saveSQL      SQL statement to save the form's data
	 * @param tableSQL     SQL statement for the form's table
	 * @param deleteSQL    SQL statement to delete the form's data
	 * @param creationDate Date and time of form creation
	 * @param createdUser  User who created the form
	 * @param department   Department linked to the form
	 * @param userGroup    User group associated with the form
	 * @param documentID   Unique identifier for the document
	 * @param formatId     Format of the form
	 * @param versionNumber Version number of the form
	 * @param isActiveForm  Indicates if the form is active
	 */
	public FormInfo(int formId, String formName, String userId, String saveSQL, String tableSQL, String deleteSQL,
			Timestamp creationDate, String createdUser, String department, String userGroup, String documentID,
			String formatId, int versionNumber, boolean isActiveForm, boolean dashboardType) {
		super();
		this.formId = formId;
		this.formName = formName;
		this.userId = userId;
		this.saveSQL = saveSQL;
		this.tableSQL = tableSQL;
		this.deleteSQL = deleteSQL;
		this.creationDate = creationDate;
		this.createdUser = createdUser;
		this.department = department;
		this.userGroup = userGroup;
		this.documentId = documentID;
		this.formatId = formatId;
		this.versionNumber = versionNumber;
		this.isActiveForm = isActiveForm;
		this.dashboardType = dashboardType;
	}

	/**
	 * Constructor to create a FormInfo object from a FormDto object.
	 * This helps in converting data from a simpler structure to a more complex one.
	 *
	 * @param formDto The Data Transfer Object (DTO) to create the FormInfo
	 */
	public FormInfo(FormDto formDto) {
		this.formId = formDto.getFormID();
		this.formName = formDto.getFormName();
		this.userId = formDto.getUserId();
		this.saveSQL = formDto.getSaveSQL();
		this.tableSQL = formDto.getTableSQL(); // Fixed from deleteSQL to tableSQL
		this.deleteSQL = formDto.getDeleteSQL();
		this.creationDate = formDto.getCreationDate();
		this.createdUser = formDto.getCreatedUser();
		this.department = formDto.getDepartment();
		this.userGroup = formDto.getUserGroup();
		this.documentId = formDto.getDocumentId();
		this.formatId = formDto.getFormatID();
		this.versionNumber = formDto.getVersionNumber();
		this.isActiveForm = formDto.getIsActiveForm();
		this.tenantId = formDto.getTenantId();
	}

	public FormInfo(int formId, String formName, String userId, String saveSQL, String tableSQL, String deleteSQL,
			Timestamp creationDate, String createdUser, String department, String userGroup, String documentId,
			String formatId, int versionNumber, boolean isActiveForm, String tenantId, boolean isPublicAccess,
			String modifiedBy, Timestamp modifiedDate, boolean dashboardType) {
		super();
		this.formId = formId;
		this.formName = formName;
		this.userId = userId;
		this.saveSQL = saveSQL;
		this.tableSQL = tableSQL;
		this.deleteSQL = deleteSQL;
		this.creationDate = creationDate;
		this.createdUser = createdUser;
		this.department = department;
		this.userGroup = userGroup;
		this.documentId = documentId;
		this.formatId = formatId;
		this.versionNumber = versionNumber;
		this.isActiveForm = isActiveForm;
		this.tenantId = tenantId;
		this.isPublicAccess = isPublicAccess;
		this.modifiedBy = modifiedBy;
		this.modifiedDate = modifiedDate;
		this.dashboardType = dashboardType;
	}

	// Getters and Setters to get and change the values of each field of FormInfo object.
	public int getFormId() {
		return formId;
	}

	public void setFormId(int formId) {
		this.formId = formId;
	}

	public String getFormName() {
		return formName;
	}

	public void setFormName(String formName) {
		this.formName = formName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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

	public String getDocumentID() {
		return documentId;
	}

	public void setDocumentID(String documentID) {
		this.documentId = documentID;
	}

	public String getDocumentId() {
		return documentId;
	}

	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}

	public String getFormatId() {
		return formatId;
	}

	public void setFormatId(String formatId) {
		this.formatId = formatId;
	}

	public String getDepartmentID() {
		return formatId;
	}

	public void setDepartmentID(String departmentID) {
		this.formatId = departmentID;
	}

	public int getVersionNumber() {
		return versionNumber;
	}

	public void setVersionNumber(int versionNumber) {
		this.versionNumber = versionNumber;
	}

	public boolean getIsActiveForm() {
		return isActiveForm;
	}

	public void setIsActiveForm(boolean isActiveForm) {
		this.isActiveForm = isActiveForm;
	}

	public void setActiveForm(boolean isActiveForm) {
		this.isActiveForm = isActiveForm;
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

	public boolean isDashboardType() {
		return dashboardType;
	}

	public void setDashboardType(boolean dashboardType) {
		this.dashboardType = dashboardType;
	}

	@Override
	public String toString() {
		return "FormInfo [formId=" + formId + ", formName=" + formName + ", userId=" + userId + ", saveSQL=" + saveSQL
				+ ", tableSQL=" + tableSQL + ", deleteSQL=" + deleteSQL + ", creationDate=" + creationDate
				+ ", createdUser=" + createdUser + ", department=" + department + ", userGroup=" + userGroup
				+ ", documentId=" + documentId + ", formatId=" + formatId + ", versionNumber=" + versionNumber
				+ ", isActiveForm=" + isActiveForm + ", tenantId=" + tenantId + ", isPublicAccess=" + isPublicAccess
				+ ", modifiedBy=" + modifiedBy + ", modifiedDate=" + modifiedDate + ", dashboardType=" + dashboardType
				+ "]";
	}

	/**
	 * Returns a string representation of the FormInfo object.
	 * This is useful for debugging and displaying information.
	 */

}
