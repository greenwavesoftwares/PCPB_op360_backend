package in.co.greenwave.taskapi.dao.implementation.sqlserver;

import in.co.greenwave.taskapi.dao.DashboardDAO;


public class DashboardService implements DashboardDAO {

//	private final G360Alert gAlert=new G360Alert();
//	DateFormat dfDt = new SimpleDateFormat("yyyy-MM-dd");
//	DateFormat dfDtTm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//	Connection con = null;
//	PreparedStatement pstmt = null;
//	@Override
//	public List<JobwiseCardData> fetchJobwiseInfo(String fromDate,String toDate) {
//		List<JobwiseCardData> data = new LinkedList<JobwiseCardData>();
//		try {
//			con = SQLServerDAOFactory.createConnection();
//			pstmt = con.prepareStatement("Select JobId,TaskName JobName,job.TaskId,COALESCE(GroupId,'NA') GroupId,[Progress] = dbo.JobWiseProgress(job.JobId),ScheduleStart,ScheduleStop,ActualStart,ActualStop,[Priority]  " + 
//					"					,Task = (Select TaskName from dbo.TaskDetails task Where job.TaskId = task.TaskId),COALESCE(Instrument,'NA') Instrument  " + 
//					"					,COALESCE(curr_activityName,'NA') curr_activityName,COALESCE(curr_performer,'NA') curr_performer,COALESCE(Asset,'NA') Asset,					" + 
//					"					[JobStatus] = CASE WHEN (GETDATE() < job.ScheduleStart) AND (job.ActualStart IS NULL) THEN CONCAT('Yet-to-Start','/NA')    " + 
//					"					WHEN (GETDATE() < job.ScheduleStart) AND (job.ActualStart IS NOT NULL) THEN CONCAT('On-Time','/NA')    " + 
//					"					WHEN GETDATE() between job.ScheduleStart AND job.ScheduleStop THEN CONCAT('On-Time','/NA')    " + 
//					"					WHEN GETDATE() > job.ScheduleStop THEN CONCAT('Late/',DATEDIFF(MINUTE,job.ScheduleStop,GETDATE()),' Minutes') END "
//					+ " ,PlannedProgress = CASE WHEN ScheduleStop <= GETDATE() THEN 100 " + 
//					"						ELSE (DATEDIFF(MINUTE,ScheduleStart,GETDATE())/DATEDIFF(MINUTE,ScheduleStart,ScheduleStop))*100.0	END from dbo.JobDetails job " + 
//					"					CROSS APPLY dbo.getcurrentInfo(JobId) "
//					+ " Where CONVERT(Date,job.ScheduleStart) between ? AND ? "
//				+" union all "
//					+"SELECT   [jobid],name,TaskId='NA',GroupId='NA',coalesce(dbo.JobWiseProgress(JobId),0) [Progress],ScheduleStart='1900-01-01',ScheduleStop='1900-01-01',[activitystarttime],[activityendtime],Priority='NA',Task=' ',assetname,' ',performer ,'NA',concat(' ','/',status),100.0 FROM [dbo].[AutoJobInfo] where activitystarttime between ? and ?");
//			pstmt.setString(1, fromDate);
//			pstmt.setString(2, toDate);
//			pstmt.setString(3, fromDate);
//			pstmt.setString(4, toDate);
//			ResultSet rs = pstmt.executeQuery();
//
//			while (rs.next()) {
//				List<String> performers = new ArrayList<String>();
//				PreparedStatement ps = con.prepareStatement("Select Distinct Performer,UserName from dbo.JobActivityDetails act,dbo.UserCredential u Where act.Performer = u.UserId AND JobId = ? ");
//				ps.setString(1, rs.getString("JobId"));
//				ResultSet rsP = ps.executeQuery();
//				while(rsP.next()) {
//					performers.add(rsP.getString("UserName"));
//				}	
//				JobwiseCardData cd = new JobwiseCardData(rs.getString("JobId"), rs.getString("JobName"), rs.getString("Priority"),rs.getString("GroupId"),rs.getString("Task"),rs.getString("Instrument"),rs.getInt("Progress"), performers);
//				
//				StringBuilder jobStatus = new StringBuilder(rs.getString("JobStatus"));
//				cd.setWorkStatus(jobStatus.substring(0, jobStatus.indexOf("/")));
//				jobStatus.replace(0, jobStatus.indexOf("/")+1,"");
//				cd.setStatusMsg(jobStatus.substring(0,jobStatus.length()));
//				
//				cd.setAsset(rs.getString("Asset"));
//				cd.setCurrentPerformer(rs.getString("curr_performer"));
//				cd.setCurrentActivity(rs.getString("curr_activityName"));
//				cd.setPlannedProgress(rs.getInt("PlannedProgress"));
//				cd.setTaskId(rs.getString("TaskId"));
//				data.add(cd);
//				ps.close();
//				rsP.close();
//			} 
//			
//		}catch (Exception e) {
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
//		return data;
//	}
//	
//	@Override
//	public List<ActivityDetails> fetchActivityStatus(String jobId){
//		List<ActivityDetails> list = new ArrayList<ActivityDetails>();
//		try {
//			con = SQLServerDAOFactory.createConnection();
//			pstmt = con.prepareStatement(" Select *,[Status] = CASE WHEN GETDATE() < ScheduledStart THEN 'Yet to Start' " + 
//					"	 WHEN ActualStop > ScheduleStop THEN 'Late' " + 
//					"	 WHEN (GETDATE() > ScheduleStop) AND (ActualStop = '1900-01-01 00:00:00.000') THEN 'Late' " + 
//					"	 WHEN (GETDATE() > ScheduledStart) AND (ActualStart = '1900-01-01 00:00:00.000') THEN 'Late' " + 
//					"	 ELSE 'On Time' END	  from " + 
//					"	  (Select JobId,ActivityId,Performer = (Select UserName from dbo.UserCredential u Where u.UserId = Performer),ScheduledStart,ScheduleStop,COALESCE(ActualStart,'1900-01-01 00:00:00') ActualStart,COALESCE(ActualStop,'1900-01-01 00:00:00') ActualStop  " + 
//					"	  FROM [dbo].[JobActivityDetails] Where JobId = ? ) tab1 ");
//			pstmt.setString(1, jobId);
//			
//			ResultSet rs = pstmt.executeQuery();
//
//			while (rs.next()) {
//				ActivityDetails act = new ActivityDetails();
//				act.setJobId(rs.getString("JobId"));
//				act.setActivityId(rs.getString("ActivityId"));
//				act.setPerformer(rs.getString("Performer"));
//				act.setScheduledActivityStartTime(dfDtTm.parse(rs.getString("ScheduledStart")));
//				act.setScheduledActivityEndTime(dfDtTm.parse(rs.getString("ScheduleStop")));
//				act.setActualActivityStartTime(dfDtTm.parse(rs.getString("ActualStart")));
//				act.setActualActivityEndTime(dfDtTm.parse(rs.getString("ActualStop")));
//				act.setActvityStatus(rs.getString("Status"));
//				list.add(act);
//			} 
//			rs.close();
//		}catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				con.close();
//				pstmt.close();
//			} catch (Exception e2) {
//				e2.printStackTrace();
//			}
//		}
//		return list;
//	}
//
//	@Override
//	public List<TimelineGroup<String>> fetchTimelineGroups(String groupType,String fromDate, String toDate) {
//		List<TimelineGroup<String>> groups = new ArrayList<TimelineGroup<String>>();
//		String sql = "";
//		if(groupType.equalsIgnoreCase("User")) {
//			/*sql="Select Distinct act.Performer,[Name]= (Select UserName from dbo.UserCredential Where UserId = act.Performer) ,job.JobId,job.TaskName JobName  " + 
//					"from dbo.JobDetails job,dbo.JobActivityDetails act " + 
//					"Where job.JobId = act.JobId AND job.ScheduleStart between CONVERT(date,?) AND CONVERT(date,?) ";
//			*/
//			
//			sql = "select * from("+"Select Distinct act.Performer,[Name]= (Select UserName from dbo.UserCredential Where UserId = act.Performer) ,job.JobId,job.TaskName JobName  " + 
//					"from dbo.JobDetails job,dbo.JobActivityDetails act " + 
//					"Where job.JobId = act.JobId AND job.ScheduleStart between CONVERT(date,?) AND CONVERT(date,?) " 
//					+" union all "
//					+" Select distinct a.performer,[Name]= (Select UserName from dbo.UserCredential Where UserId = a.Performer),jobid,name from dbo.AutoJobInfo a where a.activitystarttime between CONVERT(date,?) and CONVERT(date,?) "
//					+" )as t order by t.performer,t.jobid";
//		}else if(groupType.equalsIgnoreCase("Task")) {
//			/*sql ="Select job.TaskId,TaskName = (Select TaskName from dbo.TaskDetails Where TaskId = job.TaskId ),job.JobId,job.TaskName JobName  " + 
//					"from dbo.JobDetails job  Where  job.ScheduleStart between CONVERT(date,?) AND CONVERT(date,?) order by TaskId,job.JobId"
//					
//					;*/
//			sql="Select * from("+"Select job.TaskId,TaskName = (Select TaskName from dbo.TaskDetails Where TaskId = job.TaskId ),job.JobId,job.TaskName JobName   from dbo.JobDetails job  Where  job.ScheduleStart between CONVERT(date,?) AND CONVERT(date,?) "
//					+" union all "
//				+"Select ' ',' ',jobid,name from dbo.AutoJobInfo where activitystarttime between CONVERT(date,?) AND CONVERT(date,?) "
//					+")t order by TaskId,JobId"	
//				;
//		}else if(groupType.equalsIgnoreCase("Asset")) {
//			
//		}else if(groupType.equalsIgnoreCase("GroupId")) {
//			/*sql = "Select Distinct COALESCE(GroupId,'NA') GroupId,COALESCE(GroupId,'NA') GroupName,job.JobId,job.TaskName JobName   " + 
//					"from dbo.JobDetails job " + 
//					"Where job.ScheduleStart between CONVERT(date,?) AND CONVERT(date,?) order by GroupId,job.JobId";
//			*/
//			sql="Select * from( Select Distinct COALESCE(GroupId,'NA') GroupId,COALESCE(GroupId,'NA') GroupName,job.JobId,job.TaskName JobName"
//					+" from dbo.JobDetails job  Where job.ScheduleStart between CONVERT(date,?) AND CONVERT(date,?) "
//					+"union all"
//					+" Select distinct ' ',' ',jobid,name from dbo.AutoJobInfo a where a.activitystarttime between CONVERT(date,?) AND CONVERT(date,?) )as t  order by GroupId,JobId";
//					
//		}else if(groupType.equalsIgnoreCase("Instrument")) {
//			/*sql = "Select Distinct COALESCE(Instrument,'NA') Instrument,COALESCE(Instrument,'NA') InstrumentName,job.JobId,job.TaskName JobName   " + 
//					"from dbo.JobDetails job " + 
//					"Where job.ScheduleStart between CONVERT(date,?) AND CONVERT(date,?) order by Instrument,job.JobId";
//				*/
//			sql="select * from("+
//					"Select Distinct COALESCE(Instrument,'NA') Instrument,COALESCE(Instrument,'NA') InstrumentName,job.JobId,job.TaskName JobName "
//					+" from dbo.JobDetails job Where job.ScheduleStart between CONVERT(date,?) AND CONVERT(date,?) "
//					+" union all "
//					+" Select Distinct coalesce(assetid,' '),coalesce(assetname,' '),jobid,name from dbo.AutoJobInfo a where a.activitystarttime between CONVERT(date,?) AND CONVERT(date,?) "
//					+" )as t order by Instrument,JobId ";
//					
//					
//		}
//		
//		try {
//			
//			//System.out.println(sql);
//			con = SQLServerDAOFactory.createConnection();
//			pstmt = con.prepareStatement(sql);
//			pstmt.setString(1, fromDate);
//			pstmt.setString(2, toDate);
//			pstmt.setString(3, fromDate);
//			pstmt.setString(4, toDate);
//			ResultSet rs = pstmt.executeQuery();
//			String prevTaskId = "";
//			String prevTaskName = "";
//			List<String> jIds = new ArrayList<String>();
//			List<String> jNames = new ArrayList<String>();
//			while (rs.next()) {
//				if(prevTaskId.equalsIgnoreCase(rs.getString(1))) {
//					jIds.add(rs.getString(3));
//					jNames.add(rs.getString(4));
//					prevTaskId = rs.getString(1);
//					prevTaskName = rs.getString(2);
//					groups.add(new TimelineGroup<String>(rs.getString(3), rs.getString(4)+"("+rs.getString(3)+")", 2));
//				}else {
//					if(!prevTaskId.equalsIgnoreCase("")) {
//						groups.add(new TimelineGroup<String>(prevTaskId, prevTaskName, 1, jIds));
//					}
//					jIds = new ArrayList<String>();
//					jNames = new ArrayList<String>();
//					jIds.add(rs.getString(3));
//					jNames.add(rs.getString(4));
//					prevTaskId = rs.getString(1);
//					prevTaskName = rs.getString(2);
//					groups.add(new TimelineGroup<String>(rs.getString(3), rs.getString(4)+"("+rs.getString(3)+")", 2));
//				}
//			}
//			groups.add(new TimelineGroup<String>(prevTaskId, prevTaskName, 1, jIds));
//			rs.close();
//		}catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				con.close();
//				pstmt.close();
//			} catch (Exception e2) {
//				e2.printStackTrace();
//			}
//		}
//		return groups;
//	}
//
//	@Override
//	public List<TimelineEvent<String>> getTimelineData(String eventType, String eventId) {
//		List<TimelineEvent<String>> events = new LinkedList<TimelineEvent<String>>();
//		String sql = "";
//		if(eventType.equalsIgnoreCase("Job")) {
//			sql = "Select TaskId,JobId,Convert(smalldatetime,ScheduleStart) ScheduleStart,Convert(smalldatetime,ScheduleStop) ScheduleStop,Convert(smalldatetime,COALESCE(ActualStart,ScheduleStart)) ActualStart,Convert(smalldatetime,COALESCE(ActualStop,ScheduleStart)) ActualStop from dbo.JobDetails Where JobId = ?";
//		}else {
//			sql = "Select JobId,ActivityId,Convert(smalldatetime,ScheduledStart) ScheduledStart,Convert(smalldatetime,ScheduleStop) ScheduleStop,Convert(smalldatetime,COALESCE(ActualStart,ScheduledStart)) ActualStart,Convert(smalldatetime,COALESCE(ActualStop,ScheduledStart)) ActualStop from dbo.JobActivityDetails Where JobId = ?";
//		}
//		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
//		try {
//			con = SQLServerDAOFactory.createConnection();
//			pstmt = con.prepareStatement(sql);
//			pstmt.setString(1, eventId);
//			
//			ResultSet rs = pstmt.executeQuery();
//			
//			while (rs.next()) {
//				events.add(TimelineEvent.<String>builder()
//                        .data(rs.getString(2))
//                        .startDate(LocalDateTime.parse(rs.getString(3), formatter))
//                        .endDate(LocalDateTime.parse(rs.getString(4), formatter))
//                        .editable(false)
//                        .group(rs.getString(1))
//                        .styleClass("available")
//                        .build());
//				events.add(TimelineEvent.<String>builder()
//                        .data(rs.getString(2))
//                        .startDate(LocalDateTime.parse(rs.getString(5), formatter))
//                        .endDate(LocalDateTime.parse(rs.getString(6), formatter))
//                        .editable(false)
//                        .group(rs.getString(1))
//                        .styleClass("unavailable")
//                        .build());
//			}
//			
//			rs.close();
//		}catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				con.close();
//				pstmt.close();
//			} catch (Exception e2) {
//				e2.printStackTrace();
//			}
//		}
//		return events;
//	}
//	@Override
//	public List<JobDetails> fetchAllJobDetails(String fromdate, String todate) {
//		
//		List<JobDetails> jobs = new LinkedList<JobDetails>();
//		String sql ;//= 	"SELECT a.[TaskId] ,[JobId],coalesce(a.GroupId,'NA') [GroupId] ,dbo.JobWiseProgress(JobId) [Progress],a.[TaskName],b.TaskName [TaskTemplate] ,[Instrument] ,[Approver] ,[ScheduleStart] ,[ScheduleStop] ,[ActualStart] ,[ActualStop] ,[ReviewerIntimationTime] ,[ReviewercompletionTime] ,[Priority] ,[Repeat] ,[RepeatTill] ,a.[Remarks] ,a.[Status],curr_activityId,curr_activityName,curr_performer FROM [dbo].[JobDetails] a CROSS APPLY dbo.getcurrentInfo(JobId) x left join dbo.TaskDetails b on a.TaskId=b.TaskId Where  a.ScheduleStart between ? and ?";
//		sql="Select *,case when SUBSTRING(JobId,1,1)='A' then 'Self Assigned Job' else 'Non Self Assigned job' end [Job Type] from("
//		+"SELECT a.[TaskId] ,[JobId],coalesce(a.GroupId,'NA') [GroupId] ,dbo.JobWiseProgress(JobId) [Progress],a.[TaskName],b.TaskName [TaskTemplate] ,[Instrument] ,[Approver] ,[ScheduleStart] ,[ScheduleStop] ,[ActualStart] ,[ActualStop] ,[ReviewerIntimationTime] ,[ReviewercompletionTime] ,[Priority] ,[Repeat] ,[RepeatTill] ,a.[Remarks] ,coalesce(a.ReviewerAction,a.[Status]) [Status],curr_activityId,curr_activityName,curr_performer,formname=' ' FROM [dbo].[JobDetails] a CROSS APPLY dbo.getcurrentInfo(JobId) x left join dbo.TaskDetails b on a.TaskId=b.TaskId Where  a.ScheduleStart between ? and dateadd(day,1,?)"
//			+" union all "
//		+"SELECT  TaskId=' ', [jobid],GroupId=' ',CASE when status='Approved' then 100 else 0 end as [Progress],name,TaskTemplate=' ', [assetname] [Instrument],reviewer [Approver],ScheduleStart='1900-01-01',ScheduleStop='1900-01-01',[activitystarttime],[activityendtime],reviewstarttime,reviewendtime,Priority=' ',REpeat=NULL,NULL,Remarks=' ',[status],activityid,activityname='NA',performer,formname FROM [dbo].[AutoJobInfo] a  where activitystarttime between ? and dateadd(day,1,?) "
//			+")as t";
//		String actSql;// = "SELECT [JobId] ,job.[ActivityId],job.[TaskId],ActivityName,[Sequence] ,[LogbookName] ,[Performer] ,[Approver] ,[ScheduledStart] ,[ScheduleStop] ,[ActualStart] ,[ActualStop] ,[Status],Duration_min,[Asset] = (Select Asset from [dbo].[ActivityCreation] cr Where cr.TaskId = act.TaskId AND cr.ActivityId = act.ActivityId) FROM [dbo].[JobActivityDetails] job,dbo.ActivityCreation act  Where job.ActivityId = act.ActivityId AND job.TaskId = act.TaskId AND [JobId] = ? Order by [Sequence]";
//		actSql="SELECT [JobId] ,job.[ActivityId],job.[TaskId],ActivityName,[Sequence] ,[LogbookName],[UserName] = Performer ,[Performer],[Approver] ,[ScheduledStart] ,[ScheduleStop] ,[ActualStart] ,[ActualStop] ,[Status],Duration_min,[Asset] = (Select Asset from [dbo].[ActivityCreation] cr Where cr.TaskId = act.TaskId AND cr.ActivityId = act.ActivityId) FROM [dbo].[JobActivityDetails] job ,dbo.ActivityCreation act  Where job.ActivityId = act.ActivityId and  job.TaskId = act.TaskId AND [JobId] = ? Order by [Sequence]";
//		try {
//			con = SQLServerDAOFactory.createConnection();
//			pstmt = con.prepareStatement(sql);
//			pstmt.setString(1, fromdate);
//			pstmt.setString(2, todate);
//			pstmt.setString(3, fromdate);
//			pstmt.setString(4, todate);
//			ResultSet rs = pstmt.executeQuery();
//			while (rs.next()) {
//				//System.out.print("RowCnt= "+rs.getRow()+" ");
//				JobDetails jobD=new JobDetails();
//				TaskDetail taskD=new TaskDetail();
//				taskD.setTaskId(rs.getString("TaskId"));
//				taskD.setTaskName(rs.getString("TaskTemplate"));
//				jobD.setTask(taskD);
//				jobD.setGroupId(rs.getString("GroupId"));
//				jobD.setJobID(rs.getString("JobId"));
//				jobD.setApprover(rs.getString("Approver"));
//				SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");   
//				Date StrtD=format.parse(rs.getString("ScheduleStart")); 
//				Date stpD=format.parse(rs.getString("ScheduleStop")); 
//				jobD.setJobName(rs.getString("TaskName"));
//				jobD.setScheduledJobStartTime(StrtD);
//				jobD.setScheduledJobEndTime(stpD);
//				jobD.setPriority(rs.getString("Priority"));
//				jobD.setJobStatus(rs.getString("Status"));
//				//if(rs.getString("JobId").charAt(0)=='A') {
//					jobD.setActualJobStartTime(format.parse(rs.getString("ActualStart")!=null?rs.getString("ActualStart"):"1900-01-01 00:00:00"));
//					jobD.setActualJobEndTime(format.parse(rs.getString("ActualStop")!=null?rs.getString("ActualStop"):"1900-01-01 00:00:00"));
//				/*}else {
//					jobD.setActualJobStartTime(format.parse(rs.getString("ScheduleStart")));
//					jobD.setActualJobEndTime(format.parse(rs.getString("ScheduleStop")));
//				}*/
//				jobD.setProgress(rs.getInt("Progress"));
//				jobD.setReviewerIntimationTime(rs.getString("ReviewerIntimationTime")!= null ? format.parse(rs.getString("ReviewerIntimationTime")) : format.parse("1900-01-01 00:00:00"));
//				jobD.setCurrentActivityName(rs.getString("curr_activityName"));
//				jobD.setCurrentPerformer(rs.getString("curr_performer"));
//				jobD.setInstrument( rs.getString("Instrument")==" " ? "NA":rs.getString("Instrument"));
//		
//				
//				PreparedStatement pstmtAct = con.prepareStatement(actSql);
//				pstmtAct.setString(1, rs.getString("JobId"));
//				ResultSet rsAct = pstmtAct.executeQuery();
//				List<ActivityDetails> activityList = new LinkedList<ActivityDetails>();
//				
//				
//				ActivityDetails act=null;
//				if(rs.getString("JobId").charAt(0)=='A') {
//					
//					 act=new ActivityDetails(" ",rs.getString("curr_activityId"),rs.getString("curr_activityName") , 1, rs.getString("formname")!=null?rs.getString("formname"):" ", 100,rs.getString("Instrument")!=null?rs.getString("Instrument"):" ");
//					 //act.setScheduledActivityStartTime(format.parse(rs.getString("ScheduleStart")));
//						act.setScheduledActivityEndTime(format.parse(rs.getString("ScheduleStop")));
//						act.setActualActivityStartTime(rs.getString("ActualStart")!= null ? format.parse(rs.getString("ActualStart")): format.parse("1900-01-01 00:00:00"));
//						act.setActualActivityEndTime(rs.getString("ActualStop") != null? format.parse(rs.getString("ActualStop")) : format.parse("1900-01-01 00:00:00"));
//						act.setActvityStatus(rs.getString("Status"));
//						act.setPerformer(rs.getString("curr_performer"));
//						activityList.add(act);
//						
//				}
//				else {
//				while(rsAct.next()) {
//					
//					//System.out.println("TaskTemplate="+rs.getString("TaskTemplate")+" Job ID= "+rs.getString("JobId")+" Activity Id="+rsAct.getString("ActivityId")+"  Actual Start="+rsAct.getString("ActualStart"));
//					 act = new ActivityDetails(rsAct.getString("TaskId")!=null?rsAct.getString("TaskId"):" ", rsAct.getString("ActivityId")!=null?rsAct.getString("ActivityId"):" ", rsAct.getString("ActivityName")!=null?rsAct.getString("ActivityName"):" ", rsAct.getInt("Sequence"), rsAct.getString("LogbookName")!=null?rsAct.getString("LogbookName"):" ", rsAct.getInt("Duration_min"),rsAct.getString("Asset")!=null?rsAct.getString("Asset"):" ");
//					act.setScheduledActivityStartTime(format.parse(rsAct.getString("ScheduledStart")));
//					act.setScheduledActivityEndTime(format.parse(rsAct.getString("ScheduleStop")));
//					act.setActualActivityStartTime(rsAct.getString("ActualStart")!= null ? format.parse(rsAct.getString("ActualStart")):  format.parse("1900-01-01 00:00:00"));
//					act.setActualActivityEndTime(rsAct.getString("ActualStop") != null? format.parse(rsAct.getString("ActualStop")) : format.parse("1900-01-01 00:00:00"));
//					act.setActvityStatus(rsAct.getString("Status"));
//					act.setPerformer(rsAct.getString("Performer"));
//					activityList.add(act);
//					if(act.getActivityName().equalsIgnoreCase(rs.getString("curr_activityName"))) {
//						int t = ((int)(new Date().getTime() - format.parse(rsAct.getString("ScheduledStart")).getTime()))/60000; 
//						jobD.setActivityWaitingTime(t);
//					}
//					
//				}	
//				}
//				TaskDetail task = new TaskDetail(rs.getString("TaskTemplate"), rs.getString("TaskId"), activityList);
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
//
//	}
//	@Override
//	public FormDetails fetchFormDetails(String jobId, String activityId) {
//
//		FormDetails formDetails=null;//=new FormDetails();
//		String sql="Select distinct b.* from dbo.LogbookTransactionData a,dbo.DigitalLogbookFormInfo b where a.JobId=? and a.ActivityId=? and b.VersionNumber=a.version and a.formname=b.FormName ";
//		try {
//			con = SQLServerDAOFactory.createConnection();
//			pstmt = con.prepareStatement(sql);
//			pstmt.setString(1, jobId);
//			pstmt.setString(2, activityId);
//			ResultSet rs = pstmt.executeQuery();
//			while (rs.next()) {
//				//formDetails=new FormDetails(rs.getInt("FormId"), rs.getString("FormName"), rs.getInt("VersionNumber"), rs.getString("EquipmentID"), rs.getString("Department"), rs.getString("EquipmentName"), rs.getInt("FormatNumber"), rs.getBoolean("isActiveForm"));
//				formDetails=new FormDetails(rs.getInt("formID"), rs.getString("FormName"), rs.getInt("versionNumber"), rs.getString("DocumentID"),rs.getString("Department"), rs.getString("UserGroup"), rs.getString("FormatID") , rs.getBoolean("isActiveForm"));
//				//System.out.println(formDetails.getFormName());;
//			}
//		}catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				con.close();
//				pstmt.close();
//			} catch (Exception e2) {
//				e2.printStackTrace();
//			}
//		}
//		return formDetails;
//
//	}
//
//
//	@Override
//	public List<Transactiondata> fetchFromActivityId(String jobId,String activityId) {
//		// TODO Auto-generated method stub
//		List<Transactiondata> transactionDataList=new ArrayList<Transactiondata>();
//		String sql;//="Select ld.TransactionId,ld.transaction_remarks,lb.[key],cell.AliasId,lb.[value],ld.version,g.FormName [FormName] from dbo.LogbookTransactionData ld cross apply OpenJSON(logbookdata) lb,dbo.DigitalLogbookFormInfo g,dbo.DigitalLogbookCellDetails cell where   ld.JobId=? and ActivityId=?  and g.FormName=ld.formname and cell.FormId=g.FormId and cell.VersionNumber=ld.version and cell.CellId=lb.[key]  COLLATE SQL_Latin1_General_CP1_CI_AS and [value] like '{\"value\":%' order by ld.TransactionId";
//		sql="Select ld.TransactionId,ld.transaction_remarks,lb.[key],cell.AliasId,lb.[value],ld.version,g.FormName [FormName] from dbo.LogbookTransactionData ld cross apply OpenJSON(logbookdata) lb,dbo.DigitalLogbookFormInfo g,dbo.DigitalLogbookCellDetails cell where   ld.JobId=? and ld.ActivityId=?  and g.FormName=ld.formname and cell.FormId=g.FormId and cell.VersionNumber=ld.version and cell.CellId=lb.[key]  COLLATE SQL_Latin1_General_CP1_CI_AS and [value] like '{\"value\":%' order by ld.TransactionId";
//		
//		try {
//			con = SQLServerDAOFactory.createConnection();
//			pstmt = con.prepareStatement(sql);
//			pstmt.setString(1, jobId);
//			pstmt.setString(2, activityId);
//			
//			ResultSet rs = pstmt.executeQuery();
//			Gson gson=new Gson();
//			
//			String transactionId="";
//			String cellValue,userRemarks="",transactionRemarks="";
//			Transactiondata.TrData trData;
//			
//		    
//		   //Store First Value
//			if(rs.next()) {
//				trData=gson.fromJson(rs.getString("value"),Transactiondata.TrData.class );
//				transactionId=rs.getString("TransactionId");
//				userRemarks="Alias: "+rs.getString("AliasId")+"  Value:"+trData.getValue()+"   Remarks:"+trData.getRemarks();
//				transactionRemarks=rs.getString("transaction_remarks");
//				
//				
//			}
//			
//			
//			while(rs.next()) {
//			    if(transactionId.equalsIgnoreCase(rs.getString("TransactionId"))) {
//			    	trData=gson.fromJson(rs.getString("value"),Transactiondata.TrData.class );
//			    	userRemarks=userRemarks+"\r\n Alias: "+rs.getString("AliasId")+"  Value:"+trData.getValue()+"     Remarks:"+trData.getRemarks();
//			    	continue;
//			    	}
//			    	Transactiondata transactionData=new Transactiondata();
//			    	transactionData.setTransactionid(transactionId);
//			    	transactionData.setRemarks(transactionRemarks);
//			    	transactionData.setUserRemaks(userRemarks);
//			    	transactionDataList.add(transactionData); 
//			    	trData=gson.fromJson(rs.getString("value"),Transactiondata.TrData.class );
//					transactionId=rs.getString("TransactionId");
//					userRemarks="Alias: "+rs.getString("AliasId")+"  Value:"+trData.getValue()+"   Remarks:"+trData.getRemarks();
//					transactionRemarks=rs.getString("transaction_remarks");
//			    		
//			}
//			
//			//Store Last Data
//				Transactiondata transactionData=new Transactiondata();
//		    	transactionData.setTransactionid(transactionId);
//		    	transactionData.setRemarks(transactionRemarks);
//		    	transactionData.setUserRemaks(userRemarks);
//		    	transactionDataList.add(transactionData); 
//		    	if(transactionId=="") {
//		    		return null;
//		    	}
//		}catch (Exception e) {
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
//		return transactionDataList;
//	}
//
//	@Override
//	public ActivityDetails fetchActivitiy(String jobId, String activityId) {
//		// TODO Auto-generated method stub
//		ActivityDetails activityDetails=new ActivityDetails();
//		String sql="Select a.JobId,a.ActivityId,b.TaskName,c.ActivityName,c.Asset from dbo.JobActivityDetails a,dbo.TaskDetails b,dbo.ActivityCreation c where a.JobId=? and a.ActivityId=? and a.TaskId=b.TaskId and c.TaskId=b.TaskId and c.ActivityId=a.ActivityId"
//					+" union all " + 
//					"Select a.jobid,a.activityid,TaskName='NA','NA',a.assetname from dbo.AutoJobInfo a where a.jobid=? and a.ActivityId=?";
//		try {
//			con = SQLServerDAOFactory.createConnection();
//			pstmt = con.prepareStatement(sql);
//			pstmt.setString(1, jobId);
//			pstmt.setString(2, activityId);
//			pstmt.setString(3, jobId);
//			pstmt.setString(4, activityId);
//			ResultSet rs = pstmt.executeQuery();
//			while(rs.next()) {
//				activityDetails.setTaskName(rs.getString("TaskName")!=null?rs.getString("TaskName"):"NA");
//				activityDetails.setActivityName(rs.getString("ActivityName")!=null?rs.getString("ActivityName"):"NA");
//				activityDetails.setAssetName(rs.getString("Asset")!=null?rs.getString("Asset"):"NA");
//			}
//		}catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				con.close();
//				pstmt.close();
//			} catch (Exception e2) {
//				e2.printStackTrace();
//			}
//		}
//		return activityDetails;
//	}
//
//	@Override
//	public List<Transactiondata> fetchFromDateRangeLogBook(String fromdate, String todate, String fromname) {
//		List<Transactiondata> transactionDataList=new ArrayList<Transactiondata>();
//		String sql="Select * from(Select formname,version,JobId,ActivityId,MAX(timestamp) LastTimeStamp from dbo.LogbookTransactionData where formname=? and timestamp between ? and ? group by formname,version,JobId,ActivityId)t order by LastTimeStamp";
//		
//		try {
//			con = SQLServerDAOFactory.createConnection();
//			pstmt = con.prepareStatement(sql);
//			pstmt.setString(1, fromname);
//			pstmt.setString(2, fromdate);
//			pstmt.setString(3, todate);
//			ResultSet rs = pstmt.executeQuery();
//			while(rs.next()) {
//				Transactiondata transactionData=new Transactiondata();
//				transactionData.setFormname(rs.getString("formname"));
//				transactionData.setJobid(rs.getString("JobId"));
//				transactionData.setActivityid(rs.getString("ActivityId"));
//				transactionData.setTransactionTimeStamp(rs.getString("LastTimeStamp"));
//				transactionDataList.add(transactionData);
//			}
//		}catch (Exception e) {
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
//		return transactionDataList;
//	}



	
	
	}
