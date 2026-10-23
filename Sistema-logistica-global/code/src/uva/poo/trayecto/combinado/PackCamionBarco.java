package uva.poo.trayecto.combinado;

import java.time.LocalDate;

import uva.poo.puerto.Muelle;
import uva.poo.puerto.Puerto;
import uva.poo.trayecto.simple.Simple;


/**
 * Representa un Trayecto Combinado de Camiones y Barcos en la compania de transporte 
 * de mercacias MArkse-Co.
 * Se puede consultar el origen y destino de una trayecto, las fechas de
 * inicio y fin de trayecto. También se puede conocer que tipo de transporte
 * puede y/o va a utilizar un trayecto.
 * Permite calcular los costes de un trayectos y conocer sus descuentos.
 * 
 * @author Marta Pérez
 * @author Emily Rodrigues
 */
public class PackCamionBarco extends Combinado{

	private static final double DESCUENTO_BASE_BARCO = 0.85;
	private double descuentoBarco;
	
	/**
	 * Inicializa un trayecto combinado de camiones y barcos a partir de:
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
	 */
	public PackCamionBarco(Muelle origenMuelle, Muelle destinoMuelle, Puerto origenPuerto, Puerto destinoPuerto,
			LocalDate fechaInicioTrayecto, LocalDate fechaFinTrayecto) {
		super(origenMuelle, destinoMuelle, origenPuerto, destinoPuerto, fechaInicioTrayecto, fechaFinTrayecto);
		descuentoBarco = DESCUENTO_BASE_BARCO;
	}

	
	/**
	 * Calcula el descuento de un trayecto simple que forma parte
	 * del pack. De tal manera de que si es un tayecto en barco, 
	 * se aplica un descuento del 15%, si no será el coste total del trayecto.
	 * @param simple el trayecto simple sobre el que se le puede aplicar descuento.
	 * Si no tiene descuento aplicable, se devuelve el coste total y original del 
	 * trayecto
	 * @return si simple es un TBarco, devuelve el coste del TBarco aplicando el descuento
	 * del 15%, si no es un TBarco, devuelve el coste del trayecto.
	 */
	@Override
	public double calculaDescuento(Simple simple) {
		if (simple == null) throw new IllegalArgumentException();
		if (simple.usaTren()) throw new IllegalStateException();
		
		double costeTotalTrayecto = simple.costeTotal();
		if (simple.usaBarco()) {
			return costeTotalTrayecto * 0.85;
		} else {
			return costeTotalTrayecto;
		}
	}

	
	/**
	 * Consulta si un PackCamionBarco puede y/o va a usar un barco.
	 * @return true, ya que usó, usa o usará un barco.
	 */
	@Override
	public boolean usaBarco() {
		return true;
	}

	
	/**
	 * Consulta si un PackCamionBarco puede y/o va a usar un tren.
	 * @return false, ya que no usó, usa o usará un tren.
	 */
	@Override
	public boolean usaTren() {
		return false;
	}
	
	
	/**
     * Crea un nuevo pack de camiones y barcos a modo de copia.
     * @return la copia del pack
     */
	@Override
	public PackCamionBarco clone() {
		PackCamionBarco copia = (PackCamionBarco) super.clone();
		copia.setDescuentoBarco((int) getDescuentoPorBarco());
		return copia;
	}
	
	
	/**
	 * Consulta el descuento original aplicado sobre 
	 * el coste total de un barco.
	 * @return el descuento original
	 */
	public double getDescuentoBasePorBarco() {
		return (1 - DESCUENTO_BASE_BARCO) * 100;
	}

	
	/**
	 * Consulta el descuento sobre el coste total del trayecto en
	 * barco que se encuentra activo en este momento.
	 * @return devuelve un valor entre [0..100]
	 */
	public double getDescuentoPorBarco() {
		return (1 - descuentoBarco) * 100;
	}
	
	
	/**
	 * Modifica el descuento aplicable sobre el coste total de
	 * un trayecto en barco.
	 * @param porcentajeDescuento nuevo porcentaje de descuento,
	 * no puede ser menor que cero ni mayor que cien.
	 * @throws IllegalArgumentException si 
	 * {@code porcentajeDescuento < 0 || porcentajeDescuento > 100}
	 */
	public void setDescuentoBarco(int porcentajeDescuento) {
		if ((porcentajeDescuento < 0) || (porcentajeDescuento > 100)) 
			throw new IllegalArgumentException();
		descuentoBarco = 1 - (porcentajeDescuento / 100.0); 
	}
	
	
	/**
	 * Consulta si un trayecto es igual a otro. 
	 * Se consideran dos trayectos iguales aquellos con mismo muelle y puerto de origen,
	 * mismo muelle y puerto destino, mismas fechas de inicio y fin de trayecto, y si 
	 * todos los trayectos simples que contienen ambos trayectos combinados son iguales.
	 * @param otro trayecto combinado que se quiere conocer si es igual a {@code this}.
	 * @return true en caso de tener:
	 * 		- mismo muelle y puerto de origen
	 * 		- mismo muelle y puerto destino
	 * 		- mismas fechas de inicio y fin de trayecto
	 * 		- mismos trayectos simples
	 * 		- mismos descuentos por barco
	 */
	@Override 
	public boolean equals(Object otro) {
		if (!super.equals(otro)) return false;
		PackCamionBarco pack = (PackCamionBarco) otro;
		return getDescuentoPorBarco() == pack.getDescuentoPorBarco();
	}
}
