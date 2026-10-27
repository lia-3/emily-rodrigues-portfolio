<%@page import="uva.ssw.modelo.Tematica"%>
<%@page import="uva.ssw.modelo.Categoria"%>
<%@ page import="uva.ssw.modelo.Usuario" %>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%request.setAttribute("paginaActiva", "principal"); %>
<%@ include file="plantilla.jsp" %> 
                <div class = "contenedorResultados">
                    <label>Has buscado: "${query}"</label>
                    <h5>Categorías</h5>
                    <div class="siguiendoCuadricula">
                        <%
                            Usuario userSesion = (Usuario) session.getAttribute("usuario");
                            ArrayList<Categoria> listaCat = (ArrayList<Categoria>) request.getAttribute("categorias");
                            if (listaCat != null && !listaCat.isEmpty()) {
                                for (Categoria cat : listaCat) {
                                    String imagenCorazon = "img/corazon_vacio.png";
                                    if (userSesion != null) {
                                        boolean laSigue = uva.ssw.database.ControladorDAOS.esCategoriaSeguida(userSesion.getIdentificador(), cat.getIdCategoria());
                                        if (laSigue) {
                                            imagenCorazon = "img/corazon_relleno.png";
                                        }
                                    }
                        %>
                        <div class="tarjetaCategoria">
                            <div class="categoriaMedia" onclick="location.href='DetalleCategoriaServlet?id=<%= cat.getIdCategoria() %>'" style="cursor:pointer;">
                                <% if (cat.getFotoBase64() != null && !cat.getFotoBase64().isEmpty()) { %>
                                    <img src="data:image/jpeg;base64,<%= cat.getFotoBase64() %>" alt="<%= cat.getNombre() %>">
                                <% } else { %>
                                    <img src="img/categoria_default.jpg" alt="Categoría">
                                <% } %>
                            </div>

                            <div class="categoriaInfo">
                                <div class="categoriaTop" style="display: flex; justify-content: space-between; align-items: center;">
                                    <h3 class="categoriaTitulo"><%= cat.getNombre() %></h3>
                                    <img src="<%= imagenCorazon %>" class="iconoSeguir"
                                         data-id="<%= cat.getIdCategoria() %>"
                                         style="width: 25px; height: 25px; cursor: pointer;"
                                         onclick="alternarSeguirCategoria(this)">
                                </div>
                            </div>
                        </div>
                        <%
                                }
                            } else {
                        %>
                            <p>No se han encontrado categorías que coincidan con tu búsqueda.</p>
                        <%
                            }
                        %>

                    </div>
                    <h5>Temáticas</h5>
                    <div class="siguiendoCuadricula">
                        <%
                            ArrayList<Tematica> listaTem = (ArrayList<Tematica>) request.getAttribute("tematicas");
                            if (listaTem != null && !listaTem.isEmpty()) {
                                for (Tematica tem : listaTem) {
                                    String imagenCorazon = "img/corazon_vacio.png";
                                    if (userSesion != null) {
                                        boolean laSigue = uva.ssw.database.ControladorDAOS.esTematicaSeguida(userSesion.getIdentificador(), tem.getIdTematica());
                                        if (laSigue) {
                                            imagenCorazon = "img/corazon_relleno.png";
                                        }
                                    }
                        %>
                        <div class="tarjetaCategoria">
                            <div class="categoriaMedia" onclick="location.href='DetalleTematicaServlet?id=<%= tem.getIdTematica() %>'" style="cursor:pointer;">
                                <% if (tem.getFotoBase64() != null && !tem.getFotoBase64().isEmpty()) { %>
                                    <img src="data:image/jpeg;base64,<%= tem.getFotoBase64() %>" alt="<%= tem.getNombre() %>">
                                <% } else { %>
                                    <img src="img/tematica.png" alt="Temática">
                                <% } %>
                            </div>
                            <div class="categoriaInfo">
                                <div class="categoriaTop" style="display: flex; justify-content: space-between; align-items: center;">
                                    <h3 class="categoriaTitulo"><%= tem.getNombre() %></h3>
                                    <img src="<%= imagenCorazon %>" class="iconoSeguir"
                                         data-id="<%=tem.getIdTematica() %>"
                                         style="width: 25px; height: 25px; cursor: pointer;"
                                         onclick="alternarSeguirTematica(this)">
                                </div>
                            </div>
                        </div>
                        <%
                                }
                            } else {
                        %>
                            <p>No se han encontrado temáticas que coincidan con tu búsqueda.</p>
                        <%
                            }
                        %>
                            
                    </div>
                        <h5>Usuarios</h5>
                        <div class="siguiendoCuadricula">
                            <%
                                ArrayList<Usuario> listaUsuarios = (ArrayList<Usuario>) request.getAttribute("perfiles");

                                if (listaUsuarios != null && !listaUsuarios.isEmpty()) {
                                    for (Usuario us : listaUsuarios) {
                                        String imagenCorazon = "img/corazon_vacio.png";
                                        if (userSesion != null) {
                                            boolean loSigue = uva.ssw.database.ControladorDAOS.esPerfilSeguido(userSesion.getIdentificador(), us.getIdentificador());
                                            if (loSigue) {
                                                imagenCorazon = "img/corazon_relleno.png";
                                            }
                                        }
                                        String destinoEnlace;
                                        if (userSesion != null && us.getIdentificador() == userSesion.getIdentificador()) {
                                            destinoEnlace = "perfil"; 
                                        } else {
                                            destinoEnlace = "VerPerfilServlet?id=" + us.getIdentificador();
                                        }

                            %>
                                <div class="tarjetaPerfilSeguido">
                                    <div class="perfilMedia" onclick="location.href='<%= destinoEnlace %>'" style="cursor:pointer;">
                                        <% if (us.getFotoBase64() != null && !us.getFotoBase64().isEmpty()) { %>
                                            <img src="data:image/png;base64,<%= us.getFotoBase64() %>" alt="Foto de Perfil">
                                        <% } else { %>
                                            <img src="img/perfil_default.png" alt="Foto de Perfil"> <% } %>
                                    </div>
                                    <div class="perfilFooter">
                                        <span class="nombreUser"><%= us.getNombre() %></span>
                                        <% if (userSesion == null || us.getIdentificador() != userSesion.getIdentificador()) { %>
                                            <img src="<%= imagenCorazon %>" class="iconoSeguir"
                                            data-id="<%=us.getIdentificador() %>"
                                            style="width: 25px; height: 25px; cursor: pointer;"
                                            onclick="alternarSeguirUsuario(this)">
                                        <% } %>
                                    </div>
                                </div>
                            <%
                                    }
                                } else {
                            %>
                                <p>No se han encontrado usuarios que coincidan con tu búsqueda.</p>
                            <%
                                }
                            %>
                        </div>
                </div>
<script>
function alternarSeguirCategoria(elementoImg) {
    const idCategoria = elementoImg.getAttribute('data-id');
    
    // Llamamos a tu servlet (solo pasándole el ID)
    fetch('SeguirCategoriaServlet?id=' + idCategoria)
    .then(response => {
        if (response.ok) {
            // Si el servidor devuelve 200 OK, alternamos el dibujo del corazón en pantalla
            if (elementoImg.src.includes('corazon_vacio.png')) {
                elementoImg.src = 'img/corazon_relleno.png';
            } else {
                elementoImg.src = 'img/corazon_vacio.png';
            }
        } else if (response.status === 401) {
            alert('Debes iniciar sesión para seguir categorías.');
            window.location.href = 'iniciarSesion.jsp'; // Lo mandamos a loguearse
        } else {
            alert('Error al procesar la acción en la base de datos.');
        }
    })
    .catch(error => {
        console.error('Error:', error);
    });
}

function alternarSeguirTematica(elementoImg) {
    const idTematica = elementoImg.getAttribute('data-id');
    
    // Hacemos el fetch asíncrono apuntando al nuevo Servlet de temáticas
    fetch('SeguirTematicaServlet?id=' + idTematica, {
        method: 'GET'
    })
    .then(response => {
        if (response.ok) {
            // Alternamos la imagen del corazón de forma inmediata en la pantalla
            if (elementoImg.src.includes('corazon_vacio.png')) {
                elementoImg.src = 'img/corazon_relleno.png';
            } else {
                elementoImg.src = 'img/corazon_vacio.png';
            }
        } else if (response.status === 401) {
            alert('Debes iniciar sesión para seguir temáticas.');
            window.location.href = 'iniciarSesion.jsp';
        } else {
            alert('Error al procesar la acción en la base de datos.');
        }
    })
    .catch(error => {
        console.error('Error:', error);
    });
}

function alternarSeguirUsuario(elementoImg) {
    const idUsuario = elementoImg.getAttribute('data-id');
    
    // ATENCIÓN: Apuntamos exactamente a SeguirPerfilServlet
    fetch('SeguirPerfilServlet?id=' + idUsuario, {
        method: 'GET'
    })
    .then(response => {
        if (response.ok) {
            // Cambiamos el icono del corazón asíncronamente
            if (elementoImg.src.includes('corazon_vacio.png')) {
                elementoImg.src = 'img/corazon_relleno.png';
            } else {
                elementoImg.src = 'img/corazon_vacio.png';
            }
        } else if (response.status === 401) {
            alert('Debes iniciar sesión para seguir a otros usuarios.');
            window.location.href = 'iniciarSesion.jsp';
        } else {
            alert('Error al procesar la acción.');
        }
    })
    .catch(error => console.error('Error:', error));
}
</script>

<%@ include file="plantillaFin.jsp" %>