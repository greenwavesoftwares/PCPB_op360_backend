package in.co.greenwave.UserGroup.dao;

import java.util.List;
import java.util.Map;

import in.co.greenwave.UserGroup.model.JobDetails;
import in.co.greenwave.UserGroup.model.PlantModel;
import in.co.greenwave.UserGroup.model.UserModel;

public interface RestDAO {
	
	// Retrieves a list of job details that have been published, along with their associated job IDs.
    public List<JobDetails> getPublishedTasksWithJobId();
    
    // Fetches the details of all plants from the database.
    public List<PlantModel> getPlantInfo();

    // Adds a new plant record to the database.
    public boolean addNewPlant(PlantModel plantModel);
    
    // Retrieves detailed information of a specific plant based on the plant's ID.
    public PlantModel getAnPlantInfo(String plantId);
    
    // Deletes a plant record from the database using its plant ID.
    public boolean deletePlantByPlantId(String plantId);
    
    // Finds and returns a user based on their tenant ID.
    public UserModel findByTenantId(String tenantId);
    
    // Saves a new user or updates an existing user in the database.
    public UserModel saveUser(UserModel userModel);

    // Finds and returns a user based on their email and tenant ID.
    UserModel findByUserEmail(String email, String tenantId);

	void registerNewTenant(String tenantid);

	Map<String, String> checkTenantExpiration(String tenant);

}
