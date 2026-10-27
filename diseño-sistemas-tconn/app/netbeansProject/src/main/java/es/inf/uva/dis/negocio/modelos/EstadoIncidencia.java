package es.inf.uva.dis.negocio.modelos;

/**
 *
 * @author mgonzal
 * @author emirodr
 */
public enum EstadoIncidencia {
    SIN_RESOLVER("Sin resolver"),
    RESUELTA_REMOTAMENTE("Resuelta remotamente"),
    RESUELTA_IN_SITU("Resuelta in situ"),
    RESUELTA_PARCIALMENTE("Resuelta parcialmente"),
    ESCALADA_A_ESPECIALISTA("Escalada a especialista");

    private final String etiqueta;
    EstadoIncidencia(String etiqueta) { this.etiqueta = etiqueta; }

    @Override
    public String toString() { return etiqueta; }
}
