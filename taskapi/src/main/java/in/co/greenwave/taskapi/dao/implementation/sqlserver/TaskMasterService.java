package in.co.greenwave.taskapi.dao.implementation.sqlserver;

// Import necessary classes for SQL operations and data handling
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import in.co.greenwave.taskapi.model.TenantConfigDetails;
import in.co.greenwave.taskapi.utility.JdbcUrlUtil;
// Import DAO interface and model classes
import in.co.greenwave.taskapi.dao.TaskMasterDAO;
import in.co.greenwave.taskapi.model.ActivityConnection;
import in.co.greenwave.taskapi.model.ActivityDetails;
import in.co.greenwave.taskapi.model.TaskDetail;

// Mark this class as a Spring Repository, indicating it's a DAO component
@Repository
public class TaskMasterService implements TaskMasterDAO {

    // Autowired JdbcTemplate instances for two different databases
//    @Autowired
//    @Qualifier("jdbcTemplate1") // Specify which JdbcTemplate to use for the first database
//    public JdbcTemplate jdbcTemplateOp360; // JdbcTemplate for the first database

//    @Autowired
//    @Qualifier("jdbcTemplate2") // Specify which JdbcTemplate to use for the second database
//    public JdbcTemplate jdbcTemplate2; // JdbcTemplate for the second database

    @Autowired
	@Qualifier("DatasourceCollections")
	private  Map<String, List<JdbcTemplate>> jdbcTemplates; // Template for operations on the main database
	
    
    @Autowired
	@Qualifier("jdbcTemplate_OP360_tenant")
	private  JdbcTemplate jdbcTemplateOp360Tenant;
    
    
	
	/**
 * Retrieves a list of TaskDetail objects for a specific creator and tenant.
 *
 * This method executes a SQL query to fetch task details from the TaskDetails table,
 * filtering by the provided creator and tenantId. It also retrieves associated activity
 * details from the ActivityCreation table for each task. Tasks with statuses 'Not Ready'
 * and 'Rejected' are excluded from the result. The method returns a list of TaskDetail
 * objects containing all relevant information.
 *
 * @param creator The creator of the tasks to be fetched.
 * @param tenantId The identifier of the tenant whose tasks are to be retrieved.
 * @return A list of TaskDetail objects containing task and associated activity details.
 */
@Override
public List<TaskDetail> getAllTasksbyTenant(String creator, String tenantId) {
    // Initialize an empty list to store TaskDetail objects
    List<TaskDetail> taskList = new ArrayList<>();
    JdbcTemplate jdbcTemplateOp360=jdbcTemplates.get(tenantId).get(0);
    // SQL query to retrieve task details for a specific creator and tenantId,
    // excluding tasks with statuses 'Not Ready' and 'Rejected'
    String sql = "SELECT [CreationTime], [TaskId], [TaskName], [Status], [Creator], [PublishTime], [PublishRejectUserId], " +
                 "[Remarks], [Description], [UserGroup], [UserDept], [AssetGroup], [adhocTask],[TenantId] " +
                 "FROM [dbo].[TaskDetails] " +
                 "WHERE Creator = ? AND TenantId = ? OR Status NOT IN ('Not Ready', 'Rejected')";

    // SQL query to retrieve activity details associated with a specific TaskId
    String activitySql = "SELECT [TransactionId], [CreationTime], [TaskId], [ActivityId], [Sequence], " +
                         "[ActivityName], [logbook], [Asset], [Duration_min], [ActivityAbrv], " +
                         "[buffer], [enforceStartDuration],[enforce], [AssetId], [Groups], [Department], [Pos_X], [Pos_Y], [TenantId] " +
                         "FROM [dbo].[ActivityCreation] WHERE TaskId = ? order by sequence";

    // Execute the SQL query to fetch task details
    jdbcTemplateOp360.query(sql, new RowCallbackHandler() {
        @Override
        public void processRow(ResultSet rs) throws SQLException {
            // Loop through the result set of task details
            do {
                // Create a new TaskDetail object for each task retrieved
                TaskDetail task = new TaskDetail();
                task.setCreationTime(rs.getString("CreationTime")); // Set task creation time
                task.setTaskId(rs.getString("TaskId")); // Set task ID
                task.setTaskName(rs.getString("TaskName")); // Set task name
                task.setStatus(rs.getString("Status")); // Set task status
                task.setCreator(rs.getString("Creator")); // Set task creator
                task.setRemarks(rs.getString("Remarks")); // Set task remarks
                task.setDescription(rs.getString("Description")); // Set task description
                task.setReviewer(rs.getString("PublishRejectUserId")); // Set reviewer ID for publish/reject
                task.setAssetGroup(rs.getString("AssetGroup")); // Set asset group associated with the task
                task.setadhocTask(rs.getBoolean("adhocTask"));

                // Initialize a list to hold ActivityDetails for this task
                List<ActivityDetails> activityList = new LinkedList<>();
                // Create a map to store maximum durations for activities based on sequence
                Map<Integer, Integer> maxDurations = new HashMap<>();

                // Execute the SQL query to fetch activities associated with the current task
                jdbcTemplateOp360.query(activitySql, new RowCallbackHandler() {
                    @Override
                    public void processRow(ResultSet actRs) throws SQLException {
                        // Loop through the result set of activity details
                        do {
                            // Initialize a list for asset IDs associated with the activity
                            LinkedList<String> assetIdList = new LinkedList<>();
                            String assetIds = actRs.getString("AssetId"); // Retrieve asset IDs as a string
                            // Check if the asset ID string is null or empty
                            if (assetIds == null || assetIds.isEmpty()) {
                                assetIdList = new LinkedList<>(); // Set to empty list if null or empty
                            } else {
                                // Split the asset IDs by comma and add to the list
                                assetIdList = new LinkedList<>(Arrays.asList(assetIds.split(",")));
                            }

                            // Initialize a list for asset names associated with the activity
                            LinkedList<String> assetNameList = new LinkedList<>();
                            String assetNames = actRs.getString("Asset"); // Retrieve asset names as a string
                            // Check if the asset name string is null or empty
                            if (assetNames == null || assetNames.isEmpty()) {
                                assetNameList = new LinkedList<>(); // Set to empty list if null or empty
                            } else {
                                // Split the asset names by comma and add to the list
                                assetNameList = new LinkedList<>(Arrays.asList(assetNames.split(",")));
                            }

                            // Initialize a list for asset groups associated with the activity
                            LinkedList<String> assetGroupsList = new LinkedList<>();
                            String assetGroups = actRs.getString("Groups"); // Retrieve asset groups as a string
                            // Check if the asset groups string is null or empty
                            if (assetGroups == null || assetGroups.isEmpty()) {
                                assetGroupsList = new LinkedList<>(); // Set to empty list if null or empty
                            } else {
                                // Split the asset groups by comma and add to the list
                                assetGroupsList = new LinkedList<>(Arrays.asList(assetGroups.split(",")));
                            }

                            // Retrieve activity details
                            int sequence = actRs.getInt("Sequence"); // Get the sequence of the activity
                            int duration = actRs.getInt("Duration_min"); // Get the duration of the activity
                            int buffer = actRs.getInt("buffer"); // Get the buffer time of the activity
                            int totalDuration = duration + buffer; // Calculate total duration including buffer

                            // Update the maximum duration for this sequence
                            maxDurations.merge(sequence, totalDuration, Integer::max);

                            // Create a new ActivityDetails object and populate it with the retrieved data
                            activityList.add(
                                new ActivityDetails(
                                    actRs.getString("TaskId"),
                                    actRs.getString("ActivityId"),
                                    actRs.getString("ActivityName"),
                                    actRs.getInt("Sequence"),
                                    actRs.getString("logbook"),
                                    actRs.getInt("Duration_min"),
                                    actRs.getBigDecimal("Pos_X"),
                                    actRs.getBigDecimal("Pos_Y"),
                                    actRs.getString("ActivityAbrv"),
                                    assetIdList,
                                    assetNameList,
                                    actRs.getInt("buffer"),
                                    actRs.getBoolean("enforce"), 
                                    assetGroupsList,
                                    actRs.getString("AssetId"),
                                    actRs.getString("Asset"),
                                    actRs.getInt("enforceStartDuration")
                                )
                            );

                        } while (actRs.next()); // Continue looping while there are more rows
                    }
                }, rs.getString("TaskId")); // Pass the current TaskId to the activity query

                // Calculate total duration for all activities in this task
                int totalDuration = maxDurations.values().stream().mapToInt(Integer::intValue).sum();

                // Set the activity list and total activity count in the TaskDetail object
                task.setActivityList(activityList);
                task.setNoOfActivites(activityList.size());
                task.setHourMinutes(Integer.toString(totalDuration)); // Convert total duration to string

                // Add the populated TaskDetail object to the taskList
                taskList.add(task);

                // Log task information for debugging
                System.out.println("task ID : " + rs.getString("TaskName") + " Duration : " + totalDuration);
                
            } while (rs.next()); // Continue looping while there are more rows in the task result set
        }
    }, creator, tenantId); // Execute the query with provided parameters

    // Return the list of TaskDetail objects
    return taskList;
}

	@Override
	/**
 * Retrieves a list of ActivityConnection objects for a specific task and tenant.
 *
 * This method executes a SQL query to fetch activity connection details from the ActivityConnections 
 * table, filtered by the provided taskId and tenantId. It returns a list of ActivityConnection 
 * objects containing the source and target information for the specified task.
 *
 * @param taskId The ID of the task for which activity connections are to be retrieved.
 * @param tenantId The identifier of the tenant whose activity connections are to be fetched.
 * @return A list of ActivityConnection objects associated with the specified taskId and tenantId.
 */
public List<ActivityConnection> getActivityConnection(String taskId, String tenantId) {
    // Initialize an empty list to store ActivityConnection objects
    List<ActivityConnection> activityConnectionList = new ArrayList<>();
    JdbcTemplate jdbcTemplateOp360=jdbcTemplates.get(tenantId).get(0);
    // SQL query to retrieve activity connection details for a specific task and tenantId
    String sql = "SELECT [TaskId], [SourceId], [Source], [TargetId], [Target], [TenantId] " +
                 "FROM [dbo].[ActivityConnections] " +
                 "WHERE TaskId = ? AND TenantId = ?";

    // Execute the SQL query to fetch activity connections
    jdbcTemplateOp360.query(sql, new RowCallbackHandler() {
        @Override
        public void processRow(ResultSet rs) throws SQLException {
            // Loop through the result set of activity connections
            do {
                // Create a new ActivityConnection object for each row retrieved
                ActivityConnection activityConnection = new ActivityConnection();
                activityConnection.setTaskId(rs.getString("TaskId")); // Set task ID
                activityConnection.setSourceId(rs.getString("SourceId")); // Set source ID
                activityConnection.setSource(rs.getString("Source")); // Set source name
                activityConnection.setTargetId(rs.getString("TargetId")); // Set target ID
                activityConnection.setTarget(rs.getString("Target")); // Set target name

                // Add the populated ActivityConnection object to the activityConnectionList
                activityConnectionList.add(activityConnection);
            } while (rs.next()); // Continue looping while there are more rows in the result set
        }
    }, taskId, tenantId); // Execute the query with provided parameters

    // Return the list of ActivityConnection objects
    return activityConnectionList;
}

	@Override
	/**
 * Checks if a job exists for a given task ID and tenant ID.
 *
 * This method executes a SQL query to count the number of jobs associated 
 * with the provided taskId in the JobDetails table for the specified tenant.
 *
 * @param taskId The ID of the task for which the job existence is checked.
 * @param tenantId The identifier of the tenant to check for the job.
 * @return True if at least one job exists for the specified taskId; false otherwise.
 */
public Boolean checkJobExistsByTaskId(String taskId, String tenantId) {
    // SQL query to count the number of JobIds associated with the specified taskId and tenantId
    String sql = "SELECT count(JobId) FROM [dbo].[JobDetails] WHERE TaskId = ? AND TenantId = ?";
    JdbcTemplate jdbcTemplateOp360=jdbcTemplates.get(tenantId).get(0);
    // Fetch the appropriate JdbcTemplate for the tenant (commented out code)
    // Map<String, JdbcTemplate> jdbcTemplateMap = dynamicDatasource.getDynamicConnection(tenantId);
    // JdbcTemplate jdbcTemplateOp360 = jdbcTemplateMap.get("db_op360");

    // Execute the query and retrieve the count using queryForObject with a lambda RowMapper
    Integer count = jdbcTemplateOp360.queryForObject(sql, (rs, rowNum) -> rs.getInt(1), taskId, tenantId);

    // Return true if count is greater than 0, otherwise false
    return count != null && count > 0;
}

/**
 * Checks if a task exists for a given asset group ID and tenant ID.
 *
 * This method executes a SQL query to count the number of tasks associated 
 * with the provided assetGroupName in the TaskDetails table for the specified tenant.
 *
 * @param assetGroupName The name of the asset group for which task existence is checked.
 * @param tenantId The identifier of the tenant to check for the tasks.
 * @return True if at least one task exists for the specified assetGroupName; false otherwise.
 */
@Override
public Boolean checkTaskExistsByAssetGroupId(String assetGroupName, String tenantId) {
    // SQL query to count the number of TaskIds associated with the specified assetGroupName and tenantId
    String sql = "SELECT count(TaskId) FROM [dbo].TaskDetails WHERE AssetGroup = ? AND TenantId = ?";
    JdbcTemplate jdbcTemplateOp360=jdbcTemplates.get(tenantId).get(0);
    // Fetch the appropriate JdbcTemplate for the tenant (commented out code)
    // Map<String, JdbcTemplate> jdbcTemplateMap = dynamicDatasource.getDynamicConnection(tenantId);
    // JdbcTemplate jdbcTemplateOp360 = jdbcTemplateMap.get("db_op360");

    // Execute the query and retrieve the count using queryForObject with a lambda RowMapper
    Integer count = jdbcTemplateOp360.queryForObject(sql, (rs, rowNum) -> rs.getInt(1), assetGroupName, tenantId);

    // Return true if count is greater than 0, otherwise false
    return count != null && count > 0;
}

	@Override
	/**
 * Updates the status of a task identified by the given task ID in the specified tenant.
 *
 * This method executes a SQL update query to change the status of a task 
 * in the TaskDetails table. If the status is "Published", it also updates 
 * the publish time to the current date.
 *
 * @param taskId The ID of the task whose status is to be updated.
 * @param status The new status to set for the specified task.
 * @param tenantId The identifier of the tenant for the task.
 */
public void updateTaskStatus(String taskId, String status, String tenantId) {
    // Initialize the SQL query string
    String query = "";
    JdbcTemplate jdbcTemplateOp360=jdbcTemplates.get(tenantId).get(0);
    // Determine the query based on the status
    if (status.equals("Published"))
        query = "update [dbo].[TaskDetails] set [Status]=? , [PublishTime] = getDate() where TaskId=?";
    else
        query = "update [dbo].[TaskDetails] set [Status]=? where TaskId=?";

    // Fetch the appropriate JdbcTemplate for the tenant (commented out code)
    // Map<String, JdbcTemplate> jdbcTemplateMap = dynamicDatasource.getDynamicConnection(tenantId);
    // JdbcTemplate jdbcTemplateOp360 = jdbcTemplateMap.get("db_op360");

    // Execute the update query using PreparedStatement
    jdbcTemplateOp360.execute(query, new PreparedStatementCallback<Boolean>() {
        @Override
        public Boolean doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
            // Set the parameters for the PreparedStatement
            ps.setString(1, status); // Set the status
            ps.setString(2, taskId); // Set the taskId

            // Execute the update and return the result
            return ps.execute();
        }
    });
}

/**
 * Updates the remarks of a task identified by the given task ID in the specified tenant.
 *
 * This method executes a SQL update query to change the remarks of a task 
 * in the TaskDetails table.
 *
 * @param taskId The ID of the task whose remarks are to be updated.
 * @param remarks The new remarks to set for the specified task.
 * @param tenantId The identifier of the tenant for the task.
 */
@Override
public void updateTaskRemarks(String taskId, String remarks, String tenantId) {
    // SQL query to update the remarks of the specified task
    String query = "update [dbo].[TaskDetails] set [Remarks]=? where TaskId=?";
    JdbcTemplate jdbcTemplateOp360=jdbcTemplates.get(tenantId).get(0);
    // Fetch the appropriate JdbcTemplate for the tenant (commented out code)
    // Map<String, JdbcTemplate> jdbcTemplateMap = dynamicDatasource.getDynamicConnection(tenantId);
    // JdbcTemplate jdbcTemplateOp360 = jdbcTemplateMap.get("db_op360");

    // Execute the update query using PreparedStatement
    jdbcTemplateOp360.execute(query, new PreparedStatementCallback<>() {
        @Override
        public Object doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
            // Set the parameters for the PreparedStatement
            ps.setString(1, remarks); // Set the remarks
            ps.setString(2, taskId);  // Set the taskId

            // Execute the update
            ps.execute();
            return null; // Return null as the result is not needed
        }
    });
}

	
	
	
	
/**
 * Creates or updates activity connections for a specified task in the tenant's database.
 *
 * This method performs two main operations:
 * 1. It deletes any existing activity connections associated with the specified task ID
 *    and tenant ID to ensure that the database reflects only the most current connections.
 * 2. It inserts new activity connections provided in the list of ActivityConnection objects.
 *
 * This approach guarantees that the database remains synchronized with the current state 
 * of activity connections, preventing stale or outdated records from persisting.
 *
 * @param activityConnections A list of ActivityConnection objects to be inserted. 
 *                            Each object should contain valid identifiers and attributes 
 *                            for the source and target connections.
 * @param tenantId The identifier of the tenant for the activity connections. 
 *                 This ensures that the operations are performed within the context of the 
 *                 correct tenant's schema in a multi-tenant environment.
 * @return true if the operation is successful; false if the list is empty or an error occurs. 
 *         The method returns false if there are no new activity connections to insert,
 *         indicating no changes were made to the database.
 */
@Override
public boolean createActivityConnections(List<ActivityConnection> activityConnections, String tenantId) {
    // Fetch the dynamic JdbcTemplate for the tenant (commented out code)
    // Map<String, JdbcTemplate> jdbcTemplateMap = dynamicDatasource.getDynamicConnection(tenantId);
    // JdbcTemplate jdbcTemplateOp360 = jdbcTemplateMap.get("db_op360");

    // SQL queries
    String deleteSql = "DELETE FROM [dbo].ActivityConnections WHERE TaskId = ? AND TenantId = ?";
    String insertSql = "INSERT INTO [dbo].ActivityConnections (TaskId, SourceId, Source, TargetId, Target, TenantId) " +
            "VALUES (?, ?, ?, ?, ?, ?)";

    try {
        // Check if the list of activity connections is not empty
        if (!activityConnections.isEmpty()) {
        	 JdbcTemplate jdbcTemplateOp360=jdbcTemplates.get(tenantId).get(0);
            // 1. Delete any existing activity connections for the given taskId and tenantId.
            // This ensures that any previously stored connections related to the task are removed,
            // preventing duplicates or inconsistencies in the data.
            jdbcTemplateOp360.update(deleteSql, activityConnections.get(0).getTaskId(), tenantId);

            // 2. Iterate over the list of ActivityConnection and insert each new connection.
            // Each connection is individually inserted into the database, linking the task to its sources and targets.
            for (ActivityConnection activityConnection : activityConnections) {
                jdbcTemplateOp360.update(insertSql,
                        activityConnection.getTaskId(),     // The unique identifier for the task.
                        activityConnection.getSourceId(),   // The unique identifier for the source.
                        activityConnection.getSource(),      // The name or description of the source.
                        activityConnection.getTargetId(),    // The unique identifier for the target.
                        activityConnection.getTarget(),       // The name or description of the target.
                        tenantId);                          // The identifier for the tenant, ensuring proper schema.
            }

            // Return true if all operations succeed, indicating that the new connections have been
            // successfully stored in the database.
            return true;
        } else {
            // Return false if the list is empty, indicating that there are no connections to process.
            return false;
        }

    } catch (Exception e) {
        // Log the error and return false in case of failure to maintain a clear record of issues.
        // This exception handling prevents the application from crashing and allows for debugging.
        e.printStackTrace();
        return false;
    }
}


/**
 * Updates a task's details in the tenant's database.
 *
 * This method deletes existing records for the specified task ID 
 * and then inserts new records for task details and activity details.
 *
 * @param tenantId The identifier of the tenant for the task.
 * @param taskDetail The TaskDetail object containing the details of the task to be updated.
 */
@Override
public void updateTask(String tenantId, TaskDetail taskDetail) {
    // Fetch the dynamic JdbcTemplate for the tenant (commented out code)
    // Map<String, JdbcTemplate> jdbcTemplateMap = dynamicDatasource.getDynamicConnection(tenantId);
    // JdbcTemplate jdbcTemplateOp360 = jdbcTemplateMap.get("db_op360");

    // 1. Delete existing records based on taskId
	 JdbcTemplate jdbcTemplateOp360=jdbcTemplates.get(tenantId).get(0);
    deleteExistingRecords(jdbcTemplateOp360, taskDetail.getTaskId(), tenantId);

    // 2. Insert new task detail records
    insertTaskDetail(jdbcTemplateOp360, taskDetail, tenantId);//insert the data to the TaskDetail and activityCreation Table 
    insertActivityDetails(jdbcTemplateOp360, taskDetail, tenantId);//insert the data to the ActivityConnection Table
}

/**
 * Deletes existing records for a specified task in the tenant's database.
 *
 * This method removes all records associated with the specified taskId
 * from both the TaskDetails and ActivityCreation tables within the given tenant's schema.
 * It is important to ensure that the taskId and tenantId provided are valid to avoid 
 * unintended data loss.
 *
 * @param jdbcTemplate The JdbcTemplate used to execute the SQL queries for database operations.
 *                     It allows interaction with the underlying database using JDBC.
 * @param taskId The unique identifier of the task whose associated records are to be deleted.
 *               This is used to locate the specific records in the database.
 * @param tenantId The unique identifier of the tenant for whom the records are to be deleted.
 *                 This ensures that the operation targets the correct schema in a multi-tenant environment.
 */
private void deleteExistingRecords(JdbcTemplate jdbcTemplate, String taskId, String tenantId) {
    // SQL query to delete task details for the specified taskId and tenantId.
    String deleteTaskDetailSql = "DELETE FROM [dbo].[TaskDetails] WHERE [TaskId] = ? and [TenantId] = ?";
    
    // Execute the delete operation for TaskDetails table using the provided taskId and tenantId.
    // This removes all records associated with the specified taskId for the given tenant.
    jdbcTemplate.update(deleteTaskDetailSql, taskId, tenantId);

    // SQL query to delete activity creations for the specified taskId and tenantId.
    String deleteActivityCreationSql = "DELETE FROM [dbo].[ActivityCreation] WHERE [TaskId] = ? and [TenantId] = ?";
    
    // Execute the delete operation for ActivityCreation table using the same taskId and tenantId.
    // This ensures that any activities associated with the specified task are also removed.
    jdbcTemplate.update(deleteActivityCreationSql, taskId, tenantId);
}

/**
 * Inserts a new task detail record into the tenant's database.
 *
 * This method adds a new entry to the TaskDetails table using the provided TaskDetail object
 * and tenant ID. It populates the columns of the TaskDetails table with the values from the 
 * TaskDetail object and ensures that the record is correctly associated with the specified tenant.
 * The method does not currently handle null or default values for certain fields, which should be 
 * considered if necessary in future enhancements.
 *
 * @param jdbcTemplate The JdbcTemplate used to execute the SQL query for inserting the new task detail.
 *                     It facilitates the execution of SQL statements against the database.
 * @param taskDetail The TaskDetail object containing the information to be inserted into the database.
 *                   This object should have all required fields populated before being passed to the method.
 * @param tenantId The unique identifier of the tenant for which the task detail is being inserted.
 *                 This parameter ensures that the record is added to the correct tenant's schema.
 */
private void insertTaskDetail(JdbcTemplate jdbcTemplate, TaskDetail taskDetail, String tenantId) {
    // SQL query to insert a new task detail into the TaskDetails table.
    String insertTaskDetailSql = "INSERT INTO [dbo].[TaskDetails] (CreationTime, TaskId, TaskName, Status, Creator, PublishTime, PublishRejectUserId, Remarks, Description, UserGroup, UserDept, AssetGroup, TenantId,adhocTask) " +
            "VALUES (getDate(), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";

    // Execute the insert operation with the properties from the taskDetail object.
    // Each question mark (?) in the SQL statement corresponds to a parameter in the following method call.
    jdbcTemplate.update(insertTaskDetailSql,
//            taskDetail.getCreationTime(),  // The timestamp when the task was created.
            taskDetail.getTaskId(),         // The unique identifier for the task.
            taskDetail.getTaskName(),       // The name of the task.
            taskDetail.getStatus(),         // The current status of the task.
            taskDetail.getCreator(),        // The creator of the task.
            null,                            // Setting PublishTime to null; this can be modified if needed.
            taskDetail.getReviewer(),       // The user ID of the reviewer, if applicable.
            taskDetail.getRemarks(),        // Any remarks associated with the task.
            taskDetail.getDescription(),    // A description of the task.
            null,                            // UserGroup is set to null; modify as needed.
            null,                            // UserDept is set to null; modify as needed.
            taskDetail.getAssetGroup(),     // The asset group associated with the task.
            tenantId,
            taskDetail.isadhocTask());                      // The identifier for the tenant, ensuring data integrity in a multi-tenant setup.
    
}


	/**
 * Inserts activity details into the ActivityCreation table for a specified tenant.
 *
 * This method takes a TaskDetail object containing a list of activities and inserts each
 * activity into the tenant-specific ActivityCreation table. The method handles the 
 * mapping of properties from the ActivityDetails objects to the corresponding database columns.
 * 
 * - The method uses a JdbcTemplate for executing the SQL insert operation, which provides 
 *   efficient database access.
 * - Each activity's TransactionId is currently set to null; you may modify this 
 *   based on your application's logic.
 * - The method also handles the insertion of user group and department information
 *   based on the provided ActivityDetails.
 *
 * @param jdbcTemplate The JdbcTemplate used to execute the SQL queries. It is configured 
 *                     to connect to the specific tenant's database.
 * @param taskDetail The TaskDetail object that contains details about the task and its 
 *                   associated activities. This object includes the list of ActivityDetails 
 *                   that need to be inserted.
 * @param tenantId The unique identifier for the tenant whose ActivityCreation records are 
 *                 being updated. This ensures that the correct database schema is accessed 
 *                 for the insert operation.
 */
private void insertActivityDetails(JdbcTemplate jdbcTemplate, TaskDetail taskDetail, String tenantId) {
    // Retrieve the list of activities from the provided TaskDetail object
    List<ActivityDetails> activities = taskDetail.getActivityList();

    // Check if the activity list is not empty before proceeding with the insertion
    if (activities != null && !activities.isEmpty()) {
        // Iterate over each ActivityDetails object in the activity list
        for (ActivityDetails activity : activities) {
            // SQL query to insert a new activity record into the ActivityCreation table
            String insertActivityCreationSql = "INSERT INTO [dbo].[ActivityCreation] " +
                    "(TransactionId, CreationTime, TaskId, ActivityId, Sequence, ActivityName, logbook, " +
                    "Asset, Duration_min, ActivityAbrv, buffer,[enforceStartDuration], enforce, AssetId, Groups, Department, Pos_X, Pos_Y, TenantId) " +
                    "VALUES (?, getDate(), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";

            // Execute the insert operation using the JdbcTemplate's update method
            jdbcTemplate.update(insertActivityCreationSql,
                    // Setting TransactionId to null; may be modified to reflect actual transaction logic
                    null,
                    activity.getTaskId(),                   // TaskId from the activity
                    activity.getActivityId(),               // ActivityId from the activity
                    activity.getSequence(),                 // Sequence number for the activity
                    activity.getActivityName(),             // Name of the activity
                    activity.getLogbook(),                  // Logbook associated with the activity
                   // activity.getAssetName(),                // Asset name linked to the activity
                    activity.getAssetNameList() == null ? "" :
                    	activity.getAssetNameList().stream()
                                  .filter(Objects::nonNull)          // remove null strings
                                  .map(String::trim)                 // trim spaces
                                  .filter(s -> !s.isEmpty())         // remove empty strings
                                  .collect(Collectors.joining(", ")),
                    activity.getDuration(),                 // Duration in minutes for the activity
                    activity.getActAbrv(),                  // Abbreviation for the activity
                    activity.getBuffer(),                   // Buffer time for the activity
                    activity.getEnforceStartDuration(),
                    activity.isEnforce(),                   // Boolean flag for enforcing rules
//                    activity.getAssetId(),                  // Asset ID related to the activity
                    
                    activity.getAssetIDList() == null ? "" :
                    	activity.getAssetIDList().stream()
                                  .filter(Objects::nonNull)          // remove null strings
                                  .map(String::trim)                 // trim spaces
                                  .filter(s -> !s.isEmpty())         // remove empty strings
                                  .collect(Collectors.joining(", ")),
                    // Check for user groups; get first element if present, otherwise null
                    activity.getActivityUserGroupList().size() != 0 
                            ? activity.getActivityUserGroupList().get(0) 
                            : null,                               
                    activity.getActivityDepartment(),       // Department associated with the activity
                    activity.getxPos(),                     // X position for graphical representation
                    activity.getyPos(),                     // Y position for graphical representation
                    tenantId                                // Tenant ID to ensure proper data segregation
            );
        }
    } else {
        // Log a message if no activities are found to be inserted
        System.out.println("No activities to insert for TaskId: " + taskDetail.getTaskId());
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

    jdbcTemplates.put(tenantid, jdbcTenantList);

	System.out.println("Registered Tenant:"+jdbcTemplates.hashCode());
    System.err.println("Jdbctemplate toString:"+jdbcTemplates.toString());
}




}
