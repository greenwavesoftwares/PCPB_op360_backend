package in.co.greenwave.operation360.authservice.helper;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import in.co.greenwave.operation360.authservice.entity.Page;
import in.co.greenwave.operation360.authservice.entity.User;
import in.co.greenwave.operation360.authservice.entity.UserGroup;

@Component
public class UserModuleHelper {
	/**
	 * Helper method to split a comma-separated string into a list of strings.
	 */
	// the salt value
	public static final String SALT= "Pr!mef@ce$";

	/**
	 * Splits a comma-separated string into a list of individual strings.
	 * 
	 * @param value The comma-separated string.
	 * @return A list of strings or an empty list if the input is null or empty.
	 */
	public List<String> splitToList(String value) {
		return value != null && !value.isEmpty() ? Arrays.asList(value.split(",")) : new ArrayList<>();
	}

	/**
	 * Extracts a string value from a map safely.
	 * 
	 * @param row The map containing the data.
	 * @param key The key whose value needs to be extracted.
	 * @return The value as a string, or null if the key is not found.
	 */
	public String getString(Map<String, Object> row, String key) {
		Object value = row.get(key);
		return value != null ? value.toString() : null;
	}

	/**
	 * Extracts a boolean value from a map safely.
	 * 
	 * @param row The map containing the data.
	 * @param key The key whose value needs to be extracted.
	 * @return The value as a Boolean, or null if the key is not found.
	 */
	public Boolean getBoolean(Map<String, Object> row, String key) {
		Object value = row.get(key);
		return value != null ? (Boolean) value : null;
	}

	/**
	 * Extracts an integer value from a map safely.
	 * 
	 * @param row The map containing the data.
	 * @param key The key whose value needs to be extracted.
	 * @return The value as an Integer, or null if the key is not found.
	 */
	public Integer getInteger(Map<String, Object> row, String key) {
		Object value = row.get(key);
		return value != null ? (Integer) value : null;
	}

	/**
	 * Extracts a nullable string from a map safely.
	 * 
	 * @param row The map containing the data.
	 * @param key The key whose value needs to be extracted.
	 * @return The value as a string, or null if the key is not found.
	 */
	public String getNullableString(Map<String, Object> row, String key) {
		Object value = row.get(key);
		return value != null ? value.toString() : null;
	}

	//for generating the hash value;
	/**
	 * Generates a hashed value of a given string using SHA-1.
	 * 
	 * @param input The input string to hash.
	 * @return The hashed string in hexadecimal format.
	 */
	public  String generateHash(String input) {
		StringBuilder hash = new StringBuilder();

		try {
			MessageDigest sha = MessageDigest.getInstance("SHA-1");
			byte[] hashedBytes = sha.digest(input.getBytes());
			char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
					'a', 'b', 'c', 'd', 'e', 'f' };
			for (int idx = 0; idx < hashedBytes.length; ++idx) {
				byte b = hashedBytes[idx];
				hash.append(digits[(b & 0xf0) >> 4]);
				hash.append(digits[b & 0x0f]);
			}
		} catch (NoSuchAlgorithmException e) {
			// Handle the error if the hashing algorithm is unavailable.
			e.printStackTrace();
		}
		return hash.toString();
	}

	/**
	 * Compares two User objects and generates a string describing the differences
	 * between the selected user for update and the user before edit.
	 * 
	 * @param selectedUserForUpdate The User object representing the updated user.
	 * @param selectedUserBeforeEdit The User object representing the user before editing.
	 * @return A string detailing the changes made to the user.
	 */
	public String getChangeFieldDifferenceString(User selectedUserForUpdate , User selectedUserBeforeEdit) {
		// Log the old and new user data for debugging purposes
		System.out.println("old Data : "+selectedUserBeforeEdit);
		System.out.println("new Data : "+selectedUserForUpdate);

		// Initialize a string to store the changes
		String tempChangeString = "";

		// Check for username changes
		if(!selectedUserForUpdate.getUserName().equalsIgnoreCase(selectedUserBeforeEdit.getUserName())) {
			tempChangeString += "Username changed from " + selectedUserBeforeEdit.getUserName() + " to " + selectedUserForUpdate.getUserName() + "\r\n";
		}

		// Check for phone number changes
		if(!selectedUserForUpdate.getPhone().equalsIgnoreCase(selectedUserBeforeEdit.getPhone())) {
			tempChangeString += "Phone Number changed from " + selectedUserBeforeEdit.getPhone() + " to " + selectedUserForUpdate.getPhone() + "\r\n";
		}

		// Check for differences in user groups
		// Get the list of group names from the old and new user objects		
		List<String> tempOldGroupList = selectedUserBeforeEdit.getGroup()
				.stream()
				.map(UserGroup::getGroupName)  
				.collect(Collectors.toList());

		List<String> tempNewGroupList = selectedUserForUpdate.getGroup()
				.stream()
				.map(UserGroup::getGroupName)  
				.collect(Collectors.toList());

		// Find groups that were removed
		String tempRemovedGroupStringList = "";
		for(String oldGroup : tempOldGroupList) {
			if(!tempNewGroupList.contains(oldGroup)) {
				tempRemovedGroupStringList += oldGroup + ",";
			}
		}
		tempChangeString += (tempRemovedGroupStringList == null || tempRemovedGroupStringList.length() == 0)? "" :(tempRemovedGroupStringList.substring(0, tempRemovedGroupStringList.length() - 1)) + " groups removed from the list. \r\n";

		// Find groups that were added
		String tempAddedGroupStringList = "";
		for(String newGroup : tempNewGroupList) {
			if(!tempOldGroupList.contains(newGroup)) {
				tempAddedGroupStringList += newGroup + ",";
			}
		}
		tempChangeString += (tempAddedGroupStringList == null || tempAddedGroupStringList.length() == 0)? "" :(tempAddedGroupStringList.substring(0, tempAddedGroupStringList.length() - 1)) + " groups added to the list. \r\n";

		// Check if the department has changed
		if(!selectedUserForUpdate.getDepartment().getDeptID().equalsIgnoreCase(selectedUserBeforeEdit.getDepartment().getDeptID())) {
			tempChangeString += "Department changed from " + selectedUserBeforeEdit.getDepartment().getDeptID() + " to " + selectedUserForUpdate.getDepartment().getDeptID() + "\r\n";
		}

		// Check if the workflow home page has changed
		if(selectedUserForUpdate.getWorkFlowHomePage() != null && !selectedUserForUpdate.getWorkFlowHomePage().equalsIgnoreCase(selectedUserBeforeEdit.getWorkFlowHomePage())) {
			tempChangeString += "Workflow homepage changed from " + selectedUserBeforeEdit.getWorkFlowHomePage() + " to " + selectedUserForUpdate.getWorkFlowHomePage() + "\r\n";
		}

		// Check if the G360 home page has changed
		if(selectedUserForUpdate.getG360HomePage() != null && !selectedUserForUpdate.getG360HomePage().equalsIgnoreCase(selectedUserBeforeEdit.getG360HomePage())) {
			tempChangeString += "G360 Homepage changed from " + selectedUserBeforeEdit.getG360HomePage()+ " to " + selectedUserForUpdate.getG360HomePage() + "\r\n";
		}

		// Check if the G360 admin status has changed
		if(selectedUserForUpdate.isG360Admin() != selectedUserBeforeEdit.isG360Admin()) {
			tempChangeString += (selectedUserForUpdate.isG360Admin() ? "User has been provided G360 Admin" :  "User has been removed from Green360 Admin") +  "\r\n";
		}

		// Check if the user's active status has changed
		if(selectedUserForUpdate.isActive() != selectedUserBeforeEdit.isActive()) {
			tempChangeString += "User  changed from " + (selectedUserBeforeEdit.isActive() ? "Active" : "InActive" ) + " to " + (selectedUserForUpdate.isActive() ? "Active" : "InActive" )  + "\r\n";
		}

		// Check if the first login requirement has changed
		if(selectedUserForUpdate.isFirstLoginRequired() != selectedUserBeforeEdit.isFirstLoginRequired()) {
			tempChangeString += "First Login required changed from " + selectedUserBeforeEdit.isFirstLoginRequired()  + " to " + selectedUserForUpdate.isFirstLoginRequired() + "\r\n";
		}

		// Check if the password expiry duration has changed
		if((selectedUserForUpdate.getPasswordExpiryDuration() != selectedUserBeforeEdit.getPasswordExpiryDuration())) {
			tempChangeString += "Username changed from " + selectedUserBeforeEdit.getUserName() + " to " + selectedUserForUpdate.getUserName() + "\r\n";
		}

		//		for Allotted Pages , check using loop and detect removed and added
		// Check for differences in allotted pages
		// Get the list of page aliases from the old and new user objects
		List<String> tempOldAllottedPages = selectedUserBeforeEdit.getAllotedPages().stream()
				.map(Page::getAliasName)  
				.collect(Collectors.toList());
		List<String> tempNewAllottedPages = selectedUserForUpdate.getAllotedPages().stream()
				.map(Page::getAliasName)  
				.collect(Collectors.toList());

		// Find pages that were removed
		String tempRemovedPagesStringList = "";
		for(String oldPage : tempOldAllottedPages) {
			if(!tempNewAllottedPages.contains(oldPage)) {
				tempRemovedPagesStringList += oldPage + ",";
			}
		}
		tempChangeString += (tempRemovedPagesStringList == null || tempRemovedPagesStringList.length() == 0)? "" :(tempRemovedPagesStringList.substring(0, tempRemovedPagesStringList.length() - 1)) + " pages removed from the list. \r\n";

		// Find pages that were added
		String tempAddedPagesStringList = "";
		for(String newPage : tempNewAllottedPages) {
			if(!tempOldAllottedPages.contains(newPage)) {
				tempAddedPagesStringList = newPage + ",";
			}
		}
		tempChangeString += (tempAddedPagesStringList == null || tempAddedPagesStringList.length() == 0)? "" :(tempAddedPagesStringList.substring(0, tempAddedPagesStringList.length() - 1)) + " pages added to the list. \r\n";

		// Return the complete string describing the changes
		return tempChangeString;
	}

}

