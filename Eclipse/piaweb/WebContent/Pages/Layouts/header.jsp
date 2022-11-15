<%List<Category> categories = (List<Category>)request.getAttribute("categories");%>
<div class="header">
            <div class="top-header">
                <div class="top-header-container">
                    <div class="logo-element">
                        <img src="/piaweb/Images/Logo.png" alt="">
                        <h1><a href="/piaweb/">Questionary</a></h1>
                    </div>
					<form  method="GET" action="/piaweb/Busqueda">
                   
                    <div class="search-bar">
                    
                        <div class="search-bar-elements">
                        <!--    <input type="hidden" name="accion" value="Resultado">-->
                         <input type="text" placeholder="Buscar pregunta..." name="searchbox" id="">
                            <div class="search-button">
                          <!--    <c:url var="linkSearch" value="/Busqueda/Resultado"></c:url> -->
                              <button><i class="fas fa-search"></i></button> 
                            </div>
                        </div>
 				
                    </div>
					</form>
                    <div class="question-button">
						<c:url var="linkAsk" value="/Preguntar" ></c:url>
                        <a href="${linkAsk}">Preguntar</a>
                    </div>
                </div>

                <div class="user-log">
               		 <%if(user!=null){ %>
					<c:url var="linkProfile" value="/Perfil/Preguntas">
                    <c:param name="userID" value="<%=user.getID_Usuario() %>"></c:param>
                    </c:url>
                    <a href="${linkProfile}">
                    
                  	<img style="height:50px; width:50px; border-radius:100%"src="/piaweb/Imagenes/Usuario?imageID=<%=user.getID_Usuario() %>" alt="">
                    
                    </a>
                     <%}else{%>
                    	 <a>
                    	  <i class="fas fa-user"></i>
                    	 </a>

                     <%} %>
                    <div class="dropdown header-profile">
                        <button class="btn btn-secondary fas fa-sort-down" type="button" id="dropdownMenuButton" data-bs-toggle="dropdown" aria-expanded="false">
                          
                            </button>
                        <ul class="dropdown-menu header-profile" aria-labelledby="dropdownMenuButton">
                            
                            <%if(user != null) { %>
                            	<c:url var="linkUser" value="/Perfil/Preguntas">
                            	<c:param name="userID" value="<%=user.getID_Usuario() %>"></c:param>
                            	</c:url>
                           		<li><a class="dropdown-item fas fa-user" href="${linkUser}"><span><%= user.getNombre() + " " + user.getApellidoP()%>
                           		</span></a>
                            	</li>
                            <%} else { %>
                       
                           		<li><a class="dropdown-item fas fa-user" href="/piaweb/Acceso/Login"><span>Iniciar Sesi&oacuten
                           		</span></a>
                            	</li>
                            	
                            	     <li><a class="dropdown-item fas fa-user" href="/piaweb/Acceso/Registro"><span>Invitado (Registrate)
                           		</span></a>
                            	</li>
                            <%} %>
                            
                            <%if(user != null) { %>
                            	<c:url var="linkLogout" value="/Acceso">
                            		<c:param name="action" value="logout"></c:param>
                            	</c:url>
	                            <li>
	                                <a class="dropdown-item fas fa-sign-out-alt" href="${linkLogout}"> <span>Cerrar sesi&oacuten</span> </a>
	                            </li>
                            <%} %>
                            
                          
                        </ul>
                    </div>
                </div>
            </div>


            <div class="bottom-header">
                <div class="bottom-header-container">
					<%for(int i=0; i<5; i++) {%>
						<%if(i < categories.size()) {%>
                    	<a href="/piaweb/Categorias?categoryID=<%=categories.get(i).getID_Categoria()%>"><%=categories.get(i).getNombre() %></a>
                    	<%} else { break;}%>
                    <%} %>
                </div>
            </div>
</div>