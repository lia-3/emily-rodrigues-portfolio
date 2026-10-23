package uva.tds.implementaciones;

import uva.tds.base.Recompensa;
import uva.tds.base.Usuario;
import uva.tds.base.HibernateUtil;
import uva.tds.interfaces.IRecompensaRepositorio;
import org.hibernate.HibernateException;
import org.hibernate.query.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;


import java.util.ArrayList;
import java.util.List;

/**
 * Clase que permite trabajar con Usuarios en una base de datos mediante
 * Hibernate
 * @author Ainhoa Carbajo Orgaz
 */
public class RecompensaRepositorio implements IRecompensaRepositorio{

    private Session getSession() {
		SessionFactory factory = HibernateUtil.getSessionFactory();
		try {
			return factory.getCurrentSession();
		} catch (HibernateException e) {
			throw new RuntimeException("Error al obtener la sesión de Hibernate", e);
		}catch(Exception e){
			throw new RuntimeException("Error inesperado al obtener la sesión", e);
		}

		
	}


	/**
     * Añade una recompensa al sistema.
     *
     * @param recompensa La recompensa a añadir.
     * @throws IllegalArgumentException si la recompensa es nula
     * @throws IllegalArgumentException si ya existe una recompensa con el mismo identificador.
     */
    @Override
    public void addRecompensa(Recompensa recompensa) {
        if (recompensa == null) {
			throw new IllegalArgumentException();
		}

        if (getRecompensa(recompensa.getId()) != null) {
			throw new IllegalArgumentException("La recompensa con ese identificador ya existe");
		}
        Session session = getSession();

		try {
			session.beginTransaction();

			session.persist(recompensa);
			session.flush();

		} catch (HibernateException e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		} finally {
			session.close();
		}

        
    }


	/**
     * Asigna una recompensa a un usuario si tiene suficiente puntuación.
     *
     * @param usuario    El usuario que quiere obtener la recompensa.
     * @param idRecompensa El id de la recompensa a añadir.
     * @throws IllegalArgumentException si el usuario o el id de la recompensa es nula
     * @throws IllegalArgumentException si el usuario no tiene suficientes puntos o la recompensa esta inactiva.
     */
    @Override
    public void addRecompensaUsuario(Usuario usuario, String id) {
      Session session = getSession();
      
      
        try {
            session.beginTransaction();
            Recompensa recompensa = session.get(Recompensa.class, id);
            recompensa.setUsuario(usuario);
			session.update(recompensa);
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
     * Obtiene una recompensa dado su id
     * @param id
     * @return Recompensa
     */
    @Override
    public Recompensa getRecompensa(String id) {
        Session session = getSession();

		try {
			session.beginTransaction();

			return session.get(Recompensa.class, id);
			

		} catch (HibernateException  e) {
			e.printStackTrace();
			session.getTransaction().rollback();
			throw new RuntimeException("Error al obtener la recompensa de la base de datos", e);
		} finally {
			session.close();
		}
		
    }


	/**
     * Obtiene una lista de las recompensas activas en el sistema.
     *
     * @return Lista de recompensas activas.
     */
    @Override
    public ArrayList<Recompensa> obtenerRecompensasActivas() {
        Session session = getSession();

		try {
			session.beginTransaction();
			List <Recompensa> resultado=session.createQuery("SELECT r FROM Recompensa r WHERE r.estado = :activa").setParameter("activa", true).list();
			return new ArrayList<>(resultado);
            

		} catch (HibernateException  e) {
			e.printStackTrace();
			if (session.getTransaction().isActive()) {
                session.getTransaction().rollback(); // Revertir transacción en caso de error
            }
			throw new RuntimeException("Error al obtener la lista de recompensas activas de la base de datos", e);
		} finally {
			session.close();
		}
		
    }

    
	/**
     * Modifica una recommpensa
     *
     * @param recompensa La recompensa a actualizar.
     * 
     * @throws IllegalArgumentException si la recompensa es nula
     * @throws IllegalArgumentException si la recompensa no existe.
     */
    @Override
    public void actualizarRecompensa(Recompensa recompensa) {
        Session session = getSession();
		if (recompensa == null) {
			throw new IllegalArgumentException("La recompensa no puede ser nula");
		}
		
		try {
			session.beginTransaction();
			if(session.get(Recompensa.class,recompensa.getId())==null){
				throw new IllegalArgumentException("La recompensa con ese identificador no existe");
			}
			session.merge(recompensa);
			session.getTransaction().commit();
	
			

		} catch (HibernateException e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		} finally {
			session.close();
		}
    }

    
	/**
     * Obtiene una lista de recompensas obtenidas por un usuario.
     *
     * @param usuario El usuario del cual obtener las recompensas.
     * @return Lista de recompensas obtenidas por el usuario.
     */
    @Override
    public ArrayList<Recompensa> obtenerRecompensasUsuario(Usuario usuario) {
        Session session = getSession();

		try {
			session.beginTransaction();
           
		   List <Recompensa> resultado=session.createQuery("SELECT r FROM Recompensa r WHERE r.usuario.nif = :dni").setParameter("dni", usuario.getNif()).list();
		   return new ArrayList<>(resultado);
            

		} catch (HibernateException e) {
			e.printStackTrace();
			if (session.getTransaction().isActive()) {
                session.getTransaction().rollback(); // Revertir transacción en caso de error
            }
			throw new RuntimeException("Error al obtener la lista de recompensas de un usuario de la base de datos", e);
		} finally {
			session.close();
		}
		
    }

    /**
	 * Elimina las tablas de la base de datos
	 */
	public void clearDatabase() {
		Session session = getSession();
		session.getTransaction().begin();
		Query query = session.createSQLQuery("Truncate table RECOMPENSAS");
		query.executeUpdate();
		query = session.createSQLQuery("Truncate table RECOMPENSAS");
		query.executeUpdate();
		session.close();

	}

   

}
