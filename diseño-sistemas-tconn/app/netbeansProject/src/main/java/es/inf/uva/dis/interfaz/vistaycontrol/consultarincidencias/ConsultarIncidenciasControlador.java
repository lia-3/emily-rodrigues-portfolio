/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.inf.uva.dis.interfaz.vistaycontrol.consultarincidencias;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import es.inf.uva.dis.interfaz.GestorDeInterfazDeUsuario;
import es.inf.uva.dis.negocio.controladorescu.ControladorCUConsultarIncidencias;
import es.inf.uva.dis.negocio.modelos.EstadoIncidencia;
import es.inf.uva.dis.negocio.modelos.Incidencia;
import es.inf.uva.dis.servicioscomunes.excepciones.NoIncidenciaException;

/**
 *
 * @author emily
 */
public class ConsultarIncidenciasControlador {
    
    private ConsultarIncidenciasVista vistaIncidencias;
    private ControladorCUConsultarIncidencias controladorCU;
    private List<Incidencia> listaIncidenciasFechaSinFiltro;    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(ConsultarIncidenciasControlador.class.getName());

    
    public ConsultarIncidenciasControlador(ConsultarIncidenciasVista vista) {
        vistaIncidencias = vista;
        controladorCU = new ControladorCUConsultarIncidencias();
        listaIncidenciasFechaSinFiltro = new ArrayList<>();
    }
    
    public void procesaEventoSeleccionFecha() {
        Date fecha = vistaIncidencias.getFechaSeleccionada();   
        vistaIncidencias.mostrarMensajeError(""); 

        try {
            listaIncidenciasFechaSinFiltro = controladorCU.obtenerIncidenciasEnLaFecha(fecha);
            procesaEventoFiltrar();
        } catch (NoIncidenciaException e) {            
            logger.log(java.util.logging.Level.INFO, "No hay incidencias para el día seleccionado", e);
            vistaIncidencias.mostrarMensajeError("No hay incidencias para el día seleccionado.");
        } 
    }
    
    public void procesaEventoCancelar() {
        GestorDeInterfazDeUsuario gestorUI = GestorDeInterfazDeUsuario.getInstance();
        gestorUI.volverAVistaMenu();
    }
    
    public void procesaEventoMasDetalles() {
            vistaIncidencias.mostrarDetallesIncidencia("Not implemented yet");
    }
    
    
    public void procesaEventoFiltrar() {
        vistaIncidencias.limpiarDetalles();
        if (listaIncidenciasFechaSinFiltro != null &&  !listaIncidenciasFechaSinFiltro.isEmpty()) {
            String filtro = vistaIncidencias.getValorFiltro();
            List<Incidencia> listaFiltrada;

            switch (filtro) {
                case "Resuelta Remotamente":
                    listaFiltrada = getIncidenciasResueltasRemotamente();
                    break;
                case "Resuelta In Situ":
                    listaFiltrada = getIncidenciasResueltasInSitu();
                    break;
                case "No Resuelta":
                    listaFiltrada = getIncidenciasNoResueltas();
                    break;
                default: 
                    listaFiltrada = listaIncidenciasFechaSinFiltro;
                    break;
            }

            if (listaFiltrada.isEmpty()) {
                vistaIncidencias.mostrarMensajeError("No hay incidencias con el filtro: " + filtro);
            } else {
                vistaIncidencias.mostrarIncidencias(listaFiltrada);
            }
        }
        // si son nulas, no tiene que filtar nada porque aún no ha seleccionado día
    }
    
    
    private ArrayList<Incidencia> getIncidenciasResueltasRemotamente() {
        ArrayList<Incidencia> resultado = new ArrayList<>();
        for (Incidencia i : listaIncidenciasFechaSinFiltro) {
            EstadoIncidencia estado = i.getEstado();
            if (estado.equals(EstadoIncidencia.RESUELTA_REMOTAMENTE)){
                resultado.add(i);
            }
        }
        return resultado;
    }

    private ArrayList<Incidencia> getIncidenciasResueltasInSitu() {
        ArrayList<Incidencia> resultado = new ArrayList<>();
        for (Incidencia i : listaIncidenciasFechaSinFiltro) {
            EstadoIncidencia estado = i.getEstado();
            if (estado.equals(EstadoIncidencia.RESUELTA_IN_SITU)) resultado.add(i);
        }
        return resultado;
    }

    private ArrayList<Incidencia> getIncidenciasNoResueltas() {
        ArrayList<Incidencia> resultado = new ArrayList<>();
        for (Incidencia i : listaIncidenciasFechaSinFiltro) {
            EstadoIncidencia estado = i.getEstado();
            if (estado.equals(EstadoIncidencia.SIN_RESOLVER) 
                    || estado.equals(EstadoIncidencia.RESUELTA_PARCIALMENTE) 
                    || estado.equals(EstadoIncidencia.ESCALADA_A_ESPECIALISTA)) {
                resultado.add(i);
            }
        }
        return resultado;
    }

}