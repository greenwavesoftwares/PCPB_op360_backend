package in.co.greenwave.assetapi.configuration;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import in.co.greenwave.assetapi.model.TenantConfigDetails;
import in.co.greenwave.assetapi.utility.JdbcUrlUtil;


@Configuration
public class DynamicDatasourceConfig {
	@Autowired
	@Qualifier("jdbcTemplate_OP360_tenant")
	private JdbcTemplate jdbctemplate;

	@Bean(name = "DatasourceCollections")
	public Map<String, List<JdbcTemplate>> dynamicOp360DataSource() {
		String sql = "SELECT [db_name], [db_ip], [db_username], [db_password], [db_driver], [TenantId] FROM [tenant_details]";

		List<TenantConfigDetails> tenantDetails = jdbctemplate.query(
				sql,
				(rs, rowNum) -> new TenantConfigDetails(
						rs.getString("TenantId"),
						rs.getString("db_ip"),
						rs.getString("db_name"),
						rs.getString("db_password"),
						rs.getString("db_driver"),
						rs.getString("db_username")
						)
				);

		Map<String, List<JdbcTemplate>> jdbcTemplateCollections = new ConcurrentHashMap<String, List<JdbcTemplate>>();
		for (TenantConfigDetails tenantDetail : tenantDetails) {
			List<JdbcTemplate> jdbcTenantList = new CopyOnWriteArrayList<>();
			String[] dbNames = tenantDetail.getDbName().split(",");
			for (String dbName : dbNames) {


				dbName = dbName.trim(); // Clean whitespace
				String url = JdbcUrlUtil.buildJdbcUrl(tenantDetail.getDriver(), tenantDetail.getDbIp(), dbName);

//				DataSource ds = DataSourceBuilder.create()
//						.driverClassName(config.getDriver())
//						.url(url)
//						.username(config.getDbUser())
//						.password(config.getDbPassword())
//						.build();
				
				HikariConfig config = new HikariConfig();
				config.setJdbcUrl(url);
				config.setUsername(tenantDetail.getDbUser());
	            config.setPassword(tenantDetail.getDbPassword());
	            config.setDriverClassName(tenantDetail.getDriver());
	            config.setMaximumPoolSize(20);
	            config.setMinimumIdle(15);
	            config.setIdleTimeout(30000);         // 30 seconds
	            config.setMaxLifetime(1800000);       // 30 minutes
	            config.setConnectionTimeout(30000);   // 30 seconds
	            config.setPoolName("HikariPool-" + tenantDetail.getTenantId());
				HikariDataSource hikariDataSource=new HikariDataSource(config);
				

				JdbcTemplate jdbcTemplate=new JdbcTemplate(hikariDataSource);
				jdbcTenantList.add(jdbcTemplate);

			}

			jdbcTemplateCollections.put(tenantDetail.getTenantId(), jdbcTenantList);
		}
		System.out.println("Default connections:"+jdbcTemplateCollections.hashCode());
//		showAllDataSourcePools(dataSources);
		return jdbcTemplateCollections;
	}
	@Bean("showAllDataSourcePools")
	public String showAllDataSourcePools(@Qualifier("DatasourceCollections") Map<String,List<JdbcTemplate>> jdbcTemplates,@Qualifier("jdbcTemplate_OP360_tenant") JdbcTemplate jdbctemplateTenant) {
		try {
			if (jdbcTemplates.isEmpty()) {
				System.out.println("No data sources available.");
				return "No data sources available.";
			}
			System.out.println("Listing all DataSource pools:");
			for (Entry<String, List<JdbcTemplate>> entry : jdbcTemplates.entrySet()) {
				String tenantId = entry.getKey();
				entry.getValue().forEach((jdbcTemplate)->{
					HikariDataSource ds=(HikariDataSource) jdbcTemplate.getDataSource();
					System.out.println("Tenant ID: " + tenantId);
					System.out.println("Pool Name: " + ds.getPoolName());
					System.out.println("JDBC URL: " + ds.getJdbcUrl());
					System.out.println("Maximum Pool Size: " + ds.getMaximumPoolSize());
					System.out.println("Minimum Idle Connections: " + ds.getMinimumIdle());
					System.out.println("Current Pool Size: " + ds.getHikariPoolMXBean().getTotalConnections());
					System.out.println("Idle Connections: " + ds.getHikariPoolMXBean().getIdleConnections());
					System.out.println("Active Connections: " + ds.getHikariPoolMXBean().getActiveConnections());
					System.out.println("Threads Awaiting Connection: " + ds.getHikariPoolMXBean().getThreadsAwaitingConnection());
					System.out.println("------------------------------------");
				
				});
			}
			HikariDataSource ds=(HikariDataSource) jdbctemplateTenant.getDataSource();
			System.out.println("JdbctemplateTenant");
			System.out.println("Pool Name: " + ds.getPoolName());
			System.out.println("JDBC URL: " + ds.getJdbcUrl());
			System.out.println("Maximum Pool Size: " + ds.getMaximumPoolSize());
			System.out.println("Minimum Idle Connections: " + ds.getMinimumIdle());
			System.out.println("Current Pool Size: " + ds.getHikariPoolMXBean().getTotalConnections());
			System.out.println("Idle Connections: " + ds.getHikariPoolMXBean().getIdleConnections());
			System.out.println("Active Connections: " + ds.getHikariPoolMXBean().getActiveConnections());
			System.out.println("Threads Awaiting Connection: " + ds.getHikariPoolMXBean().getThreadsAwaitingConnection());
			System.out.println("------------------------------------");
			return "Showed Connections";
		} catch (Exception e) {
			System.err.println("Error displaying DataSource pools: " + e.getMessage());
			return "Not Showed Connections";
		}
	}

}
