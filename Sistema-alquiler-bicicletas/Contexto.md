# Sistema de Alquiler de Bicicletas y Movilidad Urbana 

## Contexto del Proyecto
Este proyecto fue desarrollado en equipo como parte de la asignatura de **Tecnologías para el Desarrollo de Software (TDS)** 

- **Autores:**
  * Emily Rodrigues
  * Ainhoa Carbajo
  * Marcos De Diego

---

## Descripción de la Práctica
El objetivo principal fue diseñar e implementar en **Java** el sistema backend completo para la gestión de una red urbana de alquiler de bicicletas, estaciones de aparcamiento, planificación de rutas y fidelización de usuarios mediante un sistema de recompensas.

El desarrollo se llevó a cabo aplicando metodologías ágiles en tres iteraciones evolutivas:

### Iteración 1: Dominio Principal y Lógica de Negocio
- **Modelado de Entidades Principales:** Clases `Usuario`, `Bicicleta`, `Parada`, `Alquiler`, `Reserva` y `Bloqueo`.
- **Gestión de Negocio:** Implementación de gestores para altas y bajas, validación de DNI, cálculo de distancias por coordenadas geográficas (Fórmula Haversine) y gestión de niveles de batería en bicicletas eléctricas.
- **Sistema de Recompensas:** Lógica para asignar e intercambiar puntos por incentivos según el uso de la red de transporte.

### Iteración 2: Arquitectura, Aislamiento y Patrón Repository
- **Desacoplamiento de la Capa de Datos:** Diseño e implementación de interfaces de repositorio (`IUsuarioRepositorio`, `IParadaRepositorio`, `IRecompensaRepositorio`, `IRutaRepositorio`).
- **Gestores en Aislamiento:** Creación de versiones en aislamiento para simular la persistencia y facilitar las pruebas de integración sin dependencias externas.
- **Gestión de Rutas:** Introducción de las clases `Ruta` y `GestorRutas` para el cálculo de trayectos óptimos entre paradas.

### Iteración 3: Persistencia con ORM, CI/CD y Refactorización
- **Integración con Base de Datos:** Implementación real de los repositorios conectándolos a una base de datos relacional mediante el ORM **Hibernate**.
- **Integración Continua:** Configuración de pipelines en **GitLab CI/CD** para la automatización de builds y tests.
- **Calidad y Refactorización:** Análisis continuo de *Code Smells* con **SonarQube** y aplicación de técnicas del *Catálogo de Refactorización de Martin Fowler* (`Extract Method`, `Consolidate Conditional Expression`, eliminación de código duplicado y reestructuración de asociaciones bidireccionales).

---

## Tecnologías y Conceptos Aplicados
- **Lenguaje:** Java
- **Persistencia & ORM:** Hibernate, SQL, Patrón Repository.
- **Calidad & Refactorización:** SonarQube, Catálogo de Refactorización de Fowler, Clean Code.
- **Testing & CI/CD:** JUnit, GitLab CI/CD.
- **Control de Versiones & Metodología:** Git, flujo por ramas y gestión de *Issues*.