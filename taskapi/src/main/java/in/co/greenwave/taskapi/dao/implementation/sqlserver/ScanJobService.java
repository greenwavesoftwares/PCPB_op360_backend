package in.co.greenwave.taskapi.dao.implementation.sqlserver;

import in.co.greenwave.taskapi.dao.ScanJobDAO;



public class ScanJobService implements ScanJobDAO {
//	private final G360Alert gAlert=new G360Alert();
//	DateFormat dfDt = new SimpleDateFormat("yyyy-MM-dd");
//	DateFormat dfDtTm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//
//
//	@Override
//	public AssetModel getLogbooksofAsset(String assetId) {
//		AssetModel model = new AssetModel();
//		String query = "Select TOP 1 * FROM [dbo].[AssetInfo] Where AssetId = ?";
//
//		try {
//			Connection con = SQLServerDAOFactory.createConnection();
//			PreparedStatement pstmt = con.prepareStatement(query);
//			pstmt.setString(1, assetId);
//			ResultSet rs = pstmt.executeQuery();
//			while (rs.next()) {
//				String logbooksListString = rs.getString("logbooks");
//				String usersListString = rs.getString("users");
//				model = new AssetModel(rs.getString("AssetId"), rs.getString("AssetName"), false, rs.getString("Icon"), rs.getString("Category"), rs.getString("Image"));
//
//				if(logbooksListString != null) {
//					List<String> logbooksList = Arrays.asList(logbooksListString.split("\\s*,\\s*"));
//					List<Integer> logbooks = new LinkedList<Integer>();
//					for(String book : logbooksList) {
//						logbooks.add(Integer.parseInt(book));
//					}
//					model.setLogbooksList(logbooks);
//				}
//				if(usersListString != null) {
//					List<String> usersList = Arrays.asList(usersListString.split("\\s*,\\s*"));
//					model.setUsersList(usersList);
//
//				}
//			}
//
//			con.close();
//			pstmt.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			try {
//
//			} catch (Exception e2) {
//				e2.printStackTrace();
//			}
//		}
//
//		return model;
//	}
//
//
//	@Override
//	public AutoJob getPendingJobInfoforAssetId(String assetid) {
//		// Select * from [dbo].[AutoJobInfo] Where assetid = '' AND [status] <> 'Completed'
//		AutoJob job = null;
//		String query = "Select * from [dbo].[AutoJobInfo] Where assetid = ? AND [status] NOT IN ('Approved','Rejected')";
//		Connection con = SQLServerDAOFactory.createConnection();
//		try {
//			PreparedStatement pstmt = con.prepareStatement(query);
//			pstmt.setString(1, assetid);
//			ResultSet rs = pstmt.executeQuery();
//			while (rs.next()) {
//				job = new AutoJob(rs.getString("jobid"), rs.getString("activityid"), rs.getString("name"), rs.getString("assetid"), rs.getString("assetname"), rs.getInt("logbookid"), rs.getString("formname"), rs.getInt("version"), rs.getString("performer"), rs.getString("reviewer"), dfDtTm.parse(rs.getString("activitystarttime")), rs.getString("status"));
//			}
//			pstmt.close();
//			rs.close();
//		} catch (Exception e) {
//			gAlert.ShowErrorAlert(e.getMessage());
//			e.printStackTrace();
//		} finally {
//			try {
//				con.close();
//			} catch (Exception e2) {
//				e2.printStackTrace();
//			}
//		}
//		return job;
//	}
//
//	@Override
//	public AutoJob getPendingJobInfoforLogbook(String formname,int formid,int version,String user) {
//		// Select * from [dbo].[AutoJobInfo] Where assetid = '' AND [status] <> 'Completed'
//		AutoJob job = null;
//		String query = "Select * from [dbo].[AutoJobInfo] Where [formname] = ? AND [logbookid] = ? AND [version] = ? AND [performer] = ? AND [assetid] IS NULL AND [status] NOT IN ('Approved','Rejected')";
//		Connection con = SQLServerDAOFactory.createConnection();
//		try {
//			PreparedStatement pstmt = con.prepareStatement(query);
//			pstmt.setString(1, formname);
//			pstmt.setInt(2, formid);
//			pstmt.setInt(3, version);
//			pstmt.setString(4, user);
//			ResultSet rs = pstmt.executeQuery();
//			while (rs.next()) {
//				job = new AutoJob(rs.getString("jobid"), rs.getString("activityid"), rs.getString("name"), rs.getString("assetid"), rs.getString("assetname"), rs.getInt("logbookid"), rs.getString("formname"), rs.getInt("version"), rs.getString("performer"), rs.getString("reviewer"), dfDtTm.parse(rs.getString("activitystarttime")), rs.getString("status"));
//			}
//			pstmt.close();
//			rs.close();
//		} catch (Exception e) {
//			gAlert.ShowErrorAlert(e.getMessage());
//			e.printStackTrace();
//		} finally {
//			try {
//				con.close();
//			} catch (Exception e2) {
//				e2.printStackTrace();
//			}
//		}
//		return job;
//	}
//	@Override
//	public boolean initialteNewAutoJob(AutoJob job) {
//		Connection con = SQLServerDAOFactory.createConnection();
//		String query = "INSERT INTO [dbo].[AutoJobInfo] ([jobid] ,[activityid] ,[name] ,[assetid] ,[assetname] ,[logbookid] ,[formname] ,[version] ,[performer] ,[reviewer] ,[activitystarttime] ,[status]) " + 
//				"  values (?,?,?,?,?,?,?,?,?,?,GETDATE(),?)";
//		boolean allright = false;
//		try {
//			PreparedStatement pstmt = con.prepareStatement(query);
//			pstmt.setString(1, job.getJobid());
//			pstmt.setString(2, job.getActivityid());
//			pstmt.setString(3,job.getJobname());
//			pstmt.setString(4,job.getAssetid());
//			pstmt.setString(5,job.getAssetname());
//			pstmt.setInt(6,job.getLogbookid());
//			pstmt.setString(7,job.getFormname());
//			pstmt.setInt(8,job.getLogbookversion());
//			pstmt.setString(9,job.getPerformer());
//			pstmt.setString(10,job.getReviewer());
//			pstmt.setString(11,job.getStatus());
//
//			int col = pstmt.executeUpdate();
//			if(col >0 ) {
//				gAlert.ShowInfoAlert("New Job Initiated "+job.getJobid());
//				allright = true;
//			}
//			else {
//				gAlert.ShowWarningAlert("No Job Initiated");
//				allright = false;
//			}
//			pstmt.close();		
//
//		} catch (Exception e) {
//			gAlert.ShowErrorAlert(e.getMessage());
//			e.printStackTrace();
//		} finally {
//			try {
//				con.close();
//			} catch (Exception e2) {
//				e2.printStackTrace();
//			}
//		}
//		return allright;
//	}
//
//
//	@Override
//	public boolean updateJobEndTime(String jobId, String activityId, String assetid, Date endtime) {
//		// UPDATE [dbo].[AutoJobInfo] SET activityendtime = ? Where jobid = ? AND activityid = ? AND assetid = ?
//		Connection con = SQLServerDAOFactory.createConnection();
//		String query = "UPDATE [dbo].[AutoJobInfo] SET activityendtime = ?, [status] = IIF([performer]<> [reviewer], 'Sent for approval','Approved') Where jobid = ? AND activityid = ? AND assetid = ?";
//		if(assetid == null)
//			query = "UPDATE [dbo].[AutoJobInfo] SET activityendtime = ?, [status] = IIF([performer]<> [reviewer], 'Sent for approval','Approved') Where jobid = ? AND activityid = ? AND assetid IS NULL";
//		boolean allright = false;
//		
//		try {
//			PreparedStatement pstmt = con.prepareStatement(query);
//			pstmt.setString(1, dfDtTm.format(endtime));
//			pstmt.setString(2, jobId);
//			pstmt.setString(3, activityId);
//			if(assetid != null)
//				pstmt.setString(4, assetid);
//			
//
//			int col = pstmt.executeUpdate();
//			if(col >0 ) {
//				gAlert.ShowInfoAlert("Job Updated"+jobId);
//				allright = true;
//			}
//			else {
//				System.err.println("End time not updated");
//				allright = false;
//			}
//			pstmt.close();		
//
//		} catch (Exception e) {
//			gAlert.ShowErrorAlert(e.getMessage());
//			e.printStackTrace();
//		} finally {
//			try {
//				con.close();
//			} catch (Exception e2) {
//				e2.printStackTrace();
//			}
//		}
//		return allright;
//	}
//
//
//	@Override
//	public List<AutoJob> getPendingforApprovalSelfAssignedJobs(String userid) {
//		List<AutoJob> data = new ArrayList<AutoJob>();
//		String query = "Select * from [dbo].[AutoJobInfo] Where [reviewer] = ? AND [status] IN ('Sent for approval','Review Started')";
//		Connection con = SQLServerDAOFactory.createConnection();
//		try {
//			PreparedStatement pstmt = con.prepareStatement(query);
//			pstmt.setString(1, userid);
//			ResultSet rs = pstmt.executeQuery();
//			while (rs.next()) {
//				AutoJob job = new AutoJob(rs.getString("jobid"), rs.getString("activityid"), rs.getString("name"), rs.getString("assetid"), rs.getString("assetname"), rs.getInt("logbookid"), rs.getString("formname"), rs.getInt("version"), rs.getString("performer"), rs.getString("reviewer"), dfDtTm.parse(rs.getString("activitystarttime")), dfDtTm.parse(rs.getString("activityendtime")), rs.getString("status"));
//				job.setRemarks(rs.getString("remarks"));
//				data.add(job);
//			}
//			pstmt.close();
//			rs.close();
//		} catch (Exception e) {
//			gAlert.ShowErrorAlert(e.getMessage());
//			e.printStackTrace();
//		} finally {
//			try {
//				con.close();
//			} catch (Exception e2) {
//				e2.printStackTrace();
//			}
//		}
//		return data;
//	}
//
//
//	@Override
//	public boolean startReviewForSelfAssignedJob(String jobId, String activityId, String assetid) {
//		// UPDATE [dbo].[AutoJobInfo] SET [reviewstarttime] = GETDATE(),[status] = 'Review Started' Where jobid = ? AND activityid = ? AND assetid = ?
//		Connection con = SQLServerDAOFactory.createConnection();
//		String query = "UPDATE [dbo].[AutoJobInfo] SET [reviewstarttime] = GETDATE(),[status] = 'Review Started' Where jobid = ? AND activityid = ? AND assetid = ?";
//		if(assetid == null)
//			query = "UPDATE [dbo].[AutoJobInfo] SET [reviewstarttime] = GETDATE(),[status] = 'Review Started' Where jobid = ? AND activityid = ? AND assetid IS NULL";
//		boolean allright = false;
//		try {
//			PreparedStatement pstmt = con.prepareStatement(query);
//			pstmt.setString(1, jobId);
//			pstmt.setString(2, activityId);
//			if(assetid  != null)
//				pstmt.setString(3, assetid);	
//
//			int col = pstmt.executeUpdate();
//			if(col >0 ) {
//				gAlert.ShowInfoAlert("Review Started for"+jobId);
//				allright = true;
//			}
//			else {
//				System.err.println("Review not started");
//				allright = false;
//			}
//			pstmt.close();		
//
//		} catch (Exception e) {
//			gAlert.ShowErrorAlert(e.getMessage());
//			e.printStackTrace();
//		} finally {
//			try {
//				con.close();
//			} catch (Exception e2) {
//				e2.printStackTrace();
//			}
//		}
//		return allright;
//	}
//
//
//	@Override
//	public boolean approveSelfAssignedJob(String jobId, String activityId, String assetid,String remarks,String status) {
//		Connection con = SQLServerDAOFactory.createConnection();
//		String query = "UPDATE [dbo].[AutoJobInfo] SET [reviewendtime] = GETDATE(), [status] = ?,[remarks] = ? Where jobid = ? AND activityid = ? AND assetid = ?";
//		if(assetid == null)
//			query = "UPDATE [dbo].[AutoJobInfo] SET [reviewendtime] = GETDATE(), [status] = ?,[remarks] = ? Where jobid = ? AND activityid = ? AND assetid IS NULL";
//		boolean allright = false;
//		try {
//			PreparedStatement pstmt = con.prepareStatement(query);
//			pstmt.setString(1, status);
//			pstmt.setString(2, remarks);
//			pstmt.setString(3, jobId);
//			pstmt.setString(4, activityId);
//			if(assetid  != null)
//				pstmt.setString(5, assetid);	
//
//			int col = pstmt.executeUpdate();
//			if(col >0 ) {
//				gAlert.ShowInfoAlert(jobId+" "+status);
//				allright = true;
//			}
//			else {
//				System.err.println("No data updated");
//				allright = false;
//			}
//			pstmt.close();		
//
//		} catch (Exception e) {
//			gAlert.ShowErrorAlert(e.getMessage());
//			e.printStackTrace();
//		} finally {
//			try {
//				con.close();
//			} catch (Exception e2) {
//				e2.printStackTrace();
//			}
//		}
//		return allright;
//	}

}
