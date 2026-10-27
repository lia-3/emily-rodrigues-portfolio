/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.inf.uva.dis.persistencia.daos;

import java.io.StringReader;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonReaderFactory;

import es.inf.uva.dis.persistencia.dbaccess.DBConnection;

/**
 *
 * @author emily
 */
public class LlamadaDAO {

    private LlamadaDAO(){
        throw new IllegalStateException("Utility class");
    }
    private static final String INSERT_LLAMADA_BASE = 
        "INSERT INTO Llamada (fecha, numero_telefono, hora, duracion, id_coordinador) " +
                       "VALUES (?, ?, ?, ?, (SELECT co.id_puesto " +
                       "                     FROM Coordinador co " +
                       "                     JOIN Puesto pu ON co.id_puesto = pu.id_puesto " +
                       "                     JOIN Empleado em ON pu.id_empleado = em.id_persona " +
                       "                     JOIN Persona pe ON em.id_persona = pe.id_persona " +
                       "                     WHERE pe.nif = ? LIMIT 1)) RETURNING id_llamada";
    
    private static final String INSERT_LLAMADA_INCORRECTA = 
        "INSERT INTO LlamadaIncorrecta (id_llamada) VALUES (?)";
    
    private static final String INSERT_LLAMADA_CONTRATO = 
        "INSERT INTO LlamadaContrato (id_llamada, id_contrato) VALUES (?, ?)";
    
    private static final String INSERT_LLAMADA_CORRECTA = 
        "INSERT INTO LlamadaCorrecta (id_llamada) VALUES (?)";
    
    private static final String INSERT_LLAMADA_INCONSISTENTE = 
        "INSERT INTO LlamadaInconsistente (id_llamada, datos_inconsistentes) VALUES (?, ?)";

    
    /**
     * Registra una llamada a partir de un JSON con la información necesaria.
     * @param llamadaJSON JSON con formato 
     *      {"tipoLlamada": "correcta", "datos": {"numeroOrigen": "600123456", "duracion": 300, 
     *      "idCoordinador": 1, "idContrato": 123}}
     */
    public static void registrarLlamada(String llamadaJSON) {
        JsonReaderFactory factory = Json.createReaderFactory(null);
        DBConnection connection = DBConnection.getInstance();
        connection.openConnection();

        try (JsonReader reader = factory.createReader(new StringReader(llamadaJSON))) {
            JsonObject root = reader.readObject();
            String tipo = root.getString("tipoLlamada");
            JsonObject datos = root.getJsonObject("datos");
            
            
            int idLlamada = insertarLlamadaBase(datos, connection);

            
            switch (tipo.toLowerCase()) {
                case "correcta":
                    insertarLlamadaCorrecta(idLlamada, datos.getInt("idContrato"), connection);
                    break;
                case "incorrecta":
                    insertarLlamadaIncorrecta(idLlamada, connection);
                    break;
                case "inconsistente":
                    insertarLlamadaInconsistente(idLlamada, datos.getInt("idContrato"), 
                                               datos.getString("datosInconsistentes"), connection);
                    break;
                default:
                    throw new IllegalArgumentException("Tipo de llamada no reconocido en JSON: " + tipo);
            }

        } catch (Exception ex) {
            Logger.getLogger(LlamadaDAO.class.getName()).log(Level.SEVERE, "Error al registrar llamada desde JSON", ex);
        } finally {
            connection.closeConnection();
        }
    }


    /**
     * Inserta la información básica de una llamada y devuelve su ID generado.
     * @param datos JSON con formato {"numeroOrigen": "600123456", "duracion": 300, 
     * "idCoordinador": 1, "idContrato": 123}
     * @param conn conexión abierta a la base de datos
     * @return ID de la llamada recién insertada
     * @throws SQLException si ocurre un error al interactuar con la base de datos
     */
    private static int insertarLlamadaBase(JsonObject datos, DBConnection conn) throws SQLException {
        try (PreparedStatement s = conn.getStatement(INSERT_LLAMADA_BASE)) {
            s.setDate(1, Date.valueOf(LocalDate.now()));
            s.setInt(2, Integer.parseInt(datos.getString("numeroOrigen")));
            s.setTime(3, Time.valueOf(LocalTime.now()));
            s.setInt(4, datos.getInt("duracion"));
            s.setString(5, datos.getString("nifCoordinador"));

            ResultSet rs = s.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            throw new SQLException("No se pudo registrar la llamada. Verifique que el coordinador con el NIF aportado exista en el sistema.");
        }
    }


    /**
     * Marca una llamada como incorrecta en la base de datos.
     * @param idLlamada ID de la llamada a marcar como incorrecta
     * @param conn conexión abierta a la base de datos
     * @throws SQLException si ocurre un error al interactuar con la base de datos
     */
    private static void insertarLlamadaIncorrecta(int idLlamada, DBConnection conn) throws SQLException {
        try (PreparedStatement s = conn.getStatement(INSERT_LLAMADA_INCORRECTA)) {
            s.setInt(1, idLlamada);
            s.executeUpdate();
        }
    }


    /**
     * Marca una llamada como correcta y la vincula a un contrato en la base de datos.
     * @param idLlamada ID de la llamada a marcar como correcta
     * @param idContrato ID del contrato al que vincular la llamada
     * @param conn conexión abierta a la base de datos
     * @throws SQLException si ocurre un error al interactuar con la base de datos
     */
    private static void insertarLlamadaCorrecta(int idLlamada, int idContrato, DBConnection conn) throws SQLException {
        vincularLlamadaAContrato(idLlamada, idContrato, conn);
        try (PreparedStatement s = conn.getStatement(INSERT_LLAMADA_CORRECTA)) {
            s.setInt(1, idLlamada);
            s.executeUpdate();
        }
    }

    /**
     * Marca una llamada como inconsistente y la vincula a un contrato en la base de datos.
     * @param idLlamada ID de la llamada a marcar como inconsistente
     * @param idContrato ID del contrato al que vincular la llamada
     * @param inconsistencias Descripción de las inconsistencias encontradas
     * @param conn conexión abierta a la base de datos
     * @throws SQLException si ocurre un error al interactuar con la base de datos
     */
    private static void insertarLlamadaInconsistente(int idLlamada, int idContrato, String inconsistencias, DBConnection conn) throws SQLException {
        vincularLlamadaAContrato(idLlamada, idContrato, conn);
        try (PreparedStatement s = conn.getStatement(INSERT_LLAMADA_INCONSISTENTE)) {
            s.setInt(1, idLlamada);
            s.setString(2, inconsistencias);
            s.executeUpdate();
        }
    }


    /**
     * Vincula una llamada a un contrato en la base de datos.
     * @param idLlamada ID de la llamada a vincular
     * @param idContrato ID del contrato al que vincular la llamada
     * @param conn conexión abierta a la base de datos
     * @throws SQLException si ocurre un error al interactuar con la base de datos
     */
    private static void vincularLlamadaAContrato(int idLlamada, int idContrato, DBConnection conn) throws SQLException {
        try (PreparedStatement s = conn.getStatement(INSERT_LLAMADA_CONTRATO)) {
            s.setInt(1, idLlamada);
            s.setInt(2, idContrato);
            s.executeUpdate();
        }
    }
}