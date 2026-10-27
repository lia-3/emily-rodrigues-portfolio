package uva.ssw.controlador;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import uva.ssw.database.ControladorDAOS;
import uva.ssw.modelo.Usuario;

@WebServlet(name = "EditarContrasennaServlet", urlPatterns = {"/EditarContrasennaServlet"})
public class EditarContrasennaServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Usuario user = (Usuario) session.getAttribute("usuario");

        if (user == null) {
            response.sendRedirect("iniciarSesion.jsp");
        } else {
            // Variables de control
            String errorMsg = null;
            String destino = "editarContrasena.jsp";

            String passActual = request.getParameter("passActual");
            String passNueva = request.getParameter("passNueva");
            String passRepetida = request.getParameter("passRepetida");

            if (passNueva.length() < 8) {
                errorMsg = "La nueva contraseña debe tener al menos 8 caracteres.";
            } else if (!passNueva.equals(passRepetida)) {
                errorMsg = "Las nuevas contraseñas no coinciden.";
            } else if (!ControladorDAOS.comprobarContrasenaActual(user.getIdentificador(), passActual)) {
                errorMsg = "La contraseña actual es incorrecta.";
            }

            if (errorMsg == null) {
                boolean exito = ControladorDAOS.actualizarPassword(user.getIdentificador(), passNueva);
                if (exito) {
                    request.setAttribute("mensaje", "Contraseña actualizada correctamente.");
                    destino = "editarPerfil.jsp";
                } else {
                    request.setAttribute("error", "Error interno al actualizar la base de datos.");
                }
            } else {
                request.setAttribute("error", errorMsg);
            }

            request.getRequestDispatcher(destino).forward(request, response);
        }
    }
}