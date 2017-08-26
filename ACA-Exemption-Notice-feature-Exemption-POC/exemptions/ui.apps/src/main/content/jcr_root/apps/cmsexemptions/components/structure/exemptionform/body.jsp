<%@include file="/libs/fd/af/components/guidesglobal.jsp"%>
<body id="hc-gov-assets" class="page ${currentPage.template.name}">
    <div id="wrapper" data-role="page">

		<cq:include script="header.jsp"/>
		
         <div class="title-container">
           <div class="row no-margin full">
             <div class="col-xs-12 col-md-offset-2 col-md-10">
             	<%--
             		TODO: replace with dynamic title
             	 --%>
               <h1><%= currentPage.getTitle() == null ? xssAPI.encodeForHTML(currentPage.getName()) : xssAPI.encodeForHTML(currentPage.getTitle()) %></h1>
             </div>
           </div>
         </div>
        
		<%-- <div id="main-body-content" class="main-body-content" role="main">  --%>
			<cq:include script="content.jsp"/>
 	
 		<%--
		</div>
		 --%>

		<%--
        <sly data-sly-include="partials/main.html" data-sly-unwrap/>
        <sly data-sly-include="partials/modals.html" data-sly-unwrap/>
        <sly data-sly-include="partials/footlibs.html" data-sly-unwrap/>
         --%>
         
         <cq:include script="footer.jsp"/>
         
    </div>
</body>