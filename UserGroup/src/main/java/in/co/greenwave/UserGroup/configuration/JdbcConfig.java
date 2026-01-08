package in.co.greenwave.UserGroup.configuration;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.boot.context.properties.ConfigurationProperties;

import org.springframework.boot.jdbc.DataSourceBuilder;

import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;

import org.springframework.jdbc.core.JdbcTemplate;

/*Configures datasource based on application.properties*/
@Configuration

public class JdbcConfig {

	//This creates the database connection for OP360
	@Bean(name = "OP360")

	@ConfigurationProperties(prefix = "spring.datasource")//spring.datasource is used from our configuration file

	public DataSource dataSource1() {

		return DataSourceBuilder.create().build();//builds datasource using configuration properties

	}

	@Bean(name = "jdbcTemplate1")//Jdbc template to run sql queries

	public JdbcTemplate jdbcTemplate1(@Qualifier("OP360") DataSource ds) {//db1 is used as datasource 

		return new JdbcTemplate(ds);

	}
	//This creates the database connection for OP360usermodule
	@Bean(name = "OP360usermodule")

	@ConfigurationProperties(prefix = "datasource.user")//datasource.user is used from our configuration file

	public DataSource dataSource2() {

		return  DataSourceBuilder.create().build();//builds datasource using configuration properties


	}

	@Bean(name = "jdbcTemplate2")//Jdbc template to run sql queries


	public JdbcTemplate jdbcTemplate2(@Qualifier("OP360usermodule") DataSource ds) {//datasource.user is used from our configuration file

		return new JdbcTemplate(ds);

	}
	//This creates the database connection for OP360tenant
	@Bean(name = "OP360tenant")
	@ConfigurationProperties(prefix = "datasource.tenant")
 
	public DataSource tenantDataSource() {
 
		return DataSourceBuilder.create().build();
 
	}
	
	//Jdbc template to run sql queries
	@Bean(name = "jdbcTemplate_OP360_tenant")
	public JdbcTemplate jdbcTemplateForTenant(@Qualifier("OP360tenant") DataSource ds) {
 
		return new JdbcTemplate(ds);
 
	}


 
}

