package in.co.greenwave.jobapi.controller;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.fasterxml.jackson.databind.ObjectMapper;

import in.co.greenwave.jobapi.dao.JobAssignDAO;
import in.co.greenwave.jobapi.dao.factory.DAOFactory;
import in.co.greenwave.jobapi.model.CalendarEventModel;
import in.co.greenwave.jobapi.model.JobDetails;
import in.co.greenwave.jobapi.model.PerformerAvailability;

@RestController
@RequestMapping("/jobassign")
public class JobAssignController {
	
	@Autowired
	private DAOFactory factory;

	private JobAssignDAO jobassignDao;
	
	
	/**
	 * @param tenantId
	 * @return a list of distinct groupIds related to all jobs
	 */
	@GetMapping(path="/tags/{tenantId}",produces ="application/json" )
	public ResponseEntity<Object> getTags(@PathVariable("tenantId") String tenantId) {
//		String currentTenant=
		System.out.println("getTags() called ");
		System.out.println("Current Tenant : "+tenantId );
		jobassignDao = factory.getJobAssignService();
		List<String> distinctGroupID = new ArrayList<>();
		try {
			distinctGroupID = jobassignDao.fetchAllGroupId(tenantId);//distinctGroupID will hold the list of groupIds
			
		}
		catch (Exception e)
		{
			e.printStackTrace();//identifying the exception
			return new ResponseEntity<Object>("Error occured : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);//returning the error message if exception occurs

		}

		return new ResponseEntity<Object>(distinctGroupID,HttpStatus.OK);//returns OK if all the data is processed successfully

	}
	
	/**
	 * @param start
	 * @param stop
	 * @param userId
	 * @param tenantId
	 * @return based on given start and stop ,this method returns the reasons for which the user is busy (refer PerformerAvailability class for the reasons   )
	 */
	@GetMapping(path="/performer/availability/{tenantId}",produces ="application/json" )
	public ResponseEntity<List<List<PerformerAvailability>>> getPerformerAvailability(
	        @PathVariable("tenantId") String tenantId,
	        @RequestParam String start,
	        @RequestParam String stop,
	        @RequestParam String activityId,
	        @RequestParam String taskId
	        
			)
	{
		
		
	    System.out.println("getPerformerAvailability() called ");
	    jobassignDao = factory.getJobAssignService();
	    
	    System.out.println("TaskId :"+taskId);

//	    List<List<PerformerAvailability>> responseList = new ArrayList<>();
	    try {
	    	
	    	String resolvedStart = URLDecoder.decode(start, StandardCharsets.UTF_8.toString());
			String resolvedStop = URLDecoder.decode(stop, StandardCharsets.UTF_8.toString());
			String resolvedActivityId = URLDecoder.decode(activityId, StandardCharsets.UTF_8.toString()).trim();
			String resolvedTaskId = URLDecoder.decode(taskId, StandardCharsets.UTF_8.toString()).trim();

	    	
	        // Parse the array of objects
	        
	            // Extract values
	          
	            // Call your DAO method for this performer
	    	List<List<PerformerAvailability>> performerAvailability =
	                    jobassignDao.getPerformerAvailability(resolvedStart, resolvedStop,resolvedActivityId,resolvedTaskId, tenantId);

	     

	        return new ResponseEntity<>(performerAvailability, HttpStatus.OK);

	    } catch (Exception e) {
	        e.printStackTrace();
	        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}

	
	/**
	 * @param start
	 * @param stop
	 * @param assetId
	 * @param tenantId
	 * @return  a list of reasons for which the asset is available  for the given start and stop based on a given assetId ( refer PerformerAvailability class for the reasons )
	 */
	@GetMapping(path="/asset/availability/{tenantId}",produces ="application/json" )
	public ResponseEntity<List<List<PerformerAvailability>>> getAssetAvailability(
	        @PathVariable("tenantId") String tenantId,
	        @RequestParam String start,
	        @RequestParam String stop,
	        @RequestParam String activityId,
	        @RequestParam String taskId
	        
			)
	{

	    System.out.println("getAssetAvailability() called ");
	    jobassignDao = factory.getJobAssignService();

//	    List<List<PerformerAvailability>> responseList = new ArrayList<>();
	    try {
	        // Parse the array of objects
	        
	            // Extract values
	          
	            // Call your DAO method for this performer
	    	List<List<PerformerAvailability>> performerAvailability =
	                    jobassignDao.getAssetAvailability(start, stop,activityId,taskId, tenantId);

	     

	        return new ResponseEntity<>(performerAvailability, HttpStatus.OK);

	    } catch (Exception e) {
	        e.printStackTrace();
	        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}
	

	
	//Based on the current userId all the calendar events( all the assigned jobs ) are returned
	@GetMapping(path="/events/{tenantId}",produces ="application/json" )
	public ResponseEntity<Object> getCalendarEvents(@RequestParam String userId,@PathVariable("tenantId") String tenantId) {
		System.out.println("getCalendarEvents() called => "+userId);
		jobassignDao = factory.getJobAssignService();
		List<CalendarEventModel> eventsList = new ArrayList<>();
		try {
			eventsList = jobassignDao.getExistingJobs(userId,tenantId);// injecting the data into the eventList
			
		}
		catch (Exception e)
		{
			e.printStackTrace();//identifying the exception
			return new ResponseEntity<Object>("Error occured : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

		}

		return new ResponseEntity<Object>(eventsList,HttpStatus.OK);//returns OK if all the data is processed successfully

	}
	
	/**
	 * @param start
	 * @param stop
	 * @param jobname
	 * @param tenantId
	 * @return the jobDetails based on jobName , jobId ,start and stop time
	 */
	@GetMapping(path="/jobdetails/{tenantId}",produces ="application/json" )
	public ResponseEntity<Object> getJobDetails(@RequestParam String jobId,@PathVariable("tenantId") String tenantId) {
		System.out.println("getJobDetails() on Calendar Clicked called ");
		jobassignDao = factory.getJobAssignService();
		JobDetails job=new JobDetails();
		try {
			job = jobassignDao.getJobDetails(tenantId,jobId);// injecting the job Details 
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return new ResponseEntity<Object>("Error occured : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

		}

		return new ResponseEntity<Object>(job,HttpStatus.OK);

	}
	
	//this method deletes a job based on its jobId and activityId
	@DeleteMapping(path="/deletejobs/{tenantId}",produces ="application/json" )
	public ResponseEntity<Object> deleteJobs(@RequestBody JobDetails job,@PathVariable("tenantId") String tenantId) {//will be changed to jobname
		System.out.println("deleteJobs() called ");
		jobassignDao = factory.getJobAssignService();
		int rowCount=0;
		try {
			rowCount = jobassignDao.deleteAssignedJob(job,tenantId);//the rowCount gives the number of row affected 
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return new ResponseEntity<Object>("Error occured : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

		}

		return new ResponseEntity<Object>(rowCount>0?true:false,HttpStatus.OK);//if the rowCount is greater than 0 then database rows have been affected ,hence returning true or false here

	}
	
	//this method returns the count of the 'Not Started' jobs based on assigner
	//Authored by Sreepriya
	@GetMapping("notStartedJobsCount/{tenantid}/{assigner}")
	public ResponseEntity<Object> getNotStartedJobsCount(@PathVariable String tenantid,@PathVariable String assigner){
		try {
			jobassignDao=factory.getJobAssignService();
			 int count=jobassignDao.getNotStartedJobsCount(assigner,tenantid);//the count is returned here
			return new ResponseEntity<Object>(count,HttpStatus.OK);
			
		}catch(Exception e) {
			System.err.println("Error in notStartedJobsCount of JobAssignController:"+e);
			return new ResponseEntity<Object>("UNAUTHORIZED ACCESS",HttpStatus.UNAUTHORIZED);
		}
	}
	
	
	 @GetMapping(path = "/sse/{tenantId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	  public SseEmitter streamEventJobData(@RequestParam String jobId,@PathVariable("tenantId") String tenantId) {
		    SseEmitter emitter = new SseEmitter(3600_000L); // 1 hour timeout
		    ObjectMapper objectMapper = new ObjectMapper(); // For JSON serialization

		    ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
		    AtomicBoolean isEmitterCompleted = new AtomicBoolean(false); // Flag to track emitter completion

		    executor.scheduleAtFixedRate(() -> {
		        try {
		        	jobassignDao = factory.getJobAssignService();
//		    		JobDetails job=new JobDetails();
		    		JobDetails job = jobassignDao.getJobDetails(tenantId,jobId);// injecting the job Details
		            String json = objectMapper.writeValueAsString(job); // Convert list to JSON
//		            System.out.println("Emitter is working");
		            emitter.send(json); // Send the JSON data
		        } catch (Exception e) {
		            e.printStackTrace();
		            // Send a blank array when an error occurs
		            try {
		                if (!isEmitterCompleted.get()) { // Check if emitter is already completed
		                    emitter.send(""); // Send an empty JSON string
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
//		                emitter.send("[]"); // Send an empty JSON array
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
		                emitter.send(""); // Send an empty JSON string
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
	
	



}
