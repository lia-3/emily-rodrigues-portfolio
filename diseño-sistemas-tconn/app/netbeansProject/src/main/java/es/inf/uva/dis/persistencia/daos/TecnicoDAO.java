/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.inf.uva.dis.persistencia.daos;

import es.inf.uva.dis.persistencia.dbaccess.DBConnection;
import java.io.StringWriter;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonWriter;
import java.util.NoSuchElementException;

/**
 *
 * @author diego
 */
public class TecnicoDAO {
    private TecnicoDAO(){
        throw new IllegalStateException("Utility class");
    }
    private static final String SELECT_TECNICO_POR_NIF_CONTRASENNA=
        "SELECT " +
    "    tt.fecha_inicio AS turnoFechaInicio, " +
    "    tt.hora_inicio AS turnoHoraInicio, tt.fecha_fin AS turnoFechaFin, " +
    "    tt.hora_fin AS turnoHoraFin, " +
    "    cs.estado AS ultimo_estado, " +
    "    tt.id_turno, b.id_zona, " +
    "    (b.ubicacion).latitud, " +
    "    (b.ubicacion).longitud " +
    "FROM Persona p " +
    "JOIN Empleado e ON p.id_persona = e.id_persona " +
    "JOIN Puesto pu ON e.id_persona = pu.id_empleado " +
    "JOIN Tecnico t ON pu.id_puesto = t.id_puesto " +
    "LEFT JOIN TurnoTecnico tt ON tt.id_turno = ( " +
    "    SELECT id_turno " +
    "    FROM TurnoTecnico " +
    "    WHERE id_tecnico = t.id_puesto " +
    "    ORDER BY fecha_inicio DESC, hora_inicio DESC " +
    "    LIMIT 1 " +
    ") " +
    "LEFT JOIN Base b ON tt.id_base = b.id_base " +
    "LEFT JOIN CambioDeSituacionTecnico cs ON cs.id_cambio = ( " +
    "    SELECT c.id_cambio " +
    "    FROM CambioDeSituacionTecnico c " +
    "    JOIN TurnoTecnico t2 ON c.id_turno_tecnico = t2.id_turno " +
    "    WHERE t2.id_tecnico = t.id_puesto " +
    "    ORDER BY c.fecha DESC, c.hora DESC " +
    "    LIMIT 1 " +
    ") " +
    "WHERE p.nif = ?;";
    
    public static String consultaTurnoSesionBasePorTecnico(String nif){
        String resultadoJSON = null;
        DBConnection conexion = DBConnection.getInstance();
        conexion.openConnection();
        try (PreparedStatement sentencia = conexion.getStatement(SELECT_TECNICO_POR_NIF_CONTRASENNA)) {
            sentencia.setString(1, nif);
            try (ResultSet rs = sentencia.executeQuery()) {
                if (rs.next()) {
                    String ultimoEstado = rs.getString("ultimo_estado");
                    if (ultimoEstado == null) {
                        ultimoEstado = "ninguno"; // O una cadena vacía ""
                    }

                    JsonObject empleadoJson = Json.createObjectBuilder()
                                .add("turnoTecnico", construyeTurno(rs))
                                .add("ultimo_estado", ultimoEstado) // Ahora nunca es null
                                .add("base", construyeBase(rs))
                                .build();

                        StringWriter escritorCadenas = new StringWriter();
                        try (JsonWriter escritorJson = Json.createWriter(escritorCadenas)) {
                            escritorJson.writeObject(empleadoJson);
                        }
                        resultadoJSON = escritorCadenas.toString();
                } else {
                    throw new NoSuchElementException();
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(EmpleadoDAO.class.getName()).log(Level.SEVERE, "Error al obtener sesión", ex);
        } finally {
            conexion.closeConnection();
        }
        return resultadoJSON;
    }
    
    private static JsonObject construyeTurno(ResultSet rs) throws SQLException {
        return Json.createObjectBuilder()
                .add("id_turno", rs.getInt("id_turno"))
                .add("fechaInicio", rs.getDate("turnoFechaInicio").toString())
                .add("horaInicio", rs.getTime("turnoHoraInicio").toString())
                .add("fechaFin", rs.getDate("turnoFechaFin").toString())
                .add("horaFin", rs.getTime("turnoHoraFin").toString())
                .build();
    }
    
    private static JsonObject construyeBase(ResultSet rs) throws SQLException {
        return Json.createObjectBuilder()
                //Me falta la ubicacion
                .add("id_zona", rs.getString("id_zona"))
                .add("latitud", rs.getString("latitud"))
                .add("longitud", rs.getString("longitud"))
                .build();
    }
}

