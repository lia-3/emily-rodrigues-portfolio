package es.inf.uva.dis.interfaz.vistaycontrol.identificarse;
import es.inf.uva.dis.negocio.controladorescu.ControladorDeCUIdentificacion;
import es.inf.uva.dis.interfaz.GestorDeInterfazDeUsuario;
import es.inf.uva.dis.servicioscomunes.excepciones.LoginException;
import es.inf.uva.dis.servicioscomunes.excepciones.PasswordException;
import es.inf.uva.dis.servicioscomunes.excepciones.UsuarioInactivoException;


/**
 *
 * @author diego
 */
public class IdentificacionControlador {
    private IdentificacionVista vista;
    private ControladorDeCUIdentificacion controladorCU;
    private GestorDeInterfazDeUsuario gestorInterfaz;
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(IdentificacionControlador.class.getName());

    
    public IdentificacionControlador(IdentificacionVista vista){
        this.vista=vista;
        this.gestorInterfaz=GestorDeInterfazDeUsuario.getInstance();
        controladorCU= new ControladorDeCUIdentificacion();
    }
    
    
    
    public void procesaEventoIdentificarse() {
        vista.limpiarError();
        String login=vista.getLogin();
        String password=vista.getPassword();
        try{
            controladorCU.identificarEmpleado(login, password);
            
            // Una vez loggeado el usuario cambia la vista a su menu
            vista.limpiarCamposEscritos();
            gestorInterfaz.mostrarVistaMenu();
        }catch (LoginException | PasswordException lg){
            logger.log(java.util.logging.Level.INFO, "Credenciales Incorrectas", lg);
            vista.mostrarMensajeError("Error: Las credenciales son incorrectas");
        }catch (UsuarioInactivoException ue) {
            vista.mostrarMensajeError("Error: Usuario inactivo");
            logger.log(java.util.logging.Level.INFO, "Usuario Inactivo");
        }
    }
}
