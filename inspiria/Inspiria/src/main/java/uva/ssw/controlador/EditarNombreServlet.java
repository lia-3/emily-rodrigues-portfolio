/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package uva.ssw.controlador;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import uva.ssw.database.ControladorDAOS;
import uva.ssw.modelo.Usuario;

/**
 *
 * @author emily
 */
@WebServlet(name = "EditarNombreServlet", urlPatterns = {"/EditarNombreServlet"})
public class EditarNombreServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet EditarNombreServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet EditarNombreServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Si entran por GET, simplemente mostramos la página
        request.getRequestDispatcher("editarNombre.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Usuario usuarioLogueado = (Usuario) session.getAttribute("usuario");

        if (usuarioLogueado == null) {
            response.sendRedirect("iniciarSesion.jsp");
            return;
        }

        String nuevoNombre = request.getParameter("nuevoNombre");

        if (nuevoNombre == null || nuevoNombre.trim().isEmpty()) {
            request.setAttribute("error", "el nombre no puede estar vacío.");
            request.getRequestDispatcher("editarNombre.jsp").forward(request, response);
            return;
        }

        boolean ok = ControladorDAOS.actualizarNombre(usuarioLogueado.getIdentificador(), nuevoNombre.trim());

        if (ok) {
            usuarioLogueado.setNombre(nuevoNombre.trim());
            session.setAttribute("usuario", usuarioLogueado);
            response.sendRedirect("editarPerfil"); 
        } else {
            
            request.setAttribute("error", "El nombre de usuario ya está en uso o no es válido.");
        request.getRequestDispatcher("editarNombre.jsp").forward(request, response);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
