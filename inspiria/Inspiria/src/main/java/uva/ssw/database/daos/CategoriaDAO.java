package uva.ssw.database.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;

import uva.ssw.database.ConnectionPool;
import uva.ssw.database.DBUtil;
import uva.ssw.modelo.Categoria;
import uva.ssw.modelo.Tutorial;

public class CategoriaDAO {
    
    private static final String SELECT_CATEGORIA = "SELECT * FROM Categoria WHERE nombreCategoria = ?";
    
    private static final String SELECT_CINCO_TUTORIALES_DE_LA_CATEGORIA = 
            "SELECT idTutorial, titulo FROM Tutorial " +
            "WHERE idCategoria = ? " +
            "ORDER BY fechaPublicacion DESC LIMIT 5";
    
    private static final String SELECT_NOMBRES_CATEGORIAS = "SELECT nombreCategoria FROM Categoria ORDER BY CAST(nombreCategoria AS CHAR) ASC;";

    private static final String SELECT_CATEGORIA_POR_ID = "SELECT * FROM Categoria WHERE idCategoria = ?";
    
    
    public static Categoria obtenerCategoriaPorNombre(String nombre) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Categoria categoria = null;

        try {
            ps = connection.prepareStatement(SELECT_CATEGORIA);
            ps.setString(1, nombre);
            rs = ps.executeQuery();

            if (rs.next()) {
                categoria = new Categoria();
                int idCat = rs.getInt("idCategoria"); 
                categoria.setIdCategoria(idCat);
                categoria.setNombre(rs.getString("nombreCategoria"));
                categoria.setNumVisitas(rs.getInt("numVisitasCategoria"));
                byte[] foto = rs.getBytes("imagenCategoria");
                if (foto != null) {
                    categoria.setFotoBase64(Base64.getEncoder().encodeToString(foto));
                }

                categoria.setListaTutoriales(obtenerUltimos5Tutoriales(idCat, connection));
            }
        } catch (SQLException e) {
            System.err.println("Error en obtenerCategoriaPorNombre: " + e.getMessage());
        } finally {
            DBUtil.closeResultSet(rs);
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        }
        return categoria;
    }
    
    public static Categoria obtenerCategoriaPorId(int id) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Categoria categoria = null;

        try {
            ps = connection.prepareStatement(SELECT_CATEGORIA_POR_ID);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                categoria = new Categoria();
                categoria.setIdCategoria(rs.getInt("idCategoria"));
                categoria.setNombre(rs.getString("nombreCategoria"));
            }
        } catch (SQLException e) {
            System.err.println("Error en obtenerCategoriaPorId: " + e.getMessage());
        } finally {
            DBUtil.closeResultSet(rs);
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        }
        return categoria;
    }

    
    private static ArrayList<Tutorial> obtenerUltimos5Tutoriales(int idCategoria, Connection conn) {
        ArrayList<Tutorial> lista = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(SELECT_CINCO_TUTORIALES_DE_LA_CATEGORIA);
            ps.setInt(1, idCategoria);
            rs = ps.executeQuery();

            while (rs.next()) {
                Tutorial t = new Tutorial();
                t.setId(rs.getInt("idTutorial"));
                t.setTitulo(rs.getString("titulo"));
                lista.add(t);
            }
        } catch (SQLException e) {
            System.err.println("Error cargando tutoriales de categoría: " + e.getMessage());
        } finally {
            DBUtil.closeResultSet(rs);
            DBUtil.closePreparedStatement(ps);
        }
        return lista;
    }
    
    
    public static ArrayList<String> obtenerNombreTodasCategoriasExistentes() {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<String> nombresCategorias = new ArrayList<>();

        try {
            ps = connection.prepareStatement(SELECT_NOMBRES_CATEGORIAS);
            rs = ps.executeQuery();

            while (rs.next()) {
                nombresCategorias.add(rs.getString("nombreCategoria"));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener nombres de categorías: " + e.getMessage());
            return null; 
        } finally {
            DBUtil.closeResultSet(rs);
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        }

        return nombresCategorias;
    }
    
    public static boolean seguirCategoria(int idUsuario, int idCategoria) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection con = pool.getConnection();
        try {
            String sql = "INSERT INTO CategoriasSeguidas (idUsuario, idCategoria) VALUES (?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idUsuario);
            ps.setInt(2, idCategoria);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { return false; } 
        finally { pool.freeConnection(con); }
    }

    public static boolean dejarSeguirCategoria(int idUsuario, int idCategoria) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection con = pool.getConnection();
        try {
            String sql = "DELETE FROM CategoriasSeguidas WHERE idUsuario = ? AND idCategoria = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idUsuario);
            ps.setInt(2, idCategoria);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { return false; } 
        finally { pool.freeConnection(con); }
    }

    public static boolean esCategoriaSeguida(int idUsuario, int idCategoria) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection con = pool.getConnection();
        try {
            String sql = "SELECT 1 FROM CategoriasSeguidas WHERE idUsuario = ? AND idCategoria = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idUsuario);
            ps.setInt(2, idCategoria);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) { return false; } 
        finally { pool.freeConnection(con); }
    }

    public static ArrayList<Categoria> obtenerCategoriasSeguidas(int idUsuario) {
        ArrayList<Categoria> lista = new ArrayList<>();
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection con = pool.getConnection();
        try {
            String sql = "SELECT c.* FROM Categoria c INNER JOIN CategoriasSeguidas cs ON c.idCategoria = cs.idCategoria WHERE cs.idUsuario = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idUsuario);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Categoria cat = new Categoria();
                cat.setIdCategoria(rs.getInt("idCategoria"));
                cat.setNombre(rs.getString("nombreCategoria"));
                byte[] foto = rs.getBytes("imagenCategoria");
                if (foto != null) {
                    cat.setFotoBase64(java.util.Base64.getEncoder().encodeToString(foto));
                }
                lista.add(cat);
            }
        } catch (SQLException e) { e.printStackTrace(); } 
        finally { pool.freeConnection(con); }
        return lista;
    }
} 