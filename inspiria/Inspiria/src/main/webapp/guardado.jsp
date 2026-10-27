<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="uva.ssw.modelo.*" %>
<%@ page import="java.util.ArrayList" %>
<% request.setAttribute("paginaActiva", "guardado"); %>
<%@ include file="plantilla.jsp" %> 

<div class="muroCuadricula">
    <% 
        ArrayList<Tutorial> listaGuardados = (ArrayList<Tutorial>) request.getAttribute("tutorialesGuardados");
        
        if (listaGuardados != null && !listaGuardados.isEmpty()) {
            for (Tutorial t : listaGuardados) {
    %>
        <a href="DetalleTutorialServlet?id=<%= t.getId() %>" style="text-decoration: none; color: inherit;">
            <div class="post">
                <div class="postCabecera">
                    <span><%= t.getAutor().getNombre() %></span>
                    <% if (t.getAutor().getFotoBase64() != null && !t.getAutor().getFotoBase64().isEmpty()) { %>
                        <img src="data:image/jpeg;base64,<%= t.getAutor().getFotoBase64() %>" class="miniPerfil">
                    <% } else { %>
                        <img src="img/usuario.png" class="miniPerfil">
                    <% } %>
                </div>
                
                <div class="postMedia">
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
                            <img src="img/comentario.png" class="iconoAccion" alt="Comentar"
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
        <p>Aún no has guardado ningún tutorial.</p>
    <%  } %>
</div>

<%@ include file="plantillaFin.jsp" %>