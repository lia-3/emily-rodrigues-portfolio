package uva.tds.base;

import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import java.util.UUID;

/**
 * Clase que representa una reserva en el sistema de alquiler de bicicletas.
 * Se puede consultar el usuario, la bicicleta, la fecha y la hora de la
 * reserva.
 * 
 * @author Emily Rodrigues
 * @author Ainhoa Carbajo
 */
@Entity
@Table(name = "RESERVAS")
public class Reserva {
    @Id
    @Column(name = "identificador")
    private String identificador;
    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_nif", referencedColumnName = "nif")
    private Usuario usuario;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "BICICLETA_ID", referencedColumnName = "identificador")
    private Bicicleta bicicleta;
    private LocalDateTime fechaHoraReserva;
    private boolean activa;

    /**
     * Crea la reserva de una bicicleta por parte de un usuario activo del sistema.
     * 
     * @param bicicleta bicicleta que el usuario va a alquilar. No puede ser null.
     *                  La bicicleta debe estar disponible para poder realizar la
     *                  reserva..
     * @param usuario   usuario que realiza la reserva. No puede ser null.
     *                  El usuario debe estar activo.
     * @param fecha     fecha en la que se hace la reserva. No puede ser null.
     * @param hora      hora en la que se hace la reserva. No puede ser null.
     * @throws IllegalArgumentException si bicicleta es null
     * @throws IllegalStateException    si la bicicleta no está disponible.
     * @throws IllegalArgumentException si usuario es null
     * @throws IllegalStateException    si el usuario no está activo.
     * @throws IllegalArgumentException si fechaHoraReserva es null.
     */
    public Reserva(Bicicleta bicicleta, Usuario usuario, LocalDateTime fechaHoraReserva) {
        setUsuario(usuario);
        setBicicleta(bicicleta);
        setFechaHoraReserva(fechaHoraReserva);
        this.identificador = UUID.randomUUID().toString();
        activa = true;
    }

    public Reserva() {

    }

    /**
     * Obtiene el identificador de este objeto.
     *
     * @return el identificador como una cadena de texto.
     */

    public String getIdentificador() {
        return identificador;
    }

    /**
     * Consulta la bicicleta que se reservó.
     * 
     * @return bicicleta reservada.
     */
    public Bicicleta getBicicleta() {
        return bicicleta.clone();
    }

    /**
     * Consulta el usuario que realizó la reserva.
     * 
     * @return usuario que reservó la bicicleta.
     * 
     */
    public Usuario getUsuario() {
        return new Usuario(usuario.getNombre(), usuario.getNif(), usuario.getPuntuacion(), usuario.isActivo());
    }

    /**
     * Consulta la fecha y hora de una reserva.
     * 
     * @return LocalDateTime con la fecha y hora de la reserva.
     */
    public LocalDateTime getFechaHoraReserva() {
        return fechaHoraReserva;
    }

    /**
     * Consulta si una fecha y hora dadas superan el limite de tiempo de la reserva.
     * 
     * @param fechaHoraAComparar fecha y hora con la que se quiere saber si se ha
     *                           alcanzado el
     *                           tiempo limite de la reserva. No puede ser null.
     * @param horasMaximas       horas máximas en las que se puede mantener una
     *                           reserva.
     *                           No puede ser menor que cero.
     * @throws IllegalArgumentException si fechaHoraAComparar == null.
     * @throws IllegalArgumentException si horasMaximas es menor que cero.
     */
    public boolean isTiempoLimiteAlcanzado(LocalDateTime fechaHoraAComparar, int horasMaximas) {
        if (horasMaximas < 0)
            throw new IllegalArgumentException();
        try {
            return fechaHoraAComparar.isAfter(fechaHoraReserva.plusHours(horasMaximas));
        } catch (NullPointerException npe) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Verifica si la reserva está activa.
     *
     * @return {true} si la reserva está activa, {false} en caso
     *         contrario.
     */

    public boolean isActiva() {
        return activa;
    }

    /**
     * Establece la fecha y hora de la reserva.
     *
     * @param fechaHoraReserva la fecha y hora de la reserva. No puede ser
     *                         {null}.
     * @throws IllegalArgumentException si {fechaHoraReserva} es {null}.
     */

    public void setFechaHoraReserva(LocalDateTime fechaHoraReserva) {
        if (fechaHoraReserva == null)
            throw new IllegalArgumentException();
        this.fechaHoraReserva = fechaHoraReserva;
    }

    /**
     * Establece el usuario asociado a esta reserva.
     *
     * @param usuario el usuario que realiza la reserva. No puede ser {null}.
     * @throws IllegalArgumentException si {usuario} es {null}.
     * @throws IllegalStateException    si el usuario no está activo.
     */

    public void setUsuario(Usuario usuario) {
        if (usuario == null)
            throw new IllegalArgumentException();
        if (!usuario.isActivo())
            throw new IllegalStateException();
        this.usuario = new Usuario(usuario.getNombre(), usuario.getNif(),
                usuario.getPuntuacion(), usuario.isActivo());
        usuario.addReserva(this);
    }

    /**
     * Establece la bicicleta asociada a esta reserva.
     *
     * @param bicicleta la bicicleta que se desea reservar. No puede ser
     *                  {null}.
     * @throws IllegalArgumentException si {bicicleta} es {null}.
     * @throws IllegalStateException    si la bicicleta no está en estado
     *                                  {DISPONIBLE}.
     */

    public void setBicicleta(Bicicleta bicicleta) {
        if (bicicleta == null)
            throw new IllegalArgumentException();
        if (bicicleta.getEstado() != EstadoBicicleta.DISPONIBLE)
            throw new IllegalStateException();
        this.bicicleta = bicicleta.clone();
        bicicleta.addReserva(this);
    }

    /**
     * Marca la reserva como inactiva.
     */

    public void setReservaInactiva() {
        activa = false;
    }

}