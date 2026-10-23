package uva.poo.trayecto.simple;
import uva.poo.puerto.*; 
import java.time.LocalDate; 



/**
 * Representa un trayecto en barco en la compañia de transporte de mercancías MArkse-Co.
 * Extiende la clase Trayecto.
 * Los trayectos en barco tienen un coste diario fijo. Se puede calcular el coste del 
 * trayecto final.
 * 
 * @author Marta Pérez
 * @author Emily Rodrigues
 */
public class TBarco extends Simple {
	
	private static final double COSTE_POR_DIA_BASE_EUROS = 4000;
	private static final double COSTE_FIJO_BASE_EUROS = 0;
	private static final double COSTE_POR_KILOMETRO_BASE_EUROS = 0;

	
	/**
	 * Inicializa un trayecto en barco a partir de:
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
    public TBarco(Muelle origenMuelle, Muelle destinoMuelle, Puerto origenPuerto, Puerto destinoPuerto,
                  LocalDate fechaInicioTrayecto, LocalDate fechaFinTrayecto) {
        super(origenMuelle, destinoMuelle, origenPuerto, destinoPuerto, fechaInicioTrayecto, fechaFinTrayecto);
        
		setCostePorDia(COSTE_POR_DIA_BASE_EUROS);
		setCosteFijo(COSTE_FIJO_BASE_EUROS);
		setCostePorKilometro(COSTE_POR_KILOMETRO_BASE_EUROS);
    }
    
    
    /**
     * Consulta si en el trayecto se usa barco.
     * @return true, siempre usa barco.
     */
    @Override
    public boolean usaBarco() {
        return true;
    }
    
    /**
     * Consulta si en el trayecto se usa tren.
     * @return false, nunca usa tren.
     */
    @Override
    public boolean usaTren() {
        return false;
    }
    
    
    /**
     * Crea un nuevo trayecto TBarco a modo de copia.
     * @return la copia del TBarco
     */
    @Override
    public TBarco clone() {
    	return (TBarco) super.clone();
    }


    /**
     * Consulta el coste por dia inicial de un trayecto simple, antes
     * de los cambios en el mercado.
     * @return coste por dia base, antes de los cambios.
     */
	@Override
	public double costePorDiaBase() {
		return COSTE_POR_DIA_BASE_EUROS;
	}


	 /**
     * Consulta el coste por kilometro inicial de un trayecto simple, antes
     * de los cambios en el mercado.
     * @return coste por kilometro base, antes de los cambios.
     */
	@Override
	public double costePorKilometroBase() {
		return COSTE_POR_KILOMETRO_BASE_EUROS;
	}


	/**
     * Consulta el coste fijo inicial de un trayecto simple, antes
     * de los cambios en el mercado.
     * @return coste fijo base, antes de los cambios.
     */
	@Override
	public double costeFijoBase() {
		return COSTE_FIJO_BASE_EUROS;
	}
}
