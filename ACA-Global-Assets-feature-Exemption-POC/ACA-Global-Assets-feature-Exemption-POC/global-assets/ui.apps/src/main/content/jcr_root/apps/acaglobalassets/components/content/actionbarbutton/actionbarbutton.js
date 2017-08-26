"use strict";
(function() {
	
	var id = null;

	try {

		id = "test";

	} catch (e) {
		console.error(e.message);
	}

	return {
		id:id
	};

})();

