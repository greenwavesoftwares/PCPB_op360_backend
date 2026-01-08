package in.co.greenwave.dao.factory;

import org.springframework.beans.factory.annotation.Autowired;

import in.co.greenwave.dao.LogbookConfigureService;
import in.co.greenwave.dao.LogbookReportService; // Importing service for logbook report handling
import in.co.greenwave.dao.LogbookService; // Importing service for logbook operations

public class SQLServerDAOFactory extends DAOFactory {

	@Autowired
	private LogbookService logbookService;
	@Autowired
	private LogbookReportService logbookReportService;
	@Autowired
	private LogbookConfigureService logbookConfigureService;
	
	@Override
	public LogbookService getLogbookService() {
		// TODO Auto-generated method stub
		return logbookService;
	}

	@Override
	public LogbookReportService getLogbookReportService() {
		// TODO Auto-generated method stub
		return logbookReportService;
	}

	@Override
	public LogbookConfigureService getLogbookConfigureService() {
		// TODO Auto-generated method stub
		return logbookConfigureService;
	}
	
}