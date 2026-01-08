package in.co.greenwave.assetapi.dao;

import java.util.List;

import in.co.greenwave.assetapi.model.AssetGroup;
import in.co.greenwave.assetapi.model.AssetModel;

/**
 * AssetConfigurationDAO is an interface that defines methods for interacting with Asset-related data.
 * It allows for retrieving or storing details from a tenant-specific database.
 */
public interface AssetConfigurationDAO {

	// Save the asset tree configuration data to the data source
    public int saveAssetTree(AssetGroup data, String tenantId);

    // Retrieve the asset tree configuration based on the provided group name
    public AssetGroup getAssetTreefromGroupName(String groupName, String tenantId);

    // Retrieve the asset tree configuration based on the provided group ID
    public AssetGroup getAssetTreefromGroupId(String groupId, String tenantId);
    
    // Retrieve an asset from a Group
    public List<AssetModel> getAssetFromGroupId(String groupId, String tenantId);
    
    // Retrieve a list of all asset groups from the data source
    public List<AssetGroup> getAllAssetGroups(String tenantId);
}
