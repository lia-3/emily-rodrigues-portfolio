/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package es.inf.uva.dis.negocio.modelos;

/**
 *
 * @author emily
 */
public enum NivelCriticidad {
    CRITICA("Crítica"),
    ALTA("Alta"),
    MEDIA("Media"),
    BAJA("Baja");

    private final String etiqueta;
    NivelCriticidad(String etiqueta) { this.etiqueta = etiqueta; }

    @Override
    public String toString() { return etiqueta; }
}
