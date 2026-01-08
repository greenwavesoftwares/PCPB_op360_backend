package in.co.greenwave.UserGroup.dao;

import java.util.List;

public interface PageDetailsDAO {
	//Retrieves alloted pages for the user
	public List<String> getAllotedPages(String userid,String tenantid);
}
