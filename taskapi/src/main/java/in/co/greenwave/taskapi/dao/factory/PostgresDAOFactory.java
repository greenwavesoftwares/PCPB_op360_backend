package in.co.greenwave.taskapi.dao.factory;

import in.co.greenwave.taskapi.dao.AssetConfigurationDAO;
import in.co.greenwave.taskapi.dao.JobAssignDAO;
import in.co.greenwave.taskapi.dao.JobReviewDAO;
import in.co.greenwave.taskapi.dao.LogbookDAO;
import in.co.greenwave.taskapi.dao.LoginDAO;
import in.co.greenwave.taskapi.dao.RestDAO;
import in.co.greenwave.taskapi.dao.TaskMasterDAO;


public class PostgresDAOFactory extends DAOFactory {

	@Override
	public TaskMasterDAO getTaskMasterService() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RestDAO getRestfulService() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JobAssignDAO getJobAssignService() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LoginDAO getLoginService() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AssetConfigurationDAO getAssetConfigurationService() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LogbookDAO getLogbookService() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JobReviewDAO getJobReviewService() {
		// TODO Auto-generated method stub
		return null;
	}

//	public static Connection createConnection() {
//	    // Use DRIVER and DBURL to create a connection
//	    // Recommend connection pool implementation/usage
//		DataSource dataSource = null;
//		Connection con = null;
//		try {
//			Context initContext  = new InitialContext();
//			Context envContext  = (Context)initContext.lookup("java:/comp/env");
//			dataSource = (DataSource)envContext.lookup("taskdata");
//			con = dataSource.getConnection();
//		}catch(NamingException e){
//			System.out.println("source caught exception");
//			e.printStackTrace();
//		}catch(SQLException e){
//			e.printStackTrace();
//		}	
//		return con;
//	  }
/*	@Override
	public CommonUtilityDAO getCommonUtilityService() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DashboardDAO getDashBoardService() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JobAssignDAO getJobAssignService() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JobPerformDAO getJobPerformService() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JobReviewDAO getJobReviewService() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LogbookDAO getLogbookService() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LoginDAO getLoginService() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaskMasterDAO getTaskMasterService() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaskReviewDAO getTaskReviewService() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ActivityReviewDAO getActivityReviewService() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public AssetConfigurationDAO getAssetConfigurationService() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ScanJobDAO getScanJobService() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public LogbookReportsDAO getLogbookReportsService() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public RestDAO getRestfulService() {
		// TODO Auto-generated method stub
		return null;
	}
*/
}
