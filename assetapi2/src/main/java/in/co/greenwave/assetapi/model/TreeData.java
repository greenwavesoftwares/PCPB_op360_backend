//package in.co.greenwave.assetapi.model;
// 
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Objects;
// 
//public class TreeData {
//	private String id;
//	private String assetName;
//	private String label;
//	private String icon;
//	
//	private boolean checked;
//	private String category;
//	private String image;
//	private List<String> users;
//	private List<Integer> logbook;
//	private String location;
//	private String manufacturer;
//	private String model;
//	private String serialno;
//	private String description;
//	private String unit;
//	private int capacity;
//	private String assetGroup;
//	private String posX;
//	private String posY;
//	
//	List<TreeData> children = new LinkedList<TreeData>();
//	private int node_level;
//	
//	private String department;
//	private List<String> groups;
//
//	public TreeData() {
//		
//	}
//	
//	public TreeData(String id, String assetName, boolean checked, String label, String icon, String category,
//			String image, List<String> users, List<Integer> logbook, String location, String manufacturer, String model,
//			String serialno, String description, String unit, int capacity, String assetGroup, String posX, String posY,
//			List<TreeData> children) {
//		super();
//		this.id = id;
//		this.assetName = assetName;
//		this.checked = checked;
//		this.label = label;
//		this.icon = icon;
//		this.category = category;
//		this.image = image;
//		this.users = users;
//		this.logbook = logbook;
//		this.location = location;
//		this.manufacturer = manufacturer;
//		this.model = model;
//		this.serialno = serialno;
//		this.description = description;
//		this.unit = unit;
//		this.capacity = capacity;
//		this.assetGroup = assetGroup;
//		this.posX = posX;
//		this.posY = posY;
//		this.children = children;
//	}
//	
//	public TreeData(String id, String assetName, boolean checked, String label, String icon, String category,
//			String image, List<String> users, List<Integer> logbook, String location, String manufacturer, String model,
//			String serialno, String description, String unit, int capacity, String assetGroup, String posX, String posY,
//			List<TreeData> children, int node_level) {
//		super();
//		this.id = id;
//		this.assetName = assetName;
//		this.checked = checked;
//		this.label = label;
//		this.icon = icon;
//		this.category = category;
//		this.image = image;
//		this.users = users;
//		this.logbook = logbook;
//		this.location = location;
//		this.manufacturer = manufacturer;
//		this.model = model;
//		this.serialno = serialno;
//		this.description = description;
//		this.unit = unit;
//		this.capacity = capacity;
//		this.assetGroup = assetGroup;
//		this.posX = posX;
//		this.posY = posY;
//		this.children = children;
//		this.node_level = node_level;
//	}
//
//	public TreeData(String id, String assetName, String label, String icon) {
//		super();
//		this.id = id;
//		this.assetName = assetName;
//		this.label = label;
//		this.icon = icon;
//	}
//	
//	public TreeData(String id, String assetName, boolean checked, String label, String icon, String category,
//			String image, List<String> users, List<Integer> logbook, String location, String manufacturer, String model,
//			String serialno, String description, String unit, int capacity, String assetGroup, String posX, String posY,
//			List<TreeData> children, String department, List<String> groups) {
//		super();
//		this.id = id;
//		this.assetName = assetName;
//		this.label = label;
//		this.icon = icon;
//		this.checked = checked;
//		this.category = category;
//		this.image = image;
//		this.users = users;
//		this.logbook = logbook;
//		this.location = location;
//		this.manufacturer = manufacturer;
//		this.model = model;
//		this.serialno = serialno;
//		this.description = description;
//		this.unit = unit;
//		this.capacity = capacity;
//		this.assetGroup = assetGroup;
//		this.posX = posX;
//		this.posY = posY;
//		this.children = children;
//		this.department = department;
//		this.groups = groups;
//	}
//
//	public String getId() {
//		return id;
//	}
//
//	public void setId(String id) {
//		this.id = id;
//	}
//
//	public String getAssetName() {
//		return assetName;
//	}
//
//	public void setAssetName(String assetName) {
//		this.assetName = assetName;
//	}
//
//	public String getLabel() {
//		return label;
//	}
//
//	public void setLabel(String label) {
//		this.label = label;
//	}
//
//	public String getIcon() {
//		return icon;
//	}
//
//	public void setIcon(String icon) {
//		this.icon = icon;
//	}
//
//	public List<TreeData> getChildren() {
//		return children;
//	}
//
//	public void setChildren(List<TreeData> children) {
//		this.children = children;
//	}
//
//	public boolean isChecked() {
//		return checked;
//	}
//
//	public void setChecked(boolean checked) {
//		this.checked = checked;
//	}
//
//	public String getCategory() {
//		return category;
//	}
//
//	public void setCategory(String category) {
//		this.category = category;
//	}
//
//	public String getImage() {
//		return image;
//	}
//
//	public void setImage(String image) {
//		this.image = image;
//	}
//
//	public List<String> getUsers() {
//		return users;
//	}
//
//	public void setUsers(List<String> users) {
//		this.users = users;
//	}
//
//	public List<Integer> getLogbook() {
//		return logbook;
//	}
//
//	public void setLogbook(List<Integer> logbook) {
//		this.logbook = logbook;
//	}
//
//	public String getLocation() {
//		return location;
//	}
//
//	public void setLocation(String location) {
//		this.location = location;
//	}
//
//	public String getManufacturer() {
//		return manufacturer;
//	}
//
//	public void setManufacturer(String manufacturer) {
//		this.manufacturer = manufacturer;
//	}
//
//	public String getModel() {
//		return model;
//	}
//
//	public void setModel(String model) {
//		this.model = model;
//	}
//
//	public String getSerialno() {
//		return serialno;
//	}
//
//	public void setSerialno(String serialno) {
//		this.serialno = serialno;
//	}
//
//	public String getDescription() {
//		return description;
//	}
//
//	public void setDescription(String description) {
//		this.description = description;
//	}
//
//	public String getUnit() {
//		return unit;
//	}
//
//	public void setUnit(String unit) {
//		this.unit = unit;
//	}
//
//	public int getCapacity() {
//		return capacity;
//	}
//
//	public void setCapacity(int capacity) {
//		this.capacity = capacity;
//	}
//
//	public String getAssetGroup() {
//		return assetGroup;
//	}
//
//	public void setAssetGroup(String assetGroup) {
//		this.assetGroup = assetGroup;
//	}
//
//	public String getPosX() {
//		return posX;
//	}
//
//	public void setPosX(String posX) {
//		this.posX = posX;
//	}
//
//	public String getPosY() {
//		return posY;
//	}
//
//	public void setPosY(String posY) {
//		this.posY = posY;
//	}
//
//	@Override
//	public int hashCode() {
//		return Objects.hash(id);
//	}
//
//	@Override
//	public boolean equals(Object obj) {
//		if (this == obj)
//			return true;
//		if (obj == null)
//			return false;
//		if (getClass() != obj.getClass())
//			return false;
//		TreeData other = (TreeData) obj;
//		return Objects.equals(id, other.id);
//	}
//
//	public int getNode_level() {
//		return node_level;
//	}
//
//	public void setNode_level(int node_level) {
//		this.node_level = node_level;
//	}
//
//	public String getDepartment() {
//		return department;
//	}
//
//	public void setDepartment(String department) {
//		this.department = department;
//	}
//
//	public List<String> getGroups() {
//		return groups;
//	}
//
//	public void setGroups(List<String> groups) {
//		this.groups = groups;
//	}
//
//	@Override





package in.co.greenwave.assetapi.model; // This line tells where the class belongs in the project

import java.util.LinkedList; // This is a special kind of list we can use to hold many items
import java.util.List; // This is a general list type that can hold many items
import java.util.Objects; // This helps us compare things to see if they are the same

// This class is like a tree that holds information about different assets (things we own)
public class TreeData {

	// These are the properties of our asset
	private String id; // A special name (ID) for each asset
	private String assetName; // The name of the asset
	private String label; // A label for the asset (like a tag)
	private String icon; // An icon representing the asset

	private boolean checked; // Tells if the asset is checked (like a checkbox)
	private String category; // The category the asset belongs to (like toys or books)
	private String image; // An image of the asset
	private List<String> users; // A list of people who use this asset
	private List<Integer> logbook; // A list of logs (records) for this asset
	private String location; // Where the asset is located
	private String manufacturer; // Who made the asset
	private String model; // The model of the asset (like a version)
	private String serialno; // A special number for the asset that is unique
	private String description; // A description of the asset (what it is)
	private String unit; // The unit of measurement (like kg, cm)
	private int capacity; // How much the asset can hold or do
	private String assetGroup; // The group this asset belongs to
	private String posX; // The position of the asset on a map (X coordinate)
	private String posY; // The position of the asset on a map (Y coordinate)

	// This is a list of child assets (like smaller assets that belong to this one)
	List<TreeData> children = new LinkedList<TreeData>(); 
	private int node_level; // How deep this asset is in the tree (like how many branches away from the root)

	private String department; // The department that owns the asset
	private List<String> groups; // A list of groups that this asset belongs to

	// This is a special method called a constructor. It helps create a new TreeData object without any properties.
	public TreeData() {

	}

	// This constructor helps create a new TreeData object with many properties
	public TreeData(String id, String assetName, boolean checked, String label, String icon, String category,
			String image, List<String> users, List<Integer> logbook, String location, String manufacturer, String model,
			String serialno, String description, String unit, int capacity, String assetGroup, String posX, String posY,
			List<TreeData> children) {
		super(); // Calls the parent class constructor (not usually needed here)
		this.id = id; // Sets the ID for the asset
		this.assetName = assetName; // Sets the name for the asset
		this.checked = checked; // Sets if the asset is checked
		this.label = label; // Sets the label for the asset
		this.icon = icon; // Sets the icon for the asset
		this.category = category; // Sets the category for the asset
		this.image = image; // Sets the image for the asset
		this.users = users; // Sets the list of users for the asset
		this.logbook = logbook; // Sets the logbook records for the asset
		this.location = location; // Sets where the asset is located
		this.manufacturer = manufacturer; // Sets who made the asset
		this.model = model; // Sets the model of the asset
		this.serialno = serialno; // Sets the unique serial number for the asset
		this.description = description; // Sets the description for the asset
		this.unit = unit; // Sets the unit of measurement for the asset
		this.capacity = capacity; // Sets the capacity of the asset
		this.assetGroup = assetGroup; // Sets the group for the asset
		this.posX = posX; // Sets the X position of the asset
		this.posY = posY; // Sets the Y position of the asset
		this.children = children; // Sets the child assets for this asset
	}

	// This constructor helps create a new TreeData object with even more properties
	public TreeData(String id, String assetName, boolean checked, String label, String icon, String category,
			String image, List<String> users, List<Integer> logbook, String location, String manufacturer, String model,
			String serialno, String description, String unit, int capacity, String assetGroup, String posX, String posY,
			List<TreeData> children, int node_level) {
		super(); // Calls the parent class constructor (not usually needed here)
		this.id = id; // Sets the ID for the asset
		this.assetName = assetName; // Sets the name for the asset
		this.checked = checked; // Sets if the asset is checked
		this.label = label; // Sets the label for the asset
		this.icon = icon; // Sets the icon for the asset
		this.category = category; // Sets the category for the asset
		this.image = image; // Sets the image for the asset
		this.users = users; // Sets the list of users for the asset
		this.logbook = logbook; // Sets the logbook records for the asset
		this.location = location; // Sets where the asset is located
		this.manufacturer = manufacturer; // Sets who made the asset
		this.model = model; // Sets the model of the asset
		this.serialno = serialno; // Sets the unique serial number for the asset
		this.description = description; // Sets the description for the asset
		this.unit = unit; // Sets the unit of measurement for the asset
		this.capacity = capacity; // Sets the capacity of the asset
		this.assetGroup = assetGroup; // Sets the group for the asset
		this.posX = posX; // Sets the X position of the asset
		this.posY = posY; // Sets the Y position of the asset
		this.children = children; // Sets the child assets for this asset
		this.node_level = node_level; // Sets the level of this asset in the tree
	}

	// This constructor helps create a new TreeData object with a few properties
	public TreeData(String id, String assetName, String label, String icon) {
		super(); // Calls the parent class constructor (not usually needed here)
		this.id = id; // Sets the ID for the asset
		this.assetName = assetName; // Sets the name for the asset
		this.label = label; // Sets the label for the asset
		this.icon = icon; // Sets the icon for the asset
	}

	// This constructor helps create a new TreeData object with lots of properties, including department and groups
	public TreeData(String id, String assetName, boolean checked, String label, String icon, String category,
			String image, List<String> users, List<Integer> logbook, String location, String manufacturer, String model,
			String serialno, String description, String unit, int capacity, String assetGroup, String posX, String posY,
			List<TreeData> children, String department, List<String> groups) {
		super(); // Calls the parent class constructor (not usually needed here)
		this.id = id; // Sets the ID for the asset
		this.assetName = assetName; // Sets the name for the asset
		this.label = label; // Sets the label for the asset
		this.icon = icon; // Sets the icon for the asset
		this.checked = checked; // Sets if the asset is checked
		this.category = category; // Sets the category for the asset
		this.image = image; // Sets the image for the asset
		this.users = users; // Sets the list of users for the asset
		this.logbook = logbook; // Sets the logbook records for the asset
		this.location = location; // Sets where the asset is located
		this.manufacturer = manufacturer; // Sets who made the asset
		this.model = model; // Sets the model of the asset
		this.serialno = serialno; // Sets the unique serial number for the asset
		this.description = description; // Sets the description for the asset
		this.unit = unit; // Sets the unit of measurement for the asset
		this.capacity = capacity; // Sets the capacity of the asset
		this.assetGroup = assetGroup; // Sets the group for the asset
		this.posX = posX; // Sets the X position of the asset
		this.posY = posY; // Sets the Y position of the asset
		this.children = children; // Sets the child assets for this asset
		this.department = department; // Sets the department for this asset
		this.groups = groups; // Sets the list of groups for this asset
	}

	// Getters and setters allow us to access and change the properties of the asset

	public String getId() {
		return id; // Returns the ID of the asset
	}

	public void setId(String id) {
		this.id = id; // Updates the ID of the asset
	}

	public String getAssetName() {
		return assetName; // Returns the name of the asset
	}

	public void setAssetName(String assetName) {
		this.assetName = assetName; // Updates the name of the asset
	}

	public String getLabel() {
		return label; // Returns the label of the asset
	}

	public void setLabel(String label) {
		this.label = label; // Updates the label of the asset
	}

	public String getIcon() {
		return icon; // Returns the icon of the asset
	}

	public void setIcon(String icon) {
		this.icon = icon; // Updates the icon of the asset
	}

	public boolean isChecked() {
		return checked; // Returns if the asset is checked
	}

	public void setChecked(boolean checked) {
		this.checked = checked; // Updates if the asset is checked
	}

	public String getCategory() {
		return category; // Returns the category of the asset
	}

	public void setCategory(String category) {
		this.category = category; // Updates the category of the asset
	}

	public String getImage() {
		return image; // Returns the image of the asset
	}

	public void setImage(String image) {
		this.image = image; // Updates the image of the asset
	}

	public List<String> getUsers() {
		return users; // Returns the list of users for the asset
	}

	public void setUsers(List<String> users) {
		this.users = users; // Updates the list of users for the asset
	}

	public List<Integer> getLogbook() {
		return logbook; // Returns the logbook records for the asset
	}

	public void setLogbook(List<Integer> logbook) {
		this.logbook = logbook; // Updates the logbook records for the asset
	}

	public String getLocation() {
		return location; // Returns the location of the asset
	}

	public void setLocation(String location) {
		this.location = location; // Updates the location of the asset
	}

	public String getManufacturer() {
		return manufacturer; // Returns the manufacturer of the asset
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer; // Updates the manufacturer of the asset
	}

	public String getModel() {
		return model; // Returns the model of the asset
	}

	public void setModel(String model) {
		this.model = model; // Updates the model of the asset
	}

	public String getSerialno() {
		return serialno; // Returns the serial number of the asset
	}

	public void setSerialno(String serialno) {
		this.serialno = serialno; // Updates the serial number of the asset
	}

	public String getDescription() {
		return description; // Returns the description of the asset
	}

	public void setDescription(String description) {
		this.description = description; // Updates the description of the asset
	}

	public String getUnit() {
		return unit; // Returns the unit of measurement for the asset
	}

	public void setUnit(String unit) {
		this.unit = unit; // Updates the unit of measurement for the asset
	}

	public int getCapacity() {
		return capacity; // Returns the capacity of the asset
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity; // Updates the capacity of the asset
	}

	public String getAssetGroup() {
		return assetGroup; // Returns the group of the asset
	}

	public void setAssetGroup(String assetGroup) {
		this.assetGroup = assetGroup; // Updates the group of the asset
	}

	public String getPosX() {
		return posX; // Returns the X position of the asset
	}

	public void setPosX(String posX) {
		this.posX = posX; // Updates the X position of the asset
	}

	public String getPosY() {
		return posY; // Returns the Y position of the asset
	}

	public void setPosY(String posY) {
		this.posY = posY; // Updates the Y position of the asset
	}

	public List<TreeData> getChildren() {
		return children; // Returns the child assets of this asset
	}

	public void setChildren(List<TreeData> children) {
		this.children = children; // Updates the child assets of this asset
	}

	public int getNode_level() {
		return node_level; // Returns the level of this asset in the tree
	}

	public void setNode_level(int node_level) {
		this.node_level = node_level; // Updates the level of this asset in the tree
	}

	public String getDepartment() {
		return department; // Returns the department that owns the asset
	}

	public void setDepartment(String department) {
		this.department = department; // Updates the department for the asset
	}

	public List<String> getGroups() {
		return groups; // Returns the list of groups for this asset
	}

	public void setGroups(List<String> groups) {
		this.groups = groups; // Updates the list of groups for this asset
	}

	// This is a method that tells us how to compare two TreeData objects to see if they are the same
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true; // If this is the same object, we say they are equal
		if (!(obj instanceof TreeData)) return false; // If the other object is not a TreeData, they are not equal
		TreeData other = (TreeData) obj; // We compare their IDs to check if they are the same
		return Objects.equals(id, other.id); // Returns true if their IDs are the same
	}

	// This method helps us create a special text version of this object (for printing or displaying)
	@Override
	public String toString() {
		return "TreeData [id=" + id + ", assetName=" + assetName + ", label=" + label + ", icon=" + icon + ", checked="
				+ checked + ", category=" + category + ", image=" + image + ", users=" + users + ", logbook=" + logbook
				+ ", location=" + location + ", manufacturer=" + manufacturer + ", model=" + model + ", serialno="
				+ serialno + ", description=" + description + ", unit=" + unit + ", capacity=" + capacity
				+ ", assetGroup=" + assetGroup + ", posX=" + posX + ", posY=" + posY + ", children=" + children
				+ ", node_level=" + node_level + ", department=" + department + ", groups=" + groups + "]";
	}

}