package uva.tds.base;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Clase de prueba para la clase Recompensa.
 * Prueba las funcionalidades y validaciones de la clase Recompensa.
 * @author Marcos de Diego Martin
 * @author Emily Rodrigues
 */
public class RecompensaTest {
    
    @Test
    public void testConstructorValidoLimiteInferior() {
        Recompensa r = new Recompensa("R2", "P", 1, false);
        assertEquals("R2", r.getId());
        assertEquals("P", r.getNombre());
        assertEquals(1, r.getPuntuacion());
        assertFalse(r.getEstado());
    }

   
    @Test
    public void testConstructorValidoLimiteSuperior() {
        Recompensa r = new Recompensa("123456", "Premio Grande Bicicl", 200, false);
        assertEquals("123456", r.getId());
        assertEquals("Premio Grande Bicicl", r.getNombre());
        assertEquals(200, r.getPuntuacion());
        assertFalse(r.getEstado());
    }

 
    @Test
    public void testConstructorIdMenorLimiteInferior() {
        assertThrows(IllegalArgumentException.class, () -> new Recompensa("", "Nombre", 50, true));
    }

    
    @Test
    public void testConstructorIdMayorLimiteSuperior() {
       
        assertThrows(IllegalArgumentException.class, () -> new Recompensa("1234567", "Nombre", 50, true));
    }

    @Test
    public void testConstructorNombreMenorLimiteInferior() {
        assertThrows(IllegalArgumentException.class, () -> new Recompensa("123456", "", 50, true));
    }

    
    @Test
    public void testConstructorNombreMayorLimiteSuperior() {
       
        assertThrows(IllegalArgumentException.class, () -> new Recompensa("123456", "Premio Grande Bicicleta", 50, true));
    }
    

    @Test
    public void testConstructorPuntuacionMenorCero() {
        assertThrows(IllegalArgumentException.class, () -> new Recompensa("ID1", "Nombre", -10, true));
    }


    @Test
    public void testConstructorCuandoIdEsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Recompensa(null, "recompensa", 1, false);
        });
    }

    @Test
    public void testConstructorCuandoIdNombreEsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Recompensa("ERROR", null, 1, false);
        });
    }

    @Test
    public void testSetusuarioNulo() {
        Recompensa r = new Recompensa("R2", "P", 1, false);
        assertThrows(IllegalArgumentException.class, () -> {
            r.setUsuario(null);
        });
    }

    @Test
    public void testEqualsOtraClase(){
        Usuario usuario = new Usuario("M","00000000T",100, true);
        Recompensa r = new Recompensa("R2", "P", 1, false);
        assertFalse(r.equals(usuario));
    }
}
