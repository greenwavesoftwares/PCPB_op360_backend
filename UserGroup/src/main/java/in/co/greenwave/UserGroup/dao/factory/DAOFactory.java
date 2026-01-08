package in.co.greenwave.UserGroup.dao.factory;

import in.co.greenwave.UserGroup.dao.JobAssignDAO;
import in.co.greenwave.UserGroup.dao.MenuInfoDAO;
import in.co.greenwave.UserGroup.dao.PageDetailsDAO;
import in.co.greenwave.UserGroup.dao.RestDAO;

public abstract class DAOFactory {
	//Abstract Method to obtain an instance of JobAssignDAO
	public abstract JobAssignDAO getJobAssignDAOService();
	//Abstract Method to obtain an instance of RestDAO
	public abstract RestDAO getRestfulService();
	//Abstract Method to obtain an instance of PageDetailsDAO
	public abstract PageDetailsDAO getPageDetailsService();
	
}