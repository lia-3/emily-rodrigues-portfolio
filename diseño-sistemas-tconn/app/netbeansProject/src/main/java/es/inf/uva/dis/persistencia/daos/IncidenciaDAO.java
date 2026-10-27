package es.inf.uva.dis.persistencia.daos;

import es.inf.uva.dis.persistencia.dbaccess.DBConnection;
import es.inf.uva.dis.servicioscomunes.excepciones.NoIncidenciaException;
import java.io.StringReader;
import java.io.StringWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonReaderFactory;
import javax.json.JsonWriter;

/**
 * OJITO   0.0!
 * Como el numero de incidencias devueltas varía, cada incidencia dentro del
 * String es un JsonObject diferente y estan identificadas como "incidencia0",
 * "incidencia1", "incidencia2" .....
 * 
 * estado = 'sinResolver', 'resueltaRemotamente', 'resueltaInSitu', 'parcialmenteResuelta', 'escaladaAEspecialista'
 * criticidad = 'critica', 'alta', 'media', 'baja'
 * 
 * @author mgonzal
 */
public class IncidenciaDAO {
    
    private IncidenciaDAO(){
        throw new IllegalStateException("Utility class");
    }
    private static final String SELECT_INCIDENCIAS_POR_FECHA =     
        "SELECT " +
        "    i.id_incidencia, " +
        "    i.codigo, " +
        "    l.hora, " +
        "    i.criticidad, " +
        "    i.estado " +
        "FROM Incidencia i " +
        "JOIN LlamadaCorrecta lc ON i.id_llamada_correcta = lc.id_llamada " +
        "JOIN LlamadaContrato lcon ON lc.id_llamada = lcon.id_llamada " + 
        "JOIN Llamada l ON lcon.id_llamada = l.id_llamada " +
        "WHERE i.fecha = ? " +
        "ORDER BY l.hora ASC;";
    
    public static String consultaIncidenciasPorFecha(String fechaJson) throws NoIncidenciaException {
        String resultadoJSON = null;
        String fechaStr = null;
        JsonReaderFactory factory = Json.createReaderFactory(null);
        try (JsonReader reader = factory.createReader(new StringReader(fechaJson))) {
            JsonObject fechaJsonObj = reader.readObject();
            fechaStr = fechaJsonObj.getString("fecha");
        } catch (Exception ex) {
            Logger.getLogger(IncidenciaDAO.class.getName()).log(Level.SEVERE, "Error al parsear JSON de fecha", ex);
        }

        if (fechaStr == null) return null;

        DBConnection conexion = DBConnection.getInstance();
        conexion.openConnection();
        try (PreparedStatement sentencia = conexion.getStatement(SELECT_INCIDENCIAS_POR_FECHA)) {
            sentencia.setDate(1, java.sql.Date.valueOf(fechaStr)); // Ahora usa la fecha extraída del JSON
            try (ResultSet rs = sentencia.executeQuery()) {

                if (!rs.next()) throw new NoIncidenciaException();

                JsonObjectBuilder incidenciaJsonBuilder = Json.createObjectBuilder();
                int i = 0;

                do {
                    JsonObject incidencia = Json.createObjectBuilder()
                            .add("codigo", rs.getInt("codigo"))
                            .add("hora", rs.getTime("hora").toString())
                            .add("criticidad", rs.getString("criticidad"))
                            .add("estado", rs.getString("estado"))
                            .build();
                    incidenciaJsonBuilder.add("incidencia" + i, incidencia);
                    i++;
                } while (rs.next());

                JsonObject incidenciaJson = incidenciaJsonBuilder.build();

                StringWriter escritorCadenas = new StringWriter();
                try (JsonWriter escritorJson = Json.createWriter(escritorCadenas)) {
                    escritorJson.writeObject(incidenciaJson);
                }
                resultadoJSON = escritorCadenas.toString();
            }
        } catch (SQLException ex) {
            Logger.getLogger(IncidenciaDAO.class.getName()).log(Level.SEVERE, "Error al obtener incidencias", ex);
        } finally {
            conexion.closeConnection();
        }
        return resultadoJSON;
    }
    
}
