(function() {

function schedulerIsActive() {
  // replace this with some real implementation
  console.log("Scheduler is active!");
  document.getElementById("status").innerHTML = "Active";
}

function schedulerIsNotActive() {
  // replace this with some real implementation
  console.log("Scheduler is not active!");
  document.getElementById("status").innerHTML = "Not Active";
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
