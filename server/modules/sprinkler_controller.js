module.exports = {
	setState: function (s) {
		state = s;
		if (state) {
			// turn the LED on for now
			console.log("turning on");
			sprinklers.write(1);
		} else {
			// turn the LED off for now
			console.log("turning off");
			sprinklers.write(0);
		}
	},
	getState: function () {
		return state;
	}
};

var m = require('mraa');
var sprinklers = new m.Gpio(8); //setup digital read on pin 5
sprinklers.dir(m.DIR_OUT); //set the gpio direction to output
sprinklers.write(0); //set the digital pin to high (1)

var state = false;