package uva.ssw.controlador;

import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import uva.ssw.database.ControladorDAOS;
import uva.ssw.modelo.Usuario;

    @WebServlet(name = "RegistroServlet", urlPatterns = {"/RegistroServlet"})
    public class RegistroServlet extends HttpServlet {

        protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String errorMsg = null;
        String destino = "/registro.jsp";

        String nombre = request.getParameter("usuario"); 
        String email = request.getParameter("email");
        String contrasenna = request.getParameter("contrasenna");
        String contrasenna2 = request.getParameter("contrasenna2");
        String terminos = request.getParameter("terminos");

        // VALIDACIONES MANUALES (Para que salgan en rojo en el JSP)
        if (nombre == null || email == null || contrasenna == null || nombre.trim().isEmpty() || email.trim().isEmpty() || contrasenna.isEmpty()) {
            errorMsg = "Todos los campos son obligatorios.";
        } 
        else if (contrasenna.length() < 8) {
            errorMsg = "La contraseña es demasiado corta (mínimo 8 caracteres).";
        } 
        else if (!contrasenna.equals(contrasenna2)) {
            errorMsg = "Las contraseñas no coinciden.";
        } 
        else if (terminos == null) {
            errorMsg = "Debes aceptar los términos y condiciones.";
        }

        if (errorMsg == null) {
            // Si no hay errores, intentamos el registro
            Usuario nuevoUsuario = new Usuario(nombre, email, contrasenna);
            int idGenerado = ControladorDAOS.registrarUsuario(nuevoUsuario); 

            if (idGenerado > 0) {
                nuevoUsuario.setIdentificador(idGenerado);
                HttpSession sesion = request.getSession();
                sesion.setAttribute("usuario", nuevoUsuario);
                response.sendRedirect("PrincipalServlet");
                return; // Salida exitosa
            } else {
                errorMsg = "El correo ya está registrado en nuestra base de datos.";
            }
        }

        // Si llegamos aquí, es que hubo un error (Msg no es null)
        request.setAttribute("error", errorMsg);
        request.getRequestDispatcher(destino).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        processRequest(request, response);
    }
}