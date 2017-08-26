package gov.hhs.cms.aca.global_assets.core.models.notifications;

import java.util.Map;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.MultiPartEmail;
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.mailer.MailingException;
import com.day.cq.mailer.MessageGateway;
import com.day.cq.mailer.MessageGatewayService;

@Component(immediate = true, label = "ACA Global Assets - EmailServiceImpl", description = "ACA Global Assets - EmailServiceImpl")
@Service
public class EmailServiceImpl implements EmailService{

	private static final Logger log = LoggerFactory.getLogger(EmailServiceImpl.class);
	@SuppressWarnings("unused")
	private static Map<String, Object> cachedConfig;

	@Reference private static MessageGatewayService messageGatewayService;

	public void sendEmail(MultiPartEmail email) throws MailingException{

		//Declare a MessageGateway service
		final MessageGateway<MultiPartEmail> messageGateway; 

		//Inject a MessageGateway Service and send the message
		messageGateway = messageGatewayService.getGateway(MultiPartEmail.class);

		if(messageGateway == null){
			log.warn("Unable to aquire Message Gateway. Verify that Day CQ Mail Service is configured.");
			throw new MailingException("Unable to aquire Message Gateway. Verify that Day CQ Mail Service is configured.");
		}

		// Check the logs to see that messageGateway is not null
		messageGateway.send(email);
	}

	public void sendEmail(Email email) throws MailingException{

		//Declare a MessageGateway service
		final MessageGateway<Email> messageGateway; 

		//Inject a MessageGateway Service and send the message
		messageGateway = messageGatewayService.getGateway(Email.class);

		if(messageGateway == null){
			log.warn("Unable to aquire Message Gateway. Verify that Day CQ Mail Service is configured.");
			throw new MailingException("Unable to aquire Message Gateway. Verify that Day CQ Mail Service is configured.");
		}

		// Check the logs to see that messageGateway is not null
		messageGateway.send(email);
	}

	/* Binding Methods */	

	@Activate
	protected void activate(final Map<String, Object> config) {
		cachedConfig = config;
		log.info("[Service Activated]");
	}

	@Deactivate
	protected void deactivate() {
		log.info("[Service Deactivated]");
	}

	protected void bindMessageGatewayService(MessageGatewayService injected){
		log.info("[Binding MessageGatewayService]");
		messageGatewayService = injected;
		log.info("[Bound MessageGatewayService]");
	}
}
