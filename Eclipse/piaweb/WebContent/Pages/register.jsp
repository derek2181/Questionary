<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.piaweb.models.*,com.piaweb.viewmodels.QuestionDetailsViewModel,java.util.*,java.sql.Blob,java.text.DateFormat, java.text.SimpleDateFormat" %>
        <%
        boolean showErrors=false;
   		User usuario =(User)request.getAttribute("usuarioRegister");
   		
   		
   		
       	String inputBoxClass="input-box input-box-error";
   		Map<String,String> errores =(Map<String,String>)request.getAttribute("errores");
   		if(errores!=null)
   			showErrors=true;
   		String iconoError="<i class='fas fa-exclamation-circle'></i>";	
   		
   		Date date=null;
   		String fecha_nacimiento=(String)request.getAttribute("fecha_nacimiento");
   		
   		
   		
   %>
            <!DOCTYPE html>
            <html>

            <head>
                <meta charset="UTF-8">
                <meta http-equiv="X-UA-Compatible" content="IE=edge">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <link rel="stylesheet" href="/piaweb/Stylesheets/login-styles.css">
                <link rel="stylesheet" href="/piaweb/Stylesheets/register-styles.css">
                <%-- <link rel="stylesheet" href="../Stylesheets/login-styles.css"> --%>
                <%-- <link rel="stylesheet" href="../Stylesheets/register-styles.css"> --%>
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
                            <div class="logo-message">
                                <p>Somos el mejor sitio de Q&A, Unete Ahora! para comenzar a descubrir</p>
                                <p class="join-question">¿Ya tienes una cuenta con nosotros?</p>
                                <div>
                                    <a href="/piaweb/Acceso/Login">Inicia Sesión</a>
                                </div>
                            </div>
                        </div>
                    </section>
                    <section class="form-login">
                        <div class="vertical-center">
                            <h2>Crear una nueva cuenta</h2>
                            <form method="POST" action="/piaweb/Acceso">
                                <input type="hidden" name="accion" value="Registro">
                                <div class="col-form">
                                    <div class="<%= (showErrors==false ? "input-box" :  (errores.get("nombre")==null ? "input-box" : inputBoxClass )) %>">
                                        <!-- Iconos -->
                                        <i class="fas fa-user-tag"></i>
                                        <input type="text" value="<%= showErrors==false ? "" :  (errores.get("nombre")==null ? usuario.getNombre() : "")%>" name="nombre" placeholder="Nombre(s)">

                                    </div>
                                    <div class="spans">

                                        <span><%= (showErrors==false ? "" :  (errores.get("nombre")==null ? "" : iconoError+errores.get("nombre"))) %> </span>
                                        <!-- <span><i class='fas fa-exclamation-circle'></i>Error</span> -->
                                    </div>

                                    <div class="<%= (showErrors==false ? "input-box" :  (errores.get("apellidoP")==null ? "input-box" : inputBoxClass )) %>">
                                        <!-- Iconos -->
                                        <i class="fas fa-user-tag"></i>
                                        <input type="text" value="<%=showErrors==false ? "" : (errores.get("apellidoP")==null ? usuario.getApellidoP() : "")%>" name="apellidoP" placeholder="Apellido Paterno">
                                    </div>
                                    <div class="spans">
                                        <span><%= (showErrors==false ? "" :  (errores.get("apellidoP")==null ? "" : iconoError+errores.get("apellidoP"))) %> </span>

                                    </div>
                                    <div class="<%= (showErrors==false ? "input-box" :  (errores.get("apellidoM")==null ? "input-box" : inputBoxClass )) %>">
                                        <!-- Iconos -->
                                        <i class="fas fa-user-tag"></i>
                                        <input type="text" value="<%= showErrors==false ? "" : (errores.get("apellidoM")==null ? usuario.getApellidoM() : "")%>" name="apellidoM" placeholder="Apellido Materno">
                                    </div>
                                    <div class="spans">
                                        <span><%= (showErrors==false ? "" :  (errores.get("apellidoM")==null ? "" : iconoError+errores.get("apellidoM"))) %> </span>

                                    </div>
                                    <div class="<%= (showErrors==false ? "input-box" :  (errores.get("ID_Usuario")==null ? "input-box" : inputBoxClass )) %>">
                                        <i class="fas fa-id-card"></i>
                                        <input type="text" value="<%= showErrors==false ? "" : (errores.get("ID_Usuario")==null ? usuario.getID_Usuario() : "")%>" name="nombreUsuario" placeholder="Nombre de usuario">
                                    </div>
                                    <div class="spans">
                                        <span><%= (showErrors==false ? "" :  (errores.get("ID_Usuario")==null ? "" : iconoError+errores.get("ID_Usuario"))) %> </span>
                                    </div>
                                </div>
                                <div class="col-form">
                                    <div class="<%= (showErrors==false ? "input-box" :  (errores.get("correo")==null ? "input-box" : inputBoxClass )) %>">
                                        <i class="fas fa-envelope"></i>
                                        <input type="email" value="<%=showErrors==false ? "" : (errores.get("correo")==null ? usuario.getCorreo() : "")%>" name="email" placeholder="Email">
                                    </div>
                                    <div class="spans">
                                        <span><%= (showErrors==false ? "" :  (errores.get("correo")==null ? "" : iconoError+errores.get("correo"))) %> </span>

                                    </div>
                                    <div class="<%= (showErrors==false ? "input-box" :  (errores.get("fecha_nacimiento")==null ? "input-box" : inputBoxClass )) %>">
                                        <i class="fas fa-calendar-alt"></i>
                                        <input type="date" value="<%=showErrors==false ? "" : (errores.get("fecha_nacimiento")==null ? fecha_nacimiento : "")%>" name="fecha_nacimiento">
                                    </div>
                                    <div class="spans">
                                        <span><%= (showErrors==false ? "" :  (errores.get("fecha_nacimiento")==null ? "" : iconoError+errores.get("fecha_nacimiento"))) %> </span>
                                    </div>
                                    <div class="<%= (showErrors==false ? "input-box" :  (errores.get("contra")==null ? "input-box" : inputBoxClass )) %>">
                                        <i class="fas fa-lock"></i>
                                        <input type="password" value="<%= showErrors==false ? "" : (errores.get("contra")==null ? usuario.getContra() : "")%>" name="contrasena" placeholder="Contraseña">
                                    </div>
                                    <div class="spans">
                                        <span><%= (showErrors==false ? "" :  (errores.get("contra")==null ? "" : iconoError+errores.get("contra"))) %> </span>
                                    </div>
                                    <div class="<%= (showErrors==false ? "input-box" :  (errores.get("contra")==null ? "input-box" : inputBoxClass )) %>">
                                        <i class="fas fa-lock"></i>
                                        <input type="password"  value="<%=showErrors==false ? "" :  (errores.get("contra")==null ? usuario.getConfirmarContrasena() : "")%>"  name="contrasena2" placeholder="Confirmar contraseña">
                                    </div>
                                    <div class="spans">
                                        <span><%= (showErrors==false ? "" :  (errores.get("contra")==null ? "" : iconoError+errores.get("contra"))) %> </span>

                                    </div>
                                </div>
                                <div class="align-center">
                                    <button>Registrarse</button>
                                </div>

                            </form>
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