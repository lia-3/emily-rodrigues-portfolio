/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.inf.uva.dis.negocio.controladorescu;

import es.inf.uva.dis.negocio.modelos.Empleado;
import es.inf.uva.dis.negocio.modelos.Sesion;
import es.inf.uva.dis.negocio.modelos.TipoRol;
import es.inf.uva.dis.persistencia.daos.EmpleadoDAO;
import es.inf.uva.dis.servicioscomunes.excepciones.LoginException;
import es.inf.uva.dis.servicioscomunes.excepciones.PasswordException;
import es.inf.uva.dis.servicioscomunes.excepciones.UsuarioInactivoException;
/**
 *
 * @author emily
 * @author mario
 * @author diego
 */
public class ControladorDeCUIdentificacion {
    
    
    public Empleado identificarEmpleado(String login,String password) throws LoginException, PasswordException, UsuarioInactivoException{
        Sesion sesion;
        String json=getJsonEmpleadoPorLoginYPassword(login, password);
        
        Empleado empleadoBuscado = new Empleado(json);
        
        if(empleadoBuscado.estaActivo()){
            sesion = Sesion.getInstance();
            sesion.iniciarUsuario(empleadoBuscado);
        } else {
            throw new UsuarioInactivoException();
        }
       
        return empleadoBuscado;
    }
    
    
    //Modificar con DAOS poner documetacion Exception
    private String getJsonEmpleadoPorLoginYPassword(String login ,String password) throws LoginException, PasswordException{
        return EmpleadoDAO.consultaEmpleadoPorLoginYPassword(login, password);
    }   
    
    public TipoRol getTipoRolSesion(){
        return Sesion.getInstance().getEmpleado().getRol();
    }
    
}
