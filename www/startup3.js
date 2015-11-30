/*  -----------------------------------------------
Startup on an IBMi:
Or submitted:
SBMJOB CMD(QSH CMD('cd /www;export JAVA_HOME=/QopenSys/QIBM/ProdData/JavaVM/jdk80/32bit;java -jar ninjadb.jar     
             www/startup.js'))                                   
JOB(NINJA)                                                
JOBQ(QSYSNOMAX)      
*/           

// -----------------------------------------------
// This is a super simple web server 
// the "router" is simply serving static files
// -----------------------------------------------

// Always configure as the very first: 
ninja.sys.setPort(8082);
ninja.sys.setWebRoot( "/users/nli/ext-6.0.0");
ninja.sys.setAppRoot( "www");

// Now the path is set for requirement:
var console = require('console.js');
console.log("Starting up - extjs demo");

// -----------------------------------------------
// "router" is the thread callback from the server
// this sample handles all 
// -----------------------------------------------
function router(req, response) {
	ninja.sys.serveFile(req.getRequestURI(), response);
}