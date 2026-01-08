package in.co.greenwave.usermodule.helper;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import in.co.greenwave.usermodule.model.Page;
import in.co.greenwave.usermodule.model.User;
import in.co.greenwave.usermodule.model.UserGroup;

@Component
public class UserModuleHelper {
	/**
	 * Helper method to split a comma-separated string into a list of strings.
	 */
	// the salt value
	public static final String SALT= "Pr!mef@ce$";
	
	public List<String> splitToList(String value) {
	    return value != null && !value.isEmpty() ? Arrays.asList(value.split(",")) : new ArrayList<>();
	}

	/**
	 * Helper method to safely extract a String from a row map.
	 */
	public String getString(Map<String, Object> row, String key) {
	    Object value = row.get(key);
	    return value != null ? value.toString() : null;
	}

	/**
	 * Helper method to safely extract a Boolean from a row map.
	 */
	public Boolean getBoolean(Map<String, Object> row, String key) {
	    Object value = row.get(key);
	    return value != null ? (Boolean) value : null;
	}

	/**
	 * Helper method to safely extract an Integer from a row map.
	 */
	public Integer getInteger(Map<String, Object> row, String key) {
	    Object value = row.get(key);
	    return value != null ? (Integer) value : null;
	}

	/**
	 * Helper method to safely extract a nullable String from a row map.
	 */
	public String getNullableString(Map<String, Object> row, String key) {
	    Object value = row.get(key);
	    return value != null ? value.toString() : null;
	}
	
	//for generating the hash value;
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
			// handle error here.
			e.printStackTrace();
		}
 
		return hash.toString();
	}
	
	
	public String getChangeFieldDifferenceString(User selectedUserForUpdate , User selectedUserBeforeEdit) {
		System.out.println("old Data : "+selectedUserBeforeEdit);
		System.out.println("new Data : "+selectedUserForUpdate);
		String tempChangeString = "";
		System.out.println(" OLD Username : "+selectedUserBeforeEdit.getUserName());
		System.out.println(" NEW Username : "+selectedUserForUpdate.getUserName());
		System.out.println("True or False : "+selectedUserForUpdate.getUserName().equalsIgnoreCase(selectedUserBeforeEdit.getUserName()));
		if(!selectedUserForUpdate.getUserName().equalsIgnoreCase(selectedUserBeforeEdit.getUserName())) {
			tempChangeString += "Username changed from " + selectedUserBeforeEdit.getUserName() + " to " + selectedUserForUpdate.getUserName() + "\r\n";
		}
 
		if(! (selectedUserForUpdate.getPhone()==null?"":selectedUserForUpdate.getPhone())
			.equalsIgnoreCase
			(selectedUserBeforeEdit.getPhone()==null?"":selectedUserBeforeEdit.getPhone())) 
		{
			
			tempChangeString += "Phone Number changed from " + selectedUserBeforeEdit.getPhone() + " to " + selectedUserForUpdate.getPhone() + "\r\n";
			
		}
 
		//for User group , check using loop and detect removed and added
		List<String> tempOldGroupList = selectedUserBeforeEdit.getGroup()
			    .stream()
			    .map(UserGroup::getGroupName)  
			    .collect(Collectors.toList());

		List<String> tempNewGroupList = selectedUserForUpdate.getGroup()
			    .stream()
			    .map(UserGroup::getGroupName)  
			    .collect(Collectors.toList());
 
		String tempRemovedGroupStringList = "";
		for(String oldGroup : tempOldGroupList) {
			if(!tempNewGroupList.contains(oldGroup)) {
				tempRemovedGroupStringList += oldGroup + ",";
			}
		}
		tempChangeString += (tempRemovedGroupStringList == null || tempRemovedGroupStringList.length() == 0)? "" :(tempRemovedGroupStringList.substring(0, tempRemovedGroupStringList.length() - 1)) + " groups removed from the list. \r\n";
 
		String tempAddedGroupStringList = "";
		for(String newGroup : tempNewGroupList) {
			if(!tempOldGroupList.contains(newGroup)) {
				tempAddedGroupStringList += newGroup + ",";
			}
		}
		tempChangeString += (tempAddedGroupStringList == null || tempAddedGroupStringList.length() == 0)? "" :(tempAddedGroupStringList.substring(0, tempAddedGroupStringList.length() - 1)) + " groups added to the list. \r\n";
		
		if(selectedUserForUpdate.getDepartment()==null || selectedUserBeforeEdit.getDepartment()==null )
		{
			System.err.println("Either of the department was null");
			tempChangeString += "Department changed. "+"\r\n";
		}
		else if(!selectedUserForUpdate.getDepartment().getDeptID().equalsIgnoreCase(selectedUserBeforeEdit.getDepartment().getDeptID())) {
			tempChangeString += "Department changed from " + selectedUserBeforeEdit.getDepartment().getDeptID() + " to " + selectedUserForUpdate.getDepartment().getDeptID() + ".\r\n";
		}
 
		if(selectedUserForUpdate.getWorkFlowHomePage() != null && !selectedUserForUpdate.getWorkFlowHomePage().equalsIgnoreCase(selectedUserBeforeEdit.getWorkFlowHomePage())) {
			tempChangeString += "Workflow homepage changed from " + selectedUserBeforeEdit.getWorkFlowHomePage() + " to " + selectedUserForUpdate.getWorkFlowHomePage() + ".\r\n";
		}
 
		if(selectedUserForUpdate.getG360HomePage() != null && !selectedUserForUpdate.getG360HomePage().equalsIgnoreCase(selectedUserBeforeEdit.getG360HomePage())) {
			tempChangeString += "G360 Homepage changed from " + selectedUserBeforeEdit.getG360HomePage()+ " to " + selectedUserForUpdate.getG360HomePage() + ".\r\n";
		}
 
		if(selectedUserForUpdate.isG360Admin() != selectedUserBeforeEdit.isG360Admin()) {
			tempChangeString += (selectedUserForUpdate.isG360Admin() ? "User has been provided G360 Admin" :  "User has been removed from Green360 Admin") +  "\r\n";
		}
 
		if(selectedUserForUpdate.isActive() != selectedUserBeforeEdit.isActive()) {
			tempChangeString += "User  changed from " + (selectedUserBeforeEdit.isActive() ? "Active" : "InActive" ) + " to " + (selectedUserForUpdate.isActive() ? "Active" : "InActive" )  + "\r\n";
		}
 
		if(selectedUserForUpdate.isFirstLoginRequired() != selectedUserBeforeEdit.isFirstLoginRequired()) {
			tempChangeString += "First Login required changed from " + selectedUserBeforeEdit.isFirstLoginRequired()  + " to " + selectedUserForUpdate.isFirstLoginRequired() + "\r\n";
		}
 
		if((selectedUserForUpdate.getPasswordExpiryDuration() != selectedUserBeforeEdit.getPasswordExpiryDuration())) {
			tempChangeString += "Password Expiry duration changed from " + selectedUserBeforeEdit.getPasswordExpiryDuration() + " to " + selectedUserForUpdate.getPasswordExpiryDuration() + "\r\n";
		}
 
		//		
		//		for Allotted Pages , check using loop and detect removed and added
		List<String> tempOldAllottedPages = selectedUserBeforeEdit.getAllotedPages().stream()
			    												  .map(Page::getAliasName)  
			    												  .collect(Collectors.toList());
		List<String> tempNewAllottedPages = selectedUserForUpdate.getAllotedPages().stream()
			    												 .map(Page::getAliasName)  
			    												 .collect(Collectors.toList());
 
		String tempRemovedPagesStringList = "";
		for(String oldPage : tempOldAllottedPages) {
			if(!tempNewAllottedPages.contains(oldPage)) {
				tempRemovedPagesStringList += oldPage + ",";
			}
		}
		tempChangeString += (tempRemovedPagesStringList == null || tempRemovedPagesStringList.length() == 0)? "" :(tempRemovedPagesStringList.substring(0, tempRemovedPagesStringList.length() - 1)) + " pages removed from the list. \r\n";
 
		String tempAddedPagesStringList = "";
		for(String newPage : tempNewAllottedPages) {
			if(!tempOldAllottedPages.contains(newPage)) {
				tempAddedPagesStringList = newPage + ",";
			}
		}
		tempChangeString += (tempAddedPagesStringList == null || tempAddedPagesStringList.length() == 0)? "" :(tempAddedPagesStringList.substring(0, tempAddedPagesStringList.length() - 1)) + " pages added to the list. \r\n";
		
 
		return tempChangeString;
	}

}
