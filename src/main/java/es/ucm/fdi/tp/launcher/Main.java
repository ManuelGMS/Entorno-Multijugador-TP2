package es.ucm.fdi.tp.launcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import es.ucm.fdi.tp.base.console.ConsolePlayer;
import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GamePlayer;
import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.base.player.ConcurrentAiPlayer;
import es.ucm.fdi.tp.base.player.RandomPlayer;
import es.ucm.fdi.tp.base.player.SmartPlayer;
import es.ucm.fdi.tp.dessignPatterns.Mediator.ChatMediator;
import es.ucm.fdi.tp.dessignPatterns.Mediator.CommonWindowMediator;
import es.ucm.fdi.tp.dessignPatterns.Mediator.ControlPanelMediator;
import es.ucm.fdi.tp.games.chess.ChessAction;
import es.ucm.fdi.tp.games.chess.ChessState;
import es.ucm.fdi.tp.games.chess.ChessView;
import es.ucm.fdi.tp.games.dc.DcAction;
import es.ucm.fdi.tp.games.dc.DcState;
import es.ucm.fdi.tp.games.dc.DcView;
import es.ucm.fdi.tp.games.ttt.TttAction;
import es.ucm.fdi.tp.games.ttt.TttState;
import es.ucm.fdi.tp.games.ttt.TttView;
import es.ucm.fdi.tp.games.was.WasAction;
import es.ucm.fdi.tp.games.was.WasState;
import es.ucm.fdi.tp.games.was.WasView;
import es.ucm.fdi.tp.mvc.common.GameTable;
import es.ucm.fdi.tp.mvc.console.ConsoleControllerImp;
import es.ucm.fdi.tp.mvc.console.ConsoleView;
import es.ucm.fdi.tp.mvc.gui.GUIController;
import es.ucm.fdi.tp.mvc.gui.GUIControllerImp;
import es.ucm.fdi.tp.mvc.gui.GUIView;
import es.ucm.fdi.tp.mvc.gui.GameContainer;

/*
 * + Añadir en el método createGame el nuevo juego ( estado inicial ).
 * + Añadir en el método createGameView el nuevo juego ( estado inicial ).
 */

public class Main {
	
	private static GameTable<?, ?> createGame(String gType) {
		
		/*
		 * Crea el GameState de un juego determinado.
		 * GameState es el MODELO.
		 * Contiene:
		 * + Estado inicial.
		 * + Estado actual.
		 * + Observadores del modelo.
		 */
		switch(gType)
		{
			case "dc": return new GameTable<DcState,DcAction>(new DcState());
			case "ttt": return new GameTable<TttState,TttAction>(new TttState(3)); 
			case "was": return new GameTable<WasState,WasAction>(new WasState());
			case "chess": return new GameTable<ChessState,ChessAction>(new ChessState());
			default: return null;
		}
				
	}
	
	private static <S extends GameState<S, A>, A extends GameAction<S,A>> 
	void startConsoleMode(String gType, GameTable<S,A> game, String playerModes[]) {
			
		// Declaramos una lista de jugadores.
		List <GamePlayer> gamePlayers = new ArrayList<>();
		
		// Añadimos los jugadores a la lista.
		for( int i = 0 ; i < playerModes.length ; ++i ) {
			
			// Creamos un tipo de jugador (SMART/RANDOM/CONCURRENT) y
			// le asignamos un nombre.
			GamePlayer p = createPlayer(playerModes[i],"PlayerName-"+(i-1));
			
			if(p == null){
				System.err.println("Invalid type of player: " + playerModes[i]);
				System.exit(1);
			}
			
			// Asignamos al jugador un numero de jugador que mas tarde
			// se usará para saber si es el turno de este durante la partida.
			p.join(i);
	
			// Añadimos el nuevo jugador a la listqa de jugadores.
			gamePlayers.add(p);
			
		}
			
		// Creamos la vista por consola y hacemos que esta se registre como un observador del modelo.
		new ConsoleView<S,A>(game);
		
		// Creamos el controlador pasandole la lista de jugadores y el modelo.
		// Al llamar a run el controlador indica que se da comienzo a la ejecución del programa / juego.
		new ConsoleControllerImp<S,A>(gamePlayers,game).run();
		
	}
	
	// Esta unción crea un tipo de jugador y le asigna un nombre.
	public static GamePlayer createPlayer(String playerType, String playerName) {
		
		switch (playerType) {
			case "manual": return new ConsolePlayer(playerName,new Scanner(System.in));
			case "rand": return new RandomPlayer(playerName);
			case "smart": return new SmartPlayer(playerName, 5);
			default: return null;
		}

	}
	
	// Este método creará la vista específica de un juego.
	//@SuppressWarnings("unchecked")
	public static <S extends GameState<S, A>, A extends GameAction<S,A>> GUIView<S,A> 
	createGameView(GUIController<S,A> ctrl, String gType, GameState<S,A> state, int playerId ){
		
		GUIView<S,A> v = null;
		
		switch(gType) {
			case "dc": v = (GUIView<S, A>) new DcView((DcState)state);			break;
			case "ttt": v = (GUIView<S, A>) new TttView((TttState)state);		break;
			case "was": v = (GUIView<S, A>) new WasView((WasState)state);		break;
			case "chess": v = (GUIView<S, A>) new ChessView((ChessState)state); break;
		}
		
		if(v != null) v.setGameController(ctrl);
		
		return v;
		
	}
	
	private static <S extends GameState<S,A>, A extends GameAction<S,A>>
	void startGUIMode(String gType, final GameTable<S, A> game, String[] otherArgs) {
		
		ChatMediator<S,A> chatMediator = new ChatMediator<>();
		
		ControlPanelMediator<S,A> controlPanelMediator = new ControlPanelMediator<>();
		
		CommonWindowMediator<S,A> commonWindowMediator = new CommonWindowMediator<>();
		
		game.setControlPanelMediator(controlPanelMediator);
		
		// Cada jugador pueder ser Manual, Random o Smart.
		// Nuestro juego sera encapsulado y lanzado desde un gestor de hebras (executor).
		
		for( int i = 0 ; i < game.getState().getPlayerCount() ; ++i ) 
		{
			
			GamePlayer p1 = new RandomPlayer("Player - " + i);
			ConcurrentAiPlayer p2 = new ConcurrentAiPlayer("Concurrent Player"); 
			p1.join(i);
			p2.join(i);			
			
			final int j = i;
			
			ExecutorService executor = Executors.newSingleThreadExecutor();
			
			try {
					
				//***********************************************************************************************************************************************//
				
				GUIController<S,A> ctrl = new GUIControllerImp<S,A>(j,p1,p2,game,chatMediator,executor);
					
				GUIView<S,A> gameView = createGameView(ctrl,gType,game.getState(),j);
		
				GameContainer<S,A> container = new GameContainer<S,A>(gameView,ctrl,game,commonWindowMediator,chatMediator,controlPanelMediator,j,game.getState());
				
				//***********************************************************************************************************************************************//
				
				commonWindowMediator.commonWindowAddClient(ctrl.getPlayerId());
				
				container.enableWindowMode();
					
				executor.submit(()-> game.start() );
				
				//***********************************************************************************************************************************************//
				
			} catch( Exception e ) {
				System.err.println(e.getMessage());
				System.exit(1);
			}
			
		}
		
		commonWindowMediator.commonWindowShow();
		
	}

	// Describimos como ha de usarse el método Main.
	private static void usage() {
		System.out.println("Syntax: Game_Name[ttt/was] Game_Mode[console/gui] player1 player2 ...[manual/random/smart] ...");
	}
	
	public static void main(String[] args) {
				
		// Args:				0	 1	  2		  3
		// Sintaxis de entrada: game mode player1 player2 ...
		
		 // Comprobamos que la llamada al main tenga por lo menos tres argumentos.
		if (args.length < 2) {
			
			// Descriptor de uso del Main.
			usage();
			
			// Salimos de la aplicación.
			System.exit(1);
		}
	
		// Crea un GameState para un juego determinado.
		// ( Obtenemos el MODELO ).
		GameTable<?,?> game = createGame(args[0]);
		
		// Si no se ha podido crear el juego.
		if (game == null) {
			
			// Indicamos que  el juego no es válido.
			System.err.println("Invalid game");
			
			// Descriptor de uso del Main.
			usage();
			
			// Salimos de la aplicación.
			System.exit(1);
		}
		
		// Capturamos desde el argumento 2 (player 1) al N-ésimo (player N).
		String[] otherArgs = Arrays.copyOfRange(args, 2, args.length);
		
		// Modo de juego: CONSOLE o GUI.
		switch (args[1]) 
		{
			case "console":
				
				// Iniciamos el modo de juego por consola para un juego determinado.
				// Argumentos: Nombre del juego, esado inicial y otros argumentos.
				startConsoleMode(args[0],game,otherArgs);
		
			break;
			case "gui":
			
				// Iniciamos el modo de juego por GUI para un juego determinado.
				// Argumentos: Nombre del juego, esado inicial y otros argumentos.
				startGUIMode(args[0],game,otherArgs);
			
			break;
			default:
			
				// Indicamos que el modo de juego no es valido.
				System.err.println("Invalid view mode: " + args[1]);
			
				// Descriptor de uso del Main.
				usage();
			
				// Salimos de la aplicación.
				System.exit(1);
		}
		
	}
	
}
