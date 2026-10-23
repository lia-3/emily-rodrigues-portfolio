package uva.tds.base;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


/**
 * Clase de tests de la clase Bloqueo
 * @author Ainhoa Carbajo Orgaz
 * @author Emily Rodrigues
 */
public class BloqueoTest {
    Bicicleta normal;
    Bicicleta biciOcupada;
    Bicicleta biciBloqueada;
    String identificadorBici;
    Bloqueo bloqueo;

    @BeforeEach
    void startUp(){
        identificadorBici = "1111";
        normal= new Bicicleta(identificadorBici);
        biciOcupada= new Bicicleta(identificadorBici, EstadoBicicleta.OCUPADA);
        biciBloqueada= new Bicicleta(identificadorBici, EstadoBicicleta.BLOQUEADA);
        bloqueo = new Bloqueo(normal);
    }


    @Test
    public void testBloqueoBicicleta(){
        LocalDate fechaFin = LocalDate.now();
        fechaFin = LocalDate.now().plusDays(10);
        LocalTime horaFin = LocalTime.of(20, 20);
        bloqueo.setFechaFin(fechaFin);
        bloqueo.setHoraFin(horaFin);
        assertTrue(bloqueo.getBicicleta().isBloqueada());
        assertEquals(bloqueo.getFechaInicio(), LocalDate.now());
        assertEquals(bloqueo.getHoraInicio().getHour(),LocalTime.now().getHour());
        assertEquals(bloqueo.getBicicleta(),normal);
        assertEquals(bloqueo.getFechaFin(), fechaFin);
        assertEquals(bloqueo.getHoraFin(), horaFin);
    }

    @Test
    public void testBloqueoCuandoBicicletaEsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Bloqueo(null);
        });
    }


    @Test
    public void testBloqueoBiciletaOcupada(){
        
         assertThrows(IllegalStateException.class, () -> {
            new Bloqueo (biciOcupada);
         });

    }

    @Test
    public void testBloqueoBiciletaBloqueada(){
        
         assertThrows(IllegalStateException.class, () -> {
            new Bloqueo (biciBloqueada);
         });

   }

   @Test
   public void testSetFechaFinCuandoEsAnteriorAFechaInicio() {
        assertThrows(IllegalStateException.class, () -> {
            bloqueo.setFechaFin(LocalDate.now().minusDays(10));
        });
   }


   @Test
   public void testSetFechaFinCuandoEsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            bloqueo.setFechaFin(null);
        });
   }


   @Test
   public void testSetHoraFinCuandoEsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            bloqueo.setHoraFin(null);
        });
   }
    
}