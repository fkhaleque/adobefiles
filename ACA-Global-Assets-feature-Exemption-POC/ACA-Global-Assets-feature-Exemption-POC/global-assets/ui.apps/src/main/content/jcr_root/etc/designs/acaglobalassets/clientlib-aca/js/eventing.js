/**
* Event binding
*
*/

 /**
 *  CTRL+ALT+Q Easter Egg listener to alert XML data in browser.
 *  
 *  @return {void}
 *  @memberOf cmsexemptions.formtheme
 *  @since 1.0.1
 */
document.body.addEventListener('keydown', function(e) {
	try{
	    if(e.ctrlKey && e.altKey && e.keyCode == 81){
	        //CTRL+ALT+Q Pressed
	        cmsexemptions.formtheme.alertGuideXML(window.guideBridge);
	    }
	} catch (e){
		//Quietly ignore
	}
});

/**
 *  CTRL+ALT+W Easter Egg listener to validate AF.
 *  
 *  @return {void}
 *  @memberOf cmsexemptions.formtheme
 *  @since 1.0.1
 */
document.body.addEventListener('keydown', function(e) {
	try{
	    if(e.ctrlKey && e.altKey && e.keyCode == 87){
	        //CTRL+ALT+W Pressed
	    	window.guideBridge.validate();
	    }
	} catch (e){
		//Quietly ignore
	}
});