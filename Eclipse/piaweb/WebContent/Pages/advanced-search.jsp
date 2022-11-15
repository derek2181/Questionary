<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.piaweb.models.*,java.util.List,com.piaweb.viewmodels.QuestionDetailsViewModel" %>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%
	User user = (User)session.getAttribute("user");
	User userProfile = (User)request.getAttribute("userInfo");
	
	List<QuestionDetailsViewModel> preguntas= (List<QuestionDetailsViewModel>)request.getAttribute("preguntasEncontradas");
	
	String busqueda=(String)request.getAttribute("busqueda");
	int numberOfPages = (int)request.getAttribute("numberOfPages");
	int pageNumber = (int)request.getAttribute("pageNumber");
	Category category = (Category)request.getAttribute("category");
%>
<!DOCTYPE html>
<html lang="en">

<head>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-giJF6kkoqNQ00vy+HMDP7azOuL0xtbfIcaT9wjKHr8RbDVddVHyTfAAsrekwKmP1" crossorigin="anonymous">
    <%@ include file="/Pages/Layouts/head.jsp" %>
    <link rel="stylesheet" href="/piaweb/Stylesheets/advanced-search.css">
    <title>Questionary</title>
</head>

<body>
    <div class="main-container">

       <%@include file="/Pages/Layouts/header.jsp" %>
        <main>
            <form action="/piaweb/Busqueda" method="GET">
                <section class="advanced-search-block">
                    <div class="search-bar-advanced-search">
                        <input type="search" value="<%=busqueda %>" placeholder="Buscar pregunta..." name="searchbox" id="">
                        <button><i class="fas fa-search"></i></button>
                    </div>

                    <div class="inputs-advanced-search">
                        <div class="block-advanced-search">
                            <label for="votos">Votos: </label>
                            <select name="votos">
                            <option value="0" selected>Todas</option>
                            <option value="1">0-5</option>
                            <option value="2">5-10</option>
                            <option value="3">10-15</option>
                        </select>
                        </div>
                        <div class="block-advanced-search">
                            <label for="categories">Categorias: </label>
                            <select name="categories">
                            <option value="Todas">Todas</option>
                            <%for(Category categoryItem : categories){ %>
                            	<option value="<%=categoryItem.getNombre()%>"><%=categoryItem.getNombre()%></option>
                            <%} %>
                        </select>
                        </div>
                        <div class="block-advanced-search">
                            <label for="favoritos">Favoritos: </label>
                            <select name="favoritos">
                            <option value="0" selected>Todas</option>
                            <option value="1">0-5</option>
                            <option value="2">5-10</option>
                            <option value="3">10-15</option>
                        </select>
                        </div>

                    </div>

                    <div class="dates-advanced-search">
                        <div class="block-dates">
                            <label for="fechaInicio">Desde:</label>
                            <input type="date" name="fechaInicio" id="">
                        </div>
                        <div class="block-dates">
                            <label for="fechaFin">Hasta:</label>
                            <input type="date" name="fechaFin" id="">
                        </div>
                    </div>

                </section>
            </form>
	<%if(preguntas!=null){ %>

            <section class="questions-block">
            	<% for(QuestionDetailsViewModel pregunta : preguntas ) {%>
                <div class="question">
                    <div class="picture-question">
                    <a href="/piaweb/Perfil/Preguntas?userID=<%=pregunta.getIdUsuario()%>">
                        <img src="/piaweb/Imagenes/Usuario?imageID=<%=pregunta.getIdUsuario()%>" alt="">
                        </a>
                    </div>
                    <div class="question-description">
                        <h2><a href="/piaweb/DetallesPregunta?questionID=<%=pregunta.getQuestion().getID_Pregunta()%>">¿<%=pregunta.getQuestion().getEncabezado() %>?</a></h2>
                    </div>
                    <div class="question-date">
                        <i class="far fa-calendar-alt"></i> <span><%= pregunta.getQuestion().getFechaFormat() %></span>
                        <a href="/piaweb/Categorias?categoryID=<%=pregunta.getIdCategoria()%>"><%=pregunta.getNombreCategoria() %></a>
                        <div class="question-reactions">
                          <span><%= pregunta.getLikes() %></span> <i class="far fa-thumbs-up"></i>
                          <%if(user!=null){%>
                       
                          <% if(user.getID_Usuario().equals(pregunta.getIdUsuario())){ %>
                                <span><%=pregunta.getDislikes()%></span> <i class="far fa-thumbs-down"></i>
                                <%}else{  %>
                                <span></span> <i class="far fa-thumbs-down"></i>
                                <%} %>
                               <%}else{ %>  
                                 <span></span> <i class="far fa-thumbs-down"></i>
                                 <%} %>
                        </div>
                    </div>
                </div>
                <%} %>
		
            </section>
            <%} if(preguntas.isEmpty()){ %>
			<div class="no-results">
                <h1>No se encontraron resultados...</h1>
                <i class="fas fa-sad-tear"></i>
            </div>
            <%} %>
        </main>
           <%if(pageNumber<=numberOfPages){ %>
        <div class="page-controls">
     
           	<%if(pageNumber != 1) {%>
            <a href="/piaweb/Busqueda?searchbox=<%=busqueda%>&pageNumber=<%=pageNumber -1 %>" class="btn-change-page">
            &lt;</a>
            <%} %>
            <%if(pageNumber != numberOfPages){ %>
            <a href="/piaweb/Busqueda?searchbox=<%=busqueda%>&pageNumber=<%=pageNumber +1 %>" class="btn-change-page btn-next-page">&gt;</a>
            <%} %>
        </div>
        <%} %>
    </div>
    <%@ include file="/Pages/Layouts/footer.jsp" %>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta1/dist/js/bootstrap.bundle.min.js" integrity="sha384-ygbV9kiqUc6oa4msXn9868pTtWMgiQaeYH7/t7LECLbyPA2x65Kgf80OJFdroafW" crossorigin="anonymous"></script>

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

        function dropdown_selection1(elemento) {
            let dropDown1 = document.getElementById("dropdownMenuButton1");
            if (elemento === "Ninguna") {
                dropDown1.innerHTML = "Votos";
            } else {
                dropDown1.innerHTML = elemento
            }

        }

        function dropdown_selection2(elemento) {
            let dropDown2 = document.getElementById("dropdownMenuButton2");
            if (elemento === "Ninguna") {
                dropDown2.innerHTML = "Favoritos";
            } else {
                dropDown2.innerHTML = elemento
            }
        }
    </script>
</body>

</html>