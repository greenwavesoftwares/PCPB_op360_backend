package in.co.greenwave.taskapi.dao.implementation.sqlserver;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import in.co.greenwave.taskapi.dao.RestDAO;
import in.co.greenwave.taskapi.model.ActivityDetails;
import in.co.greenwave.taskapi.model.TaskDetail;

/**
 * Repository class for managing task-related operations.
 * Implements the RestDAO interface for data access methods.
 */
@Repository
public class RestfulService implements RestDAO {

    // Autowired JdbcTemplate instances for two different databases
//    @Autowired
//    @Qualifier("jdbcTemplate1")
//    public JdbcTemplate jdbcTemplate; // For accessing task-related data

//    @Autowired
//    @Qualifier("jdbcTemplate2")
//    public JdbcTemplate jdbcTemplate2; // For accessing user credential data
//    
    @Autowired
   	@Qualifier("DatasourceCollections")
   	private  Map<String, List<JdbcTemplate>> jdbcTemplates; // Template for operations on the main database
   	
    /**
     * Deletes a task and its associated activities and connections from the database.
     *
     * This method executes three delete queries to remove the task from 
     * TaskDetails, ActivityCreation, and ActivityConnections tables 
     * associated with the specified task ID and tenant ID.
     *
     * @param taskId The ID of the task to be deleted.
     * @param tenantId The identifier of the tenant for which the task is to be deleted.
     */
    public void deleteTask(String taskId, String tenantId) {
        // Combined delete query for all relevant tables
        String deleteQuery = "DELETE FROM [dbo].[TaskDetails] WHERE TaskId = ? AND TenantId = ?; " +
                             "DELETE FROM [dbo].[ActivityCreation] WHERE TaskId = ? AND TenantId = ?; " +
                             "DELETE FROM [dbo].[ActivityConnections] WHERE TaskId = ? AND TenantId = ?;";

        // Execute the delete query using PreparedStatement
        JdbcTemplate jdbcTemplate=jdbcTemplates.get(tenantId).get(0);
        jdbcTemplate.execute(deleteQuery, new PreparedStatementCallback<Boolean>() {
            @Override
            public Boolean doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
                // Set parameters for the prepared statement
                ps.setString(1, taskId);
                ps.setString(2, tenantId);
                ps.setString(3, taskId);
                ps.setString(4, tenantId);
                ps.setString(5, taskId);
                ps.setString(6, tenantId);
                // Execute the batch of delete statements
                return ps.execute();
            }
        });
    }

    /**
     * Retrieves the status of a task based on its task ID.
     *
     * Executes a query to fetch the status of the specified task 
     * from the TaskDetails table in the tenant's database.
     *
     * @param taskId The ID of the task whose status is to be retrieved.
     * @param tenantId The identifier of the tenant for which the task status is to be fetched.
     * @return The status of the task, or null if an error occurs.
     */
    @Override
    public String getTaskStatus(String taskId, String tenantId) {
        String taskStatus = null; // Initialize taskStatus to null
        try {
            // Query to fetch task status from the TaskDetails table
            String query = "SELECT [Status] FROM [dbo].[TaskDetails] WHERE TaskId = ? AND TenantId = ?";
            JdbcTemplate jdbcTemplate=jdbcTemplates.get(tenantId).get(0);
            // Execute the query using PreparedStatement
            taskStatus = jdbcTemplate.execute(query, new PreparedStatementCallback<String>() {
                @Override
                public String doInPreparedStatement(PreparedStatement preparedStatement) throws SQLException {
                    preparedStatement.setString(1, taskId); // Set taskId parameter
                    preparedStatement.setString(2, tenantId); // Set tenantId parameter
                    ResultSet resultSet = preparedStatement.executeQuery(); // Execute the query
                    // Return the status if found, otherwise return an empty string
                    return resultSet.next() ? resultSet.getString("Status") : "";
                }
            });
        } catch (Exception e) {
            e.printStackTrace(); // Log any exceptions
            return null; // Return null in case of an error
        }
        return taskStatus; // Return the retrieved task status
    }

    /**
     * Retrieves task details based on the provided task ID.
     *
     * This method retrieves comprehensive details about the specified task, 
     * including its associated activities and user groups.
     *
     * @param taskId The ID of the task to retrieve details for.
     * @param tenantId The identifier of the tenant for which the task details are to be fetched.
     * @return TaskDetail object containing the details of the specified task, or null if an error occurs.
     */
    @Override
    public TaskDetail getTaskDetailByID(String taskId, String tenantId) {
        // Query to fetch main task details
        String sql = "SELECT CONVERT(varchar(19), CreationTime, 120) AS CreationTime, TaskName, tab1.TaskId, tab1.[Status], " +
                     "tab1.[PublishTime], tab1.[PublishRejectUserId], tab1.[Remarks], tab1.[Description], " +
                     "tab1.[UserGroup], tab1.[UserDept], tab1.[AssetGroup], Creator, HM, totalActivities " +
                     "FROM ( " +
                     "   SELECT [CreationTime], TaskName, TaskId, [Creator], [Status], [PublishTime], " +
                     "   [PublishRejectUserId], [Remarks], [Description], [UserGroup], [UserDept], [AssetGroup] " +
                     "   FROM [dbo].[TaskDetails] WHERE TaskId = ? AND TenantId = ? " +
                     ") tab1 " +
                     "INNER JOIN ( " +
                     "   SELECT TaskId, LEFT(CONVERT(time, DATEADD(minute, totmin, 0)), 5) AS HM, totalActivities " +
                     "   FROM ( " +
                     "       SELECT TaskId, SUM(dm) AS totmin, COUNT(dm) AS totalActivities " +
                     "       FROM ( " +
                     "           SELECT TaskId, Sequence, MAX(Duration_min) AS dm FROM [dbo].[ActivityCreation] " +
                     "           GROUP BY Sequence, TaskId " +
                     "       ) tb " +
                     "       GROUP BY TaskId " +
                     "   ) tab " +
                     ") tab2 ON tab1.TaskId = tab2.TaskId";

        // Query to fetch associated activity details
        String actSql = "SELECT ac.TaskId, ac.ActivityId, ActivityName, Pos_X, Post_Y, ac.Sequence, " +
                        "[logbook], [Duration_min], [ActivityAbrv] " +
                        "FROM [dbo].[ActivityCreation] ac " +
                        "LEFT JOIN [dbo].[ActivityNodePosition] anp " +
                        "ON ac.TaskId = anp.TaskId AND ac.ActivityId = anp.ActivityId " +
                        "WHERE ac.TaskId = ? ORDER BY SEQUENCE";

        TaskDetail taskDetail; // Object to hold the task details
        try {
        	 JdbcTemplate jdbcTemplate=jdbcTemplates.get(tenantId).get(0);
            // Execute the main task details query
            taskDetail = jdbcTemplate.query(sql, new ResultSetExtractor<TaskDetail>() {
                @Override
                public TaskDetail extractData(ResultSet rs) throws SQLException {
                    TaskDetail taskDetail = new TaskDetail();
                    // Iterate through the result set to populate the TaskDetail object
                    while (rs.next()) {
                        taskDetail.setCreationTime(rs.getString("CreationTime"));
                        taskDetail.setTaskName(rs.getString("taskName"));
                        taskDetail.setTaskId(rs.getString("TaskId"));
                        taskDetail.setCreator(rs.getString("Creator"));
                        taskDetail.setReviewer(rs.getString("PublishRejectUserId"));
                        taskDetail.setHourMinutes(rs.getString("HM"));
                        taskDetail.setAssetGroup(rs.getString("AssetGroup"));
                        taskDetail.setNoOfActivites(rs.getInt("totalActivities"));

                        // Process UserGroup if present
                        if (rs.getString("UserGroup") != null) {
                            List<String> tempGroupList = new ArrayList<>();
                            // Split and add each user group to the list
                            for (String ug : rs.getString("UserGroup").split(",")) {
                                tempGroupList.add(ug);
                            }
                            taskDetail.setUserGroupsList(tempGroupList); // Set the user groups list
                        }

                        // Process UserDept if present
                        if (rs.getString("UserDept") != null) {
                            List<String> tempDeptList = new ArrayList<>();
                            // Split and add each user department to the list
                            for (String ud : rs.getString("UserDept").split(",")) {
                                tempDeptList.add(ud);
                            }
                            taskDetail.setUserDeptsList(tempDeptList); // Set the user departments list
                        }

                        // Fetch associated activity details for the task
                        List<ActivityDetails> activityList = jdbcTemplate.query(actSql, new ResultSetExtractor<List<ActivityDetails>>() {
                            @Override
                            public List<ActivityDetails> extractData(ResultSet rsAct) throws SQLException {
                                List<ActivityDetails> activityList = new ArrayList<>();
                                // Iterate through the result set to populate activity details
                                while (rsAct.next()) {
                                    ActivityDetails activityDetails = new ActivityDetails();
                                    activityDetails.setTaskId(rsAct.getString("TaskId"));
                                    activityDetails.setActivityId(rsAct.getString("ActivityId"));
                                    activityDetails.setActivityName(rsAct.getString("ActivityName"));
                                    activityDetails.setxPos(rsAct.getBigDecimal("Pos_X"));
                                    activityDetails.setyPos(rsAct.getBigDecimal("Post_Y"));
                                    activityDetails.setSequence(rsAct.getInt("Sequence"));
                                    activityDetails.setLogbook(rsAct.getString("logbook"));
                                    activityDetails.setDuration(rsAct.getInt("Duration_min"));
                                    activityDetails.setActAbrv(rsAct.getString("ActivityAbrv"));
                                    activityList.add(activityDetails); // Add activity details to the list
                                }
                                return activityList; // Return the list of activity details
                            }
                        }, taskId); // Set taskId parameter for activity details query

                        taskDetail.setActivityList(activityList); // Set the activity list in task details
                    }
                    return taskDetail; // Return the populated TaskDetail object
                }
            }, taskId, tenantId); // Set parameters for the task details query
        } catch (Exception e) {
            e.printStackTrace(); // Log any exceptions
            return null; // Return null in case of an error
        }
        return taskDetail; // Return the retrieved task details
    }
}
