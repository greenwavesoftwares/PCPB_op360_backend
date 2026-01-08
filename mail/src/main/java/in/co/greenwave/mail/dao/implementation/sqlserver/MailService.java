package in.co.greenwave.mail.dao.implementation.sqlserver;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Repository;

import in.co.greenwave.mail.dao.MailDAO;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
@Repository
public class MailService implements MailDAO {

//	@Autowired
//	@Qualifier("jdbcTemplate_OP360")
//	private  JdbcTemplate jdbcTemplate;
//	
//	@Autowired
//	@Qualifier("jdbcTemplate_OP360_Usermodule")
//	private  JdbcTemplate jdbcTemplate_userModule;
	
	@Autowired
	private JavaMailSender javaMailSender;
	
	@Value("${spring.mail.username}")
	private String fromEmailId; // the from mailId is defined in the application.properties file
	
	
	@Override //At a time only one activity to a job is relevant 
	public boolean sendEmailNotifications(String subject, List<String> toMailIds, List<String> mailCcList, String htmlContent) {
	    // Creating a new MimeMessage for sending emails
	    MimeMessage mimeMessage = javaMailSender.createMimeMessage();
	    MimeMessageHelper helper;
	    
	    try {
	        // Initializing MimeMessageHelper to handle the email's content and attachments
	        helper = new MimeMessageHelper(mimeMessage, true);

	        // Logging the sender's email address
	        System.out.println("From Email : " + fromEmailId);

	        // Setting the 'From' field with the sender's email address
	        helper.setFrom(fromEmailId);

	        // Setting the 'To' field with the list of recipient email addresses
	        helper.setTo(toMailIds.stream().toArray(String[]::new));

	        // Setting the email subject
	        helper.setSubject(subject);

	        // Setting the email priority (1 indicates high priority)
	        helper.setPriority(1);

	        // Setting the email body content, with 'true' indicating the content is HTML
	        helper.setText(htmlContent, true);

	        // If the CC list is not null and contains emails, add the CC recipients
	        if (mailCcList != null && !mailCcList.isEmpty()) {
	            helper.setCc(mailCcList.stream().toArray(String[]::new));
	        }

	        // Sending the email using JavaMailSender
	        javaMailSender.send(mimeMessage);
	        System.out.println("Mail Sent Successfully");

	    } catch (Exception e) {
	        // Handling any exceptions that occur during the email sending process
	        e.printStackTrace();
	        return false; // Return false if an error occurs
	    }

	    return true; // Return true if the email is successfully sent
	}


}
