package uva.ssw.database.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import uva.ssw.database.ConnectionPool;
import uva.ssw.modelo.Paso;

public class PasoDAO {

    private static final String INSERT_PASO = 
            "INSERT INTO Paso (idTutorial, numero, descripcion, imagenPaso) VALUES (?, ?, ?, ?)";
    
    private static final String SELECT_PASOS_TUTORIAL = 
            "SELECT * FROM Paso WHERE idTutorial = ? ORDER BY numero ASC";

    public static boolean insertarPaso(Paso paso, int idTutorial) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection con = pool.getConnection();
        PreparedStatement ps = null;

        try {
            ps = con.prepareStatement(INSERT_PASO);
            ps.setInt(1, idTutorial);
            ps.setInt(2, paso.getNumero());
            ps.setString(3, paso.getDescripcion());
            if (paso.getImagen() != null) {
                ps.setBytes(4, paso.getImagen());
            } else {
                ps.setNull(4, java.sql.Types.BLOB);
            }

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            pool.freeConnection(con);
        }
    }


    public static ArrayList<Paso> obtenerPasosTutorial(int idTutorial) {
        ConnectionPool pool = ConnectionPool.getInstance();
        Connection con = pool.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<Paso> lista = new ArrayList<>();

        try {
            ps = con.prepareStatement(SELECT_PASOS_TUTORIAL);
            ps.setInt(1, idTutorial);
            rs = ps.executeQuery();

            while (rs.next()) {
                byte[] imagenAux = rs.getBytes("imagenPaso");

                if (imagenAux == null) {
                    imagenAux = new byte[0];
                }

                // Creamos el paso como lo hacías tú
                Paso p = new Paso(
                    rs.getInt("numero"),
                    rs.getString("descripcion"),
                    imagenAux 
                );
                
                // --- ¡AQUÍ ESTÁ LA MAGIA! ---
                // Si la imagen tiene datos, la convertimos a Base64 para el JSP
                if (imagenAux.length > 0) {
                    p.setFotoBase64(java.util.Base64.getEncoder().encodeToString(imagenAux));
                }
                
                lista.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            pool.freeConnection(con);
        }
        return lista;
    }
}