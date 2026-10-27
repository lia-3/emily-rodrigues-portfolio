<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="uva.ssw.modelo.*, java.util.ArrayList" %>
<%@ include file="plantilla.jsp" %>

<%
    ArrayList<Contacto> listaContactos = (ArrayList<Contacto>) request.getAttribute("listaContactos");
    if(listaContactos == null) listaContactos = new ArrayList<>();
%>

<h1 class="tituloConfiguracion">Gestionar Contactos y Redes</h1>

<div class="contenedorFormularioCentrado">
    <form action="GestionarEnlacesContactosServlet" method="POST" id="formContactos" onsubmit="return validarFormulario()">
        
        <h3>Tus enlaces externos</h3>
        <p class="texto-aclaracion">Añade enlaces a tus redes sociales, portfolio o tienda externa.</p>
        
        <div id="listaInputsContactos"></div>
        
        <button type="button" class="botonGris" onclick="crearFilaContacto()" style="width: 100%; margin-top: 10px;">
            + Añadir Enlace
        </button>

        <div class="contenedorBotones">
            <button type="button" class="botonSecundario" onclick="window.location.href='editarPerfil.jsp'">Cancelar</button>
            <button type="submit" class="botonPrincipal">Guardar Cambios</button>
        </div>
    </form>
</div>

<script>
    function crearFilaContacto(platValue = "", linkValue = "") {
        const div = document.createElement('div');
        div.className = 'grupoFormulario fila-contacto';
        div.style = 'border-bottom: 1px solid #f0f0f0; padding-bottom: 10px; margin-top: 10px; position: relative; overflow: hidden;';

        div.innerHTML = `
            <div style="width: calc(100% - 40px); float: left;">
                <input type="text" name="plataforma" class="inputFormulario" 
                       placeholder="Plataforma (Ej: Instagram, Portfolio)" style="margin-bottom:5px;">
                <input type="url" name="link" class="inputFormulario" 
                       placeholder="https://ejemplo.com/tuperfil">
                <div class="error" style="display:none; text-align:left;">Ambos campos son obligatorios</div>
            </div>
            <button type="button" onclick="this.parentElement.remove()" 
                    style="float: right; background: #ff4d4d; color: white; border: none; border-radius: 4px; width: 30px; height: 30px; cursor: pointer;">
                X
            </button>
            <div style="clear: both;"></div>
        `;

        div.querySelector('[name="plataforma"]').value = platValue;
        div.querySelector('[name="link"]').value = linkValue;
        document.getElementById('listaInputsContactos').appendChild(div);
    }

    // Cargar datos existentes
    <% for (Contacto c : listaContactos) { %>
        crearFilaContacto("<%= c.getPlataforma() %>", "<%= c.getLink() %>");
    <% } %>

    function validarFormulario() {
        const filas = document.querySelectorAll('.fila-contacto');
        let valido = true;
        filas.forEach(fila => {
            const plat = fila.querySelector('[name="plataforma"]').value.trim();
            const link = fila.querySelector('[name="link"]').value.trim();
            const errorMsg = fila.querySelector('.error');

            if (plat === "" || link === "") {
                errorMsg.style.display = 'block';
                valido = false;
            } else {
                errorMsg.style.display = 'none';
            }
        });
        return valido;
    }
</script>

<%@ include file="plantillaFin.jsp" %>