"use strict";
use(function () {

    var props = {};
    var label = "Simple Button";
    var labelProp = granite.resource.properties["buttonLabel"];

    if(labelProp != null && labelProp != ""){
		label = labelProp;
    }   

    props.label = label;

    return props;
});