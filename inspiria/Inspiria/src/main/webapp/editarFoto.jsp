<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="uva.ssw.modelo.Usuario" %>
<%
    Usuario usuarioLogueado = (Usuario) session.getAttribute("usuario");
    if (usuarioLogueado == null) {
        response.sendRedirect("iniciarSesion.jsp");
        return;
    }
%>

<%@ include file="plantilla.jsp" %>

<h1 class="tituloSeccion">Configuración | Foto de Perfil</h1>

<div class="flexContenedor">
    
    <!-- Columna Izquierda: Foto Actual -->
    <div class="columna">
        <h2 class="subtitulo">Foto Actual</h2>
        <div class="cuadroFoto">
            <% if (usuarioLogueado.getFotoBase64() != null && !usuarioLogueado.getFotoBase64().isEmpty()) { %>
                <img src="data:image/jpeg;base64,<%= usuarioLogueado.getFotoBase64() %>" class="fotoPerfilReal" alt="Foto actual">
            <% } else { %>
                <img src="img/usuario.png" class="fotoPerfilReal" alt="Foto por defecto">
            <% } %>
        </div>

        <form action="EditarFotoServlet" method="POST" class="campo">
            <input type="hidden" name="accion" value="eliminar">
            <button type="submit" class="botonGris" onclick="return confirm('¿Deseas eliminar tu foto?')">
                Eliminar Foto
            </button>
        </form>
    </div>

    <!-- Columna Derecha: Selección y Previsualización -->
    <div class="columna">
        <h2 class="subtitulo">Actualización Foto de Perfil</h2>
        
        <form action="EditarFotoServlet" method="POST" enctype="multipart/form-data" onsubmit="return validarEnvio()">
            <div class="grupoActualizar">
                <label for="nuevaFoto" class="cuadroFoto discontinuo">
                    Haz clic aquí para seleccionar una nueva foto
                </label>
                <input type="file" id="nuevaFoto" name="nuevaFoto" accept="image/*" class="inputFormulario" style="display:none;" onchange="previsualizarFoto(event)">
            </div>
            
            <div id="vistaPreviaContenedor" class="cuadroFoto">
                <p id="textoSinFoto" class="textoEstado">No hay selección</p>
            </div>
            
            <div class="contenedorBotonesAccion">
                <button type="submit" class="botonGris">Actualizar Foto</button>
                <a href="editarPerfil.jsp" class="botonGris enlaceBoton">Cancelar</a>
            </div>
        </form>
    </div>
</div>

<script>
    function previsualizarFoto(event) {
        var contenedor = document.getElementById("vistaPreviaContenedor");
        var archivo = event.target.files[0];
        if (archivo) {
            contenedor.innerHTML = ""; 
            var img = document.createElement("img");
            img.src = URL.createObjectURL(archivo);
            img.className = "imgPrevia"; // Usa la clase de estilos.css
            contenedor.appendChild(img);
        }
    }

    function validarEnvio() {
        if (document.getElementById("nuevaFoto").files.length === 0) {
            alert("Selecciona una imagen primero.");
            return false;
        }
        return true;
    }
</script>

<%@ include file="plantillaFin.jsp" %>