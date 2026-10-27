
package uva.ssw.controlador;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import uva.ssw.modelo.Usuario;


@WebServlet(name = "FotoPerfilServlet", urlPatterns = {"/FotoPerfilServlet"})
public class FotoPerfilServlet extends HttpServlet {

  
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
         String encoding= request.getCharacterEncoding(); 
        if (encoding==null) { 
            request.setCharacterEncoding("UTF-8"); 
        }
        
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
        if(usuario!=null && usuario.getFotoPerfil()!=null){
            byte[] foto = usuario.getFotoPerfil();
            
            response.setContentType("image/png");
            response.setContentLength(foto.length);
            response.getOutputStream().write(foto);
        }else{
            response.sendRedirect("img/usuario.png");
        }
        
        
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
