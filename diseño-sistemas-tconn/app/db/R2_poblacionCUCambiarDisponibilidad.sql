-- ================================================================================================
--  USUARIOS DE PRUEBA
--  ──────────────────────────────────────────────────────────────────────────────────────────────
--  NIF           Nombre         Contraseña    PA cubiertos
--  34567890C     Laura Sanz     passLaura     PA-13  (cancela antes de indicar que ha llegado a la base)
--                                             PA-14  (cancela antes de confirmar el cambio de estado)
--                                             PA-15  (asignada → disponible)
--                                             PA-16  (asignada → no disponible, sin RN-3)
--  55443322L     Pedro Mora     passPedro     PA-17  (fuera de turno)
--  44556677H     Ana Ruiz       passAna       PA-18  (en turno pero no asignada)
--  87654321D     Marcos Gil     passMarcos    PA-19  (asignado, base lejana → FueraBase)
--  98765432E     Elena Vega     passElena     PA-20  (asignada, RN-3 actúa → fuerza disponible)
--                                             PA-21  (tras PA-20: intenta noDisponible de nuevo, RN-3 repite)
--                                             PA-22  (tras PA-20: intenta disponible de nuevo → duplicado)
--  64683961A     Estefania García iE1bF1eD   PA-23  (Coordinadora, acceso denegado)
--  23169952M     Esteban López  bV4eT5bD     PA-24  (Supervisor, acceso denegado)


-- ================================================================================================
--  LIMPIEZA Y PREPARACIÓN
-- ================================================================================================
TRUNCATE TABLE
    AsignacionTecnicoOrdenDeServicio, OrdenDeServicio, CambioDeSituacionTecnico,
    TurnoTecnico, TurnoCoordinador, Mantenimiento, Instalacion, AccionDeServicio,
    CitaPrevia, Incidencia, LlamadaInconsistente, LlamadaCorrecta, LlamadaContrato,
    LlamadaIncorrecta, Llamada, Supervisor, Tecnico, Coordinador, Puesto,
    VinculacionesConlaEmpresa, Disponibilidades, /*RolesEnEmpresa, Empleados,*/ Empleado, Persona,
    Base, ZonaGeografica RESTART IDENTITY CASCADE;


-- ================================================================================================
--  ZONAS Y BASES
-- ================================================================================================
INSERT INTO ZonaGeografica (id_zona) VALUES
    ('ZONA_CENTRO'),    -- PA-13, PA-14, PA-15, PA-16 y técnicos de fondo
    ('ZONA_SUR'),       -- PA-20, PA-21, PA-22  (< 2 disponibles → RN-3 actúa)
    ('ZONA_LEJANA');    -- PA-19  (Tenerife, garantiza distancia > 100 km desde peninsular)

-- NOTA: ajustar latitud/longitud de ZONA_CENTRO y ZONA_SUR según la red de pruebas.
INSERT INTO Base (id_base, direccion, ubicacion, id_zona) VALUES
(1,
 ROW('Paseo Belén', 1, 0, 'A', 28013, 'Valladolid', 'Valladolid')::tipo_direccion,
 ROW(42.5487, -6.5935)::tipo_coordenada_gps,
 'ZONA_CENTRO'),
(2,
 ROW('Paseo Belén', 15, 0, 'B', 28012, 'Valladolid', 'Valladolid')::tipo_direccion,
 ROW(42.5487, -6.5935)::tipo_coordenada_gps,
 'ZONA_SUR'),
(3,
 ROW('Carretera TF-1', 100, 0, 'A', 38010, 'Santa Cruz de Tenerife', 'Santa Cruz de Tenerife')::tipo_direccion,
 ROW(28.46, -16.25)::tipo_coordenada_gps,
 'ZONA_LEJANA');


-- ================================================================================================
--  PERSONAS
-- ================================================================================================
INSERT INTO Persona (id_persona, nombre, apellidos, telefono_contacto, direccion, nif) VALUES
-- Coordinadora (PA-23)
(1,  'Estefania', 'García',  600111222, ROW('Calle Mayor',    1,  1, 'A', 28001, 'Madrid', 'Madrid')::tipo_direccion, '64683961A'),
-- Supervisor (PA-24)
(2,  'Esteban',   'López',   600333444, ROW('Av. Libertad',  10,  2, 'B', 28002, 'Madrid', 'Madrid')::tipo_direccion, '23169952M'),
-- PA-13/14/15/16: ZONA_CENTRO, en turno hoy, último estado = asignado
(3,  'Laura',     'Sanz',    600555666, ROW('Calle Luna',     5,  0, 'C', 28003, 'Madrid', 'Madrid')::tipo_direccion, '34567890C'),
-- PA-17: ZONA_CENTRO, turno caducado (ayer)
(5,  'Pedro',     'Mora',    611444555, ROW('Calle Norte',   15,  3, 'D', 28007, 'Madrid', 'Madrid')::tipo_direccion, '55443322L'),
-- PA-18: ZONA_CENTRO, en turno hoy, último estado = disponible (no asignada)
(6,  'Ana',       'Ruiz',    600777888, ROW('Calle Sol',     12,  4, 'A', 28004, 'Madrid', 'Madrid')::tipo_direccion, '44556677H'),
-- PA-19: ZONA_LEJANA, en turno hoy, último estado = asignado, pero base en Tenerife
(7,  'Marcos',    'Gil',     622300400, ROW('Av. Canarias',   7,  0, 'A', 38001, 'Santa Cruz de Tenerife', 'Santa Cruz de Tenerife')::tipo_direccion, '87654321D'),
-- PA-20/21/22: ZONA_SUR, en turno hoy, último estado = asignado (RN-3 actúa)
(8,  'Elena',     'Vega',    633500600, ROW('Calle Sur',     22,  2, 'C', 28020, 'Madrid', 'Madrid')::tipo_direccion, '98765432E'),
-- Técnicos de fondo: disponibles en ZONA_CENTRO para que PA-16 no dispare RN-3
(9,  'Sonia',     'Díaz',    644600700, ROW('Calle Oeste',    3,  0, 'B', 28015, 'Madrid', 'Madrid')::tipo_direccion, '12345678F'),
(10, 'Rubén',     'Cano',    655700800, ROW('Calle Este',     8,  1, 'A', 28028, 'Madrid', 'Madrid')::tipo_direccion, '23456789G');


-- ================================================================================================
--  EMPLEADOS Y CREDENCIALES
-- ================================================================================================
INSERT INTO Empleado (id_persona, fecha_incorporacion, password) VALUES
(1,  '2019-06-01', 'iE1bF1eD'),
(2,  '2022-06-01', 'bV4eT5bD'),
(3,  '2021-05-10', 'passLaura'),
(5,  '2023-03-15', 'passPedro'),
(6,  '2019-04-01', 'passAna'),
(7,  '2022-11-01', 'passMarcos'),
(8,  '2021-07-15', 'passElena'),
(9,  '2020-08-01', 'passSonia'),
(10, '2021-01-10', 'passRuben');

-- ================================================================================================
--  PUESTOS Y ROLES
-- ================================================================================================
INSERT INTO Puesto (id_puesto, id_empleado, fecha_inicio) VALUES
(1,  1,  '2019-06-01'),
(2,  2,  '2022-06-01'),
(3,  3,  '2021-05-10'),
(5,  5,  '2023-03-15'),
(6,  6,  '2019-04-01'),
(7,  7,  '2022-11-01'),
(8,  8,  '2021-07-15'),
(9,  9,  '2020-08-01'),
(10, 10, '2021-01-10');

INSERT INTO Coordinador (id_puesto) VALUES (1);
INSERT INTO Supervisor  (id_puesto) VALUES (2);
INSERT INTO Tecnico (id_puesto, nivel_tecnico) VALUES
(3,  'avanzado'),       -- Laura
(5,  'especializado'),  -- Pedro
(6,  'basico'),         -- Ana
(7,  'avanzado'),       -- Marcos
(8,  'basico'),         -- Elena
(9,  'basico'),         -- Sonia (fondo)
(10, 'basico');         -- Rubén (fondo)

-- ================================================================================================
--  VINCULACIONES CON LA EMPRESA (histórico)
-- ================================================================================================
INSERT INTO VinculacionesConlaEmpresa (id_empleado, inicio, vinculo) VALUES
-- Estefania: ERTE 2020→2021
(1, '2019-06-01', 'CONTRATADO'),
(1, '2020-03-01', 'EN_ERTE'),
(1, '2021-01-15', 'CONTRATADO'),
-- Esteban: ERTE 2023
(2, '2022-06-01', 'CONTRATADO'),
(2, '2023-01-01', 'EN_ERTE'),
(2, '2023-09-01', 'CONTRATADO'),
-- Laura: siempre contratada
(3, '2021-05-10', 'CONTRATADO'),
-- Pedro: siempre contratado (su problema en este CU es el turno, no la vinculación)
(5, '2023-03-15', 'CONTRATADO'),
-- Ana: ERTE reciente 2024→ vuelta
(6, '2019-04-01', 'CONTRATADO'),
(6, '2024-02-01', 'EN_ERTE'),
(6, '2024-10-01', 'CONTRATADO'),
-- Marcos
(7, '2022-11-01', 'CONTRATADO'),
-- Elena: ERTE 2023
(8, '2021-07-15', 'CONTRATADO'),
(8, '2023-04-01', 'EN_ERTE'),
(8, '2023-11-01', 'CONTRATADO'),
-- Fondo
(9,  '2020-08-01', 'CONTRATADO'),
(10, '2021-01-10', 'CONTRATADO');


-- ================================================================================================
--  DISPONIBILIDADES (histórico)
-- ================================================================================================
INSERT INTO Disponibilidades (id_empleado, comienzo, fin, disponibilidad) VALUES
-- Estefania: baja temporal durante el ERTE
(1, '2019-06-01', '2020-02-28', 'TRABAJANDO'),
(1, '2021-01-15', '2023-05-31', 'TRABAJANDO'),
(1, '2023-06-01', '2023-08-31', 'BAJA_TEMPORAL'),
(1, '2023-09-01', NULL,         'TRABAJANDO'),
-- Esteban: vacaciones verano 2025
(2, '2022-06-01', '2025-07-31', 'TRABAJANDO'),
(2, '2025-08-01', '2025-08-31', 'VACACIONES'),
(2, '2025-09-01', NULL,         'TRABAJANDO'),
-- Laura: baja 2022, vacaciones 2024
(3, '2021-05-10', '2022-01-09', 'TRABAJANDO'),
(3, '2022-01-10', '2022-07-31', 'BAJA_TEMPORAL'),
(3, '2022-08-01', '2024-06-30', 'TRABAJANDO'),
(3, '2024-07-01', '2024-07-31', 'VACACIONES'),
(3, '2024-08-01', NULL,         'TRABAJANDO'),
-- Pedro: TRABAJANDO activo (el problema es sólo que su turno de hoy está caducado)
(5, '2023-03-15', NULL, 'TRABAJANDO'),
-- Ana: TRABAJANDO activo
(6, '2019-04-01', '2024-01-31', 'TRABAJANDO'),
(6, '2024-10-01', NULL,         'TRABAJANDO'),
-- Marcos: vacaciones fin de 2025
(7, '2022-11-01', '2025-11-30', 'TRABAJANDO'),
(7, '2025-12-01', '2025-12-31', 'VACACIONES'),
(7, '2026-01-01', NULL,         'TRABAJANDO'),
-- Elena: baja temporal 2023
(8, '2021-07-15', '2023-03-31', 'TRABAJANDO'),
(8, '2023-04-01', '2023-10-31', 'BAJA_TEMPORAL'),
(8, '2023-11-01', NULL,         'TRABAJANDO'),
-- Fondo
(9,  '2020-08-01', NULL, 'TRABAJANDO'),
(10, '2021-01-10', NULL, 'TRABAJANDO');


-- ================================================================================================
--  TURNOS TÉCNICOS
-- ================================================================================================
INSERT INTO TurnoTecnico (id_turno, fecha_inicio, hora_inicio, fecha_fin, hora_fin, id_tecnico, id_base) VALUES
-- PA-13/14/15/16: Laura, turno activo hoy, ZONA_CENTRO
(1, CURRENT_DATE, '00:00:00', CURRENT_DATE, '23:59:59', 3, 1),
-- PA-17: Pedro, turno CADUCADO (ayer) → FueraTurnoException
(3, CURRENT_DATE - 1, '08:00:00', CURRENT_DATE - 1, '22:00:00', 5, 1),
-- PA-18: Ana, turno activo hoy, ZONA_CENTRO
(4, CURRENT_DATE, '00:00:00', CURRENT_DATE, '23:59:59', 6, 1),
-- PA-19: Marcos, turno activo hoy, ZONA_LEJANA (Tenerife) → FueraBaseException
(5, CURRENT_DATE, '00:00:00', CURRENT_DATE, '23:59:59', 7, 3),
-- PA-20/21/22: Elena, turno activo hoy, ZONA_SUR
(6, CURRENT_DATE, '00:00:00', CURRENT_DATE, '23:59:59', 8, 2),
-- Fondo ZONA_CENTRO: Sonia y Rubén (estado disponible → numDisponibles ≥ 2 para PA-16)
(7, CURRENT_DATE, '00:00:00', CURRENT_DATE, '23:59:59', 9,  1),
(8, CURRENT_DATE, '00:00:00', CURRENT_DATE, '23:59:59', 10, 1);


-- ================================================================================================
--  CAMBIOS DE SITUACIÓN (estado inicial por técnico)
-- ================================================================================================

-- ──────────────────────────────────────────────────────────────────────────────
--  PA-13/14/15/16 │ Laura (turno 1, ZONA_CENTRO)
--  Histórico realista: llegó, fue asignada y vuelve a base.
--  Último estado: ASIGNADO → puede pulsar "Entrar en base".
--  PA-13: cancela antes de indicar que ha llegado (sin cambio de estado)
--  PA-14: cancela antes de confirmar (sin cambio de estado)
--  PA-15: cambia a disponible (flujo principal)
--  PA-16: tras borrar el registro de PA-15, cambia a noDisponible (sin RN-3, Sonia+Rubén cubren)
-- ──────────────────────────────────────────────────────────────────────────────
INSERT INTO CambioDeSituacionTecnico (estado, ubicacion, fecha, hora, id_turno_tecnico) VALUES
('disponible',   ROW(40.41, -3.70)::tipo_coordenada_gps, '2026-05-02', '08:00:00', 1),
('asignado',     ROW(40.42, -3.71)::tipo_coordenada_gps, '2026-05-02', '09:00:00', 1);

-- PA-17 │ Pedro (turno 3, caducado)
-- No hace falta estado de situación: la excepción salta en seEncuentranEnTurno()
-- antes de consultar CambioDeSituacionTecnico.

-- ──────────────────────────────────────────────────────────────────────────────
--  PA-18 │ Ana (turno 4, ZONA_CENTRO)
--  Último estado: DISPONIBLE (no asignada a ninguna incidencia)
--  → seEncuentranEnTurno() lanza NoAsignadoException.
-- ──────────────────────────────────────────────────────────────────────────────
INSERT INTO CambioDeSituacionTecnico (estado, ubicacion, fecha, hora, id_turno_tecnico) VALUES
('disponible',   ROW(40.41, -3.70)::tipo_coordenada_gps, '2026-05-02', '08:00:00', 4);

-- ──────────────────────────────────────────────────────────────────────────────
--  PA-19 │ Marcos (turno 5, ZONA_LEJANA - Tenerife)
--  Último estado: ASIGNADO → pasa seEncuentranEnTurno() correctamente.
--  Pero su base está a ~1700 km → geolocalizar() lanza FueraBaseException.
-- ──────────────────────────────────────────────────────────────────────────────
INSERT INTO CambioDeSituacionTecnico (estado, ubicacion, fecha, hora, id_turno_tecnico) VALUES
('disponible',   ROW(28.46, -16.25)::tipo_coordenada_gps, '2026-05-02', '08:00:00', 5),
('asignado',     ROW(28.47, -16.26)::tipo_coordenada_gps, '2026-05-02', '09:00:00', 5);

-- ──────────────────────────────────────────────────────────────────────────────
--  PA-20 │ Elena (turno 6, ZONA_SUR)
--  Último estado: ASIGNADO. ZONA_SUR tiene 0 disponibles (Elena es la única y
--  está asignada) → numDisponibles = 0 < 2 → RN-3 actúa:
--    · Inserta "disponible" en BD.
--    · Lanza RN3Exception → mensaje "Regla de negocio: Pasa a estar disponible".
--
--  PA-21 (continúa sin recargar BD, tras PA-20):
--    · Elena intenta "disponible" de nuevo → EstadoDuplicadoException.
--    · Resultado: "Ya ha sido actualizado al estado Disponible".
--
--  PA-22 (continúa sin recargar BD, tras PA-20):
--    · Elena intenta "noDisponible" → aplica RN-3:
--      ultimoEstado = "disponible" → lanza RN3Exception sin insertar nada.
--    · Resultado: "Regla de negocio: Pasa a estar disponible".
-- ──────────────────────────────────────────────────────────────────────────────
INSERT INTO CambioDeSituacionTecnico (estado, ubicacion, fecha, hora, id_turno_tecnico) VALUES
('disponible',   ROW(40.40, -3.69)::tipo_coordenada_gps, '2026-05-02', '08:00:00', 6),
('asignado',     ROW(40.39, -3.68)::tipo_coordenada_gps, '2026-05-02', '10:00:00', 6);

-- ──────────────────────────────────────────────────────────────────────────────
--  Fondo ZONA_CENTRO │ Sonia (turno 7) y Rubén (turno 8)
--  Estado DISPONIBLE: hacen que numDisponibles = 2 en ZONA_CENTRO,
--  lo que permite a Laura (PA-16) cambiar a noDisponible sin que actúe RN-3.
-- ──────────────────────────────────────────────────────────────────────────────
INSERT INTO CambioDeSituacionTecnico (estado, ubicacion, fecha, hora, id_turno_tecnico) VALUES
('disponible',   ROW(40.41, -3.70)::tipo_coordenada_gps, '2026-05-02', '08:00:00', 7),
('disponible',   ROW(40.41, -3.70)::tipo_coordenada_gps, '2026-05-02', '08:00:00', 8);