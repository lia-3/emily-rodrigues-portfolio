package uva.ssw.modelo;

import java.util.ArrayList;


public class Categoria {
    
    private int idCategoria;
    private String nombre;
    private int numVisitas;
    private String fotoBase64;
    private ArrayList<Tutorial> listaTutoriales;
    
    public Categoria() {
        this.listaTutoriales = new ArrayList<>();
    }

    public Categoria(String nombre) {
        this();
        setNombre(nombre);
        setNumVisitas(0);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la categoría no puede ser nulo o vacío.");
        }
        this.nombre = nombre;
    }

    public int getNumVisitas() {
        return numVisitas;
    }

    public void setNumVisitas(int numVisitas) {
        if (numVisitas < 0) {
            throw new IllegalArgumentException("El número de visitas no puede ser negativo.");
        }
        this.numVisitas = numVisitas;
    }

    
    public void registrarVisita() {
        this.numVisitas++;
    }
   
    public String getFotoBase64() {
        return fotoBase64;
    }

    public void setFotoBase64(String fotoBase64) {
        this.fotoBase64 = fotoBase64;
    }
    
    
    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }
    
    public void setListaTutoriales(ArrayList<Tutorial> lista) {
        if (lista == null) throw new IllegalArgumentException();
        listaTutoriales = lista;
    }

    public ArrayList<Tutorial> getListaTutoriales() {
        return listaTutoriales;
    }

    public void agregarTutorial(Tutorial tutorial) {
        if (tutorial == null) throw new IllegalArgumentException();
        listaTutoriales.add(tutorial);
    }

    public void eliminarTutorial(Tutorial tutorial) {
        if (tutorial == null) throw new IllegalArgumentException();
        listaTutoriales.remove(tutorial);
    }
    

    @Override
    public String toString() {
        return "Categoria{" +
                "nombre='" + nombre + '\'' +
                ", numVisitas=" + numVisitas +
                '}';
    }
}
