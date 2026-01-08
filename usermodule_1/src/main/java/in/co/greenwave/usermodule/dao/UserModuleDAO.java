package in.co.greenwave.usermodule.dao;

import java.util.List;
import java.util.Map;

import in.co.greenwave.usermodule.model.Page;
import in.co.greenwave.usermodule.model.User;
import in.co.greenwave.usermodule.model.UserDepartment;
import in.co.greenwave.usermodule.model.UserGroup;
import in.co.greenwave.usermodule.model.UserLog;

public interface UserModuleDAO {

    /**
     * Retrieves the details of all users within a specific tenant.
     *
     * @param tenantId the identifier of the tenant.
     * @return a list of User objects containing user details.
     */
    List<User> getAllUsersDetails(String tenantId);

    /**
     * Retrieves all user groups within a specific tenant.
     *
     * @param tenantId the identifier of the tenant.
     * @return a list of UserGroup objects containing group details.
     */
    List<UserGroup> getAllGroups(String tenantId);

    /**
     * Retrieves all departments within a specific tenant.
     *
     * @param tenantId the identifier of the tenant.
     * @return a list of UserDepartment objects containing department details.
     */
    List<UserDepartment> getAllDepartments(String tenantId);

    /**
     * Retrieves details of all pages available for a specific tenant.
     *
     * @param tenantId the identifier of the tenant.
     * @return a list of Page objects containing page details.
     */
    List<Page> getAllPageDetails(String tenantId);

    /**
     * Retrieves a mapping of pages OP360 and G360
     *
     * @param tenantId the identifier of the tenant.
     * @return a map where keys represent a mapping of pages  OP360 and G360
     */
    Map<String, List<Page>> getPagesMap(String tenantId);

    /**
     * Retrieves details of a specific user within a tenant.
     *
     * @param tenantId the identifier of the tenant.
     * @param userId the identifier of the user.
     * @return a User object containing user details.
     */
    User getUserDetails(String tenantId, String userId);

    /**
     * Retrieves logs of a specific user within a tenant.
     *
     * @param tenantId the identifier of the tenant.
     * @param userId the identifier of the user.
     * @return a list of UserLog objects containing the user's log details.
     */
    List<UserLog> getUserLogs(String tenantId, String userId);

    /**
     * Checks whether a specific group is associated with a tenant.
     *
     * @param groupId the identifier of the group.
     * @param groupName the name of the group.
     * @param tenantId the identifier of the tenant.
     * @return a string indicating the association status.
     */
    String checkGroupAssociation(String groupId, String groupName, String tenantId);

    /**
     * Checks whether a specific department is associated with a tenant.
     *
     * @param deptId the identifier of the department.
     * @param tenantId the identifier of the tenant.
     * @return a string indicating the association status.
     */
    String checkDepartmentAssociation(String deptId, String tenantId);

    /**
     * Saves or updates user details.
     *
     * @param user a User object containing the details to be saved or updated.
     * @return true if the operation was successful, false otherwise.
     */
    boolean saveOrUpdateUser(User user);

    /**
     * Saves a new user group.
     *
     * @param group a UserGroup object containing group details.
     * @return true if the group was successfully saved, false otherwise.
     */
    boolean saveNewGroup(UserGroup group);

    /**
     * Saves a new department.
     *
     * @param department a UserDepartment object containing department details.
     * @return true if the department was successfully saved, false otherwise.
     */
    boolean saveNewDepartment(UserDepartment department);

    /**
     * Merges multiple users into a single group.
     *
     * @param userList a list of user identifiers to be merged.
     * @param groupId the identifier of the group.
     * @param tenantId the identifier of the tenant.
     * @return true if the users were successfully merged, false otherwise.
     */
    boolean saveMergedGroups(List<String> userList, String groupId, String tenantId);

    /**
     * Changes the password for a specific user.
     *
     * @param user a User object containing user details and the new password.
     * @return true if the password was successfully changed, false otherwise.
     */
    boolean changePassword(User user);

    /**
     * Updates the details of an existing department.
     *
     * @param department a UserDepartment object containing updated details.
     * @return true if the department was successfully updated, false otherwise.
     */
    boolean updateDepartment(UserDepartment department);

    /**
     * Updates the details of an existing user group.
     *
     * @param group a UserGroup object containing updated group details.
     * @return true if the group was successfully updated, false otherwise.
     */
    boolean updateGroup(UserGroup group);

    /**
     * Deletes a specific user group.
     *
     * @param group a UserGroup object representing the group to be deleted.
     * @return true if the group was successfully deleted, false otherwise.
     */
    boolean deleteGroup(UserGroup group);

    /**
     * Deletes a specific department.
     *
     * @param dept a UserDepartment object representing the department to be deleted.
     * @return true if the department was successfully deleted, false otherwise.
     */
    boolean deleteDepartment(UserDepartment dept);

    /**
     * Updates the login time for a specific user.
     *
     * @param userId the identifier of the user.
     * @param tenantId the identifier of the tenant.
     * @return true if the login time was successfully updated, false otherwise.
     */
    boolean updateLoginTime(String userId, String tenantId);

    /**
     * Verifies if the provided old password matches the user's current password.
     *
     * @param userId the identifier of the user.
     * @param oldPassword the old password to be verified.
     * @param tenantId the identifier of the tenant.
     * @return true if the old password matches, false otherwise.
     */
    boolean checkOldPassword(String userId, String oldPassword, String tenantId);

	void registerNewTenant(String tenantid);

	Map<String, List<String>> getAllUserIdsByGroup(String tenantId);
}
