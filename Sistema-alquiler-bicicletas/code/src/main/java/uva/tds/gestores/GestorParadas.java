package uva.tds.gestores;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.stream.Collectors;

import uva.tds.base.Alquiler;
import uva.tds.base.Bicicleta;
import uva.tds.base.Bloqueo;
import uva.tds.base.Parada;
import uva.tds.base.Reserva;
import uva.tds.base.Usuario;

/**
 * Clase que representa un gestor de paradas.
 * Almacena una lista de paradas, con sus correspondientes bicicletas, así como
 * sus alquileres, bloqueos
 * y reservas
 * 
 * @author Ainhoa Carbajo Orgaz
 * @author Emily Rodrigues
 */
public class GestorParadas {
    private ArrayList<Parada> listaParadas;
    private ArrayList<Alquiler> alquileres;
    private ArrayList<Bloqueo> bloqueos;
    private ArrayList<Reserva> reservas;

    /**
     * Constructor de un gestor de paradas.
     * Inicializa las listas de paradas, alquileres y bloqueos
     */
    public GestorParadas() {
        listaParadas = new ArrayList<>();
        bloqueos = new ArrayList<>();
        alquileres = new ArrayList<>();
        reservas = new ArrayList<>();
    }

    /**
     * Constructor de un gestor de paradas.
     * Recibe una lista inicial de paradas
     * 
     * @param listaParadas no puede ser vacía ni nula
     * @throws IllegalArgumentException si la lista de paradas está vacía o es nula
     * @throws IllegalArgumentException si hay paradas iguales en la lista
     */
    public GestorParadas(ArrayList<Parada> listaParadas) {
        if (listaParadas == null)
            throw new IllegalArgumentException();
        if (listaParadas.isEmpty())
            throw new IllegalArgumentException();
        if (listaParadas.size() != listaParadas.stream().distinct().count()) {
            throw new IllegalArgumentException();
        }
        this.listaParadas = listaParadas;
    }

    /**
     * Método que devuelve la lista de paradas. Podría estar vacía si se han
     * eliminado
     * todas las paradas
     * 
     * @return ArrayList <Paradas> con las paradas del gesotr, puede estar vacía.
     */
    public ArrayList<Parada> getParadas() {
        return listaParadas;
    }

    /**
     * Método que consulta una parada
     * 
     * @param id identificador de la parada. No puede ser null. Debe existir una
     *           parada con
     *           dicho identificador en el gestor.
     * @return parada consultada
     * @throws IllegalArgumentException si id es null
     * @throws IllegalStateException    si la parada no se encuentra en el gestor
     */
    public Parada getParada(String id) {
        if (id == null)
            throw new IllegalArgumentException();
        for (Parada p : listaParadas) {
            if (p.getIdentificador().equals(id))
                return p;
        }

        throw new IllegalStateException();
    }

    /**
     * Método que devuelve la lista de paradas activas. Puede estar vacía si no hay
     * paradas
     * activas en el gestor.
     * 
     * @return ArrayList <Paradas> de las paradas activas. Puede estar vacía
     */
    public ArrayList<Parada> getParadasActivas() {
        return listaParadas.stream().filter(parada -> parada.isActiva())
                .collect(Collectors.toCollection(ArrayList::new));

    }

    /**
     * Método que añade una parada
     * 
     * @param parada nueva a añadir. No puede ser null. No debe existir una parada
     *               igual en el gestor.
     * @throws IllegalArgumentException si parada es null
     * @throws IllegalStateException    si la parada ya está añadida
     */
    public void anadirParada(Parada parada) {
        if (parada == null)
            throw new IllegalArgumentException();
        if (listaParadas.contains(parada))
            throw new IllegalStateException();
        listaParadas.add(parada);

    }

    /**
     * Método que consulta la lisat de bicicletas disponibles en una parada
     * dado el identificador de la parada
     * 
     * @param id identificador de la parada
     * @return ArrayList<Bicicletas> bicicletas disponibles
     * @throws IllegalArgumentException si id es null
     * @throws IllegalStateException    si no existe una parada con ese
     *                                  identificador
     *                                  en el gestor
     */
    public ArrayList<Bicicleta> getBicicletasParada(String id) {
        Parada p = getParada(id);
        return p.getListaBicicletas();

    }

    /**
     * Método que consulta la lista de
     * estacionamientos disponibles de una parada
     * 
     * @param id identificador de la parada. No puede ser null. Debe existir una
     *           parada
     *           con ese identificador en el gestor.
     * @return el número de aparcamientos disponibles
     * @throws IllegalArgumentException si id es null
     * @throws IllegalStateException    si no existe una parada con ese
     *                                  identificador
     */
    public int getAparcamientosDisponibles(String id) {
        Parada p = getParada(id);
        return p.getAparcamientosDisponibles();

    }

    /**
     * Método que consulta la lista de paradas disponibles dada una latitud,
     * longitud y distancia en ,
     * 
     * @param lat latitud
     * @param lon longitud
     * @param m   distancia en metros de rango. No puede ser menor que cero
     * @return ArrayList<Parada> lista de paradas disponibles en ese rango
     * @throws IllegalArgumentException si m es menor que cero
     */
    public ArrayList<Parada> getParadasDisponiblesUbicacion(double lat, double lon, double m) {
        if (m < 0)
            throw new IllegalArgumentException();
        ArrayList<Parada> validas = new ArrayList<>();
        for (Parada p : listaParadas) {
            if (p.isActiva()) {
                double distancia = calculateDistanceByHaversineFormula(p.getLongitud(), p.getLatitud(), lon, lat);
                if (distancia <= m) {
                    validas.add(p);
                }
            }
        }

        return validas;
    }

    /**
     * Método que añade una bicicleta a una parada
     * 
     * @param idParada  identificador de la parada. No puede ser null. Debe existir
     *                  una parada con
     *                  ese identificador en el gestor.
     * @param bicicleta a añadir. No puede ser null y no pede estar en ninguna otra
     *                  parada del gestor.
     * @throws IllegalArgumentException si idParada o bicicleta es null
     * @throws IllegalStateException    si la bicicleta ya está añadida en el gestor
     * @throws IllegalStateException    si la parada no existe
     */
    public void agregaBicicleta(String idParada, Bicicleta bicicleta) {
        if ((idParada == null) || (bicicleta == null))
            throw new IllegalArgumentException();
        Parada p = getParada(idParada);
        for (Parada parada : listaParadas) {
            if (parada.isBicicletaEnParada(bicicleta.getIdentificador())) {
                throw new IllegalStateException();
            }
        }
        p.agregaBicicleta(bicicleta);

    }

    /*
     * Metodo que calcula la distancia entre dos puntos en la
     * superficie de una esfera.
     * 
     * @param lon1 longitud del punto 1
     * 
     * @param lat1 latitud del punto 1
     * 
     * @param lon2 longitud del punto 2
     * 
     * @param lat2 latitud del punto 2
     * 
     * @return double distancia en m entre los dos puntos
     */
    private static double calculateDistanceByHaversineFormula(double lon1, double lat1, double lon2, double lat2) {
        double earthRadius = 6371; // km

        lat1 = Math.toRadians(lat1);
        lon1 = Math.toRadians(lon1);
        lat2 = Math.toRadians(lat2);
        lon2 = Math.toRadians(lon2);

        double dlon = (lon2 - lon1);
        double dlat = (lat2 - lat1);

        double sinlat = Math.sin(dlat / 2);
        double sinlon = Math.sin(dlon / 2);

        double a = (sinlat * sinlat) + Math.cos(lat1) * Math.cos(lat2) * (sinlon * sinlon);
        double c = 2 * Math.asin(Math.min(1.0, Math.sqrt(a)));

        return earthRadius * c * 1000;

    }

    /**
     * Método que elimina una bicicleta de una parada dado su identificador
     * 
     * @param idParada
     * @param identificadorBici id de la bicicleta a eliminar
     * @throws IllegalArgumentException si idParada o identificadorBici son nulos
     * @throws IllegalStateException    si la bicicleta no está en la parada
     * @throws IllegalStateException    si la parada no existe
     * @throws IllegalStateException    si la bicicleta está ocupada
     */
    public void eliminarBicicleta(String idParada, String identificadorBici) {
        if ((idParada == null) || (identificadorBici == null))
            throw new IllegalArgumentException();

        boolean eliminada;
        compruebaSiLaBiciAEliminarEstaAlquilada(identificadorBici);

        eliminada = eliminaLaBiciSiEstaBloqueada(identificadorBici);
        if (!eliminada) {
            eliminada = eliminaLaBiciSiEstaReservada(identificadorBici);
        }
        if (!eliminada) {
            getParada(idParada).eliminaBicicleta(identificadorBici);
        }
    }


    private void compruebaSiLaBiciAEliminarEstaAlquilada(String idBici) {
        for (Alquiler a : alquileres) {
            if (a.getBicicleta().getIdentificador().equals(idBici)) {
                throw new IllegalStateException();
            }
        }
    }



    private boolean eliminaLaBiciSiEstaBloqueada(String idBici) {
        int i = -1;
        boolean eliminada = false;
        for (Bloqueo b : bloqueos) {
            if (b.getBicicleta().getIdentificador().equals(idBici)) {
                i = bloqueos.indexOf(b);
                eliminada = true;
            }
        }
        if (i >= 0)
            bloqueos.remove(i);
        return eliminada;
    }



    private boolean eliminaLaBiciSiEstaReservada(String idBici) {
        int i = -1;
        boolean eliminada = false;
        for (Reserva r : reservas) {
            if (r.getBicicleta().getIdentificador().equals(idBici)) {
                i = reservas.indexOf(r);
                eliminada = true;
            }
        }
        if (i >= 0)
            reservas.remove(i);
        return eliminada;
    }

    /**
     * Consulta si una parada se encuentra en el gestor a partir de su
     * identificador.
     * 
     * @param idParada identificador de la parada de la que se quiere saber si
     *                 existe dentro del gestor. No puede ser null.
     * @return true si se encuentra una parada con dicho identificador, false
     *         en caso contrario.
     * @throws IllegalArgumentException si idParada es null.
     */

    public boolean isParadaEnGestor(String idParada) {
        if (idParada == null)
            throw new IllegalArgumentException();
        for (Parada p : listaParadas) {
            if (p.getIdentificador().equals(idParada))
                return true;
        }
        return false;
    }

    /**
     * Método que alquila una bicicleta. registra el alquiler y establece la
     * bicicleta en estado Ocupada.
     * Se pueden alquilar bicicletas reservadas si no ha pasado más de una hora
     * desde que se reservó la
     * bicicleta y si alquila el usuario que hizo la reserva
     * 
     * @param idParada identificador de la parada que tiene la bicicleta. No puede
     *                 ser null. Debe existir
     *                 una parada con dicho identificador en el gestor.
     * @param idBici   identificador de la bicicleta disponible o reservada que se
     *                 desea alquilar. No puede ser
     *                 null. Debe encontrarse una bicicleta con ese identificador en
     *                 la parada especificada. Debe tener estado
     *                 DISPONIBLE o RESERVADA la bicicleta.
     * @param usuario  usuario que realiza en alquiler. No puede ser null. En caso
     *                 de que la bicicleta se
     *                 encuentre RESERVADA, el usuario debe tener una reserva
     *                 asociada a dicha bicicleta en el gestor.
     * @throws IllegalArgumentException si idParada, idBici o usuario son null.
     * @throws IllegalStateException    si la bicicleta no está en la parada
     * @throws IllegalStateException    si la parada no está en el gestor
     * @throws IllegalStateException    si el usuario pasado no hizo la reserva, en
     *                                  caso de que la bicicleta
     *                                  tenga estado RESERVADA.
     * @throws IllegalStateException    si el usuario ya tiene un alquiler en curso
     * @throws IllegalStateException    si el usuario no está activo
     * @throws IllegalStateException    si la bicicleta está bloqueada
     * @throws IllegalStateException    si la parada no está activa
     */

    public void alquilarBicicleta(String idParada, String idBici, Usuario usuario) {
        if ((idParada == null) || (idBici == null) || (usuario == null))
            throw new IllegalArgumentException();
        Parada parada = getParada(idParada);
        Bicicleta bici;
        Reserva reserva;
        if (!parada.isActiva())
            throw new IllegalStateException();
        if (tieneAlquilerEnCurso(usuario))
            throw new IllegalStateException();

        bici = parada.getBicicleta(idBici);

        if (bici.isReservada()) {
            reserva = getReserva(idBici);

            if (compruebaSiHaPasadoElTiempoLimiteDeReserva(reserva)) {
                alquilaCuandoSeAcaboElTiempoDeReserva(parada, reserva, bici, usuario);
            } else {
                alquilaConTiempoEnRangoDeTiempoDeReserva(parada, reserva, bici, usuario);
            }

        } else if (bici.isDisponible()) {
            alquilaConBiciDisponible(parada, usuario, bici);
        } else {
            throw new IllegalStateException();
        }
    }

    private void alquilaCuandoSeAcaboElTiempoDeReserva(Parada parada, Reserva reserva,
            Bicicleta bici, Usuario usuario) {
        eliminaReserva(reserva);
        parada.setBicicletaEstadoDisponible(bici.getIdentificador());
        agregaAlquiler(bici, usuario);
        parada.eliminaBicicleta(bici.getIdentificador());
    }

    private void alquilaConTiempoEnRangoDeTiempoDeReserva(Parada parada, Reserva reserva,
            Bicicleta bici, Usuario usuario) {
        if (!isUsuariosIguales(reserva.getUsuario(), usuario))
            throw new IllegalStateException();
        agregaAlquiler(bici, usuario);
        eliminaReserva(reserva);
        parada.eliminaBicicleta(bici.getIdentificador());
    }

    private void alquilaConBiciDisponible(Parada parada, Usuario usuario, Bicicleta bici) {
        agregaAlquiler(bici, usuario);
        parada.eliminaBicicleta(bici.getIdentificador());
    }

    private boolean isUsuariosIguales(Usuario usuario, Usuario otro) {
        String nifUsuario = usuario.getNif();
        String nifOtro = otro.getNif();
        return nifUsuario.equals(nifOtro);
    }

    private void eliminaReserva(Reserva reserva) {
        reservas.remove(reserva);
    }

    private void agregaAlquiler(Bicicleta bici, Usuario usuario) {
        Alquiler alquiler = new Alquiler(bici, usuario);
        alquileres.add(alquiler);
    }

    private Reserva getReserva(String idBicicleta) {
        Reserva reserva = null;
        for (Reserva r : reservas) {
            if (r.getBicicleta().getIdentificador().equals(idBicicleta)) {
                reserva = r;
            }
        }
        return reserva;
    }

    private boolean compruebaSiHaPasadoElTiempoLimiteDeReserva(Reserva reserva) {
        return reserva.isTiempoLimiteAlcanzado(LocalDateTime.now(), 1);
    }

    /**
     * Método que obtiene un alquiler a partir de un usuario
     * 
     * @param nifUsuario nif del usuario del que se desea obtener el alquiler de una
     *                   bici dada. No puede ser null y debe tener un alquiler
     *                   asociado en el gestor
     * @param idBici     identificador de la bicicleta de la que se desea obtener el
     *                   alquiler.
     *                   No puede ser null. Debe tener un alquiler asociado.
     * @return alquiler asignado al usuario con dicha bicicleta
     * @throws IllegalArgumentException si nifUsuario o idBici son null
     * @throws IllegalStateException    si el usuario no tiene ningun alquiler
     *                                  asignado
     * @throws IllegalStateException    si la bicicleta no tiene ningun alquiler
     *                                  asignado
     */
    public Alquiler getAlquiler(String nifUsuario, String idBici) {
        if (nifUsuario == null || idBici == null)
            throw new IllegalArgumentException();
        for (Alquiler a : alquileres) {
            if (a.getUsuario().getNif().equals(nifUsuario) &&
                    a.getBicicleta().getIdentificador().equals(idBici))
                return a;
        }
        throw new IllegalStateException();
    }

    /**
     * Método que devuelve una bicicleta, es decir, finaliza un alquiler en curso
     * relaizado por un
     * usuario activo del sistema.
     * 
     * @param idParada   identificador de la parada en la que se quiere depositar la
     *                   bicicleta.
     *                   Debe existir una parada con dicho identificador en el
     *                   gestor. No puede ser null.
     * @param nifUsuario NIF del usuario que realizó el alquiler de la bicicleta y
     *                   que ahora
     *                   quiere devolver. No puede ser null. Debe tener un alquiler
     *                   asociado en el gestor.
     * @param bici       bicicleta que se quiere devolver. No puede ser null. DEbe
     *                   estar ocupada.
     * 
     * @throws IllegalArgumentException si idParada, nifUsuario o bici son null
     * @throws IllegalStateException    si la parada no está en el gestor
     * @throws IllegalStateException    si no se encuentra un usuario con ese NIF
     *                                  que haya
     *                                  realizado una reserva en el sistema.
     * @throws IllegalStateException    si la parada no está activa
     * @throws IllegalStateException    si la parada está llena
     * @throws IllegalStateException    si la bicicleta no estaba ocupada
     */
    public void devolverBicicleta(String idParada, String nifUsuario, Bicicleta bici) {
        Parada parada = getParada(idParada);
        boolean usuarioReservoLaBici = false;

        if (!parada.isActiva())
            throw new IllegalStateException();

        Iterator<Alquiler> iterador = alquileres.iterator();
        while (iterador.hasNext()) {
            Alquiler a = iterador.next();
            if (a.getBicicleta().equals(bici) && a.getUsuario().getNif().equals(nifUsuario)) {
                a.setFechaFin(LocalDate.now());
                a.setHoraFin(LocalTime.now());
                usuarioReservoLaBici = true;
                iterador.remove();
            }
        }

        if (usuarioReservoLaBici) {
            bici.setEstadoDisponible();
            parada.agregaBicicleta(bici);
        } else {
            throw new IllegalStateException();
        }
    }

    /**
     * Método que verifica que un usuario tiene un alquiler en curso
     * 
     * @param usuario usuario del que se quiere verificar que tiene un alquiler.
     *                No puede ser null.
     * @return true si lo tiene, false ne caso contrario
     * @throws IllegalArgumentException si usuario es null
     */
    public boolean tieneAlquilerEnCurso(Usuario usuario) {
        if (usuario == null)
            throw new IllegalArgumentException();
        for (Alquiler a : alquileres) {
            if (a.getUsuario().getNif().equals(usuario.getNif())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Método que consulta la lista de alquileres en curso. Puede estar vacía si no
     * hay alquileres en curso.
     * 
     * @return ArrayList <Alquiler> lista de alquileres en curso, puede estar vacía
     */
    public ArrayList<Alquiler> getAlquileresEnCurso() {
        return alquileres;
    }

    /**
     * Método que bloquea una bicicleta de una parada
     * 
     * @param idParada identificador de la parada de la que se desea bloquear la
     *                 bicicleta.
     *                 No puede ser null y debe estar activa para poder desbloquear
     *                 la bicicleta.
     * @param idBici   identificador de la bicicleta que se desea bloquear. No puede
     *                 ser
     *                 null y debe encontrarse en la parada especificada. La
     *                 bicicleta no debe estar bloqueada
     *                 al realizar esta operación.
     * @throws IllegalStateException    si la parada no está en el gestor
     * @throws IllegalArgumentException si idParada == null
     * @throws IllegalStateException    si la bicicleta no está en la parada
     *                                  indicada
     * @throws IllegalArgumentException si idBici == null
     * @throws IllegalStateException    si la bicicleta ya está bloqueada
     * @throws IllegalStateException    si la parada está desactivada
     */
    public void bloquearBicicleta(String idParada, String idBici) {
        if ((idParada == null) || (idBici == null))
            throw new IllegalArgumentException();
        Parada parada = getParada(idParada);
        Bicicleta bici;
        if (!parada.isActiva())
            throw new IllegalStateException();

        bici = parada.getBicicleta(idBici);
        if (bici.isBloqueada())
            throw new IllegalStateException();

        Bloqueo bloqueo = new Bloqueo(bici);
        parada.setBicicletaEstadoBloqueada(idBici);
        anadirBloqueo(bloqueo);
    }

    /**
     * Método que consulta la lista de bloqueos de bicicletas. Puede estar vacía
     * si no hay bloqueos activos en el sistema.
     * 
     * @return la lista con los bloqueos, puede estar vacía (si no hay bloqueos).
     */
    public ArrayList<Bloqueo> getListaBloqueos() {
        return bloqueos;
    }

    /**
     * Método que obtiene un bloqueo a partir de una bicicleta
     * 
     * @param idBici identificador de la biciceleta de la que se desea obtener el
     *               bloqueo.
     *               No puede ser null y debe estar bloqueada la bicicleta con
     *               anterioridad para obtener
     *               el bloqueo.
     * @return bloqueo asignado a la bicicleta
     * @throws IllegalArgumentException si idBici es null
     * @throws IllegalStateException    si la bicicleta no tiene ningun bloqueo
     *                                  asignado
     */
    public Bloqueo getBloqueo(String idBici) {
        if (idBici == null)
            throw new IllegalArgumentException();
        for (Bloqueo b : bloqueos) {
            if (b.getBicicleta().getIdentificador().equals(idBici))
                return b;
        }
        throw new IllegalStateException();
    }

    private void anadirBloqueo(Bloqueo b) {
        bloqueos.add(b);
    }

    /**
     * Método que desbloquea una bicicleta bloqueada
     * 
     * @param idParada identenficador de la parada en la que se localiza la
     *                 bicicleta. Debe
     *                 existir una parada con ese identificador en el gestor. Debe
     *                 estar activa para poder
     *                 desbloquear a una bicicleta. No puede ser null.
     * @param idBici   identificador de la bicicleta que se desea desbloquear. No
     *                 puede ser
     *                 null.
     * @throws IllegalArgumentException si idParada o idBici son nulos.
     * @throws IllegalStateException    si la parada no está en el gestor.
     * @throws IllegalStateException    si la bicicleta no está en la parada.
     * @throws IllegalStateException    si la bicicleta no está bloqueada
     * @throws IllegalStateException    si la parada está desactivada
     * 
     */
    public void desbloquearBicicleta(String idParada, String idBici) {
        if (idParada == null || idBici == null)
            throw new IllegalArgumentException();
        Parada parada = getParada(idParada);
        Bicicleta bici;

        if (!parada.isActiva())
            throw new IllegalStateException();
        bici = parada.getBicicleta(idBici);

        if (!bici.isBloqueada())
            throw new IllegalStateException();

        Bloqueo b = getBloqueo(idBici);
        b.setFechaFin(LocalDate.now());
        b.setHoraFin(LocalTime.now());

        parada.setBicicletaEstadoDisponible(idBici);
        bloqueos.remove(b);
    }

    /**
     * Permite que un usuario pueda reserva una bicicleta que se encuentra en una
     * parada
     * a partir de sus identificadores
     * 
     * @param idParada    identificador de la parada. No puede ser null y debe
     *                    existir
     *                    en el gestor
     * @param idBicicleta identificador de la bicicleta que se quiere reserva. No
     *                    puede
     *                    ser null y debe existir en la parada indicada.
     * @param usuario     usuario que quiere reserva la bicicleta. No puede ser null
     *                    y debe estar
     *                    activo.
     * @throws IllegalArgumentException si
     *                                  {@code (idParada == null) || (idBicicleta == null) 
     * || (usuario == null)}
     * @throws IllegalStateException    si la parada no se encuentra en el gestor.
     * @throws IllegalStateException    si la bicicleta no se encuentra en la parada
     *                                  dada.
     * @throws IllegalStateException    si el usuario está inactivo o tiene otra
     *                                  reserva
     * @throws IllegalStateException    si la parada está desactivada
     * @throws IllegalStateException    si la bicicleta no está disponible
     */
    public void reservaBicicleta(String idParada, String idBicicleta, Usuario usuario) {
        Parada parada = getParada(idParada);
        Bicicleta bici;
        Reserva reserva;

        if (!parada.isActiva() || tieneUsuarioUnaReserva(usuario))
            throw new IllegalStateException();
        bici = parada.getBicicleta(idBicicleta);
        reserva = new Reserva(bici, usuario, LocalDateTime.now());

        parada.setBicicletaEstadoReservada(idBicicleta);
        reservas.add(reserva);
    }

    /**
     * Consulta si un usuario tiene una reserva activa.
     * 
     * @param usuario usuario del que se quiere conocer si tiene una reserva. No
     *                puede ser null.
     * @return true si el usuario tiene una reserva, false en caso contrario.
     * @throws IllegalArgumentException si {@code usuario == null}
     */
    public boolean tieneUsuarioUnaReserva(Usuario usuario) {
        if (usuario == null)
            throw new IllegalArgumentException();
        for (Reserva r : reservas) {
            if (r.getUsuario().getNif().equals(usuario.getNif())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Consulta las reservas activas del gestor. Si no hay ninguna reserva activa,
     * devuelve
     * una lista vacía.
     * 
     * @return reservas actuales de bicicletas que hay en el gestor. Si no hay
     *         ninguna reserva activa,
     *         devuelve una lista vacía.
     */
    public ArrayList<Reserva> getReservasBicicletas() {
        return reservas;
    }

    /**
     * Desactiva una parada a partir de su identificador
     * 
     * @param idParada identificador de la parada. No puede ser null. Debe existir
     *                 una parada
     *                 con ese identificador en el gestor.
     * @throws IllegalArgumentException si identificador es null
     * @throws IllegalStateException    si no hay una parada
     *                                  con ese identificador
     * @throws IllegalStateException    si la parada ya estaba desactivada
     */
    public void desactivarParada(String idParada) {
        Parada p = getParada(idParada);
        if (!p.isActiva())
            throw new IllegalStateException();
        p.setEstado(false);
    }

    /**
     * Activa una parada a partir de su identificador
     * 
     * @param idParada identificador de la parada. No puede ser null. Debe existir
     *                 una parada
     *                 con ese identificador en el gestor.
     * @throws IllegalArgumentException si identificador es null
     * @throws IllegalStateException    si no hay una parada
     *                                  con ese identificador
     * @throws IllegalStateException    si la parada ya estaba sactivada
     */
    public void activarParada(String idParada) {
        Parada p = getParada(idParada);
        if (p.isActiva())
            throw new IllegalStateException();
        p.setEstado(true);
    }

}