package es.ucm.fdi.tp.games.dc;

/**
 * Elementos del juego.
 * @author Daniel Calle
 * @author Manuel Guerrero
 * @version 1.0 10/08/2017
 */
public enum DcElements {

	// Fichas blancas.
	BLANCAS(0),
	
	// Fichas negras.
	NEGRAS(1),
	
	// Casilla vacía.
	EMPTY(-1),
	
	// Fuera tablero
	OUTSIDE(-2);
	
	/**
	 * value: valor del atributo de la constante enumerada.
	 */
	private final int value;

	/**
	 * Contructora del enumerado.
	 * @param value valor de laconstante enumerada.
	 */
	private DcElements(int value) {
		this.value = value;
	}
	
	/**
	 * Método que devuelve el valor de una cosntante enumerada.
	 * @return Devuelve el valor de una constante enumerada.
	 */
	public int getValue() { 
		return this.value; 
	}
	
}
