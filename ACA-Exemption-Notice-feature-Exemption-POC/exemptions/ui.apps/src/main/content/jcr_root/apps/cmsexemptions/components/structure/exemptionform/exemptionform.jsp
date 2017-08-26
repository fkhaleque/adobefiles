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
<%@page session="false" %>
<%
	String guideContainerPath = GuideELUtils.getGuideContainerPath(slingRequest, resource);
	Resource guideContainerResource = resourceResolver.resolve(guideContainerPath);
	String lang = GuideELUtils.getLocale(slingRequest, guideContainerResource);
%>
<!DOCTYPE html>
<html lang="<%= lang %>">

	<%-- THESE GIT INCLUDES WILL BE AUTOMAGICALLY STAMPED BY MAVEN DURING BUILD --%>
	<%-- ${git.build.host} --%>
	<%-- ${git.closest.tag.name} --%>
	<%-- ${git.commit.id.describe-short} --%>
	<%-- ${git.build.version} --%>
	<%-- ${git.commit.id.describe} --%>
	<%-- ${git.commit.id} --%>
	<!-- ${git.commit.id.abbrev} -->
	<%-- ${git.branch} --%>
	<%-- ${git.tags} --%>
	<%-- ${git.build.time} --%>

    <cq:include script="head.jsp"/>
    <cq:include script="body.jsp"/>

</html>

