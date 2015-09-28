exports = {
	log: function(s) {
		java.lang.System.out.println(s);
	},
	dir: function (obj) {
		exports.log(obj);
	    for (var property in obj) {
	        //if (obj.hasOwnProperty(property)) {
	            if (typeof obj[property] == "object") {
	            	exports.dir(obj[property]);
	            }
	            else {
	            	exports.log(property + "   " + obj[property]);
	            }
	        //}
	    }
	}
};
