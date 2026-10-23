package uva.tds.gestores;
import uva.tds.base.Recompensa;
import uva.tds.base.Usuario;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Clase que gestiona recompensas, permitiendo agregar, activar, desactivar, y obtener recompensas para usuarios.
 * @author Marcos de Diego Martín
 * @author Emily Rodrigues
 * @author Ainhoa Carbajo Orgaz
 */
public class GestorRecompensas{

    private Map<String, Recompensa> recompensas;
    private Map<Usuario, ArrayList<Recompensa>> recompensasPorUsuario;

    public GestorRecompensas() {
        recompensas = new HashMap<>();
        recompensasPorUsuario = new HashMap<>();
    }

    /**
     * Añade una recompensa al sistema.
     * @param recompensa La recompensa a añadir. No puede ser null. No puede existir dos recompensas iguales,
     * es decir, con el mismo identificador.
     * @throws IllegalArgumentException si recompensa es null.
     * @throws IllegalArgumentException si ya existe una recompensa con el mismo identificador.
     */
    public void addRecompensa(Recompensa recompensa) {
        if (recompensa == null) throw new IllegalArgumentException();
        if (recompensas.containsKey(recompensa.getId())) {
            throw new IllegalArgumentException("La recompensa ya existe en el sistema.");
        }
        recompensas.put(recompensa.getId(), recompensa);
    }


     /**
     * Elimina una recompensa al sistema.
     * @param idRecompensa Identificador de la recompensa a eliminar. No puede ser null. 
     * @throws IllegalArgumentException si recompensa es null.
     * @throws IllegalArgumentException si no existe una recompensa con el mismo identificador.
     */
    public void eliminaRecompensa(String idRecompensa) {
        if (!recompensas.containsKey(idRecompensa)) {
            throw new IllegalArgumentException("La recompensa no existe en el sistema.");
        }
        recompensas.remove(idRecompensa);
    }
    
    /**
     * Añade una reocmpensa a un usaurio
     * @param usuario    El usuario que quiere obtener la recompensa. No puede ser null.
     * @param Recompensa recompensa a añadir. No puede ser null. 
     * @throws IllegalArgumentException si usuario o recompensas son null.
     * @throws IllegalStateException si el usuario no tiene suficientes puntos para
     * obtener la recompensa
     * @throws IllegalStateException si la recompensa esta inactiva.
     */
    public void addRecompensaUsuario(Usuario usuario, Recompensa recompensa) {
        if (usuario == null) throw new IllegalArgumentException();
        if (recompensa == null) throw new IllegalArgumentException();
        if (usuario.getPuntuacion() < recompensa.getPuntuacion() || !recompensa.getEstado()) {
            throw new IllegalStateException();
        }
        usuario.setPuntuacion(usuario.getPuntuacion() - recompensa.getPuntuacion());
        recompensasPorUsuario.putIfAbsent(usuario, new ArrayList<>());
        recompensasPorUsuario.get(usuario).add(recompensa);
    }

    /**
     * Obtiene una lista de las recompensas activas en el sistema. Si no hay recompensas
     * activas, devuelve una lista vacía.
     * @return Lista de recompensas activas. Puede estar vacía (si no hay recompensas activas).
     */
    public ArrayList<Recompensa> obtenerRecompensasActivas() {
        ArrayList<Recompensa> activas = new ArrayList<>();
        for (Recompensa recompensa : recompensas.values()) {
            if (recompensa.getEstado()) {
                activas.add(recompensa);
            }
        }
        return activas;
    }

    /**
     * Obtiene una recompensa dado su id
     * @param id identificador de la recompensa. No puede ser null. Debe existir una
     * recompensa con ese identificador en el gestor.
     * @return Recompensa con dicho identificador.
     * @throws IllegalArgumentException si id es null
     * @throws IllegalStateException si no hay una recompensa con ese identificador en el
     * gestor.
     */
    public Recompensa getRecompensa(String id){
        if (id == null) throw new IllegalArgumentException();
        if (!recompensas.containsKey(id)) throw new IllegalStateException();
        return recompensas.get(id);
    }

    /**
     * Obtiene una lista de recompensas obtenidas por un usuario.
     * @param usuario El usuario del cual obtener las recompensas. No puede ser null.
     * @return Lista de recompensas obtenidas por el usuario. Puede estar vacía si el 
     * usuario pasado no tiene recompensas.
     */
    public ArrayList<Recompensa> obtenerRecompensasUsuario(Usuario usuario) {
        if (usuario == null) throw new IllegalArgumentException();
        return recompensasPorUsuario.getOrDefault(usuario, new ArrayList<>());
    }

    /**
     * Activa una recompensa.
     * @param id El identificador de la recompensa a activar.
     * @throws IllegalArgumentException si id es null
     * @throws IllegalStateException si la recompensa no existe.
     */    
     public void activarRecompensa(String id) {
        if (id == null) throw new IllegalArgumentException();
        Recompensa recompensa = recompensas.get(id);
        if (recompensa == null) {
            throw new IllegalStateException("La recompensa no existe en el sistema.");
        }
        recompensa.setEstado(true);
    }

    /**
     * Desactiva una recompensa.
     * @param id El identificador de la recompensa a desactivar.
     * @throws IllegalArgumentException si id es null
     * @throws IllegalStateException si la recompensa no existe.
     */
    public void desactivarRecompensa(String id) {
        if (id == null) throw new IllegalArgumentException();
        Recompensa recompensa = recompensas.get(id);
        if (recompensa == null) {
            throw new IllegalStateException("La recompensa no existe en el sistema.");
        }
        recompensa.setEstado(false);
    }

    

}

