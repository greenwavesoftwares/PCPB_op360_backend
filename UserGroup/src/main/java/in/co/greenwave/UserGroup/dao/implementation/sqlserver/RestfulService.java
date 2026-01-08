package in.co.greenwave.UserGroup.dao.implementation.sqlserver;

import java.awt.desktop.UserSessionListener;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import in.co.greenwave.UserGroup.dao.RestDAO;
import in.co.greenwave.UserGroup.model.JobDetails;
import in.co.greenwave.UserGroup.model.PlantModel;
import in.co.greenwave.UserGroup.model.TenantConfigDetails;
import in.co.greenwave.UserGroup.model.UserModel;
import in.co.greenwave.UserGroup.utility.JdbcUrlUtil;

//This class is responsible for interacting with the database for plant-related operations.
//It implements the RestDAO interface and is marked as a Spring Repository.
@Repository
public class RestfulService implements RestDAO {


	// Injecting JdbcTemplate for tenant-specific operations.
	@Autowired
	@Qualifier("jdbcTemplate_OP360_tenant")
	private  JdbcTemplate jdbcTemplateTenant;


	@Autowired
	@Qualifier("DatasourceCollections")
	Map<String, List<JdbcTemplate>> jdbcTemplates; // Template for operations on the main database


	// This method retrieves information about all plants from the database.
	@Override
	public List<PlantModel> getPlantInfo() {
		try {

			// SQL query to select plant details from the plant_details table.
			String readUserAndPlantJoinSql = "SELECT [plant_id] ,[plant_name] ,[address] ,[division] ,[customer_name] FROM [dbo].[plant_details]";
			// Execute the query and map the result set to a list of PlantModel objects.
			List<PlantModel> plants = jdbcTemplateTenant.query(readUserAndPlantJoinSql, (rs, rowNum) -> {
				PlantModel plantModel = new PlantModel();
				plantModel.setPlantName(rs.getString("plant_name"));  // Set plant name from the result set.
				plantModel.setPlantID(rs.getString("plant_id"));      // Set plant ID from the result set.
				plantModel.setAddress(rs.getString("address"));       // Set plant address from the result set.
				plantModel.setCustomerName(rs.getString("customer_name")); // Set customer name from the result set.
				plantModel.setDivision(rs.getString("division"));     // Set division from the result set.
				return plantModel;  // Return the constructed PlantModel object.
			});
			System.out.println("plants : " + plants); // Print the list of plants to the console.
			return plants; // Return the list of plants.
		}
		catch (Exception e) {
			// If there is an error during the database operation, return null.
			return null;
		}
	}

	// This method adds a new plant to the database.
	@Override
	public boolean addNewPlant(PlantModel plantModel) {
		System.out.println("addNewPlant : " + plantModel);  // Log the plant model being added.
		try {
			// SQL query to insert a new plant record into the plant_details table.
			String createPlantSql = "INSERT INTO [dbo].[plant_details] ([dateandtime], [plant_id], [plant_name], [address], [division], [customer_name]) VALUES (GETDATE(), ?, ?, ?, ?, ?)";
			// Execute the update and check if the plant was added successfully.
			int result = jdbcTemplateTenant.update(
					createPlantSql,
					plantModel.getPlantID(),      // Insert plant ID.
					plantModel.getPlantName(),     // Insert plant name.
					plantModel.getAddress(),       // Insert address.
					plantModel.getDivision(),      // Insert division.
					plantModel.getCustomerName()   // Insert customer name.
					);
			return result > 0;  // Return true if the insertion was successful.
		} catch (Exception e) {
			e.printStackTrace();  // Print the error stack trace for debugging.
			return false;  // Return false if there was an error.
		}
	}

	// This method retrieves details of a specific plant based on its ID.
	@Override
	public PlantModel getAnPlantInfo(String plantId) {
		PlantModel plant = new PlantModel();  // Create a new PlantModel object to hold the retrieved data.
		try {

			// SQL query to select a plant's details based on the plant ID.
			String readUserAndPlantJoinSql = "SELECT [plant_id] ,[plant_name] ,[address] ,[division] ,[customer_name] FROM [dbo].[plant_details] WHERE [plant_id] = ?";

			// Execute the query and get the plant details in a map.
			Map<String, Object> plantmap = jdbcTemplateTenant.queryForMap(readUserAndPlantJoinSql, plantId);

			// Retrieve values from the map and set them to the plant object.
			String plantName = (String) plantmap.get("plant_name");
			String plantID = (String) plantmap.get("plant_id");
			String address = (String) plantmap.get("address");
			String division = (String) plantmap.get("division");
			String customerName = (String) plantmap.get("customer_name");
			//			Date supportStartDate = (Date)plantmap.get("support_start_date");
			//			Date supportEndDate = (Date)plantmap.get("support_end_date");

			// Set the retrieved values to the PlantModel object.
			plant.setPlantName(plantName);
			plant.setPlantID(plantID);
			plant.setAddress(address);
			plant.setDivision(division);
			plant.setCustomerName(customerName);

			return plant;  // Return the populated PlantModel object.
		} catch (Exception e) {
			e.printStackTrace();  // Print the error stack trace for debugging.
			return null;  // Return null if there was an error.
		}
	}

	// This method deletes a plant from the database using its ID.
	@Override
	public boolean deletePlantByPlantId(String plantId) {
		System.out.println("plantId : " + plantId);  // Log the plant ID that is being deleted.
		try {
			// SQL query to delete a plant's record from the plant_details table.
			String deletePlantSql = "DELETE FROM [dbo].[plant_details] WHERE [plant_id] = ?";
			// Execute the update and store the number of rows affected.
			int rowsAffected = jdbcTemplateTenant.update(deletePlantSql, plantId);
			System.out.println("rowsAffected : " + rowsAffected);  // Log how many rows were affected by the delete operation.
			return rowsAffected > 0;  // Return true if at least one row was deleted.
		} catch (Exception e) {
			e.printStackTrace();  // Print the error stack trace for debugging.
			return false;  // Return false if there was an error.
		}
	}

	// This method finds a user by their tenant ID.
	@Override
	public UserModel findByTenantId(String tenantId) {

		// SQL query to get user details along with their associated plant details based on tenant ID.
		//		String readAnUserAndPlantJoinSql = "SELECT [TenantId], [AdminName], [Designation], [Email], [Password], [PhoneNumber], [AccountOwnerCustomer], [AccountOwnerGw], u.[PlantId], [plant_name], [division], [Homepage] FROM [dbo].[user_details] u JOIN [dbo].[plant_details] p ON u.PlantId = p.plant_id WHERE u.TenantId = ?";

		String readAnUserAndPlantJoinSql = "SELECT [TenantId] ,[AdminName] ,[Designation] ,[Email] ,[Password] ,[PhoneNumber] ,[AccountOwnerCustomer] ,[AccountOwnerGw] ,u.[PlantId] ,[plant_name] ,[address] ,[division] ,[Homepage] ,[PackageName] ,[db_name] ,[db_ip] ,[db_username] ,[db_password] ,[db_driver] FROM [dbo].[tenant_details] u JOIN [dbo].[plant_details] p ON u.PlantId = p.plant_id WHERE u.TenantId = ?";

		try {
			// Execute the query and map the results to a list of UserModel objects.
			List<UserModel> userList = jdbcTemplateTenant.query(readAnUserAndPlantJoinSql, ps -> ps.setString(1, tenantId), (rs, rowNum) -> {
				UserModel userModel = new UserModel();  // Create a new UserModel object to hold user details.
				userModel.setTenantId(rs.getString("TenantId"));  // Set tenant ID.
				userModel.setAdminName(rs.getString("AdminName"));  // Set admin name.
				userModel.setDesignation(rs.getString("Designation"));  // Set designation.
				userModel.setEmail(rs.getString("Email"));  // Set email.
				userModel.setPassword(rs.getString("Password"));  // Set password.
				userModel.setPhoneNumber(rs.getString("PhoneNumber"));  // Set phone number.
				userModel.setAccountOwnerCustomer(rs.getString("AccountOwnerCustomer")); // Set account owner customer.
				userModel.setAccountOwnerGW(rs.getString("AccountOwnerGw")); // Set account owner GW.
				userModel.setPlantID(rs.getString("PlantId"));  // Set associated plant ID.
				userModel.setPlantName(rs.getString("plant_name"));  // Set plant name.
				userModel.setAddress(rs.getString("address"));  // Set plant address.
				userModel.setDivision(rs.getString("division"));  // Set plant division.
				userModel.setHomepage(rs.getString("Homepage")); // Set homepage URL.
				userModel.setPackageName(rs.getString("PackageName")); // Set package name.
				userModel.setDatabaseName(rs.getString("db_name")); // Set database name.
				userModel.setDatabaseIP(rs.getString("db_ip")); // Set database IP.
				userModel.setDatabaseUsername(rs.getString("db_username")); // Set database username.
				userModel.setDatabasePassword(rs.getString("db_password")); // Set database password.

				// Database driver will be used later.
				//				userModel.setDatabaseDriver(rs.getString("db_driver"));

				return userModel;  // Return the populated UserModel object.
			});

			// If the user list is not empty, return the first user found; otherwise, return null.
			if (!userList.isEmpty()) {
				return userList.get(0);
			} else {
				return null;
			}
		} catch (Exception e) {
			System.out.println("exception found!");
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public UserModel findByUserEmail(String email,String tenantId) {

		// SQL query to get user details along with their associated plant details based on user email.
		//		String readAnUserAndPlantJoinSql = "SELECT [TenantId], [AdminName], [Designation], [Email], [Password], [PhoneNumber], [AccountOwnerCustomer], [AccountOwnerGw], u.[PlantId], [plant_name], [address], [division], [Homepage] FROM [dbo].[user_details] u JOIN [dbo].[tenant_details] p ON u.PlantId = p.plant_id WHERE Email = ?";

		String readAnUserAndPlantJoinSql = "SELECT [TenantId] ,[AdminName] ,[Designation] ,[Email] ,[Password] ,[PhoneNumber] ,[AccountOwnerCustomer] ,[AccountOwnerGw] ,u.[PlantId] ,[plant_name] ,[address] ,[division] ,[Homepage] ,[PackageName] ,[db_name] ,[db_ip] ,[db_username] ,[db_password] ,[db_driver] FROM [dbo].[tenant_details] u JOIN [dbo].[plant_details] p ON u.PlantId = p.plant_id WHERE Email = ?";



		try {
			// Execute the query and map the results to a list of UserModel objects.
			List<UserModel> userList = jdbcTemplateTenant.query(readAnUserAndPlantJoinSql, ps -> ps.setString(1, email), (rs, rowNum) -> {
				UserModel userModel = new UserModel();  // Create a new UserModel object to hold user details.
				userModel.setTenantId(rs.getString("TenantId"));  // Set tenant ID.
				userModel.setAdminName(rs.getString("AdminName"));  // Set admin name.
				userModel.setDesignation(rs.getString("Designation"));  // Set designation.
				userModel.setEmail(rs.getString("Email"));  // Set email.
				userModel.setPassword(rs.getString("Password"));  // Set password.
				userModel.setPhoneNumber(rs.getString("PhoneNumber"));  // Set phone number.
				userModel.setAccountOwnerCustomer(rs.getString("AccountOwnerCustomer")); // Set account owner customer.
				userModel.setAccountOwnerGW(rs.getString("AccountOwnerGw")); // Set account owner GW.
				userModel.setPlantID(rs.getString("PlantId"));  // Set associated plant ID.
				userModel.setPlantName(rs.getString("plant_name"));  // Set plant name.
				userModel.setAddress(rs.getString("address"));  // Set plant address.
				userModel.setDivision(rs.getString("division"));  // Set plant division.
				userModel.setHomepage(rs.getString("Homepage")); // Set homepage URL.
				userModel.setPackageName(rs.getString("PackageName")); // Set package name.
				userModel.setDatabaseName(rs.getString("db_name")); // Set database name.
				userModel.setDatabaseIP(rs.getString("db_ip")); // Set database IP.
				userModel.setDatabaseUsername(rs.getString("db_username")); // Set database username.
				userModel.setDatabasePassword(rs.getString("db_password")); // Set database password.

				// Database driver will be used later.
				//				userModel.setDatabaseDriver(rs.getString("db_driver"));
				return userModel;  // Return the populated UserModel object.
			});

			// If the user list is not empty, return the first user found; otherwise, return null.
			if (!userList.isEmpty()) {
				return userList.get(0);
			} else {
				return null;
			}
		} catch (Exception e) {
			System.out.println("exception found!");
			e.printStackTrace();
			return null;
		}
	}

	private java.sql.Date parseDateForDB(String dateStr) {
		if (dateStr == null || dateStr.isEmpty()) return null;

		// Expecting format dd/MM/yyyy
		String[] parts = dateStr.split("/");
		int day = Integer.parseInt(parts[0]);
		int month = Integer.parseInt(parts[1]);
		int year = Integer.parseInt(parts[2]);

		LocalDate localDate = LocalDate.of(year, month, day);
		return java.sql.Date.valueOf(localDate);
	}


	// This method saves a new user or updates an existing user in the database.
	@Override
	public UserModel saveUser(UserModel userModel) {
		try {

			// Check if the plant exists and insert it if it does not.
			String checkPlantIdSql = "IF NOT EXISTS (SELECT 1 FROM [dbo].[plant_details] WHERE [plant_id] = ?) INSERT INTO [dbo].[plant_details] ([DateAndTime], [plant_id], [plant_name], [address], [division], [customer_name]) VALUES (GETDATE(), ?, ?, ?, ?, ?)";

			// Execute the query to check and insert the plant details.
			jdbcTemplateTenant.update(
					checkPlantIdSql,
					userModel.getPlantID(),     // Check if this plant ID exists.
					userModel.getPlantID(),     // Insert plant ID.
					userModel.getPlantName(),   // Insert plant name.
					userModel.getAddress(),     // Insert address.
					userModel.getDivision(),    // Insert division.
					userModel.getCustomerName() // Insert customer name.
					);

			// Insert user details into the user_details table.
			//			String createUserSql = "INSERT INTO [dbo].[user_details] ([DateAndTime], [TenantId] ,[AdminName] ,[Designation] ,[Email] ,[Password] ,[PhoneNumber] ,[PlantId] ,[AccountOwnerCustomer] ,[AccountOwnerGw] ,[Homepage] ,[PackageName]) VALUES (GETDATE(), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

			String createUserSql = "INSERT INTO [dbo].[tenant_details] ([dateandtime] ,[TenantId] ,[AdminName] ,[Designation] ,[Email] ,[Password] ,[PhoneNumber] ,[PlantId] ,[AccountOwnerCustomer] ,[AccountOwnerGw] ,[Homepage] ,[PackageName] ,[db_name] ,[db_ip] ,[db_username] ,[db_password] ,[db_driver] ,[shorshe_ilish] ,[rosogolla] ,[cholar_dal] ,[mishti_doi] ,[luchi_alurdom]) VALUES (GETDATE() ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,? ,?)";

			// Execute the query to insert user details.
			jdbcTemplateTenant.update(
					createUserSql,
					userModel.getTenantId(),          // Tenant ID.
					userModel.getAdminName(),         // Admin name.
					userModel.getDesignation(),       // Designation.
					userModel.getEmail(),             // Email address.
					userModel.getPassword(),          // Password.
					userModel.getPhoneNumber(),       // Phone number.
					userModel.getPlantID(),           // Plant ID (foreign key to plant details).
					userModel.getAccountOwnerCustomer(), // Account owner (customer).
					userModel.getAccountOwnerGW(),    // Account owner (GW).
					userModel.getHomepage(),          // Homepage URL.
					userModel.getPackageName(),       // Package name.

					userModel.getDatabaseName(),      // Database name.
					userModel.getDatabaseIP(),        // Database IP address.
					userModel.getDatabaseUsername(),  // Database username.
					userModel.getDatabasePassword(),  // Database password.

					//					// Database Driver will be use later. Inserting null for now
					userModel.getDatabaseDriver(),

					userModel.getNoOfUsers(),
					userModel.getPoNo(),
					userModel.getStartDate(),
					userModel.getExpiryDate(),
					userModel.getGracePeriod()
					);


			String createUserSqlForUserCredentialTable = "insert into dbo.UserCredential ( [UserId] ,[UserName] ,[Password] ,[PhoneNumber] ,[G360Homepage] ,[WorkflowHomepage] ,[G360Admin] ,[Active] ,[CreatedOn] ,[ModifiedOn] ,[CreatedBy] ,[ModifiedBy] ,[Department] ,[FirstLoginRequired] ,[PasswordExpiryDuraton] ,[PasswordExpiryDate] ,[LastLogin],[TenantId]) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";

			// Return the saved user model after successful operation.

			System.out.println("userModel ::: " + userModel);

			return userModel;
		} catch (Exception e) {
			System.out.println("exception found!");
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Map<String, String> checkTenantExpiration(String tenant) {
	    String sql = "SELECT [mishti_doi], [luchi_alurdom] FROM [dbo].[tenant_details] WHERE TenantId = ?;";

	    System.out.println("tenant :: " + tenant);

	    Map<String, String> result = new HashMap<>();

	    try {
	        Map<String, Object> plantmap = jdbcTemplateTenant.queryForMap(sql, tenant);

	        String expiryDate = (plantmap.get("mishti_doi") != null) ? (String) plantmap.get("mishti_doi") : "";
	        String gracePeriod = (plantmap.get("luchi_alurdom") != null) ? (String) plantmap.get("luchi_alurdom") : "";

	        result.put("expiryDate", expiryDate);
	        result.put("gracePeriod", gracePeriod);
	        return result;
	    } catch (EmptyResultDataAccessException e) {
	        System.out.println("No tenant found for id :: " + tenant);
	        return null; // or return Map.of("expiryDate", "", "gracePeriod", "")
	    } catch (Exception e) {
	        System.out.println("exception found!");
	        e.printStackTrace();
	        return null;
	    }
	}


	@Override
	public List<JobDetails> getPublishedTasksWithJobId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void registerNewTenant(String tenantid) {
		List<JdbcTemplate> jdbcTenantList = new CopyOnWriteArrayList<>();
		String sql = "SELECT [db_name], [db_ip], [db_username], [db_password], [db_driver], [TenantId] FROM [tenant_details] where [TenantId]=?";

		TenantConfigDetails tenantDetail = jdbcTemplateTenant.queryForObject(
				sql,
				(rs, rowNum) -> new TenantConfigDetails(
						rs.getString("TenantId"),
						rs.getString("db_ip"),
						rs.getString("db_name"),
						rs.getString("db_password"),
						rs.getString("db_driver"),
						rs.getString("db_username")
						),
				tenantid
				);
		String[] dbNames = tenantDetail.getDbName().split(",");
		for (String dbName : dbNames) {
			String url = JdbcUrlUtil.buildJdbcUrl(tenantDetail.getDriver(), tenantDetail.getDbIp(), dbName);

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

		jdbcTemplates.put(tenantid, jdbcTenantList);

		System.out.println("Registered Tenant:"+jdbcTemplates.hashCode());
		System.err.println("Jdbctemplate toString:"+jdbcTemplates.toString());
	}
}
