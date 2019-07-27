package es.ucm.fdi.tp.mvc.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.dessignPatterns.Mediator.ChatMediator;
import es.ucm.fdi.tp.dessignPatterns.Mediator.CommonWindowMediator;
import es.ucm.fdi.tp.dessignPatterns.Mediator.ControlPanelMediator;
import es.ucm.fdi.tp.dessignPatterns.ObserverObservable.GameObservable;
import es.ucm.fdi.tp.dessignPatterns.ObserverObservable.GameObserver;
import es.ucm.fdi.tp.games.dc.DcState;
import es.ucm.fdi.tp.mvc.common.PlayerMode;
import es.ucm.fdi.tp.mvc.gui.GameEvent.EventType;
import es.ucm.fdi.tp.visualComponents.Chat.ChatComp;
import es.ucm.fdi.tp.visualComponents.Chat.ChatViewer;
import es.ucm.fdi.tp.visualComponents.ControlPanel.ControlPanelComp;
import es.ucm.fdi.tp.visualComponents.ControlPanel.ControlPanelViewer;
import es.ucm.fdi.tp.visualComponents.MessageViewer.MessageViewer;
import es.ucm.fdi.tp.visualComponents.MessageViewer.MessageViewerComp;
import es.ucm.fdi.tp.visualComponents.PlayersInfo.PlayersInfoComp;
import es.ucm.fdi.tp.visualComponents.PlayersInfo.PlayersInfoViewer;
import es.ucm.fdi.tp.visualComponents.Simulation.SimulationComp;
import es.ucm.fdi.tp.visualComponents.Simulation.SimulationViewer;

/**
 * Esta clase es la vista que se mostrará gráficamente. Será la única observadora
 * del modelo y coordinará a todas las componentes visuales mediante update, disable, 
 * enable, etc. (Esta clase extiende de JPanel).
 * @author Daniel Calle Sanchez
 * @author Manuel Guerrero Moñus
 */
public class GameContainer<S extends GameState<S,A>, A extends GameAction<S,A>> extends GUIView<S,A> implements GameObserver<S, A> {

	private static final long serialVersionUID = -4082768430391914303L;

	private S state;
	private int playerId;
	private boolean active;
	private GUIView<S, A> gameView;
	private GUIController<S,A> gameCtrl;
	private MessageViewer<S,A> messageViewer;
	private ControlPanelViewer<S,A> controlPanel;
	private PlayersInfoViewer<S, A> playersInfoViewer;
	
	//**************************** NUEVAS COMPONENTES VISUALES *********************************************//

	private ChatViewer<S,A> chatViewer;
	
	private ChatMediator<S,A> chatMediator;
	
	private SimulationViewer<S,A> simulationViewer;
	
	private CommonWindowMediator<S,A> commonWindowMediator;
	
	private ControlPanelMediator<S,A> controlPanelMediator;
	
	//******************************************************************************************************//
	
	public GameContainer(GUIView<S, A> gameView, GUIController<S,A> gameCtrl,GameObservable<S, A> game,
			CommonWindowMediator<S,A> commonWindowMediator, ChatMediator<S,A> chatMediator, 
			ControlPanelMediator<S,A> controlPanelMediator, int playerId, S state) {
		
		this.state = state;
		this.gameView = gameView;
		this.gameCtrl = gameCtrl;
		this.playerId = playerId;
		
		initGUI();
	 	
		game.addObserver(this);
	 	
	 	//**************************************************************//

		this.chatMediator = chatMediator;
		
		this.commonWindowMediator = commonWindowMediator;
		
		this.controlPanelMediator = controlPanelMediator;
		
		this.chatMediator.addChat(this.chatViewer);
		
		this.controlPanelMediator.addControlPanel(this.controlPanel);

		this.commonWindowMediator.commonWindowAddClient(this.playerId);
		
	 	//**************************************************************//
	 	
	 }
	
	// Inicializa los componentes gráficos y los añade al panel.
	private void initGUI() {

		//**************************************************************//
		
		this.messageViewer  = new MessageViewerComp<>();
		this.playersInfoViewer = new PlayersInfoComp<>();	
		this.controlPanel = new ControlPanelComp<>(this.gameCtrl);

		//************ INICIALIZACIÓN DE NUEVOS COMPONENTES ************//
		
		this.chatViewer = new ChatComp<>();
		
		this.simulationViewer = new SimulationComp<>(this.gameCtrl);
		
		//**************************************************************//
		
		this.gameView.setMessageViewer(this.messageViewer);
		this.gameView.setPlayersInfoViewer(this.playersInfoViewer);
		
		this.playersInfoViewer.setMessageViewer(this.messageViewer);
		this.playersInfoViewer.setPlayersInfoViewer(this.playersInfoViewer);
		
		this.controlPanel.setMessageViewer(this.messageViewer);
		this.controlPanel.setPlayersInfoViewer(this.playersInfoViewer);
		
		//******* NUEVAS COLABORACIONES ENTRE COMPONENTES VISUALES *****//
		
		this.chatViewer.setGameController(this.gameCtrl);
		
		this.simulationViewer.update(this.state,this.active);
		
		//**************************************************************//
		
		this.setLayout(new BorderLayout(5, 5));
		
		JPanel eastPanel = new JPanel();
		
		// Hay que cambiarlo si queremos seguir añadiendo componentes.
		eastPanel.setLayout(new GridLayout(4,1));
		
		eastPanel.setPreferredSize(new Dimension(300,100));
		eastPanel.add(messageViewer);
		eastPanel.add(playersInfoViewer);
		
		//************ NUEVOS ELEMENTOS PANEL ESTE ************//
		
		eastPanel.add(chatViewer);
		
		eastPanel.add(simulationViewer);
		
		//*****************************************************//
	
		this.add(gameView,BorderLayout.CENTER);
		this.add(controlPanel,BorderLayout.NORTH);
		this.add(eastPanel,BorderLayout.EAST);
	
	}
	
	// El modelo nos notificará algún evento, el cual lo procesamos
	// mejor en una hebra de swing.
	public void notifyEvent(GameEvent<S,A> e) {
		SwingUtilities.invokeLater(() -> handleEvent(e));
	}
	
	// Procesamos los eventos que provienen del modelo.
	// Algunos de ellos también habremos de pasarlos al controlador.
	private void handleEvent(GameEvent<S,A> e) {
		
		switch (e.getType()) {
			
			case Start:
				
				super.setTitle( gameView.toString() + " ( view for Player " + playerId + " )");
		
				update(e.getState(), true);
				
				this.playersInfoViewer.setNumberOfPlayer(e.getState().getPlayerCount());
				
				this.messageViewer.setContent(e.toString() + "\n" );
				
				this.gameCtrl.handleEvent(e);
				
				if(this.playerId==e.getState().getTurn()) {
				
					enable();
				
					this.messageViewer.addContent("It's your turn");
					
					//*********************************************************************************//

					this.commonWindowMediator.commonWindowInitalize(this.playerId);
					
					//*********************************************************************************//
					
				} else {
					
					disable();
				
				}
				
				if(this.gameCtrl.getPlayerMode()!=PlayerMode.MANUAL) {
				
					this.gameCtrl.handleEvent(new GameEvent<S, A>(EventType.Change,null,e.getState(),null, "Automatic movement"));
				
				} else {
					
					this.gameCtrl.handleEvent(new GameEvent<S, A>(EventType.Change,null,e.getState(),null, "Manual movement"));
				
				}
					
			break;
			case Change:

				int lastTurn = this.state.getTurn();
				
				update(e.getState(), this.active);
				
				if(this.playerId==e.getState().getTurn()) {
				
					enable();
					
					this.messageViewer.addContent("It's your turn");
					
					//**************************************************************************************************//
					
					if(lastTurn == e.getState().getTurn())
					
						this.commonWindowMediator.commonWindowChronometerStopAndPublishFor(lastTurn);
						
					this.commonWindowMediator.commonWindowChronometerStartFor(this.playerId);
					
					//**************************************************************************************************//
				
				} else {
				
					//**************************************************************************************************//
					
					if(lastTurn != e.getState().getTurn())
					
						this.commonWindowMediator.commonWindowChronometerStopAndPublishFor(this.playerId);
					
					//**************************************************************************************************//
					
					this.messageViewer.addContent("You have moved");
					
					disable();
					
				}
					
				if(this.gameCtrl.getPlayerMode()!=PlayerMode.MANUAL) {
				
					this.gameCtrl.handleEvent(new GameEvent<S, A>(EventType.Change,null,e.getState(),null, "Automatic movement"));
				
				} else {
					
					this.gameCtrl.handleEvent(new GameEvent<S, A>(EventType.Change,null,e.getState(),null, "Manual movement"));
				
				}
				
			break;
			case Error:
				
				update(e.getState(),this.active);
				
				this.messageViewer.addContent(e.toString());
			
			break;
			case Stop:
				
				update(e.getState(), false);
				
				this.gameCtrl.handleEvent(e);
				
				if(this.state.isFinished()) { 	
				
					this.messageViewer.addContent( (e.getState().getWinner() == -1)? "Draw game !!!" : (e.getState().getWinner() == this.playerId)? "You win !!!" : "You lose !!!" );
				
				} else {
					
					this.messageViewer.addContent("The game has stopped");
				}
					
				disable();
				
				this.playersInfoViewer.disable();
				
			break;
			case Info:
				
				// this.gameView.update(e.getState(),this.active);
			
				// this.messageViewer.addContent(e.toString());
			
			break;
			default:
			break;
			
		}
	
	}
	
	// Sobreescribimos la representación gráfica de la ventana para considerar paneles o barras de escritorio.
	@Override
	public void enableWindowMode() {
		
		super.enableWindowMode();
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

		Insets containerBorder = Toolkit.getDefaultToolkit().getScreenInsets(getGraphicsConfiguration());
		
		window.setBounds( this.playerId * ( (int) dim.getWidth() / this.state.getPlayerCount() ) + containerBorder.left ,
				containerBorder.top , (int) dim.getWidth() / this.state.getPlayerCount() - containerBorder.right ,
				(int) dim.getHeight() - containerBorder.bottom );
		
	}
	
	//******************************* MÉTODOS ABSTRACTOS DE LA VISTA DEFINIDOS ********************************************//
	
	// Habilitamos el panel superior y la vista específica del juego.
	@Override
	public void enable() {
		this.controlPanel.enable();
		this.gameView.enable();
	}
	
	// Desabilitamos el panel superior y la vista específica del juego.
	@Override
	public void disable() {
		this.controlPanel.disable();
		this.gameView.disable();
	}
	
	/*
	 * Hemos de actualizar el estado de juego y de la actividad de esta
	 * vista así como el de:
	 * + La vista específica del juego.
	 * + El panel supeior.
	 * + El cuadro de mensajes.
	 * + El cuadro de información del usuario.
	 */
	@Override
	public void update(S state, boolean active) {
		
		this.state = state;
		this.active = active;
		this.gameView.update(state, active);
		this.controlPanel.update(state, active);
		this.playersInfoViewer.update(state, active);
		this.messageViewer.update(state, active);
		
		//****************************************************//
		
		this.simulationViewer.update(state, active);
		
		//****************************************************//
		
	}
	
	@Override
	public void setMessageViewer(MessageViewer<S, A> infoViewer) {
		this.messageViewer = infoViewer;
	}
	
	@Override
	public void setPlayersInfoViewer(PlayersInfoViewer<S, A> playersInfoViewer) {
		this.playersInfoViewer = playersInfoViewer;
	}
	
	@Override
	public void setGameController(GUIController<S, A> gameCtrl) {
		this.gameCtrl = gameCtrl;
	}

	@Override
	public void setChatViewer(ChatViewer<S, A> chatViewer) {
		this.chatViewer = chatViewer;
	}
	
}