/**
 * @file CMS Controls 
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
 * AEM Exemptions Controls
 *
 * @namespace
 * @memberOf cmsexemptions
 * @version 0.1.0
 * @since 0.1.0
 */

cmsexemptions.controls = function() {

	var _STATE_OPTIONS = 
	[
	 "AL=Alabama", "AK=Alaska", "AZ=Arizona", "AR=Arkansas", "CA=California", "CO=Colorado", "CT=Connecticut", 
	 "DE=Delaware", "DC=District Of Columbia", "FL=Florida", "GA=Georgia", "HI=Hawaii", "ID=Idaho", "IL=Illinois", 
	 "IN=Indiana", "IA=Iowa", "KS=Kansas", "KY=Kentucky", "LA=Louisiana", "ME=Maine", "MD=Maryland", "MA=Massachusetts",
	 "MI=Michigan", "MN=Minnesota", "MS=Mississippi", "MO=Missouri", "MT=Montana", "NE=Nebraska", "NV=Nevada", "NH=New Hampshire", 
	 "NJ=New Jersey", "NM=New Mexico", "NY=New York", "NC=North Carolina", "ND=North Dakota", "OH=Ohio", "OK=Oklahoma", 
	 "OR=Oregon", "PA=Pennsylvania", "RI=Rhode Island", "SC=South Carolina", "SD=South Dakota", "TN=Tennessee", 
	 "TX=Texas", "UT=Utah", "VT=Vermont", "VA=Virginia", "WA=Washington", "WV=West Virginia", "WI=Wisconsin", 
	 "WY=Wyoming"
	];
	
	
	/**
     * Creates a list of allowable values for a dropdown in a repeating subform <br/>
     * given that each instance's dropdown value must be unique. <br/>
     * 
     * It removes the values that are already selected in the other subforms <br/>
     * from the list of possibilities. <br/>
     * 
     * Usage: typically in an options expression of a dropdown field in an adaptive form <br/>
     * cmsexemptions.controls.getAllowableDropdownItems(this, RepeatingPanel, ["x=y","a=b"]);
     *
     * @memberOf cmsexemptions.controls
     * @param {Field} Field dropdown
     * @param {Panel} Panel that contains the dropdown
     * @param {Array} Array containing strings in the format name=value
     * 
     * @returns {Array} Array of allowable values from the list of all possible values
     * @since 0.1.0
     */
	
	var getAllowableDropdownItems = function (thisDropdownWidget, panel, allItemsArray)
	{
		
		try {
			// console.log("recalc items for " + thisDropdownWidget.somExpression);
			
			// get all selected field values in all the panels that arent't this one, and aren't null
			var selValues = _(
					window.guidelib.runtime.af.filter (panel.instanceManager.instances, thisDropdownWidget.name, 
					function(panel) { 
						// we don't want to return true if it's checking itself; otherwise it'll
						// cancel its own selected value out of the allowable options.
						return (panel[this.name].value!= null && panel[this.name].somExpression != this.somExpression); 
					}, thisDropdownWidget))
				.pluck("value");
			
			if (selValues.length>0)
			{
				// toss out any selected items from the list of all items
				allItemsArray = _(allItemsArray).reject(function (item)
				{
					// matching the value before the = sign; values are in {value}={label}
					return (_(selValues).contains(item.split("=")[0]));
				},selValues);
			}
		} catch (e) {
			console.log(e);
		}
	
		return allItemsArray;
		
	};
	
	/**
     * Checks all instances of a field in a repeating panel set and returns true
     * if the value is already set.
     * 
     * Usage: typically in a validation or commit expression of a dropdown or options <br/>
     * cmsexemptions.controls.doesSiblingPanelWidgetHaveSameValue(this, RepeatingPanel);
     *
     * @memberOf cmsexemptions.controls
     * @param {Field} Field dropdown
     * @param {Panel} Panel that contains the dropdown
     * 
     * @returns {Boolean} if exists elsewhere in this panel group
     * @since 0.1.0
     */
	
	var doesSiblingPanelFieldHaveSameValue = function (thisWidget, panel)
	{
		
		
		if (thisWidget.value!=null && panel.instanceManager.instanceCount > 1)
		{
			for (var i=0; i<panel.instanceManager.instanceCount; i++)
			{
				var checkPanel=panel.instanceManager.instances[i];
				
				if (checkPanel[thisWidget.name].value!= null 
					&& checkPanel[thisWidget.name].somExpression != thisWidget.somExpression
					&& checkPanel[thisWidget.name].value == thisWidget.value)
				{
					return true;
				}
			}
		}

		// null value or no siblings or not found
		return false;
		
	};

	/**
     * Helper function for validation expressions.  Pass in a true/false value
     * and a modal id.  Modal will show if value is false.
     * 
     * @memberOf cmsexemptions.controls
     * @param {Boolean} isValid condition
     * @param {String} modalId id of modal
     * 
     * @returns {Void} 
     * @since 0.1.0
     */
	
	var validateWithModal = function (isValid, modalId, returnFocusTo)
	{
		if (!isValid) {
			modalShow(modalId, returnFocusTo);
			return false;
		}
		return true;
	};
	
	var modalShow = function(modalId, returnFocusTo) {
		
		if (returnFocusTo===undefined)
		{
			returnFocusTo = $(':focus') || document.getActiveElement;
		}

		$(modalId).modal({keyboard:false});

		// trap focus in modal
		$("body").focusin(function(evt)
		{
			if ($.contains($(modalId).get()[0],evt.target)==false && $(modalId).get()[0]!=evt.target)
			{
				$(modalId).focus();
			}
		});
		
		$(document).keypress(function(event) {
		    if (event.which == 13) {
		        event.preventDefault();
		        $(modalId + " .btn-success").click()
		    }
		});
		
		// when modal closes; disable trap handler and refocus
		// to object on page that was last in focus 
		$(modalId).on('hidden.bs.modal', function () {
			$('body').off('focusin');		    
	        $(document).off("keypress");
		    $(returnFocusTo).focus();
		});
	
	};
	
	var modalHide = function(modalId) {
		$(modalId).modal("hide");
	};
	
	
	return {
		modalShow : modalShow,
		modalHide : modalHide,
		validateWithModal : validateWithModal,
		getAllowableDropdownItems: getAllowableDropdownItems,
		doesSiblingPanelFieldHaveSameValue : doesSiblingPanelFieldHaveSameValue,
		STATE_OPTIONS : _STATE_OPTIONS
	};
	

}();