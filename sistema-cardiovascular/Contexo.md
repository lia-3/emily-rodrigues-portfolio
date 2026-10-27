# Sistema Experto de Diagnóstico Cardiovascular en CLIPS (SPCardio)

## Contexto del Proyecto
Este proyecto fue desarrollado como parte de la asignatura de **Ingeniería del Conocimiento** 

- **Autores:**
  * Antolín Caramazana, Ángel
  * Rodrigues Álvarez, Emily Fabiana

---

## Descripción de la Práctica
El objetivo principal del proyecto fue el diseño y desarrollo de una **Base de Conocimiento basada en Reglas de Producción** utilizando el lenguaje experto **CLIPS**, aplicando un Lenguaje de Representación del Conocimiento (LRC) basado en tripletas **Objeto-Atributo-Valor (O-A-V)**.

El dominio de aplicación se centra en el **Sistema Cardiovascular Humano**, implementando heurísticas clínicas asociadas al diagnóstico de patologías concretas y gestionando tanto atributos univaluados como multivaluados.

---

## Pilares Técnicos y Funcionalidades del Sistema

### 1. Gestión de Semántica Univaluada (O-A-V)
- **Plantillas Dinámicas:** Definición estructurada mediante `deftemplate` separando hechos univaluados (`oav-u`) y multivaluados (`oav-m`).
- **Control de Coherencia de Hechos:** Implementación de una regla de alta prioridad (`salience 1000`) para garantizar la semántica univaluada, eliminando de la memoria de trabajo los valores obsoletos o duplicados de un mismo atributo y conservando únicamente el último hecho añadido.

### 2. Razonamiento Clínico y Heurístico (Reglas de Producción)
El sistema modela el conocimiento superficial y clínico (basado en los párrafos clave de la especificación del dominio) automatizando el diagnóstico de cuatro patologías cardiovasculares principales:
- **Aneurisma de la Arteria Abdominal:** A partir de síntomas de dolor abdominal, rumor abdominal y presencia de masa pulsante.
- **Regurgitación Aórtica:** Evaluación del cálculo automático de la presión del pulso ($\ge 65 \text{ mmHg}$), presión sistólica elevada ($\ge 150 \text{ mmHg}$) y evidencias clínicas como rumor sistólico o dilatación del corazón.
- **Estenosis en Arteria de la Pierna:** Detección mediante el síntoma de calambres en las extremidades inferiores al andar.
- **Arteriosclerosis:** Regla de inferencia encadenada que relaciona la presencia de estenosis en la pierna con factores de pertenencia a grupos de riesgo (obesidad, tabaquismo prolongado o edad avanzada).
- **Control de Ausencia de Diagnóstico:** Mecanismo de baja prioridad (`salience -10`) para notificar formalmente cuando a un paciente analizado no se le ha diagnosticado ninguna patología cardiovascular.

---

## Tecnologías y Conceptos Aplicados
- **Lenguaje Experto:** CLIPS.
- **Paradigma de Representación:** Sistemas de Producción, tripletas O-A-V, variables y plantillas (`deftemplate`).
- **Control de Ejecución:** Gestión de prioridades mediante `salience`, operadores lógicos avanzados y funciones de control de memoria (`retract`, `assert`, `bind`).
- **Control de Versiones:** Git / GitLab (Inf-UVa).

---

## Estructura del Repositorio
- `src/` o raíz: Contiene el código fuente completo en CLIPS (`.clp`) con la definición de plantillas, hechos iniciales de pacientes de prueba (Carolina y Francisco), reglas de control univaluado, reglas de cálculo métrico y motor de inferencia diagnóstica.
- `docs/`: Memoria técnica detallada del proyecto y especificación formal de la conceptualización.