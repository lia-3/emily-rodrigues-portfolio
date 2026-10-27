package es.inf.uva.dis.persistencia.dbaccess;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBConnection {

    private static DBConnection theDBConnection;
    private Connection conn;
    private final String url;
    private final String user;
    private final String password;

    public static synchronized DBConnection getInstance() {
        if (theDBConnection == null) {
            Properties prop = new Properties();
            File configFile = new File(".." + File.separator + "db" + File.separator + "configdb.env");
            
            try (InputStream is = new FileInputStream(configFile)) {
                prop.load(is);
                
                String url = prop.getProperty("url");
                String user = prop.getProperty("user");
                String pass = prop.getProperty("password");
                
                if (url == null || user == null || pass == null) {
                    throw new IOException("Missing properties in .env file");
                }
                
                theDBConnection = new DBConnection(url, user, pass);
                
            } catch (IOException e) {
                Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return theDBConnection;
    }

    private DBConnection(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
        
    }

    public void openConnection() {
        try {
            if (conn == null || conn.isClosed()) {
                conn = DriverManager.getConnection(url, user, password);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void closeConnection() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public PreparedStatement getStatement(String sql) {
        try {
            if (conn == null || conn.isClosed()) {
                openConnection();
            }
            return conn.prepareStatement(sql);
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public Statement getStatement() {
        try {
            if (conn == null || conn.isClosed()) {
                openConnection();
            }
            return conn.createStatement();
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public Connection getConnection() {
        return conn;
    }
}