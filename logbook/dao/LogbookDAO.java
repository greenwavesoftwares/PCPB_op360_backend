package in.co.greenwave.assetapi.dao; // Declares the package for DAO interfaces

// Importing the FormDetails model, which represents the structure of a form
import java.util.List;
import in.co.greenwave.assetapi.model.FormDetails;

/**
 * LogbookDAO is an interface that defines methods for interacting with logbook-related data.
 * It allows for retrieving form details from a tenant-specific database.
 */
public interface LogbookDAO {

    /**
     * Retrieves a list of all active forms for a given tenant.
     * 
     * @param tenantId The unique identifier of the tenant for which active forms are being retrieved.
     * @return A list of FormDetails objects containing information about active forms.
     */
    public List<FormDetails> getAllActiveForms(String tenantId);
}
