/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.inf.uva.dis.negocio.controladorescu;

import java.io.StringWriter;
import java.time.LocalTime;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonWriter;

import es.inf.uva.dis.negocio.modelos.Contrato;
import es.inf.uva.dis.negocio.modelos.Sesion;
import es.inf.uva.dis.persistencia.daos.ContratoDAO;
import es.inf.uva.dis.persistencia.daos.LlamadaDAO;
import es.inf.uva.dis.servicioscomunes.excepciones.ContratoInactivoException;
import es.inf.uva.dis.servicioscomunes.excepciones.ContratoInexistenteException;

/**
 *
 * @author emily
 */
public class ControladorCUAtenderLlamada {

    private static final Logger logger = Logger.getLogger(ControladorCUAtenderLlamada.class.getName());
    
    private LocalTime marcaHoraInicio;
    private String tlfOrigenActual;
    
    private static final String NUM_ORIGEN_STRING = "numeroOrigen";
    private static final String DURACION_STRING = "DURACION_STRING";
    private static final String NIF_COORDINADOR_STRING = "nifCoordinador";
    private static final String ID_CONTRATO_STRING = "idContrato";
    private static final String TIPO_LLAMADA_STRING = "tipoLlamada";
    private static final String DATOS_STRING = "datos";

    public void registrarInicioLlamada(String numTelefonoLlamadaOrigen) {
        this.marcaHoraInicio = LocalTime.now();
        this.tlfOrigenActual = numTelefonoLlamadaOrigen;
        
        logger.log(Level.INFO, "Llamada registrada: {0} desde: {1}", new Object[]{marcaHoraInicio, tlfOrigenActual});
    }

    public void validarDatosContrato(String numeroContrato, String nifCif) throws ContratoInexistenteException, ContratoInactivoException {
        String contratoJson = ContratoDAO.validarDatosContrato(numeroContrato, nifCif);
        if (contratoJson == null) {
            throw new ContratoInexistenteException();
        }
        Contrato contrato = new Contrato(contratoJson);
        Sesion.getInstance().setContrato(contrato);
    }

    public void finalizarComoLlamadaIncorrecta() {
        String nifCoord = obtenerNifCoordinadorSesion();
        JsonObject datosInner = Json.createObjectBuilder()
                .add(NUM_ORIGEN_STRING, tlfOrigenActual)
                .add(DURACION_STRING, calcularDuracionLlamada())
                .add(NIF_COORDINADOR_STRING, nifCoord)
                .build();
        
        JsonObject root = Json.createObjectBuilder()
                .add(TIPO_LLAMADA_STRING, "incorrecta")
                .add(DATOS_STRING, datosInner)
                .build();
        
        LlamadaDAO.registrarLlamada(convertirJsonToString(root));
    }

    public void finalizarComoLlamadaInconsistente(int idContratoReal, String descripcionInconsistencias) {
        String nifCoord = obtenerNifCoordinadorSesion();
        JsonObject datosInner = Json.createObjectBuilder()
                .add(NUM_ORIGEN_STRING, tlfOrigenActual)
                .add(DURACION_STRING, calcularDuracionLlamada())
                .add(NIF_COORDINADOR_STRING, nifCoord)
                .add(ID_CONTRATO_STRING, idContratoReal)
                .add("datosInconsistentes", descripcionInconsistencias)
                .build();
        
        JsonObject root = Json.createObjectBuilder()
                .add(TIPO_LLAMADA_STRING, "inconsistente")
                .add(DATOS_STRING, datosInner)
                .build();
        
        LlamadaDAO.registrarLlamada(convertirJsonToString(root));
    }

    public void finalizarComoLlamadaCorrecta(int idContratoReal) {
        String nifCoord = obtenerNifCoordinadorSesion();
        JsonObject datosInner = Json.createObjectBuilder()
                .add(NUM_ORIGEN_STRING, tlfOrigenActual)
                .add(DURACION_STRING, calcularDuracionLlamada())
                .add(NIF_COORDINADOR_STRING, nifCoord)
                .add(ID_CONTRATO_STRING, idContratoReal)
                .build();
        
        JsonObject root = Json.createObjectBuilder()
                .add(TIPO_LLAMADA_STRING, "correcta")
                .add(DATOS_STRING, datosInner)
                .build();
        
        LlamadaDAO.registrarLlamada(convertirJsonToString(root));
    }

    private String obtenerNifCoordinadorSesion() {
        return Sesion.getInstance().getEmpleado().getDni();
    }

    private int calcularDuracionLlamada() {
        if (marcaHoraInicio == null) return 1;
        long minutes = java.time.temporal.ChronoUnit.MINUTES.between(marcaHoraInicio, LocalTime.now());
        return minutes < 1 ? 1 : (int) minutes;
    }

    private String convertirJsonToString(JsonObject json) {
        try (StringWriter sw = new StringWriter(); JsonWriter writer = Json.createWriter(sw)) {
            writer.writeObject(json);
            return sw.toString();
        } catch (Exception e) {
            return "{}";
        }
    }
    
}