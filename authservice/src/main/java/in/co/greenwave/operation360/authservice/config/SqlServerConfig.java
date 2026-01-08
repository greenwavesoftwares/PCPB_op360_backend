package in.co.greenwave.operation360.authservice.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration // This tells Spring that this class contains configurations for setting up our application.
public class SqlServerConfig {

	// This creates the first database connection (DataSource) and names it "OP360".
	@Bean(name = "OP360")
	@ConfigurationProperties(prefix = "spring.datasource") // We use the settings starting with "spring.datasource" from our configuration file.
	public DataSource dataSource1() {
		return DataSourceBuilder.create().build(); // Builds the DataSource for "OP360" using the configuration properties.
	}

	// This sets up a JdbcTemplate for "OP360" to run SQL queries. JdbcTemplate helps us talk to the database easily.
	@Bean(name = "jdbcTemplate1")
	public JdbcTemplate jdbcTemplate1(@Qualifier("OP360") DataSource ds) { // Tells Spring to use the DataSource "OP360".
		return new JdbcTemplate(ds); // Creates a JdbcTemplate to work with the "OP360" database.
	}

	// This creates the second database connection (DataSource) and names it "OP360usermodule".
	@Bean(name = "OP360usermodule")
	@ConfigurationProperties(prefix = "spring.datasourceuser") // We use the settings starting with "datasource.user" from our configuration file.
	public DataSource dataSource2() {
		return DataSourceBuilder.create().build(); // Builds the DataSource for "OP360usermodule" using the configuration properties.
	}

	// This sets up a JdbcTemplate for "OP360usermodule" to run SQL queries.
	@Bean(name = "jdbcTemplate2")
	public JdbcTemplate jdbcTemplate2(@Qualifier("OP360usermodule") DataSource ds) { // Tells Spring to use the DataSource "OP360usermodule".
		return new JdbcTemplate(ds); // Creates a JdbcTemplate to work with the "OP360usermodule" database.
	}

	// This creates the third database connection (DataSource) and names it "OP360_Master_Tenant", which is for a tenant system.
	@Bean(name = "OP360_Master_Tenant")
	@ConfigurationProperties(prefix = "spring.datasourcetenant") // We use the settings starting with "datasource.tenant" from our configuration file.
	public DataSource tenantDataSource() {
		return DataSourceBuilder.create().build(); // Builds the DataSource for "OP360_Master_Tenant" using the configuration properties.
	}

	// This sets up a JdbcTemplate for "OP360_Master_Tenant" (tenant database) to run SQL queries.
	@Bean(name = "jdbcTemplate_OP360_tenant")
	public JdbcTemplate jdbcTemplateForTenant(@Qualifier("OP360_Master_Tenant") DataSource ds) { // Tells Spring to use the DataSource "OP360_Master_Tenant".
		return new JdbcTemplate(ds); // Creates a JdbcTemplate to work with the "OP360_Master_Tenant" tenant database.
	}
}