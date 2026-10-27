<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="uva.ssw.modelo.*, java.util.ArrayList, java.util.Base64" %>
<%@ include file="plantilla.jsp" %>     

<%
    // 1. LÓGICA DE SELECCIÓN DE DATOS
    Usuario usuarioLogueado = (Usuario) session.getAttribute("usuario");
    Usuario perfilAMostrar = (Usuario) request.getAttribute("perfilBuscado");

    // Si el Servlet no envió un perfil específico, usamos el de la sesión
    if (perfilAMostrar == null) {
        perfilAMostrar = usuarioLogueado;
    }

    // 2. SEGURIDAD
    if (perfilAMostrar == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    // 3. VARIABLES DINÁMICAS (Usamos perfilAMostrar en lugar de usuarioLogueado)
    String nombre = (perfilAMostrar.getNombre() != null) ? perfilAMostrar.getNombre() : "Usuario";
    String descripcion = (perfilAMostrar.getDescripcion() != null && !perfilAMostrar.getDescripcion().isEmpty()) 
                         ? perfilAMostrar.getDescripcion() : "Creador de contenido";
    boolean aceptaComisiones = perfilAMostrar.isAceptaComisiones();
%>

<div class="infoPerfil">
    <%-- Foto de perfil --%>
    <% if (perfilAMostrar.getFotoBase64() != null && !perfilAMostrar.getFotoBase64().isEmpty()) { %>
        <img src="data:image/jpeg;base64,<%= perfilAMostrar.getFotoBase64() %>" class="avatarGrande">
    <% } else { %>
        <img src="img/usuario.png" class="avatarGrande">
    <% } %>

    <div class="datosUsuario">
        <h1><%= nombre %></h1>
        <p><%= descripcion %></p>
    </div>

    <div class="botonesAccion">
        <% if (usuarioLogueado != null && perfilAMostrar.getIdentificador() != usuarioLogueado.getIdentificador()) { 
               boolean loSigue = uva.ssw.database.ControladorDAOS.esPerfilSeguido(usuarioLogueado.getIdentificador(), perfilAMostrar.getIdentificador());
        %>
            <img src='img/<%= loSigue ? "corazon_relleno.png" : "corazon_vacio.png" %>' 
                 alt="Seguir" title="<%= loSigue ? "Dejar de seguir" : "Seguir" %>"
                 style="width: 35px; cursor: pointer; vertical-align: middle; margin-right: 10px;"
                 onclick="location.href='SeguirPerfilServlet?id=<%= perfilAMostrar.getIdentificador() %>'">
        <% } %>

        <%-- NUEVO: Botón Ver Lista Comisiones --%>
        <% if (aceptaComisiones && perfilAMostrar.getListaComisiones() != null && !perfilAMostrar.getListaComisiones().isEmpty()) { %>
            <button class="botonesGris" onclick="abrirPopupComisiones()">Ver Lista Comisiones</button>
        <% } %>

        <%-- SOLUCIÓN: Añadir este bloque para el Botón de Contactos --%>
        <% if (perfilAMostrar.getContactos() != null && !perfilAMostrar.getContactos().isEmpty()) { %>
            <button class="botonesGris" onclick="abrirPopupContactos()">Ver Contactos</button>
        <% } %>

        <% if (aceptaComisiones) { %>
            <p class="etiqueta-comisiones">Acepta Comisiones</p>
        <% } %>
    </div>
</div>

<%-- Pestañas de navegación --%>
<div class="pestanas">
    <button class="ventana activo" onclick="abrirPestana(event, 'tutoriales')">Publicaciones</button>
    <button class="ventana" onclick="abrirPestana(event, 'productos')">En venta</button>
</div>

<%-- Contenedor de Tutoriales (Publicaciones) --%>
<div id="tutoriales" class="contenido-pestana">
    <%-- Usamos muroCuadricula para que herede el estilo de 3 columnas del muro --%>
    <div class="muroCuadricula">
       
        <% 
            ArrayList<Tutorial> tutoriales = perfilAMostrar.getTutorialesPublicados();
            if (tutoriales != null && !tutoriales.isEmpty()) {
                for (Tutorial t : tutoriales) {
                    String imgHtml = "";
                    if (t.getFotoBase64() != null && !t.getFotoBase64().isEmpty()) {
                        imgHtml = "data:image/jpeg;base64," + t.getFotoBase64();
                    }
        %>
        <a href="DetalleTutorialServlet?id=<%= t.getId() %>" style="text-decoration: none; color: inherit;">
            <div class="post">
                <%-- Cabecera: Nombre y foto del autor --%>
                <div class="postCabecera">
                    <span><%= perfilAMostrar.getNombre() %></span>
                    <% if (perfilAMostrar.getFotoBase64() != null && !perfilAMostrar.getFotoBase64().isEmpty()) { %>
                        <img src="data:image/jpeg;base64,<%= perfilAMostrar.getFotoBase64() %>" class="miniPerfil">
                    <% } else { %>
                        <img src="img/usuario.png" class="miniPerfil">
                    <% } %>
                </div>

                <%-- Media: Imagen del tutorial --%>
                <div class="postMedia">
                    <% if (!imgHtml.isEmpty()) { %>
                        <img src="<%= imgHtml %>" alt="Post">
                    <% } else { %>
                        <img src="img/icono_tutorial.jpeg" alt="Sin foto">
                    <% } %>
                </div>

                <%-- Información del Post --%>
                <div class="postInfoMuro">
                    <div class="infoTop">
                        <h3 class="postTitulo"><%= t.getTitulo() %></h3>
                        <div class="postAcciones">
                            <% 
                                boolean estaGuardado = false;
                                if (usuarioLogueado != null) {
                                    // Verifica si el usuario actual tiene guardado este tutorial
                                    estaGuardado = uva.ssw.database.ControladorDAOS.esTutorialGuardado(usuarioLogueado.getIdentificador(), t.getId());
                                }
                            %>
                            <img src='img/<%= estaGuardado ? "btn_guardar_relleno.png" : "btn_guardar_vacio.png" %>' 
                                 class="iconoAccion" 
                                 alt="Guardar" 
                                 title="Guardar tutorial"
                                 onclick="window.location.href='GuardarTutorialServlet?id=<%= t.getId() %>'; return false;">

                            <img src="img/comentario.png" 
                                 class="iconoAccion" 
                                 alt="Comentar" 
                                 title="Ir a comentarios"
                                 onclick="window.location.href='DetalleTutorialServlet?id=<%= t.getId() %>#seccionComentarios'; return false;">
                        </div>
                    </div> <%-- CIERRE DE infoTop --%>

                    <%-- Categoría, Temática y Descripción --%>
                    <p class="postInfo">
                        <%= (t.getCategoria() != null) ? t.getCategoria().getNombre() : "" %><br>
                        <%= (t.getTematica() != null) ? t.getTematica().getNombre() : "" %>
                    </p>
                    <p class="postDesc"><%= t.getDescripcion() %></p>
                </div> <%-- CIERRE DE postInfoMuro --%>
            </div> <%-- CIERRE DE post --%>
        </a>
        <% 
                }
            } else { %>
                <p class="texto-vacio">No hay publicaciones disponibles.</p>
        <% } %>
    </div>
</div>

<%-- Contenedor de Productos --%>
<div id="productos" class="contenido-pestana" style="display:none;">
    <div class="mallaPublicaciones">
        <% 
            ArrayList<Producto> productos = perfilAMostrar.getProductosEnVenta();
            if (productos != null && !productos.isEmpty()) {
                for (Producto p : productos) {
                    String imgHtml = "";
                    if (p.getImagen() != null) {
                        String b64 = Base64.getEncoder().encodeToString(p.getImagen());
                        imgHtml = "data:image/jpeg;base64," + b64;
                    }
        %>
        <a href="DetalleProductoServlet?id=<%= p.getId() %>" style="text-decoration: none; color: inherit;">
            <div class="post">
                <div class="postImagen">
                    <% if (!imgHtml.isEmpty()) { %>
                        <img src="<%= imgHtml %>">
                    <% } %>
                </div>
                <div class="postInfo">
                    <h3><%= p.getTitulo() %></h3>
                    <p>Precio: <%= p.getPrecio() %>€<br>Stock: <%= p.getStock() %></p>
                </div>
            </div>
        </a>
                
            
        <% 
                }
            } else { %>
                <p class="texto-vacio">No hay productos en venta.</p>
        <% } %>
    </div>
</div>

<%-- SECCIÓN DEL POP-UP DE CONTACTOS --%>
<div id="fondoOscuroContactos" class="fondoOscuro" style="display:none" onclick="cerrarPopupContactos()"></div>

<div id="popupContactos" class="popupIdentificacion" style="display:none; min-width: 320px; padding: 20px;">
    <h3 style="margin-bottom: 10px;">Enlaces de contacto</h3>
    <hr style="border: 0; border-top: 1px solid #eee; margin-bottom: 15px;">
    
    <div id="listaContactosCuerpo" style="text-align: left; max-height: 300px; overflow-y: auto;">
        <% 
            ArrayList<Contacto> lista = perfilAMostrar.getContactos();
            if (lista != null && !lista.isEmpty()) { 
                for (Contacto c : lista) { 
        %>
            <div style="margin-bottom: 15px; border-bottom: 1px solid #fafafa; padding-bottom: 5px;">
                <span style="display: block; font-size: 0.75em; color: #888; text-transform: uppercase; font-weight: bold;">
                    <%= c.getPlataforma() %>
                </span>
                <a href="<%= c.getLink() %>" target="_blank" style="color: #007bff; text-decoration: none; word-break: break-all; font-size: 0.95em;">
                    <%= c.getLink() %>
                </a>
            </div>
        <% 
                } 
            } else { 
        %>
            <p style="color: #999; text-align: center; padding: 20px 0;">Este usuario no ha añadido enlaces de contacto.</p>
        <% } %>
    </div>

    <button onclick="cerrarPopupContactos()" class="botonGris" style="width: 100%; margin-top: 10px; cursor: pointer;">Cerrar</button>
</div>
    
<%-- SECCIÓN DEL POP-UP DE COMISIONES --%>
<div id="fondoOscuroComisiones" class="fondoOscuro" style="display:none" onclick="cerrarPopupComisiones()"></div>

<div id="popupComisiones" class="popupIdentificacion" style="display:none; min-width: 350px; padding: 20px;">
    <h3 style="margin-bottom: 10px;">Tarifas de Comisiones</h3>
    <hr style="border: 0; border-top: 1px solid #eee; margin-bottom: 15px;">
    
    <div id="listaComisionesCuerpo" style="text-align: left; max-height: 350px; overflow-y: auto;">
        <% 
            ArrayList<Comision> comisiones = perfilAMostrar.getListaComisiones();
            if (comisiones != null && !comisiones.isEmpty()) { 
                for (Comision com : comisiones) { 
        %>
            <div style="margin-bottom: 15px; border-bottom: 1px solid #f5f5f5; padding-bottom: 8px;">
                <div style="display: flex; justify-content: space-between; align-items: baseline;">
                    <strong style="font-size: 1.05em; color: #333;"><%= com.getDescripcion() %></strong>
                    <span style="color: #28a745; font-weight: bold; background: #e8f5e9; padding: 2px 8px; border-radius: 4px;">
                        <%= com.getPrecioBase() %>€
                    </span>
                </div>
            </div>
        <% 
                } 
            } else { 
        %>
            <p style="color: #999; text-align: center; padding: 20px 0;">No hay tarifas configuradas.</p>
        <% } %>
    </div>

    <button onclick="cerrarPopupComisiones()" class="botonGris" style="width: 100%; margin-top: 15px; cursor: pointer;">Cerrar</button>
</div>

<script>
    // Gestión de Pestañas
    function abrirPestana(evt, nombrePestana) {
        var i, contenido, enlaces;
        contenido = document.getElementsByClassName("contenido-pestana");
        for (i = 0; i < contenido.length; i++) {
            contenido[i].style.display = "none";
        }
        enlaces = document.getElementsByClassName("ventana");
        for (i = 0; i < enlaces.length; i++) {
            enlaces[i].className = enlaces[i].className.replace(" activo", "");
        }
        document.getElementById(nombrePestana).style.display = "block";
        evt.currentTarget.className += " activo";
    }

    // Lógica del Pop-up de Contactos
    function abrirPopupContactos() {
        document.getElementById("popupContactos").style.display = "block";
        document.getElementById("fondoOscuroContactos").style.display = "block";
    }

    function cerrarPopupContactos() {
        document.getElementById("popupContactos").style.display = "none";
        document.getElementById("fondoOscuroContactos").style.display = "none";
    }

    // Cerrar con la tecla Escape
    document.addEventListener('keydown', function(event) {
        if (event.key === "Escape") {
            cerrarPopupContactos();
        }
    });
    
    // Lógica del Pop-up de Comisiones
    function abrirPopupComisiones() {
        document.getElementById("popupComisiones").style.display = "block";
        document.getElementById("fondoOscuroComisiones").style.display = "block";
    }

    function cerrarPopupComisiones() {
        document.getElementById("popupComisiones").style.display = "none";
        document.getElementById("fondoOscuroComisiones").style.display = "none";
    }

    // Actualiza el listener de Escape para que cierre ambos
    document.addEventListener('keydown', function(event) {
        if (event.key === "Escape") {
            cerrarPopupContactos();
            cerrarPopupComisiones();
        }
    });

</script>

<%@ include file="plantillaFin.jsp" %>