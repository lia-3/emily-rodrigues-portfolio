package uva.ssw.database;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Clase encargada de insertar imágenes en las tablas de la base de datos.
 * Añade imágenes para categorías, temáticas, perfiles, tutoriales y productos.
 * Las categorías y temáticas bases y se necesitan para que funcione la página.
 * Los tutoriales, productos y usuarios son creados para poder realizar pruebas en la página.
 */
public class InsercionImagenes {

    private static final String URL = "jdbc:mariadb://localhost:3306/inspiria";
    private static final String USER = "root";
    private static final String PASS = "root1234"; 

    /* Rutas Base */
    private static final String BASE_PATH = "src/main/resources/sql/imagenes/";
    private static final String RUTA_CATEGORIAS = BASE_PATH + "categorias/";
    private static final String RUTA_TEMATICAS = BASE_PATH + "tematicas/";
    private static final String RUTA_PERFILES = BASE_PATH + "perfiles/";
    private static final String RUTA_TUTORIALES = BASE_PATH + "tutoriales/";
    private static final String RUTA_PRODUCTOS = BASE_PATH + "productos/";

    /* Rutas de Imágenes de Categorías */
    private static final String CERAMICA = RUTA_CATEGORIAS + "ceramica.jpg";
    private static final String CROCHET = RUTA_CATEGORIAS + "crochet.jpg";
    private static final String BISUTERIA = RUTA_CATEGORIAS + "bisuteria.jpg";
    private static final String COSTURA = RUTA_CATEGORIAS + "costura.jpg";
    private static final String PAPELERIA = RUTA_CATEGORIAS + "papeleria.jpg";
    private static final String ALFARERIA = RUTA_CATEGORIAS + "alfareria.jpg";
    private static final String PAPIROFLEXIA = RUTA_CATEGORIAS + "papiroflexia.jpg";

     /* Rutas de Imágenes de Temáticas */   
    private static final String NAVIDAD = RUTA_TEMATICAS + "navidad.jpg";
    private static final String HALLOWEEN = RUTA_TEMATICAS + "halloween.jpg";
    private static final String SAN_VALENTIN = RUTA_TEMATICAS + "san_valentin.jpg";
    private static final String HOGAR = RUTA_TEMATICAS + "hogar.jpg";
    private static final String OTONAL = RUTA_TEMATICAS + "otonal.jpg";
    private static final String PRIMAVERA = RUTA_TEMATICAS + "primavera.jpg";
    private static final String VERANO = RUTA_TEMATICAS + "verano.jpg";
    private static final String INVIERNO = RUTA_TEMATICAS + "invierno.jpg";
    private static final String CUMPLEAGNO = RUTA_TEMATICAS + "cumpleagno.jpg";
    private static final String BODAS = RUTA_TEMATICAS + "boda.jpg";
    private static final String INFANTIL = RUTA_TEMATICAS + "infantil.jpg";
    private static final String FIESTA = RUTA_TEMATICAS + "fiesta.jpg";


     /* Rutas de Imágenes de Perfiles */
    private static final String MARIA = RUTA_PERFILES + "maria.jpg";
    private static final String JUAN = RUTA_PERFILES + "juan.jpg";
    private static final String LAURA = RUTA_PERFILES + "laura.jpg";
    private static final String CARLOS = RUTA_PERFILES + "carlos.jpg";
    private static final String ANA = RUTA_PERFILES + "ana.jpg";
    private static final String PABLO = RUTA_PERFILES + "pablo.jpg";
    private static final String SOFIA = RUTA_PERFILES + "sofia.jpg";

    /* Rutas de Imágenes de Tutoriales */
    private static final String TAZA = RUTA_TUTORIALES + "taza.jpg";
    private static final String BUFANDA = RUTA_TUTORIALES + "bufanda.jpg";
    private static final String PLATO = RUTA_TUTORIALES + "plato.jpg";
    private static final String PENDIENTES = RUTA_TUTORIALES + "pendientes.jpg";
    private static final String BOLSA = RUTA_TUTORIALES + "bolsa.jpg";
    private static final String LINTERNA_HALLOWEEN = RUTA_TUTORIALES + "lampara_halloween_papel.jpg";
    private static final String CUENCO = RUTA_TUTORIALES + "cuenco.jpg";
    private static final String PENDIENTES_VERANO = RUTA_TUTORIALES + "pendientes_verano.jpg";
    private static final String COJIN = RUTA_TUTORIALES + "cojin.jpg";
    private static final String TAZA_CUMPLE = RUTA_TUTORIALES + "taza_cumple.jpg";
    private static final String OSO = RUTA_TUTORIALES + "oso.jpg";
    private static final String GUIRNALDA = RUTA_TUTORIALES + "guirnalda.jpg";
    private static final String BOLSO = RUTA_TUTORIALES + "bolso.jpg";
    
    /* Rutas de Imágenes de productos*/
    private static final String AMIGURI = RUTA_PRODUCTOS + "amiguri.jpg";
    private static final String LAMPARA = RUTA_PRODUCTOS + "lampara.jpg";
    private static final String JARRON = RUTA_PRODUCTOS + "jarron.jpg";

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) {
            
            insertarImagenesCategorias(conn);
            insertarImagenesTematicas(conn);
            insertarImagenesPerfiles(conn);
            insertarImagenesTutoriales(conn);
            insertarImagenesProductos(conn);
            insertarImagenesPasos(conn);
            
        } catch (SQLException e) {
            System.err.println("Error crítico de conexión: " + e.getMessage());
        }
    }

    public static void insertarImagenesCategorias(Connection conn) {
        insertarImagenGenerica(conn, "Categoria", "imagenCategoria", "nombreCategoria", "Cerámica", CERAMICA);
        insertarImagenGenerica(conn, "Categoria", "imagenCategoria", "nombreCategoria", "Crochet", CROCHET);
        insertarImagenGenerica(conn, "Categoria", "imagenCategoria", "nombreCategoria", "Bisutería", BISUTERIA);
        insertarImagenGenerica(conn, "Categoria", "imagenCategoria", "nombreCategoria", "Costura", COSTURA);
        insertarImagenGenerica(conn, "Categoria", "imagenCategoria", "nombreCategoria", "Papelería", PAPELERIA);
        insertarImagenGenerica(conn, "Categoria", "imagenCategoria", "nombreCategoria", "Alfarería", ALFARERIA);
        insertarImagenGenerica(conn, "Categoria", "imagenCategoria", "nombreCategoria", "Papiroflexia", PAPIROFLEXIA);
    }

    public static void insertarImagenesTematicas(Connection conn) {
        insertarImagenGenerica(conn, "Tematica", "imagenTematica", "nombreTematica", "Navidad", NAVIDAD);
        insertarImagenGenerica(conn, "Tematica", "imagenTematica", "nombreTematica", "Halloween", HALLOWEEN);
        insertarImagenGenerica(conn, "Tematica", "imagenTematica", "nombreTematica", "San Valentín", SAN_VALENTIN);
        insertarImagenGenerica(conn, "Tematica", "imagenTematica", "nombreTematica", "Hogar", HOGAR);
        insertarImagenGenerica(conn, "Tematica", "imagenTematica", "nombreTematica", "Otoñal", OTONAL);
        insertarImagenGenerica(conn, "Tematica", "imagenTematica", "nombreTematica", "Primavera", PRIMAVERA);
        insertarImagenGenerica(conn, "Tematica", "imagenTematica", "nombreTematica", "Verano", VERANO);
        insertarImagenGenerica(conn, "Tematica", "imagenTematica", "nombreTematica", "Invierno", INVIERNO);
        insertarImagenGenerica(conn, "Tematica", "imagenTematica", "nombreTematica", "Cumpleaños", CUMPLEAGNO);
        insertarImagenGenerica(conn, "Tematica", "imagenTematica", "nombreTematica", "Bodas", BODAS);
        insertarImagenGenerica(conn, "Tematica", "imagenTematica", "nombreTematica", "Infantil", INFANTIL);
        insertarImagenGenerica(conn, "Tematica", "imagenTematica", "nombreTematica", "Fiestas", FIESTA);   
    }

    public static void insertarImagenesPerfiles(Connection conn) {
        insertarImagenGenerica(conn, "Usuario", "fotoPerfil", "nombre", "maria_perez", MARIA);
        insertarImagenGenerica(conn, "Usuario", "fotoPerfil", "nombre", "juan_diy", JUAN);
        insertarImagenGenerica(conn, "Usuario", "fotoPerfil", "nombre", "laura_art", LAURA);
        insertarImagenGenerica(conn, "Usuario", "fotoPerfil", "nombre", "carlos_craft", CARLOS);
        insertarImagenGenerica(conn, "Usuario", "fotoPerfil", "nombre", "ana_crochet", ANA);
        insertarImagenGenerica(conn, "Usuario", "fotoPerfil", "nombre", "pablo_papel", PABLO);
        insertarImagenGenerica(conn, "Usuario", "fotoPerfil", "nombre", "sofia_tejidos", SOFIA);
    }

    public static void insertarImagenesTutoriales(Connection conn) {
        insertarImagenGenerica(conn, "Tutorial", "portada", "titulo", "Taza de cerámica", TAZA);
        insertarImagenGenerica(conn, "Tutorial", "portada", "titulo", "Bufanda de crochet", BUFANDA);
        insertarImagenGenerica(conn, "Tutorial", "portada", "titulo", "Plato decorativo", PLATO);
        insertarImagenGenerica(conn, "Tutorial", "portada", "titulo", "Pendientes Corazón", PENDIENTES);
        insertarImagenGenerica(conn, "Tutorial", "portada", "titulo", "Bolsa de tela fácil", BOLSA);
        insertarImagenGenerica(conn, "Tutorial", "portada", "titulo", "Linterna Halloween", LINTERNA_HALLOWEEN);
        insertarImagenGenerica(conn, "Tutorial", "portada", "titulo", "Cuenco otoñal", CUENCO);
        insertarImagenGenerica(conn, "Tutorial", "portada", "titulo", "Pendientes verano", PENDIENTES_VERANO);
        insertarImagenGenerica(conn, "Tutorial", "portada", "titulo", "Cojín de invierno", COJIN);
        insertarImagenGenerica(conn, "Tutorial", "portada", "titulo", "Taza cumpleaños", TAZA_CUMPLE);
        insertarImagenGenerica(conn, "Tutorial", "portada", "titulo", "Peluche infantil", OSO);
        insertarImagenGenerica(conn, "Tutorial", "portada", "titulo", "Guirnalda de papel", GUIRNALDA);
        insertarImagenGenerica(conn, "Tutorial", "portada", "titulo", "Bolso de primavera", BOLSO);
    }
    
    public static void insertarImagenesProductos(Connection conn) {
        insertarImagenGenerica(conn, "Producto", "imagen", "titulo", "Taza de cerámica", TAZA);
        insertarImagenGenerica(conn, "Producto", "imagen", "titulo", "Bufanda de crochet", BUFANDA);
        insertarImagenGenerica(conn, "Producto", "imagen", "titulo", "Cuenco otoñal", CUENCO);
        insertarImagenGenerica(conn, "Producto", "imagen", "titulo", "Taza cumpleaños", TAZA_CUMPLE);
        insertarImagenGenerica(conn, "Producto", "imagen", "titulo", "Peluche infantil", OSO);
        
        insertarImagenGenerica(conn, "Producto", "imagen", "titulo", "Amigurumi Ballena Azul", AMIGURI);
        insertarImagenGenerica(conn, "Producto", "imagen", "titulo", "Lámpara de Origami", LAMPARA);
        insertarImagenGenerica(conn, "Producto", "imagen", "titulo", "Jarrón Minimalista", JARRON);
        insertarImagenGenerica(conn, "Producto", "imagen", "titulo", "Set linternas Halloween", LINTERNA_HALLOWEEN);
        insertarImagenGenerica(conn, "Producto", "imagen", "titulo", "Set pendientes de resina", PENDIENTES_VERANO);
        insertarImagenGenerica(conn, "Producto", "imagen", "titulo", "Cojín polar bordado", COJIN);

    }
    
    public static void insertarImagenesPasos(Connection conn) {
        // Tutorial 1: Taza de cerámica
        insertarImagenPaso(conn, 1, 1, RUTA_TUTORIALES + "taza_paso1.jpg");
        insertarImagenPaso(conn, 1, 2, RUTA_TUTORIALES + "taza_paso2.jpg");
        insertarImagenPaso(conn, 1, 3, RUTA_TUTORIALES + "taza_paso3.jpg");
        insertarImagenPaso(conn, 1, 4, RUTA_TUTORIALES + "taza_paso4.jpg");
        insertarImagenPaso(conn, 1, 5, RUTA_TUTORIALES + "taza_paso5.jpg");

        // Tutorial 2: Bufanda de crochet
        insertarImagenPaso(conn, 2, 1, RUTA_TUTORIALES + "bufanda_paso1.jpg");
        insertarImagenPaso(conn, 2, 2, RUTA_TUTORIALES + "bufanda_paso2.jpg");
        insertarImagenPaso(conn, 2, 3, RUTA_TUTORIALES + "bufanda_paso3.jpg");
        insertarImagenPaso(conn, 2, 4, RUTA_TUTORIALES + "bufanda_paso4.jpg");
        insertarImagenPaso(conn, 2, 5, RUTA_TUTORIALES + "bufanda_paso5.jpg");

        // Tutorial 3: Plato decorativo
        insertarImagenPaso(conn, 3, 1, RUTA_TUTORIALES + "plato_paso1.jpg");
        insertarImagenPaso(conn, 3, 2, RUTA_TUTORIALES + "plato_paso2.jpg");
        insertarImagenPaso(conn, 3, 3, RUTA_TUTORIALES + "plato_paso3.jpg");
        insertarImagenPaso(conn, 3, 4, RUTA_TUTORIALES + "plato_paso4.jpg");
        insertarImagenPaso(conn, 3, 5, RUTA_TUTORIALES + "plato_paso5.jpg");
        
        // Tutorial 4: Pendientes Corazón
        insertarImagenPaso(conn, 4, 1, RUTA_TUTORIALES + "pendientes_paso1.jpg");
        insertarImagenPaso(conn, 4, 2, RUTA_TUTORIALES + "pendientes_paso2.jpg");
        insertarImagenPaso(conn, 4, 3, RUTA_TUTORIALES + "pendientes_paso3.jpg");
        insertarImagenPaso(conn, 4, 4, RUTA_TUTORIALES + "pendientes_paso4.jpg");
        insertarImagenPaso(conn, 4, 5, RUTA_TUTORIALES + "pendientes_paso5.jpg");
    }

    private static void insertarImagenGenerica(Connection conn, String tabla, String colImagen, String colNombre, String valorNombre, String rutaCompleta) {
        String sql = "UPDATE " + tabla + " SET " + colImagen + " = ? WHERE " + colNombre + " = ?";
        PreparedStatement pstmt = null;

        try {
            File file = new File(rutaCompleta);
            if (!file.exists()) {
                System.err.println("No encuentro la foto en: " + file.getAbsolutePath());
                return; 
            }

            pstmt = conn.prepareStatement(sql);

            try (FileInputStream fis = new FileInputStream(file)) {
                pstmt.setBinaryStream(1, fis, (int) file.length());
                pstmt.setString(2, valorNombre);
                
                if (pstmt.executeUpdate() > 0) {
                    System.out.println("[" + tabla + "] " + valorNombre + " cargada.");
                } else {
                    System.out.println("[" + tabla + "] No se encontró el registro: " + valorNombre);
                }
            }
        } catch (Exception e) {
            System.err.println("Error en " + tabla + " (" + valorNombre + "): " + e.getMessage());
        } finally {
            DBUtil.closePreparedStatement(pstmt);
        }
    }
    
    private static void insertarImagenPaso(Connection conn, int idTutorial, int numeroPaso, String rutaCompleta) {
        String sql = "UPDATE Paso SET imagenPaso = ? WHERE idTutorial = ? AND numero = ?";
        PreparedStatement pstmt = null;

        try {
            File file = new File(rutaCompleta);
            if (file.exists()) {
                pstmt = conn.prepareStatement(sql);

                try (FileInputStream fis = new FileInputStream(file)) {
                    pstmt.setBinaryStream(1, fis, (int) file.length());
                    pstmt.setInt(2, idTutorial);
                    pstmt.setInt(3, numeroPaso);
                    
                    if (pstmt.executeUpdate() > 0) {
                        System.out.println("[Paso] Tutorial " + idTutorial + " - Paso " + numeroPaso + " cargada.");
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error en Paso (Tutorial " + idTutorial + ", Paso " + numeroPaso + "): " + e.getMessage());
        } finally {
            DBUtil.closePreparedStatement(pstmt);
        }
    }
}