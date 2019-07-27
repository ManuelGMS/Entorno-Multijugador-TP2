package es.ucm.fdi.tp.visualComponents.CommonWindow;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JFrame;

import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.mvc.gui.GUIController;
import es.ucm.fdi.tp.visualComponents.Chat.ChatViewer;
import es.ucm.fdi.tp.visualComponents.JCrossTable.JCrossTableComp;
import es.ucm.fdi.tp.visualComponents.MessageViewer.MessageViewer;
import es.ucm.fdi.tp.visualComponents.PlayersInfo.PlayersInfoViewer;

public class CommonWindowComp<S extends GameState<S,A>, A extends GameAction<S,A>> extends CommonWindowViewer<S,A> {

	private static final long serialVersionUID = -8198489856750020957L;

	private JCrossTableComp stadistics;
	private ArrayList<PlayerStadictics> playerStadictics;
	
	private class PlayerStadictics {
		
		private int time;
		private int totalTime;
		
		public PlayerStadictics() {
			this.time = 0;
			this.totalTime = 0;
		}

		public int getTime() {
			return time;
		}

		public void setTime(int time) {
			this.time = time;
		}

		public int getTotalTime() {
			return totalTime;
		}

		public void setTotalTime(int totalTime) {
			this.totalTime = totalTime;
		}
		
	}
	
	public CommonWindowComp() {
		this.playerStadictics = new ArrayList<>();
		initGUI();
	}
	
	private void initGUI() {
		
		this.setLayout(new BorderLayout());

		this.setBorder(BorderFactory.createTitledBorder("Player Time"));
		
		String[] columnNames = { "Time J1" , "Time J2" };
		
		String[] rowNames = { "Millis" , "Seconds" , "Minutes" };
		
		Integer[][] dataMatrix = { 
			{ new Integer(0) , new Integer(0) } , 
			{ new Integer(0) , new Integer(0) } ,
			{ new Integer(0) , new Integer(0) }	
		};
		
		stadistics = new JCrossTableComp(rowNames, columnNames, dataMatrix);
		
		this.add(stadistics.getComponent());
		
	}
	
	//************************** MÉTODOS DE LA TABLA CRUZADA ***************************************************************//
	
	@Override
	public void addPlayer(int playerId) {
		
		playerStadictics.add(new PlayerStadictics());
		
	}
	
	@Override
	public void chronometerStopFor(int playerId) {
		
		PlayerStadictics pS = playerStadictics.get(playerId);
		
		if(pS.getTime() > 0) {
		
			int currentTime = (int) System.currentTimeMillis();
			
			int time = pS.getTime();
			
			int totalTime = pS.getTotalTime();
			
			pS.setTotalTime( totalTime + ( currentTime - time ) );

		}
		
	}

	@Override
	public void chronometerStartFor(int playerId) {
		
		playerStadictics.get(playerId).setTime( (int) System.currentTimeMillis() );

	}

	@Override
	public void updateChronometerResultsFor(int playerId) {
		
		int millis = playerStadictics.get(playerId).getTotalTime();
		
		int seconds = millis / 1000;
		
		int minutes = seconds / 60;
		
		stadistics.setValueAt(new Integer(millis),  0, playerId);
		stadistics.setValueAt(new Integer(seconds), 1, playerId);
		stadistics.setValueAt(new Integer(minutes), 2, playerId);
		
	}

	@Override
	public void resetStadistics() {
		
		for(PlayerStadictics ps: playerStadictics) {
			ps.time = 0;
			ps.totalTime = 0;
		}
		
		stadistics.resetToInitialState();
			
	}
	
	//*********************************************************************************************************************//
	
	//****************************** MÉTODOS EXCLUSIVOS DE LA VISTA *******************************************************//
	
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
		// TODO Auto-generated method stub

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
		// TODO Auto-generated method stub

	}

	@Override
	public void setChatViewer(ChatViewer<S, A> chatViewer) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void enableWindowMode() {
		
		super.enableWindowMode();
		
		JFrame window = super.getWindow();
		
		window.setTitle("Stadistics");
		
		window.pack();
		
		window.setResizable(false);
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		
		int w = (int) window.getWidth();
		
		int h = (int) window.getHeight();
		
		int x = (int) ((dim.getWidth() / 2) - (window.getWidth() / 2));
		
		int y = (int) ((dim.getHeight() / 2) - (window.getHeight() / 2));
		
		window.setBounds(x ,y,w,h);
		
	}

	//*********************************************************************************************************************//

}