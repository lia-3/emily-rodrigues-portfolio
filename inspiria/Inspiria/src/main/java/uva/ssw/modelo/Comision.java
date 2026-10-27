package uva.ssw.modelo;


public class Comision {
    
    private String identificador;
    private double precioBase;
    private String descripcion;
    

    public Comision() {}

    
    public Comision(String identificador, double precioBase, String descripcion) {
        setIdentificador(identificador);
        setPrecioBase(precioBase);
        setDescripcion(descripcion);
    }
    

    public  void setIdentificador(String identificador) {
        if (identificador == null || identificador.trim().isEmpty()) {
            throw new IllegalArgumentException("El identificador no puede ser nulo o vacío.");
        }
        this.identificador = identificador;
    }


    public String getIdentificador() {
        return identificador;
    }


    public double getPrecioBase() {
        return precioBase;
    }

    
    public void setPrecioBase(double precioBase) {
        if (precioBase < 0) {
            throw new IllegalArgumentException("El precio base no puede ser negativo.");
        }
        this.precioBase = precioBase;
    }
    

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        if (descripcion == null) throw new IllegalArgumentException();
        this.descripcion = descripcion;
    }
    

    @Override
    public String toString() {
        return "Comision{" +
                "descripcion='" + descripcion + '\'' +
                ", precioBase=" + precioBase +
                '}';
    }
}
