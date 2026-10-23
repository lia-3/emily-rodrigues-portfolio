package uva.tds.base;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

/**
 * Clase que representa una bicicleta en el sistema de alquiler de bicicletas.
 * Actualmente se distinguen dos tipos de bicicletas:
 * - Bicicletas normales: no funcionan con electricidad, también son conocidas
 * como
 * bicicletas urbanas.
 * - Bicicletas eléctricas: Funcionan con electricidad y tienen un nivel de
 * batería asociado.
 * 
 * Cada bicicleta tiene un identificador único, un tipo, un estado y, si es
 * eléctrica,
 * un nivel de batería.
 * 
 * La clase cuenta con métodos para consultar y modificar ciertos valores.
 * 
 * @author Emily Rodrigues
 */

@Entity
@Table(name = "BICICLETAS")
public class Bicicleta implements Cloneable {

    @Id
    @Column(name = "identificador", nullable = false, unique = true, length = 6)
    private String identificador;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false, length = 10)
    private TipoBicicleta tipo;

    @Column(name = "nivel_bateria")
    private int nivelBateria;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 15)
    private EstadoBicicleta estado = EstadoBicicleta.DISPONIBLE;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Alquiler> alquileres;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Reserva> reservas;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "parada_id", referencedColumnName = "identificador")
    private Parada parada;

    public Bicicleta() {
    }

    /**
     * Crea una bicicleta normal disponible a partir de su identificador
     * 
     * @param identificador String que permite identificar la bicicleta. No puede
     *                      ser null y debe tener una longitud de uno a seis
     *                      caracteres, ambos valores incluidos
     * @throws IllegalArgumentException si identificador == null
     * @throws IllegalArgumentException si el identificador no tiene una longitud de
     *                                  uno o seis
     *                                  caracteres, ambos valores incluídos
     */
    public Bicicleta(String identificador) {
        setIdentificador(identificador);
        setTipo(TipoBicicleta.NORMAL);
        alquileres = new ArrayList<>();
        reservas = new ArrayList<>();
    }

    /**
     * Crea una bicicleta normal a partir de un identificador y un estado
     * 
     * @param identificador String que permite identificar la bicicleta. No puede
     *                      ser null y debe tener una longitud de uno a seis
     *                      caracteres, ambos valores incluidos
     * @param estado        EstadoBicicleta que tiene la bicicleta al crearse. No
     *                      puede ser null
     * @throws IllegalArgumentException si identificador == null
     * @throws IllegalArgumentException si el identificador no tiene una longitud de
     *                                  uno o seis
     *                                  caracteres, ambos valores incluídos
     * @throws IllegalArgumentException si estado == null
     */
    public Bicicleta(String identificador, EstadoBicicleta estado) {
        this(identificador);
        setEstado(estado);
        alquileres = new ArrayList<>();
        reservas = new ArrayList<>();
    }

    /**
     * Crea una bicicleta eléctrica a partir de un identificador y un nivel de
     * batería
     * 
     * @param identificador String que permite identificar la bicicleta. No puede
     *                      ser null y debe tener una longitud de uno a seis
     *                      caracteres, ambos valores incluidos
     * @param nivelBateria  int con el nivel de batería de la bicicleta eléctrica.
     *                      Está
     *                      compredido en el rango de cero a cien, ambos valores
     *                      incluídos
     * @throws IllegalArgumentException si identificador == null
     * @throws IllegalArgumentException si el identificador no tiene una longitud de
     *                                  uno o seis
     *                                  caracteres, ambos valores incluídos
     * @throws IllegalArgumentExcpetion si el nivel de batería no se encuentra en el
     *                                  rango de
     *                                  cero a cien, ambos valores incluídos
     */
    public Bicicleta(String identificador, int nivelBateria) {
        setIdentificador(identificador);
        setTipo(TipoBicicleta.ELECTRICA);
        setNivelBateria(nivelBateria);
        alquileres = new ArrayList<>();
        reservas = new ArrayList<>();
    }

    /**
     * Crea una bicicleta eléctrica a partir de un identificador, un nivel de
     * batería y un
     * estado
     * 
     * @param identificador String que permite identificar la bicicleta. No puede
     *                      ser null y debe tener una longitud de uno a seis
     *                      caracteres, ambos valores incluidos
     * @param nivelBateria  int con el nivel de batería de la bicicleta eléctrica.
     *                      Está
     *                      compredido en el rango de cero a cien, ambos valores
     *                      incluídos
     * @param estado        EstadoBicicleta que tiene la bicicleta al crearse. No
     *                      puede ser null
     * @throws IllegalArgumentException si identificador == null
     * @throws IllegalArgumentException si el identificador no tiene una longitud de
     *                                  uno o seis
     *                                  caracteres, ambos valores incluídos
     * @throws IllegalArgumentExcpetion si el nivel de batería no se encuentra en el
     *                                  rango de
     *                                  cero a cien, ambos valores incluídos
     * @throws IllegalArgumentException si estado == null
     */
    public Bicicleta(String identificador, int nivelBateria, EstadoBicicleta estado) {
        this(identificador, nivelBateria);
        setEstado(estado);
        alquileres = new ArrayList<>();
        reservas = new ArrayList<>();
    }

    /**
     * Consulta el identificador de la bicicleta
     * 
     * @return String con el identificador
     */
    public String getIdentificador() {
        return identificador;
    }

    /**
     * Consulta si una bicicleta es normal
     * 
     * @return true en caso de serlo
     */
    public boolean isBicicletaNormal() {
        return tipo.equals(TipoBicicleta.NORMAL);
    }

    /**
     * Consulta si una bicicleta es eléctrica
     * 
     * @return true en caso de serlo
     */
    public boolean isBicicletaElectrica() {
        return tipo.equals(TipoBicicleta.ELECTRICA);
    }

    /**
     * Consulta el nivel de batería de una bicicleta eléctrica
     * 
     * @return int con el nivel de batería que está comprendido en el
     *         rango de cero a cien, ambos valores incluídos
     * @throws IllegalStateException si se consulta el nivel de batería
     *                               de una bicicleta normal
     */
    public int getNivelBateria() {
        if (isBicicletaNormal())
            throw new IllegalStateException();
        return nivelBateria;
    }

    /**
     * Consulta el estado de una bicicleta
     * 
     * @return EstadoBicicleta con el estado actual de la bicicleta
     */
    public EstadoBicicleta getEstado() {
        return estado;
    }

    /**
     * Modifica el estado de la bicicleta dentro del sistema
     * 
     * @param estado EstadoBicicleta al que se quiere actualizar
     *               la bicicleta. No puede ser null. Tampoco se puede hacer:
     * @throws IllegalArgumentException si estado == null
     */
    public void setEstado(EstadoBicicleta estado) {
        if (estado == null)
            throw new IllegalArgumentException();

        switch (estado) {
            case DISPONIBLE:
                setEstadoDisponible();
                break;
            case RESERVADA:
                setEstadoReservada();
                break;
            case OCUPADA:
                setEstadoOcupada();
                break;
            default:
                setEstadoBloqueada();
                break;
        }
    }

    /**
     * Cambia el estado de la bicicleta a disponible
     */
    public void setEstadoDisponible() {
        estado = EstadoBicicleta.DISPONIBLE;
    }

    /**
     * Cambia el estado de la bicicleta a reservada. No se puede realizar
     * este cambio cuando la bicicleta está bloqueada u ocupada
     */
    public void setEstadoReservada() {
        estado = EstadoBicicleta.RESERVADA;
    }

    /**
     * Cambia el estado de la bicicleta a ocupada.
     */
    public void setEstadoOcupada() {
        estado = EstadoBicicleta.OCUPADA;
    }

    /**
     * Cambia el estado de la bicicleta a bloqueada
     */
    public void setEstadoBloqueada() {
        estado = EstadoBicicleta.BLOQUEADA;
    }

    /**
     * Cambia el nivel de batería de las bicicletas eléctricas por uno dado
     * 
     * @param nivelBateria int con el nuevo nivel de batería. Comprende los valores
     *                     de
     *                     cero a cien, ambos incluídos
     * @throws IllegalArgumentException si nivelBateria tiene valores fuera del
     *                                  rango de
     *                                  cero a cien
     * @throws IllegalStateException    si intenta cambiar el nivel de batería de
     *                                  una bici normal
     */
    public void setNivelBateria(int nivelBateria) {
        if (isBicicletaNormal())
            throw new IllegalStateException();
        if ((nivelBateria < 0) || (nivelBateria > 100)) {
            throw new IllegalArgumentException();
        }
        this.nivelBateria = nivelBateria;
    }

    /**
     * Asigna una parada a esta entidad.
     *
     * @param parada la parada que se va a asociar a esta entidad.
     *               Puede ser null si se desea desasociar la parada.
     */

    public void setParada(Parada parada) {

        this.parada = parada;

    }

    /**
     * Consulta si una bicicleta tiene un estado disponible
     * 
     * @return true si está disponible, false en cualquier otro caso
     */
    public boolean isDisponible() {
        return getEstado().equals(EstadoBicicleta.DISPONIBLE);
    }

    /**
     * Consulta si una bicicleta tiene un estado ocupada
     * 
     * @return true si está ocupada, false en cualquier otro caso
     */
    public boolean isOcupada() {
        return getEstado().equals(EstadoBicicleta.OCUPADA);
    }

    /**
     * Consulta si una bicicleta tiene un estado reservada
     * 
     * @return true si está reservada, false en cualquier otro caso
     */
    public boolean isReservada() {
        return getEstado().equals(EstadoBicicleta.RESERVADA);
    }

    /**
     * Consulta si una bicicleta tiene un estado bloquedda
     * 
     * @return true si está bloqueada, false en cualquier otro caso
     */
    public boolean isBloqueada() {
        return getEstado().equals(EstadoBicicleta.BLOQUEADA);
    }

    /**
     * Obtiene la lista de alquileres asociados a esta entidad.
     *
     * @return una lista de objetos {Alquiler} asociados.
     *         Puede estar vacía si no hay alquileres asociados.
     */

    public List<Alquiler> getAlquileres() {
        return alquileres;
    }

    /**
     * Obtiene la lista de reservas asociadas a esta entidad.
     *
     * @return una lista de objetos {Reserva} asociados.
     *         Puede estar vacía si no hay reservas asociadas.
     */

    public List<Reserva> getReservas() {
        return reservas;
    }

    /**
     * Añade un alquiler a una bicicleta
     * 
     * @param Alquiler a añadir. No puede ser null.
     * @throws IllegalArgumentException si el alquiler es null.
     * @throws IllegalArgumentException si el alquiler ya ha sido añadido
     */
    public void addAlquiler(Alquiler alquiler) {
        if (alquiler == null)
            throw new IllegalArgumentException();
        if (alquileres.contains(alquiler))
            throw new IllegalArgumentException();
        alquileres.add(alquiler);

    }

    /**
     * Añade una reserva a una bicicleta
     * 
     * @param Reserva a añadir. No puede ser null.
     * @throws IllegalArgumentException si la reserva es null.
     * @throws IllegalArgumentException si la reserva ya ha sido añadida
     */
    public void addReserva(Reserva reserva) {
        if (reserva == null)
            throw new IllegalArgumentException();
        if (reservas.contains(reserva))
            throw new IllegalArgumentException();
        reservas.add(reserva);

    }

    /**
     * Comprueba si dos bicicletas son iguales
     * 
     * @return true si las bicicletas tienen el mismo identificador,
     *         false en caso contrario
     */
    @Override
    public boolean equals(Object otraBici) {
        if (otraBici == null)
            throw new IllegalArgumentException();
        Bicicleta nuevaBici = (Bicicleta) otraBici;
        return (getIdentificador().equals(nuevaBici.getIdentificador()));
    }

    /**
     * Genera una copia de la Bicleta
     * 
     * @return la copia de la Bicicleta exacta
     * @throws AssertionError cuando no se ha podido realizar
     *                        la clonación (no debería ocurrir)
     */
    @Override
    public Bicicleta clone() {
        try {
            Bicicleta copia = (Bicicleta) super.clone();
            return copia;
        } catch (CloneNotSupportedException e) {
            // esto nunca debería ocurrir
            throw new AssertionError();
        }
    }

    /* ----------- Elementos privados de clase ----------- */

    /*
     * IllegalArgumentException si identificador no está en el rango [1..6]
     * IllegalArgumentException si identificador == null
     */
    private void setIdentificador(String identificador) {
        if (identificador == null) {
            throw new IllegalArgumentException();
        }
        if (identificador.isEmpty() || identificador.length() > 6) {
            throw new IllegalArgumentException();
        }
        this.identificador = identificador;
    }

    /**
     * Establece el tipo de la bicicleta.
     *
     * @param tipo el tipo de bicicleta a asignar.
     *             Debe ser un valor de {@link TipoBicicleta}.
     */

    private void setTipo(TipoBicicleta tipo) {
        this.tipo = tipo;
    }

    /*
     * Identifica los posibles tipos de bicicleta que hay en el sistema: normal y
     * eléctricas
     */
    private enum TipoBicicleta {
        NORMAL,
        ELECTRICA
    }
}