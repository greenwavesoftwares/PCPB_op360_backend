package in.co.greenwave.jobapi.controller;

import java.io.InputStream;
import org.springframework.http.HttpHeaders;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.compress.utils.IOUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import in.co.greenwave.jobapi.model.Document;

import in.co.greenwave.jobapi.dao.JobPerformDAO;
import in.co.greenwave.jobapi.dao.MinIOUploadDAO;
import in.co.greenwave.jobapi.dao.factory.DAOFactory;
import in.co.greenwave.jobapi.model.ActivityDetails;
import in.co.greenwave.jobapi.model.JobDetails;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/jobperform")
public class JobPerformerController {
	/**
	 * The "factory" reference is getting Autowired ,with the Bean  of the JobApiConfiguration class
	 */
	@Autowired
	private DAOFactory factory;

	private JobPerformDAO jobperformdao;
	private MinIOUploadDAO miniodao;
	@Autowired
	MinioClient minioclient;

	/**
	 * Here with respect to @param jobid the lastCompleted order(int) is returned
	 * @param jobid
	 * @return int order
	 */
	@GetMapping(path="/jobidlist/{jobid}/{tenantId}", produces = "application/json")
	public ResponseEntity<Object> lastCompletedOrder(@PathVariable("jobid") String jobid,@PathVariable("tenantId") String tenantId) {
		System.out.println("lastCompletedOrder() "+jobid);
		jobperformdao = factory.getJobPerformService();
		int order;
		try {
			order = jobperformdao.fetchLastCompletedOrder(jobid,tenantId);
		}
		catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Object>("Error occured : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

		}
		return new ResponseEntity<Object>(order,HttpStatus.OK);
	}
	/**
	 * Here with respect to the @param jobid && @param lastcompletedorder the remainingActvtSize(int) is returned as response
	 * @param jobid
	 * @param lastcompletedorder
	 * @return int remainingActvtSize
	 */
	@GetMapping(path="/joblist/{jobid}/{lastcompletedorder}/{tenantId}",produces ="application/json" )
	public ResponseEntity<Object> remainingActivities(@PathVariable("jobid") String jobid, @PathVariable("lastcompletedorder") int lastcompletedorder,@PathVariable("tenantId") String tenantId) {
		System.out.println("remainingActivities() "+ jobid +" "+lastcompletedorder);
		jobperformdao = factory.getJobPerformService();
		int remainingActvtSize;
		try {
			remainingActvtSize = jobperformdao.fetchRemainingActivties(jobid, lastcompletedorder,tenantId);
		}
		catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Object>("Error occured : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<Object>(remainingActvtSize,HttpStatus.OK);
	}

	/**
	 * Based on JobId and ActivityId the filename is updated here and the  ActivityDetails JSON is returned as response
	 * @param filename
	 * @RequestBody ActivityDetails act
	 * @return ActivityDetails act
	 */
	@PutMapping(path="/fileupdate/{filename}/{tenantId}",consumes = "application/json")
	public ResponseEntity<Object> updateFile(@PathVariable("filename") String filename, @RequestBody ActivityDetails act,@PathVariable("tenantId") String tenantId) {
		jobperformdao = factory.getJobPerformService();
		System.out.println(" updateFile() filename : "+filename);
		//System.out.println("The activityDetails sent from server --> "+act.getActivityId()+" "+act.getJobId());
		try {
			jobperformdao.updateFileName(act, filename,tenantId);
		}
		catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Object>("UNAUTHORIZED ACCESS.", HttpStatus.UNAUTHORIZED);
		}

		return new ResponseEntity<Object>(act,HttpStatus.OK);
	}
	
	/**
	 * Here with respect to @param tenantid and jobid the details of a job is returned
	 * @param tenantid and jobId 
	 * @return JobDetails job
	 * @author Sreepriya Roy
	 */
	@GetMapping(path="/jobdetails/{jobId}/{tenantId}",produces = "application/json" )
	public ResponseEntity<Object> getJobDetailsByjobId(@PathVariable("jobId") String jobId,@PathVariable("tenantId") String tenantId) {
		jobperformdao = factory.getJobPerformService();
		JobDetails job=new JobDetails();
		try {
			job=jobperformdao.getJobDetailsByJobId(jobId,tenantId);
			return new ResponseEntity<Object>(job,HttpStatus.OK);
			
		}
		catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Object>("INTERNAL SERVER ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
		}

		
	}
	
	
	
	/**
	 * Here with respect to @param tenantid and performergrouplist the list of jobs required by a performer are returned
	 * @param tenantid and performergrouplist 
	 * @return List<JobDetails> jobDetailsList
	 * @author Sreepriya Roy
	 */
		@GetMapping(path="{tenantid}/{performergrouplist}",produces = "application/json")
		//@JsonPropertyOrder(value = {"context"}, alphabetic = true)
		public ResponseEntity<Object> getJobDataBasedOnPerformer(@PathVariable("performergrouplist") String performergrouplist,@PathVariable("tenantid") String tenantid)
		{
			jobperformdao = factory.getJobPerformService();
			List<JobDetails> jobDetailsList = new ArrayList<JobDetails>();
			try {
				jobDetailsList.addAll(jobperformdao.getJobDataBasedOnPerformer(performergrouplist,tenantid));
				return new ResponseEntity<Object>(jobDetailsList,HttpStatus.OK);
			}
			catch (Exception e) {
				e.printStackTrace();
				return new ResponseEntity<Object>("Error occured : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	 
	 
			}

			
		}
		
		/**-
		* Here with respect to @param tenantid and performergrouplist the list of historical jobs by a performer are returned
		 * @param tenantid and performergrouplist 
		 * @return List<JobDetails> jobDetailsList
		 * @author Sreepriya Roy
		 */
			@GetMapping(path="/historicaldata/{tenantid}/{performergrouplist}/{fromDate}/{toDate}",produces = "application/json")
			//@JsonPropertyOrder(value = {"context"}, alphabetic = true)
			public ResponseEntity<Object> getHistoricalActivitiesBasedOnPerformer(@PathVariable("performergrouplist") String performergrouplist,@PathVariable("tenantid") String tenantid,@PathVariable("fromDate") Date fromDate,@PathVariable("toDate") Date toDate)
			{
				jobperformdao = factory.getJobPerformService();
				List<ActivityDetails> historicalActivities = new ArrayList<ActivityDetails>();
				try {
					historicalActivities.addAll(jobperformdao.getHistoricalActivitiesBasedOnPerformer(performergrouplist,tenantid,fromDate,toDate));
					return new ResponseEntity<Object>(historicalActivities,HttpStatus.OK);
				}
				catch (Exception e) {
					e.printStackTrace();
					return new ResponseEntity<Object>("Error occured : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		 
		 
				}

				
			}

		/**
		 * Here with respect to @param tenantid and reviewer the number of completed jobs count is returned
		 * @param tenantid and reviewer 
		 * @return int
		 */
		@GetMapping("/getCompletedJobCount/{tenantId}/{reviewer}")
		public ResponseEntity<Object> getCompletedJobCount(@PathVariable("reviewer") String reviewer,@PathVariable("tenantId" )String tenantId){
			try {
				jobperformdao=factory.getJobPerformService();
				
				return new ResponseEntity<Object>(jobperformdao.fetchCompletedJobCount(reviewer,tenantId),HttpStatus.OK);
			}catch(Exception e) {
				e.printStackTrace();
				
				return new ResponseEntity<Object>("UNAUTHORIZED ACCESS",HttpStatus.UNAUTHORIZED);
			}
		}
		/**
		 * Here with respect to @param tenantid and performergrouplist the last ten jobs of a performer are returned,needed in the performer page
		 * @param tenantid and performergrouplist 
		 * @return List<JobDetails> jobDetailsList
		 * @author SreepriyaRoy
		 */
		@GetMapping("/lasttenjobs/{tenantid}/{performergrouplist}")
		public ResponseEntity<Object> getLastTenJobs(@PathVariable String tenantid,@PathVariable String performergrouplist){
			try {
				jobperformdao=factory.getJobPerformService();
				List<JobDetails> jobDetails=jobperformdao.fetchLastTenJobs(performergrouplist,tenantid);
				return new ResponseEntity<Object>(jobDetails,HttpStatus.OK);
				
			}catch(Exception e) {
				System.err.println("Error in getLastTenJobs of JobPerformerController:"+e);
				return new ResponseEntity<Object>("UNAUTHORIZED ACCESS",HttpStatus.UNAUTHORIZED);
			}
		}
		/**
		 * Here with respect to @param tenantid and performergrouplist the details required to display the stats in the performer page is returned
		 * @param tenantid and performergrouplist 
		 * @return List<JobDetails> jobDetailsList
		 * @author SreepriyaRoy
		 */
		@GetMapping("performerstats/{tenantid}/{performergrouplist}")
		public ResponseEntity<Object> getPerformerStats(@PathVariable String tenantid,@PathVariable String performergrouplist){
			try {
				jobperformdao=factory.getJobPerformService();
				Map<String, Map<String,Integer>> details=jobperformdao.fetchPerformerStats(performergrouplist,tenantid);
				return new ResponseEntity<Object>(details,HttpStatus.OK);
				
			}catch(Exception e) {
				System.err.println("Error in getPerformerStats of JobPerformerController:"+e);
				return new ResponseEntity<Object>("UNAUTHORIZED ACCESS",HttpStatus.UNAUTHORIZED);
			}
		}

				/**
				 * Here an activity is updated based on provided information
				 * @author Sreepriya Roy
				 */
				@PutMapping("updateactivitystatus/{tenantId}")
				public ResponseEntity<Object> updateactivitystatus(@RequestBody ActivityDetails activity,@PathVariable String tenantId){
					try {
						System.out.println("Inside updateactivitystatus");
						jobperformdao=factory.getJobPerformService();
						jobperformdao.updateActivityStatus(activity,tenantId);
						return new ResponseEntity<Object>("Update Successful",HttpStatus.OK);
						
					}catch(Exception e) {
						System.err.println("Error in updateactivitystatus of JobPerformerController:"+e);
						return new ResponseEntity<Object>("UNAUTHORIZED ACCESS",HttpStatus.UNAUTHORIZED);
					}
				}
				/**
				 * Here with respect to @param tenantid and jobid the information of a given job is returned
				 * @param tenantid and jobid 
				 * @return JobDetails jobDetailsList
				 * @author Sreepriya Roy
				 */
				@GetMapping("getJobDetailsBasedOnJobId/{jobid}/{tenantid}")
				public ResponseEntity<Object> getJobDetailsBasedOnJobId(@PathVariable String jobid,@PathVariable String tenantid){
					try {
						System.out.println("getJobDetailsBasedOnJobId");
						jobperformdao=factory.getJobPerformService();
						JobDetails details=jobperformdao.getJobDetailsBasedOnJobId(jobid,tenantid);
						return new ResponseEntity<Object>(details,HttpStatus.OK);
						
					}catch(Exception e) {
						System.err.println("Error in getJobDetailsBasedOnJobId of JobPerformerController:"+e);
						return new ResponseEntity<Object>("UNAUTHORIZED ACCESS",HttpStatus.UNAUTHORIZED);
					}
				}
				/**
				 * Here with respect to @param tenantid , jobid and activityid the bit to bypass sequence is updated 
				 * @param tenantid and jobid 
				 * @return JobDetails jobDetailsList
				 * @author Sreepriya Roy
				 */
				@PutMapping("enableActivitiesBypassingSequence/{tenantid}/{jobid}/{activityid}")
				public ResponseEntity<Object> enableActivitiesBypassingSequence(@PathVariable String jobid,@PathVariable String activityid,@PathVariable String tenantid){
					try {
						System.out.println("enableActivitiesBypassingSequence");
						jobperformdao=factory.getJobPerformService();
						jobperformdao.enableActivitiesBypassingSequence(jobid,activityid,tenantid);
						return new ResponseEntity<Object>("Update Successful",HttpStatus.OK);
						
					}catch(Exception e) {
						System.err.println("Error in enableActivitiesBypassingSequence of JobPerformerController:"+e);
						return new ResponseEntity<Object>("UNAUTHORIZED ACCESS",HttpStatus.UNAUTHORIZED);
					}
				}
				
				/**
				 * Here a job is updated based on @param tenantid and jobid
				 * @param tenantid and jobid 
				 * @author Sreepriya Roy
				 */
				@PutMapping("updatejobstatus/{jobid}/{tenantid}")
				
				public ResponseEntity<Object> updatejobstatus(@RequestBody JobDetails job,@PathVariable String tenantid){
					try {
						jobperformdao=factory.getJobPerformService();
						jobperformdao.updateJobStatus(job,tenantid);
						return new ResponseEntity<Object>("Update Successful",HttpStatus.OK);
						
					}catch(Exception e) {
						System.err.println("Error in updatejobstatus of JobPerformerController:"+e);
						return new ResponseEntity<Object>("UNAUTHORIZED ACCESS",HttpStatus.UNAUTHORIZED);
					}
				}
				
				@Deprecated
				@PutMapping("updateEnforcedActivitiesDetails/{tenantid}")
				public ResponseEntity<Object> updateEnforcedActivitiesDetails(@PathVariable String tenantid){
					try {
						jobperformdao=factory.getJobPerformService();
						jobperformdao.updateEnforcedActivitiesDetails(tenantid);
						return new ResponseEntity<Object>("Update Successful",HttpStatus.OK);
						
					}catch(Exception e) {
						System.err.println("Error in updateEnforcedActivitiesDetails of JobPerformerController:"+e);
						return new ResponseEntity<Object>("UNAUTHORIZED ACCESS",HttpStatus.UNAUTHORIZED);
					}
				}
				@PostMapping("/sendMailForNextActivities")
				public ResponseEntity<Object> sendMail(@RequestBody Map<String,String> body) {
					try {
						System.out.println("Inside send mail");
						jobperformdao=factory.getJobPerformService();
						Object obj= jobperformdao.sendMail(body.get("tenantid"),body.get("token"),body.get("jobid"));
						return new ResponseEntity<Object>(obj,HttpStatus.OK);
					}catch(Exception e) {
						System.err.println("Error in sendMail of JobPerformerController:"+e);
						return new ResponseEntity<Object>("UNAUTHORIZED ACCESS",HttpStatus.UNAUTHORIZED);
					}
				}
				@PostMapping("/uploadFile")
			    public ResponseEntity<Object> uploadSingleFile(@RequestParam("file") MultipartFile file,@RequestParam("jobid") String jobid
			    		,@RequestParam("activityid") String activityid,@RequestParam("tenantid") String tenantid) {
			        try {
			        	System.out.println("Upload file");
			        	System.out.println("Activity id:"+activityid);
			            if (file.isEmpty()) return ResponseEntity.badRequest().build();
			            miniodao=factory.getMinIOUploadService();
			            Document savedDoc = miniodao.uploadFile(file, tenantid,jobid,activityid);
			            return new ResponseEntity<Object>(savedDoc,HttpStatus.OK);
			        } catch (Exception e) {
			            e.printStackTrace();
			            return new ResponseEntity<Object>("UNAUTHORIZED ACCESS",HttpStatus.UNAUTHORIZED);
			        }
			    }
//				@GetMapping("/download")
//				public ResponseEntity<StreamingResponseBody> download(@RequestParam("miniokey") String miniokey,@RequestParam("tenant") String tenant) throws Exception {
//
//					try {
//			        	System.out.println("Download file");
//			        	String filename=miniokey.split("_")[1];
//			        	System.out.println("Filename:"+filename);
//			            miniodao=factory.getMinIOUploadService();
//			            StreamingResponseBody body= miniodao.downloadFile(miniokey,tenant);
//			            return ResponseEntity.ok()
//			                    .header(HttpHeaders.CONTENT_DISPOSITION,
//			                            "attachment; filename=\"" + filename + "\"")
//			                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
//			                    .body(body);
//			        } catch (Exception e) {
//			            e.printStackTrace();
//			           return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//
//			        }
//				}
//				    
				@GetMapping("/download")
			    public void getFileByKey(@RequestParam String miniokey,@RequestParam String tenant, HttpServletResponse response) {
			        try {
			        	System.out.println("Inside download file");
			        	 miniodao=factory.getMinIOUploadService();
			        	InputStream  stream= miniodao.downloadFile(miniokey,tenant);

			            String contentType = null;

			            // Try automatic detection
			            try {
			                contentType = java.nio.file.Files.probeContentType(
			                    java.nio.file.Paths.get(miniokey)
			                );
			            } catch (Exception ignored) {}

			            // Special handling for .eml
			            if (contentType == null && miniokey.toLowerCase().endsWith(".eml")) {
			                contentType = "message/rfc822";
			            }

			            // Fallback for unknown types
			            if (contentType == null) {
			                contentType = "application/octet-stream";
			            }

			            response.setContentType(contentType);
			            

			            try (stream; ServletOutputStream out = response.getOutputStream()) {
			                byte[] buffer = new byte[8192];
			                int bytesRead;
			                while ((bytesRead = stream.read(buffer)) != -1) {
			                    out.write(buffer, 0, bytesRead);
			                }
			                out.flush();
			            }

			        } catch (Exception e) {
			            e.printStackTrace();
			            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			        }
			    }
}
