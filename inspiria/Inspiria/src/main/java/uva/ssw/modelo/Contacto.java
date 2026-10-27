package uva.ssw.modelo;


public class Contacto {
    
    private String link;
    private String plataforma;
    

    public Contacto() {}

    
    public Contacto(String link, String plataforma) {
        setLink(link);  
        setPlataforma(plataforma);
    }
    
    
    public String getLink() {
        return link;
    }

    
    public void setLink(String link) {
        if (link == null || link.trim().isEmpty()) {
            throw new IllegalArgumentException("El link no puede ser nulo o vacío.");
        }
        this.link = link;
    }

    
    public String getPlataforma() {
        return plataforma;
    }

    
    public void setPlataforma(String plataforma) {
        if (plataforma == null || plataforma.trim().isEmpty()) {
            throw new IllegalArgumentException("La plataforma no puede ser nula o vacía.");
        }
        this.plataforma = plataforma;
    }
    
    
    @Override
    public String toString() {
        return "Contacto{" +
                "plataforma='" + plataforma + '\'' +
                ", link='" + link + '\'' +
                '}';
    }
}
