package uva.tds.interfaces;

/**
 * Interfaz que define las operaciones para consultar la distancia y el tiempo entre dos puntos,
 * dados su latitud y longitud. Es necesario identificarse con un token válido para poder realizar
 * las llamadas a estas operaciones
 * @author marcorr
 */
public interface ICalculoRutas {

    /**
     * Devuelve la menor distancia en metros entre un origen(latitud1,longitud1) 
     * y un destino(latitud2,logitud2) para una ruta en bicicleta
     * @param latitud1 la latitud del origen
     * @param longitud1 la longitud del origen
     * @param latitud2 la latitud del destino
     * @param longitud2 la longitud del destino
     * @return La distancia en metros entre origen y destino
     * @throws IllegalArgumenException si latitud1 es menor que -90 o mayor que 90
     * @throws IllegalArgumenException si longitud1 es menor que -180 o mayor que 180
     * @throws IllegalArgumenException si latitud2 es menor que -90 o mayor que 90
     * @throws IllegalArgumenException si longitud2 es menor que -180 o mayor que 180
     * @throws IllegalStateException si el cliente no está identificado
     */
    public int getDistancia(double latitud1,double longitud1,double latitud2,double longitud2);

    /**
     * Devuelve el menor tiempo en segundos entre un origen(latitud1,longitud1) 
     * y un destino(latitud2,logitud2) para una ruta en bicicleta
     * @param latitud1 la latitud del origen
     * @param longitud1 la longitud del origen
     * @param latitud2 la latitud del destino
     * @param longitud2 la longitud del destino
     * @return El tiempo en segundos entre origen y destino
     * @throws IllegalArgumenException si latitud1 es menor que -90 o mayor que 90
     * @throws IllegalArgumenException si longitud1 es menor que -180 o mayor que 180
     * @throws IllegalArgumenException si latitud2 es menor que -90 o mayor que 90
     * @throws IllegalArgumenException si longitud2 es menor que -180 o mayor que 180
     * @throws IllegalStateException si el cleinte no está identificado
     */
    public int getTiempo(double latitud1,double longitud1,double latitud2,double longitud2);


    /**
     * Identifica al cliente que quiere hacer peticiones a través del token proporcionado
     * @param token El token del cliente
     * @throws IllegalArgumentException si token es nulo
     * @throws IllegalArgumentException si el token no es válido
     */
    public void identificarse(String token);

    /**
     * Indica si se ha realizado una identificación válida
     * @return True si se ha realizado una identificación válida y false en caso contrario
     */
    public boolean clienteIdentificado();

}