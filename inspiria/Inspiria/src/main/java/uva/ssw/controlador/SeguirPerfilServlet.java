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

@WebServlet(name = "SeguirPerfilServlet", urlPatterns = {"/SeguirPerfilServlet"})
public class SeguirPerfilServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        Usuario usuarioLogueado = (Usuario) session.getAttribute("usuario");

        // si no está logueado, lo mandamos a la página de login
        if (usuarioLogueado == null) {
            response.sendRedirect("iniciarSesion.jsp");
            return;
        }

        try {
            int idPerfilDestino = Integer.parseInt(request.getParameter("id"));
            int idMiPerfil = usuarioLogueado.getIdentificador();

            // lógica de la Base de Datos
            if (idMiPerfil != idPerfilDestino) {
                if (ControladorDAOS.esPerfilSeguido(idMiPerfil, idPerfilDestino)) {
                    ControladorDAOS.dejarSeguirUsuario(idMiPerfil, idPerfilDestino);
                } else {
                    ControladorDAOS.seguirUsuario(idMiPerfil, idPerfilDestino);
                }
            }
        } catch (Exception e) {
            System.err.println("Error en SeguirPerfilServlet: " + e.getMessage());
            e.printStackTrace();
        }

        // Le decimos al navegador que regrese a la página de origen
        String referer = request.getHeader("Referer");
        if (referer != null && !referer.isEmpty()) {
            response.sendRedirect(referer); // Vuelve al tutorial, al muro, o donde estuviera
        } else {
            response.sendRedirect("MuroServlet"); 
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doGet(request, response);
    }
}