<%if(user!=null && userProfile.getID_Usuario().equals(user.getID_Usuario())){ %>
   <div class="<%=(showErrors==false ? "modal-bg" : modal)%>">
        <div class="modal-box">
            <div class="texto-modal">
                <h1>Editar perfil</h1>
                <button class="close-modal">X</button>

            </div>

            <form action="/piaweb/Perfil/" method="POST" enctype="multipart/form-data">
                <input type="hidden" name="accion" value="Editar">
				<input type="hidden" name="userID">
				
                <div class="imagen-form">
                    <img src="/piaweb/Imagenes/Usuario?imageID=<%=user.getID_Usuario()%>" alt="">
                    <input type="file" id="image" value="Adjuntar imagen" name="imagen" id="">
                    <label for="image" id="selectImage"><i class="fas fa-upload"></i> Editar imagen</label>
                    <span id="fileDetails"></span>
                </div>

                <div class="form-modal">
                    <section>
                        <div class="input-box">
                            <i class="fas fa-user"></i>
                            <input type="text" name="nombre" placeholder="Nombre" value="<%= user.getNombre()%>">
                        </div>
                        <div class="spans">
                            <span><%= (showErrors==false ? "" :  (errores.get("nombre")==null ? "" : iconoError+errores.get("nombre")))%></span>
                        </div>
                    </section>
                    <section>
                        <div class="input-box">
                            <i class="fas fa-user-friends"></i>
                            <input type="text" name="apellidoP" placeholder="Apellido paterno" value="<%=user.getApellidoP()%>">
                        </div>
                        <div class="spans">
                                        <span><%= (showErrors==false ? "" :  (errores.get("apellidoP")==null ? "" : iconoError+errores.get("apellidoP"))) %> </span>
                        </div>
                    </section>

                    <section>
                        <div class="input-box">
                            <i class="fas fa-user-friends"></i>
                            <input type="text" name="apellidoM" placeholder="Apellido materno" value="<%=user.getApellidoM() %>">
                        </div>
                        <div class="spans">
                                        <span><%= (showErrors==false ? "" :  (errores.get("apellidoM")==null ? "" : iconoError+errores.get("apellidoM"))) %> </span>
                        </div>
                    </section>

                    <section>
                        <div class="input-box">
                            <i class="fas fa-envelope"></i>
                            <input type="correo" name="correo" placeholder="Correo" value="<%=user.getCorreo()  %>">
                        </div>
                        <div class="spans">
                                        <span><%= (showErrors==false ? "" :  (errores.get("correo")==null ? "" : iconoError+errores.get("correo"))) %> </span>
                        </div>
                    </section>
                    <section>
                        <div class="input-box">
                            <i class="fas fa-key"></i>
                            <input type="password" name="contrasena1" placeholder="Contraseña nueva">
                        </div>
                        <div class="spans">
                                        <span><%= (showErrors==false ? "" :  (errores.get("contra")==null ? "" : iconoError+errores.get("contra"))) %> </span>
                        </div>
                    </section>

                    <section>
                        <div class="input-box">
                            <i class="fas fa-key"></i>
                            <input type="password" name="contrasena2" placeholder="Contraseña antigua">
                        </div>
                        <div class="spans">
                        <span><%= (showErrors==false ? "" :  (errores.get("ErrorContra")==null ? "" : iconoError+errores.get("ErrorContra"))) %> </span>

                        </div>
                    </section>


                </div>
                <div class="date-block">
                    <div class="date-form">
                        <i class="fas fa-calendar-alt"></i>
                        <input type="date" name="fecha"  value="<%=fecha_nacimiento%>">
                    </div>
                    <div class="spans">
                      <span><%= (showErrors==false ? "" :  (errores.get("fecha_nacimiento")==null ? "" : iconoError+errores.get("fecha_nacimiento"))) %> </span>
                    </div>
                </div>
                
                <div class="button-modal">
                    <input type="submit" value="Guardar cambios">

                </div>


            </form>




        </div>
    </div>
    
    <%}%>