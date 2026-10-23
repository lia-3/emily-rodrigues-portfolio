import java.util.Arrays;

/**
 * Clase que representa un contenedor con n atributos
 * 
 * @author Grupo 31
 */
public class Contenedor {
    int indice;
    int[] atributos;

    /**
     * Constructor del Contenedor
     * @param indice Indice del contenedor
     * @param atributos Atributos del contenedor
     */
    public Contenedor(int indice, int[] atributos) {
        this.indice = indice;
        this.atributos = atributos;
    }

    /**
     * Devuelve el indice del contenedor
     * @return Indice del contenedor
     */
    public int getIndice() {
        return this.indice;
    }

    /**
     * Devuelve los atributos del contenedor
     * @return Atributos del contenedor
     */
    public int[] getAtributos() {
        return this.atributos;
    }

    /**
     * Verifica si este contenedor es compatible con otro.
     * A es compatible con B si existe una reordenación de A tal que
     * cada atributo sea estrictamente menor que el correspondiente en B.
     * @param otro El otro contenedor con el que se verifica la compatibilidad
     * @return true si son compatibles, false en caso contrario
     */
    public boolean esCompatibleCon(Contenedor otro){
        if (this.atributos.length != otro.atributos.length) {
            return false;
        }
        
        int[] atrib1 = this.atributos.clone();
        int[] atrib2 = otro.atributos.clone();
        Arrays.sort(atrib1);
        Arrays.sort(atrib2);
        
        for (int i = 0; i < atrib1.length; i++) {
            if (atrib1[i] >= atrib2[i]) {
                return false;
            }
        }
        return true;
    }
    
}
