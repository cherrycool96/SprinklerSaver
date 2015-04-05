module.exports = {
	requiresWatering: function (callback) {
		// TODO: replace with the API stuff
		var weather = undefined;

		unirest.get(buildSprinkleURL('los+angeles'))
			.header("X-Mashape-Key", SPRINKLE_API_KEY)
			.header("Accept", "application/json")
			.end(function (result) {
				console.log("got data");
				var jsonResult = JSON.stringify(result.body, null, 4);
				var contents = JSON.parse(jsonResult);
				weather = contents[0].condition;
				callback(weather != 'Rain');
				//return weather === 'Rain'
				var conditionLower = weather.toLowerCase()
				var condition = conditionLower.match('rain')
				return condition;
			});
	},
	climatePermitsWatering: function () {
		// TODO: replace with the API stuff
		return true;
	}
};


var unirest = require('unirest');

var SPRINKLE_API_KEY = 'aXX2ttuLlwmshyoAQR3iEcYe2S2Kp1WyuQKjsnRFCMsDP5jxLn';
var BASE_SPRINKLE_URL = 'https://george-vustrey-weather.p.mashape.com/api.php?location=';

function buildSprinkleURL(location) {
	return BASE_SPRINKLE_URL + location;
}