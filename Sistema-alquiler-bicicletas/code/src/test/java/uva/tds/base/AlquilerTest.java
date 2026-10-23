package uva.tds.base;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
/**
 * Clase de tests de la clase Alquiler
 * @author Ainhoa Carbajo Orgaz
 * @author Emily Rodrigues
 */
public class AlquilerTest {
    Usuario usuarioActivo;
    Usuario usuarioInactivo;
    Bicicleta normal;
    Bicicleta biciOcupada;
    Bicicleta biciBloqueada;
    String identificadorBici;
    Alquiler alquiler;

    @BeforeEach
    void startUp(){
        identificadorBici = "1111";
        normal= new Bicicleta(identificadorBici);
        biciOcupada= new Bicicleta(identificadorBici, EstadoBicicleta.OCUPADA);
        biciBloqueada= new Bicicleta(identificadorBici, EstadoBicicleta.BLOQUEADA);
        usuarioActivo = new Usuario ("Juan", "22883521Q",5,true);
        usuarioInactivo = new Usuario ("Juan", "54802723W",5,false);
        alquiler= new Alquiler (normal,usuarioActivo);
    }


    @Test
    public void testAlquilerBicileta(){
        LocalDate fechaFin = LocalDate.now().plusDays(10);
        LocalTime horaFin = LocalTime.now();
        alquiler.setFechaFin(fechaFin);
        alquiler.setHoraFin(horaFin);
        assertTrue(normal.isOcupada());
        assertEquals(alquiler.getFechaInicio(), LocalDate.now());
        assertEquals(alquiler.getHoraInicio().getHour(), LocalTime.now().getHour());
        assertEquals(alquiler.getUsuario(),usuarioActivo);
        assertEquals(alquiler.getBicicleta(),normal);
        assertEquals(alquiler.getFechaFin(), fechaFin);
        assertEquals(alquiler.getHoraFin(), horaFin);
        assertTrue(normal.getAlquileres().contains(alquiler));
        assertTrue(usuarioActivo.getAlquileres().contains(alquiler));
    }

    @Test
    public void testConstructorAlquilerCuandoBicicletaNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Alquiler(null, usuarioActivo);
        });
    }


    @Test
    public void testConstructorAlquilerCuandoUsuarioNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Alquiler(normal, null);
        });
    }

    @Test
    public void testAlquilerBiciletaOcupada(){
         assertThrows(IllegalStateException.class, () -> {
            new Alquiler (biciOcupada,usuarioActivo);
         });
    }


    @Test
    public void testAlquilerBiciletaBloqueada(){
         assertThrows(IllegalStateException.class, () -> {
            new Alquiler (biciBloqueada,usuarioActivo);
         });
    }

    
    @Test
    public void testAlquilerUsuarioInactivo(){
         assertThrows(IllegalStateException.class, () -> {
            new Alquiler (normal,usuarioInactivo);
         });

    }


    @Test
    public void testSetFechaFinCuandoEsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            alquiler.setFechaFin(null);
         });
    }


    @Test
    public void testSetFechaFinCuandoEsAnteriorAFechaInicio() {
        assertThrows(IllegalStateException.class, () -> {
            alquiler.setFechaFin(LocalDate.of(2020, 10, 07));
        });
    }

    @Test
    public void testSetHoraFinCuandoEsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            alquiler.setHoraFin(null);
         });
    }
}
