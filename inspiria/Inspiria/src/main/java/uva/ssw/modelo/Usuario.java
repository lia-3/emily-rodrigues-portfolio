package uva.ssw.modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ArrayList;

public class Usuario {
    
    private int identificador; 
    private String nombre;
    private String correo;
    private String contrasena;
    private LocalDate fechaRegistro;
    private String descripcion;
    private boolean aceptaComisiones;
    private byte[] fotoPerfil;
    private String fotoBase64;
    
    private ArrayList<Contacto> contactos;
    private ArrayList<Comision> listaComisiones;
    private ArrayList<Tutorial> tutorialesGuardados;
    private ArrayList<Usuario> seguidos;
    private ArrayList<Categoria> categoriasSeguidas;
    private ArrayList<Tematica> tematicasSeguidas;
    private ArrayList<Tutorial> tutorialesPublicados;
    private ArrayList<Producto> productosEnVenta;

    public Usuario() {
        this.contactos           = new ArrayList<>();
        this.listaComisiones     = new ArrayList<>();
        this.tutorialesGuardados = new ArrayList<>();
        this.seguidos            = new ArrayList<>();
        this.categoriasSeguidas  = new ArrayList<>();
        this.tematicasSeguidas   = new ArrayList<>();
        this.tutorialesPublicados = new ArrayList<>();
        this.productosEnVenta    = new ArrayList<>();
        this.aceptaComisiones    = false;
        this.fechaRegistro       = LocalDate.now();
    }
    
    public Usuario(String nombre, String correo, String contrasena) {
        this();
        if (nombre == null || correo == null || contrasena == null) {
            throw new IllegalArgumentException("Los campos obligatorios no pueden ser nulos.");
        }
        setNombre(nombre);
        setCorreo(correo);
        setContrasena(contrasena);
    }
    
    public Usuario(int identificador, String nombre, String correo,
                   String contrasena, LocalDate fechaRegistro) {
        this();
        setIdentificador(identificador);
        setNombre(nombre);
        setCorreo(correo);
        setContrasena(contrasena);
        setFechaRegistro(fechaRegistro);
    }
    
    public int getIdentificador() {
        return identificador;
    }

    public void setIdentificador(int identificador) {
        if (identificador < 0) {
            throw new IllegalArgumentException("El identificador no puede ser negativo.");
        }
        this.identificador = identificador;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede ser nulo o vacío.");
        }
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        if (correo == null || correo.trim().isEmpty()) {
            throw new IllegalArgumentException("El correo no puede ser nulo o vacío.");
        }
        this.correo = correo;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        if (contrasena == null || contrasena.trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña no puede ser nula o vacía.");
        }
        this.contrasena = contrasena;
    }

    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDate fechaRegistro) {
        if (fechaRegistro == null) {
            throw new IllegalArgumentException("La fecha de registro no puede ser nula.");
        }
        this.fechaRegistro = fechaRegistro;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean isAceptaComisiones() {
        return aceptaComisiones;
    }

    public void setAceptaComisiones(boolean aceptaComisiones) {
        this.aceptaComisiones = aceptaComisiones;
    }

    public byte[] getFotoPerfil() {
        return fotoPerfil;
    }
    
    public String getFotoBase64() { 
        return fotoBase64; 
    }

    public void setFotoPerfil(byte[] fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }
    
    
    public void setFotoBase64(String fotoBase64) { 
        this.fotoBase64 = fotoBase64; 
    }
    
    public ArrayList<Contacto> getContactos() {
        return contactos;
    }

    public void setContactos(ArrayList<Contacto> contactos) {
        if (contactos == null) {
            throw new IllegalArgumentException("La lista de contactos no puede ser nula.");
        }
        this.contactos = contactos;
    }

    public void addContacto(Contacto contacto) {
        if (contacto == null) {
            throw new IllegalArgumentException("El contacto no puede ser nulo.");
        }

        this.contactos.add(contacto);
    }

    public void removeContacto(Contacto contacto) {
        if (contacto == null) {
            throw new IllegalArgumentException("El contacto no puede ser nulo.");
        }

        this.contactos.remove(contacto);
    }
    
    public ArrayList<Comision> getListaComisiones() {
        return listaComisiones;
    }

    public void setListaComisiones(ArrayList<Comision> listaComisiones) {
        if (listaComisiones == null) {
            throw new IllegalArgumentException("La lista de comisiones no puede ser nula.");
        }
        this.listaComisiones = listaComisiones;
    }

    public void addComision(Comision comision) {
        if (comision == null) {
            throw new IllegalArgumentException("La comisión no puede ser nula.");
        }
        this.listaComisiones.add(comision);
    }

    public void removeComision(Comision comision) {
        if (comision == null) {
            throw new IllegalArgumentException("La comisión no puede ser nula.");
        }
        this.listaComisiones.remove(comision);
    }
    
    public ArrayList<Tutorial> getTutorialesGuardados() {
        if (tutorialesGuardados == null) {
            tutorialesGuardados = new ArrayList<>();
        }
        return tutorialesGuardados;
    }

    public void setTutorialesGuardados(ArrayList<Tutorial> tutorialesGuardados) {
        if (tutorialesGuardados == null) {
            throw new IllegalArgumentException("La lista de tutoriales guardados no puede ser nula.");
        }
        this.tutorialesGuardados = tutorialesGuardados;
    }
    
    public void guardarTutorial(Tutorial tutorial) {
        if (tutorial == null) {
            throw new IllegalArgumentException("El tutorial no puede ser nulo.");
        }
        if (!this.tutorialesGuardados.contains(tutorial)) {
            this.tutorialesGuardados.add(tutorial);
        }
    }

    public void eliminarTutorialGuardado(Tutorial tutorial) {
        if (tutorial == null) {
            throw new IllegalArgumentException("El tutorial no puede ser nulo.");
        }
        this.tutorialesGuardados.remove(tutorial);
    }
    
    public ArrayList<Usuario> getSeguidos() {
        return seguidos;
    }

    public void setSeguidos(ArrayList<Usuario> seguidos) {
        if (seguidos == null) {
            throw new IllegalArgumentException("La lista de usuarios seguidos no puede ser nula.");
        }
        this.seguidos = seguidos;
    }
    
    public void seguir(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("El usuario no puede ser nulo.");
        }
        if (this.identificador == usuario.getIdentificador()) {
            throw new IllegalArgumentException("Un usuario no puede seguirse a sí mismo.");
        }
        if (!this.seguidos.contains(usuario)) {
            this.seguidos.add(usuario);
        }
    }
    
    public void dejarDeSeguir(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("El usuario no puede ser nulo.");
        }
        this.seguidos.remove(usuario);
    }
    
    public ArrayList<Categoria> getCategoriasSeguidas() {
        return categoriasSeguidas;
    }

    public void setCategoriasSeguidas(ArrayList<Categoria> categoriasSeguidas) {
        if (categoriasSeguidas == null) {
            throw new IllegalArgumentException("La lista de categorías seguidas no puede ser nula.");
        }
        this.categoriasSeguidas = categoriasSeguidas;
    }

    public void seguirCategoria(Categoria categoria) {
        if (categoria == null) {
            throw new IllegalArgumentException("La categoría no puede ser nula.");
        }
        if (!this.categoriasSeguidas.contains(categoria)) {
            this.categoriasSeguidas.add(categoria);
        }
    }

    public void dejarDeSeguirCategoria(Categoria categoria) {
        if (categoria == null) {
            throw new IllegalArgumentException("La categoría no puede ser nula.");
        }
        this.categoriasSeguidas.remove(categoria);
    }

    public ArrayList<Tematica> getTematicasSeguidas() {
        return tematicasSeguidas;
    }

    public void setTematicasSeguidas(ArrayList<Tematica> tematicasSeguidas) {
        if (tematicasSeguidas == null) {
            throw new IllegalArgumentException("La lista de temáticas seguidas no puede ser nula.");
        }
        this.tematicasSeguidas = tematicasSeguidas;
    }

    public void seguirTematica(Tematica tematica) {
        if (tematica == null) {
            throw new IllegalArgumentException("La temática no puede ser nula.");
        }
        if (!this.tematicasSeguidas.contains(tematica)) {
            this.tematicasSeguidas.add(tematica);
        }
    }

    public void dejarDeSeguirTematica(Tematica tematica) {
        if (tematica == null) {
            throw new IllegalArgumentException("La temática no puede ser nula.");
        }

        this.tematicasSeguidas.remove(tematica);
    }
    
    public ArrayList<Producto> getProductosEnVenta() {
        return productosEnVenta;
    }

    public void setProductosEnVenta(ArrayList<Producto> productos) {
        if (productos == null) {
            throw new IllegalArgumentException("La lista de publicaciones no puede ser nula.");
        }
        this.productosEnVenta = productos;
    }
    
    public ArrayList<Tutorial> getTutorialesPublicados() {
        return tutorialesPublicados;
    }

    public void setTutorialesPublicados(ArrayList<Tutorial> tutoriales) {
        if (tutoriales == null) {
            throw new IllegalArgumentException("La lista de publicaciones no puede ser nula.");
        }
        this.tutorialesPublicados = tutoriales;
    }

    /*public void addPublicacion(Publicacion publicacion) {
        if (publicacion == null) {
            throw new IllegalArgumentException("La publicación no puede ser nula.");
        }
        this.publicaciones.add(publicacion);
    }

    public void removePublicacion(Publicacion publicacion) {
        if (publicacion == null) {
            throw new IllegalArgumentException("La publicación no puede ser nula.");
        }
        this.publicaciones.remove(publicacion);
    }*/
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Usuario)) return false;
        Usuario usuario = (Usuario) o;
        return identificador != 0 && identificador == usuario.identificador;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(identificador);
    }
    
    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + identificador +
                ", nombre='" + nombre + '\'' +
                ", correo='" + correo + '\'' +
                ", aceptaComisiones=" + aceptaComisiones +
                ", numSeguidos=" + seguidos.size() +
                '}';
    }
}