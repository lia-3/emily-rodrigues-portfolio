package uva.tds.base;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;



/**
 * Clase que representa una Recompensa en un sistema de alquiler de bicicletas.
 * De una recompensa se puede conocer su identificador, nombre, puntuación y estado.
 * @author Marcos de Diego Martin
 * @author Emily Rodrigues
 * @author Ainhoa Carbajo Orgaz
 */
@Entity
@Table(name="RECOMPENSAS")
public class Recompensa {
    @Id
    @Column(name = "id", length = 6, nullable = false, unique = true)
    private String id;
    @Column(name = "nombre", nullable = false, length = 20)
    private String nombre;
    @Column(name = "puntuacion")
    private int puntuacion;
    @Column(name = "estado")
    private boolean estado;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "usuario_nif",referencedColumnName = "nif")
    private Usuario usuario;
    
    public Recompensa(){

    }
    /**
     * Constructor de la clase Recompensa. Una recompensa se caracteriza por tener un identificador,
     * nombre, una puntuación y un estado.
     * @param identificador El identificador de la recompensa. No puede ser null. Debe tener entre 1
     * y 6 caracteres, ambos valores incluidos.
     * @param nombre El nombre de la recompensa. No puede ser null. Debe tener entre 1 y 20 caracteres, 
     * ambos valores incluidos.
     * @param puntuacion La puntuación necesaria para obtener la recompensa. Debe ser mayor que 0.
     * @param estado El estado de la recompensa. Puede estar ACTIVA ({@code estado = true}) o INACTIVA
     * ({@code estado = false}).
     * @throws IllegalArgumentException si identificador es null
     * @throws IllegalArgumentException si identificador no tiene una longitud en el rango de [1..6]
     * @throws IllegalArgumentException si nombre es null
     * @throws IllegalArgumentException si nombre no tiene una longitud en el rango de [1..20]
     * @throws IllegalArgumentException si puntuacion es menor o igual que cero
     */

    public Recompensa(String id, String nombre, int puntuacion, boolean estado) {
        setId(id);
        setNombre(nombre);
        setPuntuacion(puntuacion);
        setEstado(estado);
    }

    
    /**
     * Obtiene el identificador de la recompensa.
     * @return El identificador de la recompensa.
     */
    public String getId() {
        return id;
    }

    /**
     * Obtiene el estado actual de la recompensa.
     * 
     * @return El estado de la recompensa (true está activa, false desactivada).
     */

     public boolean getEstado() {
        return estado;
    }

    /**
     * Método que consulta el usuario al que está asignada la recompensa
     * @return
     */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * Establece el identificador de la recompensa.
     * @param identificador El identificador de la recompensa. No puede ser null. Debe tener entre 1
     * y 6 caracteres, ambos valores incluidos.     
     * @throws IllegalArgumentException si identificador es null
     * @throws IllegalArgumentException si identificador no tiene una longitud en el rango de [1..6]
     */
    public void setId(String identificador) {
        if (identificador == null) throw new IllegalArgumentException();
        if (identificador.length() < 1 || identificador.length() > 6) {
            throw new IllegalArgumentException("El identificador debe tener entre 1 y 6 caracteres.");
        }
        this.id = identificador;
    }


    /**
     * Obtiene el nombre de la recompensa.
     * @return El nombre de la recompensa de tipo string.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre de la recompensa.
     * @param nombre El nombre de la recompensa. No puede ser null. Debe tener entre 1 y 20 caracteres, 
     * ambos valores incluidos.
     * @throws IllegalArgumentException si nombre es null
     * @throws IllegalArgumentException si nombre no tiene una longitud en el rango de [1..20]
     */
    public void setNombre(String nombre) {
        if (nombre == null) throw new IllegalArgumentException();
        if (nombre.length() < 1 || nombre.length() > 20) {
            throw new IllegalArgumentException("El nombre debe tener entre 1 y 20 caracteres.");
        }
        this.nombre = nombre;
    }

    /**
     * Obtiene la puntuación necesaria para obtener la recompensa.
     * @return La puntuación necesaria.
     */
    public int getPuntuacion() {
        return puntuacion;
    }

    /**
     * Establece la puntuación necesaria para obtener la recompensa.
     * @param puntuacion La puntuación de la recompensa. Debe ser mayor que 0.
     * @throws IllegalArgumentException Si la puntuación es menor o igual a 0.
     */
    public void setPuntuacion(int puntuacion) {
        if (puntuacion <= 0) {
            throw new IllegalArgumentException("La puntuación debe ser mayor que 0.");
        }
        this.puntuacion = puntuacion;
    }

    /**
     * Establece el estado de la recompensa.
     * @param estado El estado de la recompensa.
     */
    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		
		Recompensa other = (Recompensa) obj;
		return (id.equals(other.getId()));
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, puntuacion, estado);
    }

    /**
     * Métod que asigna la reocmpensa a un usuario
     * @param usuario no peude ser null
     * @throws IllegalArgumentexception si el usuario es nulo
     * 
     */
    public void setUsuario(Usuario usuario) {
        if(usuario==null) throw new IllegalArgumentException();
        this.usuario = usuario;
        
      
    }

    
}



