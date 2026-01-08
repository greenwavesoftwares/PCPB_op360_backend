package in.co.greenwave.entity;

import com.fasterxml.jackson.annotation.JsonFormat; // Importing annotation for date formatting
import com.fasterxml.jackson.annotation.JsonInclude; // Importing annotation to control JSON serialization

/**
 * This class represents a User model in the system which holds information about a @User.
 * 
 * A UserModel contains information about a user, including their personal
 * details, contact information, and plant-related data.
 */
@JsonFormat // This annotation can be used to specify date formats if needed
@JsonInclude(JsonInclude.Include.NON_NULL) // This annotation ensures that only non-null fields are included in the JSON output
public class UserModel {
	// Unique identifier for the tenant (user group) to which this user belongs
	private String tenantId;

	// The name of the user who is an admin
	private String adminName;

	// The user's job title or position
	private String designation;

	// The user's email address
	private String email;

	// The user's password for account access
	private String password;

	// The user's phone number
	private String phoneNumber;

	// The unique identifier for the user's plant (a location for operations)
	private String plantID;

	// The name of the plant where the user works
	private String plantName;

	// The user's address
	private String address;

	// The division or department within the organization
	private String division;

	// The name of the customer who owns the account
	private String accountOwnerCustomer;

	// The name of the account owner at GreenWave (GW)
	private String accountOwnerGW;

	// The URL of the user's home page or profile
	private String homepage;

	// The name of the package or plan the user is subscribed to
	private String packageName;

	// The name of the customer associated with this user
	private String customerName;

	// Getters and Setters to get and change the values of each field of UserModel object.
	public String getDesignation() {
		return designation;
	}
	public void setDesignation(String designation) {
		this.designation = designation;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getPlantID() {
		return plantID;
	}
	public void setPlantID(String plantID) {
		this.plantID = plantID;
	}
	public String getPlantName() {
		return plantName;
	}
	public void setPlantName(String plantName) {
		this.plantName = plantName;
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
	public String getAccountOwnerCustomer() {
		return accountOwnerCustomer;
	}
	public void setAccountOwnerCustomer(String accountOwnerCustomer) {
		this.accountOwnerCustomer = accountOwnerCustomer;
	}
	public String getAccountOwnerGW() {
		return accountOwnerGW;
	}
	public void setAccountOwnerGW(String accountOwnerGW) {
		this.accountOwnerGW = accountOwnerGW;
	}
	public String getHomepage() {
		return homepage;
	}
	public void setHomepage(String homepage) {
		this.homepage = homepage;
	}
	public String getTenantId() {
		return tenantId;
	}
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
	public String getAdminName() {
		return adminName;
	}
	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	/**
	 * Returns a string representation of the UserModel object.
	 * This is useful for debugging and displaying information.
	 */
	@Override
	public String toString() {
		return "UserModel [tenantId=" + tenantId + ", adminName=" + adminName + ", designation=" + designation
				+ ", email=" + email + ", password=" + password + ", phoneNumber=" + phoneNumber + ", plantID="
				+ plantID + ", plantName=" + plantName + ", address=" + address + ", division=" + division
				+ ", accountOwnerCustomer=" + accountOwnerCustomer + ", accountOwnerGW=" + accountOwnerGW
				+ ", homepage=" + homepage + ", packageName=" + packageName + ", customerName=" + customerName + "]";
	}
}
