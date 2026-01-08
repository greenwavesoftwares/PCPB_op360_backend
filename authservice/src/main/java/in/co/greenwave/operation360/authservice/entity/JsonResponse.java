package in.co.greenwave.operation360.authservice.entity;

/**
 * Represents a JSON response containing authentication and user information.
 * 
 * This class is used to encapsulate the response data for authentication requests.
 * It includes the status of the request, a token if the authentication is successful,
 * the tenant ID associated with the user, and the user details.
 * 
 * This class stores the authentication and user information to provide response as an object when an user signin.
 */
public class JsonResponse {

    private boolean status;
    private String token;
    private String tenantId;
    private User user;

    /**
     * Gets the status of the response.
     * 
     * @return true if the authentication was successful, false otherwise.
     */
    public boolean isStatus() {
        return status;
    }

    /**
     * Sets the status of the response.
     * 
     * @param status the status of the response, true if successful, false otherwise.
     */
    public void setStatus(boolean status) {
        this.status = status;
    }

    /**
     * Gets the token associated with the response.
     * 
     * @return the authentication token as a {@link String}, or null if authentication failed.
     */
    public String getToken() {
        return token;
    }

    /**
     * Sets the token for the response.
     * 
     * @param token the authentication token to set.
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * Gets the tenant ID associated with the user.
     * 
     * @return the tenant ID as a {@link String}, or null if authentication failed.
     */
    public String getTenantId() {
        return tenantId;
    }

    /**
     * Sets the tenant ID for the response.
     * 
     * @param tenantId the tenant ID to set.
     */
    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    /**
     * Gets the user details associated with the response.
     * 
     * @return the {@link User} object containing user details, or null if authentication failed.
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets the user details for the response.
     * 
     * @param user the {@link User} object to set.
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Provides a string representation of the {@link JsonResponse} object.
     * 
     * @return a {@link String} representing the {@link JsonResponse} object.
     */
    @Override
    public String toString() {
        return "JsonResponse [status=" + status + ", token=" + token + ", tenantId=" + tenantId + ", user=" + user + "]";
    }
}
