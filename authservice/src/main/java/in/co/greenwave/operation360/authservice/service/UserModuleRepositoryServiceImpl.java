package in.co.greenwave.operation360.authservice.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import in.co.greenwave.operation360.authservice.entity.AuthRequest;
import in.co.greenwave.operation360.authservice.entity.Page;
import in.co.greenwave.operation360.authservice.entity.TenantConfigDetails;
import in.co.greenwave.operation360.authservice.entity.User;
import in.co.greenwave.operation360.authservice.entity.UserDepartment;
import in.co.greenwave.operation360.authservice.entity.UserGroup;
import in.co.greenwave.operation360.authservice.helper.UserModuleHelper;
import in.co.greenwave.operation360.authservice.repository.UserModuleRepositoryService;
import in.co.greenwave.operation360.authservice.utility.JdbcUrlUtil;

/**
 * Implementation of {@link UserModuleRepositoryService}.
 * 
 * This class provides the concrete implementation for user-related repository operations, such as
 * retrieving user information and logging incorrect password attempts. It interacts with the database
 * using JDBC.
 */
@Repository
public class UserModuleRepositoryServiceImpl implements UserModuleRepositoryService {

	@Autowired
	@Qualifier("DatasourceCollections")
	Map<String, List<JdbcTemplate>> jdbcTemplates; // Template for operations on the main database
	
	@Autowired
	@Qualifier("jdbcTemplate_OP360_tenant")
	JdbcTemplate jdbcTemplateOp360Tenant; // Template for Tenant database operations
	

	@Autowired
	private UserModuleHelper userModuleHelper; // Helper class for user-related functionalities

	/**
	 * Logs an incorrect password attempt for a specific user.
	 * 
	 * @param userID the unique identifier of the user.
	 * @param tenantId the tenant ID associated with the user.
	 * @param ipAddress the IP address of the client making the request.
	 * @return true if the log entry was successfully added, false otherwise.
	 */
	public boolean addWrongPasswordLog(AuthRequest authRequest, String ipAddress) {
		JdbcTemplate jdbcTemplateOp360Usermodule=jdbcTemplates.get(authRequest.getTenantId()).get(1);
		String userId = authRequest.getUsername();
		String tenantId = authRequest.getTenantId();
		
		// SQL query to insert the wrong password attempt into the log
		String wrongPasswordLogSql = "INSERT INTO [dbo].[wrongpassword_log] ([UserId], [timestamp], [ipaddress], [TenantId]) VALUES (?, ?, ?, ?)";
		// Execute the query and get the number of affected rows
		int result = jdbcTemplateOp360Usermodule.update(wrongPasswordLogSql, userId, LocalDateTime.now(), ipAddress, tenantId);
		System.out.println("addWrongPasswordLog : : " + result);
		// Return true if at least one row was affected, indicating success
		return result > 0;
	}

	/**
	 * Fetches the details of a user based on their ID and tenant information.
	 *
	 * @param tenantId The ID representing the user's tenant.
	 * @param userId   The unique identifier for the user.
	 * @return A User object containing all relevant user details.
	 */
	public User getByUserId(AuthRequest authRequest) {
		System.out.println("Jdbc templates:=====>"+jdbcTemplates.toString());
		System.out.println("Requested connection"+jdbcTemplates.hashCode());
        
		String userId = authRequest.getUsername();
		String tenantId = authRequest.getTenantId();
		System.out.println("Tenantis:"+authRequest.getTenantId());
		JdbcTemplate jdbcTemplateOp360Usermodule=jdbcTemplates.get(authRequest.getTenantId()).get(1);
		
		System.out.println("getByUserId userId : : " + userId);
		System.out.println("getByUserId tenantId : : " + tenantId);

		// SQL query to select user details along with their roles and pages
		String selectUsersQuery = 
				"SELECT " +
						"    [UserId], " +
						"    [UserName], " +
						"    [Password], " +
						"    [PhoneNumber], " +
						"    [G360Homepage], " +
						"    [WorkflowHomepage], " +
						"    [G360Admin], " +
						"    [Active], " +
						"    [CreatedOn], " +
						"    [ModifiedOn], " +
						"    [CreatedBy], " +
						"    [ModifiedBy], " +
						"    [Department], " +
						"    [FirstLoginRequired], " +
						"    [PasswordExpiryDuraton], " +
						"    [PasswordExpiryDate], " +
						"    [LastLogin], " +
						"    [TenantId], " +
						"    groupDetails = (" +
						"        SELECT STRING_AGG(UserRole, ',') " +
						"        FROM [dbo].[UserRole] " +
						"        WHERE [UserRole].[UserId] = [UserCredential].[UserId] " +
						"        AND [UserRole].TenantId = ?" +
						"    ), " +
						"    allotedPages_workflow = (" +
						"        SELECT STRING_AGG([AllotedPage], ',') " +
						"        FROM [dbo].[UserPages] " +
						"        WHERE [UserPages].[UserId] = [UserCredential].[UserId] " +
						"        AND [UserPages].[TenantId] = ? " +
						"        AND [UserPages].[Source] = 'Workflow'" +
						"    ), " +
						"    allotedPages_g360 = (" +
						"        SELECT STRING_AGG([AllotedPage], ',') " +
						"        FROM [dbo].[UserPages] " +
						"        WHERE [UserPages].[UserId] = [UserCredential].[UserId] " +
						"        AND [UserPages].[TenantId] = ? " +
						"        AND [UserPages].[Source] = 'G360'" +
						"    ) " +
						"FROM " +
						"    [dbo].[UserCredential] " +
						"WHERE TenantId = ? and UserId=? AND Active = 1";

		// Queries to fetch all groups and departments related to the tenant
		String allGroupQuery = 
				"SELECT [GroupId] AS groupID, " +
						"       [GroupName] AS groupName, " +
						"       [PhoneNumber] AS phone, " +
						"       [HomePage] AS homePage, " +
						"       [Active] AS active, " +
						"       [TenantId] AS tenantId " +
						"FROM [dbo].[GroupCredentials] WHERE TenantId = ?";

		String allDepartmentQuery = 
				"SELECT [DeptId] AS deptID, " +
						"       [DeptName] AS deptName, " +
						"       [TenantId] AS tenantId " +
						"FROM [dbo].[DeptCredentials] WHERE TenantId = ?";

		// Fetch all groups and departments for the tenant
		List<UserGroup> allGroupList = jdbcTemplateOp360Usermodule.query(
				allGroupQuery,
				new BeanPropertyRowMapper<>(UserGroup.class),
				tenantId
				);
		List<UserDepartment> allDeptList = jdbcTemplateOp360Usermodule.query(
				allDepartmentQuery,
				new BeanPropertyRowMapper<>(UserDepartment.class),
				tenantId
				);

		// Query to fetch all pages available for the tenant
		String allPagesQuery = "SELECT DISTINCT [Page] as page,"
				+ "[Source] as source,"
				+ "[PageUrl] as pageUrl,"
				+ "[PageLogo] as pageLogo,"
				+ "[AliasName] as aliasName,"
				+ "[sequence] as sequence"
				+ " FROM [dbo].[Pages] ";

		// Fetch all page details for the tenant
		List<Page> allPagesDetails = jdbcTemplateOp360Usermodule.query(
				allPagesQuery,
				new BeanPropertyRowMapper<>(Page.class)
				);

		// Executing the user selection query and retrieve the results as a list of maps
		List<Map<String, Object>> rowMapList = jdbcTemplateOp360Usermodule.queryForList(
				selectUsersQuery,
				tenantId, tenantId, tenantId, tenantId, userId
				);

		List<User> users = new ArrayList<>(); // List to hold the user details

		// Process each row of user data
		rowMapList.forEach(row -> {
			User user = new User(); // Create a new User object

			// Set user properties using helper methods
			user.setUserID(userModuleHelper.getString(row, "UserId"));
			user.setUserName(userModuleHelper.getString(row, "UserName"));
			user.setPassword(userModuleHelper.getString(row, "Password"));
			user.setPhone(userModuleHelper.getNullableString(row, "PhoneNumber"));
			user.setG360HomePage(userModuleHelper.getNullableString(row, "G360Homepage"));
			user.setWorkFlowHomePage(userModuleHelper.getNullableString(row, "WorkflowHomepage"));
			user.setG360Admin(userModuleHelper.getBoolean(row, "G360Admin"));
			user.setActive(userModuleHelper.getBoolean(row, "Active"));
			user.setCreatedOn(userModuleHelper.getNullableString(row, "CreatedOn"));
			user.setModifiedOn(userModuleHelper.getNullableString(row, "ModifiedOn"));
			user.setCreatedBy(userModuleHelper.getNullableString(row, "CreatedBy"));
			user.setModifiedBy(userModuleHelper.getNullableString(row, "ModifiedBy"));

			// Find the department for the user
			UserDepartment foundDept = allDeptList.stream()
					.filter(dept -> dept.getDeptID().equals(userModuleHelper.getString(row, "Department")))
					.findFirst()
					.orElse(null);

			user.setDepartment(foundDept); // Set the found department

			// Find all user groups
			List<String> groupIds = userModuleHelper.splitToList(userModuleHelper.getNullableString(row, "groupDetails"));
			List<UserGroup> matchingGroups = allGroupList.stream()
					.filter(group -> groupIds.contains(group.getGroupID()))
					.collect(Collectors.toList());

			user.setGroup(matchingGroups); // Set the found groups

			// Find all allotted G360 pages
			List<String> green360Pages = userModuleHelper.splitToList(userModuleHelper.getNullableString(row, "allotedPages_g360"));
			List<Page> matching360Pages = allPagesDetails.stream()
					.filter(page -> green360Pages.contains(page.getPage()) && "G360".equalsIgnoreCase(page.getSource()))
					.collect(Collectors.toList());
			user.setG360AllottedPageList(matching360Pages); // Set the allotted G360 pages

			// Find all allotted Workflow pages
			List<String> workflowPages =userModuleHelper.splitToList(userModuleHelper.getNullableString(row, "allotedPages_workflow"));
			List<Page> matchingWorkFlowList = allPagesDetails.stream()
					.filter(page -> workflowPages.contains(page.getPage()) && "Workflow".equalsIgnoreCase(page.getSource()))
					.collect(Collectors.toList());

			user.setWorkflowAllottedPageList(matchingWorkFlowList); // Set the allotted Workflow pages

			user.setFirstLoginRequired(userModuleHelper.getBoolean(row, "FirstLoginRequired"));
			user.setPasswordExpiryDuration(userModuleHelper.getInteger(row, "PasswordExpiryDuraton"));
			user.setPasswordExpiryDate(userModuleHelper.getNullableString(row, "PasswordExpiryDate"));
			user.setLastLogIn(userModuleHelper.getNullableString(row, "LastLogin"));
			user.setTenantId(userModuleHelper.getString(row, "TenantId"));



			// Partition the pages into two lists based on their source
			Map<Boolean, List<Page>> partitionedPages = allPagesDetails.stream()
					.collect(Collectors.partitioningBy(
							page -> "Workflow".equalsIgnoreCase(page.getSource()), 
							Collectors.toList()));

			// Extract the lists
			List<Page> workflowPagesList = partitionedPages.getOrDefault(true, new ArrayList<>());
			List<Page> g360PagesList = partitionedPages.getOrDefault(false, new ArrayList<>());

			// Set the lists in the user object
			user.setAllWorkflowPageList(workflowPagesList);
			user.setAllG360PageList(g360PagesList);

			List<Page> allotedPages = Stream.concat(matchingWorkFlowList.stream(), matching360Pages.stream())
					.collect(Collectors.toList());
			user.setAllotedPages(allotedPages);
			users.add(user); // Add the user to the list
		});
		return users.get(0); // Return the first matching user
	}

	public List<Map<String, Object>> getTenantDB(String tenantId) {
		
		String sql = "SELECT [dateandtime] ,[TenantId] ,[AdminName] ,[Designation] ,[Email] ,[Password] ,[PhoneNumber] ,[PlantId] ,[AccountOwnerCustomer] ,[AccountOwnerGw] ,[Homepage] ,[PackageName] ,[db_name] ,[db_ip] ,[db_username] ,[db_password] FROM [dbo].[tenant_details] WHERE TenantId = ?";
		Object [] args = {tenantId};
		List<Map<String, Object>> rows = jdbcTemplateOp360Tenant.queryForList(sql, args);
		System.err.println("rows :--> " + rows);
		return rows;
	}
	
	public List<String> userWiseTenant(String userId) {
		String sql = "EXEC [dbo].[udsp_find_user_wise_tenant] ?";
		List<String> rows = jdbcTemplateOp360Tenant.query(sql, (rs, rowNum) -> rs.getString(1), userId);
	    System.err.println("rows :--> " + rows);
		return rows;
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
	        String url =JdbcUrlUtil.buildJdbcUrl(tenantDetail.getDriver(), tenantDetail.getDbIp(), dbName);
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

	    jdbcTemplates.put(tenantid, jdbcTenantList);

		System.out.println("Registered Tenant:"+jdbcTemplates.hashCode());
	    System.err.println("Jdbctemplate toString:"+jdbcTemplates.toString());
	}

	
}
