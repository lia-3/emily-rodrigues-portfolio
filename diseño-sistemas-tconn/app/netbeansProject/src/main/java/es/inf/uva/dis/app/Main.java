/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.inf.uva.dis.app;
import es.inf.uva.dis.interfaz.GestorDeInterfazDeUsuario;
/**
 *
 * @author diego
 */
public class Main {
    private static GestorDeInterfazDeUsuario gestorVistas;
    
    public static void main(String[] args){
        java.awt.EventQueue.invokeLater(() -> {
            gestorVistas = GestorDeInterfazDeUsuario.getInstance();
            gestorVistas.mostrarVistaIdentificacion();
        });
    }
    
}
