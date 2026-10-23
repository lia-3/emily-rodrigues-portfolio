package uva.poo.trayecto;

import static org.junit.Assert.*;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;

import es.uva.inf.poo.maps.GPSCoordinate;
import uva.poo.puerto.EstadoMuelle;
import uva.poo.puerto.Muelle;
import uva.poo.puerto.Puerto;
import uva.poo.puerto.TipoTransporte;
import uva.poo.trayecto.simple.*;


/**
 * Prueba la clase Trayecto
 * @author Marta Pérez
 * @author Emily Rodrigues
 */
public class TrayectoTest {
	
	private static final String ID_MUELLE_ORIGEN = "32";
	private static final String ID_MUELLE_DESTINO = "22";
	private static final String ID_PUERTO_ORIGEN = "PO-UNO";
	private static final String ID_PUERTO_DESTINO = "PD-DOS";
	
	private static final int NUMERO_PLAZAS = 30;
	private static final int CANTIDAD_MAX_CONTENEDORES = 50;
	private static final EstadoMuelle ESTADO_OPERATIVO = EstadoMuelle.OPERATIVO;
	
	private ArrayList<TipoTransporte> aceptaTodosLosTransportes;
	
	private Muelle muelleOrigenValido;
	private Muelle muelleDestinoValido;
	
	private Muelle muelleNoEnPuerto;
	
	private Puerto puertoOrigenValido;
	private Puerto puertoDestinoValido;
	
	private Trayecto trayectoBarco;
	
	private LocalDate fechaInicioValida = LocalDate.of(2024, 11, 8);
	private LocalDate fechaFinValida = LocalDate.of(2024, 11, 28);
	
	
	private void inicializaValores() {
		aceptaTodosLosTransportes = new ArrayList<>();
		aceptaTodosLosTransportes.add(TipoTransporte.BARCO);
		aceptaTodosLosTransportes.add(TipoTransporte.CAMION);
		aceptaTodosLosTransportes.add(TipoTransporte.TREN);
		
		muelleOrigenValido = new Muelle(ID_MUELLE_ORIGEN, new GPSCoordinate(80,80), ESTADO_OPERATIVO, NUMERO_PLAZAS, CANTIDAD_MAX_CONTENEDORES, aceptaTodosLosTransportes);
		muelleDestinoValido = new Muelle(ID_MUELLE_DESTINO, new GPSCoordinate(60,60), ESTADO_OPERATIVO, NUMERO_PLAZAS + 1, CANTIDAD_MAX_CONTENEDORES-1, aceptaTodosLosTransportes);
		muelleNoEnPuerto = new Muelle("42", new GPSCoordinate(70,70), ESTADO_OPERATIVO, NUMERO_PLAZAS, CANTIDAD_MAX_CONTENEDORES, aceptaTodosLosTransportes);
		
		puertoOrigenValido = new Puerto(ID_PUERTO_ORIGEN);
		puertoDestinoValido = new Puerto(ID_PUERTO_DESTINO);
		
		puertoOrigenValido.agregaMuelle(muelleOrigenValido);
		puertoDestinoValido.agregaMuelle(muelleDestinoValido);
		
		trayectoBarco = new TBarco(muelleOrigenValido, muelleDestinoValido, puertoOrigenValido,
				puertoDestinoValido, fechaInicioValida, fechaFinValida);
	}

	@Test
	public void testConstructorConValoresValidos() {
		inicializaValores();
		
		assertEquals(trayectoBarco.getPuertoOrigen().getIdentificador(), ID_PUERTO_ORIGEN);
		assertEquals(trayectoBarco.getPuertoDestino().getIdentificador(), ID_PUERTO_DESTINO);
		assertEquals(trayectoBarco.getMuelleOrigen().getIdMuelle(), ID_MUELLE_ORIGEN);
		assertEquals(trayectoBarco.getMuelleDestino().getIdMuelle(), ID_MUELLE_DESTINO);
		assertNotSame(trayectoBarco.getFechaInicio(), fechaInicioValida);
		assertEquals(trayectoBarco.getFechaInicio(), fechaInicioValida);
		assertNotSame(trayectoBarco.getFechaFin(), fechaFinValida);
		assertEquals(trayectoBarco.getFechaFin(), fechaFinValida);
	}
	
	
	@Test
	public void testConstructorMismaFechaInicioYFin() {
		inicializaValores();
		Trayecto trayecto = new TCamion(muelleOrigenValido, muelleDestinoValido, puertoOrigenValido,
				puertoDestinoValido, fechaInicioValida, fechaInicioValida);
		
		assertEquals(trayecto.getPuertoOrigen().getIdentificador(), ID_PUERTO_ORIGEN);
		assertEquals(trayecto.getPuertoDestino().getIdentificador(), ID_PUERTO_DESTINO);
		assertEquals(trayecto.getMuelleOrigen().getIdMuelle(), ID_MUELLE_ORIGEN);
		assertEquals(trayecto.getMuelleDestino().getIdMuelle(), ID_MUELLE_DESTINO);
		assertNotSame(trayecto.getFechaInicio(), fechaInicioValida);
		assertEquals(trayecto.getFechaInicio(), fechaInicioValida);
		assertNotSame(trayecto.getFechaFin(), fechaInicioValida);
		assertEquals(trayecto.getFechaFin(), fechaInicioValida);	
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
	
	
	@Test (expected = IllegalArgumentException.class)
	public void testConstructorConMismoPuertoDeOrigenYDestino() {
		inicializaValores();
		new TTren(muelleOrigenValido, muelleOrigenValido, puertoOrigenValido,
				puertoOrigenValido, fechaInicioValida, fechaFinValida);
	}
	
	
	@Test (expected = IllegalArgumentException.class)
	public void testConstructorConMuelleOrigenQueNoEstaEnPuertoOrigen() {
		inicializaValores();
		new TBarco(muelleNoEnPuerto, muelleDestinoValido, puertoOrigenValido,
				puertoDestinoValido, fechaInicioValida, fechaFinValida);
	}
	
	
	@Test (expected = IllegalArgumentException.class)
	public void testConstructorConMuelleDestinoQueNoEstaEnPuertoDestino() {
		inicializaValores();
		new TCamion(muelleOrigenValido, muelleNoEnPuerto, puertoOrigenValido,
				puertoDestinoValido, fechaInicioValida, fechaFinValida);
	}
	
	
	@Test (expected = IllegalArgumentException.class)
	public void testConstructorConFechaInicioPosteriorAFechaFin() {
		inicializaValores();
		LocalDate fechaInicioErronea = fechaFinValida;
		LocalDate fechaFinErronea = fechaInicioValida;
		new TTren(muelleOrigenValido, muelleNoEnPuerto, puertoOrigenValido,
				puertoDestinoValido, fechaInicioErronea, fechaFinErronea);
	}
	
	
	@Test
	public void testCloneMedianteTrayectoValido() {
		inicializaValores();
		Trayecto copia = trayectoBarco.clone();
		assertNotSame(copia, trayectoBarco);
		assertEquals(copia.getPuertoOrigen().getIdentificador(), ID_PUERTO_ORIGEN);
		assertEquals(copia.getPuertoDestino().getIdentificador(), ID_PUERTO_DESTINO);
		assertEquals(copia.getMuelleOrigen().getIdMuelle(), ID_MUELLE_ORIGEN);
		assertEquals(copia.getMuelleDestino().getIdMuelle(), ID_MUELLE_DESTINO);
		assertNotSame(copia.getFechaInicio(), fechaInicioValida);
		assertEquals(copia.getFechaInicio(), fechaInicioValida);
		assertNotSame(copia.getFechaFin(), fechaFinValida);
		assertEquals(copia.getFechaFin(), fechaFinValida);
	}
	
	
	@Test 
	public void testIsFechaFinPosteriorAOtraDadaCuandoLoEs() {
		inicializaValores();
		LocalDate fechaInferior = LocalDate.of(2024, 11, 18);
		assertTrue(trayectoBarco.isFechaFinPosterior(fechaInferior));
	}
	
	
	@Test 
	public void testIsFechaFinPosteriorAOtraDadaCuandoLoEsPorUnDia() {
		inicializaValores();
		LocalDate fechaInferior = LocalDate.of(2024, 11, 27);
		assertTrue(trayectoBarco.isFechaFinPosterior(fechaInferior));
	}
	
	
	@Test 
	public void testIsFechaFinPosteriorAOtraDadaCuandoSonIguales() {
		inicializaValores();
		assertFalse(trayectoBarco.isFechaFinPosterior(fechaFinValida));
	}
	
	
	@Test 
	public void testIsFechaFinPosteriorAOtraDadaCuandoNoLoEs() {
		inicializaValores();
		LocalDate fechaSuperior = LocalDate.of(2024, 11, 30);
		assertFalse(trayectoBarco.isFechaFinPosterior(fechaSuperior));
	}
	
	
	@Test 
	public void testIsFechaFinPosteriorAOtraDadaCuandoNoLoEsPrUnDia() {
		inicializaValores();
		LocalDate fechaSuperior = LocalDate.of(2024, 11, 29);
		assertFalse(trayectoBarco.isFechaFinPosterior(fechaSuperior));
	}
	
	
	@Test (expected = IllegalArgumentException.class)
	public void testIsFechaFinSuperiorAOtraNula() {
		inicializaValores();
		assertFalse(trayectoBarco.isFechaFinPosterior(null));
	}
	
	
	@Test
	public void testDistanciaEnKM() {
		inicializaValores();
		assertEquals(trayectoBarco.distanciaKilometros(), 2327.0921, 0.0001);
	}
	
	
	@Test
	public void testDistanciaEnMillasMarina() {
	    inicializaValores();
	    assertEquals(1256.529, trayectoBarco.distanciaMillasMarinas(), 0.001);
	}
	
	
	@Test
	public void testCosteTrayectoConValoresValidos() {
		inicializaValores();
		int dias = fechaInicioValida.until(fechaFinValida).getDays();
        double costeEsperado = (4000 * dias);
        assertEquals(costeEsperado, trayectoBarco.costeTotal(), 0.01);
	}
	
	
	@Test
	public void testEqualsCuandoDosTrayectosLoSon() {
		inicializaValores();
		assertTrue(trayectoBarco.equals(trayectoBarco));
	}
	
	
	@Test
	public void testEqualsCuandoDosTrayectosNoLoSon() {
		inicializaValores();
		Trayecto inverso = new TCamion(muelleDestinoValido, muelleOrigenValido, puertoDestinoValido,
					puertoOrigenValido, fechaInicioValida, fechaFinValida);
		assertFalse(trayectoBarco.equals(inverso));
	}
	
	@Test
	public void testEqualsConDiferenteMuelleOrigen() {
	    inicializaValores();
	    Muelle muelleOrigenDiferente = new Muelle("12", new GPSCoordinate(12, 12), ESTADO_OPERATIVO, NUMERO_PLAZAS, CANTIDAD_MAX_CONTENEDORES, aceptaTodosLosTransportes);
	    puertoOrigenValido.agregaMuelle(muelleOrigenDiferente);
	    Trayecto trayectoDiferente = new TBarco(muelleOrigenDiferente, muelleDestinoValido, puertoOrigenValido, puertoDestinoValido, fechaInicioValida, fechaFinValida);
	    assertFalse(trayectoBarco.equals(trayectoDiferente));
	}

	@Test
	public void testEqualsConDiferenteMuelleDestino() {
	    inicializaValores();
	    Muelle muelleDestinoDiferente = new Muelle("88", new GPSCoordinate(50, 50), ESTADO_OPERATIVO, NUMERO_PLAZAS, CANTIDAD_MAX_CONTENEDORES, aceptaTodosLosTransportes);
	    puertoDestinoValido.agregaMuelle(muelleDestinoDiferente);
	    Trayecto trayectoDiferente = new TBarco(muelleOrigenValido, muelleDestinoDiferente, puertoOrigenValido, puertoDestinoValido, fechaInicioValida, fechaFinValida);
	    assertFalse(trayectoBarco.equals(trayectoDiferente));
	}

	@Test
	public void testEqualsConDiferentePuertoOrigen() {
	    inicializaValores();
	    Puerto puertoOrigenDiferente = new Puerto("PO-DOS");
	    puertoOrigenDiferente.agregaMuelle(muelleOrigenValido);
	    Trayecto trayectoDiferente = new TBarco(muelleOrigenValido, muelleDestinoValido, puertoOrigenDiferente, puertoDestinoValido, fechaInicioValida, fechaFinValida);
	    assertFalse(trayectoBarco.equals(trayectoDiferente));
	}

	@Test
	public void testEqualsConDiferentePuertoDestino() {
	    inicializaValores();
	    Puerto puertoDestinoDiferente = new Puerto("PD-TRE");
	    puertoDestinoDiferente.agregaMuelle(muelleDestinoValido);
	    Trayecto trayectoDiferente = new TBarco(muelleOrigenValido, muelleDestinoValido, puertoOrigenValido, puertoDestinoDiferente, fechaInicioValida, fechaFinValida);
	    assertFalse(trayectoBarco.equals(trayectoDiferente));
	}

	@Test
	public void testEqualsConDiferenteFechaInicio() {
	    inicializaValores();
	    LocalDate fechaInicioDiferente = fechaInicioValida.plusDays(1);
	    Trayecto trayectoDiferente = new TBarco(muelleOrigenValido, muelleDestinoValido, puertoOrigenValido, puertoDestinoValido, fechaInicioDiferente, fechaFinValida);
	    assertFalse(trayectoBarco.equals(trayectoDiferente));
	}

	@Test
	public void testEqualsConDiferenteFechaFin() {
	    inicializaValores();
	    LocalDate fechaFinDiferente = fechaFinValida.plusDays(1);
	    Trayecto trayectoDiferente = new TBarco(muelleOrigenValido, muelleDestinoValido, puertoOrigenValido, puertoDestinoValido, fechaInicioValida, fechaFinDiferente);
	    assertFalse(trayectoBarco.equals(trayectoDiferente));
	}
	
	
	@Test
	public void testEqualsCuandoElTrayectoACompararEsNull() {
		inicializaValores();
		assertFalse(trayectoBarco.equals(null));
	}
	
	
	
	@Test
	public void testToString() {
	    inicializaValores();
	    String resultado = "<Inicio trayecto>\n" 
	             + "\t<Pais del Puerto Origen: PO>\n" 
	             + "\t<Localidad del Puerto Origen: UNO>\n"
	             + "\t<Fecha de Inicio de Trayecto: 2024-11-08>"
	             + "\n"
	             + "<Fin trayecto>\n"
	             + "\t<Pais del Puerto Destino: PD>\n"
	             + "\t<Localidad del Puerto Destino: DOS>\n" 
	             + "\t<Fecha de Inicio de Trayecto: 2024-11-28>\n";
	    assertEquals(resultado, trayectoBarco.toString());
	}

}