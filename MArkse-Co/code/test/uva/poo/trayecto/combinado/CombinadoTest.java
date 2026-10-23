package uva.poo.trayecto.combinado;

import static org.junit.Assert.*;
import org.junit.Test;

import es.uva.inf.poo.maps.GPSCoordinate;

import java.time.LocalDate;
import java.util.ArrayList;

import uva.poo.puerto.EstadoMuelle;
import uva.poo.puerto.Muelle;
import uva.poo.puerto.Puerto;
import uva.poo.puerto.TipoTransporte;
import uva.poo.trayecto.simple.TBarco;
import uva.poo.trayecto.simple.TCamion;
import uva.poo.trayecto.simple.TTren;


/**
 * Prueba la clase Combinado
 * @author Marta Pérez
 * @author Emily Rodrigues
 */
public class CombinadoTest {

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
    private Puerto puertoDestinoValido2;


    private LocalDate fechaInicioValida;
    private LocalDate fechaFinValida;

    private TCamion tCamion;
    private TBarco tBarco;
    private TTren tTren;
    private PackCamionBarco packCamionBarco;
    private PackCamionTren packCamionTren;
    
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
        puertoDestinoValido2 = new Puerto("RU-MSC");


        puertoOrigenValido.agregaMuelle(muelleOrigenValido);
        puertoOrigenValido2.agregaMuelle(muelleOrigenValido2);
        puertoDestinoValido.agregaMuelle(muelleDestinoValido);

        fechaInicioValida = LocalDate.of(2024, 11, 8);
        fechaFinValida = LocalDate.of(2024, 11, 28);

        tCamion = new TCamion(muelleOrigenValido2, muelleDestinoValido, puertoOrigenValido2, puertoDestinoValido, fechaInicioValida, fechaFinValida);
        tBarco = new TBarco(muelleOrigenValido, muelleDestinoValido, puertoOrigenValido, puertoDestinoValido, fechaInicioValida, fechaFinValida);
        tTren = new TTren(muelleOrigenValido, muelleDestinoValido, puertoOrigenValido, puertoDestinoValido, fechaInicioValida, fechaFinValida);
        
        packCamionBarco = new PackCamionBarco(muelleOrigenValido, muelleDestinoValido, puertoOrigenValido, puertoDestinoValido, fechaInicioValida, fechaFinValida);
        packCamionTren = new PackCamionTren(muelleOrigenValido, muelleDestinoValido, puertoOrigenValido, puertoDestinoValido, fechaInicioValida, fechaFinValida);

    }

    @Test
    public void testAgregarTrayectoSimpleValido() {
    	inicializaValores();
    	packCamionBarco.agregaTrayecto(tBarco);
        assertEquals(packCamionBarco.getTrayectosSimples().size(), 1);
        assertEquals(packCamionBarco.numeroTrayectosSimples(), 1);
    }
    

    @Test(expected = IllegalStateException.class)
    public void testAgregarTrayectoConTipoInvalido() {
    	inicializaValores();
    	packCamionTren.agregaTrayecto(tBarco);
    }
    
    
    @Test(expected = IllegalStateException.class)
    public void testAgregarTrayectoConPuertoOrigenNoCoincidente() {
        inicializaValores();
        packCamionTren = new PackCamionTren(muelleOrigenValido, muelleDestinoValido, puertoOrigenValido, puertoDestinoValido, fechaInicioValida, fechaFinValida);
        TCamion trayectoInvalido = new TCamion(muelleDestinoValido, muelleOrigenValido, puertoDestinoValido, puertoOrigenValido, fechaInicioValida, fechaFinValida);
        packCamionTren.agregaTrayecto(trayectoInvalido);
    }

    
    @Test(expected = IllegalStateException.class)
    public void testAgregarTrayectoConMuelleOrigenNoCoincidente() {
        inicializaValores();
        puertoOrigenValido.agregaMuelle(muelleOrigenValido2);
        TCamion trayectoInvalido = new TCamion(muelleOrigenValido2, muelleDestinoValido, puertoOrigenValido, puertoDestinoValido, fechaInicioValida,fechaFinValida);
        packCamionTren.agregaTrayecto(trayectoInvalido);
    }
    
    
    @Test(expected = IllegalStateException.class)
    public void testAgregarTrayectoConPuertoDestinoNoCoincidente() {
        inicializaValores();
        TCamion primerTrayecto = new TCamion(muelleOrigenValido, muelleDestinoValido, puertoOrigenValido, puertoDestinoValido, fechaInicioValida, fechaInicioValida.plusDays(1));
        TCamion trayectoInvalido = new TCamion(muelleOrigenValido, muelleDestinoValido, puertoOrigenValido, puertoDestinoValido, fechaInicioValida.plusDays(1), fechaInicioValida.plusDays(2));
        packCamionTren.agregaTrayecto(primerTrayecto);
        packCamionTren.agregaTrayecto(trayectoInvalido);
    }

    
    @Test
    public void testCalcularCosteTotal() {
    	inicializaValores();
    	packCamionBarco = new PackCamionBarco(muelleOrigenValido2, muelleDestinoValido, puertoOrigenValido2, puertoDestinoValido, fechaInicioValida, fechaFinValida);
    	Combinado combinado = packCamionBarco;
    	tCamion = new TCamion(muelleOrigenValido2, muelleDestinoValido, puertoOrigenValido2, puertoDestinoValido, fechaInicioValida, fechaInicioValida.plusDays(1));
    	tBarco = new TBarco(muelleDestinoValido, muelleOrigenValido2, puertoDestinoValido, puertoOrigenValido2, fechaInicioValida.plusDays(2), fechaInicioValida.plusDays(3));
    	combinado.agregaTrayecto(tCamion);
    	combinado.agregaTrayecto(tBarco);

        double costeTotalEsperado = (tBarco.costeTotal()*0.85 + tCamion.costeTotal()); 
        assertEquals(costeTotalEsperado, combinado.costeTotal(), 0.001);
    }

    @Test
    public void testEqualsCuandoSonIguales() {
    	inicializaValores();
        Combinado packCamionTren2 = new PackCamionTren(muelleOrigenValido, muelleDestinoValido, puertoOrigenValido, puertoDestinoValido, fechaInicioValida, fechaFinValida);
        tTren = new TTren(muelleOrigenValido, muelleDestinoValido, puertoOrigenValido, puertoDestinoValido, fechaInicioValida, fechaInicioValida.plusDays(3));
        tCamion = new TCamion(muelleDestinoValido, muelleOrigenValido, puertoDestinoValido, puertoOrigenValido, fechaInicioValida.plusDays(4), fechaInicioValida.plusDays(5));
        packCamionTren.agregaTrayecto(tTren);
        packCamionTren.agregaTrayecto(tCamion);

        packCamionTren2.agregaTrayecto(tTren);
        packCamionTren2.agregaTrayecto(tCamion);

        assertTrue(packCamionTren.equals(packCamionTren2));
    }
    
    @Test
    public void testEqualsNull() {
    	inicializaValores();
    	tTren = new TTren(muelleOrigenValido, muelleDestinoValido, puertoOrigenValido, puertoDestinoValido, fechaInicioValida, fechaInicioValida.plusDays(3));
        tCamion = new TCamion(muelleDestinoValido, muelleOrigenValido, puertoDestinoValido, puertoOrigenValido, fechaInicioValida.plusDays(4), fechaInicioValida.plusDays(5));
        packCamionTren.agregaTrayecto(tTren);
        packCamionTren.agregaTrayecto(tCamion);

        assertFalse(packCamionTren.equals(null));
    }

    @Test
    public void testEqualsSuper() {
    	inicializaValores();
        PackCamionTren pack1 = new PackCamionTren(muelleOrigenValido, muelleDestinoValido, puertoOrigenValido, puertoDestinoValido, fechaInicioValida, fechaFinValida);
        PackCamionTren pack2 = new PackCamionTren(muelleOrigenValido2, muelleDestinoValido, puertoOrigenValido2, puertoDestinoValido, fechaInicioValida, fechaFinValida);
        assertFalse(pack1.equals(pack2)); 
    }
    
    @Test
    public void testClonacionPackCamionTren() {
    	inicializaValores();
    	packCamionTren = new PackCamionTren(muelleOrigenValido, muelleDestinoValido, puertoOrigenValido, puertoDestinoValido, fechaInicioValida, fechaFinValida);
    	tCamion = new TCamion(muelleOrigenValido, muelleDestinoValido, puertoOrigenValido, puertoDestinoValido, fechaInicioValida, fechaInicioValida.plusDays(1));
    	tTren = new TTren(muelleDestinoValido, muelleOrigenValido, puertoDestinoValido, puertoOrigenValido, fechaInicioValida.plusDays(2), fechaInicioValida.plusDays(2));
    	packCamionTren.agregaTrayecto(tCamion);
        packCamionTren.agregaTrayecto(tTren);
        PackCamionTren clon = packCamionTren.clone();
        assertEquals(packCamionTren, clon);
    }
    
    @Test
    public void testClonacionPackCamionBarco() {
    	inicializaValores();
    	tCamion = new TCamion(muelleOrigenValido, muelleDestinoValido, puertoOrigenValido, puertoDestinoValido, fechaInicioValida, fechaInicioValida.plusDays(3));
        tBarco = new TBarco(muelleDestinoValido, muelleOrigenValido, puertoDestinoValido, puertoOrigenValido, fechaInicioValida.plusDays(4), fechaInicioValida.plusDays(5));
    	packCamionBarco.agregaTrayecto(tCamion);
        packCamionBarco.agregaTrayecto(tBarco);
        PackCamionBarco clon = packCamionBarco.clone();
        assertEquals(packCamionBarco, clon);
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
    public void testAgregarTrayectoUsaTrenNoPermitido() {
    	inicializaValores();
        packCamionBarco.agregaTrayecto(tTren); 
    }
    
    
    @Test(expected = IllegalStateException.class)
    public void testAgregarTrayectoConFechaFinPosterior() {
        inicializaValores();
        TCamion primerTrayecto = new TCamion(muelleOrigenValido, muelleDestinoValido, puertoOrigenValido, puertoDestinoValido, fechaInicioValida, fechaInicioValida.plusDays(3));
        TCamion trayectoInvalido = new TCamion(muelleDestinoValido, muelleOrigenValido, puertoDestinoValido, puertoOrigenValido, fechaInicioValida.plusDays(2), fechaInicioValida.plusDays(4));
        packCamionTren.agregaTrayecto(primerTrayecto);
        packCamionTren.agregaTrayecto(trayectoInvalido);
    }
    
    
    @Test(expected = IllegalStateException.class)
    public void testAgregarTrayectoConMuelleOrigenNoCoincidenteCuandoHayVariosTrayectos() {
        inicializaValores();
        TCamion primerTrayecto = new TCamion(muelleOrigenValido, muelleDestinoValido, puertoOrigenValido, puertoDestinoValido, fechaInicioValida, fechaInicioValida.plusDays(1));
        TCamion segundoTrayecto = new TCamion( muelleDestinoValido, muelleOrigenValido2, puertoDestinoValido, puertoOrigenValido2, fechaInicioValida.plusDays(1), fechaInicioValida.plusDays(2));
        puertoOrigenValido.agregaMuelle(muelleOrigenValido2); 
        TCamion trayectoInvalido = new TCamion(muelleOrigenValido2, muelleDestinoValido, puertoOrigenValido, puertoDestinoValido, fechaInicioValida.plusDays(2),fechaInicioValida.plusDays(3));
        packCamionTren.agregaTrayecto(primerTrayecto);
        packCamionTren.agregaTrayecto(segundoTrayecto);
        packCamionTren.agregaTrayecto(trayectoInvalido);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCalculaDescuentoNullPackCamionBarco() {
    	inicializaValores();
        packCamionBarco.calculaDescuento(null);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testCalculaDescuentoTrayectoNoPertenecePackCamionBarco() {
    	inicializaValores();
        packCamionBarco.calculaDescuento(tTren);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCalculaDescuentoNullPackCamionTren() {
    	inicializaValores();
        packCamionTren.calculaDescuento(null);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testCalculaDescuentoTrayectoNoPertenecePackCamionTren() {
    	inicializaValores();
    	packCamionTren.calculaDescuento(tTren);
    }
    
    @Test
    public void testCosteTotal() {
    	inicializaValores();
    	tCamion = new TCamion(muelleOrigenValido, muelleDestinoValido, puertoOrigenValido, puertoDestinoValido, fechaInicioValida, fechaInicioValida.plusDays(3));
        tBarco = new TBarco(muelleDestinoValido, muelleOrigenValido, puertoDestinoValido, puertoOrigenValido, fechaInicioValida.plusDays(4), fechaInicioValida.plusDays(5));
        packCamionBarco.agregaTrayecto(tCamion);
        packCamionBarco.agregaTrayecto(tBarco);
        double costeCamion = tCamion.costeTotal();
        double costeBarco = tBarco.costeTotal();
        double costeEsperado = costeCamion + costeBarco-costeBarco*0.15;
        assertEquals(costeEsperado, packCamionBarco.costeTotal(), 0.01);
    }
    
    @Test
    public void testHayTrayectosSiHay() {
    	inicializaValores();
    	tCamion = new TCamion(muelleOrigenValido, muelleDestinoValido, puertoOrigenValido, puertoDestinoValido, fechaInicioValida, fechaInicioValida.plusDays(3));
    	packCamionBarco.agregaTrayecto(tCamion);
        assertTrue(packCamionBarco.hayTrayectos());
    }
    
    @Test
    public void testHayTrayectosNoHay() {
    	inicializaValores();
        assertFalse(packCamionBarco.hayTrayectos());
    }
    
    @Test
    public void testFechaFinPosterior() {
    	inicializaValores();
        LocalDate fechaComparar = LocalDate.of(2023, 12, 4);
        assertTrue(tCamion.isFechaFinPosterior(fechaComparar));
    }

    @Test
    public void testFechaFinNoPosterior() {
    	inicializaValores();
        LocalDate fechaComparar = LocalDate.of(2024, 12, 6);
        assertFalse(tTren.isFechaFinPosterior(fechaComparar));
    }

    @Test
    public void testFechaFinIgual() {
    	inicializaValores();
        LocalDate fechaComparar = LocalDate.of(2024, 12, 5);
        assertFalse(tBarco.isFechaFinPosterior(fechaComparar));
    }
    
    @Test
    public void testDistanciaMillasMarinas() {
    	inicializaValores();
        double distanciaEsperada = tBarco.distanciaKilometros() * 0.539957; 
        assertEquals(distanciaEsperada, tBarco.distanciaMillasMarinas(), 0.001);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorMuelleOrigenNull() {
    	inicializaValores();
        new TCamion(null, muelleDestinoValido, puertoOrigenValido, puertoDestinoValido, fechaInicioValida, fechaFinValida);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorMuelleOrigenNoEnPuerto() {
    	inicializaValores();
        new TCamion(muelleOrigenValido2, muelleDestinoValido, puertoOrigenValido, puertoDestinoValido, fechaInicioValida, fechaFinValida);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorMuelleDestinoNull() {
    	inicializaValores();
        new TCamion(muelleOrigenValido, null, puertoOrigenValido, puertoDestinoValido2, fechaInicioValida, fechaFinValida);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorMuelleDestinoNoEnPuerto() {
    	inicializaValores();
        new TCamion(muelleOrigenValido, muelleDestinoValido, puertoOrigenValido, puertoDestinoValido2, fechaInicioValida, fechaFinValida);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorPuertoDestinoIgualAOrigen() {
    	inicializaValores();
        new TCamion(muelleOrigenValido, muelleDestinoValido, puertoOrigenValido, puertoOrigenValido, fechaInicioValida, fechaFinValida);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorPuertoOrigenNull() {
    	inicializaValores();
        new TCamion(muelleOrigenValido, muelleDestinoValido, null, puertoDestinoValido, fechaInicioValida, fechaFinValida);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorPuertoDestinoNull() {
    	inicializaValores();
        new TCamion(muelleOrigenValido, muelleDestinoValido, puertoOrigenValido, null, fechaInicioValida, fechaFinValida);
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
	
	
	@Test (expected = IllegalArgumentException.class)
	public void testIsFechaFinSuperiorAOtraNula() {
		inicializaValores();
		assertFalse(tTren.isFechaFinPosterior(null));
	}

    
    @Test
    public void testToString() {
    	inicializaValores();
        String stringEsperado = "<Inicio trayecto>\n\t<Pais del Puerto Origen: ES>\n\t<Localidad del Puerto Origen: UNO>\n\t<Fecha de Inicio de Trayecto: 2024-11-08>"
                + "\n<Fin trayecto>\n\t<Pais del Puerto Destino: PD>\n\t<Localidad del Puerto Destino: DOS>\n\t<Fecha de Inicio de Trayecto: 2024-11-28>\n";

        assertEquals(stringEsperado, packCamionBarco.toString());
    }

   
}
