package in.co.greenwave.mail.model;

import java.util.ArrayList;
import java.util.List;

public class Mail {
	
//	final String username = "scadagw2016@gmail.com"; 
//	final String password = "fkmvbnttikkeeode";   
	private List<String> toMailIds;//List of mailIds  of the user where the mail will be sent
	private String messageSubject = "";//the subject of the mail
	private String htmlPage;//the main htmlPage where the content is stored in String format
	private List<String> emailCc; //the list of mailIds who will be kept as cc while sending the mail
	
	public List<String> getToMailIds() {
		return toMailIds;
	}

	public void setToMailIds(List<String> toMailIds) {
		this.toMailIds = toMailIds;
	}

	public String getMessageSubject() {
		return messageSubject;
	}

	public void setMessageSubject(String messageSubject) {
		this.messageSubject = messageSubject;
	}

	public String getHtmlPage() {
		return htmlPage;
	}

	public void setHtmlPage(String htmlPage) {
		this.htmlPage = htmlPage;
	}

	public List<String> getEmailCc() {
		return emailCc;
	}

	public void setEmailCc(List<String> emailCc) {
		this.emailCc = emailCc;
	}

	@Override
	public String toString() {
		return "Mail [toMailIds=" + toMailIds + ", messageSubject=" + messageSubject + ", htmlPage=" + htmlPage
				+ ", emailCc=" + emailCc + "]";
	}
	
	
	
	
}
