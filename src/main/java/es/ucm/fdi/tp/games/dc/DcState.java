package es.ucm.fdi.tp.games.dc;

import java.util.ArrayList;
import java.util.List;
import es.ucm.fdi.tp.base.model.GameState;

/*
 * Cambiar:
 * + En la contructora hay que declarar como se 
 *   despliegan las fichas en el turno inicial.
 * + La dimensión ( DIM ) del tablero si no es 8 x 8.
 * + Elements por --> NombreJuego"Elements".
 * + BLANCAS y NEGRAS por los elementos correspondientes del juego.
 * + Supervisar el método toString para la representación por consola.
 * + Supervisar el método validActions los requisitos para el movimiento de las fichas.
 * + Supervisar el método searchWinner para supervisar bajo que condiciones gana cada jugador.
 */

/**
 * Clase que describe el estado en el que se encuentra la partida actual.
 * 
 * @author Daniel Calle Sánchez
 * @author Manuel Guerrero Moñús
 * @version 1.0 10/03/2017
 */
public class DcState extends GameState<DcState, DcAction> {

	private static final long serialVersionUID = 2188823906048337066L;
	
	/*
	 * turn: Número del turno de este estado. 
	 * winner: Indicador de victoria para un determinado jugador. 
	 * board: Tablero de juego. 
	 * finished: Indicador que nos dice si la partida ha acabado. 
	 * BOARD_SIZE: Tamaño del tablero de juego ( DIM x DIM ).
	 */
	
	final static int DIM = 6;
	
	private final int turn;
	private final int winner;
	private final int[][] board;
	private final boolean finished;
	

	public DcState() {

		// Es un juego de 2 jugadores.
		super(2);

		// Creamos un tablero de dimensión ( DIM x DIM ).
		this.board = new int[DcState.DIM][DcState.DIM];

		// Inicializamos el tablero como un tablero vacio.
		for (int i = 0; i < DcState.DIM; i++) {
			for (int j = 0; j < DcState.DIM; j++)
				this.board[i][j] = DcElements.EMPTY.getValue();
		}

		//**************** SITUACIÓN INICIAL DE LAS FICHAS AQUÍ ****************//
		
		for(int i = 0 , k = DcState.DIM - 1 ; i < DcState.DIM / 2 ; i++ , k--)
			
			for(int j = 0 , l = DcState.DIM / 2 + i ; j < DcState.DIM / 2 - i ; j++ , l++) {
				
				this.board[i][j] = DcElements.BLANCAS.getValue();
	
				this.board[k][l] = DcElements.NEGRAS.getValue();
				
			}

		//**********************************************************************//
		
		// Valor del primer turno.
		this.turn = 0;

		// No hay ningun ganador al principio de la partida.
		this.winner = -1;

		// La partida no ha acabado. Acaba de comenzar.
		this.finished = false;
	}

	public DcState(DcState prev, int[][] board) {

		// Es un juego de 2 jugadores.
		super(2);

		// Inicializamos el tablero a partir de uno existente.
		this.board = board;

		// Inicializamos el jugador que ha ganado la partida.
		this.winner = searchWinner();
		
		// Indicamos si ha ganado alguien en este turno.
		if (this.winner!=-1) this.finished = true;
		else this.finished = false;

		// Inicializamos a que jugador le corresponde jugar en este turno.
		this.turn = (prev.turn + 1) % 2;

	}
	
	
	// CONSTRUCTOR PARA HACER TEST sirve para no tener que realizar un movimiento falso del lobo
	// cuando compruebo si han ganado las ovejas debido a que el constructor comprueba si ha ganado
	// el jugador que tiene el turno y no me interesa saber si ha ganado el lobo
	public DcState(int[][] board,int turn) {

		// Es un juego de 2 jugadores.
		super(2);

		// Inicializamos el tablero a partir de uno existente.
		this.board = board;

		// Inicializamos el jugador que ha ganado la partida.
		this.winner = searchWinner();
		
		// Indicamos si ha ganado alguien en este turno.
		if (this.winner!=-1) this.finished = true;
		else this.finished = false;

		// Inicializamos a que jugador le corresponde jugar en este turno.
		this.turn = (turn + 1) % 2;

	}


	/**
	 * Método que devuelve el número del turno.
	 * @return Devuelve el turno del jugador al que le corresponde jugar.
	 */
	@Override
	public int getTurn() {
		return this.turn;
	}

	/**
	 * Método que devuelve el contenido de una determinada posición del tablero.
	 * @param row Fila del tablero.
	 * @param col Columna del tablero.
	 * @return Devuelve el contenido de una posición específica del tablero.
	 */
	public int at(int row, int col) {
		if (0 <= row && row < DIM && 0 <= col && col < DIM)
			return this.board[row][col];
		else
			return DcElements.OUTSIDE.getValue();
	}

	/**
	 * Método que devuelve una lista con las acciones posibles que puede realizar el jugador actual.
	 * @param playerNumber Número que identifica al jugador al que le toca jugar.
	 * @return Devuelve una lista con las acciones que el jugador actual puede hacer.
	 */
	@Override
	public List<DcAction> validActions(int playerNumber) {
		
		// Declaramos una listade acciones.
		ArrayList<DcAction> valid = new ArrayList<>();

		// Recorremos el tablero hasta encontrar al jugador especificado.
		for (int i = 0; i < board.length; i++) {

			for (int j = 0; j < board.length; j++) {

				// Hemos encontrado una ficha que pertenece al jugador indicado.
				if (board[i][j] == playerNumber) {

					/*
					 * Obtenemos todos los movimientos posibles que ese juegador
					 * puede hacer con una ficha que le pertenezca.
					 */
					for (DcMovements movement : DcMovements.values()) {

						// Hacemos un cálculo para obtener la nueva posición de la ficha.
						int row = i + movement.getVerticalMovement();
						int col = j + movement.getHorizontalMovement();

						int element = this.at(row, col);

						//************ AQUÍ LAS CONDICIONES NECESARIAS PARA MOVER A UNA CASILLA ************//
						
						if (element == DcElements.EMPTY.getValue()) {

							if (playerNumber == DcElements.BLANCAS.getValue()) {

								//******* AQUÍ RESTRICCIONES AL MOVIMIENTO DE LAS BLANCAS *******//
								
								valid.add(new DcAction(playerNumber, i, j, row, col));

								//***************************************************************//
								
							} else {

								//******* AQUÍ RESTRICCIONES AL MOVIMIENTO DE LAS NEGRAS ********//
								
								valid.add(new DcAction(playerNumber, i, j, row, col));

								//***************************************************************//
									
							}
						}
						
						//**********************************************************************************//
					}
				}
			}
		}

		// Devolvemos la lista de acciones del jugador correspondiente.
		return valid;

	}


	/**
	 * Método que devuelve un boolean que indica si el juego ha terminado.
	 * 
	 * @return Devuelve un booleano que indica si la partida ha terminado.
	 */
	@Override
	public boolean isFinished() {
		return this.finished;
	}

	/**
	 * Método que obtiene el jugador que ha ganado la partida.
	 * 
	 * @return Devuelve un valor que indica quien ha ganado la partida.
	 */
	@Override
	public int getWinner() {
		return this.winner;
	}

	/**
	 * Método que devuelve el estado actual del tablero.
	 * 
	 * @return Devuelve una copia del estado en el que está el tablero.
	 */
	public int[][] getBoard() {

		// Creamos una matriz ( dim x dim ).
		int[][] copy = new int[this.board.length][this.board.length];

		// Volcamos el estado el estado del tablero a nuestra copia.
		for (int i = 0; i < this.board.length; i++)
			for (int j = 0; j < this.board.length; j++)
				copy[i][j] = this.board[i][j];

		// Devolvemos la copia del tablero.
		return copy;
	}
	
	private int searchWinner() {
		
		int winner = -1;
		
		if(!finished) {

			boolean noWinner = false;
			
			for(int j = 0 ; j < DcState.DIM && !noWinner ; j++) 
				
				if(this.board[DcState.DIM - 1][j] != DcElements.BLANCAS.getValue())
					
					noWinner = true;
			
			if(!noWinner) winner = DcElements.BLANCAS.getValue();
			
			for(int j = 0 ; j < DcState.DIM && noWinner ; j++) 
				
				if(this.board[0][j] != DcElements.NEGRAS.getValue())
					
					noWinner = false;
			
			if( noWinner && winner == -1) winner = DcElements.NEGRAS.getValue();
			
		}

		return winner;

	}

	/**
	 * Método que se ocupa de la representación gráfica del tablero.
	 * 
	 * @return Devuelve en un String la representación gráfica del tablero.
	 */
	public String toString() {

		/*
		 * Cadena de caracteres que va a contener la representación gráfica del
		 * tablero.
		 */
		String boardRepresentation = "";

		// Recorremos el tablero mirando lo que hay en cada casilla.
		for (int i = 0; i < this.board.length; i++) {

			// Añadimos un borde del tablero.
			boardRepresentation += "|";

			for (int j = 0; j < this.board.length; j++) {

				if (at(i, j) == DcElements.BLANCAS.getValue()) {

					boardRepresentation += " B ";

				} else if (at(i, j) == DcElements.NEGRAS.getValue()) {

					boardRepresentation += " N ";

				} else {

					boardRepresentation += " . ";

				}

			}

			// Introducimos un salto de línea ( el correspondiente
			// al sistema anfitrión que estamos ejecutando ).
			boardRepresentation += System.getProperty("line.separator");

		}

		// Devolvemos la representación del tablero.
		return boardRepresentation;

	}

}