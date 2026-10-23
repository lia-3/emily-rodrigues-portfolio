package uva.poo.trayecto.combinado;

import java.time.LocalDate;

import uva.poo.puerto.Muelle;
import uva.poo.puerto.Puerto;
import uva.poo.trayecto.simple.Simple;


/**
 * Representa un Trayecto Combinado de Camiones y Trnees en la compania de transporte 
 * de mercacias MArkse-Co.
 * Se puede consultar el origen y destino de una trayecto, las fechas de
 * inicio y fin de trayecto. También se puede conocer que tipo de transporte
 * puede y/o va a utilizar un trayecto.
 * Permite calcular los costes de un trayectos y conocer sus descuentos.
 * 
 * @author Marta Pérez
 * @author Emily Rodrigues
 */
public class PackCamionTren extends Combinado {

	private static double PRECIO_BASE_POR_KILOMETRO_EN_TREN = 0.85;
	private double precioPorKilometroEnTren;
	
	/**
	 * Inicializa un trayecto combinado de camiones y trenes a partir de:
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
	public PackCamionTren(Muelle origenMuelle, Muelle destinoMuelle, Puerto origenPuerto, Puerto destinoPuerto,
			LocalDate fechaInicioTrayecto, LocalDate fechaFinTrayecto) {
		super(origenMuelle, destinoMuelle, origenPuerto, destinoPuerto, fechaInicioTrayecto, 
				fechaFinTrayecto);
		precioPorKilometroEnTren = PRECIO_BASE_POR_KILOMETRO_EN_TREN;
	}

	
	/**
	 * Calcula el descuento de un trayecto simple que forma parte del pack. 
	 * Si el trayecto es en tren, se aplica un descuento de manera que se cobra 
	 * 10 euros por cada kilómetro recorrido. Si el trayecto es en camión, se 
	 * cobra el coste total del trayecto sin descuento.
	 * 
	 * Este valor (10 euros por kilómetro en tren) puede ser modificado mediante 
	 * el método {@code setPrecioPorKilometroEnTren}.
	 * 
	 * @param simple el trayecto simple sobre el que se le puede aplicar el descuento. 
	 *              No puede ser {@code null}.
	 * @return el coste con descuento si el trayecto es en tren (distancia en kilómetros 
	 *         multiplicada por 10 euros), o el coste total si el trayecto es en camión.
	 * @throws IllegalArgumentException si {@code simple} es {@code null}.
	 * @throws IllegalStateException si el trayecto no forma parte del pack (usa un
	 * transporte que el pack no incluye, p.e un barco).
	 * @throws IllegalStateException si no hay trayectos en el pack.
	 */
	@Override
	public double calculaDescuento(Simple simple) {
		if (simple == null) throw new IllegalArgumentException();
		if (!hayTrayectos() || simple.usaBarco()) throw new IllegalStateException();
		
		if (simple.usaTren()) return simple.distanciaKilometros() * 10;
		else return simple.costeTotal();
	}

	
	/**
	 * Consulta si un PackCamionTren puede y/o va a usar un barco.
	 * @return false, ya que no usó, usa o usará un barco.
	 */
	@Override
	public boolean usaBarco() {
		return false;
	}

	
	/**
	 * Consulta si un PackCamionTren puede y/o va a usar un tren.
	 * @return true, ya que usó, usa o usará un tren.
	 */
	@Override
	public boolean usaTren() {
		return true;
	}

	
	/**
     * Crea un nuevo pack de camiones y trenes a modo de copia.
     * @return la copia del pack
     */
	@Override
	public PackCamionTren clone() {
		PackCamionTren copia = (PackCamionTren) super.clone();
		copia.setPrecioPorKilometroEnTren(getPrecioPorKilometroEnTren());
		return copia;
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
	 * 		- mismos descuentos por kilometro en tren
	 */
	@Override 
	public boolean equals(Object otro) {
		if (!super.equals(otro)) return false;
		PackCamionTren pack = (PackCamionTren) otro;
		return getPrecioBasePorKilometroEnTren() == pack.getPrecioBasePorKilometroEnTren();
	}
	
	
	/**
	 * Consulta el precio por kilometro que se aplica sobre los trayectos
	 * en tren del pack.
	 * @return precio por kilometro que se aplica sobre los trayectos en tren
	 * del pack.
	 */
	public double getPrecioPorKilometroEnTren() {
		return precioPorKilometroEnTren;
	}
	
	
	/**
	 * Consulta el precio base (el original) por kilometro que se aplica (o aplicaba)
	 * sobre los trayectos en tren del pack.
	 * @return precio base por kilometro sobre los trayectos en tren del pack.
	 */
	public double getPrecioBasePorKilometroEnTren() {
		return PRECIO_BASE_POR_KILOMETRO_EN_TREN; 
	}
	
	
	/**
	 * Modifica el precio por kilometro aplicado a cada trayecto en tren
	 * perteneciente al pack. El precio viene dado en euros.
	 * @param nuevoPrecio nuevo precio por kilometro en tren en euros.
	 * No puede ser menor que cero.
	 * @throws IllegalArgumentException si {@code nuevoPrecio < 0}
	 */
	public void setPrecioPorKilometroEnTren(double nuevoPrecio) {
		if (nuevoPrecio < 0)
			throw new IllegalArgumentException();
		precioPorKilometroEnTren = nuevoPrecio;
	}
}
