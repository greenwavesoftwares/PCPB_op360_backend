package in.co.greenwave.dto;

import java.util.List;

/**
 * A wrapper class for holding a list of @CellDto objects.
 * This class is simply a container for storing and managing cell data.
 */
public class CellDetailsDto {

	// This variable holds the actual list of CellDto objects, which contain cell information.
	private List<CellDto> cellData;

	// Default constructor: This is called when we create a new CellDetailsDto object.
	// It doesn't do anything special but is necessary for creating the object.
	public CellDetailsDto() {
		super();  // This calls the parent class's constructor (Object class by default).
	}

	// Getter method: This method allows us to access the list of CellDto objects (cellData) from outside the class.
	public List<CellDto> getCellData() {
		return cellData;
	}

	// Setter method: This method allows us to assign a list of CellDto objects to the cellData variable.
	public void setCellData(List<CellDto> cellData) {
		this.cellData = cellData;
	}

	// This method returns a string representation of the CellDetailsDto object.
	// It helps when we want to print or log the object, showing the content of the cellData list.
	@Override
	public String toString() {
		return "CellDetailsDto [cellData=" + cellData + "]";
	}
}
