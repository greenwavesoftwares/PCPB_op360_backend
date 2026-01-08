package in.co.greenwave.operation360.authservice.repository.factory;

import org.springframework.beans.factory.annotation.Autowired;

import in.co.greenwave.operation360.authservice.repository.UserModuleRepositoryService;
import in.co.greenwave.operation360.authservice.repository.UserModuleService;

public class SQLServerDAOFactory extends DAOFactory {
	@Autowired
	private UserModuleService userModuleService;

    // Service to handle database repository actions related to the user module
    @Autowired
    private UserModuleRepositoryService userModuleRepositoryService;
	
	@Override
    public UserModuleService getuserModuleService() {
		return userModuleService;
	}

    // Service to handle database repository actions related to the user module
	@Override
    public UserModuleRepositoryService getuserModuleRepositoryService() {
    	return userModuleRepositoryService;
    }
	
}