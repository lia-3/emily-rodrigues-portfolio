package uva.tds.interfaces;
import uva.tds.base.Usuario;
/**
 * Interface que define los métodos para gestionar los usuarios dentro de un sistema de gestión.
 * Esta interfaz permite registar y consultar usuarios, eliminarlos o actualizarlos y gestionar 
 * sus recompensas,
 * 
 * La implementación de esta interfaz debe garantizar la consistencia y robustez de las operaciones 
 * sobre los usuarios validando su nuf, nombre, puntuación y estado. 
 * 
 * @author Ainhoa Carbajo Orgaz
 * @author Emily Rodrigues
 */
public interface IUsuarioRepositorio {
    
    /**
     * Registra un nuevo usuario en el sistema. El usuario es activado automáticamente al registrarse.
     * No se permite registrar dos usuarios con el mismo NIF.
     * 
     * @param usuario el objeto Usuario que se desea registrar.
     * @throws IllegalArgumentException si el usuario es nulo
     * @throws IllegalArgumentException Si ya existe un usuario con el mismo NIF.
     */
    void registrarUsuario(Usuario usuario);

    /**
     * Obtiene un usuario a partir de su NIF.
     * 
     * @param nif El NIF del usuario que se desea obtener.
     * @return El objeto Usuario correspondiente al NIF. Null en caso de que no lo encuentre
     * @throws IllegalArgumentException si el nif es nulo
     * @throws IllegalArgumentException si el nif tiene una longitud distinta de 9
     *  @throws IllegalArgumentException si el usuario no está registrado
     *
     */
    Usuario getUsuario(String nif);

    /**
     * Método que elimina un usuario de sistema de gestión dado su nif
     * @param nif el identificador del usuario
     * @throws IllegalArgumentException si no existe un usuario con ese nif en el sistema
     * @throws IllegalArgumentException si el nif es nulo
     */
    void eliminarUsuario(String nif);

    /**
     * Actualiza  un usuario existente.
     *
     * @param usuario El nuevo usuario.
     * @throws IllegalArgumentException si el usuario es nulo
     * @throws IllegalArgumentException si el usuario no está registrado
     */
    void actualizarUsuario(Usuario usuario);


    /**
	 * Limpia las tablas 'USUARIOS' y 'RECOMPENSAS' en la base de datos, 
	 * eliminando todos los registros. 
     * Prepara un entorno limpio en pruebas automatizadas que interactúan 
	 * con un repositorio de usuarios y recompensas.
     * Elimina las tablas de la base de datos
	 */
	public void clearDatabase();
}