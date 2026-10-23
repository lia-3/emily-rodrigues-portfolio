import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;


/**
 * Clase principal para el analisis empirico de algoritmos de ordenacion.
 * 
 * @author Grupo 31
 * @author Marta Perez Alonso
 * @author Emily Fabiana Rodrigues Alvarez
 */
public class Main {
    
     static final int VALOR_K = 20;
     static final int TAMANNO_INICIAL = 10000;
     static final int TAMANNO_FINAL = 100000;
     static final int INCREMENTO = 10000;
     static final int REPETICIONES = 20;

    static Scanner scanner;

    static double promedio;

    static int vector[];
    static int tamanno;
    
    static String archivoCSV;
    static String promedioFormateado ;
    
    static short tipoRelleno;
    static short tipoOrdenacion;

    static Metricas metricas = new Metricas();
    static TipoAnalisis tipoAnalisisEnum;

    static Instant inicioTiempo;
    static Instant finTiempo;

    static FileWriter escritorCSV;


    /* 
     * %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
     * -------------------------------------------------------------------------------------------------------------------
     *                                                       Enums
     * -------------------------------------------------------------------------------------------------------------------
     * %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    */


    /**
     * Tipo de analisis que se puede realizar sobre los algoritmos de ordenacion.
     *
     * Valores:
     *      TIEMPO: mide el tiempo de ejecucion (microsegundos).
     *      ASIGNACIONES: cuenta el numero de asignaciones realizadas.
     *      COMPARACIONES: cuenta el numero de comparaciones realizadas.
     *
     * Se usan metodos auxiliares para convertir representaciones externas en este enum.
     */
    enum TipoAnalisis {
        TIEMPO('T'), 
        ASIGNACIONES('A'), 
        COMPARACIONES('C');
        
        final char codigo;
        

        /**
         * Constructor del enum TipoAnalisis.
         * @param codigo caracter que identifica el tipo de analisis.
         */
        TipoAnalisis(char codigo) { 
            this.codigo = codigo; 
        }
        

        /**
         * Convierte un caracter que representa el tipo de analisis en el enum
         * correspondiente.
         *
         * @param caracter caracter que identifica el tipo 'T' (tiempo), 'A' (asignaciones) o 'C'(comparaciones)
         * @return el TipoAnalisis correspondiente al caracter
         * @throws IllegalArgumentException si el caracter no corresponde a ningun tipo
         */
        public static TipoAnalisis fromChar(char caracter) {
            for (TipoAnalisis tipo : values()) {
                if (tipo.codigo == caracter) return tipo;
            }
            throw new IllegalArgumentException("Tipo de analisis invalido: " + caracter);
        }
        

        /**
         * Convierte una cadena de longitud 1 en el TipoAnalisis correspondiente.
         *
         * @param cadena que contiene un unico caracter representando el tipo
         * @return el TipoAnalisis correspondiente
         * @throws IllegalArgumentException si la cadena es {@code null}, no tiene
         *         longitud 1 o el caracter no es valido
         */
        public static TipoAnalisis fromString(String cadena) {
            if (cadena == null || cadena.length() != 1) {
                throw new IllegalArgumentException("String invalido para tipo de analisis: " + cadena);
            }
            return fromChar(cadena.charAt(0));
        }
    }


    /**
     * Tipos de relleno que pueden usarse para generar los vectores de prueba.
     *
     * Actualmente existen dos estrategias:
     *      ALEATORIO_1
     *      ALEATORIO_2 
     */
    enum TipoRelleno {
        ALEATORIO_1(1), 
        ALEATORIO_2(2);
        
        final int codigo;


        /**
         * Constructor del enum TipoRelleno.
         * @param codigo entero que identifica el tipo de relleno (1 o 2).
         */
        TipoRelleno(int codigo) { 
            this.codigo = codigo; 
        }
    }


    /**
     * Representa los algoritmos de ordenacion disponibles en el programa.
     *
     * Se identifica cada algoritmo por un codigo entero (1, 2 o 3) que
     * el usuario introduce por la entrada estandar.
     */
    enum TipoOrdenacion {
        ORDENACION_UNO(1), 
        ORDENACION_DOS(2), 
        ORDENACION_TREs(3);
        
        final int codigo;


        /**
         * Constructor del enum TipoOrdenacion.
         * @param codigo
         */
        TipoOrdenacion(int codigo) { 
            this.codigo = codigo; 
        }
    }


    /* 
     * %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
     * -------------------------------------------------------------------------------------------------------------------
     *                                                     Metricas
     * -------------------------------------------------------------------------------------------------------------------
     * %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    */


    /**
     * Contenedor simple para acumular las metricas medidas durante las repeticiones.
     *
     * Campos usados desde los metodos de ordenacion:
     *   sumaTiempo: suma de tiempos medidos en microsegundos.
     *   sumaAsignaciones: contador acumulado de asignaciones.
     *   sumaComparaciones: contador acumulado de comparaciones.
     */
    static class Metricas {
        long sumaTiempo = 0;
        long sumaAsignaciones = 0;
        long sumaComparaciones = 0;
        
        /**
         * Reinicia todas las metricas a cero.
         */
        void reiniciar() {
            sumaTiempo = 0;
            sumaAsignaciones = 0;
            sumaComparaciones = 0;
        }
    }

    

    /* 
     * %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
     * -------------------------------------------------------------------------------------------------------------------
     *                                                 Formas de rellenado 
     * -------------------------------------------------------------------------------------------------------------------
     * %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    */
    static void aleatorio1(int v[], int tam) {
        int i;
        for (i = 0; i < tam; i++) {
            v[i] = i;
        }
        for (i = tam - 1; i > 0; i--) {
            Random random = new Random();
            int j = random.nextInt(i + 1);
            int temp = v[i];
            v[i] = v[j];
            v[j] = temp;
        }
    }

    static void aleatorio2(int v[], int tam, int k) {
        int i;
        for (i = 0; i < tam; i++) {
            v[i] = i;
        }
        for (i = 0; i < tam; i++) {
            int l = Math.max(0, i - k);
            int u = Math.min(tam - 1, i + k);
            Random random = new Random();
            int j = random.nextInt(u - l + 1) + l;
            int tmp = v[i];
            v[i] = v[j];
            v[j] = tmp;
        }
    }
    

    /* 
     * %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
     * -------------------------------------------------------------------------------------------------------------------
     *                                              Algoritmo Ordenacion 1
     * -------------------------------------------------------------------------------------------------------------------
     * %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    */

    static void ordena1Asignaciones(int v[], int tam) {
        for (int i = 1; i < tam; i++) {
            int tmp = v[i];
            metricas.sumaAsignaciones++;
            int j = i - 1;
            while (j >= 0 && tmp < v[j]) {
                v[j + 1] = v[j];
                metricas.sumaAsignaciones++;
                j--;
            }
            v[j + 1] = tmp;
            metricas.sumaAsignaciones++;
        }
    }


    static void ordena1Comparaciones(int v[], int tam) {
        for (int i = 1; i < tam; i++) {
            int tmp = v[i];
            int j = i - 1;
            while (j >= 0 && tmp < v[j]) {
                metricas.sumaComparaciones++; 
                v[j + 1] = v[j];
                j--;
            }
            if (j >= 0) {
                metricas.sumaComparaciones++; 
            }
            v[j + 1] = tmp;
        }
    }


    static void ordena1Tiempo(int v[], int tam) {
        for (int i = 1; i < tam; i++) {
            int tmp = v[i];
            int j = i - 1;
            while (j >= 0 && tmp < v[j]) {
                v[j + 1] = v[j];
                j--;
            }
            v[j + 1] = tmp;
        }
    }


    /* 
     * %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
     * -------------------------------------------------------------------------------------------------------------------
     *                                              Algoritmo Ordenacion 2
     * -------------------------------------------------------------------------------------------------------------------
     * %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    */

    static void ordena2Asignaciones(int v[], int tam) {
        ordena2recAsignaciones(v, 0, tam - 1);
    }
    
    static void ordena2recAsignaciones(int v[], int l, int r) {
        if (l < r) {
            int i = l;
            int j = r;
            int p = v[i];
            while (i <= j) {
                while (v[i] < p) {
                    ++i;
                }
                while (p < v[j]) {
                    --j;
                }
                if (i <= j) {
                    int tmp = v[i];
                    metricas.sumaAsignaciones++;
                    v[i] = v[j];
                    metricas.sumaAsignaciones++;
                    v[j] = tmp;
                    metricas.sumaAsignaciones++;
                    ++i;
                    --j;
                }
            }
            ordena2recAsignaciones(v, l, i - 1);
            ordena2recAsignaciones(v, i, r);
        }
    }

    static void ordena2Comparaciones(int v[], int tam) {
        ordena2recComparaciones(v, 0, tam - 1);
    }
    
    static void ordena2recComparaciones(int v[], int l, int r) {
        if (l < r) {
            int i = l;
            int j = r;
            int p = v[i];
            while (i <= j) {
                while (v[i] < p) {
                    metricas.sumaComparaciones++;
                    ++i;
                } 
                metricas.sumaComparaciones++;
                while (p < v[j]) {
                    metricas.sumaComparaciones++;
                    --j;
                }
                metricas.sumaComparaciones++;
                if (i <= j) {
                    int tmp = v[i];
                    v[i] = v[j];
                    v[j] = tmp;
                    ++i;
                    --j;
                }
            }
            ordena2recComparaciones(v, l, i - 1);
            ordena2recComparaciones(v, i, r);
        }
    }

    static void ordena2Tiempo(int v[], int tam) {        
        ordena2recTiempo(v, 0, tam - 1);
    }
    
    static void ordena2recTiempo(int v[], int l, int r) {
        if (l < r) {
            int i = l;
            int j = r;
            int p = v[i];
            while (i <= j) {
                while (v[i] < p) {
                    ++i;
                }
                while (p < v[j]) {
                    --j;
                }
                if (i <= j) {
                    int tmp = v[i];
                    v[i] = v[j];
                    v[j] = tmp;
                    ++i;
                    --j;
                }
            }
            ordena2recTiempo(v, l, i - 1);
            ordena2recTiempo(v, i, r);
        }
    }

    /* 
     * %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
     * -------------------------------------------------------------------------------------------------------------------
     *                                              Algoritmo Ordenacion 3
     * -------------------------------------------------------------------------------------------------------------------
     * %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    */

    static void ordena3Asignaciones(int v[], int tam) {
        ordena3recAsignaciones(v, 0, tam - 1);
    }

    static void ordena3recAsignaciones(int v[], int l, int r) {
        if (l < r) {
            int i = l;
            int j = r;
            Random random = new Random();
            int k = i + random.nextInt(j - i + 1);
            int p = v[k];
            metricas.sumaAsignaciones++;
            while (i <= j) {
                while (v[i] < p) {
                    ++i;
                }
                while (p < v[j]) {
                    --j;
                }
                if (i <= j) {
                    int tmp = v[i];
                    metricas.sumaAsignaciones++;
                    v[i] = v[j];
                    metricas.sumaAsignaciones++;
                    v[j] = tmp;
                    metricas.sumaAsignaciones++;
                    ++i;
                    --j;
                }
            }
            ordena3recAsignaciones(v, l, i - 1);
            ordena3recAsignaciones(v, i, r);
        }
    }

    static void ordena3Comparaciones(int v[], int tam) {
        ordena3recComparaciones(v, 0, tam - 1);
    }

    static void ordena3recComparaciones(int v[], int l, int r) {
        if (l < r) {
            int i = l;
            int j = r;
            Random random = new Random();
            int k = i + random.nextInt(j - i + 1);
            int p = v[k];
            while (i <= j) {
                while (v[i] < p) {
                    metricas.sumaComparaciones++;
                    ++i;
                }
                metricas.sumaComparaciones++;
                while (p < v[j]) {
                    metricas.sumaComparaciones++;
                    --j;
                }
                metricas.sumaComparaciones++;
                if (i <= j) {
                    int tmp = v[i];
                    v[i] = v[j];
                    v[j] = tmp;
                    ++i;
                    --j;
                }
            }
            ordena3recComparaciones(v, l, i - 1);
            ordena3recComparaciones(v, i, r);
        }
    }

    static void ordena3Tiempo(int v[], int tam) {        
        ordena3recTiempo(v, 0, tam - 1);        
    }

    static void ordena3recTiempo(int v[], int l, int r) {
        if (l < r) {
            int i = l;
            int j = r;
            Random random = new Random();
            int k = i + random.nextInt(j - i + 1);
            int p = v[k];
            while (i <= j) {
                while (v[i] < p) {
                    ++i;
                }
                while (p < v[j]) {
                    --j;
                }
                if (i <= j) {
                    int tmp = v[i];
                    v[i] = v[j];
                    v[j] = tmp;
                    ++i;
                    --j;
                }
            }
            ordena3recTiempo(v, l, i - 1);
            ordena3recTiempo(v, i, r);
        }
    }

    

    /* 
     * %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
     * -------------------------------------------------------------------------------------------------------------------
     *                                                       main
     * -------------------------------------------------------------------------------------------------------------------
     * %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    */

    public static void main(String[] args) throws Exception {
        scanner = new Scanner(System.in);

        System.out.println("\n\n------------------- ANALISIS EMPIRICO DE ALGORITMOS DE ORDENACION -------------------");
        
        do { 
            System.out.print("Introduce tipo de algoritmo de ordenacion (Respuesta: 1/2/3): ");
            tipoOrdenacion = scanner.nextShort();
        } while (tipoOrdenacion < 1 || tipoOrdenacion > 3);

        do { 
            System.out.print("Introduce el algoritmo de relleno (1/2): ");
            tipoRelleno = scanner.nextShort();
        } while (tipoRelleno != 1 && tipoRelleno != 2);


        scanner.nextLine(); // Limpieza buffer

        tipoAnalisisEnum = solicitudTipoAnalisis();


        scanner.close();  // No se recibe nada mas por la entrada estandar


        archivoCSV = generaNombreArchivoCSV(tipoOrdenacion, tipoAnalisisEnum, tipoRelleno);

        escritorCSV = new FileWriter(archivoCSV);
        try {
            escribirCabeceraCSV(escritorCSV, tipoAnalisisEnum);
            
            for (tamanno = TAMANNO_INICIAL; tamanno <= TAMANNO_FINAL; tamanno += INCREMENTO) {
                // Nuevo tamanno de vector
                vector = new int[tamanno];
                metricas.reiniciar();
                

                for (int repeticion = 0; repeticion < REPETICIONES; repeticion++) {
                    // Nueva repeticion               
                    
                    if (tipoRelleno == 1) {
                        aleatorio1(vector, tamanno);
                    } else {
                        aleatorio2(vector, tamanno, VALOR_K);
                    }
                    
                    switch (tipoOrdenacion) {
                        case 1:
                            switch (tipoAnalisisEnum) {
                                case ASIGNACIONES:
                                    ordena1Asignaciones(vector, tamanno);
                                    break;
                                case COMPARACIONES:
                                    ordena1Comparaciones(vector, tamanno);
                                    break;
                                case TIEMPO:
                                    System.gc();
                                    inicioTiempo = Instant.now();
                                    ordena1Tiempo(vector, tamanno);
                                    finTiempo = Instant.now();
                                    metricas.sumaTiempo += Duration.between(inicioTiempo, finTiempo).toNanos() / 1000.0 ;
                                    break;
                            }
                            break;
                        case 2:
                            switch (tipoAnalisisEnum) {
                                case ASIGNACIONES:
                                    ordena2Asignaciones(vector, tamanno);
                                    break;
                                case COMPARACIONES:
                                    ordena2Comparaciones(vector, tamanno);
                                    break;
                                case TIEMPO:
                                    System.gc();
                                    inicioTiempo = Instant.now();
                                    ordena2Tiempo(vector, tamanno);
                                    finTiempo = Instant.now();
                                    metricas.sumaTiempo += Duration.between(inicioTiempo, finTiempo).toNanos() / 1000.0 ;
                                    break;
                            }
                            break;
                        default:
                            switch (tipoAnalisisEnum) {
                                case ASIGNACIONES:
                                    ordena3Asignaciones(vector, tamanno);
                                    break;
                                case COMPARACIONES:
                                    ordena3Comparaciones(vector, tamanno);
                                    break;
                                case TIEMPO:
                                    System.gc();
                                    inicioTiempo = Instant.now();
                                    ordena3Tiempo(vector, tamanno);
                                    finTiempo = Instant.now();
                                    metricas.sumaTiempo += Duration.between(inicioTiempo, finTiempo).toNanos() / 1000.0;
                                    break;
                            }
                            break;
                    }
                }    
                // Calcula promedio y lo escribe en el CSV, asi como por salida estandar
                promedio = (double) obtenerSumaAcumuladaAnalisis(tipoAnalisisEnum) / REPETICIONES;
                promedioFormateado = String.format(Locale.FRENCH, "%.6f", promedio);
                escritorCSV.write(tamanno + ";" + promedioFormateado + "\n");
                System.out.printf("Tamaño %d completado. Promedio: %f%n", tamanno, promedio);
            }
            
            System.out.println("Analisis completado. Resultados guardados en:\n--->" + archivoCSV);
            
        } catch (IOException e) {
            System.err.println("Error al escribir el archivo: " + e.getMessage());
        } finally {
            try {
                escritorCSV.close();
            } catch (IOException e) {
                System.err.println("Error al cerrar el archivo: " + e.getMessage());
            }
        }
    }

    /**
     * Solicita al usuario por consola el tipo de analisis a realizar.
     *
     * Acepta los valores A, C, T (mayusculas o minusculas) que corresponden a asignaciones,
     * comparaciones o tiempo respectivamente. Repite la solicitud hasta que
     * se reciba una entrada valida.
     *
     * @return el TipoAnalisis seleccionado por el usuario
     */
    static TipoAnalisis solicitudTipoAnalisis() {
        String entrada;
        do { 
            System.out.print("¿Haras un analisis de asignaciones, comparaciones o de tiempo? (Respuesta: A/C/T): ");
            entrada = scanner.nextLine().toUpperCase();
        } while (!entrada.matches("[ACT]"));
        
        return TipoAnalisis.fromString(entrada);
    }


    /**
     * Genera el nombre del archivo CSV donde se guardaran los resultados.
     *
     * @param ordenacion numero del algoritmo de ordenacion (1..3)
     * @param analisis tipo de analisis (TIEMPO/ASIGNACIONES/COMPARACIONES)
     * @param relleno tipo de relleno (1..2)
     * @return ruta relativa al CSV donde se escribiran los resultados
     */
    static String generaNombreArchivoCSV(int ordenacion, TipoAnalisis analisis, int relleno) {
        return "../csv/algoritmo-ordenacion" + ordenacion 
                + "_tipo-analisis" + analisis 
                + "_relleno" + relleno 
                + ".csv";
    }

    
    /**
     * Escribe la cabecera del CSV segun el tipo de analisis.
     *
     * @param escritorCSV writer abierto para el archivo CSV
     * @param tipo el tipo de analisis que determina las columnas de la cabecera
     * @throws IOException si ocurre un error de E/S al escribir
     */
    static void escribirCabeceraCSV(FileWriter escritorCSV, TipoAnalisis tipo) throws IOException {
        switch (tipo) {
            case TIEMPO:
                escritorCSV.write("Tamaño;Tiempo_Promedio(micro_s)\n");
                break;
            case ASIGNACIONES:
                escritorCSV.write("Tamaño;Asignaciones_Promedio\n");
                break;
            case COMPARACIONES:
                escritorCSV.write("Tamaño;Comparaciones_Promedio\n");
                break;
        }
    }


    /**
     * Devuelve la suma acumulada correspondiente al tipo de analisis solicitado, 
     * truncando los decimales al devolver la suma de tiempo.
     *
     * @param tipo el tipo de analisis
     * @return la suma acumulada (tiempo en microsegundos o contadores)
     */
    static long obtenerSumaAcumuladaAnalisis(TipoAnalisis tipo) {
        switch (tipo) {
            case TIEMPO:
                return metricas.sumaTiempo;
            case ASIGNACIONES:
                return metricas.sumaAsignaciones;
            case COMPARACIONES:
                return metricas.sumaComparaciones;
            default:
                return 0;
        }
    }
}