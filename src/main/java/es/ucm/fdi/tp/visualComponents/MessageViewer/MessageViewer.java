package es.ucm.fdi.tp.visualComponents.MessageViewer;

import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.mvc.gui.GUIView;

/*
 * Esta clase extiende de GuiView, por tanto es una vista y extiende a sus métodos.
 * Por otro lado esta clase define un interfaz que ha de tener el componente visual MessageViewer.
 */

// Esta clase abstracta esta relacionada con GUIView, pero con el proposito
// especifico de mostrar mensajes al usuario, etc.

public abstract class MessageViewer<S extends GameState<S,A>, A extends GameAction<S,A>> extends GUIView<S,A> {
	

	private static final long serialVersionUID = 2152520803715714183L;
	
	// Este método esta vacio por defecto ya que un MessageViewer
	// no está conectado a otro MessageViewer normalmente.
	public void setMessageViewer(MessageViewer<S,A> infoViewer) { }
	
	// Metodos para añadir mensajes a un MessageViewer.
	// setContent borra el contenido y despues añade  un mensaje.
	// addCOntente conserva el contenido y añade un mensaje.
	abstract public void setContent(String msg);
	abstract public void addContent(String msg);

}