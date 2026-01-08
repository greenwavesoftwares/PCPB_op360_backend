package in.co.greenwave.taskapi.dao.implementation.sqlserver;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import in.co.greenwave.taskapi.dao.DynamicDatasourceDAO;


@Repository
public class DynamicDatasourceService implements DynamicDatasourceDAO{

	@Override
	public Map<String, JdbcTemplate> getDynamicConnection(String tenantId) {
		// TODO Auto-generated method stub
		return null;
	}

//	@Autowired
//	@Qualifier("jdbcTemplateCollection")
//	private Map<String, Map<String, JdbcTemplate>> jdbcTemplateCollection;
//	
//	@Override
//	public Map<String, JdbcTemplate> getDynamicConnection(String tenantId) {
//		try {
//        	Map<String, JdbcTemplate> jdbcTemplateMap=jdbcTemplateCollection.get(tenantId);
//            return jdbcTemplateMap;
//        } catch (Exception e) {
//            throw new RuntimeException("Error executing query: " + e.getMessage(), e);
//        }
//	}

			
}