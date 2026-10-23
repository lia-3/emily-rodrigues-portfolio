package uva.tds.base;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/*
 * Clase de prueba de la clase Usuari
 * @author Marcos de Diego Martin
 */

public class UsuarioTest {
    private String nombreUsuario;
    private String nif;
    private int puntuacion;
    private Usuario usuario;
    private Reserva reserva;
    private Bicicleta bici;
    private Recompensa recompensa1;

      @BeforeEach
    public void startUp() {
        nombreUsuario="Marcos";
        nif="12345678Z";
        puntuacion=10;
        bici = new Bicicleta("bici1");
        usuario = new Usuario("M","00000000T",50, true);
        reserva= new Reserva (bici,usuario, LocalDateTime.of(2025, 05, 17, 12, 00));
        recompensa1 = new Recompensa("R1", "Recompensa Uno", 50, true);
    }
   
    @Test
    public void testLimiteInferior(){
        
        Usuario usuario = new Usuario("M","00000000T",0, true);

        assertEquals("M", usuario.getNombre());
        assertEquals("00000000T", usuario.getNif());
        assertEquals(0, usuario.getPuntuacion());
        assertEquals(true, usuario.isActivo());
        assertTrue(usuario.getRecompensas().isEmpty());

    }

    @Test
    public void testLimiteSuperior(){
        Usuario usuario = new Usuario("Xochipitzahuatl","99999999R",2147483647, true);

        assertEquals("Xochipitzahuatl", usuario.getNombre());
        assertEquals("99999999R", usuario.getNif());
        assertEquals(2147483647, usuario.getPuntuacion());
        assertEquals(true, usuario.isActivo());
        assertTrue(usuario.getRecompensas().isEmpty());
    }

    @Test
    public void testNombreMenorLimiteInfeior() {
        assertThrows(IllegalArgumentException.class, () -> {new Usuario("", nif, puntuacion, true); });
    }

    @Test
    public void testNombreMayorLimiteSuperior() {
        assertThrows(IllegalArgumentException.class, () -> {new Usuario("Quecholliquetzal", nif, puntuacion, true); });
    }

    @Test
    public void testPuntuacionMenorLimiteInferior() {
        assertThrows(IllegalArgumentException.class, () -> {new Usuario(nombreUsuario, nif, -1, true); });
    }


    @Test
    public void testNifMenorLimiteInferior() {
        assertThrows(IllegalArgumentException.class, () -> {new Usuario(nombreUsuario, "12345V", puntuacion, true); });
    }

    @Test
    public void testNifMayorLimiteSuperior() {
        assertThrows(IllegalArgumentException.class, () -> {new Usuario(nombreUsuario, "1234567L", puntuacion, true); });
    }

    @Test
    public void testNifLetraMenorLimiteInferior() {
        assertThrows(IllegalArgumentException.class, () -> {new Usuario(nombreUsuario, "12345678J", puntuacion, true); });
    }

    @Test
    public void testNifLetraMayorLimiteSuperior() {
        assertThrows(IllegalArgumentException.class, () -> {new Usuario(nombreUsuario, "12345678S", puntuacion, true); });
    }

    @Test
    public void testConstructoCuandoNombreEsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Usuario(null, "72909678T", 10, true);
        });
    }

    @Test
    public void testConstructoCuandoNifEsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Usuario("tds", null, 10, true);
        });
    }

    @Test
    public void testAddRecompensa(){
        
        
        usuario.addRecompensa(recompensa1);
        assertTrue(usuario.getRecompensas().contains(recompensa1));
        assertEquals(usuario.getPuntuacion(),0);
    }

    @Test
    public void testAddAlquiler(){
        
        
        Alquiler alquiler= new Alquiler (bici,usuario);
        assertTrue(usuario.getAlquileres().contains(alquiler));
       
    }

    @Test
    public void testAddReresrva(){
        assertTrue(usuario.getReservas().contains(reserva));
       
    }



    @Test
    public void testAddRecompensaDuplicada(){
       usuario.addRecompensa(recompensa1);
        assertThrows(IllegalArgumentException.class, () -> {
            usuario.addRecompensa(recompensa1);
        });
    }

    @Test
    public void testAddRecompensaNula(){
       

        assertThrows(IllegalArgumentException.class, () -> {
            usuario.addRecompensa(null);
        });
    }

    
    @Test
    public void testAddAlquilerNulo(){
        
        assertThrows(IllegalArgumentException.class, () -> {
            usuario.addAlquiler(null);
        });
       
    }

    @Test
    public void testAddRereservaNula(){
        
        assertThrows(IllegalArgumentException.class, () -> {
            usuario.addReserva(null);
        });
       
    }

    @Test
    public void testAddRecompensaPuntuacionInferior(){
        recompensa1.setPuntuacion(100);
        assertThrows(IllegalStateException.class, () -> {
            usuario.addRecompensa(recompensa1);
        });
    }

    @Test
    public void testAddRecompensaInactiva(){
        recompensa1.setEstado(false);
        assertThrows(IllegalArgumentException.class, () -> {
            usuario.addRecompensa(recompensa1);
        });
    }

    @Test
    public void testAlquilerDuplicado(){
       
        Alquiler alquiler= new Alquiler (bici,usuario);
        
        assertTrue(usuario.getAlquileres().contains(alquiler));
        assertThrows(IllegalArgumentException.class, () -> {
            usuario.addAlquiler(alquiler);
        });
       
       
       
    }

    @Test
    public void testAddRereservaDuplicada(){
    
        assertThrows(IllegalArgumentException.class, () -> {
            usuario.addReserva(reserva);
        });
       
    }

    @Test
    public void testEliminarReserva(){
        usuario.eliminarReserva(reserva.getIdentificador());
        assertFalse(usuario.getReservas().contains(reserva));

    }

    @Test
    public void testEliminarReservaIdNulo(){
       assertThrows(IllegalArgumentException.class, () -> {
            usuario.eliminarReserva(null);
        });

    }

    @Test
    public void testEqualsNulo(){
      
        assertFalse(usuario.equals(null));
    }

    @Test
    public void testEqualsOtraClase(){
      
        assertFalse(usuario.equals(bici));
    }
}
