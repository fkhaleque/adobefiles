package gov.hhs.cms.aca.global_assets.core.models.workflow;

import gov.hhs.cms.aca.global_assets.core.commons.Primitives;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.HistoryItem;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowData;
import com.adobe.granite.workflow.metadata.MetaDataMap;

public class WorkflowHelpers {

	/**
	 * Helper method to retrieve Workflow comments
	 * 
	 * @param workflowSession
	 * @param workItem
	 * @throws WorkflowException
	 */
	public static List<HistoryItem> retrieveComments(WorkflowSession workflowSession, WorkItem workItem) throws WorkflowException{

		final List<HistoryItem> history = workflowSession.getHistory(workItem.getWorkflow());
		final List<HistoryItem> comments = new ArrayList<>();

		if (history.size() > 0) {
			
			String lastComment = "";
			HistoryItem current;

			for(int i=0;i<history.size();i++){

				current = history.get(i);

				//Check for empty commments
				if(!Primitives.isNull(current) && ((!current.getComment().equals("")) || (!current.getComment().isEmpty()))){
					
					if(lastComment.equals("")){
						comments.add(current);
					}
					//The workflow engine duplicates comments, we do not want to list them
					else if(!lastComment.equals(current.getComment())){
						comments.add(current);
					}
					
					lastComment = current.getComment();
				}
			}
		}

		return comments;
	}

	/**
	 * Helper method to retrieve Workflow comments
	 * 
	 * @param workflowSession
	 * @param workItem
	 * @throws WorkflowException
	 */
	public static List<HistoryItem> retrieveHistory(WorkflowSession workflowSession, WorkItem workItem) throws WorkflowException{
		return workflowSession.getHistory(workItem.getWorkflow());
	}

	
	/**
	 * Helper method to persist workflow data key pair
	 * 
	 * @param workItem
	 * @param workflowSession
	 * @param key
	 * @param val
	 * @return boolean
	 */
	public static <T> boolean persistData(WorkItem workItem, WorkflowSession workflowSession, String key, T val) {
		WorkflowData data = workItem.getWorkflow().getWorkflowData();
		if (data.getMetaDataMap() == null) {
			return false;
		}

		data.getMetaDataMap().put(key, val);
		workflowSession.updateWorkflowData(workItem.getWorkflow(), data);

		return true;
	}

	/**
	 * Helper method to retrieve persisted data from workflow based on key pair.
	 * 
	 * @param workItem
	 * @param key
	 * @param type
	 * @return map
	 */
	public static <T> T getPersistedData(WorkItem workItem, String key, Class<T> type) {
		MetaDataMap map = workItem.getWorkflow().getWorkflowData().getMetaDataMap();
		return map.get(key, type);
	}

	/**
	 * Helper method to retrieve persisted data from workflow based on default value.
	 * 
	 * @param workItem
	 * @param key
	 * @param defaultValue
	 * @return map
	 */
	public static <T> T getPersistedData(WorkItem workItem, String key, T defaultValue) {
		MetaDataMap map = workItem.getWorkflow().getWorkflowData().getMetaDataMap();
		return map.get(key, defaultValue);
	}

	/**
	 * Helper method to parse standard workflow step arguments provided in the following fashion:
	 * argument1=value1,argument2=value2
	 * 
	 * @param args
	 * @param argsEmptyValue
	 * @return values
	 */
	public static Map<String,String> parseWorkflowArgs(MetaDataMap args, String argsEmptyValue){

		Map<String, String> values = new HashMap<String, String>();

		String argument = args.get("PROCESS_ARGS", argsEmptyValue);
		String [] keyValues;
		
		if(!argument.equals(argsEmptyValue)){
			String[] arguments = argument.split(",");

			for(int i=0;i<arguments.length;i++){

				keyValues = arguments[i].split("=");

				values.put(keyValues[0], keyValues[1]);
			}	
		}
		return values;
	}

}
