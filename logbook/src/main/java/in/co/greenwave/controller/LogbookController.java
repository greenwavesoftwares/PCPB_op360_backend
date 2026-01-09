package in.co.greenwave.controller;

import java.io.InputStream;
import java.time.LocalDateTime; // Importing LocalDateTime for date and time handling
import java.util.ArrayList; // Importing ArrayList to use dynamic arrays
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List; // Importing List to define a list of items
import java.util.Map; // Importing Map to handle key-value pairs
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired; // Importing the Autowired annotation for dependency injection
import org.springframework.core.io.InputStreamResource; // Importing for handling input streams
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity; // Importing for HTTP responses
import org.springframework.web.bind.annotation.CrossOrigin; // Importing for enabling CORS (Cross-Origin Resource Sharing)
import org.springframework.web.bind.annotation.GetMapping; // Importing for handling GET requests
import org.springframework.web.bind.annotation.PathVariable; // Importing for handling path variables in URLs
import org.springframework.web.bind.annotation.PostMapping; // Importing for handling POST requests
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody; // Importing for handling request bodies
import org.springframework.web.bind.annotation.RequestMapping; // Importing for defining request mappings
import org.springframework.web.bind.annotation.RequestParam; // Importing for handling request parameters
import org.springframework.web.bind.annotation.RestController; // Importing for defining a REST controller
import org.springframework.web.multipart.MultipartFile;

import in.co.greenwave.dto.CellDetailsDto; // Importing Data Transfer Object for cell details
import in.co.greenwave.dto.CellDto; // Importing Data Transfer Object for cell
import in.co.greenwave.dto.FormDto; // Importing Data Transfer Object for form
import in.co.greenwave.dto.ReportConfigDto; // Importing Data Transfer Object for report configuration
import in.co.greenwave.dto.ReportData; // Importing Data Transfer Object for report data
import in.co.greenwave.dto.TransactionAttachmentDto;
import in.co.greenwave.dto.TransactionDto; // Importing Data Transfer Object for transaction
import in.co.greenwave.entity.CellEditHistory; // Importing entity for cell edit history
import in.co.greenwave.entity.DropDownInfo; // Importing entity for dropdown information
import in.co.greenwave.entity.FormInfo; // Importing entity for form information
import in.co.greenwave.entity.UserEntity; // Importing entity for user information
import in.co.greenwave.entity.UserModel; // Importing entity for user model
import in.co.greenwave.util.JwtService;
import in.co.greenwave.dao.LogbookConfigureService;
import in.co.greenwave.dao.LogbookReportService; // Importing service for logbook report handling
import in.co.greenwave.dao.LogbookService; // Importing service for logbook operations
import in.co.greenwave.dao.factory.DAOFactory;
import io.jsonwebtoken.io.IOException;
//import io.minio.GetObjectArgs;
//import io.minio.MinioClient;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


/**
 * This class is the controller that handles incoming requests related to logbook operations.
 * It acts as the first layer to receive requests and process them to the corresponding services.
 * 
 * The controller is annotated with @RestController, which means it handles RESTful web service requests.
 * It uses @RequestMapping to define the base URL for all logbook-related operations.
 * 
 * CORS is enabled for all origins to allow cross-origin requests.
 * 
 * @author Subhajit Khasnobish
 */
@RestController
@RequestMapping(value = "/logbook")
@CrossOrigin("*")
public class LogbookController {


	@Autowired // Automatically injects the DAOFactory object
	private DAOFactory factory;

	/*
	 * Reference of LogbookService 
	 */
	@Autowired
	private LogbookService logbookService;

	@Autowired
	private LogbookReportService logbookReportService;

	@Autowired
	private LogbookConfigureService logbookConfigureService;

	@Autowired // This connects our JwtService automatically
	private JwtService jwtService; // This is a service that helps us work with tokens

//	@Autowired
//	private MinioClient minioClient;
	
	//added by Ahsok
	
//	@GetMapping("/file")
//    public void getFileByKey(@RequestParam String minio_key,@RequestParam String tenantId, HttpServletResponse response) {
//        try {
//        	String bucket = tenantId.toLowerCase();
//            InputStream stream = minioClient.getObject(
//                GetObjectArgs.builder()
//                    .bucket(bucket)
//                    .object(minio_key)
//                    .build()
//            );
//
//            String contentType = null;
//
//            // Try automatic detection
//            try {
//                contentType = java.nio.file.Files.probeContentType(
//                    java.nio.file.Paths.get(minio_key)
//                );
//            } catch (Exception ignored) {}
//
//            // Special handling for .eml
//            if (contentType == null && minio_key.toLowerCase().endsWith(".eml")) {
//                contentType = "message/rfc822";
//            }
//
//            // Fallback for unknown types
//            if (contentType == null) {
//                contentType = "application/octet-stream";
//            }
//
//            response.setContentType(contentType);
//            response.setHeader(
//                "Content-Disposition",
//                "inline; filename=\"" + minio_key + "\""
//            );
//
//            try (stream; ServletOutputStream out = response.getOutputStream()) {
//                byte[] buffer = new byte[8192];
//                int bytesRead;
//                while ((bytesRead = stream.read(buffer)) != -1) {
//                    out.write(buffer, 0, bytesRead);
//                }
//                out.flush();
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//        }
//    }

	/**
	 * Uploads an Excel file and processes its contents to generate a map of sheet names 
	 * and their corresponding details.
	 * 
	 * @param file, The Excel file to be uploaded and processed.
	 * @return A Map containing sheet names as keys and lists of @FormDto as values or an Empty Map if ant issue occur while uploading the Excel file.
	 * @throws error If there is an issue reading the file.
	 */
	@PostMapping("/uploadExcelFile")
	public Map<String, List<FormDto>> uploadFile(@RequestBody MultipartFile file) throws IOException {
		System.out.println("ApiController.uploadFile()");

		logbookConfigureService = factory.getLogbookConfigureService(); // Get the LogbookConfigure service
		//		logbookService = factory.getLogbookService();

		Map<String,List<FormDto>> sheetFormMap = new LinkedHashMap<String,List<FormDto>>();
		if(file!=null)
			sheetFormMap = logbookConfigureService.readExcel(file);
		else
			System.err.println("File is null");

		return sheetFormMap;
	}

	//added by Ashok
	/**
	 * Saves a form's details for a specific tenant. Allows for saving new forms or updating 
	 * existing forms based on the `isUpdate` flag.
	 * 
	 * @param formInfo The @FormDto object containing form details.
	 * @param tenantId The ID of the tenant for whom the form is being saved.
	 * @param isUpdate A boolean flag indicating whether the operation is an update.
	 * @return A @ResponseEntity containing a success or error message and HTTP status.
	 */
	@PostMapping("/saveForm/{tenantId}")
	public ResponseEntity<Map<String, Object>> saveForm(@RequestBody FormDto formInfo, @PathVariable("tenantId") String tenantId,@RequestParam  boolean isUpdate) {

		Map<String, Object> response = new HashMap<>();
		try {
			//	        logbookService = factory.getLogbookService();
			
			logbookConfigureService = factory.getLogbookConfigureService(); // Get the LogbookConfigure service
			logbookConfigureService.saveForm(formInfo,tenantId,isUpdate);
			FormDto latestForm = logbookConfigureService.getLatestForm(tenantId);
			response.put("message", "Form saved and Latest form retrieved successfully");
			response.put("status", HttpStatus.OK.value());
			response.put("data", latestForm); // Send the FormDto here

			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.put("message", "Error saving form: " + e.getMessage());
			response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());

			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	//added by Ashok
	/**
	 * Updates the active status of a form for a specific tenant and also deactivates 
	 * other versions of the form based on the `isDeactivateOtherVersion` flag.
	 * 
	 * @param formInfo The @FormDto object containing form details.
	 * @param tenantId The ID of the tenant whose form status is being updated.
	 * @param isDeactivateOtherVersion A boolean flag to deactivate other form versions.
	 * @return A @ResponseEntity containing a success or error message and HTTP status.
	 */
	@PutMapping("/updateStatus/{tenantId}")
	public ResponseEntity<Map<String, Object>> updateActiveStatus(@RequestBody FormDto formInfo, @PathVariable("tenantId") String tenantId,@RequestParam  boolean isDeactivateOtherVersion) {

		Map<String, Object> response = new HashMap<>();
		try {
			logbookConfigureService = factory.getLogbookConfigureService(); // Get the LogbookConfigure service

			logbookConfigureService.updateActiveStatus(formInfo,tenantId,isDeactivateOtherVersion);

			response.put("message", "Form Updated successfully");
			response.put("status", HttpStatus.OK.value());

			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.put("message", "Error saving form: " + e.getMessage());
			response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());

			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}



	/**
	 * Fetches the details of a specific form by tenant and formname.
	 * 
	 * @param tenantId The ID of the tenant to which the form belongs.
	 * @param formName The name of the form to fetch details for.
	 * @return A @ResponseEntity containing the form details or an error message if the 
	 *         form is not found.
	 */
	@GetMapping("/formDetails/{tenantId}")
	public ResponseEntity<?> getFormDetailsByFormName(
			@PathVariable(name = "tenantId") String tenantId,
			@RequestParam String formName) {
		System.out.println("Form details API call");

		logbookConfigureService = factory.getLogbookConfigureService(); // Get the LogbookConfigure service

		FormDto form = logbookConfigureService.getFormDetailsByFormName(tenantId, formName);

		if (form == null) {
			// Return 404 with an error message
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(Map.of(
							"message", "Form not found",
							"formName", formName,
							"tenantId", tenantId,
							"status", HttpStatus.NOT_FOUND.value()
							));
		}

		// Return the form details with 200 OK
		return ResponseEntity.ok(form);
	}

	/**
	 * Gets all data of {@link FormInfo}
	 * 
	 * @return @ResponseEntity containing collection of @FormInfo.
	 */
	@GetMapping(value = "/forminfo/{tenantId}")
	public ResponseEntity<List<FormDto>> getAllFormInfo(@PathVariable(name = "tenantId") String tenantId) {
		logbookService = factory.getLogbookService(); // Get the logbook service
		return logbookService.getAllForms(tenantId);
	}

	/**
	 * Gets all active @FormInfo data
	 * 
	 * @param tenantId 
	 * 
	 * @return @ResponseEntity containing @java.util.List of @FormInfo which are
	 *         active.
	 */
	@GetMapping(value = "/activeforminfo/{tenantId}")
	public ResponseEntity<List<FormDto>> getAllActiveFormInfo(@PathVariable(name = "tenantId") String tenantId) {
		logbookService = factory.getLogbookService(); // Get the logbook service
		return logbookService.getAllActiveForms(tenantId);
	}

	/**
	 * 
	 * @param @java.util.List of @FormInfo
	 * 
	 * @return @java.util.List of saved @FormInfo data.
	 */
	@PostMapping(value = "forminfo/savelogbook/{tenantId}")
	public ResponseEntity<List<FormInfo>> saveLogbook(@RequestBody List<FormDto> forms, @PathVariable(name = "tenantId") String tenantId) {

		ArrayList<FormInfo> formList = new ArrayList<FormInfo>();
		logbookService = factory.getLogbookService(); // Get the logbook service
		/*
		 *  Makes an @FormInfo from each of the @FormDto and add to a @java.util.List
		 *  
		 */
		forms.forEach(e -> {

			FormInfo formInfo = new FormInfo(e);

			formList.add(formInfo);

		});
		
		return logbookService.saveLogbook(formList, tenantId);
	}

	/**
	 * executes the SQL queries recieved in the @param
	 * 	
	 * @param deleteQuery as @java.lang.String
	 * @param insertQuery as @java.lang.String
	 * 
	 * @return all @FormInfo data as @java.util.List
	 */
	@PostMapping(value = "forminfo/savelogbookdata/{tenantId}")
	public ResponseEntity<List<FormInfo>> saveLogbookData(@RequestParam(name = "deleteQuery") String deleteQuery,
			@RequestParam(name = "insertQuery") String insertQuery, @PathVariable(name = "tenantId") String tenantId) {
		logbookService = factory.getLogbookService(); // Get the logbook service
		return logbookService.saveLogbookData(deleteQuery, insertQuery, tenantId);
	}


	/**
	 * saves an instance of @Transaction and fetches the data from @TransactionDto
	 * 
	 * @param takes a transaction to save
	 * 
	 * @return after saving that transaction returns that itself
	 */
	@PostMapping(value = "forminfo/savetransaction")
	public ResponseEntity<TransactionDto> saveTransaction(@RequestBody TransactionDto transactionDto) {
		logbookService = factory.getLogbookService(); // Get the logbook service
		return logbookService.saveTransaction(transactionDto);

	}

	/**
	 * 
	 * @param formName as @java.lang.String of the form which is required here
	 * 
	 * @return a @java.util.List of formInfo by the given formName
	 */
	@GetMapping(value = "forminfo/{formName}/{tenantId}")
	public ResponseEntity<List<FormInfo>> getFormInfoByFormName(@PathVariable(value = "formName") String formName, @PathVariable(name = "tenantId") String tenantId) {
		logbookService = factory.getLogbookService(); // Get the logbook service
		return logbookService.getFormInfoByFormName(formName,tenantId);
	}

	/**
	 * 
	 * gets logbooks based on formName, versionNumber and tenantId
	 * 
	 * @param formName of the required formInfo
	 * @param versionNumber of the required formInfo
	 * @param tenantId
	 * @return @java.util.List of @CellDto for that form
	 */
	@GetMapping(value = "forminfo/{formName}/versions/{version}/{tenantId}")
	public ResponseEntity<List<CellDetailsDto>> getAllCellInfoByFormNameAndVersion(@PathVariable(value = "formName") String formName,
			@PathVariable(value = "version") Integer versionNumber,@PathVariable(name = "tenantId") String tenantId) {
		logbookService = factory.getLogbookService(); // Get the logbook service
		return logbookService.getAllCellInfoByFormNameAndVersion(formName, versionNumber,tenantId);
	}

	/**
	 * 
	 * 
	 * @param formName      of form which is required
	 * @param versionNumber of the form is required
	 * @param cellId        of the cell of the form of that formName of that version
	 * 
	 * @return @CellDto details wrapped in @ResponseEntity
	 */
	@GetMapping(value = "forminfo/{formName}/versions/{versions}/cellids/{cellId}/{tenantId}")
	public ResponseEntity<CellDetailsDto> getCellInfo(@PathVariable(value = "formName") String formName,
			@PathVariable(value = "versions") Integer versionNumber, @PathVariable(value = "cellId") String cellId, @PathVariable(name = "tenantId") String tenantId) {
		logbookService = factory.getLogbookService(); // Get the logbook service
		return logbookService.getCellInfo(formName, versionNumber, cellId, tenantId);
	}

	/**
	 * Dropdown specific API , executes the query and gets the DropDowns info
	 * 
	 * @param query
	 * @return @ResponseEntity of @java.util.List of @DropDownInfo
	 */
	@PostMapping(value = "forminfo/getdropdownforquery/{tenantId}")
	public ResponseEntity<List<DropDownInfo>> getDropDownsForQuery(@RequestBody String query, @PathVariable(name = "tenantId") String tenantId){

		logbookService = factory.getLogbookService(); // Get the logbook service
		return logbookService.getDropDownsForQuery(query, tenantId);
	}


	/**
	 * Executes the recieved query and returns the result as @java.lang.Object
	 * 
	 * @param query
	 * @return @java.lang.Object as result
	 */
	@PostMapping(value = "forminfo/getqueryresultforglobalcellupdate/{tenantId}")
	public ResponseEntity<Object> getQueryResultForGlobalCellUpdate(@RequestBody String query, @PathVariable(name = "tenantId") String tenantId){


		logbookService = factory.getLogbookService(); // Get the logbook service

		ResponseEntity<Object> queryResultForGlobalCellUpdate = logbookService.getQueryResultForGlobalCellUpdate(query, tenantId);

		return queryResultForGlobalCellUpdate;
	}

	/**
	 * Gets @java.util.List of @TransactionDto
	 * 
	 * @param jobId
	 * @param activityId
	 * @param formName
	 * @param formVersion
	 * @param tenantId
	 * @return @ResponseEntity of @java.util.List of @TransactionDto
	 */
	@GetMapping(value = "forminfo/getalltransactions/{tenantId}")
	public ResponseEntity<List<TransactionDto>> getAllTransactions(@RequestParam(value = "jobId") String jobId,@RequestParam(value = "activityId") String activityId,@RequestParam(value = "formName") String formName,@RequestParam(value = "formVersion") int formVersion,
			@PathVariable(name = "tenantId") String tenantId){

		logbookService = factory.getLogbookService(); // Get the logbook service

		ResponseEntity<List<TransactionDto>> allTransactions = logbookService.getAllTransactions(jobId, activityId, formName, formVersion,tenantId);

		return allTransactions;
	}


	/**
	 * fetches @java.util.List of @CellEditHistory
	 * 
	 * @param cellid
	 * @param jobid
	 * @param activityid
	 * @return @java.util.List of @CellEditHistory
	 */
	@GetMapping(value = "forminfo/getcelledithistory/{tenantId}")
	public ResponseEntity<List<CellEditHistory>> getCellHistory(@RequestParam(value="cellId") String cellid, @RequestParam(value="jobId") String jobid, @RequestParam(value="activityId") String activityid, @PathVariable(name = "tenantId") String tenantId) {

		logbookService = factory.getLogbookService(); // Get the logbook service
		return logbookService.getCellHistory(cellid, jobid, activityid, tenantId);
	}

	/**
	 * gets all the logbooks based on userId and tenantId
	 * 
	 * @param userId
	 * @param tenantId
	 * @return List of @java.util.List of Logbooks as @java.util.Map
	 */
	@GetMapping(value = "/getLogbooksByUserIdAndTenantId/{userId}/{tenantId}")
	public ResponseEntity<List<Map<String,Object>>> getlogbooksByUserIdAndTenantId(HttpServletRequest request, @PathVariable(value = "userId") String userId,
			@PathVariable(value = "tenantId") String tenantId){
		
		System.err.println("HttpServletRequest request : : " + request);
		String authHeader = request.getHeader("Authorization"); // We check for the "Authorization" header in the request
		String token = null; // This will hold our token
		String username = null; // This will hold the username
		if (authHeader != null && authHeader.startsWith("Bearer ")) { // If we found an Authorization header and it starts with "Bearer " (like a special label for our key)
			token = authHeader.substring(7); // We get the actual token by removing "Bearer " from the start
			username = jwtService.extractUsername(token); // We use our service to get the username from the token
		}
		
		logbookService = factory.getLogbookService(); // Get the logbook service
		return logbookService.getlogbooksByUserIdAndTenantId(userId, tenantId);
	}

	@PostMapping("/saveTransactionAttachment/{tenantId}")
	ResponseEntity<List<TransactionAttachmentDto>> saveTransactionAttachment(@PathVariable(value = "tenantId") String tenantId, @RequestBody List<TransactionAttachmentDto> transactionAttachmentDto){
		logbookReportService = factory.getLogbookReportService(); // Get the logbookReport service
		ResponseEntity<List<TransactionAttachmentDto>> transactionAttachment = logbookReportService.saveTransactionAttachment(tenantId, transactionAttachmentDto);
		return transactionAttachment;
	}

	@GetMapping("/getTransactionAttachment/{transactionId}/{tenantId}")
	ResponseEntity<List<TransactionAttachmentDto>> getTransactionAttachment(@PathVariable(value = "transactionId") String transactionId, @PathVariable(value = "tenantId") String tenantId){
		logbookReportService = factory.getLogbookReportService(); // Get the logbookReport service

		ResponseEntity<List<TransactionAttachmentDto>> transactionAttachment = logbookReportService.getTransactionAttachment(tenantId, transactionId);
		return transactionAttachment;
	}


	@GetMapping("/getTransactionAttachmentFromCellIdAndTransactionId/{transactionId}/{cellId}/{tenantId}")
	ResponseEntity<TransactionAttachmentDto> getTransactionAttachment(@PathVariable(value = "transactionId") String transactionId, @PathVariable(value = "cellId") String cellId, @PathVariable(value = "tenantId") String tenantId){
		logbookReportService = factory.getLogbookReportService(); // Get the logbookReport service

		ResponseEntity<TransactionAttachmentDto> transactionAttachment = logbookReportService.getTransactionAttachmentFromCellIdAndTransactionId(tenantId, transactionId, cellId);
		return transactionAttachment;
	}

	@GetMapping("/customReportColumnMapping/{reportName}/{tenantId}")
	ResponseEntity<List<Map<String, Object>>> customReportColumnMapping(@PathVariable(value = "reportName") String reportName, @PathVariable(value = "tenantId") String tenantId){
		logbookReportService = factory.getLogbookReportService(); // Get the logbookReport service

		ResponseEntity<List<Map<String, Object>>> transactionAttachment = logbookReportService.customReportColumnMapping(reportName, tenantId);
		return transactionAttachment;
	}	

	
	
	
	



	// LogbookReportController methods starts here... (LogbookReportController was made separately earlier)


	/**
	 * Retrieves a list of transactions by form name and version within a specified date range.
	 *
	 * @param tenantId the ID of the tenant (user group)
	 * @param formName the name of the form
	 * @param version the version of the form
	 * @param fromDate the starting date for filtering transactions
	 * @param toDate the ending date for filtering transactions
	 * @return a response entity containing the list of transactions
	 */
	@GetMapping("/getTransactionsByFormnameAndVersionBetweenDates/{tenantId}")
	ResponseEntity<List<TransactionDto>> getTransactionsByFormNameAndVersionBetweenDates(
			@PathVariable(value = "tenantId") String tenantId,
			@RequestParam String formName,
			@RequestParam int version,
			@RequestParam String fromDate,
			@RequestParam String toDate){

		logbookReportService = factory.getLogbookReportService(); // Get the logbookReport service

		ResponseEntity<List<TransactionDto>> transactionsByFormNameAndVersionBetweenDates = logbookReportService.getTransactionsByFormNameAndVersionBetweenDates(tenantId, formName, version, fromDate, toDate);
		return transactionsByFormNameAndVersionBetweenDates;
	}

	/**
	 * Retrieves a list of all reports for a specified tenant.
	 *
	 * @param tenantId the ID of the tenant (user group)
	 * @return a response entity containing the list of reports
	 */
	@GetMapping("/reports/{tenantId}")
	public ResponseEntity<List<ReportConfigDto>> getAllReports(@PathVariable(value = "tenantId") String tenantId) {

		logbookReportService = factory.getLogbookReportService(); // Get the logbookReport service

		return logbookReportService.getAllReports(tenantId);
	}

	/**
	 * Retrieves all distinct report names for a specified tenant and creator.
	 *
	 * @param tenantId the ID of the tenant (user group)
	 * @param creator the name of the report creator
	 * @return a response entity containing the list of distinct report names
	 */
	@GetMapping("/distinctreportnames/{tenantId}")
	public ResponseEntity<List<String>> getAllDistinctReportNames(@PathVariable(value = "tenantId") String tenantId,@RequestParam(name = "creator") String creator) {

		logbookReportService = factory.getLogbookReportService(); // Get the logbookReport service

		return logbookReportService.getAllDistinctReportNames(tenantId,creator);
	}

	/**
	 * Retrieves reports based on the specified form name for a given tenant.
	 *
	 * @param tenantId the ID of the tenant (user group)
	 * @param formName the name of the form
	 * @return a response entity containing the list of reports
	 */
	@GetMapping("/reports/formName/{tenantId}")
	public ResponseEntity<List<ReportConfigDto>> getReportFromReportName(@PathVariable(value = "tenantId") String tenantId,@RequestParam(name = "formName") String formName) {

		logbookReportService = factory.getLogbookReportService(); // Get the logbookReport service

		return logbookReportService.getReportFromReportName(tenantId, formName);
	}

	/**
	 * Retrieves a list of transactions by form name and version for a specified tenant.
	 *
	 * @param tenantId the ID of the tenant (user group)
	 * @param formName the name of the form
	 * @param version the version of the form
	 * @return a response entity containing the list of transactions
	 */
	@GetMapping("/getTransactionsByFormnameAndVersion/{tenantId}")
	ResponseEntity<List<TransactionDto>> getTransactionsByFormNameAndVersion(@PathVariable(value = "tenantId") String tenantId,@RequestParam String formName,@RequestParam int version){

		logbookService = factory.getLogbookService(); // Get the logbook service

		ResponseEntity<List<TransactionDto>> transactionsByFormNameAndVersion = logbookService.getTransactionsByFormNameAndVersion(formName, version, tenantId);

		return transactionsByFormNameAndVersion;
	}

	/**
	 * Retrieves editable cell information by form name and version for a specified tenant.
	 *
	 * @param tenantId the ID of the tenant (user group)
	 * @param formName the name of the form
	 * @param versionNumber the version number of the form
	 * @return a response entity containing a list of editable cell information
	 */
	@GetMapping("editableforminfo/versions/{tenantId}")
	public ResponseEntity<List<CellDto>> getEditableCellInfoByFormNameAndVersion(@PathVariable(value = "tenantId") String tenantId,@RequestParam(name = "formName") String formName,
			@RequestParam(value = "version") Integer versionNumber) {

		logbookReportService = factory.getLogbookReportService(); // Get the logbookReport service

		return logbookReportService.getAllEditableCellInfoByFormNameAndVersion(tenantId, formName, versionNumber);
	}

	/**
	 * Retrieves all users for a specified tenant.
	 *
	 * @param tenantId the ID of the tenant (user group)
	 * @return a response entity containing a list of users
	 */
	@GetMapping("/users/{tenantId}")
	public ResponseEntity<List<UserEntity>> getAllUsers(@PathVariable(value = "tenantId") String tenantId) {

		logbookReportService = factory.getLogbookReportService(); // Get the logbookReport service

		return logbookReportService.getAllUsers(tenantId);
	}

	/**
	 * Retrieves user-wise report information for a specified tenant and report name.
	 *
	 * @param tenantId the ID of the tenant (user group)
	 * @param reportName the name of the report
	 * @return a response entity containing the user-wise report information
	 */
	@GetMapping("/userwisereportinfo/{tenantId}")
	public ResponseEntity<List<ReportConfigDto>> userWiseReportsInfo(@PathVariable(value = "tenantId") String tenantId,@RequestParam(name = "reportName") String reportName) {

		logbookReportService = factory.getLogbookReportService(); // Get the logbookReport service

		return logbookReportService.userWiseReportsInfo(tenantId, reportName);
	}

	/**
	 * Retrieves user-wise reports for a specified tenant and user ID.
	 *
	 * @param tenantId the ID of the tenant (user group)
	 * @param userId the ID of the user
	 * @return a response entity containing the user-wise reports
	 */
	@GetMapping("/userwisereport/{tenantId}")
	public ResponseEntity<List<ReportConfigDto>> userWiseReports(@PathVariable(value = "tenantId") String tenantId,@RequestParam(name = "userId") String userId) {

		logbookReportService = factory.getLogbookReportService(); // Get the logbookReport service

		return logbookReportService.userWiseReports(tenantId, userId);
	}

	/**
	 * This method fetches logbook report data based on the given dates and report name.
	 *
	 * @param tenantId The ID of the tenant/group.
	 * @param fromdate The start date for the report.
	 * @param todate The end date for the report.
	 * @param reportName The name of the report we want to fetch data for.
	 * @return A list of report data for the specified time period.
	 */
	@GetMapping("/reportData/{tenantId}")
	public ResponseEntity<List<Map<String, Object>>> fetchLogbookReportData(@PathVariable(value = "tenantId") String tenantId,@RequestParam(name = "fromdate") String fromdate, 
			@RequestParam(name = "todate") String todate, 
			@RequestParam(name = "reportName") String reportName) {

		logbookReportService = factory.getLogbookReportService(); // Get the logbookReport service

		return logbookReportService.fetchLogbookReportData(tenantId, fromdate, todate, reportName);
	}

	/**
	 * This method saves a new report configuration.
	 *
	 * @param tenantId The ID of the tenant/group.
	 * @param reportName The name of the report we want to save.
	 * @param map The data we want to save for the report.
	 * @return A message indicating success or failure.
	 */
	@PostMapping("/insertreport/{tenantId}")
	public ResponseEntity<String> insertReportConfig(@PathVariable(value = "tenantId") String tenantId, @RequestParam(name = "reportName") String reportName, @RequestBody Map<String, Object> map) {

		// Create a new ReportConfigDto object to hold our report configuration
		Map<String, Object> reportConfigObj = (Map<String, Object>) map.get("reportConfig");

		logbookReportService = factory.getLogbookReportService(); // Get the logbookReport service

		ReportConfigDto reportConfig = new ReportConfigDto();

		reportConfig.setFormId((int) reportConfigObj.get("formId"));
		reportConfig.setReportName((String) reportConfigObj.get("reportName"));
		reportConfig.setFormName((String) reportConfigObj.get("formName"));
		reportConfig.setVersionNumber((int) reportConfigObj.get("versionNumber"));
		reportConfig.setCellId((String) reportConfigObj.get("cellId"));
		reportConfig.setReportAliasId((String) reportConfigObj.get("reportAliasId"));
		reportConfig.setCreator((String) reportConfigObj.get("creator"));
		reportConfig.setSharedUser((String) reportConfigObj.get("sharedUser"));
		reportConfig.setFormAliasId((String) reportConfigObj.get("formAliasId"));

		// Prepare the report data
		Object reportDataObject = map.get("reportData");
		List<Map<String, Object>> reportDataObjList = new ArrayList<>();

		// Check if the report data is in a valid format
		if (reportDataObject instanceof List) {
			reportDataObjList = (List<Map<String, Object>>) reportDataObject;
		} else if (reportDataObject instanceof Map) {
			reportDataObjList.add((Map<String, Object>) reportDataObject);
		} else {
			System.err.println("Invalid format for reportData");
		}

		// Create a list of ReportData objects from the report data
		List<ReportData> reportDataList = new ArrayList<>();
		for (Map<String, Object> reportDataObj : reportDataObjList) {
			ReportData reportData = new ReportData();
			reportData.setCellId((String) reportDataObj.get("cellId"));
			reportData.setReportAliasId((String) reportDataObj.get("reportAliasId"));
			reportData.setFormAliasId((String) reportDataObj.get("formAliasId"));
			reportData.setFieldType((String) reportDataObj.get("fieldType"));
			reportDataList.add(reportData);
		}
		String creator = (String) map.get("creatorId");
		String sharedUserLists = (String) map.get("sharedUserLists");

		// Call the service to insert the report configuration and return the response
		return logbookReportService.insertReportConfig(tenantId, reportName, reportConfig, creator, sharedUserLists, reportDataList);
	}

	/**
	 * This method updates the shared users for a report.
	 *
	 * @param tenantId The ID of the tenant/group.
	 * @param reportName The name of the report to update.
	 * @param sharedUser The user to be shared.
	 * @return A message indicating success or failure.
	 */
	@PostMapping("/updateSharedUser/{tenantId}")
	public ResponseEntity<String> updateSharedUser(@PathVariable(value = "tenantId") String tenantId, @RequestParam(name = "reportName") String reportName, @RequestParam(name = "sharedUser") String sharedUser) {

		logbookReportService = factory.getLogbookReportService(); // Get the logbookReport service

		return logbookReportService.updateSharedUsersForReportConfig(tenantId, reportName, sharedUser);
	}

	/**
	 * This method gets logbooks for a specific user.
	 *
	 * @param tenantId The ID of the tenant/group.
	 * @param userId The ID of the user for whom we want to find logbooks.
	 * @return A list of logbooks for that user.
	 */
	@GetMapping("/getLogbooksByUserId/{tenantId}")
	public ResponseEntity<List<Map<String,Object>>> getLogbooksByUserId(@PathVariable(value = "tenantId") String tenantId,@RequestParam String userId){

		logbookService = factory.getLogbookService(); // Get the logbook service

		ResponseEntity<List<Map<String, Object>>> logbooksByUserId = logbookService.getLogbooksByUserId(userId, tenantId);
		return logbooksByUserId;
	}

	/**
	 * This method gets form information based on the form name and version.
	 *
	 * @param tenantId The ID of the tenant/group.
	 * @param formName The name of the form.
	 * @param version The version of the form.
	 * @return The form information for the specified name and version.
	 */
	@GetMapping("/formInfoByformNameAndVersion/{tenantId}")
	public ResponseEntity<FormInfo> getforminfoByformNameAndVersion(@PathVariable(value = "tenantId") String tenantId,@RequestParam String formName, @RequestParam int version) {

		logbookReportService = factory.getLogbookReportService(); // Get the logbookReport service

		return logbookReportService.forminfoByformNameAndVersion(tenantId, formName, version);
	}

	/**
	 * This method generates a PDF report of the logbook.
	 *
	 * @param tenantId The ID of the tenant/group.
	 * @param formName The name of the form for the logbook.
	 * @param version The version of the form.
	 * @param fromDate The start date for the report.
	 * @param toDate The end date for the report.
	 * @param index The index for pagination.
	 * @return The generated PDF report.
	 */
	@GetMapping("/logbookPdf/{tenantId}/{userid}")
	public org.springframework.http.ResponseEntity<InputStreamResource> getLogbookPdf(@PathVariable(value = "tenantId") String tenantId, @PathVariable(value = "userid") String userid, @RequestParam String formName, @RequestParam int version, @RequestParam String fromDate, @RequestParam String toDate, @RequestParam int index) {

		logbookReportService = factory.getLogbookReportService(); // Get the logbookReport service

		return logbookReportService.exportLogBookDataToPdf(tenantId, userid, formName, version, fromDate, toDate, index);
	}

	/**
	 * This method generates a PDF report for a single logbook entry.
	 *
	 * @param tenantId The ID of the tenant/group.
	 * @param formName The name of the form for the logbook.
	 * @param version The version of the form.
	 * @param index The index of the logbook entry to be exported.
	 * @return The generated PDF report for the single logbook entry.
	 */
	@GetMapping("/singleLogbookPdf/{tenantId}/{userid}")
	public org.springframework.http.ResponseEntity<InputStreamResource> getSingleLogbookPdf(@PathVariable(value = "tenantId") String tenantId, @PathVariable(value = "userid") String userid, @RequestParam String formName, @RequestParam int version,  @RequestParam String jobId, @RequestParam String activityId) {

		logbookReportService = factory.getLogbookReportService(); // Get the logbookReport service

		return logbookReportService.exportSingleLogBookDataToPdf(tenantId, userid, formName, version, jobId, activityId);
	}

	/**
	 * This method retrieves transactions based on job ID and activity ID.
	 *
	 * @param tenantId The ID of the tenant/group.
	 * @param jobId The ID of the job.
	 * @param activityId The ID of the activity.
	 * @return The transactions related to the specified job and activity.
	 */
	@GetMapping("/getTransactionByJobidAndActivityid/{tenantId}")
	public ResponseEntity<TransactionDto> getTransactionsByJobIdAndActivityId(@PathVariable(value = "tenantId") String tenantId, @RequestParam String jobId, @RequestParam String activityId){

		logbookReportService = factory.getLogbookReportService(); // Get the logbookReport service

		ResponseEntity<TransactionDto> transactionsByJobIdAndActivityId = logbookReportService.getTransactionsByJobIdAndActivityId(tenantId, jobId, activityId);
		return transactionsByJobIdAndActivityId;
	}	

	/**
	 * This method retrieves user information based on the tenant ID.
	 *
	 * @param tenantId The ID of the tenant/group.
	 * @return The user information for the specified tenant.
	 */
	@GetMapping("/user/{tenantId}")
	public ResponseEntity<UserModel> getUserByTenantId(@PathVariable String tenantId) {

		logbookReportService = factory.getLogbookReportService(); // Get the logbookReport service

		return logbookReportService.findByTenantId(tenantId);
	}
	
	@PostMapping("/register-new-tenant/{tenantid}")
    public ResponseEntity<String> registernewtenant(@PathVariable("tenantid") String tenantid) {
    	try {
    		logbookConfigureService = factory.getLogbookConfigureService(); // Get the LogbookConfigure service
        	
    		logbookConfigureService.registerNewTenant(tenantid);
        	return ResponseEntity.ok().body("Tenant registered successfully");
    	}catch(Exception e) {
    		e.printStackTrace();
    		return ResponseEntity.badRequest().body("Tenant registration unsuccessful");
    	}
    }
	
	
}



