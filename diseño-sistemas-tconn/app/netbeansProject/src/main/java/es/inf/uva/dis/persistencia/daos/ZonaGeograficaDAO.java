/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.inf.uva.dis.persistencia.daos;

import es.inf.uva.dis.persistencia.dbaccess.DBConnection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author diego
 */
public class ZonaGeograficaDAO {
    private ZonaGeograficaDAO(){
        throw new IllegalStateException("Utility class");
    }
    private static final String SELECT_ZONA_GEOGRAFICA_POR_ID_BASE=
        "SELECT COUNT(DISTINCT t.id_puesto) AS total_tecnicos " +
        "FROM TurnoTecnico tt " +
        "JOIN Tecnico t ON tt.id_tecnico = t.id_puesto " +
        "JOIN Base b ON tt.id_base = b.id_base " +
        "WHERE b.id_zona = ? " +
        "  AND CURRENT_TIMESTAMP BETWEEN " +
        "      (tt.fecha_inicio + tt.hora_inicio) AND " +
        "      (tt.fecha_fin + tt.hora_fin) " +
        "  AND ( " +
        "        SELECT cs.estado " +
        "        FROM CambioDeSituacionTecnico cs " +
        "        WHERE cs.id_turno_tecnico = tt.id_turno " +
        "        ORDER BY cs.fecha DESC, cs.hora DESC " +
        "        LIMIT 1 ) = 'disponible'";
    
    public static int consultaTurnoSesionBasePorTecnico(String idBase){
        int resultadoJSON = 0;
        DBConnection conexion = DBConnection.getInstance();
        conexion.openConnection();
        try (PreparedStatement sentencia = conexion.getStatement(SELECT_ZONA_GEOGRAFICA_POR_ID_BASE)) {
            sentencia.setString(1, idBase);
            try (ResultSet rs = sentencia.executeQuery()) {
                if (rs.next()) {
                    resultadoJSON = Integer.parseInt(rs.getString("total_tecnicos"));
                } else {
                    throw new NoSuchElementException();
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ZonaGeograficaDAO.class.getName()).log(Level.SEVERE, "Error al obtener sesión", ex);
        } finally {
            conexion.closeConnection();
        }
        return resultadoJSON;
    }
}
