<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.piaweb.models.*, java.util.*, com.piaweb.viewmodels.*" %>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%
	User user = (User)session.getAttribute("user");
	User userProfile = (User)request.getAttribute("userInfo");
	boolean showSuccessMessage = (boolean)request.getAttribute("showSuccessMessage");
	boolean showDBError = (boolean)request.getAttribute("showDBError");
	Map<String, String> errors = (HashMap<String, String>)request.getAttribute("errors");
	QuestionDetailsViewModel questionToEdit = (QuestionDetailsViewModel)request.getAttribute("questionToEdit");
	
%>
<!DOCTYPE html>
<html>
<head>
	<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-giJF6kkoqNQ00vy+HMDP7azOuL0xtbfIcaT9wjKHr8RbDVddVHyTfAAsrekwKmP1" crossorigin="anonymous">
  	 <%@ include file="/Pages/Layouts/head.jsp" %>	
    <link rel="stylesheet" href="/piaweb/Stylesheets/question-detail-styles.css">
    <link rel="stylesheet" href="/piaweb/Stylesheets/question-creation-styles.css">
    <link rel="stylesheet" href="/piaweb/Stylesheets/question-edition-styles.css">
    <title>Questionary</title>
</head>
<body>
    <div class="main-container">
         <%@include file="/Pages/Layouts/header.jsp" %>
        <div class="middle-container">
        <%if(showSuccessMessage){ %>
        	<div class="success-message">
                <span>La pregunta ha sido editada con exito! <a href="/piaweb/DetallesPregunta?questionID=<%=questionToEdit.getQuestion().getID_Pregunta()%>">CLICK AQUI</a> para volver a la pagina de detalles</span><div id="btnCloseMessage">X</div>
            </div>
        <%} %>
        <%if(showDBError){ %>
        	<div class="error-message">
                <span>Ha ocurrido un error! Ingrese menos de 50 caracteres en su pregunta y menos de 255 en la descripcion</span><div class="btnCloseMessage">X</div>
            </div>
        <%} %>
            <form class="answer" method="post" action="EditarPregunta" enctype="multipart/form-data" runat="server">
                <h2>Editar pregunta</h2>
                <div class="input-box-question">
                    <div class="question-mark">¿</div>
                    <input type="text" name="encabezado" value="<%=questionToEdit.getQuestion().getEncabezado()%>" <%=(errors != null ?  "class='error-field'" : "") %> placeholder="<%=(errors != null ? errors.get("encabezado") : "Escribe tu pregunta") %>">
                    <div class="question-mark">?</div>
                </div>
                <!-- <input type="text" placeholder="Categoria"> -->
                <label for="category">Seleccione la categoria: </label>
                <select name="categoria" id="category">
                    <%for(Category categoryItem : categories){ %>
                    	<option value="<%=categoryItem.getID_Categoria()%>" <%=(categoryItem.getID_Categoria() == questionToEdit.getIdCategoria() ? "selected" : "") %>><%=categoryItem.getNombre() %></option>
                    <%} %>
                </select>
                <div class="textarea-box">
                    <textarea name="descripcion" <%=(errors != null ?"class='error-field'" : "") %> placeholder="<%=(errors != null ? errors.get("descripcion") : "Escribe aqui la descripcion de tu pregunta(Opcional)...") %>"><%=questionToEdit.getQuestion().getDescripcion() %></textarea>
                    <div>
                        <input type="file" name="imagen" id="image" value="Adjuntar imagen" accept="image/*">
                        <label for="image" id="selectImage"><i class="fas fa-upload"></i> Seleccionar imagen</label>
                        <span id="fileDetails"></span>
                    </div>
                    <div class="img-container">
                    	<h2>Imagen descriptiva: </h2>
                    	<button type="button" id="quitarImagen">Quitar Imagen</button>                	
                   		<img id="imagenDescriptiva" src="<%=(questionToEdit.isImage() ? "/piaweb/Imagenes/Preguntas?imageID="+questionToEdit.getQuestion().getID_Pregunta(): "") %>" alt="no hay imagen">
                    </div>
                </div>
                <div class="align-right">
                    <button type="submit"><i class="fas fa-pencil-alt"></i> Editar pregunta</button>
                    <a href="/piaweb/DetallesPregunta?questionID=<%=questionToEdit.getQuestion().getID_Pregunta() %>" class="btn-cancel">Volver a la pregunta</a>
                </div>
                <input type="hidden" name="questionID" value="<%=questionToEdit.getQuestion().getID_Pregunta()%>">
                <input type="hidden" name="userID" value="<%=questionToEdit.getIdUsuario()%>">
            </form>
        </div>
      	<%@ include file="/Pages/Layouts/footer.jsp" %>
    </div>
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


            $('#image').change(function() {
                setFileName();
            });
            setFileName();

			$('#btnCloseMessage').click(function() {
				$(this).parent().hide();
			});
            
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
    <script>
    	$(document).ready(function(){
    		$('#image').value = '';
    		function readImage(input){
    			if(input.files && input.files[0]){
    				var reader = new FileReader();
    				
    				reader.onload = function(e) {
    					$('#imagenDescriptiva').attr('src', e.target.result);
    				}
    				
    				reader.readAsDataURL(input.files[0]);
    			}
    		}
    		
    		
    		$('#image').change(function(){
    			readImage(this);
    			//Add hidden parameter in the form to indicate that the image was edited
    			$('#quitarImagenParam').remove()
    		});
    		
    		$('#quitarImagen').click(function(){
    			$('#image').value = '';
    			$('#imagenDescriptiva').attr('src', '');
    			if($('form.answer').children('#quitarImagenParam').length == 0){
    				$('form.answer').append('<input type="hidden" id="quitarImagenParam" name="quitarImagen" value="true">');
    			} 
    		});
    		
    		
    	})
    </script>
    <script>
    	$(document).ready(function(){
    		$('.btnCloseMessage').click(function() {
				$(this).parent().hide();
			});
    	});
    </script>
</body>
</html>