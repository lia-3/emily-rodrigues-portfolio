# dis02-2026
---

## Índice
1. [Release 1: CU Identificarse](#release-1-cu-identificarse)
    1. [Pruebas de aceptación](#pruebas-de-aceptación)
    2. [Al finalizar el CU](#al-finalizar-el-cu)
2. [Release 2: CU Cambiar disponibilidad al llegar a la base](#release-2-cu-cambiar-disponibilidad-al-llegar-a-la-base)
    1. [IMPORTANTE](#importante)
    2. [Pruebas de aceptación](#pruebas-de-aceptación-1)
3. [Release 3: CU Consultar Incidencias](#release-3-cu-consultar-incidencias)
    1. [Pruebas de aceptación](#pruebas-de-aceptación-2)
4. [Release 4 - Final: CU Registar Llamada](#release-4---final-cu-atender-llamada)
    1. [Aclaraciones sobre el flujo de alternativas (6a - 6d) y la agrupación de campos](#aclaraciones-sobre-el-flujo-de-alternativas-6a---6d-y-la-agrupación-de-campos)
    2. [IMPORTANTE](#importante-1)
    3. [Pruebas de aceptación](#pruebas-de-aceptación-3)

--- 
## Release 1: CU Identificarse

### Pruebas de aceptación

Cargar script sql [`R1_poblacionCUIdentificarse`](app/db/R1_poblacionCUIdentificarse.sql) a la base de datos.

**Login Correcto: Distintos Roles**

* **PA-01**: Login correcto → Coordinador
    * **Prueba:**  paso 2 secuencia principal
    * **Flujo Interacción y datos a introducir:** NIF: `64683961A` / Contraseña: `iE1bF1eD`
    * **Resultado esperado:** Acceso concedido. Se muestran las opciones del rol Coordinador.
    * **Nota:** histórico de vinculación (Normal → ERTE → Normal) y disponibilidad (Disponible → Baja → Disponible)

* **PA-02:** Login correcto → Supervisor
    * **Prueba:**  paso 2 secuencia principal
    * **Flujo Interacción y datos a introducir:**NIF: `23169952M` / Contraseña: `bV4eT5bD`
    * **Resultado esperado:** Acceso concedido. Se muestran las opciones del rol Supervisor.
    * **Nota:** histórico de vinculación (Normal → ERTE → Normal) y disponibilidad (Disponible → Vacaciones → Disponible)

* **PA-03:** Login correcto → Técnico
    * **Prueba:**  paso 2 secuencia principal
    * **Flujo Interacción y datos a introducir:**NIF: `34567890C` / Contraseña: `passLaura`
    * **Resultado esperado:** Acceso concedido. Se muestran las opciones del rol Técnico.
    * **Nota:** histórico de rol (Coordinador → Técnico) y disponibilidad (Disponible → Baja → Disponible → Vacaciones → Disponible)

**Login Incorrecto**

* **PA-04:** NIF inexistente
    * **Prueba:**  paso 2a de alternativas y excepciones
    * **Flujo Interacción y datos a introducir:**NIF: `00000000X` / Contraseña: `passLaura`
    * **Resultado esperado:** Error → Credenciales Incorrectas (por nif).

* **PA-05:** Contraseña incorrecta 
    * **Prueba:**  paso 2b de alternativas y excepciones
    * **Flujo Interacción y datos a introducir:**NIF: `64683961A` / Contraseña: `wrongpass`
    * **Resultado esperado:** Error → Credenciales Incorrectas (por contraseña).

* **PA-06:** Empleado no activo → En ERTE 
    * **Prueba:**  paso 2c de alternativas y excepciones
    * **Flujo Interacción y datos a introducir:**NIF: `44556677H` / Contraseña: `passAna`
    * **Resultado esperado:** Error → Empleado no en activo.

* **PA-07:** Empleado no activo → Fuera de la empresa
    * **Prueba:**  paso 2a de alternativas y excepciones
    * **Flujo Interacción y datos a introducir:**NIF: `11223344J` / Contraseña: `passCarlos`
    * **Resultado esperado:** Error → Empleado no en activo.

* **PA-08:** Empleado no activo → De vacaciones 
    * **Prueba:**  paso 2a de alternativas y excepciones
    * **Flujo Interacción y datos a introducir:**NIF: `99887766K` / Contraseña: `passLucia`
    * **Resultado esperado:** Error → Empleado no en activo.

* **PA-09:** Empleado no activo → De baja temporal 
    * **Prueba:**  paso 2a de alternativas y excepciones
    * **Flujo Interacción y datos a introducir:**NIF: `55443322L` / Contraseña: `passPedro`
    * **Resultado esperado:** Error → Empleado no en activo.

* **PA-10:** Campos vacíos 
    * **Prueba:**  errores en los campos de la vista
    * **Flujo Interacción y datos a introducir:**NIF: ` ` / Contraseña: ` `
    * **Resultado esperado:** Error → Campos obligatorios vacíos.

* **PA-11:** NIF vacío
    * **Prueba:**  errores en los campos de la vista
    * **Flujo Interacción y datos a introducir:**NIF: ` ` / Contraseña: `1234`
    * **Resultado esperado:** Error → Campos obligatorios vacíos.

* **PA-12:** Contraseña vacía
    * **Prueba:**  errores en los campos de la vista
    * **Flujo Interacción y datos a introducir:**NIF: `64683961A` / Contraseña: ` `
    * **Resultado esperado:** Error → Campos obligatorios vacíos.

## Al finalizar el CU

Al iniciar sesión con éxito, se abre un menú que dependiendo del rol del actor muestra:

* Coordinador: 
    * Botón `Atender Llamada`
    * Botón `Añadir servicio a un contrato`

* Supervisor: 
    * Botón `Consultar Incidencias`
    * Botón `asignar técnico a base`

* Técnico: 
    * Botón `Cambiar diposnibilidad al llegar a base`
    * Botón `Completar informe adjunto al parte`

---
## Release 2: CU Cambiar disponibilidad al llegar a la base

### IMPORTANTE
Es necesario:
1. Realizar estas pruebas en el orden que indican los números de las pruebas
2. Si da errores con las coordenadas, añadir las siguientes líneas a ControladorDeCUCambioDisponibilidad.geolocalizar, dentro del try, después de la declaración e instanciiación de los tres `double[]`

            System.out.println("MI LATITUD: " + coords[0]); 
            System.out.println("MI LONGITUD: " + coords[1]);

    De esta manera podremos conocer nuestras coordenadas y así cambiarlas en las siguientes líneas del [`usuariosPruebaCUCambiarDisponibilidad`](app/db/usuariosPruebaCUCambiarDisponibilidad.sql):

            INSERT INTO Base (id_base, direccion, ubicacion, id_zona) VALUES
            (1, ..., ROW(TU_LAT_DE_CONSOLA, TU_LON_DE_CONSOLA)::tipo_coordenada_gps, 'ZONA_CENTRO'),
            (2, ..., ROW(TU_LAT_DE_CONSOLA, TU_LON_DE_CONSOLA)::tipo_coordenada_gps, 'ZONA_SUR');


3. Cargar script sql [`R2_poblacionCUCambiarDisponibilidad`](app/db/R2_poblacionCUCambiarDisponibilidad.sql) a la base de datos.

4. Cargar script sql para añadir incidencias a los tecnicos.

### Pruebas de aceptación

**Cambio no Realizado: Cancelación por parte del Técnico**

* **PA-13:** Cancela antes de indicar que ha llegado a la base
    * **Prueba:**  paso 1a de alternativas y excepciones
    * **Flujo Interacción y datos a introducir:**
        1. NIF: 34567890C / Contraseña: passLaura 
        2. Haz click en `Aceptar`
        3. Haz click en `Cambiar disponibilidad al llegar a la base`
        4. Haz click en `Cancelar`
    * Resultado: vuelve al menú del técnico

* **PA-14:** Cancela antes de confirmar el cambio de estado
    * **Prueba:**  paso 3a de alternativas y excepciones
    * **Flujo Interacción y datos a introducir:**        
        1. NIF: 34567890C / Contraseña: passLaura 
        2. Haz click en `Aceptar`
        3. Haz click en `Cambiar disponibilidad al llegar a la base`
        4. Haz click en `Entrar en base`
        5. Selecciona en el desplegable la opción `No disponible`.
        6. Haz click en `Cancelar`
    * Resultado: vuelve a la página de cambio de disponibilidad, donde aparece el botón de `Entrar en base`, y no se ha añadido ningún cambio de situación a la base de Flujo Interacción y datos a introducir.

**Cambio Correcto: Flujo Principal y Alternativos**

* **PA-15:** Está asignado y cambia a disponible
    * **Prueba:**  secuencia principal
    * **Flujo Interacción y datos a introducir:**
        1. NIF: 34567890C / Contraseña: passLaura 
        2. Haz click en `Aceptar`
        3. Haz click en `Cambiar disponibilidad al llegar a la base`
        4. Haz click en `Entrar en base`
        5. Selecciona en el desplegable la opción `Disponible`.
        6. Haz click en `Aceptar`
    * **Resultado esperado:** acceso concedido al cambio. Se registra el nuevo estado "Disponible" con la fecha/hora actual y coordenadas de la base. El técnico queda libre para nuevas incidencias. Vuelve al paso previo (reaparece botón `Entrar en base`)

* **PA-16:** Está asignado y cambia a no disponible
    * **Prueba:**  paso 3b de alternativas y excepciones
    * **Flujo Interacción y datos a introducir:**
        1. Eliminar el último registro de la tabla cambiodesituacion
        2. NIF: 34567890C / Contraseña: passLaura  
        3. Haz click en `Aceptar`
        4. Haz click en `Cambiar disponibilidad al llegar a la base`
        5. Haz click en `Entrar en base`
        6. Selecciona en el desplegable la opción `No disponible`        
        7. Haz click en `Aceptar`
    * **Resultado esperado:** acceso concedido al cambio. Se registra el nuevo estado "No Disponible" con la fecha/hora actual y coordenadas de la base. El técnico queda libre para nuevas incidencias. Vuelve al paso previo (reaparece botón `Entrar en base`)

**Cambio Incorrecto: Excepciones de Estado y Turno**

* **PA-17:** Técnico fuera de turno
    * **Prueba:**  paso 2a de alternativas y excepciones
    * **Flujo Interacción y datos a introducir:**       
        1. NIF: 55443322L / Contraseña: passPedro  
        2. Haz click en `Aceptar`
        3. Haz click en `Cambiar disponibilidad al llegar a la base`
        4. Haz click en `Entrar en base`
    * **Resultado esperado:** muestra mensaje de error "El técnico no se encuentra en turno" y no avanza (no muestra desplegable con las opciones de cambio)

* **PA-18:** Técnico no asignado previamente
    * **Prueba:**  paso 2b de alternativas y excepciones
    * **Flujo Interacción y datos a introducir:**
        1. NIF: 44556677H / Contraseña: passAna 
        2. Haz click en `Aceptar`
        3. Haz click en `Cambiar disponibilidad al llegar a la base`
        4. Haz click en `Entrar en base`
    * **Resultado esperado:** muestra mensaje de error "El técnico no esta asignado a ninguna incidencia" y no avanza (no muestra desplegable con las opciones de cambio)

* **PA-19:** Técnico fuera de base
    * **Prueba:**  incosistencia entre las coordenadas actuales del técnico y las de su base
    * **Flujo Interacción y datos a introducir:**
        1. NIF: 87654321D / Contraseña: passMarcos 
        2. Haz click en `Aceptar`
        3. Haz click en `Cambiar disponibilidad al llegar a la base`
        4. Haz click en `Entrar en base`
    * **Resultado esperado:** muestra mensaje de error "El técnico no esta dentro de su base" y no avanza (no muestra desplegable con las opciones de cambio)

**Falta de datos introducidos**
* **PA-20:** Sin selección de cambio
    * **Prueba:**  no selecciona cambio de disponibilidad
    * **Flujo Interacción y datos a introducir:**
        1. NIF: 98765432E / Contraseña: passElena   
        2. Haz click en `Aceptar`
        3. Haz click en `Cambiar disponibilidad al llegar a la base`
        4. Haz click en `Entrar en base`      
        6. Haz click en `Aceptar`
    * Resultado: muestra mensaje de error "Debe seleccionar un estado"

**Validación de Reglas de Negocio (RN-3)**

* **PA-21:** Cambio a no disponible - No cumple RN-3
    * **Prueba:**  paso 3a de alternativas y excepciones
    * **Flujo Interacción y datos a introducir:**
        1. NIF: 98765432E / Contraseña: passElena   
        2. Haz click en `Aceptar`
        3. Haz click en `Cambiar disponibilidad al llegar a la base`
        4. Haz click en `Entrar en base`
        5. Selecciona en el desplegable la opción `No disponible`       
        6. Haz click en `Aceptar`
    * Resultado: muestra mensaje de error "Pasa a estar disponible, no puede pasar a no disponible" e inserta el cambio de estado a Disponible" en la base de Flujo Interacción y datos a introducir

**Acceso denegado a cambio de responsabilidad**

* **PA-22:** Intento de acceso por Coordinador
    * **Prueba:**  verificación de precondición (Actor: Técnico)
    * **Flujo Interacción y datos a introducir:**
        NIF: 64683961A / Contraseña: iE1bF1eD  
    * Resultado: muestra la página de inicio de coordinador (no puede mostrar botones como `Cambiar disponibilidad al llegar a la base`)

* **PA-23:** Intento de acceso por Supervisor
    * **Prueba:**  verificación de precondición (Actor: Técnico)
    * **Flujo Interacción y datos a introducir:**NIF: 23169952M / Contraseña: bV4eT5bD  
    * Resultado: muestra la página de inicio de supervisor (no puede mostrar botones como `Cambiar disponibilidad al llegar a la base`)

--- 
## Release 3: CU Consultar Incidencias

* Realizar estas pruebas en el orden que indican los números de las pruebas
* Cargar script sql [`R3_poblacionCUConsultarIncidencias`](app/db/R3_poblacionCUConsultarIncidencias.sql) a la base de datos.
* Iniciar sesión con el usuario:
    * NIF: 12345678A
    * Contraseña: pass123          

### Pruebas de aceptación

**Consulta no Realizada: Cancelación y Flujos de Salida**

* **PA-24:** Cancelación al indicar fecha
    * **Prueba:**  Paso 1a de alternativas.
    * **Flujo Interacción y datos a introducir:**
        * El Supervisor pulsa en Consultar Incidencias.
        * En la pantalla de selección de fecha, haz click en Cancelar.
    * **Resultado esperado:** El sistema cancela la operación y vuelve al menú principal del Supervisor.

* **PA-25:** Finalización tras ver lista
    * **Prueba:**  Paso 6a de alternativas.
    * **Flujo Interacción y datos a introducir:**
        * El Supervisor pulsa en Consultar Incidencias.
        * Selecciona la fecha 2026-05-01.
        * Tras visualizar la lista, haz click en Cancelar.
    * **Resultado esperado:** El caso de uso finaliza y vuelve al menú principal del Supervisor.

**Validación de Datos y Excepciones**

* **PA-26:** Sin incidencias en la fecha indicada
    * **Prueba:**  Paso 2c de alternativas.
    * **Flujo Interacción y datos a introducir:**
        * El Supervisor pulsa en Consultar Incidencias.
        * Selecciona la fecha 2020-05-12.
    * **Resultado esperado:** Muestra mensaje informando que "No se registraron incidencias en la fecha indicada".
    * **Nota:** no cerrar la sesión.

* **PA-27:** Sin incidencias en la fecha con el filtro indicado
    * **Prueba:**  Error en filtro
    * **Flujo Interacción y datos a introducir:**
        * El Supervisor pulsa en Consultar Incidencias.
        * Selecciona la fecha 2020-05-02.
        * Selecciona en el filtro `Resuelta Remotamente`
    * **Resultado esperado:** Muestra mensaje informando que "No hay incidencias con el filtro: Resuelta Remotamente".
    * **Nota:** no cerrar la sesión.

* **PA-28:** Sin incidencias en la fecha con el filtro indicado
    * **Prueba:**  Error en filtro
    * **Flujo Interacción y datos a introducir:**
        * El Supervisor pulsa en Consultar Incidencias.
        * Selecciona la fecha 2020-05-03.
        * Selecciona en el filtro `Resuelta In Situ`
    * **Resultado esperado:** Muestra mensaje informando que "No hay incidencias con el filtro: Resuelta In Situ".
    * **Nota:** no cerrar la sesión.

* **PA-29:** Sin incidencias en la fecha con el filtro indicado
    * **Prueba:**  Error en filtro
    * **Flujo Interacción y datos a introducir:**
        * El Supervisor pulsa en Consultar Incidencias.
        * Selecciona la fecha 2020-05-04.
        * Selecciona en el filtro `No Resuelta`
    * **Resultado esperado:** Muestra mensaje informando que "No hay incidencias con el filtro: No Resuelta".
    * **Nota:** no cerrar la sesión.

**Consulta Correcta y Visualización**

* **PA-30:** Listado de incidencias (Flujo Principal)
    * **Prueba:**  Paso 2 de la secuencia principal.
    * **Flujo Interacción y datos a introducir:**
        * Selecciona la fecha 2020-05-01.
    * **Resultado esperado:** Se muestra una lista con 7 incidencias (1001-1007). Se verifica que aparecen: Código, Hora de llamada, Criticidad y Estado, ordenadas cronológicamente por la hora de la llamada. No permite pulsar `Mas Detalles`
    * **Nota:** manetener el estado actual de las ventanas, no cerrar la sesión.

* **PA-31:** Filtrado por estado (Usabilidad)
    * **Prueba:**  Requisito de indicaciones de usabilidad.
    * **Flujo Interacción y datos a introducir:**
        * Selecciona el filtro `Resuelta Remotamente`.
    * **Resultado esperado:** La lista se actualiza mostrando solo las incidencias con ID 1001 y 1002. No permite pulsar `Mas Detalles`
    * **Nota:** manetener el estado actual de las ventanas, no cerrar la sesión.

* **PA-32:** Filtrado por estado (Usabilidad)
    * **Prueba:**  Requisito de indicaciones de usabilidad.
    * **Flujo Interacción y datos a introducir:**
        * Selecciona el filtro `Resuelta In Situ`.
    * **Resultado esperado:** La lista se actualiza mostrando solo las incidencias con ID 1003 y 1004. No permite pulsar `Mas Detalles`
    * **Nota:** manetener el estado actual de las ventanas, no cerrar la sesión.

* **PA-33:** Filtrado por estado (Usabilidad)
    * **Prueba:**  Requisito de indicaciones de usabilidad.
    * **Flujo Interacción y datos a introducir:**
        * Mantener usuario loggeado en * PA-25
        * En la pantalla de resultados, selecciona el filtro `No Resuelta`.
    * **Resultado esperado:** La lista se actualiza mostrando solo las incidencias con ID 1005, 1006 y 1007. No permite pulsar `Mas Detalles`
    * **Nota:** manetener el estado actual de las ventanas, no cerrar la sesión.

**Integración y Seguridad**

* **PA-34:** Integración con Consultar detalles
    * **Prueba:**  Paso 4 de la secuencia principal (Caso de uso integrado).
    * **Flujo Interacción y datos a introducir:**
        * En la pantalla de resultados, selecciona el filtro `Todas`.
        * En la lista de incidencias, selecciona la incidencia con código 1001.
        * Pulsar en `Mas Detalles`
        * **Resultado esperado:** El sistema muestra el mensaje en la sección de detalles: "Not implemented yet".

* **PA-35:** Acceso denegado (Actor no autorizado)
    * **Prueba:**  Verificación de precondición (Actor: Supervisor).
    * **Flujo Interacción y datos a introducir:**
        * Iniciar seión con: Nif = 44556677C, Contraseña = pass321.
    * **Resultado esperado:** Muestra el menú del supervisor (no puede aparecer el botón de consultar incidencias)

---

## Release 4 - Final: CU Atender Llamada

### Aclaraciones sobre el flujo de alternativas (6a - 6d) y la agrupación de campos:
Se han tomado las siguientes decisiones de usabilidad:
1.  **Unificación del Panel 1:** Se ha agrupado la recogida del número de origen junto con los datos del contrato, NIF y teléfono de contacto en un único panel inicial (`datosLlamadaPanel`) para agilizar la interacción y evitar clics redundantes.
2.  **Comprobación inteligente del teléfono:** La interfaz aprovecha el teléfono de contacto introducido en el Panel 1 para pre-marcar automáticamente si coincide o no en el Panel 2, descargando al operador de tareas repetitivas pero manteniendo el control visual.
3.  **Bucle de retorno al Panel 1:** En caso de inconsistencias flagrantes o fallos en datos maestros (NIF no existente, contrato inválido, etc.), el sistema redirige al Panel 1 para permitir la edición y corrección limpia de los campos de entrada.

#### IMPORTANTE
Cargar script sql [`R4_poblacionCUAtenderLlamada`](app/db/R4_poblacionCUAtenderLlamada.sql) a la base de datos.

* Iniciar sesión como Coordinador con las siguientes credenciales (salvo para PA-53 y PA-54):
    * NIF: `64683961A`
    * Contraseña: `iE1bF1eD`

### Pruebas de aceptación

**Validación de Formato de Datos (Robustez)**

* **PA-36: Validación de longitud de teléfono origen (Largo)**
    * **Prueba:** Paso 2 de la secuencia principal (Validación de formato).
    * **Flujo Interacción y datos a introducir:** En el Panel 1:
        * Número origen: `6665554443` (10 dígitos)
        * Nº Contrato: `C-20241001`
        * NIF/CIF: `89562315A`
        * Teléfono contacto: `612345678`
        * Pulsa `Validar`
    * **Resultado esperado:** El sistema indica que el número origen es incorrecto y no permite avanzar al Panel 2.

* **PA-37: Validación de longitud de teléfono origen (Corto)**
    * **Prueba:** Paso 2 de la secuencia principal.
    * **Flujo Interacción y datos a introducir:** En el Panel 1:
        * Número origen: `66655577` (8 dígitos)
        * Nº Contrato: `C-20241001`
        * NIF/CIF: `89562315A`
        * Teléfono contacto: `612345678`
        * Pulsa `Validar`
    * **Resultado esperado:** El sistema indica que el número origen es incorrecto y no permite avanzar al Panel 2.

* **PA-38: Validación de caracteres en teléfono origen**
    * **Prueba:** Paso 2 de la secuencia principal.
    * **Flujo Interacción y datos a introducir:** En el Panel 1:
        * Número origen: `66655A444`
        * Nº Contrato: `C-20241001`
        * NIF/CIF: `89562315A`
        * Teléfono contacto: `612345678`
        * Pulsa `Validar`
    * **Resultado esperado:** El sistema detecta caracteres no numéricos, informa del error y no permite avanzar al Panel 2.

**Gestión de Campos Obligatorios (Espacios en blanco)**

* **PA-39: Bloqueo por falta de datos mínimos en Panel 1**
    * **Prueba:** Paso 3a de alternativas.
    * **Flujo Interacción y datos a introducir:** En el Panel 1:
        * Número origen: vacío
        * Nº Contrato: `C-20241001`
        * NIF/CIF: `89562315A`
        * Teléfono contacto: `612345678`
        * Pulsa el botón "No aporta datos"
    * **Resultado esperado:** El sistema impide el avance y muestra un mensaje de error indicando que hay campos vacíos (no se acepta el nº de origen vacío).

* **PA-40: Omisión de datos en Panel 1 (número contrato)**
    * **Prueba:** Paso 3 de la secuencia principal.
    * **Flujo Interacción y datos a introducir:** En el Panel 1:
        * Número origen: `666555777`
        * Nº Contrato: vacío
        * NIF/CIF: `89562315A`
        * Teléfono contacto: `612345678`
        * Pulsa `Validar`
    * **Resultado esperado:** El sistema impide el avance y muestra un mensaje de error indicando que hay campos vacíos.

* **PA-41: Omisión de datos en Panel 1 (nif/cif)**
    * **Prueba:** Paso 3 de la secuencia principal.
    * **Flujo Interacción y datos a introducir:** En el Panel 1:
        * Número origen: `666555777`
        * Nº Contrato: `C-20241001`
        * NIF/CIF: vacío
        * Teléfono contacto: `612345678`
        * Pulsa `Validar`
    * **Resultado esperado:** El sistema impide el avance y muestra un mensaje de error indicando que hay campos vacíos.

* **PA-42: Omisión de datos en Panel 1 (teléfono contacto)**
    * **Prueba:** Paso 3 de la secuencia principal.
    * **Flujo Interacción y datos a introducir:** En el Panel 1:
        * Número origen: `666555777`
        * Nº Contrato: `C-20241001`
        * NIF/CIF: `89562315A`
        * Teléfono contacto: vacío
        * Pulsa `Validar`
    * **Resultado esperado:** El sistema impide el avance y muestra un mensaje de error indicando que hay campos vacíos.

* **PA-43: Omisión de datos en Panel 1 (teléfono origen) al pulsar no aporta datos**
    * **Prueba:** campo télofono de origen
    * **Flujo Interacción y datos a introducir:** En el Panel 1:
        * Número origen: vacío
        * Nº Contrato: vacío
        * NIF/CIF: vacío
        * Teléfono contacto: vacío
        * Pulsa `No aporta datos`
    * **Resultado esperado:** El sistema impide el avance y muestra un mensaje de error indicando que hay el teléfono de origen indicado no es válido.

* **PA-44: Omisión de selección en Panel 2 (sin ubicación seleccionada)**
    * **Prueba:** Paso 5 de la secuencia principal.
    * **Flujo Interacción y datos a introducir:**
        * Panel 1: 
            * Número origen: `666555777`
            * Nº Contrato: `C-20241001`
            * NIF/CIF: `89562315A`
            * Teléfono contacto: `612345678`
            * Pulsa `Validar`.
        * Panel 2:
            * Marcar "Coincide" en Nombre/Razón Social.
            * Dejar marcado "Coincide" en Nº teléfono contacto (deducido por el sistema).
            * Dejar seleccionado "-- Seleccione una ubicación --" en el desplegable de ubicación.
            * Pulsa `Validar`.
    * **Resultado esperado:** El sistema impide continuar hacia el panel de motivos mostrando un mensaje de error hasta que se complete la selección de ubicación.

* **PA-45: Falta de confirmación de Nombre/Razón Social en Panel 2**
    * **Prueba:** Paso 5 de la secuencia principal.
    * **Flujo Interacción y datos a introducir:**
        * Panel 1:
            * Número origen: `666555777`
            * Nº Contrato: `C-20241001`
            * NIF/CIF: `89562315A`
            * Teléfono contacto: `612345678`
            * Pulsa `Validar`.
        * Panel 2:
            * No seleccionar ningún radio button en Nombre/Razón Social.
            * Dejar marcado "Coincide" en Nº teléfono contacto (deducido por el sistema).
            * Seleccionar "C/ Santiago, 22, Madrid" en el desplegable de ubicación.
            * Pulsa `Validar`.
    * **Resultado esperado:** El sistema impide continuar mostrando un mensaje de error indicando que debe confirmar si el nombre/razón social coincide o no. 

**Flujos de Excepción y Llamadas Incorrectas / Inconsistentes**

* **PA-46: Llamada Incorrecta por pulsar "No aporta datos"**
    * **Prueba:** Paso 3a de alternativas.
    * **Flujo Interacción y datos a introducir:** En el Panel 1:
        * Número origen: `666555777`
        * Nº Contrato: vacío
        * NIF/CIF: vacío
        * Teléfono contacto: vacío
        * Se pulsa el botón "No aporta datos".
    * **Resultado esperado:** El sistema registra una Llamada Incorrecta en la Base de Datos anotando la fecha/hora de inicio, la duración calculada y el número origen, y vuelve automáticamente al menú principal.

* **PA-47: Llamada Incorrecta por contrato inexistente**
    * **Prueba:** Paso 4a de alternativas.
    * **Flujo Interacción y datos a introducir:** En el Panel 1:
        * Número origen: `600111222`
        * Nº Contrato: `CONT-999` (No existe en el sistema)
        * NIF/CIF: `89562315A`
        * Teléfono contacto: `612345678`
        * Pulsa `Validar`.
    * **Resultado esperado:** El sistema detecta que el contrato no es válido, informa en la barra de estado del error, registra una Llamada Incorrecta con sus metadatos (fecha, duración, origen) y finaliza, permitiendo volver al menú o cerrar el programa únicamente.

* **PA-48: Llamada Incorrecta por contrato no en vigor**
    * **Prueba:** Paso 4a de alternativas.
    * **Flujo Interacción y datos a introducir:** En el Panel 1:
        * Número origen: `600111222`
        * Nº Contrato: `C-20241002` (Contrato existe pero caducado)
        * NIF/CIF: `89562315A`
        * Teléfono contacto: `612345678`
        * Pulsa `Validar`.
    *   **Resultado esperado:** El sistema detecta que el contrato no está en vigor, informa en la barra de estado del error, registra una Llamada Incorrecta con sus metadatos (fecha, duración, origen) y finaliza, permitiendo volver al menú o cerrar el programa únicamente.

* **PA-49: Llamada Inconsistente (Alternativa 5a)**
    * **Prueba:** Paso 5a de alternativas.
    * **Flujo Interacción y datos a introducir:**
        * Panel 1:
            * Número origen: `666555777`
            * Nº Contrato: `C-20241001`
            * NIF/CIF: `89562315A`
            * Teléfono contacto: `612345678`
            * Pulsa `Validar`.
        * Panel 2: 
            * Seleccionar "No coincide" en Nombre/Razón Social.
            * Dejar marcado "Coincide" en Nº teléfono contacto (deducido por el sistema).
            * Seleccionar una ubicación válida (ej. "C/ Santiago, 22, Madrid").
            * Pulsa el botón "No aporta datos diferentes".
    * **Resultado esperado:** El sistema procesa el evento, guarda en la base de datos una Llamada Inconsistente vinculando la descripción de la inconsistencia (deducida de los radio buttons "No coincide") junto con los datos de control (fecha/hora, duración, tlf origen) y redirige al menú principal.

**Validación de Identidad y Reintentos (Retornos al Panel de Edición)**

* **PA-50: NIF/CIF no registrado en el sistema**
    *   **Prueba:** Paso 6a de alternativas.
    *   **Flujo Interacción y datos a introducir:** En el Panel 1:
        * Número origen: `666555777`
        * Nº Contrato: `C-20241001`
        * NIF/CIF: `11111111X` (No registrado)
        * Teléfono contacto: `612345678`
        * Pulsa `Validar`.
    * **Resultado esperado:** El sistema informa de la inexistencia del cliente con ese identificador y mantiene habilitado el Panel 1 para permitir la corrección inmediata del campo.

* **PA-51: Reintento por Nombre/Razón Social no coincide**
    * **Prueba:** Paso 6b de alternativas.
    * **Flujo Interacción y datos a introducir:**
        
        * Panel 1:
            * Número origen: `666555777`
            * Nº Contrato: `C-20241001`
            * NIF/CIF: `89562315A`
            * Teléfono contacto: `612345678`
            * Pulsa `Validar`.
        * Panel 2: 
            * Seleccionar "No coincide" en Nombre/Razón Social.
            * Dejar marcado "Coincide" en Nº teléfono contacto (deducido por el sistema).
            * Seleccionar "C/ Santiago, 22, Madrid" como ubicación. Pulsar `Validar`.
    *   **Resultado esperado:** El sistema notifica la discrepancia de identidad (nombre/razón social), invita al usuario a revisar los datos y **regresa al Panel 1** para subsanar el error.

* **PA-52: Reintento por Teléfono de contacto no coincide**
    * **Prueba:** Paso 6c de alternativas.
    * **Flujo Interacción y datos a introducir:**
        * Panel 1:
            * Número origen: `666555777`
            * Nº Contrato: `C-20241001`
            * NIF/CIF: `89562315A`
            * Teléfono contacto: `612345675`
            * Pulsa `Validar`.
        * Panel 2: 
            * El sistema pre-marca "No coincide" en el radio button de teléfono. 
            * Seleccionar "Coincide" en Nombre. 
            * Seleccionar "C/ Santiago, 22, Madrid" como ubicación. Pulsar `Validar`.
    * **Resultado esperado:** El sistema informa del error de coincidencia telefónica, invita al usuario a revisar los datos y **regresa al Panel 1** para su modificación.

* **PA-53: Reintento por Ubicación no encontrada en el contrato**
    * **Prueba:** Paso 6d de alternativas.
    * **Flujo Interacción y datos a introducir:**
        * Panel 1:
            * Número origen: `666555777`
            * Nº Contrato: `C-20241001`
            * NIF/CIF: `89562315A`
            * Teléfono contacto: `612345678`
            * Pulsa `Validar`.
        * Panel 2: 
            * Marcar "Coincide" en Nombre.
            * El sistema pre-marca "Coincide" en el radio button de teléfono. 
            * En el combo desplegable de ubicación, seleccionar "No hay ubicación coincidente". Pulsar `Validar`.
    * **Resultado esperado:** El sistema muestra un aviso indicando que la dirección no corresponde a ninguna ubicación contractual autorizada, invita al usuario a revisar los datos y **regresa al Panel 1**.

**Flujos de Éxito y Finalización**

* **PA-54: Registro de Consulta (Flujo Principal Completo) - Contrato Particular**
    * **Prueba:** Pasos 8 y 9 de la secuencia principal.
    * **Flujo Interacción y datos a introducir:**
        * Panel 1:
            * Número origen: `666555777`
            * Nº Contrato: `C-20241001`
            * NIF/CIF: `89562315A`
            * Teléfono contacto: `612345678`
            * Pulsa `Validar`.
        * Panel 2: Marcar "Coincide" en Nombre. Dejar marcado "Coincide" en Teléfono. Seleccionar "C/ Santiago, 22, Madrid" en ubicación. Pulsar `Validar`.
        * Panel 3 (Motivo): Pulsar el botón "Consulta".
    * **Resultado esperado:** El sistema crea con éxito una Llamada Correcta vinculada al contrato, guarda la información temporal/duración, informa de el almacenamiento de la llamada y finaliza, permitiendo volver al menú o cerrar el programa únicamente.

* **PA-55: Registro de Consulta - Contrato Empresarial con múltiples ubicaciones**
    *   **Prueba:** Pasos 8 y 9 de la secuencia principal (contrato empresarial).
    *   **Flujo Interacción y datos a introducir:**
        *   Panel 1: 
            * Número origne:`666555888`
            * Nº Contrato:`C-EMP-001`
            * NIF/CIF:`B87654321`
            * Teléfono contacto:`910000000`
            * Pulsar `Validar`.
        * Panel 2: Marcar "Coincide" en Nombre/Razón Social. Dejar marcado "Coincide" en Teléfono. En el desplegable de ubicación, se muestran múltiples direcciones asociadas al contrato. Seleccionar una de ellas (ej. "C/ Comercio, 45, Madrid"). Pulsar `Validar`.
        * Panel 3 (Motivo): Pulsar el botón "Consulta".
    * **Resultado esperado:** El sistema crea con éxito una Llamada Correcta vinculada al contrato empresarial, guarda la información y regresa al menú del Coordinador.

* **PA-56: Integración con Registrar Incidencia**
    * **Prueba:** Paso 8a de alternativas.
    * **Flujo Interacción y datos a introducir:**
        * Panel 1:
            * Número origen: `666555777`
            * Nº Contrato: `C-20241001`
            * NIF/CIF: `89562315A`
            * Teléfono contacto: `612345678`
            * Pulsa `Validar`.
        * Panel 2: Marcar "Coincide" en Nombre y Teléfono. Seleccionar "C/ Santiago, 22, Madrid" en ubicación. Pulsar `Validar`.
        * Panel 3 (Motivo): Seleccionar la opción "Incidencia".
        * En la sub-interfaz emergente "Incidencia": Visualizar el texto informativo y pulsar el botón "Finalizar".
    *   **Resultado esperado:** Muestra el mensaje "Not implemented yet", guarda la llamada y permite cerrar el programa o volver al menú 

* **PA-57: Reintento por nombre (falla) y éxito posterior**
    * **Prueba:** Paso 6b seguido de flujo feliz.
    * **Flujo Interacción y datos a introducir:**
        * **Intento 1 (Fallo):** Panel 1: `666555777`, `C-20241001`, `89562315A`, `612345678`. Pulsa `Validar`. Panel 2: Selecciona "No coincide" en Nombre. Pulsa `Validar`. El sistema regresa al Panel 1.
        * **Intento 2 (Éxito):** Mantener los mismos datos en Panel 1. Pulsa `Validar`. Panel 2: Ahora selecciona "Coincide" en Nombre, "Coincide" en Teléfono y una ubicación válida. Pulsa `Validar`. Panel 3: Pulsa "Consulta".
    * **Resultado esperado:** El sistema registra correctamente la llamada en el histórico tras el segundo intento.

* **PA-58: Reintento por teléfono (falla) y éxito posterior**
    * **Prueba:** Paso 6c seguido de flujo feliz.
    * **Flujo Interacción y datos a introducir:**
        * **Intento 1 (Fallo):** Panel 1: `666555777`, `C-20241001`, `89562315A`, `611111111`. Pulsa `Validar`. Panel 2: El sistema pre-marca "No coincide" en Teléfono. Pulsa `Validar`. El sistema regresa al Panel 1.
        * **Intento 2 (Éxito):** Corregir el teléfono a `612345678` en el Panel 1. Pulsa `Validar`. Panel 2: Aceptar los valores por defecto ("Coincide" en ambos) y seleccionar una ubicación válida. Pulsa `Validar`. Panel 3: Pulsa "Consulta".
    * **Resultado esperado:** El sistema registra correctamente la llamada en el histórico tras el segundo intento.

* **PA-59: Reintento por ubicación (falla) y éxito posterior**
    * **Prueba:** Paso 6d seguido de flujo feliz.
    * **Flujo Interacción y datos a introducir:**
        * **Intento 1 (Fallo):** Panel 1: `666555777`, `C-20241001`, `89562315A`, `612345678`. Pulsa `Validar`. Panel 2: Marcar "Coincide" en Nombre y Teléfono. Seleccionar "No hay ubicación coincidente". Pulsa `Validar`. El sistema regresa al Panel 1.
        * **Intento 2 (Éxito):** Mantener los mismos datos en Panel 1. Pulsa `Validar`. Panel 2: Seleccionar "C/ Santiago, 22, Madrid" como ubicación. Pulsa `Validar`. Panel 3: Pulsa "Consulta".
    * **Resultado esperado:** El sistema registra correctamente la llamada en el histórico tras el segundo intento.


**Cancelación y Salida (Control de flujo)**

* **PA-60: Cancelación desde Panel 1 sin datos introducidos**
    * **Prueba:** Control de flujo interactivo.
    * **Flujo Interacción y datos a introducir:** Con la ventana recién abierta en el Panel 1, pulsar directamente el botón "Cancelar".
    * **Resultado esperado:** La ventana se cierra y el sistema regresa al menú de inicio del Coordinador sin escribir ninguna fila ni cambios en la base de datos.

* **PA-61: Cancelación desde Panel 1 con campos modificados**
    * **Prueba:** Control de flujo interactivo.
    * **Flujo Interacción y datos a introducir:** Escribir `666555777` en el campo "Teléfono origen" del Panel 1 y, antes de validar nada, pulsar el botón "Cancelar".
    * **Resultado esperado:** El caso de uso aborta de inmediato redirigiendo al menú principal sin registrar llamadas huérfanas en el sistema.

**Acceso denegado por cambio de responsabilidad (Precondiciones)**

* **PA-62: Intento de acceso por rol Técnico**
    * **Prueba:** Verificación de Actor.
    * **Flujo Interacción y datos a introducir:** Cerrar sesión (si es necesario) e iniciar sesión en la aplicación con credenciales de Técnico:
        * NIF: `34567890C`
        * Contraseña: `passLaura`
    * **Resultado esperado:** La pantalla de inicio del Técnico carga su propio entorno de trabajo y carece por completo de botones o accesos para activar el caso de uso "Atender llamada".

* **PA-63: Intento de acceso por rol Supervisor**
    * **Prueba:** Verificación de Actor.
    * **Flujo Interacción y datos a introducir:** Cerrar sesión e iniciar sesión con credenciales de Supervisor:
        * NIF: `23169952M`
        * Contraseña: `bV4eT5bD`
    * **Resultado esperado:** Carga la vista de gestión del Supervisor, asegurando que la opción de "Atender llamada" no esté disponible en sus opciones de menú.

**Pruebas adicionales de campos vacíos al pulsar "Validar"**

* **PA-64: Reintento tras fallo de nombre con modificación de NIF en Panel 1**
    * **Prueba:** Paso 6b seguido de corrección y éxito.
    * **Flujo Interacción y datos a introducir:**
        * **Intento 1 (Fallo):** Panel 1: `666555777`, `C-20241001`, `89562315A`, `612345678`. Pulsa `Validar`. Panel 2: Selecciona "No coincide" en Nombre. Pulsa `Validar`. El sistema regresa al Panel 1.
        * **Intento 2 (Éxito con modificación):** En Panel 1, cambiar el NIF a `89562315A` (el mismo, simulando corrección). Pulsa `Validar`. Panel 2: Ahora el sistema muestra los datos del cliente correcto. Seleccionar "Coincide" en Nombre, "Coincide" en Teléfono y una ubicación válida. Pulsa `Validar`. Panel 3: Pulsa "Consulta".
    * **Resultado esperado:** El sistema registra correctamente la llamada en el histórico tras el segundo intento, demostrando que la vuelta al Panel 1 permite modificar los datos de identificación.
dio button de teléfono, sin necesidad de intervención del usuario.

--- 