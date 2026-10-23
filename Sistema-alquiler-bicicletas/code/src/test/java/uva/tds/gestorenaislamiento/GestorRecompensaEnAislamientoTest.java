package uva.tds.gestorenaislamiento;
import uva.tds.interfaces.IRecompensaRepositorio;
import uva.tds.base.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;

import org.easymock.EasyMock;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Clase de prueba para la clase GestorRecompensaEnAislamiento.
 * Prueba las funcionalidades y validaciones de la clase Recompensa.
 * @author Marcos de Diego Martin
 * @author Emily Rodrigues
 * @author Ainhoa Carbajo Orgaz
 */
public class GestorRecompensaEnAislamientoTest {

     @TestSubject
    private GestorRecompensaEnAislamiento gestor;

    @Mock
    private IRecompensaRepositorio service;

    private Recompensa recompensa1;
    private Recompensa recompensa2;
    private Usuario usuario;


    @BeforeEach
    void setUp(){
        service = EasyMock.mock(IRecompensaRepositorio.class);
        gestor = new GestorRecompensaEnAislamiento(service);
        recompensa1 = new Recompensa("R1", "Recompensa Uno", 100, true);
        recompensa2 = new Recompensa("R2", "Recompensa Dos", 200, false);
        
        usuario = new Usuario("Juan Perez", "12345678Z", 150, true);
    }

    @Test
    void testAddRecompensa() {
        service.addRecompensa(recompensa2);
        EasyMock.expectLastCall().times(1);
        EasyMock.expect(service.getRecompensa("R2")).andReturn(recompensa2).times(1);
        EasyMock.replay(service);

        gestor.addRecompensa(recompensa2);
        assertEquals(recompensa2, gestor.getRecompensa("R2"));

        EasyMock.verify(service);

    }

    @Test
    void testAddRecompensaMismoId() {
        Recompensa recompensa3 = new Recompensa("R1", "Recompensa Uno", 100, true);

        service.addRecompensa(recompensa1);
        EasyMock.expectLastCall().times(1);
        service.addRecompensa(recompensa2);
        EasyMock.expectLastCall().times(1);
        service.addRecompensa(recompensa3);
        EasyMock.expectLastCall().andThrow(new IllegalArgumentException());
        EasyMock.replay(service);

      
        gestor.addRecompensa(recompensa1);
        gestor.addRecompensa(recompensa2);
        assertThrows(IllegalArgumentException.class, () -> gestor.addRecompensa(recompensa3));

        EasyMock.verify(service);

    }

    @Test
    void testObtenerRecompensasActivas() {
        ArrayList<Recompensa> recompensasEsperadas = new ArrayList<>();
        recompensasEsperadas.add(recompensa1);

        service.addRecompensa(recompensa1);
        EasyMock.expectLastCall().times(1);
        service.addRecompensa(recompensa2);
        EasyMock.expectLastCall().times(1);
        EasyMock.expect(service.obtenerRecompensasActivas()).andReturn(recompensasEsperadas).times(1);
        EasyMock.replay(service);

        gestor.addRecompensa(recompensa1);
        gestor.addRecompensa(recompensa2);
        
        ArrayList<Recompensa> activas = gestor.obtenerRecompensasActivas();
        assertTrue(activas.contains(recompensa1));
        assertFalse(activas.contains(recompensa2));
        EasyMock.verify(service);
    }

    @Test
    void testObtenerRecompensasUsuario() {
        ArrayList<Recompensa> recompensasEsperadas = new ArrayList<>();
        recompensasEsperadas.add(recompensa1);

        service.addRecompensaUsuario(usuario, "R1");
        EasyMock.expectLastCall().times(1);
        EasyMock.expect(service.obtenerRecompensasUsuario(usuario)).andReturn(recompensasEsperadas).times(1);
        EasyMock.replay(service);

        gestor.addRecompensaUsuario(usuario, "R1");
        ArrayList<Recompensa> obtenidas = gestor.obtenerRecompensasUsuario(usuario);
        assertEquals(1, obtenidas.size());
        assertTrue(obtenidas.contains(recompensa1));

        EasyMock.verify(service);
    }

    @Test
    void testActualizarRecompensa() {
        service.addRecompensa(recompensa2);
        EasyMock.expectLastCall().times(1);
        recompensa2.setEstado(true);
        service.actualizarRecompensa(recompensa2);
        EasyMock.expectLastCall().andAnswer(() -> {
            recompensa2.setEstado(true); // Cambia el estado manualmente
            return null;
        }).times(1);
        EasyMock.replay(service);

        gestor.addRecompensa(recompensa2);
        gestor.actualizarRecompensa(recompensa2);
        assertTrue(recompensa2.getEstado());
        EasyMock.verify(service);
        
    }

    @Test
    void testActualizarRecompensaNula() {
       
        service.actualizarRecompensa(null);
        EasyMock.expectLastCall().andThrow(new IllegalArgumentException()).times(1);
        EasyMock.replay(service);
    
        assertThrows(IllegalArgumentException.class, () -> gestor.actualizarRecompensa(null));
        EasyMock.verify(service);
    }

    @Test
    void testActualizarRecompensaNoExiste() {
       
        service.actualizarRecompensa(recompensa2);
        EasyMock.expectLastCall().andThrow(new IllegalArgumentException()).times(1);
        EasyMock.replay(service);
    
        assertThrows(IllegalArgumentException.class, () -> gestor.actualizarRecompensa(recompensa2));
        EasyMock.verify(service);
    }


    @Test
    void testAddRecompensaUsuarioInsuficientePuntuacion() {
        service.addRecompensaUsuario(usuario, "R2");
        EasyMock.expectLastCall().andThrow(new IllegalArgumentException()).times(1);
        EasyMock.replay(service);

        assertThrows(IllegalArgumentException.class, () -> gestor.addRecompensaUsuario(usuario, "R2"));
        EasyMock.verify(service);
        
    }

    @Test
    void testAddRecompensaUsuarioRecompensaInactiva() {
        Recompensa recompensa3 = new Recompensa("R3", "Recompensa Uno", 100, false);
        service.addRecompensaUsuario(usuario, "R3");
        EasyMock.expectLastCall().andThrow(new IllegalArgumentException()).times(1);
        EasyMock.replay(service);
       
        assertThrows(IllegalArgumentException.class, () -> gestor.addRecompensaUsuario(usuario, "R3"));
        
        EasyMock.verify(service);
        
    }

    

    

}
