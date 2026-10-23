package uva.tds.gestores;
import uva.tds.base.*;
import  java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
 
/**
 * Implementación clase que prueba los tests de la clase GestorParada
 * @author Ainhoa Carbajo Orgaz
 * @author Emily Rodrigues
 */
public class GestorParadasTest {
    Bicicleta normal;
    Bicicleta electrica;
    Bicicleta biciOcupada;
    Bicicleta nueva;
    Bicicleta bici;

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
   

    @BeforeEach
    void startUp(){
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
        bici = new Bicicleta("bici");

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

    }

    @Test
    public void testGestorParadas(){
        ArrayList <Parada> paradas = new ArrayList<>();
        paradas.add(parada1);
        paradas.add(parada2);

        ArrayList <Parada> paradasDisponibles = new ArrayList<>();
        paradasDisponibles.add(parada1);
        GestorParadas gestorParadas = new GestorParadas(paradas);

        assertArrayEquals(paradas.toArray(), gestorParadas.getParadas().toArray());
        assertArrayEquals(paradasDisponibles.toArray(), gestorParadas.getParadasActivas().toArray());
    }

    @Test
    public void testGestorParadasVacio(){
        ArrayList <Parada> paradas = new ArrayList<>();
        assertThrows(IllegalArgumentException.class, () -> {
            new GestorParadas(paradas);
        });
    }


    @Test
    public void testGestorParadasCuandoLaListaEsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new GestorParadas(null);
        });
    }

  
    @Test
    public void testGestorParadasCuandoLaListaTieneParadasRepetidas() {
        ArrayList<Parada> listaRepetidas = new ArrayList<>();
        listaRepetidas.add(parada1);
        listaRepetidas.add(parada1);
        assertThrows(IllegalArgumentException.class, () -> {
            new GestorParadas(listaRepetidas);
        });
    }


    @Test
    public void testAnadirParada(){
       
        GestorParadas gestorParadas = new GestorParadas();
        gestorParadas.anadirParada(parada1);
        gestorParadas.anadirParada(parada2);

        ArrayList <Parada> listaParadasEsperada = new ArrayList<>();
        Parada pEsperada1 = new Parada (identificadorParada1,-50,100,direccion,bicicletas,HUECOS_APARCAMIENTO,true);
        Parada pEsperada2 = new Parada (identificadorParada2,-60,90,direccion,bicicletas,HUECOS_APARCAMIENTO,true);
        listaParadasEsperada.add(pEsperada1);
        listaParadasEsperada.add(pEsperada2);
        assertArrayEquals(listaParadasEsperada.toArray(), gestorParadas.getParadas().toArray());

    }

    @Test
    public void testAnadirParadaRepetida(){
       
        GestorParadas gestorParadas = new GestorParadas();
        Parada parada = new Parada (identificadorParada1,-50,100,direccion,bicicletas,HUECOS_APARCAMIENTO,true);
        gestorParadas.anadirParada(parada1);
       

        assertThrows(IllegalStateException.class, () -> {
            gestorParadas.anadirParada(parada);
        });

    }


    @Test
    public void testAnadirParadaCuandoLaParadaEsNull() {
        GestorParadas gestorParadas = new GestorParadas();
        assertThrows(IllegalArgumentException.class, () -> {
            gestorParadas.anadirParada(null);
        });
    }

  

    @Test
    public void testGetBicicletasDisponiblesIdParada(){

        GestorParadas gestorParadas = new GestorParadas();
        gestorParadas.anadirParada(parada1);
        gestorParadas.anadirParada(parada2);
        
        assertArrayEquals(bicicletasDisponibles.toArray(), gestorParadas.getBicicletasParada(identificadorParada1).toArray());
    }

    @Test
    public void testGetBicicletasDisponiblesParadaNoIncluida(){

        GestorParadas gestorParadas = new GestorParadas();
        gestorParadas.anadirParada(parada1);
        assertThrows(IllegalStateException.class, () -> {
            gestorParadas.getBicicletasParada(identificadorParada2);
        });}

    @Test
    public void testGetEstacionamientosDisponiblesIdParada(){

        GestorParadas gestorParadas = new GestorParadas();
        gestorParadas.anadirParada(parada1);

        assertEquals(2, gestorParadas.getAparcamientosDisponibles(identificadorParada1));
    }


    @Test
    public void testgetParadasDisponiblesUbicacionConParadasEnElRango(){
        Parada parada3 = new Parada("id3", -90.0, 10.0, "C", bicicletas, HUECOS_APARCAMIENTO, true);
       
        GestorParadas gestorParadas = new GestorParadas();
        gestorParadas.anadirParada(parada1);
        gestorParadas.anadirParada(parada2);
        gestorParadas.anadirParada(parada3);

        ArrayList <Parada> listaParadasEsperada = new ArrayList<>();

        listaParadasEsperada.add(parada1);
   

        assertArrayEquals(listaParadasEsperada.toArray(), gestorParadas.getParadasDisponiblesUbicacion(-55.0,95.0,1000000).toArray());
    }


    @Test
    public void testgetParadasDisponiblesUbicacionConDistanciaIgualAlLimteInferiorYSinParadasCerca(){
        Parada parada3 = new Parada("id3", -90.0, 10.0, "C", bicicletas, HUECOS_APARCAMIENTO, true);
       
        GestorParadas gestorParadas = new GestorParadas();
        gestorParadas.anadirParada(parada1);
        gestorParadas.anadirParada(parada2);
        gestorParadas.anadirParada(parada3);

        ArrayList <Parada> listaParadasEsperada = new ArrayList<>();
   

        assertArrayEquals(listaParadasEsperada.toArray(), gestorParadas.getParadasDisponiblesUbicacion(-55.0,95.0, 0).toArray());
    }


    @Test
    public void testgetParadasDisponiblesUbicacionConDistanciaJustoMenorAlLimteInferior(){       
        GestorParadas gestorParadas = new GestorParadas();
        gestorParadas.anadirParada(parada1);
        gestorParadas.anadirParada(parada2);
        assertThrows(IllegalArgumentException.class, () -> {
            gestorParadas.getParadasDisponiblesUbicacion(-55.0,95.0, -0.01);
        });        
    }
    

    @Test
    public void testAgregarBicicleta(){
        GestorParadas gestorParadas = new GestorParadas();
        gestorParadas.anadirParada(parada1);
        gestorParadas.anadirParada(parada2);
        gestorParadas.agregaBicicleta(identificadorParada1,nueva);
        bicicletasDisponibles.add(nueva);
        assertArrayEquals(bicicletasDisponibles.toArray(), gestorParadas.getBicicletasParada(identificadorParada1).toArray());
        assertEquals(EstadoBicicleta.DISPONIBLE,nueva.getEstado());
    

    }

    @Test
    public void testAgregarBicicletaSinHuecos(){
        GestorParadas gestorParadas = new GestorParadas();
        biciOcupada.setEstadoDisponible();
        parada1.agregaBicicleta(biciOcupada);
        gestorParadas.anadirParada(parada1);
        gestorParadas.agregaBicicleta(identificadorParada1,nueva);
        assertThrows (IllegalStateException.class, ()->{
            gestorParadas.agregaBicicleta(identificadorParada1,new Bicicleta ("id4"));
        });
       
    }

    @Test
    public void testAgregarBicicletaParadaNoEsta(){
        GestorParadas gestorParadas = new GestorParadas();
        biciOcupada.setEstadoDisponible();
        parada1.agregaBicicleta(biciOcupada);
        gestorParadas.anadirParada(parada1);
        gestorParadas.agregaBicicleta(identificadorParada1,nueva);
        assertThrows (IllegalStateException.class, ()->{
            gestorParadas.agregaBicicleta(identificadorParada2,new Bicicleta ("id4"));
        });
       
    }

    @Test
    public void testAgregarBicicletaMismoIdEnLaMismaParada(){
        GestorParadas gestorParadas = new GestorParadas();
        gestorParadas.anadirParada(parada1);
        gestorParadas.agregaBicicleta(identificadorParada1,nueva);
        assertThrows (IllegalStateException.class, ()->{
            gestorParadas.agregaBicicleta(identificadorParada1,new Bicicleta ("id2"));
        });
    }


    @Test
    public void testAgregarBicicletaMismoIdEnOtraParada(){
        GestorParadas gestorParadas = new GestorParadas();
        gestorParadas.anadirParada(parada1);
        parada2.setEstado(true);
        gestorParadas.anadirParada(parada2);
        gestorParadas.agregaBicicleta(identificadorParada1, nueva);
        assertThrows (IllegalStateException.class, ()->{
            gestorParadas.agregaBicicleta(identificadorParada2,new Bicicleta ("id2"));
        });
    }


    @Test
    public void testAgregarBicicletaCuandoIdParadaNull() {
        GestorParadas gestorParadas = new GestorParadas();
        gestorParadas.anadirParada(parada1);
        assertThrows (IllegalArgumentException.class, ()->{
            gestorParadas.agregaBicicleta(null, new Bicicleta ("id2"));
        });
    }


    @Test
    public void testAgregarBicicletaCuandoBicicletaNull() {
        GestorParadas gestorParadas = new GestorParadas();
        gestorParadas.anadirParada(parada1);
        assertThrows (IllegalArgumentException.class, ()->{
            gestorParadas.agregaBicicleta(identificadorParada1, null);
        });
    }
   
     @Test 
     public void testEliminarBicicleta(){
        GestorParadas gestorParadas = new GestorParadas();
        gestorParadas.anadirParada(parada1);
        gestorParadas.eliminarBicicleta(identificadorParada1,identificadorBici);
        bicicletasDisponibles.remove(normal);
        assertArrayEquals(bicicletasDisponibles.toArray(), gestorParadas.getBicicletasParada(identificadorParada1).toArray());
     }

     

     @Test 
     public void testEliminarBicicletaNoEsta(){
        GestorParadas gestorParadas = new GestorParadas();
        gestorParadas.anadirParada(parada1);
        assertThrows (IllegalStateException.class, ()->{
        gestorParadas.eliminarBicicleta(identificadorParada1, "ocup");
       });
     }

     @Test 
     public void testEliminarBicicletaParadaNoEsta(){
        GestorParadas gestorParadas = new GestorParadas();
        gestorParadas.anadirParada(parada1);
        assertThrows (IllegalStateException.class, ()->{
        gestorParadas.eliminarBicicleta(identificadorParada2, "ocup");
       });
     }

    @Test 
    public void testEliminarBicicletaAlquilada(){
        GestorParadas gestorParadas = new GestorParadas();
        gestorParadas.anadirParada(parada1);
        gestorParadas.alquilarBicicleta(parada1.getIdentificador(), identificadorBici, otroUsuario);
        gestorParadas.agregaBicicleta(parada1.getIdentificador(), bici);
        gestorParadas.alquilarBicicleta(parada1.getIdentificador(), "bici", usuario);
        assertThrows(IllegalStateException.class, ()->{
            gestorParadas.eliminarBicicleta(parada1.getIdentificador(), "bici");
       });
    }


    @Test 
    public void testEliminarBicicletaReservada(){
        GestorParadas gestorParadas = new GestorParadas();

        gestorParadas.anadirParada(parada1);
        gestorParadas.agregaBicicleta(parada1.getIdentificador(), bici);
        gestorParadas.reservaBicicleta(identificadorParada1, "bici", usuario);

        gestorParadas.reservaBicicleta(identificadorParada1, identificadorBici, otroUsuario);
        gestorParadas.eliminarBicicleta(identificadorParada1, "bici");
        assertEquals(gestorParadas.getReservasBicicletas().size(), 1);
    }


    @Test 
    public void testEliminarBicicletaBloqueada(){
        GestorParadas gestorParadas = new GestorParadas();
        gestorParadas.anadirParada(parada1);
        gestorParadas.agregaBicicleta(parada1.getIdentificador(), bici);
        gestorParadas.bloquearBicicleta(identificadorParada1, identificadorBici);
        gestorParadas.bloquearBicicleta(identificadorParada1, "bici");
        gestorParadas.eliminarBicicleta(identificadorParada1, "bici");
        assertEquals(gestorParadas.getListaBloqueos().size(), 1);
    }


    @Test
    public void testEliminarBicicletaCuandoIdParadaNull() {
        GestorParadas gestorParadas = new GestorParadas();
        gestorParadas.anadirParada(parada1);
        assertThrows(IllegalArgumentException.class, () -> {
            gestorParadas.eliminarBicicleta(null, identificadorBici);
        });
    }


    @Test
    public void testEliminarBicicletaCuandoIdBiciNull() {
        GestorParadas gestorParadas = new GestorParadas();
        gestorParadas.anadirParada(parada1);
        assertThrows(IllegalArgumentException.class, () -> {
            gestorParadas.eliminarBicicleta(identificadorParada1, null);
        });
    }

     @Test
     public void testAlquilarBicicleta(){
        GestorParadas gestorParadas = new GestorParadas();
        electrica.setEstadoDisponible();
        gestorParadas.anadirParada(parada1);
        gestorParadas.alquilarBicicleta(identificadorParada1, identificadorElectrica, otroUsuario);
        gestorParadas.alquilarBicicleta(identificadorParada1, identificadorBici, usuario);

        assertEquals(gestorParadas.getAlquileresEnCurso().size(), 2);
        assertEquals(gestorParadas.getAlquileresEnCurso().get(1).getBicicleta().getIdentificador(), identificadorBici);
        assertEquals(gestorParadas.getAlquileresEnCurso().get(1).getUsuario().getNif(), nif);
        assertTrue(gestorParadas.tieneAlquilerEnCurso(usuario));
      }

      //TODO 
     @Test
     public void testAlquilarBicicletaReservada(){
        GestorParadas gestorParadas = new GestorParadas();
        gestorParadas.anadirParada(parada1);
        gestorParadas.alquilarBicicleta(identificadorParada1, identificadorElectrica, otroUsuario);
        gestorParadas.reservaBicicleta(identificadorParada1, identificadorBici, usuario);
        gestorParadas.alquilarBicicleta(identificadorParada1, identificadorBici, usuario);
       
        assertEquals(gestorParadas.getAlquiler(nif, identificadorBici).getBicicleta(),normal);
     }

     @Test
     public void testAlquilarBicicletaBloqueada(){
        GestorParadas gestorParadas = new GestorParadas();
        gestorParadas.anadirParada(parada1);
        normal.setEstadoBloqueada();
        assertThrows (IllegalStateException.class, ()->{
        gestorParadas.alquilarBicicleta(identificadorParada1, identificadorBici, usuario );
        });
     }

     @Test
     public void testAlquilarBicicletaNoEsta(){
        GestorParadas gestorParadas = new GestorParadas();
        gestorParadas.anadirParada(parada1);
        assertThrows (IllegalStateException.class, ()->{
        gestorParadas.alquilarBicicleta(identificadorParada1, "id2" ,usuario );
        });
     }

     @Test
     public void testAlquilarBicicletaParadaNoEsta(){
        GestorParadas gestorParadas = new GestorParadas();
        gestorParadas.anadirParada(parada1);
       
        assertThrows (IllegalStateException.class, ()->{
        gestorParadas.alquilarBicicleta(identificadorParada2, identificadorBici,usuario );
        });
     }

     @Test
     public void testAlquilarBicicletaUsuarioInactivo(){
        GestorParadas gestorParadas = new GestorParadas();
        gestorParadas.anadirParada(parada1);
        usuario.setEstado(false);
        assertThrows (IllegalStateException.class, ()->{
        gestorParadas.alquilarBicicleta(identificadorParada1, identificadorBici,usuario );
        });
     }

     @Test
     public void testAlquilarBicicletaUsuarioSinAlquiler(){
        GestorParadas gestorParadas = new GestorParadas();
        gestorParadas.anadirParada(parada1);
        assertThrows (IllegalStateException.class, ()->{
        gestorParadas.getAlquiler(nif, identificadorBici);
        });
     }
     
     @Test
     public void testAlquilarBicicletaUsuarioAlquiler(){
        GestorParadas gestorParadas = new GestorParadas();
        gestorParadas.anadirParada(parada1);
        gestorParadas.alquilarBicicleta(identificadorParada1, identificadorBici, usuario );
        assertThrows (IllegalStateException.class, ()->{
            gestorParadas.alquilarBicicleta(identificadorParada1, identificadorElectrica, usuario );
        });
     }

    @Test 
    public void testAlquilarBicicletaReservadaConUsuarioValidoEnTiempoValido() {
        GestorParadas gestorParadas = new GestorParadas();
        gestorParadas.anadirParada(parada1);
        gestorParadas.reservaBicicleta(identificadorParada1, identificadorElectrica, usuario);
        gestorParadas.alquilarBicicleta(identificadorParada1, identificadorElectrica, usuario);
        assertEquals(gestorParadas.getAlquileresEnCurso().size(), 1);
        assertEquals(gestorParadas.getReservasBicicletas().size(), 0);
    }


    @Test 
    public void testAlquilarBicicletaReservadaConUsuarioInvalidoEnTiempoValido() {
        GestorParadas gestorParadas = new GestorParadas();
        gestorParadas.anadirParada(parada1);
        gestorParadas.reservaBicicleta(identificadorParada1, identificadorBici, usuario);
        Usuario invalido = new Usuario ("Marta", "71056982T",5,true);
        assertThrows(IllegalStateException.class, () -> {
             gestorParadas.alquilarBicicleta(identificadorParada1, identificadorBici, invalido);});
    }


    @Test 
    public void testAlquilarBicicletaCuandoLaParadaEstaDesactivada() {
        GestorParadas gestorParadas = new GestorParadas();
        gestorParadas.anadirParada(parada1);
        gestorParadas.desactivarParada(identificadorParada1);
        assertThrows(IllegalStateException.class, () -> {
            gestorParadas.alquilarBicicleta(parada1.getIdentificador(), identificadorElectrica, usuario);});
    }


    @Test 
    public void testAlquilarBicicletaCuandoIdParadaNull() {
        GestorParadas gestorParadas = new GestorParadas();
        gestorParadas.anadirParada(parada1);
        assertThrows(IllegalArgumentException.class, () -> {
            gestorParadas.alquilarBicicleta(null, identificadorElectrica, usuario);});
    }


    @Test 
    public void testAlquilarBicicletaCuandoIdBiciNull() {
        GestorParadas gestorParadas = new GestorParadas();
        gestorParadas.anadirParada(parada1);
        assertThrows(IllegalArgumentException.class, () -> {
            gestorParadas.alquilarBicicleta(identificadorParada1, null, usuario);});
    }


    @Test 
    public void testAlquilarBicicletaCuandoUsuarioNull() {
        GestorParadas gestorParadas = new GestorParadas();
        gestorParadas.anadirParada(parada1);
        assertThrows(IllegalArgumentException.class, () -> {
            gestorParadas.alquilarBicicleta(identificadorParada1, identificadorElectrica, null);});
    }

     @Test
     public void testDevolverBicicleta(){
        GestorParadas gestorParadas = new GestorParadas();
        gestorParadas.anadirParada(parada1);
        gestorParadas.anadirParada(parada2);
        gestorParadas.alquilarBicicleta(identificadorParada1, identificadorBici, usuario);
        gestorParadas.alquilarBicicleta(identificadorParada1, identificadorElectrica, otroUsuario);
        gestorParadas.devolverBicicleta(identificadorParada1, nif, normal);
        assertFalse(gestorParadas.getBicicletasParada(identificadorParada1).contains(electrica));
        assertTrue(gestorParadas.getParada(identificadorParada1).getBicicleta(identificadorBici).isDisponible());
        assertFalse(gestorParadas.tieneAlquilerEnCurso(usuario));
      }

      @Test
      public void testDevolverBicicletaParadaLlena(){
         GestorParadas gestorParadas = new GestorParadas();
         
         parada1.agregaBicicleta(nueva);
            biciOcupada.setEstadoDisponible();
            parada1.agregaBicicleta(biciOcupada);
            
            gestorParadas.anadirParada(parada1);
         assertThrows (IllegalStateException.class, ()->{
            gestorParadas.devolverBicicleta(identificadorParada1, nif, new Bicicleta("id4"));
        });
      }

      @Test
      public void testDevolverBicicletaNoAlquilada() {
        GestorParadas gestorParadas = new GestorParadas();
        gestorParadas.anadirParada(parada1);
        assertThrows(IllegalStateException.class, () -> {
            gestorParadas.devolverBicicleta(identificadorParada1, nif, nueva);
        });
      }


      @Test
      public void testDevolverBicicletaAlquiladoPorOtroUsuario() {
        GestorParadas gestorParadas = new GestorParadas();
        gestorParadas.anadirParada(parada1);
        gestorParadas.alquilarBicicleta(identificadorParada1, identificadorBici, otroUsuario);
        assertThrows(IllegalStateException.class, () -> {
            gestorParadas.devolverBicicleta(identificadorParada1, nif, normal);
        });
      }


      @Test
     public void testBloquearBicicleta(){
        GestorParadas gestorParadas = new GestorParadas();
        gestorParadas.anadirParada(parada1);
        gestorParadas.bloquearBicicleta(identificadorParada1, identificadorElectrica);
        gestorParadas.bloquearBicicleta(identificadorParada1, identificadorBici);

        assertTrue(normal.isBloqueada());
        assertEquals(gestorParadas.getBloqueo(identificadorBici).getBicicleta(),normal);
      }

      
      @Test
     public void testBloquearBicicletaOtraParada(){
        GestorParadas gestorParadas = new GestorParadas();
        gestorParadas.anadirParada(parada1);
        Parada parada3 = new Parada("3", lat1, lon1, direccion, new ArrayList<Bicicleta>(), HUECOS_APARCAMIENTO, true);
        gestorParadas.anadirParada(parada3);
        parada2.setEstado(true);
        assertThrows(IllegalStateException.class,()->{
        gestorParadas.bloquearBicicleta("3", identificadorBici);
        });
     }

         @Test
     public void testBloquearBicicletaBloqueada(){
        GestorParadas gestorParadas = new GestorParadas();
        gestorParadas.anadirParada(parada1);
 
        normal.setEstadoBloqueada();
        assertThrows(IllegalStateException.class,()->{
        gestorParadas.bloquearBicicleta(identificadorParada1, identificadorBici);
        });
     }

    @Test
    public void testBloquearBicicletaCuandoLaParadaEstaDesactivada() {
        GestorParadas gestorParadas = new GestorParadas();
        gestorParadas.anadirParada(parada1);
        gestorParadas.desactivarParada(identificadorParada1);
        assertThrows(IllegalStateException.class,()->{
            gestorParadas.bloquearBicicleta(identificadorParada1, identificadorBici);
            });
    }


    @Test
    public void testBloquearBicicletaCuandoIdParadaNull() {
        GestorParadas gestorParadas = new GestorParadas();
        gestorParadas.anadirParada(parada1);
        assertThrows(IllegalArgumentException.class, () -> {
            gestorParadas.bloquearBicicleta(null, identificadorBici);
        });
    }


    @Test
    public void testBloquearBicicletaCuandoIdBicicNull() {
        GestorParadas gestorParadas = new GestorParadas();
        gestorParadas.anadirParada(parada1);
        assertThrows(IllegalArgumentException.class, () -> {
            gestorParadas.bloquearBicicleta(identificadorParada1, null);
        });
    }

        @Test
        public void testDesbloquearBicicleta(){
           GestorParadas gestorParadas = new GestorParadas();
           gestorParadas.anadirParada(parada1);
           gestorParadas.bloquearBicicleta(identificadorParada1, identificadorBici);
           gestorParadas.desbloquearBicicleta(identificadorParada1,identificadorBici);
           assertTrue(normal.isDisponible());
           assertTrue(gestorParadas.getListaBloqueos().isEmpty());
      }

      @Test
      public void testDesbloquearBicicletaOtraParada(){
         GestorParadas gestorParadas = new GestorParadas();
         gestorParadas.anadirParada(parada1);
         gestorParadas.bloquearBicicleta(identificadorParada1, identificadorBici);
         gestorParadas.anadirParada(parada2);
         parada2.setEstado(true);
         assertThrows(IllegalStateException.class,()->{
            gestorParadas.desbloquearBicicleta(identificadorParada2, identificadorBici);
         });
      }

      @Test
      public void testDesbloquearBicicletaDisponible(){
         GestorParadas gestorParadas = new GestorParadas();
         gestorParadas.anadirParada(parada1);


         assertThrows(IllegalStateException.class,()->{
            gestorParadas.desbloquearBicicleta(identificadorParada1,identificadorBici);
         });
      }


      @Test
      public void testDesbloquearBiciCuanndoLaParadaEstaDesactivada(){
         GestorParadas gestorParadas = new GestorParadas();
         normal.setEstadoBloqueada();
         gestorParadas.anadirParada(parada1);
         gestorParadas.desactivarParada(identificadorParada1);

         assertThrows(IllegalStateException.class,()->{
           gestorParadas.desbloquearBicicleta(identificadorParada1, identificadorBici);
         });
      }

    @Test
    public void testReservaBicicletaConTodosLosParametrosValidos() {
        GestorParadas gestorParadas = new GestorParadas();
        gestorParadas.anadirParada(parada1);
        gestorParadas.reservaBicicleta(identificadorParada1, identificadorBici, usuario);
        assertEquals(gestorParadas.getReservasBicicletas().size(), 1);
    }


    @Test
    public void testReservaBicicletaConUsuarioInactivo() {
        GestorParadas gestorParadas = new GestorParadas();
        gestorParadas.anadirParada(parada1);
        bici = new Bicicleta("nuevaB");
        gestorParadas.agregaBicicleta(identificadorParada1, bici);
        usuario.setEstado(false);
       
        assertThrows(IllegalStateException.class, () -> {
            gestorParadas.reservaBicicleta(identificadorParada1, "nuevaB", usuario);
        });
    }


    @Test
    public void testReservaBicicletaConParadaDesactivada() {
        GestorParadas gestorParadas = new GestorParadas();
        gestorParadas.anadirParada(parada1);
        gestorParadas.desactivarParada(identificadorParada1);
       
        assertThrows(IllegalStateException.class, () -> {
            gestorParadas.reservaBicicleta(identificadorParada1, identificadorBici, usuario);
        });
    }


    @Test
    public void testReservaBicicletaConUsuarioQueTieneOtraReserva() {
        GestorParadas gestorParadas = new GestorParadas();
        gestorParadas.anadirParada(parada1);
        bici = new Bicicleta("nuevaB");
        gestorParadas.agregaBicicleta(identificadorParada1, bici);
        gestorParadas.reservaBicicleta(identificadorParada1, "nuevaB", usuario);
        
       
        assertThrows(IllegalStateException.class, () -> {
            gestorParadas.reservaBicicleta(identificadorParada1, electrica.getIdentificador(), usuario);
        });
    }


    @Test 
    public void testIsParadaEnGestorCuandoLoEsta() {
        GestorParadas gestorParadas = new GestorParadas();
        gestorParadas.anadirParada(parada1);
        assertTrue(gestorParadas.isParadaEnGestor(identificadorParada1));
    }


    @Test 
    public void testIsParadaEnGestorCuandoNoLoEsta() {
        GestorParadas gestorParadas = new GestorParadas();
        gestorParadas.anadirParada(parada1);
        assertFalse(gestorParadas.isParadaEnGestor(identificadorParada2));
    }


    @Test 
    public void testIsParadaEnGestorCuandoElIdEsNull(){
        GestorParadas gestorParadas = new GestorParadas();
        gestorParadas.anadirParada(parada1);

        assertThrows(IllegalArgumentException.class, () -> {
            gestorParadas.isParadaEnGestor(null);
        });
    }


    @Test
    public void testDesactivarParadaCuandoEstaActivada() {
        GestorParadas gestorParadas = new GestorParadas();
        gestorParadas.anadirParada(parada1);
        gestorParadas.desactivarParada(identificadorParada1);
        assertFalse(gestorParadas.getParada(identificadorParada1).isActiva());
    }


    @Test
    public void testDesactivarParadaCuandoEstaDesactivada() {
        GestorParadas gestorParadas = new GestorParadas();
        parada1.setEstado(false);
        gestorParadas.anadirParada(parada1);
        assertThrows(IllegalStateException.class, () -> {
            gestorParadas.desactivarParada(identificadorParada1);});
    }


    @Test
    public void testActivarParadaCuandoEstaDesactivada() {
        GestorParadas gestorParadas = new GestorParadas();
        parada1.setEstado(false);
        gestorParadas.anadirParada(parada1);
        gestorParadas.activarParada(identificadorParada1);
        assertTrue(gestorParadas.getParada(identificadorParada1).isActiva());
    }


    @Test
    public void testActivarParadaCuandoEstaActivada() {
        GestorParadas gestorParadas = new GestorParadas();
        gestorParadas.anadirParada(parada1);
        assertThrows(IllegalStateException.class, () -> {
            gestorParadas.activarParada(identificadorParada1);});
    }


    @Test
    public void testTieneUsuarioUnaReservaSinReservas() {
        GestorParadas gestorParadas = new GestorParadas();
        assertFalse(gestorParadas.tieneUsuarioUnaReserva(usuario));
    }


    @Test
    public void testGetListaBloqueosCuandoEstaVacio() {
        GestorParadas gestorParadas = new GestorParadas();
        assertTrue(gestorParadas.getListaBloqueos().isEmpty());
    }


    @Test
    public void testGetParadasDisponiblesUbicacionSinParadasEnRango() {
        GestorParadas gestorParadas = new GestorParadas();
        gestorParadas.anadirParada(parada1);
        ArrayList<Parada> paradas = gestorParadas.getParadasDisponiblesUbicacion(0, 0, 10);
        assertTrue(paradas.isEmpty());
    }


    @Test
    public void testActivarParadaConIdNull() {
        GestorParadas gestorParadas = new GestorParadas();
        assertThrows(IllegalArgumentException.class, () -> {
            gestorParadas.activarParada(null);
        });
    }


    @Test
    public void testDesactivarParadaConIdNull() {
        GestorParadas gestorParadas = new GestorParadas();
        assertThrows(IllegalArgumentException.class, () -> {
            gestorParadas.desactivarParada(null);
        });
    }

    @Test
    public void testTieneUsuarioUnaReservaConReserva() {
        GestorParadas gestorParadas = new GestorParadas();
        gestorParadas.anadirParada(parada1);
        gestorParadas.reservaBicicleta(identificadorParada1, identificadorElectrica, otroUsuario);
        gestorParadas.reservaBicicleta(identificadorParada1, identificadorBici, usuario);
        assertTrue(gestorParadas.tieneUsuarioUnaReserva(usuario));
    }


    @Test
    public void testTieneUsuarioUnaReservaUsuarioNull() {
        GestorParadas gestorParadas = new GestorParadas();
        assertThrows(IllegalArgumentException.class, () -> {
            gestorParadas.tieneUsuarioUnaReserva(null);
        });
    }


    @Test
    public void testAlquilarBicicletaConUsuarioNull() {
        GestorParadas gestorParadas = new GestorParadas();
        gestorParadas.anadirParada(parada1);
        assertThrows(IllegalArgumentException.class, () -> {
            gestorParadas.alquilarBicicleta(identificadorParada1, identificadorBici, null);
        });
    }


    @Test
    public void testAlquilarBicicletaConBicicletaNull() {
        GestorParadas gestorParadas = new GestorParadas();
        gestorParadas.anadirParada(parada1);
        assertThrows(IllegalArgumentException.class, () -> {
            gestorParadas.alquilarBicicleta(identificadorParada1, null, usuario);
        });
    }


    @Test
    public void testGetAlquilerUsuarioSinAlquiler() {
        GestorParadas gestorParadas = new GestorParadas();
        gestorParadas.anadirParada(parada1);

        assertThrows(IllegalStateException.class, () -> {
            gestorParadas.getAlquiler(usuario.getNif(), identificadorBici);
        });
    }


    @Test
    public void testGetAlquilerConNifNull() {
        GestorParadas gestorParadas = new GestorParadas();
        gestorParadas.anadirParada(parada1);
        gestorParadas.alquilarBicicleta(identificadorParada1, identificadorBici, usuario);

        assertThrows(IllegalArgumentException.class, () -> {
            gestorParadas.getAlquiler(null, identificadorBici);
        });
    }


    @Test
    public void testGetAlquilerConIdBiciNull() {
        GestorParadas gestorParadas = new GestorParadas();
        gestorParadas.anadirParada(parada1);
        gestorParadas.alquilarBicicleta(identificadorParada1, identificadorBici, usuario);

        assertThrows(IllegalArgumentException.class, () -> {
            gestorParadas.getAlquiler(nif, null);
        });
    }


    @Test
    public void testGetAlquilerConUsuarioEnAlquilerDeUnaBiciDistintaALaDada() {
        GestorParadas gestorParadas = new GestorParadas();
        gestorParadas.anadirParada(parada1);
        gestorParadas.alquilarBicicleta(identificadorParada1, identificadorBici, usuario);

        assertThrows(IllegalStateException.class, () -> {
            gestorParadas.getAlquiler(nif, identificadorElectrica);
        });
    }


    @Test
    public void testDevolverBicicletaParadaDesactivada() {
        GestorParadas gestorParadas = new GestorParadas();
        gestorParadas.anadirParada(parada1);
        gestorParadas.alquilarBicicleta(identificadorParada1, identificadorBici, usuario);
        gestorParadas.desactivarParada(identificadorParada1);

        assertThrows(IllegalStateException.class, () -> {
            gestorParadas.devolverBicicleta(identificadorParada1, usuario.getNif(), normal);
        });
    }


    @Test
    public void testGetBloqueoBicicletaSinBloqueo() {
        GestorParadas gestorParadas = new GestorParadas();
        gestorParadas.anadirParada(parada1);

        assertThrows(IllegalStateException.class, () -> {
            gestorParadas.getBloqueo(identificadorBici);
        });
    }


    @Test
    public void testGetBloqueoCuandoIdBiciEsNull() {
        GestorParadas gestorParadas = new GestorParadas();
        gestorParadas.anadirParada(parada1);
        gestorParadas.bloquearBicicleta(identificadorParada1, identificadorBici);
        assertThrows(IllegalArgumentException.class, () -> {
            gestorParadas.getBloqueo(null);
        });
    }

    
    @Test
    public void testDesbloquearBicicletaConIdParadaNull() {
        GestorParadas gestorParadas = new GestorParadas();
        gestorParadas.anadirParada(parada1);
        gestorParadas.bloquearBicicleta(identificadorParada1, identificadorBici);

        assertThrows(IllegalArgumentException.class, () -> {
            gestorParadas.desbloquearBicicleta(null, identificadorBici);
        });
    }


    @Test
    public void testDesbloquearBicicletaConIdBiciNull() {
        GestorParadas gestorParadas = new GestorParadas();
        gestorParadas.anadirParada(parada1);
        gestorParadas.bloquearBicicleta(identificadorParada1, identificadorBici);

        assertThrows(IllegalArgumentException.class, () -> {
            gestorParadas.desbloquearBicicleta(identificadorParada1, null);
        });
    }


    @Test
    public void testTieneAlquilerEnCursoCuandoUsuarioNull() {
        GestorParadas gestorParadas = new GestorParadas();
        gestorParadas.anadirParada(parada1);
        assertThrows(IllegalArgumentException.class, () -> {
            gestorParadas.tieneAlquilerEnCurso(null);
        });
    }


    @Test
    public void testTieneAlquilerEnCursoCuandoEstaYHayMasAlquilerers() {
        GestorParadas gestorParadas = new GestorParadas();
        Usuario ortroUsuario= new Usuario ("Lola", "16350874J",5,true);
        gestorParadas.anadirParada(parada1);
        gestorParadas.alquilarBicicleta(identificadorParada1, identificadorBici, ortroUsuario);
        gestorParadas.alquilarBicicleta(identificadorParada1, identificadorElectrica, usuario);
        assertTrue(gestorParadas.tieneAlquilerEnCurso(usuario));
    }
}
