package in.co.greenwave.mail.dao;

import java.util.List;

public interface MailDAO {


	boolean sendEmailNotifications(String subject, List<String> toMailIds, List<String> mailCcList, String htmlContent);

}
