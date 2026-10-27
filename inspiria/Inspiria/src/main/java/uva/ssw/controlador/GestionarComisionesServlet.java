/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package uva.ssw.controlador;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import uva.ssw.database.ControladorDAOS;

import uva.ssw.modelo.*;


/**
 *
 * @author emily
 */

@WebServlet("/GestionarComisionesServlet")
public class GestionarComisionesServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Usuario user = (Usuario) request.getSession().getAttribute("usuario");
        if (user == null) {
            response.sendRedirect("iniciarSesion.jsp");
            return;
        }

        ArrayList<Comision> lista = ControladorDAOS.obtenerComisionesUsuario(user.getIdentificador());
        request.setAttribute("listaComisiones", lista);
        request.getRequestDispatcher("gestionarComisiones.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Usuario user = (Usuario) request.getSession().getAttribute("usuario");
        boolean acepta = "true".equals(request.getParameter("aceptaComisiones"));

        if (acepta) {
            String[] descripciones = request.getParameterValues("descComision");
            String[] precios = request.getParameterValues("precioComision");
            ArrayList<Comision> listaNuevas = new ArrayList<>();

            if (descripciones != null && precios != null) {
                for (int i = 0; i < descripciones.length; i++) {
                    String desc = descripciones[i].trim();
                    String precioStr = (precios.length > i) ? precios[i].trim() : "";

                    if (!desc.isEmpty() && !precioStr.isEmpty()) {
                        try {
                            double precio = Double.parseDouble(precioStr);
                            if (precio >= 0) {
                                Comision c = new Comision();
                                c.setDescripcion(desc);
                                c.setPrecioBase(precio);
                                listaNuevas.add(c);
                            }
                        } catch (NumberFormatException e) {
                            /* Ignorar error */ }
                    }
                }
            }
            ControladorDAOS.guardarComisiones(user.getIdentificador(), listaNuevas, acepta);
            user.setListaComisiones(listaNuevas);
        } else {
            ControladorDAOS.actualizarEstadoComisionesSolo(user.getIdentificador(), false);
        }

        user.setAceptaComisiones(acepta);
        response.sendRedirect("editarPerfil.jsp");
    }
}
