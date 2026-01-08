package in.co.greenwave.mail.dao.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import in.co.greenwave.mail.dao.MailDAO;



 
 
public class SQLServerDAOFactory extends DAOFactory {

	@Autowired
	private MailDAO mailService;
	
	
	@Override
	public MailDAO getMailService()
	{
		return mailService;
	}
 

 
}