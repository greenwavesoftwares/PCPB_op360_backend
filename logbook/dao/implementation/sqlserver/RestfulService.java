package in.co.greenwave.assetapi.dao.implementation.sqlserver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import in.co.greenwave.assetapi.dao.RestDAO;
import in.co.greenwave.assetapi.model.AssetModel;
import in.co.greenwave.assetapi.model.FormDetails;

//This class is marked as a repository for database operations 
@Repository
public class RestfulService implements RestDAO {

	// using two databases
	// jdbcTemplateOp360 connects to the first database. (op360)
	@Autowired
	@Qualifier("jdbcTemplate1")
	JdbcTemplate jdbcTemplateOp360;

	// jdbcTemplateOp360Usermodule connects to the second database. (op360usermodule)
	@Autowired
	@Qualifier("jdbcTemplate2")
	JdbcTemplate jdbcTemplateOp360Usermodule;

	// This function gets a list of assets for a particular tenant (a tenant is like a building owner or a customer).
	@Override
	public List<AssetModel> getAssetInfo(String tenantId) {
		// SQL is the command send to the database to get the needed data.
		String sql = "SELECT [AssetId],[AssetName],[logbooks],[location],[manufacturer] FROM ["+tenantId+"].[AssetInfo] WHERE [TenantId] = ?";

		// asking the database to give a list of assets for this tenant.
		List<Map<String, Object>> queryForList = jdbcTemplateOp360.queryForList(sql,new Object[]{tenantId}); 

		List<AssetModel> assetModels = new ArrayList<>(); // Initializing a list of AssetModel in the name of assetModels.

		// going through each row of data (each asset) and make it into an AssetModel object.
		for (Map<String, Object> row : queryForList) {

			// setting the name and details of the asset (like its name, location, and manufacturer).
			AssetModel assetModel = new AssetModel();
			assetModel.setNodeName((String) row.get("AssetId"));
			assetModel.setAliasName((String) row.get("AssetName"));
			assetModel.setLocation((String) row.get("location"));
			assetModel.setManufacturer((String) row.get("manufacturer"));

			// This part handles logbooks (special record books for the asset).
			Object logbooksObj = row.get("logbooks");

			if (logbooksObj instanceof String) {
				String logbooksStr = (String) logbooksObj;
				String[] logbookIds = logbooksStr.split(",");
				List<Integer> logbooksIntegers = new ArrayList<>();

				// turning the logbook IDs into a list of numbers.
				for (String logbookId : logbookIds) {
					if (!logbookId.trim().isEmpty()) {
						try {
							Integer id = Integer.parseInt(logbookId.trim());
							logbooksIntegers.add(id);
						} catch (NumberFormatException e) {
							System.err.println("Error parsing logbook ID: " + logbookId + " e : " + e);
						}
					}
				}
				assetModel.setLogbooksList(logbooksIntegers);
			}

			// Add this asset to the list.
			assetModels.add(assetModel);
		}

		// Return the list of all assets.
		return assetModels;
	}

	// This function gets information about one asset based on its ID.
	@Override
	public Map<String, Object> getAssetInfoByAssetId(String assetId, String tenantId) {
		// SQL query to get details about an asset.
		String sql = "SELECT [GroupId], [ParentAssetId], [NodeOrder], [AssetId], [AssetName], [Icon], [Category], [Image], [users], [logbooks], [location], [manufacturer], [model], [serialNo], [description], [uom], [capacity], [TenantId], [department], [groups] FROM [" + tenantId + "].[AssetInfo] WHERE [TenantId] = ? AND [AssetId] = ?";

		try {
			// asking the database to give the details for this asset.
			Map<String, Object> row = jdbcTemplateOp360.queryForMap(sql, tenantId, assetId);
			Map<String, Object> assetMap = new HashMap<>();

			// storeing the details about this asset in assetMap (like name, location, etc.).
			assetMap.put("id", (String) row.get("AssetId"));
			assetMap.put("assetName", (String) row.get("AssetName"));
			assetMap.put("icon", (String) row.get("Icon"));
			assetMap.put("category", (String) row.get("Category"));
			assetMap.put("image", (String) row.get("Image"));
			assetMap.put("location", (String) row.get("location"));
			assetMap.put("manufacturer", (String) row.get("manufacturer"));
			assetMap.put("model", (String) row.get("model"));
			assetMap.put("serialno", (String) row.get("serialNo"));
			assetMap.put("description", (String) row.get("description"));
			assetMap.put("unit", (String) row.get("uom"));
			assetMap.put("capacity", (int) row.get("capacity"));
			assetMap.put("department", (String) row.get("department"));

			// Parse logbooks (separate the list of logbooks into individual items).
			Object logbooksObj = row.get("logbooks");
			if (logbooksObj instanceof String) {
				String logbooksStr = (String) logbooksObj;
				String[] logbookIds = logbooksStr.split(",");
				List<Integer> logbooksIntegers = new ArrayList<>();
				for (String logbookId : logbookIds) {
					if (!logbookId.trim().isEmpty()) {
						try {
							Integer id = Integer.parseInt(logbookId.trim());
							logbooksIntegers.add(id);
						} catch (NumberFormatException e) {
							System.err.println("Error parsing logbook ID: " + logbookId + " e : " + e);
						}
					}
				}
				assetMap.put("logbook", (logbooksIntegers));
			}

			// Parse users (turn the list of users into individual names).
			Object usersObj = row.get("users");
			if (usersObj instanceof String) {
				String usersStr = (String) usersObj;
				List<String> usersList = Arrays.asList(usersStr.split(","));
				assetMap.put("users", (usersList));
			}

			// Parse groups (split the groups into a list of names).
			Object groupsObj = row.get("groups");
			if (groupsObj instanceof String) {
				String groupsStr = (String) groupsObj;
				List<String> groupsList = Arrays.asList(groupsStr.split(","));
				assetMap.put("groups", (groupsList));
			}

			return assetMap;  // Return the asset details.

		} catch (EmptyResultDataAccessException e) {
			// If no asset is found, show an error message.
			System.err.println("No asset found with assetId: " + assetId + " and tenantId: " + tenantId);
			return null;  // Handle empty result scenario
		}
	}

	// This function gets logbooks (records) for a specific asset.
	@Override
	public List<Map<String, Object>> getLogbooksByAsset(String assetId, String tenantId) {
		// SQL query to get logbooks related to this asset.
		String sql = "SELECT [AssetId], [AssetName], Item, (Select FormName from "+tenantId+".DigitalLogbookFormInfo where FormId = Item) as LogbookName, [TenantId], [department], [groups] FROM ["+tenantId+"].[AssetInfo] Cross Apply "+tenantId+".fnSplitString([logbooks],',') where AssetId = ? and TenantId = ?";
		// Ask the database to give the logbooks related to this asset.
		return jdbcTemplateOp360.queryForList(sql, assetId, tenantId);
	}

	@Override
	public List<FormDetails> getActiveLogbooksByAssetId(String assetId, String tenantId) {

		// SQL query to get logbooks related to this asset.
		String sql = "SELECT DISTINCT [FormId], [FormName], [UserID], [SaveSQL], [TableSQL], [DeleteSQL], [CreationDate], [CreatedUser], [Department], [UserGroup], [DocumentID], [FormatID], [VersionNumber],[isActiveForm], [TenantId] FROM "+tenantId+".[DigitalLogbookFormInfo] a cross apply GWSW.fnSplitString(a.[UserGroup],',') b WHERE b.Item in ( SELECT * FROM GetGroupIdsByAssetId(?) ) AND isActiveForm = 1 AND TenantId = ?";

		// Initialize an empty list for FormDetails
		List<FormDetails> formDetailsList = new ArrayList<>();

		try {
			// Query the database and get the result as a list of maps
			List<Map<String, Object>> rows = jdbcTemplateOp360.queryForList(sql, assetId, tenantId);

			// Iterate over each row and map it to a FormDetails object
			for (Map<String, Object> row : rows) {
				FormDetails formDetails = new FormDetails();
				formDetails.setFormID(row.get("FormId") != null ? Integer.parseInt(row.get("FormId").toString()) : 0);
				formDetails.setFormName(row.get("FormName") != null ? row.get("FormName").toString() : null);
				formDetails.setDepartment(row.get("Department") != null ? row.get("Department").toString() : null);
				formDetails.setSelectedGrp(row.get("UserGroup") != null ? row.get("UserGroup").toString() : null);
				formDetails.setCreatedUser(row.get("CreatedUser") != null ? row.get("CreatedUser").toString() : null);
				formDetails.setFormatID(row.get("FormatID") != null ? row.get("FormatID").toString() : null);
				formDetails.setVersionNumber(row.get("VersionNumber") != null ? Integer.parseInt(row.get("VersionNumber").toString()) : 0);
				formDetails.setIsActiveForm(row.get("isActiveForm") != null ? Boolean.valueOf(row.get("isActiveForm").toString()) : false);
				formDetails.setDocumentId(row.get("DocumentID") != null ? row.get("DocumentID").toString() : null);
				formDetails.setTableSQL(row.get("TableSQL") != null ? row.get("TableSQL").toString() : null);
				formDetails.setInsertSql(row.get("SaveSQL") != null ? row.get("SaveSQL").toString() : null);
				formDetails.setDeleteSQL(row.get("DeleteSQL") != null ? row.get("DeleteSQL").toString() : null);
				formDetailsList.add(formDetails);
			}
		} catch (DataAccessException e) {
			
			// Log the database access exception
			System.err.println("Database access error while fetching logbooks: " + e.getMessage());
			e.printStackTrace(); // Log stack trace for debugging
		} catch (Exception e) {
			// Log any other exception
			System.err.println("Unexpected error while fetching logbooks: " + e.getMessage());
			e.printStackTrace(); // Log stack trace for debugging
		}

		return formDetailsList;
	}


	// This function adds a new asset to the system.
	@Override
	public AssetModel AddNewAsset(String assetGroup, String parentAssetId, AssetModel assetModel, String tenantId) {

		try {
			// SQL query to insert a new asset into the database.
			String insertAssetSql = "INSERT INTO ["+tenantId+"].[AssetInfo] ( [GroupId],[AssetId],[logbooks],[ParentAssetId],[AssetName] ,[Icon],[Category],[users],[location],[manufacturer],[model],[serialNo],[description],[uom],[capacity],[TenantId]) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

			// These strings will store users and logbooks as comma-separated values.
			StringBuilder usersString = new StringBuilder("");
			StringBuilder logbooksString = new StringBuilder("");

			// Convert users list into a comma-separated string.
			if (assetModel.getUsersList() != null) {
				for (String singleUser : assetModel.getUsersList()) {
					usersString.append("," + singleUser);
				}
			}

			// Convert logbooks list into a comma-separated string.
			if (assetModel.getLogbooksList() != null) {
				for (int i = 0; i < assetModel.getLogbooksList().size(); i++) {
					// Convert each logbook entry to an integer and append to logbooksString
					int formId = Integer.parseInt(assetModel.getLogbooksList().get(i).toString());
					logbooksString.append("," + formId);
				}
			}

			// Remove leading comma if present in the logbooksString and usersString
			if (logbooksString.length() > 0) {
				logbooksString.deleteCharAt(0);
			}

			if (usersString.length() > 0) {
				usersString.deleteCharAt(0);
			}

			// Execute the update query to insert the new asset into the AssetInfo table
			// Prepare the data and execute the SQL to add the asset.
			jdbcTemplateOp360.update(
					insertAssetSql,
					assetGroup,
					assetModel.getNodeName(),
					logbooksString,
					parentAssetId,
					assetModel.getAliasName(),
					assetModel.getIcon(),
					assetModel.getCategory(),
					usersString,
					assetModel.getLocation(),
					assetModel.getManufacturer(),
					assetModel.getModel(),
					assetModel.getSerialNo(),
					assetModel.getDescription(),
					assetModel.getUom(),
					assetModel.getCapacity(),
					tenantId
					);

		} catch (Exception e) {

			// If something goes wrong, show an error message.
			e.printStackTrace();
		}

		// If everything is successful, return the assetModel.
		return assetModel;
	}

	// Delete an asset from the database based on the provided asset group and asset ID
	@Override
	public int deleteAnAsset(String assetGroup, String assetId, String tenantId) {
		// assuming no rows (assets) will be affected (deleted)
		int rowsAffected = 0;
		try {
			// This is a query (like a question) ask the database to get the GroupId for a specific asset group and tenant
			String groupIdQuery = "SELECT [GroupId] FROM ["+tenantId+"].[AssetGroupInfo] WHERE GroupName = ? AND [TenantId] = ?";

			// This query will delete an asset from the AssetInfo table using the GroupId, AssetId, and TenantId
			String deleteQuery = "DELETE FROM ["+tenantId+"].[AssetInfo] WHERE [GroupId] = ? AND [AssetId] = ? AND [TenantId] = ?";

			// asking the database to give the GroupId for the provided asset group and tenant
			String groupId = jdbcTemplateOp360.queryForObject(groupIdQuery, String.class, assetGroup, tenantId);

			//			// Print the GroupId, assetGroup, and assetId to check (debug) if the values are correct
			//			System.out.println("groupId : " + groupId);
			//			System.out.println("assetGroup : " + assetGroup);
			//			System.out.println("assetId : " + assetId);

			// Run the delete query and store how many rows (assets) were affected (deleted)
			rowsAffected = jdbcTemplateOp360.update(deleteQuery, groupId, assetId, tenantId);

			//			// Print the number of rows affected to check if anything was deleted
			//			System.out.println(rowsAffected);

			// If rowsAffected is more than 0, that means the asset was deleted successfully
			if (rowsAffected > 0) {
				System.out.println("Asset deleted successfully.");
			} else {
				// If rowsAffected is 0, it means no asset was found to delete
				System.out.println("No matching asset found for deletion.");
			}

		} catch (Exception e) {
			// If something goes wrong, print the error to understand what happened
			e.printStackTrace();
		}
		// Return the number of rows that were affected (deleted)
		return rowsAffected;
	}

	// Delete an asset group from the database based on the provided group ID
	@Override
	public int deleteAnAssetGroup(String groupId, String tenantId) {
		// Start by assuming no rows (groups) will be affected (deleted)
		int rowsAffected = 0;
		try {
			// This query will delete an asset group from the AssetGroupInfo table using the GroupId and TenantId
			String deleteQuery = "DELETE FROM ["+tenantId+"].[AssetGroupInfo] WHERE [GroupId] = ? AND [TenantId] = ?";

			// Print the GroupId to check (debug) if the value is correct
			System.out.println("Deleting groupId : " + groupId);

			// Run the delete query and store how many rows (groups) were affected (deleted)
			rowsAffected = jdbcTemplateOp360.update(deleteQuery, groupId, tenantId);

			// Print the number of rows affected to check if any group was deleted
			System.out.println(rowsAffected);

			// If rowsAffected is more than 0, that means the asset group was deleted successfully
			if (rowsAffected > 0) {
				System.out.println("AssetGroup deleted successfully.");
			} else {
				// If rowsAffected is 0, it means no asset group was found to delete
				System.out.println("No matching group found for deletion.");
			}

		} catch (Exception e) {
			// If something goes wrong, print the error to understand what happened
			e.printStackTrace();
		}
		// Return the number of rows that were affected (deleted)
		return rowsAffected;
	}
}




