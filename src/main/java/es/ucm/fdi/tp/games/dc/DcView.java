package es.ucm.fdi.tp.games.dc;

import java.awt.Color;

import es.ucm.fdi.tp.mvc.common.PlayerMode;
import es.ucm.fdi.tp.mvc.gui.RectBoardView;
import es.ucm.fdi.tp.visualComponents.JBoard.JBoard.Shape;

/*
 * Esta es la VISTA particular para un juego concreto. Determina
 * + Ancho y alto del tablero.
 * + Respuestas a ventos del teclado y del ratón.
 * + Titulo de la ventana.
 * + El color del tablero.
 * + La forma de las fichas.
 */

public class DcView extends RectBoardView<DcState, DcAction> {

	private static final long serialVersionUID = -317998045963418809L;
	
	private boolean help;
	private int initialCol;
	private int initialRow;
	private boolean firstClick;
	
	public DcView(DcState dcState) {
		super(dcState);
		this.help = false;
		this.firstClick = false;
	}

	// Obtener la columnas del tablero.
	@Override
	protected int getNumCols() {
		return super.state.getBoard()[0].length;
	}

	/// Obtener las filas del tablero.
	@Override
	protected int getNumRows() {
		return super.state.getBoard().length;
	}

	// Este método sirve para responder a los eventos del teclado.
	@Override
	protected void keyTyped(int keyCode) {
		
		if( this.firstClick ) {
			
			switch(keyCode) {
				case 27:
					
					this.firstClick = false;
					this.selectPiece( this.initialRow, this.initialCol, Color.BLACK);
					this.infoViewer.addContent("Selection canceled.");
					
				break;
				case 'h':
					
					Color color = null; 
					
					if(!this.help) {
						this.help = true;
						color = Color.GREEN;
					} else {
						this.help = false;
						color = Color.BLACK;
					}
					
					for(DcAction i : validActions) {
						if(this.initialRow == i.getFromRow() && this.initialCol == i.getFromCol())
							this.markCell(i.getToRow(),i.getToCol(),color);
					}
					
				break;
			}
			
		}
		
	}

	// Obtener el contenido de una determinada casilla del tablero.
	@Override
	protected Integer getPosition(int row, int col) {
		return super.state.at(row, col);
	}

	// Devuelve un tipo de figura para poder representar las fichas en el tablero.
	@Override
	protected Shape getShape(int player) {
		return ( player >= 0)? Shape.CIRCLE : Shape.NONE;
	}

	// Método que maneja los eventos relacionados con el ratón.
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
						this.selectPiece(row, col, Color.GREEN);
						this.infoViewer.addContent("Press 'h' to see posible movements");
						this.infoViewer.addContent("Selected("+row+","+col+
								"). Click on destination cell or ESC to cancel selection.");
	
					}
					
				} else {
					
					this.help = false;
					
					this.firstClick = false;
					
					DcAction action = new DcAction(this.state.getTurn(), initialRow, initialCol, row, col);
					
					this.gameCtrl.makeManualMove(action);
					
				}
					
				
			} else {
			
				this.infoViewer.addContent("It's not your turn.");
				
			}
		
		}
		
	}
	
	// Color que tendrá el tablero propio de este juego.
	@Override
	protected Color getBackground(int row, int col) {
		return ( (row%2==0&&col%2==0) || (row%2==1&&col%2==1) )? Color.LIGHT_GRAY : Color.BLACK; 
	}
	
	/*
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
	*/
	
	// Necesario para mostrar el título de la ventana.
	@Override
	public String toString() {
		return "Damas Chinas";
	}

}
