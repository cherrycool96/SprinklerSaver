var LCDcontroller = require("./modules/LCD_controller.js");
var buttonController = require("./modules/button_controller.js");
var sprinklers = require("./modules/sprinkler_controller.js");
var weather = require("./modules/weather_getter.js");
var logic = require("./modules/logic.js");
var schedule = require("./modules/schedule.js");
var analytics = require("./modules/analytics.js");
var API = require("./modules/API.js").init(startWater, stopWater);
var Util = require("./modules/utils.js");

var systemLoop;

function init () {
	LCDcontroller.setText(" SmartSprinkler ");
	// LCDcontroller.setText("================", {x:0, y:1});
	LCDcontroller.setBrightness(0);
	LCDcontroller.setColor(0, 255, 0);
	LCDcontroller.brighten();
	// dim the screen after a few seconds
	setTimeout(function () {
		LCDcontroller.dim();
	}, schedule.LCDtimeout);

	console.log(calculateTimeToNextWater());
	setTimeout(startWater, 10000);
	systemLoop = setInterval(function () {
		if (buttonController.btnStopWatering()) {
			stopWater();
		}
	}, 25);
}

function calculateTimeToNextWater () {
	// timeToNextWatering is assuming that the watering is within 24 hours of the current time. We account for this assumption later
	var timeToNextWatering = Util.MinsToMillis(schedule.timeToWater - Util.TimeToMins(Util.date.getHours(), Util.date.getMinutes()));
	timeToNextWatering = (timeToNextWatering >= 0) ? timeToNextWatering : Util.TimeToMillis(24, 0) - timeToNextWatering;
	// // accounting for the aforementioned assumption here
	var daysToNextWatering = Util.date.getDay() - schedule.daysToWater;
	daysToNextWatering = (daysToNextWatering >= 0) ? daysToNextWatering : 7 - daysToNextWatering;
	// // now we calculate the actual milliseconds until the next watering, including days
	timeToNextWatering += Util.DaysToMillis(daysToNextWatering);

	return timeToNextWatering;
}

function startWater () {
	if (!sprinklers.getState()) {
		weather.requiresWatering(function (weatherWater) {
			if (logic.decideToWater(API.forceWatering, weatherWater, weather.climatePermitsWatering())) {
				API.forceWatering = false;
				sprinklers.setState(true);
				setTimeout(function () {
					stopWater();
				}, schedule.durationOfWater);

				LCDcontroller.setText("  ~ Watering ~  ");
				// LCDcontroller.setText("================", {x:0, y:1});
				LCDcontroller.setColor(0, 100, 255);
				LCDcontroller.brighten();
				// dim the screen after a few seconds
				setTimeout(function () {
					LCDcontroller.dim();
				}, schedule.LCDtimeout);

				setTimeout(startWater, calculateTimeToNextWater());
			}
		});
	}
}

function stopWater () {
	if (sprinklers.getState()) {
		sprinklers.setState(false);
		LCDcontroller.setText(" SmartSprinkler ");
		// LCDcontroller.setText("================", {x:0, y:1});
		LCDcontroller.setColor(0, 255, 0);
		LCDcontroller.brighten();
		// dim the screen after a few seconds
		setTimeout(function () {
			LCDcontroller.dim();
		}, schedule.LCDtimeout);
	}
}

init();