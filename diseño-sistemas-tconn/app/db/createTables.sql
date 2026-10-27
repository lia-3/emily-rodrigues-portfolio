DROP TABLE IF EXISTS Disponibilidades          CASCADE;
DROP TABLE IF EXISTS VinculacionesConlaEmpresa CASCADE;
 
DROP TYPE IF EXISTS tipo_puesto        CASCADE;
DROP TYPE IF EXISTS tipo_nivel         CASCADE;
DROP TYPE IF EXISTS tipo_vinculo       CASCADE;
DROP TYPE IF EXISTS tipo_disponibilidad CASCADE;
 
DROP TABLE IF EXISTS AsignacionTecnicoOrdenDeServicio  CASCADE;
DROP TABLE IF EXISTS OrdenDeServicio                   CASCADE;
DROP TABLE IF EXISTS CambioDeSituacionTecnico          CASCADE;
DROP TABLE IF EXISTS TurnoTecnico                      CASCADE;
DROP TABLE IF EXISTS TurnoCoordinador                  CASCADE;
DROP TABLE IF EXISTS Mantenimiento                     CASCADE;
DROP TABLE IF EXISTS Instalacion                       CASCADE;
DROP TABLE IF EXISTS AccionDeServicio                  CASCADE;
DROP TABLE IF EXISTS CitaPrevia                        CASCADE;
DROP TABLE IF EXISTS Incidencia                        CASCADE;
DROP TABLE IF EXISTS LlamadaInconsistente              CASCADE;
DROP TABLE IF EXISTS LlamadaCorrecta                   CASCADE;
DROP TABLE IF EXISTS LlamadaContrato                   CASCADE;
DROP TABLE IF EXISTS LlamadaIncorrecta                 CASCADE;
DROP TABLE IF EXISTS Llamada                           CASCADE;
DROP TABLE IF EXISTS Supervisor                        CASCADE;
DROP TABLE IF EXISTS Tecnico                           CASCADE;
DROP TABLE IF EXISTS Coordinador                       CASCADE;
DROP TABLE IF EXISTS Puesto                            CASCADE;
DROP TABLE IF EXISTS Empleado                          CASCADE;
DROP TABLE IF EXISTS ContratoEmpresarialUbicaciones    CASCADE;
DROP TABLE IF EXISTS ContratoEmpresarial               CASCADE;
DROP TABLE IF EXISTS Contrato                          CASCADE;
DROP TABLE IF EXISTS InterlocutoresAutorizados         CASCADE;
DROP TABLE IF EXISTS ClienteParticular                 CASCADE;
DROP TABLE IF EXISTS ClienteEmpresarial                CASCADE;
DROP TABLE IF EXISTS Cliente                           CASCADE;
DROP TABLE IF EXISTS Persona                           CASCADE;
DROP TABLE IF EXISTS Base                              CASCADE;
DROP TABLE IF EXISTS ZonaGeografica                    CASCADE;

DROP TYPE IF EXISTS nivel_tecnico           CASCADE;
DROP TYPE IF EXISTS estado_tecnico          CASCADE;
DROP TYPE IF EXISTS horario_turno_coord     CASCADE;
DROP TYPE IF EXISTS nivel_criticidad        CASCADE;
DROP TYPE IF EXISTS estado_incidencia       CASCADE;
DROP TYPE IF EXISTS tipo_mantenimiento      CASCADE;
DROP TYPE IF EXISTS tipo_instalacion        CASCADE;
DROP TYPE IF EXISTS clase_intervencion      CASCADE;
DROP TYPE IF EXISTS tipo_direccion          CASCADE;
DROP TYPE IF EXISTS tipo_coordenada_gps     CASCADE;


CREATE TYPE nivel_tecnico AS ENUM (
    'basico', 'avanzado', 'especializado'
);

CREATE TYPE estado_tecnico AS ENUM (
    'asignado', 'disponible', 'noDisponible'
);

-- Franja horaria del turno del coordinador
CREATE TYPE horario_turno_coord AS ENUM (
    'de7a15h', 'de15a23h', 'de23a7h'
);

CREATE TYPE nivel_criticidad AS ENUM (
    'critica', 'alta', 'media', 'baja'
);

CREATE TYPE estado_incidencia AS ENUM (
    'sinResolver',
    'resueltaRemotamente',
    'resueltaInSitu',
    'parcialmenteResuelta',
    'escaladaAEspecialista'
);

CREATE TYPE tipo_mantenimiento AS ENUM (
    'incidencia', 'mantenimientoPreventivo', 'actualizacion'
);

CREATE TYPE tipo_instalacion AS ENUM (
    'nuevaInstalacion', 'ampliacion', 'migracionTecnologica'
);

CREATE TYPE clase_intervencion AS ENUM (
    'primera', 'segundaVisita', 'escalado'
);

CREATE TYPE tipo_direccion AS (
    nombre_via    VARCHAR(150),
    numero        INTEGER,
    piso          INTEGER,    
    puerta        CHAR(1),    
    codigo_postal INTEGER,
    localidad     VARCHAR(100),
    provincia     VARCHAR(100)
);

CREATE TYPE tipo_coordenada_gps AS (
    latitud   REAL,
    longitud  REAL
);


CREATE TYPE tipo_puesto AS ENUM (
    'TECNICO',
    'CALLCENTER',
    'SUPERVISOR'
);
 
CREATE TYPE tipo_nivel AS ENUM (
    'BASICO',
    'AVANZADO',
    'ESPECIALIZADO'
);
 
CREATE TYPE tipo_vinculo AS ENUM (
    'CONTRATADO',
    'EN_ERTE',
    'DESPEDIDO'
);
 
CREATE TYPE tipo_disponibilidad AS ENUM (
    'VACACIONES',
    'BAJA_TEMPORAL',
    'TRABAJANDO'
);
 
/* 
CREATE TABLE Empleados (
    nif      VARCHAR(9)   NOT NULL,
    nombre   VARCHAR(50)  NOT NULL,
    email    VARCHAR(100) NOT NULL,
    password VARCHAR(15)  NOT NULL,
 
    CONSTRAINT PK_Empleados PRIMARY KEY (nif)
);

 
CREATE TABLE RolesEnEmpresa (
    nif           VARCHAR(9)   NOT NULL,
    comienzaEnRol DATE         NOT NULL,
    puesto        tipo_puesto  NOT NULL,
    nivel         tipo_nivel,               -- {null allowed}
 
    CONSTRAINT PK_RolesEnEmpresa  PRIMARY KEY (nif, comienzaEnRol),
    CONSTRAINT FK_Roles_Empleados FOREIGN KEY (nif)
        REFERENCES Empleados (nif)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);
*/
 



CREATE TABLE ZonaGeografica (
    id_zona  VARCHAR(50) NOT NULL,
    CONSTRAINT PK_ZonaGeografica PRIMARY KEY (id_zona)
);

CREATE TABLE Base (
    id_base    SERIAL              NOT NULL,
    direccion  tipo_direccion      NOT NULL,
    ubicacion  tipo_coordenada_gps NOT NULL,
    id_zona    VARCHAR(50)         NOT NULL,
    CONSTRAINT PK_Base        PRIMARY KEY (id_base),
    CONSTRAINT FK_Base_Zona   FOREIGN KEY (id_zona)
        REFERENCES ZonaGeografica (id_zona)
        ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE Persona (
    id_persona         SERIAL         NOT NULL,
    nombre             VARCHAR(100)   NOT NULL,
    apellidos          VARCHAR(100)   NOT NULL,
    telefono_contacto  INTEGER        NOT NULL,
    direccion          tipo_direccion NOT NULL,
    nif                VARCHAR(20)    NOT NULL,
    CONSTRAINT PK_Persona     PRIMARY KEY (id_persona),
    CONSTRAINT UQ_Persona_nif UNIQUE (nif)
);


CREATE TABLE Cliente (
    id_persona INTEGER NOT NULL,
    CONSTRAINT PK_Cliente          PRIMARY KEY (id_persona),
    CONSTRAINT FK_Cliente_Persona  FOREIGN KEY (id_persona)
        REFERENCES Persona (id_persona)
        ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE ClienteParticular (
    id_persona INTEGER NOT NULL,
    CONSTRAINT PK_ClienteParticular          PRIMARY KEY (id_persona),
    CONSTRAINT FK_CliPart_Cliente            FOREIGN KEY (id_persona)
        REFERENCES Cliente (id_persona)
        ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE ClienteEmpresarial (
    id_persona           INTEGER        NOT NULL,
    cif                  VARCHAR(20)    NOT NULL,
    nombre_razon_social  VARCHAR(200)   NOT NULL,
    sede                 tipo_direccion NOT NULL,
    telefono             INTEGER        NOT NULL,
    CONSTRAINT PK_ClienteEmpresarial        PRIMARY KEY (id_persona),
    CONSTRAINT FK_CliEmp_Cliente            FOREIGN KEY (id_persona)
        REFERENCES Cliente (id_persona)
        ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT UQ_ClienteEmpresarial_cif    UNIQUE (cif)
);

CREATE TABLE InterlocutoresAutorizados (
    id_persona              INTEGER NOT NULL,
    id_cliente_empresarial  INTEGER NOT NULL,
    CONSTRAINT PK_Interlocutores       PRIMARY KEY (id_persona, id_cliente_empresarial),
    CONSTRAINT FK_Interloc_Persona     FOREIGN KEY (id_persona)
        REFERENCES Persona (id_persona)
        ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT FK_Interloc_CliEmp      FOREIGN KEY (id_cliente_empresarial)
        REFERENCES ClienteEmpresarial (id_persona)
        ON UPDATE CASCADE ON DELETE CASCADE
);


CREATE TABLE Contrato (
    id_contrato      SERIAL         NOT NULL,
    numero_contrato  VARCHAR(50)    NOT NULL,
    fecha_inicio     DATE           NOT NULL,
    fecha_fin        DATE,                     -- [0..1]
    ubicacion        tipo_direccion NOT NULL,
    activo           BOOLEAN        NOT NULL DEFAULT TRUE,
    id_cliente       INTEGER        NOT NULL,
    CONSTRAINT PK_Contrato           PRIMARY KEY (id_contrato),
    CONSTRAINT UQ_Contrato_numero    UNIQUE (numero_contrato),
    CONSTRAINT FK_Contrato_Cliente   FOREIGN KEY (id_cliente)
        REFERENCES Cliente (id_persona)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT CHK_Contrato_Fechas   CHECK (fecha_fin IS NULL OR fecha_fin >= fecha_inicio)
);

-- ContratoEmpresarial redefine «titular» apuntando a ClienteEmpresarial
CREATE TABLE ContratoEmpresarial (
    id_contrato             INTEGER NOT NULL,
    id_cliente_empresarial  INTEGER NOT NULL,
    CONSTRAINT PK_ContratoEmpresarial        PRIMARY KEY (id_contrato),
    CONSTRAINT FK_ContrEmp_Contrato          FOREIGN KEY (id_contrato)
        REFERENCES Contrato (id_contrato)
        ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT FK_ContrEmp_CliEmp            FOREIGN KEY (id_cliente_empresarial)
        REFERENCES ClienteEmpresarial (id_persona)
        ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE ContratoEmpresarialUbicaciones (
    id_contrato  INTEGER        NOT NULL,
    ubicacion    tipo_direccion NOT NULL,
    CONSTRAINT PK_ContrEmpUbic    PRIMARY KEY (id_contrato, ubicacion),
    CONSTRAINT FK_ContrEmpUbic    FOREIGN KEY (id_contrato)
        REFERENCES ContratoEmpresarial (id_contrato)
        ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE Empleado (
    id_persona          INTEGER NOT NULL,
    fecha_incorporacion DATE    NOT NULL,
    fecha_fin_contrato  DATE,             -- [0..1]
	password VARCHAR(15)  NOT NULL,
    CONSTRAINT PK_Empleado         PRIMARY KEY (id_persona),
    CONSTRAINT FK_Empleado_Persona FOREIGN KEY (id_persona)
        REFERENCES Persona (id_persona)
        ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE Puesto (
    id_puesto    INTEGER  NOT NULL,
    id_empleado  INTEGER NOT NULL,
    fecha_inicio DATE    NOT NULL,
    CONSTRAINT PK_Puesto          PRIMARY KEY (id_puesto),
    CONSTRAINT FK_Puesto_Empleado FOREIGN KEY (id_empleado)
        REFERENCES Empleado (id_persona)
        ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE Coordinador (
    id_puesto INTEGER NOT NULL,
    CONSTRAINT PK_Coordinador        PRIMARY KEY (id_puesto),
    CONSTRAINT FK_Coord_Puesto       FOREIGN KEY (id_puesto)
        REFERENCES Puesto (id_puesto)
        ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE Tecnico (
    id_puesto      INTEGER       NOT NULL,
    nivel_tecnico  nivel_tecnico NOT NULL,
    CONSTRAINT PK_Tecnico        PRIMARY KEY (id_puesto),
    CONSTRAINT FK_Tecnico_Puesto FOREIGN KEY (id_puesto)
        REFERENCES Puesto (id_puesto)
        ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE Supervisor (
    id_puesto INTEGER NOT NULL,
    CONSTRAINT PK_Supervisor        PRIMARY KEY (id_puesto),
    CONSTRAINT FK_Superv_Puesto     FOREIGN KEY (id_puesto)
        REFERENCES Puesto (id_puesto)
        ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE VinculacionesConlaEmpresa (
    id_empleado  INTEGER NOT NULL,
    inicio  DATE          NOT NULL,
    vinculo tipo_vinculo  NOT NULL,
 
    CONSTRAINT PK_Vinculaciones          PRIMARY KEY (id_empleado, inicio),
    CONSTRAINT FK_Vinculaciones_Empleados FOREIGN KEY (id_empleado)
        REFERENCES Empleado (id_persona)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);
 
CREATE TABLE Disponibilidades (
    id_empleado  INTEGER NOT NULL,
    comienzo      DATE                NOT NULL,
    fin           DATE,                        -- {null allowed}
    disponibilidad tipo_disponibilidad NOT NULL,
 
    CONSTRAINT PK_Disponibilidades          PRIMARY KEY (id_empleado, comienzo),
    CONSTRAINT FK_Disponibilidades_Empleado FOREIGN KEY (id_empleado)
        REFERENCES Empleado (id_persona)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT CHK_Disponibilidades_Fechas
        CHECK (fin IS NULL OR fin >= comienzo)
);


CREATE TABLE TurnoCoordinador (
    id_turno            SERIAL              NOT NULL,
    fecha_inicio_turno  DATE                NOT NULL,
    horario             horario_turno_coord NOT NULL,
    fecha_fin           DATE                NOT NULL,
    id_coordinador      INTEGER             NOT NULL,
    CONSTRAINT PK_TurnoCoordinador       PRIMARY KEY (id_turno),
    CONSTRAINT FK_TurnoCoord_Coord       FOREIGN KEY (id_coordinador)
        REFERENCES Coordinador (id_puesto)
        ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT CHK_TurnoCoord_Fechas     CHECK (fecha_fin >= fecha_inicio_turno)
);

CREATE TABLE TurnoTecnico (
    id_turno      SERIAL  NOT NULL,
    fecha_inicio  DATE    NOT NULL,
    hora_inicio   TIME    NOT NULL,
    fecha_fin     DATE    NOT NULL,
    hora_fin      TIME    NOT NULL,
    id_tecnico    INTEGER NOT NULL,
    id_base       INTEGER NOT NULL,
    CONSTRAINT PK_TurnoTecnico         PRIMARY KEY (id_turno),
    CONSTRAINT FK_TurnoTec_Tecnico     FOREIGN KEY (id_tecnico)
        REFERENCES Tecnico (id_puesto)
        ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT FK_TurnoTec_Base        FOREIGN KEY (id_base)
        REFERENCES Base (id_base)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT CHK_TurnoTec_Fechas     CHECK (
        fecha_fin > fecha_inicio
        OR (fecha_fin = fecha_inicio AND hora_fin > hora_inicio)
    )
);

CREATE TABLE CambioDeSituacionTecnico (
    id_cambio         SERIAL              NOT NULL,
    estado            estado_tecnico      NOT NULL,
    ubicacion         tipo_coordenada_gps NOT NULL,
    fecha             DATE                NOT NULL,
    hora              TIME                NOT NULL,
    id_turno_tecnico  INTEGER             NOT NULL,
    CONSTRAINT PK_CambioDeSituacion       PRIMARY KEY (id_cambio),
    CONSTRAINT FK_CambioSit_TurnoTec      FOREIGN KEY (id_turno_tecnico)
        REFERENCES TurnoTecnico (id_turno)
        ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE Llamada (
    id_llamada       SERIAL  NOT NULL,
    fecha            DATE    NOT NULL,
    numero_telefono  INTEGER NOT NULL,
    hora             TIME    NOT NULL,
    duracion         INTEGER NOT NULL,  -- duración en minutos (entero del modelo)
    id_coordinador   INTEGER NOT NULL,
    CONSTRAINT PK_Llamada              PRIMARY KEY (id_llamada),
    CONSTRAINT FK_Llamada_Coordinador  FOREIGN KEY (id_coordinador)
        REFERENCES Coordinador (id_puesto)
        ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE LlamadaIncorrecta (
    id_llamada INTEGER NOT NULL,
    CONSTRAINT PK_LlamadaIncorrecta       PRIMARY KEY (id_llamada),
    CONSTRAINT FK_LlamInc_Llamada         FOREIGN KEY (id_llamada)
        REFERENCES Llamada (id_llamada)
        ON UPDATE CASCADE ON DELETE CASCADE
);

-- LlamadaContrato: llamada que identifica un contrato concreto
CREATE TABLE LlamadaContrato (
    id_llamada   INTEGER NOT NULL,
    id_contrato  INTEGER NOT NULL,  -- «contrato identificado»
    CONSTRAINT PK_LlamadaContrato      PRIMARY KEY (id_llamada),
    CONSTRAINT FK_LlamContr_Llamada    FOREIGN KEY (id_llamada)
        REFERENCES Llamada (id_llamada)
        ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT FK_LlamContr_Contrato   FOREIGN KEY (id_contrato)
        REFERENCES Contrato (id_contrato)
        ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE LlamadaCorrecta (
    id_llamada INTEGER NOT NULL,
    CONSTRAINT PK_LlamadaCorrecta          PRIMARY KEY (id_llamada),
    CONSTRAINT FK_LlamCorr_LlamContrato    FOREIGN KEY (id_llamada)
        REFERENCES LlamadaContrato (id_llamada)
        ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE LlamadaInconsistente (
    id_llamada            INTEGER NOT NULL,
    datos_inconsistentes  TEXT    NOT NULL,
    CONSTRAINT PK_LlamadaInconsistente       PRIMARY KEY (id_llamada),
    CONSTRAINT FK_LlamIncons_LlamContrato    FOREIGN KEY (id_llamada)
        REFERENCES LlamadaContrato (id_llamada)
        ON UPDATE CASCADE ON DELETE CASCADE
);


-- -----------------------------------------------------------------------------
-- SECCIÓN 10 – INCIDENCIAS, CITAS PREVIAS Y ACCIONES DE SERVICIO
-- -----------------------------------------------------------------------------



-- CitaPrevia: cita programada vinculada a una AcciónDeServicio (0..1 por acción)
-- Se crea antes de la AcciónDeServicio, de ahí que sea tabla independiente
CREATE TABLE CitaPrevia (
    id_cita  SERIAL  NOT NULL,
    codigo   INTEGER NOT NULL,
    fecha    DATE    NOT NULL,
    hora     TIME    NOT NULL,
    CONSTRAINT PK_CitaPrevia PRIMARY KEY (id_cita)
);

-- Autoreferencia anterior [0..1] / siguientes [0..*] → FK DEFERRABLE
-- Tipo acción de servicio {complete, disjoint}: Instalación | Mantenimiento
CREATE TABLE AccionDeServicio (
    id_accion              SERIAL             NOT NULL,
    direccion_intervencion tipo_direccion     NOT NULL,
    clase_intervencion     clase_intervencion NOT NULL,
    fecha_creacion         DATE               NOT NULL,
    id_anterior            INTEGER,                    
    id_cita_previa         INTEGER,                     
    CONSTRAINT PK_AccionDeServicio          PRIMARY KEY (id_accion),
    CONSTRAINT FK_AccionDS_Anterior         FOREIGN KEY (id_anterior)
        REFERENCES AccionDeServicio (id_accion)
        DEFERRABLE INITIALLY DEFERRED,       -- permite insertar sin orden estricto
    CONSTRAINT FK_AccionDS_CitaPrevia       FOREIGN KEY (id_cita_previa)
        REFERENCES CitaPrevia (id_cita)
        ON UPDATE CASCADE ON DELETE SET NULL
);

CREATE TABLE Instalacion (
    id_accion  INTEGER          NOT NULL,
    tipo       tipo_instalacion NOT NULL,
    CONSTRAINT PK_Instalacion          PRIMARY KEY (id_accion),
    CONSTRAINT FK_Instal_AccionDS      FOREIGN KEY (id_accion)
        REFERENCES AccionDeServicio (id_accion)
        ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE Mantenimiento (
    id_accion  INTEGER            NOT NULL,
    tipo       tipo_mantenimiento NOT NULL,
    CONSTRAINT PK_Mantenimiento        PRIMARY KEY (id_accion),
    CONSTRAINT FK_Manten_AccionDS      FOREIGN KEY (id_accion)
        REFERENCES AccionDeServicio (id_accion)
        ON UPDATE CASCADE ON DELETE CASCADE
);

-- Incidencia: puede originarse en una LlamadaCorrecta [0..1]
CREATE TABLE Incidencia (
    id_incidencia                    SERIAL            NOT NULL,
    id_mantenimiento  INTEGER            NOT NULL,
    codigo                           INTEGER           NOT NULL,
    descripcion                      TEXT              NOT NULL,
    fecha                            DATE              NOT NULL,
    criticidad                       nivel_criticidad  NOT NULL,
    estado                           estado_incidencia NOT NULL,
    tiempo_total_resolucion_minutos  INTEGER,                    -- [0..1]
    id_llamada_correcta              INTEGER,                    -- [0..1]
    CONSTRAINT PK_Incidencia               PRIMARY KEY (id_incidencia),
    CONSTRAINT UQ_Incidencia_codigo        UNIQUE (codigo),
	CONSTRAINT FK_Instal_Mantenimiento      FOREIGN KEY (id_mantenimiento)
        REFERENCES Mantenimiento (id_accion)
        ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT FK_Incid_LlamCorrecta       FOREIGN KEY (id_llamada_correcta)
        REFERENCES LlamadaCorrecta (id_llamada)
        ON UPDATE CASCADE ON DELETE SET NULL
);


CREATE TABLE OrdenDeServicio (
    id_orden       SERIAL  NOT NULL,
    fecha_emision  DATE    NOT NULL,
    hora_emision   TIME    NOT NULL,
    id_incidencia  INTEGER NOT NULL,          
    CONSTRAINT PK_OrdenDeServicio        PRIMARY KEY (id_orden),
    CONSTRAINT FK_OrdenDS_Incidencia     FOREIGN KEY (id_incidencia)
        REFERENCES Incidencia (id_incidencia)
        ON UPDATE CASCADE ON DELETE SET NULL
);

-- AsignaciónTécnicoOrdenDeServicio: clase de asociación entre TurnoTécnico y OrdenDeServicio
-- TurnoTécnico (0..*{ordered}) ←→ (0..*{ordered}) OrdenDeServicio
CREATE TABLE AsignacionTecnicoOrdenDeServicio (
    id_tecnico                       INTEGER NOT NULL,
    id_orden                         INTEGER NOT NULL,
    tiempo_respuesta_asignacion      INTEGER NOT NULL,
    tiempo_estimado_llegada_minutos  INTEGER NOT NULL,
    hora_llegada_punto_servicio      TIME,
    CONSTRAINT PK_AsignacionTecnico        PRIMARY KEY (id_tecnico, id_orden),
    CONSTRAINT FK_AsignTec_Tecnico    FOREIGN KEY (id_tecnico)
        REFERENCES Tecnico (id_puesto)
        ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT FK_AsignTec_OrdenDS         FOREIGN KEY (id_orden)
        REFERENCES OrdenDeServicio (id_orden)
        ON UPDATE CASCADE ON DELETE CASCADE
);