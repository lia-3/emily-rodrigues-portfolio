/**
 * Interfaz para los algoritmos de resolucion
 */
public interface IMedicion {

    /**
     * Devuelve la lista de indices de la solucion.
     * @param contenedores Array de contenedores a procesar
     * @return Array con los indices de los contenedores de la solucion, podria ser null
     * si no se ha calculado la solucion aun
     */
    public int[] getIndicesSolucion(Contenedor[] contenedores);
    
    /**
     * Ejecuta el algoritmo completo.
     * @param contenedores Array de contenedores a procesar
     * @throws IllegalArgumentException si el array de contenedores es nulo o vacío
     */
    public void ejecutaAlgoritmo(Contenedor[] contenedores);
    
    /**
     * Devuelve el nombre del algoritmo.
     * @return Nombre del algoritmo
     */
    public String getNombre();
}