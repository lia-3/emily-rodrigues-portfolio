package uva.ssw.database.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import uva.ssw.database.ConnectionPool;
import uva.ssw.database.DBUtil;
import uva.ssw.modelo.DisponibilidadProducto;
import uva.ssw.modelo.Producto;

public class ProductoDAO {

    private final static String INSERT_PRODUCTO_ASOCIADO_A_TUTORIAL = 
        "INSERT INTO Producto (idUsuario, idTutorial, titulo, descripcion, precio, stock, idCategoria, idTematica, imagen) " + 
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    
    private final static String INSERT_PRODUCTO_AISLADO = 
        "INSERT INTO Producto (idUsuario, idTutorial, titulo, descripcion, precio, stock, idCategoria, idTematica, imagen) "
        + "VALUES (?, NULL, ?, ?, ?, ?, ?, ?, ?)";
    
    private final static String SELECT_PRODUCTO = "SELECT * FROM Producto WHERE idProducto = ?";
    
    private final static String SELECT_PRODUCTOS_BY_USUARIO =
        "SELECT * FROM Producto WHERE idUsuario = ?";
    
    private final static String DELETE_PRODUCTO =
        "DELETE FROM Producto WHERE idUsuario = ? AND idProducto = ?";
    
    public static boolean insertarProductoAsociadoTutorial(Producto p, int idTutorial, int idCategoria, int idTematica, int idUsuario) {
        return ejecutarInsert(INSERT_PRODUCTO_ASOCIADO_A_TUTORIAL, p, idTutorial, idCategoria, idTematica, idUsuario, true);
    }
    
    
    public static boolean insertarProductoAislado(Producto p, int idCategoria, int idTematica, int idUsuario) {
        return ejecutarInsert(INSERT_PRODUCTO_AISLADO, p, 0, idCategoria, idTematica, idUsuario, false);
    }


    public static Producto obtenerProducto(int idProducto) {
    ConnectionPool pool = ConnectionPool.getInstance();
    Connection conn = pool.getConnection();
    PreparedStatement ps = null;
    ResultSet rs = null;
    Producto p = null;

    try {
        ps = conn.prepareStatement(SELECT_PRODUCTO);
        ps.setInt(1, idProducto);
        rs = ps.executeQuery();

        if (rs.next()) {
            p = new Producto();
            p.setId(rs.getInt("idProducto"));
            p.setTitulo(rs.getString("titulo"));
            p.setDescripcion(rs.getString("descripcion"));
            p.setPrecio(rs.getDouble("precio"));
            
            int idTut = rs.getInt("idTutorial");
            if (!rs.wasNull()) p.setIdTutorialAsociado(idTut);
            
            String stockBD = rs.getString("stock");
            if (stockBD != null) {
                p.setStock(DisponibilidadProducto.valueOf(stockBD.toUpperCase()));
            }

            // 🛠️ FIX 1: ¡Recuperamos la imagen de la base de datos!
            byte[] img = rs.getBytes("imagen"); 
            if (img != null) {
                p.setImagen(img); // Guardamos los bytes binarios
                // Convertimos directamente a Base64 para que el getter del modelo (o el JSP) pueda leerlo como texto
                p.setFotoBase64(java.util.Base64.getEncoder().encodeToString(img));
            }

            // 🛠️ FIX 2: Inflamos los objetos asociados para que el JSP pueda pintar el Autor, Categoría y Temática
            int idCat = rs.getInt("idCategoria");
            int idTem = rs.getInt("idTematica");
            int idUser = rs.getInt("idUsuario");

            p.setCategoria(CategoriaDAO.obtenerCategoriaPorId(idCat));
            p.setTematica(TematicaDAO.obtenerTematicaPorId(idTem));
            p.setAutor(UsuarioDAO.obtenerPerfilUsuario(idUser));
        }
    } catch (SQLException e) {
        System.err.println("Error en obtenerProducto: " + e.getMessage());
    } finally {
        DBUtil.closeResultSet(rs);
        DBUtil.closePreparedStatement(ps);
        pool.freeConnection(conn);
    }
    return p;
}
    private static boolean ejecutarInsert(String query, Producto p, int idTutorial, int idCat, int idTem, int idUser, boolean esAsociado) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection conn = pool.getConnection();
        PreparedStatement ps = null;
        boolean exito = false;

        try {
            ps = conn.prepareStatement(query);
            ps.setInt(1, idUser);
            
            int currentIdx = 2;
            if (esAsociado) {
                ps.setInt(currentIdx, idTutorial);
                currentIdx++;
            }

            ps.setString(currentIdx++, p.getTitulo());
            ps.setString(currentIdx++, p.getDescripcion());
            ps.setDouble(currentIdx++, p.getPrecio());
            ps.setString(currentIdx++, p.getStock() != null ? p.getStock().name() : "DISPONIBLE");
            ps.setInt(currentIdx++, idCat);
            ps.setInt(currentIdx++, idTem);
            ps.setBytes(currentIdx++, p.getImagen());

            exito = ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al insertar producto: " + e.getMessage());
        } finally {
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(conn);
        }
        return exito;
    }

    public static ArrayList<Producto> obtenerProductosPorUsuario(int idUsuario) {
    ArrayList<Producto> lista = new ArrayList<>();
    ConnectionPool pool = ConnectionPool.getInstance();
    Connection conn = pool.getConnection();
    PreparedStatement ps = null;
    ResultSet rs = null;

    try {
        ps = conn.prepareStatement(SELECT_PRODUCTOS_BY_USUARIO);
        ps.setInt(1, idUsuario);
        rs = ps.executeQuery();

        while (rs.next()) {
            Producto p = new Producto();
            p.setId(rs.getInt("idProducto"));
            p.setTitulo(rs.getString("titulo"));
            p.setDescripcion(rs.getString("descripcion"));
            p.setPrecio(rs.getDouble("precio"));

            int idTut = rs.getInt("idTutorial");
            if (!rs.wasNull()) p.setIdTutorialAsociado(idTut);

            String stockBD = rs.getString("stock");
            if (stockBD != null) {
                p.setStock(DisponibilidadProducto.valueOf(stockBD.toUpperCase()));
            }

            byte[] img = rs.getBytes("imagen"); 
            if (img != null) {
                p.setImagen(img);
                p.setFotoBase64(java.util.Base64.getEncoder().encodeToString(img));
            }

            lista.add(p);
        }
    } catch (SQLException e) {
        System.err.println("FALLO CRÍTICO EN ProductoDAO: " + e.getMessage());
        e.printStackTrace();
    } finally {
        DBUtil.closeResultSet(rs);
        DBUtil.closePreparedStatement(ps);
        pool.freeConnection(conn);
    }
    return lista;
}
    
    
    public static boolean eliminarProducto(int idUsuario, int idProducto){
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection conn = pool.getConnection();
         try {
            PreparedStatement ps = conn.prepareStatement(DELETE_PRODUCTO);
            ps.setInt(1, idUsuario);
            ps.setInt(2, idProducto);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            return false;
        } finally {
            pool.freeConnection(conn);
        }
        
        
    }
    
}