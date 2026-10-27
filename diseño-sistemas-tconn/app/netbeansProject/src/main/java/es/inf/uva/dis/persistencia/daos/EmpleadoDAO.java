/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package es.inf.uva.dis.persistencia.daos;

import java.io.StringWriter;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonWriter;

import es.inf.uva.dis.persistencia.dbaccess.DBConnection;
import es.inf.uva.dis.servicioscomunes.excepciones.LoginException;
import es.inf.uva.dis.servicioscomunes.excepciones.PasswordException;

/**
 * 
 * @author emily
 */
public class EmpleadoDAO {
    
   private EmpleadoDAO(){
       throw new IllegalStateException("Utility class");
   }
   private static final String SELECT_EMPLEADO_POR_NIF_CONTRASENNA_DEVUELVE_ESTADO_ACTUAL = 
        "SELECT p.nombre, p.nif, e.password, " +
        "CASE " +
        "    WHEN t.id_puesto IS NOT NULL THEN 'TECNICO' " +
        "    WHEN c.id_puesto IS NOT NULL THEN 'COORDINADOR' " + 
        "    WHEN s.id_puesto IS NOT NULL THEN 'SUPERVISOR' " +
        "    ELSE 'SIN_ROL' " +
        "END AS nombreTipoDeRol, " +
        "v_ult.vinculo AS tipoVinculacion, " +
        "d_ult.disponibilidad AS tipoDisponibilidad " +
        "FROM Persona p " +
        "JOIN Empleado e ON p.id_persona = e.id_persona " +
        "LEFT JOIN Puesto pu_ult ON pu_ult.id_puesto = (" +
        "    SELECT id_puesto FROM Puesto " +
        "    WHERE id_empleado = e.id_persona " +
        "    ORDER BY fecha_inicio DESC LIMIT 1) " + 
        "LEFT JOIN Tecnico t ON pu_ult.id_puesto = t.id_puesto " +
        "LEFT JOIN Coordinador c ON pu_ult.id_puesto = c.id_puesto " +
        "LEFT JOIN Supervisor s ON pu_ult.id_puesto = s.id_puesto " +
        "LEFT JOIN VinculacionesConlaEmpresa v_ult ON v_ult.id_empleado = e.id_persona " +
        "    AND v_ult.inicio = (SELECT MAX(inicio) FROM VinculacionesConlaEmpresa WHERE id_empleado = e.id_persona) " +
        "LEFT JOIN Disponibilidades d_ult ON d_ult.id_empleado = e.id_persona " +
        "    AND d_ult.comienzo = (SELECT MAX(comienzo) FROM Disponibilidades WHERE id_empleado = e.id_persona) " +
        "WHERE p.nif = ?;";
  

    public static String consultaEmpleadoPorLoginYPassword(String nif, String passwordIntroducida) throws LoginException, PasswordException {
        String resultadoJSON = null;
        DBConnection conexion = DBConnection.getInstance();
        conexion.openConnection();

        try (PreparedStatement sentencia = conexion.getStatement(SELECT_EMPLEADO_POR_NIF_CONTRASENNA_DEVUELVE_ESTADO_ACTUAL)) {
            sentencia.setString(1, nif);

            try (ResultSet rs = sentencia.executeQuery()) {
                if (rs.next()) {
                    String passwordBD = rs.getString("password");
                    
                    // Comprobación de contraseña
                    if (passwordBD != null && passwordBD.equals(passwordIntroducida)) {
                        
                        String vinculo = rs.getString("tipoVinculacion");
                        String disponibilidad = rs.getString("tipoDisponibilidad");


                        JsonObject empleadoJson = Json.createObjectBuilder()
                                .add("nombre", rs.getString("nombre"))
                                .add("dni", rs.getString("nif"))
                                .add("rol", rs.getString("nombreTipoDeRol"))
                                .add("vinculacion", vinculo != null ? vinculo : "SIN_VINCULACION")
                                .add("disponibilidad", disponibilidad != null ? disponibilidad : "NO_REGISTRADA")
                                .build();

                        StringWriter escritorCadenas = new StringWriter();
                        try (JsonWriter escritorJson = Json.createWriter(escritorCadenas)) {
                            escritorJson.writeObject(empleadoJson);
                        }
                        resultadoJSON = escritorCadenas.toString();
                    } else {
                        Logger.getLogger(EmpleadoDAO.class.getName()).log(Level.INFO, "Error en el login o result set vacio");
                        throw new PasswordException();
                    }
                } else {
                    Logger.getLogger(EmpleadoDAO.class.getName()).log(Level.SEVERE, "Error en el login o result set vacio");
                    throw new LoginException();
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(EmpleadoDAO.class.getName()).log(Level.SEVERE, "Error al obtener sesión", ex);
        } finally {
            conexion.closeConnection();
        }
        return resultadoJSON;
    }
}