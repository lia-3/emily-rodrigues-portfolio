/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.inf.uva.dis.interfaz;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;

import es.inf.uva.dis.interfaz.vistaycontrol.atenderllamada.AtenderLlamadaVista;
import es.inf.uva.dis.interfaz.vistaycontrol.cambiardisponibilidad.CambiarDisponibilidadVista;
import es.inf.uva.dis.interfaz.vistaycontrol.consultarincidencias.ConsultarIncidenciasVista;
import es.inf.uva.dis.interfaz.vistaycontrol.identificarse.IdentificacionVista;
import es.inf.uva.dis.interfaz.vistaycontrol.menu.MenuVista;

/**
 *
 * @author diego
 */
public class GestorDeInterfazDeUsuario {
    private static GestorDeInterfazDeUsuario instancia;
    private JFrame vistaActual;
    private Map<Class<?>, JFrame> vistasPersistentes;
    
    private GestorDeInterfazDeUsuario(){
        this.vistaActual = null;
        this.vistasPersistentes = new HashMap<>();
    }
    

    /**
     * Obtiene la instancia única del GestorDeInterfazDeUsuario.
     * @return la instancia del GestorDeInterfazDeUsuario
     */
    public static GestorDeInterfazDeUsuario getInstance(){
        if (instancia == null) {
            instancia = new GestorDeInterfazDeUsuario();
        }
        return instancia;
    }
    

    /**
     * Oculta la vista actual y, opcionalmente, la destruye si se indica.
     * @param destruir si es true, la vista actual se destruye; si es false, solo se oculta
     */
    private void ocultarVistaActual(boolean destruir) {
        if (vistaActual != null) {
            vistaActual.setVisible(false);
            if (destruir) vistaActual.dispose();
        }
    }
    

    /**
     * Muestra la vista de identificación. 
     * Si ya existe una instancia de esta vista, se reutiliza; de lo contrario, 
     * se crea una nueva.
     */
    public void mostrarVistaIdentificacion() {
        boolean esLaMisma = vistaActual == vistasPersistentes.get(IdentificacionVista.class);
        ocultarVistaActual(!esLaMisma);
        if (!vistasPersistentes.containsKey(IdentificacionVista.class)) {
            vistasPersistentes.put(IdentificacionVista.class, new IdentificacionVista());
        }
        vistaActual = vistasPersistentes.get(IdentificacionVista.class);
        vistaActual.setVisible(true);
    }
    
    public void mostrarVistaMenu() {
        boolean esLaMisma = vistaActual == vistasPersistentes.get(MenuVista.class);
        ocultarVistaActual(!esLaMisma);
        if (!vistasPersistentes.containsKey(MenuVista.class)) {
            vistasPersistentes.put(MenuVista.class, new MenuVista());
        }
        vistaActual = vistasPersistentes.get(MenuVista.class);
        vistaActual.setVisible(true);
    }

    /**
     * Muestra la vista de cambio de disponibilidad. 
     * La vista de identificación se mantiene persistente para poder volver a ella sin perder su estado 
     */
    public void mostrarVistaCambioDisponibilidad(){ 
        ocultarVistaActual(false);
        vistaActual = new CambiarDisponibilidadVista();
        vistaActual.setVisible(true);
    }
     

    /**
     * Vuelve a la vista de identificación. Si ya se está mostrando, no se hace nada;
     * si se está mostrando otra vista, se oculta y se muestra la de identificación.
     */
    public void cerrarSesion() {
        ocultarVistaActual(true);
        for (JFrame vista : vistasPersistentes.values()) {
            vista.dispose();
        }
        vistasPersistentes.clear();
        mostrarVistaIdentificacion(); 
    }
    
    /**
     * Vuelve a la vista de menu. Si ya se está mostrando, no se hace nada;
     * si se está mostrando otra vista, se oculta y se muestra la de identificación.
     */
    public void volverAVistaMenu() {
        ocultarVistaActual(false);
        limpiarVistasPersistentes();
        vistaActual = vistasPersistentes.get(MenuVista.class);
        if (vistaActual != null) {
            vistaActual.setVisible(true);
        }
    }
     
    /**
     * Cambia la vista a la de consulta de incidencias.
     * Si ya existe una instancia de esta vista, se reutiliza; de lo contrario,
     * se crea una nueva. La consulta de las incidencias se mantiene persistente.
     */
    public void mostrarVistaConsultarIncidencias(){
        ocultarVistaActual(false);
        if (!vistasPersistentes.containsKey(ConsultarIncidenciasVista.class)) {
            vistasPersistentes.put(ConsultarIncidenciasVista.class, new ConsultarIncidenciasVista());
        }
        vistaActual = vistasPersistentes.get(ConsultarIncidenciasVista.class);
        vistaActual.setVisible(true);
    }
     
    /**
     * Cambia la vista a la de atención de llamadas.
     */
    public void mostrarVistaAtenderLlamada(){
        boolean esLaMisma = vistaActual == vistasPersistentes.get(AtenderLlamadaVista.class);
        ocultarVistaActual(!esLaMisma);
        if (!vistasPersistentes.containsKey(AtenderLlamadaVista.class)) {
            vistasPersistentes.put(AtenderLlamadaVista.class, new AtenderLlamadaVista());
        }
        vistaActual = vistasPersistentes.get(AtenderLlamadaVista.class);
        vistaActual.setVisible(true);
    }
    
    
    /**
     * Vuelve a la vista de consulta de incidencias.
     * Si ya se está mostrando, no se hace nada; si se está mostrando otra vista, 
     * se oculta y se muestra la de consulta de incidencias.
     * El detalle de la incidencia no se mantiene persistente.
     */
    public void volverAVistaConsultarIncidencias() {
        ocultarVistaActual(true);
        vistaActual = vistasPersistentes.get(ConsultarIncidenciasVista.class);
        vistaActual.setVisible(true);
    }
    
    /**
    * Limpia todas las vistas persistentes excepto la de menu.
    */
    private void limpiarVistasPersistentes() {
        for (Map.Entry<Class<?>, JFrame> entry : vistasPersistentes.entrySet()) {
            if (!entry.getKey().equals(MenuVista.class)) {
                entry.getValue().dispose();
            }
        }
        JFrame idVista = vistasPersistentes.get(MenuVista.class);
        vistasPersistentes.clear();
        if (idVista != null) {
            vistasPersistentes.put(MenuVista.class, idVista);
        }
    }
}