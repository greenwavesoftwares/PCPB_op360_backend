package in.co.greenwave.service;

import java.util.ArrayList; // Import the ArrayList class for creating lists of items
import java.util.List; // Import the List interface for handling lists
import java.util.Map; // Import the Map interface for handling key-value pairs
import java.util.stream.Collectors; // Import for using streams to process collections

import org.springframework.beans.factory.annotation.Autowired; // Import for dependency injection
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity; // Import for building HTTP responses
import org.springframework.stereotype.Repository; // Import to define a repository component

import com.fasterxml.jackson.core.JsonProcessingException; // Import for handling JSON processing exceptions
import com.fasterxml.jackson.databind.JsonMappingException; // Import for handling JSON mapping exceptions
import com.fasterxml.jackson.databind.ObjectMapper; // Import for converting objects to and from JSON

import in.co.greenwave.dao.LogbookService;
import in.co.greenwave.dto.CellDetailsDto; // Data Transfer Object for cell details
import in.co.greenwave.dto.CellDto; // Data Transfer Object for cell data
import in.co.greenwave.dto.FormDto; // Data Transfer Object for form data
import in.co.greenwave.dto.TransactionDto; // Data Transfer Object for transaction data
import in.co.greenwave.entity.CellEditHistory; // Entity representing the history of cell edits
import in.co.greenwave.entity.DropDownInfo; // Entity representing dropdown information
import in.co.greenwave.entity.FormInfo; // Entity representing form information
import in.co.greenwave.entity.Transaction; // Entity representing transaction data
import in.co.greenwave.repository.LogbookDAOServiceSqlServer; // Repository for logbook data

/**
 * This class is responsible for managing and processing logbook data.
 * It provides services for the Logbook.
 * 
 * Author: Subhajit Khasnobish
 */
@Repository // This annotation indicates that the class is a repository component
public class LogbookServiceImpl implements LogbookService {

	/**
	 *  @see getAllCellInfoByFormNameAndVersion(String formName, Integer versionNumber,String tenantId)
	 *  
	 *  used to keep the cellRowNum value
	 */
	int cellRowNum = 1;

	/**
	 *  reference of @LogbookDAOServiceSqlServer
	 */
	@Autowired
	LogbookDAOServiceSqlServer logbookRepositoryImpl;

	/**
	 * 
	 * Gets all data of {@link FormInfo}
	 * 
	 * @return @ResponseEntity containing @java.util.List of @FormInfo.
	 */
	@Override
	public ResponseEntity<List<FormDto>> getAllForms(String tenantId) {
		ResponseEntity<List<FormDto>> responseEntity = null;
		/**
		 *  Handles the exception if occurs during the DAO layer method invokation
		 */
		try {
			List<FormDto> allFormsInfo = logbookRepositoryImpl.getAllForms(tenantId);
//			ArrayList<FormDto> formDtoList = new ArrayList<FormDto>();
//			/**
//			 *  makes an @FormDto out of each @FormInfo and adds to a @java.util.List
//			 */
//			allFormsInfo.forEach(formInfo -> {
//				FormDto formDto = new FormDto(formInfo);
//				formDtoList.add(formDto);
//			});
			responseEntity = ResponseEntity.ok(allFormsInfo);
		} catch (Exception e) {
			responseEntity = ResponseEntity.internalServerError().build();		
		}
		return responseEntity;
	}

	/**
	 * Gets all active @FormInfo data
	 * 
	 * @param tenantId 
	 * 
	 * @return @ResponseEntity containing @java.util.List of @FormInfo which are
	 *         active.
	 */
	@Override
	public ResponseEntity<List<FormDto>> getAllActiveForms(String tenantId) {
		ResponseEntity<List<FormDto>> responseEntity = null;

		/**
		 *  Handles the exception if occurs during the DAO layer method invokation
		 */
		try {
			List<FormInfo> forms = logbookRepositoryImpl.getAllActiveForms(tenantId);
			/**
			 *  Dto List to get data from FormInfo
			 */
			ArrayList<FormDto> formDtoList = new ArrayList<FormDto>();
			/**
			 *  makes an @FormDto out of each @FormInfo and adds to a @java.util.List
			 */
			forms.forEach(formInfo -> {
				FormDto formDto = new FormDto(formInfo);
				formDtoList.add(formDto);
			});
			responseEntity = ResponseEntity.ok(formDtoList);
		} catch (Exception e) {
			responseEntity = ResponseEntity.internalServerError().build();
		}
		return responseEntity;
	}


	/**
	 * Saves the @java.util.List of @FormInfo 
	 * 
	 * @param @java.util.List of @FormInfo
	 * 
	 * @return @java.util.List of saved @FormInfo data.
	 */
	@Override
	public ResponseEntity<List<FormInfo>> saveLogbook(List<FormInfo> forms, String tenantId) {
		ResponseEntity<List<FormInfo>> responseEntity = null;
		/**
		 *  Handles the exception if occurs during the DAO layer method invokation
		 */
		try {
			List<FormInfo> savedForms = logbookRepositoryImpl.saveLogbook(forms, tenantId);
			responseEntity = ResponseEntity.ok(savedForms);	
		} catch (Exception e) {
			responseEntity = ResponseEntity.internalServerError().build();
		}
		return responseEntity;
	}

	/**
	 * executes the SQL queries recieved in the @param
	 * 	
	 * @param deleteQuery as @java.lang.String
	 * @param insertQuery as @java.lang.String
	 * 
	 * @return all @FormInfo data as @java.util.List
	 */
	@Override
	public ResponseEntity<List<FormInfo>> saveLogbookData(String deleteQuery, String insertQuery, String tenantId) {
		ResponseEntity<List<FormInfo>> responseEntity = null;
		/**
		 *  Handles the exception if occurs during the DAO layer method invokation
		 */
		try {
			List<FormInfo> savedLogbookData = logbookRepositoryImpl.saveLogbookData(deleteQuery, insertQuery, tenantId);
			responseEntity = ResponseEntity.ok(savedLogbookData);
		} catch (Exception e) {
			responseEntity = ResponseEntity.internalServerError().build();
		}
		return responseEntity;
	}

	/**
	 * 
	 */
	@Override
	public ResponseEntity<List<DropDownInfo>> getDropDownInfo(String query, String tenantId){
		if(query.contains("\\n")) {
			while(query.contains("\\n")) {
				query = query.replace("\\n","");
			}
		}
		if(query.charAt(0) == '"') {
			query.replaceAll("\"", "");
		}
		query.trim();
		ResponseEntity<List<DropDownInfo>> responseEntity = null;
		try {
			List<DropDownInfo> dropDownInfo = logbookRepositoryImpl.getDropDownInfo(query, tenantId);
			responseEntity = ResponseEntity.ok(dropDownInfo);
		} catch (Exception e) {
			responseEntity = ResponseEntity.internalServerError().build();
		}
		return responseEntity;
	}



	/**
	 * @author Subhajit Khasnobish Sreepriya Roy
	 * 
	 * saves an instance of @Transaction and fetches the data from @TransactionDto
	 * 
	 * @param takes a transaction to save
	 * 
	 * @return after saving that transaction returns that itself
	 */
	@Override
	public ResponseEntity<TransactionDto> saveTransaction(TransactionDto transactionDto) {
		ResponseEntity<TransactionDto> responseEntity = null;
		/**
		 *  Handles the exception if occurs during the DAO layer method invokation
		 */
		try {
			Transaction savedTransaction = logbookRepositoryImpl.saveTransaction(transactionDto,transactionDto.getTenantid());
			TransactionDto transactionDtoResponse = new TransactionDto(savedTransaction);
			responseEntity = ResponseEntity.ok(transactionDtoResponse);
		} catch (Exception e) {
			responseEntity = ResponseEntity.internalServerError().build();
			System.out.println("Exception : " + e);
		}
		return responseEntity;
	}

	/**
	 * 
	 * @param formName as @java.lang.String of the form which is required here
	 * 
	 * @return a @java.util.List of formInfo by the given formName
	 */
	@Override
	public ResponseEntity<List<FormInfo>> getFormInfoByFormName(String formName,String tenantId) {
		ResponseEntity<List<FormInfo>> responseEntity = null;
		/**
		 *  Handles the exception if occurs during the DAO layer method invokation
		 */
		try {
			List<FormInfo> formInfoWithVersions = logbookRepositoryImpl.getFormInfoByFormName(formName,tenantId);
			responseEntity = ResponseEntity.ok(formInfoWithVersions);
		} catch (Exception e) {
			responseEntity = ResponseEntity.internalServerError().build();
		}
		return responseEntity;
	}


	/**
	 * gets all @CellDetailsDto of the logbook by a formName and versionNumber
	 * 
	 * @param formName of the required formInfo
	 * @param versionNumber of the required formInfo
	 * @param tenantId
	 * @return @java.util.List of @CellDetailsDto for that form
	 */
	@Override
	public ResponseEntity<List<CellDetailsDto>> getAllCellInfoByFormNameAndVersion(String formName, Integer versionNumber,String tenantId) {
		ResponseEntity<List<CellDetailsDto>> responseEntity = null;
		/**
		 *  Handles the exception if occurs during the DAO layer method invokation
		 */
		try {
			List<CellDto> cellList = logbookRepositoryImpl.getAllCellInfoByFormNameAndVersion(formName, versionNumber,tenantId);
			ArrayList<CellDetailsDto> cellDetailsList = new ArrayList<CellDetailsDto>();
			/**
			 *  Filtering the @java.util.List of @CellInfo by rowNum and storing that filtered @java.util.List into
			 *   cellData of @CellDetailsDto
			 */
			if(cellList != null && cellList.get(0).getRowNum() != this.cellRowNum) {
				this.cellRowNum = cellList.get(0).getRowNum();
			}
			while(true) {
				if(cellList != null) {
					List<CellDto> filteredList = cellList.stream().filter(cell -> {
						return cell.getRowNum() == this.cellRowNum;
					}).collect(Collectors.toList());
					if(filteredList.isEmpty()) break;
					else {
						CellDetailsDto cellDetailsDto = new CellDetailsDto();
						cellDetailsDto.setCellData(filteredList);
						cellDetailsList.add(cellDetailsDto);
					}
					this.cellRowNum++;
				}
			}
			responseEntity = ResponseEntity.ok(cellDetailsList);
		} catch (Exception e) {
			responseEntity = ResponseEntity.internalServerError().build();
			e.printStackTrace();
		}
		this.cellRowNum = 1;
		return responseEntity;
	}

	/**
	 * 
	 * @param formName      of form which is required
	 * @param versionNumber of the form is required
	 * @param cellId        of the cell of the form of that formName of that version
	 * 
	 * @return @CellDto details wrapped in @ResponseEntity
	 */
	@Override
	public ResponseEntity<CellDetailsDto> getCellInfo(String formName, Integer versionNumber, String cellId, String tenantId) {
		ResponseEntity<CellDetailsDto> responseEntity = null;
		try {
			CellDto cellDto = logbookRepositoryImpl.getCellInfo(formName, versionNumber, cellId, tenantId);
			CellDetailsDto cellDetailsDto = new CellDetailsDto();
			cellDetailsDto.setCellData(List.of(cellDto));
			responseEntity = ResponseEntity.ok(cellDetailsDto);
		} catch (Exception e) {
			System.out.println("Exception : " + e);
			responseEntity = ResponseEntity.internalServerError().build();
		}
		return responseEntity;
	}


	/**
	 * Dropdown specific API , executes the query and gets the DropDowns info
	 * 
	 * @param query
	 * @return @ResponseEntity of @java.util.List of @DropDownInfo
	 */
	@Override
	public ResponseEntity<List<DropDownInfo>> getDropDownsForQuery(String query, String tenantId){
		ResponseEntity<List<DropDownInfo>> responseEntity = null;
		if(query.contains("\\n")) {
			while(query.contains("\\n")) {
				query = query.replace("\\n","");
			}
		}
		try {
			List<DropDownInfo> listDropDown = logbookRepositoryImpl.getDropDownsforQuery(query, tenantId);
			responseEntity = ResponseEntity.ok(listDropDown);
		}
		catch(Exception e) {
			responseEntity = ResponseEntity.internalServerError().build();
		}
		return responseEntity;
	}


	/**
	 * Executes the recieved query and returns the result as @java.lang.Object
	 * 
	 * @param query
	 * @return @java.lang.Object as result
	 */
	@Override
	public ResponseEntity<Object> getQueryResultForGlobalCellUpdate(String query, String tenantId) throws RuntimeException{
		ResponseEntity<Object> responseEntity = null;
		try {
			Object data = logbookRepositoryImpl.getQueryResultForGlobalCellUpdate(query, tenantId);
			responseEntity = ResponseEntity.ok(data);
		} catch (Exception e) {
			responseEntity = ResponseEntity.internalServerError().body(e);
			e.printStackTrace();
		}
		return responseEntity;
	}

	/**
	 * 
	 * gets @java.util.List of @TransactionDto based on @param
	 * 
	 * @param jobId
	 * @param activityId
	 * @param formName
	 * @param formVersion
	 * @return List of transactions
	 */
	@Override
	public ResponseEntity<List<TransactionDto>> getAllTransactions(String jobId, String activityId, String formName, int formVersion,String tenantId){
		ResponseEntity<List<TransactionDto>> responseEntity = null;
		try {
			List<Transaction> transactions = logbookRepositoryImpl.getAllTransactions(jobId, activityId, formName, formVersion,tenantId);
			ArrayList<TransactionDto> transactionDtoList = new ArrayList<>();
			transactions.forEach((transaction) -> {
				TransactionDto transactionDto = new TransactionDto(transaction);
				ObjectMapper objectMapper = new ObjectMapper();		
				try {
					Map<String,Object> jsonInfo = objectMapper.readValue(transaction.getLogbookData(), Map.class);
					transactionDto.setLogbookData(jsonInfo);
				} catch (JsonMappingException e) {
					e.printStackTrace();
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}
				transactionDtoList.add(transactionDto);
			});
			responseEntity = ResponseEntity.ok(transactionDtoList);
		} catch (Exception e) {
			e.printStackTrace();
			responseEntity = ResponseEntity.internalServerError().build();
		}
		return responseEntity;
	}

	/**
	 * fetches @java.util.List of @CellEditHistory
	 * 
	 * @param cellid
	 * @param jobid
	 * @param activityid
	 * @return @java.util.List of @CellEditHistory
	 */
	@Override
	public ResponseEntity<List<CellEditHistory>> getCellHistory(String cellid, String jobid, String activityid, String tenantId) {
		ResponseEntity<List<CellEditHistory>> responseEntity = null;
		try {
			List<CellEditHistory> cellHistory = logbookRepositoryImpl.getCellHistory(cellid, jobid, activityid, tenantId);
			responseEntity = ResponseEntity.ok(cellHistory);
		} catch (Exception e) {
			e.printStackTrace();
			responseEntity = ResponseEntity.internalServerError().build();
		}
		return responseEntity;
	}

	/**
	 * gets transactions by formname and version
	 * @param formName
	 * @param version
	 * @return
	 */
	@Override
	public ResponseEntity<List<TransactionDto>> getTransactionsByFormNameAndVersion(String formName,int version, String tenantId){
		ResponseEntity<List<TransactionDto>> responseEntity = null;
		try {
			List<Transaction> transactions = logbookRepositoryImpl.getTransactionsByFormNameAndVersion(formName, version, tenantId);
			ArrayList<TransactionDto> transactionDtoList = new ArrayList<>();
			transactions.forEach((transaction) -> {
				TransactionDto transactionDto = new TransactionDto(transaction);
				ObjectMapper objectMapper = new ObjectMapper();
				try {
					Map<String,Object> jsonInfo = objectMapper.readValue(transaction.getLogbookData(), Map.class);
					transactionDto.setLogbookData(jsonInfo);
				} catch (JsonMappingException e) {
					e.printStackTrace();
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}
				transactionDtoList.add(transactionDto);
			});
			responseEntity = ResponseEntity.ok(transactionDtoList);
		} catch (Exception e) {
			e.printStackTrace();
			responseEntity = ResponseEntity.internalServerError().build();
		}
		return responseEntity;
	}

	/**
	 * gets all the logbooks based on userId and tenantId
	 * 
	 * @param userId
	 * @param tenantId
	 * @return List of @java.util.List of Logbooks as @java.util.Map
	 */
	@Override
	public ResponseEntity<List<Map<String,Object>>> getLogbooksByUserId(String userId, String tenantId){
		ResponseEntity<List<Map<String,Object>>> responseEntity = null;
		try {
			List<Map<String, Object>> logbooksByUserId = logbookRepositoryImpl.getLogbooksByUserId(userId, tenantId);
			responseEntity = ResponseEntity.ok(logbooksByUserId);
		} catch (Exception e) {
			e.printStackTrace();
			responseEntity = ResponseEntity.internalServerError().build();
		}
		return responseEntity;
	}

	/**
	 * gets logbooks by user id
	 * 
	 * @param userId
	 * @param tenantId
	 * @return List of logbooks
	 */
	@Override
	public ResponseEntity<List<Map<String,Object>>> getlogbooksByUserIdAndTenantId(String userId,String tenantId){
		ResponseEntity<List<Map<String,Object>>> responseEntity = null;
		try {
			List<Map<String, Object>> logbooks = logbookRepositoryImpl.getlogbooksByUserIdAndTenantId(userId, tenantId);
			responseEntity = ResponseEntity.ok(logbooks);
		} catch (Exception e) {
			e.printStackTrace();
			responseEntity = ResponseEntity.internalServerError().build();
		}
		return responseEntity;
	}


}
