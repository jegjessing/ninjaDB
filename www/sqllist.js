exports =  function(req, response) {

	var out = response.getWriter();
	var qryStr  = req.getQueryString();

	response.setContentType("text/html;charset=utf-8");
	
	var  stmt = con.createStatement();
	// var  rs   = stmt.executeQuery("SELECT * from qgpl.items");
	var  rs   = stmt.executeQuery(req.getParameter("sql"));
	var  rsmd = rs.getMetaData();
    
	var cols  = rsmd.getColumnCount();
    
    out.println('<html><table>');
    while (rs.next()) {
    	out.println('<tr>');
        for (var i = 1; i <= cols; i++) {
        	out.print('<td>' + rs.getString(i) + '</td>');
        } 
        out.println('</tr>');
    }
    out.println('</table></html>');
	
};    