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
<%@ page import="com.adobe.aemds.guide.utils.StyleUtils" %>
<div class="col-md-1 col-sm-1 guide-tab-scroller guide-tab-scroller-previous hidden-xs"
    data-guide-nav-scroll="left">
</div>
<div class="col-md-10 col-sm-10">
    <%-- navigation tabs --%>
    <ol id="${guidePanel.id}_guide-item-nav-container"
        class="wizard-navigators ${guideLayoutContext.layoutNavigatorClasses} hidden-xs"
        data-guide-panel-edit="reorderItems" role="tablist">
    	<c:set var="liWidth" value="23"/>
    	<c:set var="liItemsLength" value="${fn:length(guidePanel.items)}"/>
		<c:if test="${liItemsLength > 0 && (94 / liItemsLength) > liWidth}">
            <c:set var="liWidth" value="${94 / liItemsLength}"/>
        </c:if>
        <c:forEach items="${guidePanel.items}" var="panelItem">
            <li id="${panelItem.id}_guide-item-nav" title="${guide:encodeForHtmlAttr(panelItem.navTitle,xssAPI)}" data-path="${panelItem.path}" role="tab" aria-controls="${panelItem.id}_guide-item"
            style="width:${liWidth}%;">
                <c:set var="panelItemCss" value="${panelItem.cssClassName}"/>
                <% String panelItemCss = (String) pageContext.getAttribute("panelItemCss");%>
                <a
                    <c:if test="${isEditMode}">
                        data-guide-toggle="tab"
                    </c:if>
                    class="<%= StyleUtils.addPostfixToClasses(panelItemCss, "_nav") %> guideNavIcon">
                    <span>${guide:encodeForHtml(panelItem.navTitle,xssAPI)}</span>
                </a>
            <div class="progress">
                  <div class="progress-bar progress-bar-warning" role="progressbar" aria-valuenow="0"
                       aria-valuemin="0" aria-valuemax="100" style="width: 0%;">
                      <span class="sr-only">0% Complete</span>
                    </div>
                 </div>
            <div class="indicator"></div>
            </li>
        </c:forEach>
    </ol>
</div>
<div class="col-md-1 col-sm-1 guide-tab-scroller guide-tab-scroller-next hidden-xs"
    data-guide-nav-scroll="right">
</div>
