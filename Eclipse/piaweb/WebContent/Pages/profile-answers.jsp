<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.piaweb.models.*,java.util.*,java.sql.Blob,java.text.DateFormat, java.text.SimpleDateFormat" %>

<%@ include file="/Pages/Layouts/profile-variables.jsp" %>

<!DOCTYPE html>
<html>
<head>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-giJF6kkoqNQ00vy+HMDP7azOuL0xtbfIcaT9wjKHr8RbDVddVHyTfAAsrekwKmP1" crossorigin="anonymous">
    <%@ include file="/Pages/Layouts/head.jsp" %>
    <link rel="stylesheet" href="/piaweb/Stylesheets/Profile.css">
    <title>Questionary - Perfil</title>
</head>
<body>
<%@include file="/Pages/Layouts/modal.jsp" %>

    <div class="main-container">

        <%@include file="/Pages/Layouts/header.jsp" %>
        <div class="middle-container">
            <%@include file="/Pages/Layouts/profile-top.jsp" %>
            <div class="profile-content">


               <div class="profile-options">
             		<c:url var="linkProfileQuestions" value="Preguntas">
                     	<c:param name="userID" value="<%=userProfile.getID_Usuario() %>"></c:param>
                    </c:url>
                	<c:url var="linkProfileAnswers" value="Respuestas">
                     	<c:param name="userID" value="<%=userProfile.getID_Usuario() %>"></c:param>
                    </c:url>
                    <c:url var="linkProfileFavorites" value="Favoritos">
                     	<c:param name="userID" value="<%=userProfile.getID_Usuario() %>"></c:param>
                    </c:url>
                    <c:url var="linkProfileVotes" value="Votos">
                     	<c:param name="userID" value="<%=userProfile.getID_Usuario() %>"></c:param>
                    </c:url>
                    <c:url var="linkProfileDislikes" value="Dislikes">
                     	<c:param name="userID" value="<%=userProfile.getID_Usuario() %>"></c:param>
                    </c:url>
     				  <a class="block-profile " href="${linkProfileQuestions}"><span>${totalOfQuestions}</span>Preguntas</a>
                    <a class="block-profile" href="${linkProfileFavorites}"><span>${totalOfFavs} </span>Favoritos</a>
                    <a class="block-profile" href="${linkProfileVotes}"><span>${totalOfLikes}</span>Votos</a>
                    <a class="block-profile  option-selected " href="${linkProfileAnswers}"><span>${totalOfAnswers}</span>Respuestas</a>
                    <%if(user!=null && userProfile.getID_Usuario().equals(user.getID_Usuario())){ %>
                    <a class="block-profile" href="${linkProfileDislikes}"><span>${totalOfDislikes}</span>Dislikes</a>
                    	<% } %> 
                </div>

                <div class="profile-stuff">
                  <%if(preguntas!=null){ %>
                <% for(QuestionDetailsViewModel pregunta : preguntas) { %>
                    <div class="question">
                        <div class="picture-question">
                        <a href="/piaweb/Perfil/Preguntas?userID=<%=pregunta.getIdUsuario()%>"><img src="/piaweb/Imagenes/Usuario?imageID=<%=pregunta.getIdUsuario() %>"></a>  
                        </div>
                        <div class="question-description">
                            <h2><a href="#"><%=	pregunta.getQuestion().getDescripcion() %></a></h2>
                        </div>
                        <div class="question-date watch-question">
                            <section>
                                <i class="far fa-calendar-alt"></i>
                                <span><%=pregunta.getQuestion().getFechaFormat()  %></span>
                            </section>

                            <a href="/piaweb/DetallesPregunta?questionID=<%=pregunta.getQuestion().getID_Pregunta() %>">Ver pregunta</a>
                        </div>
                    </div>
                       <%} %>
                    <%} %>
 					<%if(numberOfPages!=0){%>
                     <div class="page-controls">
          		<%if(pageNumber != 1) {%>
            		<a href="/piaweb/Perfil/Respuestas?userID=<%=userProfile.getID_Usuario()%>&pageNumber=<%=pageNumber -1 %>" class="btn-change-page">
           		 &lt;</a>
           			 <%} %>
          		  <%if(pageNumber != numberOfPages){ %>
          			  <a href="/piaweb/Perfil/Respuestas?userID=<%=userProfile.getID_Usuario()%>&pageNumber=<%=pageNumber +1 %>" class="btn-change-page btn-next-page">&gt;</a>
        		  <%} %>
       			 </div>
          	
       			  <%} %>
                </div>

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

            $('#image').change(function() {
                setFileName();
            });
            setFileName();
            
            function menu_fixed() {
                if ($(this).scrollTop() > 0) {
                    $('.header').addClass('fixed');
                } else $('.header').removeClass('fixed');
            }
            
            function setFileName() {
                var fileSelected = $('#image').prop("files")[0];
                $('#fileDetails').text(fileSelected.name + ' - ' + fileSelected.size / 1000 + ' KB');
            }
            // alert("Esto es una prueba");
        })
    </script>
	<script> 
	var modal = document.querySelector('.btn-modal');
	var modalBg = document.querySelector('.modal-bg');
	var close_modal = document.querySelector('.close-modal');
	modal.addEventListener('click', () => {
	    modalBg.classList.add('bg-active');
	});
	
	close_modal.addEventListener('click', () => {
	    modalBg.classList.remove('bg-active');
	}); 
	
	
	</script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta1/dist/js/bootstrap.bundle.min.js" integrity="sha384-ygbV9kiqUc6oa4msXn9868pTtWMgiQaeYH7/t7LECLbyPA2x65Kgf80OJFdroafW" crossorigin="anonymous"></script>

</body>

</html>