package in.co.greenwave.usermodule.dao.implementation.sqlserver;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;


import in.co.greenwave.usermodule.dao.DynamicDatasourceDAO;
import in.co.greenwave.usermodule.dao.UserModuleDAO;
import in.co.greenwave.usermodule.helper.UserModuleHelper;
import in.co.greenwave.usermodule.model.Page;
import in.co.greenwave.usermodule.model.TenantConfigDetails;
import in.co.greenwave.usermodule.model.User;
import in.co.greenwave.usermodule.model.UserDepartment;
import in.co.greenwave.usermodule.model.UserGroup;
import in.co.greenwave.usermodule.model.UserLog;
import in.co.greenwave.usermodule.utility.JdbcUrlUtil;



@Repository
public class UserModuleService implements UserModuleDAO {
	

	
//	@Autowired
//	@Qualifier("jdbcTemplate_OP360")
//	private  JdbcTemplate jdbcTemplate;
//
//
//	@Autowired
//	@Qualifier("jdbcTemplate_OP360_Usermodule")
//	private  JdbcTemplate jdbcTemplate_userModule;
	
	
	@Autowired
	@Qualifier("jdbcTemplate_OP360_tenant")
	private  JdbcTemplate jdbcTemplateOp360Tenant;
	
	@Autowired
	@Qualifier("DatasourceCollections")
	Map<String, List<JdbcTemplate>> jdbcTemplates;
	
	
	 // Logger class to see the details logs of services
    Logger logger
        = LoggerFactory.getLogger(UserModuleService.class);
	
	
	@Autowired
	private UserModuleHelper userModuleHelper;
	
	//This method retrieves all the user details based on tenantId and returns all userDetails
	@Override
	public List<User> getAllUsersDetails(String tenantId) {
		
		JdbcTemplate jdbcTemplate_usermodule=jdbcTemplates.get(tenantId).get(1);
	    
//	    JdbcTemplate jdbcTemplate_usermodule=jdbcTemplates.get(tenantId).get(1);
	    // Query to select all user details
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
	        "WHERE TenantId = ?";
	    
	    //This the query responsible for getting all the group details.
	    String allGroupQuery = 
	        "SELECT [GroupId] AS groupID, " +
	        "       [GroupName] AS groupName, " +
	        "       [PhoneNumber] AS phone, " +
	        "       [HomePage] AS homePage, " +
	        "       [Active] AS active, " +
	        "       [TenantId] AS tenantId " +
	        "FROM [dbo].[GroupCredentials] WHERE TenantId = ?";
	    
	    //This is the query responsible for getting all the department details based on tenantId
	    String allDepartmentQuery = 
	        "SELECT [DeptId] AS deptID, " +
	        "       [DeptName] AS deptName, " +
	        "       [TenantId] AS tenantId " +
	        "FROM [dbo].[DeptCredentials] WHERE TenantId = ?";
	    
	    //allGroupList holds all the group details
	    List<UserGroup> allGroupList = jdbcTemplate_usermodule.query(
	        allGroupQuery,
	        new BeanPropertyRowMapper<>(UserGroup.class),
	        tenantId
	    );
	    
	    //allDeptList holds all the departments
	    List<UserDepartment> allDeptList = jdbcTemplate_usermodule.query(
	        allDepartmentQuery,
	        new BeanPropertyRowMapper<>(UserDepartment.class),
	        tenantId
	    );
	    
	  
	    //This is the query responsible for fetching all the page details based on tenantId which is defined in the schema of the respective database
	    String allPagesQuery = "SELECT DISTINCT [Page] as page,"
	    		+ "[Source] as source,"
	    		+ "[PageUrl] as pageUrl,"
	    		+ "[PageLogo] as pageLogo,"
	    		+ "[AliasName] as aliasName,"
	    		+ "[sequence] as sequence"
	    		+ " FROM [dbo].[Pages] ";
	    
	    //allPagesDetails holds the all the page details
	    List<Page> allPagesDetails = jdbcTemplate_usermodule.query(
	        allPagesQuery,
	        new BeanPropertyRowMapper<>(Page.class)
//	        ,tenantId
	    );

	    //  A Map of column names and its respective value for each user is stored in the form of map
	    List<Map<String, Object>> rowMapList = jdbcTemplate_usermodule.queryForList(
	        selectUsersQuery,
	        tenantId, tenantId, tenantId, tenantId
	    );

	    List<User> users = new ArrayList<>();

	    // Processing each user achieved through rowMapList
	    rowMapList.forEach(row -> {
	        User user = new User();

	        user.setUserID(userModuleHelper.getString(row, "UserId"));
	        user.setUserName(userModuleHelper.getString(row, "UserName"));
	        user.setPassword(userModuleHelper.getString(row, "Password"));
	        //getNullableString returns null in case of null value else sets the value as String if the value is not null
	        user.setPhone(userModuleHelper.getNullableString(row, "PhoneNumber"));
	        user.setG360HomePage(userModuleHelper.getNullableString(row, "G360Homepage"));
	        user.setWorkFlowHomePage(userModuleHelper.getNullableString(row, "WorkflowHomepage"));

	        user.setG360Admin(userModuleHelper.getBoolean(row, "G360Admin"));
	        user.setActive(userModuleHelper.getBoolean(row, "Active"));
	        user.setCreatedOn(userModuleHelper.getNullableString(row, "CreatedOn"));
	        user.setModifiedOn(userModuleHelper.getNullableString(row, "ModifiedOn"));
	        user.setCreatedBy(userModuleHelper.getNullableString(row, "CreatedBy"));
	        user.setModifiedBy(userModuleHelper.getNullableString(row, "ModifiedBy"));

	        UserDepartment foundDept = allDeptList.stream()
	            .filter(dept -> dept.getDeptID().equals(userModuleHelper.getString(row, "Department")))
	            .findFirst()
	            .orElse(null); // based on found department from the allDeptList the department is set , otherwise it is set to null

	        user.setDepartment(foundDept);
	        
	        // a comma separated groupIds is achieved here it is either set to null if null else splitToList returns List<String>
	        List<String> groupIds = userModuleHelper.splitToList(userModuleHelper.getNullableString(row, "groupDetails"));
	        List<UserGroup> matchingGroups = allGroupList.stream()
	            .filter(group -> groupIds.contains(group.getGroupID()))
	            .collect(Collectors.toList());
	        //based on the matching groups from the above groupIds List , a filter is run and based on matching groupIds it is assigned to the matching group List 
	        user.setGroup(matchingGroups);

	        
	        // It also fetches the list of G360 pages that particular user belongs to i.e  in case of alloted pages in G360 and store it in 'green360Pages'
	        List<String> green360Pages = userModuleHelper.splitToList(userModuleHelper.getNullableString(row, "allotedPages_g360"));
	        //Based on 'green360Pages' the matching G360 pages with its respective page ( pagename ) and its source is also checked here in this case G360
	        //and it is added to matching360Pages
	        List<Page> matching360Pages = allPagesDetails.stream()
	            .filter(page -> green360Pages.contains(page.getPage()) && "G360".equalsIgnoreCase(page.getSource()))
	            .collect(Collectors.toList());
	        // finally the user's g360AllottedPageList is set
	        user.setG360AllottedPageList(matching360Pages);
	        
	        // It also fetches the list of workflow pages that particular user belongs  i.e in case of alloted pages in Workflow and store it in 'workflowPages'
	        List<String> workflowPages = userModuleHelper.splitToList(userModuleHelper.getNullableString(row, "allotedPages_workflow"));
	      //Based on 'workflowPages' the matching workflow pages with its respective page ( pagename ) and its source is also checked here in this case Workflow
	        //and it is added to matchingWorkFlowList
	        List<Page> matchingWorkFlowList = allPagesDetails.stream()
	            .filter(page -> workflowPages.contains(page.getPage()) && "Workflow".equalsIgnoreCase(page.getSource()))
	            .collect(Collectors.toList());
	        // finally the user's workflowAllottedPageList is set
	        user.setWorkflowAllottedPageList(matchingWorkFlowList);

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
	     //The true value contains the workflowPagesList while the false contains the g360PagesList in partitionedPages
	     List<Page> workflowPagesList = partitionedPages.getOrDefault(true, new ArrayList<>());
	     List<Page> g360PagesList = partitionedPages.getOrDefault(false, new ArrayList<>());

	     // Set the lists in the user object
	     user.setAllWorkflowPageList(workflowPagesList);
	     user.setAllG360PageList(g360PagesList);
	     
	     //Both workflowPagesList and g360PagesList are added to  allotedPages which is summation of both 
	     List<Page> allotedPages = Stream.concat(matchingWorkFlowList.stream(), matching360Pages.stream())
                 .collect(Collectors.toList());
	     user.setAllotedPages(allotedPages);

	        	

	        users.add(user);
	    });

	    return users;
	}

	
	//this method gives all group details for a tenant
	@Override
	public List<UserGroup> getAllGroups(String tenantId) {
		// TODO Auto-generated method stub
		
//		JdbcTemplate jdbcTemplate_usermodule = dynamicDatasourceDAO.getDynamicConnection(tenantId).get(op360_usermodule_key);
//		JdbcTemplate jdbcTemplate_usermodule=jdbcTemplates.get(tenantId).get(1);
		
		JdbcTemplate jdbcTemplate_usermodule=jdbcTemplates.get(tenantId).get(1);
		// This is the query for fetching all groups related to the tenant
		 String allGroupQuery = 
			        "SELECT [GroupId] AS groupID, " +
			        "       [GroupName] AS groupName, " +
			        "       [PhoneNumber] AS phone, " +
			        "       [HomePage] AS homePage, " +
			        "       [Active] AS active, " +
			        "       [TenantId] AS tenantId " +
			        "FROM [dbo].[GroupCredentials] WHERE TenantId = ?";
		// Execute the query to fetch all user groups associated with the given tenantId
		// Use jdbcTemplate_usermodule to execute the query defined by 'allGroupQuery'
		// BeanPropertyRowMapper is used to map the result set to a list of UserGroup objects
		List<UserGroup> allGroupList = jdbcTemplate_usermodule.query(
		    allGroupQuery,                        // SQL query to fetch user groups
		    new BeanPropertyRowMapper<>(UserGroup.class), // Automatically map result set to UserGroup objects
		    tenantId                              // Parameter for the query, identifying the tenant
		);

		 return allGroupList;

	}
	
	//This method gives all department details for a tenant 
	@Override
	public List<UserDepartment> getAllDepartments(String tenantId) {
		// TODO Auto-generated method stub
		// Query for fetching all departments related to a tenant
	    String allDepartmentQuery = 
		        "SELECT [DeptId] AS deptID, " +
		        "       [DeptName] AS deptName, " +
		        "       [TenantId] AS tenantId " +
		        "FROM [dbo].[DeptCredentials] WHERE TenantId = ?";
//		JdbcTemplate jdbcTemplate_usermodule = dynamicDatasourceDAO.getDynamicConnection(tenantId).get(op360_usermodule_key);
	    JdbcTemplate jdbcTemplate_usermodule=jdbcTemplates.get(tenantId).get(1);
		return jdbcTemplate_usermodule.query(allDepartmentQuery,  new BeanPropertyRowMapper<>(UserDepartment.class),tenantId);
	}
	
	//This method gives all page details for a tenant
	@Override
	public List<Page> getAllPageDetails(String tenantId) {
		// TODO Auto-generated method stub
//		Query for fetching all page details with respect to a tenant
	    String allPagesQuery = "SELECT DISTINCT [Page] as page,"
	    		+ "[Source] as source,"
	    		+ "[PageUrl] as pageUrl,"
	    		+ "[PageLogo] as pageLogo,"
	    		+ "[AliasName] as aliasName,"
	    		+ "[sequence] as sequence"
//	    		+ "[TenantId] as tenantId"
	    		+ " FROM [dbo].[Pages] ";

//		JdbcTemplate jdbcTemplate_usermodule = dynamicDatasourceDAO.getDynamicConnection(tenantId).get(op360_usermodule_key);
	    JdbcTemplate jdbcTemplate_usermodule=jdbcTemplates.get(tenantId).get(1);
	    List<Page> allPagesDetails = jdbcTemplate_usermodule.query(
	        allPagesQuery,
	        new BeanPropertyRowMapper<>(Page.class)
//	        ,tenantId
	    );
	    return allPagesDetails;
	}


// This method provides a map for different sources based on tenantId here in this case Workflow,G360
	@Override
	public Map<String,List<Page>> getPagesMap(String tenantId) {
		// TODO Auto-generated method stub
		
		//Query to fetch all pages query with respect to tenant
		 String allPagesQuery = "SELECT DISTINCT [Page] as page,"
		    		+ "[Source] as source,"
		    		+ "[PageUrl] as pageUrl,"
		    		+ "[PageLogo] as pageLogo,"
		    		+ "[AliasName] as aliasName,"
		    		+ "[sequence] as sequence"
//		    		+ "[TenantId] as tenantId"
		    		+ " FROM [dbo].[Pages] ";

//			JdbcTemplate jdbcTemplate_usermodule = dynamicDatasourceDAO.getDynamicConnection(tenantId).get(op360_usermodule_key);
		 JdbcTemplate jdbcTemplate_usermodule=jdbcTemplates.get(tenantId).get(1);
		    List<Page> allPagesDetails = jdbcTemplate_usermodule.query(
		        allPagesQuery,
		        new BeanPropertyRowMapper<>(Page.class)
//		        ,tenantId
		    );
		 // Partition the pages into two lists based on their source
	     Map<Boolean, List<Page>> partitionedPages = allPagesDetails.stream()
	         .collect(Collectors.partitioningBy(
	             page -> "Workflow".equalsIgnoreCase(page.getSource()), 
	             Collectors.toList()));

	     // Extract the lists
	     
	     List<Page> workflowPagesList = partitionedPages.getOrDefault(true, new ArrayList<>());
	     List<Page> g360PagesList = partitionedPages.getOrDefault(false, new ArrayList<>());

	     Map<String,List<Page>> pagesMap=new LinkedHashMap<>();
	     pagesMap.put("Op360", workflowPagesList);
	     pagesMap.put("G360", g360PagesList);
	     return pagesMap;
	}


 //  below method fetch details for a user based on userId and tenantId
	@Override
	public User getUserDetails(String tenantId,String userId) {
		// TODO Auto-generated method stub
//		JdbcTemplate jdbcTemplate_usermodule = dynamicDatasourceDAO.getDynamicConnection(tenantId).get(op360_usermodule_key);
		JdbcTemplate jdbcTemplate_usermodule=jdbcTemplates.get(tenantId).get(1);
//		 logger.info("User : "+userId);
		

	    // Query to select user details
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
	        "WHERE TenantId = ? and UserId=?";
//Query to fetch allGroups
	    String allGroupQuery = 
	        "SELECT [GroupId] AS groupID, " +
	        "       [GroupName] AS groupName, " +
	        "       [PhoneNumber] AS phone, " +
	        "       [HomePage] AS homePage, " +
	        "       [Active] AS active, " +
	        "       [TenantId] AS tenantId " +
	        "FROM [dbo].[GroupCredentials] WHERE TenantId = ?";
//Query to fetch all Departments
	    String allDepartmentQuery = 
	        "SELECT [DeptId] AS deptID, " +
	        "       [DeptName] AS deptName, " +
	        "       [TenantId] AS tenantId " +
	        "FROM [dbo].[DeptCredentials] WHERE TenantId = ?";
//fetch the required groupList
	    List<UserGroup> allGroupList = jdbcTemplate_usermodule.query(
	        allGroupQuery,
	        new BeanPropertyRowMapper<>(UserGroup.class),
	        tenantId
	    );
	    //fetch all department list
	    List<UserDepartment> allDeptList = jdbcTemplate_usermodule.query(
	        allDepartmentQuery,
	        new BeanPropertyRowMapper<>(UserDepartment.class),
	        tenantId
	    );
	    
	  
//Query to fetch all Page Details
	    String allPagesQuery = "SELECT DISTINCT [Page] as page,"
	    		+ "[Source] as source,"
	    		+ "[PageUrl] as pageUrl,"
	    		+ "[PageLogo] as pageLogo,"
	    		+ "[AliasName] as aliasName,"
	    		+ "[sequence] as sequence"
//	    		+ "[TenantId] as tenantId"
	    		+ " FROM [dbo].[Pages] ";
	    
	    //Fetch all pageDetails
	    List<Page> allPagesDetails = jdbcTemplate_usermodule.query(
	        allPagesQuery,
	        new BeanPropertyRowMapper<>(Page.class)
//	        ,tenantId
	    );

	    List<Map<String, Object>> rowMapList = jdbcTemplate_usermodule.queryForList(
	        selectUsersQuery,
	        tenantId, tenantId, tenantId, tenantId,userId
	    );

	    List<User> users = new ArrayList<>();

	    // Processing each user
	    rowMapList.forEach(row -> {
	        User user = new User();

	        user.setUserID(userModuleHelper.getString(row, "UserId"));
	        user.setUserName(userModuleHelper.getString(row, "UserName"));
	        user.setPassword(userModuleHelper.getString(row, "Password"));
	        //getNullableString returns null in case of null value else sets the value as String if the value is not null
	        user.setPhone(userModuleHelper.getNullableString(row, "PhoneNumber"));
	        user.setG360HomePage(userModuleHelper.getNullableString(row, "G360Homepage"));
	        user.setWorkFlowHomePage(userModuleHelper.getNullableString(row, "WorkflowHomepage"));

	        user.setG360Admin(userModuleHelper.getBoolean(row, "G360Admin"));
	        user.setActive(userModuleHelper.getBoolean(row, "Active"));
	        user.setCreatedOn(userModuleHelper.getNullableString(row, "CreatedOn"));
	        user.setModifiedOn(userModuleHelper.getNullableString(row, "ModifiedOn"));
	        user.setCreatedBy(userModuleHelper.getNullableString(row, "CreatedBy"));
	        user.setModifiedBy(userModuleHelper.getNullableString(row, "ModifiedBy"));

	        UserDepartment foundDept = allDeptList.stream()
	            .filter(dept -> dept.getDeptID().equals(userModuleHelper.getString(row, "Department")))
	            .findFirst()
	            .orElse(null); // based on found department from the allDeptList the department is set , otherwise it is set to null

	        user.setDepartment(foundDept);
	        
	        // a comma separated groupIds is achieved here it is either set to null if null else splitToList returns List<String>
	        List<String> groupIds = userModuleHelper.splitToList(userModuleHelper.getNullableString(row, "groupDetails"));
	        List<UserGroup> matchingGroups = allGroupList.stream()
	            .filter(group -> groupIds.contains(group.getGroupID()))
	            .collect(Collectors.toList());
	        //based on the matching groups from the above groupIds List , a filter is run and based on matching groupIds it is assigned to the matching group List 
	        user.setGroup(matchingGroups);

	        
	        // It also fetches the list of G360 pages that particular user belongs to i.e  in case of alloted pages in G360 and store it in 'green360Pages'
	        List<String> green360Pages = userModuleHelper.splitToList(userModuleHelper.getNullableString(row, "allotedPages_g360"));
	        //Based on 'green360Pages' the matching G360 pages with its respective page ( pagename ) and its source is also checked here in this case G360
	        //and it is added to matching360Pages
	        List<Page> matching360Pages = allPagesDetails.stream()
	            .filter(page -> green360Pages.contains(page.getPage()) && "G360".equalsIgnoreCase(page.getSource()))
	            .collect(Collectors.toList());
	        // finally the user's g360AllottedPageList is set
	        user.setG360AllottedPageList(matching360Pages);
	        
	        // It also fetches the list of workflow pages that particular user belongs  i.e in case of alloted pages in Workflow and store it in 'workflowPages'
	        List<String> workflowPages = userModuleHelper.splitToList(userModuleHelper.getNullableString(row, "allotedPages_workflow"));
	      //Based on 'workflowPages' the matching workflow pages with its respective page ( pagename ) and its source is also checked here in this case Workflow
	        //and it is added to matchingWorkFlowList
	        List<Page> matchingWorkFlowList = allPagesDetails.stream()
	            .filter(page -> workflowPages.contains(page.getPage()) && "Workflow".equalsIgnoreCase(page.getSource()))
	            .collect(Collectors.toList());
	        // finally the user's workflowAllottedPageList is set
	        user.setWorkflowAllottedPageList(matchingWorkFlowList);

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
	     //The true value contains the workflowPagesList while the false contains the g360PagesList in partitionedPages
	     List<Page> workflowPagesList = partitionedPages.getOrDefault(true, new ArrayList<>());
	     List<Page> g360PagesList = partitionedPages.getOrDefault(false, new ArrayList<>());

	     // Set the lists in the user object
	     user.setAllWorkflowPageList(workflowPagesList);
	     user.setAllG360PageList(g360PagesList);
	     
	     //Both workflowPagesList and g360PagesList are added to  allotedPages which is summation of both 
	     List<Page> allotedPages = Stream.concat(matchingWorkFlowList.stream(), matching360Pages.stream())
                 .collect(Collectors.toList());
	     user.setAllotedPages(allotedPages);

	        	

	        users.add(user);
	    });

//The first index contains the respective userDetails
//	    System.out.println("User List : "+users);
		return users.get(0);
	}


//Based on userId and tenantId the userlogs are fetched
	@Override
	public List<UserLog> getUserLogs(String tenantId, String userId) {
//	    JdbcTemplate jdbcTemplate_usermodule = dynamicDatasourceDAO.getDynamicConnection(tenantId).get(op360_usermodule_key);
		JdbcTemplate jdbcTemplate_usermodule=jdbcTemplates.get(tenantId).get(1);
	    String logQuery = "SELECT [transaction_id], [timestamp], [id], [data_from], [modified_by], [data], [reason] as remarks, [olddata] as userDetails, [newdata] as newUserDetails FROM [dbo].[data_logs] WHERE [id] = ? AND [TenantId] = ? ORDER BY [timestamp] DESC";

	    // Create an ObjectMapper instance for parsing JSON
	    ObjectMapper objectMapper = new ObjectMapper();

	    List<UserLog> logDetails = jdbcTemplate_usermodule.query(logQuery, (rs, rowNum) -> {
	        // Creating UserLog object
	        UserLog tempUser = new UserLog(
	            rs.getString("transaction_id"),
	            rs.getString("timestamp"),
	            rs.getString("id"),
	            rs.getString("data_from"),
	            rs.getString("modified_by"),
	            rs.getString("data"),
	            rs.getString("remarks")
	        );

	        
	        //Previous response was respect to userId, the new POJO created is userID , same in Dept class(deptID) and Group class(groupID)
	     // Parse the JSON fields into User objects which is stored as String in database
	        String oldDataJson = rs.getString("userDetails");
	        if (oldDataJson != null && !oldDataJson.isEmpty()) {
	            try {
	            	//the old user data is parsed using ObjectMapper class
	                User oldUserData = objectMapper.readValue(oldDataJson, User.class);
	                tempUser.setUserDetails(oldUserData);
	            } catch (JsonProcessingException e) {
	                // Handle JSON parsing error for oldDataJson
	                System.err.println("Error parsing old data JSON: " + e.getMessage());
//	                e.printStackTrace(); // Optional, for debugging purposes
	            }
	        }

	        String newDataJson = rs.getString("newUserDetails");
	        if (newDataJson != null && !newDataJson.isEmpty()) {
	            try {
	            	//the new user data is also parsed using ObjectMapper class
	                User newUserData = objectMapper.readValue(newDataJson, User.class);
	                tempUser.setNewUserDetails(newUserData);
	            } catch (JsonProcessingException e) {
	                // Handle JSON parsing error for newDataJson
	                System.err.println("Error parsing new data JSON: " + e.getMessage());
//	                e.printStackTrace(); // Optional, for debugging purposes
	            }
	        }

	        return tempUser;
	    }, new Object[]{userId, tenantId});

	    return logDetails;
	}

	
	// the method returns whether that group is associated with any  user , job , task ,groups , assets ,logbooks
	@Override
	public String checkGroupAssociation(String groupId,String groupName, String tenantId) {
//		 JdbcTemplate jdbcTemplate_usermodule = dynamicDatasourceDAO.getDynamicConnection(tenantId).get(op360_usermodule_key);
//		 JdbcTemplate jdbcTemplate_op360 = dynamicDatasourceDAO.getDynamicConnection(tenantId).get(op360_key);
		
		JdbcTemplate jdbcTemplate_usermodule=jdbcTemplates.get(tenantId).get(1);
//		JdbcTemplate jdbcTemplate_op360=jdbcTemplate;
		JdbcTemplate jdbcTemplate_op360=jdbcTemplates.get(tenantId).get(0);;
		String reason="Group already associated with ";
		if (groupId.equals(""))
			throw new IllegalArgumentException("group id is empty");

		/**
		 *  checks any association with users
		 */
		String userGroupQuery = "SELECT  [UserId], [UserRole], (SELECT userName FROM [dbo].[UserCredential] WHERE [UserId] = ur.[UserId]) AS UserName, [TenantId] FROM [dbo].[UserRole] ur WHERE [UserRole] = ? AND [TenantId] = ?;";
		/**
		 *  executes query
		 */
		List<Map<String, Object>> userGroups = jdbcTemplate_usermodule.queryForList(userGroupQuery, groupId, tenantId);

		/**
		 *  if result is not empty then returns true or else checks for other associations further
		 */
		if (!(userGroups.isEmpty())) {

			System.out.println("user roll");
			System.out.println(userGroups);
			
			return userGroups.size()==1 ? reason + "a user ["+userGroups.get(0).get("UserName").toString()+"] !":
										  reason + "multiple users !";
		}

		/**
		 *  checks any association with logbooks
		 */
		String logbookGroupQuery = "SELECT Top(1) [FormId] ,[FormName] ,[UserID] ,[SaveSQL] ,[TableSQL] ,[DeleteSQL] ,[CreationDate] ,[CreatedUser] ,[Department] ,[UserGroup] ,[DocumentID] ,[FormatID] ,[VersionNumber] ,[isActiveForm] ,[TenantId] FROM [dbo].[DigitalLogbookFormInfo] where UserGroup =  ? and TenantId = ? ";
		System.out.println(groupId);
		
		List<Map<String, Object>> logbookGroups = jdbcTemplate_op360.queryForList(logbookGroupQuery, groupId,
				tenantId);

		System.out.println("===========logbooks============" + logbookGroups);

		/**
		 *  checks if the group id exists by splitting comma seperated values
		 */
		if (!(logbookGroups.isEmpty())) {

			System.out.println("logbooks");

			Map<String, Object> logbook = logbookGroups.get(0);
			Object object = logbook.get("UserGroup");
			String formName=logbook.get("FormName").toString();
			if (object != null) {

				String[] groups = object.toString().split(",");

				for (String group : groups) {
					if (group.equals(groupId))
					{
						return reason +"logbook ["+formName+"] !";
					}
				}
			}

		}

		/**
		 *  checks any association with assets
		 */
		String assetGroupQuery = "SELECT top(1) * FROM [dbo].[AssetInfo] where groups like ?  and TenantId = ? ; ";

		List<Map<String, Object>> assetGroupMaps = jdbcTemplate_op360.queryForList(assetGroupQuery, "%" + groupName + "%",
				tenantId);//here the groupName is getting checked because the name of the group is getting stored 

		/**
		 *  checks if the group id exists by splitting comma seperated values
		 */
		if (!(assetGroupMaps.isEmpty())) {

			Map<String, Object> asset = assetGroupMaps.get(0);
			Object assetGroups = asset.get("groups");
			if (assetGroups != null) {

				String[] groups = assetGroups.toString().split(",");
				String assetName=asset.get("AssetName").toString();
				for (String group : groups) {

					if (group.equals(groupName))
						return reason +"Asset ["+assetName+"] !" ;
				}
			}

		}

		
		/**
		 *  checks any association with jobs
		 */
		String jobGroupQuery ="SELECT top 1 jad.[JobId], Jobname = (SELECT jd.Jobname FROM [dbo].[JobDetails] jd WHERE jd.JobId = jad.JobId), jad.[ActivityId], jad.[TaskId], jad.[LogbookName], jad.[Performer], jad.[Approver], jad.[PerformerType], jad.[GroupOrDeptName], jad.[AssetID], jad.[AssetName], jad.[isGroupOrDept], jad.[ScheduledStart], jad.[ScheduleStop], jad.[ActualStart], jad.[ActualStop], jad.[Status], jad.[ActivityReviewerIntimationTime], jad.[ActivityReviewercompletionTime], jad.[Remarks], jad.[FileName], jad.[FileContent], jad.[TenantId] FROM [dbo].[JobActivityDetails] jad"
				+ " where  jad.[GroupOrDeptName]=? and [TenantId]=?"; //Why not check status because for the job the activity might be in 'not started' state and in the dashboard, grouping by groups  is also there

		/**
		 *  executes the query
		 */
		List<Map<String, Object>> jobGroups = jdbcTemplate_op360.queryForList(jobGroupQuery, groupName, tenantId);

		/**
		 *  if the result is not empty implies at least one association so returns true
		 *  or else no association so returns false
		 */
		
	
		if (!(jobGroups.isEmpty()))
		{
			String jobName = assetGroupMaps.get(0).get("Jobname").toString();
			return reason+"Job ["+jobName+"] !";
		}

		System.out.println(userGroups);
		
		
		String taskQuery="SELECT  top 1 Groups,TaskName=(select d.taskname from [dbo].TaskDetails d where d.taskId=t.TaskId ) FROM [dbo].[ActivityCreation] t where t.Groups=? and t.[TenantId]=?";
		
		List<Map<String, Object>> taskGroups = jdbcTemplate_op360.queryForList( taskQuery,groupName,tenantId);
		if (!(taskGroups.isEmpty()))
		{
			String taskName = assetGroupMaps.get(0).get("TaskName").toString();
			return reason+"Task ["+taskName+"] !";
		}
		
		

		return "";

	}
	
// the method returns true or false whether a department is associated with any user , asset ,logbook ,job
	@Override
	public String checkDepartmentAssociation(String deptId,String tenantId) {
		// TODO Auto-generated method stub
//		 JdbcTemplate jdbcTemplate_usermodule = dynamicDatasourceDAO.getDynamicConnection(tenantId).get(op360_usermodule_key);
//		 JdbcTemplate jdbcTemplate_op360 = dynamicDatasourceDAO.getDynamicConnection(tenantId).get(op360_key);
		
		
		JdbcTemplate jdbcTemplate_usermodule=jdbcTemplates.get(tenantId).get(1);
		JdbcTemplate jdbcTemplate_op360=jdbcTemplates.get(tenantId).get(0);
		String reason="Department already associated with ";
		 
		 /**
			 *  checks any association with users
			 */
			String deptUserQuery = "SELECT [UserId] ,[UserName] FROM [dbo].[UserCredential] where Department = ? and TenantId = ? ";

			/**
			 * executes the query
			 */
			List<Map<String, Object>> userDepts = jdbcTemplate_usermodule.queryForList(deptUserQuery, deptId, tenantId);

			/**
			 * if the result is not empty means the department exists then returns true
			 */
			if (!userDepts.isEmpty())
			{
				return userDepts.size()==1?reason+" a user ["+userDepts.get(0).get("UserName").toString()+"] !":reason+"multiple users !";
			}

//			/**
//			 *  checks any association with assets
//			 */
//			String assetDepartmentQuery = "SELECT [AssetId] ,[AssetName] ,[department] ,[groups] FROM [dbo].[AssetInfo] where department = ? and TenantId = ? ";
//
//			/**
//			 * executes the query
//			 */
//			List<Map<String, Object>> assetDepts = jdbcTemplate_op360.queryForList(assetDepartmentQuery, deptId, tenantId);
//
//			/**
//			 * if the result is not empty means the department exists then returns true
//			 */
//			if (!assetDepts.isEmpty())
//				return true;

			/**
			 *  checks any association with logbook
			 */
			String logbookDeptQuery = "SELECT Top 1 [FormId] ,[FormName] ,[CreatedUser] ,[Department] FROM [dbo].[DigitalLogbookFormInfo] where Department = ? and TenantId = ? ";

			/**
			 * executes the query
			 */
			List<Map<String, Object>> logbookDepts = jdbcTemplate_op360.queryForList(logbookDeptQuery, deptId, tenantId);

			/**
			 * if the result is not empty means the department exists then returns true
			 */
			if (!logbookDepts.isEmpty())
			{
				return  reason + "logbook ["+logbookDepts.get(0).get("FormName").toString()+"] !";
			}

			/**
			 *  checks any association with jobs
//			 */
//			String jobDeptQuery = "SELECT [JobId] ,[ActivityId] ,[TaskId] ,[LogbookName] ,[Performer] ,[Approver] ,[PerformerType] FROM [dbo].[JobActivityDetails] where PerformerType = ? and TenantId = ? ";
//
//			/**
//			 * executes the query
//			 */
//			List<Map<String, Object>> jobDepts = jdbcTemplate_op360.queryForList(jobDeptQuery, deptId, tenantId);
//
//			/**
//			 * if the result is not empty means the department exists then returns true
//			 */
//			if (!jobDepts.isEmpty())
//				return true;
	
		return "";
	}


// the method replaces or save user details based on whether that user is new or old 
	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean saveOrUpdateUser(User user) {

		System.out.println("Current User Phone  : "+user.getPhone());

		JdbcTemplate jdbcTemplate_usermodule=jdbcTemplates.get(user.getTenantId()).get(1);
//		JdbcTemplate jdbcTemplate_op360=jdbcTemplate;
	    System.out.println("User Modified On : " + user.getModifiedOn());
	    System.out.println("User Department : "+user.getDepartment());
//	    TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
//	    System.out.println("Transaction Status : "+status.toString());
		try {

	    
	    String allUsersQuery = "SELECT [UserId] FROM [dbo].[UserCredential] WHERE TenantId = ?";
	    List<String> existingUsers = jdbcTemplate_usermodule.queryForList(allUsersQuery, String.class, user.getTenantId());

	    System.out.println("Existing Users : " + existingUsers);
	    User oldUserData = new User();
	    if(existingUsers.contains(user.getUserID()))
	    {
	    	oldUserData= getUserDetails(user.getTenantId(), user.getUserID());
	    	System.out.println("Fetched Old User Data : "+oldUserData);
	    }


	    // Hash the password
	    String password = existingUsers.contains(user.getUserID()) ? user.getPassword() : userModuleHelper.generateHash(UserModuleHelper.SALT + "user_123");
	    String createdOn = existingUsers.contains(user.getUserID()) ?"'" + user.getCreatedOn() + "'" : "getDate()";
//	    String createdOnValue = existingUsers.contains(user.getUserID()) ? user.getCreatedOn() : null;


	    // Delete queries for User credentials, group mappings, and page mappings
	    String deleteUserCredQuery = "Delete from [dbo].UserCredential where UserId = ? and TenantId = ? ";
	    String deleteUserGroupQuery = "Delete from [dbo].UserRole where UserId = ? and TenantId = ? ";
	    String deletePagesQuery = "Delete from [dbo].UserPages where UserId = ? and TenantId = ? ";

	    Object[] delArgs = { user.getUserID(), user.getTenantId() };

	    jdbcTemplate_usermodule.update(deleteUserCredQuery, delArgs);
	    jdbcTemplate_usermodule.update(deleteUserGroupQuery, delArgs);
	    jdbcTemplate_usermodule.update(deletePagesQuery, delArgs);
	    

	    // Insert query for User credential
	 // Insert query for User credential+
	    String insertUserQuery = "insert into [dbo].UserCredential ( [UserId], [UserName], [Password], [PhoneNumber], [G360Homepage], "
	            + "[WorkflowHomepage], [G360Admin], [Active], [CreatedOn], [ModifiedOn], [CreatedBy], [ModifiedBy], "
	            + "[Department], [FirstLoginRequired], [PasswordExpiryDuraton], [PasswordExpiryDate], [LastLogin], [TenantId]) "
	            + "values(?,?,?,?,?,?,?,?,"
	            + createdOn
	            + ",getDate(),?,?,?,?,?,?,?,?);";

	    // Prepare the user details arguments
	    Object[] userDetailsArgs = {
	        user.getUserID(), user.getUserName(), password,
	        user.getPhone()==null?null:user.getPhone().length()==0?null:user.getPhone()
	        		, user.getG360HomePage(),
	        user.getWorkFlowHomePage(), user.isG360Admin()
	        , user.isActive()
//	        ,user.getModifiedOn()
	        , user.getCreatedBy(), user.getModifiedBy(),
	        user.getDepartment().getDeptID(), user.isFirstLoginRequired(),
	        user.getPasswordExpiryDuration(), user.getPasswordExpiryDate(),
	        user.getLastLogIn(), user.getTenantId()
	    };


	    int userAdded = jdbcTemplate_usermodule.update(insertUserQuery, userDetailsArgs);

	    if (userAdded > 0) {
	    	//based on the user added the respective associated groups with the user is also added
	    	String insertGroups = "INSERT INTO [dbo].[UserRole] (UserId, UserRole, TenantId) VALUES (?, ?, ?)";

	    	// Prepare the batch arguments
	    	List<Object[]> groupArgs = user.getGroup().stream()
	    	    .map(group -> new Object[] { user.getUserID(), group.getGroupID(), user.getTenantId() })
	    	    .collect(Collectors.toList());

	    	// Execute batch update
	    	int[] updateCounts = jdbcTemplate_usermodule.batchUpdate(insertGroups, groupArgs);
	    	
	    	String insertPages = "INSERT INTO [dbo].UserPages (UserId, AllotedPage, Source, TenantId) VALUES (?, ?, ?, ?)";

	    	// Prepare the batch arguments
	    	List<Object[]> pageArgs = user.getAllotedPages().stream()
	    	    .map(page -> new Object[] { user.getUserID(), page.getPage(), page.getSource(), user.getTenantId() })
	    	    .collect(Collectors.toList());

	    	// Execute batch update
	    	int[] pageUpdateCounts = jdbcTemplate_usermodule.batchUpdate(insertPages, pageArgs);

	    	//  Check results
	    	System.out.println("Batch update results for pages: " + Arrays.toString(pageUpdateCounts));

	    	
	        // Log query
	        String insertQueryToLog = "INSERT INTO [dbo].[data_logs] ([transaction_id], [timestamp], [id], [data_from], [modified_by], "
	                                + "[data], [reason], [olddata], [newdata], [TenantId]) VALUES (?, GETDATE(), ?, ?, ?, ?, ?, ?, ?, ?);";

	        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
	        String transactionId = 'T' + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());

	        try {
	            if (existingUsers.contains(user.getUserID())) {
	                System.out.println("Transaction will be saved for existing User : " + user.getUserID());
	                
	                String oldDataInJson = ow.writeValueAsString(oldUserData);
//	                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	                user.setModifiedOn(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
	                String newDataInJson = ow.writeValueAsString(user);
	                String finalChangedReasons = userModuleHelper.getChangeFieldDifferenceString(user,oldUserData);
	                System.out.println("Final Changed Reasons : "+finalChangedReasons);
	                if(finalChangedReasons.length()!=0)
	                {

	                	Object[] transactionArgs = {
	                			transactionId, user.getUserID(), "UserCredentials", user.getModifiedBy(), finalChangedReasons, "",
	                			oldDataInJson, newDataInJson, user.getTenantId()
	                	};
	                	jdbcTemplate_usermodule.update(insertQueryToLog, transactionArgs);
	                }
	                else
	                {
	                	System.out.println("No Changes found to save in transaction");//This is the event the user is saving the details without any change
	                }


	            } else {
	                System.out.println("New User is getting created with Id : " + user.getUserID());

	                String oldDataInJson = ow.writeValueAsString(new User());
	                String newDataInJson = ow.writeValueAsString(user);

	                Object[] transactionArgs = {
	                    transactionId, user.getUserID(), "UserCredentials", user.getCreatedBy(),
	                    "New user created having id: " + user.getUserID(), "", oldDataInJson, newDataInJson, user.getTenantId()
	                };
	                jdbcTemplate_usermodule.update(insertQueryToLog, transactionArgs);
	            }
	        } catch (JsonProcessingException e) {
	            e.printStackTrace(); // Handle exception
	            throw new RuntimeException("Exception occured while parsing the Json");
	        }
//	        conn.commit();
	        return true;
	    }
	    } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException("Exception occurred !");
//			 transactionManager.rollback(status);
//			return false;
		}
		
		return true;

	}



//This method saves a new group
	@Override
	public boolean saveNewGroup(UserGroup group) {
		// TODO Auto-generated method stub
//		 JdbcTemplate jdbcTemplate_usermodule = dynamicDatasourceDAO.getDynamicConnection(group.getTenantId())
//				  .get(op360_usermodule_key);
		

		JdbcTemplate jdbcTemplate_usermodule=jdbcTemplates.get(group.getTenantId()).get(1);
		JdbcTemplate jdbcTemplate_op360=jdbcTemplates.get(group.getTenantId()).get(0);
		//Query for inserting a new group 
		 String insertGroupsQuery="INSERT INTO [dbo].[GroupCredentials]"
		 		+ "           ([GroupId]"
		 		+ "           ,[GroupName]"
		 		+ "           ,[PhoneNumber]"
		 		+ "           ,[HomePage]"
		 		+ "           ,[Active]"
		 		+ "           ,[TenantId])"
		 		+ "            values(?,?,?,?,?,?)";
		 //adding the required arguments
		 Object[] args= {group.getGroupID(),group.getGroupName(),group.getPhone(),group.getHomePage(),group.isActive(),group.getTenantId()};
		int updateCount=jdbcTemplate_usermodule.update(insertGroupsQuery,args);
		if(updateCount>0)
			return true;
		return false;
	}


//this method saves a new department 
	@Override
	public boolean saveNewDepartment(UserDepartment department) {
		// TODO Auto-generated method stub
//		JdbcTemplate jdbcTemplate_usermodule = dynamicDatasourceDAO.getDynamicConnection(department.getTenantId())
//				  .get(op360_usermodule_key);
		

		JdbcTemplate jdbcTemplate_usermodule=jdbcTemplates.get(department.getTenantId()).get(1);
		JdbcTemplate jdbcTemplate_op360=jdbcTemplates.get(department.getTenantId()).get(0);
		//Query for inserting a new department
		String insertQueryDept="INSERT INTO [dbo].[DeptCredentials]"
				+ "           ([DeptId]"
				+ "           ,[DeptName]"
				+ "           ,[TenantId])"
				+ "     VALUES(?,?,?)";
		Object[] args= {department.getDeptID(),department.getDeptName(),department.getTenantId()};
		int updateCount=jdbcTemplate_usermodule.update(insertQueryDept,args);
		if(updateCount>0)
			return true;
		return false;
	}


//this method associates a list of users with a group along with groupId and tenantId schema i.e during merging groups
	@Override
	public boolean saveMergedGroups(List<String> userList, String groupId, String tenantId) {
		// TODO Auto-generated method stub
//		JdbcTemplate jdbcTemplate_usermodule = dynamicDatasourceDAO.getDynamicConnection(tenantId).get(op360_usermodule_key);

		JdbcTemplate jdbcTemplate_usermodule=jdbcTemplates.get(tenantId).get(1);
//		JdbcTemplate jdbcTemplate_op360=jdbcTemplates.get(tenantId).get(0);
		//Query for inserting the groupids along with the users 
//		String deleteQuery="delete from [dbo].[UserRole] where [UserId] in(?) and [UserRole]=?;";
//		
//		String currentUsers=String.join(",", userList);
//		
//		int rowsAffected=jdbcTemplate_usermodule.update(deleteQuery,currentUsers);
		
		String query = "Insert into [dbo].[UserRole] values(?,?,?);";

		// Iterating over the userList to create an object array for each user
		List<Object[]> args = userList.stream().map(userId -> {
		    // Creating an Object array 'arg' containing userId, groupId, and tenantId
		    Object[] arg = { userId, groupId, tenantId };
		    return arg; // Returning the created object array
		}).collect(Collectors.toList()); // Collecting all the object arrays into a list and storing it in 'args' List


		//Finally firing the batchUpdate query
		int[] batchUpdate = jdbcTemplate_usermodule.batchUpdate(query, args);
		System.out.println("Batch Update : "+batchUpdate);
		if(batchUpdate.length>0)
			return true;
		return false;
	}


//To update a user password and returns a boolean value whether the user password is updated or not
	@Override
	public boolean changePassword(User user) {
		// TODO Auto-generated method stub

		JdbcTemplate jdbcTemplate_usermodule=jdbcTemplates.get(user.getTenantId()).get(1);
		//Query for updating the user password 
		String query = "Update [dbo].UserCredential set Password = ? , ModifiedOn = getDate(),[ModifiedBy]=? ,[FirstLoginRequired]=0 ,[PasswordExpiryDate]=convert(date,dateAdd(day,?,getDate())) where UserId = ? and TenantId = ?;";
		//generating the hashed password with the SALT defined in userModuleHelper class
		String hashedPassword= userModuleHelper.generateHash(UserModuleHelper.SALT + user.getPassword());
		//we are injecting the data in args array with the required parameters
		Object args[]= {hashedPassword,user.getModifiedBy() ,user.getPasswordExpiryDuration(),user.getUserID(),user.getTenantId()};
		User oldUserData=getUserDetails(user.getTenantId(),user.getUserID());
		//creating an object of ObjectWriter for storing the data in json format in database
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
//		String oldDataInJson = ow.writeValueAsString(oldUserData); 
		int updateCount=0;
		try {
//			String newDataInJson = ow.writeValueAsString(user);
//			String oldDataInJson = ow.writeValueAsString(oldUserData); 
			updateCount=jdbcTemplate_usermodule.update(query,args);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
//		int updateCount=jdbcTemplate_usermodule.update(query,args);
		if(updateCount>0)
		{
			//Now this transaction will be saved
			try {
			//Updating the time when the password got changed
			user.setModifiedOn(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
			//the newData is stored in JSON format as a String
			String newDataInJson = ow.writeValueAsString(user);
			//the oldData is stored in JSON format as a String
			String oldDataInJson = ow.writeValueAsString(oldUserData); 
			
			// A unique transactionId is generated every time
			String transactionId = 'T'+new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
			//Query to insert data in data_logs
			String insertQueryToLog = "INSERT INTO [dbo].[data_logs] ([transaction_id] ,[timestamp] ,[id] ,[data_from] ,[modified_by] ,[data] ,[reason] ,[olddata] ,[newdata],[TenantId]) VALUES (? ,GETDATE() ,? ,? ,? ,? ,? , ? , ?,?);";
		    
			//the parameters to insert in the data_logs table  based on the tenant Schema
			Object[] transactionArgs= {transactionId,
									user.getUserID(),
									"UserCredentials",
									user.getModifiedBy(),
									"Password updated by: " + user.getModifiedBy(),
									"",
									oldDataInJson,
									newDataInJson,
									user.getTenantId()};
			//Query is fired here
	    	int transactionCount=jdbcTemplate_usermodule.update(insertQueryToLog,transactionArgs);
			
	    	//if the transactionCount is greater than 0 then the data is stored in the database
			if(transactionCount>0)
				return true;
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
			return false;
		}
		return false;
		
	}


// this method updates a department and returns a flag whether that department is updated or not
	@Override
	public boolean updateDepartment(UserDepartment department) {
		// TODO Auto-generated method stub
//		JdbcTemplate jdbcTemplate_usermodule = dynamicDatasourceDAO.getDynamicConnection(department.getTenantId())

//		JdbcTemplate jdbcTemplate_usermodule=jdbcTemplates.get(tenantId).get(1);
//		JdbcTemplate jdbcTemplate_op360=jdbcTemplate;
//				  .get(op360_usermodule_key);

		JdbcTemplate jdbcTemplate_usermodule=jdbcTemplates.get(department.getTenantId()).get(1);
		JdbcTemplate jdbcTemplate_op360=jdbcTemplates.get(department.getTenantId()).get(0);;
		//Query to update department
		String updateDepartmentQuery = "UPDATE [dbo].[DeptCredentials] "
                + "SET  "
                + "    [DeptName] = ?, "
                + "    [TenantId] = ? "
                + "WHERE [DeptId] = ? AND [TenantId] = ?;";
		
		//the args array consists of the required parameters to insert into the  DeptCredentials table  based on the tenant Schema
		Object[] args= {department.getDeptName(),department.getTenantId(),department.getDeptID(),department.getTenantId()};
		int updateCount=jdbcTemplate_usermodule.update(updateDepartmentQuery,args);
		if(updateCount>0)
			return true;
		return false;

	}


//This method updates a group and returns a boolean value whether that group is updated or not
	@Override
	public boolean updateGroup(UserGroup group) {
		// TODO Auto-generated method stub
//		JdbcTemplate jdbcTemplate_usermodule = dynamicDatasourceDAO.getDynamicConnection(group.getTenantId())
//				  .get(op360_usermodule_key);

		JdbcTemplate jdbcTemplate_usermodule=jdbcTemplates.get(group.getTenantId()).get(1);
		JdbcTemplate jdbcTemplate_op360=jdbcTemplates.get(group.getTenantId()).get(0);
		//Query to update group
		String updateGroupQuery="UPDATE [dbo].[GroupCredentials]"
				+ "   SET "
				+ "       [GroupName] = ?,"
				+ "       [PhoneNumber] = ?,"
				+ "       [HomePage] = ?,"
				+ "       [Active] = ?,"
				+ "       [TenantId] = ?"
				+ " WHERE [GroupId] = ? AND TenantId=?;"
				+ "";
		//the args array consists of the required parameters to insert into the  GroupCredentials table  based on the tenant Schema
		 Object[] args= {group.getGroupName(),group.getPhone(),group.getHomePage(),group.isActive(),group.getTenantId(),group.getGroupID(),group.getTenantId()};
		 int updateCount=jdbcTemplate_usermodule.update(updateGroupQuery,args);
			if(updateCount>0)
				return true;
			return false;
		
	}


//This method deletes a particular group and returns a boolean value based on whether the group is deleted or not
	@Override
	public boolean deleteGroup(UserGroup group) {
		// TODO Auto-generated method stub
//		JdbcTemplate jdbcTemplate_usermodule = dynamicDatasourceDAO.getDynamicConnection(group.getTenantId())
//				  .get(op360_usermodule_key);

		JdbcTemplate jdbcTemplate_usermodule=jdbcTemplates.get(group.getTenantId()).get(1);
//		JdbcTemplate jdbcTemplate_op360=jdbcTemplates.get(group.getTenantId()).get(0);;
		//Query to delete a particular group based on the tenant Schema
		String query= "delete from [dbo].[GroupCredentials] where  [GroupId]=? and [TenantId]=?";
		
		//The query is fired here
		int deleteCount=jdbcTemplate_usermodule.update(query,group.getGroupID(),group.getTenantId());
		if(deleteCount>0)
			return true;
		
		return false;
	}

//This method deletes particular department and returns a boolean value based on whether the department was deleted or not
	@Override
	public boolean deleteDepartment(UserDepartment dept) {
		// TODO Auto-generated method stub
//		JdbcTemplate jdbcTemplate_usermodule = dynamicDatasourceDAO.getDynamicConnection(dept.getTenantId())
//				  .get(op360_usermodule_key);

		JdbcTemplate jdbcTemplate_usermodule=jdbcTemplates.get(dept.getTenantId()).get(1);
		JdbcTemplate jdbcTemplate_op360=jdbcTemplates.get(dept.getTenantId()).get(0);;
		// Query to delete a particular department
		String query= "delete from 	[dbo].[DeptCredentials] where  [DeptId]=? and [TenantId]=?";
		int deleteCount=jdbcTemplate_usermodule.update(query,dept.getDeptID(),dept.getTenantId());
		if(deleteCount>0)
			return true;
		
		return false;
		
	}


// to update Login time and returns true if login time is updated else false
	@Override
	public boolean updateLoginTime(String userID,String tenantId) {
		// TODO Auto-generated method stub
//		JdbcTemplate jdbcTemplate_usermodule = dynamicDatasourceDAO.getDynamicConnection(tenantId)

//		JdbcTemplate jdbcTemplate_usermodule=jdbcTemplates.get(tenantId).get(1);
//		JdbcTemplate jdbcTemplate_op360=jdbcTemplate;
//				  .get(op360_usermodule_key);

		JdbcTemplate jdbcTemplate_usermodule=jdbcTemplates.get(tenantId).get(1);
		JdbcTemplate jdbcTemplate_op360=jdbcTemplates.get(tenantId).get(0);;
		//Query to upadate the login time based on userId and tenantId
		String query= "update [dbo].[UserCredential] set LastLogin=getDate() where  [UserId]=? and [TenantId]=? ";
		int lastLoginUpdated=jdbcTemplate_usermodule.update(query,userID,tenantId);
		if(lastLoginUpdated>0)
			return true;
		
		return false;
	}
	
//This returns a true or false flag based on the previous password has matched with repect to userId and tenantID
	@Override
	public boolean checkOldPassword(String userId,String oldPassword,String tenantId) {
		// TODO Auto-generated method stub
//		JdbcTemplate jdbcTemplate_usermodule = dynamicDatasourceDAO.getDynamicConnection(tenantId)
//				  .get(op360_usermodule_key);

		JdbcTemplate jdbcTemplate_usermodule=jdbcTemplates.get(tenantId).get(1);
		JdbcTemplate jdbcTemplate_op360=jdbcTemplates.get(tenantId).get(0);
		// first the hashed password is accessed from the database and compared with the generated hash from the oldPassword
		String pswdQuery="Select [Password] from [dbo].[UserCredential] where [UserId]=? and [TenantId]=?";
		List<String> savedHash=jdbcTemplate_usermodule.queryForList(pswdQuery, String.class,userId,tenantId);
		String generatedHash=userModuleHelper.generateHash(UserModuleHelper.SALT + oldPassword);
		//the generated hash and stored has is now compared to finally return true or false
		if(generatedHash.equals(savedHash.get(0)))
			return true;
		return false;
	}


	 @Override
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
//		        	String url=	String.format("jdbc:sqlserver://%s;encrypt=true;trustServerCertificate=true;databaseName=%s",
//		                tenantDetail.getDbIp(), dbName.trim());
				HikariConfig config = new HikariConfig();
				config.setJdbcUrl(url);
				config.setUsername(tenantDetail.getDbUser());
	            config.setPassword(tenantDetail.getDbPassword());
	            config.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
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


	@Override
	public Map<String, List<String>> getAllUserIdsByGroup(String tenantId) {

	    String sql = "SELECT t1.[UserId],[UserRole],[Groupname] FROM [dbo].[UserRole] t1 inner join dbo.UserCredential t2 on t1.userId = t2.userid INNER JOIN GroupCredentials ON UserRole=GroupId where t2.Active=1";
	    JdbcTemplate jdbcTemplate_usermodule = jdbcTemplates.get(tenantId).get(1);

	    return jdbcTemplate_usermodule.query(sql, rs -> {
	        Map<String, List<String>> result = new HashMap<>();

	        while (rs.next()) {
	            String userId = rs.getString("UserId");
	            String role = rs.getString("GroupName");

	            result.computeIfAbsent(role, k -> new ArrayList<>()).add(userId);
	        }

	        return result;
	    });
	}








	

	

}
