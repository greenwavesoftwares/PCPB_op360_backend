package in.co.greenwave.jobapi.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.co.greenwave.jobapi.dao.JobAssignDAO;
import in.co.greenwave.jobapi.dao.JobReviewDAO;
import in.co.greenwave.jobapi.dao.factory.DAOFactory;
import in.co.greenwave.jobapi.model.JobDetails;

@RestController
@RequestMapping("/adhoc")
public class AdhocJobController {
	
	
	@Autowired
	private DAOFactory factory;

//	private JobReviewDAO jobreviewdao;
	private JobAssignDAO jobassigndao;
	
	@GetMapping(path="/allJobs/{tenantId}",produces ="application/json" )
	public ResponseEntity<Object> getAllAdhocJobsByTenant(@PathVariable("tenantId") String tenantId) {

		System.out.println("getAllAdhocJobsByTenant() called ");
		System.out.println("Current Tenant : "+tenantId );
		jobassigndao = factory.getJobAssignService();
		List<JobDetails> adhocJobs = new ArrayList<>();
		try {
			adhocJobs = jobassigndao.fetchAllAdhocJobs(tenantId);			
		}
		catch (Exception e)
		{
			e.printStackTrace();//identifying the exception
			return new ResponseEntity<Object>("Error occured : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);//returning the error message if exception occurs
		}
		return new ResponseEntity<Object>(adhocJobs,HttpStatus.OK);//returns OK if all the data is processed successfully

	}
	
	
	
	@GetMapping(path="/allJobs/{assigner}/{tenantId}",produces ="application/json" )
	public ResponseEntity<Object> getAllAdhocJobsByAssigner(@PathVariable("tenantId") String tenantId ,@PathVariable("assigner") String assigner) {

		System.out.println("getAllAdhocJobsByAssigner() called ");
		System.out.println("Current Tenant : "+tenantId );
		jobassigndao = factory.getJobAssignService();
		List<JobDetails> adhocJobs = new ArrayList<>();
		try {
			adhocJobs = jobassigndao.fetchAllAdhocJobsByAssigner(tenantId, assigner);			
		}
		catch (Exception e)
		{
			e.printStackTrace();//identifying the exception
			return new ResponseEntity<Object>("Error occured : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);//returning the error message if exception occurs
		}
		return new ResponseEntity<Object>(adhocJobs,HttpStatus.OK);//returns OK if all the data is processed successfully

	}
	
	

}
