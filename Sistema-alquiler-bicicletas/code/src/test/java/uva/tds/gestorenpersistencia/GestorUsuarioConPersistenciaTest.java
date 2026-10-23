package uva.tds.gestorenpersistencia;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uva.tds.base.Usuario;
import uva.tds.gestorenaislamiento.GestorUsuarioEnAislamiento;
import uva.tds.implementaciones.UsuarioRepositorio;
import uva.tds.interfaces.IUsuarioRepositorio;

/**
 * Clase de tests que prueba la clase GestorUsuarioEnAislamiento con persistencia
 * mediante base de datos
 * @author Ainhoa Carbajo Orgaz
 */
public class GestorUsuarioConPersistenciaTest {

    private IUsuarioRepositorio usuarioRepositorio;
    private GestorUsuarioEnAislamiento sistema;
    private Usuario usuarioJuan;
    String nifJuan;
    String noRegistrado;

    @BeforeEach
    public void startUp() {
        usuarioRepositorio = new UsuarioRepositorio();
        sistema= new GestorUsuarioEnAislamiento(usuarioRepositorio);
        noRegistrado="87654321A";
        nifJuan="12345678Z";
        usuarioJuan = new Usuario("Juan",nifJuan , 10, true);
        ((UsuarioRepositorio) usuarioRepositorio).clearDatabase();
    }

    @Test
    public void testRegistrarUsuario() {         
        
        sistema.registrarUsuario(usuarioJuan);
        assertEquals("Juan", sistema.getUsuario(nifJuan).getNombre());
        assertEquals(usuarioJuan, sistema.getUsuario(nifJuan));
        
    }

    @Test
    public void testRegistrarUsuarioInactivo() {         
        usuarioJuan.setEstado(false);
        sistema.registrarUsuario(usuarioJuan);
        assertEquals("Juan", sistema.getUsuario(nifJuan).getNombre());
        assertEquals(usuarioJuan, sistema.getUsuario(nifJuan));
        assertTrue(sistema.getUsuario(nifJuan).isActivo());   
    }

    @Test
    public void testRegistrarUsuarioNulo() {     
        assertThrows(IllegalArgumentException.class, () -> {
			sistema.registrarUsuario(null);
		});    
  
        
    }

    @Test
    public void testRegistrarUsuarioMismoNif() {    
        sistema.registrarUsuario(usuarioJuan); 
        assertThrows(IllegalArgumentException.class, () -> {
            sistema.registrarUsuario(usuarioJuan);
		});    
  
        
    }

    @Test
    public void testObtenerUsuarioPorNifNull() {
       
        sistema.registrarUsuario(usuarioJuan);
        assertThrows(IllegalArgumentException.class, () -> sistema.getUsuario(null));
    }

    @Test
    public void testObtenerUsuarioPorNifConLongitudMenorDelLimiteInferior() {
        
        sistema.registrarUsuario(usuarioJuan);
        assertThrows(IllegalArgumentException.class, () -> sistema.getUsuario(""));
        
    }

    @Test
    public void testObtenerUsuarioPorNifConLongitudMayorDelLimiteSuperior() {
        
        sistema.registrarUsuario(usuarioJuan);
        assertThrows(IllegalArgumentException.class, () -> sistema.getUsuario("111111111111111"));
        
    }

    @Test
    public void testObtenerUsuarioNoRegistrado() {
        
        sistema.registrarUsuario(usuarioJuan);
        assertEquals(null, sistema.getUsuario(noRegistrado));
        
    }

    @Test
    public void testActualizarNombreUsuario() {
       
        sistema.registrarUsuario(usuarioJuan);
        usuarioJuan.setNombre("Carlos");
        sistema.actualizarUsuario(usuarioJuan);
        assertEquals("Carlos", usuarioJuan.getNombre());
        
    }

    @Test
    public void testActualizarUsuarioNulo() {
       
        assertThrows(IllegalArgumentException.class, () -> sistema.actualizarUsuario(null));
        
        
    }

    @Test
    public void testActualizarUsuarioNoRegistrado() {
       
        assertThrows(IllegalArgumentException.class, () -> sistema.actualizarUsuario(usuarioJuan));
        
        
    }

    @Test
    public void testEliminarUsuario() {         
        
        sistema.registrarUsuario(usuarioJuan);
        
        sistema.eliminarUsuario(nifJuan);
        assertEquals(null,sistema.getUsuario(nifJuan));
        
    }

    @Test
    public void testEliminarUsuarioNifNulo() {         
        assertThrows(IllegalArgumentException.class, () -> sistema.eliminarUsuario(null));
    }
        @Test
    public void testEliminarUsuarioNoRegistrado() {         
        assertThrows(IllegalArgumentException.class, () -> sistema.eliminarUsuario(noRegistrado));
    }
        
    
    @AfterEach
	void tearDown() {
		usuarioRepositorio.clearDatabase();
	}

}
