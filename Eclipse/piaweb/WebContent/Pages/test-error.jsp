<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8" isErrorPage="true"%>
<%@ page import="com.piaweb.models.*, java.util.*" %>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%
	String mensaje = (String)request.getAttribute("mensaje");
	String mensajeExcepcion = (String)request.getAttribute("mensajeError");
%>
<html>
<body>
	<h1>Ha ocurrido un error Codigo de error: <%=mensaje %></h1>
	<p>Error: <%=mensajeExcepcion%></p>
</body>

</html>
