package com.random.number.controller;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.random.number.models.ActivityDetails;
import com.random.number.models.JobDetails;
import com.random.number.models.TaskDetail;
import com.random.number.service.JobDetailsService;
@RestController
@RequestMapping("/jobs")
@CrossOrigin(origins="*")
public class JobController {
	DecimalFormat decimalFormat = new DecimalFormat("#.##");
	private final RestTemplate restTemplate = new RestTemplate(); // RestTemplate instance
	private final ObjectMapper objectMapper = new ObjectMapper(); // Jackson ObjectMapper

	@Autowired
	private JobDetailsService jobDetailsService;
	
	//This is the endpoint where the logbook data will be saved from selfJob
		@GetMapping("/saveJob")
		public  Map<String, String> saveJob(
				@RequestParam String customerName,
				@RequestParam String description,
				@RequestParam String projectLeader,
				@RequestParam String activity,
				@RequestParam String hseQuestionnaire,
				@RequestParam String insurance,
				@RequestParam String poNo,
				@RequestParam String codeOfPractice,
				@RequestParam String safetyInduction,
				@RequestParam String physicalAndMedicalAptitude,
				@RequestParam String safetyAudit,
				@RequestParam String jobStartTime,
				@RequestParam String assigner) {
	
			// Step 1: Get the token from the first API (http://localhost:8097/auth/signin)
			
			boolean isInserted = jobDetailsService.insertContractorOnboardData(
					customerName, description, projectLeader, activity,
					hseQuestionnaire, insurance, poNo, codeOfPractice,
					safetyInduction, physicalAndMedicalAptitude, safetyAudit, jobStartTime
					);

			if (isInserted) {
				// Return a success response with JobDetails (Assuming JobDetails is some valid response object)
				System.out.println("Inserted");
			} else {
Map<String, String> response = new HashMap<>();
			    
			    // Set all fields as empty except for _13_3 (message)
//			    response.put("_2_3", "");
//			    response.put("_3_3", "");
//			    response.put("_4_3", "");
//			    response.put("_5_3", "");
//			    response.put("_6_3", "");
//			    response.put("_7_3", "");
//			    response.put("_8_3", "");
//			    response.put("_9_3", "");
//			    response.put("_10_3", "");
//			    response.put("_11_3", "");
//			    response.put("_12_3", "");
			    response.put("_13_3", "Insert Fail");
			    return response;
			}
			
			String token = getToken("info@greenwave.co.in", "user_123", "Vesuvius_Kolkata");

			if (token == null) {
				Map<String, String> response = new HashMap<>();
			    
			    // Set all fields as empty except for _13_3 (message)
//			    response.put("_2_3", "");
//			    response.put("_3_3", "");
//			    response.put("_4_3", "");
//			    response.put("_5_3", "");
//			    response.put("_6_3", "");
//			    response.put("_7_3", "");
//			    response.put("_8_3", "");
//			    response.put("_9_3", "");
//			    response.put("_10_3", "");
//			    response.put("_11_3", "");
//			    response.put("_12_3", "");
			    response.put("_13_3", "Database Error");
			}
			
			
			
			

			// Step 2: Call the second API (http://localhost:8095/task/test) using the Bearer token
			String jobResponse = saveJobDetailsInDb(token, customerName,
					  description,
					  projectLeader,
					  activity,
					  hseQuestionnaire,
					  insurance,
					  poNo,
					  codeOfPractice,
					  safetyInduction,
					  physicalAndMedicalAptitude,
					  safetyAudit,
					  jobStartTime,
					  assigner);

			// Return the response from the second API
			if(jobResponse.equalsIgnoreCase("Save Successfully")) {
			Map<String, String> response = new HashMap<>();
		    
		    // Set all fields as empty except for _13_3 (message)
		    response.put("_2_3", "");
		    response.put("_3_3", "");
		    response.put("_4_3", "");
		    response.put("_5_3", "");
		    response.put("_6_3", "");
		    response.put("_7_3", "");
		    response.put("_8_3", "");
		    response.put("_9_3", "");
		    response.put("_10_3", "");
		    response.put("_11_3", "");
		    response.put("_12_3", "");
		    response.put("_13_3", "");
		    response.put("_14_2", "Form saved successfully");

//		    return response;
			return response;
			}
			else {
Map<String, String> response = new HashMap<>();
			    
			    // Set all fields as empty except for _13_3 (message)
//			    response.put("_2_3", "");
//			    response.put("_3_3", "");
//			    response.put("_4_3", "");
//			    response.put("_5_3", "");
//			    response.put("_6_3", "");
//			    response.put("_7_3", "");
//			    response.put("_8_3", "");
//			    response.put("_9_3", "");
//			    response.put("_10_3", "");
//			    response.put("_11_3", "");
//			    response.put("_12_3", "");
			    response.put("_13_3", "Error in Job Assign");
			    return response;
			}
		}

		
		
		// Method to get the token from the first API
		private String getToken(String username, String password, String tenantId) {
			String apiUrl = "https://op360.greenware.app:8443/authservice_op360/auth/signin";

			// Create the request body for the authentication API
			String requestBody = "{\"username\":\"" + username + "\","
					+ "\"password\":\"" + password + "\","
					+ "\"tenantId\":\"" + tenantId + "\"}";

			// Define the headers (Content-Type: application/json)
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);

			// Create the HttpEntity with the request body and headers
			HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

			try {
				// Send the POST request to the authentication API
				ResponseEntity<String> response = restTemplate.exchange(
						apiUrl, HttpMethod.POST, entity, String.class);

				// Parse the response to extract the token
				JsonNode responseJson = objectMapper.readTree(response.getBody());
				return responseJson.get("token").asText();
			} catch (Exception e) {
				System.out.println("Error getting token: " + e.getMessage());
				return null;
			}
		}

		
		
		private String saveJobDetailsInDb(String token,
				 String customerName,
				 String description,
				 String projectLeader,
				 String activity,
				 String hseQuestionnaire,
				 String insurance,
				 String poNo,
				 String codeOfPractice,
				 String safetyInduction,
				 String physicalAndMedicalAptitude,
				 String safetyAudit,
				 String jobStartTime,
				 String assigner) {


			System.out.println("jobName : "+description+" Assigner : "+assigner+" approver : "+projectLeader+" scheduledJobStartTime : "+jobStartTime);
			// Parse scheduledJobStartTime
			LocalDateTime parsedStartTime = parseDate(jobStartTime);

			// Load hardcoded JobDetails
			JobDetails jobDetails = getHardcodedJobDetails(customerName);//pass the performer

			// Update only the dynamic request parameters
			String jobId = "J" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmssSSS"));

			jobDetails.setJobID(jobId);
			jobDetails.setJobName(description);
			jobDetails.setAssigner(assigner);
			jobDetails.setApprover(projectLeader);
			jobDetails.setScheduledJobStartTime(convertToDate( parsedStartTime));

			// Fetch and update activityList
			List<ActivityDetails> updatedActivityList = fetchCurrentActivityList(
					jobDetails.getTask().getActivityList(), 
					parsedStartTime, 
					String.valueOf(jobDetails.getWeekdays()) // Converts Integer to String
					);
			jobDetails.getTask().setActivityList(updatedActivityList);
			
			
			System.out.println("Final Activity List : "+jobDetails.getTask().getActivityList());
			System.out.println("Last Activity Endtime : "+jobDetails.getTask().getActivityList().get(jobDetails.getTask().getActivityList().size()-1).getScheduledActivityEndTime());
			jobDetails.setScheduledJobEndTime(jobDetails.getTask().getActivityList().get(jobDetails.getTask().getActivityList().size()-1).getScheduledActivityEndTime());


//			 String apiUrl = "http://localhost:8099/jobs/addUpdateJobDetails/ITCITDOP360";
			String apiUrl = "https://op360.greenware.app:8443/jobapi_op360/jobs/addUpdateJobDetails/Vesuvius_Kolkata";
			    return updateJobDetails(apiUrl, token, jobDetails);
//			return "Save Successfully";
		}
		
		private String updateJobDetails(String apiUrl, String token, JobDetails jobDetails) {
		    // Initialize RestTemplate
		    RestTemplate restTemplate = new RestTemplate();

		    // Set the Bearer token in the HTTP headers
		    HttpHeaders headers = new HttpHeaders();
		    headers.setContentType(MediaType.APPLICATION_JSON);
		    headers.setBearerAuth(token); // Set the Bearer token for authorization

		    // Create the HTTP request with headers and the jobDetails body
		    HttpEntity<JobDetails> entity = new HttpEntity<>(jobDetails, headers);

		    // Make the PUT request to the API
		    ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.PUT, entity, String.class);

		    // Check if the request was successful
		    if (response.getStatusCode() == HttpStatus.OK) {
		        return "Save Successfully";
		    } else {
		        return "Error: " + response.getStatusCode();
		    }
		}

		@GetMapping("/getJobDetails")
		private ResponseEntity<JobDetails> getJobDetails(
				@RequestParam String customerName,
				@RequestParam String description,
				@RequestParam String projectLeader,
				@RequestParam String activity,
				@RequestParam String hseQuestionnaire,
				@RequestParam String insurance,
				@RequestParam String poNo,
				@RequestParam String codeOfPractice,
				@RequestParam String safetyInduction,
				@RequestParam String physicalAndMedicalAptitude,
				@RequestParam String safetyAudit,
				@RequestParam String jobStartTime,
				@RequestParam String assigner) {


			System.out.println("jobName : "+description+" Assigner : "+assigner+" approver : "+projectLeader+" scheduledJobStartTime : "+jobStartTime);
			// Parse scheduledJobStartTime
			LocalDateTime parsedStartTime = parseDate(jobStartTime);

			// Load hardcoded JobDetails
			JobDetails jobDetails = getHardcodedJobDetails(customerName);//pass the performer

			// Update only the dynamic request parameters
			jobDetails.setJobName(description);
			jobDetails.setAssigner(assigner);
			jobDetails.setApprover(projectLeader);
			jobDetails.setScheduledJobStartTime(convertToDate( parsedStartTime));

			// Fetch and update activityList
			List<ActivityDetails> updatedActivityList = fetchCurrentActivityList(
					jobDetails.getTask().getActivityList(), 
					parsedStartTime, 
					String.valueOf(jobDetails.getWeekdays()) // Converts Integer to String
					);
			jobDetails.getTask().setActivityList(updatedActivityList);
			
			System.out.println("Last Activity Endtime : "+jobDetails.getTask().getActivityList().get(jobDetails.getTask().getActivityList().size()-1).getScheduledActivityEndTime());
			jobDetails.setScheduledJobEndTime(jobDetails.getTask().getActivityList().get(jobDetails.getTask().getActivityList().size()-1).getScheduledActivityEndTime());

			return ResponseEntity.ok(jobDetails);
		}
		private Date convertToDate(LocalDateTime localDateTime) {
			return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
		}
		
		
		private List<ActivityDetails> fetchCurrentActivityList(List<ActivityDetails> activityList, LocalDateTime fromDate, String weekdays) {
		    // Sort activities by sequence
		    activityList.sort(Comparator.comparingInt(ActivityDetails::getSequence));

		    LocalDateTime currentStartTime = fromDate;
		    Map<Integer, LocalDateTime> sequenceStartTimes = new HashMap<>();
		    Map<Integer, LocalDateTime> sequenceMaxEndTimes = new HashMap<>();

		    List<ActivityDetails> newList = new ArrayList<>();
		    int previousSequence = -1;

		    for (ActivityDetails obj : activityList) {
		        LocalDateTime startTime;
		        LocalDateTime endTime;

		        int bufferDuration = obj.getBuffer();
		        int activityDuration = obj.getDuration();
		        int sequence = obj.getSequence();

		        // 🔹 If a new sequence starts, update the current start time from the max end time of the previous sequence
		        if (sequence != previousSequence) {
		            currentStartTime = sequenceMaxEndTimes.getOrDefault(previousSequence, currentStartTime);
		            System.out.println("🔄 Updating currentStartTime for Sequence " + sequence + " -> " + currentStartTime);
		        }

		        // Store the reference start time for this sequence
		        LocalDateTime finalStartTime = currentStartTime;

		        // Ensure the sequence gets a consistent start time
		        startTime = sequenceStartTimes.computeIfAbsent(sequence, k -> finalStartTime).plusMinutes(bufferDuration);
		        LocalDateTime calculatedEndTime = startTime.plusMinutes(activityDuration);

		        // Apply skipping logic based on weekdays
		        if ("5".equals(weekdays)) {
		            calculatedEndTime = skipWeekends(startTime, activityDuration);
		        } else if ("6".equals(weekdays)) {
		            calculatedEndTime = skipSundays(startTime, activityDuration);
		        }

		        endTime = calculatedEndTime;

		        // 🔹 Ensure the last sequence gets the correct start time by storing the maximum end time
		        sequenceMaxEndTimes.put(sequence, sequenceMaxEndTimes.containsKey(sequence)
		            ? (endTime.isAfter(sequenceMaxEndTimes.get(sequence)) ? endTime : sequenceMaxEndTimes.get(sequence))
		            : endTime);

		        // Debugging print statements
		        System.out.println("📌 Sequence: " + sequence);
		        System.out.println("   ➡ Start Time: " + startTime);
		        System.out.println("   ➡ End Time: " + endTime);
		        System.out.println("   🟢 Max End Time Stored: " + sequenceMaxEndTimes.get(sequence));

		        // Update activity with new start and end times
		        obj.setScheduledActivityStartTime(convertToDate(startTime));
		        obj.setScheduledActivityEndTime(convertToDate(endTime));
		        obj.setActvityStatus("Not Started");

		        newList.add(obj);
		        previousSequence = sequence;
		    }

		    return newList;
		}


		private LocalDateTime skipWeekends(LocalDateTime startTime, int duration) {
			while (startTime.getDayOfWeek().getValue() >= 6) { // 6 = Saturday, 7 = Sunday
				startTime = startTime.plusDays(1);
			}
			return startTime.plusMinutes(duration);
		}

		private LocalDateTime skipSundays(LocalDateTime startTime, int duration) {
			if (startTime.getDayOfWeek().getValue() == 7) { // Sunday
				startTime = startTime.plusDays(1);
			}
			return startTime.plusMinutes(duration);
		}

		private LocalDateTime parseDate(String fromDate) {
			List<DateTimeFormatter> formatters = List.of(
					DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
					DateTimeFormatter.ISO_LOCAL_DATE_TIME,
					DateTimeFormatter.ISO_OFFSET_DATE_TIME
					);

			for (DateTimeFormatter formatter : formatters) {
				try {
					return LocalDateTime.parse(fromDate, formatter);
				} catch (Exception ignored) {}
			}
			throw new IllegalArgumentException("Invalid date format: " + fromDate);
		}

		private JobDetails getHardcodedJobDetails(String approver) {
			JobDetails jobDetails = new JobDetails();
			jobDetails.setJobID("J20250226T140939169");
			jobDetails.setInstrument(null);
			jobDetails.setScheduledJobEndTime(convertToDate("2025-02-26T19:45:52.987"));
			jobDetails.setActualJobStartTime(null);
			jobDetails.setActualJobEndTime(null);
			jobDetails.setPriority("Critical");
			jobDetails.setGroupId(null);
			jobDetails.setWeekdays(7);
			jobDetails.setJobStatus("Not Started");
			jobDetails.setAdhoc(false);

			TaskDetail task = new TaskDetail();
			task.setTaskId("T20250226T113116176");
			task.setActivityList(getHardcodedActivities(approver));

			jobDetails.setTask(task);
			return jobDetails;
		}
		private Date convertToDate(String dateTimeString) {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
			try {
				return formatter.parse(dateTimeString);
			} catch (ParseException e) {
				throw new RuntimeException("Error parsing date: " + dateTimeString, e);
			}
		}
		private List<ActivityDetails> getHardcodedActivities(String approver) {
			List<ActivityDetails> activityList = new ArrayList<>();
			//    activityList.add(new ActivityDetails("T20250226T113116176", "A20250226T113238612", "HSE questionnaire", 1, "Logbook 1", 30, "Asset 1"));
			//    activityList.add(new ActivityDetails("T20250226T113116176", "A20250226T113345765", "Verify public liability insurance", 2, "Logbook 2", 45, "Asset 2"));
			//    activityList.add(new ActivityDetails("T20250226T113116176", "A20250226T113421814", "Verify professional indemnity insurance", 2, "Logbook 3", 30, "Asset 3"));
			activityList.add(new ActivityDetails("T20250226T113116176", "A20250226T113238612", "HSE questionnaire", 1, "Health and Safety Questionnaire", 30, "",approver,"priyam.kaushal@vesuvius.com"));
			activityList.add(new ActivityDetails("T20250226T113116176", "A20250226T113345765", "Verify public liability insurance", 2, "Public liability insurance", 30, "",approver,"priyam.kaushal@vesuvius.com"));
			activityList.add(new ActivityDetails("T20250226T113116176", "A20250226T113421814", "Verify professional indemnity insurance", 2, "Professional indemnity", 30, "",approver,"priyam.kaushal@vesuvius.com"));
			activityList.add(new ActivityDetails("T20250226T113116176", "A20250226T115138994", "Purchase order or contract", 3, "Purchase order or Contract", 30, "",approver,"pankaj.soni@vesuvius.com"));

			activityList.add(new ActivityDetails("T20250226T113116176", "A20250226T113900208", "Project plan", 4, "", 30, "",approver,"pankaj.soni@vesuvius.com"));
			activityList.add(new ActivityDetails("T20250226T113116176", "A20250226T113557904", "Risk assessment", 5, "Risk Assessment", 30, "",approver,"priyam.kaushal@vesuvius.com"));
			activityList.add(new ActivityDetails("T20250226T113116176", "A20250226T113814484", "HSE plan", 6, "HSE plan", 30, "",approver,"priyam.kaushal@vesuvius.com"));


			activityList.add(new ActivityDetails("T20250226T113116176", "A20250226T114744419", "Safety induction", 7, "", 30, "",approver,"priyam.kaushal@vesuvius.com"));
			activityList.add(new ActivityDetails("T20250226T113116176", "A20250226T114814333", "Permits and qualifications", 7, "", 30, "",approver,"permit.supervisor@vesuvius.com"));
			activityList.add(new ActivityDetails("T20250226T113116176", "A20250226T114847395", "Physical and medical aptitude", 7, "", 30, "",approver,"medical.professional@vesuvius.com"));


			activityList.add(new ActivityDetails("T20250226T113116176", "A20250226T114722406", "Code of practice", 8, "Code of practice", 30, "",approver,"priyam.kaushal@vesuvius.com"));
			activityList.add(new ActivityDetails("T20250226T113116176", "A20250226T114910550", "Permit to work", 9, "Permit to work", 30, "",approver,"permit.supervisor@vesuvius.com"));
			activityList.add(new ActivityDetails("T20250226T113116176", "A20250226T114932423", "Safety audit", 10, "Safety audit", 30, "",approver,"pankaj.soni@vesuvius.com"));
			activityList.add(new ActivityDetails("T20250226T113116176", "A20250226T115004788", "Post project review", 11, "", 30, "",approver,"pankaj.soni@vesuvius.com"));





			return activityList;

		}
}


