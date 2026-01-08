package in.co.greenwave.dashboardapi.dao;

import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

public interface DynamicDatasourceDAO {
	
	Map<String, JdbcTemplate> getDynamicConnection(String tenantId);
}
