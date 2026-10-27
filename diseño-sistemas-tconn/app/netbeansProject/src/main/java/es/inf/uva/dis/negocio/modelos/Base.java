/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.inf.uva.dis.negocio.modelos;

import java.io.StringReader;
import java.io.StringWriter;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonReaderFactory;
import javax.json.JsonWriter;

/**
 *
 * @author mgonzal
 */
public class Base {
    
    private int idTurno;
    private String idZona;
    double lat;
    double lon;
    
    public Base(int idTurno, String idZona, double lat, double lon){
        this.idTurno = idTurno;
        this.idZona = idZona;
        this.lat = lat;
        this.lon = lon;
    }
    
    public String getIdZona(){
        return idZona;
    }
    
    public int getIdTurno(){
        return idTurno;
    }
    
    public double getLat(){
        return lat;
    }
    
    public double getLon(){
        return lon;
    }
    
    /**
     * Creacion del DTO para insertar una nueva disponibilidad
     * @param nuevoEstado estado elegidor por el tecnico
     * @return el DAO de la nueva disponibilidad
     */
    public String crearNuevaDisponibilidad (String nuevoEstado){
        JsonObject empleadoJson = Json.createObjectBuilder()
            .add("idTurno", idTurno)
            .add("lat", lat)
            .add("lon", lon)
            .add("estado", nuevoEstado)
            .build();

        StringWriter escritorCadenas = new StringWriter();
        
        try (JsonWriter escritorJson = Json.createWriter(escritorCadenas)) {
            escritorJson.writeObject(empleadoJson);
        }
        return escritorCadenas.toString();
    }
    
    // Constructor a partir del Json
    public Base(String jsonString){
        
        JsonReaderFactory factory = Json.createReaderFactory(null);
        JsonObject baseJson = null;
        try(JsonReader reader = factory.createReader( new StringReader(jsonString));){
            baseJson = reader.readObject();
        }catch(Exception e){
            System.getLogger(Base.class.getName()).log(System.Logger.Level.ERROR, (String) null, e);
        }
        
        idTurno = baseJson.getInt("id_turno");
        
        JsonObject baseDatos = baseJson.getJsonObject("base");
        
        idZona = baseDatos.getString("id_zona");
        lat = Double.parseDouble(baseDatos.getString("latitud"));
        lon = Double.parseDouble(baseDatos.getString("longitud"));
    }
}
