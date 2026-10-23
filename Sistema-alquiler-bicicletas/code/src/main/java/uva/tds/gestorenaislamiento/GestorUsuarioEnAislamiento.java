package uva.tds.gestorenaislamiento;
import uva.tds.interfaces.IUsuarioRepositorio;
import uva.tds.base.Usuario;


/**
 * Clase que se encarga de gestionar los usuarios de un sistema de alquiler de bicicletas.
 * Contiene operaciones básicas como registrar, activar y desactivar a un usuario.
 * 
 * Asimismo permite actualizar las recompensas y el nombre de los usuarios.
 * Sustituye el almacenamiento en memoria
 * 
 * @author Ainhoa Carbajo Orgaz
 * @author Marcos de Diego Martin
 */
public class GestorUsuarioEnAislamiento {
    private IUsuarioRepositorio usuarioRepo;
   

    /**
     * Constructor de la clase GestorUsuario.
     */
    public GestorUsuarioEnAislamiento(IUsuarioRepositorio usuarioRepo) {
        this.usuarioRepo=usuarioRepo;
    }

    /**
     * Registra un nuevo usuario en el sistema. El usuario es activado automáticamente al registrarse.
     * No se permite registrar dos usuarios con el mismo NIF.
     * 
     * @param usuario El objeto Usuario que se desea registrar.
     * @throws IllegalArgumentException si el usuario es nulo
     * @throws IllegalArgumentException Si ya existe un usuario con el mismo NIF.
     */
    public void registrarUsuario(Usuario usuario) {

        usuarioRepo.registrarUsuario(usuario);
    }

    /**
     * Método que elimina un usuario de sistema de gestión dado su nif
     * @param nif el identificador del usuario
     * @throws IllegalArgumentException si no existe un usuario con ese nif en el sistema
     * @throws IllegalArgumentException si el nif es nulo
     */
    public void eliminarUsuario(String  nif) {

        usuarioRepo.eliminarUsuario(nif);
    }


    /**
     * Obtiene un usuario a partir de su NIF.
     * 
     * @param nif El NIF del usuario que se desea obtener.
     * @return El objeto Usuario correspondiente al NIF.
     *  @throws IllegalArgumentException si el nif es nulo
     * @throws IllegalArgumentException si no se encuentra un usuario con
     * ese nif
     */
    public Usuario getUsuario(String nif) {
        return usuarioRepo.getUsuario(nif);
    }

    /**
     * Actualiza  un usuario existente.
     *
     * @param usuario El nuevo usuario.
     * @throws IllegalArgumentException si el usuario es nulo
     * @throws IllegalArgumentException si el usuario no está registrado
     */
    public void actualizarUsuario(Usuario usuario) {
        if(usuario==null) throw new IllegalArgumentException();
        usuarioRepo.getUsuario(usuario.getNif());
        usuarioRepo.actualizarUsuario(usuario);
        
    }


    /**
     * Desactiva un usuario en el sistema. El usuario pasa a estar inactivo.
     * 
     * @param nif El NIF del usuario que se desea desactivar.
     * @throws IllegalArgumentException si no se encuentra un usuario con
     * ese nif (incluída la cadena null)
     */
    
    public void desactivarUsuario(String nif) {
        Usuario usuario = usuarioRepo.getUsuario(nif);
        usuario.setEstado(false);
    }


    /**
     * Activa un usuario en el sistema. El usuario pasa a estar activo.
     * 
     * @param nif El NIF del usuario que se desea activar.
     * @throws IllegalArgumentException si no se encuentra un usuario con
     * ese nif (incluída la cadena null)
     */
    
    public void activarUsuario(String nif) {
        Usuario usuario = usuarioRepo.getUsuario(nif);
        usuario.setEstado(true);
    }

    
    /**
     * Agrega un número de recompensas a un usuario mediante su nif
     * 
     * @param nif del usuario, debe encontrarse el nif (usuario) en el gestor
     * @param recompensas a agregar, no puede ser negativo
     * @throws IllegalArgumentException si no se encunetra un usuario en el gestor
     * con ese nif (incluído el null)
     * @throws IllegalArgumentException si recompensas es menor que cero
     */
    
    public void agregarRecompensas(String nif, int recompensas) {
        if(recompensas<0){
            throw new IllegalArgumentException("No se puede agregar una recompensa negativa");
        }
        int totalRecompensas = usuarioRepo.getUsuario(nif).getPuntuacion();
        totalRecompensas += recompensas;
        usuarioRepo.getUsuario(nif).setPuntuacion(totalRecompensas);
    }


    /**
     * Elimina todas las recompensas de un usuario según su nif
     * 
     * @param nif del usuario del que se quiere eliminar todas las recompensas
     * @throws IllegalArgumentException si no se encuentra un usuario con ese
     * nif (incluída la cadena null)
     */
    
    public void eliminarRecompensas(String nif) {
        Usuario usuarioEliminarRecompensas = usuarioRepo.getUsuario(nif);
        usuarioEliminarRecompensas.setPuntuacion(0);
    }


    /**
     * Eliminar un número de recompensas de un usuario a partir de su nif
     * 
     * @param nif del usuario del que se quiere eliminar las recompensas
     * @param recompensas del usuario que se quieren eliminar, no pueden ser
     * menor que cero ni mayor que la puntuación del usuario
     * @throws IllegalArgumentException si no se encuentra un usuario con ese
     * nif (incluída la cadena null)
     * @throws IllegalArgumentException si recompensas es menor que cero
     * @throws IllegalStateException si las recompensas que se quieren eliminar
     * son superiores a la puntuación del usuario
     */
    
    public void eliminarRecompensas(String nif, int recompensas) {
        Usuario usuarioModificar = usuarioRepo.getUsuario(nif);
        if (recompensas < 0) throw new IllegalArgumentException("No se puede eliminar una recompensa negativa");

        int recompensasUsuario = usuarioModificar.getPuntuacion();
        if (recompensasUsuario < recompensas) throw new IllegalStateException("No se puede eliminar recompensas superiores a la puntuación de un usuario");
     
        usuarioModificar.setPuntuacion(recompensasUsuario - recompensas);
    }

     /**
     * Modifica las recompensas de un usuario, garantizando que no se eliminan más de las que tiene.
     * 
     * @param nif El NIF del usuario cuyo número de recompensas se modificará.
     * @param recompensas A modificar, no puede ser negativo ni mayor que el total de recompensas del usuario.
     * @throws IllegalArgumentException si no se encuentra un usuario con ese nif (incluída la cadena null)
     * @throws IllegalArgumentException si recompensas es menor que cero.
     */
    public void modificarRecompensas(String nif, int recompensas) {
        Usuario usuarioModificar = usuarioRepo.getUsuario(nif);
        usuarioModificar.setPuntuacion(recompensas);
    }
}
