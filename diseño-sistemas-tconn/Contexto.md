# Sistema de Gestión y Soporte Técnico en Telecomunicaciones (TCONN-ACME)

## Contexto del Proyecto
Este proyecto integral ha sido desarrollado como parte de la asignatura de **Modelado de Sistemas Software (MSS)** del **Grado en Ingeniería Informática de la Universidad de Valladolid (UVa)** (Curso 2025-2026). 

El objetivo principal ha sido el análisis, especificación, modelado e implementación de una parte crítica de los subsistemas centralizados y de bases para la empresa **TCONN-ACME**, orientada a la prestación de servicios de internet e instalaciones de telecomunicaciones tanto para clientes particulares como empresariales.

- **Autoras:** 
  * González Liras, Marío
  * Muñóz Sanchéz, Diego
  * Rodrigues Álvarez, Emily Fabiana

---

## Índice
1. [Descripción del Sistema](#descripción-del-sistema)
2. [Tecnologías y Arquitectura](#tecnologías-y-arquitectura)
3. [Estructura del Repositorio](#estructura-del-repositorio)
4. [Release 1: CU Identificarse](#release-1-cu-identificarse)
5. [Release 2: CU Cambiar disponibilidad al llegar a la base](#release-2-cu-cambiar-disponibilidad-al-llegar-a-la-base)
6. [Release 3: CU Consultar Incidencias](#release-3-cu-consultar-incidencias)
7. [Release 4 - Final: CU Atender Llamada](#release-4---final-cu-atender-llamada)

---

## Descripción del Sistema
La plataforma modela una arquitectura compleja y robusta que abarca múltiples flujos operativos de la compañía:
1. **Central Call-Center:** Recepción de llamadas, validación de contratos activos, tipificación de incidencias según su criticidad (*Crítica, Alta, Media, Baja*), soporte telefónico básico y ejecución de acciones remotas.
2. **Gestión de Campo y Técnicos:** Operativa de técnicos móviles distribuidos geográficamente, geolocalización por GPS, control de disponibilidad, aceptación de órdenes en plazos estrictos (10 min), ejecución de procedimientos estandarizados y digitalización de partes de servicio con firma digital.
3. **Supervisión y SLA:** Administración de empleados, turnos de trabajo, control de flotas de vehículos, cumplimiento estricto de Acuerdos de Nivel de Servicio (SLA) con penalizaciones económicas, y generación de informes de rendimiento.

---

## Tecnologías y Arquitectura

* **Lenguaje y Entorno:** Java (JDK 21), utilizando `java.swing` para la interfaz gráfica de escritorio.
* **Herramienta de Desarrollo:** NetBeans (versión 26).
* **Gestión de Dependencias y Build:** Apache Maven.
* **Persistencia:** Base de datos relacional centralizada en **PostgreSQL** mediante JDBC.
* **Patrones y Estilo Arquitectónico:** 
  * Arquitectura de **3 capas estrictas** combinada con el patrón **MVC** (Modelo-Vista-Controlador) y una capa relajada transversal de servicios.
  * Patrón de acceso a datos **DAO + DTO** (donde el DTO se estructura como una cadena JSON).
  * Aplicación de principios GRASP y patrones de diseño orientados a objetos.

---

## Estructura del Repositorio

El repositorio se encuentra estructurado de forma modular reflejando las diferentes fases de análisis, modelado e implementación:

```text
├── models/             # Archivo ASTAH PROFESSIONAL / VISUAL PARADIGM con modelos de diseño (arquitectura, clases, secuencia y estados).
└── app/
    ├── netbeansProject/ # Proyecto Maven gestionado para NetBeans con la implementación de los casos de uso.
    └── db/              # Scripts SQL de población, configuración configdb.env y scripts de creación de tablas.
```

- `models/`: Contiene los diagramas de arquitectura de referencia, descomposición modular, dependencias entre capas, clases de diseño detallado, diagramas de secuencia para la realización de los CUs y el diagrama de estados de la interfaz de usuario.
- `app/netbeansProject/`: Código fuente del subsistema desarrollado en Java Swing con JDBC.
- `app/db/`: Archivo `configdb.env` con las variables de entorno de conexión a PostgreSQL y scripts SQL para poblar la base de datos en las pruebas de aceptación de cada release.

---

## Release 1: CU Identificarse

### Pruebas de aceptación

Cargar el script SQL [`R1_poblacionCUIdentificarse.sql`](app/db/R1_poblacionCUIdentificarse.sql) en la base de datos PostgreSQL.

**Login Correcto: Distintos Roles**

* **PA-01: Login correcto → Coordinador**
  * **Prueba:** Paso 2 secuencia principal.
  * **Flujo Interacción y datos a introducir:** NIF: `64683961A` / Contraseña: `iE1bF1eD`
  * **Resultado esperado:** Acceso concedido. Se muestran las opciones del rol Coordinador.
  * **Nota:** Histórico de vinculación (Normal → ERTE → Normal) y disponibilidad (Disponible → Baja → Disponible).

* **PA-02: Login correcto → Supervisor**
  * **Prueba:** Paso 2 secuencia principal.
  * **Flujo Interacción y datos a introducir:** NIF: `23169952M` / Contraseña: `bV4eT5bD`
  * **Resultado esperado:** Acceso concedido. Se muestran las opciones del rol Supervisor.
  * **Nota:** Histórico de vinculación (Normal → ERTE → Normal) y disponibilidad (Disponible → Vacaciones → Disponible).

* **PA-03: Login correcto → Técnico**
  * **Prueba:** Paso 2 secuencia principal.
  * **Flujo Interacción y datos a introducir:** NIF: `34567890C` / Contraseña: `passLaura`
  * **Resultado esperado:** Acceso concedido. Se muestran las opciones del rol Técnico.
  * **Nota:** Histórico de rol (Coordinador → Técnico) y disponibilidad (Disponible → Baja → Disponible → Vacaciones → Disponible).

**Login Incorrecto / Restricciones de Actividad (RN-2)**

* **PA-04: NIF inexistente**
  * **Prueba:** Paso 2a de alternativas y excepciones.
  * **Flujo Interacción y datos a introducir:** NIF: `00000000X` / Contraseña: `passLaura`
  * **Resultado esperado:** Error → Credenciales Incorrectas (por NIF).

* **PA-05: Contraseña incorrecta**
  * **Prueba:** Paso 2b de alternativas y excepciones.
  * **Flujo Interacción y datos a introducir:** NIF: `64683961A` / Contraseña: `wrongpass`
  * **Resultado esperado:** Error → Credenciales Incorrectas (por contraseña).

* **PA-06: Empleado no activo → En ERTE**
  * **Prueba:** Paso 2c de alternativas y excepciones.
  * **Flujo Interacción y datos a introducir:** NIF: `44556677H` / Contraseña: `passAna`
  * **Resultado esperado:** Error → Empleado no en activo.

* **PA-07: Empleado no activo → Fuera de la empresa**
  * **Prueba:** Paso 2a de alternativas y excepciones.
  * **Flujo Interacción y datos a introducir:** NIF: `11223344J` / Contraseña: `passCarlos`
  * **Resultado esperado:** Error → Empleado no en activo.

* **PA-08: Empleado no activo → De vacaciones**
  * **Prueba:** Paso 2a de alternativas y excepciones.
  * **Flujo Interacción y datos a introducir:** NIF: `99887766K` / Contraseña: `passLucia`
  * **Resultado esperado:** Error → Empleado no en activo.

* **PA-09: Empleado no activo → De baja temporal**
  * **Prueba:** Paso 2a de alternativas y excepciones.
  * **Flujo Interacción y datos a introducir:** NIF: `55443322L` / Contraseña: `passPedro`
  * **Resultado esperado:** Error → Empleado no en activo.

* **PA-10: Campos vacíos**
  * **Prueba:** Errores en los campos de la vista.
  * **Flujo Interacción y datos a introducir:** NIF: ` ` / Contraseña: ` `
  * **Resultado esperado:** Error → Campos obligatorios vacíos.

* **PA-11: NIF vacío**
  * **Prueba:** Errores en los campos de la vista.
  * **Flujo Interacción y datos a introducir:** NIF: ` ` / Contraseña: `1234`
  * **Resultado esperado:** Error → Campos obligatorios vacíos.

* **PA-12: Contraseña vacía**
  * **Prueba:** Errores en los campos de la vista.
  * **Flujo Interacción y datos a introducir:** NIF: `64683961A` / Contraseña: ` `
  * **Resultado esperado:** Error → Campos obligatorios vacíos.

### Al finalizar el CU

Al iniciar sesión con éxito, se abre un menú que dependiendo del rol del actor muestra:

* **Coordinador:** 
  * Botón `Atender Llamada`
  * Botón `Añadir servicio a un contrato`
* **Supervisor:** 
  * Botón `Consultar Incidencias`
  * Botón `Asignar técnico a base`
* **Técnico:** 
  * Botón `Cambiar disponibilidad al llegar a base`
  * Botón `Completar informe adjunto al parte`

---

## Release 2: CU Cambiar disponibilidad al llegar a la base

### IMPORTANTE (Configuración GPS)
1. Realizar las pruebas en el orden indicado por la numeración.
2. Si se producen errores con las coordenadas GPS, añadir las siguientes líneas en `ControladorDeCUCambioDisponibilidad.geolocalizar`:
   ```java
   System.out.println("MI LATITUD: " + coords[0]); 
   System.out.println("MI LONGITUD: " + coords[1]);
   ```
   Ajustar las coordenadas obtenidas en la tabla `Base` mediante el script [`usuariosPruebaCUCambiarDisponibilidad.sql`](app/db/usuariosPruebaCUCambiarDisponibilidad.sql).
3. Cargar script SQL [`R2_poblacionCUCambiarDisponibilidad.sql`](app/db/R2_poblacionCUCambiarDisponibilidad.sql).

### Pruebas de aceptación

**Cambio no Realizado: Cancelación por parte del Técnico**

* **PA-13: Cancela antes de indicar que ha llegado a la base**
  * **Prueba:** Paso 1a de alternativas y excepciones.
  * **Flujo Interacción y datos a introducir:**
    1. NIF: `34567890C` / Contraseña: `passLaura`
    2. Haz clic en `Aceptar`
    3. Haz clic en `Cambiar disponibilidad al llegar a la base`
    4. Haz clic en `Cancelar`
  * **Resultado esperado:** Vuelve al menú del técnico.

* **PA-14: Cancela antes de confirmar el cambio de estado**
  * **Prueba:** Paso 3a de alternativas y excepciones.
  * **Flujo Interacción y datos a introducir:**
    1. NIF: `34567890C` / Contraseña: `passLaura`
    2. Haz clic en `Aceptar`
    3. Haz clic en `Cambiar disponibilidad al llegar a la base`
    4. Haz clic en `Entrar en base`
    5. Selecciona en el desplegable la opción `No disponible`.
    6. Haz clic en `Cancelar`
  * **Resultado esperado:** Vuelve a la página de cambio de disponibilidad y no se registra ningún cambio de situación en la BD.

**Cambio Correcto: Flujo Principal y Alternativos**

* **PA-15: Está asignado y cambia a disponible**
  * **Prueba:** Secuencia principal.
  * **Flujo Interacción y datos a introducir:**
    1. NIF: `34567890C` / Contraseña: `passLaura`
    2. Haz clic en `Aceptar`
    3. Haz clic en `Cambiar disponibilidad al llegar a la base`
    4. Haz clic en `Entrar en base`
    5. Selecciona en el desplegable la opción `Disponible`.
    6. Haz clic en `Aceptar`
  * **Resultado esperado:** Acceso concedido. Se registra el nuevo estado "Disponible" con fecha/hora actual y coordenadas de la base. El técnico queda libre para nuevas incidencias.

* **PA-16: Está asignado y cambia a no disponible**
  * **Prueba:** Paso 3b de alternativas y excepciones.
  * **Flujo Interacción y datos a introducir:**
    1. Eliminar el último registro de la tabla `cambiodesituacion`.
    2. NIF: `34567890C` / Contraseña: `passLaura`
    3. Haz clic en `Aceptar`
    4. Haz clic en `Cambiar disponibilidad al llegar a la base`
    5. Haz clic en `Entrar en base`
    6. Selecciona en el desplegable la opción `No disponible`.
    7. Haz clic en `Aceptar`
  * **Resultado esperado:** Acceso concedido. Se registra el estado "No Disponible" con fecha/hora actual y coordenadas de la base.

**Cambio Incorrecto: Excepciones de Estado y Turno**

* **PA-17: Técnico fuera de turno**
  * **Prueba:** Paso 2a de alternativas y excepciones.
  * **Flujo Interacción y datos a introducir:** NIF: `55443322L` / Contraseña: `passPedro`
  * **Resultado esperado:** Muestra mensaje de error "El técnico no se encuentra en turno".

* **PA-18: Técnico no asignado previamente**
  * **Prueba:** Paso 2b de alternativas y excepciones.
  * **Flujo Interacción y datos a introducir:** NIF: `44556677H` / Contraseña: `passAna`
  * **Resultado esperado:** Muestra mensaje de error "El técnico no está asignado a ninguna incidencia".

* **PA-19: Técnico fuera de base**
  * **Prueba:** Inconsistencia entre coordenadas del técnico y las de su base.
  * **Flujo Interacción y datos a introducir:** NIF: `87654321D` / Contraseña: `passMarcos`
  * **Resultado esperado:** Muestra mensaje de error "El técnico no está dentro de su base".

* **PA-20: Sin selección de cambio**
  * **Prueba:** No selecciona cambio de disponibilidad.
  * **Resultado esperado:** Muestra mensaje de error "Debe seleccionar un estado".

* **PA-21: Cambio a no disponible - No cumple RN-3**
  * **Prueba:** Paso 3c de alternativas y excepciones.
  * **Flujo Interacción y datos a introducir:** NIF: `98765432E` / Contraseña: `passElena`, selecciona `No disponible`.
  * **Resultado esperado:** Muestra mensaje de error "Pasa a estar disponible, no puede pasar a no disponible" e inserta automáticamente el cambio de estado a "Disponible" en la BD.

* **PA-22 y PA-23: Acceso denegado a cambio de responsabilidad**
  * Intento de acceso por Coordinador o Supervisor. Denegado por perfil de rol.

---

## Release 3: CU Consultar Incidencias

Cargar script SQL [`R3_poblacionCUConsultarIncidencias.sql`](app/db/R3_poblacionCUConsultarIncidencias.sql) a la base de datos.
Iniciar sesión con el usuario Supervisor: NIF: `12345678A` / Contraseña: `pass123`.

### Pruebas de aceptación

**Consulta no Realizada: Cancelación y Flujos de Salida**

* **PA-24: Cancelación al indicar fecha** (Paso 1a). Vuelve al menú del Supervisor.
* **PA-25: Finalización tras ver lista** (Paso 6a). Finaliza el CU y vuelve al menú.

**Validación de Datos y Excepciones**

* **PA-26: Sin incidencias en la fecha indicada** (Paso 2c). Muestra "No se registraron incidencias en la fecha indicada".
* **PA-27 a PA-29: Sin incidencias con filtros aplicados** (`Resuelta Remotamente`, `Resuelta In Situ`, `No Resuelta`). Muestra mensaje informativo del filtro vacío.

**Consulta Correcta y Visualización**

* **PA-30: Listado de incidencias (Flujo Principal)** (Paso 2). Muestra lista ordenada por hora de llamada (código, hora, criticidad, estado).
* **PA-31 a PA-33: Filtrado por estado (Usabilidad)**. La lista se filtra correctamente según el estado elegido.

**Integración y Seguridad**

* **PA-34: Integración con Consultar detalles** (Paso 4). Seleccionar una incidencia y pulsar `Más Detalles` muestra el mensaje *"Not implemented yet"*.
* **PA-35: Acceso denegado (Actor no autorizado)**. NIF: `44556677C`. El menú no muestra la opción de consultar incidencias.

---

## Release 4 - Final: CU Atender Llamada

Cargar script SQL [`R4_poblacionCUAtenderLlamada.sql`](app/db/R4_poblacionCUAtenderLlamada.sql) a la base de datos.
Iniciar sesión como Coordinador: NIF: `64683961A` / Contraseña: `iE1bF1eD`.

### Aclaraciones de Usabilidad
1. **Panel 1:** Agrupa número de origen, datos del contrato, NIF y teléfono de contacto para agilizar la gestión.
2. **Teléfono de contacto:** El sistema detecta y compara automáticamente si coincide en el Panel 2.
3. **Bucle de retorno:** Ante inconsistencias o fallos de datos maestros, el sistema regresa al Panel 1 para permitir corregir los campos.

### Pruebas de aceptación

**Validación de Formato y Campos Obligatorios**
* **PA-36 a PA-38:** Validación de longitud (no 10 ni 8 dígitos) y caracteres no numéricos en teléfono origen.
* **PA-39 a PA-45:** Control de campos obligatorios vacíos en Panel 1 y Panel 2 (ubicaciones, radio buttons sin marcar).

**Llamadas Incorrectas e Inconsistentes**
* **PA-46: Llamada Incorrecta por "No aporta datos"** (Paso 3a). Registra Llamada Incorrecta con fecha/hora, duración y origen.
* **PA-47 y PA-48: Llamada Incorrecta por contrato inexistente o no en vigor** (Paso 4a). Informa del error y registra Llamada Incorrecta.
* **PA-49: Llamada Inconsistente** (Paso 5a). Registra Llamada Inconsistente detallando las discrepancias.

**Reintentos y Retornos al Panel de Edición**
* **PA-50: NIF/CIF no registrado** (Paso 6a). Mantiene el Panel 1 activo para corregir.
* **PA-51 a PA-53: Reintento por discrepancias** (Nombre, Teléfono o Ubicación no coincidente). Notifica el error y regresa al Panel 1.

**Flujos de Éxito y Finalización**
* **PA-54: Registro de Consulta - Contrato Particular** (Pasos 8 y 9). Crea Llamada Correcta vinculada al contrato y finaliza.
* **PA-55: Registro de Consulta - Contrato Empresarial** (Múltiples ubicaciones). Permite seleccionar la sede correcta y registra la consulta.
* **PA-56: Integración con Registrar Incidencia** (Paso 8a). Abre sub-interfaz con mensaje *"Not implemented yet"* y guarda la llamada.
* **PA-57 a PA-59: Reintentos tras fallos y éxito posterior**. Demuestra que tras corregir datos en Panel 1 el flujo finaliza con éxito.
* **PA-60 y PA-61: Cancelación desde Panel 1**. Vuelve al menú del Coordinador sin escribir en la BD.
* **PA-62 y PA-63: Control de acceso por rol**. Denegado para roles Técnico y Supervisor.