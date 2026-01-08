package in.co.greenwave.jobapi.dao.implementation.sqlserver;

import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.fasterxml.jackson.databind.JsonNode;

import in.co.greenwave.jobapi.dao.JobPerformDAO;
import in.co.greenwave.jobapi.model.ActivityDetails;
import in.co.greenwave.jobapi.model.JobDetails;
import in.co.greenwave.jobapi.model.TaskDetail;
import io.jsonwebtoken.lang.Collections;
import io.minio.GetObjectArgs;
/**
 *  @Repository annotation defines that this class is where the database operations are performed.
 */
@Repository
public class JobPerformService implements JobPerformDAO {


	@Autowired
	@Qualifier("DatasourceCollections")
	Map<String, List<JdbcTemplate>> jdbcTemplates; // Template for operations on the main database


	/**
	 * The OP360 database is used here,hence @Qualifier("jdbcTemplate_OP360_tenant"
	 * For Database Configuration : Refer JobApiConfiguration class & application.properties
	 */
	@Autowired
	@Qualifier("jdbcTemplate_OP360_tenant")
	private  JdbcTemplate jdbcTemplateTenant;

	
	@Value("${api.usermodule}")
	private String usermoduleurl;
	
	@Value("${api.mail}")
	private String mail;
	
	@Value("${op360.url}")
	private String op360url;
	/*
	 * Service to fetch the jobs required by a performer
	 * Only those activities are retrieved whose previous sequence activities are completed 
	 * Author: Sreepriya Roy
	 */
	@Override
	public List<JobDetails> getJobDataBasedOnPerformer(String currentPerformer,String tenantId)
	{
		JdbcTemplate jdbcTemplate=jdbcTemplates.get(tenantId).get(0);
		//Variable to store the list of jobs required by a performer
		List<JobDetails> jobs = new LinkedList<JobDetails>();

		//Query to fetch data from job details 
		String sql = "SELECT [TaskId] ,[JobId], [adhoc],[JobName] ,[Assigner],[Approver],[GroupId] ,[ScheduleStart],[ScheduleStop],[ActualStart],ActualStop,[Priority] ,[Status],[Weekdays] FROM [dbo].[JobDetails] where JobId in (select JobId FROM [dbo].[JobActivityDetails] where Performer IN (Select * from String_Split(?,',') where value!='' ) or GroupOrDeptName IN (Select * from String_Split(?,',') where value!='')) and Status not in ('Sent For Approval','Completed','Rejected','Aborted') and TenantId=?";

		/*Query to fetch activities of a given job.
		 * The data is fetched based on sequence,i.e, if the activities of the previous sequence are completed,
		 * then only the activities of the next sequence will be retrieved.
		 */
		String actSql = "DECLARE @jobid varchar(max)=?,@performergroup varchar(max)=?; SELECT enableActivityPerformance= (SELECT CASE WHEN NOT EXISTS(SELECT * FROM   dbo.jobactivitydetails A WHERE  status != 'Completed' AND status != 'Aborted' AND A.jobid = JA.jobid AND A.activityid IN (SELECT sourceid FROM   dbo.activityconnections AC WHERE  targetid = JA.activityid and TaskId=JA.TaskId)) OR enablestart = 1 THEN 1 ELSE 0 END),* FROM   dbo.jobactivitydetails JA LEFT JOIN dbo.activitycreation Act ON Act.activityid = JA.activityid AND JA.taskid = Act.taskid WHERE  jobid = @jobid AND status != 'Completed' AND status != 'Aborted' AND ( performer IN (SELECT * FROM   String_split(@performergroup, ',')) OR groupordeptname IN (SELECT * FROM   String_split(@performergroup, ',')) AND performer != '' AND groupordeptname != '' ) ORDER BY Act.sequence";
		//			JdbcTemplate jdbcTemplate = dynamicDatasourceDAO.getDynamicConnection(tenantId)
		//					 .get(op360_key);
		// Execute the query to fetch job details, using a RowCallbackHandler to process results

		jdbcTemplate.query(sql, new RowCallbackHandler()
		{	
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				// TODO Auto-generated method stub
				//int count=0;
				try
				{
					do
					{
						//The fetched properties are assigned to jobD object
						JobDetails jobD=new JobDetails();
						jobD.setJobID(rs.getString("JobId"));//property which stores job id
						jobD.setApprover(rs.getString("Approver"));//property which stores approver
						jobD.setAssigner(rs.getString("Assigner"));//property which stores approver

						jobD.setPriority(rs.getString("Priority"));//property which stores priority
						SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");   //variable to format date
						Date StrtD=format.parse(rs.getString("ScheduleStart"));//variable which stores scheduled start
						Date stpD=format.parse(rs.getString("ScheduleStop"));//variable which stores scheduled stop
						jobD.setScheduledJobStartTime(StrtD);
						jobD.setScheduledJobEndTime(stpD);
						jobD.setJobStatus(rs.getString("Status"));  //property to store job status
						jobD.setGroupId(rs.getString("GroupId"));//property to set group id
						jobD.setJobName(rs.getString("JobName"));//property to set jobname
						Date actualstart=rs.getString("ActualStart")!=null?format.parse(rs.getString("ActualStart")):null;//variable to store the actual start of the job 
						Date actualstop=rs.getString("ActualStop")!=null?format.parse(rs.getString("ActualStop")):null;//variable to store the actual stop of the job 
						jobD.setAdhoc(rs.getBoolean("adhoc"));
						jobD.setActualJobStartTime(actualstart);
						jobD.setActualJobEndTime(actualstop);
						jobD.setWeekdays(rs.getInt("Weekdays"));//property which stores the number of days in a week a job can be performed

						//fetch actvities
						List<ActivityDetails> activityList = new LinkedList<ActivityDetails>();
						jdbcTemplate.query(actSql, new RowCallbackHandler()
						{

							@Override
							public void processRow(ResultSet rsAct) throws SQLException
							{
								// TODO Auto-generated method stub
								try
								{
									do
									{
										ActivityDetails acvtvD = new ActivityDetails(); // Object to store current activity details

										// Set the properties for the current activity
										acvtvD.setActivityId(rsAct.getString("ActivityId")); // Set the ID of the current activity
										acvtvD.setActivityName(rsAct.getString("ActivityName")); // Set the name of the current activity

										// Parse and set the scheduled start and end times for the current activity
										Date startDA = format.parse(rsAct.getString("ScheduledStart")); // Variable to hold scheduled start time
										Date stopDA = format.parse(rsAct.getString("ScheduleStop")); // Variable to hold scheduled end time
										acvtvD.setScheduledActivityStartTime(startDA); // Set the scheduled start time
										acvtvD.setScheduledActivityEndTime(stopDA); // Set the scheduled end time
										acvtvD.setEnforceStart(rsAct.getBoolean("enforceStart"));
										// Set actual start and end times, handling potential null values
										acvtvD.setActualActvtyStrt(rsAct.getString("ActualStart") == null ? "-" : rsAct.getString("ActualStart")); // Set actual start time or placeholder
										acvtvD.setActualActvtEnd(rsAct.getString("ActualStop") == null ? "-" : rsAct.getString("ActualStop")); // Set actual end time or placeholder
										acvtvD.setActualActivityStartTime(rsAct.getString("ActualStart") == null ? null : format.parse(rsAct.getString("ActualStart"))); // Parse and set actual start time
										acvtvD.setActualActivityEndTime(rsAct.getString("ActualStop") == null ? null : format.parse(rsAct.getString("ActualStop"))); // Parse and set actual end time

										// Set other properties related to the activity
										acvtvD.setPerformerType(rsAct.getString("PerformerType")); // Set the type of performer for the activity
										acvtvD.setGroupOrDeptName(rsAct.getString("GroupOrDeptName")); // Set the name of the group or department associated with the activity
										acvtvD.setActvityStatus(rsAct.getString("Status")); // Set the status of the activity
										acvtvD.setRemarks(rsAct.getString("Remarks")); // Set any remarks related to the activity
										acvtvD.setAssetId(rsAct.getString("AssetID")); // Set the ID of the associated asset
										acvtvD.setEnforce(rsAct.getBoolean("enforce")); // Set whether the activity is enforced
										acvtvD.setAssetName(rsAct.getString("AssetName")); // Set the name of the associated asset

										// Set job ID and sequence for the activity
										acvtvD.setJobId(rsAct.getString("JobId")); // Set the job ID for the current activity
										acvtvD.setSequence(rsAct.getInt("sequence")); // Set the sequence number for the activity
										acvtvD.setLogbook(rsAct.getString("LogbookName")); // Set the name of the logbook associated with the activity
										acvtvD.setPerformer(rsAct.getString("Performer")); // Set the name of the performer for the activity
										acvtvD.setApprover(rsAct.getString("Approver")); // Set the name of the approver for the activity

										// Set additional activity-related properties
										acvtvD.setActFile(rsAct.getString("FileName")); // Set the name of any associated activity file
										acvtvD.setBuffer(rsAct.getInt("buffer")); // Set the buffer time for the activity
										acvtvD.setDelayDueToBuffer("-"); // Placeholder for any delay due to buffer, initially set to "-"
										acvtvD.setActivityFileData(rsAct.getBytes("FileContent")); // Set the binary content of any associated activity file
										acvtvD.setGroupOrDept(rsAct.getBoolean("isGroupOrDept")); // Set whether the activity is associated with a group or department
										acvtvD.setEnableStart(rsAct.getBoolean("enableStart"));
										acvtvD.setEnableActivityPerformance(rsAct.getBoolean("enableActivityPerformance"));
										// Add the populated ActivityDetails object to the list of activities for the current job
										activityList.add(acvtvD); // Add the activity details to the activity list

									}while(rsAct.next());
								}
								catch (Exception e) {
									e.printStackTrace();
								}
							}
						},rs.getString("JobId"),currentPerformer);
						/**
						 *  For Each JobId the respective activity is fetched ,
						 *  The outer ResultSet rs is now resumed
						 */
						TaskDetail task=new TaskDetail();//object which stores the taskid and the list of activities
						//							task.setTaskName(rs.getString("JobName"));
						task.setTaskId(rs.getString("TaskId"));
						task.setActivityList(activityList);
						jobD.setTask(task);

						jobs.add(jobD);	
					}while(rs.next());
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		},currentPerformer,currentPerformer,tenantId);
		return jobs;	
	}




	/**
	 * Fetch the LastCompleted sequence from JobDetails Table 
	 */
	@Override
	public int fetchLastCompletedOrder(String jobId,String tenantId) {
		// TODO Auto-generated method stub
		String sql="select top 1 sequence=(select Sequence from [dbo].[ActivityCreation] where ActivityId=AId) from ( SELECT [JobId] ,[ActivityId] AId ,[TaskId] ,[LogbookName] ,[Performer] ,[Approver] ,[ScheduledStart] ,[ScheduleStop] ,[Status] FROM [dbo].[JobActivityDetails] where   JobId=? and Status=? AND [TenantId]=? ) tb order by sequence desc";

		JdbcTemplate jdbcTemplate=jdbcTemplates.get(tenantId).get(0);
		return jdbcTemplate.queryForObject(sql,Integer.class,jobId,"Completed",tenantId);
	}
	/**
	 * Fetch the remaining activity count based on JobId and Sequence
	 */
	@Override
	public int fetchRemainingActivties(String jobId, int lastCompletedOrder,String tenantId) {
		// TODO Auto-generated method stub
		JdbcTemplate jdbcTemplate=jdbcTemplates.get(tenantId).get(0);
		String sql="select count(*) size from ( select JobId,aid,seq=(select Sequence from dbo.ActivityCreation where ActivityId=aid),Status from ( select JobId,ActivityId aid ,Status from [dbo].[JobActivityDetails] where  JobId=? and Status<>'Completed' AND [TenantId]=? ) tb ) tb2 where seq=?";
		//		 JdbcTemplate jdbcTemplate = dynamicDatasourceDAO.getDynamicConnection(tenantId)
		//					 .get(tenantId);
		return jdbcTemplate.queryForObject(sql, Integer.class,jobId,lastCompletedOrder,tenantId);

	}


	@Override
	public void updateFileName(ActivityDetails act, String filename,String tenantId) {
		// TODO Auto-generated method stub
		System.out.println("The act is --> "+act.getActivityId()+" "+act.getJobId());
		JdbcTemplate jdbcTemplate=jdbcTemplates.get(tenantId).get(0);
		String sql="UPDATE [dbo].[JobActivityDetails] SET [FileName] = ? Where JobId = ? AND ActivityId = ?";
		//		JdbcTemplate jdbcTemplate = dynamicDatasourceDAO.getDynamicConnection(tenantId)
		//				 .get(op360_key);
		jdbcTemplate.update(sql,filename,act.getJobId(),act.getActivityId());
	}

	/*
	 *  Method to retrieve JobDetails based on the provided JobId and TenantId
	 * Author: Sreepriya Roy
	 */
	@Override
	public JobDetails getJobDetailsByJobId(String jobId, String tenantId) {
		JdbcTemplate jdbcTemplate=jdbcTemplates.get(tenantId).get(0);
		JobDetails jobD = new JobDetails(); // Create a new JobDetails object to store job information

		// SQL query to fetch job details for a specific JobId and TenantId
		String sql = "SELECT [TaskId], [JobId], [JobName], [Approver], [ScheduleStart], [ScheduleStop], ActualStop, [Priority], [Status] " +
				"FROM [" + tenantId + "].[JobDetails] WHERE JobId = ? AND [TenantId] = ?";

		// SQL query to fetch associated activity details for the specified JobId
		String actSql = "SELECT [JobId], AId, " +
				"ActivityName = (SELECT [ActivityName] FROM [" + tenantId + "].[ActivityCreation] WHERE ActivityId = AId), " +
				"sequence = (SELECT Sequence FROM [" + tenantId + "].[ActivityCreation] WHERE ActivityId = AId), " +
				"tb.[TaskId], [LogbookName], [Performer], [Approver], " +
				"[ScheduledStart], [ScheduleStop], [ActualStart], [ActualStop], [Status], [FileName], ac.[buffer] AS myBuffer, ac.[CreationTime] " +
				"FROM (SELECT [JobId], [ActivityId] AId, [TaskId], [LogbookName], [Performer], [Approver], " +
				"[ScheduledStart], [ScheduleStop], [ActualStart], [ActualStop], [Status], [FileName] " +
				"FROM [" + tenantId + "].[JobActivityDetails] WHERE JobId = ?) tb " +
				"LEFT JOIN " + tenantId + ".ActivityCreation ac ON tb.TaskId = ac.TaskId AND tb.AId = ac.ActivityId " +
				"ORDER BY sequence";

		// Execute the query to fetch job details, using a RowCallbackHandler to process results
		jdbcTemplate.query(sql, new RowCallbackHandler() {
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				// Handle each row returned from the job details query
				try {
					do {

						// Set properties of the JobDetails object from the ResultSet
						jobD.setJobID(rs.getString("JobId")); // Set the JobId
						jobD.setApprover(rs.getString("Approver")); // Set the approver's name
						jobD.setPriority(rs.getString("Priority")); // Set the priority of the job


						// Create a date format to parse date strings from the database
						SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

						// Parse and set the scheduled start and end times for the job
						Date startD = format.parse(rs.getString("ScheduleStart"));
						Date stopD = format.parse(rs.getString("ScheduleStop"));
						jobD.setScheduledJobStartTime(startD); // Set the scheduled start time
						jobD.setScheduledJobEndTime(stopD); // Set the scheduled end time

						jobD.setJobStatus(rs.getString("Status")); // Set the job status
						jobD.setJobName(rs.getString("JobName")); // Set the job name

						// Initialize a list to store activities associated with the job
						List<ActivityDetails> activityList = new LinkedList<ActivityDetails>();

						// Execute the query to fetch activities associated with the current JobId
						jdbcTemplate.query(actSql, new RowCallbackHandler() {
							@Override
							public void processRow(ResultSet rsAct) throws SQLException {
								// Handle each row returned from the activity details query
								try {
									do {

										// Create a new ActivityDetails object for each activity
										ActivityDetails acvtvD = new ActivityDetails();
										acvtvD.setActivityId(rsAct.getString("AId")); // Set the activity ID
										acvtvD.setActivityName(rsAct.getString("ActivityName")); // Set the activity name

										// Parse and set the scheduled start and end times for the activity
										Date startDA = format.parse(rsAct.getString("ScheduledStart"));
										Date stopDA = format.parse(rsAct.getString("ScheduleStop"));
										acvtvD.setScheduledActivityStartTime(startDA); // Set scheduled start time
										acvtvD.setScheduledActivityEndTime(stopDA); // Set scheduled end time

										// Set actual start and end times, handling potential null values
										acvtvD.setActualActvtyStrt(rsAct.getString("ActualStart") == null ? "-" : rsAct.getString("ActualStart"));
										acvtvD.setActualActvtEnd(rsAct.getString("ActualStop") == null ? "-" : rsAct.getString("ActualStop"));

										acvtvD.setActvityStatus(rsAct.getString("Status")); // Set the status of the activity
										acvtvD.setJobId(rsAct.getString("JobId")); // Set the associated JobId
										acvtvD.setSequence(rsAct.getInt("sequence")); // Set the sequence of the activity
										acvtvD.setLogbook(rsAct.getString("LogbookName")); // Set the logbook name

										// Set additional properties related to the activity
										acvtvD.setPerformer(rsAct.getString("Performer")); // Set the performer name
										acvtvD.setApprover(rsAct.getString("Approver")); // Set the approver name for the activity
										acvtvD.setActFile(rsAct.getString("FileName")); // Set the associated activity file name
										acvtvD.setBuffer(rsAct.getInt("myBuffer")); // Set the buffer value
										acvtvD.setDelayDueToBuffer("-"); // Placeholder for delay due to buffer

										// Add the populated ActivityDetails object to the activity list
										activityList.add(acvtvD);
									} while (rsAct.next()); // Continue iterating through the ResultSet for activities
								} catch (Exception e) {
									e.printStackTrace(); // Print stack trace for any exceptions during activity processing
								}
							}
						}, rs.getString("JobId")); // Pass the JobId for the activity query

						/**
						 * For each JobId, the respective activity has been fetched.
						 * The outer ResultSet (rs) is now resumed to continue processing job details.
						 */
						TaskDetail task = new TaskDetail(); // Create a new TaskDetail object for the current task
						// task.setTaskName(rs.getString("TaskName")); // (Commented out) Set the task name if needed
						task.setTaskId(rs.getString("TaskId")); // Set the TaskId for the current task
						task.setActivityList(activityList); // Associate the populated activity list with the task
						jobD.setTask(task); // Set the TaskDetail object in the JobDetails object (jobD)

						// jobs.add(jobD); // (Commented out) Add jobD to a list of jobs if needed
					} while (rs.next()); // Continue iterating through the ResultSet for jobs
				} catch (Exception e) {
					e.printStackTrace(); // Print stack trace for any exceptions during job processing
				}
			}
		}, jobId, tenantId); // Execute the query to fetch job details, passing JobId and TenantId

		return jobD; // Return the populated JobDetails object
	}
	/*
	 * Update enforced activites on interval
	 * The enforced activity is started on time if previous activities are completed
	 * Author: Sreepriya Roy
	 */
	@Override
	public void updateEnforcedActivitiesDetails(String tenantid) {
		String sql="  DECLARE @tenant varchar(max)=?;\r\n"
				+ " UPDATE J SET J.Status = 'Rejected' ,J.Remarks='Enforced Activity is not completed within time' \r\n"
				+ " \r\n"
				+ " FROM [dbo].[JobActivityDetails] JA INNER JOIN ( SELECT * FROM [dbo].[ActivityCreation] WHERE enforce = 1 ) AC ON JA.ActivityId = AC.ActivityId Inner Join [dbo].JobDetails J on J.JobId=JA.JobId WHERE (datediff(minute,JA.ActualStart,CONVERT(datetime, SYSDATETIMEOFFSET() AT TIME ZONE 'India Standard Time'))>datediff(minute,JA.ScheduledStart,JA.ScheduleStop) and JA.ActualStop is null) or (datediff(minute,JA.ActualStart,JA.ActualStop)>datediff(minute,JA.ScheduledStart,JA.ScheduleStop) and JA.ActualStop is not null) and JA.ActualStop Is  NULL and j.TenantId=@tenant\r\n"
				+ "";
		JdbcTemplate jdbcTemplate=jdbcTemplates.get(tenantid).get(0);
		jdbcTemplate.update(sql,tenantid);
	}
	/*
	 * method to update enforced activities at regular interval
	 * */
	@Override
	public void updateEnforcedActivitiesForEveryTenant() {

		String sql="SELECT DISTINCT TenantId "
				+ "  FROM [dbo].[tenant_details]";
		List<String> tenants=new ArrayList();//array to store all tenants
		jdbcTemplateTenant.query(sql, new RowCallbackHandler() {

			@Override
			public void processRow(ResultSet rs) throws SQLException {
				// TODO Auto-generated method stub
				try {
					do {
						String tenant=rs.getString("TenantId");
						tenants.add(tenant);//add the tenant in tenants list
					}while(rs.next());
				}catch(Exception e) {
					e.printStackTrace();
				}
			}

		});
		//Update enforced activities for every tenant
		for(String tenant:tenants)
			updateEnforcedActivitiesDetails(tenant);
	}


	/*
	 * Service which gives a count of total number of completed jobs based on
	 * a reviewer
	 */
	@Override
	public int fetchCompletedJobCount(String reviewer,String tenantId) {
		try {
			/*
			 * Query to count the number of completed jobs
			 * */
			JdbcTemplate jdbcTemplate=jdbcTemplates.get(tenantId).get(0);
			String sql="SELECT Count(*)  FROM [dbo].[JobDetails] where Approver=? and Status='Completed' and TenantId=?\r\n"
					+ "";
			//		JdbcTemplate jdbcTemplate = dynamicDatasourceDAO.getDynamicConnection(tenantId)
			//				 .get(op360_key);
			int completedjobcount=jdbcTemplate.queryForObject(sql,Integer.class,reviewer,tenantId);
			return completedjobcount;
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return 0;
		}
	}
	/*
	 * Service to fetch last 10 jobs based on performer
	 * a reviewer
	 * Author: Sreepriya Roy
	 */
	@Override
	public List<JobDetails> fetchLastTenJobs(String performergroupList,String tenantId){
		JdbcTemplate jdbcTemplate=jdbcTemplates.get(tenantId).get(0);//Query to fetch last ten jobs based on performer and group list

		String sql="SELECT TOP(10)* from( Select distinct j.[JobId],j.[JobName],j.[Status] FROM [" + tenantId + "].[JobDetails] j inner join [" + tenantId + "].[JobActivityDetails] a on j.JobId=a.JobId where Convert(date,ScheduleStart)<=Convert(date,SYSDATETIMEOFFSET() AT TIME ZONE 'India Standard Time') and (a.Performer in(Select * from string_split(?,',')) or a.GroupOrDeptName in(Select * from string_split(?,','))) and j.TenantId=? )t";
		List<JobDetails> jobDetails=new LinkedList<>();
		//		JdbcTemplate jdbcTemplate = dynamicDatasourceDAO.getDynamicConnection(tenantId)
		//				 .get(op360_key);
		// Execute the query to fetch last ten jobs , using a RowCallbackHandler to process results

		jdbcTemplate.query(sql, new RowCallbackHandler() {

			@Override
			public void processRow(ResultSet rs) throws SQLException {
				// TODO Auto-generated method stub
				try {
					do {
						//Object to store the details of current job
						JobDetails job=new JobDetails();
						job.setJobID(rs.getString("JobId"));//property to set job id
						job.setJobName(rs.getString("JobName"));//property to set jobname
						job.setJobStatus(rs.getString("Status"));//property to set status
						jobDetails.add(job);//the job is added to job details list
					}while(rs.next());
				}catch(Exception e) {
					e.printStackTrace();
				}
			}

		},performergroupList,performergroupList,tenantId);
		return jobDetails;
	}

	/*
	 * Service to find the details required to display the stats in the performer page
	 * Author: Sreepriya Roy
	 * */
	@Override
	public Map<String, Map<String, Integer>> fetchPerformerStats(String performers, String tenantId) {
		// TODO Auto-generated method stub
		//		JdbcTemplate jdbcTemplate = dynamicDatasourceDAO.getDynamicConnection(tenantId)
		//				 .get(op360_key);
		JdbcTemplate jdbcTemplate=jdbcTemplates.get(tenantId).get(0);
		//Query to find pending job count based on date
		String pending="SELECT DISTINCT datepart(day,DATE) AS DATE,COUNT(DATE) OVER(PARTITION BY DATE) AS VALUE FROM (SELECT  * from( Select distinct j.[JobId],j.[JobName],j.[Status],CONVERT(DATE,j.ScheduleStart) AS DATE FROM [dbo].[JobDetails] j inner join [dbo].[JobActivityDetails] a on j.JobId=a.JobId where Convert(date,ScheduleStart)<=Convert(date,SYSDATETIMEOFFSET() AT TIME ZONE 'India Standard Time') AND Convert(date,ScheduleStart)>=Convert(date,SYSDATETIMEOFFSET() AT TIME ZONE 'India Standard Time'-7) and j.Status='Not Started' and (a.Performer in(Select * from string_split(?,',')) or a.GroupOrDeptName in(Select * from string_split(?,','))) and j.TenantId=? )t )T";

		Map<String,Integer> pendingjobCount=new LinkedHashMap<>();//maps to store pending jobs based on date
		jdbcTemplate.query(pending, new RowCallbackHandler() {//execute query

			@Override
			public void processRow(ResultSet rs) throws SQLException {
				// TODO Auto-generated method stub
				try {
					do{
						String date=rs.getString("date");//variable to hold date
						int value=rs.getInt("value");//variable to store the number of pending jobs on that date
						pendingjobCount.put(date, value);//put the value  on the map
					}while(rs.next()) ;
				}catch(Exception e) {
					e.printStackTrace();
				}
			}

		},performers,performers,tenantId);

		//Query to find approved jobs count based on date
		String approved="SELECT DISTINCT datepart(day,DATE) AS DATE,COUNT(DATE) OVER(PARTITION BY DATE) AS VALUE FROM ( SELECT  * from( Select distinct j.[JobId],j.[JobName],j.[Status],CONVERT(DATE,j.ScheduleStart) AS DATE FROM [dbo].[JobDetails] j inner join [dbo].[JobActivityDetails] a on j.JobId=a.JobId where Convert(date,ScheduleStart)<=Convert(date,SYSDATETIMEOFFSET() AT TIME ZONE 'India Standard Time') AND Convert(date,ScheduleStart)>=Convert(date,SYSDATETIMEOFFSET() AT TIME ZONE 'India Standard Time'-7) and j.Status='Completed' and (a.Performer in(Select * from string_split(?,',')) or a.GroupOrDeptName in(Select * from string_split(?,','))) and j.TenantId=? )t )t";		
		Map<String,Integer> approvedjobCount=new LinkedHashMap<>();//map to store approved jobs based on date
		jdbcTemplate.query(approved, new RowCallbackHandler() {

			@Override
			public void processRow(ResultSet rs) throws SQLException {
				// TODO Auto-generated method stub
				try {
					do{
						String date=rs.getString("date");//variable to hold date
						int value=rs.getInt("value");//variable to store the number of approved jobs on that date
						approvedjobCount.put(date, value);//put the value on the map
					}while(rs.next()) ;
				}catch(Exception e) {
					e.printStackTrace();
				}
			}

		},performers,performers,tenantId);
		//Query to find the rejected jobs based on date
		String rejected="SELECT DISTINCT datepart(day,DATE) AS DATE,COUNT(DATE) OVER(PARTITION BY DATE) AS VALUE FROM ( SELECT  * from( Select distinct j.[JobId],j.[JobName],j.[Status],CONVERT(DATE,j.ScheduleStart) AS DATE FROM [dbo].[JobDetails] j inner join [dbo].[JobActivityDetails] a on j.JobId=a.JobId where Convert(date,ScheduleStart)<=Convert(date,SYSDATETIMEOFFSET() AT TIME ZONE 'India Standard Time') AND Convert(date,ScheduleStart)>=Convert(date,SYSDATETIMEOFFSET() AT TIME ZONE 'India Standard Time'-7) and j.Status='Rejected' and (a.Performer in(Select * from string_split(?,',')) or a.GroupOrDeptName in(Select * from string_split(?,','))) and j.TenantId=? )t )t";
		Map<String,Integer> rejectedjobCount=new LinkedHashMap<>();//Map to store rejected jobs based on date
		jdbcTemplate.query(rejected, new RowCallbackHandler() {//Execute the query

			@Override
			public void processRow(ResultSet rs) throws SQLException {
				// TODO Auto-generated method stub
				try {
					do {
						String date=rs.getString("date");//Store the date
						int value=rs.getInt("value");//Store the value based on the date
						rejectedjobCount.put(date, value);//Put the values into the map
					}while(rs.next());
				}catch(Exception e) {
					e.printStackTrace();
				}
			}

		},performers,performers,tenantId);
		//Query to find today's activities
		String todaysActivities="SELECT SUM(CASE WHEN completionstatus='ontime' then 1 else 0 end) as ontime,SUM(CASE WHEN completionstatus='late' then 1 else 0 end) as late from(SELECT  *,case when(ActualStart<=ScheduledStart) then 'ontime' else 'late' end AS completionstatus from( Select  j.[JobId],j.[JobName],j.[Status],a.ActivityId,a.ScheduledStart,a.ActualStart FROM [dbo].[JobDetails] j inner join [dbo].[JobActivityDetails] a on j.JobId=a.JobId where Convert(date,ScheduleStart)=Convert(date,SYSDATETIMEOFFSET() AT TIME ZONE 'India Standard Time') and (a.Performer in(Select * from string_split(?,',')) or a.GroupOrDeptName in(Select * from string_split(?,','))) and j.TenantId=? )t )T";
		Map<String,Integer> todaysactivities=new LinkedHashMap<>();//Map to store activities count based on whether they are approved, rejected or on time
		jdbcTemplate.query(todaysActivities, new RowCallbackHandler() {

			@Override
			public void processRow(ResultSet rs) throws SQLException {
				// TODO Auto-generated method stub
				try {
					do {
						int ontime=rs.getInt("ontime");//Variable to store the number of activities which are on time
						int late=rs.getInt("late");//Variable to store the number of activities which are late
						todaysactivities.put("ontime", ontime);//Update the map
						todaysactivities.put("late", late);
					}while(rs.next());
				}catch(Exception e) {
					e.printStackTrace();
				}
			}

		},performers,performers,tenantId);

		String approvedActivities="SELECT SUM(CASE WHEN completionstatus='ontime' then 1 else 0 end) as ontime,SUM(CASE WHEN completionstatus='late' then 1 else 0 end) as late from(SELECT  *,case when(ActualStart<=ScheduledStart) then 'ontime' else 'late' end AS completionstatus from( Select  j.[JobId],j.[JobName],j.[Status],a.ActivityId,a.ScheduledStart,a.ActualStart FROM [dbo].[JobDetails] j inner join [dbo].[JobActivityDetails] a on j.JobId=a.JobId where Convert(date,ScheduleStart)<=Convert(date,SYSDATETIMEOFFSET() AT TIME ZONE 'India Standard Time') AND Convert(date,ScheduleStart)>=Convert(date,SYSDATETIMEOFFSET() AT TIME ZONE 'India Standard Time'-7)  and j.Status='Completed' and (a.Performer in(Select * from string_split(?,',')) or a.GroupOrDeptName in(Select * from string_split(?,','))) and j.TenantId=? )t )T";
		Map<String,Integer> approvedactivities=new LinkedHashMap<>();//Map to store approved activities count based on ontime and late
		jdbcTemplate.query(approvedActivities, new RowCallbackHandler() {

			@Override
			public void processRow(ResultSet rs) throws SQLException {
				// TODO Auto-generated method stub
				try {
					do {
						int ontime=rs.getInt("ontime");//Variable to store the activities which are ontime
						int late=rs.getInt("late");//Variable to store the activities which are late
						approvedactivities.put("ontime", ontime);//Update the map
						approvedactivities.put("late", late);
					}while(rs.next());
				}catch(Exception e) {
					e.printStackTrace();
				}
			}

		},performers,performers,tenantId);

		String rejectedActivities="SELECT SUM(CASE WHEN completionstatus='ontime' then 1 else 0 end) as ontime,SUM(CASE WHEN completionstatus='late' then 1 else 0 end) as late from(SELECT  *,case when(ActualStart<=ScheduledStart) then 'ontime' else 'late' end AS completionstatus from( Select  j.[JobId],j.[JobName],j.[Status],a.ActivityId,a.ScheduledStart,a.ActualStart FROM [dbo].[JobDetails] j inner join [dbo].[JobActivityDetails] a on j.JobId=a.JobId where Convert(date,ScheduleStart)<=Convert(date,SYSDATETIMEOFFSET() AT TIME ZONE 'India Standard Time') AND Convert(date,ScheduleStart)>=Convert(date,SYSDATETIMEOFFSET() AT TIME ZONE 'India Standard Time'-7)  and j.Status='Rejected' and (a.Performer in(Select * from string_split(?,',')) or a.GroupOrDeptName in(Select * from string_split(?,','))) and j.TenantId=? )t )T";
		Map<String,Integer> rejectedactivities=new LinkedHashMap<>();//Map to store rejected activities based on ontime and late
		jdbcTemplate.query(rejectedActivities, new RowCallbackHandler() {

			@Override
			public void processRow(ResultSet rs) throws SQLException {
				// TODO Auto-generated method stub
				try {
					do {
						int ontime=rs.getInt("ontime");//variable to store activities which are ontime
						int late=rs.getInt("late");//variable to store activities which are late
						rejectedactivities.put("ontime", ontime);//Update the values on the map
						rejectedactivities.put("late", late);
					}while(rs.next());
				}catch(Exception e) {
					e.printStackTrace();
				}
			}

		},performers,performers,tenantId);
		//Query to find the counts of completed ,rejected and not started jobs
		String jobsql="Select sum(case when status='completed' then 1 else 0 end) as Approved ,sum(case when status='rejected' then 1 else 0 end) as Rejected ,sum(case when status='Not Started' then 1 else 0 end) as NotStarted from( SELECT  distinct j.JobId,j.Status FROM [dbo].JobDetails J INNER JOIN [dbo].JobActivityDetails A ON J.JobId=A.JobId where Convert(date,ScheduleStart)<=Convert(date,SYSDATETIMEOFFSET() AT TIME ZONE 'India Standard Time') AND Convert(date,ScheduleStart)>=Convert(date,SYSDATETIMEOFFSET() AT TIME ZONE 'India Standard Time'-7) and (a.Performer in(Select * from string_split(?,',')) or a.GroupOrDeptName in(Select * from string_split(?,','))) and j.TenantId=? )t";
		Map<String,Integer> jobCountBasedOnStatus=new LinkedHashMap<>();
		jdbcTemplate.query(jobsql, new RowCallbackHandler() {

			@Override
			public void processRow(ResultSet rs) throws SQLException {
				// TODO Auto-generated method stub
				try {
					do {
						int approved=rs.getInt("Approved");//Store count of number of approved jobs
						int rejected=rs.getInt("Rejected");//Store count of number of rejected jobs
						int notstarted=rs.getInt("NotStarted");//Store count of number of not started jobs
						jobCountBasedOnStatus.put("approved", approved);//Update the map
						jobCountBasedOnStatus.put("rejected", rejected);
						jobCountBasedOnStatus.put("notstarted", notstarted);
					}while(rs.next());
				}catch(Exception e) {
					e.printStackTrace();
				}
			}

		},performers,performers,tenantId);
		//Query to find the counts of completed ,rejected and not started activities
		String activitysql="Select sum(case when status='completed' then 1 else 0 end) as Approved ,sum(case when status='rejected' then 1 else 0 end) as Rejected ,sum(case when status='Not Started' then 1 else 0 end) as NotStarted from( SELECT  distinct a.ActivityId,a.Status FROM [dbo].JobDetails J INNER JOIN [dbo].JobActivityDetails A ON J.JobId=A.JobId where Convert(date,ScheduleStart)<=Convert(date,SYSDATETIMEOFFSET() AT TIME ZONE 'India Standard Time') AND Convert(date,ScheduleStart)>=Convert(date,SYSDATETIMEOFFSET() AT TIME ZONE 'India Standard Time'-7) and (a.Performer in(Select * from string_split(?,',')) or a.GroupOrDeptName in(Select * from string_split(?,','))) and j.TenantId=? )t";
		Map<String,Integer> activityCountBasedOnStatus=new LinkedHashMap<>();
		jdbcTemplate.query(activitysql, new RowCallbackHandler() {

			@Override
			public void processRow(ResultSet rs) throws SQLException {
				// TODO Auto-generated method stub
				try {
					do {
						int approved=rs.getInt("Approved");//Store count of number of approved activities
						int rejected=rs.getInt("Rejected");//Store count of number of rejected activities
						int notstarted=rs.getInt("NotStarted");//Store count of number of not started activities
						activityCountBasedOnStatus.put("approved", approved);//Update the map
						activityCountBasedOnStatus.put("rejected", rejected);
						activityCountBasedOnStatus.put("notstarted", notstarted);
					}while(rs.next());
				}catch(Exception e) {
					e.printStackTrace();
				}
			}

		},performers,performers,tenantId);
		//Query to find the count of critical, normal and low priority jobs
		String criticalsql="select sum(case when Priority='Critical' then 1 else 0 end) as critical ,sum(case when Priority='Normal' then 1 else 0 end) as normal ,sum(case when Priority='Low' then 1 else 0 end) as low from (Select distinct priority,j.JobId FROM [dbo].JobDetails J INNER JOIN [dbo].JobActivityDetails A ON J.JobId=A.JobId where Convert(date,ScheduleStart)<=Convert(date,SYSDATETIMEOFFSET() AT TIME ZONE 'India Standard Time') AND Convert(date,ScheduleStart)>=Convert(date,SYSDATETIMEOFFSET() AT TIME ZONE 'India Standard Time'-7) and j.Status='Completed' and (a.Performer in(Select * from string_split(?,',')) or a.GroupOrDeptName in(Select * from string_split(?,','))) and j.TenantId=? )t";
		Map<String,Integer> criticalitycount=new LinkedHashMap<>();//map to store the count of those jobs
		jdbcTemplate.query(criticalsql, new RowCallbackHandler() {

			@Override
			public void processRow(ResultSet rs) throws SQLException {
				// TODO Auto-generated method stub
				try {
					do {
						int critical=rs.getInt("critical");//store the count of critical jobs
						int normal=rs.getInt("normal");//store the count of normal priority jobs
						int low=rs.getInt("low");//store the count of low priority jobs
						criticalitycount.put("critical", critical);//Update the map
						criticalitycount.put("normal", normal);
						criticalitycount.put("low", low);
					}while(rs.next());
				}catch(Exception e) {
					e.printStackTrace();
				}
			}

		},performers,performers,tenantId);


		Map<String,Map<String,Integer>> details=new LinkedHashMap<>();//map to store the results found in the above piece of code
		details.put("approved", approvedjobCount);//add the map which holds the count of approved jobs based on date
		details.put("rejected", rejectedjobCount);//add the map which holds the count of rejected jobs based on date
		details.put("pending", pendingjobCount);//add the map which holds the count of pending jobs based on date
		details.put("todaysactivities", todaysactivities);//add the map which holds the details of today's activities
		details.put("rejectedactivities", rejectedactivities);//add the map which holds the details of rejected activities
		details.put("approvedactivities", approvedactivities);//add the map which holds the details of approved activities
		details.put("jobCountBasedOnStatus", jobCountBasedOnStatus);//add the map which holds the job count based on status
		details.put("activityCountBasedOnStatus", activityCountBasedOnStatus);//add the map which holds the activity count based on status
		details.put("criticalitycount", criticalitycount);//add the map which hold the job count based on criticality
		return details;

	}
	/*
	 * Service to update activity status and actual stop
	 * Author: Sreepiya Roy
	 * */
	@Override
	public void updateActivityStatus(ActivityDetails activity,String tenantId) {
		JdbcTemplate jdbcTemplate=jdbcTemplates.get(tenantId).get(0);
		// Prepare the SQL update statement
		String sql = "UPDATE [dbo].[JobActivityDetails] " +
				"SET ActualStop =?, Status = ? " +
				"WHERE JobId = ? AND TenantId = ? AND ActivityId = ?";
		//	    JdbcTemplate jdbcTemplate = dynamicDatasourceDAO.getDynamicConnection(activity.get("tenantid"))
		//				 .get(op360_key);

		jdbcTemplate.update(sql,
				activity.getActualActivityEndTime(),
				activity.getActvityStatus(),
				activity.getJobId(),
				tenantId,
				activity.getActivityId()
				);//Execute the query
	}



	/*
	 * Service to find all possible information of a job based on its jobid
	 * Author: Sreepriya Roy
	 * */
	@Override
	public JobDetails getJobDetailsBasedOnJobId(String jobid,String tenantid) {
		// SQL query with all selected parameters
		String query = "SELECT weekdays, JobName, Job.JobId,Job.Assigner,Job.Approver,Job.ScheduleStart,Job.ScheduleStop,Job.ActualStart,Job.ActualStop,Job.ReviewerIntimationTime,Job.ReviewercompletionTime, Job.Priority,Job.GroupId,Job.Status, (CASE WHEN ReviewerIntimationTime!=null then (case when Reviewercompletiontime!=null then datediff(day,Job.ReviewercompletionTime,Job.ReviewerIntimationTime) else (datediff(day,SYSDATETIMEOFFSET() AT TIME ZONE 'India Standard Time',Job.ReviewerIntimationTime)) end) else 0 end) as AwaitingTime, Job.remarks,(Select sum(case when status='Completed' then 1 else 0 end)*100/count(status) from [dbo].JobActivityDetails act where act.JobId=Job.JobId) as progress, (SELECT STRING_AGG([ActivityName], ', ') FROM [dbo].[ActivityCreation] act where act.TaskId=Job.Taskid) as ActivityList, (Select STRING_AGG(Case when PerformerType='Performer' then Performer else GroupOrDeptName end,',') from [dbo].JobActivityDetails act where act.JobId=job.JobId) as Performerlist, (SELECT STRING_AGG([FileName], ', ') FROM [dbo].[JobActivityDetails] act where act.TaskId=Job.Taskid) as FileName,(Select Top(1) ActivityName=(Select ActivityName from  [dbo].ActivityCreation ac where ac.ActivityId=Act.ActivityId and ac.TaskId=Act.TaskId)  from dbo.JobActivityDetails act where Job.JobId=act.Jobid and act.Status='Not Started') as CurrentActivity, (SELECT STRING_AGG([AssetName], ', ') FROM [dbo].[JobActivityDetails] act where act.TaskId=Job.Taskid) as Instrument FROM dbo.JobDetails Job where JobId=?";
		JdbcTemplate jdbcTemplate=jdbcTemplates.get(tenantid).get(0);
		// Map to hold job details
		JobDetails jobDetails = new JobDetails();
		//	    JdbcTemplate jdbcTemplate = dynamicDatasourceDAO.getDynamicConnection(tenantid) //tenantId is missing 
		//				 .get(op360_key);

		// Query execution
		jdbcTemplate.query(query, new RowCallbackHandler() {
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				try {
					SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");   //Parse date in the given format
					jobDetails.setJobName(rs.getString("JobName"));//property to set jobname
					jobDetails.setJobID(rs.getString("JobId"));//property to set jobid
					jobDetails.setAssigner( rs.getString("Assigner"));//property to set assigner
					jobDetails.setApprover( rs.getString("Approver"));//property to set approver
					Date scheduledstart=rs.getString("ScheduleStart")!=null?format.parse(rs.getString("ScheduleStart")):null;//variable to store formatted scheduled start
					Date scheduledstop=rs.getString("ScheduleStop")!=null?format.parse(rs.getString("ScheduleStop")):null; //variable to store formatted scheduled stop
					Date actualstart=rs.getString("ActualStart")!=null?format.parse(rs.getString("ActualStart")):null; //variable to store formatted actual start
					Date actualstop=rs.getString("ActualStop")!=null?format.parse(rs.getString("ActualStop")):null; //variable to store formatted actual stop
					Date reviewerintimationtime=rs.getString("ReviewerIntimationTime")!=null?format.parse(rs.getString("ReviewerIntimationTime")):null; //variable to store formatted Reviewer Intimation Time

					jobDetails.setScheduledJobStartTime(scheduledstart);
					jobDetails.setScheduledJobEndTime(scheduledstop);
					jobDetails.setActualJobStartTime(actualstart);
					jobDetails.setActualJobEndTime( actualstop);
					jobDetails.setReviewerIntimationTime(reviewerintimationtime);
					jobDetails.setPriority(rs.getString("Priority"));//property to store priority of the job
					jobDetails.setGroupId( rs.getString("GroupId"));//property to store groupid of the job
					jobDetails.setJobStatus( rs.getString("Status"));//property to store status of the job
					jobDetails.setAwatingTime( rs.getString("AwaitingTime"));//property to store the awaiting time of the job
					jobDetails.setRemarks(rs.getString("remarks"));//property to store the remarks of the job
					jobDetails.setProgress( rs.getInt("progress"));//property to store the progress done in the job
					jobDetails.setActivitylists( rs.getString("ActivityList"));//property to store the activity list in the job
					jobDetails.setPerformerLists(rs.getString("PerformerList"));//property to store the performer list in the job
					jobDetails.setFileName( rs.getString("FileName"));//property to store the file name
					jobDetails.setCurrentActivityName(rs.getString("CurrentActivity"));//property to store the current activity
					jobDetails.setInstrument(rs.getString("Instrument"));//property to store the assetlist
					jobDetails.setWeekdays(rs.getInt("weekdays"));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, jobid);

		return jobDetails;
	}

	/*
	 * Service to update the job status
	 * Author: Sreepriya Roy
	 * */

	@Override
	public void updateJobStatus(JobDetails job,String tenantid) {
		// TODO Auto-generated method stub
		//Query to update the job status
		String sql="declare @isactivitiesCompleted bit; declare @jobid varchar(max)=?; select @isactivitiesCompleted=case when count(JobId)=sum(case when status='Completed' then 1 else 0 end) then 1 else 0 end from dbo.JobActivityDetails where JobId=@jobid update dbo.JobDetails set ReviewerIntimationTime=?,ActualStop=? , Status='Sent For Approval' where JobId=@jobid  and @isactivitiesCompleted=1";
		//		JdbcTemplate jdbcTemplate = dynamicDatasourceDAO.getDynamicConnection(tenantid)//tenantId is missing
		//				 .get(op360_key);
		JdbcTemplate jdbcTemplate=jdbcTemplates.get(tenantid).get(0);
		jdbcTemplate.update(sql,
				job.getJobID(),
				job.getActualJobEndTime(),
				job.getActualJobEndTime()
				);//Query execution
	}
	/*
	 * Service to update the bit through which sequence is bypassed
	 * Author: Sreepriya Roy 
	 * */
	@Override
	public void enableActivitiesBypassingSequence(String jobid,String activityid,String tenantid) {
		String sql="update dbo.JobActivityDetails set enableStart=1 where JobId=? and ActivityId=?";
		JdbcTemplate jdbcTemplate=jdbcTemplates.get(tenantid).get(0);
		jdbcTemplate.update(sql,jobid,activityid);
	}
	/*
	 * Service to fetch historical jobs based on a performer
	 * Author: Sreepriya Roy
	 */
	@Override
	public List<ActivityDetails> getHistoricalActivitiesBasedOnPerformer(String currentPerformerGroups,String tenantId,Date fromDate,Date toDate)
	{
		JdbcTemplate jdbcTemplate=jdbcTemplates.get(tenantId).get(0);
		//Variable to store the list of jobs required by a performer
		List<ActivityDetails> historicalActivities = new LinkedList<ActivityDetails>();


		String sql = "DECLARE @performergroup varchar(max)=?; Select *,JobName=(Select JobName from dbo.JobDetails where JobId=JA.JobId) from dbo.JobActivityDetails JA left join dbo.ActivityCreation Act  on Act.ActivityId=JA.ActivityId  AND JA.TaskId=Act.TaskId  where Status in ('Completed','Aborted') and ActualStart between ? and ?";
		//		JdbcTemplate jdbcTemplate = dynamicDatasourceDAO.getDynamicConnection(tenantId)
		//				 .get(op360_key);
		// Execute the query to fetch job details, using a RowCallbackHandler to process results

		jdbcTemplate.query(sql, new RowCallbackHandler()
		{	
			@Override
			public void processRow(ResultSet rsAct) throws SQLException {
				// TODO Auto-generated method stub
				SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");   //variable to format date

				//int count=0;
				try
				{
					do
					{

						ActivityDetails activityDetails = new ActivityDetails(); // Object to store current activity details

						// Set the properties for the current activity
						activityDetails.setActivityId(rsAct.getString("ActivityId")); // Set the ID of the current activity
						activityDetails.setActivityName(rsAct.getString("ActivityName")); // Set the name of the current activity

						// Parse and set the scheduled start and end times for the current activity
						Date startDA = format.parse(rsAct.getString("ScheduledStart")); // Variable to hold scheduled start time
						Date stopDA = format.parse(rsAct.getString("ScheduleStop")); // Variable to hold scheduled end time
						activityDetails.setScheduledActivityStartTime(startDA); // Set the scheduled start time
						activityDetails.setScheduledActivityEndTime(stopDA); // Set the scheduled end time
						activityDetails.setEnforceStart(rsAct.getBoolean("enforceStart"));
						// Set actual start and end times, handling potential null values
						activityDetails.setActualActvtyStrt(rsAct.getString("ActualStart") == null ? "-" : rsAct.getString("ActualStart")); // Set actual start time or placeholder
						activityDetails.setActualActvtEnd(rsAct.getString("ActualStop") == null ? "-" : rsAct.getString("ActualStop")); // Set actual end time or placeholder
						activityDetails.setActualActivityStartTime(rsAct.getString("ActualStart") == null ? null : format.parse(rsAct.getString("ActualStart"))); // Parse and set actual start time
						activityDetails.setActualActivityEndTime(rsAct.getString("ActualStop") == null ? null : format.parse(rsAct.getString("ActualStop"))); // Parse and set actual end time

						// Set other properties related to the activity
						activityDetails.setPerformerType(rsAct.getString("PerformerType")); // Set the type of performer for the activity
						activityDetails.setGroupOrDeptName(rsAct.getString("GroupOrDeptName")); // Set the name of the group or department associated with the activity
						activityDetails.setActvityStatus(rsAct.getString("Status")); // Set the status of the activity
						activityDetails.setRemarks(rsAct.getString("Remarks")); // Set any remarks related to the activity
						activityDetails.setAssetId(rsAct.getString("AssetID")); // Set the ID of the associated asset
						activityDetails.setEnforce(rsAct.getBoolean("enforce")); // Set whether the activity is enforced
						activityDetails.setAssetName(rsAct.getString("AssetName")); // Set the name of the associated asset

						// Set job ID and sequence for the activity
						activityDetails.setJobId(rsAct.getString("JobId")); // Set the job ID for the current activity
						activityDetails.setSequence(rsAct.getInt("sequence")); // Set the sequence number for the activity
						activityDetails.setLogbook(rsAct.getString("LogbookName")); // Set the name of the logbook associated with the activity
						activityDetails.setPerformer(rsAct.getString("Performer")); // Set the name of the performer for the activity
						activityDetails.setApprover(rsAct.getString("Approver")); // Set the name of the approver for the activity

						// Set additional activity-related properties
						activityDetails.setActFile(rsAct.getString("FileName")); // Set the name of any associated activity file
						activityDetails.setBuffer(rsAct.getInt("buffer")); // Set the buffer time for the activity
						activityDetails.setDelayDueToBuffer("-"); // Placeholder for any delay due to buffer, initially set to "-"
						activityDetails.setActivityFileData(rsAct.getBytes("FileContent")); // Set the binary content of any associated activity file
						activityDetails.setGroupOrDept(rsAct.getBoolean("isGroupOrDept")); // Set whether the activity is associated with a group or department
						activityDetails.setEnableStart(rsAct.getBoolean("enableStart"));
						activityDetails.setJobName(rsAct.getString("JobName"));
						// Add the populated ActivityDetails object to the list of activities for the current job
						historicalActivities.add(activityDetails); // Add the activity details to the activity list



					}while(rsAct.next());
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		},currentPerformerGroups,fromDate,toDate);
		return historicalActivities; 	
	}
	
	@Override
	public Object sendMail(String tenantId,String token,String jobId) {
		
		RestTemplate rest = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    headers.setBearerAuth(token);
	    String uri=usermoduleurl+"/users/groupsByUser/"+tenantId;
		   
	    HttpEntity<Map<String, Object>> request = new HttpEntity<>(headers);
	        // Use Map.class to get a JSON -> Map response (or String.class if the service returns plain text)
	    // Make the PUT request to the API
	    ResponseEntity<Object> response = rest.exchange(uri, HttpMethod.GET, request, Object.class);
	    Map<String,List> groupDetails=(Map<String, List>) response.getBody();
	    
	    System.out.println("Group details=>"+groupDetails.toString());
		String mailsql="DECLARE @jobid VARCHAR(max)=?; select * from( SELECT jobid, activityid,jobname=(select jobname from JobDetails J where J.JobId=T1.JobId), performer,approver, groupordeptname,ActivityName, isgroupordept,enableActivityPerformance,Status,Sum(CASE WHEN status = 'Not Started' THEN 1 ELSE 0 END) over() notstartedcount, Count(activityid) over() totalcount FROM   (SELECT enableActivityPerformance= (SELECT CASE WHEN NOT EXISTS (SELECT * FROM   dbo.jobactivitydetails A WHERE  status != 'Completed' AND status != 'Aborted' AND A.jobid = JA.jobid AND A.activityid IN (SELECT sourceid FROM   dbo.activityconnections AC WHERE  targetid = JA.activityid)) OR enablestart = 1 THEN 1 ELSE 0 END), JA.jobid, JA.activityid, JA.performer,JA.Approver, JA.isgroupordept, JA.groupordeptname, Act.ActivityName, Act.sequence, ja.status FROM   dbo.jobactivitydetails JA LEFT JOIN dbo.activitycreation Act ON Act.activityid = JA.activityid AND JA.taskid = Act.taskid WHERE  jobid = @jobid AND status != 'Completed' AND status != 'Aborted' ) T1 WHERE  enableactivityperformance = 1 GROUP  BY performer, groupordeptname, activityid,activityname, jobid ,isGroupOrDept,status,enableActivityPerformance,approver )t where notstartedcount=totalcount";
	    
		JdbcTemplate jdbcTemplate=jdbcTemplates.get(tenantId).get(0);
		
		jdbcTemplate.query(mailsql, new RowCallbackHandler() {
			
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				// TODO Auto-generated method stub
				// If you have a dynamic app URL, set it here (replace with actual value)
				String uri = mail+"/mail/notifications";
				String jobId=rs.getString("JobId");
				String jobName=rs.getString("JobName");
				String activityId=rs.getString("ActivityId");
				String activityName=rs.getString("ActivityName");
			     String approver=rs.getString("Approver");
			    String groupOrDeptName=rs.getString("groupOrDeptName");
			    List<String> toMailIds = java.util.Collections.singletonList(rs.getString("Performer"));
			    System.out.println("gROUP OR DEPT NAME:"+groupOrDeptName);
			    if(groupOrDeptName!=null&& !groupOrDeptName.isEmpty())
			    {
			    	 toMailIds = groupDetails.entrySet().stream()
			    		    .filter(e -> e.getKey().equalsIgnoreCase(groupOrDeptName))
			    		    .map(Map.Entry::getValue)
			    		    .findFirst()
			    		    
			    		    .orElse(List.of());
			    	 
			    }
			    
			    String performer=rs.getString("performer");
			    System.out.println("To mail ids:"+toMailIds);
			    // NOTE: keep long HTML in a resource/template file in production; kept inline for clarity
			    String htmlPage = String.format("""
			    		<html lang="en" xmlns:o="urn:schemas-microsoft-com:office:office" xmlns:v="urn:schemas-microsoft-com:vml">
			    		<head>
			    		<title>Activity Perform</title>
			    		<meta content="text/html; charset=utf-8" http-equiv="Content-Type" />
			    		<meta content="width=device-width, initial-scale=1.0" name="viewport" />
			    		<link href="https://fonts.googleapis.com/css?family=Cabin" rel="stylesheet" type="text/css" />
			    		<script src="https://kit.fontawesome.com/823adb53b6.js" crossorigin="anonymous"></script>
			    		</head>

			    		<body style="background-color: #f2f2f2; margin: 0; padding: 0;">
			    		    <table border="0" cellpadding="0" cellspacing="0" width="100%%" style="background-color: #f2f2f2;">
			    		        <tr>
			    		            <td align="center">
			    		                <table width="680" style="background-color: #fbfbfb;">
			    		                    <tr>
			    		                        <td style="padding: 10px;">
			    		                            <h2>Operations360</h2>
			    		                        </td>
			    		                        <td align="right">
			    		                            <img src="https://raw.githubusercontent.com/pranab-greenwave/pranab-greenwave/main/logo.png"
			    		                                 width="50" style="display:block;" />
			    		                        </td>
			    		                    </tr>
			    		                </table>

			    		                <table width="680" style="background-color: #fbfbfb; margin-top: 10px;">
			    		                    <tr>
			    		                        <td style="padding: 25px;">
			    		                            <h3 style="margin-bottom: 5px;">Activity Perform</h3>
			    		                            <p>You have an Activity to Perform</p>
			    		                            <p style="background:#e4e3f4;padding:5px;border-radius:10px;width:fit-content;">%s</p>

			    		                            <a href="%s/workflow/performer" target="_blank"
			    		                               >
			    		                                Operations360
			    		                            </a>
			    		                        </td>
			    		                        
			    		                    </tr>
			    		                </table>

			    		                <table width="680" style="background-color: #fbfbfb; margin-top: 10px;">
			    		                    <tr>
			    		                        <td align="center">
			    		                            <h4>Job ID</h4>
			    		                            <p>%s</p>
			    		                        </td>
			    		                        <td align="center">
			    		                            <h4>Job Name</h4>
			    		                            <p>%s</p>
			    		                        </td>
			    		                        <td align="center">
			    		                            <h4>Activity ID</h4>
			    		                            <p>%s</p>
			    		                        </td>
			    		                        <td align="center">
			    		                            <h4>Activity Name</h4>
			    		                            <p>%s</p>
			    		                        </td>
			    		                    </tr>
			    		                </table>

			    		                <table width="680" style="background-color: #fbfbfb; margin-top: 10px;">
			    		                    <tr>
			    		                        <td>
			    		                            <h4>User or Group To Perform</h4>
			    		                            <ul>
			    		                                <li>%s</li>
			    		                            </ul>
			    		                        </td>
			    		                    </tr>
			    		                </table>
			    		            </td>
			    		        </tr>
			    		    </table>
			    		</body>
			    		</html>
			    		""", approver, op360url, jobId, jobName, activityId, activityName, (performer!=null&&!performer.isEmpty())?performer:groupOrDeptName);

			    // Receiver(s)
			    System.out.println("mAIL TO BE SENT=>"+toMailIds.toString());
			    String subject = "OP360";

			    // Build payload
			    Map<String, Object> emailObj = new HashMap<>();
			    emailObj.put("toMailIds", toMailIds);
			    emailObj.put("messageSubject", subject);
			    emailObj.put("htmlPage", htmlPage);

			    RestTemplate rest = new RestTemplate();

			    HttpHeaders headers = new HttpHeaders();
			    headers.setContentType(MediaType.APPLICATION_JSON);
			    headers.setBearerAuth(token);

			    HttpEntity<Map<String, Object>> request = new HttpEntity<>(emailObj, headers);

			        // Use Map.class to get a JSON -> Map response (or String.class if the service returns plain text)
			     rest.postForEntity(uri, request, Boolean.class);
			   
			}
		},jobId);
		System.out.println("mAIL SENT SUCCESSFUL");
	    return "Mail Sent Successfully";
	    
	}

	



}
