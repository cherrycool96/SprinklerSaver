module.exports = {
	init: function (startFunc, stopFunc, statusFunc, analyticsFunc) {
		startWatering = startFunc;
		stopWatering = stopFunc;
		getStatus = statusFunc;
		getAnalytics = analyticsFunc;

		app.listen(PORT, function(){
			console.log('listening on *:' + PORT);
		});

		return this;
	},
	forceWatering: false
};

var startWatering, stopWatering, getStatus, getAnalytics;
var PORT = 7655;
var app = require('express')();
var server = require('http').Server(app);

app.get("/SprinklersOn", function (req, res) {
	console.log("turning on by API");
	startWatering();
	forceWatering = true;
	res.send("Turning on");
});

app.get("/SprinklersOff", function (req, res) {
	console.log("turning off by API");
	stopWatering();
	forceWatering = false;
	res.send("Turning off");
});

app.get("/Status", function (req, res) {
	console.log("getting status by API");
	var status = getStatus();
	res.send("Status " + ((status) ? "on" : "off"));
});

app.get("/RegisterGCM", function (req, res) {
	console.log("setting GCM key by API");
	// var status = getStatus();
	res.send("fail");
});

app.get("/Analytics", function (req, res) {
	console.log("getting analytics by API");
	res.send("Analytics " + getAnalytics())
})