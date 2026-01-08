package in.co.greenwave.dashboardapi.dao.factory;

// Importing required classes for dependency injection
import org.springframework.beans.factory.annotation.Autowired;
import in.co.greenwave.dashboardapi.dao.DashboardDAO;
import in.co.greenwave.dashboardapi.dao.DynamicDatasourceDAO;

// This class is a specific implementation of DAOFactory for SQL Server databases
public class SQLServerDAOFactory extends DAOFactory {

    // Autowiring DashboardDAO to inject its implementation
    @Autowired
    private DashboardDAO dashBoardService;

    // Autowiring DynamicDatasourceDAO to inject its implementation
    @Autowired
    private DynamicDatasourceDAO dynamicDatasource;
    
    @Override
    public DashboardDAO getDashBoardService() {
        // Returns the injected DashboardDAO implementation (dashBoardService)
        return dashBoardService;
    }

    @Override
    public DynamicDatasourceDAO getDynamicDatasourceService() {
        // Returns the injected DynamicDatasourceDAO implementation (dynamicDatasource)
        return dynamicDatasource;
    }
}
