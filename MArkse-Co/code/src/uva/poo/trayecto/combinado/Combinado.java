package uva.poo.trayecto.combinado;

import java.time.LocalDate;
import java.util.ArrayList;

import uva.poo.puerto.Muelle;
import uva.poo.puerto.Puerto;
import uva.poo.trayecto.Trayecto;
import uva.poo.trayecto.simple.Simple;


/**
 * Representa un Trayecto Combinado en la compania de transporte de mercacias MArkse-Co.
 * Estos están caracterizados por formar pack de trayectos simples.
 * Se puede consultar el origen y destino de una trayecto, las fechas de
 * inicio y fin de trayecto. También se puede conocer que tipo de transporte
 * puede y/o va a utilizar un trayecto.
 * Permite calcular los costes de un trayectos y conocer sus descuentos.
 * 
 * @author Marta Pérez
 * @author Emily Rodrigues
 */
public abstract class Combinado extends Trayecto {
	
	private ArrayList<Simple> listaTSimples;
	
	/**
	 * Inicializa un trayecto combinado a partir de:
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
	protected Combinado(Muelle origenMuelle, Muelle destinoMuelle, Puerto origenPuerto,
			Puerto destinoPuerto, LocalDate fechaInicioTrayecto, LocalDate fechaFinTrayecto) {
		super(origenMuelle, destinoMuelle, origenPuerto, destinoPuerto, fechaInicioTrayecto, fechaFinTrayecto);
		listaTSimples = new ArrayList<>();
	}
	
	
	/**
	 * Agrega un trayecto simple en un combinado (un pack de simples).
	 * Para poder ser agregado el trayecto simple, debe cumplir con el tipo de transporte
	 * que admite el pack.
	 * Los trayectos se agregan de forma secuencial, es decir,
	 * van aumentando las fechas.
	 * Para que un trayecto_n sea valido, este debe tener como fecha
	 * inicio de trayecto la fecha de fin del trayecto_(n - 1) o ser posterior
	 * a esta.
	 * Por otro lado, el puerto de origen de trayecto_n debe ser igual al puerto
	 * destino de trayecto_(n - 1).
	 * Por otro lado, el muelle de origen de trayecto_n debe ser igual al muelle
	 * destino de trayecto_(n - 1).
	 * @param trayectoSimple el trayecto simple que se quiere agregar. No puede
	 * ser null. No puede existir un trayecto igual en el pack.
	 * @throws IllegalArgumentException si {@code trayectoSimple == null}
	 * @throws IllegalStateException si ya había un trayecto igual agregado con anterioridad.
	 * @throws IllegalStateException si se agrega un trayecto que usa barco cuando el 
	 * trayecto combinado no lo permite.
	 * @throws IllegalStateException si se agrega un trayecto que usa tren cuando el 
	 * trayecto combinado no lo permite.
	 */
	public void agregaTrayecto(Simple trayectoSimple) {
		if (trayectoSimple == null) throw new IllegalArgumentException();
		if (listaTSimples.contains(trayectoSimple)) throw new IllegalStateException();
		if (trayectoSimple.usaBarco() && usaTren()) throw new IllegalStateException();
		if (trayectoSimple.usaTren() && usaBarco()) throw new IllegalStateException();
		if (trayectoSimple.isFechaFinPosterior(getFechaFin())) throw new IllegalStateException();
		if (hayTrayectos()) {
			comprobacionesCuandoHayTrayectosYaAgregados(trayectoSimple);
		} else {
			comprobacionesCuandoNoHayTrayectos(trayectoSimple);
		}
				
		listaTSimples.add(trayectoSimple.clone());
	}
	
	
	private void comprobacionesCuandoHayTrayectosYaAgregados(Simple simple) {
		compruebaCoincidenciasFechasPuertosYMuelles(simple);
	}
	
	
	private void comprobacionesCuandoNoHayTrayectos(Simple simple) {
		boolean mismasFechasInicio = simple.getFechaInicio().equals(getFechaInicio());
		boolean mismosPuertoOrigen = simple.getPuertoOrigen().equals(getPuertoOrigen());
		boolean mismosMuelleOrigen = simple.getMuelleOrigen().equals(getMuelleOrigen());
		
		if (!mismasFechasInicio || !mismosPuertoOrigen || !mismosMuelleOrigen)
			throw new IllegalStateException();
	}
	
	
	/**
	 * Consulta los trayectos simples que contiene el trayecto combinado.
	 * @return lista de trayectos simples del trayecto combinado. Estara vacia 
	 * si no hay trayectos agregados (de momento) en el pack.
	 */
	public ArrayList<Simple> getTrayectosSimples() {
		ArrayList<Simple> copia = new ArrayList<>();
		for (Simple simple : listaTSimples) {
			copia.add(simple);
		}
		return copia;
	}
	
	
	/**
	 * Consulta el numero de trayectos simples incluidos
	 * en el combinado. 
	 * @return el numero de trayectos simples en el combinado.
	 */
	public int numeroTrayectosSimples() {
		return listaTSimples.size();
	}
	
	
	/**
	 * Consulta si hay trayectos simples agregados en el pack. 
	 * @return true si hay trayectos, false en caso contrario.
	 */
	public boolean hayTrayectos() {
		return !listaTSimples.isEmpty();
	}
	
	/**
	 * Calcula el descuento de un trayecto simple que forma parte
	 * del pack.
	 * @param simple el trayecto simple sobre el que se le puede aplicar descuento.
	 * Si no tiene descuento aplicable, se devuelve el coste total y original del 
	 * trayecto
	 * @return el descuento aplicado al coste del trayecto o el coste direcnto del trayecto
	 * en caso de no tener descuento.
	 */
	public abstract double calculaDescuento(Simple simple);
	
	
	/**
	 * Calcula el coste total del trayecto combinado a partir de 
	 * los simples que contiene, incluyendo el descuento aplicable (si
	 * hay).
	 * @return coste total de los trayectos.
	 */
	@Override
	public double costeTotal() {
		double total = 0;
		for (Simple trayecto : listaTSimples) {
			total += calculaDescuento(trayecto);
		}
		return total;
	}
	
	
	/**
     * Crea un nuevo trayecto combinado a modo de copia.
     * @return la copia del trayecto 
     */
	@Override
	public Combinado clone() {
		Combinado copia = (Combinado) super.clone();
		ArrayList<Simple> copiaListaTSimples = new ArrayList<>();
		for (Simple simple: listaTSimples) {
			copiaListaTSimples.add(simple.clone());
		}
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
	 * 		- mismo trayectos simples
	 */
	@Override
	public boolean equals(Object otro) {
		if (!super.equals(otro)) return false; 
		Combinado otroCombinado = (Combinado) otro;
		return listaTSimples.equals(otroCombinado.getTrayectosSimples());
	}
	
	
	/*
	 * Los trayectos se agregan de forma secuencial, es decir,
	 * van aumentando las fechas.
	 * Para que un trayecto_n sea valido, este debe tener como fecha
	 * inicio de trayecto la fecha de fin del trayecto_(n - 1) o ser posterior
	 * a esta.
	 * Por otro lado, el puerto de origen de trayecto_n debe ser igual al puerto
	 * destino de trayecto_(n - 1).
	 * Por otro lado, el muelle de origen de trayecto_n debe ser igual al muelle
	 * destino de trayecto_(n - 1).
	 */
	private void compruebaCoincidenciasFechasPuertosYMuelles(Simple simple) {
		int indiceUltimoTrayecto = indiceDelUltimoTrayectoAgregado();
		Simple ultTrayecto = listaTSimples.get(indiceUltimoTrayecto);
		
		if (ultTrayecto.isFechaFinPosterior(simple.getFechaInicio())) {
			throw new IllegalStateException();
		}
		if (!ultTrayecto.getPuertoDestino().equals(simple.getPuertoOrigen())
				|| !ultTrayecto.getMuelleDestino().equals(simple.getMuelleOrigen())) {
			throw new IllegalStateException();
		}
	}
	
	
	private int indiceDelUltimoTrayectoAgregado() {
		return listaTSimples.size() - 1;
	}
}

