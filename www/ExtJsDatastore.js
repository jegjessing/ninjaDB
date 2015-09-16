exports =  function(req, response) {

	var out = response.getWriter();
	var qryStr  = req.getQueryString();

	response.setContentType("application/json;charset=utf-8");
	
	var  stmt = con.createStatement();
	// var  rs   = stmt.executeQuery("SELECT * from qgpl.items");
	var  rs   = stmt.executeQuery(req.getParameter("sql"));
	var  rsmd = rs.getMetaData();
    
	var cols  = rsmd.getColumnCount();
    var comma = '';
    
    out.print('{ "rows": [');
    while (rs.next()) {
    
    	out.print(comma + '{');
        for (var i = 1; i <= cols; i++) {
        	out.print('"' + rsmd.getColumnName(i).toLowerCase() + '":');
        	var type = rsmd.getColumnType(i);
        	
            if (type == 3) {
                out.print(rs.getString(i));
            } else {
  		       out.print('"' + ninja.jsonEncode(rs.getString(i)) + '"');
            }
            
        	if (i<cols) out.print(',');
        } 
        out.print('}');
		comma = ',';
    }
    out.print(']}');
	
};    