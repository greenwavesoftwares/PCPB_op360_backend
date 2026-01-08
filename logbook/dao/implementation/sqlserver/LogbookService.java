package in.co.greenwave.assetapi.dao.implementation.sqlserver; // Define the package name where this code belongs

import java.util.ArrayList; // Import ArrayList, a special list that can grow in size
import java.util.List; // Import List, a type of collection to hold items
import java.util.Map; // Import Map, a collection of key-value pairs

import org.springframework.beans.factory.annotation.Autowired; // Import the @Autowired annotation to automatically connect components
import org.springframework.beans.factory.annotation.Qualifier; // Import the @Qualifier annotation to specify which bean to use
import org.springframework.jdbc.core.JdbcTemplate; // Import JdbcTemplate to make database interactions easier
import org.springframework.stereotype.Repository; // Import Repository, used to indicate this class is for database operations

import in.co.greenwave.assetapi.dao.LogbookDAO; // Import the LogbookDAO interface
import in.co.greenwave.assetapi.model.FormDetails; // Import the FormDetails model class

// This class is marked as a repository for database operations related to logbooks
@Repository
public class LogbookService implements LogbookDAO {

    // Automatically connect to jdbcTemplate1 bean for database operations
    @Autowired
    @Qualifier("jdbcTemplate1")
    JdbcTemplate jdbcTemplateOp360;

    // Automatically connect to jdbcTemplate2 bean for user module database operations
    @Autowired
    @Qualifier("jdbcTemplate2")
    JdbcTemplate jdbcTemplateOp360Usermodule;

    // This method retrieves all active forms from the database for a specific tenant
    public List<FormDetails> getAllActiveForms(String tenantId) {
        // Define a SQL query to select distinct forms that are active for the specified tenant
        String sql = "SELECT Distinct FormId,FormName,VersionNumber,DocumentID,Department,UserGroup,formatID,isActiveForm FROM [" + tenantId + "].[DigitalLogbookFormInfo] WHERE isActiveForm = 1 AND [TenantId] = ?";

        // Execute the SQL query and store the result in a list of maps, where each map represents a row
        List<Map<String, Object>> rows = jdbcTemplateOp360.queryForList(sql, new Object[]{tenantId});

        // Create an empty list to hold the FormDetails objects
        List<FormDetails> forms = new ArrayList<>();

        // Loop through each row in the result set
        for (Map<String, Object> row : rows) {
            // Create a new FormDetails object using the data from the current row
            FormDetails form = new FormDetails(
                (Integer) row.get("FormId"), // Get FormId and convert it to Integer
                ((String) row.get("FormName")).trim(), // Get FormName, trim spaces, and convert to String
                (Integer) row.get("VersionNumber"), // Get VersionNumber and convert it to Integer
                (String) row.get("DocumentID"), // Get DocumentID and convert it to String
                (String) row.get("Department"), // Get Department and convert it to String
                (String) row.get("UserGroup"), // Get UserGroup and convert it to String
                (String) row.get("FormatID"), // Get FormatID and convert it to String
                (Boolean) row.get("isActiveForm") // Get isActiveForm and convert it to Boolean
            );
            // Add the newly created form to the list of forms
            forms.add(form);
        }

        // Return the complete list of active forms
        return forms;
    }
}
