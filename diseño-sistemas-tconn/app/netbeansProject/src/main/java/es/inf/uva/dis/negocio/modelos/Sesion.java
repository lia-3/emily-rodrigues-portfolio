/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.inf.uva.dis.negocio.modelos;

/**
 *
 * @author diego
 */
public class Sesion {
    
    private static Sesion instancia;
    private Empleado empleado;
    private Base base;
    private Contrato contrato;

    private Sesion() {
        this.empleado = null; 
    }

    
    public static Sesion getInstance() {
        if (instancia == null) {
            instancia = new Sesion();
        }
        return instancia;
    }

    /**
     * Guarda la base a la que esta asignada un tecnico
     * @param base
     */
    public void setBaseTecnico(Base base){
        this.base = base;
    }
    
    public Base getBaseTecnico(){
       return base; 
    }
    
    /**
     * Vincula al empleado autenticado con la sesión actual.
     * Se llama desde el controlador tras un login exitoso.
     * @param empleado
     */
    public void iniciarUsuario(Empleado empleado){
        this.empleado = empleado;
    }

    /**
     * Permite obtener el empleado actual para consultar su rol, 
     * disponibilidad o nombre en cualquier parte de la App.
     * @return 
     */
    public Empleado getEmpleado() {
        return empleado;
    }
    
    public void setContrato(Contrato contrato){
        this.contrato = contrato;
    }
    
    public Contrato getContrato(){
        return contrato;
    }

    /**
     * Finaliza la sesión (Logout).
     */
    public void cerrarSesion() {
        this.empleado = null;
    }
}
