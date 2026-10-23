import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Locale;
import java.util.Scanner;

/**
 * Programa principal para resolver el problema de compatibilidad de
 * contenedores.
 * Permite ejecutar algoritmos con entrada de fichero o realizar análisis
 * empírico.
 * * @author Grupo 31
 */
public class Main {

    static Contenedor[] contenedores;
    static Metricas metricas = new Metricas();

    static final int REPETICIONES_POR_FICHERO = 20;

    public static void main(String[] args) {

        Locale.setDefault(Locale.US);
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n------------------- COMPATIBILIDAD DE CONTENEDORES -------------------");

        while (true) {
            System.out.println("\n===== MENU =====");
            System.out.println("1) Ejecutar algoritmo con fichero individual");
            System.out.println("2) Realizar análisis experimental (CSV desde ficheros)");
            System.out.println("3) Salir");
            System.out.print("Elige una opción: ");

            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1":
                    ejecutarDesdeFichero(scanner);
                    break;
                case "2":
                    menuAnalisis(scanner);
                    break;
                case "3":
                    System.out.println("Saliendo...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Opción no válida.");
            }
        }
    }

    /*
     * =====================================================
     * OPCIÓN 1 → Ejecutar algoritmo con entrada de fichero
     * =====================================================
     */
    static void ejecutarDesdeFichero(Scanner scanner) {

        System.out.print("\nIntroduce el nombre del archivo de entrada (ej: entrada.txt).\n");
        System.out.print("El archivo DEBE estar ubicado en la carpeta 'inputs/', en cualquiera de sus susbcarpetas.");
        System.out.println("Opciones: ");
        System.out.println("\t - '../inputs/analisis/': ficheros usados análisis experimental de Voraz y Prog. Dinámica. Está subdividida en n3, n10, n30, n50, n100.");
        System.out.println("\t\t * '../inputs/analisis/n3/'");
        System.out.println("\t\t * '../inputs/analisis/n10/'");
        System.out.println("\t\t * '../inputs/analisis/n30/'");
        System.out.println("\t\t * '../inputs/analisis/n50/'");
        System.out.println("\t\t * '../inputs/analisis/n100/'");
        System.out.println("\t - '../inputs/analisis/fb/': ficheros para análisis experimental de Fuerza Bruta (K <= 12). Está subdividida en n3, n10, n30, n50, n100.");
        System.out.println("\t\t * '../inputs/analisis/fb/n3/'");
        System.out.println("\t\t * '../inputs/analisis/fb/n10/'");
        System.out.println("\t\t * '../inputs/analisis/fb/n30/'");
        System.out.println("\t\t * '../inputs/analisis/fb/n50/'");
        System.out.println("\t\t * '../inputs/analisis/fb/n100/'");
        System.out.println("\t - '../inputs/pruebas/': fichero del ejemplo del campus y la copia del mismo adapatado para fuerza bruta.");
        System.out.print("Nombre del archivo: ");
        String nombreArchivo = scanner.nextLine().trim();

        contenedores = leerContenedores(nombreArchivo);

        if (contenedores == null) {
            System.err.println("No se pudo cargar el archivo: " + nombreArchivo);
            return;
        }

        System.out.print("\nSeleccione algoritmo - Fuerza Bruta (F), Voraz (V), Prog. Dinamica (P): ");
        String opcionAlgoritmo = scanner.nextLine().toUpperCase().trim();

        // DISCLAIMER PARA FUERZA BRUTA
        if (opcionAlgoritmo.equals("F") && contenedores.length > 13) {
            System.out.println("\n\n═══════════════════════════════════════════════════════════════");
            System.out.println("\t\t\tADVERTENCIA");
            System.out.println("═══════════════════════════════════════════════════════════════");
            System.out.println("El archivo contiene " + contenedores.length + " contenedores.");
            System.out.println("Fuerza Bruta con más de 13 contenedores puede tardar HORAS.");
            System.out.println("Se recomienda usar Voraz (V) o Programación Dinámica (P).");
            System.out.println("═══════════════════════════════════════════════════════════════");
            System.out.print("¿Deseas continuar de todas formas? (S/N): ");
            
            String confirmacion = scanner.nextLine().toUpperCase().trim();
            System.out.println("\n");
            if (!confirmacion.equals("S")) {
                System.out.println("Operación cancelada. Volviendo al menú principal...");
                return;
            }
            System.out.println("Continuando con Fuerza Bruta... Esto puede tardar mucho tiempo.");
        }

        IMedicion algoritmo; 
        try {
            algoritmo = crearAlgoritmo(opcionAlgoritmo);
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
            return;
        }

        System.out.println("\n========== RESOLVIENDO " + nombreArchivo.toUpperCase() + " ==========");
        long sumaTiempo = 0;

        int repeticiones = contenedores.length <= 15 ? 1 : REPETICIONES_POR_FICHERO;
        System.out.println("Ejecutando " + repeticiones + " veces para medir tiempo promedio...");

        for (int r = 0; r < repeticiones; r++) {
            algoritmo = crearAlgoritmo(opcionAlgoritmo);
            System.gc(); 
            Instant inicio = Instant.now();
            algoritmo.ejecutaAlgoritmo(contenedores);
            Instant fin = Instant.now();

            sumaTiempo += Duration.between(inicio, fin).toNanos();
        }

        long tiempoPromedio = sumaTiempo / repeticiones;

        int[] solucion = algoritmo.getIndicesSolucion(contenedores);

        if (solucion == null) {
            System.err.println("El algoritmo devolvió null. Algo salió mal. Verifica la implementación del algoritmo.");
            return;
        }

        System.out.println("\n----- SOLUCIÓN -----");
        System.out.println(solucion.length);
        for (int i = 0; i < solucion.length; i++) {
            System.out.print(solucion[i] + (i < solucion.length - 1 ? " " : ""));
        }
        System.out.println("\n");
        System.out
                .println("Tiempo de ejecución: " + tiempoPromedio + " ns (Promedio de " + repeticiones + " ejecuciones)");
    }

    /*
     * =====================================================
     * OPCIÓN 2 → Análisis experimental
     * =====================================================
     */
    static void menuAnalisis(Scanner scanner) {

        System.out.print("\nSeleccione algoritmo - Fuerza Bruta (F), Voraz (V), Prog. Dinamica (P): ");
        String opcionAlg = scanner.nextLine().toUpperCase();

        System.out.println("\n========== GENERANDO CSV ==========");
        generarCSVDesdeFicheros(opcionAlg);
    }

    /*
     * ===============================================
     *                  METRICAS
     * ===============================================
     */
    static class Metricas {
        long sumaTiempo = 0;

        void reiniciar() {
            sumaTiempo = 0;
        }
    }

    

    /*
     * ===============================================
     *          GENERACION DE CSV DESDE FICHEROS
     * ===============================================
     */
    static void generarCSVDesdeFicheros(String opcionAlgoritmo) {
        // Crear carpeta 'csv' si no existe
        File directorioCSV = new File("../csv");
        if (!directorioCSV.exists()) {
            directorioCSV.mkdirs();
        }

        // Definir las carpetas n[i] según el algoritmo
        String[] carpetasN;
        String directorioBase;
        
        if (opcionAlgoritmo.equals("F")) {
            // Para Fuerza Bruta: carpetas dentro de ../inputs/analisis/fb/
            directorioBase = "../inputs/analisis/fb/";
            carpetasN = new String[]{"n3", "n10", "n30", "n50", "n100"};
            System.out.println("Usando directorio para Fuerza Bruta: " + directorioBase);
        } else {
            // Para Voraz y Programación Dinámica: carpetas dentro de ../inputs/analisis/
            directorioBase = "../inputs/analisis/";
            carpetasN = new String[]{"n3", "n10", "n30", "n50", "n100"};
            System.out.println("Usando directorio: " + directorioBase);
        }

        System.out.println("\nSe procesarán " + carpetasN.length + " carpetas n[i]");
        System.out.println("═══════════════════════════════════════════════════════════════\n");

        int csvGenerados = 0;

        // Procesar cada carpeta n[i]
        for (String nombreCarpeta : carpetasN) {
            File carpeta = new File(directorioBase + nombreCarpeta);
            
            // Verificar si la carpeta existe
            if (!carpeta.exists() || !carpeta.isDirectory()) {
                System.out.println("Carpeta no encontrada: " + directorioBase + nombreCarpeta + " - Saltando...\n");
                continue;
            }

            int numAtributos = Integer.parseInt(nombreCarpeta.substring(1));
            System.out.println("Procesando carpeta: " + nombreCarpeta + " (n=" + numAtributos + " atributos)");

            // Generar nombre del CSV para esta carpeta
            String archivoCSV = "../csv/algoritmo-" + opcionAlgoritmo.toLowerCase() + "_" + nombreCarpeta + ".csv";

            procesarCarpetaYGenerarCSV(carpeta, archivoCSV, opcionAlgoritmo);
            
            System.out.println("CSV generado: " + archivoCSV);
            System.out.println("───────────────────────────────────────────────────────────────\n");
            csvGenerados++;
        }

        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("Análisis completo. Se generaron " + csvGenerados + " archivos CSV.");
        System.out.println("═══════════════════════════════════════════════════════════════\n");
    }

    /**
     * Procesa todos los ficheros de una carpeta n[i] y genera su CSV correspondiente
     */
    static void procesarCarpetaYGenerarCSV(File carpeta, String archivoCSV, String opcionAlgoritmo) {
        try {
            FileWriter escritorCSV = new FileWriter(archivoCSV);
            escritorCSV.write("NumContenedores;Tiempo_Promedio(nano_s)\n");

            // Obtener todos los ficheros .txt de esta carpeta
            File[] ficheros = carpeta.listFiles((dir, name) -> name.endsWith(".txt"));

            if (ficheros == null || ficheros.length == 0) {
                System.err.println("No se encontraron ficheros .txt en " + carpeta.getName());
                escritorCSV.close();
                return;
            }

            // Ordenar ficheros por número de contenedores
            java.util.Arrays.sort(ficheros, (f1, f2) -> {
                int k1 = extraerNumeroContenedores(f1.getName());
                int k2 = extraerNumeroContenedores(f2.getName());
                return Integer.compare(k1, k2);
            });

            System.out.println("  Ficheros encontrados: " + ficheros.length);
            System.out.print("  Progreso: ");

            // Procesar cada fichero
            for (File fichero : ficheros) {
                metricas.reiniciar();

                // Leer contenedores del fichero
                Contenedor[] contenedoresFichero = leerContenedoresDirecto(fichero);

                if (contenedoresFichero == null) {
                    System.err.println("\n  Error al leer: " + fichero.getName() + " - Saltando...");
                    continue;
                }

                int numContenedores = contenedoresFichero.length;

                // Realizar 15 repeticiones por fichero (como en REPETICIONES_POR_FICHERO)
                for (int rep = 0; rep < REPETICIONES_POR_FICHERO; rep++) {
                    IMedicion medicion = crearAlgoritmo(opcionAlgoritmo);

                    System.gc();
                    Instant inicioTiempo = Instant.now();
                    medicion.ejecutaAlgoritmo(contenedoresFichero);
                    Instant finTiempo = Instant.now();
                    metricas.sumaTiempo += Duration.between(inicioTiempo, finTiempo).toNanos();
                }

                // Calcular el promedio
                double promedio = (double) metricas.sumaTiempo / REPETICIONES_POR_FICHERO;

                // Escribir en CSV
                String valorFormateado = String.format("%.6f", promedio);
                escritorCSV.write(numContenedores + ";" + valorFormateado + "\n");
            }

            escritorCSV.close();

        } catch (IOException e) {
            System.err.println("  Error al escribir el archivo CSV: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("  Error general durante la generación del CSV: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /*
     * ===============================================
     *           FUNCIONES AUXILIARES
     * ===============================================
     */

    /**
     * Crea una instancia del algoritmo según la opción seleccionada.
     */
    static IMedicion crearAlgoritmo(String opcion) {
        switch (opcion) {
            case "F":
                return new FuerzaBruta();
            case "V":
                return new Voraz();
            case "P":
                return new ProgramacionDinamica();
            default:
                throw new IllegalArgumentException("Algoritmo invalido: " + opcion + ". Opciones validas: F, V, P.");
        }
    }

    /**
     * Lee contenedores desde un archivo. Prioriza la búsqueda en la carpeta
     * 'inputs/'.
     */
    public static Contenedor[] leerContenedores(String nombreArchivo) {
        Contenedor[] contenedores = null;
        String[] posiblesRutas = {
                "../inputs/analisis/n3/" + nombreArchivo, 
                "../inputs/analisis/n10/" + nombreArchivo, 
                "../inputs/analisis/n30/" + nombreArchivo, 
                "../inputs/analisis/n50/" + nombreArchivo, 
                "../inputs/analisis/n100/" + nombreArchivo,
                "../inputs/analisis/fb/n3/" + nombreArchivo, 
                "../inputs/analisis/fb/n10/" + nombreArchivo, 
                "../inputs/analisis/fb/n30/" + nombreArchivo, 
                "../inputs/analisis/fb/n50/" + nombreArchivo,  
                "../inputs/analisis/fb/n100/" + nombreArchivo, 
                "../inputs/pruebas/" + nombreArchivo,  
        };

        File archivo = null;
        String rutaEncontrada = null;

        for (String ruta : posiblesRutas) {
            File f = new File(ruta);
            if (f.exists() && !f.isDirectory()) {
                archivo = f;
                rutaEncontrada = ruta;
                break;
            }
        }

        if (archivo == null) {
            System.err.println(
                    "Error: No se encontro el archivo '" + nombreArchivo + "' en ninguna de las rutas esperadas.");
            return null;
        }

        try (Scanner scannerArchivo = new Scanner(archivo)) {

            if (!scannerArchivo.hasNextInt()) {
                System.err.println("Error de formato: El archivo debe comenzar con el número K de contenedores.");
                return null;
            }
            int k = scannerArchivo.nextInt();

            if (!scannerArchivo.hasNextInt()) {
                System.err.println("Error de formato: Falta el número N de atributos después de K.");
                return null;
            }
            int n = scannerArchivo.nextInt();

            contenedores = new Contenedor[k];
            for (int i = 0; i < k; i++) {
                int[] atributos = new int[n];
                for (int j = 0; j < n; j++) {
                    if (!scannerArchivo.hasNextInt()) {
                        System.err.println("Error de formato: Faltan valores de atributos en el contenedor " + (i + 1));
                        return null;
                    }
                    atributos[j] = scannerArchivo.nextInt();
                }
                contenedores[i] = new Contenedor(i + 1, atributos);
            }

            System.out.println(
                    "Archivo leido con éxito: " + rutaEncontrada + " (" + k + " contenedores, " + n + " atributos)");

        } catch (FileNotFoundException e) {
            System.err.println("Error al acceder al archivo: " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.err.println("Error general al leer el archivo: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
        return contenedores;
    }

    /**
     * Lee contenedores directamente desde un objeto File.
     */
    private static Contenedor[] leerContenedoresDirecto(File archivo) {
        Contenedor[] contenedores = null;

        try (Scanner scannerArchivo = new Scanner(archivo)) {

            if (!scannerArchivo.hasNextInt()) {
                System.err.println("Error de formato: El archivo debe comenzar con el número K de contenedores.");
                return null;
            }
            int k = scannerArchivo.nextInt();

            if (!scannerArchivo.hasNextInt()) {
                System.err.println("Error de formato: Falta el número N de atributos después de K.");
                return null;
            }
            int n = scannerArchivo.nextInt();

            contenedores = new Contenedor[k];
            for (int i = 0; i < k; i++) {
                int[] atributos = new int[n];
                for (int j = 0; j < n; j++) {
                    if (!scannerArchivo.hasNextInt()) {
                        System.err.println("Error de formato: Faltan valores de atributos en el contenedor " + (i + 1));
                        return null;
                    }
                    atributos[j] = scannerArchivo.nextInt();
                }
                contenedores[i] = new Contenedor(i + 1, atributos);
            }

            System.out
                    .println("Archivo leído: " + archivo.getName() + " (" + k + " contenedores, " + n + " atributos)");

        } catch (FileNotFoundException e) {
            System.err.println("Error al acceder al archivo: " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.err.println("Error general al leer el archivo: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
        return contenedores;
    }

    /**
     * Intenta extraer el número de contenedores del nombre del archivo.
     */
    private static int extraerNumeroContenedores(String nombreArchivo) {
        // Buscar el patrón _k seguido de números
        int indiceK = nombreArchivo.indexOf("_k");
        
        if (indiceK != -1) {
            // Extraer desde después de "_k"
            String despuesDeK = nombreArchivo.substring(indiceK + 2);
            
            // Extraer solo los dígitos consecutivos
            StringBuilder numeroStr = new StringBuilder();
            for (char c : despuesDeK.toCharArray()) {
                if (Character.isDigit(c)) {
                    numeroStr.append(c);
                } else {
                    break; // Detenerse al encontrar un carácter no numérico
                }
            }
            
            try {
                return Integer.parseInt(numeroStr.toString());
            } catch (NumberFormatException e) {
                System.err.println("Advertencia: No se pudo extraer K de " + nombreArchivo);
                return 0;
            }
        }
        
        // Fallback: extraer el primer número del archivo
        String numeroStr = nombreArchivo.replaceAll("[^0-9]", " ").trim();
        if (numeroStr.contains(" ")) {
            numeroStr = numeroStr.substring(0, numeroStr.indexOf(' '));
        }
        
        try {
            return Integer.parseInt(numeroStr);
        } catch (NumberFormatException e) {
            System.err.println("Advertencia: No se pudo extraer número de " + nombreArchivo);
            return 0;
        }
    }
}