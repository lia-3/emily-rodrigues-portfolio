/**
 * Implementación del algoritmo de Fuerza Bruta para encontrar la secuencia 
 *      más larga de contenedores compatibles.
 * Implementa la interfaz IMedicion para permitir el análisis de tiempo, 
 *      comparaciones y asignaciones.
 * 
 * @author Grupo 31
 */
public class FuerzaBruta implements IMedicion {

    private int[] mejorSolucion;
    private int longitudMejor;

    /**
     * Devuelve el nombre del algoritmo de Fuerza Bruta.
     * @return Nombre del algoritmo de Fuerza Bruta
     */
    @Override
    public String getNombre() {
        return "Fuerza Bruta";
    }

   
    /**
     * Devuelve la lista de índices de la solución encontrada por Fuerza Bruta.
     * @param contenedores Array de contenedores a procesar 
     */
    @Override
    public int[] getIndicesSolucion(Contenedor[] contenedores) {
        return mejorSolucion;
    }


    /**
     * Ejecuta el algoritmo de Fuerza Bruta para encontrar la secuencia 
     *      más larga de contenedores compatibles.
     * @param contenedores Array de contenedores a procesar
     * @throws IllegalArgumentException si el array de contenedores es nulo o vacío
     */
    @Override
    public void ejecutaAlgoritmo(Contenedor[] contenedores) {
        mejorSolucion = null;
        longitudMejor = 0;

        if (contenedores == null || contenedores.length == 0) 
            throw new IllegalArgumentException("El array de contenedores no puede ser nulo o vacío.");

        int numContenedores = contenedores.length;
        int[] permutacion = new int[numContenedores];
        boolean[] contenedoresUsados = new boolean[numContenedores];

        generarPermutaciones(contenedores, permutacion, contenedoresUsados, 0);
    }


    /**
     * Genera todas las permutaciones posibles de los contenedores y evalúa
     *      cada una para encontrar la mejor solución.
     * @param contenedores Array de contenedores a procesar
     * @param permutacion Array que almacena la permutación actual
     * @param contenedoresUsados Array que indica qué contenedores ya han 
     *      sido usados en la permutación actual
     * @param nivel Nivel actual en la generación de permutaciones
     */
    private void generarPermutaciones(Contenedor[] contenedores, int[] permutacion, 
                                        boolean[] contenedoresUsados, int nivel) {
        int numContenedores = contenedores.length;

        if (nivel == numContenedores) {
            evaluarPermutacion(contenedores, permutacion);
            return;
        }

        for (int i = 0; i < numContenedores; i++) {
            if (!contenedoresUsados[i]) {
                contenedoresUsados[i] = true;
                permutacion[nivel] = i;

                generarPermutaciones(contenedores, permutacion, contenedoresUsados, nivel + 1);

                contenedoresUsados[i] = false;
            }
        }
    }


    /**
     * Evalúa una permutación específica de contenedores para determinar la 
     *      longitud de la secuencia más larga de contenedores compatibles.
     * @param contenedores Array de contenedores a procesar
     * @param permutacion Array que representa la permutación actual de 
     *      índices de contenedores
     */
    private void evaluarPermutacion(Contenedor[] contenedores, int[] permutacion) {
        int numContenedores = permutacion.length;

        int longitudSecuenciaActualValida = 1;
        int inicioSecuenciaActualCompatible = 0;

        int mejorLongitudSecuenciaEncontradaLocal = 1;
        int inicioMejorLocal = 0;

        for (int i = 1; i < numContenedores; i++) {
            Contenedor a = contenedores[permutacion[i - 1]];
            Contenedor b = contenedores[permutacion[i]];

            if (a.esCompatibleCon(b)) {
                longitudSecuenciaActualValida++;
                if (longitudSecuenciaActualValida > mejorLongitudSecuenciaEncontradaLocal) {
                    mejorLongitudSecuenciaEncontradaLocal = longitudSecuenciaActualValida;
                    inicioMejorLocal = inicioSecuenciaActualCompatible;
                }
            } else {
                longitudSecuenciaActualValida = 1;
                inicioSecuenciaActualCompatible = i;
            }
        }

        if (mejorLongitudSecuenciaEncontradaLocal > longitudMejor) {
            longitudMejor = mejorLongitudSecuenciaEncontradaLocal;
            mejorSolucion = new int[mejorLongitudSecuenciaEncontradaLocal];
            for (int i = 0; i < mejorLongitudSecuenciaEncontradaLocal; i++) {
                mejorSolucion[i] = contenedores[permutacion[inicioMejorLocal + i]].getIndice();
            }
        }
    }
}
