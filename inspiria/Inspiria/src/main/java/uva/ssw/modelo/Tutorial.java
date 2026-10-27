package uva.ssw.modelo;

import java.time.LocalDate;
import java.util.ArrayList;

public class Tutorial extends Publicacion {
    private DificultadTutorial dificultad;
    private double costeEstimado;
    private int duracion;    
    
    private ArrayList<Herramienta> herramientasLista = new ArrayList<>();
    private ArrayList<Material> materialesLista = new ArrayList<>();
    private ArrayList<Paso> pasos = new ArrayList<>();
    private ArrayList<Comentario> comentarios = new ArrayList<>();
    private Producto productoAsociado;

    public Tutorial() { super(); }

    public Tutorial(int id, LocalDate fecha, String titulo, String descripcion,
                    Categoria cat, Tematica tem, Usuario autor,
                    DificultadTutorial dificultad, double coste, int duracion) {
        super(id, fecha, titulo, descripcion, cat, tem, autor);
        this.dificultad = dificultad;
        this.costeEstimado = coste;
        this.duracion = duracion;
    }
    

    public int getDuracion() { 
        return duracion; 
    }

    public void setDuracion(int duracion) { 
        if (duracion < 0) {
            throw new IllegalArgumentException("La duración del tutorial no puede ser negativa.");
        }
        this.duracion = duracion; 
    }

    public DificultadTutorial getDificultad() { 
        return dificultad; 
    }

    public void setDificultad(DificultadTutorial dif) { 
        if (dif == null) {
            throw new IllegalArgumentException("La dificultad del tutorial no puede ser nula.");
        }
        this.dificultad = dif; 
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
    

    public double getCosteEstimado() { 
        return costeEstimado; 
    }

    public void setCosteEstimado(double coste) { 
        if (coste < 0) {
            throw new IllegalArgumentException("El coste estimado del tutorial no puede ser negativo.");
        }
        this.costeEstimado = coste; 
    }
    

    public ArrayList<Paso> getPasos() { 
        return pasos; 
    }
    
    public void setPasos(ArrayList<Paso> pasos) { 
        if (pasos == null) {
            throw new IllegalArgumentException("La lista de pasos no puede ser nula.");
        }
        this.pasos = pasos; 
    }
    
    public void agregarPaso(Paso paso) { 
        if (paso == null) {
            throw new IllegalArgumentException("El paso no puede ser nulo.");
        }
        this.pasos.add(paso); 
    }
    
    public ArrayList<Herramienta> getHerramientas() { 
        return herramientasLista;
    }

    public void setHerramientas(ArrayList<Herramienta> herramienta) { 
        if (herramienta == null) {
            throw new IllegalArgumentException("La lista de herramientas no puede ser nula.");
        }
        herramientasLista = herramienta; 
    }

    public void agregarHerramienta(Herramienta herramientas) { 
        if (herramientas == null) {
            throw new IllegalArgumentException("La herramienta no puede ser nula.");
        }
        herramientasLista.add(herramientas); 
    }

    public ArrayList<Material> getMateriales() { 
        return materialesLista; 
    }

    public void setMateriales(ArrayList<Material> materiales) { 
        if (materiales == null) {
            throw new IllegalArgumentException("La lista de materiales no puede ser nula.");
        }
        materialesLista = materiales; 
    }

    public void agregarMaterial(Material material) { 
        if (material == null) {
            throw new IllegalArgumentException("El material no puede ser nulo.");
        }
        materialesLista.add(material); 
    }

    public Producto getProductoAsociado() { 
        return productoAsociado; 
    }
    
    public void setProductoAsociado(Producto producto) { 
        if (producto == null) {
            throw new IllegalArgumentException("El producto asociado no puede ser nulo.");
        }
        productoAsociado = producto; 
    }
}