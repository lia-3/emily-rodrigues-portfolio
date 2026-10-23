package uva.tds.base;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


/**
 * Clase de tests que prueba la clase Reserva
 * @author Emily Rodrigues
 */
public class ReservaTest {

    String nif = "00000000T";
    String nombre = "M";
    int puntuacion = 10;
    Usuario usuario;
    Bicicleta biciElectrica;
    Bicicleta copiaBiciElectrica;
    Reserva reserva;
    LocalDateTime fechaHora = LocalDateTime.of(2024, 11, 6, 18, 16);

    @BeforeEach
    void startUp() {
        usuario = new Usuario(nombre, nif, puntuacion, true);
        biciElectrica = new Bicicleta("1111", 15, EstadoBicicleta.DISPONIBLE);
        copiaBiciElectrica = biciElectrica.clone();
        reserva = new Reserva(biciElectrica, usuario, fechaHora);
    }

    @Test
    public void testConstructorConTodosLosValoresValido() {
        reserva = new Reserva(biciElectrica, usuario, fechaHora);

        assertEquals(reserva.getUsuario().getNif(), nif);
        assertEquals(reserva.getBicicleta(), copiaBiciElectrica);
        assertEquals(reserva.getFechaHoraReserva(), 
            LocalDateTime.of(2024, 11, 6, 18, 16));
        assertTrue(usuario.getReservas().contains(reserva));
        assertTrue(biciElectrica.getReservas().contains(reserva));
    }


    @Test 
    public void testConstructorConUsuarioNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Reserva(biciElectrica, null, fechaHora);});
    }


    @Test 
    public void testConstructorConUsuarioNoActivo() {
        usuario = new Usuario(nombre, nif, puntuacion, false);
        assertThrows(IllegalStateException.class, () -> {
            new Reserva(biciElectrica, usuario, fechaHora);});
    }


    @Test 
    public void testConstructorConBicicletaNula() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Reserva(null, usuario, fechaHora);});
    }

    @Test 
    public void testConstructorConBicicletaReservada() {
        biciElectrica.setEstadoReservada();
        assertThrows(IllegalStateException.class, () -> {
            new Reserva(biciElectrica, usuario, fechaHora);});
    }


    @Test 
    public void testConstructorConBicicletaOcupada() {
        biciElectrica.setEstadoOcupada();
        assertThrows(IllegalStateException.class, () -> {
            new Reserva(biciElectrica, usuario, fechaHora);});
    }


    @Test
    public void testConstructorConFechaHoraReservaNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Reserva(biciElectrica, usuario, null);});
    }

    @Test
    public void testIsTiempoLimiteAlcanzadoConTodosLosValoresValidosCuandoAunNoSeHaAlcanzadoElTiempoLimite() {
        assertFalse(reserva.isTiempoLimiteAlcanzado(
            LocalDateTime.of(2024, 11, 6, 18, 36), 1));
    }


    @Test
    public void testIsTiempoLimiteAlcanzadoConTodosLosValoresValidosCuandoSeHaAlcanzadoElTiempoLimiteYHorasMaximasIgualAlLimteInferior() {
        assertTrue(reserva.isTiempoLimiteAlcanzado(
            LocalDateTime.of(2024, 11, 6, 19, 17), 0));
    }


    @Test
    public void testIsTiempoLimiteAlcanzadoCuandoFechaYHoraACompararEsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            reserva.isTiempoLimiteAlcanzado(null, 1);
        });
    }


    @Test
    public void testIsTiempoLimiteAlcanzadoCuandoHorasMaximasEsJustoMenorQueCero() {
        assertThrows(IllegalArgumentException.class, () -> {
            reserva.isTiempoLimiteAlcanzado(LocalDateTime.of(2024,11,16,15,41), -1);
        });
    }

}
