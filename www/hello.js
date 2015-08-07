// Try this: http://localhost:8080/hello.js?John
// var module1 = require('module1');
var p = require('module1.js');

// var db  = require('db2');

function main (out, response, req) {

	var messages = req.getQueryString();

	response.setContentType("text/html;charset=utf-8");

	p.print('That was deeper');
	
	for (var i = 1; i <= 100 ; i++) {
		out.println( '<br>'  + ':'+ i + ' Hello world!! My query string is: ' + messages + " ...");
		console.log(i);
	}
}
