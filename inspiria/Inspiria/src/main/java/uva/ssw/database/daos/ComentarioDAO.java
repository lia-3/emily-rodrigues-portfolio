package uva.ssw.database.daos;

import uva.ssw.modelo.Comentario;
import uva.ssw.modelo.Usuario;
import uva.ssw.database.ConnectionPool;
import uva.ssw.database.DBUtil;
import java.sql.*;
import java.util.ArrayList;

public class ComentarioDAO {
    
    private static final String SELECT_COMENTARIO = "SELECT c.*, u.nombre FROM Comentario c " +
                       "JOIN Usuario u ON c.idUsuario = u.idUsuario " +
                       "WHERE c.idTutorial = ? ORDER BY c.fechaCreacion DESC";
    
    private static final String INSERT_COMENTARIO = 
            "INSERT INTO Comentario (idUsuario, idTutorial, texto, fechaCreacion) VALUES (?, ?, ?, ?)";
        
    
    public static ArrayList<Comentario> obtenerListaComentariosTutorial(int idTutorial) {
        ArrayList<Comentario> lista = new ArrayList<>();
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(SELECT_COMENTARIO);
            ps.setInt(1, idTutorial);
            rs = ps.executeQuery();

            while (rs.next()) {
                Comentario com = new Comentario();
                com.setIdentificador(String.valueOf(rs.getInt("idComentario")));
                com.setTexto(rs.getString("texto"));
                com.setFechaCreacion(rs.getDate("fechaCreacion").toLocalDate());
                Usuario autor = new Usuario();
                autor.setNombre(rs.getString("nombre"));
                com.setAutor(autor);
                
                lista.add(com);
            }
        } catch (SQLException e) {
            System.err.println("Error en obtenerListaComentarios: " + e.getMessage());
        } finally {
            DBUtil.closeResultSet(rs);
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        }
        return lista;
    }

    public static boolean publicarComentarioTutorial(Comentario comentario, int idUsuarioPublicante, int idTutorial) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;
        boolean insertado = false;

        try {
            ps = connection.prepareStatement(INSERT_COMENTARIO);
            ps.setInt(1, idUsuarioPublicante);
            ps.setInt(2, idTutorial);
            ps.setString(3, comentario.getTexto());
            ps.setDate(4, Date.valueOf(java.time.LocalDate.now()));

            int filas = ps.executeUpdate();
            if (filas > 0) {
                insertado = true;
            }
        } catch (SQLException e) {
            System.err.println("Error en publicarComentario: " + e.getMessage());
        } finally {
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        }
        return insertado;
    }

}