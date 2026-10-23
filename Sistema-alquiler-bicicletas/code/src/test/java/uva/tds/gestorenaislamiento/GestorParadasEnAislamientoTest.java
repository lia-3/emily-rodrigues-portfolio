package uva.tds.gestorenaislamiento;
import uva.tds.interfaces.IParadaRepositorio;
import uva.tds.base.*;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.easymock.EasyMock;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


/**
 * Clase de tests que prueba la clase GestorParadasEnAislamiento
 * @author Emily Rodrigues
 */
public class GestorParadasEnAislamientoTest {

    @TestSubject
    private GestorParadasEnAislamiento gestor;

    @Mock
    private IParadaRepositorio paradaRepositorio;

    Bicicleta normal;
    Bicicleta electrica;
    Bicicleta biciOcupada;
    Bicicleta nueva;

    Parada parada1;
    Parada parada2;
    Usuario usuario;
    Usuario otroUsuario;
    String identificadorBici;
    String identificadorElectrica;
    String identificadorParada1;
    String identificadorParada2;
    String direccion;
    String nif;
    int nivelBateria;
    double lat1;
    double lon1;
    double lat2;
    double lon2;

    ArrayList <Bicicleta> bicicletas;
    ArrayList <Bicicleta> bicicletas2;
    ArrayList <Bicicleta> bicicletasDisponibles;

    static final int NIVEL_BATERIA=10;
    static final int HUECOS_APARCAMIENTO =4;

    ArrayList<Parada> paradasMock;

    @BeforeEach
    void startUp() {
        paradaRepositorio = EasyMock.mock(IParadaRepositorio.class);

        identificadorBici = "1111";
        identificadorElectrica = "2222";
        identificadorParada1="id1";
        identificadorParada2="id2";
        nivelBateria = 10;
        lat1=-50;
        lon1=100;
        lat2=-60;
        lon2=90;

        direccion="C/Manuel Azaña N7 1A";
        nif = "54802723W";
        normal= new Bicicleta(identificadorBici);
        nueva = new Bicicleta ("id2", EstadoBicicleta.BLOQUEADA);
        electrica = new Bicicleta(identificadorElectrica, nivelBateria);
        biciOcupada = new Bicicleta("ocup", EstadoBicicleta.OCUPADA);

        usuario= new Usuario ("Juan", nif,5,true);
        otroUsuario= new Usuario ("Juan", "16350874J",5,true);

        bicicletas= new ArrayList <Bicicleta>();
        bicicletas.add(normal);
        bicicletas.add(electrica);
        
        bicicletas2= new ArrayList <Bicicleta>();
        bicicletas2.add(electrica);
        
        parada1 = new Parada (identificadorParada1,lat1,lon1,direccion,bicicletas,HUECOS_APARCAMIENTO,true);
        parada2 = new Parada (identificadorParada2,lat2,lon2,direccion,bicicletas2,HUECOS_APARCAMIENTO,false);

        bicicletasDisponibles= new ArrayList <Bicicleta>();
        bicicletasDisponibles.add(normal);
        bicicletasDisponibles.add(electrica);

        paradasMock = new ArrayList<>();
        paradasMock.add(parada1);
    }

    @Test
    public void testGestorParadasEnAislamiento() {
        ArrayList<Parada> paradas = new ArrayList<>();
        paradas.add(parada1);
        paradas.add(parada2);
        ArrayList<Parada> paradasDisponibles = new ArrayList<>();
        paradasDisponibles.add(parada1);

        EasyMock.expect(paradaRepositorio.getParadas()).andReturn(paradas).times(3);

        EasyMock.replay(paradaRepositorio);
        gestor = new GestorParadasEnAislamiento(paradaRepositorio);
        assertArrayEquals(paradas.toArray(), gestor.getParadas().toArray());
        assertArrayEquals(paradasDisponibles.toArray(), gestor.getParadasActivas().toArray());
        assertEquals(gestor.getAparcamientosDisponibles(identificadorParada1), 2);
        EasyMock.verify(paradaRepositorio);
    }

    @Test
    public void testGestorParadasEnAislamientoCuandoElObjetoDeInterfazEsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            gestor = new GestorParadasEnAislamiento(null);
        });
    }

    @Test
    public void testGestorParadasEnAislamientoCuandoNoHayParadasEnElRepositorio() {
        ArrayList<Parada> paradas = new ArrayList<>();
        ArrayList<Parada> paradasDisponibles = new ArrayList<>();

        EasyMock.expect(paradaRepositorio.getParadas()).andReturn(paradas).times(2);
        EasyMock.replay(paradaRepositorio);

        gestor = new GestorParadasEnAislamiento(paradaRepositorio);

        assertArrayEquals(paradas.toArray(), gestor.getParadas().toArray());
        assertArrayEquals(paradasDisponibles.toArray(), gestor.getParadasActivas().toArray());

        EasyMock.verify(paradaRepositorio);
    }

    @Test
    public void testAnadirParada() {
        ArrayList<Parada> listaParadasEsperada = new ArrayList<>();
        listaParadasEsperada.add(parada1);
        listaParadasEsperada.add(parada2);

        paradaRepositorio.anadirParada(parada1);
        EasyMock.expectLastCall().times(1);
        paradaRepositorio.anadirParada(parada2);
        EasyMock.expectLastCall().times(1);
        EasyMock.expect(paradaRepositorio.getParadas()).andReturn(listaParadasEsperada).times(1);

        EasyMock.replay(paradaRepositorio);

        gestor = new GestorParadasEnAislamiento(paradaRepositorio);
        gestor.anadirParada(parada1);
        gestor.anadirParada(parada2);

        assertArrayEquals(listaParadasEsperada.toArray(), gestor.getParadas().toArray());
        EasyMock.verify(paradaRepositorio);
    }

    @Test
    public void testAnadirParadaRepetida() {
        Parada paradaRepetida = new Parada(identificadorParada1, -50, 100, direccion, bicicletas, HUECOS_APARCAMIENTO, true);

        paradaRepositorio.anadirParada(parada1);
        EasyMock.expectLastCall().times(1);
        paradaRepositorio.anadirParada(paradaRepetida);
        EasyMock.expectLastCall().andThrow(new IllegalArgumentException());

        EasyMock.replay(paradaRepositorio);

        gestor = new GestorParadasEnAislamiento(paradaRepositorio);
        gestor.anadirParada(parada1);

        assertThrows(IllegalArgumentException.class, () -> {
            gestor.anadirParada(paradaRepetida);
        });

        EasyMock.verify(paradaRepositorio);
    }


    @Test
    public void testAgregarBicicletaSinHuecos(){
        biciOcupada.setEstadoDisponible();
        parada1.agregaBicicleta(biciOcupada);

        paradaRepositorio.anadirParada(parada1);
        EasyMock.expectLastCall().times(1);

        paradaRepositorio.agregarBicicleta(identificadorParada1, nueva);
        EasyMock.expectLastCall().times(1);

        paradaRepositorio.agregarBicicleta(identificadorParada1, new Bicicleta ("id4"));
        EasyMock.expectLastCall().andThrow(new IllegalStateException());

        EasyMock.replay(paradaRepositorio);
        gestor = new GestorParadasEnAislamiento(paradaRepositorio);
        
        gestor.anadirParada(parada1);
        gestor.agregarBicicleta(identificadorParada1,nueva);
        assertThrows (IllegalStateException.class, ()->{
            gestor.agregarBicicleta(identificadorParada1,new Bicicleta ("id4"));
        });
       EasyMock.verify(paradaRepositorio);
    }

    @Test
    public void testAgregarBicicletaParadaNoEsta(){
        biciOcupada.setEstadoDisponible();
        parada1.agregaBicicleta(biciOcupada);

        paradaRepositorio.anadirParada(parada1);
        EasyMock.expectLastCall().times(1);

        paradaRepositorio.agregarBicicleta(identificadorParada1, nueva);
        EasyMock.expectLastCall().times(1);

        paradaRepositorio.agregarBicicleta(identificadorParada2, new Bicicleta ("id4"));
        EasyMock.expectLastCall().andThrow(new IllegalArgumentException());

        EasyMock.replay(paradaRepositorio);
        gestor = new GestorParadasEnAislamiento(paradaRepositorio);
        gestor.anadirParada(parada1);
        gestor.agregarBicicleta(identificadorParada1,nueva);
        assertThrows (IllegalArgumentException.class, ()->{
            gestor.agregarBicicleta(identificadorParada2,new Bicicleta ("id4"));
        });
       EasyMock.verify(paradaRepositorio);
    }

    @Test
    public void testAgregarBicicletaMismoIdEnLaMismaParada(){
        paradaRepositorio.anadirParada(parada1);
        EasyMock.expectLastCall().times(1);

        paradaRepositorio.agregarBicicleta(identificadorParada1, nueva);
        EasyMock.expectLastCall().times(1);

        paradaRepositorio.agregarBicicleta(identificadorParada1, new Bicicleta ("id2"));
        EasyMock.expectLastCall().andThrow(new IllegalArgumentException());

        EasyMock.replay(paradaRepositorio);
        gestor = new GestorParadasEnAislamiento(paradaRepositorio);
        gestor.anadirParada(parada1);
        gestor.agregarBicicleta(identificadorParada1,nueva);
        assertThrows (IllegalArgumentException.class, ()->{
            gestor.agregarBicicleta(identificadorParada1,new Bicicleta ("id2"));
        });
        EasyMock.verify(paradaRepositorio);
    }


    @Test
    public void testAgregarBicicletaMismoIdEnOtraParada(){
        parada2.setEstado(true);

        paradaRepositorio.anadirParada(parada1);
        EasyMock.expectLastCall().times(1);
        
        paradaRepositorio.anadirParada(parada2);
        EasyMock.expectLastCall().times(1);

        paradaRepositorio.agregarBicicleta(identificadorParada1, nueva);
        EasyMock.expectLastCall().times(1);
        
        paradaRepositorio.agregarBicicleta(identificadorParada2, new Bicicleta ("id2"));
        EasyMock.expectLastCall().andThrow(new IllegalArgumentException());

        EasyMock.replay(paradaRepositorio);
        gestor = new GestorParadasEnAislamiento(paradaRepositorio);
        gestor.anadirParada(parada1);
        gestor.anadirParada(parada2);
        gestor.agregarBicicleta(identificadorParada1, nueva);
        assertThrows (IllegalArgumentException.class, ()->{
            gestor.agregarBicicleta(identificadorParada2,new Bicicleta ("id2"));
        });
        EasyMock.verify(paradaRepositorio);
    }


    @Test
    public void testAgregarBicicletaNula() {
        paradaRepositorio.anadirParada(parada1);
        EasyMock.expectLastCall().times(1);

        EasyMock.replay(paradaRepositorio);
        gestor = new GestorParadasEnAislamiento(paradaRepositorio);
        gestor.anadirParada(parada1);
        assertThrows (IllegalArgumentException.class, ()->{
            gestor.agregarBicicleta(identificadorParada1, null);
        });
        EasyMock.verify(paradaRepositorio);
    }


    @Test
    public void testAgregarBicicletaConIdParadaNull() {
        paradaRepositorio.anadirParada(parada1);
        EasyMock.expectLastCall().times(1);

        EasyMock.replay(paradaRepositorio);
        gestor = new GestorParadasEnAislamiento(paradaRepositorio);
        gestor.anadirParada(parada1);
        assertThrows (IllegalArgumentException.class, ()->{
            gestor.agregarBicicleta(null, nueva);
        });
        EasyMock.verify(paradaRepositorio);
    }

   
     @Test 
     public void testEliminarBicicletaValida(){
        ArrayList<Bicicleta> listaDespuesDeQuitarBici = new ArrayList<>();
        listaDespuesDeQuitarBici.add(electrica);
        Parada copiaParada1 = new Parada (identificadorParada1,lat1,lon1,direccion, listaDespuesDeQuitarBici ,HUECOS_APARCAMIENTO,true);
        ArrayList<Parada> listaParadasMock = new ArrayList<>();
        listaParadasMock.add(copiaParada1);

        paradaRepositorio.anadirParada(parada1);
        EasyMock.expectLastCall().times(1);

        paradaRepositorio.eliminarBicicleta(identificadorParada1,identificadorBici);
        EasyMock.expectLastCall().times(1);
        EasyMock.expect(paradaRepositorio.getParadas()).andReturn(listaParadasMock);

        EasyMock.replay(paradaRepositorio);
        gestor =  new GestorParadasEnAislamiento(paradaRepositorio);
        gestor.anadirParada(parada1);
        gestor.eliminarBicicleta(identificadorParada1,identificadorBici);
        bicicletasDisponibles.remove(normal);
        assertArrayEquals(bicicletasDisponibles.toArray(), gestor.getBicicletasParada(identificadorParada1).toArray());
        EasyMock.verify(paradaRepositorio);
     }


     @Test 
     public void testEliminarBicicletaConIdentificadorDeBiciNulo(){
        paradaRepositorio.anadirParada(parada1);
        EasyMock.expectLastCall().times(1);

        paradaRepositorio.eliminarBicicleta(identificadorParada1,null);
        EasyMock.expectLastCall().andThrow(new IllegalArgumentException());

        EasyMock.replay(paradaRepositorio);
        gestor =  new GestorParadasEnAislamiento(paradaRepositorio);
        gestor.anadirParada(parada1);
        assertThrows(IllegalArgumentException.class, () -> { 
            gestor.eliminarBicicleta(identificadorParada1, null);
        });
        EasyMock.verify(paradaRepositorio);
    }


    @Test 
     public void testEliminarBicicletaConIdentificadorDeParadaNulo(){
        paradaRepositorio.anadirParada(parada1);
        EasyMock.expectLastCall().times(1);

        paradaRepositorio.eliminarBicicleta(null,identificadorBici);
        EasyMock.expectLastCall().andThrow(new IllegalArgumentException());

        EasyMock.replay(paradaRepositorio);
        gestor =  new GestorParadasEnAislamiento(paradaRepositorio);
        gestor.anadirParada(parada1);
        assertThrows(IllegalArgumentException.class, () -> { 
            gestor.eliminarBicicleta(null, identificadorBici);
        });
        EasyMock.verify(paradaRepositorio);
    }


    @Test 
     public void testEliminarBicicletaConUnaParadaNoExistente(){
        paradaRepositorio.anadirParada(parada1);
        EasyMock.expectLastCall().times(1);

        paradaRepositorio.eliminarBicicleta(identificadorParada2,identificadorBici);
        EasyMock.expectLastCall().andThrow(new IllegalArgumentException());

        EasyMock.replay(paradaRepositorio);
        gestor =  new GestorParadasEnAislamiento(paradaRepositorio);
        gestor.anadirParada(parada1);
        assertThrows(IllegalArgumentException.class, () -> { 
            gestor.eliminarBicicleta(identificadorParada2, identificadorBici);
        });
        EasyMock.verify(paradaRepositorio);
    }


    @Test 
     public void testEliminarBicicletaConUnaBiciNoExistenteEnLaParada(){
        paradaRepositorio.anadirParada(parada1);
        EasyMock.expectLastCall().times(1);

        paradaRepositorio.eliminarBicicleta(identificadorParada1,"no");
        EasyMock.expectLastCall().andThrow(new IllegalArgumentException());

        EasyMock.replay(paradaRepositorio);
        gestor =  new GestorParadasEnAislamiento(paradaRepositorio);
        gestor.anadirParada(parada1);
        assertThrows(IllegalArgumentException.class, () -> { 
            gestor.eliminarBicicleta(identificadorParada1, "no");
        });
        EasyMock.verify(paradaRepositorio);
    }

    @Test 
     public void testEliminarBicicletaCuandoEstaOCupada(){
        parada1.agregaBicicleta(biciOcupada);
        paradaRepositorio.anadirParada(parada1);
        EasyMock.expectLastCall().times(1);

        paradaRepositorio.eliminarBicicleta(identificadorParada1, biciOcupada.getIdentificador());
        EasyMock.expectLastCall().andThrow(new IllegalArgumentException());

        EasyMock.replay(paradaRepositorio);
        gestor =  new GestorParadasEnAislamiento(paradaRepositorio);
        gestor.anadirParada(parada1);
        assertThrows(IllegalArgumentException.class, () -> { 
            gestor.eliminarBicicleta(identificadorParada1, biciOcupada.getIdentificador());
        });
        EasyMock.verify(paradaRepositorio);
    }

     @Test
     public void testAlquilarBicicleta(){
        Alquiler alquiler = new Alquiler(normal, usuario);
        electrica.setEstadoDisponible();
        Alquiler alquilerElectrica = new Alquiler(electrica, otroUsuario);
        ArrayList<Alquiler> alquileresMock = new ArrayList<>();
        alquileresMock.add(alquilerElectrica);
        alquileresMock.add(alquiler);

        paradaRepositorio.anadirParada(parada1);
        EasyMock.expectLastCall().times(1);
        paradaRepositorio.alquilarBicicleta(identificadorParada1, identificadorBici, usuario);
        EasyMock.expectLastCall().times(1);
        EasyMock.expect(paradaRepositorio.getAlquileresEnCurso()).andReturn(alquileresMock).times(4);
        EasyMock.expect(paradaRepositorio.tieneUsuarioUnaReserva(usuario)).andReturn(false).times(1);

        EasyMock.replay(paradaRepositorio);
        gestor = new GestorParadasEnAislamiento(paradaRepositorio);
        gestor.anadirParada(parada1);
        gestor.alquilarBicicleta(identificadorParada1, identificadorBici, usuario);

        assertEquals(gestor.getAlquileresEnCurso().size(), 2);
        assertEquals(gestor.getAlquileresEnCurso().get(1).getBicicleta().getIdentificador(), identificadorBici);
        assertEquals(gestor.getAlquileresEnCurso().get(1).getUsuario().getNif(), nif);
        assertTrue(gestor.tieneAlquilerEnCurso(usuario.getNif()));
        assertFalse(gestor.tieneUsuarioUnaReserva(usuario));
        EasyMock.verify(paradaRepositorio);
      }

    
    @Test
    public void testTieneUsuarioUnaReservaCuandoUsuarioNull() {
        paradaRepositorio.anadirParada(parada1);
        EasyMock.expectLastCall().times(1);
        paradaRepositorio.tieneUsuarioUnaReserva(null);
        EasyMock.expectLastCall().andThrow(new IllegalArgumentException());

        EasyMock.replay(paradaRepositorio);
        gestor = new GestorParadasEnAislamiento(paradaRepositorio);
        gestor.anadirParada(parada1);
        assertThrows(IllegalArgumentException.class, () -> {
            gestor.tieneUsuarioUnaReserva(null);
        });
        EasyMock.verify(paradaRepositorio);
    }


     @Test
     public void testAlquilarBicicletaReservada(){
        Alquiler alquiler = new Alquiler(normal, usuario);
        Bicicleta conAlquiler = new Bicicleta("alqu", 10);
        Alquiler electricaAlquiler = new Alquiler(conAlquiler, otroUsuario);
        ArrayList<Alquiler> listaAlquileresMock = new ArrayList<>();
        listaAlquileresMock.add(electricaAlquiler);
        listaAlquileresMock.add(alquiler);        
        
        paradaRepositorio.anadirParada(parada1);
        EasyMock.expectLastCall().times(1);
        paradaRepositorio.reservaBicicleta(identificadorParada1, identificadorBici, usuario);
        EasyMock.expectLastCall().times(1);
        paradaRepositorio.alquilarBicicleta(identificadorParada1, identificadorBici, usuario);
        EasyMock.expectLastCall().times(1);
        EasyMock.expect(paradaRepositorio.tieneUsuarioUnaReserva(usuario)).andReturn(true).times(1);
        EasyMock.expect(paradaRepositorio.getAlquileresEnCurso()).andReturn(listaAlquileresMock).times(1);

        EasyMock.replay(paradaRepositorio);
        gestor = new GestorParadasEnAislamiento(paradaRepositorio);
        gestor.anadirParada(parada1);
        gestor.reservaBicicleta(identificadorParada1, identificadorBici, usuario);
        gestor.alquilarBicicleta(identificadorParada1, identificadorBici, usuario);
        assertEquals(gestor.getAlquiler(nif, identificadorBici).getBicicleta(),normal);
        assertTrue(gestor.tieneUsuarioUnaReserva(usuario));
        EasyMock.verify(paradaRepositorio);
     }

     @Test
     public void testAlquilarBicicletaBloqueada(){
        paradaRepositorio.anadirParada(parada1);
        EasyMock.expectLastCall().times(1);
        EasyMock.expect(paradaRepositorio.getParadas()).andReturn(paradasMock).times(1);
        paradaRepositorio.alquilarBicicleta(identificadorParada1, identificadorBici, usuario);
        EasyMock.expectLastCall().andThrow(new IllegalStateException());

        EasyMock.replay(paradaRepositorio);
        gestor = new GestorParadasEnAislamiento(paradaRepositorio);
        gestor.anadirParada(parada1);
        gestor.getParada(identificadorParada1).setBicicletaEstadoBloqueada(identificadorBici);
        assertThrows (IllegalStateException.class, ()->{
            gestor.alquilarBicicleta(identificadorParada1, identificadorBici, usuario );
        });
        EasyMock.verify(paradaRepositorio);
     }

     @Test
     public void testAlquilarBicicletaNoEsta(){
        paradaRepositorio.anadirParada(parada1);
        EasyMock.expectLastCall().times(1);
        paradaRepositorio.alquilarBicicleta(identificadorParada1, "no", usuario);
        EasyMock.expectLastCall().andThrow(new IllegalArgumentException());

        EasyMock.replay(paradaRepositorio);
        gestor = new GestorParadasEnAislamiento(paradaRepositorio);
        gestor.anadirParada(parada1);
        assertThrows (IllegalArgumentException.class, ()->{
            gestor.alquilarBicicleta(identificadorParada1, "no" ,usuario );
        });
        EasyMock.verify(paradaRepositorio);
     }

     @Test
     public void testAlquilarBicicletaParadaNoEsta(){
        paradaRepositorio.anadirParada(parada1);
        EasyMock.expectLastCall().times(1);
        paradaRepositorio.alquilarBicicleta(identificadorParada2, identificadorBici, usuario);
        EasyMock.expectLastCall().andThrow(new IllegalArgumentException());

        EasyMock.replay(paradaRepositorio);
        gestor = new GestorParadasEnAislamiento(paradaRepositorio);
        gestor.anadirParada(parada1);
        assertThrows (IllegalArgumentException.class, ()->{
            gestor.alquilarBicicleta(identificadorParada2, identificadorBici,usuario );
        });
        EasyMock.verify(paradaRepositorio);
     }

     @Test
     public void testAlquilarBicicletaUsuarioInactivo(){
        usuario.setEstado(false);

        paradaRepositorio.anadirParada(parada1);
        EasyMock.expectLastCall().times(1);
        paradaRepositorio.alquilarBicicleta(identificadorParada1, identificadorBici, usuario);
        EasyMock.expectLastCall().andThrow(new IllegalStateException());

        EasyMock.replay(paradaRepositorio);
        gestor = new GestorParadasEnAislamiento(paradaRepositorio);
        gestor.anadirParada(parada1);
        assertThrows (IllegalStateException.class, ()->{
            gestor.alquilarBicicleta(identificadorParada1, identificadorBici,usuario );
        });
        EasyMock.verify(paradaRepositorio);
     }

     @Test
    public void testAlquilarBicicletaCuandoBicicletaReservadaPorOtroUsuarioAntesDeQueSeAcabeElTiempo() {
        Usuario nuevoUsuario = new Usuario("taySwift", "71969192F", 135, true);

        paradaRepositorio.anadirParada(parada1);
        EasyMock.expectLastCall().times(1);
        paradaRepositorio.reservaBicicleta(identificadorParada1, identificadorBici, usuario);
        EasyMock.expectLastCall().times(1);
        paradaRepositorio.alquilarBicicleta(identificadorParada1, identificadorBici, nuevoUsuario);
        EasyMock.expectLastCall().andThrow(new IllegalStateException());
        
        EasyMock.replay(paradaRepositorio);
        gestor = new GestorParadasEnAislamiento(paradaRepositorio);
        gestor.anadirParada(parada1);
        gestor.reservaBicicleta(identificadorParada1, identificadorBici, usuario);
        assertThrows (IllegalStateException.class, ()->{
            gestor.alquilarBicicleta(identificadorParada1, identificadorBici, nuevoUsuario);
        });
        EasyMock.verify(paradaRepositorio);
     }
     
     @Test
     public void testAlquilarBicicletaCuandoElUsuarioTieneOtroAlquiler(){
        electrica.setEstadoDisponible();
        
        paradaRepositorio.anadirParada(parada1);
        EasyMock.expectLastCall().times(1);
        paradaRepositorio.alquilarBicicleta(identificadorParada1, identificadorBici, usuario);
        EasyMock.expectLastCall().times(1);
        paradaRepositorio.alquilarBicicleta(identificadorParada1, identificadorElectrica, usuario);
        EasyMock.expectLastCall().andThrow(new IllegalStateException());
        
        EasyMock.replay(paradaRepositorio);
        gestor = new GestorParadasEnAislamiento(paradaRepositorio);
        gestor.anadirParada(parada1);
        gestor.alquilarBicicleta(identificadorParada1, identificadorBici, usuario );
        assertThrows (IllegalStateException.class, ()->{
            gestor.alquilarBicicleta(identificadorParada1, identificadorElectrica, usuario );
        });
        EasyMock.verify(paradaRepositorio);
     }


    @Test
    public void testAlquilarBicicletaConIdParadaNull() {
        paradaRepositorio.anadirParada(parada1);
        EasyMock.expectLastCall().times(1);
        paradaRepositorio.alquilarBicicleta(null, identificadorBici, usuario);
        EasyMock.expectLastCall().andThrow(new IllegalArgumentException());

        EasyMock.replay(paradaRepositorio);
        gestor = new GestorParadasEnAislamiento(paradaRepositorio);
        gestor.anadirParada(parada1);
        assertThrows(IllegalArgumentException.class, () -> {
            gestor.alquilarBicicleta(null, identificadorBici, usuario);
        });
        EasyMock.verify(paradaRepositorio);
    }


    @Test
    public void testAlquilarBicicletaConIdBiciNull() {
        paradaRepositorio.anadirParada(parada1);
        EasyMock.expectLastCall().times(1);
        paradaRepositorio.alquilarBicicleta(identificadorParada1, null, usuario);
        EasyMock.expectLastCall().andThrow(new IllegalArgumentException());

        EasyMock.replay(paradaRepositorio);
        gestor = new GestorParadasEnAislamiento(paradaRepositorio);
        gestor.anadirParada(parada1);
        assertThrows(IllegalArgumentException.class, () -> {
            gestor.alquilarBicicleta(identificadorParada1, null, usuario);
        });
        EasyMock.verify(paradaRepositorio);
    }


    @Test
    public void testAlquilarBicicletaConUsuarioNull() {
        paradaRepositorio.anadirParada(parada1);
        EasyMock.expectLastCall().times(1);
        paradaRepositorio.alquilarBicicleta(identificadorParada1, identificadorBici, null);
        EasyMock.expectLastCall().andThrow(new IllegalArgumentException());

        EasyMock.replay(paradaRepositorio);
        gestor = new GestorParadasEnAislamiento(paradaRepositorio);
        gestor.anadirParada(parada1);
        assertThrows(IllegalArgumentException.class, () -> {
            gestor.alquilarBicicleta(identificadorParada1, identificadorBici, null);
        });
        EasyMock.verify(paradaRepositorio);
    }


    @Test 
    public void testAlquilarBicicletaCuandoLaParadaEstaDesactivada() {
        paradaRepositorio.anadirParada(parada1);
        EasyMock.expectLastCall().times(1);
        EasyMock.expect(paradaRepositorio.getParadas()).andReturn(paradasMock).times(1);
        paradaRepositorio.desactivarParada(identificadorParada1);
        EasyMock.expectLastCall().times(1);
        paradaRepositorio.alquilarBicicleta(parada1.getIdentificador(), identificadorBici, usuario);
        EasyMock.expectLastCall().andThrow(new IllegalStateException());

        EasyMock.replay(paradaRepositorio);
        gestor = new GestorParadasEnAislamiento(paradaRepositorio);
        gestor.anadirParada(parada1);
        gestor.desactivarParada(identificadorParada1);
        assertThrows(IllegalStateException.class, () -> {
            gestor.alquilarBicicleta(parada1.getIdentificador(), identificadorBici, usuario);});
        EasyMock.verify(paradaRepositorio);
    }


    @Test
    public void testAGetAlquileresEnCursoCuandoNoHayNinguno() {
        paradaRepositorio.anadirParada(parada1);
        EasyMock.expectLastCall().times(1);
        EasyMock.expect(paradaRepositorio.getAlquileresEnCurso()).andReturn(new ArrayList<Alquiler>()).times(1);

        EasyMock.replay(paradaRepositorio);
        gestor = new GestorParadasEnAislamiento(paradaRepositorio);
        gestor.anadirParada(parada1);
        assertTrue(gestor.getAlquileresEnCurso().isEmpty());
        EasyMock.verify();
    }


    @Test 
    public void testTieneAlquilerEnCursoCuandoNifEsNull() {
        paradaRepositorio.anadirParada(parada1);
        EasyMock.expectLastCall().times(1);

        EasyMock.replay(paradaRepositorio);
        gestor = new GestorParadasEnAislamiento(paradaRepositorio);
        gestor.anadirParada(parada1);
        assertThrows(IllegalArgumentException.class, () -> {
            gestor.tieneAlquilerEnCurso(null);
        });
        EasyMock.verify(paradaRepositorio);
    }


    @Test
    public void testGetAlquilerCuandoNifEsNull() {
        paradaRepositorio.anadirParada(parada1);
        EasyMock.expectLastCall().times(1);

        EasyMock.replay(paradaRepositorio);
        gestor = new GestorParadasEnAislamiento(paradaRepositorio);
        gestor.anadirParada(parada1);
        assertThrows(IllegalArgumentException.class, () -> {
            gestor.getAlquiler(null, identificadorBici);
        });
        EasyMock.verify(paradaRepositorio);
    }


    @Test
    public void testGetAlquilerCuandoIdBicicletaNull() {
        paradaRepositorio.anadirParada(parada1);
        EasyMock.expectLastCall().times(1);
        EasyMock.replay(paradaRepositorio);
        gestor = new GestorParadasEnAislamiento(paradaRepositorio);
        gestor.anadirParada(parada1);
        assertThrows(IllegalArgumentException.class, () -> {
            gestor.getAlquiler(nif, null);
        });
        EasyMock.verify(paradaRepositorio);
    }


    @Test
    public void testGetAlquilerCuandoLaBicicletANoEstaAlquilada() {
        Alquiler alquilerNormal = new Alquiler(normal, usuario);
        ArrayList<Alquiler> alquileresMock = new ArrayList<>();
        alquileresMock.add(alquilerNormal);

        paradaRepositorio.anadirParada(parada1);
        EasyMock.expectLastCall().times(1);
        EasyMock.expect(paradaRepositorio.getAlquileresEnCurso()).andReturn(alquileresMock).times(1);

        EasyMock.replay(paradaRepositorio);
        gestor = new GestorParadasEnAislamiento(paradaRepositorio);
        gestor.anadirParada(parada1);
        assertThrows(IllegalStateException.class, () -> {
            gestor.getAlquiler(nif, identificadorElectrica);
        });
        EasyMock.verify(paradaRepositorio);
    }


    @Test
    public void testGetAlquilerSiLaBiciNoTieneAlquiler() {
        ArrayList<Alquiler> alquileresMock = new ArrayList<>();

        paradaRepositorio.anadirParada(parada1);
        EasyMock.expectLastCall().times(1);
        EasyMock.expect(paradaRepositorio.getAlquileresEnCurso()).andReturn(alquileresMock).times(1);

        EasyMock.replay(paradaRepositorio);
        gestor = new GestorParadasEnAislamiento(paradaRepositorio);
        gestor.anadirParada(parada1);
        assertThrows(IllegalStateException.class, () -> {
            gestor.getAlquiler(nif, identificadorElectrica);
        });
        EasyMock.verify(paradaRepositorio);
    }


    @Test
    public void testGetAlquilerSiElDuennoNoTieneAlquiler() {
        ArrayList<Alquiler> alquileresMock = new ArrayList<>();
        paradaRepositorio.anadirParada(parada1);
        EasyMock.expectLastCall().times(1);
        EasyMock.expect(paradaRepositorio.getAlquileresEnCurso()).andReturn(alquileresMock).times(1);

        EasyMock.replay(paradaRepositorio);
        gestor = new GestorParadasEnAislamiento(paradaRepositorio);
        gestor.anadirParada(parada1);
        assertThrows(IllegalStateException.class, () -> {
            gestor.getAlquiler("05647778R", identificadorBici);
        });
        EasyMock.verify(paradaRepositorio);
    }


    @Test
    public void testDevolverBicicleta(){
       ArrayList<Alquiler> alquileresFinales = new ArrayList<>();

       paradaRepositorio.anadirParada(parada1);
       EasyMock.expectLastCall().times(1);
       paradaRepositorio.alquilarBicicleta(identificadorParada1, identificadorBici, usuario); 
       EasyMock.expectLastCall().times(1);
       paradaRepositorio.devolverBicicleta(identificadorParada1, nif, normal);
       EasyMock.expectLastCall().times(1);
       EasyMock.expect(paradaRepositorio.getParadas()).andReturn(paradasMock).times(2);
       EasyMock.expect(paradaRepositorio.getAlquileresEnCurso()).andReturn(alquileresFinales).times(1);

       EasyMock.replay(paradaRepositorio);
       gestor = new GestorParadasEnAislamiento(paradaRepositorio);
       gestor.anadirParada(parada1);
       gestor.alquilarBicicleta(identificadorParada1, identificadorBici, usuario);
       gestor.devolverBicicleta(identificadorParada1, nif, normal);
       assertTrue(gestor.getBicicletasParada(identificadorParada1).contains(normal));
       assertTrue(gestor.getParada(identificadorParada1).getBicicleta(identificadorBici).isDisponible());
       assertFalse(gestor.tieneAlquilerEnCurso(nif));
       EasyMock.verify(paradaRepositorio);
     }

    @Test
    public void testDevolverBicicletaParadaLlena(){
        parada1.agregaBicicleta(nueva);
        biciOcupada.setEstadoDisponible();
        parada1.agregaBicicleta(biciOcupada);

        paradaRepositorio.anadirParada(parada1);
        EasyMock.expectLastCall().times(1);
        paradaRepositorio.devolverBicicleta(identificadorParada1, nif, new Bicicleta("id4"));
        EasyMock.expectLastCall().andThrow(new IllegalStateException());

        EasyMock.replay(paradaRepositorio);
        gestor = new GestorParadasEnAislamiento(paradaRepositorio);
        gestor.anadirParada(parada1);
        assertThrows (IllegalStateException.class, ()->{
            gestor.devolverBicicleta(identificadorParada1, nif, new Bicicleta("id4"));
        });
        EasyMock.verify(paradaRepositorio);
    }


    @Test
    public void testDevolverBicicletaCuandoLaParadaEstaDesactivada(){ 
        paradaRepositorio.anadirParada(parada1);
        EasyMock.expectLastCall().times(1);
        paradaRepositorio.alquilarBicicleta(identificadorParada1, identificadorBici, usuario); 
        EasyMock.expectLastCall().times(1);
        EasyMock.expect(paradaRepositorio.getParadas()).andReturn(paradasMock).times(1);
        paradaRepositorio.desactivarParada(identificadorParada1);
        EasyMock.expectLastCall().times(1);
        paradaRepositorio.devolverBicicleta(identificadorParada1, nif, normal);
        EasyMock.expectLastCall().andThrow(new IllegalStateException());

        EasyMock.replay(paradaRepositorio);
        gestor = new GestorParadasEnAislamiento(paradaRepositorio);
        gestor.anadirParada(parada1);
        gestor.alquilarBicicleta(identificadorParada1, identificadorBici, usuario); 
        gestor.desactivarParada(identificadorParada1);        
        assertThrows (IllegalStateException.class, ()->{
            gestor.devolverBicicleta(identificadorParada1, nif, normal);
        });
        EasyMock.verify(paradaRepositorio);
    }


    @Test
    public void testDevolverBicicletaCuandoLaBiciEstaDisponible(){
        paradaRepositorio.anadirParada(parada1);
        EasyMock.expectLastCall().times(1);
        paradaRepositorio.devolverBicicleta(identificadorParada1, nif, normal);
        EasyMock.expectLastCall().andThrow(new IllegalStateException());

        EasyMock.replay(paradaRepositorio);
        gestor = new GestorParadasEnAislamiento(paradaRepositorio);
        gestor.anadirParada(parada1);  
        assertThrows (IllegalStateException.class, ()->{
            gestor.devolverBicicleta(identificadorParada1, nif, normal);
        });
        EasyMock.verify(paradaRepositorio);
    }


    @Test
    public void testDevolverBicicletaCuandoIdParadaNull(){
        paradaRepositorio.anadirParada(parada1);
        EasyMock.expectLastCall().times(1);
        paradaRepositorio.devolverBicicleta(null, nif, normal);
        EasyMock.expectLastCall().andThrow(new IllegalArgumentException());

        EasyMock.replay(paradaRepositorio);
        gestor = new GestorParadasEnAislamiento(paradaRepositorio);
        gestor.anadirParada(parada1);  
        assertThrows (IllegalArgumentException.class, ()->{
            gestor.devolverBicicleta(null, nif, normal);
        });
        EasyMock.verify(paradaRepositorio);
    }


    @Test
    public void testDevolverBicicletaCuandoNifNull(){
        paradaRepositorio.anadirParada(parada1);
        EasyMock.expectLastCall().times(1);
        paradaRepositorio.devolverBicicleta(identificadorParada1, null, normal);
        EasyMock.expectLastCall().andThrow(new IllegalArgumentException());

        EasyMock.replay(paradaRepositorio);
        gestor = new GestorParadasEnAislamiento(paradaRepositorio);
        gestor.anadirParada(parada1);  
        assertThrows (IllegalArgumentException.class, ()->{
            gestor.devolverBicicleta(identificadorParada1, null, normal);
        });
        EasyMock.verify(paradaRepositorio);
    }


    @Test
    public void testDevolverBicicletaCuandoBiciNull(){
        paradaRepositorio.anadirParada(parada1);
        EasyMock.expectLastCall().times(1);
        paradaRepositorio.devolverBicicleta(identificadorParada1, nif, null);
        EasyMock.expectLastCall().andThrow(new IllegalArgumentException());

        EasyMock.replay(paradaRepositorio);
        gestor = new GestorParadasEnAislamiento(paradaRepositorio);
        gestor.anadirParada(parada1);  
        assertThrows (IllegalArgumentException.class, ()->{
            gestor.devolverBicicleta(identificadorParada1, nif, null);
        });
        EasyMock.verify(paradaRepositorio);
    }


    @Test
    public void testDevolverBicicletaCuandoLaParadaNoEstaEnElGestor(){
        paradaRepositorio.anadirParada(parada1);
        EasyMock.expectLastCall().times(1);
        paradaRepositorio.devolverBicicleta(identificadorParada2, nif, normal);
        EasyMock.expectLastCall().andThrow(new IllegalArgumentException());

        EasyMock.replay(paradaRepositorio);
        gestor = new GestorParadasEnAislamiento(paradaRepositorio);
        gestor.anadirParada(parada1);  
        assertThrows (IllegalArgumentException.class, ()->{
            gestor.devolverBicicleta(identificadorParada2, nif, normal);
        });
        EasyMock.verify(paradaRepositorio);
    }


    @Test
    public void testDevolverBicicletaCuandoElUsuarioNoHizoUnAlquilerSobreEsaBicicleta(){
        paradaRepositorio.anadirParada(parada1);
        EasyMock.expectLastCall().times(1);
        paradaRepositorio.devolverBicicleta(identificadorParada2, "0594778R", normal);
        EasyMock.expectLastCall().andThrow(new IllegalArgumentException());

        EasyMock.replay(paradaRepositorio);
        gestor = new GestorParadasEnAislamiento(paradaRepositorio);
        gestor.anadirParada(parada1);  
        assertThrows (IllegalArgumentException.class, ()->{
            gestor.devolverBicicleta(identificadorParada2, "0594778R", normal);
        });
        EasyMock.verify(paradaRepositorio);
    }


      @Test
     public void testBloquearBicicleta(){
        ArrayList<Bloqueo> bloqueosMock = new ArrayList<>();
        Bloqueo bloqueoEsperado = new Bloqueo(normal);
        Bloqueo bloqueoElectrica = new Bloqueo(electrica);

        bloqueosMock.add(bloqueoElectrica);
        bloqueosMock.add(bloqueoEsperado);
        normal.setEstadoDisponible();

        paradaRepositorio.anadirParada(parada1);
        EasyMock.expectLastCall().times(1);
        EasyMock.expect(paradaRepositorio.getParadas()).andReturn(paradasMock).times(2);
        paradaRepositorio.bloquearBicicleta(identificadorParada1, identificadorElectrica);
        EasyMock.expectLastCall().times(1);
        paradaRepositorio.bloquearBicicleta(identificadorParada1, identificadorBici);
        EasyMock.expectLastCall().times(1);
        EasyMock.expect(paradaRepositorio.getListaBloqueos()).andReturn(bloqueosMock).times(1);
        
        EasyMock.replay(paradaRepositorio);
        gestor = new GestorParadasEnAislamiento(paradaRepositorio);
        gestor.anadirParada(parada1);
        gestor.bloquearBicicleta(identificadorParada1, identificadorElectrica);
        gestor.bloquearBicicleta(identificadorParada1, identificadorBici);
        Bloqueo bloqueoObtenido = gestor.getBloqueo(identificadorBici);
        assertTrue(bloqueoObtenido.getBicicleta().isBloqueada());
        normal.setEstadoBloqueada();
        assertEquals(bloqueoObtenido.getBicicleta(), normal);
        EasyMock.verify(paradaRepositorio);
      }

      
      @Test
     public void testBloquearBicicletaEnParadaEquivocada(){
        paradasMock = new ArrayList<>();
        Parada parada3 = new Parada("3", lat1, lon1, direccion, new ArrayList<Bicicleta>(), HUECOS_APARCAMIENTO, true);
        paradasMock.add(parada1);
        paradasMock.add(parada3);

        paradaRepositorio.anadirParada(parada1);
        EasyMock.expectLastCall().times(1);
        paradaRepositorio.anadirParada(parada3);
        EasyMock.expectLastCall().times(1);
        EasyMock.expect(paradaRepositorio.getParadas()).andReturn(paradasMock).times(1);
        paradaRepositorio.bloquearBicicleta("3", identificadorBici);
        EasyMock.expectLastCall().andThrow(new IllegalArgumentException());

        EasyMock.replay(paradaRepositorio);
        gestor = new GestorParadasEnAislamiento(paradaRepositorio);
        gestor.anadirParada(parada1);
        
        gestor.anadirParada(parada3);
        parada2.setEstado(true);
        assertThrows(IllegalArgumentException.class,()->{
            gestor.bloquearBicicleta("3", identificadorBici);
        });
     }


     @Test
     public void testBloquearBicicletaBloqueada(){
        normal.setEstadoBloqueada();

        paradaRepositorio.anadirParada(parada1);
        EasyMock.expectLastCall().times(1);
        EasyMock.expect(paradaRepositorio.getParadas()).andReturn(paradasMock).times(1);
        paradaRepositorio.bloquearBicicleta(identificadorParada1, identificadorBici);
        EasyMock.expectLastCall().andThrow(new IllegalStateException());

        EasyMock.replay(paradaRepositorio);
        gestor = new GestorParadasEnAislamiento(paradaRepositorio);
        gestor.anadirParada(parada1);        
        assertThrows(IllegalStateException.class,()->{
            gestor.bloquearBicicleta(identificadorParada1, identificadorBici);
        });
        EasyMock.verify(paradaRepositorio);
     }

    @Test
    public void testBloquearBicicletaCuandoLaParadaEstaDesactivada() {
        Parada copiaParada1 = new Parada (identificadorParada1,lat1,lon1,direccion,bicicletas,HUECOS_APARCAMIENTO,false);
        paradasMock = new ArrayList<>();
        paradasMock.add(copiaParada1);

        paradaRepositorio.anadirParada(parada1);
        EasyMock.expectLastCall().times(1);
        paradaRepositorio.desactivarParada(identificadorParada1);
        EasyMock.expectLastCall().times(1);
        EasyMock.expect(paradaRepositorio.getParadas()).andReturn(paradasMock).times(2);
        paradaRepositorio.bloquearBicicleta(identificadorParada1, identificadorBici);
        EasyMock.expectLastCall().andThrow(new IllegalStateException());

        EasyMock.replay(paradaRepositorio);
        gestor = new GestorParadasEnAislamiento(paradaRepositorio);
        gestor.anadirParada(parada1);
        gestor.desactivarParada(identificadorParada1);
        assertThrows(IllegalStateException.class,()->{
            gestor.bloquearBicicleta(identificadorParada1, identificadorBici);
            });
    }


    @Test
    public void testBloquearBicicletaCuandoIdParadaNull() {
        paradaRepositorio.anadirParada(parada1);
        EasyMock.expectLastCall().times(1);

        EasyMock.replay(paradaRepositorio);
        gestor = new GestorParadasEnAislamiento(paradaRepositorio);
        gestor.anadirParada(parada1);
        assertThrows(IllegalArgumentException.class, () -> {
            gestor.bloquearBicicleta(null, identificadorBici);
        });
        EasyMock.verify(paradaRepositorio);
    }


    @Test
    public void testBloquearBicicletaCuandoLaParadaNoEsta() {
        paradaRepositorio.anadirParada(parada1);
        EasyMock.expectLastCall().times(1);
        EasyMock.expect(paradaRepositorio.getParadas()).andReturn(paradasMock).times(1);

        EasyMock.replay(paradaRepositorio);
        gestor = new GestorParadasEnAislamiento(paradaRepositorio);
        gestor.anadirParada(parada1);
        assertThrows(IllegalStateException.class, () -> {
            gestor.bloquearBicicleta(identificadorParada2, identificadorBici);
        });
        EasyMock.verify(paradaRepositorio);
    }


    
    @Test
    public void testBloquearBicicletaCuandoNoEstaLaParada() {
        paradasMock.add(parada1);
        paradaRepositorio.anadirParada(parada1);
        EasyMock.expectLastCall().times(1);
        EasyMock.expect(paradaRepositorio.getParadas()).andReturn(paradasMock).times(1);

        EasyMock.replay(paradaRepositorio);
        gestor = new GestorParadasEnAislamiento(paradaRepositorio);
        gestor.anadirParada(parada1);
        assertThrows(IllegalStateException.class, () -> {
            gestor.desactivarParada(identificadorParada2);
        });
        EasyMock.verify(paradaRepositorio);
    }



    @Test
    public void testBloquearBicicletaCuandoIdBicicletaNull() {
        paradaRepositorio.anadirParada(parada1);
        EasyMock.expectLastCall().times(1);
        EasyMock.expect(paradaRepositorio.getParadas()).andReturn(paradasMock).times(1);

        EasyMock.replay(paradaRepositorio);
        gestor = new GestorParadasEnAislamiento(paradaRepositorio);
        gestor.anadirParada(parada1);
        assertThrows(IllegalArgumentException.class, () -> {
            gestor.bloquearBicicleta(identificadorParada1, null);
        });
        EasyMock.verify(paradaRepositorio);
    }


    @Test
    public void testGetBloqueoCuandoIdBicicletaNull() {
        paradaRepositorio.anadirParada(parada1);
        EasyMock.expectLastCall().times(1);
        EasyMock.expect(paradaRepositorio.getParadas()).andReturn(paradasMock).times(1);
        paradaRepositorio.bloquearBicicleta(identificadorParada1, identificadorBici);
        EasyMock.expectLastCall().times(1);
        
        EasyMock.replay(paradaRepositorio);
        gestor = new GestorParadasEnAislamiento(paradaRepositorio);
        gestor.anadirParada(parada1);
        gestor.bloquearBicicleta(identificadorParada1, identificadorBici);
        assertThrows(IllegalArgumentException.class, () -> {
            gestor.getBloqueo(null);
        });
        EasyMock.verify(paradaRepositorio);
    }


    @Test
    public void testGetBloqueoCuandoLaBiciNoTieneBloqueo() {
        ArrayList<Bloqueo> bloqueosMock = new ArrayList<>();
        Bloqueo bloqueoGenerado = new Bloqueo(normal);
        bloqueosMock.add(bloqueoGenerado);

        paradaRepositorio.anadirParada(parada1);
        EasyMock.expectLastCall().times(1);
        paradaRepositorio.bloquearBicicleta(identificadorParada1, identificadorBici);
        EasyMock.expectLastCall().times(1);
        EasyMock.expect(paradaRepositorio.getParadas()).andReturn(paradasMock).times(1);
        EasyMock.expect(paradaRepositorio.getListaBloqueos()).andReturn(bloqueosMock).times(1);
        
        EasyMock.replay(paradaRepositorio);
        gestor = new GestorParadasEnAislamiento(paradaRepositorio);
        gestor.anadirParada(parada1);
        gestor.bloquearBicicleta(identificadorParada1, identificadorBici);
        assertThrows(IllegalStateException.class, () -> {
            gestor.getBloqueo("no");
        });
        EasyMock.verify(paradaRepositorio);
    }

    
    @Test
    public void testDesbloquearBicicleta(){
        ArrayList<Bloqueo> bloqueosMock = new ArrayList<>();

        EasyMock.expect(paradaRepositorio.getParadas()).andReturn(paradasMock).times(3);
        paradaRepositorio.anadirParada(parada1);
        EasyMock.expectLastCall().times(1);
        paradaRepositorio.bloquearBicicleta(identificadorParada1, identificadorBici);
        EasyMock.expectLastCall().times(1);
        paradaRepositorio.desbloquearBicicleta(identificadorParada1, identificadorBici);
        EasyMock.expectLastCall().times(1);
        EasyMock.expect(paradaRepositorio.getListaBloqueos()).andReturn(bloqueosMock);

        EasyMock.replay(paradaRepositorio);
        gestor = new GestorParadasEnAislamiento(paradaRepositorio);
        gestor.anadirParada(parada1);
        gestor.bloquearBicicleta(identificadorParada1, identificadorBici);
        gestor.desbloquearBicicleta(identificadorParada1,identificadorBici);
        assertTrue(gestor.getParada(identificadorParada1).getBicicleta(identificadorBici).isDisponible());
        assertTrue(gestor.getListaBloqueos().isEmpty());
        EasyMock.verify(paradaRepositorio);
    }

    @Test
    public void testDesbloquearBicicletaOtraParada(){
        paradasMock.add(parada2);

        EasyMock.expect(paradaRepositorio.getParadas()).andReturn(paradasMock).times(3);
        paradaRepositorio.anadirParada(parada1);
        EasyMock.expectLastCall().times(1);
        paradaRepositorio.bloquearBicicleta(identificadorParada1, identificadorBici);
        EasyMock.expectLastCall().times(1);
        paradaRepositorio.anadirParada(parada2);
        EasyMock.expectLastCall().times(1);
        paradaRepositorio.activarParada(identificadorParada2);
        EasyMock.expectLastCall().times(1);
        paradaRepositorio.desbloquearBicicleta(identificadorParada2, identificadorBici);
        EasyMock.expectLastCall().andThrow(new IllegalArgumentException());

        EasyMock.replay(paradaRepositorio);
        gestor = new GestorParadasEnAislamiento(paradaRepositorio);
        gestor.anadirParada(parada1);
        gestor.bloquearBicicleta(identificadorParada1, identificadorBici);
        gestor.anadirParada(parada2);
        gestor.activarParada(identificadorParada2);
        assertThrows(IllegalArgumentException.class,()->{
            gestor.desbloquearBicicleta(identificadorParada2, identificadorBici);
        });
        EasyMock.verify(paradaRepositorio);
    }

    @Test
    public void testDesbloquearBicicletaDisponible(){
        EasyMock.expect(paradaRepositorio.getParadas()).andReturn(paradasMock).times(1);
        paradaRepositorio.anadirParada(parada1);
        EasyMock.expectLastCall().times(1);
        paradaRepositorio.desbloquearBicicleta(identificadorParada1, identificadorBici);
        EasyMock.expectLastCall().andThrow(new IllegalStateException());

        EasyMock.replay(paradaRepositorio);
        gestor = new GestorParadasEnAislamiento(paradaRepositorio);
        gestor.anadirParada(parada1);
        assertThrows(IllegalStateException.class,()->{
            gestor.desbloquearBicicleta(identificadorParada1,identificadorBici);
        });
        EasyMock.verify(paradaRepositorio);
    }


    @Test
     public void testDesbloquearBiciCuanndoLaParadaEstaDesactivada(){
        Parada copiaParada1 = new Parada (identificadorParada1,lat1,lon1,direccion,bicicletas,HUECOS_APARCAMIENTO,false);
        paradasMock = new ArrayList<>();
        paradasMock.add(copiaParada1);

        EasyMock.expect(paradaRepositorio.getParadas()).andReturn(paradasMock).times(3);
        paradaRepositorio.anadirParada(parada1);
        EasyMock.expectLastCall().times(1);
        paradaRepositorio.bloquearBicicleta(identificadorParada1, identificadorBici);
        EasyMock.expectLastCall().times(1);
        paradaRepositorio.desactivarParada(identificadorParada1);
        EasyMock.expectLastCall().times(1);
        paradaRepositorio.desbloquearBicicleta(identificadorParada1, identificadorBici);
        EasyMock.expectLastCall().andThrow(new IllegalStateException());

        EasyMock.replay(paradaRepositorio);
        gestor = new GestorParadasEnAislamiento(paradaRepositorio);
        gestor.anadirParada(parada1);
        gestor.bloquearBicicleta(identificadorParada1, identificadorBici);
        gestor.desactivarParada(identificadorParada1);
        assertThrows(IllegalStateException.class,()->{
            gestor.desbloquearBicicleta(identificadorParada1, identificadorBici);
        });
        EasyMock.verify(paradaRepositorio);
    }

    @Test
    public void testDesbloquearBicicletaCuandoIdParadaNull() {
        EasyMock.expect(paradaRepositorio.getParadas()).andReturn(paradasMock).times(1);
        paradaRepositorio.anadirParada(parada1);
        EasyMock.expectLastCall().times(1);
        paradaRepositorio.bloquearBicicleta(identificadorParada1, identificadorBici);
        EasyMock.expectLastCall().times(1);

        EasyMock.replay(paradaRepositorio);
        gestor = new GestorParadasEnAislamiento(paradaRepositorio);
        gestor.anadirParada(parada1);
        gestor.bloquearBicicleta(identificadorParada1, identificadorBici);
        assertThrows(IllegalArgumentException.class,()->{
            gestor.desbloquearBicicleta(null, identificadorBici);
        });
        EasyMock.verify(paradaRepositorio);
    }


    @Test
    public void testDesbloquearBicicletaCuandoIdBiciNull() {
        EasyMock.expect(paradaRepositorio.getParadas()).andReturn(paradasMock).times(2);
        paradaRepositorio.anadirParada(parada1);
        EasyMock.expectLastCall().times(1);
        paradaRepositorio.bloquearBicicleta(identificadorParada1, identificadorBici);
        EasyMock.expectLastCall().times(1);
        paradaRepositorio.desbloquearBicicleta(identificadorParada1, null);
        EasyMock.expectLastCall().andThrow(new IllegalArgumentException());

        EasyMock.replay(paradaRepositorio);
        gestor = new GestorParadasEnAislamiento(paradaRepositorio);
        gestor.anadirParada(parada1);
        gestor.bloquearBicicleta(identificadorParada1, identificadorBici);
        assertThrows(IllegalArgumentException.class,()->{
            gestor.desbloquearBicicleta(identificadorParada1, null);
        });
        EasyMock.verify(paradaRepositorio);
    }

    @Test
    public void testDesbloquearBicicletaCuandoLaParadaNoEsta() {
        EasyMock.expect(paradaRepositorio.getParadas()).andReturn(paradasMock).times(2);
        paradaRepositorio.anadirParada(parada1);
        EasyMock.expectLastCall().times(1);
        paradaRepositorio.bloquearBicicleta(identificadorParada1, identificadorBici);
        EasyMock.expectLastCall().times(1);

        EasyMock.replay(paradaRepositorio);
        gestor = new GestorParadasEnAislamiento(paradaRepositorio);
        gestor.anadirParada(parada1);
        gestor.bloquearBicicleta(identificadorParada1, identificadorBici);
        assertThrows(IllegalStateException.class,()->{
            gestor.desbloquearBicicleta(identificadorParada2, identificadorBici);
        });
        EasyMock.verify(paradaRepositorio);
    }


    @Test
    public void testReservaBicicletaConTodosLosParametrosValidos() {
        ArrayList<Reserva> reservasMock = new ArrayList<>();
        Reserva reserva = new Reserva(normal, usuario, LocalDateTime.now());
        reservasMock.add(reserva);
        paradaRepositorio.anadirParada(parada1);
        EasyMock.expectLastCall().times(1);
        paradaRepositorio.reservaBicicleta(identificadorParada1, identificadorBici, usuario);
        EasyMock.expectLastCall().times(1);
        EasyMock.expect(paradaRepositorio.getReservasBicicletas()).andReturn(reservasMock).times(1);

        EasyMock.replay(paradaRepositorio);
        gestor = new GestorParadasEnAislamiento(paradaRepositorio);
        gestor.anadirParada(parada1);
        gestor.reservaBicicleta(identificadorParada1, identificadorBici, usuario);
        assertEquals(gestor.getReservasBicicletas().size(), 1);
        EasyMock.verify(paradaRepositorio);
    }


    @Test
    public void testReservaBicicletaConUsuarioInactivo() {
        usuario.setEstado(false);
        Bicicleta bici = new Bicicleta("nuevaB");

        paradaRepositorio.anadirParada(parada1);
        EasyMock.expectLastCall().times(1);
        paradaRepositorio.agregarBicicleta(identificadorParada1, bici);
        EasyMock.expectLastCall().times(1);
        paradaRepositorio.reservaBicicleta(identificadorParada1, "nuevaB", usuario);
        EasyMock.expectLastCall().andThrow(new IllegalStateException());

        EasyMock.replay(paradaRepositorio);
        gestor = new GestorParadasEnAislamiento(paradaRepositorio);
        gestor.anadirParada(parada1);
        gestor.agregarBicicleta(identificadorParada1, bici);
        assertThrows(IllegalStateException.class, () -> {
            gestor.reservaBicicleta(identificadorParada1, "nuevaB", usuario);
        });
        EasyMock.verify(paradaRepositorio);
    }


    @Test
    public void testReservaBicicletaConParadaDesactivada() {
        EasyMock.expect(paradaRepositorio.getParadas()).andReturn(paradasMock).times(1);
        paradaRepositorio.anadirParada(parada1);
        EasyMock.expectLastCall().times(1);
        paradaRepositorio.desactivarParada(identificadorParada1);
        EasyMock.expectLastCall().times(1);
        paradaRepositorio.reservaBicicleta(identificadorParada1, identificadorBici, usuario);
        EasyMock.expectLastCall().andThrow(new IllegalStateException());

        EasyMock.replay(paradaRepositorio);
        gestor = new GestorParadasEnAislamiento(paradaRepositorio);
        gestor.anadirParada(parada1);
        gestor.desactivarParada(identificadorParada1);
        assertThrows(IllegalStateException.class, () -> {
            gestor.reservaBicicleta(identificadorParada1, identificadorBici, usuario);
        });
        EasyMock.verify(paradaRepositorio);
    }


    @Test
    public void testReservaBicicletaConUsuarioQueTieneOtraReserva() {
        Bicicleta bici = new Bicicleta("nuevaB");
        
        paradaRepositorio.anadirParada(parada1);
        EasyMock.expectLastCall().times(1);
        paradaRepositorio.agregarBicicleta(identificadorParada1, bici);
        EasyMock.expectLastCall().times(1);
        paradaRepositorio.reservaBicicleta(identificadorParada1, "nuevaB", usuario);
        EasyMock.expectLastCall().times(1);
        paradaRepositorio.reservaBicicleta(identificadorParada1, electrica.getIdentificador(), usuario);
        EasyMock.expectLastCall().andThrow(new IllegalStateException());

        EasyMock.replay(paradaRepositorio);
        gestor = new GestorParadasEnAislamiento(paradaRepositorio);
        gestor.anadirParada(parada1);
        gestor.agregarBicicleta(identificadorParada1, bici);
        gestor.reservaBicicleta(identificadorParada1, "nuevaB", usuario);
        assertThrows(IllegalStateException.class, () -> {
            gestor.reservaBicicleta(identificadorParada1, electrica.getIdentificador(), usuario);
        });
        EasyMock.verify(paradaRepositorio);
    }


    @Test
    public void testReservaBicicletaDeUnaBiciNoDisponible() {
        parada1.agregaBicicleta(biciOcupada);

        paradaRepositorio.anadirParada(parada1);
        EasyMock.expectLastCall().times(1);
        paradaRepositorio.agregarBicicleta(identificadorParada1, biciOcupada);
        EasyMock.expectLastCall().times(1);
        paradaRepositorio.reservaBicicleta(identificadorParada1, biciOcupada.getIdentificador(), usuario);
        EasyMock.expectLastCall().andThrow(new IllegalStateException());


        EasyMock.replay(paradaRepositorio);
        gestor = new GestorParadasEnAislamiento(paradaRepositorio);
        gestor.anadirParada(parada1);
        gestor.agregarBicicleta(identificadorParada1, biciOcupada);
        assertThrows(IllegalStateException.class, () -> {
            gestor.reservaBicicleta(identificadorParada1, biciOcupada.getIdentificador(), usuario);
        });
        EasyMock.verify(paradaRepositorio);
    }


    @Test
    public void testReservaBicicletaCuandoUsuarioNull() {
        paradaRepositorio.anadirParada(parada1);
        EasyMock.expectLastCall().times(1);
        paradaRepositorio.reservaBicicleta(identificadorParada1, identificadorBici, null);
        EasyMock.expectLastCall().andThrow(new IllegalArgumentException());

        EasyMock.replay(paradaRepositorio);
        gestor = new GestorParadasEnAislamiento(paradaRepositorio);
        gestor.anadirParada(parada1);
        assertThrows(IllegalArgumentException.class, () -> {
            gestor.reservaBicicleta(identificadorParada1, identificadorBici, null);
        });
        EasyMock.verify(paradaRepositorio);
    }


    @Test
    public void testReservaBicicletaCuandoBiciNoEnParada() {
        paradaRepositorio.anadirParada(parada1);
        EasyMock.expectLastCall().times(1);
        paradaRepositorio.reservaBicicleta(identificadorParada1, "no", usuario);
        EasyMock.expectLastCall().andThrow(new IllegalArgumentException());

        EasyMock.replay(paradaRepositorio);
        gestor = new GestorParadasEnAislamiento(paradaRepositorio);
        gestor.anadirParada(parada1);
        assertThrows(IllegalArgumentException.class, () -> {
            gestor.reservaBicicleta(identificadorParada1, "no", usuario);
        });
        EasyMock.verify(paradaRepositorio);
    }


    @Test
    public void testReservaBicicletaCuandoBiciNull() {
        paradaRepositorio.anadirParada(parada1);
        EasyMock.expectLastCall().times(1);
        paradaRepositorio.reservaBicicleta(identificadorParada1, null, usuario);
        EasyMock.expectLastCall().andThrow(new IllegalArgumentException());

        EasyMock.replay(paradaRepositorio);
        gestor = new GestorParadasEnAislamiento(paradaRepositorio);
        gestor.anadirParada(parada1);
        assertThrows(IllegalArgumentException.class, () -> {
            gestor.reservaBicicleta(identificadorParada1, null, usuario);
        });
        EasyMock.verify(paradaRepositorio);
    }


    @Test
    public void testReservaBicicletaCuandoIdParadaNull() {
        paradaRepositorio.anadirParada(parada1);
        EasyMock.expectLastCall().times(1);
        paradaRepositorio.reservaBicicleta(null, identificadorBici, usuario);
        EasyMock.expectLastCall().andThrow(new IllegalArgumentException());

        EasyMock.replay(paradaRepositorio);
        gestor = new GestorParadasEnAislamiento(paradaRepositorio);
        gestor.anadirParada(parada1);
        assertThrows(IllegalArgumentException.class, () -> {
            gestor.reservaBicicleta(null, identificadorBici, usuario);
        });
        EasyMock.verify(paradaRepositorio);
    }


    @Test
    public void testReservaBicicletaCuandoIdParadaNoExistente() {
        paradaRepositorio.anadirParada(parada1);
        EasyMock.expectLastCall().times(1);
        paradaRepositorio.reservaBicicleta("no", identificadorBici, usuario);
        EasyMock.expectLastCall().andThrow(new IllegalArgumentException());

        EasyMock.replay(paradaRepositorio);
        gestor = new GestorParadasEnAislamiento(paradaRepositorio);
        gestor.anadirParada(parada1);
        assertThrows(IllegalArgumentException.class, () -> {
            gestor.reservaBicicleta("no", identificadorBici, usuario);
        });
        EasyMock.verify(paradaRepositorio);
    }


    @Test 
    public void testIsParadaEnGestorCuandoLoEsta() {
        paradaRepositorio.anadirParada(parada1);
        EasyMock.expectLastCall().times(1);
        EasyMock.expect(paradaRepositorio.getParadas()).andReturn(paradasMock).times(1);

        EasyMock.replay(paradaRepositorio);
        gestor = new GestorParadasEnAislamiento(paradaRepositorio);
        gestor.anadirParada(parada1);
        assertTrue(gestor.isParadaEnGestor(identificadorParada1));
        EasyMock.verify(paradaRepositorio);
    }


    @Test 
    public void testIsParadaEnGestorCuandoNoLoEsta() {
        paradaRepositorio.anadirParada(parada1);
        EasyMock.expectLastCall().times(1);
        EasyMock.expect(paradaRepositorio.getParadas()).andReturn(paradasMock).times(1);

        EasyMock.replay(paradaRepositorio);
        gestor = new GestorParadasEnAislamiento(paradaRepositorio);
        gestor.anadirParada(parada1);
        assertFalse(gestor.isParadaEnGestor(identificadorParada2));
        EasyMock.verify(paradaRepositorio);
    }


    @Test 
    public void testIsParadaEnGestorCuandoElIdEsNull(){
        paradaRepositorio.anadirParada(parada1);
        EasyMock.expectLastCall().times(1);

        EasyMock.replay(paradaRepositorio);
        gestor = new GestorParadasEnAislamiento(paradaRepositorio);
        gestor.anadirParada(parada1);

        assertThrows(IllegalArgumentException.class, () -> {
            gestor.isParadaEnGestor(null);
        });
        EasyMock.verify(paradaRepositorio);
    }


    @Test
    public void testDesactivarParadaCuandoEstaActivada() {
        ArrayList<Parada> paradasDesactivada = new ArrayList<>();
        Parada copiaParada1 = new Parada (identificadorParada1,lat1,lon1,direccion,bicicletas,HUECOS_APARCAMIENTO,false);
        paradasDesactivada.add(copiaParada1);

        paradaRepositorio.anadirParada(parada1);
        EasyMock.expectLastCall().times(1);
        EasyMock.expect(paradaRepositorio.getParadas()).andReturn(paradasMock).times(1);
        EasyMock.expect(paradaRepositorio.getParadas()).andReturn(paradasDesactivada).times(1);

        paradaRepositorio.desactivarParada(identificadorParada1);
        EasyMock.expectLastCall().times(1);

        EasyMock.replay(paradaRepositorio);
        gestor = new GestorParadasEnAislamiento(paradaRepositorio);
        gestor.anadirParada(parada1);
        gestor.desactivarParada(identificadorParada1);
        assertFalse(gestor.getParada(identificadorParada1).isActiva());
        EasyMock.verify(paradaRepositorio);
    }


    @Test
    public void testDesactivarParadaCuandoEstaDesactivada() {        
        parada1.setEstado(false);

        paradaRepositorio.anadirParada(parada1);
        EasyMock.expectLastCall().times(1);
        EasyMock.expect(paradaRepositorio.getParadas()).andReturn(paradasMock).times(1);
        paradaRepositorio.desactivarParada(identificadorParada1);
        EasyMock.expectLastCall().andThrow(new IllegalStateException());

        EasyMock.replay(paradaRepositorio);
        gestor = new GestorParadasEnAislamiento(paradaRepositorio);
        gestor.anadirParada(parada1);
        assertThrows(IllegalStateException.class, () -> {
            gestor.desactivarParada(identificadorParada1);});
        EasyMock.verify(paradaRepositorio);
    }


    @Test
    public void testDesactivarParadaCuandoIdParadaEsNull() {
        paradaRepositorio.anadirParada(parada1);
        EasyMock.expectLastCall().times(1);

        EasyMock.replay(paradaRepositorio);
        gestor = new GestorParadasEnAislamiento(paradaRepositorio);
        gestor.anadirParada(parada1);
        assertThrows(IllegalArgumentException.class, () -> {
            gestor.desactivarParada(null);
        });
        EasyMock.verify(paradaRepositorio);
    }


    @Test
    public void testDesactivarParadaCuandoNoEstaLaParada() {
        paradaRepositorio.anadirParada(parada1);
        EasyMock.expectLastCall().times(1);

        EasyMock.replay(paradaRepositorio);
        gestor = new GestorParadasEnAislamiento(paradaRepositorio);
        gestor.anadirParada(parada1);
        assertThrows(IllegalArgumentException.class, () -> {
            gestor.desactivarParada(null);
        });
        EasyMock.verify(paradaRepositorio);
    }



    @Test
    public void testActivarParadaCuandoEstaDesactivada() {
        Parada copiaParada1 = new Parada (identificadorParada1,lat1,lon1,direccion, bicicletas ,HUECOS_APARCAMIENTO,true);
        ArrayList<Parada> paradasActivada = new ArrayList<>();
        parada1.setEstado(false);
        paradasActivada.add(copiaParada1);
        parada2.setEstado(true);
        paradasActivada.add(parada2);

        paradaRepositorio.anadirParada(parada2);
        EasyMock.expectLastCall().times(1);
        paradaRepositorio.anadirParada(parada1);
        EasyMock.expectLastCall().times(1);
        EasyMock.expect(paradaRepositorio.getParadas()).andReturn(paradasMock).times(1);
        EasyMock.expect(paradaRepositorio.getParadas()).andReturn(paradasActivada).times(1);
        paradaRepositorio.activarParada(identificadorParada1);
        EasyMock.expectLastCall().times(1);

        EasyMock.replay(paradaRepositorio);
        gestor = new GestorParadasEnAislamiento(paradaRepositorio);
        gestor.anadirParada(parada2);
        gestor.anadirParada(parada1);
        gestor.activarParada(identificadorParada1);
        assertTrue(gestor.getParada(identificadorParada1).isActiva());
        EasyMock.verify(paradaRepositorio);
    }


    @Test
    public void testActivarParadaCuandoEstaActivada() {
        paradaRepositorio.anadirParada(parada1);
        EasyMock.expectLastCall().times(1);
        EasyMock.expect(paradaRepositorio.getParadas()).andReturn(paradasMock).times(1);
        paradaRepositorio.activarParada(identificadorParada1);
        EasyMock.expectLastCall().andThrow(new IllegalStateException());

        EasyMock.replay(paradaRepositorio);
        gestor = new GestorParadasEnAislamiento(paradaRepositorio);
        gestor.anadirParada(parada1);
        assertThrows(IllegalStateException.class, () -> {
            gestor.activarParada(identificadorParada1);});
        EasyMock.verify(paradaRepositorio);
    }


    @Test
    public void testActivarParadaConIdParadaNulo() {

        parada1.setEstado(false);
        paradaRepositorio.anadirParada(parada1);
        EasyMock.expectLastCall().times(1);

        EasyMock.replay(paradaRepositorio);
        gestor = new GestorParadasEnAislamiento(paradaRepositorio);
        gestor.anadirParada(parada1);
        assertThrows(IllegalArgumentException.class, () -> {
            gestor.activarParada(null);});
        EasyMock.verify(paradaRepositorio);
    }


    @Test
    public void testActivarParadaConIdParadaNoExistente() {
        parada1.setEstado(false);

        paradaRepositorio.anadirParada(parada1);
        EasyMock.expectLastCall().times(1);
        EasyMock.expect(paradaRepositorio.getParadas()).andReturn(paradasMock).times(1);

        EasyMock.replay(paradaRepositorio);
        gestor = new GestorParadasEnAislamiento(paradaRepositorio);
        gestor.anadirParada(parada1);
        assertThrows(IllegalStateException.class, () -> {
            gestor.activarParada(identificadorParada2);});
        EasyMock.verify(paradaRepositorio);
    }


    @Test
    public void testAnadirParadaNula() {
        EasyMock.replay(paradaRepositorio);
        gestor = new GestorParadasEnAislamiento(paradaRepositorio);
        assertThrows(IllegalArgumentException.class, () -> {
            gestor.anadirParada(null);
        });
        EasyMock.verify(paradaRepositorio);
    }


    @Test
    public void testGetBicicletasParadaCuandoIdParadaNull() {
        paradaRepositorio.anadirParada(parada1);
        EasyMock.expectLastCall().times(1);

        EasyMock.replay(paradaRepositorio);
        gestor = new GestorParadasEnAislamiento(paradaRepositorio);
        gestor.anadirParada(parada1);
        assertThrows(IllegalArgumentException.class, () -> {
            gestor.getBicicletasParada(null);
        });
        EasyMock.verify(paradaRepositorio);
    }


    @Test
    public void testGetParadaCuandoIdParadaNull() {
        paradaRepositorio.anadirParada(parada1);
        EasyMock.expectLastCall().times(1);

        EasyMock.replay(paradaRepositorio);
        gestor = new GestorParadasEnAislamiento(paradaRepositorio);
        gestor.anadirParada(parada1);
        assertThrows(IllegalArgumentException.class, () -> {
            gestor.getParada(null);
        });
        EasyMock.verify(paradaRepositorio);
    }


    @Test
    public void testGetParadaCuandoNoEstaEnElGestor() {
        paradasMock = new ArrayList<>();
        paradasMock.add(parada2);
        paradaRepositorio.anadirParada(parada2);
        EasyMock.expectLastCall().times(1);
        EasyMock.expect(paradaRepositorio.getParadas()).andReturn(paradasMock).times(1);
        
        EasyMock.replay(paradaRepositorio);
        gestor = new GestorParadasEnAislamiento(paradaRepositorio);
        gestor.anadirParada(parada2);
        assertThrows(IllegalStateException.class, () -> {
            gestor.getParada(identificadorParada1);
        });
        EasyMock.verify(paradaRepositorio);
    }


    @Test
    public void testGetAparcamientosDisponiblesCuandoIdParadaNull() {
        paradaRepositorio.anadirParada(parada1);
        EasyMock.expectLastCall().times(1);

        EasyMock.replay(paradaRepositorio);
        gestor = new GestorParadasEnAislamiento(paradaRepositorio);
        gestor.anadirParada(parada1);
        assertThrows(IllegalArgumentException.class, () -> {
            gestor.getAparcamientosDisponibles(null);
        });
        EasyMock.verify(paradaRepositorio);
    }


    @Test 
    public void testGetParadasDisponiblesUbicacionCuandoLaDistanciaEsMenorQueElLimiteInferior() {
        paradaRepositorio.anadirParada(parada1);
        EasyMock.expectLastCall().times(1);

        EasyMock.replay(paradaRepositorio);
        gestor = new GestorParadasEnAislamiento(paradaRepositorio);
        gestor.anadirParada(parada1);
        assertThrows(IllegalArgumentException.class, () -> {
            gestor.getParadasDisponiblesUbicacion(10,10, -0.0001);
        });
        EasyMock.verify(paradaRepositorio);
    }

    @Test 
    public void testGetParadasDisponiblesUbicacionCuandoTodoEsValido() {
        Parada parada3 = new Parada("id3", -90.0, 10.0, "C", bicicletas, HUECOS_APARCAMIENTO, true);
        ArrayList <Parada> paradasEnMock = new ArrayList<>();
        paradasEnMock.add(parada1);
        paradasEnMock.add(parada2);
        paradasEnMock.add(parada3);
        ArrayList <Parada> listaParadasEsperada = new ArrayList<>();
        listaParadasEsperada.add(parada1);

        paradaRepositorio.anadirParada(parada1);
        EasyMock.expectLastCall().times(1);
        paradaRepositorio.anadirParada(parada2);
        EasyMock.expectLastCall().times(1);
        paradaRepositorio.anadirParada(parada3);
        EasyMock.expectLastCall().times(1); 
        EasyMock.expect(paradaRepositorio.getParadas()).andReturn(paradasEnMock).times(1);

        EasyMock.replay(paradaRepositorio);
        gestor = new GestorParadasEnAislamiento(paradaRepositorio);
        gestor.anadirParada(parada1);
        gestor.anadirParada(parada2);
        gestor.anadirParada(parada3);   
        assertArrayEquals(listaParadasEsperada.toArray(), gestor.getParadasDisponiblesUbicacion(-55.0,95.0,1000000).toArray());
        EasyMock.verify(paradaRepositorio);
    }
}