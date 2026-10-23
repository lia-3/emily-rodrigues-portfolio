package uva.poo.trayecto.combinado;

import static org.junit.Assert.*;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;

import uva.poo.puerto.*;
import uva.poo.trayecto.simple.*;
import es.uva.inf.poo.maps.GPSCoordinate;


/**
 * Prueba la clase PackCamionBarco
 * @author Marta Pérez
 * @author Emily Rodrigues
 */
public class PackCamionBarcoTest {

    private static final String ID_MUELLE_ORIGEN = "14";
    private static final String ID_MUELLE_DESTINO = "65";
    private static final String ID_PUERTO_ORIGEN = "ES-UNO";
    private static final String ID_PUERTO_DESTINO = "PD-DOS";

    private static final int NUMERO_PLAZAS = 30;
    private static final int CANTIDAD_MAX_CONTENEDORES = 50;
    private static final EstadoMuelle ESTADO_OPERATIVO = EstadoMuelle.OPERATIVO;
    
    private Muelle muelleOrigenValido;
    private Muelle muelleDestinoValido;
    private Muelle muelleOrigenValido2;
    private Puerto puertoOrigenValido;
    private Puerto puertoOrigenValido2;
    private Puerto puertoDestinoValido;

    private LocalDate fechaInicioValida;
    private LocalDate fechaFinValida;

    private TCamion tCamion;
    private TBarco tBarco;
    private TTren tTren;
    private PackCamionBarco packCamionBarco;


    public void inicializaValores() {
    	ArrayList<TipoTransporte> aceptaTodosLosTransportes = new ArrayList<>();
		aceptaTodosLosTransportes.add(TipoTransporte.BARCO);
		aceptaTodosLosTransportes.add(TipoTransporte.CAMION);
		aceptaTodosLosTransportes.add(TipoTransporte.TREN);
		
        muelleOrigenValido = new Muelle(ID_MUELLE_ORIGEN, new GPSCoordinate(80, 80), ESTADO_OPERATIVO, NUMERO_PLAZAS, CANTIDAD_MAX_CONTENEDORES, aceptaTodosLosTransportes);
        muelleOrigenValido2 = new Muelle("15", new GPSCoordinate(80, 80), ESTADO_OPERATIVO, NUMERO_PLAZAS, CANTIDAD_MAX_CONTENEDORES, aceptaTodosLosTransportes);
        muelleDestinoValido = new Muelle(ID_MUELLE_DESTINO, new GPSCoordinate(60, 60), ESTADO_OPERATIVO, NUMERO_PLAZAS + 1, CANTIDAD_MAX_CONTENEDORES - 1, aceptaTodosLosTransportes);
        puertoOrigenValido = new Puerto(ID_PUERTO_ORIGEN);
        puertoOrigenValido2 = new Puerto("ES-TRE");
        puertoDestinoValido = new Puerto(ID_PUERTO_DESTINO);

        puertoOrigenValido.agregaMuelle(muelleOrigenValido);
        puertoOrigenValido2.agregaMuelle(muelleOrigenValido2);
        puertoDestinoValido.agregaMuelle(muelleDestinoValido);

        fechaInicioValida = LocalDate.of(2024, 11, 8);
        fechaFinValida = LocalDate.of(2024, 11, 28);

        tCamion = new TCamion(muelleOrigenValido2, muelleDestinoValido, puertoOrigenValido2, puertoDestinoValido, fechaInicioValida, fechaFinValida);
        tBarco = new TBarco(muelleOrigenValido, muelleDestinoValido, puertoOrigenValido, puertoDestinoValido, fechaInicioValida, fechaFinValida);
        tTren = new TTren(muelleOrigenValido, muelleDestinoValido, puertoOrigenValido, puertoDestinoValido, fechaInicioValida, fechaFinValida);
       
        packCamionBarco = new PackCamionBarco(muelleOrigenValido, muelleDestinoValido, puertoOrigenValido, puertoDestinoValido, fechaInicioValida, fechaFinValida);

    }
    
    
    @Test
	public void testConstructorConValoresValidos() {
		inicializaValores();
		packCamionBarco.setDescuentoBarco(0);
		
		assertEquals(packCamionBarco.getPuertoOrigen().getIdentificador(), ID_PUERTO_ORIGEN);
		assertEquals(packCamionBarco.getPuertoDestino().getIdentificador(), ID_PUERTO_DESTINO);
		assertEquals(packCamionBarco.getMuelleOrigen().getIdMuelle(), ID_MUELLE_ORIGEN);
		assertEquals(packCamionBarco.getMuelleDestino().getIdMuelle(), ID_MUELLE_DESTINO);
		assertNotSame(packCamionBarco.getFechaInicio(), fechaInicioValida);
		assertEquals(packCamionBarco.getFechaInicio(), fechaInicioValida);
		assertNotSame(packCamionBarco.getFechaFin(), fechaFinValida);
		assertEquals(packCamionBarco.getFechaFin(), fechaFinValida);
		assertEquals(packCamionBarco.getDescuentoBasePorBarco(), 15, 0.01);
		assertEquals(packCamionBarco.getDescuentoPorBarco(), 0, 0.01);
	}

    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorMuelleOrigenNull() {
    	inicializaValores();
        new PackCamionBarco(null, muelleDestinoValido, puertoOrigenValido, puertoDestinoValido, fechaInicioValida, fechaFinValida);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorMuelleOrigenNoEnPuerto() {
    	inicializaValores();
        new PackCamionBarco(muelleOrigenValido2, muelleDestinoValido, puertoOrigenValido, puertoDestinoValido, fechaInicioValida, fechaFinValida);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorMuelleDestinoNull() {
    	inicializaValores();
        new PackCamionBarco(muelleOrigenValido, null, puertoOrigenValido, puertoDestinoValido, fechaInicioValida, fechaFinValida);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorMuelleDestinoNoEnPuerto() {
    	inicializaValores();
        new PackCamionBarco(muelleOrigenValido, muelleDestinoValido, puertoOrigenValido, puertoOrigenValido2, fechaInicioValida, fechaFinValida);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorPuertoDestinoIgualAOrigen() {
    	inicializaValores();
        new PackCamionBarco(muelleOrigenValido, muelleDestinoValido, puertoOrigenValido, puertoOrigenValido, fechaInicioValida, fechaFinValida);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorPuertoOrigenNull() {
    	inicializaValores();
        new PackCamionBarco(muelleOrigenValido, muelleDestinoValido, null, puertoDestinoValido, fechaInicioValida, fechaFinValida);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorPuertoDestinoNull() {
    	inicializaValores();
        new PackCamionBarco(muelleOrigenValido, muelleDestinoValido, puertoOrigenValido, null, fechaInicioValida, fechaFinValida);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testFechaInicioPosteriorAFechaFin() {
    	inicializaValores();
        LocalDate fechaInicioInvalida = LocalDate.of(2024, 12, 10); 
        fechaFinValida = LocalDate.of(2024, 11, 28); 

        new TCamion(muelleOrigenValido, muelleDestinoValido, puertoOrigenValido, puertoDestinoValido, fechaInicioInvalida, fechaFinValida);
    }
    
    
    @Test (expected = IllegalArgumentException.class)
	public void testConstructorConFechaInicioNull() {
		inicializaValores();
		new TBarco(muelleOrigenValido, muelleDestinoValido, puertoOrigenValido,
				puertoDestinoValido, null, fechaFinValida);
	}
	
	
	@Test (expected = IllegalArgumentException.class)
	public void testConstructorConFechaFinNull() {
		inicializaValores();
		new TCamion(muelleOrigenValido, muelleDestinoValido, puertoOrigenValido,
				puertoDestinoValido, fechaInicioValida, null);
	}
    
    
    @Test
    public void testCosteTotal() {
    	inicializaValores();
    	tCamion = new TCamion(muelleOrigenValido, muelleDestinoValido, puertoOrigenValido, puertoDestinoValido, fechaInicioValida, fechaInicioValida.plusDays(6));
    	tBarco = new TBarco(muelleDestinoValido, muelleOrigenValido, puertoDestinoValido, puertoOrigenValido, fechaInicioValida.plusDays(7), fechaInicioValida.plusDays(7));
        packCamionBarco.agregaTrayecto(tCamion);
        packCamionBarco.agregaTrayecto(tBarco);
        double costeCamion = tCamion.costeTotal();
        double costeBarco = tBarco.costeTotal();
        double costeEsperado = costeCamion + costeBarco-costeBarco*0.15;
        assertEquals(costeEsperado, packCamionBarco.costeTotal(), 0.01);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCalculaDescuentoNull() {
    	inicializaValores();
        packCamionBarco.calculaDescuento(null);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testCalculaDescuentoTrayectoNoPertenece() {
    	inicializaValores();
        packCamionBarco.calculaDescuento(tTren);
    }
    

    @Test
    public void testClonacionPackCamionBarco() {
    	inicializaValores();
    	tCamion = new TCamion(muelleOrigenValido, muelleDestinoValido, puertoOrigenValido, puertoDestinoValido, fechaInicioValida, fechaInicioValida.plusDays(1));
        tBarco = new TBarco(muelleDestinoValido, muelleOrigenValido, puertoDestinoValido, puertoOrigenValido, fechaInicioValida.plusDays(2), fechaInicioValida.plusDays(3));
    	packCamionBarco.agregaTrayecto(tCamion);
        packCamionBarco.agregaTrayecto(tBarco);
        PackCamionBarco clon = packCamionBarco.clone();
        assertEquals(packCamionBarco, clon);
    }
    
    @Test
    public void testHayTrayectosSiHay() {
    	inicializaValores();
    	tCamion = new TCamion(muelleOrigenValido, muelleDestinoValido, puertoOrigenValido, puertoDestinoValido, fechaInicioValida, fechaInicioValida.plusDays(1));
    	packCamionBarco.agregaTrayecto(tCamion);
        assertTrue(packCamionBarco.hayTrayectos());
    }
    
    @Test
    public void testHayTrayectosNoHay() {
    	inicializaValores();
        assertFalse(packCamionBarco.hayTrayectos());
    }
    
    @Test
    public void testAgregarTrayectoSimpleValido() {
    	inicializaValores();
    	tBarco = new TBarco(muelleOrigenValido, muelleDestinoValido, puertoOrigenValido, puertoDestinoValido, fechaInicioValida, fechaInicioValida.plusDays(6));
    	packCamionBarco.agregaTrayecto(tBarco);
        assertTrue(packCamionBarco.hayTrayectos());
        assertEquals(1, packCamionBarco.getTrayectosSimples().size());
        assertEquals(packCamionBarco.numeroTrayectosSimples(), 1);
    }
    
    
    @Test(expected = IllegalStateException.class)
    public void testAgregarTrayectoConTipoInvalido() {
    	inicializaValores();
    	packCamionBarco.agregaTrayecto(tTren);
    }

    
    @Test(expected = IllegalArgumentException.class)
    public void testAgregarTrayectoNull() {
    	inicializaValores();
        packCamionBarco.agregaTrayecto(null);
    }

    @Test(expected = IllegalStateException.class)
    public void testAgregarTrayectoRepetido() {
    	inicializaValores();
    	tCamion = new TCamion(muelleOrigenValido, muelleDestinoValido, puertoOrigenValido, puertoDestinoValido, fechaInicioValida, fechaInicioValida.plusDays(5));
        packCamionBarco.agregaTrayecto(tCamion);
        packCamionBarco.agregaTrayecto(tCamion); 
    }
    
    
    @Test(expected = IllegalStateException.class)
    public void testAgregarTrayectoConFechaFinPosteriorAlPack() {
        inicializaValores();
        TCamion primerTrayecto = new TCamion(muelleOrigenValido, muelleDestinoValido, puertoOrigenValido, puertoDestinoValido, fechaInicioValida, fechaInicioValida.plusDays(3));
        TCamion trayectoInvalido = new TCamion(muelleDestinoValido, muelleOrigenValido, puertoDestinoValido, puertoOrigenValido, fechaInicioValida.plusDays(4), fechaFinValida.plusDays(4));
        packCamionBarco.agregaTrayecto(primerTrayecto);
        packCamionBarco.agregaTrayecto(trayectoInvalido);
    }
    
    
    @Test(expected = IllegalStateException.class)
    public void testAgregarTrayectoConFechaFinPosterior() {
        inicializaValores();
        TCamion primerTrayecto = new TCamion(muelleOrigenValido, muelleDestinoValido, puertoOrigenValido, puertoDestinoValido, fechaInicioValida, fechaInicioValida.plusDays(3));
        TCamion trayectoInvalido = new TCamion(muelleDestinoValido, muelleOrigenValido, puertoDestinoValido, puertoOrigenValido, fechaInicioValida.plusDays(2), fechaInicioValida.plusDays(4));
        packCamionBarco.agregaTrayecto(primerTrayecto);
        packCamionBarco.agregaTrayecto(trayectoInvalido);
    }
    
    
    @Test(expected = IllegalStateException.class)
    public void testAgregarTrayectoConMuelleOrigenNoCoincidenteCuandoHayVariosTrayectos() {
        inicializaValores();
        TCamion primerTrayecto = new TCamion(muelleOrigenValido, muelleDestinoValido, puertoOrigenValido, puertoDestinoValido, fechaInicioValida, fechaInicioValida.plusDays(1));
        TCamion segundoTrayecto = new TCamion( muelleDestinoValido, muelleOrigenValido2, puertoDestinoValido, puertoOrigenValido2, fechaInicioValida.plusDays(1), fechaInicioValida.plusDays(2));
        puertoOrigenValido.agregaMuelle(muelleOrigenValido2); 
        TCamion trayectoInvalido = new TCamion(muelleOrigenValido2, muelleDestinoValido, puertoOrigenValido, puertoDestinoValido, fechaInicioValida.plusDays(2),fechaInicioValida.plusDays(3));
        packCamionBarco.agregaTrayecto(primerTrayecto);
        packCamionBarco.agregaTrayecto(segundoTrayecto);
        packCamionBarco.agregaTrayecto(trayectoInvalido);
    }
    
    
    @Test(expected = IllegalStateException.class)
    public void testAgregarTrayectoConPuertoOrigenNoCoincidente() {
        inicializaValores();
        packCamionBarco = new PackCamionBarco(muelleOrigenValido, muelleDestinoValido, puertoOrigenValido, puertoDestinoValido, fechaInicioValida, fechaFinValida);
        TCamion trayectoInvalido = new TCamion(muelleDestinoValido, muelleOrigenValido, puertoDestinoValido, puertoOrigenValido, fechaInicioValida, fechaFinValida);
        packCamionBarco.agregaTrayecto(trayectoInvalido);
    }

    
    @Test(expected = IllegalStateException.class)
    public void testAgregarTrayectoConMuelleOrigenNoCoincidente() {
        inicializaValores();
        puertoOrigenValido.agregaMuelle(muelleOrigenValido2);
        TCamion trayectoInvalido = new TCamion(muelleOrigenValido2, muelleDestinoValido, puertoOrigenValido, puertoDestinoValido, fechaInicioValida,fechaFinValida);
        packCamionBarco.agregaTrayecto(trayectoInvalido);
    }
    
    
    @Test(expected = IllegalStateException.class)
    public void testAgregarTrayectoConPuertoDestinoNoCoincidente() {
        inicializaValores();
        TCamion primerTrayecto = new TCamion(muelleOrigenValido, muelleDestinoValido, puertoOrigenValido, puertoDestinoValido, fechaInicioValida, fechaInicioValida.plusDays(1));
        TCamion trayectoInvalido = new TCamion(muelleOrigenValido, muelleDestinoValido, puertoOrigenValido, puertoDestinoValido, fechaInicioValida.plusDays(1), fechaInicioValida.plusDays(2));
        packCamionBarco.agregaTrayecto(primerTrayecto);
        packCamionBarco.agregaTrayecto(trayectoInvalido);
    }
    
    @Test
    public void testEqualsSuper() {
    	inicializaValores();
        PackCamionBarco pack1 = new PackCamionBarco(muelleOrigenValido, muelleDestinoValido, puertoOrigenValido, puertoDestinoValido, fechaInicioValida, fechaFinValida);
        PackCamionBarco pack2 = new PackCamionBarco(muelleOrigenValido2, muelleDestinoValido, puertoOrigenValido2, puertoDestinoValido, fechaInicioValida, fechaFinValida);
        assertFalse(pack1.equals(pack2)); 
    }
    
    
    @Test
    public void testEqualsCuandoLoSonConReferenciasDistintas() {
    	inicializaValores();
        Combinado packCamionBarco2 = new PackCamionBarco(muelleOrigenValido, muelleDestinoValido, puertoOrigenValido, puertoDestinoValido, fechaInicioValida, fechaFinValida);
        tCamion = new TCamion(muelleOrigenValido, muelleDestinoValido, puertoOrigenValido, puertoDestinoValido, fechaInicioValida, fechaInicioValida.plusDays(5));
        packCamionBarco.agregaTrayecto(tCamion);
        packCamionBarco2.agregaTrayecto(tCamion);

        assertTrue(packCamionBarco.equals(packCamionBarco2));
    }
    
    @Test
    public void testEqualsIgualesPorReferencia() {
    	inicializaValores();
    	assertTrue(packCamionBarco.equals(packCamionBarco));
    }
    
    
    @Test
    public void testEqualsDiferentesPorSerPackCamionTren() {
    	inicializaValores();
    	PackCamionTren otro = new PackCamionTren(muelleOrigenValido, muelleDestinoValido, puertoOrigenValido, puertoDestinoValido, fechaInicioValida, fechaFinValida);
    	assertFalse(packCamionBarco.equals(otro));
    }
    
    
    @Test
    public void testEqualsDiferentesPorTenerDescuentosDiferentes() {
    	inicializaValores();
    	PackCamionBarco otro = new PackCamionBarco(muelleOrigenValido, muelleDestinoValido, puertoOrigenValido, puertoDestinoValido, fechaInicioValida, fechaFinValida);
    	otro.setDescuentoBarco(40);
    	assertFalse(packCamionBarco.equals(otro));
    }
    
    
    @Test
    public void testEqualsNull() {
    	inicializaValores();

        assertFalse(packCamionBarco.equals(null));
    }
    
    
    @Test (expected = IllegalArgumentException.class)
	public void testIsFechaFinSuperiorAOtraNula() {
		inicializaValores();
		assertFalse(tTren.isFechaFinPosterior(null));
	}


    
    @Test
    public void testDistanciaMillasMarinas() {
    	inicializaValores();
    	packCamionBarco.agregaTrayecto(tBarco);
        double distanciaEsperada = packCamionBarco.distanciaKilometros() * 0.539957; 
        assertEquals(distanciaEsperada, packCamionBarco.distanciaMillasMarinas(), 0.001);
    }
    
    
    @Test
    public void testToString() {
    	inicializaValores();
        String stringEsperado = "<Inicio trayecto>\n\t<Pais del Puerto Origen: ES>\n\t<Localidad del Puerto Origen: UNO>\n\t<Fecha de Inicio de Trayecto: 2024-11-08>"
                + "\n<Fin trayecto>\n\t<Pais del Puerto Destino: PD>\n\t<Localidad del Puerto Destino: DOS>\n\t<Fecha de Inicio de Trayecto: 2024-11-28>\n";

        assertEquals(stringEsperado, packCamionBarco.toString());
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testSetDescuentoBarcoCuandoElValorEsMenorQueCero() {
    	inicializaValores();
    	packCamionBarco.setDescuentoBarco(-1);
    }
    
    
    @Test (expected = IllegalArgumentException.class)
    public void testSetDescuentoBarcoCuandoElValorEsMayorQueCien() {
    	inicializaValores();
    	packCamionBarco.setDescuentoBarco(101);
    }
    
    
    @Test
    public void testSetDescuentoBarcoCuandoElValorEsCien() {
    	inicializaValores();
    	packCamionBarco.setDescuentoBarco(100);
    	assertEquals(packCamionBarco.getDescuentoPorBarco(), 100, 0.01);
    }
}
