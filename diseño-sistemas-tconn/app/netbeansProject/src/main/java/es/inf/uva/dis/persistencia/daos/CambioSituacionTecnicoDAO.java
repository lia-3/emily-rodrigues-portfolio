package es.inf.uva.dis.persistencia.daos;

import es.inf.uva.dis.negocio.modelos.Base;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import es.inf.uva.dis.persistencia.dbaccess.DBConnection;
import java.io.StringReader;
import java.sql.ResultSet;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonReaderFactory;

/**
 *
 * @author mgonzal
 */
public class CambioSituacionTecnicoDAO {
    
    private CambioSituacionTecnicoDAO(){
        throw new IllegalStateException("Utility class");
    }
    
    private static final String INSERT_CAMBIO =
    "INSERT INTO CambioDeSituacionTecnico " +
    "(estado, ubicacion, fecha, hora, id_turno_tecnico) " +
    "VALUES (?::estado_tecnico, ROW(?, ?)::tipo_coordenada_gps, CURRENT_DATE, CURRENT_TIME, ?)";
    
    public static boolean insertarNuevoCambioSituacion(String nuevaDisp){
        
        // Se lee el DTO
        JsonReaderFactory factory = Json.createReaderFactory(null);
        JsonObject nuevaDispJson = null;
        try(JsonReader reader = factory.createReader( new StringReader(nuevaDisp));){
            nuevaDispJson = reader.readObject();
        }catch(Exception e){
            System.getLogger(Base.class.getName()).log(System.Logger.Level.ERROR, (String) null, e);
        }
        
        boolean resultado = false;
        DBConnection conexion = DBConnection.getInstance();
        conexion.openConnection();
        try (PreparedStatement sentencia = conexion.getStatement(INSERT_CAMBIO)) {
            sentencia.setString(1, nuevaDispJson.getString("estado"));
            sentencia.setDouble(2, nuevaDispJson.getJsonNumber("lat").doubleValue());
            sentencia.setDouble(3, nuevaDispJson.getJsonNumber("lon").doubleValue());
            sentencia.setInt(4, nuevaDispJson.getInt("idTurno"));
            
            sentencia.executeUpdate();
            resultado = true;
        } catch (SQLException ex) {
            Logger.getLogger(CambioSituacionTecnicoDAO.class.getName()).log(Level.SEVERE, "Error al obtener sesión", ex);
        } finally {
            conexion.closeConnection();
        }
        return resultado;
    }
    
}
