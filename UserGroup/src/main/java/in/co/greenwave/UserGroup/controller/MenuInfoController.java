package in.co.greenwave.UserGroup.controller;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import in.co.greenwave.UserGroup.dao.MenuInfoDAO;
import io.jsonwebtoken.io.IOException;
@RestController
@RequestMapping("menu")
public class MenuInfoController {
	@Autowired 
	private MenuInfoDAO service;
	/**
	 * @param userid
	 * @param tenantId
	 * @return details to be displayed in menubar
	 */
	@GetMapping(path = "/{userid}/{tenantId}", produces = "application/json")
	public ResponseEntity<Object> getNavigationDetails(@PathVariable("userid") String userid,@PathVariable("tenantId") String tenantId)
	{
		try
		{


			return new ResponseEntity<Object>(service.getNavigationDetails(userid,tenantId),HttpStatus.OK);
		}
		catch (Exception e) {
			System.out.println("Exception ");
			e.printStackTrace();
			return new ResponseEntity<Object>("Error occured : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

		}

	}
	@GetMapping(path = "/fetchDetailsForMenusBadges/{userid}/{groupDetails}/{tenantId}", produces = "application/json")
	public ResponseEntity<Object> fetchDetailsForMenusBadges(@PathVariable("userid") String userid,@PathVariable("groupDetails") String groupDetails,@PathVariable("tenantId") String tenantId)
	{
		try
		{


			return new ResponseEntity<Object>(service.fetchDetailsForMenusBadges(userid,groupDetails,tenantId),HttpStatus.OK);
		}
		catch (Exception e) {
			System.out.println("Exception ");
			e.printStackTrace();
			return new ResponseEntity<Object>("Error occured : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

		}

	}
	@GetMapping(path = "/fetchDashboardDetails/{tenantId}", produces = "application/json")
	public ResponseEntity<Object> fetchDashboardDetails(@PathVariable("tenantId") String tenantId)
	{
		try
		{


			return new ResponseEntity<Object>(service.getDashboardLogbook(tenantId),HttpStatus.OK);
		}
		catch (Exception e) {
			System.out.println("Exception ");
			e.printStackTrace();
			return new ResponseEntity<Object>("Error occured : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

		}

	}
	

	/**
	 * Handles Server-Sent Events (SSE) stream for sending real-time data to the client.
	 * The client connects to this endpoint to receive periodic updates (every 20 seconds).
	 *
	 * @param user     The user ID (passed in the URL).
	 * @param groups   The user groups (passed in the URL).
	 * @param tenantid The tenant ID (passed in the URL).
	 * @return SseEmitter for streaming data to the client.
	 * @throws IOException
	 * @throws InterruptedException
	 */
	@GetMapping(value = "/stream/{user}/{groups}/{tenantid}")
	public SseEmitter streamSSE(@PathVariable String user, 
	                            @PathVariable String groups, 
	                            @PathVariable String tenantid) throws IOException, InterruptedException {
	    
	    // Create an SseEmitter with no timeout (0L means it won't timeout on its own)
	    SseEmitter emitter = new SseEmitter(0L);

	    // Create a scheduled executor to periodically send data to the client
	    ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

	    // Schedule a task that runs every 10 seconds to fetch and send updated data
	    ScheduledFuture<?> task = scheduler.scheduleAtFixedRate(() -> {
	        try {
	            // Fetch data and send it to the client via SSE
	            emitter.send(service.fetchDetailsForMenusBadges(user, groups, tenantid));
	        } catch (Exception e) {
	            // In case of error, complete the emitter with an error
	            emitter.completeWithError(e);
	        }
	    }, 0, 10, TimeUnit.SECONDS); // Initial delay = 0, period = 20 seconds

	    // Cleanup when the SSE connection is completed normally
	    emitter.onCompletion(() -> {
	        task.cancel(true);          // Stop the scheduled task
	        scheduler.shutdown();       // Shut down the scheduler
	    });

	    // Cleanup when the connection times out
	    emitter.onTimeout(() -> {
	        task.cancel(true);          // Cancel task on timeout
	        scheduler.shutdown();       // Shutdown executor
	        emitter.complete();         // Complete the emitter
	    });

	    // Cleanup on error during connection
	    emitter.onError((throwable) -> {
	        task.cancel(true);          // Cancel task on error
	        scheduler.shutdown();       // Shutdown executor
	    });

	    return emitter; // Return the emitter so the client can receive updates
	}
}
