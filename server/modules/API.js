module.exports = {
	init: function (startFunc, stopFunc) {
		startWatering = startFunc;
		stopWatering = stopFunc;

		app.listen(PORT, function(){
			console.log('listening on *:' + PORT);
		});

		return this;
	},
	forceWatering: false
};

var startWatering, stopWatering;
var PORT = 7655;
var app = require('express')();
var server = require('http').Server(app);

app.get("/SprinklersOn", function (req, res) {
	// TODO: comment the next line
	startWatering();
	forceWatering = true;
	res.send("Turning on");
});

app.get("/SprinklersOff", function (req, res) {
	stopWatering();
	forceWatering = false;
	res.send("Turning off");
});