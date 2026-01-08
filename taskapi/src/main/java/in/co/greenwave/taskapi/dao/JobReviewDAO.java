package in.co.greenwave.taskapi.dao;

import java.util.List;

import in.co.greenwave.taskapi.model.ApproverCardData;
import in.co.greenwave.taskapi.model.JobDetails;

//import org.primefaces.model.chart.BarChartModel;
//
//import in.co.greenwave.modules.commonUtilities.JobDetails;
//import in.co.greenwave.modules.jobReview.ApproverCardData;

public interface JobReviewDAO {

	public int fetchCompletedJobCount(String reviewer,String tenantId);

//	public List<JobDetails> pendingForReviewJobs(String user);
//	public List<JobDetails> approvedorRejectedJobs(String user,String revAction);
//	public void updateApprovalDetails(String jobId,String remarks,String action);
/*	public ApproverCardData getApprovedHistory(String approver);*/
//	public BarChartModel getNextSevenDayTasks(String approver);
//	public BarChartModel getLastSevenDayApprovedTasks(String approver);
//	public BarChartModel getNextSevenDayRejectedTasks(String approver);
}
