/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package uva.ssw.database.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import uva.ssw.database.ConnectionPool;
import uva.ssw.database.DBUtil;
import uva.ssw.modelo.Contacto;

/**
 *
 * @author emily
 */

public class ContactoDAO {
    
    private static final String SELECT_LISTA_CONTACTOS = "SELECT link, plataforma FROM Contacto WHERE idUsuario = ?";
    
    private static final String DELETE_CONTACTO = "DELETE FROM Contacto WHERE idUsuario = ?";
    
    private static final String INSERT_CONTACTO = "INSERT INTO Contacto (idUsuario, link, plataforma) VALUES (?, ?, ?)";
    
    
    public static ArrayList<Contacto> obtenerContactosPorUsuario(int idUsuario) {
        ArrayList<Contacto> lista = new ArrayList<>();
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(SELECT_LISTA_CONTACTOS);
            ps.setInt(1, idUsuario);
            rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new Contacto(rs.getString("link"), rs.getString("plataforma")));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        finally { DBUtil.closeResultSet(rs); DBUtil.closePreparedStatement(ps); pool.freeConnection(connection); }
        return lista;
    }

    public static boolean actualizarContactos(int idUsuario, ArrayList<Contacto> contactos) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement psDel = null;
        PreparedStatement psIns = null;
        try {
            connection.setAutoCommit(false);
            psDel = connection.prepareStatement(DELETE_CONTACTO);
            psDel.setInt(1, idUsuario);
            psDel.executeUpdate();

            psIns = connection.prepareStatement(INSERT_CONTACTO);
            for (Contacto c : contactos) {
                psIns.setInt(1, idUsuario);
                psIns.setString(2, c.getLink());
                psIns.setString(3, c.getPlataforma());
                psIns.addBatch();
            }
            psIns.executeBatch();
            connection.commit();
            return true;
        } catch (SQLException e) { 
            try { connection.rollback(); } catch (SQLException ex) {} 
            return false; 
        } finally { pool.freeConnection(connection); }
    }
}