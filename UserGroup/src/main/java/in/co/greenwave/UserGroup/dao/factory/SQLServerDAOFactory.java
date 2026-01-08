package in.co.greenwave.UserGroup.dao.factory;

import org.springframework.beans.factory.annotation.Autowired;

import in.co.greenwave.UserGroup.dao.JobAssignDAO;
import in.co.greenwave.UserGroup.dao.PageDetailsDAO;
import in.co.greenwave.UserGroup.dao.RestDAO;
/*
 * Retrieves SQL Server instances of the services
 * */
public class SQLServerDAOFactory extends DAOFactory {

	/**
	 * Autowired to the class where @Respository annotation is defined 
	 * to tell spring that here the database operations are happening 
	 */
	@Autowired
	private JobAssignDAO jobAssignDAO;

	@Autowired
	private RestDAO restDAO;
	
	
	
	@Autowired
	private PageDetailsDAO pageDetailsDAO;
	
	//Retrieves SQL server instance of JobAssignDAO
	@Override
	public JobAssignDAO getJobAssignDAOService() {
		// TODO Auto-generated method stub
		return jobAssignDAO;
	}

	//Retrieves SQL server instance of RestDAO
	@Override
	public RestDAO getRestfulService() {
		// TODO Auto-generated method stub
		return restDAO;
	}

	//Retrieves SQL server instance of RestDAO
	@Override
	public PageDetailsDAO getPageDetailsService() {
		// TODO Auto-generated method stub
		return pageDetailsDAO;
	}
	

}