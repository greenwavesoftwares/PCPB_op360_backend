package in.co.greenwave.dao.factory; // Package containing the DAO factory classes

// Importing necessary DAO interfaces to be implemented
import in.co.greenwave.dao.LogbookConfigureService;
import in.co.greenwave.dao.LogbookReportService; // Importing service for logbook report handling
import in.co.greenwave.dao.LogbookService; // Importing service for logbook operations

// Concrete class that extends the abstract DAOFactory class for Postgres database implementation
public class PostgresDAOFactory extends DAOFactory {

	@Override
	public LogbookService getLogbookService() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LogbookReportService getLogbookReportService() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LogbookConfigureService getLogbookConfigureService() {
		// TODO Auto-generated method stub
		return null;
	}
}
