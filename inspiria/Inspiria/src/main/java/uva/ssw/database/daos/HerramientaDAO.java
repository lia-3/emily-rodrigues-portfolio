package uva.ssw.database.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import uva.ssw.database.ConnectionPool;
import uva.ssw.modelo.Herramienta;

public class HerramientaDAO {

    private static final String INSERT_HERRAMIENTA = 
            "INSERT INTO Herramienta (idTutorial, nombre, coste, urlHerramienta) VALUES (?, ?, ?, ?)";
    
    private static final String SELECT_HERRAMIENTAS_TUTORIAL = 
            "SELECT * FROM Herramienta WHERE idTutorial = ?";

    // RF-12: Insertar herramienta
    public static boolean insertarHerramienta(Herramienta herramienta, int idTutorial) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection con = pool.getConnection();
        PreparedStatement ps = null;

        try {
            ps = con.prepareStatement(INSERT_HERRAMIENTA);
            ps.setInt(1, idTutorial);
            ps.setString(2, herramienta.getNombre());
            ps.setDouble(3, herramienta.getCoste());
            ps.setString(4, herramienta.getUrl());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            pool.freeConnection(con);
        }
    }

    // RF-14: Listar herramientas de un tutorial
    public static ArrayList<Herramienta> obtenerListaHerramientas(int idTutorial) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection con = pool.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<Herramienta> lista = new ArrayList<>();

        try {
            ps = con.prepareStatement(SELECT_HERRAMIENTAS_TUTORIAL);
            ps.setInt(1, idTutorial);
            rs = ps.executeQuery();

            while (rs.next()) {
                Herramienta h = new Herramienta(
                    String.valueOf(rs.getInt("idHerramienta")),
                    rs.getString("nombre"),
                    rs.getDouble("coste"),
                    rs.getString("urlHerramienta")
                );
                lista.add(h);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            pool.freeConnection(con);
        }
        return lista;
    }
}