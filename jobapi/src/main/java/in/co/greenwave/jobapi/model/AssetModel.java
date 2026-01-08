package in.co.greenwave.jobapi.model;
import java.util.ArrayList;
import java.util.List;

public class AssetModel {

	private String nodeName;
	private String aliasName;
	private boolean checked;
	private String icon;
	private String category;
	private String image;
	private List<String> usersList;
	private List<Integer> logbooksList;

	private String location;
	private String manufacturer;
	private String model;
	private String serialNo;
	private String description;
	private String uom;
	private int capacity;
	
	private String assetGroup;
	private String posX;
	private String posY;


	public AssetModel() {
		super();
		// TODO Auto-generated constructor stub
	}
	public AssetModel(String nodeName, String aliasName) {
		super();
		this.nodeName = nodeName;
		this.aliasName = aliasName;
	}
	public AssetModel(String nodeName, String aliasName, String icon) {
		super();
		this.nodeName = nodeName;
		this.aliasName = aliasName;
		this.icon = icon;
	}

	public AssetModel(String nodeName, String aliasName, boolean checked, String icon, String category, String image) {
		super();
		this.nodeName = nodeName;
		this.aliasName = aliasName;
		this.checked = checked;
		this.icon = icon;
		this.category = category;
		this.image = image;
	}


	// new asset contructor for asset logbook allocation using java
	public AssetModel(String nodeName, String aliasName, String icon,
			List<String> usersList, List<Integer> logbooksList) {
		super();
		this.nodeName = nodeName;
		this.aliasName = aliasName;
		this.icon = icon;
		this.usersList = usersList;
		this.logbooksList = logbooksList;
	}

	public AssetModel(String assetGroup, String nodeName, String aliasName, String posX, String posY,String icon) {
		super();
		this.setAssetGroup(assetGroup);
		this.nodeName = nodeName;
		this.aliasName = aliasName;
		this.posX = posX;
		this.posY = posY;
		this.icon = icon;
	}

	//new Constructor for Restful Api 
	
	public AssetModel(String nodeName, String aliasName,  List<Integer> logbooksList, String location, String manufacturer) {
		super();
		this.nodeName=nodeName;
		this.aliasName=aliasName;
		this.logbooksList=logbooksList;
		
		this.location=location;
		this.manufacturer=manufacturer;
	}
	
	//For RestApi
	public AssetModel(String nodeName, String aliasName,  String icon, String category,
			List<String> usersList, List<Integer> logbooksList, String location, String manufacturer, String model,
			String serialNo, String description, int capacity) {
		super();
		this.nodeName = nodeName;
		this.aliasName = aliasName;
		this.icon = icon;
		this.category = category;
		this.usersList = usersList;
		this.logbooksList = logbooksList;
		this.location = location;
		this.manufacturer = manufacturer;
		this.model = model;
		this.serialNo = serialNo;
		this.description = description;
		this.capacity = capacity;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nodeName == null) ? 0 : nodeName.hashCode());
		return result;
	}
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




	@Override
	public String toString() {
		return "AssetModel [nodeName=" + nodeName + ", aliasName=" + aliasName + ", checked=" + checked + ", icon="
				+ icon + ", category=" + category + ", image=" + image + ", usersList=" + usersList + ", logbooksList="
				+ logbooksList + "]";
	}
	public String getNodeName() {
		return nodeName;
	}
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
	public String getAliasName() {
		return aliasName;
	}
	public void setAliasName(String aliasName) {
		this.aliasName = aliasName;
	}
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public List<String> getUsersList() {
		return usersList;
	}
	public void setUsersList(List<String> usersList) {
		this.usersList = usersList;
	}
	
	public List<Integer> getLogbooksList() {
		return logbooksList;
	}
	public void setLogbooksList(List<Integer> logbooksList) {
		this.logbooksList = logbooksList;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getSerialNo() {
		return serialNo;
	}
	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getUom() {
		return uom;
	}
	public void setUom(String uom) {
		this.uom = uom;
	}
	public int getCapacity() {
		return capacity;
	}
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	public String getPosX() {
		return posX;
	}
	public void setPosX(String posX) {
		this.posX = posX;
	}
	public String getPosY() {
		return posY;
	}
	public void setPosY(String posY) {
		this.posY = posY;
	}
	public String getAssetGroup() {
		return assetGroup;
	}
	public void setAssetGroup(String assetGroup) {
		this.assetGroup = assetGroup;
	}

}

