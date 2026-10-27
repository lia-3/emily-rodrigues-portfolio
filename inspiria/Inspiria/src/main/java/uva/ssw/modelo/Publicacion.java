package uva.ssw.modelo;

import java.time.LocalDate;

public abstract class Publicacion {
    private int id; 
    private LocalDate fechaPublicacion;
    private String titulo;
    private String descripcion;
    private Categoria categoria;
    private Tematica tematica;
    private Usuario autor;
    private String fotoBase64;

    protected Publicacion() {}

    protected Publicacion(int id, LocalDate fechaPublicacion, String titulo, 
                          String descripcion, Categoria categoria, 
                          Tematica tematica, Usuario autor) {
        setId(id);
        setFechaPublicacion(fechaPublicacion);
        setTitulo(titulo);
        setDescripcion(descripcion);
        setCategoria(categoria);
        setTematica(tematica);
        setAutor(autor);
    }

    public int getId() {
        return id; 
    }
    
    public void setId(int id) { 
        this.id = id; 
    }

    public LocalDate getFechaPublicacion() { 
        return fechaPublicacion; 
    }

    public void setFechaPublicacion(LocalDate fecha) { 
        if (fecha == null) {
            throw new IllegalArgumentException("La fecha de publicación no puede ser nula.");
        }
        if (fecha.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de publicación no puede ser futura.");
        }
        this.fechaPublicacion = fecha; 
    }

    public String getTitulo() { 
        return titulo; 
    }

    public void setTitulo(String titulo) { 
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("El título de la publicación no puede ser nulo o vacío.");
        }
        this.titulo = titulo; 
    }

    public String getDescripcion() { 
        return descripcion; 

    }

    public void setDescripcion(String desc) { 
        if (desc == null || desc.trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción de la publicación no puede ser nula o vacía.");
        }
        this.descripcion = desc; 
    }

    public Categoria getCategoria() { 
        return categoria; 
    }

    public void setCategoria(Categoria cat) { 
        if (cat == null) {
            throw new IllegalArgumentException("La categoría de la publicación no puede ser nula.");
        }
        this.categoria = cat; 
    }

    public Tematica getTematica() { 
        return tematica; 
    }

    public void setTematica(Tematica tem) { 
        if (tem == null) {
            throw new IllegalArgumentException("La temática de la publicación no puede ser nula.");
        }
        this.tematica = tem; 
    }

    public Usuario getAutor() {
        return autor; 
    }

    public void setAutor(Usuario autor) { 
        if (autor == null) {
            throw new IllegalArgumentException("El autor de la publicación no puede ser nulo.");
        }
        this.autor = autor; 
    }
    
    public String getFotoBase64() {
        return fotoBase64;
    }

    public void setFotoBase64(String fotoBase64) {
        this.fotoBase64 = fotoBase64;
    }
}