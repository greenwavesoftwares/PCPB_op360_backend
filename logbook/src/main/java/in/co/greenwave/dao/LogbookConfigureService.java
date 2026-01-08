package in.co.greenwave.dao;

import java.util.List; // Import the List class for handling lists of items
import java.util.Map; // Import the Map class for handling key-value pairs

import org.springframework.core.io.InputStreamResource; // Import for handling input streams
import org.springframework.http.ResponseEntity; // Import for building HTTP responses
import org.springframework.web.multipart.MultipartFile;

import in.co.greenwave.dto.CellDto; // Data Transfer Object for cell data
import in.co.greenwave.dto.FormDto;
import in.co.greenwave.dto.ReportConfigDto; // Data Transfer Object for report configuration
import in.co.greenwave.dto.ReportData; // Data Transfer Object for report data
import in.co.greenwave.dto.TransactionDto; // Data Transfer Object for transaction data
import in.co.greenwave.entity.FormInfo; // Entity representing form information
import in.co.greenwave.entity.UserEntity; // Entity representing a user
import in.co.greenwave.entity.UserModel; // Model representing user information

/**
 * This interface defines the operations for managing logbook configuration.
 * It serves as a blueprint for the LogbookConfigureService implementation.
 */
public interface LogbookConfigureService {
//	
//	public Map<String, List<FormDto>> readExcel(MultipartFile file);
//
//	public void saveForm(FormDto formInfo,String tenantId,boolean isUpdate);
//	public FormDto getFormDetailsByFormName(String tenantId,String formName);
//	public void updateForm(FormDto formInfo,String tenantId);
//	public void  updateActiveStatus(FormDto formInfo,String tenantId,boolean isDeactivateOtherVersion);
//}


    /**
     * Reads the provided Excel file and extracts form details.
     * 
     * @param file The uploaded Excel file as a @MultipartFile
     * @return A @Map with a String key representing a category and a @java.util.List of @FormDto
     */
    public Map<String, List<FormDto>> readExcel(MultipartFile file);

    /**
     * Saves a new form or updates an existing form based on the input (basically an Excel Sheet).
     * 
     * @param formInfo The form details as a @FormDto
     * @param tenantId The tenant identifier
     * @param isUpdate A boolean flag indicating if the operation is updated
     */
    public void saveForm(FormDto formInfo, String tenantId, boolean isUpdate);

    /**
     * Fetches details of a form using its name and tenant.
     * 
     * @param tenantId The tenant identifier
     * @param formName The name of the form
     * @return The @FormDto containing form details
     */
    public FormDto getFormDetailsByFormName(String tenantId, String formName);

    /**
     * Updates the details of an existing form.
     * 
     * @param formInfo The updated form details as a @FormDto
     * @param tenantId The tenant identifier
     */
    public void updateForm(FormDto formInfo, String tenantId);

    /**
     * Updates the active status of a form, with an option to deactivate other versions.
     * 
     * @param formInfo The form details as a @FormDto
     * @param tenantId The tenant identifier
     * @param isDeactivateOtherVersion A boolean flag indicating if other versions should be deactivated
     */
    public void updateActiveStatus(FormDto formInfo, String tenantId, boolean isDeactivateOtherVersion);
    
    public void registerNewTenant(String tenantid);
    public FormDto getLatestForm(String tenantId);
    

}

