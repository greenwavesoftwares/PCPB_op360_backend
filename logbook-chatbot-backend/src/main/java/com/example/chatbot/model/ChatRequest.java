package com.example.chatbot.model;


import java.util.List;


public class ChatRequest {
    private Integer logbookId;
    private List<Message> messages;


    public static class Message {
        private String role;  // user | assistant
        private String content;
		public String getRole() {
			return role;
		}
		public void setRole(String role) {
			this.role = role;
		}
		public String getContent() {
			return content;
		}
		public void setContent(String content) {
			this.content = content;
		}
		public Message(String role, String content) {
			super();
			this.role = role;
			this.content = content;
		}
		public Message() {
			super();
			// TODO Auto-generated constructor stub
		}
        
    }


	public Integer getLogbookId() {
		return logbookId;
	}


	public void setLogbookId(Integer logbookId) {
		this.logbookId = logbookId;
	}


	public List<Message> getMessages() {
		return messages;
	}


	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}


	public ChatRequest(Integer logbookId, List<Message> messages) {
		super();
		this.logbookId = logbookId;
		this.messages = messages;
	}


	@Override
	public String toString() {
		return "ChatRequest [logbookId=" + logbookId + ", messages=" + messages + "]";
	}


	public ChatRequest() {
		super();
		// TODO Auto-generated constructor stub
	}
    
}