<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	if (request.getSession(false) == null) {
			response.sendRedirect("ClientConstructor");
	}
%>




<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Detailed Mqtt Package</title>

<link href="CSS/Exsheet.css" type="text/css" rel="stylesheet">

</head>
<body>
<div id = "nav"><form action="ClientConstructor" method="POST" ><h2 id="back_nav">
<input type = "image" src="pics/Home_Icon.png" id = "home_icon">Home</h2></form>
</div>
<div class = "Table_Block_JSON">
<table class = "Table_JSON_outer">

<tr>
    <th class = "T_Header"><b>Configuration Variables</b></th>
    <th class = "T_Header"><b>value</b></th>
</tr>
<c:out value = "${table_col_1}" escapeXml="false" />

</table>
</div>


<div class = "Table_Block_JSON">
<table class = "Table_JSON_outer">
   <tr>
      <th class = "T_Header"><b>Configuration Variables</b></th>
      <th class = "T_Header"><b>value</b></th>
   </tr>
<c:out value = "${table_col_2}" escapeXml="false" />

</table>
</div>

</body>
</html>