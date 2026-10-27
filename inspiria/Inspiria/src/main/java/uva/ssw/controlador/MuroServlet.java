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


@WebServlet(name = "MuroServlet", urlPatterns = {"/MuroServlet"})
public class MuroServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null) {
            response.sendRedirect("iniciarSesion.jsp");
            return;
        }

        ArrayList<Tutorial> listaMuro = ControladorDAOS.obtenerTutorialesMuroFiltrado(usuario.getIdentificador());
        
        request.setAttribute("tutorialesMuro", listaMuro);
        request.getRequestDispatcher("muro.jsp").forward(request, response);
    }
}