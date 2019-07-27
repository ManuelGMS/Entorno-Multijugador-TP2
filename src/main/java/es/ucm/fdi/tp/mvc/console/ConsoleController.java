package es.ucm.fdi.tp.mvc.console;

import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.mvc.common.GameController;

public interface ConsoleController< S extends GameState<S,A> , A extends GameAction<S,A> > extends GameController<S,A> {
	public void playGame();
}
