# Analizador Léxico y Sintáctico de OCL con Lex y Yacc 

## Contexto del Proyecto
Este proyecto fue desarrollado en equipo como parte de la asignatura de **Lenguajes de Programación (LP)**

- **Autores:**
  * Miguel Bueno Gonzalez
  * Mario Gonzalez Liras
  * Paula Negro Asegurado
  * Emily Fabiana Rodrigues Álvarez

---

## Descripción de la Práctica
El objetivo principal fue desarrollar un **analizador sintáctico y léxico utilizando Lex/Flex, Yacc/Bison y C** para procesar restricciones **OCL** (*Object Constraint Language* bajo el estándar 2.4), gestionando contextos de clases y operaciones, reportando errores sintácticos detallados y generando un informe estadístico final.

El desarrollo abarcó los siguientes pilares técnicos:

### 1. Análisis Léxico y Sintáctico
- **Definiciones y Expresiones Regulares:** Implementación en Lex de patrones específicos para palabras reservadas, operadores, tipos, valores lógicos y estructuras de contexto (`context`, `inv`, `pre`, `post`), asumiendo que las variables comienzan por letra siguiendo los convenios de la norma.
- **Gramática y Precedencias (Yacc/Bison):** Estructuración de reglas gramaticales para interpretar de forma precisa los contextos de clases, invariantes y especificaciones de operaciones sin incurrir en *hard-coding* innecesario de las operaciones no operadores[cite: 112].
- **Manejo de Errores:** Detección y reporte de errores sintácticos indicando línea y símbolo inesperado, acumulando incidencias hasta un límite definido mediante `MAX_SYNTAX_ERROR`.

### 2. Estructura de Datos y Procesamiento
- **Estructuras Dinámicas (`struct`):** Almacenamiento organizado de las clases detectadas, junto con el recuento de sus invariantes y operaciones asociadas.
- **Gestión de Colecciones y Memoria:** Registro del uso de operaciones sobre colecciones tanto en invariantes como en las especificaciones de operaciones, apoyado por funciones auxiliares en C para la reserva dinámica de memoria y la validación de registros previos.
- **Resumen Estadístico:** Generación automática de un informe estructurado al finalizar el análisis exitoso de la entrada (mostrando clases, cantidad de invariantes y uso de colecciones por operación).

---

## Tecnologías y Conceptos Aplicados
- **Lenguajes:** C, Lex/Flex, Yacc/Bison.
- **Automatización de Compilación:** Make / Makefile.
- **Entorno de Desarrollo:** Linux / Unix.
- **Control de Versiones:** Git, GitLab (Inf-UVa).

---

## Estructura y Uso del Repositorio
- `src/`: Contiene el código fuente del analizador (`.l`, `.y`) y el archivo `Makefile` para la compilación del programa ejecutable.
  * **Compilación:** Navegar a la carpeta `src` y ejecutar `make` (en caso de conflictos, realizar primero `make clean` y a continuación `make`).
  * **Ejecución:** `./parser < fichero-ocl`
- `inputs/`: Contiene los archivos de prueba normalizados del campus virtual, categorizados en ficheros sin errores (`inputXX.ocl`) y ficheros diseñados para evaluar la gestión de errores léxicos y sintácticos (`errorXX.ocl`).