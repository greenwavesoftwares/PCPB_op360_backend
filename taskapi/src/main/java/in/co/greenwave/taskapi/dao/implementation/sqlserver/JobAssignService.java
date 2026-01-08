package in.co.greenwave.taskapi.dao.implementation.sqlserver;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.text.SimpleDateFormat;
import java.text.DateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterUtils;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import in.co.greenwave.taskapi.dao.JobAssignDAO;

import in.co.greenwave.taskapi.model.ActivityDetails;
import in.co.greenwave.taskapi.model.JobDetails;
import in.co.greenwave.taskapi.model.TaskDetail;

@Repository
public class JobAssignService implements JobAssignDAO {
	@Autowired
	@Qualifier("jdbcTemplate1")
	public JdbcTemplate jdbcTemplate;
	@Autowired
	@Qualifier("jdbcTemplate2")
	public JdbcTemplate jdbcTemplate2;
	
//	@Autowired
//	RestTemplate restTemplate;
	
	@Override
	/**
	 * Retrieves a list of published tasks along with their associated activities.
	 *
	 * @return List of TaskDetail containing information about published tasks and their activities.
	 */
	public List<TaskDetail> getPublishedTasks(String tenantId){
		try {
			String sql ="Select task.TaskId,TaskName,ActivityId,Sequence,ActivityName,logbook,Duration_min,[Asset], \r\n"
					+ "					 UserDept,UserGroup,AssetGroup,AssetId from dbo.TaskDetails task, dbo.ActivityCreation act   \r\n"
					+ "					 Where task.TaskId = act.TaskId AND Status = 'Published' and task.TenantId=? \r\n"
					+ "					 order by TaskId,Sequence";
			return jdbcTemplate.query(sql,  new ResultSetExtractor<List<TaskDetail>>() {
				@Override
				public List<TaskDetail> extractData(ResultSet rs) throws SQLException {
					try {


						List<TaskDetail> taskData = new ArrayList<TaskDetail>();
						List<ActivityDetails> activityList = new ArrayList<ActivityDetails>();
						String taskName;
						String taskID;
						String userGroup;
						String userDept;
						String assetGroup;
						int i=0;
						while(rs.next()) {
							String currTaskId = rs.getString("TaskId");
							taskName = rs.getString("TaskName");
							taskID = rs.getString("TaskId");
							final String taskid=taskID;
							userGroup = rs.getString("UserGroup");
							userDept = rs.getString("UserDept");
							assetGroup = rs.getString("AssetGroup");
							
							TaskDetail task = taskData.stream()
						            .filter(obj -> obj.getTaskId().equals(taskid))
						            .findFirst()
						            .orElse(null);
							activityList = new ArrayList<ActivityDetails>();
							if(task==null)
							{
								
								activityList.add(new ActivityDetails(rs.getString("TaskId"), rs.getString("ActivityId"), rs.getString("ActivityName"), rs.getInt("Sequence"), rs.getString("logbook"), rs.getInt("Duration_min"),rs.getString("AssetId") != null ? Arrays.asList(rs.getString("AssetId").split(",")):new ArrayList<String>(),rs.getString("Asset") != null ? Arrays.asList(rs.getString("Asset").split(",")):new ArrayList<>()));
								taskData.add(new TaskDetail(taskName,taskID , activityList, Arrays.asList(userGroup.split(",")),Arrays.asList(userDept.split(",")), assetGroup));
								
							}else{
								activityList.addAll(task.getActivityList());
								activityList.add(new ActivityDetails(rs.getString("TaskId"), rs.getString("ActivityId"), rs.getString("ActivityName"), rs.getInt("Sequence"), rs.getString("logbook"), rs.getInt("Duration_min"),rs.getString("AssetId") != null ? Arrays.asList(rs.getString("AssetId").split(",")):new ArrayList<String>(),rs.getString("Asset") != null ? Arrays.asList(rs.getString("Asset").split(",")):new ArrayList<>()));
								task.setActivityList(activityList);
								taskData.stream()
						        .filter(obj -> obj.getTaskId().equals(taskid))
						        .findFirst()
						        .ifPresent(updatedTask -> taskData.set(taskData.indexOf(task),task ));

							}
							
							
						}
						//taskData.forEach(t -> System.out.print(t.getTaskId()+","));
						return taskData;
					}

					catch (Exception e) {
						e.printStackTrace();
						return null;

					} 
				}
			},tenantId);

		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * Adds or updates job details in the database.
	 *
	 * @param job JobDetails object containing information about the job.
	 * @return true if the operation is successful, false otherwise.
	 */
	@Override
	public boolean addOrUpdateJobDetails(JobDetails job,String tenantId) {
		// TODO Auto-generated method stub
		try {
			DateFormat dfDt = new SimpleDateFormat("yyyy-MM-dd");
			DateFormat dfDtTm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String jobquery="EXEC updateOrCreateJobUpdated  ?,?,?,?,?,?,?,?,?,?,?,?,? , ?,?,?,?,?";
			String activityQuery = "EXEC updateOrCreateJobActivityUpdated  ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?";
			

			jdbcTemplate.update(jobquery,job.getTask() != null ? job.getTask().getTaskId() : null,job.getJobID()
					, job.getJobName(),job.getInstrument(),job.getApprover(),dfDtTm.format(job.getScheduledJobStartTime())
					,dfDtTm.format(job.getScheduledJobEndTime()),(job.getActualJobStartTime()!=null?dfDtTm.format(job.getActualJobStartTime()):null),(job.getActualJobEndTime()!=null?dfDtTm.format(job.getActualJobEndTime()):null),job.getPriority() == null ? "Normal" : job.getPriority()
					,job.getJobStatus(),job.getAssigner(),job.getGroupId(),job.isAdhoc(),job.getReviewerIntimationTime(),(job.getJobStatus().equals("Approved")||job.getJobStatus().equals("Rejected"))?job.getJobStatus():null,job.getRemarks(),tenantId);
					
			
			if(job.getTask()!=null && job.getTask().getActivityList() != null) 
			{
				jdbcTemplate.batchUpdate(activityQuery,new BatchPreparedStatementSetter() {
						
					@Override
					public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
						// TODO Auto-generated method stub
						ActivityDetails activity = job.getTask().getActivityList().get(i);
						System.out.println("i:"+i);
						
						preparedStatement.setString(1, job.getJobID());
						preparedStatement.setString(2, activity.getActivityId());
						preparedStatement.setString(3, job.getTask().getTaskId());
						preparedStatement.setString(4, activity.getLogbook()); 
						preparedStatement.setString(5, activity.getPerformer());
						preparedStatement.setString(6, activity.getApprover());

						preparedStatement.setString(7, dfDtTm.format(activity.getScheduledActivityStartTime()));
						preparedStatement.setString(8, dfDtTm.format(activity.getScheduledActivityEndTime()));

						System.out.println("Reviewer activity after fe lines");
						preparedStatement.setString(9, activity.getActualActivityStartTime()!=null?dfDtTm.format(activity.getActualActivityStartTime()):null);
						preparedStatement.setString(10, activity.getActualActivityEndTime()!=null?dfDtTm.format(activity.getActualActivityEndTime()):null);
						preparedStatement.setString(11, activity.getActvityStatus());
						preparedStatement.setString(12, activity.getAssetId());
						preparedStatement.setString(13, activity.getAssetName());
						preparedStatement.setString(14, activity.getGroupOrDeptName() == null ? "Performer":activity.getPerformerType());
						preparedStatement.setString(15, activity.getGroupOrDeptName());
						preparedStatement.setBoolean(16, activity.isGroupOrDept());
						
						System.out.println("Reviewer activity");
						preparedStatement.setString(17, activity.getReviewerActivityStartTime()!=null?dfDtTm.format(activity.getReviewerActivityStartTime()):null);
						preparedStatement.setString(18, activity.getReviewerActivityStopTime()!=null?dfDtTm.format(activity.getReviewerActivityStopTime()):null);
						
						preparedStatement.setString(19, activity.getRemarks());
						preparedStatement.setString(20, activity.getActFile());
						//preparedStatement.setBytes(21, activity.getActivityFileData());
						preparedStatement.setString(22, tenantId);
						//System.out.println("Activity file data:"+activity.getActivityFileData());
				}

					@Override
					public int getBatchSize() {
						// TODO Auto-generated method stub
						return job.getTask().getActivityList().size();
					}
				});
				System.out.println("Update successful for a list of activities");

			}

			

			return true;
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	
//	@Override
//	public Object microserviceTesting() {
//		Object result = restTemplate.getForObject("http://GATEWAY-API/jobassign/tags", Object.class);
//		return result;
//	}
	
}

