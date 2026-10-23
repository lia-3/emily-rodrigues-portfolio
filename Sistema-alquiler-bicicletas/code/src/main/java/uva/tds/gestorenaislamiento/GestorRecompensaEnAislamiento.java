package uva.tds.gestorenaislamiento;
import uva.tds.interfaces.IRecompensaRepositorio;
import uva.tds.base.*;
import java.util.ArrayList;

/**
 * Clase que gestiona recompensas, permitiendo agregar, modificar su estado y obtener recompensas de usuarios.
 * Sustituye el almacenamiento en memoria
 * @author Ainhoa Carbajo Orgaz
 */

public class GestorRecompensaEnAislamiento{

    private IRecompensaRepositorio recompensaRepo;

    public GestorRecompensaEnAislamiento(IRecompensaRepositorio recompensaRepo) {
        this.recompensaRepo=recompensaRepo;
    }

    /**
     * Añade una recompensa al sistema.
     *
     * @param recompensa La recompensa a añadir.
     * @throws IllegalArgumentException si ya existe una recompensa con el mismo identificador.
     */

    public void addRecompensa(Recompensa recompensa) {
        recompensaRepo.addRecompensa(recompensa);
    }

    /**
     * Asigna una recompensa a un usuario si tiene suficiente puntuación.
     *
     * @param usuario    El usuario que quiere obtener la recompensa.
     * @param recompensa El id de la recompensa a obtener.
     * @throws IllegalArgumentException si el usuario no tiene suficientes puntos o la recompensa esta inactiva.
     */
    public void addRecompensaUsuario(Usuario usuario, String id) {
        recompensaRepo.addRecompensaUsuario(usuario,id);
    }

    /**
     * Obtiene una lista de las recompensas activas en el sistema.
     *
     * @return Lista de recompensas activas.
     */

    public ArrayList<Recompensa> obtenerRecompensasActivas() {
        return recompensaRepo.obtenerRecompensasActivas();
    }

     /**
     * Obtiene una recompensa dado su id
     * @param id
     * @return Recompensa
     */
    public Recompensa getRecompensa(String id){
        return recompensaRepo.getRecompensa(id);
    }

    /**
     * Obtiene una lista de recompensas obtenidas por un usuario.
     *
     * @param usuario El usuario del cual obtener las recompensas.
     * @return Lista de recompensas obtenidas por el usuario.
     */

    public ArrayList<Recompensa> obtenerRecompensasUsuario(Usuario usuario) {
        return recompensaRepo.obtenerRecompensasUsuario(usuario);
    }

    /**
     * Modifica una recommpensa
     *
     * @param recompensa La recompensa a actualizar.
     * 
     * @throws IllegalArgumentException si la recompensa es nula
     * @throws IllegalArgumentException si la recompensa no existe.
     */
    public void actualizarRecompensa(Recompensa recompensa) {
       recompensaRepo.actualizarRecompensa(recompensa);
    }

    

}
