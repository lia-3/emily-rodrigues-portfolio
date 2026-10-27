package es.inf.uva.dis.interfaz.vistaycontrol.menu;

import es.inf.uva.dis.interfaz.GestorDeInterfazDeUsuario;
import es.inf.uva.dis.negocio.modelos.Sesion;
import es.inf.uva.dis.negocio.modelos.TipoRol;

/**
 *
 * @author mgonzal
 */
public class MenuControlador {
    
    private MenuVista vista;
    private GestorDeInterfazDeUsuario gestorInterfaz;
    
    public MenuControlador(MenuVista vista){
        
        this.vista = vista;
        this.gestorInterfaz = GestorDeInterfazDeUsuario.getInstance();
        iniciarMenuEmpleado();
    }
    
    // Sirve para mostrar la informacion relativa a cada rol
    public void iniciarMenuEmpleado(){
        TipoRol rol = Sesion.getInstance().getEmpleado().getRol();
            
            switch(rol){
                case TipoRol.CORDINADOR_CALLCENTER -> vista.setMenuCoordinador();
                case TipoRol.SUPERVISOR_OPERACIONES -> vista.setMenuSupervisor();
                case TipoRol.TECNICO -> vista.setMenuTecnico();
            }
    }
    
    /**
     * Una vez que el usuario decide entrar en su caso de uso se llama al
     * gestorInterfaz para que cambie la vista y su gestor
     */
    public void entrarEnCasoUso(){
        
        TipoRol rol = Sesion.getInstance().getEmpleado().getRol();
        
        switch(rol){
            case TipoRol.CORDINADOR_CALLCENTER -> gestorInterfaz.mostrarVistaAtenderLlamada();
            case TipoRol.SUPERVISOR_OPERACIONES -> gestorInterfaz.mostrarVistaConsultarIncidencias();
            case TipoRol.TECNICO -> gestorInterfaz.mostrarVistaCambioDisponibilidad();
        }
    }
    
    public void cerrarSesion(){
        
        Sesion.getInstance().cerrarSesion();
        gestorInterfaz.cerrarSesion();
       
    }
    
}
