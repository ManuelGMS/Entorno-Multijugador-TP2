package es.ucm.fdi.tp.visualComponents.Simulation;

import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.mvc.gui.GUIView;

public abstract class SimulationViewer <S extends GameState<S,A> , A extends GameAction<S,A>> extends GUIView<S,A> {

	public SimulationViewer() {
		
	}

}
