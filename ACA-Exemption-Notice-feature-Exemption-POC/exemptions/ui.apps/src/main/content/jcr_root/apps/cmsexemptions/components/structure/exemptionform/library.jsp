<%@include file="/libs/fd/af/components/guidesglobal.jsp"%>

<%-- TODO: Remove when gov't styles aren't breaking CQ.. --%>
<c:if test="${!isEditMode}">
	<cq:includeClientLib css="global-assets.aca, global-assets.common"/>
</c:if>
<cq:includeClientLib categories="cmsexemptions.theme.formtheme"/>
<cq:includeClientLib categories="cmsexemptions.controls"/>

<%-- Load common here so it's ready for AF, ACA in footer --%>
<cq:includeClientLib js="global-assets.common"/>
