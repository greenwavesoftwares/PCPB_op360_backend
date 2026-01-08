package in.co.greenwave.assetapi.model;

// Importing necessary libraries
import java.util.List; // To use a list of items

import com.fasterxml.jackson.annotation.JsonFormat; // For formatting JSON data
import com.fasterxml.jackson.annotation.JsonInclude; // To control which fields are included in JSON

// This class represents a new group of assets
@JsonFormat // This annotation helps with JSON formatting
@JsonInclude(JsonInclude.Include.NON_NULL) // This means don't include fields that are null (empty)
public class AssetGroupNew {

    // Variables to store information about the asset group
    private String groupName; // The name of the asset group
    private String groupId; // A unique ID for the asset group
    private String createdBy; // The name of the person who created the group
    private String createdOn; // The date when the group was created
    private List<TreeData> assetInfo; // A list of asset information (TreeData objects)
    private String assetMapImage; // A link or path to an image showing the asset layout

    // Constructor that initializes group name, ID, creator, and creation date
    public AssetGroupNew(String groupName, String groupId, String createdBy, String createdOn) {
        super(); // Calls the parent class constructor (not usually needed here)
        this.groupName = groupName; // Sets the group name
        this.groupId = groupId; // Sets the group ID
        this.createdBy = createdBy; // Sets who created the group
        this.createdOn = createdOn; // Sets when the group was created
    }

    // Default constructor (no parameters)
    public AssetGroupNew() {
        super(); // Calls the parent class constructor
        // This constructor is empty, it's just here to create an object without any initial data
    }

    // Constructor that initializes group name and ID only
    public AssetGroupNew(String groupName, String groupId) {
        super(); // Calls the parent class constructor
        this.groupName = groupName; // Sets the group name
        this.groupId = groupId; // Sets the group ID
    }

    // Constructor that initializes all properties of the asset group
    public AssetGroupNew(String groupName, String groupId, String createdBy, String createdOn,
                         List<TreeData> assetInfo, String assetMapImage) {
        super(); // Calls the parent class constructor
        this.groupName = groupName; // Sets the group name
        this.groupId = groupId; // Sets the group ID
        this.createdBy = createdBy; // Sets who created the group
        this.createdOn = createdOn; // Sets when the group was created
        this.assetInfo = assetInfo; // Sets the list of asset information
        this.assetMapImage = assetMapImage; // Sets the asset map image
    }

    // Getter method to get the group name
    public String getGroupName() {
        return groupName; // Returns the group name
    }

    // Setter method to change the group name
    public void setGroupName(String groupName) {
        this.groupName = groupName; // Updates the group name
    }

    // Getter method to get the group ID
    public String getGroupId() {
        return groupId; // Returns the group ID
    }

    // Setter method to change the group ID
    public void setGroupId(String groupId) {
        this.groupId = groupId; // Updates the group ID
    }

    // Getter method to get asset information
    public List<TreeData> getAssetInfo() {
        return assetInfo; // Returns the list of asset information
    }

    // Setter method to change asset information
    public void setAssetInfo(List<TreeData> assetInfo) {
        this.assetInfo = assetInfo; // Updates the list of asset information
    }

    // Getter method to get the name of the creator
    public String getCreatedBy() {
        return createdBy; // Returns who created the group
    }

    // Setter method to change the creator's name
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy; // Updates the creator's name
    }

    // Getter method to get the creation date
    public String getCreatedOn() {
        return createdOn; // Returns when the group was created
    }

    // Setter method to change the creation date
    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn; // Updates the creation date
    }

    // Getter method to get the asset map image
    public String getAssetMapImage() {
        return assetMapImage; // Returns the asset map image
    }

    // Setter method to change the asset map image
    public void setAssetMapImage(String assetMapImage) {
        this.assetMapImage = assetMapImage; // Updates the asset map image
    }

    // This method returns a string representation of the AssetGroupNew object
    @Override
    public String toString() {
        return "AssetGroupNew [groupName=" + groupName + ", groupId=" + groupId + ", createdBy=" + createdBy
                + ", createdOn=" + createdOn + ", assetInfo=" + assetInfo + ", assetMapImage=" + assetMapImage + "]";
    }

}
