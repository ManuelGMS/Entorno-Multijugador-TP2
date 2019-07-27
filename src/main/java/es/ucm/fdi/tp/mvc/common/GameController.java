package es.ucm.fdi.tp.mvc.common;

import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GameState;

public interface GameController < S extends GameState<S,A> , A extends GameAction<S,A> > {
	public void run();
	public void stopGame();
	public void startGame();
	public void restartGame();
	public void makeManualMove(A a);
}
