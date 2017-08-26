/**
\ * @file Common AEM Form Utilities
 * @author Adobe Systems Federal Delivery <CMS-Incident@adobe.com>
 * @version 0.1.0
 */

/**
 * Parent namespace for all things common.
 *
 * @namespace
 * @version 0.1.0
 * @since 0.1.0
 * @see http://www.adobe.com
 */
var common = {};

/**
 * AEM Form Utilities.
 *
 * @namespace
 * @memberOf common
 * @version 0.1.0
 * @since 0.1.0
 */
common.utils = function () {

    /**
     * Confirms your stuff is available with a message box
     *
     * @memberOf common.utils
     * @returns {void}
     * @example marketplace.common.utils.test();
     * @since 0.1.0
     */
    var test = function () {
    		
    		
			if (isXFA())
			{
				try {
    				xfa.host.messageBox("marketplace.js XFA OK");
				} catch (e) {
					xfa.host.messageBox(e.message);
				}
            } else {
          		alert("marketplace.js JS OK");
            }
        },

        /**
         * Dev Tools.
         *
         * @memberOf common.utils
         * @returns {void}
         * @since 0.1.0
         */
        devToggleTools = function (oNode, visible) {
            if (oNode.className == "exclGroup" || oNode.className == "subform" || oNode.className == "subformSet" || oNode.className == "area") {
                for (var i = 0; i < oNode.nodes.length; i++) {
                    devToggleTools(oNode.nodes.item(i), visible);
                }
            }
            else if (oNode.className == "field") {
                if (oNode.name.indexOf("dev") == 0) {
                    if (visible) {
                        oNode.presence = "visible";
                    } else {
                        oNode.presence = "hidden";
                    }
                }
            }
        },

        /**
         *  Concatenates Field Values
         *  @param {Array} arrayOfValues - ex: array of strings
         *  @param {String} separator - put this between the fields
         *  @return {String} The concatenated fields
         *  @memberOf common.utils
         *  @since 0.1.0
         */
        
        concatArray = function (arrayOfThings, separator) {
            var concatStr="";
            if (typeof separator==='undefined') 
            {
            	separator=" ";
            }
        	for (var i = 0; i < arrayOfThings.length; i++) {
                if (arrayOfThings[i] != null) {
                    concatStr += arrayOfThings[i] + separator;
                }
            }
            return concatStr;
        },

        
        /**
         *  strips non-alphabetic 
         *  @return {string} a string devoid of non-alphabetic characters
         *  @memberOf common.utils
         *  @since 0.1.0
         */
        makeAlpha = function (thisText)
    	{
    		return (thisText!=null ? thisText.replace(/[^a-zA-Z \-]/g, "") : null);
    	},

        /**
         *  strips everything but letters and numbers, and hyphen
         *  @return {string} 
         *  @memberOf common.utils
         *  @since 0.1.0
         */
    	makeAlphaNumeric = function (thisText)
    	{
    		return (thisText!=null ? thisText.replace(/[^a-zA-Z\d\- ]/g, "") : null);
    	},
    	
        /**
         *  keeps digits
         *  @return {string} 
         *  @memberOf common.utils
         *  @since 0.1.0
         */
    	makeNumeric = function (thisText)
    	{
    		return (thisText!=null ? thisText.replace(/[^0-9]/g, "") : null);
    	},

        
        /**
         *  Trim String Utility
         *  @return {void} Nothing
         *  @memberOf common.utils
         *  @since 0.1.0
         */
        trimString = function (strInput) {
            return strInput.replace(/^\s+|\s+$/gm, '');
        },
        
        /**
         *  Determines whether or not we're running
         *  this JS in the context of an XFA or Browser
         *  @return {void} Nothing
         *  @memberOf common.utils
         *  @since 0.1.0
         */
        isXFA = function() {
        	return (typeof xfa=="object");
        };
        
    return {
    	isXFA:isXFA,
    	concatArray: concatArray,
    	makeNumeric: makeNumeric,
    	makeAlpha: makeAlpha,
    	makeAlphaNumeric: makeAlphaNumeric,
        test: test,
        devToggleTools: devToggleTools,
        trimString: trimString

    }

}();

/**
 * AEM Form Validations.
 *
 * @namespace
 * @memberOf common
 * @version 0.1.0
 * @since 0.1.0
 */
common.validation = function () {

    /**
     *  Is Valid Address
     *  @return {boolean} true or false
     *  @param {string} txtStreet
     *  @param {string} txtApt
     *  @param {string} txtCity
     *  @param {string} txtState
     *  @param {string} txtZip
     *  @param {string} txtCounty
     *  @param {boolean} isOptional
     *  @memberOf common.validation
     *  @since 0.1.0
     */
     var isAddressValid = function (txtStreet, txtApt, txtCity, txtState, txtZip, txtCounty, isOptional) {

            if (isOptional == true & txtStreet == null & txtApt == null & txtCity == null & txtState == null & txtZip == null & txtCounty == null) {
                // nothing in the fields, and it's optional.  we're ok.
                return true;
            }

            if (txtStreet != null & txtCity != null & isValidState(txtState) & isValidZIP(txtZip)) {
                // valid minimal info
                return true;
            }

            // Only way to get this far is if it's partial filled or not minimally  valid.
            return false;

        },

        /**
         *  Is Valid Year
         *  @return {boolean} true or false
         *  @param {string} yearstr
         *  @memberOf common.validation
         *  @since 0.1.0
         */
        isValidYear = function (yearstr) {
            if (yearstr != null) {
                var yearPattern = /^[0-9]{4}$/;
                return yearPattern.test(yearstr);
            }
            return false;
        },

        /**
         *  Is Valid Zip code
         *  5 digits
         *  @return {boolean} true or false
         *  @param {string} zip
         *  @memberOf common.validation
         *  @since 0.1.0
         */
        isValidZIP = function (zip) {
            if (zip != null) {
                var zipPattern = /^\d{5}$/; 
                return zipPattern.test(zip);
            }
            return false;
        },

        /**
         *  Is Valid Date
         *  @return {boolean} true or false
         *  @param {string} yearStr
         *  @param {string} monthStr
         *  @param {string} dayStr
         *  @memberOf common.validation
         *  @since 0.1.0
         */
        isValidDate = function (yearStr, monthStr, dayStr) {
            if (yearStr == null | monthStr == null | dayStr == null) {
                return false;
            }

            // parse sections into date obj
            var dateObject = util.scand("dd-mm-yyyy", dayStr + "-" + monthStr + "-" + yearStr);

            // null = bad date
            return !(dateObject == null);

        },

        /**
         *  Is Valid Date String
         *  @return {boolean} true or false
         *  @param {string} dateStr
         *  @memberOf common.validation
         *  @since 0.1.0
         */
        isValidDateString = function (dateStr) {
            // parse string as MM/DD/YYYY (w optional separators)
            if (dateStr != null) {
                var datePattern = /^(0?[1-9]|[12][0-9]|3[01])[\/\-](0?[1-9]|1[012])[\/\-]\d{4}$/;
                return datePattern.test(dateStr);
            }
            return false;
        },

        /**
         *  Is Valid Phone
         *  @return {boolean} true or false
         *  @param {string} phone
         *  @memberOf common.validation
         *  @since 0.1.0
         */
        isValidPhone = function (phone) {
            if (phone != null) {
                var phonePattern = /^[0-9]{10}$/;
                return phonePattern.test(phone);
            }
            return false;
        },

        /**
         *  Is Valid Email
         *  @return {boolean} true or false
         *  @param {string} email
         *  @memberOf common.validation
         *  @since 0.1.0
         */
        isValidEmail = function (email) {
            if (email != null) {
                var emailPattern = /^([a-zA-Z0-9._%-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4})*$/;
                return emailPattern.test(email);
            }
            return false;
        },

        /**
         *  Is Valid State
         *  @return {boolean} true or false
         *  @param {string} stateAbbr
         *  @memberOf common.validation
         *  @since 0.1.0
         */
        isValidState = function (stateAbbr) {
            if (stateAbbr != null) {
                var statePattern = /^(?:A[KLRZ]|C[AOT]|D[CE]|FL|GA|HI|I[ADLN]|K[SY]|LA|M[ADEINOST]|N[CDEHJMVY]|O[HKR]|PA|RI|S[CD]|T[NX]|UT|V[AT]|W[AIVY])*$/;
                return statePattern.test(stateAbbr.toUpperCase());
            }
            return false;
        },

        /**
         *  Is Valid SSN
         *  separated (or not) or separated by "-" or space, 
         *  not starting with 000, 666, or 900-999, 
         *  not containing 00 or 0000 in the middle 
         *  or at the end of SSN. 
         *  @return {boolean} true or false
         *  @param {string} ssn
         *  @memberOf common.validation
         *  @since 0.1.0
         */
        isValidSSN = function (ssn) {
            if (ssn != null) {
                var ssnPattern = /^(?!000|666)(?:[0-6][0-9]{2}|7(?:[0-6][0-9]|7[0-2]))-?(?!00)[0-9]{2}-?(?!0000)[0-9]{4}$/;
                return ssnPattern.test(ssn);
            }

            return false;
        },

        /**
         *  Is Valid Multi-part Date
         *  @return {boolean} true or false
         *  @param {object} oFldMonth
         *  @param {object} oFldDay
         *  @param {object} oFldYear
         *  @memberOf common.validation
         *  @since 0.1.0
         */
        isValidMultiPartDate = function (oFldMonth, oFldDay, oFldYear) {
            var sDate = oFldDay.formattedValue + "-" + oFldMonth.formattedValue + "-" + oFldYear.formattedValue;
            var oDate = util.scand("dd-mm-yyyy", sDate);  // convert to date object

            if (oDate != null) {
                //xfa.host.messageBox("got a date: " + util.printd("mm/dd/yyyy",oDate));
                return true;
            }

            return false;
        },
        
        /**
         *  isDateFuture
         *  @return {boolean} true or false
         *  @param {string} dateToCheck YYYY-MM-DD (as per adaptive form datepicker output)
         *  @memberOf common.validation
         *  @since 0.1.0
         */        
        isDateFuture = function(dateString) {
        	
        	var d = Date.parse(dateString);
        	if (!isNaN(parseFloat(d)))
        	{
        		return (d > (new Date()).getTime());
        	} else {
        		return false; // invalid; can't be future
        	}

        },
        
        /**
         *  isDatePriorTo
         *  @return {boolean} true if date prior to, false if not or if bad data
         *  @param {string} checkDate YYYY-MM-DD (as per adaptive form datepicker output)
         *  @param {string} priorTo  YYYY-MM-DD (as per adaptive form datepicker output)
         *  @memberOf common.validation
         *  @since 0.1.0
         */        
        isDatePriorTo = function(checkDate, priorTo) {
        	
        	var dc = Date.parse(checkDate);
        	var dp = Date.parse(priorTo);
        	if (!isNaN(parseFloat(dc)) && !isNaN(parseFloat(dp)))
        	{
        		return (dc < dp);
        	} else {
        		return false; // invalid
        	}
        },
        
        /**
         *  isValidBirthday
         *  @return {boolean} must be within last 100 years
         *  @param {string} birthday
         *  @memberOf common.validation
         *  @since 0.1.0
         */        
        isValidBirthday = function(birthday) {
        	
        	if (typeof birthday!==undefined && birthday!=null)
        	{
        		return !(isDateFuture(birthday) || isDatePriorTo(birthday,"1890-01-01"));
        	} else {
        		return false;
        	}
        	
        };
        


    return {

        isDateFuture: isDateFuture,
        isDatePriorTo: isDatePriorTo,
        isValidBirthday: isValidBirthday,
        isValidYear: isValidYear,
        isValidZIP: isValidZIP,
        isValidDate: isValidDate,
        isValidDateString: isValidDateString,
        isValidPhone: isValidPhone,
        isValidEmail: isValidEmail,
        isValidState: isValidState,
        isValidSSN: isValidSSN,
        isValidMultiPartDate: isValidMultiPartDate,

    }

}();