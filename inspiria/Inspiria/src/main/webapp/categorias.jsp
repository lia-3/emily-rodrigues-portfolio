<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="uva.ssw.modelo.Categoria" %>
<%@ page import="uva.ssw.modelo.Usuario" %>


<% request.setAttribute("paginaActiva", "categorias"); %>
<%@ include file="plantilla.jsp" %>
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
                <%          }
                        } else {
                %>
                    <p style="grid-column: 1 / -1; text-align: center;">Aún no existen categorías.</p>
                <%      }
                %>
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
</script>


<%@ include file="plantillaFin.jsp" %>     
