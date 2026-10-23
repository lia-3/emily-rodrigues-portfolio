package uva.tds.base;



/**
 * Identifica los distintos estados en los que se puede encontrar una bicicleta en el
 * sistema, bien sea eléctrica o normal
 * Los valores posibles son:
 * - Disponible: cualquier usuario puede usarla, está libre
 * - Ocupada: actualmente, otro usuario está usando la bicicleta
 * - Reservada: un usuario ha reservada la bicicleta
 * - Bloqueada: no se puede utilizar en este momento
 * @author Emily Rodrigues
 */
public enum EstadoBicicleta {
    DISPONIBLE, 
    OCUPADA, 
    BLOQUEADA,
    RESERVADA
}
