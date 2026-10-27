<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<% request.setAttribute("paginaActiva", "publicar"); %>
<%@ include file="plantilla.jsp" %>  

<form method="post" action="PublicarTutorial2Servlet" enctype="multipart/form-data" onsubmit="return validarPasos()">
    
    <div class="escribirMaterialesHerramientas">
        <div class="bloque-tutorial">
            <label>Materiales</label>
            <div id="contenedorMateriales">
                <div class="material">
                    <input type="text" name="nombreMaterial1" placeholder="Nombre del material">
                    <input type="text" name="linkMaterial1" placeholder="Link del material">
                </div>
            </div>
            <button type="button" onclick="addMaterial()"> + Añadir material </button>
        </div>     

        <br>
        <div class="bloque-tutorial">
            <label>Herramientas</label>
            <div id="contenedorHerramientas">
                <div class="herramienta">
                    <input type="text" name="nombreHerramienta1" placeholder="Nombre de la herramienta">
                    <input type="text" name="linkHerramienta1" placeholder="Link de la herramienta">
                </div>
            </div>
            <button type="button" onclick="addHerramienta()">+ Añadir herramienta</button>
        </div>
    </div>
        
    <div class="seccionPasos">
        <label>Pasos (Mínimo 5)</label>
        <div id="contenedorPasos">
            <% for(int i=1; i<=5; i++) { %>
                <div class="paso" id="paso<%=i%>">  
                    <label>Paso <%=i%>*</label>
                    <textarea name="descripcionPaso<%=i%>" placeholder="Descripcion del paso <%=i%>"></textarea>
                    <input type="file" name="fotoPaso<%=i%>" accept="image/*">
                </div>
            <% } %>
        </div>
        <button type="button" onclick="addPaso()">+ Añadir paso adicional</button>
    </div>
    
    <div class="dificultad">
        <label for="idDificultad">Dificultad <span>*</span></label>
        <select id="idDificultad" name="dificultad">
            <option value="PRINCIPIANTE">Principiante</option>
            <option value="INTERMEDIO">Intermedio</option>
            <option value="AVANZADO">Avanzado</option>
        </select>
    </div>

    <div class="grupoElegirProducto">
        <p>¿Quieres publicarlo también como producto? (Usará la portada del tutorial)</p>
        <input type="radio" id="radioButtonNoProducto" name="elegirProducto" value="no">
        <label for="radioButtonNoProducto">No</label>
        <input type="radio" id="radioButtonSiProducto" name="elegirProducto" value="si" checked>
        <label for="radioButtonSiProducto">Si</label>
    </div>

    <div class="precio_stock" id="divPrecio">
        <label for="idPrecio">Precio (€) <span>*</span></label>
        <input type="number" step="0.01" placeholder="20.00" id="idPrecio" name="precio">
    </div>

    <div class="precio_stock" id="divStock">
        <label for="idStock">Stock <span>*</span></label>
        <select id="idStock" name="stock">
            <option value="DISPONIBLE">Disponible</option>
        </select>
    </div>

    <div class="accionesPublicar">
        <input type="button" class="btnCancelar" value="Cancelar" onclick="location.href='PrincipalServlet'">
        <input type="submit" class="btnPublicar" value="Publicar Tutorial">
    </div>
    
    <% if(request.getAttribute("error") != null) { %>
        <p style="color:red;"><%= request.getAttribute("error") %></p>
    <% } %>
</form>

<script>
    // Control de visibilidad de campos de producto
    const radioSi = document.getElementById('radioButtonSiProducto');
    const radioNo = document.getElementById('radioButtonNoProducto');
    const inputPrecio = document.getElementById('idPrecio');
    const selectStock = document.getElementById('idStock');

    function actualizarCampos() {
        const esNo = radioNo.checked;
        inputPrecio.disabled = esNo;
        selectStock.disabled = esNo;
        document.getElementById('divPrecio').style.opacity = esNo ? "0.5" : "1";
        document.getElementById('divStock').style.opacity = esNo ? "0.5" : "1";
    }

    radioSi.addEventListener('change', actualizarCampos);
    radioNo.addEventListener('change', actualizarCampos);

    // Validación de pasos
    function validarPasos(){
        var textos = document.querySelectorAll("#contenedorPasos textarea");
        var rellenos = 0;
        textos.forEach(t => { if(t.value.trim() !== "") rellenos++; });

        if(rellenos < 5){
            alert("Es necesario escribir al menos 5 pasos para crear un tutorial.");
            return false;
        }
        return true;
    }

    // Funciones dinámicas (Add Material/Herramienta/Paso)
    function addMaterial(){
        var container = document.getElementById("contenedorMateriales");
        var index = container.children.length + 1;
        var div = document.createElement("div");
        div.className = "material";
        div.innerHTML = '<input type="text" name="nombreMaterial'+index+'" placeholder="Nombre"> ' +
                        '<input type="text" name="linkMaterial'+index+'" placeholder="Link"> ' +
                        '<button type="button" onclick="this.parentElement.remove()">X</button>';
        container.appendChild(div);
    }

    function addHerramienta(){
        var container = document.getElementById("contenedorHerramientas");
        var index = container.children.length + 1;
        var div = document.createElement("div");
        div.className = "herramienta";
        div.innerHTML = '<input type="text" name="nombreHerramienta'+index+'" placeholder="Nombre"> ' +
                        '<input type="text" name="linkHerramienta'+index+'" placeholder="Link"> ' +
                        '<button type="button" onclick="this.parentElement.remove()">X</button>';
        container.appendChild(div);
    }

    function addPaso(){
        var container = document.getElementById("contenedorPasos");
        var index = container.children.length + 1;
        var div = document.createElement("div");
        div.className = "paso";
        div.innerHTML = '<label>Paso '+index+'</label>' +
                        '<textarea name="descripcionPaso'+index+'" placeholder="Descripción"></textarea>' +
                        '<input type="file" name="fotoPaso'+index+'" accept="image/*">' +
                        '<button type="button" onclick="this.parentElement.remove()">Eliminar Paso</button>';
        container.appendChild(div);
    }

    window.onload = actualizarCampos;
</script>

<%@ include file="plantillaFin.jsp" %>