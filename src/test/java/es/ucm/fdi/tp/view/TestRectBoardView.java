package es.ucm.fdi.tp.view;

import javax.swing.SwingUtilities;

import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.games.chess.ChessView;
import es.ucm.fdi.tp.mvc.gui.RectBoardView;

public class TestRectBoardView {
	
	public static void main(String[] args) {
		
		SwingUtilities.invokeLater( () -> {
		
			RectBoardView rbv = new ChessView(null);
			
			rbv.enableWindowMode();
			
		});
		
	}

}
