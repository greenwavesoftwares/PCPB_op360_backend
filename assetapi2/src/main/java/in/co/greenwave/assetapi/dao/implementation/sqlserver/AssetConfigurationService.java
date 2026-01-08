// This is the folder (package) where our code lives.
package in.co.greenwave.assetapi.dao.implementation.sqlserver;

// Importing tools that help us work with the database and data structures like lists and maps.
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

// Importing Spring tools to help us connect to databases and handle errors.
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

// Importing custom classes from our project that we will use to define data models.
import in.co.greenwave.assetapi.dao.AssetConfigurationDAO;
import in.co.greenwave.assetapi.model.AssetGroup;
import in.co.greenwave.assetapi.model.AssetModel;

//This class is marked as a repository for database operations related to assets
// @Repository tag tells Spring this class is meant to interact with a database.
@Repository
public class AssetConfigurationService implements AssetConfigurationDAO {

	// Using two JdbcTemplate tools to connect to two different databases.
	@Autowired
	@Qualifier("DatasourceCollections")
	Map<String, List<JdbcTemplate>> jdbcTemplateCollection; // Connects to another database.

	// This method saves (inserts) an asset tree into the database.
	@Override
	public int saveAssetTree(AssetGroup data, String tenantId) {

		JdbcTemplate jdbcTemplateOp360 = jdbcTemplateCollection.get(tenantId).get(0);

		JdbcTemplate jdbcTemplateOp360Usermodule = jdbcTemplateCollection.get(tenantId).get(1);

		// Getting the ID of the user who created this asset tree.
		String userid = data.getCreatedBy();

		//		// If there's no user ID, we use "test123" as a default.
		//		if (userid == null) {
		//			userid = "test123";
		//		}

		// Deleting an asset group if it already exists.
		String deleteSql = "DELETE FROM [dbo].[AssetGroupInfo] WHERE [GroupId] = ? AND [TenantId] = ?";
		int affectedRows = jdbcTemplateOp360.update(deleteSql, data.getGroupId(), tenantId);

		// If no rows are deleted, it means we're creating a new asset group.
		if (affectedRows == 0) {
			System.out.println("Creating new Asset Tree");
		} else {
			// If rows are deleted, we are modifying an existing asset group.
			System.out.println("Modifying the group " + data.getGroupName());
		}

		// Inserting the new asset group into the database.
		String insertGroupSql = "INSERT INTO [dbo].[AssetGroupInfo] ([GroupId], [GroupName], [CreatedBy], [TenantId], [CreatedOn]) VALUES (?, ?, ?, ?, GETDATE())";
		int affectedRows1 = jdbcTemplateOp360.update(insertGroupSql, data.getGroupId(), data.getGroupName(), userid, tenantId);
		if (affectedRows1 > 0) {
			// If the insert works, we print success.
			System.out.println("Inserted successfully");
		} else {
			// If the insert fails, we print a failure message.
			System.out.println("Insertion failed");
			return 0; // Return immediately if insertion fails
		}

		// SQL query to insert each asset in the asset group.
		String insertAssetSql = "INSERT INTO [dbo].[AssetInfo] ([GroupId],[ParentAssetId],[AssetId],[AssetName], [Icon],[Category],[Image],[NodeOrder],[users],[logbooks],[location],[manufacturer],[model],[serialNo],[description],[uom],[capacity],[TenantId],[department],[groups]) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		try {
			// Loop through each parent asset in the asset group.
			for (AssetModel parent : data.getAssetInfo().keySet()) {
				System.out.println("parent_saveAssetTree : " + parent);
				// Get the list of children for each parent asset.
				List<AssetModel> children = data.getAssetInfo().get(parent);

				// Loop through each child asset.
				for (AssetModel child : children) {
					// These strings will store user, logbook, and group information.
					StringBuilder usersString = new StringBuilder();
					StringBuilder logbooksString = new StringBuilder();
					StringBuilder groupsString = new StringBuilder();

					// Add each user to the usersString.
					if (child.getUsersList() != null && !child.getUsersList().isEmpty()) {
						for (String singleUser : child.getUsersList()) {
							usersString.append(",").append(singleUser);
						}
					}

					// Add each logbook to the logbooksString.
					if (child.getLogbooksList() != null && !child.getLogbooksList().isEmpty()) {
						for (int j = 0; j < child.getLogbooksList().size(); j++) {
							int formid = Integer.parseInt(child.getLogbooksList().get(j).toString());
							logbooksString.append(",").append(formid);
						}
					}

					// Add each group to the groupsString.
					if (child.getGroups() != null && !child.getGroups().isEmpty()) {
						for (String singleGroup : child.getGroups()) {
							if(!singleGroup.equals("")) {
								groupsString.append(",").append(singleGroup);
							}
						}
					}

					// Remove the first comma from each string if it's there.
					if (logbooksString.length() > 0) {
						logbooksString.deleteCharAt(0);
					}
					if (usersString.length() > 0) {
						usersString.deleteCharAt(0);
					}
					if (groupsString.length() > 0) {
						groupsString.deleteCharAt(0);
					}

					// Queries to get form and group data from the databases.
					String query1 = "DECLARE @UserGroupsTable TABLE (Value varchar(max)); SELECT FormId, UserGroup FROM [dbo].[DigitalLogbookFormInfo]";
					String query2 = "DECLARE @usergroup varchar(max) = ?; select gc.GroupId from [dbo].[GroupCredentials] gc cross apply string_split(@usergroup, ',') AS s where gc.GroupName = s.value;";

					// Get the form and group data from the database.
					List<Map<String, Object>> formInfoList = jdbcTemplateOp360.queryForList(query1);
					List<Map<String, Object>> groupIdList = jdbcTemplateOp360Usermodule.queryForList(query2, groupsString.toString());

					// Set to store unique group IDs.
					Set<String> groupIds = new HashSet<>();
					for (Map<String, Object> groupIdMap : groupIdList) {
						groupIds.add((String) groupIdMap.get("GroupId"));
					}

					// List to store the matching form IDs.
					List<Integer> matchingFormIds = new ArrayList<>();
					for (Map<String, Object> formInfoMap : formInfoList) {
						String userGroups = (String) formInfoMap.get("UserGroup");
						if (userGroups != null) {
							String[] userGroupArray = userGroups.split(",");
							for (String userGroup : userGroupArray) {
								if (groupIds.contains(userGroup.trim())) {
									matchingFormIds.add((Integer) formInfoMap.get("FormId"));
									break;
								}
							}
						}
					}

					// Convert the list of matching form IDs to a string.
					String matchingFormIdsString = matchingFormIds.stream().map(String::valueOf).collect(Collectors.joining(","));

					// Print details about the asset being inserted for debugging.
					System.out.println("Inserting asset: " +
							"GroupId=" + data.getGroupId() +
							", ParentAssetId=" + parent.getNodeName() +
							", AssetId=" + child.getNodeName() +
							", AssetName=" + child.getAliasName() +
							", Icon=" + child.getIcon() +
							", Category=" + child.getCategory() +
							", Image=" + child.getImage() +
							", NodeOrder=" + child.getNode_level() +
							", Users=" + usersString.toString() +
							", Logbooks=" + logbooksString.toString() +
							", Location=" + child.getLocation() +
							", Manufacturer=" + child.getManufacturer() +
							", Model=" + child.getModel() +
							", SerialNo=" + child.getSerialNo() +
							", Description=" + child.getDescription() +
							", Uom=" + child.getUom() +
							", Capacity=" + child.getCapacity() +
							", TenantId=" + tenantId +
							", Department=" + child.getDepartment() +
							", Groups=" + groupsString.toString()
							);
					System.out.println("Groups string:"+groupsString);
					// Insert the asset data into the database.
					int affectedRows2 = jdbcTemplateOp360.update(insertAssetSql, data.getGroupId(), parent.getNodeName(), child.getNodeName(),
							child.getAliasName(), child.getIcon(), child.getCategory(), child.getImage(),
							child.getNode_level(), usersString.toString(), 
							matchingFormIdsString,
							child.getLocation(),
							child.getManufacturer(), child.getModel(), child.getSerialNo(), child.getDescription(),
							child.getUom(), child.getCapacity(), tenantId, child.getDepartment(), groupsString);

					if (affectedRows2 <= 0) {
						System.out.println("Failed to insert asset: " + child.getAliasName());
						return 0; // Exit if insertion fails
					}
				}
			}
			// After all assets are inserted, print success.
			System.out.println("saveAssetTree completed successfully");
			return 1;
		} catch (Exception e) {
			System.out.println("Error during asset tree insertion: " + e.getMessage());
			return 0;
		}
	}

	// This method gets a group of assets (called a tree) from the database based on the group's name.
	@Override
	public AssetGroup getAssetTreefromGroupName(String groupName, String tenantId) {

		JdbcTemplate jdbcTemplateOp360 = jdbcTemplateCollection.get(tenantId).get(0);

		// Create an empty object to store the asset group information.
		AssetGroup assetdata = new AssetGroup();

		// SQL query to get asset details like asset ID, name, location, etc., for the given group name.
		String sql = "SELECT a.[GroupId],[GroupName],[ParentAssetId],[AssetId],[AssetName],[Icon],[Category],[Image],a.[users] , a.[logbooks], a.[location] ,a.[manufacturer] ,a.[model] ,a.[serialNo] ,a.[description] ,a.[uom] ,a.[capacity],a.[TenantId] FROM [dbo].[AssetInfo] a JOIN dbo.AssetGroupInfo gr ON a.GroupId = gr.GroupId WHERE gr.GroupName = ? AND a.TenantId = ? ORDER BY [NodeOrder]";

		// Execute the query and process the results row by row.
		jdbcTemplateOp360.query(sql, new RowCallbackHandler() {

			// This method is called to handle each row of the result.
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				// Create a map to store the parent and child assets in the asset tree.
				Map<AssetModel, List<AssetModel>> treeMap = new LinkedHashMap<>();

				// Go through each row of the results.
				while (rs.next()) {
					// Set the GroupId and GroupName for the asset group.
					assetdata.setGroupId(rs.getString("GroupId"));
					assetdata.setGroupName(rs.getString("GroupName"));

					// Get the ID of the parent asset.
					String currentParentAssetId = rs.getString("ParentAssetId");

					// Get more details about the parent asset using its ID.
					AssetModel parent = getAssetInfofromAssetId(currentParentAssetId, assetdata.getGroupId(), tenantId);

					// Create a new asset (child) from the data in the current row.
					AssetModel child = new AssetModel(
							rs.getString("AssetId"),    // Asset ID
							rs.getString("AssetName"),  // Asset Name
							false,                      // Is this a parent? (No, because it's a child)
							rs.getString("Icon"),       // Icon
							rs.getString("Category"),   // Category
							rs.getString("Image")       // Image
							);

					// Get the list of logbooks for this asset (if any).
					String logbooksListString = rs.getString("logbooks");
					// Get the list of users for this asset (if any).
					String usersListString = rs.getString("users");

					// If there are logbooks listed for this asset...
					if (logbooksListString != null) {
						// Split the logbooks by commas to get each logbook separately.
						String[] logbooks = logbooksListString.split("\\s*,\\s*");
						List<Integer> logbooksList = new ArrayList<>();

						// Convert each logbook to an integer and add it to the list.
						for (String book : logbooks) {
							if (!book.trim().equalsIgnoreCase("")) {
								logbooksList.add(Integer.parseInt(book));
							}
						}

						// Set the list of logbooks for this child asset.
						child.setLogbooksList(logbooksList);
					}

					// If there are users listed for this asset...
					if (usersListString != null) {
						// Split the users by commas to get each user separately.
						String[] users = usersListString.split("\\s*,\\s*");

						// Create a list of users and add each one.
						List<String> usersList = new ArrayList<>();
						for (String u : users) {
							usersList.add(u);
						}

						// Set the list of users for this child asset.
						child.setUsersList(usersList);
					}

					// Set other details of the asset like location, manufacturer, model, etc.
					child.setLocation(rs.getString("location"));
					child.setManufacturer(rs.getString("manufacturer"));
					child.setModel(rs.getString("model"));
					child.setSerialNo(rs.getString("serialNo"));
					child.setDescription(rs.getString("description"));
					child.setUom(rs.getString("uom")); // UOM means Unit of Measurement.
					child.setCapacity(rs.getInt("capacity"));

					// Check if the parent asset is already in the tree.
					if (treeMap.keySet().contains(parent)) {
						// If the parent is already there, add this child asset to its list.
						treeMap.get(parent).add(child);
					} else {
						// If the parent isn't there yet, create a new list for the children and add the child.
						List<AssetModel> list = new ArrayList<>();
						list.add(child);
						treeMap.put(parent, list);
					}
				}

				// Set the final asset tree data in the asset group.
				assetdata.setAssetInfo(treeMap);
			}
		}, groupName, tenantId);

		// Return the complete asset group with all the assets (parents and children).
		return assetdata;
	}

	// This method gets a tree of assets from the database based on the group's ID.
	public AssetGroup getAssetTreefromGroupId(String groupId, String tenantId) {

		JdbcTemplate jdbcTemplateOp360 = jdbcTemplateCollection.get(tenantId).get(0);

		// SQL query to get asset information (like asset ID, name, location, etc.) from the database.
		String sql = "SELECT a.[GroupId], [GroupName], [ParentAssetId], [AssetId], [AssetName], [Icon], [Category], [Image], a.[users] ,a.[logbooks], a.[location], a.[manufacturer], a.[model], a.[serialNo], a.[description], a.[uom], a.[capacity], a.[TenantId], a.department, a.groups FROM [dbo].[AssetInfo] a JOIN dbo.AssetGroupInfo gr ON a.GroupId = gr.GroupId WHERE gr.GroupId = ? OR gr.GroupName = ? AND a.TenantId = ? ORDER BY [NodeOrder]";

		// Arguments passed to the SQL query.
		Object[] args = { groupId,groupId, tenantId };

		// Create an empty asset group object to store the retrieved data.
		AssetGroup assetGroup = new AssetGroup();

		// Create a map to store the parent assets and their child assets in a tree structure.
		Map<AssetModel, List<AssetModel>> treeMap = new LinkedHashMap<AssetModel, List<AssetModel>>();

		// Execute the SQL query and get the list of assets from the database.
		List<Map<String, Object>> assetGroupMapList = jdbcTemplateOp360.queryForList(sql, args);

		//Loop through each asset in the result list.
		assetGroupMapList.forEach(assetGroupMap -> {
			AssetModel parent = new AssetModel(); // Create an empty parent asset.
			AssetModel child = new AssetModel(); // Create an empty child asset.

			//Set group ID if it's available in the data.
			if(assetGroupMap.get("GroupId")!=null) {
				assetGroup.setGroupId(assetGroupMap.get("GroupId").toString());
			}

			// Set group name if it's available.
			if(assetGroupMap.get("GroupName")!=null) {
				assetGroup.setGroupName(assetGroupMap.get("GroupName").toString());
			}

			// If the asset has a parent, get the parent's information.
			if(assetGroupMap.get("ParentAssetId")!=null) {
				String currentParentAssetId = assetGroupMap.get("ParentAssetId").toString();
				parent = getAssetInfofromAssetId(currentParentAssetId, assetGroup.getGroupId(), tenantId);
			}

			// Set the child asset's ID, name, and other details.
			if(assetGroupMap.get("AssetId") != null){
				child.setNodeName(assetGroupMap.get("AssetId").toString());
			}
			if(assetGroupMap.get("AssetName") != null) {
				child.setAliasName(assetGroupMap.get("AssetName").toString());
			}
			if(assetGroupMap.get("Icon") != null) {
				child.setIcon(assetGroupMap.get("Icon").toString());
			}
			if(assetGroupMap.get("Category") != null) {
				child.setCategory(assetGroupMap.get("Category").toString());
			}
			if(assetGroupMap.get("Image") != null) {
				child.setImage(assetGroupMap.get("Image").toString());
			}

			child.setChecked(false); // Set 'checked' to false for the child asset.

			// Get logbooks, users, and groups for the child asset (if available).
			String logbooksListString = "";
			String usersListString = "";
			String groupsString = "";


			if(assetGroupMap.get("logbooks")!=null) {
				logbooksListString = assetGroupMap.get("logbooks").toString();
			}
			if(assetGroupMap.get("users")!=null) {
				usersListString = assetGroupMap.get("users").toString();
			}
			if(assetGroupMap.get("groups")!=null  ) {
				if(!assetGroupMap.get("groups").equals("")) {
					System.out.println("Ashok : "+assetGroupMap.get("groups").toString());
					groupsString = assetGroupMap.get("groups").toString();
				}
			}

			// Set the list of logbooks for the child asset.
			if (logbooksListString != null && logbooksListString != "") {
				String[] logbooks = logbooksListString.split("\\s*,\\s*");
				List<Integer> logbooksList = new ArrayList<>();
				for (String book : logbooks) {
					if (!book.trim().equalsIgnoreCase(""))
						logbooksList.add(Integer.parseInt(book));
				}
				child.setLogbooksList(logbooksList);
			}
			else {
				child.setLogbooksList(null); // If no logbooks, set to null.
			}

			// Set the list of users for the child asset.
			if (usersListString != null && usersListString != "") {
				String[] users = usersListString.split("\\s*,\\s*");
				List<String> usersList = new ArrayList<>();
				for (String u : users) {
					usersList.add(u);
				}
				child.setUsersList(usersList);
			}
			else {
				child.setUsersList(new ArrayList<>()); // If no users, set an empty list.
			}

			// Set the list of groups for the child asset.
			if (groupsString != null && !groupsString.isBlank()) {
				String[] groups = groupsString.split("\\s*,\\s*");
				List<String> groupsList = new ArrayList<>();
				if(groups.length!=0) {
					for (String u : groups) {
						groupsList.add(u);
					}
					child.setGroups(groupsList);
				}
				else {child.setGroups(groupsList);}
			}
			else {
				child.setGroups(new ArrayList<>()); // If no groups, set an empty list.
			}

			// Set other asset details (location, manufacturer, model, etc.).
			if(assetGroupMap.get("location")!=null) {
				child.setLocation(assetGroupMap.get("location").toString());
			}
			if(assetGroupMap.get("manufacturer")!=null) {
				child.setManufacturer(assetGroupMap.get("manufacturer").toString());
			}
			if(assetGroupMap.get("model")!=null) {
				child.setModel(assetGroupMap.get("model").toString());
			}
			if(assetGroupMap.get("serialNo")!=null) {
				child.setSerialNo(assetGroupMap.get("serialNo").toString());
			}
			if(assetGroupMap.get("description")!=null) {
				child.setDescription(assetGroupMap.get("description").toString());
			}
			if(assetGroupMap.get("uom")!=null) {
				child.setUom(assetGroupMap.get("uom").toString());
			}
			if(assetGroupMap.get("department")!=null) {
				child.setDepartment(assetGroupMap.get("department").toString());
			}

			// Set the capacity (if available).
			Object capacityObj = assetGroupMap.get("capacity");
			if (capacityObj != null) {
				int capacity = Integer.parseInt(capacityObj.toString());
				child.setCapacity(capacity);
			}

			// Add the child asset to the parent in the tree.
			if (treeMap.keySet().contains(parent)) {
				System.out.println("Inside if:"+child);
				treeMap.get(parent).add(child);
			} else {
				List<AssetModel> list = new ArrayList<AssetModel>();
				System.out.println("Inside else:"+child);
				list.add(child);
				treeMap.put(parent, list);
			}
		});

		//Set the tree of assets into the asset group.
		assetGroup.setAssetInfo(treeMap);

		// Print the asset group (for debugging purposes).
		System.err.println(assetGroup);

		// Return the final asset group.
		return assetGroup;
	}

	// This method gets information about an asset based on its ID and group ID
	private AssetModel getAssetInfofromAssetId(String assetId, String groupId, String tenantId) {

		JdbcTemplate jdbcTemplateOp360 = jdbcTemplateCollection.get(tenantId).get(0);

		// Initialize the AssetModel to null
		AssetModel model = null;

		// Print the group ID and asset ID to the console for tracking (like a note to remember what we're looking for)
		System.out.println(groupId+" AssetConfigurationService.getAssetInfofromAssetId() "+assetId);

		// Prepare a special question (SQL query) to ask the database for information about the asset
		String sql = "SELECT  [GroupId],[ParentAssetId],[AssetId],[AssetName],[Icon],[Category],[Image],[users] ,[logbooks]  FROM [dbo].[AssetInfo] Where [AssetId] = ? AND [GroupId] = ?";

		// Ask the database our question and get back the asset information
		model = jdbcTemplateOp360.query(sql, new ResultSetExtractor<AssetModel>() {

			// Initialize an inner AssetModel
			AssetModel innerModel = null;

			// This method is called to get the data
			@Override
			public AssetModel extractData(ResultSet rs) throws SQLException, DataAccessException {
				// Process each row in the result set
				while (rs.next()) {
					// Retrieve logbooks and users lists from the result set
					String logbooksListString = rs.getString("logbooks");
					String usersListString = rs.getString("users");

					// Create a new inner AssetModel with basic information
					innerModel = new AssetModel(rs.getString("AssetId"), rs.getString("AssetName"), false,
							rs.getString("Icon"), rs.getString("Category"), rs.getString("Image"));

					// Parse logbooks list and set it in the inner AssetModel
					if (logbooksListString != null) {
						List<Integer> logbooksList = new ArrayList<>();
						List<String> logbooks = Arrays.asList(logbooksListString.split("\\s*,\\s*"));
						logbooks.stream().filter(l -> !l.trim().equalsIgnoreCase(""))
						.forEach(l -> logbooksList.add(Integer.parseInt(l)));// Convert each logbook to integers and adding them to logbooklist
						innerModel.setLogbooksList(logbooksList); // Store logbooks in innerModel
					}

					// Parse users list and set it in the inner AssetModel
					if (usersListString != null) {
						List<String> usersList = new ArrayList<>();
						List<String> users = Arrays.asList(usersListString.split("\\s*,\\s*"));
						usersList.addAll(users); // Add all users userList
						innerModel.setUsersList(usersList); // Store userList in innerModel
					}
				}

				// If innerModel is still null, create a default AssetModel with groupId as AssetId
				if (innerModel == null)
					innerModel = new AssetModel(groupId, groupId, false, null, null, null);

				// Return the inner AssetModel
				return innerModel;
			}
		}, assetId, groupId);

		// Return the retrieved AssetModel
		return model;
	}

	// This method gets a list of all asset groups from the database
	@Override
	public List<AssetGroup> getAllAssetGroups(String tenantId) {

		JdbcTemplate jdbcTemplateOp360 = jdbcTemplateCollection.get(tenantId).get(0);

		// Ask the database for all asset groups and return them
		return jdbcTemplateOp360.query("SELECT [GroupId],[GroupName],[CreatedBy],[CreatedOn],[TenantId] FROM [dbo].[AssetGroupInfo] WHERE [TenantId] = ?",
				// Lambda expression to map each row in the result set to a new AssetGroup object
				(rs, rowNum) -> new AssetGroup(
						rs.getString("GroupName"), // name of the group
						rs.getString("GroupId"), // Id of the group
						rs.getString("CreatedBy"), // who created the group
						rs.getString("CreatedOn")), // when it was created
				tenantId // TenantId
				);
	}

	// This method return List of Assets from GroupId
	@Override
	public List<AssetModel> getAssetFromGroupId(String groupId, String tenantId) {

		JdbcTemplate jdbcTemplateOp360 = jdbcTemplateCollection.get(tenantId).get(0);

		// Query to get all information from assetInfo table using groupId and TenantId
		String sql = "SELECT [GroupId], [ParentAssetId], [NodeOrder], [AssetId], [AssetName], [Icon], [Category], [Image], [users], [logbooks], [location], [manufacturer], [model], [serialNo], [description], [uom], [capacity] ,[TenantId] FROM [OP360_PCPB].[dbo].[AssetInfo] WHERE GroupId = ? AND TenantId = ?";

		// Arguments passed to the SQL query.
		Object[] args = { groupId, tenantId };

		List<AssetModel> assetList = new ArrayList<>(); // Initializing list of assetModel in the name of assetList

		List<Map<String, Object>> assetMapList = jdbcTemplateOp360.queryForList(sql, args); // Executing the sql query and storing the result.

		// loop through the result to get each asset and store it in a list.
		for (Map<String, Object> assetMap : assetMapList) {

			AssetModel asset = new AssetModel(); // Initializing an empty asset

			asset.setNodeName((String) assetMap.get("AssetName")); // setting nodename of the asset
			asset.setAliasName((String) assetMap.get("AssetName")); // setting alisename of the asset
			asset.setIcon((String) assetMap.get("Icon")); // setting icon of the asset
			asset.setCategory((String) assetMap.get("Category")); // setting category of the asset
			asset.setImage((String) assetMap.get("Image")); // setting image of the asset
			asset.setLocation((String) assetMap.get("location")); // setting location of the asset
			asset.setManufacturer((String) assetMap.get("manufacturer")); // setting manufacture of the asset
			asset.setModel((String) assetMap.get("model")); // setting model of the asset
			asset.setSerialNo((String) assetMap.get("serialNo")); // setting serial number of the asset
			asset.setDescription((String) assetMap.get("description")); // setting description of the asset
			asset.setUom((String) assetMap.get("uom")); // setting UOM of the asset
			asset.setCapacity((Integer) assetMap.get("capacity")); // setting capacity of the asset

			// Assuming users are stored as comma-separated strings in the database
			String usersString = (String) assetMap.get("users");
			if (usersString != null && !usersString.isEmpty()) { // checking if the user string is null or empty

				// Split the logbooks string into an list
				List<String> usersList = new ArrayList<>(Arrays.asList(usersString.split(",")));
				asset.setUsersList(usersList); // setting users of the asset
			}

			// Assuming logbooks are stored as comma-separated strings in the database
			String logbooksString = (String) assetMap.get("logbooks");
			if (logbooksString != null && !logbooksString.isEmpty()) { // checking if the logbook string is null or empty

				// Split the logbooks string into an array
				String[] logbooksArray = logbooksString.split(",");

				List<Integer> logbooksList = new ArrayList<>(); // Initializing a list to store all the logbooks

				// Convert each logbook string into an integer and add it to the list
				for (String logbook : logbooksArray) {
					logbooksList.add(Integer.parseInt(logbook.trim()));
				}
				asset.setLogbooksList(logbooksList); // setting logbooks of the asset
			}
			assetList.add(asset); // storing each asset in a list named assetList.
		}
		return assetList; // returning the final list of asset
	}

}
