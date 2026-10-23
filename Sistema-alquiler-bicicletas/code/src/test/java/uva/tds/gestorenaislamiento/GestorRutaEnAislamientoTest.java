package uva.tds.gestorenaislamiento;

import uva.tds.interfaces.*;
import uva.tds.base.*;
import static org.junit.jupiter.api.Assertions.*;
import org.easymock.EasyMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.easymock.Mock;
import org.easymock.EasyMockSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase de prueba para la clase GestorRutaEnAislamiento.
 * Prueba las funcionalidades y validaciones de la clase Ruta.
 * @author Marcos de Diego Martin
 */

public class GestorRutaEnAislamientoTest extends EasyMockSupport {

    @Mock
    private IRutaRepositorio rutaRepo;

    @Mock
    private ICalculoRutas mockCalculo;

    private String token;
    private GestorRutaEnAislamiento gestor;
    private Bicicleta normal;
    private ArrayList<Bicicleta> bicicletas1;
    private Usuario usuario;
    private Parada paradaOrigen;
    private Parada paradaDestino;
    private Ruta ruta;

    @BeforeEach
    public void setUp() {
        token = "ZbttE5J5geI5z9xlEIOp";
        mockCalculo = EasyMock.mock(ICalculoRutas.class);
        rutaRepo = EasyMock.mock(IRutaRepositorio.class);
        gestor = new GestorRutaEnAislamiento(rutaRepo, mockCalculo);
        mockCalculo.identificarse(token);
        bicicletas1 = new ArrayList<>();
        normal = new Bicicleta("1111");
        bicicletas1.add(normal);

        usuario = new Usuario("Juan", "12345678Z", 10, true);
        paradaOrigen = new Parada("o", -90.0, -180.0, "C", bicicletas1, 3, true);
        paradaDestino = new Parada("i", 90.0, 180.0, "C/Manuel Azaña N7 1A", bicicletas1, 3, true);
        ruta = new Ruta("R", usuario, paradaOrigen, paradaDestino);

    }

    @Test
    public void testanadirRutaExistente() {
        EasyMock.expect(rutaRepo.buscarRutaPorIdentificador("R")).andReturn(ruta);
        replayAll();

        assertThrows(IllegalStateException.class, () -> {
            gestor.anadirRuta(ruta);
        });

        verifyAll();
    }

    @Test
    public void testanadirRutaExito() {
        EasyMock.expect(rutaRepo.buscarRutaPorIdentificador("R")).andReturn(null);
        rutaRepo.anadirRuta(ruta);
        EasyMock.replay(rutaRepo);

        gestor.anadirRuta(ruta);

        EasyMock.verify(rutaRepo);
    }

    @Test
    public void testObtenerRutasPorUsuario() {

        ArrayList<Ruta> rutasPorUsuario = new ArrayList<>();
        rutasPorUsuario.add(ruta);

        EasyMock.expect(rutaRepo.obtenerRutasPorUsuario(usuario)).andReturn(rutasPorUsuario);

        EasyMock.replay(rutaRepo);

        List<Ruta> rutasRecuperadas = gestor.obtenerRutasPorUsuario(usuario);
        assertEquals(1, rutasRecuperadas.size());
        assertEquals(ruta, rutasRecuperadas.get(0));

        EasyMock.verify(rutaRepo);
    }

    @Test
    public void testCalcularPuntuacionRuta() {
        double distancia = 10.0;
        int tiempo = 60;

        int puntuacion = gestor.calcularPuntuacionRuta(distancia, tiempo);

        assertEquals(2, puntuacion);
    }

    @Test
    public void testCalcularPuntuacionRutaTiempoCero() {
        double distancia = 10.0;
        int tiempo = 0;

        assertThrows(IllegalArgumentException.class, () -> {
            gestor.calcularPuntuacionRuta(distancia, tiempo);
        });
    }

    @Test
    public void testBuscarRutaPorIdentificador() {
        EasyMock.expect(rutaRepo.buscarRutaPorIdentificador("R")).andReturn(ruta);

        EasyMock.replay(rutaRepo);

        Ruta rutaEncontrada = gestor.buscarRutaPorIdentificador("R");

        assertNotNull(rutaEncontrada);
        assertEquals("R", rutaEncontrada.getIdentificador());

        EasyMock.verify(rutaRepo);
    }

    @Test
    public void testObtenerDistanciaTotal() {
        EasyMock.expect(rutaRepo.buscarRutaPorIdentificador("R")).andReturn(ruta);
        EasyMock.expect(mockCalculo.getDistancia(-90.0, -180.0, 90.0, 180.0)).andReturn(40000000);

        EasyMock.replay(rutaRepo, mockCalculo);

        mockCalculo.identificarse(token);
        double distanciaTotal = gestor.obtenerDistanciaTotal("R");

        assertEquals(40000.0, distanciaTotal);

        EasyMock.verify(rutaRepo, mockCalculo);
    }

    @Test
    public void testObtenerTiempoTotal() {
        EasyMock.expect(rutaRepo.buscarRutaPorIdentificador("R")).andReturn(ruta);
        EasyMock.expect(mockCalculo.clienteIdentificado()).andReturn(true);
        EasyMock.expect(mockCalculo.getTiempo(-90.0, -180.0, 90.0, 180.0)).andReturn(7200);

        EasyMock.replay(rutaRepo, mockCalculo);

        mockCalculo.identificarse(token);
        int tiempoTotal = gestor.obtenerTiempoTotal("R");

        assertEquals(120, tiempoTotal);

        EasyMock.verify(rutaRepo, mockCalculo);
    }

    @Test
    public void testObtenerTiempoTotalClienteNoIdentificado() {
        ICalculoRutas mockCalculo2;
        mockCalculo2 = EasyMock.mock(ICalculoRutas.class);
        gestor = new GestorRutaEnAislamiento(rutaRepo, mockCalculo2);

        rutaRepo.anadirRuta(ruta);
        EasyMock.expect(rutaRepo.buscarRutaPorIdentificador("R")).andReturn(ruta);
        EasyMock.expect(mockCalculo2.clienteIdentificado()).andReturn(false);
        EasyMock.replay(rutaRepo, mockCalculo2);

        assertThrows(IllegalStateException.class, () -> gestor.obtenerTiempoTotal("R"));
        verifyAll();
    }

    @Test
    public void testBuscarRutaPorIdentificadorNoExistente() {
        EasyMock.expect(rutaRepo.buscarRutaPorIdentificador("R")).andReturn(null);
        replayAll();

        assertThrows(IllegalArgumentException.class, () -> {
            gestor.buscarRutaPorIdentificador("R");
        });

        verifyAll();
    }

    @Test
    public void testanadirRutaConRutaNula() {
        Ruta rutaNula = null;
        replayAll();
        assertThrows(IllegalArgumentException.class, () -> {
            gestor.anadirRuta(rutaNula);
        });
        verifyAll();
    }
}
