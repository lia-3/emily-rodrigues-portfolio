package uva.tds.gestorenpersistencia;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uva.tds.base.Ruta;
import uva.tds.base.Usuario;
import uva.tds.base.Bicicleta;
import uva.tds.base.Parada;
import uva.tds.gestorenaislamiento.GestorRutaEnAislamiento;
import uva.tds.implementaciones.RutaRepositorio;
import uva.tds.interfaces.ICalculoRutas;
import uva.tds.interfaces.IRutaRepositorio;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Clase de tests que prueba la clase GestorRutasEnAislamiento con persistencia
 * mediante base de datos
 * @author Marcos de Diego
 */
public class GestorRutaEnPersistenciaTest {

    private IRutaRepositorio rutaRepositorio;
    private ICalculoRutas servicioCalculo;
    private GestorRutaEnAislamiento sistema;
    private Usuario usuarioJuan;
    private ArrayList<Bicicleta> bicicletas1;
    private ArrayList<Bicicleta> bicicletas2;
    private Ruta ruta1;
    private String identificador1;
    private Bicicleta normal;
    private Bicicleta normal2;

    @BeforeEach
    public void setUp() {
        rutaRepositorio = new RutaRepositorio();
        sistema = new GestorRutaEnAislamiento(rutaRepositorio, servicioCalculo);

        bicicletas1 = new ArrayList<>();
        bicicletas2 = new ArrayList<>();
        normal = new Bicicleta("1111");
        normal2 = new Bicicleta("1112");
        bicicletas1.add(normal);
        bicicletas2.add(normal2);
        usuarioJuan = new Usuario("Juan", "12345678Z", 10, true);
        Parada paradaOrigen = new Parada("Parada1", 10.0, 20.0, "DirecciónA", bicicletas1, 5, true);
        Parada paradaDestino = new Parada("Parada2", 30.0, 40.0, "DirecciónB", bicicletas2, 3, true);

        identificador1 = "R1";
        ruta1 = new Ruta(identificador1, usuarioJuan, paradaOrigen, paradaDestino);
    }

     @BeforeEach
    public void limpiarBaseDeDatos() {
        ((RutaRepositorio) rutaRepositorio).clearDatabase();
        // Repite para otras entidades o usa scripts SQL para reiniciar todo.
    }

    @Test
    public void testanadirRuta() {
        sistema.anadirRuta(ruta1);
        assertEquals(identificador1, ruta1.getIdentificador());
        assertEquals(ruta1, sistema.buscarRutaPorIdentificador(ruta1.getIdentificador()));
    }

    @Test
    public void testanadirRutaExistente() {
        sistema.anadirRuta(ruta1);
        assertThrows(IllegalArgumentException.class, () -> sistema.anadirRuta(ruta1));
    }

    @Test
    public void testanadirRutaNula() {
        assertThrows(IllegalArgumentException.class, () -> sistema.anadirRuta(null));
    }

    @Test
    public void testBuscarRutaPorIdentificador() {
        sistema.anadirRuta(ruta1);
        Ruta rutaEncontrada = sistema.buscarRutaPorIdentificador(identificador1);
        assertEquals(ruta1, rutaEncontrada);
    }

    @Test
    public void testBuscarRutaPorIdentificadorNoExistente() {
        assertThrows(IllegalArgumentException.class, () -> sistema.buscarRutaPorIdentificador("NoExiste"));
    }

    @Test
    public void testObtenerRutasPorUsuario() {
        sistema.anadirRuta(ruta1);

        List<Ruta> rutasUsuario = sistema.obtenerRutasPorUsuario(usuarioJuan);
        assertEquals(1, rutasUsuario.size());
        assertTrue(rutasUsuario.contains(ruta1));
    }

    @Test
    public void testObtenerRutasPorUsuarioSinRutas() {
        List<Ruta> rutasUsuario = sistema.obtenerRutasPorUsuario(usuarioJuan);
        assertTrue(rutasUsuario.isEmpty());
    }

    @Test
    public void testCalcularPuntuacionRuta() {
        double distancia = 10.0;
        int tiempo = 20;
        int puntuacion = sistema.calcularPuntuacionRuta(distancia, tiempo);
        assertEquals(5, puntuacion);
    }

    @Test
    public void testCalcularPuntuacionRutaTiempoCero() {
        double distancia = 10.0;
        int tiempo = 0;
        assertThrows(IllegalArgumentException.class, () -> sistema.calcularPuntuacionRuta(distancia, tiempo));
    }


    @AfterEach
	void tearDown() {
		rutaRepositorio.clearDatabase();
	}

}
