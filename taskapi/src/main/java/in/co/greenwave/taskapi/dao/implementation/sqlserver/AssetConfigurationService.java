package in.co.greenwave.taskapi.dao.implementation.sqlserver;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.stereotype.Repository;

import in.co.greenwave.taskapi.dao.AssetConfigurationDAO;
import in.co.greenwave.taskapi.model.User;

@Repository 
public class AssetConfigurationService implements AssetConfigurationDAO {
	@Autowired
	@Qualifier("jdbcTemplate1")
	public JdbcTemplate jdbcTemplate;
	@Autowired
	@Qualifier("jdbcTemplate2")
	public JdbcTemplate jdbcTemplate2;
	@Override
	public List<User> getUserDeptOrGroupwsie(String selectedInput, String deptOrGroupSelected) {
		// TODO Auto-generated method stub
		String query = "";
		if(deptOrGroupSelected.equals("department")) {
			//query wrt dept
			query = "SELECT [UserId] ,[UserName] FROM [DeptCredentials] as dct left join [UserCredential] as ucd on dct.[DeptId] = ucd.[Department] where dct.[DeptId] = ? AND ucd.[Active] = 1";

		}
		else {
			//query wrt group
			//in database department is named as roll
			query = "SELECT ugt.[UserId],[UserName] FROM [UserRoll] as ugt left join [UserCredential] as ucd on ugt.[UserId] = ucd.[UserId] where ugt.[UserRoll] = ? and ucd.[Active] = 1";
		}

		List<User> data = new LinkedList<User>();
		try {
			data.addAll(jdbcTemplate2.execute(query, new PreparedStatementCallback<List<User>>() {
	            @Override
	            public List<User> doInPreparedStatement(PreparedStatement preparedStatement) throws SQLException {
	                List<User> data=new LinkedList<User>();
	            	preparedStatement.setString(1, selectedInput);
	                ResultSet resultSet = preparedStatement.executeQuery();
	                while(resultSet.next()) {
	    				data.add(new User(resultSet.getString("UserId")  , resultSet.getString("UserName")));
	    			}
	                return data;
	            }
	        }));

			

			
		}catch (Exception e) {
			e.printStackTrace();
		}


		return data;
	}
//	private final G360Alert gAlert=new G360Alert();
//	@Override
//	public boolean checkIfGroupAlreadyExists(String groupName) {
//		boolean exists = false;
//		try {
//			Connection con = SQLServerDAOFactory.createConnection();
//			PreparedStatement pstmt = con.prepareStatement("Select [GroupName] from [dbo].[AssetGroupInfo] Where [GroupName] = ?");
//			pstmt.setString(1, groupName);
//			ResultSet rs = pstmt.executeQuery();
//			if (rs.next()) {
//				exists = true;
//			}
//			rs.close();
//			con.close();
//			pstmt.close();
//		} catch (Exception e) {
//			gAlert.ShowErrorAlert(e.getMessage());
//			e.printStackTrace();
//		} 
//		return exists;
//	}
//
//	@Override
//	public void saveAssetTree(AssetGroup data,String userid) {
//
//		try {
//			Connection con = SQLServerDAOFactory.createConnection();
//			con.setAutoCommit(false);
//			/*Deleting old Asset Tree 
//			 * Only deleting the group info as it will cascade delete the AssetInfo*/
//			PreparedStatement delGrInfo = con.prepareStatement("Delete from [dbo].[AssetGroupInfo] Where [GroupId] = ?");
//			delGrInfo.setString(1, data.getGroupId());
//			int col = delGrInfo.executeUpdate();
//			if(col == 0)
//				System.out.println("Creating new Asset Tree");
//			else
//				System.out.println("Modifing the group "+data.getGroupName());
//			delGrInfo.close();
//
//			/*Inserting group info*/
//			PreparedStatement insGrInfo = con.prepareStatement("INSERT INTO [dbo].[AssetGroupInfo] ([GroupId],[GroupName],[CreatedBy],[CreatedOn]) values (?,?,?,GETDATE())");
//			insGrInfo.setString(1, data.getGroupId());
//			insGrInfo.setString(2, data.getGroupName());
//			insGrInfo.setString(3, userid);
//			col = insGrInfo.executeUpdate();
//			//			System.out.println("Group Added");
//
//			/*Inserting Asset Tree*/
//			PreparedStatement insAssetInfo = con.prepareStatement("INSERT INTO [dbo].[AssetInfo] ([GroupId],[ParentAssetId],[AssetId],[AssetName], [Icon],[Category],[Image],[NodeOrder] ,[users] ,[logbooks] ,[location] ,[manufacturer] ,[model] ,[serialNo] ,[description] , [uom] , [capacity] ) values (?,?,?,?,?,?,?,?,?,?,?,? , ? , ? , ? , ? , ?)");
//
//			for(AssetModel parent : data.getAssetInfo().keySet()) {
//				int nodeorder = 1;
//				for(AssetModel child : data.getAssetInfo().get(parent)) {
//
//
//					StringBuilder usersString = new StringBuilder("");
//					StringBuilder logbooksString = new StringBuilder("");
//					/* looping through userslist and logbookslist and to make it comma separated String */
//					
//					if(child.getUsersList() != null)
//					for(String singleUser : child.getUsersList()) {
//						usersString.append("," + singleUser );
//					}
//					
//					if(child.getLogbooksList() != null) {
//						for (int i = 0; i < child.getLogbooksList().size(); i++) {
//							System.out.println(child.getLogbooksList().get(i).getClass());
//							int formid = Integer.parseInt(child.getLogbooksList().get(i).toString());
//							logbooksString.append("," + formid);
//
//						}
//					}
//				/*
//				 * for(String singleLogbooks : child.getLogbooksList()) {
//				 * logbooksString.append("," + singleLogbooks.toString()); }
//				 */
//
//					if(logbooksString.toString().contains(",")) {
//						logbooksString.deleteCharAt(0);
//					}
//					if(usersString.toString().contains(",")) {
//						usersString.deleteCharAt(0);
//					}
//
//
//					insAssetInfo.setString(1, data.getGroupId());
//					insAssetInfo.setString(2, parent.getNodeName());
//					insAssetInfo.setString(3, child.getNodeName());
//					insAssetInfo.setString(4, child.getAliasName());
//					insAssetInfo.setString(5, child.getIcon());
//					insAssetInfo.setString(6, child.getCategory());
//					insAssetInfo.setBinaryStream(7, null); //Image will be done later
//					insAssetInfo.setInt(8, nodeorder);
//					insAssetInfo.setString(9, usersString.toString());
//					insAssetInfo.setString(10, logbooksString.toString());
//
//					insAssetInfo.setString(11, child.getLocation());
//					insAssetInfo.setString(12, child.getManufacturer());
//					insAssetInfo.setString(13, child.getModel());
//					insAssetInfo.setString(14, child.getSerialNo());
//					insAssetInfo.setString(15, child.getDescription());
//					insAssetInfo.setString(16, child.getUom());
//					insAssetInfo.setInt(17, child.getCapacity());
//
//					insAssetInfo.addBatch();
//					nodeorder++;
//				}
//			}
//
//			int[] colArr = insAssetInfo.executeBatch();
//			if(colArr.length>0)
//				gAlert.ShowInfoAlert("Asset Group Saved");
//			insGrInfo.close();
//
//			con.commit();
//
//			con.close();
//		} catch (Exception e) {
//			gAlert.ShowErrorAlert(e.getMessage());
//			e.printStackTrace();
//
//		} 
//
//	}
//
//	@Override
//	public void deleteTreeMap(String assetId) {
//		try {
//			Connection con = SQLServerDAOFactory.createConnection();
//			con.setAutoCommit(false);
//			/*Deleting old Asset Tree 
//			 * Only deleting the group info */
//			PreparedStatement delGrInfo = con.prepareStatement("Delete from [dbo].[AssetGroupInfo] Where [GroupName] = ?");
//
//			delGrInfo.setString(1, assetId);
//			int col = delGrInfo.executeUpdate();
//
//			if(col == 0)
//				System.out.println("No Group deleted");
//			else
//				gAlert.ShowInfoAlert("Asset Group Deleted");
//			con.commit();
//			delGrInfo.close();
//			con.close();
//		} catch (Exception e) {
//			gAlert.ShowErrorAlert(e.getMessage());
//			e.printStackTrace();
//		} 
//	}
//
//
//
//	@Override
//	public AssetGroup getAssetTreefromGroupName(String groupName) {
//		AssetGroup assetdata = new AssetGroup();
//		try {
//			Connection con = SQLServerDAOFactory.createConnection();
//			PreparedStatement pstmt = con.prepareStatement("Declare @groupname as varchar(max) = ? " + 
//					"SELECT  a.[GroupId],[GroupName],[ParentAssetId],[AssetId],[AssetName],[Icon],[Category],[Image],a.[users] , a.[logbooks], a.[location] ,a.[manufacturer] ,a.[model] ,a.[serialNo] ,a.[description] ,a.[uom] ,a.[capacity]     " + 
//					"					FROM [dbo].[AssetInfo] a, dbo.AssetGroupInfo gr  " + 
//					"					Where a.GroupId = gr.GroupId AND gr.GroupName = @groupname order by [NodeOrder]");
//			pstmt.setString(1, groupName);
//			ResultSet rs = pstmt.executeQuery();
//			Map<AssetModel,List<AssetModel>> treeMap = new LinkedHashMap<AssetModel,List<AssetModel>>();
//			while (rs.next()) {
//				assetdata.setGroupId(rs.getString("GroupId"));
//				assetdata.setGroupName(rs.getString("GroupName"));
//				String currentParentAssetId = rs.getString("ParentAssetId");
//				AssetModel parent = getAssetInfofromAssetId(currentParentAssetId,assetdata.getGroupId());
//				AssetModel child = new AssetModel(rs.getString("AssetId"), rs.getString("AssetName"), false, rs.getString("Icon"), rs.getString("Category"), rs.getString("Image"));
//
//
//				String logbooksListString = rs.getString("logbooks");
//				String usersListString = rs.getString("users");
//
//
//				if(logbooksListString != null) {
//					String[] logbooks = logbooksListString.split("\\s*,\\s*");
//					List<Integer> logbooksList = new ArrayList<Integer>();
//					for(String book : logbooks) {
//						if(!book.trim().equalsIgnoreCase(""))
//							logbooksList.add(Integer.parseInt(book));
//					}
//
//					child.setLogbooksList(logbooksList);
//				}
//				if(usersListString != null) {
//					String[] users = usersListString.split("\\s*,\\s*");
//
//					List<String> usersList = new ArrayList<String>();
//					for(String u : users) {
//						usersList.add(u);
//					}
//					child.setUsersList(usersList);
//
//				}
//
//				child.setLocation(rs.getString("location"));
//				child.setManufacturer(rs.getString("manufacturer"));
//				child.setModel(rs.getString("model"));
//				child.setSerialNo(rs.getString("serialNo"));
//				child.setDescription(rs.getString("description"));
//				child.setUom(rs.getString("uom"));
//				child.setCapacity(rs.getInt("capacity"));
//
//				//String assetId = rs.getString("AssetId");
//				if(treeMap.keySet().contains(parent)) {
//					treeMap.get(parent).add(child);
//				}else {
//					List<AssetModel> list = new ArrayList<AssetModel>();
//					list.add(child);
//					treeMap.put(parent, list);
//				}
//
//
//
//			}
//
//			assetdata.setAssetInfo(treeMap);
//
//
//			pstmt.close();
//			rs.close();
//			con.close();
//
//		} catch (Exception e) {
//			gAlert.ShowErrorAlert(e.getMessage());
//			e.printStackTrace();
//		} 
//		return assetdata;
//	}
//	
//	@Override
//	public AssetGroup getAssetTreefromGroupId(String groupId) {
//		AssetGroup assetdata = new AssetGroup();
//		try {
//			Connection con = SQLServerDAOFactory.createConnection();
//			PreparedStatement pstmt = con.prepareStatement("Declare @groupId as varchar(max) = ? " + 
//					"SELECT  a.[GroupId],[GroupName],[ParentAssetId],[AssetId],[AssetName],[Icon],[Category],[Image],a.[users] , a.[logbooks], a.[location] ,a.[manufacturer] ,a.[model] ,a.[serialNo] ,a.[description] ,a.[uom] ,a.[capacity]     " + 
//					"					FROM [dbo].[AssetInfo] a, dbo.AssetGroupInfo gr  " + 
//					"					Where a.GroupId = gr.GroupId AND gr.GroupId  = @groupId order by [NodeOrder]");
//			pstmt.setString(1, groupId);
//			ResultSet rs = pstmt.executeQuery();
//			Map<AssetModel,List<AssetModel>> treeMap = new LinkedHashMap<AssetModel,List<AssetModel>>();
//			while (rs.next()) {
//				assetdata.setGroupId(rs.getString("GroupId"));
//				assetdata.setGroupName(rs.getString("GroupName"));
//				String currentParentAssetId = rs.getString("ParentAssetId");
//				AssetModel parent = getAssetInfofromAssetId(currentParentAssetId,assetdata.getGroupId());
//				AssetModel child = new AssetModel(rs.getString("AssetId"), rs.getString("AssetName"), false, rs.getString("Icon"), rs.getString("Category"), rs.getString("Image"));
//
//
//				String logbooksListString = rs.getString("logbooks");
//				String usersListString = rs.getString("users");
//
//
//				if(logbooksListString != null) {
//					String[] logbooks = logbooksListString.split("\\s*,\\s*");
//					List<Integer> logbooksList = new ArrayList<Integer>();
//					for(String book : logbooks) {
//						if(!book.trim().equalsIgnoreCase(""))
//							logbooksList.add(Integer.parseInt(book));
//					}
//
//					child.setLogbooksList(logbooksList);
//				}
//				if(usersListString != null) {
//					String[] users = usersListString.split("\\s*,\\s*");
//
//					List<String> usersList = new ArrayList<String>();
//					for(String u : users) {
//						usersList.add(u);
//					}
//					child.setUsersList(usersList);
//
//				}
//
//				child.setLocation(rs.getString("location"));
//				child.setManufacturer(rs.getString("manufacturer"));
//				child.setModel(rs.getString("model"));
//				child.setSerialNo(rs.getString("serialNo"));
//				child.setDescription(rs.getString("description"));
//				child.setUom(rs.getString("uom"));
//				child.setCapacity(rs.getInt("capacity"));
//
//				//String assetId = rs.getString("AssetId");
//				if(treeMap.keySet().contains(parent)) {
//					treeMap.get(parent).add(child);
//				}else {
//					List<AssetModel> list = new ArrayList<AssetModel>();
//					list.add(child);
//					treeMap.put(parent, list);
//				}
//
//
//
//			}
//
//			assetdata.setAssetInfo(treeMap);
//
//
//			pstmt.close();
//			rs.close();
//			con.close();
//
//		} catch (Exception e) {
//			gAlert.ShowErrorAlert(e.getMessage());
//			e.printStackTrace();
//		} 
//		return assetdata;
//	}
//
//	private AssetModel getAssetInfofromAssetId(String assetId,String groupid) {
//		AssetModel model = null;
//		try {
//			Connection con = SQLServerDAOFactory.createConnection();
//			PreparedStatement pstmt = con.prepareStatement("SELECT  [GroupId],[ParentAssetId],[AssetId],[AssetName],[Icon],[Category],[Image],[users] ,[logbooks]  FROM [dbo].[AssetInfo] Where [AssetId] = ? AND [GroupId] = ?");
//			pstmt.setString(1, assetId);
//			pstmt.setString(2, groupid);
//			ResultSet rs = pstmt.executeQuery();
//			while(rs.next()) {
//
//				String logbooksListString = rs.getString("logbooks");
//				String usersListString = rs.getString("users");
//
//				model = new AssetModel(rs.getString("AssetId"), rs.getString("AssetName"), false, rs.getString("Icon"), rs.getString("Category"), rs.getString("Image"));
//
//				if(logbooksListString != null) {
//					List<Integer> logbooksList = new ArrayList<Integer>();
//					List<String> logbooks = Arrays.asList(logbooksListString.split("\\s*,\\s*"));
//					//logbooksList.addAll(logbooks);
//					logbooks.stream().filter(l -> !l.trim().equalsIgnoreCase("")).forEach(l -> logbooksList.add(Integer.parseInt(l)));
//					model.setLogbooksList(logbooksList);
//				}
//				if(usersListString != null) {
//					List<String> usersList = new ArrayList<String>();
//					List<String> users = Arrays.asList(usersListString.split("\\s*,\\s*"));
//					usersList.addAll(users);
//					model.setUsersList(usersList);
//
//				}
//
//			}
//			rs.close();
//			con.close();
//		}catch (Exception e) {
//			gAlert.ShowErrorAlert(e.getMessage());
//			e.printStackTrace();
//		}
//		if(model == null)
//			model = new AssetModel(groupid, groupid, false, null, null, null);
//		return model; 
//	}
//
//	@Override
//	public List<AssetGroup> getAllAssetGroups() {
//		List<AssetGroup> data = new LinkedList<AssetGroup>();
//		try {
//			Connection con = SQLServerDAOFactory.createConnection();
//			PreparedStatement pstmt = con.prepareStatement("SELECT  [GroupId],[GroupName],[CreatedBy],[CreatedOn] FROM [dbo].[AssetGroupInfo]");
//			ResultSet rs = pstmt.executeQuery();
//			while(rs.next()) {
//				data.add(new AssetGroup(rs.getString("GroupName"), rs.getString("GroupId"), rs.getString("CreatedBy"), rs.getString("CreatedOn")));
//			}
//			rs.close();
//			con.close();
//		}catch (Exception e) {
//			gAlert.ShowErrorAlert(e.getMessage());
//			e.printStackTrace();
//		}
//		return data;
//	}
//
//	@Override
//	public TreeNode traveseTree(AssetModel child, TreeNode parent,Map<AssetModel, List<AssetModel>> treeMap) {
//		TreeNode parentTreeNode = new DefaultTreeNode(child, parent);
//		if(treeMap.containsKey(child)) {
//			for(AssetModel listVal : treeMap.get(child)) {
//				traveseTree(listVal, parentTreeNode,treeMap);
//			}
//		}
//		return parentTreeNode;
//	}
//
//	@Override
//	public TreeNode constructAssetTreeFromMap(String groupId,String groupName,TreeNode assetRoot, Map<AssetModel, List<AssetModel>> treeMap) {
//		assetRoot = new DefaultTreeNode(new AssetModel("Root", "Root"), null);
//		AssetModel orgNode = new AssetModel(groupId,groupName);
//		//TreeNode childnode = new DefaultTreeNode(orgNode, tempTree);
//		traveseTree(orgNode, assetRoot,treeMap);
//		return assetRoot;
//	}
//
//	@Override
//	public List<User> getUserDeptOrGroupwsie(String selectedInput, String deptOrGroupSelected) {
//
//		String query = "";
//		if(deptOrGroupSelected.equals("department")) {
//			//query wrt dept
//			query = "SELECT [UserId] ,[UserName] FROM [DeptCredentials] as dct left join [UserCredential] as ucd on dct.[DeptId] = ucd.[Department] where dct.[DeptId] = ? AND ucd.[Active] = 1";
//
//		}
//		else {
//			//query wrt group
//			//in database department is named as roll
//			query = "SELECT ugt.[UserId],[UserName] FROM [UserRoll] as ugt left join [UserCredential] as ucd on ugt.[UserId] = ucd.[UserId] where ugt.[UserRoll] = ? and ucd.[Active] = 1";
//		}
//
//		List<User> data = new LinkedList<User>();
//		try {
//			Connection con = SQLServerDAOFactory.createUserConnection();
//			PreparedStatement pstmt = con.prepareStatement(query);
//			pstmt.setString(1, selectedInput);
//			ResultSet rs = pstmt.executeQuery();
//			while(rs.next()) {
//				data.add(new User(rs.getString("UserId")  , rs.getString("UserName")));
//			}
//
//			rs.close();
//			con.close();
//		}catch (Exception e) {
//			gAlert.ShowErrorAlert(e.getMessage());
//			e.printStackTrace();
//		}
//
//
//		return data;
//	}
//
//	@Override
//	public Map<String, String> getAllAssetsFromGroupMappedWithAssetId(String groupName) {
//
//
//		Map<String, String > data = new HashMap<String, String>();
//
//		try {
//			Connection con = SQLServerDAOFactory.createConnection();
//			PreparedStatement pstmt = con.prepareStatement("SELECT [GroupId] ,[ParentAssetId] ,[NodeOrder] ,[AssetId] ,[AssetName] ,[Icon] ,[Category] ,[Image] ,[users] ,[logbooks] FROM [dbo].[AssetInfo] where [GroupId] = (SELECT [GroupId] FROM [dbo].[AssetGroupInfo] where GroupName = ?)");
//			pstmt.setString(1, groupName);
//			ResultSet rs = pstmt.executeQuery();
//			while(rs.next()) {
//				data.put(rs.getString("AssetId"), rs.getString("AssetName"));
//			}
//			rs.close();
//			con.close();
//		}catch (Exception e) {
//			gAlert.ShowErrorAlert(e.getMessage());
//			e.printStackTrace();
//		}
//		return data;
//	}
//
//	@Override
//	public List<AssetModel> getAllAssetPositionFromGroupName(String groupName) {
//		// TODO Auto-generated method stub
//		List<AssetModel> result = new ArrayList<AssetModel>();
//
//		try {
//			Connection con = SQLServerDAOFactory.createConnection();
//			PreparedStatement pstmt = con.prepareStatement("SELECT [GroupId] , atab.[AssetId] , atab.[AssetName] , COALESCE([PosX] , '0px') [PosX], COALESCE([PosY] , '0px') [PosY],Icon FROM (SELECT [GroupId] , [AssetId] ,[AssetName],Icon FROM [dbo].[AssetInfo] where [GroupId] = (SELECT [GroupId] FROM [dbo].[AssetGroupInfo] where GroupName = ? )) as atab left join [dbo].[AssetNodePosition] as anptab ON atab.[AssetId] = anptab.[AssetId]");
//			pstmt.setString(1, groupName);
//			ResultSet rs = pstmt.executeQuery();
//			while(rs.next()) {
//				result.add(new AssetModel(rs.getString("GroupId"), rs.getString("AssetId"), rs.getString("AssetName"), rs.getString("PosX"), rs.getString("PosY"), rs.getString("icon")));
//			}
//			rs.close();
//			con.close();
//		}catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		return result;
//	}
//
//	@Override
//	public void saveAssetPosition(DefaultDiagramModel assetLocationOnMap , String assetGroup) {
//
//		try {
//			Connection con = SQLServerDAOFactory.createConnection();
//
//			PreparedStatement delAssetPos = con.prepareStatement("delete from [dbo].[AssetNodePosition] where AssetGroup = ?");
//			delAssetPos.setString(1, assetGroup);
//			delAssetPos.execute();
//
//			PreparedStatement assetPostionQuery = con.prepareStatement("INSERT INTO [dbo].[AssetNodePosition] ([AssetGroup] ,[AssetId] ,[PosX] ,[PosY]) VALUES (? ,? ,? ,?)");
//
//			for(Element element : assetLocationOnMap.getElements()) {
//
//				assetPostionQuery.setString(1, assetGroup);
//				assetPostionQuery.setString(2, element.getData().toString());
//				assetPostionQuery.setString(3, element.getX());
//				assetPostionQuery.setString(4, element.getY());
//
//				assetPostionQuery.addBatch();
//			}
//
//			assetPostionQuery.executeBatch();
//
//			con.close();
//		}
//		catch(Exception ex) {
//			ex.printStackTrace();
//		}
//	}
//
//	@Override
//	public void addAssetMap(String groupId, String mapImage) {
//
//		Connection conn = SQLServerDAOFactory.createConnection();
//		PreparedStatement pstm = null;
//
//		System.out.println(groupId  + " " + mapImage );
//		try {
//			String query = "  update [dbo].[AssetGroupInfo] set [backgroundImage] = ? where [GroupId] = ?;";
//			pstm = conn.prepareStatement(query);
//			pstm.setString(1, mapImage);
//			pstm.setString(2,  groupId);
//			pstm.executeUpdate();
//
//			pstm.close();
//			conn.close();
//			//			
//		} catch (SQLException e) {
//			System.out.println(e.getMessage());
//			e.printStackTrace();
//		}
//
//
//
//
//	}
//
//	@Override
//	public String getAssetMap(String groupId) {
//		Connection conn = SQLServerDAOFactory.createConnection();
//		PreparedStatement pstm = null;
//		String result = "";
//		ResultSet rs = null;
//
//		try {
//			String query = "select [backgroundImage] from [dbo].[AssetGroupInfo] where [GroupId] = ?;";
//			pstm = conn.prepareStatement(query);
//			pstm.setString(1, groupId);
//			rs = pstm.executeQuery();
//
//			while(rs.next()) {
//				result = rs.getString("backgroundImage");
//			}
//
//			pstm.close();
//			conn.close();
//			//			
//		} catch (SQLException e) {
//			System.out.println(e.getMessage());
//			e.printStackTrace();
//		}
//
//
//		return result;
//	}

}
