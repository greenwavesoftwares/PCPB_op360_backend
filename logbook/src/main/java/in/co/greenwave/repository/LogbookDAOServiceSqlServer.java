package in.co.greenwave.repository;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

//import org.apache.jasper.tagplugins.jstl.core.Url;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.JsonProcessingException;

import javax.sql.DataSource;
import com.google.gson.Gson;
import in.co.greenwave.config.LogbookApiConfiguration;
import in.co.greenwave.dto.CellDto;
import in.co.greenwave.dto.FormDto;
import in.co.greenwave.dto.TransactionDto;
import in.co.greenwave.entity.CellEditHistory;
import in.co.greenwave.entity.CellInfo;
import in.co.greenwave.entity.DropDownInfo;
import in.co.greenwave.entity.FileTypeInfo;
import in.co.greenwave.entity.FileTypeUtil;
import in.co.greenwave.entity.FormInfo;
import in.co.greenwave.entity.Transaction;

//import io.minio.MinioClient;
//import io.minio.PutObjectArgs;

/**
 * @author Subhajit Khasnobish
 * 
 *         a DAO contains MS SQL server implementation of Database operations
 */
@Repository
public class LogbookDAOServiceSqlServer {

	private final LogbookApiConfiguration logbookApiConfiguration;

	private final DataSource OP360_Master_Tenant;

//	@Autowired
//	private MinioClient minioClient;

	// Using two JdbcTemplate tools to connect to two different databases.
	@Autowired
	@Qualifier("DatasourceCollections")
	Map<String, List<JdbcTemplate>> jdbcTemplateCollection;

	LogbookDAOServiceSqlServer(DataSource OP360_Master_Tenant, LogbookApiConfiguration logbookApiConfiguration) {
		this.OP360_Master_Tenant = OP360_Master_Tenant;
		this.logbookApiConfiguration = logbookApiConfiguration;
	} // Connects to another database.

	// Move this to the class level (constant)
	private static final Pattern HEX_PATTERN = Pattern.compile("0x[0-9A-Fa-f]+");

	//	/**
	//	 *  this is for formId which is used in multiple methods 
	//	 **/
	//	int formId;

	/**
	 * gets all logbooks
	 * 
	 * @return all forms from database
	 */
	public List<FormDto> getAllForms(String tenantId) {


		JdbcTemplate jdbcTemplateOp360 = jdbcTemplateCollection.get(tenantId).get(0);

		/**
		 * gets all logbooks 
		 */

		String sql = "SELECT [FormId], [FormName], [UserID], [SaveSQL], [TableSQL], [DeleteSQL], [CreationDate], [CreatedUser], [Department], [UserGroup], [DocumentID], [FormatID], [VersionNumber], [isActiveForm], [isPublicAccess], [ModifedBy], [ModifiedDate], [TenantId]"
//				+ ", [DashboardType]"
				+ " FROM [dbo].[DigitalLogbookFormInfo]";

		// Using queryForList to fetch multiple rows
		List<Map<String, Object>> rows = jdbcTemplateOp360.queryForList(sql);
		List<FormDto> allForms = new ArrayList<>();

		// Map each row to a FormInfo object
		for (Map<String, Object> row : rows) {
			FormDto formDto = new FormDto();
			formDto.setFormID((Integer) row.get("FormId"));
			formDto.setFormName((String) row.get("FormName"));
			formDto.setUserId((String) row.get("UserID"));
			formDto.setSaveSQL((String) row.get("SaveSQL"));
			formDto.setTableSQL((String) row.get("TableSQL"));
			formDto.setDeleteSQL((String) row.get("DeleteSQL"));
			formDto.setCreationDate((Timestamp) row.get("CreationDate"));
			formDto.setCreatedUser((String) row.get("CreatedUser"));
			formDto.setDepartment((String) row.get("Department"));
			formDto.setUserGroup((String) row.get("UserGroup"));
			formDto.setDocumentId((String) row.get("DocumentID"));
			formDto.setFormatID((String) row.get("FormatID"));
			formDto.setVersionNumber((Integer) row.get("VersionNumber"));
			formDto.setIsActiveForm((Boolean) row.get("isActiveForm"));
			formDto.setPublicAccess((Boolean) row.get("isPublicAccess"));
			formDto.setModifiedBy((String) row.get("ModifedBy"));
			formDto.setModifiedDate((Timestamp) row.get("ModifiedDate"));
			formDto.setTenantId((String) row.get("TenantId"));
//			formDto.setDashboardType((Boolean) row.get("DashboardType"));
			allForms.add(formDto);
		}

		return allForms;
	}

	/**
	 * 
	 * @return all active forms
	 */
	public List<FormInfo> getAllActiveForms(String tenantId) {

		JdbcTemplate jdbcTemplateOp360 = jdbcTemplateCollection.get(tenantId).get(0);

		/**
		 * gets all the forms which are active
		 */

		// SQL query to fetch active forms based on tenantId
		String query = "SELECT * FROM dbo.DigitalLogbookFormInfo where isActiveForm = 'true' and TenantId = ? ";

		Object[] args = { tenantId };

		/**
		 *  executes the query and gets the List of Map(row)
		 */
		List<Map<String, Object>> mapList = jdbcTemplateOp360.queryForList(query, args);

		ArrayList<FormInfo> formList = new ArrayList<FormInfo>();

		/**
		 * from each map gets the data by the column name and stores into an @FormInfo
		 * instance
		 */
		mapList.forEach(e -> {

			Integer formId = (Integer) e.get("FormId");
			String formName = (String) e.get("FormName");
			String userId = (String) e.get("UserID");
			String saveSql = (String) e.get("SaveSQL");
			String tableSql = (String) e.get("TableSQL");
			String deleteSql = (String) e.get("DeleteSQL");
			Timestamp creationDate = (Timestamp) e.get("CreationDate");
			String createdUser = (String) e.get("CreatedUser");
			String department = (String) e.get("Department");
			String userGroup = (String) e.get("UserGroup");
			String documentId = (String) e.get("DocumentID");
			String formatId = (String) e.get("FormatID");
			Integer versionNumber = (Integer) e.get("VersionNumber");
			Boolean isActiveUser = (Boolean) e.get("isActiveForm");
//			Boolean dashboardType = (Boolean) e.get("DashboardType");

			FormInfo formInfo = new FormInfo(formId, formName, userId, saveSql, tableSql, deleteSql, creationDate,
					createdUser, department, userGroup, documentId, formatId, versionNumber, isActiveUser
//					, dashboardType
					);

			formInfo.setTenantId(tenantId);

			formList.add(formInfo);
		});

		return formList;
	}

	/**
	 * saves all forms from the @java.util.List of @FormInfo
	 * 
	 * @param forms
	 * @return saved form/forms
	 */
	public List<FormInfo> saveLogbook(List<FormInfo> forms, String tenantId) {

		JdbcTemplate jdbcTemplateOp360 = jdbcTemplateCollection.get(tenantId).get(0);

		/**
		 * saves all the forms
		 */

		// SQL statement to insert a new form into the database
		String sql = "INSERT INTO [dbo].[DigitalLogbookFormInfo] ([FormId], [FormName], [UserID], [SaveSQL], [TableSQL], [DeleteSQL], [CreationDate], [CreatedUser], [Department], [UserGroup], [DocumentID], [FormatID], [VersionNumber], [isActiveForm], [TenantId]"
//				+ ", [DashboardType]"
				+ ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		// Batch update to insert multiple forms
		jdbcTemplateOp360.batchUpdate(sql, forms, forms.size(), (ps, formInfo) -> {
			ps.setInt(1, formInfo.getFormId());
			ps.setString(2, formInfo.getFormName());
			ps.setString(3, formInfo.getUserId());
			ps.setString(4, formInfo.getSaveSQL());
			ps.setString(5, formInfo.getTableSQL());
			ps.setString(6, formInfo.getDeleteSQL());
			ps.setTimestamp(7, formInfo.getCreationDate());
			ps.setString(8, formInfo.getCreatedUser());
			ps.setString(9, formInfo.getDepartment());
			ps.setString(10, formInfo.getUserGroup());
			ps.setString(11, formInfo.getDocumentID());
			ps.setString(12, formInfo.getFormatId());
			ps.setInt(13, formInfo.getVersionNumber());
			ps.setBoolean(14, formInfo.getIsActiveForm());
			ps.setString(15, formInfo.getTenantId());
//			ps.setBoolean(16, formInfo.isDashboardType());
		});

		return forms;
	}

	/**
	 * 
	 * executes the given queries in the @param
	 * 
	 * @param deleteQuery
	 * @param insertQuery
	 * @return all forms including effected ones
	 */
	public List<FormInfo> saveLogbookData(String deleteQuery, String insertQuery, String tenantId) {

		JdbcTemplate jdbcTemplateOp360 = jdbcTemplateCollection.get(tenantId).get(0);

		jdbcTemplateOp360.execute(deleteQuery);

		jdbcTemplateOp360.execute(insertQuery);

		String sql = "SELECT [FormId], [FormName], [UserID], [SaveSQL], [TableSQL], [DeleteSQL], [CreationDate], [CreatedUser], [Department], [UserGroup], [DocumentID], [FormatID], [VersionNumber], [isActiveForm], [TenantId]"
//				+ ", [DashboardType]"
				+ " FROM [dbo].[DigitalLogbookFormInfo]";

		// Using queryForList to fetch multiple rows
		List<Map<String, Object>> rows = jdbcTemplateOp360.queryForList(sql);
		List<FormInfo> allForms = new ArrayList<>();

		// Map each row to a FormInfo object
		for (Map<String, Object> row : rows) {
			FormInfo formInfo = new FormInfo();
			formInfo.setFormId((Integer) row.get("FormId"));
			formInfo.setFormName((String) row.get("FormName"));
			formInfo.setUserId((String) row.get("UserID"));
			formInfo.setSaveSQL((String) row.get("SaveSQL"));
			formInfo.setTableSQL((String) row.get("TableSQL"));
			formInfo.setDeleteSQL((String) row.get("DeleteSQL"));
			formInfo.setCreationDate((Timestamp) row.get("CreationDate"));
			formInfo.setCreatedUser((String) row.get("CreatedUser"));
			formInfo.setDepartment((String) row.get("Department"));
			formInfo.setUserGroup((String) row.get("UserGroup"));
			formInfo.setDocumentID((String) row.get("DocumentID"));
			formInfo.setFormatId((String) row.get("FormatID"));
			formInfo.setVersionNumber((Integer) row.get("VersionNumber"));
			formInfo.setIsActiveForm((Boolean) row.get("isActiveForm"));
			formInfo.setTenantId((String) row.get("TenantId"));
//			formInfo.setDashboardType((Boolean) row.get("DashboardType"));
			allForms.add(formInfo);
		}

		// Return the updated list of all forms from the repository
		return allForms;
	}

	/**
	 * 
	 * executes a SQL query and gets @java.util.List of @DropDownInfo
	 * 
	 * @param query
	 * @return @java.util.List of @DropDownInfo
	 */
	public List<DropDownInfo> getDropDownInfo(String query, String tenantId) {

		JdbcTemplate jdbcTemplateOp360 = jdbcTemplateCollection.get(tenantId).get(0);

		/**
		 *  List of DropDownInfo to be populated from the List of Map
		 */
		ArrayList<DropDownInfo> dropDownList = new ArrayList<DropDownInfo>();

		try {

			/**
			 * gets a @java.util.List of Rows as @java.util.Map
			 */
			List<Map<String, Object>> listMapDropDown = jdbcTemplateOp360.queryForList(query);

			/**
			 * gets the data from each @java.util.Map and creates an @DropDownInfo and adds
			 * that into @java.util.List
			 */
			listMapDropDown.forEach(mapDropDown -> {

				DropDownInfo dropDownInfo = new DropDownInfo(mapDropDown.get("ItemLabel").toString(),
						mapDropDown.get("ItemValue").toString());

				dropDownList.add(dropDownInfo);

			});

		} catch (DataAccessException e) {
			e.printStackTrace();
		}

		return dropDownList;

	}

	/**
	 * @author Subhajit Khasnobish Sreepriya Roy
	 * 
	 *         saves the @Transaction
	 * 
	 * @param transaction
	 * @return the saved transaction instance
	 */
//	public Transaction saveTransaction(TransactionDto transaction, String tenantId) {
//
//		JdbcTemplate jdbcTemplateOp360 = jdbcTemplateCollection.get(tenantId).get(0);
//
//		// Extracting the fields from the transaction object
//		String activityId = transaction.getActivityId();
//		String formName = transaction.getFormName();
//		String jobId = transaction.getJobId();
//
//		Map<String, Object> logbookData = transaction.getLogbookData();
//
//		String bucket = tenantId.toLowerCase();
//		Pattern pattern = HEX_PATTERN;
//
//		if (logbookData != null) {
//
//			for (Map.Entry<String, Object> a : logbookData.entrySet()) {
//
//				Object rowData = a.getValue();
//
//				if (rowData instanceof Map) {
//					Map<String, Object> rowMap = (Map<String, Object>) rowData;
//
//					if(rowMap.get("value") != null) {
//						Object actualValueObj = rowMap.get("value");
//
//						// Value is a List/Array of numbers
//						if (actualValueObj instanceof List) {
//							List<?> valueList = (List<?>) actualValueObj;
//							if (!valueList.isEmpty() && valueList.get(0) instanceof Number) {
//
//								byte[] bytes = new byte[valueList.size()];
//								for (int i = 0; i < valueList.size(); i++) {
//									bytes[i] = ((Number) valueList.get(i)).byteValue();
//								}
//								String objectKey = uploadToMinio(bucket, bytes);
//								rowMap.put("value", objectKey);
//							}
//						}
//					}
//				}
//			}
//		}
//		Gson gson = new Gson();
//		String logBookDataToString = gson.toJson(logbookData);
//
//		String role = transaction.getRole();
//		Timestamp timestamp = transaction.getTransactionTimestamp();
//		String transactionRemarks = transaction.getUserRemarks();
//		String userId = transaction.getUserId();
//		Integer version = transaction.getFormversion();
//		String transactionId = transaction.getTransactionId();
//
//		// SQL query to insert a new transaction record into the database
//		String insertSql = "INSERT INTO dbo.LogbookTransactionData (ActivityId, formname, JobId, logbookdata, role, timestamp, transaction_remarks, userid, version, TransactionId, TenantId) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
//
//		// Execute the insert query
//		int updatedRows = jdbcTemplateOp360.update(insertSql, activityId, formName, jobId, logBookDataToString, role, timestamp, transactionRemarks, userId, version, transactionId, tenantId);
//
//
//		if (updatedRows > 0) {
//			// SQL query to retrieve the saved transaction
//			String selectSql = "SELECT [slno], [TransactionId], [JobId], [ActivityId], [formname], [version], [logbookdata], [transaction_remarks], [timestamp], [userid], [role], [TenantId] FROM dbo.LogbookTransactionData WHERE TransactionId = ? AND TenantId = ?";
//
//			// Retrieve the saved transaction using queryForMap
//			try {
//				Map<String, Object> resultMap = jdbcTemplateOp360.queryForMap(selectSql, transactionId, tenantId);
//
//				// Convert the result to a Transaction object
//				return new Transaction(
//						(Long) resultMap.get("slno"),
//						(String) resultMap.get("TransactionId"),
//						(String) resultMap.get("JobId"),
//						(String) resultMap.get("ActivityId"),
//						(String) resultMap.get("formname"),
//						(Integer) resultMap.get("version"),
//						(String) resultMap.get("logbookdata"),
//						(String) resultMap.get("transaction_remarks"),
//						(Timestamp) resultMap.get("timestamp"),
//						(String) resultMap.get("userid"),
//						(String) resultMap.get("role"),
//						(String) resultMap.get("TenantId")
//						);
//			} catch (EmptyResultDataAccessException e) {
//				return null;
//			}
//		}
//
//		// Return null if the transaction was not saved or not found
//		System.out.println("Returning null because the transaction was not saved or not found");
//		return null;
//	}

	
	public Transaction saveTransaction(TransactionDto transaction, String tenantId) {

		JdbcTemplate jdbcTemplateOp360 = jdbcTemplateCollection.get(tenantId).get(0);

		// Extracting the fields from the transaction object
		String activityId = transaction.getActivityId();
		String formName = transaction.getFormName();
		String jobId = transaction.getJobId();

		Map<String, Object> logbookData = transaction.getLogbookData();
		Gson gson = new Gson();
		String logBookDataToString = gson.toJson(logbookData);

		String role = transaction.getRole();
		Timestamp timestamp = transaction.getTransactionTimestamp();
		String transactionRemarks = transaction.getUserRemarks();
		String userId = transaction.getUserId();
		Integer version = transaction.getFormversion();
		String transactionId = transaction.getTransactionId();

		// SQL query to insert a new transaction record into the database
		String insertSql = "INSERT INTO dbo.LogbookTransactionData (ActivityId, formname, JobId, logbookdata, role, timestamp, transaction_remarks, userid, version, TransactionId, TenantId) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		// Execute the insert query
		int updatedRows = jdbcTemplateOp360.update(insertSql, activityId, formName, jobId, logBookDataToString, role, timestamp, transactionRemarks, userId, version, transactionId, tenantId);


		if (updatedRows > 0) {
			// SQL query to retrieve the saved transaction
			String selectSql = "SELECT [slno], [TransactionId], [JobId], [ActivityId], [formname], [version], [logbookdata], [transaction_remarks], [timestamp], [userid], [role], [TenantId] FROM dbo.LogbookTransactionData WHERE TransactionId = ? AND TenantId = ?";

			// Retrieve the saved transaction using queryForMap
			try {
				Map<String, Object> resultMap = jdbcTemplateOp360.queryForMap(selectSql, transactionId, tenantId);

				// Convert the result to a Transaction object
				return new Transaction(
						(Long) resultMap.get("slno"),
						(String) resultMap.get("TransactionId"),
						(String) resultMap.get("JobId"),
						(String) resultMap.get("ActivityId"),
						(String) resultMap.get("formname"),
						(Integer) resultMap.get("version"),
						(String) resultMap.get("logbookdata"),
						(String) resultMap.get("transaction_remarks"),
						(Timestamp) resultMap.get("timestamp"),
						(String) resultMap.get("userid"),
						(String) resultMap.get("role"),
						(String) resultMap.get("TenantId")
						);
			} catch (EmptyResultDataAccessException e) {
				System.out.println("Transaction not found: " + transactionId);
				return null;
			}
		}

		// Return null if the transaction was not saved or not found
		System.out.println("Returning null because the transaction was not saved or not found");
		return null;
	}
	

	/**
	 * 
	 * gets @java.util.List of @FormInfo
	 * 
	 * @param formName
	 * @return all forms by the given form name
	 */
	public List<FormInfo> getFormInfoByFormName(String formName, String tenantId) {

		JdbcTemplate jdbcTemplateOp360 = jdbcTemplateCollection.get(tenantId).get(0);

		/**
		 * finds a logbook by form name and form info
		 */

		String sql = "SELECT [FormId], [FormName], [UserID], [SaveSQL], [TableSQL], [DeleteSQL], [CreationDate], [CreatedUser], [Department], [UserGroup], [DocumentID], [FormatID], [VersionNumber], [isActiveForm], [TenantId]"
//				+ ", [DashboardType]"
				+ " FROM [dbo].[DigitalLogbookFormInfo] WHERE [TenantId] = ? AND [FormName] = ?";

		Object[] args = { tenantId, formName };

		// Using queryForList to fetch multiple rows
		List<Map<String, Object>> rows = jdbcTemplateOp360.queryForList(sql, args);
		List<FormInfo> formInfos = new ArrayList<>();

		// Map each row to a FormInfo object
		for (Map<String, Object> row : rows) {
			FormInfo formInfo = new FormInfo();
			formInfo.setFormId((Integer) row.get("FormId"));
			formInfo.setFormName((String) row.get("FormName"));
			formInfo.setUserId((String) row.get("UserID"));
			formInfo.setSaveSQL((String) row.get("SaveSQL"));
			formInfo.setTableSQL((String) row.get("TableSQL"));
			formInfo.setDeleteSQL((String) row.get("DeleteSQL"));
			formInfo.setCreationDate((Timestamp) row.get("CreationDate"));
			formInfo.setCreatedUser((String) row.get("CreatedUser"));
			formInfo.setDepartment((String) row.get("Department"));
			formInfo.setUserGroup((String) row.get("UserGroup"));
			formInfo.setDocumentID((String) row.get("DocumentID"));
			formInfo.setFormatId((String) row.get("FormatID"));
			formInfo.setVersionNumber((Integer) row.get("VersionNumber"));
			formInfo.setIsActiveForm((Boolean) row.get("isActiveForm"));
			formInfo.setTenantId((String) row.get("TenantId"));
//			formInfo.setDashboardType((Boolean) row.get("DashboardType"));
			formInfos.add(formInfo);
		}

		return formInfos;
	}

	/**
	 * 
	 * gets all @CellInfo by a formName, versionNumber and tenantId
	 * 
	 * @param formName
	 * @param versionNumber
	 * @param tenantId
	 * @return all cells by the given name and version
	 */
	public List<CellDto> getAllCellInfoByFormNameAndVersion(String formName, Integer versionNumber, String tenantId) {

		JdbcTemplate jdbcTemplateOp360 = jdbcTemplateCollection.get(tenantId).get(0);

		List<CellDto> cellList = new ArrayList<>();
		try {

			// SQL query to retrieve the form ID based on the form name, version number, and tenant ID
			String formquery = "Select TOP 1 FormId from [dbo].[DigitalLogbookFormInfo] where FormName = ?   and VersionNumber = ? and TenantId = ? ";

			//			List<Map<String, Object>> listFormIds = jdbcTemplateOp360.queryForList(formquery, formName.trim(), versionNumber, tenantId);
			//			Map<String, Object> formIdMap = listFormIds.get(0);
			//			int formId = (int) formIdMap.get("FormId");

			Integer formId = jdbcTemplateOp360.queryForObject(
					formquery,
					Integer.class,
					formName.trim(),
					versionNumber,
					tenantId
					);

			//		if (!listFormIds.isEmpty()) {
			//
			//			System.out.println("listFormIds 2 : " + listFormIds);
			//
			//			Map<String, Object> formIdMap = listFormIds.get(0);
			//
			//			if (formIdMap.get("FormId") != null) {
			//
			//				formId = Integer.parseInt(formIdMap.get("FormId").toString());
			//			}
			//		} else {
			//			return null;
			//		}

			//			if (listFormIds.isEmpty()) {
			//				throw new NoSuchElementException("No form found for the given parameters.");
			//			}

			// SQL query to retrieve all cell information based on the form ID and tenant ID
			//			String cellQuery = "Declare @fromname as varchar(max)=? Declare @version as int=? Declare @tenantid as varchar(max)=? Declare @lastdata as varchar(max) Select top(1) @lastdata=c.logbookdata from dbo.LogbookTransactionData c cross apply OPENJSON(c.logbookdata) a where c.formname=@fromname and c.version=@version AND TenantId=@tenantid order by timestamp desc declare @table  Table([key1] varchar(max),[value1] varchar(max),type int); insert into @table Select * from OPENJSON(@lastdata) Declare @tablecellval as table(cell varchar(max),value1 varchar(max)); insert into @tablecellval Select a.key1 cell,t.[value] [value1]  from @table a cross apply OPENJSON(a.value1,'$') t where t.[key]='value' Select a.CellId [cellId] ,a.AliasId [aliasId] ,a.ColsSpan [colSpan] ,a.RowSpan [rowSpan] ,b.value1 [value] ,a.FieldType [fieldType] ,a.datepattern ,a.NumMinVal [minVal] ,a.NumMaxVal [maxVal] , a.AutoRefresh as autoRefresh, a.NumDecimalPoint [decimalPoints] ,a.Editable editable ,a.Dependent [dependent] ,a.DepSQL [depExpressionLogic] ,a.ImgHeight [imgHeight] ,a.ImgWidth  [imgWidth] ,a.CellCSS	[cellCSS] ,date=getdate(), a.requiredfield ,a.disabledexpression [disableExpressionLogic] ,case when a.disabledexptype=1 then 'Expression' else 'SQL' end as [disabledExpType] ,a.RefreshInterval [pollInterval] ,case when a.pollStartExpType=1 then 'Expression' else 'SQL' end as pollStartExpType ,a.pollStartExpression pollStartExpressionLogic ,case when a.pollStopExpType=1 then 'Expression' else 'SQL' end as pollStopExpType , case when a.globalCellExpType =1 then 'Rest API' else 'SQL' end as globalCellExpType ,a.pollStopExpression pollStopExpressionLogic ,a.apiMethod ,a.apiURL ,a.apiBody ,case when a.expressionfield=1 then 'Expression' else 'SQL' end as  depExpressionType ,a.ButtonIcon [buttonIcon] ,a.ButtonKeyValue [buttonKeyValue] ,a.ButtonLogic [buttonLogic] ,a.ButtonType [buttonType] ,a.globalSql [globalExpressionLogic] , a.ImagePath [imagePath], a.FileContent [fileContent] ,a.RowNum [rowNum] ,a.ColNum [colNum] ,a.url from dbo.DigitalLogbookCellDetails a left join  @tablecellval b on a.CellId=b.cell where a.FormId=(Select FormId from dbo.DigitalLogbookFormInfo b where b.FormName=@fromname and b.VersionNumber=@version and b.TenantId=@tenantid) and a.TenantId=@tenantid order by a.RowNum,a.ColNum";

			String cellQuery = "Declare @fromname as varchar(max)=? Declare @version as int=? Declare @tenantid as varchar(max)=? Declare @lastdata as varchar(max) Select top(1) @lastdata=c.logbookdata from dbo.LogbookTransactionData c cross apply OPENJSON(c.logbookdata) a where c.formname=@fromname and c.version=@version AND TenantId=@tenantid order by timestamp desc declare @table  Table([key1] varchar(max),[value1] varchar(max),type int); insert into @table Select * from OPENJSON(@lastdata) Declare @tablecellval as table(cell varchar(max),value1 varchar(max)); insert into @tablecellval Select a.key1 cell,t.[value] [value1]  from @table a cross apply OPENJSON(a.value1,'$') t where t.[key]='value' Select a.CellId [cellId] ,a.AliasId [aliasId] ,a.ColsSpan [colSpan] ,a.RowSpan [rowSpan] ,b.value1 [value] ,a.FieldType [fieldType] ,a.datepattern ,a.NumMinVal [minVal] ,a.NumMaxVal [maxVal] , a.AutoRefresh as autoRefresh, a.NumDecimalPoint [decimalPoints] ,a.Editable editable ,a.Dependent [dependent] ,a.DepSQL [depExpressionLogic] ,a.ImgHeight [imgHeight] ,a.ImgWidth  [imgWidth] ,a.CellCSS	[cellCSS] ,date=getdate(), a.requiredfield ,a.disabledexpression [disableExpressionLogic] ,case when a.disabledexptype=1 then 'Expression' else 'SQL' end as [disabledExpType] ,a.RefreshInterval [pollInterval] ,case when a.pollStartExpType=1 then 'Expression' else 'SQL' end as pollStartExpType ,a.pollStartExpression pollStartExpressionLogic ,case when a.pollStopExpType=1 then 'Expression' else 'SQL' end as pollStopExpType , case when a.globalCellExpType =1 then 'Rest API' else 'SQL' end as globalCellExpType ,a.pollStopExpression pollStopExpressionLogic ,a.apiMethod ,a.apiURL ,a.apiBody ,case when a.expressionfield=1 then 'Expression' else 'SQL' end as  depExpressionType ,a.ButtonIcon [buttonIcon] ,a.ButtonKeyValue [buttonKeyValue] ,a.ButtonLogic [buttonLogic] ,a.ButtonType [buttonType] ,a.globalSql [globalExpressionLogic] , a.ImagePath [imagePath], a.FileContent [fileContent] ,a.RowNum [rowNum] ,a.ColNum [colNum] ,a.url ,a.context from dbo.DigitalLogbookCellDetails a left join  @tablecellval b on a.CellId=b.cell where a.FormId=(Select FormId from dbo.DigitalLogbookFormInfo b where b.FormName=@fromname and b.VersionNumber=@version and b.TenantId=@tenantid) and a.TenantId=@tenantid order by a.RowNum,a.ColNum";

			// Retrieve dependent cell information
			String depSql = "Select CellId from dbo.DLBFormDependentFieldInfo where FormId = ? and DepCellId = ? and TenantId = ? ";

			// Retrieve dropdown information if the field type is a dropdown
			String drpSql = "select ItemLabel,ItemValue from dbo.DLBFormDropdownItems where FormId = ?  and CellId = ? and TenantId = ? ";

			List<Map<String, Object>> listCellMap = jdbcTemplateOp360.queryForList(cellQuery, formName.trim(), versionNumber, tenantId);

			if (listCellMap.isEmpty()) {
				throw new NoSuchElementException("No cell data found for the given form.");
			}

			/**
			 * gets the data from each @java.util.Map and stores into each @CellInfo adds to
			 * a @java.util.List along with gets Dependent @CellInfo and @DropDownInfo
			 */
			listCellMap.forEach(cell -> {

				CellDto cellInfo = new CellDto();

				String cellId = (String) cell.get("CellId");

				if (cell.get("rowNum") != null) {
					cellInfo.setRowNum((Integer) cell.get("rowNum"));
				} else {
					cellInfo.setRowNum(0); // Default value or handle null case
				}

				if (cell.get("colNum") != null) {
					cellInfo.setColNum((Integer) cell.get("colNum"));
				} else {
					cellInfo.setColNum(0); // Default value or handle null case
				}

				if (cell.get("cellId") != null) {
					cellInfo.setCellId((String) cell.get("cellId"));
				} else {
					cellInfo.setCellId(""); // Default value or handle null case
				}

				if (cell.get("aliasId") != null) {
					cellInfo.setAliasId((String) cell.get("aliasId"));
				} else {
					cellInfo.setAliasId(""); // Default value or handle null case
				}

				if (cell.get("colSpan") != null) {
					cellInfo.setColSpan((Integer) cell.get("colSpan"));
				} else {
					cellInfo.setColSpan(1); // Default value or handle null case
				}

				if (cell.get("rowSpan") != null) {
					cellInfo.setRowSpan((Integer) cell.get("rowSpan"));
				} else {
					cellInfo.setRowSpan(1); // Default value or handle null case
				}

				if (cell.get("date") != null) {
					cellInfo.setDate((Date) cell.get("date"));
				} else {
					cellInfo.setDate(null); // Default value or handle null case
				}

				if (cell.get("fieldType") != null) {
					cellInfo.setFieldType((String) cell.get("fieldType"));
				} else {
					cellInfo.setFieldType(""); // Default value or handle null case
				}

				if (cell.get("datepattern") != null) {
					cellInfo.setDatepattern((String) cell.get("datepattern"));
				} else {
					cellInfo.setDatepattern(""); // Default value or handle null case
				}

				if (cell.get("minVal") != null) {
					cellInfo.setMinVal((Double) cell.get("minVal"));
				} else {
					cellInfo.setMinVal(0.0); // Default value or handle null case
				}

				if (cell.get("maxVal") != null) {
					cellInfo.setMaxVal((Double) cell.get("maxVal"));
				} else {
					cellInfo.setMaxVal(0.0); // Default value or handle null case
				}

				if (cell.get("autoRefresh") != null) {
					cellInfo.setAutoRefresh((boolean) cell.get("autoRefresh"));
				} else {
					cellInfo.setAutoRefresh(false); // Default value or handle null case
				}

				if (cell.get("decimalPoints") != null) {
					cellInfo.setDecimalPoints((Integer) cell.get("decimalPoints"));
				} else {
					cellInfo.setDecimalPoints(0); // Default value or handle null case
				}

				if (cell.get("editable") != null) {
					cellInfo.setEditable((Boolean) cell.get("editable"));
				} else {
					cellInfo.setEditable(false); // Default value or handle null case
				}

				if (cell.get("dependent") != null) {
					cellInfo.setDependent((Boolean) cell.get("dependent"));
				} else {
					cellInfo.setDependent(false); // Default value or handle null case
				}

				if (cell.get("depExpressionLogic") != null) {
					cellInfo.setDepExpressionLogic((String) cell.get("depExpressionLogic").toString().trim().replaceAll("\n"," ").replaceAll("\t"," ").replaceAll("\r"," ").trim());
				} else {
					cellInfo.setDepExpressionLogic(""); // Default value or handle null case
				}

				if (cell.get("imgHeight") != null) {
					cellInfo.setImgHeight((Integer) cell.get("imgHeight"));
				} else {
					cellInfo.setImgHeight(0); // Default value or handle null case
				}

				if (cell.get("imgWidth") != null) {
					cellInfo.setImgWidth((Integer) cell.get("imgWidth"));
				} else {
					cellInfo.setImgWidth(0); // Default value or handle null case
				}

				if (cell.get("cellCSS") != null) {
					cellInfo.setCellCSS((String) cell.get("cellCSS"));
				} else {
					cellInfo.setCellCSS(""); // Default value or handle null case
				}

				if (cell.get("requiredfield") != null) {
					cellInfo.setRequiredfield((Boolean) cell.get("requiredfield"));
				} else {
					cellInfo.setRequiredfield(false); // Default value or handle null case
				}

				if (cell.get("disableExpressionLogic") != null) {
					cellInfo.setDisableExpressionLogic((String) cell.get("disableExpressionLogic"));
				} else {
					cellInfo.setDisableExpressionLogic(""); // Default value or handle null case
				}

				if (cell.get("disabledExpType") != null) {
					cellInfo.setDisabledExpType((String) cell.get("disabledExpType"));
				} else {
					cellInfo.setDisabledExpType(""); // Default value or handle null case
				}

				if (cell.get("pollInterval") != null) {
					cellInfo.setPollInterval((Integer) cell.get("pollInterval"));
				} else {
					cellInfo.setPollInterval(0); // Default value or handle null case
				}

				if (cell.get("pollStartExpType") != null) {
					cellInfo.setPollStartExpType((String) cell.get("pollStartExpType"));
				} else {
					cellInfo.setPollStartExpType(""); // Default value or handle null case
				}

				if (cell.get("pollStartExpressionLogic") != null) {
					cellInfo.setPollStartExpressionLogic((String) cell.get("pollStartExpressionLogic"));
				} else {
					cellInfo.setPollStartExpressionLogic(""); // Default value or handle null case
				}

				if (cell.get("pollStopExpType") != null) {
					cellInfo.setPollStopExpType((String) cell.get("pollStopExpType"));
				} else {
					cellInfo.setPollStopExpType(""); // Default value or handle null case
				}

				if (cell.get("globalCellExpType") != null) {
					cellInfo.setGlobalCellExpType((String) cell.get("globalCellExpType"));
				} else {
					cellInfo.setGlobalCellExpType(""); // Default value or handle null case
				}

				if (cell.get("pollStopExpressionLogic") != null) {
					cellInfo.setPollStopExpressionLogic((String) cell.get("pollStopExpressionLogic"));
				} else {
					cellInfo.setPollStopExpressionLogic(""); // Default value or handle null case
				}

				if (cell.get("apiMethod") != null) {
					cellInfo.setApiMethod((String) cell.get("apiMethod"));
				} else {
					cellInfo.setApiMethod(""); // Default value or handle null case
				}

				if (cell.get("apiURL") != null) {
					cellInfo.setApiUrl((String) cell.get("apiURL"));
				} else {
					cellInfo.setApiUrl(""); // Default value or handle null case
				}

				if (cell.get("apiBody") != null) {
					cellInfo.setApiBody((String) cell.get("apiBody"));
				} else {
					cellInfo.setApiBody(""); // Default value or handle null case
				}

				if (cell.get("depExpressionType") != null) {
					cellInfo.setDepExpressionType((String) cell.get("depExpressionType"));
				} else {
					cellInfo.setDepExpressionType(""); // Default value or handle null case
				}

				if (cell.get("globalExpressionLogic") != null) {
					cellInfo.setGlobalExpressionLogic((String) cell.get("globalExpressionLogic").toString().trim().replaceAll("\n"," ").replaceAll("\t"," ").replaceAll("\r"," ").trim());
				} else {
					cellInfo.setGlobalExpressionLogic(""); // Default value or handle null case
				}

				if (cell.get("buttonIcon") != null) {
					cellInfo.setButtonIcon((String) cell.get("buttonIcon"));
				} else {
					cellInfo.setButtonIcon(""); // Default value or handle null case
				}

				if (cell.get("buttonKeyValue") != null) {
					cellInfo.setButtonKeyValue((String) cell.get("buttonKeyValue"));
				} else {
					cellInfo.setButtonKeyValue(""); // Default value or handle null case
				}

				if (cell.get("buttonLogic") != null) {
					cellInfo.setButtonLogic((String) cell.get("buttonLogic"));
				} else {
					cellInfo.setButtonLogic(""); // Default value or handle null case
				}

				if (cell.get("buttonType") != null) {
					cellInfo.setButtonType((String) cell.get("buttonType"));
				} else {
					cellInfo.setButtonType(""); // Default value or handle null case
				}

				if (cell.get("imagePath") != null) {
					cellInfo.setImagePath((String) cell.get("imagePath"));
				} else {
					cellInfo.setImagePath(""); // Default value or handle null case
				}

				if (cell.get("fileContent") != null) {
					cellInfo.setFileContent((byte[]) cell.get("fileContent"));
				} else {
					cellInfo.setFileContent(new byte[0]); // Default value or handle null case
				}

				if (cell.get("url") != null) {
					cellInfo.setUrl((String) cell.get("url"));
				} else {
					cellInfo.setUrl(""); // Default value or handle null case
				}

				if (cell.get("context") != null) {
					cellInfo.setContext((String) cell.get("context"));
				} else {
					cellInfo.setContext(""); // Default value or handle null case
				}

				ArrayList<CellDto> depCellList = new ArrayList<CellDto>();
				ArrayList<DropDownInfo> dropDownList = new ArrayList<DropDownInfo>();

				List<Map<String, Object>> listMapCellDep = jdbcTemplateOp360.queryForList(depSql, formId, cellId, tenantId);

				/**
				 * gets the Dependent @CellInfo from each @java.util.Map
				 */
				listMapCellDep.forEach(depCell -> {

					String depCellId = (String) depCell.get("CellId");

					//				String sql = "select * from dbo.DigitalLogbookCellDetails where FormId = ? and CellId = ? and TenantId = ?";
					String sql = "Declare @fromname as varchar(max)=? Declare @version as int=? Declare @tenantid as varchar(max)=? Select t.cellId ,t.aliasId ,t.colSpan ,t.rowSpan ,t.fieldType ,t.datepattern ,t.minVal ,t.maxVal ,t.autoRefresh ,t.decimalPoints ,t.editable ,t.dependent ,t.depExpressionType ,t.depExpressionLogic ,t.imgHeight ,t.imgWidth ,t.cellCSS ,t.date ,t.requiredfield ,t.disableExpressionLogic ,t.disabledExpType ,t.pollInterval ,t.pollStartExpType ,t.pollStartExpressionLogic ,t.pollStopExpType ,t.pollStopExpressionLogic ,t.apiMethod ,t.apiURL ,t.apiBody ,t.buttonIcon ,t.buttonKeyValue ,t.buttonLogic ,t.buttonType ,t.globalExpressionLogic , t.imagePath, t.fileContent ,t.rowNum ,t.colNum from( Select a.CellId [cellId] ,a.AliasId [aliasId] ,a.ColsSpan [colSpan] ,a.RowSpan [rowSpan]  ,a.FieldType [fieldType] ,a.datepattern ,a.NumMinVal [minVal] ,a.NumMaxVal [maxVal] , a.AutoRefresh as autoRefresh, a.NumDecimalPoint [decimalPoints] ,a.Editable editable ,a.Dependent [dependent] ,a.DepSQL [depExpressionLogic] ,a.ImgHeight [imgHeight] ,a.ImgWidth  [imgWidth] ,a.CellCSS	[cellCSS] ,date=getdate(), a.requiredfield ,a.disabledexpression [disableExpressionLogic] ,case when a.disabledexptype=1 then 'Expression' else 'SQL' end as [disabledExpType] ,a.RefreshInterval [pollInterval] ,case when a.pollStartExpType=1 then 'Expression' else 'SQL' end as pollStartExpType ,a.pollStartExpression pollStartExpressionLogic ,case when a.pollStopExpType=1 then 'Expression' else 'SQL' end as pollStopExpType ,a.pollStopExpression pollStopExpressionLogic ,a.apiMethod ,a.apiURL ,a.apiBody ,case when a.expressionfield=1 then 'Expression' else 'SQL' end as  depExpressionType ,a.ButtonIcon [buttonIcon] ,a.ButtonKeyValue [buttonKeyValue] ,a.ButtonLogic [buttonLogic] ,a.ButtonType [buttonType] ,a.globalSql [globalExpressionLogic] , a.ImagePath [imagePath], a.FileContent [fileContent] ,a.RowNum [rowNum] ,a.ColNum [colNum] ,a.url from [dbo].DigitalLogbookCellDetails a where a.FormId=(Select FormId from [dbo].DigitalLogbookFormInfo b where b.FormName=@fromname and b.VersionNumber=@version and b.TenantId=@tenantid) and a.TenantId=@tenantid )as t where cellId=?";

					CellDto depCellInfo = jdbcTemplateOp360.queryForObject(sql,
							new BeanPropertyRowMapper<CellDto>(CellDto.class), formName.trim(), versionNumber, tenantId, depCellId);

					// Null check and default value assignment
					if (depCellInfo != null) {
						// Button related fields
						if (depCellInfo.getButtonKeyValue() == null) {
							depCellInfo.setButtonKeyValue(""); // Set default if null
						}

						if (depCellInfo.getButtonLogic() == null) {
							depCellInfo.setButtonLogic(""); // Set default if null
						}

						if (depCellInfo.getButtonIcon() == null) {
							depCellInfo.setButtonIcon(""); // Set default if null
						}

						if (depCellInfo.getButtonType() == null) {
							depCellInfo.setButtonType(""); // Set default if null
						}

						// API related fields
						if (depCellInfo.getApiMethod() == null) {
							depCellInfo.setApiMethod(""); // Set default if null
						}

						if (depCellInfo.getApiUrl() == null) {
							depCellInfo.setApiUrl(""); // Set default if null
						}

						if (depCellInfo.getApiBody() == null) {
							depCellInfo.setApiBody(""); // Set default if null
						}

						// Polling related fields
						if (depCellInfo.getPollStartExpressionLogic() == null) {
							depCellInfo.setPollStartExpressionLogic(""); // Set default if null
						}

						if (depCellInfo.getPollStartExpType() == null) {
							depCellInfo.setPollStartExpType(""); // Set default if null
						}

						if (depCellInfo.getPollStopExpressionLogic() == null) {
							depCellInfo.setPollStopExpressionLogic(""); // Set default if null
						}

						if (depCellInfo.getPollStopExpType() == null) {
							depCellInfo.setPollStopExpType(""); // Set default if null
						}

						// Dependent fields
						if (depCellInfo.getDepExpressionLogic() == null) {
							depCellInfo.setDepExpressionLogic(""); // Set default if null
						}

						if (depCellInfo.getDepExpressionType() == null) {
							depCellInfo.setDepExpressionType(""); // Set default if null
						}

						// General UI related fields
						if (depCellInfo.getCellCSS() == null) {
							depCellInfo.setCellCSS("text-align:center;"); // Set default CSS if null
						}

						if (depCellInfo.getFieldType() == null) {
							depCellInfo.setFieldType("Text"); // Default field type
						}

						if (depCellInfo.getDatepattern() == null) {
							depCellInfo.setDatepattern("dd/MM/yyyy"); // Set default date pattern
						}

						if (depCellInfo.getDate() == null) {
							depCellInfo.setDate(new Date()); // Set current date if null
						}

						// Numeric fields
						if (depCellInfo.getMinVal() == 0) {
							depCellInfo.setMinVal(-999D); // Set default minVal
						}

						if (depCellInfo.getMaxVal() == 0) {
							depCellInfo.setMaxVal(999D); // Set default maxVal
						}

						if (depCellInfo.getDecimalPoints() == 0) {
							depCellInfo.setDecimalPoints(2); // Default decimal points
						}

						// Image fields
						if (depCellInfo.getImgHeight() == 0) {
							depCellInfo.setImgHeight(30); // Set default image height
						}

						if (depCellInfo.getImgWidth() == 0) {
							depCellInfo.setImgWidth(30); // Set default image width
						}

						// Disabled fields
						if (depCellInfo.getDisableExpressionLogic() == null) {
							depCellInfo.setDisableExpressionLogic(""); // Set default if null
						}

						if (depCellInfo.getDisabledExpType() == null) {
							depCellInfo.setDisabledExpType(""); // Set default if null
						}

						// Miscellaneous
						if (depCellInfo.getGlobalExpressionLogic() == null) {
							depCellInfo.setGlobalExpressionLogic(""); // Set default if null
						}

						if(depCellInfo.getImagePath() == null) {
							depCellInfo.setImagePath("");
						}

						if (depCellInfo.getFileContent() == null) {
							depCellInfo.setFileContent(new byte[0]); // Set empty byte array if null
						}

						if (depCellInfo.getCellId() == null) {
							depCellInfo.setCellId(""); // Default cellId
						}

						if (depCellInfo.getAliasId() == null) {
							depCellInfo.setAliasId(""); // Default aliasId
						}

						if (depCellInfo.getDependentFields() == null) {
							depCellInfo.setDependentFields(new ArrayList<>()); // Default aliasId
						}

						if (depCellInfo.getUrl() == null) {
							depCellInfo.setUrl(""); // Default aliasId
						}
					}
					depCellList.add(depCellInfo);

				});

				if (cellInfo.getFieldType() != null && cellInfo.getFieldType().equalsIgnoreCase("DropDown")) {

					// String cellId = cellInfo.getCellId();

					List<Map<String, Object>> dropDownMapList = jdbcTemplateOp360.queryForList(drpSql, formId, cellId, tenantId);

					/**
					 * gets the @DropDownInfo from each @java.util.Map
					 */
					dropDownMapList.forEach(dropDownMap -> {

						DropDownInfo dropDownInfo = new DropDownInfo();

						dropDownInfo.setItemLabel(dropDownMap.get("ItemLabel").toString());
						dropDownInfo.setItemValue(dropDownMap.get("ItemValue").toString());

						dropDownList.add(dropDownInfo);
					});

				}

				cellInfo.setDependentFields(depCellList);
				cellInfo.setDropDownFields(dropDownList);

				cellList.add(cellInfo);

			});

			return cellList;

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("An error occurred while fetching cell information.", e);
		}
	}

	/**
	 * gets @CellDto by formName, versionNumber and cellId
	 * 
	 * @param formName
	 * @param versionNumber
	 * @param cellId
	 * @return @CellInfo by the name,version and cell ID
	 */
	public CellDto getCellInfo(String formName, Integer versionNumber, String cellId, String tenantId) {

		JdbcTemplate jdbcTemplateOp360 = jdbcTemplateCollection.get(tenantId).get(0);

		// Retrieve FormId based on formName and versionNumber
		//		Integer formId = jdbcTemplateOp360.queryForObject("select FormId from dbo.DigitalLogbookFormInfo where FormName = '"
		//				+ formName + "' and VersionNumber = " + versionNumber, Integer.class);

		String sql = "SELECT FormId FROM dbo.DigitalLogbookFormInfo WHERE FormName = ? AND VersionNumber = ?";
		Map<String, Object> resultMap = jdbcTemplateOp360.queryForMap(sql, formName, versionNumber);

		// Retrieve the FormId from the map
		Integer formId = (Integer) resultMap.get("FormId");

		// SQL query to get main cell information
		String mainSql = "select [FormId] ,[RowNum] ,[ColNum] ,[CellId] ,[AliasId] ,[ColsSpan] ,[RowSpan] ,[FieldType] ,[NumMinVal] ,[NumMaxVal] ,[NumDecimalPoint] ,[Editable] ,[Dependent] ,[DbColName] ,[DepSQL] ,[GlobalSQL] ,[ImagePath] ,[FileContent] ,[ImgHeight] ,[ImgWidth] ,[CellCSS] ,[ComponentCSS] ,[SaveSQL] ,[AjaxUpdateStr] ,[AutoRefresh] ,[RefreshInterval] ,[apiURL] ,[apiBody] ,[apiMethod] ,[VersionNumber] ,[requiredfield] ,[expressionfield] ,[disabledexpression] ,[disabledexptype] ,[datepattern] ,[pollStartExpType] ,[pollStartExpression] ,[pollStopExpType] ,[pollStopExpression] ,[ButtonIcon] ,[ButtonKeyValue] ,[ButtonArguments] ,[ButtonLogic] ,[ButtonClassName] ,[ButtonType] ,[url] ,[TenantId] from dbo.DigitalLogbookCellDetails " + " where FormId = " + formId + "and CellId = '" + cellId + "'";

		// SQL query to get dependent cell IDs
		//		String depSql = "Select DepCellId from dbo.DLBFormDependentFieldInfo where FormId = " + formId
		//				+ " and CellId = '" + cellId + "'";

		String depSql = "SELECT DepCellId FROM dbo.DLBFormDependentFieldInfo WHERE FormId = ? AND CellId = ?";


		// SQL query to get dropdown items
		//		String drpSql = "Select ItemLabel,ItemValue from dbo.DLBFormDropdownItems where FormId = " + formId
		//				+ " and CellId = '" + cellId + "'";

		String drpSql = "SELECT ItemLabel, ItemValue FROM dbo.DLBFormDropdownItems WHERE FormId = ? AND CellId = ?";


		// Retrieve cell information
		CellInfo cellInfo = jdbcTemplateOp360.queryForObject(mainSql, new BeanPropertyRowMapper<CellInfo>(CellInfo.class));

		CellDto cellDto = new CellDto(cellInfo);

		ArrayList<CellDto> depCellDtoList = new ArrayList<CellDto>();
		ArrayList<DropDownInfo> dropDownList = new ArrayList<DropDownInfo>();

		if (cellInfo != null && cellInfo.getDependent()) {

			cellId = cellInfo.getCellId();

			List<Map<String, Object>> listMapDep = jdbcTemplateOp360.queryForList(depSql, formId, cellId);

			/**
			 * gets data from each @java.util.Map into the instance of @CellDto and adds
			 * into
			 * 
			 * @java.util.List
			 */
			listMapDep.forEach(depCellMap -> {

				String depCellId = (String) depCellMap.get("DepCellId");

				/**
				 *  gets the logbook as @List of @CellInfo 
				 */
				String query = "select * from dbo.DigitalLogbookCellDetails where FormId = " + formId + " and CellId = '" + depCellId + "'";

				CellDto depCellDto = jdbcTemplateOp360.queryForObject(query,
						new BeanPropertyRowMapper<CellDto>(CellDto.class));

				depCellDtoList.add(depCellDto);

			});

		}

		// Check if the cell type is a dropdown
		if (cellInfo != null && cellInfo.getFieldType().equalsIgnoreCase("DropDown")) {

			// Retrieve dropdown items
			List<Map<String, Object>> listMapDrop = jdbcTemplateOp360.queryForList(drpSql, formId, cellId);

			/**
			 * gets data from each @java.util.Map into the instance of @DropDownInfo and
			 * adds into
			 * 
			 * @java.util.List
			 */
			listMapDrop.forEach(dropDownMap -> {

				String itemLabel = dropDownMap.get("ItemLabel").toString();
				String itemValue = dropDownMap.get("ItemValue").toString();

				DropDownInfo dropDownInfo = new DropDownInfo();

				dropDownInfo.setItemLabel(itemLabel);
				dropDownInfo.setItemValue(itemValue);

				dropDownList.add(dropDownInfo);
			});
		}

		cellDto.setDependentFields(depCellDtoList);
		cellDto.setDropDownFields(dropDownList);

		return cellDto;
	}

	/**
	 * Executes an sql query to get DropDown data (dropdown specific API)
	 * 
	 * @param query
	 * @return @java.util.List of @DropDownInfo
	 */
	public List<DropDownInfo> getDropDownsforQuery(String query, String tenantId) {

		JdbcTemplate jdbcTemplateOp360 = jdbcTemplateCollection.get(tenantId).get(0);

		ArrayList<DropDownInfo> listDropDown = new ArrayList<DropDownInfo>();

		try {

			List<Map<String, Object>> listDropdownMap = jdbcTemplateOp360.queryForList(query);

			listDropdownMap.forEach(dropDownMap -> {

				DropDownInfo dropDownInfo = null;

				/**
				 * if there is only one column then repeats that into item label and item value
				 * otherwise gets both the data from @java.util.Map
				 */
				if (dropDownMap.keySet().size() == 1) {

					String firstColumn = dropDownMap.keySet().iterator().next();

					String firstValue = dropDownMap.get(firstColumn).toString();

					dropDownInfo = new DropDownInfo(firstValue, firstValue);

				}

				// Otherwise, use the first column as the label and the second as the value
				else if (dropDownMap.keySet().size() > 1) {

					Iterator<String> iterator = dropDownMap.keySet().iterator();

					String firstColumn = iterator.next();

					String itemLabel = dropDownMap.get(firstColumn).toString();

					String secondColumn = iterator.next();

					String itemValue = dropDownMap.get(secondColumn).toString();

					dropDownInfo = new DropDownInfo(itemLabel, itemValue);
				}

				listDropDown.add(dropDownInfo);

			});

		} catch (DataAccessException e) {
			e.printStackTrace();
		}

		return listDropDown;
	}

	/**
	 * Gets a dynamic map based on the sql query for Cells which are not DropDowns
	 * 
	 * @param query
	 * @return @java.lang.Object
	 * @throws SQLException
	 * @throws JsonProcessingException
	 */
//	public Map<String, Object> getQueryResultForGlobalCellUpdate(String query, String tenantId) throws JsonProcessingException {
//
//		JdbcTemplate jdbcTemplateOp360 = jdbcTemplateCollection.get(tenantId).get(0);
//		String bucket = tenantId.toLowerCase();
//
//		/*
//		 * Optimized MinIO implementation
//		 */
//
//		Matcher matcher = HEX_PATTERN.matcher(query);
//
//		// Cache to map HexString -> MinIO Object Key
//		// This ensures identical files are uploaded only once and share the same key
//		Map<String, String> uniqueFileCache = new HashMap<>();
//
//		while (matcher.find()) {
//			String fullHexString = matcher.group();
//
//			// ONLY process if we haven't uploaded this exact binary content yet
//			if (!uniqueFileCache.containsKey(fullHexString)) {
//				try {
//					String hexData = fullHexString.substring(2); // Remove '0x'
//					byte[] bytes = hexStringToByteArray(hexData);
//
//					// Detect file type
//					FileTypeInfo info = FileTypeUtil.detect(bytes);
//					String objectKey = UUID.randomUUID() + info.extension;
//
//					// Upload to MinIO
//					try (InputStream input = new ByteArrayInputStream(bytes)) {
//						minioClient.putObject(
//								PutObjectArgs.builder()
//								.bucket(bucket)
//								.object(objectKey)
//								.stream(input, bytes.length, -1)
//								.contentType(info.mimeType)
//								.build()
//								);
//					}
//
//					uniqueFileCache.put(fullHexString, objectKey);
//
//					System.out.println("Uploaded new unique file: " + objectKey);
//
//				} catch (Exception e) {
//					throw new RuntimeException("Error uploading file to MinIO: " + e.getMessage(), e);
//				}
//			}
//		}
//
//		System.out.println("Final Unique Keys: " + uniqueFileCache.values());
//
//		for (Map.Entry<String, String> entry : uniqueFileCache.entrySet()) {
//			query = query.replace(entry.getKey(), "'" + entry.getValue() + "'");
//		}
//
//		/*
//		 * End MinIO implementation
//		 */
//
//		final String finalQuery = query;
//
//		return jdbcTemplateOp360.execute((ConnectionCallback<Map<String, Object>>) connection -> {
//
//			Map<String, Object> lastSelectResult = new HashMap<>();
//
//			try (Statement stmt = connection.createStatement()) {
//				boolean hasResultSet = stmt.execute(finalQuery);
//
//				while (true) {
//
//					if (hasResultSet) {
//						try (ResultSet rs = stmt.getResultSet()) {
//							ResultSetMetaData meta = rs.getMetaData();
//							int columnCount = meta.getColumnCount();
//
//							System.out.println("columnCount : " + columnCount);
//
//							if (rs.next()) {
//								lastSelectResult.clear(); // ensure last SELECT wins
//								for (int i = 1; i <= columnCount; i++) {
//									Object value = rs.getObject(i);
//									String column = meta.getColumnLabel(i);
//
//									if (value instanceof java.sql.Date) {
//										value = new SimpleDateFormat("yyyy-MM-dd").format(value);
//									} else if (value instanceof java.sql.Timestamp) {
//										value = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(value);
//									} else if (value instanceof java.sql.Time) {
//										value = new SimpleDateFormat("HH:mm:ss").format(value);
//									}
//
//									lastSelectResult.put(column, value);
//								}
//							}
//						} catch (Exception e) {
//							System.err.println("Error processing ResultSet: " + e.getMessage());
//							return Collections.singletonMap("error",e.getMessage());
//						}
//					} else {
//						int updateCount = stmt.getUpdateCount();
//						if (updateCount == -1) {
//							break; // no more results
//						}
//					}
//
//					hasResultSet = stmt.getMoreResults();
//				}
//			} 
//
//			catch (SQLException e) {
//				System.err.println("SQL execution error: " + e.getMessage());
//				throw e; // propagate SQL exceptions properly
//			}
//
//			return lastSelectResult.isEmpty() ? null : lastSelectResult;
//		});
//		
//	}
	
	public Map<String, Object> getQueryResultForGlobalCellUpdate(String query, String tenantId) {

	    JdbcTemplate jdbcTemplateOp360 = jdbcTemplateCollection.get(tenantId).get(0);
	    final String finalQuery = query;

	    return jdbcTemplateOp360.execute((ConnectionCallback<Map<String, Object>>) connection -> {

	        Map<String, Object> lastSelectResult = new HashMap<>();

	        try (Statement stmt = connection.createStatement()) {

	            boolean hasResultSet = stmt.execute(finalQuery);

	            while (true) {

	                if (hasResultSet) {
	                    try (ResultSet rs = stmt.getResultSet()) {

	                        ResultSetMetaData meta = rs.getMetaData();
	                        int columnCount = meta.getColumnCount();

	                        if (rs.next()) {
	                            lastSelectResult.clear(); // last SELECT wins

	                            for (int i = 1; i <= columnCount; i++) {
	                                Object value = rs.getObject(i);
	                                String column = meta.getColumnLabel(i);

	                                if (value instanceof java.sql.Date) {
	                                    value = new SimpleDateFormat("yyyy-MM-dd").format(value);
	                                } else if (value instanceof java.sql.Timestamp) {
	                                    value = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(value);
	                                } else if (value instanceof java.sql.Time) {
	                                    value = new SimpleDateFormat("HH:mm:ss").format(value);
	                                }

	                                lastSelectResult.put(column, value);
	                            }
	                        }
	                    }
	                } else {
	                    int updateCount = stmt.getUpdateCount();
	                    if (updateCount == -1) {
	                        break; // no more results
	                    }
	                }

	                hasResultSet = stmt.getMoreResults();
	            }

	        } catch (SQLException e) {
	            System.err.println("SQL execution error: " + e.getMessage());
	            throw e;
	        }

	        return lastSelectResult.isEmpty() ? null : lastSelectResult;
	    });
	}


	/**
	 * gets all transactions
	 * 
	 * @param jobId
	 * @param activityId
	 * @param formName
	 * @param formVersion
	 * @return List of transactions
	 */
	public List<Transaction> getAllTransactions(String jobId, String activityId, String formName, int formVersion,
			String tenantId) {

		JdbcTemplate jdbcTemplateOp360 = jdbcTemplateCollection.get(tenantId).get(0);

		String query = "select top(1) * from dbo.LogbookTransactionData where JobId = ? and ActivityId = ? and formname = ? and version = ? and TenantId = ? order by timestamp desc ";

		Object[] args = { jobId, activityId, formName, formVersion, tenantId };

		List<Map<String, Object>> rowMapList = jdbcTemplateOp360.queryForList(query, args);

		ArrayList<Transaction> transactionList = new ArrayList<Transaction>();

		/**
		 * gets the data from each of the @java.util.Map and stores into an instance of
		 * 
		 * @Transaction
		 */
		rowMapList.forEach(row -> {

			Transaction transaction = new Transaction();

			transaction.setSlno((Long) row.get("slno"));
			transaction.setTransactionId(row.get("TransactionId").toString());
			transaction.setJobId(row.get("JobId").toString());
			transaction.setActivityId(row.get("ActivityId").toString());
			transaction.setFormName(row.get("formname").toString());
			transaction.setVersion((Integer) row.get("version"));
			transaction.setLogbookData(row.get("logbookdata").toString());

			if (row.get("transaction_remarks") != null)
				transaction.setTransactionRemarks(row.get("transaction_remarks").toString());

			transaction.setTimestamp((Timestamp) row.get("timestamp"));
			transaction.setUserId(row.get("userid").toString());
			transaction.setRole(row.get("role").toString());

			transactionList.add(transaction);

		});

		System.out.println("transactionList.size() : " + transactionList.size());

		return transactionList;
	}

	/**
	 * 
	 * @param cellid
	 * @param jobid
	 * @param activityid
	 * @return List of CellEdithistory
	 */
	public List<CellEditHistory> getCellHistory(String cellid, String jobid, String activityid, String tenantId) {

		JdbcTemplate jdbcTemplateOp360 = jdbcTemplateCollection.get(tenantId).get(0);

		// Query to retrieve the edit history of a cell for a specific job and activity
		String query = "Declare @cellId as varchar(100)= ?, @jobid as varchar(100) = ?, @activityid as varchar(100) = ? Select TransactionId,[timestamp],userid,val,reason from (Select TransactionId,userid,[timestamp],[value] jsondata from (Select TransactionId,userid,[timestamp],logbookdata from dbo.LogbookTransactionData Where JobId = @jobid AND ActivityId= @activityid)tab1 CROSS APPLY(Select [value] from OpenJson(logbookdata) Where [key] =@cellId) as tab2)tab3 CROSS APPLY(Select * from OpenJson(jsondata) WITH (val varchar(max) '$.value', reason varchar(max) '$.remarks' )) tab4 Order by TransactionId";

		Object[] args = { cellid, jobid, activityid };

		// Execute the query and map the results to CellEditHistory objects
		List<Map<String, Object>> rowMapList = jdbcTemplateOp360.queryForList(query, args);

		ArrayList<CellEditHistory> cellEditHistoryList = new ArrayList<>();

		/**
		 * gets the data from each of the @java.util.Map and stores into an instance of
		 * 
		 * @Transaction
		 */
		rowMapList.forEach(row -> {

			CellEditHistory cellEditHistory = new CellEditHistory();

			cellEditHistory.setCellId(cellid);
			cellEditHistory.setTransactionId(row.get("TransactionId").toString());
			cellEditHistory.setTimestamp(row.get("timestamp").toString());
			if (row.get("val") != null) {
				cellEditHistory.setValue(row.get("val").toString());
			}
			else {
				cellEditHistory.setValue(null);
			}
			cellEditHistory.setReason(row.get("reason").toString());
			cellEditHistory.setUserid(row.get("userid").toString());
			cellEditHistoryList.add(cellEditHistory);

		});
		return cellEditHistoryList;
	}

	/**
	 * 
	 * @param formName
	 * @param version
	 * @return @java.util.List of @Transaction
	 */
	public List<Transaction> getTransactionsByFormNameAndVersion(String formName, int version, String tenantId) {

		JdbcTemplate jdbcTemplateOp360 = jdbcTemplateCollection.get(tenantId).get(0);

		// SQL query to fetch transactions based on formName and version
		String sql = "SELECT * FROM dbo.LogbookTransactionData WHERE formname = ? AND version = ?";

		// Retrieve the transactions using JdbcTemplate
		List<Map<String, Object>> resultList = jdbcTemplateOp360.queryForList(sql, formName, version);

		// Convert the result list to a list of Transaction objects
		List<Transaction> transactions = new ArrayList<>();
		for (Map<String, Object> row : resultList) {
			Transaction transaction = new Transaction(
					(Long) row.get("slno"),
					(String) row.get("TransactionId"),
					(String) row.get("JobId"),
					(String) row.get("ActivityId"),
					(String) row.get("formname"),
					(Integer) row.get("version"),
					(String) row.get("logbookdata"),
					(String) row.get("transaction_remarks"),
					(Timestamp) row.get("timestamp"),
					(String) row.get("userid"),
					(String) row.get("role"),
					(String) row.get("TenantId")
					);
			transactions.add(transaction);
		}

		return transactions;
	}


	/**
	 * gets logbooks by user id
	 * 
	 * @param userId
	 * @return @java.util.List of @java.util.Map
	 */
	public List<Map<String, Object>> getLogbooksByUserId(String userId, String tenantId) {

		JdbcTemplate jdbcTemplateOp360 = jdbcTemplateCollection.get(tenantId).get(0);

		// SQL query to select logbook information for the given user ID where the
		// logbook is active
		String query = "SELECT [FormId] ,[FormName] ,[UserID] ,[SaveSQL] ,[TableSQL] ,[DeleteSQL] ,[CreationDate] ,[CreatedUser] ,[Department] ,[UserGroup] ,[DocumentID] ,[FormatID] ,[VersionNumber] ,[isActiveForm] FROM [dbo].[DigitalLogbookFormInfo] where UserID = ? and isActiveForm = 1 ";

		// Executes the query with the provided user ID as a parameter
		Object[] args = { userId };
		List<Map<String, Object>> logbooks = jdbcTemplateOp360.queryForList(query, args);
		return logbooks;
	}

	/**
	 * 
	 * 
	 * @param userId
	 * @param tenantId
	 * @return @java.util.List of @java.util.Map
	 */
	public List<Map<String, Object>> getlogbooksByUserIdAndTenantId(String userId, String tenantId) {

		JdbcTemplate jdbcTemplateOp360 = jdbcTemplateCollection.get(tenantId).get(0);

		// SQL query to select logbook information for the given user ID and tenant ID
		// where the logbook is active
		String query = "SELECT [FormId] ,[FormName] ,[UserID] ,[SaveSQL] ,[TableSQL] ,[DeleteSQL] ,[CreationDate] ,[CreatedUser] ,[Department] ,[UserGroup] ,[DocumentID] ,[FormatID] ,[VersionNumber] ,[isActiveForm] FROM [dbo].[DigitalLogbookFormInfo] where UserID = ? and TenantId = ? and isActiveForm = 1 ";
		// Executes the query with the provided user ID and tenant ID as parameters
		Object[] args = { userId, tenantId };

		/**
		 *  gets logbooks by user id and tenant id as Map
		 */
		List<Map<String, Object>> logbooks = jdbcTemplateOp360.queryForList(query, args);
		return logbooks;
	}

//	private byte[] hexStringToByteArray(String hex) {
//		int len = hex.length();
//		byte[] data = new byte[len / 2];
//
//		for (int i = 0; i < len; i += 2) {
//			data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
//					+ Character.digit(hex.charAt(i + 1), 16));
//		}
//		return data;
//	}

//	// Helper method to avoid duplicating MinIO upload code
//	private String uploadToMinio(String bucket, byte[] bytes) {
//		FileTypeInfo info = FileTypeUtil.detect(bytes);
//		String objectKey = UUID.randomUUID() + info.extension;
//
//		try (InputStream input = new ByteArrayInputStream(bytes)) {
//			minioClient.putObject(
//					PutObjectArgs.builder()
//					.bucket(bucket)
//					.object(objectKey)
//					.stream(input, bytes.length, -1)
//					.contentType(info.mimeType)
//					.build()
//					);
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new RuntimeException("Error uploading file to MinIO: " + e.getMessage(), e);
//		}
//		return objectKey;
//	}
}
