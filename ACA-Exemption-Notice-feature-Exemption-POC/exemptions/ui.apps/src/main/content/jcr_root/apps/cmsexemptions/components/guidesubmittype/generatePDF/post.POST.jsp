<%------------------------------------------------------------------------
 ~
 ~ ADOBE CONFIDENTIAL
 ~ __________________
 ~
 ~  Copyright 2014 Adobe Systems Incorporated
 ~  All Rights Reserved.
 ~
 ~ NOTICE:  All information contained herein is, and remains
 ~ the property of Adobe Systems Incorporated and its suppliers,
 ~ if any.  The intellectual and technical concepts contained
 ~ herein are proprietary to Adobe Systems Incorporated and its
 ~ suppliers and may be covered by U.S. and Foreign Patents,
 ~ patents in process, and are protected by trade secret or copyright law.
 ~ Dissemination of this information or reproduction of this material
 ~ is strictly forbidden unless prior written permission is obtained
 ~ from Adobe Systems Incorporated.
 --------------------------------------------------------------------------%>

<%@include file="/libs/fd/af/components/guidesglobal.jsp" %>
<%@page import="com.adobe.aemds.guide.servlet.GuideSubmitServlet,
     			org.apache.sling.api.resource.ResourceUtil,
                org.apache.sling.commons.json.JSONObject,
                org.slf4j.Logger,
                org.slf4j.LoggerFactory" %>

<%@ page import="com.adobe.aemds.guide.utils.GuideUtils" %>
<%@ page import="com.adobe.forms.common.submitutils.CustomResponse" %>

<%@ page import="com.adobe.cms.exemptions.core.models.adaptiveforms.GeneratePDF" %>
<%!
    private final Logger log = LoggerFactory.getLogger(getClass());

%>


<%@taglib prefix="sling" uri="http://sling.apache.org/taglibs/sling/1.0" %>
<%
%>
<%@taglib prefix="cq" uri="http://www.day.com/taglibs/cq/1.0" %>
<sling:defineObjects/>
<%
    //Create Params here
    /*Map<String,String> redirectParameters = new HashMap<String, String>();
    if(slingRequest.getParameter("_guideValuesMap")!=null) {
        JSONObject guideValueMap = new JSONObject(slingRequest.getParameter("_guideValuesMap"));
        redirectParameters = GuideSubmitServlet.getRedirectParameters(slingRequest);
        if(redirectParameters==null) {
            redirectParameters = new HashMap<String, String>();
        }

    }*/


   /*
    * PDF Generation Service can be accessed through the Service interface but in this realm, we have no way of returning the resulting document.
    * For that reason, the client is simply forwarded to a generation servlet by forwarding the request containing the data. The PDF is then created
    * from within the servlet and buffered out to screen.
    */

    //Get Submission Properties
	final ValueMap props = ResourceUtil.getValueMap(resource);

	//Load Service Interface
    com.adobe.cms.exemptions.core.models.adaptiveforms.GeneratePDF servlet = sling.getService(com.adobe.cms.exemptions.core.models.adaptiveforms.GeneratePDF.class);

	//Collect Property Values
	String templatePath = props.get("templatePath", (String)null);
	String renderAction = props.get("renderAction", (String)null);
	String readerExtend = props.get("readerExtend", (String)null);

	//Set values inside request
	slingRequest.setAttribute(servlet.TEMPLATE_PATH, templatePath);
	slingRequest.setAttribute(servlet.RENDER_ACTION, renderAction);
	slingRequest.setAttribute(servlet.READER_EXTEND, readerExtend);

	//Forward request to servlet
	GuideSubmitServlet.setForwardPath(slingRequest, servlet.CONTEXT_PATH, "", "");     


%>
