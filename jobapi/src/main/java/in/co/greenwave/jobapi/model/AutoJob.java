package in.co.greenwave.jobapi.model;

import java.util.Date;

public class AutoJob {

	private String jobid; // Unique identifier for the job
	private String activityid; // Unique identifier for the activity
	private String jobname; // Name of the job
	private String assetid; // Identifier for the associated asset
	private String assetname; // Name of the associated asset
	private int logbookid; // Identifier for the logbook
	private String formname; // Name of the form associated with the job/activity
	private int logbookversion; // Version of the logbook being used
	private String performer; // Name of the individual responsible for performing the activity
	private String reviewer; // Name of the individual reviewing the activity
	private Date activitystarttime; // Start time of the activity
	private Date activityendtime; // End time of the activity
	private Date reviewstarttime; // Start time of the review process
	private Date reviewendtime; // End time of the review process
	private String status; // Current status of the job/activity 
	private String remarks; //Remarks related to the job

	
	
	
	
	public AutoJob() {
		
	}
	public AutoJob(String jobid, String activityid, String jobname, String assetid, String assetname, int logbookid,
			String formname, int logbookversion, String performer, String reviewer, Date activitystarttime,
			String status) {
		this.jobid = jobid;
		this.activityid = activityid;
		this.jobname = jobname;
		this.assetid = assetid;
		this.assetname = assetname;
		this.logbookid = logbookid;
		this.formname = formname;
		this.logbookversion = logbookversion;
		this.performer = performer;
		this.reviewer = reviewer;
		this.activitystarttime = activitystarttime;
		this.status = status;
	}
	public AutoJob(String jobid, String activityid, String jobname, String assetid, String assetname, int logbookid,
			String formname, int logbookversion, String performer, String reviewer, Date activitystarttime,Date activityendtime,
			String status) {
		this.jobid = jobid;
		this.activityid = activityid;
		this.jobname = jobname;
		this.assetid = assetid;
		this.assetname = assetname;
		this.logbookid = logbookid;
		this.formname = formname;
		this.logbookversion = logbookversion;
		this.performer = performer;
		this.reviewer = reviewer;
		this.activitystarttime = activitystarttime;
		this.activityendtime = activityendtime;
		this.status = status;
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((activityid == null) ? 0 : activityid.hashCode());
		result = prime * result + ((jobid == null) ? 0 : jobid.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AutoJob other = (AutoJob) obj;
		if (activityid == null) {
			if (other.activityid != null)
				return false;
		} else if (!activityid.equals(other.activityid))
			return false;
		if (jobid == null) {
			if (other.jobid != null)
				return false;
		} else if (!jobid.equals(other.jobid))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "AutoJob [jobid=" + jobid + ", activityid=" + activityid + ", jobname=" + jobname + ", assetid="
				+ assetid + ", assetname=" + assetname + ", logbookid=" + logbookid + ", formname=" + formname
				+ ", logbookversion=" + logbookversion + ", performer=" + performer + ", reviewer=" + reviewer
				+ ", activitystarttime=" + activitystarttime + ", activityendtime=" + activityendtime
				+ ", reviewstarttime=" + reviewstarttime + ", reviewendtime=" + reviewendtime + ", status=" + status
				+ "]";
	}
	public String getJobid() {
		return jobid;
	}
	public void setJobid(String jobid) {
		this.jobid = jobid;
	}
	public String getActivityid() {
		return activityid;
	}
	public void setActivityid(String activityid) {
		this.activityid = activityid;
	}
	public String getJobname() {
		return jobname;
	}
	public void setJobname(String jobname) {
		this.jobname = jobname;
	}
	public String getAssetid() {
		return assetid;
	}
	public void setAssetid(String assetid) {
		this.assetid = assetid;
	}
	public String getAssetname() {
		return assetname;
	}
	public void setAssetname(String assetname) {
		this.assetname = assetname;
	}
	public int getLogbookid() {
		return logbookid;
	}
	public void setLogbookid(int logbookid) {
		this.logbookid = logbookid;
	}
	public String getFormname() {
		return formname;
	}
	public void setFormname(String formname) {
		this.formname = formname;
	}
	public int getLogbookversion() {
		return logbookversion;
	}
	public void setLogbookversion(int logbookversion) {
		this.logbookversion = logbookversion;
	}
	public String getPerformer() {
		return performer;
	}
	public void setPerformer(String performer) {
		this.performer = performer;
	}
	public String getReviewer() {
		return reviewer;
	}
	public void setReviewer(String reviewer) {
		this.reviewer = reviewer;
	}
	public Date getActivitystarttime() {
		return activitystarttime;
	}
	public void setActivitystarttime(Date activitystarttime) {
		this.activitystarttime = activitystarttime;
	}
	public Date getActivityendtime() {
		return activityendtime;
	}
	public void setActivityendtime(Date activityendtime) {
		this.activityendtime = activityendtime;
	}
	public Date getReviewstarttime() {
		return reviewstarttime;
	}
	public void setReviewstarttime(Date reviewstarttime) {
		this.reviewstarttime = reviewstarttime;
	}
	public Date getReviewendtime() {
		return reviewendtime;
	}
	public void setReviewendtime(Date reviewendtime) {
		this.reviewendtime = reviewendtime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	
}
