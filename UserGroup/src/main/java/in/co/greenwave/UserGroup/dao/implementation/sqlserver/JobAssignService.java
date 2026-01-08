package in.co.greenwave.UserGroup.dao.implementation.sqlserver;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import in.co.greenwave.UserGroup.dao.JobAssignDAO;
import in.co.greenwave.UserGroup.model.ActivityDetails;
import in.co.greenwave.UserGroup.model.TaskDetail;

@Repository
public class JobAssignService implements JobAssignDAO {
	

	@Autowired
	@Qualifier("DatasourceCollections")
	Map<String, List<JdbcTemplate>> jdbcTemplates; // Template for operations on the main database
	
	
	/*
	 * Returns published tasks based on tenantId
	 * */

	@Override
	public List<TaskDetail> getPublishedTasks(String tenantId) {
	    String sql = "SELECT task.TaskId, TaskName, buffer, [enforceStartDuration], ActivityId, Sequence, ActivityName, logbook, " +
	                 "Duration_min, [Asset], UserDept, UserGroup, AssetGroup, AssetId, act.Department, act.Groups " +
	                 "FROM dbo.TaskDetails task, dbo.ActivityCreation act " +
	                 "WHERE task.TaskId = act.TaskId AND Status = 'Published' AND task.TenantId = ? " +
	                 "ORDER BY TaskId, Sequence";
	    JdbcTemplate jdbcTemplate=jdbcTemplates.get(tenantId).get(0);
	    return jdbcTemplate.query(sql, rs -> {
	        List<TaskDetail> taskData = new ArrayList<>();
	        List<ActivityDetails> tempActivityList = new ArrayList<>();
	        String taskName = "";
	        String taskID = "";
	        String prevTaskId = "";
	        String userGroup = "";
	        String userDept = "";
	        String assetGroup = "";

	        while (rs.next()) {
	            String currTaskId = rs.getString("TaskId");
	            if (prevTaskId.isEmpty()) {
	                tempActivityList = new ArrayList<>();
	            } else if (!prevTaskId.equalsIgnoreCase(currTaskId)) {
	                taskData.add(new TaskDetail(taskName, taskID, tempActivityList, null, null, assetGroup));
	                tempActivityList = new ArrayList<>();
	            }

	            tempActivityList.add(new ActivityDetails(
	                rs.getString("TaskId"),
	                rs.getString("ActivityId"),
	                rs.getString("ActivityName"),
	                rs.getInt("Sequence"),
	                rs.getString("logbook"),
	                rs.getInt("Duration_min"),
	                splitToList(rs.getString("AssetId")),
	                splitToList(rs.getString("Asset")),
	                rs.getString("Department"),
	                splitToList(rs.getString("Groups")),
	                rs.getInt("buffer"),
	                rs.getInt("enforceStartDuration")
	            ));

	            taskName = rs.getString("TaskName");
	            taskID = rs.getString("TaskId");
	            prevTaskId = currTaskId;
	            userGroup = rs.getString("UserGroup");
	            userDept = rs.getString("UserDept");
	            assetGroup = rs.getString("AssetGroup");
	        }

	        if (!tempActivityList.isEmpty()) {
	            taskData.add(new TaskDetail(taskName, taskID, tempActivityList, null, null, assetGroup));
	        }

	        return taskData;
	    },tenantId);
	}

	private List<String> splitToList(String input) {
	    return input != null && !input.trim().isEmpty()
	            ? Arrays.asList(input.split(","))
	            : new ArrayList<>();
	}

	
	//!-- Old Method Definition
	
//	public List<TaskDetail> getPublishedTasks(String tenantId) {
//		List<TaskDetail> taskData = new ArrayList<>();
//		ResultSet rs = null;
//
//		try {
//			//Query to fetch published task based on tenant details
//			String sql = "Select task.TaskId, TaskName, buffer,[enforceStartDuration],ActivityId, Sequence, ActivityName, logbook, Duration_min, [Asset], " +
//					"UserDept, UserGroup, AssetGroup, AssetId ,act.Department,act.Groups  " +
//					"from "+tenantId+".TaskDetails task, "+tenantId+".ActivityCreation act " +
//					"Where task.TaskId = act.TaskId AND Status = 'Published' and task.TenantId = '"+tenantId+"'"+
//					"order by TaskId, Sequence";
//	    	
//			rs = jdbcTemplate.getDataSource().getConnection().createStatement().executeQuery(sql);//execute query
//
//			List<ActivityDetails> tempActivityList = new ArrayList<>();//array to store activitylist
//			String taskName = "";//string to store task name
//			String taskID = "";//string to store task id
//			String prevTaskId = "";//string to store previous task
//			String userGroup = "";//string to store usergroup
//			
//			String userDept = "";//string to store user dept
//			String assetGroup = "";//string to store asset group
//
//			while (rs.next()) {
//				String currTaskId = rs.getString("TaskId");
//				if (prevTaskId.equalsIgnoreCase("")) {
//					tempActivityList = new ArrayList<>();//stores activity list
//					tempActivityList.add(new ActivityDetails(//adds activities
//							rs.getString("TaskId"),//fetches taskid
//							rs.getString("ActivityId"),//fetches activity id
//							rs.getString("ActivityName"),//fetches activityname
//							rs.getInt("Sequence"),//fetches sequence
//							rs.getString("logbook"),//fetches logbook
//							rs.getInt("Duration_min"),//fetches duration min
//							rs.getString("AssetId") != null&& !rs.getString("AssetId").equals("")? Arrays.asList(rs.getString("AssetId").split(",")) : new ArrayList<>(),
//							//fetches asset
//							rs.getString("Asset") != null&& !rs.getString("Asset").equals("")? Arrays.asList(rs.getString("Asset").split(",")) : new ArrayList<>(),	
//							rs.getString("Department"),//fetches dept
//							rs.getString("Groups") != null && !rs.getString("Groups").equals("")  ? Arrays.asList(rs.getString("Groups").split(",")) : new ArrayList<>(),
//							rs.getInt("buffer"),//fetches buffer
//							rs.getInt("enforceStartDuration")
//							));
//							
//					
//				} else if (prevTaskId.equalsIgnoreCase(currTaskId)) {
//					tempActivityList.add(new ActivityDetails(
//							rs.getString("TaskId"),//fetches taskid
//							rs.getString("ActivityId"),//fetches activity id
//							rs.getString("ActivityName"),//fetches activity name
//							rs.getInt("Sequence"),//fetches sequence
//							rs.getString("logbook"),//fetches logbook
//							rs.getInt("Duration_min"),//fetches duration_min
//							rs.getString("AssetId") != null&& !rs.getString("AssetId").equals("")? Arrays.asList(rs.getString("AssetId").split(",")) : new ArrayList<>(),
//							rs.getString("Asset") != null&& !rs.getString("Asset").equals("")? Arrays.asList(rs.getString("Asset").split(",")) : new ArrayList<>(),	
//							rs.getString("Department"),//fetches dept
//							rs.getString("Groups") != null&& !rs.getString("Groups").equals("")? Arrays.asList(rs.getString("Groups").split(",")) : new ArrayList<>(),
//							rs.getInt("buffer"),//fetches buffer time		
//							rs.getInt("enforceStartDuration")			
//							));
//				} else {
//					if (!tempActivityList.isEmpty()) {//checks whether activity list is empty
//						taskData.add(new TaskDetail(
//								taskName,
//								taskID,
//								tempActivityList,
//								null,null,assetGroup
//								));//adds object in taskdata
//					}
//
//					tempActivityList = new ArrayList<>();
//					tempActivityList.add(new ActivityDetails(
//							rs.getString("TaskId"),//fetches taskid
//							rs.getString("ActivityId"),//fetches activityid
//							rs.getString("ActivityName"),//fetches activity name
//							rs.getInt("Sequence"),//fetches sequence
//							rs.getString("logbook"),//fetches logbook
//							rs.getInt("Duration_min"),//fetches duration min
//							rs.getString("AssetId") != null&& !rs.getString("AssetId").equals("")? Arrays.asList(rs.getString("AssetId").split(",")) : new ArrayList<>(),
//							rs.getString("Asset") != null&& !rs.getString("Asset").equals("")? Arrays.asList(rs.getString("Asset").split(",")) : new ArrayList<>(),	
//							rs.getString("Department"),//fetches department
//							rs.getString("Groups") != null &&!rs.getString("Groups").equals("") ? Arrays.asList(rs.getString("Groups").split(",")) : new ArrayList<>(),
//							rs.getInt("buffer"),			//fetches buffer
//							rs.getInt("enforceStartDuration")
//							));//adds activity in a list
//				}
//
//				taskName = rs.getString("TaskName");//fetches taskname
//				taskID = rs.getString("TaskId");//fetches taskid
//				prevTaskId = currTaskId;
//				userGroup = rs.getString("UserGroup");//fetches usergroup
//				userDept = rs.getString("UserDept");//fetches user department
//				assetGroup = rs.getString("AssetGroup");//fetches asset group
//			}
//
//			if (!tempActivityList.isEmpty()) {//checks whether tempactivitylist is empty
//				taskData.add(new TaskDetail(
//						taskName,
//						taskID,
//						tempActivityList,null,
//						null,
//						assetGroup
//						));
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				if (rs != null) {
//					rs.close();
//				}//close resultset
//			} catch (Exception e) {
//				e.printStackTrace();//Print error
//			}
//		}
//
//		return taskData;
//	}
//
//	
}
