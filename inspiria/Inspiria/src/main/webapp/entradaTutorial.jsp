<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="uva.ssw.modelo.*" %>
<%@ page import="java.util.Base64" %>
<%
    // Recuperamos el objeto tutorial enviado por el Servlet
    Tutorial tutorial = (Tutorial) request.getAttribute("tutorial");
    if (tutorial == null) {
        response.sendRedirect("PrincipalServlet");
        return;
    }
%>
<%@ include file="plantilla.jsp" %>

<div class="seccionFotos">
        <%-- Envolvemos el título y el marcapáginas en un flex para alinearlos a los extremos --%>
        <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 15px;">
            <h4 style="margin: 0; font-size: 1.8em;"><%= tutorial.getTitulo() %></h4>
            
            <%-- RECUPERAMOS EL BOTÓN DE GUARDAR TUTORIAL (Marcapáginas) --%>
            <% 
                boolean guardadoDetalle = false;
                Usuario userSess = (Usuario) session.getAttribute("usuario");
                if (userSess != null) {
                    guardadoDetalle = uva.ssw.database.ControladorDAOS.esTutorialGuardado(userSess.getIdentificador(), tutorial.getId());
                }
            %>
            <a href="GuardarTutorialServlet?id=<%= tutorial.getId() %>" title="Guardar tutorial">
                <img src='img/<%= guardadoDetalle ? "btn_guardar_relleno.png" : "btn_guardar_vacio.png" %>' 
                     class="iconoAccion" 
                     alt="Guardar" 
                     style="width: 25px; height: 25px; cursor: pointer;">
            </a>
        </div>
        
        <%-- La foto de portada --%>
        <% if (tutorial.getFotoBase64() != null && !tutorial.getFotoBase64().isEmpty()) { %>
            <img src="data:image/jpeg;base64,<%= tutorial.getFotoBase64() %>" class="imagenTutorial" alt="Portada">
        <% } %>
</div>

    <%-- SECCIÓN DEL AUTOR: Botón dinámico que redirige al perfil + Corazón de Seguir --%>
    <% 
        Usuario autor = tutorial.getAutor(); 
        if (autor != null) { 
            Usuario usrSesion = (Usuario) session.getAttribute("usuario");
            String destinoEnlaceAutor = "VerPerfilServlet?id=" + autor.getIdentificador();
            if (usrSesion != null && usrSesion.getIdentificador() == autor.getIdentificador()) {
                destinoEnlaceAutor = "perfil";
            }
    %>
        <div class="autor-info" style="margin: 20px 0; display: flex; justify-content: center; align-items: center; gap: 15px;">
            
            <%-- EL BOTÓN ELEGANTE DEL AUTOR --%>
            <a href="<%= destinoEnlaceAutor %>" 
               style="text-decoration: none; color: #333; display: flex; align-items: center; background: #ffffff; padding: 10px 20px; border-radius: 50px; border: 1px solid #ddd; box-shadow: 0 2px 5px rgba(0,0,0,0.1); transition: transform 0.2s;">
                
                <%-- Foto de perfil del autor --%>
                <% if (autor.getFotoBase64() != null && !autor.getFotoBase64().isEmpty()) { %>
                    <img src="data:image/jpeg;base64,<%= autor.getFotoBase64() %>" 
                         style="width: 40px; height: 40px; border-radius: 50%; object-fit: cover; margin-right: 12px; border: 1px solid #eee;" 
                         alt="Foto de <%= autor.getNombre() %>">
                <% } else { %>
                    <%-- Nota: He puesto usuario.png que es el que estabas usando en otros sitios --%>
                    <img src="img/usuario.png" 
                         style="width: 40px; height: 40px; border-radius: 50%; object-fit: cover; margin-right: 12px; border: 1px solid #eee;" 
                         alt="Avatar por defecto">
                <% } %>
                
                <div style="display: flex; flex-direction: column; text-align: left;">
                    <span style="font-size: 0.75em; color: #888; text-transform: uppercase; letter-spacing: 0.5px;">Publicado por</span>
                    <strong style="font-size: 1em;">@<%= autor.getNombre() %></strong>
                </div>
            </a>

            <%-- EL BOTÓN DEL CORAZÓN (Justo al lado) --%>
            <% 
               if (usrSesion != null && usrSesion.getIdentificador() != autor.getIdentificador()) { 
                   boolean sigueAutor = uva.ssw.database.ControladorDAOS.esPerfilSeguido(usrSesion.getIdentificador(), autor.getIdentificador());
            %>
                <img src='img/<%= sigueAutor ? "corazon_relleno.png" : "corazon_vacio.png" %>' 
                     alt="Seguir" title="<%= sigueAutor ? "Dejar de seguir" : "Seguir" %>"
                     style="width: 30px; height: 30px; cursor: pointer; transition: transform 0.2s;"
                     onclick="location.href='SeguirPerfilServlet?id=<%= autor.getIdentificador() %>'">
            <% } %>

        </div>
    <% } %>

    <div class="informacionProducto">
        <div class="seccionTexto">
            <label>Descripción</label>
            <p><%= tutorial.getDescripcion() %></p>
        </div>
        <div class="seccionTexto">
            <label>Categoría</label>
            <p><%= tutorial.getCategoria().getNombre() %></p>
        </div>
        <div class="seccionTexto">
            <label>Temática</label>
            <p><%= tutorial.getTematica().getNombre() %></p>
        </div>
        <div class="seccionTexto">
            <label>Dificultad</label>
            <p><%= tutorial.getDificultad() %></p>
        </div>
        
        <%-- Materiales --%>
        <div class="seccionTexto">
            <label>Materiales necesarios</label>
            <ul>
                <% if (tutorial.getMateriales() != null && !tutorial.getMateriales().isEmpty()) {
                    for (Material m : tutorial.getMateriales()) { %>
                        <li>
                            <a href="<%= m.getUrl() %>" target="_blank"><%= m.getNombre() %></a>
                        </li>
                <%  } 
                } else { %>
                    <li>No se han especificado materiales.</li>
                <% } %>
            </ul>
        </div>

        <%-- Herramientas --%>
        <div class="seccionTexto">
            <label>Herramientas necesarias</label>
            <ul>
                <% if (tutorial.getHerramientas() != null && !tutorial.getHerramientas().isEmpty()) {
                    for (Herramienta h : tutorial.getHerramientas()) { %>
                        <li>
                            <a href="<%= h.getUrl() %>" target="_blank"><%= h.getNombre() %></a>
                        </li>
                <%  } 
                } else { %>
                    <li>No se han especificado herramientas.</li>
                <% } %>
            </ul>
        </div>

        <%-- Datos del producto asociado si existe --%>
        <% if (tutorial.getProductoAsociado() != null) { 
            Producto p = tutorial.getProductoAsociado(); %>
            <div class="seccionTexto">
                <label>Precio del producto</label>
                <p><%= p.getPrecio() %>€</p>
            </div>
            <div class="seccionTexto">
                <label>Stock disponible</label>
                <p><%= p.getStock() %></p>
            </div>
        <% } %>

        <%-- Pasos detallados del tutorial --%>
        <div class="seccionTexto">
            <label>Pasos a seguir</label>
            <% if (tutorial.getPasos() != null) {
                for (Paso paso : tutorial.getPasos()) { %>
                    <div class="paso-detalle" style="margin-bottom: 20px; border-left: 3px solid #ddd; padding-left: 15px;">
                        <p><strong>Paso <%= paso.getNumero() %>:</strong> <%= paso.getDescripcion() %></p>
                        <% if (paso.getFotoBase64() != null && !paso.getFotoBase64().isEmpty()) { %>
                            <img src="data:image/jpeg;base64,<%= paso.getFotoBase64() %>" 
                                 style="max-width: 300px; display:block; margin-top:10px; border-radius: 8px;">
                        <% } %>
                    </div>
            <%  } 
            } %>
        </div>
    </div>

    <%-- Sección de comentarios --%>
    <div id="seccionComentarios" class="seccionComentarios">
        <h3>Comentarios</h3>
        <form action="PublicarComentarioServlet" method="post" class="nuevoComentario">
            <input type="hidden" name="idTutorial" value="<%= tutorial.getId() %>">
            <textarea name="textoComentario" placeholder="Escribe un comentario..." required></textarea>
            <button type="submit" class="btnPublicarComentario">Publicar</button>
        </form>

        <div class="listaComentarios">
            <% if (tutorial.getComentarios() != null) {
                for (Comentario c : tutorial.getComentarios()) { %>
                    <div class="comentario" style="margin-bottom: 15px; padding: 10px; background: #fdfdfd; border-radius: 5px;">
                        <label style="font-weight: bold; color: #555;">@<%= c.getAutor().getNombre() %></label>
                        <p style="margin: 5px 0;"><%= c.getTexto() %></p>
                    </div>
            <%  } 
            } %>
        </div>
    </div>
            
<%@ include file="plantillaFin.jsp" %>