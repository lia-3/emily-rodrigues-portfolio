package uva.tds.gestores;

import uva.tds.base.Ruta;
import uva.tds.interfaces.ICalculoRutas;

import uva.tds.base.Parada;
import uva.tds.base.Usuario;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 * Clase que gestiona las rutas y proporciona funcionalidades para manejar y
 * obtener información sobre ellas.
 * 
 * @author Marcos de Diego Martin
 * @author Emily Rodrigues
 */
public class GestorRutas {

    private final ArrayList<Ruta> rutas;
    private final ICalculoRutas servicioCalculo;

    /**
     * Constructor del gestor de rutas.
     * 
     * @param servicioCalculo servicio utilizado para realizar el calculo de las
     *                        rutas.
     *                        No puede ser null.
     * @throws IllegalArgumentException si servicioCalculo es nulo
     */
    public GestorRutas(ICalculoRutas servicioCalculo) {
        this.rutas = new ArrayList<>();
        this.servicioCalculo = servicioCalculo;

    }

    /**
     * Añade una nueva ruta al gestor.
     * 
     * @param ruta La ruta a añadir. No puede ser null. No puede existir un
     *             ruta igual, es decir, con mismo identificador.
     * @throws IllegalArgumentException Si la ruta es null
     * @throws IllegalStateException    si ya existe una ruta con el mismo
     *                                  identificador.
     */
    public void agregarRuta(Ruta ruta) {
        if (ruta == null) {
            throw new IllegalArgumentException("La ruta no puede ser null.");
        }
        if (rutas.stream().anyMatch(r -> r.getIdentificador().equals(ruta.getIdentificador()))) {
            throw new IllegalStateException("Ya existe una ruta con el mismo identificador.");
        }
        rutas.add(ruta);
    }

    /**
     * Obtiene las rutas realizadas por un usuario
     * 
     * @param usuario El usuario cuyas rutas se desean obtener.
     * @return Una lista de rutas realizadas por el usuario. Si el usuario no
     *         tiene rutas, devuelve una lista vacía
     * @throws IllegalArgumentException Si el usuario es null.
     */

    public List<Ruta> obtenerRutasPorUsuario(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("El usuario no puede ser null.");
        }
        return rutas.stream()
                .filter(r -> r.getUsuario().equals(usuario))
                .toList();
    }

    /**
     * Calcula la puntuación de una ruta.
     * 
     * @param distancia La distancia de la ruta en kilómetros. No puede ser
     *                  negativo.
     * @param tiempo    El tiempo de la ruta en minutos. No puede ser menor o igual
     *                  que cero.
     * @return La puntuación de la ruta.
     * @throws IllegalArgumentException si distancia es menor que cero
     * @throws IllegalArgumentException Si el tiempo es menor o igual que cero.
     */
    public int calcularPuntuacionRuta(double distancia, int tiempo) {
        if (distancia < 0)
            throw new IllegalArgumentException();
        if (tiempo <= 0) {
            throw new IllegalArgumentException("El tiempo debe ser mayor que cero.");
        }
        return (int) Math.round((distancia / tiempo) * 10);
    }

    /**
     * Obtiene la distancia total de una ruta dada su identificador.
     * 
     * @param identificador El identificador de la ruta. No puede ser null. Debe
     *                      existir
     *                      la ruta en el gestor con dicho identificador
     * @return La distancia total de la ruta en kilómetros.
     * @throws IllegalArgumentExcpetion si identificador es null
     * @throws IllegalStateException    Si la ruta no existe.
     * @throws IllegalStateException    Si el cliente no está identificado en el
     *                                  servicio externo.
     * @throws IllegalArgumentException Si elidentificador es nulo
     */
    public double obtenerDistanciaTotal(String identificador) {
        if (identificador == null) {
            throw new IllegalArgumentException("El cliente no puede ser nulo.");
        }
        if (!servicioCalculo.clienteIdentificado()) {
            throw new IllegalStateException("El cliente no está identificado.");
        }
        Ruta ruta = buscarRutaPorIdentificador(identificador);
        double distanciaTotal = 0.0;

        Parada p1 = ruta.getParadaOrigen();
        Parada p2 = ruta.getParadaDestino();
        distanciaTotal += servicioCalculo.getDistancia(p1.getLatitud(), p1.getLongitud(), p2.getLatitud(),
                p2.getLongitud()) / 1000.0; // Convertir metros a kilómetros

        return distanciaTotal;
    }

    /**
     * Obtiene el tiempo total de una ruta dada su identificador.
     * 
     * @param identificador El identificador de la ruta. No puede ser null. Debe
     *                      existir una ruta
     *                      con ese identificador en el gestor
     * @return El tiempo total de la ruta en minutos
     * @throws IllegalArgumentExcpetion si identificador es null
     * @throws IllegalStateException    Si la ruta no existe.
     * @throws IllegalStateException    Si el cliente no está identificado en el
     *                                  servicio externo.
     */

    public int obtenerTiempoTotal(String identificador) {
        Ruta ruta = buscarRutaPorIdentificador(identificador);

        if (!servicioCalculo.clienteIdentificado()) {
            throw new IllegalStateException("El cliente no está identificado.");
        }

        int tiempoTotal = 0;
        Parada p1 = ruta.getParadaOrigen();
        Parada p2 = ruta.getParadaDestino();
        tiempoTotal += servicioCalculo.getTiempo(p1.getLatitud(), p1.getLongitud(), p2.getLatitud(), p2.getLongitud())
                / 60;

        return tiempoTotal;
    }

    /**
     * Busca una ruta por su identificador.
     * 
     * @param identificador El identificador de la ruta. No puede ser null.
     * @return La ruta encontrada.
     * @throws IllegalArgumentException si identificador es nulo
     * @throws IllegalStateException    Si no se encuentra la ruta.
     */
    public Ruta buscarRutaPorIdentificador(String identificador) {
        if (identificador == null) {
            throw new IllegalArgumentException("El identificador no puede ser null.");
        }
        Optional<Ruta> rutaOpt = rutas.stream().filter(r -> r.getIdentificador().equals(identificador)).findFirst();
        if (rutaOpt.isEmpty()) {
            throw new IllegalStateException("No se ha encontrado una ruta con el identificador proporcionado.");
        }
        return rutaOpt.get();
    }

}
