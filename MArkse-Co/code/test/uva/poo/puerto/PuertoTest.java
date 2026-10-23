package uva.poo.puerto;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.ArrayList;
import es.uva.inf.poo.maps.GPSCoordinate;
import uva.poo.contenedor.Contenedor;
import uva.poo.contenedor.EstadoContenedor;
import uva.poo.contenedor.Estandar;
import uva.poo.contenedor.UnidadesPeso;
import uva.poo.contenedor.UnidadesVolumen;


/**
 * Prueba la clase Puerto
 * @author Marta Pérez
 * @author Emily Rodrigues
 */
public class PuertoTest {
	
	private static final String ID_MUELLE = "32";
	private static final String ID_MUELLE_FULL = "22";
	private static final String ID_CONTENEDOR_WIKI = "CSQU3054383";
	private static final String ID_PUERTO = "AU-RTZ";
	
	private static final GPSCoordinate PUNTO_GPS = new GPSCoordinate(80,80);
	
	private static final int NUMERO_PLAZAS = 30;
	private static final int CANTIDAD_MAX_CONTENEDORES = 50;
	
	private static final EstadoMuelle ESTADO_OPERATIVO = EstadoMuelle.OPERATIVO;
	private static final EstadoMuelle ESTADO_FUERA_SERVICIO = EstadoMuelle.FUERA_DE_SERVICIO;
	
	private static final int DIEZ = 10;
	
	private Muelle muelle1;
	private Muelle muelleFull;
	
	private Puerto puerto;
	
	Contenedor contenedor;
	
	private void startUp() {
		ArrayList<TipoTransporte> aceptaTodosLosTransportes = new ArrayList<>();
		aceptaTodosLosTransportes.add(TipoTransporte.BARCO);
		aceptaTodosLosTransportes.add(TipoTransporte.CAMION);
		aceptaTodosLosTransportes.add(TipoTransporte.TREN);
		
		muelle1 = new Muelle(ID_MUELLE, PUNTO_GPS, ESTADO_FUERA_SERVICIO, NUMERO_PLAZAS, CANTIDAD_MAX_CONTENEDORES, aceptaTodosLosTransportes);
		muelleFull = new Muelle(ID_MUELLE_FULL, new GPSCoordinate(60,60), ESTADO_OPERATIVO, 1, 1, aceptaTodosLosTransportes);
		puerto = new Puerto(ID_PUERTO);
		contenedor = new Estandar(ID_CONTENEDOR_WIKI, DIEZ, DIEZ, UnidadesPeso.KILOGRAMOS,
				DIEZ, UnidadesVolumen.METROS_CUBICOS, EstadoContenedor.RECOGIDA);
		
	}
	
	@Test
	public void testConstructorConUnIdValido() {
		startUp();
		assertEquals(puerto.getIdentificador(), ID_PUERTO);
		assertEquals(puerto.getMuelles().size(), 0);
		assertFalse(puerto.isOperativo());
		assertFalse(puerto.isFueraServicio());
		assertEquals(puerto.getNumeroMuellesConEspacio(), 0);
		assertEquals(puerto.getNumeroMuellesFueraServicio(), 0);
		assertEquals(puerto.getNumeroMuellesOperativos(), 0);
		assertEquals(puerto.getNumeroMuellesSinEspacio(), 0);
		assertEquals(puerto.getMuellesConEspacio().size(), 0);
		assertEquals(puerto.getMuellesFueraServicio().size(), 0);
		assertEquals(puerto.getMuellesOperativos().size(), 0);
		assertEquals(puerto.getMuellesSinEspacio().size(), 0);
	}
	
	
	@Test(expected=IllegalArgumentException.class)
	public void testConstructorConUnIdNull() {
		String cadena = null;
		new Puerto(cadena);
	}
	
	
	@Test(expected=IllegalArgumentException.class)
	public void testConstructorConUnIdLongitudCero() {
		String cadena = "";
		new Puerto(cadena);
	}
	
	
	@Test(expected=IllegalArgumentException.class)
	public void testConstructorConUnIdLongitudSiete() {
		String cadena = "AU-RTZY";
		new Puerto(cadena);
	}
	
	
	@Test(expected=IllegalArgumentException.class)
	public void testConstructorConUnIdSinGuion() {
		String cadena = "AUYRTZ";
		new Puerto(cadena);
	}
	
	
	@Test(expected=IllegalArgumentException.class)
	public void testConstructorConUnIdGuionEnOtraPosicion() {
		String cadena = "A-URTZ";
		new Puerto(cadena);
	}
	
	
	@Test(expected=IllegalArgumentException.class)
	public void testConstructorConUnIdConCaracterNoValidoPorDebajoRango() {
		String cadena = "AU-3TZ";
		new Puerto(cadena);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testConstructorConUnIdConCaracterNoValidoPorEncimaRango() {
		String cadena = "AU-aTZ";
		new Puerto(cadena);
	}
	
	@Test
	public void testClone() {
		startUp();
		Puerto copia = puerto.clone();
		assertNotSame(puerto, copia);
		assertEquals(copia.getIdentificador(), ID_PUERTO);
		assertEquals(copia.getMuelles().size(), 0);
		assertFalse(copia.isOperativo());
		assertFalse(copia.isFueraServicio());
		assertEquals(copia.getNumeroMuellesConEspacio(), 0);
		assertEquals(copia.getNumeroMuellesFueraServicio(), 0);
		assertEquals(copia.getNumeroMuellesOperativos(), 0);
		assertEquals(copia.getNumeroMuellesSinEspacio(), 0);
		assertEquals(copia.getMuellesConEspacio().size(), 0);
		assertEquals(copia.getMuellesFueraServicio().size(), 0);
		assertEquals(copia.getMuellesOperativos().size(), 0);
		assertEquals(copia.getMuellesSinEspacio().size(), 0);
	}
	
	
	@Test
	public void testAgregaMuelleValido() {
		startUp();
		puerto.agregaMuelle(muelle1);
		muelleFull.asignarContenedorAPlaza(contenedor);
		puerto.agregaMuelle(muelleFull);
		
		assertEquals(puerto.getMuelles().size(), 2);
		assertEquals(puerto.getMuellesConEspacio().size(), 1);
		assertEquals(puerto.getMuellesSinEspacio().size(), 1);
		assertEquals(puerto.getMuellesOperativos().size(), 1);
		assertEquals(puerto.getMuellesFueraServicio().size(), 1);
		assertEquals(puerto.getNumeroMuellesConEspacio(), 1);
		assertEquals(puerto.getNumeroMuellesFueraServicio(), 1);
		assertEquals(puerto.getNumeroMuellesOperativos(), 1);
		assertEquals(puerto.getNumeroMuellesSinEspacio(), 1);
	}
	
	
	@Test (expected = IllegalArgumentException.class)
	public void testAgregaMuelleNulo() {
		startUp();
		puerto.agregaMuelle(null);
	}
	
	
	@Test (expected = IllegalArgumentException.class)
	public void testAgregaMuelleCuandoYaSeHabiaAgregado() {
		startUp();
		puerto.agregaMuelle(muelle1);
		puerto.agregaMuelle(muelle1);
	}
	
	
	@Test
	public void testIsMuelleEnPuertoCuandoLoEsta() {
		startUp();
		puerto.agregaMuelle(muelle1);
		assertTrue(puerto.isMuelleEnPuerto(ID_MUELLE));
	}
	
	@Test
	public void testIsMuelleEnPuertoCuandoNoLoEsta() {
		startUp();
		puerto.agregaMuelle(muelleFull);
		assertFalse(puerto.isMuelleEnPuerto(ID_MUELLE));
	}
	
	
	@Test (expected = IllegalArgumentException.class)
	public void testIsMuelleEnPuertoCuandoIdMuelleEsNulo() {
		startUp();
		puerto.agregaMuelle(muelleFull);
		puerto.isMuelleEnPuerto(null);
	}
	
	
	@Test
	public void testEliminaMuelleCuandoEstaElMuelle() {
		startUp();
		puerto.agregaMuelle(muelleFull);
		puerto.eliminaMuelle(ID_MUELLE_FULL);
		
		assertEquals(puerto.getMuelles().size(), 0);
		assertEquals(puerto.getMuellesSinEspacio().size(), 0);
		assertEquals(puerto.getNumeroMuellesSinEspacio(), 0);
		assertEquals(puerto.getNumeroMuellesOperativos(), 0);
		assertEquals(puerto.getMuellesOperativos().size(), 0);
	}
	
	
	@Test
	public void testEliminaTodosLosMuelles() {
		startUp();
		puerto.agregaMuelle(muelleFull);
		puerto.agregaMuelle(muelle1);
		
		puerto.eliminaTodosLosMuelles();
		
		assertEquals(puerto.getMuelles().size(), 0);
		assertEquals(puerto.getMuellesSinEspacio().size(), 0);
		assertEquals(puerto.getNumeroMuellesSinEspacio(), 0);
		assertEquals(puerto.getNumeroMuellesOperativos(), 0);
		assertEquals(puerto.getMuellesOperativos().size(), 0);
	}
	
	
	@Test (expected = IllegalArgumentException.class)
	public void testEliminaMuelleCuandoElIdDelMuelleEsNulo() {
		startUp();
		puerto.agregaMuelle(muelleFull);
		puerto.eliminaMuelle(null);
	}
	
	
	@Test (expected = IllegalStateException.class)
	public void testEliminaMuelleCuandoNoEstaElMuelle() {
		startUp();
		puerto.agregaMuelle(muelleFull);
		puerto.eliminaMuelle(ID_MUELLE);
	}
	
	
	@Test
	public void testIsCompletoCuandoNoHayMuelles() {
		startUp();
		assertTrue(puerto.isCompleto());
	}
	
	
	@Test
	public void testIsCompletoCuandoLoEsta() {	
		startUp();
		muelleFull.asignarContenedorAPlaza(contenedor);
		puerto.agregaMuelle(muelleFull);
		assertTrue(puerto.isCompleto());
	}
	
	
	@Test
	public void testIsCompletoCuandoNoLoEsta() {	
		startUp();
		puerto.agregaMuelle(muelle1);
		assertFalse(puerto.isCompleto());
	}
	
	
	@Test
	public void testEqualsCuandoDosPuertosLoSon() {
		startUp();
		Puerto copia = puerto.clone();
		assertTrue(puerto.equals(copia));
	}
	
	
	@Test
	public void testEqualsCuandoDosPuertosNoLoSon() {
		startUp();
		Puerto copia = new Puerto("AU-RZZ");
		assertFalse(puerto.equals(copia));
	}
	
	
	@Test
	public void testIsOperativoCuandoLoEstaPorQueTieneUnMuelle() {
		startUp();
		puerto.agregaMuelle(muelleFull);
		assertTrue(puerto.isOperativo());
	}
	
	
	@Test
	public void testIsOperativoCuandoNoLoEsta() {
		startUp();
		muelle1.setFueraDeServicio();
		muelleFull.setFueraDeServicio();
		puerto.agregaMuelle(muelle1);
		puerto.agregaMuelle(muelleFull);
		assertFalse(puerto.isOperativo());
	}
	
	
	@Test
	public void testIsFueraServicioCuandoNoLoEsta() {
		startUp();
		muelle1.setOperativo();
		puerto.agregaMuelle(muelleFull);
		puerto.agregaMuelle(muelle1);
		assertFalse(puerto.isFueraServicio());
	}
	
	
	@Test
	public void testIsFueraServicioCuandoLoEsta() {
		startUp();
		muelleFull.setFueraDeServicio();
		puerto.agregaMuelle(muelleFull);
		puerto.agregaMuelle(muelle1);
		assertTrue(puerto.isFueraServicio());
	}
	
	
	@Test
	public void tesMuellesADistanciaInferiorConValoresValidosYUnMuelle() {
		startUp();
		GPSCoordinate coordenada = new GPSCoordinate(80, 85);
		puerto.agregaMuelle(muelle1);
		puerto.agregaMuelle(muelleFull);
		ArrayList<Muelle> muellesADistanciaInferior = puerto.muellesADistanciaInferior(coordenada, 100);
		
		assertEquals(muellesADistanciaInferior.size(), 1);
		assertEquals(muellesADistanciaInferior.get(0).getIdMuelle(), ID_MUELLE);
	}
	
	
	@Test
	public void tesMuellesADistanciaInferiorConDistanciaCero() {
		startUp();
		GPSCoordinate coordenada = new GPSCoordinate(80, 85);
		puerto.agregaMuelle(muelle1);
		puerto.agregaMuelle(muelleFull);
		ArrayList<Muelle> muellesADistanciaInferior = puerto.muellesADistanciaInferior(coordenada, 0);
		
		assertEquals(muellesADistanciaInferior.size(), 0);
	}
	
	
	@Test (expected = IllegalArgumentException.class)
	public void tesMuellesADistanciaInferiorCuandoDistanciaEsJustoMenorQueElLimite() {
		startUp();
		GPSCoordinate coordenada = new GPSCoordinate(80, 85);
		puerto.agregaMuelle(muelle1);
		puerto.agregaMuelle(muelleFull);
		puerto.muellesADistanciaInferior(coordenada, -0.001);
	}
	
	
	@Test (expected = IllegalArgumentException.class)
	public void tesMuellesADistanciaInferiorCuandoDistanciaEsMenorQueElLimite() {
		startUp();
		GPSCoordinate coordenada = new GPSCoordinate(80, 85);
		puerto.agregaMuelle(muelle1);
		puerto.agregaMuelle(muelleFull);
		puerto.muellesADistanciaInferior(coordenada, -DIEZ);
	}
	
	
	@Test (expected = IllegalArgumentException.class)
	public void tesMuellesADistanciaInferiorCuandoLaCoordenadaEsNull() {
		startUp();
		puerto.agregaMuelle(muelle1);
		puerto.agregaMuelle(muelleFull);
		puerto.muellesADistanciaInferior(null, DIEZ);
	}
	
	
	@Test
	public void testIsMuelleOperativoCuandoLoEsta() {
		startUp();
		muelle1.setOperativo();
		puerto.agregaMuelle(muelle1);
		assertTrue(puerto.isMuelleOperativo(ID_MUELLE));
	}
	
	
	@Test
	public void testIsMuelleOperativoCuandoUnMuelleLoEstaYElOtroNo() {
		startUp();
		muelle1.setOperativo();
		puerto.agregaMuelle(muelle1);
		muelleFull.setFueraDeServicio();
		puerto.agregaMuelle(muelleFull);
		assertTrue(puerto.isMuelleOperativo(ID_MUELLE));
		assertFalse(puerto.isMuelleOperativo(ID_MUELLE_FULL));
	}
	
	@Test
	public void testIsMuelleFueraServicioCuandoLoEsta() {
		startUp();
		puerto.agregaMuelle(muelle1);
		assertTrue(puerto.isMuelleFueraServicio(ID_MUELLE));
	}
	
	
	@Test
	public void testIsMuelleFueraServicioCuandoNoLoEsta() {
		startUp();
		puerto.agregaMuelle(muelleFull);
		assertFalse(puerto.isMuelleFueraServicio(ID_MUELLE_FULL));
	}
	
	
	@Test (expected = IllegalArgumentException.class)
	public void testIsMuelleFueraServicioCuandoElMuelleNoEstaEnElPuerto() {
		startUp();
		puerto.agregaMuelle(muelle1);
		puerto.isMuelleFueraServicio(ID_MUELLE_FULL);
	}
	
	
	@Test (expected = IllegalArgumentException.class)
	public void testIsMuelleFueraServicioCuandoElIdEsNull() {
		startUp();
		puerto.agregaMuelle(muelle1);
		puerto.isMuelleFueraServicio(null);
	}
	
	
	@Test
	public void testEstaContenedorCuandoLoEsta() {
		startUp();
		muelle1.setOperativo();
		puerto.agregaMuelle(muelle1);
		puerto.agregarContenedor(contenedor);
		assertTrue(puerto.estaContenedor(ID_CONTENEDOR_WIKI));
	}
	
	
	@Test
	public void testEstaContenedorCuandoNoLoEsta() {
		startUp();
		puerto.agregaMuelle(muelle1);
		assertFalse(puerto.estaContenedor(ID_CONTENEDOR_WIKI));
	}
	
	
	@Test (expected = IllegalArgumentException.class)
	public void testEstaContenedorCuandoIdContenedorEsNull() {
		startUp();
		muelle1.setOperativo();
		puerto.agregaMuelle(muelle1);
		puerto.estaContenedor(null);
	}
	
	
	@Test (expected = IllegalStateException.class)
	public void testAgregarContenedorQueYaEstaEnUnMuelle() {
		startUp();
		muelle1.setOperativo();
		puerto.agregaMuelle(muelle1);
		puerto.agregarContenedor(contenedor);
		puerto.agregarContenedor(contenedor);
	}
	
	
	@Test (expected = IllegalStateException.class)
	public void testAgregarContenedorCuandoNoHayEspacio() {
		startUp();
		puerto.agregaMuelle(muelleFull);
		puerto.agregarContenedor(contenedor);
		Contenedor otro = new Estandar("AZXZ0091238", 10, 10, UnidadesPeso.KILOGRAMOS,
				10, UnidadesVolumen.METROS_CUBICOS, EstadoContenedor.TRANSITO);
		puerto.agregarContenedor(otro);
	}
	
	
	@Test (expected = IllegalArgumentException.class)
	public void testAgregarContenedorNull() {
		startUp();
		muelle1.setOperativo();
		puerto.agregaMuelle(muelle1);
		puerto.agregarContenedor(null);
	}
	
	
	@Test (expected = IllegalStateException.class)
	public void testAgregarContenedorCuandoPuertoEstaFueraDeServicio() {
		startUp();
		puerto.agregaMuelle(muelle1);
		puerto.agregarContenedor(contenedor);
	}
	
	
	@Test (expected = IllegalArgumentException.class)
	public void testEqualsCuandoOtroEsNull() {
		startUp();
		puerto.equals(null);
	}
	
	
	@Test
	public void testCloneCuandoElPuertoTieneVariosMuelles() {
		startUp();
		puerto.agregaMuelle(muelle1);
		puerto.agregaMuelle(muelleFull);
		
		Puerto copia = puerto.clone();
		assertNotNull(copia);
		assertEquals(copia, puerto);
		assertNotSame(copia, puerto);
	}
	
	
	@Test
	public void testQuitarContenedorCuandoEsta() {
		startUp();
		muelle1.setOperativo();
		puerto.agregaMuelle(muelle1);
		puerto.agregarContenedor(contenedor);
		puerto.quitarContenedor(ID_CONTENEDOR_WIKI);
		
		assertFalse(puerto.estaContenedor(ID_CONTENEDOR_WIKI));
	}
	
	
	@Test (expected = IllegalStateException.class)
	public void testQuitarContenedorCuandoNoEsta() {
		startUp();
		puerto.agregaMuelle(muelle1);
		puerto.agregarContenedor(contenedor);
		puerto.quitarContenedor("AZXZ0091238");
	}
	
	
	@Test (expected = IllegalArgumentException.class)
	public void testQuitarContenedorCuandoIdEsNull() {
		startUp();
		muelle1.setOperativo();
		puerto.agregaMuelle(muelle1);
		puerto.agregarContenedor(contenedor);
		puerto.quitarContenedor(null);
	}
	
	
	@Test (expected = IllegalStateException.class)
	public void testQuitarContenedorCuandoPuertoFueraDeServicio() {
		startUp();
		muelle1.setOperativo();
		puerto.agregaMuelle(muelle1);
		puerto.agregarContenedor(contenedor);
		puerto.setMuelleFueraServicio(ID_MUELLE);
		puerto.quitarContenedor(ID_CONTENEDOR_WIKI);
	}
	
	
	@Test (expected = IllegalArgumentException.class)
	public void testSetMuelleFueraServicioCuandoIdMuelleEsNull() {
		startUp();
		puerto.agregaMuelle(muelleFull);
		puerto.setMuelleFueraServicio(null);
	}
	
	
	@Test (expected = IllegalStateException.class)
	public void testSetMuelleFueraServicioCuandoIdMuelleNoEsta() {
		startUp();
		puerto.agregaMuelle(muelle1);
		puerto.setMuelleFueraServicio(ID_MUELLE_FULL);
	}
	
	
	@Test
	public void testSetMuelleOperativoCuandoEstaMuelle() {
		startUp();
		puerto.agregaMuelle(muelle1);
		puerto.setMuelleOperativo(ID_MUELLE);
		assertTrue(puerto.isMuelleOperativo(ID_MUELLE));
	}
	
	
	@Test (expected = IllegalArgumentException.class)
	public void testSetMuelleOperativoCuandoIdMuelleNull() {
		startUp();
		puerto.agregaMuelle(muelleFull);
		puerto.setMuelleOperativo(null);
	}
	
}