package es.ucm.fdi.tp.games.was;

import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GameState;

/**
 * Clase que aplica una acción ( jugada / movimiento ) al estado
 * actual de la partida y nos permite transicionar a un nuevo estado.
 * @author Daniel Calle Sánchez
 * @author Manuel Guerrero Moñús.
 * @version 1.0 10/03/2017
 */
public class WasAction implements GameAction<WasState, WasAction> {

	private static final long serialVersionUID = -1093636471718502075L;
	
	/**
	 * Atributos de la clase.
	 * toRow: Fila destino del movimiento.
	 * toCol: Columna destino del movimiento.
	 * player: Jugador que acomete la acción.
	 * fromRow: Fila origen del movimiento.
	 * fromCol: Columna origen del movimiento.
	 */
	private int toRow;
    private int toCol;
    private int player;
    private int fromRow;
    private int fromCol;
    
    /**
     * Constructora de la clase, se inicializa indicando el jugador al que pertenece
     * el turno y el movimiento que va a realizar desde una posición de origen hasta
     * la posición de destino.
     * @param player Numero de jugador al que le corresponde efectuar el movimiento.
     * @param fromRow Fila en la que se inicia el movimiento.
     * @param fromCol Columna en la que se inicia el movimiento.
     * @param toRow Fila en la que acaba el movimiento.
     * @param ToCol Columna en la que  aaba el movimiento.
     */
	public WasAction(int player, int fromRow, int fromCol, int toRow, int toCol) {
		this.toRow = toRow;
        this.toCol = toCol;
        this.player = player;
        this.fromRow = fromRow;
        this.fromCol = fromCol;
    }
	
	/**
	 * Método que devuelve el número del jugador actual.
	 * @return Numero del jugador del turno en curso.
	 */
	@Override
	public int getPlayerNumber() {
		return this.player;
	}

	/** 
	 * Método que devuelve el estado siguiente, que es el resultado de aplicar
	 * una acción al estado actual.
	 * @param state Recive el estado actual.
	 * @return Devuelve el estado siguiente.
	 */
	@Override
	public WasState applyTo(WasState state) {
		
		// Comprobamos que le toque realizar la jugada el jugador correspondiente.
		if (this.player != state.getTurn()) {
			throw new IllegalArgumentException("Not the turn of this player.");
        }

		// Cargamos el tablero del estado de juego actual.
        int[][] board = state.getBoard();
        
        /*
        *	Aplicamos el movimiento al tablero.
        *	( A plicamos una acción al estado actual ).
        *	Movemos la ficha correspondiente a la casilla elegida, por
        *	lo tanto la anterior queda vacía.
        */
        board[this.toRow][this.toCol] = board[this.fromRow][this.fromCol];
        board[this.fromRow][this.fromCol] = WasElements.EMPTY.getValue();
        
        /*
        *	Actualizamos el estado actual para pasar al siguiente.
        *	WasState = WsasState( WasState , int[][] , boolean , int )
        *	EstadoNuevo = WasState( EstadoActual , EstadoTablero , JuegoFinalizado , TurnoDelJugador 0/1 )
        */
        WasState next = new WasState(state,board);
        
        /*
        *	Devolvemos el que será el estado siguiente, que es el mismo
        *	que el estado en curso pero habiéndose actualizado con la acción.
        */
        return next;

	}
	
	/**
	 * Método que obtiene el valor de la fila origen del tablero.
	 * @return Valor de la fila origen del tablero.
	 */
	public int getFromRow() {
        return this.fromRow;
    }

	/**
	 * Método que obtiene el valor de la columna origen del tablero.
	 * @return Valor de la columna origen del tablero.
	 */
    public int getFromCol() {
        return this.fromCol;
    }

	/**
	 * Método que obtiene el valor de la fila destino del tablero.
	 * @return Valor de la fila destino del tablero.
	 */
	public int getToRow() {
        return this.toRow;
    }

	/**
	 * Método que obtiene el valor de la columna destino del tablero.
	 * @return Valor de la columna destino del tablero.
	 */
    public int getToCol() {
        return this.toCol;
    }
    
    /**
     * El modelo llamará aquí para ver si esta acción que quiero realizar
     * se corresponde con alguna de la lista de acciones validas.
     */
    @Override
	public boolean equals(Object action) {
    	
    	if(action instanceof WasAction) {
			WasAction wasAction = (WasAction) action;
			return ( 
				this.fromRow == wasAction.fromRow && this.toRow == wasAction.toRow 
				&&
				this.fromCol == wasAction.fromCol && this.toCol == wasAction.toCol
				)? true : false;
		}
    	
    	return false;
    	
	}
    
    /**
     * Método que muestra por pantalla la acción realizada.
     * @return Devuelve un String que describe la acción realizada.
     */
    @Override
    public String toString() {
    	return "Player: " + this.player + 
    		   " from (" + this.fromRow + ", " + this.fromCol + ")" +
    		   " to (" + this.toRow   + ", " + this.toCol   + ")";
    }

}