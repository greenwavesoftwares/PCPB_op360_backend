package in.co.greenwave.jobapi.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder(alphabetic = true)
public class ApproverCardData {

	// Name of the person responsible for approving 
	private String approver;

	// Count of activities that are currently pending approval
	private int pending;

	// Count of activities that have been approved
	private int approved;

	// Count of activities that have been rejected
	private int rejected;

//	private BarChartModel nextPendingTasks = new BarChartModel();
//	private BarChartModel previousApprovedTasks = new BarChartModel();
//	private BarChartModel previousRejectedTasks = new BarChartModel();
	
	
	public ApproverCardData(String approver, int pending, int approved, int rejected) {
		super();
		this.approver = approver;
		this.pending = pending;
		this.approved = approved;
		this.rejected = rejected;
	}
	public ApproverCardData() {
		// TODO Auto-generated constructor stub
	}
	public String getApprover() {
		return approver;
	}
	public void setApprover(String approver) {
		this.approver = approver;
	}
	public int getPending() {
		return pending;
	}
	public void setPending(int pending) {
		this.pending = pending;
	}
	public int getApproved() {
		return approved;
	}
	public void setApproved(int approved) {
		this.approved = approved;
	}
	public int getRejected() {
		return rejected;
	}
	public void setRejected(int rejected) {
		this.rejected = rejected;
	}
//	public BarChartModel getNextPendingTasks() {
//		return nextPendingTasks;
//	}
//	public void setNextPendingTasks(BarChartModel nextPendingTasks) {
//		this.nextPendingTasks = nextPendingTasks;
//	}
//	public BarChartModel getPreviousApprovedTasks() {
//		return previousApprovedTasks;
//	}
//	public void setPreviousApprovedTasks(BarChartModel previousApprovedTasks) {
//		this.previousApprovedTasks = previousApprovedTasks;
//	}
//	public BarChartModel getPreviousRejectedTasks() {
//		return previousRejectedTasks;
//	}
//	public void setPreviousRejectedTasks(BarChartModel previousRejectedTasks) {
//		this.previousRejectedTasks = previousRejectedTasks;
//	}
	
}
