package uva.poo.puerto;
import java.util.ArrayList;
import java.util.HashSet;

import uva.poo.contenedor.Contenedor;
import es.uva.inf.poo.maps.GPSCoordinate; 


/**
 * Clase que proporcionara las funcionalidades requeridas 
 * para manejar contenedores un Muelle.
 * @author Marta Pérez
 * @author Emily Rodrigues
 */
public class Muelle implements Cloneable {
	
	private String idMuelle;
    private GPSCoordinate puntoGPS;
    private EstadoMuelle estado;
    private int numeroPlazas;
    private int cantidadMaxContenedores;
    private ArrayList<PlazaMuelle> plazas; 
    private ArrayList<TipoTransporte> transportesPermitidos;
    

    /**
     * Constructor que crea un muelle con id, punto GPS, estado, 
     * numero de plazas, plazas cantidad maxima de contenedores y contenedores.
     * @param idMuelle String identificador del muelle
     * @param puntoGPS GPSCoordinate del muelle
     * @param estado EstadoMuelle que puede ser operativo o fuera de servicio
     * @param numeroPlazas int de las plazas que tiene el muelle
     * @param cantidadMaxContenedores int numero de contenedores maximos que puede haber en
     * el muelle. 
     * @param transportesAceptados establece los transportes aceptados por el muelle.
     * No puede ser null. Solo se permite que la lista almacene tipo de transportes sin 
     * repetir y ninguno de sus eleemntos puede ser null.
     * @throws IllegalArgumentException si puntoGPS, estado o transportesAceptados son null.
     * @throws IllegalArgumentException si el numero de plazas o cantidad maxima de 
     * contenedores son negativos.
     * @throws IllegalArgumentException si la lista transportesAceptados esta vacia o 
     * tiene mas de 3 elementos. 
     * @throws IllegalArgumentException si transportesAceptados contiene null.
     * @throws IllegalArgumentException si hay elemento de TipoTransporte repetidos en 
     * la lista transportesAceptados.
     */
    public Muelle(String idMuelle, GPSCoordinate puntoGPS, EstadoMuelle estado, 
    		int numeroPlazas, int cantidadMaxContenedores, ArrayList<TipoTransporte> transportesAceptados) {
        
        if (numeroPlazas < 0 || cantidadMaxContenedores < 0) {
            throw new IllegalArgumentException("El numero de plazas y la cantidad "
            		+ "maxima de contenedores no pueden ser negativos.");
        }
        setIdMuelle(idMuelle);
        setPuntoGPS(puntoGPS);
        setEstado(estado);
        setNumeroPlazas(numeroPlazas);
        setCantidadMaxContenedores(cantidadMaxContenedores);
        creaPlazasMuelle();
        setTransportesPermitidos(transportesAceptados);
    }


	/**
	 * Obtiene el identificador del muelle actual.
	 * @return String identificador del Muelle.
	 */
    public String getIdMuelle() {
        return idMuelle;
    }

    /**
     * Obtiene el punto GPS del muelle actual.
     * @return GPSCoordinate del punto GPS del muelle.
     */
    public GPSCoordinate getPuntoGPS() {
        return puntoGPS;
    }

    /**
     * Obtiene el estado actual del muelle.
     * @return EstadoMuelle del muelle, que puede ser: operativo o fuera de servicio.
     */
    public EstadoMuelle getEstado() {
        return estado;
    }

    /**
     * Consulta si el muelle esta operativo.
     * @return true si el muelle esta operativo, false si esta fuera de servicio.
     */
    public boolean isOperativo() {
        return getEstado() == EstadoMuelle.OPERATIVO;
    }

    /**
     * Consulta si el muelle esta fuera de servicio.
     * @return true si el muelle esta fuera de servicio, false en caso contrario.
     */
    public boolean isFueraDeServicio() {
        return getEstado() == EstadoMuelle.FUERA_DE_SERVICIO;
    }
    
    
    /**
     * Consulta si un muelle permite el transporte de contenedores
     * en tren.
     * @return true si permite el transporte de contenedores en
     * tren, false en caso contrario.
     */
    public boolean permiteTren() {
    	return transportesPermitidos.contains(TipoTransporte.TREN);
    }
    
    
    /**
     * Consulta si un muelle permite el transporte de contenedores
     * en camion.
     * @return true si permite el transporte de contenedores en
     * camion, false en caso contrario.
     */
    public boolean permiteCamion() {
    	return transportesPermitidos.contains(TipoTransporte.CAMION);
    }
    
    
    /**
     * Consulta si un muelle permite el transporte de contenedores
     * en barco.
     * @return true si permite el transporte de contenedores en
     * barco, false en caso contrario.
     */
    public boolean permiteBarco() {
    	return transportesPermitidos.contains(TipoTransporte.BARCO);
    }
    

    /**
     * Consulta la cantidad maxima de contenedores que el muelle puede almacenar.
     * @return int cantidad maxima de contenedores permitida en el muelle.
     */
    public int getCantidadMaxContenedores() {
        return cantidadMaxContenedores;
    }
    
    
    /**
     * Obtiene el numero total de plazas en el muelle.
     * @return int numero total de plazas disponibles en el muelle.
     */
    public int getNumeroPlazas() {
        return numeroPlazas;
    }
    
    /**
     * Obtiene una copia de la lista de plazas en el muelle.
     * @return ArrayList copia del ArrayList que contiene las plazas del muelle.
     */
    public ArrayList<PlazaMuelle> getPlazas() {
        return new ArrayList<>(plazas);
    }
    
    /**
     * Asigna al muelle una lista de plazas 
     * @param plazas que se quieren establecer
     * @throws IllegalArgumentException si el ArrayList de plazas es null.
     * 
     */
    public void setPlazas(ArrayList<PlazaMuelle> plazas) {
    	if(plazas==null) {
    		throw new IllegalArgumentException("Las plazan no pueden ser null.");
    	}
    	for (int i = 0; i < plazas.size(); i++) {
            PlazaMuelle plaza = plazas.get(i);
            if (Integer.parseInt(plaza.getIdentificador()) > this.numeroPlazas) {
                throw new IllegalArgumentException("ID fuera de rango");
            }
            for (int j = i + 1; j < plazas.size(); j++) {
                if (plaza.getIdentificador().equals(plazas.get(j).getIdentificador())) {
                    throw new IllegalArgumentException("Ya existe una plaza con el identificador " + plaza.getIdentificador());
                }
            }
        }
        this.plazas = plazas;  
    }

    /**
     * Establece el identificador del muelle, validandolo previamente.
     * @param idMuelle El nuevo identificador del muelle.
     * @throws IllegalArgumentException si el identificador no es valido.
     */
    private void setIdMuelle(String idMuelle) {
    	compruebaIdentificador(idMuelle);
        this.idMuelle = idMuelle;
    }

    /**
     * Establece el punto GPS del muelle.
     * @param puntoGPS El nuevo objeto GPSCoordinate que representa la ubicacion.
     * @throws IllegalArgumentException si el punto GPS es null.
     */
    private void setPuntoGPS(GPSCoordinate puntoGPS) {
    	if (puntoGPS == null) throw new IllegalArgumentException();
    	else this.puntoGPS = puntoGPS;
    }
    
    
    /*
     * Establece el estado del muelle cuando se crea.
     * @param estado estado del muelle al crearse. No puede ser null 
     * @throws IllegalArgumentException si el estado es null.
     */
    private void setEstado(EstadoMuelle estado) {
    	if (estado == null) throw new IllegalArgumentException();
    	this.estado = estado;
    }
    

    /*
     * Establece la cantidad maxima de contenedores que puede almacenar el muelle.
     * @param cantidadMaxContenedores La nueva capacidad maxima de contenedores.
     * @throws IllegalArgumentException si el valor es menor que el numero actual de contenedores.
     */
    private void setCantidadMaxContenedores(int cantidadMaxContenedores) {
        this.cantidadMaxContenedores = cantidadMaxContenedores;
    }

    /*
     * Establece el numero de plazas en el muelle.
     * @param numeroPlazas El nuevo numero de plazas en el muelle.
     * @throws IllegalArgumentException si el numero de plazas es negativo.
     */
    private void setNumeroPlazas(int numeroPlazas) {
        this.numeroPlazas = numeroPlazas;
    }
    
    
    /*
     * Establece los transportes aceptados por el muelle.
     * Solo se permite que la lista almacene tipo de transportes sin repetir y sin ser
     * null.
     * @param transportesAceptados lista de transportes permitidos. No puede ser null.
     * @throws IllegalArgumentException si transportesAceptados es null.
     * @throws IllegalArgumentException si la lista transportesAceptados esta vacia o 
     * tiene mas de 3 elementos. 
     * @throws IllegalArgumentException si transportesAceptados contiene null.
     * @throws IllegalArgumentException si hay elemento de TipoTransporte repetidos en 
     * la lista.
     */
    private void setTransportesPermitidos(ArrayList<TipoTransporte> transportesAceptados) {
		if (transportesAceptados == null) throw new IllegalArgumentException();
		if (transportesAceptados.isEmpty() || (transportesAceptados.size() > 3)) {
			throw new IllegalArgumentException();
		}
		if (transportesAceptados.contains(null)) throw new IllegalArgumentException();
		compruebaSiTransportesAceptadosNoRepiteTiposDeTransportes(transportesAceptados);
		creaCopiaDeTransportesAceptadosParaAlmacenarlos(transportesAceptados);
	}

	private void compruebaSiTransportesAceptadosNoRepiteTiposDeTransportes(ArrayList<TipoTransporte> transportesAceptados) {
    	HashSet<TipoTransporte> transportesUnicos = new HashSet<>(transportesAceptados);
	    if (transportesUnicos.size() != transportesAceptados.size()) {
	        throw new IllegalArgumentException();
	    }
    }
	
	

    private void creaCopiaDeTransportesAceptadosParaAlmacenarlos(ArrayList<TipoTransporte> transportesAceptados) {
    	transportesPermitidos = new ArrayList<>();
    	for(TipoTransporte transporte : transportesAceptados) {
    		transportesPermitidos.add(transporte);
    	}
	}
    
    
    /**
     * Cambia el estado del muelle a operativo.
     */
    public void setOperativo() {
        this.estado = EstadoMuelle.OPERATIVO;
    }

    /**
     * Cambia el estado del muelle a fuera de servicio.
     */
    public void setFueraDeServicio() {
        this.estado = EstadoMuelle.FUERA_DE_SERVICIO;
    }

    /**
     * Consulta las plazas vacias en el muelle.
     * @return lista de plazas vacias en el muelle.
     */
    public ArrayList<PlazaMuelle> getPlazasVacias() {
        ArrayList<PlazaMuelle> plazasVacias = new ArrayList<>();
        for (PlazaMuelle plaza : getPlazas()) {
            if (plaza.estaVacio()) {
                plazasVacias.add(plaza);
            }
        }
        return plazasVacias;
    }

    /**
     * Consulta las plazas llenas en el muelle.
     * @return lista de plazas llenas en el muelle.
     */
    public ArrayList<PlazaMuelle> getPlazasLlenas() {
        ArrayList<PlazaMuelle> plazasLlenas = new ArrayList<>();
        for (PlazaMuelle plaza : getPlazas()) {
            if (plaza.estaLleno()) {
                plazasLlenas.add(plaza);
            }
        }
        return plazasLlenas;
    }

    /**
     * Consulta las plazas semi-llenas en el muelle.
     * @return lista de plazas semi-llenas en el muelle.
     */
    public ArrayList<PlazaMuelle> getPlazasSemiLlenas() {
        ArrayList<PlazaMuelle> plazasSemiLlenas = new ArrayList<>();
        for (PlazaMuelle plaza : getPlazas()) {
            if (plaza.estaSemiLleno()) {
                plazasSemiLlenas.add(plaza);
            }
        }
        return plazasSemiLlenas;
    }


    /**
     * Comprueba si un determinado contenedor esta en el Muelle a partir de su identificador
     * @param idContenedor String identificador del contenedor. No puede ser null.
     * @return True si esta en contenedor, False en caso contrario
     * @throws IllegalArgumentException si {@code idContenedor == null}
     */
    public boolean estaContenedor(String idContenedor) {
    	if (idContenedor == null) throw new IllegalArgumentException();
    	for (PlazaMuelle plaza : plazas) {
    		if (plaza.estaContenedorPorId(idContenedor)) return true;
    	}
    	return false;
    }

    /**
     * Devuelve la Plaza en la cual esta determinado contenedor a partir de su id.
     * @param idContenedor del contenedor del cual se desea saber su plaza.
     * @return PlazaMuelle plaza en la cual se encuentra el contenedor.
     * @throws IllegalArgumentException si el contenedor dado no esta en el muelle.
     */
    public PlazaMuelle getPlazaDelContenedor(String idContenedor) {
    	for (int i = 0; i < getPlazas().size(); i++) {
            if (getPlazas().get(i).estaContenedorPorId(idContenedor)) {
                return getPlazas().get(i);
            }
        }
		throw new IllegalArgumentException("El contenedor no se encuentra en ninguna plaza del muelle.");
    }

    /**
     * Consulta el nivel en el que se encuentra un contenedor especifico en el muelle, a partir de su identificador
     * @param idContenedor String identificador del contenedor
     * @return el nivel en el que esta el contenedor
     * @throws IllegalStateException si se produce un error al intentar acceder o modificar el estado de la plaza.
     */
    public int getNivelApiladoDelContenedor(String idContenedor) {
        PlazaMuelle plaza = getPlazaDelContenedor(idContenedor);
        return plaza.getContenedores().indexOf(obtenerContenedorPorId(idContenedor));
    }

    /**
     * Pone un contenedor especifico en una plaza del muelle actual
     * @param contenedor que se quiere asignar a una plaza
     * @throws IllegalStateException si no hay espacio para poner el contenedor en ninguna plaza del muelle.
     * @throws IllegalArgumentException si el contenedor es null.
     * @throws IllegalStateException si no esta operativo
     * @throws IllegalStateException si ya estaba el contenedor en el muelle
     */
    public void asignarContenedorAPlaza(Contenedor contenedor) {
    	if (contenedor == null) throw new IllegalArgumentException();
    	if (isFueraDeServicio()) throw new IllegalStateException();
    	if (estaContenedor(contenedor.getIdentificador())) {
    		throw new IllegalStateException();
    	}
    	for (PlazaMuelle plaza : plazas) {
            if (plaza.sePuedeApilar()) {
                plaza.agregarContenedor(contenedor);
                return;
            }
        }

        throw new IllegalStateException("No hay plazas disponibles para el contenedor.");

    }
    

    /**
     * Elimina un contenedor dado por su identificador de la plaza del muelle en la que este.
     * @param idContenedor String identificador del contenedor que se quiere desapilar.
     * @throws IllegalArgumentException si el identificador del contenedor es nulo.
     * @throws IllegalArgumentException si el contenedor no se encuentra en el muelle.
     * @throws IllegalStateException si se produce un error al intentar acceder o modificar el estado de la plaza.
     */
    public void desapilarContenedor(String idContenedor) {
    	if (isFueraDeServicio()) throw new IllegalStateException();
    	PlazaMuelle plaza = getPlazaDelContenedor(idContenedor);
        Contenedor contenedor = obtenerContenedorPorId(idContenedor);
        
        plaza.quitarContenedor(contenedor);
    }

    /**
     * Vacia el muelle, elimina todos los contenedores de cada plaza del muelle.
     */
    public void vaciar() {
    	for (int i = 0; i < getPlazas().size(); i++) {
            getPlazas().get(i).vaciar();
        }
    }
    
    /**
     * Consulta si existe la plaza dada por su identificador en el muelle.
     * @param plaza String con el identificador de la plaza.
     * @return True si existe la plaza en el muelle, False en caso contrario.
     * @throws IllegalArgumentException si el id de la plaza es null
     */
    public boolean existeLaPlaza(String plaza){
    	if(plaza == null) {
    		throw new IllegalArgumentException("El identificador de la plaza no puede ser null.");
    	}
    	for(int i = 0; i < getPlazas().size(); i++) {
    		if(getPlazas().get(i).getIdentificador().equals(plaza)){
    			return true;
    		}
    	}
    	return false;
    }
    
    /**
     * Consulta si la plaza dada por su identificador tiene espacio libre.
     * @param idPlaza String con el identificador de la plaza.
     * @return True si la plaza indicada tiene espacio, False en caso contrario.
     * @throws IllegalArgumentException si el identificador de la plaza es null.
     */
    public boolean tieneEspacioLaPlaza(String idPlaza) {
    	PlazaMuelle plaza = obtenerPlazaPorId(idPlaza);
        return plaza.estaVacio() || plaza.estaSemiLleno();
    }
    
    /**
     * Consulta si es muelle actual tiene espacio disponible en alguna plaza
     * @return True si al menos una plaza tiene espacio, False en caso contrario.
     */
    public boolean tieneEspacio() {
        for (int i = 0; i < getPlazas().size(); i++) {
            if (getPlazas().get(i).estaVacio() || getPlazas().get(i).estaSemiLleno()) { 
                return true;
            }
        }
        return false;
    }
    
    /**
     * Obtiene una plaza del muelle a partir de su identificador.
     * @param idPlaza identificador de la plaza a buscar.
     * @return PlazaMuelle la plaza con el identificador dado o null si no se encuentra.
     * @throws IllegalArgumentException si idPlaza es null.
     * @throws IllegalStateException si no hay ninguna plaza con idPlaza.
     */
    public PlazaMuelle obtenerPlazaPorId(String idPlaza) {
        if (idPlaza == null) {
            throw new IllegalArgumentException("idPlaza no puede ser null.");
        }
        for (int i = 0; i < getPlazas().size(); i++) {
            if (getPlazas().get(i).getIdentificador().equals(idPlaza)) {
                return getPlazas().get(i);
            }
        }
        throw new IllegalStateException("No existe la plaza con ese identificador.");
    }
    
    /**
     * Obtiene un contenedor en el muelle a partir de su identificador.
     *
     * @param idContenedor Identificador del contenedor a buscar.
     * @return El contenedor con el identificador dado o null si no se encuentra.
     * @throws IllegalStateException si no hay contenedor con idContenedor
     */
    public Contenedor obtenerContenedorPorId(String idContenedor) {
    	for (int i = 0; i < getPlazas().size(); i++) {
            for (int j = 0; j < getPlazas().get(i).getContenedores().size(); j++) {
                if (getPlazas().get(i).getContenedores().get(j).getIdentificador().equals(idContenedor)) {
                    return getPlazas().get(i).getContenedores().get(j);
                }
            }
        }
        throw new IllegalStateException("No se ha encontrado ningun contenedor con ese identificador.");
    }

    /**
     * Consulta si un Muelle es igual a otro, es decir, si tienen el mismo ID, el mismo punto GPS, 
     * el mismo estado, el mismo numero de plazas, la misma cantidad maxima de contenedores, 
     * las mismas plazas y las mismas listas de contenedores en esas plazas.
     * 
     * @param otro objeto a comparar.
     * @throws IllegalArgumentException si el objeto a comparar es null.
     * @return True si son iguales, False en caso contrario.
     */
    @Override
    public boolean equals(Object otro) {
        if (otro == null) {
            throw new IllegalArgumentException("El objeto a comparar no puede ser null.");
        }
        Muelle otroMuelle = (Muelle) otro;
        boolean sonIgualesAtributos = getIdMuelle().equals(otroMuelle.getIdMuelle()) && 
                                      getPuntoGPS().equals(otroMuelle.getPuntoGPS()) &&
                                      getEstado().equals(otroMuelle.getEstado()) &&
                                      getNumeroPlazas() == otroMuelle.getNumeroPlazas() &&
                                      getCantidadMaxContenedores() == otroMuelle.getCantidadMaxContenedores();
        boolean sonIgualesPlazas = getPlazas() != null && otroMuelle.getPlazas() != null && 
                                   getPlazas().size() == otroMuelle.getPlazas().size();
        if (sonIgualesPlazas) {
            for (int i = 0; i < getPlazas().size(); i++) {
                PlazaMuelle plaza1 = getPlazas().get(i);
                PlazaMuelle plaza2 = otroMuelle.getPlazas().get(i);
                if (!plaza1.equals(plaza2)) {
                    return false;
                }
            }
        }
        return sonIgualesAtributos && sonIgualesPlazas;
    }

    
    /**
     * Crea un nuevo muelle a modo de copia.
     * @return la copia del muelle
     */
    @Override
    public Muelle clone() {
    	Muelle copia = null;
		try {
			copia = (Muelle) super.clone();
			copia.puntoGPS = this.puntoGPS;
	        copia.plazas = new ArrayList<>();
	        for (PlazaMuelle plaza : this.plazas) {
	            copia.plazas.add(plaza.clone());
	        }
	        copia.idMuelle = this.idMuelle;
	        copia.estado = this.estado; 
	        copia.numeroPlazas = this.numeroPlazas;
	        copia.cantidadMaxContenedores = this.cantidadMaxContenedores;

		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return copia;
    }
    

    /**
     *  Genera una cadena con las caracteristicas de la PlazaMuelle actual.
     *  @return String con el ID del Muelle, su estado, sus plazas, el maximo de contenedores y la lista de Contenedores.
     */
    @Override
    public String toString() {
        return "Muelle ID: " + getIdMuelle() + ", Estado: " + getEstado() + ", Plazas: " 
        		+ getNumeroPlazas() + ", Max Contenedores: " + getCantidadMaxContenedores() + "\n";
    }
    
    
    /*
     * Comprueba que el identificador del muelle sea valido: que sea una cadena de dos digitos.
     *
     * @param idMuelle El identificador del muelle a comprobar.
     * @throws IllegalArgumentException Si el identificador no es valido 
     * 		(es null, no tiene longitud 2, no es numerico o hay otro Muelle con ese id).
     */
    private void compruebaIdentificador(String idMuelle) {
        if (idMuelle == null) {
            throw new IllegalArgumentException("El identificador no puede ser null.");
        }
        if (idMuelle.length() != 2) {
            throw new IllegalArgumentException("El identificador debe tener una longitud de 2 caracteres.");
        }
        if (!Character.isDigit(idMuelle.charAt(0)) || !Character.isDigit(idMuelle.charAt(1))) {
            throw new IllegalArgumentException("El identificador debe contener solo digitos.");
        }

    }
    
    
    /*
     * A partir de los valores validados de numeroPlazas y cantidadMaxContenedores
     * crea las plazas del Muelle.
     * Cada PlazaMuelle tiene la misma capacidad de almacenaje de contenedores.
     */
    private void creaPlazasMuelle() {
    	this.plazas = new ArrayList<>(getNumeroPlazas());
    	int numeroContenedoresPorPlaza = getCantidadMaxContenedores() / getNumeroPlazas();
        for (int i = 0; i < getNumeroPlazas(); i++) {
            this.plazas.add(new PlazaMuelle("" + i, numeroContenedoresPorPlaza));
        }
    }
	
}
