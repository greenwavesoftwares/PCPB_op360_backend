
package in.co.greenwave.mail.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.co.greenwave.mail.dao.MailDAO;
import in.co.greenwave.mail.dao.factory.DAOFactory;
import in.co.greenwave.mail.model.Mail;

@RestController
@RequestMapping("/mail")
public class MailController {
	
	@Autowired
	private DAOFactory factory;

	private MailDAO mailDAO;
	
	
	@PostMapping(path="/notifications",produces ="application/json" )
	public ResponseEntity<Object> sendEmailNotifications(@RequestBody Mail mail) {
		
		// Logging the method call and mail details such as ToMailIds and MessageSubject
	    System.out.println("sendEmailNotifications() called" + " Mail IDs => " + mail.getToMailIds() + 
	                       " Subject => " + mail.getMessageSubject());

	    // Retrieving the mail service using the factory (assuming it's a dependency)
	    mailDAO = factory.getMailService();
	    
	    // Initializing an empty list to store the recipient email IDs (ToMail)
	    List<String> toMailIdList = new ArrayList<>();
	    // Fetching recipient email IDs from the Mail object
	    toMailIdList = mail.getToMailIds();

	    // Retrieving the email subject from the Mail object
	    String currentMailSubject = mail.getMessageSubject();
	    // Retrieving the HTML email content from the Mail object
	    String content = mail.getHtmlPage();

	    // Initializing an empty list to store CC email IDs
	    List<String> emailCcList = new ArrayList<>();
	    // Fetching CC email IDs from the Mail object
	    emailCcList = mail.getEmailCc();

	    // A flag to track whether the email sending operation is successful
	    boolean success = false;

	    // Calling the mailDAO to send the email with the subject, recipient list, CC list, and content
	    success = mailDAO.sendEmailNotifications(currentMailSubject, toMailIdList, emailCcList, content);

	    // If the email is sent successfully, return HTTP status OK (200)
	    if (success) {
	        return new ResponseEntity<Object>(success, HttpStatus.OK);
	    }

	    // If email sending fails, return HTTP status INTERNAL_SERVER_ERROR (500)
	    return new ResponseEntity<Object>(HttpStatus.INTERNAL_SERVER_ERROR);
//		try {
//			success = mailDAO.sendEmailNotifications(currentMailSubject, toMailIdList,emailCcList, content);
//			
//		}
//		catch (Exception e)
//		{
//			e.printStackTrace();
//			return new ResponseEntity<Object>("Error occured : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//
//		}
//
//		return new ResponseEntity<Object>(distinctGroupID,HttpStatus.OK);

	}

}
