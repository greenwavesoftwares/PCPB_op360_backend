package in.co.greenwave.jobapi.dao.factory;




import in.co.greenwave.jobapi.dao.JobAssignDAO;
import in.co.greenwave.jobapi.dao.JobPerformDAO;
import in.co.greenwave.jobapi.dao.JobReviewDAO;
import in.co.greenwave.jobapi.dao.MinIOUploadDAO;
import in.co.greenwave.jobapi.dao.ScanJobDAO;

public abstract class DAOFactory {
	  // List of DAO types supported by the factory
		//Abstract Method to obtain an instance of JobAssignDAO
	  public abstract JobAssignDAO getJobAssignService();
	  //Abstract Method to obtain an instance of JobPerformDAO
	  public abstract JobPerformDAO getJobPerformService();
	  //Abstract Method to obtain an instance of JobReviewService
	  public abstract JobReviewDAO getJobReviewService();
	  //Abstract Method to obtain an instance of ScanJobDAO
	  public abstract ScanJobDAO getScanJobService();
	  public abstract MinIOUploadDAO getMinIOUploadService();
	}
