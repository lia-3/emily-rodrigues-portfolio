import java.util.Arrays;
import java.util.Comparator;

public class ProgramacionDinamica implements IMedicion {
    private int[] mejorSolucion;

    @Override
    public String getNombre() {
        return "Programación Dinámica";
    }

    @Override
    public int[] getIndicesSolucion(Contenedor[] contenedores) {
        return mejorSolucion;
    }

    @Override
    public void ejecutaAlgoritmo(Contenedor[] contenedores) {
        if (contenedores == null || contenedores.length == 0) {
            throw new IllegalArgumentException("El array no puede ser nulo o vacío.");
        }

        final int N = contenedores.length;
        
        Contenedor[] contenedoresOrdenados = contenedores.clone();

        Comparator<Contenedor> comparadorSuma = (c1, c2) -> {
            long sum1 = Arrays.stream(c1.getAtributos()).asLongStream().sum();
            long sum2 = Arrays.stream(c2.getAtributos()).asLongStream().sum();
            return Long.compare(sum1, sum2);
        };

        // Ordenamos los contenedores por la suma de sus atributos (de menor a mayor)
        // si el Contenedor Cj es compatible con Ci, Cj debe ser mas pequeño que Ci.
        // Al ordenar por tamaño (suma de atributos), garantizamos que si Cj < Ci,
        // Cj aparecerá en el array antes que Ci, para poder aplicar PD correctamente
        Arrays.sort(contenedoresOrdenados, comparadorSuma);

        // Longitud[i]: longitud de la secuencia + larga que termina en el contenedor i
        int[] L = new int[N]; 

        // Previo[i]: indice del contenedor anterior en la secuencia optima que termina en i
        int[] Previo = new int[N]; 
        
        // inicializar Previo con -1 (no tiene predecesor)
        Arrays.fill(Previo, -1); 

        // Variables para encontrar la mejor solución global (el max de toda la tabla Longitud)
        int maxLen = 0; // Longitud de la mejor secuencia encontrada
        int maxLenIdx = -1; // Indice donde termina la mejor secuencia encontrada

        // Llenamos la tabla L usando programación dinámica
        for (int i = 0; i < N; i++) {
            
            L[i] = 1; // Cada contenedor puede formar una secuencia de al menos longitud 1

            for (int j = 0; j < i; j++) { // Verificar todos los contenedores antes de i

                // Comparar Cj con Ci
                Contenedor cj = contenedoresOrdenados[j];
                Contenedor ci = contenedoresOrdenados[i];

                if (cj.esCompatibleCon(ci)) {
                    // Si encontramos una secuencia + larga, actualizamos L[i] y Previo[i]
                    if (L[j] + 1 > L[i]) {
                        L[i] = L[j] + 1;
                        Previo[i] = j;
                    }
                }
            }
            if (L[i] > maxLen) {
                maxLen = L[i];
                maxLenIdx = i;
            }
        }
        
        // Recuperamos la secuencia de contenedores desde maxLenIdx hacia atrás
        mejorSolucion = new int[maxLen];
        int currentIdx = maxLenIdx;
        for (int k = maxLen - 1; k >= 0; k--) { 
            
            // Guardamos el indice del contenedor en la solucion
            mejorSolucion[k] = contenedoresOrdenados[currentIdx].getIndice();

            // Avanzamos al contenedor previo en la secuencia
            currentIdx = Previo[currentIdx];
        }
    }
    
}