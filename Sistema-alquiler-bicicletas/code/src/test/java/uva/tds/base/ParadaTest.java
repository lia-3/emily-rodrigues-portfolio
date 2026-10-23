package uva.tds.base;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


/**
 * Implementación clase que prueba los tests de la clase parada
 * @author Ainhoa Carbajo Orgaz
 * @author Emily Rodrigues
 */
public class ParadaTest {
    Bicicleta normal;
    Bicicleta electrica;
    Bicicleta biciOcupada;
    String identificadorBici;
    String identificadorParada;
    String direccion;
    String idElectrica;
    int nivelBateria;
    double longitud;
    double latitud;


    ArrayList <Bicicleta> bicicletas1;
    ArrayList <Bicicleta> bicicletasDisponibles;
    ArrayList <Bicicleta> listaVacia;

    Parada paradaVacia;

    static final int NIVEL_BATERIA=10;
    static final int HUECOS_APARCAMIENTO =3;
    static final double LATITUD_MINIMA = -90.0;
    static final double LATITUD_MAXIMA = 90.0;
    static final double LONGITUD_MINIMA = -180.0;
    static final double LONGITUD_MAXIMA = 180.0;

    @BeforeEach
    void startUp(){
        identificadorBici = "1111";
        identificadorParada="i";
        nivelBateria = 10;
        latitud=50.4;
        longitud=100.6;
        direccion="C/Manuel Azaña N7 1A";
        idElectrica = "elect";

        normal= new Bicicleta(identificadorBici);
        electrica = new Bicicleta(idElectrica, nivelBateria);
        biciOcupada = new Bicicleta("ocupa", EstadoBicicleta.OCUPADA);
     

        bicicletas1= new ArrayList <Bicicleta>();
        bicicletas1.add(normal);
        bicicletas1.add(electrica);
        
        

        bicicletasDisponibles= new ArrayList <Bicicleta>();
        bicicletasDisponibles.add(normal);
        bicicletasDisponibles.add(electrica);
       
        listaVacia = new ArrayList<Bicicleta>(0);
        paradaVacia = new Parada ("i",LATITUD_MINIMA,LONGITUD_MINIMA,"C", listaVacia, HUECOS_APARCAMIENTO,true);
    }

    @Test
    public void testCreacionParadaLimiteInferior(){
        
        Parada parada = new Parada (identificadorParada,LATITUD_MINIMA,LONGITUD_MINIMA,"C", bicicletas1,HUECOS_APARCAMIENTO,true);
        assertEquals(identificadorParada, parada.getIdentificador());
        assertEquals(LATITUD_MINIMA,parada.getLatitud());
        assertEquals(LONGITUD_MINIMA, parada.getLongitud());
        assertEquals("C",parada.getDireccion());
        assertEquals(2,parada.getNumeroBicicletasDisponibles());
        assertEquals(bicicletasDisponibles,parada.getListaBicicletasDisponibles());
        assertEquals(HUECOS_APARCAMIENTO,parada.getAparcamientos());
        assertEquals(1,parada.getAparcamientosDisponibles());
        assertEquals(true,parada.isActiva());
        assertFalse(parada.isLlena());
    }

    @Test
    public void testCreacionParadaCuandoLaListaDeBicisTieneDosBicisIguales() {
        bicicletas1.add(electrica);
        assertThrows(IllegalStateException.class, () -> { 
            new Parada(identificadorParada,LATITUD_MINIMA,LONGITUD_MINIMA,"C", bicicletas1,HUECOS_APARCAMIENTO,true);
        });
    }

    @Test
    public void testCreacionParadaLimiteSuperior(){
        electrica.setEstadoOcupada();
        bicicletasDisponibles.remove(electrica);
        Parada parada = new Parada (identificadorParada,LATITUD_MAXIMA,LONGITUD_MAXIMA,direccion,bicicletas1,HUECOS_APARCAMIENTO,true);
        assertEquals(identificadorParada, parada.getIdentificador());
        assertEquals(LATITUD_MAXIMA,parada.getLatitud());
        assertEquals(LONGITUD_MAXIMA, parada.getLongitud());
        assertEquals(direccion,parada.getDireccion());
        assertEquals(1,parada.getNumeroBicicletasDisponibles());
        assertEquals(bicicletasDisponibles,parada.getListaBicicletasDisponibles());
        assertEquals(HUECOS_APARCAMIENTO,parada.getAparcamientos());
        assertEquals(2,parada.getAparcamientosDisponibles());
        assertEquals(true,parada.isActiva());
    }

    @Test
    public void testCreacionParadaIdMenorLimiteInferior(){
       assertThrows(IllegalArgumentException.class, () -> {
            new Parada ("",LATITUD_MAXIMA,LONGITUD_MAXIMA,direccion,bicicletas1,HUECOS_APARCAMIENTO,true);
        });

    }

    @Test
    public void testCreacionParadaLatitudMenorLimiteInferior(){
       assertThrows(IllegalArgumentException.class, () -> {
            new Parada (identificadorParada,-91.0,180,direccion,bicicletas1,HUECOS_APARCAMIENTO,true);
        });

    }

    @Test
    public void testCreacionParadaLatitudMayorLimiteSuperior(){
       assertThrows(IllegalArgumentException.class, () -> {
            new Parada (identificadorParada,90.0,181,direccion,bicicletas1,HUECOS_APARCAMIENTO,true);
        });

    }

    @Test
    public void testCreacionParadaLongitudMenorLimiteInferior(){
       assertThrows(IllegalArgumentException.class, () -> {
            new Parada (identificadorParada,90.0,-180.1,direccion,bicicletas1,HUECOS_APARCAMIENTO,true);
        });

    }

    @Test
    public void testCreacionParadaLongitudMayorLimiteSuperior(){
       assertThrows(IllegalArgumentException.class, () -> {
            new Parada (identificadorParada,91.0,180.1,direccion,bicicletas1,HUECOS_APARCAMIENTO,true);
        });

    }



    @Test
    public void testCreacionParadaDireccionMenorLimiteInferior(){
       assertThrows(IllegalArgumentException.class, () -> {
            new Parada (identificadorParada,LATITUD_MAXIMA,LONGITUD_MAXIMA,"",bicicletas1,HUECOS_APARCAMIENTO,true);
        });

    }

    @Test
    public void testCreacionParadaDireccionMayorLimiteSuperior(){
       assertThrows(IllegalArgumentException.class, () -> {
            new Parada (identificadorParada,LATITUD_MAXIMA,LONGITUD_MAXIMA,"C/ Manuel Azaña N7 Piso 1 A",bicicletas1,HUECOS_APARCAMIENTO,true);
        });

    }

    @Test
    public void testCreacionParadaMasBiciclectasHuecos(){
       assertThrows(IllegalStateException.class, () -> {
            new Parada (identificadorParada,LATITUD_MAXIMA,LONGITUD_MAXIMA,direccion,bicicletas1,1,true);
        });

    }

    @Test
    public void testCreacionParadaBicicletasRepetidas(){
        bicicletas1.add(normal);
       assertThrows(IllegalStateException.class, () -> {
            new Parada (identificadorParada,LATITUD_MAXIMA,LONGITUD_MAXIMA,direccion,bicicletas1,1,true);
        });

    }


    @Test
    public void testCreacionParadaConIdNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Parada(null, LATITUD_MAXIMA, LONGITUD_MAXIMA, direccion, bicicletas1, 1, true);
        });
    }


    @Test
    public void testCreacionParadaConDireccionNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Parada(identificadorParada, LATITUD_MAXIMA, LONGITUD_MAXIMA, null, bicicletas1, 1, true);
        });
    }


    @Test
    public void testCreacionParadaConListaBicicletasNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Parada(identificadorParada, LATITUD_MAXIMA, LONGITUD_MAXIMA, direccion,  null, 1, true);
        });
    }


    @Test
    public void testCreacionParadaConAparcamientosJustoPorDebajoDelLimiteInferior() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Parada(identificadorParada, LATITUD_MAXIMA, LONGITUD_MAXIMA, direccion,  bicicletas1, -1, true);
        });
    }


    @Test
    public void testCreacionParadaConAparcamientosJustoPorIgualAlLimiteInferior() {
        paradaVacia = new Parada(identificadorBici, LATITUD_MAXIMA, LONGITUD_MAXIMA, direccion, listaVacia, 
                                    0, false);
        assertEquals(paradaVacia.getAparcamientos(), 0);
    }

    @Test
    public void testAgregarBici() {
        electrica.setEstadoOcupada();
        paradaVacia.agregaBicicleta(electrica);
        ArrayList<Bicicleta> listaBici = new ArrayList<Bicicleta>();
        listaBici.add(electrica.clone());
        assertArrayEquals(paradaVacia.getListaBicicletasDisponibles().toArray(), listaBici.toArray());
        assertTrue(paradaVacia.getListaBicicletasDisponibles().get(0).isDisponible());
    }


    @Test
    public void testAgregarBiciNull() {
        assertThrows(IllegalArgumentException.class, () -> {paradaVacia.agregaBicicleta(null);});
    }


    @Test
    public void testAgregarBiciYaAgregada() {
        paradaVacia.agregaBicicleta(electrica);
        assertThrows(IllegalArgumentException.class, () -> {paradaVacia.agregaBicicleta(electrica.clone());});
    }

    @Test
    public void testAgregarBiciParadaLlena() {
        Parada parada = new Parada (identificadorParada,LATITUD_MINIMA,LONGITUD_MINIMA,"C", bicicletas1,HUECOS_APARCAMIENTO,true);
        assertThrows(IllegalArgumentException.class, () -> {
            parada.agregaBicicleta(electrica.clone());});
    }


    @Test
    public void testIsBicicletaEnParadaCuandoEstaEnLaParada() {
        paradaVacia.agregaBicicleta(electrica);
        assertTrue(paradaVacia.isBicicletaEnParada(electrica.getIdentificador()));
    }


    @Test
    public void testIsBicicletaEnParadaCuandoNoEstaEnLaParada() {
        assertFalse(paradaVacia.isBicicletaEnParada(electrica.getIdentificador()));
    }

    @Test
    public void testIsBicicletaEnParadaCuandoElIdBciEsNull() {
        paradaVacia.agregaBicicleta(biciOcupada);
        assertThrows(IllegalArgumentException.class, () -> {paradaVacia.isBicicletaEnParada(null);});
    }


    @Test
    public void testEliminarBiciCuandoEstaEnLaParada() {
        paradaVacia.agregaBicicleta(normal);
        paradaVacia.eliminaBicicleta(identificadorBici);
        assertEquals(paradaVacia.getListaBicicletasDisponibles().size(), 0);
    }

    @Test
    public void testEliminarBiciCuandoElIdBiciEsNull() {
        paradaVacia.agregaBicicleta(biciOcupada);
        assertThrows(IllegalArgumentException.class, () -> {paradaVacia.eliminaBicicleta(null);});
    }

    @Test
    public void testEliminarBiciCuandoNoEstaLaBici() {
        paradaVacia.agregaBicicleta(biciOcupada);
        assertThrows(IllegalStateException.class, () -> {paradaVacia.eliminaBicicleta("2222");});
    }


    @Test
    public void testSetBiciEstadoDisponibleConIdentificadorValido() {
        paradaVacia.agregaBicicleta(biciOcupada);
    ;
        paradaVacia.setBicicletaEstadoDisponible("ocupa");
        EstadoBicicleta estadoBicicleta = paradaVacia.getListaBicicletasDisponibles().get(0).getEstado();
        assertEquals(estadoBicicleta, EstadoBicicleta.DISPONIBLE);
    }


    @Test
    public void testSetBiciEstadoReservadaConIdentificadorValido() {
        paradaVacia.agregaBicicleta(electrica);
        paradaVacia.setBicicletaEstadoReservada(idElectrica);
        EstadoBicicleta estadoBicicleta = paradaVacia.getListaBicicletas().get(0).getEstado();
        assertEquals(estadoBicicleta, EstadoBicicleta.RESERVADA);
    }


    @Test
    public void testSetBiciEstadoBloqueadaConIdentificadorValido() {
        paradaVacia.agregaBicicleta(electrica);
        paradaVacia.setBicicletaEstadoBloqueada(idElectrica);
        EstadoBicicleta estadoBicicleta = paradaVacia.getListaBicicletas().get(0).getEstado();
        assertEquals(estadoBicicleta, EstadoBicicleta.BLOQUEADA);
    }


    @Test
    public void testGetBicicletaStringNoNulo() {
        paradaVacia.agregaBicicleta(electrica);
        assertEquals(electrica, paradaVacia.getBicicleta(idElectrica));
        assertNotSame(electrica, paradaVacia.getBicicleta(idElectrica));
    }


    @Test
    public void testGetBicicletaStringNulo() {
        paradaVacia.agregaBicicleta(electrica);
        assertEquals(electrica, paradaVacia.getBicicleta(idElectrica));
        assertThrows(IllegalArgumentException.class, () -> {
            paradaVacia.getBicicleta(null);});
    }


    @Test
    public void testSetIdentificadorCuandoIdEsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            paradaVacia.setIdentificador(null);
        });
    }


    @Test
    public void testSetDireccionCuandoEsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            paradaVacia.setDireccion(null);
        });
    }

    
    @Test
    public void testEqualsCuandoParadaEnNull() {
        assertFalse(paradaVacia.equals(null));
    }


    @Test
    public void testEqualsCuandoParadaEsOtroTipoDeObjeto() {
        assertFalse(paradaVacia.equals("otro"));
    }

}

