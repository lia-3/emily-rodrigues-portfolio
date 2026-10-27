<%@ page import="uva.ssw.modelo.Usuario" %>
<% Usuario usuario = (Usuario) session.getAttribute("usuario");
   boolean identificado=!(usuario==null);
  %>
<% String paginaActiva = (String) request.getAttribute("paginaActiva");
    if(paginaActiva==null) paginaActiva="";
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>INSPIRIA-Principal</title>
    <link rel="stylesheet" href="estilo.css">
</head>
<body>
    <div class="contenedorPrincipal">
        
        <div class="barraOpciones">
            <ul class="menu">
                <li <%if(paginaActiva.equals("principal")){ %> class="activo" <%}%>>
                    <a href="PrincipalServlet"><img src="img/inicio.png" class="iconoMenu"> Inicio</a></li>
                
                <li <%if(paginaActiva.equals("publicar")){ %> class="activo" <%}%>>
                    <% if(!identificado){ %>
                    <a onclick="avisoIdentificacion()"> <img src="img/mas.png" class="iconoMenu"> Publicar</a>
                    <% } else { %>
                        <a href="publicar.jsp"><img src="img/mas.png" class="iconoMenu"> Publicar</a>
                    <% } %>
                    </li>
                <li <%if(paginaActiva.equals("tendencias")){ %> class="activo" <%}%>>
                    <a href="TendenciasServlet"><img src="img/crecer.png" class="iconoMenu"> Tendencias</a></li>
                
                <li <%if(paginaActiva.equals("siguiendo")){ %> class="activo" <%}%>>
                    <% if(!identificado){ %>
                    <a onclick="avisoIdentificacion()"> <img src="img/corazon_relleno.png" class="iconoMenu"> Siguiendo</a>
                    <% } else { %>
                        <a href="siguiendoCategorias.jsp"><img src="img/corazon_relleno.png" class="iconoMenu"> Siguiendo</a>
                    <% } %>                
                </li>
                <li <%if(paginaActiva.equals("guardado")){ %> class="activo" <%}%>>
                    <% if(!identificado){ %>
                    <a onclick="avisoIdentificacion()"> <img src="img/btn_guardar_relleno.png" class="iconoMenu"> Guardados</a>
                    <% } else { %>
                        <a href="GuardadosServlet"><img src="img/btn_guardar_relleno.png" class="iconoMenu"> Guardados</a>
                    <% } %>
                </li>
                
                <li <%if(paginaActiva.equals("categorias")){ %> class="activo" <%}%>>
                    <% if(!identificado){ %>
                    <a onclick="avisoIdentificacion()"> <img src="img/categoria.png" class="iconoMenu"> Categorías</a>
                    <% } else { %>
                        <a href="CategoriasServlet"><img src="img/categoria.png" class="iconoMenu"> Categorías</a>
                    <% } %>
                </li>
                
                <li <%if(paginaActiva.equals("tematicas")){ %> class="activo" <%}%>>
                    <% if(!identificado){ %>
                    <a onclick="avisoIdentificacion()"> <img src="img/tematica.png" class="iconoMenu"> Temáticas</a>
                    <% } else { %>
                        <a href="TematicasServlet"><img src="img/tematica.png" class="iconoMenu"> Temáticas</a>
                    <% } %>
                </li>
                
                <li <%if(paginaActiva.equals("muro")){ %> class="activo" <%}%>>
                    <% if(!identificado){ %>
                    <a onclick="avisoIdentificacion()"> <img src="img/muro.png" class="iconoMenu"> Muro</a>
                    <% } else { %>
                        <a href="MuroServlet"><img src="img/muro.png" class="iconoMenu"> Muro</a>
                    <% } %>
                </li>
            </ul>
            <div class="contenedorLogo">
                <img src="img/logo_inspiria.png" alt="Logo Inspiria" class="imagenLogo">
            </div>
        </div>

        <div class="contenido">
            <form action="ResultadosBuscadorServlet" method="GET" class="contenedorBusqueda">
                <input type="text" name="terminoBusqueda" placeholder="Buscar" class="inputBuscar">
                <img src="img/lupa.png" class="iconoLupa">

                <div class="contenedorPerfil" onclick="mostrarPopup()">
                    <div class="circuloPerfil">
                        <img src="FotoPerfilServlet" class="iconoPerfil" alt="Perfil">
                    </div>
                </div>
            </form>
            
            <div id="popupPerfil" class="popup" style="display:none;">
            <% if(identificado) { %>
                <a href="perfil"> Ver perfil.</a>
                <a href="editarPerfil"> Editar perfil </a>
                <a href="cerrarSesion"> Cerrar sesion </a>
            <% } else { %>
                <a href="registro.jsp"> Crear cuenta </a>
                <a href="iniciarSesion.jsp"> Iniciar sesion </a>
            <% } %>
            </div>
            
        <div id="fondoOscuro" class="fondoOscuro" style="display:none"></div>    
        <div id="avisoIdentificacion" class="popupIdentificacion" style="display:none">
            <p> Es necesario identificarse </p>
            <a href="registro.jsp"> Crear cuenta </a>
            <a href="iniciarSesion.jsp"> Iniciar sesion </a>
            <button onclick="cerrarAvisoIdentificacion()" class="botonesAccion"> Cancelar </button>
        </div>
    
            
        
            