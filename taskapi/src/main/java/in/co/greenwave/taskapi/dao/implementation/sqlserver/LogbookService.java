package in.co.greenwave.taskapi.dao.implementation.sqlserver;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import in.co.greenwave.taskapi.dao.LogbookDAO;
import in.co.greenwave.taskapi.model.FormDetails;
import in.co.greenwave.taskapi.model.UserDepartment;

@Repository
public class LogbookService implements LogbookDAO {
	@Autowired
	@Qualifier("jdbcTemplate1")
	public JdbcTemplate jdbcTemplate;
	@Autowired
	@Qualifier("jdbcTemplate2")
	public JdbcTemplate jdbcTemplate2;
	
	/**
	 * Retrieves all form names from the database.
	 *
	 * @return List of FormDetails containing information about forms.
	 */
	@Override
	public List<FormDetails> getAllFormNames() {

		List<FormDetails> forms = new ArrayList<>();

		try {
			String sql = "SELECT Distinct FormId, FormName, VersionNumber, DocumentID, Department, " +
					"UserGroup, formatID, CreatedUser, isActiveForm, [SaveSQL], [TableSQL], [DeleteSQL] " +
					"FROM [dbo].[DigitalLogbookFormInfo]";
			List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);

			for (Map<String, Object> row : rows) {
				FormDetails form = new FormDetails(
						(int) row.get("FormId"),
						(String) row.get("FormName"),
						(int) row.get("VersionNumber"),
						(String) row.get("DocumentID"),
						(String) row.get("Department"),
						(String) row.get("UserGroup"),
						(String) row.get("formatID"),
						(String) row.get("CreatedUser"),
						(boolean) row.get("isActiveForm"),
						(String) row.get("SaveSQL"),
						(String) row.get("DeleteSQL"),
						(String) row.get("TableSQL")
						);
				forms.add(form);
			}
		} catch (Exception e) {
			// Handle exceptions appropriately or log them
			e.printStackTrace();
		}
		return forms;

	}
	
	/**
	 * Retrieves all user departments from the second database (jdbcTemplate2).
	 *
	 * @return List of UserDepartment containing information about user departments.
	 */
	@Override
	public List<UserDepartment> getAllUserDept() {
	      List<UserDepartment> data = new ArrayList<>();

	        try {
	            List<Map<String, Object>> rows = jdbcTemplate2.queryForList("SELECT [DeptId], [DeptName] FROM [dbo].[DeptCredentials]");
	            for (Map<String, Object> row : rows) {
	                String deptId = (String) row.get("DeptId");
	                String deptName = (String) row.get("DeptName");
	                data.add(new UserDepartment(deptId, deptName));
	            }
	        } catch (Exception e) {
	            // Handle exceptions according to your requirement
	            e.printStackTrace();
	        }

	        return data;
	}

	/*@Override
	public List<UserDepartment> getAllUserDept() {
		// TODO Auto-generated method stub
		return null;
	}*/

//	private final G360Alert ALERTOBJ=new G360Alert();
//	DateFormat dfDt = new SimpleDateFormat("yyyy-MM-dd");
//	DateFormat dfDtTm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//	Connection con = null;
//	PreparedStatement pstmt = null;
//	
//	@Override
//	public List<FormDetails> getAllFormNames(){
//		List<FormDetails> forms = new ArrayList<FormDetails>();
//		Connection con = null;
//		PreparedStatement pstmt = null;
//		
//		try {			
//			con = SQLServerDAOFactory.createConnection();
//			pstmt = con.prepareStatement("Select Distinct FormId,FormName,VersionNumber,DocumentID,Department,UserGroup,formatID,CreatedUser,isActiveForm,[SaveSQL],[TableSQL],[DeleteSQL] From [dbo].[DigitalLogbookFormInfo]");
//			ResultSet rs = pstmt.executeQuery();
//			while(rs.next()) {
//				FormDetails form = new FormDetails(rs.getInt("FormId"), rs.getString("FormName").trim(), rs.getInt("VersionNumber"), rs.getString("DocumentID"), rs.getString("Department"), rs.getString("UserGroup"), rs.getString("FormatID"), rs.getString("CreatedUser"), rs.getBoolean("isActiveForm"), rs.getString("SaveSQL"), rs.getString("DeleteSQL"), rs.getString("TableSQL"));		
//				forms.add(form);
//			}
//		} 
//		catch (Exception e) {
//			ALERTOBJ.ShowErrorAlert(e.getMessage());
//			e.printStackTrace();
//		}
//		finally {
//			try {
//				con.close();
//				pstmt.close();
//			} catch (SQLException e) { 
//				ALERTOBJ.ShowErrorAlert(e.getMessage());
//				e.printStackTrace();
//			}
//
//		}
//		return forms;
//	}
//
//@Override
//	public List<FormDetails> getAllActiveForms(){
//		List<FormDetails> forms = new ArrayList<FormDetails>();
//		Connection con = null;
//		PreparedStatement pstmt = null;
//		
//		try {			
//			con = SQLServerDAOFactory.createConnection();
//			pstmt = con.prepareStatement("Select Distinct FormId,FormName,VersionNumber,DocumentID,Department,UserGroup,formatID,isActiveForm From [dbo].[DigitalLogbookFormInfo]  Where isActiveForm = 1");
//			ResultSet rs = pstmt.executeQuery();
//			while(rs.next()) {
//				forms.add(new FormDetails(rs.getInt("FormId"), rs.getString("FormName").trim(), rs.getInt("VersionNumber"), rs.getString("DocumentID"), rs.getString("Department"), rs.getString("UserGroup"), rs.getString("FormatID"), rs.getBoolean("isActiveForm")));
//			}
//		} 
//		catch (Exception e) {
//			ALERTOBJ.ShowErrorAlert(e.getMessage());
//			e.printStackTrace();
//		}
//		finally {
//			try {
//				con.close();
//				pstmt.close();
//			} catch (SQLException e) { 
//				ALERTOBJ.ShowErrorAlert(e.getMessage());
//				e.printStackTrace();
//			}
//
//		}
//		return forms;
//	}
//
//
//@Override
//	public List<FormDetails> getformDetailsWithFormName(String FormName) {
//		
//		Connection con = null;
//		PreparedStatement pstmt = null;
//		List<FormDetails> frmd = new ArrayList<FormDetails>();
//		try {			
//			con = SQLServerDAOFactory.createConnection();
//			pstmt = con.prepareStatement("Select [FormId],[FormName],[SaveSQL],[TableSQL],COALESCE([DeleteSQL],'') DeleteSQL,[DocumentID],[Department],[UserGroup],[formatID],[VersionNumber] from [dbo].[DigitalLogbookFormInfo] Where FormName = ? ");
//			pstmt.setString(1, FormName);
//			ResultSet rs = pstmt.executeQuery();
//			while(rs.next()) {
//				frmd.add(new FormDetails(rs.getInt("FormId"), rs.getString("FormName"), rs.getString("TableSQL"), rs.getString("SaveSQL"),rs.getString("DeleteSQL"), rs.getString("DocumentID"), rs.getString("Department"), rs.getString("UserGroup"), rs.getString("FormatID"), rs.getInt("VersionNumber")));
//			}
//		} 
//		catch (Exception e) {
//			ALERTOBJ.ShowErrorAlert(e.getMessage());
//			e.printStackTrace();
//		}
//		finally {
//			try {
//				con.close();
//				pstmt.close();
//			} catch (SQLException e) { 
//				ALERTOBJ.ShowErrorAlert(e.getMessage());
//				e.printStackTrace();
//			}
//
//		}
//		return frmd;
//	}
//	@Override
//	public List<String> getMetaDataofResultSet(String sql){
//		List<String> items = new ArrayList<String>();
//		Connection con = null;
//		PreparedStatement pstmt = null;
//		
//		try {			
//			con = SQLServerDAOFactory.createConnection();
//			pstmt = con.prepareStatement(sql);
//			ResultSet rs = pstmt.executeQuery();
//			ResultSetMetaData rsmd = rs.getMetaData();
//			for(int i = 1 ; i<=rsmd.getColumnCount() ; i++) {
//				items.add(rsmd.getColumnName(i));
//			}
//		} 
//		catch (Exception e) {
//			ALERTOBJ.ShowErrorAlert(e.getMessage());
//			e.printStackTrace();
//		}
//		finally {
//			try {
//				con.close();
//				pstmt.close();
//			} catch (SQLException e) { 
//				ALERTOBJ.ShowErrorAlert(e.getMessage());
//				e.printStackTrace();
//			}
//
//		}
//		return items;
//	}
//	@Override
//	public String getDataFromDepSQL(String sql) {
//		String data = "";
//		Connection con = null;
//		PreparedStatement pstmt = null;
//		
//		try {			
//			con = SQLServerDAOFactory.createConnection();
//			pstmt = con.prepareStatement(sql);
//			System.out.println(sql);
//			ResultSet rs = pstmt.executeQuery();
//			while(rs.next()) {
//				data = rs.getString(1);
//			}
//		} 
//		catch (Exception e) {
//			//ALERTOBJ.ShowErrorAlert(e.getMessage());
//			data = "@Error@";
//			e.printStackTrace();
//		}
//		finally {
//			try {
//				con.close();
//				pstmt.close();
//			} catch (SQLException e) { 
//				//ALERTOBJ.ShowErrorAlert(e.getMessage());
//				e.printStackTrace();
//			}
//
//		}
//		return data;
//	}
//	@Override
//	public List<FormInfo> loadDLBForm(String formName, int vrsnNumber){
//		List<FormInfo> formModel = new ArrayList<FormInfo>();
//		Connection con = null;
//		PreparedStatement pstmt = null;
//		PreparedStatement depPstmt = null;
//		PreparedStatement drpDwnPstmt = null;
//		
//		
//		
//		String sql = "SELECT [FormId] ,[RowNum] ,RowChanged = CONVERT(bit,IIF((COALESCE((LAG(RowNum) Over (Order by RowNum,ColNum)),RowNum)) <> RowNum,1,0)) ,[ColNum] ,[CellId] ,[AliasId] ,[ColsSpan] ,[RowSpan] ,[FieldType] ,[NumMinVal] ,[NumMaxVal] ,[NumDecimalPoint] ,[Editable] ,[Dependent] ,[DbColName] ,[DepSQL] ,[ImagePath] ,[ImgHeight] ,[ImgWidth] ,[CellCSS] ,[ComponentCSS] ,[SaveSQL] ,[AjaxUpdateStr] ,[AutoRefresh],[RefreshInterval] ,[apiURL],[apiBody],[apiMethod],[requiredfield],[expressionfield],[disabledexpression],[disabledexptype],[datepattern],[pollStartExpType],[pollStartExpression],[pollStopExpType],[pollStopExpression],[GlobalSQL] FROM [dbo].[DigitalLogbookCellDetails] Where FormId = (select formId from [dbo].[DigitalLogbookFormInfo] where FormName = ? and VersionNumber = ?) order by RowNum,ColNum";
//		String depSql="SELECT [FormId],[CellId],[DepCellId]  FROM [dbo].[DLBFormDependentFieldInfo] Where FormId = (select formId from [dbo].[DigitalLogbookFormInfo] where FormName = ? and VersionNumber = ?)";
//		String drpSql = "SELECT  [ItemLabel],[ItemValue] FROM [dbo].[DLBFormDropdownItems] Where FormId = (select formId from [dbo].[DigitalLogbookFormInfo] where FormName = ? and VersionNumber = ?) AND CellId = ?";
//		try {
//			con = SQLServerDAOFactory.createConnection();
//			pstmt = con.prepareStatement(sql);
//			pstmt.setString(1, formName);
//			pstmt.setInt(2, vrsnNumber);
//			ResultSet rs = pstmt.executeQuery();
//			FormInfo row = new FormInfo();
//			List<in.co.greenwave.modules.logbook.FormInfo.CellInfo> cellList = new ArrayList<in.co.greenwave.modules.logbook.FormInfo.CellInfo>();
//			while(rs.next()) {
//				boolean changed = rs.getBoolean("RowChanged"); 
//				if(changed) {
//					row.setCellData(cellList);
//					formModel.add(row);
//					//New Row
//					row = new FormInfo();
//					cellList = new ArrayList<in.co.greenwave.modules.logbook.FormInfo.CellInfo>();
//					in.co.greenwave.modules.logbook.FormInfo.CellInfo cell = new in.co.greenwave.modules.logbook.FormInfo.CellInfo();
//					cell.setCellId(rs.getString("CellId"));
//					cell.setAliasId(rs.getString("AliasId"));
//					cell.setColSpan(rs.getInt("ColsSpan"));
//					cell.setRowSpan(rs.getInt("RowSpan"));
//					cell.setFieldType(rs.getString("FieldType"));
//					cell.setMinVal(rs.getDouble("NumMinVal"));
//					cell.setMaxVal(rs.getDouble("NumMaxVal"));
//					cell.setDecimalPoints(rs.getInt("NumDecimalPoint"));
//					cell.setEditable(rs.getBoolean("Editable"));
//					cell.setDbColName(rs.getString("DbColName"));
//					cell.setDependent(rs.getBoolean("Dependent"));
//					cell.setDepSql(rs.getString("DepSQL"));
//					cell.setImagePath(rs.getString("ImagePath"));
//					cell.setImgHeight(rs.getInt("ImgHeight"));
//					cell.setImgWidth(rs.getInt("ImgWidth"));
//					cell.setCellCSS(rs.getString("CellCSS"));
//					cell.setSaveSQL(rs.getString("AjaxUpdateStr"));
//					cell.setUpdateStr(rs.getString("AjaxUpdateStr"));
//					cell.setPollable(rs.getBoolean("AutoRefresh"));
//					cell.setPollInterval(rs.getInt("RefreshInterval"));
//					cell.setApiUrl(rs.getString("apiURL"));
//					cell.setApiBody(rs.getString("apiBody"));
//					cell.setApiMethod(rs.getString("apiMethod"));
//					cell.setRequiredfield(rs.getBoolean("requiredfield"));
//					cell.setIsexpression(rs.getBoolean("expressionfield"));
//					cell.setDisableExpression(rs.getString("disabledexpression"));
//					cell.setDisabledExptype(rs.getBoolean("disabledexptype"));
//					cell.setDatepattern(rs.getString("datepattern"));
//					cell.setPollStartExpType(rs.getBoolean("pollStartExpType"));
//					cell.setPollStartExpression(rs.getString("pollStartExpression"));
//					cell.setPollStopExpType(rs.getBoolean("pollStopExpType"));
//					cell.setPollStopExpression(rs.getString("pollStopExpression"));
//					cell.setCellUpdateQuery(rs.getString("GlobalSQL"));
//					
//					if(cell.getFieldType().equalsIgnoreCase("Dropdown") && cell.getDepSql().trim().equalsIgnoreCase("") ) {
//						drpDwnPstmt = con.prepareStatement(drpSql);
//						drpDwnPstmt.setString(1, formName);
//						drpDwnPstmt.setInt(2, vrsnNumber);
//						drpDwnPstmt.setString(3, cell.getCellId());
//						ResultSet rsDrp = drpDwnPstmt.executeQuery();
//						List<DropDownItems> items = new ArrayList<DropDownItems>();
//						while(rsDrp.next()) {
//							items.add(new DropDownItems(rsDrp.getString("ItemLabel"), rsDrp.getString("ItemValue")));							
//						}
//						cell.setDrpItems(items);
//					}
//					
//					cellList.add(cell);
//				}else {
//					in.co.greenwave.modules.logbook.FormInfo.CellInfo cell = new in.co.greenwave.modules.logbook.FormInfo.CellInfo();
//					cell.setCellId(rs.getString("CellId"));
//					cell.setAliasId(rs.getString("AliasId"));
//					cell.setColSpan(rs.getInt("ColsSpan"));
//					cell.setRowSpan(rs.getInt("RowSpan"));
//					cell.setFieldType(rs.getString("FieldType"));
//					cell.setMinVal(rs.getDouble("NumMinVal"));
//					cell.setMaxVal(rs.getDouble("NumMaxVal"));
//					cell.setDecimalPoints(rs.getInt("NumDecimalPoint"));
//					cell.setEditable(rs.getBoolean("Editable"));
//					cell.setDbColName(rs.getString("DbColName"));
//					cell.setDependent(rs.getBoolean("Dependent"));
//					cell.setDepSql(rs.getString("DepSQL"));
//					cell.setImagePath(rs.getString("ImagePath"));
//					cell.setImgHeight(rs.getInt("ImgHeight"));
//					cell.setImgWidth(rs.getInt("ImgWidth"));
//					cell.setCellCSS(rs.getString("CellCSS"));
//					cell.setSaveSQL(rs.getString("AjaxUpdateStr"));
//					cell.setUpdateStr(rs.getString("AjaxUpdateStr"));
//					cell.setPollable(rs.getBoolean("AutoRefresh"));
//					cell.setPollInterval(rs.getInt("RefreshInterval"));
//					cell.setApiUrl(rs.getString("apiURL"));
//					cell.setApiBody(rs.getString("apiBody"));
//					cell.setApiMethod(rs.getString("apiMethod"));
//					cell.setRequiredfield(rs.getBoolean("requiredfield"));
//					cell.setIsexpression(rs.getBoolean("expressionfield"));
//					cell.setDisableExpression(rs.getString("disabledexpression"));
//					cell.setDisabledExptype(rs.getBoolean("disabledexptype"));
//					cell.setDatepattern(rs.getString("datepattern"));
//					cell.setPollStartExpType(rs.getBoolean("pollStartExpType"));
//					cell.setPollStartExpression(rs.getString("pollStartExpression"));
//					cell.setPollStopExpType(rs.getBoolean("pollStopExpType"));
//					cell.setPollStopExpression(rs.getString("pollStopExpression"));
//					cell.setCellUpdateQuery(rs.getString("GlobalSQL"));
//					
//					
//					if(cell.getFieldType().equalsIgnoreCase("Dropdown") && cell.getDepSql().trim().equalsIgnoreCase("") ) {
//						drpDwnPstmt = con.prepareStatement(drpSql);
//						drpDwnPstmt.setString(1, formName);
//						drpDwnPstmt.setInt(2, vrsnNumber);
//						drpDwnPstmt.setString(3, cell.getCellId());
//						ResultSet rsDrp = drpDwnPstmt.executeQuery();
//						List<DropDownItems> items = new ArrayList<DropDownItems>();
//						while(rsDrp.next()) {
//							items.add(new DropDownItems(rsDrp.getString("ItemLabel"), rsDrp.getString("ItemValue")));							
//						}
//						cell.setDrpItems(items);
//					}
//					cellList.add(cell);
//				}
//			}
//			row.setCellData(cellList);
//			formModel.add(row);
//			
//			depPstmt = con.prepareStatement(depSql);
//			depPstmt.setString(1, formName);
//			depPstmt.setInt(2, vrsnNumber);
//			ResultSet rsDep = depPstmt.executeQuery();
//			List<FormInfo> frm2 = new ArrayList<FormInfo>();
//			frm2.addAll(formModel);
//			while(rsDep.next()) {
//				String cellId = rsDep.getString("CellId");
//				String depCellId = rsDep.getString("DepCellId");
//				for(FormInfo fRow : formModel) {
//					for(in.co.greenwave.modules.logbook.FormInfo.CellInfo cell : fRow.getCellData()) {
//						
//						if(cell.getCellId().equalsIgnoreCase(cellId)) {
//							
//							for(FormInfo fDRow : frm2) {
//								for(in.co.greenwave.modules.logbook.FormInfo.CellInfo dcell : fDRow.getCellData()) {
//									if(dcell.getCellId().equalsIgnoreCase(depCellId)) {
//										cell.getDependentFields().add(dcell);
//									}
//								}
//							}
//						}
//					}
//				}
//			}
//			
//		} catch (Exception e) {
//			ALERTOBJ.ShowErrorAlert(e.getMessage());
//			e.printStackTrace();
//		}
//		finally {
//			try {
//				con.close();
//				pstmt.close();
//			} catch (SQLException e) { 
//				ALERTOBJ.ShowErrorAlert(e.getMessage());
//				e.printStackTrace();
//			}
//
//		}
//		
//		
//		return formModel;
//	}
//	@Override
//	public List<DBResultModel> getTableDataManualEntry(String sql) {
//		List<DBResultModel> data = new LinkedList<DBResultModel>();
//		
//		PreparedStatement pstm = null;
//		ResultSet rs = null;
//
//		try {
//			con = SQLServerDAOFactory.createConnection();
//			pstm = con.prepareStatement(sql);
//			rs = pstm.executeQuery();
//			while (rs.next()) {
//				Map<String, String> map = new LinkedHashMap<String, String>();
//				for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
//					map.put(rs.getMetaData().getColumnName(i), rs.getString(i));
//				}
//				data.add(new DBResultModel(map));
//			}
//
//		} catch (Exception e) {
//			ALERTOBJ.ShowExceptionAlert(e.getMessage());
//			e.printStackTrace();
//		} finally {
//			try {
//				con.close();
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				ALERTOBJ.ShowExceptionAlert(e.getMessage());
//				e.printStackTrace();
//			}
//		}
//		return data;
//	}
//	@Override
//	public List<DropDownItems> getDrpItemsfromSQL(String sql){
//		List<DropDownItems> drpItems = new ArrayList<DropDownItems>();
//		Connection con = null;
//		PreparedStatement pstmt = null;
//		
//		try {			
//			con = SQLServerDAOFactory.createConnection();
//			pstmt = con.prepareStatement(sql);
//			ResultSet rs = pstmt.executeQuery();
//			while(rs.next()) {
//				if(rs.getMetaData().getColumnCount() == 1)
//					drpItems.add(new DropDownItems(rs.getString(1), rs.getString(1)));
//				else if(rs.getMetaData().getColumnCount() == 2)
//					drpItems.add(new DropDownItems(rs.getString(1),rs.getString(2)));
//			}
//			System.out.println("Items : "+drpItems);
//		} 
//		catch (Exception e) {
//			//ALERTOBJ.ShowErrorAlert(e.getMessage());
//			e.printStackTrace();
//		}
//		finally {
//			try {
//				con.close();
//				pstmt.close();
//			} catch (SQLException e) { 
//				//ALERTOBJ.ShowErrorAlert(e.getMessage());
//				e.printStackTrace();
//			}
//
//		}
//		return drpItems;
//	}
//	@Override
//	public int saveLogbookData(String deleteQuery,String insertQuery) {
//		Connection con = null;
//		PreparedStatement pstm = null;
//		int colCount = 0;
//		try {	
//			con = SQLServerDAOFactory.createConnection();
//			if(deleteQuery!= null) {			
//				pstm = con.prepareStatement(deleteQuery);
//				colCount = pstm.executeUpdate();
//				System.out.println(colCount+" Data deleted");
//				pstm.close();
//			}
//			pstm = con.prepareStatement(insertQuery);
//			colCount = pstm.executeUpdate();
//			if(colCount>0) {
//				ALERTOBJ.ShowInfoAlert("Data Saved Successfully");
//			}else {
//				ALERTOBJ.ShowWarningAlert("No Data Saved. Please check for Errors!!");
//			}
//		}catch (Exception e) {
//			ALERTOBJ.ShowErrorAlert(e.getMessage());
//			e.printStackTrace();
//		}
//		finally {
//			try {
//				con.close();
//				pstm.close();
//			} catch (SQLException e) { 
//				e.printStackTrace();
//			}
//
//		}
//		return colCount;
//	}
//	@Override
//	public void deleteSavedRow(String delSql) {
//		Connection con = null;
//		PreparedStatement pstm = null;
//		int colCount = 0;
//		try {	
//			con = SQLServerDAOFactory.createConnection();
//			pstm = con.prepareStatement(delSql);
//			colCount = pstm.executeUpdate();
//			System.out.println(colCount+" Data deleted");
//			pstm.close();
//			if(colCount>0) {
//				ALERTOBJ.ShowInfoAlert("Row has been deleted");
//			}else {
//				ALERTOBJ.ShowWarningAlert("No row deleted");
//			}
//			
//		}catch (Exception e) {
//			ALERTOBJ.ShowErrorAlert(e.getMessage());
//			e.printStackTrace();
//		}
//		finally {
//			try {
//				con.close();
//				pstm.close();
//			} catch (SQLException e) { 
//				e.printStackTrace();
//			}
//
//		}
//		//return colCount;
//	}
//	@Override
//	public void deleteLogbook(int formId) {
//		Connection con = null;
//		PreparedStatement pstm = null;
//		int colCount = 0;
//		System.out.println("LogbookService.deleteLogbook()"+formId);
//		try {	
//			
//			String delSql = "  Delete from dbo.DigitalLogbookFormInfo Where FormId = ? ";
//			con = SQLServerDAOFactory.createConnection();
//			con.setAutoCommit(false);
//			pstm = con.prepareStatement(delSql);
//			pstm.setInt(1, formId);
//			colCount = pstm.executeUpdate();
//			con.commit();
//			System.out.println(colCount+" Data deleted");
//			pstm.close();
//			if(colCount>0) {
//				ALERTOBJ.ShowInfoAlert("Form Deleted!!!");
//			}else {
//				ALERTOBJ.ShowWarningAlert("No form deleted");
//			}
//			
//		}catch (Exception e) {
//			ALERTOBJ.ShowErrorAlert(e.getMessage());
//			e.printStackTrace();
//		}
//		finally {
//			try {
//				con.close();
//				pstm.close();
//			} catch (SQLException e) { 
//				e.printStackTrace();
//			}
//
//		}
//		//return colCount;
//	}
//	@Override
//	public void saveDLBForm(String oldFormName,String formName,String tableSql,String insertSQL,String deleteSQL,
//			String user,List<FormInfo> formModel,String documentId,String selectedGrp,String department,String formatID,
//			Boolean versionNumber, Boolean editExisting,List<Integer> versionList,int vrsnNumber, String userID) {
//		
//		//System.out.println("LogbookService.saveDLBForm()"+insertSQL);
//		Connection con = null;
//		PreparedStatement pstm = null;
//		PreparedStatement cellPstmt = null;
//		PreparedStatement depPstmt = null;
//		PreparedStatement drpDwnPstmt = null;
//	
//		int colCount = 0;
//		String formInfoSQL = null;
//		String cellSQL = null;
//		String depCellSQL = null;
//		String drpDwnSQL = null;
//		
//		formInfoSQL = "INSERT INTO [dbo].[DigitalLogbookFormInfo] ([FormName],[SaveSQL],[TableSQL],[CreationDate],[CreatedUser],[DeleteSQL],[DocumentID],[Department],[UserGroup],[formatID],[UserID],[VersionNumber])  values (?,?,?,GETDATE(),?,?,?,?,?,?,?,?)";
//		cellSQL = "Insert Into [dbo].[DigitalLogbookCellDetails] ([FormId] ,[RowNum] ,[ColNum] ,[CellId] ,[AliasId] ,[ColsSpan] ,[RowSpan] ,[FieldType] ,[NumMinVal] ,[NumMaxVal] ,[NumDecimalPoint] ,[Editable] ,[Dependent] ,[DbColName] ,[DepSQL] ,[ImagePath] ,[ImgHeight] ,[ImgWidth] ,[CellCSS] ,[ComponentCSS] ,[SaveSQL] ,[AjaxUpdateStr],[AutoRefresh],[RefreshInterval],[apiURL],[apiBody],[apiMethod],[VersionNumber],[requiredfield],[expressionfield] ,[disabledexpression],[disabledexptype],[datepattern],[pollStartExpType],[pollStartExpression],[pollStopExpType],[pollStopExpression],[GlobalSQL]) " + 
//				"  values ((Select FormId from dbo.DigitalLogbookFormInfo Where FormName = ? and VersionNumber = ?),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"; 
//		depCellSQL = "INSERT INTO [dbo].[DLBFormDependentFieldInfo] ([FormId],[CellId],[DepCellId],[VersionNumber]) values ((Select FormId from dbo.DigitalLogbookFormInfo Where FormName = ? and VersionNumber = ?),?,?,?)";
//		drpDwnSQL = "INSERT INTO [dbo].[DLBFormDropdownItems] ([FormId],[CellId],[ItemLabel],[ItemValue],[VersionNumber]) values ((Select FormId from dbo.DigitalLogbookFormInfo Where FormName = ? and VersionNumber = ?),?,?,?,?)";
//		
//		try {
//			con = SQLServerDAOFactory.createConnection();
//			con.setAutoCommit(false);
//			
//			if ((!editExisting) || versionNumber ) {
//			pstm = con.prepareStatement(formInfoSQL);
//			pstm.setString(1, formName);
//			pstm.setString(2, insertSQL);
//			pstm.setString(3, tableSql);
//			pstm.setString(4, user);
//			pstm.setString(5, deleteSQL);
//			pstm.setString(6, documentId);
//			pstm.setString(7, department);
//			pstm.setString(8, selectedGrp);
//			pstm.setString(9, formatID);
//			pstm.setString(10, userID);
//			if(!editExisting) {
//				if (vrsnNumber == 0 && versionList.isEmpty()) {
//					pstm.setInt(11, 1);
//				}
//				else {
//					pstm.setInt(11, Collections.max(versionList) + 1);
//				}
//			}
//			else {
//				if(versionNumber)
//					pstm.setInt(11, Collections.max(versionList) + 1);
//			}
//			
//			colCount = pstm.executeUpdate();
//			pstm.close();
//			con.commit();
//			} 
//			else {
//				formInfoSQL = "Update [dbo].[DigitalLogbookFormInfo] set [SaveSQL] = ?, [TableSQL] = ?, [CreationDate] = GETDATE(), [CreatedUser] = ?, [DeleteSQL] = ?, [DocumentID] = ?, [Department] = ?, [UserGroup] = ?, [formatID] = ? where [FormName] = ? and [VersionNumber] = ? and [UserID] = ?";
//				
//				pstm = con.prepareStatement("delete from [dbo].[DigitalLogbookCellDetails] where FormId = (Select FormId from dbo.DigitalLogbookFormInfo Where FormName = ? and VersionNumber = ?) "
//						+ "delete from [dbo].[DLBFormDependentFieldInfo] where FormId = (Select FormId from dbo.DigitalLogbookFormInfo Where FormName = ? and VersionNumber = ?) "
//						+ "delete from [dbo].[DLBFormDropdownItems] where FormId = (Select FormId from dbo.DigitalLogbookFormInfo Where FormName = ? and VersionNumber = ?)");
//				pstm.setString(1, oldFormName);
//				pstm.setInt(2, vrsnNumber);
//				pstm.setString(3, oldFormName);
//				pstm.setInt(4, vrsnNumber);
//				pstm.setString(5, oldFormName);
//				pstm.setInt(6, vrsnNumber);
//				pstm.execute();
//				pstm.close();
//				con.commit();
//				
//				pstm = con.prepareStatement(formInfoSQL);
//				pstm.setString(1, insertSQL);
//				pstm.setString(2, tableSql);
//				pstm.setString(3, user);
//				pstm.setString(4, deleteSQL);
//				pstm.setString(5, documentId);
//				pstm.setString(6, department);
//				pstm.setString(7, selectedGrp);
//				pstm.setString(8, formatID);
//				pstm.setString(9, formName);
//				pstm.setInt(10, vrsnNumber);
//				pstm.setString(11, userID);
//				
//				colCount = pstm.executeUpdate();
//				pstm.close();
//				con.commit();
//			}
//			
//			int rowNum = 1;
//			int colNum = 1;
//			cellPstmt = con.prepareStatement(cellSQL);
//			for(FormInfo row : formModel) {
//				colNum = 1;
//				for(in.co.greenwave.modules.logbook.FormInfo.CellInfo cell : row.getCellData()) {
//					cellPstmt.setString(1, formName);
//					if(!editExisting) {
//						if (vrsnNumber == 0 && versionList.isEmpty()) {
//							cellPstmt.setInt(2, 1);
//						}
//						else {
//							cellPstmt.setInt(2, Collections.max(versionList) + 1);
//						}
//					}
//					else {
//						if(versionNumber)
//							cellPstmt.setInt(2, Collections.max(versionList) + 1);
//						else 
//							cellPstmt.setInt(2, vrsnNumber);
//					}
//					cellPstmt.setInt(3, rowNum);
//					cellPstmt.setInt(4, colNum);
//					cellPstmt.setString(5, cell.getCellId());
//					cellPstmt.setString(6, cell.getAliasId());
//					cellPstmt.setInt(7, cell.getColSpan());
//					cellPstmt.setInt(8, cell.getRowSpan());
//					cellPstmt.setString(9, cell.getFieldType());
//					cellPstmt.setDouble(10, cell.getMinVal());
//					cellPstmt.setDouble(11, cell.getMaxVal());
//					cellPstmt.setInt(12, cell.getDecimalPoints());
//					cellPstmt.setBoolean(13, cell.isEditable());
//					cellPstmt.setBoolean(14, cell.isDependent());
//					cellPstmt.setString(15, cell.getDbColName());
//					cellPstmt.setString(16, cell.getDepSql());
//					cellPstmt.setString(17, cell.getImagePath());
//					cellPstmt.setInt(18,cell.getImgHeight());
//					cellPstmt.setInt(19, cell.getImgWidth());
//					cellPstmt.setString(20, cell.getCellCSS());
//					cellPstmt.setString(21, null);
//					cellPstmt.setString(22, cell.getSaveSQL());
//					cellPstmt.setString(23,cell.getUpdateStr());
//					cellPstmt.setBoolean(24, cell.isPollable());
//					cellPstmt.setInt(25,cell.getPollInterval());
//					cellPstmt.setString(26, cell.getApiUrl());
//					cellPstmt.setString(27,cell.getApiBody());
//					cellPstmt.setString(28,cell.getApiMethod());
//					if(!editExisting) {
//						if (vrsnNumber == 0 && versionList.isEmpty()) {
//							cellPstmt.setInt(29, 1);
//						}
//						else {
//							cellPstmt.setInt(29, Collections.max(versionList) + 1);
//						}
//					}
//					else {
//						if(versionNumber)
//							cellPstmt.setInt(29, Collections.max(versionList) + 1);
//						else
//							cellPstmt.setInt(29, vrsnNumber);
//					}
//					cellPstmt.setBoolean(30, cell.isRequiredfield());
//					cellPstmt.setBoolean(31, cell.isIsexpression());
//					cellPstmt.setString(32, cell.getDisableExpression());
//					cellPstmt.setBoolean(33, cell.isDisabledExptype());
//					cellPstmt.setString(34, cell.getDatepattern());
//					cellPstmt.setBoolean(35, cell.isPollStartExpType());
//					cellPstmt.setString(36, cell.getPollStartExpression());
//					cellPstmt.setBoolean(37, cell.isPollStopExpType());
//					cellPstmt.setString(38, cell.getPollStopExpression());
//					cellPstmt.setString(39, cell.getCellUpdateQuery());
//					
//					cellPstmt.addBatch();
//					colNum++;
//				}
//				rowNum++;
//			}
//			cellPstmt.executeBatch();
//			con.commit();
//			
//			depPstmt = con.prepareStatement(depCellSQL);
//			for(FormInfo row : formModel) {
//				for(in.co.greenwave.modules.logbook.FormInfo.CellInfo cell : row.getCellData()) {
//					if(cell.isDependent()) {
//						for(in.co.greenwave.modules.logbook.FormInfo.CellInfo depCell : cell.getDependentFields()) {
//							depPstmt.setString(1, formName);
//							if(!editExisting) {
//								if (vrsnNumber == 0 && versionList.isEmpty()) {
//									depPstmt.setInt(2, 1);
//								}
//								else {
//									depPstmt.setInt(2, Collections.max(versionList) + 1);
//								}
//							}
//							else {
//								if(versionNumber)
//									depPstmt.setInt(2, Collections.max(versionList) + 1);
//								else 
//									depPstmt.setInt(2, vrsnNumber);
//							}
//							depPstmt.setString(3, cell.getCellId());
//							depPstmt.setString(4, depCell.getCellId());
//							if(!editExisting) {
//								if (vrsnNumber == 0 && versionList.isEmpty()) {
//									depPstmt.setInt(5, 1);
//								}
//								else {
//									depPstmt.setInt(5, Collections.max(versionList) + 1);
//								}
//							}
//							else {
//								if(versionNumber)
//									depPstmt.setInt(5, Collections.max(versionList) + 1);
//								else 
//									depPstmt.setInt(5, vrsnNumber);
//							}
//							depPstmt.addBatch();
//						}
//					}
//				}
//			}
//			depPstmt.executeBatch();
//			con.commit();
//			
//			drpDwnPstmt = con.prepareStatement(drpDwnSQL);
//			for(FormInfo row : formModel) {
//				for(in.co.greenwave.modules.logbook.FormInfo.CellInfo cell : row.getCellData()) {
//					if(cell.getDrpItems().size()>0) {
//						for(DropDownItems drpItem : cell.getDrpItems()) {
//							drpDwnPstmt.setString(1, formName);
//							if(!editExisting) {
//								if (vrsnNumber == 0 && versionList.isEmpty()) {
//									drpDwnPstmt.setInt(2, 1);
//								}
//								else {
//									drpDwnPstmt.setInt(2, Collections.max(versionList) + 1);
//								}
//							}
//							else {
//								if(versionNumber)
//									drpDwnPstmt.setInt(2, Collections.max(versionList) + 1);
//								else 
//									drpDwnPstmt.setInt(2, vrsnNumber);
//							}
//							drpDwnPstmt.setString(3, cell.getCellId());
//							drpDwnPstmt.setString(4, drpItem.getItemLabel());
//							drpDwnPstmt.setString(5, drpItem.getItemValue());
//							if(!editExisting) {
//								if (vrsnNumber == 0 && versionList.isEmpty()) {
//									drpDwnPstmt.setInt(6, 1);
//								}
//								else {
//									drpDwnPstmt.setInt(6, Collections.max(versionList) + 1);
//								}
//							}
//							else {
//								if(versionNumber)
//									drpDwnPstmt.setInt(6, Collections.max(versionList) + 1);
//								else 
//									drpDwnPstmt.setInt(6, vrsnNumber);
//							}
//							drpDwnPstmt.addBatch();
//						}
//					}
//				}
//			}
//			drpDwnPstmt.executeBatch();
//			con.commit();
//			
//			ALERTOBJ.ShowInfoAlert("Form "+formName+" Saved successfully");
//			
//		}catch (Exception e) {
//			ALERTOBJ.ShowErrorAlert(e.getMessage());
//			e.printStackTrace();
//		}
//		finally {
//			try {
//				con.rollback();
//				con.close();
//				pstm.close();
//				cellPstmt.close();
//				depPstmt.close();
//				drpDwnPstmt.close();
//			} catch (SQLException e) { 
//				e.printStackTrace();
//			}
//		}
//	}
//	@Override
//	public String fetchLogbookNameforJob(String jobId, String activityId) {
//		String logbook = "";
//		Connection con = null;
//		PreparedStatement pstmt = null;
//		
//		try {			
//			con = SQLServerDAOFactory.createConnection();
//			pstmt = con.prepareStatement("Select LogbookName from dbo.JobActivityDetails Where JobId = ? AND ActivityId = ?");
//			pstmt.setString(1, jobId);
//			pstmt.setString(2, activityId);
//			ResultSet rs = pstmt.executeQuery();
//			while(rs.next()) {
//				logbook = rs.getString("LogbookName");
//			}
//			if(logbook == null || logbook.trim() == "")
//				ALERTOBJ.ShowErrorAlert("Logbook not found");
//		} 
//		catch (Exception e) {
//			ALERTOBJ.ShowErrorAlert(e.getMessage());
//			e.printStackTrace();
//		}
//		finally {
//			try {
//				con.close();
//				pstmt.close();
//			} catch (SQLException e) { 
//				ALERTOBJ.ShowErrorAlert(e.getMessage());
//				e.printStackTrace();
//			}
//
//		}
//		return logbook;		
//	}
//	@Override
//	public void saveTransaction(Transactiondata objTransdata) {
//		Connection con = null;
//		PreparedStatement pstm = null;
//		int colCount = 0;
//		try {	
//			con = SQLServerDAOFactory.createConnection();
//			pstm = con.prepareStatement("insert into dbo.LogbookTransactionData(TransactionId,JobId,ActivityId,formname,[version],logbookdata,transaction_remarks,[timestamp],userid,[role]) values (?,?,?,?,?,?,?,getdate(),?,?)");
//			pstm.setString(1, objTransdata.getTransactionid());
//			pstm.setString(2, objTransdata.getJobid());
//			pstm.setString(3, objTransdata.getActivityid());
//			pstm.setString(4, objTransdata.getFormname());
//			pstm.setInt(5, objTransdata.getFormversion());
//			pstm.setString(6, new JSONObject(objTransdata.getJsoninfo()).toString());
//			pstm.setString(7, objTransdata.getRemarks());
//			pstm.setString(8,objTransdata.getUserid());
//			pstm.setString(9, objTransdata.getRole());
//			
//			
//			
//			colCount = pstm.executeUpdate();
//			System.out.println("Transaction Inserted = "+objTransdata.getTransactionid());
//			ALERTOBJ.ShowInfoAlert("Transaction Saved");
//			pstm.close();
//			
//			
//		}catch (Exception e) {
//			ALERTOBJ.ShowErrorAlert(e.getMessage());
//			e.printStackTrace();
//		}
//		finally {
//			try {
//				con.close();
//				pstm.close();
//			} catch (SQLException e) { 
//				e.printStackTrace();
//			}
//
//		}
//
//		
//	}
//	@Override
//	public String fetchTransactionId(String jobId, String activityId,String logbook) {
//		String trId = "";
//		Connection con = null;
//		PreparedStatement pstmt = null;
//		
//		try {			
//			con = SQLServerDAOFactory.createConnection();
//			pstmt = con.prepareStatement("Select TransactionId From [dbo].[LogbookTransactionData] Where JobId = ? AND ActivityId = ? AND [formname] = ?");
//			pstmt.setString(1, jobId);
//			pstmt.setString(2, activityId);
//			pstmt.setString(3, logbook);
//			
//			ResultSet rs = pstmt.executeQuery();
//			while(rs.next()) {
//				trId = rs.getString("TransactionId");
//			}
//			
//		} 
//		catch (Exception e) {
//			ALERTOBJ.ShowErrorAlert(e.getMessage());
//			e.printStackTrace();
//		}
//		finally {
//			try {
//				con.close();
//				pstmt.close();
//			} catch (SQLException e) { 
//				ALERTOBJ.ShowErrorAlert(e.getMessage());
//				e.printStackTrace();
//			}
//
//		}
//		return trId;
//	}
//	@Override
//	public String lastJsonLogData(String logbookname,int version,String jobid,String activityid) {
//		Connection con = null;
//		PreparedStatement pstm = null;
//		String sql="Select top(1) logbookdata from dbo.LogbookTransactionData where formname=? AND [version] = ? AND [JobId] = ? AND [ActivityId] = ? order by timestamp desc";
//		String celldata="";
//		try {
//			con = SQLServerDAOFactory.createConnection();
//			pstm = con.prepareStatement(sql);
//			pstm.setString(1, logbookname);
//			pstm.setInt(2, version);
//			pstm.setString(3, jobid);
//			pstm.setString(4, activityid);
//			ResultSet rs = pstm.executeQuery();
//			
//			while(rs.next()) {
//				celldata = rs.getString("logbookdata");
//			}
//		}catch(Exception e) {
//			ALERTOBJ.ShowErrorAlert(e.getMessage());			
//		}
//		finally {
//			try {
//				pstm.close();
//				con.close();
//			} catch (SQLException e) {
//				
//				e.printStackTrace();
//			}
//			
//		}
//		
//		return celldata;
//		// TODO Auto-generated method stub
//		
//	}
//	@Override
//	public void updateLogbookStatus(String formname, int version, boolean status) {
//		Connection conn = null;
//		try {
//			conn = SQLServerDAOFactory.createConnection();
//			conn.setAutoCommit(false);
//
//			String sql = "{call UpdateLogbookFormStatus(?,?,?)}";
//			CallableStatement cstmt = conn.prepareCall(sql);
//			cstmt.setString(1, formname);
//			cstmt.setInt(2, version);
//			cstmt.setBoolean(3, status);
//			int res = cstmt.executeUpdate();
//
//			
//			if(res>0)
//				ALERTOBJ.ShowInfoAlert("Status Updated");
//			else
//				ALERTOBJ.ShowWarningAlert("Unable to update status as the logbook is active");
//
//			conn.commit();
//
//			System.out.println("Data Commited");
//
//		} catch (Exception e) {
//			ALERTOBJ.ShowExceptionAlert(e.getMessage());
//			e.printStackTrace();
//			try {
//				conn.rollback();
//			} catch (SQLException e1) {
//				e1.printStackTrace();
//				ALERTOBJ.ShowExceptionAlert(e1.getMessage());
//			}
//		} finally {
//			try {
//				conn.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//				ALERTOBJ.ShowExceptionAlert(e.getMessage());
//			}
//
//		}
//		
//	}
//	@Override
//	public List<CellEditHistory> getCellHistory(String cellid,String cellname, String jobid, String activityid) {
//		List<CellEditHistory> data = new LinkedList<CellEditHistory>();
//		Connection con = null;
//		PreparedStatement pstm = null;
//		String sql="Declare @cellId as varchar(100)= ?, " + 
//				"@jobid as varchar(100) = ?, " + 
//				"@activityid as varchar(100) = ? " + 
//				"Select TransactionId,[timestamp],userid,val,reason from " + 
//				"( " + 
//				"Select TransactionId,userid,[timestamp],[value] jsondata from " + 
//				"( " + 
//				"Select TransactionId,userid,[timestamp],logbookdata from dbo.LogbookTransactionData Where JobId = @jobid AND ActivityId= @activityid " + 
//				")tab1 " + 
//				"CROSS APPLY(Select [value] from OpenJson(logbookdata) " + 
//				"Where [key] =@cellId) as tab2 " + 
//				")tab3 " + 
//				"CROSS APPLY(Select * from OpenJson(jsondata) " + 
//				"WITH (val varchar(max) '$.value', " + 
//				"reason varchar(max) '$.remarks' )) tab4 " + 
//				"Order by TransactionId";
//		try {
//			con = SQLServerDAOFactory.createConnection();
//			pstm = con.prepareStatement(sql);
//			pstm.setString(1, cellid);
//			pstm.setString(2, jobid);
//			pstm.setString(3, activityid);
//			ResultSet rs = pstm.executeQuery();
//			
//			while(rs.next()) {
//				data.add(new CellEditHistory(cellid, cellname, rs.getString("TransactionId"), rs.getString("timestamp"), rs.getString("val"), rs.getString("reason"),rs.getString("userid")));
//			}
//		}catch(Exception e) {
//			ALERTOBJ.ShowErrorAlert(e.getMessage());			
//		}
//		finally {
//			try {
//				pstm.close();
//				con.close();
//			} catch (SQLException e) {
//				
//				e.printStackTrace();
//			}
//			
//		}
//		return data;
//	}
//
//	@Override
//	public List<FormDetails> getUserIdWiseForms(String userid) {
//		List<FormDetails> forms = new ArrayList<FormDetails>();
//		Connection con = null;
//		PreparedStatement pstmt = null;
//		
//		try {	
//			List<String> groups = new ArrayList<String>();
//			con = SQLServerDAOFactory.createUserConnection();
//			pstmt = con.prepareStatement("SELECT Distinct [UserId],[UserRoll] FROM [dbo].[UserRoll] Where UserId = ?");
//			pstmt.setString(1, userid);
//			ResultSet rs = pstmt.executeQuery();
//			while(rs.next()) {
//				groups.add(rs.getString("UserRoll"));
//			}
//			con.close();
//			pstmt.close();
//			rs.close();
//			
//			con = SQLServerDAOFactory.createConnection();
//			pstmt = con.prepareStatement("Select Distinct FormId,FormName,VersionNumber,DocumentID,Department,UserGroup,formatID,isActiveForm From [dbo].[DigitalLogbookFormInfo]  Where isActiveForm = 1");
//			rs = pstmt.executeQuery();
//			while(rs.next()) {
//				List<String> fGroupList = new ArrayList<String>();
//				if(rs.getString("UserGroup")!= null && !rs.getString("UserGroup").trim().equals("")) {
//					fGroupList = Arrays.asList(rs.getString("UserGroup").split(","));
//				}
//				boolean isAvailable = false;
//				for(String ugr : groups) {
//					for(String fgr : fGroupList) {
//						if(ugr.equalsIgnoreCase(fgr)) {
//							isAvailable = true;
//						}
//					}
//					if(isAvailable)
//						break;
//				}
//				if(isAvailable && rs.getBoolean("isActiveForm"))
//					forms.add(new FormDetails(rs.getInt("FormId"), rs.getString("FormName").trim(), rs.getInt("VersionNumber"), rs.getString("DocumentID"), rs.getString("Department"), rs.getString("UserGroup"), rs.getString("FormatID"), rs.getBoolean("isActiveForm")));
//			}
//			rs.close();
//		} 
//		catch (Exception e) {
//			ALERTOBJ.ShowErrorAlert(e.getMessage());
//			e.printStackTrace();
//		}
//		finally {
//			try {
//				con.close();
//				pstmt.close();
//			} catch (SQLException e) { 
//				ALERTOBJ.ShowErrorAlert(e.getMessage());
//				e.printStackTrace();
//			}
//
//		}
//		return forms;
//	}
//
//	@Override
//	public Map<String, String> getQueryResultForGlobalCellUpdate(String query) {
//		Connection con = null;
//		PreparedStatement pstm = null;
//		Map<String,String> datamap = new LinkedHashMap<String,String>();
//		try {	
//			
//			con = SQLServerDAOFactory.createUserConnection();
//			pstm = con.prepareStatement(query);
//			ResultSet rs = pstm.executeQuery();
//			while(rs.next()) {
//				for(int i=1; i<= rs.getMetaData().getColumnCount(); i++) {
//					datamap.put(rs.getMetaData().getColumnName(i), rs.getString(i));
//				}
//				
//			}
//			pstm.close();
//			rs.close();
//			con.close();
//			
//		} 
//		catch (Exception e) {
//			e.printStackTrace();
//		}
//		finally {
//			try {
//				con.close();
//				pstm.close();
//			} catch (SQLException e) { 
//				
//				e.printStackTrace();
//			}
//
//		}
//		return datamap;
//	}
	
}
