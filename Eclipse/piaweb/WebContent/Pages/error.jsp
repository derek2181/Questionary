<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8" isErrorPage="true"%>
<%@ page import="com.piaweb.models.*, java.util.*" %>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%
	User user = (User)session.getAttribute("user");
%>
<!DOCTYPE html>
<html>

<head>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-giJF6kkoqNQ00vy+HMDP7azOuL0xtbfIcaT9wjKHr8RbDVddVHyTfAAsrekwKmP1" crossorigin="anonymous">
   	<%@ include file="/Pages/Layouts/head.jsp" %>
    <link rel="stylesheet" href="/piaweb/Stylesheets/error-styles.css">
    <title>Questionary</title>
</head>
<body>
    <div class="main-container">
       <%@include file="/Pages/Layouts/header.jsp" %>
         <div class="middle-container">
         	<div class="error-card">
                <h1 id="top">Upss.. No se ha encontrado lo que estabas buscando</h1>
                <img src="/piaweb/Images/error-image.png" alt="">
             </div>
        </div>
         <%@ include file="/Pages/Layouts/footer.jsp" %>
    </div>
    <script>
        $(document).ready(function() {
            $(window).scroll(function() {
                menu_fixed();
            });
            menu_fixed();
            $('#toTop').click(function() {

                $("html, body").scrollTop(0);
                // $('html, body').stop().animate({scrollTop:0}, 2000);
            });


            function menu_fixed() {
                if ($(this).scrollTop() > 0) {
                    $('.header').addClass('fixed');
                } else $('.header').removeClass('fixed');
            }
            // alert("Esto es una prueba");
        })
    </script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta1/dist/js/bootstrap.bundle.min.js" integrity="sha384-ygbV9kiqUc6oa4msXn9868pTtWMgiQaeYH7/t7LECLbyPA2x65Kgf80OJFdroafW" crossorigin="anonymous"></script>
    </div>
</body>
</html>