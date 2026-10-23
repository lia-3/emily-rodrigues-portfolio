package uva.poo.puerto;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.ArrayList;
import uva.poo.contenedor.Contenedor;
import uva.poo.contenedor.EstadoContenedor;
import uva.poo.contenedor.Estandar;
import uva.poo.contenedor.FlatRack;
import uva.poo.contenedor.UnidadesPeso;
import uva.poo.contenedor.UnidadesVolumen;


/**
 * Prueba la clase PlazaMuelle
 * @author Marta Pérez
 * @author Emily Rodrigues
 */
public class PlazaMuelleTest {

	private static final Contenedor CONTENEDOR_VALIDO = new Estandar("CSQU3054383", 5000.0, 20000.0, UnidadesPeso.KILOGRAMOS, 33.0, UnidadesVolumen.METROS_CUBICOS, EstadoContenedor.RECOGIDA);
	private static final Contenedor CONTENEDOR_SIN_TECHO = new FlatRack("AZXJ0091237", 4000.0, 30000.0, UnidadesPeso.KILOGRAMOS, 33.0, UnidadesVolumen.METROS_CUBICOS, EstadoContenedor.RECOGIDA);

	 
    @Test
    public void testConstructorSinContenedoresValido() {
        String identificador = "3";
        int alturaMaxima = 6;
        PlazaMuelle plazaMuelle = new PlazaMuelle(identificador, alturaMaxima);
        assertEquals(identificador, plazaMuelle.getIdentificador());
        assertEquals(alturaMaxima, plazaMuelle.getAlturaMaxima());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorIdentificadorNull() {
        new PlazaMuelle(null, 6);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorIdYListaContenedoresNull() {
        new PlazaMuelle(null, 6, null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorAlturaMaximaNegativa() {
        new PlazaMuelle("3", -1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorAlturaMaximaNegativaConContenedores() {
    	ArrayList<Contenedor> contenedores = new ArrayList<>();
        new PlazaMuelle("3", -1, contenedores);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorListaContenedoresNull() {
        new PlazaMuelle("3", 6, null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorListaContenedoresExcedeAlturaMaxima() {
        ArrayList<Contenedor> contenedores = new ArrayList<>();
        contenedores.add(CONTENEDOR_VALIDO);
        contenedores.add(CONTENEDOR_VALIDO);
        new PlazaMuelle("3", 1, contenedores);
    }
    
    @Test
    public void testConstructorValidoConContenedores() {
        ArrayList<Contenedor> contenedores = new ArrayList<>();
        contenedores.add(CONTENEDOR_VALIDO);
        PlazaMuelle plaza = new PlazaMuelle("3", 6, contenedores);
        assertEquals("3", plaza.getIdentificador());
        assertEquals(6, plaza.getAlturaMaxima());
        assertEquals(1, plaza.getContenedores().size());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorContenedoresDuplicados() {
        ArrayList<Contenedor> contenedores = new ArrayList<>();
        contenedores.add(CONTENEDOR_VALIDO);
        contenedores.add(CONTENEDOR_VALIDO); 
        new PlazaMuelle("3", 10, contenedores); 
    }
    
    @Test(expected = IllegalStateException.class)
    public void testAgregarContenedorPlazaLlena() {
        PlazaMuelle plaza = new PlazaMuelle("1", 1);
        plaza.agregarContenedor(CONTENEDOR_VALIDO);
        plaza.agregarContenedor(CONTENEDOR_SIN_TECHO);
    }

    @Test
    public void testAgregarContenedorExitoso() {
        PlazaMuelle plaza = new PlazaMuelle("1", 2);
        Contenedor contenedor = CONTENEDOR_VALIDO;
        plaza.agregarContenedor(contenedor);
        assertTrue(plaza.estaContenedor(contenedor));
    }
    
    @Test(expected = IllegalStateException.class)
    public void testAgregarContenedorSobreContenedorQueNoTieneTecho() {
        PlazaMuelle plaza = new PlazaMuelle("1", 2);
        Contenedor contenedor1 = CONTENEDOR_SIN_TECHO;
        Contenedor contenedor2 = CONTENEDOR_VALIDO;
        plaza.agregarContenedor(contenedor1);
        plaza.agregarContenedor(contenedor2);
    }
    
    @Test
    public void testSePuedeApilarVacio() {
    	PlazaMuelle plaza = new PlazaMuelle("1", 2);
        assertTrue(plaza.sePuedeApilar());
    }
    
    @Test
    public void testSePuedeApilarNoVacio() {
    	PlazaMuelle plaza = new PlazaMuelle("1", 2);
    	Contenedor contenedor1 = CONTENEDOR_VALIDO;
        plaza.agregarContenedor(contenedor1);
        assertTrue(plaza.sePuedeApilar());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testContenedoresConDuplicados() {
    	PlazaMuelle plaza = new PlazaMuelle("0", 2);

    	plaza.agregarContenedor(CONTENEDOR_VALIDO);
    	plaza.agregarContenedor(CONTENEDOR_VALIDO); 
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testQuitarContenedorNoExistente() {
        PlazaMuelle plaza = new PlazaMuelle("1", 2);
        plaza.quitarContenedor(CONTENEDOR_VALIDO);
    }

    @Test
    public void testQuitarContenedorExitoso() {
        PlazaMuelle plaza = new PlazaMuelle("1", 2);
        Contenedor contenedor = CONTENEDOR_VALIDO;
        plaza.agregarContenedor(contenedor);
        plaza.quitarContenedor(contenedor);
        assertFalse(plaza.estaContenedor(contenedor));
    }
    
    @Test
    public void testEstaVacioInicialmente() {
        PlazaMuelle plaza = new PlazaMuelle("1", 2);
        assertTrue(plaza.estaVacio());
    }

    @Test
    public void testEstaLleno() {
        PlazaMuelle plaza = new PlazaMuelle("1", 1);
        plaza.agregarContenedor(CONTENEDOR_VALIDO);
        assertTrue(plaza.estaLleno());
    }
    
    @Test
    public void testEstaLlenoFalse() {
        PlazaMuelle plaza = new PlazaMuelle("1", 2);
        plaza.agregarContenedor(CONTENEDOR_VALIDO);
        assertFalse(plaza.estaLleno());
    }

    @Test
    public void testEstaSemiLleno() {
        PlazaMuelle plaza = new PlazaMuelle("1", 3);
        plaza.agregarContenedor(CONTENEDOR_VALIDO);
        assertTrue(plaza.estaSemiLleno());
    }
    
    @Test
    public void testEstaSemiLlenoFalsePorqueEstaLleno() {
        PlazaMuelle plaza = new PlazaMuelle("1", 1);
        plaza.agregarContenedor(CONTENEDOR_VALIDO);
        assertFalse(plaza.estaSemiLleno());
    }
    
    @Test
    public void testEstaSemiLlenoFalsePorqueEstaVacio() {
        PlazaMuelle plaza = new PlazaMuelle("1", 1);
        assertFalse(plaza.estaSemiLleno());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testEstaContenedorConStringContenedorNulo() {
    	PlazaMuelle plaza = new PlazaMuelle("1", 3);
        plaza.estaContenedorPorId(null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testEstaContenedorConContenedorNulo() {
    	PlazaMuelle plaza = new PlazaMuelle("1", 3);
        plaza.estaContenedor(null);
    }
    
    @Test
    public void testEstaContenedorPorStringValido() {
    	PlazaMuelle plaza = new PlazaMuelle("1", 3);
        plaza.agregarContenedor(CONTENEDOR_VALIDO);
        assertTrue(plaza.estaContenedor(CONTENEDOR_VALIDO));
    }
    
    @Test
    public void testNoEstaContenedorValido() {
    	PlazaMuelle plaza = new PlazaMuelle("1", 3);
        assertFalse(plaza.estaContenedorPorId("CSQU3054383"));
    }
    
    @Test
    public void testEstaContenedorPorStringExistente() {
        PlazaMuelle plaza = new PlazaMuelle("1", 3);
        plaza.agregarContenedor(CONTENEDOR_VALIDO);
        assertTrue(plaza.estaContenedorPorId(CONTENEDOR_VALIDO.getIdentificador()));
    }

    @Test
    public void testNoEstaContenedorPorStringValido() {
    	PlazaMuelle plaza = new PlazaMuelle("1", 3);
    	plaza.agregarContenedor(CONTENEDOR_VALIDO);
        assertFalse(plaza.estaContenedorPorId("CSQU3054381"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAnnadirListaExcedeAlturaMaxima() {
        PlazaMuelle plaza = new PlazaMuelle("1", 2);
        ArrayList<Contenedor> nuevosContenedores = new ArrayList<>();
        nuevosContenedores.add(CONTENEDOR_VALIDO);
        nuevosContenedores.add(CONTENEDOR_VALIDO);
        nuevosContenedores.add(CONTENEDOR_VALIDO);
        plaza.annadirLista(nuevosContenedores);
    }

    @Test
    public void testAnnadirListaExitoso() {
        PlazaMuelle plaza = new PlazaMuelle("1", 3);
        ArrayList<Contenedor> nuevosContenedores = new ArrayList<>();
        nuevosContenedores.add(CONTENEDOR_VALIDO);
        nuevosContenedores.add(CONTENEDOR_VALIDO);
        plaza.annadirLista(nuevosContenedores);
        assertEquals(2, plaza.getContenedores().size());
    }
    
    @Test
    public void testVaciarContenedores() {
        PlazaMuelle plaza = new PlazaMuelle("1", 5);
        plaza.agregarContenedor(CONTENEDOR_VALIDO);
        assertEquals(1, plaza.getContenedores().size());
        
        plaza.vaciar();
        
        assertTrue(plaza.estaVacio());
        assertEquals(0, plaza.getContenedores().size());
    }
    
    @Test
    public void testCopiarPlazaMuelle() {
        ArrayList<Contenedor> contenedores = new ArrayList<>();
        contenedores.add(CONTENEDOR_VALIDO);
        PlazaMuelle plazaOriginal = new PlazaMuelle("1", 5, contenedores);
        
        PlazaMuelle plazaCopia = plazaOriginal.copiarPlazaMuelle();
        
        assertEquals(plazaOriginal.getIdentificador(), plazaCopia.getIdentificador());
        assertEquals(plazaOriginal.getAlturaMaxima(), plazaCopia.getAlturaMaxima());
        assertEquals(plazaOriginal.getContenedores().size(), plazaCopia.getContenedores().size());
        assertTrue(plazaCopia.estaContenedor(CONTENEDOR_VALIDO));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testEqualsConOtroNull() {
        PlazaMuelle plaza = new PlazaMuelle("1", 3);
        plaza.equals(null);  
    }

    @Test
    public void testEqualsExitoso() {
        PlazaMuelle plaza1 = new PlazaMuelle("1", 3);
        PlazaMuelle plaza2 = new PlazaMuelle("1", 3);
        assertTrue(plaza1.equals(plaza2));
    }

    @Test
    public void testEqualsFalso() {
        PlazaMuelle plaza1 = new PlazaMuelle("1", 3);
        PlazaMuelle plaza2 = new PlazaMuelle("2", 3);
        assertFalse(plaza1.equals(plaza2));
    }

    @Test
    public void testEqualsDiferenteCantidadContenedores() {
        PlazaMuelle plaza1 = new PlazaMuelle("1", 3);
        PlazaMuelle plaza2 = new PlazaMuelle("1", 3);

        plaza1.agregarContenedor(CONTENEDOR_SIN_TECHO);

        assertFalse(plaza1.equals(plaza2));
    }
    
    @Test
    public void testEqualsDiferenteAlturaMaxim2() {
        PlazaMuelle plaza1 = new PlazaMuelle("1", 2);
        PlazaMuelle plaza2 = new PlazaMuelle("1", 3);

        assertFalse(plaza1.equals(plaza2));
    }
    
    @Test
    public void testCloneExitoso() {
    	ArrayList<Contenedor> contenedores = new ArrayList<>();
        contenedores.add(CONTENEDOR_VALIDO);
        contenedores.add(CONTENEDOR_SIN_TECHO);
        PlazaMuelle plaza = new PlazaMuelle("3", 3, contenedores);
        PlazaMuelle copiaPlaza = plaza.clone();
        assertEquals(plaza, copiaPlaza);
    }
    
    @Test
    public void testToString() {
        PlazaMuelle plaza = new PlazaMuelle("1", 3);
        plaza.agregarContenedor(CONTENEDOR_VALIDO);
        String expectedString = "Identificador:1\nAltura Maxima: 3\nLista de Contenedores: [" + CONTENEDOR_VALIDO.toString() + "]";
        
        assertEquals(expectedString, plaza.toString().trim());
    }


}