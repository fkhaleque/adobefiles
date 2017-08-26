"use strict";
use(function () {

    var noticeData = {
        "labels": ["Failures", "Errors", "Successful"],
        "values": [10, 50, 940],
        "series": ["Notices", "Notices", "Notices"]
    };

    var pageData = {
        "labels": ["Pages"],
        "values": [16000],
        "series": ["Pages"]
    };

    var localeData = {
        "labels": ["English", "Spanish"],
        "values": [60, 940],
        "series": ["Notices", "Notices"]
    };

    return{

        notices:JSON.stringify(noticeData),
        pages:JSON.stringify(pageData),
        languages:JSON.stringify(localeData)

    };
});