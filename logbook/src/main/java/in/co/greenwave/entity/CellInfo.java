package in.co.greenwave.entity;

/**
 * Represents information about a cell of a @Logbook.
 * 
 * <p>
 * This class stores details about a specific cell within a Logbook, such as its
 * position (row and column), the type of field it represents, its size, validation rules, and any 
 * special behavior like being editable, dependent on other fields, or triggering actions such as auto-refresh.
 * It can also store visual details like CSS styling and any button actions for the cell.
 * </p>
 */
public class CellInfo {

	// The ID of the form this cell belongs to.
	private int formId;

	// The row number where the cell is placed in the form.
	private int rowNum;

	// The column number where the cell is placed in the form.
	private int colNum;

	// A unique identifier for the cell.
	private String cellId;

	// The alias (another name) used for the cell.
	private String aliasId;

	// The number of columns this cell spans (width in the form).
	private int colSpan;

	// The number of rows this cell spans (height in the form).
	private int rowSpan;

	// The type of input field this cell represents (e.g., text, number, date).
	private String fieldType;

	// The minimum value allowed for numeric fields.
	private Double numMinVal;

	// The maximum value allowed for numeric fields.
	private Double numMaxVal;

	// The number of decimal places allowed for numeric fields.
	private int numDecimalPoint;

	// Indicates if the cell can be edited by the user.
	private boolean editable;

	// Indicates if this cell depends on the value of other cells.
	private boolean dependent;

	// The column name in the database associated with this cell's value.
	private String dbColName;

	// SQL query to determine how this cell depends on other values.
	private String depSql;

	// The file path for an image to be displayed in the cell.
	private String imagePath;

	// The height of the image in the cell.
	private int imgHeight;

	// The width of the image in the cell.
	private int imgWidth;

	// The CSS styling applied to the cell for visual design.
	private String cellCSS;

	// The CSS styling applied to any components inside the cell.
	private String componentCss;

	// SQL query to save the cell's data to the database.
	private String saveSql;

	// A string that defines how the cell should be updated via AJAX (without refreshing the page).
	private String ajaxUpdateStr;

	// Indicates if the cell should automatically refresh its content.
	private boolean autoRefresh;

	// The time interval (in seconds) for auto-refreshing the cell's content.
	private int refreshInterval;

	// The API URL to fetch or send data for this cell.
	private String apiUrl;

	// The body (content) to be sent with the API request for this cell.
	private String apiBody;

	// The HTTP method (GET, POST, etc.) used for the API request.
	private String apiMethod;

	// The version number of this cell, used to track changes over time.
	private int versionNumber;

	// Indicates if this cell is a required field (must be filled out by the user).
	private boolean requiredField;

	// Indicates if this cell contains an expression (a formula or logic).
	private boolean expressionField;

	// An expression that defines when this cell should be disabled (not editable).
	private String disabledExpression;

	// Defines the type of disabled expression (true or false).
	private boolean disabledExpType;

	// The date format pattern for date fields in the cell.
	private String datePattern;

	// Indicates if the polling (repeated checking) for the start condition is enabled.
	private boolean pollStartExpType;

	// The expression that defines when polling for start should begin.
	private String pollStartExpression;

	// Indicates if the polling for the stop condition is enabled.
	private boolean pollStopExpType;

	// The expression that defines when polling for stop should begin.
	private String pollStopExpression;

	// The icon that should appear on a button in the cell (if applicable).
	private String buttonIcon;

	// The key value associated with the button (for identification).
	private String buttonKeyValue;

	// The arguments passed to the button when it is clicked.
	private String buttonArguments;

	// The logic or behavior triggered when the button is pressed.
	private String buttonLogic;

	// The CSS class applied to the button for styling.
	private String buttonClassName;

	// The type of button (e.g., submit, reset, etc.).
	private String buttonType;

	// SQL query that is global for this cell (applies to all situations).
	private String globalSql;

	// Content of a file (e.g., image, document) associated with the cell.
	private byte[] fileContent;

	// Default constructor: Creates an empty CellInfo object.
	public CellInfo() {
	}

	/**
	 * Constructor to initialize the CellInfo object with all its details.
	 * 
	 * @param formId the ID of the form
	 * @param rowNum the row number in the form
	 * @param colNum the column number in the form
	 * @param cellId the unique ID of the cell
	 * @param aliasId an alias name for the cell
	 * @param colSpan how many columns the cell spans
	 * @param rowSpan how many rows the cell spans
	 * @param fieldType the type of field (text, number, etc.)
	 * @param numMinVal the minimum value for numeric fields
	 * @param numMaxVal the maximum value for numeric fields
	 * @param numDecimalPoint number of decimal places for numbers
	 * @param editable whether the cell can be edited
	 * @param dependent whether the cell depends on other cells
	 * @param dbColName the database column associated with the cell
	 * @param depSql the SQL query for cell dependency
	 * @param imagePath the image path for the cell
	 * @param imgHeight the height of the image
	 * @param imgWidth the width of the image
	 * @param cellCss the CSS applied to the cell
	 * @param componentCss the CSS applied to components inside the cell
	 * @param saveSql the SQL query to save the cell data
	 * @param ajaxUpdateStr the string for AJAX updates
	 * @param autoRefresh whether the cell automatically refreshes
	 * @param refreshInterval how often the cell refreshes (in seconds)
	 * @param apiUrl the URL for API requests
	 * @param apiBody the body of the API request
	 * @param apiMethod the HTTP method (GET, POST, etc.) used for the API
	 * @param versionNumber the version number of the cell
	 * @param requiredField whether the field is required
	 * @param expressionField whether the cell uses an expression
	 * @param disableExpression the expression to disable the cell
	 * @param disabledExpType the type of disabled expression (true/false)
	 * @param datePattern the date pattern for date fields
	 * @param pollStartExpType whether polling for start is enabled
	 * @param pollStartExpression the expression for polling start
	 * @param pollStopExpType whether polling for stop is enabled
	 * @param pollStopExpression the expression for polling stop
	 * @param buttonIcon the icon for a button in the cell
	 * @param buttonKeyValue the key value associated with the button
	 * @param buttonArguments the arguments for the button
	 * @param buttonLogic the logic triggered when the button is clicked
	 * @param buttonClassName the CSS class applied to the button
	 * @param buttonType the type of button (submit, reset, etc.)
	 * @param globalSql the global SQL query for the cell
	 * @param fileContent content of a file associated with the cell
	 */
	public CellInfo(int formId, int rowNum, int colNum, String cellId, String aliasId, int colSpan,
			int rowSpan, String fieldType, Double numMinVal, Double numMaxVal, int numDecimalPoint,
			boolean editable, boolean dependent, String dbColName, String depSql, String imagePath, int imgHeight,
			int imgWidth, String cellCss, String componentCss, String saveSql, String ajaxUpdateStr,
			boolean autoRefresh, int refreshInterval, String apiUrl, String apiBody, String apiMethod,
			int versionNumber, boolean requiredField, boolean expressionField, String disableExpression,
			boolean disabledExpType, String datePattern, boolean pollStartExpType, String pollStartExpression,
			boolean pollStopExpType, String pollStopExpression, String buttonIcon, String buttonKeyValue,
			String buttonArguments, String buttonLogic, String buttonClassName, String buttonType, String globalSql,
			byte [] fileContent) {
		super();
		this.formId = formId;
		this.rowNum = rowNum;
		this.colNum = colNum;
		this.cellId = cellId;
		this.aliasId = aliasId;
		this.colSpan = colSpan;
		this.rowSpan = rowSpan;
		this.fieldType = fieldType;
		this.numMinVal = numMinVal;
		this.numMaxVal = numMaxVal;
		this.numDecimalPoint = numDecimalPoint;
		this.editable = editable;
		this.dependent = dependent;
		this.dbColName = dbColName;
		this.depSql = depSql;
		this.imagePath = imagePath;
		this.imgHeight = imgHeight;
		this.imgWidth = imgWidth;
		this.cellCSS = cellCss;
		this.componentCss = componentCss;
		this.saveSql = saveSql;
		this.ajaxUpdateStr = ajaxUpdateStr;
		this.autoRefresh = autoRefresh;
		this.refreshInterval = refreshInterval;
		this.apiUrl = apiUrl;
		this.apiBody = apiBody;
		this.apiMethod = apiMethod;
		this.versionNumber = versionNumber;
		this.requiredField = requiredField;
		this.expressionField = expressionField;
		this.disabledExpression = disableExpression;
		this.disabledExpType = disabledExpType;
		this.datePattern = datePattern;
		this.pollStartExpType = pollStartExpType;
		this.pollStartExpression = pollStartExpression;
		this.pollStopExpType = pollStopExpType;
		this.pollStopExpression = pollStopExpression;
		this.buttonIcon = buttonIcon;
		this.buttonKeyValue = buttonKeyValue;
		this.buttonArguments = buttonArguments;
		this.buttonLogic = buttonLogic;
		this.buttonClassName = buttonClassName;
		this.buttonType = buttonType;
		this.globalSql = globalSql;
		this.fileContent = fileContent;
	}

	// Getters and Setters to get and change the values of each field of CellInfo object.
	public int getFormId() {
		return formId;
	}

	public void setFormId(int formId) {
		this.formId = formId;
	}

	public int getRowNum() {
		return rowNum;
	}

	public void setRowNum(int rowNum) {
		this.rowNum = rowNum;
	}

	public int getColNum() {
		return colNum;
	}

	public void setColNum(int colNum) {
		this.colNum = colNum;
	}

	public String getCellId() {
		return cellId;
	}

	public void setCellId(String cellId) {
		this.cellId = cellId;
	}

	public String getAliasId() {
		return aliasId;
	}

	public void setAliasId(String aliasId) {
		this.aliasId = aliasId;
	}

	public int getColsSpan() {
		return colSpan;
	}

	public void setColsSpan(int colSpan) {
		this.colSpan = colSpan;
	}

	public int getRowSpan() {
		return rowSpan;
	}

	public void setRowSpan(int rowSpan) {
		this.rowSpan = rowSpan;
	}

	public String getFieldType() {
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	public Double getNumMinVal() {
		return numMinVal;
	}

	public void setNumMinVal(Double numMinVal) {
		this.numMinVal = numMinVal;
	}

	public Double getNumMaxVal() {
		return numMaxVal;
	}

	public void setNumMaxVal(Double numMaxVal) {
		this.numMaxVal = numMaxVal;
	}

	public int getNumDecimalPoint() {
		return numDecimalPoint;
	}

	public void setNumDecimalPoint(int numDecimalPoint) {
		this.numDecimalPoint = numDecimalPoint;
	}

	public boolean getEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	public boolean getDependent() {
		return dependent;
	}

	public void setDependent(boolean dependent) {
		this.dependent = dependent;
	}

	public String getDbColName() {
		return dbColName;
	}

	public void setDbColName(String dbColName) {
		this.dbColName = dbColName;
	}

	public String getDepSql() {
		return depSql;
	}

	public void setDepSql(String depSql) {
		this.depSql = depSql;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public int getImgHeight() {
		return imgHeight;
	}

	public void setImgHeight(int imgHeight) {
		this.imgHeight = imgHeight;
	}

	public int getImgWidth() {
		return imgWidth;
	}

	public void setImgWidth(int imgWidth) {
		this.imgWidth = imgWidth;
	}

	public String getCellCss() {
		return cellCSS;
	}

	public void setCellCss(String cellCss) {
		this.cellCSS = cellCss;
	}

	public String getComponentCss() {
		return componentCss;
	}

	public void setComponentCss(String componentCss) {
		this.componentCss = componentCss;
	}

	public String getSaveSql() {
		return saveSql;
	}

	public void setSaveSql(String saveSql) {
		this.saveSql = saveSql;
	}

	public String getAjaxUpdateStr() {
		return ajaxUpdateStr;
	}

	public void setAjaxUpdateStr(String ajaxUpdateStr) {
		this.ajaxUpdateStr = ajaxUpdateStr;
	}

	public boolean getAutoRefresh() {
		return autoRefresh;
	}

	public void setAutoRefresh(boolean autoRefresh) {
		this.autoRefresh = autoRefresh;
	}

	public int getRefreshInterval() {
		return refreshInterval;
	}

	public void setRefreshInterval(int refreshInterval) {
		this.refreshInterval = refreshInterval;
	}

	public String getApiUrl() {
		return apiUrl;
	}

	public void setApiUrl(String apiUrl) {
		this.apiUrl = apiUrl;
	}

	public String getApiBody() {
		return apiBody;
	}

	public void setApiBody(String apiBody) {
		this.apiBody = apiBody;
	}

	public String getApiMethod() {
		return apiMethod;
	}

	public void setApiMethod(String apiMethod) {
		this.apiMethod = apiMethod;
	}

	public int getVersionNumber() {
		return versionNumber;
	}

	public void setVersionNumber(int versionNumber) {
		this.versionNumber = versionNumber;
	}

	public boolean getRequiredField() {
		return requiredField;
	}

	public void setRequiredField(boolean requiredField) {
		this.requiredField = requiredField;
	}

	public boolean getExpressionField() {
		return expressionField;
	}

	public void setExpressionField(boolean expressionField) {
		this.expressionField = expressionField;
	}

	public String getDisableExpression() {
		return disabledExpression;
	}

	public void setDisableExpression(String disableExpression) {
		this.disabledExpression = disableExpression;
	}

	public boolean getDisabledExpType() {
		return disabledExpType;
	}

	public void setDisabledExpType(boolean disabledExpType) {
		this.disabledExpType = disabledExpType;
	}

	public String getDatePattern() {
		return datePattern;
	}

	public void setDatePattern(String datePattern) {
		this.datePattern = datePattern;
	}

	public boolean getPollStartExpType() {
		return pollStartExpType;
	}

	public void setPollStartExpType(boolean pollStartExpType) {
		this.pollStartExpType = pollStartExpType;
	}

	public String getPollStartExpression() {
		return pollStartExpression;
	}

	public void setPollStartExpression(String pollStartExpression) {
		this.pollStartExpression = pollStartExpression;
	}

	public boolean getPollStopExpType() {
		return pollStopExpType;
	}

	public void setPollStopExpType(boolean pollStopExpType) {
		this.pollStopExpType = pollStopExpType;
	}

	public String getPollStopExpression() {
		return pollStopExpression;
	}

	public void setPollStopExpression(String pollStopExpression) {
		this.pollStopExpression = pollStopExpression;
	}

	public String getButtonIcon() {
		return buttonIcon;
	}

	public void setButtonIcon(String buttonIcon) {
		this.buttonIcon = buttonIcon;
	}

	public String getButtonKeyValue() {
		return buttonKeyValue;
	}

	public void setButtonKeyValue(String buttonKeyValue) {
		this.buttonKeyValue = buttonKeyValue;
	}

	public String getButtonArguments() {
		return buttonArguments;
	}

	public void setButtonArguments(String buttonArguments) {
		this.buttonArguments = buttonArguments;
	}

	public String getButtonLogic() {
		return buttonLogic;
	}

	public void setButtonLogic(String buttonLogic) {
		this.buttonLogic = buttonLogic;
	}

	public String getButtonClassName() {
		return buttonClassName;
	}

	public void setButtonClassName(String buttonClassName) {
		this.buttonClassName = buttonClassName;
	}

	public String getButtonType() {
		return buttonType;
	}

	public void setButtonType(String buttonType) {
		this.buttonType = buttonType;
	}

	public String getGlobalSql() {
		return globalSql;
	}

	public void setGlobalSql(String globalSql) {
		this.globalSql = globalSql;
	}

	public byte[] getFileContent() {
		return fileContent;
	}

	public void setFileContent(byte[] fileContent) {
		this.fileContent = fileContent;
	}

	// A method to represent the object as a string, useful for logging or debugging.
	@Override
	public String toString() {
		return "CellInfo [formId=" + formId + ", rowNum=" + rowNum + ", colNum=" + colNum + ", cellId=" + cellId
				+ ", aliasId=" + aliasId + ", colSpan=" + colSpan + ", rowSpan=" + rowSpan + ", fieldType=" + fieldType
				+ ", numMinVal=" + numMinVal + ", numMaxVal=" + numMaxVal + ", numDecimalPoint=" + numDecimalPoint
				+ ", editable=" + editable + ", dependent=" + dependent + ", dbColName=" + dbColName + ", depSql="
				+ depSql + ", imagePath=" + imagePath + ", imgHeight=" + imgHeight + ", imgWidth=" + imgWidth
				+ ", cellCss=" + cellCSS + ", componentCss=" + componentCss + ", saveSql=" + saveSql
				+ ", ajaxUpdateStr=" + ajaxUpdateStr + ", autoRefresh=" + autoRefresh + ", refreshInterval="
				+ refreshInterval + ", apiUrl=" + apiUrl + ", apiBody=" + apiBody + ", apiMethod=" + apiMethod
				+ ", versionNumber=" + versionNumber + ", requiredField=" + requiredField + ", expressionField="
				+ expressionField + ", disableExpression=" + disabledExpression + ", disabledExpType=" + disabledExpType
				+ ", datePattern=" + datePattern + ", pollStartExpType=" + pollStartExpType + ", pollStartExpression="
				+ pollStartExpression + ", pollStopExpType=" + pollStopExpType + ", pollStopExpression="
				+ pollStopExpression + ", buttonIcon=" + buttonIcon + ", buttonKeyValue=" + buttonKeyValue
				+ ", buttonArguments=" + buttonArguments + ", buttonLogic=" + buttonLogic + ", buttonClassName="
				+ buttonClassName + ", buttonType=" + buttonType + ", globalSql=" + globalSql + "]";
	}

}
