/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.inf.uva.dis.negocio.modelos;

import java.io.StringReader;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonReaderFactory;


/**
 *
 * @author emily
 * @author mario
 * @author diego
 */
public class Empleado {
    private String nombreEmpleado;
    private String dni;
    private TipoRol rol;
    private TipoVinculacion vinculacion;
    private TipoDisponibilidad disponibilidad;
    
    public Empleado(String nombre,String dni,String rol,String vinculacion, String disponibilidad){
       this.nombreEmpleado=nombre;
       this.dni=dni;
       asignarEnums (rol, vinculacion, disponibilidad);
    }
    
    public boolean estaActivo(){
        return vinculacion == TipoVinculacion.CONTRATADO && 
               disponibilidad == TipoDisponibilidad.TRABAJANDO;
    }
    
    public TipoRol getRol(){
        return rol;
    }

    public String getDni(){
        return dni;
    }
    
    public String getNombreEmpleado() {
        return nombreEmpleado;
    }
    
    // Constructor basado en Json
    public Empleado(String jsonString){
        
        JsonReaderFactory factory = Json.createReaderFactory(null);
        JsonObject empleadoJson = null;
        try(JsonReader reader = factory.createReader( new StringReader(jsonString));){
            empleadoJson = reader.readObject();
        }catch(Exception e){
            System.getLogger(Empleado.class.getName()).log(System.Logger.Level.ERROR, (String) null, e);
        }
        
        nombreEmpleado = empleadoJson.getString("nombre");
        dni = empleadoJson.getString("dni");
            
        String rolString = empleadoJson.getString("rol");   
        String vinculacionString = empleadoJson.getString("vinculacion");         
        String disponibilidadString = empleadoJson.getString("disponibilidad");
        
        asignarEnums (rolString, vinculacionString, disponibilidadString);
    }
    
    private void asignarEnums(String rol, String vinculacion, String disponibilidad) {
        switch(vinculacion) {
            case "DESPEDIDO" -> this.vinculacion = TipoVinculacion.DESPEDIDO;
            case "CONTRATADO" -> this.vinculacion = TipoVinculacion.CONTRATADO;
            case "EN_ERTE" -> this.vinculacion = TipoVinculacion.ENERTE;
            default -> this.vinculacion = null;
        }

        switch(rol) {
            case "COORDINADOR", "CALLCENTER" -> this.rol = TipoRol.CORDINADOR_CALLCENTER;
            case "SUPERVISOR" -> this.rol = TipoRol.SUPERVISOR_OPERACIONES;
            case "TECNICO" -> this.rol = TipoRol.TECNICO;
            default -> this.rol = null;
        }

        switch(disponibilidad) {
            case "TRABAJANDO" -> this.disponibilidad = TipoDisponibilidad.TRABAJANDO;
            case "VACACIONES" -> this.disponibilidad = TipoDisponibilidad.VACACIONES;
            case "BAJA_TEMPORAL" -> this.disponibilidad = TipoDisponibilidad.BAJA_TEMPORAL;
            default -> this.disponibilidad = null;
        }
    }
}
