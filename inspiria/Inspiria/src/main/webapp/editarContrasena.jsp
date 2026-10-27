<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="uva.ssw.modelo.Usuario" %>
<% 
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

<h2 class="tituloSeccion">Configuración | Contraseña</h2>

<div class="contenedorFormularioContraseña">
    <div class="tarjetaFormulario">
        <form action="EditarContrasennaServlet" method="post">
            
            <div class="campo">
                <label for="passActual">Contraseña actual</label>
                <input type="password" id="passActual" name="passActual" 
                       placeholder="Escribe tu contraseña actual" required>
            </div>

            <div class="campo">
                <label for="passNueva">Nueva contraseña</label>
                <input type="password" id="passNueva" name="passNueva" 
                       placeholder="Escribe la nueva contraseña" required>
            </div>

            <div class="campo">
                <label for="passRepetida">Repetir nueva contraseña</label>
                <input type="password" id="passRepetida" name="passRepetida" 
                       placeholder="Repite la nueva contraseña" required>
            </div>

            <%-- Zona de mensajes --%>
            <% String error = (String) request.getAttribute("error"); 
               if (error != null) { %>
                <p class="error" style="margin-bottom: 15px;"><%= error %></p>
            <% } %>
            <% String mensaje = (String) request.getAttribute("mensaje"); 
               if (mensaje != null) { %>
                <p style="color: green; margin-bottom: 15px;"><%= mensaje %></p>
            <% } %>

            <div class="botonesFormulario">
                <button type="button" class="boton-cancelar" 
                        onclick="window.location.href='editarPerfil.jsp'">Cancelar</button>
                <button type="submit" class="boton-aceptar">Aceptar</button>
            </div>
        </form>
    </div>
</div>

<%@ include file="plantillaFin.jsp" %>