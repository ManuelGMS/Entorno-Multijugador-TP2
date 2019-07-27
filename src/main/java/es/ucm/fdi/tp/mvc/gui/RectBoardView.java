package es.ucm.fdi.tp.mvc.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.visualComponents.Chat.ChatViewer;
import es.ucm.fdi.tp.visualComponents.JBoard.JBoard;
import es.ucm.fdi.tp.visualComponents.JBoard.JBoard.Shape;
import es.ucm.fdi.tp.visualComponents.MessageViewer.MessageViewer;
import es.ucm.fdi.tp.visualComponents.PlayersInfo.PlayersInfoViewer;
import es.ucm.fdi.tp.visualComponents.PlayersInfo.PlayersInfoViewer.PlayersInfoObserver;

/*
 * Esta clase posee como padre a GuiView (abstact), luego es una vista.
 * Permite comunicarse con los componentes que van a formar el tablero:
 * JBoard, infoViewer y messageViewer. Todos ellos compondrán la GUI 
 * con la que el usuario va a interactuar.
 */

public abstract class RectBoardView
<S extends GameState<S,A>, A extends GameAction<S,A>> extends GUIView<S, A>
implements PlayersInfoObserver {

	private static final long serialVersionUID = -2989199564287992076L;
	
	// Un tablero.
	private JBoard jBoard;
	
	// Un estado (el último) para llamar a repaint() cuando sea necesario.
	protected S state;
		
	protected boolean active;
		
	// Acciones validas sobre el tablero.
	protected List<A> validActions;
	
	// Un MessageViewer para mostrar mensajes informativos.
	protected MessageViewer<S,A> infoViewer;
	
	// Un controlador para pasarle una acción.
	protected GUIController<S,A> gameCtrl;
	
	// Un Player'sInfo para consultar colores.
	protected PlayersInfoViewer<S,A> playersInfoViewer;
	
	//*************************** NUEVAS COMPONENTES VISUALES ************************************//
	
	protected ChatViewer<S,A> chatViewer;
	
	//********************************************************************************************//
	
	public RectBoardView(S state) {
		this.state = state;
		initGUI();
	}
	
	//************************** MÉTODOS ABSTRACTOS **********************************************//
	
	// Estos metodos abstractos, son implementados en la vista particular de cada juego.
	
	protected abstract int getNumCols();
	protected abstract int getNumRows();
	protected abstract void keyTyped(int keyCode);
	protected abstract Shape getShape(int player);
	protected abstract Integer getPosition(int row, int col);
	protected abstract void mouseClicked(int row, int col, int clickCount, int mouseButton);
	
	//********************************************************************************************//
	
	private void initGUI() {
		
		this.setLayout(new BorderLayout());
		
		this.jBoard = new JBoard() {
			
			private static final long serialVersionUID = -3745671300001877973L;

			@Override
			protected void keyTyped(int keyCode) {
				RectBoardView.this.keyTyped(keyCode);
			}

			@Override
			protected void mouseClicked(int row, int col, int clickCount, int mouseButton) {
				RectBoardView.this.mouseClicked(row,col,clickCount,mouseButton);
			}

			@Override
			protected Shape getShape(int player) {
				return RectBoardView.this.getShape(player);
			}

			@Override
			protected Color getColor(int player) {
				return RectBoardView.this.getPlayerColor(player);			
			}

			@Override
			protected Integer getPosition(int row, int col) {
				return RectBoardView.this.getPosition(row, col);
			}

			@Override
			protected Color getBackground(int row, int col) {
				return RectBoardView.this.getBackground(row,col);
			}

			@Override
			protected int getNumRows() {
				return RectBoardView.this.getNumRows();
			}

			@Override
			protected int getNumCols() {
				return RectBoardView.this.getNumCols();
			}
			
			@Override
			protected ImageIcon getIcon(Integer p) {
				return RectBoardView.this.getIcon(p);
			}
		};
		
		this.jBoard.setMinimumSize(new Dimension(300,300));
		
		this.add(this.jBoard, BorderLayout.CENTER);
		
	}

	
	// Color por defecto que van a tener todas las casillas del tablero.
	protected Color getBackground(int row, int col) {
		return Color.LIGHT_GRAY;
	}
	
	// Margen de separación de las casillas.
	protected int getSepPixels() {
		return 2;
	}
	
	// Si el id es de un jugador, devolver el color de este, si no devolver un color por defecto.
	protected Color getPlayerColor(int id) {
		return this.playersInfoViewer.getPlayerColor(id);
	}
	
	protected ImageIcon getIcon(Integer p) {
		return null;
	}
	
	// Método para seleccionar una ficha en su posición inicial.
	public void selectPiece(int row, int col, Color color) {
		this.jBoard.selectPiece(col, row, color);
	}
	
	// Marca una celda destino en caso de que el usuario pida ayuda.
	public void markCell(int row, int col, Color color) {
		this.jBoard.markCell(col, row, color);
	}
	
	//************************** MÉTODOS ABSTRACTOS DEFINIDOS **********************************************//
	
	// Habilita el tablero de juego.
	public void enable() {
		this.jBoard.setEnabled(true);
	}
	
	// Deshabilita el tablero de juego.
	public void disable() {
		this.jBoard.setEnabled(false);
	}

	// Actualiza el estado del juego, el estado de activación,
	// saca las acciones validas del nuevo estado y repinta el tablero
	// con los nuevos datos.
	public void update(S state, boolean active) {
		this.state = state;
		this.active = active;
		this.validActions = state.validActions(this.gameCtrl.getPlayerId());
		this.jBoard.repaint();
	}
	
	public void setMessageViewer(MessageViewer<S,A> messageViewer) {
		this.infoViewer = messageViewer;
	}

	public void setPlayersInfoViewer(PlayersInfoViewer<S,A> playersInfoViewer) {
		this.playersInfoViewer = playersInfoViewer;
		this.playersInfoViewer.addObserver(this);
	}

	// Permite a la vista establecer el controlador.
	public void setGameController(GUIController<S,A> gameCtrl) {
		this.gameCtrl = gameCtrl;	
	}
	
	@Override
	public void setChatViewer(ChatViewer<S, A> chatViewer) {
		this.chatViewer = chatViewer;
	}
	
	//******************************************************************************************************//
	
	//****************************** MÉTODOS SOBREESCRITOS DEL PADRE ***************************************//
	
	// Lanza este componente como un objeto de GUI.
	@Override
	public void enableWindowMode() {
		super.enableWindowMode();
		super.window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		super.window.setTitle("Window Title");
		super.window.setSize(640,480);
		super.window.setVisible(true);
		super.window.add(this);
	}
	
	// Repinta el tablero cuando el usuario ha seleccionado un color de ficha diferente.
	@Override
	public void colorChanged(int p, Color color) {
		this.jBoard.repaint();
	}
	
	//******************************************************************************************************//
	
 }