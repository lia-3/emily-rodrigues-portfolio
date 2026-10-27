
package uva.ssw.controlador;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import uva.ssw.modelo.Usuario;
import uva.ssw.database.ControladorDAOS;
import uva.ssw.modelo.Categoria;
import uva.ssw.modelo.Tematica;

@WebServlet(urlPatterns = {"/ResultadosBuscadorServlet"})
public class ResultadosBuscadorServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String busqueda = request.getParameter("terminoBusqueda");
        // Obtenemos las tres listas
        ArrayList<Categoria> listaCategorias = new ArrayList<>();
        ArrayList<Tematica> listaTematicas = new ArrayList<>();
        ArrayList<Usuario> listaPerfiles = new ArrayList<>();

        Categoria cat = ControladorDAOS.buscarCategoria(busqueda);
        if (cat != null) {
            listaCategorias.add(cat);
        }

        Tematica tem = ControladorDAOS.buscarTematica(busqueda);
        if (tem != null) {
            listaTematicas.add(tem);
        }

        Usuario user = ControladorDAOS.buscarUsuario(busqueda);
        if (user != null) {
            listaPerfiles.add(user);
        }
        request.setAttribute("categorias", listaCategorias);
        request.setAttribute("tematicas", listaTematicas);
        request.setAttribute("perfiles", listaPerfiles);
        request.setAttribute("query", busqueda);

        request.getRequestDispatcher("resultadosBusqueda.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Redirigimos el POST al GET para que funcione de ambas formas
        doGet(request, response);
    }
}
