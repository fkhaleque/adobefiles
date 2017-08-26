package gov.hhs.cms.aca.global_assets.core.models.workflow;

import java.util.Map;

import javax.jcr.Session;

import org.apache.sling.api.resource.LoginException;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.Workflow;
import com.adobe.granite.workflow.exec.WorkflowData;
import com.adobe.granite.workflow.model.WorkflowModel;

public interface WorkflowInvoker {

	/**
	 * A method used for programatic workflow model invocation.
	 * 
	 * @param workflowName
	 * @param payloadPath
	 * @param session (automatically created using aca-service if null)
	 * @throws LoginException
	 * @throws WorkflowException 
	 */
//	public abstract void startWorkflow(String workflowName,
//			String payloadPath, WorkflowSession session) throws LoginException, WorkflowException;

//	public abstract void startWorkflow(String workflowName, String payloadPath, Session session) throws LoginException, WorkflowException;
	

	/**
	 * A method used for programatic workflow model invocation.
	 *  
	 * @param workflowName
	 * @param wfData
	 * @param session
	 * @throws LoginException
	 * @throws WorkflowException
	 */
	public abstract Workflow startWorkflow(String workflowName, WorkflowData wfData,
			WorkflowSession wfSession, Map<String,Object> metaData) throws LoginException, WorkflowException;

	/**
	 * A method used to retrieve a Workflow Model from the Workflow Service
	 * 
	 * @param workflowName
	 * @param wfSession
	 * @return
	 * @throws WorkflowException
	 */
	public abstract WorkflowModel initiateModel(String workflowName, WorkflowSession wfSession)
			throws WorkflowException;

	/**
	 * A method used to retrieve a Workflow Session from the Workflow Service
	 * @param session
	 * @return
	 * @throws LoginException
	 */
	public abstract WorkflowSession initiateSession(Session session) throws LoginException;

	/**
	 * A convenience method used to retrieve a Workflow Session from the Workflow Service using a default service account
	 * @param session
	 * @return
	 * @throws LoginException
	 */
	public abstract WorkflowSession initiateSession() throws LoginException;

}