package es.ucm.fdi.tp.visualComponents.CommonWindow;

import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.mvc.gui.GUIView;

public abstract class CommonWindowViewer <S extends GameState<S,A>, A extends GameAction<S,A>> extends GUIView<S,A> {

	private static final long serialVersionUID = 1791795576968015003L;

	public abstract void resetStadistics();
	
	public abstract void addPlayer(int playerId);
	
	public abstract void chronometerStopFor(int playerId);
	
	public abstract void chronometerStartFor(int playerId);
	
	public abstract void updateChronometerResultsFor(int playerId);

}
