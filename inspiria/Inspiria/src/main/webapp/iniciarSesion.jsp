<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>INSPIRIA - Login</title>
    <link rel="stylesheet" href="estilo.css"> 
</head>
<body>
    <div class="contenedorPrincipal">
        
        <div class="contenedorIzquierdaRegistro">
            <img src="img/logo_inspiria.png" alt="Logo Inspiria" class="imagenLogoInicial">
            <h1>INSPIRIA</h1>
        </div>

        <div class="contenedorDerechaLogin">
            <div class="tarjetaRegistro">                
                <form action="IniciarSesionServlet" method="post">
                    <div class="campo">
                        <label>Correo electrónico o Usuario</label>
                        <input type="text" name="email" placeholder="Tu email o nombre de usuario">
                    </div>

                    <div class="campo">
                        <label>Contraseña</label>
                        <div class="inputPassword">
                            <input type="password" name="contrasenna" placeholder="Ingresa tu contraseña">
                            <span class="showBtn">Mostrar</span>
                    </div>
                        <a href="#" style="font-size: 0.8em; color: #666; text-decoration: none; display: block; margin-top: 5px;">
                            ¿Has olvidado tu contraseña?
                        </a>
                    </div>
                    
                    <% String error = (String) request.getAttribute("error"); %>
                    <% if (error != null) { %>
                    <p class="error"><%= error %></p>
                    <% } %>

                    <div class="accionesRegistrar">
                        <button type="button" class="btnCancelarRegistro" onclick="location.href='index.html'">Cancelar</button>
                        <button type="submit" class="btnRegistrar">Entrar</button>
                    </div>
                </form>
            </div>
        </div>

    </div>
</body>
</html>