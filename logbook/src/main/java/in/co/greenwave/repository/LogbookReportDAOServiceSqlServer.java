package in.co.greenwave.repository; // This package contains repository classes for data access.

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.layout.LayoutArea;
import com.itextpdf.layout.layout.LayoutContext;
import com.itextpdf.layout.layout.LayoutResult;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import com.itextpdf.layout.renderer.CellRenderer;
import com.itextpdf.layout.renderer.DrawContext;
import in.co.greenwave.config.LogbookApiConfiguration;
import in.co.greenwave.dto.CellDetailsDto;
import in.co.greenwave.dto.CellDto;
import in.co.greenwave.dto.ReportConfigDto;
import in.co.greenwave.dto.ReportData;
import in.co.greenwave.dto.TransactionAttachmentDto;
import in.co.greenwave.dto.TransactionDto;
import in.co.greenwave.entity.FormInfo;
import in.co.greenwave.entity.ReportConfigEntity;
import in.co.greenwave.entity.Transaction;
import in.co.greenwave.entity.UserEntity;
import in.co.greenwave.entity.UserModel;

/**
 * This class manages the data access for logbook reports.
 * It retrieves report configuration information from the database.
 */
@Repository // This annotation indicates that this class is a repository, used for data access.
public class LogbookReportDAOServiceSqlServer {

	private final LogbookApiConfiguration logbookApiConfiguration;

	// Autowired to automatically inject the required dependencies.
	@Autowired
	LogbookDAOServiceSqlServer logbookRepositoryImpl; // A reference to another repository for logbook data.

	// Using two JdbcTemplate tools to connect to two different databases.
	@Autowired
	@Qualifier("DatasourceCollections")
	Map<String, List<JdbcTemplate>> jdbcTemplateCollection; // Connects to another database.

	@Autowired
	@Qualifier("jdbcTemplate_OP360_tenant")
	JdbcTemplate jdbcTemplateOp360Tenant; // Another JdbcTemplate for Tenant database operations.

	int cellRowNum = 1;

	LogbookReportDAOServiceSqlServer(LogbookApiConfiguration logbookApiConfiguration) {
		this.logbookApiConfiguration = logbookApiConfiguration;
	} // A variable to keep track of the row number for cell data.

	/**
	 * This method retrieves all logbook reports for a specific tenant.
	 *
	 * @param tenantId The ID of the tenant whose reports we want to retrieve.
	 * @return A list of ReportConfigEntity containing report configuration data.
	 */
	public List<ReportConfigEntity> getAllLogBookReports(String tenantId) {

		JdbcTemplate jdbcTemplateOp360 = jdbcTemplateCollection.get(tenantId).get(0);

		// SQL query to select report configurations from the database based on tenant ID.
		String sql = "SELECT [ReportName], [FormId], [FormName], [VersionNumber], [CellId], [ReportAliasId], [Creator], [SharedUser], [FormAliasId], [TenantId] FROM [dbo].[reportconfigurator] WHERE [TenantId] = ?";

		Object[] args = { tenantId }; // Arguments for the SQL query.

		// Execute the query and get a list of maps (each map represents a row of data).
		List<Map<String, Object>> rows = jdbcTemplateOp360.queryForList(sql, args);

		// Map each row to a ReportConfigEntity object.
		List<ReportConfigEntity> reportInfo = rows.stream().map(row -> {
			ReportConfigEntity reportConfig = new ReportConfigEntity(); // Create a new ReportConfigEntity object.
			reportConfig.setReportName((String) row.get("ReportName")); // Set the report name.
			reportConfig.setFormId((Integer) row.get("FormId")); // Set the form ID.
			reportConfig.setFormName((String) row.get("FormName")); // Set the form name.
			reportConfig.setVersionNumber((Integer) row.get("VersionNumber")); // Set the version number.
			reportConfig.setCellId((String) row.get("CellId")); // Set the cell ID.
			reportConfig.setReportAliasId((String) row.get("ReportAliasId")); // Set the report alias ID.
			reportConfig.setCreator((String) row.get("Creator")); // Set the creator's name.
			reportConfig.setSharedUser((String) row.get("SharedUser")); // Set the shared user.
			reportConfig.setFormAliasId((String) row.get("FormAliasId")); // Set the form alias ID.
			reportConfig.setTenantId((String) row.get("TenantId")); // Set the tenant ID.
			return reportConfig; // Return the populated ReportConfigEntity object.
		}).toList(); // Convert the stream back to a list.

		return reportInfo; // Return the list of report configurations.
	}

	/**
	 * This method gets all unique report names shared by a specific creator.
	 *
	 * @param tenantId The ID of the tenant.
	 * @param creator The name of the creator to filter reports.
	 * @return A list of distinct report names.
	 */
	public List<String> getAllDistinctReportNames(String tenantId, String creator) {

		JdbcTemplate jdbcTemplateOp360 = jdbcTemplateCollection.get(tenantId).get(0);

		String likeCreator = "%" + creator + "%"; // Create a pattern to match the creator's name.
		// SQL query to select distinct report names based on tenant ID and creator.
		String sql = "SELECT DISTINCT [ReportName] FROM [dbo].[reportconfigurator] WHERE [TenantId] = ? AND SharedUser LIKE ?";

		List<String> reportNames = new ArrayList<>(); // Create a list to store report names.
		Object[] args = { tenantId, likeCreator }; // Arguments for the SQL query.

		// Execute the query and get a list of maps.
		List<Map<String, Object>> rows = jdbcTemplateOp360.queryForList(sql, args);
		for (Map<String, Object> row : rows) {
			// Add each report name to the list.
			for (Map.Entry<String, Object> entry : row.entrySet()) {
				Object value = entry.getValue();
				if (value != null) {
					reportNames.add(value.toString()); // Convert to string and add to the list.
				}
			}
		}
		return reportNames; // Return the list of report names.
	}

	/**
	 * This method retrieves report configuration information based on the form name.
	 *
	 * @param tenantId The ID of the tenant.
	 * @param formName The name of the form to filter reports.
	 * @return A list of ReportConfigEntity containing the report information.
	 */
	public List<ReportConfigEntity> getReportInfoByFormName(String tenantId, String formName) {

		JdbcTemplate jdbcTemplateOp360 = jdbcTemplateCollection.get(tenantId).get(0);

		// SQL query to select report configurations for a specific form.
		String sql = "SELECT [ReportName], [FormId], [FormName], [VersionNumber], [CellId], [ReportAliasId], [Creator], [SharedUser], [FormAliasId], [TenantId] FROM [dbo].[reportconfigurator] WHERE [TenantId] = ? AND [FormName] = ?";

		Object[] args = { tenantId, formName }; // Arguments for the SQL query.

		// Execute the query and get a list of maps.
		List<Map<String, Object>> rows = jdbcTemplateOp360.queryForList(sql, args);

		// Convert each row into a ReportConfigEntity object.
		List<ReportConfigEntity> reportInfo = rows.stream().map(row -> {
			ReportConfigEntity reportConfig = new ReportConfigEntity(); // Create a new ReportConfigEntity object.
			reportConfig.setReportName((String) row.get("ReportName")); // Set the report name.
			reportConfig.setFormId((Integer) row.get("FormId")); // Set the form ID.
			reportConfig.setFormName((String) row.get("FormName")); // Set the form name.
			reportConfig.setVersionNumber((Integer) row.get("VersionNumber")); // Set the version number.
			reportConfig.setCellId((String) row.get("CellId")); // Set the cell ID.
			reportConfig.setReportAliasId((String) row.get("ReportAliasId")); // Set the report alias ID.
			reportConfig.setCreator((String) row.get("Creator")); // Set the creator's name.
			reportConfig.setSharedUser((String) row.get("SharedUser")); // Set the shared user.
			reportConfig.setFormAliasId((String) row.get("FormAliasId")); // Set the form alias ID.
			reportConfig.setTenantId((String) row.get("TenantId")); // Set the tenant ID.
			return reportConfig; // Return the completed ReportConfigEntity object.
		}).toList(); // Convert the stream back to a list.

		return reportInfo; // Return the list of report configurations.
	}

	/**
	 * This method retrieves all users for a specific tenant.
	 *
	 * @param tenantId The ID of the tenant whose users we want to get.
	 * @return A list of UserEntity containing user information.
	 */
	public List<UserEntity> getAllUsers(String tenantId) {

		JdbcTemplate jdbcTemplateOp360Usermodule = jdbcTemplateCollection.get(tenantId).get(1);

		// SQL query to select all user details from the user credential table.
		String sql = "SELECT [UserId], [UserName], [Password], [PhoneNumber], [WorkflowHomepage], [Active], [CreatedOn], [ModifiedOn], [CreatedBy], [ModifiedBy], [TenantId] FROM [dbo].[UserCredential] WHERE [TenantId] = ?";

		Object[] args = { tenantId }; // Arguments for the SQL query.

		// Execute the query and get a list of maps.
		List<Map<String, Object>> rows = jdbcTemplateOp360Usermodule.queryForList(sql, args);

		// Convert each row into a UserEntity object.
		List<UserEntity> allUsers = rows.stream().map(row -> {
			UserEntity user = new UserEntity(); // Create a new UserEntity object.
			user.setUserId((String) row.get("UserId")); // Set the user ID.
			user.setUserName((String) row.get("UserName")); // Set the user name.
			user.setPassword((String) row.get("Password")); // Set the password.
			user.setPhoneNumber((Long) row.get("PhoneNumber")); // Set the phone number.
			user.setHomePage((String) row.get("WorkflowHomepage")); // Set the homepage.
			user.setActive((Boolean) row.get("Active")); // Set the active status.
			user.setCreatedOn((Date) row.get("CreatedOn")); // Set the created date.
			user.setModifiedOn((Date) row.get("ModifiedOn")); // Set the modified date.
			user.setCreatedBy((String) row.get("CreatedBy")); // Set who created the user.
			user.setModifiedBy((String) row.get("ModifiedBy")); // Set who last modified the user.
			user.setTenantId((String) row.get("TenantId")); // Set the tenant ID.
			return user; // Return the completed UserEntity object.
		}).toList(); // Convert the stream back to a list.

		return allUsers; // Return the list of users.
	}

	/**
	 * This method retrieves report information based on the report name and tenant ID.
	 *
	 * @param tenantId The ID of the tenant.
	 * @param reportName The name of the report.
	 * @return A list of ReportConfigEntity containing user-wise report information.
	 */
	public List<ReportConfigEntity> userWiseReportsInfo(String tenantId, String reportName) {

		JdbcTemplate jdbcTemplateOp360 = jdbcTemplateCollection.get(tenantId).get(0);

		// 		SQL query to select report configurations for a specific report name.
		//		String sql = "SELECT [ReportName], [FormId], [FormName], [VersionNumber], [CellId], [ReportAliasId], [FormAliasId], [SharedUser] FROM [dbo].[reportconfigurator] WHERE [TenantId] = ? AND [ReportName] = ?";
		String sql = "SELECT DISTINCT r.[ReportName], r.[FormId], r.[FormName], r.[VersionNumber], r.[CellId], c.FieldType, r.[ReportAliasId], r.[FormAliasId], r.[SharedUser] FROM [dbo].[reportconfigurator] r JOIN [dbo].[DigitalLogbookCellDetails] c ON c.FormId = r.FormId AND c.CellId = r.CellId WHERE r.[TenantId] = ? AND [ReportName] = ?";

		List<ReportConfigEntity> reports = new ArrayList<>(); // Create a list to store report configurations.

		// Execute the query and get a list of maps.
		List<Map<String, Object>> rows = jdbcTemplateOp360.queryForList(sql, tenantId, reportName);
		for (Map<String, Object> row : rows) {
			ReportConfigEntity reportConfig = new ReportConfigEntity(); // Create a new ReportConfigEntity object.
			reportConfig.setCellId(row.get("CellId") != null ? (String) row.get("CellId") : null); // Set cell ID if available.
			reportConfig.setReportAliasId(row.get("ReportAliasId") != null ? (String) row.get("ReportAliasId") : null); // Set report alias ID if available.
			reportConfig.setFormAliasId(row.get("FormAliasId") != null ? (String) row.get("FormAliasId") : null); // Set form alias ID if available.
			reportConfig.setSharedUser(row.get("SharedUser") != null ? (String) row.get("SharedUser") : null); // Set shared user if available.
			reports.add(reportConfig); // Add the completed ReportConfigEntity to the list.
		}
		return reports; // Return the list of user-wise reports.
	}

	/**
	 * This method retrieves unique report names associated with a specific user.
	 *
	 * @param tenantId The ID of the tenant.
	 * @param userId The ID of the user whose reports we want to get.
	 * @return A list of ReportConfigEntity containing user-wise report information.
	 */
	public List<ReportConfigEntity> userWiseReports(String tenantId, String userId) {

		JdbcTemplate jdbcTemplateOp360 = jdbcTemplateCollection.get(tenantId).get(0);

		// SQL query to select distinct report configurations for a specific user.
		String sql = "SELECT DISTINCT ReportName, FormName, VersionNumber, SharedUser, FormId, creator FROM dbo.reportconfigurator WHERE [TenantId] = ? AND creator=?";

		List<ReportConfigEntity> reports = new ArrayList<>(); // Create a list to store report configurations.

		// Execute the query and get a list of maps.
		List<Map<String, Object>> rows = jdbcTemplateOp360.queryForList(sql, tenantId, userId);
		for (Map<String, Object> row : rows) {
			ReportConfigEntity reportConfig = new ReportConfigEntity(); // Create a new ReportConfigEntity object.
			reportConfig.setReportName((String) row.get("ReportName")); // Set the report name.
			reportConfig.setFormName((String) row.get("FormName")); // Set the form name.
			reportConfig.setVersionNumber((int) row.get("VersionNumber")); // Set the version number.
			reportConfig.setFormId((int) row.get("FormId")); // Set the form ID.
			String sharedUser = (String) row.get("SharedUser"); // Get the shared user.
			reportConfig.setSharedUser(sharedUser); // Set the shared user.
			reportConfig.setCreator((String) row.get("creator"));
			reports.add(reportConfig); // Add the completed ReportConfigEntity to the list.
		}
		return reports; // Return the list of reports.
	}

	/**
	 * This method inserts a new report configuration into the database.
	 *
	 * @param tenantId The ID of the tenant.
	 * @param reportname The name of the report to insert.
	 * @param reportConfig The configuration details for the report.
	 * @param creatorId The ID of the user creating the report.
	 * @param sharedUserLists A string containing shared user information.
	 * @param reportData A list of ReportData containing details about the report.
	 * @return An integer indicating the success of the insertion (1 for success, 0 for failure).
	 */
	public int insertReportConfig(String tenantId, String reportname, ReportConfigDto reportConfig,
			String creatorId, String sharedUserLists, List<ReportData> reportData) {

		JdbcTemplate jdbcTemplateOp360 = jdbcTemplateCollection.get(tenantId).get(0);

		// SQL query to check if the report name already exists.
		String checkIfExistsQuery = "SELECT COUNT(ReportName) AS Total_Report FROM dbo.reportconfigurator WHERE [TenantId] = ? AND ReportName=?";

		// SQL query to insert the new report configuration into the database.
		String insertReportConfigQuery = "INSERT INTO dbo.reportconfigurator(ReportName, FormName, CellId, ReportAliasId, FormAliasId, FormId, VersionNumber, creator, SharedUser, TenantId) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try {
			// Check if the report name already exists in the database.
			int existingReportCount = jdbcTemplateOp360.queryForObject(checkIfExistsQuery, Integer.class, tenantId, reportname);
			if (existingReportCount > 0) { // If the report already exists
				System.out.println("Report Name Already Exists");
				return 0; // Return 0 indicating failure.
			}

			// Insert the new report configuration using batch update for efficiency.
			jdbcTemplateOp360.batchUpdate(insertReportConfigQuery, new BatchPreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement pstmt, int i) throws SQLException {
					ReportData reportDataItem = reportData.get(i); // Get the report data item for the current index.
					pstmt.setString(1, reportname); // Set the report name.
					pstmt.setString(2, reportConfig.getFormName()); // Set the form name.
					pstmt.setString(3, reportDataItem.getCellId()); // Set the cell ID.
					pstmt.setString(4, reportDataItem.getReportAliasId()); // Set the report alias ID.
					pstmt.setString(5, reportDataItem.getFormAliasId()); // Set the form alias ID.
					pstmt.setInt(6, reportConfig.getFormId()); // Set the form ID.
					pstmt.setInt(7, reportConfig.getVersionNumber()); // Set the version number.
					pstmt.setString(8, creatorId); // Set the creator ID.
					pstmt.setString(9, sharedUserLists); // Set the shared users.
					pstmt.setString(10, tenantId); // Set the tenant ID.
				}

				@Override
				public int getBatchSize() {
					return reportData.size(); // Batch size should be the size of reportData
				}
			});

		} catch (DataAccessException e) {
			e.printStackTrace(); // Print the stack trace for debugging if there's an exception.
			return 0; // Return 0 if there's an exception indicating failure.
		}
		return 1; // Return 1 indicating successful insertion.
	}

	/**
	 * This method updates the shared users for a specific report configuration.
	 *
	 * @param tenantId The ID of the tenant.
	 * @param reportName The name of the report to update.
	 * @param sharedUser The new shared user information to set.
	 * @return An integer indicating the number of rows affected (0 if none).
	 */
	public int updateSharedUsersForReportConfig(String tenantId, String reportName, String sharedUser) {

		JdbcTemplate jdbcTemplateOp360 = jdbcTemplateCollection.get(tenantId).get(0);

		// SQL query to update shared user information for a specific report.
		String sql = "UPDATE [dbo].[reportconfigurator] SET [SharedUser] = ? WHERE ReportName = ? AND [TenantId] = ?";

		try {

			// Execute the update query and get the number of rows affected.
			int rowsAffected = jdbcTemplateOp360.update(sql, sharedUser, reportName, tenantId);
			return rowsAffected; // Return the number of rows affected by the update.

		} catch (DataAccessException e) {

			// Handle any database access exceptions
			System.err.println("Error executing the update: " + e.getMessage()); // Print the error message.
			e.printStackTrace(); // Print the stack trace for debugging.
			return 0;  // Return 0 indicating failure.

		} catch (Exception e) {

			// Handle any other exceptions
			System.err.println("An unexpected error occurred: " + e.getMessage()); // Print the error message.
			e.printStackTrace(); // Print the stack trace for debugging.
			return 0; // Return 0 indicating failure.
		}
	}

	/**
	 * This method fetches logbook report data based on specified criteria.
	 *
	 * @param tenantId The ID of the tenant.
	 * @param fromdate The starting date for fetching report data.
	 * @param todate The ending date for fetching report data.
	 * @param reportName The name of the report to fetch.
	 * @return A list of maps containing the fetched report data.
	 */
	public List<Map<String, Object>> fetchLogbookReportData(String tenantId, String fromdate, String todate, String reportName) {

		JdbcTemplate jdbcTemplateOp360 = jdbcTemplateCollection.get(tenantId).get(0);

		List<Map<String, Object>> resultData = new LinkedList<>(); // Create a list to hold the results.

		try {

			// SQL query to get the unique report configurations for the specified tenant and report name.
			//			String query1 = "SELECT distinct ReportName, FormName, VersionNumber, CellId, ReportAliasId FROM [dbo].[reportconfigurator] where [TenantId] = ? AND ReportName = ? order by CellId";

			// SQL query to dynamically create a report based on the specified criteria.
			String query3 = "DECLARE @tt nVARCHAR(max); SELECT @tt = STUFF( (SELECT ',' + CellId FROM [dbo].[reportconfigurator] WHERE [TenantId] = ? AND ReportName = ? FOR XML PATH('')), 1, 1, '' ); DECLARE @keyt nVARCHAR(max) = @tt; DECLARE @ReportNamet  nvarchar(100) = ?; DECLARE @fromtimestampt   datetime = ?; DECLARE @totimestampt   datetime = ?; DECLARE @keyst nvarchar(max) = ''; SELECT @keyst += QUOTENAME(value) + ',' FROM string_split(@keyt, ','); SET @keyst = LEFT(@keyst, LEN(@keyst) - 1); SET NOCOUNT ON; DECLARE @dynamict AS NVARCHAR(MAX) = CONCAT( 'SELECT * FROM ( SELECT [TransactionId], [Value], Timestamp, [key] FROM ( SELECT DISTINCT t.TransactionId, t.formname, version, JSON_VALUE([value], ''$.value'') AS [value], timestamp, [key] COLLATE SQL_Latin1_General_CP1_CI_AS AS [key] FROM dbo.LogbookTransactionData t CROSS APPLY OPENJSON(t.logbookdata), dbo.DigitalLogbookCellDetails a, dbo.DigitalLogbookFormInfo b WHERE t.timestamp BETWEEN ''', @fromtimestampt, ''' AND ''', @totimestampt, ''' AND [key] COLLATE SQL_Latin1_General_CP1_CI_AS = a.CellId COLLATE SQL_Latin1_General_CP1_CI_AS AND a.FormId = b.FormId AND t.formname = b.FormName AND t.version = b.VersionNumber ) tab LEFT JOIN [dbo].[reportconfigurator] r ON tab.formname = r.FormName AND tab.[key] = r.CellId WHERE ReportName = ''', @ReportNamet ,''' ) AS pivot_tab PIVOT ( MAX([Value]) FOR [key] IN (', @keyst, ') ) AS pivot_result ORDER BY Timestamp;' ); EXEC(@dynamict);";

			// Execute the first query to get report configurations.
			//			List<Map<String, Object>> rows1 = jdbcTemplateOp360.queryForList(query1, tenantId, reportName);
			// Execute the second query to fetch logbook data based on the criteria.
			List<Map<String, Object>> rows2 = jdbcTemplateOp360.queryForList(query3, tenantId, reportName, reportName, fromdate, todate);

			Map<String, String> aliasMap = new LinkedHashMap<>(); // Create a map to hold cell IDs and their corresponding report alias IDs.
			// Populate the alias map with data from the first query.
			//			for (Map<String, Object> row : rows1) {
			//				String cellId = (String) row.get("CellId");
			//				String reportAliasId = (String) row.get("ReportAliasId");
			//				aliasMap.put(cellId, reportAliasId); // Add cell ID and report alias ID to the map.
			//			}

			// Process the results from the second query.
			for (Map<String, Object> row : rows2) {
				Map<String, String> dbmap = new LinkedHashMap<>(); // Create a new map for the current row of data.
				for (Map.Entry<String, Object> entry : row.entrySet()) {
					String entryKey = entry.getKey(); // Get the key for the current entry.
					String entryValue = ""; // Initialize the entry value.
					if (entry.getValue() != null) {
						entryValue = entry.getValue().toString(); // Convert the value to a string if it's not null.
					}
					// Map the entry key to its alias if it exists, otherwise use the original key.
					String mappedKey = aliasMap.getOrDefault(entryKey, entryKey);
					dbmap.put(mappedKey, entryValue); // Add the mapped key and value to the dbmap.
				}
				Map<String, Object> dbm = new LinkedHashMap<>(dbmap); // Create a new LinkedHashMap for the result.
				resultData.add(dbm); // Add the completed dbm to the result data list.
			}
		} catch (Exception e) {
			System.err.println(e.getMessage()); // Print any error messages that occur.
			e.printStackTrace(); // Print the stack trace for debugging.
		}
		return resultData; // Return the list of fetched report data.
	}

	/**
	 * This method fetches logbook view report data based on the specified criteria.
	 *
	 * @param tenantId The ID of the tenant (like a customer).
	 * @param fromdate The start date for the report.
	 * @param todate The end date for the report.
	 * @param formName The name of the form to filter by.
	 * @return A list of maps containing the report data.
	 */
	public List<Map<String, Object>> fetchLogbookViewReportData(String tenantId, String fromdate, String todate, String formName) {

		JdbcTemplate jdbcTemplateOp360 = jdbcTemplateCollection.get(tenantId).get(0);

		List<Map<String, Object>> resultData = new LinkedList<>(); // Create a list to hold the results.

		try {
			// SQL query to get the latest transaction data for the specified form and date range.
			String query = "Select * from( Select formname,version,JobId,ActivityId,MAX(timestamp) LastTimeStamp from dbo.LogbookTransactionData where formname=? and timestamp between ? and ? group by formname,version,JobId,ActivityId)t order by LastTimeStamp";

			// Execute the query and get the results as a list of maps.
			List<Map<String, Object>> rows = jdbcTemplateOp360.queryForList(query, formName, fromdate, todate);

			// Loop through each row of results.
			for (Map<String, Object> row : rows) {
				Map<String, Object> rowData = new HashMap<>(); // Create a new map for the current row.

				// Get values from the current row and put them into the rowData map.
				String formname = (String) row.get("formname");
				int version = (int) row.get("version");
				String jobId = (String) row.get("JobId");
				String activityId = (String) row.get("ActivityId");
				String lastTimeStamp = (String) row.get("LastTimeStamp");

				// Add the values to the rowData map.
				rowData.put("formname", formname);
				rowData.put("version", version);
				rowData.put("jobId", jobId);
				rowData.put("activityId", activityId);
				rowData.put("lastTimeStamp", lastTimeStamp);

				// Add the rowData map to the resultData list.
				resultData.add(rowData);
			}
		} catch (Exception e) {
			// Print any error messages that occur.
			System.err.println(e.getMessage());
			e.printStackTrace();
		}

		return resultData; // Return the list of results.
	}

	/**
	 * This method retrieves transactions based on the form name, version, and date range.
	 *
	 * @param tenantId The ID of the tenant (like a customer).
	 * @param formName The name of the form to filter by.
	 * @param version The version of the form.
	 * @param fromDate The start date for fetching transactions.
	 * @param toDate The end date for fetching transactions.
	 * @return A list of Transaction objects that match the criteria.
	 */
	public List<Transaction> getTransactionsByFormNameAndVersionBetweenDates(String tenantId, String formName, int version, String fromDate, String toDate) {

		JdbcTemplate jdbcTemplateOp360 = jdbcTemplateCollection.get(tenantId).get(0);

		List<Transaction> allTransactions = new ArrayList<>(); // Create a list to hold the transactions.

		//		// SQL query to get the latest transaction data for the specified criteria.
		//		String sql = "SELECT [slno] ,[TransactionId] ,[JobId] ,[ActivityId] ,[formname] ,[version] ,[logbookdata] ,[transaction_remarks] ,[timestamp] ,[userid] ,[role] FROM ( SELECT [slno] ,[TransactionId] ,[JobId] ,[ActivityId] ,[formname] ,[version] ,[logbookdata] ,[transaction_remarks] ,[timestamp] ,[userid] ,[role], ROW_NUMBER() OVER (PARTITION BY JobId, ActivityId ORDER BY timestamp DESC) AS r FROM [dbo].[LogbookTransactionData] WHERE [TenantId] = ? AND [formname] = ? AND [version] = ? AND convert(date,[timestamp]) BETWEEN ? AND ? ) AS t WHERE r = 1 ORDER BY timestamp;";

		String sql = "DECLARE @tenant VARCHAR(MAX) = ?; DECLARE @formname VARCHAR(MAX) = ?; DECLARE @version int = ?; DECLARE @fromDate Date = ?; DECLARE @toDate Date = ?; select [slno] ,[TransactionId] ,t1.[JobId] ,t1.[ActivityId] ,t1.[formname] ,t1.[version] ,t1.[logbookdata] ,[transaction_remarks] ,[timestamp] ,[userid] ,[role],coalesce(ScheduledStart,c.activitystarttime) [ActivityStartTime] from( SELECT [slno] ,[TransactionId] ,[JobId] ,[ActivityId] ,[formname] ,[version] ,[logbookdata] ,[transaction_remarks] ,[timestamp] ,[userid] ,[role], ScheduledStart FROM ( SELECT [slno] ,[TransactionId] ,a.[JobId] ,a.[ActivityId] ,a.[formname] ,a.[version] ,[logbookdata] ,[transaction_remarks] ,[timestamp] ,[userid] ,[role], ROW_NUMBER() OVER (PARTITION BY a.JobId, a.ActivityId ORDER BY timestamp DESC) AS r,b.ScheduledStart FROM [dbo].[LogbookTransactionData] a left join dbo.JobActivityDetails b on a.JobId=b.JobId and a.ActivityId=b.ActivityId WHERE a.[TenantId] = @tenant AND a.[formname] = @formname AND a.[version] = @version AND convert(date,[timestamp]) BETWEEN @fromDate AND @toDate )as t where r=1 )as t1 left join dbo.AutoJobInfo c on t1.JobId=c.JobId and t1.ActivityId=c.activityid order by coalesce(ScheduledStart,c.activitystarttime)";

		// Create an array of arguments for the SQL query.
		Object[] args = {tenantId, formName, version, fromDate, toDate};

		// Execute the query and get the results as a list of maps.
		List<Map<String, Object>> rowMapList = jdbcTemplateOp360.queryForList(sql, args);

		// Loop through each row of results.
		rowMapList.forEach(row -> {
			Transaction transaction = new Transaction(); // Create a new Transaction object.
			// Set the values from the current row into the transaction object.
			transaction.setSlno((Long) row.get("slno"));
			transaction.setTransactionId(row.get("TransactionId").toString());
			transaction.setJobId(row.get("JobId").toString());
			transaction.setActivityId(row.get("ActivityId").toString());
			transaction.setFormName(row.get("formname").toString());
			transaction.setVersion((Integer) row.get("version"));
			transaction.setLogbookData(row.get("logbookdata").toString());

			// Check if transaction remarks exist, and if so, set them.
			if (row.get("transaction_remarks") != null)
				transaction.setTransactionRemarks(row.get("transaction_remarks").toString());

			transaction.setTimestamp((Timestamp) row.get("timestamp"));
			transaction.setUserId(row.get("userid").toString());
			transaction.setRole(row.get("role").toString());

			// Add the transaction to the list.
			allTransactions.add(transaction);
		});

		return allTransactions; // Return the list of transactions.
	}

	/**
	 * This method retrieves form information based on the form name and version.
	 *
	 * @param tenantId The ID of the tenant (like a customer).
	 * @param formName The name of the form to look up.
	 * @param version The version of the form to look up.
	 * @return A FormInfo object containing the form details, or null if not found.
	 */
	public FormInfo forminfoByformNameAndVersion(String tenantId, String formName, int version) {

		JdbcTemplate jdbcTemplateOp360 = jdbcTemplateCollection.get(tenantId).get(0);

		// SQL query to get the form information for the specified tenant, form name, and version.
		String sql = "SELECT [FormId], [FormName], [UserID], [SaveSQL], [TableSQL], [DeleteSQL], [CreationDate], [CreatedUser], [Department], [UserGroup], [DocumentID], [FormatID], [VersionNumber], [isActiveForm], [TenantId]"
//				+ ", [DashboardType]"
				+ " FROM [dbo].[DigitalLogbookFormInfo] WHERE [TenantId] = ? AND [FormName] = ? AND [VersionNumber] = ?";

		// Create an array of arguments for the SQL query.
		Object[] args = { tenantId, formName, version };

		// Use queryForList to fetch multiple rows based on the query.
		List<Map<String, Object>> rows = jdbcTemplateOp360.queryForList(sql, args);

		// Check if any results were found.
		if (!rows.isEmpty()) {
			Map<String, Object> row = rows.get(0); // Get the first row (assuming only one result).

			FormInfo formInfo = new FormInfo(); // Create a new FormInfo object.
			// Set the values from the row into the formInfo object.
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

			return formInfo; // Return the FormInfo object with details.
		}

		return null; // Return null if no results were found.
	}

	/**
	 * This class is used to draw special lines in the cells of a PDF table.
	 */
	class CellLinesRenderer extends CellRenderer {

		// Constructor to create a CellLinesRenderer for a specific cell.
		public CellLinesRenderer(Cell modelElement) {
			super(modelElement); // Call the parent class constructor to set up the cell.
		}

		@Override
		public void drawBorder(DrawContext drawContext) {
			super.drawBorder(drawContext); // Draw the default cell border.

			// Get the area occupied by the cell.
			Rectangle rect = getOccupiedAreaBBox();

			// Get the canvas to draw on.
			PdfCanvas canvas = drawContext.getCanvas();

			// Set the color of the lines to black.
			canvas.setStrokeColor(Color.BLACK);
			// Draw a horizontal line in the middle of the cell.
			canvas.moveTo(rect.getLeft() + 10, (rect.getTop() + rect.getBottom()) / 2);
			canvas.lineTo(rect.getRight() - 10, (rect.getTop() + rect.getBottom()) / 2);
			canvas.stroke(); // Complete the line drawing.
		}

		@Override
		public CellRenderer getNextRenderer() {
			// Create a new instance of CellLinesRenderer for the next cell.
			return new CellLinesRenderer((Cell) modelElement);
		}
	}

	/**
	 * This class handles events related to the header of the PDF document.
	 */
	public class HeaderEventHandler implements IEventHandler {
		Document doc; // This variable holds the PDF document.
		FormInfo selectedForm; // This variable holds information about the selected form.
		String userid;

		// Constructor to create a HeaderEventHandler with a document and selected form.
		public HeaderEventHandler(Document document, FormInfo selectedForm, String userid) {
			this.doc = document; // Set the document.
			this.selectedForm = selectedForm; // Set the selected form.
			this.userid = userid;
		}

		@Override
		public void handleEvent(Event event) {
			// When an event occurs, this method handles it.
			PdfDocumentEvent docEvent = (PdfDocumentEvent) event; // Get the PDF document event.
			PdfDocument pdfDoc = docEvent.getDocument(); // Get the PDF document.
			PdfPage page = docEvent.getPage(); // Get the current page.
			Rectangle pageSize = page.getPageSize(); // Get the size of the page.
			PdfCanvas pdfCanvas = new PdfCanvas(page.newContentStreamBefore(), page.getResources(), pdfDoc); // Create a canvas to draw on the page.
			Canvas canvas = new Canvas(pdfCanvas, pdfDoc, pageSize); // Create a drawing area on the canvas.
			Table table = new Table(6); // Create a new table with 6 columns.

			// Create a cell for the form name and add it to the table.
			Cell tCell = new Cell(1, 1).add(new Paragraph(selectedForm.getFormName()));
			tCell.setTextAlignment(TextAlignment.CENTER).setFontSize(9); // Center the text and set the font size.
			tCell.setVerticalAlignment(VerticalAlignment.MIDDLE); // Vertically center the text in the cell.
			tCell.setBorder(Border.NO_BORDER); // Remove the border from the cell.
			table.addCell(tCell); // Add the cell to the table.

			// Create another cell for the form name (spanning 3 columns) and add it to the table.
			tCell = new Cell(1, 3).add(new Paragraph(selectedForm.getFormName()));
			tCell.setTextAlignment(TextAlignment.CENTER).setFontSize(9);
			tCell.setVerticalAlignment(VerticalAlignment.MIDDLE);
			tCell.setBorder(Border.NO_BORDER);
			table.addCell(tCell);

			// Format the current date and add more information about the form to a new cell.
			DateFormat dfDtTm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // Define the date format.
			tCell = new Cell(1, 2).add(new Paragraph("Document Id: " + selectedForm.getDocumentId()));
			tCell.add(new Paragraph("Format Id: " + selectedForm.getFormatId()))
			.add(new Paragraph("Generated By: " + userid))
			.add(new Paragraph(dfDtTm.format(new Date()))); // Add the formatted date.
			tCell.setTextAlignment(TextAlignment.RIGHT).setFontSize(8); // Right-align the text and set the font size.
			tCell.setVerticalAlignment(VerticalAlignment.TOP); // Align the text at the top of the cell.
			tCell.setBorder(Border.NO_BORDER); // Remove the border from the cell.
			table.addCell(tCell); // Add the cell to the table.

			// Calculate the width of the table to fit within the page margins.
			float contentWidth = pageSize.getWidth() - doc.getLeftMargin() - doc.getRightMargin();
			table.setWidth(contentWidth); // Set the table width.
			table.setHorizontalAlignment(HorizontalAlignment.CENTER); // Center the table on the page.
			table.setMarginTop(10); // Set a top margin for the table.
			table.setBorder(Border.NO_BORDER); // Remove the border from the table.

			// Create the table layout and add it to the canvas.
			LayoutResult result = table.createRendererSubTree().setParent(canvas.getRenderer())
					.layout(new LayoutContext(new LayoutArea(1, new Rectangle(0, 0, contentWidth, 1000))));
			float headerHeight = result.getOccupiedArea().getBBox().getHeight(); // Get the height of the header.

			// Adjust the document margins based on the header height.
			doc.setTopMargin(headerHeight + 10);
			doc.setBottomMargin(80); // Set a bottom margin for the document.

			canvas.add(table); // Add the table to the canvas.
		}
	}

	/**
	 * This class handles events related to the footer of the PDF document.
	 */
	public class FooterEventHandler implements IEventHandler {
		protected PdfFont font; // Variable to hold the font for the PDF.
		TransactionDto activityDetails; // This variable holds details about the transaction.
		Document doc; // This variable holds the PDF document.
		Map<String, String> footerDetails; // This variable holds details for the footer of the PDF.
		UserModel userModel; // This variable holds information about the user.

		// Constructor to initialize the FooterEventHandler with a document, activity details, and tenant ID.
		public FooterEventHandler(Document document, TransactionDto activityDetails, String tenantId) {
			this.doc = document; // Set the document.
			this.activityDetails = activityDetails; // Set the transaction details.
			// Get the footer details based on the job ID, activity ID, and tenant ID.
			this.footerDetails = getPdfFooterDetails(activityDetails.getJobId(), activityDetails.getActivityId(), tenantId);
			// Find the user model based on the tenant ID.
			this.userModel = findByTenantId(tenantId);
			try {
				// Create a font for the PDF.
				this.font = PdfFontFactory.createFont();
			} catch (Exception ex) {
				ex.printStackTrace(); // Print the stack trace if an error occurs while creating the font.
			}
		}

		@Override
		public void handleEvent(Event event) {
			// This method handles the footer event when generating the PDF.

			// Print details about the activity for debugging purposes.
			//			System.err.println("activityDetails.getActivityId() : " + activityDetails.getActivityId());
			//			System.err.println("activityDetails.getFormName() : " + activityDetails.getFormName());
			//			System.err.println("activityDetails.getFormversion() : " + activityDetails.getFormversion());
			//			System.err.println("activityDetails.getJobId() : " + activityDetails.getJobId());
			//			System.err.println("activityDetails.getTransactionId() : " + activityDetails.getTransactionId());
			//			System.err.println("activityDetails.getUserId() : " + activityDetails.getUserId());
			//			System.err.println("activityDetails.getTransactionTimestamp() : " + activityDetails.getTransactionTimestamp());

			PdfDocumentEvent docEvent = (PdfDocumentEvent) event; // Get the PDF document event.
			PdfDocument pdfDoc = docEvent.getDocument(); // Get the PDF document.
			PdfPage page = docEvent.getPage(); // Get the current page of the document.
			// Define a rectangular area for the footer with specific width and height.
			Rectangle pageSize = new Rectangle(36, 0, page.getPageSize().getWidth() - 72, 55);
			PdfCanvas pdfCanvas = new PdfCanvas(page.newContentStreamBefore(), page.getResources(), pdfDoc); // Create a canvas to draw on the page.
			Canvas canvas = new Canvas(pdfCanvas, pdfDoc, pageSize); // Create a drawing area on the canvas.
			canvas.setFontSize(10); // Set the font size for the footer text.

			// Get footer details, using "NA" as a default value if details are not available.
			String actn = footerDetails.get("activityName") != null ? footerDetails.get("activityName") : "NA";
			String assetn = footerDetails.get("assetName") != null ? footerDetails.get("assetName") : "NA";
			String taskn = footerDetails.get("taskName") != null ? footerDetails.get("taskName") : "NA";

			//			System.err.println("userModel :::  " + userModel);

			// Safely access userModel details.
			String plantName = userModel.getPlantName() != null ? userModel.getPlantName() : "Unknown Plant";
			String address = userModel.getAddress() != null ? userModel.getAddress() : "Unknown Address";
			String division = userModel.getDivision() != null ? userModel.getDivision() : "Unknown Division";
			String phoneNumber = userModel.getPhoneNumber() != null ? userModel.getPhoneNumber() : "0000";
			String email = userModel.getEmail() != null ? userModel.getEmail() : "Unknown Email";

			// Safely access activityDetails fields.
			String jobId = activityDetails.getJobId() != null ? activityDetails.getJobId() : "NA";

			Table table = new Table(2); // Create a new table with 2 columns.
			Cell tCell; // Variable to hold individual cells for the table.

			// Create and add cells to the table with user and activity details.
			tCell = new Cell(1, 1).add(new Paragraph(userModel.getPlantName() != null ? userModel.getPlantName() : "").setFixedLeading(5));
			tCell.setTextAlignment(TextAlignment.LEFT).setPaddingTop(5); // Align text to the left and set padding.
			tCell.setVerticalAlignment(VerticalAlignment.MIDDLE); // Vertically center the text in the cell.
			tCell.setBorder(Border.NO_BORDER); // Remove the border from the cell.
			table.addCell(tCell); // Add the cell to the table.

			// Add activity name to the table.
			tCell = new Cell(1, 1).add(new Paragraph(footerDetails.get("activityName") != null ? "Activity Name: " + footerDetails.get("activityName") : "").setFixedLeading(5));
			tCell.setTextAlignment(TextAlignment.RIGHT).setPaddingTop(5); // Align text to the right.
			tCell.setVerticalAlignment(VerticalAlignment.MIDDLE);
			tCell.setBorder(Border.NO_BORDER);
			table.addCell(tCell); // Add the cell to the table.

			// Add more details about the user and activity.
			tCell = new Cell(1, 1).add(new Paragraph(userModel.getAddress() != null ? userModel.getAddress() : "").setFixedLeading(5));
			tCell.setTextAlignment(TextAlignment.LEFT);
			tCell.setVerticalAlignment(VerticalAlignment.MIDDLE);
			tCell.setBorder(Border.NO_BORDER);
			table.addCell(tCell); // Add the cell to the table.

			tCell = new Cell(1, 1).add(new Paragraph(footerDetails.get("assetName") != null ? "Asset ID: " + footerDetails.get("assetName") : "").setFixedLeading(5));
			tCell.setTextAlignment(TextAlignment.RIGHT);
			tCell.setVerticalAlignment(VerticalAlignment.MIDDLE);
			tCell.setBorder(Border.NO_BORDER);
			table.addCell(tCell); // Add the cell to the table.

			tCell = new Cell(1, 1).add(new Paragraph(userModel.getDivision() != null ? "Division: " + userModel.getDivision() : "").setFixedLeading(5));
			tCell.setTextAlignment(TextAlignment.LEFT);
			tCell.setVerticalAlignment(VerticalAlignment.MIDDLE);
			tCell.setBorder(Border.NO_BORDER);
			table.addCell(tCell); // Add the cell to the table.

			tCell = new Cell(1, 1).add(new Paragraph(activityDetails.getJobId() != null ? "Job ID: " + activityDetails.getJobId() : "").setFixedLeading(5));
			tCell.setTextAlignment(TextAlignment.RIGHT);
			tCell.setVerticalAlignment(VerticalAlignment.MIDDLE);
			tCell.setBorder(Border.NO_BORDER);
			table.addCell(tCell); // Add the cell to the table.

			tCell = new Cell(1, 1).add(new Paragraph(userModel.getPhoneNumber() != null ? "Phone: " + userModel.getPhoneNumber() : "").setFixedLeading(5));
			tCell.setTextAlignment(TextAlignment.LEFT);
			tCell.setVerticalAlignment(VerticalAlignment.MIDDLE);
			tCell.setBorder(Border.NO_BORDER);
			table.addCell(tCell); // Add the cell to the table.

			tCell = new Cell(1, 1).add(new Paragraph(footerDetails.get("taskName") != null ? "Task name: " + footerDetails.get("taskName") : "").setFixedLeading(5));
			tCell.setTextAlignment(TextAlignment.RIGHT);
			tCell.setVerticalAlignment(VerticalAlignment.MIDDLE);
			tCell.setBorder(Border.NO_BORDER);
			table.addCell(tCell); // Add the cell to the table.

			tCell = new Cell(1, 1).add(new Paragraph(userModel.getEmail() != null ? "Email: " + userModel.getEmail() : "").setFixedLeading(5));
			tCell.setTextAlignment(TextAlignment.LEFT);
			tCell.setVerticalAlignment(VerticalAlignment.MIDDLE);
			tCell.setBorder(Border.NO_BORDER);
			table.addCell(tCell); // Add the cell to the table.

			table.setBorder(Border.NO_BORDER); // Remove the border from the entire table.

			canvas.add(table); // Add the table to the canvas, drawing it in the footer of the PDF.
		}
	}

	// This method exports logbook data to a PDF document.
	public ByteArrayInputStream exportLogBookDataToPdf(String tenantId, String userid, String formName, int version, String fromDate, String toDate, int index) {
		this.cellRowNum = 1; // Start counting rows from 1.

		// Get all cell information based on the provided form name and version.
		List<CellDto> cellList = logbookRepositoryImpl.getAllCellInfoByFormNameAndVersion(formName, version, tenantId);
		ArrayList<CellDetailsDto> cellDetailsList = new ArrayList<CellDetailsDto>(); // Create a list to hold cell details.

		// Retrieve transactions that match the specified form name, version, and date range.
		List<Transaction> transactions = getTransactionsByFormNameAndVersionBetweenDates(tenantId, formName, version, fromDate, toDate);
		ArrayList<TransactionDto> transactionDtoList = new ArrayList<>(); // Create a list to hold transaction details.

		transactions.forEach(transaction -> {
			TransactionDto transactionDto = new TransactionDto(transaction);
			ObjectMapper objectMapper = new ObjectMapper(); // Create an ObjectMapper to convert JSON data.
			try {
				Map<String, Object> jsonInfo = objectMapper.readValue(transaction.getLogbookData(), Map.class);

				for (Map.Entry<String, Object> entry : jsonInfo.entrySet()) {
					if (entry.getValue() instanceof Map) {
						Map<String, Object> innerMap = (Map<String, Object>) entry.getValue();

						if (innerMap.containsKey("value") && "Blob_File".equals(innerMap.get("value"))) {

							TransactionAttachmentDto t = getTransactionAttachmentFromCellIdAndTransactionId(
									tenantId, transaction.getTransactionId(), entry.getKey()
									);

							// Replace "Blob_File" with the actual file content only if `t` is not null
							if (t != null && t.getFileContent() != null) {
								innerMap.put("value", t.getFileContent());
							}
						}
					}
				}

				transactionDto.setLogbookData(jsonInfo);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}

			transactionDtoList.add(transactionDto);
		});


		// Create cell details based on the rows in the cell list.
		while (true) {
			// Filter the cell list to get cells that match the current row number.
			List<CellDto> filteredList = cellList.stream().filter(cell -> {
				return cell.getRowNum() == this.cellRowNum; // Check if the cell's row number matches.
			}).collect(Collectors.toList());

			if (filteredList.isEmpty()) break; // If no cells match, exit the loop.
			else {
				CellDetailsDto cellDetailsDto = new CellDetailsDto(); // Create a new cell details DTO.
				cellDetailsDto.setCellData(filteredList); // Set the filtered cell data.
				cellDetailsList.add(cellDetailsDto); // Add the cell details DTO to the list.
			}
			this.cellRowNum++; // Move to the next row.
		}

		// Get form information based on the provided tenant ID, form name, and version.
		FormInfo forminfo = forminfoByformNameAndVersion(tenantId, formName, version);

		ByteArrayOutputStream out = new ByteArrayOutputStream(); // Create a stream to hold the PDF data.

		try {
			// Create a writer for the PDF document.
			PdfWriter writer = new PdfWriter(out);
			PdfDocument pdfDoc = new PdfDocument(writer); // Create a new PDF document.
			Document document = new Document(pdfDoc); // Create a document from the PDF.
			//			document.getPdfDocument().setDefaultPageSize(PageSize.A4); // Set the page size to A4.

			// Create header and footer handlers for the PDF.
			HeaderEventHandler headerHandler = new HeaderEventHandler(document, forminfo, userid);
			FooterEventHandler footerHandler = new FooterEventHandler(document, transactionDtoList.get(index), tenantId);

			// Add the header and footer event handlers to the PDF document.
			pdfDoc.addEventHandler(PdfDocumentEvent.START_PAGE, headerHandler);
			pdfDoc.addEventHandler(PdfDocumentEvent.END_PAGE, footerHandler);

			// Add content to the document using the cell and transaction details.
			addContent(document, cellDetailsList, transactionDtoList, index);

			document.close(); // Close the document.
			pdfDoc.close(); // Close the PDF document.
			writer.close(); // Close the writer.

			System.out.println("Pdf created successfully."); // Print success message.
		} catch (Exception e) {
			e.printStackTrace(); // Print any errors that occur during PDF creation.
		}
		return new ByteArrayInputStream(out.toByteArray()); // Return the PDF as a ByteArrayInputStream.
	}

	// This method adds content to the PDF document based on the provided cell details and transaction data.
	public void addContentOriginal(Document document, List<CellDetailsDto> cellDetailsList, List<TransactionDto> transactionDtoList, int index) {

		// Get the specific transaction data based on the provided index.
		TransactionDto transactionDto = transactionDtoList.get(index);

		// Print a message indicating that the document is being downloaded.
		System.out.println("Downloading...Document");

		int counter = 0; // A counter to keep track of the number of rows processed.
		int tableWidth = 0; // A variable to calculate the total width of the table.

		// Loop through each CellDetailsDto object in the cellDetailsList.
		for (CellDetailsDto f : cellDetailsList) {
			// Loop through each CellDto object within the current CellDetailsDto.
			for (CellDto c : f.getCellData()) {
				// If it's the first cell in the row, add the column span to the table width.
				if (counter == 0) {
					tableWidth += c.getColSpan(); // Add the column span of the current cell to the total width.
				}
				// Print the column span of the current cell for debugging.
			}
			counter++; // Increment the counter after processing each row.
		}

		// Add an empty paragraph to the document for spacing.
		document.add(new Paragraph());

		try {
			// Create a new table with the calculated width.
			Table table = new Table(tableWidth);
			Cell tCell; // Declare a variable to hold individual cells.
			counter = 0; // Reset the counter for rows.

			int rowNumber = 1;

			// Loop through each CellDetailsDto object in the cellDetailsList again.
			for (CellDetailsDto f : cellDetailsList) {

				int rowColSpan = 0;

				// Loop through each CellDto object within the current CellDetailsDto.
				for (CellDto c : f.getCellData()) {

					if (c.getColSpan() == tableWidth && c.getRowSpan() > 1) {
						System.out.println("Auto-fixing rowSpan for: " + c.getCellId() + " from " + c.getRowSpan() + " to 1");
						c.setRowSpan(1);
					}

					rowColSpan += c.getColSpan();

					// Create a new cell with the specified row span and column span.
					tCell = new Cell(c.getRowSpan(), c.getColSpan());
					String value = ""; // A variable to hold the cell's value.
					Paragraph p = new Paragraph(); // Create a new paragraph to hold the cell's content.

					// Check if the cell is editable or dependent, and not an image.
					//					if ((c.isEditable() || c.isDependent()) && !(c.getFieldType().equalsIgnoreCase("image"))) {
					//					if (!(c.getFieldType().equalsIgnoreCase("image"))) {
					//
					//						// Check if the logbook data contains a value for the current cell.
					//						if (transactionDto.getLogbookData().get(c.getCellId()) != null && transactionDto.getLogbookData().get(c.getCellId()) != "") {
					//							Map<String, String> logbookDataMap = (Map<String, String>) transactionDto.getLogbookData().get(c.getCellId());
					//
					//							// Get the value from the logbook data map.
					//							if (logbookDataMap.get("value") != null) {
					//								//								System.err.println("mp.get(\"value\") : " + logbookDataMap.get("value")); // Print the value for debugging.
					//								//								value = logbookDataMap.get("value"); // Set the value to the cell's value.
					//								Object cellValue = logbookDataMap.get("value");
					//
					//								if (cellValue instanceof byte[]) {
					//									// If the value is binary, treat it as an image or file.
					//									byte[] fileContent = (byte[]) cellValue;
					//									if(fileContent.length > 3 && 
					//											((fileContent[0] == (byte) 0xFF && fileContent[1] == (byte) 0xD8 && fileContent[2] == (byte) 0xFF) || // JPEG
					//													(fileContent[0] == (byte) 0x89 && fileContent[1] == (byte) 0x50 && fileContent[2] == (byte) 0x4E && fileContent[3] == (byte) 0x47) || // PNG
					//													(fileContent[0] == (byte) 0x47 && fileContent[1] == (byte) 0x49 && fileContent[2] == (byte) 0x46)) // GIF
					//											) {
					//										Image image = new Image(ImageDataFactory.create(fileContent));
					//										image.setAutoScale(true);
					//										tCell.add(image);
					//									}
					//									else {
					//										value = "Blob_File"; // Set the value to the cell's value.
					//										System.err.println("cellValue.toString() :: " + cellValue.toString());
					//										p.add(value); // Add the value to the paragraph.
					//										tCell.add(p); // Add the paragraph to the cell.
					//									}
					//								} else {
					//									value = cellValue.toString(); // Set the value to the cell's value.
					//									System.err.println("cellValue.toString() :: " + cellValue.toString());
					//									p.add(value); // Add the value to the paragraph.
					//									tCell.add(p); // Add the paragraph to the cell.
					//								}
					//							}
					//						} 
					//						//						else {
					//						//							// If no value is found, add an empty value to the paragraph and cell.
					//						//							value = "";
					//						//							p.add(value); // Add the empty value to the paragraph.
					//						//							tCell.add(p); // Add the paragraph to the cell.
					//						//							tCell.setNextRenderer(new CellLinesRenderer(tCell)); // Set a new renderer for the cell lines.
					//						//						}
					//						else {
					//							// If aliasId is available, use it as fallback value
					//							if (c.getAliasId() != null && !c.getAliasId().isEmpty()) {
					//								value = c.getAliasId();
					//							} else {
					//								value = "";
					//							}
					//							p.add(value);
					//							tCell.add(p);
					//							tCell.setNextRenderer(new CellLinesRenderer(tCell));
					//						}
					//
					//					}
					if (!c.getFieldType().equalsIgnoreCase("image")) {
						Object cellData = transactionDto.getLogbookData().get(c.getCellId());
						boolean valueSet = false;

						if (cellData != null && !cellData.toString().isEmpty()) {
							if (cellData instanceof Map) {
								Map<String, Object> logbookDataMap = (Map<String, Object>) cellData;
								if (logbookDataMap.get("value") != null) {
									Object cellValue = logbookDataMap.get("value");

									if (cellValue instanceof byte[]) {
										byte[] fileContent = (byte[]) cellValue;
										if (fileContent.length > 3 && (
												(fileContent[0] == (byte) 0xFF && fileContent[1] == (byte) 0xD8 && fileContent[2] == (byte) 0xFF) || // JPEG
												(fileContent[0] == (byte) 0x89 && fileContent[1] == (byte) 0x50 && fileContent[2] == (byte) 0x4E && fileContent[3] == (byte) 0x47) || // PNG
												(fileContent[0] == (byte) 0x47 && fileContent[1] == (byte) 0x49 && fileContent[2] == (byte) 0x46)) // GIF
												) {
											Image image = new Image(ImageDataFactory.create(fileContent));
											image.setAutoScale(true);
											tCell.add(image);
											valueSet = true;
										} else {
											value = "Blob_File";
										}
									} else {
										value = cellValue.toString();
									}
									if (!valueSet) {
										p.add(value);
										tCell.add(p);
										valueSet = true;
									}
								}
							} else {
								// It's a simple value, not a map
								value = cellData.toString();
								p.add(value);
								tCell.add(p);
								valueSet = true;
							}
						}

						// If value was not set and aliasId exists, use aliasId
						if (!valueSet) {
							if (c.getAliasId() != null && !c.getAliasId().isEmpty()) {
								if(!c.isEditable()) {
									value = c.getAliasId();
								}
								else {
									value = "";
								}
							} else {
								value = "";
							}
							p.add(value);
							tCell.add(p);
							if(value == "") {
								tCell.setNextRenderer(new CellLinesRenderer(tCell));
							}
						}
					}

					else if (c.getFieldType().equalsIgnoreCase("image")) { // If the cell contains an image.
						// Check if the image content is present.
						if (c.getFileContent() == null || c.getFileContent().length == 0) {
							System.out.println("Could not find resource"); // Print a message if the image resource is not found.
						} else {
							// Create an image from the file content and set its dimensions and alignment.
							Image image = new Image(ImageDataFactory.create(c.getFileContent()));
							image.setHeight(c.getImgHeight()).setWidth(c.getImgWidth()).setHorizontalAlignment(HorizontalAlignment.CENTER);
							tCell.add(image.setAutoScaleWidth(true).setAutoScaleHeight(true)); // Add the image to the cell and auto-scale it.
						}

					} else if (c.getFieldType().equalsIgnoreCase("button")) { // If the cell is neither editable nor an image.
						// Set the cell's value based on its alias ID or set it to an empty string.
						if (c.getAliasId() != null) {
							value = c.getAliasId()+"_Button"; // Get the alias ID as the value.
						} else {
							value = "_Button"; // Set the value to empty if no alias ID is found.
						}

						p.add(value); // Add the value to the paragraph.
						tCell.add(p); // Add the paragraph to the cell.
					} else { // If the cell is neither editable nor an image.
						// Set the cell's value based on its alias ID or set it to an empty string.
						//						if (c.getAliasId() != null) {
						//							value = c.getAliasId(); // Get the alias ID as the value.
						//						} else {
						value = ""; // Set the value to empty if no alias ID is found.
						//						}

						p.add(value); // Add the value to the paragraph.
						tCell.add(p); // Add the paragraph to the cell.
					}
					// Set padding and alignment for the cell.
					tCell.setPadding(4); // Add padding around the cell.
					tCell.setTextAlignment(TextAlignment.CENTER); // Center the text in the cell.
					tCell.setVerticalAlignment(VerticalAlignment.MIDDLE); // Center the text vertically in the cell.

					//					// Extract text and background colors once
					//					int[] textColor = extractHexColor(c.getCellCSS(), "color");
					//					int[] backgroundColor = extractHexColor(c.getCellCSS(), "background-color");
					//
					//					// If either color is null, skip adding styles
					//					if (textColor == null || backgroundColor == null) {
					//						System.err.println("Skipping style due to missing color. CSS: " + c.getCellCSS());
					//					} else {
					//
					//						//					tCell.addStyle(new Style().setBackgroundColor(new DeviceRgb(255, 0, 0)));
					//						//					tCell.addStyle(new Style().setBackgroundColor(new DeviceRgb(0x80, 0x80, 0x80))); // Gray: #808080
					//						// Apply extracted colors using DeviceRgb
					//						tCell.addStyle(new Style()
					//								.setFontColor(new DeviceRgb(textColor[0], textColor[1], textColor[2])) // Set text color
					//								.setBackgroundColor(new DeviceRgb(backgroundColor[0], backgroundColor[1], backgroundColor[2])) // Set background color
					//								);
					//					}


					// Add the cell to the table.
					table.addCell(tCell);
				}

				rowNumber++;

				counter++; // Increment the counter after processing each row.
			}

			System.out.println("tableWidth = " + tableWidth);

			// Center the table horizontally in the document.
			table.setHorizontalAlignment(HorizontalAlignment.CENTER);

			// Add the completed table to the document.
			document.add(table);
		} catch (Exception e) {
			e.printStackTrace(); // Print any errors that occur during the process.
		}
	}

	// This method adds content to the PDF document based on the provided cell details and transaction data.
	public void addContent(Document document, List<CellDetailsDto> cellDetailsList, List<TransactionDto> transactionDtoList, int index) {

		TransactionDto transactionDto = transactionDtoList.get(index);
		System.out.println("Downloading...Document");

		if (cellDetailsList == null || cellDetailsList.isEmpty()) {
			System.out.println("No cell data to process.");
			return;
		}

		boolean[][] occupiedGrid = fillCell(cellDetailsList);

		int maxRowNum = occupiedGrid.length;
		int maxColNum = maxRowNum > 0 ? occupiedGrid[0].length : 0;
		System.out.println("Grid size: " + maxRowNum + "x" + maxColNum);

		// Flatten all cells 
		List<CellDto> allCells = new ArrayList<>();
		for (CellDetailsDto detail : cellDetailsList) {
			if (detail.getCellData() != null) {
				allCells.addAll(detail.getCellData());
			}
		}

		// build grid
		CellDto[][] grid = new CellDto[maxRowNum][maxColNum];
		for (CellDto cell : allCells) {
			for (int i = 0; i < cell.getRowSpan(); i++) {
				for (int j = 0; j < cell.getColSpan(); j++) {
					int rowIndex = cell.getRowNum() - 1 + i;
					int colIndex = cell.getColNum() - 1 + j;
					if (rowIndex < maxRowNum && colIndex < maxColNum) {
						grid[rowIndex][colIndex] = cell;
					}
				}
			}
		}

		try {
			float[] colWidths = new float[maxColNum];
			for (int c = 0; c < maxColNum; c++) {
				float maxWidth = 50;
				for (int r = 0; r < maxRowNum; r++) {
					CellDto cell = grid[r][c];
					if (cell != null) {
						Object cellData = transactionDto.getLogbookData().get(cell.getCellId());
						String text = "";
						if (cellData instanceof Map) {
							Object val = ((Map<?, ?>) cellData).get("value");
							text = val != null ? val.toString() : "";
						} else if (cellData != null) {
							text = cellData.toString();
						}
						maxWidth = Math.max(maxWidth, text.length() * 5);
					}
				}
				colWidths[c] = maxWidth;
			}

			// Step 2: Determine page orientation based on total width
			PageSize[] pageSizes = new PageSize[]{PageSize.A4, PageSize.A3, PageSize.A2, PageSize.A1};

			float totalTableWidth = 0;
			for (float w : colWidths) totalTableWidth += w;

			PageSize selectedPageSize = PageSize.A4; // default

			System.out.println("totalTableWidth -- " + totalTableWidth);

			for (PageSize ps : pageSizes) {
				float availableWidthPortrait = ps.getWidth() - document.getLeftMargin() - document.getRightMargin();
				float availableWidthLandscape = ps.getWidth() - document.getLeftMargin() - document.getRightMargin();

				System.out.println("availableWidthPortrait -- " + availableWidthPortrait);
				System.out.println("availableWidthLandscape -- " + availableWidthLandscape);

				if (totalTableWidth <= availableWidthPortrait) {
					selectedPageSize = ps;
					break;
				} else if (totalTableWidth <= availableWidthLandscape) {
					selectedPageSize = ps.rotate();
					break;
				}
			}

			System.out.println("selectedPageSize.getWidth() -- " + selectedPageSize.getWidth() + " : selectedPageSize.getHeight() -- " + selectedPageSize.getHeight());

			document.getPdfDocument().setDefaultPageSize(selectedPageSize);
			float pageWidth = selectedPageSize.getWidth() - document.getLeftMargin() - document.getRightMargin();

			// Step 3: Scale columns to fit page
			for (int i = 0; i < colWidths.length; i++) {
				colWidths[i] = colWidths[i] * pageWidth / totalTableWidth;
			}

			Table table = new Table(colWidths);
			table.setHorizontalAlignment(HorizontalAlignment.CENTER);

			//			Table table = new Table(maxColNum);

			for (int r = 0; r < maxRowNum; r++) {
				for (int c = 0; c < maxColNum; c++) {
					CellDto cell = grid[r][c];
					Cell tCell;

					if (cell != null && cell.getRowNum() - 1 == r && cell.getColNum() - 1 == c) {

						// Only add the top-left of the spanned cell
						tCell = new Cell(cell.getRowSpan(), cell.getColSpan());

						// --- Add content ---
						Object cellData = transactionDto.getLogbookData().get(cell.getCellId());
						Paragraph p = new Paragraph();
						String value = "";

						if (!cell.getFieldType().equalsIgnoreCase("image")) {
							cellData = transactionDto.getLogbookData().get(cell.getCellId());
							boolean valueSet = false;

							if (cellData != null) {
								// If cellData is a Map
								if (cellData instanceof Map<?, ?> map) {
									Object cellValue = map.get("value");

									if (cellValue instanceof byte[] fileContent && fileContent.length > 3) {
										// Check if byte array is an image
										boolean isImage =
												(fileContent[0] == (byte)0xFF && fileContent[1] == (byte)0xD8 && fileContent[2] == (byte)0xFF) || // JPEG
												(fileContent[0] == (byte)0x89 && fileContent[1] == (byte)0x50 && fileContent[2] == (byte)0x4E && fileContent[3] == (byte)0x47) || // PNG
												(fileContent[0] == (byte)0x47 && fileContent[1] == (byte)0x49 && fileContent[2] == (byte)0x46); // GIF

										if (isImage) {
											Image image = new Image(ImageDataFactory.create(fileContent));
											image.setAutoScale(true);
											tCell.add(image);
											valueSet = true;
										} else {
											value = "Blob_File";
										}
									} else if (cellValue != null) {
										value = cellValue.toString();
									}
								} else {
									// Simple value
									value = cellData.toString();
								}
							}

							// If still no value, use aliasId if not editable
							if (!valueSet) {
								if ((value == null || value.isEmpty()) && cell.getAliasId() != null && !cell.getAliasId().isEmpty() && !cell.isEditable()) {
									value = cell.getAliasId();
								}

								tCell.add(new Paragraph(value.isEmpty() ? "--" : value));

								if (value.isEmpty()) {
									//									tCell.setNextRenderer(new CellLinesRenderer(tCell));
									tCell.add(new Paragraph(""));
								}
							}
						}

						else if (cell.getFieldType().equalsIgnoreCase("image")) { // If the cell contains an image.
							// Check if the image content is present.
							if (cell.getFileContent() == null || cell.getFileContent().length == 0) {
								System.out.println("Could not find resource"); // Print a message if the image resource is not found.
							} else {
								// Create an image from the file content and set its dimensions and alignment.
								Image image = new Image(ImageDataFactory.create(cell.getFileContent()));
								image.setHeight(cell.getImgHeight()).setWidth(cell.getImgWidth()).setHorizontalAlignment(HorizontalAlignment.CENTER);
								tCell.add(image.setAutoScaleWidth(true).setAutoScaleHeight(true)); // Add the image to the cell and auto-scale it.
							}

						} else if (cell.getFieldType().equalsIgnoreCase("button")) { // If the cell is neither editable nor an image.
							// Set the cell's value based on its alias ID or set it to an empty string.
							if (cell.getAliasId() != null) {
								value = cell.getAliasId()+"_Button"; // Get the alias ID as the value.
							} else {
								value = "_Button"; // Set the value to empty if no alias ID is found.
							}

							p.add(value); // Add the value to the paragraph.
							tCell.add(p); // Add the paragraph to the cell.
						} else if (cell.getFieldType().equalsIgnoreCase("boolean")) {
							if (cellData != null && String.valueOf(((Map<?, ?>) cellData).get("value")).equalsIgnoreCase("true")) {
								value = ("✔"); // mimic CheckCircleIcon
							} else {
								value = ("✘"); // mimic CancelIcon
							}
							p.add(value); // Add the value to the paragraph.
							tCell.add(p); // Add the paragraph to the cell.
						}
						else { // If the cell is neither editable nor an image.
							value = ""; // Set the value to empty if no alias ID is found.
							p.add(value); // Add the value to the paragraph.
							tCell.add(p); // Add the paragraph to the cell.
						}

					} else if (cell == null) {
						tCell = new Cell(1, 1);
						tCell.add(new Paragraph(""));
					} else {
						continue; // part of a spanned cell
					}

					tCell.setPadding(4);
					tCell.setTextAlignment(TextAlignment.CENTER);
					tCell.setVerticalAlignment(VerticalAlignment.MIDDLE);
					tCell.setHorizontalAlignment(HorizontalAlignment.CENTER);

					table.addCell(tCell);

					//// For every created tCell
					// tCell.setKeepTogether(true);
				}
			}

			document.add(table);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Retrieves transaction data based on the job ID and activity ID.
	 * @param tenantId The ID of the tenant for which the data is being retrieved.
	 * @param jobId The ID of the job associated with the transaction.
	 * @param activityId The ID of the activity associated with the transaction.
	 * @return A TransactionDto object containing the transaction data.
	 */
	public TransactionDto getTransactionsByJobIdAndActivityId(String tenantId, String jobId, String activityId) {

		JdbcTemplate jdbcTemplateOp360 = jdbcTemplateCollection.get(tenantId).get(0);

		// Define a SQL query to select the most recent transaction for the specified job and activity.
		String query = "SELECT TOP (1) [slno] ,[TransactionId] ,[JobId] ,[ActivityId] ,[formname] ,[version] ,[logbookdata] ,[transaction_remarks] ,[timestamp] ,[userid] ,[role] FROM [dbo].[LogbookTransactionData] where JobId = ? And ActivityId = ? AND TenantId = ? ORDER BY [timestamp] DESC";

		// Prepare the parameters to be used in the SQL query.
		Object[] args = {jobId, activityId, tenantId};

		// Execute the query and map the result to a Transaction object.
		Transaction trData = jdbcTemplateOp360.queryForObject(query, new BeanPropertyRowMapper<Transaction>(Transaction.class), args);

		// Convert the Transaction object into a TransactionDto object for easier use.
		TransactionDto transactionDto = new TransactionDto(trData);

		return transactionDto; // Return the TransactionDto object containing the transaction data.
	}

	/**
	 * Exports logbook data to a PDF document for a specific form and version.
	 * @param tenantId The ID of the tenant for which the data is being exported.
	 * @param formName The name of the form associated with the logbook data.
	 * @param version The version of the form.
	 * @param index The index of the transaction in the list.
	 * @return A ByteArrayInputStream containing the generated PDF.
	 */
	public ByteArrayInputStream exportSingleLogBookDataToPdf(String tenantId, String userid, String formName, int version, String jobId, String activityId) {
		this.cellRowNum = 1; // Initialize the row number for cells.

		// Retrieve all cell information for the specified form and version.
		List<CellDto> cellList = logbookRepositoryImpl.getAllCellInfoByFormNameAndVersion(formName, version, tenantId);
		ArrayList<CellDetailsDto> cellDetailsList = new ArrayList<CellDetailsDto>(); // List to hold cell details.

		// Retrieve all transactions for the specified form and version.
		//		List<Transaction> transactions = logbookRepositoryImpl.getTransactionsByFormNameAndVersion(formName, version, tenantId);
		List<Transaction> transactions = logbookRepositoryImpl.getAllTransactions(jobId, activityId, formName, version, tenantId);

		ArrayList<TransactionDto> transactionDtoList = new ArrayList<>(); // List to hold transaction data.

		// Process each transaction to convert it to a TransactionDto.
		transactions.forEach((transaction) -> {
			TransactionDto transactionDto = new TransactionDto(transaction);
			ObjectMapper objectMapper = new ObjectMapper(); // Create an ObjectMapper for JSON processing.
			try {
				// Convert the logbook data from JSON string to a Map.
				Map<String, Object> jsonInfo = objectMapper.readValue(transaction.getLogbookData(), Map.class);
				transactionDto.setLogbookData(jsonInfo); // Set the logbook data in the TransactionDto.
			} catch (JsonMappingException e) {
				e.printStackTrace(); // Print stack trace if there is a mapping error.
			} catch (JsonProcessingException e) {
				e.printStackTrace(); // Print stack trace if there is a processing error.
			}
			transactionDtoList.add(transactionDto); // Add the TransactionDto to the list.
		});

		// Filter cell data by row number and build a list of cell details.
		while (true) {
			List<CellDto> filteredList = cellList.stream().filter(cell -> {
				return cell.getRowNum() == this.cellRowNum; // Filter cells that match the current row number.
			}).collect(Collectors.toList());
			if (filteredList.isEmpty()) break; // Break if no cells are found for the current row number.
			else {
				CellDetailsDto cellDetailsDto = new CellDetailsDto();
				cellDetailsDto.setCellData(filteredList); // Set the filtered cells in CellDetailsDto.
				cellDetailsList.add(cellDetailsDto); // Add the CellDetailsDto to the list.
			}
			this.cellRowNum++; // Increment the row number for the next iteration.
		}

		// Retrieve form information based on the form name and version.
		FormInfo forminfo = forminfoByformNameAndVersion(tenantId, formName, version);

		ByteArrayOutputStream out = new ByteArrayOutputStream(); // Create an output stream for the PDF.

		try {
			// Initialize the PDF writer and document.
			PdfWriter writer = new PdfWriter(out);
			PdfDocument pdfDoc = new PdfDocument(writer);
			Document document = new Document(pdfDoc);
			document.getPdfDocument().setDefaultPageSize(PageSize.A4); // Set the default page size to A4.

			// Set up header and footer event handlers for the PDF document.
			HeaderEventHandler headerHandler = new HeaderEventHandler(document, forminfo, userid);
			FooterEventHandler footerHanlder = new FooterEventHandler(document, transactionDtoList.get(0), tenantId);

			// Add the event handlers to the PDF document.
			pdfDoc.addEventHandler(PdfDocumentEvent.START_PAGE, headerHandler);
			pdfDoc.addEventHandler(PdfDocumentEvent.END_PAGE, footerHanlder);

			// Add the content (cells and transactions) to the document.
			addContent(document, cellDetailsList, transactionDtoList, 0);

			// Close the document and writer to finalize the PDF.
			document.close();
			pdfDoc.close();
			writer.close();

			System.out.println("Pdf created successfully."); // Print success message.
		} catch (Exception e) {
			e.printStackTrace(); // Print stack trace if an error occurs.
		}
		return new ByteArrayInputStream(out.toByteArray()); // Return the PDF as a ByteArrayInputStream.
	}


	/**
	 * Retrieves details for the PDF footer based on the job ID, activity ID, and tenant ID.
	 * @param jobId The ID of the job.
	 * @param activityId The ID of the activity.
	 * @param tenantId The ID of the tenant.
	 * @return A map containing the activity name, task name, and asset name.
	 */
	public Map<String, String> getPdfFooterDetails(String jobId, String activityId, String tenantId) {

		JdbcTemplate jdbcTemplateOp360 = jdbcTemplateCollection.get(tenantId).get(0);

		// Define a SQL query to retrieve the footer details based on the job and activity IDs.
		String sql="DECLARE @JobId VARCHAR(50) = ?; DECLARE @ActivityId VARCHAR(50) = ?; DECLARE @TenantId VARCHAR(50) = ?; declare @assetname varchar(50)=''; declare @taskname varchar(50)=''; if(LEFT(@JobId, 2) = 'AJ' ) begin set @taskname=null end else begin set @taskname=(select  top 1  [TaskName] from dbo.TaskDetails where TaskId=(select TaskId from dbo.[AutoJobInfo] where jobid=@JobId and [activityid]=@ActivityId)) end IF LEFT(@JobId, 2) = 'AJ' AND LEFT(@ActivityId, 2) = 'AA' BEGIN set @assetname=(select  [AssetName] from [dbo].[AssetInfo] where AssetId=(select AssetId from [dbo].[AutoJobInfo] where jobid=@JobId and [activityid]=@ActivityId)) SELECT ActivityName=null,TaskName=@taskname, AssetName=@assetname FROM [dbo].[AutoJobInfo] AJI WHERE AJI.activityid = @ActivityId AND AJI.jobid = @JobId AND AJI.TenantId = @TenantId; END ELSE BEGIN SELECT AC.ActivityName, TD.TaskName, JAD.AssetName FROM [dbo].[ActivityCreation] AS AC JOIN [dbo].[JobActivityDetails] AS JAD ON AC.ActivityId = JAD.ActivityId JOIN [dbo].[TaskDetails] AS TD ON TD.TaskId = JAD.TaskId WHERE AC.ActivityId = @ActivityId AND JAD.JobId = @JobId AND AC.TenantId = @TenantId; END";

		// Execute the SQL query and store the result in a list of maps.
		List<Map<String, Object>> result = jdbcTemplateOp360.queryForList(sql, jobId, activityId, tenantId);

		// Create a new map to hold the footer details for the PDF.
		Map<String, String> pdfFooter = new HashMap<>();
		result.forEach(row -> {
			// Populate the map with activity name, task name, and asset name from the result.
			pdfFooter.put("activityName", (String) row.get("ActivityName"));
			pdfFooter.put("taskName", (String) row.get("TaskName"));
			pdfFooter.put("assetName", (String) row.get("AssetName"));
		});

		return pdfFooter; // Return the map containing footer details.
	}

	/**
	 * Retrieves a user model based on the tenant ID.
	 * @param tenantId The ID of the tenant.
	 * @return A UserModel object containing user details, or null if not found.
	 */
	public UserModel findByTenantId(String tenantId) {

		// Define a SQL query to retrieve user and plant details for the specified tenant.
		String readAnUserAndPlantJoinSql = "SELECT [TenantId], [AdminName], [Designation], [Email], [Password], [PhoneNumber], [AccountOwnerCustomer], [AccountOwnerGw], u.[PlantId], [plant_name], [address], [division], [Homepage] FROM [dbo].[tenant_details] u JOIN [dbo].[plant_details] p ON u.PlantId = p.plant_id WHERE u.TenantId = ?";

		try {

			// Execute the SQL query and map the result to a list of UserModel objects.
			List<UserModel> userList = jdbcTemplateOp360Tenant.query(readAnUserAndPlantJoinSql, 
					ps -> ps.setString(1, tenantId), (rs, rowNum) -> {
						UserModel userModel = new UserModel(); // Create a new UserModel instance.
						userModel.setTenantId(rs.getString("TenantId"));
						userModel.setAdminName(rs.getString("AdminName"));
						userModel.setDesignation(rs.getString("Designation"));
						userModel.setEmail(rs.getString("Email"));
						userModel.setPassword(rs.getString("Password"));
						userModel.setPhoneNumber(rs.getString("PhoneNumber"));
						userModel.setPlantID(rs.getString("PlantId"));
						userModel.setPlantName(rs.getString("plant_name"));
						userModel.setAddress(rs.getString("Address"));
						userModel.setDivision(rs.getString("Division"));
						userModel.setAccountOwnerCustomer(rs.getString("AccountOwnerCustomer"));
						userModel.setAccountOwnerGW(rs.getString("AccountOwnerGw"));
						userModel.setHomepage(rs.getString("Homepage"));
						return userModel; // Return the populated UserModel object.
					});

			// Check if any users were found, and return the first one if available, otherwise return null.
			return userList.isEmpty() ? null : userList.get(0);

		} catch(Exception e) {
			System.out.println("error occurred in findByTenantId :: " + e);
			return null; // <-- Important: ensure a UserModel return type is satisfied
		}
	}

	public List<TransactionAttachmentDto> saveTransactionAttachment(String tenantId, List<TransactionAttachmentDto> transactionAttachmentDtos) {

		JdbcTemplate jdbcTemplateOp360 = jdbcTemplateCollection.get(tenantId).get(0);

		String sql = "INSERT INTO dbo.LogbookTransactionAttachment (TransactionId, JobId, ActivityId, formname, version, CellId, FileName, FileContent, TenantId) VALUES (?, ? ,? ,?, ?, ?, ?, ?, ?)";

		List<TransactionAttachmentDto> successfullyInsertedDtos = new ArrayList<>();
		List<TransactionAttachmentDto> failedDtos = new ArrayList<>();

		for (TransactionAttachmentDto dto : transactionAttachmentDtos) {
			try {
				int rowsAffected = jdbcTemplateOp360.update(sql,
						dto.getTransactionId(),
						dto.getJobId(),
						dto.getActivityId(),
						dto.getFormName(),
						dto.getFormversion(),
						dto.getCellId(),
						dto.getFileName(),
						dto.getFileContent(),
						tenantId
						);

				if (rowsAffected > 0) {
					System.err.println("dto :: " + dto);
					successfullyInsertedDtos.add(dto);
				} else {
					System.err.println("Failed to insert transaction attachment for TransactionId: " + dto.getTransactionId());
					failedDtos.add(dto);
				}
			} catch (Exception e) {
				System.err.println("Exception occurred while inserting TransactionId: " + dto.getTransactionId() + " - " + e.getMessage());
				failedDtos.add(dto);
			}
		}

		System.out.println("Successfully inserted: " + successfullyInsertedDtos.size() + " records.");
		if (!failedDtos.isEmpty()) {
			System.err.println("Failed to insert " + failedDtos.size() + " records.");
		}

		return successfullyInsertedDtos; // Return only successfully inserted records
	}


	public List<TransactionAttachmentDto> getTransactionAttachment(String tenantId, String transactionId) {

		JdbcTemplate jdbcTemplateOp360 = jdbcTemplateCollection.get(tenantId).get(0);

		String sql = "SELECT TransactionId, JobId, ActivityId, formname, version, CellId, FileName, FileContent, TenantId FROM dbo.LogbookTransactionAttachment WHERE TransactionId = ? AND TenantId = ?";

		try {
			List<Map<String, Object>> rows = jdbcTemplateOp360.queryForList(sql, transactionId, tenantId);

			List<TransactionAttachmentDto> transactionAttachments = new ArrayList<>();

			for (Map<String, Object> row : rows) {
				TransactionAttachmentDto dto = new TransactionAttachmentDto(
						(String) row.get("TransactionId"),
						(String) row.get("JobId"),
						(String) row.get("ActivityId"),
						(String) row.get("formname"),
						(Integer) row.get("version"),
						(String) row.get("CellId"),
						(String)row.get("FileName"),
						(byte[]) row.get("FileContent"),
						(String) row.get("TenantId")
						);
				transactionAttachments.add(dto);
			}

			return transactionAttachments;
		} catch (Exception e) {
			System.err.println("Error retrieving transaction attachments for TransactionId: " + transactionId + " - " + e.getMessage());
			return Collections.emptyList(); // Return an empty list if an exception occurs
		}
	}


	public TransactionAttachmentDto getTransactionAttachmentFromCellIdAndTransactionId(String tenantId, String transactionId, String cellId) {

		JdbcTemplate jdbcTemplateOp360 = jdbcTemplateCollection.get(tenantId).get(0);

		String sql = "SELECT TransactionId, JobId, ActivityId, formname, version, CellId, FileName, FileContent, TenantId FROM dbo.LogbookTransactionAttachment WHERE TransactionId = ? AND CellId = ? AND TenantId = ?";

		try {
			Map<String, Object> row = jdbcTemplateOp360.queryForMap(sql, transactionId, cellId, tenantId);

			TransactionAttachmentDto dto = new TransactionAttachmentDto(
					(String) row.get("TransactionId"),
					(String) row.get("JobId"),
					(String) row.get("ActivityId"),
					(String) row.get("formname"),
					(Integer) row.get("version"),
					(String) row.get("CellId"),
					(String)row.get("FileName"),
					(byte[]) row.get("FileContent"),
					(String) row.get("TenantId")
					);
			return dto;
		} catch (Exception e) {
			System.err.println("Error retrieving transaction attachments for TransactionId: " + transactionId + " - " + e.getMessage());
			return null; // Return an empty list if an exception occurs
		}
	}

	public List<Map<String, Object>> customReportColumnMapping(String reportName, String tenantId) {

		JdbcTemplate jdbcTemplateOp360 = jdbcTemplateCollection.get(tenantId).get(0);

		String sql = "SELECT distinct ReportName, FormName, VersionNumber, CellId, ReportAliasId FROM [dbo].[reportconfigurator] where [TenantId] = ? AND ReportName = ? order by CellId;";

		try {
			List<Map<String, Object>> row = jdbcTemplateOp360.queryForList(sql, tenantId, reportName);

			return row;
		} catch (Exception e) {
			System.err.println("Error retrieving transaction attachments for TransactionId: " + reportName + " - " + e.getMessage());
			return null; // Return an empty list if an exception occurs
		}
	}

	private boolean[][] fillCell(List<CellDetailsDto> cellDetailsList) {
		if (cellDetailsList == null || cellDetailsList.isEmpty()) {
			return new boolean[0][0];
		}

		List<CellDto> allCells = new ArrayList<>();
		for (CellDetailsDto detail : cellDetailsList) {
			if (detail.getCellData() != null) {
				allCells.addAll(detail.getCellData());
			}
		}

		int maxColNum = 0, maxRowNum = 0;
		for (CellDto cell : allCells) {
			int colNum = cell.getColNum();
			int colSpan = Math.max(cell.getColSpan(), 1);
			int rowNum = cell.getRowNum();
			int rowSpan = Math.max(cell.getRowSpan(), 1);

			maxColNum = Math.max(maxColNum, colNum + colSpan - 1);
			maxRowNum = Math.max(maxRowNum, rowNum + rowSpan - 1);
		}

		boolean[][] occupiedGrid = new boolean[maxRowNum][maxColNum];
		for (CellDto cell : allCells) {
			for (int i = 0; i < cell.getRowSpan(); i++) {
				for (int j = 0; j < cell.getColSpan(); j++) {
					int rowIndex = (cell.getRowNum() - 1) + i;
					int colIndex = (cell.getColNum() - 1) + j;
					if (rowIndex < maxRowNum && colIndex < maxColNum) {
						occupiedGrid[rowIndex][colIndex] = true;
					}
				}
			}
		}
		return occupiedGrid;
	}

}