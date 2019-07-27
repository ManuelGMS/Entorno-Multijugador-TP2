package es.ucm.fdi.tp.mvc.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javax.swing.SwingUtilities;
import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GamePlayer;
import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.base.player.ConcurrentAiPlayer;
import es.ucm.fdi.tp.dessignPatterns.Mediator.ChatMediator;
import es.ucm.fdi.tp.dessignPatterns.ObserverObservable.SimulationObservable;
import es.ucm.fdi.tp.dessignPatterns.ObserverObservable.SimulationObserver;
import es.ucm.fdi.tp.dessignPatterns.ObserverObservable.SmartMoveObservable;
import es.ucm.fdi.tp.dessignPatterns.ObserverObservable.SmartMoveObserver;
import es.ucm.fdi.tp.games.dc.DcAction;
import es.ucm.fdi.tp.games.dc.DcState;
import es.ucm.fdi.tp.mvc.common.GameTable;
import es.ucm.fdi.tp.mvc.common.PlayerMode;

/*
 * Esta clase extiende a que es una clase abstracta a modod de interfaz que a su vez extiende del controlador,
 * luego esta clase es el controlador del juego en el modo gráfico.
 */

public class GUIControllerImp<S extends GameState<S,A>, A extends GameAction<S,A>> 
implements SmartMoveObservable , SimulationObservable , GUIController<S,A> {
	
	private volatile int playerId;
	private volatile boolean activo;
	private volatile GameTable<S,A> game;
	private volatile GamePlayer randPlayer;
	private volatile PlayerMode playerMode;
	private volatile ExecutorService executor;
	private volatile Future<?> smartPlayerFuture;
	private volatile ConcurrentAiPlayer smartPlayer;
	private volatile ArrayList<SmartMoveObserver> smartObserver;

	//******************** NUEVOS ELEMENTOS **********************************************//
	
	private volatile ExecutorService[] simulationPools;
	
	private volatile ChatMediator<S,A> chatMediator;
	
	private volatile ArrayList<SimulationObserver> simulationObserver;
	
	//***********************************************************************************//
	
	public GUIControllerImp(int playerId, GamePlayer randPlayer, ConcurrentAiPlayer smartPlayer, GameTable<S, A> game, 
			ChatMediator<S,A> chatMediator, ExecutorService executor) {
		
		this.game = game;
		this.playerId = playerId;
		this.executor = executor;
		this.randPlayer = randPlayer;
		this.smartPlayer = smartPlayer;
		this.playerMode = PlayerMode.MANUAL;
		this.smartObserver = new ArrayList<SmartMoveObserver>();
		
		//*******************************************************************************//
		
		this.chatMediator = chatMediator;
		
		this.simulationObserver = new ArrayList<SimulationObserver>();
		
		//*******************************************************************************//
		
	}

	//*************** GUI CONTROLLER ****************************************************//
	
	// Si cambiamos el tipo de jugador tenemos que decidir el tipo del movimiento nuevo.
	public void changePlayerMode(PlayerMode mode) {
		
		this.playerMode = mode;
		
		if( playerMode != PlayerMode.MANUAL && !this.game.getState().isFinished() && activo) {
			
			if( this.game.getState().getTurn() == this.playerId ) {
				
				this.decideMakeAutomaticMove();
				
			}
			
		}
		
	}
	
	// Este método es llamado por la vista, cuando esta obtenga una notificación del modelo.
	// Es importante obtener el turno actual de e.getState() y no de game.getState().
	public void handleEvent(GameEvent<S,A> e) {
		
		switch (e.getType()) {
			case Start:
				
				activo = true;
				stopSmartPlayer();
				
			break;
			case Change:

				if( e.getState().getTurn() == this.playerId ) {
					
					if( e.getState().isFinished() ) {
						
						this.stopGame();
						
					} else if( playerMode != PlayerMode.MANUAL ) {
						
						this.decideMakeAutomaticMove();
						
					}
					
				}
				
			break;
			case Error:
			break;
			case Stop:
				
				stopSmartPlayer();
				activo = false;
				
			break;
			case Info:
				
				
				
			break;
			case Chat:
								
				this.chatMediator.talkToPlayers(e.toString());
				
			break;
			default:
			break;
		}
	}
	
	// Realizamos un movimiento en función del modo seleccionado.
	private void decideMakeAutomaticMove() {
		
		if(this.playerMode == PlayerMode.RANDOM) 
			
			makeRandomMove();
		
		else if(this.playerMode == PlayerMode.SMART) 
			
			makeSmartMove();
		
	}
	
	// Llama al método execute sobre el modelo.
	private void makeMove(A a){
		
		this.game.execute(a);
		
	}
	
	// Solicitamos la realización de un movimiento aleatorio.
	@Override
	public void makeRandomMove() {
		
		if( playerId == game.getState().getTurn() ) {
			
			A action = this.randPlayer.requestAction(game.getState());
			
			makeMove(action);
			
		}
		
	}
	
	// Realizamos la ejecución del algoritmo MaxMin dentro de un executor
	// para luego notificar el resultado del jugador inteligente a los jugadores.
	public Future<?> makeSmartMove() {

		if( smartPlayerFuture == null || smartPlayerFuture.isDone() ) {
		
			smartPlayerFuture = executor.submit( () -> {

				for( SmartMoveObserver smrObs : smartObserver ) 
					
					smrObs.onStart();
				
				long time1 = System.currentTimeMillis();
				
				A action = smartPlayer.requestAction(game.getState());
				
				long time2 = System.currentTimeMillis();
				
				for( SmartMoveObserver smrObs : smartObserver ) 
					
					smrObs.onEnd(action!=null,time2-time1,smartPlayer.getEvaluationCount(),smartPlayer.getValue());

				game.execute(action);
				
				
			}); 
			
			return smartPlayerFuture;
			
		} else {

			return null;

		}
		
	}
	
	// Establecemos el máximo numero de hebras para la jugada del smartPlayer.
	@Override
	public void smartPlayerConcurrencyLevel(int nThreads) {
		
		smartPlayer.setMaxThreads(nThreads);
		
	}

	// Límite de tiempo para que el jugador inteligente piense su jugada.
	@Override
	public void smartPlayerTimeLimit(int timeOut) {
		
		smartPlayer.setTimeout(timeOut);
		
	}

	// Detiene la hebra del jugador inteligente.
	@Override
	public void stopSmartPlayer() {
		
		if( smartPlayerFuture != null ) {
		
			if( !smartPlayerFuture.isCancelled() )
		
				smartPlayerFuture.cancel(true);
		
		}
		
	}

	// Añade un observador del jugador inteligente.
	@Override
	public void addSmartPlayerObserver(SmartMoveObserver o) {
		
		smartObserver.add(o);
		
	}
	
	// Añade un observador a la simulacion.
	@Override
	public void addSimulationObserver(SimulationObserver o) {
		
		simulationObserver.add(o);
		
	}
	
	// Obtiene el modod de juego del jugador actual.
	@Override
	public PlayerMode getPlayerMode() {
		
		return this.playerMode;
		
	}
	
	// Obtiene el Id del jugador actual.
	@Override
	public int getPlayerId() {
		
		return this.playerId;
		
	}
	
	@Override
	public void stopSimulation() {
		
		if(simulationPools != null) {
		
			for(ExecutorService es: simulationPools)
				
				if(!es.isShutdown())
				
					es.shutdownNow();
		
		}
		
	}
	
	@Override
	public void makeSimulation(S initialState, int pools, int matchesByPool, int maxActions) {
		
		Thread sim = new Thread( () ->  { 
		
			boolean end = false;
			
			simulationPools = new ExecutorService[pools];
		
			for(int i = 0 ; i < pools ; ++i)
				
				simulationPools[i] = Executors.newCachedThreadPool();
			
			for(int i = 0 ; i < matchesByPool && !end ; ++i) {
				
				for(int j = 0 ; j < simulationPools.length && !end ; ++j) {
					
					if(!simulationPools[j].isShutdown()) {
					
						simulationPools[j].submit( () -> { 
							
							int result = randomGame(initialState, maxActions);
							
							for(SimulationObserver so : simulationObserver)
							
								SwingUtilities.invokeLater( () -> so.notifyMatchResult(result) );
							
						} );
					
					} else {
						
						end = true;
						
					}
						
				}
				
			}
			
		} );

		sim.start();

	}
	
	private int randomGame(S initialState, int maxActions) {
		
		Random r = new Random();
		
		S currState = initialState;
		
		List<A> validActions = currState.validActions(currState.getTurn());
		
		for(int i = 0 ; i < maxActions && !validActions.isEmpty() && !currState.isFinished() ; ++i) {
						
			A rndAction = validActions.get(r.nextInt(validActions.size()));
			
			currState = rndAction.applyTo(currState);
			
			validActions = currState.validActions(currState.getTurn());
			
		}
		
		return currState.getWinner();
		
	}
	
	//***********************************************************************************//

	//******************** CONTROLLER CORE **********************************************//
	
	@Override
	public void run() {
		
		startGame();
		
	}

	@Override
	public void stopGame() {
		
		this.game.stop();
		
	}

	@Override
	public void startGame() {
		
		this.game.start();
		
	}
	
	// Reinicia el juego.
	@Override
	public void restartGame() {
			
		if(!game.getState().isFinished() && activo)
				
			this.stopGame();
			
		this.startGame();
			
	}
	
	// El movimiento manual puede realizarse si es el turno del jugador correspondiente.
	@Override
	public void makeManualMove(A a) {
			
		if( playerId == game.getState().getTurn() ) 
			
			makeMove(a);
		
	}

	//***********************************************************************************//
	
}
