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
 
<%@include file="/libs/fd/af/components/guidesglobal.jsp"%>
<div id="${guidePanel.id}_guide-item-container">
    <c:if test="${guidePanel.hasToolbar  && guidePanel.toolbarPosition == 'Top'}">
        <sling:include path="${guidePanel.toolbar.path}"/>
    </c:if>
<c:forEach items="${guidePanel.items}" var="panelItem">
    <div id="${panelItem.id}_guide-item" role="tabpanel">
    
	    <c:set var="isNestedLayout" value="${guide:hasNestablePanelLayout(guidePanel,panelItem)}"/>
        <c:if test="${isNestedLayout}">
            <c:set var="guidePanelResourceType" value="/apps/cmsexemptipns/layouts/panel/cardwizard/panelContainer.jsp" scope="request"/>
        </c:if>
        
        <sling:include path="${panelItem.path}" resourceType="${panelItem.resourceType}"/>
    </div>
</c:forEach>

    <c:if test="${guidePanel.hasToolbar  && guidePanel.toolbarPosition == 'Bottom'}">
        <sling:include path="${guidePanel.toolbar.path}"/>
    </c:if>
</div>
