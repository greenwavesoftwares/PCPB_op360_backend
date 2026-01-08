package in.co.greenwave.assetapi.model;

// This class represents a form's details in our application.
public class FormDetails {

	// These are the properties or characteristics of the form.
	private int formID; // A unique number to identify each form
	private String formName; // The name of the form
	private String tableSQL; // SQL command to create the table for the form
	private String insertSql; // SQL command to add data to the form's table
	private String deleteSQL; // SQL command to remove data from the form's table
	private String createdUser; // The name of the user who created the form
	private String documentId; // An identifier for the document associated with the form
	private String department; // The department that the form belongs to
	private String selectedGrp; // The selected group for the form
	private String formatID; // An identifier for the format of the form
	private int versionNumber; // The version number of the form, to keep track of changes
	private Boolean isActiveForm = false; // A flag to show if the form is currently active

	// This is a default constructor, which creates an empty form without any details.
	public FormDetails() {

	}

	// This constructor allows us to create a form with specific details.
	public FormDetails(int formID, String formName, String tableSQL, String insertSql, String deleteSQL, 
			String documentId, String department, String selectedGrp, String formatID, int vrsnNumb) {
		super();
		this.formID = formID; // Set the unique form ID
		this.formName = formName; // Set the name of the form
		this.tableSQL = tableSQL; // Set the SQL command for the form's table
		this.insertSql = insertSql; // Set the SQL command to insert data
		this.deleteSQL = deleteSQL; // Set the SQL command to delete data
		this.documentId = documentId; // Set the document ID
		this.department = department; // Set the department name
		this.formatID = formatID; // Set the format ID
		this.versionNumber = vrsnNumb; // Set the version number
		this.selectedGrp = selectedGrp; // Set the selected group
	}

	// This constructor allows us to create a form with more details, including whether it is active.
	public FormDetails(int formID, String formName, int versionNumber, String documentId,
			String department, String selectedGrp, String formatID, String createdUser, 
			Boolean isActiveForm, String insertSql, String deleteSQL, String tableSQL) {
		super();
		this.formID = formID;            // Set the unique form ID
		this.formName = formName;        // Set the name of the form
		this.versionNumber = versionNumber; // Set the version number
		this.documentId = documentId;    // Set the document ID
		this.department = department;     // Set the department name
		this.formatID = formatID;        // Set the format ID
		this.createdUser = createdUser;  // Set the name of the user who created it
		this.isActiveForm = isActiveForm; // Set whether the form is active
		this.selectedGrp = selectedGrp;  // Set the selected group
		this.insertSql = insertSql;      // Set the SQL command to insert data
		this.deleteSQL = deleteSQL;      // Set the SQL command to delete data
		this.tableSQL = tableSQL;        // Set the SQL command for the form's table
	}

	// This constructor is a simpler version that sets only a few details.
	public FormDetails(int formID, String formName, int versionNumber, String documentId,
			String department, String selectedGrp, String formatID, Boolean isActiveForm) {
		super();
		this.formID = formID; // Set the unique form ID
		this.formName = formName; // Set the name of the form
		this.versionNumber = versionNumber; // Set the version number
		this.documentId = documentId; // Set the document ID
		this.department = department; // Set the department name
		this.formatID = formatID; // Set the format ID
		this.isActiveForm = isActiveForm; // Set whether the form is active
		this.selectedGrp = selectedGrp; // Set the selected group
	}

	// This constructor allows creating a form with just the SQL commands and name.
	public FormDetails(String formName, String tableSQL, String insertSql, String deleteSQL) {
		super();
		this.formName = formName; // Set the name of the form
		this.tableSQL = tableSQL; // Set the SQL command for the form's table
		this.insertSql = insertSql; // Set the SQL command to insert data
		this.deleteSQL = deleteSQL; // Set the SQL command to delete data
	}

	// These are the getter and setter methods. They allow us to get and set the values of the properties.

	public String getFormName() {
		return formName; // Returns the form name
	}

	public void setFormName(String formName) {
		this.formName = formName; // Sets the form name
	}

	public String getTableSQL() {
		return tableSQL; // Returns the SQL command for the table
	}

	public void setTableSQL(String tableSQL) {
		this.tableSQL = tableSQL; // Sets the SQL command for the table
	}

	public String getInsertSql() {
		return insertSql; // Returns the SQL command to insert data
	}

	public void setInsertSql(String insertSql) {
		this.insertSql = insertSql; // Sets the SQL command to insert data
	}

	public String getDeleteSQL() {
		return deleteSQL; // Returns the SQL command to delete data
	}

	public void setDeleteSQL(String deleteSQL) {
		this.deleteSQL = deleteSQL; // Sets the SQL command to delete data
	}

	public String getDocumentId() {
		return documentId; // Returns the document ID
	}

	public void setDocumentId(String documentId) {
		this.documentId = documentId; // Sets the document ID
	}

	public String getDepartment() {
		return department; // Returns the department name
	}

	public void setDepartment(String department) {
		this.department = department; // Sets the department name
	}

	public String getFormatID() {
		return formatID; // Returns the format ID
	}

	public void setFormatID(String formatID) {
		this.formatID = formatID; // Sets the format ID
	}

	public int getVersionNumber() {
		return versionNumber; // Returns the version number
	}

	public void setVersionNumber(int versionNumber) {
		this.versionNumber = versionNumber; // Sets the version number
	}

	public int getFormID() {
		return formID; // Returns the unique form ID
	}

	public void setFormID(int formID) {
		this.formID = formID; // Sets the unique form ID
	}

	public Boolean getIsActiveForm() {
		return isActiveForm; // Returns whether the form is active
	}

	public void setIsActiveForm(Boolean isActiveForm) {
		this.isActiveForm = isActiveForm; // Sets whether the form is active
	}

	public String getSelectedGrp() {
		return selectedGrp; // Returns the selected group
	}

	public void setSelectedGrp(String selectedGrp) {
		this.selectedGrp = selectedGrp; // Sets the selected group
	}

	public String getCreatedUser() {
		return createdUser; // Returns the name of the user who created the form
	}

	public void setCreatedUser(String createdUser) {
		this.createdUser = createdUser; // Sets the name of the user who created the form
	}

	// This method creates a string that describes the form's details.
	@Override
	public String toString() {
		return "FormDetails [formID=" + formID + ", formName=" + formName + ", tableSQL=" + tableSQL + 
				", insertSql=" + insertSql + ", deleteSQL=" + deleteSQL + ", createdUser=" + createdUser + 
				", documentId=" + documentId + ", department=" + department + ", selectedGrp=" + selectedGrp + 
				", formatID=" + formatID + ", versionNumber=" + versionNumber + ", isActiveForm=" + isActiveForm + "]";
	}
}
