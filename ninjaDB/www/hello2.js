// Try this: http://localhost:8080/hello.js?John
var x = require('module1');

out.print(module1.func1());

function main (out, response, req) {

	var messages = req.getQueryString();

	response.setContentType("text/html;charset=utf-8");

	for (var i = 1;i<1000;i++) {
		out.println( '<br>' + i + ' Hello world!!' + messages + " ...");

	}
	
}