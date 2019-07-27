package es.ucm.fdi.tp.visualComponents.PlayersInfo;

import java.awt.Color;
import java.util.List;

import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.mvc.gui.GUIView;

/**
 * PlayersInfoViewer is an abstract class that is also GUIViwew,
 * but with the very specific purpose of managing the colours of
 * players, i.e., allowing to change the color of a player, to consult
 * the color of a layer, to notify observers if a color has been
 * changed, etc. See the color chooser example in the extra
 * package to understand how to implement …
 * 
 * @author Daniel Calle Sanchez
 * @author Manuel Guerrero Moñus
 *
 */
public abstract class PlayersInfoViewer
<S extends GameState<S,A>, A extends GameAction<S,A>> extends GUIView<S,A> {
	
	private static final long serialVersionUID = -408556611759904303L;
	
	// Lista de observadores.
	protected List<PlayersInfoObserver> observers;

	// Este metodo esta vacio por defecto ya que un PlayersInfoViewer no esta 
	// conectado normalmente a otro PlayersInfoViewer.
	public void setPlayersInfoViewer(PlayersInfoViewer<S, A> playersInfoViewer) { }
	
	// Estalbece el numero de jugadores. Inicialmente asigna un color
	// aleatorio a los juegadores usando el iterador retornado por Utils.colorsGenerator. 
	abstract public void setNumberOfPlayer(int n);
	
	// Metodo para consultar el color asignado al jugador.
	abstract public Color getPlayerColor(int p);
	
	/*
	 * Esta clase debería permitar a otros objetos registrar y recivir notificaciones
	 * cuando el color de un jugador cambia. 
	 */
	
	// Los observadores deberían implementar esta interfaz. 
	public interface PlayersInfoObserver {
		public void colorChanged(int p, Color color);
	}
	
	// Añade al observador.
	public void addObserver(PlayersInfoObserver o) { observers.add(o); }
	
	// Notifica a todas las subclases el cambio de color.
	protected void notifyObservers(int p, Color c) {
		for (PlayersInfoObserver o : observers) o.colorChanged(p,c);
	}
	
}