package uva.ssw.controlador;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import uva.ssw.database.ControladorDAOS;
import java.util.ArrayList;

@WebServlet(name = "PublicarTutorial1Servlet", urlPatterns = {"/PublicarTutorial1Servlet"})
@MultipartConfig
public class PublicarTutorial1Servlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");

        // CASO 1: El usuario envía el formulario (Siguiente)
        if (request.getMethod().equalsIgnoreCase("POST")) {
            String nombre = request.getParameter("nombre");
            String descripcion = request.getParameter("descripcion");
            String categoria = request.getParameter("categoria");
            String tematica = request.getParameter("tematica");
            
            HttpSession sesion = request.getSession();

            // Guardar Portada
            Part portadaPart = request.getPart("portada");
            if (portadaPart != null && portadaPart.getSize() > 0) {
                byte[] portadaBytes = portadaPart.getInputStream().readAllBytes();
                sesion.setAttribute("tutorialPortada", portadaBytes);
            }

            sesion.setAttribute("tutorialNombre", nombre);
            sesion.setAttribute("tutorialDescripcion", descripcion);
            sesion.setAttribute("tutorialCategoria", categoria);
            sesion.setAttribute("tutorialTematica", tematica);
            
            response.sendRedirect("publicarTutorial2.jsp");
        } 
        // CASO 2: Se solicita la página (Carga de datos)
        else {
            ArrayList<String> listaCat = ControladorDAOS.listarTodosLosNombresDeCategorias();
            ArrayList<String> listaTem = ControladorDAOS.listarTodosLosNombresDeTematicas();
            
            request.setAttribute("listaCategorias", listaCat);
            request.setAttribute("listaTematicas", listaTem);
            
            request.getRequestDispatcher("publicarTutorial1.jsp").forward(request, response);
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