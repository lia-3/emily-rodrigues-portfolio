package uva.ssw.controlador;

import java.io.IOException;
import java.util.Base64;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import uva.ssw.database.ControladorDAOS;
import uva.ssw.modelo.Usuario;

@WebServlet(name = "EditarFotoServlet", urlPatterns = {"/EditarFotoServlet"})
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024, // 1MB
    maxFileSize = 1024 * 1024 * 5,    // 5MB
    maxRequestSize = 1024 * 1024 * 10 // 10MB
)
public class EditarFotoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Redirige a la vista del formulario de selección de foto
        request.getRequestDispatcher("editarFoto.jsp").forward(request, response);
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

        String accion = request.getParameter("accion");

        if ("eliminar".equals(accion)) {
            // 1. Borrar en la base de datos (pasando null)
            boolean exito = ControladorDAOS.actualizarFotoPerfil(usuarioLogueado.getIdentificador(), null);
            if (exito) {
                // 2. Limpiar el objeto en sesión
                usuarioLogueado.setFotoPerfil(null);
                usuarioLogueado.setFotoBase64(null);
                session.setAttribute("usuario", usuarioLogueado);
            }
        } else {
            // Lógica normal de actualizar (subir archivo)
            Part filePart = request.getPart("nuevaFoto"); 
            if (filePart != null && filePart.getSize() > 0) {
                byte[] fotoBytes = filePart.getInputStream().readAllBytes();
                if (ControladorDAOS.actualizarFotoPerfil(usuarioLogueado.getIdentificador(), fotoBytes)) {
                    usuarioLogueado.setFotoPerfil(fotoBytes);
                    usuarioLogueado.setFotoBase64(Base64.getEncoder().encodeToString(fotoBytes));
                    session.setAttribute("usuario", usuarioLogueado);
                }
            }
        }

        // Siempre redirige de vuelta a la configuración principal
        response.sendRedirect("editarPerfil.jsp"); 
    }
}