package uva.poo.puerto;

import java.util.ArrayList;
import es.uva.inf.poo.maps.GPSCoordinate;
import uva.poo.contenedor.Contenedor;

/**
 * Representa un Puerto en la compania de transporte de mercacias MArkse-Co.
 * 
 * Se pueden consultar las caracteristicas de un puerto como los muelles que tiene, 
 * su estado actual y su identificador.
 * 
 * Se pueden realizar consultas sobre los muelles del puerto como el estado 
 * de los mismos, asi como gestionar los muelles de un puerto.
 * @author Marta Pérez
 * @author Emily Rodrigues
 */
public class Puerto implements Cloneable{

	private String identificador;
	private ArrayList<Muelle> muelles;
	
	
	/**
     * Constructor que crea un nuevo Puerto con el identificador proporcionado.
     * @param idPuerto El identificador del puerto en formato de 6 caracteres que 
     * representan el pais y la localidad del puerto separados por un guion (PP-LLL):
     * 		- Pais en donde se encuentra el puerto: PP.
     * 		- Localidad del puerto: LLL
     * @throws IllegalArgumentException Si el identificador es null.
     * @throws IllegalArgumentException Si el identificador no sigue el formato 
     * adecuado.
     */
	public Puerto(String idPuerto) {
		setIdentificador(idPuerto);
		setMuelles();
	}
	
	
	/**
     * Crea un nuevo puerto a modo de copia.
     * @return la copia del puerto
     */
	@Override
	public Puerto clone() {
		Puerto copia = null;
		try {
			copia = (Puerto) super.clone();
			copia.setIdentificador(this.getIdentificador());
			copia.setMuelles(this.getMuelles());
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return copia;
	}
	
	
	/**
     * Obtiene el identificador del puerto.
     * @return String identificador del puerto.
     */
	public String getIdentificador() {
		return identificador;
	}
	
	
	/**
     * Obtiene una lista de muelles del puerto.
     * @return ArrayList lista de muelles del puerto.
     */
	public ArrayList<Muelle> getMuelles() {
		ArrayList<Muelle> copiaMuelles = new ArrayList<>();
		for (Muelle muelle : muelles) {
			copiaMuelles.add(muelle.clone());
		}
		return copiaMuelles;
	}
	
	
	/**
     * Consulta si un puerto esta operativo.
     * Un puerto esta operativo si tiene al menos un
     * muelle operativo.
     * Devuelve false si no tiene ningún muelle en el puerto.
     * @return true si al menos un muelle es operativo, false en caso contrario.
     */
	public boolean isOperativo() {
		if (muelles.isEmpty()) return false;
		for (Muelle muelle : muelles) {
			if (muelle.isOperativo()) return true;
		}
		return false;
	}
	
	
	/**
     * Consulta si un puerto esta fuera de servicio. 
     * Un puerto esta fuera de servicio si todos sus muelles
     * estan fuera de servicio.
     * Devuelve true si no tiene ningún muelle en el puerto.
     * @return true si el puerto esta fuera de servicio, false en caso contrario.
     * Devuelve false si no hay muelles en el puerto.
     */
	public boolean isFueraServicio() {
		if (muelles.isEmpty()) return false;
		for (Muelle muelle : muelles) {
			if (muelle.isOperativo()) return false;
		}
		return true;
	}
	
	
	/**
     * Consulta si un muelle especifico dado por su identificador en el puerto esta
     * operativo.
     * @param idMuelle El identificador del muelle.
     * @return true si el muelle esta operativo, false si no lo esta.
     * @throws IllegalArgumentException si {@code idMuelle == null}.
     * @throws IllegalArgumentException Si el muelle no esta en el puerto.
     */
	public boolean isMuelleOperativo(String idMuelle) {
		if (!isMuelleEnPuerto(idMuelle)) throw new IllegalArgumentException();
		
		for (Muelle muelle : muelles) {
			if (muelle.getIdMuelle().equals(idMuelle) && muelle.isOperativo()) {
				return true;
			}
		}
		return false;
	}
	
	
	/**
     * Consulta si un muelle especifico dado por su id en el puerto esta fuera de 
     * servicio.
     * @param idMuelle El identificador del muelle.
     * @return true si el muelle esta fuera de servicio, false en caso contrario.
     * @throws IllegalArgumentException si {@code idMuelle == null}.
     * @throws IllegalArgumentException Si el muelle no esta en el puerto.
     */
	public boolean isMuelleFueraServicio(String idMuelle) {
		return !isMuelleOperativo(idMuelle);
	}
	
	
	/**
     * Obtiene una lista de muelles operativos en el puerto actual.
     * @return ArrayList lista de muelles operativos.
     */
	public ArrayList<Muelle> getMuellesOperativos() {
		ArrayList<Muelle> muellesOperativos = new ArrayList<>(0);
		
		for (Muelle muelle : muelles) {
			if (muelle.isOperativo()) {
				muellesOperativos.add(muelle.clone());
			}
		}
		return muellesOperativos;
	}
	
	
	/**
     * Obtiene el numero de muelles operativos en el puerto actual.
     * @return int numero de muelles operativos.
     */
	public int getNumeroMuellesOperativos() {
		return getMuellesOperativos().size();
	}
	
	
	/**
     * Obtiene una lista de muelles fuera de servicio en el puerto actual.
     * @return ArrayList lista de muelles fuera de servicio.
     */
	public ArrayList<Muelle> getMuellesFueraServicio() {
		ArrayList<Muelle> muellesFueraServicio = new ArrayList<>(0);
		
		for (Muelle muelle : muelles) {
			if (muelle.isFueraDeServicio()) {
				muellesFueraServicio.add(muelle.clone());
			}
		}
		return muellesFueraServicio;
	}
	
	
	/**
     * Obtiene el numero de muelles fuera de servicio en el puerto actual.
     * Devuelve cero si no hay muelles en el puerto.
     * @return int numero de muelles fuera de servicio.
     */
	public int getNumeroMuellesFueraServicio() {
		return getMuellesFueraServicio().size();
	}
	
	
	/**
     * Obtiene una lista de muelles que tienen espacio disponible en el puerto actual.
     * @return ArrayList lista de muelles con espacio disponible.
     */
	public ArrayList<Muelle> getMuellesConEspacio() {
		ArrayList<Muelle> muellesConEspacio = new ArrayList<>(0);
		Muelle espacio;
		
		for(Muelle muelle : muelles) {
			if (muelle.tieneEspacio()) {
				espacio = muelle.clone();
				muellesConEspacio.add(espacio);
			}
		}
		return muellesConEspacio;
	}
	
	
	/**
     * Obtiene el numero de muelles con espacio disponible en el puerto actual.
     * Devuelve cero si no hay muelles en el puerto.
     * @return int numero de muelles con espacio disponible.
     */
	public int getNumeroMuellesConEspacio() {
		return getMuellesConEspacio().size();
	}
	
	
	/**
     * Obtiene una lista de muelles sin espacio disponible en el puerto actual.
     * @return ArrayList lista de muelles sin espacio disponible.
     */
	public ArrayList<Muelle> getMuellesSinEspacio() {
		ArrayList<Muelle> muellesSinEspacio = new ArrayList<>(0);
		Muelle sinEspacio;
		
		for (Muelle muelle : muelles) {
			if (!muelle.tieneEspacio()) {
				sinEspacio = muelle.clone();
				muellesSinEspacio.add(sinEspacio);
			}
		}
		return muellesSinEspacio;
	}
	
	
	/**
     * Obtiene el numero de muelles sin espacio disponible en el puerto.
     * Devuelve cero si no hay muelles en el puerto.
     * @return int numero de muelles sin espacio disponible.
     */
	public int getNumeroMuellesSinEspacio() {
		return getMuellesSinEspacio().size();
	}
	
	
	/**
	 * Verifica si el puerto esta completo, si todos los muelles estan sin espacio.
	 * Devuelve true si no hay muelles en el puerto.
	 * @return true si el puerto esta completo, false si hay muelles con espacio 
	 * disponible.
	 */
	public boolean isCompleto() {
		return muelles.size() == getNumeroMuellesSinEspacio();
	}
	
	
	/**
     * Agrega un muelle al puerto actual.
     * @param muelle El muelle a agregar.
     * @throws IllegalArgumentException Si el muelle es null.
     * @throws IllegalArgumentException Si el muelle ya existe en el puerto.
     */
	public void agregaMuelle(Muelle muelle) {
		if (muelle == null) throw new IllegalArgumentException();
		if (isMuelleEnPuerto(muelle.getIdMuelle())) throw new IllegalArgumentException();
		
		muelles.add(muelle);
	}
	
	
	/**
	 * Elimina un muelle del puerto a partir de su identificador.
	 * @param idMuelle El identificador del muelle a eliminar. No puede ser null.
	 * Debe exisitr un muelle con ese identificador en el puerto.
	 * @throws IllegalArgumentException si {@code idMuelle == null}.
     * @throws IllegalArgumentException Si el muelle no esta en el puerto.
     */
	public void eliminaMuelle(String idMuelle) {
		if (idMuelle == null) throw new IllegalArgumentException();
		boolean eliminado = false;
		for (int i = 0; i < muelles.size(); i++) {
			if (muelles.get(i).getIdMuelle().equals(idMuelle)) {
				muelles.remove(i);
				eliminado = true;
			}
		}
		if (!eliminado) throw new IllegalStateException();
	}
	
	
	/**
     * Elimina todos los muelles del puerto actual.
     */
	public void eliminaTodosLosMuelles() {
		setMuelles();
	}
	
	
	/**
     * Verifica si existe un muelle a partir de su identificador en el puerto.
     * @param idMuelle El identificador del muelle.
     * @return true si el muelle esta en el puerto, false en caso contrario.
     * @throws IllegalArgumentException si el id es null.
     */
	public boolean isMuelleEnPuerto(String idMuelle) {
		if (idMuelle == null) throw new IllegalArgumentException();
		for (Muelle muelle : muelles) {
			if (muelle.getIdMuelle().equals(idMuelle)) {
				return true;
			}
		}
		return false;
	}
	
	
	/**
     * Compara si dos puertos son iguales, es decir si tienen mismo identificador.
     * @param puerto El puerto a comparar.
     * @return true si los puertos son iguales, false en caso contrario.
     * @throws IllegalArgumentException si {@code puerto == null}.
     */
	@Override
	public boolean equals(Object puerto) {
		if (puerto == null) throw new IllegalArgumentException();
		Puerto otro = (Puerto) puerto;
		return getIdentificador().equals(otro.getIdentificador());
	}
	
	
	/**
     * Obtiene una lista de muelles dentro de un radio de distancia especificado
     * desde un punto GPS determinado.
     * @param puntoComparacion El punto GPS de referencia. No puede ser null.
     * @param distancia El radio de distancia desde el punto de referencia. Tiene que
     * ser mayor o igual que cero.
     * @return ArrayList Una lista de muelles que se encuentran dentro de la distancia
     * especificada.
     * @throws IllegalArgumentException si {@code puntoComparacion == null}.
     * @throws IllegalArgumentException si {@code distancia < 0}.
     */
	public ArrayList<Muelle> muellesADistanciaInferior(GPSCoordinate puntoComparacion, double distancia) {
		if (distancia < 0) throw new IllegalArgumentException();
		ArrayList<Muelle> muellesDistanciaInferior = new ArrayList<>(0);
		double distanciaHaciaMuelle;
		Muelle muelleInferior;
		
		for (Muelle muelle : muelles) {
			muelleInferior = muelle.clone();
			distanciaHaciaMuelle = muelleInferior.getPuntoGPS().getDistanceTo(puntoComparacion);
			
			if (distanciaHaciaMuelle <= distancia) {
				muellesDistanciaInferior.add(muelleInferior);
			}
		}
		return muellesDistanciaInferior;
	}
	

	/**
	 * Consulta si un contenedor se encuentra en el puerto.
	 * @param idContenedor identificador del contenedor del cual se quiere saber
	 * si se encuentra en el puerto. No puede ser null.
	 * @return true en caso de que exista un contenedor con dicho identificador,
	 * false en caso contrario.
	 * @throws IllegalArgumentException si {@code idContenedor == null}
	 */
	public boolean estaContenedor(String idContenedor) {
		if (idContenedor == null) throw new IllegalArgumentException();
		for (Muelle muelle: muelles) {
			if (muelle.estaContenedor(idContenedor)) return true;
		}
		return false;
	}
	

	/**
	 * Agrega un  contenedor en el puerto. No puede encontrarse ya un contenedor
	 * igual en alguno de sus muelles y solo se puede agregar si hay espacio en algún
	 * muelle para dicho contenedor.
	 * @param contenedor contenedor que se quiere añadir, no puede ser null.
	 * @throws IllegalArgumentException si {@code contenedor == null}
	 * @throws IllegalStateException si ya existe ese contenedor en el puerto.
	 * @throws IllegalStateException si el puerto no esta activo.
	 * @throws IllegalStateException si los muelles activos no tienen espacio para agregar
	 * un nuevo contenedor.
	 */
	public void agregarContenedor(Contenedor contenedor) {
		if (contenedor == null) throw new IllegalArgumentException();
		if (estaContenedor(contenedor.getIdentificador())) throw new IllegalStateException();
		int i = 0;
		Muelle muelleConEspacio = null;
		while((muelleConEspacio == null) && (i < muelles.size())) {
			if(muelles.get(i).tieneEspacio() && muelles.get(i).isOperativo()) {
				muelleConEspacio = muelles.get(i);
				muelleConEspacio.asignarContenedorAPlaza(contenedor);
			} else {
				i++;
			}
		}
		if (muelleConEspacio == null) {
			throw new IllegalStateException();
		}
	}
	
	
	/**
	 * Elimina un contenedor del puerto. Debe encontrarse en algunos de los muelles
	 * del puerto. 
	 * Si el puerto no esta activo, no se puede quitar el contenedor.
	 * Si el puerto esta activo y contenedor esta en un muelle del puerto, 
	 * este muelle debe estar operativo para poder sacar el contenedpr.
	 * @param idContenedor identificador del contenedor que se quiere eliminar. No
	 * puede ser null.
	 * @throws IllegalArgumentException si {@code idContenedor == null}
	 * @throws IllegalStateException si no se encuentra ningún contenedor
	 * con dicho identificador en el puerto.
	 */
	public void quitarContenedor(String idContenedor) {
		if (idContenedor == null) throw new IllegalArgumentException();
		
		int i = 0;
		boolean encontrado = false;
		Muelle muelleADesapilar = null;
		
		while((i < muelles.size()) && !encontrado) {
			muelleADesapilar = muelles.get(i);
			if (muelleADesapilar.estaContenedor(idContenedor)
					&& muelleADesapilar.isOperativo()) {
				muelleADesapilar.desapilarContenedor(idContenedor);
				encontrado = true;
			} else {
				i++;
			}
		}
		
		if (!encontrado) throw new IllegalStateException();
	}
	
	
	/**
	 * Modifica el estado de un muelle a operativo.
	 * Debe existir el muelle en el puerto.
	 * @param idMuelle identificador del muelle. No puede
	 * ser null. Debe existir un muelle con dicho identificador 
	 * en el sistema.
	 * @throws IllegalArgumentException si {@code idMuelle == null}
	 * @throws IllegalStateException si no existe un muelle con identificador
	 * {@code idMuelle} en el puerto.
	 */
	public void setMuelleOperativo(String idMuelle) {
		if (idMuelle == null) throw new IllegalArgumentException();
		Muelle muelle = getMuelle(idMuelle);
		muelle.setOperativo();
	}
	
	
	/**
	 * Modifica el estado de un muelle a fuera de servicio.
	 * Debe existir el muelle en el puerto.
	 * @param idMuelle identificador del muelle. No puede
	 * ser null. Debe existir un muelle con dicho identificador 
	 * en el sistema.
	 * @throws IllegalArgumentException si {@code idMuelle == null}
	 * @throws IllegalStateException si no existe un muelle con identificador
	 * {@code idMuelle} en el puerto.
	 */
	public void setMuelleFueraServicio(String idMuelle) {
		if (idMuelle == null) throw new IllegalArgumentException();
		Muelle muelle = getMuelle(idMuelle);
		muelle.setFueraDeServicio();
	}
	
	private Muelle getMuelle(String idMuelle) {
		Muelle muelle = null;
		boolean estaMuelle = false;
		int i = 0;
		
		while ((i < muelles.size()) && !estaMuelle) {
			muelle = muelles.get(i);
			if (muelle.getIdMuelle().equals(idMuelle)) {
				estaMuelle = true;
			} else {
				i++;
			}
		}
		
		if (estaMuelle) {
			return muelle;
		} else {
			throw new IllegalStateException();
		}
	}
	

	/*
     * IllegalArgumentException Si el identificador es null.
     * IllegalArgumentException Si el identificador no cumple con el formato adecuado.
     */
	private void setIdentificador(String idPuerto) {
		String pais;
		String codigoLocalidad;
		
		if (idPuerto == null) {
			throw new IllegalArgumentException();
		}
		if (idPuerto.length() != 6) {
			throw new IllegalArgumentException();
		}
		if (idPuerto.charAt(2) != '-') throw new IllegalArgumentException();
		
		pais = idPuerto.substring(0, 2);
		compruebaCadenaDeLetras(pais);
		codigoLocalidad = idPuerto.substring(3, 6);
		compruebaCadenaDeLetras(codigoLocalidad);
		identificador = idPuerto;
		
	}
	
	/*
     * IllegalArgumentException Si la cadena contiene caracteres no validos (
     * que no sean letras mayusculas).
     */
	private void compruebaCadenaDeLetras(String cadena) {
		for (int i = 0; i < cadena.length(); i++) {
			if ((cadena.charAt(i) < 'A') || (cadena.charAt(i) > 'Z')) {
				throw new IllegalArgumentException();
			}
		}
	}


	private void setMuelles(ArrayList<Muelle> muelles) {
		this.muelles = new ArrayList<>();
		for (Muelle muelle : muelles) {
			this.muelles.add(muelle.clone());
		}
	}
	
	/**
     * Reiniciar la lista de muelles, la vacia.
     */
	private void setMuelles() {
		muelles = new ArrayList<>();
	}
	
}