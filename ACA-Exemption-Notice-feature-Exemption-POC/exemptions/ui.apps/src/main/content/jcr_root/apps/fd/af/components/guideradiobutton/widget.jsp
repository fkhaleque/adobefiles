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
<div class="<%=GuideConstants.GUIDE_FIELD_RADIOBUTTONGROUP_ITEMS%>" style="${guide:encodeForHtmlAttr(guideField.styles,xssAPI)};${guide:encodeForHtmlAttr(guideField.widgetInlineStyles,xssAPI)}">
    <c:forEach items="${guideField.options}" var="option" varStatus="loopCounter">
        <div class="guideRadioButtonItem ${guideField.alignment}  ${guide:encodeForHtmlAttr(guideField.name,xssAPI)} ${guide:encodeForHtmlAttr(guideField.cssClassName,xssAPI)}">
            <div class="<%= GuideConstants.GUIDE_FIELD_WIDGET%> left" data-id="${loopCounter.count}">
                <input type="radio" id="${guideField.id}${'-'}${loopCounter.count}${"_widget"}"
                       name="${guide:encodeForHtmlAttr(guideField.name,xssAPI)}" value="${guide:encodeForHtmlAttr(option.key,xssAPI)}" ${option.key == guideField.value ? "checked" : ""} />
            </div>
            <div class="<%=GuideConstants.GUIDE_WIDGET_LABEL%> right">
                <label for="${guideField.id}${'-'}${loopCounter.count}${"_widget"}">${guide:encodeForHtml(option.value,xssAPI)}</label>
            </div>
        </div>
    </c:forEach>
</div>
