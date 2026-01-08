package in.co.greenwave.taskapi.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


public class ActivityConnection {

	private String taskId;
	
	private String source;
	
	private String sourceId;
	
	private String target;
	
	private String targetId;
	
	public ActivityConnection() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ActivityConnection(String taskId, String source, String sourceId, String target, String targetId) {
		super();
		this.taskId = taskId;
		this.source = source;
		this.sourceId = sourceId;
		this.target = target;
		this.targetId = targetId;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getTargetId() {
		return targetId;
	}

	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}

	@Override
	public String toString() {
		return "ActivityConnection [taskId=" + taskId + ", source=" + source + ", sourceId=" + sourceId + ", target="
				+ target + ", targetId=" + targetId + "]";
	}
	
	
}
