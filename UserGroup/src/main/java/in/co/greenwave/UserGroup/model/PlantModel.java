package in.co.greenwave.UserGroup.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonFormat
//lexicographically arranges the alphabets
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlantModel {
	// Name of the plant or facility (e.g., "Plant A", "Factory 1")
	private String plantName;

	// Unique identifier for the plant or facility (e.g., a code or number)
	private String plantID;

	// Physical address of the plant or facility (e.g., street address, city, country)
	private String address;

	// Division or department within the plant (e.g., "Manufacturing", "Quality Control")
	private String division;

	// Name of the customer associated with the plant or the job (e.g., company or individual name)
	private String customerName;

	
	public String getPlantName() {
		return plantName;
	}
	public void setPlantName(String plantName) {
		this.plantName = plantName;
	}
	public String getPlantID() {
		return plantID;
	}
	public void setPlantID(String plantID) {
		this.plantID = plantID;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getDivision() {
		return division;
	}
	public void setDivision(String division) {
		this.division = division;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	
	@Override
	public String toString() {
		return "PlantModel [plantName=" + plantName + ", plantID=" + plantID + ", address=" + address + ", division="
				+ division + ", customerName=" + customerName + "]";
	}
}

