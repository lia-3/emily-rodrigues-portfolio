# Sistema de Pruebas de Carga y Monitorización Web (ERSS)

## Contexto del Proyecto
Este proyecto fue desarrollado en equipo como parte de la asignatura de **Evaluación y Rendimiento de Sistemas Software (ERSS)**

- **Autores:**
  * Emily Fabiana Rodrigues Álvarez
  * Mario Martín Carmona
  * Elena Martínez Vázquez

---

## Descripción de la Práctica
El objetivo principal fue desarrollar una aplicación web funcional (tienda online basada en Flask y SQLAlchemy) y someterla a un proceso riguroso de **evaluación de rendimiento mediante pruebas de carga automatizadas**, analizando el impacto directo sobre los recursos del sistema y monitorizándolos en tiempo real.

El desarrollo se estructuró en torno a los siguientes componentes técnicos:

### 1. Desarrollo del Sistema Web (Backend & Frontend)
- **Arquitectura MVC:** Implementación de una tienda online en **Flask (Python)** con gestión de sesiones, autenticación segura mediante hashing de contraseñas (*Werkzeug*) y plantillas HTML con Jinja2.
- **Persistencia de Datos:** Creación de bases de datos relacionales independientes para la tienda online (`tienda_online`) y el registro de métricas del sistema (`grafana`).
- **API REST:** Desarrollo de endpoints específicos para facilitar la automatización de flujos de usuario (registro, login, consulta de productos, carrito y procesamiento de pedidos).

### 2. Pruebas de Carga con Locust
- **Simulación de Tráfico Concurrente:** Diseño de escenarios de prueba realistas mediante scripts en Python (`load_test.py`) con la herramienta **Locust**.
- **Escenarios Evaluados:** Simulación progresiva de carga bajo distintos niveles de concurrencia (desde 20 hasta 500 usuarios virtuales simultáneos) para identificar los umbrales de saturación y cuellos de botella del backend.

### 3. Monitorización y Análisis de Rendimiento (Grafana)
- **Recopilación de Métricas:** Desarrollo de scripts en Python empleando la librería `psutil` para la extracción periódica de indicadores clave del sistema operativo.
- **Visualización en Tiempo Real:** Configuración de dashboards en **Grafana** para correlacionar la carga de usuarios de Locust con el consumo real de recursos:
  * **CPU:** Carga instantánea, frecuencia del procesador y consumo detallado por proceso.
  * **Memoria:** Evolución del uso de memoria RAM y estabilidad del espacio de intercambio (SWAP).
  * **Disco y Red:** Control del uso de almacenamiento, operaciones de E/S, tráfico de red (bytes enviados/recibidos) y número de conexiones activas.
  * **Procesos:** Conteo y análisis de procesos activos bajo carga.

---

## Tecnologías y Conceptos Aplicados
- **Lenguaje & Entorno:** Python, Flask, SQLAlchemy, Bash Scripting.
- **Pruebas de Carga:** Locust.
- **Monitorización & Visualización:** Grafana, psutil.
- **Persistencia:** MySQL / MariaDB.
- **Control de Versiones:** Git y organización estructurada por módulos.

---

## Estructura del Repositorio
- `aplicacion/`: Contiene el código fuente del backend (Flask), controladores, modelos ORM, plantillas HTML y recursos estáticos del frontend.
- `monitor/`: Scripts de Python y configuraciones para la recolección, procesamiento y almacenamiento persistente de las métricas de rendimiento.
- `load_test.py`: Script de definición de escenarios y flujos de usuarios virtuales para las pruebas de carga con Locust.
- `application_datos.sql` / `grafana.sql`: Scripts SQL para la inicialización de las bases de datos de la tienda online y de la monitorización.