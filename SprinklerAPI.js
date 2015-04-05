var unirest = require('unirest');
//var uri = require('uri');

var SPRINKLE_API_KEY = 'aXX2ttuLlwmshyoAQR3iEcYe2S2Kp1WyuQKjsnRFCMsDP5jxLn';
var BASE_SPRINKLE_URL = 'https://community-open-weather-map.p.mashape.com/find?q=';

function buildSprinkleURL(location) {
	return BASE_SPRINKLE_URL + location;
}

function requiresWatering(callback) {
	var weather = undefined;

	unirest.get(buildSprinkleURL('los+angeles'))
		.header("X-Mashape-Key", SPRINKLE_API_KEY)
		.header("Accept", "text/plain")
		.end(function (result) {
			console.log("got data");
			var jsonResult = JSON.stringify(result.body, null, 4);
			var contents = JSON.parse(jsonResult);
			weather = contents.list[0].weather[0].main;
			callback(weather === 'Rain');
			// return weather === 'Rain'
		});

	return weather;
}

requiresWatering(function (weatherWater) {
	console.log(weatherWater);
});





// var unirest = require('unirest');

// var SPRINKLE_API_KEY = 'aXX2ttuLlwmshyoAQR3iEcYe2S2Kp1WyuQKjsnRFCMsDP5jxLn';
// var BASE_SPRINKLE_URL = 'https://community-open-weather-map.p.mashape.com/daily?';

// function buildSprinkleURL(cnt, location) {
//   return BASE_SPRINKLE_URL + 'cnt=' + cnt + '&q=' + location;
// }

// unirest.get(buildSprinkleURL(1, 'los+angeles'))
// .header("X-Mashape-Key", SPRINKLE_API_KEY)
// .header("Accept", "text/plain")
// .end(function (result) {
//   var jsonResult = JSON.stringify(result.body, null, 4);
//   var JSO = JSON.parse(jsonResult);
//   console.log(JSO);
// });