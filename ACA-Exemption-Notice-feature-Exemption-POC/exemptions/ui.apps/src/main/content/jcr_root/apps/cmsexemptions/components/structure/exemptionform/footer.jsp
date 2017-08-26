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
<%@ page import="java.util.Calendar" %><%
    String guideContainerPath = GuideELUtils.getGuideContainerPath(slingRequest, resource);
    Resource guideContainerResource = resourceResolver.resolve(guideContainerPath);
    String lang = GuideELUtils.getLocale(slingRequest, guideContainerResource);
    // We want these strings from the i18n/wcm/core and not from our bundle
    // These were extracted by the aemds job as we are using i18n.get here
    I18n i18n = new I18n(slingRequest.getResourceBundle(new Locale(lang)));
    int year = Calendar.getInstance().get(Calendar.YEAR);
%>

<%-- bring in js (matching CSS call in library.jsp) --%>
<cq:includeClientLib js="global-assets.aca"/>


<footer class="footer-small" role="complementary">
	<h2 class="accessibility">Footer</h2>
	<div class="container">
		<div class="hidden-xs">
			<div class="row">
				<div class="col-sm-5 col-lg-3"
					style="font-size: 10px; line-height: 14px;">
					<a title="HHS.gov" class="icon eagle" href="http://www.hhs.gov/">
						HHS.gov </a> <span>A federal government website managed by the
						U.S. Centers for Medicare &amp; Medicaid Services. 7500 Security
						Boulevard, Baltimore, MD 21244</span>
				</div>
				<div class="col-sm-5 pull-right">
					<ul class="list-inline pull-right">
						<li><a title="Whitehouse.gov" class="icon whitehouse"
							href="http://www.whitehouse.gov/"> Whitehouse.gov </a></li>
						<li><a title="USA.gov" class="icon usagov"
							href="http://www.usa.gov/"> USA.gov </a></li>
					</ul>
				</div>
				<div class="clearfix"></div>
			</div>
		</div>
	</div>
</footer>