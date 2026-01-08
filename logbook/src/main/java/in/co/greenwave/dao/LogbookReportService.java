package in.co.greenwave.dao;

import java.util.List; // Import the List class for handling lists of items
import java.util.Map; // Import the Map class for handling key-value pairs

import org.springframework.core.io.InputStreamResource; // Import for handling input streams
import org.springframework.http.ResponseEntity; // Import for building HTTP responses

import in.co.greenwave.dto.CellDto; // Data Transfer Object for cell data
import in.co.greenwave.dto.ReportConfigDto; // Data Transfer Object for report configuration
import in.co.greenwave.dto.ReportData; // Data Transfer Object for report data
import in.co.greenwave.dto.TransactionAttachmentDto;
import in.co.greenwave.dto.TransactionDto; // Data Transfer Object for transaction data
import in.co.greenwave.entity.FormInfo; // Entity representing form information
import in.co.greenwave.entity.UserEntity; // Entity representing a user
import in.co.greenwave.entity.UserModel; // Model representing user information

/**
 * This interface defines the operations for managing logbook reports.
 * It serves as a blueprint for the LogbookReportService implementation.
 */
public interface LogbookReportService {

	/**
	 * Get all reports for a specific group (tenant).
	 *
	 * @param tenantId The ID of the group.
	 * @return A list of report configurations as Data Transfer Objects (DTOs).
	 */
	ResponseEntity<List<ReportConfigDto>> getAllReports(String tenantId);

	/**
	 * Get all distinct report names created by a specific user.
	 *
	 * @param tenantId The ID of the group.
	 * @param creator The creator of the reports.
	 * @return A list of unique report names.
	 */
	ResponseEntity<List<String>> getAllDistinctReportNames(String tenantId, String creator);

	/**
	 * Get reports based on the report name for a specific group.
	 *
	 * @param tenantId The ID of the group.
	 * @param formName The name of the form related to the report.
	 * @return A list of report configurations as DTOs.
	 */
	ResponseEntity<List<ReportConfigDto>> getReportFromReportName(String tenantId, String formName);

	/**
	 * Get all editable cell information for a specific form and version.
	 *
	 * @param tenantId The ID of the group.
	 * @param formName The name of the form.
	 * @param versionNumber The version of the form.
	 * @return A list of editable cell data as DTOs.
	 */
	ResponseEntity<List<CellDto>> getAllEditableCellInfoByFormNameAndVersion(String tenantId, String formName, Integer versionNumber);

	/**
	 * Get all users for a specific group.
	 *
	 * @param tenantId The ID of the group.
	 * @return A list of user entities.
	 */
	ResponseEntity<List<UserEntity>> getAllUsers(String tenantId);

	/**
	 * Get reports specific to a user within a group.
	 *
	 * @param tenantId The ID of the group.
	 * @param userId The ID of the user.
	 * @return A list of report configurations as DTOs.
	 */
	ResponseEntity<List<ReportConfigDto>> userWiseReports(String tenantId, String userId);

	/**
	 * Fetch logbook report data within a date range for a specific report.
	 *
	 * @param tenantId The ID of the group.
	 * @param fromDate The starting date for fetching data.
	 * @param toDate The ending date for fetching data.
	 * @param reportName The name of the report.
	 * @return A list of logbook report data as key-value pairs.
	 */
	ResponseEntity<List<Map<String, Object>>> fetchLogbookReportData(String tenantId, String fromDate, String toDate, String reportName);

	/**
	 * Insert a new report configuration for a specific group.
	 *
	 * @param tenantId The ID of the group.
	 * @param reportname The name of the report.
	 * @param reportconfiglist The report configuration details.
	 * @param creatorId The ID of the creator.
	 * @param sharedUserLists The list of users with whom the report is shared.
	 * @param reportData Additional data for the report.
	 * @return A confirmation message as a string.
	 */
	ResponseEntity<String> insertReportConfig(String tenantId, String reportname, ReportConfigDto reportconfiglist, String creatorId, String sharedUserLists, List<ReportData> reportData);

	/**
	 * Update the list of shared users for a specific report configuration.
	 *
	 * @param tenantId The ID of the group.
	 * @param reportName The name of the report.
	 * @param sharedUser The user to share the report with.
	 * @return A confirmation message as a string.
	 */
	ResponseEntity<String> updateSharedUsersForReportConfig(String tenantId, String reportName, String sharedUser);

	/**
	 * Get transactions for a specific form and version within a date range.
	 *
	 * @param tenantId The ID of the group.
	 * @param formName The name of the form.
	 * @param version The version of the form.
	 * @param fromDate The starting date for filtering transactions.
	 * @param toDate The ending date for filtering transactions.
	 * @return A list of transactions as Data Transfer Objects (DTOs).
	 */
	ResponseEntity<List<TransactionDto>> getTransactionsByFormNameAndVersionBetweenDates(String tenantId, String formName, int version,
			String fromDate, String toDate);

	/**
	 * Get user-wise report information for a specific report name.
	 *
	 * @param tenantId The ID of the group.
	 * @param reportName The name of the report.
	 * @return A list of report configurations as DTOs.
	 */
	ResponseEntity<List<ReportConfigDto>> userWiseReportsInfo(String tenantId, String reportName);

	/**
	 * Get information about a specific form based on its name and version.
	 *
	 * @param tenantId The ID of the group.
	 * @param formName The name of the form.
	 * @param version The version of the form.
	 * @return Form information as an entity.
	 */
	ResponseEntity<FormInfo> forminfoByformNameAndVersion(String tenantId, String formName, int version);

	/**
	 * Export logbook data to a PDF file.
	 *
	 * @param tenantId The ID of the group.
	 * @param formName The name of the form.
	 * @param version The version of the form.
	 * @param fromDate The starting date for exporting data.
	 * @param todate The ending date for exporting data.
	 * @param index The index for pagination or specific data fetching.
	 * @return A PDF file containing the logbook data.
	 */
	org.springframework.http.ResponseEntity<InputStreamResource> exportLogBookDataToPdf(String tenantId, String userid, String formName, int version, String fromDate, String todate, int index);

	/**
	 * Export a single logbook entry to a PDF file.
	 *
	 * @param tenantId The ID of the group.
	 * @param formName The name of the form.
	 * @param version The version of the form.
	 * @param index The index for pagination or specific data fetching.
	 * @return A PDF file containing the single logbook entry.
	 */
	org.springframework.http.ResponseEntity<InputStreamResource> exportSingleLogBookDataToPdf(String tenantId, String userid, String formName, int version, String jobId, String activityId);

	/**
	 * Get transactions based on job ID and activity ID.
	 *
	 * @param tenantId The ID of the group.
	 * @param jobId The ID of the job.
	 * @param activityId The ID of the activity.
	 * @return A transaction as a Data Transfer Object (DTO).
	 */
	ResponseEntity<TransactionDto> getTransactionsByJobIdAndActivityId(String tenantId, String jobId, String activityId);

	/**
	 * Find a user by their tenant ID.
	 *
	 * @param tenantId The ID of the group.
	 * @return The user model as a Data Transfer Object (DTO).
	 */
	ResponseEntity<UserModel> findByTenantId(String tenantId);

	ResponseEntity<List<TransactionAttachmentDto>> saveTransactionAttachment(String tenantId, List<TransactionAttachmentDto> transactionAttachmentDto);

	ResponseEntity<List<TransactionAttachmentDto>> getTransactionAttachment(String tenantId, String transactionId);
	
	ResponseEntity<TransactionAttachmentDto> getTransactionAttachmentFromCellIdAndTransactionId(String tenantId, String transactionId, String cellId);

	ResponseEntity<List<Map<String, Object>>> customReportColumnMapping(String reportName, String tenantId);
}
