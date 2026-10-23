package uva.tds.gestorenaislamiento;

import uva.tds.interfaces.*;
import uva.tds.base.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Clase que gestiona las rutas y proporciona funcionalidades para manejar y
 * obtener información sobre ellas.
 * 
 * @author Marcos de Diego Martin
 */

public class GestorRutaEnAislamiento {

    private final IRutaRepositorio repositorio;
    private final ICalculoRutas servicioCalculo;

    /**
     * Constructor del gestor de rutas.
     * 
     * @param repositorio     Repositorio que maneja el almacenamiento de rutas
     *                        (puede ser una base de datos, archivo, etc.).
     * @param servicioCalculo Servicio que calcula la distancia y el tiempo de las
     *                        rutas.
     */
    public GestorRutaEnAislamiento(IRutaRepositorio repositorio, ICalculoRutas servicioCalculo) {
        this.repositorio = repositorio;
        this.servicioCalculo = servicioCalculo;
    }

    /**
     * Añade una nueva ruta al repositorio.
     * 
     * @param ruta La ruta a añadir.
     * @throws IllegalArgumentException Si la ruta es null o si ya existe una ruta
     *                                  con el mismo identificador.
     */

    public void anadirRuta(Ruta ruta) {
        if (ruta == null || repositorio.buscarRutaPorIdentificador(ruta.getIdentificador()) != null) {
            throw new IllegalArgumentException("La ruta es incorrecta.");
        }
        repositorio.anadirRuta(ruta);
    }

    /**
     * Obtiene las rutas realizadas por un usuario.
     * 
     * @param usuario El usuario cuyas rutas se desean obtener.
     * @return Una lista de rutas realizadas por el usuario.
     * @throws IllegalArgumentException Si el usuario es null.
     */

    public List<Ruta> obtenerRutasPorUsuario(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("El usuario no puede ser null.");
        }
        return repositorio.obtenerRutasPorUsuario(usuario).stream()
                .filter(r -> r.getUsuario().equals(usuario))
                .toList();
    }

    /**
     * Calcula la puntuación de una ruta.
     * 
     * @param distancia La distancia de la ruta en kilómetros.
     * @param tiempo    El tiempo de la ruta en minutos.
     * @return La puntuación de la ruta.
     * @throws IllegalArgumentException Si el tiempo es 0.
     */

    public int calcularPuntuacionRuta(double distancia, int tiempo) {
        if (tiempo <= 0) {
            throw new IllegalArgumentException("El tiempo debe ser mayor que cero.");
        }
        return (int) Math.round((distancia / tiempo) * 10);
    }

    /**
     * Obtiene la distancia total de una ruta dada su identificador.
     * 
     * @param identificador El identificador de la ruta.
     * @return La distancia total de la ruta en kilómetros.
     * @throws IllegalArgumentException Si la ruta no existe.
     * @throws IllegalStateException    Si el cliente no está identificado en el
     *                                  servicio externo.
     */

    public double obtenerDistanciaTotal(String identificador) {
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
     * @param identificador El identificador de la ruta.
     * @return El tiempo total de la ruta en minutos.
     * @throws IllegalArgumentException Si la ruta no existe.
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
                / 60; // Convertir segundos a minutos

        return tiempoTotal;
    }

    /**
     * Busca una ruta por su identificador.
     * 
     * @param identificador El identificador de la ruta.
     * @return La ruta encontrada.
     * @throws IllegalArgumentException Si no se encuentra la ruta.
     */
    public Ruta buscarRutaPorIdentificador(String identificador) {
        Ruta ruta = repositorio.buscarRutaPorIdentificador(identificador);
        if (ruta == null) {
            throw new IllegalArgumentException("No se ha encontrado una ruta con el identificador proporcionado.");
        }
        return ruta;
    }
}
