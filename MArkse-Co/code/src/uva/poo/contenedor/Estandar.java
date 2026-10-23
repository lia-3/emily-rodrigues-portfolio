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
public class Estandar extends Contenedor implements Cloneable{
	
	boolean tieneTecho;
	
	/**
	 * Crea un contenedor estandar a partir de:
	 * 		- Un identificador que sigue la ISO 6346 hasta el digito de control (ver 
	 * 		  <a href="https://en.wikipedia.org/wiki/ISO_6346">ISO 6346</a>)
	 * 
	 * 		- El peso del contenedor.
	 * 		- La maxima carga uitl permitida.
	 * 		- La unidad de peso, aplicable para el peso del contenedor y la maxima
	 * 		  carga util.
	 * 		- El volumen del contenedor.
	 * 		- La unidad de volumen.
	 * 		- El estado del contenedor.
	 * Se supone que cualquier contenedor estándar tiene techo.
	 * Igualmente se considera que un contenedor estándar al crearse tiene techo, pero 
	 * esta característica puede variar.
	 * 
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
	public Estandar(String idContenedor, double pesoTara, double maxCargaUtil, UnidadesPeso unidadPeso,
			double volumen, UnidadesVolumen unidadVolumen, EstadoContenedor estado) {
		super(idContenedor, pesoTara, maxCargaUtil, unidadPeso, volumen, unidadVolumen, estado);
		tieneTecho = true;
	}
	
	
	/**
	 * Elimina el techo del contenedor. Debe tener techo para poder realizar esta operación.
	 * @throws IllegalStateException si el contenedor no tenía techo.
	 */
	public void setNoTieneTecho() {
		if (!tieneTecho) throw new IllegalStateException();
		tieneTecho = false;
	}
	
	
	/**
	 * Agrega techo al contenedor. Debe no tener techo para poder realizar esta operación.
	 * @throws IllegalStateException si el contenedor ya tenía techo.
	 */
	public void setTieneTecho() {
		if (tieneTecho) throw new IllegalStateException();
		tieneTecho = true;
	}

	/**
	 * Consulta si un contenedor estandar tiene techo.
	 * @return true en caso de tener techo, false en caso contrario.
	 */
	@Override
	public boolean tieneTecho() {
		return tieneTecho;
	}

	
	/**
	 * Consulta si un contenedor estandar tiene paredes (siempre las tiene).
	 * @return true, porque siempre las tiene.
	 */
	@Override
	public boolean tieneParedes() {
		return true;
	}

	
	/**
	 * Consulta si un contenedor estandar tiene motor (nunca tiene).
	 * @return false, porque nunca tiene.
	 */
	@Override
	public boolean tieneMotor() {
		return false;
	}
	
	/**
     * Crea un nuevo contenedor estandar a modo de copia.
     * @return la copia del contenedor
     */
	@Override
	public Estandar clone() {
		Estandar copia = null;
		copia = (Estandar) super.clone();
		copia.tieneTecho = tieneTecho;
		return copia;
	}
	
	
	/**
	 * Consulta si dos contenedores estandar son iguales. Es decir, si tienen mismo 
	 * identificador.
	 * @param otro contenedor con el que se quiere comparar. No puede ser null.
	 * @return true en caso de tener ambos el mismo identificador, false en
	 * caso contrario.
	 */
	@Override
	public boolean equals(Object otro) {
		return super.equals(otro);
	}
}
