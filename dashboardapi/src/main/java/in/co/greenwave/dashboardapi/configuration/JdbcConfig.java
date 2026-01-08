package in.co.greenwave.dashboardapi.configuration;

// Importing required classes for DataSource and JdbcTemplate configuration
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Configuration class for setting up two DataSources:
 * db1 -> OP360 (Main Application Database)
 * db2 -> OP360_Usermodule (User Module Database)
 * Reference: Values for these DataSources are provided in the application.properties file.
 */
@Configuration  // Marks the class as a source of Spring-managed beans
public class JdbcConfig {

    @Bean(name = "db1")  // Defines a bean named "db1" for the first DataSource (OP360)
    @ConfigurationProperties(prefix = "spring.datasource")  // Maps properties prefixed with "spring.datasource" from the properties file to configure this DataSource
    public DataSource dataSource1() {
        // Creates and returns the DataSource for the OP360 database using DataSourceBuilder
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "jdbcTemplate_OP360")  // Defines a bean named "jdbcTemplate_OP360" for JdbcTemplate corresponding to the OP360 database
    public JdbcTemplate jdbcTemplate1(@Qualifier("db1") DataSource ds) {
        // Injects the "db1" DataSource and creates a JdbcTemplate instance for the OP360 database
        return new JdbcTemplate(ds);
    }

    @Bean(name = "db2")  // Defines a bean named "db2" for the second DataSource (OP360_Usermodule)
    @ConfigurationProperties(prefix = "datasource.user")  // Maps properties prefixed with "datasource.user" from the properties file to configure this DataSource
    public DataSource dataSource2() {
        // Creates and returns the DataSource for the OP360_Usermodule database using DataSourceBuilder
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "jdbcTemplate_OP360_Usermodule")  // Defines a bean named "jdbcTemplate_OP360_Usermodule" for JdbcTemplate corresponding to the OP360_Usermodule database
    public JdbcTemplate jdbcTemplate2(@Qualifier("db2") DataSource ds) {
        // Injects the "db2" DataSource and creates a JdbcTemplate instance for the OP360_Usermodule database
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

//	@Bean(name = "db3")
//	@ConfigurationProperties(prefix = "datasource.tenant")
// 
//	public DataSource tenantDataSource() {
// 
//		return DataSourceBuilder.create().build();
// 
//	}
//	
//	@Bean(name = "jdbcTemplate_OP360_tenant")
//	public JdbcTemplate jdbcTemplateForTenant(@Qualifier("db3") DataSource ds) {
// 
//		return new JdbcTemplate(ds);
// 
//	}
	

//	@Bean(name = "jdbcTemplateCollection")
//    public Map<String, Map<String, JdbcTemplate>> fetchJdbcTemplateCollection(@Qualifier("jdbcTemplate_OP360_tenant") JdbcTemplate jdbcTemplateTenant) {
//        try {
//            Map<String, Map<String, JdbcTemplate>> jdbcTemplateCollection = new LinkedHashMap<>();
//            String sql = "SELECT [db_name], [db_ip], [db_username], [db_password], [db_driver], [TenantId] FROM [tenant_details]";
//            List<Map<String, Object>> tenantDetailsList = jdbcTemplateTenant.queryForList(sql);
// 
//            if (tenantDetailsList.isEmpty()) {
//                throw new RuntimeException("No tenant details found.");
//            }
// 
//            for (Map<String, Object> tenantDetails : tenantDetailsList) {
//                // Getting the db_name which contains database names separated by commas
//                String dbNames = (String) tenantDetails.get("db_name");
//                String[] dbNameArray = dbNames.split(",");
// 
//                String dbDriver = (String) tenantDetails.get("db_driver");
//                String dbIp = (String) tenantDetails.get("db_ip");
//                String username = (String) tenantDetails.get("db_username");
//                String password = (String) tenantDetails.get("db_password");
//                String tenantId = (String) tenantDetails.get("TenantId");
// 
//                // Create a map to store the JdbcTemplates for this tenant
//                Map<String, JdbcTemplate> jdbcTemplateMap = new LinkedHashMap<>();
// 
//                for (int i = 0; i < dbNameArray.length; i++) {
//                    String dbUrl = "jdbc:sqlserver://" + dbIp + ";encrypt=true;trustServerCertificate=true;databaseName=" + dbNameArray[i];
//                    try {
//                        // Create a dynamic DataSource
//                        DataSource dynamicDataSource = createDataSource(dbUrl, username, password, dbDriver);
//                        JdbcTemplate jdbcTemplate = new JdbcTemplate(dynamicDataSource);
// 
//                        // Add JdbcTemplate to map
//                        jdbcTemplateMap.put("jdbcTemplate" + (i + 1), jdbcTemplate);
//                        if (i == 0) {
//                            jdbcTemplateMap.put("db_op360", jdbcTemplate);
//                        }
//                        if (i == 1) {
//                            jdbcTemplateMap.put("db_op360_usermodule", jdbcTemplate);
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        throw new RuntimeException("Error creating DataSource for database: " + dbNameArray[i], e);
//                    }
//                }
// 
//                jdbcTemplateCollection.put(tenantId, jdbcTemplateMap);
//            }
// 
//            return jdbcTemplateCollection;
// 
//        } catch (Exception e) {
//            throw new RuntimeException("Error executing query: " + e.getMessage(), e);
//        }
//    }
// 
//    private DataSource createDataSource(String url, String username, String password, String driverClassName) {
//        return DataSourceBuilder.create()
//                .url(url)
//                .username(username)
//                .password(password)
//                .driverClassName(driverClassName)
//                .build();
//    }
}

