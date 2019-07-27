package es.ucm.fdi.tp.visualComponents.ControlPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;
import es.ucm.fdi.tp.base.Utils;
import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.dessignPatterns.ObserverObservable.SmartMoveObserver;
import es.ucm.fdi.tp.mvc.common.PlayerMode;
import es.ucm.fdi.tp.mvc.gui.GUIController;
import es.ucm.fdi.tp.visualComponents.Chat.ChatViewer;
import es.ucm.fdi.tp.visualComponents.MessageViewer.MessageViewer;
import es.ucm.fdi.tp.visualComponents.PlayersInfo.PlayersInfoViewer;

/**
 * ControlPanel is the concrete class that will create the button for
 * playing automatically, selecting the player mode, etc.
 * All buttons should be enabled only when it is the turn of the player
 * who plays in this window, and only if the player mode is MANUAL. The
 * combo-box for selecting the player mode should always be enabled. This
 * behaviour should be coded in the disable and enable methods.
 * 
 * @author Daniel Calle Sanchez
 * @author Manuel Guerrero Moñus
 *
 */
public class ControlPanelComp<S extends GameState<S,A>, A extends GameAction<S,A>> extends ControlPanelViewer<S,A> implements SmartMoveObserver {
    
	private static final long serialVersionUID = 6254758538201027109L;
	
	private S state;
	private boolean active;
	private JLabel brainLabel;
	private JLabel timerLabel;
	private JToolBar container;
	private JButton stopButton;
	private PlayerMode currentMode;
	private JButton twiceMoveButton;
	private JButton smartMoveButton;
	private JSpinner timeOutSpinner;
	private JSpinner nThreadsSpinner;
	private JButton randomMoveButton;
	private JButton restartGameButton;
	private JButton stopProgramButton;
	private JButton exitProgramButton;
	private GUIController<S,A> gameCtrl;
	private JComboBox<PlayerMode> modesCB;
	private MessageViewer<S, A> messageViewer;
	
	//************* MULTIPLE MOVEMENTS ***********************************************************************//
	
	private int aditionalMovements;
	
	private final static int ADITIONAL_MOVEMENTS = 1;
	
	private final static int NO_ADITIONAL_MOVEMENTS = -1;
	
	private final static int EMPTY_ADITIONAL_MOVEMENTS = 0;
	
	//********************************************************************************************************//
	
    public ControlPanelComp(GUIController<S,A> gameCtrl) {
        
    	this.gameCtrl = gameCtrl;
        
        initGUI();
        
        gameCtrl.addSmartPlayerObserver(this);
        
        //***********************************************************************//
        
        this.aditionalMovements = ControlPanelComp.NO_ADITIONAL_MOVEMENTS;
        
        //***********************************************************************//
        
    }
    
    // Crea los componentes y sus listeners. 
    // Llama a los metodos del controlador usando SwingUtilities.invokeLater
     
    private void initGUI() {
    
    	this.setLayout(new BorderLayout());
    	
    	this.container = new JToolBar();
    	
    	this.container.setLayout(new BorderLayout());
    	
    	// Panel donde estan las componentes.
    	JPanel mainPanel = new JPanel();
    	
        // Boton del jugador Random.
        this.randomMoveButton = new JButton();
        this.randomMoveButton.setToolTipText("Random Move");
        this.randomMoveButton.setIcon(new ImageIcon(Utils.loadImage("dice.png")));
        this.randomMoveButton.addActionListener( (ActionEvent e) -> { 
        	
        	disable();
        	
        	this.gameCtrl.makeRandomMove();
        	
        });
        
        mainPanel.add(this.randomMoveButton);
        
        // Boton del jugador Smart.
        this.smartMoveButton = new JButton();
        this.smartMoveButton.setToolTipText("Smart Move");
        this.smartMoveButton.setIcon(new ImageIcon(Utils.loadImage("nerd.png")));
        this.smartMoveButton.addActionListener( (ActionEvent e) -> { 
        	
        	disable();
        
        	currentMode = gameCtrl.getPlayerMode();
        	
        	gameCtrl.changePlayerMode(PlayerMode.SMART);
        	
        	modesCB.setSelectedItem(PlayerMode.SMART);
        	
        	this.gameCtrl.makeSmartMove();
        	
        });
       
        mainPanel.add(this.smartMoveButton);
        
        this.twiceMoveButton = new JButton();
		
        this.twiceMoveButton.setToolTipText("Play Twice");
        
        this.twiceMoveButton.setIcon(new ImageIcon(Utils.loadImage("two.png")));
        
		this.twiceMoveButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				twiceMoveButton.setEnabled(false);
				
				aditionalMovements = ControlPanelComp.ADITIONAL_MOVEMENTS;
				
			}
			
		});
		
		mainPanel.add(twiceMoveButton);
        
        // Boton ReStart.
        this.restartGameButton = new JButton();
        this.restartGameButton.setToolTipText("ReStart Game");
        this.restartGameButton.setIcon(new ImageIcon(Utils.loadImage("restart.png")));
        this.restartGameButton.addActionListener( (ActionEvent e) -> {
        	
        	this.gameCtrl.restartGame();
        	
        });
        
        mainPanel.add(restartGameButton);
        
        this.stopProgramButton = new JButton();
        this.stopProgramButton.setToolTipText("Stop Game");
        this.stopProgramButton.setIcon(new ImageIcon(Utils.loadImage("stop_game.png")));
        this.stopProgramButton.addActionListener( (ActionEvent e) -> {
        	
        	if( !ControlPanelComp.this.state.isFinished() && ControlPanelComp.this.active )
        		
        		ControlPanelComp.this.gameCtrl.stopGame();

		});
        
        mainPanel.add(this.stopProgramButton);
        
        // Boton exitProgram.
        this.exitProgramButton = new JButton();
        this.exitProgramButton.setToolTipText("Exit Game");
        this.exitProgramButton.setIcon(new ImageIcon(Utils.loadImage("exit.png")));
        this.exitProgramButton.addActionListener( (ActionEvent e) -> {
				
			JFrame frame = new JFrame();

			int answer = JOptionPane.showOptionDialog(frame,
					"Do you want exit of the game?","Info message",
					JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,null,null);

			if( answer == JOptionPane.YES_OPTION ) System.exit(0);

		});
        
        mainPanel.add(this.exitProgramButton);
        
        // Label playerMode
    	JLabel playerMode = new JLabel("Player Mode:");
    	
    	mainPanel.add(playerMode);
    	
        //******************* PLAYER MODE COMBO - BOX ********************************************************************//
    	
    	this.modesCB = new JComboBox<PlayerMode>(new DefaultComboBoxModel<PlayerMode>() {
    		
			private static final long serialVersionUID = -8691330628592500707L;

			@Override
    		public void setSelectedItem(Object o) {
    			
    			super.setSelectedItem(o);
    			
    			decideMovement((PlayerMode) o);
    			
    			if(messageViewer != null)
    				
    				messageViewer.addContent("Player changed to " + o.toString());
    			
    		}
			
			public void decideMovement(PlayerMode playerMode) {
    			
				switch(playerMode) {
    				
    				case RANDOM:
			
    					gameCtrl.stopSmartPlayer();
    					
    					gameCtrl.changePlayerMode(PlayerMode.RANDOM);
    					
    					disable();
    					
					break;
					
    				case SMART:

    					gameCtrl.changePlayerMode(PlayerMode.SMART);

    					disable();
    					
    				break;
    				
    				default:

    					gameCtrl.stopSmartPlayer();
    					
    					gameCtrl.changePlayerMode(PlayerMode.MANUAL);

    					if(state != null && gameCtrl.getPlayerId() == state.getTurn() && !state.isFinished() && active)
    						enable();
    					
    				break;
				
				}
			
			}
    		
    	});
    	

    	//****************************************************************************************************************//
    	
    	for( PlayerMode i : PlayerMode.values() ) this.modesCB.addItem(i);
    	
    	mainPanel.add(this.modesCB);
    	
    	JPanel autoPanel = new JPanel();

		JPanel mainPanelAutoPanel = new JPanel();
	
	    autoPanel.setLayout(new BorderLayout());
	    
	    autoPanel.setBorder(BorderFactory.createTitledBorder("Smart Moves"));

	    autoPanel.add(mainPanelAutoPanel);
	    
	    brainLabel = new JLabel(new ImageIcon(Utils.loadImage("brain.png")));
	    
	    brainLabel.setOpaque(true);
	    
	    mainPanelAutoPanel.add(brainLabel);
	    
	    nThreadsSpinner = new JSpinner(new SpinnerNumberModel(1,1,Runtime.getRuntime().availableProcessors(),1));
	    
	    nThreadsSpinner.addChangeListener( (e) -> this.gameCtrl.smartPlayerConcurrencyLevel( (Integer) nThreadsSpinner.getValue() ) );
	    
	    mainPanelAutoPanel.add(nThreadsSpinner);
	    
	    timerLabel = new JLabel(new ImageIcon(Utils.loadImage("timer.png")));
	    
	    mainPanelAutoPanel.add(timerLabel);
	    
	    timeOutSpinner = new JSpinner(new SpinnerNumberModel(1000,50,5000,500));
	    
	    timeOutSpinner.addChangeListener( (e) -> this.gameCtrl.smartPlayerTimeLimit( (Integer) timeOutSpinner.getValue() ) );

	    mainPanelAutoPanel.add(timeOutSpinner);
	    
	    stopButton = new JButton(new ImageIcon(Utils.loadImage("stop.png")));
	    
		stopButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
			
				gameCtrl.stopSmartPlayer();
					
				modesCB.setSelectedItem(PlayerMode.MANUAL);
			
			}
			
		});

		mainPanelAutoPanel.add(stopButton);
	    
		mainPanel.add(autoPanel);
		
		this.container.add(mainPanel,BorderLayout.WEST);
		
		// El componente se expande ocupando todo el contenedor.
	    this.add(container,BorderLayout.PAGE_END);
        
    }
    
    //********************** MÉTODOS HEREDADOS DE LA VISTA ********************************************************************//
    
    // Los botones solo han de estar habilitados cuando es el turno del jugador
    // que juega en esta ventana, y solo si el modo es MANUAL. El combo - box siempre
    // ha de estar habilitado.

    @Override
    public void enable() {
    
    	if(gameCtrl.getPlayerMode() == PlayerMode.MANUAL) {
    	
    		if(this.aditionalMovements == ControlPanelComp.NO_ADITIONAL_MOVEMENTS)
    			
    			this.twiceMoveButton.setEnabled(true);
    			
	    	this.smartMoveButton.setEnabled(true);
	    	
	    	this.randomMoveButton.setEnabled(true);
    
    	}
    	
    }
    
    @Override
    public void disable() {
    	
    	this.twiceMoveButton.setEnabled(false);
    	
    	this.smartMoveButton.setEnabled(false);
    	
    	this.randomMoveButton.setEnabled(false);
    	
    	if(this.aditionalMovements == ControlPanelComp.EMPTY_ADITIONAL_MOVEMENTS)
    		
    		this.aditionalMovements = ControlPanelComp.NO_ADITIONAL_MOVEMENTS;
    	
    }
    
    @Override
	public void update(S state, boolean active) {
    	this.active = active;
    	this.state = state;	
	}

	@Override
	public void setMessageViewer(MessageViewer<S,A> infoViewer) {
		this.messageViewer = infoViewer;
	}

	@Override
	public void setPlayersInfoViewer(PlayersInfoViewer<S,A> playersInfoViewer) {
	
	}

	@Override
	public void setGameController(GUIController<S,A> gameCtrl) {
		this.gameCtrl = gameCtrl;
	}
	
	@Override
	public void setChatViewer(ChatViewer<S,A> chatViewer) {
		
	}
	
	//*************************************************************************************************************************//
	
	//************************ SMARTPLAYER OBSERVER ***************************************************************************//
	
	@Override
	public void onStart() {
		
		brainLabel.setBackground(Color.YELLOW);

	}

	@Override
	public void onEnd(boolean success, long time, int nodes, double value) {
		
		brainLabel.setBackground(getBackground());
		
		if(modesCB.getSelectedItem() != PlayerMode.SMART) currentMode = null;
		
		if(currentMode != null) {
				
			gameCtrl.changePlayerMode(currentMode);
				
			modesCB.setSelectedItem(currentMode);
				
			currentMode = null;
				
		}
			
		if(!success) {
			
			if(active)
				
				enable();
			
			else
				
				disable();
			
		} else {
				
			messageViewer.addContent(nodes +" nodes in "+ time +" ms. Value = "+ String.format("%.5f",value));
			
		}
		
	}

	//*************************************************************************************************************************//

	//*************** MULTIPLE MOVEMENTS **************************************************************************************//
	
	@Override
	public boolean hasAditionalMovements() {
		
		return (this.aditionalMovements > 0);
		
	}

	@Override
	public void decreaseAditionalMovements() {
		
		this.aditionalMovements--;
		
	}
	
	//*************************************************************************************************************************//
	
}