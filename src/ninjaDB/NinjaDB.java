

package ninjaDB;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;



 
public class NinjaDB extends AbstractHandler
{
	
	static ScriptEngine engine;
	static int a=0;

    public void handle(String target,
                       Request baseRequest,
                       HttpServletRequest request,
                       HttpServletResponse response) 
        throws IOException, ServletException 
    {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        baseRequest.setHandled(true);
        
        PrintWriter out = response.getWriter();
        String rcs  = request.getRequestURI();// ; getPathTranslated();// request.getQueryString();
        String qrystr = request.getQueryString();
        System.out.println(rcs);
        
        // Now just return "404" for plain resources
        if (rcs.equals("/favicon.ico")) {
        	response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;	
        }
        
        // Run the script engine
		try {
			
			// if (a++ == 0) {
				File scriptFile = new File("www" + rcs);
		        BufferedReader reader = new BufferedReader(new FileReader(scriptFile));
	
				// evaluate JavaScript code that defines a function with one parameter
		        // engine.eval("function hello(out) { out.println('Yes !!') }");
		        engine.eval(reader);
			// }

	        
	        // create an Invocable object by casting the script engine object
	        Invocable inv = (Invocable) engine;

	        // invoke the function named "main" with "Scripting!" as the argument

	        //rcs.substring(1)
	        
	        Object p = inv.invokeFunction("main", out, response , request);
	        // Object p = inv.invokeFunction(rcs.substring(1), out, response , request);
	        
	        // inv.invokeFunction("main2", p);
	        
	        
		} catch (Error | Exception e) {
		//} catch (ScriptException | NoSuchMethodException e) {
					// TODO Auto-generated catch block
			   response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			e.printStackTrace();
		}
          
    }
 
    public static void main(String[] args) throws Exception
    {
        engine = new ScriptEngineManager().getEngineByName("nashorn");
    	
        // Just simple hard-code for test purposes
        engine.eval("load('www/startup.js');");
        
    	Server server = new Server(8080);
        server.setHandler(new NinjaDB());
        server.start();
        server.join();
    }
    
    public static Object require (String n) throws Exception
    {

    	System.out.println("Loading module : " + n);
    	
    	StringBuilder s = new StringBuilder();
    	s.append("function exporter() {var exports;");
    	
    	// Run the script engine
		File scriptFile = new File("www/" + n);
	    
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
 

