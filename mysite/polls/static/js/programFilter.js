function filters() {
    location.href="?year=" +
    document.getElementsByName("yearSelection")[0].options[document.getElementsByName("yearSelection")[0].selectedIndex].value + 
    "&course-list=" +
    document.getElementsByName("course-list")[0].options[document.getElementsByName("course-list")[0].selectedIndex].value +
    "&running-list=" + 
    document.getElementsByName("running-list")[0].options[document.getElementsByName("running-list")[0].selectedIndex].value;
}

function getUrlVars() {
    var vars = {};
    var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi, function(m,key,value) {
        vars[key] = value;
    });
    return vars;
}