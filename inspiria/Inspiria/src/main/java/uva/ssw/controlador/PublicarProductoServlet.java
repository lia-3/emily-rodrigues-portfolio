package uva.ssw.controlador;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.io.IOException;
import java.time.LocalDate;
import uva.ssw.database.ControladorDAOS;
import uva.ssw.modelo.Categoria;
import uva.ssw.modelo.Producto;
import uva.ssw.modelo.Tematica;
import uva.ssw.modelo.Usuario;

@WebServlet(name = "PublicarProductoServlet", urlPatterns = {"/PublicarProductoServlet"})
@MultipartConfig
public class PublicarProductoServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String encoding = request.getCharacterEncoding(); 
        if (encoding == null) { 
            request.setCharacterEncoding("UTF-8"); 
        }
        HttpSession sesion = request.getSession();
        
        String nombre = request.getParameter("nombre");
        String descripcion = request.getParameter("descripcion");
        String categoriaNombre = request.getParameter("categoria");
        String tematicaNombre = request.getParameter("tematica");
        double precio = Double.parseDouble(request.getParameter("precio"));
        Usuario autor = (Usuario) sesion.getAttribute("usuario");
        
        Categoria categoria = ControladorDAOS.buscarCategoria(categoriaNombre);
        Tematica tematica = ControladorDAOS.buscarTematica(tematicaNombre);
        
        Producto producto = new Producto();
        producto.setTitulo(nombre);
        producto.setDescripcion(descripcion);
        producto.setPrecio(precio);
        producto.setCategoria(categoria);
        producto.setTematica(tematica);
        producto.setAutor(autor);
        producto.setFechaPublicacion(LocalDate.now());
        
        // Se reemplazó el bucle for por una recuperación directa y segura de una única foto
        Part part = request.getPart("fotosProducto");
        if (part != null && part.getSize() > 0) {
            byte[] foto = part.getInputStream().readAllBytes();
            producto.setImagen(foto);
        }
        
        boolean publicado = ControladorDAOS.publicarProductoAislado(producto);
        if (publicado) {
            response.sendRedirect("PrincipalServlet");
        } else {
            request.setAttribute("error", "Error al publicar");
            request.getRequestDispatcher("publicarProducto.jsp").forward(request, response);
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

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}