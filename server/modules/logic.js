module.exports = {
	decideToWater: function (user, weather, climate) {
		if (user || (weather && climate)) {
			return true;
		}
		return false;
	}
};
