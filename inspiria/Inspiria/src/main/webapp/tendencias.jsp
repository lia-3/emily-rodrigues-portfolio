<%@page import="uva.ssw.modelo.Categoria"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<% request.setAttribute("paginaActiva", "tendencias"); %>
<%@ include file="plantilla.jsp" %>     

<%
    Categoria ceramica = (Categoria) request.getAttribute("ceramica");
    Categoria costura  = (Categoria) request.getAttribute("costura");
    Categoria crochet  = (Categoria) request.getAttribute("crochet");
%>
<% if (costura != null) out.println("<!-- foto costura: " + (costura.getFotoBase64() != null ? "OK, longitud=" + costura.getFotoBase64().length() : "NULL") + " -->"); %>


            <h2>Tendencias en categorías</h2>

            <div class="contenedorPodio">
                <div class="puesto-2"
                    <% if (ceramica != null && ceramica.getFotoBase64() != null) { %>
                    style="background-image: url('data:image/jpeg;base64,<%= ceramica.getFotoBase64() %>');"
                    <% } %>>
                   <div class="numeroPosicion">2</div>
                   <div class="cajaCategoria">Cerámica</div>
               </div>
                <div class="puesto-1"
                    <% if (costura != null && costura.getFotoBase64() != null) { %>
                    style="background-image: url('data:image/jpeg;base64,<%= costura.getFotoBase64() %>');"
                    <% } %>>
                   <div class="numeroPosicion">1</div>
                   <div class="cajaCategoria">Costura</div>
               </div>
                <div class="puesto-3"
                    <% if (crochet != null && crochet.getFotoBase64() != null) { %>
                    style="background-image: url('data:image/jpeg;base64,<%= crochet.getFotoBase64() %>');"
                    <% } %>>
                   <div class="numeroPosicion">3</div>
                   <div class="cajaCategoria">Crochet</div>
               </div>
                <div style="clear: both;"></div> </div>

            <div class="lista-tendencias">
                <div class="elementoLista">④ Papiroflexia.</div>
                <div class="elementoLista">⑤ Alambrismo.</div>
                <div class="elementoLista">⑥ Talla.</div>
                <div class="elementoLista">⑦ Orfebreria.</div>
            </div>
  
<%@ include file="plantillaFin.jsp" %>