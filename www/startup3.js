// -----------------------------------------------
// This is a super simple web server 
// the "router" is simply serving static files
// -----------------------------------------------

// Always configure as the very first: 
ninja.setPort(8080);
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