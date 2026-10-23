# Sistema de Gestión y Soporte Técnico en Telecomunicaciones 

## Contexto del Proyecto
Este proyecto integral fue desarrollado como parte de la asignatura de **Modelado de Sistemas Software (MSS)** del **Grado en Ingeniería Informática de la Universidad de Valladolid (UVa)** (Curso 2025-2026). 

El objetivo principal ha sido el análisis, especificación y modelado completo de un sistema informático avanzado para la empresa **TCONN-ACME**, orientada a la prestación de servicios de internet e instalaciones de telecomunicaciones tanto para clientes particulares como empresariales.

- **Autoras:** 
    * Martínez Lafuente, Elisa
    * Rodrigues Álvarez, Emily Fabiana


---

## Descripción del Sistema
La plataforma modela una arquitectura compleja y robusta que abarca múltiples flujos operativos de la compañía:
1. **Central Call-Center:** Recepción de llamadas, validación de contratos activos, tipificación de incidencias según su criticidad (*Crítica, Alta, Media, Baja*), soporte telefónico básico y ejecución de acciones remotas.
2. **Gestión de Campo y Técnicos:** Operativa de técnicos móviles distribuidos geográficamente, geolocalización por GPS, control de disponibilidad, aceptación de órdenes en plazos estrictos (10 min), ejecución de procedimientos estandarizados y digitalización de partes de servicio con firma digital.
3. **Supervisión y SLA:** Administración de empleados, turnos de trabajo, control de flotas de vehículos, cumplimiento estricto de Acuerdos de Nivel de Servicio (SLA) con penalizaciones económicas, y generación de informes de rendimiento.

---

## Estructura del Repositorio
El proyecto se encuentra estructurado de forma modular reflejando las diferentes fases de análisis y modelado llevadas a cabo:

- `modelo-proceso-negocio/`: Modelado de procesos de negocio mediante diagramas de actividad UML (cobertura desde la recepción de la llamada en el call center hasta la resolución de la incidencia, regreso a base y actualización de disponibilidad del técnico).
- `modelo-dominio/`: Modelado conceptual del dominio del sistema en diagramas de clases UML, identificando clases, atributos, asociaciones, multiplicidades y requisitos de información extraídos de las entrevistas con los *stakeholders*.
- `restricciones-ocl-diag-actividad/`: Especificación formal de restricciones mediante **OCL** (*Object Constraint Language*) para validar reglas complejas de negocio (como restricciones de contratos activos, turnos, descansos obligatorios de 12 horas, lógica de SLA y penalizaciones), además del diagrama de estados dinámico de una incidencia.
- `Contexto.md`: Documentación detallada del caso de estudio, resumen de entrevistas con coordinadores, técnicos de campo, supervisores y director comercial, así como los anexos de partes de desplazamiento e informes adjuntos.

---

## Tecnologías y Conceptos Aplicados
- **Ingeniería del Software:** Modelado conceptual, análisis de requisitos, diagramas de actividad, diagramas de clases y diagramas de estados (UML).
- **Lenguajes de Especificación:** OCL (Object Constraint Language).
- **Herramientas de Modelado:** Visual Paradigm y herramientas de apoyo al diseño formal.
- **Control de Versiones:** Git / GitLab (con evolución continua e histórico de commits estructurados por etapas).