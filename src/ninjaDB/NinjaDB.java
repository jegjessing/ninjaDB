

package ninjaDB;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringEscapeUtils;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;
// FastCGI interface:
// https://eclipse.googlesource.com/jetty/org.eclipse.jetty.project/+/fbbe2426e50f40a884d253f189df317494341c33/jetty-fcgi/fcgi-proxy/src/test/java/org/eclipse/jetty/fcgi/proxy/WordPressSPDYFastCGIProxyServer.java
import org.eclipse.jetty.fcgi.server.proxy.FastCGIProxyServlet;

 
public class NinjaDB extends AbstractHandler
{

	// Defaults until set
	static int port = 8080;
	static String webroot = "./"; 
	static String approot = "./"; 
	static String defaultDoc = "index.html"; 
	
	
	// class globals
	static ScriptEngine engine;
	static Invocable startup;
	
	public static String fileExtention (String fileName) {
		String extension = "";

		// Strip parameters of
		int beg = 	fileName.indexOf('?');
		if (beg > 0) {
			fileName = fileName.substring(0, beg);
		}
		
		int i = fileName.lastIndexOf('.');
		if (i > 0) {
		    extension = fileName.substring(i+1);
		}
		return  extension;
	}
	
	// Simple hardcoded for now !! Todo!! load mime table
	public static String getMimeForResource  (String rcs) {
		String ext = fileExtention(rcs);
		if (ext.equals("js")) {
			return("application/javascript");
	    } else if (ext.equals("css")) {
	    	return("text/css");
	    } else if (ext.equals("png")) {
	    	return("image/png");
	    } else if (ext.equals("jpg")) {
	    	return("image/jpg");
	    } else if (ext.equals("gif")) {
	    	return("image/gif");
	    } else if (ext.equals("json")) {
	    	return("application/json");
	    } else if (ext.equals("pdf")) {
	    	return("application/pdf");
	    }
		return ("text/html");
	}
		
	public static void serveFile (String rcs, HttpServletResponse response) {
		
		OutputStream os = null;
		PrintWriter out = null;
		
		// TODO: add rules for default documents
		if (rcs.endsWith("/")) {
			rcs += defaultDoc;
		}
		
		response.setContentType(getMimeForResource(rcs));
		
		// serve "raw" files 
		try {
			os  = response.getOutputStream();
			
			FileInputStream fin = new FileInputStream(webroot + rcs);
			// File webFile  = new File(webroot + rcs);
		    
			final byte [] buffer = new byte[4096];
			// FileReader reader = new FileReader(webFile);
			
		    int len = 0;
		    while ((len = fin.read(buffer)) > 0) {
		        os.write(buffer, 0 , len);
		    }		   
		    fin.close();
		    
			
		} catch (FileNotFoundException e) {
		
			try {
				os.write(e.getMessage().getBytes());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		} catch (IOException e) {
			try {
				os.write(e.getMessage().getBytes());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			response.setStatus(HttpServletResponse.SC_FORBIDDEN );
		}
	
    	
    }
	
       
    public void handle(String target,
            Request baseRequest,
            HttpServletRequest request,
            HttpServletResponse response) 
	throws IOException, ServletException 
	{
		response.setContentType("text/html;charset=utf-8");
		response.setStatus(HttpServletResponse.SC_OK);
		baseRequest.setHandled(true);
		
		String rcs  = request.getRequestURI();
		
		// String qrystr = request.getQueryString();
		System.out.println(rcs);
		 
		try {
			Object p = startup.invokeFunction("router", request, response);
		} catch (NoSuchMethodException | ScriptException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	
	}
 
    
    
    public static void main(String[] args) throws Exception
    {
    	 
    	NinjaDB ninja = new NinjaDB();
    	ninja = new NinjaDB();
        engine = new ScriptEngineManager().getEngineByName("nashorn");
    	
        // Pre-defined mapping of build in classes
        // TODO !!! put in config file
        engine.eval(
        		"var ninja={};"+
        		"ninja.sys = Packages.ninjaDB.NinjaDB;" +
        		"var require = ninja.sys.require;" +
        		"ninja.db={};" +		
        		"ninja.db.connect = function (config) {" +
        		"	return ninja.sys.dbConnect(" +
        		"		config.driver, " +
        		"		config.server, "+ 
        		"		config.user," + 
        		"		config.password " +
        		"	);" +
        		"};"	
        );
        
        // deafault to "startup.js if none is given
        String startupFile = args.length >= 1 ? args[0] : "startup.js";  
        
        engine.eval("load('" + startupFile + "');");
        
        startup  = (Invocable) engine;
       
    	Server server = new Server(port);
    	server.setHandler(ninja);
        server.start();
        server.join();
    }
        

    public static void setPort  (String n) 
    {
    	System.out.println("Port is: " + n);
    	port = Integer.valueOf(n);
    }

    public static void setWebRoot  (String n) 
    {
    	System.out.println("Web document root is: " + n);
    	webroot = n;
    }
    
    public static void setAppRoot  (String n) 
    {
    	System.out.println("Application file root  is: " + n);
    	approot = n;
    }
    public  void setConfig   (Object o) 
    {	
    	System.out.println("config : " + o.toString() );
    }
    public static String jsonEncode  (String n) 
    {
    	if (n == null || n.isEmpty() ) return "";
    	
    	return StringEscapeUtils.ESCAPE_JSON.translate(n.trim());
    	
    }
    
    public static Connection dbConnect (String driver , String connection , String user , String pwd) throws Exception
    {
    	Connection con;
     	Class.forName(driver) ;
    	con = DriverManager.getConnection(connection , user , pwd);
    	return con;
    	// engine.put("con", con);
    }
    	
    
    public static Object require (String n) throws Exception
    {

    	System.out.println("Loading module : " + n);
    	
    	StringBuilder s = new StringBuilder();
    	s.append("function exporter() {var exports;");
    	
    	// Run the script engine
		File scriptFile = new File(approot + "/" + n);
	    
		final char[] buffer = new char[4096];
		FileReader reader = new FileReader(scriptFile);
		
	    int len = 0;
	    while ((len = reader.read(buffer)) > 0) {
	        s.append (buffer, 0, len);
	    }
	    reader.close();
	    
	    s.append("return exports;}");
    	
		// evaluate JavaScript code that defines a function with one parameter
        // engine.eval("function hello(out) { out.println('Yes !!') }");
        engine.eval(s.toString());
	
        
        // create an Invocable object by casting the script engine object
        Invocable inv = (Invocable) engine;

        // invoke the function named "main" with "Scripting!" as the argument
        Object p = inv.invokeFunction("exporter");
    	
    	
    	return p;
    }
}
 

