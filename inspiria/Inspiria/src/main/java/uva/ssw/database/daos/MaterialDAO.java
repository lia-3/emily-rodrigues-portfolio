package uva.ssw.database.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import uva.ssw.database.ConnectionPool;
import uva.ssw.modelo.Material;

public class MaterialDAO {

    private static final String INSERT_MATERIAL = 
            "INSERT INTO Material (idTutorial, nombre, coste, urlMaterial) VALUES (?, ?, ?, ?)";
    
    private static final String SELECT_MATERIALES_TUTORIAL = 
            "SELECT * FROM Material WHERE idTutorial = ?";

            
    public static boolean insertarMaterial(Material material, int idTutorial) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection con = pool.getConnection();
        PreparedStatement ps = null;

        try {
            ps = con.prepareStatement(INSERT_MATERIAL);
            ps.setInt(1, idTutorial);
            ps.setString(2, material.getNombre());
            ps.setDouble(3, material.getCoste());
            ps.setString(4, material.getUrl());

            int filas = ps.executeUpdate();
            return filas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            pool.freeConnection(con);
        }
    }

    
    public static ArrayList<Material> obtenerListaMateriales(int idTutorial) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection con = pool.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<Material> lista = new ArrayList<>();

        try {
            ps = con.prepareStatement(SELECT_MATERIALES_TUTORIAL);
            ps.setInt(1, idTutorial);
            rs = ps.executeQuery();

            while (rs.next()) {
                Material m = new Material(
                    String.valueOf(rs.getInt("idMaterial")), 
                    rs.getString("nombre"),
                    rs.getDouble("coste"),
                    rs.getString("urlMaterial")
                );
                lista.add(m);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            pool.freeConnection(con);
        }
        return lista;
    }
}