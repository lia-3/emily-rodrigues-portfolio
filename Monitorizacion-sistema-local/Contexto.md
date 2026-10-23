# Sistema de Monitorización de Rendimiento Local (ERSS)

## Contexto del Proyecto
Este proyecto fue desarrollado en equipo como parte de la asignatura de **Evaluación y Rendimiento de Sistemas Software (ERSS)** 

- **Autores:**
  * Emily Fabiana Rodrigues Álvarez
  * Mario Martín Carmona
  * Elena Martínez Vázquez

---

## Descripción de la Práctica
El objetivo principal fue diseñar e implementar un sistema automatizado para la **recopilación, almacenamiento y visualización del rendimiento de una máquina virtual**, empleando la biblioteca Java **OSHI** (*Operating System and Hardware Information*).

El desarrollo se estructuró en torno a los siguientes componentes técnicos:

### 1. Recopilación y Automatización de Métricas
- **Extracción de Datos:** Desarrollo de un script automatizado en Bash (`terminal.sh`) respaldado por la biblioteca OSHI para la captura periódica de métricas del sistema operativo[cite: 3].
- **Automatización de Tareas (Systemd):** Configuración de un servicio (`metrics.service`) y un temporizador (`metrics.timer`) en Linux para la ejecución en segundo plano y control temporal de la recolección de datos.

### 2. Persistencia y Almacenamiento
- **Base de Datos Relacional:** Configuración de un servidor **MySQL** (`scriptDB.sql`) para el almacenamiento estructurado y persistente de todas las métricas de rendimiento recopiladas (CPU, memoria, red y disco).

### 3. Visualización y Análisis (Grafana)
- **Dashboard Personalizado:** Integración y configuración de **Grafana** para la representación visual en tiempo real de los datos.
- **Métricas Analizadas:**
  * **CPU:** Carga instantánea, frecuencia del procesador y consumo detallado por proceso.
  * **Memoria:** Evolución del uso de RAM y almacenamiento en memoria SWAP.
  * **Red y Disco:** Estadísticas de tráfico (bytes enviados/recibidos), conexiones activas, operaciones de E/S (lecturas/escrituras) y uso del espacio en disco.
  * **Procesos:** Conteo total de procesos activos y listados detallados de ejecución.

---

## Tecnologías y Conceptos Aplicados
- **Lenguaje & Entorno:** Bash Scripting, Linux (Systemd).
- **Librería de Sistema:** OSHI (OS and Hardware Information)[cite: 3].
- **Persistencia:** MySQL, SQL.
- **Monitorización & Visualización:** Grafana.
- **Control de Versiones:** Git y organización estructurada por directorios de infraestructura.

---

## Estructura del Repositorio
- `src/`: Contiene el script Bash para la extracción automatizada de métricas del sistema[cite: 3].
- `config/`: Archivos `.service` y `.timer` para la programación y ejecución en segundo plano mediante el demonio del sistema.
- `database/`: Scripts SQL para la creación y gestión de la base de datos relacional.
- `docs/`: Documentación técnica y memoria de la práctica.