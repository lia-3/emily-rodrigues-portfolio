package uva.ssw.controlador;

import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import uva.ssw.database.ControladorDAOS;
import uva.ssw.modelo.Categoria;


@WebServlet(name = "PrincipalServlet", urlPatterns = {"/PrincipalServlet"})
public class PrincipalServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        
    Categoria ceramica = ControladorDAOS.buscarCategoria("Cerámica");
    Categoria costura  = ControladorDAOS.buscarCategoria("Costura");
    Categoria crochet  = ControladorDAOS.buscarCategoria("Crochet");

    request.setAttribute("ceramica", ceramica);
    request.setAttribute("costura",  costura);
    request.setAttribute("crochet",  crochet);

    RequestDispatcher rd = getServletContext().getRequestDispatcher("/principal.jsp");
    rd.forward(request, response);
        
        
        
    }

   

}
