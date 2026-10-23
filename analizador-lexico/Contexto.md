# Analizador Léxico de OCL con Lex (Lenguajes de Programación)

## Contexto del Proyecto
Este proyecto fue desarrollado en equipo como parte de la asignatura de **Lenguajes de Programación (LP)** 

- **Autores:**
  * Miguel Bueno Gonzalez
  * Mario Gonzalez Liras
  * Paula Negro Asegurado
  * Emily Fabiana Rodrigues Álvarez

---

## Descripción de la Práctica
El objetivo principal fue desarrollar un **analizador léxico utilizando Lex/Flex y C** capaz de reconocer los elementos del léxico de las restricciones **OCL** (*Object Constraint Language*) bajo una versión simplificada, reportando errores léxicos con indicación de fila y columna en caso de encontrar anomalías.

El desarrollo abarcó los siguientes pilares técnicos:

### 1. Análisis Léxico y Gestión de Estados
- **Definiciones y Expresiones Regulares:** Categorización precisa de palabras reservadas, símbolos y tipos de elementos del lenguaje OCL.
- **Control por Estados:** Utilización de 3 estados diferentes en Lex para diferenciar el contexto de las declaraciones, las operaciones sobre colecciones y las operaciones específicas de cada clase.

### 2. Estructura de Datos y Procesamiento
- **Estructuras Dinámicas (`struct`):** Implementación de estructuras en C para almacenar de forma organizada las clases detectadas en un archivo OCL, junto con el recuento de sus invariantes y operaciones.
- **Gestión de Memoria:** Creación de funciones auxiliares dedicadas a la verificación de existencia previa de clases y a la redimensión dinámica de arrays en memoria.
- **Resumen Estadístico:** Ejecución del análisis mediante `yylex()` y generación final de un informe con el resumen de clases, invariantes, operaciones y colecciones utilizadas.

---

## Tecnologías y Conceptos Aplicados
- **Lenguajes:** C, Lex/Flex.
- **Automatización de Compilación:** Make / Makefile.
- **Entorno de Desarrollo:** Linux / Unix.
- **Control de Versiones:** Git, GitLab (Inf-UVa).

---

## Estructura y Uso del Repositorio
- `src/`: Contiene el código fuente del analizador (`.l`) y el archivo `Makefile` para la compilación del programa ejecutable.
  * **Compilación:** Navegar a la carpeta `src` y ejecutar `make` (en caso de conflictos, realizar primero `make clean` y a continuación `make`).
  * **Ejecución:** `./analizador < fichero-ocl`
- `inputs/`: Contiene los archivos de prueba para validar el correcto funcionamiento del analizador, incluyendo ejemplos basados en las diapositivas de la asignatura (`aeropuerto.txt`), pruebas iniciales de validación (`entrada.txt`) y casos proporcionados en el campus virtual.