<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList" %>

<%
    // Si entras por el JSP directamente, no habrá lista de categorías.
    // En ese caso, redirigimos al Servlet para que las cargue.
    if (request.getAttribute("listaCategorias") == null) {
        response.sendRedirect("PublicarTutorial1Servlet");
        return; 
    }
%>

<% request.setAttribute("paginaActiva", "publicar"); %>
<%@ include file="plantilla.jsp" %>

<form method="post" action="PublicarTutorial1Servlet" enctype="multipart/form-data" onsubmit="return validar()">
    <div class="contenedorFormulario">
        
        <div class="seccionFotos">
            <label>Portada del Tutorial <span>*</span></label>

            <div class="subirFotos" onclick="document.getElementById('inputPortada').click()" 
                 style="background-color: #f0f0f0; border: 2px dashed #ccc; margin-bottom: 5px; cursor:pointer; text-align:center; padding:20px;">
                Seleccionar Imagen de Portada
            </div>

            <p class="texto-aclaracion">Debe ser una foto del resultado final del tutorial. Esta será la foto del producto en caso de querer vender el resultado final.</p>

            <input type="file" id="inputPortada" name="portada" accept="image/*" style="display:none" onchange="mostrarPortada(event)">

            <div id="vistaPreviaPortada" style="margin-top: 15px; margin-bottom: 20px; text-align: center;">
                </div>
        </div>

        <div class="clasificacion">
            <div class="escribirNombre">
                <label for="idNombre">Nombre <span>*</span></label>
                <textarea id="idNombre" name="nombre"></textarea>
            </div>
            
            <div class="escribirDescripcion">
                <label for="idDescripcion">Descripción <span>*</span></label>
                <textarea id="idDescripcion" name="descripcion"></textarea>
            </div>

            <div class="tematica_categoria">
                <label for="idCategoria">Categoría <span>*</span></label>
                <select id="idCategoria" name="categoria">
                    <% 
                        ArrayList<String> listaCat = (ArrayList<String>) request.getAttribute("listaCategorias");
                        if(listaCat != null) {
                            for (String cat : listaCat) {
                    %>
                        <option value="<%= cat %>"><%= cat %></option>
                    <%      } 
                        } 
                    %>
                </select>
            </div>

            <div class="tematica_categoria">
                <label for="idTematica">Temática <span>*</span></label>
                <select id="idTematica" name="tematica">
                    <% 
                        ArrayList<String> listaTem = (ArrayList<String>) request.getAttribute("listaTematicas");
                        if(listaTem != null) {
                            for (String tem : listaTem) {
                    %>
                        <option value="<%= tem %>"><%= tem %></option>
                    <%      } 
                        } 
                    %>
                </select>
            </div>

            <div class="accionesPublicar">
                <input type="reset" class="btnCancelar" value="Cancelar" onclick="location.href='publicar.jsp'">
                <input type="submit" class="btnSiguiente" value="Siguiente">
            </div>

            <% String error = (String) request.getAttribute("error"); 
               if(error != null) { %>
                <p class="error" style="color:red;"><%= error %></p>
            <% } %>
        </div>
    </div>
</form>

<script>
    // Previsualización de la imagen
    function mostrarPortada(event) {
        var contenedor = document.getElementById("vistaPreviaPortada");
        contenedor.innerHTML = ""; 
        var archivo = event.target.files[0];
        if (archivo) {
            var foto = document.createElement("img");
            foto.src = URL.createObjectURL(archivo);
            foto.style.width = "200px";
            foto.style.height = "150px";
            foto.style.objectFit = "cover";
            foto.style.borderRadius = "8px";
            foto.style.border = "2px solid #333";
            contenedor.appendChild(foto);
        }
    }

    // Validación antes de enviar
    function validar(){
        var nombre = document.getElementById("idNombre").value.trim();
        var descripcion = document.getElementById("idDescripcion").value.trim();
        var portada = document.getElementById("inputPortada").files.length;

        if (nombre === "" || descripcion === "") {
            alert("Es necesario poner nombre y descripción al tutorial");
            return false;
        }
        if (portada === 0) {
            alert("Debes seleccionar una imagen de portada");
            return false;
        }
        return true;
    }
</script>

<%@ include file="plantillaFin.jsp" %>