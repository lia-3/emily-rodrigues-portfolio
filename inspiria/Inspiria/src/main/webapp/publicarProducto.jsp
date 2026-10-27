<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="uva.ssw.database.ControladorDAOS" %>
<%@ page import="java.util.ArrayList" %>
<% request.setAttribute("paginaActiva", "publicar"); %>
<%@ include file="plantilla.jsp" %>  

<form method="post" action="PublicarProductoServlet" enctype="multipart/form-data" onsubmit="return validarProducto()">
    <div class="contenedorFormularioProducto">
        <div class="seccionFotos">
            <div class="subirFotos" onclick="document.getElementById('inputFotos').click()">Añadir Foto</div>
            <div class="subidasFotos">
                <input type="file" id="inputFotos" name="fotosProducto" accept="image/*" style="display:none" onchange="mostrarFotos(event)">
                <label>Seleccionada <span>*</span></label>
                <div class="thumbnailScrollTutorial" id="contenedorFotos">
                </div>
            </div>
        </div>
        
        <div class="clasificacion">
            <div class="escribirNombre">
                <label for="idNombre">Nombre <span>*</span></label>
                <textarea id="idNombre" name="nombre"></textarea>
            </div>
            <div class="escribirDescripcion">
                <label for="idDescripcion">Descripción <span>*</span></label>
                <textarea id="idDescripcion" name="descripcion" ></textarea>
            </div>

            <div class="tematica_categoria">
                <label for="idCategoria">Categoría <span>*</span></label>
                <select id="idCategoria" name="categoria">
                    <% 
                        ControladorDAOS controlador = new ControladorDAOS();
                        ArrayList<String> listaCat = controlador.listarTodosLosNombresDeCategorias();
                        if (listaCat != null) {
                            for (String cat : listaCat) {
                    %>
                        <option value="<%= cat %>"><%= cat %></option>
                    <% 
                            }
                        } 
                    %>
                </select>
            </div>

            <div class="tematica_categoria">
                <label for="idTematica">Temática <span>*</span></label>
                <select id="idTematica" name="tematica">
                    <% 
                        ArrayList<String> listaTem = controlador.listarTodosLosNombresDeTematicas();
                        if (listaTem != null) {
                            for (String tem : listaTem) {
                    %>
                        <option value="<%= tem %>"><%= tem %></option>
                    <% 
                            }
                        } 
                    %>
                </select>
            </div>
            <div class="precio_stock">
                <label for="idPrecio">Precio (€) <span>*</span></label>
                <input type="number" placeholder="Introduce el precio del producto" id="idPrecio" name="precio">
            </div>
            <div class="precio_stock">
                <label for="idStock">Stock <span>*</span></label>
                <select id="idStock" name="stock">
                    <option>Disponible</option>
                    <option>No disponible</option>
                </select>
            </div>
            <div class="accionesPublicar">
                <input type="reset" class="btnCancelar" value="Cancelar" onclick="location.href='publicar.jsp'">
                <input type="submit" class="btnPublicar" value="Publicar">
            </div>
        </div>
    </div>
</form>

<script>
    function mostrarFotos(event){
        var contenedor = document.getElementById("contenedorFotos");
        // Limpiamos el contenedor para borrar cualquier selección previa
        contenedor.innerHTML = ""; 
        
        var fotos = event.target.files;
        if(fotos.length > 0){
            var div = document.createElement("div");
            div.className = "thumbnail";
            var foto = document.createElement("img");
            // Accedemos únicamente a la primera y única imagen
            foto.src = URL.createObjectURL(fotos[0]);
            foto.className = "imagen";
            div.appendChild(foto);
            contenedor.appendChild(div);
        }
    }
    
    function validarProducto(){
        var nombre = document.getElementById("idNombre").value.trim();
        var descripcion = document.getElementById("idDescripcion").value.trim();
        var precio = document.getElementById("idPrecio").value.trim();     
        if (nombre === "" || descripcion === "" || precio === "") {
            alert("Es obligatorio poner nombre, descripcion y precio al producto");
            return false;
        }
        return true;  
    }
</script>
<%@ include file="plantillaFin.jsp" %>