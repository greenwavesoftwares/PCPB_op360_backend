package in.co.greenwave.service;

// Importing necessary classes for handling data and web responses
import java.io.ByteArrayInputStream; // Helps create input streams from byte arrays
import java.text.DateFormat; // For formatting dates
import java.text.SimpleDateFormat; // For creating specific date formats
import java.util.ArrayList; // For using dynamic arrays
import java.util.Date; // For working with date objects
import java.util.List; // For using lists of items
import java.util.Map; // For handling key-value pairs

import org.springframework.beans.factory.annotation.Autowired; // Automatically gets the required services
import org.springframework.core.io.InputStreamResource; // Helps manage data streams
import org.springframework.http.ContentDisposition; // Manages content disposition in HTTP
import org.springframework.http.HttpHeaders; // For HTTP headers
import org.springframework.http.HttpStatus; // For using HTTP status codes
import org.springframework.http.MediaType; // For handling media types in responses
import org.springframework.http.ResponseEntity; // For creating HTTP responses
import org.springframework.stereotype.Service; // Marks this class as a service component

import com.fasterxml.jackson.core.JsonProcessingException; // Handles JSON processing exceptions
import com.fasterxml.jackson.databind.JsonMappingException; // Handles JSON mapping exceptions
import com.fasterxml.jackson.databind.ObjectMapper; // Helps convert objects to JSON and vice versa

import in.co.greenwave.dao.LogbookReportService;
import in.co.greenwave.dto.CellDto; // Data Transfer Object for cell data
import in.co.greenwave.dto.ReportConfigDto; // Data Transfer Object for report configuration
import in.co.greenwave.dto.ReportData; // Data Transfer Object for report data
import in.co.greenwave.dto.TransactionAttachmentDto;
import in.co.greenwave.dto.TransactionDto; // Data Transfer Object for transaction data
import in.co.greenwave.entity.FormInfo; // Entity class for form information
import in.co.greenwave.entity.ReportConfigEntity; // Entity class for report configuration
import in.co.greenwave.entity.Transaction; // Entity class for transaction
import in.co.greenwave.entity.UserEntity; // Entity class for user data
import in.co.greenwave.entity.UserModel; // Entity class for user models
import in.co.greenwave.repository.LogbookDAOServiceSqlServer; // Repository for logbook data
import in.co.greenwave.repository.LogbookReportDAOServiceSqlServer; // Repository for logbook report data

/**
 * This class is like a helper for managing logbook reports.
 * It gets information about reports and helps with saving and retrieving data.
 * 
 * The @Service annotation tells Spring that this class provides business logic.
 */
@Service
public class LogbookReportServiceImpl implements LogbookReportService {

	@Autowired
	LogbookDAOServiceSqlServer logbookRepositoryImpl; // This helps us talk to the database for logbook data

	@Autowired
	LogbookReportDAOServiceSqlServer logbookReportRepositoryImpl; // This helps us talk to the database for report data

	/**
	 * This method gets all reports for a specific group.
	 *
	 * @param tenantId The ID of the group we want reports for.
	 * @return A list of report configurations.
	 */
	public ResponseEntity<List<ReportConfigDto>> getAllReports(String tenantId) {
		ResponseEntity<List<ReportConfigDto>> responseEntity = null; // Prepare a response entity
		try {
			// Get all report entities from the database
			List<ReportConfigEntity> allFormsInfo = logbookReportRepositoryImpl.getAllLogBookReports(tenantId);
			System.out.println(allFormsInfo.get(0).getFormId());
			ArrayList<ReportConfigDto> formDtoList = new ArrayList<ReportConfigDto>(); // List to hold report configurations

			// Convert each report entity to a Data Transfer Object (DTO)
			allFormsInfo.forEach(formInfo -> {
				ReportConfigDto formDto = new ReportConfigDto(formInfo);
				formDtoList.add(formDto); // Add the DTO to the list
			});
			responseEntity=ResponseEntity.ok(formDtoList); // Return the list as a response
		} catch (Exception e) {
			responseEntity = ResponseEntity.internalServerError().build(); // Return an error response if something goes wrong
		}
		return responseEntity; // Return the response entity
	}

	/**
	 * This method gets all distinct report names for a specific group and creator.
	 *
	 * @param tenantId The ID of the group.
	 * @param creator The person who created the reports.
	 * @return A list of unique report names.
	 */
	public ResponseEntity<List<String>> getAllDistinctReportNames(String tenantId, String creator) {
		ResponseEntity<List<String>> responseEntity = null; // Prepare a response entity
		try {

			// Get all distinct report names from the database
			List<String> allFormsInfo = logbookReportRepositoryImpl.getAllDistinctReportNames(tenantId, creator);
//			System.out.println("getAllDistinctReportNames : " + allFormsInfo);
			responseEntity=ResponseEntity.ok(allFormsInfo); // Return the list of report names
		} catch (Exception e) {
			responseEntity=ResponseEntity.internalServerError().build(); // Return an error response if something goes wrong
		}
		return responseEntity; // Return the response entity
	}

	/**
	 * This method gets reports by their name for a specific group.
	 *
	 * @param tenantId The ID of the group.
	 * @param formName The name of the report we want to find.
	 * @return A list of report configurations for that name.
	 */
	public ResponseEntity<List<ReportConfigDto>> getReportFromReportName(String tenantId, String formName) {
		ResponseEntity<List<ReportConfigDto>> responseEntity = null; // Prepare a response entity
		try {

			// Get reports from the database by form name
			List<ReportConfigEntity> allFormsInfo = logbookReportRepositoryImpl.getReportInfoByFormName(tenantId, formName);

			System.out.println(formName);

			if(!allFormsInfo.isEmpty())
				System.out.println(allFormsInfo.get(0).getFormId()); // Print the form ID if available
			ArrayList<ReportConfigDto> formDtoList = new ArrayList<ReportConfigDto>(); // List to hold report configurations

			// Convert each report entity to a DTO
			allFormsInfo.forEach(formInfo -> {
				ReportConfigDto formDto = new ReportConfigDto(formInfo);
				formDtoList.add(formDto); // Add the DTO to the list
			});
			responseEntity = ResponseEntity.ok(formDtoList); // Return the list as a response
		} catch (Exception e) {
			responseEntity = ResponseEntity.internalServerError().build(); // Return an error response if something goes wrong
		}
		return responseEntity; // Return the response entity
	}

	/**
	 * This method gets all editable cell information for a specific report and version.
	 *
	 * @param tenantId The ID of the group.
	 * @param formName The name of the report.
	 * @param versionNumber The version of the report.
	 * @return A list of editable cells.
	 */
	public ResponseEntity<List<CellDto>> getAllEditableCellInfoByFormNameAndVersion(String tenantId, String formName, Integer versionNumber) {
		ResponseEntity<List<CellDto>> responseEntity = null; // Prepare a response entity
		try {

			// Get all cell information from the database
			List<CellDto> cellList = logbookRepositoryImpl.getAllCellInfoByFormNameAndVersion(formName, versionNumber,tenantId);
			List<CellDto> editableCellList = new ArrayList<>();

			// Add cells to the list if they are editable or dependent
			for(CellDto cell : cellList) {
				if(cell.isDependent()||cell.isEditable()) {
					editableCellList.add(cell);
				}
			}
			responseEntity = ResponseEntity.ok(editableCellList); // Return the list of editable cells
		} catch (Exception e) {
			System.out.println("Exception : " + e); // Print the exception
			responseEntity = ResponseEntity.internalServerError().build(); // Return an error response if something goes wrong
		}
		return responseEntity; // Return the response entity
	}

	/**
	 * This method gets all users for a specific group.
	 *
	 * @param tenantId The ID of the group.
	 * @return A list of users in that group.
	 */
	public ResponseEntity<List<UserEntity>> getAllUsers(String tenantId) {
		ResponseEntity<List<UserEntity>> responseEntity = null; // Prepare a response entity
		try {

			// Get all users from the database
			List<UserEntity> allUser = logbookReportRepositoryImpl.getAllUsers(tenantId);
			responseEntity = ResponseEntity.ok(allUser); // Return the list of users
		} catch (Exception e) {
			System.out.println("Exception : " + e); // Print the exception
			responseEntity = ResponseEntity.internalServerError().build(); // Return an error response if something goes wrong
		}
		return responseEntity; // Return the response entity
	}

	/**
	 * This method retrieves reports for a specific user within a group.
	 *
	 * @param tenantId The ID of the group.
	 * @param userId The ID of the user whose reports we want to fetch.
	 * @return A list of report configurations for that user.
	 */
	public ResponseEntity<List<ReportConfigDto>> userWiseReports(String tenantId, String userId) {
		ResponseEntity<List<ReportConfigDto>> responseEntity = null; // Prepare a response entity
		try {

			// Fetch reports for the specified user from the database
			List<ReportConfigEntity> allReports = logbookReportRepositoryImpl.userWiseReports(tenantId, userId);
			ArrayList<ReportConfigDto> reportList = new ArrayList<ReportConfigDto>(); // List to hold report configurations

			// Convert each report entity to a Data Transfer Object (DTO)
			allReports.forEach(reportInfo -> {
				ReportConfigDto reportDto = new ReportConfigDto(reportInfo);
				reportList.add(reportDto); // Add the DTO to the list
			});
			System.out.println("reportList : " + reportList); // Print the list of reports
			responseEntity = ResponseEntity.ok(reportList); // Return the list as a response
		} catch (Exception e) {
			responseEntity = ResponseEntity.internalServerError().build(); // Return an error response if something goes wrong
		}
		return responseEntity; // Return the response entity
	}

	/**
	 * This method fetches logbook report data for a specific date range and report name.
	 *
	 * @param tenantId The ID of the group.
	 * @param fromDate The start date for the report data.
	 * @param toDate The end date for the report data.
	 * @param reportName The name of the report we want to fetch.
	 * @return A list of report data represented as key-value pairs.
	 */
	public ResponseEntity<List<Map<String, Object>>> fetchLogbookReportData(String tenantId, String fromDate, String toDate, String reportName) {
		ResponseEntity<List<Map<String, Object>>> responseEntity = null; // Prepare a response entity
		try {

			// Fetch logbook report data from the database
			List<Map<String, Object>> allReports = logbookReportRepositoryImpl.fetchLogbookReportData(tenantId, fromDate, toDate, reportName);
			responseEntity = ResponseEntity.ok(allReports); // Return the report data
		} catch (Exception e) {
			responseEntity = ResponseEntity.internalServerError().build(); // Return an error response if something goes wrong
		}
		return responseEntity; // Return the response entity
	}

	/**
	 * This method inserts a new report configuration into the system.
	 *
	 * @param tenantId The ID of the group.
	 * @param reportname The name of the report.
	 * @param reportconfiglist The configuration details for the report.
	 * @param creatorId The ID of the user who created the report.
	 * @param sharedUserLists The list of users with whom the report is shared.
	 * @param reportData Additional data for the report.
	 * @return A message indicating the result of the insert operation.
	 */
	public ResponseEntity<String> insertReportConfig(String tenantId, String reportname, ReportConfigDto reportconfiglist,
			String creatorId, String sharedUserLists, List<ReportData> reportData) {
		ResponseEntity<String> responseEntity = null; // Prepare a response entity
		try {

			// Insert the report configuration into the database
			int insertReportResponse = logbookReportRepositoryImpl.insertReportConfig(tenantId, reportname, reportconfiglist, creatorId, sharedUserLists, reportData);
			if(insertReportResponse == 0) {
				responseEntity = ResponseEntity.status(201).body("Report Name Already Exists"); // Report name already exists
			}
			if(insertReportResponse > 0) {
				responseEntity = ResponseEntity.ok("Report Inserted Successfully"); // Report inserted successfully
			}
		} catch (Exception e) {
			responseEntity = ResponseEntity.internalServerError().build(); // Return an error response if something goes wrong
		}
		return responseEntity; // Return the response entity
	}

	/**
	 * This method updates the shared users for a specific report configuration.
	 *
	 * @param tenantId The ID of the group.
	 * @param reportName The name of the report to update.
	 * @param sharedUser The updated list of shared users.
	 * @return A message indicating the result of the update operation.
	 */
	public ResponseEntity<String> updateSharedUsersForReportConfig(String tenantId, String reportName, String sharedUser) {
		ResponseEntity<String> responseEntity = null; // Prepare a response entity
		try {

			// Update the shared users for the specified report
			int insertReportResponse = logbookReportRepositoryImpl.updateSharedUsersForReportConfig(tenantId, reportName, sharedUser);
			if(insertReportResponse == 0) {
				responseEntity = ResponseEntity.status(201).body("Could not update Shared User"); // Failed to update
			}
			if(insertReportResponse > 0) {
				System.out.println("Shared user updated successfully"); // Print success message
				responseEntity = ResponseEntity.ok("Shared user updated successfully"); // Return success response
			}
		} catch (Exception e) {
			System.out.println("Exception : " + e); // Print the exception
			responseEntity = ResponseEntity.internalServerError().build(); // Return an error response if something goes wrong
		}
		return responseEntity; // Return the response entity
	}

	/**
	 * This method retrieves transactions for a specific form and version within a date range.
	 *
	 * @param tenantId The ID of the group.
	 * @param formName The name of the form.
	 * @param version The version of the form.
	 * @param fromDate The start date for filtering transactions.
	 * @param toDate The end date for filtering transactions.
	 * @return A list of transactions as Data Transfer Objects (DTOs).
	 */
	public ResponseEntity<List<TransactionDto>> getTransactionsByFormNameAndVersionBetweenDates(String tenantId, String formName,
			int version, String fromDate, String toDate) {
		ResponseEntity<List<TransactionDto>> responseEntity = null; // Prepare a response entity
		try {

			// Fetch transactions from the repository
			List<Transaction> transactions = logbookReportRepositoryImpl.getTransactionsByFormNameAndVersionBetweenDates(tenantId, formName, version, fromDate, toDate);
			ArrayList<TransactionDto> transactionDtoList = new ArrayList<>(); // List to hold transaction DTOs

			// Convert each transaction entity to a DTO
			transactions.forEach((transaction) -> {
				TransactionDto transactionDto = new TransactionDto(transaction); // Create DTO from transaction
				ObjectMapper objectMapper = new ObjectMapper(); // Create an ObjectMapper to handle JSON		

				try {

					// Convert logbook data from JSON string to a map
					Map<String,Object> jsonInfo = objectMapper.readValue(transaction.getLogbookData(), Map.class);
					System.out.println(jsonInfo); // Print the JSON data
					transactionDto.setLogbookData(jsonInfo); // Set the logbook data in the DTO
				} catch (JsonMappingException e) {
					e.printStackTrace(); // Print error if there's a problem mapping JSON
				} catch (JsonProcessingException e) {
					e.printStackTrace(); // Print error if there's a problem processing JSON
				}
				transactionDtoList.add(transactionDto); // Add the DTO to the list
			});
			responseEntity = ResponseEntity.ok(transactionDtoList); // Return the list of transaction DTOs
		} catch (Exception e) {
			responseEntity = ResponseEntity.internalServerError().build(); // Return an error response if something goes wrong	
		}
		return responseEntity; // Return the response entity
	}

	/**
	 * This method retrieves reports for a specific user based on the report name.
	 *
	 * @param tenantId The ID of the group.
	 * @param reportName The name of the report.
	 * @return A list of report configurations as Data Transfer Objects (DTOs).
	 */
	public ResponseEntity<List<ReportConfigDto>> userWiseReportsInfo(String tenantId, String reportName) {
		ResponseEntity<List<ReportConfigDto>> responseEntity = null; // Prepare a response entity
		try {

			// Fetch user-specific reports from the repository
			List<ReportConfigEntity> allReports = logbookReportRepositoryImpl.userWiseReportsInfo(tenantId, reportName);
			ArrayList<ReportConfigDto> reportList = new ArrayList<ReportConfigDto>();	 // List to hold report DTOs

			// Convert each report entity to a DTO
			allReports.forEach(reportInfo -> {
				ReportConfigDto reportDto = new ReportConfigDto(reportInfo); // Create DTO from report
				reportList.add(reportDto); // Add the DTO to the list
			});
			System.err.println(logbookReportRepositoryImpl.userWiseReportsInfo(tenantId, reportName)); // Print the fetched reports
			responseEntity = ResponseEntity.ok(reportList); // Return the list of report DTOs
		} catch (Exception e) {
			responseEntity = ResponseEntity.internalServerError().build(); // Return an error response if something goes wrong
		}
		return responseEntity; // Return the response entity
	}

	/**
	 * This method retrieves form information based on the form name and version.
	 *
	 * @param tenantId The ID of the group.
	 * @param formName The name of the form.
	 * @param version The version of the form.
	 * @return Form information as an entity.
	 */
	public ResponseEntity<FormInfo> forminfoByformNameAndVersion(String tenantId, String formName, int version) {
		ResponseEntity<FormInfo> responseEntity = null; // Prepare a response entity
		try {

			// Fetch form information from the repository
			FormInfo formInfoWithVersions = logbookReportRepositoryImpl.forminfoByformNameAndVersion(tenantId, formName, version);
			responseEntity = ResponseEntity.ok(formInfoWithVersions); // Return the form information
		} catch (Exception e) {
			responseEntity = ResponseEntity.internalServerError().build(); // Return an error response if something goes wrong
		}
		return responseEntity; // Return the response entity
	}

	/**
	 * This method exports logbook data to a PDF file.
	 *
	 * @param tenantId The ID of the group.
	 * @param formName The name of the form.
	 * @param version The version of the form.
	 * @param fromDate The start date for the data.
	 * @param todate The end date for the data.
	 * @param index The index for pagination or specific data fetching.
	 * @return A PDF file containing the logbook data.
	 */
	public org.springframework.http.ResponseEntity<InputStreamResource> exportLogBookDataToPdf(String tenantId, String userid, String formName, int version, String fromDate, String todate, int index) {
		try {

			// Export logbook data to a PDF
			ByteArrayInputStream bis = logbookReportRepositoryImpl.exportLogBookDataToPdf(tenantId, userid, formName, version, fromDate, todate, index);
			HttpHeaders headers = new HttpHeaders(); // Prepare HTTP headers
			DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd-HH:mm:ss"); // Format the date for the filename
			Date todaysDate = new Date(); // Get today's date
			String formattedDate = dateFormat.format(todaysDate); // Format today's date
			String frmName = formName.replace(" ", "-"); // Replace spaces in form name with dashes
			String filename = frmName + "_" + formattedDate + ".pdf"; // Create a filename for the PDF
			System.out.println("fileName : " + filename); // Print the filename
			headers.setContentDisposition(ContentDisposition.builder("attachment").filename(filename).build()); // Set the filename in the headers

			// Return the PDF as a response
			return org.springframework.http.ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
					.body(new InputStreamResource(bis));
		} catch (Exception e) {
			e.printStackTrace(); // Print any errors that occur
			return org.springframework.http.ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Return an error response if something goes wrong
		}
	}

	/**
	 * This method exports a single logbook entry to a PDF file.
	 *
	 * @param tenantId The ID of the group.
	 * @param formName The name of the form.
	 * @param version The version of the form.
	 * @param index The index for pagination or specific data fetching.
	 * @return A PDF file containing the single logbook entry.
	 */
	public org.springframework.http.ResponseEntity<InputStreamResource> exportSingleLogBookDataToPdf(String tenantId, String userid, String formName, int version, String jobId, String activityId){
		try {

			// Export a single logbook entry to a PDF
			ByteArrayInputStream bis = logbookReportRepositoryImpl.exportSingleLogBookDataToPdf(tenantId, userid, formName, version, jobId, activityId);
			HttpHeaders headers = new HttpHeaders(); // Prepare HTTP headers
			DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd-HH:mm:ss"); // Format the date for the filename
			Date todaysDate = new Date(); // Get today's date
			String formattedDate = dateFormat.format(todaysDate); // Format today's date
			String frmName = formName.replace(" ", "-"); // Replace spaces in form name with dashes
			String filename = frmName + "_" + formattedDate + ".pdf"; // Create a filename for the PDF
			System.out.println("fileName : " + filename); // Print the filename
			headers.setContentDisposition(ContentDisposition.builder("attachment").filename(filename).build()); // Set the filename in the headers

			// Return the PDF as a response
			return org.springframework.http.ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
					.body(new InputStreamResource(bis));
		} catch (Exception e) {
			e.printStackTrace(); // Print any errors that occur
			return org.springframework.http.ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Return an error response
		}
	}

	/**
	 * This method retrieves transactions based on job ID and activity ID.
	 *
	 * @param tenantId The ID of the group.
	 * @param jobId The ID of the job.
	 * @param activityId The ID of the activity.
	 * @return A transaction as a Data Transfer Object (DTO).
	 */
	@Override
	public ResponseEntity<TransactionDto> getTransactionsByJobIdAndActivityId(String tenantId, String jobId, String activityId) {
		ResponseEntity<TransactionDto> responseEntity = null; // Prepare a response entity
		try {

			// Fetch transaction by job ID and activity ID from the repository
			TransactionDto transactionsByJobIdAndActivityId = logbookReportRepositoryImpl.getTransactionsByJobIdAndActivityId(tenantId, jobId, activityId);
			responseEntity = ResponseEntity.ok(transactionsByJobIdAndActivityId); // Return the transaction DTO
		} catch (Exception e) {
			responseEntity = ResponseEntity.internalServerError().build(); // Return an error response if something goes wrong
		}
		return responseEntity; // Return the response entity
	}

	/**
	 * This method finds a user by their tenant ID.
	 *
	 * @param tenantId The ID of the group.
	 * @return The user model as a Data Transfer Object (DTO).
	 */
	@Override
	public ResponseEntity<UserModel> findByTenantId(String tenantId) {
		ResponseEntity<UserModel> responseEntity = null; // Prepare a response entity
		try {

			// Fetch user by tenant ID from the repository
			UserModel user = logbookReportRepositoryImpl.findByTenantId(tenantId);
			responseEntity = ResponseEntity.ok(user); // Return the user DTO
		} catch (Exception e) {
			responseEntity = ResponseEntity.internalServerError().build(); // Return an error response if something goes wrong
		}
		return responseEntity; // Return the response entity
	}
	
	
	public ResponseEntity<List<TransactionAttachmentDto>> saveTransactionAttachment(String tenantId, List<TransactionAttachmentDto> transactionAttachment) {
		ResponseEntity<List<TransactionAttachmentDto>> responseEntity = null; // Prepare a response entity
		try {

			// Fetch transactions from the repository
			List<TransactionAttachmentDto> transactions = logbookReportRepositoryImpl.saveTransactionAttachment(tenantId, transactionAttachment);
			responseEntity = ResponseEntity.ok(transactions); // Return the list of transaction DTOs
		} catch (Exception e) {
			responseEntity = ResponseEntity.internalServerError().build(); // Return an error response if something goes wrong	
		}
		return responseEntity; // Return the response entity
	}
	
	public ResponseEntity<List<TransactionAttachmentDto>> getTransactionAttachment(String tenantId, String transactionId){
		ResponseEntity<List<TransactionAttachmentDto>> responseEntity = null; // Prepare a response entity
		try {
			// Fetch transactions from the repository
			List<TransactionAttachmentDto> transactions = logbookReportRepositoryImpl.getTransactionAttachment(tenantId, transactionId);
			responseEntity = ResponseEntity.ok(transactions); // Return the list of transaction DTOs
		} catch (Exception e) {
			responseEntity = ResponseEntity.internalServerError().build(); // Return an error response if something goes wrong	
		}
		return responseEntity; // Return the response entity
	}
	
	
	public ResponseEntity<TransactionAttachmentDto> getTransactionAttachmentFromCellIdAndTransactionId(String tenantId, String transactionId, String cellId){
		ResponseEntity<TransactionAttachmentDto> responseEntity = null; // Prepare a response entity
		try {
			// Fetch transactions from the repository
			TransactionAttachmentDto transactions = logbookReportRepositoryImpl.getTransactionAttachmentFromCellIdAndTransactionId(tenantId, transactionId, cellId);
			responseEntity = ResponseEntity.ok(transactions); // Return the list of transaction DTOs
		} catch (Exception e) {
			responseEntity = ResponseEntity.internalServerError().build(); // Return an error response if something goes wrong	
		}
		return responseEntity; // Return the response entity
	}
	
	public ResponseEntity<List<Map<String, Object>>> customReportColumnMapping(String reportName, String tenantId){
		ResponseEntity<List<Map<String, Object>>> responseEntity = null; // Prepare a response entity
		try {
			// Fetch transactions from the repository
			List<Map<String, Object>> transactions = logbookReportRepositoryImpl.customReportColumnMapping(reportName, tenantId);
			responseEntity = ResponseEntity.ok(transactions); // Return the list of transaction DTOs
		} catch (Exception e) {
			responseEntity = ResponseEntity.internalServerError().build(); // Return an error response if something goes wrong	
		}
		return responseEntity; // Return the response entity
	}
}