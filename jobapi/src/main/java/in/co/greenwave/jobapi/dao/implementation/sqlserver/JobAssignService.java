package in.co.greenwave.jobapi.dao.implementation.sqlserver;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import in.co.greenwave.jobapi.dao.JobAssignDAO;
import in.co.greenwave.jobapi.model.ActivityDetails;
import in.co.greenwave.jobapi.model.CalendarEventModel;
import in.co.greenwave.jobapi.model.JobDetails;
import in.co.greenwave.jobapi.model.PerformerAvailability;
import in.co.greenwave.jobapi.model.TaskDetail;


@Repository
public class JobAssignService implements JobAssignDAO {



//	@Autowired
//	@Qualifier("jdbcTemplate_OP360")
//	private  JdbcTemplate jdbcTemplate;

//	@Autowired
//	@Qualifier("jdbcTemplate_OP360_Usermodule")
//	private  JdbcTemplate jdbcTemplate_userModule;
	
	@Autowired
	@Qualifier("DatasourceCollections")
	Map<String, List<JdbcTemplate>> jdbcTemplates; // Template for operations on the main database
	
	
//	@Autowired
//	@Qualifier("platformtransactionmanager")
//	private PlatformTransactionManager transactionManager;


	// this method returns groupIds related with all jobs with respect to a tenant
	@Override
	public List<String> fetchAllGroupId(String tenantId) {
		//List<String> groups = new LinkedList<String>();
		JdbcTemplate jdbcTemplate=jdbcTemplates.get(tenantId).get(0);
		
		//Query to fetch groupIds
		String query = "Select Distinct [GroupId] from [dbo].[JobDetails] WHERE GroupId IS NOT NULL and [TenantId]=?";
		//		JdbcTemplate jdbcTemplate = dynamicDatasourceDAO.getDynamicConnection(tenantId)
		//				 .get(op360_key);
		return jdbcTemplate.queryForList(query, String.class,tenantId);
	}

	// this method returns a list of PerformerAvailability (to specify the reasons for which the asset is unavailable)  based on start,stop time and assetId
	@Override
	public List<List<PerformerAvailability>> getAssetAvailability(String activityStart, String activityStop, String activityId,String taskId,String tenantId) {
		// TODO Auto-generated method stub
		JdbcTemplate jdbcTemplate=jdbcTemplates.get(tenantId).get(0);
		System.out.println("Current ActivityId : "+activityId+"Current TaskID : "+taskId);
//		JdbcTemplate jdbcTemplate_usermodule=jdbcTemplates.get(tenantId).get(1);
		
		List<PerformerAvailability> allPerfromersAssets = new LinkedList<PerformerAvailability>();
		//  retrieve asset availability based on the provided activity start, activity stop, asset ID, and tenant ID
		
		String currentActivityAssetIdsQuery=  "SELECT DISTINCT STRING_AGG([AssetId], ',') AS assetIds " +
			    "FROM [dbo].[ActivityCreation] " +
			    "WHERE [ActivityId] = ?";

					String activityAssets = jdbcTemplate.queryForObject(
							currentActivityAssetIdsQuery, String.class,activityId
						);
		
		//below is the query for fetching result from a User Defined TVF 
					String sql = 
						    "SELECT " +
						    "   CASE " +
						    "       WHEN ? >= ScheduledStart AND ? < ScheduleStop THEN 0 " +
						    "       WHEN ? BETWEEN ScheduledStart AND ScheduleStop THEN 0 " +
						    "       WHEN ScheduledStart BETWEEN ? AND ? THEN 0 " +
						    "       WHEN ScheduleStop > ? AND ScheduleStop <= ? THEN 0 " +
						    "       ELSE 1 " +
						    "   END AS [Availability], " +
						    "   JobId, ActivityId, Performer, Approver, AssetID, AssetName, " +
						    "   ScheduledStart, ScheduleStop, [TenantId] " +
						    "FROM dbo.JobActivityDetails " +
						    "CROSS APPLY (SELECT item FROM dbo.fnSplitString(?, ',')) a " +
						    "WHERE JobActivityDetails.AssetId = a.item " +
						    "  AND ScheduledStart >= ? " +
						    "  AND [Status] = 'Not Started' " +
						    "  AND [TenantId] = ?";

						jdbcTemplate.query(sql, new RowCallbackHandler() {
						    @Override
						    public void processRow(ResultSet rs) throws SQLException {
						        try {
						            do {
						                allPerfromersAssets.add(
						                    new PerformerAvailability(
						                        rs.getBoolean("Availability"),
						                        rs.getString("JobId"),
						                        rs.getString("ActivityId"),
						                        rs.getString("Performer"),
						                        rs.getString("Approver"),
						                        rs.getString("ScheduledStart"),
						                        rs.getString("ScheduleStop"),
						                        rs.getString("AssetID"),
						                        rs.getString("AssetName")
						                    )
						                );
						            } while (rs.next());
						        } catch (Exception e) {
						            e.printStackTrace();
						        }
						    }
						}, 
								 activityStart,
								    activityStart,
								    activityStop,
								    activityStart,
								    activityStop,
								    activityStart,
								    activityStop,
								    activityAssets,
								    activityStart,
								    tenantId
						);

		List<List<PerformerAvailability>> allPerformersAssets=allPerfromersAssets.stream()
			    .collect(Collectors.groupingBy(PerformerAvailability::getPerformer))
			    .values()
			    .stream()
			    .toList();
		System.out.println("All Perfromer Asset : "+allPerformersAssets);
		 return allPerformersAssets;
	}

	//this method returns a list of all calendar events based on a user	
	@Override
	public List<CalendarEventModel> getExistingJobs(String user,String tenantId) {
		// TODO Auto-generated method stub
		JdbcTemplate jdbcTemplate=jdbcTemplates.get(tenantId).get(0);
		
		List<CalendarEventModel> eventList=new ArrayList<>();
		//Query to fetch all JobDetails to map it into the CalendarEventModel
		String sql="Select [TaskId] ,[JobId] ,[JobName] ,[Instrument] ,[Approver] ,[ScheduleStart] ,[ScheduleStop] ,[ActualStart] ,[ActualStop] ,[Priority] ,[Repeat] ,[RepeatTill] ,[Remarks] ,[Status] from dbo.JobDetails Where [Assigner] = ? and [TenantId]=?";
		//		JdbcTemplate jdbcTemplate = dynamicDatasourceDAO.getDynamicConnection(tenantId)
		//				 .get(op360_key);

		//injecting the data in the CalendarEventModel modal 
		jdbcTemplate.query(sql, new RowCallbackHandler() {

			@Override
			public void processRow(ResultSet rs) throws SQLException {
				// TODO Auto-generated method stub
				do
				{
					try {
						String eventStart =rs.getString("ScheduleStart");
						String eventStop = rs.getString("ScheduleStop");
						CalendarEventModel model=new CalendarEventModel();
						model.setScheduleStart(eventStart);//setting the scheduleStart of the model
						model.setScheduleStop(eventStop);//setting the scheduleStop of the model
						model.setPriority(rs.getString("Priority"));//setting the priority
						//						model.setTaskname(rs.getString("TaskName"));
						model.setJobname(rs.getString("JobName"));;//setting the jobna
						model.setJobId(rs.getString("JobId"));//setting the jobId
						model.setStatus(rs.getString("Status"));//setting the jobStatus
						eventList.add(model);
					} catch ( SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}while(rs.next());

			}
		},user,tenantId);
		return eventList;
	}


	//this method returns a list of PerformerAvailability( a list of reasons for which that performer is unavailable) based on activity start and stop
	@Override
	public List<List<PerformerAvailability>> getPerformerAvailability(String activityStart, String activityStop,String activityId,String taskId, String tenantId) {
//		System.out.println("Connection ? "+jdbcTemplates.get(tenantId).get(0));
		
		JdbcTemplate jdbcTemplate=jdbcTemplates.get(tenantId).get(0);
		
		JdbcTemplate jdbcTemplate_usermodule=jdbcTemplates.get(tenantId).get(1);
		
		//The check is done to ensure optimal check for the performers who belong to the activity group and not everyone , NOTE : The Group names are saved in the activities table and groupIds in the UserRole pages
		
		String activityGroupNamesQuery="Select [Groups] from [dbo].[ActivityCreation] where [TaskId]=? and [ActivityId]=?"; //Already comma separated single string
		
		
		String activityGroupNames=jdbcTemplate.queryForObject(
			    activityGroupNamesQuery, String.class,taskId,activityId
			);
		
		String activityGroupIdsQuery="Select STRING_AGG([GroupId], ',') from [dbo].[GroupCredentials] where [GroupName] in(select value from STRING_SPLIT(?,',')) ";
		
		String activityGroupIds=jdbcTemplate_usermodule.queryForObject(
				activityGroupIdsQuery, String.class,activityGroupNames //== null ?"":activityGroupNames
			);
		
		String performersQueryWithGroup =
			    "SELECT DISTINCT STRING_AGG(UserId, ',') AS UserIds " +
			    "FROM [dbo].[UserPages] " +
			    "WHERE AllotedPage = 'Performer' " +
			    "AND UserId IN (SELECT [UserId] FROM [dbo].[UserRole] WHERE UserId IN (SELECT value FROM STRING_SPLIT(?,',')))";

			String performersQueryWithoutGroup =
			    "SELECT DISTINCT STRING_AGG(UserId, ',') AS UserIds " +
			    "FROM [dbo].[UserPages] " +
			    "WHERE AllotedPage = 'Performer'";

			String performers;

			if (activityGroupIds == null) {
			    performers = jdbcTemplate_usermodule.queryForObject(
			        performersQueryWithoutGroup,
			        String.class
			    );
			} else {
			    performers = jdbcTemplate_usermodule.queryForObject(
			        performersQueryWithGroup,
			        String.class,
			        activityGroupIds
			    );
			}
			
			
//			String debugSql = String.format(
//				    "SELECT CASE " +
//				    "WHEN '%s' >= ScheduledStart AND '%s' < ScheduleStop THEN 0 " +
//				    "WHEN '%s' BETWEEN ScheduledStart AND ScheduleStop THEN 0 " +
//				    "WHEN ScheduledStart BETWEEN '%s' AND '%s' THEN 0 " +
//				    "WHEN ScheduleStop > '%s' AND ScheduleStop <= '%s' THEN 0 " +
//				    "ELSE 1 END AS Availability, " +
//				    "JobId, ActivityId, Performer, Approver, ScheduledStart, ScheduleStop, TenantId " +
//				    "FROM dbo.JobActivityDetails " +
//				    "CROSS APPLY (SELECT item FROM dbo.fnSplitString(Performer, ',')) b " +
//				    "WHERE item = '%s' " +
//				    "AND ScheduledStart >= '%s' " +
//				    "AND Status = 'Not Started' " +
//				    "AND TenantId = '%s' " +
//				    "ORDER BY ScheduledStart;",
//				    
//				    activityStart,
//				    activityStart,
//				    activityStop,
//				    activityStart,
//				    activityStop,
//				    activityStart,
//				    activityStop,
//				    performers,
//				    activityStart,
//				    tenantId
//				);
//
//				System.out.println("FINAL SQL = \n" + debugSql);

		List<PerformerAvailability> currentActivityPerfromers = new LinkedList<PerformerAvailability>();
		//below is the query for fetching result from a User Defined TVF 
		String sql = 
			    "SELECT " +
			    "   CASE " +
			    "       WHEN ? >= ScheduledStart AND ? < ScheduleStop THEN 0 " +
			    "       WHEN ? BETWEEN ScheduledStart AND ScheduleStop THEN 0 " +
			    "       WHEN ScheduledStart BETWEEN ? AND ? THEN 0 " +
			    "       WHEN ScheduleStop > ? AND ScheduleStop <= ? THEN 0 " +
			    "       ELSE 1 " +
			    "   END AS [Availability], " +
			    "   JobId, ActivityId, Performer, Approver, ScheduledStart, ScheduleStop, [TenantId] " +
			    "FROM dbo.JobActivityDetails " +
			    "CROSS APPLY (SELECT item FROM dbo.fnSplitString(Performer, ',')) b " +
			    "WHERE Item = ? " +
			    "  AND ScheduledStart >= ? " +
			    "  AND [Status] = 'Not Started' " +
			    "  AND [TenantId] = ? " +
			    "ORDER BY ScheduledStart";

			jdbcTemplate.query(sql, new RowCallbackHandler() {
			    @Override
			    public void processRow(ResultSet rs) throws SQLException {
			        try {
			            do {
			                currentActivityPerfromers.add(
			                    new PerformerAvailability(
			                        rs.getBoolean("Availability"),
			                        rs.getString("JobId"),
			                        rs.getString("ActivityId"),
			                        rs.getString("Performer"),
			                        rs.getString("Approver"),
			                        rs.getString("ScheduledStart"),
			                        rs.getString("ScheduleStop")
			                    )
			                );
			            } while (rs.next());
			        } catch (Exception e) {
			            e.printStackTrace();
			        }
			    }
			}, 
					 activityStart,
					    activityStart,
					    activityStop,
					    activityStart,
					    activityStop,
					    activityStart,
					    activityStop,
					    performers,
					    activityStart,
					    tenantId
			);
			
			



		
		
		return currentActivityPerfromers.stream()
			    .collect(Collectors.groupingBy(PerformerAvailability::getPerformer))
			    .values()
			    .stream()
			    .toList();
		

	}

	

	//this method returns jobDetails based on jobId,jobName ,start and stop time of the job
	@Override
	public JobDetails getJobDetails(String tenantId,String jobId) {
		// TODO Auto-generated method stub
		JdbcTemplate jdbcTemplate=jdbcTemplates.get(tenantId).get(0);
		
		JobDetails job=new JobDetails();
		//Sql to fetch the job Details of the given jobId from JobDetails
		String sql="Select j.[TaskId],[TaskName],[JobId] ,j.[JobName],adhoc,j.[TenantId] ,[Assigner],[GroupId],[Instrument] ,[Approver] ,[ScheduleStart] ,[ScheduleStop] " + 
				"  ,[ActualStart] ,[ActualStop] ,[Priority] ,[Repeat] ,[RepeatTill] ,j.[Remarks] ,j.[Status] , t.AssetGroup,j.[Weekdays]" + 
				"  from dbo.JobDetails j, dbo.TaskDetails t Where j.TaskId=t.TaskId  " + 
				"   AND j.[TenantId]=? and j.jobId=?";
		//Sql to fetch the activity details of the given jobId from JobActivityDetails
		String activitySql="Select [JobId] ,job.[ActivityId] ,job.[TaskId] ,ActivityName,"+ 
				"[Sequence],Duration_min ,[LogbookName] ,[buffer],[Performer] ,[Approver] ,[ScheduledStart] ,[ScheduleStop] ,"+ 
				"[ActualStart] ,[ActualStop] ,[Status] ,[AssetName], job.[isGroupOrDept],job.[PerformerType]," + 
				"job.[GroupOrDeptName],job.[AssetID],[Pos_X] = (SELECT [Pos_X] FROM [dbo].[ActivityCreation] cr WHERE cr.TaskId = act.TaskId AND cr.ActivityId = act.ActivityId),[Pos_Y] = (SELECT [Pos_Y] FROM [dbo].[ActivityCreation] cr WHERE cr.TaskId = act.TaskId AND cr.ActivityId = act.ActivityId) from dbo.JobActivityDetails job,dbo.ActivityCreation act" +
				" Where job.ActivityId = act.ActivityId AND " + 
				"job.TaskId = act.TaskId AND JobId = ?  order by Sequence";
		//		JdbcTemplate jdbcTemplate = dynamicDatasourceDAO.getDynamicConnection(tenantId)
		//				 .get(op360_key);

		//injecting the data into the job details object
		jdbcTemplate.query(sql, new RowCallbackHandler() {//processing each row and injecting that data into the respective properties

			@Override
			public void processRow(ResultSet rs) throws SQLException {
				// TODO Auto-generated method stub
				do {
					job.setJobID(rs.getString("JobId"));
					job.setApprover(rs.getString("Approver"));
					job.setGroupId(rs.getString("GroupId"));
					job.setInstrument(rs.getString("Instrument"));
					//					System.out.println("Current Instrument Value : "+rs.getString("Instrument"));
					job.setAssigner(rs.getString("Assigner"));
					job.setJobStatus(rs.getString("Status"));
					job.setJobName(rs.getString("JobName"));
					job.setWeekdays(rs.getInt("Weekdays"));
					job.setAdhoc(rs.getBoolean("adhoc"));
					try {
						job.setScheduledJobEndTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.0").parse(rs.getString("ScheduleStop")));
						//System.out.println("Parsed Starttime"+job.getScheduledJobStartTime());
						job.setScheduledJobStartTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.0").parse(rs.getString("ScheduleStart")));
					} catch (ParseException | SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					job.setPriority(rs.getString("Priority"));
					//job.setInstrument(rs.getString("Remarks"));	//Need to modify


					//Adding activities
					TaskDetail task = new TaskDetail();
					List<ActivityDetails> activityList = new LinkedList<ActivityDetails>();
					task.setTaskId(rs.getString("TaskId"));
					task.setTaskName(rs.getString("TaskName"));
					task.setAssetGroup(rs.getString("AssetGroup"));


					jdbcTemplate.query(activitySql, new RowCallbackHandler() {

						@Override
						public void processRow(ResultSet rsAct) throws SQLException {
							// TODO Auto-generated method stub
							do {
								try {
									ActivityDetails activity=new ActivityDetails(rsAct.getString("TaskId"), rs.getString("JobName"), rsAct.getString("ActivityId"), rsAct.getString("ActivityName"), rsAct.getString("LogbookName"),
											rsAct.getString("Performer"), rsAct.getString("Approver"), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.0").parse(rsAct.getString("ScheduledStart")), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.0").parse(rsAct.getString("ScheduleStop")),
											rsAct.getString("AssetName"), rsAct.getInt("sequence"), rsAct.getInt("Duration_min"),rsAct.getBoolean("isGroupOrDept"),rsAct.getString("performerType"),rsAct.getString("GroupOrDeptName"),rsAct.getString("AssetID"),rsAct.getString("Status"),rsAct.getInt("buffer"));
									activity.setxPos(rsAct.getString("Pos_X"));
									activity.setyPos(rsAct.getString("Pos_Y"));
									
									activityList.add(activity);
								} catch (SQLException | ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}while(rsAct.next());
						}

					},rs.getString("JobId"));

					task.setActivityList(activityList);
					job.setTask(task);
				}while(rs.next());			
			}
		},tenantId,jobId);
		return job;
	}

	//deletes a job from JobDetails table based  on taskId,jobId and jobname , similarly deletes the activity details from the JobActivityDetails table
	//returns the count of rows affected
	@Override
	public int deleteAssignedJob(JobDetails job,String tenantId) {
		// TODO Auto-generated method stub
		JdbcTemplate jdbcTemplate=jdbcTemplates.get(tenantId).get(0);
		
		System.out.println("deleteAssignedJob() called");
		//delete query to  delete the job information from JobDetails
		String jobquery = "delete from dbo.JobDetails where [TaskId] = ? AND [JobId] = ?  AND [JobName] = ? and [TenantId]=?";
		//delete query to delete the activity details of the job
		String activityQuery = "delete from [dbo].[JobActivityDetails] where JobId = ? and ActivityId = ? and [TaskId] = ? and [TenantId]=?";

		List<Object[]> list=new ArrayList<>();
		for (ActivityDetails activity : job.getTask().getActivityList()) {

			//adding the properties for which the delete query gets executed 
			Object[] temp= {job.getJobID(),activity.getActivityId(),activity.getTaskId(),tenantId};
			list.add(temp);
		}

		int jobrows=jdbcTemplate.update(jobquery,job.getTask().getTaskId(),job.getJobID(),job.getJobName(),tenantId);
		//We do batchUpdate for the activityQuery i.e firing the delete query for the activities associated with the job
		int activityrows[]=jdbcTemplate.batchUpdate(activityQuery,list);	
		int totalRowsAffected=activityrows.length+jobrows;
		System.out.println("Total row affected => "+totalRowsAffected);
		return totalRowsAffected;//returning the number of rows affected
	}


	//this method returns the count of 'Not Started' jobs for a particular assigner
	@Override
	public int getNotStartedJobsCount(String assigner, String tenantid) {
		// TODO Auto-generated method stub
		JdbcTemplate jdbcTemplate=jdbcTemplates.get(tenantid).get(0);
		
		String sql="Select sum(Case when Status='Not Started' then 1 else 0 end) as notstartedjobs from [dbo].JobDetails\r\n"
				+ "				where Assigner=? and TenantId=?";
		Map<String,Integer> count=new LinkedHashMap<>();
		//		JdbcTemplate jdbcTemplate = dynamicDatasourceDAO.getDynamicConnection(tenantid)
		//				 .get(op360_key);
		jdbcTemplate.query(sql, new RowCallbackHandler() {// processing each row and injecting the data in the respective properties

			@Override
			public void processRow(ResultSet rs) throws SQLException {
				// TODO Auto-generated method stub
				try {
					do {
						count.put("notstartedjobs",rs.getInt("notstartedjobs"));

					}while(rs.next());
				}catch(Exception e) {
					e.printStackTrace();
				}
			}

		},assigner,tenantid);
		return count.get("notstartedjobs");
	}





	/**
	 * Adds or updates job details and related activity details in the database.
	 * This method is transactional — if any part fails, the entire operation is rolled back.
	 *
	 * @param job      The JobDetails object containing job and activity info.
	 * @param tenantId The tenant ID used for dynamic schema/table resolution.
	 */
	@Transactional(rollbackFor = Exception.class,propagation=Propagation.REQUIRED)
	@Override
	public void addOrUpdateJobDetails(JobDetails job,String tenantId) {
		// TODO Auto-generated method stub
		JdbcTemplate jdbcTemplate=jdbcTemplates.get(tenantId).get(0);
		
		// Create a new transaction template using the injected transaction manager.
		// Ensures that both job and activity DB operations are part of a single transaction.
		//Error rollback is automatically handled if any exception occurs in the transaction block
		
			DateFormat dfDtTm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// Format the current date and time
			// Set the time zone to UTC
	        dfDtTm.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
			String jobquery="DECLARE @TaskId VARCHAR(200) =?, @JobId  VARCHAR(200) =?, @JobName  VARCHAR(200) =?, @Instrument  VARCHAR(200)=? , @Approver  VARCHAR(200) =?, @ScheduleStart  datetime=?, @ScheduleStop  datetime=?, @ActualStart  datetime=?, @ActualStop  datetime=?, @Priority  VARCHAR(200)=?  , @Status  VARCHAR(200)=?, @Assigner  VARCHAR(200)=?, @GroupId  VARCHAR(200) =?, @adhoc  VARCHAR(200)=?, @ReviewerIntimationTime DateTime=?, @ReviewerAction Varchar(200)=?, @Remarks varchar(max)=?, @TenantId varchar(max)=?, @Weekdays numeric(3,0)=?; DECLARE @ifexists bit=0 SELECT @ifexists=1 where exists(SELECT * FROM  [dbo].[JobDetails] WHERE Jobid=@JobId) if @ifexists=0 begin INSERT INTO [dbo].JobDetails ([TaskId] ,[JobId] ,[JobName] ,[Instrument] ,[Approver] ,[ScheduleStart] ,[ScheduleStop] ,[ActualStart] ,[ActualStop] ,[Priority]  ,[Status],[Assigner],[GroupId] , [adhoc],[ReviewerIntimationTime], [ReviewerAction], [Remarks],[TenantId],[Weekdays]) values ( @TaskId, @JobId , @JobName , @Instrument , @Approver , @ScheduleStart  , @ScheduleStop  , @ActualStart  , @ActualStop  , @Priority   , @Status , @Assigner, @GroupId , @adhoc, @ReviewerIntimationTime, @ReviewerAction, @Remarks, @TenantId, @Weekdays ) end else begin INSERT INTO [dbo].[LogJobDetails] select SYSDATETIMEOFFSET() AT TIME ZONE 'India Standard Time' , * from [dbo].[JobDetails] WHERE [JobId] = @jobId and exists ( select SYSDATETIMEOFFSET() AT TIME ZONE 'India Standard Time' , * from [dbo].[JobDetails] WHERE [JobId] = @jobId) UPDATE [dbo].JobDetails SET [TaskId] = @TaskId, [JobId] = @JobId, [JobName] = @JobName, [Instrument] = @Instrument, [Approver] = @Approver, [ScheduleStart] = @ScheduleStart, [ScheduleStop] = @ScheduleStop, [ActualStart] = @ActualStart, [ActualStop] = @ActualStop, [Priority] = @Priority, [Status] = @Status, [Assigner] = @Assigner, [GroupId] = @GroupId, [adhoc] = @adhoc, [ReviewerIntimationTime] = @ReviewerIntimationTime, [ReviewerAction] = @ReviewerAction, [Remarks] = @Remarks, [TenantId] = @TenantId, [Weekdays] = @Weekdays WHERE [JobId] = @JobId; END";//Add or update the job using a stored procedure
			String activityQuery = "DECLARE @JobId varchar(200)=?, @ActivityId varchar(200)=?, @TaskId varchar(200)=?, @LogbookName varchar(200)=?, @Performer varchar(200)=?, @Approver varchar(200)=?, @ScheduledStart datetime=?, @ScheduleStop datetime=?, @ActualStart datetime=?, @ActualStop datetime=?, @Status varchar(200)=?, @AssetID varchar(200)=?, @AssetName varchar(200)=?, @PerformerType varchar(200)=?, @GroupOrDeptName varchar(200)=?, @isGroupOrDept bit=?, @ActivityReviewerIntimationTime datetime=?, @ActivityReviewercompletionTime datetime=?, @Remarks Varchar(max)=?, @FileName Varchar(200)=?, @FileContent Varbinary(max)=?, @TenantId Varchar(max)=?, @enforceStart bit=?,@enableStart bit=?; DECLARE @ifexists bit=0; SELECT @ifexists=1 where exists(SELECT * FROM [dbo].[JobActivityDetails] WHERE Jobid=@jobid and ActivityId=@ActivityId); if @ifexists=0 begin INSERT INTO [dbo].[JobActivityDetails] ([JobId],[ActivityId],[TaskId],[LogbookName],[Performer],[Approver], [ScheduledStart],[ScheduleStop],[ActualStart],[ActualStop],[Status],[AssetID],[AssetName],[PerformerType], [GroupOrDeptName],[isGroupOrDept],[ActivityReviewerIntimationTime],[ActivityReviewercompletionTime], [Remarks], [FileName],[FileContent],[TenantId],[enforceStart],[enableStart]) values ( @JobId , @ActivityId , @TaskId , @LogbookName , @Performer , @Approver , @ScheduledStart , @ScheduleStop , @ActualStart , @ActualStop , @Status , @AssetID , @AssetName , @PerformerType , @GroupOrDeptName , @isGroupOrDept, @ActivityReviewerIntimationTime, @ActivityReviewercompletionTime, @Remarks, @FileName, @FileContent, @TenantId,@enforceStart,@enableStart ) END ELSE BEGIN INSERT INTO [dbo].[LogJobActivityDetails] SELECT SYSDATETIMEOFFSET() AT TIME ZONE 'India Standard Time' , * FROM [dbo].[JobActivityDetails] WHERE [JobId] =  @jobId AND [ActivityId] = @ActivityId; UPDATE [dbo].[JobActivityDetails] SET JobId=@JobId , ActivityId=@ActivityId , TaskId=@TaskId , LogbookName=@LogbookName , Performer=@Performer , Approver=@Approver , ScheduledStart=@ScheduledStart , ScheduleStop=@ScheduleStop , ActualStart=@ActualStart , ActualStop=@ActualStop , Status=@Status , AssetId=@AssetID , AssetName=@AssetName , PerformerType=@PerformerType , GroupOrDeptName=@GroupOrDeptName , isGroupOrDept=@isGroupOrDept, ActivityReviewerIntimationTime=@ActivityReviewerIntimationTime, ActivityReviewercompletionTime=@ActivityReviewercompletionTime, Remarks=@Remarks, FileName=@FileName, FileContent=@FileContent, TenantId=@TenantId,enforceStart=@enforceStart,enableStart=@enableStart WHERE [JobId] =  @jobId AND [ActivityId] = @ActivityId; END";//Add or update activity using a stored procedure
			/*Update the jobdetails and jobactivitydetails table in the database*/
			jdbcTemplate.update(jobquery,job.getTask() != null ? job.getTask().getTaskId() : null,job.getJobID()
					, job.getJobName(),job.getInstrument(),job.getApprover(),dfDtTm.format(job.getScheduledJobStartTime())
					,dfDtTm.format(job.getScheduledJobEndTime()),(job.getActualJobStartTime()!=null?dfDtTm.format(job.getActualJobStartTime()):null),(job.getActualJobEndTime()!=null?dfDtTm.format(job.getActualJobEndTime()):null),job.getPriority() == null ? "Normal" : job.getPriority()
							,job.getJobStatus(),job.getAssigner(),job.getGroupId(),job.isAdhoc(),job.getReviewerIntimationTime(),(job.getJobStatus().equals("Approved")||job.getJobStatus().equals("Rejected"))?job.getJobStatus():null,job.getRemarks(),tenantId,job.getWeekdays());

			System.out.println("dfDtTm.format(job.getScheduledJobEndTime())==>"+dfDtTm.format(job.getScheduledJobEndTime()));
			if(job.getTask()!=null && job.getTask().getActivityList() != null) 
			{
				jdbcTemplate.batchUpdate(activityQuery,new BatchPreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
						// TODO Auto-generated method stub
						ActivityDetails activity = job.getTask().getActivityList().get(i);
						preparedStatement.setString(1, job.getJobID());
						preparedStatement.setString(2, activity.getActivityId());
						preparedStatement.setString(3, job.getTask().getTaskId());
						preparedStatement.setString(4, activity.getLogbook()); 
						preparedStatement.setString(5, activity.getPerformer());
						preparedStatement.setString(6, activity.getApprover());
						preparedStatement.setString(7, dfDtTm.format(activity.getScheduledActivityStartTime()));
						preparedStatement.setString(8, dfDtTm.format(activity.getScheduledActivityEndTime()));
						preparedStatement.setString(9, activity.getActualActivityStartTime()!=null?dfDtTm.format(activity.getActualActivityStartTime()):null);
						preparedStatement.setString(10, activity.getActualActivityEndTime()!=null?dfDtTm.format(activity.getActualActivityEndTime()):null);
						preparedStatement.setString(11, activity.getActvityStatus());
						preparedStatement.setString(12, activity.getAssetId());
						preparedStatement.setString(13, activity.getAssetName());
						preparedStatement.setString(14, activity.getGroupOrDeptName() == null ? "Performer":activity.getPerformerType());
						preparedStatement.setString(15, activity.getGroupOrDeptName());
						preparedStatement.setBoolean(16, activity.isGroupOrDept());
						preparedStatement.setString(17, activity.getReviewerActivityStartTime()!=null?dfDtTm.format(activity.getReviewerActivityStartTime()):null);
						preparedStatement.setString(18, activity.getReviewerActivityStopTime()!=null?dfDtTm.format(activity.getReviewerActivityStopTime()):null);
						preparedStatement.setString(19, activity.getRemarks());
						preparedStatement.setString(20, activity.getActFile());
						preparedStatement.setBytes(21, activity.getActivityFileData());
						preparedStatement.setString(22, tenantId);
						preparedStatement.setBoolean(23, activity.getEnforceStart());

						preparedStatement.setBoolean(24, activity.isEnableStart());

					}


					@Override
					public int getBatchSize() {
						// TODO Auto-generated method stub
						return job.getTask().getActivityList().size();
					}
				});
			}
		


	}

	/**
	 * Adds or updates job details and related activity details in the database.
	 * This method is transactional — if any part fails, the entire operation is rolled back.
	 *
	 * @param jobList      The JobDetails object containing job and activity info.
	 * @param tenantId The tenant ID used for dynamic schema/table resolution.
	 */
	@Transactional(rollbackFor = Exception.class)
	@Override
	public void addOrUpdateListOfJobs(List<JobDetails> jobList, String tenantId) {
		JdbcTemplate jdbcTemplate=jdbcTemplates.get(tenantId).get(0);
		
		// Create a new transaction template using the injected transaction manager.
		// Ensures that both job and activity DB operations are part of a single transaction.
		//Error rollback is automatically handled if any exception occurs in the transaction block
//		TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);//used to roll back when even one transaction fail
//		transactionTemplate.executeWithoutResult(status -> {

			DateFormat dfDtTm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			dfDtTm.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
			
			for (JobDetails job : jobList) {
				String jobquery="DECLARE @TaskId VARCHAR(200) =?, @JobId  VARCHAR(200) =?, @JobName  VARCHAR(200) =?, @Instrument  VARCHAR(200)=? , @Approver  VARCHAR(200) =?, @ScheduleStart  datetime=?, @ScheduleStop  datetime=?, @ActualStart  datetime=?, @ActualStop  datetime=?, @Priority  VARCHAR(200)=?  , @Status  VARCHAR(200)=?, @Assigner  VARCHAR(200)=?, @GroupId  VARCHAR(200) =?, @adhoc  VARCHAR(200)=?, @ReviewerIntimationTime DateTime=?, @ReviewerAction Varchar(200)=?, @Remarks varchar(max)=?, @TenantId varchar(max)=?, @Weekdays numeric(3,0)=?; DECLARE @ifexists bit=0 SELECT @ifexists=1 where exists(SELECT * FROM  [dbo].[JobDetails] WHERE Jobid=@JobId) if @ifexists=0 begin INSERT INTO [dbo].JobDetails ([TaskId] ,[JobId] ,[JobName] ,[Instrument] ,[Approver] ,[ScheduleStart] ,[ScheduleStop] ,[ActualStart] ,[ActualStop] ,[Priority]  ,[Status],[Assigner],[GroupId] , [adhoc],[ReviewerIntimationTime], [ReviewerAction], [Remarks],[TenantId],[Weekdays]) values ( @TaskId, @JobId , @JobName , @Instrument , @Approver , @ScheduleStart  , @ScheduleStop  , @ActualStart  , @ActualStop  , @Priority   , @Status , @Assigner, @GroupId , @adhoc, @ReviewerIntimationTime, @ReviewerAction, @Remarks, @TenantId, @Weekdays ) end else begin INSERT INTO [dbo].[LogJobDetails] select SYSDATETIMEOFFSET() AT TIME ZONE 'India Standard Time' , * from [dbo].[JobDetails] WHERE [JobId] = @jobId and exists ( select SYSDATETIMEOFFSET() AT TIME ZONE 'India Standard Time' , * from [dbo].[JobDetails] WHERE [JobId] = @jobId) UPDATE [dbo].JobDetails SET [TaskId] = @TaskId, [JobId] = @JobId, [JobName] = @JobName, [Instrument] = @Instrument, [Approver] = @Approver, [ScheduleStart] = @ScheduleStart, [ScheduleStop] = @ScheduleStop, [ActualStart] = @ActualStart, [ActualStop] = @ActualStop, [Priority] = @Priority, [Status] = @Status, [Assigner] = @Assigner, [GroupId] = @GroupId, [adhoc] = @adhoc, [ReviewerIntimationTime] = @ReviewerIntimationTime, [ReviewerAction] = @ReviewerAction, [Remarks] = @Remarks, [TenantId] = @TenantId, [Weekdays] = @Weekdays WHERE [JobId] = @JobId; END";//Add or update the job using a stored procedure
				String activityQuery = "DECLARE @JobId varchar(200)=?, @ActivityId varchar(200)=?, @TaskId varchar(200)=?, @LogbookName varchar(200)=?, @Performer varchar(200)=?, @Approver varchar(200)=?, @ScheduledStart datetime=?, @ScheduleStop datetime=?, @ActualStart datetime=?, @ActualStop datetime=?, @Status varchar(200)=?, @AssetID varchar(200)=?, @AssetName varchar(200)=?, @PerformerType varchar(200)=?, @GroupOrDeptName varchar(200)=?, @isGroupOrDept bit=?, @ActivityReviewerIntimationTime datetime=?, @ActivityReviewercompletionTime datetime=?, @Remarks Varchar(max)=?, @FileName Varchar(200)=?, @FileContent Varbinary(max)=?, @TenantId Varchar(max)=?, @enforceStart bit=?,@enableStart bit=?; DECLARE @ifexists bit=0; SELECT @ifexists=1 where exists(SELECT * FROM [dbo].[JobActivityDetails] WHERE Jobid=@jobid and ActivityId=@ActivityId); if @ifexists=0 begin INSERT INTO [dbo].[JobActivityDetails] ([JobId],[ActivityId],[TaskId],[LogbookName],[Performer],[Approver], [ScheduledStart],[ScheduleStop],[ActualStart],[ActualStop],[Status],[AssetID],[AssetName],[PerformerType], [GroupOrDeptName],[isGroupOrDept],[ActivityReviewerIntimationTime],[ActivityReviewercompletionTime], [Remarks], [FileName],[FileContent],[TenantId],[enforceStart],[enableStart]) values ( @JobId , @ActivityId , @TaskId , @LogbookName , @Performer , @Approver , @ScheduledStart , @ScheduleStop , @ActualStart , @ActualStop , @Status , @AssetID , @AssetName , @PerformerType , @GroupOrDeptName , @isGroupOrDept, @ActivityReviewerIntimationTime, @ActivityReviewercompletionTime, @Remarks, @FileName, @FileContent, @TenantId,@enforceStart,@enableStart ) END ELSE BEGIN INSERT INTO [dbo].[LogJobActivityDetails] SELECT SYSDATETIMEOFFSET() AT TIME ZONE 'India Standard Time' , * FROM [dbo].[JobActivityDetails] WHERE [JobId] =  @jobId AND [ActivityId] = @ActivityId; UPDATE [dbo].[JobActivityDetails] SET JobId=@JobId , ActivityId=@ActivityId , TaskId=@TaskId , LogbookName=@LogbookName , Performer=@Performer , Approver=@Approver , ScheduledStart=@ScheduledStart , ScheduleStop=@ScheduleStop , ActualStart=@ActualStart , ActualStop=@ActualStop , Status=@Status , AssetId=@AssetID , AssetName=@AssetName , PerformerType=@PerformerType , GroupOrDeptName=@GroupOrDeptName , isGroupOrDept=@isGroupOrDept, ActivityReviewerIntimationTime=@ActivityReviewerIntimationTime, ActivityReviewercompletionTime=@ActivityReviewercompletionTime, Remarks=@Remarks, FileName=@FileName, FileContent=@FileContent, TenantId=@TenantId,enforceStart=@enforceStart,enableStart=@enableStart WHERE [JobId] =  @jobId AND [ActivityId] = @ActivityId; END";//Add or update activity using a stored procedure

				jdbcTemplate.update(jobquery, 
						job.getTask() != null ? job.getTask().getTaskId() : null,
								job.getJobID(), job.getJobName(), job.getInstrument(),
								job.getApprover(), dfDtTm.format(job.getScheduledJobStartTime()),
								dfDtTm.format(job.getScheduledJobEndTime()),
								job.getActualJobStartTime() != null ? dfDtTm.format(job.getActualJobStartTime()) : null,
										job.getActualJobEndTime() != null ? dfDtTm.format(job.getActualJobEndTime()) : null,
												job.getPriority() == null ? "Normal" : job.getPriority(),
														job.getJobStatus(), job.getAssigner(), job.getGroupId(), job.isAdhoc(),
														job.getReviewerIntimationTime(),
														(job.getJobStatus().equals("Approved") || job.getJobStatus().equals("Rejected")) ? job.getJobStatus() : null,
																job.getRemarks(), tenantId, job.getWeekdays()
						);

				if (job.getTask() != null && job.getTask().getActivityList() != null) {
					jdbcTemplate.batchUpdate(activityQuery, new BatchPreparedStatementSetter() {
						@Override
						public void setValues(PreparedStatement ps, int i) throws SQLException {
							ActivityDetails activity = job.getTask().getActivityList().get(i);
							ps.setString(1, job.getJobID());
							ps.setString(2, activity.getActivityId());
							ps.setString(3, job.getTask().getTaskId());
							ps.setString(4, activity.getLogbook());
							ps.setString(5, activity.getPerformer());
							ps.setString(6, activity.getApprover());
							ps.setString(7, dfDtTm.format(activity.getScheduledActivityStartTime()));
							ps.setString(8, dfDtTm.format(activity.getScheduledActivityEndTime()));
							ps.setString(9, activity.getActualActivityStartTime() != null ? dfDtTm.format(activity.getActualActivityStartTime()) : null);
							ps.setString(10, activity.getActualActivityEndTime() != null ? dfDtTm.format(activity.getActualActivityEndTime()) : null);
							ps.setString(11, activity.getActvityStatus());
							ps.setString(12, activity.getAssetId());
							ps.setString(13, activity.getAssetName());
							ps.setString(14, activity.getGroupOrDeptName() == null ? "Performer" : activity.getPerformerType());
							ps.setString(15, activity.getGroupOrDeptName());
							ps.setBoolean(16, activity.isGroupOrDept());
							ps.setString(17, activity.getReviewerActivityStartTime() != null ? dfDtTm.format(activity.getReviewerActivityStartTime()) : null);
							ps.setString(18, activity.getReviewerActivityStopTime() != null ? dfDtTm.format(activity.getReviewerActivityStopTime()) : null);
							ps.setString(19, activity.getRemarks());
							ps.setString(20, activity.getActFile());
							ps.setBytes(21, activity.getActivityFileData());
							ps.setString(22, tenantId);
							ps.setBoolean(23, activity.getEnforceStart());
							ps.setBoolean(24, activity.isEnableStart());
						}

						@Override
						public int getBatchSize() {
							return job.getTask().getActivityList().size();
						}
					});
				}
			}
//		});

	}

	@Override
	public List<JobDetails> fetchAllAdhocJobs(String tenantId) {
		// TODO Auto-generated method stub
		List<JobDetails> jobList=new ArrayList<>();
		String sql="Select j.[TaskId],[TaskName],[JobId] ,j.[JobName],j.Weekdays,j.[TenantId] ,[Assigner],[GroupId],[Instrument] ,[Approver] ,[ScheduleStart] ,[ScheduleStop] , [ActualStart] ,[ActualStop] ,[Priority] ,[Repeat] ,[RepeatTill] ,j.[Remarks] ,j.[Status] ,[adhoc], t.AssetGroup from dbo.JobDetails j, dbo.TaskDetails t Where j.TaskId=t.TaskId AND j.[TenantId]=? and adhoc = 1 ";
		String activitySql="Select job.[JobId] ,j.JobName,j.Weekdays,job.[ActivityId] ,job.[TaskId] ,ActivityName, [Sequence],Duration_min ,[LogbookName] ,[Performer] ,job.[Approver] ,job.[ScheduledStart] ,job.[ScheduleStop] , job.[ActualStart] ,job.[ActualStop] ,job.[Status] ,[AssetName], job.[isGroupOrDept],job.[PerformerType], job.[GroupOrDeptName],job.[AssetID],[Pos_X] = (SELECT [Pos_X] FROM [dbo].[ActivityCreation] cr WHERE cr.TaskId = act.TaskId AND cr.ActivityId = act.ActivityId),[Pos_Y] = (SELECT [Pos_Y] FROM [dbo].[ActivityCreation] cr WHERE cr.TaskId = act.TaskId AND cr.ActivityId = act.ActivityId) from dbo.JobDetails j, dbo.JobActivityDetails job,dbo.ActivityCreation act Where j.JobId = job.JobId and job.ActivityId = act.ActivityId AND job.TaskId = act.TaskId AND job.JobId = ?   order by Sequence";
		JdbcTemplate jdbcTemplate=jdbcTemplates.get(tenantId).get(0);
		System.out.println(jdbcTemplate);
		jdbcTemplate.query(sql, new RowCallbackHandler() {
		
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				
				// TODO Auto-generated method stub
				do {
					JobDetails job=new JobDetails();
					job.setJobID(rs.getString("JobId"));
					job.setApprover(rs.getString("Approver"));
					job.setGroupId(rs.getString("GroupId"));
//					job.setInstrument(rs.getString("Instrument"));
//					System.out.println("Current Instrument Value : "+rs.getString("Instrument"));
					job.setAssigner(rs.getString("Assigner"));
					job.setJobStatus(rs.getString("Status"));
					job.setJobName(rs.getString("JobName"));
					job.setWeekdays(rs.getInt("Weekdays"));
					job.setAdhoc(rs.getBoolean("adhoc"));
					try {
						job.setScheduledJobEndTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(rs.getString("ScheduleStop")));
//						System.out.println("Parsed Starttime"+job.getScheduledJobStartTime());
						job.setScheduledJobStartTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(rs.getString("ScheduleStart")));
						job.setActualJobStartTime(rs.getString("ActualStart") != null ? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(rs.getString("ActualStart")) : null);
					job.setActualJobEndTime(rs.getString("ActualStop") != null ? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(rs.getString("ActualStop")) : null);
					} catch (ParseException | SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					job.setPriority(rs.getString("Priority"));
					//job.setInstrument(rs.getString("Remarks"));	//Need to modify
 
 
					//Adding activities
					TaskDetail task = new TaskDetail();
					List<ActivityDetails> activityList = new LinkedList<ActivityDetails>();
					task.setTaskId(rs.getString("TaskId"));
					task.setTaskName(rs.getString("TaskName"));
					task.setAssetGroup(rs.getString("AssetGroup"));
					
					
					jdbcTemplate.query(activitySql, new RowCallbackHandler() {
						
						@Override
						public void processRow(ResultSet rsAct) throws SQLException {
							// TODO Auto-generated method stub
							do {
//							 System.out.println("activity Length : "+rsAct.getFetchSize());	
								try {
//									System.out.println("Job Id : "+rs.getString("JobName")+ " ActivityName : "+rsAct.getString("ActivityName")+" Status : "+);
									ActivityDetails activity=new ActivityDetails(rsAct.getString("TaskId"), rs.getString("JobName"), rsAct.getString("ActivityId"), rsAct.getString("ActivityName"), rsAct.getString("LogbookName"),
											rsAct.getString("Performer").isBlank()||rsAct.getString("Performer").isEmpty() || rsAct.getString("Performer")==null ? rsAct.getString("GroupOrDeptName"):rsAct.getString("Performer"), rsAct.getString("Approver"), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.0").parse(rsAct.getString("ScheduledStart")), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.0").parse(rsAct.getString("ScheduleStop")),
											rsAct.getString("AssetName"), rsAct.getInt("sequence"), rsAct.getInt("Duration_min"),rsAct.getBoolean("isGroupOrDept"),rsAct.getString("performerType"),rsAct.getString("GroupOrDeptName"),rsAct.getString("AssetID"),rsAct.getString("Status"),
											rsAct.getString("ActualStart") != null ? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(rsAct.getString("ActualStart")) : null,
											rsAct.getString("ActualStop") != null ? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(rsAct.getString("ActualStop")) : null,rsAct.getString("JobId"),rsAct.getInt("Weekdays")
													);
									activityList.add(activity);
									activity.setxPos(rsAct.getString("Pos_X"));
									activity.setyPos(rsAct.getString("Pos_Y"));
								} catch (SQLException | ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}while(rsAct.next());
						}
						
					},rs.getString("JobId"));
					
					task.setActivityList(activityList);
					job.setTask(task);
					
					jobList.add(job);
				}while(rs.next());			
			}
		},tenantId);
		
	
		
		return jobList;
	}

	@Override
	public List<JobDetails> fetchAllAdhocJobsByAssigner(String tenantId,String assigner) {
		List<JobDetails> jobList=new ArrayList<>();
		String sql="Select j.[TaskId],[TaskName],[JobId] ,j.[JobName],j.Weekdays,j.[TenantId] ,[Assigner],[GroupId],[Instrument] ,[Approver] ,[ScheduleStart] ,[ScheduleStop] , [ActualStart] ,[ActualStop] ,[Priority] ,[Repeat] ,[RepeatTill] ,j.[Remarks] ,j.[Status] ,[adhoc], t.AssetGroup from dbo.JobDetails j, dbo.TaskDetails t Where j.TaskId=t.TaskId AND j.[TenantId]=? and adhoc = 1 and [Assigner]=? ";
		String activitySql="Select job.[JobId] ,j.JobName,j.Weekdays,job.[ActivityId] ,job.[TaskId] ,ActivityName, [Sequence],Duration_min ,[LogbookName] ,[Performer] ,job.[Approver] ,job.[ScheduledStart] ,job.[ScheduleStop] , job.[ActualStart] ,job.[ActualStop] ,job.[Status] ,[AssetName], job.[isGroupOrDept],job.[PerformerType], job.[GroupOrDeptName],job.[AssetID],[Pos_X] = (SELECT [Pos_X] FROM [dbo].[ActivityCreation] cr WHERE cr.TaskId = act.TaskId AND cr.ActivityId = act.ActivityId),[Pos_Y] = (SELECT [Pos_Y] FROM [dbo].[ActivityCreation] cr WHERE cr.TaskId = act.TaskId AND cr.ActivityId = act.ActivityId) from dbo.JobDetails j, dbo.JobActivityDetails job,dbo.ActivityCreation act Where j.JobId = job.JobId and job.ActivityId = act.ActivityId AND job.TaskId = act.TaskId AND job.JobId = ?   order by Sequence";
		JdbcTemplate jdbcTemplate=jdbcTemplates.get(tenantId).get(0);
		System.out.println(jdbcTemplate);
		jdbcTemplate.query(sql, new RowCallbackHandler() {
		
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				
				// TODO Auto-generated method stub
				do {
					JobDetails job=new JobDetails();
					job.setJobID(rs.getString("JobId"));
					job.setApprover(rs.getString("Approver"));
					job.setGroupId(rs.getString("GroupId"));
//					job.setInstrument(rs.getString("Instrument"));
//					System.out.println("Current Instrument Value : "+rs.getString("Instrument"));
					job.setAssigner(rs.getString("Assigner"));
					job.setJobStatus(rs.getString("Status"));
					job.setJobName(rs.getString("JobName"));
					job.setWeekdays(rs.getInt("Weekdays"));
					job.setAdhoc(rs.getBoolean("adhoc"));
					try {
						job.setScheduledJobEndTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(rs.getString("ScheduleStop")));
//						System.out.println("Parsed Starttime"+job.getScheduledJobStartTime());
						job.setScheduledJobStartTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(rs.getString("ScheduleStart")));
						job.setActualJobStartTime(rs.getString("ActualStart") != null ? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(rs.getString("ActualStart")) : null);
					job.setActualJobEndTime(rs.getString("ActualStop") != null ? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(rs.getString("ActualStop")) : null);
					} catch (ParseException | SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					job.setPriority(rs.getString("Priority"));
					//job.setInstrument(rs.getString("Remarks"));	//Need to modify
 
 
					//Adding activities
					TaskDetail task = new TaskDetail();
					List<ActivityDetails> activityList = new LinkedList<ActivityDetails>();
					task.setTaskId(rs.getString("TaskId"));
					task.setTaskName(rs.getString("TaskName"));
					task.setAssetGroup(rs.getString("AssetGroup"));
					
					
					jdbcTemplate.query(activitySql, new RowCallbackHandler() {
						
						@Override
						public void processRow(ResultSet rsAct) throws SQLException {
							// TODO Auto-generated method stub
							do {
//							 System.out.println("activity Length : "+rsAct.getFetchSize());	
								try {
//									System.out.println("Job Id : "+rs.getString("JobName")+ " ActivityName : "+rsAct.getString("ActivityName")+" Status : "+);
									ActivityDetails activity=new ActivityDetails(rsAct.getString("TaskId"), rs.getString("JobName"), rsAct.getString("ActivityId"), rsAct.getString("ActivityName"), rsAct.getString("LogbookName"),
											rsAct.getString("Performer").isBlank()||rsAct.getString("Performer").isEmpty() || rsAct.getString("Performer")==null ? rsAct.getString("GroupOrDeptName"):rsAct.getString("Performer"), rsAct.getString("Approver"), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.0").parse(rsAct.getString("ScheduledStart")), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.0").parse(rsAct.getString("ScheduleStop")),
											rsAct.getString("AssetName"), rsAct.getInt("sequence"), rsAct.getInt("Duration_min"),rsAct.getBoolean("isGroupOrDept"),rsAct.getString("performerType"),rsAct.getString("GroupOrDeptName"),rsAct.getString("AssetID"),rsAct.getString("Status"),
											rsAct.getString("ActualStart") != null ? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(rsAct.getString("ActualStart")) : null,
											rsAct.getString("ActualStop") != null ? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(rsAct.getString("ActualStop")) : null,rsAct.getString("JobId"),rsAct.getInt("Weekdays")
													);
									activityList.add(activity);
									activity.setxPos(rsAct.getString("Pos_X"));
									activity.setyPos(rsAct.getString("Pos_Y"));
								} catch (SQLException | ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}while(rsAct.next());
						}
						
					},rs.getString("JobId"));
					
					task.setActivityList(activityList);
					job.setTask(task);
					
					jobList.add(job);
				}while(rs.next());			
			}
		},tenantId,assigner);
		
	
		
		return jobList;
	}







}










