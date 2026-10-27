package es.inf.uva.dis.interfaz.vistaycontrol.cambiardisponibilidad;

import java.util.logging.Level;
import java.util.logging.Logger;

import es.inf.uva.dis.interfaz.GestorDeInterfazDeUsuario;
import es.inf.uva.dis.negocio.controladorescu.ControladorDeCUCambioDisponibilidad;
import es.inf.uva.dis.servicioscomunes.excepciones.FueraBaseException;
import es.inf.uva.dis.servicioscomunes.excepciones.FueraTurnoException;
import es.inf.uva.dis.servicioscomunes.excepciones.NoAsignadoException;
import es.inf.uva.dis.servicioscomunes.excepciones.RN3Exception;

/**
 *
 * @author diego
 */
public class CambiarDisponibilidadControlador {
    private final CambiarDisponibilidadVista vista;
    private final ControladorDeCUCambioDisponibilidad controladorCU;
    private final GestorDeInterfazDeUsuario gestorInterfaz;
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(CambiarDisponibilidadControlador.class.getName());
    
    public CambiarDisponibilidadControlador(CambiarDisponibilidadVista vista){
        this.vista=vista;
        this.gestorInterfaz=GestorDeInterfazDeUsuario.getInstance();
        controladorCU= new ControladorDeCUCambioDisponibilidad();
    }
    
    public void volverAlMenu() {
        vista.cerrarVista();
        gestorInterfaz.volverAVistaMenu();
    }

    public void entrarBaseEvento(){
        
        try{
            // Primero comprueba si esta en turno y asignado a una incidencia
            controladorCU.seEncuentranEnTurno();
            
            // Despues se comprueba que el tecnico este realmente en la base
            // y se consigue su direccion actual.
            String direccion = controladorCU.geolocalizar();
            
            // Y se avisa a la vista
            vista.setVistaCambioDisponibilidad(direccion);
            
        }catch(FueraTurnoException e){
            logger.log(java.util.logging.Level.INFO, "Técnico fuera de turno", e);
            vista.mostrarErrorEntrarBase("El técnico no se encuentra en turno");
        }catch(NoAsignadoException e){            
            logger.log(java.util.logging.Level.INFO, "Técnico no asignado a ninguna incidencia", e);
            vista.mostrarErrorEntrarBase("El técnico no esta asignado a ninguna incidencia");
        }catch(FueraBaseException e){
            logger.log(java.util.logging.Level.INFO, "Técnico no se encuentra cerca/dentro de su base", e);
            vista.mostrarErrorEntrarBase("El técnico no esta dentro de su base");
        }catch(RN3Exception e){
            logger.log(java.util.logging.Level.INFO, "Técnico no puede pasar a 'no disponible, hay menos de 2 técnicos disponibles", e);
            vista.mostrarErrorEntrarBase("El técnico no puede cambiar su estado a no"
                    + " disponible porque habria menos de 1 empleado en la base");
        }catch(Exception e){
            Logger.getLogger(CambiarDisponibilidadControlador.class.getName()).log(Level.SEVERE, "Paso algo con la geo",e);
            vista.mostrarErrorEntrarBase("No se pudo acceder a la base");
        }
    }
    
    
    
    public void procesaEventoBotonAceptarCambioDisponibilidad(){
        String nuevoEstado = vista.getNuevoEstado();

        try{
            controladorCU.registrarCambioEstado(nuevoEstado);
            vista.mostrarExito("Estado cambiado correctamente"); 

        } catch (RN3Exception e){
            vista.mostrarErrorRN3("Pasa a estar disponible, no puede pasar a \"no disponible\"");
        } catch (Exception e){
            Logger.getLogger(CambiarDisponibilidadControlador.class.getName()).log(Level.SEVERE, "Error al guardar disp", e);
            vista.mostrarErrorCambiarDisp("No se ha podido actualizar su estado");
        }
    }
}