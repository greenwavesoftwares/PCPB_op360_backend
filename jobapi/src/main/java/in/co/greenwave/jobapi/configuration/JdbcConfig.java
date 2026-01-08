package in.co.greenwave.jobapi.configuration;

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
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
/**
 * Here Two DataSource have been configured :
 *           OP360 --> OP360 
 *           OP360usermodule --> OP360_Usermodule
 * Reference : application.properties          
 */
@Configuration
public class JdbcConfig
{

	@Bean(name = "OP360")
	@ConfigurationProperties(prefix = "spring.datasource")

	public DataSource dataSource1() {

		return DataSourceBuilder.create().build();

	}

	@Bean(name = "jdbcTemplate_OP360")
	public JdbcTemplate jdbcTemplate1(@Qualifier("OP360") DataSource ds) {
	    return new JdbcTemplate(ds);
	}

	@Bean(name = "OP360usermodule")
	@ConfigurationProperties(prefix = "spring.datasourceuser")
	public DataSource dataSource2() {

		return  DataSourceBuilder.create().build();

	}

	@Bean(name = "jdbcTemplate_OP360_Usermodule")
	public JdbcTemplate jdbcTemplate2(@Qualifier("OP360usermodule") DataSource ds) {

		return new JdbcTemplate(ds);

	}
	@Bean(name = "OP360tenant")
	@ConfigurationProperties(prefix = "spring.datasourcetenant")
	public DataSource dataSource3() {

		return  DataSourceBuilder.create().build();

	}

	@Bean(name = "jdbcTemplate_OP360_tenant")
	public JdbcTemplate jdbcTemplate3(@Qualifier("OP360tenant") DataSource ds) {

		return new JdbcTemplate(ds);

	}
	@Bean(name="platformtransactionmanager")
    public PlatformTransactionManager transactionManager(@Qualifier("OP360tenant")DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
	
	
}

