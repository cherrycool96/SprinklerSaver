module.exports = {
	btnStopWatering: function () {
		return btnStopWatering.read() === 1;
	}
};

var m = require('mraa'); //require mraa

var btnStopWatering = new m.Gpio(5); //setup digital read on pin 6
btnStopWatering.dir(m.DIR_IN); //set the gpio direction to input