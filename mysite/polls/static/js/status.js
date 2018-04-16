(function() {

function schedulerIsActive() {
  document.getElementById("status").innerHTML = "Active";
  $(".lockout").addClass("schedulerActive");
}

function schedulerIsNotActive() {
  document.getElementById("status").innerHTML = "Not Active";
  $(".lockout").removeClass("schedulerActive");
}

function parseStatus(data) {
    if (data.status) {
      schedulerIsActive();
    } else {
      schedulerIsNotActive();
    }
}

function updateStatus() {
  console.log("updating scheduler status...");
  $.getJSON("/status/", "", parseStatus);
};

window.setTimeout(updateStatus, 10);
window.setInterval(updateStatus, 3000);

})();
