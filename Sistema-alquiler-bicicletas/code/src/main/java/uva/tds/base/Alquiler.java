package uva.tds.base;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.UUID;
/**
 * Clase que gestiona el alquiler de una bicicleta
 * Guarda la fecha y hora de inicio y fin del alquiler, así como el ussuario y la bicicleta involucrados
 * 
 * @author Ainhoa Carbajo Orgaz
 * @author Emily Rodrigues
 */
@Entity
@Table(name="ALQUILERES")
public class Alquiler {
    @Id
    @Column(name="identificador")
    private String identificador;
    @ManyToOne(optional=false)
    @JoinColumn(name = "BICICLETA_ID", referencedColumnName = "identificador")
    private Bicicleta bicicleta;
    @Column(name = "fechaInicio")
    private LocalDate fechaInicio;
    @Column(name = "horaInicio")
    private LocalTime horaInicio;
    @Column(name = "fechaFin")
    private LocalDate fechaFin;
    @Column(name = "horaFin")
    private LocalTime horaFin;
    @ManyToOne(optional=false)
    @JoinColumn(name = "usuario_nif", referencedColumnName = "nif")
    private Usuario usuario;

    public Alquiler() {
        
    }

    /**
     * Constructor de la clase alquiler. 
     * @param bicicleta bicicleta que se va a alquilar. No puede ser null.
     * Debe estar disponible o reservada
     * @param usuario usuario que realiza el alquiler. No puede ser null.
     * Debe estar activo.
     * @throws IllegalArgumentException si bicicleta o usuario es null.
     * @throws IllegalStateException si la bicicleta está bloqueada u ocupada
     * @throws IllegalStateException si el usuario no está activo
     */
    public Alquiler( Bicicleta bicicleta, Usuario usuario){
       
        setUsuario(usuario);
        setBicicleta(bicicleta);
        this.fechaInicio=LocalDate.now();
        this.horaInicio=LocalTime.now();
        this.usuario=usuario;
        this.identificador=UUID.randomUUID().toString();
    }

    /**
     * Método que consulta el identificador de una biccileta
     * @return String identificador
     */
    public String getIdentificador() {
        return identificador;
    }

    /**
     * Método que consulta la bicicleta del alquiler
     * @return Bicicleta alquilada
     */
    public Bicicleta getBicicleta() {
        return bicicleta;
    }   

    /**
     * Método que consulta la fecha de inicio del alquiler
     * @return fecha de inicio de alquiler
     */
    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    /**
     * Método que consulta la hora de inicio del alquiler
     * @return hora de inicio de alquiler
     */
    public LocalTime getHoraInicio() {
        return horaInicio;
    }
    
    /**
     * Método que consulta la fecha de fin del alquiler
     * @return fecha fin de alquiler
     */
    public LocalDate getFechaFin() {
        return fechaFin;
    }

    /**
     * Método que consulta la hora de fin del alquiler
     * @return hora fin del alquiler
     */
    public LocalTime getHoraFin() {
       return horaFin;
    }

    /**
     * Método que consulta el usuario que hizo el alquiler
     * @return Usuario que hizo el aquiler
     */
    public Usuario getUsuario() {
        return usuario;
    }

    /**Método que modifica el usuario del aqluiler
     * @Param usuario
     * @Throw IllegalArgumentException si el usuario es nulo o está inactivo
     * 
     */
    private void setUsuario(Usuario usuario){
       if(usuario==null) throw new IllegalArgumentException();
        if(!usuario.isActivo()) throw new IllegalStateException();
        this.usuario = usuario;
        usuario.addAlquiler(this);
    }
    
    /**
     * Método que modifica la bicicleta del alquiler
     * 
     * @param bici
     * @throw IllegalArgumentException si la bicicleta es nula o no está disponible
     */
    private void setBicicleta (Bicicleta bicicleta){
        if (bicicleta == null) throw new IllegalArgumentException();
        if( bicicleta.getEstado()== EstadoBicicleta.BLOQUEADA || bicicleta.getEstado() == EstadoBicicleta.OCUPADA)
            throw new IllegalStateException();
        this.bicicleta = bicicleta;
        bicicleta.setEstado(EstadoBicicleta.OCUPADA);
        bicicleta.addAlquiler(this);
    }

    /**
     * Indica la fecha de fin del alquiler
     * @param fechaFin fecha de finalización del alquiler. No puede ser null. Debe ser igual
     * o posterior a la fecha de inicio de alquiler
     * @throws IllegalArgumentException si {@code fechaFin == null}
     * @throws IllegalStateException si fechaFin es anterior a la fechaInicio del alquiler
     */
     public void setFechaFin(LocalDate fechaFin) {
        if (fechaFin == null) throw new IllegalArgumentException();
        if (fechaFin.isBefore(fechaInicio)) throw new IllegalStateException();
        this.fechaFin = fechaFin;
    }

    /**
     * Método que modifica la hora de fin del alquiler
     * @param horaFin hora de finalización del alquiler. No puede ser null.
     * @throws IllegalArgumentException si {@code horaFin == null}
     */
    public void setHoraFin(LocalTime horaFin) {
        if (horaFin == null) throw new IllegalArgumentException();
        this.horaFin = horaFin;
    }


}
