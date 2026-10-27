<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="uva.ssw.modelo.*" %>
<%@ page import="java.util.ArrayList" %>
<% request.setAttribute("paginaActiva", "muro"); %>
<%@ include file="plantilla.jsp" %>     

<div class="muroCuadricula">
    <% 
        ArrayList<Tutorial> listaMuro = (ArrayList<Tutorial>) request.getAttribute("tutorialesMuro");
        
        if (listaMuro != null && !listaMuro.isEmpty()) {
            for (Tutorial t : listaMuro) {
    %>
        <a href="DetalleTutorialServlet?id=<%= t.getId() %>" style="text-decoration: none; color: inherit;">
            <div class="post">
                <div class="postCabecera" style="display: flex; align-items: center; justify-content: space-between; padding-bottom: 10px;">
                    
                    <div style="display: flex; align-items: center; gap: 10px;">
                        <% if (t.getAutor().getFotoBase64() != null && !t.getAutor().getFotoBase64().isEmpty()) { %>
                            <img src="data:image/jpeg;base64,<%= t.getAutor().getFotoBase64() %>" class="miniPerfil" style="width: 30px; height: 30px; border-radius: 50%; object-fit: cover;">
                        <% } else { %>
                            <img src="img/usuario.png" class="miniPerfil" style="width: 30px; height: 30px; border-radius: 50%;">
                        <% } %>
                        <span style="font-weight: bold; color: #333;">@<%= t.getAutor().getNombre() %></span>
                    </div>

                    <% 
                       if (session.getAttribute("usuario") != null && ((Usuario)session.getAttribute("usuario")).getIdentificador() != t.getAutor().getIdentificador()) { 
                           boolean loSigueMuro = uva.ssw.database.ControladorDAOS.esPerfilSeguido(((Usuario)session.getAttribute("usuario")).getIdentificador(), t.getAutor().getIdentificador());
                    %>
                        <img src='img/<%= loSigueMuro ? "corazon_relleno.png" : "corazon_vacio.png" %>' 
                             style="width: 25px; height: 25px; cursor: pointer; transition: transform 0.2s;" 
                             alt="Seguir" 
                             title="<%= loSigueMuro ? "Dejar de seguir" : "Seguir" %>"
                             onclick="window.location.href='SeguirPerfilServlet?id=<%= t.getAutor().getIdentificador() %>'; return false;">
                    <% } %>
                </div>
                
                <div class="postMedia">
                    <%-- Mostramos la foto real de la base de datos --%>
                    <% if (t.getFotoBase64() != null && !t.getFotoBase64().isEmpty()) { %>
                        <img src="data:image/jpeg;base64,<%= t.getFotoBase64() %>" alt="Post">
                    <% } else { %>
                        <img src="img/icono_tutorial.jpeg" alt="Sin foto">
                    <% } %>
                </div>
                
                <div class="postInfoMuro">
                    <div class="infoTop">
                        <h3 class="postTitulo"><%= t.getTitulo() %></h3>
                        <div class="postAcciones">
                            <% 
                                boolean estaGuardado = false;
                                Usuario userSesion = (Usuario) session.getAttribute("usuario");
                                if (userSesion != null) {
                                    estaGuardado = uva.ssw.database.ControladorDAOS.esTutorialGuardado(userSesion.getIdentificador(), t.getId());
                                }
                             %>
                            <img src='img/<%= estaGuardado ? "btn_guardar_relleno.png" : "btn_guardar_vacio.png" %>' 
                                class="iconoAccion" alt="Guardar" title="Guardar tutorial"
                                onclick="window.location.href='GuardarTutorialServlet?id=<%= t.getId() %>'; return false;">
                            <img src="img/comentario.png" class="iconoAccion" alt="Comentar" title="Ir a comentarios"
                                 onclick="window.location.href='DetalleTutorialServlet?id=<%= t.getId() %>#seccionComentarios'; return false;">
                        </div>
                    </div>
                    <p class="postInfo"><%= t.getCategoria().getNombre() %><br><%= t.getTematica().getNombre() %></p>
                    <p class="postDesc"><%= t.getDescripcion() %></p>
                </div>
            </div>
        </a>
    <% 
            }
        } else { 
    %>
        <p>No hay tutoriales para mostrar en el muro en este momento.</p>
    <%  } %>
</div>

<%@ include file="plantillaFin.jsp" %>