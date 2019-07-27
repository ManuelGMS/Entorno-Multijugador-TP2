package es.ucm.fdi.tp.launcher;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import es.ucm.fdi.tp.base.console.ConsolePlayer;
import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GamePlayer;
import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.base.player.RandomPlayer;
import es.ucm.fdi.tp.base.player.SmartPlayer;
import es.ucm.fdi.tp.games.ttt.TttState;
import es.ucm.fdi.tp.games.was.WasState;

/**
 * Class Main
 * @author Daniel Calle Sánchez
 * @author Manuel Guerrero Moñús
 * @version 1.0 08/03/2017
 */
public class MainPr4 {
	
	/**
	 * Check that the arguments are correct
	 * @param args
	 * 			argument of main
	 * @return
	 * 			true if the arguments are valid or false if they are not valid
	 */
	public static boolean isValidArguments(String... args) {
		
		//Check that the number of arguments is correct
		if (args.length < 3 || args.length > 3)
			return false;
	
		if (createInitialState(args[0])==null)
			return false;
		
		for(int i=1;i<args.length;i++)
			if (createPlayer(args[0],args[i],"Player " + i)==null)
				return false;
		
		return true;
			
	}
	
	/**
	 * Creates a GameState
	 * 
	 * @param gameName
	 *            name of the game order
	 * @return GameState of the game passed by argument or if not exist return
	 *         null
	 */
	public static GameState<?, ?> createInitialState(String gameName) {
		//Check the type of game passed by the argument, if it exists return the corresponding GameState
		//If no return null
		switch (gameName) {

		case "ttt":
			return new TttState(3);

		case "was":
			return new WasState();

		default:
			return null;
		}
	}

	/**
	 * Creates GamePlayer
	 * 
	 * @param gameName
	 *            kind of game
	 * @param playerType
	 *            kind of player
	 * @param playerName
	 *            name of player
	 * @return a GamePlayer or null if not exist
	 */
	public static GamePlayer createPlayer(String gameName, String playerType, String playerName) {
		//Check the type of player passed by the argument, if it exists return the corresponding GamePlayyer
		//If no return null
		switch (playerType) {
		case "console":
			Scanner sc = new Scanner(System.in);
			return new ConsolePlayer(playerName, sc);
		case "rand":
			return new RandomPlayer(playerName);
		case "smart":
			return new SmartPlayer(playerName, 5);

		default:
			return null;
		}

	}

	/**
	 * Initialized a game with players
	 * 
	 * @param initialState
	 *            initial GameState
	 * @param players
	 *            list of GamePlayer
	 * @return integer of the winner player
	 */
	public static <S extends GameState<S, A>, A extends GameAction<S, A>> int playGame(GameState<S, A> initialState,
			List<GamePlayer> players) {
		int playerCount = 0;
		for (GamePlayer p : players) {
			p.join(playerCount++); // welcome each player, and assign
									// playerNumber
		}
		@SuppressWarnings("unchecked")
		S currentState = (S) initialState;
		
		//Print the initial state
		System.out.println("After action:\n" + currentState);
		while (!currentState.isFinished()) {
			// request move
			A action = players.get(currentState.getTurn()).requestAction(currentState);
			// apply move
			currentState = action.applyTo(currentState);
			System.out.println("After action:\n" + currentState);

			if (currentState.isFinished()) {
				// game over
				String endText = "The game ended: ";
				int winner = currentState.getWinner();
				if (winner == -1) {
					endText += "draw!";
				} else {
					endText += "player " + (winner) + " (" + players.get(winner).getName() + ") won!";
				}
				System.out.println(endText);
			}
		}
		return currentState.getWinner();
	}

	/**
	 * Repeatedly plays a game-state with a vs b
	 * 
	 * @param initialState object GameState
	 * @param players object List of GamePlayers
	 * @param times integer of times that the method is execute
	 */
	public static void match(GameState<?, ?> initialState, List <GamePlayer> players, int times) {
		//Create a array of results of the game
		int results [] = new int[players.size()];
		int winner;
		//execute the game times repetitions and save the results
		for (int i = 0; i < times; i++) {
			winner = playGame(initialState, players);
			++results[winner];
		}
		
		//print the results
		System.out.print("Result: ");
		
		for(int i = 0;i < results.length-1;i++)
			System.out.print(results[i] + " for " + players.get(i).getName() + " vs ");
		
		System.out.println(results[results.length-1] + " for " + players.get(players.size()-1).getName());
	}

	/**
	 * Main method.
	 * 
	 * @param args
	 */
	public static void main(String... args) {
		//informacion del programa
		System.out.println("Syntax: game player1 player2");
		
		//Check that the number of arguments is correct
		if (!isValidArguments(args)) {
			System.err.println("Error: The arguments are not correct");
			System.exit(1);
		}
		else {
			//Create the initial state of the game
			GameState<?, ?> gameState = createInitialState(args[0]);
			//Get all players
			List <GamePlayer> gamePlayers = new ArrayList<>();
			for(int i=1;i<args.length;i++)
				gamePlayers.add(createPlayer(args[0],args[i],"PlayerName-" + (i-1)));
			//execute the game with 10 repetitions
			match(gameState,gamePlayers,10);
			
		}


	}
}
