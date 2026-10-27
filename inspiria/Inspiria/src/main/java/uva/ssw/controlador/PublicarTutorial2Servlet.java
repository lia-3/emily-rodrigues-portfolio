package uva.ssw.controlador;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import uva.ssw.database.ControladorDAOS;
import uva.ssw.modelo.Categoria;
import uva.ssw.modelo.DificultadTutorial;
import uva.ssw.modelo.Tematica;
import uva.ssw.modelo.Material;
import uva.ssw.modelo.Herramienta;
import uva.ssw.modelo.Paso;
import uva.ssw.modelo.Producto;
import uva.ssw.modelo.Tutorial;
import uva.ssw.modelo.Usuario;
import uva.ssw.modelo.DisponibilidadProducto;
      
import java.time.LocalDate;

@WebServlet(name = "PublicarTutorial2Servlet", urlPatterns = {"/PublicarTutorial2Servlet"})
@MultipartConfig
public class PublicarTutorial2Servlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        HttpSession sesion = request.getSession();
        
        // 1. Recuperar datos de la Sesión (Paso 1)
        String nombre = (String) sesion.getAttribute("tutorialNombre");
        String descripcion = (String) sesion.getAttribute("tutorialDescripcion");
        String categoriaNombre = (String) sesion.getAttribute("tutorialCategoria");
        String tematicaNombre = (String) sesion.getAttribute("tutorialTematica");
        Usuario autor = (Usuario) sesion.getAttribute("usuario");
        byte[] portada = (byte[]) sesion.getAttribute("tutorialPortada");
        
        
        Categoria categoria = ControladorDAOS.buscarCategoria(categoriaNombre);
        Tematica tematica = ControladorDAOS.buscarTematica(tematicaNombre);
        
        // 2. Crear objeto Tutorial
        Tutorial tutorial = new Tutorial();
        if (portada != null) {
            tutorial.setFotoBase64(java.util.Base64.getEncoder().encodeToString(portada));
        }
        tutorial.setTitulo(nombre);
        tutorial.setDescripcion(descripcion);
        tutorial.setCategoria(categoria);
        tutorial.setTematica(tematica);
        tutorial.setAutor(autor);
        tutorial.setFechaPublicacion(LocalDate.now());

        String dificultadNombre = request.getParameter("dificultad");
        tutorial.setDificultad(DificultadTutorial.valueOf(dificultadNombre.toUpperCase()));
        
        // 3. Procesar Materiales dinámicos
        int numMat = 1;
        while(request.getParameter("nombreMaterial" + numMat) != null){
            String n = request.getParameter("nombreMaterial" + numMat);
            String l = request.getParameter("linkMaterial" + numMat);
            if(!n.isEmpty()){
                Material m = new Material();
                m.setNombre(n);
                m.setUrl(l);
                tutorial.agregarMaterial(m);
            }
            numMat++;
        }
        
        // 4. Procesar Herramientas dinámicas
        int numHer = 1;
        while(request.getParameter("nombreHerramienta" + numHer) != null){
            String n = request.getParameter("nombreHerramienta" + numHer);
            String l = request.getParameter("linkHerramienta" + numHer);
            if(!n.isEmpty()){
                Herramienta h = new Herramienta();
                h.setNombre(n);
                h.setUrl(l);
                // ESTA ES LA LÍNEA QUE FALTABA (Añádela):
                tutorial.agregarHerramienta(h);
            }
            numHer++;
        }

        // 5. Procesar Pasos y sus fotos
        int numPaso = 1;
        while(request.getParameter("descripcionPaso" + numPaso) != null){
            String desc = request.getParameter("descripcionPaso" + numPaso);
            if(!desc.trim().isEmpty()){
                Paso paso = new Paso();
                paso.setNumero(numPaso);
                paso.setDescripcion(desc);
                
                jakarta.servlet.http.Part fotoPart = request.getPart("fotoPaso" + numPaso);
                if(fotoPart != null && fotoPart.getSize() > 0){
                    paso.setImagen(fotoPart.getInputStream().readAllBytes());
                }
                tutorial.agregarPaso(paso);
            }
            numPaso++;
        }
        
        // 6. Lógica de Producto Asociado e Imagen
        String subirProducto = request.getParameter("elegirProducto");
        if ("si".equals(subirProducto)) {
            
            Producto producto = new Producto();
            producto.setTitulo(nombre);
            producto.setDescripcion(descripcion);
            producto.setCategoria(categoria);
            producto.setTematica(tematica);
            producto.setAutor(autor);
            producto.setFechaPublicacion(LocalDate.now());
            
            String stockStr = request.getParameter("stock");
            if (stockStr != null) {
                producto.setStock(DisponibilidadProducto.valueOf(stockStr.toUpperCase()));
            }
            
            // ASIGNAR LA MISMA PORTADA DEL TUTORIAL AL PRODUCTO
            producto.setImagen(portada); 

            String precioStr = request.getParameter("precio");
            if (precioStr != null && !precioStr.isEmpty()) {
                producto.setPrecio(Double.parseDouble(precioStr));
            }
            // El stock se maneja en el DAO o puedes setearlo aquí si tienes el Enum
            tutorial.setProductoAsociado(producto);
        }
        
        // 7. Guardar en BD
        boolean exito = ControladorDAOS.publicarContenidoCompleto(tutorial);
        if(exito){
            // Limpiar sesión para evitar duplicados si vuelve atrás
            sesion.removeAttribute("tutorialPortada");
            response.sendRedirect("PrincipalServlet");
        } else {
            request.setAttribute("error", "Error crítico al guardar en la base de datos.");
            request.getRequestDispatcher("publicarTutorial2.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }
}