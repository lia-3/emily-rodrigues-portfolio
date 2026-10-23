package uva.tds.interfaces;
import uva.tds.base.*;

import java.util.ArrayList;


/**
 * Interface que define los métodos para gestionar las paradas de bicicletas y operaciones 
 * asociadas a las bicicletas en cada parada dentro de un sistema de gestión de bicicletas.
 * Esta interfaz permite añadir y eliminar paradas, agregar y quitar bicicletas, gestionar 
 * bloqueos y reservas, y realizar alquileres de bicicletas.
 * 
 * La implementación de esta interfaz debe garantizar la consistencia y robustez de las operaciones 
 * sobre las paradas y bicicletas, validando los identificadores, los estados de las bicicletas,
 * y las condiciones de las paradas. 
 * 
 * @author Emily Rodrigues
 */
public interface IParadaRepositorio {

    /**
     * Consulta todas las paradas del gestor. Podría estar vacía si se han eliminado
     * todas las paradas
     * @return lista de paradas del gestor. Podría estar vacía si se han eliminado
     * todas las paradas
     */
    public ArrayList<Parada> getParadas();


    /**
     * Añade una nueva parada al repositorio.
     * @param parada parada a añadir. No puede ser null.
     * @throws IllegalArgumentException si {@code parada == null}.
     * @throws IllegalStateException si la parada ya está en el repositorio.
     */
    public void anadirParada(Parada parada);


    /**
     * Añade una bicicleta a una parada específica. Cuando se añade una bicicleta a una parada,
     * la bicicleta cambia su estado a DISPONIBLE.
     * @param idParada identificador de la parada. No puede ser null. Debe existir dicha parada
     * en el gestor.
     * @param bicicleta bicicleta a añadir. No puede ser null. No se podrá añadir la bicicleta
     * a la parada si ya existe otra bicicleta con el mismo identificador en el gestor.
     * @throws IllegalArgumentException si {@code idParada == null} o {@code bicicleta == null}.
     * @throws IllegalStateException si la bicicleta ya está en el gestor.
     * @throws IllegalStateException si la parada no está en el repositorio.
     */
    public void agregarBicicleta(String idParada, Bicicleta bicicleta);


    /**
     * Método que elimina una bicicleta de una parada dado su identificador
     * @param idParada identificador de la parada. No puede ser null. Debe
     * existir una parada con ese identificador. 
     * @param identificadorBici id de la bicicleta a eliminar. No puede ser null.
     * Debe existir una bicicleta con dicho identificador en la parada. No puede tener
     * estado OCUPADA.
     * @throws IllegalArgumentException si idParada o identificadorBici son nulos
     * @throws IllegalStateException si la bicicleta no está en la parada
     * @throws IllegalStateException si la parada no está
     * @throws IllegalStateException si la bicicleta está ocupada 
     */
    public void eliminarBicicleta(String idParada, String identificadorBici);


    /**
     * Desactiva una parada a partir de su identificador. No se puede desactivar una parada
     * que no existe. No se puede desactivar una parada que ya está desactivada.
     * @param idParada identificador de la parada. No puede ser null. Debe existir una
     * parada con dicho identificador en el repositorio.
     * @throws IllegalArgumentException si identificador es null
     * @throws IllegalStateException si no hay una parada con ese identificador
     * @throws IllegalStateException si la parada ya estaba desactivada
     */
    public void desactivarParada(String idParada);


    /**
     * Activa una parada a partir de su identificador. Debe estar desactivada. 
     * Debe existir una parada con dicho identificador en el gestor.
     * @param idParada identificador de la parada. No puede ser null. 
     * @throws IllegalArgumentException si identificador es null
     * @throws IllegalStateException si no hay una parada
     * con ese identificador
     * @throws IllegalStateException si la parada ya estaba activa.
     */
    public void activarParada(String idParada);


    /**
     * Método que bloquea una bicicleta de una parada
     * @param idParada identificador de la parada de la que se desea bloquear la bicicleta. 
     * No puede ser null y debe estar activa para poder bloquear la bicicleta. 
     * @param idBici identificador de la bicicleta que se desea bloquear. No puede ser 
     * null y debe encontrarse en la parada especificada. La bicicleta no debe estar bloqueada
     * al realizar esta operación.
     * @throws IllegalStateException si la parada no está en el gestor
     * @throws IllegalArgumentException si idParada == null
     * @throws IllegalStateException si la bicicleta no está en la parada indicada
     * @throws IllegalArgumentException si idBici == null
     * @throws IllegalStateException si la bicicleta ya está bloqueada
     * @throws IllegalStateException si la parada está desactivada
     */
    public void bloquearBicicleta(String idParada, String idBicicleta);


    /**
     * Consulta los bloqueos de bicicletas en el repositorio. 
     * En caso de no haber bloqueos, deuelve una lista vacía.
     * @return lista de bloqueos en el repositorio. Si no hay bloqueos,
     * debuelve una lista vacía.
     */
    public ArrayList<Bloqueo> getListaBloqueos();


    /**
     * Método que desbloquea una bicicleta bloqueada en una parada dada.
     * @param idParada identenficador de la parada en la que se localiza la bicicleta. Debe
     * existir una parada con ese identificador en el gestor. Debe estar activa para poder
     * desbloquear a una bicicleta. No puede ser null.
     * @param idBici identificador de la bicicleta que se desea desbloquear. No puede ser
     * null. La bicicleta debe estar bloqueada para poder desbloquearla.
     * @throws IllegalArgumentException si idParada o idBici son nulos.
     * @throws IllegalStateException si la parada no está en el gestor.
     * @throws IllegalStateException si la bicicleta no está en la parada.
     * @throws IllegalStateException si la bicicleta no está bloqueada
     * @throws IllegalStateException si la parada está desactivada
     */
    public void desbloquearBicicleta(String idParada, String idBicicleta);


    /**
     * Permite que un usuario pueda reservar una bicicleta que se encuentra en una parada 
     * a partir de sus identificadores
     * @param idParada identificador de la parada. No puede ser null y debe existir
     * en el gestor. Debe estar activa para realizar la reserva.
     * @param idBicicleta identificador de la bicicleta que se quiere reserva. No puede
     * ser null y debe existir en la parada indicada. La bicicleta debe estar disponible para
     * poder realizar la reserva.
     * @param usuario usuario que quiere reserva la bicicleta. No puede ser null, debe estar
     * activo y no puede tener otra reserva en el gestor.
     * @throws IllegalArgumentException si {@code (idParada == null) || (idBicicleta == null) 
     * || (usuario == null)}
     * @throws IllegalStateException si la parada no se encuentra en el gestor.
     * @throws IllegalStateException si la parada está desactivada.
     * @throws IllegalStateException si la bicicleta no se encuentra en la parada dada.
     * @throws IllegalStateException si la bicicleta no está disponible.
     * @throws IllegalStateException si el usuario está inactivo o tiene otra reserva.
     */
    public void reservaBicicleta(String idParada, String idBicicleta, Usuario usuario);


    /**
     * Consulta las reservas activas del gestor. Si no hay ninguna reserva activa, devuelve
     * una lista vacía.
     * @return reservas actuales de bicicletas que hay en el gestor. Si no hay ninguna reserva activa, 
     * devuelve una lista vacía.
     */
    public ArrayList<Reserva> getReservasBicicletas();


    /**
     * Consulta las reservas activas del gestor. Si no hay reservas en el gestor,
     * devuelve una lista vacía.
     * @return lista con las reservas actuales de bicicletas que hay en el gestor,
     * o una lista vacía si no hay reservas.
     */
    public ArrayList<Alquiler> getAlquileresEnCurso();


    /**
     * Método que alquila una bicicleta. Cuando se alquila, se elimina del almacenamiento.
     * Registra el alquiler y establece la bicicleta en estado OCUPADA.
     * Se pueden alquilar bicicletas reservadas si no ha pasado más de una hora desde que se reservó la
     * bicicleta y si alquila el usuario que hizo la reserva.
     * La parada debe encontrarse activa para poder realizar el alquiler.
     * @param idParada identificador de la parada que tiene la bicicleta. No puede ser null. Debe existir
     * una parada con dicho identificador en el gestor.
     * @param idBici identificador de la bicicleta disponible o reservada que se desea alquilar. No puede ser 
     * null. Debe encontrarse una bicicleta con ese identificador en la parada especificada. Debe tener estado
     * DISPONIBLE o RESERVADA la bicicleta.
     * @param usuario usuario que realiza en alquiler. No puede ser null. En caso de que la bicicleta se 
     * encuentre RESERVADA, el usuario debe tener una reserva asociada a dicha bicicleta en el gestor antes de que
     * pase el período de reserva. Una vez que se ha acabado ese tiempo, cualquier usuario puede alquilar la
     * bicicleta.
     * @throws IllegalArgumentException si idParada, idBici o usuario son null.
     * @throws IllegalStateException si la bicicleta no está en la parada
     * @throws IllegalStateException si la parada no está en el gestor
     * @throws IllegalStateException si el usuario pasado no hizo la reserva, en caso de que la bicicleta
     * tenga estado RESERVADA y no se haya acabado el período de reserva
     * @throws IllegalStateException si el usuario ya tiene un alquiler en curso
     * @throws IllegalStateException si el usuario no está activo
     * @throws IllegalStateException si la bicicleta está BLOQUEADA u OCUPADA
     * @throws IllegalStateException si la parada no está activa
     */
    public void alquilarBicicleta(String idParada, String idBicicleta, Usuario usuario);


    /**
     * Método que devuelve una bicicleta, es decir, finaliza un alquiler en curso relaizado por un 
     * usuario activo del sistema.
     * @param idParada identificador de la parada en la que se quiere depositar la bicicleta.
     * Debe existir una parada con dicho identificador en el gestor. No puede ser null.
     * @param nifUsuario NIF del usuario que realizó el alquiler de la bicicleta y que ahora
     * quiere devolver. No puede ser null. Debe tener un alquiler asociado en el gestor.
     * @param bici bicicleta que se quiere devolver. No puede ser null. DEbe estar ocupada.
     * 
     * @throws IllegalArgumentException si idParada, nifUsuario o bici son null
     * @throws IllegalStateException si la parada no está en el gestor
     * @throws IllegalStateException si no se encuentra un usuario con ese NIF que haya
     * realizado una reserva en el sistema.
     * @throws IllegalStateException si la parada no está activa
     * @throws IllegalStateException si la parada está llena
     * @throws IllegalStateException si la bicicleta no estaba ocupada
     */
    public void devolverBicicleta(String idParada, String nif, Bicicleta bici);


    /**
     * Consulta si un usuario tiene una reserva activa.
     * @param usuario usuario del que se quiere conocer si tiene una reserva. No
     * puede ser null.
     * @return true si el usuario tiene una reserva, false en caso contrario.
     * @throws IllegalArgumentException si {@code usuario == null}
     */
    public boolean tieneUsuarioUnaReserva(Usuario usuario);
}