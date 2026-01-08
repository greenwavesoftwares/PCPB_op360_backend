package in.co.greenwave.taskapi.dao;
import java.util.List;

import in.co.greenwave.taskapi.model.User;
import in.co.greenwave.taskapi.model.UserDepartment;
import in.co.greenwave.taskapi.model.UserGroup;



public interface LoginDAO {
	public List<User> getAllUserInfo();
	public List<UserGroup> getAllUserGroup();
	public List<User> getUserFromAllotedPage(String allotedPage);
	public abstract List<UserDepartment> getAllUserDept();
	
	
//	public String getUserWiseHomepage(String userId);
//	public List<String> userWiseAllotedPages(String userId,String source);
//	public boolean login(String userid,String password);
//	public void updateLastLogin(String userid);
//	public void addorEditUser(User user,boolean newUser , User olderVersionOfUser , String changeList);
//	public int getActiveUsers();
//	public void changePassword(User user , String modifiedBy , User oldVersionOfUser);
//	public User getUserInfo(String userid , String userSrouce);
//	public List<String> getPagesForSource(String sourceName);
//	public void addLeaveApplication(List<HolidayDB> holidayData);
//	public List<HolidayDB> getPendingLeaveApproval(String approver);
//	public void respondToLeaveApplications(String userid , String fromdate, String todate ,String approverComment,String status);
//	public List<HolidayDB> getAllHolidays();
//	public void updateProfile(User user , String modifiedBy , String modifiedLog , User oldVersionOfUser);
//	public List<HolidayDB> getMyLeave(String userId);
//	public void createOrUpdate_OneUserGroup(UserGroup userGroup, boolean updateCall);
//	public void createOrUpdate_Department(UserDepartment userDept, boolean updateCall);
//	public List<UserDepartment> getAllUserDept();
//	public void updateWrongPasswords(String userId);
//	public int wrongPasswrodIn24Hours(String userId , int hours);
//	public void makeUserInActive(String userId);
//	public boolean isUserActive(String userId);
//	public void deleteAllWrongPasswords(String userId , boolean leftLast);
//	public List<UserLog> getUserlogForOneUser(String userId);

}
