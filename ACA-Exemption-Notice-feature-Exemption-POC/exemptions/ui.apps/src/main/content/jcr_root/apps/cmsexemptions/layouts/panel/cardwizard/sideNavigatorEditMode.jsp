<%@include file="/libs/fd/af/components/guidesglobal.jsp"%>
<%@ page import="com.adobe.aemds.guide.utils.StyleUtils" %>
<%-- navigation tabs --%>
<ul id="${guidePanel.id}_guide-item-nav-container" class="tab-navigators tab-navigators-vertical in"
    data-guide-panel-edit="reorderItems" role="tablist">
    <c:forEach items="${guidePanel.items}" var="panelItem">
        <c:set var="isNestedLayout" value="${guide:hasNestablePanelLayout(guidePanel,panelItem)}"/>
        <li id="${panelItem.id}_guide-item-nav" title="${guide:encodeForHtmlAttr(panelItem.navTitle,xssAPI)}" data-path="${panelItem.path}" role="tab" aria-controls="${panelItem.id}_guide-item">
            <c:set var="panelItemCss" value="${panelItem.cssClassName}"/>
            <% String panelItemCss = (String) pageContext.getAttribute("panelItemCss");%>
            <a data-guide-toggle="tab" class="<%= StyleUtils.addPostfixToClasses(panelItemCss, "_nav") %> guideNavIcon nested_${isNestedLayout}">${guide:encodeForHtml(panelItem.navTitle,xssAPI)}</a>
            <c:if test="${isNestedLayout}">
                <guide:initializeBean name="guidePanel" className="com.adobe.aemds.guide.common.GuidePanel"
                    resourcePath="${panelItem.path}" restoreOnExit="true">
                    <sling:include path="${panelItem.path}"
                                   resourceType="/apps/cmsexemptions/layouts/cardwizard/defaultNavigatorLayout.jsp"/>
                </guide:initializeBean>
            </c:if>
            <em></em>
        </li>
    </c:forEach>
</ul>
