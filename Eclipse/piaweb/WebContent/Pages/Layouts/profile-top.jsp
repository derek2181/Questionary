<%
response.setHeader("Cache-Control","no-cache"); //HTTP 1.1   
response.setHeader("Cache-Control","no-store"); //HTTP 1.1 
response.setHeader("Pragma","no-cache"); //HTTP 1.0   
response.setDateHeader("Last-Modified", (new Date()).getTime() ); //prevents caching at the proxy server
%>

			<div class="profile-top-block">
                <div class="profile-info">
                    <img src="/piaweb/Imagenes/Usuario?imageID=<%=userProfile.getID_Usuario()%>" alt="">
                    <div class="profile-elements">
                        <span><%=userProfile.getNombre() + " " + userProfile.getApellidoP()%></span>
                      
                                                  <%if (user != null) { %>
                            	<%if(user.getID_Usuario().equals(userProfile.getID_Usuario())) {%>
	                            <div class="profile-tags">
                          
							   <h3><%=userProfile.getNombre() + " " + userProfile.getApellidoP() + " " +userProfile.getApellidoM() %></h3>
                            	<h3><%= userProfile.getCorreo() %></h3>
                            	<h3><%= java.util.concurrent.TimeUnit.DAYS.convert( Math.abs(userProfile.getFecha_nacimiento().getTime().getTime() - new java.util.Date().getTime() ), java.util.concurrent.TimeUnit.MILLISECONDS)/365%> a&ntildeos</h3>
  
	                            <div class="edit-profile">
                                <button class="btn-modal">Editar perfil</button>
	                            </div>
                       			 </div>
                            	<%} %>
							<%} %>
							 	
									
                    </div>
                </div>
            </div>
            <%if(!userProfile.isActivo()){ %>
                <div class="account-suspended">
                    <i class="fas fa-times"></i>Esta cuenta ha sido suspendida<i class="fas fa-times"></i>
                </div>
            <%}%>
            
  