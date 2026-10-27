package uva.ssw.controlador;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import uva.ssw.database.ControladorDAOS;
import uva.ssw.modelo.Tutorial;

@WebServlet(name = "DetalleTutorialServlet", urlPatterns = {"/DetalleTutorialServlet"})
public class DetalleTutorialServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String idParam = request.getParameter("id");
        
        if (idParam == null || idParam.isEmpty()) {
            response.sendRedirect("PrincipalServlet");
            return;
        }

        try {
            int idTutorial = Integer.parseInt(idParam);

            Tutorial tutorial = ControladorDAOS.obtenerTutorialCompleto(idTutorial);

            if (tutorial != null) {
                request.setAttribute("tutorial", tutorial);
                request.getRequestDispatcher("entradaTutorial.jsp").forward(request, response);
            } else {
                response.sendRedirect("PrincipalServlet");
            }
            
        } catch (NumberFormatException e) {
            response.sendRedirect("PrincipalServlet");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}