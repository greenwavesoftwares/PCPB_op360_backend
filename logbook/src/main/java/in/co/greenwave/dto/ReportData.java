package in.co.greenwave.dto;

/**
 * The ReportData class holds details related to report cells, report aliases, and form aliases.
 * 
 * This class is designed to manage information about reports, specifically the identifiers for cells, reports,
 * and forms. It also includes a field to define the type of data for the report. This object is used across 
 * different parts of the application to represent and transfer report-related data.
 * 
 * This object is used generally during insert of a report
 */
public class ReportData {

	// The unique ID for a cell in the report.
	private String cellId;

	// The alias (another name) for the report, which helps in identifying the report.
	private String reportAliasId;

	// The alias (another name) for the form, which helps in identifying the form linked to the report.
	private String formAliasId;

	// The type of the field in the report (e.g., text, number, date).
	private String fieldType;

	// Default constructor: Creates an empty ReportData object.
	public ReportData() {
	}

	// Constructor to create a ReportData object with cellId, reportAliasId, and formAliasId.
	public ReportData(String cellId, String reportAliasId, String formAliasId) {
		this.cellId = cellId;
		this.reportAliasId = reportAliasId;
		this.formAliasId = formAliasId;
	}

	// Getter method to retrieve the cell ID.
	public String getCellId() {
		return cellId;
	}

	// Setter method to update the cell ID.
	public void setCellId(String cellId) {
		this.cellId = cellId;
	}

	// Getter method to retrieve the report alias ID.
	public String getReportAliasId() {
		return reportAliasId;
	}

	// Setter method to update the report alias ID.
	public void setReportAliasId(String reportAliasId) {
		this.reportAliasId = reportAliasId;
	}

	// Getter method to retrieve the form alias ID.
	public String getFormAliasId() {
		return formAliasId;
	}

	// Setter method to update the form alias ID.
	public void setFormAliasId(String formAliasId) {
		this.formAliasId = formAliasId;
	}

	// Getter method to retrieve the field type.
	public String getFieldType() {
		return fieldType;
	}

	// Setter method to update the field type.
	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	// A method to represent the object as a string, useful for debugging or logging.
	@Override
	public String toString() {
		return "ReportData [cellId=" + cellId + ", reportAliasId=" + reportAliasId + ", formAliasId=" + formAliasId + "]";
	}

	// Generates a unique hash code for the object, based on the cell ID.
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cellId == null) ? 0 : cellId.hashCode());
		return result;
	}

	// Method to check if two ReportData objects are equal by comparing their cell IDs.
	@Override
	public boolean equals(Object obj) {
		// If both objects are the same, return true.
		if (this == obj)
			return true;
		// If the other object is null, return false.
		if (obj == null)
			return false;
		// If the classes are not the same, return false.
		if (getClass() != obj.getClass())
			return false;
		ReportData other = (ReportData) obj;
		// Check if the cellId of both objects is equal.
		if (cellId == null) {
			if (other.cellId != null)
				return false;
		} else if (!cellId.equals(other.cellId))
			return false;
		return true;
	}
}
