--- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% ---
---                                          Eliminacion Tablas										   ---
--- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% ---
DROP TABLE Firma;
DROP TABLE Inspector;
DROP TABLE Tutela;
DROP TABLE Representante;
DROP TABLE Porcentaje;
DROP TABLE Titular;
DROP TABLE Persona;
DROP TABLE Infraccion;
DROP TABLE ActaInspeccion;
DROP TABLE Canon;
DROP TABLE Construccion;
DROP TABLE Concesion;
DROP TABLE Parcela;

DROP DOMAIN estadoAdministrativoConcesion;
DROP DOMAIN estadoPlanAutoproteccionConcesion;
DROP DOMAIN estadoLegalConstruccion;
DROP DOMAIN tipoInspector;
DROP DOMAIN estadoLiquidacionInfraccion;
DROP DOMAIN tipoSancionInfraccion;
DROP DOMAIN gravedadInfraccion;


--- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% ---
---                                               Dominios											   ---
--- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% ---

CREATE DOMAIN estadoPlanAutoproteccionConcesion AS VARCHAR(13)
    CHECK (VALUE IN ('innecesario', 'noPresentado', 'presentado', 'revisado', 'aprobado'));

CREATE DOMAIN estadoAdministrativoConcesion AS VARCHAR(11)
    CHECK (VALUE IN ('vigente', 'cedida', 'extinguida', 'revocada'));

CREATE DOMAIN estadoLegalConstruccion AS VARCHAR(11)
    CHECK (VALUE IN ('legalizada', 'enProceso', 'ilegal'));

CREATE DOMAIN tipoInspector AS VARCHAR(10)
    CHECK (VALUE IN ('principal', 'apoyo'));

CREATE DOMAIN estadoLiquidacionInfraccion AS VARCHAR(10)
    CHECK (VALUE IN ('noPagada', 'anulada', 'liquidada'));

CREATE DOMAIN tipoSancionInfraccion AS VARCHAR(10)
    CHECK (VALUE IN ('economica', 'temporal'));

CREATE DOMAIN gravedadInfraccion AS VARCHAR(10)
    CHECK (VALUE IN ('leve', 'grave', 'muyGrave'));



--- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% ---
---                                          Creacion Tablas										   ---
--- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% ---

CREATE TABLE Parcela(
    referenciaCatastral CHAR(20),
    ubicacion VARCHAR(100) not null,
    superficie REAL not null,
    PRIMARY KEY (referenciaCatastral),
    CONSTRAINT superficieValida CHECK (superficie > 0)
);


--- Las concesiones no se pueden borrar por el historico que se debe mantener
--- La fecha maxima de finalizacion no puede ser mayor a 75 años desde la 
--- 		formalizacion en caso de no ser por cesion o mortis causa
--- La fecha maxima de finalizacion en caso de cesion o mortis causa no puede 
---  		ser mayor a 30 años desde la formalizacion
CREATE TABLE Concesion(
    idConcesion CHAR(20),
    idAgrupacion CHAR(20),
    referenciaCatastral CHAR(20) not null,
    fechaFormalizacion DATE not null,
    fechaFinalizacion DATE not null,
    estadoPlanAutoProteccion estadoPlanAutoproteccionConcesion not null,
    usoAutorizado CHAR(20) not null,
    estadoAdministrativo estadoAdministrativoConcesion not null,
    porCesion BOOLEAN not null,
    porMortisCausa BOOLEAN not null,
    PRIMARY KEY (idConcesion),
    FOREIGN KEY (referenciaCatastral) REFERENCES Parcela,
    CONSTRAINT periodoValido CHECK (fechaFinalizacion >= fechaFormalizacion)
);


CREATE TABLE Construccion(
    licenciaMunicipal CHAR(50),
    referenciaCatastral CHAR(20),
    valorTasacion REAL not null,
    fechaAutorizacion DATE not null,
    tipoUso VARCHAR(20) not null,
    estadoLegal estadoLegalConstruccion not null,
    PRIMARY KEY (licenciaMunicipal),
    FOREIGN KEY (referenciaCatastral) REFERENCES Parcela ON DELETE CASCADE,
    CONSTRAINT valorTasacionValido CHECK (valorTasacion > 0)
);


CREATE TABLE Canon (
    periodoAnual INTEGER,
    idConcesion CHAR(20),
    valorMonetario REAL not null,
    fechaMaxPago DATE not null,
    fechaRealizacionPago DATE,
    PRIMARY KEY (periodoAnual, idConcesion),
    FOREIGN KEY (idConcesion) REFERENCES Concesion,
    CONSTRAINT periodoValido CHECK (periodoAnual >= 1985),
    CONSTRAINT valorMonetarioValido CHECK (valorMonetario > 0)
);


--- La fecha de imposicion no puede ser anterior a la fecha de la formalizacion de la concesion
CREATE TABLE ActaInspeccion (
    numActa INTEGER,
    idConcesion CHAR(20),
    fechaYHora TIMESTAMP not null,
    motivo VARCHAR(100) not null,
    objetoInspeccion VARCHAR(50) not null,
    PRIMARY KEY (numActa),
    FOREIGN KEY (idConcesion) REFERENCES Concesion
);


--- La fecha de imposicion no puede ser anterior a la fecha del acta de inspeccion en caso 
--- 		de que exista
--- La fecha de imposicion no puede ser anterior a la fecha de formalizacion de la concesion 
--- 		en caso de que no este asociado a una concesion directamente
CREATE TABLE Infraccion (
    idInfraccion CHAR(20),
    idConcesion CHAR(20),
    numActa INTEGER,
    articuloInfringido VARCHAR(50) not null,
    tipoSancion tipoSancionInfraccion not null,
    gravedad gravedadInfraccion not null,
    fechaImposicion DATE not null,
    importe REAL,
    estadoLiquidacion estadoLiquidacionInfraccion not null,
    PRIMARY KEY (idInfraccion),
    FOREIGN KEY (idConcesion) REFERENCES Concesion, 
    FOREIGN KEY (numActa) REFERENCES ActaInspeccion,
    CONSTRAINT xorForeignKeys CHECK (
        (idConcesion IS not null AND numActa IS null)
        OR (idConcesion IS null AND numActa IS not null)
    ),
    CONSTRAINT tipoSancionImporte CHECK (
        (tipoSancion = 'economica' AND importe IS not null)
        OR
        (tipoSancion = 'temporal' AND importe IS null)
    ),
    CONSTRAINT importeValido CHECK (
        importe IS null OR importe > 0
    )
);


CREATE TABLE Persona (
    numIdentificacion CHAR(9),
    nombre VARCHAR(150) not null,
    fechaNacimiento DATE not null,
    direccion VARCHAR(255) not null,
    telefono INTEGER not null,
    email VARCHAR(320) not null,
    PRIMARY KEY (numIdentificacion),
    CONSTRAINT telefonoValido CHECK (telefono >= 100000000 AND telefono <= 999999999)
);


--- No se puede eliminar un titular por el historico de concesiones, pero en caso de eliminar una persona, 
--- 			se eliminan sus datos como titular
CREATE TABLE Titular (
    numIdentificacion CHAR(9),
    razonSocial VARCHAR(255),
    incapacitado BOOLEAN,
    PRIMARY KEY (numIdentificacion),
    FOREIGN KEY (numIdentificacion) REFERENCES Persona ON DELETE CASCADE,
    CONSTRAINT tipoTitular CHECK (
        (razonSocial IS not null AND incapacitado IS null)
        OR
        (razonSocial IS null AND incapacitado IS not null)
    )
);


CREATE TABLE Porcentaje (
    numIdentificacion CHAR(9),
    idConcesion CHAR(20),
    porcenateTitularidad REAL not null,
    PRIMARY KEY (numIdentificacion, idConcesion),
    FOREIGN KEY (numIdentificacion) REFERENCES Titular,
    FOREIGN KEY (idConcesion) REFERENCES Concesion,
    CONSTRAINT porcentajeValido CHECK (porcenateTitularidad >= 0 AND porcenateTitularidad <= 100)
);


--- La relacion con el titular no puede ser 'autotutela'
--- Un representante no puede ser menor de edad ni estar incapacitado
CREATE TABLE Representante (
    numIdentificacion CHAR(9),
    relacionTitular VARCHAR(20) not null,
    PRIMARY KEY (numIdentificacion),
    FOREIGN KEY (numIdentificacion) REFERENCES Persona ON DELETE CASCADE
);

--- Una persona no puede ser representante de si misma
--- La relacion de tutela solo existe si el titular esta incapacitado o es menor de edad
CREATE TABLE Tutela (
    numIdentificacionTitular CHAR(9),
    numIdentificacionRepresentante CHAR(9),
    PRIMARY KEY (numIdentificacionTitular, numIdentificacionRepresentante),
    FOREIGN KEY (numIdentificacionTitular) REFERENCES Titular(numIdentificacion) ON DELETE CASCADE,
    FOREIGN KEY (numIdentificacionRepresentante) REFERENCES Representante(numIdentificacion)
);


--- Un inspector no puede tener un acta de inspeccion si es titular de esa concesion
CREATE TABLE Inspector (
    numIdentificacion CHAR(9),
    numAcreditacion CHAR(20) not null,
    cargo tipoInspector not null,
    UNIQUE (numAcreditacion),
    PRIMARY KEY (numIdentificacion),
    FOREIGN KEY (numIdentificacion) REFERENCES Persona ON DELETE CASCADE
);


CREATE TABLE Firma (
    numIdentificacion CHAR(9),
    numActa INTEGER,
    PRIMARY KEY (numIdentificacion, numActa),
    FOREIGN KEY (numIdentificacion) REFERENCES Inspector,
    FOREIGN KEY (numActa) REFERENCES ActaInspeccion ON DELETE CASCADE
);


--- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% ---
---                                          Insercion Tuplas										   ---
--- %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% ---

-- CONCESION Titular Menor No Infracciones
-- 1. Personas
--      P1: Titular menor
INSERT INTO Persona(numIdentificacion, nombre, fechaNacimiento, direccion, telefono, email)
VALUES( '70831256L', 'Paco Perez Garcia', '2009-05-17', 'Plaza de la Constitución, 19, 4.B, Almonte, Huelva',
	691455021, 'paqu1toelchocolat3ro0917@gmail.com');
--       P2: Representante del menor
INSERT INTO Persona(numIdentificacion, nombre, fechaNacimiento, direccion, telefono, email)
VALUES( '28614212G', 'Arturo Perez Reverte','1984-12-25', 'Plaza de la Constitución 19, 4.B, Almonte, Huelva',
	691455021, 'soyescritorPerez@gmail.com');
--       P3: Inspector 1 (con NIE)
INSERT INTO Persona(numIdentificacion, nombre, fechaNacimiento, direccion, telefono, email)
VALUES( 'Z5863297G', 'Lucia Gil Torres','1992-07-04', 'Calle Inspeccion 3, Almonte, Huelva',
	695156391, 'luciaInspectora@gmail.com');
--      P4: Inspector 2
INSERT INTO Persona(numIdentificacion, nombre, fechaNacimiento, direccion, telefono, email)
VALUES( '54286543B', 'Ivan Serra Montero','1997-01-23', 'Ronda Mayor, 82, 37075, Béjar, (Salamanca)',
	619127286, 'ivanserra@gmail.com');

-- 2 Roles
INSERT INTO Titular(numIdentificacion, razonSocial, incapacitado)VALUES('70831256L', NULL, FALSE);
INSERT INTO Representante(numIdentificacion,relacionTitular)VALUES ('28614212G','Padre');
INSERT INTO Inspector(numIdentificacion,numAcreditacion,cargo)VALUES('Z5863297G','INSPECTOR-0000000001', 'principal');
INSERT INTO Inspector(numIdentificacion,numAcreditacion,cargo)VALUES('54286543B','INSPECTOR-0000000002', 'apoyo');
INSERT INTO Tutela(numIdentificacionTitular, numIdentificacionRepresentante)VALUES('70831256L','28614212G');

---3 Parcela
INSERT INTO Parcela(referenciaCatastral, ubicacion, superficie)
VALUES('REFERENCIA-CAT-00004','Solar Sector SA-17 Cm Sevilla, 32, 21730 Almonte, Huelva', 600.25);

-- 4 Concesion
INSERT INTO Concesion(idConcesion,referenciaCatastral,fechaFormalizacion,
    fechaFinalizacion,estadoPlanAutoproteccion,usoAutorizado,estadoAdministrativo,
	porCesion,porMortisCausa)
VALUES('CONCESION-2023-00004','REFERENCIA-CAT-00004', '2023-03-11', '2053-03-11',
	'innecesario', 'agropecuario', 'vigente', TRUE, FALSE);

-- 5 Porcentaje
INSERT INTO Porcentaje(idConcesion, numIdentificacion, porcenateTitularidad)
VALUES('CONCESION-2023-00004', '70831256L',100);

-- 6 Construccion
INSERT INTO Construccion(licenciaMunicipal, referenciaCatastral, valorTasacion, 
	fechaAutorizacion, tipoUso, estadoLegal)
VALUES('LICENCIA-AYUNTAMIENTO-0000000000000000000000000003', 'REFERENCIA-CAT-00004', 83400.50,'2024-06-01',
     'Pozo', 'legalizada');

-- 7 Canon
--- Pago durante todos los años
INSERT INTO Canon(PeriodoAnual, idConcesion, valorMonetario, fechaMaxPago, fechaRealizacionPago)
VALUES(2023,'CONCESION-2023-00004', 4000.00, '2023-12-31', '2023-08-22');
INSERT INTO Canon(PeriodoAnual, idConcesion, valorMonetario, fechaMaxPago, fechaRealizacionPago)
VALUES(2024,'CONCESION-2023-00004', 4000.00, '2024-12-31', '2024-05-10');
INSERT INTO Canon(PeriodoAnual, idConcesion, valorMonetario, fechaMaxPago, fechaRealizacionPago)
VALUES(2025,'CONCESION-2023-00004', 4000.00, '2025-12-31', '2025-10-19');

-- 8 Acta Inspeccion , Firma
INSERT INTO ActaInspeccion(numActa, idConcesion,  fechaYHora, motivo, objetoInspeccion)
VALUES(103495, 'CONCESION-2023-00004', '2024-04-03 11:35:11', 'control','inspeccion general');

INSERT INTO Firma(numIdentificacion, numActa)VALUES('Z5863297G',103495);
INSERT INTO Firma(numIdentificacion, numActa)VALUES('54286543B',103495);

--9 Infraccion (No tiene)


-- CONCESION Mortis Causa 4 hermanos y 1 menor con infracciones y concesion del muerto
-- 1. Personas
--      P1: Titular hermano 1
INSERT INTO Persona(numIdentificacion, nombre, fechaNacimiento, direccion, telefono, email)
VALUES( '62059700L', 'Maria Jesus Vazquez Guijarro', '1995-06-27', 'KALEA MADRID, 27, 11191, SETENIL DE LAS BODEGAS(CÁDIZ)',
	702052608, 'paqu1toelchocolat3ro0917@gmail.com');
--      P2: Titular hermano 2
INSERT INTO Persona(numIdentificacion, nombre, fechaNacimiento, direccion, telefono, email)
VALUES( '08157609S', 'Jesus Maria Vazquez Guijarro', '1993-01-02', 'CAMÍ CATALUNYA, 51, 23553, CHILLUÉVAR(JAÉN)',
	639274881, 'JesuleMaVazGui@gmail.com');
--      P3: Titular hermano 3
INSERT INTO Persona(numIdentificacion, nombre, fechaNacimiento, direccion, telefono, email)
VALUES( '19779494T', 'Maria Jose Vazquez Guijarro', '1997-11-11', 'CAMÍ CATALUNYA, 51, 23553, CHILLUÉVAR(JAÉN)',
	758560062, 'mariaJoseVAZGUI@gmail.com');
--      P4: Titular hermano 4
INSERT INTO Persona(numIdentificacion, nombre, fechaNacimiento, direccion, telefono, email)
VALUES( '62303163G', 'Jose Maria Vazquez Guijarro', '2000-08-19', 'CAMÍ CATALUNYA, 51, 23553, CHILLUÉVAR(JAÉN)',
	625668557, 'joseMariaVazGui@gmail.com');
--       P5: Representante del incapacitado (hermano 3)
INSERT INTO Persona(numIdentificacion, nombre, fechaNacimiento, direccion, telefono, email)
VALUES( '32388919C', 'Maria Guijarro Antolin','1974-12-25', 'CAMÍ CATALUNYA, 51, 23553, CHILLUÉVAR(JAÉN)',
	758560062, 'mariaGuiAntonz@gmail.com');
--       P6: Antiguo propietario de la concesion (muerto)
INSERT INTO Persona(numIdentificacion, nombre, fechaNacimiento, direccion, telefono, email)
VALUES( '16189275N', 'Felipe Vazquez Caminero','1969-12-25', 'CAMÍ CATALUNYA, 51, 23553, CHILLUÉVAR(JAÉN)',
	789330228, 'FelipVazCamin@gmail.com');
--       P7: Inspector
INSERT INTO Persona(numIdentificacion, nombre, fechaNacimiento, direccion, telefono, email)
VALUES( '34574095N', 'Isaac Caceres Aguirre','1970-04-11', 'PASSATGE MAYOR, 76, 23882, ESCAÑUELA(JAÉN)',
	695156391, 'luciaInspectora@gmail.com');
--       P8: Inspector
INSERT INTO Persona(numIdentificacion, nombre, fechaNacimiento, direccion, telefono, email)
VALUES( '21286243L', 'Rosa Maria Ramos Tejero','1982-02-02', 'TRAVESÍA MADRID, 63, 23642, SANTA ELENA(JAÉN)',
	639588824, 'rosaMariRamosTejero@gmail.com');

-- 2 Roles
INSERT INTO Titular(numIdentificacion, razonSocial, incapacitado)VALUES('16189275N', NULL, FALSE);

INSERT INTO Titular(numIdentificacion, razonSocial, incapacitado)VALUES('62059700L', NULL, FALSE);
INSERT INTO Titular(numIdentificacion, razonSocial, incapacitado)VALUES('08157609S', NULL, FALSE);
INSERT INTO Titular(numIdentificacion, razonSocial, incapacitado)VALUES('19779494T', NULL, TRUE);
INSERT INTO Titular(numIdentificacion, razonSocial, incapacitado)VALUES('62303163G', NULL, FALSE);
INSERT INTO Representante(numIdentificacion,relacionTitular)VALUES ('32388919C','Madre');
INSERT INTO Inspector(numIdentificacion,numAcreditacion,cargo)VALUES('34574095N','INSPECTOR-0000000003', 'principal');
INSERT INTO Inspector(numIdentificacion,numAcreditacion,cargo)VALUES('21286243L','INSPECTOR-0000000004', 'apoyo');
INSERT INTO Tutela(numIdentificacionTitular, numIdentificacionRepresentante)VALUES('19779494T','32388919C');

---3 Parcela
INSERT INTO Parcela(referenciaCatastral, ubicacion, superficie)
VALUES('REFERENCIA-CAT-00005','Solar Sector SA-17 Cm Sevilla, 32, 21730 Almonte, Huelva', 600.25);

-- 4 Concesion
INSERT INTO Concesion(idConcesion,referenciaCatastral,fechaFormalizacion,
    fechaFinalizacion,estadoPlanAutoproteccion,usoAutorizado,estadoAdministrativo,
	porCesion,porMortisCausa)
VALUES('CONCESION-1989-00005','REFERENCIA-CAT-00005', '1890-01-14', '2021-08-12',
	'innecesario', 'agropecuario', 'cedida', FALSE, FALSE);

INSERT INTO Concesion(idConcesion,referenciaCatastral,fechaFormalizacion,
    fechaFinalizacion,estadoPlanAutoproteccion,usoAutorizado,estadoAdministrativo,
	porCesion,porMortisCausa)
VALUES('CONCESION-2021-00006','REFERENCIA-CAT-00005', '2021-08-23', '2051-08-23',
	'innecesario', 'agropecuario', 'vigente', TRUE, TRUE);

-- 5 Porcentaje
INSERT INTO Porcentaje(idConcesion, numIdentificacion, porcenateTitularidad)
VALUES('CONCESION-2021-00006', '62059700L',25);
INSERT INTO Porcentaje(idConcesion, numIdentificacion, porcenateTitularidad)
VALUES('CONCESION-2021-00006', '08157609S',25);
INSERT INTO Porcentaje(idConcesion, numIdentificacion, porcenateTitularidad)
VALUES('CONCESION-2021-00006', '19779494T',25);
INSERT INTO Porcentaje(idConcesion, numIdentificacion, porcenateTitularidad)
VALUES('CONCESION-2021-00006', '62303163G',25);

-- 6 Construccion (no tiene)

-- 7 Canon
--- Pago durante todos los años de ambas concesiones
INSERT INTO Canon(PeriodoAnual, idConcesion, valorMonetario, fechaMaxPago, fechaRealizacionPago)
VALUES(1989,'CONCESION-1989-00005', 4000.00, '1989-12-31', '1989-10-30');
INSERT INTO Canon(PeriodoAnual, idConcesion, valorMonetario, fechaMaxPago, fechaRealizacionPago)
VALUES(1990,'CONCESION-1989-00005', 4050.00, '1990-12-31', '1990-10-30');
INSERT INTO Canon(PeriodoAnual, idConcesion, valorMonetario, fechaMaxPago, fechaRealizacionPago)
VALUES(1991,'CONCESION-1989-00005', 4100.00, '1991-12-31', '1991-10-30');
INSERT INTO Canon(PeriodoAnual, idConcesion, valorMonetario, fechaMaxPago, fechaRealizacionPago)
VALUES(1992,'CONCESION-1989-00005', 4120.00, '1992-12-31', '1992-10-30');
INSERT INTO Canon(PeriodoAnual, idConcesion, valorMonetario, fechaMaxPago, fechaRealizacionPago)
VALUES(1993,'CONCESION-1989-00005', 4140.00, '1993-12-31', '1993-10-30');
INSERT INTO Canon(PeriodoAnual, idConcesion, valorMonetario, fechaMaxPago, fechaRealizacionPago)
VALUES(1994,'CONCESION-1989-00005', 4145.00, '1994-12-31', '1994-10-30');
INSERT INTO Canon(PeriodoAnual, idConcesion, valorMonetario, fechaMaxPago, fechaRealizacionPago)
VALUES(1995,'CONCESION-1989-00005', 4160.00, '1995-12-31', '1995-10-30');
INSERT INTO Canon(PeriodoAnual, idConcesion, valorMonetario, fechaMaxPago, fechaRealizacionPago)
VALUES(1996,'CONCESION-1989-00005', 4170.00, '1996-12-31', '1996-10-30');
INSERT INTO Canon(PeriodoAnual, idConcesion, valorMonetario, fechaMaxPago, fechaRealizacionPago)
VALUES(1997,'CONCESION-1989-00005', 4200.00, '1997-12-31', '1997-10-30');
INSERT INTO Canon(PeriodoAnual, idConcesion, valorMonetario, fechaMaxPago, fechaRealizacionPago)
VALUES(1998,'CONCESION-1989-00005', 4210.00, '1998-12-31', '1998-10-30');
INSERT INTO Canon(PeriodoAnual, idConcesion, valorMonetario, fechaMaxPago, fechaRealizacionPago)
VALUES(1999,'CONCESION-1989-00005', 4220.00, '1999-12-31', '1999-10-30');
INSERT INTO Canon(PeriodoAnual, idConcesion, valorMonetario, fechaMaxPago, fechaRealizacionPago)
VALUES(2000,'CONCESION-1989-00005', 4240.00, '2000-12-31', '2000-10-30');
INSERT INTO Canon(PeriodoAnual, idConcesion, valorMonetario, fechaMaxPago, fechaRealizacionPago)
VALUES(2001,'CONCESION-1989-00005', 4240.00, '2001-12-31', '2001-08-02');
INSERT INTO Canon(PeriodoAnual, idConcesion, valorMonetario, fechaMaxPago, fechaRealizacionPago)
VALUES(2002,'CONCESION-1989-00005', 4245.00, '2002-12-31', '2002-08-22');
INSERT INTO Canon(PeriodoAnual, idConcesion, valorMonetario, fechaMaxPago, fechaRealizacionPago)
VALUES(2003,'CONCESION-1989-00005', 4250.00, '2003-12-31', '2003-08-22');
INSERT INTO Canon(PeriodoAnual, idConcesion, valorMonetario, fechaMaxPago, fechaRealizacionPago)
VALUES(2004,'CONCESION-1989-00005', 4255.00, '2004-12-31', '2004-08-22');
INSERT INTO Canon(PeriodoAnual, idConcesion, valorMonetario, fechaMaxPago, fechaRealizacionPago)
VALUES(2005,'CONCESION-1989-00005', 4260.00, '2005-12-31', '2005-08-22');
INSERT INTO Canon(PeriodoAnual, idConcesion, valorMonetario, fechaMaxPago, fechaRealizacionPago)
VALUES(2006,'CONCESION-1989-00005', 4280.00, '2006-12-31', '2006-08-22');
INSERT INTO Canon(PeriodoAnual, idConcesion, valorMonetario, fechaMaxPago, fechaRealizacionPago)
VALUES(2007,'CONCESION-1989-00005', 4300.00, '2007-12-31', '2007-08-22');
INSERT INTO Canon(PeriodoAnual, idConcesion, valorMonetario, fechaMaxPago, fechaRealizacionPago)
VALUES(2008,'CONCESION-1989-00005', 4300.00, '2008-12-31', '2008-08-22');
INSERT INTO Canon(PeriodoAnual, idConcesion, valorMonetario, fechaMaxPago, fechaRealizacionPago)
VALUES(2009,'CONCESION-1989-00005', 4310.00, '2009-12-31', '2009-08-22');
INSERT INTO Canon(PeriodoAnual, idConcesion, valorMonetario, fechaMaxPago, fechaRealizacionPago)
VALUES(2010,'CONCESION-1989-00005', 4320.00, '2010-12-31', '2010-08-22');
INSERT INTO Canon(PeriodoAnual, idConcesion, valorMonetario, fechaMaxPago, fechaRealizacionPago)
VALUES(2011,'CONCESION-1989-00005', 4335.00, '2011-12-31', '2011-08-22');
INSERT INTO Canon(PeriodoAnual, idConcesion, valorMonetario, fechaMaxPago, fechaRealizacionPago)
VALUES(2012,'CONCESION-1989-00005', 4340.00, '2012-12-31', '2012-08-22');
INSERT INTO Canon(PeriodoAnual, idConcesion, valorMonetario, fechaMaxPago, fechaRealizacionPago)
VALUES(2013,'CONCESION-1989-00005', 4365.00, '2013-12-31', '2013-08-22');
INSERT INTO Canon(PeriodoAnual, idConcesion, valorMonetario, fechaMaxPago, fechaRealizacionPago)
VALUES(2014,'CONCESION-1989-00005', 4370.00, '2014-12-31', '2014-08-22');
INSERT INTO Canon(PeriodoAnual, idConcesion, valorMonetario, fechaMaxPago, fechaRealizacionPago)
VALUES(2015,'CONCESION-1989-00005', 4378.00, '2015-12-31', '2015-08-22');
INSERT INTO Canon(PeriodoAnual, idConcesion, valorMonetario, fechaMaxPago, fechaRealizacionPago)
VALUES(2016,'CONCESION-1989-00005', 4390.00, '2016-12-31', '2016-08-22');
INSERT INTO Canon(PeriodoAnual, idConcesion, valorMonetario, fechaMaxPago, fechaRealizacionPago)
VALUES(2017,'CONCESION-1989-00005', 4395.00, '2017-12-31', '2017-08-22');
INSERT INTO Canon(PeriodoAnual, idConcesion, valorMonetario, fechaMaxPago, fechaRealizacionPago)
VALUES(2018,'CONCESION-1989-00005', 4400.00, '2018-12-31', '2018-08-22');
INSERT INTO Canon(PeriodoAnual, idConcesion, valorMonetario, fechaMaxPago, fechaRealizacionPago)
VALUES(2019,'CONCESION-1989-00005', 4407.00, '2019-12-31', '2019-08-22');
INSERT INTO Canon(PeriodoAnual, idConcesion, valorMonetario, fechaMaxPago, fechaRealizacionPago)
VALUES(2020,'CONCESION-1989-00005', 4417.00, '2020-12-31', '2020-08-22');

INSERT INTO Canon(PeriodoAnual, idConcesion, valorMonetario, fechaMaxPago, fechaRealizacionPago)
VALUES(2021,'CONCESION-2021-00006', 4420.00, '2021-12-31', '2021-08-22');
INSERT INTO Canon(PeriodoAnual, idConcesion, valorMonetario, fechaMaxPago, fechaRealizacionPago)
VALUES(2022,'CONCESION-2021-00006', 4430.00, '2022-12-31', '2022-08-22');
INSERT INTO Canon(PeriodoAnual, idConcesion, valorMonetario, fechaMaxPago, fechaRealizacionPago)
VALUES(2023,'CONCESION-2021-00006', 4435.00, '2023-12-31', '2023-08-22');
INSERT INTO Canon(PeriodoAnual, idConcesion, valorMonetario, fechaMaxPago, fechaRealizacionPago)
VALUES(2024,'CONCESION-2021-00006', 4445.00, '2024-12-31', '2024-08-22');
INSERT INTO Canon(PeriodoAnual, idConcesion, valorMonetario, fechaMaxPago, fechaRealizacionPago)
VALUES(2025,'CONCESION-2021-00006', 4450.00, '2025-12-31', '2025-08-22');


-- 8 Acta Inspeccion , Firma
INSERT INTO ActaInspeccion(numActa, idConcesion,  fechaYHora, motivo, objetoInspeccion)
VALUES(1195, 'CONCESION-1989-00005', '1989-05-03 11:35:11', 'control','inspeccion general');
INSERT INTO ActaInspeccion(numActa, idConcesion,  fechaYHora, motivo, objetoInspeccion)
VALUES(2495, 'CONCESION-1989-00005', '1994-04-21 11:35:11', 'control','inspeccion general');
INSERT INTO ActaInspeccion(numActa, idConcesion,  fechaYHora, motivo, objetoInspeccion)
VALUES(3765, 'CONCESION-1989-00005', '1999-02-16 11:35:11', 'control','inspeccion general');
INSERT INTO ActaInspeccion(numActa, idConcesion,  fechaYHora, motivo, objetoInspeccion)
VALUES(4385, 'CONCESION-1989-00005', '2003-07-10 11:35:11', 'vertido de residuos','vertido de residuos');
INSERT INTO ActaInspeccion(numActa, idConcesion,  fechaYHora, motivo, objetoInspeccion)
VALUES(5968, 'CONCESION-1989-00005', '2004-06-30 11:35:11', 'control','inspeccion general');
INSERT INTO ActaInspeccion(numActa, idConcesion,  fechaYHora, motivo, objetoInspeccion)
VALUES(7495, 'CONCESION-1989-00005', '2009-04-02 11:35:11', 'control','inspeccion general');
INSERT INTO ActaInspeccion(numActa, idConcesion,  fechaYHora, motivo, objetoInspeccion)
VALUES(10873, 'CONCESION-1989-00005', '2014-08-18 11:35:11', 'control','inspeccion general');
INSERT INTO ActaInspeccion(numActa, idConcesion,  fechaYHora, motivo, objetoInspeccion)
VALUES(11495, 'CONCESION-1989-00005', '2019-10-26 11:35:11', 'control','inspeccion general');

INSERT INTO ActaInspeccion(numActa, idConcesion,  fechaYHora, motivo, objetoInspeccion)
VALUES(14758, 'CONCESION-2021-00006', '2024-02-05 11:35:11', 'control anual','inspeccion general');

INSERT INTO Firma(numIdentificacion, numActa)VALUES('34574095N',1195);
INSERT INTO Firma(numIdentificacion, numActa)VALUES('34574095N',2495);
INSERT INTO Firma(numIdentificacion, numActa)VALUES('21286243L',2495);
INSERT INTO Firma(numIdentificacion, numActa)VALUES('34574095N',3765);
INSERT INTO Firma(numIdentificacion, numActa)VALUES('21286243L',3765);
INSERT INTO Firma(numIdentificacion, numActa)VALUES('34574095N',4385);
INSERT INTO Firma(numIdentificacion, numActa)VALUES('54286543B',4385);
INSERT INTO Firma(numIdentificacion, numActa)VALUES('34574095N',5968);
INSERT INTO Firma(numIdentificacion, numActa)VALUES('54286543B',5968);
INSERT INTO Firma(numIdentificacion, numActa)VALUES('34574095N',7495);
INSERT INTO Firma(numIdentificacion, numActa)VALUES('21286243L',7495);
INSERT INTO Firma(numIdentificacion, numActa)VALUES('34574095N',10873);
INSERT INTO Firma(numIdentificacion, numActa)VALUES('21286243L',10873);
INSERT INTO Firma(numIdentificacion, numActa)VALUES('34574095N',11495);
INSERT INTO Firma(numIdentificacion, numActa)VALUES('54286543B',11495);
INSERT INTO Firma(numIdentificacion, numActa)VALUES('34574095N',14758);

--9 Infraccion 
INSERT INTO Infraccion VALUES('INF-2003-07',NULL,4385,'Art. 58.3 h.','economica','muyGrave', '2003-07-10',16080.99,'liquidada');

--Concesion con agrupacion

---3 Parcela
INSERT INTO Parcela(referenciaCatastral, ubicacion, superficie)
VALUES('REFERENCIA-CAT-00007','Monte Bajo, Almonte', 1000.5);

INSERT INTO Parcela(referenciaCatastral, ubicacion, superficie)
VALUES('REFERENCIA-CAT-00008','Monte Bajo, Almonte', 725.87);

-- 4 Concesion
INSERT INTO Concesion(idConcesion,idAgrupacion,referenciaCatastral,fechaFormalizacion,
    fechaFinalizacion,estadoPlanAutoproteccion,usoAutorizado,estadoAdministrativo,
	porCesion,porMortisCausa)
VALUES('CONCESION-2021-00007','AGRUPACION-000000001','REFERENCIA-CAT-00007', '2021-02-02', '2024-12-31',
	'innecesario', 'agropecuario', 'extinguida', FALSE, FALSE);

INSERT INTO Concesion(idConcesion,idAgrupacion,referenciaCatastral,fechaFormalizacion,
    fechaFinalizacion,estadoPlanAutoproteccion,usoAutorizado,estadoAdministrativo,
	porCesion,porMortisCausa)
VALUES('CONCESION-2021-00008','AGRUPACION-000000002','REFERENCIA-CAT-00008', '2021-02-02', '2024-12-31',
	'innecesario', 'agropecuario', 'extinguida', FALSE, FALSE);

-- 5 Porcentaje
INSERT INTO Porcentaje(idConcesion, numIdentificacion, porcenateTitularidad)
VALUES('CONCESION-2021-00007', '62059700L',100);
INSERT INTO Porcentaje(idConcesion, numIdentificacion, porcenateTitularidad)
VALUES('CONCESION-2021-00008', '62059700L',100);

-- 6 Construccion
INSERT INTO Construccion(licenciaMunicipal, referenciaCatastral, valorTasacion, 
	fechaAutorizacion, tipoUso, estadoLegal)
VALUES('LICENCIA-AYUNTAMIENTO-0000000000000000000000000004', 'REFERENCIA-CAT-00007', 100400.50,'2021-06-01',
     'Almacen', 'legalizada');

-- 7 Canon
--- Pago durante todos los años
INSERT INTO Canon(PeriodoAnual, idConcesion, valorMonetario, fechaMaxPago, fechaRealizacionPago)
VALUES(2021,'CONCESION-2021-00007', 4000.00, '2021-12-31', '2021-12-09');
INSERT INTO Canon(PeriodoAnual, idConcesion, valorMonetario, fechaMaxPago, fechaRealizacionPago)
VALUES(2022,'CONCESION-2021-00007', 4300.00, '2022-12-31', NULL);
INSERT INTO Canon(PeriodoAnual, idConcesion, valorMonetario, fechaMaxPago, fechaRealizacionPago)
VALUES(2023,'CONCESION-2021-00007', 4310.00, '2023-12-31', NULL);
INSERT INTO Canon(PeriodoAnual, idConcesion, valorMonetario, fechaMaxPago, fechaRealizacionPago)
VALUES(2024,'CONCESION-2021-00007', 4315.00, '2024-12-31', NULL);

INSERT INTO Canon(PeriodoAnual, idConcesion, valorMonetario, fechaMaxPago, fechaRealizacionPago)
VALUES(2021,'CONCESION-2021-00008', 4000.00, '2021-12-31', '2021-12-09');
INSERT INTO Canon(PeriodoAnual, idConcesion, valorMonetario, fechaMaxPago, fechaRealizacionPago)
VALUES(2022,'CONCESION-2021-00008', 4300.00, '2022-12-31', NULL);
INSERT INTO Canon(PeriodoAnual, idConcesion, valorMonetario, fechaMaxPago, fechaRealizacionPago)
VALUES(2023,'CONCESION-2021-00008', 4310.00, '2023-12-31', NULL);
INSERT INTO Canon(PeriodoAnual, idConcesion, valorMonetario, fechaMaxPago, fechaRealizacionPago)
VALUES(2024,'CONCESION-2021-00008', 4315.00, '2024-12-31', NULL);


-- 8 Acta Inspeccion , Firma
INSERT INTO ActaInspeccion(numActa, idConcesion,  fechaYHora, motivo, objetoInspeccion)
VALUES(200845, 'CONCESION-2021-00007', '2021-06-12 11:00:00', 'construccion','Almacen');

INSERT INTO ActaInspeccion(numActa, idConcesion,  fechaYHora, motivo, objetoInspeccion)
VALUES(200889, 'CONCESION-2021-00008', '2021-06-12 12:00:00', 'control','Almacen');

INSERT INTO Firma(numIdentificacion, numActa)VALUES('Z5863297G',200845);
INSERT INTO Firma(numIdentificacion, numActa)VALUES('54286543B',200845);
INSERT INTO Firma(numIdentificacion, numActa)VALUES('Z5863297G',200889);
INSERT INTO Firma(numIdentificacion, numActa)VALUES('54286543B',200889);

--9 Infraccion 
INSERT INTO Infraccion VALUES('INF-2024-02','CONCESION-2021-00007',NULL,'Art 58.3 m.','temporal','muyGrave','2024-12-31', 
NULL,'noPagada');
INSERT INTO Infraccion VALUES('INF-2024-03','CONCESION-2021-00008',NULL,'Art 58.3 m.','temporal','muyGrave','2024-12-31', 
NULL,'noPagada');

-- Concesion de una empresa
-- 1. Personas
--      P1: Empresa
INSERT INTO Persona(numIdentificacion, nombre, fechaNacimiento, direccion, telefono, email)
VALUES( 'A34569087', 'Cooperativa Piensos Agralmonte S.L.', '2016-08-27', 'Poligono Industrial El Juncarejo, 99, 23854, (Jaén)',
	695955021, 'piensosAgralmonte@gmail.com');

-- 2 Roles
INSERT INTO Titular(numIdentificacion, razonSocial, incapacitado)VALUES('A34569087', 'agraria', NULL);

---3 Parcela
INSERT INTO Parcela(referenciaCatastral, ubicacion, superficie)
VALUES('REFERENCIA-CAT-00009','Monte Alto, Almonte', 600.25);

-- 4 Concesion
INSERT INTO Concesion(idConcesion,referenciaCatastral,fechaFormalizacion,
    fechaFinalizacion,estadoPlanAutoproteccion,usoAutorizado,estadoAdministrativo,
	porCesion,porMortisCausa)
VALUES('CONCESION-2023-00010','REFERENCIA-CAT-00009', '2023-03-11', '2053-03-11',
	'innecesario', 'agropecuario', 'vigente', TRUE, FALSE);

-- 5 Porcentaje
INSERT INTO Porcentaje(idConcesion, numIdentificacion, porcenateTitularidad)
VALUES('CONCESION-2023-00010', '70831256L',100);

-- 6 Construccion
INSERT INTO Construccion(licenciaMunicipal, referenciaCatastral, valorTasacion, 
	fechaAutorizacion, tipoUso, estadoLegal)
VALUES('LICENCIA-AYUNTAMIENTO-0000000000000000000000000006', 'REFERENCIA-CAT-00009', 1500.50,'2023-06-27',
     'Invernadero', 'legalizada');

-- 7 Canon
--- Pago durante todos los años
INSERT INTO Canon(PeriodoAnual, idConcesion, valorMonetario, fechaMaxPago, fechaRealizacionPago)
VALUES(2023,'CONCESION-2023-00010', 4000.00, '2023-12-31', '2023-11-02');
INSERT INTO Canon(PeriodoAnual, idConcesion, valorMonetario, fechaMaxPago, fechaRealizacionPago)
VALUES(2024,'CONCESION-2023-00010', 4000.00, '2024-12-31', '2024-09-18');
INSERT INTO Canon(PeriodoAnual, idConcesion, valorMonetario, fechaMaxPago, fechaRealizacionPago)
VALUES(2025,'CONCESION-2023-00010', 4000.00, '2025-12-31', '2025-10-05');

-- 8 Acta Inspeccion , Firma
INSERT INTO ActaInspeccion(numActa, idConcesion,  fechaYHora, motivo, objetoInspeccion)
VALUES(203460, 'CONCESION-2023-00010', '2024-04-03 11:35:11', 'control','inspeccion general');

INSERT INTO Firma(numIdentificacion, numActa)VALUES('Z5863297G',203460);
INSERT INTO Firma(numIdentificacion, numActa)VALUES('54286543B',203460);

--9 Infraccion (no tiene)

-- Concesiones 
-- 1. Insertamos Personas (Base para Titulares, Representantes e Inspectores)
-- P1: Persona física normal (Titular)
INSERT INTO Persona VALUES ('12345678A', 'Juan Pérez', '1980-05-15', 'Calle Mayor 1, Almonte', 600111222, 'juan.perez@email.com');
-- P2: Persona jurídica (Titular - Empresa)
INSERT INTO Persona VALUES ('B87654321', 'Construcciones Agricolas S.L.', '1990-01-01', 'Polígono Ind. Sur, Valencia', 960000001, 'contacto@agrarias.com');
-- P3: Menor de edad (Titular incapacitado por edad) - Nacido en 2015
INSERT INTO Persona VALUES ('99999999M', 'Luisito García', '2015-06-20', 'Calle monte 10, Almonte', 600333444, 'tutor@email.com');
-- P4: Representante (Padre de Luisito)
INSERT INTO Persona VALUES ('11112222R', 'Carlos García', '1975-03-10', 'Calle monte 10, Almonte', 600555666, 'carlos.garcia@email.com');
-- P5: Inspector Principal
INSERT INTO Persona VALUES ('55555555I', 'María López', '1982-11-30', 'Calle Inspección 1', 600777888, 'm.lopez@admin.es');
-- P6: Inspector de Apoyo
INSERT INTO Persona VALUES ('66666666I', 'Pedro Ruiz', '1995-02-14', 'Calle Inspección 2', 600999000, 'p.ruiz@admin.es');
-- Personas Herederos
INSERT INTO Persona VALUES ('33344455A', 'Ana Heredia', '1985-04-12', 'Calle Legado 1, Malaga', 611222333, 'ana.h@email.com');
INSERT INTO Persona VALUES ('44455566B', 'Berto Heredia', '1988-09-25', 'Calle Legado 1, Malaga', 622333444, 'berto.h@email.com');
INSERT INTO Persona VALUES ('55566677C', 'Carla Heredia', '2001-02-14', 'Av. Futuro 10, Madrid', 633444555, 'carla.h@email.com');

-- 2. Definición de Roles
INSERT INTO Titular VALUES ('12345678A', NULL, FALSE);
INSERT INTO Titular VALUES ('B87654321', 'Explotacion agraria', NULL);
INSERT INTO Titular VALUES ('99999999M', NULL, FALSE); 
INSERT INTO Titular VALUES ('33344455A', NULL, FALSE);
INSERT INTO Titular VALUES ('44455566B', NULL, FALSE);
INSERT INTO Titular VALUES ('55566677C', NULL, FALSE);

INSERT INTO Representante VALUES ('11112222R', 'Padre/Tutor');
INSERT INTO Tutela VALUES ('99999999M', '11112222R');

INSERT INTO Inspector VALUES ('55555555I', 'ACR-001', 'principal');
INSERT INTO Inspector VALUES ('66666666I', 'ACR-002', 'apoyo');

-- 3. Parcelas
INSERT INTO Parcela VALUES ('REFERENCIA-CAT-00001', 'Monte Alto, Almonte', 1500.50);
INSERT INTO Parcela VALUES ('REFERENCIA-CAT-00002', 'Monte Bajo, Almonte', 500.00);
INSERT INTO Parcela VALUES ('REFERENCIA-CAT-00003', 'Monte Medio, Almonte', 3000.00);
INSERT INTO Parcela VALUES ('REFERENCIA-CAT-H0003', 'Monte Herencia, Almonte', 800.00);

-- 4. Concesiones
INSERT INTO Concesion VALUES ('CONCESION-2020-00001', NULL, 'REFERENCIA-CAT-00001', '2020-01-01', '2050-01-01', 'aprobado', 'agropecuario', 'vigente', FALSE, FALSE);
-- Concesión REVOCADA
INSERT INTO Concesion VALUES ('CONCESION-2021-00002', NULL, 'REFERENCIA-CAT-00002', '2021-06-01', '2041-06-01', 'presentado', 'agropecuario', 'revocada', FALSE, FALSE);
INSERT INTO Concesion VALUES ('CONCESION-2019-00003', NULL, 'REFERENCIA-CAT-00003', '2019-01-01', '2054-01-01', 'revisado', 'agropecuario', 'vigente', FALSE, FALSE);
INSERT INTO Concesion VALUES ('CONCESION-HERENCIA-1', NULL, 'REFERENCIA-CAT-H0003', '2025-01-10', '2050-01-10', 'innecesario', 'agropecuario', 'vigente', TRUE, TRUE);

-- 5. Porcentaje de Titularidad
INSERT INTO Porcentaje VALUES ('12345678A', 'CONCESION-2020-00001', 100.0);
INSERT INTO Porcentaje VALUES ('99999999M', 'CONCESION-2021-00002', 100.0);
INSERT INTO Porcentaje VALUES ('B87654321', 'CONCESION-2019-00003', 100.0);
INSERT INTO Porcentaje VALUES ('33344455A', 'CONCESION-HERENCIA-1', 40.0);
INSERT INTO Porcentaje VALUES ('44455566B', 'CONCESION-HERENCIA-1', 40.0);
INSERT INTO Porcentaje VALUES ('55566677C', 'CONCESION-HERENCIA-1', 20.0);

-- 6. Construcciones
INSERT INTO Construccion VALUES ('LICENCIA-AYUNTAMIENTO-0000000000000000000000000001', 'REFERENCIA-CAT-00001', 120000.00, '2020-02-01', 'Invernadero', 'legalizada');
INSERT INTO Construccion VALUES ('LICENCIA-AYUNTAMIENTO-0000000000000000000000000002', 'REFERENCIA-CAT-00003', 500000.00, '2023-11-15', 'Industrial', 'enProceso');

-- 7. Cánones (MODIFICADO: Todos pagados)
-- A) CONCESION-2019-00003 (Empresa) - Desde 2019 hasta 2024. Todo pagado.
INSERT INTO Canon VALUES (2019, 'CONCESION-2019-00003', 4500.00, '2019-12-31', '2019-11-01');
INSERT INTO Canon VALUES (2020, 'CONCESION-2019-00003', 4600.00, '2020-12-31', '2020-10-15');
INSERT INTO Canon VALUES (2021, 'CONCESION-2019-00003', 4700.00, '2021-12-31', '2021-12-01');
INSERT INTO Canon VALUES (2022, 'CONCESION-2019-00003', 4800.00, '2022-12-31', '2022-11-20');
INSERT INTO Canon VALUES (2023, 'CONCESION-2019-00003', 4900.00, '2023-12-31', '2023-10-05');
INSERT INTO Canon VALUES (2024, 'CONCESION-2019-00003', 5000.00, '2024-12-31', '2024-11-20');

-- B) CONCESION-2020-00001 (Juan Pérez) - Desde 2020 hasta 2024. Todo pagado.
INSERT INTO Canon VALUES (2020, 'CONCESION-2020-00001', 1400.00, '2020-12-31', '2020-06-01');
INSERT INTO Canon VALUES (2021, 'CONCESION-2020-00001', 1420.00, '2021-12-31', '2021-06-10');
INSERT INTO Canon VALUES (2022, 'CONCESION-2020-00001', 1450.00, '2022-12-31', '2022-05-25');
INSERT INTO Canon VALUES (2023, 'CONCESION-2020-00001', 1480.00, '2023-12-31', '2023-07-01');
INSERT INTO Canon VALUES (2024, 'CONCESION-2020-00001', 1500.00, '2024-12-31', '2024-06-15');

-- C) CONCESION-2021-00002 (La REVOCADA - Luisito) - Desde 2021 hasta 2024.
--    El último año (2024) está SIN PAGAR (NULL).
INSERT INTO Canon VALUES (2021, 'CONCESION-2021-00002', 800.00, '2021-12-31', '2021-09-01');
INSERT INTO Canon VALUES (2022, 'CONCESION-2021-00002', 800.00, '2022-12-31', '2022-08-20');
INSERT INTO Canon VALUES (2023, 'CONCESION-2021-00002', 800.00, '2023-12-31', '2023-09-15');
INSERT INTO Canon VALUES (2024, 'CONCESION-2021-00002', 800.00, '2024-12-31', NULL);

-- D) CONCESION-HERENCIA-1 - Comienza en 2025.
INSERT INTO Canon VALUES (2025, 'CONCESION-HERENCIA-1', 1200.00, '2025-12-31', '2025-01-11');


-- 8. Actuaciones de Inspección y Firmas
INSERT INTO ActaInspeccion VALUES (1001, 'CONCESION-2020-00001', '2024-08-15 10:30:00', 'Control rutinario', 'Estructura terraza');
INSERT INTO Firma VALUES ('55555555I', 1001);

INSERT INTO ActaInspeccion VALUES (1002, 'CONCESION-2019-00003', '2024-09-01 09:00:00', 'Denuncia vertidos', 'Salida de aguas');
INSERT INTO Firma VALUES ('66666666I', 1002);

-- 9. Infracciones (MODIFICADO)
-- Infracción 1: Derivada del Acta 1002.
INSERT INTO Infraccion VALUES ('INF-2024-01',NULL, 1002, 'Art. 45.3 Ley Costas', 'economica', 'grave', '2024-09-10', 3000.50, 'noPagada');

-- Infracción 2: Asociada a la Concesión REVOCADA (CONCESION-2021-00002).
-- Requisitos cumplidos: Muy Grave, No Pagada, Tipo Económica (requiere importe).
INSERT INTO Infraccion VALUES ('INF-REVOCACION', 'CONCESION-2021-00002', NULL, 'Art. 90 Violacion Uso', 'economica', 'muyGrave', '2024-05-20', 75000.00, 'noPagada');