<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="uva.ssw.modelo.Usuario" %>
<%@ include file="plantilla.jsp" %>     

<%
    Usuario usuarioLogueado = (Usuario) session.getAttribute("usuario");
%>

<h1 class="tituloConfiguracion">Configuración</h1>

<div class="contenedorConfiguracion">
    <div class="tarjetaPerfil">
        <div class="fotoPerfilPlaceholder">
            <% if (usuarioLogueado != null && usuarioLogueado.getFotoBase64() != null && !usuarioLogueado.getFotoBase64().isEmpty()) { %>
                <img src="data:image/jpeg;base64,<%= usuarioLogueado.getFotoBase64() %>" class="imagenPerfilGrande">
            <% } else { %>
                <img src="img/usuario.png" class="imagenPerfilGrande">
            <% } %>
        </div>

        <div class="infoPerfil">
            <h3>Nombre de Usuario</h3>
            <p><%= (usuarioLogueado != null) ? usuarioLogueado.getNombre() : "No disponible" %></p>
            
            <h3>Correo</h3>
            <p><%= (usuarioLogueado != null) ? usuarioLogueado.getCorreo() : "No disponible" %></p>
            
            <h3>Descripción</h3>
            <p>
                <% if (usuarioLogueado != null && usuarioLogueado.getDescripcion() != null && !usuarioLogueado.getDescripcion().trim().isEmpty()) { %>
                    <%= usuarioLogueado.getDescripcion() %>
                <% } else { %>
                    Sin descripción
                <% } %>
            </p>
        </div>
    </div>

    <div class="seccionBotonesConfig">
        <button type="button" class="botonConfig" onclick="window.location.href='EditarFotoServlet'">
            Cambiar Foto
        </button>
        <button type="button" class="botonConfig" onclick="window.location.href='EditarNombreServlet'">
            Cambiar Nombre Usuario
        </button>
        <button class="botonConfig" onclick="window.location.href='EditarContrasennaServlet'">
            Cambiar Contraseña
        </button>
        <button class="botonConfig" onclick="window.location.href='editarDescripcion.jsp'">
            Cambiar Descripcion
        </button>
        <button class="botonConfig" onclick="window.location.href='GestionarComisionesServlet'">
            Gestionar Comisiones
        </button>
        <button class="botonConfig" onclick="window.location.href='GestionarEnlacesContactosServlet'">
            Gestionar Contactación
        </button>
    </div>
</div>

<%@ include file="plantillaFin.jsp" %>