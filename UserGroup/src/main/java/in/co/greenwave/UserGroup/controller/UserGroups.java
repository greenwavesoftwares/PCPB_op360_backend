package in.co.greenwave.UserGroup.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.co.greenwave.UserGroup.dao.JobAssignDAO;
import in.co.greenwave.UserGroup.dao.factory.DAOFactory;
import in.co.greenwave.UserGroup.model.TaskDetail;

@RestController
@RequestMapping("/usergroup")
public class UserGroups {



	@Autowired
	private DAOFactory factory;

	private JobAssignDAO jobAssignDAO;




//	/**
//	 * Retrieves public tasks associated with a specific job asset group.
//	 * Fetches task details from the data source and filters tasks based on the provided job asset group.
//	 * Returns a list of task details associated with the specified job asset group.
//	 * Handles cases where no tasks are found or if an error occurs during the process.
//	 * 
//	 * @param group The name of the job asset group for filtering tasks
//	 * @return List of TaskDetail containing tasks associated with the specified job asset group
//	 *         Returns an empty list if no tasks are found or an error response if an exception occurs.
//	 */

	//http://localhost:8081/usergroup/jobAssetGroup/software@greenwave.co.in
	@GetMapping("/jobAssetGroup/{group}/{tenantId}")
	public ResponseEntity<List<TaskDetail>> getPublishedTask(@PathVariable("tenantId") String tenantId,@PathVariable("group") String group) {
	    try {
	        jobAssignDAO = factory.getJobAssignDAOService();
	        List<TaskDetail> publishedTasks = jobAssignDAO.getPublishedTasks(tenantId);
	        List<TaskDetail> publishedTask = new ArrayList<>();

	        for (TaskDetail taskDetail : publishedTasks) {
	                publishedTask.add(taskDetail);
	        }

	        return ResponseEntity.ok(publishedTask);
	    } catch (Exception e) {
	        e.printStackTrace();
	        // Customize the error response as needed
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ArrayList<>());
	    }
	}



}