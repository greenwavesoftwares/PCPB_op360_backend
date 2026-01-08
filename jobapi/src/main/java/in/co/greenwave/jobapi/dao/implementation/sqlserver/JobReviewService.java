package in.co.greenwave.jobapi.dao.implementation.sqlserver;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import in.co.greenwave.jobapi.dao.JobReviewDAO;
import in.co.greenwave.jobapi.model.ActivityDetails;
import in.co.greenwave.jobapi.model.ApproverCardData;
import in.co.greenwave.jobapi.model.JobDetails;
import in.co.greenwave.jobapi.model.TaskDetail;
import in.co.greenwave.jobapi.model.TenantConfigDetails;
import in.co.greenwave.jobapi.utility.JdbcUrlUtil;

@Repository
public class JobReviewService implements JobReviewDAO {
	/**
	 * For Database Configuration : Refer JobApiConfiguration class & application.properties
	 */
	
	@Autowired
	@Qualifier("jdbcTemplate_OP360_tenant")
	private  JdbcTemplate jdbcTemplateOp360Tenant;

	@Autowired
	@Qualifier("DatasourceCollections")
	private  Map<String, List<JdbcTemplate>> jdbcTemplates; // Template for operations on the main database
	
	/**
	 * Update JobDetails based on JobId
	 */
	@Override
	public void updateApprovalDetails(String jobId, String remarks, String action,String tenantId) {
		JdbcTemplate jdbcTemplate=jdbcTemplates.get(tenantId).get(0);
		String query="UPDATE [dbo].[JobDetails] SET Remarks = ? , ReviewerAction = ? , [ReviewercompletionTime] = SYSDATETIMEOFFSET() AT TIME ZONE 'India Standard Time',Status = 'Finished' Where JobId = ? AND [TenantId]=?";
		jdbcTemplate.update(query,remarks,action,jobId,tenantId);
	}
	/**
	 * First a Query is fired to fetch the JobDetails with respect to the Approver(i.e user) & 
	 * With respect to each JobID , the list of ActivityDetails is fetched through actSql  and finally added to the JobDetails object is added to the list
	 */
	
	/**
	 * Fetch Jobs to review based on user and tenantId
	 * @author SreepriyaRoy
	 */
	@Override
	public List<JobDetails> pendingForReviewJobs(String user, String tenantId) {
    List<JobDetails> jobs = new LinkedList<JobDetails>(); // Initialize a list to store job details
    JdbcTemplate jdbcTemplate=jdbcTemplates.get(tenantId).get(0);
    // Query to find the jobs based on a job reviewer
    String sql = "SELECT [TaskId], [JobId], [JobName], [Assigner], [TenantId], [GroupId], [Instrument], [Approver], [ScheduleStart], [ScheduleStop], [ActualStart], [ActualStop], [ReviewerIntimationTime], [ReviewercompletionTime], [Priority], [Repeat], [RepeatTill], [Remarks], [Status], [Weekdays] FROM [dbo].[JobDetails] WHERE Approver = ? AND [Status] <> 'Completed' AND status <> 'Rejected' AND status <> 'Aborted' AND [TenantId]=?";

    // Query to find the activities of a given job based on job ID
    String actSql = "SELECT [JobId], [isGroupOrDept], [enforce], [FileName], [ActualStart], [ActualStop], PerformerType, GroupOrDeptName, job.[ActivityId], job.[TaskId], job.[TenantId], job.[AssetID], job.[AssetName], ActivityName, [Sequence], [LogbookName], [Performer], [Approver], [ScheduledStart], [ActivityReviewerIntimationTime], [ActivityReviewercompletionTime], [ScheduleStop], [ActualStart], [ActualStop], [Remarks],[enableStart] ,[FileContent], [Status], Duration_min, [Asset] = (SELECT Asset FROM [dbo].[ActivityCreation] cr WHERE cr.TaskId = act.TaskId AND cr.ActivityId = act.ActivityId),[Pos_X] = (SELECT [Pos_X] FROM [dbo].[ActivityCreation] cr WHERE cr.TaskId = act.TaskId AND cr.ActivityId = act.ActivityId),[Pos_Y] = (SELECT [Pos_Y] FROM [dbo].[ActivityCreation] cr WHERE cr.TaskId = act.TaskId AND cr.ActivityId = act.ActivityId) FROM [dbo].[JobActivityDetails] job, dbo.ActivityCreation act WHERE job.ActivityId = act.ActivityId AND job.TaskId = act.TaskId AND [JobId] = ? AND act.[TenantId]=? ORDER BY [Sequence]";

    // Execute the query using RowCallbackHandler
    jdbcTemplate.query(sql, new RowCallbackHandler() {
        @Override
        public void processRow(ResultSet rs) throws SQLException {
            try {
                do {
                    JobDetails jobD = new JobDetails(); // Object to store the information of the current job
                    TaskDetail taskD = new TaskDetail(); // Object to store task details
                    
                    // Set task ID from the result set
                    taskD.setTaskId(rs.getString("TaskId")); // Store the Task ID
                    
//                  taskD.setTaskName(rs.getString("TaskName")); // (Commented out) Set task name if needed
                    
                    // Set job details from the result set
                    jobD.setJobName(rs.getString("JobName")); // Store job name
                    jobD.setTask(taskD); // Link the task to the job
                    jobD.setJobID(rs.getString("JobId")); // Store job ID
                    jobD.setGroupId(rs.getString("GroupId")); // Store group ID
                    jobD.setApprover(rs.getString("Approver")); // Store approver
                    
                    // Parse scheduled start and stop times
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // Date format
                    Date StrtD = format.parse(rs.getString("ScheduleStart")); // Parse scheduled start date
                    Date stpD = format.parse(rs.getString("ScheduleStop")); // Parse scheduled stop date
                    
                    // Set scheduled times in job details
                    jobD.setScheduledJobStartTime(StrtD); // Set scheduled job start time
                    jobD.setScheduledJobEndTime(stpD); // Set scheduled job end time
                    
                    // Set priority and status
                    jobD.setPriority(rs.getString("Priority")); // Store job priority
                    jobD.setJobStatus(rs.getString("Status")); // Store job status
                    
                    // Set reviewer times with null checks
                    jobD.setReviewerIntimationTime(rs.getString("ReviewerIntimationTime") != null ? format.parse(rs.getString("ReviewerIntimationTime")) : null); // Parse reviewer intimation time
                    jobD.setAssigner(rs.getString("Assigner")); // Store assigner
                    
                    // (Duplicate) Store job status again
                    jobD.setJobStatus(rs.getString("Status")); // Store job status again
                    
                    // (Duplicate) Store job name again
                    jobD.setJobName(rs.getString("JobName")); // Store job name again
                    
                    // Parse actual start and stop times with null checks
                    Date actualstart = rs.getString("ActualStart") != null ? format.parse(rs.getString("ActualStart")) : null; // Parse actual start time
                    Date actualstop = rs.getString("ActualStop") != null ? format.parse(rs.getString("ActualStop")) : null; // Parse actual stop time
                    
                    // Set actual times in job details
                    jobD.setActualJobStartTime(actualstart); // Set actual job start time
                    jobD.setActualJobEndTime(actualstop); // Set actual job end time
                    jobD.setWeekdays(rs.getInt("Weekdays")); // Store weekdays
                    
                    // Initialize a list to hold activity details
                    List<ActivityDetails> activityList = new LinkedList<ActivityDetails>(); // Array to store the activities of the given job
                    
                    // Query to fetch activities related to the current job
                    jdbcTemplate.query(actSql, new RowCallbackHandler() {
                        @Override
                        public void processRow(ResultSet rsAct) throws SQLException {
                            try {
                                do {
                                    // Create a new ActivityDetails object for the current activity
                                    ActivityDetails act = new ActivityDetails(rsAct.getString("TaskId"), rsAct.getString("ActivityId"), rsAct.getString("ActivityName"), rsAct.getInt("Sequence"), rsAct.getString("LogbookName"), rsAct.getInt("Duration_min"), rsAct.getString("Asset"));
                                    
                                    // Set activity details from the result set
                                    act.setScheduledActivityStartTime(format.parse(rsAct.getString("ScheduledStart"))); // Set scheduled activity start time
                                    act.setScheduledActivityEndTime(format.parse(rsAct.getString("ScheduleStop"))); // Set scheduled activity end time
                                    act.setActualActivityStartTime(rsAct.getString("ActualStart") != null ? format.parse(rsAct.getString("ActualStart")) : null); // Set actual activity start time
                                    act.setActualActivityEndTime(rsAct.getString("ActualStop") != null ? format.parse(rsAct.getString("ActualStop")) : null); // Set actual activity end time
                                    act.setActvityStatus(rsAct.getString("Status")); // Set activity status
                                    act.setPerformer(rsAct.getString("Performer")); // Set performer
                                    act.setApprover(rsAct.getString("Approver")); // Set approver
                                    act.setReviewerActivityStartTime(rsAct.getString("ActivityReviewerIntimationTime") != null ? format.parse(rsAct.getString("ActivityReviewerIntimationTime")) : null); // Set reviewer activity start time
                                    act.setReviewerActivityStopTime(rsAct.getString("ActivityReviewercompletionTime") != null ? format.parse(rsAct.getString("ActivityReviewercompletionTime")) : null); // Set reviewer activity end time
                                    act.setRemarks(rsAct.getString("Remarks")); // Set remarks
                                    act.setActivityFileData(rsAct.getBytes("FileContent")); // Set file data
                                    act.setGroupOrDeptName(rsAct.getString("GroupOrDeptName")); // Set group/department name
                                    act.setPerformerType(rsAct.getString("PerformerType")); // Set performer type
                                    act.setGroupOrDept(rsAct.getBoolean("isGroupOrDept")); // Set whether it's a group or department
                                    act.setAssetId(rsAct.getString("AssetID")); // Set asset ID
                                    act.setActFile(rsAct.getString("FileName")); // Set activity file name
                                    act.setxPos(rsAct.getString("Pos_X"));
                                    act.setyPos(rsAct.getString("Pos_Y"));
                                    act.setEnforce(rsAct.getBoolean("enforce")); // Set whether the activity is enforced or not
                                    act.setAssetName(rsAct.getString("AssetName")); // Set asset name
                                    act.setEnableStart(rsAct.getBoolean("enableStart"));
                                    activityList.add(act); // Add activity to the list
                                    
                                } while (rsAct.next()); // Continue fetching rows until done
                            } catch (Exception e) {
                                // Handle any exceptions during row processing
                                e.printStackTrace(); // Log the stack trace for debugging
                            }
                        }
                    }, rs.getString("JobId"), rs.getString("TenantId")); // Execute activity query with parameters
                    
//                  TaskDetail task = new TaskDetail(rs.getString("TaskName"), rs.getString("TaskId"), activityList); // (Commented out) This line could initialize a task with a name if needed
                    
                    TaskDetail task = new TaskDetail(null, rs.getString("TaskId"), activityList); // Initialize task without name but with activity list

                    jobD.setTask(task); // Set task in job details
                    jobs.add(jobD); // Add job to the list
                    
                } while (rs.next()); // Continue fetching jobs until done
            } catch (Exception e) {
                // Handle any exceptions during job processing
                e.printStackTrace(); // Log the stack trace for debugging
            }
        }
    }, user, tenantId); // Execute job query with parameters

    return jobs; // Return the list of jobs
}

	/**
	 * Fetch Activities to review based on user and tenantId
	 * @author SreepriyaRoy
	 */
	@Override
	public List<JobDetails> pendingForReviewActivities(String user, String tenantId) {
		JdbcTemplate jdbcTemplate=jdbcTemplates.get(tenantId).get(0);

	    List<JobDetails> jobs = new LinkedList<JobDetails>(); // Initialize a list to store job details

	    // Query to fetch jobs whose activities the user is supposed to review
	    String sql = "SELECT [TaskId], [JobId], [ActualStart], [ActualStop], [ReviewerIntimationTime], [ReviewercompletionTime], [JobName], [Assigner], [TenantId], [GroupId], [Instrument], [Approver], [ScheduleStart], [ScheduleStop], [ActualStart], [ActualStop], [ReviewerIntimationTime], [ReviewercompletionTime], [Priority], [Repeat], [RepeatTill], [Remarks], [Status], [Weekdays] FROM [dbo].[JobDetails] WHERE JobId IN (SELECT JobId FROM dbo.JobActivityDetails WHERE Approver=?) AND [Status] <> 'Completed' AND status <> 'Rejected' AND status <> 'Aborted'  AND [TenantId]=?";

	    // Query to fetch activities the user is supposed to review
	    String actSql = "SELECT [JobId], [isGroupOrDept], enforce, PerformerType, [FileName],[enableStart], GroupOrDeptName, job.[ActivityId], job.[TaskId], job.[TenantId], ActivityName, job.[AssetID], job.[AssetName], [Sequence], [LogbookName], [Performer], [Approver], [ScheduledStart], [ActivityReviewerIntimationTime], [ActivityReviewercompletionTime], [ScheduleStop], [ActualStart], [ActualStop], [Remarks], [FileContent], [Status], Duration_min, [Asset] = (SELECT Asset FROM [dbo].[ActivityCreation] cr WHERE cr.TaskId = act.TaskId AND cr.ActivityId = act.ActivityId) FROM [dbo].[JobActivityDetails] job, dbo.ActivityCreation act WHERE job.ActivityId = act.ActivityId AND job.TaskId = act.TaskId AND [JobId] = ? AND act.[TenantId]=? AND Approver=? AND status!='Completed' and status!='Aborted' and Performer!=Approver ORDER BY [Sequence]";

	    // Execute the query with RowCallbackHandler to process the rows
	    jdbcTemplate.query(sql, new RowCallbackHandler() {
	        @Override
	        public void processRow(ResultSet rs) throws SQLException {
	            try {
	                do {
	                    JobDetails jobD = new JobDetails(); // Object to store the current job
	                    TaskDetail taskD = new TaskDetail(); // Object to store the current task
	                    
	                    // Set task ID from the result set
	                    taskD.setTaskId(rs.getString("TaskId")); // Property to store the task ID of the current job
	                    
//	                  taskD.setTaskName(rs.getString("TaskName")); // (Commented out) Task name can be set here if needed
	                    
	                    // Set job details from the result set
	                    jobD.setJobName(rs.getString("JobName")); // Property to store the job name of the current job
	                    jobD.setTask(taskD); // Link the task to the job
	                    jobD.setJobID(rs.getString("JobId")); // Property to store the job ID of the current job
	                    jobD.setGroupId(rs.getString("GroupId")); // Property to store the group ID of the current job
	                    jobD.setApprover(rs.getString("Approver")); // Property to store the approver of the current job

	                    // Parse schedule start and stop times
	                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // Parse datetime in the given format
	                    Date StrtD = format.parse(rs.getString("ScheduleStart")); // Parse scheduled start date
	                    Date stpD = format.parse(rs.getString("ScheduleStop")); // Parse scheduled stop date

	                    // Set scheduled times in job details
	                    jobD.setScheduledJobStartTime(StrtD); // Set scheduled job start time
	                    jobD.setScheduledJobEndTime(stpD); // Set scheduled job end time
	                    
	                    // Set priority and status
	                    jobD.setPriority(rs.getString("Priority")); // Property to store the priority of the current job
	                    jobD.setJobStatus(rs.getString("Status")); // Property to store the status of the given job

	                    // Set reviewer times with null checks
	                    jobD.setReviewerIntimationTime(rs.getString("ReviewerIntimationTime") != null ? format.parse(rs.getString("ReviewerIntimationTime")) : null);
	                    jobD.setAssigner(rs.getString("Assigner")); // Property to set assigner of the given job
	                    jobD.setJobStatus(rs.getString("Status")); //  Property to store status of the given job
	                    jobD.setJobName(rs.getString("JobName")); // Property to store job name
	                    
	                    // Parse actual start and stop times with null checks
	                    Date actualstart = rs.getString("ActualStart") != null ? format.parse(rs.getString("ActualStart")) : null; // Parse actual start time
	                    Date actualstop = rs.getString("ActualStop") != null ? format.parse(rs.getString("ActualStop")) : null; // Parse actual stop time

	                    // Set actual times in job details
	                    jobD.setActualJobStartTime(actualstart); // Property to store actual start time
	                    jobD.setActualJobEndTime(actualstop); // Property to store actual stop time
	                    jobD.setWeekdays(rs.getInt("Weekdays")); // Property to store weekdays

	                    // Initialize a list to hold activity details
	                    List<ActivityDetails> activityList = new LinkedList<ActivityDetails>(); 

	                    // Query to fetch activities related to the current job
	                    jdbcTemplate.query(actSql, new RowCallbackHandler() {
	                        @Override
	                        public void processRow(ResultSet rsAct) throws SQLException {
	                            try {
	                                do {
	                                    // Object act is used to store the necessary information of the current activity
	                                    ActivityDetails act = new ActivityDetails(rsAct.getString("TaskId"), rsAct.getString("ActivityId"), rsAct.getString("ActivityName"), rsAct.getInt("Sequence"), rsAct.getString("LogbookName"), rsAct.getInt("Duration_min"), rsAct.getString("Asset"));

	                                    // Set activity details from the result set
	                                    act.setActivityName(rsAct.getString("ActivityName"));
	                                    act.setScheduledActivityStartTime(format.parse(rsAct.getString("ScheduledStart"))); // Set scheduled activity start time
	                                    act.setScheduledActivityEndTime(format.parse(rsAct.getString("ScheduleStop"))); // Set scheduled activity end time
	                                    act.setActualActivityStartTime(rsAct.getString("ActualStart") != null ? format.parse(rsAct.getString("ActualStart")) : null); // Set actual activity start time
	                                    act.setActualActivityEndTime(rsAct.getString("ActualStop") != null ? format.parse(rsAct.getString("ActualStop")) : null); // Set actual activity end time
	                                    act.setActvityStatus(rsAct.getString("Status")); // Set activity status
	                                    act.setPerformer(rsAct.getString("Performer")); // Set performer
	                                    act.setApprover(rsAct.getString("Approver")); // Set approver
	                                    act.setReviewerActivityStartTime(rsAct.getString("ActivityReviewerIntimationTime") != null ? format.parse(rsAct.getString("ActivityReviewerIntimationTime")) : null); // Set reviewer activity start time
	                                    act.setReviewerActivityStopTime(rsAct.getString("ActivityReviewercompletionTime") != null ? format.parse(rsAct.getString("ActivityReviewercompletionTime")) : null); // Set reviewer activity end time
	                                    act.setRemarks(rsAct.getString("Remarks")); // Set remarks
	                                    act.setActivityFileData(rsAct.getBytes("FileContent")); // Set file data
	                                    act.setGroupOrDeptName(rsAct.getString("GroupOrDeptName")); // Set group/department name
	                                    act.setPerformerType(rsAct.getString("PerformerType")); // Set performer type
	                                    act.setGroupOrDept(rsAct.getBoolean("isGroupOrDept")); // Set whether it's a group or department
	                                    act.setAssetId(rsAct.getString("AssetID")); // Set asset ID
	                                    act.setActFile(rsAct.getString("FileName")); // Set activity file name
	                                    act.setAssetName(rsAct.getString("AssetName")); // Set asset name
	                                    act.setEnforce(rsAct.getBoolean("enforce")); // Set whether enforce is true or false
	                                    act.setEnableStart(rsAct.getBoolean("enableStart"));
	                                    activityList.add(act); // Add activity to the list
	                                    
	                                } while (rsAct.next()); // Continue fetching rows until done
	                            } catch (Exception e) {
	                                // Handle any exceptions during row processing
	                                e.printStackTrace(); // Log the stack trace for debugging
	                            }
	                        }
	                    }, rs.getString("JobId"), rs.getString("TenantId"), user); // Execute activity query with parameters

//	                  TaskDetail task = new TaskDetail(rs.getString("TaskName"), rs.getString("TaskId"), activityList); // (Commented out) This line could initialize a task with a name if needed
	                    TaskDetail task = new TaskDetail(null, rs.getString("TaskId"), activityList); // Initialize task without name but with activity list
	                    
	                    // Check if there are activities and add to job
	                    if (activityList.size() > 0) {
	                        jobD.setTask(task); // Set task in job details
	                        jobs.add(jobD); // Add job to the list of jobs
	                    }
	                } while (rs.next()); // Continue fetching jobs until done
	            } catch (Exception e) {
	                // Handle any exceptions during job processing
	                e.printStackTrace(); // Log the stack trace for debugging
	            }
	        }
	    }, user, tenantId); // Execute job query with parameters

	    return jobs; // Return the list of jobs
	}


	//Here the BarChartModel class will be implemented later on [ PrimeFace jar will be required... ]
	/**
	 * Fetch the ApproverCard Details
	 */
	@Override
	public ApproverCardData getApprovedHistory(String approver,String tenantId) {
		JdbcTemplate jdbcTemplate=jdbcTemplates.get(tenantId).get(0);
		System.out.println("inside getApprovedHistory");
		String sql = "Select Approver, COALESCE([Pending], 0) [Pending], COALESCE([Approved], 0) [Approved], COALESCE([Rejected], 0) [Rejected] from " +
				"( " +
				"Select Approver, JobId,[TenantId], COALESCE(ReviewerAction, 'Pending') ReviewerAction from [dbo].[JobDetails] Where Approver = ? AND [TenantId]=?"+
				"  ) as tab1 " +
				"PIVOT(COUNT(JobId) for ReviewerAction IN ([Pending], [Approved], [Rejected])) as piv";

		return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(ApproverCardData.class),approver,tenantId);
	}
	
	
	//returns the activity name ,asset name and taskname for the respective jobId and activityId
	@Override
	public Map<String, String> getPdfFooterDetails(String jobId, String activityId,String tenantId) {
		// TODO Auto-generated method stub
		//Because for multiple jobs can be created for the same task
		//if the jobId starts from 'AJ' then the footer details from  AutoJobInfo table is fetched
		// && if the jobId starts with 'J' then the footer details from JobDetails table is fetched
		String sql="DECLARE @JobId VARCHAR(50) = ?;"
				+ " DECLARE @ActivityId VARCHAR(50) = ?;"
				+ " DECLARE @TenantId VARCHAR(500) = ?;"
				+ " declare @assetname varchar(500)='';"
				+ " declare @taskname varchar(500)=''; "
				+ " if(LEFT(@JobId, 2) = 'AJ' )"
				+ "	begin"
				+ "	set @taskname=null"
				+ " end"
				+ " else"
				+ " begin"
				+ " set @taskname=(select  top 1  [TaskName] from dbo.TaskDetails where TaskId=(select TaskId from dbo.[AutoJobInfo] where jobid=@JobId and [activityid]=@ActivityId)) \r\n"
				+ " end"
				+ " IF LEFT(@JobId, 2) = 'AJ' AND LEFT(@ActivityId, 2) = 'AA' "
				+ " BEGIN"
				+ "	set @assetname=(select  [AssetName] from [dbo].[AssetInfo] where AssetId=(select AssetId from [dbo].[AutoJobInfo] where jobid=@JobId and [activityid]=@ActivityId))"
				+ " SELECT ActivityName=null,TaskName=@taskname, AssetName=@assetname FROM [dbo].[AutoJobInfo] AJI WHERE AJI.activityid = @ActivityId AND AJI.jobid = @JobId AND AJI.TenantId = @TenantId;"
				+ " END"
				+ " ELSE"
				+ " BEGIN "
				+ "	SELECT AC.ActivityName, TD.TaskName, JAD.AssetName FROM [dbo].[ActivityCreation] AS AC "
				+ " JOIN [dbo].[JobActivityDetails] AS JAD ON AC.ActivityId = JAD.ActivityId JOIN [dbo].[TaskDetails] AS TD ON TD.TaskId = JAD.TaskId WHERE AC.ActivityId = @ActivityId "
				+ " AND JAD.JobId = @JobId AND AC.TenantId = @TenantId; END";
		JdbcTemplate jdbcTemplate=jdbcTemplates.get(tenantId).get(0);
		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql,jobId,activityId,tenantId);
		
		Map<String,String> pdfFooter=new HashMap<>();
		result.forEach(row -> {
		    pdfFooter.put("activityName", (String) row.get("ActivityName"));
		    pdfFooter.put("taskName", (String) row.get("TaskName"));
		    pdfFooter.put("assetName", (String) row.get("AssetName"));//setting the respective properties
		});
		
		System.out.println("Pdf Footer Details : "+pdfFooter);
		return pdfFooter;

	}


	@Override
	public List<JobDetails> getAllJobs(String tenantId) {
		
		// TODO Auto-generated method stub
		List<JobDetails> jobList=new ArrayList<>();
		String sql="Select j.[TaskId],[TaskName],[JobId] ,j.[JobName],j.Weekdays,j.[TenantId] ,[Assigner],[GroupId],[Instrument] ,[Approver] ,[ScheduleStart] ,[ScheduleStop] , [ActualStart] ,[ActualStop] ,[Priority] ,[Repeat] ,[RepeatTill] ,j.[Remarks] ,j.[Status] ,[adhoc], t.AssetGroup from dbo.JobDetails j, dbo.TaskDetails t Where j.TaskId=t.TaskId AND j.[TenantId]=? ";
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
		
	//added by Ashok
	@Override
	public List<JobDetails> getAllJobsForCard(String tenantId,String fromDate, String toDate) {
//		JdbcTemplate jdbcTemplate = dynamicDatasourceDAO.getDynamicConnection(tenantId)
//				 .get(op360_key);
		System.out.println("getAllJobsForCard() ");
		List<JobDetails> jobList=new ArrayList<>();
		String sql="select * from( SELECT j.[TaskId], [TaskName], j.[JobId], j.[JobName], j.Weekdays, j.[TenantId], [Assigner], [GroupId], [Instrument], [Approver], [ScheduleStart], [ScheduleStop], [ActualStart], [ActualStop], [Priority], [Repeat], [RepeatTill], j.[Remarks], j.[Status], t.AssetGroup FROM dbo.JobDetails j INNER JOIN dbo.TaskDetails t ON j.TaskId = t.TaskId UNION ALL SELECT 'Self Job' AS TaskId, 'Self Job' AS TaskName, AJI.[JobId] AS JobId, AJI.[Name] AS JobName, 7 AS Weekdays, AJI.[TenantId], performer AS Assigner, NULL AS GroupId, NULL AS Instrument, AJI.[Reviewer] AS Approver, AJI.[ActivityStartTime] AS ScheduleStart, COALESCE(AJI.[ActivityEndTime],SYSDATETIMEOFFSET() AT TIME ZONE 'India Standard Time') AS ScheduleStop, AJI.[ActivityStartTime] AS ActualStart, COALESCE(AJI.[ActivityEndTime],SYSDATETIMEOFFSET() AT TIME ZONE 'India Standard Time') AS ActualStop, 'Normal' AS Priority, NULL AS Repeat, NULL AS RepeatTill, AJI.[Remarks], AJI.[Status], AJI.[AssetName] AS AssetGroup FROM dbo.[AutoJobInfo] AJI )t2 where convert(date,t2.ScheduleStart) between ? and ? ";
		String activitySql="DECLARE @JobId AS VARCHAR(400) = ?; SELECT job.[JobId], j.[JobName], j.Weekdays, job.[ActivityId], job.[TaskId], act.[ActivityName], act.[Sequence], act.[Duration_min], act.logbook , job.[Performer], job.[Approver], job.[ScheduledStart], job.[ScheduleStop], job.[ActualStart], job.[ActualStop], job.[Status], job.[AssetName], job.[isGroupOrDept], job.[PerformerType], job.[GroupOrDeptName], job.[AssetID] FROM dbo.JobDetails j INNER JOIN dbo.JobActivityDetails job ON j.JobId = job.JobId INNER JOIN dbo.ActivityCreation act ON job.ActivityId = act.ActivityId AND job.TaskId = act.TaskId WHERE job.JobId = @JobId UNION ALL SELECT AJI.[JobId], AJI.[Name] AS JobName, 7 AS Weekdays, AJI.[ActivityId], 'Self Job' AS TaskId, 'Self Job' AS ActivityName, 1 AS Sequence, DATEDIFF(MINUTE, AJI.[ActivityStartTime], COALESCE(AJI.[ActivityEndTime], SYSDATETIMEOFFSET() AT TIME ZONE 'India Standard Time')) AS Duration_min, AJI.formname AS logbook, AJI.Performer, AJI.[Reviewer] AS Approver, AJI.[ActivityStartTime] AS ScheduledStart, COALESCE(AJI.[ActivityEndTime],SYSDATETIMEOFFSET() AT TIME ZONE 'India Standard Time') AS ScheduleStop, AJI.[ActivityStartTime] AS ActualStart, COALESCE(AJI.[ActivityEndTime],SYSDATETIMEOFFSET() AT TIME ZONE 'India Standard Time') AS ActualStop, AJI.[Status], AJI.[AssetName], NULL AS isGroupOrDept, NULL AS PerformerType, NULL AS GroupOrDeptName, NULL AS AssetID FROM [dbo].[AutoJobInfo] AJI where jobid = @JobId order by Sequence";
		JdbcTemplate jdbcTemplate=jdbcTemplates.get(tenantId).get(0);
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
					try {
						job.setScheduledJobEndTime( rs.getString("ScheduleStop")!=null ? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(rs.getString("ScheduleStop")) : null );
						//System.out.println("Parsed Starttime"+job.getScheduledJobStartTime());
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
							 System.out.println("activity Length : "+rsAct.getFetchSize());	
								try {
//									System.out.println("Job Id : "+rs.getString("JobName")+ " ActivityName : "+rsAct.getString("ActivityName")+" Status : "+);
									activityList.add(new ActivityDetails(rsAct.getString("TaskId"), rs.getString("JobName"), rsAct.getString("ActivityId"), rsAct.getString("ActivityName"), rsAct.getString("logbook"),
											rsAct.getString("Performer").isBlank()||rsAct.getString("Performer").isEmpty() || rsAct.getString("Performer")==null ? rsAct.getString("GroupOrDeptName"):rsAct.getString("Performer"), rsAct.getString("Approver"), rsAct.getString("ScheduledStart") !=null ? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(rsAct.getString("ScheduledStart")):null, rsAct.getString("ScheduleStop") !=null ?  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(rsAct.getString("ScheduleStop")) : null,
											rsAct.getString("AssetName"), rsAct.getInt("sequence"), rsAct.getInt("Duration_min"),rsAct.getBoolean("isGroupOrDept"),rsAct.getString("performerType"),rsAct.getString("GroupOrDeptName"),rsAct.getString("AssetID"),rsAct.getString("Status"),
											rsAct.getString("ActualStart") != null ? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(rsAct.getString("ActualStart")) : null,
											rsAct.getString("ActualStop") != null ? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(rsAct.getString("ActualStop")) : null,rsAct.getString("JobId"),rsAct.getInt("Weekdays")
											));
									
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
		},fromDate,toDate);
		
		return jobList;
	}

	 public List<Map<String, Object>> getJobDetailsByTicketId(String tenantId, String ticketId) {
	        String sql = "SELECT jd.[TaskId], jd.[JobId], jd.[JobName], jd.[GroupId], jd.[Instrument], jd.[Assigner], jd.[Approver], jd.[ScheduleStart], jd.[ScheduleStop], jd.[ActualStart], jd.[ActualStop], jd.[ReviewerIntimationTime], jd.[ReviewercompletionTime], jd.[ReviewerAction], jd.[Priority], jd.[Repeat], jd.[RepeatTill], jd.[Remarks] AS JobRemarks, jd.[Status] AS JobStatus, jd.[adhoc], jd.[TenantId] AS JobTenantId, jd.[Weekdays], ja.[ActivityId], ja.[LogbookName], ja.[Performer], ja.[Approver] AS ActivityApprover, ja.[PerformerType], ja.[GroupOrDeptName], ja.[AssetID], ja.[AssetName], ja.[isGroupOrDept], ja.[ScheduledStart] AS ActivityScheduledStart, ja.[ScheduleStop] AS ActivityScheduledStop, ja.[ActualStart] AS ActivityActualStart, ja.[ActualStop] AS ActivityActualStop, ja.[Status] AS ActivityStatus, ja.[ActivityReviewerIntimationTime], ja.[ActivityReviewercompletionTime], ja.[Remarks] AS ActivityRemarks, ja.[FileName], ja.[FileContent], ja.[TenantId] AS ActivityTenantId FROM dbo.[JobDetails] jd LEFT JOIN dbo.[JobActivityDetails] ja ON jd.[JobId] = ja.[JobId] WHERE CHARINDEX('_', jd.[JobName]) > 0 AND LEFT(jd.[JobName], CHARINDEX('_', jd.[JobName]) - 1) = ? ;";
	        JdbcTemplate jdbcTemplate=jdbcTemplates.get(tenantId).get(0);
	        return jdbcTemplate.queryForList(sql, ticketId);
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

	 /*
		 * Service to update sequential activities in a job, i.e., an activity id will be provided and based on that activity id that particular 
		 * activity and the activities of the next sequence will be set to Not Started state
		 * Author: Sreepriya Roy
		 */
		@Override
		public void updateSequentialStatus(String tenantid,String jobid,String activityid,int sequence) {
			String sql="DECLARE @jobid varchar(max)=?, @activityid varchar(max)=?,@sequence int=?;"
					+ "update [dbo].[JobActivityDetails] "
					+ "   SET [Status] = 'Not Started', ActualStart= NULL, ActualStop=NULL "
					+ " WHERE JobId=@jobid and (ActivityId=@activityid or ActivityId in (Select activityid from ActivityCreation a where [dbo].[JobActivityDetails].TaskId=a.TaskId and  Sequence>@sequence))";
			JdbcTemplate jdbcTemplate=jdbcTemplates.get(tenantid).get(0);
			jdbcTemplate.update(sql,
					jobid,activityid,sequence
					);//Query execution
			System.out.println("Update successful");
		}

		/**
		 * Fetch Jobs to review based on user and tenantId
		 * @author SreepriyaRoy
		 */
		@Override
		public JobDetails fetchJobDetails(String jobid, String tenantId) {
	    JobDetails job = new JobDetails(); // Initialize a list to store job details
	    JdbcTemplate jdbcTemplate=jdbcTemplates.get(tenantId).get(0);
	    // Query to find the jobs based on a job reviewer
	    String sql = "SELECT [TaskId], [JobId], [JobName], [Assigner], [TenantId], [GroupId], [Instrument], [Approver], [ScheduleStart], [ScheduleStop], [ActualStart], [ActualStop], [ReviewerIntimationTime], [ReviewercompletionTime], [Priority], [Repeat], [RepeatTill], [Remarks], [Status], [Weekdays] FROM [dbo].[JobDetails] WHERE JobId=?";

	    // Query to find the activities of a given job based on job ID
	    String actSql = "SELECT [JobId], [isGroupOrDept], [enforce], [FileName], [ActualStart], [ActualStop], PerformerType, GroupOrDeptName, job.[ActivityId], job.[TaskId], job.[TenantId], job.[AssetID], job.[AssetName], ActivityName, [Sequence], [LogbookName], [Performer], [Approver], [ScheduledStart], [ActivityReviewerIntimationTime], [ActivityReviewercompletionTime], [ScheduleStop], [ActualStart], [ActualStop], [Remarks],[enableStart] ,[FileContent], [Status], Duration_min, [Asset] = (SELECT Asset FROM [dbo].[ActivityCreation] cr WHERE cr.TaskId = act.TaskId AND cr.ActivityId = act.ActivityId),[Pos_X] = (SELECT [Pos_X] FROM [dbo].[ActivityCreation] cr WHERE cr.TaskId = act.TaskId AND cr.ActivityId = act.ActivityId),[Pos_Y] = (SELECT [Pos_Y] FROM [dbo].[ActivityCreation] cr WHERE cr.TaskId = act.TaskId AND cr.ActivityId = act.ActivityId) FROM [dbo].[JobActivityDetails] job, dbo.ActivityCreation act WHERE job.ActivityId = act.ActivityId AND job.TaskId = act.TaskId AND [JobId] = ? ORDER BY [Sequence]";

	    // Execute the query using RowCallbackHandler
	    jdbcTemplate.query(sql, new RowCallbackHandler() {
	        @Override
	        public void processRow(ResultSet rs) throws SQLException {
	            try {
	                do {
	                    TaskDetail taskD = new TaskDetail(); // Object to store task details
	                    
	                    // Set task ID from the result set
	                    taskD.setTaskId(rs.getString("TaskId")); // Store the Task ID
	                    
//	                  taskD.setTaskName(rs.getString("TaskName")); // (Commented out) Set task name if needed
	                    
	                    // Set job details from the result set
	                    job.setJobName(rs.getString("JobName")); // Store job name
	                    job.setTask(taskD); // Link the task to the job
	                    job.setJobID(rs.getString("JobId")); // Store job ID
	                    job.setGroupId(rs.getString("GroupId")); // Store group ID
	                    job.setApprover(rs.getString("Approver")); // Store approver
	                    
	                    // Parse scheduled start and stop times
	                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // Date format
	                    Date StrtD = format.parse(rs.getString("ScheduleStart")); // Parse scheduled start date
	                    Date stpD = format.parse(rs.getString("ScheduleStop")); // Parse scheduled stop date
	                    
	                    // Set scheduled times in job details
	                    job.setScheduledJobStartTime(StrtD); // Set scheduled job start time
	                    job.setScheduledJobEndTime(stpD); // Set scheduled job end time
	                    
	                    // Set priority and status
	                    job.setPriority(rs.getString("Priority")); // Store job priority
	                    job.setJobStatus(rs.getString("Status")); // Store job status
	                    
	                    // Set reviewer times with null checks
	                    job.setReviewerIntimationTime(rs.getString("ReviewerIntimationTime") != null ? format.parse(rs.getString("ReviewerIntimationTime")) : null); // Parse reviewer intimation time
	                    job.setAssigner(rs.getString("Assigner")); // Store assigner
	                    
	                    // (Duplicate) Store job status again
	                    job.setJobStatus(rs.getString("Status")); // Store job status again
	                    
	                    // (Duplicate) Store job name again
	                    job.setJobName(rs.getString("JobName")); // Store job name again
	                    
	                    // Parse actual start and stop times with null checks
	                    Date actualstart = rs.getString("ActualStart") != null ? format.parse(rs.getString("ActualStart")) : null; // Parse actual start time
	                    Date actualstop = rs.getString("ActualStop") != null ? format.parse(rs.getString("ActualStop")) : null; // Parse actual stop time
	                    
	                    // Set actual times in job details
	                    job.setActualJobStartTime(actualstart); // Set actual job start time
	                    job.setActualJobEndTime(actualstop); // Set actual job end time
	                    job.setWeekdays(rs.getInt("Weekdays")); // Store weekdays
	                    
	                    // Initialize a list to hold activity details
	                    List<ActivityDetails> activityList = new LinkedList<ActivityDetails>(); // Array to store the activities of the given job
	                    
	                    // Query to fetch activities related to the current job
	                    jdbcTemplate.query(actSql, new RowCallbackHandler() {
	                        @Override
	                        public void processRow(ResultSet rsAct) throws SQLException {
	                            try {
	                                do {
	                                    // Create a new ActivityDetails object for the current activity
	                                    ActivityDetails act = new ActivityDetails(rsAct.getString("TaskId"), rsAct.getString("ActivityId"), rsAct.getString("ActivityName"), rsAct.getInt("Sequence"), rsAct.getString("LogbookName"), rsAct.getInt("Duration_min"), rsAct.getString("Asset"));
	                                    
	                                    // Set activity details from the result set
	                                    act.setScheduledActivityStartTime(format.parse(rsAct.getString("ScheduledStart"))); // Set scheduled activity start time
	                                    act.setScheduledActivityEndTime(format.parse(rsAct.getString("ScheduleStop"))); // Set scheduled activity end time
	                                    act.setActualActivityStartTime(rsAct.getString("ActualStart") != null ? format.parse(rsAct.getString("ActualStart")) : null); // Set actual activity start time
	                                    act.setActualActivityEndTime(rsAct.getString("ActualStop") != null ? format.parse(rsAct.getString("ActualStop")) : null); // Set actual activity end time
	                                    act.setActvityStatus(rsAct.getString("Status")); // Set activity status
	                                    act.setPerformer(rsAct.getString("Performer")); // Set performer
	                                    act.setApprover(rsAct.getString("Approver")); // Set approver
	                                    act.setReviewerActivityStartTime(rsAct.getString("ActivityReviewerIntimationTime") != null ? format.parse(rsAct.getString("ActivityReviewerIntimationTime")) : null); // Set reviewer activity start time
	                                    act.setReviewerActivityStopTime(rsAct.getString("ActivityReviewercompletionTime") != null ? format.parse(rsAct.getString("ActivityReviewercompletionTime")) : null); // Set reviewer activity end time
	                                    act.setRemarks(rsAct.getString("Remarks")); // Set remarks
	                                    act.setActivityFileData(rsAct.getBytes("FileContent")); // Set file data
	                                    act.setGroupOrDeptName(rsAct.getString("GroupOrDeptName")); // Set group/department name
	                                    act.setPerformerType(rsAct.getString("PerformerType")); // Set performer type
	                                    act.setGroupOrDept(rsAct.getBoolean("isGroupOrDept")); // Set whether it's a group or department
	                                    act.setAssetId(rsAct.getString("AssetID")); // Set asset ID
	                                    act.setActFile(rsAct.getString("FileName")); // Set activity file name
	                                    act.setxPos(rsAct.getString("Pos_X"));
	                                    act.setyPos(rsAct.getString("Pos_Y"));
	                                    act.setEnforce(rsAct.getBoolean("enforce")); // Set whether the activity is enforced or not
	                                    act.setAssetName(rsAct.getString("AssetName")); // Set asset name
	                                    act.setEnableStart(rsAct.getBoolean("enableStart"));
	                                    activityList.add(act); // Add activity to the list
	                                    
	                                } while (rsAct.next()); // Continue fetching rows until done
	                            } catch (Exception e) {
	                                // Handle any exceptions during row processing
	                                e.printStackTrace(); // Log the stack trace for debugging
	                            }
	                        }
	                    }, rs.getString("JobId")); // Execute activity query with parameters
	                    
//	                  TaskDetail task = new TaskDetail(rs.getString("TaskName"), rs.getString("TaskId"), activityList); // (Commented out) This line could initialize a task with a name if needed
	                    
	                    TaskDetail task = new TaskDetail(null, rs.getString("TaskId"), activityList); // Initialize task without name but with activity list

	                    job.setTask(task); // Set task in job details
	                    
	                } while (rs.next()); // Continue fetching jobs until done
	            } catch (Exception e) {
	                // Handle any exceptions during job processing
	                e.printStackTrace(); // Log the stack trace for debugging
	            }
	        }
	    }, jobid); // Execute job query with parameters

	    return job; // Return the list of jobs
	}

		@Override
public List<JobDetails> getAllJobsByUser(String tenantId,String user, int calendarMonth, int calendarYear) {
			
//			System.out.println("Current Assigner : "+user);
//			System.out.println("Current tenant : "+tenantId);
//			System.out.println("Current Month : "+calendarMonth);
//			System.out.println("Current Year : "+calendarYear);
//			System.out.println("Tenant : "+jdbcTemplates.keySet());
			// TODO Auto-generated method stub
			List<JobDetails> jobList=new ArrayList<>();
			String sql = ""
				    + "DECLARE @Month INT = ?; "
				    + "DECLARE @Year INT = ?; "
				    + "SELECT j.[TaskId], [TaskName], [JobId], j.[JobName], j.Weekdays, j.[TenantId], "
				    + "       [Assigner], [GroupId], [Instrument], [Approver], [ScheduleStart], [ScheduleStop], "
				    + "       [ActualStart], [ActualStop], [Priority], [Repeat], [RepeatTill], j.[Remarks], "
				    + "       j.[Status], [adhoc], t.AssetGroup "
				    + "FROM dbo.JobDetails j "
				    + "JOIN dbo.TaskDetails t ON j.TaskId = t.TaskId "
				    + "WHERE j.[TenantId] = ? "
				    + "  AND Assigner = ? "
				    + " AND ( "
				    + "       ( "
				    + "           MONTH(j.[ScheduleStart]) = CASE WHEN @Month = 1 THEN 12 ELSE @Month - 1 END "
				    + "           AND YEAR(j.[ScheduleStart]) = CASE WHEN @Month = 1 THEN @Year - 1 ELSE @Year END "
				    + "       ) "
				    + "    OR ( "
				    + "           MONTH(j.[ScheduleStart]) = @Month "
				    + "           AND YEAR(j.[ScheduleStart]) = @Year "
				    + "       ) "
				    + "    OR ( "
				    + "           MONTH(j.[ScheduleStart]) = CASE WHEN @Month = 12 THEN 1 ELSE @Month + 1 END "
				    + "           AND YEAR(j.[ScheduleStart]) = CASE WHEN @Month = 12 THEN @Year + 1 ELSE @Year END "
				    + "       ) "
				    + " );";
//Why the OR condition to handle the indexing and the list change in UI
			String activitySql="Select job.[JobId] ,j.JobName,j.Weekdays,job.[ActivityId] ,job.[TaskId] ,ActivityName, [Sequence],Duration_min ,[LogbookName] ,[Performer] ,job.[Approver] ,job.[ScheduledStart] ,job.[ScheduleStop] , job.[ActualStart] ,job.[ActualStop] ,job.[Status] ,[AssetName], job.[isGroupOrDept],job.[PerformerType], job.[GroupOrDeptName],job.[AssetID],[Pos_X] = (SELECT [Pos_X] FROM [dbo].[ActivityCreation] cr WHERE cr.TaskId = act.TaskId AND cr.ActivityId = act.ActivityId),[Pos_Y] = (SELECT [Pos_Y] FROM [dbo].[ActivityCreation] cr WHERE cr.TaskId = act.TaskId AND cr.ActivityId = act.ActivityId) from dbo.JobDetails j, dbo.JobActivityDetails job,dbo.ActivityCreation act Where j.JobId = job.JobId and job.ActivityId = act.ActivityId AND job.TaskId = act.TaskId AND job.JobId = ?   order by Sequence";
			JdbcTemplate jdbcTemplate=jdbcTemplates.get(tenantId).get(0);
//			System.out.println(jdbcTemplate);
			jdbcTemplate.query(sql, new RowCallbackHandler() {
			
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					
					// TODO Auto-generated method stub
					do {
						JobDetails job=new JobDetails();
						job.setJobID(rs.getString("JobId"));
						job.setApprover(rs.getString("Approver"));
						job.setGroupId(rs.getString("GroupId"));
//						job.setInstrument(rs.getString("Instrument"));
//						System.out.println("Current Instrument Value : "+rs.getString("Instrument"));
						job.setAssigner(rs.getString("Assigner"));
						job.setJobStatus(rs.getString("Status"));
						job.setJobName(rs.getString("JobName"));
						job.setWeekdays(rs.getInt("Weekdays"));
						job.setAdhoc(rs.getBoolean("adhoc"));
						try {
							job.setScheduledJobEndTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(rs.getString("ScheduleStop")));
//							System.out.println("Parsed Starttime"+job.getScheduledJobStartTime());
							job.setScheduledJobStartTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(rs.getString("ScheduleStart")));
							job.setActualJobStartTime(rs.getString("ActualStart") != null ? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(rs.getString("ActualStart")) : null);
						job.setActualJobEndTime(rs.getString("ActualStop") != null ? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(rs.getString("ActualStop")) : null);
//						System.out.println("Current Job "+job);
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
//								 System.out.println("activity Length : "+rsAct.getFetchSize());	
									try {
//										System.out.println("Job Id : "+rs.getString("JobName")+ " ActivityName : "+rsAct.getString("ActivityName")+" Status : "+);
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
			},calendarMonth,calendarYear,tenantId,user);
			
		System.out.println("Returned size :"+jobList.size());
			
			return jobList;
		}


		@Override
		public List<JobDetails> getAllJobsByUserAndDate(String tenantId, String user, String date) {
			// TODO Auto-generated method stub
			List<JobDetails> jobList=new ArrayList<>();
			System.out.println("Calendar Date : "+date);
			System.out.println("Assigner : "+user);
			System.out.println("Current Tenant :"+tenantId);
			String sql = 
				    "SELECT j.[TaskId], [TaskName], [JobId], j.[JobName], j.Weekdays, j.[TenantId], " +
				    "       [Assigner], [GroupId], [Instrument], [Approver], [ScheduleStart], [ScheduleStop], " +
				    "       [ActualStart], [ActualStop], [Priority], [Repeat], [RepeatTill], j.[Remarks], " +
				    "       j.[Status], [adhoc], t.AssetGroup " +
				    "FROM dbo.JobDetails j " +
				    "JOIN dbo.TaskDetails t ON j.TaskId = t.TaskId " +
				    "WHERE j.[TenantId] = ? " +
				    "  AND Assigner = ? " +
				    "  AND CAST(ScheduleStart AS DATE) = ?";


			String activitySql="Select job.[JobId] ,j.JobName,j.Weekdays,job.[ActivityId] ,job.[TaskId] ,ActivityName, [Sequence],Duration_min ,[LogbookName] ,[Performer] ,job.[Approver] ,job.[ScheduledStart] ,job.[ScheduleStop] , job.[ActualStart] ,job.[ActualStop] ,job.[Status] ,[AssetName], job.[isGroupOrDept],job.[PerformerType], job.[GroupOrDeptName],job.[AssetID],[Pos_X] = (SELECT [Pos_X] FROM [dbo].[ActivityCreation] cr WHERE cr.TaskId = act.TaskId AND cr.ActivityId = act.ActivityId),[Pos_Y] = (SELECT [Pos_Y] FROM [dbo].[ActivityCreation] cr WHERE cr.TaskId = act.TaskId AND cr.ActivityId = act.ActivityId) from dbo.JobDetails j, dbo.JobActivityDetails job,dbo.ActivityCreation act Where j.JobId = job.JobId and job.ActivityId = act.ActivityId AND job.TaskId = act.TaskId AND job.JobId = ?   order by Sequence";
			JdbcTemplate jdbcTemplate=jdbcTemplates.get(tenantId).get(0);
//			System.out.println(jdbcTemplate);
			jdbcTemplate.query(sql, new RowCallbackHandler() {
			
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					
					// TODO Auto-generated method stub
					do {
						JobDetails job=new JobDetails();
						job.setJobID(rs.getString("JobId"));
						job.setApprover(rs.getString("Approver"));
						job.setGroupId(rs.getString("GroupId"));
//						job.setInstrument(rs.getString("Instrument"));
//						System.out.println("Current Instrument Value : "+rs.getString("Instrument"));
						job.setAssigner(rs.getString("Assigner"));
						job.setJobStatus(rs.getString("Status"));
						job.setJobName(rs.getString("JobName"));
						job.setWeekdays(rs.getInt("Weekdays"));
						job.setAdhoc(rs.getBoolean("adhoc"));
						try {
							job.setScheduledJobEndTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(rs.getString("ScheduleStop")));
//							System.out.println("Parsed Starttime"+job.getScheduledJobStartTime());
							job.setScheduledJobStartTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(rs.getString("ScheduleStart")));
							job.setActualJobStartTime(rs.getString("ActualStart") != null ? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(rs.getString("ActualStart")) : null);
						job.setActualJobEndTime(rs.getString("ActualStop") != null ? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(rs.getString("ActualStop")) : null);
//						System.out.println("Current Job "+job);
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
//								 System.out.println("activity Length : "+rsAct.getFetchSize());	
									try {
//										System.out.println("Job Id : "+rs.getString("JobName")+ " ActivityName : "+rsAct.getString("ActivityName")+" Status : "+);
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
			},tenantId,user,date);
			
		
			
			return jobList;
		}
			


}
