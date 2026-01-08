package in.co.greenwave.taskapi.dao.implementation.sqlserver;

import in.co.greenwave.taskapi.dao.JobPerformDAO;


public class JobPerformService implements JobPerformDAO {
	
//	private final G360Alert gAlert=new G360Alert();
//	DateFormat dfDt = new SimpleDateFormat("yyyy-MM-dd");
//	DateFormat dfDtTm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//	Connection con = null;
//	PreparedStatement pstmt = null;
//
//	@Override
//	public List<JobDetails> fetchJobDetails(String performer) {
//
//		List<JobDetails> jobDetailsList=new ArrayList<JobDetails>();
//
//		String query="SELECT [TaskId] ,[JobId] ,[TaskName] ,[Approver] ,[ScheduleStart] ,[ScheduleStop],ActualStop,[Priority] ,[Status] FROM [dbo].[JobDetails] where JobId in (select JobId FROM [dbo].[JobActivityDetails] where Performer=?) and Status not in ('Pending for review','Finished')";
//
//		try {
//			 con = SQLServerDAOFactory.createConnection();
//			pstmt = con.prepareStatement(query);
//			pstmt.setString(1, performer);
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
//
//				jobDetailsList.add(jobD);	 
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
//	@Override
//	public void updateJobActivity(ActivityDetails activityDetails) {
//		//System.out.println("Dao.updateActivityStatus() "+activityDetails.getActvityStatus()+" "+activityDetails.getActualActivityStartTime()+activityDetails.getJobId()+" "+activityDetails.getActivityId());
//		String query=null;
//		if(activityDetails.getActvityStatus().equalsIgnoreCase("Started"))
//			query="update [dbo].[JobActivityDetails] set [ActualStart]=? , [Status]=? where [JobId]=? and [ActivityId]=?";
//		else
//			query="update [dbo].[JobActivityDetails] set [ActualStop]=? , [Status]=? where [JobId]=? and [ActivityId]=?";
//
//		try {
//			 con = SQLServerDAOFactory.createConnection();
//			pstmt = con.prepareStatement(query);
//			if(activityDetails.getActvityStatus().equalsIgnoreCase("Started"))
//				pstmt.setTimestamp(1,new java.sql.Timestamp(activityDetails.getActualActivityStartTime().getTime()));
//			else
//				pstmt.setTimestamp(1,new java.sql.Timestamp(activityDetails.getActualActivityEndTime().getTime()));	
//			pstmt.setString(2,activityDetails.getActvityStatus());
//			pstmt.setString(3, activityDetails.getJobId());
//			pstmt.setString(4, activityDetails.getActivityId());
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
//	@Override
//	public int fetchLastCompletedOrder(String jobId) {
//
//
//		String query="select top 1 sequence=(select Sequence from [dbo].[ActivityCreation] where ActivityId=AId) from ( SELECT [JobId] ,[ActivityId] AId ,[TaskId] ,[LogbookName] ,[Performer] ,[Approver] ,[ScheduledStart] ,[ScheduleStop] ,[Status] FROM [dbo].[JobActivityDetails] where   JobId=? and Status=? ) tb order by sequence desc";
//
//		int order=0;
//		try {
//			 con = SQLServerDAOFactory.createConnection();
//			pstmt = con.prepareStatement(query);
//			pstmt.setString(1, jobId);
//			pstmt.setString(2,"Completed");
//			ResultSet rs = pstmt.executeQuery();
//			while (rs.next()) {
//				order=rs.getInt("sequence");
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
//		return order;
//	}
//	@Override
//	public int fetchRemainingActvties(String jobId, int lastCompletedOrder) {
//
//		String query="select count(*) size from ( select JobId,aid,seq=(select Sequence from dbo.ActivityCreation where ActivityId=aid),Status from ( select JobId,ActivityId aid ,Status from [dbo].[JobActivityDetails] where  JobId=? and Status<>'Completed' ) tb ) tb2 where seq=?";
//
//		int count=0;
//		try {
//			con = SQLServerDAOFactory.createConnection();
//			pstmt = con.prepareStatement(query);
//			pstmt.setString(1, jobId);
//			pstmt.setInt(2,lastCompletedOrder);
//			ResultSet rs = pstmt.executeQuery();
//			while (rs.next()) {
//				count= rs.getInt("size");
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
//		return count;
//	}
//	@Override
//	public void updateJobActualStarttime(ActivityDetails activityDetails) {
//		//System.out.println(activityDetails);
//		String query="update dbo.[JobDetails] set ActualStart=? where JobId=?";
//        try {
//        	con = SQLServerDAOFactory.createConnection();
//        	pstmt = con.prepareStatement(query);
//        	pstmt.setTimestamp(1,new java.sql.Timestamp(activityDetails.getActualActivityStartTime().getTime()));
//        	pstmt.setString(2,activityDetails.getJobId());
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
//        
//	}
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
//	@Override
//	public void updateJobActualStoptime(ActivityDetails activityDetails) {
//		String query="update dbo.[JobDetails] set ActualStop=?,[ReviewerIntimationTime]=?,[Status]=? where JobId=? ";
//        try {
//        	con = SQLServerDAOFactory.createConnection();
//        	pstmt = con.prepareStatement(query);
//        	pstmt.setTimestamp(1,new java.sql.Timestamp(activityDetails.getActualActivityEndTime().getTime()));
//        	pstmt.setTimestamp(2,new java.sql.Timestamp(activityDetails.getActualActivityEndTime().getTime()));
//        	pstmt.setString(3,"Pending for review");
//        	pstmt.setString(4,activityDetails.getJobId());
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
//		
//	}
//	@Override
//	public int fetchdActivity(String performer, String status) {
//		 int count=0;
//		   
//		   String query="select count(*) conutactivity FROM [dbo].[JobActivityDetails]  where Performer=?  and status=?";
//		   
//		   try {
//				con = SQLServerDAOFactory.createConnection();
//				pstmt = con.prepareStatement(query);
//				pstmt.setString(1, performer);
//				pstmt.setString(2, status);
//				ResultSet rs = pstmt.executeQuery();
//				while (rs.next()) {
//					count= rs.getInt("conutactivity");
//				}
//
//			} catch (Exception e) {
//				e.printStackTrace();
//			} finally {
//				try {
//					con.close();
//					pstmt.close();
//				} catch (Exception e2) {
//					e2.printStackTrace();
//				}
//			}
//		   
//		   return count;
//	}
//	@Override
//	public int fetchJobCount(String performer, String status) {
//		 int count=0;
//		 String query="select  count(*) jobcount from dbo.JobDetails where JobId in (select JobId from  [dbo].[JobActivityDetails]  where Performer=?) and status=?";
//		   
//		   try {
//				con = SQLServerDAOFactory.createConnection();
//				pstmt = con.prepareStatement(query);
//				pstmt.setString(1, performer);
//				pstmt.setString(2, status);
//				ResultSet rs = pstmt.executeQuery();
//				while (rs.next()) {
//					count= rs.getInt("jobcount");
//				}
//
//			} catch (Exception e) {
//				e.printStackTrace();
//			} finally {
//				try {
//					con.close();
//					pstmt.close();
//				} catch (Exception e2) {
//					e2.printStackTrace();
//				}
//			}
//		   
//		   return count;  
//	}
//	@Override
//	public List<ActivityDetails> fetchPendingActivities(String currentPerformer) {
//		System.out.println("JobPerformService.fetchPendingActivities()");
//		
//		List<ActivityDetails> listofPendingActvt=new ArrayList<ActivityDetails>();
//		
//		String query="set NOcount on;DECLARE @dateVal TABLE(dateAcvt date) INSERT INTO @dateVal VALUES (convert(date,GETDATE())) , (Dateadd(day,1,convert(date,GETDATE()))) , (Dateadd(day,2,convert(date,GETDATE()))) , (Dateadd(day,3,convert(date,GETDATE()))), (Dateadd(day,4,convert(date,GETDATE()))), (Dateadd(day,5,convert(date,GETDATE()))), (Dateadd(day,6,convert(date,GETDATE()))) SELECT dateAcvt date,coalesce(actvtCount,0) actvtCount FROM @dateVal tbl1 left join ( select date,count(ActivityId) actvtCount from ( select ActivityId,convert(date,ScheduledStart) date FROM [dbo].[JobActivityDetails] where Performer=? and status=?) tab where date between convert(date,GETDATE()) and DateAdd(day,6,convert(date,GETDATE())) group by date ) tbl2 on tbl1.dateAcvt=tbl2.date order by date";
//		try {
//			 con = SQLServerDAOFactory.createConnection();
//			pstmt = con.prepareStatement(query);
//			pstmt.setString(1, currentPerformer);
//			pstmt.setString(2,"Not Started");
//			ResultSet rs = pstmt.executeQuery();
//			while (rs.next()) {
//				ActivityDetails ad=new ActivityDetails();
//				ad.setDate(rs.getString("date"));
//				ad.setActvtCount(rs.getInt("actvtCount"));
//				listofPendingActvt.add(ad);    
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
//		return listofPendingActvt;
//	}
//	
//	@Override
//	public List<JobDetails> fetchPendingJobTime(String currentPerformer) {  
//     List<JobDetails> listofPendingJobTime=new ArrayList<JobDetails>();
//		
//		String query="select JobName,ActivityName+' ('+JobName+')' JobActvt,Duration_min from ( select JobName=(select TaskName from [dbo].[JobDetails] where JobId=JId),ActivityName,Duration_min from ( select TaskId,JobId JId,ActivityId,ScheduledStart,ScheduleStop from [dbo].[JobActivityDetails] where Status=? and Performer=? and convert(date,ScheduledStart)=convert(date,GETDATE()) ) tb inner join dbo.ActivityCreation ac on tb.TaskId=ac.TaskId and tb.ActivityId=ac.ActivityId ) ft order by JobName";
//		try {
//			 con = SQLServerDAOFactory.createConnection();
//			pstmt = con.prepareStatement(query);
//			pstmt.setString(1, "Not Started");
//			pstmt.setString(2,currentPerformer);
//			ResultSet rs = pstmt.executeQuery();
//			while (rs.next()) {
//				JobDetails ad=new JobDetails();
//				  TaskDetail tsk=new TaskDetail();
//				  tsk.setTaskName(rs.getString("JobActvt"));
//				ad.setTask(tsk);
//				ad.setJobRemainingTime(rs.getInt("Duration_min"));
//				listofPendingJobTime.add(ad);    
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
//		return listofPendingJobTime;	  
//	}
//	@Override
//	public List<ActivityDetails> fetchApprovedActivities(String currentPerformer) {
//		
//        List<ActivityDetails> listofApprovedActvt=new ArrayList<ActivityDetails>();
//		String query="set NOcount on; DECLARE @dateVal TABLE(dateAcvt date) INSERT INTO @dateVal VALUES (convert(date,GETDATE())) , (Dateadd(day,-1,convert(date,GETDATE()))) , (Dateadd(day,-2,convert(date,GETDATE()))) , (Dateadd(day,-3,convert(date,GETDATE()))), (Dateadd(day,-4,convert(date,GETDATE()))), (Dateadd(day,-5,convert(date,GETDATE()))), (Dateadd(day,-6,convert(date,GETDATE()))) SELECT dateAcvt date,coalesce(actvtCount,0) actvtCount FROM @dateVal tbl1 left join ( select date,count(ActivityId) actvtCount from ( select ActivityId,convert(date,ActualStop) date FROM [dbo].[JobActivityDetails] where Performer=? and status=? ) tab where date between DateAdd(day,-6,convert(date,GETDATE())) and convert(date,GETDATE()) group by date ) tbl2 on tbl1.dateAcvt=tbl2.date order by date desc";
//		try {
//			 con = SQLServerDAOFactory.createConnection();
//			pstmt = con.prepareStatement(query);
//			pstmt.setString(1, currentPerformer);
//			pstmt.setString(2,"Completed");
//			ResultSet rs = pstmt.executeQuery();
//			while (rs.next()) {
//				ActivityDetails ad=new ActivityDetails();
//				ad.setDate(rs.getString("date"));
//				ad.setActvtCount(rs.getInt("actvtCount"));
//				listofApprovedActvt.add(ad);    
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
//		return listofApprovedActvt;
//		
//	}
//	@Override
//	public List<ActivityDetails> fetchActivityDelayCount(String currentPerformer,String status) {
//		  List<ActivityDetails> listofApprovedActvtDelay=new ArrayList<ActivityDetails>();
//		 String query="select actvtStatus,count(actvtStatus) cnt from ( select actvtStatus= case when abs(DATEDIFF(MINUTE,ScheduleStop,ActualStop))<=3 then 'OnTime' when ActualStop>ScheduleStop then 'Late' when ActualStop<ScheduleStop then 'Early' end from [dbo].[JobActivityDetails] where Performer=? and Status=? ) ft group by actvtStatus";
//		 try {
//			 con = SQLServerDAOFactory.createConnection();
//			pstmt = con.prepareStatement(query);
//			pstmt.setString(1, currentPerformer);
//			pstmt.setString(2,status);
//			ResultSet rs = pstmt.executeQuery();
//			while (rs.next()) {
//				ActivityDetails ad=new ActivityDetails();
//				ad.setActvityStatus(rs.getString("actvtStatus"));
//				ad.setActvtCount(rs.getInt("cnt"));
//				listofApprovedActvtDelay.add(ad);    
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
//		return listofApprovedActvtDelay;
//		 
//	}
//	@Override
//	public List<ActivityDetails> fetchRejectedActivities(String currentPerformer) {
//		  List<ActivityDetails> listofRejectedActvt=new ArrayList<ActivityDetails>();
//			String query="Set NOcount on; DECLARE @dateVal TABLE(dateAcvt date) INSERT INTO @dateVal VALUES (convert(date,GETDATE())) , (Dateadd(day,-1,convert(date,GETDATE()))) , (Dateadd(day,-2,convert(date,GETDATE()))) , (Dateadd(day,-3,convert(date,GETDATE()))), (Dateadd(day,-4,convert(date,GETDATE()))), (Dateadd(day,-5,convert(date,GETDATE()))), (Dateadd(day,-6,convert(date,GETDATE()))) SELECT dateAcvt date,coalesce(actvtCount,0) actvtCount FROM @dateVal tbl1 left join ( select date,count(ActivityId) actvtCount from (select ActivityId,convert(date,ActualStop) date FROM [dbo].[JobActivityDetails] where Performer=? and status=?) tab where date between DateAdd(day,-6,convert(date,GETDATE())) and convert(date,GETDATE()) group by date ) tbl2 on tbl1.dateAcvt=tbl2.date order by date desc";
//			try {
//				con = SQLServerDAOFactory.createConnection();
//				pstmt = con.prepareStatement(query);
//				pstmt.setString(1, currentPerformer);
//				pstmt.setString(2,"Rejected");
//				ResultSet rs = pstmt.executeQuery();
//				while (rs.next()) {
//					ActivityDetails ad=new ActivityDetails();
//					ad.setDate(rs.getString("date"));
//					ad.setActvtCount(rs.getInt("actvtCount"));
//					listofRejectedActvt.add(ad);    
//				}
//
//			} catch (Exception e) {
//				e.printStackTrace();
//			} finally {
//				try {
//					con.close();
//					pstmt.close();
//				} catch (Exception e2) {
//					e2.printStackTrace();
//				}
//			}
//
//			return listofRejectedActvt;
//	}
//	
//	@Override
//	public int fetchCompletedJobCount(String currentPerformer, String status, String reviewerAction) {
//		 int count=0;
//		 String query="select count(*) jobcount from dbo.JobDetails where JobId in (select JobId from [dbo].[JobActivityDetails] where Performer=?) and status=? and ReviewerAction=?";
//		   
//		   try {
//				con = SQLServerDAOFactory.createConnection();
//				pstmt = con.prepareStatement(query);
//				pstmt.setString(1, currentPerformer);
//				pstmt.setString(2, status);
//				pstmt.setString(3, reviewerAction);
//				ResultSet rs = pstmt.executeQuery();
//				while (rs.next()) {
//					count= rs.getInt("jobcount");
//				}
//
//			} catch (Exception e) {
//				e.printStackTrace();
//			} finally {
//				try {
//					con.close();
//					pstmt.close();
//				} catch (Exception e2) {
//					e2.printStackTrace();
//				}
//			}
//		   
//		   return count;	
//	}
//	@Override
//	public List<JobDetails> fetchJobCountProrityWise(String currentPerformer) {
//		 List<JobDetails> listCount=new ArrayList<JobDetails>();
//		 String query="select Priority,count(Priority) countP from ( select JobId,Priority from dbo.JobDetails where JobId in (select JobId from dbo.JobActivityDetails where Performer=?) and Status=? and convert(date,ScheduleStart)<=convert(date,GETDATE()) ) prt group by Priority";
//		 try {
//			 con = SQLServerDAOFactory.createConnection();
//			pstmt = con.prepareStatement(query);
//			pstmt.setString(1, currentPerformer);
//			pstmt.setString(2,"Not Started");
//			ResultSet rs = pstmt.executeQuery();
//			while (rs.next()) {
//				JobDetails ad=new JobDetails();
//				ad.setPriority(rs.getString("Priority"));
//				ad.setJobCount(rs.getInt("countP"));
//				listCount.add(ad);    
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
//		return listCount;
//
//	}
//	@Override
//	public List<JobDetails> fetchPerformerJobDetails(String currentPerfromer) {
//		List<JobDetails> jobs = new LinkedList<JobDetails>();
//		System.out.println("JobPerformService.fetchPerformerJobDetails()"+currentPerfromer);
//		
//		String sql = "SELECT [TaskId] ,[JobId] ,[TaskName] ,[Approver] ,[ScheduleStart] ,[ScheduleStop],ActualStop,[Priority] ,[Status] FROM [dbo].[JobDetails] where JobId in (select JobId FROM [dbo].[JobActivityDetails] where ? IN (Select * from String_Split(Performer,',') )) and Status not in ('Pending for review','Finished')";
//		String actSql = "select [JobId] , AId ,ActivityName=(select [ActivityName] from [dbo].[ActivityCreation] where ActivityId=AId) ," + 
//				"sequence=(select Sequence from [dbo].[ActivityCreation] where ActivityId=AId) ,tb.[TaskId] ,[LogbookName] ,[Performer] " + 
//				",[Approver] ,[ScheduledStart] ,[ScheduleStop],[ActualStart],[ActualStop] ,[Status],[FileName],ac.[buffer] as myBuffer, ac.[CreationTime] from " + 
//				"( SELECT [JobId],[ActivityId] AId ,[TaskId] ,[LogbookName] ,[Performer] ,[Approver] ,[ScheduledStart] ,[ScheduleStop],[ActualStart]," + 
//				"[ActualStop],[Status],[FileName] FROM [dbo].[JobActivityDetails] where JobId= ?)" + 
//				"tb left join dbo.ActivityCreation ac on tb.TaskId = ac.TaskId and tb.AId = ac.ActivityId order by sequence";
//		
//		try {
//			con = SQLServerDAOFactory.createConnection();
//			pstmt = con.prepareStatement(sql);
//			pstmt.setString(1, currentPerfromer);
//			ResultSet rs = pstmt.executeQuery();
//			while (rs.next()) {
//				JobDetails jobD=new JobDetails();
//				jobD.setJobID(rs.getString("JobId"));
//				jobD.setApprover(rs.getString("Approver"));
//				jobD.setPriority(rs.getString("Priority"));
//				jobD.setPriorityColor(jobD.getPriority().equalsIgnoreCase("Critical")?"danger":jobD.getPriority().equalsIgnoreCase("Normal")?"warning":"primary");
//					SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");   
//					Date StrtD=format.parse(rs.getString("ScheduleStart")); 
//					Date stpD=format.parse(rs.getString("ScheduleStop"));
//				jobD.setScheduledJobStartTime(StrtD);
//				jobD.setScheduledJobEndTime(stpD);
//				jobD.setJobStatus(rs.getString("Status"));   
//			    
//				//fetch actvities
//				  
//				PreparedStatement pstmtAct = con.prepareStatement(actSql);
//				pstmtAct.setString(1, rs.getString("JobId"));
//				ResultSet rsAct = pstmtAct.executeQuery();
//				List<ActivityDetails> activityList = new LinkedList<ActivityDetails>();
//				 int i=0;
////				 System.out.println("Job");
////				System.out.println(rs.getString("JobId"));
//				while (rsAct.next()) {
//					ActivityDetails acvtvD=new ActivityDetails();
//					acvtvD.setActivityId(rsAct.getString("AId"));
//					acvtvD.setActivityName(rsAct.getString("ActivityName"));
//						Date StrtDA=format.parse(rsAct.getString("ScheduledStart")); 
//						Date stpDA=format.parse(rsAct.getString("ScheduleStop")); 
//					acvtvD.setScheduledActivityStartTime(StrtDA);
//					acvtvD.setScheduledActivityEndTime(stpDA);
//					acvtvD.setActualActvtyStrt(rsAct.getString("ActualStart")==null?"-":rsAct.getString("ActualStart"));
//					acvtvD.setActualActvtEnd(rsAct.getString("ActualStop")==null?"-":rsAct.getString("ActualStop"));					
//					acvtvD.setActvityStatus(rsAct.getString("Status"));
//					if (acvtvD.getActvityStatus().equalsIgnoreCase("Sent for approval")
//						|| acvtvD.getActvityStatus().equalsIgnoreCase("Completed") 
//						|| acvtvD.getActvityStatus().equalsIgnoreCase("Review Started")) {
//						acvtvD.setBooleanForActivityStatus(true);
//					}
//					acvtvD.setJobId(rsAct.getString("JobId"));					
//					acvtvD.setSequence(rsAct.getInt("sequence"));
//					acvtvD.setLogbook(rsAct.getString("LogbookName"));
//					List<String> performers = Arrays.asList(rsAct.getString("Performer").split(","));
//					for(String p : performers) {
//						if(p.equalsIgnoreCase(currentPerfromer)) {
//							acvtvD.setPerformer(p);
//							break;
//						}else {
//							acvtvD.setPerformer(p);
//						}
//					}
//					acvtvD.setApprover(rsAct.getString("Approver"));  
////					boolean b=currentPerfromer.equalsIgnoreCase(acvtvD.getPerformer())?false:true;	 
//					boolean b= Arrays.asList(acvtvD.getPerformer().split(",")).contains(currentPerfromer)?false:true;
//					acvtvD.setNotBelongToPerformer(b);
//					boolean b2=true;
//					acvtvD.setActvityBtnDisbleForActvtOrder(b2); 
//					acvtvD.setActFile(rsAct.getString("FileName"));
//					acvtvD.setBuffer(rsAct.getInt("myBuffer"));
//					acvtvD.setDelayDueToBuffer("-");
//	  
//				activityList.add(acvtvD);
//					
//				}
//				TaskDetail task=new TaskDetail();
//				task.setTaskName(rs.getString("TaskName"));
//				task.setTaskId(rs.getString("TaskId"));
//				task.setActivityList(activityList);
//				jobD.setTask(task);
//					
//			 jobs.add(jobD);	
//			}
//			
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}finally {
//			try {
//				con.close();
//				pstmt.close();
//			} catch (Exception e2) {
//				e2.printStackTrace();
//			}
//		}	
//	return jobs;	
//	}
//	
//	@Override
//	public List<JobDetails> approvedorRejectedJobsForPerformer(String currentPerformer, String status) {
//	
//		List<JobDetails> jobs = new LinkedList<JobDetails>();
////		System.out.println("JobPerformService.fetchPerformerJobDetails()"+currentPerformer+" "+status);
//		
//		String sql = "SELECT [TaskId] ,[JobId] ,[TaskName] ,[Approver] ,[ScheduleStart] ,[ScheduleStop],ActualStop,[Priority] ,[Status] FROM [dbo].[JobDetails] where JobId in (select JobId FROM [dbo].[JobActivityDetails] where Performer=?) and Status='Finished' and ReviewerAction=?";
//		String actSql = "select [JobId] , AId ,ActivityName=(select [ActivityName] from [dbo].[ActivityCreation] where ActivityId=AId) ,sequence=(select Sequence from [dbo].[ActivityCreation] where ActivityId=AId) ,[TaskId] ,[LogbookName] ,[Performer] ,[Approver] ,[ScheduledStart] ,[ScheduleStop],[ActualStart],[ActualStop] ,[Status] from ( SELECT [JobId] ,[ActivityId] AId ,[TaskId] ,[LogbookName] ,[Performer] ,[Approver] ,[ScheduledStart] ,[ScheduleStop],[ActualStart],[ActualStop],[Status] FROM [dbo].[JobActivityDetails] where JobId=?) tb order by sequence";
//		
//		try {
//			con = SQLServerDAOFactory.createConnection();
//			pstmt = con.prepareStatement(sql);
//			pstmt.setString(1, currentPerformer);
//			pstmt.setString(2, status);
//			ResultSet rs = pstmt.executeQuery();
//			while (rs.next()) {
//				JobDetails jobD=new JobDetails();
//				jobD.setJobID(rs.getString("JobId"));
//				jobD.setApprover(rs.getString("Approver"));
//				jobD.setPriority(rs.getString("Priority"));
//				jobD.setPriorityColor(jobD.getPriority().equalsIgnoreCase("Critical")?"danger":jobD.getPriority().equalsIgnoreCase("Normal")?"warning":"primary");
//					SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");   
//					Date StrtD=format.parse(rs.getString("ScheduleStart")); 
//					Date stpD=format.parse(rs.getString("ScheduleStop"));
//				jobD.setScheduledJobStartTime(StrtD);
//				jobD.setScheduledJobEndTime(stpD);
//				jobD.setJobStatus(rs.getString("Status"));   
//			    
//				//fetch actvities
//				  
//				PreparedStatement pstmtAct = con.prepareStatement(actSql);
//				pstmtAct.setString(1, rs.getString("JobId"));
//				ResultSet rsAct = pstmtAct.executeQuery();
//				List<ActivityDetails> activityList = new LinkedList<ActivityDetails>();
//				 
//				while (rsAct.next()) {
//					ActivityDetails acvtvD=new ActivityDetails();
//					acvtvD.setActivityId(rsAct.getString("AId"));
//					acvtvD.setActivityName(rsAct.getString("ActivityName"));
//						Date StrtDA=format.parse(rsAct.getString("ScheduledStart")); 
//						Date stpDA=format.parse(rsAct.getString("ScheduleStop")); 
//					acvtvD.setScheduledActivityStartTime(StrtDA);
//					acvtvD.setScheduledActivityEndTime(stpDA);
//					acvtvD.setActualActvtyStrt(rsAct.getString("ActualStart")==null?"-":rsAct.getString("ActualStart"));
//					acvtvD.setActualActvtEnd(rsAct.getString("ActualStop")==null?"-":rsAct.getString("ActualStop"));
//					
//					acvtvD.setActvityStatus(rsAct.getString("Status"));			  
//					acvtvD.setJobId(rsAct.getString("JobId"));
//					acvtvD.setSequence(rsAct.getInt("sequence"));
//					acvtvD.setLogbook(rsAct.getString("LogbookName"));
//					acvtvD.setPerformer(rsAct.getString("Performer"));
//					acvtvD.setApprover(rsAct.getString("Approver"));  
//					boolean b=currentPerformer.equalsIgnoreCase(acvtvD.getPerformer())?false:true;	 
//					acvtvD.setNotBelongToPerformer(b);
//					
//				activityList.add(acvtvD);
//					
//				}
//				TaskDetail task=new TaskDetail();
//				task.setTaskName(rs.getString("TaskName"));
//				task.setTaskId(rs.getString("TaskId"));
//				task.setActivityList(activityList);
//				jobD.setTask(task);
//					
//			 jobs.add(jobD);	
//			}	
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}finally {
//			try {
//				con.close();
//				pstmt.close();
//			} catch (Exception e2) {
//				e2.printStackTrace();
//			}
//		}
//	return jobs;	
//	}
//	
//	@Override
//	public void updateFileName(ActivityDetails act, String filename) {
//		
//		String query="UPDATE [dbo].[JobActivityDetails] SET [FileName] = ? Where JobId = ? AND ActivityId = ?";
//		try {
//			con = SQLServerDAOFactory.createConnection();
//			pstmt = con.prepareStatement(query);
//			pstmt.setString(1, filename);
//			pstmt.setString(2, act.getJobId());
//			pstmt.setString(3, act.getActivityId());
//			pstmt.executeUpdate();
//
//		} catch (Exception e) {
//			gAlert.ShowExceptionAlert(e.getMessage());
//			e.printStackTrace();
//		} finally {
//			try {
//				con.close();
//				pstmt.close();
//			} catch (Exception e2) {
//				gAlert.ShowExceptionAlert(e2.getMessage());
//				e2.printStackTrace();
//			}
//		}
//		
//	}
	
	
}
