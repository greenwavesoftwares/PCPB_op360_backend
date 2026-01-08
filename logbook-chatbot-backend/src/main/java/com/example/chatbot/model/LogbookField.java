package com.example.chatbot.model;

public class LogbookField {
    private String remarks;
    private String value;
    private boolean disabled;
    private boolean requiredfield;
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public boolean isDisabled() {
		return disabled;
	}
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
	public boolean isRequiredfield() {
		return requiredfield;
	}
	public void setRequiredfield(boolean requiredfield) {
		this.requiredfield = requiredfield;
	}
	public LogbookField(String remarks, String value, boolean disabled, boolean requiredfield) {
		super();
		this.remarks = remarks;
		this.value = value;
		this.disabled = disabled;
		this.requiredfield = requiredfield;
	}
	public LogbookField() {
		super();
		// TODO Auto-generated constructor stub
	}
    
}