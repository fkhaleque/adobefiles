package gov.hhs.cms.aca.global_assets.core.models.notifications;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.MultiPartEmail;

import com.day.cq.mailer.MailingException;

public interface EmailService {
	
	public abstract void sendEmail(MultiPartEmail email) throws MailingException;
	
	public abstract void sendEmail(Email email) throws MailingException;

}