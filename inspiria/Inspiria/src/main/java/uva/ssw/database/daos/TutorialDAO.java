package uva.ssw.database.daos;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Base64;

import uva.ssw.database.ConnectionPool;
import uva.ssw.database.DBUtil;
import uva.ssw.modelo.Categoria;
import uva.ssw.modelo.DificultadTutorial;
import uva.ssw.modelo.Tematica;
import uva.ssw.modelo.Tutorial;

public class TutorialDAO {

    private static final String INSERT_TUTORIAL = 
        "INSERT INTO Tutorial (idUsuario, idCategoria, idTematica, titulo, descripcion, " +
                                "fechaPublicacion, dificultad, costeEstimado, duracion, portada) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String SELECT_TUTORIAL = "SELECT * FROM Tutorial WHERE idTutorial = ?";
    
    private static final String SELECT_TUTORIALES_BY_USUARIO = 
        "SELECT t.*, c.nombreCategoria AS nombreCat, tem.nombreTematica AS nombreTem " +
        "FROM Tutorial t " +
        "JOIN Categoria c ON t.idCategoria = c.idCategoria " +
        "JOIN Tematica tem ON t.idTematica = tem.idTematica " +
        "WHERE t.idUsuario = ?";

    private static final String SELECT_TUTORIALES = "SELECT * FROM Tutorial ORDER BY fechaPublicacion DESC";
    
    private static final String SELECT_TUTORIALES_GUARDADOS = 
        "SELECT t.* FROM Tutorial t " +
                         "INNER JOIN TutorialesGuardados tg ON t.idTutorial = tg.idTutorial " +
                         "WHERE tg.idUsuario = ? ORDER BY t.fechaPublicacion DESC";

    private static final String INSERT_TUTORIAL_GUARDADO = 
        "INSERT INTO TutorialesGuardados (idUsuario, idTutorial) VALUES (?, ?)";

    private static final String SELECT_TUTORIAL_GUARDADO =
        "SELECT 1 FROM TutorialesGuardados WHERE idUsuario = ? AND idTutorial = ?";

    private static final String DELETE_TUTORIAL_GUARDADO =
        "DELETE FROM TutorialesGuardados WHERE idUsuario = ? AND idTutorial = ?";
    
    
    private static final String DELETE_TUTORIAL =
        "DELETE FROM Tutorial WHERE idUsuario = ? AND idTutorial = ?";
    
    public static int insertarTutorial(Tutorial t, int idUsuario, int idCategoria, int idTematica) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection conn = pool.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        int idGenerado = -1;

        try {
            ps = conn.prepareStatement(INSERT_TUTORIAL, Statement.RETURN_GENERATED_KEYS);

            ps.setInt(1, idUsuario);
            ps.setInt(2, idCategoria);
            ps.setInt(3, idTematica);
            ps.setString(4, t.getTitulo());
            ps.setString(5, t.getDescripcion());
            ps.setDate(6, Date.valueOf(t.getFechaPublicacion()));
            
            if (t.getDificultad() != null) {
                ps.setString(7, t.getDificultad().name());
            } else {
                ps.setString(7, "PRINCIPIANTE");
            }

            ps.setDouble(8, t.getCosteEstimado());
            ps.setInt(9, t.getDuracion());
            
            if (t.getFotoBase64() != null) {
                byte[] imagenBytes = Base64.getDecoder().decode(t.getFotoBase64());
                ps.setBytes(10, imagenBytes);
            } else {
                ps.setNull(10, java.sql.Types.BLOB);
            }
            ps.executeUpdate();

            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                idGenerado = rs.getInt(1);
            }

        } catch (SQLException e) {
            System.err.println("Error en insertarTutorial: " + e.getMessage());
        } finally {
            DBUtil.closeResultSet(rs);
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(conn);
        }
        return idGenerado;
    }

    
    public static Tutorial obtenerTutorial(int idTutorial) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection conn = pool.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Tutorial t = null;

        try {
            ps = conn.prepareStatement(SELECT_TUTORIAL);
            ps.setInt(1, idTutorial);
            rs = ps.executeQuery();

            if (rs.next()) {
                t = new Tutorial();
                t.setId(rs.getInt("idTutorial"));
                t.setTitulo(rs.getString("titulo"));
                t.setDescripcion(rs.getString("descripcion"));
                t.setFechaPublicacion(rs.getDate("fechaPublicacion").toLocalDate());
                t.setCosteEstimado(rs.getDouble("costeEstimado"));
                t.setDuracion(rs.getInt("duracion"));
                String dif = rs.getString("dificultad"); 
                if (dif != null) {
                    try {
                        // Convertimos el String al valor del Enum correspondiente
                        t.setDificultad(uva.ssw.modelo.DificultadTutorial.valueOf(dif.toUpperCase())); 
                    } catch (Exception ex) {
                        t.setDificultad(uva.ssw.modelo.DificultadTutorial.PRINCIPIANTE); 
                    }
                }
                byte[] img = rs.getBytes("portada"); 
                if (img != null) {
                    t.setFotoBase64(Base64.getEncoder().encodeToString(img));
                }
                int idCat = rs.getInt("idCategoria");
                int idTem = rs.getInt("idTematica");
                int idUser = rs.getInt("idUsuario");

                t.setCategoria(CategoriaDAO.obtenerCategoriaPorId(idCat));
                t.setTematica(TematicaDAO.obtenerTematicaPorId(idTem));
                t.setAutor(UsuarioDAO.obtenerPerfilUsuario(idUser));
            }
        } catch (SQLException e) {
            System.err.println("Error en obtenerTutorial: " + e.getMessage());
        } finally {
            DBUtil.closeResultSet(rs);
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(conn);
        }
        return t;
    }
    

    public static ArrayList<Tutorial> obtenerTutorialesPorUsuario(int idUsuario) {
        ArrayList<Tutorial> lista = new ArrayList<>();
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection conn = pool.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = conn.prepareStatement(SELECT_TUTORIALES_BY_USUARIO);
            ps.setInt(1, idUsuario);
            rs = ps.executeQuery();

            while (rs.next()) {
                Tutorial t = new Tutorial();
                t.setId(rs.getInt("idTutorial"));
                t.setTitulo(rs.getString("titulo"));
                t.setDescripcion(rs.getString("descripcion"));

                Date fechaSQL = rs.getDate("fechaPublicacion");
                if (fechaSQL != null) {
                    t.setFechaPublicacion(fechaSQL.toLocalDate());
                }

                byte[] img = rs.getBytes("portada"); 
                if (img != null) {
                    t.setFotoBase64(Base64.getEncoder().encodeToString(img));
                }

                String dif = rs.getString("dificultad");
                if (dif != null) {
                    try {
                        t.setDificultad(DificultadTutorial.valueOf(dif.toUpperCase()));
                    } catch (Exception ex) {}
                }

                byte[] fotoBytes = rs.getBytes("portada");
                if (fotoBytes != null) {
                    t.setFotoBase64(java.util.Base64.getEncoder().encodeToString(fotoBytes));
                }
                
                Categoria cat = new Categoria();
                cat.setIdCategoria(rs.getInt("idCategoria"));
                cat.setNombre(rs.getString("nombreCat")); 
                t.setCategoria(cat);

                
                Tematica tem = new Tematica();
                tem.setIdTematica(rs.getInt("idTematica"));
                tem.setNombre(rs.getString("nombreTem")); 
                t.setTematica(tem);

                lista.add(t);
            }
        } catch (SQLException e) {
            System.err.println("FALLO CRÍTICO EN TutorialDAO: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DBUtil.closeResultSet(rs);
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(conn);
        }
        return lista;
    }
    
    
    public static ArrayList<Tutorial> obtenerTodosTutoriales() {
        ArrayList<Tutorial> lista = new ArrayList<>();
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection conn = pool.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = conn.prepareStatement(SELECT_TUTORIALES);
            rs = ps.executeQuery();

            while (rs.next()) {
                Tutorial t = new Tutorial();
                t.setId(rs.getInt("idTutorial"));
                t.setTitulo(rs.getString("titulo"));
                t.setDescripcion(rs.getString("descripcion"));

                Date fechaSQL = rs.getDate("fechaPublicacion");
                if (fechaSQL != null) {
                    t.setFechaPublicacion(fechaSQL.toLocalDate());
                }

                byte[] img = rs.getBytes("portada"); 
                if (img != null) {
                    t.setFotoBase64(java.util.Base64.getEncoder().encodeToString(img));
                }

                t.setCategoria(CategoriaDAO.obtenerCategoriaPorId(rs.getInt("idCategoria")));
                t.setTematica(TematicaDAO.obtenerTematicaPorId(rs.getInt("idTematica")));
                t.setAutor(UsuarioDAO.obtenerPerfilUsuario(rs.getInt("idUsuario"))); 

                lista.add(t);
            }
        } catch (SQLException e) {
            System.err.println("Error en obtenerTodosTutoriales: " + e.getMessage());
        } finally {
            DBUtil.closeResultSet(rs);
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(conn);
        }
        return lista;
    }
    
    // filtra solo los de las personas q sigue
    public static ArrayList<Tutorial> obtenerTutorialesMuroFiltrado(int idUsuarioLogueado) {
        ArrayList<Tutorial> lista = new ArrayList<>();
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection conn = pool.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT t.* FROM Tutorial t " +
                         "INNER JOIN PerfilesSeguidos ps ON t.idUsuario = ps.idUsuarioSeguido " +
                         "WHERE ps.idUsuarioSigue = ? " +
                         "ORDER BY t.fechaPublicacion DESC";
            
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idUsuarioLogueado);
            rs = ps.executeQuery();

            while (rs.next()) {
                Tutorial t = new Tutorial();
                t.setId(rs.getInt("idTutorial"));
                t.setTitulo(rs.getString("titulo"));
                t.setDescripcion(rs.getString("descripcion"));

                java.sql.Date fechaSQL = rs.getDate("fechaPublicacion");
                if (fechaSQL != null) {
                    t.setFechaPublicacion(fechaSQL.toLocalDate());
                }

                byte[] img = rs.getBytes("portada"); 
                if (img != null) {
                    t.setFotoBase64(java.util.Base64.getEncoder().encodeToString(img));
                }

                // Recuperamos las categorías, temática y el autor para el muro
                t.setCategoria(CategoriaDAO.obtenerCategoriaPorId(rs.getInt("idCategoria")));
                t.setTematica(TematicaDAO.obtenerTematicaPorId(rs.getInt("idTematica")));
                t.setAutor(UsuarioDAO.obtenerPerfilUsuario(rs.getInt("idUsuario"))); 

                lista.add(t);
            }
        } catch (SQLException e) {
            System.err.println("Error en obtenerTutorialesMuroFiltrado: " + e.getMessage());
        } finally {
            DBUtil.closeResultSet(rs);
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(conn);
        }
        return lista;
    }
    
    public static ArrayList<Tutorial> obtenerTutorialesGuardados(int idUsuario) {
        ArrayList<Tutorial> lista = new ArrayList<>();
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection conn = pool.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

            ps = conn.prepareStatement(SELECT_TUTORIALES_GUARDADOS);
            ps.setInt(1, idUsuario);
            rs = ps.executeQuery();

            while (rs.next()) {
                Tutorial t = new Tutorial();
                t.setId(rs.getInt("idTutorial"));
                t.setTitulo(rs.getString("titulo"));
                t.setDescripcion(rs.getString("descripcion"));

                Date fechaSQL = rs.getDate("fechaPublicacion");
                if (fechaSQL != null) {
                    t.setFechaPublicacion(fechaSQL.toLocalDate());
                }

                byte[] img = rs.getBytes("portada"); 
                if (img != null) {
                    t.setFotoBase64(java.util.Base64.getEncoder().encodeToString(img));
                }

                t.setCategoria(CategoriaDAO.obtenerCategoriaPorId(rs.getInt("idCategoria")));
                t.setTematica(TematicaDAO.obtenerTematicaPorId(rs.getInt("idTematica")));
                t.setAutor(UsuarioDAO.obtenerPerfilUsuario(rs.getInt("idUsuario"))); 

                lista.add(t);
            }
        } catch (SQLException e) {
            System.err.println("Error en obtenerTutorialesGuardados: " + e.getMessage());
        } finally {
            DBUtil.closeResultSet(rs);
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(conn);
        }
        return lista;
    }
    
    public static boolean guardarTutorial(int idUsuario, int idTutorial) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection conn = pool.getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement(INSERT_TUTORIAL_GUARDADO);
            ps.setInt(1, idUsuario);
            ps.setInt(2, idTutorial);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            return false; 
        } finally {
            pool.freeConnection(conn);
        }
    }

    public static boolean esTutorialGuardado(int idUsuario, int idTutorial) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection conn = pool.getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement(SELECT_TUTORIAL_GUARDADO);
            ps.setInt(1, idUsuario);
            ps.setInt(2, idTutorial);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            return false;
        } finally {
            pool.freeConnection(conn);
        }
    }
    
    
    public static boolean eliminarTutorialGuardado(int idUsuario, int idTutorial) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection conn = pool.getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement(DELETE_TUTORIAL_GUARDADO);
            ps.setInt(1, idUsuario);
            ps.setInt(2, idTutorial);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        } finally {
            pool.freeConnection(conn);
        }
    }
    
    public static boolean eliminarTutorial(int idUsuario, int idTutorial){
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection conn = pool.getConnection();
         try {
            PreparedStatement ps = conn.prepareStatement(DELETE_TUTORIAL);
            ps.setInt(1, idUsuario);
            ps.setInt(2, idTutorial);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            return false;
        } finally {
            pool.freeConnection(conn);
        }
        
        
    }
    
    public static ArrayList<Tutorial> obtenerTutorialesPorCategoria(int idCategoria) {
        ArrayList<Tutorial> lista = new ArrayList<>();
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection conn = pool.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        // Ordena por fecha más reciente y rompe empates con el ID más alto
        String sql = "SELECT * FROM Tutorial WHERE idCategoria = ? ORDER BY fechaPublicacion DESC, idTutorial DESC";

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idCategoria);
            rs = ps.executeQuery();

            while (rs.next()) {
                Tutorial t = new Tutorial();
                t.setId(rs.getInt("idTutorial"));
                t.setTitulo(rs.getString("titulo"));
                t.setDescripcion(rs.getString("descripcion"));

                Date fechaSQL = rs.getDate("fechaPublicacion");
                if (fechaSQL != null) {
                    t.setFechaPublicacion(fechaSQL.toLocalDate());
                }

                byte[] img = rs.getBytes("portada"); 
                if (img != null) {
                    t.setFotoBase64(java.util.Base64.getEncoder().encodeToString(img));
                }

                t.setCategoria(CategoriaDAO.obtenerCategoriaPorId(rs.getInt("idCategoria")));
                t.setTematica(TematicaDAO.obtenerTematicaPorId(rs.getInt("idTematica")));
                t.setAutor(UsuarioDAO.obtenerPerfilUsuario(rs.getInt("idUsuario"))); 

                lista.add(t);
            }
        } catch (SQLException e) {
            System.err.println("Error en TutorialDAO.obtenerTutorialesPorCategoria: " + e.getMessage());
        } finally {
            DBUtil.closeResultSet(rs);
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(conn);
        }
        return lista;
    }
    public static ArrayList<Tutorial> obtenerTutorialesPorTematica(int idTematica) {
        ArrayList<Tutorial> lista = new ArrayList<>();
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection conn = pool.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = "SELECT * FROM Tutorial WHERE idTematica = ? ORDER BY fechaPublicacion DESC, idTutorial DESC";

        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idTematica);
            rs = ps.executeQuery();

            while (rs.next()) {
                Tutorial t = new Tutorial();
                t.setId(rs.getInt("idTutorial"));
                t.setTitulo(rs.getString("titulo"));
                t.setDescripcion(rs.getString("descripcion"));

                java.sql.Date fechaSQL = rs.getDate("fechaPublicacion");
                if (fechaSQL != null) {
                    t.setFechaPublicacion(fechaSQL.toLocalDate());
                }

                byte[] img = rs.getBytes("portada"); 
                if (img != null) {
                    t.setFotoBase64(java.util.Base64.getEncoder().encodeToString(img));
                }

                t.setCategoria(CategoriaDAO.obtenerCategoriaPorId(rs.getInt("idCategoria")));
                t.setTematica(TematicaDAO.obtenerTematicaPorId(rs.getInt("idTematica")));
                t.setAutor(UsuarioDAO.obtenerPerfilUsuario(rs.getInt("idUsuario"))); 

                lista.add(t);
            }
        } catch (SQLException e) {
            System.err.println("Error en TutorialDAO.obtenerTutorialesPorTematica: " + e.getMessage());
        } finally {
            DBUtil.closeResultSet(rs);
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(conn);
        }
        return lista;
    }
}