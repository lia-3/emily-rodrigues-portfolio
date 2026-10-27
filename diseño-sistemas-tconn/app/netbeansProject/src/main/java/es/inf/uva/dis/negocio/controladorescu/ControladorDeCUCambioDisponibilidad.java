/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.inf.uva.dis.negocio.controladorescu;

import java.io.IOException;
import java.io.StringReader;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonReaderFactory;

import es.inf.uva.dis.negocio.modelos.Base;
import es.inf.uva.dis.negocio.modelos.Sesion;
import es.inf.uva.dis.persistencia.daos.CambioSituacionTecnicoDAO;
import es.inf.uva.dis.persistencia.daos.TecnicoDAO;
import es.inf.uva.dis.persistencia.daos.ZonaGeograficaDAO;
import es.inf.uva.dis.servicioscomunes.Geolocalizador;
import es.inf.uva.dis.servicioscomunes.excepciones.FueraBaseException;
import es.inf.uva.dis.servicioscomunes.excepciones.FueraTurnoException;
import es.inf.uva.dis.servicioscomunes.excepciones.NoAsignadoException;
import es.inf.uva.dis.servicioscomunes.excepciones.RN3Exception;

/**
 *
 * @author mgonzal
 */
public class ControladorDeCUCambioDisponibilidad {
    
    private static final int MIN_DISTANCE = 500000;
    private static final Geolocalizador GEOLOCALIZADOR = new Geolocalizador();

    
    // Se comprueba que el usaurio este en turno y asignado,
    // despues se guarda en sesion la base de su turno.
    public void seEncuentranEnTurno()throws Exception{        
        String dniTecnico = Sesion.getInstance().getEmpleado().getDni();
        String json = TecnicoDAO.consultaTurnoSesionBasePorTecnico(dniTecnico);
        
        JsonReaderFactory factory = Json.createReaderFactory(null);
        JsonObject turnoJson = null;
        JsonReader reader = factory.createReader( new StringReader(json));
        turnoJson = reader.readObject();
        
        
        // Comprueba que el tecnico este en turno y asignado
        JsonObject turnoTecnico = turnoJson.getJsonObject("turnoTecnico");
        LocalDate iniFecha = LocalDate.parse(turnoTecnico.getString("fechaInicio"));
        LocalTime iniHora = LocalTime.parse(turnoTecnico.getString("horaInicio"));
        LocalDate finFecha = LocalDate.parse(turnoTecnico.getString("fechaFin"));
        LocalTime finHora = LocalTime.parse(turnoTecnico.getString("horaFin"));
        
        LocalDateTime iniTurno = iniFecha.atTime(iniHora);        
        LocalDateTime finTurno = finFecha.atTime(finHora);
        
        if (iniTurno.isAfter(LocalDateTime.now()) || finTurno.isBefore(LocalDateTime.now())){
            throw new FueraTurnoException();
        }
        if (!turnoJson.getString("ultimo_estado").equals("asignado")){
            throw new NoAsignadoException();
        }
        
        JsonObject datosParaBase = Json.createObjectBuilder()
            .add("id_turno", turnoJson.getJsonObject("turnoTecnico").getInt("id_turno"))
            .add("base", turnoJson.getJsonObject("base"))
            .build();
        
        Base base = new Base(datosParaBase.toString());
        Sesion.getInstance().setBaseTecnico(base);
        
    }
    
    public void registrarCambioEstado(String nuevoEstado) throws RN3Exception, SQLException {
        Sesion sesion = Sesion.getInstance();
        
        String estadoAFijar = nuevoEstado;
        if ("noDisponible".equals(nuevoEstado)) {
            estadoAFijar = aplicarRN3(sesion);
        }

        if (!insertarCambioEstado(estadoAFijar)) throw new SQLException();
    }

    /**
     * Aplica la RN-3: si no hay suficientes disponibles en la zona,
     * fuerza el estado a "disponible".
     * 
     * Devuelve el estado a insertar si la RN-3 no se dispara.
     * Lanza RN3Exception si la RN-3 actúa (con o sin inserción previa).
     * 
     * También se asegura de no insertar el mismo cambio de estado varias
     * veces seguidas en la basende datos
     */
    private String aplicarRN3(Sesion sesion) throws RN3Exception, SQLException {
        String idZona = sesion.getBaseTecnico().getIdZona();
        int numDisponibles = ZonaGeograficaDAO.consultaTurnoSesionBasePorTecnico(idZona);

        if (numDisponibles >= 2) {
            return "noDisponible";
        }else{
            if (!insertarCambioEstado("disponible")) throw new SQLException();
            throw new RN3Exception(); 
        }
    }
    
    
    
    private boolean insertarCambioEstado(String nuevoEstado){
        
        String dtoNuevaDisp = Sesion.getInstance().getBaseTecnico().crearNuevaDisponibilidad(nuevoEstado);

        return CambioSituacionTecnicoDAO.insertarNuevoCambioSituacion(dtoNuevaDisp); 
    }
    
    // Se comprueba que el usario este en el perimetro de su base
    public String geolocalizar() throws FueraBaseException {
        
        try {
            double[] coords = GEOLOCALIZADOR.obtenerCoordsActuales();
            double lat = coords[0];
            double lon = coords[1];

            Base base = Sesion.getInstance().getBaseTecnico();

            if (GEOLOCALIZADOR.calcularDistancia(lat, lon, base.getLat(), base.getLon()) > MIN_DISTANCE) {
                throw new FueraBaseException();
            }

            return GEOLOCALIZADOR.obtenerDireccion(lat, lon);

        } catch (FueraBaseException e) {
            throw e;
        } catch (IOException e) {           
            throw new FueraBaseException();
        }
    }
}
