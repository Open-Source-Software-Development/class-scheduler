function filterYear() {
    location.href="?year=" + 
    document.getElementsByName("yearSelection")[0].options[document.getElementsByName("yearSelection")[0].selectedIndex].value +
    "&program=" + getUrlVars()["program"];
}

function getUrlVars() {
    var vars = {};
    var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi, function(m,key,value) {
        vars[key] = value;
    });
    return vars;
}