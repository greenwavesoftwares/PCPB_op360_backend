package in.co.greenwave.UserGroup.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

// Importing necessary classes for DAO (Data Access Objects), Models, and Utilities
import in.co.greenwave.UserGroup.dao.PageDetailsDAO;
import in.co.greenwave.UserGroup.dao.RestDAO;
import in.co.greenwave.UserGroup.model.UserModel;
import in.co.greenwave.UserGroup.utility.JwtService;
import in.co.greenwave.UserGroup.utility.UtilClass;

// This is the UserController class that handles user-related HTTP requests.
// It is marked with @RestController to define it as a REST API controller.
// It listens to requests under the "/users" URL.
@RestController
@RequestMapping("/users")
public class UserController {

	// Autowiring necessary components for dependency injection.
	// These components will be automatically instantiated by Spring.
	@Autowired
	private RestDAO restDAO; // Used for accessing user-related database operations.

	@Autowired
	private AuthenticationManager authenticationManager; // Handles user authentication.

	@Autowired
	private PasswordEncoder passwordEncoder; // Used to encode user passwords securely.

	@Autowired
	private PageDetailsDAO pageDetailsDAO; // Handles page-related database operations.

	// Static constant for adding extra security (salt) when hashing passwords.
	public static final String SALT = "Pr!mef@ce$";

	// This method handles GET requests to fetch a user's information by their tenant ID.
	// @PathVariable tells Spring to extract the tenantId from the URL.
	@GetMapping("/user/{tenantId}")
	public ResponseEntity<UserModel> getUserByTenantId(@PathVariable String tenantId) throws Exception {

		// Retrieves user info based on the tenantId using the DAO.
		UserModel user = restDAO.findByTenantId(tenantId);
		if (user != null) {
			// If the user exists, return the user info with an HTTP 200 (OK) status.
			return new ResponseEntity<>(user, HttpStatus.OK);
		} else {
			// If the user doesn't exist, return an HTTP 404 (Not Found) status.
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	// This method handles POST requests for creating a new user.
	// The user details are sent in the body of the request (@RequestBody).
	@PostMapping("/user/signup")
	public ResponseEntity<String> createUserHandler(@RequestBody UserModel user) throws Exception {
		// Extracts all relevant details from the UserModel object.
		String tenantId = user.getTenantId();
		String email = user.getEmail();
		String password = user.getPassword();
		System.out.println("tenantId : " + tenantId);
		System.out.println("email : " + email);

		UserModel savedUser = null;

		// Check if a user with the given tenantId already exists.
		UserModel isUserIdExist = restDAO.findByTenantId(tenantId);
		System.out.println("isUserIdExist : " + isUserIdExist);

		// Check if the email is already associated with a user in the same tenant.
		UserModel isUserEmailExist = restDAO.findByUserEmail(email, tenantId);
		System.out.println("isUserIdExist : " + isUserIdExist);

		// If a user with the same tenantId exists, return an error response.
		if (isUserIdExist != null) {
			return new ResponseEntity<>("UserID is already used with another account.", HttpStatus.BAD_REQUEST) ;
		}

		// If the email is already in use, return an error response.
		if (isUserEmailExist != null) {
			return new ResponseEntity<>("Email is already used with another account.", HttpStatus.BAD_REQUEST);
		}

		// Create a new user by copying the details from the input.
		UserModel createdUser = new UserModel();
		createdUser.setTenantId(tenantId);
		createdUser.setAdminName(user.getAdminName());
		createdUser.setDesignation(user.getDesignation());
		createdUser.setEmail(email);

		// Hash and store the user's password using a utility class for extra security.
		createdUser.setPassword(UtilClass.generateHash(SALT + password));

		createdUser.setPhoneNumber(user.getPhoneNumber());
		createdUser.setAccountOwnerCustomer(user.getAccountOwnerCustomer());
		createdUser.setAccountOwnerGW(user.getAccountOwnerGW());
		createdUser.setPlantID(user.getPlantID());
		createdUser.setPlantName(user.getPlantName());
		createdUser.setAddress(user.getAddress());
		createdUser.setDivision(user.getDivision());
		//		createdUser.setHomepage(user.getHomepage());
		createdUser.setPackageName(user.getPackageName());
		createdUser.setDatabaseName(user.getDatabaseName()); // Set database name.
		createdUser.setDatabaseIP(user.getDatabaseIP()); // Set database IP.
		createdUser.setDatabaseUsername(user.getDatabaseUsername()); // Set database username.
		createdUser.setDatabasePassword(user.getDatabasePassword()); // Set database password.
		createdUser.setDatabaseDriver(user.getDatabaseDriver());

		createdUser.setNoOfUsers(encrypt(user.getNoOfUsers()));
		createdUser.setPoNo(encrypt(user.getPoNo()));
		createdUser.setStartDate(encrypt(user.getStartDate()));
		createdUser.setExpiryDate(encrypt(user.getExpiryDate()));
		createdUser.setGracePeriod(encrypt(user.getGracePeriod()));

		if(checkDatabaseConnection(user.getDatabaseIP(),user.getDatabaseName(),user.getDatabaseUsername(),user.getDatabasePassword(),user.getDatabaseDriver())) {
			// Save the new user in the database.
			savedUser = restDAO.saveUser(createdUser);
		}
		else {
			// If there was an issue saving the user, return an error response.
			return new ResponseEntity<>("Database Credential error", HttpStatus.INTERNAL_SERVER_ERROR);
		}

		// If the user was successfully saved, return a success response.
		if (savedUser != null) {
			return new ResponseEntity<>("User created successfully.", HttpStatus.CREATED);
		}

		// If there was an issue saving the user, return an error response.
		return new ResponseEntity<>("Could not create user.", HttpStatus.INTERNAL_SERVER_ERROR);
	}

	// This method handles GET requests to fetch pages assigned to a user.
	// It takes the user's ID and tenant ID from the URL path.
	@GetMapping("/getAllotedPagesByUserIdTenantId/{userid}/{tenantid}")
	public ResponseEntity<Object> getAllotedPages(@PathVariable String userid, @PathVariable String tenantid) {
		try {
			// Get the list of pages allotted to the user based on their ID and tenant ID.
			List<String> allotedPages = pageDetailsDAO.getAllotedPages(userid, tenantid);
			// Return the list of pages with an HTTP 200 (OK) status.
			return new ResponseEntity<Object>(allotedPages, HttpStatus.OK);
		} catch (Exception e) {
			// If an error occurs, print the stack trace and return an error response.
			e.printStackTrace();
			return new ResponseEntity<Object>("Error occurred in getAllotedPages(): " + e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@PostMapping("/register-new-tenant/{tenantid}")
	public ResponseEntity<String> registernewtenant(@PathVariable("tenantid") String tenantid) {
		try {

			restDAO.registerNewTenant(tenantid);
			return ResponseEntity.ok().body("Tenant registered successfully");
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body("Tenant registration unsuccessful");
		}
	}

	@GetMapping("/checkTenantExpiration/{tenantid}")
	public ResponseEntity<?> checkTenantExpiration(@PathVariable String tenantid) {
		Map<String, String> result = new HashMap<>();
		try {
			// Get the list of pages allotted to the user based on their ID and tenant ID.
			Map<String, String> allotedPages = restDAO.checkTenantExpiration(tenantid);

			String expiryDate = decrypt(allotedPages.get("expiryDate"));
			String gracePeriod = decrypt(allotedPages.get("gracePeriod"));

			result.put("expiryDate", expiryDate);
			result.put("gracePeriod", gracePeriod);

			// Return the list of pages with an HTTP 200 (OK) status.
			return new ResponseEntity<Map<String, String>>(result, HttpStatus.OK);
		} catch (Exception e) {
			// If an error occurs, print the stack trace and return an error response.
			e.printStackTrace();
			return new ResponseEntity<>("Error occurred in getAllotedPages(): " + e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public static boolean checkDatabaseConnection(String dbIp, String dbName,
			String username, String password,
			String driverClassName) {
		Connection connection = null;

		// Determine the default port and JDBC URL template based on driver
		String url;

		if (driverClassName.contains("mysql")) {
			url = "jdbc:mysql://" + dbIp + ":3306/" + dbName + "?useSSL=false&serverTimezone=UTC";
		} else if (driverClassName.contains("postgresql")) {
			url = "jdbc:postgresql://" + dbIp + ":5432/" + dbName;
		} else if (driverClassName.contains("oracle")) {
			url = "jdbc:oracle:thin:@" + dbIp + ":1521:" + dbName;
		} else if (driverClassName.contains("sqlserver")) {
			url = "jdbc:sqlserver://" + dbIp + ":1433;databaseName=" + dbName + ";encrypt=true;trustServerCertificate=true;";  // For self-signed certificates or SSL issues
		} else {
			throw new IllegalArgumentException("Unsupported driver: " + driverClassName);
		}

		try {
			System.out.println("url :: " + url);
			System.out.println("urusernamel :: " + username);
			System.out.println("password :: " + password);
			Class.forName(driverClassName);
			connection = DriverManager.getConnection(url, username, password);
			return connection != null && !connection.isClosed();
		} catch (ClassNotFoundException e) {
			System.out.println("Driver class not found: " + e.getMessage());
		} catch (SQLException e) {
			System.out.println("Failed to connect to the database: " + e.getMessage());
		} finally {
			try {
				if (connection != null && !connection.isClosed()) {
					connection.close();
				}
			} catch (SQLException e) {
				System.out.println("Error closing the connection: " + e.getMessage());
			}
		}

		return false;
	}

	private static final String HEX_KEY = JwtService.SECRET;

	// Convert hex string to byte[]
	private static byte[] hexStringToByteArray(String s) {
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
					+ Character.digit(s.charAt(i+1), 16));
		}
		return data;
	}

	private static SecretKeySpec getKeySpec() {
		byte[] keyBytes = hexStringToByteArray(HEX_KEY);
		return new SecretKeySpec(keyBytes, "AES");
	}

	// Encrypt String or Integer
	public static String encrypt(Object input) throws Exception {
		String plainText = input.toString();
		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, getKeySpec());
		byte[] encrypted = cipher.doFinal(plainText.getBytes("UTF-8"));
		return Base64.getEncoder().encodeToString(encrypted);
	}

	// Decrypt back to String
	public static String decrypt(String encryptedText) throws Exception {
		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, getKeySpec());
		byte[] decoded = Base64.getDecoder().decode(encryptedText);
		byte[] decrypted = cipher.doFinal(decoded);
		return new String(decrypted, "UTF-8");
	}
}
