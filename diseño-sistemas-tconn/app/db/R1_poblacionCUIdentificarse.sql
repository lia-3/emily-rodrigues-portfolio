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
--  PERSONAS Y EMPLEADOS
--- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% ---

INSERT INTO Persona (id_persona, nombre, apellidos, telefono_contacto, direccion, nif) VALUES
(1, 'Estefania', 'García', 600111222, ROW('Calle Mayor', 1, 1, 'A', 28001, 'Madrid', 'Madrid'), '64683961A'),
(2, 'Esteban',   'López',  600333444, ROW('Av. Libertad', 10, 2, 'B', 28002, 'Madrid', 'Madrid'), '23169952M'),
(3, 'Laura',     'Sanz',   600555666, ROW('Calle Luna', 5, 0, 'C', 28003, 'Madrid', 'Madrid'), '34567890C'),
(4, 'Ana',       'Ruiz',   600777888, ROW('Calle Sol', 12, 4, 'A', 28004, 'Madrid', 'Madrid'), '44556677H'),
(5, 'Carlos',    'Gómez',  600999000, ROW('Calle Pez', 3, 1, 'B', 28005, 'Madrid', 'Madrid'), '11223344J'),
(6, 'Lucia',     'Vila',   611222333, ROW('Calle Real', 8, 2, 'A', 28006, 'Madrid', 'Madrid'), '99887766K'),
(7, 'Pedro',     'Mora',   611444555, ROW('Calle Norte', 15, 3, 'D', 28007, 'Madrid', 'Madrid'), '55443322L');

INSERT INTO Empleado (id_persona, fecha_incorporacion, fecha_fin_contrato, password) VALUES
(1, '2019-06-01', NULL,         'iE1bF1eD'),
(2, '2022-06-01', NULL,         'bV4eT5bD'),
(3, '2021-05-10', NULL,         'passLaura'),
(4, '2019-04-01', NULL,         'passAna'),
(5, '2020-01-10', '2025-06-01', 'passCarlos'), 
(6, '2022-09-01', NULL,         'passLucia'),
(7, '2023-03-15', NULL,         'passPedro');


--- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% ---
--  PUESTOS Y ROLES
--- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% ---

INSERT INTO Puesto (id_puesto, id_empleado, fecha_inicio) VALUES
(1, 1, '2019-06-01'), 
(2, 2, '2022-06-01'), 
(3, 3, '2021-05-10'), 
(4, 4, '2019-04-01'),
(5, 5, '2020-01-10'), 
(6, 6, '2022-09-01'), 
(7, 7, '2023-03-15');

INSERT INTO Coordinador (id_puesto) VALUES (1), (5);
INSERT INTO Supervisor (id_puesto) VALUES (2), (6);
INSERT INTO Tecnico (id_puesto, nivel_tecnico) VALUES (3, 'avanzado'), (4, 'basico'), (7, 'especializado');

--- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% ---
--  VINCULACIONES Y DISPONIBILIDADES
--- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% ---

INSERT INTO VinculacionesConlaEmpresa (id_empleado, inicio, vinculo) VALUES
(1, '2019-06-01', 'CONTRATADO'), 
(1, '2020-03-01', 'EN_ERTE'), 
(1, '2021-01-15', 'CONTRATADO'),
(2, '2022-06-01', 'CONTRATADO'), 
(2, '2023-01-01', 'EN_ERTE'), 
(2, '2023-09-01', 'CONTRATADO'),
(3, '2021-05-10', 'CONTRATADO'), 
(4, '2019-04-01', 'CONTRATADO'), 
(4, '2024-02-01', 'EN_ERTE'),
(5, '2020-01-10', 'CONTRATADO'), 
(5, '2025-06-01', 'DESPEDIDO'), 
(6, '2022-09-01', 'CONTRATADO'),
(7, '2023-03-15', 'CONTRATADO');

INSERT INTO Disponibilidades (id_empleado, comienzo, fin, disponibilidad) VALUES
(1, '2019-06-01', '2020-02-28', 'TRABAJANDO'),
(1, '2021-01-15', '2023-05-31', 'TRABAJANDO'),
(1, '2023-06-01', '2023-08-31', 'BAJA_TEMPORAL'),
(1, '2023-09-01', NULL,         'TRABAJANDO'),
(2, '2022-06-01', '2022-12-31', 'TRABAJANDO'),
(2, '2023-09-01', '2025-07-31', 'TRABAJANDO'),
(2, '2025-08-01', '2025-08-31', 'VACACIONES'),
(2, '2025-09-01', NULL,         'TRABAJANDO'),
(3, '2021-05-10', '2022-01-09', 'TRABAJANDO'),
(3, '2022-01-10', '2022-03-01', 'BAJA_TEMPORAL'),
(3, '2022-08-01', NULL,         'TRABAJANDO'),
(4, '2019-04-01', '2024-01-31', 'TRABAJANDO'),
(5, '2020-01-10', '2025-05-31', 'TRABAJANDO'),
(6, '2022-09-01', '2026-03-31', 'TRABAJANDO'),
(6, '2026-04-01', NULL,         'VACACIONES'),
(7, '2023-03-15', '2026-02-28', 'TRABAJANDO'),
(7, '2026-03-01', NULL,         'BAJA_TEMPORAL');