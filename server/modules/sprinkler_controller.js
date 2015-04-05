module.exports = {
	setState: function (s) {
		state = s;
		if (state) {
			// turn the LED on for now
			sprinklers.write(1);
		} else {
			// turn the LED off for now
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