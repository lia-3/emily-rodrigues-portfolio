package uva.poo.contenedor;


/**
 * Representa un Contenedor en la compania de transporte de mercancias MArkse-Co.
 * Se puede consultar de un contenedor su identificador (basado en la ISO 6346), 
 * peso de tara, carga maxima, volumen, estado, si tiene techo, paredes y motor.
 *
 * Se puede calcular el precio total de transporte y trabajar con los listaTrayectos 
 * del viaje de un contenedor.
 * 
 * @author Marta Pérez
 * @author Emily Rodrigues
 */
public class Refrigerado extends Contenedor {

	
	/**
	 * Crea un contenedor refigerado a partir de:
	 * 		- Un identificador que sigue la ISO 6346 hasta el digito de control (ver 
	 * 		  <a href="https://en.wikipedia.org/wiki/ISO_6346">ISO 6346</a>)
	 * 		- El peso del contenedor.
	 * 		- La maxima carga uitl permitida.
	 * 		- La unidad de peso, aplicable para el peso del contenedor y la maxima
	 * 		  carga util.
	 * 		- El volumen del contenedor.
	 * 		- La unidad de volumen.
	 * 		- El estado del contenedor.
	 * @param idContenedor identificador del contenedor. No puede ser null y 
	 * debe seguir la ISO 6346.
	 * @param pesoTara peso del contenedor. No puede ser menor o igual que cero.
	 * @param maxCargaUtil maxima carga util que permite el contenedor. No puede
	 * ser menor o igual que cero.
	 * @param unidadPeso unidad de peso de pesoTara y de maxCargaUtil. No puede
	 * ser null. 
	 * @param volumen volumen del contenedor. No puede ser menor o igual que cero.
	 * @param unidadVolumen unidad de volumen. No puede ser null.
	 * @param estado estado del contenedor. No puede ser null.
	 * 
	 * @throws IllegalArgumentException si idContenedor, unidadPeso, unidadVolumen o
	 * estado son null.
	 * @throws IllegalArgumentException si idContenedor no sigue la ISO 6346.
	 * @throws IllegalArgumentException si pesoTara, maxCargaUtil o volumen
	 * son menores o iguales a cero.
	 */
	public Refrigerado(String idContenedor, double pesoTara, double maxCargaUtil, UnidadesPeso unidadPeso,
			double volumen, UnidadesVolumen unidadVolumen, EstadoContenedor estado) { 
		super(idContenedor, pesoTara, maxCargaUtil, unidadPeso, volumen, unidadVolumen, estado);
	}
	
	
	/**
	 * Consulta si un refigerado tiene techo (siempre tiene).
	 * @return true, porque siempre tiene.
	 */
	@Override
	public boolean tieneTecho() {
		return true;
	}

	
	/**
	 * Consulta si un refigerado tiene paredes (siempre tiene).
	 * @return true, porque siempre tiene.
	 */
	@Override
	public boolean tieneParedes() {
		return true;
	}

	
	/**
	 * Consulta si un siempre tiene motor (siempre tiene).
	 * @return true, porque siempre tiene.
	 */
	@Override
	public boolean tieneMotor() {
		return true;
	}
	
	
	/**
     * Crea un nuevo contenedor refrigerado a modo de copia.
     * @return la copia del contenedor
     */
	@Override
	public Refrigerado clone() {
		return (Refrigerado) super.clone();
	}
}
