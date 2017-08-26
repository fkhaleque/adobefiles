/**
 * @file CMS Hardship Rules 
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
 * AEM Exemptions Hardship Ruleset.
 *
 * @namespace
 * @memberOf cmsexemptions
 * @version 0.1.0
 * @since 0.1.0
 */

cmsexemptions.hardship = function() {

	/**
     * validates if this module is wired up
     * use for developing, override as you see fit.
     *
     * @memberOf cmsexemptions.hardship
     * @param {Object} obj an object
     * @returns {Void} nothing
     * @since 0.1.0
     */
	var test = function(obj)
	{
		// do something;
		alert("test");
		
	}
	
    var getHardship14Description = function(descField)
    {
   	 	// look at all hardships in this panel instance,
    	// extract value from the repeating HardshipEntry section
    	// and return a value that can be used for the binding
    	// outside the repeating section.
    	var reason = 
    		_.reduce (descField.panel.HardshipEntry.instanceManager.instances,  
		 function (memo, hsPanel) {
	 		if (hsPanel.HardshipType.value === "14" && !_.isNull(hsPanel.HSDetails.description.value))
	 		{
	 			memo += hsPanel.HSDetails.description.value;
	 		}
	 		return memo;
    	} , "" );
   	
    	return reason;
   	 
    }
    
	
	return {
		getHardship14Description : getHardship14Description,
		test : test
	}

}();