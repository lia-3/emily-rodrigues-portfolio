package uva.tds.interfaces;
import uva.tds.base.Recompensa;
import uva.tds.base.Usuario;
import java.util.ArrayList;

/**
 * Interface que define los métodos para gestionar las recompensas dentro de un sistema de gestión.
 * Esta interfaz permite añadir y eliminar recompensas, consultarlas y modificar su estado.
 * 
 * La implementación de esta interfaz debe garantizar la consistencia y robustez de las operaciones 
 * sobre los usuarios validando su nif, nombre, puntuación y estado. 
 * @author Ainhoa Carbajo Orgaz
 */
public interface IRecompensaRepositorio {

    /**
     * Añade una recompensa al sistema.
     *
     * @param recompensa La recompensa a añadir.
     * @throws IllegalArgumentException si la recompensa es nula
     * @throws IllegalArgumentException si ya existe una recompensa con el mismo identificador.
     */

    void addRecompensa(Recompensa recompensa);

    /**
     * Asigna una recompensa a un usuario si tiene suficiente puntuación.
     *
     * @param usuario    El usuario que quiere obtener la recompensa.
     * @param idRecompensa El id de la recompensa a añadir.
     * @throws IllegalArgumentException si el usuario o el id de la recompensa es nula
     * @throws IllegalArgumentException si el usuario no tiene suficientes puntos o la recompensa esta inactiva.
     */

    void addRecompensaUsuario(Usuario usuario, String idRecompensa);

    /**
     * Obtiene una lista de las recompensas activas en el sistema.
     *
     * @return Lista de recompensas activas.
     */

    ArrayList<Recompensa> obtenerRecompensasActivas();

    /**
     * Obtiene una lista de recompensas obtenidas por un usuario.
     *
     * @param usuario El usuario del cual obtener las recompensas.
     * @return Lista de recompensas obtenidas por el usuario.
     */

    ArrayList<Recompensa> obtenerRecompensasUsuario(Usuario usuario);

    /**
     * Obtiene una recompensa dado su id
     * @param id
     * @return Recompensa
     */
    public Recompensa getRecompensa(String id);
    
    /**
     * Modifica una recommpensa
     *
     * @param recompensa La recompensa a actualizar.
     * 
     * @throws IllegalArgumentException si la recompensa es nula
     * @throws IllegalArgumentException si la recompensa no existe.
     */
    void actualizarRecompensa(Recompensa recompensa);

}