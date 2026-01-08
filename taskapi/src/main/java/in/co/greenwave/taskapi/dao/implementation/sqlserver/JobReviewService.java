package in.co.greenwave.taskapi.dao.implementation.sqlserver;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import in.co.greenwave.taskapi.dao.JobReviewDAO;
import in.co.greenwave.taskapi.model.ApproverCardData;
@Repository
public class JobReviewService implements JobReviewDAO 
{
	@Autowired
	@Qualifier("jdbcTemplate1")
	public JdbcTemplate jdbcTemplate;
	@Autowired
	@Qualifier("jdbcTemplate2")
	public JdbcTemplate jdbcTemplate2;
	
	@Override
	public int fetchCompletedJobCount(String reviewer,String tenantId) {
		try {
		String sql="SELECT Count(*)  FROM [dbo].[JobDetails] where Approver=? and Status='Completed' and TenantId=?\r\n"
				+ "";
		int completedjobcount=jdbcTemplate.queryForObject(sql,Integer.class,reviewer,tenantId);
		System.out.println("Completed job count:"+completedjobcount);
		return completedjobcount;
		}
		catch (Exception e) {
			// TODO: handle exception
			System.err.println(e);
			return 0;
		}
	}

	//	private final G360Alert gAlert=new G360Alert();
	//	DateFormat dfDt = new SimpleDateFormat("yyyy-MM-dd");
	//	DateFormat dfDtTm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	//	Connection con = null;
	//	PreparedStatement pstmt = null;
	//	
	//	
	//	@Override
	//	public List<JobDetails> pendingForReviewJobs(String user){
	//		List<JobDetails> jobs = new LinkedList<JobDetails>();
	//		System.out.println("Dao.pendingForReviewJobs()"+user);
	//		String sql = "SELECT [TaskId] ,[JobId] ,[TaskName] ,[Instrument] ,[Approver] ,[ScheduleStart] ,[ScheduleStop] ,[ActualStart] ,[ActualStop] ,[ReviewerIntimationTime] ,[ReviewercompletionTime] ,[Priority] ,[Repeat] ,[RepeatTill] ,[Remarks] ,[Status],curr_activityId,curr_activityName,curr_performer FROM [dbo].[JobDetails] CROSS APPLY dbo.getcurrentInfo(JobId) Where  Approver= ? AND [Status] <> 'Finished'";
	//		String actSql = "SELECT [JobId] ,job.[ActivityId],job.[TaskId],ActivityName,[Sequence] ,[LogbookName] ,[Performer] ,[Approver] ,[ScheduledStart] ,[ScheduleStop] ,[ActualStart] ,[ActualStop] ,[Status],Duration_min,[Asset] = (Select Asset from [dbo].[ActivityCreation] cr Where cr.TaskId = act.TaskId AND cr.ActivityId = act.ActivityId) FROM [dbo].[JobActivityDetails] job,dbo.ActivityCreation act  Where job.ActivityId = act.ActivityId AND job.TaskId = act.TaskId AND [JobId] = ? Order by [Sequence]";
	//		try {
	//			con = SQLServerDAOFactory.createConnection();
	//			pstmt = con.prepareStatement(sql);
	//			pstmt.setString(1, user);
	//			ResultSet rs = pstmt.executeQuery();
	//			while (rs.next()) {
	//				JobDetails jobD=new JobDetails();
	//				TaskDetail taskD=new TaskDetail();
	//				taskD.setTaskId(rs.getString("TaskId"));
	//				taskD.setTaskName(rs.getString("TaskName"));
	//				jobD.setTask(taskD);
	//				jobD.setJobID(rs.getString("JobId"));
	//				jobD.setApprover(rs.getString("Approver"));
	//				SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");   
	//				Date StrtD=format.parse(rs.getString("ScheduleStart")); 
	//				Date stpD=format.parse(rs.getString("ScheduleStop")); 
	//				jobD.setScheduledJobStartTime(StrtD);
	//				jobD.setScheduledJobEndTime(stpD);
	//				jobD.setPriority(rs.getString("Priority"));
	//				jobD.setJobStatus(rs.getString("Status"));
	//				jobD.setReviewerIntimationTime(rs.getString("ReviewerIntimationTime")!= null ? format.parse(rs.getString("ReviewerIntimationTime")) : format.parse("1900-01-01 00:00:00"));
	//				jobD.setCurrentActivityName(rs.getString("curr_activityName"));
	//				jobD.setCurrentPerformer(rs.getString("curr_performer"));
	//				
	//				///Adding Activities
	//
	//				PreparedStatement pstmtAct = con.prepareStatement(actSql);
	//				pstmtAct.setString(1, rs.getString("JobId"));
	//				ResultSet rsAct = pstmtAct.executeQuery();
	//				List<ActivityDetails> activityList = new LinkedList<ActivityDetails>();
	//				while(rsAct.next()) {
	//					ActivityDetails act = new ActivityDetails(rsAct.getString("TaskId"), rsAct.getString("ActivityId"), rsAct.getString("ActivityName"), rsAct.getInt("Sequence"), rsAct.getString("LogbookName"), rsAct.getInt("Duration_min"),rsAct.getString("Asset"));
	//					act.setScheduledActivityStartTime(format.parse(rsAct.getString("ScheduledStart")));
	//					act.setScheduledActivityEndTime(format.parse(rsAct.getString("ScheduleStop")));
	//					act.setActualActivityStartTime(rsAct.getString("ActualStart")!= null ? format.parse(rsAct.getString("ActualStart")):  format.parse("1900-01-01 00:00:00"));
	//					act.setActualActivityEndTime(rsAct.getString("ActualStop") != null? format.parse(rsAct.getString("ActualStop")) : format.parse("1900-01-01 00:00:00"));
	//					act.setActvityStatus(rsAct.getString("Status"));
	//					activityList.add(act);
	//					
	//					if(act.getActivityName().equalsIgnoreCase(rs.getString("curr_activityName"))) {
	//						int t = ((int)(new Date().getTime() - format.parse(rsAct.getString("ScheduledStart")).getTime()))/60000; 
	//						jobD.setActivityWaitingTime(t);
	//					}
	//				}
	//				TaskDetail task = new TaskDetail(rs.getString("TaskName"), rs.getString("TaskId"), activityList);
	//				jobD.setTask(task);
	//				jobs.add(jobD);	 
	//			}
	//
	//		} catch (Exception e) {
	//			e.printStackTrace();
	//		} finally {
	//			try {
	//				con.close();
	//				pstmt.close();
	//			} catch (Exception e2) {
	//				e2.printStackTrace();
	//			}
	//		}
	//		return jobs;
	//	}
	//	@Override
	//	public void updateApprovalDetails(String jobId, String remarks, String action) {
	//		String query="UPDATE [dbo].[JobDetails] SET Remarks = ? , ReviewerAction = ? , [ReviewercompletionTime] = GETDATE(),Status = 'Finished' Where JobId = ? ";
	//		con = SQLServerDAOFactory.createConnection();
	//		try {
	//			pstmt = con.prepareStatement(query);
	//			pstmt.setString(1, remarks);
	//			pstmt.setString(2, action);
	//			pstmt.setString(3, jobId);
	//			
	//			pstmt.executeUpdate();
	//			gAlert.ShowInfoAlert("Job updated");
	//		} catch (SQLException e) {
	//			// TODO Auto-generated catch block
	//			e.printStackTrace();
	//			try {
	//				con.rollback();
	//			} catch (SQLException e1) {
	//				// TODO Auto-generated catch block
	//				e1.printStackTrace();
	//			}
	//		}
	//		finally {
	//			try {
	//				con.close();
	//				pstmt.close();
	//			} catch (SQLException e) {
	//				e.printStackTrace();
	//			}
	//		}
	//
	//	}
	/*@Override
	public ApproverCardData getApprovedHistory(String approver) {
		System.out.println("inside getApprovedHistory");
		String sql = "  Select Approver,COALESCE([Pending],0) [Pending]  ,COALESCE([Approved],0) [Approved],COALESCE([Rejected],0) [Rejected] from " + 
				"  ( " + 
				"  Select Approver,JobId,COALESCE(ReviewerAction,'Pending') ReviewerAction from [dbo].[JobDetails] Where Approver = '"+approver+ 
				"'  ) as tab1 " + 
				"  PIVOT(COUNT(JobId) for ReviewerAction IN ([Pending],[Approved],[Rejected])) as piv";
		
		return jdbcTemplate.queryForObject(sql, new Object[]{approver}, new RowMapper<ApproverCardData>() {

			@Override
			public ApproverCardData mapRow(ResultSet rs, int rowNum) throws SQLException {
				// TODO Auto-generated method stub
				return new ApproverCardData(
						rs.getString("Approver"), 
						rs.getInt("Pending"), 
						rs.getInt("Approved"), 
						rs.getInt("Rejected")
						);
			}
		});
	}
	*/
	//	@Override
	//	public BarChartModel getNextSevenDayTasks(String approver) {
	//		BarChartModel model = new BarChartModel();
	//        ChartSeries series = new ChartSeries();
	//        try {
	//			con = SQLServerDAOFactory.createConnection();
	//			pstmt = con.prepareStatement(" Select FORMAT(CONVERT(Date,ScheduleStart),'dd') [Date],COUNT(JobId) Jobs from [dbo].[JobDetails] Where CONVERT(Date,ScheduleStart) between CONVERT(Date,GETDATE()) AND CONVERT(Date,DATEADD(DAY,6,GETDATE())) AND [Approver] = ? Group by CONVERT(Date,ScheduleStart)");
	//			pstmt.setString(1, approver);
	//			ResultSet rs = pstmt.executeQuery();
	//			while(rs.next()) {
	//				series.set(rs.getString("Date"), rs.getInt("Jobs"));
	//			}
	//			model.addSeries(series);
	//			model = formatBarChart(model);
	//			
	//		} catch (Exception e) {
	//			e.printStackTrace();
	//		} finally {
	//			try {
	//				con.close();
	//				pstmt.close();
	//			} catch (Exception e2) {
	//				e2.printStackTrace();
	//			}
	//		}
	//        
	//		return model;
	//	}
	//	@Override
	//	public BarChartModel getLastSevenDayApprovedTasks(String approver) {
	//		BarChartModel model = new BarChartModel();
	//        ChartSeries series = new ChartSeries();
	//        try {
	//			con = SQLServerDAOFactory.createConnection();
	//			pstmt = con.prepareStatement(" Select FORMAT(CONVERT(Date,ScheduleStart),'dd') [Date],COUNT(JobId) Jobs from [dbo].[JobDetails] Where ReviewerAction = 'Approved' AND [Approver] = ?  AND CONVERT(Date,ScheduleStart) between CONVERT(Date,DATEADD(DAY,-6,GETDATE())) AND CONVERT(Date,GETDATE()) Group by CONVERT(Date,ScheduleStart)");
	//			pstmt.setString(1, approver);
	//			ResultSet rs = pstmt.executeQuery();
	//			while(rs.next()) {
	//				series.set(rs.getString("Date"), rs.getInt("Jobs"));
	//			}
	//			model.addSeries(series);
	//			model = formatBarChart(model);
	//			
	//		} catch (Exception e) {
	//			e.printStackTrace();
	//		} finally {
	//			try {
	//				con.close();
	//				pstmt.close();
	//			} catch (Exception e2) {
	//				e2.printStackTrace();
	//			}
	//		}
	//        return model;
	//	}
	//	@Override
	//	public BarChartModel getNextSevenDayRejectedTasks(String approver) {
	//		BarChartModel model = new BarChartModel();
	//        ChartSeries series = new ChartSeries();
	//        try {
	//			con = SQLServerDAOFactory.createConnection();
	//			pstmt = con.prepareStatement(" Select FORMAT(CONVERT(Date,ScheduleStart),'hh') [Date],COUNT(JobId) Jobs from [dbo].[JobDetails] Where ReviewerAction = 'Rejected' AND [Approver] = ?  AND CONVERT(Date,ScheduleStart) between CONVERT(Date,DATEADD(DAY,-6,GETDATE())) AND CONVERT(Date,GETDATE()) Group by CONVERT(Date,ScheduleStart)");
	//			pstmt.setString(1, approver);
	//			ResultSet rs = pstmt.executeQuery();
	//			while(rs.next()) {
	//				series.set(rs.getString("Date"), rs.getInt("Jobs"));
	//			}
	//			model.addSeries(series);
	//			model = formatBarChart(model);
	//			
	//		} catch (Exception e) {
	//			e.printStackTrace();
	//		} finally {
	//			try {
	//				con.close();
	//				pstmt.close();
	//			} catch (Exception e2) {
	//				e2.printStackTrace();
	//			}
	//		}
	//        return model;
	//	}
	//	
	//	
	//	public BarChartModel formatBarChart(BarChartModel barModel) {
	//		//barModel = initBarModel();
	// 
	//        Axis xAxis = barModel.getAxis(AxisType.X);
	//        xAxis.setLabel("");
	// 
	//        Axis yAxis = barModel.getAxis(AxisType.Y);
	//        yAxis.setLabel("");
	//        yAxis.setMin(0);
	//        barModel.setAnimate(true);
	//        barModel.setDatatipEditor("tooltipContentEditor");
	//        barModel.setShadow(false);
	//        barModel.setBarPadding(2);
	//        barModel.setBarMargin(1);
	//        barModel.setBarWidth(10);
	//        barModel.setSeriesColors("003f5c");
	//        barModel.setExtender("chartExtender");
	//        return barModel;
	//    }
	//	@Override
	//	public List<JobDetails> approvedorRejectedJobs(String user, String revAction) {
	//		List<JobDetails> jobs = new LinkedList<JobDetails>();
	//		System.out.println("Dao.pendingForReviewJobs()"+user);
	//		String sql = "SELECT [TaskId] ,[JobId] ,[TaskName] ,[Instrument] ,[Approver] ,[ScheduleStart] ,[ScheduleStop] ,[ActualStart] ,[ActualStop] ,[ReviewerIntimationTime] ,[ReviewercompletionTime] ,[Priority] ,[Repeat] ,[RepeatTill] ,[Remarks] ,[Status] FROM [dbo].[JobDetails] Where  Approver= ? AND [Status] = 'Finished' AND [ReviewerAction] = ?";
	//		String actSql = "SELECT [JobId] ,job.[ActivityId],job.[TaskId],ActivityName,[Sequence] ,[LogbookName] ,[Performer] ,[Approver] ,[ScheduledStart] ,[ScheduleStop] ,[ActualStart] ,[ActualStop] ,[Status],Duration_min,[Asset] = (Select Asset from [dbo].[ActivityCreation] cr Where cr.TaskId = act.TaskId AND cr.ActivityId = act.ActivityId) FROM [dbo].[JobActivityDetails] job,dbo.ActivityCreation act  Where job.ActivityId = act.ActivityId AND job.TaskId = act.TaskId AND [JobId] = ? Order by [Sequence]";
	//		try {
	//			con = SQLServerDAOFactory.createConnection();
	//			pstmt = con.prepareStatement(sql);
	//			pstmt.setString(1, user);
	//			pstmt.setString(2, revAction);
	//			ResultSet rs = pstmt.executeQuery();
	//			while (rs.next()) {
	//				JobDetails jobD=new JobDetails();
	//				TaskDetail taskD=new TaskDetail();
	//				taskD.setTaskId(rs.getString("TaskId"));
	//				taskD.setTaskName(rs.getString("TaskName"));
	//				jobD.setTask(taskD);
	//				jobD.setJobID(rs.getString("JobId"));
	//				jobD.setApprover(rs.getString("Approver"));
	//				SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");   
	//				Date StrtD=format.parse(rs.getString("ScheduleStart")); 
	//				Date stpD=format.parse(rs.getString("ScheduleStop")); 
	//				jobD.setScheduledJobStartTime(StrtD);
	//				jobD.setScheduledJobEndTime(stpD);
	//				jobD.setPriority(rs.getString("Priority"));
	//				jobD.setJobStatus(rs.getString("Status"));
	//				jobD.setReviewerIntimationTime(rs.getString("ReviewerIntimationTime")!= null ? format.parse(rs.getString("ReviewerIntimationTime")) : format.parse("1900-01-01 00:00:00"));
	//				///Adding Activities
	//
	//				PreparedStatement pstmtAct = con.prepareStatement(actSql);
	//				pstmtAct.setString(1, rs.getString("JobId"));
	//				ResultSet rsAct = pstmtAct.executeQuery();
	//				List<ActivityDetails> activityList = new LinkedList<ActivityDetails>();
	//				while(rsAct.next()) {
	//					ActivityDetails act = new ActivityDetails(rsAct.getString("TaskId"), rsAct.getString("ActivityId"), rsAct.getString("ActivityName"), rsAct.getInt("Sequence"), rsAct.getString("LogbookName"), rsAct.getInt("Duration_min"), rsAct.getString("Asset"));
	//					act.setScheduledActivityStartTime(format.parse(rsAct.getString("ScheduledStart")));
	//					act.setScheduledActivityEndTime(format.parse(rsAct.getString("ScheduleStop")));
	//					act.setActualActivityStartTime(rsAct.getString("ActualStart")!= null ? format.parse(rsAct.getString("ActualStart")):  format.parse("1900-01-01 00:00:00"));
	//					act.setActualActivityEndTime(rsAct.getString("ActualStop") != null? format.parse(rsAct.getString("ActualStop")) : format.parse("1900-01-01 00:00:00"));
	//					act.setActvityStatus(rsAct.getString("Status"));
	//					activityList.add(act);
	//				}
	//				TaskDetail task = new TaskDetail(rs.getString("TaskName"), rs.getString("TaskId"), activityList);
	//				jobD.setTask(task);
	//				jobs.add(jobD);	 
	//			}
	//
	//		} catch (Exception e) {
	//			e.printStackTrace();
	//		} finally {
	//			try {
	//				con.close();
	//				pstmt.close();
	//			} catch (Exception e2) {
	//				e2.printStackTrace();
	//			}
	//		}
	//		return jobs;
	//	}
}
