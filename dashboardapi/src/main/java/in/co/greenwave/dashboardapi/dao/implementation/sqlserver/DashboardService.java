package in.co.greenwave.dashboardapi.dao.implementation.sqlserver;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import com.google.gson.Gson;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import in.co.greenwave.dashboardapi.utility.JdbcUrlUtil;
import in.co.greenwave.dashboardapi.dao.DashboardDAO;
import in.co.greenwave.dashboardapi.dao.DynamicDatasourceDAO;
import in.co.greenwave.dashboardapi.model.ActivityDetails;
import in.co.greenwave.dashboardapi.model.FormDetails;
import in.co.greenwave.dashboardapi.model.JobDetails;
import in.co.greenwave.dashboardapi.model.JobwiseCardData;
import in.co.greenwave.dashboardapi.model.TaskDetail;
import in.co.greenwave.dashboardapi.model.TenantConfigDetails;
import in.co.greenwave.dashboardapi.model.Transactiondata;
import in.co.greenwave.dashboardapi.model.User;
import in.co.greenwave.dashboardapi.model.UserwiseJobDetails;


@Repository
public class DashboardService implements DashboardDAO{

	    // Injecting the OP360 jdbcTemplate for database operations.
//	    @Autowired
//	    @Qualifier("jdbcTemplate_OP360")
//	    private  JdbcTemplate jdbcTemplate;

	    // Injecting the User module jdbcTemplate.
//	    @Autowired
//	    @Qualifier("jdbcTemplate_OP360_Usermodule")
//	    private  JdbcTemplate jdbcTemplateUser;
	    
	    
	    @Autowired
		@Qualifier("jdbcTemplate_OP360_tenant")
		private  JdbcTemplate jdbcTemplateOp360Tenant;

	    // Uncomment if needed for tenant-specific JDBC template.
	    // @Autowired
	    // @Qualifier("jdbcTemplate_OP360_tenant")
	    // private  JdbcTemplate jdbcTemplateTenant;
	    
	    // Uncomment if needed to use JdbcConfig for tenant-based configurations.
	    // @Autowired
	    // private   JdbcConfig JdbcConfig;
	    
	    // Injecting the dynamic data source for multi-tenant support.
	    @Autowired
	    private DynamicDatasourceDAO dynamicDatasource;
	
	    @Autowired
		@Qualifier("DatasourceCollections")
		private  Map<String, List<JdbcTemplate>> jdbcTemplates; // Template for operations on the main database
		


	@Override
	
public List<JobwiseCardData> fetchJobwiseInfo(String tenantId, String fromDate, String toDate) {
    // Dynamic datasource fetching (uncomment if using multi-tenant approach)
    // dynamicDatasource = factory.getDynamicDatasourceService();
    // Map<String, JdbcTemplate> jdbcTemplateMap = dynamicDatasource.getDynamicConnection(tenantId);
    // JdbcTemplate jdbcTemplate = jdbcTemplateMap.get("db_op360");
    
    // List to store the results
    List<JobwiseCardData> data = new LinkedList<JobwiseCardData>();
    JdbcTemplate jdbcTemplate=jdbcTemplates.get(tenantId).get(0);
    // SQL query to fetch all the jobs information based on tenantId, fromDate, and toDate
    String sql = "Select JobId, JobName, job.TaskId, COALESCE(GroupId,'NA') GroupId, [Progress] = dbo.JobWiseProgress(job.JobId), "
               + "ScheduleStart, ScheduleStop, ActualStart, ActualStop, [Priority], "
               + "Task = (Select TaskName from dbo.TaskDetails task Where job.TaskId = task.TaskId), "
               + "COALESCE(Instrument,'NA') Instrument, COALESCE(curr_activityName,'NA') curr_activityName, "
               + "COALESCE(curr_performer,'NA') curr_performer, COALESCE(Asset,'NA') Asset, "
               + "[JobStatus] = CASE WHEN (GETDATE() < job.ScheduleStart) AND (job.ActualStart IS NULL) THEN CONCAT('Yet-to-Start','/NA') "
               + "WHEN (GETDATE() < job.ScheduleStart) AND (job.ActualStart IS NOT NULL) THEN CONCAT('On-Time','/NA') "
               + "WHEN GETDATE() between job.ScheduleStart AND job.ScheduleStop THEN CONCAT('On-Time','/NA') "
               + "WHEN GETDATE() > job.ScheduleStop THEN CONCAT('Late/',DATEDIFF(MINUTE,job.ScheduleStop,GETDATE()),' Minutes') END, "
               + "PlannedProgress = CASE WHEN ScheduleStop <= GETDATE() THEN 100 "
               + "ELSE (DATEDIFF(MINUTE,ScheduleStart,GETDATE()) / DATEDIFF(MINUTE,ScheduleStart,ScheduleStop)) * 100.0 END "
               + "from dbo.JobDetails job "
               + "CROSS APPLY dbo.getcurrentInfo(JobId) "
               + "Where CONVERT(Date, job.ScheduleStart) between ? AND ? and TenantId = ? "
               + "union all "
               + "SELECT [jobid], name, TaskId='NA', GroupId='NA', coalesce(dbo.JobWiseProgress(JobId), 0) [Progress], "
               + "ScheduleStart='1900-01-01', ScheduleStop='1900-01-01', [activitystarttime], [activityendtime], Priority='NA', Task=' ', "
               + "assetname, ' ', performer, 'NA', concat(' ','/',status), 100.0 FROM [dbo].[AutoJobInfo] "
               + "where activitystarttime between ? and ? and TenantId = ?";
    
    // Executing the query and processing the result
    jdbcTemplate.query(sql, new RowCallbackHandler() {

        @Override
        public void processRow(ResultSet rs) throws SQLException {
            try {
                // Loop through each row in the result set
                do {
                    // List to store performers for the current job
                    List<String> performers = new ArrayList<String>();
                    
                    // SQL query to fetch distinct performers for the job
                    // Uncomment the username fetch if needed
                    // String perSql = "Select Distinct Performer, UserName from dbo.JobActivityDetails act,dbo.UserCredential u Where act.Performer = u.UserId AND JobId = ? ";
                    String perSql = "Select Distinct Performer from dbo.JobActivityDetails act Where act.Performer <> '' AND JobId = ? ";
                    
                    // Execute performer query and store results
                    jdbcTemplate.query(perSql, new RowCallbackHandler() {

                        @Override
                        public void processRow(ResultSet rsP) throws SQLException {
                            try {
                                // Add each performer to the performers list
                                do {
                                    performers.add(rsP.getString("Performer"));
                                } while (rsP.next());

                            } catch (Exception e) {
                                // Handle any exceptions during performer processing
                            }

                        }
                    }, rs.getString("JobId"));

                    // Create a new JobwiseCardData object and populate it with data from the result set
                    JobwiseCardData cd = new JobwiseCardData(
                        rs.getString("JobId"), 
                        rs.getString("JobName"), 
                        rs.getString("Priority"), 
                        rs.getString("GroupId"), 
                        rs.getString("Task"), 
                        rs.getString("Instrument"), 
                        rs.getInt("Progress"), 
                        performers
                    );
                    
                    
                    StringBuilder jobStatus = new StringBuilder(rs.getString("JobStatus"));
                    cd.setWorkStatus(jobStatus.substring(0, jobStatus.indexOf("/")));
                    jobStatus.replace(0, jobStatus.indexOf("/") + 1, "");
                    cd.setStatusMsg(jobStatus.substring(0, jobStatus.length()));
                    
                    // Set additional fields for the current job
                    cd.setAsset(rs.getString("Asset"));
                    cd.setCurrentPerformer(rs.getString("curr_performer"));
                    System.out.println("Current Act : " + rs.getString("curr_activityName"));
                    cd.setCurrentActivity(rs.getString("curr_activityName"));
                    cd.setPlannedProgress(rs.getInt("PlannedProgress"));
                    cd.setTaskId(rs.getString("TaskId"));
                    
                    // Add the job card data to the list
                    data.add(cd);
                    
                } while (rs.next());

            } catch (Exception e) {
                // Handle any exceptions during result set processing
            }

        }
    }, fromDate, toDate, tenantId, fromDate, toDate, tenantId);

    // Return the list of job-wise card data
    return data;
}

	

	@Override

public List<Transactiondata> fetchFromActivityId(String tenantId, String jobId, String activityId) {
    // Retrieve dynamic data source (commented out)
    // dynamicDatasource = factory.getDynamicDatasourceService();
    // Map<String, JdbcTemplate> jdbcTemplateMap = dynamicDatasource.getDynamicConnection(tenantId);
    // JdbcTemplate jdbcTemplate = jdbcTemplateMap.get("db_op360");

    List<Transactiondata> transactionDataList = new ArrayList<Transactiondata>();
    JdbcTemplate jdbcTemplate=jdbcTemplates.get(tenantId).get(0);
    // SQL query to fetch transaction data for a given jobId and activityId and TenantId
    String sql = "Select ld.TransactionId, ld.transaction_remarks, lb.[key], cell.AliasId, lb.[value], ld.version, g.FormName [FormName] " +
                 "from dbo.LogbookTransactionData ld " +
                 "cross apply OpenJSON(logbookdata) lb, dbo.DigitalLogbookFormInfo g, " +
                 tenantId + ".DigitalLogbookCellDetails cell " +
                 "where ld.JobId = ? and ld.ActivityId = ? and ld.TenantId = ? " +
                 "and g.FormName = ld.formname and cell.FormId = g.FormId " +
                 "and cell.VersionNumber = ld.version and cell.CellId = lb.[key] " +
                 "COLLATE SQL_Latin1_General_CP1_CI_AS and [value] like '%\"value\":%' " +
                 "order by ld.TransactionId";

    // Execute the query and process the result set
    jdbcTemplate.query(sql, new RowCallbackHandler() {
        @Override
        public void processRow(ResultSet rs) throws SQLException {
            try {
                Gson gson = new Gson();  
                String transactionId = "";
                String cellValue, userRemarks = "", transactionRemarks = "";
                Transactiondata.TrData trData;

                // Process the first row of the result set
                if (rs.next()) {
                    trData = gson.fromJson(rs.getString("value"), Transactiondata.TrData.class);
                    transactionId = rs.getString("TransactionId");
                    userRemarks = "Alias: " + rs.getString("AliasId") + "  Value: " + trData.getValue() + "   Remarks: " + trData.getRemarks();
                    transactionRemarks = rs.getString("transaction_remarks");
                }

                // Process subsequent rows in the result set
                do {
                    // If the current transaction ID matches the previous one, aggregate user remarks
                    if (transactionId.equalsIgnoreCase(rs.getString("TransactionId"))) {
                        trData = gson.fromJson(rs.getString("value"), Transactiondata.TrData.class);
                        userRemarks = userRemarks + "\r\n Alias: " + rs.getString("AliasId") + "  Value: " + trData.getValue() + "   Remarks: " + trData.getRemarks();
                        continue; // Continue to next row
                    }

                    // Create a new Transactiondata object for a different transaction
                    Transactiondata transactionData = new Transactiondata();
                    transactionData.setJobid(jobId);
                    transactionData.setActivityid(activityId);
                    transactionData.setTransactionid(transactionId);
                    transactionData.setRemarks(transactionRemarks);
                    transactionData.setUserRemaks(userRemarks);
                    transactionDataList.add(transactionData); 

                    // Prepare for the next transaction
                    trData = gson.fromJson(rs.getString("value"), Transactiondata.TrData.class);
                    transactionId = rs.getString("TransactionId");
                    userRemarks = "Alias: " + rs.getString("AliasId") + "  Value: " + trData.getValue() + "   Remarks: " + trData.getRemarks();
                    transactionRemarks = rs.getString("transaction_remarks");

                } while (rs.next()); // Continue looping through result set
                
                // Add the last transaction data to the list
                Transactiondata transactionData = new Transactiondata();
                transactionData.setJobid(jobId);
                transactionData.setActivityid(activityId);
                transactionData.setTransactionid(transactionId);
                transactionData.setRemarks(transactionRemarks);
                transactionData.setUserRemaks(userRemarks);
                transactionDataList.add(transactionData); 

                // Return early if no transaction ID was found
                if (transactionId.equals("")) {
                    return; 
                }
            } catch (Exception e) {
                // Handle exceptions that may arise during processing
                // TODO: handle exception
            }
        }
    }, jobId, activityId, tenantId); // Pass parameters to the query

    return transactionDataList; // Return the list of transaction data
}

	
	@Override
	public List<String> fetchLogbooksByDateRange(String tenantId, String fromdate, String todate) {
    // Retrieve dynamic data source (commented out)
    // dynamicDatasource = factory.getDynamicDatasourceService();
    // Map<String, JdbcTemplate> jdbcTemplateMap = dynamicDatasource.getDynamicConnection(tenantId);
    // JdbcTemplate jdbcTemplate = jdbcTemplateMap.get("db_op360");
		JdbcTemplate jdbcTemplate=jdbcTemplates.get(tenantId).get(0);
    // SQL query to fetch distinct logbook names within the specified date range and TenantId
    String sql = "Declare @fromdate as date=? Declare @todate as date=? " +
                 "select distinct * from( " +
                 "select distinct LogbookName FROM [dbo].[JobActivityDetails] " +
                 "where convert(date, ScheduledStart) between @fromdate and @todate " +
                 "and TenantId = ? and LogbookName is not null " +
                 "union all " +
                 "Select distinct a.formname FROM [dbo].[AutoJobInfo] a " +
                 "where convert(date, activitystarttime) between @fromdate and @todate " +
                 "and TenantId = ?) as tbl";

    // Execute the query and return a list of distinct logbook names as strings
    return jdbcTemplate.queryForList(sql, String.class, fromdate, todate, tenantId, tenantId);
}

@Override
public List<String> fetchAssetsByDateRange(String tenantId, String fromDate, String toDate) {
    // Retrieve dynamic data source (commented out)
    // dynamicDatasource = factory.getDynamicDatasourceService();
    // Map<String, JdbcTemplate> jdbcTemplateMap = dynamicDatasource.getDynamicConnection(tenantId);
    // JdbcTemplate jdbcTemplate = jdbcTemplateMap.get("db_op360");
	JdbcTemplate jdbcTemplate=jdbcTemplates.get(tenantId).get(0);
    // SQL query to fetch distinct asset names within the specified date range and tenantId
    String sql = "Declare @fromdate as date=? Declare @todate as date=? " +
                 "select distinct * from( " +
                 "select distinct AssetName FROM [dbo].[JobActivityDetails] " +
                 "where convert(date, ScheduledStart) between @fromdate and @todate " +
                 "and TenantId = ? and AssetName is not null " +
                 "and AssetID is not null and AssetName<>'' and AssetID <>'' " +
                 "union all " +
                 "Select distinct a.assetname FROM [dbo].[AutoJobInfo] a " +
                 "where convert(date, activitystarttime) between @fromdate and @todate " +
                 "and TenantId = ? and assetid is not null and assetid<>'') as tbl";

    // Execute the query and return a list of distinct asset names as strings
    return jdbcTemplate.queryForList(sql, String.class, fromDate, toDate, tenantId, tenantId);
}

@Override
public List<JobDetails> fetchAllJobDetails(String tenantId, String fromdate, String todate) {
    // Print the provided date range for debugging purposes
    System.out.println(fromdate + " date " + todate);
    JdbcTemplate jdbcTemplate=jdbcTemplates.get(tenantId).get(0);
    // Initialize a list to hold job details
    List<JobDetails> jobs = new LinkedList<JobDetails>(); 

    // SQL query to fetch job details from the database
    String sql = "Select *, case when SUBSTRING(JobId,1,1)='A' then 'Self Assigned Job' else 'Non Self Assigned job' end [Job Type] from("
            + "SELECT a.[TaskId], [JobId], coalesce(a.GroupId, 'NA') [GroupId], "
            + tenantId + ".JobWiseProgress(JobId) [Progress], a.[JobName], "
            + "b.TaskName [TaskTemplate], [Instrument], [Approver], [ScheduleStart], [ScheduleStop], "
            + "[ActualStart], [ActualStop], [ReviewerIntimationTime], [ReviewercompletionTime], "
            + "[Priority], [Repeat], [RepeatTill], a.[Remarks], "
            + "coalesce(a.ReviewerAction, a.[Status]) [Status], curr_activityId, curr_activityName, "
            + "curr_performer, formname=' ' "
            + "FROM [dbo].[JobDetails] a CROSS APPLY dbo.getcurrentInfo(JobId) x "
            + "left join dbo.TaskDetails b on a.TaskId = b.TaskId "
            + "Where convert(date, a.ScheduleStart) between ? and ? and a.TenantId = ? "
            + "union all "
            + "SELECT TaskId=' ', [jobid], GroupId=' ', CASE when status='Approved' then 100 else 0 end as [Progress], "
            + "name, TaskTemplate=' ', [assetname] [Instrument], reviewer [Approver], ScheduleStart='1900-01-01', "
            + "ScheduleStop='1900-01-01', [activitystarttime], [activityendtime], reviewstarttime, reviewendtime, "
            + "Priority=' ', REpeat=NULL, NULL, Remarks=' ', [status], activityid, activityname='NA', performer, formname "
            + "FROM [dbo].[AutoJobInfo] a where convert(date, activitystarttime) between ? and ? and TenantId = ? "
            + ") as t";
//    System.out.println("SQL Query: " + sql);
//    System.out.println("Parameters: " + fromdate + ", " + todate + ", " + tenantId);

    // SQL query to fetch activity details associated with a job
    String actSql = "SELECT [JobId], job.[ActivityId], job.[TaskId], ActivityName, [Sequence], [LogbookName], "
            + "[UserName] = Performer, [Performer], job.GroupOrDeptName, [Approver], [ScheduledStart], "
            + "[ScheduleStop], [ActualStart], [ActualStop], [Status], Duration_min, "
            + "[Asset] = (Select Asset from [dbo].[ActivityCreation] cr Where cr.TaskId = act.TaskId "
            + "AND cr.ActivityId = act.ActivityId) FROM [dbo].[JobActivityDetails] job, "
            + tenantId + ".ActivityCreation act Where job.ActivityId = act.ActivityId and job.TaskId = act.TaskId "
            + "AND [JobId] = ? Order by [Sequence]";

    // Execute the main SQL query and process the results using a RowCallbackHandler
    jdbcTemplate.query(sql, new RowCallbackHandler() {
        @Override
        public void processRow(ResultSet rs) throws SQLException {
            try {
                // Iterate through each row of the result set
                do {
                    // Create a new JobDetails object for each job retrieved
                    JobDetails jobD = new JobDetails();
                    TaskDetail taskD = new TaskDetail();

                    // Set task details from the ResultSet
                    taskD.setTaskId(rs.getString("TaskId")); // Set the Task ID
                    taskD.setTaskName(rs.getString("TaskTemplate")); // Set the Task Template Name
                    jobD.setTask(taskD); // Associate TaskDetail with JobDetails
                    jobD.setGroupId(rs.getString("GroupId")); // Set Group ID
                    jobD.setJobID(rs.getString("JobId")); // Set Job ID
                    jobD.setApprover(rs.getString("Approver")); // Set Approver Name

                    // Parse date strings to Date objects
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date StrtD = format.parse(rs.getString("ScheduleStart")); // Parse scheduled start date
                    Date stpD = format.parse(rs.getString("ScheduleStop")); // Parse scheduled stop date
                    jobD.setJobName(rs.getString("JobName")); // Set Job Name
                    jobD.setScheduledJobStartTime(StrtD); // Set Scheduled Job Start Time
                    jobD.setScheduledJobEndTime(stpD); // Set Scheduled Job End Time
                    jobD.setPriority(rs.getString("Priority")); // Set Job Priority
                    jobD.setJobStatus(rs.getString("Status")); // Set Job Status

                    // Set actual job start and end times, using a default if null
                    jobD.setActualJobStartTime(format.parse(rs.getString("ActualStart") != null ? rs.getString("ActualStart") : "1900-01-01 00:00:00"));
                    jobD.setActualJobEndTime(format.parse(rs.getString("ActualStop") != null ? rs.getString("ActualStop") : "1900-01-01 00:00:00"));

                    // Set job progress
                    jobD.setProgress(rs.getInt("Progress"));
                    // Set reviewer notification time, using a default if null
                    jobD.setReviewerIntimationTime(rs.getString("ReviewerIntimationTime") != null ? format.parse(rs.getString("ReviewerIntimationTime")) : format.parse("1900-01-01 00:00:00"));
                    jobD.setCurrentActivityName(rs.getString("curr_activityName")); // Set current activity name
                    jobD.setCurrentPerformer(rs.getString("curr_performer")); // Set current performer name
//                    jobD.setInstrument(rs.getString("Instrument").equals(" ") ? "NA" : rs.getString("Instrument")); // Set instrument name

                    // List to hold associated activities for the job
                    List<ActivityDetails> activityList = new LinkedList<ActivityDetails>();

                    // Check if the job is self-assigned
                    if (rs.getString("JobId").charAt(0) == 'A') {
                        // Create a new ActivityDetails object for self-assigned jobs
                        ActivityDetails act = new ActivityDetails(" ", rs.getString("curr_activityId"), rs.getString("curr_activityName"), 1, rs.getString("formname") != null ? rs.getString("formname") : " ", 100, rs.getString("Instrument") != null ? rs.getString("Instrument") : "NA");
                        
                        // Parse scheduled start and stop times
                        Date StrtD1 = format.parse(rs.getString("ScheduleStart"));
                        act.setScheduledActivityStartTime(StrtD1); // Set scheduled activity start time
                        Date StrtD2 = format.parse(rs.getString("ScheduleStop"));
                        act.setScheduledActivityEndTime(StrtD2); // Set scheduled activity end time

                        // Set actual activity start and end times
                        act.setActualActivityStartTime(rs.getString("ActualStart") != null ? format.parse(rs.getString("ActualStart")) : format.parse("1900-01-01 00:00:00"));
                        act.setActualActivityEndTime(rs.getString("ActualStop") != null ? format.parse(rs.getString("ActualStop")) : format.parse("1900-01-01 00:00:00"));
                        act.setActvityStatus(rs.getString("Status")); // Set activity status
                        act.setPerformer(rs.getString("curr_performer")); // Set performer name
                        activityList.add(act); // Add the activity to the list
                    } else {
                        // For non-self-assigned jobs, execute the activity SQL query
                        jdbcTemplate.query(actSql, new RowCallbackHandler() {
                            @Override
                            public void processRow(ResultSet rsAct) throws SQLException {
                                try {
                                    // Iterate through each activity in the result set
                                    do {
                                        // Create a new ActivityDetails object for each activity retrieved
                                        ActivityDetails act = new ActivityDetails(
                                            rsAct.getString("TaskId") != null ? rsAct.getString("TaskId") : " ", // Set Task ID
                                            rsAct.getString("ActivityId") != null ? rsAct.getString("ActivityId") : " ", // Set Activity ID
                                            rsAct.getString("ActivityName") != null ? rsAct.getString("ActivityName") : " ", // Set Activity Name
                                            rsAct.getInt("Sequence"), // Set Activity Sequence
                                            rsAct.getString("LogbookName") != null ? rsAct.getString("LogbookName") : " ", // Set Logbook Name
                                            rsAct.getInt("Duration_min"), // Set Duration in minutes
                                            rsAct.getString("Asset") == null || rsAct.getString("Asset").isBlank() || rsAct.getString("Asset").isEmpty() ? "NA" : rsAct.getString("Asset") // Set Asset
                                        );
                                        // Parse scheduled start and stop times for the activity
                                        act.setScheduledActivityStartTime(format.parse(rsAct.getString("ScheduledStart")));
                                        act.setScheduledActivityEndTime(format.parse(rsAct.getString("ScheduleStop")));
                                        act.setActualActivityStartTime(rsAct.getString("ActualStart") != null ? format.parse(rsAct.getString("ActualStart")) : format.parse("1900-01-01 00:00:00")); // Set actual start time
                                        act.setActualActivityEndTime(rsAct.getString("ActualStop") != null ? format.parse(rsAct.getString("ActualStop")) : format.parse("1900-01-01 00:00:00")); // Set actual end time
                                        act.setActvityStatus(rsAct.getString("Status")); // Set activity status
                                        act.setPerformer(rsAct.getString("Performer")); // Set performer name
                                        act.setApprover(rsAct.getString("Approver")); // Set approver name
                                        act.setGroupOrDeptName(rsAct.getString("GroupOrDeptName")); // Set group or department name
                                        activityList.add(act); // Add the activity to the list
                                        // Check if the current activity matches the activity name to set waiting time
                                        if(act.getActivityName().equalsIgnoreCase(rs.getString("curr_activityName"))) {
											int t = ((int)(new Date().getTime() - format.parse(rsAct.getString("ScheduledStart")).getTime()))/60000; 
											jobD.setActivityWaitingTime(t);
										}
                                    } while (rsAct.next()); // Continue processing until there are no more rows
                                } catch (Exception e) {
                                    // Handle exceptions during result processing
                                    e.printStackTrace();
                                }
                            }
                        }, rs.getString("JobId")); // Pass JobId to the activity SQL query
                    }
                    
                    // Set the activity list in the JobDetails object and add it to the main list
                    TaskDetail task = new TaskDetail(rs.getString("TaskTemplate"), rs.getString("TaskId"), activityList);
					jobD.setTask(task); //Add the task to the to job  
					jobs.add(jobD);	 // Add the JobDetails object to the list of jobs
                } while (rs.next()); // Continue processing until there are no more rows
            } catch (Exception e) {
                // Handle exceptions during result processing
                e.printStackTrace();
            }
        }
    }, fromdate, todate, tenantId, fromdate, todate, tenantId); // Pass parameters to the main SQL query

    return jobs; // Return the list of JobDetails
}


/**
 * Objective: Fetches task-wise job details based on the provided tenant ID and date range. 
 * The method aggregates job details for each user, asset, tag, and task template.
 * 
 * @param tenantId The ID of the tenant for which the job details are fetched.
 * @param fromDate The starting date for fetching job details.
 * @param toDate   The ending date for fetching job details.
 * @return A map containing task-wise job details categorized by task template.
 */

	
	@Override
	public Map<String, LinkedList<UserwiseJobDetails>> fetchTaskWiseJob(String tenantId,String fromDate, String toDate) {
		Map<String,LinkedList<UserwiseJobDetails>> userWiseJobMap=new HashMap<String,LinkedList<UserwiseJobDetails>>();
		Map<String,LinkedList<UserwiseJobDetails>> assetWiseJobMap=new HashMap<String,LinkedList<UserwiseJobDetails>>();
		Map<String,LinkedList<UserwiseJobDetails>> tagWiseJobMap=new HashMap<String,LinkedList<UserwiseJobDetails>>(); 
		Map<String,LinkedList<UserwiseJobDetails>> taskWiseJobMap=new HashMap<String,LinkedList<UserwiseJobDetails>>();
		List<JobwiseCardData> jobWiseData;
		List<JobDetails> joblists =new ArrayList<JobDetails>();
		List<User> usersList = new ArrayList<>();
		List<String> assetListSelectItem=new ArrayList<String>();
		LinkedHashSet<String> assetSet=new LinkedHashSet<String>();
		List<String> logbookLists=new ArrayList<String>();
		LinkedHashSet<String> logbookSet=new LinkedHashSet<String>();
		LinkedHashSet<String> logbookselectedSet=new LinkedHashSet<String>();
		List<String> logbookListsSelectItem=new ArrayList<String>();
		List<String> assetList=new ArrayList<String>();

		jobWiseData = new ArrayList<JobwiseCardData>();
		//jobWiseData = dao.fetchJobwiseInfo(dfDt.format(fromDate),dfDt.format(toDate));
		//get all jobs based on the from date and to date
		joblists=fetchAllJobDetails(tenantId,fromDate,toDate);
		usersList=getAllUserInfo(tenantId);
		userWiseJobMap.clear();
		assetWiseJobMap.clear();
		tagWiseJobMap.clear();
		taskWiseJobMap.clear();

		//			onViewChange();
		//			generateTimelineView();


		for(JobDetails job:joblists) {
			job.getTask().getActivityList().stream().distinct().forEach(s->job.setPerformerLists(job.getPerformerLists()+ s.getPerformer()+",")) ;
			String performerArray[]=job.getPerformerLists().split(",");
			LinkedHashSet<String> performerS=new LinkedHashSet<String>();
			for(String s:performerArray) {
				for(User user:usersList){
					if(user.getUserID().equalsIgnoreCase(s)) {		
						performerS.add(user.getUserName());
						break;
					}
				}
			}
			String performer="";
			System.out.println("performerS : "+performerS);
			for(String s:performerS) {
				performer+=","+s;
			}
			//System.out.println("Job Performer= "+performer);
			job.setPerformerLists(performer.trim());
			if(job.getPerformerLists().length()>0)
				job.setPerformerLists( job.getPerformerLists().substring(1,job.getPerformerLists().length()));
		}
		//mapTaskJob=joblists.stream().collect(Collectors.toMap(s->s.getTask().getTaskName(),s->s ));
		//For each job of joblists
		for(JobDetails job:joblists) {
			//System.out.println("Job="+job.toString());
			List<ActivityDetails> activityList=job.getTask().getActivityList();
			/*
			 *  Make  object of UserwiseJobDetails for Task and Tag Selection
			 *  at reference variable tempJobTaskTag
			 * */
			UserwiseJobDetails tempJobTaskTag=new UserwiseJobDetails();
			//Set activity list for tempJobTaskTag
			tempJobTaskTag.setActvityList(job.getTask().getActivityList());
			String assetsForTempJobTaskTag="";
			LinkedHashSet<String> assetsTempJobTaskTagLinkedHashSet=new LinkedHashSet<String>(); 
			/* 
			 * For each activity of job
			 * */
			for(ActivityDetails activity:activityList){ 
				/*
				 * Make object of UserwiseJobDetails for User and Asset selection
				 * at reference variable tempJobUserAsset
				 * */
				UserwiseJobDetails tempJobUserAsset=new UserwiseJobDetails();
				tempJobUserAsset.setActvityList(job.getTask().getActivityList());
				tempJobUserAsset.setJobId(job.getJobID());
				tempJobTaskTag.setJobId(job.getJobID());
				tempJobTaskTag.setApprover(job.getApprover());
				tempJobUserAsset.setApprover(job.getApprover());
				tempJobUserAsset.setGroupId(job.getGroupId());
				System.out.println("job.getGroupId() => "+job.getGroupId());
				tempJobTaskTag.setGroupId(job.getGroupId());
				tempJobUserAsset.setJobName(job.getJobName());
				tempJobTaskTag.setJobName(job.getJobName()); 
				//System.out.println("job.Job Reviwer () : "+job.getApprover());
				tempJobUserAsset.setJobstatus(job.getJobStatus());
				tempJobTaskTag.setJobstatus(job.getJobStatus());
				Format formatter = new SimpleDateFormat("dd/MM/YYYY HH:mm:ss");
				tempJobUserAsset.setJobActualStartTime(formatter.format(job.getActualJobStartTime()));
				tempJobTaskTag.setJobActualStartTime(formatter.format(job.getActualJobStartTime()));
				tempJobUserAsset.setJobActualStopTime(formatter.format(job.getActualJobEndTime()));
				tempJobTaskTag.setJobActualStopTime(formatter.format(job.getActualJobEndTime()));
				tempJobUserAsset.setJobScheduleStartTime(formatter.format(job.getScheduledJobStartTime()));
				tempJobTaskTag.setJobScheduleStartTime(formatter.format(job.getScheduledJobStartTime()));
				tempJobUserAsset.setJobScheduleStopTime(formatter.format(job.getScheduledJobEndTime()));
				tempJobTaskTag.setJobScheduleStopTime(formatter.format(job.getScheduledJobEndTime()));
				tempJobUserAsset.setPriority(job.getPriority());
				tempJobTaskTag.setPriority(job.getPriority());
				tempJobUserAsset.setProgress(job.getProgress().toString());
				tempJobTaskTag.setProgress(job.getProgress().toString());
				tempJobUserAsset.setAssets(activity.getAssetName());
				assetListSelectItem.add(activity.getAssetName());
				assetSet.add(activity.getAssetName());
				assetsTempJobTaskTagLinkedHashSet.add(activity.getAssetName());
				tempJobUserAsset.setTaskTemplate(job.getTask().getTaskName());
//				tempJobTaskTag.setTaskTemplate(job.getTask().getTaskName().trim().length()!=0?job.getTask().getTaskName():"Self Assigned");
				tempJobTaskTag.setTaskTemplate(
					    job.getTask() != null && job.getTask().getTaskName() != null && !job.getTask().getTaskName().trim().isEmpty()
					    ? job.getTask().getTaskName().trim()
					    : "Self Assigned"
					);
				tempJobUserAsset.setCurrentActivity(job.getCurrentActivityName());
				tempJobTaskTag.setCurrentActivity(job.getCurrentActivityName());
				String userName="";
				for(User user:usersList) {
					if(user.getUserID().equalsIgnoreCase(job.getCurrentPerformer())) {
						userName=user.getUserName();
						break;
					}
				}
				tempJobUserAsset.setCurrentPerformer(userName);
				tempJobTaskTag.setCurrentPerformer(userName);
				//tempJobUserAsset.setCurrentPerformer(job.getCurrentPerformer());
				//tempJobTaskTag.setCurrentPerformer(job.getCurrentPerformer());
				List<String> initialList=new ArrayList<String>();
				initialList=Arrays.asList(job.getPerformerLists().split(","));
				tempJobTaskTag.setInitial(initialList);
				//System.out.println("Hm="+hm.toString());
				//tempJobTaskTag.setUserNameList(userNameList);
				tempJobUserAsset.setPerformers(job.getPerformerLists());
				tempJobTaskTag.setPerformers(job.getPerformerLists());
				tempJobUserAsset.setLogBook(activity.getLogbook());
				tempJobTaskTag.setLogBook(activity.getLogbook());
				logbookLists.add(activity.getLogbook());
				logbookSet.add(activity.getLogbook());
				logbookselectedSet.add(activity.getLogbook());
				userName="";
				for(User user:usersList) {
					if(user.getUserID().equalsIgnoreCase(activity.getPerformer())) {
						userName=user.getUserName();
						break;
					}
				}
				//tempJobUserAsset.setPerformer(activity.getPerformer());
				//tempJobTaskTag.setPerformer(activity.getPerformer());
				tempJobUserAsset.setPerformer(userName);
				tempJobTaskTag.setPerformer(userName);
				tempJobUserAsset.setActivityId(activity.getActivityId());
				tempJobTaskTag.setActivityId(activity.getActivityId());
				tempJobUserAsset.setActivityName(activity.getActivityName());
				tempJobTaskTag.setActivityName(activity.getActivityName());
				tempJobUserAsset.setActivityStartTime(formatter.format(activity.getActualActivityStartTime()));
				tempJobTaskTag.setActivityStartTime(formatter.format(activity.getActualActivityStartTime()));
				tempJobUserAsset.setActivityStartTime(formatter.format(activity.getActualActivityStartTime()));
				tempJobTaskTag.setActivityStartTime(formatter.format(activity.getActualActivityStartTime()));
				tempJobUserAsset.setActivityStoptime(formatter.format(activity.getActualActivityEndTime()));
				tempJobTaskTag.setActivityStoptime(formatter.format(activity.getActualActivityEndTime()));
				//Activity Elapsed Time
				Date objDate1 = activity.getActualActivityStartTime();
				Date objDate2 = activity.getActualActivityEndTime();
				long time_difference=objDate2.getTime()-objDate1.getTime();
				long days_difference = (time_difference / (1000*60*60*24)) % 365;
				long seconds_difference = (time_difference / 1000)% 60;   
				long minutes_difference = (time_difference / (1000*60)) % 60;   
				long hours_difference = (time_difference / (1000*60*60)) % 24; 
				tempJobUserAsset.setElapsedtime(days_difference+"d"+hours_difference+"h"+minutes_difference+"m");
				//Job Elapsed Time
				objDate1=job.getActualJobStartTime();
				objDate2=job.getActualJobEndTime();
				time_difference=objDate2.getTime()-objDate1.getTime();
				days_difference = (time_difference / (1000*60*60*24)) % 365;
				seconds_difference = (time_difference / 1000)% 60;   
				minutes_difference = (time_difference / (1000*60)) % 60;   
				hours_difference = (time_difference / (1000*60*60)) % 24; 
				tempJobUserAsset.setJobElapsedTime(days_difference+"d:"+hours_difference+"h:"+minutes_difference+"m");
				tempJobTaskTag.setJobElapsedTime(days_difference+"d:"+hours_difference+"h:"+minutes_difference+"m");
				//User key wise
				if(userWiseJobMap.containsKey(tempJobUserAsset.getPerformer())) {
					userWiseJobMap.get(tempJobUserAsset.getPerformer()).add(tempJobUserAsset);
				}else {
					userWiseJobMap.put(tempJobUserAsset.getPerformer(), new LinkedList<UserwiseJobDetails>() {{
						add(tempJobUserAsset);
					}});
				}
				//Asset key wise 
				if(assetWiseJobMap.containsKey(tempJobUserAsset.getAssets())) {
					assetWiseJobMap.get(tempJobUserAsset.getAssets()).add(tempJobUserAsset);
				}else {
					assetWiseJobMap.put(tempJobUserAsset.getAssets(), new LinkedList<UserwiseJobDetails>() {{
						add(tempJobUserAsset);
					}});
				}
			}
			/*
			 * 
			 * Add Distinct assets in comma separated way to tempJob1
			 * */
			assetsForTempJobTaskTag="";
			for(String s:assetsTempJobTaskTagLinkedHashSet) {
				assetsForTempJobTaskTag+=s+",";
			}
			if(assetsForTempJobTaskTag.trim().length()>1) {
				tempJobTaskTag.setAssets(assetsForTempJobTaskTag.trim().substring(0,assetsForTempJobTaskTag.trim().length()-1));
			}
			//Group Id key wise
			if(tagWiseJobMap.containsKey(tempJobTaskTag.getGroupId())) {
				tagWiseJobMap.get(tempJobTaskTag.getGroupId()).add(tempJobTaskTag);
			}else {
				tagWiseJobMap.put(tempJobTaskTag.getGroupId(), new LinkedList<UserwiseJobDetails>() {{
					add(tempJobTaskTag);
				}});
			}
			//Task Template key wise
			if(taskWiseJobMap.containsKey(tempJobTaskTag.getTaskTemplate())) {
				taskWiseJobMap.get(tempJobTaskTag.getTaskTemplate()).add(tempJobTaskTag);
			}else {
				taskWiseJobMap.put(tempJobTaskTag.getTaskTemplate(), new LinkedList<UserwiseJobDetails>() {{
					add(tempJobTaskTag);
				}});
			}
		}






		logbookListsSelectItem=logbookLists.stream().distinct().collect(Collectors.toList());
		logbookLists=logbookLists.stream().distinct().collect(Collectors.toList());
		assetListSelectItem=assetListSelectItem.stream().distinct().collect(Collectors.toList());
		assetList=assetListSelectItem;




		return taskWiseJobMap;
	}





public List<User> getAllUserInfo(String tenantId) {
    // Initialize a list to store User objects
    List<User> userList = new LinkedList<User>();
    JdbcTemplate jdbcTemplateUser=jdbcTemplates.get(tenantId).get(1);
    // Initialize sets to hold unique page names for G360 and Workflow
    HashSet<String> g360PageList = new HashSet<String>();
    HashSet<String> workFlowPAgeList = new HashSet<String>();

    // Retrieve dynamic data source if needed (commented out)
    // dynamicDatasource = factory.getDynamicDatasourceService();
    // Map<String, JdbcTemplate> jdbcTemplateMap = dynamicDatasource.getDynamicConnection(tenantId);
    // JdbcTemplate jdbcTemplateUser = jdbcTemplateMap.get("db_op360_usermodule");

    // SQL query to retrieve G360 pages for the given tenant
    String g360PagesSQL = "SELECT [Page] FROM [dbo].[Pages] where [Source] = ?";
    
    // Execute the query and process the result set for G360 pages
    jdbcTemplateUser.query(g360PagesSQL, new RowCallbackHandler() {
        @Override
        public void processRow(ResultSet g360Pages) throws SQLException {
            try {
                do {
                    // Add each page to the G360 page list
                    g360PageList.add(g360Pages.getString("Page"));
                } while (g360Pages.next()); // Continue until there are no more rows
            } catch (Exception e) {
                // Handle any exceptions (currently a placeholder)
            }
        }
    }, "Workflow"); // Pass parameters for the query

    // SQL query to retrieve Workflow pages for the given tenant
    String workFlowPagesSQL = "SELECT [Page] FROM [dbo].[Pages] where [Source] = ?";

    // Execute the query and process the result set for Workflow pages
    jdbcTemplateUser.query(workFlowPagesSQL, new RowCallbackHandler() {
        @Override
        public void processRow(ResultSet wfPages) throws SQLException {
            try {
                do {
                    // Add each page to the Workflow page list
                    workFlowPAgeList.add(wfPages.getString("Page"));
                } while (wfPages.next()); // Continue until there are no more rows
            } catch (Exception e) {
                // Handle any exceptions (currently a placeholder)
            }
        }
    }, "Workflow"); // Pass parameters for the query

    // SQL query to retrieve user credentials for the given tenant
    String sql = "Select * from [dbo].[UserCredential]";
    
    // Execute the query and process the result set for user credentials
    jdbcTemplateUser.query(sql, new RowCallbackHandler() {
        @Override
        public void processRow(ResultSet rs) throws SQLException {
            try {
                do {
                    // Create a User object for each user in the result set
                    User user = new User(
                        rs.getString("UserId"),
                        rs.getString("UserName"),
                        rs.getString("Password"),
                        rs.getString("PhoneNumber"),
                        rs.getString("WorkflowHomepage"),
                        rs.getString("G360Homepage"),
                        rs.getBoolean("G360Admin"),
                        rs.getBoolean("Active"),
                        rs.getString("CreatedOn"),
                        rs.getString("ModifiedOn"),
                        rs.getString("CreatedBy"),
                        rs.getString("ModifiedBy"),
                        rs.getString("Department"),
                        rs.getBoolean("FirstLoginRequired"),
                        rs.getInt("PasswordExpiryDuraton"),
                        rs.getString("PasswordExpiryDate"),
                        rs.getString("LastLogin")
                    );

                    // SQL query to retrieve alloted pages for the current user
                    String pageSQL = "Select * from [dbo].[UserPages] Where UserId = ?";
                    List<String> allotedPages = new LinkedList<String>();
                    
                    // Execute the query and process the result set for alloted pages
                    jdbcTemplateUser.query(pageSQL, new RowCallbackHandler() {
                        @Override
                        public void processRow(ResultSet pRs) throws SQLException {
                            try {
                                do {
                                    // Add each alloted page to the list
                                    allotedPages.add(pRs.getString("AllotedPage"));
                                } while (pRs.next()); // Continue until there are no more rows
                                user.setAllotedPages(allotedPages); // Set alloted pages in user object
                            } catch (Exception e) {
                                // Handle any exceptions (currently a placeholder)
                            }
                        }
                    }, rs.getString("UserId")); // Pass user ID as a parameter

                    // SQL query to retrieve user roles for the current user
                    String rollSQL = "Select * from [dbo].[UserRole] Where UserId = ?";
                    List<String> groups = new LinkedList<String>();
                    
                    // Execute the query and process the result set for user roles
                    jdbcTemplateUser.query(rollSQL, new RowCallbackHandler() {
                        @Override
                        public void processRow(ResultSet rollRs) throws SQLException {
                            try {
                                do {
                                    // Add each user role to the list
                                    groups.add(rollRs.getString("UserRoll"));
                                } while (rollRs.next()); // Continue until there are no more rows
                                user.setGroup(groups); // Set user groups in user object
                            } catch (Exception e) {
                                // Handle any exceptions (currently a placeholder)
                            }
                        }
                    }, rs.getString("UserId")); // Pass user ID as a parameter

                    // Set the complete lists of pages for the user
                    user.setAllG360PageList(g360PageList);
                    user.setAllWorkflowPageList(workFlowPAgeList);

                    // Update the user's selected pages based on allotments
                    user.updateSourceSelectionPages(allotedPages);
                    userList.add(user); // Add the user object to the user list
                } while (rs.next()); // Continue until there are no more rows
            } catch (Exception e) {
                // Handle any exceptions (currently a placeholder)
            }
        }
    });

    // Return the complete list of users with their information
    return userList;
}

	@Override
	public Map<String, LinkedList<UserwiseJobDetails>> fetchTagWiseJob(String tenantId,String fromDate, String toDate) {
		// Create a map to hold user-wise job details, where the key is the user ID and the value is a list of job details for that user
		Map<String, LinkedList<UserwiseJobDetails>> userWiseJobMap = new HashMap<String, LinkedList<UserwiseJobDetails>>();

		// Create a map to hold asset-wise job details, where the key is the asset name and the value is a list of job details related to that asset
		Map<String, LinkedList<UserwiseJobDetails>> assetWiseJobMap = new HashMap<String, LinkedList<UserwiseJobDetails>>();

		// Create a map to hold tag-wise job details, where the key is the tag name and the value is a list of job details associated with that tag
		Map<String, LinkedList<UserwiseJobDetails>> tagWiseJobMap = new HashMap<String, LinkedList<UserwiseJobDetails>>();

		// Create a map to hold task-wise job details, where the key is the task name and the value is a list of job details for that task
		Map<String, LinkedList<UserwiseJobDetails>> taskWiseJobMap = new HashMap<String, LinkedList<UserwiseJobDetails>>();

		// Declare a list to hold job-wise card data
		List<JobwiseCardData> jobWiseData;

		// Declare a list to hold job details retrieved from the database
		List<JobDetails> joblists = new ArrayList<JobDetails>();

		// Declare a list to hold user information
		List<User> usersList = new ArrayList<>();

		// Declare a list to hold selected asset items for display
		List<String> assetListSelectItem = new ArrayList<String>();

		// Create a set to hold unique assets, preventing duplicates
		LinkedHashSet<String> assetSet = new LinkedHashSet<String>();

		// Declare a list to hold logbook names
		List<String> logbookLists = new ArrayList<String>();

		// Create a set to hold unique logbooks
		LinkedHashSet<String> logbookSet = new LinkedHashSet<String>();

		// Create a set to hold selected logbooks
		LinkedHashSet<String> logbookselectedSet = new LinkedHashSet<String>();

		// Declare a list to hold selected logbook items for display
		List<String> logbookListsSelectItem = new ArrayList<String>();

		// Declare a list to hold asset names
		List<String> assetList = new ArrayList<String>();

		// Initialize the jobWiseData list to store job card data
		jobWiseData = new ArrayList<JobwiseCardData>();

		// Uncomment this line to fetch job-wise data based on the provided date range (currently commented out)
		// jobWiseData = dao.fetchJobwiseInfo(dfDt.format(fromDate), dfDt.format(toDate));

		// Retrieve all job details based on the specified tenant ID and date range
		joblists = fetchAllJobDetails(tenantId, fromDate, toDate);

		// Get all user information based on the tenant ID
		usersList = getAllUserInfo(tenantId);

		// Clear previous data from the user-wise job map to avoid overlaps in data
		userWiseJobMap.clear();

		// Clear previous data from the asset-wise job map to avoid overlaps in data
		assetWiseJobMap.clear();

		// Clear previous data from the tag-wise job map to avoid overlaps in data
		tagWiseJobMap.clear();

		// Clear previous data from the task-wise job map to avoid overlaps in data
		taskWiseJobMap.clear();



		// Iterate over each JobDetails object in the joblists
		for (JobDetails job : joblists) {
		    // For each job, retrieve the distinct performers from the activity list and append them to the job's performer lists
		    job.getTask().getActivityList().stream().distinct().forEach(s -> 
		        job.setPerformerLists(job.getPerformerLists() + s.getPerformer() + ",")
		    );

		    // Split the performer lists string into an array based on the comma delimiter
		    String performerArray[] = job.getPerformerLists().split(",");

		    // Create a LinkedHashSet to hold unique performer names (to prevent duplicates)
		    LinkedHashSet<String> performerS = new LinkedHashSet<String>();

		    // Loop through each performer ID in the performerArray
		    for (String s : performerArray) {
		        // Check against the usersList to find matching user information
		        for (User user : usersList) {
		            // If the user ID matches (case-insensitive), add the user's name to the performer set
		            if (user.getUserID().equalsIgnoreCase(s)) {
		                performerS.add(user.getUserName());
		                break; // Exit the inner loop once a match is found
		            }
		        }
		    }

		    // Initialize a string to build the final performer list
		    String performer = "";
		    // Print the unique performer names for debugging purposes
		    System.out.println("performerS : " + performerS);

		    // Append each unique performer name to the performer string
		    for (String s : performerS) {
		        performer += "," + s;
		    }

		    // Uncomment the line below to print the complete performer list for debugging purposes
		    // System.out.println("Job Performer= " + performer);

		    // Set the performer lists in the job with the constructed string, trimming any leading/trailing spaces
		    job.setPerformerLists(performer.trim());

		    // If the performer list has a length greater than zero, remove the leading comma from the string
		    if (job.getPerformerLists().length() > 0)
		        job.setPerformerLists(job.getPerformerLists().substring(1, job.getPerformerLists().length()));
		}

		
		//For each job of joblists
		for(JobDetails job:joblists) {
			//System.out.println("Job="+job.toString());
			List<ActivityDetails> activityList=job.getTask().getActivityList();
			/*
			 *  Make  object of UserwiseJobDetails for Task and Tag Selection
			 *  at reference variable tempJobTaskTag
			 * */
			UserwiseJobDetails tempJobTaskTag=new UserwiseJobDetails();
			//Set activity list for tempJobTaskTag
			tempJobTaskTag.setActvityList(job.getTask().getActivityList());
			String assetsForTempJobTaskTag="";
			LinkedHashSet<String> assetsTempJobTaskTagLinkedHashSet=new LinkedHashSet<String>(); 
			/* 
			 * For each activity of job
			 * */
			for(ActivityDetails activity:activityList){ 
				/*
				 * Make object of UserwiseJobDetails for User and Asset selection
				 * at reference variable tempJobUserAsset
				 * */
				UserwiseJobDetails tempJobUserAsset=new UserwiseJobDetails();
				tempJobUserAsset.setActvityList(job.getTask().getActivityList());
				tempJobUserAsset.setJobId(job.getJobID());
				tempJobTaskTag.setJobId(job.getJobID());
				tempJobUserAsset.setGroupId(job.getGroupId());
				System.out.println("job.getGroupId() => "+job.getGroupId());
				tempJobTaskTag.setGroupId(job.getGroupId());
				tempJobUserAsset.setJobName(job.getJobName());
				tempJobTaskTag.setJobName(job.getJobName()); 
				//System.out.println("job.getJobName() : "+job.getJobName());
				tempJobUserAsset.setJobstatus(job.getJobStatus());
				tempJobTaskTag.setJobstatus(job.getJobStatus());
				Format formatter = new SimpleDateFormat("dd/MM/YYYY HH:mm:ss");
				tempJobUserAsset.setJobActualStartTime(formatter.format(job.getActualJobStartTime()));
				tempJobTaskTag.setJobActualStartTime(formatter.format(job.getActualJobStartTime()));
				tempJobUserAsset.setJobActualStopTime(formatter.format(job.getActualJobEndTime()));
				tempJobTaskTag.setJobActualStopTime(formatter.format(job.getActualJobEndTime()));
				tempJobUserAsset.setJobScheduleStartTime(formatter.format(job.getScheduledJobStartTime()));
				tempJobTaskTag.setJobScheduleStartTime(formatter.format(job.getScheduledJobStartTime()));
				tempJobUserAsset.setJobScheduleStopTime(formatter.format(job.getScheduledJobEndTime()));
				tempJobTaskTag.setJobScheduleStopTime(formatter.format(job.getScheduledJobEndTime()));
				tempJobUserAsset.setPriority(job.getPriority());
				tempJobTaskTag.setPriority(job.getPriority());
				tempJobUserAsset.setProgress(job.getProgress().toString());
				tempJobTaskTag.setProgress(job.getProgress().toString());
				tempJobUserAsset.setAssets(activity.getAssetName());
				assetListSelectItem.add(activity.getAssetName());
				assetSet.add(activity.getAssetName());
				assetsTempJobTaskTagLinkedHashSet.add(activity.getAssetName());
				tempJobUserAsset.setTaskTemplate(job.getTask().getTaskName());
				tempJobTaskTag.setTaskTemplate(job.getTask().getTaskName().trim().length()!=0?job.getTask().getTaskName():"Self Assigned");
				tempJobUserAsset.setCurrentActivity(job.getCurrentActivityName());
				tempJobTaskTag.setCurrentActivity(job.getCurrentActivityName());
				String userName="";
				for(User user:usersList) {
					if(user.getUserID().equalsIgnoreCase(job.getCurrentPerformer())) {
						userName=user.getUserName();
						break;
					}
				}
				tempJobUserAsset.setCurrentPerformer(userName);
				tempJobTaskTag.setCurrentPerformer(userName);
				//tempJobUserAsset.setCurrentPerformer(job.getCurrentPerformer());
				//tempJobTaskTag.setCurrentPerformer(job.getCurrentPerformer());
				List<String> initialList=new ArrayList<String>();
				initialList=Arrays.asList(job.getPerformerLists().split(","));
				tempJobTaskTag.setInitial(initialList);
				//System.out.println("Hm="+hm.toString());
				//tempJobTaskTag.setUserNameList(userNameList);
				tempJobUserAsset.setPerformers(job.getPerformerLists());
				tempJobTaskTag.setPerformers(job.getPerformerLists());
				tempJobUserAsset.setLogBook(activity.getLogbook());
				tempJobTaskTag.setLogBook(activity.getLogbook());
				logbookLists.add(activity.getLogbook());
				logbookSet.add(activity.getLogbook());
				logbookselectedSet.add(activity.getLogbook());
				userName="";
				for(User user:usersList) {
					if(user.getUserID().equalsIgnoreCase(activity.getPerformer())) {
						userName=user.getUserName();
						break;
					}
				}
				//tempJobUserAsset.setPerformer(activity.getPerformer());
				//tempJobTaskTag.setPerformer(activity.getPerformer());
				tempJobUserAsset.setPerformer(userName);
				tempJobTaskTag.setPerformer(userName);
				tempJobUserAsset.setActivityId(activity.getActivityId());
				tempJobTaskTag.setActivityId(activity.getActivityId());
				tempJobUserAsset.setActivityName(activity.getActivityName());
				tempJobTaskTag.setActivityName(activity.getActivityName());
				tempJobUserAsset.setActivityStartTime(formatter.format(activity.getActualActivityStartTime()));
				tempJobTaskTag.setActivityStartTime(formatter.format(activity.getActualActivityStartTime()));
				tempJobUserAsset.setActivityStartTime(formatter.format(activity.getActualActivityStartTime()));
				tempJobTaskTag.setActivityStartTime(formatter.format(activity.getActualActivityStartTime()));
				tempJobUserAsset.setActivityStoptime(formatter.format(activity.getActualActivityEndTime()));
				tempJobTaskTag.setActivityStoptime(formatter.format(activity.getActualActivityEndTime()));
				//Activity Elapsed Time
				Date objDate1 = activity.getActualActivityStartTime();
				Date objDate2 = activity.getActualActivityEndTime();
				long time_difference=objDate2.getTime()-objDate1.getTime();
				long days_difference = (time_difference / (1000*60*60*24)) % 365;
				long seconds_difference = (time_difference / 1000)% 60;   
				long minutes_difference = (time_difference / (1000*60)) % 60;   
				long hours_difference = (time_difference / (1000*60*60)) % 24; 
				tempJobUserAsset.setElapsedtime(days_difference+"d"+hours_difference+"h"+minutes_difference+"m");
				//Job Elapsed Time
				objDate1=job.getActualJobStartTime();
				objDate2=job.getActualJobEndTime();
				time_difference=objDate2.getTime()-objDate1.getTime();
				days_difference = (time_difference / (1000*60*60*24)) % 365;
				seconds_difference = (time_difference / 1000)% 60;   
				minutes_difference = (time_difference / (1000*60)) % 60;   
				hours_difference = (time_difference / (1000*60*60)) % 24; 
				tempJobUserAsset.setJobElapsedTime(days_difference+"d:"+hours_difference+"h:"+minutes_difference+"m");
				tempJobTaskTag.setJobElapsedTime(days_difference+"d:"+hours_difference+"h:"+minutes_difference+"m");
				//User key wise
				if(userWiseJobMap.containsKey(tempJobUserAsset.getPerformer())) {
					userWiseJobMap.get(tempJobUserAsset.getPerformer()).add(tempJobUserAsset);
				}else {
					userWiseJobMap.put(tempJobUserAsset.getPerformer(), new LinkedList<UserwiseJobDetails>() {{
						add(tempJobUserAsset);
					}});
				}
				//Asset key wise 
				if(assetWiseJobMap.containsKey(tempJobUserAsset.getAssets())) {
					assetWiseJobMap.get(tempJobUserAsset.getAssets()).add(tempJobUserAsset);
				}else {
					assetWiseJobMap.put(tempJobUserAsset.getAssets(), new LinkedList<UserwiseJobDetails>() {{
						add(tempJobUserAsset);
					}});
				}
			}
			/*
			 * 
			 * Add Distinct assets in comma separated way to tempJob1
			 * */
			assetsForTempJobTaskTag="";
			for(String s:assetsTempJobTaskTagLinkedHashSet) {
				assetsForTempJobTaskTag+=s+",";
			}
			if(assetsForTempJobTaskTag.trim().length()>1) {
				tempJobTaskTag.setAssets(assetsForTempJobTaskTag.trim().substring(0,assetsForTempJobTaskTag.trim().length()-1));
			}
			//Group Id key wise
			if(tagWiseJobMap.containsKey(tempJobTaskTag.getGroupId())) {
				tagWiseJobMap.get(tempJobTaskTag.getGroupId()).add(tempJobTaskTag);
			}else {
				tagWiseJobMap.put(tempJobTaskTag.getGroupId(), new LinkedList<UserwiseJobDetails>() {{
					add(tempJobTaskTag);
				}});
			}
			//Task Template key wise
			if(taskWiseJobMap.containsKey(tempJobTaskTag.getTaskTemplate())) {
				taskWiseJobMap.get(tempJobTaskTag.getTaskTemplate()).add(tempJobTaskTag);
			}else {
				taskWiseJobMap.put(tempJobTaskTag.getTaskTemplate(), new LinkedList<UserwiseJobDetails>() {{
					add(tempJobTaskTag);
				}});
			}
		}






		logbookListsSelectItem=logbookLists.stream().distinct().collect(Collectors.toList());
		logbookLists=logbookLists.stream().distinct().collect(Collectors.toList());
		assetListSelectItem=assetListSelectItem.stream().distinct().collect(Collectors.toList());
		assetList=assetListSelectItem;




		return tagWiseJobMap;
	}

	@Override
	public Map<String, LinkedList<UserwiseJobDetails>> fetchUserWiseJob(String tenantId,String fromDate, String toDate) {

		Map<String,LinkedList<UserwiseJobDetails>> userWiseJobMap=new HashMap<String,LinkedList<UserwiseJobDetails>>();
		Map<String,LinkedList<UserwiseJobDetails>> assetWiseJobMap=new HashMap<String,LinkedList<UserwiseJobDetails>>();
		Map<String,LinkedList<UserwiseJobDetails>> tagWiseJobMap=new HashMap<String,LinkedList<UserwiseJobDetails>>(); 
		Map<String,LinkedList<UserwiseJobDetails>> taskWiseJobMap=new HashMap<String,LinkedList<UserwiseJobDetails>>();
		List<JobwiseCardData> jobWiseData;
		List<JobDetails> joblists =new ArrayList<JobDetails>();
		List<User> usersList = new ArrayList<>();
		List<String> assetListSelectItem=new ArrayList<String>();
		LinkedHashSet<String> assetSet=new LinkedHashSet<String>();
		List<String> logbookLists=new ArrayList<String>();
		LinkedHashSet<String> logbookSet=new LinkedHashSet<String>();
		LinkedHashSet<String> logbookselectedSet=new LinkedHashSet<String>();
		List<String> logbookListsSelectItem=new ArrayList<String>();
		List<String> assetList=new ArrayList<String>();

		jobWiseData = new ArrayList<JobwiseCardData>();
		//jobWiseData = dao.fetchJobwiseInfo(dfDt.format(fromDate),dfDt.format(toDate));
		//get all jobs based on the from date and to date
		joblists=fetchAllJobDetails(tenantId,fromDate,toDate);
		usersList=getAllUserInfo(tenantId);
		System.out.println("usersList after API call : "+usersList);
		userWiseJobMap.clear();
		assetWiseJobMap.clear();
		tagWiseJobMap.clear();
		taskWiseJobMap.clear();

		//			onViewChange();
		//			generateTimelineView();


		for(JobDetails job:joblists) {
			job.getTask().getActivityList().stream().distinct().forEach(s->job.setPerformerLists(job.getPerformerLists()+ s.getPerformer()+",")) ;
			String performerArray[]=job.getPerformerLists().split(",");
			LinkedHashSet<String> performerS=new LinkedHashSet<String>();
			for(String s:performerArray) {
				for(User user:usersList){
					if(user.getUserID().equalsIgnoreCase(s)) {		
						performerS.add(user.getUserName());
						break;
					}
				}
			}
			String performer="";
			System.out.println("performerS : "+performerS);
			for(String s:performerS) {
				performer+=","+s;
			}
			//System.out.println("Job Performer= "+performer);
			job.setPerformerLists(performer.trim());
			if(job.getPerformerLists().length()>0)
				job.setPerformerLists( job.getPerformerLists().substring(1,job.getPerformerLists().length()));
		}
		//mapTaskJob=joblists.stream().collect(Collectors.toMap(s->s.getTask().getTaskName(),s->s ));
		//For each job of joblists
		for(JobDetails job:joblists) {
			//System.out.println("Job="+job.toString());
			List<ActivityDetails> activityList=job.getTask().getActivityList();
			/*
			 *  Make  object of UserwiseJobDetails for Task and Tag Selection
			 *  at reference variable tempJobTaskTag
			 * */
			UserwiseJobDetails tempJobTaskTag=new UserwiseJobDetails();
			//Set activity list for tempJobTaskTag
			tempJobTaskTag.setActvityList(job.getTask().getActivityList());
			String assetsForTempJobTaskTag="";
			LinkedHashSet<String> assetsTempJobTaskTagLinkedHashSet=new LinkedHashSet<String>(); 
			/* 
			 * For each activity of job
			 * */
			for(ActivityDetails activity:activityList){ 
				/*
				 * Make object of UserwiseJobDetails for User and Asset selection
				 * at reference variable tempJobUserAsset
				 * */
				UserwiseJobDetails tempJobUserAsset=new UserwiseJobDetails();
				tempJobUserAsset.setActvityList(job.getTask().getActivityList());
				tempJobUserAsset.setJobId(job.getJobID());
				tempJobTaskTag.setJobId(job.getJobID());
				tempJobUserAsset.setGroupId(job.getGroupId());
//				System.out.println("job.currentPerformer() => "+job.getg);
				tempJobTaskTag.setGroupId(job.getGroupId());
				tempJobUserAsset.setJobName(job.getJobName());
				tempJobTaskTag.setJobName(job.getJobName()); 
				//System.out.println("job.getJobName() : "+job.getJobName());
				tempJobUserAsset.setJobstatus(job.getJobStatus());
				tempJobTaskTag.setJobstatus(job.getJobStatus());
				Format formatter = new SimpleDateFormat("dd/MM/YYYY HH:mm:ss");
				tempJobUserAsset.setJobActualStartTime(formatter.format(job.getActualJobStartTime()));
				tempJobTaskTag.setJobActualStartTime(formatter.format(job.getActualJobStartTime()));
				tempJobUserAsset.setJobActualStopTime(formatter.format(job.getActualJobEndTime()));
				tempJobTaskTag.setJobActualStopTime(formatter.format(job.getActualJobEndTime()));
				tempJobUserAsset.setJobScheduleStartTime(formatter.format(job.getScheduledJobStartTime()));
				tempJobTaskTag.setJobScheduleStartTime(formatter.format(job.getScheduledJobStartTime()));
				tempJobUserAsset.setJobScheduleStopTime(formatter.format(job.getScheduledJobEndTime()));
				tempJobTaskTag.setJobScheduleStopTime(formatter.format(job.getScheduledJobEndTime()));
				tempJobUserAsset.setPriority(job.getPriority());
				tempJobTaskTag.setPriority(job.getPriority());
				tempJobUserAsset.setProgress(job.getProgress().toString());
				tempJobTaskTag.setProgress(job.getProgress().toString());
				tempJobUserAsset.setAssets(activity.getAssetName());
				assetListSelectItem.add(activity.getAssetName());
				assetSet.add(activity.getAssetName());
				assetsTempJobTaskTagLinkedHashSet.add(activity.getAssetName());
				tempJobUserAsset.setTaskTemplate(job.getTask().getTaskName());
				tempJobTaskTag.setTaskTemplate(job.getTask().getTaskName().trim().length()!=0?job.getTask().getTaskName():"Self Assigned");
				tempJobUserAsset.setCurrentActivity(job.getCurrentActivityName());
				tempJobTaskTag.setCurrentActivity(job.getCurrentActivityName());
				String userName="";
				for(User user:usersList) {
					if(user.getUserID().equalsIgnoreCase(job.getCurrentPerformer())) {
						userName=user.getUserName();
						break;
					}
				}
				tempJobUserAsset.setCurrentPerformer(userName);
				tempJobTaskTag.setCurrentPerformer(userName);
				//tempJobUserAsset.setCurrentPerformer(job.getCurrentPerformer());
				//tempJobTaskTag.setCurrentPerformer(job.getCurrentPerformer());
				List<String> initialList=new ArrayList<String>();
				initialList=Arrays.asList(job.getPerformerLists().split(","));
				tempJobTaskTag.setInitial(initialList);
				//System.out.println("Hm="+hm.toString());
				//tempJobTaskTag.setUserNameList(userNameList);
				tempJobUserAsset.setPerformers(job.getPerformerLists());
				tempJobTaskTag.setPerformers(job.getPerformerLists());
				tempJobUserAsset.setLogBook(activity.getLogbook());
				tempJobTaskTag.setLogBook(activity.getLogbook());
				logbookLists.add(activity.getLogbook());
				logbookSet.add(activity.getLogbook());
				logbookselectedSet.add(activity.getLogbook());
				userName="";
				System.out.println("userList : "+usersList);
				for(User user:usersList) {
					if(user.getUserID().equalsIgnoreCase(activity.getPerformer())) {
						System.out.println("performer : "+activity.getPerformer());
						userName=user.getUserName();
						break;
					}
				}
				//tempJobUserAsset.setPerformer(activity.getPerformer());
				//tempJobTaskTag.setPerformer(activity.getPerformer());
				tempJobUserAsset.setPerformer(userName.isBlank()|| userName.isEmpty() ? activity.getGroupOrDeptName()+" (Group)":userName);
				tempJobTaskTag.setPerformer(userName.isBlank()|| userName.isEmpty() ? activity.getGroupOrDeptName()+" (Group)":userName);
				tempJobUserAsset.setActivityId(activity.getActivityId());
				tempJobTaskTag.setActivityId(activity.getActivityId());
				tempJobUserAsset.setActivityName(activity.getActivityName());
				tempJobTaskTag.setActivityName(activity.getActivityName());
				tempJobUserAsset.setActivityStartTime(formatter.format(activity.getActualActivityStartTime()));
				tempJobTaskTag.setActivityStartTime(formatter.format(activity.getActualActivityStartTime()));
				tempJobUserAsset.setActivityStartTime(formatter.format(activity.getActualActivityStartTime()));
				tempJobTaskTag.setActivityStartTime(formatter.format(activity.getActualActivityStartTime()));
				tempJobUserAsset.setActivityStoptime(formatter.format(activity.getActualActivityEndTime()));
				tempJobTaskTag.setActivityStoptime(formatter.format(activity.getActualActivityEndTime()));
				//Activity Elapsed Time
				Date objDate1 = activity.getActualActivityStartTime();
				Date objDate2 = activity.getActualActivityEndTime();
				long time_difference=objDate2.getTime()-objDate1.getTime();
				long days_difference = (time_difference / (1000*60*60*24)) % 365;
				long seconds_difference = (time_difference / 1000)% 60;   
				long minutes_difference = (time_difference / (1000*60)) % 60;   
				long hours_difference = (time_difference / (1000*60*60)) % 24; 
				tempJobUserAsset.setElapsedtime(days_difference+"d:"+hours_difference+"h:"+minutes_difference+"m");
				//Job Elapsed Time
				objDate1=job.getActualJobStartTime();
				objDate2=job.getActualJobEndTime();
				time_difference=objDate2.getTime()-objDate1.getTime();
				days_difference = (time_difference / (1000*60*60*24)) % 365;
				seconds_difference = (time_difference / 1000)% 60;   
				minutes_difference = (time_difference / (1000*60)) % 60;   
				hours_difference = (time_difference / (1000*60*60)) % 24; 
				tempJobUserAsset.setJobElapsedTime(days_difference+"d:"+hours_difference+"h:"+minutes_difference+"m");
				tempJobTaskTag.setJobElapsedTime(days_difference+"d:"+hours_difference+"h:"+minutes_difference+"m");
				//User key wise
				if(userWiseJobMap.containsKey(tempJobUserAsset.getPerformer())) {
					userWiseJobMap.get(tempJobUserAsset.getPerformer()).add(tempJobUserAsset);
				}else {
					userWiseJobMap.put(tempJobUserAsset.getPerformer(), new LinkedList<UserwiseJobDetails>() {{
						add(tempJobUserAsset);
					}});
				}
				//Asset key wise 
				if(assetWiseJobMap.containsKey(tempJobUserAsset.getAssets())) {
					assetWiseJobMap.get(tempJobUserAsset.getAssets()).add(tempJobUserAsset);
				}else {
					assetWiseJobMap.put(tempJobUserAsset.getAssets(), new LinkedList<UserwiseJobDetails>() {{
						add(tempJobUserAsset);
					}});
				}
			}
			/*
			 * 
			 * Add Distinct assets in comma separated way to tempJob1
			 * */
			assetsForTempJobTaskTag="";
			for(String s:assetsTempJobTaskTagLinkedHashSet) {
				assetsForTempJobTaskTag+=s+",";
			}
			if(assetsForTempJobTaskTag.trim().length()>1) {
				tempJobTaskTag.setAssets(assetsForTempJobTaskTag.trim().substring(0,assetsForTempJobTaskTag.trim().length()-1));
			}
			//Group Id key wise
			if(tagWiseJobMap.containsKey(tempJobTaskTag.getGroupId())) {
				tagWiseJobMap.get(tempJobTaskTag.getGroupId()).add(tempJobTaskTag);
			}else {
				tagWiseJobMap.put(tempJobTaskTag.getGroupId(), new LinkedList<UserwiseJobDetails>() {{
					add(tempJobTaskTag);
				}});
			}
			//Task Template key wise
			if(taskWiseJobMap.containsKey(tempJobTaskTag.getTaskTemplate())) {
				taskWiseJobMap.get(tempJobTaskTag.getTaskTemplate()).add(tempJobTaskTag);
			}else {
				taskWiseJobMap.put(tempJobTaskTag.getTaskTemplate(), new LinkedList<UserwiseJobDetails>() {{
					add(tempJobTaskTag);
				}});
			}
		}






		logbookListsSelectItem=logbookLists.stream().distinct().collect(Collectors.toList());
		logbookLists=logbookLists.stream().distinct().collect(Collectors.toList());
		assetListSelectItem=assetListSelectItem.stream().distinct().collect(Collectors.toList());
		assetList=assetListSelectItem;




		return userWiseJobMap;
	}

	@Override
	public Map<String, LinkedList<UserwiseJobDetails>> fetchAssetWiseJob(String tenantId,String fromDate, String toDate) {

		Map<String,LinkedList<UserwiseJobDetails>> userWiseJobMap=new HashMap<String,LinkedList<UserwiseJobDetails>>();
		Map<String,LinkedList<UserwiseJobDetails>> assetWiseJobMap=new HashMap<String,LinkedList<UserwiseJobDetails>>();
		Map<String,LinkedList<UserwiseJobDetails>> tagWiseJobMap=new HashMap<String,LinkedList<UserwiseJobDetails>>(); 
		Map<String,LinkedList<UserwiseJobDetails>> taskWiseJobMap=new HashMap<String,LinkedList<UserwiseJobDetails>>();
		List<JobwiseCardData> jobWiseData;
		List<JobDetails> joblists =new ArrayList<JobDetails>();
		List<User> usersList = new ArrayList<>();
		List<String> assetListSelectItem=new ArrayList<String>();
		LinkedHashSet<String> assetSet=new LinkedHashSet<String>();
		List<String> logbookLists=new ArrayList<String>();
		LinkedHashSet<String> logbookSet=new LinkedHashSet<String>();
		LinkedHashSet<String> logbookselectedSet=new LinkedHashSet<String>();
		List<String> logbookListsSelectItem=new ArrayList<String>();
		List<String> assetList=new ArrayList<String>();

		jobWiseData = new ArrayList<JobwiseCardData>();
		//jobWiseData = dao.fetchJobwiseInfo(dfDt.format(fromDate),dfDt.format(toDate));
		//get all jobs based on the from date and to date
		joblists=fetchAllJobDetails(tenantId,fromDate,toDate);
		usersList=getAllUserInfo(tenantId);
		userWiseJobMap.clear();
		assetWiseJobMap.clear();
		tagWiseJobMap.clear();
		taskWiseJobMap.clear();

		//			onViewChange();
		//			generateTimelineView();


		for(JobDetails job:joblists) {
			job.getTask().getActivityList().stream().distinct().forEach(s->job.setPerformerLists(job.getPerformerLists()+ s.getPerformer()+",")) ;
			String performerArray[]=job.getPerformerLists().split(",");
			LinkedHashSet<String> performerS=new LinkedHashSet<String>();
			for(String s:performerArray) {
				for(User user:usersList){
					if(user.getUserID().equalsIgnoreCase(s)) {		
						performerS.add(user.getUserName());
						break;
					}
				}
			}
			String performer="";
			System.out.println("performerS : "+performerS);
			for(String s:performerS) {
				performer+=","+s;
			}
			//System.out.println("Job Performer= "+performer);
			job.setPerformerLists(performer.trim());
			if(job.getPerformerLists().length()>0)
				job.setPerformerLists( job.getPerformerLists().substring(1,job.getPerformerLists().length()));
		}
		//mapTaskJob=joblists.stream().collect(Collectors.toMap(s->s.getTask().getTaskName(),s->s ));
		//For each job of joblists
		for(JobDetails job:joblists) {
			//System.out.println("Job="+job.toString());
			List<ActivityDetails> activityList=job.getTask().getActivityList();
			/*
			 *  Make  object of UserwiseJobDetails for Task and Tag Selection
			 *  at reference variable tempJobTaskTag
			 * */
			UserwiseJobDetails tempJobTaskTag=new UserwiseJobDetails();
			//Set activity list for tempJobTaskTag
			tempJobTaskTag.setActvityList(job.getTask().getActivityList());
			String assetsForTempJobTaskTag="";
			LinkedHashSet<String> assetsTempJobTaskTagLinkedHashSet=new LinkedHashSet<String>(); 
			/* 
			 * For each activity of job
			 * */
			for(ActivityDetails activity:activityList){ 
				/*
				 * Make object of UserwiseJobDetails for User and Asset selection
				 * at reference variable tempJobUserAsset
				 * */
				UserwiseJobDetails tempJobUserAsset=new UserwiseJobDetails();
				tempJobUserAsset.setActvityList(job.getTask().getActivityList());
				tempJobUserAsset.setJobId(job.getJobID());
				tempJobTaskTag.setJobId(job.getJobID());
				tempJobUserAsset.setGroupId(job.getGroupId());
				System.out.println("job.getGroupId() => "+job.getGroupId());
				tempJobTaskTag.setGroupId(job.getGroupId());
				tempJobUserAsset.setJobName(job.getJobName());
				tempJobTaskTag.setJobName(job.getJobName()); 
				//System.out.println("job.getJobName() : "+job.getJobName());
				tempJobUserAsset.setJobstatus(job.getJobStatus());
				tempJobTaskTag.setJobstatus(job.getJobStatus());
				Format formatter = new SimpleDateFormat("dd/MM/YYYY HH:mm:ss");
				tempJobUserAsset.setJobActualStartTime(formatter.format(job.getActualJobStartTime()));
				tempJobTaskTag.setJobActualStartTime(formatter.format(job.getActualJobStartTime()));
				tempJobUserAsset.setJobActualStopTime(formatter.format(job.getActualJobEndTime()));
				tempJobTaskTag.setJobActualStopTime(formatter.format(job.getActualJobEndTime()));
				tempJobUserAsset.setJobScheduleStartTime(formatter.format(job.getScheduledJobStartTime()));
				tempJobTaskTag.setJobScheduleStartTime(formatter.format(job.getScheduledJobStartTime()));
				tempJobUserAsset.setJobScheduleStopTime(formatter.format(job.getScheduledJobEndTime()));
				tempJobTaskTag.setJobScheduleStopTime(formatter.format(job.getScheduledJobEndTime()));
				tempJobUserAsset.setPriority(job.getPriority());
				tempJobTaskTag.setPriority(job.getPriority());
				tempJobUserAsset.setProgress(job.getProgress().toString());
				tempJobTaskTag.setProgress(job.getProgress().toString());
				tempJobUserAsset.setAssets(activity.getAssetName());
				assetListSelectItem.add(activity.getAssetName());
				assetSet.add(activity.getAssetName());
				assetsTempJobTaskTagLinkedHashSet.add(activity.getAssetName());
				tempJobUserAsset.setTaskTemplate(job.getTask().getTaskName());
				tempJobTaskTag.setTaskTemplate(job.getTask().getTaskName().trim().length()!=0?job.getTask().getTaskName():"Self Assigned");
				tempJobUserAsset.setCurrentActivity(job.getCurrentActivityName());
				tempJobTaskTag.setCurrentActivity(job.getCurrentActivityName());
				String userName="";
				for(User user:usersList) {
					if(user.getUserID().equalsIgnoreCase(job.getCurrentPerformer())) {
						userName=user.getUserName();
						break;
					}
				}
				tempJobUserAsset.setCurrentPerformer(userName);
				tempJobTaskTag.setCurrentPerformer(userName);
				//tempJobUserAsset.setCurrentPerformer(job.getCurrentPerformer());
				//tempJobTaskTag.setCurrentPerformer(job.getCurrentPerformer());
				List<String> initialList=new ArrayList<String>();
				initialList=Arrays.asList(job.getPerformerLists().split(","));
				tempJobTaskTag.setInitial(initialList);
				//System.out.println("Hm="+hm.toString());
				//tempJobTaskTag.setUserNameList(userNameList);
				tempJobUserAsset.setPerformers(job.getPerformerLists());
				tempJobTaskTag.setPerformers(job.getPerformerLists());
				tempJobUserAsset.setLogBook(activity.getLogbook());
				tempJobTaskTag.setLogBook(activity.getLogbook());
				logbookLists.add(activity.getLogbook());
				logbookSet.add(activity.getLogbook());
				logbookselectedSet.add(activity.getLogbook());
				userName="";
				for(User user:usersList) {
					if(user.getUserID().equalsIgnoreCase(activity.getPerformer())) {
						userName=user.getUserName();
						break;
					}
				}
				//tempJobUserAsset.setPerformer(activity.getPerformer());
				//tempJobTaskTag.setPerformer(activity.getPerformer());
				tempJobUserAsset.setPerformer(activity.getPerformer().isBlank() || activity.getPerformer().isEmpty() ? activity.getGroupOrDeptName()+" (Group)":activity.getPerformer());
				tempJobTaskTag.setPerformer(activity.getPerformer().isBlank() || activity.getPerformer().isEmpty() ? activity.getGroupOrDeptName()+" (Group)":activity.getPerformer());
				tempJobUserAsset.setActivityId(activity.getActivityId());
				tempJobTaskTag.setActivityId(activity.getActivityId());
				tempJobUserAsset.setActivityName(activity.getActivityName());
				tempJobTaskTag.setActivityName(activity.getActivityName());
				tempJobUserAsset.setActivityStartTime(formatter.format(activity.getActualActivityStartTime()));
				tempJobTaskTag.setActivityStartTime(formatter.format(activity.getActualActivityStartTime()));
				tempJobUserAsset.setActivityStartTime(formatter.format(activity.getActualActivityStartTime()));
				tempJobTaskTag.setActivityStartTime(formatter.format(activity.getActualActivityStartTime()));
				tempJobUserAsset.setActivityStoptime(formatter.format(activity.getActualActivityEndTime()));
				tempJobTaskTag.setActivityStoptime(formatter.format(activity.getActualActivityEndTime()));
				//Activity Elapsed Time
				Date objDate1 = activity.getActualActivityStartTime();
				Date objDate2 = activity.getActualActivityEndTime();
				long time_difference=objDate2.getTime()-objDate1.getTime();
				long days_difference = (time_difference / (1000*60*60*24)) % 365;
				long seconds_difference = (time_difference / 1000)% 60;   
				long minutes_difference = (time_difference / (1000*60)) % 60;   
				long hours_difference = (time_difference / (1000*60*60)) % 24; 
				tempJobUserAsset.setElapsedtime(days_difference+"d:"+hours_difference+"h:"+minutes_difference+"m");
				//Job Elapsed Time
				objDate1=job.getActualJobStartTime();
				objDate2=job.getActualJobEndTime();
				time_difference=objDate2.getTime()-objDate1.getTime();
				days_difference = (time_difference / (1000*60*60*24)) % 365;
				seconds_difference = (time_difference / 1000)% 60;   
				minutes_difference = (time_difference / (1000*60)) % 60;   
				hours_difference = (time_difference / (1000*60*60)) % 24; 
				tempJobUserAsset.setJobElapsedTime(days_difference+"d:"+hours_difference+"h:"+minutes_difference+"m");
				tempJobTaskTag.setJobElapsedTime(days_difference+"d:"+hours_difference+"h:"+minutes_difference+"m");
				//User key wise
				if(userWiseJobMap.containsKey(tempJobUserAsset.getPerformer())) {
					userWiseJobMap.get(tempJobUserAsset.getPerformer()).add(tempJobUserAsset);
				}else {
					userWiseJobMap.put(tempJobUserAsset.getPerformer(), new LinkedList<UserwiseJobDetails>() {{
						add(tempJobUserAsset);
					}});
				}
				//Asset key wise 
				if(assetWiseJobMap.containsKey(tempJobUserAsset.getAssets())) {
					assetWiseJobMap.get(tempJobUserAsset.getAssets()).add(tempJobUserAsset);
				}else {
					assetWiseJobMap.put(tempJobUserAsset.getAssets(), new LinkedList<UserwiseJobDetails>() {{
						add(tempJobUserAsset);
					}});
				}
			}
			/*
			 * 
			 * Add Distinct assets in comma separated way to tempJob1
			 * */
			assetsForTempJobTaskTag="";
			for(String s:assetsTempJobTaskTagLinkedHashSet) {
				assetsForTempJobTaskTag+=s+",";
			}
			if(assetsForTempJobTaskTag.trim().length()>1) {
				tempJobTaskTag.setAssets(assetsForTempJobTaskTag.trim().substring(0,assetsForTempJobTaskTag.trim().length()-1));
			}
			//Group Id key wise
			if(tagWiseJobMap.containsKey(tempJobTaskTag.getGroupId())) {
				tagWiseJobMap.get(tempJobTaskTag.getGroupId()).add(tempJobTaskTag);
			}else {
				tagWiseJobMap.put(tempJobTaskTag.getGroupId(), new LinkedList<UserwiseJobDetails>() {{
					add(tempJobTaskTag);
				}});
			}
			//Task Template key wise
			if(taskWiseJobMap.containsKey(tempJobTaskTag.getTaskTemplate())) {
				taskWiseJobMap.get(tempJobTaskTag.getTaskTemplate()).add(tempJobTaskTag);
			}else {
				taskWiseJobMap.put(tempJobTaskTag.getTaskTemplate(), new LinkedList<UserwiseJobDetails>() {{
					add(tempJobTaskTag);
				}});
			}
		}






		logbookListsSelectItem=logbookLists.stream().distinct().collect(Collectors.toList());
		logbookLists=logbookLists.stream().distinct().collect(Collectors.toList());
		assetListSelectItem=assetListSelectItem.stream().distinct().collect(Collectors.toList());
		assetList=assetListSelectItem;




		return assetWiseJobMap;
	}

public Map<String, List<JobwiseCardData>> fetchAllCardData(String tenantId, String fromDate, String toDate, String groupType) {
    // Initialize a LinkedHashMap to store the grouped JobwiseCardData
    Map<String, List<JobwiseCardData>> dataMap = new LinkedHashMap<String, List<JobwiseCardData>>();

    // Create a list to store all unique groups
    List<String> allGroups = new ArrayList<String>();

    // Retrieve job-wise data based on tenant ID and date range
    List<JobwiseCardData> jobWiseData = fetchJobwiseInfo(tenantId, fromDate, toDate);

    // Initialize a new LinkedHashMap to hold the grouped data
    dataMap = new LinkedHashMap<String, List<JobwiseCardData>>();

    // Determine the grouping method based on the groupType parameter
    if (groupType.equalsIgnoreCase("Temp")) {
        // For "Temp" group type, collect unique performers
        for (JobwiseCardData data : jobWiseData) {
            for (String u : data.getPerformers()) {
                // Add unique performers to the allGroups list
                if (!allGroups.contains(u)) {
                    allGroups.add(u);
                }
            }
        }

        // Group job-wise data by performers
        for (String u : allGroups) {
            for (JobwiseCardData data : jobWiseData) {
                // Check if the data contains the current performer
                if (data.getPerformers().contains(u)) {
                    // If the performer already exists in dataMap, append the data
                    if (dataMap.containsKey(u)) {
                        List<JobwiseCardData> jobWiseData1 = new ArrayList<JobwiseCardData>();
                        jobWiseData1.addAll(dataMap.get(u));
                        jobWiseData1.add(data);
                        dataMap.put(u, jobWiseData1);
                    } else {
                        // Create a new list for this performer and add the data
                        List<JobwiseCardData> jobWiseData1 = new ArrayList<JobwiseCardData>();
                        jobWiseData1.add(data);
                        dataMap.put(u, jobWiseData1);
                    }
                }
            }
        }
    } else if (groupType.equalsIgnoreCase("Task")) {
        // For "Task" group type, collect unique task names
        for (JobwiseCardData data : jobWiseData) {
            System.out.println("My Status : " + data.getStatusMsg());
            if (!allGroups.contains(data.getTaskName())) {
                allGroups.add(data.getTaskName());
            }
        }

        // Group job-wise data by task names
        for (String u : allGroups) {
            for (JobwiseCardData data : jobWiseData) {
                // Check if the task name matches the current group
                if ((data.getTaskName() != null ? data.getTaskName() : " ").equalsIgnoreCase(u != null ? u : " ")) {
                    // If the task name already exists in dataMap, append the data
                    if (dataMap.containsKey(u != null ? u : " ")) {
                        List<JobwiseCardData> jobWiseData1 = new ArrayList<JobwiseCardData>();
                        jobWiseData1.addAll(dataMap.get(u));
                        jobWiseData1.add(data);
                        dataMap.put(u, jobWiseData1);
                    } else {
                        // Create a new list for this task and add the data
                        List<JobwiseCardData> jobWiseData1 = new ArrayList<JobwiseCardData>();
                        jobWiseData1.add(data);
                        dataMap.put(u, jobWiseData1);
                    }
                }
            }
        }
    } else if (groupType.equalsIgnoreCase("Asset")) {
        // For "Asset" group type, collect unique asset names
        for (JobwiseCardData data : jobWiseData) {
            // If asset is null, set it to "NA"
            if (data.getAsset() == null) {
                data.setAsset("NA");
            }
            if (!allGroups.contains(data.getAsset())) {
                allGroups.add(data.getAsset());
            }
        }

        // Group job-wise data by asset names
        for (String u : allGroups) {
            for (JobwiseCardData data : jobWiseData) {
                // Check if the asset matches the current group
                if (data.getAsset().equalsIgnoreCase(u)) {
                    // If the asset already exists in dataMap, append the data
                    if (dataMap.containsKey(u)) {
                        List<JobwiseCardData> jobWiseData1 = new ArrayList<JobwiseCardData>();
                        jobWiseData1.addAll(dataMap.get(u));
                        jobWiseData1.add(data);
                        dataMap.put(u, jobWiseData1);
                    } else {
                        // Create a new list for this asset and add the data
                        List<JobwiseCardData> jobWiseData1 = new ArrayList<JobwiseCardData>();
                        jobWiseData1.add(data);
                        dataMap.put(u, jobWiseData1);
                    }
                }
            }
        }
    } else if (groupType.equalsIgnoreCase("Tag")) {
        // For "Tag" group type, collect unique group IDs
        for (JobwiseCardData data : jobWiseData) {
            // If asset is null, set it to "NA"
            if (data.getAsset() == null) {
                data.setAsset("NA");
            }
            if (!allGroups.contains(data.getGroupId())) {
                allGroups.add(data.getGroupId());
            }
        }

        // Group job-wise data by group IDs
        for (String u : allGroups) {
            for (JobwiseCardData data : jobWiseData) {
                // Check if the group ID matches the current group
                if (data.getGroupId().equalsIgnoreCase(u)) {
                    // If the group ID already exists in dataMap, append the data
                    if (dataMap.containsKey(u)) {
                        List<JobwiseCardData> jobWiseData1 = new ArrayList<JobwiseCardData>();
                        jobWiseData1.addAll(dataMap.get(u));
                        jobWiseData1.add(data);
                        dataMap.put(u, jobWiseData1);
                    } else {
                        // Create a new list for this group ID and add the data
                        List<JobwiseCardData> jobWiseData1 = new ArrayList<JobwiseCardData>();
                        jobWiseData1.add(data);
                        dataMap.put(u, jobWiseData1);
                    }
                }
            }
        }
    } else if (groupType.equalsIgnoreCase("User")) {
        // For "User" group type, collect unique instrument names
        for (JobwiseCardData data : jobWiseData) {
            // If instrument is null, set it to "NA"
            if (data.getInstrument() == null) {
                data.setInstrument("NA");
            }
            if (!allGroups.contains(data.getInstrument())) {
                allGroups.add(data.getInstrument());
            }
        }

        // Group job-wise data by instrument names
        for (String u : allGroups) {
            for (JobwiseCardData data : jobWiseData) {
                // Check if the instrument matches the current group
                if (data.getInstrument().equalsIgnoreCase(u)) {
                    // If the instrument already exists in dataMap, append the data
                    if (dataMap.containsKey(u)) {
                        List<JobwiseCardData> jobWiseData1 = new ArrayList<JobwiseCardData>();
                        jobWiseData1.addAll(dataMap.get(u));
                        jobWiseData1.add(data);
                        dataMap.put(u, jobWiseData1);
                    } else {
                        // Create a new list for this instrument and add the data
                        List<JobwiseCardData> jobWiseData1 = new ArrayList<JobwiseCardData>();
                        jobWiseData1.add(data);
                        dataMap.put(u, jobWiseData1);
                    }
                }
            }
        }
    }

    // Return the final map containing grouped job-wise data
    return dataMap;
}


@Override
public List<Map<String, Object>> fetchJobStatusByTicketNo(String tenantId, String ticketNo) {
	JdbcTemplate jdbcTemplate=jdbcTemplates.get(tenantId).get(0);
	// SQL query to select the top record from the JobDetails table for a specific task name and tenant ID
   
	String sql = "SELECT  [TaskId], [JobId], [JobName], [GroupId], [Assigner], [Approver], " +
                 "[Priority], [Remarks], [Status], TenantId " +
                 "FROM [dbo].[JobDetails] jd " +
                 "WHERE CHARINDEX('_', jd.[JobName]) > 0 \r\n"
                 + "AND LEFT(jd.[JobName], CHARINDEX('_', jd.[JobName]) - 1) = ? AND TenantId = ?";

    // Execute the query and retrieve the result as a list of maps
    List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, ticketNo, tenantId);

    // Return the result; if no records are found, an empty list will be returned
    return result;
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




