package in.co.greenwave.jobapi.dao.implementation.sqlserver;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import in.co.greenwave.jobapi.dao.ScanJobDAO;
import in.co.greenwave.jobapi.model.AssetModel;
import in.co.greenwave.jobapi.model.AutoJob;


@Repository
public class ScanJobService implements ScanJobDAO {

	static final String default_prefix="SelfJob created with";
	
	

	@Autowired
	@Qualifier("DatasourceCollections")
	Map<String, List<JdbcTemplate>> jdbcTemplates; // Template for operations on the main database
	
	
	
//this method returns the assetInfo with respect to a current assetId and tenantId
	@Override
	public AssetModel getLogbooksofAsset(String assetId,String tenantId) {
		System.out.println("getLogbooksofAsset()");
		// Query to get assetInfo
		String sql = "Select TOP 1 * FROM [dbo].[AssetInfo] Where AssetId = ? AND [TenantId]=?";
		AssetModel model = new AssetModel();
		JdbcTemplate jdbcTemplate=jdbcTemplates.get(tenantId).get(0);
		jdbcTemplate.query(sql, new RowCallbackHandler() {
			
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				// TODO Auto-generated method stub
				
				do//here top 1 query is written so the loop will iterate only one time
				{
					String logbooksListString = rs.getString("logbooks");
					System.out.println(rs.getString("logbooks"));
					String usersListString = rs.getString("users");
					//model = new AssetModel(rs.getString("AssetId"), rs.getString("AssetName"), false, rs.getString("Icon"), rs.getString("Category"), rs.getString("Image"));
					model.setNodeName(rs.getString("AssetId"));
					model.setAliasName(rs.getString("AssetName"));
					model.setChecked(false);
					model.setIcon( rs.getString("Icon"));
					model.setCategory(rs.getString("Category"));
					model.setImage(rs.getString("Image"));

					// Check if logbooksListString is not null before processing
					if (logbooksListString != null) {

					    // Split the logbooksListString by commas, trimming any surrounding spaces
					    // Convert the resulting array into a List of Strings
					    List<String> logbooksList = Arrays.asList(logbooksListString.split("\\s*,\\s*"));
					    System.out.println("logbooksList after split : " + logbooksList);

					    // Create a new list to store the integer values of logbooks
					    List<Integer> logbooks = new LinkedList<Integer>();

					    // If logbooksList contains more than one element, process each element
					    if (logbooksList.size() > 1) {

					        // Iterate over the list of logbook strings and convert each one to an integer
					        for (String book : logbooksList) {
					            logbooks.add(Integer.parseInt(book));  // Parse each string to an integer and add to the list
					        }
					    }

					    // Set the list of logbooks in the model
					    model.setLogbooksList(logbooks);
					}

					System.out.println("Logbook List:"+model.getLogbooksList());
					System.out.println("Users List String : "+usersListString.length());
					// Check if usersListString is not null and has a length greater than 0 before processing
					if (usersListString != null && usersListString.length() > 0) {

					    // Split the usersListString by commas, trimming any surrounding spaces
					    // Convert the resulting array into a List of Strings
					    List<String> usersList = Arrays.asList(usersListString.split("\\s*,\\s*"));

					    // Set the list of users in the model
					    model.setUsersList(usersList);
					}

					else
					{
						model.setUsersList(new ArrayList<>());
					}
					System.out.println("Users List : "+model.getUsersList());
				}
				while(rs.next());
			}
			
		},assetId,tenantId);
		System.out.println("Returned Asset Model => "+model);
		return model;
	}

	
	// this method returns the AutoJob information which is pending state i.e the jobs which are started based on particular assetid and tenantid
	@Override
	public AutoJob getPendingJobInfoforAssetId(String assetid,String tenantId) {
		SimpleDateFormat dfDtTm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// here SimpleDateFormat is used to inject the datetime into the object properties as String 
		JdbcTemplate jdbcTemplate=jdbcTemplates.get(tenantId).get(0);
		//Query to get pending AutoJob information 
		        String sql = "SELECT * FROM [dbo].[AutoJobInfo] WHERE assetid = ? AND [status] NOT IN ('Approved','Rejected') AND [TenantId]=?";
		        AutoJob job=new AutoJob();
		        //return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(AutoJob.class), assetid);
				jdbcTemplate.query(sql, new RowCallbackHandler() {
				
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					// TODO Auto-generated method stub	
					do
					{
						job.setJobid(rs.getString("jobid"));
						job.setActivityid(rs.getString("activityid"));
						job.setJobname(rs.getString("name"));
						job.setAssetid( rs.getString("assetid"));
						job.setAssetname(rs.getString("assetname"));
						job.setLogbookid( rs.getInt("logbookid"));
						job.setFormname(rs.getString("formname"));
						job.setLogbookversion(rs.getInt("version"));
						job.setPerformer(rs.getString("performer"));
						job.setReviewer(rs.getString("reviewer"));
						try {
							job.setActivitystarttime(rs.getString("activitystarttime") != null ? dfDtTm.parse(rs.getString("activitystarttime")) : null);
							job.setActivityendtime(rs.getString("activityendtime") != null ? dfDtTm.parse(rs.getString("activityendtime")) : null);
							job.setReviewstarttime(rs.getString("reviewstarttime") != null ? dfDtTm.parse(rs.getString("reviewstarttime")) : null);
							job.setReviewendtime(rs.getString("reviewendtime") != null ? dfDtTm.parse(rs.getString("reviewendtime")) : null);

						} catch (ParseException | SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						job.setStatus(rs.getString("status"));
					}while(rs.next());
				}
				},assetid,tenantId);
				return job;
	}


	//this method returns a boolean value after updating the Autojob endtime based on jobId , activityId and assetId
	@Override
	public boolean updateJobEndTime(String jobId, String activityId, String assetid,String tenantId,Date time) {
		System.out.println("JobId : "+jobId+" ActivityId : "+activityId+" AssetId : "+assetid);
		System.out.println("Activity End Time : "+time);
		int count=0;
		JdbcTemplate jdbcTemplate=jdbcTemplates.get(tenantId).get(0);
		boolean allright = false;
		//Sql to update autojob activityendtime based on activityid,jobid and tenantId
		String sql = "UPDATE [dbo].[AutoJobInfo] SET activityendtime = ?, [status] = IIF([performer]<> [reviewer], 'Sent for approval','Approved') Where jobid = ? AND activityid = ? AND [TenantId]=?";
			count=jdbcTemplate.update(sql,new Timestamp(time.getTime()),jobId,activityId,tenantId);
			//if the update is successfully processed then the count will be greater than 0
			allright=count>0?true:false;
//		}	
		return allright;
	}
	
	

	//creates a new AutoJob object and insert those information into the database and returns true or false based on whether the query is processed successfully or not
	@Override
	public boolean initialteNewAutoJob(AutoJob job,String tenantId) {
		System.out.println("initiateNewAutoJob() " + job);
		System.out.println("Current Activity Start : "+job.getActivitystarttime());

	    // Query to check if the job name already exists
		String checkSql = "SELECT COUNT(*) FROM [dbo].[AutoJobInfo] " +
                "WHERE [name] LIKE ? " + 
                "AND CAST(activitystarttime AS DATE) = CAST(? AS DATE) " +
                "AND performer = ? " +
                "AND [formname] = ?";

		JdbcTemplate jdbcTemplate=jdbcTemplates.get(tenantId).get(0);
	    int existingCount = jdbcTemplate.queryForObject(checkSql, Integer.class, default_prefix+" "+job.getFormname()+"%",new Timestamp(job.getActivitystarttime().getTime()),job.getPerformer(),job.getFormname());
	    System.out.println("Query FormName Parameter : "+default_prefix+" "+job.getFormname()+"%");

	    final String initial_job_name=job.getJobname();
	    System.out.println("Current FormName : "+initial_job_name);
	    System.out.println("Existing Count : "+existingCount);

		if(existingCount > 0 && job.getJobname().startsWith(default_prefix))
		{
        Integer lastNumber = extractLastInteger(job.getJobname());

        if (lastNumber != null && lastNumber != (existingCount + 1) ) {
            System.err.println("Job name '" + job.getJobname() + "' already exists in database! Changing the job name with the latest count.");

            job.setJobname(job.getJobname().replaceFirst("(\\d+)$", String.valueOf(existingCount + 1)));

            System.out.println("New Job Name: " + job.getJobname());
        
    }
		}
    System.out.println(initial_job_name.equalsIgnoreCase(job.getJobname())?"Default Jobname":"Jobname updated :"+job.getJobname());
	    // Query to insert new AutoJob information
	    String insertSql = "INSERT INTO [dbo].[AutoJobInfo] " +
	            "([jobid], [activityid], [name], [assetid], [assetname], [logbookid], [formname], [version], " +
	            "[performer], [reviewer], [activitystarttime], [status], [TenantId]) " + 
	            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	    int count = jdbcTemplate.update(insertSql,
	            job.getJobid(),
	            job.getActivityid(),
	            job.getJobname(),
	            job.getAssetid(),
	            job.getAssetname(),
	            job.getLogbookid(),
	            job.getFormname(),
	            job.getLogbookversion(),
	            job.getPerformer(),
	            job.getReviewer(),
	            new Timestamp(job.getActivitystarttime().getTime()),
	            job.getStatus(),
	            tenantId
	    );

	    System.out.println("Current Insert count: " + count);
	    
	    return count > 0;
	}

	private Integer extractLastInteger(String jobname) {
		// TODO Auto-generated method stub
		Pattern pattern = Pattern.compile("(\\d+)\\s*$"); // Matches last number at the end
        Matcher matcher = pattern.matcher(jobname);

        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1)); // Extract and convert to integer
        }
        return null; // Return null if no number is found
	}


	//
	@Override
	public AutoJob getPendingJobInfoforLogbook(String formname, int formid, int version, String user,String tenantId) {
		// TODO Auto-generated method stub
		SimpleDateFormat dfDtTm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// here SimpleDateFormat is used to inject the datetime into the object properties as String 
		String query = "Select top 1 *  from [dbo].[AutoJobInfo] Where [formname] = ? AND [logbookid] = ? AND [version] = ? AND [performer] = ? AND [TenantId]=? AND [assetid] IS NULL AND [status] NOT IN ('Approved','Rejected') order by [activitystarttime] desc";
		AutoJob job=new AutoJob();
		System.out.println("Final Query : "+query);
		JdbcTemplate jdbcTemplate=jdbcTemplates.get(tenantId).get(0);
		// Execute the query using jdbcTemplate and handle the result set with a RowCallbackHandler
		jdbcTemplate.query(query, new RowCallbackHandler() {
		    
		    // Override the processRow method to define how each row in the ResultSet should be processed
		    @Override
		    public void processRow(ResultSet rs) throws SQLException {
		        // Logic to process each row of the ResultSet goes here
			do
			{
				job.setJobid(rs.getString("jobid"));
				job.setActivityid(rs.getString("activityid"));
				job.setJobname(rs.getString("name"));
				job.setAssetid( rs.getString("assetid"));
				job.setAssetname(rs.getString("assetname"));
				job.setLogbookid( rs.getInt("logbookid"));
				job.setFormname(rs.getString("formname"));
				job.setLogbookversion(rs.getInt("version"));
				job.setPerformer(rs.getString("performer"));
				job.setReviewer(rs.getString("reviewer"));
				try {
					job.setActivitystarttime(dfDtTm.parse(rs.getString("activitystarttime")));
					String activityEndTimeStr = rs.getString("activityendtime");
					if (activityEndTimeStr == null || activityEndTimeStr.isEmpty()) {
					    job.setActivityendtime(null);
					} else {
					    job.setActivityendtime(dfDtTm.parse(activityEndTimeStr));
					}
				} catch (ParseException | SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				job.setStatus(rs.getString("status"));
			}while(rs.next());
		}
		},formname,formid,version,user,tenantId);
		return job;
	}
	
//return List<AutoJob> for a given user that are still pending to be approved
	@Override
	public List<AutoJob> getPendingforApprovalSelfAssignedJobs(String userid,String tenantId) {
		// TODO Auto-generated method stub
		List<AutoJob> data = new ArrayList<AutoJob>();
		//Query to get all the AutoJobs that are still pending to be approved
		String query = "Select * from [dbo].[AutoJobInfo] Where [reviewer] = ? AND [status] IN ('Sent for approval','Review Started') and TenantId=?";
		SimpleDateFormat dfDtTm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		JdbcTemplate jdbcTemplate=jdbcTemplates.get(tenantId).get(0);
		jdbcTemplate.query(query, new RowCallbackHandler() {
			
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				// TODO Auto-generated method stub	
				do
				{
					AutoJob job=new AutoJob();
					job.setJobid(rs.getString("jobid"));
					job.setActivityid(rs.getString("activityid"));
					job.setJobname(rs.getString("name"));
					job.setAssetid( rs.getString("assetid"));
					job.setAssetname(rs.getString("assetname"));
					job.setLogbookid( rs.getInt("logbookid"));
					job.setFormname(rs.getString("formname"));
					job.setLogbookversion(rs.getInt("version"));
					job.setPerformer(rs.getString("performer"));
					job.setReviewer(rs.getString("reviewer"));
					
					try {
						job.setActivitystarttime(dfDtTm.parse(rs.getString("activitystarttime")));
						job.setActivitystarttime(dfDtTm.parse(rs.getString("activitystarttime")));
						String activityEndTimeStr = rs.getString("activityendtime");
						if (activityEndTimeStr == null || activityEndTimeStr.isEmpty()) {
						    job.setActivityendtime(null);
						} else {
						    job.setActivityendtime(dfDtTm.parse(activityEndTimeStr));
						}
					} catch (ParseException | SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					job.setStatus(rs.getString("status"));
					data.add(job);
				}while(rs.next());
			}
			},userid,tenantId);
		return data;
	}
	
	//query to update the job status and remarks based on jobid,assetid,activityId
	@Override
	public boolean updateStatusForSelfAssignedJob(String jobId, String activityId, String assetid,String status,String remarks,String tenantId,Date reviewerStart) {
		// TODO Auto-generated method stub
		String query = "DECLARE @status varchar(max)=?,@remarks varchar(max)=?,@jobid varchar(max)=?,@activityid varchar(max)=?,@assetid varchar(max)=?,@tenantid varchar(max)=? ; if(@status='Review Started') UPDATE dbo.[AutoJobInfo] SET [reviewstarttime] = ?, [status] = @status,[remarks] = @remarks Where jobid = @jobid AND activityid = @activityid  and TenantId=@tenantid; else if(@status='Approved' or @status='Rejected') UPDATE dbo.[AutoJobInfo] SET [reviewstarttime] = ?, [status] = @status,[remarks] = @remarks Where jobid = @jobid AND activityid = @activityid and TenantId=@tenantid; else UPDATE dbo.[AutoJobInfo] SET  [status] = @status,[remarks] = @remarks Where jobid = @jobid AND activityid = @activityid and TenantId=@tenantid;";
		JdbcTemplate jdbcTemplate=jdbcTemplates.get(tenantId).get(0);
		int update=jdbcTemplate.update(query,status,remarks, jobId, activityId, assetid,tenantId,
				new Timestamp(reviewerStart.getTime()),
				new Timestamp(reviewerStart.getTime())
				);
		return update>0?true:false;
	}


	@Override
	public List<AutoJob> getAllSelfJobs(String tenantId) {
		// TODO Auto-generated method stub
		String query = "SELECT * FROM [dbo].[AutoJobInfo] where TenantId=?";
		
		List<AutoJob> allJobs=new ArrayList<>();
		JdbcTemplate jdbcTemplate=jdbcTemplates.get(tenantId).get(0);
		SimpleDateFormat dfDtTm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		jdbcTemplate.query(query, new RowCallbackHandler() {
			
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				// TODO Auto-generated method stub	
				do
				{
					AutoJob job=new AutoJob();
					job.setJobid(rs.getString("jobid"));
					job.setActivityid(rs.getString("activityid"));
					job.setJobname(rs.getString("name"));
					job.setAssetid( rs.getString("assetid"));
					job.setAssetname(rs.getString("assetname"));
					job.setLogbookid( rs.getInt("logbookid"));
					job.setFormname(rs.getString("formname"));
					job.setLogbookversion(rs.getInt("version"));
					job.setPerformer(rs.getString("performer"));
					job.setReviewer(rs.getString("reviewer"));
					
					try {
						job.setActivitystarttime(dfDtTm.parse(rs.getString("activitystarttime")));
						job.setActivitystarttime(dfDtTm.parse(rs.getString("activitystarttime")));
						String activityEndTimeStr = rs.getString("activityendtime");
						if (activityEndTimeStr == null || activityEndTimeStr.isEmpty()) {
						    job.setActivityendtime(null);
						} else {
						    job.setActivityendtime(dfDtTm.parse(activityEndTimeStr));
						}
					} catch (ParseException | SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					job.setStatus(rs.getString("status"));
					allJobs.add(job);
				}while(rs.next());
			}
			},tenantId);
		
		
		return allJobs;

	}


}
