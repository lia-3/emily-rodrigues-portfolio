# Analizador Semántico y Traductor de Modelos con ANTLR-4 (Lenguajes de Programación)

## Contexto del Proyecto
Este proyecto fue desarrollado en equipo como parte de la asignatura de **Lenguajes de Programación (LP)** del **Grado en Ingeniería Informática de la Universidad de Valladolid (UVa)** (Curso 2024-2025).

- **Autores:**
  * Miguel Bueno González
  * Mario González Liras
  * Paula Negro Asegurado
  * Emily Fabiana Rodrigues Álvarez

---

## Descripción de la Práctica
El objetivo principal fue desarrollar un **analizador semántico y un generador de código en Java** utilizando **ANTLR-4**, capaz de procesar especificaciones de modelos de clases UML simplificados y restricciones OCL asociadas, validando reglas semánticas complejas y traduciendo el modelo a código Java ejecutable[cite: 113, 114, 115].

El desarrollo abarcó los siguientes pilares técnicos:

### 1. Comprobación de Reglas Semánticas (`SemanticChecker.java`)
- **Validación de Clases y Tipos UML:** Verificación de que los atributos, parámetros de operaciones y tipos de retorno correspondan a tipos primitivos, enumeraciones, *datatypes* o clases válidas definidas en el modelo (reglas UML.1 a UML.3)[cite: 113, 114].
- **Consistencia de Asociaciones:** Comprobación de que ambos extremos de las asociaciones apunten a clases existentes y que el uso de modificadores como `unique` o `sorted` sea coherente con la multiplicidad (reglas UML.4 a UML.6)[cite: 114].
- **Validación de Contextos OCL:** Comprobación de que los contextos (`context ID` y `context ID::operation`) hagan referencia a clases y operaciones válidas del modelo, validando además el uso correcto de `self.ID` frente a atributos y asociaciones opuestas (reglas OCL.1 a OCL.3)[cite: 114].

### 2. Generación de Código Java (`JavaCodeGenerator.java`)
- **Mapeo de Tipos y Colecciones:** Traducción de tipos primitivos UML a Java (`Boolean` -> `boolean`, `Integer` -> `int`, `Real` -> `float`, `String` -> `String`) y mapeo avanzado de asociaciones según su unicidad y orden (empleando `TreeSet`, `HashSet`, `ArrayList` o `LinkedList` de `java.util`)[cite: 114, 115].
- **Traducción de Invariantes y Métodos:** Generación automática de métodos de comprobación (`checkN()`) para cada invariante, constructores con aserciones de validación, y estructuración de métodos para operaciones con inyección literal de bloques `pre`/`post` en comentarios de código[cite: 115].

---

## Tecnologías y Conceptos Aplicados
- **Lenguajes:** Java, ANTLR-4[cite: 113, 116].
- **Automatización de Compilación:** Make / Makefile[cite: 116].
- **Entorno de Desarrollo:** Linux / Unix[cite: 116].
- **Control de Versiones:** Git, GitLab (Inf-UVa)[cite: 116].

---

## Estructura y Uso del Repositorio
- `src/`: Contiene el código fuente del proyecto (`Main.java`, `SemanticChecker.java`, `JavaCodeGenerator.java`), la gramática de ANTLR-4 y el archivo `Makefile` para la automatización[cite: 113, 116].
  * **Compilación general:** Escribir `make` en la raíz del proyecto para generar las clases de ANTLR-4 y compilar todo el código Java.
  * **Compilación del modelo generado:** Escribir `make compile-model` para validar la compilación del código Java resultante.
  * **Ejecución del analizador y traductor:** 
    ```bash
    java -cp src/bin:lib/antlr-4.13.2-complete.jar Main inputs/input_ok_00.txt
    ```
    *(Este proceso genera el archivo `ModeloGenerado.java` en el directorio `src` siempre que el texto cumpla con todas las reglas semánticas y sintácticas)[cite: 113].*
- `inputs/`: Contiene los archivos de prueba para validar el comportamiento del sistema:
  * `input_ok_00.txt` y `input_ok_01.txt`: Ficheros válidos que cumplen correctamente con todas las especificaciones de la práctica[cite: 113].
  * `input_error_00.txt`: Fichero basado en el caso del campus virtual que incorpora errores intencionados en reglas UML y OCL para comprobar la robustez del detector semántico[cite: 113].