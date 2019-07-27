package es.ucm.fdi.tp.mvc.common;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GameError;
import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.dessignPatterns.Mediator.ControlPanelMediator;
import es.ucm.fdi.tp.dessignPatterns.ObserverObservable.GameObservable;
import es.ucm.fdi.tp.dessignPatterns.ObserverObservable.GameObserver;
import es.ucm.fdi.tp.games.chess.ChessState;
import es.ucm.fdi.tp.games.dc.DcState;
import es.ucm.fdi.tp.games.ttt.TttState;
import es.ucm.fdi.tp.games.was.WasState;
import es.ucm.fdi.tp.mvc.gui.GameEvent;
import es.ucm.fdi.tp.mvc.gui.GameEvent.EventType;

/*
 * Motor de juego basado en eventos.
 * Mantiene un estado inicial y uno actual
 * Este motor notificará a los observadores de cualquier cambio en el juego.
 */

// GameAction y GameObservable son interfaces que definen operaciones para algun tipo que las implemente.

public class GameTable<S extends GameState<S, A>, A extends GameAction<S, A>> implements GameObservable<S, A> {

    // Estado actual, actual y estado de activacion del juego.
	private S state;
	private S initState;
	private boolean active;
	
	// Acciones validas y observadores.
	List<A> validActions;
	List<GameObserver<S, A>> obs;
	
	//***********************************************************************************************************//
	
	private ControlPanelMediator<S,A> controlPanelMediator;
	
	//***********************************************************************************************************//
	
	//***********************************************************************************************************//
	
    public GameTable(S initState) {
    	if(initState==null) throw new GameError("Initial state cannot be null");
    	this.obs = new ArrayList<GameObserver<S,A>>();
    	this.initState = initState;
    	this.state = initState;
    	this.active = false;
    }
    
    //***********************************************************************************************************//
    
    // Inicializa o reinicia el jeugo.
    public void start() {
    	active = true;
    	this.state = this.initState;
    	this.validActions = this.state.validActions(this.state.getTurn());
		notifyObservers(new GameEvent<>(EventType.Start, null, this.state, null, "Game Started!"));
    }
    
    // Detiene el juego.
    public void stop() {
    	if(this.active) {
    		this.active = false;
    		notifyObservers(new GameEvent<>(EventType.Stop, null, this.state, null, "Game Stopped!"));
    	} else {
    		GameError err = new GameError("Error");
    		notifyObservers(new GameEvent<S,A>(EventType.Stop,null,this.state,err, "Error"));  		
    		throw err;
    	}
    }
    
    // Ejecutar una acción sobre el modelo.
    public void execute(A action) {
    	
    	boolean found = false; // Flag que se activa si la accion sobre el tablero es valida.
    	
    	Iterator<A> it = this.validActions.iterator(); // Iterador que sirve para recorrer la lista de acciones validas.
		
    	// Recorremos la lista de acciones para ver si la que queremos realizar coincide con alguna.
    	
		while(!found && it.hasNext()) {
			if(action.equals(it.next())) found = true;
		}
		
		// Estamos intentando realizar una acción ilegal.
		if(!found) {
			
			notifyObservers( new GameEvent<S,A>(EventType.Info,action,this.state,null, "Invalid movement"));  
			
		} else {
    
	    	if(this.active && !this.state.isFinished() && action.getPlayerNumber() == this.state.getTurn() ) {
	    		
	    		// Aplicamos la acción sobre el estado actual y obtenemos el nuevo estado actual.
	    		this.state = action.applyTo(this.state);
	    		
	    		// Obtenemos la lista de acciones válidas sobre el tablero para aquel jugador al que corresponda el turno.
	    		this.validActions = this.state.validActions(nextTurn(action.getPlayerNumber(),this.state.getTurn()));
	    		
	    		notifyObservers(new GameEvent<S,A>(EventType.Change,action,this.state,null, "The game state has changed!"));  		
	    	
	    	} else {
	    		
	    		GameError err = new GameError("Cannot perform action");
	    		
	    		notifyObservers(new GameEvent<S,A>(EventType.Error,action,this.state,err, "Error"));  		
	    		
	    		throw err;
	    	
	    	}
	    	
		}
    	
    }
    
    // Acceso al estado del modelo.
    public S getState() {
        return this.state;
    }
    
    //**************** OBSERVADOR - OBSERVABLE ******************************************************************//
    
    private void notifyObservers(GameEvent<S,A> e) {
    	for (GameObserver<S,A> o: obs) o.notifyEvent(e);	
    }

    public void addObserver(GameObserver<S, A> o) {
        this.obs.add(o);
    }
    
    public void removeObserver(GameObserver<S, A> o) {
        this.obs.remove(o);
    }
    
    //***********************************************************************************************************//
    
    //**************** MEDIATOR *********************************************************************************//
    
    public void setControlPanelMediator(ControlPanelMediator<S,A> controlPanelMediator) {
    	
    	this.controlPanelMediator = controlPanelMediator;
    	
    }
    
    //***********************************************************************************************************//
    
    //*********** DETERMINE NEXT TURN ***************************************************************************//
    
    private int nextTurn(int lastTurn, int currentTurn) {
    	
    	// GUI MODE
		if(this.controlPanelMediator != null) {
		
			if(this.controlPanelMediator.hasMultipleMovements(lastTurn)) {
    			
				if(this.state instanceof TttState) {
					
					TttState tttState = (TttState) this.state;
					
					this.state = (S) new TttState( tttState , tttState.getBoard() , tttState.isFinished() , tttState.getWinner() );
					
				} else if(this.state instanceof WasState) {
					
					WasState wasState = (WasState) this.state;
					
					this.state = (S) new WasState( wasState , wasState.getBoard() );
					
				} else if(this.state instanceof ChessState) {
					
					ChessState chessState = (ChessState) this.state;
					
					this.state = (S) new ChessState( chessState , chessState.getBoard() , chessState.canCastle() , chessState.isEnPassant() , chessState.isInCheck() );
					
				} else if(this.state instanceof DcState) {
					
					DcState dcState = (DcState) this.state;
					
					this.state = (S) new DcState( dcState , dcState.getBoard() );
					
				}
				
    			currentTurn = lastTurn;
    			
    			this.controlPanelMediator.decreasePlayerMovements(currentTurn);
    	
    		}
			
		} 
		
		return currentTurn;
    	
    }
    
    //***********************************************************************************************************//
    
}
