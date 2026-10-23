package uva.tds.base;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/*
 * Clase de prueba de la clase Ruta
 * @author Marcos de Diego
 */

public class RutaTest {
    
    Bicicleta normal;
    ArrayList <Bicicleta> bicicletas1;

    
    @BeforeEach
    void startUp(){
        bicicletas1= new ArrayList <Bicicleta>();
        normal= new Bicicleta("1111");
        bicicletas1.add(normal);
        
    }


    @Test
    void rutaValidaLimiteInferior() {
        Usuario usuario = new Usuario("Juan", "12345678Z", 10, true);
        Parada paradaOrigen = new Parada ("o",-90.0,-180,"C", bicicletas1,3,true);
        Parada paradaDestino = new Parada ("i",90.0,180,"C/Manuel Azaña N7 1A", bicicletas1,3,true);

        Ruta ruta = new Ruta("R", usuario, paradaOrigen, paradaDestino);

        assertEquals("R", ruta.getIdentificador());
        assertEquals(usuario, ruta.getUsuario());
        assertEquals(paradaOrigen, ruta.getParadaOrigen());
        assertEquals(paradaDestino, ruta.getParadaDestino());
    }


    @Test
    void rutaValidaLimiteSuperior() {
        Usuario usuario = new Usuario("Juan", "12345678Z", 10, true);
        Parada paradaOrigen = new Parada ("o",-90.0,-180,"C", bicicletas1,3,true);
        Parada paradaDestino = new Parada ("i",90.0,180,"C/Manuel Azaña N7 1A", bicicletas1,3,true);

        Ruta ruta = new Ruta("R234567", usuario, paradaOrigen, paradaDestino);

        assertEquals("R234567", ruta.getIdentificador());
        assertEquals(usuario, ruta.getUsuario());
        assertEquals(paradaOrigen, ruta.getParadaOrigen());
        assertEquals(paradaDestino, ruta.getParadaDestino());
    }


    @Test
    void rutaInvalidaIdMenorLimiteInferior() {
        Usuario usuario = new Usuario("Juan", "12345678Z", 10, true);
        Parada paradaOrigen = new Parada ("o",-90.0,-180,"C", bicicletas1,3,true);
        Parada paradaDestino = new Parada ("i",90.0,180,"C/Manuel Azaña N7 1A", bicicletas1,3,true);

        assertThrows(IllegalArgumentException.class, () -> {new Ruta(" ", usuario, paradaOrigen, paradaDestino);});

    }

    @Test
    void rutaInvalidaIdMayorLimiteSuperior() {
        Usuario usuario = new Usuario("Juan", "12345678Z", 10, true);
        Parada paradaOrigen = new Parada ("o",-90.0,-180,"C", bicicletas1,3,true);
        Parada paradaDestino = new Parada ("i",90.0,180,"C/Manuel Azaña N7 1A", bicicletas1,3,true);

        assertThrows(IllegalArgumentException.class, () -> {new Ruta("12345678", usuario, paradaOrigen, paradaDestino);});

    }

    @Test
    void rutaInvalidaIdNulo() {
        Usuario usuario = new Usuario("Juan", "12345678Z", 10, true);
        Parada paradaOrigen = new Parada ("o",-90.0,-180,"C", bicicletas1,3,true);
        Parada paradaDestino = new Parada ("i",90.0,180,"C/Manuel Azaña N7 1A", bicicletas1,3,true);

        assertThrows(IllegalArgumentException.class, () -> {new Ruta(null, usuario, paradaOrigen, paradaDestino);});

    }

    @Test
    void rutaInvalidaUsuarioNulo() {
        Parada paradaOrigen = new Parada ("o",-90.0,-180,"C", bicicletas1,3,true);
        Parada paradaDestino = new Parada ("i",90.0,180,"C/Manuel Azaña N7 1A", bicicletas1,3,true);

        assertThrows(IllegalArgumentException.class, () -> {new Ruta("r", null, paradaOrigen, paradaDestino);});

    }

    @Test
    void rutaInvalidaParadaOrigenNulo() {
        Usuario usuario = new Usuario("Juan", "12345678Z", 10, true);
        Parada paradaDestino = new Parada ("i",90.0,180,"C/Manuel Azaña N7 1A", bicicletas1,3,true);

        assertThrows(IllegalArgumentException.class, () -> {new Ruta("12345", usuario, null, paradaDestino);});

    }

    @Test
    void rutaInvalidaParadaDestinoNulo() {
        Usuario usuario = new Usuario("Juan", "12345678Z", 10, true);
        Parada paradaOrigen = new Parada ("o",-90.0,-180,"C", bicicletas1,3,true);

        assertThrows(IllegalArgumentException.class, () -> {new Ruta("1238", usuario, paradaOrigen, null);});

    }

    @Test
    void testGetParadas() {
        Usuario usuario = new Usuario("Juan", "12345678Z", 10, true);
        Parada paradaOrigen = new Parada("o", -90.0, -180, "C", bicicletas1, 3, true);
        Parada paradaDestino = new Parada("i", 90.0, 180, "C/Manuel Azaña N7 1A", bicicletas1, 3, true);

        Ruta ruta = new Ruta("R", usuario, paradaOrigen, paradaDestino);

        

        // Verificamos que las paradas sean las esperadas
        assertEquals(paradaOrigen, ruta.getParadaOrigen());
        assertEquals(paradaDestino, ruta.getParadaDestino());
    }

   

}