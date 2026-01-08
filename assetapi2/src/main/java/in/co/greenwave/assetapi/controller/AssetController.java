package in.co.greenwave.assetapi.controller;

import java.time.LocalDateTime; // Used to get the current date and time
import java.time.format.DateTimeFormatter; // Used to format the date and time
import java.util.ArrayList; // List implementation to store many items
import java.util.HashMap; 
import java.util.LinkedHashMap; // Ordered map for key-value pairs
import java.util.LinkedList; // List implementation for dynamic data
import java.util.List; // List interface
import java.util.Map; // Map interface for key-value pairs

import org.springframework.beans.factory.annotation.Autowired; // Used to automatically add objects
import org.springframework.http.HttpStatus; // HTTP status codes (like 200, 404, 500)
import org.springframework.http.ResponseEntity; // Used to return HTTP responses
import org.springframework.web.bind.annotation.DeleteMapping; // Mapping DELETE HTTP requests
import org.springframework.web.bind.annotation.GetMapping; // Mapping GET HTTP requests
import org.springframework.web.bind.annotation.PathVariable; // Capture parts of the URL
import org.springframework.web.bind.annotation.PostMapping; // Mapping POST HTTP requests
import org.springframework.web.bind.annotation.RequestBody; // Capture request data in the body
import org.springframework.web.bind.annotation.RequestMapping; // Base URL mapping
import org.springframework.web.bind.annotation.RequestParam; // Get URL parameters
import org.springframework.web.bind.annotation.RestController; // Makes this class a REST controller

import com.fasterxml.jackson.databind.ObjectMapper; // Helps convert between objects and JSON

import in.co.greenwave.assetapi.dao.AssetConfigurationDAO; // DAO for asset configuration
import in.co.greenwave.assetapi.dao.LogbookDAO; // DAO for logbook management
import in.co.greenwave.assetapi.dao.RestDAO; // DAO for RESTful services
import in.co.greenwave.assetapi.dao.factory.DAOFactory; // Factory to get DAO services
import in.co.greenwave.assetapi.model.AssetGroup; // Asset group model
import in.co.greenwave.assetapi.model.AssetModel; // Asset model
import in.co.greenwave.assetapi.model.FormDetails; // Form details model
import in.co.greenwave.assetapi.model.LogbookInfo; // Logbook info model
import in.co.greenwave.assetapi.model.TreeData; // Tree structure data model


@RestController // This class is a REST controller that handles API requests
@RequestMapping("/api") // All endpoints start with "/api"
public class AssetController {

	@Autowired // Automatically injects the DAOFactory object
	private DAOFactory factory;

	private AssetConfigurationDAO assetDao; // Used to interact with asset configurations

	private LogbookDAO logbookDao; // Used to manage logbooks

	private RestDAO restdao; // Used to call other services

	// ------------------------ Methods ------------------------

	// Converts a list of tree data into an AssetGroup object
	public AssetGroup convertTreeDataListToAssetGroup(List<TreeData> treeDataList, String createdby) {
		LocalDateTime currentDateTime = LocalDateTime.now(); // Get the current time
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"); // Time format
		String formattedDateTime = currentDateTime.format(formatter); // Format the current time to set createdon in a assetgroup

		AssetGroup assetGroup = new AssetGroup(); // Create a new AssetGroup object
		assetGroup.setGroupName(treeDataList.get(0).getAssetName()); // Set the group name
		assetGroup.setGroupId(treeDataList.get(0).getId()); // Set the group ID
		assetGroup.setCreatedOn(formattedDateTime); // Set the creation time
		assetGroup.setCreatedBy(createdby); // Set who created the group

		Map<AssetModel, List<AssetModel>> assetMap = new LinkedHashMap<>(); // Map for asset models
		traverse(treeDataList.get(0), assetMap); // Traverse the tree to build asset models
		assetGroup.setAssetInfo(assetMap); // Set asset info in the group
		return assetGroup; // Return the AssetGroup
	}

	// Traverses tree data to build a map of AssetModels
	private Map<AssetModel, List<AssetModel>> traverse(TreeData asset, Map<AssetModel, List<AssetModel>> assetMap) {
		List<AssetModel> assetModelList = new ArrayList<>(); // List for child AssetModels
		for(TreeData a : asset.getChildren()) { // Loop through children
			assetModelList.add(createAssetModel(a)); // Add each child AssetModel
		}
		assetMap.put(createAssetModel(asset), assetModelList); // Add to map
		for(TreeData child : asset.getChildren()) { // If there are children, traverse them
			traverse(child, assetMap); 
		}
		return assetMap; // Return the map
	}

	// Creates an AssetModel from tree data
	private AssetModel createAssetModel(TreeData treeData) {
		AssetModel assetModel = new AssetModel(); // Create a new AssetModel
		// Set various properties like node name, alias name, icon, category, users, etc.
		assetModel.setNodeName(treeData.getId());
		assetModel.setAliasName(treeData.getAssetName());
		assetModel.setChecked(treeData.isChecked());
		assetModel.setIcon(treeData.getIcon());
		assetModel.setCategory(treeData.getCategory());
		assetModel.setImage(treeData.getImage());
		assetModel.setUsersList(treeData.getUsers());
		assetModel.setLogbooksList(treeData.getLogbook());
		assetModel.setLocation(treeData.getLocation());
		assetModel.setManufacturer(treeData.getManufacturer());
		assetModel.setModel(treeData.getModel());
		assetModel.setSerialNo(treeData.getSerialno());
		assetModel.setDescription(treeData.getDescription());
		assetModel.setUom(treeData.getUnit());
		assetModel.setCapacity(treeData.getCapacity());
		assetModel.setAssetGroup(treeData.getAssetGroup());
		assetModel.setPosX(treeData.getPosX());
		assetModel.setPosY(treeData.getPosY());
		assetModel.setNode_level(treeData.getNode_level());
		assetModel.setDepartment(treeData.getDepartment());
		assetModel.setGroups(treeData.getGroups());
		return assetModel; // Return the final AssetModel
	}

	// ------------------------------------------------------------------------
	// |                              Get Methods                             |
	// ------------------------------------------------------------------------

	// Returns a list of asset groups for a given tenant ID
	@GetMapping(path = "/assetgroups/{tenantId}", produces = "application/json")
	public ResponseEntity<?> getAssetGroupsList(@PathVariable("tenantId")  String tenantId) {
		try {
			assetDao = factory.getAssetConfigurationService(); // Get the asset service
			List<AssetGroup> assetList = assetDao.getAllAssetGroups(tenantId); // Get asset groups
			return ResponseEntity.status(HttpStatus.OK).body(assetList); // Return the asset list
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage()); // Handle errors
		}
	}

	// Returns assets in a group based on group ID and tenant ID
	@GetMapping(path = "/assetGroups/j/{assetGroupId}/{tenantId}", produces = "application/json")
	public ResponseEntity<List<AssetModel>> getAssetsByGroup(@PathVariable("assetGroupId")  String assetGroupId, @PathVariable("tenantId")  String tenantId) throws Exception {
		try {
			assetDao = factory.getAssetConfigurationService(); // Get the asset service
			List<AssetModel> assetFromGroupId = assetDao.getAssetFromGroupId(assetGroupId, tenantId); // Get assets
			return ResponseEntity.ok().body(assetFromGroupId); // Return the assets
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Handle errors
		}
	}

	// This method gets an "Asset Group Tree" for a specific group and tenant
	@GetMapping(path = "/assetGroups/{assetGroupId}/{tenantId}", produces = "application/json")
	public ResponseEntity<?> getAssetGroupTree(@PathVariable("assetGroupId")  String assetGroupId, @PathVariable("tenantId")  String tenantId) {

		// Get the service to work with assets (like a helper to fetch data)
		assetDao = factory.getAssetConfigurationService();

		// Create a list to store tree data (just like making a bucket to collect items)
		List<TreeData> treeMap = new LinkedList<>();

		// Get the asset group using the group ID and tenant ID
		AssetGroup group = assetDao.getAssetTreefromGroupId(assetGroupId, tenantId);

		// Add the root group data (think of it as the main parent item in a family tree)
		treeMap.add(new TreeData(group.getGroupId(), group.getGroupName(), group.getGroupName(), null));

		// Loop through each main node (big branches) in the asset group
		for (AssetModel node : group.getAssetInfo().keySet()) {

			// Loop through each child (small branches) connected to the main node
			for (AssetModel children : group.getAssetInfo().get(node)) {

				// Create a TreeData object for the parent (main node) with details like name, icon, etc.
				TreeData parent = new TreeData(
						node.getNodeName(), // Node name
						node.getAliasName(), // Alias (nickname)
						node.isChecked(), // Whether it's checked or not (like a checkbox)
						node.getAliasName(), // Alias again
						node.getIcon(), // Icon image
						node.getCategory(), // Category
						node.getImage(), // Image
						node.getUsersList(), // List of users
						node.getLogbooksList(), // Logbooks list
						node.getLocation(), // Location
						node.getManufacturer(), // Manufacturer
						node.getModel(), // Model
						node.getSerialNo(), // Serial number
						node.getDescription(), // Description
						node.getUom(), // Unit of measure (how much or what size)
						node.getCapacity(), // Capacity (how much it can hold)
						node.getAssetGroup(), // Group info
						node.getPosX(), // X-position (where it is horizontally)
						node.getPosY(), // Y-position (where it is vertically)
						new ArrayList<>(), // Empty list for future children (small branches)
						node.getDepartment(), // Department it belongs to
						node.getGroups() // Groups it belongs to
						);

				// Create a TreeData object for the child (small node) with the same details
				TreeData child = new TreeData(
						children.getNodeName(), // Node name
						children.getAliasName(), // Alias (nickname)
						children.isChecked(), // Checked status
						children.getAliasName(), // Alias again
						children.getIcon(), // Icon image
						children.getCategory(), // Category
						children.getImage(), // Image
						children.getUsersList(), // List of users
						children.getLogbooksList(), // Logbooks list
						children.getLocation(), // Location
						children.getManufacturer(), // Manufacturer
						children.getModel(), // Model
						children.getSerialNo(), // Serial number
						children.getDescription(), // Description
						children.getUom(), // Unit of measure
						children.getCapacity(), // Capacity
						children.getAssetGroup(), // Group info
						children.getPosX(), // X-position
						children.getPosY(), // Y-position
						new ArrayList<>(), // Empty list for future children
						children.getDepartment(), // Department
						children.getGroups() // Groups
						);

				// Set the relationship between parent and child (like connecting the small branch to the big branch)
				setChildren(treeMap, parent, child);
			}
		}

		// Return the tree structure as a response
		return ResponseEntity.ok(treeMap);
	}


	// This method connects a child to its parent in the tree (like adding a branch to another branch)
	private void setChildren(List<TreeData> tree, TreeData parent, TreeData child) {
		for (TreeData node : tree) { // Loop through each node (branch) in the tree
			if (node.equals(parent)) { // If the current node is the same as the parent we are looking for
				node.getChildren().add(child); // Add the child to the parent's list of children (attach the small branch to the big branch)
				break; // Stop looping because we found the parent and added the child
			}
			if (node.getChildren().size() > 0) { // If the current node has its own children (more branches below it)
				// Call this method again to check its children and see if they are the parent we are looking for
				setChildren(node.getChildren(), parent, child); // This is called recursion (the method calls itself to go deeper into the tree)
			}
		}
	}

	// This method returns an AssetGroup based on the group name, asset ID, and tenant ID
	@GetMapping(path = "/assetgroups/{assetGroup}/assets/{assetId}/{tenantId}", produces = "application/json")
	public ResponseEntity<?> getAssetInfo(
			@PathVariable("assetGroup")  String assetGroup, // The asset group name from the URL
			@PathVariable("assetId")  String assetId, // The asset ID from the URL
			@PathVariable("tenantId")  String tenantId // The tenant ID from the URL
			) {
		try {
			assetDao = factory.getAssetConfigurationService(); // Get the service that handles asset configuration
			AssetGroup assetfromGroupName = assetDao.getAssetTreefromGroupName(assetGroup, tenantId); // Get the asset group information based on the group name and tenant ID
			if (assetfromGroupName != null) { // If the asset group is found
				Map<AssetModel, List<AssetModel>> assetInfo = (Map<AssetModel, List<AssetModel>>) assetfromGroupName.getAssetInfo(); // Get the assets in the group (this is a map of AssetModels and their children)
				for (Map.Entry<AssetModel, List<AssetModel>> entry : assetInfo.entrySet()) { // Loop through each entry in the asset info map
					AssetModel keyAsset = entry.getKey(); // Get the main asset (key)
					if (keyAsset.getNodeName().equalsIgnoreCase(assetId)) { // If the asset's node name matches the given asset ID (ignoring case)
						List<AssetModel> assetsList = new ArrayList<>(); // Create a list to store the found asset
						assetsList.add(keyAsset); // Add the key asset (main asset) to the list
						return ResponseEntity.status(HttpStatus.OK).body(assetsList);  // Return the list of assets with HTTP status 200 (OK)
					}
				}
			}
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // If no asset was found, return a 404 (Not Found) response
		} catch (Exception e) {
			e.printStackTrace(); // Print the error for debugging
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage()); // Return a 500 (Internal Server Error) response with the error message
		}
	}

	// This method gets logbook information for a specific asset based on the node name and tenant ID
	@GetMapping(path = "/logbooks/nodeId/{tenantId}", produces = "application/json")
	public ResponseEntity<?> getLogbooksByAsset(
			@RequestParam  String nodeName, // The name of the node we want to find logbooks for
			@PathVariable("tenantId")  String tenantId // The ID of the tenant
			) {
		List<LogbookInfo> logbookInfoList = new ArrayList<>(); // Create an empty list to store logbook information
		try {
			logbookDao = factory.getLogbookService(); // Get the logbook service to access logbook data
			List<FormDetails> allActiveForms = logbookDao.getAllActiveForms(tenantId); // Get all active forms for the given tenant
			restdao = factory.getRestfulService(); // Get the service to access asset information
			List<AssetModel> assetInfo2 = restdao.getAssetInfo(tenantId); // Get asset information for the given tenant
			boolean nodeNameFound = false; // A flag to check if we found the node name
			for (AssetModel assetModel : assetInfo2) { // Loop through each asset to check if we find the matching node name
				if (nodeName.equals(assetModel.getNodeName())) { // If we found the node
					for (Integer logbookFormName : assetModel.getLogbooksList()) { // Loop through each logbook form name linked to the asset
						for (FormDetails formDetails : allActiveForms) { // Loop through all active forms to find matching logbook form details
							if (logbookFormName.equals(formDetails.getFormID())) { // If IDs match
								LogbookInfo logbookInfo = new LogbookInfo(formDetails.getFormName(), formDetails.getVersionNumber()); // Create a new logbook info object and add it to the list
								logbookInfoList.add(logbookInfo);
								break; // Exit this loop since we found the matching logbook
							}
						}
					}
					nodeNameFound = true; // Set the flag to true since we found the node
				}
			}
			if (nodeNameFound) { // If we found the node name
				return ResponseEntity.ok(logbookInfoList); // return the logbook info list with a 200 OK response
			} else { // If the node name is not found 
				String notFoundMessage = "Node '" + nodeName + "' is not present.";
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFoundMessage); // return a 404 Not Found message
			}
		} catch (Exception e) {
			e.printStackTrace(); // If there's an error, print it and return a 500 Internal Server Error message
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	// This method gets logbook details for a specific asset based on the asset ID and tenant ID
	@GetMapping("/logbookbyasset/{assetId}/{tenantId}")
	public List<Map<String, Object>> getAssetDetails(
			@PathVariable("assetId")  String assetId, // The ID of the asset we want details for
			@PathVariable("tenantId")  String tenantId // The ID of the tenant
			) {
		restdao = factory.getRestfulService(); // Get the service to access asset details
		return restdao.getLogbooksByAsset(assetId, tenantId); // Return the logbook details for the specified asset
	}

	// This method gets specific information about an asset based on its ID and tenant ID
	@GetMapping("/assetInfoByAssetId/{assetId}/{tenantId}")
	public Map<String, Object> getAssetInfoByAssetId(
			@PathVariable("assetId")  String assetId, // The ID of the asset we want information about
			@PathVariable("tenantId")  String tenantId // The ID of the tenant
			) {
		restdao = factory.getRestfulService(); // Get the service to access asset information
		return restdao.getAssetInfoByAssetId(assetId, tenantId); // Return the asset information for the specified asset ID
	}


	
	// This method gets logbook information for a specific asset based on the node name and tenant ID
	@GetMapping(path = "/getactivelogbooksbyassetid/{assetId}/{tenantId}", produces = "application/json")
	public ResponseEntity<?> getActiveLogbooksByAsset(
	        @PathVariable("assetId") String assetId, // The ID of the asset
	        @PathVariable("tenantId") String tenantId // The ID of the tenant
	) {
	    try {
	        // Get the service to access asset details
	        restdao = factory.getRestfulService();
	        
	        // Fetch the logbook details for the specified asset
	        List<FormDetails> logbooks = restdao.getActiveLogbooksByAssetId(assetId, tenantId);

	        // Check if the result is empty
	        if (logbooks.isEmpty()) {
	            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No logbooks found for the given asset and tenant.");
	        }

	        // Return the result with a 200 OK status
	        return ResponseEntity.ok(logbooks);

	    } catch (Exception e) {
	        // Log the exception (consider using a logging framework in production)
	        System.err.println("Error fetching logbooks: " + e.getMessage());
	        e.printStackTrace();

	        // Return a 500 Internal Server Error status with the error message
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("An error occurred while fetching logbooks. Please try again later.");
	    }
	}


	// ------------------------------------------------------------------------
	// |                              Post Methods                            |
	// ------------------------------------------------------------------------

	// This method saves information about an asset group
	@PostMapping(path = "/assetgroups/{tenantId}", consumes = "application/json")
	public ResponseEntity<?> saveAssetGroupInfo(
			@RequestBody  Map<String, Object> groupData, // Get the group information from the request
			@PathVariable("tenantId")  String tenantId // Get the tenant ID from the URL
			) {
		Map<AssetModel, List<AssetModel>> assetInfo = new HashMap<>(); // Create a map to hold asset information
		// Get details about the asset group from the received data
		String groupName = (String) groupData.get("groupName"); // The name of the group
		String groupId = (String) groupData.get("groupId"); // The ID of the group
		String createdBy = (String) groupData.get("createdBy"); // Who created the group
		String createdOn = (String) groupData.get("createdOn"); // When the group was created
		List<Map<String, Object>> children = (List<Map<String, Object>>) groupData.get("children"); // Get the list of child assets (if any)
		String assetMapImage = (String) groupData.get("assetMapImage"); // Get the image for the asset map
		assetInfo = putInAssetInfo((List<Map<String, Object>>) children); // Put child assets into the asset information map
		AssetGroup group = new AssetGroup(groupName, groupId, createdBy, createdOn, assetInfo, assetMapImage); // Create a new AssetGroup object with all the details
		assetDao = factory.getAssetConfigurationService(); // Get the asset configuration service to save the group
		assetDao.saveAssetTree(group, tenantId); // Save the asset group in the database for the given tenant
		return ResponseEntity.ok(group); // Return a response with the saved group information
	}

	// This method organizes child assets into a map
	public Map<AssetModel, List<AssetModel>> putInAssetInfo(List<Map<String, Object>> treeData) {
		Map<AssetModel, List<AssetModel>> assetInfo = new HashMap<>(); // Create a new map for assets
		for (Map<String, Object> tree : treeData) { // Loop through each asset in the tree data
			AssetModel parentAsset = createAssetModel(tree); // Create a parent asset from the data
			List<Map<String, Object>> children = (List<Map<String, Object>>) tree.get("children"); // Get child assets
			List<AssetModel> childAssets = createChildAssetModels(children); // Create child asset models
			assetInfo.put(parentAsset, childAssets); // Add parent asset and its children to the map
		}
		return assetInfo; // Return the complete asset information map
	}

	// This method creates an asset model from the given data
	private AssetModel createAssetModel(Map<String, Object> tree) {
		// Get various properties from the asset data
		String id = (String) tree.get("id"); // Asset ID
		String assetName = (String) tree.get("assetName"); // Asset name
		boolean checked = (boolean) tree.get("checked"); // Is the asset checked?
		String icon = (String) tree.get("icon"); // Icon for the asset
		String category = (String) tree.get("category"); // Category of the asset
		String image = (String) tree.get("image"); // Image of the asset
		List<String> users = (List<String>) tree.get("users"); // Users related to the asset
		List<Integer> logbook = (List<Integer>) tree.get("logbook"); // Logbook IDs for the asset
		String location = (String) tree.get("location"); // Location of the asset
		String manufacturer = (String) tree.get("manufacturer"); // Manufacturer of the asset
		String model = (String) tree.get("model"); // Model of the asset
		String serialno = (String) tree.get("serialno"); // Serial number of the asset
		String description = (String) tree.get("description"); // Description of the asset
		String unit = (String) tree.get("unit"); // Unit of the asset
		int capacity = (int) tree.get("capacity"); // Capacity of the asset
		String assetGroup = (String) tree.get("assetGroup"); // Group to which the asset belongs
		String posX = (String) tree.get("posX"); // X position of the asset
		String posY = (String) tree.get("posY"); // Y position of the asset
		return new AssetModel(id, assetName, checked, icon, category, image, users, logbook, location, manufacturer, model, serialno, description, unit, capacity, assetGroup, posX, posY); // Return a new AssetModel object created from the above data
	}

	// This method creates child asset models from the given child data
	private List<AssetModel> createChildAssetModels(List<Map<String, Object>> children) {
		List<AssetModel> childAssets = new ArrayList<>(); // Create a list for child assets
		if (children != null) { // Check if there are any children
			for (Map<String, Object> child : children) { // Loop through each child asset
				AssetModel childAsset = createAssetModel(child); // Create an AssetModel for the child
				List<Map<String, Object>> grandchildren = (List<Map<String, Object>>) child.get("children"); // Get grandchildren if they exist
				List<AssetModel> grandchildAssets = createChildAssetModels(grandchildren); // Create child assets for grandchildren
				childAssets.add(childAsset); // Add the child asset to the list
				childAssets.addAll(grandchildAssets); // Add all grandchild assets to the list
			}
		}
		return childAssets; // Return the list of child assets
	}

	// This method creates a new asset in a specific asset group
	@PostMapping(path = "/assetgroups/{assetGroup}/{assetParentId}/{tenantId}", consumes = "application/json")
	public ResponseEntity<?> AssetGroupList(
			@PathVariable("assetGroup")  String assetGroup, // The asset group to which the asset belongs
			@PathVariable("assetParentId")  String assetParentId, // The ID of the parent asset
			@PathVariable("tenantId")  String tenantId, // The ID of the tenant
			@RequestBody AssetModel assetModel // The model of the asset being created
			) {
		// Get services for REST and asset configuration
		restdao = factory.getRestfulService();
		assetDao = factory.getAssetConfigurationService();
		List<AssetGroup> allAssetGroups = assetDao.getAllAssetGroups(tenantId); // Get a list of all asset groups
		AssetModel success = new AssetModel(); // Create a new AssetModel to hold the success response
		try {
			for (AssetGroup assetGroup2 : allAssetGroups) { // Loop through each asset group to find a match
				String groupId = assetGroup2.getGroupId(); // Get the ID of the current asset group
				if (groupId.equalsIgnoreCase(assetGroup)) { // If the group ID matches the given asset group
					success = restdao.AddNewAsset(assetGroup, assetParentId, assetModel, tenantId); // Create a new asset using the REST service
				}
			}
			return ResponseEntity.status(HttpStatus.OK).body(success); // Return a success response with the created asset information
		} catch (Exception e) { // If there's an error
			e.printStackTrace(); // print error and return an internal server error response
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	// This method saves a new asset group based on tree data
	@PostMapping(path = "/assetgroup/{createdBy}/{tenantId}", produces = "application/json")
	public ResponseEntity<?> saveAssetGroup(
			@RequestBody  List<TreeData> treeDataList, // The tree data for the asset group
			@PathVariable("createdBy")  String createdby, // Who created the group
			@PathVariable("tenantId")  String tenantId // The ID of the tenant
			) {
		// Check if any required parameter is null or empty
		if (createdby == null || createdby.trim().isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User ID is not present!");
		}
		if (tenantId == null || tenantId.trim().isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tenant ID is not present!");
		}
		if (treeDataList == null || treeDataList.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tree data is not provided!");
		}
		AssetGroup group = convertTreeDataListToAssetGroup(treeDataList, createdby); // Convert the tree data into an AssetGroup object
		assetDao = factory.getAssetConfigurationService(); // Get the asset configuration service to save the group
		try {
			int result = assetDao.saveAssetTree(group, tenantId); // Save the asset group in the database for the given tenant
			if(result < 1) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to save assetGroup");
			}
		} catch (Exception e) { // If there's an error
			e.printStackTrace(); // print error and return an internal server error response
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
		}
		return ResponseEntity.status(HttpStatus.OK).body("inserted"); // Return a response indicating the group was inserted successfully
	}



	// ------------------------------------------------------------------------
	// |                              Delete Methods                          |
	// ------------------------------------------------------------------------


	// This method deletes an asset based on its group and ID
	@DeleteMapping(path = "/assetgroups/{assetgroup}/assetId/{assetId}/{tenantId}", produces = "application/json")
	public ResponseEntity<?> deleteAnAsset(
			@PathVariable("assetgroup")  String assetgroup, // Get the name of the asset group from the URL
			@PathVariable("assetId")  String assetId, // Get the ID of the asset to delete from the URL
			@PathVariable("tenantId")  String tenantId // Get the tenant ID from the URL
			) {
		try {
			restdao = factory.getRestfulService(); // Get the service that helps with asset-related actions
			int result = restdao.deleteAnAsset(assetgroup, assetId, tenantId); // Call the service to delete the asset from the AssetInfo table
			if (result < 1) { // Check if the asset was deleted successfully
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete asset"); // If not, return an error message
			}
			// If everything went well, we don't need to do anything extra here
		} catch (Exception e) { // If there's an error
			e.printStackTrace(); // print error out for us to see
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete asset"); // Return an error message if something goes wrong
		}
		return ResponseEntity.status(HttpStatus.OK).body("Asset deleted successfully"); // Return a success message if the asset was deleted
	}

	// This method deletes an entire asset group
	@DeleteMapping(path = "/assetgroup/{assetgroup}/{tenantId}", produces = "application/json")
	public ResponseEntity<?> deleteAnAssetGroup(
			@PathVariable("assetgroup")  String groupId, // Get the ID of the asset group from the URL
			@PathVariable("tenantId")  String tenantId // Get the tenant ID from the URL
			) {
		try {
			restdao = factory.getRestfulService(); // Get the service that helps with asset-related actions
			int result = restdao.deleteAnAssetGroup(groupId, tenantId); // Call the service to delete the asset group
			if (result < 1) { // Check if the asset group was deleted successfully
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete assetGroup"); // If not, return an error message
			}
		} catch (Exception e) { // If there's an error
			e.printStackTrace(); // print error out for us to see
			System.out.println(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete assetGroup"); // Return an error message if something goes wrong
		}
		return ResponseEntity.status(HttpStatus.OK).body("AssetGroup deleted successfully"); // Return a success message if the asset group was deleted
	}
	@PostMapping("/register-new-tenant/{tenantid}")
    public ResponseEntity<String> registernewtenant(@PathVariable("tenantid") String tenantid) {
    	try {
    		restdao = factory.getRestfulService();
        	
    		restdao.registerNewTenant(tenantid);
        	return ResponseEntity.ok().body("Tenant registered successfully");
    	}catch(Exception e) {
    		e.printStackTrace();
    		return ResponseEntity.badRequest().body("Tenant registration unsuccessful");
    	}
    }
}
