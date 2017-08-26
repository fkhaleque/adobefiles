package gov.hhs.cms.aca.global_assets.core.models.workflow;

public class WorkflowConstants {


	/* workflow metadata keys */
	public static final String METADATA_PP_RECORD = "record";
	public static final String METADATA_PP_HEADER = "header";
	public static final String METADATA_PP_FILENAME = "ppfilename";
	public static final String METADATA_BATCH_ID = "batchid";
	public static final String METADATA_NOTICE_TYPE_ID = "noticetypeid";
	public static final String METADATA_SOURCE_ROW = "sourcerow";
	public static final String METADATA_PP_EXTENSION = "extension";
	public static final String METADATA_RECORD_XML = "recordxml";
	public static final String METADATA_CMRECORD_XML = "cmRecordxml";
	public static final String METADATA_RECORD_TEMPLATE = "recordtemplate";
	public static final String METADATA_ERROR_DESCRIPTION = "errordescription";
	public static final String METADATA_NPC_RESPONSE = "npcResponse";
	public static final String METADATA_NOTICE_TYPE_VO = "noticeTypeVo";

	public static final String METADATA_RESULT_VALID_XML = "validxml";
	public static final String METADATA_IS_ERROR = "iserror";
	public static final String METADATA_XML_FILENAME = "xmlfilename";

	public static final String METADATA_SERIALIZABLE_DOCUMENT_PAYLOAD = "recordpayload";

	/* General */
	public static final String EMPTY_VALUE = "empty";
	public static final String ADHOC_APPLICATIONNAME = "AdHoc";

	/* AssetService */
	public static final int DAM_RETRIES = 10;
	public static final int UPDATE_INTERVAL = 10;

	/* Workflow */
	public static final String BATCHID_WORKFLOW_PARAM = "batchId";
	public static final String BATCHINFOVO_WORKFLOW_PARAM = "batchInfoVo";
	public static final String CONTENTMETAVO_WORKFLOW_PARAM = "contentMetaVo";
	public static final String BATCHNOTICEGENERATIONVO_WORKFLOW_PARAM = "batchNoticeGenerationVo";

	public static final String JCRPATH_PAYLOAD_WORKFLOW_PARAM = "JCR_PATH";
	public static final String JCRUUID_PAYLOAD_WORKFLOW_PARAM = "JCR_UUID";
	public static final String BINARY_PAYLOAD_WORKFLOW_PARAM = "BINARY";
	public static final String JAVAOBJECT_PAYLOAD_WORKFLOW_PARAM = "JAVA_OBJECT";
		
}
