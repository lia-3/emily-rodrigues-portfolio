<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="uva.ssw.modelo.*" %>
<%@ page import="java.util.Base64" %>
<%
    Producto producto = (Producto) request.getAttribute("producto");
    if (producto == null) {
        response.sendRedirect("PrincipalServlet");
        return;
    }
%>
<%@ include file="plantilla.jsp" %>

<div class="seccionFotos">
        <%-- Envolvemos el título y el marcapáginas en un flex para alinearlos a los extremos --%>
        <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 15px;">
            <h4 style="margin: 0; font-size: 1.8em;"><%= producto.getTitulo() %></h4>
        </div>
        
        <%-- La foto de portada --%>
        <% if (producto.getFotoBase64() != null && !producto.getFotoBase64().isEmpty()) { %>
            <img src="data:image/jpeg;base64,<%= producto.getFotoBase64() %>" class="imagenProducto" alt="Portada">
        <% } %>
</div>
    <%-- SECCIÓN DEL AUTOR: Botón dinámico que redirige al perfil + Corazón de Seguir --%>
    <% 
        Usuario autor = producto.getAutor(); 
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
        <p><%= producto.getDescripcion() %></p>
    </div>
    <div class="seccionTexto">
        <label>Categoría</label>
        <p><%= producto.getCategoria().getNombre() %></p>
    </div>
    <div class="seccionTexto">
        <label>Temática</label>
        <p><%= producto.getTematica().getNombre() %></p>
    </div>
    <div class="seccionTexto">
        <label>Precio</label>
        <p><%= producto.getPrecio() %>€</p>
    </div>
    <div class="seccionTexto">
        <label>Stock</label>
        <p><%= producto.getStock() %></p>
    </div>
</div>

<%@ include file="plantillaFin.jsp" %>     
