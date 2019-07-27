package es.ucm.fdi.tp.visualComponents.Simulation;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.dessignPatterns.ObserverObservable.SimulationObserver;
import es.ucm.fdi.tp.mvc.gui.GUIController;
import es.ucm.fdi.tp.visualComponents.Chat.ChatViewer;
import es.ucm.fdi.tp.visualComponents.JCrossTable.JCrossTableComp;
import es.ucm.fdi.tp.visualComponents.JCrossTable.JCrossTableViewer;
import es.ucm.fdi.tp.visualComponents.MessageViewer.MessageViewer;
import es.ucm.fdi.tp.visualComponents.PlayersInfo.PlayersInfoViewer;

public class SimulationComp < S extends GameState<S,A> , A extends GameAction<S,A> > extends SimulationViewer<S,A> 
implements SimulationObserver  {

	private static final long serialVersionUID = -2029116378110346733L;

	private JButton stop;
	private JButton start;
	private JCrossTableViewer crossTable;
	
	private S state;
	private GUIController<S,A> ctrl;
	
	private final static int POOLS = 4;
	private final static int MAX_ACTIONS = 100;
	private final static int MATCHES_BY_POOL = 2500;
	
	private final static int DRAW = -1;
	private final static int FIRST_ROW = 0;
	private final static int DRAWS_COLUMN = 2;
	private final static int DEFEATS_COLUMN = 1;
	private final static int VICTORIES_COLUMN = 0;
	
	public SimulationComp(GUIController<S,A> ctrl) {
		
		this.ctrl = ctrl;
		
		initGUI();
		
		ctrl.addSimulationObserver(this);
		
	}

	private void initGUI() {
		
		this.setLayout(new BorderLayout());

		this.setBorder(BorderFactory.createTitledBorder("Emulation"));
		
		JPanel mainPanel = new JPanel();
		
		String[] rowNames = { "Player " + ctrl.getPlayerId() };
		
		String[] columnNames = { "Victories" , "Defeats" , "Draws" };
		
		Object[][] dataMatrix = {
			
				{ new Integer(0) , new Integer(0) , new Integer(0) }
				
		};
		
		crossTable = new JCrossTableComp(rowNames, columnNames, dataMatrix);
		
		crossTable.setSize( 280 , 38 );
		
		mainPanel.add(crossTable.getComponent());
		
		start = new JButton("START");
		
		start.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
			
				crossTable.resetToInitialState();
				
				ctrl.makeSimulation(state,SimulationComp.POOLS, SimulationComp.MATCHES_BY_POOL, SimulationComp.MAX_ACTIONS);
				
			}
			
		});
		
		mainPanel.add(start);
		
		stop = new JButton("STOP");
		
		stop.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
			
				ctrl.stopSimulation();
				
			}
			
		});
		
		mainPanel.add(stop,BorderLayout.PAGE_END);
		
		this.add(mainPanel);
		
	}

	//********************** MÉTODOS HEREDADOS DE LA VISTA ********************************************************************//
	
	@Override
	public void enable() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void disable() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(S state, boolean active) {
		
		this.state = state;
		
	}

	@Override
	public void setMessageViewer(MessageViewer<S,A> infoViewer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPlayersInfoViewer(PlayersInfoViewer<S,A> playersInfoViewer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setGameController(GUIController<S,A> gameCtrl) {
		
		this.ctrl = gameCtrl;
		
	}

	@Override
	public void setChatViewer(ChatViewer<S,A> chatViewer) {
		// TODO Auto-generated method stub
		
	}

	//*************************************************************************************************************************//

	//************************************ SIMULATION OBSERVER ****************************************************************//
	
	@Override
	public void notifyMatchResult(int result) {
		
		int column;
		
		if((result == SimulationComp.DRAW)) {
			
			column = SimulationComp.DRAWS_COLUMN;
			
		} else {
			
			if(result != this.ctrl.getPlayerId()) {
				
				column = SimulationComp.DEFEATS_COLUMN;
				
			} else {
				
				column = SimulationComp.VICTORIES_COLUMN;
				
			}
			
		}
		
		Integer value = (Integer) this.crossTable.getValueAt( SimulationComp.FIRST_ROW , column );
		
		this.crossTable.setValueAt( ++value , SimulationComp.FIRST_ROW , column );
		
	}
	
	//*************************************************************************************************************************//
	
}
