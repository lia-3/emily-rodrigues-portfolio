package uva.tds.gestorenpersistencia;
import uva.tds.interfaces.IParadaRepositorio;
import uva.tds.base.*;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import uva.tds.gestorenaislamiento.GestorParadasEnAislamiento;
import uva.tds.gestorenaislamiento.GestorRecompensaEnAislamiento;
import uva.tds.implementaciones.ParadaRepositorio;
import uva.tds.implementaciones.RecompensaRepositorio;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;


/**
 * Clase de tests que prueba la clase GestorParadasEnAislamiento con persistencia
 * mediante base de datos
 * @author Emily Rodrigues
 * @author Marcos de Diego
 */
public class GestorParadasEnPersistenciaTest {

    /*
     * La gestión de los usuarios a la hora de realizar una reserva 
     * debe ser tratada por la clase Reserva y no por las paradas.
     * Lo mismo ocurre en el caso de los alquileres.
     */

    private GestorParadasEnAislamiento gestor;

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
    void setUp() {
        paradaRepositorio = new ParadaRepositorio();
        gestor = new GestorParadasEnAislamiento(paradaRepositorio);
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
        bicicletas2.add(biciOcupada);
        
        parada1 = new Parada (identificadorParada1,lat1,lon1,direccion,bicicletas,HUECOS_APARCAMIENTO,true);
        parada2 = new Parada (identificadorParada2,lat2,lon2,direccion,bicicletas2,HUECOS_APARCAMIENTO,false);

        bicicletasDisponibles= new ArrayList <Bicicleta>();
        bicicletasDisponibles.add(normal);
        bicicletasDisponibles.add(electrica);

       
    }

    @BeforeEach
    public void limpiarBaseDeDatos() {
        try{
        ((ParadaRepositorio) paradaRepositorio).clearDatabase();
        }catch(HibernateException e ){
            e.printStackTrace();
        }
    }
    
    

    @Test
    public void testGestorParadasEnAislamiento() {
        ArrayList<Parada> paradas = new ArrayList<>();
        paradas.add(parada1);
        paradas.add(parada2);
        ArrayList<Parada> paradasDisponibles = new ArrayList<>();
        paradasDisponibles.add(parada1);

        paradaRepositorio.anadirParada(parada1);
        paradaRepositorio.anadirParada(parada2);

        
        assertArrayEquals(paradas.toArray(), gestor.getParadas().toArray());
        assertArrayEquals(paradasDisponibles.toArray(), gestor.getParadasActivas().toArray());
        assertEquals(gestor.getAparcamientosDisponibles(identificadorParada1), 2);
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

        

        assertArrayEquals(paradas.toArray(), gestor.getParadas().toArray());
        assertArrayEquals(paradasDisponibles.toArray(), gestor.getParadasActivas().toArray());
    }

    @Test
    public void testAnadirParada() {
        ArrayList<Parada> listaParadasEsperada = new ArrayList<>();
        listaParadasEsperada.add(parada1);
        listaParadasEsperada.add(parada2);

       
        gestor.anadirParada(parada1);
        gestor.anadirParada(parada2);

        assertArrayEquals(listaParadasEsperada.toArray(), gestor.getParadas().toArray());
    }

    @Test
    public void testAnadirParadaRepetida() {
        Parada paradaRepetida = new Parada(identificadorParada1, -50, 100, direccion, bicicletas, HUECOS_APARCAMIENTO, true);

        gestor.anadirParada(parada1);

        assertThrows(IllegalArgumentException.class, () -> {
            gestor.anadirParada(paradaRepetida);
        });
    }


    @Test
    public void testAgregarBicicletaSinHuecos(){
        parada1 = new Parada (identificadorParada1,lat1,lon1,direccion,bicicletas,3,true);
        biciOcupada.setEstadoDisponible();

        
        
        gestor.anadirParada(parada1);
        gestor.agregarBicicleta(identificadorParada1,nueva);
        assertThrows (IllegalStateException.class, ()->{
            gestor.agregarBicicleta(identificadorParada1,new Bicicleta ("id4"));
        });
    }

    @Test
    public void testAgregarBicicletaParadaNoEsta(){
        biciOcupada.setEstadoDisponible();
        parada1.agregaBicicleta(biciOcupada);
        
       
        gestor.anadirParada(parada1);
        gestor.agregarBicicleta(identificadorParada1,nueva);
        assertThrows (IllegalStateException.class, ()->{
            gestor.agregarBicicleta(identificadorParada2,new Bicicleta ("id4"));
        });
    }

    @Test
    public void testAgregarBicicletaMismoIdEnLaMismaParada(){
      
        gestor.anadirParada(parada1);
        gestor.agregarBicicleta(identificadorParada1,nueva);
        assertThrows (IllegalArgumentException.class, ()->{
            gestor.agregarBicicleta(identificadorParada1,new Bicicleta ("id2", 15));
        });
    }


    @Test
    public void testAgregarBicicletaMismoIdEnOtraParada(){
       
        gestor.anadirParada(parada1);
        gestor.anadirParada(parada2);
        gestor.agregarBicicleta(identificadorParada1, nueva);
        assertThrows (IllegalArgumentException.class, ()->{
            gestor.agregarBicicleta(identificadorParada2,new Bicicleta ("id2"));
        });
    }


    @Test
    public void testAgregarBicicletaNula() {
        
        gestor.anadirParada(parada1);
        assertThrows (IllegalArgumentException.class, ()->{
            gestor.agregarBicicleta(identificadorParada1, null);
        });
    }


    @Test
    public void testAgregarBicicletaConIdParadaNull() {
        
        gestor.anadirParada(parada1);
        assertThrows (IllegalArgumentException.class, ()->{
            gestor.agregarBicicleta(null, nueva);
        });
    }

   
     @Test 
     public void testEliminarBicicletaValida(){
        gestor =  new GestorParadasEnAislamiento(paradaRepositorio);
        gestor.anadirParada(parada1);
        gestor.eliminarBicicleta(identificadorParada1,identificadorBici);
        bicicletasDisponibles.remove(normal);
        assertArrayEquals(bicicletasDisponibles.toArray(), gestor.getBicicletasParada(identificadorParada1).toArray());
     }


     @Test 
     public void testEliminarBicicletaConIdentificadorDeBiciNulo(){
        gestor =  new GestorParadasEnAislamiento(paradaRepositorio);
        gestor.anadirParada(parada1);
        assertThrows(IllegalArgumentException.class, () -> { 
            gestor.eliminarBicicleta(identificadorParada1, null);
        });
    }


    @Test 
     public void testEliminarBicicletaConIdentificadorDeParadaNulo(){
        gestor =  new GestorParadasEnAislamiento(paradaRepositorio);
        gestor.anadirParada(parada1);
        assertThrows(IllegalArgumentException.class, () -> { 
            gestor.eliminarBicicleta(null, identificadorBici);
        });
    }


    @Test 
     public void testEliminarBicicletaConUnaParadaNoExistente(){
        gestor =  new GestorParadasEnAislamiento(paradaRepositorio);
        gestor.anadirParada(parada1);
        assertThrows(IllegalArgumentException.class, () -> { 
            gestor.eliminarBicicleta(identificadorParada2, identificadorBici);
        });
    }


    @Test 
     public void testEliminarBicicletaConUnaBiciNoExistenteEnLaParada(){
        gestor =  new GestorParadasEnAislamiento(paradaRepositorio);
        gestor.anadirParada(parada1);
        assertThrows(IllegalStateException.class, () -> { 
            gestor.eliminarBicicleta(identificadorParada1, "no");
        });
    }

    @Test 
     public void testEliminarBicicletaCuandoEstaOCupada(){
        gestor =  new GestorParadasEnAislamiento(paradaRepositorio);
        gestor.anadirParada(parada1);
        assertThrows(IllegalStateException.class, () -> { 
            gestor.eliminarBicicleta(identificadorParada1, biciOcupada.getIdentificador());
        });
    }

    @Test
    public void testAlquilarBicicleta() {    
        
        gestor.anadirParada(parada1);
        gestor.alquilarBicicleta(identificadorParada1, identificadorBici, usuario);
    
        assertEquals(gestor.getAlquileresEnCurso().size(), 1);
        assertEquals(gestor.getAlquileresEnCurso().get(0).getBicicleta().getIdentificador(), identificadorBici);
        assertEquals(gestor.getAlquileresEnCurso().get(0).getUsuario().getNif(), nif);
        assertTrue(gestor.tieneAlquilerEnCurso(usuario.getNif()));
        assertFalse(gestor.tieneUsuarioUnaReserva(usuario));
    }
    

    
    @Test
    public void testTieneUsuarioUnaReservaCuandoUsuarioNull() {
        
        gestor.anadirParada(parada1);
        assertThrows(IllegalArgumentException.class, () -> {
            gestor.tieneUsuarioUnaReserva(null);
        });
    }


    @Test
    public void testAlquilarBicicletaReservada() {
        
        gestor.anadirParada(parada1);
        assertTrue(usuario.getReservas().isEmpty());
        assertTrue(gestor.getReservasBicicletas().isEmpty());
        gestor.reservaBicicleta(identificadorParada1, identificadorBici, usuario);
        gestor.alquilarBicicleta(identificadorParada1, identificadorBici, usuario);
        assertEquals(gestor.getAlquiler(nif, identificadorBici).getBicicleta(), normal);
        assertTrue(gestor.tieneUsuarioUnaReserva(usuario));
    }   


     @Test
     public void testAlquilarBicicletaBloqueada(){
        
        normal.setEstadoBloqueada();
        gestor.anadirParada(parada1);
        assertThrows (IllegalStateException.class, ()->{
            gestor.alquilarBicicleta(identificadorParada1, identificadorBici, usuario );
        });
     }

     @Test
     public void testAlquilarBicicletaNoEsta(){
        
        gestor.anadirParada(parada1);
        assertThrows (IllegalStateException.class, ()->{
            gestor.alquilarBicicleta(identificadorParada1, "no" ,usuario );
        });
     }

     @Test
     public void testAlquilarBicicletaParadaNoEsta(){
        
        gestor.anadirParada(parada1);
        assertThrows (IllegalStateException.class, ()->{
            gestor.alquilarBicicleta(identificadorParada2, identificadorBici,usuario );
        });
     }

     @Test
     public void testAlquilarBicicletaUsuarioInactivo() {
        
         Session session = HibernateUtil.getSessionFactory().openSession();
         try {
             session.beginTransaction();
             session.saveOrUpdate(usuario); // Usar saveOrUpdate para evitar duplicados
             session.getTransaction().commit();
         } finally {
             session.close();
         }
     
     
         session = HibernateUtil.getSessionFactory().openSession();
         try {
             session.beginTransaction();
             usuario.setEstado(false); // Cambiar el estado a inactivo
             usuario = (Usuario) session.merge(usuario); // Sincronizar el objeto usuario
             session.getTransaction().commit();
         } finally {
             session.close();
         }
     
     
         gestor.anadirParada(parada1);
         assertThrows(IllegalStateException.class, () -> {
             gestor.alquilarBicicleta(identificadorParada1, identificadorBici, usuario);
         });
     }
     

        
     @Test
    public void testAlquilarBicicletaCuandoBicicletaReservadaPorOtroUsuarioAntesDeQueSeAcabeElTiempo() {
        Usuario nuevoUsuario = new Usuario("taySwift", "71969192F", 135, true);
        usuario.setEstado(true);
        gestor.anadirParada(parada1);
        gestor.reservaBicicleta(identificadorParada1, identificadorBici, usuario);
        assertThrows (IllegalStateException.class, ()->{
            gestor.alquilarBicicleta(identificadorParada1, identificadorBici, nuevoUsuario);
        });
     }
     
     @Test
     public void testAlquilarBicicletaCuandoElUsuarioTieneOtroAlquiler(){
        electrica.setEstadoDisponible();

        
        gestor.anadirParada(parada1);
        gestor.alquilarBicicleta(identificadorParada1, identificadorBici, usuario );
        assertThrows (IllegalStateException.class, ()->{
            gestor.alquilarBicicleta(identificadorParada1, identificadorElectrica, usuario );
        });
     }


    @Test
    public void testAlquilarBicicletaConIdParadaNull() {
        
        gestor.anadirParada(parada1);
        assertThrows(IllegalArgumentException.class, () -> {
            gestor.alquilarBicicleta(null, identificadorBici, usuario);
        });
    }


    @Test
    public void testAlquilarBicicletaConIdBiciNull() {
        
        gestor.anadirParada(parada1);
        assertThrows(IllegalArgumentException.class, () -> {
            gestor.alquilarBicicleta(identificadorParada1, null, usuario);
        });
    }


    @Test
    public void testAlquilarBicicletaConUsuarioNull() {
        
        gestor.anadirParada(parada1);
        assertThrows(IllegalArgumentException.class, () -> {
            gestor.alquilarBicicleta(identificadorParada1, identificadorBici, null);
        });
    }


    @Test 
    public void testAlquilarBicicletaCuandoLaParadaEstaDesactivada() {
        
        gestor.anadirParada(parada1);
        gestor.desactivarParada(identificadorParada1);
        assertThrows(IllegalStateException.class, () -> {
            gestor.alquilarBicicleta(parada1.getIdentificador(), identificadorBici, usuario);});
    }


    @Test
    public void testAGetAlquileresEnCursoCuandoNoHayNinguno() {
        
        gestor.anadirParada(parada1);
        assertTrue(gestor.getAlquileresEnCurso().isEmpty());
    }


    @Test 
    public void testTieneAlquilerEnCursoCuandoNifEsNull() {
        
        gestor.anadirParada(parada1);
        assertThrows(IllegalArgumentException.class, () -> {
            gestor.tieneAlquilerEnCurso(null);
        });
    }


    @Test
    public void testGetAlquilerCuandoNifEsNull() {
        
        gestor.anadirParada(parada1);
        assertThrows(IllegalArgumentException.class, () -> {
            gestor.getAlquiler(null, identificadorBici);
        });
    }


    @Test
    public void testGetAlquilerCuandoIdBicicletaNull() {
        
        gestor.anadirParada(parada1);
        assertThrows(IllegalArgumentException.class, () -> {
            gestor.getAlquiler(nif, null);
        });
    }


    @Test
    public void testGetAlquilerCuandoLaBicicletANoEstaAlquilada() {
        
        gestor.anadirParada(parada1);
        assertThrows(IllegalStateException.class, () -> {
            gestor.getAlquiler(nif, identificadorElectrica);
        });
    }


    @Test
    public void testGetAlquilerSiLaBiciNoTieneAlquiler() {
        
        gestor.anadirParada(parada1);
        assertThrows(IllegalStateException.class, () -> {
            gestor.getAlquiler(nif, identificadorElectrica);
        });
    }


    @Test
    public void testGetAlquilerSiElDuennoNoTieneAlquiler() {
        
        gestor.anadirParada(parada1);
        assertThrows(IllegalStateException.class, () -> {
            gestor.getAlquiler("05647778R", identificadorBici);
        });
    }


    @Test
    public void testDevolverBicicleta(){
       
       gestor.anadirParada(parada1);
       gestor.alquilarBicicleta(identificadorParada1, identificadorBici, usuario);
       Bicicleta alquilada = gestor.getAlquiler(nif, identificadorBici).getBicicleta();
       gestor.devolverBicicleta(identificadorParada1, nif, alquilada);
       assertTrue(gestor.getBicicletasParada(identificadorParada1).contains(normal));
       assertTrue(gestor.getParada(identificadorParada1).getBicicleta(identificadorBici).isDisponible());
       assertFalse(gestor.tieneAlquilerEnCurso(nif));
     }

    @Test
    public void testDevolverBicicletaParadaLlena(){
        parada1.agregaBicicleta(nueva);
        biciOcupada.setEstadoDisponible();
        parada1.agregaBicicleta(biciOcupada);
        
        gestor.anadirParada(parada1);
        assertThrows (IllegalStateException.class, ()->{
            gestor.devolverBicicleta(identificadorParada1, nif, new Bicicleta("id4"));
        });
    }


    @Test
    public void testDevolverBicicletaCuandoLaParadaEstaDesactivada(){ 
        
        gestor.anadirParada(parada1);
        gestor.alquilarBicicleta(identificadorParada1, identificadorBici, usuario); 
        gestor.desactivarParada(identificadorParada1);        
        assertThrows (IllegalStateException.class, ()->{
            gestor.devolverBicicleta(identificadorParada1, nif, normal);
        });
    }


    @Test
    public void testDevolverBicicletaCuandoLaBiciEstaDisponible(){
        
        gestor.anadirParada(parada1);  
        assertThrows (IllegalStateException.class, ()->{
            gestor.devolverBicicleta(identificadorParada1, nif, normal);
        });
    }


    @Test
    public void testDevolverBicicletaCuandoIdParadaNull(){
        
        gestor.anadirParada(parada1);  
        assertThrows (IllegalArgumentException.class, ()->{
            gestor.devolverBicicleta(null, nif, normal);
        });
    }


    @Test
    public void testDevolverBicicletaCuandoNifNull(){
        
        gestor.anadirParada(parada1);  
        assertThrows (IllegalArgumentException.class, ()->{
            gestor.devolverBicicleta(identificadorParada1, null, normal);
        });
    }


    @Test
    public void testDevolverBicicletaCuandoBiciNull(){
        
        gestor.anadirParada(parada1);  
        assertThrows (IllegalArgumentException.class, ()->{
            gestor.devolverBicicleta(identificadorParada1, nif, null);
        });
    }


    @Test
    public void testDevolverBicicletaCuandoLaParadaNoEstaEnElGestor(){
        
        gestor.anadirParada(parada1);  
        assertThrows (IllegalStateException.class, ()->{
            gestor.devolverBicicleta(identificadorParada2, nif, normal);
        });
    }


    @Test
    public void testDevolverBicicletaCuandoElUsuarioNoHizoUnAlquilerSobreEsaBicicleta(){
        
        gestor.anadirParada(parada1);  
        assertThrows (IllegalStateException.class, ()->{
            gestor.devolverBicicleta(identificadorParada2, "0594778R", normal);
        });
    }


      @Test
     public void testBloquearBicicleta(){
        
        gestor.anadirParada(parada1);
        gestor.bloquearBicicleta(identificadorParada1, identificadorElectrica);
        gestor.bloquearBicicleta(identificadorParada1, identificadorBici);
        Bloqueo bloqueoObtenido = gestor.getBloqueo(identificadorBici);

        assertTrue(bloqueoObtenido.getBicicleta().isBloqueada());
        normal.setEstadoBloqueada();
        assertEquals(bloqueoObtenido.getBicicleta(), normal);
      }

      
      @Test
     public void testBloquearBicicletaEnParadaEquivocada(){
        Parada parada3 = new Parada("3", lat1, lon1, direccion, new ArrayList<Bicicleta>(), HUECOS_APARCAMIENTO, true);

        
        gestor.anadirParada(parada1);
        
        gestor.anadirParada(parada3);
        parada2.setEstado(true);
        assertThrows(IllegalStateException.class,()->{
            gestor.bloquearBicicleta("3", identificadorBici);
        });
     }


     @Test
     public void testBloquearBicicletaBloqueada(){
        normal.setEstadoBloqueada();
        
        
        gestor.anadirParada(parada1);        
        assertThrows(IllegalStateException.class,()->{
            gestor.bloquearBicicleta(identificadorParada1, identificadorBici);
        });
     }

    @Test
    public void testBloquearBicicletaCuandoLaParadaEstaDesactivada() {
        
        gestor.anadirParada(parada1);
        gestor.desactivarParada(identificadorParada1);
        assertThrows(IllegalStateException.class,()->{
            gestor.bloquearBicicleta(identificadorParada1, identificadorBici);
            });
    }


    @Test
    public void testBloquearBicicletaCuandoIdParadaNull() {
        
        gestor.anadirParada(parada1);
        assertThrows(IllegalArgumentException.class, () -> {
            gestor.bloquearBicicleta(null, identificadorBici);
        });
    }


    @Test
    public void testBloquearBicicletaCuandoLaParadaNoEsta() {
        
        gestor.anadirParada(parada1);
        assertThrows(IllegalStateException.class, () -> {
            gestor.bloquearBicicleta(identificadorParada2, identificadorBici);
        });
    }


    
    @Test
    public void testBloquearBicicletaCuandoNoEstaLaParada() {
        
        gestor.anadirParada(parada1);
        assertThrows(IllegalStateException.class, () -> {
            gestor.desactivarParada(identificadorParada2);
        });
    }



    @Test
    public void testBloquearBicicletaCuandoIdBicicletaNull() {
        
        gestor.anadirParada(parada1);
        assertThrows(IllegalArgumentException.class, () -> {
            gestor.bloquearBicicleta(identificadorParada1, null);
        });
    }


    @Test
    public void testGetBloqueoCuandoIdBicicletaNull() {
        
        gestor.anadirParada(parada1);
        gestor.bloquearBicicleta(identificadorParada1, identificadorBici);
        assertThrows(IllegalArgumentException.class, () -> {
            gestor.getBloqueo(null);
        });
    }


    @Test
    public void testGetBloqueoCuandoLaBiciNoTieneBloqueo() {
        
        gestor.anadirParada(parada1);
        gestor.bloquearBicicleta(identificadorParada1, identificadorBici);
        assertThrows(IllegalStateException.class, () -> {
            gestor.getBloqueo("no");
        });
    }

    
    @Test
    public void testDesbloquearBicicleta(){
        
        gestor.anadirParada(parada1);
        gestor.bloquearBicicleta(identificadorParada1, identificadorBici);
        gestor.desbloquearBicicleta(identificadorParada1,identificadorBici);
        assertTrue(gestor.getParada(identificadorParada1).getBicicleta(identificadorBici).isDisponible());
    }

    @Test
    public void testDesbloquearBicicletaOtraParada(){
        
        gestor.anadirParada(parada1);
        gestor.bloquearBicicleta(identificadorParada1, identificadorBici);
        gestor.anadirParada(parada2);
        gestor.activarParada(identificadorParada2);
        assertThrows(IllegalStateException.class,()->{
            gestor.desbloquearBicicleta(identificadorParada2, identificadorBici);
        });
    }

    @Test
    public void testDesbloquearBicicletaDisponible(){
        
        gestor.anadirParada(parada1);
        assertThrows(IllegalStateException.class,()->{
            gestor.desbloquearBicicleta(identificadorParada1,identificadorBici);
        });
    }


    @Test
     public void testDesbloquearBiciCuanndoLaParadaEstaDesactivada(){
        
        gestor.anadirParada(parada1);
        gestor.bloquearBicicleta(identificadorParada1, identificadorBici);
        gestor.desactivarParada(identificadorParada1);
        assertThrows(IllegalStateException.class,()->{
            gestor.desbloquearBicicleta(identificadorParada1, identificadorBici);
        });
    }

    @Test
    public void testDesbloquearBicicletaCuandoIdParadaNull() {
        
        gestor.anadirParada(parada1);
        gestor.bloquearBicicleta(identificadorParada1, identificadorBici);
        assertThrows(IllegalArgumentException.class,()->{
            gestor.desbloquearBicicleta(null, identificadorBici);
        });
    }


    @Test
    public void testDesbloquearBicicletaCuandoIdBiciNull() {
        
        gestor.anadirParada(parada1);
        gestor.bloquearBicicleta(identificadorParada1, identificadorBici);
        assertThrows(IllegalArgumentException.class,()->{
            gestor.desbloquearBicicleta(identificadorParada1, null);
        });
    }

    @Test
    public void testDesbloquearBicicletaCuandoLaParadaNoEsta() {
        
        gestor.anadirParada(parada1);
        gestor.bloquearBicicleta(identificadorParada1, identificadorBici);
        assertThrows(IllegalStateException.class,()->{
            gestor.desbloquearBicicleta(identificadorParada2, identificadorBici);
        });
    }


    @Test
    public void testReservaBicicletaConTodosLosParametrosValidos() {
        
        gestor.anadirParada(parada1);
        gestor.reservaBicicleta(identificadorParada1, identificadorBici, usuario);
        assertEquals(gestor.getReservasBicicletas().size(), 1);
    }


    @Test
    public void testReservaBicicletaConUsuarioInactivo() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            session.save(usuario); 
            session.getTransaction().commit();
        } finally {
            session.close();
        }
    
        session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            usuario = (Usuario)session.merge(usuario); 
            usuario.setEstado(false);         
            session.getTransaction().commit();
        } finally {
            session.close();
        }
    
        Bicicleta bici = new Bicicleta("nuevaB");
        session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            session.save(bici); 
            session.getTransaction().commit();
        } finally {
            session.close();
        }
    
        gestor.anadirParada(parada1);
        gestor.agregarBicicleta(identificadorParada1, bici);
    
        assertThrows(IllegalStateException.class, () -> {
            gestor.reservaBicicleta(identificadorParada1, "nuevaB", usuario);
        });
    }
    

    @Test
    public void testReservaBicicletaConParadaDesactivada() {
        
        gestor.anadirParada(parada1);
        gestor.desactivarParada(identificadorParada1);
        assertThrows(IllegalStateException.class, () -> {
            gestor.reservaBicicleta(identificadorParada1, identificadorBici, usuario);
        });
    }


    @Test
    public void testReservaBicicletaConUsuarioQueTieneOtraReserva() {
        Bicicleta bici = new Bicicleta("nuevaB");
        
        
        gestor.anadirParada(parada1);
        gestor.agregarBicicleta(identificadorParada1, bici);
        gestor.reservaBicicleta(identificadorParada1, "nuevaB", usuario);
        assertThrows(IllegalStateException.class, () -> {
            gestor.reservaBicicleta(identificadorParada1, electrica.getIdentificador(), usuario);
        });
    }


    @Test
    public void testReservaBicicletaDeUnaBiciNoDisponible() {
        
        gestor.anadirParada(parada1);
        gestor.agregarBicicleta(identificadorParada1, biciOcupada);
        gestor.alquilarBicicleta(identificadorParada1, biciOcupada.getIdentificador(), usuario);
        assertThrows(IllegalStateException.class, () -> {
            gestor.reservaBicicleta(identificadorParada1, biciOcupada.getIdentificador(), usuario);
        });
    }


    @Test
    public void testReservaBicicletaCuandoUsuarioNull() {
        
        gestor.anadirParada(parada1);
        assertThrows(IllegalArgumentException.class, () -> {
            gestor.reservaBicicleta(identificadorParada1, identificadorBici, null);
        });
    }


    @Test
    public void testReservaBicicletaCuandoBiciNoEnParada() {
        
        gestor.anadirParada(parada1);
        assertThrows(IllegalStateException.class, () -> {
            gestor.reservaBicicleta(identificadorParada1, "no", usuario);
        });
    }


    @Test
    public void testReservaBicicletaCuandoBiciNull() {
        
        gestor.anadirParada(parada1);
        assertThrows(IllegalArgumentException.class, () -> {
            gestor.reservaBicicleta(identificadorParada1, null, usuario);
        });
    }


    @Test
    public void testReservaBicicletaCuandoIdParadaNull() {
        
        gestor.anadirParada(parada1);
        assertThrows(IllegalArgumentException.class, () -> {
            gestor.reservaBicicleta(null, identificadorBici, usuario);
        });
    }


    @Test
    public void testReservaBicicletaCuandoIdParadaNoExistente() {
        
        gestor.anadirParada(parada1);
        assertThrows(IllegalStateException.class, () -> {
            gestor.reservaBicicleta("no", identificadorBici, usuario);
        });
    }


    @Test 
    public void testIsParadaEnGestorCuandoLoEsta() {
        
        gestor.anadirParada(parada1);
        assertTrue(gestor.isParadaEnGestor(identificadorParada1));
    }


    @Test 
    public void testIsParadaEnGestorCuandoNoLoEsta() {
        
        gestor.anadirParada(parada1);
        assertFalse(gestor.isParadaEnGestor(identificadorParada2));
    }


    @Test 
    public void testIsParadaEnGestorCuandoElIdEsNull(){
        
        gestor.anadirParada(parada1);

        assertThrows(IllegalArgumentException.class, () -> {
            gestor.isParadaEnGestor(null);
        });
    }


    @Test
    public void testDesactivarParadaCuandoEstaActivada() {
        
        gestor.anadirParada(parada1);
        gestor.desactivarParada(identificadorParada1);
        assertFalse(gestor.getParada(identificadorParada1).isActiva());
    }


    @Test
    public void testDesactivarParadaCuandoEstaDesactivada() {        
        parada1.setEstado(false);

        
        gestor.anadirParada(parada1);
        assertThrows(IllegalStateException.class, () -> {
            gestor.desactivarParada(identificadorParada1);});
    }


    @Test
    public void testDesactivarParadaCuandoIdParadaEsNull() {
        
        gestor.anadirParada(parada1);
        assertThrows(IllegalArgumentException.class, () -> {
            gestor.desactivarParada(null);
        });
    }


    @Test
    public void testDesactivarParadaCuandoNoEstaLaParada() {
        
        gestor.anadirParada(parada1);
        assertThrows(IllegalArgumentException.class, () -> {
            gestor.desactivarParada(null);
        });
    }


    @Test
    public void testActivarParadaCuandoEstaDesactivada() {
        parada1.setEstado(false);
        parada2.setEstado(true);

        
        gestor.anadirParada(parada2);
        gestor.anadirParada(parada1);
        gestor.activarParada(identificadorParada1);
        assertTrue(gestor.getParada(identificadorParada1).isActiva());
    }


    @Test
    public void testActivarParadaCuandoEstaActivada() {
        
        gestor.anadirParada(parada1);
        assertThrows(IllegalStateException.class, () -> {
            gestor.activarParada(identificadorParada1);});
    }


    @Test
    public void testActivarParadaConIdParadaNulo() {
        parada1.setEstado(false);

        
        gestor.anadirParada(parada1);
        assertThrows(IllegalArgumentException.class, () -> {
            gestor.activarParada(null);});
    }


    @Test
    public void testActivarParadaConIdParadaNoExistente() {
        parada1.setEstado(false);

        
        gestor.anadirParada(parada1);
        assertThrows(IllegalStateException.class, () -> {
            gestor.activarParada(identificadorParada2);});
    }


    @Test
    public void testAnadirParadaNula() {
        
        assertThrows(IllegalArgumentException.class, () -> {
            gestor.anadirParada(null);
        });
    }


    @Test
    public void testGetBicicletasParadaCuandoIdParadaNull() {
        
        gestor.anadirParada(parada1);
        assertThrows(IllegalArgumentException.class, () -> {
            gestor.getBicicletasParada(null);
        });
    }


    @Test
    public void testGetParadaCuandoIdParadaNull() {
        
        gestor.anadirParada(parada1);
        assertThrows(IllegalArgumentException.class, () -> {
            gestor.getParada(null);
        });
    }


    @Test
    public void testGetParadaCuandoNoEstaEnElGestor() {
        
        gestor.anadirParada(parada2);
        assertThrows(IllegalStateException.class, () -> {
            gestor.getParada(identificadorParada1);
        });
    }


    @Test
    public void testGetAparcamientosDisponiblesCuandoIdParadaNull() {
        
        gestor.anadirParada(parada1);
        assertThrows(IllegalArgumentException.class, () -> {
            gestor.getAparcamientosDisponibles(null);
        });
    }


    @Test 
    public void testGetParadasDisponiblesUbicacionCuandoLaDistanciaEsMenorQueElLimiteInferior() {
        
        gestor.anadirParada(parada1);
        assertThrows(IllegalArgumentException.class, () -> {
            gestor.getParadasDisponiblesUbicacion(10,10, -0.0001);
        });
    }

    @Test 
    public void testGetParadasDisponiblesUbicacionCuandoTodoEsValido() {
        ArrayList<Bicicleta> bicisParada3 = new ArrayList<>();
        Parada parada3 = new Parada("id3", -90.0, 10.0, "C", bicisParada3, HUECOS_APARCAMIENTO, true);
        ArrayList <Parada> listaParadasEsperada = new ArrayList<>();
        listaParadasEsperada.add(parada1);

        
        gestor.anadirParada(parada1);
        gestor.anadirParada(parada2);
        gestor.anadirParada(parada3);   
        assertArrayEquals(listaParadasEsperada.toArray(), gestor.getParadasDisponiblesUbicacion(-55.0,95.0,1000000).toArray());
    }


    
}