package in.co.greenwave.UserGroup.dao;

import java.util.List;

import org.springframework.util.MultiValueMap;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import in.co.greenwave.UserGroup.model.DashboardLogbook;
import in.co.greenwave.UserGroup.model.MenuBadgeDetails;
import in.co.greenwave.UserGroup.model.NavigationDetails;

public interface MenuInfoDAO {

	//Retrieves menu details for the user
	NavigationDetails getNavigationDetails(String userid,String teanantId);

	MenuBadgeDetails fetchDetailsForMenusBadges(String userId, String groupDetails, String tenantId);

	List<DashboardLogbook> getDashboardLogbook(String tenantid);

}
