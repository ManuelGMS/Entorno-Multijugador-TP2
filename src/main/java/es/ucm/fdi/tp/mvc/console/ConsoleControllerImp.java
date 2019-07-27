package es.ucm.fdi.tp.mvc.console;

import java.util.List;

import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GamePlayer;
import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.mvc.common.GameController;
import es.ucm.fdi.tp.mvc.common.GameTable;
import es.ucm.fdi.tp.mvc.gui.GameEvent;

/*
 * CONTROLLER.
 * Controlador de la aplicación para el juego por consola.
 * El controlador es el componente software 
 * que se ocupa de dirigir las acciones que han de realizarse,
 * quien las ha de realizar y en que orden.
 */

public class ConsoleControllerImp<S extends GameState<S,A>,A extends GameAction<S,A>> implements ConsoleController<S,A> {

	private boolean stopped; // Estado activo del juego.
	private GameTable<S,A> game; // El modelo.
	private List<GamePlayer> players; // Lista de juegadores. 
	
	public ConsoleControllerImp(List<GamePlayer> players, GameTable<S,A> game) {
		this.game = game;
		this.stopped = true;
		this.players = players;
	}
	
	//************************** CONSOLE CONTROLLER **************************************************************************//
	
	// Método que contiene el bucle del juego.
	@Override
	public void playGame() {

		// Solicitamos una acción al jugador correspondiente e intentamos aplicarla sobre el modelo.
		while(!this.game.getState().isFinished() && !this.stopped) {
			A action = this.players.get(this.game.getState().getTurn()).requestAction(this.game.getState());
			makeManualMove(action);
		}
		
	}
	
	//***********************************************************************************************************************//
	
	//************************** CONTROLLER CORE ****************************************************************************// 
	
	// Método que inicializa la aplicación.
	@Override
	public void run() { 
		
		startGame(); 
		playGame(); 
		stopGame(); 
		
	}
	
	// Método que pone el juego en estado de parada.
	@Override
	public void stopGame() { 
		
		this.game.stop();
		this.stopped = true; 
		
	}
	
	// Método que pone el jeugo en el estado activo.
	@Override
	public void startGame() { 
		
		this.game.start();
		this.stopped = false; 
		
	}

	@Override
	public void restartGame() {
		
		// Si el juego no ha parado, lo mandamos a un estado de parada.
		if(!game.getState().isFinished()) 
					
			this.stopGame();
				
		// Comenzar el juego desde el estado inicial.
		this.startGame();
		
	}

	@Override
	public void makeManualMove(A a) { 
		
		this.game.execute(a); 
		
	}
	
	//***********************************************************************************************************************//
	
}
