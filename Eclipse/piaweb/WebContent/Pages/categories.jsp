<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="com.piaweb.models.*, com.piaweb.viewmodels.*, java.util.*" %>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%
	User user = (User)session.getAttribute("user");
	int numberOfPages = (int)request.getAttribute("numberOfPages");
	int pageNumber = (int)request.getAttribute("pageNumber");
	List<QuestionCardViewModel> questions = (List<QuestionCardViewModel>)request.getAttribute("questions");
	Category category = (Category)request.getAttribute("category");
	//List<Category> categories = (List<Category>)request.getAttribute("categories"); 
%>
<!DOCTYPE html>
<html>
<head>
	<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-giJF6kkoqNQ00vy+HMDP7azOuL0xtbfIcaT9wjKHr8RbDVddVHyTfAAsrekwKmP1" crossorigin="anonymous">
    <%@ include file="/Pages/Layouts/head.jsp" %>
    <link rel="stylesheet" href="/piaweb/Stylesheets/categories.css">
    <title>Questionary - <%=category.getNombre() %></title>
</head>
<body>
    <div class="main-container">

         <%@include file="/Pages/Layouts/header.jsp" %>
        <div class="middle-container">
            <h1 id="top"><%=category.getNombre() %></h1>
            <div class="questions">

			<% for(QuestionCardViewModel questionCard : questions) {%>
                <div class="question">
                    <div class="picture-question">
                        <a href="/piaweb/Perfil/Preguntas?userID=<%=questionCard.getIdUsuario()%>"><img src="/piaweb/Imagenes/Usuario?imageID=<%=questionCard.getIdUsuario() %>" alt=""></a>
                    </div>
                    <div class="question-description">
                        <h2><a href="/piaweb/DetallesPregunta?questionID=<%=questionCard.getIdPregunta()%>">¿<%=questionCard.getEncabezado()%>?</a></h2>
                    </div>
                    <div class="question-date">
                        <i class="far fa-calendar-alt"></i> <span><%=questionCard.getFechaFormat() %></span>
                        <a href="/piaweb/Categorias?categoryID=<%=questionCard.getIdCategoria()%>"><%=questionCard.getNombreCategoria() %></a>
                        <div class="question-reactions">
                        
                            <span><%=questionCard.getLikes()%></span> <i class="far fa-thumbs-up"></i>
                            <%if(user!=null){ %>
                            <%if(user.getID_Usuario().equals(questionCard.getIdUsuario())){ %>
                            <span><%=questionCard.getDislikes() %></span> <i class="far fa-thumbs-down"></i>
                            <%}else{ %>
                             <span></span> <i class="far fa-thumbs-down"></i>
                            <%} %>
                            <%}else{ %>
                            	       <span></span> <i class="far fa-thumbs-down"></i>
                            <%} %>
                        </div>
                    </div>
                </div>
			<%} %>
            </div>
        </div>
			
			<%if(numberOfPages!=0){%>
        <div class="page-controls categories">
        	
        	<%if(pageNumber != 1) {%>
            <a href="/piaweb/Categorias?categoryID=<%=category.getID_Categoria()%>&pageNumber=<%=pageNumber -1 %>" class="btn-change-page">
            &lt;</a>
            <%} %>
            <%if(pageNumber != numberOfPages){ %>
            <a href="/piaweb/Categorias?categoryID=<%=category.getID_Categoria()%>&pageNumber=<%=pageNumber +1 %>" class="btn-change-page btn-next-page">&gt;</a>
            <%} %>
        </div>
        <%}else{ %>
        	<div class="no-questions-message">
				<h3>Esta categoria no tiene preguntas...</h3>
        		<p>¿Quieres ser el primero en hacer una pregunta?
        			<%if(user == null){ %> 
        			<a href="/piaweb/Acceso/Login?returnURL=piaweb/Preguntar?categoryID=<%=category.getID_Categoria()%>">Inicia sesión</a>
        			<%} else { %>
        				<a href="/piaweb/Preguntar?categoryID=<%=category.getID_Categoria()%>">Haz una pregunta</a>
        			<%} %>
       			</p>
        	</div>
        <%} %>
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