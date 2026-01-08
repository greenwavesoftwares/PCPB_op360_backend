package in.co.greenwave.entity;

/**
 * This class represents the configuration for a report in the system.
 * 
 * <p>
 * It contains important details about the report, such as its name, the form it is linked to, 
 * the version number, the creator of the report, and any users with whom it is shared.
 * </p>
 */
public class ReportConfigEntity {
	// The name of the report
	private String reportName;

	// The unique identifier for the form associated with this report
	private int formId;

	// The name of the form associated with this report
	private String formName;

	// The version number of the report
	private int versionNumber;

	// The unique identifier for a cell in the report
	private String cellId;

	// An alias (or nickname) for the report, which can be used in references
	private String reportAliasId;

	// The username of the person who created the report
	private String creator;

	// The username of any user with whom the report is shared
	private String sharedUser;

	// An alias (or nickname) for the form that can be used in references
	private String formAliasId;

	// The identifier for the tenant (user group) associated with this report
	private String tenantId;
	
	private String fieldType;

	// Default constructor: Creates an empty ReportConfigEntity object.
	public ReportConfigEntity() {
	}

	/**
	 * Constructor to create a ReportConfigEntity object with all its details.
	 *
	 * @param reportName    The name of the report
	 * @param formId        Unique identifier for the form linked to the report
	 * @param formName      The name of the form associated with the report
	 * @param versionNumber The version number of the report
	 * @param cellId        Unique identifier for a cell in the report
	 * @param reportAliasId An alias for the report
	 * @param creator       The username of the report creator
	 * @param sharedUser    The username of the user with whom the report is shared
	 * @param formAliasId   An alias for the form associated with the report
	 * @param tenantId      Identifier for the tenant (user group) of the report
	 */
	public ReportConfigEntity(String reportName, int formId, String formName, int versionNumber, String cellId,
			String reportAliasId, String creator, String sharedUser, String formAliasId, String tenantId) {
		super();
		this.reportName = reportName;     // Set the name of the report
		this.formId = formId;             // Set the form identifier
		this.formName = formName;         // Set the name of the form
		this.versionNumber = versionNumber; // Set the version number of the report
		this.cellId = cellId;             // Set the cell identifier
		this.reportAliasId = reportAliasId; // Set the report alias
		this.creator = creator;           // Set the creator's username
		this.sharedUser = sharedUser;     // Set the shared user's username
		this.formAliasId = formAliasId;   // Set the form alias
		this.tenantId = tenantId;         // Set the tenant identifier
	}

	// Getters and Setters to get and change the values of each field of ReportConfigEntity object.
	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

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

	public int getVersionNumber() {
		return versionNumber;
	}

	public void setVersionNumber(int versionNumber) {
		this.versionNumber = versionNumber;
	}

	public String getCellId() {
		return cellId;
	}

	public void setCellId(String cellId) {
		this.cellId = cellId;
	}

	public String getReportAliasId() {
		return reportAliasId;
	}

	public void setReportAliasId(String reportAliasId) {
		this.reportAliasId = reportAliasId;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getSharedUser() {
		return sharedUser;
	}

	public void setSharedUser(String sharedUser) {
		this.sharedUser = sharedUser;
	}

	public String getFormAliasId() {
		return formAliasId;
	}

	public void setFormAliasId(String formAliasId) {
		this.formAliasId = formAliasId;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public String getFieldType() {
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	
	/**
	 * Returns a string representation of the ReportConfigEntity object.
	 * This is useful for debugging and displaying information.
	 */
	@Override
	public String toString() {
		return "ReportConfigEntity [reportName=" + reportName + ", formId=" + formId + ", formName=" + formName
				+ ", versionNumber=" + versionNumber + ", cellId=" + cellId + ", reportAliasId=" + reportAliasId
				+ ", creator=" + creator + ", sharedUser=" + sharedUser + ", formAliasId=" + formAliasId + ", tenantId="
				+ tenantId + ", fieldType=" + fieldType + "]";
	}
}
