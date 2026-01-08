package in.co.greenwave.UserGroup.dao.implementation.sqlserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.stereotype.Repository;
import in.co.greenwave.UserGroup.dao.PageDetailsDAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *  @Repository annotation defines that this class is where the database operations are performed.
 */
@Repository
public class PageDetailsService implements PageDetailsDAO{

	 
//		@Autowired
//		private DynamicDatasourceDAO dynamicDatasourceDAO;
	
	/**
	 * For Database Configuration : Refer Jdbcconfig class & application.properties
	 */

	@Autowired
	@Qualifier("DatasourceCollections")
	Map<String, List<JdbcTemplate>> jdbcTemplates; // Template for operations on the main database
	
	
	
	@Override // Indicate that this method overrides a method in the interface
	public List<String> getAllotedPages(String userid, String tenantid) { // Define method to get allotted pages for a user

//			JdbcTemplate jdbcTemplateDynamicConnection  = dynamicDatasourceDAO.getDynamicConnection(tenantid).get("db_op360_usermodule"); // (Commented out) Get dynamic JdbcTemplate based on tenant ID
		JdbcTemplate jdbcTemplate_userModule=jdbcTemplates.get(tenantid).get(1);
		// Define SQL query to retrieve page URLs for allotted pages based on user ID and tenant ID
		String sql = "Select PageUrl From  [dbo].Pages where [Page] in (SELECT [AllotedPage] FROM [dbo].[UserPages] where UserId=? and TenantId=?)"; 
		List<String> allotedPages = new LinkedList<>(); // Create a list to hold allotted page URLs
		
		// Execute the SQL query using jdbcTemplate_userModule
		jdbcTemplate_userModule.execute(sql, new PreparedStatementCallback<Void>() { // Execute the SQL with a callback
			@Override // Override the doInPreparedStatement method
			public Void doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException { // Handle PreparedStatement execution
				ps.setString(1, userid); // Set the user ID in the PreparedStatement
				ps.setString(2, tenantid); // Set the tenant ID in the PreparedStatement
				ResultSet rs = ps.executeQuery(); // Execute the query and get the ResultSet
				while (rs.next()) { // Iterate over the ResultSet
					allotedPages.add(rs.getString("PageUrl")); // Add the PageUrl to the list of allotted pages
				}
				return null; // Return null as required by the interface
			}
		}); // End of PreparedStatementCallback execution
		return allotedPages; // Return the list of allotted pages
	}

}
