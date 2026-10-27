<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="uva.ssw.modelo.Usuario" %>
<% 
    // Establecemos la página activa para el menú lateral
    request.setAttribute("paginaActiva", "editarPerfil"); 
%>
<%@ include file="plantilla.jsp" %>

<%
    Usuario usuarioLogueado = (Usuario) session.getAttribute("usuario");
    if (usuarioLogueado == null) {
        response.sendRedirect("iniciarSesion.jsp");
        return;
    }
%>

<div class="seccionConfiguracion">
    <h1 class="tituloSeccion">Configuración | Nombre Usuario</h1>

    <div class="contenedorFormularioCentrado">
        <!-- El formulario apunta al Servlet -->
        <form action="EditarNombreServlet" method="post">
            
            <div class="grupoFormulario">
                <label for="nombreActual">Nombre de usuario actual</label>
                <input type="text" id="nombreActual" name="nombreActual" 
                       value="<%= usuarioLogueado.getNombre() %>" readonly 
                       class="inputFormulario inputSoloLectura">
            </div>

            <div class="grupoFormulario">
                <label for="nuevoNombre">Nuevo nombre de usuario</label>
                <input type="text" id="nuevoNombre" name="nuevoNombre" 
                       placeholder="Escribe tu nuevo nombre..." 
                       class="inputFormulario" required>
            </div>

            <%-- Mensajes de error o éxito si el servlet los envía --%>
            <% String error = (String) request.getAttribute("error"); 
               if (error != null) { %>
                <p class="error" style="margin-bottom: 15px;"><%= error %></p>
            <% } %>

            <div class="contenedorBotones">
                <button type="button" class="botonSecundario" 
                        onclick="window.location.href='editarPerfil.jsp'">Cancelar</button>
                <button type="submit" class="botonPrincipal">Aceptar</button>
            </div>

        </form>
    </div>
</div>

<%@ include file="plantillaFin.jsp" %>