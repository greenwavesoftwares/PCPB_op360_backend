package com.example.chatbot.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Document(collection = "logbookMetaInfo")
public class LogbookMetaInfo {
    @Id
    private String id;
    private int logbookId;
    private String formName;
    private int versionNumber;
    private Map<String, String> data; // key to label
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getLogbookId() {
		return logbookId;
	}
	public void setLogbookId(int logbookId) {
		this.logbookId = logbookId;
	}
	public String getFormName() {
		return formName;
	}
	public void setFormName(String formName) {
		this.formName = formName;
	}
	public int getVersionNumber() {
		return versionNumber;
	}
	public void setVersionNumber(int versionNumber) {
		this.versionNumber = versionNumber;
	}
	public Map<String, String> getData() {
		return data;
	}
	public void setData(Map<String, String> data) {
		this.data = data;
	}
	public LogbookMetaInfo(String id, int logbookId, String formName, int versionNumber, Map<String, String> data) {
		super();
		this.id = id;
		this.logbookId = logbookId;
		this.formName = formName;
		this.versionNumber = versionNumber;
		this.data = data;
	}
    
}