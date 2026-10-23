package uva.poo.trayecto.simple;

import uva.poo.trayecto.Trayecto;
import uva.poo.puerto.Muelle;
import uva.poo.puerto.Puerto;
import java.time.LocalDate;

/**
 * Representa un Trayecto Simple en la compania de transporte de mercacias MArkse-Co.
 * Se puede consultar el origen y destino de una trayecto, las fechas de
 * inicio y fin de trayecto. También se puede conocer que tipo de transporte
 * puede y/o va a utilizar un trayecto, el coste por dia, por trayecto y por kilometros.
 * Permite calcular los costes de un trayectos.
 * 
 * @author Marta Pérez
 * @author Emily Rodrigues
 */
public abstract class Simple extends Trayecto {
	
	private double costePorDia;
	private double costeFijo;
	private double costePorKilometro;

	
	/**
	 * Inicializa un trayecto simple a partir de:
	 * - Un origen (Puerto y Muelle)
	 * - Un destino (Puerto y Muelle)
	 * - Una fecha de inicio y fin de trayecto
	 * 
	 * @param origenMuelle muelle de origen del trayecto. No puede ser null. El muelle
	 * de origen debe encontrarse en el puerto de origen.
	 * @param destinoMuelle muelle de destino del trayecto. No puede ser null. El muelle 
	 * debe encontrarse en el puerto de destino.
	 * @param origenPuerto puerto de origen del trayecto. No puede ser null.
	 * @param destinoPuerto puerto de destino del trayecto. No puede ser null y tampoco 
	 * puede ser igual a puerto de origen.
	 * @param fechaInicioTrayecto dia en el que se inicio el trayecto. No puede ser null.
	 * @param fechaFinTrayecto dia en el que se finalizo el trayecto. No puede ser null
	 * ni anterior a la fecha de inicio del trayecto.
	 * 
	 * @throws IllegalArgumentException si origenMuelle, destinoMuelle, origenPuerto, 
	 * destinoPuerto, fechaInicioTrayecto o fechaFinTrayecto son null.
	 * @throws IllegalArgumentException si origenMuelle no se encuentra en
	 * origenPuerto.
	 * @throws IllegalArgumentException si muelle destinoMuelle no se encuentra en 
	 * destinoPuerto.
	 * @throws IllegalArgumentException si origenPuerto y destinoPuerto son los mismos.
	 * @throws IllegalArgumentException si fechaInicioTrayecto es posterior a fechaFinTrayecto.
	 */
    protected Simple(Muelle origenMuelle, Muelle destinoMuelle, Puerto origenPuerto, 
                  Puerto destinoPuerto, LocalDate fechaInicioTrayecto, LocalDate fechaFinTrayecto) {
        super(origenMuelle, destinoMuelle, origenPuerto, destinoPuerto, fechaInicioTrayecto, fechaFinTrayecto);
    }

    /**
     * Calcula el coste de un trayecto simple a partir de su coste por dia,
	 * coste fijo y coste por kilometro.
     * @return coste total del trayecto.
     */
    @Override
    public double costeTotal() {
        int dias = getFechaInicio().until(getFechaFin()).getDays();
        return (costePorDia() * dias) + costeFijo() + (costePorKilometro() * distanciaKilometros());

    }    
    
    
    /**
     * Crea un nuevo Trayecto Simple a modo de copia.
     * @return la copia del trayecto
     */
    @Override
    public Simple clone() {
    	return (Simple) super.clone();
    }
    
    
    /**
     * Consulta el coste por dia de un trayecto simple.
     * @return coste por dia, no el coste total por todos los dias
     * de trayecto.
     */
    public double costePorDia() {
    	return costePorDia;
    }

    
    /**
     * Consulta el coste fijo de un trayecto simple en euros.
     * @return el coste fijo del trayecto.
     */
    public double costeFijo() {
    	return costeFijo;
    }
    
    
    
    /**
     * Consulta el coste por kilometro de un trayecto simple.
     * @return coste por (un) kilometro, no el coste total por kilometros
     * recorridos en el trayecto completo.
     */
    public double costePorKilometro() {
    	return costePorKilometro;
    }
    
    
    /**
     * Modifica el coste por (un) dia de un trayecto simple en euros.
     * @param nuevoCostePorDia nuevo coste. No puede ser menor que
     * cero.
     * @throws IllegalArgumentException si {@code nuevoCosteDia < 0}
     */
    public void setCostePorDia(double nuevoCostePorDia) {
    	if (nuevoCostePorDia < 0) throw new IllegalArgumentException();
    	costePorDia = nuevoCostePorDia;
    }
    
    
    /**
     * Modifica el coste por (un) kilometro de un trayecto simple en euros.
     * @param nuevoCostePorKilometro nuevo coste. No puede ser menor que
     * cero.
     * @throws IllegalArgumentException si {@code nuevoCostePorKilometro < 0}
     */
    public void setCostePorKilometro(double nuevoCostePorKilometro) {
    	if (nuevoCostePorKilometro < 0) throw new IllegalArgumentException();
    	costePorKilometro = nuevoCostePorKilometro;
    }
    
    
    /**
     * Modifica el coste fijo de un trayecto simple en euros.
     * @param nuevoCosteFijo nuevo coste. No puede ser menor que
     * cero.
     * @throws IllegalArgumentException si {@code nuevoCosteDia < 0}
     */
    public void setCosteFijo(double nuevoCosteFijo) {
    	if (nuevoCosteFijo < 0) throw new IllegalArgumentException();
    	costeFijo = nuevoCosteFijo;
    }
    
    
    /**
     * Consulta el coste por dia inicial de un trayecto simple, antes
     * de los cambios en el mercado.
     * @return coste por dia base, antes de los cambios.
     */
    public abstract double costePorDiaBase();
    
    
    /**
     * Consulta el coste por kilometro inicial de un trayecto simple, antes
     * de los cambios en el mercado.
     * @return coste por kilometro base, antes de los cambios.
     */
    public abstract double costePorKilometroBase();
    
    
    /**
     * Consulta el coste fijo inicial de un trayecto simple, antes
     * de los cambios en el mercado.
     * @return coste fijo base, antes de los cambios.
     */
    public abstract double costeFijoBase();
}
