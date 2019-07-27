package es.ucm.fdi.tp.visualComponents.ControlPanel;

import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.mvc.gui.GUIView;

public abstract class ControlPanelViewer<S extends GameState<S,A> , A extends GameAction<S,A>> extends GUIView<S,A> {

	private static final long serialVersionUID = -6716488561266049604L;

	public abstract boolean hasAditionalMovements();
	
	public abstract void decreaseAditionalMovements();
	
}
