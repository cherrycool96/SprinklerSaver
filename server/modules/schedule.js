var Util = require("./utils.js");

// module.exports = {
// 	daysToWater: [2],
// 	timeToWater: Util.TimeToMins(3, 30),
// 	durationOfWater: Util.MinsToMillis(30),
// 	LCDtimeout: Util.MinsToMillis(0.2)
// };

module.exports = {
	daysToWater: [Util.date.getDay()],
	timeToWater: Util.TimeToMins(Util.date.getHours(), Util.date.getMinutes() + 0.3),
	durationOfWater: Util.MinsToMillis(0.3),
	LCDtimeout: Util.MinsToMillis(0.08)
};