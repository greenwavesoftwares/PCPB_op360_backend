package in.co.greenwave.mail.configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
/**
 * Here Two DataSource have been configured :
 *           db1 --> OP360 
 *           db2 --> OP360_Usermodule
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
	@ConfigurationProperties(prefix = "datasource.user")
	public DataSource dataSource2() {

		return  DataSourceBuilder.create().build();

	}

	@Bean(name = "jdbcTemplate_OP360_Usermodule")
	public JdbcTemplate jdbcTemplate2(@Qualifier("OP360usermodule") DataSource ds) {

		return new JdbcTemplate(ds);

	}

}

