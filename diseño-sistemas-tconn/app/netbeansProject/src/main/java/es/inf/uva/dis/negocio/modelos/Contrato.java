/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.inf.uva.dis.negocio.modelos;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonWriter;

/**
 *
 * @author emily
 */
public class Contrato {

    private int idContratoReal;
    private String numeroContrato;
    private String nombreTitular;
    private String telephoneTitular; 
    private List<String> ubicaciones;

    private static final String NUM_CONTACTO_STRING = "numContacto";
    private static final String UBICACION_STRING = "ubicaciones";
    private static final String ID_CONTRATO_REAL_STRING = "idContratoReal";
            
    public Contrato(String contratoJson) {
        this.ubicaciones = new ArrayList<>();
        try (JsonReader reader = Json.createReader(new StringReader(contratoJson))) {
            JsonObject json = reader.readObject();
            
            if (json.containsKey(NUM_CONTACTO_STRING)) {
                this.numeroContrato = json.getString(NUM_CONTACTO_STRING);
            }
            if (json.containsKey(ID_CONTRATO_REAL_STRING)) {
                this.idContratoReal = json.getInt(ID_CONTRATO_REAL_STRING);
            }
            
            this.nombreTitular = json.getString("nombreTitular");
            this.telephoneTitular = json.getString("telefonoTitular");
            
            if (json.containsKey(UBICACION_STRING)) {
                JsonArray array = json.getJsonArray(UBICACION_STRING);
                for (int i = 0; i < array.size(); i++) {
                    this.ubicaciones.add(array.getString(i));
                }
            }
        } catch (Exception e) {
            java.util.logging.Logger.getLogger(Contrato.class.getName())
                    .log(java.util.logging.Level.SEVERE, "Error al reconstruir Contrato desde JSON", e);
        }
    }

    public Contrato(int idContratoReal, String numeroContrato, String nombreTitular, String telefonoTitular, List<String> ubicaciones) {
        this.idContratoReal = idContratoReal;
        this.numeroContrato = numeroContrato;
        this.nombreTitular = nombreTitular;
        this.telephoneTitular = telefonoTitular;
        this.ubicaciones = ubicaciones;
    }

    public int getIdContratoReal() { return idContratoReal; }
    public String getNumeroContrato() { return numeroContrato; }
    public String getNombreTitular() { return nombreTitular; }
    public String getTelefonoTitular() { return telephoneTitular; }
    public List<String> getUbicaciones() { return ubicaciones; }

    public String jsonToString() {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (String ubi : ubicaciones) {
            arrayBuilder.add(ubi);
        }

        JsonObject json = Json.createObjectBuilder()
                .add(NUM_CONTACTO_STRING, numeroContrato != null ? numeroContrato : "")
                .add(ID_CONTRATO_REAL_STRING, idContratoReal)
                .add("nombreTitular", nombreTitular != null ? nombreTitular : "")
                .add("telefonoTitular", telephoneTitular != null ? telephoneTitular : "")
                .add(UBICACION_STRING, arrayBuilder.build())
                .build();

        try (StringWriter sw = new StringWriter(); JsonWriter writer = Json.createWriter(sw)) {
            writer.writeObject(json);
            return sw.toString();
        } catch (Exception e) {
            return "{}";
        }
    }
}