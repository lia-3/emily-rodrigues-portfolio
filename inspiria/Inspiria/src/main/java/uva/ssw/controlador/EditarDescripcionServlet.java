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
@WebServlet(name = "EditarDescripcionServlet", urlPatterns = {"/EditarDescripcionServlet"})
public class EditarDescripcionServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null) {
            response.sendRedirect("iniciarSesion.jsp");
            return;
        }

        // 1. Recuperar el parámetro del formulario
        String nuevaDesc = request.getParameter("nuevaDescripcion");

        // 2. Intentar actualizar en BD
            boolean exito = ControladorDAOS.actualizarDescripcion(usuario.getIdentificador(), nuevaDesc);

        if (exito) {
            usuario.setDescripcion(nuevaDesc);
            session.setAttribute("usuario", usuario);

            // OPCIÓN A: Ir directamente al perfil tras guardar
            response.sendRedirect("editarPerfil.jsp"); 
        } else {
            request.setAttribute("error", "No se pudo actualizar.");
            request.getRequestDispatcher("editarDescripcion.jsp").forward(request, response);
        }
    }

    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Si intentan entrar por GET, los mandamos al formulario JSP
        response.sendRedirect("editarDescripcion.jsp");
    }
}
