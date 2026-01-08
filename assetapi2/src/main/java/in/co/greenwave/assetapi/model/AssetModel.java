package in.co.greenwave.assetapi.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

// This is the AssetModel class. It is used to represent an asset with many details like its name, location, and more.
// An asset could be anything like a machine, tool, or equipment in a plant.

public class AssetModel {

	// Basic information about the asset
	private String nodeName; // The name of the asset (e.g., "Pump 1")
	private String aliasName; // A nickname or short name for the asset
	private boolean checked; // Whether this asset is selected or checked in the system (true/false)
	private String icon; // An icon representing the asset visually (e.g., a picture of a pump)
	private String category; // The category of the asset (e.g., "Electronics", "Mechanical")
	private String image; // A link or path to an image representing the asset
	private List<String> usersList; // List of users who can access or manage this asset
	private List<Integer> logbooksList; // List of logbook IDs linked to this asset (a logbook keeps track of asset details)

	// Detailed information about the asset's physical properties
	private String location; // Where the asset is located (e.g., "Factory Floor 1")
	private String manufacturer; // The company that made this asset (e.g., "XYZ Corp")
	private String model; // The model number of the asset (e.g., "P-2000")
	private String serialNo; // Serial number of the asset (a unique identifier)
	private String description; // A description of what the asset does or its purpose
	private String uom; // Unit of measurement for the asset's capacity (e.g., "Liters")
	private int capacity; // How much this asset can hold or do (e.g., 500 liters capacity)

	// Information about the asset's position and grouping
	private String assetGroup; // The group this asset belongs to (e.g., "Water Systems")
	private String posX; // X-coordinate of the asset's position on a map
	private String posY; // Y-coordinate of the asset's position on a map

	// List of child assets (if this asset has other smaller assets inside it)
	List<AssetModel> children = new LinkedList<AssetModel>();
	private int node_level; // The level or depth of this asset in a hierarchy (e.g., level 1 could mean the main asset, level 2 could mean its sub-assets)

	// Other details like which department manages this asset and groups it belongs to
	private String department; // The department responsible for this asset (e.g., "Maintenance")
	private List<String> groups; // List of groups this asset is associated with (e.g., "Critical Assets")

	// Default constructor (used when we don't have any information yet)
	public AssetModel() {
		super();
	}

	// Constructor to create an asset with just its name and alias
	public AssetModel(String nodeName, String aliasName) {
		super();
		this.nodeName = nodeName;
		this.aliasName = aliasName;
	}

	// Constructor to create an asset with a name, alias, and icon
	public AssetModel(String nodeName, String aliasName, String icon) {
		super();
		this.nodeName = nodeName;
		this.aliasName = aliasName;
		this.icon = icon;
	}

	// Constructor to create an asset with more details: checked status, icon, category, and image
	public AssetModel(String nodeName, String aliasName, boolean checked, String icon, String category, String image) {
		super();
		this.nodeName = nodeName;
		this.aliasName = aliasName;
		this.checked = checked;
		this.icon = icon;
		this.category = category;
		this.image = image;
	}

	// Constructor to create an asset with users list and logbooks list
	// We use a default empty list if logbooksList is not provided (null)
	public AssetModel(String nodeName, String aliasName, String icon, List<String> usersList, List<Integer> logbooksList) {
		super();
		this.nodeName = nodeName;
		this.aliasName = aliasName;
		this.icon = icon;
		this.usersList = usersList;
		this.logbooksList = (logbooksList != null) ? logbooksList : new ArrayList<>();
	}

	// Constructor to create an asset with its group, position, and icon
	public AssetModel(String assetGroup, String nodeName, String aliasName, String posX, String posY, String icon) {
		super();
		this.setAssetGroup(assetGroup);
		this.nodeName = nodeName;
		this.aliasName = aliasName;
		this.posX = posX;
		this.posY = posY;
		this.icon = icon;
	}

	// Constructor to create an asset for a Restful API, including location and manufacturer details
	public AssetModel(String nodeName, String aliasName, List<Integer> logbooksList, String location, String manufacturer) {
		super();
		this.nodeName = nodeName;
		this.aliasName = aliasName;
		this.logbooksList = (logbooksList != null) ? logbooksList : new ArrayList<>();
		this.location = location;
		this.manufacturer = manufacturer;
	}

	// Constructor for detailed asset information including location, manufacturer, model, and description
	public AssetModel(String nodeName, String aliasName, String icon, String category, List<String> usersList, List<Integer> logbooksList, String location, String manufacturer, String model, String serialNo, String description, int capacity) {
		super();
		this.nodeName = nodeName;
		this.aliasName = aliasName;
		this.icon = icon;
		this.category = category;
		this.usersList = usersList;
		this.logbooksList = (logbooksList != null) ? logbooksList : new ArrayList<>();
		this.location = location;
		this.manufacturer = manufacturer;
		this.model = model;
		this.serialNo = serialNo;
		this.description = description;
		this.capacity = capacity;
	}

	// Overriding the hashCode method to uniquely identify assets by their name
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nodeName == null) ? 0 : nodeName.hashCode());
		return result;
	}

	// Overriding the equals method to check if two assets are the same based on their name
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AssetModel other = (AssetModel) obj;
		if (nodeName == null) {
			if (other.nodeName != null)
				return false;
		} else if (!nodeName.equals(other.nodeName))
			return false;
		return true;
	}

	// This is a simple constructor to create an AssetModel with all the above details.
	public AssetModel(String nodeName, String aliasName, boolean checked, String icon, String category, String image,
			List<String> usersList, List<Integer> logbooksList, String location, String manufacturer,
			String model, String serialNo, String description, String uom, int capacity, String assetGroup,
			String posX, String posY) {
		// This is where we set each of the details when we create an AssetModel object.
		this.nodeName = nodeName;
		this.aliasName = aliasName;
		this.checked = checked;
		this.icon = icon;
		this.category = category;
		this.image = image;
		this.usersList = usersList;
		this.logbooksList = logbooksList;
		this.location = location;
		this.manufacturer = manufacturer;
		this.model = model;
		this.serialNo = serialNo;
		this.description = description;
		this.uom = uom;
		this.capacity = capacity;
		this.assetGroup = assetGroup;
		this.posX = posX;
		this.posY = posY;
	}

	// Constructor with full details, including nested child assets (children) and node level
	public AssetModel(String nodeName, String aliasName, boolean checked, String icon, String category, String image,
			List<String> usersList, List<Integer> logbooksList, String location, String manufacturer, String model,
			String serialNo, String description, String uom, int capacity, String assetGroup, String posX, String posY,
			List<AssetModel> children, int node_level) {
		super();
		this.nodeName = nodeName;
		this.aliasName = aliasName;
		this.checked = checked;
		this.icon = icon;
		this.category = category;
		this.image = image;
		this.usersList = usersList;
		this.logbooksList = logbooksList;
		this.location = location;
		this.manufacturer = manufacturer;
		this.model = model;
		this.serialNo = serialNo;
		this.description = description;
		this.uom = uom;
		this.capacity = capacity;
		this.assetGroup = assetGroup;
		this.posX = posX;
		this.posY = posY;
		this.children = children;
		this.node_level = node_level;
	}

	// Another constructor that includes department and groups information
	public AssetModel(String nodeName, String aliasName, boolean checked, String icon, String category, String image,
			List<Integer> logbooksList, String location, String manufacturer, String model, String serialNo,
			String description, String uom, int capacity, String assetGroup, String posX, String posY, int node_level,
			String department, List<String> groups) {
		super();
		this.nodeName = nodeName;
		this.aliasName = aliasName;
		this.checked = checked;
		this.icon = icon;
		this.category = category;
		this.image = image;
		this.logbooksList = logbooksList;
		this.location = location;
		this.manufacturer = manufacturer;
		this.model = model;
		this.serialNo = serialNo;
		this.description = description;
		this.uom = uom;
		this.capacity = capacity;
		this.assetGroup = assetGroup;
		this.posX = posX;
		this.posY = posY;
		this.node_level = node_level;
		this.department = department;
		this.groups = groups;
	}

	// This is a method to get (ask for) the asset's name.
	public String getNodeName() {
		return nodeName;
	}

	// This is a method to set (give) the asset's name.
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	// This is a method to get the asset's alias (nickname).
	public String getAliasName() {
		return aliasName;
	}

	// This is a method to set the asset's alias (nickname).
	public void setAliasName(String aliasName) {
		this.aliasName = aliasName;
	}

	// This is a method to find out if the asset is checked (true or false).
	public boolean isChecked() {
		return checked;
	}

	// This is a method to mark the asset as checked or not checked.
	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	// This is a method to get the asset's icon (a small picture).
	public String getIcon() {
		return icon;
	}

	// This is a method to set the asset's icon.
	public void setIcon(String icon) {
		this.icon = icon;
	}

	// This is a method to get the asset's category (what type it is).
	public String getCategory() {
		return category;
	}

	// This is a method to set the asset's category.
	public void setCategory(String category) {
		this.category = category;
	}

	// This is a method to get the asset's image (a bigger picture).
	public String getImage() {
		return image;
	}

	// This is a method to set the asset's image.
	public void setImage(String image) {
		this.image = image;
	}

	// This is a method to get the list of users who are allowed to use the asset.
	public List<String> getUsersList() {
		return usersList;
	}

	// This is a method to set the list of users who can use the asset.
	public void setUsersList(List<String> usersList) {
		this.usersList = usersList;
	}

	// This is a method to get the list of logbooks (where important notes about the asset are stored).
	public List<Integer> getLogbooksList() {
		return logbooksList;
	}

	// This is a method to set the list of logbooks for the asset.
	public void setLogbooksList(List<Integer> logbooksList) {
		this.logbooksList = logbooksList;
	}

	// This is a method to get the asset's location (where it is).
	public String getLocation() {
		return location;
	}

	// This is a method to set the asset's location.
	public void setLocation(String location) {
		this.location = location;
	}

	// This is a method to get the manufacturer (who made the asset).
	public String getManufacturer() {
		return manufacturer;
	}

	// This is a method to set the asset's manufacturer.
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	// This is a method to get the asset's model (what type or version it is).
	public String getModel() {
		return model;
	}

	// This is a method to set the asset's model.
	public void setModel(String model) {
		this.model = model;
	}

	// This is a method to get the serial number (a unique code) of the asset.
	public String getSerialNo() {
		return serialNo;
	}

	// This is a method to set the asset's serial number.
	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	// This is a method to get the asset's description (what it does).
	public String getDescription() {
		return description;
	}

	// This is a method to set the asset's description.
	public void setDescription(String description) {
		this.description = description;
	}

	// This is a method to get the unit of measurement (how we measure the asset's ability or size).
	public String getUom() {
		return uom;
	}

	// This is a method to set the unit of measurement for the asset.
	public void setUom(String uom) {
		this.uom = uom;
	}

	// This is a method to get the asset's capacity (how much it can do or hold).
	public int getCapacity() {
		return capacity;
	}

	// This is a method to set the asset's capacity.
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	// This is a method to get the asset's X position (its position on a map).
	public String getPosX() {
		return posX;
	}

	// This is a method to set the asset's X position.
	public void setPosX(String posX) {
		this.posX = posX;
	}

	// This is a method to get the asset's Y position (its position on a map).
	public String getPosY() {
		return posY;
	}

	// This is a method to set the asset's Y position.
	public void setPosY(String posY) {
		this.posY = posY;
	}

	// This is a method to get the group the asset belongs to.
	public String getAssetGroup() {
		return assetGroup;
	}

	// This is a method to set the group the asset belongs to.
	public void setAssetGroup(String assetGroup) {
		this.assetGroup = assetGroup;
	}

	// This is a method to get the children of this asset (smaller parts inside it).
	public List<AssetModel> getChildren() {
		return children;
	}

	// This is a method to set the children of this asset.
	public void setChildren(List<AssetModel> children) {
		this.children = children;
	}

	// This is a method to get the level of this asset (how deep it is in a hierarchy).
	public int getNode_level() {
		return node_level;
	}

	// This is a method to set the level of this asset.
	public void setNode_level(int node_level) {
		this.node_level = node_level;
	}

	// This is a method to get the department responsible for this asset.
	public String getDepartment() {
		return department;
	}

	// This is a method to set the department responsible for the asset.
	public void setDepartment(String department) {
		this.department = department;
	}

	// This is a method to get the list of groups this asset belongs to.
	public List<String> getGroups() {
		return groups;
	}

	// This is a method to set the list of groups for this asset.
	public void setGroups(List<String> groups) {
		this.groups = groups;
	}

	// This method returns a string that describes the asset with all its details in one line.
	@Override
	public String toString() {
		return "AssetModel [nodeName=" + nodeName + ", aliasName=" + aliasName + ", checked=" + checked + ", icon="
				+ icon + ", category=" + category + ", image=" + image + ", usersList=" + usersList + ", logbooksList="
				+ logbooksList + ", location=" + location + ", manufacturer=" + manufacturer + ", model=" + model
				+ ", serialNo=" + serialNo + ", description=" + description + ", uom=" + uom + ", capacity=" + capacity
				+ ", assetGroup=" + assetGroup + ", posX=" + posX + ", posY=" + posY + ", children=" + children
				+ ", node_level=" + node_level + ", department=" + department + ", groups=" + groups + "]";
	}
}