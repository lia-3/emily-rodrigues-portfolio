package uva.tds.gestorenaislamiento;
import uva.tds.interfaces.IParadaRepositorio;
import uva.tds.base.*;
import java.util.ArrayList;


/**
 * Clase que representa un gestor de paradas en aislamiento.
 * Permite el alquiler, reserva, bloqueo, devolución y eliminación de una bicicleta
 * de una parada así como la adición de bicis en una parada del gestor.
 * También permite realizar operaciones sobre las paradas como añadir, activar y desactivar
 * paradas.
 * Esta clase trabaja con persistencia que han de seguir la interface {@code IParadaRepositorio}
 * @author Emily Rodrigues
 */
public class GestorParadasEnAislamiento {

    private IParadaRepositorio paradaRepo;

    /**
     * Constructor de un gestor de paradas a partir de un repositorio.
     * @param paradaRepo repositorio que contiene la información de las paradas y bicicletas.
     * No puede ser null.
     * @throws IllegalArgumentException si {@code paradaRepo == null}
     */
    public GestorParadasEnAislamiento(IParadaRepositorio paradaRepo) {
        if (paradaRepo == null) throw new IllegalArgumentException();
        this.paradaRepo = paradaRepo;
    }


    /**
     * Consulta todas las paradas del gestor. Podría estar vacía si se han eliminado
     * todas las paradas
     * @return lista de paradas del gestor. Podría estar vacía si se han eliminado
     * todas las paradas
     */
    public ArrayList<Parada> getParadas() {
        return paradaRepo.getParadas();
    }


    /**
     * Obtiene una parada a partir de su identificador.
     * @param id identificador de la parada. No puede ser null y debe existir en el repositorio.
     * @return la parada correspondiente.
     * @throws IllegalArgumentException si {@code id == null}.
     * @throws IllegalStateException si la parada no se encuentra en el repositorio.
     */
    public Parada getParada(String id) {
        if (id == null) throw new IllegalArgumentException();
        ArrayList<Parada> listaParadas = paradaRepo.getParadas();
        for (Parada p: listaParadas) {
            if (p.getIdentificador().equals(id)) {
                return p;
            }
        }
        throw new IllegalStateException();
    }


    /**
     * Consulta las paradas activas.
     * @return lista de paradas activas. En caso de no tener ninguna parada
     * activa, devuelve una lista vacía (no nula).
     */
    public ArrayList<Parada> getParadasActivas() {
        ArrayList<Parada> paradasRepositorio = paradaRepo.getParadas();
        ArrayList<Parada> paradasActivas = new ArrayList<>();
        for (Parada p : paradasRepositorio) {
            if(p.isActiva()) {
                paradasActivas.add(p);
            }
        }
        return paradasActivas;
    }


    /**
     * Añade una nueva parada al repositorio.
     * @param parada parada a añadir. No puede ser null.
     * @throws IllegalArgumentException si {@code parada == null}.
     * @throws IllegalStateException si la parada ya está en el repositorio.
     */
    public void anadirParada(Parada parada) {
        if (parada == null) throw new IllegalArgumentException();
        paradaRepo.anadirParada(parada);
    }


    /**
     * Consulta las bicicletas disponibles de una parada dada a partir de su identificador.
     * @param idParada identificador de la parada. No puede ser null. Debe existir dicha parada
     * en el gestor.
     * @return lista de bicicletas disponibles. Devuelve una lista vacía (no nula) en caso de no 
     * haber bicicletas en la parada.
     * @throws IllegalArgumentException si {@code idParada == null}.
     * @throws IllegalStateException si la parada no está en el repositorio.
     */
    public ArrayList<Bicicleta> getBicicletasParada(String idParada) {
        if (idParada == null) throw new IllegalArgumentException();
        Parada parada = getParada(idParada);
        return parada.getListaBicicletas();
    }


    /**
     * Devuelve el número de aparcamientos disponibles en una parada dada a partir de su identificador.
     * @param idParada identificador de la parada. No puede ser null. Debe existir dicha parada
     * en el gestor.
     * @return número de aparcamientos disponibles.
     * @throws IllegalArgumentException si {@code idParada == null}.
     * @throws IllegalStateException si la parada no está en el repositorio.
     */
    public int getAparcamientosDisponibles(String idParada) {
        if (idParada == null) throw new IllegalArgumentException();
        Parada parada = getParada(idParada);
        return parada.getAparcamientosDisponibles();
    }


    /**
     * Consulta las paradas disponibles dentro de una distancia (en metros) dado desde una ubicación específica.
     * @param lat latitud del punto de referencia.
     * @param lon longitud del punto de referencia.
     * @param distanciaMax distancia máxima en metros. No puede ser menor que cero.
     * @return lista de paradas dentro del rango especificado.
     * @throws IllegalArgumentException si {@code distanciaMax < 0}.
     */
    public  ArrayList<Parada> getParadasDisponiblesUbicacion(double lat, double lon, double distanciaMax) {
        if (distanciaMax < 0) throw new IllegalArgumentException();
        ArrayList <Parada> validas = new ArrayList<>();
        for (Parada p : paradaRepo.getParadas()) {
            if(p.isActiva()){
            double distancia = calculateDistanceByHaversineFormula(p.getLongitud(),p.getLatitud(), lon, lat);
                if (distancia <= distanciaMax) {
                    validas.add(p);
                }
            }
        }
        return validas;
    } 


    /*
     * Método que calcula la distancia entre dos puntos en la 
     * superficie de una esfera.
     * @param lon1 longitud del punto 1
     * @param lat1 latitud del punto 1
     * @param lon2 longitud del punto 2
     * @param lat2 latitud del punto 2
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
        
        double a = (sinlat * sinlat) + Math.cos(lat1)*Math.cos(lat2)*(sinlon*sinlon);
        double c = 2 * Math.asin (Math.min(1.0, Math.sqrt(a)));
        
        return earthRadius * c * 1000;
        
    }


    /**
     * Añade una bicicleta a una parada específica.
     * @param idParada identificador de la parada. No puede ser null. Debe existir dicha parada
     * en el gestor.
     * @param bicicleta bicicleta a añadir. No puede ser null. No se podrá añadir la bicicleta
     * a la parada si ya existe otra bicicleta con el mismo identificador en el gestor.
     * @throws IllegalArgumentException si {@code idParada == null} o {@code bicicleta == null}.
     * @throws IllegalStateException si la bicicleta ya está en el gestor.
     * @throws IllegalStateException si la parada no está en el repositorio.
     */
    public void agregarBicicleta(String idParada, Bicicleta bicicleta) {
        if (idParada == null || bicicleta == null) {
            throw new IllegalArgumentException("El identificador de la parada y la bicicleta no pueden ser nulos.");
        }
        paradaRepo.agregarBicicleta(idParada, bicicleta);
    }


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
    public void eliminarBicicleta(String idParada, String idBicicleta) {
        paradaRepo.eliminarBicicleta(idParada, idBicicleta);
    }


    /**
     * Consulta si una parada se encuentra en el gestor a partir de su 
     * identificador.
     * @param idParada identificador de la parada de la que se quiere saber si
     * existe dentro del gestor. No puede ser null.
     * @return true si se encuentra una parada con dicho identificador, false
     * en caso contrario.
     * @throws IllegalArgumentException si idParada es null.
     */
    public boolean isParadaEnGestor(String idParada) {
        if (idParada == null) throw new IllegalArgumentException();
        for (Parada p: paradaRepo.getParadas()) {
            if (p.getIdentificador().equals(idParada)) return true;
        }
        return false;
    }


    /**
     * Desactiva una parada a partir de su identificador
     * @param idParada identificador de la parada
     * @throws IllegalArgumentException si identificador es null
     * @throws IllegalStateException si no hay una parada
     * con ese identificador
     * @throws IllegalStateException si la parada ya estaba desactivada
     */
    public void desactivarParada(String idParada) {
        if (!isParadaEnGestor(idParada)) throw new IllegalStateException();
        paradaRepo.desactivarParada(idParada);
    }


    /**
     * Activa una parada a partir de su identificador. Debe estar desactivada. 
     * Debe existir una parada con dicho identificador en el gestor.
     * @param idParada identificador de la parada. No puede ser null. 
     * @throws IllegalArgumentException si identificador es null
     * @throws IllegalStateException si no hay una parada
     * con ese identificador
     * @throws IllegalStateException si la parada ya estaba activa.
     */
    public void activarParada(String idParada) {
        if (!isParadaEnGestor(idParada)) throw new IllegalStateException();
        paradaRepo.activarParada(idParada);
    }


    /**
     * Método que obtiene un bloqueo a partir del identificador de una bicicleta.
     * @param idBicicleta identificador de la biciceleta de la que se desea obtener el bloqueo. 
     * No puede ser null y debe estar bloqueada la bicicleta con anterioridad para obtener
     * el bloqueo.
     * @return bloqueo asignado a la bicicleta
     * @throws IllegalArgumentException si el idBicicleta es null.
     * @throws IllegalStateException si la bicicleta no tiene ningun bloqueo asignado.
     */
    public Bloqueo getBloqueo(String idBicicleta) {
        if (idBicicleta == null) throw new IllegalArgumentException();
        for (Bloqueo b : getListaBloqueos()) {
            if (b.getBicicleta().getIdentificador().equals(idBicicleta)) {
                return b;
            }
        }
        throw new IllegalStateException();
    }


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
    public void bloquearBicicleta(String idParada, String idBicicleta) {
        if (!isParadaEnGestor(idParada)) throw new IllegalStateException();
        if (idBicicleta == null) throw new IllegalArgumentException();
        paradaRepo.bloquearBicicleta(idParada, idBicicleta);
    }


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
    public void desbloquearBicicleta(String idParada, String idBicicleta) {
        if (!isParadaEnGestor(idParada)) throw new IllegalStateException();
        paradaRepo.desbloquearBicicleta(idParada, idBicicleta);
    }


    /**
     * Consulta los bloqueos de bicicletas en el repositorio. 
     * En caso de no haber bloqueos, deuelve una lista vacía.
     * @return lista de bloqueos en el repositorio. Si no hay bloqueos,
     * debuelve una lista vacía.
     */
    public ArrayList<Bloqueo> getListaBloqueos() {
        return paradaRepo.getListaBloqueos();
    }


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
    public void reservaBicicleta(String idParada, String idBicicleta, Usuario usuario){
        paradaRepo.reservaBicicleta(idParada, idBicicleta, usuario);
    }


    /**
     * Consulta las reservas activas del gestor. Si no hay reservas en el gestor,
     * devuelve una lista vacía.
     * @return lista con las reservas actuales de bicicletas que hay en el gestor,
     * o una lista vacía si no hay reservas.
     */
    public ArrayList<Reserva> getReservasBicicletas() {
        return paradaRepo.getReservasBicicletas();
    }


    /**
     * Método que consulta la lista de alquileres en curso. Devuelve una lista
     * vacía si no hay alquileres en curso.
     * @return lista de alquileres en curso, puede estar vacía (no hay alquileres activos
     * en ese momento).
     */
    public ArrayList<Alquiler> getAlquileresEnCurso() {
        return paradaRepo.getAlquileresEnCurso();
    }


    /**
     * Método que verifica que un usuario tiene un alquiler en curso
     * @param usuario usuario del que se quiere verificar que tiene un alquiler.
     * No puede ser null.
     * @return true si lo tiene, false en caso contrario 
     * @throws IllegalArgumentException si {@code nif == null}
     */
    public boolean tieneAlquilerEnCurso(String nif) {
        if (nif == null) throw new IllegalArgumentException();
        for (Alquiler a : getAlquileresEnCurso()) {
            if (a.getUsuario().getNif().equals(nif)) {
                return true;
            }
        }
        return false;
    }


    /**
     * Método que obtiene un alquiler a partir de un usuario. Debe existir un alquiler asociado
     * al usuario con una bicicleta dada.
     * @param nifUsuario nif del usuario del que se desea obtener el alquiler de una 
     * bici dada. No puede ser null y debe tener un alquiler asociado en el gestor.
     * @param idBici identificador de la bicicleta de la  que se desea obtener el alquiler.
     * No puede ser null. Debe tener un alquiler asociado con el usuario
     * @return alquiler asignado al usuario con dicha bicicleta
     * @throws IllegalArgumentException si nifUsuario o idBici son null
     * @throws IllegalStateException si el usuario no tiene ningun alquiler asignado
     * @throws IllegalStateException si la bicicleta no tiene ningun alquiler asignado
     */
    public Alquiler getAlquiler(String nif, String idBicicleta) {
        if ((nif == null) || (idBicicleta == null)) throw new IllegalArgumentException();
        for (Alquiler a: getAlquileresEnCurso()) {
            if (a.getUsuario().getNif().equals(nif) && a.getBicicleta().getIdentificador().equals(idBicicleta)) {
                return a;
            }
        }
        throw new IllegalStateException();
    }


    /**
     * Método que alquila una bicicleta. Registra el alquiler y establece la bicicleta en estado OCUPADA.
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
    public void alquilarBicicleta(String idParada, String idBicicleta, Usuario usuario) {
        paradaRepo.alquilarBicicleta(idParada, idBicicleta, usuario);
    }


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
    public void devolverBicicleta(String idParada, String nif, Bicicleta bici){ 
        paradaRepo.devolverBicicleta(idParada, nif, bici);
    }


    /**
     * Consulta si un usuario tiene una reserva activa.
     * @param usuario usuario del que se quiere conocer si tiene una reserva. No
     * puede ser null.
     * @return true si el usuario tiene una reserva, false en caso contrario.
     * @throws IllegalArgumentException si {@code usuario == null}
     */
    public boolean tieneUsuarioUnaReserva(Usuario usuario) {
        return paradaRepo.tieneUsuarioUnaReserva(usuario);
    }
}