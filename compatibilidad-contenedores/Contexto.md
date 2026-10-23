# Compatibilidad de Contenedores en Cadenas de Suministro

## Contexto del Proyecto
Este proyecto fue desarrollado en equipo como parte de la asignatura de **Análisis y Diseño de Algoritmos (ADA)** 

- **Autores:**
  * Marta Pérez Alonso
  * Emily Fabiana Rodrigues Álvarez

---

## Descripción de la Práctica
El objetivo principal fue resolver el **problema de la secuencia máxima de contenedores compatibles** en una cadena de suministro, evaluando y comparando tres técnicas de diseño algorítmico: **Fuerza Bruta**, **Algoritmo Voraz** y **Programación Dinámica**

El desarrollo y el diseño del marco experimental abarcaron los siguientes pilares técnicos:

### 1. Arquitectura Modular del Sistema
- **Polimorfismo e Interfaz Común (`IMedicion`):** Implementación de una interfaz unificada para la invocación y toma de métricas estandarizadas en las clases `FuerzaBruta`, `Voraz` y `ProgramacionDinamica`
- **Modelo de Dominio (`Contenedor`):** Clase encargada de almacenar las dimensiones/atributos del contenedor y ejecutar la lógica de verificación de compatibilidad (`esCompatibleCon`), aplicando una reordenación previa de atributos

### 2. Rangos Experimentales y Restricciones de Escala
- **Fuerza Bruta ($O(k!)$):** Restricción estricta de la escala de prueba a un rango de contenedores $k \in [1, 10]$ y atributos $n \in \{3, 10, 30\}$, dada la explosión combinatoria del *backtracking*
- **Programación Dinámica ($O(k^2 \cdot n)$ / $O(k \log k)$ empírico) y Voraz ($O(k \log k + k \cdot n)$):** Escalado masivo hasta $k = 1000$ contenedores ($k \in [10, 1000]$) y variaciones en la dimensión de atributos $n \in \{3, 10, 30, 50, 100\}$

### 3. Rigor Metodológico y Control del Entorno
- **Control de Ejecución JVM:** Ejecución con la máquina virtual configurada con la bandera `-Xint` (JIT desactivado) e invocación explícita del recolector de basura (`System.gc()`) antes de cada bloque de pruebas
- **Estabilidad Estadística:** Promedio aritmético sobre $20$ ejecuciones independientes por cada configuración de $(k, n)$ almacenadas en CSVs jerárquicos

---

## Tecnologías y Conceptos Aplicados
- **Lenguaje:** Java
- **Técnicas Algorítmicas:** *Backtracking* / Fuerza Bruta, Estrategias Voraces (Heurística de ordenación por suma de atributos), Programación Dinámica (Ecuaciones de recurrencia y reconstrucción de secuencia con tabla de predecesores)
- **Análisis de Complejidad:** Estudio del impacto del número de atributos ($n$) frente al número de contenedores ($k$)[
- **Control de Versiones:** Git.

---

## Estructura y Uso del Repositorio
- `src/`: Código fuente de la arquitectura modular (`Contenedor.java`, `IMedicion.java`, `FuerzaBruta.java`, `Voraz.java`, `ProgramacionDinamica.java`, `Main.java`)
- `inputs/`: Organización jerárquica de archivos de prueba:
  * `analisis/`: Casos divididos en subdirectorios por algoritmo, número de atributos ($n$) y cantidad de contenedores ($k$)
  * `pruebas/`: Ejemplos para validación de formato (ej. `ejemplo_campus.txt`, `entrada.txt`)
- `csv/`: Salida automatizada con las mediciones de tiempos de ejecución

### Ejecución
Compilar el código fuente e invocar la clase principal desactivando el JIT:
```bash
javac -d bin src/*.java
java -Xint -cp bin Main inputs/pruebas/ejemplo_campus.txt