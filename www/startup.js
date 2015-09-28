/* To run this on a IBMi :

cd /www;
export JAVA_HOME=/QopenSys/QIBM/ProdData/JavaVM/jdk80/32bit;
java -jar ninjadb.jar www/startup.js

Or submitted:
SBMJOB CMD(QSH CMD('cd /www;export JAVA_HOME=/QopenSys/QIBM/ProdData/JavaVM/jdk80/32bit;java -jar ninjadb.jar     
             www/startup.js'))                                   
JOB(NINJA)                                                
JOBQ(QSYSNOMAX)                                           

-------------------------------------------------- */

/* Define nija config before anything else*/
ninja.setPort(8080);
ninja.setWebRoot( "www");
ninja.setAppRoot( "www");

var con = ninja.dbConnect (
 "com.ibm.as400.access.AS400JDBCDriver",
 "jdbc:as400:DKSRV206",
 "DEMO",
 "demo"
);

// Require modules: 
var console = require('console.js');

console.log("Starting up");
// -----------------------------------------------
// "router" is the thread callback from the server
// This is a Simple router implementations 
// using a "routs" objects.. It is up to you
// -----------------------------------------------
function router(req, response) {
	var rcs  = req.getRequestURI();
	var method = req.getMethod().toLowerCase();
	var f = routs[rcs];
	var m;
	if ( ! f) {
		f = routs["*"];
    }
        
	if (f) {
		m = f[method];
		if (m) {
			m(req, response);
		} else {
			console.log('HTTP method ' + method + ' was not supported for' + rcs);
		} 
	} 	
}

// -----------------------------------------------
// For now  - just a simple global object
// -----------------------------------------------
var routs = { 

	"/helloworld" : {
		get  : function(req, resp) {
    
    		var out = resp.getWriter();
	    	var messages = req.getQueryString();

			resp.setContentType("text/html;charset=utf-8");

			console.log('That was deeper');
			
			for (var i = 1; i <= 100 ; i++) {
				out.println( '<br>'  + ':'+ i + ' Hello world!! My query string is: ' + messages + " ...");
			}
		}
    },


    "/ExtJsDatastore" : {
		get  : require("ExtJsDatastore.js")
    },
    
    "/sqllist" : {
		get  : require("sqllist2.js")
    },
    
    "/hello" : {
		get  : require("hello.js")
    },
    
    
	"/": { 
		get  : function(req, response) {
    		ninja.serveFile("/index.html", response);
		}
    },
    
	"404" : {
		get  : function(req, response) {
			var out = response.getWriter();
			response.setContentType("text/html;charset=utf-8");
			out.println( '<br>Upps '  + req.getRequestURI() + ' was not found');
		}
    },
    
	"*" : {
		get  : function(req, response) {
			ninja.serveFile(req.getRequestURI(), response);
		}
    },
    
};


