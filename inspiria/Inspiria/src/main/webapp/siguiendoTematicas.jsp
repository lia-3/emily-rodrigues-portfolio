<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="uva.ssw.modelo.Usuario" %>
<%@ page import="uva.ssw.modelo.Tematica" %>
<% request.setAttribute("paginaActiva", "siguiendo"); %>
<%@ include file="plantilla.jsp" %> 

            <div class="contenedorFiltros">
                <button class="btnFiltro" onclick = "location.href='siguiendoCategorias.jsp'">Categorías</button>
                <button class="btnFiltro activoFiltro">Temáticas</button>
                <button class="btnFiltro" onclick = "location.href='siguiendoPerfiles.jsp'">Perfiles</button>
            </div>
     
            <div class="siguiendoCuadricula">
                <%
                    Usuario userSesion = (Usuario) session.getAttribute("usuario");
                    if (userSesion != null) {
                        ArrayList<Tematica> tematicasSeguidas = uva.ssw.database.ControladorDAOS.obtenerTematicasSeguidas(userSesion.getIdentificador());
                        
                        if (tematicasSeguidas != null && !tematicasSeguidas.isEmpty()) {
                            for (Tematica tem : tematicasSeguidas) {
                %>
                    <div class="tarjetaCategoria">
                        <%-- Redirige al detalle de la temática cuando Iria lo implemente --%>
                        <div class="categoriaMedia" onclick="location.href='DetalleTematicaServlet?id=<%= tem.getIdTematica() %>'" style="cursor:pointer;">
                            <% if (tem.getFotoBase64() != null && !tem.getFotoBase64().isEmpty()) { %>
                                <img src="data:image/jpeg;base64,<%= tem.getFotoBase64() %>" alt="<%= tem.getNombre() %>">
                            <% } else { %>
                                <img src="img/tematica.png" alt="Temática">
                            <% } %>
                        </div>
                        <div class="categoriaInfo">
                            <div class="categoriaTop" style="display: flex; justify-content: space-between; align-items: center;">
                                <h3 class="categoriaTitulo"><%= tem.getNombre() %></h3>
                                <img src="img/corazon_relleno.png" class="iconoSeguir" 
                                     style="width: 25px; height: 25px; cursor: pointer;"
                                     onclick="window.location.href='SeguirTematicaServlet?id=<%= tem.getIdTematica() %>'; return false;">
                            </div>
                        </div>
                    </div>
                <%          }
                        } else {
                %>
                    <p style="grid-column: 1 / -1; text-align: center;">Aún no sigues ninguna temática.</p>
                <%      }
                    }
                %>
            </div>

<%@ include file="plantillaFin.jsp" %>