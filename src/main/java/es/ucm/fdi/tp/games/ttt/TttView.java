package es.ucm.fdi.tp.games.ttt;

import java.awt.Color;

import es.ucm.fdi.tp.mvc.common.PlayerMode;
import es.ucm.fdi.tp.mvc.gui.RectBoardView;
import es.ucm.fdi.tp.visualComponents.JBoard.JBoard;
import es.ucm.fdi.tp.visualComponents.JBoard.JBoard.Shape;

public class TttView extends RectBoardView<TttState, TttAction> {

	private static final long serialVersionUID = 6793467291262550857L;
	
	public TttView(TttState tttState) {
		super(tttState);
	}
	
	// Las filas y las columnas del tablero pueden ser obtenidas del último estado.
	
	protected int getNumCols() {
		return super.state.getBoard()[0].length;
	}
	
	protected int getNumRows() {
		return super.state.getBoard().length;
	}
	
	protected Integer getPosition(int row, int col) {
		return super.state.at(row, col);
	}
	
	@Override
	protected Color getBackground(int row, int col) {
		return new Color(218, 255, 179);
	}
	
	@Override
	protected int getSepPixels() {
		return 0;
	}
	
	public void enable() {
		super.enable();
	}
	
	protected void keyTyped(int keyCode) {}
	
	protected void mouseClicked(int row, int col, int clickCount, int mouseButton) {
		
		if( !this.state.isFinished() && this.active && this.gameCtrl.getPlayerMode() == PlayerMode.MANUAL ) {
			
			if( this.state.getTurn() == super.gameCtrl.getPlayerId() ) {
					TttAction action = new TttAction(this.state.getTurn(),row,col);
					this.gameCtrl.makeManualMove(action);
			} else {
				this.infoViewer.addContent("It's not your turn.");
			}
			
		}
		
	}

	@Override
	public String toString() {
		return "Tic Tac Toe";
	}

	@Override
	protected Shape getShape(int player) {
		if(player==0)
			return Shape.CROSS;
		else if(player==1)
			return Shape.CIRCLE_BORDER;
		else
			return Shape.NONE;
			
	}
	
}