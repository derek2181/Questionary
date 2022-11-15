<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"  %>

 <%@ page import="com.piaweb.models.*,java.util.*" %>
 <%
 	String mensaje = (String)request.getAttribute("mensaje");
	String mensajeExcepcion = (String)request.getAttribute("mensajeError");	
 %>
  
    <!DOCTYPE html>
    <html>

    <head>
        <meta charset="UTF-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="/piaweb/Stylesheets/login-styles.css">
        <link rel="stylesheet" href="/piaweb/Stylesheets/error-generic-styles.css">
        <link rel="preconnect" href="https://fonts.gstatic.com">
        <link href="https://fonts.googleapis.com/css2?family=Roboto:ital,wght@0,100;0,300;0,400;0,500;0,700;0,900;1,100;1,300;1,400;1,500;1,700;1,900&display=swap" rel="stylesheet">
        <link rel="preconnect" href="https://fonts.gstatic.com">
        <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@900&display=swap" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.2/css/all.min.css" integrity="sha512-HK5fgLBL+xu6dm/Ii3z4xhlSUyZgTT9tuc/hSrtw6uzJOvgRr2a9jyxxT1ely+B+xFAmJKVSTbpM/CuL7qxO8w==" crossorigin="anonymous" />
        <script src="https://code.jquery.com/jquery-3.5.1.slim.js" integrity="sha256-DrT5NfxfbHvMHux31Lkhxg42LY6of8TaYyK50jnxRnM=" crossorigin="anonymous"></script>
        <title>Questionary</title>
    </head>

    <body>
        <main>
            <section class="logo-section">
                <div class="vertical-center">
                    <div class="logo">
                        <img src="/piaweb/Images/Logo.png" alt="logo">
                        <h2><a href="/piaweb/">Questionary</a></h2>
                    </div>

                </div>
            </section>
            <section class="form-login">
                <div class="vertical-center">
                    <h2>Ha ocurrido un error, Codigo de error: <%=mensaje%></h2>
                    <img src="/piaweb/Images/error-image.png" alt="imagen de error">
                    <p><%=mensajeExcepcion %> <a href="/piaweb/">Click aqui</a> para volver a la pagina principal</p>
                </div>
            </section>
        </main>
        <script>
            $(document).ready(function() {
                $('input').focus(function() {
                    $(this).parent('div').css('border', '1px solid #13263a');
                }).blur(function() {
                    $(this).parent('div').css('border', '1px solid #d4d4d4');
                })
            })
        </script>
    </body>

    </html>