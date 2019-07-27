package es.ucm.fdi.tp.games.was;

import java.awt.Color;

import es.ucm.fdi.tp.mvc.common.PlayerMode;
import es.ucm.fdi.tp.mvc.gui.RectBoardView;
import es.ucm.fdi.tp.visualComponents.JBoard.JBoard;
import es.ucm.fdi.tp.visualComponents.JBoard.JBoard.Shape;

public class WasView extends RectBoardView<WasState, WasAction> {
	
	private static final long serialVersionUID = -4334481511799170808L;
	
	private boolean help;
	private int initialCol;
	private int initialRow;
	private boolean firstClick;
	
	public WasView(WasState wasState) {
		super(wasState);
		this.help = false;
		this.firstClick = false;
	}
	
	protected int getNumCols() {
		return super.state.getBoard()[0].length;
	}
	
	protected int getNumRows() {
		return super.state.getBoard().length;
	}
	
	protected Integer getPosition(int row, int col) {
		return super.state.at(row,col);
	}
	
	@Override
	protected Color getBackground(int row, int col) {
		return ((row%2==0&&col%2==0)||(row%2==1&&col%2==1))? Color.LIGHT_GRAY : Color.BLACK; 
	}
	
	@Override
	protected int getSepPixels() {
		return 2;
	}
	
	
	@Override
	protected void keyTyped(int keyCode) {
	
		if( this.firstClick ) {
			if(keyCode == 27) {
				this.firstClick = false;
				this.selectPiece( this.initialRow, this.initialCol, Color.BLACK);
				this.infoViewer.addContent("Selection canceled.");
			} else if(keyCode == 'h') {
				Color color;
				if(!this.help) {
					color = Color.GREEN;
					this.help = true;
				} else {
					color = Color.BLACK;
					this.help = false;
				}
				for(WasAction i : validActions) {
					if(this.initialRow == i.getFromRow() && this.initialCol == i.getFromCol())
						this.markCell(i.getToRow(), i.getToCol(), color);
				}
			}
			
		}
		
	}
	
	@Override
	public void enable() {
		super.enable();
		this.firstClick = false;
		if(this.gameCtrl.getPlayerMode()==PlayerMode.MANUAL)
			this.infoViewer.addContent("Click on source cell");
	}
	
	@Override
	public void disable() {
		super.enable();
		this.firstClick = false;
	}
	
	@Override
	protected void mouseClicked(int row, int col, int clickCount, int mouseButton) {
	
		if( !this.state.isFinished() && this.active && this.gameCtrl.getPlayerMode() == PlayerMode.MANUAL ) {
		
			int playerId = super.gameCtrl.getPlayerId();
				
			if( playerId == this.state.getTurn() )
			{
					
				if(!this.firstClick) {
					if(playerId == super.state.at(row, col) && playerId == super.state.getTurn()) {
						this.initialCol = col;
						this.initialRow = row;
						this.firstClick = true;
						this.infoViewer.addContent("Selected("+row+","+col+
								"). Click on destination cell or ESC to cancel selection.");
						this.infoViewer.addContent("Press 'h' to see posible movements");
	
						this.selectPiece(row, col, Color.GREEN);
					}
				} else {
					this.firstClick = false;
					this.help = false;
					WasAction action = new WasAction(this.state.getTurn(), initialRow, initialCol, row, col);
					this.gameCtrl.makeManualMove(action);
				}
					
				
			} else
				this.infoViewer.addContent("It's not your turn.");
		
		}
			
	}
	
	@Override
	public String toString() {
		return "Wolf and Sheeps";
	}

	@Override
	protected Shape getShape(int player) {
		if( player >= 0)
			return Shape.CIRCLE;
		else
			return Shape.NONE;
	}

}
