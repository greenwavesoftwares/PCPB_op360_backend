package in.co.greenwave.jobapi.dao.factory;

import in.co.greenwave.jobapi.dao.JobAssignDAO;
import in.co.greenwave.jobapi.dao.JobPerformDAO;
import in.co.greenwave.jobapi.dao.JobReviewDAO;
import in.co.greenwave.jobapi.dao.MinIOUploadDAO;
import in.co.greenwave.jobapi.dao.ScanJobDAO;


/*
 * Retrieves instances of Postgres services.
 * Currently not used
 * */
public class PostgresDAOFactory extends DAOFactory {


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
	public ScanJobDAO getScanJobService() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MinIOUploadDAO getMinIOUploadService() {
		// TODO Auto-generated method stub
		return null;
	}



}
