/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.inf.uva.dis.negocio.controladorescu;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonReaderFactory;
import javax.json.JsonWriter;
import java.util.List;

import es.inf.uva.dis.negocio.modelos.Incidencia;
import es.inf.uva.dis.persistencia.daos.IncidenciaDAO;
import es.inf.uva.dis.servicioscomunes.excepciones.NoIncidenciaException;

/**
 *
 * @author emily
 */
public class ControladorCUConsultarIncidencias {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(ControladorCUConsultarIncidencias.class.getName());
    
    
    public List<Incidencia> obtenerIncidenciasEnLaFecha(Date fecha) throws NoIncidenciaException {
        String fechaFormateada = fecha.toInstant()
            .atZone(java.time.ZoneId.systemDefault())
            .toLocalDate()
            .toString(); 

        JsonObject fechaJson = Json.createObjectBuilder()
            .add("fecha", fechaFormateada)
            .build();

        StringWriter escritorCadenas = new StringWriter();
        String resultadoJSON;
        try (JsonWriter escritorJson = Json.createWriter(escritorCadenas)) {
            escritorJson.writeObject(fechaJson);
        }
        resultadoJSON = escritorCadenas.toString();

        String listaIncidenciaJSON = IncidenciaDAO.consultaIncidenciasPorFecha(resultadoJSON); 
        
        return creaListaIncidencias(listaIncidenciaJSON);
    }

    private List<Incidencia> creaListaIncidencias(String listaIncidenciaJSON) {
        ArrayList<Incidencia> listaIncidencias = new ArrayList<>();

        JsonReaderFactory factory = Json.createReaderFactory(null);
        try (JsonReader reader = factory.createReader(new StringReader(listaIncidenciaJSON))) {
            JsonObject listaJson = reader.readObject();

            int i = 0;
            while (listaJson.containsKey("incidencia" + i)) {
                JsonObject incidenciaJson = listaJson.getJsonObject("incidencia" + i);

                StringWriter escritorCadenas = new StringWriter();
                try (JsonWriter escritorJson = Json.createWriter(escritorCadenas)) {
                    escritorJson.writeObject(incidenciaJson);
                }
                Incidencia incidenciaAgregar = new Incidencia(escritorCadenas.toString());
                listaIncidencias.add(incidenciaAgregar);
                i++;
            }
        } catch (Exception ex) {
            logger.log(java.util.logging.Level.SEVERE,"Error al parsear lista de incidencias", ex);
        }

        return listaIncidencias;
    }
    
}
