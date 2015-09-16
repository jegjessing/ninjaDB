exports =  function(req, response) {

	var out = response.getWriter();
	var messages = req.getQueryString();

	response.setContentType("text/html;charset=utf-8");

	console.log('I was "required" in ');
	
	for (var i = 1; i <= 100 ; i++) {
		out.println( '<br>'  + ':'+ i + ' Hello from myapp.js !! My ÆØÅ query string is: ' + messages + " ...");
	}
};
