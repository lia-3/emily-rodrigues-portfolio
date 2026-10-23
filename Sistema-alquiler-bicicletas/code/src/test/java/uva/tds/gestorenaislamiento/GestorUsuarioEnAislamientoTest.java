package uva.tds.gestorenaislamiento;
import uva.tds.interfaces.IUsuarioRepositorio;
import uva.tds.base.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertFalse;

import org.easymock.EasyMock;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Clase de prueba para la clase GestorUsuarioEnAislamiento.
 * Prueba las funcionalidades y validaciones de la clase Usuario.
 * @author Marcos de Diego Martin
 * @author Ainhoa Carbajo
 */

public class GestorUsuarioEnAislamientoTest {
    @TestSubject
    private GestorUsuarioEnAislamiento gestor;

    @Mock
    private IUsuarioRepositorio usuarioRepositorio;

    int recompensaCero = 0;
    String nifJuan;
    public Usuario usuarioJuan;


    Parada paradaOrigen;
    Parada paradaDestino;
    Ruta ruta;
    Ruta ruta2;

    Bicicleta normal;
    ArrayList <Bicicleta> bicicletas1;

    String noRegistrado;


    @BeforeEach
    public void startUp() {
        usuarioRepositorio = EasyMock.mock(IUsuarioRepositorio.class);
        gestor= new GestorUsuarioEnAislamiento(usuarioRepositorio);
        nifJuan="12345678Z";
        usuarioJuan = new Usuario("Juan",nifJuan , 10, true);

        bicicletas1= new ArrayList <Bicicleta>();
        normal= new Bicicleta("1111");
        bicicletas1.add(normal);
        System.out.println("Set Up");

        paradaOrigen = new Parada ("o",-90.0,-180,"C", bicicletas1,3,true);
        paradaDestino = new Parada ("i",90.0,180,"C/Manuel Azaña N7 1A", bicicletas1,3,true);
        ruta = new Ruta("R1", usuarioJuan, paradaOrigen, paradaDestino);
        ruta2 = new Ruta("R2", usuarioJuan, paradaOrigen, paradaDestino);

        noRegistrado="87654321A";
        
    }


    @Test
    public void testRegistrarUsuario() {         
        usuarioRepositorio.registrarUsuario(usuarioJuan);
        EasyMock.expectLastCall().times(1);
        EasyMock.expect(usuarioRepositorio.getUsuario(nifJuan)).andReturn(usuarioJuan).times(1);
        EasyMock.replay(usuarioRepositorio);
        gestor.registrarUsuario(usuarioJuan);
        assertEquals(usuarioJuan, gestor.getUsuario(nifJuan));
        EasyMock.verify(usuarioRepositorio);
    }

    @Test
    public void testRegistroUsuarioMismoNif() {
        Usuario ana = new Usuario("Ana", nifJuan, recompensaCero, true);
        usuarioRepositorio.registrarUsuario(usuarioJuan);
        EasyMock.expectLastCall().times(1);
        usuarioRepositorio.registrarUsuario(ana);
        EasyMock.expectLastCall().andThrow( new IllegalArgumentException());
        EasyMock.replay(usuarioRepositorio);
        gestor.registrarUsuario(usuarioJuan);
        assertThrows(IllegalArgumentException.class, () -> gestor.registrarUsuario(ana));
        EasyMock.verify(usuarioRepositorio);
    }

    @Test
    public void testRegistroUsuarioNulo() {
        
        usuarioRepositorio.registrarUsuario(null);
        EasyMock.expectLastCall().andThrow( new IllegalArgumentException());
        EasyMock.replay(usuarioRepositorio);
        assertThrows(IllegalArgumentException.class, () -> gestor.registrarUsuario(null));
        EasyMock.verify(usuarioRepositorio);
    }

    @Test
    public void testRegistrarUsuarioInactivo() {
        usuarioJuan.setEstado(false);
        usuarioRepositorio.registrarUsuario(usuarioJuan);
        EasyMock.expectLastCall().andAnswer(() -> {usuarioJuan.setEstado(true);return null;});
        EasyMock.expect(usuarioRepositorio.getUsuario(nifJuan)).andAnswer(() -> usuarioJuan).times(2);
        EasyMock.replay(usuarioRepositorio);
        gestor.registrarUsuario(usuarioJuan);
        assertEquals(usuarioJuan, gestor.getUsuario(nifJuan)); 
        assertTrue(gestor.getUsuario(nifJuan).isActivo());    
        EasyMock.verify(usuarioRepositorio);
    }
    
    

    @Test
    public void testObtenerUsuarioPorNifNull() {
        usuarioRepositorio.registrarUsuario(usuarioJuan);
        EasyMock.expectLastCall().times(1);
        EasyMock.expect(usuarioRepositorio.getUsuario(null)).andThrow(new IllegalArgumentException());
        EasyMock.replay(usuarioRepositorio);
        gestor.registrarUsuario(usuarioJuan);
        assertThrows(IllegalArgumentException.class, () -> gestor.getUsuario(null));
        EasyMock.verify(usuarioRepositorio);
    }

    @Test
    public void testObtenerUsuarioPorNifConLongitudMenorDelLimiteInferior() {
        usuarioRepositorio.registrarUsuario(usuarioJuan);
        EasyMock.expectLastCall().times(1);
        EasyMock.expect(usuarioRepositorio.getUsuario("")).andThrow(new IllegalArgumentException());
        EasyMock.replay(usuarioRepositorio);
        gestor.registrarUsuario(usuarioJuan);
        assertThrows(IllegalArgumentException.class, () -> gestor.getUsuario(""));
        EasyMock.verify(usuarioRepositorio);
    }


    @Test
    public void testObtenerUsuarioPorNifConLongitudMayorDelLimiteInferior() {
        usuarioRepositorio.registrarUsuario(usuarioJuan);
        EasyMock.expectLastCall().times(1);
        EasyMock.expect(usuarioRepositorio.getUsuario("111111111111111")).andThrow(new IllegalArgumentException());
        EasyMock.replay(usuarioRepositorio);
        gestor.registrarUsuario(usuarioJuan);
        assertThrows(IllegalArgumentException.class, () -> gestor.getUsuario("111111111111111"));
        EasyMock.verify(usuarioRepositorio);
    }


    @Test
    public void testObtenerUsuarioPorNifDeUnUsuarionoRegistrado() {
        usuarioRepositorio.registrarUsuario(usuarioJuan);
        EasyMock.expectLastCall().times(1);
        EasyMock.expect(usuarioRepositorio.getUsuario(noRegistrado)).andReturn (null);
        EasyMock.replay(usuarioRepositorio);
        gestor.registrarUsuario(usuarioJuan);
        assertEquals(null,gestor.getUsuario(noRegistrado));
        EasyMock.verify(usuarioRepositorio);
    }


    @Test
    public void testActualizarNombreUsuario() {
        usuarioRepositorio.registrarUsuario(usuarioJuan);
        EasyMock.expect(usuarioRepositorio.getUsuario(nifJuan)).andReturn(usuarioJuan).times(1);

        usuarioRepositorio.actualizarUsuario(usuarioJuan);
        EasyMock.expectLastCall().times(1);
        EasyMock.replay(usuarioRepositorio);

        gestor.registrarUsuario(usuarioJuan);
        usuarioJuan.setNombre("Carlos");
        gestor.actualizarUsuario(usuarioJuan);

        assertEquals("Carlos", usuarioJuan.getNombre());
        EasyMock.verify(usuarioRepositorio);
    }

    @Test
    public void testActualizarUsuarionoRegistrado() {
        
        EasyMock.expect(usuarioRepositorio.getUsuario(nifJuan)).andThrow(new IllegalArgumentException());
        EasyMock.replay(usuarioRepositorio);

        usuarioJuan.setNombre("Carlos");
        assertThrows(IllegalArgumentException.class, () -> gestor.actualizarUsuario(usuarioJuan));

        EasyMock.verify(usuarioRepositorio);
    }


    @Test
    public void testDesactivarUsuario() {
        usuarioRepositorio.registrarUsuario(usuarioJuan);
        EasyMock.expectLastCall().times(1);
        EasyMock.expect(usuarioRepositorio.getUsuario(nifJuan)).andReturn(usuarioJuan).times(1);
        EasyMock.replay(usuarioRepositorio);
    
        gestor.registrarUsuario(usuarioJuan);
        gestor.desactivarUsuario(nifJuan);
        assertFalse(usuarioJuan.isActivo()); 
        EasyMock.verify(usuarioRepositorio);
    }

    @Test
    public void testDesactivarUsuarioNifNulo() {
        EasyMock.expect(usuarioRepositorio.getUsuario(null)).andThrow(new IllegalArgumentException());
        EasyMock.replay(usuarioRepositorio);
        assertThrows(IllegalArgumentException.class, () -> gestor.desactivarUsuario(null));
        EasyMock.verify(usuarioRepositorio);
    }

    @Test
    public void testDesactivarUsuarioNifnoRegistrado() {
        EasyMock.expect(usuarioRepositorio.getUsuario(noRegistrado)).andThrow(new IllegalArgumentException());
        EasyMock.replay(usuarioRepositorio);
        assertThrows(IllegalArgumentException.class, () -> gestor.desactivarUsuario(noRegistrado));
        EasyMock.verify(usuarioRepositorio);
    }
    
    
    @Test
    public void testActivarUsuario() {
        Usuario usuarioJuanDesactivado = new Usuario("Juan", nifJuan, recompensaCero, false);
        usuarioRepositorio.registrarUsuario(usuarioJuanDesactivado);
        EasyMock.expectLastCall().times(1);
        EasyMock.expect(usuarioRepositorio.getUsuario(nifJuan)).andReturn(usuarioJuanDesactivado).times(1);
        EasyMock.replay(usuarioRepositorio);
        gestor.registrarUsuario(usuarioJuanDesactivado);
        gestor.activarUsuario(nifJuan);
        assertTrue(usuarioJuanDesactivado.isActivo());
        EasyMock.verify(usuarioRepositorio);
    }

    @Test
    public void testActivarUsuarioNifNulo() {
        EasyMock.expect(usuarioRepositorio.getUsuario(null)).andThrow(new IllegalArgumentException());
        EasyMock.replay(usuarioRepositorio);
        assertThrows(IllegalArgumentException.class, () -> gestor.activarUsuario(null));
        EasyMock.verify(usuarioRepositorio);
    }

    
    @Test
    public void testActivarUsuarioNifnoRegistrado() {
        EasyMock.expect(usuarioRepositorio.getUsuario(noRegistrado)).andThrow(new IllegalArgumentException());
        EasyMock.replay(usuarioRepositorio);
        assertThrows(IllegalArgumentException.class, () -> gestor.activarUsuario(noRegistrado));
        EasyMock.verify(usuarioRepositorio);
    }
       

    @Test
    public void testAgregaRecompensasConValorPositivo() {
        EasyMock.expect(usuarioRepositorio.getUsuario(nifJuan)).andReturn(usuarioJuan).times(2);
        EasyMock.replay(usuarioRepositorio);
        gestor.agregarRecompensas(nifJuan, 16);
        assertEquals(26, usuarioJuan.getPuntuacion());
        EasyMock.verify(usuarioRepositorio);
    }

    @Test
    public void testAgregaRecompensasConValorPositivoEnElLimite() {
        usuarioRepositorio.registrarUsuario(usuarioJuan);
        EasyMock.expectLastCall().once();
        EasyMock.expect(usuarioRepositorio.getUsuario(nifJuan)).andReturn(usuarioJuan).times(2);
        EasyMock.replay(usuarioRepositorio);
        gestor.registrarUsuario(usuarioJuan);
        gestor.agregarRecompensas(nifJuan, recompensaCero);
        assertEquals(10, usuarioJuan.getPuntuacion());
        EasyMock.verify(usuarioRepositorio);
    }
    

    
    
    @Test 
    public void testAgregaRecompensasConValorMenorAlLimiteInferior() {
        usuarioJuan.setPuntuacion(0);
        usuarioRepositorio.registrarUsuario(usuarioJuan);
        EasyMock.expectLastCall().once();
        EasyMock.expect(usuarioRepositorio.getUsuario(nifJuan)).andReturn(usuarioJuan).anyTimes();
        EasyMock.replay(usuarioRepositorio);
        gestor.registrarUsuario(usuarioJuan);
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> gestor.agregarRecompensas(nifJuan, -1)
        );
        assertEquals("No se puede agregar una recompensa negativa", exception.getMessage());
        EasyMock.verify(usuarioRepositorio);
    }
    
    


    @Test
    public void testEliminaTodasLasRecompensas() {
        usuarioRepositorio.registrarUsuario(usuarioJuan);
        EasyMock.expectLastCall().once();
        EasyMock.expect(usuarioRepositorio.getUsuario(nifJuan)).andReturn(usuarioJuan).anyTimes();
        EasyMock.replay(usuarioRepositorio);
        gestor.registrarUsuario(usuarioJuan); 
        usuarioJuan.setPuntuacion(16);
        gestor.eliminarRecompensas(nifJuan);
        assertEquals(0, usuarioJuan.getPuntuacion());
        EasyMock.verify(usuarioRepositorio);
    }
    
    

    @Test
    public void testEliminaUnNumeroDeRecompensasConValorPositivoCuandoElUsuarioTieneRecompensas() {
        usuarioRepositorio.registrarUsuario(usuarioJuan);
        EasyMock.expectLastCall().once();
        EasyMock.expect(usuarioRepositorio.getUsuario(nifJuan)).andReturn(usuarioJuan).anyTimes();
        EasyMock.replay(usuarioRepositorio);
        gestor.registrarUsuario(usuarioJuan);
        usuarioJuan.setPuntuacion(16);
        gestor.eliminarRecompensas(nifJuan, 4);
        assertEquals(12, usuarioJuan.getPuntuacion());
        EasyMock.verify(usuarioRepositorio);
    }
    
    @Test
    public void testEliminaUnNumeroDeRecompensasConValorLimiteInferiorCuandoElUsuarioTieneRecompensas() {
        usuarioRepositorio.registrarUsuario(usuarioJuan);
        EasyMock.expectLastCall().once();
        EasyMock.expect(usuarioRepositorio.getUsuario(nifJuan)).andReturn(usuarioJuan).anyTimes();
        EasyMock.replay(usuarioRepositorio);
        gestor.registrarUsuario(usuarioJuan);
        usuarioJuan.setPuntuacion(16);
        gestor.eliminarRecompensas(nifJuan, recompensaCero);
        assertEquals(16, usuarioJuan.getPuntuacion());
        EasyMock.verify(usuarioRepositorio);
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
        usuarioRepositorio.registrarUsuario(usuarioJuan);
        EasyMock.expectLastCall().once();
        EasyMock.expect(usuarioRepositorio.getUsuario(nifJuan)).andReturn(usuarioJuan).anyTimes();
        EasyMock.replay(usuarioRepositorio);
        gestor.registrarUsuario(usuarioJuan);
        usuarioJuan.setPuntuacion(10);
        assertThrows(IllegalStateException.class, () -> gestor.eliminarRecompensas(nifJuan, 20));
        EasyMock.verify(usuarioRepositorio);
    }
    


    @Test
    public void testEliminaRecompensasConValorIgualALaPuntuacionDelUsuario() {
        usuarioRepositorio.registrarUsuario(usuarioJuan);
        EasyMock.expectLastCall().times(1);
        EasyMock.expect(usuarioRepositorio.getUsuario(nifJuan)).andReturn(usuarioJuan).anyTimes();
        EasyMock.replay(usuarioRepositorio);
        gestor.registrarUsuario(usuarioJuan);
        usuarioJuan.setPuntuacion(10);
        gestor.eliminarRecompensas(nifJuan, 10);
        assertEquals(0, usuarioJuan.getPuntuacion());
        EasyMock.verify(usuarioRepositorio);
    }
    

    @Test
    public void testModificarRecompensasMayor(){
        usuarioRepositorio.registrarUsuario(usuarioJuan);
        EasyMock.expectLastCall().times(1);
        EasyMock.expect(usuarioRepositorio.getUsuario(nifJuan)).andReturn(usuarioJuan).anyTimes();
        EasyMock.replay(usuarioRepositorio);
        gestor.registrarUsuario(usuarioJuan);
        gestor.modificarRecompensas(nifJuan,50);
        assertEquals(50, usuarioJuan.getPuntuacion());
        EasyMock.verify(usuarioRepositorio);
    }
    
}
