package uva.tds.implementaciones;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.PersistenceException;

import uva.tds.base.Alquiler;
import uva.tds.base.Bicicleta;
import uva.tds.base.Bloqueo;
import uva.tds.base.EstadoBicicleta;
import uva.tds.base.HibernateUtil;
import uva.tds.base.Parada;
import uva.tds.base.Reserva;
import uva.tds.base.Usuario;
import uva.tds.interfaces.IParadaRepositorio;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.hibernate.Transaction;


/**
 * Clase que permite trabajar con Paradas en una base de datos mediante
 * Hibernate
 * @author Emily Rodrigues
 */
public class ParadaRepositorio implements IParadaRepositorio{

    /*
     * La gestión de los usuarios a la hora de realizar una reserva 
     * debe ser tratada por la clase Reserva y no por las paradas.
     * Lo mismo ocurre en el caso de los alquileres.
     */
    private Session getSession() {
		SessionFactory factory = HibernateUtil.getSessionFactory();
		Session session;
		try {
			session = factory.getCurrentSession();
			return session;
		} catch (HibernateException e) {
			e.printStackTrace();
		}

		return null;
	}


    /**
     * Consulta todas las paradas del gestor. Podría estar vacía si se han eliminado
     * todas las paradas
     * @return lista de paradas del gestor. Podría estar vacía si se han eliminado
     * todas las paradas
     */
    @Override
    public ArrayList<Parada> getParadas() {
        Session session =  getSession();
        try {
        
            session.beginTransaction(); 

            List<Parada> resultado = session.createQuery("SELECT p FROM Parada p", Parada.class)
                .getResultList();
            
            session.getTransaction().commit(); 

            return new ArrayList<>(resultado); 

        } catch (HibernateException e) {
            e.printStackTrace();
            if (session != null && session.getTransaction().isActive()) {
                session.getTransaction().rollback(); 
            }
            throw new RuntimeException("Error al obtener las paradas", e); 
        } finally {
                session.close(); 
            
        }
    }



    /**
     * Añade una nueva parada al repositorio.
     * @param parada parada a añadir. No puede ser null.
     * @throws IllegalArgumentException si {@code parada == null}.
     * @throws IllegalStateException si la parada ya está en el repositorio.
     */
    @Override
    public void anadirParada(Parada parada) {
        Session session = getSession();

        try {
            session.beginTransaction();
            session.persist(parada);
            session.flush();
            session.getTransaction().commit();
        } catch (org.hibernate.exception.ConstraintViolationException e) {
            if (session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            throw new IllegalArgumentException();
        } catch (PersistenceException e) {
            if (session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            throw new IllegalArgumentException();
        } finally {
           
                session.close();
            
        }
    }


    private boolean estaBicicleta(Bicicleta bici) {
        ArrayList<Parada> listaParadas = getParadas();
        for (Parada p : listaParadas) {
            if (p.getListaBicicletas().contains(bici)) return true;
        }
        return false;
    }




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
    @Override
    public void agregarBicicleta(String idParada, Bicicleta bicicleta) {
        if (estaBicicleta(bicicleta)) throw new IllegalArgumentException();
        Session session = getSession();
      
        try {
            session.beginTransaction();
            Parada parada = session.get(Parada.class, idParada) ;
            if ((session.get(Parada.class, idParada) == null) || parada.isLlena()) throw new IllegalStateException();
            parada.agregaBicicleta(bicicleta);
			session.update(parada);
            bicicleta.setParada(parada);
			session.getTransaction().commit();
        } catch (HibernateException  e) {
            e.printStackTrace();
            if (session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            throw e;
        } finally {
            session.close();
        }
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
    @Override
    public void eliminarBicicleta(String idParada, String identificadorBici) {
        Session session = getSession();
        
        try {
            session.beginTransaction();

            Parada parada = session.get(Parada.class, idParada);
            if (parada == null) {
                throw new IllegalArgumentException();
            }
            Bicicleta bici = parada.getBicicleta(identificadorBici);
            if (bici.isOcupada()) throw new IllegalArgumentException();
            parada.eliminaBicicleta(identificadorBici);
            bici.setParada(null);

       
            session.update(parada);

            session.getTransaction().commit();

        } catch (HibernateException e) {
            e.printStackTrace();
            if (session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            throw e;
        } finally {
            session.close();
        }
    }


    /**
     * Desactiva una parada a partir de su identificador. No se puede desactivar una parada
     * que no existe. No se puede desactivar una parada que ya está desactivada.
     * @param idParada identificador de la parada. No puede ser null. Debe existir una
     * parada con dicho identificador en el repositorio.
     * @throws IllegalArgumentException si identificador es null
     * @throws IllegalStateException si no hay una parada con ese identificador
     * @throws IllegalStateException si la parada ya estaba desactivada
     */
    @Override
    public void desactivarParada(String idParada) {
        if (idParada == null) throw new IllegalArgumentException("El identificador de la parada no puede ser nulo.");

        ArrayList<Parada> paradas = getParadas();
        Parada parada = paradas.stream()
            .filter(p -> p.getIdentificador().equals(idParada))
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("La parada con id " + idParada + " no existe."));

        if (!parada.isActiva()) throw new IllegalStateException("La parada ya está desactivada.");

        Session session = getSession();
        try {
            session.beginTransaction();

            parada.setEstado(false);
            session.update(parada);

            session.getTransaction().commit();
        } catch (HibernateException e) {
            e.printStackTrace();
            if (session.getTransaction().isActive()) session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
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
    @Override
    public void activarParada(String idParada) {
        if (idParada == null) throw new IllegalArgumentException("El identificador de la parada no puede ser nulo.");

        ArrayList<Parada> paradas = getParadas();
        Parada parada = paradas.stream()
            .filter(p -> p.getIdentificador().equals(idParada))
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("La parada con id " + idParada + " no existe."));

        if (parada.isActiva()) throw new IllegalStateException("La parada ya está activa.");

        Session session = getSession();
        try {
            session.beginTransaction();

            parada.setEstado(true);
            session.update(parada);

            session.getTransaction().commit();
        } catch (HibernateException e) {
            e.printStackTrace();
            if (session.getTransaction().isActive()) session.getTransaction().rollback();
            throw e;
        } finally {
             session.close();
        }
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
        if (idParada == null || idBicicleta == null) throw new IllegalArgumentException("Los identificadores no pueden ser nulos.");
    
        Session session = getSession();
        try {
            session.beginTransaction();
    
            Parada parada = session.get(Parada.class, idParada);
            if (parada == null || !parada.isActiva()) throw new IllegalStateException("La parada no existe o está desactivada.");
    
            Bicicleta bicicleta = parada.getListaBicicletas().stream()
                .filter(b -> b.getIdentificador().equals(idBicicleta))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("La bicicleta no está en la parada."));
    
            Bloqueo bloqueo = new Bloqueo(bicicleta);
            session.merge(bicicleta);
            session.merge(bloqueo);
    
            session.getTransaction().commit();
        } catch (HibernateException e) {
            e.printStackTrace();
            if (session.getTransaction().isActive()) session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }
    


    /**
     * Consulta los bloqueos de bicicletas en el repositorio. 
     * En caso de no haber bloqueos, deuelve una lista vacía.
     * @return lista de bloqueos en el repositorio. Si no hay bloqueos,
     * debuelve una lista vacía.
     */
    @Override
    public ArrayList<Bloqueo> getListaBloqueos() {
        Session session = getSession();
        ArrayList<Bloqueo> listaBloqueos = new ArrayList<>();
        try {
            session.beginTransaction();


            List<Bloqueo> bloqueosActivos = session.createQuery(
                "FROM Bloqueo b WHERE b.fechaFin IS NULL", Bloqueo.class)
                .getResultList();


            listaBloqueos.addAll(bloqueosActivos);

            session.getTransaction().commit();
        } catch (HibernateException e) {
            e.printStackTrace();
            if (session.getTransaction().isActive()) session.getTransaction().rollback();
            throw e;
        } finally {
           session.close();
        }
        return listaBloqueos;
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
    @Override
    public void desbloquearBicicleta(String idParada, String idBicicleta) {
        if (idParada == null || idBicicleta == null) throw new IllegalArgumentException("Los identificadores no pueden ser nulos.");

        Session session = getSession();
        try {
            session.beginTransaction();

            Parada parada = session.get(Parada.class, idParada);
            if (parada == null || !parada.isActiva()) throw new IllegalStateException("La parada no existe o está desactivada.");

            Bicicleta bicicleta = parada.getListaBicicletas().stream()
                .filter(b -> b.getIdentificador().equals(idBicicleta))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("La bicicleta no está en la parada."));

            if (bicicleta.getEstado() != EstadoBicicleta.BLOQUEADA) throw new IllegalStateException("La bicicleta no está bloqueada.");

            bicicleta.setEstado(EstadoBicicleta.DISPONIBLE);
            session.update(bicicleta);

            session.getTransaction().commit();
        } catch (HibernateException e) {
            e.printStackTrace();
            if (session.getTransaction().isActive()) session.getTransaction().rollback();
            throw e;
        } finally {
           session.close();
        }
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
    @Override
    public void reservaBicicleta(String idParada, String idBicicleta, Usuario usuario) {
        if (idParada == null || idBicicleta == null || usuario == null) throw new IllegalArgumentException();
        Session session = getSession();
    
        try {
            session.beginTransaction();
    
  
            Parada parada = session.get(Parada.class, idParada);
            if (parada == null || !parada.isActiva()) {
                throw new IllegalStateException("La parada con id " + idParada + " no existe.");
            }
    

            Bicicleta bicicleta = parada.getListaBicicletas().stream()
                .filter(bici -> bici.getIdentificador().equals(idBicicleta))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("La bicicleta con id " + idBicicleta + " no está en la parada."));
    
       
            if (!bicicleta.isDisponible()) {
                throw new IllegalStateException("La bicicleta no está disponible para reserva.");
            }
    
  
            Usuario usuarioPersistido = session.get(Usuario.class, usuario.getNif());
            if (usuarioPersistido == null || !usuarioPersistido.isActivo()) {
                throw new IllegalStateException("El usuario no está activo o no existe.");
            }

            if (usuarioPersistido.getReservas().stream().anyMatch(reserva -> reserva.isActiva())) {
                throw new IllegalStateException("El usuario ya tiene una reserva activa.");
            }

 
            if (usuarioPersistido.getReservas().stream()
                .anyMatch(r -> r.getBicicleta().getIdentificador().equals(bicicleta.getIdentificador()))) {
                throw new IllegalStateException("El usuario ya tiene una reserva para esta bicicleta.");
            }
    

            Reserva reserva = new Reserva(bicicleta, usuarioPersistido, LocalDateTime.now());
    
    
            session.update(bicicleta);
            session.update(usuarioPersistido);
            session.persist(reserva);     
            session.getTransaction().commit();
    
        } catch (HibernateException e) {
            e.printStackTrace();
            if (session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            throw e;
        } finally {
            session.close();
        }
    }


    private static boolean usuarioTieneAlquiler(Usuario usuario, ArrayList<Alquiler> dondeBuscar) {
        for (Alquiler a : dondeBuscar) {
            if (a.getUsuario().getNif().equals(usuario.getNif())) return true;
        }
        return false;
    }
    


    /**
     * Consulta las reservas activas del gestor. Si no hay ninguna reserva activa, devuelve
     * una lista vacía.
     * @return reservas actuales de bicicletas que hay en el gestor. Si no hay ninguna reserva activa, 
     * devuelve una lista vacía.
     */
    @Override
    public ArrayList<Reserva> getReservasBicicletas() {
        Session session = null;
        List<Reserva> resultado = null;
        try {
            session = getSession();
            session.beginTransaction(); 
 
            resultado = session.createQuery(
                "SELECT r FROM Reserva r", 
                Reserva.class
            ).getResultList();

            session.getTransaction().commit(); 

        } catch (HibernateException e) {
            e.printStackTrace();
            if (session != null && session.getTransaction().isActive()) {
                session.getTransaction().rollback(); 
            }
        } finally {
           
                session.close();
            
        }
        return new ArrayList<>(resultado);
    }


    /**
     * Consulta los alquileres activas del gestor. Si no hay reservas en el gestor,
     * devuelve una lista vacía.
     * @return lista con los alquileres actuales de bicicletas que hay en el gestor,
     * o una lista vacía si no hay reservas.
     */
    @Override
    public ArrayList<Alquiler> getAlquileresEnCurso() {
        Session session = null;
        try {
            session = getSession();
            session.beginTransaction(); 

            List<Alquiler> resultado = session.createQuery(
                "SELECT a FROM Alquiler a WHERE a.fechaFin IS NULL AND a.horaFin IS NULL", 
                Alquiler.class
            ).getResultList();


            return new ArrayList<>(resultado);

        } catch (HibernateException e) {
            e.printStackTrace();
            if (session != null && session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            throw new RuntimeException("Error al obtener los alquileres en curso", e);
        } finally {
            
                session.close(); 
        
        }
    }


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
    @Override
    public void alquilarBicicleta(String idParada, String idBicicleta, Usuario usuario) {
        if (usuario == null || idBicicleta == null  || idParada == null) throw new IllegalArgumentException();
        Session session = getSession();

        try {
            session.beginTransaction();

            Parada parada = session.get(Parada.class, idParada);
            if ( parada == null || !parada.isActiva()) {
                throw new IllegalStateException("La parada con id " + idParada + " no existe.");
            }

            Bicicleta bicicleta = parada.getListaBicicletas().stream()
                .filter(bici -> bici.getIdentificador().equals(idBicicleta))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("La bicicleta con id " + idBicicleta + " no está en la parada."));

            if (bicicleta.getEstado() == EstadoBicicleta.BLOQUEADA || bicicleta.getEstado() == EstadoBicicleta.OCUPADA) {
                throw new IllegalStateException("La bicicleta no está disponible para alquiler.");
            }

            Usuario usuarioPersistido = session.get(Usuario.class, usuario.getNif());
            if (usuarioPersistido == null || !usuarioPersistido.isActivo()) {
                throw new IllegalStateException("El usuario no está activo o no existe.");
            }

            List<Alquiler> alquileres = session.createQuery(
                "SELECT a FROM Alquiler a WHERE a.fechaFin IS NULL AND a.horaFin IS NULL", 
                Alquiler.class
            ).getResultList();
            ArrayList<Alquiler> listaAlquileres = new ArrayList<>(alquileres);

 
            if (usuarioTieneAlquiler(usuarioPersistido, listaAlquileres)) throw new IllegalStateException();

    
            Alquiler alquiler = new Alquiler(bicicleta, usuarioPersistido);

            parada.eliminaBicicleta(idBicicleta);

           
            bicicleta.setEstado(EstadoBicicleta.OCUPADA);
            parada.getListaBicicletas().remove(bicicleta);
            bicicleta.setParada(null);
 
            session.persist(alquiler);
            session.update(bicicleta);
            session.update(parada);

            session.getTransaction().commit();

        } catch (HibernateException e) {
            e.printStackTrace();
            if (session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            throw e;
        } finally {
            session.close();
        }
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
    @Override
    public void devolverBicicleta(String idParada, String nif, Bicicleta bicicleta) {
        if (idParada == null || nif == null || bicicleta == null)
            throw new IllegalArgumentException("Los parámetros no pueden ser nulos.");

        Session session = getSession();
        try {
            session.beginTransaction();

              Parada parada = session.get(Parada.class, idParada);
            if (parada == null) throw new IllegalStateException("La parada no existe.");
            if (!parada.isActiva()) throw new IllegalStateException("La parada está desactivada.");
            if (parada.isLlena()) throw new IllegalStateException("La parada está llena.");


            Usuario usuario = session.get(Usuario.class, nif);
            if (usuario == null) throw new IllegalStateException("El usuario no existe.");


            Bicicleta bicicletaPersistida = session.get(Bicicleta.class, bicicleta.getIdentificador());
            if (bicicletaPersistida == null) throw new IllegalStateException("La bicicleta no existe.");
            if (!bicicletaPersistida.isOcupada()) throw new IllegalStateException("La bicicleta no está ocupada.");

            Alquiler alquiler = usuario.getAlquileres().stream()
                .filter(a -> a.getBicicleta().equals(bicicletaPersistida) && a.getFechaFin() == null)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("El usuario no tiene un alquiler activo para esta bicicleta."));
            alquiler.setFechaFin(LocalDate.now());
            alquiler.setHoraFin(LocalTime.now());

 
            bicicletaPersistida.setEstadoDisponible();
            parada.agregaBicicleta(bicicletaPersistida);
            bicicletaPersistida.setParada(parada);

            session.update(alquiler);
            session.merge(bicicletaPersistida);
            session.update(parada);

            session.getTransaction().commit();
        } catch (HibernateException e) {
            e.printStackTrace();
            if (session.getTransaction().isActive()) session.getTransaction().rollback();
            throw e;
        } finally {
             session.close();
        }
    }


     /**
     * Consulta si un usuario tiene una reserva activa.
     * @param usuario usuario del que se quiere conocer si tiene una reserva. No
     * puede ser null.
     * @return true si el usuario tiene una reserva, false en caso contrario.
     * @throws IllegalArgumentException si {@code usuario == null}
     */
    @Override
    public boolean tieneUsuarioUnaReserva(Usuario usuario) {
        if (usuario == null) throw new IllegalArgumentException();
        ArrayList<Reserva> listaReservas = getReservasBicicletas();
        for (Reserva r : listaReservas) {
            if (r.getUsuario().getNif().equals(usuario.getNif())) {
                return true;
            }
        }
        return false;
    }

      
    public void clearDatabase() {
        Session session = getSession();
        try {
            session.beginTransaction();
    
       
            session.createSQLQuery("DELETE FROM \"RESERVAS\"").executeUpdate();
            session.createSQLQuery("DELETE FROM \"ALQUILERES\"").executeUpdate();
            session.createSQLQuery("DELETE FROM \"BLOQUEOS\"").executeUpdate();
            
            session.createSQLQuery("DELETE FROM \"PARADAS_BICICLETAS\"").executeUpdate();
            session.createSQLQuery("DELETE FROM \"BICICLETAS\"").executeUpdate();
            session.createSQLQuery("DELETE FROM \"PARADAS\"").executeUpdate();
    
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            throw new IllegalStateException("Error al limpiar la base de datos.", e);
        } finally {
            if (session.isOpen()) {
                session.close();
            }
        }
    }
          
}
