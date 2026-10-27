package uva.ssw.controlador;

import java.io.IOException;
import java.util.ArrayList;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import uva.ssw.database.ControladorDAOS;
import uva.ssw.modelo.Tutorial;

@WebServlet(name = "DetalleTematicaServlet", urlPatterns = {"/DetalleTematicaServlet"})
public class DetalleTematicaServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String idParam = request.getParameter("id");
        ArrayList<Tutorial> listaTutoriales = new ArrayList<>();

        if (idParam != null && !idParam.isEmpty()) {
            try {
                int idTematica = Integer.parseInt(idParam);
                
                listaTutoriales = ControladorDAOS.obtenerTutorialesPorTematica(idTematica);
                
            } catch (NumberFormatException e) {
                System.err.println("Error al parsear el id de la temática: " + e.getMessage());
            }
        }

        request.setAttribute("tutorialesTematica", listaTutoriales);
        request.getRequestDispatcher("detalleTematica.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}