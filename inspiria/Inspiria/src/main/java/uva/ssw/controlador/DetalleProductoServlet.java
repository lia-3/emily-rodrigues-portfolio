package uva.ssw.controlador;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import uva.ssw.database.ControladorDAOS;
import uva.ssw.modelo.Producto;

@WebServlet(name = "DetalleProductoServlet", urlPatterns = {"/DetalleProductoServlet"})
public class DetalleProductoServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String idParam = request.getParameter("id");
        
        if (idParam == null || idParam.isEmpty()) {
            response.sendRedirect("PrincipalServlet");
            return;
        }

        try {
            int idProducto = Integer.parseInt(idParam);

            // Recuperamos el producto mapeado con todas sus propiedades
            Producto producto = ControladorDAOS.obtenerProductoCompleto(idProducto);

            if (producto != null) {
                // Inyectamos el atributo con el nombre "producto" que esperará tu entradaProducto.jsp
                request.setAttribute("producto", producto);
                request.getRequestDispatcher("entradaProducto.jsp").forward(request, response);
            } else {
                response.sendRedirect("PrincipalServlet");
            }
            
        } catch (NumberFormatException e) {
            System.err.println("Error al parsear el id del producto: " + e.getMessage());
            response.sendRedirect("PrincipalServlet");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}