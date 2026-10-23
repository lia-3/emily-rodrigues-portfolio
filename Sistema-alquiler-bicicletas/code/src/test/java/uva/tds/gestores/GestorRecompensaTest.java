package uva.tds.gestores;
import uva.tds.base.Recompensa;
import uva.tds.base.Usuario;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


/**
 * Clase de tests que prueba la clase GestorRecompensa
 * @author Marcos de Diego Martín
 * @author Emily Rodrigues
 * @author Ainhoa Carbajo Orgaz
 */
public class GestorRecompensaTest {


    private GestorRecompensas gestor;
    private Recompensa recompensa1;
    private Recompensa recompensa2;
    private Recompensa recompensa3;
    private Usuario usuario;

    @BeforeEach
    public void startUp() {
        gestor = new GestorRecompensas();
        recompensa1 = new Recompensa("R1", "Recompensa Uno", 100, true);
        recompensa2 = new Recompensa("R2", "Recompensa Dos", 200, false);
        recompensa3 = new Recompensa("R3", "Recompensa Tres",50 , true);
        usuario = new Usuario("Juan Perez", "12345678Z", 150, true);
    }

    @Test
    public void testObtenerListaRecompensasActivas() {
        gestor.addRecompensa(recompensa1);
        gestor.addRecompensa(recompensa2);
        ArrayList<Recompensa> activas = gestor.obtenerRecompensasActivas();
        assertEquals(recompensa1,gestor.getRecompensa("R1"));
        assertFalse(activas.contains(recompensa2));
    }

    @Test
    public void testEliminarRecompensa() {
        gestor.addRecompensa(recompensa1);
        gestor.addRecompensa(recompensa3);
        gestor.eliminaRecompensa("R3");
        ArrayList<Recompensa> activas = gestor.obtenerRecompensasActivas();
        assertFalse(activas.contains(recompensa3));
    }

    @Test
    public void testEliminarRecompensaNula() {
        assertThrows(IllegalArgumentException.class, () -> gestor.eliminaRecompensa(null));
    }

    @Test
    public void testEliminarRecompensaNoExistente() {
        assertThrows(IllegalArgumentException.class, () -> gestor.eliminaRecompensa("R1"));
    }
    
    

    @Test
    public void testAñadirRecompensasMismoId() {
        Recompensa recompensa4 = new Recompensa("R1", "Recompensa Uno", 100, true);
        gestor.addRecompensa(recompensa1);
        gestor.addRecompensa(recompensa2);
        assertThrows(IllegalArgumentException.class, () -> gestor.addRecompensa(recompensa4));
    }

    @Test
    public void testObtenerRecompensasPorUsuario() {
        gestor.addRecompensaUsuario(usuario, recompensa1);
        ArrayList<Recompensa> obtenidas = gestor.obtenerRecompensasUsuario(usuario);
        assertEquals(1, obtenidas.size());
        assertTrue(obtenidas.contains(recompensa1));
    }

    @Test
    public void testObtenerRecompensasUsuarioNulo() {
        gestor.addRecompensa(recompensa1);
        assertThrows(IllegalArgumentException.class, () -> {
            gestor.obtenerRecompensasUsuario(null);
        });
    }


    @Test
    public void testAddrRecompensaUsuario_InsuficientePuntuacion() {
        assertThrows(IllegalStateException.class, () -> gestor.addRecompensaUsuario(usuario, recompensa2));
    }

    @Test
    public void testAddRecompensaUsuario_Inactiva() {
        Recompensa recompensa3 = new Recompensa("R1", "Recompensa Uno", 100, false);
        assertThrows(IllegalStateException.class, () -> gestor.addRecompensaUsuario(usuario, recompensa3));
    }

    @Test
    public void testAddRecompensaUsuarioCuandoElUsuarioEsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            gestor.addRecompensaUsuario(null, recompensa1);
        });
    }


    @Test
    public void testAddRecompensaUsuarioCuandoLaRecompensaEsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            gestor.addRecompensaUsuario(usuario, null);
        });
    }


    @Test
    public void testDesactivarRecompensa() {
        gestor.addRecompensa(recompensa1);
        gestor.desactivarRecompensa("R1");
        assertFalse(recompensa1.getEstado());
    }

    @Test
    public void testDesactivarRecompensaNoExiste() {
        assertThrows(IllegalStateException.class, () -> gestor.desactivarRecompensa("R3"));
    }

    @Test
    public void testActivarRecompensa() {
        gestor.addRecompensa(recompensa2);
        gestor.activarRecompensa("R2");
        assertTrue(recompensa2.getEstado());
    }

    @Test
    public void testDesactivarRecompensaNula() {
        assertThrows(IllegalArgumentException.class, () -> gestor.desactivarRecompensa(null));
    }

    @Test
    public void testActivarRecompensaNula() {
        assertThrows(IllegalArgumentException.class, () -> gestor.activarRecompensa(null));
    }


    @Test
    public void testActivarRecompensaNoExiste() {
        assertThrows(IllegalStateException.class, () -> gestor.activarRecompensa("R3"));
    }

    
    @Test
    public void testAddRecompensaCuandoEsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            gestor.addRecompensa(null);
        });
    }

    @Test
    public void testAddRecompensaDuplicada() {
        gestor.addRecompensa(recompensa1);
        assertThrows(IllegalArgumentException.class, () -> {
            gestor.addRecompensa(recompensa1);
        });
    }

    


    @Test
    public void testGetRecompensaCuandoLaRecompensaEsNull() {
        gestor.addRecompensa(recompensa1);
        assertThrows(IllegalArgumentException.class, () -> {
            gestor.getRecompensa(null);
        });
    }


    @Test
    public void testGetRecompensaCuandoLaRecompensaNoExiste() {
        gestor.addRecompensa(recompensa1);
        assertThrows(IllegalStateException.class, () -> {
            gestor.getRecompensa(recompensa2.getId());
        });
    }

}


    


    
