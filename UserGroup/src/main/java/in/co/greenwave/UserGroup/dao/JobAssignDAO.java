package in.co.greenwave.UserGroup.dao;

import java.util.Date;
import java.util.List;

import in.co.greenwave.UserGroup.model.TaskDetail;




public interface JobAssignDAO {

	//Retrieves published tasks
	public List<TaskDetail> getPublishedTasks(String tenantId);

}
