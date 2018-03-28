function setup() {
	var blocks = document.getElementsByClassName("ClickableTimeBlock");
	for (var key in blocks) {
		if (!blocks.hasOwnProperty(key)) {
			continue;
		}
		(new AvailabilityButton(key, blocks[key])).setup();
	}
}

function AvailabilityButton(blockID, element) {
	this.blockID = element.id;
	this.element = element;
	this.state = element.title; // set block as whatever it is in database for whoever is logged in
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
	currentCell.innerHTML = this.stateList[this.state].label
			+ this.getInputTag();
	currentCell.style.backgroundColor = this.stateList[this.state].color;	
	this.state = (this.state + 1) % this.stateList.length;
}

AvailabilityButton.prototype.stateList = [
	{label : "Available", color : "#57b712"},
	{label : "Dislike", color : "#f6ff00"},
	{label : "Unavailable", color : "#e24e34"}
];

AvailabilityButton.prototype.getInputTag = function() {
	return "<input type=\"hidden\" name=\"" + this.blockID + "\" value=\"" + this.state + "\">";
}