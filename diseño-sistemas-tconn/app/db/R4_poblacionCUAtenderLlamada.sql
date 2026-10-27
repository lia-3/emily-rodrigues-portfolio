-- =============================================================================
-- LIMPIEZA TOTAL DE TABLAS (RESTART IDENTITY para reiniciar SERIALs)
-- =============================================================================
TRUNCATE TABLE 
    AsignacionTecnicoOrdenDeServicio, OrdenDeServicio, CambioDeSituacionTecnico, 
    TurnoTecnico, TurnoCoordinador, Mantenimiento, Instalacion, AccionDeServicio, 
    CitaPrevia, Incidencia, LlamadaInconsistente, LlamadaCorrecta, LlamadaContrato, 
    LlamadaIncorrecta, Llamada, Supervisor, Tecnico, Coordinador, Puesto, 
    VinculacionesConlaEmpresa, Disponibilidades, Empleado, Persona, 
    Base, ZonaGeografica, ContratoEmpresarialUbicaciones, ContratoEmpresarial,
    Contrato, InterlocutoresAutorizados, ClienteEmpresarial, ClienteParticular,
    Cliente
    RESTART IDENTITY CASCADE;

-- =============================================================================
-- 1. INFRAESTRUCTURA BASE Y COORDINADOR (Actor principal del CU)
-- =============================================================================

-- Zona geográfica necesaria para la Base
INSERT INTO ZonaGeografica (id_zona) VALUES ('ZONA_CENTRO');

-- Base del coordinador (no se usa directamente en el CU, pero puede ser necesaria)
INSERT INTO Base (direccion, ubicacion, id_zona) 
VALUES (
    ROW('Calle Principal', 1, 1, 'A', 28001, 'Madrid', 'Madrid')::tipo_direccion,
    ROW(40.4167, -3.7032)::tipo_coordenada_gps,
    'ZONA_CENTRO'
);

-- Persona → Empleado → Puesto → Coordinador (credenciales según PA-01 y PA-54)
INSERT INTO Persona (nombre, apellidos, telefono_contacto, direccion, nif)
VALUES (
    'Carlos', 'Coordinador', 600111222,
    ROW('Calle Admin', 10, 2, 'B', 28002, 'Madrid', 'Madrid')::tipo_direccion,
    '64683961A'  -- NIF del coordinador según pruebas
);

INSERT INTO Empleado (id_persona, fecha_incorporacion, password) 
VALUES (1, '2020-01-01', 'iE1bF1eD');  -- Contraseña según pruebas

INSERT INTO Puesto (id_puesto, id_empleado, fecha_inicio) 
VALUES (100, 1, '2020-01-01');

INSERT INTO Coordinador (id_puesto) VALUES (100);

-- Turno del coordinador (activo para la fecha de las pruebas)
INSERT INTO TurnoCoordinador (fecha_inicio_turno, horario, fecha_fin, id_coordinador)
VALUES ('2026-05-14', 'de7a15h', '2026-05-14', 100);

INSERT INTO VinculacionesConlaEmpresa (id_empleado, inicio, vinculo) 
VALUES (1, '2020-01-01', 'CONTRATADO');

-- Disponibilidad en activo para el Coordinador (id_persona = 1)
INSERT INTO Disponibilidades (id_empleado, comienzo, fin, disponibilidad) 
VALUES (1, '2020-01-01', NULL, 'TRABAJANDO');
-- >>> ============================================================= <<<

-- Turno del coordinador (activo para la fecha de las pruebas)
INSERT INTO TurnoCoordinador (fecha_inicio_turno, horario, fecha_fin, id_coordinador)
VALUES ('2026-05-14', 'de7a15h', '2026-05-14', 100);

-- =============================================================================
-- 2. CLIENTE PARTICULAR VÁLIDO (Éxito PA-48 y PA-49)
-- Datos: nº contrato C-20241001, NIF 89562315A, teléfono contacto 612345678
-- =============================================================================

INSERT INTO Persona (nombre, apellidos, telefono_contacto, direccion, nif)
VALUES (
    'Juan Antonio', 'Pérez García', 612345678,
    ROW('C/ Santiago', 22, 1, 'A', 28001, 'Madrid', 'Madrid')::tipo_direccion,
    '89562315A'  -- NIF según pruebas
);

INSERT INTO Cliente (id_persona) VALUES (2);
INSERT INTO ClienteParticular (id_persona) VALUES (2);

-- Contrato activo (PA-48 usa C-20241001, PA-42 usa C-20241002 como caducado)
INSERT INTO Contrato (numero_contrato, fecha_inicio, fecha_fin, ubicacion, activo, id_cliente)
VALUES (
    'C-20241001', '2024-10-01', NULL, 
    ROW('C/ Santiago', 22, 1, 'A', 28001, 'Madrid', 'Madrid')::tipo_direccion,
    TRUE, 2
);

-- =============================================================================
-- 3. CLIENTE EMPRESARIAL VÁLIDO (Éxito PA-60)
-- Datos: nº contrato C-EMP-001, CIF B87654321, nombre "Tech Solutions S.L."
-- =============================================================================

INSERT INTO Persona (nombre, apellidos, telefono_contacto, direccion, nif)
VALUES (
    'Laura', 'Gómez Fernández', 699999999,
    ROW('C/ Comercio', 45, 2, 'B', 28002, 'Madrid', 'Madrid')::tipo_direccion,
    'B87654321'  -- CIF como NIF en Persona
);

INSERT INTO Cliente (id_persona) VALUES (3);

INSERT INTO ClienteEmpresarial (id_persona, cif, nombre_razon_social, sede, telefono)
VALUES (
    3, 'B87654321', 'Tech Solutions S.L.',
    ROW('C/ Comercio', 45, 2, 'B', 28002, 'Madrid', 'Madrid')::tipo_direccion,
    910000000
);

-- Contrato empresarial activo
INSERT INTO Contrato (numero_contrato, fecha_inicio, fecha_fin, ubicacion, activo, id_cliente)
VALUES (
    'C-EMP-001', '2024-01-01', '2026-12-31',
    ROW('C/ Comercio', 45, 2, 'B', 28002, 'Madrid', 'Madrid')::tipo_direccion,
    TRUE, 3
);

INSERT INTO ContratoEmpresarial (id_contrato, id_cliente_empresarial) 
VALUES (2, 3);

-- Múltiples ubicaciones para probar selección en Panel 2 (PA-60 y flujo 6d)
INSERT INTO ContratoEmpresarialUbicaciones (id_contrato, ubicacion)
VALUES 
    (2, ROW('C/ Comercio', 45, 2, 'B', 28002, 'Madrid', 'Madrid')::tipo_direccion),  -- sede
    (2, ROW('C/ Almacén', 12, 0, 'A', 28003, 'Madrid', 'Madrid')::tipo_direccion),    -- almacén
    (2, ROW('Av. Tecnología', 8, 3, 'C', 28004, 'Madrid', 'Madrid')::tipo_direccion);  -- oficina2

-- =============================================================================
-- 4. CLIENTE PARTICULAR CON CONTRATO CADUCADO (Alternativa 4a / PA-59)
-- Datos: nº contrato C-20241002, NIF 11111111H (no registrado como cliente)
-- =============================================================================

-- Persona sin ser cliente (para probar PA-44: NIF no registrado)
-- Pero primero, creamos el cliente con contrato caducado según especificación
INSERT INTO Persona (nombre, apellidos, telefono_contacto, direccion, nif)
VALUES (
    'Miguel', 'Rodríguez Sánchez', 622333444,
    ROW('C/ Mayor', 33, 2, 'C', 28005, 'Madrid', 'Madrid')::tipo_direccion,
    '11111111H'
);

INSERT INTO Cliente (id_persona) VALUES (4);
INSERT INTO ClienteParticular (id_persona) VALUES (4);

-- Contrato caducado (activo = FALSE)
INSERT INTO Contrato (numero_contrato, fecha_inicio, fecha_fin, ubicacion, activo, id_cliente)
VALUES (
    'C-20241002', '2024-10-01', '2025-04-01',  -- ya caducado
    ROW('C/ Mayor', 33, 2, 'C', 28005, 'Madrid', 'Madrid')::tipo_direccion,
    FALSE, 4
);

-- =============================================================================
-- 5. CLIENTE PARTICULAR VÁLIDO (para PA-45, PA-46, PA-50b, PA-63)
-- Datos: teléfono 612345678 que coincide, para probar pre-selección automática
-- =============================================================================

INSERT INTO Persona (nombre, apellidos, telefono_contacto, direccion, nif)
VALUES (
    'María', 'López Martínez', 612345678,  -- teléfono que coincide
    ROW('Av. Constitución', 7, 1, 'B', 28006, 'Madrid', 'Madrid')::tipo_direccion,
    '89562316B'
);

INSERT INTO Cliente (id_persona) VALUES (5);
INSERT INTO ClienteParticular (id_persona) VALUES (5);

INSERT INTO Contrato (numero_contrato, fecha_inicio, fecha_fin, ubicacion, activo, id_cliente)
VALUES (
    'C-20241003', '2024-10-01', NULL,
    ROW('Av. Constitución', 7, 1, 'B', 28006, 'Madrid', 'Madrid')::tipo_direccion,
    TRUE, 5
);

-- =============================================================================
-- 6. CLIENTE PARTICULAR PARA PROBAR NIF NO REGISTRADO (PA-44)
-- NIF 11111111X no existe en Persona
-- (No se inserta nada, la búsqueda fallará)
-- =============================================================================

-- =============================================================================
-- 7. DATOS ADICIONALES PARA PRUEBAS DE REINTENTO Y COMPROBACIONES
-- Cliente con teléfono diferente (para PA-46, PA-50b)
-- =============================================================================

INSERT INTO Persona (nombre, apellidos, telefono_contacto, direccion, nif)
VALUES (
    'Carlos', 'Ruiz Jiménez', 699888777,  -- teléfono diferente al que se usará en prueba
    ROW('C/ Estrella', 101, 2, 'D', 28007, 'Madrid', 'Madrid')::tipo_direccion,
    '89562317C'
);

INSERT INTO Cliente (id_persona) VALUES (6);
INSERT INTO ClienteParticular (id_persona) VALUES (6);

INSERT INTO Contrato (numero_contrato, fecha_inicio, fecha_fin, ubicacion, activo, id_cliente)
VALUES (
    'C-20241004', '2024-10-01', NULL,
    ROW('C/ Estrella', 101, 2, 'D', 28007, 'Madrid', 'Madrid')::tipo_direccion,
    TRUE, 6
);

-- =============================================================================
-- 8. CLIENTE EMPRESARIAL PARA PRUEBAS DE ALTERNATIVA 6a (NIF asociado a Persona pero no a Cliente)
-- Persona id_persona=7 tiene Persona pero no es Cliente
-- =============================================================================

INSERT INTO Persona (nombre, apellidos, telefono_contacto, direccion, nif)
VALUES (
    'Empresa', 'Sin Contrato S.L.', 666777888,
    ROW('C/ Negocios', 50, 0, 'A', 28008, 'Madrid', 'Madrid')::tipo_direccion,
    'B99999999Z'
);
-- NOTA: NO se inserta en Cliente ni ClienteEmpresarial, por lo que buscar este NIF
-- disparará la alternativa 6a.

-- =============================================================================
-- 9. CLIENTE EMPRESARIAL PARA PRUEBA PA-45 (nombre/razón social no coincide)
-- Se usará el mismo C-EMP-001 pero con un NIF diferente (si el controlador lo permite)
-- O se crea uno específico
-- =============================================================================

INSERT INTO Persona (nombre, apellidos, telefono_contacto, direccion, nif)
VALUES (
    'Ana', 'Martín López', 644555666,
    ROW('C/ Falsa', 123, 0, 'A', 28009, 'Madrid', 'Madrid')::tipo_direccion,
    'B88888888Y'
);

INSERT INTO Cliente (id_persona) VALUES (8);

INSERT INTO ClienteEmpresarial (id_persona, cif, nombre_razon_social, sede, telefono)
VALUES (
    8, 'B88888888Y', 'Consultoría ACME S.L.',
    ROW('C/ Falsa', 123, 0, 'A', 28009, 'Madrid', 'Madrid')::tipo_direccion,
    911222333
);

INSERT INTO Contrato (numero_contrato, fecha_inicio, fecha_fin, ubicacion, activo, id_cliente)
VALUES (
    'C-EMP-005', '2025-01-01', NULL,
    ROW('C/ Falsa', 123, 0, 'A', 28009, 'Madrid', 'Madrid')::tipo_direccion,
    TRUE, 8
);

INSERT INTO ContratoEmpresarial (id_contrato, id_cliente_empresarial) 
VALUES (4, 8);

-- =============================================================================
-- 10. CLIENTE PARA PROBAR ALTERNATIVA 6c (teléfono de contacto no coincide)
-- Teléfono registrado: 677888999 (diferente al que se usará en la prueba)
-- =============================================================================

INSERT INTO Persona (nombre, apellidos, telefono_contacto, direccion, nif)
VALUES (
    'Roberto', 'Sánchez Gil', 677888999,
    ROW('C/ Teléfono', 15, 1, 'A', 28010, 'Madrid', 'Madrid')::tipo_direccion,
    '89562318D'
);

INSERT INTO Cliente (id_persona) VALUES (9);
INSERT INTO ClienteParticular (id_persona) VALUES (9);

INSERT INTO Contrato (numero_contrato, fecha_inicio, fecha_fin, ubicacion, activo, id_cliente)
VALUES (
    'C-20241006', '2024-10-01', NULL,
    ROW('C/ Teléfono', 15, 1, 'A', 28010, 'Madrid', 'Madrid')::tipo_direccion,
    TRUE, 9
);

-- =============================================================================
-- 11. EMPLEADOS PARA PRUEBAS DE PRECONDICIONES / ACTORES DENEGADOS
-- =============================================================================

-- -----------------------------------------------------------------------------
-- PA-57: Rol Técnico (NIF: 34567890C, Contraseña: passLaura)
-- -----------------------------------------------------------------------------
INSERT INTO Persona (nombre, apellidos, telefono_contacto, direccion, nif)
VALUES (
    'Laura', 'Técnica Pruebas', 655111222,
    ROW('Av. Principal', 4, 1, 'C', 47001, 'Valladolid', 'Valladolid')::tipo_direccion,
    '34567890C'
); -- Generará id_persona = 10 (siguiendo el orden del script original)

INSERT INTO Empleado (id_persona, fecha_incorporacion, password) 
VALUES (10, '2025-01-01', 'passLaura');

INSERT INTO Puesto (id_puesto, id_empleado, fecha_inicio) 
VALUES (200, 10, '2025-01-01');

INSERT INTO Tecnico (id_puesto, nivel_tecnico) 
VALUES (200, 'avanzado');

INSERT INTO VinculacionesConlaEmpresa (id_empleado, inicio, vinculo) 
VALUES (10, '2025-01-01', 'CONTRATADO');

INSERT INTO Disponibilidades (id_empleado, comienzo, fin, disponibilidad) 
VALUES (10, '2025-01-01', NULL, 'TRABAJANDO');


-- -----------------------------------------------------------------------------
-- PA-58: Rol Supervisor (NIF: 23169952M, Contraseña: bV4eT5bD)
-- -----------------------------------------------------------------------------
INSERT INTO Persona (nombre, apellidos, telefono_contacto, direccion, nif)
VALUES (
    'Supervisor', 'Gestor Pruebas', 655333444,
    ROW('Calle Castillo', 18, 3, 'A', 47002, 'Valladolid', 'Valladolid')::tipo_direccion,
    '23169952M'
); -- Generará id_persona = 11

INSERT INTO Empleado (id_persona, fecha_incorporacion, password) 
VALUES (11, '2024-06-01', 'bV4eT5bD');

INSERT INTO Puesto (id_puesto, id_empleado, fecha_inicio) 
VALUES (300, 11, '2024-06-01');

INSERT INTO Supervisor (id_puesto) 
VALUES (300);

INSERT INTO VinculacionesConlaEmpresa (id_empleado, inicio, vinculo) 
VALUES (11, '2024-06-01', 'CONTRATADO');

INSERT INTO Disponibilidades (id_empleado, comienzo, fin, disponibilidad) 
VALUES (11, '2024-06-01', NULL, 'TRABAJANDO');