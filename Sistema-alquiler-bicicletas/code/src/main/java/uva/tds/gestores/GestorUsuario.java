package uva.tds.gestores;
import uva.tds.base.Usuario;

import java.util.HashMap;
import java.util.Map;


/**
 * Clase que se encarga de gestionar los usuarios de un sistema de alquiler de bicicletas.
 * Contiene operaciones básicas como registrar, activar y desactivar a un usuario.
 * 
 * Asimismo permite actualizar las recompensas y el nombre de los usuarios.
 * 
 * @author Marcos de Diego Martin
 * @author Emily Rodrigues
 * @author Ainhoa Carbajo
 */
public class GestorUsuario {

    private Map<String, Usuario> usuarios;

    /**
     * Constructor de la clase GestorUsuario.
     */
    public GestorUsuario() {
        usuarios = new HashMap<>();
    }

    /**
     * Registra un nuevo usuario en el sistema. El usuario es activado automáticamente al registrarse.
     * No se permite registrar dos usuarios con el mismo NIF.
     * 
     * @param usuario usuario que se desea registrar. No puede ser null
     * @throws IllegalArgumentException si usuario es null
     * @throws IllegalStateException Si ya existe un usuario con el mismo NIF.
     */
    public void registrarUsuario(Usuario usuario) {
        if (usuario == null) throw new IllegalArgumentException();
        if (usuarios.containsKey(usuario.getNif())) {
            throw new IllegalStateException("Ya existe un usuario con ese NIF.");
        }
        usuario.setEstado(true);
        usuarios.put(usuario.getNif(), usuario);
    }


    /**
     * Obtiene un usuario a partir de su NIF.
     * 
     * @param nif El NIF del usuario que se desea obtener. No puede ser null. Debe
     * existir un usuario con ese NIF en el gestor.
     * @return el usuario correspondiente al NIF.
     * @throws IllegalArgumentException si nif es null
     * @throws IllegalStateException si no se encuentra un usuario con ese nif
     */
    public Usuario getUsuario(String nif) {
        if (nif == null) throw new IllegalArgumentException();
        if (!usuarios.containsKey(nif)) {
            throw new IllegalStateException("El usuario no existe.");
        }
        return usuarios.get(nif);
    }


     /**
     * Actualiza el nombre de un usuario a partir de su NIF
     * @param nif El NIF del usuario a actualizar.
     * @param nombre El nuevo nombre del usuario. Si es null, no se actualiza.
     * @throws IllegalArgumentException si nif o nombre son null
     * @throws IllegalStateException si no se encuntra un usuario con ese nif en el gestor.
     */
    public void actualizarNombreUsuario(String nif, String nuevoNombre) {
        Usuario usuario = getUsuario(nif);
        usuario.setNombre(nuevoNombre);
    }

    /**
     * Desactiva un usuario en el sistema. El usuario pasa a estar inactivo.
     * @param nif El NIF del usuario que se desea desactivar. No puede ser null.
     * @throws IllegalArgumentException si nif es null
     * @throws IllegalStateException si no se encuentra un usuario con
     * ese nif 
     */
    public void desactivarUsuario(String nif) {
        Usuario usuario = getUsuario(nif);
        usuario.setEstado(false);
    }


    /**
     * Activa un usuario en el sistema. El usuario pasa a estar activo.
     * @param nif El NIF del usuario que se desea activar. No puede ser null.
     * @throws IllegalArgumentException si nif es null
     * @throws IllegalStateException si no se encuentra un usuario con
     * ese nif 
     */
  
    public void activarUsuario(String nif) {
        Usuario usuario = getUsuario(nif);
        usuario.setEstado(true);
    }

    
    /**
     * Agrega un número de recompensas a un usuario mediante su nif
     * 
     * @param nif del usuario, debe encontrarse el nif (usuario) en el gestor
     * @param recompensas a agregar, no puede ser negativo
     * @throws IllegalArgumentException si el nif es nulo
     * @throws IllegalStateException si no se encuentra un usuario con
     * ese nif 
     * @throws IllegalArgumentException si recompensas es menor que cero
     */   
    public void agregarRecompensas(String nif, int recompensas) {
        if (recompensas < 0) throw new IllegalArgumentException();
        int totalRecompensas = getUsuario(nif).getPuntuacion() + recompensas;
        
        getUsuario(nif).setPuntuacion(totalRecompensas);
    }


    /**
     * Elimina todas las recompensas de un usuario según su nif
     * @param nif del usuario del que se quiere eliminar todas las recompensas. No puede
     * ser null.
     * @throws IllegalArgumentException si nif es null
     * @throws IllegalStateException si no se encuentra un usuario con
     * ese nif 
     */    
    public void eliminarTodasLasRecompensas(String nif) {
        Usuario usuarioEliminarRecompensas = getUsuario(nif);
        usuarioEliminarRecompensas.setPuntuacion(0);
    }


    /**
     * Elimina un número de recompensas de un usuario a partir de su nif
     * @param nif del usuario del que se quiere eliminar las recompensas. No puede ser null. 
     * Debe existir un usuario con dicho NIF en el gestor.
     * @param recompensas del usuario que se quieren eliminar, no pueden ser
     * menor que cero ni mayor que la puntuación del usuario
     * @throws IllegalArgumentException si nif es null
     * @throws IllegalStateException si no se encuentra un usuario con
     * ese nif 
     * @throws IllegalArgumentException si recompensas es menor que cero
     * @throws IllegalStateException si las recompensas que se quieren eliminar
     * son superiores a la puntuación del usuario, esto provocaría que el usuario tenga
     * recompensas negativas.
     */
    public void eliminarRecompensas(String nif, int recompensas) {
        Usuario usuarioModificar = getUsuario(nif);
        if (recompensas < 0) throw new IllegalArgumentException("No se puede eliminar una recompensa negativa");

        int recompensasUsuario = usuarioModificar.getPuntuacion();
        if (recompensasUsuario < recompensas) throw new IllegalStateException("No se puede eliminar recompensas superiores a la puntuación de un usuario");
     
        usuarioModificar.setPuntuacion(recompensasUsuario - recompensas);
    }
}
