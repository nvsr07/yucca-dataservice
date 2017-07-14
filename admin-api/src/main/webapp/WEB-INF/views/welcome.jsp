<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Spring 4 MVC Hello World Example with Maven Eclipse</title>
<link rel='stylesheet' href='<c:url value="/resources/css/style.css" />' type='text/css' media='all' /> 
</head>

<body>

	<h2>Hello World, Spring MVC</h2>
	
	<p>-----------------------------------</p>
	
	<p>newDataType_id:          ${newDataType_id}</p>
	<p>newDataType_code:        ${newDataType_code}</p>
	<p>newDataType_description: ${newDataType_description}</p>
		
	<p>-----------------------------------</p>		
		
	<p>modifiedDataType_id,          ${modifiedDataType_id}</p>
	<p>modifiedDataType_code,        ${modifiedDataType_code}</p>
	<p>modifiedDataType_description, ${modifiedDataType_description}</p>

	<p>-----------------------------------</p>
			
	<p>${confirmDeleted}</p>

</body>

</html>