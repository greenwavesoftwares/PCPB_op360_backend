package in.co.greenwave.jobapi.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import in.co.greenwave.jobapi.dao.JobAssignDAO;
import in.co.greenwave.jobapi.dao.JobReviewDAO;
import in.co.greenwave.jobapi.dao.factory.DAOFactory;
import in.co.greenwave.jobapi.model.ApproverCardData;
import in.co.greenwave.jobapi.model.JobDetails;


@RestController
@RequestMapping("/jobs")
public class JobController {
	/**
	 * The "factory" reference is getting Autowired ,with the Bean object of the JobApiConfiguration class
	 */
	@Autowired
	private DAOFactory factory;

	private JobReviewDAO jobreviewdao;
	private JobAssignDAO jobassigndao;

	/**
	 * Here Two Parameters are send from the server side based on which Updation is done in the DB
	 * If the @param action is "rejYes" then "Rejected"  && If the @param action is "apr" then "Approved
	 * @param action
	 * @param @RequestBody JobDetails job
	 * @return job 
	 */
	@Deprecated
	@PutMapping(path="/jobaction/{action}/{tenantId}",consumes = "application/json" )
	public ResponseEntity<Object> updateJobAction(@PathVariable("action") String action, @RequestBody JobDetails job,@PathVariable("tenantId") String tenantId) {
		System.out.println("updateJobAction() called ");
		System.out.println("The JobDetails sent is --> "+job.getJobID()+" "+job.getRemarks());
		jobreviewdao = factory.getJobReviewService();
		try {
			if(action.equalsIgnoreCase("rejYes")) {
				jobreviewdao.updateApprovalDetails(job.getJobID(), job.getRemarks(), "Rejected",tenantId);
			}else if(action.equalsIgnoreCase("apr")) {
				jobreviewdao.updateApprovalDetails(job.getJobID(), job.getRemarks(), "Approved",tenantId);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Object>("UNAUTHORIZED ACCESS.", HttpStatus.UNAUTHORIZED);
		}

		return new ResponseEntity<Object>(job,HttpStatus.OK);
	}

	/**
	 * With respect to an activity reviewer the List<JobDetails> is  returned as a response 
	 * @param reviewer
	 * @return List<JobDetails> jobsToReviewActivities
	 * @author SreepriyaRoy
	 */

	@GetMapping(path="/activityreview/{reviewer}/{tenantId}",produces ="application/json" )
	public ResponseEntity<Object> getActivityReviewData(@PathVariable("reviewer") String reviewer,@PathVariable("tenantId") String tenantId) {
		jobreviewdao = factory.getJobReviewService();
		List<JobDetails> jobsToReviewActivities = new ArrayList<JobDetails>();
		try {
			jobsToReviewActivities = jobreviewdao.pendingForReviewActivities(reviewer,tenantId);

		}
		catch (Exception e)
		{
			e.printStackTrace();
			return new ResponseEntity<Object>("Error occured : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

		}

		//		if (jobsToReview.isEmpty())
		//		{
		//			return new ResponseEntity<Object>("UNAUTHORIZED ACCESS.", HttpStatus.UNAUTHORIZED);
		//		}
		return new ResponseEntity<Object>(jobsToReviewActivities,HttpStatus.OK);

	}
	/**
	 * With respect to job reviewer the List<JobDetails> is  returned as a response 
	 * @param reviewer
	 * @return List<JobDetails> jobsToReview
	 * @author SreepriyaRoy
	 */
	@GetMapping(path="/jobreview/{reviewer}/{tenantId}",produces ="application/json" )
	public ResponseEntity<Object> getJobReviewData(@PathVariable("reviewer") String reviewer,@PathVariable("tenantId") String tenantId) {
		jobreviewdao = factory.getJobReviewService();
		List<JobDetails> jobsToReview = new ArrayList<JobDetails>();
		try {
			jobsToReview = jobreviewdao.pendingForReviewJobs(reviewer,tenantId);

		}
		catch (Exception e)
		{
			e.printStackTrace();
			return new ResponseEntity<Object>("Error occured : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

		}

		//		if (jobsToReview.isEmpty())
		//		{
		//			return new ResponseEntity<Object>("UNAUTHORIZED ACCESS.", HttpStatus.UNAUTHORIZED);
		//		}
		return new ResponseEntity<Object>(jobsToReview,HttpStatus.OK);

	}



	// BarChartModel will be implemented here.....
	/**
	 * With respect to a reviewer the ApproverCardData JSON is returned  as response
	 * @param reviewer
	 * @return ApproverCardData cardData
	 */
	@Deprecated
	@GetMapping(path="/jobreview/cardData/{reviewer}/{tenantId}", produces="application/json")
	public ResponseEntity<Object> getJobReviewCardData(@PathVariable("reviewer") String reviewer,@PathVariable("tenantId") String tenantId) {
		System.out.println("getJobReviewCardData() called "+reviewer);
		jobreviewdao = factory.getJobReviewService();
		ApproverCardData cardData = new ApproverCardData();
		try {
			cardData = jobreviewdao.getApprovedHistory(reviewer,tenantId);
			//System.out.println(cardData.toString());
			// cardData.setNextPendingTasks(jobreviewdao.getNextSevenDayTasks(reviewer));
			// cardData.setPreviousApprovedTasks(jobreviewdao.getLastSevenDayApprovedTasks(reviewer)); //This are actually used in the BarChartModel
			// cardData.setPreviousRejectedTasks(jobreviewdao.getNextSevenDayRejectedTasks(reviewer));
		}
		catch (Exception e) {
			return new ResponseEntity<Object>("Error occured : " + e.getMessage(), HttpStatus.NO_CONTENT);
		}
		if (cardData.equals(null)) {
			return new ResponseEntity<Object>("UNAUTHORIZED ACCESS.", HttpStatus.UNAUTHORIZED);
		}
		return new ResponseEntity<Object>(cardData,HttpStatus.OK);
	}

	//this method returns all the required pdf footer details for a logbook based on jobId and activityId
	@GetMapping(path="/pdfDetails/{tenantId}", produces="application/json")
	public ResponseEntity<Object> getPdfFooterDetails(@RequestParam String jobId,@RequestParam String activityId,@PathVariable("tenantId") String tenantId) {
		System.out.println("getPdfFooterDetails() called "+jobId+" "+activityId);
		jobreviewdao = factory.getJobReviewService();
		Map<String,String> pdfFooterDetails=new HashMap<>();
		try {
			pdfFooterDetails = jobreviewdao.getPdfFooterDetails(jobId,activityId,tenantId);//pdfFooterDetails is a map where the key holds the property key and the value holds the respective key details
		}
		catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Object>("Error occured : " + e.getMessage(), HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Object>(pdfFooterDetails,HttpStatus.OK);
	}

	//get all jobdetails based on a particular tenant
	@GetMapping(path="/allJobs/{tenantId}", produces="application/json")
	public ResponseEntity<Object> getAllJobDetails(@PathVariable("tenantId") String tenantId) {
		System.out.println("getAllJobDetails() called "+tenantId);
		jobreviewdao = factory.getJobReviewService();
		List<JobDetails> jobs=new ArrayList<>();//Object to hold the List<JobDetails> data
		try {
			jobs = jobreviewdao.getAllJobs(tenantId);// injecting the data into the jobs
		}
		catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Object>("Error occured : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<Object>(jobs,HttpStatus.OK);
	}


	//	@PostMapping("/test")
	//	public void test(@RequestBody AssetGroup group)
	//	{
	//		//AssetGroup a = (AssetGroup) group;
	//		System.out.println("Aagaya--> "+group.getCreatedBy());
	//	}

	//Added By Ashok
	@GetMapping(path="/allJobsForCard/{tenantId}", produces="application/json")
	public ResponseEntity<Object> getAllJobDetailsForCard(@PathVariable("tenantId") String tenantId,@RequestParam String fromDate,
			@RequestParam String toDate) {
		//		System.out.println("getAllJobDetails() called "+tenantId);
		jobreviewdao = factory.getJobReviewService();
		List<JobDetails> jobs=new ArrayList<>();
		try {
			jobs = jobreviewdao.getAllJobsForCard(tenantId,fromDate, toDate);
		}
		catch (Exception e) {
			System.out.println("Error : "+e.getMessage());
			e.printStackTrace();
			return new ResponseEntity<Object>("Error occured : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<Object>(jobs,HttpStatus.OK);
	}

	//Added By Ashok added for support360
	@GetMapping(path="/allJobsByTicket/{tenantId}", produces="application/json")
	public List<Map<String, Object>> getJobDetails(@PathVariable("tenantId") String tenantId,@RequestParam String ticketId) {
		jobreviewdao = factory.getJobReviewService();
		return jobreviewdao.getJobDetailsByTicketId(tenantId,ticketId);
	}
	/**
	 * Add or update job based 
	 * @param tenantId , taskId 
	 * @requestbody JobDetails job
	 * @author SreepriyaRoy

	 * */



	/**
	 * Add or update job based 
	 * @param tenantId , taskId 
	 * @requestbody JobDetails job
	 * @author SreepriyaRoy

	 * */
	@PutMapping(path="addUpdateJobDetails/{tenantId}", consumes = "application/json")

	public ResponseEntity<Object> addUpdateJobDetails(@RequestBody JobDetails job,@PathVariable("tenantId")String tenantId ) {
		try {
			System.out.println("Task controller called:"+job.toString());
			jobassigndao = factory.getJobAssignService();
			jobassigndao.addOrUpdateJobDetails(job,tenantId);//Service to add or update jobs
			return new ResponseEntity<Object>(job,HttpStatus.OK);


		}
		catch(Exception ex) {
			ex.printStackTrace();
			return new ResponseEntity<Object>("Error occured : " + ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);

		}

	}

	/**
	 * Add or update job based 
	 * @param tenantId , taskId 
	 * @requestbody JobDetails job
	 * @author SreepriyaRoy

	 * */


	@PutMapping(path="addUpdateJobDetailsList/{tenantId}", consumes = "application/json")
	public ResponseEntity<Object> addUpdateJobDetails(@RequestBody List<JobDetails> jobs,@PathVariable("tenantId")String tenantId ) {
		try {
			System.out.println("Task controller called:"+jobs.toString());
			jobassigndao = factory.getJobAssignService();
			jobassigndao.addOrUpdateListOfJobs(jobs,tenantId);//Service to add or update jobs
			return new ResponseEntity<Object>(jobs,HttpStatus.OK);


		}
		catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Object>("Error occured : " + e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);

		}

	}





	@GetMapping(path = "/sse/user/{tenantId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public SseEmitter streamJobs(@PathVariable String tenantId,@RequestParam String user,@RequestParam String dateRangeJson) throws JsonMappingException, JsonProcessingException {
		
		SseEmitter emitter = new SseEmitter(3600_000L); // 1 hour timeout
		ObjectMapper objectMapper = new ObjectMapper(); // For JSON serialization
		
		    JsonNode root = objectMapper.readTree(dateRangeJson);

		    // Extract eventListDate
//		    int eventMonth = root.get("eventListDate").get("month").asInt();
//		    int eventYear  = root.get("eventListDate").get("year").asInt();

		    // Extract selectedCalendarDate
		    int calendarMonth = root.get("selectedCalendarDate").get("month").asInt();
		    int calendarYear  = root.get("selectedCalendarDate").get("year").asInt();

		ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
		AtomicBoolean isEmitterCompleted = new AtomicBoolean(false); // Flag to track emitter completion

		executor.scheduleAtFixedRate(() -> {
			
			try {
				jobreviewdao = factory.getJobReviewService();
				List<JobDetails> jobData = jobreviewdao.getAllJobsByUser(tenantId,user,calendarMonth,calendarYear);
				String json = objectMapper.writeValueAsString(jobData); // Convert list to JSON
//							            System.out.println("Emitter is working");
				emitter.send(json); // Send the JSON data
			} catch (Exception e) {
				e.printStackTrace();
				// Send a blank array when an error occurs
				try {
					if (!isEmitterCompleted.get()) { // Check if emitter is already completed
						emitter.send("[]"); // Send an empty JSON array
					}
				} catch (Exception sendException) {
					sendException.printStackTrace();
				} finally {
					if (isEmitterCompleted.compareAndSet(false, true)) { // Complete the emitter only once
						emitter.complete(); // Complete the emitter
						executor.shutdown(); // Shut down the executor
					}
				}
			}
		}, 0, 2, TimeUnit.SECONDS);

		emitter.onCompletion(() -> {
			executor.shutdown();
		});

		emitter.onTimeout(() -> {
			// Send a blank array before completing
			try {
				if (!isEmitterCompleted.get()) { // Check if emitter is already completed
					//			                emitter.send("[]"); // Send an empty JSON array
				}
			} catch (Exception sendException) {
				sendException.printStackTrace();//printing the exception that has occurred
			} finally {
				if (isEmitterCompleted.compareAndSet(false, true)) { // Complete the emitter only once
					emitter.complete(); // Complete the emitter
					executor.shutdown(); // Shut down the executor
				}
			}
		});

		// Use onError to handle unexpected issues
		emitter.onError((Throwable throwable) -> {
			System.out.println("An error occurred in the SSE connection: " + throwable.getMessage());
			// Send a blank array on error
			try {
				if (!isEmitterCompleted.get()) { // Check if emitter is already completed
					emitter.send("[]"); // Send an empty JSON array
				}
			} catch (Exception sendException) {
				sendException.printStackTrace();
			} finally {
				if (isEmitterCompleted.compareAndSet(false, true)) { // Complete the emitter only once
					emitter.complete(); // Complete the emitter
					executor.shutdown(); // Shut down the executor
				}
			}
		});

		return emitter;
		
	}
	@PostMapping("/register-new-tenant/{tenantid}")
	public ResponseEntity<String> registernewtenant(@PathVariable("tenantid") String tenantid) {
		try {
			jobreviewdao = factory.getJobReviewService();

			jobreviewdao.registerNewTenant(tenantid);
			return ResponseEntity.ok().body("Tenant registered successfully");
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body("Tenant registration unsuccessful");
		}
	}
	@PutMapping("/updateSequentialStatus/{jobid}/{activityid}/{sequence}/{tenantid}")
	public ResponseEntity<String> updateSequentialStatus(@PathVariable("tenantid") String tenantid,@PathVariable("activityid") String activityid,@PathVariable("jobid") String jobid,@PathVariable("sequence")int sequence) {
		try {
			jobreviewdao = factory.getJobReviewService();

			jobreviewdao.updateSequentialStatus(tenantid, jobid, activityid, sequence);
			return ResponseEntity.ok().body("Job successfully reverted");
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body("Tenant revertion unsuccessful");
		}
	}
	@GetMapping("/{jobid}/{tenantid}")
	public ResponseEntity<Object> fetchJobDetails(@PathVariable("jobid") String jobid,@PathVariable("tenantid") String tenantid){
		try {
			jobreviewdao = factory.getJobReviewService();

			JobDetails job=jobreviewdao.fetchJobDetails(jobid,tenantid);
			return ResponseEntity.ok().body(job);
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Object>("Error occured : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}
	
	
	@GetMapping(path = "/user/{tenantId}")
	public ResponseEntity<Object> getJobsByDateAndUser(@PathVariable("tenantId") String tenantid,@RequestParam String date,@RequestParam String user){
		try {
			jobreviewdao = factory.getJobReviewService();
			System.out.println("Current Date Selected : "+date);
			List<JobDetails> jobs=jobreviewdao.getAllJobsByUserAndDate(tenantid,user,date);
			return ResponseEntity.ok().body(jobs);
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Object>("Error occured : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}
}
