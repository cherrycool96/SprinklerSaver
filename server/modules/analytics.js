module.exports = {
	addWatering: function (time, duration) {
		graphData.push({time: time, duration: duration});
	},
	getDataAsGraph: function () {
		var out = [];
		for (var i = 0; i < graphData.length; i++) {
			out.push({x:graphData[i].time, y:graphData[i].duration});
		}
		return out;
	}
};

var graphData = [];