<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="uva.ssw.modelo.Tematica" %>
<%@ page import="uva.ssw.modelo.Usuario" %>

<% request.setAttribute("paginaActiva", "tematicas"); %>
<%@ include file="plantilla.jsp" %>
            <div class="siguiendoCuadricula">
                <%
                    Usuario userSesion = (Usuario) session.getAttribute("usuario");
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
                <%          }
                        } else {
                %>
                    <p style="grid-column: 1 / -1; text-align: center;">Aún no existen temáticas.</p>
                <%      }
                %>
            </div>
<script>
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
</script>

<%@ include file="plantillaFin.jsp" %>     
