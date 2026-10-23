package uva.tds.implementaciones;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import uva.tds.base.HibernateUtil;
import uva.tds.base.Parada;
import uva.tds.base.Ruta;
import uva.tds.base.Usuario;
import uva.tds.interfaces.IRutaRepositorio;


/**
 * Clase que permite trabajar con Rutas en una base de datos mediante
 * Hibernate
 * @author Marcos de Diego Martin
 */
public class RutaRepositorio implements IRutaRepositorio {

    private Session getSession() {
        SessionFactory factory = HibernateUtil.getSessionFactory();
        try {
            Session session = factory.getCurrentSession();
            if (session == null) {
                throw new IllegalStateException("No se pudo obtener una sesión de Hibernate.");
            }
            return session;
        } catch (HibernateException e) {
            throw new IllegalStateException("Error al iniciar una sesión de Hibernate.", e);
        }
    }


    /**
     * Añade una nueva ruta al repositorio.
     * 
     * @param ruta La ruta a añadir.
     * @throws IllegalArgumentException Si la ruta es null o si ya existe una ruta con el mismo identificador.
     */
    @Override
    public void anadirRuta(Ruta ruta) {
        if (ruta == null) {
            throw new IllegalArgumentException("La ruta no puede ser null.");
        }
        if (buscarRutaPorIdentificador(ruta.getIdentificador()) != null) {
            throw new IllegalArgumentException("La ruta con el identificador ya existe.");
        }

        Session session = getSession();
        try {
            session.beginTransaction();

            Usuario usuarioPersistido = (Usuario) session.merge(ruta.getUsuario());
            Parada paradaOrigenPersistida = (Parada) session.merge(ruta.getParadaOrigen());
            Parada paradaDestinoPersistida = (Parada) session.merge(ruta.getParadaDestino());

            ruta.setUsuario(usuarioPersistido);
            ruta.setParadaOrigen(paradaOrigenPersistida);
            ruta.setParadaDestino(paradaDestinoPersistida);

            session.persist(ruta);
            session.flush();
            session.getTransaction().commit();
        } catch (HibernateException e) {
            if (session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            throw new IllegalStateException("Error al añadir la ruta.", e);
        } finally {
            if (session.isOpen()) {
                session.close();
            }
        }
    }

    /**
     * Obtiene una lista de rutas asociadas a un usuario específico.
     *
     * @param usuario el usuario del que se desea obtener las rutas. No puede ser
     *                {null}.
     * @return una lista de rutas asociadas al usuario especificado.
     * @throws IllegalArgumentException si {usuario} es {null}.
     * @throws IllegalStateException    si ocurre algún error durante la operación
     *                                  de base de datos.
     */

    public ArrayList<Ruta> obtenerRutasPorUsuario(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("El usuario no puede ser null.");
        }

        Session session = getSession();
        try {
            session.beginTransaction();
            String hql = "FROM Ruta r WHERE r.usuario = :usuario";
            List<Ruta> rutas = session.createQuery(hql, Ruta.class)
                    .setParameter("usuario", session.merge(usuario))
                    .getResultList();
            session.getTransaction().commit();
            return new ArrayList<>(rutas);
        } catch (Exception e) {
            if (session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            throw new IllegalStateException("Error al obtener rutas por usuario.", e);
        } finally {
            if (session.isOpen()) {
                session.close();
            }
        }
    }


    /**
     * Busca una ruta por su identificador.
     * 
     * @param identificador El identificador de la ruta.
     * @return La ruta encontrada.
     * @throws IllegalArgumentException Si no se encuentra una ruta con el identificador proporcionado.
     */
    @Override
    public Ruta buscarRutaPorIdentificador(String identificador) {
        if (identificador == null || identificador.trim().isEmpty()) {
            throw new IllegalArgumentException("El identificador no puede ser null o vacío.");
        }

        Session session = getSession();
        try {
            session.beginTransaction();
            Ruta ruta = session.get(Ruta.class, identificador);
            session.getTransaction().commit();
            return ruta;
        } catch (Exception e) {
            if (session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            throw new IllegalStateException("Error al buscar la ruta por identificador.", e);
        } finally {
            if (session.isOpen()) {
                session.close();
            }
        }
    }

    /**
     * Limpia las tablas 'USUARIOS' y 'Ruta' en la base de datos,
     * eliminando todos los registros.
     * Prepara un entorno limpio en pruebas automatizadas que interactúan
     * con un repositorio de usuarios y recompensas.
     * Elimina las tablas de la base de datos
     */
    @Override
    public void clearDatabase() {
        Session session = getSession();
        try {
            session.beginTransaction();
            Query query = session.createSQLQuery("TRUNCATE TABLE Ruta");
            query.executeUpdate();
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


    /**
     * Calcula la puntuación de una ruta.
     * 
     * @param distancia La distancia de la ruta en kilómetros.
     * @param tiempo El tiempo de la ruta en minutos.
     * @return La puntuación de la ruta.
     * @throws IllegalArgumentException Si el tiempo es 0 o menor.
     */
    @Override
    public int calcularPuntuacionRuta(double distancia, int tiempo) {
        if (tiempo <= 0) {
            throw new IllegalArgumentException("El tiempo debe ser mayor a 0.");
        }
        return (int) (distancia / tiempo * 100);
    }
}
