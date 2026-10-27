package uva.ssw.controlador;

import java.io.IOException;
import java.util.ArrayList;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import uva.ssw.database.ControladorDAOS;
import uva.ssw.modelo.Tutorial;
import uva.ssw.modelo.Usuario;

@WebServlet(name = "GuardadosServlet", urlPatterns = {"/GuardadosServlet"})
public class GuardadosServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession sesion = request.getSession();
        Usuario usuario = (Usuario) sesion.getAttribute("usuario");
        
        if (usuario == null) {
            response.sendRedirect("iniciarSesion.jsp");
            return;
        }

        ArrayList<Tutorial> listaGuardados = ControladorDAOS.obtenerTutorialesGuardados(usuario.getIdentificador());
        request.setAttribute("tutorialesGuardados", listaGuardados);
        request.getRequestDispatcher("guardado.jsp").forward(request, response);
    }
}