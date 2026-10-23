package uva.tds.base;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Clase que gestiona el bloqueo de una bicicleta
 * Guarda la fecha y hora de inicio y fin del bloqueo, así como la bicicleta involucrados
 * @author Ainhoa Carbajo Orgaz
 * @author Emily Rodrigues
 */
@Entity
@Table(name="BLOQUEOS")
public class Bloqueo {
    @Id
    @Column(name="identificador")
    private String identificador;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "bicicleta",referencedColumnName = "identificador")
    private Bicicleta bicicleta;
    @Column(name = "fechaInicio")
    private LocalDate fechaInicio;
    @Column(name = "horaInicio")
    private LocalTime horaInicio;
    @Column(name = "fechaFin")
    private LocalDate fechaFin;
    @Column(name = "horaFin")
    private LocalTime horaFin;


    public Bloqueo() {
        
    }


    /**
     * Constructor de la clase bloqueo. 
     * @param bicicleta bicicleta que se va a bloquear. No puede ser null. 
     * Debe estar disponible.
     * @throws IllegalArgumentException si bicicleta es null
     * @throws IllegalStateException si bicicleta no está disponible
     */
    public Bloqueo(Bicicleta bicicleta){
        if (bicicleta == null) throw new IllegalArgumentException();
        if(!bicicleta.isDisponible())
            throw new IllegalStateException();
        identificador = UUID.randomUUID().toString();
        this.bicicleta=bicicleta.clone();
        this.bicicleta.setEstadoBloqueada();
        fechaInicio=LocalDate.now();
        horaInicio=LocalTime.now();
    }

    /**
     * Método que consulta la bicicleta del bloqueo
     * @return bicicleta bloqueada
     */
    public Bicicleta getBicicleta() {
        return bicicleta.clone();
    } 
    
    /**
     * Método que consulta la fecha de inicio del bloqueo
     * @return fecha de inicio del bloqueo
     */
    public LocalDate getFechaInicio() {
        return fechaInicio;
    }


    /**
     * Método que consulta la hora de inicio del bloqueo
     * @return hora de inicio del bloqueo
     */
    public LocalTime getHoraInicio() {
        return horaInicio;
    }
    
    /**
     * Método que consulta la fecha de fin del bloqueo
     * @return fecha fin del bloqueo
     */
    public LocalDate getFechaFin() {
        return fechaFin;
    }

    /**
     * Método que consulta la hora de fin del bloqueo
     * @return hora fin del bloqueo
     */
    public LocalTime getHoraFin() {
       return horaFin;
    }

     /**
     * Método que modifica la fecha de fin del bloqueo
     * @param fechaFin fecha en la que se finaliza el bloqueo. No puede
     * ser null. No puede ser anterior a la fecha de inicio del bloqueo.
     * @throws IllegalArgumentException si {@code fechaFin == null}
     * @throws IllegalStateException si fechaFin es anterior a la fecha de inicio
     * del bloqueo
     */
    public void setFechaFin(LocalDate fechaFin) {
        if (fechaFin == null) throw new IllegalArgumentException();
        if (fechaFin.isBefore(fechaInicio)) throw new IllegalStateException();
        this.fechaFin = fechaFin;
    }

    /**
     * Método que modifica la hora de fin del bloqueo
     * @param horaFin hora del día en la que se finaliza el bloqueo. No 
     * puede ser null.
     * @throws IllegalArgumentException si {@code horaFin == null}
     */
    public void setHoraFin(LocalTime horaFin) {
        if (horaFin == null) throw new IllegalArgumentException();
        this.horaFin = horaFin;
    }
}
