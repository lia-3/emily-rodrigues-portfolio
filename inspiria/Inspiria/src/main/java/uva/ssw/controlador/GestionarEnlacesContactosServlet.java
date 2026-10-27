package uva.ssw.controlador;

import java.io.IOException;
import java.util.ArrayList;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import uva.ssw.database.ControladorDAOS;
import uva.ssw.modelo.Contacto;
import uva.ssw.modelo.Usuario;

@WebServlet(name = "GestionarEnlacesContactosServlet", urlPatterns = {"/GestionarEnlacesContactosServlet"})
public class GestionarEnlacesContactosServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Usuario user = (Usuario) request.getSession().getAttribute("usuario");
        if (user == null) {
            response.sendRedirect("iniciarSesion.jsp");
            return;
        }
        
        // Consultamos a la DB para mostrar los datos actuales en el formulario [cite: 67]
        ArrayList<Contacto> lista = ControladorDAOS.obtenerContactosUsuario(user.getIdentificador());
        request.setAttribute("listaContactos", lista);

        request.getRequestDispatcher("gestionarEnlacesContactos.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Usuario user = (Usuario) session.getAttribute("usuario");

        if (user == null) {
            response.sendRedirect("iniciarSesion.jsp");
            return;
        }

        // Capturamos los arrays de inputs del formulario
        String[] plataformas = request.getParameterValues("plataforma");
        String[] links = request.getParameterValues("link");

        ArrayList<Contacto> listaNuevos = new ArrayList<>();
        if (plataformas != null && links != null) {
            for (int i = 0; i < plataformas.length; i++) {
                if (!plataformas[i].trim().isEmpty() && !links[i].trim().isEmpty()) {
                    listaNuevos.add(new Contacto(links[i], plataformas[i]));
                }
            }
        }

        boolean exito = ControladorDAOS.guardarContactos(user.getIdentificador(), listaNuevos);

        if (exito) {
            user.setContactos(listaNuevos); 
            session.setAttribute("usuario", user); 

            response.sendRedirect("editarPerfil.jsp"); 
        } else {
            request.setAttribute("error", "Error al guardar los contactos en la base de datos");
            doGet(request, response);
        }
    }
}