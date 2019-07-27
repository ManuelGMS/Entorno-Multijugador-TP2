package es.ucm.fdi.tp.mvc.gui;

import java.util.concurrent.Future;

import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.dessignPatterns.ObserverObservable.SimulationObserver;
import es.ucm.fdi.tp.dessignPatterns.ObserverObservable.SmartMoveObserver;
import es.ucm.fdi.tp.mvc.common.GameController;
import es.ucm.fdi.tp.mvc.common.PlayerMode;

public interface GUIController< S extends GameState<S,A> , A extends GameAction<S,A> > extends GameController<S,A> {
	
	public int getPlayerId();
	public void makeRandomMove();
	public void stopSmartPlayer();
	public Future<?> makeSmartMove();
	public PlayerMode getPlayerMode();
	public void handleEvent(GameEvent<S,A> e);
	public void changePlayerMode(PlayerMode p);
	public void smartPlayerTimeLimit(int timeOut);
	public void smartPlayerConcurrencyLevel(int nThreads);
	public void addSmartPlayerObserver(SmartMoveObserver o);	

	//****************** NUEVAS OPERACIONES *******************************************************//
	
	public void stopSimulation();
	
	public void addSimulationObserver(SimulationObserver o);
	
	public void makeSimulation(S initialState, int pools, int matchesByPool, int maxActions);
	
}
