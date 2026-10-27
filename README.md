# Emily Rodrigues · Portfolio de desarrollo de software

Estudiante de 4º de Ingeniería Informática en la Universidad de Valladolid (UVa). Me interesa el **desarrollo backend** y la **calidad de software (QA)**. Este repositorio reúne los proyectos que he desarrollado durante la carrera, casi todos en equipo.

**Contacto:** [LinkedIn](https://www.linkedin.com/in/emily-rodrigues-4616b438a/) · [GitHub](https://github.com/lia-3)

---

## Stack y herramientas

| Área | Tecnologías |
|---|---|
| Lenguajes | Java (el que más uso), Python, C, Bash, Prolog |
| Backend y persistencia | Hibernate, JDBC, Flask, SQLAlchemy, patrón Repository |
| Bases de datos | MySQL / MariaDB, PostgreSQL |
| Testing y calidad | JUnit, pruebas en aislamiento, pruebas con base de datos, pruebas de aceptación, Locust, SonarQube |
| DevOps y herramientas | Git, GitHub, GitLab CI/CD, Make, Linux (systemd), Grafana |

---

## Proyectos

### Backend y calidad de software

- **[Sistema de alquiler de bicicletas](./Sistema-alquiler-bicicletas)** · *Tecnologías para el Desarrollo de Software*
  Backend en Java para una red urbana de alquiler de bicicletas, con estaciones, rutas y sistema de recompensas. Desarrollado en tres iteraciones ágiles: lógica de negocio, patrón Repository con gestores en aislamiento y persistencia con Hibernate. Incluye pruebas con JUnit, pipeline en GitLab CI/CD y análisis de *code smells* con SonarQube, además de refactorizaciones del catálogo de Fowler.
  `Java` `Hibernate` `JUnit` `GitLab CI/CD` `SonarQube`
  Para saber más: [Contexto del sistema de alquiler de bicletas](./sistema-alquiler-bicicletas/Contexto.md)

- **[Sistema TCONN-ACME: implementación](./dise%C3%B1o-sistemas-tconn)** · *Diseño de Sistemas Software*
  Implementación de parte de los subsistemas de gestión y soporte técnico de una empresa de telecomunicaciones, entregada en cuatro *releases* (identificarse, cambiar disponibilidad, consultar incidencias, atender llamada). Aplicación de escritorio con Java Swing y PostgreSQL por JDBC, con un plan de **pruebas de aceptación** por release y scripts SQL para poblar los datos de cada prueba.
  `Java` `Swing` `JDBC` `PostgreSQL`
  Para saber más: [Contexto del sistema de TCONN-ACME en implementación](./diseño-sistemas-tconn/Contexto.md)

- **[Sistema de logística global](./Sistema-logistica-global)** · *Programación Orientada a Objetos*
  Backend en Java para modelar puertos, muelles, contenedores y trayectos de una empresa logística ficticia. Jerarquías de clases y clases abstractas (contenedores estándar, refrigerados y *flat rack*; trayectos simples y combinados), con pruebas unitarias en JUnit 4, principios SOLID y documentación con JavaDoc.
  `Java` `JUnit 4` `SOLID` `JavaDoc`
  Para saber más: [Contexto del sistema de logística global](./sistema-logistica-global/Contexto.md)

### Pruebas de carga y monitorización

- **[Pruebas de carga y monitorización web](./Monitorizacion-pagina-web)** · *Evaluación y Rendimiento de Sistemas Software*
  Tienda online en Flask con API REST, sometida a pruebas de carga con Locust (de 20 a 500 usuarios virtuales simultáneos) para localizar cuellos de botella. Las métricas del sistema se recogen con `psutil`, se guardan en MySQL y se visualizan en dashboards de Grafana.
  `Python` `Flask` `Locust` `Grafana` `MySQL`
  Para saber más: [Contexto de las pruebas de carga y monitorización web](./monitorizacion-pagina-web/Contexto.md)

- **[Monitorización de rendimiento local](./Monitorizacion-sistema-local)** · *Evaluación y Rendimiento de Sistemas Software*
  Sistema que recoge automáticamente las métricas de una máquina virtual (CPU, memoria, red y disco) con la librería OSHI, las programa con un servicio y un temporizador de systemd, las guarda en MySQL y las muestra en Grafana.
  `Bash` `OSHI` `systemd` `MySQL` `Grafana`
  Para saber más: [Contexto de la monitorización del rendimiento local](./monitorizacion-sistema-local/Contexto.md)

### Bases de datos y desarrollo web

- **[Base de datos de concesiones de los Montes de Almonte](./base-datos-montes-almonte)** · *Análisis y Diseño de Bases de Datos*
  Diseño conceptual, lógico e implementación de una base de datos relacional para gestionar las concesiones administrativas de los montes de propios del Ayuntamiento de Almonte, a partir de su ordenanza reguladora. Incluye diagrama entidad-relación extendido, esquema relacional y justificación de las decisiones de diseño.
  `Modelo E-R` `Esquema relacional` `SQL`
  Para saber más: [Contexto de la base de datos de los Montes de Almonte](./base-datos-montes-almonte/Contexto.md)

- **[INSPIRIA](./inspiria)** · *Servicios y Sistemas Web*
  Red social de tutoriales de artesanía y "hazlo tú mismo", con venta de productos y gestión de encargos. Incluye el análisis de requisitos, el modelo de dominio, el diseño de la base de datos y un prototipo funcional con Servlets, JSP y DAOs sobre MariaDB y Tomcat.
  `Java` `JSP` `Servlets` `MariaDB` `Tomcat`
  Para saber más: [Contexto de la aplicación web INSPIRIA](./inspiria/Contexto.md)

### Algoritmos

- **[Análisis empírico de algoritmos de ordenación](./analisis-empirico-algoritmos-ordenacion)** · *Análisis y Diseño de Algoritmos*
  Comparativa experimental de Inserción, QuickSort con primer pivote y QuickSort con pivote aleatorio sobre vectores de hasta 100.000 elementos. Mediciones controladas (JIT desactivado, 20 repeticiones por tamaño) y análisis de la complejidad asintótica.
  `Java` `Make` `Análisis de complejidad`
  Para saber más: [Contexto del análisis empírico de algoritmos de ordenación](./analisis-empirico-algoritmos-ordenacion/Contexto.md)

- **[Compatibilidad de contenedores en cadenas de suministro](./compatibilidad-contenedores)** · *Análisis y Diseño de Algoritmos*
  Resolución de la secuencia máxima de contenedores compatibles con tres técnicas: fuerza bruta, algoritmo voraz y programación dinámica. Arquitectura modular con una interfaz común de medición y comparación experimental del rendimiento de cada enfoque.
  `Java` `Backtracking` `Voraz` `Programación dinámica`
  Para saber más: [Contexto de la compatibilidad de contenedores](./compatibilidad-contenedores/Contexto.md)

### Lenguajes de programación y compiladores

Tres prácticas encadenadas sobre un subconjunto de UML y OCL:

- **[Analizador léxico](./analizador-lexico)**: reconocimiento del léxico de OCL con Lex y C, con tres estados y un informe estadístico final. `C` `Lex/Flex`
    Para saber más: [Contexto del analizador léxico](./analizador-lexico/Contexto.md)
- **[Analizador léxico y sintáctico](./analizador-lexico-sintactico)**: análisis léxico y sintáctico de OCL con Lex y Yacc. `Lex` `Yacc`
    Para saber más: [Contexto del analizador léxico-sintáctico](./analizador-lexico-sintactico/Contexto.md)
- **[Analizador semántico y traductor](./analizador-semantico)**: validación de reglas semánticas de un modelo UML/OCL y generación automática de código Java con ANTLR-4. `Java` `ANTLR-4`
    Para saber más: [Contexto del analizador semántico](./analizador-semantico/Contexto.md)

### Modelado de sistemas

- **[Modelado de sistemas TCONN-ACME](./modelado-sistemas-tconn)** · *Modelado de Sistemas Software*
  Análisis y modelado completo de un sistema de soporte técnico en telecomunicaciones: procesos de negocio con diagramas de actividad, modelo de dominio con diagramas de clases, restricciones formales en OCL y diagrama de estados de las incidencias.
  `UML` `OCL` `Visual Paradigm`
  Para saber más: [Contexto del modelado de sistemas TCONN-ACME](./modelado-sistemas-tconn/Contexto.md)

### Otros

- **[Test interactivo desde PDF](./test)**
  Aplicación en Python que lee preguntas de un PDF con cuatro opciones, carga las respuestas correctas desde un CSV y muestra el test de forma interactiva con interfaz gráfica.
  `Python` `PyMuPDF` `Tkinter` `Expresiones regulares`
  Para saber más: [Contexto de la mini aplicación de test](./test/Contexto.md)

---

## Sobre estos proyectos

La mayoría son trabajos académicos realizados en equipo. Cada carpeta incluye un `Contexto.md` con los autores, el objetivo, las tecnologías usadas y las instrucciones para compilar o ejecutar el proyecto.
