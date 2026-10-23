package uva.poo.contenedor;

import java.util.ArrayList;
import uva.poo.trayecto.Trayecto;


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
public abstract class Contenedor implements Cloneable{
	
	private String identificador;
	private double pesoTara;
	private double maximaCargaUtil;
	private double volumen;
	private EstadoContenedor estado;
	private ArrayList<Trayecto> listaTrayectos;
	
	private static final int LONGITUD_IDENTIFICADOR = 11;
	private static final double PESO_FACTOR_CAMBIO_UNIDADES = 2.20462;
	private static final double VOLUMEN_FACTOR_CAMBIO_UNIDADES = 35.3147;
	
	
	/**
	 * Crea un contenedor a partir de:
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
	protected Contenedor(String idContenedor, double pesoTara, double maxCargaUtil, UnidadesPeso unidadPeso,
						double volumen, UnidadesVolumen unidadVolumen, EstadoContenedor estado) {
		setIdentificador(idContenedor);
		setPesoTara(pesoTara, unidadPeso);
		setMaximaCargaUtil(maxCargaUtil, unidadPeso);
		setVolumen(volumen, unidadVolumen);
		setEstado(estado);
		setTrayectos();
	}
	
	
	/**
     * Crea un nuevo contenedor a modo de copia.
     * @return la copia del contenedor
     */
	@Override
	public Contenedor clone() {
		Contenedor copia = null;
		try {
			copia = (Contenedor) super.clone();
			copia.setIdentificador(identificador);
			copia.setEstado(estado);
			copia.setTrayectos(getTrayectos());
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return copia;
	}
	
	
	/**
	 * Consulta el identificador del contenedor.
	 * @return identificador del contenedor.
	 */
	public String getIdentificador() {
		return identificador;
	}
	
	
	/**
	 * Consulta el peso del contenedor en kilogramos.
	 * @return peso del contenedor en kilogramos.
	 */
	public double getPesoTaraKilogramos() {
		return pesoTara;
	}
	
	
	/**
	 * Consulta el peso del contenedor en libras.
	 * @return peso del contenedor en libras.
	 */
	public double getPesoTaraLibras() {
		return conversionDeKilogramosALibras(pesoTara);
	}
	
	
	/**
	 * Consulta la maxima carga util del contenedor en kilogramos.
	 * @return maxima carga util del contenedor en kilogramos.
	 */
	public double getMaximaCargaUtilKilogramos() {
		return maximaCargaUtil;
	}
	
	
	/**
	 * Consulta la maxima carga util del contenedor en libras.
	 * @return maxima carga util del contenedor en libras.
	 */
	public double getMaximaCargaUtilLibras() {
		return conversionDeKilogramosALibras(maximaCargaUtil);
	}
	
	
	/**
	 * Consulta el volumen del contenedor en metros cubicos.
	 * @return volumen del contenedor en metros cubicos.
	 */
	public double getVolumenMetrosCubicos() {
		return volumen;
	}
	
	
	/**
	 * Consulta el volumen del contenedor en pies cubicos.
	 * @return volumen del contenedor en metros cubicos.
	 */
	public double getVolumenPiesCubicos() {
		return conversionDeMetrosCubicosAPiesCubicos(volumen);
	}
	
	
	/**
	 * Consulta el estado del contenedor actualmente.
	 * @return estado del contenedor.
	 */
	public EstadoContenedor getEstado() {
		return estado;
	}
	
	
	/**
	 * Consulta la lista de listaTrayectos que tiene un contenedor actualmente.
	 * @return lista de listaTrayectos del contenedor.
	 */
	public ArrayList<Trayecto> getTrayectos(){
		ArrayList<Trayecto> copia = new ArrayList<>(listaTrayectos.size());
		
		for(Trayecto trayecto : listaTrayectos) {
			copia.add(trayecto.clone());
		}
		
		return copia;
	}
	
	
	
	
	
	/**
	 * Establece que el contenedor esta en estado RECOGIDA.
	 */
	public void setEstadoRecogida() {
		estado = EstadoContenedor.RECOGIDA;
	}
	
	
	/**
	 * Establece que el contenedor esta en estado TRANSITO.
	 */
	public void setEstadoTransito() {
		estado = EstadoContenedor.TRANSITO;
	}
	
	
	/**
	 * Consulta si dos contenedores son iguales. Es decir, si tienen mismo 
	 * identificador.
	 * @param otroContenedor contenedor con el que se quiere comparar. No puede ser null.
	 * @return true en caso de tener ambos el mismo identificador, false en
	 * caso contrario.
	 */
	@Override
	public boolean equals(Object otroContenedor) {
	    if (otroContenedor == null) return false; 
	    if (getClass() != otroContenedor.getClass()) return false;  
	    Contenedor otro = (Contenedor) otroContenedor;  
	    return getIdentificador().equals(otro.getIdentificador());  
	}

	
	/**
	 * Agrega un trayecto nuevo al viaje del contenedor.
	 * @param nuevoTrayecto trayecto a añadir en el viaje. No puede ser null.
	 * No puede ser igual a otro trayecto.
	 * @throws IllegalArgumentException si {@code nuevoTrayecto == null.}
	 * @throws IllegalArgumentException si ya esta ese trayecto igual en el viaje.
	 */
	public void agregaTrayecto(Trayecto nuevoTrayecto) {
		if (nuevoTrayecto == null) throw new IllegalArgumentException();
		for(Trayecto trayecto : listaTrayectos) {
			if (nuevoTrayecto.equals(trayecto)) {
				throw new IllegalArgumentException("No pueden haber dos trayecto iguales");
			}
		}
		listaTrayectos.add(nuevoTrayecto.clone());
	}
	
	
	/**
	 * Calcula el precio total del transporte, es decir, el precio del viaje
	 * a partir del coste por dia y del coste por milla marina internacionales.
	 * @return precio total de el viaje completo.
	 */
	public double calculaPrecioTotalTransporte() {		
		double precioTotal = 0;
		for(Trayecto trayecto : listaTrayectos) {
			precioTotal += trayecto.costeTotal();
		}
		return precioTotal;
	}
	
	
	/**
	 * Inicia un nuevo viaje.
	 */
	public void iniciaNuevoViaje() {
		setTrayectos();
	}
	
	
	/**
	 * Cuenta el numero de listaTrayectos del viaje del contenedor.
	 * @return umero de listaTrayectos del viaje
	 */
	public int cuentaTrayectos() {
		return listaTrayectos.size();
	}
	
	
	/**
	 * Consulta si un contenedor esta en transito.
	 * @return true si esta en transito, false en caso contrario.
	 */
	public boolean isEnTransito() {
		return estado.equals(EstadoContenedor.TRANSITO);
	}
	
	
	/**
	 * Consulta si un contenedor esta en recogida.
	 * @return true si esta en recogida, false en caso contrario.
	 */
	public boolean isEnRecogida() {
		return estado.equals(EstadoContenedor.RECOGIDA);
	}
	
	
	/**
	 * Consulta si el contenedor en un viaje completo usó, usa o usará
	 * un barco.
	 * @return true si el contenedor usó, usa o usará un barco, false
	 * caso contrario.
	 */
	public boolean usaBarco() {
		for(Trayecto trayecto : listaTrayectos) {
			if (trayecto.usaBarco()) return true;
		}
		return false;
	}
	
	
	/**
	 * Consulta si el contenedor en un viaje completo usó, usa o usará
	 * un tren.
	 * @return true si el contenedor usó, usa o usará un tren, false
	 * caso contrario.
	 */
	public boolean usaTren() {
		for(Trayecto trayecto : listaTrayectos) {
			if (trayecto.usaTren()) return true;
		}
		return false;
	}
	
	
	/**
	 * Consulta si el contenedor tiene techo.
	 * @return true si tiene techo, false en caso contrario.
	 */
	public abstract boolean tieneTecho();
	
	
	/**
	 * Consulta si un contenedor tiene paredes.
	 * @return true si el contenedor tiene paredes, false en caso contrario.
	 */
	public abstract boolean tieneParedes();
	
	
	/**
	 * Consulta si un contenedor tiene motor.
	 * @return true si el contenedor tiene motor, false en caso contrario.
	 */
	public abstract boolean tieneMotor();

	
	/*
	 * IllegalArgumentException si maxCargaUtil <= 0
	 * IllegalArgumentException si unidadPeso == null
	 */
	private void setMaximaCargaUtil(double maxCargaUtil, UnidadesPeso unidadPeso) {
		maximaCargaUtil = compruebaVariablePeso(maxCargaUtil, unidadPeso);
	}
	
	
	/*
	 * IllegalArgumentException si peso <= 0.0
	 * IllegalArgumentException si unidadPeso == null
	 */
	private void setPesoTara(double peso, UnidadesPeso unidadPeso) {
		pesoTara = compruebaVariablePeso(peso, unidadPeso);
	}
	
	
	/*
	 * IllegalArgumentException si nuevoValor <= 0.0
	 * IllegalArgumentException si unidad == null
	 */
	private double compruebaVariablePeso(double nuevoValor, UnidadesPeso unidad) {
		if (unidad == null) throw new IllegalArgumentException();
		if (nuevoValor <= 0) throw new IllegalArgumentException();
		
		if (unidad.equals(UnidadesPeso.LIBRAS)) {
			return conversionDeLibrasAKilogramos(nuevoValor);
		}
		else {
			return nuevoValor;
		}
	}
	
	
	/*
	 * IllegalArgumentException si volumen <= 0.0
	 * IllegalArgumentException si unidadVolumen == null
	 */
	private void setVolumen(double volumen, UnidadesVolumen unidadVolumen) {
		if (volumen <= 0) throw new IllegalArgumentException();
		if (unidadVolumen == null) throw new IllegalArgumentException();
		
		
		if(unidadVolumen.equals(UnidadesVolumen.PIES_CUBICOS)) {
			this.volumen = conversionDePiesCubicosAMetrosCubicos(volumen);
		} else {
			this.volumen = volumen;
		}
	}
	
	
	private double conversionDePiesCubicosAMetrosCubicos(double piesCubicos) {
		return (piesCubicos / VOLUMEN_FACTOR_CAMBIO_UNIDADES);
	}
	
	
	private double conversionDeLibrasAKilogramos(double libras) {
		return (libras / PESO_FACTOR_CAMBIO_UNIDADES);
	}
	

	private double conversionDeMetrosCubicosAPiesCubicos(double metrosCubicos) {
		return (metrosCubicos * VOLUMEN_FACTOR_CAMBIO_UNIDADES);
	}
	
	
	private double conversionDeKilogramosALibras(double kilogramos) {
		return (kilogramos * PESO_FACTOR_CAMBIO_UNIDADES);
	}
	
	/*
	 * IllegalArgumentException si estado == null
	 */
	private void setEstado(EstadoContenedor estado) {
		if (estado == null) throw new IllegalArgumentException();
		
		this.estado = estado;
	}
	
	
	private void setTrayectos() {
		listaTrayectos = new ArrayList<>();
	}
	
	
	private void setTrayectos(ArrayList<Trayecto> lista) {
		listaTrayectos = new ArrayList<>();
		for(Trayecto trayecto : lista) {
			listaTrayectos.add(trayecto.clone());
		}
	}
	
	
	
	/*
	 * IllegalArgumentException si idContenedor == null
	 * IllegalArgumentException si idContenedor no sigue ISO 6346
	 */
	private void setIdentificador(String idContenedor) {
		String codigoDuenno;
		char equipamiento;
		String numeroSerie;
		String digitoControl = "";
		
		compruebaLongitudIdentificador(idContenedor);
		
		codigoDuenno = idContenedor.substring(0, 3);
		compruebaCodigoDuenno(codigoDuenno);
		
		equipamiento = idContenedor.charAt(3);
		compruebaEquipamiento(equipamiento);
		
		numeroSerie = idContenedor.substring(4, LONGITUD_IDENTIFICADOR - 1);
		compruebaCadenaNumeros(numeroSerie);
		
		digitoControl += idContenedor.charAt(LONGITUD_IDENTIFICADOR - 1);
		compruebaCadenaNumeros(digitoControl);
		
		compruebaDigitoControl(idContenedor);
		
		identificador = idContenedor;
		
	}
	
	
	/*
	 * IllegalArgumentException si idContenedor == null
	 * IllegalArgumentException si idContenedor.length() != LONGITUD_IDENTIFICADOR
	 */
	private void compruebaLongitudIdentificador(String idContenedor) {
		if (idContenedor == null) throw new IllegalArgumentException();
		if(idContenedor.length() != LONGITUD_IDENTIFICADOR) throw new IllegalArgumentException();
	}
	
	
	/*
	 * IllegalArgumentException si todos los caracteres de codigoDuenno no son [A..Z]
	 */
	private void compruebaCodigoDuenno(String codigoDuenno) {
		compruebaSiLaCadenaSeEncuentraEnUnRangoDeCaracteres(codigoDuenno, 'A', 'Z');
	}
	
	
	/*
	 * IllegalArgumentException si equipamiento no es [U, J, Z]
	 */
	private void compruebaEquipamiento(char equipamiento) {
		if ((equipamiento != 'J') && (equipamiento != 'U') && (equipamiento != 'Z')) {
			throw new IllegalArgumentException();
		}
	}
	
	
	/*
	 * IllegalArgumentException si no es cadena numerica
	 */
	private void compruebaCadenaNumeros(String numeroSerie) {
		compruebaSiLaCadenaSeEncuentraEnUnRangoDeCaracteres(numeroSerie, '0', '9');
	}
	
	
	/*
	 * Se supone que menorCaracter < mayorCaracter
	 */
	private void compruebaSiLaCadenaSeEncuentraEnUnRangoDeCaracteres(String cadena, char menorCaracter, char mayorCaracter) {		
		for (int i = 0; i < cadena.length(); i++) {
			if ((cadena.charAt(i) < menorCaracter) || (cadena.charAt(i) > mayorCaracter)) {
				throw new IllegalArgumentException();
			}
		}
	}
	
	
	/*
	 * 
	 * 
	 */
	private void compruebaDigitoControl(String idContenedor) {
		int sumaValoresYPosiciones = 0;
		int temporal;
		int digitoControlEsperado;
		int digitoControlDado = Integer.parseInt(idContenedor.charAt(LONGITUD_IDENTIFICADOR-1) + "");
		char caracter;
		
		for(int i = 0; i < LONGITUD_IDENTIFICADOR - 1; i++) {
			caracter = idContenedor.charAt(i);
			
			if(isLetra(caracter)) {
				sumaValoresYPosiciones += obtieneValorLetra(caracter, i);
			} else {
				sumaValoresYPosiciones += obtieneValorDigito(caracter, i);
			}
		}
		temporal = sumaValoresYPosiciones / LONGITUD_IDENTIFICADOR;
		temporal *= LONGITUD_IDENTIFICADOR;
		digitoControlEsperado = sumaValoresYPosiciones - temporal;
		
		if (digitoControlEsperado != digitoControlDado) {
			throw new IllegalArgumentException();
		}
	}
	
	
	private boolean isLetra(char caracter) {
		return ((caracter >= 'A') && (caracter <= 'Z'));
	}
	
	
	/*
	 * Supone que la pasan una letra mayuscula como caracter
	 */
	private int obtieneValorLetra(char caracter, int posicion) {
		if (caracter == 'A') {
			return 10 * (int)Math.pow(2, posicion);
			
		} else if ((caracter >= 'B') && (caracter <= 'K')) {
			return (12 + (caracter - 'B')) * (int)Math.pow(2, posicion);
			
		} else if ((caracter >= 'L') && (caracter <= 'V')) {
			return (23 + (caracter - 'L')) * (int)Math.pow(2, posicion);
			
		} else {
			return (34 + (caracter-'V')) * (int)Math.pow(2, posicion);
		}
	}
	
	
	/*
	 * Supone que le pasan un digito como caracter
	 */
	private int obtieneValorDigito(char caracter, int posicion) {
		return (caracter - '0') * (int)Math.pow(2, posicion);
	}

}
