# INSPIRIA - Plataforma Social de Tutoriales de Artesanía

## Contexto del Proyecto
Este proyecto ha sido desarrollado como parte de la asignatura de **Servicios y Sistemas Web**

El objetivo principal ha sido el análisis, especificación, diseño y prototipado de una aplicación web completa denominada **INSPIRIA**, una red social colaborativa centrada en el mundo de la artesanía y el movimiento "Hazlo tú mismo" (DIY). La plataforma conecta a artesanos, creadores de contenido y entusiastas, ofreciendo un espacio para compartir tutoriales detallados, vender productos artesanales y gestionar encargos personalizados (comisiones).

- **Autores:**
    * Mielgo Martín, Álvaro
    * Pérez Alonso, Marta
    * Ramos Prados, Iria
    * Rodrigues Álvarez, Emily Fabiana

---

## Descripción del Sistema
INSPIRIA surge para resolver dos problemas principales detectados en redes sociales genéricas: la superficialidad de los tutoriales (vídeos rápidos y poco detallados) y la dificultad de comercialización de productos artesanales. La plataforma modela una arquitectura robusta que abarca los siguientes flujos operativos:

1. **Gestión de Usuarios y Comunidad:** Registro, inicio de sesión, perfiles públicos y privados, configuración de cuenta (foto, nombre, contraseña), y un sistema social de seguimiento (follow) a otros usuarios, categorías y temáticas.
2. **Publicación de Contenidos (Tutoriales):** Creación de tutoriales tipo blog con campos obligatorios (categoría, temática, dificultad, duración, materiales, herramientas y un mínimo de 5 pasos con explicación textual y al menos una imagen). Incluye la opción de asociar la venta del producto final.
3. **Comercio y Comisiones:** Publicación de productos artesanales en venta (con o sin tutorial asociado), gestión de stock, y un sistema de contacto externo (correo, Telegram, etc.) para gestionar pedidos y comisiones personalizadas. Los artesanos pueden activar la opción "Abierto a comisiones" y listar precios orientativos.
4. **Exploración y Feed:** Página principal con tendencias (categorías y temáticas más visitadas), buscador, sección de guardados, y un muro personalizado (feed) con las publicaciones de los perfiles seguidos.

---

## Estructura del Repositorio
El proyecto se encuentra estructurado de forma modular, reflejando las diferentes fases de análisis, diseño y desarrollo llevadas a cabo:

- `analisis_grupo31.pdf`: Documento de análisis de la aplicación y páginas web estáticas. Incluye la especificación de requisitos (funcionales, de información, reglas de negocio y no funcionales), el modelo de dominio, el mapa del sitio web y la elaboración de las páginas estáticas principales.
- `prototipo_grupo31.pdf`: Memoria del prototipo funcional. Detalla la estructura de la base de datos (modelo relacional, decisiones técnicas, diagrama E-R), la implementación de los requisitos funcionales seleccionados (RF-02, RF-03, RF-04, RF-11, RF-12, RF-17), las modificaciones sobre el modelo de dominio y el porcentaje de trabajo individual.
- `propuesta_grupo31.pdf`: Documento inicial de propuesta del proyecto. Contiene la descripción del problema, los usuarios objetivo y un borrador de la solución con bocetos de las principales pantallas (página de inicio, principal, publicar, tendencias, perfil, etc.).
- `inspiria_reorientado.pdf`: Documento descriptivo de la plataforma desde una perspectiva funcional y de negocio. Explica los problemas que resuelve, los tipos de usuarios, las funciones principales y la especificación detallada de cada parte del sitio web.
- `src/` (o carpeta de código): Espacio destinado a la implementación final de la aplicación web, que incluirá los Servlets, JSP, DAOs y scripts SQL para la base de datos MariaDB, según lo especificado en el prototipo.

---

## Tecnologías y Conceptos Aplicados
- **Ingeniería del Software:** Análisis de requisitos, modelado conceptual (diagramas de clases, entidad-relación), diseño de mapas de sitio y especificación de reglas de negocio.
- **Desarrollo Web:** Java, JSP (JavaServer Pages), Servlets, HTML5, CSS3, y gestión de sesiones (HttpSession).
- **Bases de Datos:** MariaDB (MySQL), diseño relacional normalizado, uso de `LONGBLOB` para imágenes, restricciones `ENUM`, `CHECK`, `ON DELETE CASCADE` y `Connection Pool` en Tomcat.
- **Herramientas:** Apache Tomcat, NetBeans, Visual Paradigm (para diagramas UML), Git / GitLab (control de versiones).
- **Metodologías:** Prototipado incremental y desarrollo guiado por requisitos.

---

## Declaración de Uso de Inteligencia Artificial
Si bien se ha hecho uso de inteligencia artificial generativa (IA-Gen), cada consulta y respuesta generada fue evaluada antes de incluirla en este proyecto.

- **Gemini:** Utilizado para la resolución de errores de integración entre Servlets y DAOs, la redacción y estructuración de la documentación, y la generación de datos de prueba para el script SQL.
- **Claude:** Utilizado como apoyo informativo durante la elaboración de requisitos (métricas verificables para requisitos no funcionales) y para consultas generales sobre herramientas de diagramación y calidad de redacción.

---

**Nota:** Este repositorio contiene la documentación completa del análisis y el prototipo, así como el código fuente de la aplicación final desarrollada como parte de la asignatura.