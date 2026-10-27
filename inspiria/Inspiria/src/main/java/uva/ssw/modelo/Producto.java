package uva.ssw.modelo;

import java.time.LocalDate;
import java.util.ArrayList;

public class Producto extends Publicacion {
    private double precio;
    private DisponibilidadProducto stock; 
    private byte[] imagen;
    private Integer idTutorialAsociado; // Puede ser null (Producto Aislado)
    private ArrayList<Comentario> comentarios = new ArrayList<>();

    public Producto() { super(); }

    public Producto(int id, LocalDate fecha, String titulo, String descripcion,
                    Categoria cat, Tematica tem, Usuario autor,
                    double precio, DisponibilidadProducto stock, byte[] imagen) {
        super(id, fecha, titulo, descripcion, cat, tem, autor);
        setPrecio(precio);
        setStock(stock);
        setImagen(imagen);
    }

    
    public double getPrecio() { 
        return precio; 
    }

    public void setPrecio(double precio) { 
        if (precio < 0) {
            throw new IllegalArgumentException("El precio del producto no puede ser negativo.");
        }
        this.precio = precio; 
    }

    public DisponibilidadProducto getStock() { 
        return stock; 
    }
    
    public void setStock(DisponibilidadProducto stock) { 
        if (stock == null) {
            throw new IllegalArgumentException("El stock del producto no puede ser nulo.");
        }
        this.stock = stock; 
    }

    public byte[] getImagen() { 
        return imagen; 
    }
    
    public void setImagen(byte[] imagen) { 
        if (imagen == null) {
            throw new IllegalArgumentException("La imagen del producto no puede ser nula.");
        }
        this.imagen = imagen; 
    }

    public Integer getIdTutorialAsociado() {   
        return idTutorialAsociado; 
    }

    public void setIdTutorialAsociado(Integer idTutorial) { 
        if (idTutorial == null) {
            throw new IllegalArgumentException("El ID del tutorial asociado no puede ser nulo.");
        }
        this.idTutorialAsociado = idTutorial; 
    }
    
    public ArrayList<Comentario> getComentarios() { 
        return comentarios; 
    }

    public void setComentarios(ArrayList<Comentario> comentarios) { 
        if (comentarios == null) {
            throw new IllegalArgumentException("La lista de comentarios no puede ser nula.");
        }
        this.comentarios = comentarios; 
    }

    public void agregarComentario(Comentario c) { 
        if (c == null) {
            throw new IllegalArgumentException("El comentario no puede ser nulo.");
        }
        this.comentarios.add(c); }
}