package in.co.greenwave.dashboardapi.dao.factory;

import in.co.greenwave.dashboardapi.dao.DashboardDAO;
import in.co.greenwave.dashboardapi.dao.DynamicDatasourceDAO;

public abstract class DAOFactory {
	public abstract DashboardDAO getDashBoardService();
	public abstract DynamicDatasourceDAO getDynamicDatasourceService();


	}


