package in.co.greenwave.taskapi.dao;

import java.util.List;
import java.util.Map;

import in.co.greenwave.taskapi.model.User;

public interface AssetConfigurationDAO {
	
	public List<User> getUserDeptOrGroupwsie(String selectedInput , String deptOrGroupSelected);


//	public boolean checkIfGroupAlreadyExists(String groupName);
//	public void saveAssetTree(AssetGroup data,String userid);
//	public AssetGroup getAssetTreefromGroupName(String groupName);
//	public AssetGroup getAssetTreefromGroupId(String groupId);
//	public void deleteTreeMap(String assetId);
//	public List<AssetGroup> getAllAssetGroups();
//	public TreeNode traveseTree(AssetModel child,TreeNode parent,Map<AssetModel, List<AssetModel>> treeMap);
//	public TreeNode constructAssetTreeFromMap(String groupId,String groupName,TreeNode assetRoot,Map<AssetModel,List<AssetModel>> treeMap);
//	public Map<String , String> getAllAssetsFromGroupMappedWithAssetId(String groupName);
//	public List<AssetModel> getAllAssetPositionFromGroupName(String groupName);
//	public void saveAssetPosition(DefaultDiagramModel assetLocationOnMap, String assetGroup );
//	public void addAssetMap(String userId , String mapImage);
//	public String getAssetMap(String groupId);

}
