/**
 * @file CMS Site Custom AF JS 
 * @author Adobe LC-ACA-DEV@adobe.com
 * @version 0.1.0
 */

/**
 * Parent namespace for Adobe CMS Exemptions
 *
 * @namespace
 * @version 0.1.0
 * @since 0.1.0
 * @see http://www.adobe.com
 */
	
var cmsexemptions = cmsexemptions || {};

/**
 * AEM Exemptions Form Utilities.
 *
 * @namespace
 * @memberOf cmsexemptions
 * @version 0.1.0
 * @since 0.1.0
 */

cmsexemptions.formtheme = function() {

    /**
     * A stepCompletionHandler function which can execute
     * a panel completion expression in addition to
     * the regular guidebridge validation.
     * 
     * a new attribute "panelCompletionExpression" must be added to the panel node
     * in CRX.  the result of this expression must be an array of {som:, errorText:}
     * (which is what guideBridge.validate generates).  an empty array should
     * be returned if the expression validates without error.
     *
     * @memberOf cmsexemptions.formtheme
     * @param {String} somExpression of the current panel
     * @returns {Boolean} if panel validated correctly
     * @example cmsexemptions.formtheme.stepCompletionHandler(this.panel.navigationContext.currentItem.somExpression)
     * @since 0.1.0
     */
	var stepCompletionHandler = function(somExpression)
    {
    	

  		var thisPanel = window.guideBridge.resolveNode(somExpression);

  		// validate, but dont focus on error
  		var ret = window.guideBridge.validate([],somExpression, false) ;
  		
  		// trigger panel specific post-validate handler, if exist
  		$(thisPanel).trigger("postValidate", [ ret ] );
  		
  		return ret;
		
    };

    /**
     * Event handler to be called when adaptive form validates.
     * will show or hide the topErrors div with a populated UL
     * of all the form errors, if any.
     *
     * @memberOf cmsexemptions.formtheme
     * @returns {void}
     * @example validationCompleteTopErrorHandler();
     * @since 0.1.0
     */
    
    var displayErrorBox = function (visible){
    	var te = $('#topErrors');
		if (visible)
		{
			te.removeClass("hidden");
			te.attr("aria-hidden", false);
			te.attr("tabindex",0);
		} else {
			te.addClass("hidden");
			te.attr("aria-hidden",true);
			te.attr("tabindex",-1);
		}
    };
    
	var validationCompleteTopErrorHandler = function(event,data) {
		
    	// show or hide top error box if we have errors
		displayErrorBox(data.newText.length>0);

    	// if errors, populate LI with errors
    	if (data.newText.length>0)
    	{
    		$('#topErrorTitle').text("Your information contains " + data.newText.length + " error(s).");

    		var errList = $('#topErrorList');
    		errList.empty();
    		
    		$.each(data.newText, function(i)
	    	{
    			// console.log("error in: " + data.newText[i].som);
    			
    			$('<a />', {
    			    text: data.newText[i].errorText,
    			    href: "",
    			    tabindex: "0",
    			    click: function(event) {
    			    	event.preventDefault();
    			    	window.guideBridge.setFocus(data.newText[i].som,"firstItem");
    			    },
    				keyup: function(event){
    					if(event.keyCode == 13 || event.keyCode == 32)
    					{
    						this.click();
    					}
    				}
    			}).wrap('<li/>').parent().appendTo(errList);
	    	    
	    	});
    		
    		if (data.newText.length==1)
    		{
    			// only one error, goto that field
		    	window.guideBridge.setFocus(data.newText[0].som,"firstItem");
    		} else {
    			// focus the error list
    			$('html, body').animate({scrollTop : 0},500);
    			$('#topErrors').focus();
    		}

    	}
	},
	

	  /**
     *  Displays Alert box with guide XML in it
     *  @param {GuideBridge} guide bridge object
     *  @return {void}
     *  @memberOf cmsexemptions.formtheme
     *  @since 0.1.0
     */
    alertGuideXML = function(gb) {
        
    	gb.getDataXML(
		{
			success: function(guideResultObject) { alert(guideResultObject.data); },
			error:function(guideResultObject) { alert("Couldn't retrieve data."); },
			context: this,
			async: true
		});

     };

	  /**
      *  Fired when af field values change; if
      *  named field changes, fire appropriate field
      *  handler function.
      *  
      *  @param {evt} Event object
      *  @param {fld} Field affected by event
      *  @return {void}
      *  @memberOf cmsexemptions.formtheme
      *  @since 0.1.0
      */
     elementValueChangedHandler = function(evt,fld)
     {
		
		// console.log(fld.target.somExpression + " -> " + fld.target.value);
		
    	if (fld.target.className == "guideRadioButton")
		{

			var opts = $(guideBridge._getGuideDomElement(fld.target.somExpression));
			
			// turn all options off
			opts.find(".guideRadioButtonItem").toggleClass("selected", false);
			
			// find selected radio, turn on highlight
			opts.find("input:checked").closest(".guideRadioButtonItem").toggleClass("selected",true);

			
		} else if (fld.target.className == "guideCheckBox") {

			// find checkbox and toggle selected attribute
			$(guideBridge._getGuideDomElement(fld.target.somExpression)).find(".guideCheckBoxItem").toggleClass("selected", (fld.newText!=null));


		}

     };

     /**
      *  Gets the full names of all dependents
      *  concatenated together, separated by a comma
      *
      *  Requires that the Person2 Panel is located at
      *  guide[0].guide1[0].guideRootPanel[0].Person2[0]
      *
      *  @return {String} Dependent Names
      *  @memberOf cmsexemptions.formtheme
      *  @since 0.1.0
      */

     var getDependentNames = function()
     {

     	var p2panel = guideBridge.resolveNode("guide[0].guide1[0].guideRootPanel[0].Person2[0]");

    	var deps =
    	 _.reduce (p2panel.instanceManager.instances,
		 function (memo, panelObj) {

	 		if (panelObj.PersonalInformation.OnP1Return.value == "Y" && panelObj.PersonalInformation.CommonName.value != "")
	 		{
	 			if (memo!="")
	 				memo += ", ";

	 			memo += panelObj.PersonalInformation.CommonName.value;
	 		}
	 		return memo;
    	 } , "" );

    	return deps;

     }

	 var screenerNavigator = function(button, direction)
	 {

		 if (direction=="back")
		 {
			 window.guideBridge.setFocus(null, 'prevItemDeep');
			 window.guideBridge.validate([],window.guideBridge.resolveNode(window.guideBridge.getFocus()).panel.navigationContext.currentItem.somExpression);

		 } else if (direction=="next") {

			 if (window.guideBridge.validate([],button.panel.navigationContext.currentItem.somExpression))
			 {

				 var exempt = window.guideBridge.resolveNode("guide[0].guide1[0].guideRootPanel[0].ChooseExemption[0].ExemptionSelectionPanel[0].radioExemptions1[0]").value;
				 var state = window.guideBridge.resolveNode("guide[0].guide1[0].guideRootPanel[0].StateSelection[0].State[0]").value;

				 if (exempt==null || (exempt == "AFFORD" && state==null))
				 {
					 window.guideBridge.setFocus(null, 'nextItemDeep');
				 } else {
					 redirectToExemption(exempt,state);
					 return;
				 }
			 }
		 }

		 // toolbar visible?
		 // does current panel have a previous nav item?  show the toolbar
		 window.guideBridge.resolveNode(window.guideBridge.getNavigablePanel([])).toolbar.visible
		 	= window.guideBridge.resolveNode(window.guideBridge.getFocus()).panel.parent.navigationContext.hasPrevItem;

	 }

     /**
      *  Redirects the browser to an exemption form
      *
      *  @param {String} formKey Exemption form key: HS/FFM/SBM/TRIBAL/SECT/HSM/AFFORD
      *  @param {String} state US state abbreviation (for when formKey==AFFORD)
      *  @return {void}
      *  @memberOf cmsexemptions.formtheme
      *  @since 0.1.0
      */
     var redirectToExemption = function (formKey,state) {

 		var redirectUrl = "";

 		if (formKey=="AFFORD")
 		{
 	 		var sbmArray = ["CA","CO","DC","HI","ID","KY","MD","MA","MN","NY","RI","VT","WA"];

 	 		if (state!=null && sbmArray.indexOf(state)>=0)
 	 		{
 	 			formKey="SBM";
 	 		} else {
 	 			formKey="FFM";
 	 		}
 		}

		preventAccidentalNavigation(false);
 		switch (formKey)
 		{
 			case "HS":
 				location.href="https://www.healthcare.gov/exemption-online-form/Hardship.html";
 				break;
 			case "FFM":
 				location.href="https://www.healthcare.gov/exemption-online-form/Affordability-FFM.html";
 				break;
 			case "SBM":
 				location.href="https://www.healthcare.gov/exemption-online-form/Affordability-SBM.html";
 				break;
 			case "TRIBAL":
 				location.href="https://www.healthcare.gov/exemptions-tool/#/results/2015/details/tribal";
 				break;
 			case "SECT":
 				location.href="https://www.healthcare.gov/exemptions-tool/#/results/2015/details/religion";
 				break;
 			case "HSM":
 				location.href="https://www.healthcare.gov/exemptions-tool/#/results/2015/details/healthcare-sharing-ministry";
 				break;
 			case "INCARC":
 				location.href="https://www.healthcare.gov/exemptions-tool/#/results/2015/details/incarceration";
 				break;
 			default:
 				alert ("Redirect to unknown exemption application: " + formKey);
 		}
 	};


 	 /**
     *  Prevent accidental navigation away from form-in-process
     *
     *  @return {void}
     *  @memberOf cmsexemptions.formtheme
     *  @since 0.1.0
     */
    var preventAccidentalNavigation = function (prevent) {

    	if (prevent==false)
    	{
			window.onbeforeunload = false;
    	} else {

			window.onbeforeunload = function(evt) {
				return 'Are you sure you wish to leave this application?';
		    }

			window.guideBridge.on("submitStart", function()
				{
	    			window.onbeforeunload = false;
	        		window.guideBridge.off("submitStart");
				}
			);
    	}

    }

    
	 /**
	 *  Reset multi-instance panel
	 *
	 *  Deletes any created instances above minOccur
	 *  Runs resetData() on all remaining instances
	 *  
	 *  @param {Panel} Panel to reset
	 *  @return {void}
	 *  @memberOf cmsexemptions.formtheme
	 *  @since 0.1.0
	 */     
    var resetMultiInstancePanel = function(panel) {
    	
    	while (panel.instanceManager.instanceCount > panel.instanceManager.minOccur)
    		panel.instanceManager.removeInstance(panel.instanceManager.instanceCount-1);

    	for (var i=0; i<panel.instanceManager.instanceCount; i++)
    		panel.instanceManager.instances[i].resetData();

    }

	return {
		alertGuideXML: alertGuideXML,
		displayErrorBox : displayErrorBox,
		elementValueChangedHandler: elementValueChangedHandler,
		getDependentNames : getDependentNames,
		preventAccidentalNavigation : preventAccidentalNavigation,
		redirectToExemption: redirectToExemption,
		resetMultiInstancePanel : resetMultiInstancePanel,
		screenerNavigator : screenerNavigator,
		stepCompletionHandler: stepCompletionHandler,
		validationCompleteTopErrorHandler : validationCompleteTopErrorHandler
	}

}();


// Do this once guide is initialized
window.addEventListener("bridgeInitializeStart", function(evnt) {
	 
    // get hold of the guideBridge object
    var gb = evnt.detail.guideBridge;

    //wait for the completion of AF
    gb.connect(function (){
    	
        // activate topError handler -- big red box if form has errors.
        gb.on("validationComplete", cmsexemptions.formtheme.validationCompleteTopErrorHandler );
        
        // activate behavior when items change on form
        gb.on("elementValueChanged",cmsexemptions.formtheme.elementValueChangedHandler);
        
        
        /**
        * Attach Keybindings
        */

        document.body.addEventListener('keydown', function(e) {
        	try{
        	    if(e.ctrlKey && e.altKey && e.keyCode == 81){
        	        //CTRL+ALT+Q Pressed
        	        cmsexemptions.formtheme.alertGuideXML(window.guideBridge);
        	    } else if(e.ctrlKey && e.altKey && e.keyCode == 87) {
        	        //CTRL+ALT+W Pressed
        	    	window.guideBridge.validate();
        	    }
        	} catch (e){
        		//Quietly ignore
        	}
        });        
        
        // if privacy modal exists, pop it up
        if ($("#pii").length) {
        	cmsexemptions.controls.modalShow('#pii','#main');
        }
                
    });
});