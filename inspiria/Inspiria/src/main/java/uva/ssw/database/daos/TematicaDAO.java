package uva.ssw.database.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;

import uva.ssw.database.ConnectionPool;
import uva.ssw.database.DBUtil;
import uva.ssw.modelo.Tematica;
import uva.ssw.modelo.Tutorial;

public class TematicaDAO {
    
    private static final String SELECT_TEMATICA = "SELECT * FROM Tematica WHERE nombreTematica = ?";
    
    private static final String SELECT_CINCO_TUTORIALES_DE_LA_TEMATICA = 
            "SELECT idTutorial, titulo FROM Tutorial " +
            "WHERE idTematica = ? " +
            "ORDER BY fechaPublicacion DESC LIMIT 5";
    
    private static final String SELECT_NOMBRES_TEMATICAS = "SELECT nombreTematica FROM Tematica ORDER BY CAST(nombreTematica AS CHAR) ASC;";
    
    private static final String SELECT_TEMATICA_POR_ID = "SELECT * FROM Tematica WHERE idTematica = ?";
    
    public static Tematica obtenerTematicaPorNombre(String nombre) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Tematica tematica = null;

        try {
            ps = connection.prepareStatement(SELECT_TEMATICA);
            ps.setString(1, nombre);
            rs = ps.executeQuery();

            if (rs.next()) {
                tematica = new Tematica();
                int idTem = rs.getInt("idTematica");
                tematica.setIdTematica(idTem);
                tematica.setNombre(rs.getString("nombreTematica"));
                tematica.setNumVisitas(rs.getInt("numVisitasTematica"));
                byte[] foto = rs.getBytes("imagenTematica");
                if (foto != null) {
                    tematica.setFotoBase64(Base64.getEncoder().encodeToString(foto));
                }

                tematica.setListaTutoriales(obtenerUltimos5Tutoriales(idTem, connection));
            }
        } catch (SQLException e) {
            System.err.println("Error en obtenerTematicaPorNombre: " + e.getMessage());
        } finally {
            DBUtil.closeResultSet(rs);
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        }
        return tematica;
    }
    
    public static Tematica obtenerTematicaPorId(int id) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Tematica tematica = null;

        try {
            ps = connection.prepareStatement(SELECT_TEMATICA_POR_ID);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                tematica = new Tematica();
                tematica.setIdTematica(rs.getInt("idTematica"));
                tematica.setNombre(rs.getString("nombreTematica"));
            }
        } catch (SQLException e) {
            System.err.println("Error en obtenerTematicaPorId: " + e.getMessage());
        } finally {
            DBUtil.closeResultSet(rs);
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        }
        return tematica;
    }

    private static ArrayList<Tutorial> obtenerUltimos5Tutoriales(int idTematica, Connection conn) {
        ArrayList<Tutorial> lista = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(SELECT_CINCO_TUTORIALES_DE_LA_TEMATICA);
            ps.setInt(1, idTematica);
            rs = ps.executeQuery();

            while (rs.next()) {
                Tutorial t = new Tutorial();
                t.setId(rs.getInt("idTutorial"));
                t.setTitulo(rs.getString("titulo"));
                lista.add(t);
            }
        } catch (SQLException e) {
            System.err.println("Error cargando tutoriales de temática: " + e.getMessage());
        } finally {
            DBUtil.closeResultSet(rs);
            DBUtil.closePreparedStatement(ps);
        }
        return lista;
    }
    
    public static ArrayList<String> obtenerNombreTodasTematicasExistentes() {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<String> nombresTematicas = new ArrayList<>();

        try {
            ps = connection.prepareStatement(SELECT_NOMBRES_TEMATICAS);
            rs = ps.executeQuery();

            while (rs.next()) {
                nombresTematicas.add(rs.getString("nombreTematica"));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener nombres de tematica: " + e.getMessage());
            return null; 
        } finally {
            DBUtil.closeResultSet(rs);
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        }

        return nombresTematicas;
    }
    
    public static boolean seguirTematica(int idUsuario, int idTematica) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection con = pool.getConnection();
        try {
            String sql = "INSERT INTO TematicasSeguidas (idUsuario, idTematica) VALUES (?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idUsuario);
            ps.setInt(2, idTematica);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { return false; } 
        finally { pool.freeConnection(con); }
    }

    public static boolean dejarSeguirTematica(int idUsuario, int idTematica) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection con = pool.getConnection();
        try {
            String sql = "DELETE FROM TematicasSeguidas WHERE idUsuario = ? AND idTematica = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idUsuario);
            ps.setInt(2, idTematica);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { return false; } 
        finally { pool.freeConnection(con); }
    }

    public static boolean esTematicaSeguida(int idUsuario, int idTematica) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection con = pool.getConnection();
        try {
            String sql = "SELECT 1 FROM TematicasSeguidas WHERE idUsuario = ? AND idTematica = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idUsuario);
            ps.setInt(2, idTematica);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) { return false; } 
        finally { pool.freeConnection(con); }
    }

    public static ArrayList<Tematica> obtenerTematicasSeguidas(int idUsuario) {
        ArrayList<Tematica> lista = new ArrayList<>();
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection con = pool.getConnection();
        try {
            String sql = "SELECT t.* FROM Tematica t INNER JOIN TematicasSeguidas ts ON t.idTematica = ts.idTematica WHERE ts.idUsuario = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idUsuario);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Tematica tem = new Tematica();
                tem.setIdTematica(rs.getInt("idTematica"));
                tem.setNombre(rs.getString("nombreTematica"));
                byte[] foto = rs.getBytes("imagenTematica");
                if (foto != null) {
                    tem.setFotoBase64(java.util.Base64.getEncoder().encodeToString(foto));
                }
                lista.add(tem);
            }
        } catch (SQLException e) { e.printStackTrace(); } 
        finally { pool.freeConnection(con); }
        return lista;
    }
}