package in.co.greenwave.taskapi.dao.implementation.sqlserver;


import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import in.co.greenwave.taskapi.dao.LoginDAO;
import in.co.greenwave.taskapi.model.User;
import in.co.greenwave.taskapi.model.UserDepartment;
import in.co.greenwave.taskapi.model.UserGroup;


@Repository
public class LoginService implements LoginDAO {

	@Autowired
	@Qualifier("jdbcTemplate1")
	public JdbcTemplate jdbcTemplate;
	@Autowired
	@Qualifier("jdbcTemplate2")
	public JdbcTemplate OP360_UsermodulejdbcTemplate;
	
	/**
	 * Retrieves all user groups from the database.
	 *
	 * @return List of UserGroup containing information about user groups.
	 */
	@Override
	public List<UserGroup> getAllUserGroup() {

		String query = "SELECT [GroupId], [GroupName], [PhoneNumber], [HomePage], [Active] FROM [dbo].[GroupCredentials]";

		List<UserGroup> userGroups = OP360_UsermodulejdbcTemplate.query(query, (rs, rowNum) -> {
			UserGroup group = new UserGroup();
			group.setGroupID(rs.getString("GroupId"));
			group.setGroupName(rs.getString("GroupName"));
			// Assuming PhoneNumber is a String in the UserGroup class
			group.setPhone(BigInteger.valueOf(rs.getInt("PhoneNumber")));
			group.setHomePage(rs.getString("HomePage"));
			group.setActive(rs.getBoolean("Active"));
			return group;
		});

		return userGroups;
	}

	/**
	 * Retrieves all user information from the database.
	 *
	 * @return List of User containing information about users.
	 */
	@Override
	public List<User> getAllUserInfo() {
		List<User> userList = new LinkedList<>();

		try {
			String g360PagesQuery = "SELECT [Page] FROM [dbo].[Pages] WHERE [Source] = ?";
			HashSet<String> g360PageList = new HashSet<>(OP360_UsermodulejdbcTemplate.queryForList(g360PagesQuery, String.class, "G360"));

			String workflowPagesQuery = "SELECT [Page] FROM [dbo].[Pages] WHERE [Source] = ?";
			HashSet<String> workFlowPageList = new HashSet<>(OP360_UsermodulejdbcTemplate.queryForList(workflowPagesQuery, String.class, "Workflow"));

			String userCredentialQuery = "SELECT * FROM [dbo].[UserCredential]";
			SqlRowSet rs = OP360_UsermodulejdbcTemplate.queryForRowSet(userCredentialQuery);
			while (rs.next()) {
				User user = new User(
						rs.getString("UserId"),
						rs.getString("UserName"),
						rs.getString("Password"),
						rs.getString("PhoneNumber"),
						rs.getString("WorkflowHomepage"),
						rs.getString("G360Homepage"),
						rs.getBoolean("G360Admin"),
						rs.getBoolean("Active"),
						rs.getString("CreatedOn"),
						rs.getString("ModifiedOn"),
						rs.getString("CreatedBy"),
						rs.getString("ModifiedBy"),
						rs.getString("Department"),
						rs.getBoolean("FirstLoginRequired"),
						rs.getInt("PasswordExpiryDuraton"),
						rs.getString("PasswordExpiryDate"),
						rs.getString("LastLogin")
						);

				String userPagesQuery = "SELECT AllotedPage FROM [dbo].[UserPages] WHERE UserId = ?";
				//System.out.println(" rs.getString(\"UserId\") ==> "+ rs.getString("UserId"));
				List<String> allotedPages = OP360_UsermodulejdbcTemplate.queryForList(userPagesQuery, String.class,rs.getString("UserId"));
				//  System.out.println("allotedPages ==>  "+allotedPages);

				user.setAllotedPages(allotedPages);

				String userRollQuery = "SELECT UserRoll FROM [dbo].[UserRoll] WHERE UserId = ?";
				List<String> groups = OP360_UsermodulejdbcTemplate.queryForList(userRollQuery, String.class, rs.getString("UserId"));
				//   System.out.println("groups ==>  "+groups);
				user.setGroup(groups);

				user.setAllG360PageList(g360PageList);
				user.setAllWorkflowPageList(workFlowPageList);

				user.updateSourceSelectionPages(allotedPages);
				userList.add(user);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return userList;
	}

	/**
	 * Retrieves users who have access to a specific allotted page.
	 *
	 * @param allotedPage The allotted page for which users are to be retrieved.
	 * @return List of User containing information about users with access to the specified page.
	 */
	@Override
	public List<User> getUserFromAllotedPage(String allotedPage) {
		// TODO Auto-generated method stub
		List<User> getUserPages = new ArrayList<>();

		try {
			String sql=("SELECT distinct [UserId],[AllotedPage] FROM [dbo].[UserPages] where AllotedPage = ?");

			getUserPages.addAll(OP360_UsermodulejdbcTemplate.execute(sql, new PreparedStatementCallback<List<User>>() {
				@Override
				public List<User> doInPreparedStatement(PreparedStatement preparedStatement) throws SQLException {
					preparedStatement.setString(1, allotedPage);
					List<String> page;
					List<User> getUserPages = new ArrayList<>();

					ResultSet rs = preparedStatement.executeQuery();
					while (rs.next()) {
						page = new ArrayList<>();
						page.add(rs.getString("AllotedPage"));
						getUserPages.add(new User(rs.getString("UserId"),page));
					}
					return getUserPages;
				}
			}));


			return getUserPages;
		}
		catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}
	@Override
	public List<UserDepartment> getAllUserDept() {
		List<UserDepartment> data = new ArrayList<>();

		try {
			List<Map<String, Object>> rows = OP360_UsermodulejdbcTemplate.queryForList("SELECT [DeptId], [DeptName] FROM [dbo].[DeptCredentials]");
			for (Map<String, Object> row : rows) {
				String deptId = (String) row.get("DeptId");
				String deptName = (String) row.get("DeptName");
				data.add(new UserDepartment(deptId, deptName));
			}
		} catch (Exception e) {
			// Handle exceptions according to your requirement
			e.printStackTrace();
		}

		return data;
	}

	//	private final G360Alert gAlert=new G360Alert();
	//	DateFormat dfDt = new SimpleDateFormat("yyyy-MM-dd");
	//	DateFormat dfDtTm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	//	Connection con = null;
	//	PreparedStatement pstmt = null;
	//
	//	@Override
	//	public String getUserWiseHomepage(String userId) {
	//		String page = "";
	//		try {
	//			con = SQLServerDAOFactory.createUserConnection();
	//			pstmt = con.prepareStatement("Select [WorkflowHomepage] from [dbo].[UserCredential] Where UserId = ?");
	//			pstmt.setString(1, userId);
	//			ResultSet rs = pstmt.executeQuery();
	//			while (rs.next()) {
	//				page = rs.getString("WorkflowHomepage");
	//			}
	//			rs.close();
	//		} catch (Exception e) {
	//			e.printStackTrace();
	//		} finally {
	//			try {
	//
	//				con.close();
	//				pstmt.close();
	//			} catch (Exception e2) {
	//				e2.printStackTrace();
	//			}
	//		}
	//		return page;
	//	}
	//
	//	@Override
	//	public List<String> userWiseAllotedPages(String userId,String source) {
	//		List<String> pages = new LinkedList<String>();
	//		try {
	//			con = SQLServerDAOFactory.createUserConnection();
	//			pstmt = con.prepareStatement("Select AllotedPage from dbo.UserPages Where UserId = ?  AND [AllotedPage] IN (Select [Page] from dbo.Pages Where [Source] = ?)");
	//			pstmt.setString(1, userId);
	//			pstmt.setString(2, source);
	//			ResultSet rs = pstmt.executeQuery();
	//			while (rs.next()) {
	//				pages.add(rs.getString("AllotedPage")) ;
	//			}
	//			rs.close();
	//		} catch (Exception e) {
	//			e.printStackTrace();
	//		} finally {
	//			try {
	//
	//				con.close();
	//				pstmt.close();
	//			} catch (Exception e2) {
	//				e2.printStackTrace();
	//			}
	//		}
	//		return pages;
	//	}
	//
	//	@Override
	//	public boolean login(String userid, String password) {
	//		try {
	//			con = SQLServerDAOFactory.createUserConnection();
	//			pstmt = con.prepareStatement("Select * from dbo.UserCredential Where UserId = ? AND [Password] = ?");
	//			pstmt.setString(1, userid);
	//			pstmt.setString(2, password);
	//			ResultSet rs = pstmt.executeQuery();
	//			return rs.next();
	//
	//		} catch (Exception e) {
	//
	//			e.printStackTrace();
	//			return false;
	//		} finally {
	//			try {
	//
	//				con.close();
	//				pstmt.close();
	//			} catch (Exception e2) {
	//				e2.printStackTrace();
	//			}
	//		}
	//	}
	//
	//	@Override
	//	public void addorEditUser(User user, boolean newUser , User olderVersionOfUser , String changeList) {
	//		String delUserquery = "Delete from dbo.UserCredential Where UserId = ?;"
	//				+ "Delete from dbo.UserPages Where UserId = ?;"
	//				+ "Delete from dbo.[UserRoll] Where UserId = ?";
	//
	//
	//		String insertQuery  = newUser ? "INSERT INTO [dbo].[UserCredential] ([UserId] ,[UserName] ,[Password] , [PhoneNumber] ,[G360Homepage] ,[WorkflowHomepage] ,[G360Admin] ,[Active] , [CreatedOn] ,[ModifiedOn] ,[CreatedBy] ,[ModifiedBy] , [Department] ,[FirstLoginRequired] ,[PasswordExpiryDuraton] ,[PasswordExpiryDate] ,[LastLogin]) VALUES (? ,? ,? ,? ,? ,? ,? ,? , ? ,null, ? ,? , ?, ? , ? , DATEADD(DAY , ? ,CONVERT(DATE , GETDATE())) , ? )" : "INSERT INTO [dbo].[UserCredential] ([UserId] ,[UserName] ,[Password] , [PhoneNumber] ,[G360Homepage] ,[WorkflowHomepage] ,[G360Admin] ,[Active] , [CreatedOn] ,[ModifiedOn] ,[CreatedBy] ,[ModifiedBy] , [Department] ,[FirstLoginRequired] ,[PasswordExpiryDuraton] ,[PasswordExpiryDate] ,[LastLogin]) VALUES (? ,? ,? ,? ,? ,? ,? ,? , ? ,GETDATE(),? ,? , ?, ? , ? , DATEADD(DAY , ? ,CONVERT(DATE , GETDATE())) , ? )";;
	//		String allotPageQuery = "Insert Into dbo.UserPages ([UserId],[AllotedPage]) values (?,?)";
	//		String userRollQuery = "Insert Into dbo.[UserRoll] ([UserId],[UserRoll]) values (?,?)";
	//
	//		//log query
	//		String insertQueryToLog = "INSERT INTO [dbo].[data_logs] ([transaction_id] ,[timestamp] ,[id] ,[data_from] ,[modified_by] ,[data] ,[reason] ,[olddata] ,[newdata]) VALUES (? ,GETDATE() ,? ,? ,? ,? ,? , ? , ?);";
	//
	//		try {
	//			con = SQLServerDAOFactory.createUserConnection();
	//			con.setAutoCommit(false);
	//
	//			//adding data to log
	//			if(newUser) {
	//				String transactionId = 'T'+new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
	//
	//				Gson gson = new Gson();
	//
	//				String oldDataInJson = gson.toJson(new User()); 
	//				String newDataInJson = gson.toJson(user); 
	//
	//				pstmt = con.prepareStatement(insertQueryToLog);
	//				pstmt.setString(1, transactionId);
	//				pstmt.setString(2, user.getUserID());
	//				pstmt.setString(3, "UserCredentials");
	//				pstmt.setString(4, user.getCreatedBy());
	//				pstmt.setString(5, "New user created having id: " + user.getUserID());
	//				pstmt.setString(6, "");
	//				pstmt.setString(7, oldDataInJson);
	//				pstmt.setString(8, newDataInJson);
	//				pstmt.executeUpdate();
	//			}
	//			else {
	//				String transactionId = 'T'+new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
	//
	//				Gson gson = new Gson();
	//				String oldDataInJson = gson.toJson(olderVersionOfUser); 
	//				String newDataInJson = gson.toJson(user); 
	//
	//				pstmt = con.prepareStatement(insertQueryToLog);
	//				pstmt.setString(1, transactionId);
	//				pstmt.setString(2, user.getUserID());
	//				pstmt.setString(3, "UserCredentials");
	//				pstmt.setString(4, user.getModifiedBy());
	//				pstmt.setString(5, changeList);
	//				pstmt.setString(6, "");
	//				pstmt.setString(7, oldDataInJson);
	//				pstmt.setString(8, newDataInJson);
	//				pstmt.executeUpdate();
	//			}
	//
	//			//Deleteing user
	//			pstmt = con.prepareStatement(delUserquery);
	//			pstmt.setString(1, user.getUserID());
	//			pstmt.setString(2, user.getUserID());
	//			pstmt.setString(3, user.getUserID());
	//			pstmt.executeUpdate();
	//
	//			//Inserting userCredential
	//			pstmt = con.prepareStatement(insertQuery);
	//			pstmt.setString(1, user.getUserID());
	//			pstmt.setString(2, user.getUserName());
	//			pstmt.setString(3, user.getPassword());
	//			pstmt.setString(4, user.getPhone());
	//			pstmt.setString(5, user.getG360HomePage());
	//			pstmt.setString(6, user.getWorkFlowHomePage());
	//			pstmt.setBoolean(7, user.isG360Admin());	
	//			pstmt.setBoolean(8, user.isActive());	
	//			pstmt.setString(9, user.getCreatedOn());
	//			pstmt.setString(10, user.getCreatedBy());
	//			pstmt.setString(11, user.getModifiedBy());
	//			pstmt.setString(12, user.getDepartment());	
	//			pstmt.setBoolean(13, user.isFirstLoginRequired());	
	//			pstmt.setInt(14, user.getPasswordExpiryDuration());
	//			pstmt.setInt(15, user.getPasswordExpiryDuration());
	//			pstmt.setString(16, user.getLastLogIn());		
	//			pstmt.executeUpdate();
	//
	//			//Inserting alloted pages
	//			pstmt = con.prepareStatement(allotPageQuery);
	//			for(String page : user.getAllotedPages()) {
	//				pstmt.setString(1, user.getUserID());
	//				pstmt.setString(2, page);
	//				pstmt.addBatch();
	//			}			
	//			pstmt.executeBatch();
	//
	//			//Inserting groups
	//			pstmt = con.prepareStatement(userRollQuery);
	//			for(String group : user.getGroup()) {
	//				pstmt.setString(1, user.getUserID());
	//				pstmt.setString(2, group);
	//				pstmt.addBatch();
	//			}			
	//			pstmt.executeBatch();
	//
	//			con.commit();
	//			if(newUser)
	//				gAlert.ShowInfoAlert("User Added!!! Please note that default password is user_123");
	//			else
	//				gAlert.ShowInfoAlert("User Modified");
	//
	//		} catch (Exception e) {
	//
	//			e.printStackTrace();
	//		} finally {
	//			try {
	//				con.rollback();
	//				con.close();
	//				pstmt.close();
	//			} catch (Exception e2) {
	//				e2.printStackTrace();
	//			}
	//		}
	//
	//
	//	}
	//
	//	@Override
	//	public int getActiveUsers() {
	//		int users = 0;
	//		try {
	//			con = SQLServerDAOFactory.createUserConnection();
	//			pstmt = con.prepareStatement("Select COUNT(*) ActiveUsers from [dbo].[UserCredential] Where Active = 1");
	//			ResultSet rs = pstmt.executeQuery();
	//			while (rs.next()) {
	//				users = rs.getInt("ActiveUsers");
	//			}
	//			rs.close();
	//		} catch (Exception e) {
	//			e.printStackTrace();
	//		} finally {
	//			try {
	//
	//				con.close();
	//				pstmt.close();
	//			} catch (Exception e2) {
	//				e2.printStackTrace();
	//			}
	//		}
	//		return users;
	//	}
	//
	//	@Override
	//	public List<User> getAllUserInfo() {
	//		List<User> userList = new LinkedList<User>();
	//		try {
	//			con = SQLServerDAOFactory.createUserConnection();
	//
	//			PreparedStatement g360PagesPstm = con.prepareStatement("SELECT [Page] FROM [dbo].[Pages] where [Source] = ?");
	//			g360PagesPstm.setString(1, "G360");
	//			ResultSet g360Pages = g360PagesPstm.executeQuery();
	//			HashSet<String> g360PageList =  new HashSet<String>();
	//			while(g360Pages.next()) {
	//				g360PageList.add(g360Pages.getString("Page"));
	//			}
	//
	//
	//			PreparedStatement workFlowPagesPstm = con.prepareStatement("SELECT [Page] FROM [dbo].[Pages] where [Source] = ?");
	//			workFlowPagesPstm.setString(1,"Workflow");
	//			ResultSet wfPages = workFlowPagesPstm.executeQuery();
	//			HashSet<String> workFlowPAgeList = new HashSet<String>();
	//			while(wfPages.next()) {
	//				workFlowPAgeList.add(wfPages.getString("Page"));
	//			}
	//
	//
	//
	//			pstmt = con.prepareStatement("Select * from [dbo].[UserCredential]");
	//			ResultSet rs = pstmt.executeQuery();
	//			while (rs.next()) {
	//				User user = new User(rs.getString("UserId"), rs.getString("UserName"), rs.getString("Password"), rs.getString("PhoneNumber"), rs.getString("WorkflowHomepage") , rs.getString("G360Homepage") , rs.getBoolean("G360Admin"), rs.getBoolean("Active") , rs.getString("CreatedOn") ,  rs.getString("ModifiedOn"),  rs.getString("CreatedBy"),  rs.getString("ModifiedBy"), rs.getString("Department"), rs.getBoolean("FirstLoginRequired"), rs.getInt("PasswordExpiryDuraton"), rs.getString("PasswordExpiryDate"), rs.getString("LastLogin"));
	//
	//				PreparedStatement pagePstm = con.prepareStatement("Select * from [dbo].[UserPages] Where UserId = ?");
	//				pagePstm.setString(1, rs.getString("UserId"));
	//				ResultSet pRs = pagePstm.executeQuery();
	//				List<String> allotedPages = new LinkedList<String>();
	//				while(pRs.next()) {
	//					allotedPages.add(pRs.getString("AllotedPage"));
	//				}
	//				user.setAllotedPages(allotedPages);
	//
	//				PreparedStatement rollPstm = con.prepareStatement("Select * from [dbo].[UserRoll] Where UserId = ?");
	//				rollPstm.setString(1, rs.getString("UserId"));
	//				ResultSet rollRs = rollPstm.executeQuery();
	//				List<String> groups = new LinkedList<String>();
	//				while(rollRs.next()) {
	//					groups.add(rollRs.getString("UserRoll"));
	//				}
	//				user.setGroup(groups);
	//
	//
	//				user.setAllG360PageList(g360PageList);
	//				user.setAllWorkflowPageList(workFlowPAgeList);
	//
	//				user.updateSourceSelectionPages(allotedPages);
	//				userList.add(user);
	//				pagePstm.close();
	//				pRs.close();
	//			}
	//			rs.close();
	//		} catch (Exception e) {
	//			e.printStackTrace();
	//		} finally {
	//			try {
	//
	//				con.close();
	//				pstmt.close();
	//			} catch (Exception e2) {
	//				e2.printStackTrace();
	//			}
	//		}
	//		return userList;
	//	}
	//
	//	@Override
	//	public void changePassword(User user , String modifiedBy ,  User oldVersionOfUser) {
	//
	//
	//		String insertQueryToLog = "INSERT INTO [dbo].[data_logs] ([transaction_id] ,[timestamp] ,[id] ,[data_from] ,[modified_by] ,[data] ,[reason] ,[olddata] ,[newdata]) VALUES (? ,GETDATE() ,? ,? ,? ,? ,? , ? , ?);";
	//		// save data to the log table
	//		try {
	//			con = SQLServerDAOFactory.createUserConnection();
	//			con.setAutoCommit(false); 
	//			String transactionId = 'T'+new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
	//			Gson gson = new Gson();
	//
	//			String oldDataInJson = gson.toJson(oldVersionOfUser); 
	//			String newDataInJson = gson.toJson(user); 
	//
	//			pstmt = con.prepareStatement(insertQueryToLog);
	//			pstmt.setString(1, transactionId);
	//			pstmt.setString(2, user.getUserID());
	//			pstmt.setString(3, "UserCredentials");
	//			pstmt.setString(4, modifiedBy);
	//			pstmt.setString(5, "Password Changed");
	//			pstmt.setString(6, "");
	//			pstmt.setString(7, oldDataInJson);
	//			pstmt.setString(8, newDataInJson);
	//			pstmt.executeUpdate();
	//			con.commit();
	//		} catch (Exception e) {
	//			e.printStackTrace();
	//		} finally {
	//			try {
	//				con.rollback();
	//				con.close();
	//				pstmt.close();
	//			} catch (Exception e2) {
	//				e2.printStackTrace();
	//			}
	//		}
	//
	//
	//		try {
	//			con = SQLServerDAOFactory.createUserConnection();
	//			pstmt = con.prepareStatement("UPDATE [dbo].[UserCredential] SET Password = ?,FirstLoginRequired = 0 , PasswordExpiryDate = DATEADD(DAY , ? ,CONVERT(DATE , GETDATE())) Where UserId = ? ");
	//			pstmt.setString(1, user.getPassword());
	//			pstmt.setInt(2, user.getPasswordExpiryDuration());
	//			pstmt.setString(3, user.getUserID());
	//			pstmt.executeUpdate();
	//			gAlert.ShowInfoAlert("Password Updated for "+user.getUserID());
	//		} catch (Exception e) {
	//			gAlert.ShowErrorAlert(e.getMessage());
	//			e.printStackTrace();
	//		} finally {
	//			try {
	//
	//				con.close();
	//				pstmt.close();
	//			} catch (Exception e2) {
	//				e2.printStackTrace();
	//			}
	//		}
	//
	//	}
	//
	//	@Override
	//	public User getUserInfo(String userid , String userSrouce) {
	//		//System.out.println("LoginService.getUserInfo()"+userid);
	//		User user = new User();
	//		try {
	//			con = SQLServerDAOFactory.createUserConnection();
	//			pstmt = con.prepareStatement("Select * from [dbo].[UserCredential] Where [UserId] = ?");
	//			pstmt.setString(1, userid);
	//			ResultSet rs = pstmt.executeQuery();
	//			while (rs.next()) {
	//				user = new User(rs.getString("UserId"), rs.getString("UserName"), rs.getString("Password"), rs.getString("PhoneNumber"), rs.getString("WorkflowHomepage"), rs.getString("G360Homepage") , rs.getBoolean("G360Admin") , rs.getBoolean("Active"), rs.getString("CreatedOn") ,  rs.getString("ModifiedOn"),  rs.getString("CreatedBy"),  rs.getString("ModifiedBy") , rs.getString("Department"), rs.getBoolean("FirstLoginRequired"), rs.getInt("PasswordExpiryDuraton"), rs.getString("PasswordExpiryDate"), rs.getString("LastLogin"));
	//
	//				PreparedStatement pagePstm = con.prepareStatement("SELECT [AllotedPage] , [Source] FROM [dbo].[UserPages] as alp left join [dbo].[Pages] pgs on alp.[AllotedPage] = pgs.Page where [Source] = ? and UserId = ?");
	//				pagePstm.setString(1, userSrouce);
	//				pagePstm.setString(2, rs.getString("UserId"));
	//				ResultSet pRs = pagePstm.executeQuery();
	//				List<String> allotedPages = new LinkedList<String>();
	//				while(pRs.next()) {
	//					allotedPages.add(pRs.getString("AllotedPage"));
	//				}
	//				user.setAllotedPages(allotedPages);
	//
	//				if(userSrouce.equals("Workflow")) {
	//					user.setAllWorkflowPageList(new HashSet<String>(allotedPages));
	//				}
	//				else {
	//					user.setAllG360PageList(new HashSet<String>(allotedPages));
	//				}
	//				//				System.out.println(allotedPages);
	//				PreparedStatement rollPstm = con.prepareStatement("Select * from [dbo].[UserRoll] Where UserId = ?");
	//				rollPstm.setString(1, rs.getString("UserId"));
	//				ResultSet rollRs = rollPstm.executeQuery();
	//				List<String> rolls = new LinkedList<String>();
	//				while(rollRs.next()) {
	//					rolls.add(rollRs.getString("UserRoll"));
	//				}
	//				user.setGroup(rolls);
	//
	//
	//				pagePstm.close();
	//				pRs.close();
	//			}
	//			rs.close();
	//		} catch (Exception e) {
	//			e.printStackTrace();
	//		} finally {
	//			try {
	//
	//				con.close();
	//				pstmt.close();
	//			} catch (Exception e2) {
	//				e2.printStackTrace();
	//			}
	//		}
	//		return user;
	//	}
	//
	//	@Override
	//	public void addLeaveApplication(List<HolidayDB> holidayData) {
	//		String query = "INSERT INTO [dbo].[Holiday_Personal] ([userid],[holiday],[reason],[leaveapprover],[approverComment],[leavestatus]) values (?,?,?,?,?,?)";
	//		try {
	//			con = SQLServerDAOFactory.createUserConnection();
	//			con.setAutoCommit(false);
	//
	//			//Deleteing user
	//			pstmt = con.prepareStatement(query);
	//			for(HolidayDB data : holidayData) {
	//				pstmt.setString(1, data.getUserid());
	//				pstmt.setString(2, dfDt.format(data.getHoliday()));
	//				pstmt.setString(3, data.getReason());
	//				pstmt.setString(4, data.getApprover());
	//				pstmt.setString(5, data.getApproverComment());
	//				pstmt.setString(6, data.getStatus());
	//				pstmt.addBatch();
	//			}
	//
	//			pstmt.executeBatch();			
	//			con.commit();
	//			gAlert.ShowInfoAlert("Leave applied. Wait for the approval...");
	//
	//		} catch (Exception e) {
	//			gAlert.ShowErrorAlert(e.getMessage());
	//			e.printStackTrace();
	//		} finally {
	//			try {
	//				con.rollback();
	//				con.close();
	//				pstmt.close();
	//			} catch (Exception e2) {
	//				gAlert.ShowErrorAlert(e2.getMessage());
	//				e2.printStackTrace();
	//			}
	//		}
	//
	//	}
	//
	//	@Override
	//	public List<HolidayDB> getPendingLeaveApproval(String approver) {
	//		List<HolidayDB> data = new ArrayList<HolidayDB>();
	//		try {
	//			con = SQLServerDAOFactory.createUserConnection();
	//			pstmt = con.prepareStatement("Select * from [dbo].[Holiday_Personal] Where leaveapprover = ? AND leavestatus = 'Initiated' order by userid,holiday");
	//			pstmt.setString(1, approver);
	//			ResultSet rs = pstmt.executeQuery();
	//			while (rs.next()) {
	//				data.add(new HolidayDB(rs.getString("userid"),dfDt.parse(rs.getString("holiday")), rs.getString("reason"), rs.getString("leaveapprover"), rs.getString("approverComment"), rs.getString("leavestatus")));
	//			}
	//			rs.close();
	//		} catch (SQLException | ParseException e) {
	//			gAlert.ShowExceptionAlert(e.getMessage());
	//			e.printStackTrace();
	//		} finally {
	//			try {
	//
	//				con.close();
	//				pstmt.close();
	//			} catch (Exception e2) {
	//				e2.printStackTrace();
	//			}
	//		}
	//		return data;
	//	}
	//
	//	@Override
	//	public void respondToLeaveApplications(String userid , String fromdate, String todate ,String approverComment,String status) {
	//		String query = "UPDATE  [dbo].[Holiday_Personal] SET leavestatus = ? , approverComment = ? Where userid = ? AND holiday between ? AND ? AND leavestatus = 'Initiated'";
	//		try {
	//			con = SQLServerDAOFactory.createUserConnection();
	//			//con.setAutoCommit(false);	
	//			//			System.out.println(fromdate+"---"+todate+"---"+userid);
	//			pstmt = con.prepareStatement(query);
	//			//for(HolidayDB data : holidayData) {
	//			pstmt.setString(1, status);
	//			pstmt.setString(2, approverComment);				
	//			pstmt.setString(3, userid);
	//			pstmt.setString(4, fromdate);
	//			pstmt.setString(5, todate);
	//			//pstmt.addBatch();
	//			//}
	//
	//			pstmt.executeUpdate();		
	//			//con.commit();
	//			gAlert.ShowInfoAlert("Leave "+status);
	//
	//		} catch (Exception e) {
	//			gAlert.ShowErrorAlert(e.getMessage());
	//			e.printStackTrace();
	//		} finally {
	//			try {
	//
	//				con.close();
	//				pstmt.close();
	//			} catch (Exception e2) {
	//				gAlert.ShowErrorAlert(e2.getMessage());
	//				e2.printStackTrace();
	//			}
	//		}
	//
	//	}
	//
	//	@Override
	//	public List<HolidayDB> getAllHolidays() {
	//		List<HolidayDB> data = new ArrayList<HolidayDB>();
	//		try {
	//			con = SQLServerDAOFactory.createUserConnection();
	//			pstmt = con.prepareStatement("Select userid,holiday,reason from dbo.Holiday_Personal Where leavestatus = 'Approved' " + 
	//					" UNION ALL " + 
	//					"Select userid = 'Official',Holiday,Reason from dbo.Holiday_Official");
	//			ResultSet rs = pstmt.executeQuery();
	//			while (rs.next()) {
	//				data.add(new HolidayDB(rs.getString("userid"),dfDt.parse(rs.getString("holiday")), rs.getString("reason")));
	//			}
	//			rs.close();
	//		} catch (SQLException | ParseException e) {
	//			gAlert.ShowExceptionAlert(e.getMessage());
	//			e.printStackTrace();
	//		} finally {
	//			try {
	//
	//				con.close();
	//				pstmt.close();
	//			} catch (Exception e2) {
	//				e2.printStackTrace();
	//			}
	//		}
	//		return data;
	//	}
	//
	//	@Override
	//	public void updateProfile(User user , String modifiedBy , String modifiedLog , User oldVersionOfUser) {
	//
	//
	//		String insertQueryToLog = "INSERT INTO [dbo].[data_logs] ([transaction_id] ,[timestamp] ,[id] ,[data_from] ,[modified_by] ,[data] ,[reason] ,[olddata] ,[newdata]) VALUES (? ,GETDATE() ,? ,? ,? ,? ,? , ? , ?);";
	//		// save data to the log table
	//		try {
	//			con = SQLServerDAOFactory.createUserConnection();
	//			con.setAutoCommit(false); 
	//			String transactionId = 'T'+new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
	//
	//			Gson gson = new Gson();
	//
	//			String oldDataInJson = gson.toJson(oldVersionOfUser); 
	//			String newDataInJson = gson.toJson(user); 
	//
	//			pstmt = con.prepareStatement(insertQueryToLog);
	//			pstmt.setString(1, transactionId);
	//			pstmt.setString(2, user.getUserID());
	//			pstmt.setString(3, "UserCredentials");
	//			pstmt.setString(4, user.getUserID());
	//			pstmt.setString(5, modifiedLog);
	//			pstmt.setString(6, "");
	//			pstmt.setString(7, oldDataInJson);
	//			pstmt.setString(8, newDataInJson);
	//			pstmt.executeUpdate();
	//			con.commit();
	//		} catch (Exception e) {
	//			e.printStackTrace();
	//		} finally {
	//			try {
	//				con.rollback();
	//				con.close();
	//				pstmt.close();
	//			} catch (Exception e2) {
	//				e2.printStackTrace();
	//			}
	//		}
	//
	//		try {
	//			con = SQLServerDAOFactory.createUserConnection();
	//			pstmt = con.prepareStatement("EXEC [dbo].[updateUserProfileInfo] ?, ? ,? , ?, ? , ? ,?, ? , ? , ? ,?, ?;");
	//			pstmt.setString(1, user.getUserID());
	//			pstmt.setString(2, user.getUserName());
	//			pstmt.setString(3, user.getPassword());
	//			pstmt.setString(4, user.getPhone());
	//			pstmt.setString(5, user.getG360HomePage());
	//			pstmt.setString(6, user.getWorkFlowHomePage());
	//			pstmt.setBoolean(7, user.isG360Admin());
	//			pstmt.setBoolean(8, user.isActive());
	//			pstmt.setString(9, user.getCreatedOn());
	//			pstmt.setString(10 , user.getCreatedBy());
	//			pstmt.setString(11 , modifiedBy);
	//			pstmt.setString(12 , user.getDepartment());
	//			pstmt.executeUpdate();
	//			gAlert.ShowInfoAlert("Profile Updated for "+user.getUserID());
	//		} catch (Exception e) {
	//			gAlert.ShowErrorAlert(e.getMessage());
	//			e.printStackTrace();
	//		} finally {
	//			try {
	//
	//				con.close();
	//				pstmt.close();
	//			} catch (Exception e2) {
	//				e2.printStackTrace();
	//			}
	//		}
	//
	//	}
	//
	//	@Override
	//	public List<HolidayDB> getMyLeave(String userId) {
	//
	//		List<HolidayDB> data = new ArrayList<HolidayDB>();
	//
	//		try {
	//			con = SQLServerDAOFactory.createUserConnection();
	//			pstmt = con.prepareStatement("SELECT [userid] ,[holiday] ,[reason] ,[leaveapprover] ,[approverComment] ,[leavestatus] FROM [dbo].[Holiday_Personal] where userid = ?");
	//			pstmt.setString(1, userId);
	//			ResultSet rs = pstmt.executeQuery();
	//			while (rs.next()) {
	//
	//				data.add(new HolidayDB(rs.getString("userid"),dfDt.parse(rs.getString("holiday")), rs.getString("reason"), rs.getString("leaveapprover"), rs.getString("approverComment"), rs.getString("leavestatus")));
	//
	//			}
	//			rs.close();
	//
	//		} catch (Exception e) {
	//			gAlert.ShowErrorAlert(e.getMessage());
	//			e.printStackTrace();
	//		} finally {
	//			try {
	//
	//				con.close();
	//				pstmt.close();
	//			} catch (Exception e2) {
	//				e2.printStackTrace();
	//			}
	//		}
	//		return data;
	//	}
	//
	//	@Override
	//	public List<UserGroup> getAllUserGroup() {
	//
	//
	//		List<UserGroup> data = new ArrayList<UserGroup>();
	//
	//		try {
	//			con = SQLServerDAOFactory.createUserConnection();
	//			pstmt = con.prepareStatement("SELECT [GroupId] ,[GroupName] ,[PhoneNumber] ,[HomePage] ,[Active] FROM [dbo].[GroupCredentials]");
	//			ResultSet rs = pstmt.executeQuery();
	//			while (rs.next()) {
	//				data.add(new UserGroup(rs.getString("GroupId") , rs.getString("GroupName"), BigInteger.valueOf(rs.getLong("PhoneNumber")), rs.getString("HomePage"), (rs.getByte("Active") == 0 ? false : true)));
	//				//System.out.println(new UserGroup(rs.getString("GroupId") , rs.getString("GroupName"), rs.getString("PhoneNumber"), rs.getString("HomePage"), (rs.getByte("approverComment") == 0 ? false : true)));
	//			}
	//			rs.close();
	//
	//		} catch (Exception e) {
	//			gAlert.ShowErrorAlert(e.getMessage());
	//			e.printStackTrace();
	//		} finally {
	//			try {
	//
	//				con.close();
	//				pstmt.close();
	//			} catch (Exception e2) {
	//				e2.printStackTrace();
	//			}
	//		}
	//		return data;
	//	}
	//
	//	@Override
	//	public void createOrUpdate_OneUserGroup(UserGroup userGroup , boolean updateCall) {
	//
	//
	//		String query = "Delete from [dbo].[GroupCredentials] where [GroupId] = ? ; insert into [dbo].[GroupCredentials] ([GroupId] ,[GroupName] ,[PhoneNumber] ,[HomePage] ,[Active]) values (? , ? , ? , ? , ?)";
	//		try {
	//			con = SQLServerDAOFactory.createUserConnection();
	//			con.setAutoCommit(false);
	//
	//			//Inserting Data
	//			pstmt = con.prepareStatement(query);
	//			pstmt.setString(1, userGroup.getGroupID());
	//			pstmt.setString(2, userGroup.getGroupID());
	//			pstmt.setString(3, userGroup.getGroupName());
	//			pstmt.setLong(4 , userGroup.getPhone().longValue());
	//			pstmt.setString(5 , userGroup.getHomePage());
	//			pstmt.setInt(6 , (userGroup.isActive() ? 1 : 0));
	//
	//			pstmt.executeUpdate();			
	//			con.commit();
	//
	//			if(updateCall) {
	//				gAlert.ShowInfoAlert(userGroup.getGroupID() + "'s Details Updated...");
	//			}
	//			else {
	//				gAlert.ShowInfoAlert("New Group Inserted...");
	//			}
	//
	//		} catch (Exception e) {
	//			gAlert.ShowErrorAlert(e.getMessage());
	//			e.printStackTrace();
	//		} finally {
	//			try {
	//				con.rollback();
	//				con.close();
	//				pstmt.close();
	//			} catch (Exception e2) {
	//				gAlert.ShowErrorAlert(e2.getMessage());
	//				e2.printStackTrace();
	//			}
	//		}
	//
	//
	//	}
	//
	//	@Override
	//	public List<String> getPagesForSource(String sourceName) {
	//
	//
	//		List<String> data = new ArrayList<String>();
	//
	//		try {
	//			con = SQLServerDAOFactory.createUserConnection();
	//			pstmt = con.prepareStatement("SELECT [Page] FROM [dbo].[Pages] where [Source] = ?");
	//			pstmt.setString(1 , sourceName);
	//			ResultSet rs = pstmt.executeQuery();
	//
	//			while (rs.next()) {
	//				data.add(rs.getString("Page"));
	//			}
	//			rs.close();
	//
	//		} catch (Exception e) {
	//			gAlert.ShowErrorAlert(e.getMessage());
	//			e.printStackTrace();
	//		} finally {
	//			try {
	//
	//				con.close();
	//				pstmt.close();
	//			} catch (Exception e2) {
	//				e2.printStackTrace();
	//			}
	//		}
	//		return data;
	//	}
	//
	//	@Override
	//	public void createOrUpdate_Department(UserDepartment userDept, boolean updateCall) {
	//
	//
	//		String query = "Delete from [dbo].[DeptCredentials] where [DeptId] = ? ; insert into [dbo].[DeptCredentials] ([DeptId],[DeptName]) values (? , ? )";
	//		try {
	//			con = SQLServerDAOFactory.createUserConnection();
	//			con.setAutoCommit(false);
	//
	//			//Inserting Data
	//			pstmt = con.prepareStatement(query);
	//			pstmt.setString(1, userDept.getDeptID());
	//			pstmt.setString(2, userDept.getDeptName());
	//			pstmt.setString(3, userDept.getDeptName());
	//
	//			pstmt.executeUpdate();			
	//			con.commit();
	//
	//			if(updateCall) {
	//				gAlert.ShowInfoAlert(userDept.getDeptID() + "'s Details Updated...");
	//			}
	//			else {
	//				gAlert.ShowInfoAlert("New Department Inserted...");
	//			}
	//
	//		} catch (Exception e) {
	//			gAlert.ShowErrorAlert(e.getMessage());
	//			e.printStackTrace();
	//		} finally {
	//			try {
	//				con.rollback();
	//				con.close();
	//				pstmt.close();
	//			} catch (Exception e2) {
	//				gAlert.ShowErrorAlert(e2.getMessage());
	//				e2.printStackTrace();
	//			}
	//		}
	//
	//	}
	//

	//	@Override
	//	public List<UserDepartment> getAllUserDept() {
	//
	//		List<UserDepartment> data = new ArrayList<UserDepartment>();
	//
	//		try {
	//			con = SQLServerDAOFactory.createUserConnection();
	//			pstmt = con.prepareStatement("SELECT [DeptId] ,[DeptName] FROM [dbo].[DeptCredentials]");
	//			ResultSet rs = pstmt.executeQuery();
	//			while (rs.next()) {
	//				data.add(new UserDepartment(rs.getString("DeptId") , rs.getString("DeptName")));
	//
	//			}
	//			rs.close();
	//
	//		} catch (Exception e) {
	//			gAlert.ShowErrorAlert(e.getMessage());
	//			e.printStackTrace();
	//		} finally {
	//			try {
	//
	//				con.close();
	//				pstmt.close();
	//			} catch (Exception e2) {
	//				e2.printStackTrace();
	//			}
	//		}
	//		return data;
	//	}
	//
	//	@Override
	//	public void updateLastLogin(String userid) {
	//
	//		try {
	//			con = SQLServerDAOFactory.createUserConnection();
	//			pstmt = con.prepareStatement("UPDATE [dbo].[UserCredential] SET [LastLogin] = GETDATE() WHERE [UserId] = ?;");
	//			pstmt.setString(1, userid);
	//			pstmt.executeUpdate();
	//		} catch (Exception e) {
	//			gAlert.ShowErrorAlert(e.getMessage());
	//			e.printStackTrace();
	//		} finally {
	//			try {
	//				con.close();
	//				pstmt.close();
	//			} catch (Exception e2) {
	//				e2.printStackTrace();
	//			}
	//		}
	//
	//
	//	}
	//
	//	@Override
	//	public void updateWrongPasswords(String userId) {
	//
	//		//		System.out.println(userId + ": userId called");
	//		try {
	//			con = SQLServerDAOFactory.createUserConnection();
	//			
	//			con.setAutoCommit(false);
	//			
	//			pstmt = con.prepareStatement("INSERT INTO [WrongPasswordTracker] ([UserId],[timestamp]) VALUES (? , GETDATE());");
	//			pstmt.setString(1, userId);
	//			pstmt.executeUpdate();
	//			
	//			pstmt = con.prepareStatement("INSERT INTO [dbo].[wrongpassword_log] ([UserId] ,[timestamp] ,[ipaddress]) VALUES (? , GETDATE() ,?);");
	//			
	//			
	//			pstmt.setString(1, userId);
	//			pstmt.setString(2, "");
	//			pstmt.executeUpdate();
	//			
	//			con.commit();
	//		} catch (Exception e) {
	//			gAlert.ShowErrorAlert(e.getMessage());
	//			e.printStackTrace();
	//		} finally {
	//			try {
	//				con.close();
	//				pstmt.close();
	//			} catch (Exception e2) {
	//				e2.printStackTrace();
	//			}
	//		}
	//
	//
	//	}
	//
	//	@Override
	//	public int wrongPasswrodIn24Hours(String userId , int hours) {
	//		// TODO Auto-generated method stub
	//
	//
	//		int totalWrongPasswordInLast24Hours = 0;
	//
	//		try {
	//			con = SQLServerDAOFactory.createUserConnection();
	//			pstmt = con.prepareStatement("SELECT CASE WHEN DATEDIFF(HOUR , MIN(timestamp) , MAX(timestamp)) <= ? THEN COUNT(*) ELSE 1 END AS countVal FROM [dbo].[WrongPasswordTracker] WHERE UserId = ?");
	//			pstmt.setInt(1, hours);
	//			pstmt.setString(2, userId);
	//			ResultSet rs = pstmt.executeQuery();
	//			while (rs.next()) {
	//				totalWrongPasswordInLast24Hours = Integer.parseInt(rs.getString("countVal"));
	//			}
	//			rs.close();
	//
	//		} catch (Exception e) {
	//			gAlert.ShowErrorAlert(e.getMessage());
	//			e.printStackTrace();
	//		} finally {
	//			try {
	//
	//				con.close();
	//				pstmt.close();
	//			} catch (Exception e2) {
	//				e2.printStackTrace();
	//			}
	//		}
	//
	//
	//		//		System.out.println(totalWrongPasswordInLast24Hours);
	//		return totalWrongPasswordInLast24Hours;
	//
	//
	//	}
	//
	//	@Override
	//	public void makeUserInActive(String userId) {
	//
	//		try {
	//			con = SQLServerDAOFactory.createUserConnection();
	//			con.setAutoCommit(false); 
	//
	//			pstmt = con.prepareStatement("UPDATE [dbo].[UserCredential] SET [Active] = 0 WHERE [UserId] = ?;");
	//			pstmt.setString(1, userId);
	//			pstmt.executeUpdate();
	//
	//			String insertQueryToLog = "INSERT INTO [dbo].[data_logs] ([transaction_id] ,[timestamp] ,[id] ,[data_from] ,[modified_by] ,[data] ,[reason] ,[olddata] ,[newdata]) VALUES (? ,GETDATE() ,? ,? ,? ,? ,? , ? , ?);";
	//			pstmt = con.prepareStatement(insertQueryToLog);
	//
	//			String transactionId = 'T'+new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
	//
	//			pstmt.setString(1, transactionId);
	//			pstmt.setString(2, userId);
	//			pstmt.setString(3, "UserCredentials");
	//			pstmt.setString(4, "");
	//			pstmt.setString(5, "User Account has been inactivated due to multiple login attempts with wrong credentials");
	//			pstmt.setString(6, "User has been blocked");
	//			pstmt.setString(7, "");
	//			pstmt.setString(8, "");
	//
	//
	//			pstmt.executeUpdate();
	//
	//			con.commit();
	//
	//			gAlert.ShowInfoAlert("User Account has been inactivated due to multiple login attempts with wrong credentials , Contact Admin!!! ");
	//		} catch (Exception e) {
	//			gAlert.ShowErrorAlert(e.getMessage());
	//			e.printStackTrace();
	//		} finally {
	//			try {
	//				con.close();
	//				pstmt.close();
	//			} catch (Exception e2) {
	//				e2.printStackTrace();
	//			}
	//		}
	//
	//
	//	}
	//
	//	@Override
	//	public boolean isUserActive(String userId) {
	//
	//		try {
	//			con = SQLServerDAOFactory.createUserConnection();
	//			pstmt = con.prepareStatement("Select * from dbo.UserCredential Where UserId = ? and Active = 1");
	//			pstmt.setString(1, userId);
	//			ResultSet rs = pstmt.executeQuery();
	//			return rs.next();
	//
	//		} catch (Exception e) {
	//
	//			e.printStackTrace();
	//			return false;
	//		} finally {
	//			try {
	//
	//				con.close();
	//				pstmt.close();
	//			} catch (Exception e2) {
	//				e2.printStackTrace();
	//			}
	//		}
	//	}
	//
	//	@Override
	//	public void deleteAllWrongPasswords(String userId, boolean leftLast) {
	//
	//		String query = "delete FROM [WrongPasswordTracker] Where UserId = ? ";
	//		if(leftLast)
	//			query += " and timestamp <> (select MAX(timestamp) from [WrongPasswordTracker])";
	//		try {
	//			con = SQLServerDAOFactory.createUserConnection();
	//			pstmt = con.prepareStatement(query);
	//			pstmt.setString(1, userId);
	//			pstmt.executeUpdate();
	//		} catch (Exception e) {
	//			gAlert.ShowErrorAlert(e.getMessage());
	//			e.printStackTrace();
	//		} finally {
	//			try {
	//				con.close();
	//				pstmt.close();
	//			} catch (Exception e2) {
	//				e2.printStackTrace();
	//			}
	//		}
	//
	//	}
	//
	//	@Override
	//	public List<UserLog> getUserlogForOneUser(String userId) {
	//
	//
	//		List<UserLog> data = new ArrayList<UserLog>();
	//
	//		try {
	//			con = SQLServerDAOFactory.createUserConnection();
	//			pstmt = con.prepareStatement("SELECT [transaction_id] ,[timestamp] ,[id] ,[data_from] ,[modified_by] ,[data] ,[reason]  , [olddata]  , [newdata] FROM [dbo].[data_logs] where [id] = ? order by [timestamp] desc");
	//			pstmt.setString(1, userId);
	//			ResultSet rs = pstmt.executeQuery();
	//			while (rs.next()) {
	//				UserLog tempUser = new UserLog(rs.getString("transaction_id") , rs.getString("timestamp"),rs.getString("id") , rs.getString("data_from"),rs.getString("modified_by") , rs.getString("data"), rs.getString("reason"));
	//
	//				Gson gson = new Gson();
	//
	//				User oldUserDataFromJson = gson.fromJson(rs.getString("olddata") , User.class); 
	//				tempUser.setUserDetails(oldUserDataFromJson);
	//				User newUserDataFromJson = gson.fromJson(rs.getString("newdata") ,User.class); 
	//				tempUser.setNewUserDetails(newUserDataFromJson);
	//
	//				data.add(tempUser);
	//			}
	//			rs.close();
	//
	//		} catch (Exception e) {
	//			gAlert.ShowErrorAlert(e.getMessage());
	//			e.printStackTrace();
	//		} finally {
	//			try {
	//
	//				con.close();
	//				pstmt.close();
	//			} catch (Exception e2) {
	//				e2.printStackTrace();
	//			}
	//		}
	//		return data;
	//	}
	//	
	//	@Override
	//	public List<User> getUserFromAllotedPage(String allotedPage) {
	//		List<User> getUserPages = new ArrayList<>();
	//		List<String> page;
	//		try {
	//			con = SQLServerDAOFactory.createUserConnection();
	//			pstmt = con.prepareStatement("SELECT distinct [UserId],[AllotedPage] FROM [dbo].[UserPages] where AllotedPage = ?");
	//			pstmt.setString(1, allotedPage);
	//			ResultSet rs = pstmt.executeQuery();
	//			while (rs.next()) {
	//				page = new ArrayList<>();
	//				page.add(rs.getString("AllotedPage"));
	//				getUserPages.add(new User(rs.getString("UserId"),page));
	//			}
	//			rs.close();
	//		} catch (Exception e) {
	//			e.printStackTrace();
	//		} finally {
	//			try {
	//				con.close();
	//				pstmt.close();
	//			} catch (Exception e2) {
	//				e2.printStackTrace();
	//			}
	//		}
	//		return getUserPages;
	//	}

}
