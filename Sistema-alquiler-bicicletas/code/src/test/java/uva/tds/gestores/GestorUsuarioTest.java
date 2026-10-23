package uva.tds.gestores;
import uva.tds.base.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


/**
 * Clase de tests que prueba la clase GestorUsuario
 * 
 * @author Marcos de Diego Martin
 * @author Emily Rodrigues
 * @author Ainhoa Carbajo
 */
public class GestorUsuarioTest {

    public int recompensaCero = 0;
    public String nifJuan;
    public Usuario usuarioJuan;
    public GestorUsuario gestor;

    public Alquiler alquiler;
    public Bicicleta bici;

    @BeforeEach
    public void startUp() {
        nifJuan = "12345678Z";
        usuarioJuan = new Usuario("Juan", nifJuan, recompensaCero, true);
        gestor = new GestorUsuario();
        bici = new Bicicleta("bici1");
        alquiler= new Alquiler (bici,usuarioJuan);
    }


    @Test
    public void testRegistrarUsuario() {
        gestor.registrarUsuario(usuarioJuan);
        assertEquals(usuarioJuan, gestor.getUsuario(nifJuan));
    }

    @Test
    public void testRegistroUsuarioMismoNif() {
        Usuario usuario2 = new Usuario("Ana", nifJuan, recompensaCero, true);
        gestor.registrarUsuario(usuarioJuan);
        assertThrows(IllegalStateException.class, () -> gestor.registrarUsuario(usuario2));
    }

    @Test
    public void testRegistrarUsuarioNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            gestor.registrarUsuario(null);
        });
    }

    @Test
    public void testRegistrarUsuarioInactivo() {
        gestor.registrarUsuario(usuarioJuan);
        assertEquals(true, gestor.getUsuario(nifJuan).isActivo());
    }


    @Test
    public void testObtenerUsuarioPorNifNull() {
        gestor.registrarUsuario(usuarioJuan);
        assertThrows(IllegalArgumentException.class, () -> gestor.getUsuario(null));
    }

    @Test
    public void testObtenerUsuarioPorNifConLongitudMenorDelLimiteInferior() {
        gestor.registrarUsuario(usuarioJuan);
        assertThrows(IllegalStateException.class, () -> gestor.getUsuario(""));
    }


    @Test
    public void testObtenerUsuarioPorNifConLongitudMayorDelLimiteInferior() {
        gestor.registrarUsuario(usuarioJuan);
        assertThrows(IllegalStateException.class, () -> gestor.getUsuario("111111111111111"));
    }


    @Test
    public void testObtenerUsuarioPorNifDeUnUsuarioNoRegistrado() {
        gestor.registrarUsuario(usuarioJuan);
        assertThrows(IllegalStateException.class, () -> gestor.getUsuario("87654321A"));
    }


    @Test
    public void testActualizarNombreUsuario() {
        gestor.registrarUsuario(usuarioJuan);
        gestor.actualizarNombreUsuario(nifJuan, "Carlos");
        assertEquals("Carlos", gestor.getUsuario(nifJuan).getNombre());
    }

    @Test
    public void testActualizarNombreUsuarioNoRegistrado() {
        gestor.registrarUsuario(usuarioJuan);
        assertThrows(IllegalStateException.class, () -> gestor.actualizarNombreUsuario("87654321A","Juan"));
        
    }

    @Test
    public void testActualizarNombreUsuarioNifNull() {
        gestor.registrarUsuario(usuarioJuan);
        assertThrows(IllegalArgumentException.class, () -> gestor.actualizarNombreUsuario(null,"Juan"));
        
    }

    @Test
    public void testActualizarNombreUsuarioNombreNull() {
        gestor.registrarUsuario(usuarioJuan);
        assertThrows(IllegalArgumentException.class, () -> gestor.actualizarNombreUsuario(nifJuan,null));
        
    }


    @Test
    public void testDesactivarUsuario() {
        gestor.registrarUsuario(usuarioJuan);
        gestor.desactivarUsuario(nifJuan);
        assertFalse(gestor.getUsuario(nifJuan).isActivo());
    }

    @Test
    public void testDesactivarUsuarioNulo() {
        assertThrows(IllegalArgumentException.class, () -> gestor.desactivarUsuario(null));
    }

    @Test
    public void testDesactivarUsuarioNoRegistrado() {
        assertThrows(IllegalStateException.class, () -> gestor.desactivarUsuario("87654321A"));
    }


    @Test
    public void testActivarUsuario() {
        Usuario usuarioJuanDesactivado =  new Usuario("Juan", nifJuan, recompensaCero, false);
        gestor.registrarUsuario(usuarioJuanDesactivado);
        gestor.activarUsuario(nifJuan);
        assertTrue(gestor.getUsuario(nifJuan).isActivo());
    }

    @Test
    public void testActivarUsuarioNulo() {
        assertThrows(IllegalArgumentException.class, () -> gestor.activarUsuario(null));
    }

    @Test
    public void testActivarUsuarioNoRegistrado() {
        assertThrows(IllegalStateException.class, () -> gestor.activarUsuario("87654321A"));
    }


    @Test
    public void testAgregaRecompensasConValorPositivoLimiteInferior(){
        gestor.registrarUsuario(usuarioJuan);
        gestor.agregarRecompensas(nifJuan, recompensaCero);
        assertEquals(gestor.getUsuario(nifJuan).getPuntuacion(), recompensaCero);
    }

    @Test 
    public void testAgregaRecompensasConValorMenorAlLimiteInferior() {
        gestor.registrarUsuario(usuarioJuan);
        assertThrows(IllegalArgumentException.class, () -> gestor.agregarRecompensas(nifJuan, -1));
    }

    @Test 
    public void testAgregaRecompensasNifNoRegistrado() {
        gestor.registrarUsuario(usuarioJuan);
        assertThrows(IllegalStateException.class, () -> gestor.agregarRecompensas("87654321A", 1));
    }

    @Test 
    public void testAgregaRecompensasNifNulo() {
        gestor.registrarUsuario(usuarioJuan);
        assertThrows(IllegalArgumentException.class, () -> gestor.agregarRecompensas(null, 1));
    }

    @Test
    public void testGetAlquiler(){
        gestor.registrarUsuario(usuarioJuan);
        assertTrue(gestor.getUsuario(nifJuan).getAlquileres().contains(alquiler));
    }

    @Test
    public void testEliminaTodasLasRecompensas(){
        gestor.registrarUsuario(usuarioJuan);
        gestor.agregarRecompensas(nifJuan, 16);
        gestor.eliminarTodasLasRecompensas(nifJuan);
        assertEquals(gestor.getUsuario(nifJuan).getPuntuacion(), recompensaCero);
    }

    @Test 
    public void testEliminatRecompensasNifNoRegistrado() {
        gestor.registrarUsuario(usuarioJuan);
        assertThrows(IllegalStateException.class, () -> gestor.eliminarRecompensas("87654321A", 1));
    }

    @Test 
    public void testEliminarRecompensasNifNulo() {
        gestor.registrarUsuario(usuarioJuan);
        assertThrows(IllegalArgumentException.class, () -> gestor.eliminarRecompensas(null, 1));
    }
    

    @Test
    public void testEliminaUnNumeroDeRecompensasConValorPositivoCuandoElUsuarioTieneRecompensas() {
        usuarioJuan.setPuntuacion(16);
        gestor.registrarUsuario(usuarioJuan);
        gestor.eliminarRecompensas(nifJuan, 4);
        assertEquals(gestor.getUsuario(nifJuan).getPuntuacion(), 12);
    }


    @Test
    public void testEliminaUnNumeroDeRecompensasConValorLimiteInferiorCuandoElUsuarioTieneRecompensas() {
        usuarioJuan.setPuntuacion(16);
        gestor.registrarUsuario(usuarioJuan);
        gestor.eliminarRecompensas(nifJuan, recompensaCero);
        assertEquals(gestor.getUsuario(nifJuan).getPuntuacion(), 16);
    }


    @Test
    public void testEliminaRecompensasConValorNegativo() {
        gestor.registrarUsuario(usuarioJuan);
        assertThrows(IllegalArgumentException.class, () -> gestor.eliminarRecompensas(nifJuan, -4));
    }


    @Test
    public void testEliminaRecompensasConValorNegativoJustoDebajoDelLimiteInferior() {
        gestor.registrarUsuario(usuarioJuan);
        assertThrows(IllegalArgumentException.class, () -> gestor.eliminarRecompensas(nifJuan, -1));
    }


    @Test
    public void testEliminaRecompensasConValorSuperiorALaPuntuacionDelUsuario() {
        usuarioJuan.setPuntuacion(10);
        gestor.registrarUsuario(usuarioJuan);
        assertThrows(IllegalStateException.class, () -> gestor.eliminarRecompensas(nifJuan, 20));
    }


    @Test
    public void testEliminaRecompensasConValorIgualALaPuntuacionDelUsuario() {
        usuarioJuan.setPuntuacion(10);
        gestor.registrarUsuario(usuarioJuan);
        gestor.eliminarRecompensas(nifJuan, 10);
        assertEquals(gestor.getUsuario(nifJuan).getPuntuacion(), recompensaCero);
    }




}
