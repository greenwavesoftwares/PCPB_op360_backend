package in.co.greenwave.UserGroup.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

//only shows not null parameters in the json
@JsonInclude(JsonInclude.Include.NON_NULL)
//lexicographically arranges the alphabets
@JsonPropertyOrder(alphabetic=true)
public class PerformerAvailability {
	// Flag indicating whether the item (job, activity, asset, etc.) is available or not
	private boolean availability;

	// Unique identifier for the job associated with the task or activity
	private String jobId;

	// Unique identifier for the activity related to the job or task
	private String actId;

	// Name of the performer responsible for carrying out the activity or task
	private String performer;

	// Name of the person responsible for approving the activity or task
	private String approver;

	// Unique identifier for the asset involved in the job or activity
	private String assetId;

	// Name of the asset associated with the job or activity (e.g., tool, equipment)
	private String assetName;

	// Scheduled start time for the job or activity (typically in a string format like "yyyy-MM-dd HH:mm:ss")
	private String scheduleStart;

	// Scheduled stop time for the job or activity (typically in a string format like "yyyy-MM-dd HH:mm:ss")
	private String scheduleStop;

	
	// for getAssetAvailability 
	public PerformerAvailability(boolean availability, String jobId, String actId, String performer, String approver,
			String scheduleStart, String scheduleStop, String assetId, String assetName) {
		super();
		this.availability = availability;
		this.jobId = jobId;
		this.actId = actId;
		this.performer = performer;
		this.approver = approver;
		this.scheduleStart = scheduleStart;
		this.scheduleStop = scheduleStop;
		this.assetId = assetId;
		this.assetName = assetName;
	}
	
	public PerformerAvailability(boolean availability, String jobId, String actId, String performer, String approver,
			String scheduleStart, String scheduleStop) {
		super();
		this.availability = availability;
		this.jobId = jobId;
		this.actId = actId;
		this.performer = performer;
		this.approver = approver;
		this.scheduleStart = scheduleStart;
		this.scheduleStop = scheduleStop;
	}
	
	public boolean isAvailability() {
		return availability;
	}
	public void setAvailability(boolean availability) {
		this.availability = availability;
	}
	public String getJobId() {
		return jobId;
	}
	public void setJobId(String jobId) {
		this.jobId = jobId;
	}
	public String getActId() {
		return actId;
	}
	public void setActId(String actId) {
		this.actId = actId;
	}
	public String getPerformer() {
		return performer;
	}
	public void setPerformer(String performer) {
		this.performer = performer;
	}
	public String getApprover() {
		return approver;
	}
	public void setApprover(String approver) {
		this.approver = approver;
	}
	public String getScheduleStart() {
		return scheduleStart;
	}
	public void setScheduleStart(String scheduleStart) {
		this.scheduleStart = scheduleStart;
	}
	public String getScheduleStop() {
		return scheduleStop;
	}
	public void setScheduleStop(String scheduleStop) {
		this.scheduleStop = scheduleStop;
	}
	
	public String getAssetId() {
		return assetId;
	}
	public void setAssetId(String assetId) {
		this.assetId = assetId;
	}
	public String getAssetName() {
		return assetName;
	}
	public void setAssetName(String assetName) {
		this.assetName = assetName;
	}
	
}
