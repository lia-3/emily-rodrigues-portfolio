/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.inf.uva.dis.persistencia.daos;

import java.io.StringReader;
import java.io.StringWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonReaderFactory;
import javax.json.JsonWriter;

import es.inf.uva.dis.persistencia.dbaccess.DBConnection;
import es.inf.uva.dis.servicioscomunes.excepciones.ContratoInactivoException;
import es.inf.uva.dis.servicioscomunes.excepciones.ContratoInexistenteException;

/**
 *
 * @author emily
 */
public class ContratoDAO {

    private static final Logger logger = Logger.getLogger(ContratoDAO.class.getName());

    private ContratoDAO(){
        throw new IllegalStateException("Utility class");
    }
    private static final String SELECT_CONTRATO_Y_TITULAR = 
        "SELECT p.nombre, p.apellidos, p.telefono_contacto, p.nif, " +
        "       ce.nombre_razon_social, ce.telefono AS tlf_empresa, c.activo, c.id_contrato, " +
        "       (c.ubicacion).nombre_via || ', ' || (c.ubicacion).numero || ', ' || (c.ubicacion).localidad AS ubi_principal_txt " +
        "FROM Contrato c " +
        "JOIN Cliente cl ON c.id_cliente = cl.id_persona " +
        "JOIN Persona p ON cl.id_persona = p.id_persona " +
        "LEFT JOIN ClienteEmpresarial ce ON cl.id_persona = ce.id_persona " +
        "WHERE c.numero_contrato = ?;";

    private static final String SELECT_UBICACIONES_EXTRAS = 
        "SELECT (ubicacion).nombre_via || ', ' || (ubicacion).numero || ', ' || (ubicacion).localidad AS direccion_formateada " +
        "FROM ContratoEmpresarialUbicaciones " +
        "WHERE id_contrato = ?;";
    
    private static boolean isContratoActivo(ResultSet rs)throws SQLException{
        return rs.next();
    }
    
    public static String validarDatosContrato(String numContrato, String nifCif) throws ContratoInexistenteException, ContratoInactivoException{
        String resultadoJSON = null;
        DBConnection db = DBConnection.getInstance();
        
        try (PreparedStatement ps = db.getStatement(SELECT_CONTRATO_Y_TITULAR)) {
            if (ps == null) {
                throw new SQLException("No se pudo preparar la sentencia SELECT_CONTRATO_Y_TITULAR");
            }
            
            ps.setString(1, numContrato);
            ResultSet rs = ps.executeQuery();
            
            if(!isContratoActivo(rs)){
                throw new ContratoInexistenteException();
            }
            boolean activo = rs.getBoolean("activo");
            if (!activo) {
                throw new ContratoInactivoException();
            }

            String nifBD = rs.getString("nif");
            if (!nifBD.equalsIgnoreCase(nifCif)) {
                throw new ContratoInexistenteException();
            }

            int idContrato = rs.getInt("id_contrato");
            String nombrePersona = rs.getString("nombre");
            String apellidosPersona = rs.getString("apellidos");
            String tlfContactoPersona = rs.getString("telefono_contacto");
            String razonSocial = rs.getString("nombre_razon_social");
            String tlfEmpresa = rs.getString("tlf_empresa");
            String ubiPrincipalText = rs.getString("ubi_principal_txt");

            String nombreFinal;
            String tlfFinal;

            if (razonSocial != null) {
                nombreFinal = razonSocial;
                tlfFinal = tlfEmpresa != null ? tlfEmpresa : tlfContactoPersona;
            } else {
                nombreFinal = nombrePersona + " " + apellidosPersona;
                tlfFinal = tlfContactoPersona;
            }

            JsonArrayBuilder ubiBuilder = Json.createArrayBuilder();

            if (ubiPrincipalText != null) {
                ubiBuilder.add(ubiPrincipalText);
            }

            try (PreparedStatement psUbi = db.getStatement(SELECT_UBICACIONES_EXTRAS)) {
                if (psUbi == null) {
                    throw new SQLException("No se pudo preparar la sentencia SELECT_UBICACIONES_EXTRAS");
                }
                psUbi.setInt(1, idContrato);
                ResultSet rsUbi = psUbi.executeQuery();
                while (rsUbi.next()) {
                   ubiBuilder.add(rsUbi.getString("direccion_formateada"));
                }
            }
            resultadoJSON = datoValidadosStringJSON(numContrato, idContrato, nombreFinal, tlfFinal, ubiBuilder);
            
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error de base de datos", ex);
        } finally {
            db.closeConnection();
        }
        
        return resultadoJSON;
    }
    
    private static String datoValidadosStringJSON(String numContrato, int idContratoReal, String nombre, String tlf, JsonArrayBuilder ubicaciones) {
        JsonObject json = Json.createObjectBuilder()
                .add("numContrato", numContrato)
                .add("idContratoReal", idContratoReal)
                .add("nombreTitular", nombre)
                .add("telefonoTitular", tlf)
                .add("ubicaciones", ubicaciones.build())
                .build();

        try (StringWriter sw = new StringWriter(); JsonWriter writer = Json.createWriter(sw)) {
            writer.writeObject(json);
            return sw.toString();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error al generar JSON de salida", e);
            return null;
        }
    }
}