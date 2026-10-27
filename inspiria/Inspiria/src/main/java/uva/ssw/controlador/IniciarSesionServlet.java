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


@WebServlet(urlPatterns = {"/IniciarSesionServlet"})
public class IniciarSesionServlet extends HttpServlet {

    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String encoding= request.getCharacterEncoding(); 
        if (encoding==null) { 
            request.setCharacterEncoding("UTF-8"); 
        }
        
        String email = request.getParameter("email");
        String contrasenna = request.getParameter("contrasenna");
        
        Usuario usuario = ControladorDAOS.validarUsuario(email, contrasenna);
        
        if(usuario == null){
            request.setAttribute("error", "Correo o contraseña incorrecto");
            request.setAttribute("email", email);
            request.getServletContext().getRequestDispatcher("/iniciarSesion.jsp").forward(request, response);
            return;
        }
        
        HttpSession sesion = request.getSession();
        sesion.setAttribute("usuario", usuario);
        response.sendRedirect("PrincipalServlet");        
    }   

    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    
}
