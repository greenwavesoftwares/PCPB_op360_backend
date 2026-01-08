package in.co.greenwave.taskapi.dao.implementation.sqlserver;

import in.co.greenwave.taskapi.dao.ActivityReviewDAO;

public class ActivityReviewService implements ActivityReviewDAO {
//	private final G360Alert gAlert=new G360Alert();
//	DateFormat dfDt = new SimpleDateFormat("yyyy-MM-dd");
//	DateFormat dfDtTm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//	Connection con = null;
//	PreparedStatement pstmt = null;
//	
//	
//	@Override
//	public List<JobDetails> fetchJobDetailsForApprover(String currentApprover) {
//        List<JobDetails> jobDetailsList=new ArrayList<JobDetails>();
//		String query="SELECT [TaskId] ,[JobId] ,[TaskName] ,[Approver] ,[ScheduleStart] ,[ScheduleStop],ActualStop,[Priority] ,[Status] FROM [dbo].[JobDetails] where JobId in (select JobId FROM [dbo].[JobActivityDetails] where approver=?) and Status not in ('Pending for review','Finished')";
//		try {
//			 con = SQLServerDAOFactory.createConnection();
//			pstmt = con.prepareStatement(query);
//			pstmt.setString(1, currentApprover);
//			ResultSet rs = pstmt.executeQuery();
//			while (rs.next()) {
//				JobDetails jobD=new JobDetails();
//				  TaskDetail taskD=new TaskDetail();
//				  taskD.setTaskId(rs.getString("TaskId"));
//				  taskD.setTaskName(rs.getString("TaskName"));
//			    jobD.setTask(taskD);
//			    jobD.setJobID(rs.getString("JobId"));
//			      SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");   
//			      Date StrtD=format.parse(rs.getString("ScheduleStart")); 
//			      Date stpD=format.parse(rs.getString("ScheduleStop")); 
//			    jobD.setScheduledJobStartTime(StrtD);
//			    jobD.setScheduledJobEndTime(stpD);
//			    jobD.setPriority(rs.getString("Priority"));
//			    jobD.setJobStatus(rs.getString("Status"));
//			
//			 jobDetailsList.add(jobD);	 
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
//		return jobDetailsList;
//	
//	}
//	@Override
//	public List<ActivityDetails> fetchJobActivityDetails(String jobId) {
//		//System.out.println("Dao.fetchJobActivityDetails()"+jobId);
//
//		List<ActivityDetails> activityDetailsList=new ArrayList<>();
//		String query="select [JobId] , AId ,ActivityName=(select [ActivityName] from [dbo].[ActivityCreation] where ActivityId=AId) ,sequence=(select Sequence from [dbo].[ActivityCreation] where ActivityId=AId) ,[TaskId] ,[LogbookName] ,[Performer] ,[Approver] ,[ScheduledStart] ,[ScheduleStop],[ActualStart],[ActualStop] ,[Status] from ( SELECT [JobId] ,[ActivityId] AId ,[TaskId] ,[LogbookName] ,[Performer] ,[Approver] ,[ScheduledStart] ,[ScheduleStop],[ActualStart],[ActualStop],[Status] FROM [dbo].[JobActivityDetails] where JobId=?) tb order by sequence";
//		try {
//			 con = SQLServerDAOFactory.createConnection();
//			pstmt = con.prepareStatement(query);
//			pstmt.setString(1, jobId);
//			ResultSet rs = pstmt.executeQuery();
//			while (rs.next()) {
//				ActivityDetails ad=new ActivityDetails();
//				ad.setJobId(rs.getString("JobId"));
//				ad.setActivityId(rs.getString("AId"));
//				ad.setActivityName(rs.getString("ActivityName"));
//				ad.setSequence(rs.getInt("sequence"));
//				ad.setTaskId(rs.getString("TaskId"));
//				ad.setLogbook(rs.getString("LogbookName"));
//				ad.setPerformer(rs.getString("Performer"));
//				ad.setApprover(rs.getString("Approver"));
//				SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");   
//				Date StrtD=format.parse(rs.getString("ScheduledStart")); 
//				Date stpD=format.parse(rs.getString("ScheduleStop")); 
//				ad.setScheduledActivityStartTime(StrtD);
//				ad.setScheduledActivityEndTime(stpD);
//				ad.setActualActvtyStrt(rs.getString("ActualStart")==null?"-":rs.getString("ActualStart"));
//				ad.setActualActvtEnd(rs.getString("ActualStop")==null?"-":rs.getString("ActualStop"));
//
//				ad.setActvityStatus(rs.getString("Status"));
//
//				activityDetailsList.add(ad);    
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
//
//
//
//		return activityDetailsList;
//	}
//
//	@Override
//	public void updateJobActvityByApprover(ActivityDetails activityDetails) {
//		String query=null;
//		  if(activityDetails.getActvityStatus().equalsIgnoreCase("Review Started"))
//		    query="update [dbo].[JobActivityDetails] set [ActivityReviewerIntimationTime]=?,[Remarks]=?,[Status]=? where [JobId]=? and [ActivityId]=?";
//		  else
//		    query="update [dbo].[JobActivityDetails] set [ActivityReviewercompletionTime]=? ,[Remarks]=?,[Status]=? where [JobId]=? and [ActivityId]=?";
//		 
//		try {
//			 con = SQLServerDAOFactory.createConnection();
//			pstmt = con.prepareStatement(query);
//			if(activityDetails.getActvityStatus().equalsIgnoreCase("Review Started")) {
//			     pstmt.setTimestamp(1,new java.sql.Timestamp(activityDetails.getReviewerActivityStartTime().getTime()));
//			     pstmt.setString(2,null);
//			}
//			else {
//				pstmt.setTimestamp(1,new java.sql.Timestamp(activityDetails.getReviewerActivityStopTime().getTime()));
//				pstmt.setString(2,activityDetails.getRemarks());
//			}
//				
//			pstmt.setString(3,activityDetails.getActvityStatus());
//			
//			pstmt.setString(4, activityDetails.getJobId());
//			pstmt.setString(5, activityDetails.getActivityId());
//			pstmt.executeUpdate();
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
//	}
//	
//	@Override
//	public int fetchLastSequence(String jobId) {
//	   int seq=0;
//	   
//	   String query="select top 1 Sequence from [dbo].[JobActivityDetails] jd inner join dbo.ActivityCreation jad on jd.TaskId=jad.TaskId and jd.ActivityId=jad.ActivityId where JobId=? order by Sequence desc";
//	   
//	   try {
//			con = SQLServerDAOFactory.createConnection();
//			pstmt = con.prepareStatement(query);
//			pstmt.setString(1, jobId);
//			ResultSet rs = pstmt.executeQuery();
//			while (rs.next()) {
//				seq= rs.getInt("Sequence");
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
//	   
//	   return seq;
//	}
//	
//	@Override
//	public void updateJobStatus(String status,Date actualTime,String jId) {
//		String query="update dbo.[JobDetails] set Status=?,ActualStop=?,[ReviewerIntimationTime]=? where JobId=? ";
//        try {
//        	con = SQLServerDAOFactory.createConnection();
//        	pstmt = con.prepareStatement(query);
//        	pstmt.setString(1,"Pending for review");
//        	pstmt.setTimestamp(2,new java.sql.Timestamp(actualTime.getTime()));
//        	pstmt.setTimestamp(3,new java.sql.Timestamp(actualTime.getTime()));
//        	pstmt.setString(4,jId);
//        	pstmt.executeUpdate();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}  
//       finally {
//    	   try {
//				con.close();
//				pstmt.close();
//			} catch (Exception e2) {
//				e2.printStackTrace();
//			}
//	   }
//		
//	}
//	@Override
//	public List<JobDetails> fetchApproverJobs(String currentApprover) {
//		List<JobDetails> jobs = new LinkedList<JobDetails>();
//		//System.out.println("ActivityReviewService.fetchApproverJobs()"+currentApprover);
//		
//		String sql = "SELECT [TaskId] ,[JobId] ,[TaskName] ,[Approver] ,[ScheduleStart] ,[ScheduleStop],ActualStop,[Priority] ,[Status] FROM [dbo].[JobDetails] where JobId in (select JobId FROM [dbo].[JobActivityDetails] where approver=?) and Status not in ('Pending for review','Finished')";
//		String actSql = "select [JobId] , AId ,ActivityName=(select [ActivityName] from [dbo].[ActivityCreation] where ActivityId=AId) ,sequence=(select Sequence from [dbo].[ActivityCreation] where ActivityId=AId) ,[TaskId] ,[LogbookName] ,[Performer] ,[Approver] ,[ScheduledStart] ,[ScheduleStop],[ActualStart],[ActualStop] ,[Status],[FileName] from ( SELECT [JobId] ,[ActivityId] AId ,[TaskId] ,[LogbookName] ,[Performer] ,[Approver] ,[ScheduledStart] ,[ScheduleStop],[ActualStart],[ActualStop],[Status],[FileName] FROM [dbo].[JobActivityDetails] where JobId=?) tb order by sequence";
//		try {
//			con = SQLServerDAOFactory.createConnection();
//			pstmt = con.prepareStatement(sql);
//			pstmt.setString(1, currentApprover);
//			ResultSet rs = pstmt.executeQuery();
//			while (rs.next()) {
//				JobDetails jobD=new JobDetails();
//				jobD.setJobID(rs.getString("JobId")); 
//				jobD.setPriority(rs.getString("Priority"));
//				jobD.setPriorityColor(jobD.getPriority().equalsIgnoreCase("Critical")?"danger":jobD.getPriority().equalsIgnoreCase("Normal")?"warning":"primary");
//					SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");   
//					Date StrtD=format.parse(rs.getString("ScheduleStart")); 
//					Date stpD=format.parse(rs.getString("ScheduleStop"));
//				jobD.setScheduledJobStartTime(StrtD);
//				jobD.setScheduledJobEndTime(stpD);
//				jobD.setJobStatus(rs.getString("Status")); //M
//				
//				// fetch actvities
//				PreparedStatement pstmtAct = con.prepareStatement(actSql);
//				pstmtAct.setString(1, rs.getString("JobId"));
//				ResultSet rsAct = pstmtAct.executeQuery();
//				List<ActivityDetails> activityList = new LinkedList<ActivityDetails>();
//				
//				 while (rsAct.next()) {
//						ActivityDetails acvtvD=new ActivityDetails();
//						acvtvD.setActivityId(rsAct.getString("AId"));
//						acvtvD.setActivityName(rsAct.getString("ActivityName"));
//							Date StrtDA=format.parse(rsAct.getString("ScheduledStart")); 
//							Date stpDA=format.parse(rsAct.getString("ScheduleStop")); 
//						acvtvD.setScheduledActivityStartTime(StrtDA);
//						acvtvD.setScheduledActivityEndTime(stpDA);
//						acvtvD.setActualActvtyStrt(rsAct.getString("ActualStart")==null?"-":rsAct.getString("ActualStart"));
//						acvtvD.setActualActvtEnd(rsAct.getString("ActualStop")==null?"-":rsAct.getString("ActualStop"));
//						
//						acvtvD.setActvityStatus(rsAct.getString("Status"));
//						  
//						acvtvD.setJobId(rsAct.getString("JobId"));
//						acvtvD.setSequence(rsAct.getInt("sequence"));
//						acvtvD.setLogbook(rsAct.getString("LogbookName"));
//						acvtvD.setPerformer(rsAct.getString("Performer"));
//						acvtvD.setApprover(rsAct.getString("Approver"));  
//						boolean b=currentApprover.equalsIgnoreCase(acvtvD.getApprover())?false:true;	 	 
//						acvtvD.setNotBelongToApprover(b);
//						acvtvD.setActFile(rsAct.getString("FileName"));
//					activityList.add(acvtvD);
//						
//					}
//				   TaskDetail task=new TaskDetail();
//					task.setTaskName(rs.getString("TaskName"));
//					task.setTaskId(rs.getString("TaskId"));
//					task.setActivityList(activityList);
//					jobD.setTask(task);
//					
//				 jobs.add(jobD);
//				
//			}
//		
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		finally {
//			try {
//				con.close();
//				pstmt.close();
//			} catch (Exception e2) {
//				e2.printStackTrace();
//			}
//		}
//		
//		return jobs;
//	}
}
