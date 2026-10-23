# Sistema de Gestión de Contenedores y Logística

## Contexto del Proyecto
Este proyecto fue desarrollado en equipo como parte de la asignatura de **Programación Orientada a Objetos (POO)

* **Autoras:** 
  * Marta Pérez
  * Emily Rodrigues

---

## Descripción de la Práctica
El objetivo principal fue diseñar e implementar en **Java** un sistema backend para modelar la logística global de la empresa ficticia *MArkse-Co*. El sistema simula la gestión integral de puertos, muelles, flotas de contenedores y rutas de transporte, aplicando principios sólidos de ingeniería de software.

El desarrollo se dividió en dos fases principales:

### Estructura Base y Gestión de Puertos
* **Modelado de Entidades Principales:** Clases `Puerto`, `Muelle`, `Contenedor` y `Trayecto`.
* **Lógica de Almacenamiento:** Gestión de plazas en los muelles, control de apilamiento de contenedores según sus características físicas (presencia de techo, dimensiones) y restricciones de capacidad.
* **Integración Externa:** Uso de la clase `GPSCoordinate` (proporcionada por la universidad mediante bytecode) para calcular distancias geográficas reales en millas marinas.
* **Calidad:** Implementación de pruebas unitarias exhaustivas con **JUnit 4** y aplicación de criterios de *Clean Code*.

### Ampliación, Polimorfismo y Patrones de Diseño
* **Jerarquías de Clases y Clases Abstractas:** 
  * Evolución de `Contenedor` a clases especializadas: `Estandar`, `Refrigerado` (con control de motor/temperatura) y `FlatRack` (requiere dos plazas y restricciones especiales de apilamiento).
  * Evolución de `Trayecto` hacia tipos **Simples** (`TBarco`, `TTren`, `TCamion`) y **Combinados** (`PackCamionBarco`, `PackCamionTren`) con reglas de tarificación y optimización de costes.
* **Adaptación de Infraestructuras:** Modificación de los muelles para admitir únicamente contenedores compatibles con las infraestructuras de transporte disponibles.

---

## Tecnologías y Conceptos Aplicados
* **Lenguaje:** Java (JDK 11)
* **Paradigma:** Programación Orientada a Objetos avanzada (Herencia, Polimorfismo, Clases Abstractas).
* **Testing:** JUnit 4.
* **Buenas Prácticas:** Principios SOLID, Clean Code y documentación técnica con JavaDoc.