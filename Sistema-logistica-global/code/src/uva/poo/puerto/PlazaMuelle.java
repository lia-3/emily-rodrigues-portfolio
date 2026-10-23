package uva.poo.puerto;
import java.util.ArrayList;
import uva.poo.contenedor.Contenedor;

/**
 * Clase que proporcionara las funcionalidades requeridas 
 * para manejar contenedores sobre la plaza de un muelle
 * @author Marta Pérez
 * @author Emily Rodrigues
 */


public class PlazaMuelle implements Cloneable {
	
	private String identificador;
	private int alturaMaxima;
	private ArrayList<Contenedor> listaContenedores; 
	
	/**
	 * Constructor que crea una PlazaMuelle vacia a partir 
	 * de su identificador y altura maxima.
	 * @param identificador de la plazaMuelle
	 * @param alturaMaxima maximos contenedores de la PlazaMuelle
	 * @throws IllegalArgumentException si identificador o listaContenedores es null
	 * @throws IllegalArgumentException si alturaMaxima es negativa.
	 */
	public PlazaMuelle(String identificador, int alturaMaxima) {
		if(identificador == null) {
			throw new IllegalArgumentException("this no puede tener valores nulos.");
		}
		if(alturaMaxima < 0) {
			throw new IllegalArgumentException("La altura maxima no puede ser negativa");
		}
		
		this.identificador = identificador;
		this.alturaMaxima = alturaMaxima;
		this.listaContenedores = new ArrayList<>(); 
	}
	
	
	/**
	 * Constructor que crea una PlazaMuelle a partir de su identificador, 
	 * altura maxima y la lista de contenedores que iran en ella.
	 * @param identificador de la plazaMuelle
	 * @param alturaMaxima maximos contenedores de la PlazaMuelle
	 * @param listaContenedores lista de los contenedores que estan en PlazaMuelle
	 * @throws IllegalArgumentException si identificador o listaContenedores es null
	 * @throws IllegalArgumentException si alturaMaxima es negativa o la lista de 
	 * contenedores supera la altura maxima.
	 * @throws IllegalArgumentException si la lista de Contenedores contiene contenedores 
	 * con el mismo identificador o duplicados.
	 */
	public PlazaMuelle(String identificador, int alturaMaxima, ArrayList<Contenedor> listaContenedores) {
		if(identificador == null || listaContenedores == null) {
			throw new IllegalArgumentException("this no puede tener valores nulos.");
		}
		if(alturaMaxima < 0) {
			throw new IllegalArgumentException("La altura maxima no puede ser negativa");
		}
		if(listaContenedores.size() > alturaMaxima) {
			throw new IllegalArgumentException("El tamaño de la lista de contenedores no puede ser mayor que la altura maxima");
		}
		ArrayList<String> contenedores = new ArrayList<>();
	    for (Contenedor contenedor : listaContenedores) {
	        if (contenedores.contains(contenedor.getIdentificador())) {
	            throw new IllegalArgumentException("La lista de contenedores contiene duplicados.");
	        }
	        contenedores.add(contenedor.getIdentificador());
	    }
		setIdentificador(identificador);
		setAlturaMaxima(alturaMaxima);
		setContenedores(listaContenedores);
		
	}
	
	
	/**
	 * Obtiene el identificador de la plaza actual.
	 * @return String identificador de la PlazaMuelle.
	 */
	public String getIdentificador() {
		return identificador;
	}
	
	/**
	 * Obtiene la altura maxima de la plaza actual.
	 * @return int altura maxima de la PlazaMuelle.
	 */
	public int getAlturaMaxima() {
		return alturaMaxima;
	}
	
	/**
	 * Obtiene la lista de contenedores de la plaza actual.
	 * @return ArrayList con los contenedores de la PlazaMuelle.
	 */
	public ArrayList<Contenedor> getContenedores() {
		return copiarContenedores(); 
	}
	
	/**
	 * Establece un identificador para la plaza actual.
	 * @param nuevoIdentificador a asignar
	 */
	private void setIdentificador(String nuevoIdentificador) {
		identificador = nuevoIdentificador;
	}
	
	/**
	 * Establece una nueva altura maxima para la plaza actual.
	 * @param nuevaAlturaMaxima nueva altura maxima que se va a asignar.
	 */
	private void setAlturaMaxima(int nuevaAlturaMaxima) {
		alturaMaxima = nuevaAlturaMaxima;
	}
	
	/**
	 * Establece una nueva lista de contenedores para la plaza actual.
	 * @param nuevaListaContenedores nueva lista de contenedores a asignar.
	 */
	private void setContenedores(ArrayList<Contenedor> nuevaListaContenedores) {
		listaContenedores = nuevaListaContenedores;
	}
	
	/**
	 * Añade un contenedor a la plaza
	 * @param nuevoContenedor a añadir
	 * @throws IllegalStateException si no se pueden apilar mas contenedores.
	 * @throws IllegalArgumentException si el contenedor que se quiere agregar 
	 * ya esta en la plaza.
	 */
	public void agregarContenedor(Contenedor nuevoContenedor) {
		if (!estaVacio() && !sePuedeApilar()) {
		    throw new IllegalStateException("No se puede apilar sobre este tipo de contenedor porque no tiene techo o no hay espacio.");
		}

		if (estaContenedorPorId(nuevoContenedor.getIdentificador())) {
	        throw new IllegalArgumentException("Ya existe un contenedor con este identificador en la plaza.");
	    }
		listaContenedores.add(nuevoContenedor);
	}
	
	/**
	 * Elimina un determinado contenedor de la plaza.
	 * @param contenedorAQuitar contenedor que se quiere eliminar.
	 * @throws IllegalArgumentException cuando el contenedor que se quiere quitar 
	 * no esta en la plaza.
	 */
	public void quitarContenedor(Contenedor contenedorAQuitar) {
		if(!estaContenedor(contenedorAQuitar)) {
			throw new IllegalArgumentException("No existe el contenedor que "
					+ "se quiere quitar en la lista de contenedores.");
		}
		listaContenedores.remove(contenedorAQuitar);
	}
	
	/**
	 * Consulta si el tamaño de la lista de contenedores es 0, es decir, la plaza esta vacia.
	 * @return True si la plaza esta vacia, False en caso contrario.
	 */
	public boolean estaVacio() {
		return getContenedores().isEmpty();
	}
	
	/**
	 * Consulta si la plaza esta llena, si el tamaño de la lista de contenedores 
	 * ha alcanzado la altura maxima.
	 * @return True si esta llena, False en caso contrario.
	 */
	public boolean estaLleno() {
		return getAlturaMaxima() == getContenedores().size();
	}
	
	/**
	 * Consulta si la plaza aun no esta llena pero tampoco vacia.
	 * @return True si esta semillena, False en caso contrario.
	 */
	public boolean estaSemiLleno() {
		return !getContenedores().isEmpty() && getContenedores().size() < getAlturaMaxima();
	}
	
	/**
	 * Consulta si esta un determinado contenedor en la plaza.
	 * @param contenedor que se desea saber si esta.
	 * @return True si esta en la lista de contenedores, False en caso contrario.
	 * @throws IllegalArgumentException si Contenedor es null.
	 */
	public boolean estaContenedor(Contenedor contenedor) {
		if(contenedor == null) {
			throw new IllegalArgumentException("El contenedor no puede ser nulo");
		}
		return getContenedores().contains(contenedor);
	}
	
	/**
	 * Consulta si esta un determinado contenedor en la plaza a partir de su identificador.
	 * @param contenedorId identificador del contenedor que se desea saber si esta.
	 * @return True si esta en la lista de contenedores, False en caso contrario.
	 * @throws IllegalArgumentException si el id del contenedor es null.
	 */
	public boolean estaContenedorPorId(String contenedorId) {
		if(contenedorId == null) {
			throw new IllegalArgumentException("El id del contenedor no puede ser nulo");
		}
		for(int i = 0; i<getContenedores().size(); i++) {
			if(getContenedores().get(i).getIdentificador().equals(contenedorId)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Consulta si aún pueden apilar contenedores en la plaza.
	 * @return True si aún no está lleno, el contenedor de la cima tiene techo (se puede apilar) y no se supera la altura máxima. 
	 *         False en caso contrario.
	 */
	public boolean sePuedeApilar() {
	    if (getContenedores().size() < getAlturaMaxima()) {
	        if (!estaVacio() && !getContenedores().get(getContenedores().size() - 1).tieneTecho()) {
	            return false;  
	        }
	        return true;  
	    }
	    return false;  
	}

	
	/**
	 * Elimina todos los contenedores de la PlazaMuelle
	 */
	public void vaciar() {
		listaContenedores.clear();
	}
	
	/**
	 * Añade un conjunto de contenedores a la plaza junto a los que habia originalmente.
	 * @param nuevaListaContenedores la lista de contenedores que se desea añadir.
	 * @throws IllegalArgumentException cuando no hay suficiente tamaño para añadir la lista.
	 */
	public void annadirLista(ArrayList<Contenedor> nuevaListaContenedores) {
		if(getContenedores().size() + nuevaListaContenedores.size() > getAlturaMaxima()) {
			throw new IllegalArgumentException("La nueva lista junto con la anterior "
					+ "superan la altura maxima permitida.");
		}
		listaContenedores.addAll(nuevaListaContenedores);
	}
	
	/**
	 * Genera una copia de la PlazaMuelle actual.
	 * @return PlazaMuelle con las mismas caracteristicas que el original.
	 */
	public PlazaMuelle copiarPlazaMuelle() {
		return new PlazaMuelle(getIdentificador(), getAlturaMaxima(), getContenedores());
	}
	
	/**
	 * Genera una copia de la lista de contenedores.
	 * @return Una nueva lista que contiene una copia de los contenedores actuales.
	 */
	private ArrayList<Contenedor> copiarContenedores() {
	    return new ArrayList<>(listaContenedores);
	}

	
	/**
	 * Genera una cadena con las caracteristicas de la PlazaMuelle actual.
	 * @return String con el Id de la Plaza, su altura maxima, y su lista de contenedores.
	 */
	@Override
	public String toString() {
		return "Identificador:" + getIdentificador() + "\nAltura Maxima: " 
				+ getAlturaMaxima() + "\nLista de Contenedores: " + getContenedores();
	}
	
	
	/**
     * Crea una nueva plaza de un muelle a modo de copia.
     * @return la copia de la plaza
     */
    @Override
    public PlazaMuelle clone() {
    	PlazaMuelle copia = null;
		try {
			copia = (PlazaMuelle) super.clone();
			copia.listaContenedores = new ArrayList<>();
	        for (Contenedor contenedor : this.listaContenedores) {
	            copia.listaContenedores.add(contenedor.clone());
	        }
	        copia.identificador = this.identificador; 
	        copia.alturaMaxima = this.alturaMaxima;
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return copia;
    }
    
	
	/**
	 * Consulta si una PlazaMuelle es igual a otra, es decir si tienen 
	 * mismo identificacdor, misma altura maxima y misma lista de contenedores.
	 * @param otro objeto a comparar
	 * @throws IllegalArgumentException si otro es null
	 * @return True si son iguales False en caso contrario
	 */
    @Override
	public boolean equals(Object otro) {
		if (otro == null) {
			throw new IllegalArgumentException("otro no puede ser null");
		}

		return  getIdentificador().equals(((PlazaMuelle)otro).getIdentificador())  && 
				getAlturaMaxima() == ((PlazaMuelle)otro).getAlturaMaxima() &&
				getContenedores().equals(((PlazaMuelle)otro).getContenedores());
	}
	
	
}
