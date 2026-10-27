package uva.ssw.modelo;

import java.time.LocalDate;


public class Comentario {
    
    private String identificador;
    private String texto;
    private LocalDate fechaCreacion;
    private Usuario autor;

    public Comentario() {}

    public Comentario(String identificador, String texto, LocalDate fechaCreacion, Usuario autor) {
        setAutor(autor);
        setFechaCreacion(fechaCreacion);
        setIdentificador(identificador);   
        setTexto(texto);
    }
    

    public String getIdentificador() {
        return identificador;
    }
    

    public void setIdentificador(String identificador) {
        if (identificador == null || identificador.trim().isEmpty()) {
            throw new IllegalArgumentException("El identificador no puede ser nulo o vacío.");
        }
        this.identificador = identificador;
    }
    

    public String getTexto() {
        return texto;
    }

    
    public void setTexto(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            throw new IllegalArgumentException("El texto del comentario no puede ser nulo o vacío.");
        }
        this.texto = texto;
    }

    
    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    
    public void setFechaCreacion(LocalDate fechaCreacion) {
        if (fechaCreacion == null) {
            throw new IllegalArgumentException("La fecha de creación no puede ser nula.");
        }
        this.fechaCreacion = fechaCreacion;
    }

    
    public Usuario getAutor() {
        return autor;
    }

    
    public void setAutor(Usuario autor) {
        if (autor == null) {
            throw new IllegalArgumentException("El autor del comentario no puede ser nulo.");
        }
        this.autor = autor;
    }
    

    @Override
    public String toString() {
        return "Comentario{" +
                "identificador='" + identificador + '\'' +
                ", autor=" + (autor != null ? autor.getNombre() : "null") +
                ", fechaCreacion=" + fechaCreacion +
                ", texto='" + texto + '\'' +
                '}';
    }
}
