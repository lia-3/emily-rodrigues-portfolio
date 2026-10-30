# Análisis Empírico de Algoritmos de Ordenación

## Contexto del Proyecto
Este proyecto fue desarrollado en equipo como parte de la asignatura de **Análisis y Diseño de Algoritmos (ADA)** 

- **Autores:**
  * Marta Pérez Alonso
  * Emily Fabiana Rodrigues Álvarez

---

## Descripción de la Práctica
El objetivo principal fue realizar un **análisis empírico y comparativo de eficiencia** de tres algoritmos de ordenación (`ordena1` - Inserción, `ordena2` - QuickSort con primer pivote, `ordena3` - QuickSort con pivote aleatorio) evaluados bajo dos patrones de generación de datos aleatorios (`aleatorio1` y `aleatorio2`)

El desarrollo y la metodología experimental se estructuraron en torno a los siguientes pilares técnicos:

### 1. Entorno de Desarrollo y Control Metodológico
- **Desactivación de JIT:** Uso obligatorio de la bandera `-Xint` en la JVM para anular la compilación en tiempo de ejecución y evitar optimizaciones dinámicas que alterasen las mediciones temporales
- **Gestión de Memoria y Pila:** Ampliación del tamaño de la pila (`-Xss16m`) para prevenir *StackOverflowError* debido a las llamadas recursivas profundas en la ordenación rápida, junto con invocaciones explícitas al recolector de basura (`System.gc()`) antes de cada toma de tiempos
- **Medición de Alta Precisión:** Captura temporal precisa en nanosegundos utilizando `Instant` y `Duration` de Java (convertidos a microsegundos, $\mu s$), además del conteo exacto de operaciones elementales de comparación y asignación sobre los elementos del vector

### 2. Diseño Experimental y Parámetros de Prueba
- **Tamaños Evaluados ($n$):** Evaluación progresiva desde $10.000$ hasta $100.000$ elementos con pasos de $10.000$
- **Repetición Estadística:** Ejecución de $20$ iteraciones independientes por cada tamaño de vector con reinicialización de datos para obtener promedios aritméticos estables
- **Algoritmos de Relleno:**
  * `aleatorio1`: Generación de vectores totalmente desordenados mediante permutación aleatoria
  * `aleatorio2`: Generación de vectores casi ordenados restringiendo el desplazamiento máximo ($k = 20$)
---

## Tecnologías y Conceptos Aplicados
- **Lenguaje:** Java
- **Automatización & Compilación:** `Makefile` configurado con banderas específicas de la JVM (`-Xint`, `-Xss16m`)
- **Análisis de Datos:** Excel / LibreOffice Calc (Ajuste de curvas por coeficientes de variación $CV\%$, análisis asintótico $\Theta(n)$, $\Theta(n \log n)$, $\Theta(n^2)$)
- **Control de Versiones:** Git.

---

## Estructura y Uso del Repositorio
- `src/`: Código fuente de los algoritmos de ordenación, funciones de generación aleatoria y lógica de medición.
- `csv/`: Archivos de datos generados automáticamente con los promedios numéricos por tamaño (tiempos, asignaciones y comparaciones)
- `Makefile`: Script para automatizar la compilación, ejecución sin JIT y limpieza del entorno
  * **Compilar y Ejecutar:**
    ```bash
    make run
    ```
  * **Limpiar binarios y temporales:**
    ```bash
    make clean
    ```