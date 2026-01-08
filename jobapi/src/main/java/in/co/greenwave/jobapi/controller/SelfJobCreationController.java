package in.co.greenwave.jobapi.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.co.greenwave.jobapi.dao.ScanJobDAO;
import in.co.greenwave.jobapi.dao.factory.DAOFactory;
import in.co.greenwave.jobapi.model.AssetModel;
import in.co.greenwave.jobapi.model.AutoJob;

@RestController
@RequestMapping("/selfJob")
public class SelfJobCreationController {
	
	
	
	@Autowired
	private DAOFactory factory;

	private ScanJobDAO scanjobdao;

	/**
	 * @param assetId
	 * @param tenantId
	 * @return assetModel object which contains information of the logbooks for a given assetId
	 */
	@GetMapping(path="/assetlogbooks/{assetId}/{tenantId}",produces ="application/json" )
	public ResponseEntity<Object> getAssetLogbooks(@PathVariable("assetId") String assetId,@PathVariable("tenantId") String tenantId) {
		System.out.println("getAssetLogbooks() called "+assetId);
		scanjobdao = factory.getScanJobService(); // fetching the object from getScanJobService()  to access the service
		
		AssetModel model=new AssetModel();//
		
		try {
			model = scanjobdao.getLogbooksofAsset(assetId,tenantId); // injecting the processed data into the model
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return new ResponseEntity<Object>("Error occured : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR); // identifying the exception and returning the error message for the same 

		}

		return new ResponseEntity<Object>(model,HttpStatus.OK);// returning response OK , if everything works fine along with the model data

	}
	
	
	/**
	 * @param assetId
	 * @param tenantId
	 * @returns the auto job information which is still in pending stage i.e have started the job but not yet finished for a given asset
	 */
	@GetMapping(path="/pendingJobs/{assetId}/{tenantId}",produces ="application/json" )
	public ResponseEntity<Object> getPendingJobInfoforAssetId(@PathVariable("assetId") String assetId,@PathVariable("tenantId") String tenantId) {
		System.out.println("getPendingJobInfoforAssetId() called "+assetId);
		scanjobdao = factory.getScanJobService();
		AutoJob model=new AutoJob();// AutoJob object to store information for the pending Job
		try {
			model = scanjobdao.getPendingJobInfoforAssetId(assetId,tenantId);// injecting the autoJob information into the model
			
		}
		catch (Exception e)
		{
			e.printStackTrace();//identifying where the exception has occurred
			return new ResponseEntity<Object>("Error occured : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR); //returning the error message

		}
		System.out.println("Returned AutoJob => "+model);
		return new ResponseEntity<Object>(model,HttpStatus.OK);// response is send OK when the data is processed successfully

	}
	
	
	/**
	 * @param autojob
	 * @param tenantId
	 * @return boolean value based on whether the time for jobId is updated or not
	 */
	@PutMapping(path="/updateJob/{tenantId}")
	public ResponseEntity<Object> updatePendingJob(@RequestBody AutoJob autojob,@PathVariable("tenantId") String tenantId) {
		System.out.println("updatePendingJob() called "+autojob);
//		JSONObject jsonObject = new JSONObject(details);
		String currentAssetId=autojob.getAssetid();
		String currentJobId=autojob.getJobid();
		String currentActivityId=autojob.getActivityid();
		System.out.println("JobId :"+currentJobId+" ActivityId : "+currentActivityId+"AssetId : "+currentAssetId);
		scanjobdao = factory.getScanJobService();
		boolean success=false;
		try {
			success = scanjobdao.updateJobEndTime(currentJobId,currentActivityId,currentAssetId,tenantId,autojob.getActivityendtime());// updating the success value if the endTime of the job is updated or not
			
		}
		catch (Exception e)
		{
			e.printStackTrace();//identifying where the exception has occurred
			return new ResponseEntity<Object>("Error occured : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);//returning the error message

		}

		return new ResponseEntity<Object>(success,HttpStatus.OK);// response is send OK when the data is processed successfully

	}
	
	/**
	 * @param autojob
	 * @param tenantId
	 * @return returns true or false based on whether a new AutoJob is created 
	 */
	@PostMapping(path="/{tenantId}")
	public ResponseEntity<Object> initializeNewAutoJob(@RequestBody AutoJob autojob,@PathVariable("tenantId") String tenantId) {
		System.out.println("initializeNewAutoJob() called "+autojob);
		boolean success=false;
		scanjobdao = factory.getScanJobService();
		try {
			success = scanjobdao.initialteNewAutoJob(autojob,tenantId);// it creates a new AutoJob and returns a boolean value if a new AutoJob is created or not
			System.out.println("Success ? "+success );
			
		}
		catch (Exception e)
		{
			System.out.println("Error occurred here : "+e);//identifying where the exception has occurred
			return new ResponseEntity<Object>("Error occured : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

		}
		System.out.println("Success ? "+success );
		return new ResponseEntity<Object>(success,HttpStatus.OK);// response is send OK when the data is processed successfully

	}
	
	@GetMapping(path="/pendingJobs/{formname}/{formid}/{version}/{user}/{tenantId}",produces ="application/json" )
	public ResponseEntity<Object> getPendingJobInfoforLogbookForm(@PathVariable("formname")String formname, @PathVariable("formid")int formid,@PathVariable("version") int version,@PathVariable("user") String user,@PathVariable("tenantId") String tenantId) {
		System.out.println("getPendingJobInfoforLogbookForm() called "+"FormName : "+formname+"FormId : "+formid+"Version : "+version+"user : "+user);
		scanjobdao = factory.getScanJobService();
		AutoJob job=new AutoJob();
		try {
			job = scanjobdao.getPendingJobInfoforLogbook(formname, formid, version, user,tenantId);
			
		}
		catch (Exception e)
		{
			e.printStackTrace();//identifying where the exception has occurred
			return new ResponseEntity<Object>("Error occured : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

		}

		return new ResponseEntity<Object>(job,HttpStatus.OK);// response is send OK when the data is processed successfully

	}
	
	/**
	 * @param userId
	 * @param tenantId
	 * @return an List<AutoJob> that are required to be approved by the approver here in this case userId in the parameter
	 */
	@GetMapping(path="/pendingApprovals/{userId}/{tenantId}",produces ="application/json" )
	public ResponseEntity<Object> getPendingApprovalsForSelfJob(@PathVariable("userId") String userId,@PathVariable("tenantId") String tenantId) {
		System.out.println("getPendingApprovalsForSelfJob() called "+"UserId : "+userId+"tenantId : "+tenantId);
		scanjobdao = factory.getScanJobService();
		List<AutoJob> jobs=new ArrayList<>();//List<AutoJob> that  are going to store all the pending approvals of AutoJob
		try {
			jobs = scanjobdao.getPendingforApprovalSelfAssignedJobs(userId,tenantId);//injecting the data into the jobs object
			
		}
		catch (Exception e)
		{
			e.printStackTrace();//identifying where the exception has occurred
			return new ResponseEntity<Object>("Error occured : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

		}

		return new ResponseEntity<Object>(jobs,HttpStatus.OK);// response is send OK when the data is processed successfully

	}
	
	
	/**
	 * @param tenantId
	 * @param job
	 * @returns  boolean value based on the job status is updated or not
	 */
	@PutMapping(path="/review/{tenantId}",produces ="application/json" )
	public ResponseEntity<Object> reviewForSelfAssignedJob(@PathVariable("tenantId") String tenantId,@RequestBody AutoJob job) {
		System.out.println("reviewForSelfAssignedJob() called "+"AutoJob : "+job);
		scanjobdao = factory.getScanJobService();
		boolean success=false;
		try {
			
			success = scanjobdao.updateStatusForSelfAssignedJob(job.getJobid(),job.getActivityid(),job.getAssetid(),job.getStatus(),job.getRemarks(),tenantId,job.getReviewstarttime());// injecting  the boolean value based on whether the status information is updated or not
			
		}
		catch (Exception e)
		{
			e.printStackTrace();//identifying the exception
			return new ResponseEntity<Object>("Error occured : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR); //returning the error message in case any exception occurs

		}

		return new ResponseEntity<Object>(success,HttpStatus.OK);// response is send OK when the data is processed successfully

	}
	
	
	@GetMapping(path="/{tenantId}",produces ="application/json" )
	public ResponseEntity<Object> getAllSelfJobs(@PathVariable("tenantId") String tenantId) {
		System.out.println("getAllSelfJobs() called "+"tenant : "+tenantId);
		scanjobdao = factory.getScanJobService();
		List<AutoJob> allAutojobs=new ArrayList<>();
		try {
			
			allAutojobs = scanjobdao.getAllSelfJobs(tenantId);// injecting  the boolean value based on whether the status information is updated or not
			
		}
		catch (Exception e)
		{
			e.printStackTrace();//identifying the exception
			return new ResponseEntity<Object>("Error occured : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR); //returning the error message in case any exception occurs

		}

		return new ResponseEntity<Object>(allAutojobs,HttpStatus.OK);// response is send OK when the data is processed successfully

	}
	
	
	
	
	
	
	

}
