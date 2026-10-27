package uva.ssw.modelo;

public class Material {
    
    private String identificador;
    private String nombre;
    private double coste;
    private String url;    
    

    public Material() {
    }

    public Material(String identificador, String nombre, double coste, String url) {
        setIdentificador(identificador);
        setNombre(nombre);
        setUrl(url);
        setCoste(coste);
    }    

    public void setIdentificador(String identificador) {
        if (identificador == null) throw new IllegalArgumentException("El identificador no puede ser nulo.");
        this.identificador = identificador;
    }

    public String getIdentificador() {
        return identificador;
    }   

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre ==  null) throw new IllegalArgumentException();
        this.nombre = nombre;
    }

    public double getCoste() {
        return coste;
    }

    public void setCoste(double coste) {
        if (coste < 0) {
            throw new IllegalArgumentException("El coste de la Material no puede ser negativo.");
        }
        this.coste = coste;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        if (url == null) throw new IllegalArgumentException();
        this.url = url;
    }    

    @Override
    public String toString() {
        return "Material{" +
                "nombre='" + nombre + '\'' +
                ", coste=" + coste +
                ", url=" + url +
                '}';
    }
}
