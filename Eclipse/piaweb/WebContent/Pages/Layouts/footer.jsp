
<div class="footer">
            <div class="top-footer" id="toTop">
                Volver arriba
            </div>

            <div class="bottom-footer">
                <a href="#">Categorias</a>
                
                <ul>
                	
				<%for(Category categoryItem : categories) {%>
                    <li>
                        <a href="/piaweb/Categorias?categoryID=<%=categoryItem.getID_Categoria()%>"><%=categoryItem.getNombre() %></a>
                    </li>
				<%} %>
                
                </ul>

            </div>
            <div class="developers">
                <span>Copyright &copy; LMAD</span>
                <span>Felix Leopoldo Lara Sanchez <br>
                     Derek Jafet Cortes Madriz
                </span>
            </div>
        </div>