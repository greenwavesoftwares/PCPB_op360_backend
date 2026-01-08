package in.co.greenwave.dto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import in.co.greenwave.entity.CellInfo;
import in.co.greenwave.entity.DropDownInfo;

/**
 * This is a Data Transfer Object (DTO) used to hold cell information from @CellInfo entity.
 */
public class CellDto {

	// List of drop-down options that might be associated with this cell
	private List<DropDownInfo> dropDownFields = new ArrayList<DropDownInfo>();

	// List of fields that depend on the value of this cell
	private List<CellDto> dependentFields;

	// Unique ID of the cell
	private String cellId;

	// Alias or alternative ID for the cell
	private String aliasId;

	// Number of columns this cell will span
	private int colSpan = 1;

	// Number of rows this cell will span
	private int rowSpan = 1;

	// Value stored in this cell
	private String value;

	// Type of the field, defaulting to "Text"
	private String fieldType = "Text";

	// Date associated with this cell
	private Date date;

	// Pattern to format dates, defaulting to "dd/MM/yyyy"
	private String datepattern = "dd/MM/yyyy";

	// Minimum numeric value allowed in the cell
	private double minVal = -999D;

	// Maximum numeric value allowed in the cell
	private double maxVal = 999D;

	// Number of decimal points for numeric values
	private int decimalPoints = 2;

	// Determines if the cell can be edited
	private boolean editable = false;

	// Determines if the cell depends on other fields
	private boolean dependent = false;

	// Logic expression to evaluate if this cell depends on others
	private String depExpressionLogic = "";

	// Height and width of an image, if used in the cell
	private int imgHeight = 30;
	private int imgWidth = 30;

	// CSS style for the cell, defaulting to centered text alignment
	private String cellCSS = "text-align:center;";

	// Whether the cell is required or not
	private boolean requiredfield = false;

	// Logic to determine when the cell should be disabled
	private String disableExpressionLogic = "";

	// Whether the cell is currently disabled
	private boolean disabled = false;

	// Type of expression used to check if the cell is disabled (SQL or Expression)
	private String disabledExpType;

	// Time interval for polling (in milliseconds)
	private int pollInterval;

	// Type of expression used for starting polling (SQL or Expression)
	private String pollStartExpType;

	// Logic expression for starting polling
	private String pollStartExpressionLogic;

	// Type of expression used for stopping polling (SQL or Expression)
	private String pollStopExpType;

	// Logic expression for stopping polling
	private String pollStopExpressionLogic;

	// HTTP method for an API call related to the cell (GET, POST, etc.)
	private String apiMethod;

	// URL for an API call related to the cell
	private String apiUrl;

	// Body of the API call (usually in JSON format)
	private String apiBody;

	// Type of expression used for dependencies (SQL or Expression)
	private String depExpressionType;

	// If true, the cell will automatically refresh itself
	private boolean autoRefresh;

	// Row number where the cell is located
	private int rowNum;

	// Column number where the cell is located
	private int colNum;

	// Icon associated with a button in the cell, if any
	private String buttonIcon;

	// Key value associated with a button, if any
	private String buttonKeyValue;

	// Logic that determines what happens when a button is clicked
	private String buttonLogic;

	// CSS class name for styling the button
	private String buttonClassName;

	// Type of the button (e.g., submit, reset)
	private String buttonType;

	// Global logic expression applied to the cell
	private String globalExpressionLogic;

	private String imagePath;

	// Content of a file, if attached to the cell
	private byte[] fileContent;

	//added by ashok
	private String url;

	//Added by Ashok
	private String globalCellExpType;

	//Added by Tredipta Instructed by Ashok
	private String context;

	// Default constructor to set date patterns based on the field type
	public CellDto() {
		if (fieldType != null && fieldType.equals("DateTime"))
			datepattern = "dd/MM/yyyy HH:mm:ss";  // Format for DateTime fields
		else if (fieldType != null && fieldType.equals("Date"))
			datepattern = "dd/MM/yyyy";  // Format for Date fields
		else if (fieldType != null && fieldType.equals("Time"))
			datepattern = "HH:mm:ss";  // Format for Time fields
	}

	//added by ashok
	public CellDto(String cellId, String aliasId, int colSpan, int rowSpan) {
		super();
		this.cellId = cellId;
		this.aliasId = aliasId;
		this.colSpan = colSpan;
		this.rowSpan = rowSpan;
	}
	//added by ashok
	public CellDto(String cellId, String aliasId, int colSpan, int rowSpan,int rowNum, int colNum,String cellCSS) {
		super();
		this.cellId = cellId;
		this.aliasId = aliasId;
		this.colSpan = colSpan;
		this.rowSpan = rowSpan;
		this.rowNum = rowNum;
		this.colNum = colNum;
		this.cellCSS = cellCSS;
	}

	//constructure with all fields added by ashok excep globalCellexpType
	public CellDto(List<DropDownInfo> dropDownFields, List<CellDto> dependentFields, String cellId, String aliasId,
			int colSpan, int rowSpan, String value, String fieldType, Date date, String datepattern, double minVal,
			double maxVal, int decimalPoints, boolean editable, boolean dependent, String depExpressionLogic,
			int imgHeight, int imgWidth, String cellCSS, boolean requiredfield, String disableExpressionLogic,
			boolean disabled, String disabledExpType, int pollInterval, String pollStartExpType,
			String pollStartExpressionLogic, String pollStopExpType, String pollStopExpressionLogic, String apiMethod,
			String apiUrl, String apiBody, String depExpressionType, boolean autoRefresh, int rowNum, int colNum,
			String buttonIcon, String buttonKeyValue, String buttonLogic, String buttonClassName, String buttonType,
			String globalExpressionLogic, byte[] fileContent, String url) {
		super();
		this.dropDownFields = dropDownFields;
		this.dependentFields = dependentFields;
		this.cellId = cellId;
		this.aliasId = aliasId;
		this.colSpan = colSpan;
		this.rowSpan = rowSpan;
		this.value = value;
		this.fieldType = fieldType;
		this.date = date;
		this.datepattern = datepattern;
		this.minVal = minVal;
		this.maxVal = maxVal;
		this.decimalPoints = decimalPoints;
		this.editable = editable;
		this.dependent = dependent;
		this.depExpressionLogic = depExpressionLogic;
		this.imgHeight = imgHeight;
		this.imgWidth = imgWidth;
		this.cellCSS = cellCSS;
		this.requiredfield = requiredfield;
		this.disableExpressionLogic = disableExpressionLogic;
		this.disabled = disabled;
		this.disabledExpType = disabledExpType;
		this.pollInterval = pollInterval;
		this.pollStartExpType = pollStartExpType;
		this.pollStartExpressionLogic = pollStartExpressionLogic;
		this.pollStopExpType = pollStopExpType;
		this.pollStopExpressionLogic = pollStopExpressionLogic;
		this.apiMethod = apiMethod;
		this.apiUrl = apiUrl;
		this.apiBody = apiBody;
		this.depExpressionType = depExpressionType;
		this.autoRefresh = autoRefresh;
		this.rowNum = rowNum;
		this.colNum = colNum;
		this.buttonIcon = buttonIcon;
		this.buttonKeyValue = buttonKeyValue;
		this.buttonLogic = buttonLogic;
		this.buttonClassName = buttonClassName;
		this.buttonType = buttonType;
		this.globalExpressionLogic = globalExpressionLogic;
		this.fileContent = fileContent;
		this.url = url;
	}


	//constructure with all fields added by ashok
	public CellDto(List<DropDownInfo> dropDownFields, List<CellDto> dependentFields, String cellId, String aliasId,
			int colSpan, int rowSpan, String value, String fieldType, Date date, String datepattern, double minVal,
			double maxVal, int decimalPoints, boolean editable, boolean dependent, String depExpressionLogic,
			int imgHeight, int imgWidth, String cellCSS, boolean requiredfield, String disableExpressionLogic,
			boolean disabled, String disabledExpType, int pollInterval, String pollStartExpType,
			String pollStartExpressionLogic, String pollStopExpType, String pollStopExpressionLogic, String apiMethod,
			String apiUrl, String apiBody, String depExpressionType, boolean autoRefresh, int rowNum, int colNum,
			String buttonIcon, String buttonKeyValue, String buttonLogic, String buttonClassName, String buttonType,
			String globalExpressionLogic, byte[] fileContent, String url, String globalCellExpType) {
		super();
		this.dropDownFields = dropDownFields;
		this.dependentFields = dependentFields;
		this.cellId = cellId;
		this.aliasId = aliasId;
		this.colSpan = colSpan;
		this.rowSpan = rowSpan;
		this.value = value;
		this.fieldType = fieldType;
		this.date = date;
		this.datepattern = datepattern;
		this.minVal = minVal;
		this.maxVal = maxVal;
		this.decimalPoints = decimalPoints;
		this.editable = editable;
		this.dependent = dependent;
		this.depExpressionLogic = depExpressionLogic;
		this.imgHeight = imgHeight;
		this.imgWidth = imgWidth;
		this.cellCSS = cellCSS;
		this.requiredfield = requiredfield;
		this.disableExpressionLogic = disableExpressionLogic;
		this.disabled = disabled;
		this.disabledExpType = disabledExpType;
		this.pollInterval = pollInterval;
		this.pollStartExpType = pollStartExpType;
		this.pollStartExpressionLogic = pollStartExpressionLogic;
		this.pollStopExpType = pollStopExpType;
		this.pollStopExpressionLogic = pollStopExpressionLogic;
		this.apiMethod = apiMethod;
		this.apiUrl = apiUrl;
		this.apiBody = apiBody;
		this.depExpressionType = depExpressionType;
		this.autoRefresh = autoRefresh;
		this.rowNum = rowNum;
		this.colNum = colNum;
		this.buttonIcon = buttonIcon;
		this.buttonKeyValue = buttonKeyValue;
		this.buttonLogic = buttonLogic;
		this.buttonClassName = buttonClassName;
		this.buttonType = buttonType;
		this.globalExpressionLogic = globalExpressionLogic;
		this.fileContent = fileContent;
		this.url = url;
		this.globalCellExpType = globalCellExpType;
	}



	// Constructor to initialize CellDto from CellInfo entity
	public CellDto(CellInfo cellInfo) {
		this.cellId = cellInfo.getCellId();  // Set cellId from entity
		this.aliasId = cellInfo.getAliasId();  // Set aliasId from entity
		this.colSpan = cellInfo.getColsSpan();  // Set column span
		this.rowSpan = cellInfo.getRowSpan();  // Set row span
		this.fieldType = cellInfo.getFieldType();  // Set field type (e.g., Text, Date)
		this.minVal = cellInfo.getNumMinVal() != null ? cellInfo.getNumMinVal() : -999D;  // Set minimum value
		this.maxVal = cellInfo.getNumMaxVal() != null ? cellInfo.getNumMaxVal() : 999D;  // Set maximum value
		this.decimalPoints = cellInfo.getNumDecimalPoint();  // Set decimal points
		this.editable = cellInfo.getEditable();  // Set if the cell is editable
		this.dependent = cellInfo.getDependent();  // Set if the cell depends on other fields
		this.cellCSS = cellInfo.getCellCss();  // Set CSS styles
		this.apiUrl = cellInfo.getApiUrl();  // Set API URL for cell
		this.apiBody = cellInfo.getApiBody();  // Set API body for cell
		this.apiMethod = cellInfo.getApiMethod();  // Set HTTP method for API
		this.rowNum = cellInfo.getRowNum();  // Set row number of the cell
		this.colNum = cellInfo.getColNum();  // Set column number of the cell
		this.requiredfield = cellInfo.getRequiredField();  // Set if the cell is required
		this.disableExpressionLogic = cellInfo.getDisableExpression();  // Set disable logic
		this.disabledExpType = cellInfo.getDisabledExpType() ? "SQL" : "Expression";  // Set disable expression type
		this.datepattern = cellInfo.getDatePattern();  // Set date pattern
		this.pollStartExpType = cellInfo.getPollStartExpType() ? "SQL" : "Expression";  // Set polling start type
		this.pollStartExpressionLogic = cellInfo.getPollStartExpression();  // Set polling start logic
		this.pollStopExpType = cellInfo.getPollStopExpType() ? "SQL" : "Expression";  // Set polling stop type
		this.pollStopExpressionLogic = cellInfo.getPollStopExpression();  // Set polling stop logic
		this.buttonIcon = cellInfo.getButtonIcon();  // Set button icon
		this.buttonKeyValue = cellInfo.getButtonKeyValue();  // Set button key value
		this.buttonLogic = cellInfo.getButtonLogic();  // Set button logic
		this.buttonClassName = cellInfo.getButtonClassName();  // Set button class name
		this.buttonType = cellInfo.getButtonType();  // Set button type
		this.fileContent = cellInfo.getFileContent();  // Set file content if present
		this.depExpressionType = cellInfo.getExpressionField() ? "SQL" : "Expression";  // Set dependency type

		// Initialize dependentFields and dropDownFields if required
		this.dependentFields = new ArrayList<>();
		this.dropDownFields = new ArrayList<>();

		// If additional logic is needed for dependentFields or dropDownFields, you can implement that here.
	}


	// Full constructor to initialize all fields in CellDto
	public CellDto(List<DropDownInfo> dropDownFields, List<CellDto> dependentFields, String cellId, String aliasId,
			int colSpan, int rowSpan, String value, String fieldType, Date date, String datepattern, double minVal,
			double maxVal, int decimalPoints, boolean editable, boolean dependent, String depExpressionLogic,
			int imgHeight, int imgWidth, String cellCSS, boolean requiredfield, String disableExpressionLogic,
			boolean disabled, String disabledExpType, int pollInterval, String pollStartExpType,
			String pollStartExpressionLogic, String pollStopExpType, String pollStopExpressionLogic, String apiMethod,
			String apiUrl, String apiBody, String depExpressionType, boolean autoRefresh, int rowNum, int colNum,
			String buttonIcon, String buttonKeyValue, String buttonLogic, String buttonClassName, String buttonType,
			String globalExpressionLogic, byte[] fileContent) {
		super();
		this.dropDownFields = dropDownFields;
		this.dependentFields = dependentFields;
		this.cellId = cellId;
		this.aliasId = aliasId;
		this.colSpan = colSpan;
		this.rowSpan = rowSpan;
		this.value = value;
		this.fieldType = fieldType;
		this.date = date;
		this.datepattern = datepattern;
		this.minVal = minVal;
		this.maxVal = maxVal;
		this.decimalPoints = decimalPoints;
		this.editable = editable;
		this.dependent = dependent;
		this.depExpressionLogic = depExpressionLogic;
		this.imgHeight = imgHeight;
		this.imgWidth = imgWidth;
		this.cellCSS = cellCSS;
		this.requiredfield = requiredfield;
		this.disableExpressionLogic = disableExpressionLogic;
		this.disabled = disabled;
		this.disabledExpType = disabledExpType;
		this.pollInterval = pollInterval;
		this.pollStartExpType = pollStartExpType;
		this.pollStartExpressionLogic = pollStartExpressionLogic;
		this.pollStopExpType = pollStopExpType;
		this.pollStopExpressionLogic = pollStopExpressionLogic;
		this.apiMethod = apiMethod;
		this.apiUrl = apiUrl;
		this.apiBody = apiBody;
		this.depExpressionType = depExpressionType;
		this.autoRefresh = autoRefresh;
		this.rowNum = rowNum;
		this.colNum = colNum;
		this.buttonIcon = buttonIcon;
		this.buttonKeyValue = buttonKeyValue;
		this.buttonLogic = buttonLogic;
		this.buttonClassName = buttonClassName;
		this.buttonType = buttonType;
		this.globalExpressionLogic = globalExpressionLogic;
		this.fileContent = fileContent;
	}


	public CellDto(List<DropDownInfo> dropDownFields, List<CellDto> dependentFields, String cellId, String aliasId,
			int colSpan, int rowSpan, String value, String fieldType, Date date, String datepattern, double minVal,
			double maxVal, int decimalPoints, boolean editable, boolean dependent, String depExpressionLogic,
			int imgHeight, int imgWidth, String cellCSS, boolean requiredfield, String disableExpressionLogic,
			boolean disabled, String disabledExpType, int pollInterval, String pollStartExpType,
			String pollStartExpressionLogic, String pollStopExpType, String pollStopExpressionLogic, String apiMethod,
			String apiUrl, String apiBody, String depExpressionType, boolean autoRefresh, int rowNum, int colNum,
			String buttonIcon, String buttonKeyValue, String buttonLogic, String buttonClassName, String buttonType,
			String globalExpressionLogic, String imagePath, byte[] fileContent, String url,
			String globalCellExpType) {
		super();
		this.dropDownFields = dropDownFields;
		this.dependentFields = dependentFields;
		this.cellId = cellId;
		this.aliasId = aliasId;
		this.colSpan = colSpan;
		this.rowSpan = rowSpan;
		this.value = value;
		this.fieldType = fieldType;
		this.date = date;
		this.datepattern = datepattern;
		this.minVal = minVal;
		this.maxVal = maxVal;
		this.decimalPoints = decimalPoints;
		this.editable = editable;
		this.dependent = dependent;
		this.depExpressionLogic = depExpressionLogic;
		this.imgHeight = imgHeight;
		this.imgWidth = imgWidth;
		this.cellCSS = cellCSS;
		this.requiredfield = requiredfield;
		this.disableExpressionLogic = disableExpressionLogic;
		this.disabled = disabled;
		this.disabledExpType = disabledExpType;
		this.pollInterval = pollInterval;
		this.pollStartExpType = pollStartExpType;
		this.pollStartExpressionLogic = pollStartExpressionLogic;
		this.pollStopExpType = pollStopExpType;
		this.pollStopExpressionLogic = pollStopExpressionLogic;
		this.apiMethod = apiMethod;
		this.apiUrl = apiUrl;
		this.apiBody = apiBody;
		this.depExpressionType = depExpressionType;
		this.autoRefresh = autoRefresh;
		this.rowNum = rowNum;
		this.colNum = colNum;
		this.buttonIcon = buttonIcon;
		this.buttonKeyValue = buttonKeyValue;
		this.buttonLogic = buttonLogic;
		this.buttonClassName = buttonClassName;
		this.buttonType = buttonType;
		this.globalExpressionLogic = globalExpressionLogic;
		this.imagePath = imagePath;
		this.fileContent = fileContent;
		this.url = url;
		this.globalCellExpType = globalCellExpType;
	}

	public CellDto(List<DropDownInfo> dropDownFields, List<CellDto> dependentFields, String cellId, String aliasId,
			int colSpan, int rowSpan, String value, String fieldType, Date date, String datepattern, double minVal,
			double maxVal, int decimalPoints, boolean editable, boolean dependent, String depExpressionLogic,
			int imgHeight, int imgWidth, String cellCSS, boolean requiredfield, String disableExpressionLogic,
			boolean disabled, String disabledExpType, int pollInterval, String pollStartExpType,
			String pollStartExpressionLogic, String pollStopExpType, String pollStopExpressionLogic, String apiMethod,
			String apiUrl, String apiBody, String depExpressionType, boolean autoRefresh, int rowNum, int colNum,
			String buttonIcon, String buttonKeyValue, String buttonLogic, String buttonClassName, String buttonType,
			String globalExpressionLogic, String imagePath, byte[] fileContent, String url, String globalCellExpType,
			String context) {
		super();
		this.dropDownFields = dropDownFields;
		this.dependentFields = dependentFields;
		this.cellId = cellId;
		this.aliasId = aliasId;
		this.colSpan = colSpan;
		this.rowSpan = rowSpan;
		this.value = value;
		this.fieldType = fieldType;
		this.date = date;
		this.datepattern = datepattern;
		this.minVal = minVal;
		this.maxVal = maxVal;
		this.decimalPoints = decimalPoints;
		this.editable = editable;
		this.dependent = dependent;
		this.depExpressionLogic = depExpressionLogic;
		this.imgHeight = imgHeight;
		this.imgWidth = imgWidth;
		this.cellCSS = cellCSS;
		this.requiredfield = requiredfield;
		this.disableExpressionLogic = disableExpressionLogic;
		this.disabled = disabled;
		this.disabledExpType = disabledExpType;
		this.pollInterval = pollInterval;
		this.pollStartExpType = pollStartExpType;
		this.pollStartExpressionLogic = pollStartExpressionLogic;
		this.pollStopExpType = pollStopExpType;
		this.pollStopExpressionLogic = pollStopExpressionLogic;
		this.apiMethod = apiMethod;
		this.apiUrl = apiUrl;
		this.apiBody = apiBody;
		this.depExpressionType = depExpressionType;
		this.autoRefresh = autoRefresh;
		this.rowNum = rowNum;
		this.colNum = colNum;
		this.buttonIcon = buttonIcon;
		this.buttonKeyValue = buttonKeyValue;
		this.buttonLogic = buttonLogic;
		this.buttonClassName = buttonClassName;
		this.buttonType = buttonType;
		this.globalExpressionLogic = globalExpressionLogic;
		this.imagePath = imagePath;
		this.fileContent = fileContent;
		this.url = url;
		this.globalCellExpType = globalCellExpType;
		this.context = context;
	}

	// Getter and Setters fors all the fields of CellDto
	public List<DropDownInfo> getDropDownFields() {
		return dropDownFields;
	}


	public void setDropDownFields(List<DropDownInfo> dropDownFields) {
		this.dropDownFields = dropDownFields;
	}


	public List<CellDto> getDependentFields() {
		return dependentFields;
	}


	public void setDependentFields(List<CellDto> dependentFields) {
		this.dependentFields = dependentFields;
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


	public int getColSpan() {
		return colSpan;
	}


	public void setColSpan(int colSpan) {
		this.colSpan = colSpan;
	}


	public int getRowSpan() {
		return rowSpan;
	}


	public void setRowSpan(int rowSpan) {
		this.rowSpan = rowSpan;
	}


	public String getValue() {
		return value;
	}


	public void setValue(String value) {
		this.value = value;
	}


	public String getFieldType() {
		return fieldType;
	}


	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}


	public Date getDate() {
		return date;
	}


	public void setDate(Date date) {
		this.date = date;
	}


	public String getDatepattern() {
		return datepattern;
	}


	public void setDatepattern(String datepattern) {
		this.datepattern = datepattern;
	}


	public double getMinVal() {
		return minVal;
	}


	public void setMinVal(double minVal) {
		this.minVal = minVal;
	}


	public double getMaxVal() {
		return maxVal;
	}


	public void setMaxVal(double maxVal) {
		this.maxVal = maxVal;
	}


	public int getDecimalPoints() {
		return decimalPoints;
	}


	public void setDecimalPoints(int decimalPoints) {
		this.decimalPoints = decimalPoints;
	}


	public boolean isEditable() {
		return editable;
	}


	public void setEditable(boolean editable) {
		this.editable = editable;
	}


	public boolean isDependent() {
		return dependent;
	}


	public void setDependent(boolean dependent) {
		this.dependent = dependent;
	}


	public String getDepExpressionLogic() {
		return depExpressionLogic;
	}


	public void setDepExpressionLogic(String depExpressionLogic) {
		this.depExpressionLogic = depExpressionLogic;
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


	public String getCellCSS() {
		return cellCSS;
	}


	public void setCellCSS(String cellCSS) {
		this.cellCSS = cellCSS;
	}


	public boolean isRequiredfield() {
		return requiredfield;
	}


	public void setRequiredfield(boolean requiredfield) {
		this.requiredfield = requiredfield;
	}


	public String getDisableExpressionLogic() {
		return disableExpressionLogic;
	}


	public void setDisableExpressionLogic(String disableExpressionLogic) {
		this.disableExpressionLogic = disableExpressionLogic;
	}


	public boolean isDisabled() {
		return disabled;
	}


	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}


	public String getDisabledExpType() {
		return disabledExpType;
	}


	public void setDisabledExpType(String disabledExpType) {
		this.disabledExpType = disabledExpType;
	}


	public int getPollInterval() {
		return pollInterval;
	}


	public void setPollInterval(int pollInterval) {
		this.pollInterval = pollInterval;
	}


	public String getPollStartExpType() {
		return pollStartExpType;
	}


	public void setPollStartExpType(String pollStartExpType) {
		this.pollStartExpType = pollStartExpType;
	}


	public String getPollStartExpressionLogic() {
		return pollStartExpressionLogic;
	}


	public void setPollStartExpressionLogic(String pollStartExpressionLogic) {
		this.pollStartExpressionLogic = pollStartExpressionLogic;
	}


	public String getPollStopExpType() {
		return pollStopExpType;
	}


	public void setPollStopExpType(String pollStopExpType) {
		this.pollStopExpType = pollStopExpType;
	}


	public String getPollStopExpressionLogic() {
		return pollStopExpressionLogic;
	}


	public void setPollStopExpressionLogic(String pollStopExpressionLogic) {
		this.pollStopExpressionLogic = pollStopExpressionLogic;
	}


	public String getApiMethod() {
		return apiMethod;
	}


	public void setApiMethod(String apiMethod) {
		this.apiMethod = apiMethod;
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


	public String getDepExpressionType() {
		return depExpressionType;
	}


	public void setDepExpressionType(String depExpressionType) {
		this.depExpressionType = depExpressionType;
	}


	public boolean isAutoRefresh() {
		return autoRefresh;
	}


	public void setAutoRefresh(boolean autoRefresh) {
		this.autoRefresh = autoRefresh;
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


	public String getGlobalExpressionLogic() {
		return globalExpressionLogic;
	}


	public void setGlobalExpressionLogic(String globalExpressionLogic) {
		this.globalExpressionLogic = globalExpressionLogic;
	}


	public byte[] getFileContent() {
		return fileContent;
	}


	public void setFileContent(byte[] fileContent) {
		this.fileContent = fileContent;
	}


	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}



	public String getGlobalCellExpType() {
		return globalCellExpType;
	}

	public void setGlobalCellExpType(String globalCellExpType) {
		this.globalCellExpType = globalCellExpType;
	}


	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	// This method returns a string representation of the CellDto object.
	// It helps when we want to print or log the object, showing the content of the CellDto.
	@Override
	public String toString() {
		return "CellDto [dropDownFields=" + dropDownFields + ", dependentFields=" + dependentFields + ", cellId="
				+ cellId + ", aliasId=" + aliasId + ", colSpan=" + colSpan + ", rowSpan=" + rowSpan + ", value=" + value
				+ ", fieldType=" + fieldType + ", date=" + date + ", datepattern=" + datepattern + ", minVal=" + minVal
				+ ", maxVal=" + maxVal + ", decimalPoints=" + decimalPoints + ", editable=" + editable + ", dependent="
				+ dependent + ", depExpressionLogic=" + depExpressionLogic + ", imgHeight=" + imgHeight + ", imgWidth="
				+ imgWidth + ", cellCSS=" + cellCSS + ", requiredfield=" + requiredfield + ", disableExpressionLogic="
				+ disableExpressionLogic + ", disabled=" + disabled + ", disabledExpType=" + disabledExpType
				+ ", pollInterval=" + pollInterval + ", pollStartExpType=" + pollStartExpType
				+ ", pollStartExpressionLogic=" + pollStartExpressionLogic + ", pollStopExpType=" + pollStopExpType
				+ ", pollStopExpressionLogic=" + pollStopExpressionLogic + ", apiMethod=" + apiMethod + ", apiUrl="
				+ apiUrl + ", apiBody=" + apiBody + ", depExpressionType=" + depExpressionType + ", autoRefresh="
				+ autoRefresh + ", rowNum=" + rowNum + ", colNum=" + colNum + ", buttonIcon=" + buttonIcon
				+ ", buttonKeyValue=" + buttonKeyValue + ", buttonLogic=" + buttonLogic + ", buttonClassName="
				+ buttonClassName + ", buttonType=" + buttonType + ", globalExpressionLogic=" + globalExpressionLogic
				+ ", imagePath=" + imagePath + ", fileContent=" + Arrays.toString(fileContent) + ", url=" + url
				+ ", globalCellExpType=" + globalCellExpType + ", context=" + context + "]";
	}
}
