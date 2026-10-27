package uva.ssw.modelo;


public class Paso {
    
    private int numero;
    private String descripcion;
    private byte[] imagen;
    private String fotoBase64;
    
    
    public Paso() {}
    
    
    public Paso(int numero, String descripcion) {
        setNumero(numero);
        setDescripcion(descripcion);
    }
    
    
    public Paso(int numero, String descripcion, byte[] imagen) {
        setNumero(numero);
        setDescripcion(descripcion);
        setImagen(imagen);
    }

    
    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        if (numero <= 0) {
            throw new IllegalArgumentException("El número de paso debe ser un entero positivo.");
        }
        this.numero = numero;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        if (descripcion == null) throw new IllegalArgumentException("La descripción no puede ser nula.");
        this.descripcion = descripcion;
    }

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        if (imagen == null) throw new IllegalArgumentException("La imagen no puede ser nula.");
        this.imagen = imagen;
    }

    public String getFotoBase64() {
        return fotoBase64;
    }

    public void setFotoBase64(String fotoBase64) {
        this.fotoBase64 = fotoBase64;
    }

    /** @return true si este paso tiene imagen asociada. */
    public boolean tieneImagen() {
        return imagen != null && imagen.length > 0;
    }

    
    @Override
    public String toString() {
        return "Paso{" +
                "numero=" + numero +
                ", descripcion='" + descripcion + '\'' +
                ", tieneImagen=" + tieneImagen() +
                '}';
    }
}
