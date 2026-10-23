package uva.poo.trayecto;

import java.time.LocalDate;
import es.uva.inf.poo.maps.GPSCoordinate;
import uva.poo.puerto.Muelle;
import uva.poo.puerto.Puerto;


/**
 * Representa un Trayecto en la compania de transporte de mercacias MArkse-Co.
 * Se puede consultar el origen y destino de una trayecto, las fechas de
 * inicio y fin de trayecto. También se puede conocer que tipo de transporte
 * puede y/o va a utilizar un trayecto.
 * Permite calcular los costes de un trayectos.
 * 
 * @author Marta Pérez
 * @author Emily Rodrigues
 */
public abstract class Trayecto implements Cloneable{
	
	private Muelle origenMuelle;
	private Muelle destinoMuelle;
	private Puerto origenPuerto;
	private Puerto destinoPuerto;
	private LocalDate fechaInicioTrayecto;
	private LocalDate fechaFinTrayecto;
	
	
	/**
	 * Inicializa un trayecto a partir de:
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
	protected Trayecto(Muelle origenMuelle, Muelle destinoMuelle, Puerto origenPuerto,
					Puerto destinoPuerto, LocalDate fechaInicioTrayecto, LocalDate fechaFinTrayecto) {
		setFechaInicioTrayecto(fechaInicioTrayecto);
		setFechaFinTrayecto(fechaFinTrayecto);
		setPuertoOrigen(origenPuerto);
		setMuelleOrigen(origenMuelle);
		setPuertoDestino(destinoPuerto);
		setMuelleDestino(destinoMuelle);
	}
	
	
	/**
	 * Consulta el muelle de origen del trayecto.
	 * @return muelle de origen del trayecto.
	 */
	public Muelle getMuelleOrigen() {
		return origenMuelle.clone();
	}
	
	
	/**
	 * Consulta el muellde de destino del trayecto.
	 * @return muelle de destino del trayecto.
	 */
	public Muelle getMuelleDestino() {
		return destinoMuelle.clone();
	}
	
	
	/**
	 * Consulta el puerto de origen del trayecto.
	 * @return puerto de origen del trayecto.
	 */
	public Puerto getPuertoOrigen() {
		return origenPuerto.clone();
	}
	
	
	/**
	 * Consulta el puerto de destino del trayecto.
	 * @return puerto destino del trayecto.
	 */
	public Puerto getPuertoDestino() {
		return destinoPuerto.clone();
	}
	
	
	/**
	 * Consulta la fecha de inicio del trayecto.
	 * @return fecha de incio del trayecto.
	 */
	public LocalDate getFechaInicio() {
		return devuelveCopiaFecha(fechaInicioTrayecto);
	}
	
	
	/**
	 * Consulta la fecha de fin del trayecto.
	 * @return fecha de fin del trayecto.
	 */
	public LocalDate getFechaFin() {
		return devuelveCopiaFecha(fechaFinTrayecto);
	}
	
	
	/**
	 * Consulta si la fecha de fin es posterior a otra dada.
	 * @param fechaComparar fecha dada para comparar con la fecha
	 * de fin de trayecto.
	 * @return true si la fecha de fin de trayecto es posterior a
	 * fechaComparar, false en caso contrario.
	 * @throws IllegalArgumentException si {@code fechaComparar == null}
	 */
	public boolean isFechaFinPosterior(LocalDate fechaComparar) {
		if (fechaComparar == null) throw new IllegalArgumentException();
		return fechaFinTrayecto.isAfter(fechaComparar);
	}
	
	
	/**
	 * Calcula la distancia en millas marinas internacionales de un trayecto.
	 * @return distancia en millas marinas internacionales.
	 */
	public double distanciaMillasMarinas() {
	    return distanciaKilometros() * 0.539957;
	}
	
	
	/**
	 * Calcula la distacia en kilometros de un trayecto.
	 * @return distancia en kilometros.
	 */
	public double distanciaKilometros() {
	    GPSCoordinate coordenadaInicio = origenMuelle.getPuntoGPS();
	    GPSCoordinate coordenadaFin = destinoMuelle.getPuntoGPS();
	    return coordenadaInicio.getDistanceTo(coordenadaFin);
	}

	
	/**
	 * Consulta si un trayecto es igual a otro. 
	 * Se consideran dos trayectos iguales aquellos con mismo muelle y puerto de origen,
	 * mismo muelle y puerto destino, y mismas fechas de inicio y fin de trayecto
	 * @param otro trayecto que se quiere conocer si es igual a {@code this}
	 * @return true en caso de tener:
	 * 		- mismo muelle y puerto de origen
	 * 		- mismo muelle y puerto destino
	 * 		- mismas fechas de inicio y fin de trayecto
	 * 		- tienen el mismo uso de los trayectos, es decir:
	 * 			- ambos usan (o no) barcos.
	 * 			- ambos usan (o no) trenes.
	 */
	@Override
	public boolean equals(Object otro) {
		if (otro == null) return false;
		if (otro == this) return true;
		Trayecto trayecto = (Trayecto) otro;
		boolean sonIgualesMuelleOrigen = origenMuelle.equals(trayecto.getMuelleOrigen());
		boolean sonIgualesMuelleDestino = destinoMuelle.equals(trayecto.getMuelleDestino());
		boolean sonIgualesPuertoOrigen = origenPuerto.equals(trayecto.getPuertoOrigen());
		boolean sonIgualesPuertoDestino = destinoPuerto.equals(trayecto.getPuertoDestino());
		boolean sonFechasInicioIguales = fechaInicioTrayecto.equals(trayecto.getFechaInicio());
		boolean sonFechasFinIguales = fechaFinTrayecto.equals(trayecto.getFechaFin());
		boolean sonIgualesUsoBarco = trayecto.usaBarco() == usaBarco();
		boolean sonIgualesUsoTren = trayecto.usaTren() == usaTren();
		
		return (sonIgualesMuelleOrigen && sonIgualesMuelleDestino 
				&& sonIgualesPuertoOrigen && sonIgualesPuertoDestino
				&& sonFechasInicioIguales && sonFechasFinIguales
				&& sonIgualesUsoBarco && sonIgualesUsoTren);
	}
	

	/**
	 * Consulta el coste total de un trayecto, aplicando descuentos
	 * y el modelo negocio asociado al valor final.
	 * @return coste total del trayecto.
	 */
	public abstract double costeTotal();
	
	
	/**
	 * Consulta si un trayecto puede y/o va a usar un barco.
	 * @return true si puede usar un barco, false en caso contrario.
	 */
	public abstract boolean usaBarco();
	
	
	/**
	 * Consulta si un trayecto puede y/o va a usar un tren.
	 * @return true si puede usar un tren, false en caso contrario.
	 */
	public abstract boolean usaTren();
	
	
	/**
     * Crea un nuevo trayecto a modo de copia.
     * @return la copia del trayecto
     */
	@Override
	public Trayecto clone() {
		Trayecto copia = null;
		try {
			copia = (Trayecto) super.clone();
	        copia.origenMuelle = this.origenMuelle.clone();
	        copia.destinoMuelle = this.destinoMuelle.clone();
	        copia.origenPuerto = this.origenPuerto.clone();
	        copia.destinoPuerto = this.destinoPuerto.clone();
	        copia.fechaInicioTrayecto = LocalDate.of(
	            this.fechaInicioTrayecto.getYear(),
	            this.fechaInicioTrayecto.getMonthValue(),
	            this.fechaInicioTrayecto.getDayOfMonth()
	        );
	        copia.fechaFinTrayecto = LocalDate.of(
	            this.fechaFinTrayecto.getYear(),
	            this.fechaFinTrayecto.getMonthValue(),
	            this.fechaFinTrayecto.getDayOfMonth()
	        );
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return copia;
	}
	

	
	/**
	 * Devuelve una representacion en formato de texto del trayecto, incluyendo informacion
	 * sobre el puerto de origen, el puerto de destino y las fechas de inicio y fin del 
	 * trayecto.
	 * 
	 * @return Una cadena de texto que contiene la informacion detallada del trayecto 
	 * en el siguiente formato:
	 *         Inicio trayecto
	 *         		Pais del Puerto Origen: [codigo del pais]
     *				Localidad del Puerto Origen: [codigo de localidad]
	 *				Fecha de Inicio de Trayecto: [fecha inicio]
	 *         	Fin trayecto
	 *          	Pais del Puerto Destino: [codigo del pais]
	 *           	Localidad del Puerto Destino: [codigo de localidad]
	 *           	Fecha de Inicio de Trayecto: [fecha fin]
	 */
	@Override
	public String toString() {
	    String paisOrigen = origenPuerto.getIdentificador().substring(0, 2); 
	    String localidadOrigen = origenPuerto.getIdentificador().substring(3,6);
	    String inicio = "<Inicio trayecto>\n\t<Pais del Puerto Origen: " + paisOrigen 
	            + ">\n\t<Localidad del Puerto Origen: " + localidadOrigen 
	            + ">\n\t<Fecha de Inicio de Trayecto: " + fechaInicioTrayecto + ">";
	    
	    String paisDestino = destinoPuerto.getIdentificador().substring(0, 2);
	    String localidadDestino = destinoPuerto.getIdentificador().substring(3, 6);
	    String fin = "\n<Fin trayecto>\n\t<Pais del Puerto Destino: " + paisDestino 
	            + ">\n\t<Localidad del Puerto Destino: " + localidadDestino 
	            + ">\n\t<Fecha de Inicio de Trayecto: " + fechaFinTrayecto + ">\n";
	    
	    return inicio + fin;
	}
	
	
	/*
	 * Genera copia de una fecha.
	 * Importante para cuando se crean clones de trayectos.
	 * @param fecha que se quiere clonar. SE SUPONE QUE NO ES NULL.
	 * @return la copia de la fecha dada.
	 */
	private static LocalDate devuelveCopiaFecha(LocalDate fecha) {
		return LocalDate.of(fecha.getYear(), fecha.getMonthValue(), fecha.getDayOfMonth());
	}
	
	
	private void setFechaInicioTrayecto(LocalDate fechaInicioTrayecto) {
		if (fechaInicioTrayecto == null) throw new IllegalArgumentException();
		int agno = fechaInicioTrayecto.getYear();
		int mes = fechaInicioTrayecto.getMonthValue();
		int dia = fechaInicioTrayecto.getDayOfMonth();
		
		this.fechaInicioTrayecto = LocalDate.of(agno, mes, dia);
	}
	
	
	private void setFechaFinTrayecto(LocalDate fechaFinTrayecto) {
		if (fechaFinTrayecto == null) throw new IllegalArgumentException();
		if (fechaInicioTrayecto.isAfter(fechaFinTrayecto)) throw new IllegalArgumentException("La fecha de inicio de trayecto debe ser anterior de la fecha de fin de trayecto");
		int agno = fechaFinTrayecto.getYear();
		int mes = fechaFinTrayecto.getMonthValue();
		int dia = fechaFinTrayecto.getDayOfMonth();
		
		this.fechaFinTrayecto = LocalDate.of(agno, mes, dia);
	}


	private void setMuelleOrigen(Muelle origenMuelle) {
		if (origenMuelle == null) throw new IllegalArgumentException();
		
		if (!origenPuerto.isMuelleEnPuerto(origenMuelle.getIdMuelle())) {
			throw new IllegalArgumentException("El muelle de origen debe encontrarse en el puerto de origen");
		}
		this.origenMuelle = origenMuelle.clone();
	}
	
	
	private void setMuelleDestino(Muelle destinoMuelle) {
		if (destinoMuelle == null) throw new IllegalArgumentException();
		
		if (!destinoPuerto.isMuelleEnPuerto(destinoMuelle.getIdMuelle())) {
			throw new IllegalArgumentException("El muelle de destino debe encontrarse en el puerto de destino");
		}
		this.destinoMuelle = destinoMuelle.clone();
	}


	private void setPuertoOrigen(Puerto origenPuerto) {
		if (origenPuerto == null) throw new IllegalArgumentException();
		this.origenPuerto = origenPuerto.clone();	
	}
	
	
	private void setPuertoDestino(Puerto destinoPuerto) {
		if (destinoPuerto == null) throw new IllegalArgumentException();
		if (destinoPuerto.getIdentificador().equals(origenPuerto.getIdentificador())) {
			throw new IllegalArgumentException("El puerto de destino no puede ser el mismo que de el de origen");
		}
		this.destinoPuerto = destinoPuerto.clone();
	}
}
