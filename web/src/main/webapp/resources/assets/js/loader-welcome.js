/* 
 _____   _           _         _                        _                  
|_   _| | |         | |       | |                      | |                 
  | |   | |__   __ _| |_ ___  | |_ ___  _ __ ___   __ _| |_ ___   ___  ___ 
  | |   | '_ \ / _` | __/ _ \ | __/ _ \| '_ ` _ \ / _` | __/ _ \ / _ \/ __|
 _| |_  | | | | (_| | ||  __/ | || (_) | | | | | | (_| | || (_) |  __/\__ \
 \___/  |_| |_|\__,_|\__\___|  \__\___/|_| |_| |_|\__,_|\__\___/ \___||___/

Oh nice, welcome to the js file of dreams.
Enjoy responsibly!
@ihatetomatoes

*/

$.fn.extend({
	//switchClass parameter is to replicate toggleClass functionality.
	toggleClassDelay: function (className, delay, switchClass)
	{
		this.toggleClass(className, switchClass);

		setTimeout($.proxy(function ()
		{
			this.toggleClass(className, switchClass);
		}, this), delay);
	}
});

$(document).ready(function() {
	$('#content').toggleClassDelay('ng-hide', 2500);
	$('#loader').toggleClassDelay('active', 2500);
});