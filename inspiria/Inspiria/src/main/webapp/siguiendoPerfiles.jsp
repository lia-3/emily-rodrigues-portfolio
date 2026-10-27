<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="uva.ssw.modelo.Usuario" %>

<% request.setAttribute("paginaActiva", "siguiendo"); %>
<%@ include file="plantilla.jsp" %>

            <div class="contenedorFiltros">
                <button class="btnFiltro" onclick = "location.href='siguiendoCategorias.jsp'">Categorías</button>
                <button class="btnFiltro" onclick = "location.href='siguiendoTematicas.jsp'">Temáticas</button>
                <button class="btnFiltro activoFiltro">Perfiles</button>
            </div>
            <div class="siguiendoCuadricula">
            <%
                Usuario userSesion = (Usuario) session.getAttribute("usuario");
                if (userSesion != null) {
                    ArrayList<Usuario> seguidos = uva.ssw.database.ControladorDAOS.obtenerPerfilesSeguidos(userSesion.getIdentificador());
                    
                    if (seguidos != null && !seguidos.isEmpty()) {
                        for (Usuario u : seguidos) {
            %>
                <div class="tarjetaPerfilSeguido">
                    <div class="perfilMedia" onclick="location.href='VerPerfilServlet?id=<%= u.getIdentificador() %>'" style="cursor:pointer;">
                        <% if (u.getFotoBase64() != null && !u.getFotoBase64().isEmpty()) { %>
                            <img src="data:image/jpeg;base64,<%= u.getFotoBase64() %>" alt="Foto de Perfil">
                        <% } else { %>
                            <img src="img/usuario.png" alt="Foto de Perfil">
                        <% } %>
                    </div>
                    
                    <div class="perfilFooter">
                        <span class="nombreUser"><%= u.getNombre() %></span>
                        <img src="img/corazon_relleno.png" class="iconoSeguir" style="cursor:pointer;"
                             onclick="location.href='SeguirPerfilServlet?id=<%= u.getIdentificador() %>'">
                    </div>
                </div>
            <%          }
                    } else {
            %>
                <p style="grid-column: 1 / -1; text-align: center;">Aún no sigues a ningún perfil.</p>
            <%      }
                }
            %>
        </div>

<%@ include file="plantillaFin.jsp" %> 