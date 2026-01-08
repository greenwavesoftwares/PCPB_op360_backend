package in.co.greenwave.assetapi.dao.factory;

import org.springframework.beans.factory.annotation.Autowired;

import in.co.greenwave.assetapi.dao.AssetConfigurationDAO;
import in.co.greenwave.assetapi.dao.LogbookDAO;
import in.co.greenwave.assetapi.dao.RestDAO;

public class SQLServerDAOFactory extends DAOFactory {
	@Autowired
	private LogbookDAO logbookService;
	@Autowired
	private AssetConfigurationDAO assetConfigurationService;
	@Autowired
	private RestDAO restfulService;
	
	@Override
	public LogbookDAO getLogbookService() {
		// TODO Auto-generated method stub
		return logbookService;
	}
 
	@Override
	public AssetConfigurationDAO getAssetConfigurationService() {
		// TODO Auto-generated method stub
		return assetConfigurationService;
	}
 
	@Override
	public RestDAO getRestfulService() {
		// TODO Auto-generated method stub
		return restfulService;
	}
}