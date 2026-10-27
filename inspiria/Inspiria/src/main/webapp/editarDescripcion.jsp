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
    
    String descActual = (usuarioLogueado.getDescripcion() != null) ? usuarioLogueado.getDescripcion() : "";
%>

<h2 class="tituloSeccion">Configuración | Descripción</h2>

<div class="contenedorFormularioContraseña">
    <div class="tarjetaFormulario">
        <form action="EditarDescripcionServlet" method="post">
            
            <div class="campo">
                <label for="nuevaDescripcion">Tu biografía</label>
                <textarea id="nuevaDescripcion" 
                          name="nuevaDescripcion" 
                          class="campo-biografia"
                          placeholder="Escribe algo sobre ti..."
                          maxlength="255"><%= descActual %></textarea>
                <small class="texto-aclaracion">Máximo 255 caracteres.</small>
            </div>

            <%-- Mensajes de estado --%>
            <% String error = (String) request.getAttribute("error"); 
               if (error != null) { %>
                <p class="error"><%= error %></p>
            <% } %>
            
            <% String mensaje = (String) request.getAttribute("mensaje"); 
               if (mensaje != null) { %>
                <p class="mensaje-exito"><%= mensaje %></p>
            <% } %>

            <div class="botonesFormulario">
                <button type="button" class="boton-cancelar" 
                        onclick="window.location.href='editarPerfil.jsp'">Cancelar</button>
                <button type="submit" class="boton-aceptar">Guardar</button>
            </div>
        </form>
    </div>
</div>

<%@ include file="plantillaFin.jsp" %>