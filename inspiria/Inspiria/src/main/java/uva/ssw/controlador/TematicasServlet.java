package uva.ssw.controlador;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import uva.ssw.database.ControladorDAOS;
import uva.ssw.modelo.Tematica;

import java.io.IOException;
import java.util.ArrayList;

@WebServlet("/TematicasServlet")
public class TematicasServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 1. Instanciamos el controlador
        ControladorDAOS controlador = new ControladorDAOS();
        
        // 2. Obtenemos la lista de nombres (Strings) que nos da tu método
        ArrayList<String> nombresTematicas = controlador.listarTodosLosNombresDeTematicas();
        
        // 3. Creamos la lista de objetos Tematica que la JSP está esperando
        ArrayList<Tematica> listaParaJSP = new ArrayList<>();
        
        if (nombresTematicas != null) {
            for (String nombre : nombresTematicas) {
                // Buscamos el objeto Tematica completo usando el método que ya tienes
                Tematica tem = controlador.buscarTematica(nombre);
                
                if (tem != null) {
                    listaParaJSP.add(tem); // Lo añadimos a la lista final
                }
            }
        }
        
        // 4. Pasamos la lista de objetos bajo el atributo "tematicas"
        request.setAttribute("tematicas", listaParaJSP);
        
        // 5. Redirigimos a tu vista tematicas.jsp
        request.getRequestDispatcher("tematicas.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doGet(request, response);
    }
}