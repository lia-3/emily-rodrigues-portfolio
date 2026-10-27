package es.inf.uva.dis.negocio.modelos;

import java.io.StringReader;
import java.time.LocalTime;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonReaderFactory;

/**
 *
 * @author mgonzal
 */
public class Incidencia {
    
    private LocalTime hora;
    private int codigo;
    private EstadoIncidencia estado;
    private CriticidadIncidencia criticidad;
    
    
    public Incidencia (LocalTime hora, int codigo, String estado, String criticidad){
        
        this.hora = hora;
        this.codigo = codigo;
        
        switch(estado){
            case "sinResolver":
                this.estado = EstadoIncidencia.SIN_RESOLVER;
                break;
            case "resueltaRemotamente": 
                this.estado = EstadoIncidencia.RESUELTA_REMOTAMENTE;
                break;
            case "resueltaInSitu": 
                this.estado = EstadoIncidencia.RESUELTA_IN_SITU;
                break;
            case "parcialmenteResuelta": 
                this.estado = EstadoIncidencia.RESUELTA_PARCIALMENTE;
                break;
            case "escaladaAEspecialista": 
                this.estado = EstadoIncidencia.ESCALADA_A_ESPECIALISTA;
                break;
            default:
                this.estado = null;
                break;
        }
        
        switch(criticidad){
            case "critica": 
                this.criticidad = CriticidadIncidencia.CRITICA;
                break;
            case "alta": 
                this.criticidad = CriticidadIncidencia.ALTA;
                break;
            case "media": 
                this.criticidad = CriticidadIncidencia.MEDIA;
                break;
            case "baja": 
                this.criticidad = CriticidadIncidencia.BAJA;
                break;
            default:
                this.criticidad = null;
                break;
        }
    }
    
    public int getCodigo(){
        return codigo;
    }
    
    public LocalTime getHora(){
        return hora;
    }
    
    public EstadoIncidencia getEstado(){
        return estado;
    }
    
    public CriticidadIncidencia getCriticidad(){
        return criticidad;
    }
    
    // Constructor a partir del Json 
    public Incidencia (String jsonString){
        
        JsonReaderFactory factory = Json.createReaderFactory(null);
        JsonObject incidenciaJson = null;
        try(JsonReader reader = factory.createReader( new StringReader(jsonString));){
            incidenciaJson = reader.readObject();
        }catch(Exception e){
            System.getLogger(Incidencia.class.getName()).log(System.Logger.Level.ERROR, (String) null, e);
        }
        
        hora = LocalTime.parse(incidenciaJson.getString("hora"));
        codigo = incidenciaJson.getInt("codigo");
        String estadoString = incidenciaJson.getString("estado");
        String criticidadString = incidenciaJson.getString("criticidad");
        
        switch(estadoString){
            case "sinResolver": 
                estado = EstadoIncidencia.SIN_RESOLVER;
                break;
            case "resueltaRemotamente": 
                estado = EstadoIncidencia.RESUELTA_REMOTAMENTE;
                break;
            case "resueltaInSitu": 
                estado = EstadoIncidencia.RESUELTA_IN_SITU;
                break;
            case "parcialmenteResuelta": 
                estado = EstadoIncidencia.RESUELTA_PARCIALMENTE;
                break;
            case "escaladaAEspecialista": 
                estado = EstadoIncidencia.ESCALADA_A_ESPECIALISTA;
                break;
            default:
                estado = null;
                break;
        }
        
        switch(criticidadString){
            case "critica": 
                criticidad = CriticidadIncidencia.CRITICA;
                break;
            case "alta": 
                criticidad = CriticidadIncidencia.ALTA;
                break;
            case "media": 
                criticidad = CriticidadIncidencia.MEDIA;
                break;
            case "baja": 
                criticidad = CriticidadIncidencia.BAJA;
                break;
            default:
                criticidad = null;
                break;
        }
    }

    @Override
    public String toString() {
        return "Código: " + codigo +
            " - Hora Llamada: " + hora +
            " - Criticidad: " + criticidad.toString() +
            " - Estado: " + estado.toString();
    }
    
}
