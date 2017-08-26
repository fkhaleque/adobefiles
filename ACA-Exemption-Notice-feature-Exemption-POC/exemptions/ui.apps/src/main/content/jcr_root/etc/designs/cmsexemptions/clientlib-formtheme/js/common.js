/**
 * @file CMS Exemptions Common Rules
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
 * AEM Exemptions Common Ruleset.
 *
 * @namespace
 * @memberOf cmsexemptions
 * @version 0.1.0
 * @since 0.1.0
 */

cmsexemptions.common = function() {

	var determineHousehold = function(evt, isValid) {

		try {

			var panelDom = $(guideBridge._getGuideDomElement(this.somExpression));

			if (!isValid && this.PlanToFile.value!=="Y")
			{
				cmsexemptions.controls.modalShow("#notfiling_modal");
			} else {

				// have valid household configuration
				// update number of P2 panels

				// console.log("Updating number of person 2");

				var p2Panel = guideBridge.resolveNode("guide[0].guide1[0].guideRootPanel[0].Person2[0]");
				var spousePanel = guideBridge.resolveNode("guide[0].guide1[0].guideRootPanel[0].Spouse[0]");
				var depCount = parseInt(this.NumberOfDependents.value,10);

				// sanity check
				if (_.isNull(p2Panel) || _.isNull(spousePanel) || _.isNull(depCount))
				{
						console.log("Unexpected p2Panel/spousePanel/depCount null");
						return;
				}

				// adjust dependent count if required
				if (depCount<p2Panel.instanceManager.instanceCount)
				{
						// don't delete only instance
						for (var i=p2Panel.instanceManager.instanceCount; i>depCount && i>1;i--)
						{
							p2Panel.instanceManager.removeInstance(p2Panel.instanceManager.instanceCount-1);
						}
				} else if (depCount>p2Panel.instanceManager.instanceCount) {
						// add additional panels
						for (var i=p2Panel.instanceManager.instanceCount; i<depCount;i++)
						{
							p2Panel.instanceManager.addInstance();
						}
				}

				if (depCount===0)
				{
					// no deps? hide and reset
					p2Panel.resetData();
					p2Panel.visible=false;
					p2Panel.validationsDisabled=true;
				} else {
						// has deps; show all and prefill
					_.each(p2Panel.instanceManager.instances, function(ele,list,idx) {
						ele.visible=true;
						ele.validationsEnabled=true;
						window.guideBridge.setProperty(
							[
							ele.Person2Title.somExpression,
							ele.FileJointly.somExpression,
							ele.PersonalInformation.OnP1Return.somExpression,
							ele.PersonalInformation.IsSpouse.somExpression
							]
							,"value", [("Dependent " + (ele.instanceIndex+ 1) + " of " + (ele.instanceManager.instanceCount)),"N","Y","N"]);
					});
				}

				if (window.guideBridge._guide.guideRootPanel.HouseholdDetermination.IsFilingJointly.value !== "Y")
				{
					// not filing jointly
					spousePanel.visible = false;
					spousePanel.validationsDisabled = true;
					spousePanel.resetData();
				} else {
					// filing jointly
					spousePanel.visible = true;
					spousePanel.validationsDisabled = false;

					window.guideBridge.setProperty(
						[
						spousePanel.FileJointly.somExpression,
						spousePanel.PersonalInformation.OnP1Return.somExpression,
						spousePanel.PersonalInformation.IsSpouse.somExpression,
						spousePanel.PersonalInformation.RelationshipToFiler.somExpression,
						spousePanel.TaxQuestions.PlanToFile.somExpression
						]
						,"value", ["Y","N","Y","SPOUSE","Y"]);

				}
			}

		} catch (e) {
			console.log("error recalculating household:" + e.message);
		}

	};

	return {
		determineHousehold : determineHousehold
	}

}();
