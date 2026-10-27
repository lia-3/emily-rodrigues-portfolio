package es.inf.uva.dis.interfaz.vistaycontrol.atenderllamada;

import es.inf.uva.dis.interfaz.GestorDeInterfazDeUsuario;
import es.inf.uva.dis.negocio.controladorescu.ControladorCUAtenderLlamada;
import es.inf.uva.dis.negocio.modelos.Contrato;
import es.inf.uva.dis.negocio.modelos.Sesion;
import es.inf.uva.dis.servicioscomunes.excepciones.ContratoInactivoException;
import es.inf.uva.dis.servicioscomunes.excepciones.ContratoInexistenteException;

/**
 * Controlador de la vista "Atender Llamada". Se encarga de gestionar la lógica de interacción
 * @author mario
 * @author diego
 * @author emily
 */
public class AtenderLlamadaControlador {
    
    private AtenderLlamadaVista vista;
    private ControladorCUAtenderLlamada controladorCU;
    
    /**
     * Constructor del controlador, recibe la vista asociada y crea el controlador de caso de uso.
     * @param vista La vista "Atender Llamada" que este controlador gestionará
     */
    public AtenderLlamadaControlador(AtenderLlamadaVista vista) {
        this.vista = vista;
        this.controladorCU = new ControladorCUAtenderLlamada();
    }
    
    /**
     * Invocado cuando el usuario cancela en el panel de inicio de llamada.
     * Se vuelve al menú principal
     */
    public void procesaEventoCancelarEnInicioLlamada() {
        GestorDeInterfazDeUsuario.getInstance().volverAVistaMenu();
    }
    
    /**
     * Invocado cuando el usuario no aporta datos en el panel de inicio de llamada.
     * Se registra la llamada como incorrecta y se muestra un mensaje.
     * PRE: el número de teléfono de origen ya ha sido validado previamente.
     */
    public void procesaEventoNoApartoDatos() {
        String numTelefonoOrigen = vista.getNumTelefonoLlamadaOrigen();
        controladorCU.registrarInicioLlamada(numTelefonoOrigen); 
        controladorCU.finalizarComoLlamadaIncorrecta();
        vista.mostrarMensajeDeLlamadaAlmacenada("Se ha añadido como llamada incorrecta");
    } 


    /**
     * Invocado cuando el usuario aporta datos pero no coinciden con el contrato.
     * Se registra la llamada como inconsistente, se almacena el motivo y se muestra un mensaje.
     * PRE: el contrato ya ha sido validado previamente.
     */
    public void procesaEventoNoAportaDatosDiferentes() {
        StringBuilder sb = new StringBuilder("Inconsistencia declarada por operador: ");
        if (!vista.coincideNombreRazonSocial()) sb.append("[Nombre no coincide] ");
        if (!vista.coincideTlfRadioButton()) sb.append("[Telefono no coincide] ");
        if (!vista.coincideUbicacion()) sb.append("[Ubicacion no coincide/autorizada]");
        
        Contrato contratoActual = Sesion.getInstance().getContrato();
        
        int idContrato = (contratoActual != null) ? contratoActual.getIdContratoReal() : 0;
        controladorCU.finalizarComoLlamadaInconsistente(idContrato, sb.toString());
        vista.mostrarMensajeDeLlamadaAlmacenada("Llamada almacenada como inconsistente");
    }
    
    
    /**
     * Invocado cuando el usuario aporta datos pero no coinciden con el contrato.
     * Se registra la llamada como inconsistente, se almacena el motivo y se muestra un mensaje.
     * PRE: el contrato ya ha sido validado previamente.
     */
    public void procesaEventoValidarDatosAportados() {
        String numTelefonoOrigen = vista.getNumTelefonoLlamadaOrigen();
        String numContrato = vista.geNumContratoTextField();
        String nifCif = vista.getNifCifTextField();
        String tlfContacto = vista.getTlfContactoTextField();
        
        try {
            controladorCU.registrarInicioLlamada(numTelefonoOrigen);
            controladorCU.validarDatosContrato(numContrato, nifCif);
            
            Contrato contratoActual = Sesion.getInstance().getContrato();
            
            vista.setNombreClienteConfirmacion(contratoActual.getNombreTitular());
            vista.setTlfClienteConfirmacion(contratoActual.getTelefonoTitular());
            
            boolean coincideTlf = tlfContacto.trim().equals(contratoActual.getTelefonoTitular().trim());
            vista.premarcarCoincidenciaTelefono(coincideTlf);
            
            vista.cargarUbicacionesEnCombo(contratoActual.getUbicaciones());
            
            vista.mostrarPanelConfirmacion();
            
        } catch (IllegalArgumentException ex) {
            vista.mostrarMensajeError(ex.getMessage());
        } catch (ContratoInexistenteException ex) {
            vista.mostrarMensajeError("ERROR: El contrato o NIF/CIF introducido no está registrado.");
            vista.regresarAPanelEdicion(false);
        } catch (ContratoInactivoException ex) {
            vista.mostrarMensajeDeLlamadaAlmacenada("Llamada almacenada como incorrecta. El contrato existe pero no está actualmente en vigor.");
            controladorCU.finalizarComoLlamadaIncorrecta();
            vista.regresarAPanelEdicion(true);
        }
    }

    /**
     * Invocado cuando el usuario valida la confirmación de datos. Se comprueba que los datos coincidan y
     * se muestra el panel de motivo o se vuelve a edición con mensaje de error.
     * PRE: el contrato ya ha sido validado previamente.
     */
    public void procesaEventoValidarConfirmacion() {
        boolean coincideNombre = vista.coincideNombreRazonSocial();
        boolean coincideTlf = vista.coincideTlfRadioButton();
        boolean coincideUbicacion = vista.coincideUbicacion();
        
        if (!coincideNombre) {
            vista.mostrarMensajeError("ERROR: Identidad inválida. El nombre no coincide. Volviendo a edición.");
            vista.regresarAPanelEdicion(false); 
            return;
        }
        if (!coincideTlf) {
            vista.mostrarMensajeError("ERROR: El teléfono de contacto no coincide. Revisar datos de entrada.");
            vista.regresarAPanelEdicion(false);
            return;
        }
        if (!coincideUbicacion) {
            vista.mostrarMensajeError("ERROR: Ubicación no autorizada contractualmente. Volviendo a edición.");
            vista.regresarAPanelEdicion(false);
            return;
        }
        
        vista.mostrarPanelMotivo();
    }

    /**
     * Invocado cuando el usuario valida el motivo de la llamada.
      * PRE: el contrato ya ha sido validado previamente.
      * POST: la llamada se ha registrado como correcta en el sistema y muestra un mensaje de confirmación.
     */
    public void procesaEventoConsulta(){
        Contrato contratoActual = Sesion.getInstance().getContrato();

        if (contratoActual != null) {
            controladorCU.finalizarComoLlamadaCorrecta(contratoActual.getIdContratoReal());
        }
        vista.mostrarMensajeDeLlamadaAlmacenada("Llamada correcta de contrato almacenada");
    }


    /**
     * Invocado cuando el usuario indica que la llamada se ha producido por una incidencia. 
      * PRE: el contrato ya ha sido validado previamente.
      * POST: la llamada se ha registrado como correcta en el sistema y muestra un mensaje de confirmación.
     */
    public void procesaEventoIncidencia() {
        Contrato contratoActual = Sesion.getInstance().getContrato();

        if (contratoActual != null) {
            controladorCU.finalizarComoLlamadaCorrecta(contratoActual.getIdContratoReal());
        }
        vista.mostrarMensajeDeLlamadaAlmacenada("Llamada correcta de contrato almacenada");
        vista.mostrarMensajeNotImplementedYet();
    }

}