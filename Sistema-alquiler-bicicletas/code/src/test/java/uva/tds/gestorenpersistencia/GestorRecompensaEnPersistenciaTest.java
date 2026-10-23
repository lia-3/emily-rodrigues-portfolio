package uva.tds.gestorenpersistencia;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uva.tds.base.Recompensa;
import uva.tds.base.Usuario;
import uva.tds.gestorenaislamiento.GestorRecompensaEnAislamiento;
import uva.tds.implementaciones.RecompensaRepositorio;
import uva.tds.interfaces.IRecompensaRepositorio;

/**
 * Clase de tests que prueba la clase GestorRecompensaEnAislamiento con persistencia
 * mediante base de datos
 * @author Ainhoa Carbajo
 */

public class GestorRecompensaEnPersistenciaTest {
    private IRecompensaRepositorio recompensaRepositorio;
    private GestorRecompensaEnAislamiento sistema;
    private Recompensa recompensa1;
    private Recompensa recompensa2;
    private Usuario usuario;

    @BeforeEach
    void setUp(){
        recompensaRepositorio = new RecompensaRepositorio();
        sistema = new GestorRecompensaEnAislamiento(recompensaRepositorio);
        recompensa1 = new Recompensa("R1", "Recompensa Uno", 100, true);
        recompensa2 = new Recompensa("R2", "Recompensa Dos", 200, false);
        usuario = new Usuario("Juan Perez", "12345678Z", 150, true);

        ((RecompensaRepositorio) recompensaRepositorio).clearDatabase();
       
    }


    @Test
    void testAddRecompensa() {
        sistema.addRecompensa(recompensa1);
        assertEquals(recompensa1, sistema.getRecompensa("R1"));
    }

    @Test
    void testAddRecompensaNula() {
        assertThrows( IllegalArgumentException.class, () -> sistema.addRecompensa(null));
        
    }

    @Test
    void testAddRecompensaRepetida() {
        sistema.addRecompensa(recompensa1);
        assertThrows( IllegalArgumentException.class, () -> sistema.addRecompensa(recompensa1));
        
    }

    @Test
    void testAddRecompensaUsuario() {
        sistema.addRecompensa(recompensa1);
        sistema.addRecompensaUsuario(usuario,"R1");
        assertEquals(recompensa1, sistema.getRecompensa("R1"));
        ArrayList<Recompensa> obtenidas = sistema.obtenerRecompensasUsuario(usuario);
        assertEquals(recompensa1,sistema.getRecompensa("R1"));
        assertEquals(1, obtenidas.size());
        assertTrue(obtenidas.contains(recompensa1));
    }

    @Test
    void testObtenerRecompensasActivas() {
        sistema.addRecompensa(recompensa1);
        sistema.addRecompensa(recompensa2);
        ArrayList<Recompensa> activas = sistema.obtenerRecompensasActivas();
        assertEquals(recompensa1, sistema.getRecompensa("R1"));
        assertEquals(recompensa2, sistema.getRecompensa("R2"));
        assertFalse(activas.contains(recompensa2));
        assertTrue(activas.contains(recompensa1));
    }

    @Test
    public void testActualizarRecompensa() {
       
        sistema.addRecompensa(recompensa1);
        recompensa1.setNombre("Recompensa 1.5");
        sistema.actualizarRecompensa(recompensa1);
        assertEquals("Recompensa 1.5", sistema.getRecompensa("R1").getNombre());
        
    }

    @Test
    public void testActualizarRecompensaNula() {
       
        sistema.addRecompensa(recompensa1);
        assertThrows( IllegalArgumentException.class, () -> sistema.actualizarRecompensa( null));
        
    }

    @Test
    public void testActualizarRecompensaNoRegistrada() {
       assertThrows( IllegalArgumentException.class, () -> sistema.actualizarRecompensa( recompensa1));
        
    }
    
    
}
