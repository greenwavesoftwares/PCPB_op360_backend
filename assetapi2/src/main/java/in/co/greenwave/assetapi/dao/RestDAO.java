package in.co.greenwave.assetapi.dao; // Declares the package for DAO interfaces

// Import necessary classes for handling lists, maps, and models.
import java.util.List;
import java.util.Map;
import in.co.greenwave.assetapi.model.AssetModel;
import in.co.greenwave.assetapi.model.FormDetails;

/**
 * RestDAO defines an interface for interacting with assets through a RESTful API.
 * It provides methods for fetching, adding, deleting assets, and retrieving logbook data.
 */
public interface RestDAO {

    /**
     * Retrieves a list of asset information from the RESTful API.
     * 
     * @param tenantId The unique identifier of the tenant for which the asset information is being retrieved.
     * @return A list of AssetModel objects containing details about each asset.
     */
    public List<AssetModel> getAssetInfo(String tenantId);

    /**
     * Adds a new asset to the system through the RESTful API.
     * 
     * @param assetGroup The group to which the new asset belongs.
     * @param parentAssetId The ID of the parent asset under which the new asset will be added.
     * @param assetModel The model containing the new asset's details.
     * @param tenantId The unique identifier of the tenant for which the asset is being added.
     * @return The newly created AssetModel after it's added to the system.
     */
    public AssetModel AddNewAsset(String assetGroup, String parentAssetId, AssetModel assetModel, String tenantId);

    /**
     * Deletes an existing asset from the system via the RESTful API.
     * 
     * @param assetGroup The group from which the asset will be deleted.
     * @param AssetId The ID of the asset that is to be deleted.
     * @param tenantId The unique identifier of the tenant for which the asset is being deleted.
     * @return An integer representing the result of the deletion (success/failure).
     */
    public int deleteAnAsset(String assetGroup, String AssetId, String tenantId);

    /**
     * Deletes an asset group from the system via the RESTful API.
     * 
     * @param groupId The ID of the asset group to be deleted.
     * @param tenantId The unique identifier of the tenant for which the asset group is being deleted.
     * @return An integer representing the result of the deletion (success/failure).
     */
    public int deleteAnAssetGroup(String groupId, String tenantId);

    /**
     * Retrieves logbook information for a specific asset.
     * 
     * @param assetId The ID of the asset for which logbook information is being retrieved.
     * @param tenantId The unique identifier of the tenant for which logbook information is being fetched.
     * @return A list of maps containing logbook data related to the asset.
     */
    public List<Map<String, Object>> getLogbooksByAsset(String assetId, String tenantId);

    /**
     * Retrieves detailed asset information for a specific asset ID.
     * 
     * @param assetId The ID of the asset whose information is being retrieved.
     * @param tenantId The unique identifier of the tenant for which asset information is being fetched.
     * @return A map containing key-value pairs of the asset's information.
     */
    Map<String, Object> getAssetInfoByAssetId(String assetId, String tenantId);

	List<FormDetails> getActiveLogbooksByAssetId(String assetId, String tenantId);
	
	public void registerNewTenant(String tenantid);
}
