package in.co.greenwave.usermodule.dao.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import in.co.greenwave.usermodule.dao.UserModuleDAO;
 

 
 
public class SQLServerDAOFactory extends DAOFactory {
	
	@Autowired
	private UserModuleDAO userModuleService;

	@Override
	public UserModuleDAO getUserModuleService() {
		// TODO Auto-generated method stub
		return userModuleService;
	}

 
}