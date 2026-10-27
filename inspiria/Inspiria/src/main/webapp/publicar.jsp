<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<% request.setAttribute("paginaActiva", "publicar"); %>
<%@ include file="plantilla.jsp" %>     

     
            <div class="contenedorIzquierdaPublicar">
                <img src="img/icono_producto.jpg" alt="Producto" class="imagenSeccion">
                <h2>Producto</h2>
                <button class="botonPantallaPublicar" onclick="location.href='publicarProducto.jsp'">Publicar producto</button>
            </div>

            <div class="contenedorDerechaPublicar">
                <img src="img/icono_tutorial.jpeg" alt="Tutorial" class="imagenSeccion">
                <h2>Tutorial</h2>
                <button class="botonPantallaPublicar" onclick="location.href='publicarTutorial1.jsp'">Publicar tutorial</button>
            </div>
<%@ include file="plantillaFin.jsp" %>     
            

