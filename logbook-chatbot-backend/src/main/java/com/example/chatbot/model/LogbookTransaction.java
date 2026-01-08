package com.example.chatbot.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.mongodb.lang.Nullable;

import java.util.Map;

@Document(collection = "logbookTransaction")
public class LogbookTransaction {
    @Id
    private String id;
    private int logbookId;
    private String formName;
    private String transactionTimestamp; // Use String to avoid ZonedDateTime parsing issue
    @Nullable
    private Map<String, LogbookField> logbookData; // key to value info
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
	public String getTransactionTimestamp() {
		return transactionTimestamp;
	}
	public void setTransactionTimestamp(String transactionTimestamp) {
		this.transactionTimestamp = transactionTimestamp;
	}
	public Map<String, LogbookField> getLogbookData() {
		return logbookData;
	}
	public void setLogbookData(Map<String, LogbookField> logbookData) {
		this.logbookData = logbookData;
	}
	public LogbookTransaction(String id, int logbookId, String formName, String transactionTimestamp,
			Map<String, LogbookField> logbookData) {
		super();
		this.id = id;
		this.logbookId = logbookId;
		this.formName = formName;
		this.transactionTimestamp = transactionTimestamp;
		this.logbookData = logbookData;
	}
    
}