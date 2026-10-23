package uva.tds.interfaces;
import uva.tds.base.Ruta;
import uva.tds.base.Usuario;
import java.util.List;

/**
 * Interfaz que define las operaciones para gestionar las rutas en un repositorio.
 * Esta interfaz proporciona funcionalidades para añadir rutas, obtener rutas por usuario,
 * calcular puntuaciones y obtener distancias y tiempos de las rutas.
 * 
 * @author Marcos de Diego Martin
 * @author Emily Rodrigues
 */
public interface IRutaRepositorio {

    /**
     * Añade una nueva ruta al repositorio.
     * 
     * @param ruta La ruta a añadir.
     * @throws IllegalArgumentException Si la ruta es null o si ya existe una ruta con el mismo identificador.
     */
    void anadirRuta(Ruta ruta);

    /**
     * Obtiene las rutas realizadas por un usuario.
     * 
     * @param usuario El usuario cuyas rutas se desean obtener.
     * @return Una lista de rutas realizadas por el usuario.
     * @throws IllegalArgumentException Si el usuario es null.
     */
    List<Ruta> obtenerRutasPorUsuario(Usuario usuario);

    /**
     * Calcula la puntuación de una ruta.
     * 
     * @param distancia La distancia de la ruta en kilómetros.
     * @param tiempo El tiempo de la ruta en minutos.
     * @return La puntuación de la ruta.
     * @throws IllegalArgumentException Si el tiempo es 0 o menor.
     */
    int calcularPuntuacionRuta(double distancia, int tiempo);


    /**
     * Busca una ruta por su identificador.
     * 
     * @param identificador El identificador de la ruta.
     * @return La ruta encontrada.
     * @throws IllegalArgumentException Si no se encuentra una ruta con el identificador proporcionado.
     */
    Ruta buscarRutaPorIdentificador(String identificador);


    /**
	 * Limpia las tablas 'USUARIOS' y 'Ruta' en la base de datos, 
	 * eliminando todos los registros. 
     * Prepara un entorno limpio en pruebas automatizadas que interactúan 
	 * con un repositorio de usuarios y recompensas.
     * Elimina las tablas de la base de datos
	 */
	public void clearDatabase();



}

