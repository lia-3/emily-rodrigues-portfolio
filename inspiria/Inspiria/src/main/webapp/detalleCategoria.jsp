<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="uva.ssw.modelo.*" %>

<% request.setAttribute("paginaActiva", "categorias"); %>
<%@ include file="plantilla.jsp" %>
<% 
        ArrayList<Tutorial> listaDetallesCategoria = (ArrayList<Tutorial>) request.getAttribute("tutorialesCategoria");
        Usuario userSesion = (Usuario) session.getAttribute("usuario");
        String nombreCategoria = "Categoria";
        if (listaDetallesCategoria != null && !listaDetallesCategoria.isEmpty()) {
            nombreCategoria = listaDetallesCategoria.get(0).getCategoria().getNombre();
        }
    %>
<div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 15px;">
       <h4 style="margin: 0; font-size: 1.8em;"><%= nombreCategoria %></h4>
</div>
<div class="muroCuadricula">
    <%
        if (listaDetallesCategoria != null && !listaDetallesCategoria.isEmpty()) {
            for (Tutorial t : listaDetallesCategoria) {
    %>
            <div class="post" 
                 onclick="location.href='DetalleTutorialServlet?id=<%= t.getId() %>'" 
                 style="cursor: pointer;">
                 
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
                       // Mostrar corazón para seguir al creador del tutorial si es el propio usuario
                       if (userSesion != null && userSesion.getIdentificador() != t.getAutor().getIdentificador()) { 
                           boolean loSigueMuro = uva.ssw.database.ControladorDAOS.esPerfilSeguido(userSesion.getIdentificador(), t.getAutor().getIdentificador());
                    %>
                        <img src='img/<%= loSigueMuro ? "corazon_relleno.png" : "corazon_vacio.png" %>' 
                             style="width: 25px; height: 25px; cursor: pointer; transition: transform 0.2s;" 
                             alt="Seguir" 
                             data-id="<%= t.getAutor().getIdentificador() %>"
                             title="<%= loSigueMuro ? "Dejar de seguir" : "Seguir" %>"
                             onclick="event.stopPropagation(); alternarSeguirCreador(this)">
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
                    <div class="infoTop" style="display: flex; justify-content: space-between; align-items: center;">
                        <h3 class="postTitulo" style="margin: 0;"><%= t.getTitulo() %></h3>
                        <div class="postAcciones" style="display: flex; gap: 10px;">
                            <% 
                                boolean estaGuardado = false;
                                if (userSesion != null) {
                                    estaGuardado = uva.ssw.database.ControladorDAOS.esTutorialGuardado(userSesion.getIdentificador(), t.getId());
                                }
                             %>
                            <img src='img/<%= estaGuardado ? "btn_guardar_relleno.png" : "btn_guardar_vacio.png" %>' 
                                class="iconoAccion" alt="Guardar" title="Guardar tutorial"
                                data-id="<%= t.getId() %>"
                                style="cursor: pointer; width: 22px; height: 22px;"
                                onclick="event.stopPropagation(); alternarGuardarTutorial(this)">
                                
                            <img src="img/comentario.png" class="iconoAccion" alt="Comentar" title="Ir a comentarios"
                                 style="cursor: pointer; width: 22px; height: 22px;"
                                 onclick="event.stopPropagation(); location.href='DetalleTutorialServlet?id=' + <%= t.getId() %> + '#seccionComentarios'">
                        </div>
                    </div>
                    <p class="postInfo" style="margin: 5px 0;"><%= t.getCategoria().getNombre() %><br><%= t.getTematica().getNombre() %></p>
                    <p class="postDesc" style="margin: 5px 0; color: #666;"><%= t.getDescripcion() %></p>
                </div>
            </div>
    <% 
            }
        } else { 
    %>
        <p style="grid-column: 1 / -1; text-align: center; color: #777;">No hay tutoriales publicados en esta categoría todavía.</p>
    <%  } %>
</div>

<script>
function alternarSeguirCreador(elementoImg) {
    const idAutor = elementoImg.getAttribute('data-id');
    
    fetch('SeguirPerfilServlet?id=' + idAutor, { method: 'GET' })
    .then(response => {
        if (response.ok) {
            if (elementoImg.src.includes('corazon_vacio.png')) {
                elementoImg.src = 'img/corazon_relleno.png';
                elementoImg.title = 'Dejar de seguir';
            } else {
                elementoImg.src = 'img/corazon_vacio.png';
                elementoImg.title = 'Seguir';
            }
        } else if (response.status === 401) {
            alert('Debes iniciar sesión para seguir creadores.');
            window.location.href = 'iniciarSesion.jsp';
        }
    })
    .catch(error => console.error('Error Follow:', error));
}

function alternarGuardarTutorial(elementoImg) {
    const idTutorial = elementoImg.getAttribute('data-id');
    
    fetch('GuardarTutorialServlet?id=' + idTutorial, { method: 'GET' })
    .then(response => {
        if (response.ok) {
            if (elementoImg.src.includes('btn_guardar_vacio.png')) {
                elementoImg.src = 'img/btn_guardar_relleno.png';
            } else {
                elementoImg.src = 'img/btn_guardar_vacio.png';
            }
        } else if (response.status === 401) {
            alert('Debes iniciar sesión para guardar tutoriales.');
            window.location.href = 'iniciarSesion.jsp';
        }
    })
    .catch(error => console.error('Error Guardar:', error));
}
</script>

<%@ include file="plantillaFin.jsp" %>