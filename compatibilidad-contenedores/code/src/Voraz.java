import java.util.Random;

/**
 * Implementación del algoritmo Voraz Puro para encontrar la secuencia más larga 
 * de contenedores compatibles.
 * 
 * Estrategia Voraz:
 * 1. Elección Voraz: Ordenar los contenedores por la suma de sus atributos (de menor
 *      a mayor).
 * 2. Búsqueda Voraz: Desde cada contenedor, intentar extender la cadena con
 *    el siguiente contenedor compatible más adelante en el array ordenado.
 * 
 * Estrategia de Ordenación: Ordenación Rápida basada en la suma de los atributos
 * 
 * @author Grupo 31
 */
public class Voraz implements IMedicion {

    private int[] mejorSolucion;
    private int longitudMejor;


    /**
     * Obtiene el nombre del algoritmo.
     * @return Nombre del algoritmo "Voraz".
     */
    @Override
    public String getNombre() {
        return "Voraz";
    }


    /**
     * Obtiene los índices de la mejor solución encontrada.
     * @param contenedores Array de contenedores
     *      (puede ser null si no se ha ejecutado el algoritmo).
     * @return Array con los índices de los contenedores en la mejor solución.
     */
    @Override
    public int[] getIndicesSolucion(Contenedor[] contenedores) {
        return mejorSolucion;
    }


    /**
     * Ejecuta el algoritmo Voraz para encontrar la secuencia más larga 
     * de contenedores compatibles.
     * @param contenedores Array de contenedores sobre los que se ejecuta el algoritmo.
     */
    @Override
    public void ejecutaAlgoritmo(Contenedor[] contenedores) {

        if (contenedores == null || contenedores.length == 0)
            throw new IllegalArgumentException("Contenedores nulo o vacío");

        // 1. Ordenar por suma (heurística voraz)
        Contenedor[] copiaContenedores = contenedores.clone();
        ordenacionRapida(copiaContenedores, 0, copiaContenedores.length - 1);

        // 2. Construcción voraz: empezar con el de menor suma
        Contenedor ultimoContenedor = copiaContenedores[0];
        int[] cadena = new int[copiaContenedores.length];
        int tam = 0;

        cadena[tam++] = ultimoContenedor.getIndice();

        // 3. Selección voraz: tomar siguiente compatible
        for (int j = 1; j < copiaContenedores.length; j++) {
            if (ultimoContenedor.esCompatibleCon(copiaContenedores[j])) {
                cadena[tam++] = copiaContenedores[j].getIndice();
                ultimoContenedor = copiaContenedores[j];
            }
        }

        // 4. Guardar solución única
        longitudMejor = tam;
        mejorSolucion = new int[tam];
        for (int k = 0; k < tam; k++)
            mejorSolucion[k] = cadena[k];
    }


    /**
     * Calcula la suma de los atributos de un contenedor.
     * @param c Contenedor del que se quiere calcular la suma de atributos.
     * @return Suma de los atributos del contenedor.
     */
    private long suma(Contenedor c) {
        long s = 0;
        for (int a : c.getAtributos()) s += a;
        return s;
    }


    /**
     * Realiza la ordenación rápida de un array de contenedores
     *      según la suma de sus atributos.
     * @param contenedores Array de contenedores a ordenar.
     * @param indiceInicio Indice de inicio de la porción a ordenar.
     * @param indiceFin Indice de fin de la porción a ordenar.
     */
    private void ordenacionRapida(Contenedor[] contenedores, int indiceInicio, int indiceFin) {
        if (indiceInicio < indiceFin) {
            int izquierda = indiceInicio;
            int derecha = indiceFin;
            Contenedor temporal;

            Random r = new Random();
            int k = izquierda + r.nextInt(derecha - izquierda + 1);

            long p = suma(contenedores[k]);

            while (izquierda <= derecha) {
                while (suma(contenedores[izquierda]) < p) izquierda++;
                while (suma(contenedores[derecha]) > p) derecha--;
                if (izquierda <= derecha) {
                    temporal = contenedores[izquierda];
                    contenedores[izquierda] = contenedores[derecha];
                    contenedores[derecha] = temporal;
                    izquierda++;
                    derecha--;
                }
            }

            ordenacionRapida(contenedores, indiceInicio, derecha);
            ordenacionRapida(contenedores, izquierda, indiceFin);
        }
    }
}
