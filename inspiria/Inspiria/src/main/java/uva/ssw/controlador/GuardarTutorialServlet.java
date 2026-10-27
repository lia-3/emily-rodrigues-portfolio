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

@WebServlet(name = "GuardarTutorialServlet", urlPatterns = {"/GuardarTutorialServlet"})
public class GuardarTutorialServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null) {
            response.sendRedirect("iniciarSesion.jsp");
            return;
        }

        int idTutorial = Integer.parseInt(request.getParameter("id"));
        
        if (ControladorDAOS.esTutorialGuardado(usuario.getIdentificador(), idTutorial)) {
            ControladorDAOS.eliminarTutorialGuardado(usuario.getIdentificador(), idTutorial);
        } else {
            ControladorDAOS.guardarTutorial(usuario.getIdentificador(), idTutorial);
        }
        
        String referer = request.getHeader("Referer");
        response.sendRedirect(referer != null ? referer : "MuroServlet");
    }
}