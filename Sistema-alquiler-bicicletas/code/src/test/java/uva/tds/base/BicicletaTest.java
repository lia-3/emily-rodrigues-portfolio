package uva.tds.base;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


/**
 * Clase de tests que prueba la clase Bicicleta
 * @author Emily Rodrigues
 */
public class BicicletaTest {

    Bicicleta normal;
    Bicicleta electrica;
    String identificador;
    int nivelBateria;

    Bicicleta electricaDisponibleExplicito;
    Bicicleta electricaDisponibleImplicito;
    Bicicleta normalDisponibleExplicito;
    Bicicleta normalDisponibleImplicito;
    Bicicleta biciOcupada;
    Bicicleta biciReservada;
    Bicicleta biciBloqueada;

    static final int NIVEL_BATERIA_NIVEL_SUPERIOR = 100;
    static final int NIVEL_BATERIA_NIVEL_INFERIOR = 0;

    static final int NIVEL_BATERIA_ENCIMA_NIVEL_SUPERIOR = 101;
    static final int NIVEL_BATERIA_DEBAJO_NIVEL_INFERIOR = -1;


    @BeforeEach
    void startUp(){
        identificador = "1111";
        nivelBateria = 10;

        electricaDisponibleExplicito = new Bicicleta(identificador, nivelBateria, EstadoBicicleta.DISPONIBLE);
        electricaDisponibleImplicito = new Bicicleta(identificador, nivelBateria);
        normalDisponibleExplicito = new Bicicleta(identificador, EstadoBicicleta.DISPONIBLE);
        normalDisponibleImplicito = new Bicicleta(identificador);
        biciOcupada = new Bicicleta(identificador, EstadoBicicleta.OCUPADA);
        biciReservada = new Bicicleta(identificador, EstadoBicicleta.RESERVADA);
        biciBloqueada = new Bicicleta(identificador, EstadoBicicleta.BLOQUEADA);
    }


    @Test
    public void testCreacionBicicletaNormalValidaConLongitudIdentificadorConLimiteInferiorYDisponibleDeManeraImplicita(){
        normal = new Bicicleta("1");
        identificador = "1";
        assertEquals(normal.getIdentificador(), identificador);
        assertEquals(normal.getEstado(), EstadoBicicleta.DISPONIBLE);
    }


    @Test
    public void testCreacionBicicletaNormalValidaConEstadoBloqueado(){
        normal = new Bicicleta(identificador, EstadoBicicleta.BLOQUEADA);
        assertEquals(normal.getIdentificador(), identificador);
        assertEquals(normal.getEstado(), EstadoBicicleta.BLOQUEADA);
    }


    @Test
    public void testCreacionBicicletaNormalConEstadoVacio() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Bicicleta(identificador, null);});
    }


    @Test
    public void testCreacionBicicletaElectricaValidaConBateriaNivelSuperiorYDisponible(){
        nivelBateria = NIVEL_BATERIA_NIVEL_SUPERIOR;
        electrica = new Bicicleta(identificador, nivelBateria);
        comprobacionIdentificadorTipoNivelBateriaYQueNoSeaBiciNormalParaBicisElectricas();
        assertEquals(electrica.getEstado(), EstadoBicicleta.DISPONIBLE);
    }


    @Test
    public void testCreacionBicicletaValidaLimiteSuperiorBicicletaElectricaConBateriaNivelInferiorYReservada(){
        identificador = "111111";
        nivelBateria = NIVEL_BATERIA_NIVEL_INFERIOR;
        electrica = new Bicicleta(identificador, nivelBateria, EstadoBicicleta.RESERVADA);
        comprobacionIdentificadorTipoNivelBateriaYQueNoSeaBiciNormalParaBicisElectricas();
        assertEquals(electrica.getEstado(), EstadoBicicleta.RESERVADA);
    }


    @Test
    public void testCreacionBicicletaElectricaConValoresValidosIntermedios() {
        electrica = new Bicicleta(identificador, nivelBateria, EstadoBicicleta.OCUPADA);
        comprobacionIdentificadorTipoNivelBateriaYQueNoSeaBiciNormalParaBicisElectricas();
        assertEquals(electrica.getEstado(), EstadoBicicleta.OCUPADA);
    }


    @Test
    public void testCreacionBicicletaNormalConIdentificadorConCaracteresPorDebajoDelLimiteInferior() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Bicicleta("");});

    }


    @Test
    public void testCreacionBicicletaNormalConIdentificadorConCaracteresPorEncimaDelLimiteSuperior() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Bicicleta("1234567");});

    }


    @Test
    public void testCreacionBicicletaConIdentificadorConCaracteresMuyPorEncimaDelLimiteSuperior() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Bicicleta("123456789123456789123456789");});
    }


    @Test
    public void testCreacionBicicletaElectricaConEstadoVacio() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Bicicleta(identificador, nivelBateria, null);});
    }


    @Test
    public void testCreacionBicicletaElectricaConNivelDeBateriaPorDebajoDelNivelInferior() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Bicicleta(identificador, NIVEL_BATERIA_DEBAJO_NIVEL_INFERIOR);});
    }


    @Test
    public void testCreacionBicicletaElectricaConNivelDeBateriaPorEncimaDelNivelSuperior() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Bicicleta(identificador, NIVEL_BATERIA_ENCIMA_NIVEL_SUPERIOR);});
    }


    @Test
    public void testCreacionBicicletaElectricaConNivelDeBateriaBastantePorDebajoDelNivelInferior() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Bicicleta(identificador, -100);});
    }


    @Test
    public void testCreacionBicicletaElectricaConNivelDeBateriaBastantePorEncimaDelNivelSuperior() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Bicicleta(identificador, 10000);});
    }


    @Test 
    public void testCreacionBicicletaConIdentificadorNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Bicicleta(null);});
    }


    @Test
    public void testIsBicicletaNormalDeUnaBicicletaNormalConstructorConEstadoDisponibleImplicito() {
        assertTrue(normalDisponibleImplicito.isBicicletaNormal());
    }


    @Test
    public void testIsBicicletaNormalDeUnaBicicletaElectricaConEstadoDisponibleImplicito() {
        assertFalse(electricaDisponibleImplicito.isBicicletaNormal());
    }


    @Test
    public void testIsBicicletaNormalDeUnaBicicletaNormalConstructorConEstadoDisponibleExplicito() {
        assertTrue(normalDisponibleExplicito.isBicicletaNormal()); 
    }


    @Test
    public void testIsBicicletaNormalDeUnaBicicletaElectricaConEstadoDisponibleExplicito() {
        assertFalse(electricaDisponibleExplicito.isBicicletaNormal());
    }


    @Test
    public void testIsBicicletaElectricaDeUnaBicicletaNormalConstructorConEstadoDisponibleImplicito() {
        assertFalse(normalDisponibleImplicito.isBicicletaElectrica());
    }


    @Test
    public void testIsBicicletaElectricaDeUnaBicicletaNormalConstructorConEstadoDisponibleExplicito() {
        assertFalse(normalDisponibleExplicito.isBicicletaElectrica());
    }


    @Test
    public void testIsBicicletaElectricaDeUnaBicicletaElectricaConstructorConEstadoDisponibleImplicito() {
        assertTrue(electricaDisponibleImplicito.isBicicletaElectrica());
    }


    @Test
    public void testIsBicicletaElectricaDeUnaBicicletaElectricaConstructorConEstadoDisponibleExplicito() {
        assertTrue(electricaDisponibleExplicito.isBicicletaElectrica());
    }


    @Test
    public void testSetEstadoReservada() {
        normalDisponibleExplicito.setEstado(EstadoBicicleta.RESERVADA);
        assertEquals(normalDisponibleExplicito.getEstado(), EstadoBicicleta.RESERVADA);
    }


    @Test
    public void testSetEstadoOcupada() {
        normalDisponibleImplicito.setEstado(EstadoBicicleta.OCUPADA);
        assertEquals(normalDisponibleImplicito.getEstado(), EstadoBicicleta.OCUPADA);
    }


    @Test
    public void testSetEstadoBloqueada() {
        electricaDisponibleImplicito.setEstado(EstadoBicicleta.BLOQUEADA);
        assertEquals(electricaDisponibleImplicito.getEstado(), EstadoBicicleta.BLOQUEADA);
    }


    @Test
    public void testSetEstadoDisponible() {
        electrica = new Bicicleta(identificador, nivelBateria, EstadoBicicleta.BLOQUEADA);
        electrica.setEstado(EstadoBicicleta.DISPONIBLE);
        assertEquals(electricaDisponibleImplicito.getEstado(), EstadoBicicleta.DISPONIBLE);
    }


    @Test
    public void testSetEstadoNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Bicicleta(identificador, null);});
    }


    @Test
    public void testSetNivelBateriaAlLimiteInferior() {
        electricaDisponibleExplicito.setNivelBateria(NIVEL_BATERIA_NIVEL_INFERIOR);
        assertEquals(electricaDisponibleExplicito.getNivelBateria(), NIVEL_BATERIA_NIVEL_INFERIOR);
    }


    @Test
    public void testSetNivelBateriaAlLimiteSuperior() {
        electricaDisponibleExplicito.setNivelBateria(NIVEL_BATERIA_NIVEL_SUPERIOR);
        assertEquals(electricaDisponibleExplicito.getNivelBateria(), NIVEL_BATERIA_NIVEL_SUPERIOR);
    }
    

    @Test
    public void testSetNivelBateriaSuperiorAlLimiteSuperior() {
        assertThrows(IllegalArgumentException.class, () -> {
            electricaDisponibleExplicito.setNivelBateria(NIVEL_BATERIA_ENCIMA_NIVEL_SUPERIOR);});
    }


    @Test
    public void testSetNivelBateriaInferiorAlLimiteInferior() {
        assertThrows(IllegalArgumentException.class, () -> {
            electricaDisponibleExplicito.setNivelBateria(NIVEL_BATERIA_DEBAJO_NIVEL_INFERIOR);});
    }


    @Test
    public void testSetNivelBateriaAUnaBiciNormal() {
        assertThrows(IllegalStateException.class, () -> {
            normalDisponibleExplicito.setNivelBateria(nivelBateria);});
    }


    @Test
    public void testGetNivelBateriaAUnaBiciNormal() {
        assertThrows(IllegalStateException.class, () -> {
            normalDisponibleExplicito.getNivelBateria();});
    }


    @Test
    public void testSetEstadoDisponibleAOcupada() {
        normalDisponibleExplicito.setEstado(EstadoBicicleta.OCUPADA);
        assertEquals(normalDisponibleExplicito.getEstado(), EstadoBicicleta.OCUPADA);
    }


    @Test
    public void testSetEstadoDisponibleABloqueada() {
        normalDisponibleExplicito.setEstado(EstadoBicicleta.BLOQUEADA);
        assertEquals(normalDisponibleExplicito.getEstado(), EstadoBicicleta.BLOQUEADA);
    }


    @Test
    public void testSetEstadoDisponibleAReservada() {
        normalDisponibleExplicito.setEstado(EstadoBicicleta.RESERVADA);
        assertEquals(normalDisponibleExplicito.getEstado(), EstadoBicicleta.RESERVADA);
    }


    @Test
    public void testSetEstadoOcupadaADisponible() {
        biciOcupada.setEstado(EstadoBicicleta.DISPONIBLE);
        assertEquals(biciOcupada.getEstado(), EstadoBicicleta.DISPONIBLE);
    }


    @Test
    public void testSetEstadoOcupadaABloqueada() {
        biciOcupada.setEstado(EstadoBicicleta.BLOQUEADA);
        assertEquals(biciOcupada.getEstado(), EstadoBicicleta.BLOQUEADA);
    }


    @Test
    public void testSetEstadoOcupadaAReservada() {
        biciReservada.setEstado(EstadoBicicleta.OCUPADA);
        assertEquals(biciReservada.getEstado(), EstadoBicicleta.OCUPADA);
    }


    @Test
    public void testSetEstadoReservadaAOcupada() {
        biciReservada.setEstado(EstadoBicicleta.OCUPADA);
        assertEquals(biciReservada.getEstado(), EstadoBicicleta.OCUPADA);
    }


    @Test
    public void testSetEstadoReservadaABloqueada() {
        biciReservada.setEstado(EstadoBicicleta.BLOQUEADA);
        assertEquals(biciReservada.getEstado(), EstadoBicicleta.BLOQUEADA);
    }


    @Test
    public void testSetEstadoReservadaADisponible() {
        biciReservada.setEstado(EstadoBicicleta.DISPONIBLE);
        assertEquals(biciReservada.getEstado(), EstadoBicicleta.DISPONIBLE);
    }


    @Test
    public void testSetEstadoBloqueadaADisponible() {
        biciBloqueada.setEstado(EstadoBicicleta.DISPONIBLE);
        assertEquals(biciBloqueada.getEstado(), EstadoBicicleta.DISPONIBLE);
    }


    @Test
    public void testSetEstadoBloqueadaAReservada() {
        biciBloqueada.setEstado(EstadoBicicleta.RESERVADA);
        assertEquals(biciBloqueada.getEstado(), EstadoBicicleta.RESERVADA);
    }


    @Test
    public void testSetEstadoBloqueadaAOcupada() {
        biciBloqueada.setEstado(EstadoBicicleta.OCUPADA);
        assertTrue(biciBloqueada.isOcupada());
    }


    @Test
    public void testIsDisponibleCuandoEstaDisponible(){
        assertTrue(normalDisponibleExplicito.isDisponible());
    }


    @Test
    public void testIsDisponibleCuandoEstaOcupada(){
        assertFalse(biciOcupada.isDisponible());
    }


    @Test
    public void testIsDisponibleCuandoEstaReservada(){
        assertFalse(biciReservada.isDisponible());
    }


    @Test
    public void testIsDisponibleCuandoEstaBloqueada(){
        assertFalse(biciBloqueada.isDisponible());
    }


    @Test
    public void testIsOcupadaCuandoEstaOcupada(){
        assertTrue(biciOcupada.isOcupada());
    }


    @Test
    public void testIsOcupadaCuandoEstaDisponible(){
        assertFalse(normalDisponibleExplicito.isOcupada());
    }


    @Test
    public void testIsOcupadaCuandoEstaReservada(){
        assertFalse(biciReservada.isOcupada());
    }


    @Test
    public void testIsOcupadaCuandoEstaBloqueada(){
        assertFalse(biciBloqueada.isOcupada());
    }


    @Test
    public void testIsReservadaCuandoEstaReservada(){
        assertTrue(biciReservada.isReservada());
    }


    @Test
    public void testIsReservadaCuandoEstaDisponible(){
        assertFalse(normalDisponibleExplicito.isReservada());
    }


    @Test
    public void testIsReservadaCuandoEstaOcupada(){
        assertFalse(biciOcupada.isReservada());
    }


    @Test
    public void testIsReservadaCuandoEstaBloqueada(){
        assertFalse(biciBloqueada.isReservada());
    }


    @Test
    public void testIsBloqueadaCuandoEstaBloqueada(){
        assertTrue(biciBloqueada.isBloqueada());
    }


    @Test
    public void testIsBloqueadaCuandoEstaDisponible(){
        assertFalse(normalDisponibleExplicito.isBloqueada());
    }


    @Test
    public void testIsBloqueadaCuandoEstaOcupada(){
        assertFalse(biciOcupada.isBloqueada());
    }


    @Test
    public void testIsBloqueadaCuandoEstaReservada(){
        assertFalse(biciReservada.isBloqueada());
    }


    @Test
    public void testEqualsCuandoDosBicisTienenMismoIdentificador() {
        assertTrue(normalDisponibleImplicito.equals(electricaDisponibleImplicito));
    }


    @Test
    public void testEqualsCuandoDosBicisTienenDistintoIdentificador() {
        Bicicleta otraBici = new Bicicleta("otra", EstadoBicicleta.DISPONIBLE);
        assertFalse(normalDisponibleImplicito.equals(otraBici));
    }


    @Test
    public void testEqualsCuandoLaOtraBiciEsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            normalDisponibleImplicito.equals(null);
        });
    }

    @Test
    public void testAddAlquilerNulo() {
        assertThrows(IllegalArgumentException.class, () -> {
            electricaDisponibleExplicito.addAlquiler(null);
        });
    }

    @Test
    public void testAddAlquilerDuplicado() {
        Usuario usuario = new Usuario ("Juan", "22883521Q",5,true);
        Alquiler  alquiler= new Alquiler (electricaDisponibleExplicito,usuario);
        assertThrows(IllegalArgumentException.class, () -> {
            electricaDisponibleExplicito.addAlquiler(alquiler);
        });
    }

    @Test
    public void testAddRereservaNula(){
        Usuario usuario = new Usuario("M","00000000T",100, true);
        Bicicleta bici = new Bicicleta("bici1");
        Reserva reserva= new Reserva (bici,usuario, LocalDateTime.of(2025, 05, 17, 12, 00));
        assertThrows(IllegalArgumentException.class, () -> {
            bici.addReserva(null);
        });
       
    }

    @Test
    public void testAddRereservaDuplicada(){
        Usuario usuario = new Usuario("M","00000000T",100, true);
        Bicicleta bici = new Bicicleta("bici1");
        Reserva reserva= new Reserva (bici,usuario, LocalDateTime.of(2025, 05, 17, 12, 00));
       
        assertThrows(IllegalArgumentException.class, () -> {
            usuario.addReserva(reserva);
        });
       
    }

    @Test
    public void testClone() {
        Bicicleta copia = electricaDisponibleExplicito.clone();
        assertNotSame(copia, electricaDisponibleExplicito);
        assertEquals(copia, electricaDisponibleExplicito);
    }


    /* Método de clase que no son test en si mismos, pero son llamados por otros tests */
    public void comprobacionIdentificadorTipoNivelBateriaYQueNoSeaBiciNormalParaBicisElectricas(){
        assertEquals(electrica.getIdentificador(), identificador);
        assertEquals(electrica.getNivelBateria(), nivelBateria);
    }
}