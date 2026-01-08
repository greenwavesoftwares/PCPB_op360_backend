package in.co.greenwave.operation360.authservice.entity;

/**
 * The UserDepartment class represents a department within an organization.
 * It contains details like the department ID, department name, and tenant ID.
 */
public class UserDepartment {
    private String deptID; // Unique identifier for the department
    private String deptName; // Name of the department
    private String tenantId; // The tenant ID associated with this department 

    /**
     * Constructor that allows initializing all fields of the UserDepartment class.
     * 
     * @param deptID   The unique ID of the department.
     * @param deptName The name of the department.
     * @param tenantId The tenant ID.
     */
    public UserDepartment(String deptID, String deptName, String tenantId) {
        super();
        this.deptID = deptID;
        this.deptName = deptName;
        this.tenantId = tenantId;
    }

    /**
     * Default constructor for creating an empty UserDepartment object.
     */
    public UserDepartment() {
        super();
    }

    /**
     * Gets the department ID.
     * 
     * @return The unique ID of the department.
     */
    public String getDeptID() {
        return deptID;
    }

    /**
     * Sets the department ID.
     * 
     * @param deptID The unique ID of the department to be set.
     */
    public void setDeptID(String deptID) {
        this.deptID = deptID;
    }

    /**
     * Gets the department name.
     * 
     * @return The name of the department.
     */
    public String getDeptName() {
        return deptName;
    }

    /**
     * Sets the department name.
     * 
     * @param deptName The name of the department to be set.
     */
    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    /**
     * Gets the tenant ID.
     * 
     * @return The tenant ID associated with this department.
     */
    public String getTenantId() {
        return tenantId;
    }

    /**
     * Sets the tenant ID.
     * 
     * @param tenantId The tenant ID to be set.
     */
    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    /**
     * Provides a string representation of the UserDepartment object.
     * This helps to easily print out all details of the department.
     * 
     * @return A string that shows the department ID, name, and tenant ID.
     */
    @Override
    public String toString() {
        return "UserDepartment [deptID=" + deptID + ", deptName=" + deptName + ", tenantId=" + tenantId + "]";
    }
}
