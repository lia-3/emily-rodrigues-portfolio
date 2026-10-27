<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>INSPIRIA - Registro</title>
    <link rel="stylesheet" href="estilo.css">
</head>
<body>
    <div class="contenedorPrincipal flex-container">
        <div class="contenedorIzquierdaRegistro">
            <img src="img/logo_inspiria.png" alt="Logo Inspiria" class="imagenLogoInicial"> 
            <h1>INSPIRIA</h1>
        </div>

        <div class="contenedorDerechaRegistro">
            <div class="tarjetaRegistro"> 
                <form action="RegistroServlet" method="post"> 
                    
                    <div class="campo">
                        <label>Nombre de Usuario</label> 
                        <input type="text" name="usuario" placeholder="Usuario"> 
                    </div>

                    <div class="campo">
                        <label>Correo</label> 
                        <input type="email" name="email" placeholder="usuario@email.com"> 
                    </div>

                    <div class="campo">
                        <label>Contraseña</label> 
                        <div class="inputPassword"> 
                            <input type="password" name="contrasenna" placeholder="Mínimo 8 caracteres"> 
                            <span class="showBtn">Mostrar</span> 
                        </div>
                    </div>

                    <div class="campo">
                        <label>Repetir contraseña</label> 
                        <div class="inputPassword"> 
                            <input type="password" name="contrasenna2" placeholder="Repite tu contraseña"> 
                            <span class="showBtn">Mostrar</span>
                        </div>
                    </div>

                    <div class="terminos"> 
                        <input type="checkbox" id="tos" name="terminos"> 
                        <label for="tos">Acepto los términos y condiciones de servicio</label> 
                    </div>

                    <%-- MENSAJE DE ERROR: Controlado por la clase .error del CSS --%>
                    <%
                        String error = (String) request.getAttribute("error");
                        if(error != null){ 
                    %>
                        <p class="error"><%= error %></p>
                    <% } %>
                    
                    <div class="accionesRegistrar"> 
                        <button type="button" class="btnCancelarRegistro" 
                                onclick="location.href='index.html'">Cancelar</button> 
                        <button type="submit" class="btnRegistrar">Registrarse</button> 
                    </div>
                </form>
            </div>
        </div>
    </div>

    <script>
        // Lógica para el botón de mostrar/ocultar contraseña
        document.querySelectorAll('.showBtn').forEach(btn => {
            btn.addEventListener('click', function() {
                const input = this.previousElementSibling;
                if (input.type === "password") {
                    input.type = "text";
                    this.textContent = "Ocultar";
                } else {
                    input.type = "password";
                    this.textContent = "Mostrar";
                }
            });
        });
    </script>
</body>
</html>