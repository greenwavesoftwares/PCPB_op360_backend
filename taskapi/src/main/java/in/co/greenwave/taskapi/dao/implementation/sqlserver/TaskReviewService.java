package in.co.greenwave.taskapi.dao.implementation.sqlserver;

import in.co.greenwave.taskapi.dao.TaskReviewDAO;


public class TaskReviewService implements TaskReviewDAO {
//
//	private final G360Alert gAlert=new G360Alert();
//	DateFormat dfDt = new SimpleDateFormat("yyyy-MM-dd");
//	DateFormat dfDtTm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//	Connection con = null;
//	PreparedStatement pstmt = null;
//	
//	@Override
//	public List<TaskDetail> getTaskDetailForReviewer(String reviewer) {
//		List<TaskDetail> taskdetail = new ArrayList<>();
//		System.out.println("TaskReviewService.getTaskDetailForReviewer()");
//		
//		String sql = "select convert(varchar(19),CreationTime,120) CreationTime,TaskName,tab1.TaskId,Creator,HM from ( select [CreationTime],taskName,TaskId,[Creator] from [dbo].[TaskDetails] where [PublishRejectUserId]=? and [Status]='pending' ) tab1 inner join ( select TaskId,left(convert(time,DATEADD(minute,totmin,0)),5) as HM from ( select TaskId,sum(dm) totmin from ( select TaskId,Sequence,max(Duration_min) dm FROM [dbo].[ActivityCreation] group by Sequence,TaskId ) tb group by TaskId ) tab ) tab2 on tab1.TaskId=tab2.TaskId";
//		String actSql = "SELECT ac.TaskId, ac.ActivityId, ActivityName, Pos_X, Post_Y, ac.Sequence, [logbook],[Duration_min],[ActivityAbrv],AssetId,Asset AssetName FROM [dbo].[ActivityCreation] ac Left JOIN [dbo].[ActivityNodePosition] anp ON ac.TaskId=anp.TaskId AND ac.ActivityId=anp.ActivityId WHERE ac.TaskId=? ORDER BY SEQUENCE";
//	    
//		try {
//			con = SQLServerDAOFactory.createConnection();
//			pstmt = con.prepareStatement(sql);
//			pstmt.setString(1, reviewer);
//			ResultSet rs = pstmt.executeQuery();
//			while (rs.next()) {
//				TaskDetail tskd = new TaskDetail();
//				tskd.setCreationTime(rs.getString("CreationTime"));
//				tskd.setTaskName(rs.getString("taskName"));
//				tskd.setTaskId(rs.getString("TaskId"));
//				tskd.setCreator(rs.getString("Creator"));
//				tskd.setHourMinutes(rs.getString("HM"));
//				
//				//fetch actvities
//				  
//					PreparedStatement pstmtAct = con.prepareStatement(actSql);
//					pstmtAct.setString(1, rs.getString("TaskId"));
//					ResultSet rsAct = pstmtAct.executeQuery();
//					List<ActivityDetails> activityList = new LinkedList<ActivityDetails>();
//					while (rsAct.next()) {
//						ActivityDetails acdt=new ActivityDetails();
//						acdt.setTaskId(rsAct.getString("TaskId"));
//						acdt.setActivityId(rsAct.getString("ActivityId"));
//						acdt.setActivityName(rsAct.getString("ActivityName"));
//						acdt.setxPos(rsAct.getString("Pos_X"));
//						acdt.setyPos(rsAct.getString("Post_Y"));
//						acdt.setSequence(rsAct.getInt("Sequence"));
//						acdt.setLogbook(rsAct.getString("logbook"));
//						acdt.setDuration(rsAct.getInt("Duration_min"));
//						acdt.setActAbrv(rsAct.getString("ActivityAbrv"));
//						acdt.setAssetId(rsAct.getString("AssetId"));
//						acdt.setAssetName(rsAct.getString("AssetName"));
//						activityList.add(acdt);
//					}
//				
//				  tskd.setActivityList(activityList);
//				taskdetail.add(tskd);
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
//		return taskdetail;
//	}
//	
//	public List<ActivityConnection> getActivityConnection(String taskId) {
//
//		List<ActivityConnection> activityConnectionList = new ArrayList<>();
//
//		String query = "SELECT  [TaskId],[SourceId],[Source],[TargetId],[Target] FROM [dbo].[ActivityConnections] where taskid=?";
//
//		try {
//			 con = SQLServerDAOFactory.createConnection();
//			pstmt = con.prepareStatement(query);
//			pstmt.setString(1, taskId);
//			ResultSet rs = pstmt.executeQuery();
//			while (rs.next()) {
//				activityConnectionList.add(new ActivityConnection(rs.getString("TaskId"), rs.getString("source"),
//						rs.getString("SourceId"), rs.getString("Target"), rs.getString("TargetId")));
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
//		return activityConnectionList;
//	}
//	
//	// update task status
//
//	public void updateTaskStatus(String taskId,String status) {
//
//		String query = "update [TaskDetails] set [Status]=? where TaskId=?";
//
//		try {
//			con = SQLServerDAOFactory.createConnection();
//			pstmt = con.prepareStatement(query);
//			pstmt.setString(1,status);
//			pstmt.setString(2,taskId);
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
//
//	}
////	update taskname
//	public void updateTaskRemarks(String taskId,String remarks) {
//
//		String query = "update [TaskDetails] set [Remarks]=? where TaskId=?";
//
//		try {
//			 con = SQLServerDAOFactory.createConnection();
//			pstmt = con.prepareStatement(query);
//			pstmt.setString(1,remarks);
//			pstmt.setString(2,taskId);
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
//
//	}
	
}
