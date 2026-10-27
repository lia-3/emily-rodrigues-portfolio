package uva.ssw.controlador;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import uva.ssw.database.ControladorDAOS;
import uva.ssw.modelo.Comentario;
import uva.ssw.modelo.Tutorial;
import uva.ssw.modelo.Producto;
import uva.ssw.modelo.Usuario;

@WebServlet(name = "PublicarComentarioServlet", urlPatterns = {"/PublicarComentarioServlet"})
public class PublicarComentarioServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null) {
            response.sendRedirect("iniciarSesion.jsp");
            return;
        }

        String texto = request.getParameter("textoComentario");
        String idTutorialParam = request.getParameter("idTutorial");

        if (texto != null && !texto.trim().isEmpty()) {
            
            Comentario com = new Comentario();
            com.setTexto(texto);
            com.setAutor(usuario);

            if (idTutorialParam != null && !idTutorialParam.isEmpty()) {
                try {
                    int idTutorial = Integer.parseInt(idTutorialParam);
                    
                    Tutorial tut = new Tutorial();
                    tut.setId(idTutorial);

                    ControladorDAOS.comentarTutorial(com, tut);
                    
                    response.sendRedirect("DetalleTutorialServlet?id=" + idTutorial);
                    return; 
                    
                } catch (NumberFormatException e) {
                    System.err.println("Error al parsear idTutorial: " + e.getMessage());
                }
            } 
            
        }

        response.sendRedirect("PrincipalServlet");
    }
}