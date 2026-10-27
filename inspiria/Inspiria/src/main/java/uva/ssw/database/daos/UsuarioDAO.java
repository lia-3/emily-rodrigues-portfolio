package uva.ssw.database.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Base64;

import uva.ssw.database.ConnectionPool;
import uva.ssw.database.DBUtil;
import uva.ssw.modelo.Comision;
import uva.ssw.modelo.Contacto;
import uva.ssw.modelo.Usuario;

public class UsuarioDAO {
    
    private static final String SELECT_USUARIO_LOGIN = 
            "SELECT * FROM Usuario WHERE (correo = ? OR nombre = ?) AND passwordUsuario = ?";
    
    private static final String SELECT_PERFIL_BY_ID = 
            "SELECT * FROM Usuario WHERE idUsuario = ?";
    
    private static final String SELECT_USUARIO_BY_NOMBRE = 
            "SELECT * FROM Usuario WHERE nombre = ?"; 

    private static final String INSERT_USUARIO = 
            "INSERT INTO Usuario (nombre, correo, passwordUsuario, descripcion, aceptaComisiones, fotoPerfil) "
            + "VALUES (?, ?, ?, ?, ?, ?)";
    
    private static final String SELECT_CONTACTOS_BY_USER = 
            "SELECT link, plataforma FROM Contacto WHERE idUsuario = ?";

    private static final String SELECT_ENTRADAS_BY_USER = 
            "SELECT precioBase, descripcion FROM Entrada WHERE idUsuario = ?";
    
    private static final String UPDATE_FOTO_PERFIL = 
            "UPDATE Usuario SET fotoPerfil = ? WHERE idUsuario = ?";
    
    private static final String UPDATE_NOMBRE_USUARIO = "UPDATE Usuario SET nombre = ? WHERE idUsuario = ?";
    
    private static final String SELECT_CHECK_PASS = 
            "SELECT idUsuario FROM Usuario WHERE idUsuario = ? AND passwordUsuario = ?";
    
    private static final String UPDATE_PASSWORD = "UPDATE Usuario SET passwordUsuario = ? WHERE idUsuario = ?";
    
    private static final String UPDATE_DESCRIPCION = "UPDATE Usuario SET descripcion = ? WHERE idUsuario = ?";
    
    private static final String UPDATE_ACEPTA_COMISIONES = 
        "UPDATE Usuario SET aceptaComisiones = ? WHERE idUsuario = ?";

    
    public static int registrarUsuario(Usuario usuario) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection con = pool.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        int idGenerado = -1;

        try {
            ps = con.prepareStatement(INSERT_USUARIO, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getCorreo());
            ps.setString(3, usuario.getContrasena()); 
            ps.setString(4, usuario.getDescripcion());
            ps.setBoolean(5, usuario.isAceptaComisiones());
            ps.setBytes(6, usuario.getFotoPerfil());

            int filasAfectadas = ps.executeUpdate();
            if (filasAfectadas > 0) {
                rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    idGenerado = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al registrar usuario: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DBUtil.closeResultSet(rs);
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(con);
        }
        return idGenerado;
    }

    public static Usuario validarUsuario(String identificador, String password) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Usuario u = null;
        try {
            ps = connection.prepareStatement(SELECT_USUARIO_LOGIN);
            ps.setString(1, identificador); 
            ps.setString(2, identificador);
            ps.setString(3, password);
            rs = ps.executeQuery(); 

            if (rs.next()) {
                u = extraerUsuarioDeResultSet(rs); 
                int id = u.getIdentificador();
                u.setContactos(obtenerContactosDeUsuario(id));
                u.setListaComisiones(obtenerComisionesDeUsuario(id)); 
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeResultSet(rs);
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        }
        return u; 
    }

    public static Usuario obtenerPerfilUsuario(int idUsuario) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection con = pool.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Usuario user = null;
        try {
            ps = con.prepareStatement(SELECT_PERFIL_BY_ID);
            ps.setInt(1, idUsuario);
            rs = ps.executeQuery();
            if (rs.next()) {
                user = extraerUsuarioDeResultSet(rs);
                user.setContactos(obtenerContactosDeUsuario(idUsuario));
                user.setListaComisiones(obtenerComisionesDeUsuario(idUsuario));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeResultSet(rs);
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(con);
        }
        return user;
    }

    public static Usuario obtenerUsuarioPorNombre(String nombre) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection con = pool.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Usuario user = null;
        try {
            ps = con.prepareStatement(SELECT_USUARIO_BY_NOMBRE);
            ps.setString(1, nombre);
            rs = ps.executeQuery();
            if (rs.next()) {
                user = extraerUsuarioDeResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeResultSet(rs);
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(con);
        }
        return user;
    }

    private static Usuario extraerUsuarioDeResultSet(ResultSet rs) throws SQLException {
        Usuario u = new Usuario();
        u.setIdentificador(rs.getInt("idUsuario"));
        u.setNombre(rs.getString("nombre"));
        u.setCorreo(rs.getString("correo"));
        u.setDescripcion(rs.getString("descripcion"));
        u.setAceptaComisiones(rs.getBoolean("aceptaComisiones"));
        byte[] foto = rs.getBytes("fotoPerfil");
        if (foto != null) {
            u.setFotoPerfil(foto);
            u.setFotoBase64(Base64.getEncoder().encodeToString(foto));
        }
        return u;
    }

    public static ArrayList<Contacto> obtenerContactosDeUsuario(int idUsuario) {
        ArrayList<Contacto> lista = new ArrayList<>();
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection con = pool.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(SELECT_CONTACTOS_BY_USER);
            ps.setInt(1, idUsuario);
            rs = ps.executeQuery();
            while (rs.next()) {
                Contacto c = new Contacto();
                c.setLink(rs.getString("link"));
                c.setPlataforma(rs.getString("plataforma"));
                lista.add(c);
            }
        } catch (SQLException e) { e.printStackTrace(); 
        } finally {
            DBUtil.closeResultSet(rs);
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(con);
        }
        return lista;
    }

    public static ArrayList<Comision> obtenerComisionesDeUsuario(int idUsuario) {
        ArrayList<Comision> lista = new ArrayList<>();
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection con = pool.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = con.prepareStatement(SELECT_ENTRADAS_BY_USER);
            ps.setInt(1, idUsuario);
            rs = ps.executeQuery();
            while (rs.next()) {
                Comision com = new Comision();
                com.setPrecioBase(rs.getDouble("precioBase"));
                com.setDescripcion(rs.getString("descripcion"));
                lista.add(com);
            }
        } catch (SQLException e) { e.printStackTrace(); 
        } finally {
            DBUtil.closeResultSet(rs);
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(con);
        }
        return lista;
    }
    
    public static boolean actualizarFotoPerfil(int idUsuario, byte[] nuevaFoto) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection con = pool.getConnection();
        PreparedStatement ps = null;
        boolean exito = false;
        try {
            ps = con.prepareStatement(UPDATE_FOTO_PERFIL);
            ps.setBytes(1, nuevaFoto);
            ps.setInt(2, idUsuario);
            exito = ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(con);
        }
        return exito;
    }
    
    public static boolean actualizarNombre(int idUsuario, String nuevoNombre) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection con = pool.getConnection();
        PreparedStatement ps = null;
        boolean exito = false;
        try {
            ps = con.prepareStatement(UPDATE_NOMBRE_USUARIO);
            ps.setString(1, nuevoNombre);
            ps.setInt(2, idUsuario);
            exito = ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(con);
        }
        return exito;
    }
    
    public static boolean comprobarContrasenaActual(int idUsuario, String passActual) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection con = pool.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean valida = false;
        try {
            ps = con.prepareStatement(SELECT_CHECK_PASS);
            ps.setInt(1, idUsuario);
            ps.setString(2, passActual);
            rs = ps.executeQuery();
            valida = rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeResultSet(rs);
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(con);
        }
        return valida;
    }

    public static boolean actualizarPassword(int idUsuario, String nuevaPass) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection con = pool.getConnection();
        PreparedStatement ps = null;
        boolean exito = false;
        try {
            ps = con.prepareStatement(UPDATE_PASSWORD);
            ps.setString(1, nuevaPass);
            ps.setInt(2, idUsuario);
            exito = ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(con);
        }
        return exito;
    }
    
    public static boolean actualizarDescripcion(int idUsuario, String nuevaDesc) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection con = pool.getConnection();
        PreparedStatement ps = null;
        boolean exito = false;
        try {
            ps = con.prepareStatement(UPDATE_DESCRIPCION);
            ps.setString(1, nuevaDesc);
            ps.setInt(2, idUsuario);
            exito = ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(con);
        }
        return exito;
    }
    
    public static boolean actualizarAceptaComisiones(int idUsuario, boolean acepta) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection con = pool.getConnection();
        PreparedStatement ps = null;
        boolean exito = false;
        try {
            ps = con.prepareStatement(UPDATE_ACEPTA_COMISIONES);
            ps.setBoolean(1, acepta);
            ps.setInt(2, idUsuario);
            exito = ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(con);
        }
        return exito;
    }
    
    
    public static Usuario obtenerPerfilExterno(int idUsuario) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection con = pool.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Usuario u = null;
        try {
            ps = con.prepareStatement(SELECT_PERFIL_BY_ID);
            ps.setInt(1, idUsuario);
            rs = ps.executeQuery();
            if (rs.next()) {
                u = extraerUsuarioDeResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeResultSet(rs);
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(con);
        }
        return u;
    }
    
    public static boolean seguirUsuario(int idUsuarioSigue, int idUsuarioSeguido) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection con = pool.getConnection();
        try {
            String sql = "INSERT INTO PerfilesSeguidos (idUsuarioSigue, idUsuarioSeguido) VALUES (?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idUsuarioSigue);
            ps.setInt(2, idUsuarioSeguido);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        } finally {
            pool.freeConnection(con);
        }
    }

    public static boolean dejarSeguirUsuario(int idUsuarioSigue, int idUsuarioSeguido) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection con = pool.getConnection();
        try {
            String sql = "DELETE FROM PerfilesSeguidos WHERE idUsuarioSigue = ? AND idUsuarioSeguido = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idUsuarioSigue);
            ps.setInt(2, idUsuarioSeguido);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        } finally {
            pool.freeConnection(con);
        }
    }

    public static boolean esPerfilSeguido(int idUsuarioSigue, int idUsuarioSeguido) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection con = pool.getConnection();
        try {
            String sql = "SELECT 1 FROM PerfilesSeguidos WHERE idUsuarioSigue = ? AND idUsuarioSeguido = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idUsuarioSigue);
            ps.setInt(2, idUsuarioSeguido);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            return false;
        } finally {
            pool.freeConnection(con);
        }
    }
    
    public static ArrayList<Usuario> obtenerPerfilesSeguidos(int idUsuarioSigue) {
        ArrayList<Usuario> lista = new ArrayList<>();
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection con = pool.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            // Unimos la tabla Usuario con la tabla PerfilesSeguidos
            String sql = "SELECT u.* FROM Usuario u " +
                         "INNER JOIN PerfilesSeguidos ps ON u.idUsuario = ps.idUsuarioSeguido " +
                         "WHERE ps.idUsuarioSigue = ?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, idUsuarioSigue);
            rs = ps.executeQuery();
            while(rs.next()){
                lista.add(extraerUsuarioDeResultSet(rs)); // Reutilizamos tu método que saca la foto Base64
            }
        } catch (SQLException e) { 
            e.printStackTrace(); 
        } finally { 
            DBUtil.closeResultSet(rs);
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(con); 
        }
        return lista;
    }
}