package gov.hhs.cms.aca.global_assets.core.models.workflow;

import gov.hhs.cms.aca.global_assets.core.commons.Primitives;
import gov.hhs.cms.aca.global_assets.core.commons.SlingHelpersImpl;
import gov.hhs.cms.aca.global_assets.core.models.notifications.EmailServiceImpl;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.jcr.Session;
import javax.mail.util.ByteArrayDataSource;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.framework.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.aemfd.docmanager.Document;
import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.HistoryItem;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.day.cq.mailer.MailingException;


@Component
@Service

@Properties({
	@Property(name = Constants.SERVICE_DESCRIPTION, value = "ACA Global Assets Email Payload Workflow Implementation."),
	@Property(name = Constants.SERVICE_VENDOR, value = "Adobe"),
	@Property(name = "process.label", value = "ACA Global Assets Email Payload Workflow Step")})

/**
 * This implementation class overrides the WorkflowProcess service in order to create a custom Worflow step which implements 
 * our custom WorkflowContext service.
 * 
 * @author  Guillaume (William) Clement
 *
 */
public class EmailPayloadWorkflowStepModel implements WorkflowProcess {

	private static final Logger log = LoggerFactory.getLogger(EmailPayloadWorkflowStepModel.class);

	private final static String emailToParam = "emailTo";
	private final static String emailCcParam = "emailCc";
	private final static String emailFromParam = "emailFrom";
	private final static String emailSubjectParam = "emailSubject";
	private final static String emailMsgParam = "emailMsg";

	private final static String emptyValue = "empty";

	/**
	 * The method called by the AEM Workflow Engine to perform Workflow work. It overrides the default execute() method.
	 *
	 * @param workItem the work item representing the resource moving through the Workflow
	 * @param workflowSession the workflow session
	 * @param args arguments for this Workflow Process defined on the Workflow Model (PROCESS_ARGS, argSingle, argMulti)
	 * @return void
	 * @throws WorkflowException when the Workflow Process step cannot complete. This will cause the WF to retry.
	 * @author Guillaume (William) Clement
	 */
	@Override
	public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap args) throws WorkflowException {

		try {
			log.debug("ACA Global Assets - Custom EmailPayloadWorkflowStepModel invoked.");

			final Map<String,String> arguments = WorkflowHelpers.parseWorkflowArgs(args, emptyValue);

			//Set up the Email message
			final MultiPartEmail email = new MultiPartEmail();

			//Set the email values
			if(arguments.containsKey(emailToParam) && (!arguments.get(emailToParam).equals(emptyValue))){
				final String emailToRecipients = arguments.get(emailToParam);
				email.addTo(emailToRecipients);
			}
			if(arguments.containsKey(emailCcParam) && (!arguments.get(emailCcParam).equals(emptyValue))){
				final String emailCcRecipients = arguments.get(emailCcParam);
				email.addCc(emailCcRecipients);
			}
			if(arguments.containsKey(emailFromParam) && (!arguments.get(emailFromParam).equals(emptyValue))){
				final String emailFrom = arguments.get(emailFromParam);
				email.setFrom(emailFrom);
			}
			if(arguments.containsKey(emailSubjectParam) && (!arguments.get(emailSubjectParam).equals(emptyValue))){
				final String emailSubject = arguments.get(emailSubjectParam);
				email.setSubject(emailSubject);
			}
			if(arguments.containsKey(emailMsgParam) && (!arguments.get(emailMsgParam).equals(emptyValue))){
				String emailMsg = arguments.get(emailMsgParam);

				final String commentSep = "=============================";
				final String lineBreak = System.lineSeparator();

				//Add workflow comments to email body
				final List<HistoryItem> comments = WorkflowHelpers.retrieveComments(workflowSession, workItem);
				final String startComment = WorkflowHelpers.getPersistedData(workItem, "startComment", "empty");

				if(!startComment.equals("empty")){				
					emailMsg += lineBreak + commentSep + lineBreak
							+ "Date: " + WorkflowHelpers.retrieveHistory(workflowSession, 
									workItem)
									.get(0)
									.getDate()
									.toLocaleString() + lineBreak
							+ "Commenter: " + WorkflowHelpers.retrieveHistory(workflowSession, workItem).get(0).getUserId() + lineBreak
							+ "Start Comment: " + startComment + lineBreak
							+ commentSep;
				}

				if(!comments.isEmpty()){
					StringBuffer stringBuffer = new StringBuffer();
					
					for(int i=0;i<comments.size();i++){
						stringBuffer.append(lineBreak + commentSep + lineBreak
								+ "Date: " + comments.get(i).getDate().toLocaleString() + lineBreak
								+ "Commenter: " + comments.get(i).getUserId() + lineBreak
								+ "Comment: " + comments.get(i).getComment() + lineBreak
								+ commentSep);	
					}
					emailMsg = emailMsg.concat(stringBuffer.toString());
				}

				email.setMsg(emailMsg);
			}	    

			//Get ResourceResolver from Workflow Session
			final Session session = workflowSession.adaptTo(Session.class);
			final ResourceResolver resolver = SlingHelpersImpl.getResourceResolver(session);

			//Get payload
			final String payload = workItem.getWorkflowData().getPayload().toString();

			//Load payload into streamable document
			final Document attach = new Document(payload, resolver);
			
			InputStream attachStream = null;
			ByteArrayDataSource dataSource = null;
			
			try{
				//Buffer the document into an email attachable BytArrayDataSource object
				attachStream = attach.getInputStream();
				dataSource = new ByteArrayDataSource(attachStream, attach.getContentType());
			} finally{
				if(!Primitives.isNull(attachStream)){
					attachStream.close();
				}
			} 

			//Extract filename
			final String attachName = payload.substring(payload.lastIndexOf("/"), payload.length());

			//Attach the data source to the email
			email.attach(dataSource, attachName, "Approved Letter", Email.ATTACHMENTS); 

			//Avoid document leaks
			attach.dispose();

			final EmailServiceImpl emailService = new EmailServiceImpl();

			emailService.sendEmail(email);
		} catch (MailingException e) {
			log.error("A MailingException has been thrown while sending the requested email", e);
			throw new WorkflowException("A MailingException has been thrown while sending the requested email", e);
		} catch (EmailException e) {
			log.error("A EmailException has been thrown while creating the requested email", e);
			throw new WorkflowException("A EmailException has been thrown while creating the requested email", e);
		} catch (LoginException e) {
			log.error("A LoginException has been thrown while aquiring a ResourceResolver", e);
			throw new WorkflowException("A LoginException has been thrown while aquiring a ResourceResolver", e);
		} catch (IOException e) {
			log.error("A IOException has been thrown while reading the requested email attachment", e);
			throw new WorkflowException("A IOException has been thrown while reading the requested email attachment", e);
		}
	}
}
