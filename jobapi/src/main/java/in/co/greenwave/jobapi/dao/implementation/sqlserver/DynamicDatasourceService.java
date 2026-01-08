//package in.co.greenwave.jobapi.dao.implementation.sqlserver;
//
//import java.util.HashMap;
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.Map;
//
//import javax.sql.DataSource;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.jdbc.DataSourceBuilder;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.stereotype.Repository;
//import in.co.greenwave.jobapi.dao.DynamicDatasourceDAO;
//import in.co.greenwave.jobapi.configuration.JdbcConfig;
//
//@Repository
//public class DynamicDatasourceService implements DynamicDatasourceDAO{
// 
//	@Autowired
//	@Qualifier("jdbcTemplate_OP360_tenant")
//	private  JdbcTemplate jdbcTemplateTenant;
//	
//	@Autowired
//	@Qualifier("jdbcTemplateCollection")
//	private Map<String, Map<String, JdbcTemplate>> jdbcTemplateCollection;
//	
//	
//	
//	@Override
//	public Map<String, JdbcTemplate> getDynamicConnection(String tenantId) {
//        try {
//        	Map<String, JdbcTemplate> jdbcTemplateMap=jdbcTemplateCollection.get(tenantId);
//            return jdbcTemplateMap;
// 
//        } catch (Exception e) {
//            throw new RuntimeException("Error executing query: " + e.getMessage(), e);
//        }
//    }			
//}
