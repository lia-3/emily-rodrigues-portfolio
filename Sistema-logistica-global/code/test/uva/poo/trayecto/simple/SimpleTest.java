package uva.poo.trayecto.simple;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;

import uva.poo.puerto.Muelle;
import uva.poo.puerto.Puerto;
import uva.poo.puerto.TipoTransporte;
import uva.poo.trayecto.Trayecto;
import uva.poo.puerto.EstadoMuelle;
import es.uva.inf.poo.maps.GPSCoordinate;

/**
 * Prueba la clase Simple
 * @author Marta Pérez
 * @author Emily Rodrigues
 */
public class SimpleTest {

    private static final String ID_MUELLE_ORIGEN = "14";
    private static final String ID_MUELLE_DESTINO = "65";
    private static final String ID_PUERTO_ORIGEN = "ES-UNO";
    private static final String ID_PUERTO_DESTINO = "PD-DOS";

    private static final int NUMERO_PLAZAS = 30;
    private static final int CANTIDAD_MAX_CONTENEDORES = 50;
    private static final EstadoMuelle ESTADO_OPERATIVO = EstadoMuelle.OPERATIVO;
    
    private ArrayList<TipoTransporte> aceptaTodosLosTransportes;

    private Muelle muelleOrigenValido;
    private Muelle muelleDestinoValido;
    private Puerto puertoOrigenValido;
    private Puerto puertoDestinoValido;

    private LocalDate fechaInicioValida;
    private LocalDate fechaFinValida;

    private TBarco tBarco;
    private TCamion tCamion;
	private TTren tTren;

    @Before
    public void inicializaValores() {
    	aceptaTodosLosTransportes = new ArrayList<>();
		aceptaTodosLosTransportes.add(TipoTransporte.BARCO);
		aceptaTodosLosTransportes.add(TipoTransporte.CAMION);
    	
        muelleOrigenValido = new Muelle(ID_MUELLE_ORIGEN, new GPSCoordinate(80, 80), ESTADO_OPERATIVO, NUMERO_PLAZAS, CANTIDAD_MAX_CONTENEDORES, aceptaTodosLosTransportes);
        muelleDestinoValido = new Muelle(ID_MUELLE_DESTINO, new GPSCoordinate(60, 60), ESTADO_OPERATIVO, NUMERO_PLAZAS + 1, CANTIDAD_MAX_CONTENEDORES - 1, aceptaTodosLosTransportes);
        puertoOrigenValido = new Puerto(ID_PUERTO_ORIGEN);
        puertoDestinoValido = new Puerto(ID_PUERTO_DESTINO);

        puertoOrigenValido.agregaMuelle(muelleOrigenValido);
        puertoDestinoValido.agregaMuelle(muelleDestinoValido);

        fechaInicioValida = LocalDate.of(2024, 11, 8);
        fechaFinValida = LocalDate.of(2024, 11, 28);

        tBarco = new TBarco(muelleOrigenValido, muelleDestinoValido, puertoOrigenValido, puertoDestinoValido, fechaInicioValida, fechaFinValida);
        tCamion = new TCamion(muelleOrigenValido, muelleDestinoValido, puertoOrigenValido, puertoDestinoValido, fechaInicioValida, fechaFinValida);
        tTren = new TTren(muelleOrigenValido, muelleDestinoValido, puertoOrigenValido, puertoDestinoValido, fechaInicioValida, fechaFinValida);
    }

    @Test
    public void testCosteTrayectoTBarco() {
        int dias = fechaInicioValida.until(fechaFinValida).getDays();
        double costeEsperado = (4000 * dias);

        assertEquals(costeEsperado, tBarco.costeTotal(), 0.01);
    }

    @Test
    public void testCosteTrayectoTCamion() {
        double distanciaEsperada = tCamion.distanciaKilometros();
        double costeEsperado = (0 * 20) + 200 + (4.5 * distanciaEsperada); 

        assertEquals(costeEsperado, tCamion.costeTotal(), 0.01);
    }

    @Test
    public void testCosteTrayectoTTren() {
        double distanciaEsperada = tTren.distanciaKilometros();
        double costeEsperado = (0 * 20) + 20 + (12.5 * distanciaEsperada); 

        assertEquals(costeEsperado, tTren.costeTotal(), 0.01);
    }

    @Test
    public void testUsaBarco() {
        assertTrue(tBarco.usaBarco());
        assertFalse(tBarco.usaTren());

        assertFalse(tCamion.usaBarco());
        assertFalse(tTren.usaBarco());
    }

    @Test
    public void testUsaTren() {
        assertTrue(tTren.usaTren());
        assertFalse(tBarco.usaTren());
        assertFalse(tCamion.usaTren());
    }

    @Test
    public void testClonacionTBarco() {
        Simple clon = tBarco.clone();
        assertNotSame(tBarco, clon);
        assertEquals(tBarco.getPuertoOrigen(), clon.getPuertoOrigen());
        assertEquals(tBarco.getPuertoDestino(), clon.getPuertoDestino());
        assertEquals(tBarco.getFechaInicio(), clon.getFechaInicio());
        assertEquals(tBarco.getFechaFin(), clon.getFechaFin());
        assertEquals(tBarco.costeFijoBase(), clon.costeFijoBase(), 0.01);
		assertEquals(tBarco.costePorDiaBase(), clon.costePorDiaBase(), 0.01);
		assertEquals(tBarco.costePorKilometroBase(), clon.costePorKilometroBase(), 0.01);
    }

    @Test
    public void testClonacionTCamion() {
        Simple clon = tCamion.clone();
        assertNotSame(tCamion, clon);
        assertEquals(tCamion.getPuertoOrigen(), clon.getPuertoOrigen());
        assertEquals(tCamion.getPuertoDestino(), clon.getPuertoDestino());
        assertEquals(tCamion.getFechaInicio(), clon.getFechaInicio());
        assertEquals(tCamion.getFechaFin(), clon.getFechaFin());
        assertEquals(tCamion.costeFijoBase(), clon.costeFijoBase(), 0.01);
		assertEquals(tCamion.costePorDiaBase(), clon.costePorDiaBase(), 0.01);
		assertEquals(tCamion.costePorKilometroBase(), clon.costePorKilometroBase(), 0.01);
    }

    @Test
    public void testClonacionTTren() {
        Simple clon = tTren.clone();
        assertNotSame(tTren, clon);
        assertEquals(tTren.getPuertoOrigen(), clon.getPuertoOrigen());
        assertEquals(tTren.getPuertoDestino(), clon.getPuertoDestino());
        assertEquals(tTren.getFechaInicio(), clon.getFechaInicio());
        assertEquals(tTren.getFechaFin(), clon.getFechaFin());
        assertEquals(tTren.costeFijoBase(), clon.costeFijoBase(), 0.01);
		assertEquals(tTren.costePorDiaBase(), clon.costePorDiaBase(), 0.01);
		assertEquals(tTren.costePorKilometroBase(), clon.costePorKilometroBase(), 0.01);
    }
    
    @Test
	public void testConstructorConValoresValidosModificandoLosCostesPorElValorDelLimiteInferior() {
		inicializaValores();
		tBarco.setCosteFijo(0);
		tBarco.setCostePorDia(0);
		tBarco.setCostePorKilometro(0);
		Simple trayecto = tBarco;
		
		assertEquals(trayecto.getPuertoOrigen().getIdentificador(), ID_PUERTO_ORIGEN);
		assertEquals(trayecto.getPuertoDestino().getIdentificador(), ID_PUERTO_DESTINO);
		assertEquals(trayecto.getMuelleOrigen().getIdMuelle(), ID_MUELLE_ORIGEN);
		assertEquals(trayecto.getMuelleDestino().getIdMuelle(), ID_MUELLE_DESTINO);
		assertNotSame(trayecto.getFechaInicio(), fechaInicioValida);
		assertEquals(trayecto.getFechaInicio(), fechaInicioValida);
		assertNotSame(trayecto.getFechaFin(), fechaFinValida);
		assertEquals(trayecto.getFechaFin(), fechaFinValida);
		assertEquals(trayecto.costeFijo(), 0, 0.01);
		assertEquals(trayecto.costePorDia(), 0, 0.01);
		assertEquals(trayecto.costePorKilometro(), 0, 0.01);
		assertEquals(trayecto.costeFijoBase(), 0, 0.01);
		assertEquals(trayecto.costePorDiaBase(), 4000, 0.01);
		assertEquals(trayecto.costePorKilometroBase(), 0, 0.01);
	}
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorFechaInicioNula() {
        new TBarco(muelleOrigenValido, muelleDestinoValido, puertoOrigenValido, puertoDestinoValido, null, fechaFinValida);
    }
    
    @Test (expected = IllegalArgumentException.class)
	public void testConstructorConFechaFinNull() {
		inicializaValores();
		new TCamion(muelleOrigenValido, muelleDestinoValido, puertoOrigenValido,
				puertoDestinoValido, fechaInicioValida, null);
	}

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorPuertoIncompatible() {
        Puerto puertoInvalido = new Puerto("INVALIDO");
        new TCamion(muelleOrigenValido, muelleDestinoValido, puertoInvalido, puertoDestinoValido, fechaInicioValida, fechaFinValida);
    }

    @Test
    public void testEqualsTrayectosIguales() {
        TBarco trayectoIgual = new TBarco(muelleOrigenValido, muelleDestinoValido, puertoOrigenValido, puertoDestinoValido, fechaInicioValida, fechaFinValida);
        assertEquals(tBarco, trayectoIgual);
    }
    
    @Test
    public void testEqualsTrayectosIgualesNull() {
        assertFalse(tBarco.equals(null));
    }
    
    @Test
    public void testEqualsTrayectosThisIguales() {
        assertEquals(tBarco, tBarco);
    }

    @Test
    public void testEqualsTrayectosDiferentesPorFechas() {
        TBarco trayectoDiferente = new TBarco(muelleOrigenValido, muelleDestinoValido, puertoOrigenValido, puertoDestinoValido, fechaInicioValida.minusDays(1), fechaFinValida);
        assertNotEquals(tBarco, trayectoDiferente);
    }

    @Test
    public void testEqualsTrayectosDiferentesPorPuertos() {
        Puerto puertoDiferente = new Puerto("ES-VLN");
        puertoDiferente.agregaMuelle(muelleDestinoValido);
        TBarco trayectoDiferente = new TBarco(muelleOrigenValido, muelleDestinoValido, puertoOrigenValido, puertoDiferente, fechaInicioValida, fechaFinValida);
        assertNotEquals(tBarco, trayectoDiferente);
    }
    
    @Test
    public void testEqualsTrayectosDiferentesUsosDeBarcoYTren() {
        assertNotEquals(tBarco, tTren);
    }

    @Test
    public void testGettersTrayecto() {
        assertEquals(muelleOrigenValido, tCamion.getMuelleOrigen());
        assertEquals(muelleDestinoValido, tCamion.getMuelleDestino());
        assertEquals(puertoOrigenValido, tCamion.getPuertoOrigen());
        assertEquals(puertoDestinoValido, tCamion.getPuertoDestino());
        assertEquals(fechaInicioValida, tCamion.getFechaInicio());
        assertEquals(fechaFinValida, tCamion.getFechaFin());
    }

    @Test
    public void testDistanciaKilometros() {
        assertEquals(2327.0921, tTren.distanciaKilometros(), 0.01);
    }

    @Test
    public void testToString() {
        String paisOrigen = puertoOrigenValido.getIdentificador().substring(0, 2);
        String localidadOrigen = puertoOrigenValido.getIdentificador().substring(3, 6);
        String paisDestino = puertoDestinoValido.getIdentificador().substring(0, 2);
        String localidadDestino = puertoDestinoValido.getIdentificador().substring(3, 6);

        String toStringEsperado = "<Inicio trayecto>\n\t<Pais del Puerto Origen: " + paisOrigen 
            + ">\n\t<Localidad del Puerto Origen: " + localidadOrigen 
            + ">\n\t<Fecha de Inicio de Trayecto: " + fechaInicioValida 
            + ">\n<Fin trayecto>\n\t<Pais del Puerto Destino: " + paisDestino 
            + ">\n\t<Localidad del Puerto Destino: " + localidadDestino 
            + ">\n\t<Fecha de Inicio de Trayecto: " + fechaFinValida + ">\n";

        assertEquals(toStringEsperado, tBarco.toString());
    }
    
    @Test (expected = IllegalArgumentException.class)
	public void testConstructorConMuelleOrigenNull() {
		inicializaValores();
		new TTren(null, muelleDestinoValido, puertoOrigenValido,
				puertoDestinoValido, fechaInicioValida, fechaFinValida);
	}
	
	
	@Test (expected = IllegalArgumentException.class)
	public void testConstructorConMuelleDestinoNull() {
		inicializaValores();
		new TBarco(muelleOrigenValido, null, puertoOrigenValido,
				puertoDestinoValido, fechaInicioValida, fechaFinValida);
	}
	
	
	@Test (expected = IllegalArgumentException.class)
	public void testConstructorConPuertoOrigenNull() {
		inicializaValores();
		new TCamion(muelleOrigenValido, muelleDestinoValido, null,
				puertoDestinoValido, fechaInicioValida, fechaFinValida);
	}
	
	
	@Test (expected = IllegalArgumentException.class)
	public void testConstructorConPuertoDestinoNull() {
		inicializaValores();
		new TTren(muelleOrigenValido, muelleDestinoValido, puertoOrigenValido,
				null, fechaInicioValida, fechaFinValida);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testConstructorConMismoPuertoDeOrigenYDestino() {
		inicializaValores();
		new TTren(muelleOrigenValido, muelleOrigenValido, puertoOrigenValido,
				puertoOrigenValido, fechaInicioValida, fechaFinValida);
	}
	
	
	@Test (expected = IllegalArgumentException.class)
	public void testConstructorConMuelleOrigenQueNoEstaEnPuertoOrigen() {
		inicializaValores();
		Muelle muelleNoEnPuerto = new Muelle("42", new GPSCoordinate(70,70), ESTADO_OPERATIVO, NUMERO_PLAZAS, CANTIDAD_MAX_CONTENEDORES, aceptaTodosLosTransportes);
		new TBarco(muelleNoEnPuerto, muelleDestinoValido, puertoOrigenValido,
				puertoDestinoValido, fechaInicioValida, fechaFinValida);
	}
	
	
	@Test (expected = IllegalArgumentException.class)
	public void testConstructorConMuelleDestinoQueNoEstaEnPuertoDestino() {
		inicializaValores();
		Muelle muelleNoEnPuerto = new Muelle("42", new GPSCoordinate(70,70), ESTADO_OPERATIVO, NUMERO_PLAZAS, CANTIDAD_MAX_CONTENEDORES, aceptaTodosLosTransportes);
		new TCamion(muelleOrigenValido, muelleNoEnPuerto, puertoOrigenValido,
				puertoDestinoValido, fechaInicioValida, fechaFinValida);
	}
	
	
	@Test (expected = IllegalArgumentException.class)
	public void testConstructorConFechaInicioPosteriorAFechaFin() {
		inicializaValores();
		LocalDate fechaInicioErronea = fechaFinValida;
		LocalDate fechaFinErronea = fechaInicioValida;
		Muelle muelleNoEnPuerto = new Muelle("42", new GPSCoordinate(70,70), ESTADO_OPERATIVO, NUMERO_PLAZAS, CANTIDAD_MAX_CONTENEDORES, aceptaTodosLosTransportes);
		new TTren(muelleOrigenValido, muelleNoEnPuerto, puertoOrigenValido,
				puertoDestinoValido, fechaInicioErronea, fechaFinErronea);
	}
	

	@Test
	public void testDistanciaEnKM() {
		inicializaValores();
		Trayecto trayectoBarco = new TBarco(muelleOrigenValido, muelleDestinoValido, puertoOrigenValido, puertoDestinoValido, fechaInicioValida, fechaFinValida);
		assertEquals(trayectoBarco.distanciaKilometros(), 2327.0921, 0.0001);
	}
	
	
	@Test
	public void testDistanciaEnMillasMarina() {
	    inicializaValores();
		Trayecto trayectoBarco = new TBarco(muelleOrigenValido, muelleDestinoValido, puertoOrigenValido, puertoDestinoValido, fechaInicioValida, fechaFinValida);
	    assertEquals(1256.529, trayectoBarco.distanciaMillasMarinas(), 0.001);
	}
	
	@Test 
	public void testIsFechaFinPosteriorAOtraDadaCuandoLoEs() {
		inicializaValores();
		LocalDate fechaInferior = LocalDate.of(2024, 11, 18);
		Trayecto trayectoBarco = new TBarco(muelleOrigenValido, muelleDestinoValido, puertoOrigenValido, puertoDestinoValido, fechaInicioValida, fechaFinValida);
		assertTrue(trayectoBarco.isFechaFinPosterior(fechaInferior));
	}
	
	
	@Test 
	public void testIsFechaFinPosteriorAOtraDadaCuandoLoEsPorUnDia() {
		inicializaValores();
		LocalDate fechaInferior = LocalDate.of(2024, 11, 27);
		Trayecto trayectoBarco = new TBarco(muelleOrigenValido, muelleDestinoValido, puertoOrigenValido, puertoDestinoValido, fechaInicioValida, fechaFinValida);
		assertTrue(trayectoBarco.isFechaFinPosterior(fechaInferior));
	}
	
	
	@Test 
	public void testIsFechaFinPosteriorAOtraDadaCuandoSonIguales() {
		inicializaValores();
		Trayecto trayectoBarco = new TBarco(muelleOrigenValido, muelleDestinoValido, puertoOrigenValido, puertoDestinoValido, fechaInicioValida, fechaFinValida);
		assertFalse(trayectoBarco.isFechaFinPosterior(fechaFinValida));
	}
	
	
	@Test 
	public void testIsFechaFinPosteriorAOtraDadaCuandoNoLoEs() {
		inicializaValores();
		LocalDate fechaSuperior = LocalDate.of(2024, 11, 30);
		Trayecto trayectoBarco = new TBarco(muelleOrigenValido, muelleDestinoValido, puertoOrigenValido, puertoDestinoValido, fechaInicioValida, fechaFinValida);
		assertFalse(trayectoBarco.isFechaFinPosterior(fechaSuperior));
	}
	
	
	@Test 
	public void testIsFechaFinPosteriorAOtraDadaCuandoNoLoEsPrUnDia() {
		inicializaValores();
		LocalDate fechaSuperior = LocalDate.of(2024, 11, 29);
		Trayecto trayectoBarco = new TBarco(muelleOrigenValido, muelleDestinoValido, puertoOrigenValido, puertoDestinoValido, fechaInicioValida, fechaFinValida);
		assertFalse(trayectoBarco.isFechaFinPosterior(fechaSuperior));
	}
	
	
	@Test (expected = IllegalArgumentException.class)
	public void testIsFechaFinSuperiorAOtraNula() {
		inicializaValores();
		Trayecto trayectoBarco = new TBarco(muelleOrigenValido, muelleDestinoValido, puertoOrigenValido, puertoDestinoValido, fechaInicioValida, fechaFinValida);
		trayectoBarco.isFechaFinPosterior(null);
	}


	@Test (expected = IllegalArgumentException.class)
	public void testSetCostePorDiaConValorMenorAlLimiteInferior() {
		inicializaValores();
		tBarco.setCostePorDia(-0.01);
	}
	
	
	@Test (expected = IllegalArgumentException.class)
	public void testSetCostePorKilometroConValorMenorAlLimiteInferior() {
		inicializaValores();
		tTren.setCostePorKilometro(-0.01);
	}
	
	
	@Test (expected = IllegalArgumentException.class)
	public void testSetCosteFijoConValorMenorAlLimiteInferior() {
		inicializaValores();
		tCamion.setCosteFijo(-0.01);
	}
}
