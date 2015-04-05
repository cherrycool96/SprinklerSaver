module.exports = {
	TimeToMins: function (hours, minutes) {
		return hours * 60 + minutes;
	},
	TimeToMillis: function (hours, minutes) {
		return this.MinsToMillis(hours * 60 + minutes);
	},
	MinsToMillis: function (mins) {
		return mins * 60000;
	},
	DaysToMillis: function (days) {
		return days * 1440000;
	},
	date: new Date()
};
