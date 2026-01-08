package in.co.greenwave.entity;

/**
 * Represents an item in a dropdown menu for a cell.
 * 
 * <p>
 * This class stores information about each option available in a dropdown list.
 * Each option has a label (the text shown to users) and a value (the underlying data 
 * associated with that option). For example, if you have a dropdown for selecting colors, 
 * the label could be "Red" and the value could be "red". 
 * </p>
 */
public class DropDownInfo {

	// The text that appears in the dropdown for this item.
	String itemLabel;

	// The value associated with this dropdown item, used for data processing.
	String itemValue;

	// Default constructor: Creates an empty DropDownInfo object.
	public DropDownInfo() {
	}

	/**
	 * Constructor to initialize a DropDownInfo object with a label and value.
	 * 
	 * @param itemLabel the text shown in the dropdown
	 * @param itemValue the value associated with this item
	 */
	public DropDownInfo(String itemLabel, String itemValue) {
		super();
		this.itemLabel = itemLabel;
		this.itemValue = itemValue;
	}

	// Returns the label of the dropdown item.
	public String getItemLabel() {
		return itemLabel;
	}

	// Sets the label for the dropdown item.
	public void setItemLabel(String itemLabel) {
		this.itemLabel = itemLabel;
	}

	// Returns the value of the dropdown item.
	public String getItemValue() {
		return itemValue;
	}

	// Sets the value for the dropdown item.
	public void setItemValue(String itemValue) {
		this.itemValue = itemValue;
	}

	/**
	 * Returns a string representation of the DropDownInfo object.
	 * This is useful for debugging and displaying information.
	 * 
	 * @return a string describing the dropdown item
	 */
	@Override
	public String toString() {
		return "DropDownInfo [itemLabel=" + itemLabel + ", itemValue=" + itemValue + "]";
	}
}
