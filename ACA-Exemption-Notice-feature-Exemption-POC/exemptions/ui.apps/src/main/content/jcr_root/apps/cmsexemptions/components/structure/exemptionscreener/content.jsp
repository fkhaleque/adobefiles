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
<div class="guideBody">
	<%-- 
    <div class="guideGlobalErrors" data-guide-globalerrors style="display: none"></div>
    --%>

	<div class="container" style="position:relative;">
		<div class="row" style="position:relative;">
    		<div id="topErrors" class="col-md-12 clearfix hidden">
    			<h3 id="topErrorTitle"></h3>
				<ul id="topErrorList"></ul>
			</div>
    	</div>
	</div>

	<%--
	<div class="lite-card">
		<div class="lite-card-inner">
		 --%>
			<guide:includeGuideContainer/>
			<%--
		</div>
	</div>
	 --%>
</div>
