package uva.tds.gestores;

import uva.tds.interfaces.ICalculoRutas;
import uva.tds.base.*;

import static org.junit.jupiter.api.Assertions.*;
import org.easymock.EasyMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.easymock.Mock;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase de tests que prueba la clase GestorRuta
 * @author Marcos de Diego Martín
 * @author Ainhoa Carbajo
 */

public class GestorRutasTest {
    @Mock
    private ICalculoRutas mockCalculo;

    private GestorRutas gestor;
    private Usuario usuario;
    private Parada paradaOrigen;
    private Parada paradaDestino;
    private Ruta ruta;
    private Ruta ruta2;

    private String token;
    Bicicleta normal;
    ArrayList<Bicicleta> bicicletas1;

    @BeforeEach
    public void setUp() {
        token = "ZbttE5J5geI5z9xlEIOp";
        mockCalculo = EasyMock.mock(ICalculoRutas.class);
        // Identificar al cliente en el mock antes de las pruebas
        mockCalculo.identificarse(token);

        gestor = new GestorRutas(mockCalculo);
        usuario = new Usuario("Juan", "12345678Z", 10, true);

        bicicletas1 = new ArrayList<Bicicleta>();
        normal = new Bicicleta("1111");
        bicicletas1.add(normal);

        paradaOrigen = new Parada("o", -90.0, -180, "C", bicicletas1, 3, true);
        paradaDestino = new Parada("i", 90.0, 180, "C/Manuel Azaña N7 1A", bicicletas1, 3, true);
        ruta = new Ruta("R1", usuario, paradaOrigen, paradaDestino);
        ruta2 = new Ruta("R2", usuario, paradaOrigen, paradaDestino);
    }

    @Test
    public void usoServicioCalculoSinIdentificar() {
        ICalculoRutas mockCalculo2 = EasyMock.mock(ICalculoRutas.class);
        gestor = new GestorRutas(mockCalculo2);
        EasyMock.expect(mockCalculo2.clienteIdentificado()).andReturn(false);

        EasyMock.replay(mockCalculo2);
        // Añadir la ruta al gestor
        gestor.agregarRuta(ruta);
        assertThrows(IllegalStateException.class, () -> gestor.obtenerDistanciaTotal("R1"));
        EasyMock.verify(mockCalculo2);
    }

    @Test
    public void testAgregarRuta() {
        gestor.agregarRuta(ruta);
        assertEquals(1, gestor.obtenerRutasPorUsuario(usuario).size());

    }

    @Test
    public void testAgregarRutaNull() {
        assertThrows(IllegalArgumentException.class, () -> gestor.agregarRuta(null));
    }

    @Test
    public void testAgregarRutaDuplicada() {
        gestor.agregarRuta(ruta);
        assertThrows(IllegalStateException.class, () -> gestor.agregarRuta(ruta));
    }

    @Test
    public void testObtenerRutasPorUsuario() {
        gestor.agregarRuta(ruta);
        gestor.agregarRuta(ruta2);
        List<Ruta> rutasUsuario = gestor.obtenerRutasPorUsuario(usuario);
        assertEquals(2, rutasUsuario.size());
        assertTrue(rutasUsuario.contains(ruta));
        assertTrue(rutasUsuario.contains(ruta2));
    }

    @Test
    public void testObtenerRutasPorUsuarioConUsuarioNull() {
        assertThrows(IllegalArgumentException.class, () -> gestor.obtenerRutasPorUsuario(null));
    }

    @Test
    public void testCalcularPuntuacionRuta() {
        double distancia = 100.0;
        int tiempo = 60;
        int puntuacionEsperada = (int) Math.round((distancia / tiempo) * 10);
        int puntuacion = gestor.calcularPuntuacionRuta(distancia, tiempo);
        assertEquals(puntuacionEsperada, puntuacion);
    }

    @Test
    public void testCalcularPuntuacionRutaMenorLimiteInferior() {
        double distancia = 100.0;
        int tiempo = -60;
        assertThrows(IllegalArgumentException.class, () -> gestor.calcularPuntuacionRuta(distancia, tiempo));
    }

    @Test
    public void testObtenerDistanciaTotal() {
        // Añadir la ruta al gestor
        gestor.agregarRuta(ruta);

        EasyMock.expect(mockCalculo.getDistancia(-90.0, -180.0, 90.0, 180.0)).andReturn(402);
        EasyMock.expect(mockCalculo.clienteIdentificado()).andReturn(true);
        EasyMock.replay(mockCalculo);
        // Invocar el método a probar
        double distanciaTotal = gestor.obtenerDistanciaTotal("R1");

        // Verificar la distancia total
        assertEquals(0.402, distanciaTotal, 0.01); // 0.5 km
        EasyMock.verify();
    }

    @Test
    public void testObtenerDistanciaTotalIdNulo() {

        assertThrows(IllegalArgumentException.class, () -> gestor.obtenerDistanciaTotal(null));
    }

    @Test
    public void testObtenerDistanciaTotalIdMenorLimiteInferior() {

        assertThrows(IllegalStateException.class, () -> gestor.obtenerDistanciaTotal(""));
    }

    @Test
    public void testObtenerDistanciaTotalIdMayorLimiteSuperior() {

        assertThrows(IllegalStateException.class, () -> gestor.obtenerDistanciaTotal("12345678"));
    }

    @Test
    public void testBuscarRutaPorIdentificadorNoExistente() {

        assertThrows(IllegalStateException.class, () -> gestor.obtenerDistanciaTotal("R3"));
    }

    @Test
    public void testObtenerTiempoTotal() {
        // Añadir la ruta al gestor
        gestor.agregarRuta(ruta);

        EasyMock.expect(mockCalculo.getTiempo(-90.0, -180.0, 90.0, 180.0)).andReturn(1340);
        EasyMock.expect(mockCalculo.clienteIdentificado()).andReturn(true);
        EasyMock.replay(mockCalculo);

        // Invocar el método a probar
        int tiempoTotal = gestor.obtenerTiempoTotal("R1");

        // Verificar el tiempo total
        assertEquals(22, tiempoTotal); // 30 minutos
        EasyMock.verify();
    }

    @Test
    public void testObtenerTiempoTotalNoIdentificado() {
        ICalculoRutas mockCalculo2 = EasyMock.mock(ICalculoRutas.class);
        gestor = new GestorRutas(mockCalculo2);
        EasyMock.expect(mockCalculo2.clienteIdentificado()).andReturn(false);
        EasyMock.replay(mockCalculo2);
        // Añadir la ruta al gestor
        gestor.agregarRuta(ruta);
        assertThrows(IllegalStateException.class, () -> gestor.obtenerTiempoTotal("R1"));
        EasyMock.verify(mockCalculo2);

    }

    @Test
    void testBuscarRutaPorIdentificadorConIdentificadorNulo() {
        assertThrows(IllegalArgumentException.class, () -> {
            gestor.buscarRutaPorIdentificador(null);
        });
    }

    @Test
    void testBuscarRutaPorIdentificadorConRutaNoExistente() {
        gestor.agregarRuta(ruta);

        assertThrows(IllegalStateException.class, () -> {
            gestor.buscarRutaPorIdentificador("R3");
        });
    }

    @Test
    public void testCalcularPuntuacionRutaDistanciaMenorLimiteInferior() {
        double distancia = -100.0;
        int tiempo = 60;
        assertThrows(IllegalArgumentException.class, () -> gestor.calcularPuntuacionRuta(distancia, tiempo));
    }

}
