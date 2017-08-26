package gov.hhs.cms.aca.global_assets.core.models.workflow;

import java.util.Map;

import javax.jcr.Session;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
// import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.granite.workflow.WorkflowException;
// import com.adobe.granite.workflow.WorkflowService;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.Workflow;
import com.adobe.granite.workflow.exec.WorkflowData;
import com.adobe.granite.workflow.model.WorkflowModel;

import gov.hhs.cms.aca.global_assets.core.commons.SlingHelpersImpl;

@Component(immediate = true, label = "ACA Batch Notice - WorkflowInvokerImpl", description = "ACA Batch Notice - WorkflowInvokerImpl")
@Service
public class WorkflowInvokerImpl implements WorkflowInvoker {

//	@Reference
//	private static WorkflowService workflowService;

	private static final Logger log = LoggerFactory.getLogger(WorkflowInvokerImpl.class);

	/* (non-Javadoc)
	 * @see gov.hhs.cms.aca.batch_notice_processing.core.models.workflow.WorkflowInvoker#startWorkflow(java.lang.String, java.lang.Object)
	 */
//	@Override
//	public void startWorkflow(String workflowName, String payloadPath, Session session) throws LoginException, WorkflowException{
//
//		final ResourceResolver resourceResolver = SlingHelpersImpl.getResourceResolver("aca-service");
//		startWorkflow(workflowName, payloadPath,resourceResolver.adaptTo(WorkflowSession.class) );
//	}

//	@Override
//	public void startWorkflow(String workflowName, String payloadPath, WorkflowSession session) throws LoginException, WorkflowException{
//
//		log.debug("startWorkflow(name,path,sess): " + workflowName);
//
//		//Create a workflow session 
//		// final WorkflowSession wfSession = initiateSession(session);
//
//		// Get the workflow model
//		final WorkflowModel wfModel = initiateModel(workflowName, session);
//
//		// Get the workflow data
//		// The first param in the newWorkflowData method is the payloadType.  Just a fancy name to let it know what type of workflow it is working with.
//		final WorkflowData wfData = session.newWorkflowData(WorkflowConstants.JCRPATH_PAYLOAD_WORKFLOW_PARAM, payloadPath);
//
//		// Invoke the Workflow
//		session.startWorkflow(wfModel, wfData);	
//	}

	/* (non-Javadoc)
	 * @see gov.hhs.cms.aca.batch_notice_processing.core.models.workflow.WorkflowInvoker#startWorkflow(java.lang.String, java.lang.Object, java.lang.Object)
	 */
	@Override
	public Workflow startWorkflow(String workflowName, WorkflowData wfData, WorkflowSession wfSession, Map<String,Object> metaData) throws LoginException, WorkflowException{

		log.debug("startWorkflow(name,data,sess,map): " + workflowName);
		WorkflowModel wfModel = initiateModel(workflowName, wfSession);
		
		// Invoke the Workflow
		Workflow workflow =  wfSession.startWorkflow(wfModel, wfData, metaData);
		
		return workflow;
	}

	@Override
	public WorkflowModel initiateModel(String workflowName, WorkflowSession wfSession) throws WorkflowException {
		//Get the workflow model
		WorkflowModel wfModel = wfSession.getModel(workflowName);
		
		if(null == wfModel){
			throw new NullPointerException("Workflow Model could not be fetched. Please verify user permissions and/or model ID.");    	
		}
		
		return wfModel;
	}

	@Override
	public WorkflowSession initiateSession(Session session) throws LoginException {

		
//		var resolver = sling.getRequest().getResource().getResourceResolver();
//		var wfSession = resolver.adaptTo(Packages.com.adobe.granite.workflow.WorkflowSession);
		//Invoke the adaptTo method to create a Session

		// if(session == null){
			//Use service account session if one isn't provided.
			final ResourceResolver resourceResolver = SlingHelpersImpl.getResourceResolver("aca-service");
			return resourceResolver.adaptTo(WorkflowSession.class);
		// } 
		
		//Create a workflow session 
		// return session; // workflowService.getWorkflowSession(session);

	}
	
	@Override
	public WorkflowSession initiateSession() throws LoginException {
		return initiateSession(null);
	}

	/*
	 * Binding Methods
	 */	

	@Activate
	protected void activate() {
		log.info("[Service Activated]");
	}

	@Deactivate
	protected void deactivate() {
		log.info("[Service Deactivated]");
	}

//	protected void bindWorkflowService(WorkflowService injected){
//		log.info("[Binding WorkflowService]");
//		workflowService = injected;
//		log.info("[Bound WorkflowService]");
//	}

}
