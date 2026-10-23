package uva.tds.base;

import javax.persistence.*;

/**
 * La clase Ruta representa una ruta en un sistema de alquiler de bicicletas.
 * De una ruta se puede conocer su identificador, el usuario que realizó la
 * ruta,
 * la parada de origen y destino de la ruta
 * 
 * @author Marcos de Diego Martin
 * @author Emily Rodrigues
 */
@Entity
@Table(name = "Ruta")
public class Ruta {

    @Id
    @Column(name = "identificador", nullable = false, length = 7)
    private String identificador;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "usuario_id", referencedColumnName = "nif", nullable = false)
    private Usuario usuario;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "parada_origen", referencedColumnName = "identificador", nullable = false)
    private Parada paradaOrigen; // Supongamos que usamos un identificador o nombre de la parada
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "parada_destino", referencedColumnName = "identificador", nullable = false)
    private Parada paradaDestino;

    /**
     * Constructor de la clase Ruta.
     * 
     * @param identificador El identificador único de la ruta. No puede ser null.
     *                      Debe tener entre 1 y 7 caracteres.
     * @param usuario       El usuario que realizó la ruta. No puede ser null.
     * @param paradaOrigen  La parada de origen de la ruta. No puede ser null.
     * @param paradaDestino La parada de destino de la ruta. No puede ser null.
     * @throws IllegalArgumentException si identificador, usuario, paradaOrigen o
     *                                  paradaDestino
     *                                  son null.
     * @throws IllegalArgumentException si el identifiador de la ruta no tiene un
     *                                  longitud de [1..7]
     *                                  caracteres
     */
    public Ruta(String identificador, Usuario usuario, Parada paradaOrigen, Parada paradaDestino) {
        setIdentificador(identificador);
        setUsuario(usuario);
        setParadaOrigen(paradaOrigen);
        setParadaDestino(paradaDestino);

    }

    public Ruta() {
    }

    /**
     * Consulta el identificador único de la ruta.
     * 
     * @return El identificador de la ruta.
     */
    public String getIdentificador() {
        return identificador;
    }

    /**
     * Consulta el usuario asociado a la ruta.
     * 
     * @return El usuario que realizó la ruta.
     */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * Consulta la parada de origen de la ruta.
     * 
     * @return La parada de origen.
     */
    public Parada getParadaOrigen() {
        return paradaOrigen;
    }

    /**
     * Consulta la parada de destino de la ruta.
     * 
     * @return La parada de destino.
     */
    public Parada getParadaDestino() {
        return paradaDestino;
    }

    /**
     * Establece el identificador único de la ruta.
     * 
     * @param identificador El identificador único de la ruta. No puede ser null,
     *                      vacío ni tener una longitud mayor a 7 caracteres.
     * @throws IllegalArgumentException Si el identificador es null, vacío o
     *                                  tiene una longitud mayor a 7 caracteres.
     */
    public void setIdentificador(String identificador) {
        if (identificador == null || identificador.trim().isEmpty() || identificador.length() > 7) {
            throw new IllegalArgumentException("El identificador no puede ser null.");
        }
        this.identificador = identificador;
    }

    /**
     * Establece el usuario asociado a la ruta.
     * 
     * @param usuario El usuario que realiza la ruta. No puede ser null.
     * @throws IllegalArgumentException Si el usuario es null.
     */
    public void setUsuario(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("El usuario no puede ser null.");
        }
        this.usuario = usuario;
    }

    /**
     * Establece la parada de origen de la ruta.
     * 
     * @param paradaOrigen La parada de origen. No puede ser null.
     * @throws IllegalArgumentException Si la parada de origen es null.
     */

    public void setParadaOrigen(Parada paradaOrigen) {
        if (paradaOrigen == null) {
            throw new IllegalArgumentException("La parada de origen no puede ser null.");
        }
        this.paradaOrigen = paradaOrigen;
    }

    /**
     * Establece la parada de destino de la ruta.
     * 
     * @param paradaDestino La parada de destino. No puede ser null.
     * @throws IllegalArgumentException Si la parada de destino es null.
     */

    public void setParadaDestino(Parada paradaDestino) {
        if (paradaDestino == null) {
            throw new IllegalArgumentException("La parada de destino no puede ser null.");
        }
        this.paradaDestino = paradaDestino;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Ruta ruta = (Ruta) o;
        return identificador != null && identificador.equals(ruta.identificador);
    }

    @Override
    public int hashCode() {
        return identificador != null ? identificador.hashCode() : 0;
    }

}
