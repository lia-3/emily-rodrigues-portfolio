/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package uva.ssw.controlador;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import uva.ssw.database.ControladorDAOS;
import uva.ssw.modelo.Usuario;

/**
 *
 * @author emily
 */

@WebServlet(name = "VerPerfilServlet", urlPatterns = {"/VerPerfilServlet"})
public class VerPerfilServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
        throws ServletException, IOException {
    
        String idParam = request.getParameter("id");
        if (idParam == null || idParam.isEmpty()) {
            response.sendRedirect("PrincipalServlet");
            return;
        }

        try {
            int idUsuario = Integer.parseInt(idParam);

            // El Servlet habla SOLO con el Controlador
            Usuario perfilBuscado = ControladorDAOS.obtenerPerfilCompletoPorId(idUsuario);

            if (perfilBuscado != null && perfilBuscado.getNombre() != null) {
                request.setAttribute("perfilBuscado", perfilBuscado);
                request.getRequestDispatcher("verPerfil.jsp").forward(request, response);
            } else {
                // Si el nombre es null, es que el DAO no lo encontró o no lo mapeó
                response.sendRedirect("PrincipalServlet");
            }
        } catch (Exception e) {
            response.sendRedirect("PrincipalServlet");
        }
    }
}