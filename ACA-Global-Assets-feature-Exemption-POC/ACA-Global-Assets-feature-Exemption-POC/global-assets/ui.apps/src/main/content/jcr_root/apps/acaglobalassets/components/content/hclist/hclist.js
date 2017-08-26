"use strict";
(function() {

	var errors = Math.floor((Math.random() * 10) + 1);
	var id = properties.get("title").toLowerCase();
	var data = {
		'y': [333, 333, 333],
		'x': ['Generated', 'Error', 'Failure'],
		'series': [1, 1, 1],
		'seriesLabel': [1, 1, 1]
		};

	return {
		errors : errors,
		id : id,
		data : data
	};
})();