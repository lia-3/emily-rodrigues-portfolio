package uva.poo.puerto;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.ArrayList;
import es.uva.inf.poo.maps.GPSCoordinate;
import uva.poo.contenedor.Contenedor;
import uva.poo.contenedor.EstadoContenedor;
import uva.poo.contenedor.Estandar;
import uva.poo.contenedor.FlatRack;
import uva.poo.contenedor.Refrigerado;
import uva.poo.contenedor.UnidadesPeso;
import uva.poo.contenedor.UnidadesVolumen;


/**
 * Prueba la clase Muelle
 * @author Marta Pérez
 * @author Emily Rodrigues
 */
public class MuelleTest {

	private static final String ID_MUELLE = "32";
	private static final GPSCoordinate PUNTO_GPS = new GPSCoordinate(80,80);
	private static final int NUMERO_PLAZAS = 30;
	private static final int CANTIDAD_MAX_CONTENEDORES = 50;
	private static final EstadoMuelle ESTADO_OPERATIVO = EstadoMuelle.OPERATIVO;
	private static final EstadoMuelle ESTADO_NO_OPERATIVO = EstadoMuelle.FUERA_DE_SERVICIO;
	private static final Contenedor CONTENEDOR_VALIDO = new Estandar("CSQU3054383", 5000.0, 20000.0, UnidadesPeso.KILOGRAMOS, 33.0, UnidadesVolumen.METROS_CUBICOS, EstadoContenedor.RECOGIDA);               
	private static final Contenedor OTRO_CONTENEDOR_VALIDO = new FlatRack("AZXJ0091237", 4000.0, 30000.0, UnidadesPeso.KILOGRAMOS, 33.0, UnidadesVolumen.METROS_CUBICOS, EstadoContenedor.RECOGIDA);               
	private static final Contenedor OTRO_CONTENEDOR_VALIDO_MAS = new Refrigerado("AZXZ0091238", 4000.0, 30000.0, UnidadesPeso.KILOGRAMOS, 33.0, UnidadesVolumen.METROS_CUBICOS, EstadoContenedor.RECOGIDA);               
	private ArrayList<TipoTransporte> aceptaTodosLosTransportes;
	private ArrayList<TipoTransporte> transportesNoAceptados;
	
	private Muelle muelle;
	
	private void inicializaValores() {
		aceptaTodosLosTransportes = new ArrayList<>();
		aceptaTodosLosTransportes.add(TipoTransporte.BARCO);
		aceptaTodosLosTransportes.add(TipoTransporte.CAMION);
		aceptaTodosLosTransportes.add(TipoTransporte.TREN);
		
		transportesNoAceptados = new ArrayList<>();
		muelle = new Muelle(ID_MUELLE, PUNTO_GPS, ESTADO_OPERATIVO, NUMERO_PLAZAS, CANTIDAD_MAX_CONTENEDORES, aceptaTodosLosTransportes);
	}
	
	@Test
    public void testConstructorValido() {
		inicializaValores();
        assertEquals(ID_MUELLE, muelle.getIdMuelle());
        assertEquals(PUNTO_GPS, muelle.getPuntoGPS());
        assertEquals(ESTADO_OPERATIVO, muelle.getEstado());
        assertTrue(muelle.isOperativo());
        assertFalse(muelle.isFueraDeServicio());
        assertEquals(NUMERO_PLAZAS, muelle.getNumeroPlazas());
        assertEquals(CANTIDAD_MAX_CONTENEDORES, muelle.getCantidadMaxContenedores());
        assertTrue(muelle.permiteBarco());
        assertTrue(muelle.permiteCamion());
        assertTrue(muelle.permiteTren());
    }
	
	@Test(expected = IllegalArgumentException.class)
    public void testConstructorIDMuelleNullNoValido() {
		inicializaValores();
		new Muelle(null, PUNTO_GPS, ESTADO_OPERATIVO, NUMERO_PLAZAS, CANTIDAD_MAX_CONTENEDORES, aceptaTodosLosTransportes);
    }
	
	@Test(expected = IllegalArgumentException.class)
    public void testConstructorIDMuelleNoSonDigitosNoValido() {
		inicializaValores();
        new Muelle("1a", PUNTO_GPS, ESTADO_OPERATIVO, NUMERO_PLAZAS, CANTIDAD_MAX_CONTENEDORES, aceptaTodosLosTransportes);
    }
	
	@Test(expected = IllegalArgumentException.class)
    public void testConstructorIDMuelleExcedeLongitudNoValido() {
		inicializaValores();
        new Muelle("1234", PUNTO_GPS, ESTADO_OPERATIVO, NUMERO_PLAZAS, CANTIDAD_MAX_CONTENEDORES, aceptaTodosLosTransportes);
    }
	
	@Test(expected = IllegalArgumentException.class)
    public void testConstructorPuntoGPSNullNoValido() {
		inicializaValores();
        new Muelle(ID_MUELLE, null, ESTADO_OPERATIVO, NUMERO_PLAZAS, CANTIDAD_MAX_CONTENEDORES, aceptaTodosLosTransportes);
    }
	
	@Test(expected = IllegalArgumentException.class)
    public void testConstructorEstadoNullNoValido() {
		inicializaValores();
        new Muelle(ID_MUELLE, PUNTO_GPS, null, NUMERO_PLAZAS, CANTIDAD_MAX_CONTENEDORES, aceptaTodosLosTransportes);
    }
	
	@Test(expected = IllegalArgumentException.class)
    public void testConstructorNumeroPlazasNegativoNoValido() {
		inicializaValores();
        new Muelle(ID_MUELLE, PUNTO_GPS, ESTADO_OPERATIVO, -4, CANTIDAD_MAX_CONTENEDORES, aceptaTodosLosTransportes);
    }
	
	@Test(expected = IllegalArgumentException.class)
    public void testConstructorCantidadMaxContenedoresNegativoNoValido() {
		inicializaValores();
        new Muelle(ID_MUELLE, PUNTO_GPS, ESTADO_OPERATIVO, NUMERO_PLAZAS, -4, aceptaTodosLosTransportes);
    }
	
	@Test
	public void testConstructorMuelleConContenedores() {
		inicializaValores();
	    ArrayList<Contenedor> contenedores = new ArrayList<>();
	    contenedores.add(OTRO_CONTENEDOR_VALIDO);
	    contenedores.add(CONTENEDOR_VALIDO);

	    Muelle otroMuelle = new Muelle(ID_MUELLE, PUNTO_GPS, ESTADO_OPERATIVO, NUMERO_PLAZAS, CANTIDAD_MAX_CONTENEDORES, aceptaTodosLosTransportes);
	    Muelle muelleCopia = otroMuelle.clone();

	    assertNotNull(muelleCopia);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testConstructorCuandoTransportesAceptadosNull() {
		inicializaValores();
	    new Muelle(ID_MUELLE, PUNTO_GPS, ESTADO_OPERATIVO, NUMERO_PLAZAS, CANTIDAD_MAX_CONTENEDORES, null);
	}
	
	
	@Test (expected = IllegalArgumentException.class)
	public void testConstructorCuandoTransportesAceptadosContieneNull() {
		inicializaValores();
		transportesNoAceptados.add(null);
	    new Muelle(ID_MUELLE, PUNTO_GPS, ESTADO_OPERATIVO, NUMERO_PLAZAS, CANTIDAD_MAX_CONTENEDORES, transportesNoAceptados);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testConstructorCuandoTransportesAceptadosContieneMasDeTresElementos() {
		inicializaValores();
		transportesNoAceptados.add(TipoTransporte.BARCO);
		transportesNoAceptados.add(TipoTransporte.CAMION);
		transportesNoAceptados.add(TipoTransporte.TREN);
		transportesNoAceptados.add(TipoTransporte.BARCO);
	    new Muelle(ID_MUELLE, PUNTO_GPS, ESTADO_OPERATIVO, NUMERO_PLAZAS, CANTIDAD_MAX_CONTENEDORES, transportesNoAceptados);
	}
	
	
	@Test (expected = IllegalArgumentException.class)
	public void testConstructorCuandoTransportesAceptadosContieneDosElementosRepetidos() {
		inicializaValores();
		transportesNoAceptados.add(TipoTransporte.BARCO);
		transportesNoAceptados.add(TipoTransporte.BARCO);
	    new Muelle(ID_MUELLE, PUNTO_GPS, ESTADO_OPERATIVO, NUMERO_PLAZAS, CANTIDAD_MAX_CONTENEDORES, transportesNoAceptados);
	}
	
	
	@Test (expected = IllegalArgumentException.class)
	public void testConstructorCuandoTransportesAceptadosEstaVacio() {
		inicializaValores();
	    new Muelle(ID_MUELLE, PUNTO_GPS, ESTADO_OPERATIVO, NUMERO_PLAZAS, CANTIDAD_MAX_CONTENEDORES, transportesNoAceptados);
	}

	@Test
    public void testCloneValido() {
		inicializaValores();
		Muelle muelleOriginal = new Muelle(ID_MUELLE, PUNTO_GPS, ESTADO_NO_OPERATIVO, NUMERO_PLAZAS, CANTIDAD_MAX_CONTENEDORES, aceptaTodosLosTransportes);
        Muelle muelleCopia = muelleOriginal.clone();
        assertEquals(muelleOriginal.getIdMuelle(), muelleCopia.getIdMuelle());
        assertEquals(muelleOriginal.getPuntoGPS(), muelleCopia.getPuntoGPS());
        assertEquals(muelleOriginal.getEstado(), muelleCopia.getEstado());
        assertFalse(muelleCopia.isOperativo());
        assertTrue(muelleCopia.isFueraDeServicio());
        assertEquals(muelleOriginal.getNumeroPlazas(), muelleCopia.getNumeroPlazas());
        assertEquals(muelleOriginal.getCantidadMaxContenedores(), muelleCopia.getCantidadMaxContenedores());
    }
	
	
	@Test
	public void testCambiarEstadoMuelle() {
		inicializaValores();
		muelle = new Muelle(ID_MUELLE, PUNTO_GPS, ESTADO_OPERATIVO, NUMERO_PLAZAS, CANTIDAD_MAX_CONTENEDORES,aceptaTodosLosTransportes);
	    muelle.setFueraDeServicio();
	    assertEquals(EstadoMuelle.FUERA_DE_SERVICIO, muelle.getEstado());
	    muelle.setOperativo();
	    assertEquals(EstadoMuelle.OPERATIVO, muelle.getEstado());
	}
	
	
	
	
	@Test
	public void testGetPlazasVacias() {
		inicializaValores();
	    PlazaMuelle plazaVacia = new PlazaMuelle("0", 3);
	    PlazaMuelle plazaLlena = new PlazaMuelle("1", 2);
	    PlazaMuelle plazaSemiLlena = new PlazaMuelle("2", 3); 
	    plazaLlena.agregarContenedor(CONTENEDOR_VALIDO);
	    plazaLlena.agregarContenedor(OTRO_CONTENEDOR_VALIDO);
	    plazaSemiLlena.agregarContenedor(OTRO_CONTENEDOR_VALIDO_MAS);

	    ArrayList<PlazaMuelle> plazas = new ArrayList<>();
	    plazas.add(plazaVacia);
	    plazas.add(plazaLlena);
	    plazas.add(plazaSemiLlena);

	    muelle.setPlazas(plazas); 

	    ArrayList<PlazaMuelle> plazasVacias = muelle.getPlazasVacias();
	    assertEquals(1, plazasVacias.size());
	    assertTrue(plazasVacias.contains(plazaVacia));
	}
	
	
	@Test
	public void testGetPlazasSemiLlenas() {
		inicializaValores();
	    PlazaMuelle plazaVacia = new PlazaMuelle("0", 3);
	    PlazaMuelle plazaLlena = new PlazaMuelle("1", 2);
	    PlazaMuelle plazaSemiLlena = new PlazaMuelle("2", 3); 
	    plazaLlena.agregarContenedor(CONTENEDOR_VALIDO);
	    plazaLlena.agregarContenedor(OTRO_CONTENEDOR_VALIDO);
	    plazaSemiLlena.agregarContenedor(OTRO_CONTENEDOR_VALIDO_MAS);

	    ArrayList<PlazaMuelle> plazas = new ArrayList<>();
	    plazas.add(plazaVacia);
	    plazas.add(plazaLlena);
	    plazas.add(plazaSemiLlena);

	    muelle.setPlazas(plazas); 

	    ArrayList<PlazaMuelle> plazasSemiLlenas = muelle.getPlazasSemiLlenas();
	    assertEquals(1, plazasSemiLlenas.size());
	    assertTrue(plazasSemiLlenas.contains(plazaSemiLlena));
	}
	
	@Test
	public void testGetPlazasLlenas() {
		inicializaValores();
	    PlazaMuelle plazaVacia = new PlazaMuelle("0", 3);
	    PlazaMuelle plazaLlena = new PlazaMuelle("1", 2);
	    PlazaMuelle plazaSemiLlena = new PlazaMuelle("2", 3); 
	    plazaLlena.agregarContenedor(CONTENEDOR_VALIDO);
	    plazaLlena.agregarContenedor(OTRO_CONTENEDOR_VALIDO);
	    plazaSemiLlena.agregarContenedor(OTRO_CONTENEDOR_VALIDO_MAS);

	    ArrayList<PlazaMuelle> plazas = new ArrayList<>();
	    plazas.add(plazaVacia);
	    plazas.add(plazaLlena);
	    plazas.add(plazaSemiLlena);

	    muelle.setPlazas(plazas); 

	    ArrayList<PlazaMuelle> plazasLlenas = muelle.getPlazasLlenas();
	    assertEquals(1, plazasLlenas.size());
	    assertTrue(plazasLlenas.contains(plazaLlena));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testSetPlazasNulo() {
		inicializaValores();
	    muelle.setPlazas(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSetPlazasConIdDuplicado() {
		inicializaValores();
	    muelle = new Muelle("01", PUNTO_GPS, ESTADO_OPERATIVO, 5, 10, aceptaTodosLosTransportes);
	    PlazaMuelle plaza1 = new PlazaMuelle("0", 3);
	    PlazaMuelle plaza2 = new PlazaMuelle("0", 4); 
	    ArrayList<PlazaMuelle> plazas = new ArrayList<>();
	    plazas.add(plaza1);
	    plazas.add(plaza2);
	    muelle.setPlazas(plazas);	
	}
	
	@Test
	public void testSetPlazasSinDuplicados() {
		inicializaValores();
	    muelle = new Muelle("02", PUNTO_GPS, ESTADO_OPERATIVO, 5, 10, aceptaTodosLosTransportes);
	    PlazaMuelle plaza1 = new PlazaMuelle("1", 3);
	    PlazaMuelle plaza2 = new PlazaMuelle("2", 3);
	    ArrayList<PlazaMuelle> plazas = new ArrayList<>();
	    plazas.add(plaza1);
	    plazas.add(plaza2);
	    muelle.setPlazas(plazas);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSetPlazasConDuplicados() {
		inicializaValores();
		muelle = new Muelle("02", PUNTO_GPS, ESTADO_OPERATIVO, 5, 10, aceptaTodosLosTransportes);
	    PlazaMuelle plaza1 = new PlazaMuelle("1", 3);
	    PlazaMuelle plaza2 = new PlazaMuelle("1", 4); 
	    ArrayList<PlazaMuelle> plazas = new ArrayList<>();
	    plazas.add(plaza1);
	    plazas.add(plaza2);
	    muelle.setPlazas(plazas);	
	}
	
	@Test
	public void testAgregarPlazaConIdValido() {
		inicializaValores();
	    muelle = new Muelle("03", PUNTO_GPS, ESTADO_OPERATIVO, 3, 10, aceptaTodosLosTransportes);
	    PlazaMuelle plaza = new PlazaMuelle("0", 3);
	    ArrayList<PlazaMuelle> plazas = new ArrayList<>();
	    plazas.add(plaza);
	    muelle.setPlazas(plazas);		
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAgregarPlazaConIdFueraDeRango() {
		inicializaValores();
	    muelle = new Muelle("03", PUNTO_GPS, ESTADO_OPERATIVO, 3, 10, aceptaTodosLosTransportes);
	    PlazaMuelle plaza = new PlazaMuelle("5", 3); 
	    ArrayList<PlazaMuelle> plazas = new ArrayList<>();
	    plazas.add(plaza);
	    muelle.setPlazas(plazas);		
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAgregarPlazaConIdDuplicadoEnArray() {
		inicializaValores();
	    muelle = new Muelle("03", PUNTO_GPS, ESTADO_OPERATIVO, 3, 10, aceptaTodosLosTransportes);
	    PlazaMuelle plaza1 = new PlazaMuelle("0", 3);
	    PlazaMuelle plaza2 = new PlazaMuelle("0", 4); 
	    ArrayList<PlazaMuelle> plazas = new ArrayList<>();
	    plazas.add(plaza1);
	    plazas.add(plaza2);
	    muelle.setPlazas(plazas);	
	}
	
	@Test(expected = IllegalArgumentException.class)
    public void testAsignarContenedorNull() {
		inicializaValores(); 
        ArrayList<PlazaMuelle> plazas = new ArrayList<>();
        PlazaMuelle plaza = new PlazaMuelle("0", 3);
	    plazas.add(plaza);
        muelle.setPlazas(plazas); 
        muelle.asignarContenedorAPlaza(null);
    }
	
	
	@Test(expected = IllegalStateException.class)
    public void testAsignarContenedorCuandoMuelleEstaFueraDeServicio() {
		inicializaValores();
        ArrayList<PlazaMuelle> plazas = new ArrayList<>();
        PlazaMuelle plaza = new PlazaMuelle("0", 3);
	    plazas.add(plaza);
        muelle.setPlazas(plazas); 
        muelle.setFueraDeServicio();
        muelle.asignarContenedorAPlaza(CONTENEDOR_VALIDO);
    }
	

	@Test
    public void testEstaContenedor() {
		inicializaValores();
        ArrayList<PlazaMuelle> plazas = new ArrayList<>();
        PlazaMuelle plaza = new PlazaMuelle("0", 3);
	    plazas.add(plaza);
        muelle.setPlazas(plazas); 
        muelle.asignarContenedorAPlaza(CONTENEDOR_VALIDO);
        muelle.asignarContenedorAPlaza(OTRO_CONTENEDOR_VALIDO);
        assertTrue(muelle.estaContenedor(CONTENEDOR_VALIDO.getIdentificador())); 
        assertFalse(muelle.estaContenedor(OTRO_CONTENEDOR_VALIDO_MAS.getIdentificador())); 
    }
	
	@Test(expected = IllegalArgumentException.class)
    public void testEstaContenedorNull() {
		inicializaValores();
        ArrayList<PlazaMuelle> plazas = new ArrayList<>();
        PlazaMuelle plaza = new PlazaMuelle("0", 3);
	    plazas.add(plaza);
        muelle.setPlazas(plazas); 
        muelle.asignarContenedorAPlaza(CONTENEDOR_VALIDO);
        muelle.asignarContenedorAPlaza(OTRO_CONTENEDOR_VALIDO);
        assertTrue(muelle.estaContenedor(null)); 
    }

	@Test
	public void testGetPlazaDelContenedor() {
		inicializaValores();
	    PlazaMuelle plaza1 = new PlazaMuelle("0", 3);
	    PlazaMuelle plaza2 = new PlazaMuelle("1", 2);
	    PlazaMuelle plaza3 = new PlazaMuelle("2", 3);

	    plaza1.agregarContenedor(CONTENEDOR_VALIDO);
	    plaza2.agregarContenedor(OTRO_CONTENEDOR_VALIDO);
	    plaza3.agregarContenedor(OTRO_CONTENEDOR_VALIDO_MAS);

	    ArrayList<PlazaMuelle> plazas = new ArrayList<>();
	    plazas.add(plaza1);
	    plazas.add(plaza2);
	    plazas.add(plaza3);
	    muelle.setPlazas(plazas);

	    assertEquals(plaza1, muelle.getPlazaDelContenedor(CONTENEDOR_VALIDO.getIdentificador()));
	    assertEquals(plaza2, muelle.getPlazaDelContenedor(OTRO_CONTENEDOR_VALIDO.getIdentificador()));
	    assertEquals(plaza3, muelle.getPlazaDelContenedor(OTRO_CONTENEDOR_VALIDO_MAS.getIdentificador()));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testGetPlazaDelContenedorNoEstaElContenedor() {
		inicializaValores();
	    PlazaMuelle plaza1 = new PlazaMuelle("0", 3);
	    PlazaMuelle plaza2 = new PlazaMuelle("1", 2);
	    PlazaMuelle plaza3 = new PlazaMuelle("2", 3);

	    plaza1.agregarContenedor(CONTENEDOR_VALIDO);
	    plaza3.agregarContenedor(OTRO_CONTENEDOR_VALIDO_MAS);

	    ArrayList<PlazaMuelle> plazas = new ArrayList<>();
	    plazas.add(plaza1);
	    plazas.add(plaza2);
	    plazas.add(plaza3);
	    muelle.setPlazas(plazas);

	    muelle.getPlazaDelContenedor(OTRO_CONTENEDOR_VALIDO.getIdentificador());
	}
	
	@Test
	public void testGetNivelApiladoDelContenedor() {
		inicializaValores();
	    PlazaMuelle plaza = new PlazaMuelle("0", 3);
	    plaza.agregarContenedor(CONTENEDOR_VALIDO);
	    plaza.agregarContenedor(OTRO_CONTENEDOR_VALIDO);
	    
	    ArrayList<PlazaMuelle> plazas = new ArrayList<>();
	    plazas.add(plaza);
	    muelle.setPlazas(plazas);
	    
	    int nivel = muelle.getNivelApiladoDelContenedor(CONTENEDOR_VALIDO.getIdentificador());
	    assertEquals(0, nivel); 

	    nivel = muelle.getNivelApiladoDelContenedor(OTRO_CONTENEDOR_VALIDO.getIdentificador());
	    assertEquals(1, nivel); 
	}

	@Test(expected = IllegalStateException.class)
	public void testAsignarContenedorSinEspacio() {
		inicializaValores();
	    PlazaMuelle plaza = new PlazaMuelle("0", 1);
	    plaza.agregarContenedor(CONTENEDOR_VALIDO);
	    ArrayList<PlazaMuelle> plazas = new ArrayList<>();
	    plazas.add(plaza);
	    muelle.setPlazas(plazas);
	    
	    
	    muelle.asignarContenedorAPlaza(OTRO_CONTENEDOR_VALIDO);
	}
	
	
	@Test (expected = IllegalStateException.class)
	public void testAsignarContenedorAPlazaCuandoEstaElContenedorEnUnaPlazaSuperiorAlPrimerEspacioVacio() {
		inicializaValores();
	    muelle = new Muelle(ID_MUELLE, PUNTO_GPS, ESTADO_OPERATIVO, NUMERO_PLAZAS, 3, aceptaTodosLosTransportes);
	    PlazaMuelle plaza1 = new PlazaMuelle("1", 1);
	    PlazaMuelle plaza2 = new PlazaMuelle("2", 1);
	    PlazaMuelle plaza3 = new PlazaMuelle("3", 1);
	    ArrayList<PlazaMuelle> plazas = new ArrayList<>();
	    plazas.add(plaza1);
	    plazas.add(plaza2);
	    plazas.add(plaza3);
	    muelle.setPlazas(plazas);
	    
	    muelle.asignarContenedorAPlaza(CONTENEDOR_VALIDO);
	    muelle.asignarContenedorAPlaza(OTRO_CONTENEDOR_VALIDO);
	    muelle.asignarContenedorAPlaza(OTRO_CONTENEDOR_VALIDO_MAS);
	    
	    muelle.desapilarContenedor(OTRO_CONTENEDOR_VALIDO.getIdentificador());
	    muelle.asignarContenedorAPlaza(OTRO_CONTENEDOR_VALIDO_MAS);
	}

	@Test
	public void testDesapilarContenedorValido() {
		inicializaValores();	    
	    PlazaMuelle plaza = new PlazaMuelle("0", 3);
	    plaza.agregarContenedor(CONTENEDOR_VALIDO);
	    
	    ArrayList<PlazaMuelle> plazas = new ArrayList<>();
	    plazas.add(plaza);
	    muelle.setPlazas(plazas);
	    
	    assertTrue(plaza.getContenedores().contains(CONTENEDOR_VALIDO));
	    
	    muelle.desapilarContenedor(CONTENEDOR_VALIDO.getIdentificador());
	    
	    assertFalse(plaza.getContenedores().contains(CONTENEDOR_VALIDO));
	}
	
	
	@Test (expected = IllegalStateException.class)
	public void testDesapilarContenedorCuandoMuelleEstaFueraDeServicio() {
		inicializaValores();
	    PlazaMuelle plaza = new PlazaMuelle("0", 3);
	    plaza.agregarContenedor(CONTENEDOR_VALIDO);
	    ArrayList<PlazaMuelle> plazas = new ArrayList<>();
	    plazas.add(plaza);
	    muelle.setPlazas(plazas);
	    muelle.setFueraDeServicio();
	    muelle.desapilarContenedor("CSQU3054383");
	}
	
	@Test
	public void testVaciarConPlazasConContenedores() {
		inicializaValores();
	    muelle = new Muelle(ID_MUELLE, PUNTO_GPS, ESTADO_OPERATIVO, NUMERO_PLAZAS, CANTIDAD_MAX_CONTENEDORES,aceptaTodosLosTransportes);
	    
	    PlazaMuelle plaza1 = new PlazaMuelle("0", 3);
	    PlazaMuelle plaza2 = new PlazaMuelle("1", 2);
	    plaza1.agregarContenedor(CONTENEDOR_VALIDO);
	    plaza2.agregarContenedor(OTRO_CONTENEDOR_VALIDO);
	    
	    ArrayList<PlazaMuelle> plazas = new ArrayList<>();
	    plazas.add(plaza1);
	    plazas.add(plaza2);
	    muelle.setPlazas(plazas);
	    
	    assertFalse(plaza1.getContenedores().isEmpty());
	    assertFalse(plaza2.getContenedores().isEmpty());
	    
	    muelle.vaciar();
	    
	    assertTrue(plaza1.getContenedores().isEmpty());
	    assertTrue(plaza2.getContenedores().isEmpty());
	}

	@Test
	public void testVaciarConPlazasVacias() {
		inicializaValores();
	    
	    PlazaMuelle plaza1 = new PlazaMuelle("0", 3);
	    PlazaMuelle plaza2 = new PlazaMuelle("1", 2);
	    
	    ArrayList<PlazaMuelle> plazas = new ArrayList<>();
	    plazas.add(plaza1);
	    plazas.add(plaza2);
	    muelle.setPlazas(plazas);
	    
	    assertTrue(plaza1.getContenedores().isEmpty());
	    assertTrue(plaza2.getContenedores().isEmpty());
	    
	    muelle.vaciar();
	    
	    assertTrue(plaza1.getContenedores().isEmpty());
	    assertTrue(plaza2.getContenedores().isEmpty());
	}

	@Test
	public void testVaciarConMuelleSinPlazas() {
		inicializaValores();
	    
	    muelle.vaciar();
	}
	
	@Test
	public void testExisteLaPlazaPlazaExiste() {
		inicializaValores();
	    PlazaMuelle plaza1 = new PlazaMuelle("0", 3);
	    PlazaMuelle plaza2 = new PlazaMuelle("1", 3);

	    ArrayList<PlazaMuelle> plazas = new ArrayList<>();
	    plazas.add(plaza1);
	    plazas.add(plaza2);

	    muelle.setPlazas(plazas);

	    assertTrue(muelle.existeLaPlaza("0"));
	    assertTrue(muelle.existeLaPlaza("1"));
	}

	@Test
	public void testExisteLaPlazaPlazaNoExiste() {
		inicializaValores();
	    PlazaMuelle plaza1 = new PlazaMuelle("0", 3);
	    PlazaMuelle plaza2 = new PlazaMuelle("1", 3);

	    ArrayList<PlazaMuelle> plazas = new ArrayList<>();
	    plazas.add(plaza1);
	    plazas.add(plaza2);

	    muelle.setPlazas(plazas);

	    assertFalse(muelle.existeLaPlaza("2"));
	    assertFalse(muelle.existeLaPlaza("3"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testExisteLaPlazaIdentificadorNull() {
		inicializaValores();
	    muelle.existeLaPlaza(null);
	}

	@Test
	public void testTieneEspacioLaPlazaConEspacioDisponible() {
		inicializaValores();
	    PlazaMuelle plaza = new PlazaMuelle("0", 3); 

	    ArrayList<PlazaMuelle> plazas = new ArrayList<>();
	    plazas.add(plaza);
	    muelle.setPlazas(plazas);

	    assertTrue(muelle.tieneEspacioLaPlaza("0"));
	}

	@Test
	public void testTieneEspacioLaPlazaSemillena() {
		inicializaValores();
	    PlazaMuelle plaza = new PlazaMuelle("0", 3); 

	    plaza.agregarContenedor(CONTENEDOR_VALIDO); 
	    ArrayList<PlazaMuelle> plazas = new ArrayList<>();
	    plazas.add(plaza);
	    muelle.setPlazas(plazas);

	    assertTrue(muelle.tieneEspacioLaPlaza("0"));
	}
	
	@Test
	public void testTieneEspacioSemilleno() {
		inicializaValores();
	    PlazaMuelle plaza1 = new PlazaMuelle("0", 3);
	    PlazaMuelle plaza2 = new PlazaMuelle("1", 3);

	    ArrayList<PlazaMuelle> plazas = new ArrayList<>();
	    plazas.add(plaza1);
	    plazas.add(plaza2);
	    plaza1.agregarContenedor(CONTENEDOR_VALIDO); 
	    muelle.setPlazas(plazas);

	    assertTrue(muelle.tieneEspacio());
	}
	
	@Test
	public void testTieneEspacioLaPlazaSinEspacioDisponible() {
		inicializaValores();
	    PlazaMuelle plaza = new PlazaMuelle("0", 1);

	    plaza.agregarContenedor(CONTENEDOR_VALIDO); 
	    ArrayList<PlazaMuelle> plazas = new ArrayList<>();
	    plazas.add(plaza);
	    muelle.setPlazas(plazas);

	    assertFalse(muelle.tieneEspacioLaPlaza("0"));
	}

	@Test
	public void testTieneEspacioConPlazaDisponible() {
		inicializaValores();
	    PlazaMuelle plaza1 = new PlazaMuelle("0", 3);
	    PlazaMuelle plaza2 = new PlazaMuelle("1", 3);

	    ArrayList<PlazaMuelle> plazas = new ArrayList<>();
	    plazas.add(plaza1);
	    plazas.add(plaza2);
	    muelle.setPlazas(plazas);

	    assertTrue(muelle.tieneEspacio());
	}

	@Test
	public void testTieneEspacioSinPlazaDisponible() {
		inicializaValores();
	    PlazaMuelle plaza1 = new PlazaMuelle("1", 1);

	    plaza1.agregarContenedor(CONTENEDOR_VALIDO); 
	    ArrayList<PlazaMuelle> plazas = new ArrayList<>();
	    plazas.add(plaza1);
	    muelle.setPlazas(plazas);

	    assertFalse(muelle.tieneEspacio());
	}

	@Test
	public void testObtenerPlazaPorIdPlazaExiste() {
		inicializaValores();
	    PlazaMuelle plaza = new PlazaMuelle("0", 3);

	    ArrayList<PlazaMuelle> plazas = new ArrayList<>();
	    plazas.add(plaza);
	    muelle.setPlazas(plazas);

	    assertEquals(plaza, muelle.obtenerPlazaPorId("0"));
	}

	@Test(expected = IllegalStateException.class)
	public void testObtenerPlazaPorIdPlazaNoExiste() {
		inicializaValores();
	    
	    ArrayList<PlazaMuelle> plazas = new ArrayList<>();
	    plazas.add(new PlazaMuelle("0", 2)); 
	    muelle.setPlazas(plazas);
	    
	    muelle.obtenerPlazaPorId("1"); 
	}

	@Test(expected = IllegalArgumentException.class)
	public void testObtenerPlazaPorIdPlazaNull() {
		inicializaValores();

	    muelle.obtenerPlazaPorId(null); 
	}

	@Test(expected = IllegalStateException.class)
	public void testObtenerContenedorPorIdContenedorNoExiste() {
		inicializaValores();

	    PlazaMuelle plaza = new PlazaMuelle("0", 1);
	    ArrayList<PlazaMuelle> plazas = new ArrayList<>();
	    plazas.add(plaza);
	    muelle.setPlazas(plazas);

	    muelle.obtenerContenedorPorId(CONTENEDOR_VALIDO.getIdentificador());
	}

	@Test
	public void testObtenerContenedorPorIdContenedorExiste() {
		inicializaValores();
	    
	    PlazaMuelle plaza = new PlazaMuelle("0", 1);
	    plaza.agregarContenedor(CONTENEDOR_VALIDO);
	    
	    ArrayList<PlazaMuelle> plazas = new ArrayList<>();
	    plazas.add(plaza);
	    muelle.setPlazas(plazas);

	    Contenedor resultado = muelle.obtenerContenedorPorId(CONTENEDOR_VALIDO.getIdentificador());
	    assertEquals(CONTENEDOR_VALIDO, resultado);
	}
	
	@Test
    public void testCloneExitoso() {
		inicializaValores();
        PlazaMuelle plaza = new PlazaMuelle("0", 1);
	    plaza.agregarContenedor(CONTENEDOR_VALIDO);
	    ArrayList<PlazaMuelle> plazas = new ArrayList<>();
	    plazas.add(plaza);
	    muelle.setPlazas(plazas);
	    Muelle muelleCopia = muelle.clone();
        assertEquals(muelle, muelleCopia);
    }
    
	
	@Test
	public void testEqualsConPlazasDeDistintoTamanno() {
		inicializaValores();
	    Muelle muelle1 = new Muelle(ID_MUELLE, PUNTO_GPS, ESTADO_OPERATIVO, NUMERO_PLAZAS, CANTIDAD_MAX_CONTENEDORES, aceptaTodosLosTransportes);
	    Muelle muelle2 = new Muelle(ID_MUELLE, PUNTO_GPS, ESTADO_OPERATIVO, NUMERO_PLAZAS, CANTIDAD_MAX_CONTENEDORES, aceptaTodosLosTransportes);

	    PlazaMuelle plazaMuelle1 = new PlazaMuelle("0", 2);
	    PlazaMuelle plazaMuelle2 = new PlazaMuelle("1", 3);

	    ArrayList<PlazaMuelle> plazasMuelle1 = new ArrayList<>();
	    plazasMuelle1.add(plazaMuelle1);

	    ArrayList<PlazaMuelle> plazasMuelle2 = new ArrayList<>();
	    plazasMuelle2.add(plazaMuelle1);
	    plazasMuelle2.add(plazaMuelle2);

	    muelle1.setPlazas(plazasMuelle1);
	    muelle2.setPlazas(plazasMuelle2);

	    assertFalse(muelle1.equals(muelle2));
	}
	
	@Test
	public void testEqualsConPlazasNoNulasYDelMismoTamanno() {
		inicializaValores();
	    Muelle muelle1 = new Muelle(ID_MUELLE, PUNTO_GPS, ESTADO_OPERATIVO, NUMERO_PLAZAS, CANTIDAD_MAX_CONTENEDORES, aceptaTodosLosTransportes);
	    Muelle muelle2 = new Muelle(ID_MUELLE, PUNTO_GPS, ESTADO_OPERATIVO, NUMERO_PLAZAS, CANTIDAD_MAX_CONTENEDORES, aceptaTodosLosTransportes);

	    PlazaMuelle plaza1Muelle1 = new PlazaMuelle("0", 3);
	    PlazaMuelle plaza2Muelle1 = new PlazaMuelle("1", 3);
	    PlazaMuelle plaza1Muelle2 = new PlazaMuelle("2", 3);
	    PlazaMuelle plaza2Muelle2 = new PlazaMuelle("3", 3);

	    ArrayList<PlazaMuelle> plazasMuelle1 = new ArrayList<>();
	    plazasMuelle1.add(plaza1Muelle1);
	    plazasMuelle1.add(plaza2Muelle1);

	    ArrayList<PlazaMuelle> plazasMuelle2 = new ArrayList<>();
	    plazasMuelle2.add(plaza1Muelle2);
	    plazasMuelle2.add(plaza2Muelle2);

	    muelle1.setPlazas(plazasMuelle1);
	    muelle2.setPlazas(plazasMuelle2);

	    assertFalse(muelle1.equals(muelle2));
	}

	@Test
	public void testEqualsUnMuelleConPlazasNulasYOtroNo() {
		inicializaValores();
	    Muelle muelle1 = new Muelle(ID_MUELLE, PUNTO_GPS, ESTADO_OPERATIVO, NUMERO_PLAZAS, CANTIDAD_MAX_CONTENEDORES, aceptaTodosLosTransportes);
	    Muelle muelle2 = new Muelle(ID_MUELLE, PUNTO_GPS, ESTADO_OPERATIVO, NUMERO_PLAZAS, CANTIDAD_MAX_CONTENEDORES, aceptaTodosLosTransportes);

	    PlazaMuelle plaza1Muelle1 = new PlazaMuelle("0", 3);

	    ArrayList<PlazaMuelle> plazasMuelle1 = new ArrayList<>();
	    plazasMuelle1.add(plaza1Muelle1);

	    muelle1.setPlazas(plazasMuelle1);

	    assertFalse(muelle1.equals(muelle2));
	}

	@Test
	public void testEqualsConMismoId() {
		inicializaValores();
	    Muelle muelle1 = new Muelle(ID_MUELLE, PUNTO_GPS, ESTADO_OPERATIVO, NUMERO_PLAZAS, CANTIDAD_MAX_CONTENEDORES, aceptaTodosLosTransportes);
	    Muelle muelle2 = new Muelle(ID_MUELLE, PUNTO_GPS, ESTADO_OPERATIVO, NUMERO_PLAZAS, CANTIDAD_MAX_CONTENEDORES, aceptaTodosLosTransportes);
	    
	    assertTrue(muelle1.equals(muelle2));
	}

	@Test
	public void testEqualsConDiferenteMaxContenedores() {
		inicializaValores();
	    Muelle muelle1 = new Muelle(ID_MUELLE, PUNTO_GPS, ESTADO_OPERATIVO, NUMERO_PLAZAS, CANTIDAD_MAX_CONTENEDORES, aceptaTodosLosTransportes);
	    Muelle muelle2 = new Muelle(ID_MUELLE, PUNTO_GPS, ESTADO_OPERATIVO, NUMERO_PLAZAS, 6, aceptaTodosLosTransportes);
	    
	    assertFalse(muelle1.equals(muelle2));
	}
	
	@Test
	public void testEqualsConDiferenteNumPlazas() {
		inicializaValores();
	    Muelle muelle1 = new Muelle(ID_MUELLE, PUNTO_GPS, ESTADO_OPERATIVO, NUMERO_PLAZAS, CANTIDAD_MAX_CONTENEDORES, aceptaTodosLosTransportes);
	    Muelle muelle2 = new Muelle(ID_MUELLE, PUNTO_GPS, ESTADO_OPERATIVO, 4, CANTIDAD_MAX_CONTENEDORES, aceptaTodosLosTransportes);
	    
	    assertFalse(muelle1.equals(muelle2));
	}
	
	@Test
	public void testEqualsConDiferenteEstado() {
		inicializaValores();
	    Muelle muelle1 = new Muelle(ID_MUELLE, PUNTO_GPS, ESTADO_OPERATIVO, NUMERO_PLAZAS, CANTIDAD_MAX_CONTENEDORES, aceptaTodosLosTransportes);
	    Muelle muelle2 = new Muelle(ID_MUELLE, PUNTO_GPS, ESTADO_NO_OPERATIVO, 4, CANTIDAD_MAX_CONTENEDORES, aceptaTodosLosTransportes);
	    
	    assertFalse(muelle1.equals(muelle2));
	}

	@Test
	public void testEqualsConDiferentePuntoGPS() {
		inicializaValores();
	    Muelle muelle1 = new Muelle(ID_MUELLE, PUNTO_GPS, ESTADO_OPERATIVO, NUMERO_PLAZAS, CANTIDAD_MAX_CONTENEDORES,aceptaTodosLosTransportes);
	    Muelle muelle2 = new Muelle(ID_MUELLE, new GPSCoordinate(80,90), ESTADO_NO_OPERATIVO, NUMERO_PLAZAS, CANTIDAD_MAX_CONTENEDORES, aceptaTodosLosTransportes);
	    
	    assertFalse(muelle1.equals(muelle2));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testEqualsConNull() {
		inicializaValores();
	    muelle.equals(null);
	}

	@Test
	public void testEqualsCuandoListaContenedoresNoEquals() {
		inicializaValores();
	    ArrayList<Contenedor> contenedores1 = new ArrayList<>();
	    contenedores1.add(CONTENEDOR_VALIDO);
	    contenedores1.add(OTRO_CONTENEDOR_VALIDO_MAS);
	    
	    ArrayList<Contenedor> contenedores2 = new ArrayList<>();
	    contenedores2.add(CONTENEDOR_VALIDO);
	    contenedores2.add(OTRO_CONTENEDOR_VALIDO);
	    
	    PlazaMuelle plaza1 = new PlazaMuelle("01", 100, contenedores1);
	    PlazaMuelle plaza2 = new PlazaMuelle("01", 100, contenedores2); 
	    
	    Muelle muelle1 = new Muelle(ID_MUELLE, PUNTO_GPS, ESTADO_OPERATIVO, NUMERO_PLAZAS, CANTIDAD_MAX_CONTENEDORES, aceptaTodosLosTransportes);
	    Muelle muelle2 = new Muelle(ID_MUELLE, PUNTO_GPS, ESTADO_OPERATIVO, NUMERO_PLAZAS, CANTIDAD_MAX_CONTENEDORES, aceptaTodosLosTransportes);
	    ArrayList<PlazaMuelle> plazas1 = new ArrayList<>();
	    ArrayList<PlazaMuelle> plazas2 = new ArrayList<>();


	    plazas1.add(plaza1);
	    plazas2.add(plaza2);
	    muelle1.setPlazas(plazas1);
	    muelle2.setPlazas(plazas2);
	    
	    assertFalse(muelle1.equals(muelle2));
	}
	
	@Test
	public void testEqualsConDiferenteId() {
		inicializaValores();
	    Muelle muelle1 = new Muelle("12", PUNTO_GPS, ESTADO_OPERATIVO, NUMERO_PLAZAS, CANTIDAD_MAX_CONTENEDORES, aceptaTodosLosTransportes);
	    Muelle muelle2 = new Muelle("34", PUNTO_GPS, ESTADO_OPERATIVO, NUMERO_PLAZAS, CANTIDAD_MAX_CONTENEDORES,aceptaTodosLosTransportes);
	    
	    assertFalse(muelle1.equals(muelle2));
	}

	@Test
	public void testEqualsCuandoListaContenedoresEquals() {
		inicializaValores();
	    ArrayList<Contenedor> contenedores1 = new ArrayList<>();
	    contenedores1.add(CONTENEDOR_VALIDO);
	    contenedores1.add(OTRO_CONTENEDOR_VALIDO);
	    
	    ArrayList<Contenedor> contenedores2 = new ArrayList<>();
	    contenedores2.add(CONTENEDOR_VALIDO);
	    contenedores2.add(OTRO_CONTENEDOR_VALIDO);
	    
	    PlazaMuelle plaza1 = new PlazaMuelle("01", 100, contenedores1);
	    PlazaMuelle plaza2 = new PlazaMuelle("01", 100, contenedores2); 
	    
	    Muelle muelle1 = new Muelle(ID_MUELLE, PUNTO_GPS, ESTADO_OPERATIVO, NUMERO_PLAZAS, CANTIDAD_MAX_CONTENEDORES, aceptaTodosLosTransportes);
	    Muelle muelle2 = new Muelle(ID_MUELLE, PUNTO_GPS, ESTADO_OPERATIVO, NUMERO_PLAZAS, CANTIDAD_MAX_CONTENEDORES, aceptaTodosLosTransportes);
	    ArrayList<PlazaMuelle> plazas1 = new ArrayList<>();
	    ArrayList<PlazaMuelle> plazas2 = new ArrayList<>();


	    plazas1.add(plaza1);
	    plazas2.add(plaza2);
	    muelle1.setPlazas(plazas1);
	    muelle2.setPlazas(plazas2);
	    
	    assertTrue(muelle1.equals(muelle2));
	}
	
	@Test
	public void testEqualsCuandoPlazasNoEquals() {
		inicializaValores();
	    ArrayList<Contenedor> contenedores1 = new ArrayList<>();
	    contenedores1.add(CONTENEDOR_VALIDO);
	    contenedores1.add(OTRO_CONTENEDOR_VALIDO);
	    
	    ArrayList<Contenedor> contenedores2 = new ArrayList<>();
	    contenedores2.add(CONTENEDOR_VALIDO);
	    contenedores2.add(OTRO_CONTENEDOR_VALIDO);
	    
	    PlazaMuelle plaza1 = new PlazaMuelle("01", 100, contenedores1);
	    PlazaMuelle plaza2 = new PlazaMuelle("02", 100, contenedores2); 
	    
	    Muelle muelle1 = new Muelle(ID_MUELLE, PUNTO_GPS, ESTADO_OPERATIVO, NUMERO_PLAZAS, CANTIDAD_MAX_CONTENEDORES, aceptaTodosLosTransportes);
	    Muelle muelle2 = new Muelle(ID_MUELLE, PUNTO_GPS, ESTADO_OPERATIVO, NUMERO_PLAZAS, CANTIDAD_MAX_CONTENEDORES, aceptaTodosLosTransportes);
	    ArrayList<PlazaMuelle> plazas1 = new ArrayList<>();
	    ArrayList<PlazaMuelle> plazas2 = new ArrayList<>();


	    plazas1.add(plaza1);
	    plazas2.add(plaza2);
	    muelle1.setPlazas(plazas1);
	    muelle2.setPlazas(plazas2);
	    
	    assertFalse(muelle1.equals(muelle2));
	}
	
	@Test
	public void testToString() {
		inicializaValores();
	    PlazaMuelle plaza = new PlazaMuelle("0", 1);
	    plaza.agregarContenedor(CONTENEDOR_VALIDO);
	    
	    ArrayList<PlazaMuelle> plazas = new ArrayList<>();
	    plazas.add(plaza);
	    muelle.setPlazas(plazas);
	    
	    String expectedString = "Muelle ID: " + ID_MUELLE + ", Estado: " + ESTADO_OPERATIVO + ", Plazas: " 
	                            + NUMERO_PLAZAS + ", Max Contenedores: " + CANTIDAD_MAX_CONTENEDORES + "\n";
	                           
	    
	    assertEquals(expectedString, muelle.toString());
	}


	@Test
	public void testPermiteBarcoCuandoNoLoPermite() {
		aceptaTodosLosTransportes = new ArrayList<>();
		aceptaTodosLosTransportes.add(TipoTransporte.CAMION);
		aceptaTodosLosTransportes.add(TipoTransporte.TREN);
		
		muelle = new Muelle(ID_MUELLE, PUNTO_GPS, ESTADO_OPERATIVO, NUMERO_PLAZAS, CANTIDAD_MAX_CONTENEDORES, aceptaTodosLosTransportes);
		assertFalse(muelle.permiteBarco());
	}
	
	
	@Test
	public void testPermiteCamionCuandoNoLoPermite() {
		aceptaTodosLosTransportes = new ArrayList<>();
		aceptaTodosLosTransportes.add(TipoTransporte.BARCO);
		aceptaTodosLosTransportes.add(TipoTransporte.TREN);
		
		muelle = new Muelle(ID_MUELLE, PUNTO_GPS, ESTADO_OPERATIVO, NUMERO_PLAZAS, CANTIDAD_MAX_CONTENEDORES, aceptaTodosLosTransportes);
		assertFalse(muelle.permiteCamion());
	}
	
	
	@Test
	public void testPermiteTrenCuandoNoLoPermite() {
		aceptaTodosLosTransportes = new ArrayList<>();
		aceptaTodosLosTransportes.add(TipoTransporte.BARCO);
		aceptaTodosLosTransportes.add(TipoTransporte.CAMION);
		
		muelle = new Muelle(ID_MUELLE, PUNTO_GPS, ESTADO_OPERATIVO, NUMERO_PLAZAS, CANTIDAD_MAX_CONTENEDORES, aceptaTodosLosTransportes);
		assertFalse(muelle.permiteTren());
	}
}
