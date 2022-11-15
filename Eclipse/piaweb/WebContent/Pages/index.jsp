<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="com.piaweb.models.*,java.util.*,com.piaweb.viewmodels.*" %>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%
	User user = (User)session.getAttribute("user");
	//List<Category> categories = (List<Category>)request.getAttribute("categories"); 
		List<QuestionCardViewModel> preguntas=(List<QuestionCardViewModel>)request.getAttribute("questions");
		int numberOfPages = (int)request.getAttribute("numberOfPages");
		int pageNumber = (int)request.getAttribute("pageNumber");
		
%>
<!DOCTYPE html>
<html>

<head>
	<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-giJF6kkoqNQ00vy+HMDP7azOuL0xtbfIcaT9wjKHr8RbDVddVHyTfAAsrekwKmP1" crossorigin="anonymous">
    <%@ include file="/Pages/Layouts/head.jsp" %>
    <title>Questionary</title>
   
</head>

<body>
    <div class="main-container">
       <%@include file="/Pages/Layouts/header.jsp" %>
        <div class="middle-container">
            <h1 id="top">Preguntas mas recientes</h1>
            
            <div class="questions">
         <%if(preguntas!=null){ %>
                <% for(QuestionCardViewModel pregunta : preguntas) { %>
                    <div class="question">
                        <div class="picture-question">
                          <a href="/piaweb/Perfil/Preguntas?userID=<%=pregunta.getIdUsuario()%>"> <img src="/piaweb/Imagenes/Usuario?imageID=<%=pregunta.getIdUsuario() %>" alt=""></a> 
                        </div>
                        <div class="question-description">
                            <h2><a href="/piaweb/DetallesPregunta?questionID=<%=pregunta.getIdPregunta()%>">¿<%= pregunta.getEncabezado()  %>?</a></h2>
                        </div>
                        <div class="question-date">
                            <i class="far fa-calendar-alt"></i> <span><%=pregunta.getFechaFormat()  %> </span>
                            <a href="/piaweb/Categorias?categoryID=<%=pregunta.getIdCategoria()%>"><%=pregunta.getNombreCategoria()  %></a>
                            <div class="question-reactions">
    					  <span><%= pregunta.getLikes() %></span><i class="far fa-thumbs-up"></i>
    					  <%if(user!=null){ %>
    					  <%if(user.getID_Usuario().equals(pregunta.getIdUsuario())){ %>
    					   <span><%= pregunta.getDislikes() %></span> <i class="far fa-thumbs-down"></i>
    					   <%}%>
    					   <%}%>
                            </div>
                        </div>
                    </div>
                    <%} %>
                    <%} %>
            </div>
  				<%if(numberOfPages!=0) {%>
                     <div class="page-controls">
          		<%if(pageNumber != 1) {%>
            		<a href="/piaweb?pageNumber=<%=pageNumber -1 %>" class="btn-change-page">
           		 &lt;</a>
           			 <%} %>
          		  <%if(pageNumber != numberOfPages){ %>
          			  <a href="/piaweb?pageNumber=<%=pageNumber +1 %>" class="btn-change-page btn-next-page">&gt;</a>
        		  <%} %>
       			 </div>
       			 <%} %>
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
</body>

</html>