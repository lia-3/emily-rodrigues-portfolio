# INSPIRIA

## Pasos para poner en marcha la base de datos 

De esta manera podremos crear las tablas de la base de datos, añadir las primeras categorías y temáticas de la aplicación, así como un par de usuarios, tutoriales, productos y comentarios iniciales que pueden servir para pruebas

### 1. Configurar el archivo de contexto (context.xml)
Es necesario configurar las credenciales de acceso a la base de datos:

1. Copia el contenido del archivo `context.example.xml`.
2. Crea un nuevo archivo llamado `context.xml` en el mismo directorio.
3. Pega el código copiado en el nuevo `context.xml` y modifica la contraseña con tus credenciales locales.

### 2. Cargar el script SQL inicial
Ejecuta el script de creación de tablas e inserción de datos que se encuentra en la siguiente ruta:

    * Ruta en disco (ventana files de NetBeans): src/main/resources/sql/scripts
    * Desde el IDE (NetBeans): Expande Other Sources ➔ src/main/resources/ ➔ paquete sql.scripts.

### 3. Configurar y ejecutar la inserción de imágenes
Para cargar las imágenes iniciales en la base de datos, debes actualizar y ejecutar un componente Java:

1. Abre el archivo InsercionImagenes.java. Lo encontrarás en:
    * Ruta en disco: src/main/java/uva/ssw/database
    * Desde el IDE: Expande Source Packages ➔ paquete uva.ssw.database.
2. Cambia la contraseña dentro del código de este archivo para que coincida con la de tu base de datos.
3. Ejecuta el archivo InsercionImagenes.java como una aplicación Java independiente.