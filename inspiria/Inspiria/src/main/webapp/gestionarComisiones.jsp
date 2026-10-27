<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="uva.ssw.modelo.*, java.util.ArrayList" %>
<%@ include file="plantilla.jsp" %>

<%
    Usuario user = (Usuario) session.getAttribute("usuario"); 
    ArrayList<Comision> listaComisiones = (ArrayList<Comision>) request.getAttribute("listaComisiones");
    if(listaComisiones == null) listaComisiones = new ArrayList<>(); 
%>

<h1 class="tituloConfiguracion">Gestionar Comisiones</h1>

<div class="contenedorFormularioCentrado">
    <form action="GestionarComisionesServlet" method="POST" id="formComisiones" onsubmit="return validarFormulario()">
        
        <div class="grupoElegirProducto">
            <p>¿Deseas aceptar encargos/comisiones? <span>*</span></p>
            <input type="radio" name="aceptaComisiones" value="true" id="si" 
                   <%= (user.isAceptaComisiones()) ? "checked" : "" %> onclick="toggleComisiones(true)"> 
            <label for="si">Sí</label>
            
            <input type="radio" name="aceptaComisiones" value="false" id="no" 
                   <%= (!user.isAceptaComisiones()) ? "checked" : "" %> onclick="toggleComisiones(false)"> 
            <label for="no">No</label>
        </div>

        <div id="seccionEditorComisiones" style="<%= user.isAceptaComisiones() ? "display:block;" : "display:none;" %>"> 
            <hr style="margin: 20px 0; border: 0; border-top: 1px solid #eee;">
            <h3>Tus servicios</h3>
            <p class="texto-aclaracion">Define los servicios que ofreces y su precio base.</p>
            
            <div id="listaInputsComisiones"></div>

            <script>
                function crearFila(descValue = "", precioValue = "") {
                    const div = document.createElement('div');
                    div.className = 'grupoFormulario fila-comision'; // Clase añadida para validar
                    div.style = 'border-bottom: 1px solid #f0f0f0; padding-bottom: 10px; margin-top: 10px; position: relative; overflow: hidden;';

                    div.innerHTML = `
                        <div style="width: calc(100% - 40px); float: left;">
                            <input type="text" name="descComision" class="inputFormulario" 
                                   placeholder="Ej: Retrato en acuarela" style="margin-bottom:5px;">
                            <input type="number" step="0.01" min="0" name="precioComision" 
                                   class="inputFormulario" placeholder="Precio Base (€)">
                            <div class="error" style="display:none; text-align:left;">Debe completar descripción y precio</div>
                        </div>
                        <button type="button" class="btnEliminarFila" onclick="eliminarFila(this)" 
                                style="float: right; background: #ff4d4d; color: white; border: none; border-radius: 4px; width: 30px; height: 30px; cursor: pointer; font-weight: bold;">
                            X
                        </button>
                        <div style="clear: both;"></div>
                    `;

                    div.querySelector('[name="descComision"]').value = descValue;
                    div.querySelector('[name="precioComision"]').value = precioValue;

                    document.getElementById('listaInputsComisiones').appendChild(div); 
                }

                const comisionesExistentes = [
                    <% for (int i = 0; i < listaComisiones.size(); i++) {
                        Comision com = listaComisiones.get(i);%> 
                    { desc: "<%= com.getDescripcion()%>", precio: "<%= com.getPrecioBase()%>" }<%= i < listaComisiones.size() - 1 ? "," : ""%> 
                    <% }%>
                ];

                comisionesExistentes.forEach(function (c) {
                    crearFila(c.desc, c.precio);
                });

                function eliminarFila(btn) {
                    const fila = btn.closest('.grupoFormulario');
                    if (fila) fila.remove();
                }

                function validarFormulario() {
                    const acepta = document.querySelector('input[name="aceptaComisiones"]:checked').value === 'true';
                    if (!acepta) return true;

                    const filas = document.querySelectorAll('.fila-comision');
                    let valido = true;

                    filas.forEach(fila => {
                        const desc = fila.querySelector('[name="descComision"]').value.trim();
                        const precio = fila.querySelector('[name="precioComision"]').value.trim();
                        const errorMsg = fila.querySelector('.error');

                        if (desc === "" || precio === "") {
                            errorMsg.style.display = 'block';
                            valido = false;
                        } else {
                            errorMsg.style.display = 'none';
                        }
                    });

                    return valido;
                }
            </script>
            
            <button type="button" class="botonGris" onclick="crearFila()" style="width: 100%; margin-top: 10px;">+ Añadir Servicio</button>
        </div>

        <div class="contenedorBotones">
            <button type="button" class="botonSecundario" onclick="window.location.href='editarPerfil.jsp'">Cancelar</button>
            <button type="submit" class="botonPrincipal">Guardar Cambios</button>
        </div>
    </form>
</div>

<script>
    function toggleComisiones(show) {
        document.getElementById('seccionEditorComisiones').style.display = show ? 'block' : 'none'; 
    }

    document.addEventListener('keypress', function (e) {
        if (e.target.name === 'precioComision' && e.key === '-') {
            e.preventDefault();
        }
    });
</script>

<%@ include file="plantillaFin.jsp" %>