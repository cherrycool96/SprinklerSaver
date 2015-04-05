module.exports = {
	setText: function (msg, pos, clear) {
		if (clear === undefined)
			clear = true
		if (clear) {
			myLcd.clear();
			myLcd.setCursor(0, 0);
		}
		if (pos === undefined) {
			pos = {x:0, y:0};
		}
		myLcd.setCursor(pos.y, pos.x);
		myLcd.write(msg);
		myLcd.setCursor(0, 0);
	},
	setColor: function (r, g, b) {
		toColor = {r:r, g:g, b:b};

		intervalColor = setInterval(function () {
			if ((color.r === toColor.r) && 
				(color.g === toColor.g) && 
				(color.b === toColor.b)) {
				clearInterval(intervalColor);
			} else {
				color.r = (color.r < toColor.r) ? color.r + 2 : color.r - 2;
				color.g = (color.g < toColor.g) ? color.g + 2 : color.g - 2;
				color.b = (color.b < toColor.b) ? color.b + 2 : color.b - 2;
				// catch bounds
				color.r = (color.r < 0) ? 0 : color.r;
				color.g = (color.g < 0) ? 0 : color.g;
				color.b = (color.b < 0) ? 0 : color.b;
				color.r = (color.r > 255) ? 255 : color.r;
				color.g = (color.g > 255) ? 255 : color.g;
				color.b = (color.b > 255) ? 255 : color.b;
			}
		}, 1);
		myLcd.setColor(color.r * brightness, color.g * brightness, color.b * brightness);
	},
	brighten: function () {
		var intervalBrightness = setInterval(function () {
			if (brightness >= 1) {
				brightness = 1;
				clearInterval(intervalBrightness);
			}
			else
				brightness += 0.0025;
			myLcd.setColor(color.r * brightness, color.g * brightness, color.b * brightness);
		}, 1)
	},
	dim: function () {
		intervalBrightness = setInterval(function () {
			if (brightness <= 0) {
				brightness = 0;
				clearInterval(intervalBrightness);
			}
			else
				brightness -= 0.0025;
			myLcd.setColor(color.r * brightness, color.g * brightness, color.b * brightness);
		}, 2)
	},
	setBrightness: function (b) {
		brightness = b;
		myLcd.setColor(color.r * brightness, color.g * brightness, color.b * brightness);
	}
};

//Load i2clcd module
var LCD = require('jsupm_i2clcd');
//Initialize Jhd1313m1 at 0x62 (RGB_ADDRESS) and 0x3E (LCD_ADDRESS) 
var myLcd = new LCD.Jhd1313m1 (0, 0x3E, 0x62);

myLcd.scroll(false);
myLcd.home();
myLcd.clear();
myLcd.setCursor(0, 0);
// myLcd.home();
myLcd.write("Starting...");
myLcd.setCursor(0, 0);



var color = {r: 255, b: 255, g:255};
var toColor = {r: 255, b: 255, g:255};
var brightness = 1;
var intervalBrightness, intervalColor;