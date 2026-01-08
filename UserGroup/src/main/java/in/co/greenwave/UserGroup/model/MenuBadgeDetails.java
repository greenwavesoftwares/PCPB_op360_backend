package in.co.greenwave.UserGroup.model;

public class MenuBadgeDetails {
	private int tasksToReviewCount;
	private int activitiesToPerformCount;
	private int activitiesToReviewCount;
	private int jobsToReviewCount;
	private int reportsCount;
	public MenuBadgeDetails(int tasksToReviewCount, int activitiesToPerformCount, int activitiesToReviewCount,
			int jobsToReviewCount, int reportsCount) {
		super();
		this.tasksToReviewCount = tasksToReviewCount;
		this.activitiesToPerformCount = activitiesToPerformCount;
		this.activitiesToReviewCount = activitiesToReviewCount;
		this.jobsToReviewCount = jobsToReviewCount;
		this.reportsCount = reportsCount;
	}
	public MenuBadgeDetails() {
		super();
		// TODO Auto-generated constructor stub
	}
	public int getTasksToReviewCount() {
		return tasksToReviewCount;
	}
	public void setTasksToReviewCount(int tasksToReviewCount) {
		this.tasksToReviewCount = tasksToReviewCount;
	}
	public int getActivitiesToPerformCount() {
		return activitiesToPerformCount;
	}
	public void setActivitiesToPerformCount(int activitiesToPerformCount) {
		this.activitiesToPerformCount = activitiesToPerformCount;
	}
	public int getActivitiesToReviewCount() {
		return activitiesToReviewCount;
	}
	public void setActivitiesToReviewCount(int activitiesToReviewCount) {
		this.activitiesToReviewCount = activitiesToReviewCount;
	}
	public int getJobsToReviewCount() {
		return jobsToReviewCount;
	}
	public void setJobsToReviewCount(int jobsToReviewCount) {
		this.jobsToReviewCount = jobsToReviewCount;
	}
	public int getReportsCount() {
		return reportsCount;
	}
	public void setReportsCount(int reportsCount) {
		this.reportsCount = reportsCount;
	}
	@Override
	public String toString() {
		return "MenuBadgeDetails [tasksToReviewCount=" + tasksToReviewCount + ", activitiesToPerformCount="
				+ activitiesToPerformCount + ", activitiesToReviewCount=" + activitiesToReviewCount
				+ ", jobsToReviewCount=" + jobsToReviewCount + ", reportsCount=" + reportsCount + "]";
	}
	
	
}
