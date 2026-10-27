package uva.ssw.controlador;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import uva.ssw.database.ControladorDAOS;
import uva.ssw.modelo.Categoria;

import java.io.IOException;
import java.util.ArrayList;

@WebServlet("/CategoriasServlet")
public class CategoriasServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        ControladorDAOS controlador = new ControladorDAOS();
        ArrayList<String> nombresCategorias = controlador.listarTodosLosNombresDeCategorias();
        ArrayList<Categoria> listaParaJSP = new ArrayList<>();
        
        if (nombresCategorias != null) {
            for (String nombre : nombresCategorias) {
                Categoria cat = controlador.buscarCategoria(nombre);
                
                if (cat != null) {
                    listaParaJSP.add(cat);
                }
            }
        }
        request.setAttribute("categorias", listaParaJSP);
        request.getRequestDispatcher("categorias.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doGet(request, response);
    }
}