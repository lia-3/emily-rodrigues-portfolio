# Base de Datos de Concesiones Administrativas: Montes de Almonte (ADBD)

## Contexto del Proyecto
Este proyecto fue desarrollado en equipo como parte de la asignatura de **Análisis y Diseño de Bases de Datos** 

- **Autores**
  * Antolín Caramazana, Ángel
  * Muñóz Sánchéz, Diego
  * Pérez Alonso, Marta
  * Rodrigues Alvarez, Emily Fabiana

---

## Descripción de la Práctica
El objetivo principal fue el diseño conceptual, lógico e implementación de un sistema gestor de bases de datos relacional para la gestión integral del padrón de concesiones administrativas sobre los denominados **"montes de propios" del Ayuntamiento de Almonte (Huelva)**, basándose estrictamente en su Ordenanza Reguladora

El sistema abarca la gestión de parcelas de dominio público, concesiones temporales, cánones fiscales anuales, inspecciones, infracciones, sanciones y la jerarquía completa de personas (titulares, representantes e inspectores)

---

## Decisiones de Diseño Clave ("¿Por qué hemos hecho...?")
Durante el modelado del sistema a partir de la normativa municipal, se tomaron las siguientes decisiones de diseño fundamentales:
- **Gestión de Sanciones:** Nos percatamos de que, según la ordenanza, una infracción puede dar lugar como máximo a dos sanciones diferenciadas (una monetaria y otra de reducción o revocación de tiempo). Por simplicidad y eficiencia del modelo relacional, se estructuró para que cada infracción agrupe de forma limpia sus penalizaciones asociadas
- **Evolución histórica de Parcelas y Concesiones:** Una misma parcela de Almonte puede estar vinculada a múltiples concesiones a lo largo del tiempo (normalmente por cuestiones de titularidad o transmisiones). Por ello, ante un cambio de concesión, se genera un nuevo registro histórico sobre el mismo terreno físico
- **Exclusión de la entidad "Cesión":** No se trata la cesión como una entidad independiente debido a que el sistema solo almacena datos validados y firmados; una vez aprobada una cesión, el resultado práctico es la formalización de una nueva concesión con un nuevo titular y un nuevo identificador
- **Agrupación de Parcelas:** Las concesiones con lindero común de un mismo titular que se deseen agrupar comparten un identificador común (`idAgrupación`), simplifying las consultas directas por agrupación

---

## Modelado y Esquemas del Sistema

### 1. Diagrama Entidad-Relación (ERD)
El modelo conceptual refleja las entidades principales (*Parcela, Concesión, Canon, Construcción, ActaInspección, Infracción y Persona*), junto con su jerarquía de generalización `ISA` para personas (físicas, jurídicas, representantes e inspectores)

![Versión Actual ERD](img/erd.png)

*Para facilitar su análisis detallado, se puede consultar la [versión intermedia y modular del ERD](img/intermedio.png).*

### 2. Esquema Relacional
La traducción del modelo conceptual al modelo lógico relacional se estructuró optimizando la integridad referencial, resolviendo las relaciones N:M mediante tablas débiles intermedias (*Porcentaje, Tutela, Firma*) y aplicando la estrategia de tablas por superclase y subclases para la jerarquía de personas

![Versión Actual Relacional](img/esquema-relacional.png)

---

## Tecnologías y Conceptos Aplicados
- **Modelado de Datos:** Diagramas Entidad-Relación extendidos (EER), Mapeo Relacional, Restricciones de integridad (XOR, Claves Compuestas, Rangos de validación).
- **Normativa de Dominio:** Ley de Montes, Reglamento de Bienes de Entidades Locales de Andalucía y Ordenanza Reguladora de Montes de Propios de Almonte
- **Control de Versiones:** Git y organización modular de documentación.

---

## Estructura del Repositorio
- `docs/`: Documentación técnica completa del proyecto (`memoria.pdf`)
- `img/`: Capturas y esquemas gráficos del modelo relacional y entidad-relación.
- `database/` *(o scripts)*: Scripts de creación de bases de datos, restricciones e inserción de datos de prueba validados.