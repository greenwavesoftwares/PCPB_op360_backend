package in.co.greenwave.dashboardapi.controller;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import in.co.greenwave.dashboardapi.dao.DashboardDAO;
import in.co.greenwave.dashboardapi.dao.factory.DAOFactory;
import in.co.greenwave.dashboardapi.model.JobDetails;
import in.co.greenwave.dashboardapi.model.JobwiseCardData;
import in.co.greenwave.dashboardapi.model.Transactiondata;
import in.co.greenwave.dashboardapi.model.UserwiseJobDetails;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    // Autowire DAOFactory to get data access objects
    @Autowired
    private DAOFactory factory;

    // DashboardDAO instance to access dashboard-specific methods
    private DashboardDAO dashBoardService;

    // Method to fetch card-wise data based on tenantId, fromDate, toDate, and groupType
    @GetMapping("/fetchCardWiseData/{tenantId}")
    public ResponseEntity<Map<String, List<JobwiseCardData>>> fetchCardWiseData(
        @PathVariable("tenantId") String tenantId,
        @RequestParam String fromDate,
        @RequestParam String toDate,
        @RequestParam String groupType) {


        // Get the DashBoardService instance from the factory
        dashBoardService = factory.getDashBoardService();

        // Fetch the card data from the service
        Map<String, List<JobwiseCardData>> responseData = dashBoardService.fetchAllCardData(tenantId, fromDate, toDate, groupType);

        // Filter out null keys
        Map<String, List<JobwiseCardData>> filteredResponseData = responseData.entrySet()
            .stream()
            .filter(entry -> entry.getKey() != null)
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        // Return the response wrapped in a ResponseEntity with an HTTP 200 OK status
        return ResponseEntity.ok(filteredResponseData);
    }

    // Method to fetch transaction details by tenantId, jobId, and activityId
    @GetMapping("/fetchTransaction/{tenantId}")
    public List<Transactiondata> fetchTransactionDetails(@PathVariable("tenantId") String tenantId, @RequestParam String jobId, @RequestParam String activityId) {
        // Get the DashBoardService instance from the factory
        dashBoardService = factory.getDashBoardService();

        // Fetch and return transaction details from the service
        return dashBoardService.fetchFromActivityId(tenantId, jobId, activityId);
    }
    
   
    // Method to fetch job details based on tenantId and a hardcoded date range
    @GetMapping("/fetch/{tenantId}")
    public List<JobDetails> fetchJobDetails(@PathVariable("tenantId") String tenantId) {
        // Hardcoded date range for this example
        String fromDate = "2024-05-14";
        String toDate = "2024-05-15";

        // Get the DashBoardService instance from the factory
        dashBoardService = factory.getDashBoardService();

        // Fetch and return job details from the service
        return dashBoardService.fetchAllJobDetails(tenantId, fromDate, toDate);
    }

    // Method to fetch task-wise job details based on tenantId, fromDate, and toDate
    @GetMapping("/taskWiseJob/{tenantId}")
    public ResponseEntity<Map<String, LinkedList<UserwiseJobDetails>>> fetchTaskWiseJobDetails(
        @PathVariable("tenantId") String tenantId,
        @RequestParam String fromDate,
        @RequestParam String toDate) {

        // Log to indicate the method was called
        System.out.println("taskWiseJobcalled : ");

        // Get the DashBoardService instance from the factory
        dashBoardService = factory.getDashBoardService();

        // Fetch task-wise job details from the service
        Map<String, LinkedList<UserwiseJobDetails>> response = dashBoardService.fetchTaskWiseJob(tenantId, fromDate, toDate);

        // Filter out null keys
        Map<String, LinkedList<UserwiseJobDetails>> filteredResponse = response.entrySet()
            .stream()
            .filter(entry -> entry.getKey() != null)
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        // Return the filtered response wrapped in a ResponseEntity with an HTTP 200 OK status
        return ResponseEntity.ok(filteredResponse);
    }

    // Method to fetch tag-wise job details by tenantId, fromDate, and toDate
    @GetMapping("/tagWiseJob/{tenantId}")
    public ResponseEntity<Map<String, LinkedList<UserwiseJobDetails>>> fetchTagWiseJobDetails(
        @PathVariable("tenantId") String tenantId,
        @RequestParam String fromDate,
        @RequestParam String toDate) {

        // Get the DashBoardService instance from the factory
        dashBoardService = factory.getDashBoardService();

        // Fetch tag-wise job details from the service
        Map<String, LinkedList<UserwiseJobDetails>> response = dashBoardService.fetchTagWiseJob(tenantId, fromDate, toDate);

        // Filter out null keys
        Map<String, LinkedList<UserwiseJobDetails>> filteredResponse = response.entrySet()
            .stream()
            .filter(entry -> entry.getKey() != null)
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        // Return the filtered response wrapped in a ResponseEntity with an HTTP 200 OK status
        return ResponseEntity.ok(filteredResponse);
    }

    // Method to fetch user-wise job details by tenantId, fromDate, and toDate
    @GetMapping("/userWiseJob/{tenantId}")
    public ResponseEntity<Map<String, LinkedList<UserwiseJobDetails>>> fetchUserWiseJobDetails(
        @PathVariable("tenantId") String tenantId,
        @RequestParam String fromDate,
        @RequestParam String toDate) {

        // Get the DashBoardService instance from the factory
        dashBoardService = factory.getDashBoardService();

        // Fetch user-wise job details from the service
        Map<String, LinkedList<UserwiseJobDetails>> response = dashBoardService.fetchUserWiseJob(tenantId, fromDate, toDate);

        // Filter out null keys
        Map<String, LinkedList<UserwiseJobDetails>> filteredResponse = response.entrySet()
            .stream()
            .filter(entry -> entry.getKey() != null)
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        // Return the filtered response wrapped in a ResponseEntity with an HTTP 200 OK status
        return ResponseEntity.ok(filteredResponse);
    }

    // Method to fetch asset-wise job details by tenantId, fromDate, and toDate
    @GetMapping("/assetWiseJob/{tenantId}")
    public ResponseEntity<Map<String, LinkedList<UserwiseJobDetails>>> fetchAssetWiseJobDetails(
        @PathVariable("tenantId") String tenantId,
        @RequestParam String fromDate,
        @RequestParam String toDate) {

        // Get the DashBoardService instance from the factory
        dashBoardService = factory.getDashBoardService();

        // Fetch asset-wise job details from the service
        Map<String, LinkedList<UserwiseJobDetails>> response = dashBoardService.fetchAssetWiseJob(tenantId, fromDate, toDate);

        // Filter out null keys
        Map<String, LinkedList<UserwiseJobDetails>> filteredResponse = response.entrySet()
            .stream()
            .filter(entry -> entry.getKey() != null)
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        // Return the filtered response wrapped in a ResponseEntity with an HTTP 200 OK status
        return ResponseEntity.ok(filteredResponse);
    }

    // Method to fetch logbooks within a specific date range by tenantId, fromDate, and toDate
    @GetMapping("/logbooks/{tenantId}")
    public List<String> fetchLogbooksByDateRange(
        @PathVariable("tenantId") String tenantId,
        @RequestParam String fromDate,
        @RequestParam String toDate) {

        // Get the DashBoardService instance from the factory
        dashBoardService = factory.getDashBoardService();

        // Fetch and return logbooks within the date range
        return dashBoardService.fetchLogbooksByDateRange(tenantId, fromDate, toDate);
    }

    // Method to fetch assets within a specific date range by tenantId, fromDate, and toDate
    @GetMapping("/assests/{tenantId}")
    public List<String> fetchAssetByDateRange(
        @PathVariable("tenantId") String tenantId,
        @RequestParam String fromDate,
        @RequestParam String toDate) {

        // Get the DashBoardService instance from the factory
        dashBoardService = factory.getDashBoardService();

        // Fetch and return assets within the date range
        return dashBoardService.fetchAssetsByDateRange(tenantId, fromDate, toDate);
    }

    // Method to fetch job status by tenantId and ticketNo
    @GetMapping("/jobStatus/{tenantId}")
    public ResponseEntity<List<Map<String, Object>>> getTicketDetailsByPlantIdAndTicketNo(
        @PathVariable("tenantId") String tenantId,
        @RequestParam String ticketNo) {

        // Get the DashBoardService instance from the factory
        dashBoardService = factory.getDashBoardService();

        // Fetch job status from the service
        List<Map<String, Object>> response = dashBoardService.fetchJobStatusByTicketNo(tenantId, ticketNo);

        // Return the response wrapped in a ResponseEntity with an HTTP 200 OK status
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/register-new-tenant/{tenantid}")
    public ResponseEntity<String> registernewtenant(@PathVariable("tenantid") String tenantid) {
    	try {
//    		jobreviewdao = factory.getJobReviewService();
            dashBoardService = factory.getDashBoardService();
        	
            dashBoardService.registerNewTenant(tenantid);
        	return ResponseEntity.ok().body("Tenant registered successfully");
    	}catch(Exception e) {
    		e.printStackTrace();
    		return ResponseEntity.badRequest().body("Tenant registration unsuccessful");
    	}
    }

}
