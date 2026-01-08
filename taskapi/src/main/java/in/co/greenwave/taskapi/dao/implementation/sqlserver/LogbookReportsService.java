package in.co.greenwave.taskapi.dao.implementation.sqlserver;

import in.co.greenwave.taskapi.dao.LogbookReportsDAO;




public class LogbookReportsService implements LogbookReportsDAO {
//	private final G360Alert ALERTOBJ=new G360Alert();
//	@Override
//	public void insertReportConfig(String reportname,List<ReportConfig> reportconfiglist,String creatorId,List<User> sharedUserLists,FormDetails formdetails) {
//		String sql1="Select count(ReportName) as [Total_Report] from dbo.reportconfigurator where ReportName=?";
//		Connection con = null;
//		PreparedStatement pstmt = null;
//		try {
//			con = SQLServerDAOFactory.createConnection();
//			pstmt = con.prepareStatement(sql1);
//			pstmt.setString(1, reportname);
//			ResultSet rs=pstmt.executeQuery();
//			while(rs.next()) {
//				if(Integer.parseInt(rs.getString("Total_Report"))>0) {
//					ALERTOBJ.ShowErrorAlert("Report Name Already Exist");
//					rs.close();
//					return;
//				}
//			}
//			rs.close();
//			
//		}catch(Exception e) {
//			e.printStackTrace();
//		}finally {
//			try {
//				con.close();
//				pstmt.close();
//		}catch(Exception e1) {
//				e1.printStackTrace();
//			}
//		}
//		
//		String s="";
//		
//	for(User user:sharedUserLists) {
//				s+=user.getUserID()+",";
//			}
//			if(s.trim().length()>0) {
//			s=s.substring(0, s.lastIndexOf(','));
//		}
//		String sql="Insert into dbo.reportconfigurator(ReportName,FormName,CellId,ReportAliasId,FormAliasId,FormId,VersionNumber,creator,SharedUser) values(?,?,?,?,?,?,?,?,?) ";
//		
//		/*Connection con = null;
//		PreparedStatement pstmt = null;*/
//		
//	
//		try {
//			con = SQLServerDAOFactory.createConnection();
//			con.setAutoCommit(false);  
//			//Statement stmt=con.createStatement();  
//			pstmt = con.prepareStatement(sql);
//			for(ReportConfig reportconfig:reportconfiglist) {
//				List<ReportData> reportDataList=reportconfig.getReportDataList();
//				for(ReportData reportdata:reportDataList) {
//					pstmt.setString(1, reportname);
//					pstmt.setString(2, reportconfig.getFormName());
//					pstmt.setString(3, reportdata.getCellId());
//					pstmt.setString(4, reportdata.getReportAliasId());
//					pstmt.setString(5, reportdata.getFormAliasId());
//					pstmt.setInt(6, formdetails.getFormID());
//					pstmt.setInt(7, formdetails.getVersionNumber());
//					System.out.println(formdetails.getVersionNumber());
//					pstmt.setString(8, creatorId);
//					pstmt.setString(9, s);
//					pstmt.addBatch();
//					//System.out.println("Inside Report Data");
//				}
//				int[] count=pstmt.executeBatch();
//			}
//			
//			
//			con.commit();
//		}catch(Exception e) {
//			e.printStackTrace();
//		}finally {
//			try {
//				con.close();
//				pstmt.close();
//		}catch(Exception e1) {
//				e1.printStackTrace();
//			}
//		}
//	}
//
//	@Override
//	public List<ReportConfig> userWiseReports(String userid) {
//		String sql="select  distinct ReportName,FormName,VersionNumber,SharedUser  from dbo.reportconfigurator where creator='"+userid+"'";
//		String sql1="select  distinct CellId,ReportAliasId,FormAliasId  from dbo.reportconfigurator where creator=? and ReportName=?";
//		Connection con = null;
//		PreparedStatement pstmt = null;
//		PreparedStatement pstmt1 = null;
//		List<ReportConfig> reports=new ArrayList<ReportConfig>();
//		List<ReportData> reportdataList=new ArrayList<ReportData>();
//		try {
//			con = SQLServerDAOFactory.createConnection();
//			pstmt = con.prepareStatement(sql);
//			pstmt1=con.prepareCall(sql1);
//			ResultSet rs=pstmt.executeQuery();
//			
//			ReportConfig reportconfig=new ReportConfig();
//			ReportData reportdata=new ReportData();
//	
//			while(rs.next()) {
//				
//		        pstmt1.setString(1, userid);
//		        pstmt1.setString(2, rs.getString("ReportName"));
//				ResultSet rs1=pstmt1.executeQuery();
//				reportdataList=new ArrayList<ReportData>();
//				while(rs1.next()) {
//					reportdata=new ReportData();
//					reportdata.setCellId(rs1.getString("CellId"));
//					reportdata.setFormAliasId(rs1.getString("FormAliasId"));
//					reportdata.setReportAliasId(rs1.getString("ReportAliasId"));
//					reportdataList.add(reportdata);
//				}
//				
//				
//				reportconfig=new ReportConfig();
//				
//				
//				reportconfig.setReportName(rs.getString("ReportName"));
//				
//				reportconfig.setFormName(rs.getString("FormName"));
//				reportconfig.setFormVersion(rs.getString("VersionNumber"));
//				String users[]=rs.getString("SharedUser").split(",");
//				
//				List<User> userList=new ArrayList<User>();
//				for(int i=0;i<users.length;i++) {
//					User user=new User();
//					user.setUserID(users[i]);
//					userList.add(user);
//				}
//				
//				reportconfig.setSharedUsers(userList);
//				reportconfig.setReportDataList(reportdataList);
//				reports.add(reportconfig);
//				
//			}
//		}catch(Exception e) {
//			e.printStackTrace();
//		}finally {
//			try {
//				con.close();
//				pstmt.close();
//		}catch(Exception e1) {
//				e1.printStackTrace();
//			}
//		}	
//		return reports;
//	}
//
//	@Override
//	public int updateReports(String reportname, List<User> sharedUserList) {
//		String s="";
//		for(User user:sharedUserList) {
//			s+=user.getUserID()+",";
//		}
//		s=s.substring(0, s.lastIndexOf(','));
//		String sql="Update dbo.reportconfigurator set SharedUser=? where ReportName='"+reportname+"'";
//
//		Connection con = null;
//		PreparedStatement pstmt = null;
//		try {
//			con = SQLServerDAOFactory.createConnection();
//			pstmt = con.prepareStatement(sql);
//			pstmt.setString(1, s);
//			pstmt.addBatch();
//			int[] count=pstmt.executeBatch();
//			con.commit();
//		}catch(Exception e) {
//			e.printStackTrace();
//			return 0;
//		}finally {
//			try {
//				con.close();
//				pstmt.close();
//		}catch(Exception e1) {
//				e1.printStackTrace();
//			}
//		}
//		return 1;
//	}
//	@Override
//	public int updateReportsCell(String reportname, List<ReportData> reportDataList) {
//		// TODO Auto-generated method stub
//		String sql="update dbo.reportconfigurator set ReportAliasId=? where reportname=? and cellid=? and formaliasid=?";
//		
//
//		Connection con = null;
//		PreparedStatement pstmt = null;
//		try {
//			con = SQLServerDAOFactory.createConnection();
//			pstmt = con.prepareStatement(sql);
//			for(ReportData reportdata:reportDataList ) {
//				pstmt.setString(1, reportdata.getReportAliasId());
//				pstmt.setString(2, reportname);
//				pstmt.setString(3, reportdata.getCellId());
//				pstmt.setString(4, reportdata.getFormAliasId());
//				
//				pstmt.addBatch();
//			}
//			int count[]=pstmt.executeBatch();
//		}catch(Exception e) {
//			e.printStackTrace();
//			return 0;
//			}finally {
//				try {
//					con.close();
//					pstmt.close();
//				}catch(Exception e1) {
//					e1.printStackTrace();
//				}
//		
//			}
//		return 1;
//	}
//
//	
//	@Override
//	public ReportConfig fetchLogbookReportData(String fromdate, String todate, String reportName) {
//		ReportConfig reportData = new ReportConfig();
//		List<DBResultModel> resultData = new LinkedList<DBResultModel>();
//		Connection con = null;
//		PreparedStatement pstmt = null;
//		
//		try {
//			con = SQLServerDAOFactory.createConnection();
//			pstmt = con.prepareStatement("\r\n" + 
//					"DECLARE @t nVARCHAR(max)\r\n" + 
//					"\r\n" + 
//					"SELECT @t = STUFF((SELECT ',' + CellId FROM [dbo].[reportconfigurator] \r\n" + 
//					"WHERE ReportName = ? FOR XML PATH('')), 1, 1, '');\r\n" + 
//					"\r\n" + 
//					"DECLARE @key nVARCHAR(max) = @t;\r\n" + 
//					"declare @ReportName  nvarchar(100)=?;\r\n" + 
//					"declare @fromtimestamp   datetime=?;\r\n" + 
//					"declare @totimestamp   datetime=?;\r\n" + 
//					"DECLARE @keys nvarchar(max) = '';\r\n" + 
//					"SELECT @keys += QUOTENAME(value) + ',' FROM string_split(@key, ',');\r\n" + 
//					"SET @keys = LEFT(@keys, LEN(@keys) - 1);\r\n" + 
//					"\r\n" + 
//					"SET NOCOUNT ON;\r\n" + 
//					"\r\n" + 
//					"DECLARE @dynamic as NVARCHAR(MAX) = concat('\r\n" + 
//					"SELECT *\r\n" + 
//					"FROM (\r\n" + 
//					"    SELECT\r\n" + 
//					"        [Value], \r\n" + 
//					"        Timestamp,\r\n" + 
//					"        [key]\r\n" + 
//					"    FROM (\r\n" + 
//					"        SELECT DISTINCT\r\n" + 
//					"            t.formname,\r\n" + 
//					"            version,\r\n" + 
//					"            transactionid,\r\n" + 
//					"            JSON_VALUE([value], ''$.value'') AS [value],\r\n" + 
//					"            timestamp,\r\n" + 
//					"            [key] COLLATE SQL_Latin1_General_CP1_CI_AS AS [key]\r\n" + 
//					"        FROM\r\n" + 
//					"            dbo.LogbookTransactionData t\r\n" + 
//					"            CROSS APPLY OPENJSON(t.logbookdata),\r\n" + 
//					"            dbo.DigitalLogbookCellDetails a,\r\n" + 
//					"            dbo.DigitalLogbookFormInfo b\r\n" + 
//					"        WHERE\r\n" + 
//					"            t.timestamp BETWEEN ''',@fromtimestamp,''' AND ''' ,@totimestamp, '''\r\n" + 
//					"            AND [key] COLLATE SQL_Latin1_General_CP1_CI_AS = a.CellId COLLATE SQL_Latin1_General_CP1_CI_AS\r\n" + 
//					"            AND a.FormId = b.FormId\r\n" + 
//					"            AND t.formname = b.FormName\r\n" + 
//					"            AND t.version = b.VersionNumber\r\n" + 
//					"            AND [value] LIKE ''{\"va%''\r\n" + 
//					"    ) tab\r\n" + 
//					"    LEFT JOIN [dbo].[reportconfigurator] r ON tab.formname = r.FormName AND tab.[key] = r.CellId\r\n" + 
//					"    WHERE ReportName = ''',@ReportName,'''\r\n" + 
//					") AS pivot_tab\r\n" + 
//					"PIVOT (\r\n" + 
//					"    MAX([Value]) FOR [key] IN (' ,@keys, ')\r\n" + 
//					") AS pivot_result\r\n" + 
//					"ORDER BY Timestamp;\r\n" + 
//					"')\r\n" + 
//					"exec( @dynamic);");
//			
//			pstmt.setString(1, reportName);
//			pstmt.setString(2, reportName);
//			pstmt.setString(3, fromdate);
//			pstmt.setString(4, todate);
//			ResultSet rs = pstmt.executeQuery();
//			ResultSetMetaData rsmd = rs.getMetaData();
//			
//			while(rs.next()) {
//				Map<String, String> dbmap = new LinkedHashMap<String,String>();
//				for(int i = 1 ; i<=rsmd.getColumnCount() ; i++) {
//					dbmap.put(rsmd.getColumnName(i), rs.getString(rsmd.getColumnName(i)));
//				}
//				DBResultModel dbm = new DBResultModel(dbmap);
//				resultData.add(dbm);
//			}
//			reportData.setResultData(resultData);
//			
//			rs.close();
//		} 
//		catch (Exception e) {
//			ALERTOBJ.ShowErrorAlert(e.getMessage());
//			e.printStackTrace();
//		}
//		finally {
//			try {
//				con.close();
//				pstmt.close();
//			} catch (SQLException e) { 
//				ALERTOBJ.ShowErrorAlert(e.getMessage());
//				e.printStackTrace();
//			}
//		}
//		return reportData;
//	}
//
//	@Override
//	public ReportConfig fetchReportMetaData(String reportName) {
//		ReportConfig reportData = new ReportConfig();
//		List<ReportData> reportDataList = new LinkedList<ReportData>();
//		Connection con = null;
//		PreparedStatement pstmt = null;
//		
//		try {
//			con = SQLServerDAOFactory.createConnection();
//			pstmt = con.prepareStatement("SELECT distinct ReportName,FormName,VersionNumber,CellId,ReportAliasId FROM [dbo].[reportconfigurator]\r\n" + 
//					"where ReportName = ? order by CellId");
//			
//			pstmt.setString(1, reportName);
//			ResultSet rs = pstmt.executeQuery();
//			
//			String rName = "";
//			String formName = "";
//			String versionNumber = "";
//			
//			while(rs.next()) {
//				rName = rs.getString("ReportName");
//				formName = rs.getString("FormName");
//				versionNumber = rs.getString("VersionNumber");
//				ReportData rd = new ReportData();
//				rd.setCellId(rs.getString("CellId"));
//				rd.setReportAliasId(rs.getString("ReportAliasId"));
//				if (!(reportDataList.contains(rd)))
//					reportDataList.add(rd);
//			}
//			reportData.setReportName(rName);
//			reportData.setFormName(formName);
//			reportData.setFormVersion(versionNumber);
//			reportData.setReportDataList(reportDataList);
//			
//			rs.close();
//		} 
//		catch (Exception e) {
//			ALERTOBJ.ShowErrorAlert(e.getMessage());
//			e.printStackTrace();
//		}
//		finally {
//			try {
//				con.close();
//				pstmt.close();
//			} catch (SQLException e) { 
//				ALERTOBJ.ShowErrorAlert(e.getMessage());
//				e.printStackTrace();
//			}
//		}
//		return reportData;
//	}
//
//	@Override
//	public List<String> getReportNames(String userid) {
//		List<String> reportNames = new ArrayList<String>();
//		
//		Connection con = null;
//		PreparedStatement pstmt = null;
//		
//		try {
//			con = SQLServerDAOFactory.createConnection();
//			pstmt = con.prepareStatement("SELECT distinct Userid,Reportname FROM [dbo].[reportconfigurator] r\r\n" + 
//					"cross apply (select item as Userid from dbo.fnSplitString(SharedUser,',')) t\r\n" + 
//					"where Userid = ?\r\n" + 
//					"order by userid,Reportname");
//			
//			pstmt.setString(1, userid);
//			ResultSet rs = pstmt.executeQuery();
//			
//			while(rs.next()) {
//				reportNames.add(rs.getString("Reportname"));
//			}
//			
//			rs.close();
//		} 
//		catch (Exception e) {
//			ALERTOBJ.ShowErrorAlert(e.getMessage());
//			e.printStackTrace();
//		}
//		finally {
//			try {
//				con.close();
//				pstmt.close();
//			} catch (SQLException e) { 
//				ALERTOBJ.ShowErrorAlert(e.getMessage());
//				e.printStackTrace();
//			}
//		}
//		return reportNames;
//	}
//
//	@Override
//	public void deleteReportConfig(ReportConfig reportconfig) {
//		// TODO Auto-generated method stub
//		String sql="delete from dbo.reportconfigurator where ReportName=? and FormName=?";
//		Connection con = null;
//		PreparedStatement pstmt = null;
//		
//		try {
//			con = SQLServerDAOFactory.createConnection();
//			pstmt=con.prepareStatement(sql);
//			pstmt.setString(1, reportconfig.getReportName());
//			pstmt.setString(2, reportconfig.getFormName());
//			pstmt.addBatch();
//			int[] c=pstmt.executeBatch();
//			ALERTOBJ.ShowInfoAlert("Report is deleted");
//			
//		} 
//		catch (Exception e) {
//			ALERTOBJ.ShowErrorAlert(e.getMessage());
//			e.printStackTrace();
//		}
//		finally {
//			try {
//				con.close();
//				pstmt.close();
//			} catch (SQLException e) { 
//				ALERTOBJ.ShowErrorAlert(e.getMessage());
//				e.printStackTrace();
//			}
//		}
//		
//	}
//
//	@Override
//	public void updateReports(String creatorid,String reportname, ReportConfig reportconfig,List<ReportData> reportDataList,List<User> users,FormDetails formdetails) {
//		
//		
//		String s="";
//		for(User user:users) {
//			s+=user.getUserID()+",";
//		}
//		
//		s=s.substring(0, s.lastIndexOf(','));
//		String sql="delete from dbo.reportconfigurator where ReportName=? and FormName=? and Creator=?";
//		Connection con = null;
//		PreparedStatement pstmt = null;
//	
//		try {
//			con = SQLServerDAOFactory.createConnection();
//			pstmt=con.prepareStatement(sql);
//			pstmt.setString(1, reportname);
//			pstmt.setString(2, reportconfig.getFormName());
//			pstmt.setString(3, creatorid);
//			pstmt.addBatch();
//			int[] c=pstmt.executeBatch();
//		}	catch (Exception e) {
//			ALERTOBJ.ShowErrorAlert(e.getMessage());
//			e.printStackTrace();
//		}
//		finally {
//			try {
//				con.close();
//				pstmt.close();
//			} catch (SQLException e) { 
//				ALERTOBJ.ShowErrorAlert(e.getMessage());
//				e.printStackTrace();
//			}
//		}
//		sql="Insert into dbo.reportconfigurator(ReportName,FormName,CellId,ReportAliasId,FormAliasId,FormId,VersionNumber,creator,SharedUser) values(?,?,?,?,?,?,?,?,?) ";
//			try {
//				con = SQLServerDAOFactory.createConnection();
//				pstmt=con.prepareStatement(sql);
//			for(ReportData reportdata:reportDataList) {
//				pstmt.setString(1, reportname);
//				pstmt.setString(2, reportconfig.getFormName());
//				pstmt.setString(3, reportdata.getCellId());
//				pstmt.setString(4, reportdata.getReportAliasId());
//				pstmt.setString(5, reportdata.getFormAliasId());
//				pstmt.setInt(6, formdetails.getFormID());
//				pstmt.setInt(7, formdetails.getVersionNumber());
//				System.out.println(formdetails.getVersionNumber());
//				pstmt.setString(8, creatorid);
//				pstmt.setString(9, s);
//				pstmt.addBatch();
//				
//			}
//			int count[]=pstmt.executeBatch();
//					ALERTOBJ.ShowInfoAlert("Report Updated");
//		} 
//		catch (Exception e) {
//			ALERTOBJ.ShowErrorAlert(e.getMessage());
//			e.printStackTrace();
//		}
//		finally {
//			try {
//				con.close();
//				pstmt.close();
//			} catch (SQLException e) { 
//				ALERTOBJ.ShowErrorAlert(e.getMessage());
//				e.printStackTrace();
//			}
//		}
//	}	
}
