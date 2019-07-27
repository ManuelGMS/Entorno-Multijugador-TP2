package es.ucm.fdi.tp.mvc.gui;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.visualComponents.Chat.ChatViewer;
import es.ucm.fdi.tp.visualComponents.MessageViewer.MessageViewer;
import es.ucm.fdi.tp.visualComponents.PlayersInfo.PlayersInfoViewer;

/*
 * Clase abstracta.
 * 
 * Esta es la clase superior de todas las VISTAS, es una clase que extiende de JPanel y tiene una
 * ventana (JFrame) como atributo. Luego cualquier clase que extienda de esta puede ser dibujada
 * (representada) en una ventana.
 * 
 * En esta clase han de añadirse todos los setter necesarios para que cualquier componente
 * gráfica pueda colaborar con otra si le es necesario.
 * 
 * Las clases que se especializen como VISTA habrán de implementar los siguientes métodos.
 * Métodos comunes entre las vistas:
 * + update
 * + enable --> Habilita los componentes visuales.
 * + disable --> Deshabilita los componentes visuales.
 * + setMessageViewer --> 
 * + setPlayersInfoViewer
 * + setGameController
 */

public abstract class GUIView<S extends GameState<S,A>, A extends GameAction<S,A>> extends JPanel {
	
	private static final long serialVersionUID = 4380717645581676426L;
	
	protected JFrame window;
	
	public void enableWindowMode() {
		this.window = new JFrame("Window Title");
		this.window.setContentPane(this);
		this.window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.window.setVisible(true);
	}
	
	public void disableWindowMode() {
		this.window.dispose(); 
		this.window = null;
	}

	public JFrame getWindow() {
		return window;
	}
	
	public void setTitle(String title) {
		if(this.window != null) {
			this.window.setTitle(title);
		} else {
			this.setBorder(BorderFactory.createTitledBorder(title));
		}
	}
	
	// Metodos para habilitar / deshabilitar un componente.
	public abstract void enable();
	public abstract void disable();
	
	// Actualizar el componente cuando tengamos un nuevo estado.
	// Puede implementarse para recibir un estado o un GameEvent<S,A> e. 
	public abstract void update(S state, boolean active);
	
	// Podemos asignarlo a un componente para que muestre mensajes.
	public abstract void setMessageViewer(MessageViewer<S,A> infoViewer);
	
	// Podemos aasignarlo a un componente para consultar los colores de los juegadores.
	public abstract void setPlayersInfoViewer(PlayersInfoViewer<S,A> playersInfoViewer);
	
	// Este metodo es usado para pasar el controlador a determinada componente.
	public abstract void setGameController(GUIController<S,A> gameCtrl);
	
	//*********************** NUEVOS COMPONENTES GRÁFICOS *******************************//
	
	public abstract void setChatViewer(ChatViewer<S,A> chatViewer);
	
	//***********************************************************************************//
	
}
