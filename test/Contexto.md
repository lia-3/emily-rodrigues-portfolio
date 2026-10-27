# Simulador Interactivo de Exámenes (Test PDF & CSV)

## Contexto del Proyecto

Este proyecto fue desarrollado de forma individual como una herramienta de apoyo académico para la preparación y autoevaluación de exámenes tipo test.

El objetivo principal ha sido diseñar una aplicación de escritorio interactiva en Python capaz de procesar automáticamente un documento PDF con un cuestionario estructurado y un archivo CSV con las soluciones oficiales, ofreciendo una experiencia de simulación de examen realista con cálculo dinámico de puntuaciones (incluyendo penalización por errores) y estadísticas detalladas de rendimiento.

**Autora:**  
Rodrigues Álvarez, Emily Fabiana

---

## Descripción del Sistema

La aplicación gestiona de forma integrada el flujo completo de resolución de un examen tipo test a través de una interfaz gráfica intuitiva (Tkinter):

- **Procesamiento y Extracción de Documentos:** Lectura y análisis automatizado de archivos PDF mediante la librería PyMuPDF, detectando y estructurando de forma secuencial las preguntas numeradas (1..n) y sus respectivas opciones de respuesta (a..d).

- **Carga y Validación de Soluciones:** Procesamiento seguro de un archivo CSV con las respuestas correctas, aplicando normalización de datos mediante expresiones regulares para asegurar la correspondencia exacta con las preguntas del test.

- **Simulador Interactivo y Puntuación:** Interfaz de usuario con navegación fluida (preguntas anteriores y siguientes), control de estados (botones bloqueados tras responder para evitar modificaciones) y un sistema de baremación configurable (ej. suma por aciertos y penalización por fallos) con visualización de la puntuación acumulada en tiempo real.

- **Informe de Resultados y Estadísticas:** Pantalla de cierre que desglosa el rendimiento del estudiante (preguntas totales, contestadas, no contestadas, aciertos, fallos, puntuación final y nota sobre 10).

---

## Estructura del Repositorio

El repositorio se encuentra estructurado de forma directa para facilitar su puesta en marcha:

- `pdf_tester.py`: Archivo principal de la aplicación que contiene la lógica de negocio, el motor de extracción de PDF/CSV y la interfaz gráfica basada en Tkinter.

- `README.md`: Documentación técnica del proyecto, requisitos e instrucciones de instalación y ejecución.

---

## Tecnologías y Conceptos Aplicados

- **Lenguaje y Entorno:** Python (entornos virtuales `venv`).

- **Librerías de Procesamiento y UI:** PyMuPDF (`fitz`) para el análisis de documentos PDF, Tkinter para el diseño de la interfaz gráfica de escritorio.

- **Manipulación de Datos y Patrones:** Expresiones regulares (`re`) para el parseo de textos, lectura y estructuración de archivos mediante `csv`.

- **Control de Versiones:** Git.