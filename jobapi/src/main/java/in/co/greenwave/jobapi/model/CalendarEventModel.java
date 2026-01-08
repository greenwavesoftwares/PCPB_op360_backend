package in.co.greenwave.jobapi.model;

public class CalendarEventModel {
	
	private String jobname; // Name of the job associated with the calendar event
	private String scheduleStart; // Scheduled start time for the event (formatted as a String)
	private String scheduleStop; // Scheduled end time for the event (formatted as a String)
	private String priority; // Priority of the event (e.g., Critical, Medium, Low)
	private String jobId; // Unique identifier for the job related to the event
	private String status; // Current status of the event 
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	public String getJobId() {
		return jobId;
	}
	public void setJobId(String jobId) {
		this.jobId = jobId;
	}
	public String getJobname() {
		return jobname;
	}
	public void setJobname(String jobname) {
		this.jobname = jobname;
	}
	@Override
	public String toString() {
		return "CalendarEventModel [jobname=" + jobname + ", scheduleStart=" + scheduleStart + ", scheduleStop="
				+ scheduleStop + ", priority=" + priority + ", jobId=" + jobId + ", status=" + status + "]";
	}

}
