package in.co.greenwave.jobapi.dao.factory;

import org.springframework.beans.factory.annotation.Autowired;
import in.co.greenwave.jobapi.dao.JobAssignDAO;
import in.co.greenwave.jobapi.dao.JobPerformDAO;
import in.co.greenwave.jobapi.dao.JobReviewDAO;
import in.co.greenwave.jobapi.dao.MinIOUploadDAO;
import in.co.greenwave.jobapi.dao.ScanJobDAO;

/*
 * Retrieves SQL Server instances of the services
 * */
public class SQLServerDAOFactory extends DAOFactory {
	@Autowired //Autowired with what ? To the class with which it implements where @Respository annotation is defined to tell spring that here the database operations are happening 
	private JobAssignDAO jobAssignService;
	@Autowired
	private JobPerformDAO jobPerformService;
	@Autowired
	private JobReviewDAO jobReviewService;
	@Autowired
	private ScanJobDAO scanJobService;
	@Autowired
	private MinIOUploadDAO miniodao;

	//Retrieves SQL server instance of JobAssignDAO
	@Override
	public JobAssignDAO getJobAssignService() {
		// TODO Auto-generated method stub
		return jobAssignService;
	}
 
	//Retrieves SQL server instance of JobPerformDAO
	@Override
	public JobPerformDAO getJobPerformService() {
		// TODO Auto-generated method stub
		return jobPerformService;
	}
 
	//Retrieves SQL server instance of JobReviewDAO
	@Override
	public JobReviewDAO getJobReviewService() {
		// TODO Auto-generated method stub
		return jobReviewService;
	}
 
	//Retrieves SQL server instance of ScanJobDAO
	@Override
	public ScanJobDAO getScanJobService() {
		// TODO Auto-generated method stub
		return scanJobService;
	}

	@Override
	public MinIOUploadDAO getMinIOUploadService() {
		return miniodao;
	}
 
}