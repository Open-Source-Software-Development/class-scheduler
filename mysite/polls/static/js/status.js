(function() {
//If scheduler is active, set status to "Active" in the top margin and add 
//the "schedulerActive" class to submit buttons
function schedulerIsActive() {
  document.getElementById("status").innerHTML = "Active";
  $(".lockout").addClass("schedulerActive");
}
//If scheduler is not active, set status to "Not Active" in the top margin and
//remove the "schedulerActive" class from submit buttons
function schedulerIsNotActive() {
  document.getElementById("status").innerHTML = "Not Active";
  $(".lockout").removeClass("schedulerActive");
}
//Call the above functions when appropriate
function parseStatus(data) {
    if (data.status) {
      schedulerIsActive();
    } else {
      schedulerIsNotActive();
    }
}
//Every 3 seconds, check to see if the scheduler algorithm is active
function updateStatus() {
  console.log("updating scheduler status...");
  $.getJSON("/status/", "", parseStatus);
};

window.setTimeout(updateStatus, 10);
window.setInterval(updateStatus, 3000);

})();
