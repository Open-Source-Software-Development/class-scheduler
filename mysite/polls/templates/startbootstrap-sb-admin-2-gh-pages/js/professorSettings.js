function setup() {
	var blocks = document.getElementsByClassName("ClickableTimeBlock");
	for (var key in blocks) {
		if (!blocks.hasOwnProperty(key)) {
			continue;
		}
		(new AvailabilityButton(blocks[key])).setup();
	}
}

function AvailabilityButton(element) {
	this.element = element;
	this.state = 0;
}

AvailabilityButton.prototype.setup = function() {
	var that = this;
	this.element.onclick = function(){
		that.update();
	};
	this.update();
}

AvailabilityButton.prototype.update = function() {
	var currentCell = this.element;
	currentCell.innerHTML = this.stateList[this.state].label;
	currentCell.style.backgroundColor = this.stateList[this.state].color;	
	this.state = (this.state + 1) % this.stateList.length;
}

AvailabilityButton.prototype.stateList = [
	{label : "Available", color : "#57b712"},
	{label : "Unavailable", color : "#e24e34"},
	{label : "Prefer Against", color : "#f6ff00"}	
];