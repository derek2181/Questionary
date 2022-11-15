<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.piaweb.models.*,com.piaweb.viewmodels.*,java.io.*, java.util.*" %>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%
	User user = (User)session.getAttribute("user");
	QuestionDetailsViewModel questionDetails = (QuestionDetailsViewModel)request.getAttribute("questionDetails");
	
	boolean showSuccessMessage = (boolean)request.getAttribute("showSuccessMessage");
	
	Answer answer = (Answer)request.getAttribute("answer");
	Map<String, String> errors = (HashMap<String,String>)request.getAttribute("errors");
	
	List<AnswerCardViewModel> answers = (List<AnswerCardViewModel>)request.getAttribute("answers");
	AnswerCardViewModel correctAnswer = (AnswerCardViewModel)request.getAttribute("correctAnswer");
	
	int numberOfPages = (int)request.getAttribute("numberOfPages");
	int pageNumber = (int)request.getAttribute("pageNumber");
	int totalOfAnswers = (int)request.getAttribute("totalOfAnswers");
	  //byte[] imgData = new byte[questionDetails.getQuestion().getImagen().available()];
%>
<!DOCTYPE html>
<html>
<head>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-giJF6kkoqNQ00vy+HMDP7azOuL0xtbfIcaT9wjKHr8RbDVddVHyTfAAsrekwKmP1" crossorigin="anonymous">
  	 <%@ include file="/Pages/Layouts/head.jsp" %>	
    <link rel="stylesheet" href="/piaweb/Stylesheets/question-detail-styles.css">
    <title>Questionary - ¿<%=questionDetails.getQuestion().getEncabezado() %>?</title>
</head>

<body>
    <div class="modal fade" id="confirmateDelete" tabindex="-1" aria-labelledby="confirmateDeleteLabel" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="confirmateDeleteLabel">¿Estas seguro que quieres eliminar esta pregunta?</h5>
            </div>
            <div class="modal-body">
                Si decides eliminar esta pregunta no podras volver a recuperarla
            </div>
            <div class="modal-footer">
                <button type="button" class="btn-cancel" data-bs-dismiss="modal">Cancelar</button>
                 <form action="/piaweb/DetallesPregunta" method="POST">
                    <input type="hidden" name="accion" value="eliminarPregunta">
                    <input type="hidden" name="userID" value="<%=questionDetails.getIdUsuario()%>">
                    <input type="hidden" name="questionID" value="<%=questionDetails.getQuestion().getID_Pregunta()%>">
                    <button type="submit" class="btn-delete"><i class="fas fa-trash-alt"></i> Eliminar</button>		                        
                </form>
            </div>
            </div>
        </div>
    </div>
    
    <div class="main-container">
       <%@include file="/Pages/Layouts/header.jsp" %>
        <div class="middle-container">
        <%if(showSuccessMessage){ %>
        	<div class="success-message">
                <span>La respuesta ha sido publicada con exito en esta pregunta!</span><div id="btnCloseMessage">X</div>
            </div>
        <%} %>
            <div class="card-question-detail">
                <div class="card-question-detail-header">
                    <div class="card-question-img">
                    <c:url var="linkProfile" value="/Perfil/Preguntas">
                    	<c:param name="userID" value="<%=questionDetails.getIdUsuario()%>"></c:param>
                    </c:url>
                        <img src="/piaweb/Imagenes/Usuario?imageID=<%=questionDetails.getIdUsuario()%>" alt="">
                        <a href="${linkProfile}"><span><%=questionDetails.getNombreUsuario() + " " + questionDetails.getApellidoPUsuario()%></span></a>
                        <%if(questionDetails.getQuestion().isActivo_editar()){ %>
                        <span class="edited-tag">Pregunta editada</span>
                        <%} %>
                    </div>
                    <div class="card-question-title">
                        <h2>¿<%=questionDetails.getQuestion().getEncabezado() %>?</h2>
                    </div>
                    <div class="card-question-date">
                        <span><i class="far fa-calendar-alt"></i> <span><%=questionDetails.getQuestion().getFechaFormat()%></span></span>
                       <span class="category"><a href="/piaweb/Categorias?categoryID=<%=questionDetails.getIdCategoria()%>"><%=questionDetails.getNombreCategoria() %></a> </span>
                    </div>
                </div>
                <div class="card-question-detail-content">
                	
                    <p><%=questionDetails.getQuestion().getDescripcion() %></p>
                    <%if(questionDetails.isImage()){ %>
	                   <h3>Imagen Descriptiva:</h3>
	                   <%	
		                  // questionDetails.getQuestion().getImagen().read(imgData);
	                 		//response.setContentType("image/gif");
	                
	                   %>
	                   <img src="/piaweb/Imagenes/Preguntas?imageID=<%=questionDetails.getQuestion().getID_Pregunta() %>" alt="">
                    <%} %>
                </div>
                <div class="card-question-detail-footer">
              		<div class="card-left-btns">
                	<%if(user != null){ %>
                		<%if(user.getID_Usuario().equals(questionDetails.getIdUsuario()) && user.isActivo()){ %>
		                    
		                        <a href="/piaweb/EditarPregunta?questionID=<%=questionDetails.getQuestion().getID_Pregunta() %>" class="btn-edit"><i class="fas fa-pencil-alt"></i> Editar</a>
                                <button type="button" class="btn-delete" data-bs-toggle="modal" data-bs-target="#confirmateDelete"><i class="fas fa-trash-alt"></i> Eliminar</button>
		                    
                		<%} %>
                	<%} %>
                	</div>
                    <div class="card-right-btns">
                    	<%if(user != null){ %>
                			<%if(user.getID_Usuario().equals(questionDetails.getIdUsuario()) && user.isActivo()){ %>
		                   
			                
			                <button><%=questionDetails.getFavs()%><i class="far fa-star"></i></button>
	                     		<button><%=questionDetails.getLikes()%><i class="far fa-thumbs-up"></i></button>
	                        	<button><%=questionDetails.getDislikes()%><i class="far fa-thumbs-down"></i></button>
			                
			                
                			<%}else if(user.isActivo()){ %>
                			
                		 <form>
			                    <button type="button" id="fav" class="<%= questionDetails.getFaved()==1 ? "targeted" : "" %>"><%=questionDetails.getFavs()%><i class="far fa-star"></i></button>
			                </form>
			                <form>
			                	<button type="button" id="likeQuestion" class="<%= questionDetails.getLiked()==1 ? "targeted" : "" %>"><%=questionDetails.getLikes()%><i class="far fa-thumbs-up"></i></button>
			                	</form>
			                <form>
	                        	<button type="button" id="dislikeQuestion" class="<%= questionDetails.getDisliked()==1 ? "targeted" : "" %>"><i class="far fa-thumbs-down"></i></button>
	                        	</form>
                			<%}else{ %>
                			<button><%=questionDetails.getFavs()%><i class="far fa-star"></i></button>
	                     		<button><%=questionDetails.getLikes()%><i class="far fa-thumbs-up"></i></button>
	                        	<button><i class="far fa-thumbs-down"></i></button>
                		<%}} else { %>
                				<button><%=questionDetails.getFavs()%><i class="far fa-star"></i></button>
	                     		<button><%=questionDetails.getLikes()%><i class="far fa-thumbs-up"></i></button>
	                        	<button><i class="far fa-thumbs-down"></i></button>
                		<%} %>
                        
                    </div>
                </div>
            </div>
             <%if(numberOfPages != 0) {%>
            <h2><%=totalOfAnswers %> respuestas</h2>
            <hr>
            <!-- Mostrar respuesta correcta -->
            <%if(correctAnswer != null) {%>
               <div class="card-answer correct-answer">
                <div class="card-answer-header">
                    <div class="card-answer-img">
                        <img src="/piaweb/Imagenes/Usuario?imageID=<%=correctAnswer.getAnswer().getID_Usuario() %>" alt="">
                        <a href="/piaweb/Perfil/Preguntas?userID=<%=correctAnswer.getAnswer().getID_Usuario()%>"><span><%=correctAnswer.getNombreUsuario()%></span></a>
                    </div>
                    <div class="card-answer-date">
                        <span><i class="far fa-calendar-alt"></i> <span><%=correctAnswer.getAnswer().getFechaFormat() %></span> </span>

                    </div>
                    <span class="tag-correct"><i class="fas fa-check-circle"></i> Correcta</span>
                    <%if(correctAnswer.getAnswer().isActivo_editar()){ %>
                    <span class="tag-edited"><i class="fas fa-user-edit"></i> Editada</span>
                    <%} %>
                </div>
                <div class="card-answer-content">
                    <p><%=correctAnswer.getAnswer().getRespuesta() %></p>
                    <%if(correctAnswer.getAnswer().getImagen() != null && correctAnswer.getAnswer().getImagen().available() != 0){ %>
                    <h3>Imagen Descriptiva</h3>
                    <img src="/piaweb/Imagenes/Respuestas?imageID=<%=correctAnswer.getAnswer().getID_Respuesta() %>" alt="Imagen descriptiva de la respuesta">
                    <%} %>
                </div>
                <div class="card-answer-footer">
                    <div class="card-left-btns">
                    <%if(user!=null){ %>
                    	<%if(user.getID_Usuario().equals(questionDetails.getIdUsuario())){ %>
                    	<form action="/piaweb/DetallesPregunta" method="POST">
                    		<input type="hidden" name="accion" value="desmarcarCorrecta">
                    		<input type="hidden" name="answerID" value="<%=correctAnswer.getAnswer().getID_Respuesta() %>">
                   			<input type="hidden" name="questionID" value="<%=questionDetails.getQuestion().getID_Pregunta()%>">
	                        <button type="submit" class="btn-correct-answer btn-remove-correct-answer"><i class="fas fa-times-circle"></i> Quitar como respuesta correcta</button>                    	
                    	</form>
                        <%} %>
                        
                        <%if(user.getID_Usuario().equals(correctAnswer.getAnswer().getID_Usuario())){ %>
                        	<a href="/piaweb/EditarRespuesta?answerID=<%=correctAnswer.getAnswer().getID_Respuesta()%>" class="btn-edit"><i class="fas fa-pencil-alt"></i> Editar</a>
	                        <form action="/piaweb/DetallesPregunta" method="POST">
	                        	<input type="hidden" name="accion" value="eliminarRespuesta">
	                        	<input type="hidden" name="questionID" value="<%=questionDetails.getQuestion().getID_Pregunta()%>">
	                    		<input type="hidden" name="answerID" value="<%=correctAnswer.getAnswer().getID_Respuesta() %>">
		                        <button type="submit" class="btn-delete"><i class="fas fa-trash-alt"></i> Eliminar</a>
                        	</form>
                        <%} %>
                    <%} %>
                    </div>
                    <div class="card-right-btns">
                       <%if(user!=null){ %>
                        <%if(user.getID_Usuario().equals(correctAnswer.getAnswer().getID_Usuario()) && user.isActivo()){ %>
                        <button><%=correctAnswer.getLikes() %> <i class="far fa-thumbs-up"></i></button>                        
                        <button><%=correctAnswer.getDislikes() %> <i class="far fa-thumbs-down"></i></button>
                        <%}else if(user.isActivo()){ %>
                         <form>
                         <button class="<%= correctAnswer.getLiked()==1 ? "answerButtonLike targeted" : "answerButtonLike"  %>"  type="button" id="<%=correctAnswer.getAnswer().getID_Respuesta()%>"><%=correctAnswer.getLikes() %> <i class="far fa-thumbs-up"></i></button>
                    	</form>
                        <form>
                        <button class="<%= correctAnswer.getDisliked()==1 ? "answerButtonDislike targeted" : "answerButtonDislike"%>" type="button" id="<%=correctAnswer.getAnswer().getID_Respuesta()%>" ><i class="far fa-thumbs-down"></i></button>
                        </form>
                        
                        <%}else{ %>
                        
                       		<button><%=correctAnswer.getLikes() %> <i class="far fa-thumbs-up"></i></button>
                          <button><i class="far fa-thumbs-down"></i></button> 
                        <%} %>
                        
                        <%}else{ %>
                          <button><%=correctAnswer.getLikes() %> <i class="far fa-thumbs-up"></i></button>
                          <button><i class="far fa-thumbs-down"></i></button> 
                        <%} %>
                    </div>
                </div>
            </div>
            <%if(answers.size() != 0){ %>
            <h2>Otras Respuestas</h2>
            <hr>
            <%} %>
            <%} %>
            <%for(AnswerCardViewModel answerItem : answers){ %>
            <%if(answerItem.getAnswer().isActivo_eliminar()){ %>
            <div class="card-answer">
                <div class="card-answer-header">
                    <div class="card-answer-img">
                        <img src="/piaweb/Imagenes/Usuario?imageID=<%=answerItem.getAnswer().getID_Usuario() %>" alt="">
                        <a href="/piaweb/Perfil/Preguntas?userID=<%=answerItem.getAnswer().getID_Usuario()%>"><span><%=answerItem.getNombreUsuario()%></span></a>
                    </div>
                    <div class="card-answer-date">
                        <span><i class="far fa-calendar-alt"></i> <span><%=answerItem.getAnswer().getFechaFormat() %></span> </span>
						
                    </div>
                     <%if(answerItem.getAnswer().isActivo_editar()){ %>
                    <span class="tag-edited"><i class="fas fa-user-edit"></i> Editada</span>
                    <%} %>
                    <!-- <span class="tag-correct"><i class="fas fa-check-circle"></i> Correcta</span> -->
                </div>
                <div class="card-answer-content">
                    <p><%=answerItem.getAnswer().getRespuesta() %></p>
                    <%if(answerItem.getAnswer().getImagen() != null && answerItem.getAnswer().getImagen().available() != 0){ %>
                    <h3>Imagen Descriptiva</h3>
                    <img src="/piaweb/Imagenes/Respuestas?imageID=<%=answerItem.getAnswer().getID_Respuesta() %>" alt="Imagen descriptiva de la respuesta">
                    <%} %>
                </div>
                <div class="card-answer-footer">
                    <div class="card-left-btns">
                    <%if(user!=null){ %>
                    	<%if(user.getID_Usuario().equals(questionDetails.getIdUsuario()) && user.isActivo()){ %>
                    	<form action="/piaweb/DetallesPregunta" method="POST">
                    		<input type="hidden" name="accion" value="marcarCorrecta">
                    		<input type="hidden" name="answerID" value="<%=answerItem.getAnswer().getID_Respuesta() %>">
                   			<input type="hidden" name="questionID" value="<%=questionDetails.getQuestion().getID_Pregunta()%>">
	                        <button type="submit" class="btn-correct-answer"><i class="fas fa-check-circle"></i> Marcar como correcta</button>                    	
                    	</form>
                        <%} %>
                        
                        <%if(user.getID_Usuario().equals(answerItem.getAnswer().getID_Usuario())  && user.isActivo()){ %>
                        	<a href="/piaweb/EditarRespuesta?answerID=<%=answerItem.getAnswer().getID_Respuesta()%>" class="btn-edit"><i class="fas fa-pencil-alt"></i> Editar</a>
                        	<button type="button" class="btn-delete" data-bs-toggle="modal" data-bs-target="#confirmateDeleteAnswer<%=answerItem.getAnswer().getID_Respuesta()%>"><i class="fas fa-trash-alt"></i> Eliminar</button>
                        	<div class="modal fade" id="confirmateDeleteAnswer<%=answerItem.getAnswer().getID_Respuesta()%>" tabindex="-1" aria-labelledby="confirmateDeleteAnswerLabel" aria-hidden="true">
						        <div class="modal-dialog modal-dialog-centered">
						            <div class="modal-content">
						            <div class="modal-header">
						                <h5 class="modal-title" id="confirmateDeleteAnswerLabel">¿Estas seguro que quieres eliminar esta respuesta?</h5>
						            </div>
						            <div class="modal-body">
						                Si decides eliminar esta respuesta no podras volver a recuperarla
						            </div>
						            <div class="modal-footer">
						                <button type="button" class="btn-cancel" data-bs-dismiss="modal">Cancelar</button>
						                <form action="/piaweb/DetallesPregunta" method="POST">
				                        	<input type="hidden" name="accion" value="eliminarRespuesta">
				                        	<input type="hidden" name="questionID" value="<%=questionDetails.getQuestion().getID_Pregunta()%>">
				                    		<input type="hidden" name="answerID" value="<%=answerItem.getAnswer().getID_Respuesta() %>">
					                        <button type="submit" class="btn-delete"><i class="fas fa-trash-alt"></i> Eliminar</a>
						               	</form>
						            </div>
						            </div>
						        </div>
    					</div>
                        <%} %>
                    <%} %>
                    </div>
                    <div class="card-right-btns">
                        <%if(user!=null){ %>
                        <%if(user.getID_Usuario().equals(answerItem.getAnswer().getID_Usuario())  && user.isActivo()){ %>
                        <button><%=answerItem.getLikes() %> <i class="far fa-thumbs-up"></i></button>
                        <button><%=answerItem.getDislikes() %> <i class="far fa-thumbs-down"></i></button>
                        <%}else if(user.isActivo()){ %>
                          <form>
                         <button class="<%= answerItem.getLiked()==1 ? "answerButtonLike targeted" :  "answerButtonLike" %>" type="button" id="<%=answerItem.getAnswer().getID_Respuesta()%>"><%=answerItem.getLikes() %> <i class="far fa-thumbs-up"></i></button>
                    	</form>
                        <form>
                        <button class="<%= answerItem.getDisliked()==1 ? "answerButtonDislike targeted" : "answerButtonDislike"%>" type="button" id="<%=answerItem.getAnswer().getID_Respuesta()%>" type="button" id="<%=answerItem.getAnswer().getID_Respuesta()%>" ><i class="far fa-thumbs-down"></i></button>
                        </form>
                        <%}else{ %>
                        
                          <button><%=answerItem.getLikes() %> <i class="far fa-thumbs-up"></i></button>
                       
                          <button><i class="far fa-thumbs-down"></i></button> 
                        
                        <%} %>
                        <%}else{ %>
                           <button><%=answerItem.getLikes() %> <i class="far fa-thumbs-up"></i></button>
                        
                          <button><i class="far fa-thumbs-down"></i></button> 
                        <%} %>
                    </div>
                </div>
            </div>
            <%} else { %>
            <div class="card-answer deleted-answer">
                <div class="card-answer-header">
                    <div class="card-answer-img">
                        <img src="/piaweb/Imagenes/Usuario?imageID=<%=answerItem.getAnswer().getID_Usuario() %>" alt="">
                        <a href="/piaweb/Perfil/Preguntas?userID=<%=answerItem.getAnswer().getID_Usuario()%>"><span><%=answerItem.getNombreUsuario() %></span></a>
                    </div>
                    <div class="card-answer-date">
                        <span><i class="far fa-calendar-alt"></i> <span><%=answerItem.getAnswer().getFechaFormat() %></span></span>
                    </div>
                    <!-- <span class="tag-correct"><i class="fas fa-check-circle"></i> Correcta</span> -->
                </div>
                <div class="card-answer-content">
                    <p>Esta respuesta ha sido eliminada.</p>
                </div>
            </div>
            
            
            <%} %>
            
            <%} %>
           
            <div class="page-controls">
            	<%if(pageNumber != 1){ %>
	                <a href="/piaweb/DetallesPregunta?questionID=<%=questionDetails.getQuestion().getID_Pregunta()%>&pageNumber=<%=pageNumber - 1 %>" class="btn-change-page">&lt</a>            	
            	<%} %>
            	<%if(pageNumber != numberOfPages){ %>            	
	                <a href="/piaweb/DetallesPregunta?questionID=<%=questionDetails.getQuestion().getID_Pregunta()%>&pageNumber=<%=pageNumber + 1 %>" class="btn-change-page btn-next-page">&gt</a>
            	<%} %>
            </div>
         	<%} else { %>
         		<h2>Esta pregunta todavia no cuenta con respuestas</h2>
         		<hr>
         	<%} %>
            <div class="answer-box" id="answerBox">
	            <%if(user != null) {%>
	            	<%if(!user.getID_Usuario().equals(questionDetails.getIdUsuario()) && user.isActivo()){ %>
	      		 <form class="answer" action="/piaweb/DetallesPregunta" method="POST" enctype="multipart/form-data">
	      		 	<input type="hidden" name="accion" value="responder">
	      		 	<input type="hidden" name="questionID" value="<%=questionDetails.getQuestion().getID_Pregunta()%>">
	                <h2>Responder a la pregunta</h2>
	                <div class="textarea-box">
	                    <textarea <%=(errors != null ? "class='error-field'" : "") %> placeholder="<%=(errors != null ? errors.get("respuesta") : "Escribe aqui tu respuesta...") %>" name="respuesta" ></textarea>
	                    <div>
	                        <input type="file" id="image" name="image" value="Adjuntar imagen" accept="image/*">
	                        <label for="image" id="selectImage"><i class="fas fa-upload"></i> Seleccionar imagen</label>
	                        <span id="fileDetails"></span>
	                    </div>
	                </div>
	                <div class="align-right">
	                    <button type="submit">Publicar respuesta</button>
	                </div>
	            </form>
	            	<%}else if(!user.isActivo()){  %>
	              	<div class="offline-answer">
		          	  <h2>Esta cuenta ha sido bloqueada no puede responder</h2>              
	            	</div>
	            	<%} %>
	            <%}else{ %> 
	            <div class="offline-answer">
		            <h2>¿Quieres responder a esta pregunta?</h2>
		            <p><a href="/piaweb/Acceso/Login?returnURL=piaweb/DetallesPregunta?questionID=<%=questionDetails.getQuestion().getID_Pregunta()%>">Inicia sesión</a> para poder responder</p>            
	            </div>
	            <%} %>
            </div>
            
        </div>
   	 	<%@ include file="/Pages/Layouts/footer.jsp" %>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta1/dist/js/bootstrap.bundle.min.js" integrity="sha384-ygbV9kiqUc6oa4msXn9868pTtWMgiQaeYH7/t7LECLbyPA2x65Kgf80OJFdroafW" crossorigin="anonymous"></script>
    <script src="http://code.jquery.com/jquery-latest.js"> </script>
    <script>

        $(document).ready(function() {
        	<%if(user!=null){%>	
        	
	$('.answerButtonDislike').click(function(){
        		
        		var idRespuesta = this.id;
        		var usuario='<%=user.getID_Usuario()%>';
        		var condition='Answer';
        		var tipo='Dislike';
        		
        		//poner lo que haga falta para coordinar colores conn clicks
        		  $('#'+idRespuesta+'.answerButtonLike').removeClass('targeted');
                  if ($(this).hasClass('targeted')) {
                      $(this).removeClass('targeted')

                  } else {
                      $(this).addClass('targeted')
                  }
        		
        		
        		$.ajax({
        			url: 'Interaccion',
        			type: 'POST',
        			data:{
        			id:idRespuesta,
        			userID:usuario,
        			condition:condition,
        			tipo:tipo
        			},
        			success:function(likes){
        				$('#'+idRespuesta+'').html(likes);
        		
        			}
        		});
        	});
        	
        	
        	
        	$('.answerButtonLike').click(function(){
        		
        		var idRespuesta = this.id;
        		var usuario='<%=user.getID_Usuario()%>';
        		var condition='Answer';
        		var tipo='Like';
        		
        		$('#'+idRespuesta+'.answerButtonDislike').removeClass('targeted');
                if ($(this).hasClass('targeted')) {
                    $(this).removeClass('targeted')

                } else {
                    $(this).addClass('targeted')
                }
        		
        		$.ajax({
        			url: 'Interaccion',
        			type: 'POST',
        			data:{
        			id:idRespuesta,
        			userID:usuario,
        			condition:condition,
        			tipo:tipo
        			},
        			success:function(likes){
        				$('#'+idRespuesta+'').html(likes);
        		
        			}
        		});
        	});
        	
        	
        	$('#likeQuestion').click(function(){
        		var idPregunta = <%=questionDetails.getQuestion().getID_Pregunta()%>;
        		var usuario='<%=user.getID_Usuario()%>';
        		var action="LikeQuestion";
        		var condition='Question';
        		var tipo='Like';
        		
        		$('#dislikeQuestion').removeClass('targeted');
                if ($(this).hasClass('targeted')) {
                    $(this).removeClass('targeted')

                } else {
                    $(this).addClass('targeted')
                }
        		
        		
        		$.ajax({
        			url: 'Interaccion',
        			type: 'POST',
        			data:{
        			id:idPregunta,
        			userID:usuario,
        			action:action,
        			condition:condition,
        			tipo:tipo
        			},
        			success:function(likes){
        				$('#likeQuestion').html(likes);
	
        			}
        		});
        	});
        	
        	$('#dislikeQuestion').click(function(){
        		var idPregunta = <%=questionDetails.getQuestion().getID_Pregunta()%>;
        		var usuario='<%=user.getID_Usuario()%>';
        		var condition='Question';
        		var tipo='Dislike';
        		var action="LikeQuestion";
        		
        		
        		$('#likeQuestion').removeClass('targeted');
                if ($(this).hasClass('targeted')) {
                    $(this).removeClass('targeted')

                } else {
                    $(this).addClass('targeted')
                }
        		
        		$.ajax({
        			url: 'Interaccion',
        			type: 'POST',
        			data:{
        			id:idPregunta,
        			userID:usuario,
        			condition:condition,
        			tipo:tipo,
        			action:action
        			},
        			success:function(likes){
        				$('#likeQuestion').html(likes);
        		
        				
        			}
        		});
        	});
        	
        	$('#fav').click(function(){
        		var idPregunta = <%=questionDetails.getQuestion().getID_Pregunta()%>;
        		var usuario='<%=user.getID_Usuario()%>';
        		var action='favQuestion';
        		var condition='Question';
        	
        		 if ($(this).hasClass('targeted')) {
                     $(this).removeClass('targeted')

                 } else {
                     $(this).addClass('targeted')
                 }
        		
        		$.ajax({
        			url : 'Interaccion',
        			type: 'POST',
        			data:{
        			id:idPregunta,
        			userID:usuario,
        			action:action,
        			condition:condition
        			},
        			success:function(favs){
        				$('#fav').html(favs);
        			}});
        		
        		
        	});
        	
        <% }%>
        	//doomi
        	
        	
        	
            $(window).scroll(function() {
                menu_fixed();
            });
            menu_fixed();

            $('#toTop').click(function() {

                $("html, body").scrollTop(0);
                // $('html, body').stop().animate({scrollTop:0}, 2000);
            });

            $('#btnCloseMessage').click(function() {
				$(this).parent().hide();
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
        })
    </script>
    
  
</body>
</html>