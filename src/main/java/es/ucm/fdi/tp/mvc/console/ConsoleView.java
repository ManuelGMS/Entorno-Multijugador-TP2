package es.ucm.fdi.tp.mvc.console;

import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.dessignPatterns.ObserverObservable.GameObservable;
import es.ucm.fdi.tp.dessignPatterns.ObserverObservable.GameObserver;
import es.ucm.fdi.tp.mvc.gui.GameEvent;

/*
 * La VISTA del modo consola no es mas que un mero expectador que
 * se declara como observador del modelo y que publicará los resultados de cada jugada.
 */

public class ConsoleView<S extends GameState<S,A>, A extends GameAction<S,A>> implements GameObserver<S,A>{

	public ConsoleView(GameObservable<S,A> gameTable) {
		gameTable.addObserver(this);
	}

	// La vista recivirá una notificación del modelo, dependiendo de cual notificaremos cosas distintas.  
	@Override
	public void notifyEvent(GameEvent<S, A> e) {
		
		switch (e.getType()) {
			case Stop:
				    
				String endText = "The game ended:";
					    
				int winner = e.getState().getWinner();
				    
				if (winner == -1) 
					endText += "draw!";
				else 
					endText += "player " + winner + " won!";
				
				        
				System.out.println(endText);
				
			break;
			case Change:
				
				System.out.println("Current State:");
				System.out.println(e.getState());
				System.out.println("Turn for player" + e.getState().getTurn());
				
			break;
			case Start:
				
				System.out.println("The game has started");
				System.out.println("Current State:");
				System.out.println(e.getState());
				System.out.println("Turn for player" + e.getState().getTurn());
				
			break;
			default:
			break;
		}
		
	}
	
}