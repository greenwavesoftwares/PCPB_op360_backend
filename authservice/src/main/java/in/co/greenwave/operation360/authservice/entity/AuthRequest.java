package in.co.greenwave.operation360.authservice.entity;

/**
 * Represents an authentication request containing user credentials.
 * 
 * This class is used to encapsulate the data required for user authentication,
 * specifically the username and password. It provides getter and setter methods
 * to access and modify these fields.
 */
public class AuthRequest {

    private String username;
    private String password;
    private String tenantId;

    /**
     * Gets the username associated with the authentication request.
     * 
     * @return the username as a {@link String}.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username for the authentication request.
     * 
     * @param username the username to set.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the password associated with the authentication request.
     * 
     * @return the password as a {@link String}.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password for the authentication request.
     * 
     * @param password the password to set.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the tenantId associated with the authentication request.
     * 
     * @return the tenantId as a {@link String}.
     */
	public String getTenantId() {
		return tenantId;
	}

	/**
     * Sets the tenantId for the authentication request.
     * 
     * @param tenantId the tenantId to set.
     */
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
}
