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

@WebServlet(name = "SeguirCategoriaServlet", urlPatterns = {"/SeguirCategoriaServlet"})
public class SeguirCategoriaServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        Usuario usuarioLogueado = (Usuario) session.getAttribute("usuario");

        if (usuarioLogueado == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // Código 401
            return;
        }

        int idCategoria = Integer.parseInt(request.getParameter("id"));
        int idMiPerfil = usuarioLogueado.getIdentificador();

        if (ControladorDAOS.esCategoriaSeguida(idMiPerfil, idCategoria)) {
            ControladorDAOS.dejarSeguirCategoria(idMiPerfil, idCategoria);
        } else {
            ControladorDAOS.seguirCategoria(idMiPerfil, idCategoria);
        }

        response.setStatus(HttpServletResponse.SC_OK);
    }
}