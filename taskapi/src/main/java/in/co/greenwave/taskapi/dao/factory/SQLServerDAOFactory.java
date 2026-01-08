package in.co.greenwave.taskapi.dao.factory;

import org.springframework.beans.factory.annotation.Autowired;

import in.co.greenwave.taskapi.dao.AssetConfigurationDAO;
import in.co.greenwave.taskapi.dao.JobAssignDAO;
import in.co.greenwave.taskapi.dao.JobReviewDAO;
import in.co.greenwave.taskapi.dao.LogbookDAO;
import in.co.greenwave.taskapi.dao.LoginDAO;
import in.co.greenwave.taskapi.dao.RestDAO;
import in.co.greenwave.taskapi.dao.TaskMasterDAO;

public class SQLServerDAOFactory extends DAOFactory {
	@Autowired
	TaskMasterDAO taskMasterService;
	@Autowired
	RestDAO restfulService;
	@Autowired
	JobAssignDAO jobAssignService;
	@Autowired
	LoginDAO loginService;
	@Autowired
	AssetConfigurationDAO assetConfigurationService;
	@Autowired
	LogbookDAO logbookService;
	@Autowired
	JobReviewDAO jobReviewService;
	
	@Override
	public TaskMasterDAO getTaskMasterService() {
		// TODO Auto-generated method stub
		return taskMasterService;
	}
	@Override
	public RestDAO getRestfulService() {
		// TODO Auto-generated method stub
		return restfulService;
	}
	@Override
	public JobAssignDAO getJobAssignService() {
		// TODO Auto-generated method stub
		return jobAssignService;
	}
	@Override
	public LoginDAO getLoginService() {
		// TODO Auto-generated method stub
		return loginService;
	}
	@Override
	public AssetConfigurationDAO getAssetConfigurationService() {
		// TODO Auto-generated method stub
		return assetConfigurationService;
	}
	@Override
	public LogbookDAO getLogbookService() {
		// TODO Auto-generated method stub
		return logbookService;
	}
	@Override
	public JobReviewDAO getJobReviewService() {
		// TODO Auto-generated method stub
		return jobReviewService;
	}
	
	
}
