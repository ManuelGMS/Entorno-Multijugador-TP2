package es.ucm.fdi.tp.dessignPatterns.Mediator;

import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.visualComponents.CommonWindow.CommonWindowComp;
import es.ucm.fdi.tp.visualComponents.CommonWindow.CommonWindowViewer;

public class CommonWindowMediator<S extends GameState<S,A>,A extends GameAction<S,A>> {

	private CommonWindowViewer<S,A> commonWindowViewer;
	
	public CommonWindowMediator() {
		
		this.commonWindowViewer = new CommonWindowComp<>();
		
	}

	public void commonWindowAddClient(int playerId) {
		
		this.commonWindowViewer.addPlayer(playerId);
	
	}
	
	public void commonWindowShow() {
		
		this.commonWindowViewer.enableWindowMode();
		
	}
	
	public void commonWindowInitalize(int playerId) {
		
		this.commonWindowViewer.resetStadistics();
		
		this.commonWindowViewer.chronometerStartFor(playerId);
		
	}
	
	public void commonWindowChronometerStartFor(int playerId) {
		
		this.commonWindowViewer.chronometerStartFor(playerId);
		
	}
	
	public void commonWindowChronometerStopAndPublishFor(int playerId) {
		
		this.commonWindowViewer.chronometerStopFor(playerId);
		
		this.commonWindowViewer.updateChronometerResultsFor(playerId);
		
	}
	
}
