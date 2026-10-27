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

@WebServlet(name = "SeguirTematicaServlet", urlPatterns = {"/SeguirTematicaServlet"})
public class SeguirTematicaServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        Usuario usuarioLogueado = (Usuario) session.getAttribute("usuario");

        if (usuarioLogueado == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        int idTematica = Integer.parseInt(request.getParameter("id"));
        int idMiPerfil = usuarioLogueado.getIdentificador();

        if (ControladorDAOS.esTematicaSeguida(idMiPerfil, idTematica)) {
            ControladorDAOS.dejarSeguirTematica(idMiPerfil, idTematica);
        } else {
            ControladorDAOS.seguirTematica(idMiPerfil, idTematica);
        }

        response.setStatus(HttpServletResponse.SC_OK);
    }
}