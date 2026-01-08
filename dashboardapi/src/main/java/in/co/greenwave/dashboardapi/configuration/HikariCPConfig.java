package in.co.greenwave.dashboardapi.configuration;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.annotation.PostConstruct;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
 
import javax.sql.DataSource;
import java.util.Map;
 
@Configuration
public class HikariCPConfig {
 
    private final ApplicationContext applicationContext;
 
    public HikariCPConfig(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
 
    @PostConstruct
    public void printAllHikariCPConfigs() {
        Map<String, DataSource> dataSources = applicationContext.getBeansOfType(DataSource.class);
 
        if (dataSources.isEmpty()) {
            System.out.println("No DataSource beans found.");
            return;
        }
 
        System.out.println("======= HikariCP Configurations =======");
        for (Map.Entry<String, DataSource> entry : dataSources.entrySet()) {
            String beanName = entry.getKey();
            DataSource dataSource = entry.getValue();
 
            if (dataSource instanceof HikariDataSource hikariDataSource) {
                System.out.println("DataSource Bean: " + beanName);
                System.out.println("JDBC URL: " + hikariDataSource.getJdbcUrl());
                System.out.println("Username: " + hikariDataSource.getUsername());
                System.out.println("Max Pool Size: " + hikariDataSource.getMaximumPoolSize());
                System.out.println("Minimum Idle: " + hikariDataSource.getMinimumIdle());
                System.out.println("Idle Timeout: " + hikariDataSource.getIdleTimeout());
                System.out.println("Connection Timeout: " + hikariDataSource.getConnectionTimeout());
                System.out.println("---------------------------------------");
            } else {
                System.out.println("DataSource Bean: " + beanName + " is not HikariCP.");
            }
        }
    }
}
