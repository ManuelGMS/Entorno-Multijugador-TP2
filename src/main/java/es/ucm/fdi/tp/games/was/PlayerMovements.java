package es.ucm.fdi.tp.games.was;

/**
 * Enumerado que se utiliza para generar los movimientos del lobo.
 * @author Daniel Calle Sánchez
 * @author Manuel Guerrero Moñús
 * @version 1.0 10/08/2017
 */
public enum PlayerMovements {

	// Posibles movmimientos que puede hacer un jugador.
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
	PlayerMovements(int verticalMovement, int horizontalMovement) {
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