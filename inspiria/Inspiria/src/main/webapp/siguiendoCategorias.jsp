<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="uva.ssw.modelo.Categoria" %>

<% request.setAttribute("paginaActiva", "siguiendo"); %>
<%@ include file="plantilla.jsp" %>
            <div class="contenedorFiltros">
                <button class="btnFiltro activoFiltro">Categorías</button>
                <button class="btnFiltro" onclick = "location.href='siguiendoTematicas.jsp'">Temáticas</button>
                <button class="btnFiltro" onclick = "location.href='siguiendoPerfiles.jsp'">Perfiles</button>
            </div>
            <div class="siguiendoCuadricula">
                <%
                    Usuario userSesion = (Usuario) session.getAttribute("usuario");
                    if (userSesion != null) {
                        ArrayList<Categoria> categoriasSeguidas = uva.ssw.database.ControladorDAOS.obtenerCategoriasSeguidas(userSesion.getIdentificador());
                        
                        if (categoriasSeguidas != null && !categoriasSeguidas.isEmpty()) {
                            for (Categoria cat : categoriasSeguidas) {
                %>
                    <div class="tarjetaCategoria">
                        <div class="categoriaMedia" onclick="location.href='DetalleCategoriaServlet?id=<%= cat.getIdCategoria() %>'" style="cursor:pointer;">
                            <% if (cat.getFotoBase64() != null && !cat.getFotoBase64().isEmpty()) { %>
                                <img src="data:image/jpeg;base64,<%= cat.getFotoBase64() %>" alt="<%= cat.getNombre() %>">
                            <% } else { %>
                                <img src="img/categoria_default.jpg" alt="Categoría">
                            <% } %>
                        </div>
                        
                        <div class="categoriaInfo">
                            <div class="categoriaTop" style="display: flex; justify-content: space-between; align-items: center;">
                                <h3 class="categoriaTitulo"><%= cat.getNombre() %></h3>
                                <img src="img/corazon_relleno.png" class="iconoSeguir" 
                                     style="width: 25px; height: 25px; cursor: pointer;"
                                     onclick="window.location.href='SeguirCategoriaServlet?id=<%= cat.getIdCategoria() %>'; return false;">
                            </div>
                        </div>
                    </div>
                <%          }
                        } else {
                %>
                    <p style="grid-column: 1 / -1; text-align: center;">Aún no sigues ninguna categoría.</p>
                <%      }
                    }
                %>
            </div>
<%@ include file="plantillaFin.jsp" %>     
