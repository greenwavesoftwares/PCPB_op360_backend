package in.co.greenwave.dao;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import in.co.greenwave.dto.CellDetailsDto;
import in.co.greenwave.dto.FormDto;
import in.co.greenwave.dto.TransactionDto;
import in.co.greenwave.entity.CellEditHistory;
import in.co.greenwave.entity.DropDownInfo;
import in.co.greenwave.entity.FormInfo;

/**
 * specifies the services of log book
 * 
 * @author Subhajit Khasnobish 
 */
public interface LogbookService {

	/**
	 * 
	 * Gets all data of {@link FormInfo}
	 * 
	 * @return @ResponseEntity containing @java.util.List of @FormInfo.
	 */
	ResponseEntity<List<FormDto>> getAllForms(String tenantId);

	/**
	 * gets all active @FormInfo
	 * 
	 * @return @java.util.List of @FormInfo
	 */
	public ResponseEntity<List<FormDto>> getAllActiveForms(String tenantId);

	/**
	 * saves the received @FormInfo
	 * 
	 * @param a @java.util.List of @FormInfo
	 * 
	 * @return the saved @FormInfo from the database, wrapped in @ResponseEntity
	 */
	public ResponseEntity<List<FormInfo>> saveLogbook(List<FormInfo> forms, String tenantId);

	/**
	 * executes the received queries
	 * 
	 * @param deleteQuery as @java.lang.String
	 * @param insertQuery as @java.lang.String
	 * 
	 * @return all Forms including which are effected from the deleteQuery and
	 *         insertQuery
	 */
	public ResponseEntity<List<FormInfo>> saveLogbookData(String deleteQuery, String insertQuery, String tenantId);

	/**
	 * 
	 * @param jobId
	 * @param activityId
	 * @param formName
	 * @param formVersion
	 * @return List of transactions
	 */
	ResponseEntity<List<TransactionDto>> getAllTransactions(String jobId, String activityId, String formName,
			int formVersion, String tenantId);

	public ResponseEntity<List<DropDownInfo>> getDropDownInfo(String query, String tenantId);

	/**
	 * saves the transaction
	 * 
	 * @param transactionDto
	 * @return
	 */
	public ResponseEntity<TransactionDto> saveTransaction(TransactionDto transactionDto);

	/**
	 * gets all @FormInfo for the form name given
	 * 
	 * @param formName
	 * 
	 * @return @java.util.List of @FormInfo
	 */
	public ResponseEntity<List<FormInfo>> getFormInfoByFormName(String formName,String tenantId);

	/**
	 * @java.util.List of @CellInfo for the given form name and version
	 * 
	 * @param formName
	 * @param versionNumber
	 * @return @java.util.List of @CellDto
	 */
	public ResponseEntity<List<CellDetailsDto>> getAllCellInfoByFormNameAndVersion(String formName, Integer versionNumber,String tenantId);

	/**
	 * gets the cell for the given form name, version and cell ID
	 * 
	 * @param formName
	 * @param versionNumber
	 * @param cellId
	 * @return @CellDto for the given params
	 */
	public ResponseEntity<CellDetailsDto> getCellInfo(String formName, Integer versionNumber, String cellId, String tenantId);

	/**
	 * 
	 * @param query
	 * @return @java.util.Map as result
	 */
	public ResponseEntity<Object> getQueryResultForGlobalCellUpdate(String query, String tenantId);

	/**
	 * Executes an sql query to get DropDown data (dropdown specific API)
	 * 
	 * @param query
	 * 
	 * @return @java.util.List of DropDown instances 
	 */
	public ResponseEntity<List<DropDownInfo>> getDropDownsForQuery(String query, String tenantId);


	/**
	 * 
	 * @param cellid
	 * @param jobid
	 * @param activityid
	 * @return List of CellEditHistory
	 */
	public ResponseEntity<List<CellEditHistory>> getCellHistory(String cellid, String jobid, String activityid, String tenantId);



	/**
	 * gets transactions by formname and version
	 * @param formName
	 * @param version
	 * @return
	 */
	ResponseEntity<List<TransactionDto>> getTransactionsByFormNameAndVersion(String formName, int version, String tenantId);

	/**
	 * gets logbooks by user id
	 * 
	 * @param userId
	 * @return
	 */
	ResponseEntity<List<Map<String, Object>>> getLogbooksByUserId(String userId, String tenantId);

	/**
	 * gets logbooks by user id
	 * 
	 * @param userId
	 * @param tenantId
	 * @return List of logbooks
	 */
	ResponseEntity<List<Map<String, Object>>> getlogbooksByUserIdAndTenantId(String userId, String tenantId);




}
