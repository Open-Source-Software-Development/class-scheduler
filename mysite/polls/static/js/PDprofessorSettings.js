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
	this.blockID = blockID;
	this.element = element;
	this.state = 0; // set block as whatever it is in database for whoever is selected from list
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
	return "<input type=\"hidden\" name=\"t" + this.blockID + "\" value=\"" + this.state + "\">";
}