package in.co.greenwave.UserGroup.dao.implementation.sqlserver; // Define the package for the class

import java.sql.PreparedStatement; 
import java.sql.ResultSet; 
import java.sql.SQLException; 
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap; 
import java.util.LinkedHashMap; 
import java.util.List; 
import java.util.Map; 
import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.beans.factory.annotation.Qualifier; 
import org.springframework.dao.DataAccessException; 
import org.springframework.jdbc.core.JdbcTemplate; 
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.MultiValueMap;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import in.co.greenwave.UserGroup.dao.MenuInfoDAO; 
import in.co.greenwave.UserGroup.dao.factory.DAOFactory;
import in.co.greenwave.UserGroup.model.DashboardLogbook;
import in.co.greenwave.UserGroup.model.MenuBadgeDetails;
import in.co.greenwave.UserGroup.model.NavigationDetails; 
/**
 *  @Repository annotation defines that this class is where the database operations are performed.
 */
@Repository 
/**
 *  @author SreepriyaRoy
	
 * */
public class MenuInfoService implements MenuInfoDAO { 
	/**
	 * For Database Configuration : Refer Jdbcconfig class & application.properties
	 */

	@Autowired
	@Qualifier("DatasourceCollections")
	Map<String, List<JdbcTemplate>> jdbcTemplates; // Template for operations on the main database
	
	
	/**
	 * Retrieves the pages shown in the menubar
	 * @param userid, tenantId
	 */
	@Override 
	public NavigationDetails getNavigationDetails(String userid, String tenantId) { 
     
		// SQL query to retrieve page details based on user ID
		String sql = "SELECT AliasName , PageUrl, PageLogo FROM [dbo].[Pages] WHERE Page IN ( SELECT AllotedPage FROM [dbo].UserPages WHERE UserId =? ) order by sequence";
		JdbcTemplate jdbcTemplate_userModule=jdbcTemplates.get(tenantId).get(1);
		// Create a map to hold page URLs
		Map<String, String> pageurlmap = new LinkedHashMap(); // LinkedHashMap to maintain insertion order
		// Create a map to hold page logos
		Map<String, String> pagelogomap = new LinkedHashMap<>(); // LinkedHashMap for page logos

		// Execute the SQL query using the jdbcTemplate_userModule
		jdbcTemplate_userModule.execute(sql, new PreparedStatementCallback<Void>() { // Execute the SQL with a callback
			@Override // Override the doInPreparedStatement method
			public Void doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException { // Handle PreparedStatement execution
				ps.setString(1, userid); // Set the user ID in the PreparedStatement
//				ps.setString(2, tenantId); // (Commented out) Set tenant ID if needed
				ResultSet rs = ps.executeQuery(); // Execute the query and get the ResultSet
				while (rs.next()) { 
					String pagename = rs.getString(1); // Get the page name from the first column
					String pageurl = rs.getString(2); // Get the page URL from the second column
					pageurlmap.put(pagename, pageurl); // Put the page name and URL in the map
					String pagelogo = rs.getString(3); // Get the page logo from the third column
					pagelogomap.put(pagename, pagelogo); // Put the page name and logo in the map
				}
				return null; // Return null as required by the interface
			}
		}); // End of PreparedStatementCallback execution
		
		
		// Create a root NavigationDetails object to represent the top-level navigation
		NavigationDetails root = new NavigationDetails("root", null, null, null, null); // Initialize root navigation details
		for (Map.Entry<String, String> entry : pageurlmap.entrySet()) { // Iterate over each entry in the page URL map
			String[] pages = entry.getKey().split("/"); // Split the page name by '/'
			NavigationDetails parent = root; // Set the parent to the root initially
			for (int i = 0; i < pages.length; i++) { // Iterate over each page in the split array
				String currentpage = pages[i]; // Get the current page
				String parentpage = entry.getKey().substring(0, entry.getKey().indexOf(currentpage)); // Get the parent page name

				// Find the child node in the navigation structure
				NavigationDetails currentnode = findChildNode(parent, parentpage, currentpage); // Call method to find the child node
				// String label, String url, List<NavigationDetails> items, String parent
				if (currentnode == null) { // If the current node is not found
					// Create a new NavigationDetails object for the current page
					currentnode = new NavigationDetails(currentpage, "", null, parentpage, pagelogomap.get(entry.getKey())); // Initialize the current node
					if (i == (pages.length - 1)) // If it's the last page in the split array
						currentnode.setPath(entry.getValue()); // Set the path for the current node
				}
				List<NavigationDetails> children = new ArrayList<>(); // Create a new list for child nodes
				if (parent.getItems() != null) // If the parent has existing items
					children = parent.getItems(); // Get the existing items

				int flag = 0; // Initialize a flag to check for duplicates
				if (parent.getItems() != null) // If the parent has items
					for (NavigationDetails node : parent.getItems()) // Iterate over each child node
						if (node.getLabel().equals(currentpage)) // Check if the current page already exists
							flag = 1; // Set flag to indicate duplication
				if (flag == 0) // If the page does not exist
					children.add(currentnode); // Add the current node to the children list
				parent.setItems(children); // Set the updated children list in the parent
				parent = currentnode; // Update the parent to the current node
			}
			
            
		}
		return root; // Return the constructed navigation details
	}
	
	// Method to find a child node within the navigation structure
	private NavigationDetails findChildNode(NavigationDetails parent, String parentpage, String currentpage) { // Define the method to find a child node
		if (parent.getItems() != null) { // If the parent has children
			for (NavigationDetails node : parent.getItems()) { // Iterate over each child node
				if (node.getParent().equals(parentpage) && node.getLabel().equals(currentpage)) { // Check for a match
					return node; // Return the found node
				} else // If not found
					findChildNode(node, parentpage, currentpage); // Recursively search in the child node
			}
		}
		return null; // Return null if the child node is not found
	}

	@Override
	public MenuBadgeDetails fetchDetailsForMenusBadges(String userId,String groupDetails, String tenantId) {
		// TODO Auto-generated method stub
		
		JdbcTemplate jdbcTemplate=jdbcTemplates.get(tenantId).get(0);
		String sql="DECLARE @userId varchar(max)=?,@groupDetails varchar(max)=?, @tasksToReviewCount int=0,@jobsToreviewCount int=0,@activitiesToPerformCount int =0,@activitiesToReviewCount int=0,@reportsCount int =0; "+
		"select @tasksToReviewCount=count(*) from [dbo].TaskDetails where PublishRejectUserId=@userId and Status='Pending';"+
				"Select @jobsToreviewCount=count(*) from [dbo].JobDetails where   Status in ('Sent For Approval') and Approver=@userId;Select @jobsToreviewCount=@jobsToreviewCount+count(*) from [dbo].[AutoJobInfo] Where [reviewer] = @userId AND [status] IN ('Sent for approval','Review Started')"+
				"Select @activitiesToReviewCount=count(*) from [dbo].JobActivityDetails JA where Status IN ('Review Started','Sent For Approval') and Approver=@userId and  exists(Select J.Status from [dbo].JobDetails J WHERE JA.JobId=J.JobId AND J.Status NOT IN ('Completed','Rejected','Aborted')) and Approver!=Performer;"+
		"Select @reportsCount=count(distinct(ReportName))  from [dbo].reportconfigurator where SharedUser like '%'+@userId+'%'; DECLARE @jobid varchar(max)='',@performergroup varchar(max)=Concat(@userId,',',@groupDetails); SELECT @jobid=concat(@jobid,',',JobId) FROM [dbo].[JobDetails] where JobId in (select JobId FROM [dbo].[JobActivityDetails] where Performer=@userid or GroupOrDeptName IN (Select * from String_Split(@performergroup,',') where value!='')) and Status not in ('Sent For Approval','Completed','Rejected','Aborted');" +
				"Select @activitiesToPerformCount=count(*) from [dbo].JobActivityDetails JA left join [dbo].ActivityCreation Act  on Act.ActivityId=JA.ActivityId and Act.TaskId=JA.TaskId  where  Status !='Completed' and status!='Aborted' and Status!='Review Started' and Status!='Sent For Approval' and ((Performer in (SELECT * FROM String_Split(@performergroup,','))AND Performer!='') or GroupOrDeptName in ((SELECT * FROM String_Split(@performergroup,','))) AND GroupOrDeptName!='') and JobId in(Select * from String_Split(@jobid,',')) AND( NOT EXISTS(SELECT * FROM dbo.JobActivityDetails A WHERE Status!='Completed' and status!='Aborted'AND A.JobId=JA.JobId and A.ActivityId in(SELECT SourceId FROM ActivityConnections AC WHERE TargetId=JA.ActivityId)) or  enableStart=1 ); "+
		"select @tasksToReviewCount as tasksToReviewCount,@jobsToreviewCount as jobsToreviewCount,@activitiesToPerformCount as activitiesToPerformCount,@activitiesToReviewCount as activitesToReviewCount,@reportsCount as reportsCount;";
		MenuBadgeDetails menuBadgeDetails = jdbcTemplate.query(
			    sql,
			    ps -> {
			        ps.setString(1, userId);
			        ps.setString(2, groupDetails);
			    },
			    rs -> {
			        MenuBadgeDetails details = new MenuBadgeDetails();
			        if (rs.next()) {
			            details.setTasksToReviewCount(rs.getInt("tasksToReviewCount"));
			            details.setJobsToReviewCount(rs.getInt("jobsToreviewCount"));
			            details.setActivitiesToPerformCount(rs.getInt("activitiesToPerformCount"));
			            details.setActivitiesToReviewCount(rs.getInt("activitesToReviewCount"));
			            details.setReportsCount(rs.getInt("reportsCount"));
			        }
			        return details;
			    }
			);
		return menuBadgeDetails;
	}
	@Override
	public List<DashboardLogbook> getDashboardLogbook(String tenantid){
		JdbcTemplate jdbcTemplate=jdbcTemplates.get(tenantid).get(0);
		String sql="SELECT [FormId] ,[FormName] ,[DashboardType] ,VersionNumber FROM [GREENWAVE].[dbo].[DigitalLogbookFormInfo] where isActiveForm=1 and DashboardType=1";
		List<DashboardLogbook> dashboardlogbooks=new ArrayList<DashboardLogbook>();
		jdbcTemplate.execute(sql,new PreparedStatementCallback<Void>() {

			@Override
			public Void doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
				// TODO Auto-generated method stub
				ResultSet rs=ps.executeQuery();
				while(rs.next()) {
					DashboardLogbook dl=new DashboardLogbook();
					dl.setFormid(rs.getString("FormId"));
					dl.setFormname(rs.getString("FormName"));
					dl.setVersionNumber(rs.getString("VersionNumber"));
					dashboardlogbooks.add(dl);
					
				}
				return null;
			}
			
		});
		return dashboardlogbooks;
	}
	
}
