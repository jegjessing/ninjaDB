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
ninja.setPort(8082);
ninja.setWebRoot( "/users/nli/Desktop/ext-6.0.0");
ninja.setAppRoot( "www");

// Now the path is set for requirement:
var console = require('console.js');
console.log("Starting up");

// -----------------------------------------------
// "router" is the thread callback from the server
// this sample handles all 
// -----------------------------------------------
function router(req, response) {
	ninja.serveFile(req.getRequestURI(), response);
}