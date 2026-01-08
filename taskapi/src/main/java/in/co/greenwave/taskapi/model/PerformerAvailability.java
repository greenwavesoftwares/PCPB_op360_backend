package in.co.greenwave.taskapi.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder(alphabetic = true)
@JsonInclude(JsonInclude.Include.NON_NULL)

public class PerformerAvailability {

	private boolean availability;
	private String jobId;
	private String actId;
	private String performer;
	private String approver;
	private String assetId;
	private String assetName;
	private String scheduleStart;
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

