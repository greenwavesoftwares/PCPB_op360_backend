package in.co.greenwave.service;

import java.sql.Types;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import in.co.greenwave.dao.LogbookConfigureService;
import in.co.greenwave.dto.CellDto;
import in.co.greenwave.dto.FormDto;
import in.co.greenwave.entity.DropDownInfo;
import in.co.greenwave.entity.TenantConfigDetails;
import in.co.greenwave.util.JdbcUrlUtil;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
@Repository
public class LogbookConfigureServiceImpl implements LogbookConfigureService{
	
	@Autowired
	@Qualifier("DatasourceCollections")
	Map<String, List<JdbcTemplate>> jdbcTemplateCollection; // Template for operations on the main database
	
	@Autowired
	@Qualifier("jdbcTemplate_OP360_tenant")
	JdbcTemplate jdbcTemplateOp360Tenant; // Template for Tenant database operations

	@Override
	public Map<String, List<FormDto>> readExcel(MultipartFile file) {
		List<FormDto> formModel = new ArrayList<>();
		Map<String, List<FormDto>> sheetFormMap = new LinkedHashMap<>();
		Workbook workbook;

		try {
			workbook = WorkbookFactory.create(file.getInputStream());

			// Iterating over sheets
			for (Sheet sheet : workbook) {
				System.out.println("Processing Sheet: " + sheet.getSheetName());
				formModel = new LinkedList<>();

				// Iterating over rows
				for (Row row : sheet) {
					List<CellDto> tabCells = new LinkedList<>();
					System.out.println("Row------------" + (row.getRowNum() + 1));

					// Iterating over cells
					for (Cell cell : row) {

						String cellValue = new DataFormatter().formatCellValue(cell);
						//System.out.println("cell Value : "+cellValue);
						//System.out.println("column Index: "+cell.getColumnIndex()+"row index: "+cell.getRowIndex());
						if (!cellValue.trim().isEmpty()) {
							// Handling merged regions
							int rowSpan = 1, colSpan = 1;
							CellRangeAddress range = getMergedRegionForCell(cell);
							if (range != null) {
								rowSpan = range.getLastRow() - range.getFirstRow() + 1;
								colSpan = range.getLastColumn() - range.getFirstColumn() + 1;
							}

							// Get cell styles for CSS output
							CellStyle style = cell.getCellStyle();
							String textColor = getFontColorHex(style, workbook);
							String fillColor = getFillColorHex(style, workbook);

							//	                        System.out.printf("Cell [%d, %d]: text-color: #%s; fill-color: #%s;%n",
							//	                                cell.getRowIndex() + 1, cell.getColumnIndex() + 1,
							//	                                textColor, fillColor);

							String excelCellCSS = String.format("text-align: center; color: #%s; background-color: #%s;", textColor, fillColor);
							//	                        System.out.println("Generated CSS: " + css);
							// Create CellDto and handle comments
							System.out.println("");
							CellDto gridCell = new CellDto("_" + (cell.getRowIndex() + 1) + "_" + (cell.getColumnIndex() + 1),
									cellValue, colSpan, rowSpan, (cell.getRowIndex() + 1), (cell.getColumnIndex() + 1),excelCellCSS);
							//System.out.println("my id : "+""+(columnToLetter((cell.getColumnIndex() + 1))+(cell.getRowIndex() + 1)));
							//	                        CellDto gridCell = new CellDto(""+(columnToLetter((cell.getColumnIndex() + 1))+(cell.getRowIndex() + 1)),
							//	                                cellValue, colSpan, rowSpan, (cell.getRowIndex() + 1), (cell.getColumnIndex() + 1),excelCellCSS);

							String comment = getCellComment(cell);
							if (!comment.trim().isEmpty()) {
								gridCell = setcellAccordingtoComment(gridCell, comment);
							}

							tabCells.add(gridCell);
						}
					}

					if (!tabCells.isEmpty()) {
						formModel.add(new FormDto(tabCells));
					}
				}

				sheetFormMap.put(sheet.getSheetName(), formModel);
			}

			workbook.close();
			System.out.println("Excel reading completed.");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return sheetFormMap;
	}

	/**
	 * Extracts font (text) color in HEX format.
	 */
	private String getFontColorHex(CellStyle style, Workbook workbook) {
		Font font = workbook.getFontAt(style.getFontIndexAsInt());
		if (font instanceof XSSFFont) { // For .xlsx files
			XSSFFont xssfFont = (XSSFFont) font;
			XSSFColor color = xssfFont.getXSSFColor();
			return color != null ? color.getARGBHex().substring(2) : "default";
		}
		// For .xls files (indexed colors)
		short colorIndex = font.getColor();
		if (workbook instanceof HSSFWorkbook) {
			HSSFPalette palette = ((HSSFWorkbook) workbook).getCustomPalette();
			HSSFColor hssfColor = palette.getColor(colorIndex);
			return hssfColor != null ? getHSSFColorHex(hssfColor) : "default";
		}
		return "default";
	}

	/**
	 * Extracts fill (background) color in HEX format.
	 */
	private String getFillColorHex(CellStyle style, Workbook workbook) {
		if (style.getFillForegroundColorColor() instanceof XSSFColor) {
			XSSFColor xssfColor = (XSSFColor) style.getFillForegroundColorColor();
			return xssfColor != null ? xssfColor.getARGBHex().substring(2) : "transparent";
		}
		// For .xls files (indexed colors)
		short colorIndex = style.getFillForegroundColor();
		if (workbook instanceof HSSFWorkbook) {
			HSSFPalette palette = ((HSSFWorkbook) workbook).getCustomPalette();
			HSSFColor hssfColor = palette.getColor(colorIndex);
			return hssfColor != null ? getHSSFColorHex(hssfColor) : "transparent";
		}
		return "transparent";
	}

	/**
	 * Converts HSSFColor to HEX format.
	 */
	private String getHSSFColorHex(HSSFColor hssfColor) {
		short[] rgb = hssfColor.getTriplet();
		return String.format("#%02x%02x%02x", rgb[0], rgb[1], rgb[2]);
	}

	public String getCellComment(Cell cell) {
		String cellComment = "";
		try {
			if(cell.getCellComment()!= null) {
				String cellAuthor = cell.getCellComment().getAuthor();
				if(cellAuthor!= null)
					cellComment = cell.getCellComment().getString().getString().replaceAll(cellAuthor+":", "");
				else
					cellComment = cell.getCellComment().getString().getString();
				//				System.out.println(cell.getCellComment().getAuthor()+"Cell comment for "+cell.getAddress()+" is: "+cellComment);
			}
		}catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return cellComment;
	}
	public CellRangeAddress getMergedRegionForCell(Cell c) {
		Sheet s = c.getRow().getSheet();
		for (CellRangeAddress mergedRegion : s.getMergedRegions()) {
			if (mergedRegion.isInRange(c.getRowIndex(), c.getColumnIndex())) {
				// This region contains the cell in question
				return mergedRegion;
			}
		}
		// Not in any
		return null;
	}

	private CellDto setcellAccordingtoComment(CellDto cell , String comment) {
		if(comment.trim().equalsIgnoreCase("text")) {
			cell.setFieldType("Text");
			cell.setEditable(true);
		}else if(comment.trim().equalsIgnoreCase("number")) {
			cell.setFieldType("Number");
			cell.setEditable(true);
		}else if(comment.trim().equalsIgnoreCase("comment") || comment.trim().equalsIgnoreCase("remarks")) {
			cell.setFieldType("CommentBox");
			cell.setEditable(true);
		}else if(comment.trim().contains(",")) {
			cell.setFieldType("Dropdown");
			String[] items = comment.split(",");
			for(String item : items) {
				cell.getDropDownFields().add(new DropDownInfo(item.trim(), item.trim()));
			}
			cell.setEditable(true);
		}else if(comment.trim().equalsIgnoreCase("boolean") || comment.trim().equalsIgnoreCase("bool") || comment.trim().equalsIgnoreCase("checkbox")) {
			cell.setFieldType("Boolean");
			cell.setEditable(true);
		}else if(comment.trim().equalsIgnoreCase("Date")) {
			cell.setFieldType("Date");
			cell.setEditable(true);
		}else if(comment.trim().equalsIgnoreCase("Datetime") || comment.trim().equalsIgnoreCase("Dateandtime")) {
			cell.setFieldType("DateTime");
			cell.setEditable(true);
		}else if(comment.trim().equalsIgnoreCase("time")) {
			cell.setFieldType("Time");
			cell.setEditable(true);
		}
		return cell;
	}

	@Override
	public void saveForm(FormDto formInfo,String tenantId,boolean isUpdate) {
		

		JdbcTemplate jdbcTemplateOp360 = jdbcTemplateCollection.get(tenantId).get(0);
		
		// Insert into DigitalLogbookFormInfo
		try {


			if(isUpdate) {
				System.out.println("id : "+formInfo.getFormID()+" and Id : "+formInfo.getVersionNumber());
				//without version change
				String formInfoSQL = "UPDATE [dbo].[DigitalLogbookFormInfo] "
						+ "SET CreationDate = getDate(), "
						+ "FormName = ?, "
						+ "UserID = ?, "
						+ "SaveSQL = ?, "
						+ "TableSQL = ?, "
						+ "DeleteSQL = ?, "
						+ "CreatedUser = ?, "
						+ "Department = ?, "
						+ "UserGroup = ?, "
						+ "DocumentID = ?, "
						+ "FormatID = ?, "
						+ "VersionNumber = ?, "
						+ "isActiveForm = ?, "
						+"isPublicAccess = ?, "
						+"ModifedBy = ?, "
//						+"DashboardType = ?, "
						+"ModifiedDate = getDate(), "
						+ "TenantId = ? "
						+ "WHERE FormId = ? and VersionNumber = ?"; // Replace <PrimaryKeyColumn> with the actual column name of the unique identifier.
				int updateSql = jdbcTemplateOp360.update(formInfoSQL,
						formInfo.getFormName(),
						formInfo.getUserId(),
						formInfo.getSaveSQL(),
						formInfo.getTableSQL(),
						formInfo.getDeleteSQL(),
						formInfo.getCreatedUser(),
						formInfo.getDepartment(),
						formInfo.getUserGroup(),
						formInfo.getDocumentId(),
						formInfo.getFormatID(),
						formInfo.getVersionNumber(),
						formInfo.getIsActiveForm(),
						formInfo.isPublicAccess(),
//						formInfo.isDashboardType(),
						formInfo.getModifiedBy(),
						tenantId,
						formInfo.getFormID(),
						formInfo.getVersionNumber()); // Replace <PrimaryKeyValue> with the actual value of the unique identifier.


				String deleteCellSQL = "DELETE FROM [dbo].[DigitalLogbookCellDetails] "
						+ "WHERE FormId = ?"; // Replace <PrimaryKeyColumn> with the actual column name for the unique identifier.

				int deleteCount = jdbcTemplateOp360.update(deleteCellSQL,formInfo.getFormID() ); // Replace <PrimaryKeyValue> with the value of the unique identifier.
				if (deleteCount > 0) {
					System.out.println(deleteCount + " record(s) deleted successfully from cellDetails table.");

					for (CellDto cell : formInfo.getCellInfo()) {
						String insertCellSql = "INSERT INTO [dbo].[DigitalLogbookCellDetails] "
								+ "(FormId, RowNum, ColNum, CellId, AliasId, ColsSpan, RowSpan, FieldType, NumMinVal, NumMaxVal, "
								+ "NumDecimalPoint, Editable, Dependent, DbColName, DepSQL, GlobalSQL, ImagePath, FileContent, "
								+ "ImgHeight, ImgWidth, CellCSS, ComponentCSS, SaveSQL, AjaxUpdateStr, AutoRefresh, RefreshInterval, "
								+ "apiURL, apiBody, apiMethod, VersionNumber, requiredfield, expressionfield, disabledexpression, disabledexptype, "
								+ "datepattern, pollStartExpType, pollStartExpression, pollStopExpType, pollStopExpression, ButtonIcon, "
								+ "ButtonKeyValue, ButtonArguments, ButtonLogic, ButtonClassName, ButtonType, url,TenantId,globalCellExpType,context) "
								+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?)";

						jdbcTemplateOp360.update(insertCellSql, (PreparedStatementSetter) ps -> {
							ps.setInt(1, formInfo.getFormID());
							ps.setInt(2, cell.getRowNum());
							ps.setInt(3, cell.getColNum());
							ps.setString(4, cell.getCellId());
							ps.setString(5, cell.getAliasId());
							ps.setInt(6, cell.getColSpan());
							ps.setInt(7, cell.getRowSpan());
							ps.setString(8, cell.getFieldType()==null?"Text":cell.getFieldType());
							ps.setDouble(9, cell.getMinVal());
							ps.setDouble(10,cell.getMaxVal());
							ps.setInt(11, cell.getDecimalPoints());
							ps.setBoolean(12, cell.isEditable());
							ps.setBoolean(13, cell.isDependent());
							//		                ps.setString(14, cell.getDbColName());
							ps.setString(14, null); //instaed of DBColName
							//		                ps.setString(15, cell.getDepSql());
							//		                ps.setString(16, cell.getGlobalSql());
							//		                ps.setString(17, cell.getImagePath());
							ps.setString(15, cell.getDepExpressionLogic());//for deSql column
							ps.setString(16, cell.getGlobalExpressionLogic());//for GlobalsqlColumn
							ps.setString(17, cell.getImagePath());//for ImagePath

							// Set fileContent as a binary stream
							if (cell.getFileContent() != null) {
								ps.setBytes(18, cell.getFileContent());
							} else {
								ps.setNull(18, Types.BINARY);
							}

							ps.setInt(19, cell.getImgHeight());
							ps.setInt(20, cell.getImgWidth());
							ps.setString(21, cell.getCellCSS());
							//		                ps.setString(22, cell.getComponentCss());
							ps.setString(22, null);//form ComponentCss column
							ps.setString(23, "");// for save sql column
							ps.setString(24, null);//for [AjaxUpdateStr] column
							ps.setBoolean(25, cell.isAutoRefresh());
							ps.setInt(26, cell.getPollInterval());//RefreshInterval column
							ps.setString(27, cell.getApiUrl());
							ps.setString(28, cell.getApiBody());
							ps.setString(29, cell.getApiMethod());
							ps.setInt(30, formInfo.getVersionNumber()); //version column
							ps.setBoolean(31, cell.isRequiredfield());
							ps.setBoolean(32, ( (cell.getDepExpressionType()==null || cell.getDepExpressionType().equals("SQL") ) ?false:true)); //for expressionfield column
							ps.setString(33, cell.getDisableExpressionLogic());// for disabledexpression column
							ps.setBoolean(34, ( (cell.getDisabledExpType()==null || cell.getDisabledExpType().equals("SQL") ) ?false:true));//for DisabledExpType column
							ps.setString(35, cell.getDatepattern());
							ps.setBoolean(36, ( (cell.getPollStartExpType()==null || cell.getPollStartExpType().equals("SQL") ) ?false:true)); // for PollStartExpType column
							ps.setString(37, cell.getPollStartExpressionLogic());// for PollStartExpression column
							ps.setBoolean(38,((cell.getPollStopExpType()==null) || cell.getPollStopExpType().equals("SQL") ?false:true)); //for PollStopExpType column
							ps.setString(39, cell.getPollStopExpressionLogic());// fro PollStopExpression for column
							ps.setString(40, cell.getButtonIcon());
							ps.setString(41, cell.getButtonKeyValue());
							ps.setString(42, null);// for buttonArgument  column
							ps.setString(43, cell.getButtonLogic());
							ps.setString(44, cell.getButtonClassName());
							ps.setString(45, cell.getButtonType());
							ps.setString(46, cell.getUrl());
							ps.setString(47, tenantId);
							ps.setBoolean(48, ( (cell.getGlobalCellExpType()==null || cell.getGlobalCellExpType().equals("SQL") ) ?false:true)); //for global cell update type (SQL/API)		                
							ps.setString(49, cell.getContext());


							//		                ps.setInt(1, formInfo.getFormID());
							//		                ps.setInt(2, cell.getRowNum());
							//		                ps.setInt(3, cell.getColNum());
							//		                ps.setString(4, cell.getCellId());
							//		                ps.setString(5, cell.getAliasId());
							//		                ps.setInt(6, cell.getColSpan());
							//		                ps.setInt(7, cell.getRowSpan());
							//		                ps.setString(8, cell.getFieldType()==null?"Text":cell.getFieldType());
							//		                ps.setDouble(9, cell.getMinVal());
							//		                ps.setDouble(10,cell.getMaxVal());
							//		                ps.setInt(11, cell.getDecimalPoints());
							//		                ps.setBoolean(12, cell.isEditable());
							//		                ps.setBoolean(13, cell.isDependent());
							////		                ps.setString(14, cell.getDbColName());
							//		                ps.setString(14, null); //instaed of DBColName
							////		                ps.setString(15, cell.getDepSql());
							////		                ps.setString(16, cell.getGlobalSql());
							////		                ps.setString(17, cell.getImagePath());
							//		                ps.setString(15, cell.getDepExpressionLogic());//for deSql column
							//		                ps.setString(16, cell.getGlobalExpressionLogic());//for GlobalsqlColumn
							//		                ps.setString(17, null);//for ImagePath
							//
							//		                // Set fileContent as a binary stream
							//		                if (cell.getFileContent() != null) {
							//		                    ps.setBytes(18, cell.getFileContent());
							//		                } else {
							//		                    ps.setNull(18, Types.BINARY);
							//		                }
							//
							//		                ps.setInt(19, cell.getImgHeight());
							//		                ps.setInt(20, cell.getImgWidth());
							//		                ps.setString(21, cell.getCellCSS());
							////		                ps.setString(22, cell.getComponentCss());
							//		                ps.setString(22, null);//form ComponentCss column
							//		                ps.setString(23, "");// for save sql column
							//		                ps.setString(24, null);//for [AjaxUpdateStr] column
							//		                ps.setBoolean(25, cell.isAutoRefresh());
							//		                ps.setInt(26, 0);//RefreshInterval column
							//		                ps.setString(27, cell.getApiUrl());
							//		                ps.setString(28, cell.getApiBody());
							//		                ps.setString(29, cell.getApiMethod());
							//		                ps.setInt(30, formInfo.getVersionNumber()); //version column
							//		                ps.setBoolean(31, cell.isRequiredfield());
							//		                ps.setBoolean(32, false); //for expressionfield column
							//		                ps.setString(33, cell.getDisableExpressionLogic());// for disabledexpression column
							//		                ps.setBoolean(34, cell.isDisabled());//for DisabledExpType column
							//		                ps.setString(35, cell.getDatepattern());
							//		                ps.setBoolean(36, false); // for PollStartExpType column
							//		                ps.setString(37, cell.getPollStartExpressionLogic());// for PollStartExpression column
							//		                ps.setBoolean(38,false); //for PollStopExpType column
							//		                ps.setString(39, cell.getPollStopExpressionLogic());// fro PollStopExpression for column
							//		                ps.setString(40, cell.getButtonIcon());
							//		                ps.setString(41, cell.getButtonKeyValue());
							//		                ps.setString(42, null);// for buttonArgument  column
							//		                ps.setString(43, cell.getButtonLogic());
							//		                ps.setString(44, cell.getButtonClassName());
							//		                ps.setString(45, cell.getButtonType());
							//		                ps.setString(46, cell.getUrl());
							//		                ps.setString(47, tenantId);
						});

						System.out.println("Row inserted for Cell ID: " + cell.getCellId());
					}


				} else {
					System.out.println("No records were deleted. in CellDetails Table");
				}

				String deleteCellDropdownSQL = "DELETE FROM [dbo].[DLBFormDropdownItems] "
						+ "WHERE FormId = ?"; // Replace <PrimaryKeyColumn> with the actual column name for the unique identifier.

				int deleteDropDownCount = jdbcTemplateOp360.update(deleteCellDropdownSQL,formInfo.getFormID() ); // Replace <PrimaryKeyValue> with the value of the unique identifier.
				System.out.println(deleteDropDownCount + " record(s) deleted successfully from cell dropdown table.");


				// Insert into DLBFormDropdownItems
				for (CellDto cell : formInfo.getCellInfo()) {
					if(cell.getFieldType().equals("Dropdown"))
					{
						if(cell.getDepExpressionLogic()==null || cell.getDepExpressionLogic().isEmpty()) {

							for (DropDownInfo dropDownItem : cell.getDropDownFields()) {
								String dropDownItemsSQL = "INSERT INTO [dbo].[DLBFormDropdownItems] (FormId, CellId, ItemLabel, ItemValue, VersionNumber,TenantId) VALUES (?, ?, ?, ?, ?,?)";
								jdbcTemplateOp360.update(dropDownItemsSQL,
										formInfo.getFormID(),
										cell.getCellId(),
										dropDownItem.getItemLabel(),
										dropDownItem.getItemValue(),
										formInfo.getVersionNumber(),tenantId);
							}
						}
					}

				}	
				String deleteDepCellSQL = "DELETE FROM [dbo].[DLBFormDependentFieldInfo] "
						+ "WHERE FormId = ?"; // Replace <PrimaryKeyColumn> with the actual column name for the unique identifier.

				int deleteDepCellSQLCount = jdbcTemplateOp360.update(deleteDepCellSQL,formInfo.getFormID() ); // Replace <PrimaryKeyValue> with the value of the unique identifier.
				System.out.println(deleteDepCellSQLCount + " record(s) deleted successfully from cell Dependent table.");


				// Insert into DLBFormDropdownItems
				for (CellDto cell : formInfo.getCellInfo()) {
					if(cell.getDependentFields()!=null) {
						for(CellDto depCell : cell.getDependentFields()) {
							String insertDepCellSql = "INSERT INTO [dbo].[DLBFormDependentFieldInfo] " +
									"(FormId, DepCellId,CellId, VersionNumber,[TenantId]) VALUES (?, ?, ?, ?,?)";

							// Execute the query
							int insertDepCellSqlCount =  jdbcTemplateOp360.update(insertDepCellSql, formInfo.getFormID(), cell.getCellId(), depCell.getCellId(), formInfo.getVersionNumber(),tenantId);

						}
					}
				}
			}
			else {
				
				System.out.println("Else Part call");
				
				String lastVersionSql = "SELECT ISNULL((\r\n"
						+ "  SELECT TOP 1 [VersionNumber] \r\n"
						+ "  FROM [dbo].[DigitalLogbookFormInfo] \r\n"
						+ "  WHERE FormName = ?\r\n"
						+ "  ORDER BY [VersionNumber] DESC\r\n"
						+ "), 0) AS VersionNumber;\r\n"
						+ "";
				Integer lastVersion = jdbcTemplateOp360.queryForObject(
				    lastVersionSql,
				    Integer.class,
				    formInfo.getFormName()
				);
				System.out.println("lastVersion + 1 : " + lastVersion+1);

				String formInfoSQL = "INSERT INTO [dbo].[DigitalLogbookFormInfo] "
						+ "(CreationDate, FormName, UserID, SaveSQL, TableSQL, DeleteSQL,  CreatedUser, Department, "
						+ "UserGroup, DocumentID, FormatID, VersionNumber, isActiveForm,isPublicAccess,ModifedBy,TenantId,ModifiedDate) "
						+ "VALUES ( getDate(), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,getDate())";
				int insrtSql = jdbcTemplateOp360.update(formInfoSQL,
						// formInfo.getFormId(),
						formInfo.getFormName(),
						formInfo.getUserId(),
						formInfo.getSaveSQL(),
						formInfo.getTableSQL(),
						formInfo.getDeleteSQL(),
						// formInfo.getCreationDate(),
						formInfo.getCreatedUser(),
						formInfo.getDepartment(),
						formInfo.getUserGroup(),
						formInfo.getDocumentId(),
						formInfo.getFormatID(),
						lastVersion+1,//formInfo.getVersionNumber()
						false,formInfo.isPublicAccess(),formInfo.getModifiedBy(),tenantId);

				// Insert into DigitalLogbookCellDetails
				System.out.println("insert count : "+insrtSql);

				String getLastFormId = "SELECT TOP 1 [FormId] FROM [dbo].[DigitalLogbookFormInfo] ORDER BY FormId DESC";

				Integer lastFormId = jdbcTemplateOp360.queryForObject(getLastFormId, Integer.class);
				System.out.println("lastFormId : "+lastFormId);



				for (CellDto cell : formInfo.getCellInfo()) {
					String insertCellSql = "INSERT INTO [dbo].[DigitalLogbookCellDetails] "
							+ "(FormId, RowNum, ColNum, CellId, AliasId, ColsSpan, RowSpan, FieldType, NumMinVal, NumMaxVal, "
							+ "NumDecimalPoint, Editable, Dependent, DbColName, DepSQL, GlobalSQL, ImagePath, FileContent, "
							+ "ImgHeight, ImgWidth, CellCSS, ComponentCSS, SaveSQL, AjaxUpdateStr, AutoRefresh, RefreshInterval, "
							+ "apiURL, apiBody, apiMethod, VersionNumber, requiredfield, expressionfield, disabledexpression, disabledexptype, "
							+ "datepattern, pollStartExpType, pollStartExpression, pollStopExpType, pollStopExpression, ButtonIcon, "
							+ "ButtonKeyValue, ButtonArguments, ButtonLogic, ButtonClassName, ButtonType, url,TenantId,globalCellExpType,context) "
							+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?)";

					jdbcTemplateOp360.update(insertCellSql, (PreparedStatementSetter) ps -> {
						ps.setInt(1, lastFormId);
						ps.setInt(2, cell.getRowNum());
						ps.setInt(3, cell.getColNum());
						ps.setString(4, cell.getCellId());
						ps.setString(5, cell.getAliasId());
						ps.setInt(6, cell.getColSpan());
						ps.setInt(7, cell.getRowSpan());
						ps.setString(8, cell.getFieldType()==null?"Text":cell.getFieldType());
						ps.setDouble(9, cell.getMinVal());
						ps.setDouble(10,cell.getMaxVal());
						ps.setInt(11, cell.getDecimalPoints());
						ps.setBoolean(12, cell.isEditable());
						ps.setBoolean(13, cell.isDependent());
						//                ps.setString(14, cell.getDbColName());
						ps.setString(14, null); //instaed of DBColName
						//                ps.setString(15, cell.getDepSql());
						//                ps.setString(16, cell.getGlobalSql());
						//                ps.setString(17, cell.getImagePath());
						ps.setString(15, cell.getDepExpressionLogic());//for deSql column
						ps.setString(16, cell.getGlobalExpressionLogic());//for GlobalsqlColumn
						ps.setString(17, cell.getImagePath());//for ImagePath

						// Set fileContent as a binary stream
						if (cell.getFileContent() != null) {
							ps.setBytes(18, cell.getFileContent());
						} else {
							ps.setNull(18, Types.BINARY);
						}

						ps.setInt(19, cell.getImgHeight());
						ps.setInt(20, cell.getImgWidth());
						ps.setString(21, cell.getCellCSS());
						//                ps.setString(22, cell.getComponentCss());
						ps.setString(22, null);//form ComponentCss column
						ps.setString(23, "");// for save sql column
						ps.setString(24, null);//for [AjaxUpdateStr] column
						ps.setBoolean(25, cell.isAutoRefresh());
						ps.setInt(26, cell.getPollInterval());//RefreshInterval column
						ps.setString(27, cell.getApiUrl());
						ps.setString(28, cell.getApiBody());
						ps.setString(29, cell.getApiMethod());
						ps.setInt(30, formInfo.getVersionNumber()); //version column
						ps.setBoolean(31, cell.isRequiredfield());
						ps.setBoolean(32, ( (cell.getDepExpressionType()==null || cell.getDepExpressionType().equals("SQL") ) ?false:true)); //for expressionfield column
						ps.setString(33, cell.getDisableExpressionLogic());// for disabledexpression column
						ps.setBoolean(34, ( (cell.getDisabledExpType()==null || cell.getDisabledExpType().equals("SQL") ) ?false:true));//for DisabledExpType column
						ps.setString(35, cell.getDatepattern());
						ps.setBoolean(36, ((cell.getPollStartExpType()==null) || cell.getPollStartExpType().equals("SQL")?false:true)); // for PollStartExpType column
						ps.setString(37, cell.getPollStartExpressionLogic());// for PollStartExpression column
						ps.setBoolean(38,((cell.getPollStopExpType()==null) || cell.getPollStopExpType().equals("SQL")?false:true)); //for PollStopExpType column
						ps.setString(39, cell.getPollStopExpressionLogic());// fro PollStopExpression for column
						ps.setString(40, cell.getButtonIcon());
						ps.setString(41, cell.getButtonKeyValue());
						ps.setString(42, null);// for buttonArgument  column
						ps.setString(43, cell.getButtonLogic());
						ps.setString(44, cell.getButtonClassName());
						ps.setString(45, cell.getButtonType());
						ps.setString(46, cell.getUrl());
						ps.setString(47, tenantId);
						ps.setBoolean(48, ( (cell.getGlobalCellExpType()==null || cell.getGlobalCellExpType().equals("SQL") ) ?false:true)); //for global cell update type (SQL/API)		                
						ps.setString(49, cell.getContext());


					});

					//            System.out.println("Row inserted for Cell ID: " + cell.getCellId());

					if(cell.getFieldType().equals("Dropdown"))
					{
						if(cell.getDepExpressionLogic()==null || cell.getDepExpressionLogic().isEmpty()) {
							for (DropDownInfo dropDownItem : cell.getDropDownFields()) {
								String dropDownItemsSQL = "INSERT INTO [dbo].[DLBFormDropdownItems] (FormId, CellId, ItemLabel, ItemValue, VersionNumber,TenantId) VALUES (?, ?, ?, ?, ?,?)";
								jdbcTemplateOp360.update(dropDownItemsSQL,
										lastFormId,
										cell.getCellId(),
										dropDownItem.getItemLabel(),
										dropDownItem.getItemValue(),
										formInfo.getVersionNumber(),tenantId);
							}
						}
					}

					if(cell.getDependentFields()!=null) {
						for(CellDto depCell : cell.getDependentFields()) {
							String insertDepCellSql = "INSERT INTO [dbo].[DLBFormDependentFieldInfo] " +
									"(FormId, DepCellId,CellId, VersionNumber,[TenantId]) VALUES (?, ?, ?, ?,?)";

							// Execute the query
							int insertDepCellSqlCount =  jdbcTemplateOp360.update(insertDepCellSql, lastFormId, cell.getCellId(), depCell.getCellId(), formInfo.getVersionNumber(),tenantId);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public FormDto getFormDetailsByFormName(String tenantId, String formName) {
		
		JdbcTemplate jdbcTemplateOp360 = jdbcTemplateCollection.get(tenantId).get(0);
		
		try {
			String sql = "SELECT TOP(1) [FormId], [FormName], [UserID], [SaveSQL], [TableSQL], " +
							"[DeleteSQL], [CreationDate], [CreatedUser], [Department], [UserGroup], " +
							"[DocumentID], [FormatID], [VersionNumber], [isActiveForm], [TenantId]"
//							+ ", [DashboardType]"
							+ " FROM [dbo].[DigitalLogbookFormInfo] WHERE FormName = ? " +
							"ORDER BY VersionNumber DESC";

			return jdbcTemplateOp360.queryForObject(sql, (rs, rowNum) -> {
				FormDto formDto = new FormDto();
				formDto.setFormID(rs.getInt("FormId"));
				formDto.setFormName(rs.getString("FormName"));
				formDto.setUserId(rs.getString("UserID"));
				formDto.setSaveSQL(rs.getString("SaveSQL"));
				formDto.setTableSQL(rs.getString("TableSQL"));
				formDto.setDeleteSQL(rs.getString("DeleteSQL"));
				formDto.setCreationDate(rs.getTimestamp("CreationDate"));
				formDto.setCreatedUser(rs.getString("CreatedUser"));
				formDto.setDepartment(rs.getString("Department"));
				formDto.setUserGroup(rs.getString("UserGroup"));
				formDto.setDocumentId(rs.getString("DocumentID"));
				formDto.setFormatID(rs.getString("FormatID"));
				formDto.setVersionNumber(rs.getInt("VersionNumber"));
				formDto.setActiveForm(rs.getBoolean("isActiveForm"));
				formDto.setTenantId(rs.getString("TenantId"));
//				formDto.setDashboardType(rs.getBoolean("DashboardType"));
				return formDto;
			}, formName);
		} catch (EmptyResultDataAccessException e) {
			return null; // Return null or throw a custom exception
		}
	}

	@Override
	public void updateForm(FormDto formInfo, String tenantId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateActiveStatus(FormDto formInfo, String tenantId,boolean isDeactivateOtherVersion) {
		
		JdbcTemplate jdbcTemplateOp360 = jdbcTemplateCollection.get(tenantId).get(0);
		
		// SQL for setting isActiveForm = 1 for the specified FormName and VersionNumber
		if(isDeactivateOtherVersion) {
			String setActiveSQL = "UPDATE [dbo].[DigitalLogbookFormInfo] " +
					"SET isActiveForm = 1 " +
					"WHERE FormName = ? AND VersionNumber = ?";

			// SQL for setting isActiveForm = 0 for other versions of the same FormName
			String setInactiveSQL = "UPDATE [dbo].[DigitalLogbookFormInfo] " +
					"SET isActiveForm = 0 " +
					"WHERE FormName = ? AND VersionNumber != ?";

			// Execute the first update query
			int activeUpdateCount = jdbcTemplateOp360.update(
					setActiveSQL,
					formInfo.getFormName(),
					formInfo.getVersionNumber()
					);

			// Execute the second update query
			int inactiveUpdateCount = jdbcTemplateOp360.update(
					setInactiveSQL,
					formInfo.getFormName(),
					formInfo.getVersionNumber()
					);

			// Log the update counts for debugging
			System.out.println(activeUpdateCount + " records set to active.");
			System.out.println(inactiveUpdateCount + " records set to inactive.");
		}

		else {
			System.out.println("Else Part call");
			String setInactiveSQL = "UPDATE [dbo].[DigitalLogbookFormInfo] " +
					"SET isActiveForm = 0 " +
					"WHERE FormName = ? AND VersionNumber = ?";
			int inactiveUpdateCount = jdbcTemplateOp360.update(
					setInactiveSQL,
					formInfo.getFormName(),
					formInfo.getVersionNumber()
					);
			System.out.println(inactiveUpdateCount + " records set to inactive.");
		}
	}

	public void registerNewTenant(String tenantid) {
		List<JdbcTemplate> jdbcTenantList = new CopyOnWriteArrayList<>();
		String sql = "SELECT [db_name], [db_ip], [db_username], [db_password], [db_driver], [TenantId] FROM [tenant_details] where [TenantId]=?";

		TenantConfigDetails tenantDetail = jdbcTemplateOp360Tenant.queryForObject(
				sql,
				(rs, rowNum) -> new TenantConfigDetails(
						rs.getString("TenantId"),
						rs.getString("db_ip"),
						rs.getString("db_name"),
						rs.getString("db_password"),
						rs.getString("db_driver"),
						rs.getString("db_username")
						),
				tenantid
				);
		String[] dbNames = tenantDetail.getDbName().split(",");
		for (String dbName : dbNames) {
			String url = JdbcUrlUtil.buildJdbcUrl(tenantDetail.getDriver(), tenantDetail.getDbIp(), dbName);
			HikariConfig config = new HikariConfig();
			config.setJdbcUrl(url);
			config.setUsername(tenantDetail.getDbUser());
			config.setPassword(tenantDetail.getDbPassword());
			config.setDriverClassName(tenantDetail.getDriver());
			config.setMaximumPoolSize(20);
			config.setMinimumIdle(15);
			config.setIdleTimeout(30000);         // 30 seconds
			config.setMaxLifetime(1800000);       // 30 minutes
			config.setConnectionTimeout(30000);   // 30 seconds
			config.setPoolName("HikariPool-" + tenantDetail.getTenantId());
			HikariDataSource hikariDataSource=new HikariDataSource(config);
			JdbcTemplate jdbcTemplate=new JdbcTemplate(hikariDataSource);
			jdbcTenantList.add(jdbcTemplate);

		}

		jdbcTemplateCollection.put(tenantid, jdbcTenantList);

		System.out.println("Registered Tenant:"+jdbcTemplateCollection.hashCode());
		System.err.println("Jdbctemplate toString:"+jdbcTemplateCollection.toString());
	}
	
	
	public FormDto getLatestForm(String tenantId) {
		JdbcTemplate jdbcTemplateOp360 = jdbcTemplateCollection.get(tenantId).get(0);
        String sql = "SELECT TOP(1) [FormId], [FormName], [UserID], [SaveSQL], [TableSQL], [DeleteSQL], " +
                "[CreationDate], [CreatedUser], [Department], [UserGroup], [DocumentID], [FormatID], " +
                "[VersionNumber], [isActiveForm], [isPublicAccess], [ModifedBy], [ModifiedDate], [TenantId]"
//                + ", [DashboardType]"
                + " FROM [dbo].[DigitalLogbookFormInfo] ORDER BY FormId DESC";

        return jdbcTemplateOp360.queryForObject(sql, new RowMapper<FormDto>() {
            @Override
            public FormDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                FormDto dto = new FormDto();
                dto.setFormID(rs.getInt("FormId"));
                dto.setFormName(rs.getString("FormName"));
                dto.setUserId(rs.getString("UserID"));
                dto.setSaveSQL(rs.getString("SaveSQL"));
                dto.setTableSQL(rs.getString("TableSQL"));
                dto.setDeleteSQL(rs.getString("DeleteSQL"));
                dto.setCreationDate(rs.getTimestamp("CreationDate"));
                dto.setCreatedUser(rs.getString("CreatedUser"));
                dto.setDepartment(rs.getString("Department"));
                dto.setUserGroup(rs.getString("UserGroup"));
                dto.setDocumentId(rs.getString("DocumentID"));
                dto.setFormatID(rs.getString("FormatID"));
                dto.setVersionNumber(rs.getInt("VersionNumber"));
                dto.setActiveForm(rs.getBoolean("isActiveForm"));
                dto.setPublicAccess(rs.getBoolean("isPublicAccess"));
                dto.setModifiedBy(rs.getString("ModifedBy"));
                dto.setModifiedDate(rs.getTimestamp("ModifiedDate"));
                dto.setTenantId(rs.getString("TenantId"));
//                dto.setDashboardType(rs.getBoolean("DashboardType"));
                
                // InsertSQL is not part of the query, so set manually or remove field
                dto.setInsertSQL(null);
                dto.setSelectedGrp(null);
                // CellInfo list remains empty (unless populated elsewhere)

                return dto;
            }
        });
    }
}


