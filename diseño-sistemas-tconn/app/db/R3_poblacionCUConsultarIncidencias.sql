--- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% ---
--  LIMPIEZA DE DATOS 
--- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% ---
TRUNCATE TABLE 
    AsignacionTecnicoOrdenDeServicio, OrdenDeServicio, CambioDeSituacionTecnico, 
    TurnoTecnico, TurnoCoordinador, Mantenimiento, Instalacion, AccionDeServicio, 
    CitaPrevia, Incidencia, LlamadaInconsistente, LlamadaCorrecta, LlamadaContrato, 
    LlamadaIncorrecta, Llamada, Supervisor, Tecnico, Coordinador, Puesto, 
    VinculacionesConlaEmpresa, Disponibilidades, /*RolesEnEmpresa, Empleados,*/ Empleado, Persona, 
    Base, ZonaGeografica RESTART IDENTITY CASCADE;

--- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% ---
--  SUPERVISOR PARA EL LOGIN   user del login --> 12345678A  pass123
--- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% ---

INSERT INTO Persona (id_persona, nombre, apellidos, telefono_contacto, direccion, nif) VALUES
(1, 'Ulises', 'Montevideo', 600111222, ROW('Calle Mayor', 1, 1, 'A', 28001, 'Madrid', 'Madrid'), '12345678A'),
(2, 'Octavio', 'Mendoza', 698765432, ROW('Avenida Salamanca', 22, 2, 'B', 47005, 'Valladolid', 'Valladolid'), '87654321B'),
(3, 'Geremias', 'Beltran', 611223344, ROW('Paseo Zorrilla', 45, 3, 'C', 47006, 'Valladolid', 'Valladolid'), '44556677C');


INSERT INTO Empleado (id_persona, fecha_incorporacion, password) VALUES
(1, '2019-06-01', 'pass123'),
(3, '2019-06-02', 'pass321'); 


INSERT INTO Puesto (id_puesto, id_empleado, fecha_inicio) VALUES
(1, 1, '2019-06-01'),
(3, 3, '2019-06-02');

INSERT INTO Supervisor (id_puesto) VALUES (1);
INSERT INTO Cliente (id_persona) VALUES (2);
INSERT INTO Coordinador (id_puesto) VALUES (3);


INSERT INTO VinculacionesConlaEmpresa (id_empleado, inicio, vinculo) VALUES
(1, '2019-06-01', 'CONTRATADO'),
(3, '2019-06-02', 'CONTRATADO');

INSERT INTO Disponibilidades (id_empleado, comienzo, fin, disponibilidad) VALUES
(1, '2019-06-01', NULL, 'TRABAJANDO'),
(3, '2019-06-02', NULL, 'TRABAJANDO');

INSERT INTO Contrato (id_contrato, numero_contrato, fecha_inicio, ubicacion, activo, id_cliente)
VALUES (1, 'CONT-001', '2026-01-01', ROW('Calle Mayor', 1, 1, 'A', 28001, 'Madrid', 'Madrid'), TRUE, 2);

--- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% ---
--  LLAMADAS
--- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% ---

INSERT INTO Llamada (id_llamada, fecha, numero_telefono, hora, duracion, id_coordinador)
VALUES
(1, '2026-05-01', 698765432, '08:15:00', 12, 3),
(2, '2026-05-01', 698765432, '08:30:00', 8, 3),
(3, '2026-05-01', 698765432, '08:00:00', 10, 3),
(4, '2026-05-01', 698765432, '09:00:00', 15, 3),
(5, '2026-05-01', 698765432, '10:15:00', 5,  3),
(6, '2026-05-01', 698765432, '11:30:00', 20, 3),
(7, '2026-05-01', 698765432, '12:45:00', 7,  3);

INSERT INTO LlamadaContrato ( id_llamada, id_contrato)
VALUES
(1, 1), (2, 1), (3, 1), (4, 1), (5, 1), (6, 1), (7, 1);

INSERT INTO LlamadaCorrecta (id_llamada)
VALUES
(1), (2), (3), (4), (5), (6), (7);

--- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% ---
--  INCIDENCIAS 2 resueltas remotamente, 2 resueltas in situ y 3 no resueltas una de cada tipo
--- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% ---

INSERT INTO CitaPrevia (id_cita, codigo, fecha, hora) 
VALUES (1, 5001, '2026-05-02', '10:00:00');

INSERT INTO AccionDeServicio (id_accion, direccion_intervencion, clase_intervencion, fecha_creacion, id_cita_previa) VALUES
(1, ROW('Calle Mayor', 1, 1, 'A', 28001, 'Madrid', 'Madrid'), 'primera', '2026-05-01', NULL),
(2, ROW('Calle Mayor', 1, 1, 'A', 28001, 'Madrid', 'Madrid'), 'escalado', '2026-05-01', NULL),
(3, ROW('Avenida Salamanca', 22, 2, 'B', 47005, 'Valladolid', 'Valladolid'), 'segundaVisita', '2026-05-01', NULL),
(4, ROW('Avenida Salamanca', 22, 2, 'B', 47005, 'Valladolid', 'Valladolid'), 'primera', '2026-05-01', NULL),
(5, ROW('Paseo Zorrilla', 45, 3, 'C', 47006, 'Valladolid', 'Valladolid'), 'primera', '2026-05-01', NULL),
(6, ROW('Paseo Zorrilla', 45, 3, 'C', 47006, 'Valladolid', 'Valladolid'), 'segundaVisita', '2026-05-01', NULL),
(7, ROW('Paseo Zorrilla', 45, 3, 'C', 47006, 'Valladolid', 'Valladolid'), 'escalado', '2026-05-01', 1); -- Esta tiene cita

INSERT INTO Mantenimiento (id_accion, tipo) VALUES
(1, 'incidencia'), 
(2, 'incidencia'), 
(3, 'incidencia'),
(4, 'incidencia'), 
(5, 'incidencia'), 
(6, 'incidencia'),
(7, 'incidencia');

INSERT INTO Incidencia (id_incidencia, id_mantenimiento, codigo, descripcion, fecha, criticidad, estado, tiempo_total_resolucion_minutos, id_llamada_correcta)
VALUES
-- 2 Resueltas remotamente
(1, 1, 1001, 'Router sin conexion', '2026-05-01', 'alta', 'resueltaRemotamente', NULL, 1),
(2, 2, 1002, 'No carga el google', '2026-05-01', 'baja', 'resueltaRemotamente', NULL, 2),
-- 2 Resueltas in situ
(3, 3, 1003, 'Va muy lento', '2026-05-01', 'media', 'resueltaInSitu', NULL, 3),
(4, 4, 1004, 'El wifi no va', '2026-05-01', 'critica', 'resueltaInSitu', NULL, 4),
-- 3 No resueltas (una de cada tipo según tu estado)
(5, 5, 1005, 'Otra vez el mismo', '2026-05-01', 'alta', 'sinResolver', NULL, 5),
(6, 6, 1006, 'Otro router roto', '2026-05-01', 'baja', 'parcialmenteResuelta', NULL, 6),
(7, 7, 1007, 'Tiro el router por la ventana', '2026-05-01', 'media', 'escaladaAEspecialista', NULL, 7);

-- ===================================================================================================
-- CASO 1: '2026-05-02' -> NO hay remotas. SÍ hay "In Situ" y SÍ hay de todas las "Aún Sin Resolver"
-- ===================================================================================================

-- Llamadas del día 02
INSERT INTO Llamada (id_llamada, fecha, numero_telefono, hora, duracion, id_coordinador) VALUES
(8,  '2026-05-02', 698765432, '09:00:00', 10, 3),
(9,  '2026-05-02', 698765432, '10:00:00', 15, 3),
(10, '2026-05-02', 698765432, '11:00:00', 12, 3),
(11, '2026-05-02', 698765432, '12:00:00', 8,  3);

INSERT INTO LlamadaContrato (id_llamada, id_contrato) VALUES
(8, 1), (9, 1), (10, 1), (11, 1);

INSERT INTO LlamadaCorrecta (id_llamada) VALUES
(8), (9), (10), (11);

-- Acciones de servicio y Mantenimientos del día 02
INSERT INTO AccionDeServicio (id_accion, direccion_intervencion, clase_intervencion, fecha_creacion, id_cita_previa) VALUES
(8,  ROW('Calle Mayor', 1, 1, 'A', 28001, 'Madrid', 'Madrid'), 'primera', '2026-05-02', NULL),
(9,  ROW('Calle Mayor', 1, 1, 'A', 28001, 'Madrid', 'Madrid'), 'primera', '2026-05-02', NULL),
(10, ROW('Calle Mayor', 1, 1, 'A', 28001, 'Madrid', 'Madrid'), 'primera', '2026-05-02', NULL),
(11, ROW('Calle Mayor', 1, 1, 'A', 28001, 'Madrid', 'Madrid'), 'primera', '2026-05-02', NULL);

INSERT INTO Mantenimiento (id_accion, tipo) VALUES
(8, 'incidencia'), (9, 'incidencia'), (10, 'incidencia'), (11, 'incidencia');

-- Incidencias del día 02
INSERT INTO Incidencia (id_incidencia, id_mantenimiento, codigo, descripcion, fecha, criticidad, estado, id_llamada_correcta) VALUES
(8,  8,  1008, 'Corte físico de fibra (In Situ)', '2026-05-02', 'alta',    'resueltaInSitu',        8),
(9,  9,  1009, 'Revisión pendiente (Sin Resolver)', '2026-05-02', 'media',   'sinResolver',           9),
(10, 10, 1010, 'Configuración a medias (Parcial)',  '2026-05-02', 'baja',    'parcialmenteResuelta',  10),
(11, 11, 1011, 'Problema de firmware (Escalada)',   '2026-05-02', 'critica', 'escaladaAEspecialista', 11);


-- ===================================================================================================
-- CASO 2: '2026-05-03' -> NO hay "In Situ". SÍ hay remotas y SÍ hay de todas las "Aún Sin Resolver"
-- ===================================================================================================

-- Llamadas del día 03
INSERT INTO Llamada (id_llamada, fecha, numero_telefono, hora, duracion, id_coordinador) VALUES
(12, '2026-05-03', 698765432, '09:30:00', 5,  3),
(13, '2026-05-03', 698765432, '10:30:00', 20, 3),
(14, '2026-05-03', 698765432, '11:30:00', 14, 3),
(15, '2026-05-03', 698765432, '12:30:00', 9,  3);

INSERT INTO LlamadaContrato (id_llamada, id_contrato) VALUES
(12, 1), (13, 1), (14, 1), (15, 1);

INSERT INTO LlamadaCorrecta (id_llamada) VALUES
(12), (13), (14), (15);

-- Acciones de servicio y Mantenimientos del día 03
INSERT INTO AccionDeServicio (id_accion, direccion_intervencion, clase_intervencion, fecha_creacion, id_cita_previa) VALUES
(12, ROW('Avenida Salamanca', 22, 2, 'B', 47005, 'Valladolid', 'Valladolid'), 'primera', '2026-05-03', NULL),
(13, ROW('Avenida Salamanca', 22, 2, 'B', 47005, 'Valladolid', 'Valladolid'), 'primera', '2026-05-03', NULL),
(14, ROW('Avenida Salamanca', 22, 2, 'B', 47005, 'Valladolid', 'Valladolid'), 'primera', '2026-05-03', NULL),
(15, ROW('Avenida Salamanca', 22, 2, 'B', 47005, 'Valladolid', 'Valladolid'), 'primera', '2026-05-03', NULL);

INSERT INTO Mantenimiento (id_accion, tipo) VALUES
(12, 'incidencia'), (13, 'incidencia'), (14, 'incidencia'), (15, 'incidencia');

-- Incidencias del día 03
INSERT INTO Incidencia (id_incidencia, id_mantenimiento, codigo, descripcion, fecha, criticidad, estado, id_llamada_correcta) VALUES
(12, 12, 1012, 'Reinicio de ONT remoto (Remota)',    '2026-05-03', 'baja',    'resueltaRemotamente',   12),
(13, 13, 1013, 'Avería masiva de zona (Sin Resolver)', '2026-05-03', 'critica', 'sinResolver',           13),
(14, 14, 1014, 'Saturación de canal (Parcial)',       '2026-05-03', 'media',   'parcialmenteResuelta',  14),
(15, 15, 1015, 'Fallo de autenticación (Escalada)',   '2026-05-03', 'alta',    'escaladaAEspecialista', 15);


-- ===================================================================================================
-- CASO 3: '2026-05-04' -> NO hay "Aún Sin Resolver". SÍ hay remotas y SÍ hay "In Situ"
-- ===================================================================================================

-- Llamadas del día 04
INSERT INTO Llamada (id_llamada, fecha, numero_telefono, hora, duracion, id_coordinador) VALUES
(16, '2026-05-04', 698765432, '08:45:00', 11, 3),
(17, '2026-05-04', 698765432, '11:15:00', 18, 3);

INSERT INTO LlamadaContrato (id_llamada, id_contrato) VALUES
(16, 1), (17, 1);

INSERT INTO LlamadaCorrecta (id_llamada) VALUES
(16), (17);

-- Acciones de servicio y Mantenimientos del día 04
INSERT INTO AccionDeServicio (id_accion, direccion_intervencion, clase_intervencion, fecha_creacion, id_cita_previa) VALUES
(16, ROW('Paseo Zorrilla', 45, 3, 'C', 47006, 'Valladolid', 'Valladolid'), 'primera', '2026-05-04', NULL),
(17, ROW('Paseo Zorrilla', 45, 3, 'C', 47006, 'Valladolid', 'Valladolid'), 'primera', '2026-05-04', NULL);

INSERT INTO Mantenimiento (id_accion, tipo) VALUES
(16, 'incidencia'), (17, 'incidencia');

-- Incidencias del día 04
INSERT INTO Incidencia (id_incidencia, id_mantenimiento, codigo, descripcion, fecha, criticidad, estado, id_llamada_correcta) VALUES
(16, 16, 1016, 'Cambio de DNS telefónico (Remota)', '2026-05-04', 'baja', 'resueltaRemotamente', 16),
(17, 17, 1017, 'Sustitución cable RJ45 (In Situ)',   '2026-05-04', 'media', 'resueltaInSitu',       17);