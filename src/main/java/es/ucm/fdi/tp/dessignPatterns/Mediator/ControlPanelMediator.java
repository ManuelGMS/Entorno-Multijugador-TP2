package es.ucm.fdi.tp.dessignPatterns.Mediator;

import java.util.ArrayList;
import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.visualComponents.ControlPanel.ControlPanelViewer;

public class ControlPanelMediator<S extends GameState<S,A>,A extends GameAction<S,A>> {

	private ArrayList<ControlPanelViewer<S,A>> controlPanels;
	
	public ControlPanelMediator() {
		
		this.controlPanels = new ArrayList<>();
		
	}

	public void addControlPanel(ControlPanelViewer<S,A> controlPanel) {
		
		this.controlPanels.add(controlPanel);
		
	}
	
	public boolean hasMultipleMovements(int playerId) {
		
		return (this.controlPanels.get(playerId).hasAditionalMovements())? true : false;
		
	}
	
	public void decreasePlayerMovements(int playerId) {
		
		this.controlPanels.get(playerId).decreaseAditionalMovements();
		
	}
	
}
