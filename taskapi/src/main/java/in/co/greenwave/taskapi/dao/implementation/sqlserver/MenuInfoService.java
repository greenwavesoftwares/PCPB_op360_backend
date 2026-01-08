package in.co.greenwave.taskapi.dao.implementation.sqlserver;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.stereotype.Repository;

import in.co.greenwave.taskapi.dao.MenuInfoDAO;
import in.co.greenwave.taskapi.model.NavigationDetails;

@Repository
public class MenuInfoService implements MenuInfoDAO{
	
	@Autowired
	@Qualifier("jdbcTemplate2")
	private JdbcTemplate jdbctemplate;
	@Override
	public NavigationDetails getNavigationDetails(String userid) {
		System.out.println("getNavigationDetailsService");
		
		String sql="SELECT Page, PageUrl,PageLogo \r\n"
				+ "FROM [dbo].[Pages] \r\n"
				+ "WHERE Page COLLATE SQL_Latin1_General_CP1_CI_AS IN (\r\n"
				+ "  SELECT AllotedPage COLLATE SQL_Latin1_General_CP1_CI_AS \r\n"
				+ "  FROM UserPages \r\n"
				+ "  WHERE UserId = ? \r\n"
				+ ");";
		System.out.println(sql);
		Map<String,String> pageurlmap=new HashMap<>();
		Map<String,String> pagelogomap=new HashMap<>();
		jdbctemplate.execute(sql, new PreparedStatementCallback<Void>() {
		
			@Override
			public Void doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
				ps.setString(1, userid);
				ResultSet rs = ps.executeQuery();
				while(rs.next()) {
					String pagename=rs.getString(1);
					String pageurl=rs.getString(2);
					pageurlmap.put(pagename, pageurl);
					String pagelogo=rs.getString(3);
					pagelogomap.put(pagename, pagelogo);
				}
				return null;
			}

			
		});
		System.out.println("Pagename"+pageurlmap.toString());
		System.out.println("userid:"+userid);
		
		NavigationDetails root=new NavigationDetails("root",null,null,null,null);
		for (Map.Entry<String,String> entry : pageurlmap.entrySet())  {
			String[] pages=entry.getKey().split("/");
			NavigationDetails parent=root;
			for(int i=0;i<pages.length;i++) {
				String currentpage=pages[i];
				String parentpage=entry.getKey().substring(0,entry.getKey().indexOf(currentpage));
				
				NavigationDetails currentnode=findChildNode(parent,parentpage,currentpage);
				//String label, String url, List<NavigationDetails> items, String parent
				if(currentnode==null) {
					currentnode=new NavigationDetails(currentpage,"",null,parentpage,pagelogomap.get(entry.getKey()));
					if(i==(pages.length-1))
						currentnode.setPath(entry.getValue());
				}
				List<NavigationDetails> children=new ArrayList<>();
				if(parent.getItems()!=null)
					children=parent.getItems();
				
				int flag=0;
				if(parent.getItems()!=null)
				for(NavigationDetails node: parent.getItems())
					if(node.getLabel().equals(currentpage))
						flag=1;
				if(flag==0)
				children.add(currentnode);
				parent.setItems(children);
//				parent.setItems(children);
				parent=currentnode;
			}
			
            //createTree(entry.getKey(),entry.getValue()); 
            
		}
		return root;
		
	}
	private NavigationDetails findChildNode(NavigationDetails parent, String parentpage,String currentpage) {
		if(parent.getItems()!=null)
		{
			for(NavigationDetails node:parent.getItems()) {
				if(node.getParent().equals(parentpage)&&node.getLabel().equals(currentpage)) {
					System.out.println("Find child===> Parent: "+parent.getLabel()+" Child: "+currentpage);
					return node;
				}
				else
					findChildNode(node,parentpage,currentpage);
			}
		}
		return null;
	}
}
