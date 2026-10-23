package uva.tds.base;

import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Table;


/**
 * Clase que representa un usuario en un sistema de alquiler de bicicletas.
 * De un usuario se puede conocer su nombre dentro del sistema, su nif, su puntuación en
 * recompensas y su estado (activo o inactivo). 
 * Además un usuario podrá tener asociadas 0 más alquileres y recompensas
 * @author Marcos de Diego Martin
 * @author Emily Rodrigues
 * @author Ainhoa Carbajo
 */
@Entity
@Table(name="USUARIOS")
public class Usuario {

    @Column(name = "nombre", nullable = false, length = 15)
    private String nombre;
    @Id
    @Column(name = "nif", length = 9, nullable = false, unique = true)
    private String nif;
    @Column(name = "puntuacion")
    private int puntuacion;
    @Column(name = "estado")
    private boolean activo;
    @OneToMany( cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<Recompensa> recompensas;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<Alquiler> alquileres;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<Reserva> reservas;

    private final String letras = "TRWAGMYFPDXBNJZSQVHLCKE";
    private final int numeroLetras= 23;
    
    public Usuario(){

    }

   

    /**
     * Crea un usuario del sistema. Un usuario se caracteriza por tener un nombre, 
     * un NIF español, la puntuación que tiene actualmente, y un estado (activo o inactivo).
     * Se inicializa con la lista de recompensas vacía.
     * @param nombre El nombre del usuario. No puede ser null. Debe tener una longitud entre
     * 1 y 15 caracteres, ambos valores incluídos.
     * @param nif El NIF del usuario. No puede ser null. Debe seguir el formato correcto de
     * un DNI en España, es decir, debe contar con ocho dígitos y un carácter de control, por lo
     * tanto nif debe tener una longitud de 9 caracteres. Este caracter se obtiene a partir del 
     * numero completo del DNI dividido entre el numero 23. Al resto resultante de dicha division,
     * que esta comprendido entre 0 y 22, se le asigna la letra de control segun una equivalencia.
     *
     * @param puntuacion La puntuación de recompensa del usuario. No puede ser negativa.
     * @param activo El estado del usuario. Puede estar activo (estado = true) o inactivo (estado = false).
     * @throws IllegalArgumentException Si nombre o nif son null.
     * @throws IllegalArgumentException si la longitud de nombre no se encuentra en el rango [1..15]
     * @throws IllegalArgumentException si la longitud de nif no es de nueve caracteres
     * @throws IllegalArgumentException si los 8 primeros caracteres del nif no son dígitos.
     * @throws IllegalArgumentException si el último carácter de nif no es una letra mayúscula.
     * @throws IllegalArgumentException si el carácter de control no es válido, siguiendo la referencia.
     * @throws IllegalArgumentException si la puntuacion es menor que cero
     */
    public Usuario(String nombre, String nif, int puntuacion, boolean activo) {
        setNombre(nombre);
        setNif(nif);
        setPuntuacion(puntuacion);
        setEstado(activo);
        recompensas= new ArrayList<>();
        alquileres = new ArrayList<>();
        reservas= new ArrayList<>();
    }

    /**
     * Consulta el nombre del usuario.
     * @return El nombre del usuario.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Consulta el NIF del usuario.
     * @return El NIF del usuario.
     */
    public String getNif() {
        return nif;
    }

    /**
     * Consulta la puntuación de recompensa del usuario.
     * @return La puntuación de recompensa del usuario.
     */
    public int getPuntuacion() {
        return puntuacion;
    }

    /**
     * Consulta el estado del usuario (true si esta activo o false si esta inactivo).
     * @return true si está activo, false si está inactivo.
     */
    public boolean isActivo() {
        return activo;
    }

     /**
     * Establece el nombre del usuario.
     * @param nombre El nombre del usuario. No puede ser null. Debe tener una longitud entre
     * 1 y 15 caracteres, ambos valores incluídos.
     * @throws IllegalArgumentException Si nombre es null.
     * @throws IllegalArgumentException si la longitud de nombre no se encuentra en el rango [1..15]
     */
    public void setNombre(String nombre){
        if (nombre == null) throw new IllegalArgumentException();
        if (nombre.length() < 1 || nombre.length() > 15) {
            throw new IllegalArgumentException("El nombre debe tener entre 1 y 15 caracteres.");
        }
        this.nombre = nombre;
    }


    /**
     * Método que modifica el nif de un usuario
     * @param nif String
     * @throws IllegalArgumentException si el dni es nulo
     * @throws IllegalArgumentException si la longitud del nif es distinta de 9
     */
    private void setNif(String nif){
        if (nif == null) throw new IllegalArgumentException();
        if (nif.length() != 9) {
            throw new IllegalArgumentException("El dni introducido es incorrecto.");
        }

        String numeros = nif.substring(0, 8);
        char letra = nif.charAt(8);
        char letraEsperada = letras.charAt(Integer.parseInt(numeros) % numeroLetras);
        if(!(letra == letraEsperada)){
            throw new IllegalArgumentException("El dni introducido es incorrecto.");
        }

        this.nif=nif;
    }

    /**
     * Establece la puntuación de recompensa del usuario.
     * La puntuación no puede ser negativa.
     * @param puntuacion La puntuación de recompensa.
     * @throws IllegalArgumentException Si la puntuación es negativa.
     */
    public void setPuntuacion(int puntuacion) {
        if (puntuacion < 0) {
            throw new IllegalArgumentException("La puntuación no puede ser negativa.");
        }
        this.puntuacion = puntuacion;
    }

    /**
     * Establece el estado del usuario (activo o inactivo).
     * @param activo El estado del usuario. Si estado es true indica que el está es ACTIVO,
     * si es false indica que es INACTIVO
     */
    public void setEstado(boolean activo){
        this.activo=activo;
    }

    /**
     * Método que consulta la lista de recompensas de un usuario
     * @return ArrayList recompensas
     */
    public List<Recompensa> getRecompensas() {
        return recompensas;
    }

    
    /**
     * Método que consulta la lista de alquileres de un usuario
     * @return ArrayList alquileres
     */
    public List<Alquiler> getAlquileres() {
        return alquileres;
    }

    /**
     * Método que consulta la lista de reservas de un usuario
     * @return ArrayList reservas
     */
    public List<Reserva> getReservas() {
        return reservas;
    }

  

    @Override
	public int hashCode() {
		return Objects.hash(nif, nombre, puntuacion,activo);
	}

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;

        Usuario otro = (Usuario) obj;
        return (this.nif.equals(otro.getNif()));
        

    }


     /**
     * Añade una reocmpensa a un usaurio
     * @param Recompensa recompensa a añadir. No puede ser null. 
     * @throws IllegalArgumentException si la recompensa es null.
     * @throws IllegalStateException si el usuario no tiene suficientes puntos para
     * obtener la recompensa
     * @throws IllegalArgumentException si la recompensa esta inactiva o ya ha sido añadida
     */
    public void addRecompensa(Recompensa recompensa) {
        if (!recompensaValida(recompensa)) throw new IllegalArgumentException();
        if (getPuntuacion() < recompensa.getPuntuacion()) {
            throw new IllegalStateException();
        }
        setPuntuacion(getPuntuacion() - recompensa.getPuntuacion());
        recompensas.add(recompensa);
        recompensa.setUsuario(this);
    }

    private boolean recompensaValida(Recompensa recompensa){
        return recompensa != null &&
        recompensa.getEstado() &&
        !recompensas.contains(recompensa);
    }
    
     /**
     * Añade un alquiler a un usaurio
     * @param Alquiler a añadir. No puede ser null. 
     * @throws IllegalArgumentException si el alquiler es null.
     *@throws IllegalArgumentException si el alquiler ya ha sido añadido
     */
    public void addAlquiler(Alquiler alquiler) {
        if (alquiler == null) throw new IllegalArgumentException();
        if (alquileres.contains(alquiler)) throw new IllegalArgumentException();
        alquileres.add(alquiler);
        
        
    }

     /**
     * Añade una reserva a un usaurio
     * @param Reserva a añadir. No puede ser null. 
     * @throws IllegalArgumentException si la reserva es null.
     *@throws IllegalArgumentException si la reserva ya ha sido añadida
     */
    public void addReserva(Reserva reserva) {
        if (reserva == null) throw new IllegalArgumentException();
        if (reservas.contains(reserva)) throw new IllegalArgumentException();
        reservas.add(reserva);
        
        
    }

    /**
     * Elimina una reserva a un usuario.
     * Si la reserva no ha sido añadida no se produce ningñun cambio
     * @param identificador de la reserva a eliminar. No puede ser null. 
     * @throws IllegalArgumentException si el id de la reserva es null.
     */
    public void eliminarReserva(String id) {
        if (id == null) throw new IllegalArgumentException();
                reservas.removeIf(r-> r.getIdentificador().equals(id));
               
    }

    

}
