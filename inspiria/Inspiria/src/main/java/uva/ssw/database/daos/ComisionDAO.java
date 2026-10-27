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
import uva.ssw.modelo.Comision;

/**
 *
 * @author emily
 */
public class ComisionDAO {

    private static final String SELECT_BY_USER = "SELECT * FROM Entrada WHERE idUsuario = ?";
    private static final String DELETE_BY_USER = "DELETE FROM Entrada WHERE idUsuario = ?";
    private static final String INSERT_COMISION = "INSERT INTO Entrada (idUsuario, precioBase, descripcion) VALUES (?, ?, ?)";

    public static ArrayList<Comision> obtenerComisionesPorUsuario(int idUsuario) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<Comision> lista = new ArrayList<>();

        try {
            ps = connection.prepareStatement(SELECT_BY_USER);
            ps.setInt(1, idUsuario);
            rs = ps.executeQuery();
            while (rs.next()) {
                Comision c = new Comision();
                c.setIdentificador(String.valueOf(rs.getInt("idEntrada")));
                c.setPrecioBase(rs.getDouble("precioBase"));
                c.setDescripcion(rs.getString("descripcion"));
                lista.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeResultSet(rs);
            DBUtil.closePreparedStatement(ps);
            pool.freeConnection(connection);
        }
        return lista;
    }

    public static boolean actualizarComisiones(int idUsuario, ArrayList<Comision> comisiones) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection connection = pool.getConnection();
        PreparedStatement psDel = null;
        PreparedStatement psIns = null;

        try {
            connection.setAutoCommit(false);

            psDel = connection.prepareStatement(DELETE_BY_USER);
            psDel.setInt(1, idUsuario);
            psDel.executeUpdate();

            if (comisiones != null && !comisiones.isEmpty()) {
                psIns = connection.prepareStatement(INSERT_COMISION);
                for (Comision c : comisiones) {
                    psIns.setInt(1, idUsuario);
                    psIns.setDouble(2, Math.max(0, c.getPrecioBase())); 
                    psIns.setString(3, c.getDescripcion());
                    psIns.addBatch();
                }
                psIns.executeBatch();
            }

            connection.commit();
            return true;
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        } finally {
            DBUtil.closePreparedStatement(psDel);
            DBUtil.closePreparedStatement(psIns);
            pool.freeConnection(connection);
        }
    }
}
