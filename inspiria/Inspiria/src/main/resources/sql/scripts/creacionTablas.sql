--- ##################################################################################################################### ---
-----------------------------------------------------------------------------------------------------------------------------
---                                                 Creación de base de datos
-----------------------------------------------------------------------------------------------------------------------------
--- ##################################################################################################################### ---


DROP DATABASE IF EXISTS inspiria;
CREATE DATABASE inspiria;
USE inspiria;

SET FOREIGN_KEY_CHECKS = 0;


--- ##################################################################################################################### ---
-----------------------------------------------------------------------------------------------------------------------------
---                                                     Eliminación de tablas
-----------------------------------------------------------------------------------------------------------------------------
--- ##################################################################################################################### ---


DROP TABLE IF EXISTS Entrada;
DROP TABLE IF EXISTS Contacto;
DROP TABLE IF EXISTS Muro;
DROP TABLE IF EXISTS TutorialesGuardados;
DROP TABLE IF EXISTS PerfilesSeguidos;
DROP TABLE IF EXISTS TematicasSeguidas;
DROP TABLE IF EXISTS CategoriasSeguidas;
DROP TABLE IF EXISTS Comentario;
DROP TABLE IF EXISTS Material;
DROP TABLE IF EXISTS Herramienta;
DROP TABLE IF EXISTS Paso;
DROP TABLE IF EXISTS Producto;
DROP TABLE IF EXISTS Tutorial;
DROP TABLE IF EXISTS Tematica;
DROP TABLE IF EXISTS Categoria;
DROP TABLE IF EXISTS Usuario;


--- ##################################################################################################################### ---
-----------------------------------------------------------------------------------------------------------------------------
---                                                     Creación de tablas
-----------------------------------------------------------------------------------------------------------------------------
--- ##################################################################################################################### ---


CREATE TABLE Usuario (
    idUsuario INT AUTO_INCREMENT,
    nombre VARCHAR(20) NOT NULL UNIQUE,
    correo VARCHAR(100) NOT NULL UNIQUE,
    passwordUsuario VARCHAR(255) NOT NULL,
    fechaRegistro DATE NOT NULL DEFAULT CURRENT_DATE,
    descripcion VARCHAR(100),
    aceptaComisiones BOOLEAN NOT NULL DEFAULT FALSE,
    fotoPerfil LONGBLOB,
    PRIMARY KEY (idUsuario)
);

CREATE TABLE Categoria (
    idCategoria INT AUTO_INCREMENT,
    nombreCategoria ENUM('Cerámica', 'Crochet', 'Bisutería', 'Costura', 'Papelería', 'Alfarería', 'Papiroflexia') NOT NULL,
    numVisitasCategoria INT DEFAULT 0,
    imagenCategoria LONGBLOB,
    PRIMARY KEY (idCategoria)
);

CREATE TABLE Tematica (
    idTematica INT AUTO_INCREMENT,
    nombreTematica ENUM('Navidad', 'Halloween', 'San Valentín', 'Hogar', 'Otoñal', 'Primavera',
                         'Verano', 'Invierno', 'Cumpleaños', 'Bodas', 'Infantil', 'Fiestas') NOT NULL,
    numVisitasTematica INT DEFAULT 0,
    imagenTematica LONGBLOB,
    PRIMARY KEY (idTematica)
);

CREATE TABLE Tutorial (
    idTutorial INT AUTO_INCREMENT,
    idUsuario INT NOT NULL,
    fechaPublicacion DATE NOT NULL DEFAULT CURRENT_DATE,
    titulo VARCHAR(25) NOT NULL,
    descripcion VARCHAR(500),
    dificultad ENUM('PRINCIPIANTE', 'INTERMEDIO', 'AVANZADO') NOT NULL,
    costeEstimado DECIMAL(10,2), 
    duracion INT NOT NULL, -- guardar en minutos y pasar en jsp a horas y minutos
    idCategoria INT NOT NULL,
    idTematica INT NOT NULL,
    portada LONGBLOB,
    PRIMARY KEY (idTutorial),
    FOREIGN KEY (idUsuario) REFERENCES Usuario(idUsuario) ON DELETE CASCADE,
    FOREIGN KEY (idCategoria) REFERENCES Categoria(idCategoria),
    FOREIGN KEY (idTematica) REFERENCES Tematica(idTematica)
);

CREATE TABLE Producto (
    idProducto INT AUTO_INCREMENT,
    idUsuario INT NOT NULL,
    idTutorial INT DEFAULT NULL UNIQUE,
    titulo VARCHAR(125) NOT NULL,
    fechaPublicacion DATE NOT NULL DEFAULT CURRENT_DATE,
    descripcion VARCHAR(500) NOT NULL,
    precio DECIMAL(10,2) NOT NULL, 
    stock ENUM('DISPONIBLE', 'AGOTADO') NOT NULL DEFAULT 'DISPONIBLE',
    imagen LONGBLOB,
    idCategoria INT NOT NULL,
    idTematica INT NOT NULL,
    PRIMARY KEY (idProducto),
    FOREIGN KEY (idUsuario) REFERENCES Usuario(idUsuario) ON DELETE CASCADE,
    FOREIGN KEY (idTutorial) REFERENCES Tutorial(idTutorial) ON DELETE SET NULL,
    FOREIGN KEY (idCategoria) REFERENCES Categoria(idCategoria),
    FOREIGN KEY (idTematica) REFERENCES Tematica(idTematica),
    CONSTRAINT precio_producto_positivo CHECK (precio >= 0)
);

CREATE TABLE Paso (
    numero INT NOT NULL,
    idTutorial INT NOT NULL,
    descripcion VARCHAR(500) NOT NULL,
    imagenPaso LONGBLOB,
    PRIMARY KEY (numero, idTutorial),
    FOREIGN KEY (idTutorial) REFERENCES Tutorial(idTutorial) ON DELETE CASCADE
);

CREATE TABLE Herramienta (
    idHerramienta INT AUTO_INCREMENT,
    idTutorial INT NOT NULL,
    nombre VARCHAR(50) NOT NULL,
    coste DECIMAL(10,2) NOT NULL,
    urlHerramienta VARCHAR(512) NOT NULL,
    PRIMARY KEY (idHerramienta),
    FOREIGN KEY (idTutorial) REFERENCES Tutorial(idTutorial) ON DELETE CASCADE,
    CONSTRAINT coste_herramienta_positivo CHECK (coste >= 0)
);

CREATE TABLE Material (
    idMaterial INT AUTO_INCREMENT,
    idTutorial INT NOT NULL,
    nombre VARCHAR(50) NOT NULL,
    coste DECIMAL(10,2) NOT NULL,
    urlMaterial VARCHAR(512) NOT NULL,
    PRIMARY KEY (idMaterial),
    FOREIGN KEY (idTutorial) REFERENCES Tutorial(idTutorial) ON DELETE CASCADE,
    CONSTRAINT coste_material_positivo CHECK (coste >= 0)
);

CREATE TABLE Comentario (
    idComentario INT AUTO_INCREMENT,
    idUsuario INT NOT NULL,
    idTutorial INT NOT NULL,
    texto VARCHAR(512) NOT NULL,
    fechaCreacion DATE NOT NULL DEFAULT CURRENT_DATE,
    PRIMARY KEY (idComentario),
    FOREIGN KEY (idUsuario) REFERENCES Usuario(idUsuario) ON DELETE CASCADE,
    FOREIGN KEY (idTutorial) REFERENCES Tutorial(idTutorial) ON DELETE CASCADE
);

CREATE TABLE TutorialesGuardados (
    idUsuario INT NOT NULL,
    idTutorial INT NOT NULL,
    PRIMARY KEY (idUsuario, idTutorial),
    FOREIGN KEY (idUsuario) REFERENCES Usuario(idUsuario) ON DELETE CASCADE,
    FOREIGN KEY (idTutorial) REFERENCES Tutorial(idTutorial) ON DELETE CASCADE
);

CREATE TABLE Muro (
    idUsuario INT NOT NULL,
    idTutorial INT NOT NULL,
    PRIMARY KEY (idUsuario, idTutorial),
    FOREIGN KEY (idUsuario) REFERENCES Usuario(idUsuario) ON DELETE CASCADE,
    FOREIGN KEY (idTutorial) REFERENCES Tutorial(idTutorial) ON DELETE CASCADE
);

CREATE TABLE PerfilesSeguidos (
    idUsuarioSigue INT NOT NULL,
    idUsuarioSeguido INT NOT NULL,
    PRIMARY KEY (idUsuarioSigue, idUsuarioSeguido),
    FOREIGN KEY (idUsuarioSigue) REFERENCES Usuario(idUsuario) ON DELETE CASCADE,
    FOREIGN KEY (idUsuarioSeguido) REFERENCES Usuario(idUsuario) ON DELETE CASCADE,
    CONSTRAINT no_autoseguirse CHECK (idUsuarioSigue <> idUsuarioSeguido)
);

CREATE TABLE CategoriasSeguidas (
    idUsuario INT NOT NULL,
    idCategoria INT NOT NULL,
    PRIMARY KEY (idUsuario, idCategoria),
    FOREIGN KEY (idUsuario) REFERENCES Usuario(idUsuario) ON DELETE CASCADE,
    FOREIGN KEY (idCategoria) REFERENCES Categoria(idCategoria) ON DELETE CASCADE
);

CREATE TABLE TematicasSeguidas (
    idUsuario INT NOT NULL,
    idTematica INT NOT NULL,
    PRIMARY KEY (idUsuario, idTematica),
    FOREIGN KEY (idUsuario) REFERENCES Usuario(idUsuario) ON DELETE CASCADE,
    FOREIGN KEY (idTematica) REFERENCES Tematica(idTematica) ON DELETE CASCADE
);

CREATE TABLE Contacto (
    idContacto INT AUTO_INCREMENT,
    idUsuario INT NOT NULL,
    link VARCHAR(512) NOT NULL,
    plataforma VARCHAR(50),
    PRIMARY KEY (idContacto),
    FOREIGN KEY (idUsuario) REFERENCES Usuario(idUsuario) ON DELETE CASCADE
);

CREATE TABLE Entrada (
    idEntrada INT AUTO_INCREMENT,
    idUsuario INT NOT NULL,
    precioBase DECIMAL(10,2) NOT NULL,
    descripcion VARCHAR(100) NOT NULL,
    PRIMARY KEY (idEntrada),
    FOREIGN KEY (idUsuario) REFERENCES Usuario(idUsuario) ON DELETE CASCADE,
    CONSTRAINT precio_entrada_positivo CHECK (precioBase >= 0)
);


--- ##################################################################################################################### ---
-----------------------------------------------------------------------------------------------------------------------------
---                          Inserción de Categorías y Temáticas para el funcionamiento de la aplicación
-----------------------------------------------------------------------------------------------------------------------------
--- ##################################################################################################################### ---


INSERT INTO Categoria (nombreCategoria, numVisitasCategoria, imagenCategoria) VALUES 
('Cerámica', 0, NULL),
('Crochet', 0, NULL),
('Bisutería', 0, NULL),
('Costura', 0, NULL),
('Papelería', 0, NULL),
('Alfarería', 0, NULL),
('Papiroflexia', 0, NULL);

INSERT INTO Tematica (nombreTematica, numVisitasTematica, imagenTematica) VALUES 
('Navidad', 0, NULL), 
('Halloween', 0, NULL), 
('San Valentín', 0, NULL), 
('Hogar', 0, NULL), 
('Otoñal', 0, NULL),
('Primavera', 0, NULL),
('Verano', 0, NULL),
('Invierno', 0, NULL),
('Cumpleaños', 0, NULL),
('Bodas', 0, NULL),
('Infantil', 0, NULL),
('Fiestas', 0, NULL);


--- ##################################################################################################################### ---
-----------------------------------------------------------------------------------------------------------------------------
---                          Inserción de datos de prueba para el funcionamiento de la aplicación
-----------------------------------------------------------------------------------------------------------------------------
--- ##################################################################################################################### ---

INSERT INTO Usuario (nombre, correo, passwordUsuario, fechaRegistro, descripcion, aceptaComisiones) 
VALUES  
    ('maria_perez', 'mariaperez@email.com', 'contraseña', '2025-01-10', 'Hola! Soy Maria Pérez', TRUE),
    ('juan_diy', 'juan@email.com', 'contraseña', '2025-02-15', 'Amante del crochet', FALSE),
    ('laura_art', 'laura@email.com', 'contraseña', '2025-03-05', 'Ceramista profesional', TRUE),
    ('carlos_craft', 'carlos@email.com', '12345678', '2025-04-12', 'Carpintero aficionado', TRUE),
    ('ana_crochet', 'ana@email.com', 'password', '2025-05-20', 'Tejo sueños', TRUE),
    ('pablo_papel', 'pablo@email.com', 'pablo123', '2025-06-01', 'Origami artesanal', FALSE);

INSERT INTO Tutorial (idUsuario, fechaPublicacion, titulo, descripcion, dificultad, costeEstimado, duracion, idCategoria, idTematica, portada)
VALUES
    (1, '2025-03-15', 'Taza de cerámica', 'Tutorial básico de modelado en barro.', 'PRINCIPIANTE', 15.00, 120, 1, 4, NULL),
    (2, '2025-05-10', 'Bufanda de crochet', 'Teje una bufanda con punto alto paso a paso.', 'INTERMEDIO', 10.00, 90, 2, 1, NULL),
    (1, '2025-08-22', 'Plato decorativo', 'Técnica de prensado de flores en arcilla.', 'AVANZADO', 20.00, 180, 1, 4, NULL),
    (3, '2026-02-01', 'Pendientes Corazón', 'Arcilla polimérica para San Valentín.', 'PRINCIPIANTE', 8.50, 45, 3, 3, NULL),
    (4, '2026-04-15', 'Bolsa de tela fácil', 'Costura creativa para principiantes.', 'INTERMEDIO', 12.00, 60, 4, 4, NULL);


INSERT INTO Comentario (idUsuario, idTutorial, texto, fechaCreacion) VALUES
    -- Tutorial 1 (Taza - Publicado 2025-03-15)
    (2, 1, '¡Me encanta! ¿Qué tipo de arcilla recomiendas para que no se agriete?', '2025-10-12'),
    (5, 1, 'Muy bien explicado, mañana mismo compro los materiales.', '2025-11-05'),
    (3, 1, 'Lo intenté y me quedó un poco torcida, ¡pero tiene su encanto!', '2026-01-20'),
    
    -- Tutorial 2 (Bufanda - Publicado 2025-05-10)
    (1, 2, 'Juan, ¿podrías hacer un video sobre cómo cambiar de color de hilo?', '2025-12-01'),
    (6, 2, 'La bufanda queda genial, el punto alto es mi favorito.', '2025-12-15'),
    (4, 2, '¿Sirve lana de algodón o mejor lana merino?', '2026-02-10'),
    
    -- Tutorial 3 (Plato - Publicado 2025-08-22)
    (3, 3, 'Increíble técnica, el acabado con flores secas queda muy profesional.', '2026-03-02'),
    (4, 3, '¿Es necesario barnizarlo después de que se seque al aire?', '2026-03-05'),
    
    -- Tutorial 4 (Pendientes - Publicado 2026-02-01)
    (1, 4, '¡Qué monos! Ideales para regalar en San Valentín.', '2026-02-05'), -- Post-publicación
    (2, 4, '¿Se pueden hacer con arcilla que no necesite horno?', '2026-02-14'),
    
    -- Tutorial 5 (Bolsa - Publicado 2026-04-15)
    (5, 5, '¿Qué tipo de aguja recomiendas para esta loneta?', '2026-04-16'), -- Post-publicación
    (3, 5, 'Tutorial súper útil, ya tengo mi tote bag lista.', '2026-04-20'),
    (1, 5, '¡Me apunto este proyecto para el fin de semana!', '2026-04-25');


-- 1. Productos ASOCIADOS a Tutoriales
INSERT INTO Producto (idUsuario, idTutorial, titulo, fechaPublicacion, descripcion, precio, stock, idCategoria, idTematica, imagen)
VALUES
    -- Taza de barro (Objeto utilitario -> Alfarería ID 6)
    (1, 1, 'Taza de cerámica', '2025-03-15', 'Tutorial básico de modelado en barro.', 22.50, 'DISPONIBLE', 6, 4, NULL),

    -- Bufanda (ID 2)
    (2, 2, 'Bufanda de crochet', '2025-05-10', 'Teje una bufanda con punto alto paso a paso.', 18.00, 'DISPONIBLE', 2, 1, NULL);


-- 2. Productos INDEPENDIENTES
INSERT INTO Producto (idUsuario, idTutorial, titulo, fechaPublicacion, descripcion, precio, stock, idCategoria, idTematica, imagen)
VALUES
    -- Amigurumi (ID 2)
    (5, NULL, 'Amigurumi Ballena Azul', '2025-11-20', 'Pequeño peluche de crochet hecho a mano.', 25.00, 'DISPONIBLE', 2, 4, NULL),

    -- Lámpara (ID 5)
    (6, NULL, 'Lámpara de Origami', '2026-01-10', 'Lámpara geométrica de papel reforzado.', 45.00, 'DISPONIBLE', 5, 4, NULL),

    -- Jarrón (Pieza torneada -> Alfarería ID 6)
    (3, NULL, 'Jarrón Minimalista', '2026-03-15', 'Pieza de alfarería torneada con acabado en mate.', 60.00, 'DISPONIBLE', 6, 4, NULL);

INSERT INTO Paso (idTutorial, numero, descripcion, imagenPaso)
VALUES
    -- TUTORIAL 1: Taza de cerámica
    (1, 1, 'Prepara la arcilla amasándola sobre una superficie porosa para eliminar burbujas de aire.', NULL),
    (1, 2, 'Crea una base circular plana de 1 cm de grosor usando un rodillo o las manos.', NULL),
    (1, 3, 'Forma cilindros alargados (churros) y colócalos sobre la base para levantar las paredes.', NULL),
    (1, 4, 'Alisa las uniones con una paleta de madera o una esponja húmeda hasta que la superficie sea uniforme.', NULL),
    (1, 5, 'Modela el asa aparte y únela al cuerpo de la taza usando barbotina (arcilla líquida) para asegurar la adherencia.', NULL),

    -- TUTORIAL 2: Bufanda de crochet
    (2, 1, 'Realiza un nudo corredizo y teje una cadena de base de aproximadamente 30 puntos.', NULL),
    (2, 2, 'En la primera fila, teje puntos altos en cada una de las cadenas base para dar cuerpo a la bufanda.', NULL),
    (2, 3, 'Gira el tejido, sube con tres cadenas y repite el punto alto en cada espacio de la fila anterior.', NULL),
    (2, 4, 'Continúa tejiendo hasta alcanzar una longitud de 150 cm o el largo deseado.', NULL),
    (2, 5, 'Corta el hilo dejando una hebra de 10 cm, pásala por el último bucle y escóndela con una aguja lanera.', NULL),

    -- TUTORIAL 3: Plato decorativo (Flores prensadas)
    (3, 1, 'Extiende una plancha de arcilla de forma circular con un diámetro de unos 20 cm.', NULL),
    (3, 2, 'Selecciona flores y hojas secas y disponlas sobre la arcilla creando una composición armoniosa.', NULL),
    (3, 3, 'Pasa el rodillo suavemente sobre las flores para que queden impresas y niveladas con la superficie.', NULL),
    (3, 4, 'Coloca la plancha sobre un molde cóncavo para que tome la forma de plato mientras seca.', NULL),
    (3, 5, 'Una vez seca, retira los restos vegetales y lija suavemente los bordes para un acabado profesional.', NULL),

    -- TUTORIAL 4: Pendientes Corazón
    (4, 1, 'Acondiciona la arcilla polimérica roja frotándola con las manos hasta que esté maleable.', NULL),
    (4, 2, 'Extiende la arcilla con un rodillo de silicona buscando un grosor uniforme de 3 mm.', NULL),
    (4, 3, 'Usa un cortador metálico en forma de corazón para extraer las piezas de los pendientes.', NULL),
    (4, 4, 'Hornea las piezas siguiendo las instrucciones del fabricante (normalmente 110°C durante 30 min).', NULL),
    (4, 5, 'Perfora la parte superior con un punzón y coloca las anillas y los ganchos para pendientes.', NULL),

    -- TUTORIAL 5: Bolsa de tela fácil
    (5, 1, 'Corta dos rectángulos de tela de 40x45 cm y dos tiras de 60 cm para las asas.', NULL),
    (5, 2, 'Enfrenta los derechos de los rectángulos y cose los laterales y el fondo con una puntada recta.', NULL),
    (5, 3, 'Realiza un dobladillo doble en la parte superior de la bolsa y fíjalo con alfileres.', NULL),
    (5, 4, 'Cose el dobladillo superior y asegura las asas en su posición mediante una costura en forma de cuadrado.', NULL),
    (5, 5, 'Plancha la bolsa terminada para asentar las costuras y darle una apariencia impecable.', NULL);

INSERT INTO Material (idTutorial, nombre, coste, urlMaterial)
VALUES
    -- Materiales para TUTORIAL 1: Taza de cerámica
    (1, 'Arcilla Blanca de Modelar (1.5kg)', 4.95, 'https://www.creavea.es/arcilla-blanca-de-modelar-15-kg_tienda-recurso_8558.html'),
    (1, 'Barbotina líquida (500ml)', 6.20, 'https://www.marphil.com/producto/barbotina-blanca-liquida/'),

    -- Materiales para TUTORIAL 2: Bufanda de crochet
    (2, 'Lana Merino Aran (100g)', 8.50, 'https://www.puntosdefantasia.com/lana-drops-big-merino'),
    (2, 'Aguja de crochet 5mm', 3.75, 'https://www.lanasstop.com/accesorios/agujas-ganchillo-aluminio'),

    -- Materiales para TUTORIAL 3: Plato decorativo
    (3, 'Pasta de modelar secado al aire (1kg)', 5.25, 'https://www.milbby.com/products/pasta-de-modelar-das-blanca-1kg'),
    (3, 'Set de flores prensadas naturales', 12.90, 'https://www.creavea.es/flores-prensadas-secas-naturales-x-20_tienda-recurso_105260.html'),

    -- Materiales para TUTORIAL 4: Pendientes Corazón
    (4, 'Arcilla Polimérica FIMO Soft Roja', 2.85, 'https://www.vpiera.com/es/fimo-soft-57gr-rojo-cereza-026'),
    (4, 'Ganchos para pendientes de Plata de Ley', 4.50, 'https://www.cooksongold.es/Hallfindings/Pendientes-Ganchos-De-Plata-925-prcode-NVQ-000'),

    -- Materiales para TUTORIAL 5: Bolsa de tela fácil
    (5, 'Tela de Loneta de Algodón (1 metro)', 9.00, 'https://www.telas.es/loneta-de-algodon-natural-p-203_1106_001.html'),
    (5, 'Hilo de coser Gütermann poliéster', 2.50, 'https://www.tiendatelas.com/hilo-gutermann-coselotodo-100m');


INSERT INTO Herramienta (idTutorial, nombre, coste, urlHerramienta)
VALUES
    -- Herramientas para TUTORIAL 1: Taza de cerámica
    (1, 'Kit de 8 herramientas para modelar', 12.50, 'https://www.amazon.es/herramientas-modelado-arcilla-ceramica/dp/B07996X9R5'),
    (1, 'Rodillo de madera para amasar', 8.90, 'https://www.marphil.com/producto/rodillo-de-madera-con-asas/'),

    -- Herramientas para TUTORIAL 2: Bufanda de crochet
    (2, 'Set de ganchillos ergonómicos', 15.00, 'https://www.puntosdefantasia.com/set-ganchillos-knitpro-waves'),
    (2, 'Tijeras de costura cigüeña', 6.50, 'https://www.lanasstop.com/accesorios/tijeras-bordar-ciguena'),

    -- Herramientas para TUTORIAL 3: Plato decorativo
    (3, 'Guías de madera para grosor (1cm)', 9.95, 'https://www.creavea.es/listones-de-madera-para-arcilla_tienda-recurso_1020.html'),
    (3, 'Esponja de alfarero sintética', 1.50, 'https://www.milbby.com/products/esponja-para-ceramica'),

    -- Herramientas para TUTORIAL 4: Pendientes Corazón
    (4, 'Rodillo de metacrilato para FIMO', 7.20, 'https://www.vpiera.com/es/rodillo-acrilico-fimo'),
    (4, 'Alicates de punta redonda para bisutería', 5.50, 'https://www.amazon.es/alicates-bisuteria-punta-redonda/dp/B0058ED39A'),

    -- Herramientas para TUTORIAL 5: Bolsa de tela fácil
    (5, 'Máquina de coser Singer Tradition', 169.00, 'https://www.leroymerlin.es/productos/maquina-de-coser-singer-tradition-2250-81923055.html'),
    (5, 'Cinta métrica de costura (150cm)', 1.20, 'https://www.telas.es/cinta-metrica-hoechstmass-p-01_1001_111.html');


INSERT INTO TutorialesGuardados (idUsuario, idTutorial) VALUES 
    (1, 2), -- Guarda la "Bufanda de crochet"
    (1, 4); -- Guarda los "Pendientes Corazón"


--- ##################################################################################################################### ---
-----------------------------------------------------------------------------------------------------------------------------
---                          Nuevos usuarios, tutoriales, productos y datos de prueba ampliados
-----------------------------------------------------------------------------------------------------------------------------
--- ##################################################################################################################### ---


-- Nuevos usuarios (IDs 7 y 8)
INSERT INTO Usuario (nombre, correo, passwordUsuario, fechaRegistro, descripcion, aceptaComisiones) 
VALUES  
    ('sofia_tejidos',  'sofia@email.com', 'sofia123', '2025-07-15', 'Costura y tejidos artesanales', TRUE),
    ('marta_bordados', 'marta@email.com', 'marta123', '2025-08-20', 'Bisutería y manualidades',      FALSE);


-- Nuevos tutoriales (IDs 6–13)
-- Categorías cubiertas con tutorial: Papelería(5) y Alfarería(6) — quedaba sin tutorial, ahora tienen
-- Categoría SIN publicación intencionada: Papiroflexia(7)
-- Temáticas cubiertas: Halloween(2), Otoñal(5), Primavera(6), Verano(7), Invierno(8), Cumpleaños(9), Infantil(11), Fiestas(12)
-- Temática SIN publicación intencionada: Bodas(10)
INSERT INTO Tutorial (idUsuario, fechaPublicacion, titulo, descripcion, dificultad, costeEstimado, duracion, idCategoria, idTematica, portada)
VALUES
    (7, '2025-09-01', 'Linterna Halloween',  'Linterna de papel para decorar en Halloween.',           'PRINCIPIANTE',  5.00,  60,  5, 2,  NULL), -- id 6
    (3, '2025-09-20', 'Cuenco otoñal',       'Cuenco de alfarería con textura de hojas secas.',        'INTERMEDIO',   18.00, 150,  6, 5,  NULL), -- id 7
    (5, '2026-03-10', 'Bolso de primavera',  'Bolso de crochet en colores pastel para primavera.',     'INTERMEDIO',   14.00, 120,  2, 6,  NULL), -- id 8
    (8, '2026-05-05', 'Pendientes verano',   'Pendientes de resina epoxi para el verano.',             'PRINCIPIANTE',  9.00,  50,  3, 7,  NULL), -- id 9
    (7, '2025-11-10', 'Cojín de invierno',   'Cojín de tela polar cosido a mano para el invierno.',   'PRINCIPIANTE', 11.00,  70,  4, 8,  NULL), -- id 10
    (3, '2026-01-25', 'Taza cumpleaños',     'Taza de cerámica personalizable como regalo.',           'INTERMEDIO',   16.00, 130,  1, 9,  NULL), -- id 11
    (5, '2026-02-20', 'Peluche infantil',    'Amigurumi de crochet con forma de osito para niños.',   'PRINCIPIANTE', 12.00,  90,  2, 11, NULL), -- id 12
    (8, '2026-04-01', 'Guirnalda de papel',  'Guirnalda de eslabones de papel para decorar fiestas.', 'PRINCIPIANTE',  4.00,  45,  5, 12, NULL); -- id 13


INSERT INTO Paso (idTutorial, numero, descripcion, imagenPaso)
VALUES
    -- Tutorial 6: Linterna Halloween
    (6, 1, 'Dobla una hoja de papel negro por la mitad a lo largo dejando un margen de 2 cm en el borde superior.', NULL),
    (6, 2, 'Realiza cortes paralelos desde el doblez sin llegar al margen superior, separados aproximadamente 1 cm entre sí.', NULL),
    (6, 3, 'Desdobla el papel con cuidado y únelo formando un cilindro, pegando los extremos con cola o celo.', NULL),
    (6, 4, 'Recorta una tira de papel naranja de 1x20 cm y pégala en la parte superior a modo de asa.', NULL),
    (6, 5, 'Añade ojos y boca recortados en papel naranja y pégalos en el cilindro para darle forma de calabaza.', NULL),

    -- Tutorial 7: Cuenco otoñal
    (7, 1, 'Amasa la arcilla de alfarería sobre una superficie lisa hasta que no queden burbujas de aire.', NULL),
    (7, 2, 'Extiende una plancha circular de 1,5 cm de grosor con el rodillo para formar la base del cuenco.', NULL),
    (7, 3, 'Levanta las paredes poco a poco con los pulgares usando la técnica de pellizco hasta obtener la forma deseada.', NULL),
    (7, 4, 'Presiona hojas secas de otoño sobre la superficie exterior para imprimir su textura natural.', NULL),
    (7, 5, 'Deja secar 48 horas, lija los bordes con suavidad y aplica esmalte en tonos ocres antes del horneado.', NULL),

    -- Tutorial 8: Bolso de primavera
    (8, 1, 'Realiza un nudo corredizo con hilo de algodón pastel y teje una cadena base de 60 puntos.', NULL),
    (8, 2, 'Trabaja en punto bajo durante 30 filas para formar el cuerpo rectangular del bolso.', NULL),
    (8, 3, 'Cierra los laterales con puntos deslizados para que el bolso tome forma de saco.', NULL),
    (8, 4, 'Teje dos asas de 40 cadenetas cada una y cóselas firmemente a ambos lados del borde superior.', NULL),
    (8, 5, 'Confecciona flores pequeñas de crochet en colores contrastantes y cóselas como adorno en la parte delantera.', NULL),

    -- Tutorial 9: Pendientes verano
    (9, 1, 'Mezcla los dos componentes de la resina epoxi en partes iguales según las instrucciones del fabricante.', NULL),
    (9, 2, 'Añade unas gotas de tinte azul o turquesa y pequeñas conchas o flores secas a la mezcla.', NULL),
    (9, 3, 'Vierte la mezcla en moldes de silicona con forma de gota o círculo y elimina las burbujas con un palillo.', NULL),
    (9, 4, 'Deja curar la resina 24 horas en un lugar seco, sin polvo ni humedad directa.', NULL),
    (9, 5, 'Desmolda las piezas, lija los bordes con lija de grano fino y coloca los ganchos con unos alicates de punta.', NULL),

    -- Tutorial 10: Cojín de invierno
    (10, 1, 'Corta dos cuadrados de tela polar de 45x45 cm con tijeras de costura bien afiladas.', NULL),
    (10, 2, 'Dibuja o aplica un apliqué de copo de nieve sobre la cara delantera del cojín.', NULL),
    (10, 3, 'Enfrenta los derechos de ambas telas y cose tres lados con puntada recta dejando un lado abierto.', NULL),
    (10, 4, 'Da la vuelta al cojín e introduce el relleno de guata hasta el grosor deseado.', NULL),
    (10, 5, 'Cierra el lado abierto a mano con punto de escalera para lograr un acabado invisible y resistente.', NULL),

    -- Tutorial 11: Taza cumpleaños
    (11, 1, 'Prepara una bola de arcilla blanca y trabájala con las manos hasta eliminar todas las burbujas.', NULL),
    (11, 2, 'Modela un cilindro de paredes uniformes de 5 mm usando la técnica de pellizco desde el interior.', NULL),
    (11, 3, 'Alisa la superficie exterior con una esponja húmeda y forma el asa por separado con un rulo de arcilla.', NULL),
    (11, 4, 'Une el asa al cuerpo usando barbotina y presiona bien las uniones; deja secar al menos 24 horas.', NULL),
    (11, 5, 'Decora con pequeñas bolitas o letras de arcilla de colores antes del secado final y aplica esmalte transparente.', NULL),

    -- Tutorial 12: Peluche infantil
    (12, 1, 'Teje 6 puntos en un anillo mágico y aumenta progresivamente hasta obtener 18 puntos para la cabeza.', NULL),
    (12, 2, 'Trabaja en redondo sin aumentos durante 6 vueltas para dar volumen al cuerpo del osito.', NULL),
    (12, 3, 'Disminuye gradualmente hasta cerrar el círculo y rellena la pieza con fibra de relleno hipoalergénica.', NULL),
    (12, 4, 'Teje las orejas, patitas y hocico por separado y cóselos a la cabeza con aguja lanera.', NULL),
    (12, 5, 'Coloca los ojos de seguridad antes de cerrar del todo y borda la nariz y boca con hilo negro.', NULL),

    -- Tutorial 13: Guirnalda de papel
    (13, 1, 'Corta tiras de papel de colores de 20x2 cm con tijeras o guillotina para obtener bordes limpios.', NULL),
    (13, 2, 'Forma un anillo con la primera tira superponiendo los extremos 1 cm y pégalos con cola de barra.', NULL),
    (13, 3, 'Pasa la siguiente tira por el interior del anillo anterior y ciérrala del mismo modo formando otro eslabón.', NULL),
    (13, 4, 'Repite el proceso alternando colores hasta alcanzar la longitud deseada para la guirnalda.', NULL),
    (13, 5, 'Refuerza los extremos de la cadena con una tira más gruesa doblada que sirva de punto de sujeción.', NULL);


INSERT INTO Material (idTutorial, nombre, coste, urlMaterial)
VALUES
    -- Tutorial 6: Linterna Halloween
    (6, 'Pack papel negro A4 (50 hojas)',          3.50,  'https://www.amazon.es/papel-negro-a4-colores/dp/B07KXQJ9J3'),
    (6, 'Pack papel naranja A4 (50 hojas)',         3.50,  'https://www.amazon.es/papel-naranja-a4-colores/dp/B07KXQJ9J4'),
    -- Tutorial 7: Cuenco otoñal
    (7, 'Arcilla de alfarería roja (2 kg)',        11.90, 'https://www.marphil.com/producto/arcilla-roja-de-alfareria/'),
    (7, 'Esmalte cerámico ocre (250 ml)',           8.50,  'https://www.creavea.es/esmalte-ceramico-ocre_tienda-recurso_2210.html'),
    -- Tutorial 8: Bolso de primavera
    (8, 'Hilo de algodón pastel rosa (100 g)',      6.90,  'https://www.lanasstop.com/hilos-algodon/katia-cotton-color'),
    (8, 'Hilo de algodón verde menta (50 g)',       4.50,  'https://www.puntosdefantasia.com/hilo-algodon-verano'),
    -- Tutorial 9: Pendientes verano
    (9, 'Kit resina epoxi cristal (200 ml)',       12.95, 'https://www.amazon.es/kit-resina-epoxi-cristal/dp/B08XY1Z2A3'),
    (9, 'Moldes silicona formas gota (12 ud)',      5.99,  'https://www.amazon.es/moldes-silicona-pendientes-resina/dp/B09Z3Q1R2T'),
    (9, 'Ganchos pendiente plata (20 ud)',          3.50,  'https://www.cooksongold.es/Hallfindings/Pendientes-Ganchos-De-Plata-925-prcode-NVQ-001'),
    -- Tutorial 10: Cojín de invierno
    (10, 'Tela polar blanca (0,5 m)',               4.80,  'https://www.telas.es/tela-polar-blanca-lisa-p-301_2001_001.html'),
    (10, 'Relleno guata para cojín (300 g)',        3.95,  'https://www.tiendatelas.com/relleno-guata-cojin-300g'),
    -- Tutorial 11: Taza cumpleaños
    (11, 'Arcilla blanca de modelar (1 kg)',        5.50,  'https://www.milbby.com/products/arcilla-blanca-modelar-1kg'),
    (11, 'Set arcilla polimérica colores x6',       8.90,  'https://www.vpiera.com/es/set-fimo-soft-colores'),
    -- Tutorial 12: Peluche infantil
    (12, 'Hilo amigurumi beige (100 g)',            5.95,  'https://www.lanasstop.com/hilos-amigurumi/drops-safran'),
    (12, 'Fibra de relleno hipoalergénica (100 g)', 3.50,  'https://www.amazon.es/fibra-relleno-munecos-hipoalergenica/dp/B07TK2L3N1'),
    (12, 'Ojos de seguridad negros 8 mm (10 ud)',   2.80,  'https://www.amazon.es/ojos-seguridad-amigurumi-negros/dp/B08MKQR9T7'),
    -- Tutorial 13: Guirnalda de papel
    (13, 'Pack papel de colores A4 (100 hojas)',    6.50,  'https://www.amazon.es/pack-papel-colores-a4/dp/B07PQRT2MN'),
    (13, 'Cola de barra Pritt (3 ud)',              3.20,  'https://www.amazon.es/pritt-stick-cola-barra-3-unidades/dp/B000WG65GO');


INSERT INTO Herramienta (idTutorial, nombre, coste, urlHerramienta)
VALUES
    -- Tutorial 6: Linterna Halloween
    (6, 'Tijeras de manualidades Fiskars',          8.90,  'https://www.amazon.es/fiskars-tijeras-manualidades/dp/B0000934LJ'),
    (6, 'Regla metálica 30 cm',                     2.50,  'https://www.amazon.es/regla-metalica-30cm/dp/B07MZXP4QK'),
    -- Tutorial 7: Cuenco otoñal
    (7, 'Torno de mano para principiantes',        35.00, 'https://www.marphil.com/producto/torno-de-mano-alfareria/'),
    (7, 'Rodillo de madera con asas',               8.90,  'https://www.marphil.com/producto/rodillo-de-madera-con-asas/'),
    -- Tutorial 8: Bolso de primavera
    (8, 'Ganchillo ergonómico 4 mm Clover',         9.50,  'https://www.puntosdefantasia.com/ganchillo-clover-amour-4mm'),
    (8, 'Aguja lanera de tapicería (5 ud)',          2.80,  'https://www.lanasstop.com/accesorios/agujas-laneras-tapiceria'),
    -- Tutorial 9: Pendientes verano
    (9, 'Alicates punta redonda bisutería',         5.50,  'https://www.amazon.es/alicates-bisuteria-punta-redonda/dp/B0058ED39A'),
    (9, 'Lija de agua grano 800 (5 hojas)',          1.95,  'https://www.amazon.es/lija-agua-grano-800/dp/B07QNWX9T2'),
    -- Tutorial 10: Cojín de invierno
    (10, 'Aguja de coser a mano curva (6 ud)',       3.20,  'https://www.tiendatelas.com/agujas-coser-mano-curvas'),
    (10, 'Alfileres de costura (100 ud)',            2.50,  'https://www.telas.es/alfileres-costura-100-unidades-p-01_2002_001.html'),
    -- Tutorial 11: Taza cumpleaños
    (11, 'Kit herramientas modelar (8 ud)',         12.50, 'https://www.amazon.es/herramientas-modelado-arcilla-ceramica/dp/B07996X9R5'),
    (11, 'Esponja de alfarero sintética',            1.50,  'https://www.milbby.com/products/esponja-para-ceramica'),
    -- Tutorial 12: Peluche infantil
    (12, 'Ganchillo ergonómico 3 mm Clover',        9.50,  'https://www.puntosdefantasia.com/ganchillo-clover-amour-3mm'),
    (12, 'Aguja lanera punta roma (5 ud)',           2.80,  'https://www.lanasstop.com/accesorios/agujas-laneras-punta-roma'),
    -- Tutorial 13: Guirnalda de papel
    (13, 'Guillotina de papel A4 Maped',           18.90, 'https://www.amazon.es/maped-guillotina-papel-a4/dp/B003W0E23O'),
    (13, 'Tijeras de manualidades Fiskars',          8.90,  'https://www.amazon.es/fiskars-tijeras-manualidades/dp/B0000934LJ');


-- Productos asociados a los nuevos tutoriales
INSERT INTO Producto (idUsuario, idTutorial, titulo, fechaPublicacion, descripcion, precio, stock, idCategoria, idTematica, imagen)
VALUES
    (3, 7,  'Cuenco otoñal',  '2025-10-01', 'Cuenco de alfarería con textura de hojas secas.', 35.00, 'DISPONIBLE', 6, 5,  NULL),
    (3, 11, 'Taza cumpleaños', '2026-02-10', 'Taza de cerámica personalizable como regalo.',           28.00, 'DISPONIBLE', 1, 9,  NULL),
    (5, 12, 'Peluche infantil',   '2026-03-05', 'Amigurumi de crochet con forma de osito para niños.',             22.00, 'DISPONIBLE', 2, 11, NULL);

-- Productos independientes de los nuevos usuarios
INSERT INTO Producto (idUsuario, idTutorial, titulo, fechaPublicacion, descripcion, precio, stock, idCategoria, idTematica, imagen)
VALUES
    (7, NULL, 'Set linternas Halloween', '2025-09-15', 'Pack de 3 linternas de papel artesanales para decorar en Halloween.', 12.00, 'DISPONIBLE', 5, 2,  NULL),
    (8, NULL, 'Set pendientes de resina','2026-05-10', 'Tres pares de pendientes de resina epoxi con tonos veraniegoss.',     18.00, 'DISPONIBLE', 3, 7,  NULL),
    (7, NULL, 'Cojín polar bordado',     '2025-12-01', 'Cojín de tela polar con bordado de copo de nieve, 45x45 cm.',        24.00, 'AGOTADO',    4, 8,  NULL);


INSERT INTO Comentario (idUsuario, idTutorial, texto, fechaCreacion) VALUES
    -- Tutorial 6: Linterna Halloween
    (1, 6, '¡Qué idea tan original! Lo haré con mis sobrinos esta Halloween.',      '2025-09-10'),
    (2, 6, 'Muy sencillo y queda genial, ideal para hacer en familia.',              '2025-10-15'),
    -- Tutorial 7: Cuenco otoñal
    (1, 7, '¿Qué tipo de arcilla usaste? La textura de las hojas queda preciosa.',  '2025-10-05'),
    (4, 7, 'El esmalte ocre combina perfecto con la temática otoñal.',               '2025-10-20'),
    -- Tutorial 8: Bolso de primavera
    (1, 8, '¿Con qué tipo de hilo queda mejor, algodón o acrílico?',                '2026-03-15'),
    (8, 8, 'El bolso en tonos menta y rosa queda precioso, ¡me apunto!',            '2026-03-22'),
    -- Tutorial 9: Pendientes verano
    (7, 9, 'Nunca había trabajado la resina, parece más fácil de lo que pensaba.',  '2026-05-10'),
    (3, 9, '¿Los moldes de silicona sirven para cualquier tipo de resina?',          '2026-05-12'),
    -- Tutorial 10: Cojín de invierno
    (2, 10, '¡Ya lo tengo listo para el invierno! El copo de nieve quedó genial.',  '2025-11-20'),
    (5, 10, 'Perfecto para hacer en otoño y tenerlo listo para el frío.',            '2025-12-05'),
    -- Tutorial 11: Taza cumpleaños
    (2, 11, 'Perfecto como regalo personalizado, lo haré para mi madre.',            '2026-02-01'),
    (7, 11, '¿Se puede usar en el lavavajillas después de esmaltarla?',              '2026-02-15'),
    -- Tutorial 12: Peluche infantil
    (4, 12, 'Lo hice para mi sobrina y quedó adorable, ¡gracias por el tutorial!',  '2026-03-01'),
    (8, 12, 'El anillo mágico me cuesta mucho, ¿tienes algún consejo?',              '2026-03-10'),
    -- Tutorial 13: Guirnalda de papel
    (1, 13, 'La hice para el cumpleaños de mi hija y quedó genial.',                 '2026-04-10'),
    (6, 13, 'Tan fácil que la hice con los niños en media hora.',                    '2026-04-15');


INSERT INTO TutorialesGuardados (idUsuario, idTutorial) VALUES
    (7, 6),
    (7, 10),
    (8, 9),
    (8, 13);


-- maria_perez sigue a 2 Usuarios (carlos_craft y laura_art)
INSERT INTO PerfilesSeguidos (idUsuarioSigue, idUsuarioSeguido)
SELECT (SELECT idUsuario FROM Usuario WHERE nombre = 'maria_perez'),
       (SELECT idUsuario FROM Usuario WHERE nombre = 'carlos_craft');

INSERT INTO PerfilesSeguidos (idUsuarioSigue, idUsuarioSeguido)
SELECT (SELECT idUsuario FROM Usuario WHERE nombre = 'maria_perez'),
       (SELECT idUsuario FROM Usuario WHERE nombre = 'laura_art');

-- sofia_tejidos sigue a maria_perez y ana_crochet
INSERT INTO PerfilesSeguidos (idUsuarioSigue, idUsuarioSeguido)
SELECT (SELECT idUsuario FROM Usuario WHERE nombre = 'sofia_tejidos'),
       (SELECT idUsuario FROM Usuario WHERE nombre = 'maria_perez');

INSERT INTO PerfilesSeguidos (idUsuarioSigue, idUsuarioSeguido)
SELECT (SELECT idUsuario FROM Usuario WHERE nombre = 'sofia_tejidos'),
       (SELECT idUsuario FROM Usuario WHERE nombre = 'ana_crochet');

-- marta_bordados sigue a laura_art y pablo_papel
INSERT INTO PerfilesSeguidos (idUsuarioSigue, idUsuarioSeguido)
SELECT (SELECT idUsuario FROM Usuario WHERE nombre = 'marta_bordados'),
       (SELECT idUsuario FROM Usuario WHERE nombre = 'laura_art');

INSERT INTO PerfilesSeguidos (idUsuarioSigue, idUsuarioSeguido)
SELECT (SELECT idUsuario FROM Usuario WHERE nombre = 'marta_bordados'),
       (SELECT idUsuario FROM Usuario WHERE nombre = 'pablo_papel');

-- maria_perez sigue a 2 Categorías (Costura y Cerámica)
INSERT INTO CategoriasSeguidas (idUsuario, idCategoria)
SELECT (SELECT idUsuario FROM Usuario WHERE nombre = 'maria_perez'),
       (SELECT idCategoria FROM Categoria WHERE nombreCategoria = 'Costura');

INSERT INTO CategoriasSeguidas (idUsuario, idCategoria)
SELECT (SELECT idUsuario FROM Usuario WHERE nombre = 'maria_perez'),
       (SELECT idCategoria FROM Categoria WHERE nombreCategoria = 'Cerámica');

-- sofia_tejidos sigue Crochet y Papelería
INSERT INTO CategoriasSeguidas (idUsuario, idCategoria)
SELECT (SELECT idUsuario FROM Usuario WHERE nombre = 'sofia_tejidos'),
       (SELECT idCategoria FROM Categoria WHERE nombreCategoria = 'Crochet');

INSERT INTO CategoriasSeguidas (idUsuario, idCategoria)
SELECT (SELECT idUsuario FROM Usuario WHERE nombre = 'sofia_tejidos'),
       (SELECT idCategoria FROM Categoria WHERE nombreCategoria = 'Papelería');

-- marta_bordados sigue Bisutería y Alfarería
INSERT INTO CategoriasSeguidas (idUsuario, idCategoria)
SELECT (SELECT idUsuario FROM Usuario WHERE nombre = 'marta_bordados'),
       (SELECT idCategoria FROM Categoria WHERE nombreCategoria = 'Bisutería');

INSERT INTO CategoriasSeguidas (idUsuario, idCategoria)
SELECT (SELECT idUsuario FROM Usuario WHERE nombre = 'marta_bordados'),
       (SELECT idCategoria FROM Categoria WHERE nombreCategoria = 'Alfarería');

-- maria_perez sigue 2 Temáticas (Hogar y San Valentín)
INSERT INTO TematicasSeguidas (idUsuario, idTematica)
SELECT (SELECT idUsuario FROM Usuario WHERE nombre = 'maria_perez'),
       (SELECT idTematica FROM Tematica WHERE nombreTematica = 'Hogar');

INSERT INTO TematicasSeguidas (idUsuario, idTematica)
SELECT (SELECT idUsuario FROM Usuario WHERE nombre = 'maria_perez'),
       (SELECT idTematica FROM Tematica WHERE nombreTematica = 'San Valentín');

-- sofia_tejidos sigue Primavera e Infantil
INSERT INTO TematicasSeguidas (idUsuario, idTematica)
SELECT (SELECT idUsuario FROM Usuario WHERE nombre = 'sofia_tejidos'),
       (SELECT idTematica FROM Tematica WHERE nombreTematica = 'Primavera');

INSERT INTO TematicasSeguidas (idUsuario, idTematica)
SELECT (SELECT idUsuario FROM Usuario WHERE nombre = 'sofia_tejidos'),
       (SELECT idTematica FROM Tematica WHERE nombreTematica = 'Infantil');

-- marta_bordados sigue Verano y Halloween
INSERT INTO TematicasSeguidas (idUsuario, idTematica)
SELECT (SELECT idUsuario FROM Usuario WHERE nombre = 'marta_bordados'),
       (SELECT idTematica FROM Tematica WHERE nombreTematica = 'Verano');

INSERT INTO TematicasSeguidas (idUsuario, idTematica)
SELECT (SELECT idUsuario FROM Usuario WHERE nombre = 'marta_bordados'),
       (SELECT idTematica FROM Tematica WHERE nombreTematica = 'Halloween');

SET FOREIGN_KEY_CHECKS = 1;