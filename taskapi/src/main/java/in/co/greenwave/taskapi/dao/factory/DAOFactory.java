package in.co.greenwave.taskapi.dao.factory;

import in.co.greenwave.taskapi.dao.AssetConfigurationDAO;
import in.co.greenwave.taskapi.dao.JobAssignDAO;
import in.co.greenwave.taskapi.dao.JobReviewDAO;
import in.co.greenwave.taskapi.dao.LogbookDAO;
import in.co.greenwave.taskapi.dao.LoginDAO;
import in.co.greenwave.taskapi.dao.RestDAO;
import in.co.greenwave.taskapi.dao.TaskMasterDAO;

public abstract class DAOFactory {
	public abstract TaskMasterDAO getTaskMasterService();
	public abstract RestDAO getRestfulService() ;
	public abstract JobAssignDAO getJobAssignService() ;
	public abstract JobReviewDAO getJobReviewService();
	public abstract LoginDAO getLoginService();
	public abstract AssetConfigurationDAO getAssetConfigurationService();
	public abstract LogbookDAO getLogbookService();
	  
/*
	  // List of DAO types supported by the factory
//	  public static final int SQLSERVER = 1;
//	  public static final int ORACLE = 2;
//	  public static final int POSTGRES = 3;
	 
	  

	  // There will be a method for each DAO that can be 
	  // created. The concrete factories will have to 
	  // implement these methods.
	  public abstract CommonUtilityDAO getCommonUtilityService();
	  public abstract DashboardDAO getDashBoardService();
	  public abstract JobAssignDAO getJobAssignService();
	  public abstract JobPerformDAO getJobPerformService();
	  public abstract JobReviewDAO getJobReviewService();
	  
	  public abstract LoginDAO getLoginService();
	  
	  public abstract TaskReviewDAO getTaskReviewService();
	  public abstract ActivityReviewDAO getActivityReviewService();
	  public abstract AssetConfigurationDAO getAssetConfigurationService();
	  public abstract ScanJobDAO getScanJobService();
	  public abstract LogbookReportsDAO getLogbookReportsService();
	  public abstract RestDAO getRestfulService();
	
*/
//	  public static DAOFactory getDAOFactory(
//	      int whichFactory) {
//	  
//	    switch (whichFactory) {
//	      case SQLSERVER: 
//	          return new SQLServerDAOFactory();
//	     // case ORACLE    : 
//	          //return new OracleDAOFactory(); 
//	      case POSTGRES:
//	      		return new PostgresDAOFactory();
//	     
//	      default           : 
//	          return null;
//	    }
//	  }
	

	
	}
