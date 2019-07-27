package es.ucm.fdi.tp.games.chess;

import java.awt.Color;
import java.util.Collection;

import javax.swing.ImageIcon;

import es.ucm.fdi.tp.base.Utils;
import es.ucm.fdi.tp.games.chess.ChessBoard.Piece;
import es.ucm.fdi.tp.mvc.common.PlayerMode;
import es.ucm.fdi.tp.mvc.gui.RectBoardView;
import es.ucm.fdi.tp.visualComponents.JBoard.JBoard;
import es.ucm.fdi.tp.visualComponents.JBoard.JBoard.Shape;

public class ChessView extends RectBoardView<ChessState, ChessAction> {
	
	private static final long serialVersionUID = -4334481511799170808L;
	
	private int initialCol;
	private int initialRow;
	private boolean firstClick;
	private boolean help;
	private static ImageIcon chessIcons[] = loadChessIcons();
	
	public ChessView(ChessState ChessState) {
		super(ChessState);
		this.firstClick = false;
		this.help = false;
	}
	
	protected int getNumCols() {
		return super.state.getDimension();
	}
	
	protected int getNumRows() {
		return super.state.getDimension();
	}
	
	@Override
	protected Integer getPosition(int row, int col) {
	        return super.state != null && !ChessBoard.empty((byte) state.at(row,col))? state.at(row, col): null;
	}
	

    
    private static ImageIcon[] loadChessIcons() {
        ImageIcon[] icons = new ImageIcon[Piece.Empty.white()+1]; //->Piece.Empty.White()+1
        for (Piece p: Piece.values()) {
            if(p!=Piece.Empty && p != Piece.Outside) {
                byte code = p.white();
                icons[code] = new ImageIcon(Utils.loadImage("chess/" + Piece.iconName(code))); //r_w.png
            }
        }
        for (Piece p: Piece.values()) {
            if(p!=Piece.Empty && p != Piece.Outside) {
                byte code = p.black();
                icons[code] = new ImageIcon(Utils.loadImage("chess/" + Piece.iconName(code))); //r_w.png
            }
        }
        return icons;
    }
	
	
	//Override these 2 methods so you draw the chess
	//board as chess board (as in the image in these slides)
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
				for(ChessAction i : validActions) {
					if(this.initialRow == i.getSrcRow() && this.initialCol == i.getSrcCol())
						this.markCell(i.getDstRow(), i.getDstCol(), color);
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
	
		if(!this.state.isFinished() && this.active && this.gameCtrl.getPlayerMode() == PlayerMode.MANUAL) {
		
			int playerId = super.gameCtrl.getPlayerId();
				
			if( playerId == this.state.getTurn() )
			{
					
				if(!this.firstClick) {
					int id = super.state.at(row, col);
					if(ChessBoard.white((byte)id)) {
						id = 0;
					} else {
						id = 1;
					}
					if(playerId == id) {
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
					 ChessAction action = new ChessAction(this.state.getTurn(), initialRow, initialCol, row, col);
			            if (state.isValid(action)) {
			            	this.gameCtrl.makeManualMove(action);
			            } else {
			                action = getSpecialAction(action);
			                if(action!=null) {
			                	this.gameCtrl.makeManualMove(action);
			                } else {
			                	this.infoViewer.addContent("Invalid Move");
			                }
			            }
			            this.repaint();
				}
					
				
			} else
				this.infoViewer.addContent("It's not your turn.");
		
		}
			
	}
	
	
	private ChessAction getSpecialAction(ChessAction action) {
		Collection<ChessAction> actions = state.validActions(this.state.getTurn());
	    for ( ChessAction a: actions) {
	        if(a.getSrcRow() == action.getSrcRow() &&
	            a.getSrcCol() == action.getSrcCol() &&
	            a.getDstRow() == action.getDstRow() &&
	            a.getDstCol() == action.getDstCol()
	        )
	        return a;
	    }
	    return null;
	}

	@Override
	protected Color getPlayerColor(int id) {
		Color color;
		if(ChessBoard.black((byte)id)) {
			color = super.getPlayerColor(0);
		} else {
			color = super.getPlayerColor(1);
		}
		
		return color;
	}

	@Override
	public String toString() {
		return "Chess";
	}

	@Override
	protected Shape getShape(int player) {
		if( player < 16)
			return Shape.CIRCLE;
		else
			return Shape.NONE;
	}

	@Override
	protected ImageIcon getIcon(Integer p) {
		return chessIcons[p];
	}

}
