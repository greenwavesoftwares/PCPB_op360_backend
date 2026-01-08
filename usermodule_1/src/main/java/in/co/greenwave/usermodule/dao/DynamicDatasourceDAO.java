package in.co.greenwave.usermodule.dao;

import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

public interface DynamicDatasourceDAO {
	
	String op360_key="db_op360";
	String op360_usermodule_key="db_op360_usermodule";
//	 Map<String, JdbcTemplate>  getDynamicConnection(String tenantId);
}