package es.ucm.fdi.tp.games.dc;

/**
 * Enumerado que se utiliza para generar los movimientos del lobo.
 * @author Daniel Calle Sánchez
 * @author Manuel Guerrero Moñús
 * @version 1.0 10/08/2017
 */
public enum DcMovements {

	// Posibles movmimientos que puede hacer un jugador.
	UP(1,0),
	DOWN(-1,0),
	LEFT(0,-1),
	RIGHT(0,1),
	UP_LEFT(-1,-1),
	UP_RIGHT(-1,1),
	DOWN_RIGHT(1,1),
	DOWN_LEFT(1,-1);
	
	private int verticalMovement;
	private int horizontalMovement;
	
	/**
	 * Constructora del enumerado.
	 * @param verticalMovement: Desplazamiento en vertical.
	 * @param horizontalMovement: Desplazamiento en horizontal.
	 */
	DcMovements(int verticalMovement, int horizontalMovement) {
		this.verticalMovement = verticalMovement;
		this.horizontalMovement = horizontalMovement;
	}

	public int getVerticalMovement() {
		return verticalMovement;
	}
	
	public int getHorizontalMovement() {
		return horizontalMovement;
	}
	
}