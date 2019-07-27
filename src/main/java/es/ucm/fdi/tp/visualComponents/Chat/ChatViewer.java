package es.ucm.fdi.tp.visualComponents.Chat;

import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.mvc.gui.GUIView;

public abstract class ChatViewer<S extends GameState<S,A>,A extends GameAction<S,A>> extends GUIView<S,A> {

	private static final long serialVersionUID = -6819113701739563223L;

	public abstract void getMessage(String message);
	
	public abstract void sendMessage(String message);
	
}
