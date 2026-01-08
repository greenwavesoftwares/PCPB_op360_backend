package in.co.greenwave.dto;

import in.co.greenwave.entity.ReportConfigEntity;

/**
 * This class represents a Data Transfer Object (DTO) for report configuration.
 * 
 * <p>
 * A DTO is a simple object that carries data between processes. In this case, 
 * it carries information related to report configurations such as the report's 
 * name, version, and other relevant details. 
 * </p>
 */
public class ReportConfigDto {
	// The unique identifier for the form associated with the report
	private int formId;

	// The name of the report
	private String reportName;

	// The name of the form associated with the report
	private String formName;

	// The version number of the report
	private int versionNumber;

	// The unique identifier for a cell in the report
	private String cellId;

	// An alias (or nickname) for the report
	private String reportAliasId;

	// The username of the person who created the report
	private String creator;

	// The username of any user with whom the report is shared
	private String sharedUser;

	// An alias (or nickname) for the form associated with the report
	private String formAliasId;

	// The identifier for the tenant (user group) associated with this report
	private String tenantId;

	// Indicates if the report can be filtered
	private boolean filterable;

	// Indicates if the report can be sorted
	private boolean sortable;

	private String fieldType;

	// Default constructor: Creates an empty ReportConfigDto object.
	public ReportConfigDto() {
		super();
	}

	/**
	 * Constructor to create a ReportConfigDto object with all its details.
	 *
	 * @param formId        Unique identifier for the form linked to the report
	 * @param reportName    The name of the report
	 * @param formName      The name of the form associated with the report
	 * @param versionNumber The version number of the report
	 * @param cellId        Unique identifier for a cell in the report
	 * @param reportAliasId An alias for the report
	 * @param creator       The username of the report creator
	 * @param sharedUser    The username of the user with whom the report is shared
	 * @param formAliasId   An alias for the form associated with the report
	 * @param tenantId      Identifier for the tenant (user group) of the report
	 * @param filterable    Indicates if the report can be filtered
	 * @param sortable      Indicates if the report can be sorted
	 */
	public ReportConfigDto(int formId, String reportName, String formName, int versionNumber, String cellId,
			String reportAliasId, String creator, String sharedUser, String formAliasId, String tenantId,
			boolean filterable, boolean sortable, String fieldType) {
		super();
		this.formId = formId;           // Set the form identifier
		this.reportName = reportName;   // Set the name of the report
		this.formName = formName;       // Set the name of the form
		this.versionNumber = versionNumber; // Set the version number of the report
		this.cellId = cellId;           // Set the cell identifier
		this.reportAliasId = reportAliasId; // Set the report alias
		this.creator = creator;         // Set the creator's username
		this.sharedUser = sharedUser;   // Set the shared user's username
		this.formAliasId = formAliasId; // Set the form alias
		this.tenantId = tenantId;       // Set the tenant identifier
		this.filterable = filterable;   // Set whether the report can be filtered
		this.sortable = sortable;       // Set whether the report can be sorted
		this.fieldType = fieldType;
	}

	/**
	 * Constructor to create a ReportConfigDto object from a ReportConfigEntity.
	 *
	 * @param reportConfigEntity The ReportConfigEntity from which to create the DTO
	 */
	public ReportConfigDto(ReportConfigEntity reportConfigEntity) {
		// Print the reportConfigEntity to the console for debugging purposes
		System.out.println(reportConfigEntity);

		// Initialize the DTO fields from the entity
		this.formId = reportConfigEntity.getFormId();          // Get and set the form identifier
		this.reportName = reportConfigEntity.getReportName();  // Get and set the report name
		this.formName = reportConfigEntity.getFormName();      // Get and set the form name
		this.versionNumber = reportConfigEntity.getVersionNumber(); // Get and set the version number
		this.cellId = reportConfigEntity.getCellId();          // Get and set the cell identifier
		this.reportAliasId = reportConfigEntity.getReportAliasId(); // Get and set the report alias
		this.creator = reportConfigEntity.getCreator();        // Get and set the creator's username
		this.sharedUser = reportConfigEntity.getSharedUser();  // Get and set the shared user's username
		this.formAliasId = reportConfigEntity.getFormAliasId(); // Get and set the form alias
		this.tenantId = reportConfigEntity.getTenantId();      // Get and set the tenant identifier
		this.fieldType = reportConfigEntity.getFieldType();
	}


	// Getters and Setters to get and change the values of each field of ReportConfigDto object.
	public int getFormId() {
		return formId;
	}
	public void setFormId(int formId) {
		this.formId = formId;
	}
	public String getReportName() {
		return reportName;
	}
	public void setReportName(String reportName) {
		this.reportName = reportName;
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

	public boolean isFilterable() {
		return filterable;
	}

	public void setFilterable(boolean filterable) {
		this.filterable = filterable;
	}

	public boolean isSortable() {
		return sortable;
	}

	public void setSortable(boolean sortable) {
		this.sortable = sortable;
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
	 * Returns a string representation of the ReportConfigDto object.
	 * This is useful for debugging and displaying information.
	 */
	@Override
	public String toString() {
		return "ReportConfigDto [formId=" + formId + ", reportName=" + reportName + ", formName=" + formName
				+ ", versionNumber=" + versionNumber + ", cellId=" + cellId + ", reportAliasId=" + reportAliasId
				+ ", creator=" + creator + ", sharedUser=" + sharedUser + ", formAliasId=" + formAliasId + ", tenantId="
				+ tenantId + ", filterable=" + filterable + ", sortable=" + sortable + ", fieldType=" + fieldType + "]";
	}
}
