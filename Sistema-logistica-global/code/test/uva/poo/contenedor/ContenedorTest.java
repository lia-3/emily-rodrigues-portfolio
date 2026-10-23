package uva.poo.contenedor;

import static org.junit.Assert.*;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;

import es.uva.inf.poo.maps.GPSCoordinate;
import uva.poo.puerto.EstadoMuelle;
import uva.poo.puerto.Muelle;
import uva.poo.puerto.Puerto;
import uva.poo.puerto.TipoTransporte;
import uva.poo.trayecto.Trayecto;
import uva.poo.trayecto.simple.TBarco;
import uva.poo.trayecto.simple.TCamion;
import uva.poo.trayecto.simple.TTren;


/**
 * Prueba la clase Contenedor
 * @author Marta Pérez
 * @author Emily Rodrigues
 */
public class ContenedorTest {

	private static final String ID_MUELLE = "32";
	private static final String ID_CONTENEDOR_WIKI = "CSQU3054383";
	private static final String OTRO_ID_CONTENEDOR = "AZXZ0091238";
	private static final String TERCER_ID_CONTENEDOR = "AZXJ0091237";
	private static final GPSCoordinate PUNTO_GPS = new GPSCoordinate(80,80);
	private static final int NUMERO_PLAZAS = 30;
	private static final int CANTIDAD_MAX_CONTENEDORES = 50;
	private static final EstadoMuelle ESTADO_OPERATIVO = EstadoMuelle.OPERATIVO;
	
	private static final int DIEZ = 10;	
	
	private Puerto puertoOrigen1 = new Puerto("PO-UNO");
	private Puerto puertoOrigen2 = new Puerto("PO-DOS");
	private Puerto puertoDestino1 = new Puerto("PD-UNO");
	private Puerto puertoDestino2 = new Puerto("PD-DOS");
	private Puerto puertoOrigen3 = new Puerto("ES-TRS");
	private Puerto puertoDestino3 = new Puerto("RU-TRS");
	
	private Trayecto trayecto1;
	private Trayecto trayecto2;
	private Trayecto trayecto3;
	
	private Contenedor contenedor;
	
	
	private void inicializaValores() {
		ArrayList<TipoTransporte> aceptaTodosLosTransportes = new ArrayList<>();
		aceptaTodosLosTransportes.add(TipoTransporte.BARCO);
		aceptaTodosLosTransportes.add(TipoTransporte.CAMION);
		aceptaTodosLosTransportes.add(TipoTransporte.TREN);
				
		Muelle muelleOrigen1 = new Muelle(ID_MUELLE, PUNTO_GPS, ESTADO_OPERATIVO, 
				NUMERO_PLAZAS, CANTIDAD_MAX_CONTENEDORES, aceptaTodosLosTransportes);
		Muelle muelleDestino1 = new Muelle("22", new GPSCoordinate(60,60), ESTADO_OPERATIVO, 
				NUMERO_PLAZAS + 1, CANTIDAD_MAX_CONTENEDORES, aceptaTodosLosTransportes);
		Muelle muelleOrigen2 = new Muelle("42", new GPSCoordinate(70,70), ESTADO_OPERATIVO, 
				NUMERO_PLAZAS, CANTIDAD_MAX_CONTENEDORES + 1, aceptaTodosLosTransportes);
		Muelle muelleDestino2 = new Muelle("52", new GPSCoordinate(75,80), ESTADO_OPERATIVO, 
				NUMERO_PLAZAS + 1, CANTIDAD_MAX_CONTENEDORES-1, aceptaTodosLosTransportes);
		Muelle muelleDestino3 = new Muelle("21", new GPSCoordinate(10,70), ESTADO_OPERATIVO, 
				NUMERO_PLAZAS + 1, CANTIDAD_MAX_CONTENEDORES+2, aceptaTodosLosTransportes);
		Muelle muelleOrigen3 = new Muelle("52", new GPSCoordinate(70,80), ESTADO_OPERATIVO, 
				NUMERO_PLAZAS, CANTIDAD_MAX_CONTENEDORES + 2, aceptaTodosLosTransportes);
		
		puertoOrigen1.agregaMuelle(muelleOrigen1);
		puertoOrigen2.agregaMuelle(muelleOrigen2);
		puertoDestino1.agregaMuelle(muelleDestino1);
		puertoDestino2.agregaMuelle(muelleDestino2);
		puertoOrigen3.agregaMuelle(muelleOrigen3);
		puertoDestino3.agregaMuelle(muelleDestino3);
		
		LocalDate fechaInicio1 = LocalDate.of(2024, 11, 8);
		LocalDate fechaFin1 = LocalDate.of(2024, 11, 28);
		LocalDate fechaInicio2 = LocalDate.of(2024, 12, 8);
		LocalDate fechaFin2 = LocalDate.of(2024, 12, 28);
		LocalDate fechaInicio3 = LocalDate.of(2024, 10, 8);
		LocalDate fechaFin3 = LocalDate.of(2024, 10, 28);
		
		trayecto1 = new TBarco(muelleOrigen1, muelleDestino1, puertoOrigen1, 
				puertoDestino1, fechaInicio1, fechaFin1);
		trayecto2 = new TTren(muelleOrigen2, muelleDestino2, puertoOrigen2, 
				puertoDestino2, fechaInicio2, fechaFin2);
		trayecto3 = new TCamion(muelleOrigen3, muelleDestino3, puertoOrigen3, 
				puertoDestino3, fechaInicio3, fechaFin3);
		
		contenedor = new Estandar(ID_CONTENEDOR_WIKI, DIEZ, DIEZ, UnidadesPeso.KILOGRAMOS,
				DIEZ, UnidadesVolumen.METROS_CUBICOS, EstadoContenedor.RECOGIDA);
		
		
		contenedor.agregaTrayecto(trayecto1);
		contenedor.agregaTrayecto(trayecto2);
		contenedor.agregaTrayecto(trayecto3);
	}	
	
	@Test
	public void testConstructorCuandoIdValidoTipoJConTechoConParedesSinMotorFactorDiezParaTodasLasUnidadesEnKgYMetrosCubicosEnRecogida() {
		contenedor = new Estandar(TERCER_ID_CONTENEDOR, DIEZ, DIEZ, UnidadesPeso.KILOGRAMOS,
						DIEZ, UnidadesVolumen.METROS_CUBICOS, EstadoContenedor.RECOGIDA);
		assertEquals(contenedor.getIdentificador(), TERCER_ID_CONTENEDOR);
		assertEquals(contenedor.getMaximaCargaUtilKilogramos(), 10.0, 0.001);
		assertEquals(contenedor.getPesoTaraKilogramos(), 10.0, 0.001);
		assertEquals(contenedor.getVolumenMetrosCubicos(), DIEZ, 0.001);
		assertEquals(contenedor.getEstado(), EstadoContenedor.RECOGIDA);
		assertTrue(contenedor.tieneTecho());
		assertFalse(contenedor.tieneMotor());
		assertTrue(contenedor.tieneParedes());
	}
	
	
	@Test
	public void testConstructorCuandoIdValidoTipoSinTechoSinParedesSinMotorFactorUnoParaTodasLasUnidadesEnLibrasYPiesCubicosEnTransito() {
		contenedor = new FlatRack(OTRO_ID_CONTENEDOR, 1, 1, UnidadesPeso.LIBRAS,
						1, UnidadesVolumen.PIES_CUBICOS, EstadoContenedor.TRANSITO);
		assertEquals(contenedor.getIdentificador(), OTRO_ID_CONTENEDOR);
		assertEquals(contenedor.getMaximaCargaUtilLibras(), 1, 0.001);
		assertEquals(contenedor.getPesoTaraLibras(), 1, 0.001);
		assertEquals(contenedor.getVolumenPiesCubicos(), 1, 0.001);
		assertEquals(contenedor.getEstado(), EstadoContenedor.TRANSITO);
		assertFalse(contenedor.tieneTecho());
		assertFalse(contenedor.tieneMotor());
		assertFalse(contenedor.tieneParedes());
	}
	
	
	@Test
	public void testConstructorCuandoIdValidoTipoUConMotor() {
		contenedor = new Refrigerado(OTRO_ID_CONTENEDOR, 1, 1, UnidadesPeso.KILOGRAMOS,
				1, UnidadesVolumen.METROS_CUBICOS, EstadoContenedor.TRANSITO);
		assertEquals(contenedor.getIdentificador(), OTRO_ID_CONTENEDOR);
		assertEquals(contenedor.getMaximaCargaUtilKilogramos(), 1, 0.001);
		assertEquals(contenedor.getPesoTaraKilogramos(), 1.0, 0.001);
		assertEquals(contenedor.getVolumenMetrosCubicos(), 1, 0.001);
		assertEquals(contenedor.getEstado(), EstadoContenedor.TRANSITO);
		assertTrue(contenedor.tieneTecho());
		assertTrue(contenedor.tieneMotor());
		assertTrue(contenedor.tieneParedes());
	}
	
	
	@Test (expected = IllegalArgumentException.class)
	public void testConstructorCuandoIdEsNull() {
		new Estandar(null, DIEZ, DIEZ, UnidadesPeso.KILOGRAMOS,
						DIEZ, UnidadesVolumen.METROS_CUBICOS, EstadoContenedor.TRANSITO);
	}
	
	
	@Test (expected = IllegalArgumentException.class)
	public void testConstructorCuandoIdEsCadenaVacia() {
		new Estandar("", DIEZ, DIEZ, UnidadesPeso.KILOGRAMOS,
						DIEZ, UnidadesVolumen.METROS_CUBICOS, EstadoContenedor.TRANSITO);
	}
	
	
	@Test (expected = IllegalArgumentException.class)
	public void testConstructorCuandoIdTineLongitudSuperiorAVL() {
		new Estandar("CSQU30543833", DIEZ, DIEZ, UnidadesPeso.KILOGRAMOS,
						DIEZ, UnidadesVolumen.METROS_CUBICOS, EstadoContenedor.TRANSITO);
	}
	
	
	@Test (expected = IllegalArgumentException.class)
	public void testConstructorCuandoIdTineUnValorNoValidoEnElCodigoDuenno() {
		new Estandar("CSqU3054383", DIEZ, DIEZ, UnidadesPeso.KILOGRAMOS,
						DIEZ, UnidadesVolumen.METROS_CUBICOS, EstadoContenedor.TRANSITO);
	}
	
	
	@Test (expected = IllegalArgumentException.class)
	public void testConstructorCuandoIdTineUnTipoInvalido() {
		new Estandar("CSQA3054383", DIEZ, DIEZ, UnidadesPeso.KILOGRAMOS,
						DIEZ, UnidadesVolumen.METROS_CUBICOS, EstadoContenedor.TRANSITO);
	}
	
	
	@Test (expected = IllegalArgumentException.class)
	public void testConstructorCuandoIdTineUnCaracterInvalidoEnElNumeroDeSerie() {
		new Estandar("CSQU30A4383", DIEZ, DIEZ, UnidadesPeso.KILOGRAMOS,
						DIEZ, UnidadesVolumen.METROS_CUBICOS, EstadoContenedor.TRANSITO);
	}
	
	
	@Test (expected = IllegalArgumentException.class)
	public void testConstructorCuandoIdTineUnDigitoControlInvalido() {
		new Estandar("CSQU3054384", DIEZ, DIEZ, UnidadesPeso.KILOGRAMOS,
						DIEZ, UnidadesVolumen.METROS_CUBICOS, EstadoContenedor.TRANSITO);
	}
	
	
	@Test (expected = IllegalArgumentException.class)
	public void testConstructorCuandoPesoTaraCeroKG() {
		new Estandar(ID_CONTENEDOR_WIKI, 0, DIEZ, UnidadesPeso.KILOGRAMOS,
						DIEZ, UnidadesVolumen.METROS_CUBICOS, EstadoContenedor.TRANSITO);
	}
	
	
	@Test (expected = IllegalArgumentException.class)
	public void testConstructorCuandoPesoTaraMenorCeroKG() {
		new Estandar(ID_CONTENEDOR_WIKI, -9, DIEZ, UnidadesPeso.KILOGRAMOS,
						DIEZ, UnidadesVolumen.METROS_CUBICOS, EstadoContenedor.TRANSITO);
	}
	
	
	@Test (expected = IllegalArgumentException.class)
	public void testConstructorCuandoMaximaCargaUtilCeroKG() {
		new Estandar(ID_CONTENEDOR_WIKI, DIEZ, 0, UnidadesPeso.KILOGRAMOS,
						DIEZ, UnidadesVolumen.METROS_CUBICOS, EstadoContenedor.TRANSITO);
	}
	
	
	@Test (expected = IllegalArgumentException.class)
	public void testConstructorCuandoMaximaCargaUtilMenorCeroKG() {
		new Estandar(ID_CONTENEDOR_WIKI, DIEZ, -9, UnidadesPeso.KILOGRAMOS,
						DIEZ, UnidadesVolumen.METROS_CUBICOS, EstadoContenedor.TRANSITO);
	}
	
	
	@Test (expected = IllegalArgumentException.class)
	public void testConstructorCuandoUnidadesPesoNull() {
		new Estandar(ID_CONTENEDOR_WIKI, DIEZ, DIEZ, null,
						DIEZ, UnidadesVolumen.METROS_CUBICOS, EstadoContenedor.TRANSITO);
	}
	
	
	@Test (expected = IllegalArgumentException.class)
	public void testConstructorCuandoVolumenCero() {
		new Estandar(ID_CONTENEDOR_WIKI, DIEZ, DIEZ, UnidadesPeso.KILOGRAMOS,
						0, UnidadesVolumen.METROS_CUBICOS, EstadoContenedor.TRANSITO);
	}
	
	
	@Test (expected = IllegalArgumentException.class)
	public void testConstructorCuandoVolumenMenorCero() {
		new Estandar(ID_CONTENEDOR_WIKI, DIEZ, DIEZ, UnidadesPeso.KILOGRAMOS,
						-9, UnidadesVolumen.METROS_CUBICOS, EstadoContenedor.TRANSITO);
	}
	
	
	@Test (expected = IllegalArgumentException.class)
	public void testConstructorCuandoUnidadesVolumenNull() {
		new Estandar(ID_CONTENEDOR_WIKI, DIEZ, DIEZ, UnidadesPeso.KILOGRAMOS,
						DIEZ, null, EstadoContenedor.TRANSITO);
	}
	
	
	@Test (expected = IllegalArgumentException.class)
	public void testConstructorCuandoEstadoNull() {
		new Estandar(ID_CONTENEDOR_WIKI, DIEZ, DIEZ, UnidadesPeso.KILOGRAMOS,
						DIEZ, UnidadesVolumen.METROS_CUBICOS, null);
	}
	
	
	@Test
	public void testClone() {
		inicializaValores();
		Contenedor copia = contenedor.clone();
		assertNotSame(copia, contenedor);
		assertEquals(copia.getIdentificador(), ID_CONTENEDOR_WIKI);
		assertEquals(copia.getMaximaCargaUtilKilogramos(), DIEZ, 0.001);
		assertEquals(copia.getPesoTaraKilogramos(), DIEZ, 0.001);
		assertEquals(copia.getVolumenMetrosCubicos(), DIEZ, 0.001);
		assertEquals(copia.getEstado(), EstadoContenedor.RECOGIDA);
		assertTrue(copia.tieneTecho());
	}
	
	
	@Test (expected = IllegalArgumentException.class)
	public void testConstructorLaCadenaNoSeEncuentraEnUnRangoDeCaracteresValido() {
	    new Estandar("@AAJ1234567", DIEZ, DIEZ, UnidadesPeso.KILOGRAMOS,
				DIEZ, UnidadesVolumen.METROS_CUBICOS, EstadoContenedor.RECOGIDA);
	} 

	
	@Test
	public void testSetEstadoRecogida() {
		inicializaValores();
		contenedor.setEstadoRecogida();
		assertEquals(contenedor.getEstado(), EstadoContenedor.RECOGIDA);
	}
	
	
	@Test
	public void testSetEstadoTransito() {
		inicializaValores();
		contenedor.setEstadoTransito();
		assertEquals(contenedor.getEstado(), EstadoContenedor.TRANSITO);
	}
	
	
	@Test
	public void testEqualsCuandoDosContenedorSonIguales() {
		contenedor = new Estandar(ID_CONTENEDOR_WIKI, 10, 10, UnidadesPeso.KILOGRAMOS,
				10, UnidadesVolumen.METROS_CUBICOS, EstadoContenedor.RECOGIDA);
		Contenedor copia = contenedor.clone();
		assertTrue(contenedor.equals(copia));
	}
	
	
	@Test
	public void testEqualsCuandoDosContenedorNoLoSon() {
		contenedor = new Estandar(ID_CONTENEDOR_WIKI, 10, 10, UnidadesPeso.KILOGRAMOS,
				10, UnidadesVolumen.METROS_CUBICOS, EstadoContenedor.RECOGIDA);
		Contenedor copia = new Estandar(OTRO_ID_CONTENEDOR, 10, 10, UnidadesPeso.KILOGRAMOS,
				10, UnidadesVolumen.METROS_CUBICOS, EstadoContenedor.RECOGIDA);
		assertFalse(contenedor.equals(copia));
	}
	
	@Test
	public void testEqualsConNull() {
		contenedor = new Estandar(ID_CONTENEDOR_WIKI, 10, 20, UnidadesPeso.KILOGRAMOS, 10, UnidadesVolumen.METROS_CUBICOS, EstadoContenedor.RECOGIDA);
	    assertFalse(contenedor.equals(null)); 
	}
	
	@Test
	public void testEqualsConClaseDiferente() {
	    contenedor = new Estandar(ID_CONTENEDOR_WIKI, 10, 10, UnidadesPeso.KILOGRAMOS,
				10, UnidadesVolumen.METROS_CUBICOS, EstadoContenedor.RECOGIDA);
	    Contenedor otroContenedor = new FlatRack(OTRO_ID_CONTENEDOR, 10, 10, UnidadesPeso.KILOGRAMOS,
				10, UnidadesVolumen.METROS_CUBICOS, EstadoContenedor.RECOGIDA);
	    assertFalse(contenedor.equals(otroContenedor)); 
	}


	
	@Test
	public void testAgregaTrayectoValido() {
		inicializaValores();
		contenedor = new Estandar(ID_CONTENEDOR_WIKI, DIEZ, DIEZ, UnidadesPeso.KILOGRAMOS,
				DIEZ, UnidadesVolumen.METROS_CUBICOS, EstadoContenedor.RECOGIDA);
		contenedor.agregaTrayecto(trayecto1);
		contenedor.agregaTrayecto(trayecto2);
		contenedor.agregaTrayecto(trayecto3);
		ArrayList<Trayecto> trayectosGuardados = contenedor.getTrayectos();
		
		assertEquals(trayectosGuardados.size(), 3);
		assertNotNull(trayectosGuardados.get(0));
		assertNotNull(trayectosGuardados.get(1));
		assertEquals(trayectosGuardados.get(0).getPuertoOrigen().getIdentificador(), puertoOrigen1.getIdentificador());
		assertEquals(trayectosGuardados.get(0).getPuertoDestino().getIdentificador(), puertoDestino1.getIdentificador());
		assertEquals(trayectosGuardados.get(1).getPuertoOrigen().getIdentificador(), puertoOrigen2.getIdentificador());
		assertEquals(trayectosGuardados.get(1).getPuertoDestino().getIdentificador(), puertoDestino2.getIdentificador());
		assertEquals(trayectosGuardados.get(2).getPuertoOrigen().getIdentificador(), puertoOrigen3.getIdentificador());
		assertEquals(trayectosGuardados.get(2).getPuertoDestino().getIdentificador(), puertoDestino3.getIdentificador());
	
	}
	
	
	@Test (expected = IllegalArgumentException.class)
	public void testAgregaTrayectoNulo() {
		inicializaValores();
		contenedor.agregaTrayecto(null);
	}
	
	
	@Test (expected = IllegalArgumentException.class)
	public void testAgregaTrayectoyaIntroducido() {
		inicializaValores();
		contenedor.agregaTrayecto(trayecto1);
		contenedor.agregaTrayecto(trayecto1);
	}
	
	
	@Test
	public void testIniciaNuevoViaje() {
		inicializaValores();
		contenedor = new Estandar(ID_CONTENEDOR_WIKI, DIEZ, DIEZ, UnidadesPeso.KILOGRAMOS,
				DIEZ, UnidadesVolumen.METROS_CUBICOS, EstadoContenedor.RECOGIDA);
		contenedor.agregaTrayecto(trayecto1);
		contenedor.agregaTrayecto(trayecto2);
		contenedor.agregaTrayecto(trayecto3);
		contenedor.iniciaNuevoViaje();
		assertEquals(contenedor.getTrayectos().size(), 0);
	}

	@Test
	public void testCalcularPrecioTotalTransporte() {
	    inicializaValores();
	    double precioTotalEsperado = 4000 * (trayecto1.getFechaInicio().until(trayecto1.getFechaFin()).getDays()) + 
	                                 20 + 12.5 * (trayecto2.distanciaKilometros()) + 
	                                 200 + 4.5 * (trayecto3.distanciaKilometros());
	    assertEquals(precioTotalEsperado, contenedor.calculaPrecioTotalTransporte(), 0.01);
	}

	@Test
	public void testUsaBarcoConTrayectoDeBarco() {
	    inicializaValores();
	    contenedor = new Estandar(ID_CONTENEDOR_WIKI, DIEZ, DIEZ, UnidadesPeso.KILOGRAMOS,
				DIEZ, UnidadesVolumen.METROS_CUBICOS, EstadoContenedor.RECOGIDA);
	    contenedor.agregaTrayecto(trayecto1);
	    assertTrue(contenedor.usaBarco());
	}

	@Test
	public void testUsaBarcoSinTrayectoDeBarco() {
	    inicializaValores();
	    contenedor = new Estandar(ID_CONTENEDOR_WIKI, DIEZ, DIEZ, UnidadesPeso.KILOGRAMOS,
				DIEZ, UnidadesVolumen.METROS_CUBICOS, EstadoContenedor.RECOGIDA);
	    contenedor.agregaTrayecto(trayecto2); 
	    contenedor.agregaTrayecto(trayecto3); 
	    assertFalse(contenedor.usaBarco());
	}

	@Test
	public void testUsaTrenConTrayectoDeTren() {
	    inicializaValores();
	    contenedor = new Estandar(ID_CONTENEDOR_WIKI, DIEZ, DIEZ, UnidadesPeso.KILOGRAMOS,
				DIEZ, UnidadesVolumen.METROS_CUBICOS, EstadoContenedor.RECOGIDA);
	    contenedor.agregaTrayecto(trayecto2); 
	    assertTrue(contenedor.usaTren());
	}

	@Test
	public void testUsaTrenSinTrayectoDeTren() {
	    inicializaValores();
	    contenedor = new Estandar(ID_CONTENEDOR_WIKI, DIEZ, DIEZ, UnidadesPeso.KILOGRAMOS,
				DIEZ, UnidadesVolumen.METROS_CUBICOS, EstadoContenedor.RECOGIDA);
	    contenedor.agregaTrayecto(trayecto1);
	    contenedor.agregaTrayecto(trayecto3); 
	    assertFalse(contenedor.usaTren());
	}

	
	@Test
	public void testCuentaTrayectos() {
		inicializaValores();
		contenedor = new Estandar(ID_CONTENEDOR_WIKI, DIEZ, DIEZ, UnidadesPeso.KILOGRAMOS,
				DIEZ, UnidadesVolumen.METROS_CUBICOS, EstadoContenedor.RECOGIDA);
		contenedor.agregaTrayecto(trayecto1);
		contenedor.agregaTrayecto(trayecto2);
		assertEquals(contenedor.cuentaTrayectos(), 2);
	}
	
	
	@Test
	public void testIsEnTransitoCuandoLoEsta() {
		inicializaValores();
		contenedor.setEstadoTransito();
		assertTrue(contenedor.isEnTransito());
	}
	
	
	@Test
	public void testIsEnTransitoCuandoEstaEnRecogida() {
		inicializaValores();
		assertFalse(contenedor.isEnTransito());
	}
	
	
	@Test
	public void testIsEnRecogidaCuandoLoEsta() {
		inicializaValores();
		assertTrue(contenedor.isEnRecogida());
	}
	
	
	@Test
	public void testIsEnRecogidaCuandoEstaEnTransito() {
		inicializaValores();
		contenedor.setEstadoTransito();
		assertFalse(contenedor.isEnRecogida());
	}
	
	@Test
	public void testSetNoTieneTechoConTecho() {
	    Estandar contenedorEstandar = new Estandar(ID_CONTENEDOR_WIKI, DIEZ, DIEZ, UnidadesPeso.KILOGRAMOS,
				DIEZ, UnidadesVolumen.METROS_CUBICOS, EstadoContenedor.RECOGIDA);
	    contenedorEstandar.setNoTieneTecho();
	    assertFalse(contenedorEstandar.tieneTecho());
	}
	
	@Test(expected = IllegalStateException.class)
	public void testSetNoTieneTechoYaSinTecho() {
	    Estandar contenedorEstandar = new Estandar(ID_CONTENEDOR_WIKI, DIEZ, DIEZ, UnidadesPeso.KILOGRAMOS,
	                DIEZ, UnidadesVolumen.METROS_CUBICOS, EstadoContenedor.RECOGIDA);
	    contenedorEstandar.setNoTieneTecho();
	    contenedorEstandar.setNoTieneTecho();
	}
	
	@Test
	public void testSetTieneTechoSinTecho() {
	    Estandar contenedorEstandar = new Estandar(ID_CONTENEDOR_WIKI, DIEZ, DIEZ, UnidadesPeso.KILOGRAMOS,
	                DIEZ, UnidadesVolumen.METROS_CUBICOS, EstadoContenedor.RECOGIDA);
	    contenedorEstandar.setNoTieneTecho();
	    contenedorEstandar.setTieneTecho();
	    assertTrue(contenedorEstandar.tieneTecho());
	}
	
	@Test(expected = IllegalStateException.class)
	public void testSetTieneTechoYaConTecho() {
	    Estandar contenedorEstandar = new Estandar(ID_CONTENEDOR_WIKI, DIEZ, DIEZ, UnidadesPeso.KILOGRAMOS,
	                DIEZ, UnidadesVolumen.METROS_CUBICOS, EstadoContenedor.RECOGIDA);
	    contenedorEstandar.setTieneTecho();
	}
	

}
