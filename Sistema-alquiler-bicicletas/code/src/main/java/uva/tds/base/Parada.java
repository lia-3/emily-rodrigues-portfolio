package uva.tds.base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.*;

/**
 * Clase que representa una parada de nuestro sistema de alquiler de bicicletas.
 * Dispone de un identificador, una ubicación (basada en latitud y longitud),
 * una dirección, bicicletas disponibles y aparcamientos disponibles y un estado
 * (activa o inactiva)
 * 
 * Se puede añadir y eliminar bicicletas.
 * 
 * Igualmente se puede modificar el estado de las bicicletas que se encuentran
 * en la parada
 * 
 * @author Ainhoa Carbajo
 * @author Emily Rodrigues
 */

@Entity
@Table(name = "PARADAS")
public class Parada {

    static final double LATITUD_MINIMA = -90.0;
    static final double LATITUD_MAXIMA = 90.0;
    static final double LONGITUD_MINIMA = -180.0;
    static final double LONGITUD_MAXIMA = 180.0;
    static final int DIR_MAXIMA = 20;

    @Id
    @Column(name = "identificador", nullable = false, unique = true, length = 50)
    private String identificador;

    @Column(name = "latitud", nullable = false)
    private double latitud;

    @Column(name = "longitud", nullable = false)
    private double longitud;

    @Column(name = "direccion", nullable = false, length = 20)
    private String direccion;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = false )
    private List<Bicicleta> bicicletas;

    @Column(name = "aparcamientos", nullable = false)
    private int aparcamientos;

    @Column(name = "activa", nullable = false)
    private boolean activa;

    public Parada() {
    }

    /**
     * Crea una nueva parada. La parada pasa a estar activa
     * 
     * @param identificador El identificador de la parada. Debe tener al menos 1
     *                      carácter. No puede ser null.
     * @param latitud       La latitud de la ubicación de la parada. Debe estar
     *                      entre -90.0 y 90.0. Se aplica un decimal.
     * @param longitud      La longitud de la ubicación de la parada. Debe estar
     *                      entre -180 y 180. Se aplica un decimal
     * @param direccion     La dirección de la parada. No puede ser null. Debe tener
     *                      entre 1 y 20 caracteres
     * @param bicicletas    Lista de las bicicletas de la parada. No puede ser null.
     * @param aparcamientos Número de aparcamientos disponibles.
     * @throws IllegalArgumentException si el identificador, la dirección o la lista
     *                                  de bicicletas es null.
     * @throws IllegalArgumentException si identificador es vacío o sólo contiene
     *                                  espacios en blanco
     * @throws IllegalArgumentException si latitud es menor que -90 o mayor que 90
     * @throws IllegalArgumentException si longitud es menor que -180 o mayor que
     *                                  180
     * @throws IllegalArgumentException si direccion es vacío o sólo contiene
     *                                  espacios en blanco
     * @throws IllegalArgumentException si direccion tiene más de 20 caracteres
     * @throws IllegalArgumentException si aparcamientos es menor que cero
     * @throws IllegalStateException    si bicicletas tiene un tamaño mayor que
     *                                  aparcamientos
     * @throws IllegalStateException    si hay bicicletas repetidas el la lista de
     *                                  bicicletas.
     */
    public Parada(String identificador, double latitud, double longitud, String direccion,
            ArrayList<Bicicleta> bicicletas, int aparcamientos, boolean activa) {
        if ((identificador == null) || (direccion == null) || (bicicletas == null)) {
            throw new IllegalArgumentException();
        }
        if (aparcamientos < 0)
            throw new IllegalArgumentException();
        if (bicicletas.size() > aparcamientos) {
            throw new IllegalStateException();
        }
        if (bicicletas.stream().anyMatch(i -> Collections.frequency(bicicletas, i) > 1))
            throw new IllegalStateException();
        setIdentificador(identificador);
        setLatitud(latitud);
        setLongitud(longitud);
        setDireccion(direccion);
        this.aparcamientos = aparcamientos;
        this.bicicletas = bicicletas;

        setEstado(activa);
    }

    /**
     * Método que devuelve el identificador de la parada
     * 
     * @return String id de la parada
     */
    public String getIdentificador() {
        return identificador;
    }

    /**
     * Método que devuelve la latitud de la parada
     * 
     * @return double latitud de la parada
     */
    public double getLatitud() {
        return latitud;
    }

    /**
     * Método que devuelve la longitud de la ubicación
     * de la parada
     * 
     * @return double longitud
     */
    public double getLongitud() {
        return longitud;
    }

    /**
     * Método que devuelve la dirección de la parada
     * 
     * @return String dirección
     */
    public String getDireccion() {
        return direccion;
    }

    /**
     * Método que devuelve el número de
     * biciletas disponibles
     * 
     * @return int biciletas disponibles
     */
    public int getNumeroBicicletasDisponibles() {
        int disponibles = 0;
        for (Bicicleta bici : bicicletas)
            if (bici.isDisponible())
                disponibles++;
        return disponibles;
    }

    /**
     * Método que devuelve la lista bicicletas disponibles, puede estar vacía la
     * lista si
     * no hay bicicletas disponibles en la parada.
     * 
     * @return ArrayList biciletas disponibles, puede estar vacía la lista si
     *         no hay bicicletas disponibles.
     */
    public ArrayList<Bicicleta> getListaBicicletasDisponibles() {
        return bicicletas.stream().filter(bicicleta -> !bicicleta.isOcupada())
                .collect(Collectors.toCollection(ArrayList::new));

    }

    /**
     * Método que devuelve la lista de todas las bicicletas, puede estar vacía
     * la lista si no hay bicicletas en la parada.
     * 
     * @return ArrayList biciletas, puede estar vacía la lista si no hay bicicletas.
     */
    public ArrayList<Bicicleta> getListaBicicletas() {
        return new ArrayList<>(bicicletas);

    }

    /**
     * Consulta la bicicleta de la parada a partir de un identificador que
     * debe existir en la parada.
     * 
     * @param idBicicleta identificador de la bicicleta. No puede ser null.
     * @return bicicleta que se consulta a partir del identificador.
     * @throws IllegalArgumentException si idBicicleta es null.
     * @throws IllegalStateException    si no se encuentra una bicicleta.
     *                                  en la parada con el identificador dado
     *                                  (idBicicleta).
     */
    public Bicicleta getBicicleta(String idBicicleta) {
        int i = devuelveIndiceBicicletaAPartirDeSuIdentificador(idBicicleta);
        return bicicletas.get(i).clone();
    }

    /**
     * Método que devuelve el número de aparcamientos disponibles (vacíos)
     * 
     * @return int aparcamientos disponibles
     */
    public int getAparcamientosDisponibles() {
        return aparcamientos - getNumeroBicicletasDisponibles();
    }

    /**
     * Método que devuelve el número de aparcamientos totales existentes en la
     * parada.
     * 
     * @return int aparcamientos totales
     */
    public int getAparcamientos() {
        return aparcamientos;
    }

    /**
     * Método que comprueba si la parada está llena, es decir, todos sus
     * aparcamientos
     * tienen una bicicleta.
     * 
     * @return true si lo está, false en caso contrario
     */
    public boolean isLlena() {
        return getAparcamientos() - getNumeroBicicletasDisponibles() == 0;
    }

    /**
     * Consulta si una parada está activa.
     * 
     * @return boolean true (activa) o false (inactiva)
     */
    public boolean isActiva() {
        return activa;
    }

    /**
     * Método que modifica el identificador de una parada
     * 
     * @param identificador identificador de la parada. No puede ser null. Debe
     *                      tener al menos un carácter.
     * @throws IllegalArgumentException si identificador es null.
     * @throws IllegalArgumentException si identificador es vacío o sólo contiene
     *                                  espacios en blanco
     */
    public void setIdentificador(String identificador) {
        if (identificador == null)
            throw new IllegalArgumentException();
        if (identificador.isBlank()) {
            throw new IllegalArgumentException();
        }
        this.identificador = identificador;
    }

    /**
     * Método que modifica la latitud de la ubicación de la parada
     * 
     * @param latitud entre -90.0 y 90.0
     * @throws IllegalArgumentException si latitud es menor que -90 o mayor que 90
     */
    public void setLatitud(double latitud) {
        if (latitud < LATITUD_MINIMA || latitud > LATITUD_MAXIMA) {
            throw new IllegalArgumentException();
        }
        this.latitud = latitud;
    }

    /**
     * Método que modifica la longitud de la ubicación
     * de la parada
     * 
     * @param longitud entre -180.0 y 180.0
     * @throws IllegalArgumentException si longitud es menor que -180 o mayor que
     *                                  180
     */
    public void setLongitud(double longitud) {
        if (longitud < LONGITUD_MINIMA || longitud > LONGITUD_MAXIMA) {
            throw new IllegalArgumentException();
        }
        this.longitud = longitud;
    }

    /**
     * Método que modifica la dirección de una parada
     * 
     * @param direccion debe tener entre 1 y 20 caracteres
     * @throws IllegalArgumentException si direccion es null
     * @throws IllegalArgumentException si direccion es vacío o sólo contiene
     *                                  espacios en blanco
     * @throws IllegalArgumentException si direccion tiene más de 20 caracteres
     */
    public void setDireccion(String direccion) {
        if (direccion == null)
            throw new IllegalArgumentException();
        if (direccion.isBlank()) {
            throw new IllegalArgumentException();
        }
        if (direccion.length() > DIR_MAXIMA) {
            throw new IllegalArgumentException();
        }
        this.direccion = direccion;
    }

    /**
     * Método que modifica el estado de la parada
     * 
     * @param estado true (activa) or false (inactiva)
     */
    public void setEstado(boolean estado) {
        this.activa = estado;
    }

    /**
     * Modifica el estado de una bicicleta que está en la parada a DISPONIBLE
     * mediante
     * su identificador
     * 
     * @param idBicicleta identificador de la bicicleta, no puede ser null
     * @throws IllegalArgumentException si idBicicleta es null
     * @throws IllegalStateException    si no se encuentra una bicicleta con ese
     *                                  identificador
     *                                  en la parada
     */
    public void setBicicletaEstadoDisponible(String idBicicleta) {
        int indiceBiciCambiar = devuelveIndiceBicicletaAPartirDeSuIdentificador(idBicicleta);
        bicicletas.get(indiceBiciCambiar).setEstadoDisponible();
    }

    /**
     * Modifica el estado de una bicicleta que está en la parada a RESERVADA
     * mediante
     * su identificador
     * 
     * @param idBicicleta identificador de la bicicleta, no puede ser null
     * @throws IllegalArgumentException si idBicicleta es null
     * @throws IllegalStateException    si no se encuentra una bicicleta con ese
     *                                  identificador
     *                                  en la parada
     */
    public void setBicicletaEstadoReservada(String idBicicleta) {
        int indiceBiciCambiar = devuelveIndiceBicicletaAPartirDeSuIdentificador(idBicicleta);
        bicicletas.get(indiceBiciCambiar).setEstadoReservada();
    }

    /**
     * Modifica el estado de una bicicleta que está en la parada a BLOQUEADA
     * mediante
     * su identificador
     * 
     * @param idBicicleta identificador de la bicicleta, no puede ser null
     * @throws IllegalArgumentException si idBicicleta es null
     * @throws IllegalStateException    si no se encuentra una bicicleta con ese
     *                                  identificador
     *                                  en la parada
     */
    public void setBicicletaEstadoBloqueada(String idBicicleta) {
        int indiceBiciCambiar = devuelveIndiceBicicletaAPartirDeSuIdentificador(idBicicleta);
        bicicletas.get(indiceBiciCambiar).setEstadoBloqueada();
    }

    /**
     * Consulta si una bicicleta está en la parada a partir de su identificador
     * 
     * @param idBicicleta el identificador de la parada, no puede ser null
     * @return true si se ha encontrado una bicicleta con ese identificador, false
     *         en caso contrario
     * @throws IllegalArgumentException si idBicicleta es null
     */
    public boolean isBicicletaEnParada(String idBicicleta) {
        if (idBicicleta == null)
            throw new IllegalArgumentException();
        for (Bicicleta b : bicicletas) {
            if (b.getIdentificador().equals(idBicicleta)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Añade una bicicleta a la parada. Esta no puede encontrarse ya en la parada
     * 
     * @param bicicleta la bicicleta que se quiere añadir, no puede ser null
     * @throws IllegalArgumentException si bicicleta es null
     * @throws IllegalStateException    si ya estaba en la parada la bicicleta que
     *                                  se
     *                                  quiere añadir
     * @throws IllegalStatetException   si la parada está llena
     */
    public void agregaBicicleta(Bicicleta bicicleta) {
        if (bicicleta == null)
            throw new IllegalArgumentException();
        if (bicicletas.contains(bicicleta))
            throw new IllegalArgumentException();
        if (isLlena())
            throw new IllegalStateException();
        bicicleta.setEstadoDisponible();
        bicicletas.add(bicicleta);
        bicicleta.setParada(this);

    }

    /**
     * Elimina una bicicleta a partir de su identificador
     * 
     * @param idBicicleta identificador de la bicicleta que se quiere eliminar,
     *                    no puede ser null
     * @return la bicicleta eliminada
     * @throws IllegalArgumentException si idBicicleta es null
     * @throws IllegalStateException    si no se encuentra una bicicleta con ese
     *                                  identificador en la parada
     */
    public void eliminaBicicleta(String idBicicleta) {
        int indiceEliminar = devuelveIndiceBicicletaAPartirDeSuIdentificador(idBicicleta);
        bicicletas.get(indiceEliminar).setParada(null);
        bicicletas.remove(indiceEliminar);

    }

    /**
     * Devuelve el índice de una bicicleta en la lista de bicicletas a partir de su
     * identificador.
     *
     * @param idBicicleta el identificador de la bicicleta. No puede ser
     *                    {@code null}.
     * @return el índice de la bicicleta en la lista.
     * @throws IllegalArgumentException si {@code idBicicleta} es {@code null}.
     * @throws IllegalStateException    si no se encuentra una bicicleta con el
     *                                  identificador proporcionado.
     */

    private int devuelveIndiceBicicletaAPartirDeSuIdentificador(String idBicicleta) {
        if (idBicicleta == null)
            throw new IllegalArgumentException();
        for (Bicicleta b : bicicletas) {
            if (b.getIdentificador().equals(idBicicleta)) {
                return bicicletas.indexOf(b);
            }
        }
        throw new IllegalStateException();
    }

    /**
     * Consulta si dos paradas son iguales
     * 
     * @return true si las paradas tiene mismo identificador, latitud
     *         y longitud. Devuelve false en caso contrario
     */
    @Override
    public boolean equals(Object p) {
        if (p == null)
            return false;
        if (p == this)
            return true;
        if (p.getClass() != Parada.class)
            return false;
        Parada nuevaParada = (Parada) p;
        return (getIdentificador().equals(nuevaParada.getIdentificador()));

    }
}
