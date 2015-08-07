java.lang.System.out.println("Starting up");

var require = Packages.portfolio.Portfolio3.require;

var console = {
	log: function (l) {
		java.lang.System.out.println(l);
	}
}


var app = express();

app.use(express.queryParser());
app.use(express.bodyParser());
app.use(sessionHandler());


var requireLogin = function(req, res, next) {
	if ( req.session.loggedIn == false ) {
		app.load('/authfail');
	}
	else {
		next();
	}
};

app.get('/user', requireLogin, function(req, res, next) {

});
