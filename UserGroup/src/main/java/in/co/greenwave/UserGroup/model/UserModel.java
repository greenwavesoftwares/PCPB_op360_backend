package in.co.greenwave.UserGroup.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

// This class indentify details of a tenant
@JsonFormat
//lexicographically arranges the alphabets
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserModel {
	// Tenant ID: A unique identifier for the tenant (or customer) in a multi-tenant system.
	private String tenantId;

	// Admin Name: The name of the administrator or primary contact for the tenant.
	private String adminName;

	// Designation: The job title or role of the administrator (e.g., Manager, Director).
	private String designation;

	// Email: The email address associated with the administrator or tenant for communication.
	private String email;

	// Password: The password associated with the tenant's admin account for authentication.
	private String password;

	// Phone Number: The contact phone number for the administrator or the tenant.
	private String phoneNumber;

	// Plant ID: A unique identifier for the plant (or facility) associated with the tenant.
	private String plantID;

	// Plant Name: The name of the plant (or facility) associated with the tenant.
	private String plantName;

	// Address: The physical address of the plant or tenant location.
	private String address;

	// Division: The division or department within the organization that the tenant belongs to.
	private String division;

	// Account Owner (Customer): The customer who owns the account (could be an external organization or user).
	private String accountOwnerCustomer;

	// Account Owner (GW): The internal owner or account manager for the tenant's account.
	private String accountOwnerGW;

	// Homepage: The URL of the tenant’s homepage or website.
	private String homepage;

	// Package Name: The name of the package or service plan that the tenant is subscribed to.
	private String packageName;

	// Customer Name: The name of the customer associated with the tenant, if different from the tenant name.
	private String customerName;

	// Database Username: The username used to authenticate and access the tenant's database.
	private String databaseUsername;

	// Database Name: The name of the database associated with the tenant.
	private String databaseName;

	// Database IP: The IP address of the server where the tenant's database is hosted.
	private String databaseIP;

	// Database Password: The password associated with the database username for secure access.
	private String databasePassword;

	private String databaseDriver;

	private String noOfUsers;
	
	private String poNo;

	private String startDate;

	private String expiryDate;

	private String gracePeriod;

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

	public String getDatabaseUsername() {
		return databaseUsername;
	}

	public void setDatabaseUsername(String databaseUsername) {
		this.databaseUsername = databaseUsername;
	}

	public String getDatabaseName() {
		return databaseName;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	public String getDatabaseIP() {
		return databaseIP;
	}

	public void setDatabaseIP(String databaseIP) {
		this.databaseIP = databaseIP;
	}

	public String getDatabasePassword() {
		return databasePassword;
	}

	public void setDatabasePassword(String databasePassword) {
		this.databasePassword = databasePassword;
	}

	public String getDatabaseDriver() {
		return databaseDriver;
	}

	public void setDatabaseDriver(String databaseDriver) {
		this.databaseDriver = databaseDriver;
	}

	public String getNoOfUsers() {
		return noOfUsers;
	}

	public void setNoOfUsers(String noOfUsers) {
		this.noOfUsers = noOfUsers;
	}

	public String getPoNo() {
		return poNo;
	}

	public void setPoNo(String poNo) {
		this.poNo = poNo;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}

	public String getGracePeriod() {
		return gracePeriod;
	}

	public void setGracePeriod(String gracePeriod) {
		this.gracePeriod = gracePeriod;
	}

	@Override
	public String toString() {
		return "UserModel [tenantId=" + tenantId + ", adminName=" + adminName + ", designation=" + designation
				+ ", email=" + email + ", password=" + password + ", phoneNumber=" + phoneNumber + ", plantID="
				+ plantID + ", plantName=" + plantName + ", address=" + address + ", division=" + division
				+ ", accountOwnerCustomer=" + accountOwnerCustomer + ", accountOwnerGW=" + accountOwnerGW
				+ ", homepage=" + homepage + ", packageName=" + packageName + ", customerName=" + customerName
				+ ", databaseUsername=" + databaseUsername + ", databaseName=" + databaseName + ", databaseIP="
				+ databaseIP + ", databasePassword=" + databasePassword + ", databaseDriver=" + databaseDriver
				+ ", noOfUsers=" + noOfUsers + ", poNo=" + poNo + ", startDate=" + startDate + ", expiryDate="
				+ expiryDate + ", gracePeriod=" + gracePeriod + "]";
	}

}
